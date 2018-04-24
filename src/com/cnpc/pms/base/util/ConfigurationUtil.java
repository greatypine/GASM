// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ConfigurationUtil.java

package com.cnpc.pms.base.util;

import com.cnpc.pms.base.exception.UtilityException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.xml.sax.InputSource;

public final class ConfigurationUtil {

	public ConfigurationUtil() {
	}

	public static Object parseXMLObject(Class clazz, Resource resource)
			throws UtilityException {
		try {
			java.io.InputStream is = resource.getInputStream();
			InputSource inputSource = new InputSource(is);
			DigesterLoader digesterLoader = (new DigesterLoaderBuilder())
					.useDefaultAnnotationRuleProviderFactory()
					.useDefaultDigesterLoaderHandlerFactory();
			Digester digester = digesterLoader.createDigester(clazz);
			return digester.parse(inputSource);
		} catch (Exception e) {
			log.error(
					"Fail to transfer Java Object[class: {}] from {}, reason: ",
					clazz.getSimpleName(), resource);
			e.printStackTrace();
			throw new UtilityException((new StringBuilder())
					.append("Fail to parse XML Object of ").append(resource)
					.toString(), e);
		}
	}

	public static Resource getSingleResource(String location) {
		Resource activeResource;
		location = prepareLocation(location);
		activeResource = null;
		Resource resources[];
		try {
			resources = loader.getResources(location);
			if (resources.length == 0) {
				log.warn("Can't find any [{}] :", location);
				return null;
			}
			Resource arr$[];
			int len$;
			int i$;
			while (true) {
				if (resources.length <= 1)
					break;
				arr$ = resources;
				len$ = arr$.length;
				i$ = 0;

				if (i$ >= len$)
					break;
				Resource resource = arr$[i$];
				if (isDominator(resource))
					return resource;
				i$++;
			}
			arr$ = resources;
			len$ = arr$.length;
			i$ = 0;
			_L2: while (true) {
				if (i$ >= len$)
					break;
				Resource resource = arr$[i$];
				if (resource.getURL().toString().indexOf("pmsbase") > -1)
					return resource;
				i$++;
			}

			activeResource = resources[0];
			log.debug("Use [{}] at {}", location, activeResource.getURI());
			return activeResource;
		} catch (IOException e) {
			log.error("Fail to find [{}] :", location, e);
		}
		return null;

	}

	public static Resource[] getSortedResources(String location) {
		Resource resources[];
		location = prepareLocation(location);
		resources = null;
		try {
		resources = loader.getResources(location);
		if (resources.length == 0) {
			log.warn("Can't find any [{}] :", location);
			return null;
		}
		
			int flag = 0;
			boolean handledFile = false;
			if (resources.length > 1) {
				for (int i = 0; i < resources.length || flag < 2; i++) {
					Resource resource = resources[i];
					if (isDominator(resource) && !handledFile) {
						if (i != resources.length - 1) {
							resources[i] = resources[resources.length - 1];
							resources[resources.length - 1] = resource;
							handledFile = true;
						}
					} else if (resource.getURI().toString().indexOf("pmsbase") > -1
							&& i != 0) {
						resources[i] = resources[0];
						resources[0] = resource;
					}
					flag++;
				}

			}
			log.debug("Get [{}] configuration at {}", location, resources);
		} catch (IOException e) {
			log.error("Fail to find [{}] :", location, e);
		}
		return resources;
	}

	public static Resource[] getAllResources(String location) {
		location = prepareLocation(location);
		Resource resources[] = null;
		try {
			resources = loader.getResources(location);
			log.debug("Get [{}] configuration at {}", location, resources);
		} catch (IOException e) {
			log.error("Fail to find [{}] :", location, e);
		}
		return resources;
	}

	private static String prepareLocation(String location) {
		if (!location.startsWith("classpath"))
			location = (new StringBuilder()).append("classpath*:")
					.append(location).toString();
		return location;
	}

	private static boolean isDominator(Resource resource) throws IOException {
		if (dominator == null)
			return resource.getURL().getProtocol().equals("file");
		if (resource.getURL().getProtocol().equals("file"))
			return true;
		else
			return resource.getURL().getFile().contains(dominator);
	}

	public static void setDominator(String d) {
		if (dominator == null) {
			log.info("Set Dominative Configurer Path: {}", d);
			dominator = d;
		} else {
			log.warn(
					"The dominator has already bean set as {}, and now try to set as {} which will be ignored.",
					dominator, d);
		}
	}

	public static final String DEFAULT_CONFIG_PACKAGE = "pmsbase";
	private static Logger log = LoggerFactory
			.getLogger(ConfigurationUtil.class);
	private static PathMatchingResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
	private static String dominator;

}
