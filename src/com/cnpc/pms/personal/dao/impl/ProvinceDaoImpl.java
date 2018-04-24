package com.cnpc.pms.personal.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.personal.dao.ProvinceDao;
import com.cnpc.pms.personal.entity.Province;

public class ProvinceDaoImpl extends BaseDAOHibernate  implements ProvinceDao{

	@Override
	public String getProvinceByTown_id(Long town_id) {
		String t_province="SELECT t_province.* from t_town LEFT JOIN t_county ON t_town.county_id=t_county.id "+
"LEFT JOIN t_city ON t_county.city_id=t_city.id LEFT JOIN t_province ON t_city.province_id=t_province.id WHERE t_town.id="+town_id;
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(t_province).addEntity(Province.class);
		 List<Province> list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 Province province= list.get(0);
			  System.out.println(province.getName());
			  return province.getName();
		 }
		return null;
	}

	@Override
	public Province getProvinceInfoByTown_id(Long town_id) {
		String t_province="SELECT t_province.* from t_town LEFT JOIN t_county ON t_town.county_id=t_county.id "+
				"LEFT JOIN t_city ON t_county.city_id=t_city.id LEFT JOIN t_province ON t_city.province_id=t_province.id WHERE t_town.id="+town_id;
						HibernateTemplate template = getHibernateTemplate();
						SessionFactory sessionFactory = template.getSessionFactory();
						 Session session = sessionFactory.getCurrentSession();
						 SQLQuery sqlQuery =session.createSQLQuery(t_province).addEntity(Province.class);
						 List<Province> list = sqlQuery.list();
						 if(list!=null&&list.size()>0){
							 Province province= list.get(0);
							  return province;
						 }
						return null;
	}

	@Override
	public Integer getProvinceCount() {
		String sql = "select count(1) from t_province";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(sql);
		 List<Province> list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 return Integer.parseInt(list.get(0)+"");
		 }
		return null;
	}

	@Override
	public Integer getProvinceCountByStore() {
		String sql = "select count(DISTINCT province_id) from t_store where province_id is not null";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(sql);
		 List<Province> list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 return Integer.parseInt(list.get(0)+"");
		 }
		return null;
	}

}
