package com.cnpc.pms.personal.dao.impl;

import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.personal.dao.FerryPushDao;

public class FerryPushDaoImpl extends BaseDAOHibernate implements FerryPushDao{

	@Override
	public void updateFerryPushStatus(Long store_id, String str_month, Integer state_type) {
		String sql="update t_ferry_push SET state_type="+state_type+" WHERE store_id="+store_id+"  AND str_month='"+str_month+"'";
		Session session = getHibernateTemplate().getSessionFactory()
                .getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.executeUpdate();
	}

}
