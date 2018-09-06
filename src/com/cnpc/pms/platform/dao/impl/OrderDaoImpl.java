package com.cnpc.pms.platform.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.dynamic.entity.EshopPurchaseDto;
import com.cnpc.pms.dynamic.entity.UserOperationStatDto;
import com.cnpc.pms.platform.dao.OrderDao;
import com.cnpc.pms.utils.ValueUtil;

/**
 * 接口数据库层实现类
 * Created by liuxiao on 2016/8/23 0023.
 */
public class OrderDaoImpl extends DAORootHibernate implements OrderDao {


	public Integer getOrderCount(String store_id,String employee_id,String year_month){
        /*if(year_month == null){
            year_month = "DATE_FORMAT(curdate(),'%Y-%m')";
        }else{
            year_month = "'"+year_month+"'";
        }
        int result = 0;
		String sql = "SELECT" +
                "       COUNT(DISTINCT tor.group_id) AS complete_count" +
                "       FROM t_order tor" +
                "      JOIN t_order_flow tof ON tor.id=tof.order_id" +
                "      AND tof.order_status='signed'" +
                "      AND DATE_FORMAT(tof.create_time,'%Y-%m') = "+year_month;
        if(ValueUtil.checkValue(store_id)){
            sql +="      AND tor.store_id = '"+store_id+"'";
        }
        if(ValueUtil.checkValue(employee_id)){
            sql +="      AND tor.employee_id = '"+employee_id+"'";
        }

        Session session = getHibernateTemplate().getSessionFactory().openSession();
        try{
            Query query = session.createSQLQuery(sql);
            result = Integer.valueOf(query.uniqueResult().toString());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }*/
        return 0;
	}

    @Override
    public Map<String, Object> queryOrderEmployeeCountByStore(PageInfo pageInfo, String store_id, String date_value) {
       /* if(date_value == null){
            date_value = "DATE_FORMAT(curdate(),'%Y-%m')";
        }else{
            date_value = "'"+date_value+"'";
        }
        String sql_header = "SELECT e.`name` AS employee_name,IFNULL(t.complete_count,0) AS complete_count ";
        String sql_from = "FROM t_employee e LEFT JOIN " +
                "(" +
                "   SELECT" +
                "       tor.employee_id," +
                "       tor.employee_name," +
                "       COUNT(DISTINCT tor.group_id) AS complete_count" +
                "   FROM t_order tor" +
                "   JOIN t_order_flow tof ON tor.id=tof.order_id" +
                "   AND tof.order_status='signed'" +
                "   AND DATE_FORMAT(tof.create_time,'%Y-%m') = "+date_value+"" +
                "   AND store_id = '"+store_id+"'" +
                "   AND tor.employee_name IS NOT NULL" +
                "   GROUP BY tor.employee_id" +
                ") t ON t.employee_id = e.id WHERE e.status = 0 AND e.store_id = '"+store_id+"' ";
        String sql_order = "ORDER BY complete_count DESC";
        String sql_count = "SELECT COUNT(1) ";
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        Map<String,Object> map_result = new HashMap<String,Object>();
        List<?> lst_data = null;
        try{
            SQLQuery countQuery = session.createSQLQuery(sql_count.concat(sql_from));
            pageInfo.setTotalRecords(Integer.valueOf(countQuery.list().get(0).toString()));

            SQLQuery query = session.createSQLQuery(sql_header.concat(sql_from).concat(sql_order));
            lst_data = query
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                    .setFirstResult(
                            pageInfo.getRecordsPerPage()
                                    * (pageInfo.getCurrentPage() - 1))
                    .setMaxResults(pageInfo.getRecordsPerPage()).list();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        if(lst_data == null){
            lst_data = new ArrayList<Map<String,Object>>();
        }
        map_result.put("pageinfo", pageInfo);
        map_result.put("header", "");
        map_result.put("data", lst_data);*/
        return null;
    }

    @Override
    public List<Map<String, Object>> getOrderEmployeeData(String store_id, String date_value) {
        /*if(date_value == null){
            date_value = "DATE_FORMAT(curdate(),'%Y-%m')";
        }else{
            date_value = "'"+date_value+"'";
        }
        String sql = "SELECT e.`name` AS employee_name,IFNULL(t.complete_count,0) AS complete_count "+
                "FROM t_employee e LEFT JOIN " +
                "(" +
                "   SELECT" +
                "       tor.employee_id," +
                "       tor.employee_name," +
                "       COUNT(DISTINCT tor.group_id) AS complete_count" +
                "   FROM t_order tor" +
                "   JOIN t_order_flow tof ON tor.id=tof.order_id" +
                "   AND tof.order_status='signed'" +
                "   AND DATE_FORMAT(tof.create_time,'%Y-%m') = "+date_value+
                "   AND store_id = '"+store_id+"'" +
                "   AND tor.employee_name IS NOT NULL" +
                "   GROUP BY tor.employee_id" +
                ") t ON t.employee_id = e.id WHERE e.status = 0 AND e.store_id = '"+store_id+"' "+
                "ORDER BY complete_count DESC";

        Session session = getHibernateTemplate().getSessionFactory().openSession();
        List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
        try{
            SQLQuery query = session.createSQLQuery(sql);
            List<?> lst_data = query
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            if(lst_data != null){
                for(Object obj : lst_data){
                    Map<String,Object> map_data = (Map<String,Object>)obj;
                    Map<String,Object> map_content = (Map<String,Object>)obj;
                    map_content.put("name",map_data.get("employee_name"));
                    map_content.put("value",map_data.get("complete_count"));
                    lst_result.add(map_content);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }*/
        return null;
    }
    
 
    
