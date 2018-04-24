package com.cnpc.pms.base.configuration.registry;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import com.cnpc.pms.base.entity.EntityHelper;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.springbean.helper.ArtifactBeanHelper;
import com.cnpc.pms.base.springbean.helper.DataSourceConfigurer;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.base.util.StrUtil;

public class ManagerRegisterHelper {

	protected static final Logger log = LoggerFactory.getLogger(ManagerRegisterHelper.class);

	/**
	 * @see registerManager
	 * @see registerManagerByEntity
	 * @param entityName
	 * @return
	 */
	public static String getDefaultDaoBeanName(String entityName) {
		String daoName = DataSourceConfigurer.getDefaultBeanName(ArtifactBeanHelper.DAO_TYPE);
		return SpringHelper.getDecoratedNameByEntity(daoName, entityName);
	}

	/* properties add to manager beans */
	private static final String PROPERTY_DAO = "dao";
	private static final String PROPERTY_ENTITY_CLASS = "entityClass";

	public static void registerManager(BeanDefinitionRegistry beanFactory, String beanName, String fullClassName,
			String implFullClassName, String daoBeanName, String entityName) {

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
		String parentName = "txProxyTemplate";// SpringHelper.getDecoratedNameByEntity("txProxyTemplate", entityName);//
												// "txProxyTemplate"
		managerBeanProxy.setParentName(parentName);
		managerBeanProxy.getPropertyValues().addPropertyValue(pvTarget);

		beanFactory.registerBeanDefinition(beanName, managerBeanProxy);
		log.debug("Register Manager[{}] with Parent[{}], Concrete[{}]", new Object[] { beanName, parentName,
				implFullClassName });
	}

	private static void injectDAO2Manager(GenericBeanDefinition managerBean, String daoName) {
		RuntimeBeanReference refDao = new RuntimeBeanReference(daoName);
		PropertyValue pvDao = new PropertyValue(PROPERTY_DAO, refDao);
		managerBean.getPropertyValues().addPropertyValue(pvDao);
		log.debug("Inject DAO Bean[{}] to {}.", daoName, managerBean.getBeanClassName());
	}

	public static void registerManagerByEntity(BeanDefinitionRegistry target, BeanDefinitionRegistry... containers) {
		Map<String, Class<?>> map = EntityHelper.getEntityMapping();
		outer: for (String entityName : map.keySet()) {
			String managerBeanName = SpringHelper.getManagerNameByEntity(entityName);
			for (BeanDefinitionRegistry container : containers) {
				if (container.containsBeanDefinition(managerBeanName)) {
					log.debug("Get Register Manager of [{}] : {}", entityName, managerBeanName);
					continue outer;
				}
			}
			log.debug("Entity[{}] doesn't have manager.", entityName);
			String daoBeanName = ManagerRegisterHelper.getDefaultDaoBeanName(entityName);
			if (daoBeanName == null) {
				log.error("Fail to get AlternativeDS Entity[{}]'s TargetDS.", entityName);
				continue;
			}
			ManagerRegisterHelper.registerManager(target, managerBeanName, IManager.class.getCanonicalName(),
					BaseManagerImpl.class.getCanonicalName(), daoBeanName, entityName);
			log.debug("Auto-generated Entity[{}]'s manager. ", entityName);
		}
	}
}
