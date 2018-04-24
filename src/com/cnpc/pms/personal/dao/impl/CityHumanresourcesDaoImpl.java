package com.cnpc.pms.personal.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.personal.dao.CityHumanresourcesDao;
import com.cnpc.pms.personal.dao.HumanresourcesDao;

public class CityHumanresourcesDaoImpl extends DAORootHibernate implements CityHumanresourcesDao{

	//取得汇思最大员工编号
	@Override
	public String queryMaxEmpNo(String type) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t_city_humanresources.employee_no from t_city_humanresources WHERE 1=1 ");
		sql.append(" and t_city_humanresources.employee_no like '"+type+"%' ");
		/*if(type!=null&&type.equals("HS")){
			sql.append(" and t_humanresources.employee_no like 'GAHSBJ%' ");
		}else{
			sql.append(" and t_humanresources.employee_no like 'GATKBJ%' ");
		}*/
		sql.append(" ORDER BY t_city_humanresources.employee_no DESC limit 1");
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		//获得查询数据
        List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        String maxEmployee_no = "00000";
        for(Object o:lst_data){
        	Map<String, Object> map = (Map<String, Object>)o;
        	maxEmployee_no=map.get("employee_no").toString();
        }
		return maxEmployee_no;
	}
		
	
}
