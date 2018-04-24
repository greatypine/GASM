package com.cnpc.pms.base.paging.impl;

import com.cnpc.pms.base.paging.IJoin;

/**
 * Entity Related Join
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class EntityRelatedJoin extends AbstractJoin {

	private static final long serialVersionUID = 1L;

	/** The type. */
	private final int type;

	/** The logic name. */
	private final String joinerAlias; // alias for that class

	/** The by logic name. */
	private final String mainAlias; // by which the class joined

	/** The by properties. */
	private final String mainProperty;

	/**
	 * Instantiates a new join.
	 * 
	 * Select t.id,f.id from User AS t Left Outer Join t.family as f
	 * 
	 * @param joinerAlias
	 *            the join table alias name (f)
	 * @param mainAlias
	 *            the main table alias name (t)
	 * @param mainProperty
	 *            the join properties (family)
	 * @param joinType
	 *            the join type (left)
	 */
	public EntityRelatedJoin(String joinerAlias, String mainAlias, String mainProperty, int joinType) {
		this.joinerAlias = joinerAlias;
		this.mainAlias = mainAlias;
		this.mainProperty = mainProperty;
		this.type = joinType;
	}

	@Override
	protected String getSelfJoinClause() {
		StringBuffer buf = new StringBuffer();
		switch (type) {
		case IJoin.LEFT:
			buf.append(" LEFT OUTER JOIN ");
			break;
		case IJoin.RIGHT:
			buf.append(" RIGHT OUTER JOIN ");
			break;
		case IJoin.INNER:
			buf.append(" INNER JOIN ");
			break;
		default:
			break;
		}
		buf.append(" ").append(mainAlias).append('.').append(mainProperty).append(" AS ").append(joinerAlias);
		return buf.toString();
	}

	@Override
	protected String getSelfConditions() {
		return " 1=1 ";
	}

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return joinerAlias;
	}

}