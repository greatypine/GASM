package com.cnpc.pms.base.init.model;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "inits/init/data")
public class PMSInitData {
	@SetProperty(pattern = "inits/init/data", attributeName = "file")
	private String file;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
