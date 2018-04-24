package com.cnpc.pms.base.dynamicds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentDSContext {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(CurrentDSContext.class);
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setCurrent(String currentDS) {
		contextHolder.set(currentDS);
	}

	public static String getCurrent() {
		return contextHolder.get();
	}

	public static void clear() {
		contextHolder.remove();
	}

	public static void switchToDefault() {
		setCurrent(DynamicDataSource.DEFAULT_DATA_SOURCE);
	}
}
