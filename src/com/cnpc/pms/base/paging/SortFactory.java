package com.cnpc.pms.base.paging;

import com.cnpc.pms.base.paging.impl.Sort;

/**
 * A factory for creating Sort objects.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class SortFactory {

	/**
	 * Creates a new Sort object.
	 * 
	 * @param varName
	 *            the var name
	 * @return the i sort
	 */
	public static ISort createSort(String varName) {
		ISort ret = new Sort(varName);
		return ret;
	}

	/**
	 * Creates a new Sort object.
	 * 
	 * @param varName
	 *            the var name
	 * @param direction
	 *            the direction
	 * @return the i sort
	 */
	public static ISort createSort(String varName, int direction) {
		ISort ret = new Sort(varName, direction);
		return ret;
	}

	/**
	 * Creates a new Sort object.
	 * 
	 * @param logicName
	 *            the logic name
	 * @param varName
	 *            the var name
	 * @param direction
	 *            the direction
	 * @return the i sort
	 */
	public static ISort createSort(String logicName, String varName, int direction) {
		ISort ret = new Sort(logicName, varName, direction);
		return ret;
	}
}
