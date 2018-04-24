package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.AuditDao;

public class AuditDaoImpl extends BaseDAOHibernate  implements AuditDao{

	@Override
	public List<Map<String, Object>> getAuditList(String where, PageInfo pageInfo) {
		String str_count_sql = "select count(1) from t_audit where 1=1 "+where;
				        String find_sql="select * from t_audit where 1=1 ";
				        StringBuilder sb_sql = new StringBuilder();
				        sb_sql.append(find_sql);
				        sb_sql.append(where+" order by check_id asc,id desc ");
				        //SQL查询对象
				        SQLQuery query = getHibernateTemplate().getSessionFactory()
				                .getCurrentSession().createSQLQuery(sb_sql.toString());
				        //查询数据量对象
				        SQLQuery countQuery = getHibernateTemplate().getSessionFactory()
				                .getCurrentSession()
				                .createSQLQuery(str_count_sql);
				        pageInfo.setTotalRecords(Integer.valueOf(countQuery.list().get(0).toString()));
				        //获得查询数据
				        List<?> lst_data = query
				                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				                .setFirstResult(
				                        pageInfo.getRecordsPerPage()
				                                * (pageInfo.getCurrentPage() - 1))
				                .setMaxResults(pageInfo.getRecordsPerPage()).list();

				        List<Map<String,Object>> lst_result = new ArrayList<Map<String, Object>>();
				        //如果没有数据返回
				        if(lst_data == null || lst_data.size() == 0){
				            return lst_result;
				        }
				        //转换成需要的数据形式
				        for(Object obj_data : lst_data){
				        	lst_result.add((Map<String,Object>)obj_data);
				        }
				        return lst_result;
	}

}
