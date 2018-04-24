package com.cnpc.pms.base.init.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;

@ObjectCreate(pattern = "inits")
public class PMSInits {
	private final List<PMSInit> inits = new ArrayList<PMSInit>();

	@SetNext
	public void addInit(PMSInit init) {
		this.inits.add(init);
	}

	public List<PMSInit> getInits() {
		return inits;
	}

}
