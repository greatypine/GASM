package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.MsgNoticeDao;
import com.cnpc.pms.personal.dao.OfficeDao;

public class MsgNoticeDaoImpl extends BaseDAOHibernate implements MsgNoticeDao {

	@Override
	public List<Map<String, Object>> queryNoticeList(Long num) {
        String find_sql="select b.id,DATE_FORMAT(b.create_time,'%Y-%m-%d') as notice_date,b.title,b.noticeNo from (select DISTINCT noticeNo  from t_notice_reciver where storeNo='0010Y0008' and status=0) a INNER JOIN t_notice b on a.noticeNo= b.noticeNo ORDER BY b.noticeNo DESC";
        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(find_sql);
        
        if(num!=null){
        	sb_sql.append(" limit "+num);
        }
        
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sb_sql.toString());
        List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        List<Map<String,Object>> lst_result = new ArrayList<Map<String, Object>>();
        if(lst_data == null || lst_data.size() == 0){
            return lst_result;
        }
        for(Object obj_data : lst_data){
        	lst_result.add((Map<String,Object>)obj_data);
        }
        return lst_result;
	}
	
	
	
	
	
	@Override
	public List<Map<String, Object>> queryMessageList(Long num) {
        String find_sql="";
        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(find_sql);
        
        if(num!=null){
        	sb_sql.append(" limit "+num);
        }
        
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sb_sql.toString());
        List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        List<Map<String,Object>> lst_result = new ArrayList<Map<String, Object>>();
        if(lst_data == null || lst_data.size() == 0){
            return lst_result;
        }
        for(Object obj_data : lst_data){
        	lst_result.add((Map<String,Object>)obj_data);
        }
        return lst_result;
	}
	
	
	
}
