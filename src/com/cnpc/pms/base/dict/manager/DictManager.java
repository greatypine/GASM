package com.cnpc.pms.base.dict.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dict.entity.Dict;
import com.cnpc.pms.base.exception.DictImportException;
import com.cnpc.pms.base.manager.IManager;

/**
 * <p>
 * <b>Dict manager interface.</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 *
 * @author hefei
 * @since 2010-12-23
 */
public interface DictManager extends IManager {
	/**
	 * Cache dicts.
	 */
	void initializeCache();

	/**
	 * find dicts by name.
	 *
	 * @param dictName
	 *            the table name of the dictionary
	 * @return List<Dict>
	 */
	Map<String, Dict> getDict(String dictName);

	/**
	 * Find dict text.
	 *
	 * @param dictName
	 *            the dict name
	 * @param dictCode
	 *            the dict code
	 * @return the string
	 */
	String getText(String dictName, String dictCode);

	/**
	 * Ordered by serialNumber
	 *
	 * @param dictName
	 * @return
	 */
	List<Dict> findDictByName(String dictName);

	/**
	 * Find dict by type list.
	 * @author  李海
	 * 根据字典表（dict_info）的字典类型查询字典内容
	 * @param dictType the dict type
	 * @return the list
	 */
	List<Dict> findDictByType(String dictType);

	@Deprecated
	String findDictText(String dictName, String dictCode);

	/**
	 * 导入字典数据. 会删除系统中已有的所有字典表.
	 *
	 * @throws IOException
	 *
	 * @throws Exception
	 *             the exception
	 */
	void importDict() throws DictImportException, IOException;

	/**
	 * 删除字典表.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	void dropDictTable() throws IOException;

}
