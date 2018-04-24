package com.cnpc.pms.personal.dao.impl;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.IPage;
import com.cnpc.pms.personal.dao.RelationDao;
import com.cnpc.pms.personal.dao.WorkRecordTotalDao;
import com.cnpc.pms.utils.ValueUtil;
import org.apache.commons.collections.map.HashedMap;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkRecordTotalDaoImpl extends BaseDAOHibernate implements WorkRecordTotalDao {
	@Override
	public Map<String, Object> queryWorkRecordTotalByCheck(String work_month,IPage pageInfo,String citysql) {
		StringBuffer sbfCondition = new StringBuffer();
		if(citysql!=""&&citysql.length()>0){
			sbfCondition.append(" and s.city_name in ("+citysql+")");
		}else{
			sbfCondition.append(" and 0=1 ");
		}
		
		String str_from = " FROM" +
				"	t_store s LEFT JOIN " +
				"	(SELECT * FROM t_work_record_total WHERE work_month = '"+work_month+"') wrt " +
				"		ON wrt.store_id = s.store_id" +
				"	LEFT JOIN tb_bizbase_user u ON u.id = s.skid" +
				"	WHERE (wrt.commit_status IN (1,2) OR wrt.commit_status IS NULL)" + sbfCondition.toString() +
				"	ORDER BY wrt.commit_status DESC";
		String str_count_sql = "SELECT COUNT(1)".concat(str_from);
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(str_count_sql);
		pageInfo.setTotalRecords(Integer.valueOf(query.uniqueResult().toString()));

		String sql = "SELECT s.`name` AS store_name,u.`name` as sk_name,u.phone," +
				"	(" +
				"		CASE WHEN wrt.id is NULL THEN '未保存' " +
				"		WHEN wrt.commit_status = 1 THEN '已提交'" +
				"		WHEN wrt.commit_status = 2 THEN '未提交'" +
				"		ELSE '' END\n" +
				"	) AS commit_status".concat(str_from);

		query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();

		Map<String,Object> map_result = new HashMap<String,Object>();
		map_result.put("pageinfo", pageInfo);
		map_result.put("header", "");
		map_result.put("data", lst_data);
		return map_result;
	}
}
