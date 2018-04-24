package com.cnpc.pms.base.entity;

import java.io.IOException;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.util.ClassUtils;

import com.cnpc.pms.base.springbean.helper.DataSourceConfigurer;

public class PMSAnnotationSessionFactoryBean extends AnnotationSessionFactoryBean {

	public static final String DATASOURCE_KEY_NAME = "entityDataSource";

	private final Logger log = LoggerFactory.getLogger(getClass());
	private String entityDataSource;
	private boolean isMainDataSource = true;
	private String[] packagesToScan;
	private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private static final String RESOURCE_PATTERN = "**/*.class";
	private TypeFilter[] entityTypeFilters = new TypeFilter[] { new AnnotationTypeFilter(Entity.class, false),
			new AnnotationTypeFilter(Embeddable.class, false), new AnnotationTypeFilter(MappedSuperclass.class, false) };

	public void setEntityDataSource(String entityDataSource) {
		this.entityDataSource = entityDataSource;
		this.isMainDataSource = DataSourceConfigurer.isMainDataSource(entityDataSource);// StrUtil.isEmpty(this.entityDataSource);
	}

	@Override
	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	@Override
	public void setEntityTypeFilters(TypeFilter[] entityTypeFilters) {
		this.entityTypeFilters = entityTypeFilters;
	}

	@Override
	protected void scanPackages(AnnotationConfiguration config) {
		log.debug("{}", this.getDataSource().toString());
		if (this.packagesToScan != null) {
			try {

				for (String pkg : this.packagesToScan) {
					String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
							+ ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
					Resource[] resources = this.resourcePatternResolver.getResources(pattern);
					MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
					inner: for (Resource resource : resources) {
						if (resource.isReadable()) {
							MetadataReader reader = readerFactory.getMetadataReader(resource);
							String className = reader.getClassMetadata().getClassName();

							Class<?> clazz = Class.forName(className);
							AlternativeDS ds = clazz.getAnnotation(AlternativeDS.class);
							if (isMainDataSource) {
								if (ds != null) {
									// README: Comment out 2012/01/12 to support Query on alternative datasource;
									// Without registered entity Hibernate will not execute query even the datasource
									// changed.
									// log.debug("Main DataSource Skip @Alternative Entity [{}]", className);
									// continue inner;
								}
							} else {
								if (ds == null) {
									log.debug("DataSource[{}] Skip Normal Entity [{}]", this.entityDataSource,
											className);
									continue inner;
								} else if (ds.source().equals(this.entityDataSource) == false) {
									log.debug("DataSource[{}] Skip OtherDS[{}] Entity [{}]", new Object[] {
											this.entityDataSource, ds.source(), className });
									continue inner;
								}
							}

							if (matchesFilter(reader, readerFactory)) {
								config.addAnnotatedClass(this.resourcePatternResolver.getClassLoader().loadClass(
										className));
								registerEntityHelper(clazz);
							}
						}
					}
				}
			} catch (IOException ex) {
				throw new MappingException("Failed to scan classpath for unlisted classes", ex);
			} catch (ClassNotFoundException ex) {
				throw new MappingException("Failed to load annotated classes from classpath", ex);
			}
		}
	}

	private boolean matchesFilter(MetadataReader reader, MetadataReaderFactory readerFactory) throws IOException {
		if (this.entityTypeFilters != null) {
			for (TypeFilter filter : this.entityTypeFilters) {
				if (filter.match(reader, readerFactory)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Register Entity Class to EntityHelper
	 * 
	 * @param clazz
	 */
	private void registerEntityHelper(Class<?> clazz) {
		if (isMainDataSource && clazz.isAnnotationPresent(Entity.class)) {
			EntityHelper.registerEntity(clazz);
		}
	}

	public SessionFactory getSessionFactory4JTA() {
		return super.getSessionFactory();
	}
}
