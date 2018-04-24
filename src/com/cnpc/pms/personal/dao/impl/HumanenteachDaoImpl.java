package com.cnpc.pms.personal.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.personal.dao.HumanenteachDao;
import com.cnpc.pms.personal.dao.ImportHumanresourcesDao;
import com.cnpc.pms.personal.entity.Humanenteach;
import com.cnpc.pms.personal.entity.ImportHumanresources;
import com.cnpc.pms.personal.entity.YyMicrData;

public class HumanenteachDaoImpl extends DAORootHibernate implements HumanenteachDao{

	@Override
	public void deleteHumanenTeachByParentId(Long hr_id) {
		if(hr_id!=null){
			String sql = "DELETE FROM t_human_teach WHERE t_human_teach.hr_id="+hr_id;
			SQLQuery deletehuman_sqlQuery = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createSQLQuery(sql);
			deletehuman_sqlQuery.executeUpdate();
		}
	}
	
	@Override
	public List<Humanenteach> queryHumanenTeachByParentId(Long hr_id) {
		String sql = "select * FROM t_human_teach WHERE t_human_teach.hr_id="
				+ hr_id;
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		query.addEntity(Humanenteach.class);
		List<Humanenteach> list = query.list();
		return list;
	}
	
	
	
}
