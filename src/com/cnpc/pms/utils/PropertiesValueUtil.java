// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:  PropertiesValueUtil.java

package com.cnpc.pms.utils;

import com.cnpc.pms.base.util.ConfigurationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 根据传入的properties文件路径获取文件中配置的内容
 * Create by liuxiao on 2016/05/09
 */
public class PropertiesValueUtil {
	public String fileEncoding = "UTF-8";
	private final Logger log = LoggerFactory
			.getLogger(PropertiesValueUtil.class);
	private final Properties properties;

	public PropertiesValueUtil(String str_properties) {
		PropertiesPersister propertiesPersister;
		Resource resources[];
		int i;
		fileEncoding = "UTF-8";
		properties = new Properties();
		propertiesPersister = new DefaultPropertiesPersister();
		resources = ConfigurationUtil
				.getSortedResources(str_properties);
		Resource resource = null;

		i = 0;
		_L3: while (true) {
			if (resources == null)
				break;
			if (i >= resources.length)
				break;
			try {

				InputStream is;
				resource = resources[i];
				log.info("Loading properties file from {}", resource);
				is = null;
				is = resource.getInputStream();
				if (resource.getFilename().endsWith(".xml")) {
					propertiesPersister.loadFromXml(properties, is);
				} else {
					getClass();
					if ("UTF-8" != null) {
						getClass();
						propertiesPersister.load(properties,
								new InputStreamReader(is, "UTF-8"));
					} else {
						propertiesPersister.load(properties, is);
					}
				}
				if (is == null)
					continue; /* Loop/switch isn't completed */

				is.close();
			} catch (IOException e) {
				log.warn("Fail to close {}, reason: ", resource, e.getMessage());
			}
			i++;
			continue; /* Loop/switch isn't completed */
			// IOException ex;
			// ex;
			// log.warn("Could not load properties from {}, reason:", resource,
			// ex.getMessage());

			// goto _L3
		}

	}

	/**
	 * 获取Int类型的值
	 * @param key 键
	 * @return 值
     */
	public int getIntValue(String key) {
		return Integer.valueOf(properties.getProperty(key)).intValue();
	}

	/**
	 * 获取int类型的值，带默认值
	 * @param key 键
	 * @param defaultValue 默认值
     * @return 值
     */
	public int getIntValue(String key, int defaultValue) {
		try {
			return getIntValue(key);
		} catch (Exception e) {
			log.error("Fail to get int value of {}", key);
		}
		return defaultValue;
	}

	/**
	 * 获取String类型的值
	 * @param key 键
	 * @return 值
	 */
	public String getStringValue(String key) {
		return properties.getProperty(key);
	}

	/**
	 * 获取String类型的值，带默认值
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public String getStringValue(String key, String defaultValue) {
		try {
			return getStringValue(key);
		} catch (Exception e) {
			log.error("Fail to get int value of {}", key);
		}
		return defaultValue;
	}

//	public String getPlaceHolderString(String key) {
//		if (key == null)
//			return null;
//		else
//			return (new StringBuilder()).append("${").append(key).append("}")
//					.toString();
//	}

//	public String getPlaceHolderKey(String s) {
//		if (s == null)
//			return null;
//		if (s.startsWith("${") && s.endsWith("}"))
//			return s.substring("${".length(), s.length() - "}".length());
//		else
//			return s;
//	}

}
