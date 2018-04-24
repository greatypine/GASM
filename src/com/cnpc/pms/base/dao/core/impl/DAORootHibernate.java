package com.cnpc.pms.base.dao.core.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.EntityMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.cnpc.pms.base.dao.core.HQLHelper;
import com.cnpc.pms.base.dao.core.IDAORoot;
import com.cnpc.pms.base.dao.core.SQLHelper;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.IJoin;
import com.cnpc.pms.base.paging.IPage;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.util.SpringHelper;

public class DAORootHibernate extends HibernateDaoSupport implements IDAORoot {
	protected Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	public <T> List<T> findByExample(T exampleEntity) {
		return getHibernateTemplate().findByExample(exampleEntity);
	}

	public <T> T get(Class<T> clazz, Serializable id) {
		return clazz.cast(getHibernateTemplate().get(clazz, id));
	}

	public Serializable getId(Object o) {
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Serializable id = null;
		if (o != null && sf != null) {
			Class<?> clazz = SpringHelper.getOriginalClass(o.getClass());
			ClassMetadata cm = sf.getClassMetadata(clazz);
			if (cm != null) {
				id = cm.getIdentifier(o, EntityMode.POJO);
			}
		}
		return id;
	}

	public boolean isNew(Object o) {
		Serializable id = getId(o);
		if (id != null) {
			Class<?> clazz = SpringHelper.getOriginalClass(o.getClass());
			Object obj = this.get(clazz, id);
			if (obj != null) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(Class<T> clazz) {
		return getHibernateTemplate().loadAll(clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(Class<T> clazz, FSP fsp) {
		if (fsp == null) {
			return this.getList(clazz);
		}
		final HQLHelper helper = new HQLHelper(clazz, null, fsp, null);
		List<T> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				return helper.getList(session);
			}
		});
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(Class<T> clazz, IFilter filter, ISort sort, IPage page) {
		final HQLHelper helper = new HQLHelper(clazz, null, filter, sort, page);
		List<T> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				return helper.getList(session);
			}
		});
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getListWithJoin(Class<T> clazz, String alias, FSP fsp, IJoin join) {
		final HQLHelper helper = new HQLHelper(clazz, alias, fsp, join);
		List<T> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				return helper.getList(session);
			}
		});
		return list;
	}

	public <T> T getUnique(Class<T> clazz, IFilter filter) {
		final HQLHelper helper = new HQLHelper(clazz, null, filter, null, null);
		Object result = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				return helper.getUnique(session);
			}
		});
		return clazz.cast(result);
	}

	public List<?> queryProperties(Class<?> clazz, IFilter filter, ISort sort, IPage page, final String selectString) {
		final HQLHelper helper = new HQLHelper(clazz, null, filter, sort, page);
		List<?> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				return helper.getList(session, selectString);
			}
		});
		return list;
	}

	public void refresh(Object o) {
		if (o != null) {
			getHibernateTemplate().refresh(o);
		}
	}

	public void remove(Object o) {
		if (o != null) {
			getHibernateTemplate().delete(o);
		}
	}

	public void removeById(Class<?> clazz, Serializable id) {
		this.remove(this.get(clazz, id));
	}

	public void save(Object o) {
		if (o != null) {
			getHibernateTemplate().saveOrUpdate(o);
		}
	}

	public int bulkOperate(String hql, Object[] params) {
		return getHibernateTemplate().bulkUpdate(hql, params);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, ?>> querySQL(String selectString, List<?> params, IPage page) {
		final SQLHelper helper = new SQLHelper(selectString, params, page);
		List<Map<String, ?>> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				return helper.getListOfMap(session);
			}
		});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<List<?>> querySQLAsList(String selectString, List<?> params, IPage page) {
		final SQLHelper helper = new SQLHelper(selectString, params, page);
		List<List<?>> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				return helper.getListOfList(session);
			}
		});
		return list;
	}

}
