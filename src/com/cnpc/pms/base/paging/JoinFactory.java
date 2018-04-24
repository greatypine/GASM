package com.cnpc.pms.base.paging;

import com.cnpc.pms.base.entity.IEntity;
import com.cnpc.pms.base.paging.impl.EntityRelatedJoin;
import com.cnpc.pms.base.paging.impl.PropertiesJoin;

/**
 * A factory for creating Join objects.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class JoinFactory {

	/**
	 * Creates a new Join object.
	 * 
	 * @param joinerClass
	 *            the clazz
	 * @param joinerAlias
	 *            the alias
	 * @param properties
	 *            the properties
	 * @param mainAlias
	 *            the by alias
	 * @param mainProperties
	 *            the by properties
	 * @param joinType
	 *            the join type (not used here)
	 * @return the i join
	 */
	public static IJoin createJoin(Class<? extends IEntity> joinerClass, String joinerAlias, String[] properties,
			String mainAlias, String[] mainProperties, int joinType) {

		return new PropertiesJoin(joinerClass, joinerAlias, properties, mainAlias, mainProperties);
	}

	public static IJoin createEntityRelatedJoin(String joinerAlias, String mainAlias, String joinProperty, int joinType) {
		return new EntityRelatedJoin(joinerAlias, mainAlias, joinProperty, joinType);
	}
}
