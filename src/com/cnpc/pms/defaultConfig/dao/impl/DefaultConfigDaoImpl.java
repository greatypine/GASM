/**
 * gaobaolei
 */
package com.cnpc.pms.defaultConfig.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.defaultConfig.dao.DefaultConfigDao;
import com.cnpc.pms.defaultConfig.entity.DefaultConfig;

/**
 * @author gaobaolei
 *
 */
public class DefaultConfigDaoImpl extends BaseDAOHibernate implements DefaultConfigDao{

	
	@Override
	public Map<String, Object> checkDefaultConfig(String employee_no,String employee_name) {
		String sql="select * from t_default_config where employee_no='"+employee_no+"' and employee_name='"+employee_name+"'";
		//SQL查询对象
				SQLQuery query = getHibernateTemplate().getSessionFactory()
						.getCurrentSession().createSQLQuery(sql);

				//获得查询数据
				List<Map<String, Object>> lst_data = query
						.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
				
		return (lst_data==null||lst_data.size()==0)?null:lst_data.get(0);
	}

	
	@Override
	public void saveDefaultConfig(DefaultConfig defaultConfig) {
		String sql="insert ignore into t_default_config("
				+ "version,"
				+ "employee_name,"
				+ "employee_no,"
				+ "duty,"
				+ "default_system,"
				+ "create_time,"
				+ "create_user,"
				+ "update_time"
				+ ")"
				+ "values("
				+ ""+defaultConfig.getVersion()+","
				+ "'"+defaultConfig.getEmployee_name()+"',"
				+ "'"+defaultConfig.getEmployee_no()+"',"
				+ "'"+defaultConfig.getDuty()+"',"
				+ ""+defaultConfig.getDefault_system()+","
				+ "'"+defaultConfig.getCreate_time()+"',"
				+ "'"+defaultConfig.getCreate_user()+"',"
				+ "'"+defaultConfig.getUpdate_time()+"'"
				+ ")";
		
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
		query.executeUpdate();
		
	}

	
	@Override
	public void updateDefaultConfig(DefaultConfig defaultConfig) {
		String sql="update t_default_config set "
				+ "duty='"+defaultConfig.getDuty()+"',"
				+ "default_system="+defaultConfig.getDefault_system()+","
				+ "update_time='"+defaultConfig.getUpdate_time()+"'"
				+" where id="+defaultConfig.getId();
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
		int updateresult = query.executeUpdate();		
		
	}


}
