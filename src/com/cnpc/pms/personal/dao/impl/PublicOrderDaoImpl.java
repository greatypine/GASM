package com.cnpc.pms.personal.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.PublicOrderDao;
import com.cnpc.pms.personal.entity.PublicOrder;

public class PublicOrderDaoImpl extends BaseDAOHibernate implements
		PublicOrderDao {

	@Override
	public Map<String, Object> queryPublicOrder(PublicOrder publicOrder,
			PageInfo pageInfo) {
		
		String sqlwhere = " a.df_ispubseas=1 ";
		
		if(publicOrder.getOrdertype()!=null&&publicOrder.getOrdertype().equals("0")){
			//公海订单
			sqlwhere+= " and a.df_customer_id not like 'fakecustomer%' ";
		}else if(publicOrder.getOrdertype()!=null&&publicOrder.getOrdertype().equals("1")){
			//微超订单
			sqlwhere+= " and a.df_customer_id like 'fakecustomer%' ";
		}
		
		if(publicOrder.getOrdersn()!=null&&publicOrder.getOrdersn().length()>0){
			sqlwhere += " and a.df_order_sn = '"+publicOrder.getOrdersn()+"'";
		}
		if(publicOrder.getStoreno()!=null&&publicOrder.getStoreno().length()>0){
			sqlwhere += " and a.df_storeno = '"+publicOrder.getStoreno()+"'";
		}else{
			//查询当前登录人所管理城的城市的所有门店 
			if(publicOrder.getStorenos()!=null&&publicOrder.getStorenos().length()>0){
				sqlwhere +=" and a.df_storeno in("+publicOrder.getStorenos()+")";
			}else{
				sqlwhere +=" and 1=0 ";
			}
		}
		if(publicOrder.getEmployeeno()!=null&&publicOrder.getEmployeeno().length()>0){
			sqlwhere +=" and a.employee_no = '"+publicOrder.getEmployeeno()+"'";
		}
		if(publicOrder.getAutostatus()!=null&&publicOrder.getAutostatus().toString().equals("0")){
			//未分配
			sqlwhere +=" and (a.employee_no is null or a.employee_no ='') ";
		}else if(publicOrder.getAutostatus()!=null&&publicOrder.getAutostatus().toString().equals("1")){
			//已分配
			sqlwhere +=" and a.employee_no is NOT NULL and a.employee_no <>''";
		}
		
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
	    Calendar cal = Calendar.getInstance(); 
	    cal.setTime(new Date());
		if(publicOrder.getSignedtime()!=null&&publicOrder.getSignedtime().equals("cur_month")){
			//取本月
			cal.add(Calendar.MONTH, 0);
			String cur_month = simpleDateFormat.format(cal.getTime());
			sqlwhere +=" and DATE_FORMAT(a.df_signed_time,'%Y-%m') = '"+cur_month+"'";
		}else if(publicOrder.getSignedtime()!=null&&publicOrder.getSignedtime().equals("prev_month")){
			//取上月 
			cal.add(Calendar.MONTH, -1);
			String pre_month = simpleDateFormat.format(cal.getTime());
			sqlwhere +=" and DATE_FORMAT(a.df_signed_time,'%Y-%m') = '"+pre_month+"'";
		}
		
		if(publicOrder.getSignedtimeymd()!=null&&publicOrder.getSignedtimeymd().length()>0){
			sqlwhere +=" and DATE_FORMAT(a.df_signed_time,'%Y-%m-%d') = '"+publicOrder.getSignedtimeymd()+"'";
		}
		
		//sqlwhere +=" ORDER BY a.order_num DESC,a.update_time  ";
		
		// TODO Auto-generated method stub
		//String sql = " SELECT * FROM ((SELECT *,'1' as order_num FROM df_order_pubseas_monthly WHERE (df_customer_id NOT like '%fakecustomerformicromarket%' or df_customer_id is NULL) ) UNION ALL (SELECT *,'0' as order_num FROM df_order_pubseas_monthly WHERE df_customer_id like '%fakecustomerformicromarket%')) a   where "+sqlwhere;
		//String sql = " select * from (SELECT df_order_pubseas_monthly.*,(case when df_order_pubseas_monthly.df_customer_id like 'fakecustomerformicromarket%' then '0' ELSE '1' END) as  order_num FROM df_order_pubseas_monthly WHERE df_order_pubseas_monthly.df_ispubseas=1) a   where "+sqlwhere + " ORDER BY a.order_num DESC,a.update_time ";
		String sql = "SELECT * FROM df_order_pubseas_monthly a where "+sqlwhere + " ORDER BY a.update_time";
		String sql_count="select COUNT(1) as total,sum(df_trading_price) as totalprice from df_order_pubseas_monthly a where "+sqlwhere;
		
		//String sql_count = "SELECT COUNT(1) as total FROM (" + sqlcount + ") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql_count);
		//Object total = query_count.uniqueResult();
		List<?> total = query_count
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		Map<String, Object> maps = (Map<String, Object>) total.get(0);
		
		pageInfo.setTotalRecords(Integer.valueOf(maps.get("total").toString()));

		Query query = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);

		List<?> list = query
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(
						pageInfo.getRecordsPerPage()
								* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();

		List<Map<String, Object>> rtList = new ArrayList<Map<String,Object>>();
		//处理日期 
		if(list!=null&&list.size()>0){
			for(Object o:list){
				Map<String, Object> mapObj = (Map<String, Object>) o;
				String signed_time =mapObj.get("df_signed_time")==null?"":mapObj.get("df_signed_time").toString();
				
				SimpleDateFormat simpleDateFormatYM = new SimpleDateFormat("yyyy-MM");
				SimpleDateFormat simpleDateFormatNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    Calendar calendar= Calendar.getInstance(); 
			    calendar.setTime(new Date());
			    calendar.add(Calendar.MONTH, 0);
				String cur_month_one = simpleDateFormatYM.format(calendar.getTime())+"-01 18:00:00";
				String now_month_day = simpleDateFormatNow.format(new Date());
				int ret = compare_date(now_month_day, cur_month_one);
				if(ret>0&&signed_time.length()>7){
					/*System.out.println("当前时间："+now_month_day);
					System.out.println("本月六点："+cur_month_one);
					System.out.println("判断如果是 上个月的数据不可以分配，为公海均分！！！");*/
					calendar.add(Calendar.MONTH, -1);
					System.out.println("上个月："+simpleDateFormatYM.format(calendar.getTime()));
					String pre_month = simpleDateFormatYM.format(calendar.getTime());
					String sign_month = signed_time.substring(0,7);
					System.out.println("签收月："+sign_month);
					if(pre_month.equals(sign_month)){
						mapObj.put("order_type", "999");
					}else{
						mapObj.put("order_type", "");
					}
				}else{
					mapObj.put("order_type", "");
				}
				rtList.add(mapObj);
			}
		}
		
		Map<String, Object> map_result = new HashMap<String, Object>();
		Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("data", rtList);
		map_result.put("total_pages", total_pages);
		String rt_totalprice = maps.get("totalprice")==null?"0":maps.get("totalprice").toString();
		map_result.put("totalprice", rt_totalprice);
		return map_result;
	}
	
	
	@Override
	public List<Map<String, Object>> exportPublicOrder(PublicOrder publicOrder){
		String sqlwhere = " a.df_ispubseas=1 ";
		
		if(publicOrder.getOrdertype()!=null&&publicOrder.getOrdertype().equals("0")){
			//公海订单
			sqlwhere+= " and a.df_customer_id not like 'fakecustomer%' ";
		}else if(publicOrder.getOrdertype()!=null&&publicOrder.getOrdertype().equals("1")){
			//微超订单
			sqlwhere+= " and a.df_customer_id like 'fakecustomer%' ";
		}
		
		if(publicOrder.getOrdersn()!=null&&publicOrder.getOrdersn().length()>0){
			sqlwhere += " and a.df_order_sn = '"+publicOrder.getOrdersn()+"'";
		}
		if(publicOrder.getStoreno()!=null&&publicOrder.getStoreno().length()>0){
			sqlwhere += " and a.df_storeno = '"+publicOrder.getStoreno()+"'";
		}else{
			//查询当前登录人所管理城的城市的所有门店
			if(publicOrder.getStorenos()!=null&&publicOrder.getStorenos().length()>0){
				sqlwhere +=" and a.df_storeno in("+publicOrder.getStorenos()+")";
			}else{
				sqlwhere +=" and 1=0 ";
			}
		}
		
		if(publicOrder.getEmployeeno()!=null&&publicOrder.getEmployeeno().length()>0){
			sqlwhere +=" and a.employee_no = '"+publicOrder.getEmployeeno()+"'";
		}
		if(publicOrder.getAutostatus()!=null&&publicOrder.getAutostatus().toString().equals("0")){
			//未分配
			sqlwhere +=" and (a.employee_no is null or a.employee_no ='') ";
		}else if(publicOrder.getAutostatus()!=null&&publicOrder.getAutostatus().toString().equals("1")){
			//已分配
			sqlwhere +=" and a.employee_no is NOT NULL and a.employee_no <>''";
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
	    Calendar cal = Calendar.getInstance(); 
	    cal.setTime(new Date());
		if(publicOrder.getSignedtime()!=null&&publicOrder.getSignedtime().equals("cur_month")){
			//取本月
			cal.add(Calendar.MONTH, 0);
			String cur_month = simpleDateFormat.format(cal.getTime());
			sqlwhere +=" and DATE_FORMAT(a.df_signed_time,'%Y-%m') = '"+cur_month+"'";
		}else if(publicOrder.getSignedtime()!=null&&publicOrder.getSignedtime().equals("prev_month")){
			//取上月 
			cal.add(Calendar.MONTH, -1);
			String pre_month = simpleDateFormat.format(cal.getTime());
			sqlwhere +=" and DATE_FORMAT(a.df_signed_time,'%Y-%m') = '"+pre_month+"'";
		}
		
		if(publicOrder.getSignedtimeymd()!=null&&publicOrder.getSignedtimeymd().length()>0){
			sqlwhere +=" and DATE_FORMAT(a.df_signed_time,'%Y-%m-%d') = '"+publicOrder.getSignedtimeymd()+"'";
		}
		
		//sqlwhere +=" ORDER BY a.order_num DESC,a.update_time  ";
		//TODO Auto-generated method stub
		//String sql = " SELECT * FROM ((SELECT *,'1' as order_num FROM df_order_pubseas_monthly WHERE (df_customer_id NOT like '%fakecustomerformicromarket%' or df_customer_id is NULL) ) UNION ALL (SELECT *,'0' as order_num FROM df_order_pubseas_monthly WHERE df_customer_id like '%fakecustomerformicromarket%')) a   where "+sqlwhere;
		//String sql = " select * from (SELECT df_order_pubseas_monthly.*,(case when df_order_pubseas_monthly.df_customer_id like 'fakecustomerformicromarket%' then '0' ELSE '1' END) as  order_num FROM df_order_pubseas_monthly WHERE df_order_pubseas_monthly.df_ispubseas=1) a   where "+sqlwhere;
		String sql = "SELECT * FROM df_order_pubseas_monthly a where "+sqlwhere + " ORDER BY a.update_time";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	
	
	@Override
	public Map<String, Object> querySearchOrder(PublicOrder publicOrder,
			PageInfo pageInfo) {
		
		String sqlwhere = " 1=1 ";
		
		if(publicOrder.getOrdersn()!=null&&publicOrder.getOrdersn().length()>0){
			sqlwhere += " and a.df_order_sn = '"+publicOrder.getOrdersn()+"'";
		}
		if(publicOrder.getStoreno()!=null&&publicOrder.getStoreno().length()>0){
			sqlwhere += " and a.df_storeno = '"+publicOrder.getStoreno()+"'";
		}else{
			//查询当前登录人所管理城的城市的所有门店 
			if(publicOrder.getStorenos()!=null&&publicOrder.getStorenos().length()>0){
				sqlwhere +=" and a.df_storeno in("+publicOrder.getStorenos()+")";
			}else{
				sqlwhere +=" and 1=0 ";
			}
		}
		if(publicOrder.getEmployeeno()!=null&&publicOrder.getEmployeeno().length()>0){
			sqlwhere +=" and a.employee_no = '"+publicOrder.getEmployeeno()+"'";
		}
		
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
	    Calendar cal = Calendar.getInstance(); 
	    cal.setTime(new Date());
		if(publicOrder.getSignedtime()!=null&&publicOrder.getSignedtime().equals("cur_month")){
			//取本月
			cal.add(Calendar.MONTH, 0);
			String cur_month = simpleDateFormat.format(cal.getTime());
			sqlwhere +=" and DATE_FORMAT(a.df_signed_time,'%Y-%m') = '"+cur_month+"'";
		}else if(publicOrder.getSignedtime()!=null&&publicOrder.getSignedtime().equals("prev_month")){
			//取上月 
			cal.add(Calendar.MONTH, -1);
			String pre_month = simpleDateFormat.format(cal.getTime());
			sqlwhere +=" and DATE_FORMAT(a.df_signed_time,'%Y-%m') = '"+pre_month+"'";
		}
		
		if(publicOrder.getSignedtimeymd()!=null&&publicOrder.getSignedtimeymd().length()>0){
			sqlwhere +=" and DATE_FORMAT(a.df_signed_time,'%Y-%m-%d') = '"+publicOrder.getSignedtimeymd()+"'";
		}
		
		
		if(publicOrder.getDf_order_placename()!=null&&publicOrder.getDf_order_placename().length()>0){
			sqlwhere +=" and df_order_placename like '%"+publicOrder.getDf_order_placename()+"%'";
		}
		if(publicOrder.getDf_tiny_village_name()!=null&&publicOrder.getDf_tiny_village_name().length()>0){
			sqlwhere +=" and df_tiny_village_name like '%"+publicOrder.getDf_tiny_village_name()+"%'";
		}
		if(publicOrder.getDf_area_name()!=null&&publicOrder.getDf_area_name().length()>0){
			sqlwhere +=" and df_area_name like '%"+publicOrder.getDf_area_name()+"%'";
		}
		if(publicOrder.getDf_employee_a_name()!=null&&publicOrder.getDf_employee_a_name().length()>0){
			sqlwhere +=" and df_employee_a_name like '%"+publicOrder.getDf_employee_a_name()+"%'";
		}
		if(publicOrder.getDf_dep_name()!=null&&publicOrder.getDf_dep_name().length()>0){
			sqlwhere +=" and df_dep_name like '%"+publicOrder.getDf_dep_name()+"%'";
		}
		if(publicOrder.getDf_channel_name()!=null&&publicOrder.getDf_channel_name().length()>0){
			sqlwhere +=" and df_channel_name like '%"+publicOrder.getDf_channel_name()+"%'";
		}
		if(publicOrder.getSearchordertype()!=null&&publicOrder.getSearchordertype().length()>0){
			String searchordertype = publicOrder.getSearchordertype();
			if(searchordertype!=null&&searchordertype.equals("1")){
				sqlwhere +=" and df_isabnormal = 1";
			}
			if(searchordertype!=null&&searchordertype.equals("2")){
				sqlwhere +=" and df_ispubseas = 1";
			}
			if(searchordertype!=null&&searchordertype.equals("3")){
				sqlwhere +=" and df_isreturn = 1";
			}
		}
		
		
		String sql = "SELECT * FROM df_order_pubseas_monthly a where "+sqlwhere + " ORDER BY a.update_time";
		String sql_count="select COUNT(1) as total,sum(df_trading_price) as totalprice from df_order_pubseas_monthly a where "+sqlwhere;

		Query query_count = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql_count);
		List<?> total = query_count
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		Map<String, Object> maps = (Map<String, Object>) total.get(0);
		
		pageInfo.setTotalRecords(Integer.valueOf(maps.get("total").toString()));

		Query query = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);

		List<?> list = query
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(
						pageInfo.getRecordsPerPage()
								* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();

		List<Map<String, Object>> rtList = new ArrayList<Map<String,Object>>();
		//处理日期 
		if(list!=null&&list.size()>0){
			for(Object o:list){
				Map<String, Object> mapObj = (Map<String, Object>) o;
				String signed_time =mapObj.get("df_signed_time")==null?"":mapObj.get("df_signed_time").toString();
				
				SimpleDateFormat simpleDateFormatYM = new SimpleDateFormat("yyyy-MM");
				SimpleDateFormat simpleDateFormatNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    Calendar calendar= Calendar.getInstance(); 
			    calendar.setTime(new Date());
			    calendar.add(Calendar.MONTH, 0);
				String cur_month_one = simpleDateFormatYM.format(calendar.getTime())+"-01 18:00:00";
				String now_month_day = simpleDateFormatNow.format(new Date());
				int ret = compare_date(now_month_day, cur_month_one);
				if(ret>0&&signed_time.length()>7){
					calendar.add(Calendar.MONTH, -1);
					System.out.println("上个月："+simpleDateFormatYM.format(calendar.getTime()));
					String pre_month = simpleDateFormatYM.format(calendar.getTime());
					String sign_month = signed_time.substring(0,7);
					System.out.println("签收月："+sign_month);
					if(pre_month.equals(sign_month)){
						mapObj.put("order_type", "999");
					}else{
						mapObj.put("order_type", "");
					}
				}else{
					mapObj.put("order_type", "");
				}
				rtList.add(mapObj);
			}
		}
		
		Map<String, Object> map_result = new HashMap<String, Object>();
		Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("data", rtList);
		map_result.put("total_pages", total_pages);
		String rt_totalprice = maps.get("totalprice")==null?"0":maps.get("totalprice").toString();
		map_result.put("totalprice", rt_totalprice);
		return map_result;
	}
	
	
	
	@Override
	public List<Map<String, Object>> exportSearchOrder(PublicOrder publicOrder){
		String sqlwhere = " 1=1 ";
		
		if(publicOrder.getOrdersn()!=null&&publicOrder.getOrdersn().length()>0){
			sqlwhere += " and a.df_order_sn = '"+publicOrder.getOrdersn()+"'";
		}
		if(publicOrder.getStoreno()!=null&&publicOrder.getStoreno().length()>0){
			sqlwhere += " and a.df_storeno = '"+publicOrder.getStoreno()+"'";
		}else{
			//查询当前登录人所管理城的城市的所有门店
			if(publicOrder.getStorenos()!=null&&publicOrder.getStorenos().length()>0){
				sqlwhere +=" and a.df_storeno in("+publicOrder.getStorenos()+")";
			}else{
				sqlwhere +=" and 1=0 ";
			}
		}
		
		if(publicOrder.getEmployeeno()!=null&&publicOrder.getEmployeeno().length()>0){
			sqlwhere +=" and a.employee_no = '"+publicOrder.getEmployeeno()+"'";
		}
		if(publicOrder.getAutostatus()!=null&&publicOrder.getAutostatus().toString().equals("0")){
			//未分配
			sqlwhere +=" and (a.employee_no is null or a.employee_no ='') ";
		}else if(publicOrder.getAutostatus()!=null&&publicOrder.getAutostatus().toString().equals("1")){
			//已分配
			sqlwhere +=" and a.employee_no is NOT NULL and a.employee_no <>''";
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
	    Calendar cal = Calendar.getInstance(); 
	    cal.setTime(new Date());
		if(publicOrder.getSignedtime()!=null&&publicOrder.getSignedtime().equals("cur_month")){
			//取本月
			cal.add(Calendar.MONTH, 0);
			String cur_month = simpleDateFormat.format(cal.getTime());
			sqlwhere +=" and DATE_FORMAT(a.df_signed_time,'%Y-%m') = '"+cur_month+"'";
		}else if(publicOrder.getSignedtime()!=null&&publicOrder.getSignedtime().equals("prev_month")){
			//取上月 
			cal.add(Calendar.MONTH, -1);
			String pre_month = simpleDateFormat.format(cal.getTime());
			sqlwhere +=" and DATE_FORMAT(a.df_signed_time,'%Y-%m') = '"+pre_month+"'";
		}
		
		if(publicOrder.getSignedtimeymd()!=null&&publicOrder.getSignedtimeymd().length()>0){
			sqlwhere +=" and DATE_FORMAT(a.df_signed_time,'%Y-%m-%d') = '"+publicOrder.getSignedtimeymd()+"'";
		}
		
		
		if(publicOrder.getDf_order_placename()!=null&&publicOrder.getDf_order_placename().length()>0){
			sqlwhere +=" and df_order_placename like '%"+publicOrder.getDf_order_placename()+"%'";
		}
		if(publicOrder.getDf_tiny_village_name()!=null&&publicOrder.getDf_tiny_village_name().length()>0){
			sqlwhere +=" and df_tiny_village_name like '%"+publicOrder.getDf_tiny_village_name()+"%'";
		}
		if(publicOrder.getDf_area_name()!=null&&publicOrder.getDf_area_name().length()>0){
			sqlwhere +=" and df_area_name like '%"+publicOrder.getDf_area_name()+"%'";
		}
		if(publicOrder.getDf_employee_a_name()!=null&&publicOrder.getDf_employee_a_name().length()>0){
			sqlwhere +=" and df_employee_a_name like '%"+publicOrder.getDf_employee_a_name()+"%'";
		}
		if(publicOrder.getDf_dep_name()!=null&&publicOrder.getDf_dep_name().length()>0){
			sqlwhere +=" and df_dep_name like '%"+publicOrder.getDf_dep_name()+"%'";
		}
		if(publicOrder.getDf_channel_name()!=null&&publicOrder.getDf_channel_name().length()>0){
			sqlwhere +=" and df_channel_name like '%"+publicOrder.getDf_channel_name()+"%'";
		}
		if(publicOrder.getSearchordertype()!=null&&publicOrder.getSearchordertype().length()>0){
			String searchordertype = publicOrder.getSearchordertype();
			if(searchordertype!=null&&searchordertype.equals("1")){
				sqlwhere +=" and df_isabnormal = 1";
			}
			if(searchordertype!=null&&searchordertype.equals("2")){
				sqlwhere +=" and df_ispubseas = 1";
			}
			if(searchordertype!=null&&searchordertype.equals("3")){
				sqlwhere +=" and df_isreturn = 1";
			}
		}
		
		
		//sqlwhere +=" ORDER BY a.order_num DESC,a.update_time  ";
		//TODO Auto-generated method stub
		//String sql = " SELECT * FROM ((SELECT *,'1' as order_num FROM df_order_pubseas_monthly WHERE (df_customer_id NOT like '%fakecustomerformicromarket%' or df_customer_id is NULL) ) UNION ALL (SELECT *,'0' as order_num FROM df_order_pubseas_monthly WHERE df_customer_id like '%fakecustomerformicromarket%')) a   where "+sqlwhere;
		//String sql = " select * from (SELECT df_order_pubseas_monthly.*,(case when df_order_pubseas_monthly.df_customer_id like 'fakecustomerformicromarket%' then '0' ELSE '1' END) as  order_num FROM df_order_pubseas_monthly WHERE df_order_pubseas_monthly.df_ispubseas=1) a   where "+sqlwhere;
		String sql = "SELECT * FROM df_order_pubseas_monthly a where "+sqlwhere + " ORDER BY a.update_time";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int compare_date(String DATE1, String DATE2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
	
}
