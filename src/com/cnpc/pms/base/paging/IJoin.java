package com.cnpc.pms.base.paging;

import java.io.Serializable;

/**
 * The Interface IJoin.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public interface IJoin extends Serializable {

	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int INNER = 3;

	public abstract IJoin appendJoin(IJoin paramIJoin);

	public abstract String getConditions();

	public abstract String getJoinedClasses();

	public abstract IJoin getNext();

	public abstract String getAlias();

}
