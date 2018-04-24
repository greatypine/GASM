package com.cnpc.pms.base.manager;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.exception.PMSManagerException;
import com.cnpc.pms.base.manager.core.IManagerRoot;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.IJoin;

/**
 * The Interface IManager.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public interface IManager extends IManagerRoot {

	IDAO getDao();

	// ########## Unhandled method ##########

	/**
	 * Iterate objects.
	 * 
	 * @param clazz
	 *            the clazz
	 * @param alias
	 *            the alias
	 * @param join
	 *            the join
	 * @param fsp
	 *            the fsp
	 * @return the iterator
	 */
	Iterator<?> iterateObjectsWithJoin(Class<?> clazz, String alias, IJoin join, FSP fsp);

	// ########################################
	// Following are shadowed methods see IManagerRoot
	/**
	 * Create a new instance of entity class.
	 * 
	 * @return the object
	 * @throws PMSManagerException
	 * 
	 */
	Object createNewObject() throws PMSManagerException;

	/**
	 * Get an object based on identifier.
	 * 
	 * @param id
	 *            the identifier (primary key) of the class
	 * @return a populated object
	 */
	Object getObject(Serializable id);

	/**
	 * Get an object's identifier.
	 * 
	 * @param o
	 *            the o
	 * @return the object id
	 * 
	 */
	Serializable getObjectId(Object o);

	/**
	 * Get all objects.
	 * 
	 * @return List of populated objects
	 */
	List<?> getObjects();

	/**
	 * Get list of objects based on FSP (Filter, Sorting and Pagnination).
	 * 
	 * @param fsp
	 *            the fsp
	 * @return the objects
	 */
	List<?> getObjects(FSP fsp);

	/**
	 * Get list of objects based on filter.
	 * 
	 * @param filter
	 *            the filter
	 * @return the objects
	 */
	List<?> getObjects(IFilter filter);

	/**
	 * Get single object based on filter.
	 * 
	 * @param filter
	 *            the filter
	 * @return the unique object
	 */
	Object getUniqueObject(IFilter filter);

	/**
	 * Refresh an object.
	 * 
	 * @param o
	 *            the object to refresh
	 */
	void refreshObject(Object o);

	/**
	 * Delete an object.
	 * 
	 * @param o
	 *            the object to remove
	 */
	void removeObject(Object o);

	/**
	 * Delete an object based on id.
	 * 
	 * @param id
	 *            the identifier of the class
	 */
	void removeObjectById(Serializable id);

	/**
	 * Save or update an object.
	 * 
	 * @param o
	 *            the object to save
	 */
	void saveObject(Object o);

	// ##########################################
	// Deprecated methods, keep for compatibility with eProcurement
	/**
	 * 
	 * 请使用本Entity相关联的Manager，直接调用manager.getObjects(fsp).<br/>
	 * For Multithreading Reason,Add This method.
	 * 
	 * @param entityClass
	 *            the entity class
	 * @param fsp
	 *            the fsp
	 * @return The List with the special entity.
	 * 
	 */
	@Deprecated
	List<?> getObjects(Class<?> entityClass, FSP fsp);

	/**
	 * 请使用本Entity相关联的Manager，直接调用manager.removeObjectById(id).<br/>
	 * Removes the object by class and id.
	 * Note: will not invoke the pre- & post- defined in the Manager.
	 * 
	 * @param clazz
	 *            the class
	 * @param id
	 *            the id
	 */
	// XXX: should delete this method?
	// will not invoke the pre- & post- defined in the Manager.
	void removeObjectById(Class<?> clazz, Serializable id);

	// XXX: REMOVE the following methods!
	@Deprecated
	public Object getObj(String queryId, Long id) throws ClassNotFoundException;

	@Deprecated
	public void saveObj(String queryId, String objectJsonString) throws Exception;

	@Deprecated
	public void removeObj(String queryId, Long id) throws ClassNotFoundException;

	// Remove after 2013.12.31
	// @Deprecated
	// void setEntityClassName(String entityClassName) throws PMSManagerException;
	// void validate() throws PMSManagerException;

	// Comment at 2013.7.26
	// No use. Just call getObjects.iterate
	/**
	 * Iterate objects based on FSP (Filter, Sorting and Pagnination). Normally,
	 * iterate will give better performance when you are using cache system.
	 * 
	 * @param fsp
	 *            the fsp
	 * @return the iterator
	 */

	// Iterator<?> iterateObjects(FSP fsp);

	/**
	 * Iterate objects based on filter. Normally, iterate will give better
	 * performance when you are using cache system.
	 * 
	 * @param filter
	 *            the filter
	 * @return the iterator
	 */
	// Iterator<?> iterateObjects(IFilter filter);

}
