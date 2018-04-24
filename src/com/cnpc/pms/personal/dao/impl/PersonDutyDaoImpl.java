package com.cnpc.pms.personal.dao.impl;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.personal.dao.PersonDutyDao;

public class PersonDutyDaoImpl extends BaseDAOHibernate implements PersonDutyDao {
	/**
	 * 判断街道或社区，商业信息是否由值，如果有值将该街道或社区下的不是该负责人删除，否则将对应的所有负责人删除
	 * ids--负责人id拼接,type--类型，id--业务id 
	 */
	@Override
	public void deletePersonDuty(String ids, Integer type, Long id) {
		String delSql=null;
		// TODO Auto-generated method stub
		if(ids==null||"".equals(ids)){
			delSql="delete from t_customer_duty where duty_id="+id+" and type="+type;
		}else{
			delSql="delete from t_customer_duty where duty_id="+id+" and type="+type+" and customer_id not in ("+ids+")";
		}
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(delSql);
		int executeUpdate = sqlQuery.executeUpdate();
		
	}

	
}
