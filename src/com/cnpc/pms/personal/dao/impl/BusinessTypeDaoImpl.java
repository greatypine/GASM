package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.personal.dao.BusinessTypeDao;
import com.cnpc.pms.personal.entity.BusinessType;

public class BusinessTypeDaoImpl extends BaseDAOHibernate implements BusinessTypeDao {

	@Override
	public List<Map<String, Object>> getTwolevelCode() {
		List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
		Map<String,Object> map=null;
		String t_house="SELECT * from t_poi_type GROUP BY level1_code";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(t_house);
		 List<BusinessType> list = sqlQuery.addEntity(BusinessType.class).list();
		 if(list!=null&&list.size()>0){
			 for (BusinessType businessType : list) {
				map=new HashMap<String, Object>();
				map.put("two_level_index", businessType.getLevel1_name());
				map.put("two_level_code", businessType.getLevel1_code());
				listMap.add(map);
			}
		 }
		return listMap;
	}

	@Override
	public List<Map<String, Object>> getThreeCode(String level2_code) {
		List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
		Map<String,Object> map=null;
		String string="";
		if(level2_code!=null){
			string=" where level1_code='"+level2_code+"' ";
		}
		String t_house="SELECT * from t_poi_type "+string+" GROUP BY level2_code";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(t_house);
		 List<BusinessType> list = sqlQuery.addEntity(BusinessType.class).list();
		 if(list!=null&&list.size()>0){
			 for (BusinessType businessType : list) {
				map=new HashMap<String, Object>();
				map.put("three_level_index", businessType.getLevel2_name());
				map.put("three_level_code", businessType.getLevel2_code());
				listMap.add(map);
			}
		 }
		return listMap;
	}

	@Override
	public List<Map<String, Object>> getFourCode(String level3_code) {
		List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
		Map<String,Object> map=null;
		String string="";
		if(level3_code!=null){
			string=" where level2_code='"+level3_code+"' ";
		}
		String t_house="SELECT * from t_poi_type "+string+" GROUP BY level3_code";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(t_house);
		 List<BusinessType> list = sqlQuery.addEntity(BusinessType.class).list();
		 if(list!=null&&list.size()>0){
			 for (BusinessType businessType : list) {
				map=new HashMap<String, Object>();
				map.put("four_level_index", businessType.getLevel3_name());
				map.put("four_level_code", businessType.getLevel3_code());
				listMap.add(map);
			}
		 }
		return listMap;
	}

	@Override
	public List<Map<String, Object>> getFiveCode(String level4_code) {
		List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
		Map<String,Object> map=null;
		String string="";
		if(level4_code!=null){
			string=" where level3_code='"+level4_code+"' ";
		}
		String t_house="SELECT * from t_poi_type "+string+" GROUP BY level4_code";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(t_house);
		 List<BusinessType> list = sqlQuery.addEntity(BusinessType.class).list();
		 if(list!=null&&list.size()>0){
			 for (BusinessType businessType : list) {
				map=new HashMap<String, Object>();
				map.put("five_level_index", businessType.getLevel4_name());
				map.put("five_level_code", businessType.getLevel4_code());
				listMap.add(map);
			}
		 }
		return listMap;
	}


	

}
