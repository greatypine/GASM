package com.cnpc.pms.personal.dao.impl;

import java.util.List;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.WorkRecordDao;
import com.cnpc.pms.personal.dao.YyMicrDataDao;
import com.cnpc.pms.personal.dao.YyStoreDao;
import com.cnpc.pms.personal.entity.YyMicrData;
import com.cnpc.pms.personal.entity.YyStore;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

public class WorkRecordDaoImpl extends DAORootHibernate implements WorkRecordDao{
	@Override
	public void delWorkRecoedById(Long workrecord_id){
		StringBuffer sql = new StringBuffer();
		if(workrecord_id!=null){
			sql.append("delete from t_work_record WHERE t_work_record.workrecord_id="+workrecord_id+" ");
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	}
	
}
