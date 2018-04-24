package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;


import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.ExpressDao;
import com.cnpc.pms.personal.entity.Humanresources;

public class ExpressDaoImpl extends BaseDAOHibernate implements ExpressDao {
	
	@Override
	public List<Map<String,Object>> queryExpressCount(String month) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT	t_express.store_id," +
				"(SELECT city_name FROM t_store WHERE t_store.store_id=t_express.store_id) AS cityName," +
				"(select name from t_store where t_store.store_id=t_express.store_id) as storename," +
				"(SELECT storeno FROM t_store WHERE t_store.store_id=t_express.store_id) AS storeNo," +
				"t_express.employee_name,t_express.employee_no,count(t_express.express_no) as express_count FROM	t_express");
		sbf.append(" WHERE  t_express.status=0 and (t_express.express_status<>0 or t_express.express_status is null)  ");
		if(month!=null&&month.length()>0){
			sbf.append(" AND DATE_FORMAT(t_express.express_date,'%Y-%m') = '"+month+"' ");
		}else{
			sbf.append(" AND 1=0 ");
		}
		sbf.append(" AND t_express.employee_no is not null ");
		sbf.append(" AND t_express.employee_no <> 'undefined' ");
		sbf.append(" AND t_express.employee_name <> 'undefined' ");
		sbf.append(" GROUP BY store_id,employee_no ORDER BY store_id");

		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sbf.toString());

		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
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


	/**
	 * 通过门店主键 和 日期查询快递信息
	 * @param storeId,month
	 * @param month
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryExpressInfoByStoreIdAndMonth(Long storeId, String month) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT	t_express.store_id," +
				"(SELECT city_name FROM t_store WHERE t_store.store_id=t_express.store_id) AS cityName," +
				"(select name from t_store where t_store.store_id=t_express.store_id) as storename," +
				"(SELECT storeno FROM t_store WHERE t_store.store_id=t_express.store_id) AS storeNo," +
				"t_express.employee_name,t_express.employee_no,count(t_express.express_no) as express_count FROM	t_express");
		sbf.append(" WHERE  t_express.status=0 and (t_express.express_status<>0 or t_express.express_status is null)  ");
		if(month!=null&&month.length()>0){
			sbf.append(" AND DATE_FORMAT(t_express.express_date,'%Y-%m') = '"+month+"' ");
		}else{
			sbf.append(" AND 1=0 ");
		}
		sbf.append(" AND t_express.employee_no is not null ");
		sbf.append(" AND t_express.employee_no <> 'undefined' ");
		sbf.append(" AND t_express.employee_name <> 'undefined' ");
		sbf.append(" AND store_id = ");
		sbf.append("'"+storeId+"'");
		sbf.append(" GROUP BY store_id,employee_no ORDER BY store_id");
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession()
				.createSQLQuery(sbf.toString());
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
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



	/**
	 * crm 查询当前登录国安侠a的 当月的  所有快递信息
	 * @param store_id
	 * @param employee_no
	 * @param pageInfo
	 * @return
	 */
	@Override
	public Map<String, Object> queryExpressByEmployeeNo(Long store_id,String employee_no,PageInfo pageInfo){
		String sqlfrom =" FROM"+
		" ("+
		"	SELECT"+
		"		t_express.id,"+
		"		t_express.express_no,"+
		"		t_express.create_time,"+
		"		t_express.employee_no,"+
		"		t_express.employee_name,"+
		"		t_express.rcv_name,"+
		"		t_express.rcv_address,"+
		"		t_express.express_company,"+
		"		t_express.rcv_tel,"+
		"		t_express.store_id"+
		"	FROM"+
		"		t_express"+
		"	WHERE"+
		"		t_express.express_no IS NOT NULL"+
		"	AND t_express.employee_no IS NOT NULL"+
		"	AND t_express. STATUS = 0"+
		"	AND ("+
		"		t_express.express_status <> 0"+
		"		OR t_express.express_status IS NULL"+
		"	)"+
		"	AND t_express.store_id ="+ store_id+
		" ) a"+
		" LEFT JOIN t_store ON t_store.store_id = a.store_id WHERE a.employee_no='"+employee_no+"' AND a.store_id="+store_id+" ORDER BY a.create_time DESC";
		
		String sqllist = "SELECT a.*, t_store. NAME AS storename "+sqlfrom;
		String sqlcount = "SELECT count(1) as totalcount "+sqlfrom;
		
		SQLQuery querycount = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlcount);
    	String countnum = querycount.uniqueResult().toString();
    	
    	Map<String, Object> maps = new HashMap<String, Object>();
    	//SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqllist);
        
       //获得查询数据
         List<Map<String, Object>> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
                .setMaxResults(pageInfo.getRecordsPerPage()).list();
        
        int pages = 0;
        if(countnum!="0"){
        	pages =(Integer.parseInt(countnum)-1)/10+1;
        }
        maps.put("data", lst_data);
        maps.put("totalpage", countnum);
        maps.put("pagenum", pages);
    	return maps;
	}

	
	
	
	
	 @Override
	    public Integer gettotalExpressCount(String employee_no) {
	        String sql = "select count(1) from t_express t "+
							"where t.status=0 and (t.express_status<>0 or t.express_status is null)  "+
							"AND t.employee_no='"+employee_no+"'";
	        SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession()
	                .createSQLQuery(sql);
	        return Integer.valueOf(query.uniqueResult().toString());
	    }
	
	
	 @Override
	    public Integer getTotalRelationCount(String employee_no) {
	        String sql = "select count(1)  from t_relation r LEFT JOIN t_customer c ON c.id = r.customer_id" +
	                " WHERE r.employee_no = '"+employee_no+"'";
	        SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession()
	                .createSQLQuery(sql);
	        return Integer.valueOf(query.uniqueResult().toString());
	    }
	
	 
	 
	 /**
	  * CRM店长 根据门店 查询 快递代送排序 图表的方法  
	  * @param store_id
	  * @param date
	  * @return
	  */
	 @Override
	 public List<Map<String, Object>> queryExpressListByStoreId(Long store_id,List<Humanresources> humanresources){
		 String sql="SELECT "+
					"		t.employee_no,t.employee_name,count(t.express_no) as count  "+  
					"	FROM "+
					"		t_express t  "+
					"	WHERE "+
					"		t. STATUS = 0 "+
					"	AND ( "+
					"		t.express_status <> 0 "+
					"		OR t.express_status IS NULL "+
					"	) "+
					"	AND DATE_FORMAT(t.express_date, '%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m')   "+
					"	AND t.store_id ="+store_id+ 
					"	AND t.employee_no is NOT NULL GROUP BY t.employee_no ORDER BY count(t.express_no) DESC ";
		 SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession()
	                .createSQLQuery(sql);
		 List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		 
		 List<Map<String, Object>> retMaps = new ArrayList<Map<String,Object>>();
		 if(humanresources!=null){
			 for(Humanresources h:humanresources){
				 Map<String, Object> map = new HashMap<String, Object>();
				 map.put("employee_no", h.getEmployee_no());
				 map.put("employee_name", h.getName());
				 if(lst_data!=null){
					 for(Map<String, Object> o:lst_data){
						 if(h.getName().equals(o.get("employee_name").toString())){
							 map.put("count", o.get("count"));
						 }
					 }
				 }
				 if(map.get("count")==null){
					 map.put("count",0);
				 }
				 retMaps.add(map);
			 }
		 }
		 return retMaps;
	 }




	
	@Override
	public List<Map<String, Object>> queryExpressList_CSZJ_QYJL(Long city_id, String employeeId,String role) {
		String whereStr= "";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select t.store_id,t.name,tbu.name as employeeName from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employeeId+"' and a.id = b.gmid and b.ctid="+city_id+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	on t.city_name  = t1.cityname"+
					" 	INNER JOIN tb_bizbase_user as tbu on t.skid = tbu.id";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select t.store_id,t.name,tbu.name as employeeName from t_store t left  join  tb_bizbase_user as tbu on t.skid = tbu.id  where rmid = (select id from tb_bizbase_user where employeeId='"+employeeId+"')";
		} 
		
		String sql="SELECT ifnull(temp.total,0) as total,ts.store_id,ts.name,ts.employeeName FROM  (select COUNT(1) as total ,d.store_id from ( "+
				" SELECT b.employee_no,a.store_id"+
				" FROM tb_bizbase_user AS a "+
				" INNER JOIN ("+whereStr+") c"+
				" ON a.store_id = c.store_id  AND a.disabledFlag = 1 AND a. NAME NOT LIKE '%测试%'"+
				" AND (a.pk_usergroup = 3224 OR a.employeeId IN ( SELECT employee_no FROM t_humanresources t1 INNER JOIN "+
				" ("+whereStr+") t2"+
				" ON t1.store_id=t2.store_id and t1.humanstatus=1 AND t1.zw = '副店长' AND t1.remark = '国安侠'))"+
				" INNER JOIN (select * from t_express where  status=0 and DATE_FORMAT(express_date,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m') AND (express_status <> 0 or express_status IS NULL)) AS b ON a.employeeId = b.employee_no "+
				" ) d"+
				" group by d.store_id) temp RIGHT JOIN ("+whereStr+") ts on ts.store_id = temp.store_id ORDER BY total desc limit 20";

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> queryExpressList_CSZJ_QYJL_before(Long city_id, String employee_id, String role,
			String q_date) {
		String whereStr="";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select t.store_id,t.name,t.skid from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employee_id+"' and a.id = b.gmid and b.ctid="+city_id+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	 on t.city_name  = t1.cityname";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select store_id,name,skid from t_store where rmid = (select id from tb_bizbase_user where employeeId='"+employee_id+"')";
		}
		
		String sql = "select  ifnull(b.amount,0) as  total,a.skid,a.store_id,a.name,tbu.name as employeeName  from ("+whereStr+") as a left join  t_before_date_express b on a.store_id = b.store_id  and  b.bind_date='"+q_date+"' left join tb_bizbase_user as tbu on a.skid = tbu.id order by total desc limit 20";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}




	
	@Override
	public List<Map<String, Object>> getExpressOfPrevMonthOfStore(String month) {
		String sql = "SELECT count(1) as amount,a.store_id,c.name as store_name,'"+month+"' as bind_date FROM tb_bizbase_user AS a"+
				" INNER JOIN t_store  c"+
				" on a.store_id = c.store_id  AND a.disabledFlag = 1 AND a. NAME NOT LIKE '%测试%'"+
				" AND (a.pk_usergroup = 3224 OR a.employeeId IN ( SELECT employee_no FROM t_humanresources t1 INNER JOIN "+
				" t_store t2"+
				" on t1.store_id=t2.store_id and t1.humanstatus=1 AND t1.zw = '副店长' AND t1.remark = '国安侠'))"+
				" INNER JOIN (select * from t_express where DATE_FORMAT(create_time,'%Y-%m')='"+month+"')  AS b ON a.employeeId = b.employee_no"+
				" GROUP BY a.store_id";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}




	
	@Override
	public Integer getAllExpressOfStore(String storeId) {
		 String sql="SELECT "+
					"		count(t.express_no) as total  "+  
					"	FROM "+
					"		t_express t  "+
					"	WHERE "+
					"		t. STATUS = 0 "+
					"	AND ( "+
					"		t.express_status <> 0 "+
					"		OR t.express_status IS NULL "+
					"	) "+
					
					"	AND t.store_id  in "+storeId+ 
					"	AND t.employee_no is NOT NULL ";
		 	SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			Integer result = query.uniqueResult()==null?0:Integer.parseInt(query.uniqueResult().toString());
			return result;
	}




	/**
	 * 城市总监级别查询快递信息
	 * @param month
	 * @return List<Map<String,Object>>
	 */
	@Override
	public List<Map<String, Object>> queryExpressCount_cityLevel(String month) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(" SELECT * FROM t_express te WHERE te.status=0 ");
		sbf.append(" AND te.status=0   ");
		if(month!=null&&month.length()>0){
			sbf.append(" AND DATE_FORMAT(te.express_date,'%Y-%m') = '"+month+"' ");
		}else{
			sbf.append(" AND 1=0 ");
		}
		sbf.append(" AND te.employee_no is not null ");
		sbf.append(" AND te.employee_no <> 'undefined' ");
		sbf.append(" AND te.employee_name <> 'undefined' ");
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession()
				.createSQLQuery(sbf.toString());
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
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


	/**
	 * 通過城市Id查詢該城市信息
	 * @param cityId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryExpressByCityId(Long cityId) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(" SELECT * FROM  t_dist_citycode ");
		if(cityId!=null&&!"".equals(cityId)){
			sbf.append(" WHERE id = '"+cityId+"'");
		}
		
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession()
				.createSQLQuery(sbf.toString());
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
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
