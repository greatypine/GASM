package com.cnpc.pms.personal.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.personal.dao.GeneralDao;

public class GeneralDaoImpl extends BaseDAOHibernate implements GeneralDao {
	
	@Override
	public String queryMaxEmployee_no(){
		String sql = "SELECT MAX(t_general.employee_no) as max_employee_no from t_general;";
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
		String max_employee_no = query.uniqueResult()==null?null:query.uniqueResult().toString();
		return max_employee_no;
	}
	
	@Override
	public int saveGeneralCityReference(Long gmid,String vals){
		deleteGeneralCityReference(gmid);
		String sql = "insert into t_general_city_reference(gmid,ctid) VALUES "+vals;
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
		int ret = query.executeUpdate();
		return ret;
	}
	@Override
	public int deleteGeneralCityReference(Long gmid){
		String sql = "DELETE FROM t_general_city_reference WHERE t_general_city_reference.gmid ="+gmid;
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
		int ret = query.executeUpdate();
		return ret;
	}

	@Override
	public Long queryMaxid(){
		String sql = "SELECT case when max(id) is null then 0 ELSE max(id) END AS count FROM t_general_city_reference";
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
		return Long.parseLong(query.uniqueResult().toString());
	}
	
	@Override
	public Map<String, Object> queryCityNamesById(Long gmid){
		String sql = "SELECT gc.id,dc.cityname,dc.id as cityid FROM t_general_city_reference gc LEFT JOIN t_dist_citycode dc ON gc.ctid=dc.id WHERE gc.gmid="+gmid;
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		Map<String, Object> ret_map = new HashMap<String, Object>();
		String retname = "";
		String retid="";
		if(lst_data!=null&&lst_data.size()>0){
			for(Object o:lst_data){
				Map<String, Object> map = (Map<String, Object>) o;
				String tmpname = map.get("cityname")==null?"":map.get("cityname").toString();
				int tmpcityid = map.get("cityid")==null?0:Integer.parseInt(map.get("cityid").toString());
				if(tmpname!=null&&tmpname.length()>0){
					retname+=tmpname+",";
				}
				if(tmpcityid!=0){
					retid+=tmpcityid+",";
				}
			}
		}
		ret_map.put("citynames", retname);
		ret_map.put("cityids", retid);
		return ret_map;
	}
	@Override
	public String queryGeneralIdsByCity(String cityname){
		String sql = "SELECT * FROM t_general_city_reference WHERE t_general_city_reference.ctid in (SELECT id from t_dist_citycode WHERE t_dist_citycode.cityname like '%"+cityname+"%')";
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		String ret = "";
		if(lst_data!=null&&lst_data.size()>0){
			for(Object o:lst_data){
				Map<String, Object> map = (Map<String, Object>) o;
				int tmp = map.get("gmid")==null?0:Integer.parseInt(map.get("gmid").toString());
				ret+=tmp+",";
			}
		}
		if(ret!=""){
			ret=ret.substring(0,ret.length()-1);
		}
		return ret;
	}
	
}
