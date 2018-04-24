package com.cnpc.pms.base.dao.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.hql.QueryTranslator;
import org.hibernate.hql.ast.QueryTranslatorImpl;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.base.entity.IEntity;
import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.IJoin;
import com.cnpc.pms.base.paging.IPage;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.model.PMSQuery;
import com.cnpc.pms.base.util.StrUtil;

/**
 * <P>
 * <strong>PMS Base DAO</strong>
 * </p>
 * provide the basis CRUD operators to any entity, implement in hibernate way.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn/
 * 
 * @author Zhou Zaiqing
 * @since 2010/10/28 Modified by WangFen 2010.12.27
 */
public class BaseDAOHibernate extends DAORootHibernate implements IDAO {

	/** The log. */
	// protected Logger log = LoggerFactory.getLogger(getClass());

	private String getConditionSql( IFilter filter) {
		StringBuffer sql = new StringBuffer();
		sql.append(" WHERE ").append(filter.toString());
		return sql.toString();
	}
	
	public Integer getCount(Class<?> targetClass, IFilter filter){
		String strsql ="SELECT COUNT(1) FROM " +targetClass.getName()+ getConditionSql(filter);
		Object obj = this.getSession().createSQLQuery(strsql).uniqueResult();
		return new Integer(obj.toString());
	}
	
	public Integer getDistinctCount(Class<?> targetClass, IFilter filter,
			String distinct){
		return getCount(targetClass,filter);
	}
	
