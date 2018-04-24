package com.cnpc.pms.base.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.cnpc.pms.base.dao.core.IDAORoot;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.IJoin;
import com.cnpc.pms.base.paging.IPage;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.query.model.PMSQuery;


/**
 * The Interface IDAO.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public interface IDAO extends IDAORoot {

	/**
	 * Execute a query based on the given example entity object.
	 * 
	 * @param exampleEntity
	 * @return a List containing 0 or more persistent instances
	 */
	Object findObjects(Object exampleEntity);

	/**
	 * Generic method to get an object based on class and identifier. An
	 * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
	 * found.
	 * 
	 * @param clazz
	 *            model class to lookup
	 * @param id
	 *            the identifier (primary key) of the class
	 * @return a populated object
	 * @see org.springframework.orm.ObjectRetrievalFailureException
	 */
	Object getObject(Class<?> clazz, Serializable id);

	/**
	 * Get identifier of object.
	 * 
	 * @param o
	 *            the o
	 * @return id of object
	 */
	Serializable getObjectId(Object o);

	/**
	 * Generic method used to get all objects list of a particular type.
	 * 
	 * @param clazz
	 *            the type of objects (a.k.a. while class) to get data from
	 * @return the objects
	 */
	@SuppressWarnings("rawtypes")
	List getObjects(Class<?> clazz);

	/**
	 * Generic method used to get objects list of a particular type by FSP.
	 * 
	 * @param clazz
	 *            the type of objects (a.k.a. while class) to get data from
	 * @param fsp
	 *            FSP object used to query objects.
	 * @return the objects
	 */
	@SuppressWarnings("rawtypes")
	List getObjects(Class<?> clazz, FSP fsp);

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
	@SuppressWarnings("rawtypes")
	List getObjects(Class<?> clazz, IFilter filter, ISort sort, IPage page);

	/**
	 * Get Statistics information in a query.
	 * 
	 * @param clazz
	 *            class
	 * @param filter
	 *            IFilter
	 * @param selectString
	 *            select primitive.
	 * @return statistics information.
	 */
	@SuppressWarnings("rawtypes")
	List getStatisticsInfo(Class<?> clazz, IFilter filter, String selectString);

	/**
	 * Generic method used to get unique objects of a particular type. Be
	 * careful with this method. If filter can not unique a object (multiple
	 * object match the filter) User you get exception.
	 * 
	 * @param clazz
	 *            the type of objects (a.k.a. while class) to get data from
	 * @param filter
	 *            IFilter object.
	 * @return the unique object
	 */
	Object getUniqueObject(Class<?> clazz, IFilter filter);

	/**
	 * Check if an object is not exist in datasource.
	 * 
	 * @param o
	 *            the o
	 * @return true, if is new object
	 */
	boolean isNew(Object o);

	/**
	 * Generic method used to get objects iterator of a particular type by IJoin
	 * and fsp.
	 * 
	 * @param clazz
	 *            the type of objects to get data from
	 * @param alias
	 *            Alias that will be used for clazz
	 * @param join
	 *            IJoin object.
	 * @param fsp
	 *            FSP object used to query objects.
	 * @return the objects
	 */
	@SuppressWarnings("rawtypes")
	Iterator iterateObjectsWithJoin(Class<?> clazz, String alias, IJoin join, FSP fsp);

	/**
	 * Refresh object from datasource.
	 * 
	 * @param o
	 *            the o
	 */
	void refreshObject(Object o);

	/**
	 * Generic method to delete an object.
	 * 
	 * @param o
	 *            the object to remove
	 */
	void removeObject(Object o);

	/**
	 * Generic method to delete an object based on class and id.
	 * 
	 * @param clazz
	 *            model class to lookup
	 * @param id
	 *            the identifier (primary key) of the class
	 */
	void removeObjectById(Class<?> clazz, Serializable id);

	/**
	 * Generic method to save an object - handles both update and insert.
	 * 
	 * @param o
	 *            the object to save
	 */
	void saveObject(Object o);

	/**
	 * 
	 * @param updateHQL
	 * @param params
	 * @return the size of updated
	 */
	int bulUpdate(String updateHQL, Object[] params);

	/**
	 * 
	 * @param deleteHQL
	 * @param params
	 * @return the size of deleted
	 */
	int bulDelete(final String deleteHQL, final Object[] params);

	/**
	 * get objects by query and FSP.
	 * to deal the distinct problem.
	 * 
	 * @param pmsQuery
	 * @param fsp
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List getObjects(PMSQuery pmsQuery, FSP fsp);

	/**
	 * Gets the objects count.
	 * 
	 * @param pmsQuery
	 *            the pms query
	 * @param filter
	 *            the filter
	 * @return the objects count
	 */
	Long getObjectsCount(final PMSQuery pmsQuery, final IFilter filter);

	Integer getCount(Class<?> targetClass, IFilter filter);

	Integer getDistinctCount(Class<?> targetClass, IFilter filter,
			String distinct);

	List getDistinctList(Class<?> targetClass, FSP fsp, String distinct);

	// ################################################
	// Removed methods

	/**
	 * Generic method used to get objects iterator of a particular type by FSP.
	 * 
	 * @param clazz
	 *            the type of objects (a.k.a. while class) to get data from
	 * @param fsp
	 *            FSP object used to query objects.
	 * @return the iterator
	 */
	// @SuppressWarnings("rawtypes")
	// Iterator iterateObjects(Class<?> clazz, FSP fsp);

	/**
	 * Generic method used to get objects iterator of a particular type by
	 * filter.
	 * 
	 * @param clazz
	 *            the type of objects (a.k.a. while class) to get data from
	 * @param filter
	 *            filter object used to query objects.
	 * @param sort
	 *            sorting object.
	 * @param page
	 *            pagination object.
	 * @return the iterator
	 */
	// @SuppressWarnings("rawtypes")
	// Iterator iterateObjects(Class<?> clazz, IFilter filter, ISort sort, IPage page);

	/**
	 * save audit object
	 * 
	 * @param businessObject
	 * @param auditObject
	 */
	// @Deprecated
	// void saveAuditObject(Object businessObject, Object auditObject);

}
