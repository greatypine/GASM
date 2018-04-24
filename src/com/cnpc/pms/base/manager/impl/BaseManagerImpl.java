package com.cnpc.pms.base.manager.impl;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.entity.PMSAuditEntity;
import com.cnpc.pms.base.exception.PMSManagerException;
import com.cnpc.pms.base.exception.ValidationException;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.manager.core.impl.ManagerRootImpl;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.IJoin;
import com.cnpc.pms.base.paging.operator.Condition;
import com.cnpc.pms.base.query.QueryDefinition;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.StrUtil;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base Manager for common objects' CRUD operations. <br/>
 * copyright(c) 2010 IBM, http://www.ibm.com
 * 
 * @author Zhou Zaiqing
 * @since 2010/10/29
 *        see com.cnpc.pms.base.manager.IManager
 */
public class BaseManagerImpl extends ManagerRootImpl implements IManager {

	// ########## Unhandled method ##########

	public Iterator<?> iterateObjectsWithJoin(Class<?> clazz, String alias, IJoin join, FSP fsp) {
		return getDao().iterateObjectsWithJoin(clazz, alias, join, fsp);
	}

	/**
	 * @param entity
	 *            entity to validate
	 * @throws ValidationException
	 *             validation exception
	 */
	public void validateEntity(final Object entity) throws ValidationException {
		Field[] fields = entity.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getAnnotation(javax.persistence.Column.class) != null) {
				long length = field.getAnnotation(javax.persistence.Column.class).length();
				boolean nullable = field.getAnnotation(javax.persistence.Column.class).nullable();
				field.setAccessible(true);
				Object valueObject;
				try {
					valueObject = field.get(entity);
					if (nullable == false && valueObject == null) {
						throw (new ValidationException("Null value is not allowed!"));
					}

					if (valueObject != null) {
						if ((valueObject.toString()).length() > length) {
							throw (new ValidationException(
									"The length of the parameter is greater than the specified length!"));
						}
					}
				} catch (IllegalArgumentException e) {
					throw (new ValidationException("Illegal argument!"));
				} catch (IllegalAccessException e) {
					throw (new ValidationException("Illegal Access!"));
				}
			}
		}
	}

	// ########################################
	// Following are shadowed methods.
	// see IManagerRoot
	public Object createNewObject() throws PMSManagerException {
		return this.create();
	}

	public Object getObject(Serializable id) {
		return this.get(id);
	}

	public Serializable getObjectId(Object o) {
		return this.getId(o);
	}

	// TODO:2011-05-26 change from getDao().getObjects(entityClass, (IFilter) null, null, null);
	public List<?> getObjects() {
		return this.getList();
	}

	public List<?> getObjects(FSP fsp) {
		return this.getList(fsp);
	}

	public List<?> getObjects(IFilter filter) {
		return this.getList(filter);
	}

	public Object getUniqueObject(IFilter filter) {
		return this.getUnique(filter);
	}

	public void refreshObject(Object o) {
		this.refresh(o);
	}

	public void removeObject(Object o) {
		this.remove(o);
	}

	public void removeObjectById(Serializable id) {
		this.removeById(id);
	}

	public void saveObject(Object o) {
		this.save(o);
	}

	// #####################################
	// Supporting inner methods
	public IDAO getDao() {
		return (IDAO) super.getDao();
	}

	protected boolean checkPrivilege(Object o) {
		return this.checkData(o);
	}

	protected void preSave(Object o) {
		this.preSaveObject(o);
	}

	protected void postSave(Object o) {
		this.postSaveObject(o);
	}

	protected void preRemove(Object o) {
		this.preRemoveObject(o);
	}

	@SuppressWarnings("unchecked")
	protected boolean checkData(Object o) {
		// get businessId
		String businessId = getBusinessId(o);

		if (businessId == null)
			return true;

		// get current's dataACL for add;
		Map<String, Set<Condition>> dataACLForAdd = null;
		try {
			dataACLForAdd = (Map<String, Set<Condition>>) SessionManager.getUserSession().getSessionData()
					.get("dataACLForAdd");
		} catch (NullPointerException e) {
			dataACLForAdd = null;
		}

		if (dataACLForAdd == null)
			return true;

		// get conditions by businessId;
		Set<Condition> conditions = dataACLForAdd.get(businessId);
		if (conditions == null)
			return true;

		// data permission;
		return validateData(o, conditions);

	}

	/**
	 * @param o
	 * @return
	 */
	private String getBusinessId(Object o) {
		String businessId = null;

		String filterEntity = PropertiesUtil.getValue("dataPermission.filter.entity");

		// need not filter data;
		if (filterEntity == null || "".equals(filterEntity)) {
			return businessId;
		}

		String[] filterEntitys = filterEntity.split(",");

		String className = o.getClass().getName();

		for (String entity : filterEntitys) {
			if (entity.equals(className)) {
				businessId = ClassUtils.getShortName(className);
				break;
			}
		}

		return businessId;
	}

	protected boolean validateData(Object o, Set<Condition> conditions) {
		for (Condition condition : conditions) {
			if (!condition.isValidate(o)) {
				return false;
			}
		}

		return true;
	}




	@Deprecated
	protected void postSaveObject(Object o) {
	}

	@Deprecated
	protected void preRemoveObject(Object o) {
	}

	public List<?> getObjects(Class<?> entityClass, FSP fsp) {
		return getDao().getObjects(entityClass, fsp);
	}

	public void removeObjectById(Class<?> clazz, Serializable id) {
		getDao().removeObjectById(clazz, id);
	}

	// following methods contains information of PMSQuery
	// It's not a good design.
	public Object getObj(String queryId, Long id) throws ClassNotFoundException {
		Class<?> entityClass = getClassByQuery(queryId);
		this.setEntityClass(entityClass);
		return this.getObject(id);
	}

	public void removeObj(String queryId, Long id) throws ClassNotFoundException {
		Class<?> entityClass = getClassByQuery(queryId);
		getDao().removeObjectById(entityClass, id);
	}

	public void saveObj(String queryId, String objectJsonString) throws Exception {
		Class<?> entityClass = getClassByQuery(queryId);
		Object object = StrUtil.fromJson(objectJsonString, entityClass);
		this.saveObject(object);
	}


	protected Class<?> getClassByQuery(String queryId) throws ClassNotFoundException {
		String className = QueryDefinition.getQueryById(queryId).getBaseClass();
		Class<?> entityClass = Class.forName(className);
		return entityClass;
	}

	// ############## Removed methods #############
	// Deprecated
	// Remove after 2013.12.31
	// public void setEntityClassName(String entityClassName) throws PMSManagerException {
	// try {
	// entityClass = Class.forName(entityClassName);
	// } catch (ClassNotFoundException e) {
	// throw new PMSManagerException("Fail to get Entity class: " + entityClassName, e);
	// }
	// }

	/**
	 * Manager that needs validation should override this method and invoke it before execute business method.If throws
	 * ValidationException, constructor parameter must be included in property file for i18n purpose.
	 */
	// public void validate() {
	// log.debug("dao: {}", dao);
	// log.debug("this.dao: {}", this.dao);
	// log.debug("getDao: {}", getDao());
	// log.debug("this.getDao: {}", this.getDao());
	// }

	// protected void postRemoveObject(Object o) {
	// }

	// public Iterator<?> iterateObjects(FSP fsp) {
	// return this.getIterator(fsp);
	// }
	//
	// public Iterator<?> iterateObjects(IFilter filter) {
	// return this.getIterator(filter);
	// }

	protected void preSaveObject(Object o) {
		if (o instanceof PMSAuditEntity) {
			User sessionUser = null;
			if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
			}
			PMSAuditEntity commonEntity = (PMSAuditEntity) o;
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			// insert处理时添加创建人和创建时间
			if (null == commonEntity.getCreateDate()) {
				commonEntity.setCreateDate(sdate);
				if (null != sessionUser) {
					commonEntity.setCreateUserId(sessionUser.getCode());
				}
			}
			commonEntity.setLastModifyDate(sdate);
			if (null != sessionUser) {
				commonEntity.setLastModifyUserID(sessionUser.getCode());
			}
		}
	}
}
