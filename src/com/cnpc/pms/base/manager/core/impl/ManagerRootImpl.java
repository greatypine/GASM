package com.cnpc.pms.base.manager.core.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnpc.pms.base.dao.core.IDAORoot;
import com.cnpc.pms.base.exception.DataPermissionException;
import com.cnpc.pms.base.exception.PMSManagerException;
import com.cnpc.pms.base.manager.core.IManagerRoot;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.IFilter;

/**
 * New API for IManager.
 * 
 * TODO:
 * 1. check the privileges mechanisam, getList, getIterator methods.
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Jul 22, 2013
 */
public abstract class ManagerRootImpl implements IManagerRoot {

	/** The base logger. */
	protected Logger log = LoggerFactory.getLogger(getClass());
	/** The dao. */
	protected IDAORoot dao = null;

	/** The entity class. */
	protected Class<?> entityClass = null;

	// Sub classes can overwrite these methods. User can add customized logic here.
	/**
	 * Check the privilege of current user with the given object.
	 * Always return true in this base implements.
	 * 
	 * @param o
	 * @return
	 */
	protected boolean checkPrivilege(Object o) {
		return true;
	}

	protected void preSave(Object o) {
	}

	protected void postSave(Object o) {
	}

	protected void preRemove(Object o) {
	}

	protected void postRemove(Object o) {
	}

	public IDAORoot getDao() {
		return dao;
	}

	public final void setDao(IDAORoot dao) {
		this.dao = dao;
	}

	public final Class<?> getEntityClass() {
		return entityClass;
	}

	public final void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public final Object newTx(String methodName, Object... args) throws PMSManagerException {
		try {
			Method method = null;
			Method[] methods = this.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equalsIgnoreCase(methodName)) {
					method = methods[i];
					break;
				}
			}
			if (method == null) {
				throw new PMSManagerException("Fail to get method: " + methodName);
			}
			return method.invoke(this, args);
		} catch (InvocationTargetException e) {
			throw new PMSManagerException("Invoked method " + methodName + " error", e.getCause());
		} catch (Exception e) {
			log.error("Fail to invoke method[{}],reason:{}", methodName, e.toString());
			throw new PMSManagerException("Invoked method " + methodName + " error", e);
		}
	}

	public final boolean isNew(Object o) {
		return getDao().isNew(o);
	}

	public final Object create() throws PMSManagerException {
		if (entityClass != null) {
			try {
				return entityClass.newInstance();
			} catch (IllegalAccessException e) {
				throw new PMSManagerException("Fail to create Entity instance: " + entityClass, e);
			} catch (InstantiationException e) {
				throw new PMSManagerException("Fail to create Entity instance: " + entityClass, e);
			}
		} else {
			throw new PMSManagerException("Unsetted Entity Class. Fail to create instance.");
		}
	}

	// privilege
	public final Object get(Serializable id) {
		Serializable longId;
		if (id.toString().matches("[0-9]*")) {
			longId = Long.valueOf(id.toString());
		} else {
			longId = id;
			log.warn("Invoke getObject of {} get non-number id: {}", this.getClass().getSimpleName(), id);
		}

		Object o = getDao().get(entityClass, longId);
		if (o != null && !checkPrivilege(o)) {
			throw new DataPermissionException("${base.save.noDataPermission}");
		}
		return o;
	}

	public Serializable getId(Object o) {
		return getDao().getId(o);
	}

	// privilege?
	public final List<?> getList() {
		return getDao().getList(entityClass);
	}

	public final List<?> getList(FSP fsp) {
		return getDao().getList(entityClass, fsp);
	}

	public final List<?> getList(IFilter filter) {
		return getDao().getList(entityClass, filter, null, null);
	}

	public final Object getUnique(IFilter filter) {
		Object o = getDao().getUnique(entityClass, filter);
		return o;
	}

	public final void refresh(Object o) {
		getDao().refresh(o);
	}

	public final void remove(Object o) {
		if (o != null && !checkPrivilege(o)) {
			throw new DataPermissionException("${base.save.noDataPermission}");
		}
		preRemove(o);
		getDao().remove(o);
		postRemove(o);
	}

	public final void removeById(Serializable id) {
		Object object = this.get(id);
		this.remove(object);
	}

	public void save(Object o) {
		if (o != null && !checkPrivilege(o)) {
			throw new DataPermissionException("${base.save.noDataPermission}");
		}
		preSave(o);
		getDao().save(o);
		postSave(o);
	}

	public List<?> getStringList(FSP fsp, String selectItems) {
		return getDao().queryProperties(entityClass, fsp.getFilter(), fsp.getSort(), fsp.getPage(), selectItems);
	}
}
