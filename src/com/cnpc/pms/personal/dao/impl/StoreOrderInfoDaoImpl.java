package com.cnpc.pms.personal.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.StoreOrderInfoDao;

public class StoreOrderInfoDaoImpl extends BaseDAOHibernate implements StoreOrderInfoDao{

	
	@Override
	public Map<String, Object> queryStoreOrderInfoListApp(PageInfo pageInfo,String employee_no,String types,String inputnum){
		String sqlwhere=" where 1=1 ";
		if(employee_no!=null&&employee_no.length()>0){
			sqlwhere+=" and employee_no = '"+employee_no+"'";
		}else{
			sqlwhere+=" and 1=0 ";
		}
		if(types!=null&&types.length()>0){
			sqlwhere+=" and worder_status in("+types+")";
		}else{
			sqlwhere+=" and 1=0 ";
		}
		if(inputnum!=null&&inputnum.length()>0){
			sqlwhere+=" AND (RIGHT(worder_sn,4)='"+inputnum+"' OR RIGHT(phone,4)='"+inputnum+"') ";
		}
		
		sqlwhere+=" order by worder_status asc,id desc  ";
		
		String sql = "SELECT * FROM t_storeorder_info "+sqlwhere;
		String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql_count);
		List<?> total = query_count
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		Map<String, Object> maps = (Map<String, Object>) total.get(0);
		pageInfo.setTotalRecords(Integer.valueOf(maps.get("total").toString()));
		Query query = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);
		List<?> list = query
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(
						pageInfo.getRecordsPerPage()
								* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		Map<String, Object> map_result = new HashMap<String, Object>();
		Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("data", list);
		map_result.put("total_pages", total_pages);
		return map_result;
	}
	
	@Override
	public Map<String, Object> queryStoreOrderInfoListByPhone(PageInfo pageInfo,String phone,String inputnum){
		String sqlwhere=" where 1=1 ";
		if(phone!=null&&phone.length()>0){
			sqlwhere +=" and phone ='"+phone+"' ";
		}
		if(inputnum!=null&&inputnum.length()>0){
			sqlwhere+=" AND (RIGHT(worder_sn,4)='"+inputnum+"' OR RIGHT(phone,4)='"+inputnum+"') ";
		}
		
		sqlwhere+=" order by worder_status asc,id desc  ";
		
		String sql = "SELECT * FROM t_storeorder_info "+sqlwhere;
		String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";
		Query query_count = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql_count);
		List<?> total = query_count
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		Map<String, Object> maps = (Map<String, Object>) total.get(0);
		pageInfo.setTotalRecords(Integer.valueOf(maps.get("total").toString()));
		Query query = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);
		List<?> list = query
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(
						pageInfo.getRecordsPerPage()
								* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		Map<String, Object> map_result = new HashMap<String, Object>();
		Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("data", list);
		map_result.put("total_pages", total_pages);
		return map_result;
	}
	

}
