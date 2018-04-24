package com.cnpc.pms.base.configuration.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.cnpc.pms.base.configuration.model.IBeanPackage;
import com.cnpc.pms.base.configuration.model.PMSBean;
import com.cnpc.pms.base.configuration.model.PMSPackage;

public abstract class Registrar {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	protected BeanDefinitionRegistry beanFactory;
	protected PMSPackage pmsPackage;
	protected IBeanPackage detailPackage;
	protected String type;

	protected Registrar(BeanDefinitionRegistry beanFactory, PMSPackage pmsPackage) {
		this.beanFactory = beanFactory;
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

	public abstract String getBeanName(String fullClassName);
}
