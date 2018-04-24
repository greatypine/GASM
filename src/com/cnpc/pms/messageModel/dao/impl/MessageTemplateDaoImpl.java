/**
 * gaobaolei
 */
package com.cnpc.pms.messageModel.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.messageModel.dao.MessageTemplateDao;
import com.cnpc.pms.messageModel.entity.MessageTemplate;

/**
 * @author gaobaolei
 *
 */
public class MessageTemplateDaoImpl extends BaseDAOHibernate implements MessageTemplateDao{
	
	public void saveMessageTemplate(List<MessageTemplate> list){
		
	}

	
	@Override
	public List<Map<String, Object>> queryMessageTemplate(String whereStr,PageInfo pageInfo) {
		String sql = "select * from t_messageTemplate where status=0 "+whereStr;
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        
        pageInfo.setTotalRecords(query.list().size());
        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
                .setMaxResults(pageInfo.getRecordsPerPage()).list();

        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }

        return (List<Map<String,Object>>)lst_data;
	}


	
	@Override
	public List<Map<String, Object>> queryMessageTemplateByModel(String model) {
		
		
		String sql ="select *  from t_messageTemplate where status=0 ";
		
		if(model!=null&&!"".equals(model)){
			sql=sql+" and model='"+model+"'";
		}
		
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        
       
        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
               

        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }

        return (List<Map<String,Object>>)lst_data;
	}


	
	@Override
	public List<Map<String, Object>> queryMessageTemplateByCode(String code) {
		String sql =  "select a.*,b.name as employeeName from t_messageTemplate a left join tb_bizbase_user b on a.approver = b.employeeId where a.status=0 and a.code="+code+" order by a.id";

		
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        
       
        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
               

        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }

        return (List<Map<String,Object>>)lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getMessageTemplate(String code) {
		String sql="select * from t_messageTemplate a left join t_messageTemplateCallBack b on a.code = b.templateCode where a.code='"+code+"'";
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        
       
        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
               

        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }

        return (List<Map<String,Object>>)lst_data;
	}


	
	@Override
	public void deleteMessageTemplateCallBack(String code) {
		String  sql="delete from t_messagetemplatecallback where templateCode='"+code+"'";
		 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		 query.executeUpdate();
	}
	
}
