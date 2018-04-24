package com.cnpc.pms.personal.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.personal.dao.CityDao;
import com.cnpc.pms.personal.entity.City;

public class CityDaoImpl extends BaseDAOHibernate implements CityDao{

	@Override
	public Integer getCityCountByProvinceId(Long province_id) {
		String sql = "select count(1) from t_city where province_id = '" + province_id+"'";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(sql);
		 List list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 return Integer.parseInt(list.get(0)+"");
		 }
		return null;
	}

	@Override
	public Integer getConCityByProvinceId(Long province_id) {
		String sql = "select count(*) from (select city_id from t_store where province_id = '"+province_id+"' GROUP BY city_id) t where t.city_id is not null";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(sql);
		 List list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 return Integer.parseInt(list.get(0)+"");
		 }
		return null;
	}

	@Override
	public Integer getCityCount() {
		String sql = "select count(id) from t_city";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(sql);
		 List list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 return Integer.parseInt(list.get(0)+"");
		 }
		return null;
	}

	@Override
	public Integer getConCityCount() {
		String sql = "select count(*) from (select city_id from t_store where city_id is not null and flag=0 and ifnull(estate,'')!='闭店中' GROUP BY city_id) t";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(sql);
		 List list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 return Integer.parseInt(list.get(0)+"");
		 }
		return null;
	}

	@Override
	public List<City> getCityByCityName(String cityName) {
		String sql = "select * from t_city where name like '"+cityName+"'";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(sql);
		 List<City> list = (List<City>)sqlQuery.list();
		 return list;
	}

	@Override
	public String getCityByCityId(Long cityId) {
		String sql = "select cityname from t_dist_citycode WHERE id = "+cityId;
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(sql);
		 List list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 return list.get(0)+"";
		 }
		return null;
	}

}
