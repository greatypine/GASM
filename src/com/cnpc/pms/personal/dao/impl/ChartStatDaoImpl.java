package com.cnpc.pms.personal.dao.impl;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.dao.ChartStatDao;
import com.cnpc.pms.personal.dto.ChartStatDto;
import com.cnpc.pms.personal.entity.UserRecommendInfo;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.ImpalaUtil;
import com.ibm.db2.jcc.a.a;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import java.util.ArrayList;
import java.util.Collection;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryBusinesSummary(QueryConditions queryConditions) {
		// TODO Auto-generated method stub
		List list0 = new ArrayList();
		String storeId = queryConditions.getQueryId();
		
		String sql1 = "SELECT day1_order_count, day1_order_amount, day1_customer_count, day1_new_customer, day2_order_count, day2_order_amount, day2_customer_count, day2_new_customer, mon1_order_count, mon1_order_amount, mon1_customer_count, mon1_new_customer, mon2_order_count, mon2_order_amount, mon2_customer_count, mon2_new_customer FROM ( SELECT 'flag' AS flag, count(1) AS day1_order_count, sum( gmv_price - IF ( return_label = '1', returned_amount, 0 )) AS day1_order_amount, count(DISTINCT customer_id) AS day1_customer_count FROM df_mass_order_total WHERE store_id = '00000000000000000000000000000030' AND to_date (sign_time) = to_date (days_sub(now(), 1))) temp11 LEFT JOIN ( SELECT 'flag' AS flag, count(DISTINCT customer_id) AS day1_new_customer FROM df_mass_order_total WHERE store_id = '00000000000000000000000000000030' AND customer_isnew_flag != '-1' AND to_date (sign_time) = to_date (days_sub(now(), 1))) temp12 ON temp11.flag = temp12.flag LEFT JOIN ( SELECT 'flag' AS flag, count(1) AS day2_order_count, sum( gmv_price - IF ( return_label = '1', returned_amount, 0 )) AS day2_order_amount, count(DISTINCT customer_id) AS day2_customer_count FROM df_mass_order_total WHERE store_id = '00000000000000000000000000000030' AND to_date (sign_time) = to_date (days_sub(now(), 2))) temp21 ON temp12.flag = temp21.flag LEFT JOIN ( SELECT 'flag' AS flag, count(DISTINCT customer_id) AS day2_new_customer FROM df_mass_order_total WHERE store_id = '00000000000000000000000000000030' AND customer_isnew_flag != '-1' AND to_date (sign_time) = to_date (days_sub(now(), 2))) temp22 ON temp21.flag = temp22.flag LEFT JOIN ( SELECT 'flag' AS flag, count(1) AS mon1_order_count, sum( gmv_price - IF ( return_label = '1', returned_amount, 0 )) AS mon1_order_amount, count(DISTINCT customer_id) AS mon1_customer_count FROM df_mass_order_total WHERE store_id = '00000000000000000000000000000030' AND substr(sign_time, 1, 7) = from_unixtime( cast( months_sub (now(), 1) AS BIGINT ), \"yyyy-MM\" )) temp31 ON temp22.flag = temp31.flag LEFT JOIN ( SELECT 'flag' AS flag, count(DISTINCT customer_id) AS mon1_new_customer FROM df_mass_order_total WHERE store_id = '00000000000000000000000000000030' AND customer_isnew_flag != '-1' AND substr(sign_time, 1, 7) = from_unixtime( cast( months_sub (now(), 1) AS BIGINT ), \"yyyy-MM\" )) temp32 ON temp31.flag = temp32.flag LEFT JOIN ( SELECT 'flag' AS flag, count(1) AS mon2_order_count, sum( gmv_price - IF ( return_label = '1', returned_amount, 0 )) AS mon2_order_amount, count(DISTINCT customer_id) AS mon2_customer_count FROM df_mass_order_total WHERE store_id = '00000000000000000000000000000030' AND substr(sign_time, 1, 7) = from_unixtime( cast( months_sub (now(), 2) AS BIGINT ), \"yyyy-MM\" )) temp41 ON temp32.flag = temp41.flag LEFT JOIN ( SELECT 'flag' AS flag, count(DISTINCT customer_id) AS mon2_new_customer FROM df_mass_order_total WHERE store_id = '00000000000000000000000000000030' AND customer_isnew_flag != '-1' AND substr(sign_time, 1, 7) = from_unixtime( cast( months_sub (now(), 2) AS BIGINT ), \"yyyy-MM\" )) temp42 ON temp41.flag = temp42.flag";
		List<Map<String,Object>> list1 = ImpalaUtil.execute(sql1);
		list0.addAll(list1);
		
		String sql2 = "SELECT num1_ad, num2_ad, num3_ad, num4_ad FROM ( SELECT 'flag' AS flag, num1_ad, num2_ad, num4_ad FROM ( SELECT row_number () over (ORDER BY temp11.count DESC) AS num1_ad, row_number () over ( ORDER BY temp11.order_amount DESC ) AS num2_ad, row_number () over ( ORDER BY temp11.customer_count DESC ) AS num4_ad, temp11.store_id FROM ( SELECT store_id, count(1) AS count, sum( gmv_price - IF ( return_label = '1', returned_amount, 0 )) AS order_amount, count(DISTINCT customer_id) AS customer_count FROM df_mass_order_total dmot LEFT JOIN gemini.t_order_address toa ON dmot.order_address_id = toa.id WHERE 1 = 1 AND to_date (sign_time) = to_date (days_sub(now(), 1)) AND ad_code IN ( SELECT t2.ad_code FROM df_mass_order_total t1 LEFT JOIN gemini.t_order_address t2 ON t1.order_address_id = t2.id WHERE t1.store_id = '00000000000000000000000000000030' GROUP BY t2.ad_code ) GROUP BY store_id ) temp11 ) a1 WHERE a1.store_id = '00000000000000000000000000000030' ) A1 LEFT JOIN ( SELECT 'flag' AS flag, num3_ad FROM ( SELECT row_number () over ( ORDER BY temp12.new_customer DESC ) AS num3_ad, temp12.store_id FROM ( SELECT store_id, count(DISTINCT customer_id) AS new_customer FROM df_mass_order_total dmot LEFT JOIN gemini.t_order_address toa ON dmot.order_address_id = toa.id WHERE 1 = 1 AND to_date (sign_time) = to_date (days_sub(now(), 1)) AND ad_code IN ( SELECT t2.ad_code FROM df_mass_order_total t1 LEFT JOIN gemini.t_order_address t2 ON t1.order_address_id = t2.id WHERE t1.store_id = '00000000000000000000000000000030' AND t1.customer_isnew_flag != '-1' GROUP BY t2.ad_code ) GROUP BY store_id ) temp12 ) a2 WHERE a2.store_id = '00000000000000000000000000000030' ) A2 ON A1.flag = A2.flag UNION ALL SELECT num1_ad, num2_ad, num3_ad, num4_ad FROM ( SELECT 'flag' AS flag, num1_ad, num2_ad, num4_ad FROM ( SELECT row_number () over (ORDER BY temp11.count DESC) AS num1_ad, row_number () over ( ORDER BY temp11.order_amount DESC ) AS num2_ad, row_number () over ( ORDER BY temp11.customer_count DESC ) AS num4_ad, temp11.store_id FROM ( SELECT store_id, count(1) AS count, sum( gmv_price - IF ( return_label = '1', returned_amount, 0 )) AS order_amount, count(DISTINCT customer_id) AS customer_count FROM df_mass_order_total dmot LEFT JOIN gemini.t_order_address toa ON dmot.order_address_id = toa.id WHERE 1 = 1 AND substr(sign_time, 1, 7) = from_unixtime( cast( months_sub (now(), 1) AS BIGINT ), \"yyyy-MM\" ) AND ad_code IN ( SELECT t2.ad_code FROM df_mass_order_total t1 LEFT JOIN gemini.t_order_address t2 ON t1.order_address_id = t2.id WHERE t1.store_id = '00000000000000000000000000000030' GROUP BY t2.ad_code ) GROUP BY store_id ) temp11 ) a1 WHERE a1.store_id = '00000000000000000000000000000030' ) A1 LEFT JOIN ( SELECT 'flag' AS flag, num3_ad FROM ( SELECT row_number () over ( ORDER BY temp12.new_customer DESC ) AS num3_ad, temp12.store_id FROM ( SELECT store_id, count(DISTINCT customer_id) AS new_customer FROM df_mass_order_total dmot LEFT JOIN gemini.t_order_address toa ON dmot.order_address_id = toa.id WHERE 1 = 1 AND substr(sign_time, 1, 7) = from_unixtime( cast( months_sub (now(), 1) AS BIGINT ), \"yyyy-MM\" ) AND ad_code IN ( SELECT t2.ad_code FROM df_mass_order_total t1 LEFT JOIN gemini.t_order_address t2 ON t1.order_address_id = t2.id WHERE t1.store_id = '00000000000000000000000000000030' AND t1.customer_isnew_flag != '-1' GROUP BY t2.ad_code ) GROUP BY store_id ) temp12 ) a2 WHERE a2.store_id = '00000000000000000000000000000030' ) A2 ON A1.flag = A2.flag UNION ALL SELECT num1_ad, num2_ad, num3_ad, num4_ad FROM ( SELECT 'flag' AS flag, num1_ad, num2_ad, num4_ad FROM ( SELECT row_number () over (ORDER BY temp11.count DESC) AS num1_ad, row_number () over ( ORDER BY temp11.order_amount DESC ) AS num2_ad, row_number () over ( ORDER BY temp11.customer_count DESC ) AS num4_ad, temp11.store_id FROM ( SELECT store_id, count(1) AS count, sum( gmv_price - IF ( return_label = '1', returned_amount, 0 )) AS order_amount, count(DISTINCT customer_id) AS customer_count FROM df_mass_order_total dmot LEFT JOIN gemini.t_order_address toa ON dmot.order_address_id = toa.id WHERE 1 = 1 AND ad_code IN ( SELECT t2.ad_code FROM df_mass_order_total t1 LEFT JOIN gemini.t_order_address t2 ON t1.order_address_id = t2.id WHERE t1.store_id = '00000000000000000000000000000030' GROUP BY t2.ad_code ) GROUP BY store_id ) temp11 ) a1 WHERE a1.store_id = '00000000000000000000000000000030' ) A1 LEFT JOIN ( SELECT 'flag' AS flag, num3_ad FROM ( SELECT row_number () over ( ORDER BY temp12.new_customer DESC ) AS num3_ad, temp12.store_id FROM ( SELECT store_id, count(DISTINCT customer_id) AS new_customer FROM df_mass_order_total dmot LEFT JOIN gemini.t_order_address toa ON dmot.order_address_id = toa.id WHERE 1 = 1 AND ad_code IN ( SELECT t2.ad_code FROM df_mass_order_total t1 LEFT JOIN gemini.t_order_address t2 ON t1.order_address_id = t2.id WHERE t1.store_id = '00000000000000000000000000000030' AND t1.customer_isnew_flag != '-1' GROUP BY t2.ad_code ) GROUP BY store_id ) temp12 ) a2 WHERE a2.store_id = '00000000000000000000000000000030' ) A2 ON A1.flag = A2.flag UNION ALL SELECT num1_ad, num2_ad, num3_ad, num4_ad FROM ( SELECT 'flag' AS flag, num1_ad, num2_ad, num4_ad FROM ( SELECT row_number () over (ORDER BY temp11.count DESC) AS num1_ad, row_number () over ( ORDER BY temp11.order_amount DESC ) AS num2_ad, row_number () over ( ORDER BY temp11.customer_count DESC ) AS num4_ad, temp11.store_id FROM ( SELECT store_id, count(1) AS count, sum( gmv_price - IF ( return_label = '1', returned_amount, 0 )) AS order_amount, count(DISTINCT customer_id) AS customer_count FROM df_mass_order_total dmot LEFT JOIN gemini.t_order_address toa ON dmot.order_address_id = toa.id WHERE 1 = 1 AND to_date (sign_time) = to_date (days_sub(now(), 1)) AND city_code IN ( SELECT t2.city_code FROM df_mass_order_total t1 LEFT JOIN gemini.t_order_address t2 ON t1.order_address_id = t2.id WHERE t1.store_id = '00000000000000000000000000000030' GROUP BY t2.city_code ) GROUP BY store_id ) temp11 ) a1 WHERE a1.store_id = '00000000000000000000000000000030' ) A1 LEFT JOIN ( SELECT 'flag' AS flag, num3_ad FROM ( SELECT row_number () over ( ORDER BY temp12.new_customer DESC ) AS num3_ad, temp12.store_id FROM ( SELECT store_id, count(DISTINCT customer_id) AS new_customer FROM df_mass_order_total dmot LEFT JOIN gemini.t_order_address toa ON dmot.order_address_id = toa.id WHERE 1 = 1 AND to_date (sign_time) = to_date (days_sub(now(), 1)) AND city_code IN ( SELECT t2.city_code FROM df_mass_order_total t1 LEFT JOIN gemini.t_order_address t2 ON t1.order_address_id = t2.id WHERE t1.store_id = '00000000000000000000000000000030' AND t1.customer_isnew_flag != '-1' GROUP BY t2.city_code ) GROUP BY store_id ) temp12 ) a2 WHERE a2.store_id = '00000000000000000000000000000030' ) A2 ON A1.flag = A2.flag UNION ALL SELECT num1_ad, num2_ad, num3_ad, num4_ad FROM ( SELECT 'flag' AS flag, num1_ad, num2_ad, num4_ad FROM ( SELECT row_number () over (ORDER BY temp11.count DESC) AS num1_ad, row_number () over ( ORDER BY temp11.order_amount DESC ) AS num2_ad, row_number () over ( ORDER BY temp11.customer_count DESC ) AS num4_ad, temp11.store_id FROM ( SELECT store_id, count(1) AS count, sum( gmv_price - IF ( return_label = '1', returned_amount, 0 )) AS order_amount, count(DISTINCT customer_id) AS customer_count FROM df_mass_order_total dmot LEFT JOIN gemini.t_order_address toa ON dmot.order_address_id = toa.id WHERE 1 = 1 AND substr(sign_time, 1, 7) = from_unixtime( cast( months_sub (now(), 1) AS BIGINT ), \"yyyy-MM\" ) AND city_code IN ( SELECT t2.city_code FROM df_mass_order_total t1 LEFT JOIN gemini.t_order_address t2 ON t1.order_address_id = t2.id WHERE t1.store_id = '00000000000000000000000000000030' GROUP BY t2.city_code ) GROUP BY store_id ) temp11 ) a1 WHERE a1.store_id = '00000000000000000000000000000030' ) A1 LEFT JOIN ( SELECT 'flag' AS flag, num3_ad FROM ( SELECT row_number () over ( ORDER BY temp12.new_customer DESC ) AS num3_ad, temp12.store_id FROM ( SELECT store_id, count(DISTINCT customer_id) AS new_customer FROM df_mass_order_total dmot LEFT JOIN gemini.t_order_address toa ON dmot.order_address_id = toa.id WHERE 1 = 1 AND substr(sign_time, 1, 7) = from_unixtime( cast( months_sub (now(), 1) AS BIGINT ), \"yyyy-MM\" ) AND city_code IN ( SELECT t2.city_code FROM df_mass_order_total t1 LEFT JOIN gemini.t_order_address t2 ON t1.order_address_id = t2.id WHERE t1.store_id = '00000000000000000000000000000030' AND t1.customer_isnew_flag != '-1' GROUP BY t2.city_code ) GROUP BY store_id ) temp12 ) a2 WHERE a2.store_id = '00000000000000000000000000000030' ) A2 ON A1.flag = A2.flag UNION ALL SELECT num1_ad, num2_ad, num3_ad, num4_ad FROM ( SELECT 'flag' AS flag, num1_ad, num2_ad, num4_ad FROM ( SELECT row_number () over (ORDER BY temp11.count DESC) AS num1_ad, row_number () over ( ORDER BY temp11.order_amount DESC ) AS num2_ad, row_number () over ( ORDER BY temp11.customer_count DESC ) AS num4_ad, temp11.store_id FROM ( SELECT store_id, count(1) AS count, sum( gmv_price - IF ( return_label = '1', returned_amount, 0 )) AS order_amount, count(DISTINCT customer_id) AS customer_count FROM df_mass_order_total dmot LEFT JOIN gemini.t_order_address toa ON dmot.order_address_id = toa.id WHERE 1 = 1 AND city_code IN ( SELECT t2.city_code FROM df_mass_order_total t1 LEFT JOIN gemini.t_order_address t2 ON t1.order_address_id = t2.id WHERE t1.store_id = '00000000000000000000000000000030' GROUP BY t2.city_code ) GROUP BY store_id ) temp11 ) a1 WHERE a1.store_id = '00000000000000000000000000000030' ) A1 LEFT JOIN ( SELECT 'flag' AS flag, num3_ad FROM ( SELECT row_number () over ( ORDER BY temp12.new_customer DESC ) AS num3_ad, temp12.store_id FROM ( SELECT store_id, count(DISTINCT customer_id) AS new_customer FROM df_mass_order_total dmot LEFT JOIN gemini.t_order_address toa ON dmot.order_address_id = toa.id WHERE 1 = 1 AND city_code IN ( SELECT t2.city_code FROM df_mass_order_total t1 LEFT JOIN gemini.t_order_address t2 ON t1.order_address_id = t2.id WHERE t1.store_id = '00000000000000000000000000000030' AND t1.customer_isnew_flag != '-1' GROUP BY t2.city_code ) GROUP BY store_id ) temp12 ) a2 WHERE a2.store_id = '00000000000000000000000000000030' ) A2 ON A1.flag = A2.flag UNION ALL SELECT num1_ad, num2_ad, num3_ad, num4_ad FROM ( SELECT 'flag' AS flag, num1_ad, num2_ad, num4_ad FROM ( SELECT row_number () over (ORDER BY temp11.count DESC) AS num1_ad, row_number () over ( ORDER BY temp11.order_amount DESC ) AS num2_ad, row_number () over ( ORDER BY temp11.customer_count DESC ) AS num4_ad, temp11.store_id FROM ( SELECT store_id, count(1) AS count, sum( gmv_price - IF ( return_label = '1', returned_amount, 0 )) AS order_amount, count(DISTINCT customer_id) AS customer_count FROM df_mass_order_total dmot WHERE 1 = 1 AND to_date (sign_time) = to_date (days_sub(now(), 1)) GROUP BY store_id ) temp11 ) a1 WHERE a1.store_id = '00000000000000000000000000000030' ) A1 LEFT JOIN ( SELECT 'flag' AS flag, num3_ad FROM ( SELECT row_number () over ( ORDER BY temp12.new_customer DESC ) AS num3_ad, temp12.store_id FROM ( SELECT store_id, count(DISTINCT customer_id) AS new_customer FROM df_mass_order_total dmot WHERE 1 = 1 AND to_date (sign_time) = to_date (days_sub(now(), 1)) GROUP BY store_id ) temp12 ) a2 WHERE a2.store_id = '00000000000000000000000000000030' ) A2 ON A1.flag = A2.flag UNION ALL SELECT num1_ad, num2_ad, num3_ad, num4_ad FROM ( SELECT 'flag' AS flag, num1_ad, num2_ad, num4_ad FROM ( SELECT row_number () over (ORDER BY temp11.count DESC) AS num1_ad, row_number () over ( ORDER BY temp11.order_amount DESC ) AS num2_ad, row_number () over ( ORDER BY temp11.customer_count DESC ) AS num4_ad, temp11.store_id FROM ( SELECT store_id, count(1) AS count, sum( gmv_price - IF ( return_label = '1', returned_amount, 0 )) AS order_amount, count(DISTINCT customer_id) AS customer_count FROM df_mass_order_total dmot WHERE 1 = 1 AND substr(sign_time, 1, 7) = from_unixtime( cast( months_sub (now(), 1) AS BIGINT ), \"yyyy-MM\" ) GROUP BY store_id ) temp11 ) a1 WHERE a1.store_id = '00000000000000000000000000000030' ) A1 LEFT JOIN ( SELECT 'flag' AS flag, num3_ad FROM ( SELECT row_number () over ( ORDER BY temp12.new_customer DESC ) AS num3_ad, temp12.store_id FROM ( SELECT store_id, count(DISTINCT customer_id) AS new_customer FROM df_mass_order_total dmot WHERE 1 = 1 AND substr(sign_time, 1, 7) = from_unixtime( cast( months_sub (now(), 1) AS BIGINT ), \"yyyy-MM\" ) GROUP BY store_id ) temp12 ) a2 WHERE a2.store_id = '00000000000000000000000000000030' ) A2 ON A1.flag = A2.flag UNION ALL SELECT num1_ad, num2_ad, num3_ad, num4_ad FROM ( SELECT 'flag' AS flag, num1_ad, num2_ad, num4_ad FROM ( SELECT row_number () over (ORDER BY temp11.count DESC) AS num1_ad, row_number () over ( ORDER BY temp11.order_amount DESC ) AS num2_ad, row_number () over ( ORDER BY temp11.customer_count DESC ) AS num4_ad, temp11.store_id FROM ( SELECT store_id, count(1) AS count, sum( gmv_price - IF ( return_label = '1', returned_amount, 0 )) AS order_amount, count(DISTINCT customer_id) AS customer_count FROM df_mass_order_total dmot WHERE 1 = 1 GROUP BY store_id ) temp11 ) a1 WHERE a1.store_id = '00000000000000000000000000000030' ) A1 LEFT JOIN ( SELECT 'flag' AS flag, num3_ad FROM ( SELECT row_number () over ( ORDER BY temp12.new_customer DESC ) AS num3_ad, temp12.store_id FROM ( SELECT store_id, count(DISTINCT customer_id) AS new_customer FROM df_mass_order_total dmot WHERE 1 = 1 GROUP BY store_id ) temp12 ) a2 WHERE a2.store_id = '00000000000000000000000000000030' ) A2 ON A1.flag = A2.flag";
		List<Map<String,Object>> list2 = ImpalaUtil.execute(sql2);
		list0.addAll(list2);
		
		return list0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryrecommenduserlist(QueryConditions queryConditions) {
		// TODO Auto-generated method stub
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		//Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		//Map<String, Object> order_obj = null;
		//List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		/*if (lst_data != null && lst_data.size() > 0) {
			order_obj = (Map<String, Object>) lst_data.get(0);
		}*/
		String platformid = null;
		PageInfo pageInfo = queryConditions.getPageinfo();
		int recordsPerPage = pageInfo.getRecordsPerPage();
		for(Map<String, Object> map : queryConditions.getConditions()){
			if("platformid".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				platformid = map.get("value").toString();
			}
		}
		
		String sqlDemo = "SELECT a.customerId, but.mobilephone, but.area_no, but.employee_a_no, COALESCE (b.item1, '') item1, COALESCE (c.item2, '') item2, COALESCE (d.item3, '') item3 FROM ( SELECT DISTINCT a.customer_id customerId, COALESCE ( split_part (b.tag_level4_id, ',', 1), '' ) item1, COALESCE ( substring( split_part (b.tag_level4_id, ',', 2), locate( ',', split_part (b.tag_level4_id, ',', 2)) + 1 ), '' ) item2, COALESCE ( substring( split_part (b.tag_level4_id, ',', 3), locate( split_part ( split_part (b.tag_level4_id, ',', 3), ',', 2 ), split_part (b.tag_level4_id, ',', 3)) + length( split_part ( split_part (b.tag_level4_id, ',', 3), ',', 2 )) + 1 ), '' ) item3 FROM t_statis_cust_operate_classify a LEFT JOIN t_recomm_num_result b ON a.customer_id = b.customer_id WHERE 1 = 1 AND level_num = 1 ORDER BY a.customer_id ) a LEFT JOIN ( SELECT tag_level4_id id, tag_level4_name item1 FROM f_skuclass GROUP BY tag_level4_id, tag_level4_name ) b ON COALESCE (a.item1, '$') = b.id LEFT JOIN ( SELECT tag_level4_id id, tag_level4_name item2 FROM f_skuclass GROUP BY tag_level4_id, tag_level4_name ) c ON COALESCE (a.item2, '$') = c.id LEFT JOIN ( SELECT tag_level4_id id, tag_level4_name item3 FROM f_skuclass GROUP BY tag_level4_id, tag_level4_name ) d ON COALESCE (a.item3, '$') = d.id LEFT JOIN b_user_tiny but ON a.customerId = but.customer_id where but.platformid like '" +platformid+ "' order by a.customerId";
		List<Map<String,Object>> list = ImpalaUtil.executeGuoan(sqlDemo);
		pageInfo.setTotalRecords(list.size());
		List<Map<String,Object>> listDemo = list.subList(pageInfo.getRecordsPerPage()*(pageInfo.getCurrentPage()-1), pageInfo.getRecordsPerPage()*pageInfo.getCurrentPage());
		
//		pageInfo.setTotalRecords();
		/*List<Map<String,Object>> tr = ImpalaUtil.executeGuoan("select count(1) as  totalRecords from ("+sqlDemo+") temp");
		Object totalrecords = tr.get(0).get("totalrecords");
		pageInfo.setTotalRecords((Integer) totalrecords);
		int sdfa = pageInfo.getRecordsPerPage();
		int sdfasd = pageInfo.getRecordsPerPage()*(pageInfo.getCurrentPage()-1);
		String sql = sqlDemo+"  order by a.customerId  limit "+pageInfo.getRecordsPerPage()+" offset "+ pageInfo.getRecordsPerPage()*(pageInfo.getCurrentPage()-1);
		List<Map<String,Object>> list = ImpalaUtil.executeGuoan(sql);*/
		List<UserRecommendInfo> lst_data = new ArrayList();
		for(int i=0;i<listDemo.size();i++) {
			Map<String,Object> temp = listDemo.get(i);
			UserRecommendInfo uri = new UserRecommendInfo();
			uri.setCustomerId((String) temp.get("customerId"));
			StringBuilder  mobilephone = new StringBuilder((String) temp.get("mobilephone"));
			String mobilephoneShow = "<a href=\"javascript:;\" onmouseover=\"showinfo('<strong>用户电话：</strong>"+mobilephone+"<br>@customertips"+i%recordsPerPage+"');\" id=\"customertips"+i%recordsPerPage+"\">"+mobilephone.replace(3, 7, "****").toString()+"</a>";
			//mobilephone.replace(3, 7, "****");
			uri.setMobilephone(mobilephoneShow);
			uri.setArea_no((String) temp.get("area_no"));
			uri.setEmployee_a_no((String) temp.get("employee_a_no"));
			uri.setItem1((String) temp.get("item1"));
			uri.setItem2((String) temp.get("item2"));
			uri.setItem3((String) temp.get("item3"));
			lst_data.add(uri);
		}
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", lst_data);
		return returnMap;
	}
	
}
