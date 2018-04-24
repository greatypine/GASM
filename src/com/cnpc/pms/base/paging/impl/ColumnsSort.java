/**
 * 
 */
package com.cnpc.pms.base.paging.impl;

/**
 * @author haochengjie
 *
 */
public class ColumnsSort {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5801038389048270341L;

	/** The variable name. */
	private String variableName;

	/** The direction. */
	private String variableValue;
	
	private String sortType;

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableValue() {
		return variableValue;
	}

	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}

	public String getSortType() {
		if("1".equals(sortType)){
			sortType = "ASC";
		}else if("-1".equals(sortType)){
			sortType = "DESC";
		}
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	

}
