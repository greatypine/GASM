/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.cnpc.pms.base.dao.core;

import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.IJoin;
import com.cnpc.pms.base.paging.IPage;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.util.StrUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HQLHelper {
	protected Logger log = LoggerFactory.getLogger(super.getClass());
	protected Class<?> clazz;
	protected IFilter filter;
	protected ISort sort;
	protected IPage page;
	protected boolean needCount = false;
	protected String alias = "_default_";
	protected IJoin join;
	protected String fromAndWhere;
	protected Query query;
	protected Set<String> aliasName = new HashSet();

	protected HQLHelper() {
	}

	public HQLHelper(Class<?> clazz, String alias, FSP fsp, IJoin join) {
		if (fsp != null) {
			this.filter = fsp.getFilter();
			this.sort = fsp.getSort();
			this.page = fsp.getPage();
		}
		this.join = join;
		for (IJoin j = join; j != null; j = j.getNext()) {
			this.aliasName.add(j.getAlias());
		}
		init(clazz, alias);
	}

	public HQLHelper(Class<?> clazz, String alias, IFilter filter, ISort sort, IPage page) {
		this.filter = filter;
		this.sort = sort;
		this.page = page;
		init(clazz, alias);
	}

	public List<?> getList(Session session) {
		prepareQuery(session);
		return this.query.list();
	}

	public List<?> getList(Session session, String selectItems) {
		prepareQuery(session, selectItems);
		return this.query.list();
	}

	public List<?> getList(Session session, ResultTransformer transformer) {
		prepareQuery(session);
		this.query.setResultTransformer(transformer);
		return this.query.list();
	}

	public Object getUnique(Session session) {
		prepareQuery(session);
		return this.clazz.cast(this.query.uniqueResult());
	}

	protected void init(Class<?> clazz, String alias) {
		this.clazz = clazz;
		if (StrUtil.isNotBlank(alias)) {
			this.alias = alias;
		}
		if (this.page != null) {
			this.needCount = true;
		}
		initClauses();
	}

	protected void initClauses() {
		String filterString = "1=1";
		if (this.filter != null) {
			filterString = this.filter.getString();
		}

		StringBuffer from = new StringBuffer();
		StringBuffer where = new StringBuffer();

		from.append(" FROM ").append(this.clazz.getName()).append(' ').append(this.alias);
		where.append(" WHERE ").append(filterString);
		if (this.join != null) {
			from.append(this.join.getJoinedClasses());
			where.append(" AND (").append(this.join.getConditions()).append(")");
		}
		this.fromAndWhere = from.append(where).toString();
		this.log.debug("from clause: {}\nwhere clause: {}", from, where);
	}

	private void prepareQuery(Session session) {
		prepareQuery(session, null);
	}

	private void prepareQuery(Session session, String selectString) {
		String sortClause = "";
		if (this.sort != null) {
			sortClause = this.sort.getString();
			if (StrUtil.isNotBlank(sortClause)) {
				sortClause = " ORDER BY " + sortClause;
			}
		}
		String selectHQL = getSelectClause(selectString) + this.fromAndWhere + sortClause;
		this.query = session.createQuery(selectHQL);
		if (this.needCount == true) {
			Integer size = getCount(session);
			if (size != null) {
				this.page.setTotalRecords(size.intValue());
			}
			this.query.setFirstResult(this.page.getStartRowPosition());
			this.query.setMaxResults(this.page.getRecordsPerPage());
		}
		setParameters(this.query);
	}

	public Integer getCount(Session session) {
		Query count = session.createQuery("SELECT COUNT(*) " + this.fromAndWhere);
		setParameters(count);
		Number size = (Number) count.uniqueResult();
		return Integer.valueOf(size.intValue());
	}

	protected String getSelectClause(String selectString) {
		if (StrUtil.isNotBlank(selectString)) {
			StringBuffer selectItems = new StringBuffer();
			selectItems.append("SELECT ");
			if (selectString.indexOf("$") > -1) {
				selectItems.append(selectString.replaceAll("\\$", this.alias + "."));
			} else {
				String[] items = selectString.split(",");
				for (int i = 0; i < items.length; ++i) {
					selectItems.append(this.alias).append(".").append(items[i].trim());
					if (i < items.length - 1) {
						selectItems.append(",");
					}
				}
			}
			return selectItems.toString();
		}
		return "";
	}

	protected void setParameters(Query query) {
		if (this.filter != null) {
			List values = this.filter.getValues();
			for (int i = 0; i < values.size(); ++i) {
				Object o = values.get(i);
				if (this.clazz.getPackage().getName().startsWith("com.cnpc.pms.bizbase"))
					setParameter(query, i, o);
				else
					query.setParameter(i, o);
			}
		}
	}

	protected void setParameter(Query query, int index, Object value) {
		if (value instanceof Boolean)
			query.setBoolean(index, ((Boolean) value).booleanValue());
		else if (value instanceof Byte)
			query.setByte(index, ((Byte) value).byteValue());
		else if (value instanceof Character)
			query.setCharacter(index, ((Character) value).charValue());
		else if (value instanceof Double)
			query.setDouble(index, ((Double) value).doubleValue());
		else if (value instanceof Float)
			query.setFloat(index, ((Float) value).floatValue());
		else if (value instanceof Integer)
			query.setInteger(index, ((Integer) value).intValue());
		else if (value instanceof Long)
			query.setLong(index, ((Long) value).longValue());
		else if (value instanceof Short)
			query.setShort(index, ((Short) value).shortValue());
		else if (value instanceof String)
			query.setString(index, (String) value);
		else if (value instanceof byte[])
			query.setBinary(index, (byte[]) (byte[]) value);
		else if (value instanceof BigDecimal)
			query.setBigDecimal(index, (BigDecimal) value);
		else if (value instanceof BigInteger)
			query.setBigInteger(index, (BigInteger) value);
		else if (value instanceof java.sql.Date)
			query.setDate(index, (java.sql.Date) value);
		else if (value instanceof Time)
			query.setTime(index, (Time) value);
		else if (value instanceof Timestamp)
			query.setTimestamp(index, (Timestamp) value);
		else if (value instanceof java.util.Date) {
			query.setTimestamp(index, (java.util.Date) value);
		} else if (value instanceof Locale)
			query.setLocale(index, (Locale) value);
		else {
			query.setParameter(index, value);
		}
		this.log.debug("Set [{}] Type:{}, Value:{}",
				new Object[] { Integer.valueOf(index), value.getClass(), value });
	}
}