package com.cnpc.pms.base.common.filter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.cnpc.pms.base.common.context.PMSApplicationContext;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.base.util.web.ResourceWebConfigurer;
import com.cnpc.pms.base.util.web.Slf4jWebConfigurer;

/**
 * The listener interface for receiving PMSContextLoader events. The class that
 * is interested in processing a PMSContextLoader event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's <code>addPMSContextLoaderListener</code>
 * method. When the PMSContextLoader event occurs, that object's appropriate
 * method is invoked.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation ,
 * http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 * 
 */
public class PMSContextLoaderListener implements ServletContextListener {

	/** The context loader. */
	private ContextLoader contextLoader;

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(PMSContextLoaderListener.class);

	/**
	 * Close the root web application context.
	 * 
	 * @param event
	 *            the event
	 */
	public void contextDestroyed(ServletContextEvent event) {
		if (this.contextLoader != null) {
			this.contextLoader.closeWebApplicationContext(event.getServletContext());
		}
	}

	/**
	 * Initialize the root web application context & child generic web
	 * application context.
	 * 
	 * @param event
	 *            the event
	 */
	public void contextInitialized(ServletContextEvent event) {
		log.debug("Start Initialize Web PMS Context");
		ResourceWebConfigurer.setDominatorConfig(event.getServletContext());
		Slf4jWebConfigurer.initLogging(event.getServletContext());
		log.debug("Start Initialize Web ContextLoader ====>");
		contextLoader = new ContextLoader();
		contextLoader.initWebApplicationContext(event.getServletContext());
		log.debug("<====End of Initialize Web ContextLoader");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		SpringHelper.setRootContext(wac);
		log.debug("<====End of Initialize GenericWebApplicationContext");
		PMSApplicationContext.getInstance().initialize();
	}

}
