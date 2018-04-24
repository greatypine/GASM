package com.cnpc.pms.dynamic.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.dynamic.dao.ChartStatDao;

public class ChartStatDaoImpl extends BaseDAOHibernate implements ChartStatDao {

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryDayTurnover(String storeno,String cityname) {
		
		String sql = "SELECT IFNULL(FLOOR(SUM(dod.trading_price)),0) AS day_amount FROM df_mass_order_daily dod WHERE DATE(dod.sign_time) = DATE(curdate()) ";
		if(StringUtils.isNotEmpty(storeno)){
			sql = sql + "AND dod.store_code = '"+storeno+"' ";
		}
		if(StringUtils.isNotEmpty(cityname)){
			sql = sql + "AND dod.store_city_name='"+cityname+"' ";
		}
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		Map<String, Object> order_obj = null;
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (lst_data != null && lst_data.size() > 0) {
			order_obj = (Map<String, Object>) lst_data.get(0);
		}
		return order_obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryTurnorverByRealtime(String storeno, String cityname){
		String sql = "SELECT DATE_FORMAT(NOW(),'%H:%i:%S') AS real_time, IFNULL(FLOOR(SUM(trading_price)), 0) AS turnover FROM	df_mass_order_daily dod WHERE 1=1 ";
		if(StringUtils.isNotEmpty(storeno)){
			sql = sql + " AND dod.store_code = '"+storeno+"' ";
		}
		if(StringUtils.isNotEmpty(cityname)){
			sql = sql + " AND dod.store_city_name='"+cityname+"' ";
		}
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		Map<String, Object> order_obj = null;
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (lst_data != null && lst_data.size() > 0) {
			order_obj = (Map<String, Object>) lst_data.get(0);
		}
		return order_obj;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryTurnoverByHour(String storeno,String cityname){
		String sql = "SELECT IFNULL(FLOOR(sum(dom.trading_price)), 0) as turnover,DATE_FORMAT(dom.sign_time,'%H') as time FROM	df_mass_order_daily dom "
				+ "WHERE DATEDIFF(dom.sign_time,NOW())=0 ";
		if(StringUtils.isNotEmpty(storeno)){
			sql = sql + " AND dom.store_code = '"+storeno+"' ";
		}
		if(StringUtils.isNotEmpty(cityname)){
			sql = sql + " AND dom.store_city_name='"+cityname+"' ";
		}
		sql = sql + " GROUP BY DATE_FORMAT(dom.sign_time,'%H') ";
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryTurnoverByDay(String storeno,String cityname){
		String sql="SELECT IFNULL(FLOOR(sum(dom.trading_price)), 0) AS turnover,DATE(dom.sign_time) as day_time FROM df_mass_order_monthly dom "
				+ "WHERE DATE_ADD(sign_time, INTERVAL 6 DAY) > DATE(CURDATE()) ";
		if(StringUtils.isNotEmpty(storeno)){
			sql = sql + " AND dom.store_code = '"+storeno+"' ";
		}
		if(StringUtils.isNotEmpty(cityname)){
			sql = sql + " AND dom.store_city_name='"+cityname+"' ";
		}
		sql = sql + " GROUP BY DATE(dom.sign_time) ";
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryMonthTurnover(String storeno,String cityname) {
		String sql = "SELECT IFNULL(sum(dst.order_amount),0) as month_amount FROM ds_storetrade dst WHERE dst.year = YEAR(curdate()) AND dst.month = MONTH(curdate()) ";
		if(StringUtils.isNotEmpty(storeno)){
			sql = sql + "AND dst.storeno = '"+storeno+"' ";
		}
		if(StringUtils.isNotEmpty(cityname)){
			sql = sql + "AND dst.city_name='"+cityname+"' ";
		}
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		Map<String, Object> order_obj = null;
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (lst_data != null && lst_data.size() > 0) {
			order_obj = (Map<String, Object>) lst_data.get(0);
		}
		return order_obj;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryTurnoverByWeek(String storeno,String cityname){
		String sql = "SELECT YEARWEEK(sign_time) as week_date ,	subdate(sign_time,date_format(sign_time,'%w')-7) as week_time, IFNULL(FLOOR(SUM(trading_price)),0) AS week_amount FROM df_mass_order_total WHERE 1=1 ";
		if(StringUtils.isNotEmpty(storeno)){
			sql = sql + " AND store_code = '"+storeno+"' ";
		}
		if(StringUtils.isNotEmpty(cityname)){
			sql = sql + " AND store_city_name='"+cityname+"' ";
		}
		sql = sql + " GROUP BY week_date";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryTurnoverByMonth(String storeno,String cityname){
		String sql = "SELECT IFNULL(FLOOR(SUM(dst.order_amount)),0) AS month_amount,CONCAT(dst.year,'-',dst.month) AS month_time from ds_storetrade dst WHERE 1=1 ";
		if(StringUtils.isNotEmpty(storeno)){
			sql = sql + " AND dst.storeno = '"+storeno+"' ";
		}
		if(StringUtils.isNotEmpty(cityname)){
			sql = sql + " AND dst.city_name='"+cityname+"' ";
		}
		sql = sql + " GROUP BY dst.year,dst.month ";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryOnlineUser(String storeno,String cityname) {
		String sql = "SELECT IFNULL(COUNT(DISTINCT(dom.customer_id)),0) as customer_num FROM df_mass_order_monthly dom WHERE date(dom.sign_time) = curdate() AND customer_id NOT LIKE 'fakecustomer%' "
				+ "AND delivery_type != 'self' ";
		if(StringUtils.isNotEmpty(storeno)){
			sql = sql + "AND dom.store_code = '"+storeno+"' ";
		}
		if(StringUtils.isNotEmpty(cityname)){
			sql = sql + "AND dom.store_city_name='"+cityname+"' ";
		}
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		Map<String, Object> order_obj = null;
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (lst_data != null && lst_data.size() > 0) {
			order_obj = (Map<String, Object>) lst_data.get(0);
		}
		return order_obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryOfflineUser(String storeno,String cityname) {
		String sql = "SELECT IFNULL(COUNT(DISTINCT(dom.customer_id)),0) as customer_num FROM df_mass_order_monthly dom WHERE date(dom.sign_time) = curdate() AND customer_id LIKE 'fakecustomer%' "
				+ "OR delivery_type = 'self' ";
		if(StringUtils.isNotEmpty(storeno)){
			sql = sql + "AND dom.store_code = '"+storeno+"' ";
		}
		if(StringUtils.isNotEmpty(cityname)){
			sql = sql + "AND dom.store_city_name='"+cityname+"' ";
		}
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		Map<String, Object> order_obj = null;
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (lst_data != null && lst_data.size() > 0) {
			order_obj = (Map<String, Object>) lst_data.get(0);
		}
		return order_obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryMonthConsumptionUser(String storeno, String cityname) {
		String sql = "SELECT IFNULL(SUM(dcm.pay_count), 0) AS pay_count, IFNULL(SUM(dcm.new_count), 0) AS new_count, dcm.order_ym as month_time FROM ds_cusum_month dcm WHERE 1=1 ";
		if(StringUtils.isNotEmpty(storeno)){
			sql = sql + " AND dcm.storeno='"+storeno+"'";
		}
		if(StringUtils.isNotEmpty(cityname)){
			sql = sql + " AND dcm.city_id=(select id from t_dist_citycode where cityname = '"+cityname+"')";
		}
		sql = sql + "  GROUP BY month_time";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryCurMonthUser(String storeno, String cityname){
		String sql = "SELECT IFNULL(SUM(dcm.pay_count), 0) AS pay_count, IFNULL(SUM(dcm.new_count), 0) AS new_count FROM ds_cusum_month dcm WHERE dcm.order_ym = DATE_FORMAT(CURDATE(),'%Y%m') ";
		if(StringUtils.isNotEmpty(storeno)){
			sql = sql + " AND dcm.storeno='"+storeno+"'";
		}
		if(StringUtils.isNotEmpty(cityname)){
			sql = sql + " AND dcm.city_id=(select id from t_dist_citycode where cityname = '"+cityname+"')";
		}
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		Map<String, Object> order_obj = null;
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (lst_data != null && lst_data.size() > 0) {
			order_obj = (Map<String, Object>) lst_data.get(0);
		}
		return order_obj;
	}
	
}
