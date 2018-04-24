package com.cnpc.pms.base.query.model;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.operator.ConditionOperator;

/**
 * PMS Query Column Object. <br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/27
 */
@ObjectCreate(pattern = "pmsquery/query/column")
public class PMSColumn {

	/** The key. */
	@SetProperty(attributeName = "key", pattern = "pmsquery/query/column")
	private String key;

	/** The header. */
	@SetProperty(attributeName = "header", pattern = "pmsquery/query/column")
	private String header;

	/** The value. */
	@SetProperty(attributeName = "value", pattern = "pmsquery/query/column")
	private Object value;

	/** The type. */
	@SetProperty(attributeName = "type", pattern = "pmsquery/query/column")
	private String type = "java.lang.String";

	/** The format. */
	@SetProperty(attributeName = "format", pattern = "pmsquery/query/column")
	private String format;

	/** The dict. */
	@SetProperty(attributeName = "dict", pattern = "pmsquery/query/column")
	private String dict;

	/** The width. */
	@SetProperty(attributeName = "width", pattern = "pmsquery/query/column")
	private String width = "10%";

	/** The align. */
	@SetProperty(attributeName = "align", pattern = "pmsquery/query/column")
	private String align = "left";

	/** The display. */
	@SetProperty(attributeName = "display", pattern = "pmsquery/query/column")
	private boolean display = true;

	/** The allow sort. */
	@SetProperty(attributeName = "allowSort", pattern = "pmsquery/query/column")
	private boolean allowSort = false;

	/** The allow search. */
	@SetProperty(attributeName = "allowSearch", pattern = "pmsquery/query/column")
	private boolean allowSearch = false;

	// String join with ',', may contains: count,sum,avg,min,max
	@SetProperty(attributeName = "stat", pattern = "pmsquery/query/column")
	private String stat = "";

	/** The operator. */
	@SetProperty(attributeName = "operator", pattern = "pmsquery/query/column")
	private String operator = "EQ";
	

	/** The tips. */
	@SetProperty(attributeName = "tips", pattern = "pmsquery/query/column")
	private boolean tips = false;

	/** The tips. */
	@SetProperty(attributeName = "columnSort", pattern = "pmsquery/query/column")
	private String columnSort = null;

	public String getColumnSort() {
		return columnSort;
	}

	public void setColumnSort(String columnSort) {
		this.columnSort = columnSort;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public ConditionOperator getOperatorObject() throws InvalidFilterException {
		return ConditionOperator.getOperator(this.operator);
	}

	/**
	 * Gets the align.
	 * 
	 * @return the align
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * Gets the format.
	 * 
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Gets the header.
	 * 
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Gets the width.
	 * 
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * Checks if is allow search.
	 * 
	 * @return the allowSearch
	 */
	public boolean isAllowSearch() {
		return allowSearch;
	}

	/**
	 * Checks if is allow sort.
	 * 
	 * @return the allowSort
	 */
	public boolean isAllowSort() {
		return allowSort;
	}

	/**
	 * Checks if is display.
	 * 
	 * @return the display
	 */
	public boolean isDisplay() {
		return display;
	}

	/**
	 * Sets the align.
	 * 
	 * @param align
	 *            the align to set
	 */
	public void setAlign(String align) {
		this.align = align;
	}

	/**
	 * Sets the allow search.
	 * 
	 * @param allowSearch
	 *            the allowSearch to set
	 */
	public void setAllowSearch(boolean allowSearch) {
		this.allowSearch = allowSearch;
	}

	/**
	 * Sets the allow sort.
	 * 
	 * @param allowSort
	 *            the allowSort to set
	 */
	public void setAllowSort(boolean allowSort) {
		this.allowSort = allowSort;
	}

	/**
	 * Sets the display.
	 * 
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(boolean display) {
		this.display = display;
	}

	/**
	 * Sets the format.
	 * 
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Sets the header.
	 * 
	 * @param header
	 *            the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * Sets the key.
	 * 
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Sets the width.
	 * 
	 * @param width
	 *            the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * Gets the dict.
	 * 
	 * @return the dict
	 */
	public String getDict() {
		return dict;
	}

	/**
	 * Sets the dict.
	 * 
	 * @param dict
	 *            the new dict
	 */
	public void setDict(String dict) {
		this.dict = dict;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}
	

	public boolean isTips() {
		return tips;
	}

	public void setTips(boolean tips) {
		this.tips = tips;
	}
}
