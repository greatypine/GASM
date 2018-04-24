package com.cnpc.pms.base.common.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;

import com.cnpc.pms.base.init.ExcelImpConfig;
import com.cnpc.pms.base.query.QueryDefinition;
import com.cnpc.pms.base.schedule.manager.TaskManager;
import com.cnpc.pms.base.schedule.quartz.PmsSchedulerFactoryBean;
import com.cnpc.pms.base.schedule.quartz.SchedulerHelper;
import com.cnpc.pms.base.util.SpringHelper;

/***
 * CNPC Application Context Loader.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 11.4.2010
 */
// TODO: Still keep this initialize() 3 times after SpringHelper.setRootContext. May need optimization.
public final class PMSApplicationContext {

	/** Logger available to subclasses. */
	private static final Logger log = LoggerFactory.getLogger(PMSApplicationContext.class);

	/** The Constant _instance. */
	private static final PMSApplicationContext instance = new PMSApplicationContext();

	/**
	 * Get PMSApplicationContext Instance.
	 * 
	 * @return singleton instance of PMSApplicationContext.
	 */
	public static PMSApplicationContext getInstance() {
		return instance;
	}

	/**
	 * Private Constructor.
	 */
	private PMSApplicationContext() {
		log.debug("Initialize PMSApplicationContext instance");
	}

	/**
	 * Initialize PMS Application.
	 */
	public void initialize() {
		// Moved to PMSAnnotationSessionFactoryBean.scanPackages()
		// see PMSAnnotationSessionFactoryBean.registerEntityHelper
		// we will cache the Entity at the beginning time now. 2012-01-19
		// log.debug("Start to collect entity classes ===>");
		// EntityHelper.getEntityMapping();
		// log.debug("<=== End of auto collect entity classes:");

		// Moved to SpringHelper.setRootContext. 2012-01-19
		// log.debug("Start to auto register spring beans ===>");
		// PMSConfigHandler.initialPMSConfig();
		// log.debug("<=== End of auto register spring beans:");

		log.debug("Start to cache query definitions ===>");
		QueryDefinition.getInstance();
		log.debug("<=== End of cache query definitions:");

		log.debug("Start to load excel import definitions ===>");
		ExcelImpConfig.getInstance();
		log.debug("<=== End of load excel import definitions:");

		//if (BeanFactoryUtils.beansOfTypeIncludingAncestors(SpringHelper.getApplicationContext(),
		//		PmsSchedulerFactoryBean.class).size() > 0) {
			//log.debug("Start to load scheduled tasks ===>");
			//((TaskManager) SpringHelper.getManagerByClass("taskManager")).refreshTask();
			//log.debug("<=== End of loading scheduled tasks. Now start the schedulers...");
			//SchedulerHelper.getInstance().start();
		//} else {
			//log.debug("There are no PmsSchedulerFactoryBean");
		//}

	}
}
