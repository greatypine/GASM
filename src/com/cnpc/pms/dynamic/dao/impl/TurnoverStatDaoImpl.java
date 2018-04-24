package com.cnpc.pms.dynamic.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.dao.TurnoverStatDao;
import com.cnpc.pms.dynamic.entity.MassOrderDto;
import com.cnpc.pms.dynamic.entity.TurnoverStatDto;

public class TurnoverStatDaoImpl extends BaseDAOHibernate implements TurnoverStatDao {

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryStoreStat(TurnoverStatDto storeStatDto,PageInfo pageInfo,String timeFlag){
		String sql = "SELECT a.store_city_name AS city_name, a.store_name,	a.store_code, IFNULL(sum(a.gmv_price), 0) AS gmv_price,"
				+ "IFNULL(SUM( CASE WHEN loan_label !='4' THEN	 gmv_price ELSE 0 END ),0) AS gmv_price_profit, IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN returned_amount ELSE 0 END ),0) AS return_price,IFNULL(SUM(CASE WHEN (return_label='1' and loan_label !='4') THEN returned_amount ELSE 0 END ),0) "
				+ "AS return_price_profit,count(1) AS order_num,IFNULL(SUM( CASE WHEN loan_label !='4' THEN 1 ELSE 0 END ),0) AS order_num_profit,IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN 1 ELSE 0 END ),0) AS return_num,IFNULL(SUM( CASE WHEN (return_label='1' and loan_label !='4')THEN 1 ELSE 0 END ),0) AS return_num_profit FROM ";

		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_monthly a ";
		} else {
			sql = sql + " df_mass_order_total a ";
		}

		sql = sql + " where a.store_white!='QA' AND a.store_status=0 AND a.store_name NOT LIKE '%测试%' and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' ";
		
