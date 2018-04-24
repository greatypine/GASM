package com.cnpc.pms.base.dict.manager.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.cnpc.pms.base.dict.DictCache;
import com.cnpc.pms.base.dict.dao.DictDAO;
import com.cnpc.pms.base.dict.entity.Dict;
import com.cnpc.pms.base.dict.manager.DictManager;
import com.cnpc.pms.base.exception.DictImportException;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.util.ConfigurationUtil;
import com.cnpc.pms.base.util.PropertiesUtil;

/**
 * <p>
 * <b>Dict manager interface's implement.</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2010-12-23
 */
public class DictManagerImpl extends BaseManagerImpl implements DictManager {

	@Override
	public DictDAO getDao() {
		return (DictDAO) dao;
	}

	/**
	 * Cache dicts.
	 * 
	 * @param
	 *
	*/
	public void initializeCache() {
		synchronized (DictCache.getInstance()) {
			Map<String, Map<String, Dict>> dicts = DictCache.getInstance().getDicts();
			for (String key : dicts.keySet()) {
				dicts.put(key, loadDictFromDB(key));
			}
		}
	}

	/**
	 * get the dictionary by the table name
	 * 
	 * @param dictName
	 *            table name of the dictionary
	 * @return List<Dict>
	 */

	public Map<String, Dict> getDict(String dictName) {
		Map<String, Dict> dict = DictCache.getInstance().getDict(dictName);
		if (dict == null || dict.size() == 0) {
			log.info("Find unregistered Dictionary [{}]", dictName);
			Map<String, Dict> registerDict = loadDictFromDB(dictName);
			if (registerDict != null && registerDict.size() > 0) {
				log.info("Unregistered Dictionary [{}] has been defined in DB with [{}] items.", dictName,
						registerDict.size());
				synchronized (DictCache.getInstance()) {
					dict = DictCache.getInstance().getDict(dictName);
					if (dict == null || dict.size() == 0) {
						dict = registerDict;
						DictCache.getInstance().setDict(dictName, dict);
						log.info("Register Dictionary [{}] with [{}] items.", dictName, registerDict.size());
					}
				}
			} else {
				log.error("Fail to get dictionary [{}] from DB", dictName);
			}
		}
		return dict;
	}

	/**
	 * 从缓存中查找字典内容，如果没有，从数据库获取
	 * @param dictName
	 * @return
     */
	public Map<String,Dict> getNewDict(String dictName){
		Map<String,Dict> dict = DictCache.getInstance().getDict(dictName);
		if (dict == null || dict.size() == 0) {
			Map<String, Dict> registerDict = loadNewDictFromDB(dictName);
			if (registerDict != null && registerDict.size() > 0) {
				synchronized (DictCache.getInstance()) {
					dict = DictCache.getInstance().getDict(dictName);
					if (dict == null || dict.size() == 0) {
						dict = registerDict;
						DictCache.getInstance().setDict(dictName, dict);
					}
				}
			} else {
				log.error("Fail to get dictionary [{}] from DB", dictName);
			}
		}
		return dict;
	}

	/**
	 * Find dict text.
	 * 
	 * @param dictName
	 *            the dict name
	 * @param dictCode
	 *            the dict code
	 * @return the string Will return "dict.code" in case of missing dict or item
	 */

	public String getText(String dictName, String dictCode) {
		String text = null;
		Map<String, Dict> dictionary = getDict(dictName);
		if (dictionary == null) {
			log.error("Fail to get dictionary [{}]", dictName);
		} else {
			Dict dictItem = null;
			dictItem = dictionary.get(dictCode);
			if (dictItem != null) {
				text = dictItem.getDictText();
			} else {
				log.error("Fail to get dict item[code={}] from [{}]", dictCode, dictName);
			}
		}
		if (text == null) {
			text = dictName + "." + dictCode;
		}
		return text;
	}

	private Map<String, Dict> loadDictFromDB(String dictName) {
		Map<String, Dict> dict = null;
		try {
			dict = getDao().loadDict(dictName);
			log.debug("load from DB for Dictionary[{}] with [{}] item(s).", dictName, dict.size());
		} catch (Exception e) {
			log.error("Fail to load dictionary[{}] for DB.", dictName, e);
		}
		return dict;
	}

