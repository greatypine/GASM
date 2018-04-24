package com.cnpc.pms.heatmap.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.heatmap.dao.OrderHeatDao;

/**
 * 
 * @author gaoll
 *
 */
public class OrderHeatDaoImpl extends DAORootHibernate implements OrderHeatDao{

	@Override
	public List<Map<String, Object>> getTinyVillageOrderInfo(Long store_id,String beginDate,String endDate) {
		String sql="select c.info_village_code as code ,count(c.id) AS count FROM df_mass_order_monthly c inner join t_store s on c.store_id = s.platformid " 
				+"where s.store_id = "+store_id+" and  c.sign_time>='"+beginDate+"' and c.sign_time<='"+endDate+"' and c.eshop_name NOT LIKE '%测试%' AND c.eshop_white!='QA' "
				+"and c.store_name NOT LIKE '%测试%' and c.store_white!='QA' AND c.store_status =0 and c.info_village_code is not null GROUP BY c.info_village_code";
		 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageGMVInfo(Long store_id, String beginDate, String endDate) {
		String sql="select c.info_village_code as code ,ifnull(SUM(c.gmv_price),0) AS count FROM df_mass_order_monthly c inner join t_store s on c.store_id = s.platformid " 
				+"where s.store_id = "+store_id+" and  c.sign_time>='"+beginDate+"' and c.sign_time<='"+endDate+"' and c.eshop_name NOT LIKE '%测试%' AND c.eshop_white!='QA' "
				+"and c.store_name NOT LIKE '%测试%' and c.store_white!='QA' AND c.store_status =0 and c.info_village_code is not null GROUP BY c.info_village_code";
		 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageOrderCount(Long store_id, String beginDate, String endDate) {
		String sql= "select ifnull(avg(count),0) as avgCount from (select count(c.id) AS count FROM df_mass_order_monthly c inner join t_store s on c.store_id = s.platformid " 
				+"where s.store_id = "+store_id+" and  c.sign_time>='"+beginDate+"' and c.sign_time<='"+endDate+"' and c.eshop_name NOT LIKE '%测试%' AND c.eshop_white!='QA' "
				+"and c.store_name NOT LIKE '%测试%' and c.store_white!='QA' AND c.store_status =0 and c.info_village_code is not null GROUP BY c.info_village_code) t";
		 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageGMVCount(Long store_id, String beginDate, String endDate) {
		String sql= "select ifnull(avg(count),0) as avgCount from (select ifnull(sum(c.gmv_price),0) AS count FROM df_mass_order_monthly c inner join t_store s on c.store_id = s.platformid  "
				+"where s.store_id = "+store_id+" and  c.sign_time>='"+beginDate+"' and c.sign_time<='"+endDate+"' and c.eshop_name NOT LIKE '%测试%' AND c.eshop_white!='QA' "
				+"and c.store_name NOT LIKE '%测试%' and c.store_white!='QA' AND c.store_status =0 and c.info_village_code is not null GROUP BY c.info_village_code) t";
		 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageCustomerInfo(Long store_id, String beginDate, String endDate) {
		/*String sql= "select a.tiny_village_id as id,a.tiny_village_code as code,count(a.customer_id) as count from df_customer_order_month_trade_new a INNER JOIN t_store b ON a.store_id = b.platformid "
				+"where b.store_id = "+store_id+" and a.tiny_village_code is not null GROUP BY a.tiny_village_code";*/
		String sql = "select c.info_village_code as code,count(DISTINCT c.customer_id) as count from  df_mass_order_monthly c inner join t_store s on c.store_code = s.storeno "
				+"where s.store_id = "+store_id+" and c.sign_time>='"+beginDate+"' and c.sign_time<='"+endDate+"' and c.eshop_name NOT LIKE '%测试%' AND c.eshop_white!='QA' "
				+"and c.info_village_code is not null and c.store_name NOT LIKE '%测试%' and c.store_white!='QA' AND c.store_status =0 GROUP BY c.info_village_code";
		 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageCustomerCount(Long store_id, String beginDate, String endDate) {
		/*String sql= "select MAX(count) as maxCount,MIN(count) as minCount from (select a.tiny_village_id as id,a.tiny_village_code as code,count(a.customer_id) as count from df_customer_order_month_trade_new a INNER JOIN t_store b ON a.store_id = b.platformid "
				+"where b.store_id = "+store_id+" and a.tiny_village_code is not null GROUP BY a.tiny_village_code) t";*/
		String sql = "select ifnull(avg(count),0) as avgCount from (select count(DISTINCT c.customer_id) as count from  df_mass_order_monthly c inner join t_store s on c.store_code = s.storeno "
				+"where s.store_id = "+store_id+" and c.sign_time>='"+beginDate+"' and c.sign_time<='"+endDate+"' and c.eshop_name NOT LIKE '%测试%' AND c.eshop_white!='QA' "
				+"and c.info_village_code is not null and c.store_name NOT LIKE '%测试%' and c.store_white!='QA' AND c.store_status =0 GROUP BY c.info_village_code) t";
		 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageOrderInfoByCity(Long city_id, String beginDate, String endDate) {
		String sql= "select a.info_village_code as code,ifnull(count(a.id),0) as count from df_mass_order_monthly a INNER JOIN t_store s ON a.store_code = s.storeno INNER JOIN t_dist_citycode c  ON s.city_name = c.cityname "
				+"where c.id = "+city_id+" and a.sign_time>='"+beginDate+"' and a.sign_time<='"+endDate+"' and a.info_village_code is not null and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' "
				+"and a.store_name NOT LIKE '%测试%' and a.store_white!='QA' AND a.store_status =0 GROUP BY a.info_village_code ";
		 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageOrderCountByCity(Long city_id, String beginDate, String endDate) {
		String sql= "select ifnull(avg(count),0) as avgCount from (select ifnull(count(a.id),0) as count from df_mass_order_monthly a INNER JOIN t_store s ON a.store_code = s.storeno INNER JOIN t_dist_citycode c  ON s.city_name = c.cityname "
				+"where c.id = "+city_id+" and a.sign_time>='"+beginDate+"' and a.sign_time<='"+endDate+"' and a.info_village_code is not null and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' "
				+"and a.store_name NOT LIKE '%测试%' and a.store_white!='QA' AND a.store_status =0 GROUP BY a.info_village_code) t";
		 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageGMVInfoByCity(Long city_id, String beginDate, String endDate) {
		String sql= "select a.info_village_code as code, sum(a.gmv_price) as count from df_mass_order_monthly a INNER JOIN t_store s ON a.store_code = s.storeno INNER JOIN t_dist_citycode c  ON s.city_name = c.cityname "
				+"where c.id = "+city_id+" and a.sign_time>='"+beginDate+"' and a.sign_time<='"+endDate+"' and a.info_village_code is not null and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' "
				+"and a.store_name NOT LIKE '%测试%' and a.store_white!='QA' AND a.store_status =0 GROUP BY a.info_village_code";
		 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageGMVCountByCity(Long city_id, String beginDate, String endDate) {
		String sql= "select ifnull(avg(count),0) as avgCount from (select ifnull(sum(a.gmv_price),0) as count from df_mass_order_monthly a INNER JOIN t_store s ON a.store_code = s.storeno INNER JOIN t_dist_citycode c  ON s.city_name = c.cityname "
				+"where c.id = "+city_id+" and a.sign_time>='"+beginDate+"' and a.sign_time<='"+endDate+"' and a.info_village_code is not null and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' "
				+"and a.store_name NOT LIKE '%测试%' and a.store_white!='QA' AND a.store_status =0 GROUP BY a.info_village_code) t";
		 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageCustomerInfoByCity(Long city_id, String beginDate, String endDate) {
		String sql= "select a.info_village_code as code,COUNT(DISTINCT a.customer_id) as count from df_mass_order_monthly a INNER JOIN t_store b ON a.store_code = b.storeno INNER JOIN t_dist_citycode c ON b.city_name = c.cityname "
				+ "where c.id = "+city_id+" and DATE_FORMAT(a.sign_time,'%Y/%m/%d HH:ii')>='"+beginDate+"' and DATE_FORMAT(a.sign_time,'%Y/%m/%d HH:ii')<='"+endDate+"' and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' "
				+"and a.info_village_code is not null and a.store_name NOT LIKE '%测试%' and a.store_white!='QA' AND a.store_status =0 GROUP BY a.info_village_code";
		 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageCustomerCountByCity(Long city_id, String beginDate, String endDate) {
		String sql= "select ifnull(avg(count),0) as avgCount from (select COUNT(DISTINCT a.customer_id) as count from df_mass_order_monthly a INNER JOIN t_store b ON a.store_code = b.storeno INNER JOIN t_dist_citycode c ON b.city_name = c.cityname "
				+"where c.id = "+city_id+" and DATE_FORMAT(a.sign_time,'%Y/%m/%d HH:ii')>='"+beginDate+"' and DATE_FORMAT(a.sign_time,'%Y/%m/%d HH:ii')<='"+endDate+"' and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA'  "
				+"and a.info_village_code is not null and a.store_name NOT LIKE '%测试%' and a.store_white!='QA' AND a.store_status =0  GROUP BY a.info_village_code) t";
		 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

}
