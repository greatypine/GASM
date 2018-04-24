package com.cnpc.pms.personal.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.personal.dao.ImportHumanresourcesDao;
import com.cnpc.pms.personal.entity.ImportHumanresources;

public class ImportHumanresourcesDaoImpl extends DAORootHibernate implements ImportHumanresourcesDao{

	
	@Override
	public List<ImportHumanresources> queryImportHumanresourcesByCard(String cardNumber,String name) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM t_import_humanresources WHERE 1=1 ");
		if(name!=null&&name.length()>0){
			sql.append(" AND t_import_humanresources.name ='"+name+"'");
		}
		if(cardNumber!=null&&cardNumber.length()>0){
			sql.append(" AND t_import_humanresources.cardnumber='"+cardNumber+"' ");
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		query.addEntity(ImportHumanresources.class);
		List<ImportHumanresources> list = query.list();
		return list;
	}
	
	
	@Override
	public Map<String,Double> queryMaxImportDept() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MAX(t_import_humanresources.deptno) as maxno,t_import_humanresources.deptname FROM t_import_humanresources GROUP BY deptname");
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		//获得查询数据
        List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        Map<String, Double> map_resultMap = new HashMap<String, Double>();
        for(Object o:lst_data){
        	Map<String, Object> map = (Map<String, Object>)o;
        	map_resultMap.put(map.get("deptname").toString(), Double.parseDouble(map.get("maxno").toString()));
        }
		return map_resultMap;
	}
	
	
	
	
	@Override
	public void updateImportHumanresourcesId(String ids) {
		if(ids!=null&&ids.length()>0){
			String sql = "update t_import_humanresources set t_import_humanresources.importstatus=1 WHERE t_import_humanresources.id in("+ids+");";
			SQLQuery updateimporthuman_sqlQuery = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createSQLQuery(sql);
			updateimporthuman_sqlQuery.executeUpdate();
		}
	}
}
