package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.ApprovalDao;

public class ApprovalDaoImpl extends BaseDAOHibernate implements ApprovalDao{

	@Override
	public List<Map<String, Object>> getApprovalInfoList(String where, PageInfo pageInfo) {
		//sql查询列，用于分页计算数据总数
        String str_count_sql = "SELECT count(1) FROM t_approval WHERE state_type!=3 "+where;
        //sql查询列，用于页面展示所有的数据
        String find_sql="SELECT app.state_type,app.store_id,app.id,app.`name`,app.state_type,app.str_month,app.type_id,app.store_name FROM t_approval app where state_type!=3  "+where+"  order by app.state_type ASC,app.id DESC ";
        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(find_sql);
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
