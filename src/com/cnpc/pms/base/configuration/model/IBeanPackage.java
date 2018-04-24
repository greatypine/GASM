package com.cnpc.pms.base.configuration.model;

import java.util.Map;

public interface IBeanPackage {
	String getInterfacePackage();

	String getImplementPackage();

	Map<String, PMSBean> getSpecificBeans();
}
