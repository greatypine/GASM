package com.cnpc.pms.platform.dao.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dto.StoreDTO;
import com.cnpc.pms.personal.entity.YyStore;
import com.cnpc.pms.platform.dao.PlatformStoreDao;
import com.cnpc.pms.platform.entity.PlatformStore;

public class PlatformStoreDaoImpl extends DAORootHibernate implements PlatformStoreDao{

	@Override
	public List<PlatformStore> getPlatformStoreList(String where, PageInfo pageInfo) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from t_store where 1=1 ");
		sql.append(where);
		Session session = getHibernateTemplate().getSessionFactory().openSession();
        Map<String,Object> map_result = new HashMap<String,Object>();
        List<PlatformStore> lst_data = null;
        try{
            SQLQuery query = session.createSQLQuery(sql.toString());
            query.addEntity(PlatformStore.class);
            lst_data = query
                    .setFirstResult(
                            pageInfo.getRecordsPerPage()
                                    * (pageInfo.getCurrentPage() - 1))
                    .setMaxResults(pageInfo.getRecordsPerPage()).list();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        /*if(lst_data == null){
            lst_data = new ArrayList<Map<String,Object>>();
        }
        map_result.put("pageinfo", pageInfo);
        map_result.put("header", "");
        map_result.put("data", lst_data);*/
        return lst_data;
		
		
		
		
		
		/*SQLQuery query = getHibernateTemplate().getSessionFactory()
				.openSession().createSQLQuery(sql.toString());
		query.addEntity(StoreDTO.class);
		List<StoreDTO> list = query.list();
		pageInfo.setTotalRecords(list.size());
		List<StoreDTO> returnList = query.setFirstResult(
				pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		return returnList;*/
	}
	
	
	
	
	
	@Override
	public Map<String, Object> getPlatformStoreInfoList(String where, PageInfo pageInfo) {
		 String sql_count="select count(1) from t_store where 1=1 "+where;
		 String find_sql="SELECT id,name from t_store where 1=1 "+where;
	        Session session = getHibernateTemplate().getSessionFactory().openSession();
	        Map<String,Object> map_result = new HashMap<String,Object>();
	        List<?> lst_data = null;
	        try{
	            SQLQuery countQuery = session.createSQLQuery(sql_count);
	            pageInfo.setTotalRecords(Integer.valueOf(countQuery.list().get(0).toString()));

	            SQLQuery query = session.createSQLQuery(find_sql);
	            lst_data = query
	                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
	                    .setFirstResult(
	                            pageInfo.getRecordsPerPage()
	                                    * (pageInfo.getCurrentPage() - 1))
	                    .setMaxResults(pageInfo.getRecordsPerPage()).list();
	        }catch (Exception e){
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
	        if(lst_data == null){
	            lst_data = new ArrayList<Map<String,Object>>();
	        }
	        map_result.put("pageinfo", pageInfo);
	        map_result.put("header", "平台门店列表");
	        map_result.put("data", lst_data);
	        return map_result;
	}





	@Override
	public int getPlatformStoreCount(String where) {
		int platformcount=0;
		String sql_count="select count(1) from t_store where 1=1 "+where;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		 try{
	            SQLQuery countQuery = session.createSQLQuery(sql_count);
	            platformcount= Integer.valueOf(countQuery.list().get(0).toString());
	        }catch (Exception e){
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
		return platformcount;
	}





	@Override
	public List<Map<String, Object>> getEmployeeByStore(String storeNo,Boolean isRealtime) {
		String sql = "";
		if(isRealtime){
			sql = " and te.online = 'online' and ts.`code` = '"+storeNo+"'";
		}else{
			sql = "and te.online is not null and ts.`code` = '"+storeNo+"'";
		}
		String str_sql = "select concat(te.id,'') as employeeId,CONCAT(te.store_id,'') as platformid,te.name as employeeName,ts.name as storeName,te.code as employeeNo,te.online as online from t_employee te INNER JOIN t_store ts ON ts.id = te.store_id where te.status = 0 "+sql;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
        List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
        try{
            SQLQuery query = session.createSQLQuery(str_sql);
            List<Map<String,Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            lst_result = lst_data;
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
		return lst_result;
	}





	@Override
	public List<Map<String, Object>> getEmployeeByEmployeeNo(String employeeNo,Boolean isRealtime) {
		String sql = "";
		if(isRealtime){
			sql = " and te.online = 'online' and te.CODE = '"+employeeNo+"'";
		}else{
			sql = " and te.CODE = '"+employeeNo+"'";
		}
		String str_sql = "select concat(te.id,'') as employeeId,te.name as employeeName,ts.name as storeName,te.code as employeeNo,te.`online` as online from t_employee te INNER JOIN t_store ts ON ts.id = te.store_id where te.status = 0"+sql;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
        List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
        try{
            SQLQuery query = session.createSQLQuery(str_sql);
            List<Map<String,Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            lst_result = lst_data;
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
		return lst_result;
	}





	@Override
	public Map<String,Object> getEmployeeStatus(String employeeNo) {
		String str_sql = "select `online` from t_employee where status = 0 and code = '"+employeeNo+"'";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		List<Map<String,Object>> lst_data = new ArrayList<Map<String,Object>>();
        try{
            SQLQuery query = session.createSQLQuery(str_sql);
            lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return lst_data.size()>0?lst_data.get(0):null;
	}





	@Override
	public List<Map<String, Object>> getEmployeeByCity(String city_code, Boolean isRealtime) {
		String str_sql = "";
		if(isRealtime){
			str_sql = " and ts.city_code ='"+city_code+"' and te.online = 'online'";
		}else{
			str_sql = " and ts.city_code ='"+city_code+"' and te.online is not null";
		}
		String sql = "select concat(te.id,'') as employeeId,CONCAT(te.store_id,'') as platformid,te.name as employeeName,ts.name as storeName,te.code as employeeNo,te.online as online,te.mobilephone as mobilephone from t_employee te "
				+"INNER JOIN t_store ts ON ts.id = te.store_id where  te.status = 0 and ts.name NOT LIKE '%测试%' and ts.white!='QA'"+str_sql;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
        List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
        try{
            SQLQuery query = session.createSQLQuery(sql);
            List<Map<String,Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            lst_result = lst_data;
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
		return lst_result;
	}

}
