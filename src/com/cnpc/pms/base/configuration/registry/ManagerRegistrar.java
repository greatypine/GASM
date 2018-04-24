package com.cnpc.pms.base.configuration.registry;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.cnpc.pms.base.configuration.model.PMSBean;
import com.cnpc.pms.base.configuration.model.PMSPackage;
import com.cnpc.pms.base.util.SpringHelper;

public class ManagerRegistrar extends Registrar {

	public ManagerRegistrar(BeanDefinitionRegistry beanFactory, PMSPackage pmsPackage) {
		super(beanFactory, pmsPackage);
		detailPackage = pmsPackage.getManagerPackage();
		type = "Manager Beans";
	}

	@Override
	public void register(String fullClassName, String implFullClassName) {
		registerManager(fullClassName, implFullClassName, pmsPackage);
	}

	private void registerManager(String fullClassName, String implFullClassName, PMSPackage pmsPackage) {
		String entityName = null;
		PMSBean bean = pmsPackage.getManagerPackage().getSpecificBeans().get(fullClassName);
		if (pmsPackage.isNoEntity() == false && (bean == null || bean.isNoEntity() == false)) {
			entityName = SpringHelper.getEntityNameByManager(fullClassName);
		}
		String daoBeanName = null;
		if (pmsPackage.getDaoPackage() != null) {
			String daoName =
							pmsPackage.getDaoPackage().getInterfacePackage() + "."
											+ SpringHelper.getDaoNameByManager(fullClassName);
			if (beanFactory.isBeanNameInUse(daoName)) {
				log.debug("Find Dao Bean [{}] for {}", daoName, implFullClassName);
				daoBeanName = daoName;
			}
		}
		if (daoBeanName == null) {
			daoBeanName = ManagerRegisterHelper.getDefaultDaoBeanName(entityName);
		}
		ManagerRegisterHelper.registerManager(beanFactory, getBeanName(fullClassName), fullClassName,
			implFullClassName, daoBeanName, entityName);
	}

	@Override
	public String getBeanName(String fullClassName) {
		return SpringHelper.getShortNameAsProperty(fullClassName);
	}

}
