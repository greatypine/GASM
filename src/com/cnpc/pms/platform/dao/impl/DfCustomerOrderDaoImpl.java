package com.cnpc.pms.platform.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.personal.entity.TinyVillageCode;
import com.cnpc.pms.personal.manager.TinyVillageCodeManager;
import com.cnpc.pms.platform.dao.DfCustomerOrderDao;

public class DfCustomerOrderDaoImpl extends DAORootHibernate implements DfCustomerOrderDao {

	@Override
	public Integer findCustomerCount() {
		String sql_count = "SELECT ifnull(count(1),0) from (select customer_id from df_customer_order_month_trade group by customer_id) t";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery countQuery = session.createSQLQuery(sql_count);
			return Integer.valueOf(countQuery.list().get(0).toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public Integer findCustomerCountByStore(String storeCode) {
		String sql_count = "select ifnull(count(*),0) from (select customer_id from df_customer_order_month_trade dfc INNER JOIN t_store ts ON dfc.store_id = ts.id and ts.`code` in ("
				+ storeCode + ") GROUP BY dfc.customer_id) t";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery countQuery = session.createSQLQuery(sql_count);
			return Integer.valueOf(countQuery.list().get(0).toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return 0;
	}

	@Override
	public List<Map<String, Object>> findCustomerLatLng(String storeNo) {
		String str_sql = "select COUNT(t.customer_id) as count,t.latitude as lat,t.longitude as lng from (select customer.customer_id,customer.latitude,customer.longitude from df_customer_order_month_trade customer INNER JOIN t_store ts ON ts.id = customer.store_id where ts.`code` IN ("
				+ storeNo
				+ ") and customer.latitude is not null and customer.longitude is not null  GROUP BY  customer.customer_id,customer.store_id) t GROUP BY t.latitude,t.longitude";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		List<Map<String, Object>> lst_result = new ArrayList<Map<String, Object>>();
		try {
			SQLQuery query = session.createSQLQuery(str_sql);
			List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			lst_result = lst_data;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return lst_result;
	}

	@Override
	public List<Map<String, Object>> findCustomerLatLngCount(String storeNo) {
		String str_sql = "select MAX(t1.count) as maxCount,MIN(t1.count) as minCount from (select COUNT(t.customer_id) as count,t.latitude as lat,t.longitude as lng from (select customer.customer_id,customer.latitude,customer.longitude from df_customer_order_month_trade customer INNER JOIN t_store ts ON ts.id = customer.store_id where ts.`code` IN ("
				+ storeNo
				+ ") and customer.latitude is not null and customer.longitude is not null GROUP BY  customer.customer_id,customer.store_id) t GROUP BY t.latitude,t.longitude)t1";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		List<Map<String, Object>> lst_result = new ArrayList<Map<String, Object>>();
		try {
			SQLQuery query = session.createSQLQuery(str_sql);
			List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			lst_result = lst_data;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return lst_result;
	}

	@Override
	public Integer findCustomerByTinyVillageCode(String codes) {
		String sql_count="select count(DISTINCT customer_id) from df_customer_order_month_trade where tiny_village_code in ("+codes+")";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		 try{
	            SQLQuery countQuery = session.createSQLQuery(sql_count);
	            return  Integer.valueOf(countQuery.list().get(0).toString());
	     }catch (Exception e){
	            e.printStackTrace();
	     } finally {
	            session.close();
	     }
		return 0;
	}


	@Override
	public void syncTinyVillageCustomer() {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		TinyVillageCodeManager tinyVillageCodeManager = (TinyVillageCodeManager) SpringHelper
				.getBean("tinyVillageCodeManager");
		try {
			// 如果大于2018-02-06
			// 10:00:00说明是第一次执行执行全部数据
			String str_sql = "SELECT tiny_village_code,COUNT(DISTINCT customer_id) as custom_totle FROM df_customer_order_month_trade WHERE tiny_village_code is not NULL AND order_ym<201801 GROUP BY tiny_village_code";
			SQLQuery query = session.createSQLQuery(str_sql);
			List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			if (lst_data != null && lst_data.size() > 0) {
				for (Map<String, Object> map : lst_data) {
					String tiny_village_code = map.get("tiny_village_code") + "";
					String customer_id = map.get("custom_totle") + "";
					// 根据小区code查询小区
					TinyVillageCode tinyVillageCode = tinyVillageCodeManager.findTinyVillageByCode(tiny_village_code);
					if (tinyVillageCode != null) {
						tinyVillageCode.setConsumer_number(customer_id);
						tinyVillageCodeManager.saveObject(tinyVillageCode);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public Integer findCustomerNumberBytinyvillageCode(String tinycode) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		String str_sql = "SELECT COUNT(DISTINCT customer_id) as custom_totle FROM df_customer_order_month_trade WHERE tiny_village_code='"
				+ tinycode + "'";
		SQLQuery query = session.createSQLQuery(str_sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (lst_data != null && lst_data.size() > 0) {
			Map<String, Object> map = lst_data.get(0);
			if (map != null) {
				return Integer.parseInt(map.get("custom_totle") + "");
			}
		}
		return null;
	}

}
