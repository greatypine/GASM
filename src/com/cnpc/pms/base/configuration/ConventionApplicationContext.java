package com.cnpc.pms.base.configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;

import com.cnpc.pms.base.configuration.registry.DynamicClassLoader;
import com.cnpc.pms.base.configuration.registry.ManagerRegisterHelper;
import com.cnpc.pms.base.configuration.registry.RefreshType;
import com.cnpc.pms.base.configuration.registry.RegistrarAgent;
import com.cnpc.pms.base.schedule.quartz.SchedulerHelper;
import com.cnpc.pms.base.util.ConfigurationUtil;

public class ConventionApplicationContext extends AbstractRefreshableApplicationContext {

	public static final String DEFAULT_CONFIG_LOCATION = "/module/*.xml";

	private RefreshType refreshType;// Values: {none,all,impl}
	private boolean refreshable = false;

	private final ClassLoader defaultClassLoader = this.getClassLoader();
	private BeanDefinition auditBeanDefinition;

	/** The cached files. */
	private Map<Resource, Long> cachedFiles;

	private Map<File, Long> fileAndTime;
	private Map<File, String> fileAndClassName;
	private final Set<String> updatedClassName = new HashSet<String>();

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private boolean initialized = false;

	@Override
	public void setParent(ApplicationContext parent) {
		initAuditBeanDefinition(parent);
		ApplicationContext realParent;
		if (parent != null && parent instanceof BeanDefinitionRegistry) {
			realParent = parent;
			log.info("Set Parent appContext: {}", parent);
		} else {
			log.info("Parent appContext is not available as BeanDefinitionRegistry: {}", parent);
			GenericApplicationContext genericAppContext = new GenericApplicationContext();
			genericAppContext.setParent(parent);
			registerAuditBean(genericAppContext);
			genericAppContext.refresh();
			realParent = genericAppContext;
			log.info("Create new Parent appContext to hold Jar Classes: {}", genericAppContext);
		}
		super.setParent(realParent);
	}

	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws IOException, BeansException {

		if (initialized == false) {
			this.initialize();
		}

		if (this.refreshable) {
			this.setupClassLoader();
		}

		this.registerAuditBean(beanFactory);

		RegistrarAgent agent = new RegistrarAgent(beanFactory, this.getClassLoader(), this.refreshType);
		for (Resource resource : cachedFiles.keySet()) {
			try {
				if (this.needDynamicLoad(resource)) {
					log.debug("Loading auto register beans from {}", resource);
					agent.register(resource);
				}
			} catch (IOException e) {
				log.error("Fail to get [{}] :", resource, e);
			}
		}
		log.debug("cached module config files: {}", cachedFiles.size());

		if (initialized == false) {
			log.info("Start to generate default managers for those entities with no manager defined");
			ManagerRegisterHelper.registerManagerByEntity(this.getParentAsRegistry(), beanFactory,
				this.getParentAsRegistry());
			this.postInitialize();
		}
	}

	// Judge whether the resource should be dynamic loaded.
	private boolean needDynamicLoad(Resource resource) throws IOException {
		return resource.getURL().getProtocol().equals("file") && resource.getURL().getFile().indexOf("pmsbase") == -1;
	}

	private BeanDefinitionRegistry getParentAsRegistry() {
		return (BeanDefinitionRegistry) super.getParent();
	}

	private void initAuditBeanDefinition(ApplicationContext auditBeanContainer) {
		ConfigurableListableBeanFactory parentBeanFactory =
						((ConfigurableApplicationContext) auditBeanContainer).getBeanFactory();
		try {
			auditBeanDefinition = parentBeanFactory.getBeanDefinition("loggerAutoProxy");
		} catch (NoSuchBeanDefinitionException e) {
			log.info("There is no Audit Bean defined.");
		}
	}

