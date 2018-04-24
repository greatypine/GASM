package com.cnpc.pms.base.configuration.registry;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import com.cnpc.pms.base.configuration.model.PMSApplication;
import com.cnpc.pms.base.configuration.model.PMSModule;
import com.cnpc.pms.base.configuration.model.PMSPackage;
import com.cnpc.pms.base.exception.UtilityException;
import com.cnpc.pms.base.util.ConfigurationUtil;

public class RegistrarAgent {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final BeanDefinitionRegistry beanFactory;
	private final ClassLoader classLoader;
	private final boolean dynamicLoad;
	private final Set<String> registeredBeans;
	private boolean reloadInterface = true;

	public RegistrarAgent(BeanDefinitionRegistry beanFactory, ClassLoader classLoader) {
		this.beanFactory = beanFactory;
		this.classLoader = classLoader;
		dynamicLoad = this.classLoader instanceof DynamicClassLoader;
		log.info("Init a [{}] registrar.", dynamicLoad ? "DYNAMIC" : "STATIC");
		registeredBeans = new HashSet<String>();
	}

	public RegistrarAgent(BeanDefinitionRegistry beanFactory, ClassLoader classLoader, RefreshType refreshType) {
		this(beanFactory, classLoader);
		reloadInterface = refreshType == RefreshType.all;
		log.info("This registrar will reload interface? {}", reloadInterface);
	}

	public void register(Resource resource) {
		try {
			PMSApplication app = (PMSApplication) ConfigurationUtil.parseXMLObject(PMSApplication.class, resource);
			for (PMSModule module : app.getModules()) {
				log.info("Start to auto register module[{}] ", module.getId());
				for (PMSPackage pkg : module.getPackages()) {
					log.debug("Start to auto register package[{}] ", pkg.getId());
					registerToSpring(new DaoRegistrar(beanFactory, pkg));
					registerToSpring(new ManagerRegistrar(beanFactory, pkg));
				}
			}
		} catch (UtilityException e) {
			log.error("Fail to digester config file {}, reason:", resource, e.getMessage());
		}
	}

	public Set<String> getRegisteredBeans() {
		return registeredBeans;
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
	private void registerToSpring(Registrar registrar) {
		if (registrar.hasConfig() == false) {
			log.debug("PMSPackage [{}] didn't define {}.", registrar.getPackageId(), registrar.getType());
			return;
		}
		String packageName = registrar.getInterfacePackage();
		String implPkgName = registrar.getImplementPackage();
		if (implPkgName == null) {
			implPkgName = packageName + ".impl";
		}
		Resource[] resources = ConfigurationUtil.getAllResources(getPackageLocation(packageName));
		for (Resource resource : resources) {
			String fileName = resource.getFilename();
			String className = fileName.substring(0, fileName.indexOf('.'));
			String fullClassName = packageName + "." + className;
			if (registrar.shouldSkipBean(fullClassName)) {
				log.debug("Skip excluded interface: {}", fullClassName);
				continue;
			}
			try {
				// NOTE In the case of TestCase, interface classes are already loaded here.
				// Outside the ClassLoader can't find the loaded classes.
				if (dynamicLoad && reloadInterface) {
					registerClass(resource, fullClassName);
				}
				Class<?> clazz = classLoader.loadClass(fullClassName);
				log.debug(
						"----------> {} = {}, {}",
						new Object[] { clazz.getSimpleName(), ClassUtils.isVisible(clazz, classLoader),
								clazz.getClassLoader() });
				// clazz.isAnonymousClass() == true ||
				if (clazz.isInterface() == false) {
					continue;
				}
				String implFullClassName = getImplements(clazz, implPkgName);
				registrar.register(fullClassName, implFullClassName);
				registeredBeans.add(registrar.getBeanName(fullClassName));
			} catch (Exception e) {
				log.error("Fail to register bean {} from: " + implPkgName, fullClassName, e);
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
	private String getImplements(Class<?> interfaceClass, String implPkgName) throws ClassNotFoundException,
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
			log.error("Fail to get Implements of {} from {}", interfaceClass.getCanonicalName(), implPkgName);
			throw new ClassNotFoundException("Fail to Get any implenment in the given path.");
		} else {
			return result;
		}
	}

	private static String getAlmostPackageLocation(String classpath, Class<?> interfaceClass) {
		String location = classpath.replace('.', '/');
		location = location + "/" + ClassUtils.getShortName(interfaceClass) + "*.class";
		return location;
	}

	private String getImplementsWithResources(Class<?> interfaceClass, String implPkgName, Resource[] implResources)
			throws ClassNotFoundException, LinkageError {
		String result = null;
		for (Resource implResource : implResources) {
			String implFileName = implResource.getFilename();
			String implClassName = implFileName.substring(0, implFileName.indexOf('.'));
			String implFullClassName = implPkgName + "." + implClassName;
			if (dynamicLoad)
				registerClass(implResource, implFullClassName);
			Class<?> implClass = classLoader.loadClass(implFullClassName);
			log.debug(
					"==========> {} = {}, {}",
					new Object[] { implClass.getSimpleName(), ClassUtils.isVisible(implClass, classLoader),
							implClass.getClassLoader() });

			if (implClass.isAnonymousClass() || implClass.isMemberClass()) {
				log.debug("Skip Anonymous or Member Class {}", implClass);
				continue;
			}
			if (ClassUtils.getAllInterfacesForClassAsSet(implClass).contains(interfaceClass)) {
				log.debug("Class {} has interfaces: {}", implFullClassName, interfaceClass);
				result = implFullClassName;
			} else {
				log.debug("Suppose but not a [{}] implenment: {}", interfaceClass, implClass);
			}
		}
		if (result == null) {
			log.warn("Fail to Get a [{}] implenment in path [{}].", interfaceClass, implPkgName);
		}
		return result;
	}

	private static String getPackageLocation(String classpath) {
		String location = classpath.replace('.', '/');
		location = location + "/*.class";
		return location;
	}

	private void registerClass(Resource resource, String fullClassName) {
		if (dynamicLoad == false) {
			return;
		}
		try {
			if (resource.getURL().getProtocol().equals("file")) {
				((DynamicClassLoader) classLoader).registerClass(fullClassName, resource.getFile());
			}
		} catch (IOException e) {
			log.error("Error to register Class[{}] with {}", fullClassName, resource);
			e.printStackTrace();
		}
	}
}
