package com.cnpc.pms.base.dict.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.dict.dao.DictDAO;
import com.cnpc.pms.base.dict.entity.Dict;
import com.cnpc.pms.base.exception.DictImportException;

/**
 * <p>
 * <b>Dict data access object implement.</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2010-12-23
 */
public class DictDAOHibernate extends BaseDAOHibernate implements DictDAO {
	private static final Integer BATCH_SIZE = 100;

	/**
	 * get the dictionary by the table name
	 * 
	 * @param dictName
	 *            table name of the dictionary
	 * @return List<Dict> 2012-10-12 郝成杰修改，加入在页面是否显示标识符
	 */

	@SuppressWarnings("unchecked")
	public Map<String, Dict> loadDict(String dictName) {
		SQLQuery query = this.getSession().createSQLQuery(Dict.getSelectSql(dictName));
		List<Object[]> resultList = query.list();
		if (resultList.isEmpty()) {
			return null;
		}
		Map<String, Dict> dictList = new HashMap<String, Dict>();
		for (Object[] objects : resultList) {
			Dict dict = new Dict(objects);
			dictList.put(dict.getDictCode(), dict);
		}
		return dictList;
	}

	@Override
	public Map<String, Dict> loadNewDict(String dictType) {
		SQLQuery query = this.getSession().createSQLQuery(Dict.getDictByType(dictType));
		List<Object[]> resultList = query.list();
		if(resultList.isEmpty()){
			return null;
		}
		Map<String,Dict> dictList = new HashMap<String, Dict>();
		for (Object[] objects : resultList){
			Dict dict = new Dict(objects);
			dictList.put(dict.getDictCode(),dict);
		}
		return dictList;
	}

	public boolean createTable(String dictName) {
		String sql = Dict.getCreateSql(dictName);
		log.debug("Start to create Dict: [{}]", sql);
		try {
			executeSql(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean dropTable(String dictName) {
		String sql = Dict.getDropSql(dictName);
		log.debug("Start to drop Dict: [{}]", sql);
		try {
			executeSql(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Batch insert data.
	 * 
	 * @param dicts
	 *            the data to batch insert
	 */
	public void batchInsertData(List<Dict> dicts) {
		Transaction tx = this.getSession().beginTransaction();
		try {
			int counter = 1;
			for (Dict dict : dicts) {
				insertDict(dict);
				if (counter % BATCH_SIZE == 0) {
					tx.commit();
					log.info("We have import {} Dictionary Items.", counter);
				}
				if (!tx.isActive()) {
					tx.begin();
				}
				counter++;
			}
			log.info("We have import ALL {} Dictionary Items.", counter);
		} catch (Exception e) {
			log.error("We get error in import dictionary:", e);
			throw new DictImportException(e.getMessage());
		}
	}

	private void insertDict(Dict dict) {
		Object obj = this.getSession().createSQLQuery(dict.getCountSql()).uniqueResult();
		if (Integer.valueOf(obj.toString()) > 0) {
			executeSql(dict.getDeleteSql());
			log.debug("Delete dict[{}] code[{}] with value[{}] for insert",
					new String[] { dict.getDictName(), dict.getDictCode(), dict.getDictText() });
		}
		log.debug("Insert dict[{}] code[{}] with value[{}]", new String[] { dict.getDictName(), dict.getDictCode(),
				dict.getDictText() });
		executeSql(dict.getInsertSql());
	}

	/**
	 * Execute native-sql.
	 * 
	 * @param sql
	 *            the query string
	 */
	private Object executeSql(final String sql) {
		return getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(final Session session) throws SQLException {
				SQLQuery sqlQuery = session.createSQLQuery(sql);
				return sqlQuery.executeUpdate();
			}
		});
	}

}
