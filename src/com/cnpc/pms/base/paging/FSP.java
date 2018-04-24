package com.cnpc.pms.base.paging;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.security.SecuredFilter;

/**
 * The Class FSP.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class FSP implements Serializable {

	/** The Constant serialVersionUID. @serial */
	private static final long serialVersionUID = -5365072213157772600L;

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(FSP.class);

	/** The default filter. */
	private IFilter defaultFilter = null;

	/** The user filter. */
	private IFilter userFilter;

	/** The sort. */
	private ISort sort;

	/** The page. */
	private IPage page;

	/** The sec filter. */
	private SecuredFilter secFilter = null;

	/** The clazz. */
	private Class<?> clazz;

	/**
	 * Inits the.
	 * 
	 * @param clazz
	 *            the clazz
	 */
	public void init(Class<?> clazz) {
		init(clazz, null);
	}

	/**
	 * Inits the.
	 * 
	 * @param clazz
	 *            the clazz
	 * @param alias
	 *            the logic name
	 */
	public void init(Class<?> clazz, String alias) {
		this.setClazz(clazz);
		secFilter = new SecuredFilter(clazz, alias);
	}

	/**
	 * Gets the filter.
	 * 
	 * @return Returns the user filter.
	 * @throws InvalidFilterException
	 */
	public IFilter getFilter() {

		// IFilter filter;
		// if (getSecuredFilter() != null) {
		// filter = new SimpleFilter(ConditionOperator.AND, getUserFilter(), getSecuredFilter());
		// } else {
		// filter = getUserFilter();
		// }
		//
		// if (defaultFilter != null) {
		// return new SimpleFilter(ConditionOperator.AND, defaultFilter, filter);
		// } else {
		// return filter;
		// }

		IFilter filter = getUserFilter();
		if (filter != null) {
			try {
				filter = filter.appendAnd(getSecuredFilter()).appendAnd(defaultFilter);
			} catch (InvalidFilterException e) {
				log.error("get Filter error: {}", e);
			}
		}
		return filter;
	}

	/**
	 * Gets the default filter.
	 * 
	 * @return the default filter
	 */
	public IFilter getDefaultFilter() {
		return defaultFilter;
	}

	public IPage getPage() {
		return page;
	}

	/**
	 * Gets the secured filter.
	 * 
	 * @return the secured filter
	 */
	protected SecuredFilter getSecuredFilter() {
		return null;
		// XXX 等SecuredFilter完善后考虑使用
		// try {
		// if (secFilter == null && clazz != null) {
		// secFilter = new SecuredFilter(clazz);
		// // secFilter.setPrivilege(privilege);
		// }
		// if (secFilter != null && secFilter.getValues() == null) {
		// secFilter = null;
		// }
		// } catch (InvalidFilterException e) {
		// log.debug("getSecuredFilter error.{}", e);
		// }
		// return secFilter;
	}

	public ISort getSort() {
		return sort;
	}

	public IFilter getUserFilter() {
		return userFilter;
	}

	public void setDefaultFilter(IFilter defaultFilter) {
		this.defaultFilter = defaultFilter;
	}

	public void setPage(IPage page) {
		this.page = page;
	}

	public void setSort(ISort sort) {
		this.sort = sort;
	}

	public void setUserFilter(IFilter filter) {
		this.userFilter = filter;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

}
