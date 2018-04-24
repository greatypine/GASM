package com.cnpc.pms.base.dict.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * <p>
 * <b>Dict Entity.</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hewang
 * @since 2011-03-04
 */
public class Dict {

	/** The dict name(table name). */
	private String dictName;

	/** The dict code. */
	private String dictCode;

	/** The dict text. */
	private String dictText;

	/** The serial number. */
	private String serialNumber;

	/** The dict disabled . */
	private String disabledFlag;// 2012-10-12 郝成杰修改，加入在页面是否显示标识符

	public static String getCreateSql(String name) {
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE ").append(getTableName(name));
		sql.append("(dictCode varchar(255) DEFAULT NULL,");
		sql.append("dictText varchar(255) DEFAULT NULL,");
		sql.append("serialNumber varchar(20) DEFAULT NULL,");
		sql.append("disabledFlag varchar(20) DEFAULT NULL,");
		sql.append("PRIMARY KEY (dictCode)");
		sql.append(")");
		return sql.toString();
	}

	public static String getDropSql(String name) {
		StringBuffer sql = new StringBuffer();
		sql.append("DROP TABLE ").append(getTableName(name));// oracle doesn't support .append("IF EXISTS;")
		return sql.toString();
	}

	public static String getSelectSql(String name) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT '").append(name).append("', dictCode, dictText, serialNumber, disabledFlag FROM ")
				.append(getTableName(name)).append(" ORDER BY serialNumber");
		return sql.toString();
	}

	public static String getDictByType(String type){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT '").append(type).append("',item_code,item_code_name,display_sequence,item_description ")
				.append(" FROM dict_info")
				.append(" WHERE dict_code = '").append(type).append("' and item_status =1 ")
				.append(" ORDER BY display_sequence");
		return sql.toString();
	}

	private static String getTableName(String name) {
		return "dict_" + name;
	}

	@JsonIgnore
	public String getInsertSql() {
		StringBuffer addSql = new StringBuffer();
		addSql.append("INSERT INTO ").append(this.getTableName())
				.append(" (dictCode,dictText,serialNumber,disabledFlag) VALUES('").append(dictCode).append("', '")
				.append(dictText).append("', '").append(serialNumber).append("','").append(disabledFlag).append("') ");
		return addSql.toString();
	}

	@JsonIgnore
	public String getCountSql() {
		return "SELECT COUNT(1) FROM " + getConditionSql();
	}

	@JsonIgnore
	public String getDeleteSql() {
		return "DELETE FROM " + getConditionSql();
	}

	private String getConditionSql() {
		StringBuffer sql = new StringBuffer();
		sql.append(getTableName()).append(" WHERE dictCode='").append(dictCode).append("'");
		return sql.toString();
	}

	@JsonIgnore
	public String getTableName() {
		return Dict.getTableName(this.dictName);
	}

	public Dict(Object[] objects) {
		dictName = objects[0].toString();
		dictCode = objects[1].toString();
		dictText = objects[2].toString();
		// missing serialNumber will be set as code
		serialNumber = (null == objects[3] || "null".equals(objects[3])) ? dictCode : objects[3].toString();
		disabledFlag = (String) objects[4]; // 2012-10-12 郝成杰修改，加入在页面是否显示标识符
	}

	public Dict() {
	}

	public String getDisabledFlag() {
		return disabledFlag;
	}

	public void setDisabledFlag(String disabledFlag) {
		this.disabledFlag = disabledFlag;
	}

	/**
	 * Gets the dict code.
	 * 
	 * @return the dictCode
	 */
	public String getDictCode() {
		return dictCode;
	}

	/**
	 * Gets the dict name.
	 * 
	 * @return the dictName
	 */
	public String getDictName() {
		return dictName;
	}

	/**
	 * Gets the dict text.
	 * 
	 * @return the dictText
	 */
	public String getDictText() {
		return dictText;
	}

	/**
	 * Sets the dict code.
	 * 
	 * @param dictCode
	 *            the dictCode to set
	 */
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	/**
	 * Sets the dict text.
	 * 
	 * @param dictText
	 *            the dictText to set
	 */
	public void setDictText(String dictText) {
		this.dictText = dictText;
	}

	/**
	 * Sets the dict name.
	 * 
	 * @param dictName
	 *            the dictName to set
	 */
	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	/**
	 * Gets the serial number.
	 * 
	 * @return the serial number
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Sets the serial number.
	 * 
	 * @param serialNumber
	 *            the new serial number
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
}