		if (StringUtils.isNotEmpty(storeStatDto.getBeginDate())) {
			sql = sql + " and (a.sign_time between '" + storeStatDto.getBeginDate() + " 00:00:00' and '"
					+ storeStatDto.getEndDate() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getCityName())){
			sql = sql + " and a.store_city_name like '%" + storeStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getStoreNo())){
			sql = sql + " and a.store_code ='" + storeStatDto.getStoreNo().trim()+ "'";
		}
		
		sql = sql + " GROUP BY a.store_code ";

		String sql_count = "SELECT COUNT(1) as total FROM (" + sql + ") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sql_count);
		Object total = query_count.uniqueResult();
		pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

		int startData = (pageInfo.getCurrentPage() - 1) * pageInfo.getRecordsPerPage();
		int recordsPerPage = pageInfo.getRecordsPerPage();
		sql = sql + " LIMIT " + startData + "," + recordsPerPage;

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		Map<String, Object> map_result = new HashMap<String, Object>();
		Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("data", list);
		map_result.put("total_pages", total_pages);
		return map_result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> exportStoreStat(TurnoverStatDto storeStatDto,String timeFlag){
		String sql = "SELECT a.store_city_name AS city_name, a.store_name,	a.store_code, IFNULL(sum(a.gmv_price), 0) AS gmv_price,"
				+ "IFNULL(SUM( CASE WHEN loan_label !='4' THEN	 gmv_price ELSE 0 END ),0) AS gmv_price_profit, IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN returned_amount ELSE 0 END ),0) AS return_price,IFNULL(SUM(CASE WHEN (return_label='1' and loan_label !='4') THEN returned_amount ELSE 0 END ),0) "
				+ "AS return_price_profit,count(1) AS order_num,IFNULL(SUM( CASE WHEN loan_label !='4' THEN 1 ELSE 0 END ),0) AS order_num_profit,IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN 1 ELSE 0 END),0) AS return_num,IFNULL(SUM( CASE WHEN (return_label='1' and loan_label !='4')THEN 1 ELSE 0 END ),0) AS return_num_profit FROM ";

		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_monthly a ";
		} else {
			sql = sql + " df_mass_order_total a ";
		}

		sql = sql + " where a.store_white!='QA' AND a.store_status=0 AND a.store_name NOT LIKE '%测试%' and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' ";
		
		if (StringUtils.isNotEmpty(storeStatDto.getBeginDate())) {
			sql = sql + " and (a.sign_time between '" + storeStatDto.getBeginDate() + " 00:00:00' and '"
					+ storeStatDto.getEndDate() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getCityName())){
			sql = sql + " and a.store_city_name like '%" + storeStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getStoreNo())){
			sql = sql + " and a.store_code ='" + storeStatDto.getStoreNo().trim()+ "'";
		}
		
		sql = sql + " GROUP BY a.store_code ";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object>  queryAreaStat(TurnoverStatDto storeStatDto,PageInfo pageInfo,String timeFlag){
		String sql = "SELECT a.store_city_name AS city_name, a.store_name,  IFNULL(a.area_code,'') as area_code, IFNULL(a.info_employee_a_no,'') as employee_a_no, "
				+ "IFNULL(sum(a.gmv_price), 0) AS gmv_price,IFNULL(SUM(CASE WHEN loan_label !='4' THEN gmv_price ELSE 0 END),0) AS gmv_price_profit,IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN returned_amount ELSE 0 END ),0) AS return_price,IFNULL(SUM(CASE WHEN (return_label='1' and loan_label !='4') THEN returned_amount ELSE 0 END),0) "
				+ "AS return_price_profit,count(1) AS order_num,IFNULL(SUM( CASE WHEN loan_label !='4' THEN 1 ELSE 0 END),0) AS order_num_profit,IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN 1 ELSE 0 END),0) AS return_num,IFNULL(SUM( CASE WHEN (return_label='1' and loan_label !='4')THEN 1 ELSE 0 END),0) AS return_num_profit FROM ";
		
		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_monthly a ";
		} else {
			sql = sql + " df_mass_order_total a ";
		}

		sql = sql + " where area_code is not null AND a.store_white!='QA' AND a.store_status=0 AND a.store_name NOT LIKE '%测试%' and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' ";
		
		if (StringUtils.isNotEmpty(storeStatDto.getBeginDate())) {
			sql = sql + " and (a.sign_time between '" + storeStatDto.getBeginDate() + " 00:00:00' and '"
					+ storeStatDto.getEndDate() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getCityName())){
			sql = sql + " and a.store_city_name like '%" + storeStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getStoreNo())){
			sql = sql + " and a.store_code ='" + storeStatDto.getStoreNo().trim()+ "'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getAreaNo())){
			sql = sql + " and a.area_code ='" + storeStatDto.getAreaNo().trim()+ "'";
		}
		
		sql = sql + " GROUP BY a.store_code,a.area_code ";

		String sql_count = "SELECT COUNT(1) as total FROM (" + sql + ") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sql_count);
		Object total = query_count.uniqueResult();
		pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

		int startData = (pageInfo.getCurrentPage() - 1) * pageInfo.getRecordsPerPage();
		int recordsPerPage = pageInfo.getRecordsPerPage();
		sql = sql + " LIMIT " + startData + "," + recordsPerPage;

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		Map<String, Object> map_result = new HashMap<String, Object>();
		Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("data", list);
		map_result.put("total_pages", total_pages);
		return map_result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> exportAreaStat(TurnoverStatDto storeStatDto,String timeFlag){
		String sql = "SELECT a.store_city_name AS city_name, a.store_name,  IFNULL(a.area_code,'') as area_code, IFNULL(a.info_employee_a_no,'') as employee_a_no, "
				+ "IFNULL(sum(a.gmv_price), 0) AS gmv_price,IFNULL(SUM(CASE WHEN loan_label !='4' THEN gmv_price ELSE 0 END),0) AS gmv_price_profit,IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN returned_amount ELSE 0 END ),0) AS return_price,IFNULL(SUM(CASE WHEN (return_label='1' and loan_label !='4') THEN returned_amount ELSE 0 END),0) "
				+ "AS return_price_profit,count(1) AS order_num,IFNULL(SUM( CASE WHEN loan_label !='4' THEN 1 ELSE 0 END),0) AS order_num_profit,IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN 1 ELSE 0 END),0) AS return_num,IFNULL(SUM( CASE WHEN (return_label='1' and loan_label !='4')THEN 1 ELSE 0 END),0) AS return_num_profit FROM ";
		
		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_monthly a ";
		} else {
			sql = sql + " df_mass_order_total a ";
		}

		sql = sql + " where area_code is not null AND a.store_white!='QA' AND a.store_status=0 AND a.store_name NOT LIKE '%测试%' and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA'  ";
		
		if (StringUtils.isNotEmpty(storeStatDto.getBeginDate())) {
			sql = sql + " and (a.sign_time between '" + storeStatDto.getBeginDate() + " 00:00:00' and '"
					+ storeStatDto.getEndDate() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getCityName())){
			sql = sql + " and a.store_city_name like '%" + storeStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getStoreNo())){
			sql = sql + " and a.store_code ='" + storeStatDto.getStoreNo().trim()+ "'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getAreaNo())){
			sql = sql + " and a.area_code ='" + storeStatDto.getAreaNo().trim()+ "'";
		}
		
		sql = sql + " GROUP BY a.store_code,a.area_code ";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryDeptStat(TurnoverStatDto storeStatDto,PageInfo pageInfo,String timeFlag){
		String sql = "SELECT a.store_city_name AS city_name, a.store_name,	a.store_code, IFNULL(a.department_name,'') as department_name, IFNULL(sum(a.gmv_price), 0) AS gmv_price,"
				+ "IFNULL(SUM( CASE WHEN return_label='1' THEN returned_amount ELSE 0 END ),0) AS return_price,count(1) AS order_num,IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN 1 ELSE 0 END ),0) AS return_num FROM ";

		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_monthly a ";
		} else {
			sql = sql + " df_mass_order_total a ";
		}

		sql = sql + " where a.store_white!='QA' AND a.store_status=0 AND a.store_name NOT LIKE '%测试%' and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' ";
		
		if (StringUtils.isNotEmpty(storeStatDto.getBeginDate())) {
			sql = sql + " and (a.sign_time between '" + storeStatDto.getBeginDate() + " 00:00:00' and '"
					+ storeStatDto.getEndDate() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getCityName())){
			sql = sql + " and a.store_city_name like '%" + storeStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getStoreNo())){
			sql = sql + " and a.store_code ='" + storeStatDto.getStoreNo().trim()+ "'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getDeptName())){
			sql = sql + " and a.department_name like '%" + storeStatDto.getDeptName().trim() + "%'";
		}
		
		sql = sql + " GROUP BY a.store_code,a.department_id ";

		String sql_count = "SELECT COUNT(1) as total FROM (" + sql + ") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sql_count);
		Object total = query_count.uniqueResult();
		pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

		int startData = (pageInfo.getCurrentPage() - 1) * pageInfo.getRecordsPerPage();
		int recordsPerPage = pageInfo.getRecordsPerPage();
		sql = sql + " LIMIT " + startData + "," + recordsPerPage;

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		Map<String, Object> map_result = new HashMap<String, Object>();
		Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("data", list);
		map_result.put("total_pages", total_pages);
		return map_result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> exportDeptStat(TurnoverStatDto storeStatDto,String timeFlag){
		String sql = "SELECT a.store_city_name AS city_name, a.store_name,	a.store_code, IFNULL(a.department_name,'') as department_name, IFNULL(sum(a.gmv_price), 0) AS gmv_price,"
				+ "IFNULL(SUM( CASE WHEN return_label='1' THEN returned_amount ELSE 0 END),0) AS return_price,count(1) AS order_num,IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN 1 ELSE 0 END),0) AS return_num FROM ";

		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_monthly a ";
		} else {
			sql = sql + " df_mass_order_total a ";
		}

		sql = sql + " where a.store_white!='QA' AND a.store_status=0 AND a.store_name NOT LIKE '%测试%' and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' ";
		
		if (StringUtils.isNotEmpty(storeStatDto.getBeginDate())) {
			sql = sql + " and (a.sign_time between '" + storeStatDto.getBeginDate() + " 00:00:00' and '"
					+ storeStatDto.getEndDate() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getCityName())){
			sql = sql + " and a.store_city_name like '%" + storeStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getStoreNo())){
			sql = sql + " and a.store_code ='" + storeStatDto.getStoreNo().trim()+ "'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getDeptName())){
			sql = sql + " and a.department_name like '%" + storeStatDto.getDeptName().trim() + "%'";
		}
		
		sql = sql + " GROUP BY a.store_code,a.department_id ";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryChannelStat(TurnoverStatDto storeStatDto,PageInfo pageInfo,String timeFlag){
		String sql = "SELECT a.store_city_name AS city_name, a.store_name,	a.store_code, IFNULL(a.channel_name,'') as channel_name, IFNULL(sum(a.gmv_price), 0) AS gmv_price,"
				+ "IFNULL(SUM( CASE WHEN return_label='1' THEN returned_amount ELSE 0 END),0) AS return_price,count(1) AS order_num,IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN 1 ELSE 0 END),0) AS return_num FROM ";

		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_monthly a ";
		} else {
			sql = sql + " df_mass_order_total a ";
		}

		sql = sql + " where a.store_white!='QA' AND a.store_status=0 AND a.store_name NOT LIKE '%测试%' and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' ";
		
		if (StringUtils.isNotEmpty(storeStatDto.getBeginDate())) {
			sql = sql + " and (a.sign_time between '" + storeStatDto.getBeginDate() + " 00:00:00' and '"
					+ storeStatDto.getEndDate() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getCityName())){
			sql = sql + " and a.store_city_name like '%" + storeStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getStoreNo())){
			sql = sql + " and a.store_code ='" + storeStatDto.getStoreNo().trim()+ "'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getChannelName())){
			sql = sql + " and a.channel_name like '%" + storeStatDto.getChannelName().trim() + "%'";
		}
		
		sql = sql + " GROUP BY a.store_code,a.channel_id ";

		String sql_count = "SELECT COUNT(1) as total FROM (" + sql + ") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sql_count);
		Object total = query_count.uniqueResult();
		pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

		int startData = (pageInfo.getCurrentPage() - 1) * pageInfo.getRecordsPerPage();
		int recordsPerPage = pageInfo.getRecordsPerPage();
		sql = sql + " LIMIT " + startData + "," + recordsPerPage;

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		Map<String, Object> map_result = new HashMap<String, Object>();
		Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("data", list);
		map_result.put("total_pages", total_pages);
		return map_result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> exportChannelStat(TurnoverStatDto storeStatDto,String timeFlag){
		String sql = "SELECT a.store_city_name AS city_name, a.store_name,	a.store_code, IFNULL(a.channel_name,'') as channel_name, IFNULL(sum(a.gmv_price), 0) AS gmv_price,"
				+ "IFNULL(SUM( CASE WHEN return_label='1' THEN returned_amount ELSE 0 END),0) AS return_price,count(1) AS order_num,IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN 1 ELSE 0 END),0) AS return_num FROM ";

		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_monthly a ";
		} else {
			sql = sql + " df_mass_order_total a ";
		}

		sql = sql + " where a.store_white!='QA' AND a.store_status=0 AND a.store_name NOT LIKE '%测试%' and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' ";
		
		if (StringUtils.isNotEmpty(storeStatDto.getBeginDate())) {
			sql = sql + " and (a.sign_time between '" + storeStatDto.getBeginDate() + " 00:00:00' and '"
					+ storeStatDto.getEndDate() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getCityName())){
			sql = sql + " and a.store_city_name like '%" + storeStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getStoreNo())){
			sql = sql + " and a.store_code ='" + storeStatDto.getStoreNo().trim()+ "'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getChannelName())){
			sql = sql + " and a.channel_name like '%" + storeStatDto.getChannelName().trim() + "%'";
		}
		
		sql = sql + " GROUP BY a.store_code,a.channel_id ";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryEshopStat(TurnoverStatDto storeStatDto,PageInfo pageInfo,String timeFlag){
		String sql = "SELECT a.store_city_name AS city_name, a.store_name,	a.store_code, IFNULL(a.eshop_name,'') as eshop_name, IFNULL(sum(a.gmv_price), 0) AS gmv_price,"
				+ "IFNULL(SUM( CASE WHEN return_label='1' THEN returned_amount ELSE 0 END),0) AS return_price,count(1) AS order_num,IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN 1 ELSE 0 END),0) AS return_num FROM ";

		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_monthly a ";
		} else {
			sql = sql + " df_mass_order_total a ";
		}

		sql = sql + " where a.store_white!='QA' AND a.store_status=0 AND a.store_name NOT LIKE '%测试%' and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' ";
		
		if (StringUtils.isNotEmpty(storeStatDto.getBeginDate())) {
			sql = sql + " and (a.sign_time between '" + storeStatDto.getBeginDate() + " 00:00:00' and '"
					+ storeStatDto.getEndDate() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getCityName())){
			sql = sql + " and a.store_city_name like '%" + storeStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getStoreNo())){
			sql = sql + " and a.store_code ='" + storeStatDto.getStoreNo().trim()+ "'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getEshopName())){
			sql = sql + " and a.eshop_name like '%" + storeStatDto.getEshopName().trim() + "%'";
		}
		
		sql = sql + " GROUP BY a.store_code,a.eshop_id ";

		String sql_count = "SELECT COUNT(1) as total FROM (" + sql + ") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sql_count);
		Object total = query_count.uniqueResult();
		pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

		int startData = (pageInfo.getCurrentPage() - 1) * pageInfo.getRecordsPerPage();
		int recordsPerPage = pageInfo.getRecordsPerPage();
		sql = sql + " LIMIT " + startData + "," + recordsPerPage;

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		Map<String, Object> map_result = new HashMap<String, Object>();
		Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("data", list);
		map_result.put("total_pages", total_pages);
		return map_result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> exportEshopStat(TurnoverStatDto storeStatDto,String timeFlag){
		String sql = "SELECT a.store_city_name AS city_name, a.store_name,	a.store_code, IFNULL(a.eshop_name,'') as eshop_name, IFNULL(sum(a.gmv_price), 0) AS gmv_price,"
				+ "IFNULL(SUM( CASE WHEN return_label='1' THEN returned_amount ELSE 0 END),0) AS return_price,count(1) AS order_num,IFNULL(SUM( CASE WHEN return_label='1' "
				+ "THEN 1 ELSE 0 END),0) AS return_num FROM ";

		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_monthly a ";
		} else {
			sql = sql + " df_mass_order_total a ";
		}

		sql = sql + " where a.store_white!='QA' AND a.store_status=0 AND a.store_name NOT LIKE '%测试%' and a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' ";
		
		if (StringUtils.isNotEmpty(storeStatDto.getBeginDate())) {
			sql = sql + " and (a.sign_time between '" + storeStatDto.getBeginDate() + " 00:00:00' and '"
					+ storeStatDto.getEndDate() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getCityName())){
			sql = sql + " and a.store_city_name like '%" + storeStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getStoreNo())){
			sql = sql + " and a.store_code ='" + storeStatDto.getStoreNo().trim()+ "'";
		}
		if(StringUtils.isNotEmpty(storeStatDto.getEshopName())){
			sql = sql + " and a.eshop_name like '%" + storeStatDto.getEshopName().trim() + "%'";
		}
		
		sql = sql + " GROUP BY a.store_code,a.eshop_id ";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryAreaByCode(String area_code) {
		String sql = "SELECT IFNULL(ta.`name`,'') AS area_name FROM t_area ta WHERE 1=1 ";

		if (StringUtils.isNotEmpty(area_code)) {
			sql = sql + " AND ta.area_no= '" + area_code + "'";
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
	public Map<String, Object> queryEmployeeByNO(String employee_no) {
		String sql = "SELECT IFNULL(th.`name`,'') AS employee_a_name, IFNULL(th.phone,'') as employee_a_phone FROM t_humanresources th WHERE 1 = 1 ";

		if (StringUtils.isNotEmpty(employee_no)) {
			sql = sql + " AND th.employee_no = '" + employee_no + "'";
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
