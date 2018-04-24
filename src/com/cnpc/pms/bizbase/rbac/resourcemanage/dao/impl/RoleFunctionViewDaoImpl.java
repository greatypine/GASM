package com.cnpc.pms.bizbase.rbac.resourcemanage.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dao.RoleFunctionViewDao;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.RoleFunctionView;

public class RoleFunctionViewDaoImpl extends BaseDAOHibernate implements  RoleFunctionViewDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleFunctionView> queryRoleFunctionViewList(PageInfo pageInfo,List<Map<String, Object>> condition) {
		String countHeader = "SELECT COUNT(1) ";
		String sqlHeader = "select tab2.id, tab1.id funcRoleId,tab3.FUNCTIONID functionId,tab3.path path,tab3.BUTTONNAME buttonName";
		StringBuilder sql = new StringBuilder();
		List<RoleFunctionView> result = new ArrayList<RoleFunctionView>();
		
		sql.append("\n  from tb_bizbase_role tab1 left join tb_bizbase_role_function tab2");
		sql.append("\n  on tab1.id=tab2.pk_role ");
		sql.append("\n right join ");
		sql.append("\n (select t.path path, ");
		sql.append("\n       case when t1.id is null then t.id else t1.id end functionId, ");
		sql.append("\n        t1.activity_name as buttonName ");
		sql.append("\n   from tb_bizbase_function t ");
		sql.append("\n   left join tb_bizbase_function t1 on t.id = t1.parent_code and t1.type = '2' ");
		sql.append("\n  where t.type = '1' ");
		sql.append("\n   order by t.path ,t1.orderno) tab3 ");
		sql.append("\n   on tab2.pk_activity=tab3.functionid where 1 = 1");
		
		for(Map<String, Object> map : condition){
			
			if("funcRoleId".equals(map.get("key")) && checkValue(map.get("value"))){
				
				sql.append(" and tab1.id = '"+getStringValue(map.get("value"))+"'");
				
			}
		}
		
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sqlHeader.concat(sql.toString()));
		
		SQLQuery countQuery = getHibernateTemplate().getSessionFactory()
				.getCurrentSession()
				.createSQLQuery(countHeader.concat(sql.toString()));
		int totalRecords = Integer.valueOf(countQuery.list().get(0).toString());
		pageInfo.setTotalRecords(totalRecords);
		
		List<Map<String, Object>> list = query
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(
						pageInfo.getRecordsPerPage()
								* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		
		
		for(Map<String,Object> map : list){
			RoleFunctionView view = new RoleFunctionView();
			Object pathObject = map.get("path");
			if(pathObject != null && !"".equals(pathObject)){
				query = getHibernateTemplate().getSessionFactory()
						.getCurrentSession()
						.createSQLQuery("SELECT GROUP_CONCAT(fn.activity_name) FROM tb_bizbase_function fn  WHERE fn.activity_code IN ('".concat(pathObject.toString().replace(",", "','").concat("')")));
				Object obj = query.uniqueResult();
				if(obj != null && !"".equals(obj)){
					view.setMenuName(obj.toString());
				}
			}else{
				view.setMenuName("");
			}
			view.setFuncRoleId(getLongValue(map.get("funcRoleId")));
			view.setFunctionId(getLongValue(map.get("functionId")));
			view.setId(getLongValue(map.get("id")));
			view.setButtonName(getStringValue(map.get("buttonName")));
			result.add(view);
		}
		return result;
	}

	public Long getLongValue(Object value){
		if(value != null && !"".equals(value)){
			return Long.valueOf(value.toString());
		}
		return null;
	}
	
	public String getStringValue(Object value){
		if(value != null && !"".equals(value)){
			return value.toString();
		}
		return null;
	}
	
	public Boolean checkValue(Object value){
		if(value != null && !"".equals(value)){
			return true;
		}
		return false;
	}
}