	public List getDistinctList(Class<?> targetClass, FSP fsp, String distinct){
		
		return null;
	}
	/**
	 * 
	 */
	public Object findObjects(Object exampleEntity) {
		// Object o = getHibernateTemplate().findByExample(exampleEntity);
		// return o;
		return super.findByExample(exampleEntity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.base.dao.IDAO#getObject(java.lang.Class,
	 * java.io.Serializable)
	 */
	/**
	 * Gets the object.
	 * 
	 * @param clazz
	 *            the clazz
	 * @param id
	 *            the id
	 * @return the object
	 */
	public Object getObject(Class<?> clazz, Serializable id) {
		// Object o = getHibernateTemplate().get(clazz, id);
		// return o;
		return super.get(clazz, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.base.dao.IDAO#getObjectId(java.lang.Object)
	 */
	/**
	 * Gets the object id.
	 * 
	 * @param o
	 *            the o
	 * @return the object id
	 */

	public Serializable getObjectId(Object o) {
		// SessionFactory sf = getHibernateTemplate().getSessionFactory();
		// Serializable id = null;
		// if (o != null && sf != null) {
		// Class<?> clazz = SpringHelper.getOriginalClass(o.getClass());
		// ClassMetadata cm = sf.getClassMetadata(clazz);
		// if (cm != null) {
		// id = cm.getIdentifier(o, EntityMode.POJO);
		// }
		// }
		// return id;
		return super.getId(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.base.dao.IDAO#getObjects(java.lang.Class)
	 */
	/**
	 * Gets the objects.
	 * 
	 * @param clazz
	 *            the clazz
	 * @return the objects
	 */

	@SuppressWarnings("rawtypes")
	public List getObjects(Class<?> clazz) {
		// return getHibernateTemplate().loadAll(clazz);
		return super.getList(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.base.dao.IDAO#getObjects(java.lang.Class,
	 * com.cnpc.pms.base.paging.FSP)
	 */
	/**
	 * Gets the objects.
	 * 
	 * @param clazz
	 *            the clazz
	 * @param fsp
	 *            the fsp
	 * @return the objects
	 */
	@SuppressWarnings("rawtypes")
	public List getObjects(Class<?> clazz, FSP fsp) {
		IFilter filter = null;
		ISort sort = null;
		IPage page = null;
		if (fsp != null) {
			filter = fsp.getFilter();
			sort = fsp.getSort();
			page = fsp.getPage();
		}
		return getObjects(clazz, filter, sort, page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.base.dao.IDAO#getObjects(com.cnpc.pms.base.query.model.PMSQuery,
	 * com.cnpc.pms.base.paging.FSP)
	 */
	/**
	 * Gets the objects.
	 * 
	 * @param pmsQuery
	 *            the pmsQuery
	 * @param fsp
	 *            the fsp
	 * @return the objects
	 */
	@SuppressWarnings("unchecked")
	public List getObjects(final PMSQuery pmsQuery, final FSP fsp) {
		List<IEntity> ret = null;
		ret = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return processFind(pmsQuery, session, fsp);
			}
		});
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.base.dao.IDAO#getObjects(java.lang.Class)
	 */
	/**
	 * Gets the objects.
	 * 
	 * @param clazz
	 *            the clazz
	 * @param filter
	 *            the filter
	 * @param sort
	 *            the sort
	 * @param page
	 *            the page
	 * @return the objects
	 */
	@SuppressWarnings("rawtypes")
	public List getObjects(final Class<?> clazz, final IFilter filter, final ISort sort, final IPage page) {
		HibernateTemplate ht = getHibernateTemplate();
		List<?> ret = null;
		ret = ht.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				return processFind(session, clazz, filter, sort, page);
			}
		});
		return ret;
	}

	/**
	 * (non-Javadoc).
	 * 
	 * @param clazz
	 *            the clazz
	 * @param filter
	 *            the filter
	 * @param selectString
	 *            the select string
	 * @return the statistics info
	 * @see com.cnpc.pms.base.dao.IDAO#getStatisticsInfo(Class, IFilter, String)
	 */
	@SuppressWarnings("rawtypes")
	public List getStatisticsInfo(final Class clazz, final IFilter filter, final String selectString) {
		HibernateTemplate ht = getHibernateTemplate();
		List ret = ht.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				return processStatisticsFind(session, clazz, filter, selectString);
			}
		});
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.base.dao.IDAO#getUniqueObject(java.lang.Class,
	 * com.cnpc.pms.base.paging.IFilter)
	 */
	/**
	 * Gets the unique object.
	 * 
	 * @param clazz
	 *            the clazz
	 * @param filter
	 *            the filter
	 * @return the unique object
	 */

	public Object getUniqueObject(final Class<?> clazz, final IFilter filter) {
		HibernateTemplate ht = getHibernateTemplate();
		Object ret = ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				return processFindUnique(session, clazz, filter);
			}
		});
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.base.dao.IDAO#isNewObject(java.lang.Object)
	 */
	/**
	 * Checks if is new object.
	 * 
	 * @param o
	 *            the o
	 * @return true, if is new object
	 */

	public boolean isNew(Object o) {
		// Serializable id = getObjectId(o);
		// if (id != null) {
		// Class<?> clazz = SpringHelper.getOriginalClass(o.getClass());
		// Object obj = getHibernateTemplate().load(clazz, id);
		// if (obj != null) {
		// return false;
		// }
		// }
		// return true;
		return super.isNew(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.base.dao.IDAO#iterateObjects(java.lang.Class,
	 * com.cnpc.pms.base.paging.FSP)
	 */
	/**
	 * Iterate objects.
	 * 
	 * @param clazz
	 *            the clazz
	 * @param fsp
	 *            the fsp
	 * @return the iterator
	 */
	// XXX not used?
	@SuppressWarnings("rawtypes")
	public Iterator iterateObjects(final Class<?> clazz, final FSP fsp) {
		IFilter filter = null;
		ISort sort = null;
		IPage page = null;
		if (fsp != null) {
			filter = fsp.getFilter();
			sort = fsp.getSort();
			page = fsp.getPage();
		}
		return iterateObjects(clazz, filter, sort, page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.base.dao.IDAO#iterateObjects(java.lang.Class,
	 * com.cnpc.pms.base.paging.IFilter, com.cnpc.pms.base.paging.ISort,
	 * com.cnpc.pms.base.paging.IPage)
	 */
	/**
	 * Iterate objects.
	 * 
	 * @param clazz
	 *            the clazz
	 * @param filter
	 *            the filter
	 * @param sort
	 *            the sort
	 * @param page
	 *            the page
	 * @return the iterator
	 */
	// XXX not used?
	@SuppressWarnings("rawtypes")
	public Iterator iterateObjects(final Class<?> clazz, final IFilter filter, final ISort sort, final IPage page) {
		HibernateTemplate ht = getHibernateTemplate();
		Iterator ret = (Iterator) ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				return processIterateFind(session, clazz, filter, sort, page);
			}
		});
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.base.dao.IDAO#getObjects(java.lang.Class,
	 * java.lang.String, com.cnpc.pms.base.paging.IJoin,
	 * com.cnpc.pms.base.paging.FSP)
	 */
	/**
	 * Gets the objects.
	 * 
	 * @param entityClass
	 *            the entity class
	 * @param alias
	 *            the alias
	 * @param join
	 *            the join
	 * @param fsp
	 *            the fsp
	 * @return the objects
	 */
	// XXX not used?
	@SuppressWarnings("rawtypes")
	public Iterator iterateObjectsWithJoin(final Class entityClass, final String alias, final IJoin join, final FSP fsp) {
		HibernateTemplate ht = getHibernateTemplate();

		return (Iterator) ht.execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws SQLException {
				return processJoinFind(session, entityClass, alias, join, fsp.getFilter(), fsp.getSort(), fsp.getPage());
			}
		});
	}

	/**
	 * Process find.
	 * 
	 * @param session
	 *            the session
	 * @param clazz
	 *            the clazz
	 * @param filter
	 *            the filter
	 * @param sort
	 *            the sort
	 * @param page
	 *            the page
	 * @return the list
	 */
	List<?> processFind(Session session, Class<?> clazz, IFilter filter, ISort sort, IPage page) {
		Query[] queries = setupQuery(session, clazz, null, null, filter, sort, page);
		Query query = queries[0];
		Query countQuery = queries[1];
		if (page != null && countQuery != null) {
			long begin = System.currentTimeMillis();
			Long size = (Long) countQuery.uniqueResult();
			long end = System.currentTimeMillis();
			log.debug("Query Count String:{}\n\tUse: {}ms", countQuery.getQueryString(), (end - begin));
			if (size != null) {
				page.setTotalRecords(size.intValue());
			}
			query.setFirstResult(page.getStartRowPosition());
			query.setMaxResults(page.getRecordsPerPage());
			// System.out.println("lisongtao:query_count--"+size+"--"+ System.currentTimeMillis()+"--thread id:" +
			// Thread.currentThread().getId());
		}
		long begin = System.currentTimeMillis();
		// System.out.println("lisongtao:query: query_time--"+query.getQueryString()+"--"+(System.currentTimeMillis() -
		// begin)+"--"+ System.currentTimeMillis()+"--thread id:" + Thread.currentThread().getId());
		List<?> ret = query.list();
		// System.out.println("lisongtao:query: query_count--"+ret.size()+"--"+
		// System.currentTimeMillis()+"--thread id:" + Thread.currentThread().getId());
		long end = System.currentTimeMillis();
		log.debug("Query String:{}\n\tUse: {}ms", query.getQueryString(), (end - begin));

		return ret;
	}

	/**
	 * 处理配置有distinct的查询
	 * 
	 * @param pmsQuery
	 * @param session
	 * @param fsp
	 * @return
	 */
	private List<?> processFind(PMSQuery pmsQuery, Session session, FSP fsp) {
		IFilter filter = null;
		ISort sort = null;
		IPage page = null;
		if (fsp != null) {
			filter = fsp.getFilter();
			sort = fsp.getSort();
			page = fsp.getPage();
		}
		Query[] queries = setupDistinctQuery(session, pmsQuery, null, null, filter, sort, page);
		Query query = queries[0];
		Query countQuery = queries[1];
		if (page != null && countQuery != null) {
			long begin = System.currentTimeMillis();
			Long size = null;
			Object sizeObject =  countQuery.uniqueResult();
			if(sizeObject instanceof BigDecimal){
				size = ((BigDecimal)sizeObject).longValue();
			}else if(sizeObject instanceof BigInteger){
				size = ((BigInteger)sizeObject).longValue();
			}else{
				size = (Long)sizeObject;
			}
			long end = System.currentTimeMillis();
			log.debug("Query Count String:{}\n\tUse: {}ms", countQuery.getQueryString(), (end - begin));
			if (size != null) {
				page.setTotalRecords(size.intValue());
			}
			query.setFirstResult(page.getStartRowPosition());
			query.setMaxResults(page.getRecordsPerPage());
		}
		long begin = System.currentTimeMillis();
		List<?> ret = query.list();
		long end = System.currentTimeMillis();
		log.debug("Query String:{}\n\tUse: {}ms", query.getQueryString(), (end - begin));
		return ret;
	}

	/**
	 * 获取执行结果集的大小
	 * 
	 * @param pmsQuery
	 *            the pms query
	 * @param session
	 *            the session
	 * @param filter
	 *            the filter
	 * @return the List
	 */
	private List processGetCount(PMSQuery pmsQuery, Session session, IFilter filter) {
		Query countQuery;
		IPage pageInfo = new PageInfo();
		if (StrUtil.isNotBlank(pmsQuery.getDistinct())) {
			Query[] queries = setupDistinctQuery(session, pmsQuery, null, null, filter, null, null);
			countQuery = queries[1];
		} else {
			Query[] queries = setupQuery(session, pmsQuery.getTargetClass(), null, null, filter, null, pageInfo);
			countQuery = queries[1];
		}
		List ret = countQuery.list();
		return ret;
	}

	/**
	 * 构建配置有distinct的Query(CountQuery及Query)
	 * 
	 * @param session
	 * @param pmsQuery
	 * @param alias
	 * @param join
	 * @param filter
	 * @param sort
	 * @param page
	 * @return
	 */
	private Query[] setupDistinctQuery(Session session, PMSQuery pmsQuery, String alias, IJoin join, IFilter filter,
			ISort sort, IPage page) {
		try {
			// distinct语句
			String distinctString = pmsQuery.getDistinct();
			List<String> distinctFields = StrUtil.strToList(distinctString, ",");
			// select distinct new Entity(name, group) from xx的形式
			StringBuffer select = new StringBuffer("SELECT DISTINCT new "
					+ pmsQuery.getTargetClass().getCanonicalName() + "(");
			int iCount = 0;
			// 拼接distinct字段，带默认别名
			for (String field : distinctFields) {
				select.append(IFilter.DEFAULT_ALIAS + "." + field);
				if (iCount < distinctFields.size() - 1) {
					select.append(",");
				}
				iCount++;
			}
			select.append(")");

			Class<?> clazz = pmsQuery.getTargetClass();
			Query countQuery = null, query = null;
			String fromAndWhere = setupFromAndWhere(clazz, alias, join, filter);
			// if (page != null) {
			// 取得from、where子句，处理成select count(*) from (select distinct xx from XYZ)的形式
			// 由于HQL不支持select count(distinct x, y) from xx;
			// 并且HQL不支持select count(*) from (select distinct x, y from xx);
			// 先将HQL转换成SQL，再拼接成select count语句.
			StringBuffer countSb = new StringBuffer();
			// 形成select distinct x, y from xx where a=?
			countSb.append(select).append(fromAndWhere);
			// SQL翻译器
			QueryTranslator queryTranslator = new QueryTranslatorImpl(countSb.toString(), countSb.toString(),
					Collections.EMPTY_MAP, (SessionFactoryImplementor) this.getSessionFactory());
			queryTranslator.compile(Collections.EMPTY_MAP, false);
			String sqlFromAndWhere = queryTranslator.getSQLString();
			// HQL目前不支持此种写法
			StringBuffer sqlCount = new StringBuffer("SELECT COUNT(*) FROM (");
			sqlCount.append(sqlFromAndWhere);
			sqlCount.append(") _table_");
			// 创建SQLQuery查询总条数
			countQuery = session.createSQLQuery(sqlCount.toString());
			// }
			StringBuffer orderSb = new StringBuffer();
			String sortString = null;
			if (sort != null) {
				sortString = sort.getString();
			}
			if (StrUtil.isNotBlank(sortString)) {
				orderSb.append(" ORDER BY ").append(sortString);
			}
			// 以select distinct开头的查询语句
			StringBuffer queryString = new StringBuffer(select);
			queryString.append(fromAndWhere).append(orderSb);
			query = session.createQuery(queryString.toString());
			List<?> filterValues = null;
			if (filter != null) {
				filterValues = filter.getValues();
			}
			if (filterValues != null && !filterValues.isEmpty()) {
				int index = 0;
				for (Object val : filterValues) {
					// if (page != null) {
					countQuery = setParameter(countQuery, index, val);
					// }
					query = setParameter(query, index, val);
					index++;
				}
			}
			Query[] ret = { query, countQuery };
			return ret;
		} catch (InvalidFilterException e) {
			throw new HibernateException(e);
		}
	}

	/**
	 * Process find unique.
	 * 
	 * @param session
	 *            the session
	 * @param clazz
	 *            the clazz
	 * @param filter
	 *            the filter
	 * @return the object
	 */
	Object processFindUnique(Session session, Class<?> clazz, IFilter filter) {
		// Query[] queries = setupQuery(session, clazz, null, null, filter, null, null);
		Query query = setupUniqueQuery(session, clazz, filter);// queries[0];
		long begin = System.currentTimeMillis();
		Object ret = query.uniqueResult();
		long end = System.currentTimeMillis();
		log.debug("Query Unique String:{}\n\tUse: {}ms", query.getQueryString(), (end - begin));
		return ret;
	}

	/**
	 * Process iterate find.
	 * 
	 * @param session
	 *            the session
	 * @param clazz
	 *            the clazz
	 * @param filter
	 *            the filter
	 * @param sort
	 *            the sort
	 * @param page
	 *            the page
	 * @return the iterator
	 */
	Iterator<?> processIterateFind(Session session, Class<?> clazz, IFilter filter, ISort sort, IPage page) {
		return this.processFind(session, clazz, filter, sort, page).iterator();
	}

	/**
	 * Process join find.
	 * 
	 * @param session
	 *            the session
	 * @param clazz
	 *            the clazz
	 * @param alias
	 *            the alias
	 * @param join
	 *            the join
	 * @param filter
	 *            the filter
	 * @param sort
	 *            the sort
	 * @param page
	 *            the page
	 * @return the iterator
	 */
	Iterator processJoinFind(Session session, Class clazz, String alias, IJoin join, IFilter filter, ISort sort,
			IPage page) {
		Query[] queries = setupQuery(session, clazz, alias, join, filter, sort, page);
		if (queries == null || queries.length <= 0) {
			return null;
		}
		Query query = queries[0];
		Query countQuery = queries[1];
		if (page != null && countQuery != null) {
			long begin = System.currentTimeMillis();
			Long size = (Long) countQuery.uniqueResult();
			long end = System.currentTimeMillis();
			log.debug("Query count string: " + countQuery.getQueryString() + "\n\tUse: " + (end - begin) + "ms");
			if (size != null) {
				page.setTotalRecords(size.intValue());
			}
			query.setFirstResult(page.getStartRowPosition());
			query.setMaxResults(page.getRecordsPerPage());
		}
		long begin = System.currentTimeMillis();
		Iterator ret = query.iterate();
		long end = System.currentTimeMillis();
		log.debug("Query string: " + query.getQueryString() + "\n\tUse: " + (end - begin) + "ms");
		return ret;
	}

	/**
	 * Process statistics find.
	 * 
	 * @param session
	 *            the session
	 * @param clazz
	 *            the clazz
	 * @param filter
	 *            the filter
	 * @param selectString
	 *            the select string
	 * @return the list
	 */
	@SuppressWarnings("rawtypes")
	List processStatisticsFind(Session session, Class<?> clazz, IFilter filter, String selectString) {
		Query statisticsQuery = setupStatisticsQuery(session, clazz, null, null, filter, selectString);
		List ret = null;
		if (statisticsQuery != null) {
			ret = statisticsQuery.list();
		}
		return ret;
	}

	public void refreshObject(Object o) {
		// if (o != null) {
		// getHibernateTemplate().refresh(o);
		// }
		super.refresh(o);
	}

	public void removeObject(Object o) {
		// if (o != null) {
		// getHibernateTemplate().delete(o);
		// }
		super.remove(o);
	}

	public void removeObjectById(Class<?> clazz, Serializable id) {
		// removeObject(getObject(clazz, id));
		super.removeById(clazz, id);
	}

	public void saveObject(Object o) {
		// if (o != null) {
		// getHibernateTemplate().saveOrUpdate(o);
		// }
		super.save(o);
	}

	/**
	 * Sets the parameter.
	 * 
	 * @param query
	 *            the query
	 * @param index
	 *            the index
	 * @param value
	 *            the value
	 * @return the query
	 */
	// 是否需要此方法？
	protected Query setParameter(Query query, int index, Object value) {
		Query ret;
		if (value instanceof Boolean) {
			ret = query.setBoolean(index, ((Boolean) value).booleanValue());
		} else if (value instanceof Byte) {
			ret = query.setByte(index, ((Byte) value).byteValue());
		} else if (value instanceof Character) {
			ret = query.setCharacter(index, ((Character) value).charValue());
		} else if (value instanceof Double) {
			ret = query.setDouble(index, ((Double) value).doubleValue());
		} else if (value instanceof Float) {
			ret = query.setFloat(index, ((Float) value).floatValue());
		} else if (value instanceof Integer) {
			ret = query.setInteger(index, ((Integer) value).intValue());
		} else if (value instanceof Long) {
			ret = query.setLong(index, ((Long) value).longValue());
		} else if (value instanceof Short) {
			ret = query.setShort(index, ((Short) value).shortValue());
		} else if (value instanceof String) {
			ret = query.setString(index, (String) value);
		} else if (value instanceof byte[]) {
			ret = query.setBinary(index, (byte[]) value);
		} else if (value instanceof BigDecimal) {
			ret = query.setBigDecimal(index, (BigDecimal) value);
		} else if (value instanceof BigInteger) {
			ret = query.setBigInteger(index, (BigInteger) value);
		} else if (value instanceof Date) {
			ret = query.setDate(index, (Date) value);
		} else if (value instanceof Time) {
			ret = query.setTime(index, (Time) value);
		} else if (value instanceof Timestamp) {
			ret = query.setTimestamp(index, (Timestamp) value);
		} else if (value instanceof java.util.Date) {
			// ret = query.setDate(index, (java.util.Date) value);
			ret = query.setTimestamp(index, (java.util.Date) value);
		} else if (value instanceof Locale) {
			ret = query.setLocale(index, (Locale) value);
		} else {
			ret = query.setParameter(index, value);
		}
		log.debug("Set [{}] Type:{}, Value:{}", new Object[] { index, value.getClass(), value });
		return ret;
	}

	/**
	 * Setup query.
	 * 
	 * @param session
	 *            the session
	 * @param clazz
	 *            the clazz
	 * @param alias
	 *            the alias
	 * @param join
	 *            the join
	 * @param filter
	 *            the filter
	 * @param sort
	 *            the sort
	 * @param page
	 *            the page
	 * @return the query[]
	 */
	// XXX 优化
	private Query[] setupQuery(Session session, Class<?> clazz, String alias, IJoin join, IFilter filter, ISort sort,
			IPage page) {
		try {
			Query countQuery = null, query = null;
			String fromAndWhere = setupFromAndWhere(clazz, alias, join, filter);
			StringBuffer countSb = new StringBuffer();
			countSb.append("SELECT COUNT(*) ");
			if (page != null) {
				countQuery = session.createQuery(countSb.append(fromAndWhere).toString());
			}
			StringBuffer orderSb = new StringBuffer();
			String sortString = null;
			if (sort != null) {
				sortString = sort.getString();
			}
			if (StrUtil.isNotBlank(sortString)) {
				orderSb.append(" ORDER BY ").append(sortString);
			}
			query = session.createQuery(fromAndWhere + orderSb.toString());
			List<?> filterValues = null;
			if (filter != null) {
				filterValues = filter.getValues();
			}
			if (filterValues != null && !filterValues.isEmpty()) {
				int index = 0;
				for (Object val : filterValues) {
					if (page != null) {
						countQuery = setParameter(countQuery, index, val);
					}
					query = setParameter(query, index, val);
					index++;
				}
			}
			Query[] ret = { query, countQuery };
			return ret;
		} catch (InvalidFilterException e) {
			throw new HibernateException(e);
		}
	}

	/**
	 * 
	 * @param session
	 * @param clazz
	 * @param filter
	 * @return
	 */
	private Query setupUniqueQuery(Session session, Class<?> clazz, IFilter filter) {
		try {
			Query query = null;
			String fromAndWhere = setupFromAndWhere(clazz, null, null, filter);
			query = session.createQuery(fromAndWhere);
			List<?> filterValues = null;
			if (filter != null) {
				filterValues = filter.getValues();
			}
			if (filterValues != null && !filterValues.isEmpty()) {
				int index = 0;
				for (Object val : filterValues) {
					query = setParameter(query, index, val);
					index++;
				}
			}
			return query;
		} catch (InvalidFilterException e) {
			throw new HibernateException(e);
		}
	}

	/**
	 * Setup statistics query.
	 * 
	 * @param session
	 *            the session
	 * @param clazz
	 *            the clazz
	 * @param alias
	 *            the alias
	 * @param join
	 *            the join
	 * @param filter
	 *            the filter
	 * @param selectString
	 *            the select string
	 * @return the query
	 */
	private Query setupStatisticsQuery(Session session, Class<?> clazz, String alias, IJoin join, IFilter filter,
			String selectString) {
		try {
			String fromAndWhere = setupFromAndWhere(clazz, alias, join, filter);
			StringBuffer statisticsSb = new StringBuffer();
			statisticsSb.append("SELECT ").append(selectString);
			String hql = statisticsSb.append(fromAndWhere).toString();
			log.debug("Generat HQL: {}", hql);
			Query statisticsQuery = session.createQuery(hql);

			List<?> filterValues = null;
			if (filter != null) {
				filterValues = filter.getValues();
			}
			if (filterValues != null && !filterValues.isEmpty()) {
				int index = 0;
				for (Object val : filterValues) {
					statisticsQuery = setParameter(statisticsQuery, index, val);
					index++;
				}
			}
			return statisticsQuery;
		} catch (InvalidFilterException e) {
			throw new HibernateException(e);
		}
	}

	public String setupFromAndWhere(Class<?> clazz, String alias, IJoin join, IFilter filter)
			throws InvalidFilterException {
		String aliasName = alias;
		if (aliasName == null || aliasName.trim().length() == 0) {
			aliasName = IFilter.DEFAULT_ALIAS;
		}
		String filterString = "1=1";// default condition
		if (filter != null) {
			filter.setTableAlias(aliasName);
			filterString = filter.getString();
		}

		StringBuffer fromString = new StringBuffer();
		StringBuffer whereString = new StringBuffer();

		fromString.append(" FROM ").append(clazz.getName()).append(' ').append(aliasName);
		whereString.append(" WHERE ").append(filterString);
		if (join != null) {// NOTE: unchecked construct join-clause
			fromString.append(join.getJoinedClasses());
			whereString.append(" AND (").append(join.getConditions()).append(")");
			// fromString.append(' ').append(join.getString());
		}
		String hql = fromString.append(whereString).toString();
		return hql;
	}

	/**
	 * Batch-update data
	 * 
	 * @param updateHQL
	 *            HQL string for updating statement
	 * @param params
	 *            parameters
	 */
	public int bulUpdate(String updateHQL, Object[] params) {
		// return this.getHibernateTemplate().bulkUpdate(updateHQL, params);
		return super.bulkOperate(updateHQL, params);
	}

	/**
	 * Batch-delete data
	 * 
	 * @param deleteHQL
	 *            HQL string for deleting statement
	 * @param params
	 *            parameters
	 * @throws Exception
	 *             Exception
	 */
	public int bulDelete(final String deleteHQL, final Object[] params) {
		// HibernateTemplate hTemplate = this.getHibernateTemplate();
		// Object items = hTemplate.execute(new HibernateCallback() {
		// public Object doInHibernate(Session session) {
		// Query queryObject = session.createQuery(deleteHQL);
		// if (params != null) {
		// for (int i = 0; i < params.length; i++) {
		// queryObject.setParameter(i, params[i]);
		// }
		// }
		// return Integer.valueOf(queryObject.executeUpdate());
		// }
		// });
		// return Integer.valueOf(items.toString());
		return super.bulkOperate(deleteHQL, params);
	}

	/**
	 * Gets the objects count.
	 * 
	 * @param pmsQuery
	 *            the pms query
	 * @param filter
	 *            the filter
	 * @return the objects count
	 */
	public Long getObjectsCount(final PMSQuery pmsQuery, final IFilter filter) {
		List ret = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return processGetCount(pmsQuery, session, filter);
			}
		});

		if (ret.size() > 0) {
			Object objvalue = ret.get(0);
			if (objvalue instanceof Long) {
				return (Long) objvalue;
			} else {
				BigDecimal result = (BigDecimal) objvalue;
				return result.longValue();
			}
		} else {
			return 0l;
		}
	}

	// ####################### new API since 2013.7.22 ######

	// public List<String> getResults(final Class<?> clazz, final FSP fsp, final String selectItem) {
	// HibernateTemplate ht = getHibernateTemplate();
	// List ret = ht.executeFind(new HibernateCallback() {
	// public Object doInHibernate(Session session) throws SQLException {
	// return processSelect(session, clazz, fsp, selectItem);
	// }
	// });
	// return ret;
	// }
	//
	// private List processSelect(Session session, Class<?> clazz, FSP fsp, String selectString) {
	// String fromAndWhere = setupFromAndWhere(clazz, null, null, filter);
	// query = session.createQuery(fromAndWhere + orderSb.toString());
	// return ret;
	// }

	// /**
	// * Save audit object.
	// *
	// * @param businessObject
	// * the initial business object
	// * @param auditObject
	// * the audit object to record the history info
	// */
	// public void saveAuditObject(Object businessObject, Object auditObject) {
	// BeanUtils.copyProperties(businessObject, auditObject);
	// this.saveObject(businessObject);
	// this.saveObject(auditObject);
	// }
}
