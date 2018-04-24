package com.cnpc.pms.base.configuration;

import java.beans.Introspector;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;

import com.cnpc.pms.base.configuration.model.IBeanPackage;
import com.cnpc.pms.base.configuration.model.PMSApplication;
import com.cnpc.pms.base.configuration.model.PMSBean;
import com.cnpc.pms.base.configuration.model.PMSModule;
import com.cnpc.pms.base.configuration.model.PMSPackage;
import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.EntityHelper;
import com.cnpc.pms.base.exception.UtilityException;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.util.ConfigurationUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.base.util.StrUtil;

/**
 * Inject beans according to pmsconfig.xml file
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Xiang ZhongMing
 * @since 2011-4-25
 * @deprecated
 */
@Deprecated
public class PMSConfigHandler {

	/** The Constant DEFAULT_CONFIG_LOCATION. */
	public static final String DEFAULT_CONFIG_LOCATION = "/module/*.xml";

	public static final String BUSINESS_LOGIC_BEAN_POSTFIX = "Manager";
	public static final String DATA_ACCESS_OBJECT_POSTFIX = "DAO";
	private static final int BUSINESS_LOGIC_CUT_OFF = BUSINESS_LOGIC_BEAN_POSTFIX.length();
	private static final int DATA_ACCESS_CUT_OFF = DATA_ACCESS_OBJECT_POSTFIX.length();

	private static final String DEFAULT_DAO_BEAN = "basedao";
	private static final String DEFAULT_SESSION_FACTORY = "sessionFactory";

	/* properties add to manager beans */
	private static final String PROPERTY_DAO = "dao";
	private static final String PROPERTY_ENTITY_CLASS = "entityClass";

	/** The Constant log. */
	final static Logger log = LoggerFactory.getLogger(PMSConfigHandler.class);

	/** The cached files. */
	private static Map<Resource, Long> cachedFiles = new HashMap<Resource, Long>();

	/** Auto Registered Manager Beans */
	private static Map<String, GenericBeanDefinition> configedManagerBeans = new HashMap<String, GenericBeanDefinition>();

	/** The cached files count. */
	private static int cachedFilesCount = 0;

	private static abstract class Registor {
		PMSPackage pmsPackage;
		IBeanPackage detailPackage;
		String type;

		Registor(PMSPackage pmsPackage) {
			this.pmsPackage = pmsPackage;
		}

		public String getPackageId() {
			return pmsPackage.getId();
		}

		public String getType() {
			return type;
		}

		public boolean shouldSkipBean(String fullClassName) {
			PMSBean bean = detailPackage.getSpecificBeans().get(fullClassName);
			if (bean != null && bean.isExcluded()) {
				return true;
			}
			return false;
		}

		public String getInterfacePackage() {
			return detailPackage.getInterfacePackage();
		}

		public String getImplementPackage() {
			return detailPackage.getImplementPackage();
		}

		public boolean hasConfig() {
			return detailPackage != null;
		}

		public abstract void register(String fullClassName, String implFullClassName);
	}

	private static class DaoRegistor extends Registor {
		DaoRegistor(PMSPackage pmsPackage) {
			super(pmsPackage);
			detailPackage = pmsPackage.getDaoPackage();
			type = "DAO Beans";
		}

		@Override
		public void register(String fullClassName, String implFullClassName) {
			registerSimpleDAO(fullClassName, implFullClassName, pmsPackage);
		}

	};

	private static class ManagerRegistor extends Registor {

		ManagerRegistor(PMSPackage pmsPackage) {
			super(pmsPackage);
			detailPackage = pmsPackage.getManagerPackage();
			type = "Manager Beans";
		}

		@Override
		public void register(String fullClassName, String implFullClassName) {
			registerManager(fullClassName, implFullClassName, pmsPackage);
		}
	}

