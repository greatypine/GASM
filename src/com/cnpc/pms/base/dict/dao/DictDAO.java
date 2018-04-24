package com.cnpc.pms.base.dict.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.dict.entity.Dict;

/**
 * <p>
 * <b>Dict data access object interface.</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2010-12-23
 */
public interface DictDAO extends IDAO {

	/**
	 * @param dictName
	 *            the table name of the dictionary
	 * @return Map<String, Dict>
	 */
	Map<String, Dict> loadDict(String dictName);


	/* 在新的字典表中根据字典类型获取字典内容
	 * @param dictName
	 * @return
	 */
	Map<String, Dict> loadNewDict(String dictName);

	/**
	 * Create Table for Dictionary
	 * 
	 * @param dictName
	 *            dictionary name, should be translated to table name here.
	 * @return create successfully or not
	 */
	boolean createTable(String dictName);

	/**
	 * Drop Table for Dictionary
	 * 
	 * @param dictName
	 *            dictionary name, should be translated to table name here.
	 * @return drop successfully or not
	 */
	boolean dropTable(String dictName);

	/**
	 * Batch insert data.
	 * 
	 * @param dicts
	 *            the data to batch insert
	 */
	void batchInsertData(List<Dict> dicts);
}
