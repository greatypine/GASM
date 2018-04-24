package com.cnpc.pms.base.dict;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.cnpc.pms.base.dict.entity.Dict;
import com.cnpc.pms.base.exception.UtilityException;
import com.cnpc.pms.base.util.ConfigurationUtil;
import com.cnpc.pms.base.util.XmlUtil;

/**
 * Dictionary Cache Class.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author lushu
 * @since Apr 26, 2011
 */
public class DictCache {

	public static final String DEFAULT_CONFIG_LOCATION = "/conf/dicts*.xml";

	final static Logger log = LoggerFactory.getLogger(DictCache.class);
	private static DictCache instance = new DictCache();
	private final Map<String, Map<String, Dict>> dicts = new HashMap<String, Map<String, Dict>>();

	/**
	 * 私有的构造方法
	 */
	private DictCache() {
		Resource[] resources = ConfigurationUtil.getAllResources(DEFAULT_CONFIG_LOCATION);
		if (resources != null) {
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				log.info("Loading dictionaries from {}", resource);
				try {
					NodeList nodes = XmlUtil.getNodesByXpath(resource.getInputStream(), "//dicts/dict");
					for (int j = 0; j < nodes.getLength(); j++) {
						Element o = (Element) nodes.item(j);
						String name = o.getAttribute("name");
						String description = o.getAttribute("description");
						if (dicts.containsKey(name) == true) {
							log.error("Duplicated Dictionary register! Dict[{}], in file {}", name, resource);
						}
						dicts.put(name, null);
						log.debug("Get preload dictionary: [{}]{}", name, description);
					}
				} catch (UtilityException e) {
					log.error("Fail to digester dictionaries from {}, reason:", resource, e.getMessage());
				} catch (IOException e) {
					log.error("Could not load dictionaries from {}, reason:", resource, e.getMessage());
				}
			}
			log.debug("Number of preloaded dictionaries is {}", dicts.size());
		}
	}

	/**
	 * Get Dictionary by dictname.
	 * 
	 * @param dictName
	 *            the query id
	 * @return Map<String, Dict> instance.
	 */
	public Map<String, Dict> getDict(String dictName) {
		return dicts.get(dictName);
	}

	public void setDict(String dictName, Map<String, Dict> dict) {
		dicts.put(dictName, dict);
	}

	/**
	 * Get All Dictionary Definition.
	 * 
	 * @return
	 */
	public Map<String, Map<String, Dict>> getDicts() {
		return dicts;
	}

	public static DictCache getInstance() {
		return instance;
	}

}
