package com.cnpc.pms.personal.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.DistCityDao;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.entity.House;
import com.cnpc.pms.personal.entity.Store;

public class DistCityDaoImpl extends BaseDAOHibernate implements DistCityDao {
	
	public int removeDistCityByUserid(Long userid){
		String removeSql = "delete from t_dist_city where pk_userid="+userid;
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(removeSql);
		int update = query.executeUpdate();
		return update;
	}
	
	@Override
	public List queryDistCityCount(){
		String sql = "SELECT pk_userid,count(1) as citycount FROM t_dist_city GROUP BY pk_userid";
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	

}
