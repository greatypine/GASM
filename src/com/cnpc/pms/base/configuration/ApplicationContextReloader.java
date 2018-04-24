package com.cnpc.pms.base.configuration;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.cnpc.pms.base.util.SpringHelper;

public class ApplicationContextReloader {

	protected static final Logger log = LoggerFactory.getLogger(ApplicationContextReloader.class);

	public static void refresh() throws IOException {
		log.debug("Refresh ApplicationContext.");
		ApplicationContext applicationContext = SpringHelper.getApplicationContext();
		if (applicationContext != null && applicationContext instanceof ConventionApplicationContext) {
			ConventionApplicationContext childBean = (ConventionApplicationContext) applicationContext;
			childBean.reloadClasses();
		} else {
			log.info("ApplicationContext is null or not a ConfigurableApplicationContext type: {}", applicationContext);
		}
	}
}
