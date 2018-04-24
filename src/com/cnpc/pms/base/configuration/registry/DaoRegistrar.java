package com.cnpc.pms.base.configuration.registry;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import com.cnpc.pms.base.configuration.model.PMSBean;
import com.cnpc.pms.base.configuration.model.PMSPackage;
import com.cnpc.pms.base.springbean.helper.ArtifactBeanHelper;
import com.cnpc.pms.base.springbean.helper.DataSourceConfigurer;
import com.cnpc.pms.base.util.SpringHelper;

public class DaoRegistrar extends Registrar {

	public DaoRegistrar(BeanDefinitionRegistry beanFactory, PMSPackage pmsPackage) {
		super(beanFactory, pmsPackage);
		detailPackage = pmsPackage.getDaoPackage();
		type = "DAO Beans";
	}

	@Override
	public void register(String fullClassName, String implFullClassName) {
		if (beanFactory.isBeanNameInUse(fullClassName)) {
			log.debug("DAO bean {} already registered.", fullClassName);
		} else {
			String sessionFactoryBeanName =
							DataSourceConfigurer.getDefaultBeanName(ArtifactBeanHelper.SESSIONFACTORY_BEAN_TYPE);
			PMSBean bean = pmsPackage.getManagerPackage().getSpecificBeans().get(fullClassName);
			if (pmsPackage.isNoEntity() == false && (bean == null || bean.isNoEntity() == false)) {
				String entityName = SpringHelper.getEntityNameByDAO(fullClassName);
				sessionFactoryBeanName = SpringHelper.getDecoratedNameByEntity(sessionFactoryBeanName, entityName);
			}
			RuntimeBeanReference refSessionFactory = new RuntimeBeanReference(sessionFactoryBeanName);
			PropertyValue pvSessFactory = new PropertyValue("sessionFactory", refSessionFactory);
			GenericBeanDefinition bdDaoTarget = new GenericBeanDefinition();
			bdDaoTarget.setBeanClassName(implFullClassName);
			bdDaoTarget.getPropertyValues().addPropertyValue(pvSessFactory);
			beanFactory.registerBeanDefinition(getBeanName(fullClassName), bdDaoTarget);
			log.debug("Register DAO bean:{}", fullClassName);
		}
	}

	@Override
	public String getBeanName(String fullClassName) {
		return fullClassName;
	}

}
