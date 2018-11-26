package com.cnpc.pms.personal.dao.impl;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.personal.dao.ChartStatDao;
import com.cnpc.pms.personal.dto.ChartStatDto;
import com.cnpc.pms.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartStatDaoImpl extends BaseDAOHibernate implements ChartStatDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryContainsStoreDistCityList(){
		String sql = "select DISTINCT(cityno) as cityno,city_name as cityname from t_store where name not like '%储备%' and name not like '%办公%' and name not like '%测试%' and flag=0 ";
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryDayTurnover(ChartStatDto csd) {
		
		String sql = "SELECT IFNULL(FLOOR(SUM(dod.gmv_price)),0) AS day_amount FROM df_mass_order_daily dod WHERE DATE(dod.sign_time) = DATE(curdate()) "
				+ "AND dod.store_name NOT LIKE '%测试%' ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dod.store_code = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND dod.store_city_name like '"+csd.getCityname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND dod.department_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND dod.channel_name like '%"+csd.getChannelname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getcLabel()) && StringUtils.isNotEmpty(csd.getcLabel())){
			if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "0".equals(csd.getMaxBLabel())){//C+小B
				sql = sql + " AND dod.store_name not like '%企业购%' ";
			}else if("0".equals(csd.getcLabel()) && "0".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//大B
				sql = sql + " AND dod.store_name like '%企业购%' ";
			}else if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//All
			}else{
				return new HashMap<String, Object>();
			}
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
	public List<Map<String, Object>> queryTurnoverByHour(ChartStatDto csd){
		String sql = "SELECT IFNULL(FLOOR(sum(dom.gmv_price)), 0) as turnover,DATE_FORMAT(dom.sign_time,'%H') as time FROM	df_mass_order_daily dom "
				+ "WHERE DATEDIFF(dom.sign_time,NOW())=0 AND dom.store_name NOT LIKE '%测试%' ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dom.store_code = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND dom.store_city_name like '"+csd.getCityname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND dom.department_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND dom.channel_name like '%"+csd.getChannelname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getcLabel()) && StringUtils.isNotEmpty(csd.getcLabel())){
			if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "0".equals(csd.getMaxBLabel())){//C+小B
				sql = sql + " AND dom.store_name not like '%企业购%' ";
			}else if("0".equals(csd.getcLabel()) && "0".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//大B
				sql = sql + " AND dom.store_name like '%企业购%' ";
			}else if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//All
			}else{
				return new ArrayList<Map<String, Object>>();
			}
		}
		sql = sql + " GROUP BY DATE_FORMAT(dom.sign_time,'%H') ";
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryTurnoverByDay(ChartStatDto csd){
		String sql="SELECT IFNULL(FLOOR(sum(dom.gmv_price)), 0) AS turnover,DATE(dom.sign_time) as day_time FROM df_mass_order_total dom "
				+ "WHERE dom.store_name NOT LIKE '%测试%' ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dom.store_code = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND dom.store_city_name like '"+csd.getCityname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND dom.department_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND dom.channel_name like '%"+csd.getChannelname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getcLabel()) && StringUtils.isNotEmpty(csd.getcLabel())){
			if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "0".equals(csd.getMaxBLabel())){//C+小B
				sql = sql + " AND dom.store_name not like '%企业购%' ";
			}else if("0".equals(csd.getcLabel()) && "0".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//大B
				sql = sql + " AND dom.store_name like '%企业购%' ";
			}else if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//All
			}else{
				return new ArrayList<Map<String, Object>>();
			}
		}
		sql = sql + " GROUP BY DATE(dom.sign_time) ";
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryMonthTurnover(ChartStatDto csd) {
		String sql = "SELECT IFNULL(FLOOR(sum(dst.order_amount)),0) as month_amount FROM ds_ope_gmv_storechannel_month dst WHERE dst.year = YEAR(curdate()) AND dst.month = MONTH(curdate()) ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dst.storeno = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND dst.city_name='"+csd.getCityname()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND dst.dep_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND dst.channel_name like '%"+csd.getChannelname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getcLabel()) && StringUtils.isNotEmpty(csd.getcLabel())){
			if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "0".equals(csd.getMaxBLabel())){//C+小B
				sql = sql + " AND dst.store_name not like '%企业购%' ";
			}else if("0".equals(csd.getcLabel()) && "0".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//大B
				sql = sql + " AND dst.store_name like '%企业购%' ";
			}else if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//All
			}else{
				return new HashMap<String, Object>();
			}
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
	public Map<String, Object> queryOnlineOfflineTurnover(ChartStatDto csd){
		String sql = "SELECT IFNULL(FLOOR(SUM(CASE WHEN (`delivery_type` = 'self' OR order_address_id IS NULL) THEN gmv_price ELSE 0 END)),0) offline_amount,	"
				+ "IFNULL(FLOOR(SUM(CASE WHEN (`delivery_type` != 'self' AND order_address_id IS NOT NULL) THEN gmv_price ELSE 0 END ) ), 0 ) online_amount FROM df_mass_order_total dom "
				+ "WHERE dom.store_name NOT LIKE '%测试%' ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dom.store_code = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND dom.store_city_name like '"+csd.getCityname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND dom.department_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND dom.channel_name like '%"+csd.getChannelname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getcLabel()) && StringUtils.isNotEmpty(csd.getcLabel())){
			if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "0".equals(csd.getMaxBLabel())){//C+小B
				sql = sql + " AND dom.store_name not like '%企业购%' ";
			}else if("0".equals(csd.getcLabel()) && "0".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//大B
				sql = sql + " AND dom.store_name like '%企业购%' ";
			}else if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//All
			}else{
				return new HashMap<String, Object>();
			}
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
	public List<Map<String, Object>> queryTurnoverByWeek(ChartStatDto csd){
		String sql = "SELECT YEARWEEK(sign_time) as week_date ,	subdate(sign_time,date_format(sign_time,'%w')) as week_time, IFNULL(FLOOR(SUM(gmv_price)),0) AS week_amount FROM df_mass_order_total WHERE store_name NOT LIKE '%测试%' ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND store_code = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND store_city_name like '"+csd.getCityname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND department_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND channel_name like '%"+csd.getChannelname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getcLabel()) && StringUtils.isNotEmpty(csd.getcLabel())){
			if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "0".equals(csd.getMaxBLabel())){//C+小B
				sql = sql + " AND store_name not like '%企业购%' ";
			}else if("0".equals(csd.getcLabel()) && "0".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//大B
				sql = sql + " AND store_name like '%企业购%' ";
			}else if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//All
			}else{
				return new ArrayList<Map<String, Object>>();
			}
		}
		sql = sql + " GROUP BY week_date";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryTurnoverByMonth(ChartStatDto csd){
		String sql = "SELECT IFNULL(FLOOR(SUM(dst.order_amount)),0) AS month_amount,CONCAT(dst.year,'-',dst.month) AS month_time from ds_ope_gmv_storechannel_month dst "
				+ "WHERE dst.year = YEAR(curdate()) ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dst.storeno = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND dst.city_name like '"+csd.getCityname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND dst.dep_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND dst.channel_name like '%"+csd.getChannelname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getcLabel()) && StringUtils.isNotEmpty(csd.getcLabel())){
			if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "0".equals(csd.getMaxBLabel())){//C+小B
				sql = sql + " AND dst.store_name not like '%企业购%' ";
			}else if("0".equals(csd.getcLabel()) && "0".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//大B
				sql = sql + " AND dst.store_name like '%企业购%' ";
			}else if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//All
			}else{
				return new ArrayList<Map<String, Object>>();
			}
		}
		sql = sql + " GROUP BY dst.year,dst.month ";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryTargetByMonth(ChartStatDto csd){
		String sql = "SELECT IFNULL(FLOOR(SUM(dt.target_value)), 0) AS month_amount,CONCAT(dt. YEAR, '-', dt. MONTH) AS month_time "
				+ "FROM	df_bussiness_target dt WHERE dt.type = 'gmv' AND dt.period_type = 'month' AND dt. YEAR = YEAR (curdate()) ";
		if(StringUtils.isEmpty(csd.getCityname()) && StringUtils.isEmpty(csd.getDeptname()) && StringUtils.isEmpty(csd.getChannelname())){
			sql = sql + " AND dt.city_name!='' ";
		}else{
			if(StringUtils.isNotEmpty(csd.getCityname())){
				sql = sql + " AND dt.city_name like '"+csd.getCityname()+"%' ";
			}
			if(StringUtils.isNotEmpty(csd.getDeptname())){
				sql = sql + " AND dt.dep_name like '%"+csd.getDeptname()+"%' ";
			}
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND dt.channel_name like '%"+csd.getChannelname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dt.store_no = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getcLabel()) && StringUtils.isNotEmpty(csd.getSmallBLabel())){
			sql = sql + " AND dt.param_first=" + csd.getcLabel() +" AND dt.param_second="+csd.getSmallBLabel();
		}
		if(StringUtils.isNotEmpty(csd.getMaxBLabel())){
			sql = sql + " AND dt.param_third=" + csd.getMaxBLabel();
		}
		sql = sql + " GROUP BY dt.year,dt.month ";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryDataOfScatterplot(ChartStatDto csd){
		String sql = "SELECT IFNULL(SUM(dsc.order_count),0) AS order_count,	IFNULL(FLOOR(SUM(dsc.order_amount)),0) AS order_amount, dsc.city_name,dsc.cityno, dsc.store_name,CONCAT(dsc.year,'-',dsc.month) as time,dsc.storeno "
				+ "FROM ds_ope_gmv_storechannel_month dsc WHERE 1=1 ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dsc.storeno = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND dsc.city_name like '"+csd.getCityname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND dep_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND channel_name like '%"+csd.getChannelname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getcLabel()) && StringUtils.isNotEmpty(csd.getcLabel())){
			if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "0".equals(csd.getMaxBLabel())){//C+小B
				sql = sql + " AND store_name not like '%企业购%' ";
			}else if("0".equals(csd.getcLabel()) && "0".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//大B
				sql = sql + " AND store_name like '%企业购%' ";
			}else if("1".equals(csd.getcLabel()) && "1".equals(csd.getSmallBLabel()) && "1".equals(csd.getMaxBLabel())){//All
			}else{
				return new ArrayList<Map<String, Object>>();
			}
		}
		sql = sql + " GROUP BY city_name,store_name,time HAVING time>=DATE_FORMAT(DATE_SUB( CURRENT_DATE() , INTERVAL 3 MONTH ),'%Y-%c') ";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryDayUser(ChartStatDto csd) {
		String sql = "SELECT COUNT(DISTINCT customer_id) cus_count,	COUNT(DISTINCT (CASE WHEN customer_isnew_flag != '-1' THEN customer_id END)) AS new_cus_count "
				+ "FROM df_mass_order_daily dod WHERE DATE(dod.sign_time) = DATE(curdate()) AND dod.store_name NOT LIKE '%测试%' ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dod.store_code = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND dod.store_city_name='"+csd.getCityname()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND dod.department_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND dod.channel_name like '%"+csd.getChannelname()+"%' ";
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
	public List<Map<String, Object>> queryUserByHour(ChartStatDto csd){
		String sql = "SELECT COUNT(DISTINCT customer_id) cus_count,	COUNT(DISTINCT( CASE WHEN customer_isnew_flag != '-1' THEN customer_id END)) AS new_cus_count,"
				+ "DATE_FORMAT(dod.sign_time, '%H') AS time FROM df_mass_order_daily dod WHERE	DATE(dod.sign_time) = DATE(curdate()) AND dod.store_name NOT LIKE '%测试%' ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dod.store_code = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND dod.store_city_name='"+csd.getCityname()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND dod.department_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND dod.channel_name like '%"+csd.getChannelname()+"%' ";
		}
		sql = sql + " GROUP BY time ";
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryUserByDay(ChartStatDto csd){
		String sql="SELECT COUNT(DISTINCT customer_id) cus_count,COUNT(DISTINCT( CASE WHEN customer_isnew_flag != '-1' THEN customer_id END)) AS new_cus_count,"
				+ "DATE(dom.sign_time) as day_time FROM df_mass_order_total dom WHERE dom.store_name NOT LIKE '%测试%' ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dom.store_code = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND dom.store_city_name='"+csd.getCityname()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND dom.department_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND dom.channel_name like '%"+csd.getChannelname()+"%' ";
		}
		sql = sql + " GROUP BY day_time ";
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryUserByWeek(ChartStatDto csd) {
		String sql = "SELECT YEARWEEK(sign_time) as week_date ,	subdate(sign_time,date_format(sign_time,'%w')) as week_time, COUNT(DISTINCT customer_id) cus_count,"
				+ "COUNT(DISTINCT (CASE WHEN customer_isnew_flag != '-1' THEN customer_id END)) AS new_cus_count FROM df_mass_order_total WHERE store_name NOT LIKE '%测试%' ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND store_code = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND store_city_name='"+csd.getCityname()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND department_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND channel_name like '%"+csd.getChannelname()+"%' ";
		}
		sql = sql + " GROUP BY week_date";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryUserByMonth(ChartStatDto csd){
		String sql = "SELECT DATE_FORMAT(sign_time, '%Y-%c') AS month_date, COUNT(DISTINCT customer_id) cus_count,"
				+ "COUNT(DISTINCT (CASE WHEN customer_isnew_flag != '-1' THEN customer_id END)) AS new_cus_count FROM df_mass_order_total WHERE store_name NOT LIKE '%测试%' ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND store_code = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND store_city_name='"+csd.getCityname()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND department_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND channel_name like '%"+csd.getChannelname()+"%' ";
		}
		sql = sql + " GROUP BY month_date";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryOnlineOfflineUser(ChartStatDto csd) {
		String sql = "SELECT COUNT(DISTINCT (CASE WHEN customer_id LIKE 'fakecustomer%'	OR delivery_type = 'self' THEN	dom.customer_id END)) AS offline_customer,"
				+ "COUNT(DISTINCT (CASE WHEN customer_id NOT LIKE 'fakecustomer%' AND delivery_type != 'self' THEN dom.customer_id END)) AS online_customer "
				+ "FROM df_mass_order_total dom WHERE dom.store_name NOT LIKE '%测试%' ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dom.store_code = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND dom.store_city_name='"+csd.getCityname()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND dom.department_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND dom.channel_name like '%"+csd.getChannelname()+"%' ";
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
	public List<Map<String, Object>> querUserOfScatterplot(ChartStatDto csd) {
		String curMonthFirst = DateUtils.getCurrMonthFirstDate("yyyy-MM-dd");
		String sql =  "SELECT COUNT(DISTINCT customer_id) pay_count, COUNT(DISTINCT(CASE WHEN customer_isnew_flag != '-1' THEN customer_id END)) AS new_count,"
				+ "dom.store_city_name FROM df_mass_order_monthly dom WHERE dom.sign_time >= '"+curMonthFirst+"' "
				+ "AND dom.store_name NOT LIKE '%测试%' AND dom.store_code IS NOT NULL AND dom.store_code != '' ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dom.store_code = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND dom.store_city_name='"+csd.getCityname()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND dom.department_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND dom.channel_name like '%"+csd.getChannelname()+"%' ";
		}
		sql = sql + " GROUP BY store_city_name";

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryCurMonthUser(ChartStatDto csd){
		String curMonthFirst = DateUtils.getCurrMonthFirstDate("yyyy-MM-dd");
		String sql = "SELECT COUNT(DISTINCT customer_id) cus_count FROM	df_mass_order_monthly dod WHERE	dod.sign_time >= '"+curMonthFirst+"' AND dod.store_name NOT LIKE '%测试%' ";
		if(StringUtils.isNotEmpty(csd.getStoreno())){
			sql = sql + " AND dod.store_code = '"+csd.getStoreno()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getCityname())){
			sql = sql + " AND dod.store_city_name='"+csd.getCityname()+"' ";
		}
		if(StringUtils.isNotEmpty(csd.getDeptname())){
			sql = sql + " AND dod.department_name like '%"+csd.getDeptname()+"%' ";
		}
		if(StringUtils.isNotEmpty(csd.getChannelname())){
			sql = sql + " AND dod.channel_name like '%"+csd.getChannelname()+"%' ";
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
