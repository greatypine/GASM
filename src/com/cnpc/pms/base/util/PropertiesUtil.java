// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PropertiesUtil.java

package com.cnpc.pms.base.util;

import java.io.*;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

// Referenced classes of package com.cnpc.pms.base.util:
//            ConfigurationUtil

public class PropertiesUtil {
	public static final String DEFAULT_CONFIG_LOCATION = "/conf/application.properties";
	public static final String XML_FILE_EXTENSION = ".xml";
	public String fileEncoding = "UTF-8";
	private static final Logger log = LoggerFactory
			.getLogger(PropertiesUtil.class);
	private static PropertiesUtil instance = new PropertiesUtil();
	private final Properties properties;
	private static final String PLACEHOLDER_PREFIX = "${";
	private static final String PLACEHOLDER_SUFFIX = "}";

	protected PropertiesUtil() {
		PropertiesPersister propertiesPersister;
		Resource resources[];
		int i;
		fileEncoding = "UTF-8";
		properties = new Properties();
		propertiesPersister = new DefaultPropertiesPersister();
		resources = ConfigurationUtil
				.getSortedResources("/conf/application.properties");
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

	public static Properties getProperties() {
		return instance.properties;
	}

	public static String getValue(String key) {
		return instance.properties.getProperty(key);
	}

	public static int getIntValue(String key) {
		return Integer.valueOf(instance.properties.getProperty(key)).intValue();
	}

	public static int getIntValue(String key, int defaultValue) {
		try {
			return getIntValue(key);
		} catch (Exception e) {
			log.error("Fail to get int value of {}", key);
		}
		return defaultValue;
	}

	public static String getPlaceHolderString(String key) {
		if (key == null)
			return null;
		else
			return (new StringBuilder()).append("${").append(key).append("}")
					.toString();
	}

	public static String getPlaceHolderKey(String s) {
		if (s == null)
			return null;
		if (s.startsWith("${") && s.endsWith("}"))
			return s.substring("${".length(), s.length() - "}".length());
		else
			return s;
	}

}
