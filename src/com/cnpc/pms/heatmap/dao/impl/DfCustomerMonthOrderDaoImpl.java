package com.cnpc.pms.heatmap.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.heatmap.dao.DfCustomerMonthOrderDao;

/**
 * 
 * @author gaoll
 *
 */
public class DfCustomerMonthOrderDaoImpl extends DAORootHibernate implements DfCustomerMonthOrderDao{

	@Override
	public Integer findAllCustomer() {
		String sql = "select count(customer_id) from (select customer_id from df_customer_order_month_trade_new GROUP BY customer_id) t";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public Integer findCustomerByProvinceId(Long province_id) {
		String sql = "select count(1) from (select customer_id from df_customer_order_month_trade_new customer INNER JOIN (select t_store.platformid from t_store where province_id = "+province_id+") t "
				+ "ON t.platformid = customer.store_id GROUP BY customer.customer_id) t1";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public Integer findCustomerByCityId(Long city_id) {
		String sql = "select count(1) from (select customer_id from df_customer_order_month_trade_new customer INNER JOIN t_store store ON store.platformid = customer.store_id"
				+" INNER JOIN t_dist_citycode citycode ON store.city_name = citycode.cityname where citycode.id = "+city_id+" GROUP BY customer.customer_id) t";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public Integer findCustomerByStoreId(Long store_id) {
		String sql = "select COUNT(1) from (select customer_id from df_customer_order_month_trade_new customer INNER JOIN t_store store ON "
				+ "store.platformid = customer.store_id and store.store_id = "+store_id+" GROUP BY customer.customer_id) t";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public Integer findCustomerByAreaNo(String area_no) {
		String sql = "select count(DISTINCT customer.customer_id) from df_customer_order_month_trade_new customer INNER JOIN "
				+" (select ttv.id from t_area_info tai INNER JOIN t_tiny_village ttv on tai.tin_village_id = ttv.id and tai.tin_village_id is not  NULL and tai.status=0 and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null ) where tai.area_no = '"+area_no+"'"
				+" UNION"
				+" select ttv.id from t_area_info tai INNER JOIN t_tiny_village  ttv on  tai.status=0 and tai.tin_village_id is null and tai.village_id = ttv.village_id   and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null) where tai.area_no = '"+area_no+"') c"
				+" ON customer.tiny_village_id = c.id";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public Integer findCustomerByVillageCode(String code) {
		String sql = "select count(DISTINCT customer_id) from df_customer_order_month_trade_new where tiny_village_code = '"+code+"'";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findCustomerLatLngByCity(Long city_id) {
		String sql="select customer.longitude as lng,customer.latitude as lat,count(1) as count from df_customer_order_month_trade_new customer INNER JOIN t_store store ON store.platformid = customer.store_id "
				+"INNER JOIN t_dist_citycode citycode ON store.city_name = citycode.cityname where citycode.id = "+city_id+" and customer.latitude is not null and customer.longitude is not null "
				+"and customer.latitude !=0 and customer.longitude !=0  GROUP BY customer.latitude,customer.longitude";
 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> findCustomerLatLngCountByCity(Long city_id) {
		String sql="select max(t.count) as maxCount,min(t.count) as minCount from (select count(1) as count from df_customer_order_month_trade_new customer INNER JOIN t_store store ON store.platformid = customer.store_id "
				+"INNER JOIN t_dist_citycode citycode ON store.city_name = citycode.cityname where citycode.id = "+city_id+" and customer.latitude is not null and customer.longitude is not null "
				+"and customer.latitude !=0 and customer.longitude !=0 GROUP BY customer.latitude,customer.longitude) t";
 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> findCustomerLatLngByStore(Long store_id) {
		String sql="select count(1) as count,customer.longitude as lng,customer.latitude as lat from df_customer_order_month_trade_new customer INNER JOIN "
				+"t_store store ON store.platformid = customer.store_id where store.store_id = "+store_id+" and customer.latitude is not null "
				+"and customer.longitude is not null and customer.latitude !=0 and customer.longitude !=0  GROUP BY customer.latitude,customer.longitude";
 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> findCustomerLatLngCountByStore(Long store_id) {
		String sql="select max(t.count) as maxCount,min(t.count) as minCount from (select count(1) as count from df_customer_order_month_trade_new customer INNER JOIN "
				+"t_store store ON store.platformid = customer.store_id where store.store_id = "+store_id+" and customer.latitude is not null "
				+"and customer.longitude is not null and customer.latitude !=0 and customer.longitude !=0  GROUP BY customer.latitude,customer.longitude) t";
 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


}
