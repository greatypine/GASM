package com.cnpc.pms.personal.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.personal.dao.DfMassOrderTotalDao;
import com.cnpc.pms.personal.entity.TinyVillageCode;
import com.cnpc.pms.personal.manager.TinyVillageCodeManager;

public class DfMassOrderTotalDaoImpl extends BaseDAOHibernate implements DfMassOrderTotalDao {

	@Override
	public void findTinyVillageCustomer() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date1 = "2018-01-05 11:50:00";
		// 获取系统当前时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String date2 = format.format(date);
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		String sql = "";
		// 当大于当前时间时调用全部数据
		TinyVillageCodeManager tinyVillageCodeManager = (TinyVillageCodeManager) SpringHelper
				.getBean("tinyVillageCodeManager");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				sql = "SELECT info_village_code,COUNT(DISTINCT customer_id) as store_customer_coun FROM df_mass_order_total WHERE info_village_code is not NULL  GROUP BY info_village_code";
				Query query = session.createSQLQuery(sql);
				List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
						.list();
				if (lst_data != null && lst_data.size() > 0) {
					for (Map<String, Object> map : lst_data) {
						String tiny_village_code = map.get("info_village_code") + "";
						String customer_count = map.get("store_customer_coun") + "";
						// 根据小区code查询小区
						TinyVillageCode tinyVillageCode = tinyVillageCodeManager
								.findTinyVillageByCode(tiny_village_code);
						if (tinyVillageCode != null) {
							tinyVillageCode.setConsumer_number_after(customer_count);
							tinyVillageCodeManager.saveObject(tinyVillageCode);
						}
					}
				}
			} else {
				sql = "SELECT info_village_code,COUNT(DISTINCT customer_id) as store_customer_coun FROM df_mass_order_total WHERE info_village_code is not NULL and sign_time >=DATE_ADD('"
						+ date2 + "', INTERVAL -60 MINUTE) " + " AND sign_time <='" + date2
						+ "' GROUP BY info_village_code";
				Query query = session.createSQLQuery(sql);
				List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
						.list();
				if (lst_data != null && lst_data.size() > 0) {
					for (Map<String, Object> map : lst_data) {
						String tiny_village_code = map.get("info_village_code") + "";
						Integer findCustomerNumberBytinyvillageCode = findCustomerNumberBytinyvillageCode(
								tiny_village_code);
						if (findCustomerNumberBytinyvillageCode != null) {
							// 根据小区code查询小区
							TinyVillageCode tinyVillageCode = tinyVillageCodeManager
									.findTinyVillageByCode(tiny_village_code);
							if (tinyVillageCode != null) {
								tinyVillageCode.setConsumer_number_after(findCustomerNumberBytinyvillageCode + "");
								tinyVillageCodeManager.saveObject(tinyVillageCode);
							}
						}
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

	public Integer findCustomerNumberBytinyvillageCode(String tinycode) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			String str_sql = "SELECT COUNT(DISTINCT customer_id) as store_customer_coun FROM df_mass_order_total WHERE info_village_code='"
					+ tinycode + "' ";
			SQLQuery query = session.createSQLQuery(str_sql);
			List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			if (lst_data != null && lst_data.size() > 0) {
				Map<String, Object> map = lst_data.get(0);
				if (map != null) {
					return Integer.parseInt(map.get("store_customer_coun") + "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

}
