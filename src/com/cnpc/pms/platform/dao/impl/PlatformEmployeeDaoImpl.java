/**
 * gaobaolei
 */
package com.cnpc.pms.platform.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.platform.dao.PlatformEmployeeDao;

/**
 * @author gaobaolei
 *
 */
public class PlatformEmployeeDaoImpl extends DAORootHibernate implements PlatformEmployeeDao {

	
	@Override
	public List<Map<String, Object>> getEmployeeByEmployeeNo(String employeeNo) {
		String str_sql = "select concat(te.id,'') as employeeId,te.name as employeeName,te.code as employeeNo,te.`online` as online from t_employee te  where  te.CODE = '"+employeeNo+"'";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
        List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
        try{
            SQLQuery query = session.createSQLQuery(str_sql);
            List<Map<String,Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            lst_result = lst_data;
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
		return lst_result;
	}
	
	
	
}
