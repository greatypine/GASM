package com.cnpc.pms.base.junit;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.cnpc.pms.base.common.context.PMSApplicationContext;
import com.cnpc.pms.base.util.Slf4jConfigurer;
import com.cnpc.pms.base.util.SpringHelper;

/**
 * The test cases based on Spring Framework should inherited from PMSTestCase. <BR />
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/23
 */
public abstract class PMSTestCase extends TestCase {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	/** The servlet context. */
	protected ServletContext servletContext = new MockServletContext("/webapp", new FileSystemResourceLoader());

	/**
	 * Gets the config location.
	 * 
	 * @return the config location
	 */
	protected String[] getConfigLocation() {
		String[] config = new String[] { "classpath*:/conf/appContext.xml", "classpath*:/conf/appContext-database.xml" };
		return config;
	}

	/**
	 * Sets the up.
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() {
		try {
			this.initializeIfNecessary();
		} catch (Exception e) {
			fail("Failed to initialize:" + e);
		}
	}

	/**
	 * Initialize if necessary.
	 */
	protected void initializeIfNecessary() {
		if (SpringHelper.getApplicationContext() == null) {
			synchronized (SpringHelper.class) {
				if (SpringHelper.getApplicationContext() == null) {
					try {
						Slf4jConfigurer.initLogging();
						XmlWebApplicationContext ctx = new XmlWebApplicationContext();
						ctx.setConfigLocations(getConfigLocation());
						ctx.setServletContext(servletContext);
						ctx.refresh();
						SpringHelper.setRootContext(ctx);
						PMSApplicationContext.getInstance().initialize();
					} catch (Exception e) {
						e.printStackTrace();
						fail(e.getMessage());
					}
				}
			}
		}
	}
}
