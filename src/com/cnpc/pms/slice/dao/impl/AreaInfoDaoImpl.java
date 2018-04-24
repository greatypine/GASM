/**
 * gaobaolei
 */
package com.cnpc.pms.slice.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.IJoin;
import com.cnpc.pms.base.paging.IPage;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.model.PMSQuery;
import com.cnpc.pms.slice.dao.AreaDao;
import com.cnpc.pms.slice.dao.AreaInfoDao;
import com.cnpc.pms.slice.dto.AreaDto;
import com.cnpc.pms.slice.entity.Area;
import com.cnpc.pms.slice.entity.AreaInfo;
import com.ibm.db2.jcc.t4.ob;

/**
 * @author gaobaolei
 *
 */
public class AreaInfoDaoImpl extends BaseDAOHibernate implements AreaInfoDao {

	@Override
	public List<Map<String, Object>> queryAreaInfoByAreaId(String area_id){
//		String sql = "SELECT t_town.name,t_area_info.area_id,t_village.name as villname,t_tiny_village.name as tinyname,t_building.name as buildingname from t_area_info "+ 
//		" LEFT JOIN t_town ON t_town.id=t_area_info.town_id "+
//		" LEFT JOIN t_building ON t_building.id=t_area_info.building_id "+
//		" LEFT JOIN t_tiny_village ON t_tiny_village.id=t_area_info.tin_village_id "+
//		" LEFT JOIN t_village ON t_village.id=t_area_info.village_id "+
//		" WHERE area_id in ("+area_id+")";
		
		
		String sql="select tt.name,tv.name as villname,ttv.name as tinyname,a.area_id from ((select tai.area_id,tai.area_no,tai.town_id,tai.village_id,ttv.id as tin_village_id  from t_area_info tai INNER join t_tiny_village ttv on tai.village_id = ttv.village_id  where tai.area_id in ("+area_id+") and tai.village_id is not null and tai.tin_village_id is null and  ttv.status=0 and (ttv.dellable <> 1 or ttv.dellable is null)) "+  
					" UNION ALL "+ 
					" (select tai.area_id,tai.area_no,tai.town_id,tai.village_id,tai.tin_village_id  from t_area_info tai inner JOIN t_tiny_village ttv on tai.tin_village_id = ttv.id where tai.area_id in ("+area_id+") and tai.tin_village_id is  not null and ttv.status=0 and (ttv.dellable <> 1 or ttv.dellable is null)) ) a "+ 
					" left JOIN t_town tt on a.town_id = tt.id "+ 
					" left JOIN t_village tv on a.village_id = tv.id "+ 
					" left join  t_tiny_village ttv on a.tin_village_id = ttv.id";
		 SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession().createSQLQuery(sql);
	      //获得查询数据
	     List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<AreaInfo> findAreaInfoByString(String idString) {
		String sql_from="SELECT * FROM t_area_info WHERE tin_village_id in("+idString+")  and area_id in (SELECT id FROM t_area WHERE status=0)";
		
		 Session session = getHibernateTemplate().getSessionFactory().openSession();
	        Map<String,Object> map_result = new HashMap<String,Object>();
	        List<?> lst_data = null;
	        try{
	            SQLQuery sqlQuery = session.createSQLQuery(sql_from);
	            List<AreaInfo> list = sqlQuery.addEntity(AreaInfo.class).list();
	            return list;
	        }catch (Exception e){
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
	        return null;
	}

	@Override
	public List<Map<String, Object>> findAreaInfoBytinyId(Long tinyId) {
		String sql_from="select * from ( select area_id,tin_village_id  from t_area_info where status=0 and tin_village_id is not null  union all  select a.area_id,b.id as tin_village_id  from (select * from t_area_info where status=0 and tin_village_id is null) a left join t_tiny_village b  on  a.village_id = b.village_id) c  where c.tin_village_id ="+tinyId;
		 Session session = getHibernateTemplate().getSessionFactory().openSession();
	        List<?> lst_data = null;
	        try{
	            SQLQuery sqlQuery = session.createSQLQuery(sql_from);
	            List<Map<String, Object>> list = sqlQuery.list();
	            return list;
	        }catch (Exception e){
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
		return null;
	}

}