	private Map<String,Dict> loadNewDictFromDB(String dictType){
		Map<String,Dict> dict = null;
		try{
			dict = getDao().loadNewDict(dictType);
			log.debug("load from DB for Dictionary[{}] with [{}] item(s).", dictType, dict.size());
		}catch (Exception e){
			log.error("Fail to load dictionary[{}] for DB.", dictType, e);
		}
		return dict;
	}

	/**
	 * 导入字典.
	 * 
	 * @throws IOException
	 * @throws DictImportException
	 */

	public void importDict() throws DictImportException, IOException {
		List<Dict> dicts = new ArrayList<Dict>();
		Set<String> tables = new HashSet<String>();
		fetchDictFromFile(tables, dicts);
		for (String table : tables) {
			boolean flag = false;
			flag = getDao().createTable(table);
			log.info("create table {} successfully? {} ", table, flag);
		}
		getDao().batchInsertData(dicts);
	}

	public void dropDictTable() throws IOException {
		Set<String> tables = new HashSet<String>();
		List<Dict> dicts = new ArrayList<Dict>();
		fetchDictFromFile(tables, dicts);
		for (String table : tables) {
			boolean flag = false;
			flag = getDao().dropTable(table);
			log.info("drop table {} successfully? {}", table, flag);
		}
	}

	private void fetchDictFromFile(Set<String> tables, List<Dict> dicts) throws IOException {
		String DEFAULT_CONFIG_LOCATION = "/init/dict/*.csv";
		Resource[] resources = ConfigurationUtil.getAllResources(DEFAULT_CONFIG_LOCATION);
		if (resources != null) {
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				log.info("Init dictionaries from: {}", resource);
				ICsvBeanReader initFile = new CsvBeanReader(new InputStreamReader(resource.getInputStream()),
						CsvPreference.EXCEL_PREFERENCE);
				boolean needI18n = (resource.getFilename().indexOf("i18n") > -1);
				String[] header = initFile.getCSVHeader(true);
				CellProcessor[] processors = new CellProcessor[header.length];
				Dict dict;
				String cachedDictName = null;
				while ((dict = initFile.read(Dict.class, header, processors)) != null) {
					if (dict.getDictName().equals(cachedDictName) == false) {
						cachedDictName = dict.getDictName();
						tables.add(dict.getDictName());
						log.debug("Get new Dictionary: {}", cachedDictName);
					}
					if (needI18n) {
						dict.setDictText(PropertiesUtil.getPlaceHolderString(dict.getDictText()));
					}
					dicts.add(dict);
				}
				log.info("Get {} dictionaries: {}.", tables.size(), tables);
			}
			log.info("All Get {} dictionaries: {} ", tables.size(), tables);
		}
	}

	public List<Dict> findDictByName(String dictName) {
		Map<String, Dict> dictMap = getDict(dictName);
		if (null == dictMap) {
			return null;
		}
		List<Dict> dicts = new ArrayList<Dict>();
		dicts.addAll(dictMap.values());
		if (dicts.size() > 0) {
			Collections.sort(dicts, new Comparator<Dict>() {
				public int compare(Dict d1, Dict d2) {
					try {
						Integer n1 = Integer.valueOf(d1.getSerialNumber());
						Integer n2 = Integer.valueOf(d2.getSerialNumber());
						return n1.compareTo(n2);
					} catch (NumberFormatException e) {
						log.error("Fail to sort dictionary[{}] items, reason:{}", d1.getDictName(), e.getMessage());
						return d1.getSerialNumber().compareTo(d2.getSerialNumber());
					}
				}
			});
		}
		return dicts;
	}

	@Override
	public List<Dict> findDictByType(String dictType) {
		Map<String,Dict> dictMap = getNewDict(dictType);
		if(null == dictMap){
			return null;
		}
		List<Dict> dicts = new ArrayList<Dict>();
		dicts.addAll(dictMap.values());
		if(dicts.size() > 0 ){
			Collections.sort(dicts, new Comparator<Dict>() {
				public int compare(Dict d1, Dict d2) {
					return 0;
				}
			});
		}
		return dicts;
	}



	@Deprecated
	public String findDictText(String dictName, String dictCode) {
		return getText(dictName, dictCode);
	}

}
