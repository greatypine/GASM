package com.cnpc.pms.base.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import com.cnpc.pms.base.springbean.helper.DataSourceConfigurer;

public class EntityHelper {

	private static final Logger log = LoggerFactory.getLogger(EntityHelper.class);

	private static EntityHelper instance = new EntityHelper();
	private final Map<String, Class<?>> entityMapping;

	private EntityHelper() {
		entityMapping = new HashMap<String, Class<?>>();
	}

	synchronized static void registerEntity(Class<?> clazz) {
		Class<?> overwrited = instance.entityMapping.put(ClassUtils.getShortName(clazz.getCanonicalName()), clazz);
		if (overwrited != null) {
			log.warn("Duplicated Entity Class Registered! {}", overwrited);
		}
	}

	public static Map<String, Class<?>> getEntityMapping() {
		return instance.entityMapping;
	}

	public static Class<?> getClass(String className) throws ClassNotFoundException {
		if (instance.entityMapping.containsKey(className)) {
			return instance.entityMapping.get(className);
		} else {
			return Class.forName(className);
		}
	}

	public static void print() {
		log.info("#############All Registered Entity################");
		Set<String> keys = new TreeSet<String>(instance.entityMapping.keySet());
		for (String key : keys) {
			Class<?> clazz = instance.entityMapping.get(key);
			AlternativeDS ds = clazz.getAnnotation(AlternativeDS.class);
			log.info("|-{} \t= [{},{}] {}", new Object[] {key, DataSourceConfigurer.isMainDataSource(ds) ? "T" : "F",
					ds == null ? "" : ds.source(), clazz.getCanonicalName() });
		}
		log.info("\\- There are {} classes registered.", instance.entityMapping.size());
	}
}
