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
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.messageModel.dao.MessageNewDao;
import com.cnpc.pms.messageModel.entity.Message;

/**
 * @author gaobaolei
 *
 */
public class MessageNewDaoImpl extends BaseDAOHibernate implements MessageNewDao{

	
	@Override
	public List<Map<String, Object>> queryMessageByCode(String code) {
		String sql = "select * from t_message where status=0 and code="+code;
		
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());

        //获得查询数据
        List<Map<String, Object>> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> queryApproveMessage(String whereStr, PageInfo pageInfo) {
		String sql="select tm.* from t_message tm where status=0 and isDelete=0 and type='other_notice'   "+whereStr+"  order by messageCode desc";
		
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
        
        
        pageInfo.setTotalRecords(query.list().size());
        //获得查询数据
        List<Map<String, Object>> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(
                        pageInfo.getRecordsPerPage()
                                * (pageInfo.getCurrentPage() - 1))
                .setMaxResults(pageInfo.getRecordsPerPage()).list();


        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }


        return (List<Map<String,Object>>)lst_data;
	}

	
	@Override
	public List<Map<String, Object>> queryNoApproveMessage(String whereStr, PageInfo pageInfo) {
		String sql="select tm.* from t_message tm  where status=0 "+whereStr+"  order by messageCode desc";
		
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
        
        
        pageInfo.setTotalRecords(query.list().size());
        //获得查询数据
        List<Map<String, Object>> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(
                        pageInfo.getRecordsPerPage()
                                * (pageInfo.getCurrentPage() - 1))
                .setMaxResults(pageInfo.getRecordsPerPage()).list();


        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }


        return (List<Map<String,Object>>)lst_data;
	}



	@Override
	public List<Map<String, Object>> getApproveMessageDetailInfo(Message message) {
		StringBuilder sql = new StringBuilder("select tm.*,tbu.name as employeeName from t_message tm   inner join tb_bizbase_user tbu on tbu.employeeId = tm.receiveId ");
		
		if(message.getMessageCode() == null){
			sql.append(" and tm.messageCode is null ");
		}else{
			sql.append(" and tm.messageCode="+message.getMessageCode());
		}
		
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());
        
        
        //获得查询数据
        List<Map<String, Object>> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();


        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }


        return (List<Map<String,Object>>)lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getMessageCallBack() {
		
		String sql="select  * from dict_messageCallback ";
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
        
        
        //获得查询数据
        List<Map<String, Object>> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();


        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }


        return (List<Map<String,Object>>)lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getReceiveCity(String whereStr,PageInfo pageInfo) {
		String sql="select * from   t_dist_city a where a.status=0 "+whereStr;
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
        
        
        pageInfo.setTotalRecords(query.list().size());
        //获得查询数据
        List<Map<String, Object>> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(
                        pageInfo.getRecordsPerPage()
                                * (pageInfo.getCurrentPage() - 1))
                .setMaxResults(pageInfo.getRecordsPerPage()).list();


        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }


        return (List<Map<String,Object>>)lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getReceiveStore(String cityStr,String otherWhere, PageInfo pageInfo) {
		if(cityStr!=null&&!"".equals(cityStr)){
			cityStr = " where citycode in ("+cityStr+")";
		}
		String sql="select b.store_id,b.name,b.city_name from (select * from t_dist_citycode "+cityStr+") as a inner join t_store as b on a.cityname=b.city_name "+otherWhere;
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
        
        
        pageInfo.setTotalRecords(query.list().size());
        //获得查询数据
        List<Map<String, Object>> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(
                        pageInfo.getRecordsPerPage()
                                * (pageInfo.getCurrentPage() - 1))
                .setMaxResults(pageInfo.getRecordsPerPage()).list();


        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }


        return (List<Map<String,Object>>)lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getReceiveEmployee(String whereStr, PageInfo pageInfo) {
		
		String sql="select id,name,employeeId from tb_bizbase_user  where disabledFlag=1 "+whereStr;
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
        
        
        pageInfo.setTotalRecords(query.list().size());
        //获得查询数据
        List<Map<String, Object>> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(
                        pageInfo.getRecordsPerPage()
                                * (pageInfo.getCurrentPage() - 1))
                .setMaxResults(pageInfo.getRecordsPerPage()).list();


        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }


        return (List<Map<String,Object>>)lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getReceiveZW(String otherWhere, PageInfo pageInfo) {
		
		String sql=" select * from (select DISTINCT zw from t_humanresources union ALL select DISTINCT zw from t_storekeeper  union ALL select '城市总监' as zw) as a where 1=1 "+otherWhere;
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
        
        
        pageInfo.setTotalRecords(query.list().size());
        //获得查询数据
        List<Map<String, Object>> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(
                        pageInfo.getRecordsPerPage()
                                * (pageInfo.getCurrentPage() - 1))
                .setMaxResults(pageInfo.getRecordsPerPage()).list();


        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }


        return (List<Map<String,Object>>)lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getReceiveEmployee(Map<String, Object> param) {
		String sql = "";
		if(param.get("employee")!=null){
			 sql="select employeeId,token,os,client_id from tb_bizbase_user where employeeId  in "+param.get("employee");
		}else{
			
			sql = "select a.employeeId,a.token,a.os,a.client_id from tb_bizbase_user as  a inner join  t_store as b  on a.store_id = b.store_id where a.disabledFlag=1 and employeeId is not null";
			if(param.get("city")!=null){
				
				sql =sql+ " and   b.city_name in (select cityname from t_dist_citycode where citycode in "+param.get("city")+")";
			}
			
			if(param.get("store")!=null){
				sql =sql+ " and b.store_id in "+param.get("store");
			}
			
			if(param.get("zw")!=null){
				sql =sql+ "  and a.zw in "+param.get("zw");
			}
			
		}
		
		 SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession().createSQLQuery(sql);
	        
	        
        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
         

        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }


        return (List<Map<String, Object>>)lst_data;
	}


	
	@Override
	public List<Map<String, Object>> queryMessageNew(Message message) {
       
		String sql = "select * from t_message where status=0 and isRead=0 and type not like 'news_%' and isDelete=0 and receiveId='"+message.getReceiveId()+"' order by id desc limit 0,3";
		
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());

        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return lst_data;
	}


	
	@Override
	public List<Map<String, Object>> queryMessageHistory(Message message,PageInfo pageInfo) {
		 String whereStr = "";
	        if(message.getIsRead()==0){
	        	 whereStr = " and isRead=0 ";
	        }else if(message.getIsRead()==1){
	        	 whereStr = " and isRead=1 ";
	        }
			String sql = "select * from t_message where status=0 and isDelete=0  and type not like 'news_%' and receiveId='"+message.getReceiveId()+"' "+whereStr+"  order by id desc ";
			
			//SQL查询对象
	        SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession().createSQLQuery(sql);
	        
	        
	        pageInfo.setTotalRecords(query.list().size());
	        //获得查询数据
	        List<Map<String, Object>> lst_data = query
	                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
	                .setFirstResult(
	                        pageInfo.getRecordsPerPage()
	                                * (pageInfo.getCurrentPage() - 1))
	                .setMaxResults(pageInfo.getRecordsPerPage()).list();


	        //如果没有数据返回
	        if(lst_data == null || lst_data.size() == 0){
	            return new ArrayList<Map<String, Object>>();
	        }


	        return (List<Map<String,Object>>)lst_data;

	}


	
	@Override
	public void readMessage(Message message) {
		String sql="update t_message set isRead =1 , readDate=NOW() where id="+message.getId();
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
	    query.executeUpdate();
	}


	
	@Override
	public void deleteMessage(Message message) {
		String sql="update t_message set isDelete =1 , deleteDate=NOW() where id="+message.getId();
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	}


	
	@Override
	public Integer getUnReadMessage(Message message) {
		String sql="select count(id) as amount from t_message where isRead=0 and isDelete=0 and type not like 'news_%' and receiveId='"+message.getReceiveId()+"'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		return Integer.parseInt(query.uniqueResult().toString());
	}


	
	@Override
	public Map<String, Object> getCustomerHouse(Long id) {
		String sql = "select a.*,tv.village_id from t_house a inner JOIN (select house_id from t_house_customer where id in (select MAX(id) from t_house_customer  where customer_id ="+id+")) b on   a.id = b.house_id inner join t_tiny_village tv on a.tinyvillage_id=tv.id";
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());

        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
       if(lst_data!=null&&lst_data.size()>0){
    	   return lst_data.get(0);
       }
		
       return null;
	}


	
	@Override
	public Map<String, Object> getAreaOfCustomer(Long id, Integer model) {
		String sql = "select ta.employee_a_no,ta.store_id from t_area ta INNER JOIN t_area_info tai on ta.id = tai.area_id and tai.building_id="+id+" and build_model="+model;
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());

        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
       if(lst_data!=null&&lst_data.size()>0){
    	   return lst_data.get(0);
       }
		
       return null;
	}


	
	@Override
	public Map<String, Object> getAreaOfCustomerByTinyVillage(Long tinyvillageId) {
		String sql = "select ta.employee_a_no,ta.store_id from t_area ta INNER JOIN t_area_info tai on ta.id = tai.area_id and tai.building_id is null and ta.status=0  and tai.tin_village_id="+tinyvillageId;
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());

        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
       if(lst_data!=null&&lst_data.size()>0){
    	   return lst_data.get(0);
       }
		
       return null;
	}


	
	@Override
	public Map<String, Object> getAreaOfCustomerByVillage(Long villageId) {
		String sql = "select ta.employee_a_no,ta.store_id from t_area ta INNER JOIN t_area_info tai on ta.id = tai.area_id and tai.tin_village_id is null and ta.status=0 and tai.village_id="+villageId;
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());

        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
       if(lst_data!=null&&lst_data.size()>0){
    	   return lst_data.get(0);
       }
		
       return null;
	}


	
	@Override
	public List<Map<String, Object>> getMessageOfNews(Message message) {
		String sql="select * from t_message  where type like 'news_%' and isRead=0 and isDelete=0 and receiveId='"+message.getReceiveId()+"' order by id desc limit 0,5";
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());

        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
       if(lst_data!=null&&lst_data.size()>0){
    	   return lst_data;
       }
		
       return null;
	}


	
	@Override
	public List<Map<String, Object>> getMessageByEmployee(Long  employee_id) {
		String sql="select * from t_message  where type not like 'news_%' and  isDelete=0 and to_employee_id="+employee_id+" order by id desc ";
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());

        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
       if(lst_data!=null&&lst_data.size()>0){
    	   return lst_data;
       }
		
       return null;
	}


	/* (non-Javadoc)
	 * @see com.cnpc.pms.messageModel.dao.MessageNewDao#getUnReadMessage(java.lang.Long)
	 */
	@Override
	public Integer getUnReadMessage(Long employee_id) {
		String sql="select count(id) as amount from t_message where isRead=0 and isDelete=0 and to_employee_id="+employee_id;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		return Integer.parseInt(query.uniqueResult().toString());
	}


	
	@Override
	public void deleteMessage(Long employee_id) {
		String sql="update t_message set isDelete =1 , deleteDate=NOW() where  to_employee_id="+employee_id;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.executeUpdate();
		
	}


	
	@Override
	public void deleteAllMessage(String employeeId) {
		
		String sql="update t_message set isDelete =1 , deleteDate=NOW() where receiveId='"+employeeId+"'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	}


	@Override
	public void deleteWorkRecord(Message message) {
		String sql="update t_message set isDelete =1 , deleteDate=NOW() where pk_id="+message.getPk_id()+" and type='work_record'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.executeUpdate();
		
	}



	@Override
	public Map<String, Object> getStorekeeper(Long store_id) {
		String sql = "select b.employeeId,b.name from t_store a INNER JOIN tb_bizbase_user b on a.skid = b.id and a.store_id="+store_id;
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());

        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
       if(lst_data!=null&&lst_data.size()>0){
    	   return lst_data.get(0);
       }
		
       return null;
	}


	
	@Override
	public List<Map<String, Object>> queryCityInfo(Long employeeId) {
		String sql="select * from t_dist_city where status=0 and  pk_userid="+employeeId;
		SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession().createSQLQuery(sql.toString());

        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	    return lst_data;   
	}


	
	@Override
	public List<Map<String, Object>> queryStoreByCity(String cityInfo) {
		String sql = "select a.store_id from t_store a  inner join (select cityname from t_dist_citycode where citycode in("+cityInfo+")) b on a.city_name =b.cityname and a.status=0";
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());

		//获得查询数据
	    List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	    return lst_data; 
	}


	
	@Override
	public List<Map<String, Object>> queryMessageByStoreKeeperId(String storeKeeperId) {
		String sql = "SELECT * FROM t_message WHERE receiveId='"+storeKeeperId+"' ORDER BY isRead ASC,create_time DESC limit 5";
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());
		//获得查询数据
	    List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	    return lst_data; 
	}
	
	
	//点击显示更多消息列表 
	@Override
	public List<Map<String, Object>> queryMoreMessageByStoreKeeperId(String content,String storeKeeperId, PageInfo pageInfo) {
		String sqlwhere = "";
		if(content!=null&&content.length()>0){
			sqlwhere+=" and content like '%"+content+"%'";
		}
		String sql = "SELECT * FROM t_message WHERE isDelete=0 and receiveId='"+storeKeeperId+"' "+sqlwhere+" ORDER BY isRead ASC,create_time DESC";
		//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
        pageInfo.setTotalRecords(query.list().size());
        //获得查询数据
        List<Map<String, Object>> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(
                        pageInfo.getRecordsPerPage()
                                * (pageInfo.getCurrentPage() - 1))
                .setMaxResults(pageInfo.getRecordsPerPage()).list();
        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }
        return (List<Map<String,Object>>)lst_data;
	}
	
	
	
	
	
	
	
	
	
	@Override
	public void updateMessageReadById(Long id){
		if(id!=null){
			String sql = "UPDATE t_message SET isRead=1 WHERE id ="+id;
			SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession().createSQLQuery(sql.toString());
			int rt = query.executeUpdate();
		}
	}
	
}
