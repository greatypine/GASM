package com.cnpc.pms.base.paging.impl;

import com.cnpc.pms.base.entity.IEntity;
import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.util.StrUtil;

/**
 * Support Join like:
 * Select t1.id,t2.id from table1 t1, table2 t2 where t1.name=t2.owner
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class PropertiesJoin extends AbstractJoin {

	/** The Constant serialVersionUID. @serial */
	private static final long serialVersionUID = 1L;

	/** The class name. */
	private String className; // the class to be joined

	/** The logic name. */
	private String joinerAlias; // alias for that class

	/** The properties. */
	private String[] properties;

	/** The by logic name. */
	private String mainAlias; // by which the class joined

	/** The by properties. */
	private String[] mainProperties;

	/**
	 * Instantiates a new join.
	 * 
	 * @param clazz
	 *            - the joiner class
	 * @param joinerAlias
	 * @param properties
	 * @param mainAlias
	 * @param mainProperties
	 * @param joinType
	 */
	public PropertiesJoin(Class<? extends IEntity> clazz, String joinerAlias, String[] properties, String mainAlias,
			String[] mainProperties) {
		if (properties == null) {
			properties = new String[] {};
		}
		if (mainProperties == null) {
			mainProperties = new String[] {};
		}
		if (properties.length != mainProperties.length) {
			throw new InvalidFilterException("The given properies donot match alias target.");
		}
		this.className = clazz.getSimpleName();
		this.joinerAlias = joinerAlias;
		if (StrUtil.isBlank(this.joinerAlias)) {
			this.joinerAlias = "_joiner_" + System.currentTimeMillis();
		}
		this.mainAlias = mainAlias;
		this.properties = properties;
		this.mainProperties = mainProperties;
	}

	@Override
	protected String getSelfJoinClause() {
		StringBuffer buf = new StringBuffer();
		buf.append(", ").append(className).append(" AS ").append(joinerAlias);
		return buf.toString();
	}

	@Override
	protected String getSelfConditions() {
		StringBuffer buf = new StringBuffer();
		buf.append(" 1=1 ");
		for (int i = 0; i < mainProperties.length; i++) {
			buf.append(" AND ").append(mainAlias).append('.').append(mainProperties[i]).append(" = ")
					.append(joinerAlias).append('.').append(properties[i]);
		}
		return buf.toString();
	}

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return joinerAlias;
	}

}