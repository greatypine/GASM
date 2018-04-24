package com.cnpc.pms.base.dao.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.IJoin;
import com.cnpc.pms.base.paging.IPage;
import com.cnpc.pms.base.paging.ISort;

/**
 * TODO: Join(add new type join based on IFilter), Distinct
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Jul 26, 2013
 */
public interface IDAORoot {
	/**
	 * Execute a query based on the given example entity object.
	 * 
	 * @param exampleEntity
	 * @return a List containing 0 or more persistent instances
	 */
	<T> List<T> findByExample(T exampleEntity);

	/**
	 * Generic method to get an object based on class and identifier.
	 * Return null if not found.
	 * 
	 * @param clazz
	 * @param id
	 * @return the object with the id or null if not found
	 */
	<T> T get(Class<T> clazz, Serializable id);

	/**
	 * Get identifier of object.
	 * 
	 * @param o
	 *            the o
	 * @return id of object
	 */
	Serializable getId(Object o);

	/**
	 * Check if an object is not exist in database.
	 * 
	 * @param o
	 *            the o
	 * @return true, if is new object
	 */
	boolean isNew(Object o);

	/**
	 * Generic method used to get all objects list of a particular type.
	 * 
	 * @param clazz
	 *            the type of objects (a.k.a. while class) to get data from
	 * @return a List containing 0 or more persistent instances
	 */
	<T> List<T> getList(Class<T> clazz);

	/**
	 * Generic method used to get objects list of a particular type by FSP.
	 * 
	 * @param clazz
	 *            the type of objects (a.k.a. while class) to get data from
	 * @param fsp
	 *            FSP object used to query objects.
	 * @return the objects
	 */
	<T> List<T> getList(Class<T> clazz, FSP fsp);

	/**
	 * Generic method used to get objects list of a particular type by filter.
	 * 
	 * @param clazz
	 *            the type of objects (a.k.a. while class) to get data from
	 * @param filter
	 *            filter object used to query objects.
	 * @param sort
	 *            the sort
	 * @param page
	 *            the page
	 * @return List of populated objects
	 */
	<T> List<T> getList(Class<T> clazz, IFilter filter, ISort sort, IPage page);

	/**
	 * Generic method used to get objects iterator of a particular type by IJoin
	 * and fsp.
	 * 
	 * @param clazz
	 *            the type of objects to get data from
	 * @param alias
	 *            Alias that will be used for clazz
	 * @param fsp
	 *            FSP object used to query objects.
	 * @param join
	 *            IJoin object.
	 * @return the objects
	 */
	<T> List<T> getListWithJoin(Class<T> clazz, String alias, FSP fsp, IJoin join);

	/**
	 * Generic method used to get unique objects of a particular type or null if the query returns no results.
	 * If filter can not unique a object (multiple object match the filter) then would throw an exception.
	 * 
	 * @param clazz
	 *            the type of objects (a.k.a. while class) to get data from
	 * @param filter
	 *            IFilter object.
	 * @return the unique object
	 */
	<T> T getUnique(Class<T> clazz, IFilter filter);

	/**
	 * Refresh object from database.
	 * 
	 * @param o
	 *            the o
	 */
	void refresh(Object o);

	/**
	 * Generic method to delete an object.
	 * 
	 * @param o
	 *            the object to remove
	 */
	void remove(Object o);

	/**
	 * Generic method to delete an object based on class and id.
	 * 
	 * @param clazz
	 *            model class to lookup
	 * @param id
	 *            the identifier (primary key) of the class
	 */
	void removeById(Class<?> clazz, Serializable id);

	/**
	 * Generic method to save an object - handles both update and insert.
	 * 
	 * @param o
	 *            the object to save
	 */
	void save(Object o);

	/**
	 * Update/delete all objects according to the given query,
	 * binding a number of values to "?" parameters in the query string.
	 * 
	 * @param hql
	 *            update or delete HQL
	 * @param params
	 * @return the size of operated
	 */
	int bulkOperate(String hql, Object[] params);

	// ################# New API since 2013.7.22 ###############
	/**
	 * Get select information in a query.
	 * 
	 * @param clazz
	 *            class
	 * @param filter
	 *            IFilter
	 * @param selectString
	 *            select primitive.
	 * @return statistics information.
	 */
	List<?> queryProperties(Class<?> clazz, IFilter filter, ISort sort, IPage page, String selectString);

	/**
	 * Get the select result as a List of Map, each record is mapped to a map
	 * 
	 * @param selectString
	 * @param params
	 * @param page
	 * @return
	 */
	List<Map<String, ?>> querySQL(String selectString, List<?> params, IPage page);

	/**
	 * Get the select result as a List of List, each record's field is stored in a list
	 * 
	 * @param selectString
	 * @param params
	 * @param page
	 * @return
	 */
	List<List<?>> querySQLAsList(String selectString, List<?> params, IPage page);
}
