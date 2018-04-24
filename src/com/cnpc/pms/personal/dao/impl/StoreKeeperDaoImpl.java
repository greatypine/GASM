package com.cnpc.pms.personal.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.personal.dao.HumanresourcesDao;
import com.cnpc.pms.personal.dao.StoreKeeperDao;

public class StoreKeeperDaoImpl extends DAORootHibernate implements StoreKeeperDao{

	
	//取得 运营经理最大编号
	@Override
	public String queryMaxNo(String type) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t_storekeeper.employee_no FROM t_storekeeper WHERE 1=1 ");
		if(type!=null&&type.equals("SK")){
			sql.append(" AND t_storekeeper.employee_no LIKE 'SK%' ");
		}else{
			sql.append(" AND t_storekeeper.employee_no LIKE 'RM%' ");
		}
		sql.append(" ORDER BY t_storekeeper.employee_no DESC LIMIT 1 ");
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		//获得查询数据
        List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        String maxEmployee_no = null;
        for(Object o:lst_data){
        	Map<String, Object> map = (Map<String, Object>)o;
        	maxEmployee_no=map.get("employee_no").toString();
        }
		return maxEmployee_no;
	}
	
	
}