	private void registerAuditBean(BeanDefinitionRegistry beanFactory) {
		if (auditBeanDefinition != null) {
			GenericBeanDefinition newAuditBean = new GenericBeanDefinition(auditBeanDefinition);
			beanFactory.registerBeanDefinition("loggerAutoProxy", newAuditBean);
			newAuditBean.setAbstract(false);
		}
	}

	private void initialize() {
		cachedFiles = new HashMap<Resource, Long>();
		Resource[] resources = ConfigurationUtil.getAllResources(DEFAULT_CONFIG_LOCATION);
		if (resources != null) {
			RegistrarAgent agent = new RegistrarAgent(getParentAsRegistry(), defaultClassLoader);
			for (Resource resource : resources) {
				try {
					if (this.needDynamicLoad(resource)) {
						cachedFiles.put(resource, resource.lastModified());
						continue;
					}
					log.info("Loading auto register beans from {}", resource);
					agent.register(resource);
				} catch (IOException e) {
					log.error("Fail to get [{}] :", resource, e);
				}
			}
		}
	}

	private void postInitialize() {
		initialized = true;
	}

	private void setupClassLoader() {
		DynamicClassLoader classLoader;
		if (this.getClassLoader() instanceof DynamicClassLoader == false) {
			if (initialized == true) {
				throw new RuntimeException("Wrong type of ClassLoader: " + this.getClassLoader());
			}
			classLoader = new DynamicClassLoader(defaultClassLoader);
			fileAndTime = classLoader.getFileAndTime();
			fileAndClassName = classLoader.getFileAndClassName();
		} else {
			DynamicClassLoader currentClassLoader = (DynamicClassLoader) this.getClassLoader();
			currentClassLoader.destroy();
			if (updatedClassName.size() > 0) {
				// NOTE Maybe optimized here.2012/02/08
				Map<String, Class<?>> classNameAndClass = currentClassLoader.getClassNameAndClass();
				// for (String className : updatedClassName) {
				// classNameAndClass.remove(className);
				// }
				classNameAndClass.clear();
			}
			classLoader = new DynamicClassLoader(currentClassLoader);
		}
		log.debug("Now ClassLoad is: {}", this.getClassLoader());
		this.setClassLoader(classLoader);
	}

	public void reloadClasses() throws IOException {
		log.debug("Start to check Classes.....");
		if (this.refreshable == false) {
			return;
		}
		long start = System.currentTimeMillis();
		updatedClassName.clear();
		for (Map.Entry<File, Long> entry : fileAndTime.entrySet()) {
			if (entry.getKey().lastModified() > entry.getValue()) {
				String className = fileAndClassName.get(entry.getKey());
				updatedClassName.add(className);
				fileAndTime.put(entry.getKey(), entry.getKey().lastModified());
				log.info("UPDATE CLASS FILE:{}", entry.getKey());
			}
		}
		boolean configFileUpdated = false;
		Resource[] resources = ConfigurationUtil.getAllResources(DEFAULT_CONFIG_LOCATION);
		for (Resource resource : resources) {
			try {
				if (this.needDynamicLoad(resource)) {
					if (cachedFiles.containsKey(resource) || cachedFiles.get(resource) == resource.lastModified()) {
						continue;
					} else {
						configFileUpdated = true;
						cachedFiles.put(resource, resource.lastModified());
					}
				}
			} catch (IOException e) {
				log.error("Fail to get [{}] :", resource, e);
			}
		}
		if (updatedClassName.size() > 0 || configFileUpdated) {
			SchedulerHelper.getInstance().standBy();
			this.refresh();
			log.info("######Reloaded ApplicationContext with: {} ms", System.currentTimeMillis() - start);
			SchedulerHelper.getInstance().start();
		}
	}

	/**
	 * @param refreshable
	 *            the refreshable to set
	 */
	public void setRefresh(String refresh) {
		this.refreshType = RefreshType.valueOf(refresh);
		this.refreshable = (this.refreshType == RefreshType.all || this.refreshType == RefreshType.impl);
	}

}