    /**
     * crm 首页根据划片信息查询 订单详情列表
     */
    @Override
    public Map<String, Object> queryOrderListByArea(String store_id,String area,PageInfo pageInfo){
    	// 查询片区 里的 所有服务 
    	/*String sql = "SELECT t.id,t.order_sn,t.customer_id,store_id,t.employee_name,t.placename,t.order_date,t.customer_name,t.mobilephone,concat(GROUP_CONCAT(t_order_item.eshop_pro_name),'') as eshop_pro_name from (SELECT "+
    	"	a.*,toa.placename,t_customer.mobilephone,t_customer.short_name as customer_name "+
    	"FROM	(		SELECT"+
    	"			tor.*,tof.create_time as order_date"+
    	"		FROM"+
    	"			t_order tor"+
    	"		JOIN t_order_flow tof ON tof.order_id = tor.id"+
    	//"		AND DATE_FORMAT(tof.create_time, '%Y-%m') = DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -0 MONTH),'%Y-%m') "+
    	"		AND tor.store_id = '"+store_id+"'"+
    	"		AND tof.order_status = 'signed'"+
    	"    AND tor.employee_name is NOT NULL "+
    	"	) a"+
    	" LEFT JOIN t_order_address toa ON toa.id = a.order_address_id"+
    	" LEFT JOIN t_customer ON t_customer.id=a.customer_id "+
    	" WHERE"+ 
    	"	toa.placename IN ("+area+")) t JOIN t_order_item ON t_order_item.order_id = t.id  GROUP BY t.order_sn ORDER BY t.order_date desc";
    	
    	String sqlcount = "select count(1) as totalcount from (SELECT "+
    	    	"	a.*,toa.placename,t_customer.short_name as customer_name "+
    	    	"FROM	(SELECT"+
    	    	"			tor.*,tof.create_time as order_date"+
    	    	"		FROM"+
    	    	"			t_order tor"+
    	    	"		JOIN t_order_flow tof ON tof.order_id = tor.id"+
    	    	//"		AND DATE_FORMAT(tof.create_time, '%Y-%m') =  DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -0 MONTH),'%Y-%m') "+
    	    	"		AND tor.store_id = '"+store_id+"'"+
    	    	"		AND tof.order_status = 'signed'"+
    	    	"    AND tor.employee_name is NOT NULL "+
    	    	"	) a"+
    	    	" LEFT JOIN t_order_address toa ON toa.id = a.order_address_id"+
    	    	" LEFT JOIN t_customer ON t_customer.id=a.customer_id "+
    	    	" WHERE"+
    	    	"	toa.placename IN ("+area+ ")) t JOIN t_order_item ON t_order_item.order_id = t.id  GROUP BY t.order_sn ORDER BY t.order_date desc";
    	Session session = getHibernateTemplate().getSessionFactory().openSession();
    	Map<String, Object> maps = new HashMap<String, Object>();
		try {
			SQLQuery querycount = session.createSQLQuery(sqlcount);
			String countnum = querycount.list().size()+"";
			//SQL查询对象
			String retSql = "select * from ("+sql+") z";
			//SQL查询对象
			SQLQuery query = session.createSQLQuery(retSql);
			//获得查询数据
			List<Map<String, Object>> lst_data  = query
			        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
			        .setFirstResult(pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
			        .setMaxResults(pageInfo.getRecordsPerPage()).list();
			
			int pages = 0;
			if(countnum!="0"){
				pages = (Integer.parseInt(countnum)-1)/10+1;
			}
			maps.put("data", lst_data);
			maps.put("totalpage", countnum);
			maps.put("pagenum", pages);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			session.close();
		}*/
    	return null;
    }
    
    
    
    
    /**
     * crm 根据员工编号 查询 订单详情列表
     */
    @Override
    public Map<String, Object> queryOrderListByEmployeeNo(String store_id,String employee_no,String area_names,PageInfo pageInfo){
    	
    	// 查询片区 里的 所有服务 
    	/*String sqlwhere =" from (SELECT "+
    	"	a.*,toa.placename,t_customer.mobilephone,t_customer.short_name as customer_name "+
    	"FROM	(		SELECT"+
    	"			tor.*,tof.create_time as order_date"+
    	"		FROM"+
    	"			t_order tor"+
    	"		JOIN t_order_flow tof ON tof.order_id = tor.id"+
    	//"		AND DATE_FORMAT(tof.create_time, '%Y-%m') = DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -0 MONTH),'%Y-%m') "+
    	"		AND tor.store_id = '"+store_id+"'"+
    	"		AND tof.order_status = 'signed'"+
    	"    AND tor.employee_name like '%"+ employee_no + "'"+
    	"	) a"+
    	" LEFT JOIN t_order_address toa ON toa.id = a.order_address_id"+
    	" LEFT JOIN t_customer ON t_customer.id=a.customer_id "+
    	" WHERE"+ 
    	"	1=1 ) t JOIN t_order_item ON t_order_item.order_id = t.id GROUP BY t.order_sn  ORDER BY t.order_date desc";
    	
    	String sql="SELECT t.id,t.order_sn,t.customer_id,store_id,t.employee_name,t.order_date,t.customer_name,t.mobilephone,concat(GROUP_CONCAT(t_order_item.eshop_pro_name),'') as eshop_pro_name  "+ sqlwhere;
    	String sqlcount = "select count(1) as totalcount "+ sqlwhere;
    	
    	Session session = getHibernateTemplate().getSessionFactory().openSession();
    	
    	Map<String, Object> maps = new HashMap<String, Object>();
		try {
			SQLQuery querycount = session.createSQLQuery(sqlcount);
			String countnum = querycount.list().size()+"";
			//SQL查询对象
			String retSql = "select * from ("+sql+") z";
			SQLQuery query = session.createSQLQuery(retSql);
			//获得查询数据
			List<Map<String, Object>> lst_data  = query
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
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			session.close();
		}*/
    	return null;
    }
    
    
    /**
     * 查询统计图 近四个月的数据
     * @return
     */
    @Override
    public List<Map<String, Object>> queryOrderFourMonth(String store_id,String areaInfo){
    	/*List<String> curMonths = new ArrayList<String>();
    	excnowdate(curMonths);
    	
    	String sql ="select b.curmonth as month,(CASE WHEN s.totalcount is null THEN 0 ELSE s.totalcount END) as totalcount  from " +
    			" (SELECT '"+curMonths.get(0)+"' as curmonth " +
    			" UNION " +
    			" SELECT '"+curMonths.get(1)+"' as curmonth" +
    			" UNION " +
    			" SELECT '"+curMonths.get(2)+"' as curmonth" +
    			" UNION " +
    			" SELECT '"+curMonths.get(3)+"' as curmonth) b LEFT JOIN (select" +
    			" 		DATE_FORMAT(t.order_date,'%Y-%m') as month ," +
    			" 		count(1) as totalcount  " +
    			" 		from" +
    			" 		(SELECT" +
    			" 				a.order_date," +
    			" 				toa.placename," +
    			" 				t_customer.short_name as customer_name    " +    
    			" 				FROM" +
    			" 				(SELECT" +
    			"        tor.order_address_id,tor.customer_id," +
    			"        tof.create_time as order_date  " +
    			"    FROM" +
    			"        t_order tor  " +
    			"    JOIN" +
    			"        t_order_flow tof  " +
    			"            ON tof.order_id = tor.id  " +
    			"            AND DATE_FORMAT(tof.create_time," +
    			"        '%Y-%m') in('"+curMonths.get(0)+"','"+curMonths.get(1)+"','"+curMonths.get(2)+"','"+curMonths.get(3)+"')  " +
    			"        AND tor.store_id = '"+store_id+"'  " +              
    			"        AND tof.order_status = 'signed'  " +
    			"        AND tor.employee_name is NOT NULL  ) a " +
    			" LEFT JOIN" +
    			"    t_order_address toa " +
    			"        ON toa.id = a.order_address_id     " +   
    			" LEFT JOIN" +
    			"    t_customer " +
    			"        ON t_customer.id=a.customer_id  " +
    			" WHERE" +
    			"    toa.placename IN (" + areaInfo +
    			"    )" +
    			" ) t group by DATE_FORMAT(t.order_date,'%Y-%m')) s ON b.curmonth = s.month ";
    	
    	Session session = getHibernateTemplate().getSessionFactory().openSession();
    	
        //获得查询数据
		List<Map<String, Object>> lst_data  = null;
		try {
			SQLQuery query = session.createSQLQuery(sql);
			lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			session.close();
		}*/
    	
        return null;
    	    	
    }
    
    
    
    
    /**
     * APP查询统计图 近五个月的数据
     * @return
     */
    @Override
    public List<Map<String, Object>> queryOrderFiveMonth(String store_id,String areaInfo){
    	/*List<String> curMonths = new ArrayList<String>();
    	excnowdatefive(curMonths);
    	
    	String sql ="select b.curmonth as month,(CASE WHEN s.totalcount is null THEN 0 ELSE s.totalcount END) as totalcount, "+
				" (CASE	WHEN s.totalprice IS NULL THEN 0 ELSE	 s.totalprice  END "+
				" ) AS totalprice   from " +
    			" (SELECT '"+curMonths.get(0)+"' as curmonth " +
    			" UNION " +
    			" SELECT '"+curMonths.get(1)+"' as curmonth" +
    			" UNION " +
    			" SELECT '"+curMonths.get(2)+"' as curmonth" +
    			" UNION " +
    			" SELECT '"+curMonths.get(3)+"' as curmonth" +
    			" UNION " +
    			" SELECT '"+curMonths.get(4)+"' as curmonth) b LEFT JOIN (select" +
    			" 		DATE_FORMAT(t.order_date,'%Y-%m') as month ," +
    			" 		count(1) as totalcount,sum(t.payable_price) as totalprice  " +
    			" 		from" +
    			" 		(SELECT" +
    			" 				a.order_date," +
    			" 				toa.placename," +
    			" 				a.payable_price," +
    			" 				t_customer.short_name as customer_name    " +    
    			" 				FROM" +
    			" 				(SELECT" +
    			"        tor.order_address_id,tor.customer_id,tor.payable_price," +
    			"        tof.create_time as order_date  " +
    			"    FROM" +
    			"        t_order tor  " +
    			"    JOIN" +
    			"        t_order_flow tof  " +
    			"            ON tof.order_id = tor.id  " +
    			"            AND DATE_FORMAT(tof.create_time," +
    			"        '%Y-%m') in('"+curMonths.get(0)+"','"+curMonths.get(1)+"','"+curMonths.get(2)+"','"+curMonths.get(3)+"','"+curMonths.get(4)+"')  " +
    			"        AND tor.store_id = '"+store_id+"'  " +              
    			"        AND tof.order_status = 'signed'  " +
    			"        AND tor.employee_name is NOT NULL  ) a " +
    			" LEFT JOIN" +
    			"    t_order_address toa " +
    			"        ON toa.id = a.order_address_id     " +   
    			" LEFT JOIN" +
    			"    t_customer " +
    			"        ON t_customer.id=a.customer_id  " +
    			" WHERE" +
    			"    toa.placename IN (" + areaInfo +
    			"    )" +
    			" ) t group by DATE_FORMAT(t.order_date,'%Y-%m')) s ON b.curmonth = s.month ";
    	
    	
    	Session session = getHibernateTemplate().getSessionFactory().openSession();
    	
        //获得查询数据
		List<Map<String, Object>> retList  = new ArrayList<Map<String,Object>>();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			
			if(lst_data!=null){
				for(Map<String, Object> o:lst_data){
					String month = Integer.parseInt(o.get("month").toString().split("-")[1].toString())+"月";
					o.put("month", month);
					retList.add(o);
				}
			}
			
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			session.close();
		}*/
    	
        return null;
    	    	
    }
    
    
    /**
     * 根据订单sn编号 查询明细信息 
     * @param order_sn
     * @return
     */
    @Override
    public Map<String, Object> queryOrderInfoBySN(String order_sn){
    	String sql ="SELECT "+
    			"				CONCAT(a.id,'') as id,"+
				"				a.order_sn,"+
				"				CONCAT(a.customer_id,'') as customer_id,"+
				"				CONCAT(a.order_address_id,'') as order_address_id,"+
				"				a.total_quantity,"+
					"				a.trading_price,"+
				"				a.payable_price,"+
				"				CONCAT(a.order_status,'') as order_status,"+
				"				CONCAT(a.order_type,'') as order_type,"+
				"				a.employee_name,"+
				"				a.employee_phone,"+
				"				a.appointment_start_time,"+
				"				a.create_time,"+
				"				a.appointment_end_time,"+
				"				a.seller_remark,"+
					"		toa.address,"+
					"		toa.name as short_name,"+
					"		toa.mobilephone,"+
					"		concat("+
					"			GROUP_CONCAT(ti.eshop_pro_name),"+
					"			''"+
					"		) AS eshop_pro_name"+
					"	FROM"+
					"		("+
					"			SELECT"+
					"				t_order.id,"+
					"				t_order.order_sn,"+
					"				t_order.customer_id,"+
					"				t_order.order_address_id,"+
					"				t_order.total_quantity,"+
					"				t_order.trading_price,"+
					"				t_order.payable_price,"+
					"				t_order.order_status,"+
					"				t_order.order_type,"+
					"				t_order.employee_name,"+
					"				t_order.employee_phone,"+
					"				t_order.appointment_start_time,"+
					"				t_order.create_time,"+
					"				t_order.appointment_end_time,"+
					"				t_order.seller_remark"+
					"			FROM"+
					"				t_order"+
					"			WHERE"+
					"				t_order.order_sn = '"+order_sn+"'"+
					"		) a"+
					"	LEFT JOIN t_order_address toa ON toa.id = a.order_address_id"+
					
					"	LEFT JOIN t_order_item ti ON ti.order_id = a.id"+
					"	GROUP BY"+
					"		a.order_sn";
    	
    	Session session = getHibernateTemplate().getSessionFactory().openSession();
        //获得查询数据
    	Map<String, Object> order_obj = null;
		try { 
			SQLQuery query = session.createSQLQuery(sql);
			List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			if(lst_data!=null&&lst_data.size()>0){
				order_obj = (Map<String, Object>) lst_data.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			session.close();
		}
    	return order_obj;
    }
    
    
    @Override
    public List<Map<String, Object>> queryOrderItemInfoById(String order_id){
    	List<Map<String, Object>> lst_data = null;
    	List<Map<String, Object>> ret_date = new ArrayList<Map<String,Object>>();
    	String sql = "SELECT * from t_order_item WHERE t_order_item.order_id='"+order_id+"'";
    	if(order_id!=null&&order_id.length()>0){
    		Session session = getHibernateTemplate().getSessionFactory().openSession();
        	try { 
    			SQLQuery query = session.createSQLQuery(sql);
    			lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    			if(lst_data!=null&&lst_data.size()>0){
    				for(Map<String, Object> obj:lst_data){
    					String img_url = obj.get("first_url")==null?"":obj.get("first_url").toString();
    					if(img_url!=null&&img_url.length()>0){
    						String url = "https://imgcdn.guoanshequ.com/"+img_url;
    						obj.put("first_url", url);
    					}
    					ret_date.add(obj);
    				}
    			}
    		} catch (HibernateException e) {
    			e.printStackTrace();
    		}finally{
    			session.close();
    		}
    	}
    	return ret_date;
    }

    
    
    
    
    
    
    
    //取得 当前月的前四个月
    private static void excnowdate(List<String> monthList) {
    	 Calendar calendar = Calendar.getInstance();
		  int year = calendar.get(Calendar.YEAR);
		  int month = calendar.get(Calendar.MONTH)+1;
		  String month1="";
		  String month2="";
		  String month3="";
		  String month4="";
		  if(month<4){
			  if(month==3){
				  month1 = year+"-"+"0"+month;
				  month2 = year+"-"+"0"+(month-1);
				  month3 = year+"-"+"0"+(month-2);
				  month4 = (year-1)+"-"+"12";
			  }else if(month == 2){
				  month1 = year+"-"+"0"+month;
				  month2 = year+"-"+"0"+(month-1);
				  month3 = (year-1)+"-"+"12";
				  month4 = (year-1)+"-"+"11";
			  }else if(month == 1){
				  month1 = year+"-"+"0"+month;
				  month2 = (year-1)+"-"+"12";
				  month3 = (year-1)+"-"+"11";
				  month4 = (year-1)+"-"+"10";
			  }
		  }else{
			  month1 = "0"+month;
			  month2 = "0"+(month-1);
			  month3 = "0"+(month-2);
			  month4 = "0"+(month-3);
			  month1=year+"-"+month1.substring(month1.length()-2,month1.length());
			  month2=year+"-"+month2.substring(month2.length()-2,month2.length());
			  month3=year+"-"+month3.substring(month3.length()-2,month3.length());
			  month4=year+"-"+month4.substring(month4.length()-2,month4.length());
		  }
		  monthList.add(month4);
		  monthList.add(month3);
		  monthList.add(month2);
		  monthList.add(month1);
	}
    
    
    
    
  //取得 当前月的前五个月
    private static void excnowdatefive(List<String> monthList) {
    	Calendar calendar = Calendar.getInstance();
		  int year = calendar.get(Calendar.YEAR);
		  int month = calendar.get(Calendar.MONTH)+1;
		  String month1="";
		  String month2="";
		  String month3="";
		  String month4="";
		  String month5="";
		  if(month<5){
			  if(month == 4){
				  month1 = year+"-"+"0"+month;
				  month2 = year+"-"+"0"+(month-1);
				  month3 = year+"-"+"0"+(month-2);
				  month4 = year+"-"+"0"+(month-3);
				  month5 = (year-1)+"-"+"12";
			  }else if(month==3){
				  month1 = year+"-"+"0"+month;
				  month2 = year+"-"+"0"+(month-1);
				  month3 = year+"-"+"0"+(month-2);
				  month4 = (year-1)+"-"+"12";
				  month5 = (year-1)+"-"+"11";
			  }else if(month == 2){
				  month1 = year+"-"+"0"+month;
				  month2 = year+"-"+"0"+(month-1);
				  month3 = (year-1)+"-"+"12";
				  month4 = (year-1)+"-"+"11";
				  month5 = (year-1)+"-"+"10";
			  }else if(month == 1){
				  month1 = year+"-"+"0"+month;
				  month2 = (year-1)+"-"+"12";
				  month3 = (year-1)+"-"+"11";
				  month4 = (year-1)+"-"+"10";
				  month5 = (year-1)+"-"+"09";
			  }
		  }else{
			  month1 = "0"+month;
			  month2 = "0"+(month-1);
			  month3 = "0"+(month-2);
			  month4 = "0"+(month-3);
			  month5 = "0"+(month-4);
			  month1=year+"-"+month1.substring(month1.length()-2,month1.length());
			  month2=year+"-"+month2.substring(month2.length()-2,month2.length());
			  month3=year+"-"+month3.substring(month3.length()-2,month3.length());
			  month4=year+"-"+month4.substring(month4.length()-2,month4.length());
			  month5=year+"-"+month5.substring(month5.length()-2,month5.length());
		  }
		  monthList.add(month5);
		  monthList.add(month4);
		  monthList.add(month3);
		  monthList.add(month2);
		  monthList.add(month1);
	}
    
    
    @Override
    public Integer gettotalOrderCount(String store_id,String employee_id){
       /* int result = 0;
		String sql = "SELECT" +
                "       COUNT(DISTINCT tor.group_id) AS complete_count" +
                "       FROM t_order tor" +
                "      JOIN t_order_flow tof ON tor.id=tof.order_id" +
                "      AND tof.order_status='signed'";
        if(ValueUtil.checkValue(store_id)){
            sql +="      AND tor.store_id = '"+store_id+"'";
        }
        if(ValueUtil.checkValue(employee_id)){
            sql +="      AND tor.employee_id = '"+employee_id+"'";
        }

        Session session = getHibernateTemplate().getSessionFactory().openSession();
        try{
            Query query = session.createSQLQuery(sql);
            result = Integer.valueOf(query.uniqueResult().toString());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }*/
        return null;
	}
   
    
    /**
     * CRM店长 根据门店 查询 送单量排序  图表的方法  
     * @param store_id
     * @return
     */
    @Override
    public List<Map<String, Object>> queryOrderCountByStoreId(String store_id){
    	/*String sql = "select rt.employee_name,count(1) as count from (SELECT "+
					"		t.employee_name,count(1) as count   "+
					"	FROM "+
					"		( "+
					"			SELECT "+
					"				a.*" +
					"				, toa.placename, "+
					"				t_customer.mobilephone, "+
					"				t_customer.short_name AS customer_name "+
					"			FROM "+
					"				( "+
					"					SELECT "+
					"						tor.* " +
					"					, tof.create_time AS order_date "+
					"					FROM "+
					"						t_order tor "+
					"					JOIN t_order_flow tof ON tof.order_id = tor.id "+
					"					AND tor.store_id = '"+store_id+"' "+
					"					AND tof.order_status = 'signed'  "+
					"                   AND DATE_FORMAT(tof.create_time, '%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m')  "+
					//"                   AND DATE_FORMAT(tof.create_time, '%Y-%m')='2017-03' "+
					"				) a "+
					"			LEFT JOIN t_order_address toa ON toa.id = a.order_address_id "+
					"			LEFT JOIN t_customer ON t_customer.id = a.customer_id "+
					"			WHERE "+
					"				1 = 1 "+
					"		) t "+
					"	JOIN t_order_item ON t_order_item.order_id = t.id "+
					"	GROUP BY "+
					"		t.order_sn) rt GROUP BY rt.employee_name HAVING rt.employee_name is NOT NULL ";
    	

    	 Session session = getHibernateTemplate().getSessionFactory().openSession();
    	 List<Map<String, Object>> lst_data = null;
         try{
             Query query = session.createSQLQuery(sql);
             lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
         }catch (Exception e){
             e.printStackTrace();
         }finally {
             session.close();
         }*/
		 return null;
		 
    }
    
    
    
    
    /**
     * CRM店长图表 根据门店及片区，查询所有订单数据
     * @param store_id
     * @param area_name
     */
    @Override
    public List<Map<String, Object>> queryOrderListByArea(String store_id,String area_name){
       /* String sql ="    SELECT "+
				    "	z.placename,count(1) as ordercount,SUM(z.payable_price) as total_price  "+
				    " FROM "+
				    "	( "+
				    "		SELECT "+
				    "			t.id, "+
				    "			t.order_sn, "+
				    "			t.customer_id, "+
				    "			store_id, "+
				    "			t.employee_name, "+
				    "			t.placename, "+
				    "			t.order_date, "+
				    "			t.customer_name, "+
				    "			t.mobilephone, "+
				    "			concat( "+
				    "				GROUP_CONCAT( "+
				    "					t_order_item.eshop_pro_name "+
				    "				), "+
				    "				'' "+
				    "			) AS eshop_pro_name, "+
				    "			t.payable_price  "+
				    "		FROM "+
				    "			( "+
				    "				SELECT "+
				    "					a.*, toa.placename "+
				    "					t_customer.mobilephone, "+
				    "					t_customer.short_name AS customer_name "+
				    "				FROM "+
				    "					( "+
				    "						SELECT "+
				    "							tor.*, tof.create_time AS order_date "+
				    "						FROM "+
				    "							t_order tor "+
				    "						JOIN t_order_flow tof ON tof.order_id = tor.id "+
				    "						AND tor.store_id = '"+store_id+"' "+
				    "						AND tof.order_status = 'signed' "+
				    //"                   	AND DATE_FORMAT(tof.create_time, '%Y-%m')='2017-03'  "+
				    "                   	AND DATE_FORMAT(tof.create_time, '%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m')  "+
				    "						AND tor.employee_name IS NOT NULL "+
				    "					) a "+
				    "				LEFT JOIN t_order_address toa ON toa.id = a.order_address_id "+
				    "				LEFT JOIN t_customer ON t_customer.id = a.customer_id "+
				    "				WHERE "+
				    "					toa.placename IN ( "+
				    						area_name +
				    "					) "+
				    "			) t "+
				    "		JOIN t_order_item ON t_order_item.order_id = t.id "+
				    "		GROUP BY "+
				    "			t.order_sn "+
				    "		ORDER BY "+
				    "			t.order_date DESC "+
				    "	) z  GROUP BY z.placename  ";
        
        Session session = getHibernateTemplate().getSessionFactory().openSession();
   	 	List<Map<String, Object>> lst_data = null;
        try{
            Query query = session.createSQLQuery(sql);
            lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }*/
        return null;
    }
    
    
    /**
     * 店长CRM 根据门店ID取得今年的 每个月的订单数及金额
     * @param store_id
     */
    @Override
    public List<Map<String, Object>> queryOrderCountByMonthStoreId(String store_id){
    	/*String sql = "SELECT m.month,case when o.totalcount is NULL then 0 else o.totalcount END as total_count,ROUND(CASE when o.total_price is null then 0 else o.total_price end,0) as total_price from ( "+
					execSqlMonth() +
					"	) m LEFT JOIN (SELECT "+
					"			DATE_FORMAT(t.order_date, '%Y-%m') AS MONTH, "+
					"			count(1) AS totalcount, "+
					"			SUM(t.payable_price) as total_price  "+
					"		FROM "+
					"			( "+
					"				SELECT "+
					"					a.order_date, "+
					"					a.payable_price, "+
					"					toa.placename, "+
					"					t_customer.short_name AS customer_name "+
					"				FROM "+
					"					( "+
					"						SELECT "+
					"							tor.payable_price, "+
					"							tor.order_address_id, "+
					"							tor.customer_id, "+
					"							tof.create_time AS order_date "+
					"						FROM "+
					"							t_order tor "+
					"						JOIN t_order_flow tof ON tof.order_id = tor.id "+
					"						AND DATE_FORMAT(tof.create_time, '%Y-%m') IN ("+execMonth()+") "+
					"						AND tor.store_id = '"+store_id+"' "+
					"						AND tof.order_status = 'signed' "+
					"						AND tor.employee_name IS NOT NULL "+
					"					) a "+
					"				LEFT JOIN t_order_address toa ON toa.id = a.order_address_id "+
					"				LEFT JOIN t_customer ON t_customer.id = a.customer_id "+
					"				WHERE "+
					"					1 = 1  "+
					"			) t "+
					"		GROUP BY "+
					"			DATE_FORMAT(t.order_date, '%Y-%m') ) o ON o.MONTH = m.month ";
    	
    	Session session = getHibernateTemplate().getSessionFactory().openSession();
   	 	List<Map<String, Object>> lst_data = null;
        try{
            Query query = session.createSQLQuery(sql);
            lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }*/
		
		return null;
    }
    
    
    
    
    /**
     * APP个人中心 不分片 查询订单记录
     */
    @Override
    public List<Map<String, Object>> queryOrderFiveMonthOrderApp(String store_id,String employee_no){
    	/*List<String> curMonths = new ArrayList<String>();
    	excnowdatefive(curMonths);
    	
    	String sql ="select b.curmonth as month,(CASE WHEN s.totalcount is null THEN 0 ELSE s.totalcount END) as totalcount, "+
				" (CASE	WHEN s.totalprice IS NULL THEN 0 ELSE	 s.totalprice  END "+
				" ) AS totalprice   from " +
    			" (SELECT '"+curMonths.get(0)+"' as curmonth " +
    			" UNION " +
    			" SELECT '"+curMonths.get(1)+"' as curmonth" +
    			" UNION " +
    			" SELECT '"+curMonths.get(2)+"' as curmonth" +
    			" UNION " +
    			" SELECT '"+curMonths.get(3)+"' as curmonth" +
    			" UNION " +
    			" SELECT '"+curMonths.get(4)+"' as curmonth) b LEFT JOIN (select" +
    			" 		DATE_FORMAT(t.order_date,'%Y-%m') as month ," +
    			" 		count(1) as totalcount,sum(t.payable_price) as totalprice  " +
    			" 		from" +
    			" 		(SELECT" +
    			" 				a.order_date," +
    			" 				toa.placename," +
    			" 				a.payable_price," +
    			" 				t_customer.short_name as customer_name    " +    
    			" 				FROM" +
    			" 				(SELECT" +
    			"        tor.order_address_id,tor.customer_id,tor.payable_price," +
    			"        tof.create_time as order_date  " +
    			"    FROM" +
    			"        t_order tor  " +
    			"    JOIN" +
    			"        t_order_flow tof  " +
    			"            ON tof.order_id = tor.id  " +
    			"            AND DATE_FORMAT(tof.create_time," +
    			"        '%Y-%m') in('"+curMonths.get(0)+"','"+curMonths.get(1)+"','"+curMonths.get(2)+"','"+curMonths.get(3)+"','"+curMonths.get(4)+"')  " +
    			"        AND tor.store_id = '"+store_id+"'  " +              
    			"        AND tof.order_status = 'signed'  " +
    	    	"    	 AND tor.employee_name like '%"+ employee_no + "'"+
    			"        AND tor.employee_name is NOT NULL  ) a " +
    			" LEFT JOIN" +
    			"    t_order_address toa " +
    			"        ON toa.id = a.order_address_id     " +   
    			" LEFT JOIN" +
    			"    t_customer " +
    			"        ON t_customer.id=a.customer_id  " +
    			" WHERE" +
    			"    1=1 " +
    			" ) t group by DATE_FORMAT(t.order_date,'%Y-%m')) s ON b.curmonth = s.month ";
    	
    	
    	Session session = getHibernateTemplate().getSessionFactory().openSession();
    	
        //获得查询数据
		List<Map<String, Object>> retList  = new ArrayList<Map<String,Object>>();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			
			if(lst_data!=null){
				for(Map<String, Object> o:lst_data){
					String month = Integer.parseInt(o.get("month").toString().split("-")[1].toString())+"月";
					o.put("month", month);
					retList.add(o);
				}
			}
			
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			session.close();
		}*/
    	
        return null;
    	    	
    }
    
    
    
    
    
    
    
    
    
    
    public String execMonth(){
    	StringBuffer  ret = new StringBuffer();
    	Calendar cal = Calendar.getInstance();
    	int year = cal.get(Calendar.YEAR);
    	int month = cal.get(Calendar.MONTH) + 1;
    	ret.append("'"+year+"-01'");
    	for(int i=2;i<=month;i++){
    		String tm = "0"+i;
 	        String ym ="'"+ year+"-"+tm.substring(tm.length()-2,tm.length())+"'";
 	        ret.append(","+ym);
    	}
    	System.out.println(ret.toString());
    	return ret.toString();
	 }
	 
	 public String execSqlMonth(){
	    	StringBuffer  ret = new StringBuffer();
	    	Calendar cal = Calendar.getInstance();
	    	int year = cal.get(Calendar.YEAR);
	    	int month = cal.get(Calendar.MONTH) + 1;
	    	ret.append("SELECT '"+year+"-01' as month ");
	    	for(int i=2;i<=month;i++){
	    		String tm = "0"+i;
	 	        String ym = " SELECT '"+year+"-"+tm.substring(tm.length()-2,tm.length())+"' AS month ";
	 	        ret.append("UNION"+ym);
	    	}
	    	System.out.println(ret.toString());
	    	return ret.toString();
      }

	
	@Override
	public List<Map<String, Object>> queryOrderListOfStore_CSZJ_QYJL(Object store) {
		
		
		/*String sql ="    SELECT  t.store_id,count(1) as ordercount,round(SUM(t.payable_price),0) as total_price  "+
				    "	 FROM	( "+
				    "		SELECT 	tor.order_sn,tor.id,tor.store_id,tor.payable_price, tof.create_time AS order_date"+
				    "		FROM t_order tor "+
				    "		JOIN t_order_flow tof ON tof.order_id = tor.id "+
				    "		AND tof.order_status = 'signed' "+
				    //" 	AND DATE_FORMAT(tof.create_time, '%Y-%m')='2017-03'  "+
				    "   	AND DATE_FORMAT(tof.create_time, '%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m')  "+
				    "		AND tor.employee_name IS NOT NULL "+
				    " 		AND tor.store_id IN "+store+
				    "	) t  GROUP BY t.store_id order by ordercount desc limit 20";

     
     Session session = getHibernateTemplate().getSessionFactory().openSession();
	 	List<Map<String, Object>> lst_data = null;
     try{
        SQLQuery query = session.createSQLQuery(sql);
         lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
     }catch (Exception e){
         e.printStackTrace();
     }finally {
         session.close();
     }*/
     
     
     return null;
	}

	
	@Override
	public List<Map<String, Object>> queryOrderCountOfMonth_CSZJ_QYJL(Object store) {

		/*String sql ="    SELECT  DATE_FORMAT(t.order_date, '%c月') AS order_date,count(1) as ordercount,round(SUM(t.payable_price),0) as total_price  "+
				    "	 FROM	( "+
				    "		SELECT 	tor.order_sn,tor.id,tor.store_id,tor.payable_price, tof.create_time AS order_date"+
				    "		FROM t_order tor "+
				    "		JOIN t_order_flow tof ON tof.order_id = tor.id "+
				    "		AND tof.order_status = 'signed' "+
				    //" 	AND DATE_FORMAT(tof.create_time, '%Y-%m')='2017-03'  "+
				    "   	AND YEAR(tof.create_time)=YEAR(CURDATE())  "+
				    "		AND tor.employee_name IS NOT NULL "+
				    " 		AND tor.store_id IN "+store+
				    "	) t  GROUP BY DATE_FORMAT(t.order_date, '%Y-%m')";

     
     Session session = getHibernateTemplate().getSessionFactory().openSession();
	 	List<Map<String, Object>> lst_data = null;
     try{
        SQLQuery query = session.createSQLQuery(sql);
         lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
     }catch (Exception e){
         e.printStackTrace();
     }finally {
         session.close();
     }*/
     return null;
	}

	
	@Override
	public Map<String, Object> getAllOrderOfStore(String storeId) {
		/*String sql ="    SELECT  count(1) as total,round(SUM(t.payable_price),0) as total_price  "+
			    "	 FROM	( "+
			    "		SELECT 	tor.order_sn,tor.id,tor.store_id,tor.payable_price, tof.create_time AS order_date"+
			    "		FROM t_order tor "+
			    "		JOIN t_order_flow tof ON tof.order_id = tor.id "+
			    "		AND tof.order_status = 'signed' "+
			    //" 	AND DATE_FORMAT(tof.create_time, '%Y-%m')='2017-03'  "+
			    "		AND tor.employee_name IS NOT NULL "+
			    " 		AND tor.store_id IN "+storeId+
			    "	) t ";

		 Session session = getHibernateTemplate().getSessionFactory().openSession();
		 List<Map<String, Object>> lst_data = null;
	     try{
	       SQLQuery query = session.createSQLQuery(sql);
	       lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	     }catch (Exception e){
	         e.printStackTrace();
	     }finally {
	         session.close();
	     }
	    
		return (lst_data==null||lst_data.size()==0)?null:lst_data.get(0);*/
		return null;
	}

	
	@Override
	public Map<String, Object> getOrderFlow(String order_id, String status) {
		/*String sql ="select * from t_order_flow where order_id='"+order_id+"' and order_status='"+status+"'";
		 Session session = getHibernateTemplate().getSessionFactory().openSession();
		 List<Map<String, Object>> lst_data = null;
	     try{
	       SQLQuery query = session.createSQLQuery(sql);
	       lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	     }catch (Exception e){
	         e.printStackTrace();
	     }finally {
	         session.close();
	     }
	    
		return (lst_data==null||lst_data.size()==0)?null:lst_data.get(0);*/
		return null;
	}

	


	
	@Override
	public List<Map<String, Object>> queryOrderSetInterval(String career_group,String nowDate){
		String sql = "select id,store_id, trading_price,DATE_FORMAT(df_signed_time,'%Y-%m-%d') as create_date  from df_order_signed_daily where dep_name like '%"+career_group+"%' and DATE_FORMAT(df_signed_time,'%Y-%m-%d') ='"+nowDate+"'";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	 	List<Map<String, Object>> lst_data = null;
	     try{
	        SQLQuery query = session.createSQLQuery(sql);
	         lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	     }catch (Exception e){
	         e.printStackTrace();
	     }finally {
	         session.close();
	     }
	     return lst_data;
	}

	
	@Override
	public Map<String, Object> queryOrderOfArea(String employee_no,PageInfo pageInfo,String order_sn) {
		String whereStr = "";
		if(order_sn!=null&&!"".equals(order_sn)){
			whereStr = " and dosm.order_sn='"+order_sn+"' ";
		}
//	    String  sql=" select d.*,tc.mobilephone,tc.short_name as customer_name,CONCAT(GROUP_CONCAT(toi.eshop_pro_name),'') as eshop_pro_name from "
//					+" (select employee_a_no,order_placename from tmp_area_to_order_block where employee_a_no='"+employee_no+"') a INNER JOIN "
//					+" (select b.employee_name,b.order_sn,b.id,b.create_time,b.customer_id,b.df_signed_time,c.placename,c.address from df_order_signed_monthly b LEFT JOIN t_order_address c on b.order_address_id = c.id "+whereStr+" ) d" 
//					+" on a.order_placename = d.placename LEFT JOIN t_order_item toi on d.id = toi.order_id "
//					+ " LEFT JOIN t_customer tc ON tc.id=d.customer_id GROUP BY d.id  ORDER BY d.df_signed_time desc";
	  
		String sql="SELECT dosm.order_sn,dosm.placename,CONCAT(dosm.order_address_id,'') as order_address_id,dosm.employee_name,CONCAT(dosm.id,'') as id,dosm.create_time,dosm.customer_id,dosm.df_signed_time,tc.mobilephone,tc.short_name AS customer_name, CONCAT(GROUP_CONCAT(toi.eshop_pro_name),'') AS eshop_pro_name,CONCAT('https://imgcdn.guoanshequ.com/',ifnull(first_url,'')) as jpgUrl "+
					"FROM df_order_signed_monthly dosm "+
					"LEFT JOIN t_order_item toi ON dosm.id = toi.order_id "+
					"LEFT JOIN t_customer tc ON tc.id=dosm.customer_id "+
					"where dosm.placename in "+
					"(select order_placename from tmp_area_to_order_block where employee_a_no = '"+employee_no+"' and order_placename is not null) "+whereStr+
					"GROUP BY dosm.id ORDER BY dosm.df_signed_time DESC ";
					
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	    List<Map<String, Object>> lst_data = new ArrayList<Map<String,Object>>();
	    Map<String, Object> map_result = new HashMap<String, Object>();
	    try{
	    	 SQLQuery query = session.createSQLQuery(sql);
	         pageInfo.setTotalRecords(query.list().size());
	         //获得查询数据
	         lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
	                 .setFirstResult(pageInfo.getRecordsPerPage()* (pageInfo.getCurrentPage() - 1))
	                 .setMaxResults(pageInfo.getRecordsPerPage()).list();
	         Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
	 		 map_result.put("pageinfo",pageInfo);
	 		 map_result.put("totalpage", total_pages);
	 		 map_result.put("data", lst_data);
	     }catch (Exception e){
	         e.printStackTrace();
	         
	     }finally {
	         session.close();
	     }
	     return map_result;
	}

	
	@Override
	public Map<String, Object> getOrderByOrderSN(String order_sn) {
		String sql = "select concat(id,'') as id,create_time,payable_price,concat(employee_id,'') as employee_id from df_order_signed_monthly where order_sn='"+order_sn+"'";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	 	List<Map<String, Object>> lst_data = null;
	 	Map<String, Object> map_r = null;
	     try{
	        SQLQuery query = session.createSQLQuery(sql);
	         lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	         if(lst_data!=null&&lst_data.size()>0){
	        	 map_r = lst_data.get(0);
	         }
	     }catch (Exception e){
	         e.printStackTrace();
	     }finally {
	         session.close();
	     }
	     return map_r;
	}

	

	@Override
	public List<Map<String, Object>> getOrderAddressByAddressId(String addressId) {
		String sql="select address,concat(id,'') as id from t_order_address where id in ("+addressId+")";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	 	List<Map<String, Object>> lst_data = null;
	 	Map<String, Object> map_r = null;
	     try{
	        SQLQuery query = session.createSQLQuery(sql);
	        lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	        
	     }catch (Exception e){
	         e.printStackTrace();
	     }finally {
	         session.close();
	     }
	     return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> getOrderItemByOrderId(String orderId) {
		String sql="select CONCAT('https://imgcdn.guoanshequ.com/',ifnull(first_url,'')) as jpgUrl,concat(order_id,'') as order_id from t_order_item    where  order_id in ("+orderId+") GROUP BY order_id";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	 	List<Map<String, Object>> lst_data = null;
	 	Map<String, Object> map_r = null;
	     try{
	        SQLQuery query = session.createSQLQuery(sql);
	        lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	        
	     }catch (Exception e){
	         e.printStackTrace();
	     }finally {
	         session.close();
	     }
	     return lst_data;
	}
	
    

	@Override
	public List<Map<String, Object>> getDailyOrderOfCurDay(String cityNo,DynamicDto dd){
		String sqlStr = "";
		String whereCustomerStr = "";
		String whereStr = "";
		String dateStr = "";
		String beginDate = dd.getBeginDate();
		if(cityNo!=null&&cityNo.startsWith("00")){
			cityNo = cityNo.substring(1,cityNo.length());
		}
		String endDate = dd.getEndDate();
			if(dd.getTarget()==1){//城市总监
				whereStr+=" AND t.city_code='"+cityNo+"' ";
				whereCustomerStr+=" AND t5.city_code='"+cityNo+"' ";
			}else if(dd.getTarget()==2){//店长
				whereStr+=" and t.`code`='"+dd.getStoreNo()+"' ";
				whereCustomerStr+=" AND `code`='"+dd.getStoreNo()+"' ";
			}else if(dd.getTarget()==3){//国安侠
				whereStr+=" AND tor.employee_id='"+dd.getEmployeeId()+"' ";
				whereCustomerStr+=" AND employee_id='"+dd.getEmployeeId()+"' ";
			}
		if(beginDate!=null&&endDate!=null&&!"".equals(beginDate)&&!"".equals(endDate)){
			dateStr = " WHERE df_signed_time BETWEEN '"+beginDate+" 00:00:00' and '"+endDate+" 23:59:59' ";
//			dateStr = " WHERE df_signed_time BETWEEN '"+"2017-11-07"+" 00:00:00' and '"+"2017-11-08"+" 23:59:59' ";
		}
		if(dd.getTarget()==1){
			sqlStr = "SELECT IFNULL(COUNT(tor.trading_price),0) AS checked_order_count,IFNULL(SUM(tor.total_quantity), 0) AS total_order_count," +
					"IFNULL(SUM(tor.trading_price),0) AS totle_price,IFNULL(SUM(tor.total_quantity),0) AS total_quantity,(SELECT IFNULL(count" +
					"(DISTINCT(t4.customer_id)),0) FROM t_store t5 LEFT JOIN df_order_signed_daily t4 ON t4.store_id = t5.id " +
					dateStr+whereCustomerStr+") " +
					"AS customer_count FROM t_store t LEFT JOIN df_order_signed_daily tor ON t.id = tor.store_id "+dateStr+whereStr;
		}else{
			sqlStr = "SELECT COUNT(t2.trading_price) AS checked_order_count,IFNULL(SUM(t2.trading_price),0) AS totle_price," +
					"IFNULL(SUM(t2.total_quantity),0) AS total_order_count,(SELECT COUNT(t3.customer_id) " +
					" FROM (SELECT t4.customer_id,t5.city_code FROM t_store t5 LEFT JOIN df_order_signed_daily t4  ON t4.store_id=t5.id "+dateStr+whereCustomerStr+" GROUP BY customer_id) t3) AS customer_count " +
							" FROM (SELECT t.city_code AS city_code,tor.trading_price AS trading_price,tor.total_quantity AS total_quantity," +
							"tor.customer_id as customer_id FROM t_store t LEFT JOIN df_order_signed_daily tor ON t.id=tor.store_id "
							+dateStr+whereStr+") t2 ";
		}
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
		try{
			Query query = session.createSQLQuery(sqlStr);
			List<?> lst_data = query
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            if(lst_data != null){
                for(Object obj : lst_data){
                    Map<String,Object> map_data = (Map<String,Object>)obj;
                    Map<String,Object> map_content = (Map<String,Object>)obj;
                    map_content.put("checked_order_count",map_data.get("checked_order_count"));
                    map_content.put("totle_price",map_data.get("totle_price"));
                    map_content.put("total_order_count",map_data.get("total_order_count"));
                    map_content.put("customer_count",map_data.get("customer_count"));
                    lst_result.add(map_content);
                }
            }
		}catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
		return lst_result;
	}

	
	@Override
	public Map<String, Object> getEmployeeByIdOfGemini(String id) {
		String sql="select CONCAT('https://imgcdn.guoanshequ.com/',ifnull(avatar,'')) as pngUrl,id,name from t_employee    where id = '"+id+"'";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	 	List<Map<String, Object>> lst_data = null;
	 	Map<String, Object> map_r = null;
	     try{
	        SQLQuery query = session.createSQLQuery(sql);
	        lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	        if(lst_data!=null&&lst_data.size()>0){
		    	 map_r =  lst_data.get(0);
		     }
	     }catch (Exception e){
	         e.printStackTrace();
	     }finally {
	         session.close();
	     }
	    return map_r;
	}
	
	
	@Override
	public List<Map<String, Object>> getLagLatByOrder(String string_sql,String beginDate,String endDate) {
		String str_sql = "select dosm.longitude as lng,dosm.latitude as lat,COUNT(dosm.id) as count from df_order_signed_monthly dosm INNER JOIN t_store ts ON ts.id = dosm.store_id  where dosm.longitude is not null and dosm.latitude is not null and ts.code in ("+string_sql+") and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')>='"+beginDate+"' and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')<='"+endDate+"' GROUP BY dosm.longitude,dosm.latitude";
		 Session session = getHibernateTemplate().getSessionFactory().openSession();
	        List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
	        try{
	            SQLQuery query = session.createSQLQuery(str_sql);
	            List<Map<String,Object>> lst_data = query
	                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	            lst_result = lst_data;
	        }catch (Exception e){
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
       
        return lst_result;
	}

	@Override
	public List<Map<String, Object>> getLagLatTradingPriceByOrder(String string_sql, String beginDate, String endDate) {
		String str_sql = "select dosm.longitude as lng,dosm.latitude as lat,sum(dosm.trading_price) as count from df_order_signed_monthly dosm INNER JOIN t_store ts ON ts.id = dosm.store_id  where dosm.longitude is not null and dosm.latitude is not null and ts.code in ("+string_sql+") and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')>='"+beginDate+"' and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')<='"+endDate+"' GROUP BY dosm.longitude,dosm.latitude";
		 Session session = getHibernateTemplate().getSessionFactory().openSession();
	        List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
	        try{
	            SQLQuery query = session.createSQLQuery(str_sql);
	            List<Map<String,Object>> lst_data = query
	                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	            lst_result = lst_data;
	        }catch (Exception e){
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
      
       return lst_result;
	}

	@Override
	public List<Map<String, Object>> getLagLatCount(String string_sql, String beginDate, String endDate) {
		String str_sql = "select MAX(t1.count) as maxCount,MIN(t1.count) as minCount from (select count(dosm.id) as count from df_order_signed_monthly dosm INNER JOIN t_store ts ON ts.id = dosm.store_id  where dosm.longitude is not null and dosm.latitude is not null and ts.code in ("+string_sql+") and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')>='"+beginDate+"' and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')<='"+endDate+"' GROUP BY dosm.longitude,dosm.latitude) t1";
		 Session session = getHibernateTemplate().getSessionFactory().openSession();
	        List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
	        try{
	            SQLQuery query = session.createSQLQuery(str_sql);
	            List<Map<String,Object>> lst_data = query
	                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	            lst_result = lst_data;
	        }catch (Exception e){
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
     
      return lst_result;
	}

	@Override
	public List<Map<String, Object>> getLagLatTradingPriceCount(String string_sql, String beginDate, String endDate) {
		String str_sql = "select MAX(t1.count) as maxCount,MIN(t1.count) as minCount from (select sum(dosm.trading_price) as count from df_order_signed_monthly dosm INNER JOIN t_store ts ON ts.id = dosm.store_id  where dosm.longitude is not null and dosm.latitude is not null and ts.code in ("+string_sql+") and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')>='"+beginDate+"' and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')<='"+endDate+"' GROUP BY dosm.longitude,dosm.latitude) t1";
		 Session session = getHibernateTemplate().getSessionFactory().openSession();
	        List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
	        try{
	            SQLQuery query = session.createSQLQuery(str_sql);
	            List<Map<String,Object>> lst_data = query
	                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	            lst_result = lst_data;
	        }catch (Exception e){
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
     
      return lst_result;
	}

	@Override
	public List<Map<String, Object>> getDailyStoreOrderOfCurDay(DynamicDto dd,List<Map<String, Object>> cityNO,List<Map<String, Object>> provinceNO) {
		String sqlStr = "";
//		sqlStr = "SELECT SUM(t.trading_price) as storecur_all_price FROM df_order_signed_daily t " +
//				"WHERE t.df_signed_time BETWEEN '"+dd.getBeginDate()+" 00:00:00' and '"+dd.getEndDate()+" 23:59:59'";
		String cityStr1 = "";
		String provinceStr1 = "";
		if(cityNO!=null&&cityNO.size()>0){
			String cityNo = String.valueOf(cityNO.get(0).get("cityno"));
			if(cityNo.startsWith("00")){
				cityNo = cityNo.substring(1,cityNo.length());
			}
			cityStr1+=" and t.city_code='"+cityNo+"' ";
		}
		if(provinceNO!=null&&provinceNO.size()>0){
			provinceStr1+=" and t.province_code='"+provinceNO.get(0).get("gb_code")+"'";
		}
		sqlStr="SELECT IFNULL(COUNT(tor.trading_price),0) AS checked_order_count,IFNULL(SUM(tor.total_quantity), 0) " +
				"AS total_order_count,IFNULL(SUM(tor.trading_price),0) AS storecur_all_price,IFNULL" +
				"(SUM(tor.total_quantity),0) AS total_quantity FROM df_order_signed_daily tor LEFT JOIN " +
				"t_store t ON " +"t.id = tor.store_id  WHERE df_signed_time BETWEEN '"+dd.getBeginDate()+
				" 00:00:00' and '"+dd.getEndDate()+" 23:59:59'"+cityStr1+provinceStr1;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
		try{
			Query query = session.createSQLQuery(sqlStr);
			List<?> lst_data = query
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            if(lst_data != null){
                for(Object obj : lst_data){
                	 Map<String,Object> map_data = (Map<String,Object>)obj;
                     Map<String,Object> map_content = (Map<String,Object>)obj;
                     map_content.put("storecur_all_price",map_data.get("storecur_all_price"));
                     map_content.put("checked_order_count",map_data.get("checked_order_count"));
                     lst_result.add(map_content);
                }
            }
		}catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
		return lst_result;
	}
	
	/**
	 * 总部查看：按eshop统计交易额
	 */
	@Override
	public List<Map<String, Object>> queryTradeByEshop(DynamicDto dd) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String beginDate ="";
		String endDate ="";
		Calendar cale = Calendar.getInstance();  
        cale = Calendar.getInstance();  
        cale.add(Calendar.MONTH, 0);  
        cale.set(Calendar.DAY_OF_MONTH, 1);  
        beginDate = format.format(cale.getTime()); 
        cale = Calendar.getInstance();  
        cale.add(Calendar.MONTH, 1);  
        cale.set(Calendar.DAY_OF_MONTH, 0);  
        endDate = format.format(cale.getTime());
		String sql = "select eshop_name , sum(trading_price) order_amount from df_order_signed_monthly "+
					 "where df_signed_time >='"+beginDate+" 00:00:00' and df_signed_time <='"+endDate+" 23:59:59' "+ 
					 "group by eshop_name "+ 
					 "order by sum(trading_price) desc "+
					 "limit 10 ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		List<Map<String, Object>> lst_result = new ArrayList<Map<String, Object>>();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			lst_result = lst_data;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
       return lst_result;
	}


	
	@Override
	public Map<String, Object> selectEStoreRankingOfStore(DynamicDto dynamicDto,PageInfo pageInfo) {
		String  sql ="select SUM(IFNULL(trading_price,0)) as amount,eshop_name as name,eshop_id from df_order_signed_monthly a where store_id='"+dynamicDto.getStoreIds()+"'  and DATE_FORMAT(df_signed_time,'%Y-%m')='"+dynamicDto.getBeginDate()+"'  GROUP BY eshop_id order by amount desc ";
		//String sql ="select SUM(IFNULL(trading_price,0)) as amount,eshop_name as name,eshop_id from df_order_signed_monthly a where store_id='"+dynamicDto.getStoreIds()+"'  and DATE_FORMAT(df_signed_time,'%Y-%m')='2017-12'  GROUP BY eshop_id order by amount desc ";

		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Map<String,Object> map_result = new HashMap<String,Object>();
		List<?> list=null;
		try {
			String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";
			if(pageInfo!=null){
				Query query_count = session.createSQLQuery(sql_count);
				Object total = query_count.uniqueResult();
				pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

				Query query = session.createSQLQuery(sql);

				list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
						pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();
			
				Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
				map_result.put("pageinfo",pageInfo);
				map_result.put("totalPage", total_pages);
				map_result.put("totalRecords", total);
			}else{
				Query query = session.createSQLQuery(sql);
				list= query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			}
			
			map_result.put("gmv", list);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
       return map_result;
	}


	@Override
	public Map<String, Object> getEmployeeByEmployeeIdOfGemini(String id) {
		String sql="select CONCAT('https://imgcdn.guoanshequ.com/',ifnull(avatar,'')) as pngUrl,id,code,name from t_employee    where code = '"+id+"'";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	 	List<Map<String, Object>> lst_data = null;
	 	Map<String, Object> map_r = null;
	     try{
	        SQLQuery query = session.createSQLQuery(sql);
	        lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	        if(lst_data!=null&&lst_data.size()>0){
		    	 map_r =  lst_data.get(0);
		     }
	     }catch (Exception e){
	         e.printStackTrace();
	     }finally {
	         session.close();
	     }
	    return map_r;
	}

	@Override
	public Map<String, Object> queryOrderOfAreaForApp(String area_no, PageInfo pageInfo, String order_sn) {
		String whereStr = "";
		if(order_sn!=null&&!"".equals(order_sn)){
			whereStr = " and dosm.order_sn='"+order_sn+"' ";
		}
		String sql="SELECT dosm.order_sn,dosm.placename,CONCAT(dosm.order_address_id,'') as order_address_id,dosm.employee_name,CONCAT(dosm.id,'') as id,dosm.create_time,dosm.customer_id,dosm.df_signed_time,tc.mobilephone,tc.short_name AS customer_name, CONCAT(GROUP_CONCAT(toi.eshop_pro_name),'') AS eshop_pro_name,CONCAT('https://imgcdn.guoanshequ.com/',ifnull(first_url,'')) as jpgUrl "+
					"FROM df_order_signed_monthly dosm "+
					"LEFT JOIN t_order_item toi ON dosm.id = toi.order_id "+
					"LEFT JOIN t_customer tc ON tc.id=dosm.customer_id "+
					"where dosm.placename in "+
					"(select order_placename from tmp_area_to_order_block where area_no = '"+area_no+"' and order_placename is not null) "+whereStr+
					"GROUP BY dosm.id ORDER BY dosm.df_signed_time DESC ";
					
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	    List<Map<String, Object>> lst_data = new ArrayList<Map<String,Object>>();
	    Map<String, Object> map_result = new HashMap<String, Object>();
	    try{
	    	 SQLQuery query = session.createSQLQuery(sql);
	         pageInfo.setTotalRecords(query.list().size());
	         //获得查询数据
	         lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
	                 .setFirstResult(pageInfo.getRecordsPerPage()* (pageInfo.getCurrentPage() - 1))
	                 .setMaxResults(pageInfo.getRecordsPerPage()).list();
	         Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
	 		 map_result.put("pageinfo",pageInfo);
	 		 map_result.put("totalpage", total_pages);
	 		 map_result.put("data", lst_data);
	     }catch (Exception e){
	         e.printStackTrace();
	         
	     }finally {
	         session.close();
	     }
	     return map_result;
	}

	@Override
	public List<Map<String, Object>> queryTradeByProduct(DynamicDto dd, List<Map<String, Object>> cityNO,
			List<Map<String, Object>> provinceNO) {
		String sqlStr = "";
		String cityStr1 = "";
		String provinceStr1 = "";
		if(cityNO!=null&&cityNO.size()>0){
			String cityNo = String.valueOf(cityNO.get(0).get("cityno"));
			if(cityNo.startsWith("00")){
				cityNo = cityNo.substring(1,cityNo.length());
			}
			cityStr1+=" and ts.city_code='"+cityNo+"' ";
		}
		if(provinceNO!=null&&provinceNO.size()>0){
			provinceStr1+=" and ts.province_code='"+provinceNO.get(0).get("gb_code")+"' ";
		}
		sqlStr=" select product_name,sum(cur_month) product_count from t_product_store_stat dpross "+
				" left join t_store ts on (dpross.store_id = ts.id ) "+
				" where 1=1 "+ cityStr1 + provinceStr1 +
				" group by product_id "+
				" order by sum(cur_month) desc "+ 
				" limit 10 ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
		try{
			Query query = session.createSQLQuery(sqlStr);
			List<?> lst_data = query
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            if(lst_data != null){
                for(Object obj : lst_data){
                	 Map<String,Object> map_data = (Map<String,Object>)obj;
                     Map<String,Object> map_content = (Map<String,Object>)obj;
                     map_content.put("product_name",map_data.get("product_name"));
                     map_content.put("product_count",map_data.get("product_count"));
                     lst_result.add(map_content);
                }
            }
		}catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
		return lst_result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryBusinessModelBaseType(){
		String sql = "select distinct(m.name) as eshop_model from t_eshop t,t_business_model m where t.business_model_id=m.id ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		List<Map<String, Object>> lst_data = new ArrayList<Map<String, Object>>();

		try {
			SQLQuery query = session.createSQLQuery(sql);
			lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return lst_data;
	}
	
	@Override
	public Map<String, Object> queryEshopModelBaseInfo(List<String> eshopList, EshopPurchaseDto shopPurchaseDto, PageInfo pageInfo){
		String sql="select CONCAT(t.id,'') as eshop_id,t.name as eshop_name,t.code as eshop_code,m.name as eshop_model from t_eshop t,t_business_model m " +
				"where t.business_model_id=m.id and t.id not in (:eshopIdRanges) ";
		if(StringUtils.isNotEmpty(shopPurchaseDto.getEshopId())){
			sql=sql+" and t.id ='"+shopPurchaseDto.getEshopId()+"'";
		}
		
		if(StringUtils.isNotEmpty(shopPurchaseDto.getEshopCode())){
			sql=sql+" and t.code ='"+shopPurchaseDto.getEshopCode()+"'";
		}
		
		if(StringUtils.isNotEmpty(shopPurchaseDto.getEshopModel())){
			sql=sql+" and m.name ='"+shopPurchaseDto.getEshopModel()+"'";
		}
		
		if(StringUtils.isNotEmpty(shopPurchaseDto.getEshopName())){
			sql=sql+" and t.name like '%"+shopPurchaseDto.getEshopName().trim()+"%'";
		}
		
		sql=sql+" order by t.code asc";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	 	Map<String,Object> map_result = new HashMap<String,Object>();
	     try{
	        String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";
	        Query query_count = session.createSQLQuery(sql_count);
	        query_count.setParameterList("eshopIdRanges", eshopList);  
			Object total = query_count.uniqueResult();
			pageInfo.setTotalRecords(Integer.valueOf(total.toString()));
			
			SQLQuery query = session.createSQLQuery(sql);
			List<?> list = query.setParameterList("eshopIdRanges", eshopList).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
					pageInfo.getRecordsPerPage()
							* (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

			Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
			map_result.put("pageinfo",pageInfo);
			map_result.put("data", list);
			map_result.put("total_pages", total_pages);
	     }catch (Exception e){
	         e.printStackTrace();
	     }finally {
	         session.close();
	     }
		return map_result;
	}
	
	@Override
	public Map<String, Object> queryUserOpsNewCusStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo){
		String sql="select tb.city_name, tb.store_name, tb.name as area_name, count(distinct(case when order_month_count = 1 then customer_id end)) new_count,"
				+ " count(distinct(case when order_month_count = 1 and trading_price > 10 then customer_id end)) new_10_count,count(distinct(case when order_month_count = 1 "
				+ " and trading_price > 20 then customer_id end)) new_20_count from df_customer_order_month_trade mt left JOIN tmp_area_to_order_block tb on (mt.placename=tb.order_placename and mt.store_id=tb.platformid) "
				+ " where order_ym >='"+userOperationStatDto.getBeginDate()+"' and order_ym <='"+userOperationStatDto.getEndDate()+"' and mt.placename is not null and tb.order_placename is not null ";
		
		if(StringUtils.isNotEmpty(userOperationStatDto.getCityName())){
			sql = sql + " and tb.city_name like '%" + userOperationStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(userOperationStatDto.getStoreNo())){
			sql = sql + " and tb.storeno ='" + userOperationStatDto.getStoreNo().trim()+ "'";
		}
		
		sql=sql+" group by mt.store_id,tb.area_no ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	 	Map<String,Object> map_result = new HashMap<String,Object>();
	     try{
	        String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";
	        Query query_count = session.createSQLQuery(sql_count);
			Object total = query_count.uniqueResult();
			pageInfo.setTotalRecords(Integer.valueOf(total.toString()));
			
			SQLQuery query = session.createSQLQuery(sql);
			List<?> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
					pageInfo.getRecordsPerPage()
							* (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

			Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
			map_result.put("pageinfo",pageInfo);
			map_result.put("data", list);
			map_result.put("total_pages", total_pages);
	     }catch (Exception e){
	         e.printStackTrace();
	     }finally {
	         session.close();
	     }
		return map_result;
	}
	
	@Override
	public Map<String, Object> queryUserOpsPayStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo){
		String sql="select tb.city_name, tb.store_name, tb.name as area_name, count(distinct customer_id) pay_count,"
				+ " count(distinct(case when trading_price > 10 then customer_id end)) pay_10_count, count(distinct(case when trading_price > 20 "
				+ " then customer_id end)) pay_20_count from df_customer_order_month_trade mt left JOIN tmp_area_to_order_block tb on (mt.placename=tb.order_placename and mt.store_id=tb.platformid) "
				+ " where order_ym >='"+userOperationStatDto.getBeginDate()+"' and order_ym <='"+userOperationStatDto.getEndDate()+"' and mt.placename is not null and tb.order_placename is not null ";
		
		if(StringUtils.isNotEmpty(userOperationStatDto.getCityName())){
			sql = sql + " and tb.city_name like '%" + userOperationStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(userOperationStatDto.getStoreNo())){
			sql = sql + " and tb.storeno ='" + userOperationStatDto.getStoreNo().trim()+ "'";
		}
		
		sql=sql+" group by mt.store_id,tb.area_no ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	 	Map<String,Object> map_result = new HashMap<String,Object>();
	     try{
	        String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";
	        Query query_count = session.createSQLQuery(sql_count);
			Object total = query_count.uniqueResult();
			pageInfo.setTotalRecords(Integer.valueOf(total.toString()));
			
			SQLQuery query = session.createSQLQuery(sql);
			List<?> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
					pageInfo.getRecordsPerPage()
							* (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

			Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
			map_result.put("pageinfo",pageInfo);
			map_result.put("data", list);
			map_result.put("total_pages", total_pages);
	     }catch (Exception e){
	         e.printStackTrace();
	     }finally {
	         session.close();
	     }
		return map_result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> exportUserOpsNewCusStat(UserOperationStatDto userOperationStatDto){
		String sql="select tb.city_name, tb.store_name, tb.name as area_name, count(distinct(case when order_month_count = 1 then customer_id end)) new_count,"
				+ " count(distinct(case when order_month_count = 1 and trading_price > 10 then customer_id end)) new_10_count,count(distinct(case when order_month_count = 1 "
				+ " and trading_price > 20 then customer_id end)) new_20_count from df_customer_order_month_trade mt left JOIN tmp_area_to_order_block tb on (mt.placename=tb.order_placename and mt.store_id=tb.platformid) "
				+ " where order_ym >='"+userOperationStatDto.getBeginDate()+"' and order_ym <='"+userOperationStatDto.getEndDate()+"' and mt.placename is not null and tb.order_placename is not null ";
		
		if(StringUtils.isNotEmpty(userOperationStatDto.getCityName())){
			sql = sql + " and tb.city_name like '%" + userOperationStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(userOperationStatDto.getStoreNo())){
			sql = sql + " and tb.storeno ='" + userOperationStatDto.getStoreNo().trim()+ "'";
		}
		
		sql=sql+" group by mt.store_id,tb.area_no ";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> exportUserOpsPayStat(UserOperationStatDto userOperationStatDto){
		String sql="select tb.city_name, tb.store_name, tb.name as area_name, count(distinct customer_id) pay_count,"
				+ " count(distinct(case when trading_price > 10 then customer_id end)) pay_10_count, count(distinct(case when trading_price > 20 "
				+ " then customer_id end)) pay_20_count from df_customer_order_month_trade mt left JOIN tmp_area_to_order_block tb on (mt.placename=tb.order_placename and mt.store_id=tb.platformid) "
				+ " where order_ym >='"+userOperationStatDto.getBeginDate()+"' and order_ym <='"+userOperationStatDto.getEndDate()+"' and mt.placename is not null and tb.order_placename is not null ";
		
		if(StringUtils.isNotEmpty(userOperationStatDto.getCityName())){
			sql = sql + " and tb.city_name like '%" + userOperationStatDto.getCityName().trim() + "%'";
		}
		if(StringUtils.isNotEmpty(userOperationStatDto.getStoreNo())){
			sql = sql + " and tb.storeno ='" + userOperationStatDto.getStoreNo().trim()+ "'";
		}
		
		sql=sql+" group by mt.store_id,tb.area_no ";
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryOrderByCustomerIdTop20(String customer_id){
		String sql = "SELECT CONCAT(t_order.id,'') as id,CONCAT(t_order.customer_id,'') as customer_id,CONCAT(t_order.store_id,'') as store_id,t_order.sign_time,t_order.trading_price FROM t_order WHERE t_order.customer_id='"+customer_id+"' AND t_order.sign_time IS NOT NULL ORDER BY t_order.sign_time DESC LIMIT 5";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		List<Map<String, Object>> lst_data = new ArrayList<Map<String, Object>>();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return lst_data;
	}
	

	
	@Override
	public Map<String, Object> queryStoreCustomerOfMonth(String plateformId,PageInfo pageInfo) {
		String sql="select count(1) as total,concat(substr(order_ym,1,4),'年') as year, concat(substr(order_ym,5,2),'月') as month from df_customer_order_month_trade  where store_id='"+plateformId+"' GROUP BY order_ym ORDER BY total desc";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	 	Map<String,Object> map_result = new HashMap<String,Object>();
	     try{
	    	List<?> list=null;
	    	SQLQuery query = session.createSQLQuery(sql);
	    	if(pageInfo!=null){
	    		String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";
	 	        Query query_count = session.createSQLQuery(sql_count);
	 	       
	 			Object total = query_count.uniqueResult();
	 			pageInfo.setTotalRecords(Integer.valueOf(total.toString()));
	 			
	 			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
	 					pageInfo.getRecordsPerPage()
	 							* (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

	 			Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
	 			map_result.put("pageinfo",pageInfo);
	 			map_result.put("total_pages", total_pages);
	    	}else{
	    		list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	    	}
	    	map_result.put("data", list);
	     }catch (Exception e){
	         e.printStackTrace();
	     }finally {
	         session.close();
	     }
	     return map_result;
	}
	@Override
	public Map<String, Object> queryStoreCustmerCount(DynamicDto dd,List<Map<String, Object>> cityNO,
			List<Map<String, Object>> provinceNO,PageInfo pageInfo) {
		String cityStr1 = "";
		String provinceStr1 = "";
		if(cityNO!=null&&cityNO.size()>0){
			String cityNo = String.valueOf(cityNO.get(0).get("cityno"));
			if(cityNo.startsWith("00")){
				cityNo = cityNo.substring(1,cityNo.length());
			}
			cityStr1+=" and ts.city_code='"+cityNo+"' ";
		}
		if(provinceNO!=null&&provinceNO.size()>0){
			provinceStr1+=" and ts.province_code='"+provinceNO.get(0).get("gb_code")+"'";
		}
		Map<String, Object> maps = new HashMap<String, Object>();
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		String sql = "SELECT count(tr.customer_id) AS customer_count,ts.`name` AS store_name,ts.city_code as city_code,concat(tr.store_id,'') as store_id FROM " +
				"df_customer_order_month_trade tr LEFT JOIN t_store ts ON tr.store_id = ts.id  WHERE tr.order_ym = '"+
				dd.getYear()+(dd.getMonth()<10?("0"+dd.getMonth()):dd.getMonth())+"' "+cityStr1+provinceStr1+" and ts.`name` is not null GROUP BY ts.`name` ORDER BY customer_count DESC ";
		String sql_count = "SELECT count(tdd.customer_count) as customer_cnt from ( "+sql+") tdd ";
		Map<String, Object> map_result = new HashMap<String, Object>();
		List<?> list = null;
		try {
			Query query_count = session.createSQLQuery(sql_count);
			List<?> total = query_count
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			if(pageInfo!=null){
				if(total!=null&&total.size()>0){
					maps = (Map<String, Object>) total.get(0);
					pageInfo.setTotalRecords(Integer.valueOf(maps.get("customer_cnt").toString()));
				}else{
					pageInfo.setTotalRecords(Integer.valueOf(0));
				}
			}
			Query query = session.createSQLQuery(sql);
			
			if(pageInfo==null){
				list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			}else{
				list=query
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(
						pageInfo.getRecordsPerPage()
								* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
			}
			if(pageInfo!=null){
				Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
				map_result.put("pageinfo", pageInfo);
				map_result.put("total_pages", total_pages);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.close();
		}
		map_result.put("gmv", list);
		return map_result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryPositionByOrdersn(String order_sn) {
		String sql="select ifnull(df.latitude,'') as latitude,ifnull(df.longitude,'') as longitude from df_order_signed_monthly df where order_sn = '"+order_sn+"'";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	 	List<Map<String, Object>> lst_data = null;
	 	Map<String, Object> map_r = null;
	     try{
	        SQLQuery query = session.createSQLQuery(sql);
	        lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	        if(lst_data!=null&&lst_data.size()>0){
		    	 map_r =  lst_data.get(0);
		     }
	     }catch (Exception e){
	         e.printStackTrace();
	     }finally {
	         session.close();
	     }
	    return map_r;
	}

	@Override
	public List<Map<String, Object>> getDailyUserOfCurDay(DynamicDto dd,List<Map<String, Object>> cityNO,
			List<Map<String, Object>> provinceNO) {
		String sqlStr = "";
		String whereCustomerStr = "";
		String whereStr = "";
		String dateStr = "";
		String beginDate = dd.getBeginDate();
		String cityNo = "";
		String provinceNo = "";
		if(cityNO!=null&&cityNO.size()>0){
			cityNo = String.valueOf(cityNO.get(0).get("cityno"));
			if(cityNo!=null&&cityNo.startsWith("00")){
				cityNo = cityNo.substring(1,cityNo.length());
			}
			whereStr+=" AND t.city_code='"+cityNo+"' ";
			whereCustomerStr+=" AND t5.city_code='"+cityNo+"' ";
		}
		String endDate = dd.getEndDate();
		if(provinceNO!=null&&provinceNO.size()>0){
			provinceNo = String.valueOf(provinceNO.get(0).get("gb_code"));
			whereCustomerStr+=" AND t5.city_code='"+provinceNo+"' ";
			whereStr+=" and t.province_code='"+provinceNo+"' ";
		}
		if(beginDate!=null&&endDate!=null&&!"".equals(beginDate)&&!"".equals(endDate)){
			dateStr = " WHERE df_signed_time BETWEEN '"+beginDate+" 00:00:00' and '"+endDate+" 23:59:59' ";
//			dateStr = " WHERE df_signed_time BETWEEN '"+"2017-12-27"+" 00:00:00' and '"+"2017-12-27"+" 23:59:59' ";
		}
			sqlStr = "SELECT DISTINCT(SELECT IFNULL(count(DISTINCT(t4.customer_id)),0) AS customer_count FROM t_store t5 LEFT JOIN df_order_signed_daily t4 ON t4.store_id = t5.id " +
					dateStr+ whereCustomerStr+" ) AS customer_count FROM t_store t LEFT JOIN df_order_signed_daily " +
					"tor ON t.id = tor.store_id "+dateStr+whereStr;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
		try{
			Query query = session.createSQLQuery(sqlStr);
			List<?> lst_data = query
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            if(lst_data != null){
                for(Object obj : lst_data){
                    Map<String,Object> map_data = (Map<String,Object>)obj;
                    Map<String,Object> map_content = (Map<String,Object>)obj;
                    map_content.put("customer_count",map_data.get("customer_count"));
                    lst_result.add(map_content);
                }
            }
		}catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
		return lst_result;
	}

	@Override
	public List<Map<String, Object>> getLatlngCustomerByCity(String string_sql, String beginDate, String endDate) {
		String str_sql = "select dosm.latitude as lat,dosm.longitude as lng,count(distinct dosm.customer_id) as count from df_order_signed_monthly dosm INNER JOIN t_store ts ON dosm.store_id = ts.id where ts.city_code = '"+string_sql+"'"
				+"and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')>='"+beginDate+"' and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')<='"+endDate+"' and dosm.latitude is not null and dosm.eshop_name NOT LIKE '%测试%' GROUP BY dosm.latitude,dosm.longitude";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	        List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
	        try{
	            SQLQuery query = session.createSQLQuery(str_sql);
	            List<Map<String,Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	            lst_result = lst_data;
	        }catch (Exception e){
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
     
        return lst_result;
	}

	@Override
	public List<Map<String, Object>> getLatlngCustomerCountByCity(String cityNo, String beginDate, String endDate) {
		String str_sql = "select max(count) as maxCount,min(count) as minCount from (select count(distinct dosm.customer_id) as count from df_order_signed_monthly dosm INNER JOIN t_store ts ON dosm.store_id = ts.id where ts.city_code = '"+cityNo+"'"
				+"and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')>='"+beginDate+"' and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')<='"+endDate+"' and dosm.eshop_name NOT LIKE '%测试%' "
				+ "and ts.name NOT LIKE '%测试%' and ts.white!='QA' and dosm.latitude is not null GROUP BY dosm.latitude,dosm.longitude) t";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	        List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
	        try{
	            SQLQuery query = session.createSQLQuery(str_sql);
	            List<Map<String,Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	            lst_result = lst_data;
	        }catch (Exception e){
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
     
        return lst_result;
	}

	@Override
	public List<Map<String, Object>> getLatlngCustomerByStore(String storeNo, String beginDate, String endDate) {
		String str_sql = "select dosm.latitude as lat,dosm.longitude as lng,count(distinct dosm.customer_id) as count from df_order_signed_monthly dosm INNER JOIN t_store ts ON dosm.store_id = ts.id where ts.`code` = "+storeNo
				+"and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')>='"+beginDate+"' and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')<='"+endDate+"' and dosm.eshop_name NOT LIKE '%测试%' "
				+ "and ts.name NOT LIKE '%测试%' and ts.white!='QA' and dosm.latitude is not null GROUP BY dosm.latitude,dosm.longitude";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	        List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
	        try{
	            SQLQuery query = session.createSQLQuery(str_sql);
	            List<Map<String,Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	            lst_result = lst_data;
	        }catch (Exception e){
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
     
        return lst_result;
	}

	@Override
	public List<Map<String, Object>> getLatlngCustomerCountByStore(String storeNo, String beginDate, String endDate) {
		String str_sql = "select max(count) as maxCount,min(count) as minCount from (select count(distinct dosm.customer_id) as count from df_order_signed_monthly dosm INNER JOIN t_store ts ON dosm.store_id = ts.id where ts.`code` = "+storeNo
				+"and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')>='"+beginDate+"' and DATE_FORMAT(dosm.df_signed_time,'%Y/%m/%d HH:ii')<='"+endDate+"' and dosm.eshop_name NOT LIKE '%测试%' and dosm.latitude is not null GROUP BY dosm.latitude,dosm.longitude) t";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
	        List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
	        try{
	            SQLQuery query = session.createSQLQuery(str_sql);
	            List<Map<String,Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	            lst_result = lst_data;
	        }catch (Exception e){
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
     
        return lst_result;
	}

	@Override
	public List<Map<String, Object>> getDailyFirstOrderCity() {
		String sqlStr = "";
		sqlStr="SELECT t.province_code as province_code,t.city_code as city_code,tor.df_signed_time FROM df_order_signed_daily tor LEFT JOIN " +
				"t_store t ON t.id = tor.store_id  WHERE t.city_code IS NOT null ORDER BY tor.df_signed_time DESC LIMIT 6 ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
		try{
			Query query = session.createSQLQuery(sqlStr);
			List<Map<String,Object>> lst_data = query
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			lst_result = lst_data;
		}catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
		
		return lst_result;
	}
}
