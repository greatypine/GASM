package com.cnpc.pms.base.paging;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.cnpc.pms.base.paging.impl.Sort;

/**
 * Data Sorting Information.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn/
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/27
 */
@JsonDeserialize(as = Sort.class)
public interface ISort extends Serializable {

	/** The Constant ASC. */
	public static final int ASC = 1;

	/** The Constant DESC. */
	public static final int DESC = -1;

	/**
	 * Append sort.
	 * 
	 * @param next
	 *            the next
	 * @return the i sort
	 */
	ISort appendSort(ISort next);

	/**
	 * Gets the sort string.
	 * 
	 * @return the sort string
	 */
	String getString();
}
