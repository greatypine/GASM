package com.cnpc.pms.personal.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.personal.dao.NewsDao;

public class NewsDaoImpl extends BaseDAOHibernate  implements NewsDao{

	@Override
	public void deleteObject(Object object) {
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 session.delete(object);
		
	}

}
