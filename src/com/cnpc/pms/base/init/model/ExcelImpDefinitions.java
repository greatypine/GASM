package com.cnpc.pms.base.init.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;

@ObjectCreate(pattern = "definitions")
public class ExcelImpDefinitions {
	private final List<ExcelImpDefinition> definitions = new ArrayList<ExcelImpDefinition>();

	@SetNext
	public void addDefinition(ExcelImpDefinition definition) {
		this.definitions.add(definition);
	}

	public List<ExcelImpDefinition> getDefinitions() {
		return definitions;
	}

}
