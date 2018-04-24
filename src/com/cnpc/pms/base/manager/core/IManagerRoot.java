package com.cnpc.pms.base.manager.core;

import java.io.Serializable;
import java.util.List;

import com.cnpc.pms.base.dao.core.IDAORoot;
import com.cnpc.pms.base.exception.PMSManagerException;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.IFilter;

/**
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Jul 22, 2013
 */
public interface IManagerRoot {

	// ############### Original API #############

	/**
	 * Gets the dao.
	 * 
	 * @return the dao
	 */
	IDAORoot getDao();

	/**
	 * Sets the dao.
	 * 
	 * @param dao
	 *            the dao to set
	 */
	void setDao(IDAORoot dao);

	/**
	 * Gets the entity class.
	 * 
	 * @return the entityClass
	 */
	Class<?> getEntityClass();

	/**
	 * Set Entity.
	 * 
	 * @param entityClass
	 *            the new entity class
	 */
	void setEntityClass(Class<?> entityClass);

	/**
	 * Agent for invoke methods in this class in a new Transaction
	 * 
	 * @param methodName
	 * @param args
	 * @return
	 */
	Object newTx(String methodName, Object... args) throws PMSManagerException;

	/**
	 * Check if the object doesn't exist in datasource.
	 * 
	 * @param o
	 *            the object to check
	 * @return true if the object exists in datasource.
	 */
	boolean isNew(Object o);

	// ########### Refactory API at 2013.7.22 ###########
	/**
	 * Create a new instance of entity class.
	 * 
	 * @return the object
	 * @throws PMSManagerException
	 */
	Object create() throws PMSManagerException;

	/**
	 * Get an object based on identifier.
	 * 
	 * @param id
	 *            the identifier (primary key) of the class
	 * @return a populated object
	 */
	Object get(Serializable id);

	/**
	 * Get an object's identifier.
	 * 
	 * @param o
	 *            the o
	 * @return the object id
	 */
	Serializable getId(Object o);

	/**
	 * Get all objects.
	 * 
	 * @return List of populated objects
	 */
	List<?> getList();

	/**
	 * Get list of objects based on FSP (Filter, Sorting and Pagnination).
	 * 
	 * @param fsp
	 *            the fsp
	 * @return the objects
	 */
	List<?> getList(FSP fsp);

	/**
	 * Get list of objects based on filter.
	 * 
	 * @param filter
	 *            the filter
	 * @return the objects
	 */
	List<?> getList(IFilter filter);

	/**
	 * Get single object based on filter.
	 * 
	 * @param filter
	 *            the filter
	 * @return the unique object
	 */
	Object getUnique(IFilter filter);

	/**
	 * Refresh an object.
	 * 
	 * @param o
	 *            the object to refresh
	 */
	void refresh(@DynamicType Object o);

	/**
	 * Delete an object.
	 * 
	 * @param o
	 *            the object to remove
	 */
	void remove(@DynamicType Object o);

	/**
	 * Delete an object based on id.
	 * 
	 * @param id
	 *            the identifier of the class
	 */
	void removeById(Serializable id);

	/**
	 * Save or update an object.
	 * 
	 * @param o
	 *            the object to save
	 */
	void save(@DynamicType Object o);

	// ###### NEW API Since 2013.7.22 ###############
	/**
	 * Return a String List present results of select items
	 * 
	 * @param fsp
	 * @param selectItems
	 * @return
	 */
	List<?> getStringList(FSP fsp, String selectItems);
}
