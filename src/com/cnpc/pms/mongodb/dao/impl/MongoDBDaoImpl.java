/**
 * gaobaolei
 */
package com.cnpc.pms.mongodb.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.mongodb.dao.MongoDBDao;
import com.google.inject.internal.Join;

/**
 * @author gaobaolei
 *
 */
public class MongoDBDaoImpl extends BaseDAOHibernate implements MongoDBDao {

	
	@Override
	public List<Map<String, Object>> queryAllTinyVillageByStore(Long storeId) {
		String sql = "select t5.*,t4.area_no,t4.areaname,ifnull(t4.employee_a_no,'') as employee_a_no,t4.employee_a_name,ifnull(t4.employee_b_no,'') as employee_b_no,t4.employee_b_name,t4.dellable from "+
					"	(select id,name,status,dellable from t_tiny_village  where  status=0  and FIND_IN_SET(town_id,(SELECT town_id from t_store where store_id="+storeId+"))) t5 "+
					"	 left join "+
					"	(select t1.store_id,t1.area_no,t1.id as areaid,t1.name as areaname,t1.employee_a_no,t1.employee_a_name,t1.employee_b_no,t1.employee_b_name,t3.name as tname,t3.id,t3.dellable from t_area t1 "+ 
					"	INNER JOIN t_area_info t2 on t1.id = t2.area_id and t1.status=0 and t2.tin_village_id is null "+
					"	INNER JOIN t_tiny_village t3 on t3.village_id = t2.village_id and t3.status=0  and (t3.dellable <> 1 or t3.dellable is null) "+
					"	union ALL "+
					"	select t1.store_id,t1.area_no,t1.id as areaid,t1.name as areaname,t1.employee_a_no,t1.employee_a_name,t1.employee_b_no,t1.employee_b_name,t3.name as tname,t3.id,t3.dellable from t_area t1  "+
					"	INNER JOIN t_area_info t2 on t1.id = t2.area_id and t1.status=0 and t2.tin_village_id is not null "+
					"	INNER JOIN t_tiny_village t3 on t3.id = t2.tin_village_id  and t3.status=0  and (t3.dellable <> 1 or t3.dellable is null)) t4 "+
					"	on t5.id = t4.id";
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> queryAllTinyAreaOfStore(Long storeId) {
		//String sql="select a.id,a.name,b.code,b.coordinate_range from t_tiny_village a INNER JOIN tiny_area b on a.id = b.tiny_village_id   and  a.status=0 and (a.dellable <> 1 or a.dellable is null) and FIND_IN_SET(a.town_id,(SELECT town_id from t_store where store_id="+storeId+"))";
		String sql="select a.id,b.code from t_tiny_village a inner join tiny_village_code b on a.id = b.tiny_village_id  where  a.status=0  and FIND_IN_SET(a.town_id,(SELECT town_id from t_store where store_id="+storeId+"))";
 
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public List<Map<String, Object>> queryAllTinyVillageOfArea(Long area_id) {
		String sql="select tin_village_id,ifnull(employee_a_no,'') as employee_a_no, ifnull(employee_b_no,'') as employee_b_no from t_area_info where status=0 and tin_village_id is not NULL and area_id="+area_id +" UNION "+
				" select b.id as tin_village_id,a.employee_a_no,a.employee_b_no from (select * from t_area_info where tin_village_id is null and status=0 and village_id is not null and area_id="+area_id+") a "+
				" INNER JOIN t_tiny_village b on a.village_id = b.village_id";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	public List<Map<String, Object>> queryAllTinyVillageOfArea(Long storeId,String areaNo){
		String sql="select t1.*,t2.code from ("+
				" select ttv.name as tinyvillageName,tai.tin_village_id,ta.name,ifnull(ta.employee_a_no,'') as employee_a_no, ifnull(ta.employee_b_no,'') as employee_b_no,ifnull(ta.employee_a_name,'') as employee_a_name,ifnull(ta.employee_b_name,'') as employee_b_name  from t_area ta inner join t_area_info tai on ta.id = tai.area_id  and tai.status=0 and tai.tin_village_id is not NULL and tai.store_id="+storeId+" and tai.area_no='"+areaNo+"' INNER JOIN t_tiny_village ttv on tai.tin_village_id = ttv.id and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null) "+
				" UNION "+
				" select ttv.name as tinyvillageName,ttv.id as tin_village_id,ta.name,ifnull(ta.employee_a_no,'') as employee_a_no, ifnull(ta.employee_b_no,'') as employee_b_no,ifnull(ta.employee_a_name,'') as employee_a_name,ifnull(ta.employee_b_name,'') as employee_b_name from t_area ta inner join t_area_info tai on ta.id = tai.area_id and tai.tin_village_id is null and tai.status=0 and tai.village_id is not null  and tai.store_id="+storeId+" and tai.area_no='"+areaNo+"' "+
				" INNER JOIN t_tiny_village ttv on tai.village_id = ttv.village_id and  ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null)) t1 "+
				" INNER JOIN tiny_village_code t2 on t1.tin_village_id = t2.tiny_village_id";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@Override
	public List<Map<String, Object>> getAreaOfTinyVillage(String code) {
		String sql="select c.*,d.code from  "
				+ "(select a.store_id,a.name,a.area_no,b.tin_village_id,b.village_id,b.town_id,a.employee_a_no,a.employee_a_name,a.employee_b_no,a.employee_b_name from t_area a INNER JOIN t_area_info b on a.id = b.area_id and b.tin_village_id is not  NULL and b.status=0   INNER JOIN t_tiny_village ttv on b.tin_village_id = ttv.id and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null)"
				+ "UNION  "
				+ "select ta.store_id,ta.name,ta.area_no,ttv.id as tin_village_id,tai.village_id,ttv.town_id,ta.employee_a_no,ta.employee_a_name,ta.employee_b_no,ta.employee_b_name  from t_area ta INNER JOIN  t_area_info tai on ta.id = tai.area_id and tai.status=0 and tai.tin_village_id is null  INNER JOIN t_tiny_village  ttv on tai.village_id = ttv.village_id   and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null)) c  "
				+ "INNER JOIN tiny_village_code d  on c.tin_village_id = d.tiny_village_id ";
		if(code!=null&&!"".equals(code)){
			sql =sql+" where c.store_id="+Long.parseLong(code);
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	@Override
	public List<Map<String, Object>> getAllTinyVillageOdAreaNo(String area_no) {
		String sql="SELECT c.*, d. CODE FROM ( SELECT a.store_id,a. NAME,a.area_no,b.tin_village_id,a.employee_a_no,a.employee_a_name,a.employee_b_no,a.employee_b_name FROM t_area a"+
				" INNER JOIN t_area_info b ON a.id = b.area_id AND b.tin_village_id IS NOT NULL AND b. STATUS = 0 and a.area_no='"+area_no+"' INNER JOIN t_tiny_village ttv ON b.tin_village_id = ttv.id"+
				" AND ttv. STATUS = 0 AND ( ttv.dellable <> 1 OR ttv.dellable IS NULL ) UNION SELECT ta.store_id,ta. NAME,ta.area_no,ttv.id AS tin_village_id,ta.employee_a_no,ta.employee_a_name,ta.employee_b_no,ta.employee_b_name"+
				" FROM t_area ta INNER JOIN t_area_info tai ON ta.id = tai.area_id AND tai. STATUS = 0 AND tai.tin_village_id IS NULL and ta.area_no='"+area_no+"' INNER JOIN t_tiny_village ttv ON tai.village_id = ttv.village_id"+
				" AND ttv. STATUS = 0 AND ( ttv.dellable <> 1 OR ttv.dellable IS NULL)) c INNER JOIN tiny_village_code d ON c.tin_village_id = d.tiny_village_id";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	

}
