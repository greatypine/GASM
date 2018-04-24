package com.cnpc.pms.base.configuration.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;

/**
 * PMS Configuraiton Application Model<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/18
 */
@ObjectCreate(pattern = "application")
public class PMSApplication {

	/** The modules. */
	private final List<PMSModule> modules = new ArrayList<PMSModule>();

	/**
	 * Adds the module.
	 * 
	 * @param module
	 *            the module
	 */
	@SetNext
	public void addModule(PMSModule module) {
		this.modules.add(module);
	}

	/**
	 * Gets the modules.
	 * 
	 * @return the modules
	 */
	public List<PMSModule> getModules() {
		return modules;
	}

}
