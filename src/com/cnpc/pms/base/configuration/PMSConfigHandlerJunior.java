package com.cnpc.pms.base.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.Resource;

import com.cnpc.pms.base.configuration.registry.ManagerRegisterHelper;
import com.cnpc.pms.base.configuration.registry.RegistrarAgent;
import com.cnpc.pms.base.util.ConfigurationUtil;
import com.cnpc.pms.base.util.SpringHelper;

/**
 * Inject beans according to pmsconfig.xml file
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Xiang ZhongMing
 * @since 2011-4-25
 */
public class PMSConfigHandlerJunior {

	/** The Constant DEFAULT_CONFIG_LOCATION. */
	public static final String DEFAULT_CONFIG_LOCATION = "/module/*.xml";
	private static final Logger log = LoggerFactory.getLogger(PMSConfigHandlerJunior.class);

	/**
	 * Initial pms config.
	 */
	public synchronized static void initialPMSConfig() {
		RegistrarAgent agent =
						new RegistrarAgent((BeanDefinitionRegistry) SpringHelper.getApplicationContext(),
											SpringHelper.getApplicationContext().getClassLoader());
		Resource[] resources = ConfigurationUtil.getAllResources(DEFAULT_CONFIG_LOCATION);
		if (resources != null) {
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				log.info("Loading auto register beans from {}", resource);
				agent.register(resource);
			}
		}
		log.info("Start to generate default managers for those entities with no manager defined");
		ManagerRegisterHelper.registerManagerByEntity((BeanDefinitionRegistry) SpringHelper.getApplicationContext(),
			(BeanDefinitionRegistry) SpringHelper.getApplicationContext());
		log.info("After auto-register. Start to print out the beans:");
	}

}