	/**
	 * Initial pms config.
	 */
	public synchronized static void initialPMSConfig() {
		Resource[] resources = ConfigurationUtil.getAllResources(DEFAULT_CONFIG_LOCATION);
		if (resources != null) {
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				log.info("Loading auto register beans from {}", resource);
				registerBeans(resource);
			}
		}
		cachedFilesCount = cachedFiles.size();
		log.debug("cached module config files: {}", cachedFilesCount);
		log.info("Start to generate default managers for those entities with no manager defined");
		registerManagerByEntity();
		log.info("After auto-register. Start to print out the beans:");
		SpringHelper.printBeans();
		// int i = 1;
		// for (String s : SpringHelper.getApplicationContext().getBeanDefinitionNames()) {
		// log.info("|-{}, {} = {}", new Object[] {i++, s, SpringHelper.getBean(s) });
		// }
		// log.info("\\- There are {} auto-registered beans.", SpringHelper.getApplicationContext()
		// .getBeanDefinitionCount());
	}

	/**
	 * Register beans in spring context.
	 * 
	 * @param resource
	 *            the resource
	 */
	private static void registerBeans(Resource resource) {
		try {
			PMSApplication app =  (PMSApplication) ConfigurationUtil.parseXMLObject(PMSApplication.class, resource);
			for (PMSModule module : app.getModules()) {
				log.info("Start to auto register module[{}] ", module.getId());
				for (PMSPackage pkg : module.getPackages()) {
					log.debug("Start to auto register package[{}] ", pkg.getId());
					registerToSpring(new PMSConfigHandler.DaoRegistor(pkg));
					registerToSpring(new PMSConfigHandler.ManagerRegistor(pkg));
				}
			}
			if (resource.getURL().getProtocol().equals("file")) {
				cachedFiles.put(resource, resource.getFile().lastModified());
			}
		} catch (IOException e) {
			log.error("Could not load config file from {}, reason:", resource, e.getMessage());
		} catch (UtilityException e) {
			log.error("Fail to digester config file {}, reason:", resource, e.getMessage());
		}
	}

	/**
	 * Register dao beans.
	 * 
	 * @param daoPackage
	 *            the dao package
	 * @param ctx
	 *            the ctx
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void registerToSpring(Registor registor) {
		if (registor.hasConfig() == false) {
			log.debug("PMSPackage [{}] didn't define {}.", registor.getPackageId(), registor.getType());
			return;
		}
		String packageName = registor.getInterfacePackage();
		String implPkgName = registor.getImplementPackage();
		if (implPkgName == null) {
			implPkgName = packageName + ".impl";
		}
		Resource[] resources = ConfigurationUtil.getAllResources(getPackageLocation(packageName));
		for (Resource resource : resources) {
			String fileName = resource.getFilename();
			String className = fileName.substring(0, fileName.indexOf('.'));
			String fullClassName = packageName + "." + className;
			if (registor.shouldSkipBean(fullClassName)) {
				log.debug("Skip excluded interface: {}", fullClassName);
				continue;
			}
			try {
				Class<?> clazz = ClassUtils.forName(fullClassName);
				if (clazz.isAnonymousClass() == true || clazz.isInterface() == false) {
					continue;
				}
				String implFullClassName = getImplements(clazz, implPkgName);
				registor.register(fullClassName, implFullClassName);
			} catch (Exception e) {
				log.error("Fail to register DAO bean {} from: " + implPkgName, fullClassName, e);
				continue;
			}
		}
	}

	/**
	 * Get the implement class of given interface
	 * 
	 * @param interfaceClass
	 * @param implPkgName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws LinkageError
	 */
	private static String getImplements(Class<?> interfaceClass, String implPkgName) throws ClassNotFoundException,
			LinkageError {
		log.debug("Start to get Implements of {} from {}", interfaceClass, implPkgName);
		String result = null;
		Resource[] implResources;
		// We will guess the implement class first
		implResources = ConfigurationUtil.getAllResources(getAlmostPackageLocation(implPkgName, interfaceClass));
		result = getImplementsWithResources(interfaceClass, implPkgName, implResources);
		// If the default implement classname failed, we will iterate the path
		if (result == null) {
			implResources = ConfigurationUtil.getAllResources(getPackageLocation(implPkgName));
			result = getImplementsWithResources(interfaceClass, implPkgName, implResources);
		}
		if (result == null) {
			throw new ClassNotFoundException("Fail to Get any implenment in the given path.");
		} else {
			return result;
		}
	}

	private static String getImplementsWithResources(Class<?> interfaceClass, String implPkgName,
			Resource[] implResources) throws ClassNotFoundException, LinkageError {
		for (Resource implResource : implResources) {
			String implFileName = implResource.getFilename();
			String implClassName = implFileName.substring(0, implFileName.indexOf('.'));
			String implFullClassName = implPkgName + "." + implClassName;
			Class<?> implClass = ClassUtils.forName(implFullClassName);
			if (implClass.isAnonymousClass() || implClass.isMemberClass()) {
				log.debug("Skip Anonymous or Member Class {}", implClass);
				continue;
			}
			if (ClassUtils.getAllInterfacesForClassAsSet(implClass).contains(interfaceClass)) {
				log.debug("Class {} has interfaces: {}", implFullClassName, interfaceClass);
				return implFullClassName;
			} else {
				log.debug("Suppose but not a [{}] implenment: {}", interfaceClass, implClass);
			}
		}
		log.warn("Fail to Get a [{}] implenment in path [{}].", interfaceClass, implPkgName);
		return null;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Deprecated
	private static void registerDAO(String fullClassName, String implFullClassName) {
		RuntimeBeanReference refSessionFactory = new RuntimeBeanReference("sessionFactory");
		PropertyValue pvSessFactory = new PropertyValue("sessionFactory", refSessionFactory);

		GenericWebApplicationContext ctx = (GenericWebApplicationContext) SpringHelper.getApplicationContext();
		GenericBeanDefinition bdDaoTarget = new GenericBeanDefinition();
		bdDaoTarget.setBeanClassName(implFullClassName);
		bdDaoTarget.getPropertyValues().addPropertyValue(pvSessFactory);
		ctx.registerBeanDefinition(fullClassName + "Target", bdDaoTarget);

		TypedStringValue proxyInterfacesValue = new TypedStringValue(fullClassName);
		PropertyValue pvProxyInterfaces = new PropertyValue("proxyInterfaces", proxyInterfacesValue);

		TypedStringValue interceptorName = new TypedStringValue("PMSHibernateInterceptor");
		TypedStringValue targetName = new TypedStringValue(fullClassName + "Target");
		ManagedList listInterceptorNames = new ManagedList(2);
		listInterceptorNames.add(interceptorName);
		listInterceptorNames.add(targetName);
		PropertyValue pvInterceptorNames = new PropertyValue("interceptorNames", listInterceptorNames);

		GenericBeanDefinition bdDaoProxy = new GenericBeanDefinition();
		bdDaoProxy.setBeanClassName("org.springframework.aop.framework.ProxyFactoryBean");
		bdDaoProxy.getPropertyValues().addPropertyValue(pvProxyInterfaces);
		bdDaoProxy.getPropertyValues().addPropertyValue(pvInterceptorNames);
		ctx.registerBeanDefinition(fullClassName, bdDaoProxy);
		log.debug("Register DAO bean:{}", fullClassName);
	}

	private static void registerSimpleDAO(String fullClassName, String implFullClassName, PMSPackage pmsPackage) {
		GenericWebApplicationContext ctx = (GenericWebApplicationContext) SpringHelper.getApplicationContext();
		if (ctx.containsBean(fullClassName)) {
			log.debug("DAO bean {} already registered.", fullClassName);
		} else {
			String sessionFactoryBeanName = null;
			PMSBean bean = pmsPackage.getManagerPackage().getSpecificBeans().get(fullClassName);
			if (pmsPackage.isNoEntity() == false && (bean == null || bean.isNoEntity() == false)) {
				String entityName = getEntityNameFromDAO(fullClassName);
				sessionFactoryBeanName = getAlterDSBeanName(DEFAULT_SESSION_FACTORY, entityName);
			}
			if (sessionFactoryBeanName == null) {
				sessionFactoryBeanName = DEFAULT_SESSION_FACTORY;
			}
			RuntimeBeanReference refSessionFactory = new RuntimeBeanReference(sessionFactoryBeanName);
			PropertyValue pvSessFactory = new PropertyValue("sessionFactory", refSessionFactory);
			GenericBeanDefinition bdDaoTarget = new GenericBeanDefinition();
			bdDaoTarget.setBeanClassName(implFullClassName);
			bdDaoTarget.getPropertyValues().addPropertyValue(pvSessFactory);
			ctx.registerBeanDefinition(fullClassName, bdDaoTarget);
			log.debug("Register DAO bean:{}", fullClassName);
		}
	}

	private static void registerManager(String fullClassName, String implFullClassName, PMSPackage pmsPackage) {
		String entityName = null;
		PMSBean bean = pmsPackage.getManagerPackage().getSpecificBeans().get(fullClassName);
		if (pmsPackage.isNoEntity() == false && (bean == null || bean.isNoEntity() == false)) {
			entityName = getEntityName(fullClassName);
		}
		String beanName = SpringHelper.getShortNameAsProperty(fullClassName);
		GenericWebApplicationContext ctx = (GenericWebApplicationContext) SpringHelper.getApplicationContext();
		String daoBeanName = null;
		if (pmsPackage.getDaoPackage() != null) {
			String daoName = pmsPackage.getDaoPackage().getInterfacePackage() + "." + getDaoBeanName(fullClassName);
			if (ctx.isBeanNameInUse(daoName)) {
				log.debug("Find Dao Bean [{}] for {}", daoName, implFullClassName);
				daoBeanName = daoName;
			}
		}
		if (daoBeanName == null) {
			daoBeanName = getDefaultDaoBeanName(entityName);
		}
		innerRegisterManager(beanName, fullClassName, implFullClassName, daoBeanName, entityName);
	}

	/**
	 * Generate default Manager Instances for Entities without Manager
	 */
	private static void registerManagerByEntity() {
		Map<String, Class<?>> map = EntityHelper.getEntityMapping();
		for (String key : map.keySet()) {
			String managerBeanName = getManagerBeanName(key);
			if (configedManagerBeans.containsKey(managerBeanName) == false) {
				try {
					SpringHelper.getBean(managerBeanName);
					log.debug("Get Register Manager of [{}] : {}", key, managerBeanName);
				} catch (BeansException e) {
					log.debug("Entity[{}] doesn't have manager.", key);
					String daoBeanName = getDefaultDaoBeanName(key);
					if (daoBeanName == null) {
						log.error("Fail to get AlternativeDS Entity[{}]'s TargetDS.", key);
						continue;
					}
					innerRegisterManager(managerBeanName, IManager.class.getCanonicalName(),
							BaseManagerImpl.class.getCanonicalName(), daoBeanName, key);
					log.debug("Auto-generated Entity[{}]'s manager. ", key);
				}
			} else {
				// log.debug("Get Config Manager of [{}] : {}", key, managerBeanName);
			}
		}
	}

	/**
	 * @see registerManager
	 * @see registerManagerByEntity
	 * @param entityClass
	 * @return
	 */
	private static String getDefaultDaoBeanName(String entityName) {
		return getAlterDSBeanName(DEFAULT_DAO_BEAN, entityName);
	}

	private static String getAlterDSBeanName(String basicBeanName, String entityName) {
		if (StrUtil.isEmpty(entityName)) {
			return basicBeanName;
		}
		String beanName = basicBeanName;
		String suffix = getDataSourceSuffix(entityName);
		if (StrUtil.isNotBlank(suffix)) {
			beanName = beanName + "_" + suffix;
		}
		return beanName;
	}

	private static String getDataSourceSuffix(String entityName) {
		String suffix = null;
		Class<?> entityClass;
		try {
			entityClass = EntityHelper.getClass(entityName);
			AlternativeDS ds = entityClass.getAnnotation(AlternativeDS.class);
			if (ds != null) {
				suffix = ds.source();
			}
			// boolean alternativeDS =
			// ClassUtils.getAllInterfacesForClassAsSet(entityClass).contains(IAlternativeDS.class);
			// if (alternativeDS) {
			// suffix = ((IAlternativeDS) entityClass.newInstance()).getTargetDS();
			// }
		} catch (ClassNotFoundException ex1) {
			log.error("Fail to find the Entity: [{}].", entityName);
		} catch (Exception e) {
			log.error("Fail to get AlternativeDS Entity[{}]'s TargetDS.", entityName);
		}
		return suffix;
	}

	private static void innerRegisterManager(String beanName, String fullClassName, String implFullClassName,
			String daoBeanName, String entityName) {

		GenericWebApplicationContext ctx = (GenericWebApplicationContext) SpringHelper.getApplicationContext();
		GenericBeanDefinition managerBean = new GenericBeanDefinition();
		managerBean.setBeanClassName(implFullClassName);
		injectDAO2Manager(managerBean, daoBeanName);
		if (StrUtil.isNotBlank(entityName)) {
			Class<?> clazz;
			try {
				clazz = EntityHelper.getClass(entityName);
				PropertyValue entityClass = new PropertyValue(PROPERTY_ENTITY_CLASS, clazz);
				managerBean.getPropertyValues().addPropertyValue(entityClass);
				log.debug("Set EntityClass [{}] for Manager [{}]", entityName, beanName);
			} catch (ClassNotFoundException e) {
				log.error("Fail to inject EntityClass [{}] to Manager [{}]", entityName, beanName);
			}
		}
		BeanDefinitionHolder holder = new BeanDefinitionHolder(managerBean, fullClassName);
		PropertyValue pvTarget = new PropertyValue("target", holder);

		GenericBeanDefinition managerBeanProxy = new GenericBeanDefinition();
		String parentName = "txProxyTemplate";// getAlterDSBeanName("txProxyTemplate", entityName);// "txProxyTemplate"
		managerBeanProxy.setParentName(parentName);
		managerBeanProxy.getPropertyValues().addPropertyValue(pvTarget);

		ctx.registerBeanDefinition(beanName, managerBeanProxy);
		configedManagerBeans.put(beanName, managerBeanProxy);
		log.debug("Register Manager[{}] with Parent[{}], Concrete[{}]", new Object[] { beanName, parentName,
				implFullClassName });
	}

	private static void injectDAO2Manager(GenericBeanDefinition managerBean, String daoName) {
		RuntimeBeanReference refDao = new RuntimeBeanReference(daoName);
		PropertyValue pvDao = new PropertyValue(PROPERTY_DAO, refDao);
		managerBean.getPropertyValues().addPropertyValue(pvDao);
		log.debug("Inject DAO Bean[{}] to {}.", daoName, managerBean.getBeanClassName());
	}

	/**
	 * Update rejection beans in spring context.
	 */
	public static void update() {
		if (cachedFilesCount > 0) {
			for (Resource resource : cachedFiles.keySet()) {
				synchronized (cachedFiles) {
					try {
						if (resource.getFile().lastModified() > cachedFiles.get(resource)) {
							registerBeans(resource);
							cachedFiles.put(resource, resource.getFile().lastModified());
						}
					} catch (IOException e) {
						log.error("Could not load config file from {}, reason:", resource, e.getMessage());
					}
				}
			}
		}
	}

	private static String getEntityName(String managerName) {
		String str = ClassUtils.getShortName(managerName);
		return str.substring(0, str.length() - BUSINESS_LOGIC_CUT_OFF);
	}

	private static String getEntityNameFromDAO(String daoName) {
		String str = ClassUtils.getShortName(daoName);
		return str.substring(0, str.length() - DATA_ACCESS_CUT_OFF);
	}

	private static String getDaoBeanName(String managerName) {
		return getEntityName(managerName) + DATA_ACCESS_OBJECT_POSTFIX;
	}

	public static String getManagerBeanName(String entityName) {
		return Introspector.decapitalize(entityName) + BUSINESS_LOGIC_BEAN_POSTFIX;
	}

	private static String getPackageLocation(String classpath) {
		String location = classpath.replace('.', '/');
		location = location + "/*.class";
		return location;
	}

	private static String getAlmostPackageLocation(String classpath, Class<?> interfaceClass) {
		String location = classpath.replace('.', '/');
		location = location + "/" + ClassUtils.getShortName(interfaceClass) + "*.class";
		return location;
	}
}
