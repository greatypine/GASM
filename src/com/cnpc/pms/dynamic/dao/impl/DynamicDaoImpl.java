/**
 * gaobaolei
 */
package com.cnpc.pms.dynamic.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.IJoin;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.dao.DynamicDao;
import com.cnpc.pms.dynamic.entity.AbnormalOrderDto;
import com.cnpc.pms.dynamic.entity.DynamicDto;

/**
 * @author gaobaolei
 *
 */
public class DynamicDaoImpl extends BaseDAOHibernate implements DynamicDao{

	
	@Override
	public int getNewaddcus(DynamicDto dd) {
		String sub_str="";
		if(dd.getTarget()==1){//城市总监
			if(dd.getEmployeeId()!=null&&!"".equals(dd.getEmployeeId())){
				sub_str= "   tor.store_code in (select t.storeno from t_store t	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM	t_dist_city a INNER JOIN "
						+"	 t_dist_citycode tdc ON a.citycode  = tdc.citycode and a.pk_userid="+dd.getEmployeeId()+" and tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.flag=0 "
						+"   AND t.status != '闭店中')";
			}else{
				sub_str= "   tor.store_code in ( select t.storeno	from t_store t inner join 	(SELECT tdc.id,tdc.cityname FROM "
						+"	 t_dist_citycode tdc where tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.flag=0 "
						+"   AND t.status != '闭店中')";
			}
			
		}else if(dd.getTarget()==2){//店长
			sub_str = "  tor.store_code='"+dd.getStoreNo()+"'";
		}
		String sql="select  ifnull(sum(case when customer_isnew_flag >='10' then 1 else 0 end),0) as new_10_count "+
				   " from df_mass_order_monthly tor where customer_isnew_flag !='-1' "+
				   " and tor.sign_time >='"+dd.getBeginDate()+"' and tor.sign_time <'"+dd.getEndDate()+"' "+
				   " and "+sub_str+
				   " and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA' "+
				   " and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0  ";
		
		 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	     return Integer.valueOf(query.uniqueResult().toString());
	}
 
	@Override
	public int  getNewaddcusOfGAX(DynamicDto dd){
		String sub_str="";
		if(dd.getTarget()==1){//城市总监
			if(dd.getEmployeeId()!=null&&!"".equals(dd.getEmployeeId())){
				sub_str= "   tor.store_code in (select t.storeno from t_store t	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM	t_dist_city a INNER JOIN "
						+"	 t_dist_citycode tdc ON a.citycode  = tdc.citycode and a.pk_userid="+dd.getEmployeeId()+" and tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.flag=0 "
						+"   AND t.status != '闭店中')";
			}else{
				sub_str= "   tor.store_code in ( select t.storeno	from t_store t inner join 	(SELECT tdc.id,tdc.cityname FROM "
						+"	 t_dist_citycode tdc where tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.flag=0 "
						+"   AND t.status != '闭店中')";
			}
			
		}else if(dd.getTarget()==2){//店长
			sub_str = "  tor.store_code='"+dd.getStoreNo()+"'";
		}
		String sql="select "+
					" ifnull(sum(case when customer_isnew_flag >='10' then 1 else 0 end),0) as new_10_count "+
					" from df_mass_order_monthly tor "+
					" where customer_isnew_flag !='-1' "+
					" and tor.sign_time >='"+dd.getBeginDate()+"' and tor.sign_time <'"+dd.getEndDate()+"' "+
					" and "+sub_str+
					" and info_employee_a_no is not null and info_employee_a_no !='' "+
					" and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA'  "+
					" and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0 ";
		
		 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	     return Integer.valueOf(query.uniqueResult().toString());
	}
	
	@Override
	public int getRebuycus(DynamicDto dd) {
		
		String sub_str="";
		if(dd.getTarget()==1){//城市总监
			if(dd.getEmployeeId()!=null&&!"".equals(dd.getEmployeeId())){
				sub_str= "	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM	t_dist_city a INNER JOIN "
						+"	 t_dist_citycode tdc ON a.citycode  = tdc.citycode and a.pk_userid="+dd.getEmployeeId()+" and tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.platformid IS NOT NULL "
						+"   AND t.platformid != ''";
			}else{
				sub_str= "	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM "
						+"	 t_dist_citycode tdc where tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.platformid IS NOT NULL "
						+"   AND t.platformid != ''";
			}
		}else if(dd.getTarget()==2){//店长
			sub_str = " where t.store_id="+dd.getStoreId();
		}
		String sql="SELECT IFNULL(SUM(IFNULL(aa.rebuy_20_count,0)),0) AS amount FROM ds_rebuycus aa  INNER JOIN "
				+"  (SELECT t.store_id,t.platformid FROM t_store t "
				+ sub_str
				+"  ) bb on aa.platformid = bb.platformid and aa.year="+dd.getYear()+" and aa.month= "+dd.getMonth();
		
		 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	     return Integer.valueOf(query.uniqueResult().toString());
	}

	
	@Override
	public double getStoretrade(DynamicDto dd) {
		
		String sub_str="";
		if(dd.getTarget()==1){//城市总监
			if(dd.getEmployeeId()!=null&&!"".equals(dd.getEmployeeId())){
				sub_str= "   and storeno in (select t.storeno from t_store t	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM	t_dist_city a INNER JOIN "
						+"	 t_dist_citycode tdc ON a.citycode  = tdc.citycode and a.pk_userid="+dd.getEmployeeId()+" and tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.flag=0 "
						+"   AND t.status != '闭店中')";
			}else{
				sub_str= "   and storeno in ( select t.storeno	from t_store t inner join 	(SELECT tdc.id,tdc.cityname FROM "
						+"	 t_dist_citycode tdc where tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.flag=0 "
						+"   AND t.status != '闭店中')";
			}
		}else if(dd.getTarget()==2){//店长
			sub_str = " and storeno='"+dd.getStoreNo()+"'";
		}
		 String sql="select ifnull(sum(ifnull(pesgmv,0)),0) as amount  from ds_storetrade where year="+dd.getYear()+" and month="+dd.getMonth()+sub_str;
		
		 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	    
		 return Double.valueOf(query.uniqueResult().toString());
	}

	
	public double  getStoretradeOfGAX(DynamicDto dd){
		String sub_str="";
		if(dd.getTarget()==1){//城市总监
			if(dd.getEmployeeId()!=null&&!"".equals(dd.getEmployeeId())){
				sub_str= "   and storeno in (select t.storeno from t_store t	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM	t_dist_city a INNER JOIN "
						+"	 t_dist_citycode tdc ON a.citycode  = tdc.citycode and a.pk_userid="+dd.getEmployeeId()+" and tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.flag=0 "
						+"   AND t.status != '闭店中')";
			}else{
				sub_str= "   and storeno in ( select t.storeno	from t_store t inner join 	(SELECT tdc.id,tdc.cityname FROM "
						+"	 t_dist_citycode tdc where tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.flag=0 "
						+"   AND t.status != '闭店中')";
			}
		}else if(dd.getTarget()==2){//店长
			sub_str = " and storeno='"+dd.getStoreNo()+"'";
		}
		 String sql="select ifnull(sum(ifnull(pesgmv,0)),0) as amount  from ds_emptrade where year="+dd.getYear()+" and month="+dd.getMonth()+sub_str;
		
		 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	    
		 return Double.valueOf(query.uniqueResult().toString());
	}
	
	@Override
	public int getSendorders(DynamicDto dd) {
		String sub_str="";
		if(dd.getTarget()==1){//城市总监
			if(dd.getEmployeeId()!=null&&!"".equals(dd.getEmployeeId())){
				sub_str= "	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM	t_dist_city a INNER JOIN "
						+"	 t_dist_citycode tdc ON a.citycode  = tdc.citycode and a.pk_userid="+dd.getEmployeeId()+" and tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.platformid IS NOT NULL "
						+"   AND t.platformid != ''";
			}else{
				sub_str= "	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM "
						+"	 t_dist_citycode tdc where tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.platformid IS NOT NULL "
						+"   AND t.platformid != ''";
			}
			
		}else if(dd.getTarget()==2){//店长
			sub_str = " where t.store_id="+dd.getStoreId();
		}else if(dd.getTarget()==3){//国安侠
			dd.setEmployeeNo(dd.getEmployeeName()+"+"+dd.getEmployeeNo());
			String sql="SELECT IFNULL(SUM(IFNULL(aa.datanum,0)),0) AS amount FROM ds_sendorders aa  where username='"+dd.getEmployeeNo()+"' and aa.year="+dd.getYear()+" and aa.month= "+dd.getMonth();
			
			 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		     return Integer.valueOf(query.uniqueResult().toString());
		}
		String sql="SELECT IFNULL(SUM(IFNULL(aa.datanum,0)),0) AS amount FROM ds_sendorders aa  INNER JOIN "
				+"  (SELECT t.store_id,t.platformid FROM t_store t "
				+ sub_str
				+"  ) bb on aa.platformid = bb.platformid and aa.year="+dd.getYear()+" and aa.month= "+dd.getMonth();
		
		 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	     return Integer.valueOf(query.uniqueResult().toString());
		
	}

	
	@Override
	public int getRewardtimes(DynamicDto dd) {
		
		String sub_str="";
		if(dd.getTarget()==1){//城市总监
			
			if(dd.getEmployeeId()!=null&&!"".equals(dd.getEmployeeId())){
				sub_str= "	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM	t_dist_city a INNER JOIN "
						+"	 t_dist_citycode tdc ON a.citycode  = tdc.citycode and a.pk_userid="+dd.getEmployeeId()+" and tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.platformid IS NOT NULL "
						+"   AND t.platformid != ''";
			}else{
				sub_str= "	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM "
						+"	 t_dist_citycode tdc where tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.platformid IS NOT NULL "
						+"   AND t.platformid != ''";
			}
		}else if(dd.getTarget()==2){//店长
			sub_str = " where t.store_id="+dd.getStoreId();
		}
		String sql="SELECT IFNULL(SUM(IFNULL(aa.dashang,0)),0) AS amount FROM ds_rewardtimes aa  INNER JOIN "
				+"  (SELECT t.store_id,t.platformid FROM t_store t "
				+ sub_str
				+"  ) bb on aa.platformid = bb.platformid and aa.year="+dd.getYear()+" and aa.month= "+dd.getMonth();
		
		 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	     return Integer.valueOf(query.uniqueResult().toString());
	}


	
	@Override
	public int getRelation(DynamicDto dd) {
	     String sub_str="";
	     if(dd.getTarget()==1){//城市总监
	    	     if(dd.getEmployeeId()!=null&&!"".equals(dd.getEmployeeId())){
	    	    	 sub_str= "	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM	t_dist_city a INNER JOIN "
	 						+"	 t_dist_citycode tdc ON a.citycode  = tdc.citycode and a.pk_userid="+dd.getEmployeeId()+" and tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
	 						+"   AND t.platformid IS NOT NULL "
	 						+"   AND t.platformid != ''";
	    	     }else{
	    	    	 sub_str= "	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM "
	 						+"	 t_dist_citycode tdc where  tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
	 						+"   AND t.platformid IS NOT NULL "
	 						+"   AND t.platformid != ''";
	    	     }
				 
			}else if(dd.getTarget()==2){//店长
				 sub_str = " where t.store_id="+dd.getStoreId();
			}else if(dd.getTarget()==3){//国安侠
				 String sql="SELECT ifnull(SUM(IFNULL(aa.relationnum,0)),0) AS amount FROM ds_topdata aa  where aa.persontype = 2 and employeeno='"+dd.getEmployeeNo()+"' and aa.year="+dd.getYear()+" and aa.month= "+dd.getMonth();
				 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			     return Integer.valueOf(query.uniqueResult().toString());
			}
			String sql="SELECT ifnull(SUM(IFNULL(aa.storerelationnum,0)),0) AS amount FROM ds_topdata aa  INNER JOIN "
					+"  (SELECT t.store_id,t.platformid FROM t_store t "
					+ sub_str
					+"  ) bb on aa.storeid = bb.store_id and aa.persontype = 1 and aa.year="+dd.getYear()+" and aa.month= "+dd.getMonth();
			
			 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		     return Integer.valueOf(query.uniqueResult().toString());
	}


	
	@Override
	public int getCustomer(DynamicDto dd) {
	    String sub_str="";
	    if(dd.getTarget()==1){//城市总监
	    	if(dd.getEmployeeId()!=null&&!"".equals(dd.getEmployeeId())){
   	    	 sub_str= "	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM	t_dist_city a INNER JOIN "
						+"	 t_dist_citycode tdc ON a.citycode  = tdc.citycode and a.pk_userid="+dd.getEmployeeId()+" and tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.platformid IS NOT NULL "
						+"   AND t.platformid != ''";
   	     }else{
   	    	 sub_str= "	INNER JOIN	(SELECT tdc.id,tdc.cityname FROM "
						+"	 t_dist_citycode tdc where  tdc.id = "+dd.getCityId()+" ) t1 ON t.city_name = t1.cityname "
						+"   AND t.platformid IS NOT NULL "
						+"   AND t.platformid != ''";
   	     }
		}else if(dd.getTarget()==2){//店长
			sub_str = " where t.store_id="+dd.getStoreId();
		}else if(dd.getTarget()==3){//国安侠
			String sql="SELECT ifnull(SUM(IFNULL(aa.cusgrade2,0)),0) AS amount FROM ds_topdata aa where  employeeno='"+dd.getEmployeeNo()+"' and aa.persontype = 2 and aa.year="+dd.getYear()+" and aa.month= "+dd.getMonth();
			
			 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		     return Integer.valueOf(query.uniqueResult().toString());
		}
		String sql="SELECT ifnull(SUM(IFNULL(aa.storecusgrade2,0)),0) AS amount FROM ds_topdata aa  INNER JOIN "
				+"  (SELECT t.store_id,t.platformid FROM t_store t "
				+ sub_str
				+"  ) bb on aa.storeid = bb.store_id and aa.persontype = 1 and aa.year="+dd.getYear()+" and aa.month= "+dd.getMonth();
		
		 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	     return Integer.valueOf(query.uniqueResult().toString());
	}


	
	@Override
	public int getBusinessInfo(DynamicDto dd) {
		
		String sql="";
		if(dd.getTarget()==1){//城市总监
			if(dd.getEmployeeId()!=null&&!"".equals(dd.getEmployeeId())){
				 sql = "select ifnull(count(ttbi.id),0) as amount from "
							+" (select aa.id from t_city aa ,"
							+" (SELECT tdc.id,tdc.cityname FROM t_dist_city a "
							
							+" INNER JOIN t_dist_citycode tdc ON a.pk_userid="+dd.getEmployeeId()+" and tdc.id="+dd.getCityId()+") as bb where aa.`name` like CONCAT('%',bb.cityname,'%')) as cc "
							+" INNER JOIN t_county tc "
							+" on cc.id = tc.city_id "
							+" INNER JOIN t_town tt "
							+" on tc.id= tt.county_id "
							+" INNER JOIN "
							+" t_town_business_info ttbi "
							+" on tt.id = ttbi.town_id and  DATE_FORMAT(ttbi.create_time,'%Y-%c') = CONCAT('"+dd.getYear()+"','-','"+dd.getMonth()+"') ";
			}else{
				 sql = "select ifnull(count(ttbi.id),0) as amount from "
							+" (select aa.id from t_city aa ,"
							+" (SELECT tdc.id,tdc.cityname FROM"
							+" t_dist_citycode tdc where tdc.id="+dd.getCityId()+") as bb where aa.`name` like CONCAT('%',bb.cityname,'%')) as cc "
							+" INNER JOIN t_county tc "
							+" on cc.id = tc.city_id "
							+" INNER JOIN t_town tt "
							+" on tc.id= tt.county_id "
							+" INNER JOIN "
							+" t_town_business_info ttbi "
							+" on tt.id = ttbi.town_id and  DATE_FORMAT(ttbi.create_time,'%Y-%c') = CONCAT('"+dd.getYear()+"','-','"+dd.getMonth()+"') ";
			}
			
		}else if(dd.getTarget()==2){//店长
			 sql = "select ifnull(count(ttbi.id),0) as amount from "
						+" t_store t inner join "
						+" t_town_business_info ttbi "
						+" on t.town_id = ttbi.town_id and t.store_id="+dd.getStoreId()+"  DATE_FORMAT(ttbi.create_time,'%Y-%c') = CONCAT('"+dd.getYear()+"','-','"+dd.getMonth()+"') ";
		}
		
		 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	     return Integer.valueOf(query.uniqueResult().toString());
	}


	
	@Override
	public int getOfficeInfo(DynamicDto dd) {
		String sql="";
		if(dd.getTarget()==1){//城市总监
			if(dd.getEmployeeId()!=null&&!"".equals(dd.getEmployeeId())){
				 sql = "select ifnull(count(ttbi.id),0) as amount from "
							+" (select aa.id from t_city aa ,"
							+" (SELECT tdc.id,tdc.cityname FROM t_dist_city a "
							
							+" INNER JOIN t_dist_citycode tdc ON a.pk_userid="+dd.getEmployeeId()+" and tdc.id="+dd.getCityId()+") as bb where aa.`name` like CONCAT('%',bb.cityname,'%')) as cc "
							+" INNER JOIN t_county tc "
							+" on cc.id = tc.city_id "
							+" INNER JOIN t_town tt "
							+" on tc.id= tt.county_id "
							+" INNER JOIN "
							+" t_town_business_info ttbi "
							+" on tt.id = ttbi.town_id and  DATE_FORMAT(ttbi.create_time,'%Y-%c') = CONCAT('"+dd.getYear()+"','-','"+dd.getMonth()+"') ";
			}else{
				 sql = "select ifnull(count(ttbi.id),0) as amount from "
							+" (select aa.id from t_city aa ,"
							+" (SELECT tdc.id,tdc.cityname FROM"
							+" t_dist_citycode tdc where tdc.id="+dd.getCityId()+") as bb where aa.`name` like CONCAT('%',bb.cityname,'%')) as cc "
							+" INNER JOIN t_county tc "
							+" on cc.id = tc.city_id "
							+" INNER JOIN t_town tt "
							+" on tc.id= tt.county_id "
							+" INNER JOIN "
							+" t_town_business_info ttbi "
							+" on tt.id = ttbi.town_id and  DATE_FORMAT(ttbi.create_time,'%Y-%c') = CONCAT('"+dd.getYear()+"','-','"+dd.getMonth()+"') ";
			}
		}else if(dd.getTarget()==2){//店长
			 sql = "select ifnull(count(ttbi.id),0) as amount from "
						+" t_store t inner join "
						+" t_office_info toi "
						+" on t.town_id = toi.town_id and t.store_id="+dd.getStoreId()+" and  DATE_FORMAT(toi.create_time,'%Y-%c') = CONCAT('"+dd.getYear()+"','-','"+dd.getMonth()+"') ";
		}
		
		 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	     return Integer.valueOf(query.uniqueResult().toString());
	}


	
	@Override
	public List<Map<String, Object>> getStoretradeList(Long cityId, Long employeeId, Integer year, Integer month,
			String flag) {
		String sql="SELECT aa.* FROM ds_storetrade aa  INNER JOIN "
				+"  (SELECT t.store_id,t.platformid FROM t_store t INNER JOIN "
				+"	(SELECT tdc.id,tdc.cityname FROM	t_dist_city a INNER JOIN "
				
				+"	 t_dist_citycode tdc ON a.citycode = tdc.citycode and a.pk_userid="+employeeId+" and tdc.id="+cityId+") t1 ON t.city_name = t1.cityname "
				+"   AND t.platformid IS NOT NULL "
				+"   AND t.platformid != '') bb on aa.platformid = bb.platformid and aa.year="+year+" and aa.month= "+month;
		
		 //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);

        
        //获得查询数据
        List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

        List<Map<String,Object>> lst_result = new ArrayList<Map<String, Object>>();

        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return lst_result;
        }
       
        return lst_result;
	}


	
	@Override
	public List<Map<String, Object>> getStoreOrderList(Long cityId, String employeeId, Integer year, Integer month,
			String flag) {
		
		return null;
	}


	
	@Override
	public List<Map<String, Object>> getRelationList(Long cityId, String employeeId, Integer year, Integer month,
			String flag) {
		
		return null;
	}


	
	@Override
	public int getAllHouseAmount(Long cityId, Long employeeId,Integer house_type) {
		String sql="";
		if(house_type==0){
			 sql = "select ifnull(count(th.id),0) as amount from "
					+" (select aa.id from t_city aa ,"
					+" (SELECT a.id,a.cityname from  t_dist_citycode a inner join t_dist_city b on a.citycode = b.citycode and b.pk_userid="+employeeId+" and a.id="+cityId+") as bb where aa.`name` like CONCAT('%',bb.cityname,'%')) as cc "
					+" INNER JOIN t_county tc on cc.id = tc.city_id"
					+" INNER JOIN t_town tt on tc.id= tt.county_id"
					+" INNER JOIN t_village  tv on tt.id = tv.town_id "
					+" INNER JOIN t_tiny_village ttv on ttv.village_id = tv.id"
					+" INNER JOIN t_house th on th.tinyvillage_id = ttv.id ";
		}else if( house_type==1){
			sql = "select ifnull(count(th.id),0) as amount from "
					+" (select aa.id from t_city aa ,"
					+" (SELECT a.id,a.cityname from  t_dist_citycode a inner join t_dist_city b on a.citycode = b.citycode and b.pk_userid="+employeeId+" and a.id="+cityId+") as bb where aa.`name` like CONCAT('%',bb.cityname,'%')) as cc "
					+" INNER JOIN t_county tc on cc.id = tc.city_id"
					+" INNER JOIN t_town tt on tc.id= tt.county_id"
					+" INNER JOIN t_village  tv on tt.id = tv.town_id "
					+" INNER JOIN t_tiny_village ttv on ttv.village_id = tv.id "
					+" INNER JOIN t_building tb on ttv.id = tb.tinyvillage_id "
					+" INNER JOIN t_house th on th.building_id = tb.id ";
		}
		
				
	
	 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
     return Integer.valueOf(query.uniqueResult().toString());
	}


	
	@Override
	public int getAllTinyVillageAmount(Long cityId, Long employeeId) {
		
		String	sql = "select ifnull(count(ttv.id),0) as amount from "
					+" (select aa.id from t_city aa ,"
					+" (SELECT tdc.id,tdc.cityname FROM t_dist_city a "
					+" INNER JOIN t_dist_citycode tdc on a.citycode = tdc.citycode and a.pk_userid = "+employeeId+" and tdc.id = "+cityId+") as bb where aa.`name` like CONCAT('%',bb.cityname,'%')) as cc "
					+" INNER JOIN t_county tc on cc.id = tc.city_id"
					+" INNER JOIN t_town tt on tc.id= tt.county_id"
					+" INNER JOIN t_village  tv on tt.id = tv.town_id "
					+" INNER JOIN t_tiny_village ttv on ttv.village_id = tv.id ";
					
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	 	return Integer.valueOf(query.uniqueResult().toString());
	}


	
	@Override
	public int getAllVillageAmount(Long cityId, Long employeeId) {
		String	sql = "select ifnull(count(tv.id),0) as amount from "
				+" (select aa.id from t_city aa ,"
				+" (SELECT tdc.id,tdc.cityname FROM t_dist_city a "
				+" INNER JOIN t_dist_citycode tdc ON a.citycode = tdc.citycode and a.pk_userid = "+employeeId+" and tdc.id= "+cityId+") as bb where aa.`name` like CONCAT('%',bb.cityname,'%')) as cc "
				+" INNER JOIN t_county tc on cc.id = tc.city_id"
				+" INNER JOIN t_town tt on tc.id= tt.county_id"
				+" INNER JOIN t_village  tv on tt.id = tv.town_id ";
	
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	 	return Integer.valueOf(query.uniqueResult().toString());
	}


	
	@Override
	public int getAllTownAmount(Long cityId, Long employeeId) {
		String	sql = "select ifnull(count(tt.id),0) as amount from "
				+" (select aa.id from t_city aa ,"
				+" (SELECT tdc.id,tdc.cityname FROM t_dist_city a "
				
				+" INNER JOIN t_dist_citycode tdc ON a.citycode = tdc.citycode and a.pk_userid="+employeeId+" and tdc.id="+cityId+") as bb where aa.`name` like CONCAT('%',bb.cityname,'%')) as cc "
				+" INNER JOIN t_county tc on cc.id = tc.city_id"
				+" INNER JOIN t_town tt on tc.id= tt.county_id";
				
	
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	 	return Integer.valueOf(query.uniqueResult().toString());
	}


	/* (non-Javadoc)
	 * @see com.cnpc.pms.dynamic.dao.DynamicDao#queryBusiness(com.cnpc.pms.dynamic.entity.DynamicDto, com.cnpc.pms.base.paging.impl.PageInfo)
	 */
	@Override
	public Map<String, Object> queryBusiness(DynamicDto dd, PageInfo pageInfo) {
		String sql="";
		if(dd.getTarget()==1){//城市总监
			 sql = "select cc.cityname,tt.name as townname,ttbi.business_name from "
					+" (select aa.id,bb.cityname from t_city aa ,"
					+" (SELECT tdc.id,tdc.cityname FROM t_dist_city a "
					
					+" INNER JOIN t_dist_citycode tdc ON a.pk_userid="+dd.getEmployeeId()+" and tdc.id="+dd.getCityId()+") as bb where aa.`name` like CONCAT('%',bb.cityname,'%')) as cc "
					+" INNER JOIN t_county tc "
					+" on cc.id = tc.city_id "
					+" INNER JOIN t_town tt "
					+" on tc.id= tt.county_id "
					+" INNER JOIN "
					+" t_town_business_info ttbi "
					+" on tt.id = ttbi.town_id and  ttbi.business_name like '%"+dd.getSearchstr()+"%'";
		}else if(dd.getTarget()==2){//店长
			 /*sql = "select ifnull(count(ttbi.id),0) as amount from "
						+" t_store t inner join "
						+" t_town_business_info ttbi "
						+" on t.town_id = ttbi.town_id and t.store_id="+dd.getStoreId()+"  DATE_FORMAT(ttbi.create_time,'%Y-%c') = CONCAT('"+dd.getYear()+"','-','"+dd.getMonth()+"') ";*/
		}
		
		String sql_count = "SELECT COUNT(1) FROM ("+sql+") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
		pageInfo.setTotalRecords(Integer.valueOf(query_count.uniqueResult().toString()));

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<?> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

		Map<String,Object> map_result = new HashMap<String,Object>();
		Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
		map_result.put("pageinfo",pageInfo);
		map_result.put("totalpage", total_pages);
		map_result.put("data", list);
		return map_result;
	}


	
	@Override
	public Map<String, Object> queryOffice(DynamicDto dd, PageInfo pageInfo) {
		String sql="";
		if(dd.getTarget()==1){//城市总监
			 sql = "select cc.cityname,tt.name as townname,toi.office_name from "
					+" (select aa.id,bb.cityname from t_city aa ,"
					+" (SELECT tdc.id,tdc.cityname FROM t_dist_city a "
					
					+" INNER JOIN t_dist_citycode tdc ON a.citycode = tdc.citycode and a.pk_userid="+dd.getEmployeeId()+" and tdc.id="+dd.getCityId()+") as bb where aa.`name` like CONCAT('%',bb.cityname,'%')) as cc "
					+" INNER JOIN t_county tc "
					+" on cc.id = tc.city_id "
					+" INNER JOIN t_town tt "
					+" on tc.id= tt.county_id "
					+" INNER JOIN "
					+" t_office_info toi "
					+" on tt.id = toi.town_id and  toi.office_name like '%"+dd.getSearchstr()+"%'";
		}else if(dd.getTarget()==2){//店长
			/* sql = "select ifnull(count(ttbi.id),0) as amount from "
						+" t_store t inner join "
						+" t_office_info toi "
						+" on t.town_id = toi.town_id and t.store_id="+dd.getStoreId()+" and  DATE_FORMAT(toi.create_time,'%Y-%c') = CONCAT('"+dd.getYear()+"','-','"+dd.getMonth()+"') ";*/
		}
		
		
		String sql_count = "SELECT COUNT(1) FROM ("+sql+") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
		pageInfo.setTotalRecords(Integer.valueOf(query_count.uniqueResult().toString()));

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<?> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

		Map<String,Object> map_result = new HashMap<String,Object>();
		Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
		map_result.put("pageinfo",pageInfo);
		map_result.put("totalpage", total_pages);
		map_result.put("pageinfo",pageInfo);
		map_result.put("data", list);
		return map_result;
	}


	
	@Override
	public List<Map<String, Object>> queryBusiness(DynamicDto dd) {
		String sql="";
		if(dd.getTarget()==1){//城市总监
			 sql = "select cc.cityname,tt.name as townname,ttbi.business_name from "
					+" (select aa.id,bb.cityname from t_city aa ,"
					+" (SELECT tdc.id,tdc.cityname FROM t_dist_city a "
					+" INNER JOIN t_dist_citycode tdc ON a.citycode = tdc.citycode and a.pk_userid="+dd.getEmployeeId()+" and tdc.id="+dd.getCityId()+") as bb where aa.`name` like CONCAT('%',bb.cityname,'%')) as cc "
					+" INNER JOIN t_county tc "
					+" on cc.id = tc.city_id "
					+" INNER JOIN t_town tt "
					+" on tc.id= tt.county_id "
					+" INNER JOIN "
					+" t_town_business_info ttbi "
					+" on tt.id = ttbi.town_id and  ttbi.business_name like '%"+dd.getSearchstr()+"%'";
		}else if(dd.getTarget()==2){//店长
			 /*sql = "select ifnull(count(ttbi.id),0) as amount from "
						+" t_store t inner join "
						+" t_town_business_info ttbi "
						+" on t.town_id = ttbi.town_id and t.store_id="+dd.getStoreId()+"  DATE_FORMAT(ttbi.create_time,'%Y-%c') = CONCAT('"+dd.getYear()+"','-','"+dd.getMonth()+"') ";*/
		}
		
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
		
	}


	
	@Override
	public List<Map<String, Object>> queryOffice(DynamicDto dd) {
		String sql="";
		if(dd.getTarget()==1){//城市总监
			 sql = "select cc.cityname,tt.name as townname,toi.office_name from "
					+" (select aa.id,bb.cityname from t_city aa ,"
					+" (SELECT tdc.id,tdc.cityname FROM t_dist_city a "
					
					+" INNER JOIN t_dist_citycode tdc ON a.citycode = tdc.citycode and a.pk_userid="+dd.getEmployeeId()+" and tdc.id="+dd.getCityId()+") as bb where aa.`name` like CONCAT('%',bb.cityname,'%')) as cc "
					+" INNER JOIN t_county tc "
					+" on cc.id = tc.city_id "
					+" INNER JOIN t_town tt "
					+" on tc.id= tt.county_id "
					+" INNER JOIN "
					+" t_office_info toi "
					+" on tt.id = toi.town_id and  toi.office_name like '%"+dd.getSearchstr()+"%'";
		}else if(dd.getTarget()==2){//店长
			/* sql = "select ifnull(count(ttbi.id),0) as amount from "
						+" t_store t inner join "
						+" t_office_info toi "
						+" on t.town_id = toi.town_id and t.store_id="+dd.getStoreId()+" and  DATE_FORMAT(toi.create_time,'%Y-%c') = CONCAT('"+dd.getYear()+"','-','"+dd.getMonth()+"') ";*/
		}
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	
	@Override
	public Map<String, Object> queryAbnormalOrder(AbnormalOrderDto abnormalOrderDto,PageInfo pageInfo) {
		// TODO Auto-generated method stub
		String sql = "select a.*,a.abnortype as description,ifnull(b.gmv_price,0) as gmv_price from ds_abnormal_order a left join  df_mass_order_monthly b on a.ordersn = b.order_sn    where  a.status in ('0','3') ";
		if("can_appeal".equals(abnormalOrderDto.getDatatype())){
			sql="select a.*,a.abnortype as description,ifnull(b.gmv_price,0) as gmv_price,  case a.status when 0 then '未申诉'  when 1  then '申诉中' when 2 then '已驳回' when '3' then '已通过' end as state from ds_abnormal_order a  left join  df_mass_order_monthly b on a.ordersn = b.order_sn  where 1=1 ";
		}
		
		if(abnormalOrderDto.getBeginDate()!=null&&!"".equals(abnormalOrderDto.getBeginDate())){
			sql =sql+ " and DATE_FORMAT(a.signedtime,'%Y/%m/%d') >='"+abnormalOrderDto.getBeginDate()+"'";
		}
		
		if(abnormalOrderDto.getEndDate()!=null&&!"".equals(abnormalOrderDto.getEndDate())){
			sql = sql+ " and DATE_FORMAT(a.signedtime,'%Y/%m/%d') <= '"+abnormalOrderDto.getEndDate()+"'";
		}
		
		if(abnormalOrderDto.getAbnortype()!=null&&!"".equals(abnormalOrderDto.getAbnortype())){
			sql=sql+" and a.abnortype ='"+abnormalOrderDto.getAbnortype()+"'";
		}
		
		if(abnormalOrderDto.getStatus()!=null&&!"".equals(abnormalOrderDto.getStatus())){
			sql=sql+" and a.status ='"+abnormalOrderDto.getStatus()+"'";
		}
		
		if(abnormalOrderDto.getOrdersn()!=null&&!"".equals(abnormalOrderDto.getOrdersn())){
			sql=sql+" and a.ordersn ='"+abnormalOrderDto.getOrdersn()+"'";
		}
		
		if(abnormalOrderDto.getDatatype()!=null&&!"".equals(abnormalOrderDto.getDatatype())){
			if("cannot_appeal".equals(abnormalOrderDto.getDatatype())){
				sql=sql+" and a.datatype !='manual_can_appeal'";
			}else {
				sql=sql+" and a.datatype ='manual_can_appeal'";
			}
			
		}
		
		if(abnormalOrderDto.getStoreno()!=null&&!"".equals(abnormalOrderDto.getStoreno())){
			sql=sql+" and a.storeno ='"+abnormalOrderDto.getStoreno()+"'";
		}
		
		if(abnormalOrderDto.getCityname()!=null&&!"".equals(abnormalOrderDto.getCityname())){
			sql=sql+" and a.cityname like '%"+abnormalOrderDto.getCityname()+"%'";
		}
		
//		if(abnormalOrderDto.getProduct()!=null&&!"".equals(abnormalOrderDto.getProduct())){
//			sql=sql+" and a.cityname like '%"+abnormalOrderDto.getCityname()+"%'";
//		}
		
		if(abnormalOrderDto.getMinPrice()!=null&&!"".equals(abnormalOrderDto.getMinPrice().toString())){
			sql=sql+" and a.tradingprice >= "+abnormalOrderDto.getMinPrice();
		}
		
		if(abnormalOrderDto.getMaxPrice()!=null&&!"".equals(abnormalOrderDto.getMaxPrice().toString())){
			sql=sql+" and a.tradingprice <= "+abnormalOrderDto.getMaxPrice();
		}
		
		if(abnormalOrderDto.getDept()!=null&&!"".equals(abnormalOrderDto.getDept())){
			sql=sql+" and a.deptname like '%"+abnormalOrderDto.getDept()+"%'";
		}
		
		if(abnormalOrderDto.getChannel()!=null&&!"".equals(abnormalOrderDto.getChannel())){
			sql=sql+" and a.channelname like '%"+abnormalOrderDto.getChannel()+"%'";
		}
		if("can_appeal".equals(abnormalOrderDto.getDatatype())){
			sql=sql+" order by a.status,a.updatetime asc";
		}else{
			sql=sql+" order by a.signedtime desc";
		}
		
		String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
		Object total = query_count.uniqueResult();
		pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<?> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

		Map<String,Object> map_result = new HashMap<String,Object>();
		Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
		map_result.put("pageinfo",pageInfo);
		map_result.put("data", list);
		map_result.put("total_pages", total_pages);
		return map_result;
		
	}



	@Override
	public List<Map<String, Object>> queryAbnormalType() {
		String sql="select * from ds_abnormal_type where status=1";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public List<Map<String, Object>> queryAbnormalOrder(AbnormalOrderDto abnormalOrderDto) {
		String sql = "select a.*,a.abnortype as description,ifnull(b.gmv_price,0) as gmv_price,  case a.status when 0 then '未通过'   when '3' then '已通过' else a.status end as state from ds_abnormal_order a left join df_mass_order_monthly b on a.ordersn = b.order_sn    where  a.status in ('0','3') ";
		if("can_appeal".equals(abnormalOrderDto.getDatatype())){
			sql="select a.*,a.abnortype as description,ifnull(b.gmv_price,0) as gmv_price,  case a.status when 0 then '未申诉'  when 1  then '申诉中' when 2 then '已驳回' when '3' then '已通过' end as state from ds_abnormal_order a left join df_mass_order_monthly b on a.ordersn = b.order_sn where 1=1 ";
		}
		
		if(abnormalOrderDto.getBeginDate()!=null&&!"".equals(abnormalOrderDto.getBeginDate())){
			sql =sql+ " and DATE_FORMAT(a.signedtime,'%Y/%m/%d') >='"+abnormalOrderDto.getBeginDate()+"'";
		}
		
		if(abnormalOrderDto.getEndDate()!=null&&!"".equals(abnormalOrderDto.getEndDate())){
			sql = sql+ " and DATE_FORMAT(a.signedtime,'%Y/%m/%d') <= '"+abnormalOrderDto.getEndDate()+"'";
		}
		
		if(abnormalOrderDto.getAbnortype()!=null&&!"".equals(abnormalOrderDto.getAbnortype())){
			sql=sql+" and a.abnortype ='"+abnormalOrderDto.getAbnortype()+"'";
		}
		
		if(abnormalOrderDto.getStatus()!=null&&!"".equals(abnormalOrderDto.getStatus())){
			sql=sql+" and a.status ='"+abnormalOrderDto.getStatus()+"'";
		}
		
		if(abnormalOrderDto.getOrdersn()!=null&&!"".equals(abnormalOrderDto.getOrdersn())){
			sql=sql+" and a.ordersn ='"+abnormalOrderDto.getOrdersn()+"'";
		}
		
		if(abnormalOrderDto.getDatatype()!=null&&!"".equals(abnormalOrderDto.getDatatype())){
			if("cannot_appeal".equals(abnormalOrderDto.getDatatype())){
				sql=sql+" and a.datatype !='manual_can_appeal'";
			}else {
				sql=sql+" and a.datatype ='manual_can_appeal'";
			}
		}
		
		if(abnormalOrderDto.getStoreno()!=null&&!"".equals(abnormalOrderDto.getStoreno())){
			sql=sql+" and a.storeno ='"+abnormalOrderDto.getStoreno()+"'";
		}
		
		if(abnormalOrderDto.getCityname()!=null&&!"".equals(abnormalOrderDto.getCityname())){
			sql=sql+" and a.cityname like  '%"+abnormalOrderDto.getCityname()+"%'";
		}
		
//		if(abnormalOrderDto.getProduct()!=null&&!"".equals(abnormalOrderDto.getProduct())){
//			sql=sql+" and a.cityname like '%"+abnormalOrderDto.getCityname()+"%'";
//		}
		
		if(abnormalOrderDto.getMinPrice()!=null&&!"".equals(abnormalOrderDto.getMinPrice().toString())){
			sql=sql+" and a.tradingprice >= "+abnormalOrderDto.getMinPrice();
		}
		
		if(abnormalOrderDto.getMaxPrice()!=null&&!"".equals(abnormalOrderDto.getMaxPrice().toString())){
			sql=sql+" and a.tradingprice <= "+abnormalOrderDto.getMaxPrice();
		}
		
		if(abnormalOrderDto.getDept()!=null&&!"".equals(abnormalOrderDto.getDept())){
			sql=sql+" and a.deptname like '%"+abnormalOrderDto.getDept()+"%'";
		}
		
		if(abnormalOrderDto.getChannel()!=null&&!"".equals(abnormalOrderDto.getChannel())){
			sql=sql+" and a.channelname like '%"+abnormalOrderDto.getChannel()+"%'";
		}
		
		if("can_appeal".equals(abnormalOrderDto.getDatatype())){
			sql=sql+" order by a.status,a.updatetime asc";
		}else{
			sql=sql+" order by a.signedtime desc";
		}
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}


	
	@Override
	public List<Map<String, Object>> queryAbnormalByOrderSn(String ordersn) {
		// TODO Auto-generated method stub
		String  sql="select id ,storeno from ds_abnormal_order where ordersn='"+ordersn+"'";
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}


	
	@Override
	public List<Map<String, Object>> queryCityByUser(Integer target,Long userid, String name) {
		String sql="select a.id as ctid,a.cityname as name from  t_dist_citycode a inner join t_dist_city b on a.citycode= b.citycode and b.pk_userid="+userid;
		if(target==0||target==3){//总部权限或者事业群
			sql = "select a.id as ctid,a.cityname as name from t_dist_citycode a where 1=1 ";
		}
		if(name!=null){
			sql = sql+" and a.cityname like '%"+name+"%'";
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	    //获得查询数据
	    List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	    return lst_data;
		
	}


	
	@Override
	public List<Map<String, Object>> queryAbnormalType(String descrip) {
		String sql="select * from ds_abnormal_type where description='"+descrip+"' and datatype='manual'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public List<Map<String, Object>> queryStoreTradeOfDept(DynamicDto dynamicDto) {
		String sql = "select SUM(d.gmv) as gmv,name,storeno,career_group,storename,employee_no from (select a.*,b.store_name,b.dep_name,IFNULL(b.gmv,0) as gmv,ifnull(c.name,'') as name,ifnull(c.employee_no,'') as employee_no from "+
					" (select ts.storeno,ts.name as storename,ts.store_id,td.career_group from t_store ts,t_data_human_type td where storeno in ("+dynamicDto.getStoreNo()+")) a LEFT JOIN "+
					" (select store_name,storeno,dep_name,SUM(IFNULL(order_amount,0)) as gmv from ds_storetrade_channel where year="+dynamicDto.getYear()+"  and month="+dynamicDto.getMonth()+" GROUP BY storeno,dep_name) b "+
					" on a.storeno = b.storeno and a.career_group like CONCAT('%',b.dep_name,'%') LEFT JOIN (select th.name,th.employee_no,th.career_group,ts.storeno  from t_humanresources th INNER JOIN t_store ts on th.store_id = ts.store_id  and   th.zw='服务专员' and th.humanstatus = 1) c on  a.career_group  = c.career_group and c.storeno = a.storeno) d  GROUP BY d.storeno,d.career_group,d.employee_no";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@Override
	public Map<String, Object> getLastMonthGMVCityRankingTop10(DynamicDto dd,PageInfo pageInfo) {
		String province_id = dd.getProvinceId()==null?"":String.valueOf(dd.getProvinceId());
		String city_id = dd.getCityId()==null?"":String.valueOf(dd.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		Map<String, Object> maps = new HashMap<String, Object>();
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND t.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and d.id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and d.id='"+province_id+"' ";
		}
		String sql = "SELECT d.id as city_id,ds.city_name,SUM(ds.pesgmv) as gmv_sum,t.province_id," +
				"t.`name` FROM ds_storetrade ds LEFT JOIN t_store t ON ds.storeno=t.storeno left join  t_dist_citycode d on d.cityname=t.city_name  " +
				"WHERE ds.`month`='"+dd.getMonth()+"' and ds.`year`='"+dd.getYear()+"' "+provinceStr+cityStr+" GROUP BY ds.city_name  ORDER BY gmv_sum DESC ";
		String sql_count = "SELECT count(tdd.city_name) as city_count from ("+sql+") tdd ";
		Query query_count = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql_count);
		List<?> total = query_count
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(pageInfo!=null){
			if(total!=null&&total.size()>0){
				maps = (Map<String, Object>) total.get(0);
				pageInfo.setTotalRecords(Integer.valueOf(maps.get("city_count").toString()));
			}else{
				pageInfo.setTotalRecords(Integer.valueOf(0));
			}
		}
		Query query = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);

		List<?> list = null;
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
		Map<String, Object> map_result = new HashMap<String, Object>();
		if(pageInfo!=null){
			Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
			map_result.put("pageinfo", pageInfo);
			map_result.put("total_pages", total_pages);
		}
		map_result.put("gmv", list);
		return map_result;
	}

	//cityId
	@Override
	public Map<String, Object> getLastMonthGMVStoreRankingTop10(DynamicDto dd,PageInfo pageInfo) {
		String province_id = dd.getProvinceId()==null?"":String.valueOf(dd.getProvinceId());
		String city_id = dd.getCityId()==null?"":String.valueOf(dd.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		Map<String, Object> maps = new HashMap<String, Object>();
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND t.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and d.id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and d.id='"+province_id+"' ";
		}
		String sql = "SELECT d.id as city_id,t.city_name as city_name,t.store_id as store_id,ds.store_name as store_name,ds.pesgmv as store_pesgmv,t.province_id " +
				"FROM ds_storetrade ds LEFT JOIN t_store t ON ds.storeno=t.storeno left join t_dist_citycode d on d.cityname=t.city_name  WHERE " +"t.storeno is not null "+
				" and ds.`month`='"+dd.getMonth()+"' " +
				"and ds.`year`='"+dd.getYear()+"' "+provinceStr+cityStr+" ORDER BY store_pesgmv DESC ";
		String sql_count = "SELECT count(tdd.store_name) as store_count from ("+sql+") tdd ";
		Query query_count = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql_count);
		List<?> total = query_count
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(pageInfo!=null){
			if(total!=null&&total.size()>0){
				maps = (Map<String, Object>) total.get(0);
				pageInfo.setTotalRecords(Integer.valueOf(maps.get("store_count").toString()));
			}else{
				pageInfo.setTotalRecords(Integer.valueOf(0));
			}
		}
		Query query = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);

		List<?> list = null;
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
		Map<String, Object> map_result = new HashMap<String, Object>();
		if(pageInfo!=null){
			Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
			map_result.put("pageinfo", pageInfo);
			map_result.put("total_pages", total_pages);
		}
		map_result.put("gmv", list);
		return map_result;
	}


	@Override
	public List<Map<String, Object>> selectAllCity() {
		String sql="select * from  t_dist_citycode ";
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	    //获得查询数据
	    List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	    return lst_data;
	}



	/**
	 * 总部查看：按事业部交易额进行排名
	 * @param dynamicDto
	 * @return
	 */
	//cityId
	@Override
	public Map<String, Object> queryTradeByDepName(DynamicDto dynamicDto,PageInfo pageInfo) {
		String province_id = dynamicDto.getProvinceId()==null?"":String.valueOf(dynamicDto.getProvinceId());
		String city_id = dynamicDto.getCityId()==null?"":String.valueOf(dynamicDto.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		Map<String, Object> maps = new HashMap<String, Object>();
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND ts.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and d.id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and d.id='"+province_id+"' ";
		}
		String sql ="select dsch.city_name as city_name,d.id as city_id,tdht.career_group as dep_name,ifnull(sum(order_amount),0) as order_amount from ds_storetrade_channel dsch "+
					"left join t_store ts on (dsch.storeno = ts.storeno) "+
					"left join t_data_human_type tdht on (tdht.career_group like CONCAT('%',dsch.dep_name,'%')) "+
					"left join t_dist_citycode d on d.cityname=ts.city_name  "+
					"where ts.storeno is not null and year ="+dynamicDto.getYear()+" and month = "+dynamicDto.getMonth()+" AND tdht.career_group IS NOT NULL "+ 
					 provinceStr + cityStr +
					"group by tdht.career_group "+ 
					"order by sum(order_amount) desc ";
		String sql_count = "SELECT count(tdd.dep_name) as dep_count from ("+sql+") tdd ";
		Query query_count = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql_count);
		List<?> total = query_count
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(pageInfo!=null){
			if(total!=null&&total.size()>0){
				maps = (Map<String, Object>) total.get(0);
				pageInfo.setTotalRecords(Integer.valueOf(maps.get("dep_count").toString()));
			}else{
				pageInfo.setTotalRecords(Integer.valueOf(0));
			}
		}
		Query query = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);

		List<?> list = null;
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
		Map<String, Object> map_result = new HashMap<String, Object>();
		if(pageInfo!=null){
			Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
			map_result.put("pageinfo", pageInfo);
			map_result.put("total_pages", total_pages);
		}
		map_result.put("gmv", list);
		return map_result;
	}

	/**
	 * 总部查看：按频道交易额进行排名
	 * @param dynamicDto
	 * @return
	 */
	@Override
	public Map<String, Object> queryTradeByChannelName(DynamicDto dynamicDto,PageInfo pageInfo) {
		String province_id = dynamicDto.getProvinceId()==null?"":String.valueOf(dynamicDto.getProvinceId());
		String city_id = dynamicDto.getCityId()==null?"":String.valueOf(dynamicDto.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		Map<String, Object> maps = new HashMap<String, Object>();
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND ts.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and d.id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and d.id='"+province_id+"' ";
		}
		String sql ="select d.id as city_id,d.cityname as city_name,channel_name,ifnull(sum(order_amount),0) as order_amount from ds_storetrade_channel dsch "+
				"left join t_store ts on (dsch.storeno = ts.storeno) "+
				" left join t_dist_citycode d on d.cityname=ts.city_name "+
				"where ts.storeno is not null and year ="+dynamicDto.getYear()+" and month = "+dynamicDto.getMonth()+" and channel_name is not null "+
				 provinceStr + cityStr +
				"group by channel_name "+ 
				"order by sum(order_amount) desc ";
		String sql_count = "SELECT count(tdd.channel_name) as channel_count from ( "+sql+" ) tdd ";
		Query query_count = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql_count);
		List<?> total = query_count
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(pageInfo!=null){
			if(total!=null&&total.size()>0){
				maps = (Map<String, Object>) total.get(0);
				pageInfo.setTotalRecords(Integer.valueOf(maps.get("channel_count").toString()));
			}else{
				pageInfo.setTotalRecords(Integer.valueOf(0));
			}
		}
		Query query = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);

		List<?> list = null;
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
		Map<String, Object> map_result = new HashMap<String, Object>();
		if(pageInfo!=null){
			Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
			map_result.put("pageinfo", pageInfo);
			map_result.put("total_pages", total_pages);
		}
		map_result.put("gmv", list);
		return map_result;
	}
    
	@Override
	public List<Map<String, Object>> findCityCount(DynamicDto dd) {
		String sql="";
		if(dd.getTarget()==1){//省
			sql="select t1.city_name,t2.id,t2.citycode,t2.cityno from (select id from t_city where province_id = "+dd.getProvinceId()+") t INNER JOIN t_store t1 on t.id = t1.city_id INNER JOIN t_dist_citycode t2 on t1.city_name = t2.cityname group by t1.city_name";
		}else if(dd.getTarget()==0||dd.getTarget()==3){//全国
			sql="select cityname as city_name,id,citycode,cityno from  t_dist_citycode ";
			if(dd.getCityId()!=null&&!"".equals(dd.getCityId())){
				sql=sql+" where id="+dd.getCityId();
			}
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	    //获得查询数据
	    List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	    return lst_data;
	}


	@Override
	public List<Map<String, Object>> findStoreCount(DynamicDto dd) {
		String sql="";
		if(dd.getTarget()==1){//省
			sql="select t.store_id,t.name,t.storeno,t.city_name,t1.id as cityId from t_store t INNER JOIN (select id from t_city where province_id="+dd.getProvinceId()+") t2 on t.city_id = t2.id left join t_dist_citycode t1  on t.city_name  = t1.cityname where  t.name not like '%测试%'";
		}else if(dd.getTarget()==0||dd.getTarget()==3){//全国
			
			String whereStr = "";
			if(dd.getCityId()!=null&&!"".equals(dd.getCityId())){
				whereStr = " and t1.id="+dd.getCityId();
			}
			
			sql ="select t.store_id,t.name,t.storeno,t.city_name,t1.id as cityId from t_store t left join t_dist_citycode t1  on t.city_name  = t1.cityname where  t.name not like '%测试%' "+ whereStr+"";
		}
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	    //获得查询数据
	    List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	    return lst_data;
	}


	@Override
	public List<Map<String, Object>> findStoreKeeperCount(DynamicDto dd) {
		
		String sql="";
		if(dd.getTarget()==0||dd.getTarget()==3){//全国||城市
			String whereStr="";
			if(dd.getCityId()!=null&&!"".equals(dd.getCityId())){
				whereStr = " where tdc.id="+dd.getCityId();
			}
			
			sql = "select tbu.name as keeperName,tbu.employeeId,tbu.mobilephone,tbu.id,t2.name as storeName,t2.city_name from (select t.skid,t.name,t.city_name " +
					" from t_store t  inner join  (select tdc.id,tdc.cityname from" +
					" t_dist_citycode tdc  "+whereStr+") t1	on t.city_name  = t1.cityname ) t2  " +
					" INNER JOIN tb_bizbase_user as tbu on t2.skid = tbu.id";
		}else if(dd.getTarget()==1){//省
			sql="select tbu.name as keeperName,tbu.employeeId,tbu.mobilephone,tbu.id,t.name as storeName,t.city_name from t_store t INNER JOIN (select id from t_city where province_id="+dd.getProvinceId()+") t2 on t.city_id = t2.id left join t_dist_citycode t1  on t.city_name  = t1.cityname left JOIN tb_bizbase_user as tbu on t.skid = tbu.id  where  t.name not like '%测试%'";
		}
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	@Override
	public Map<String, Object> findGaxCount(DynamicDto dd,PageInfo pageInfo) {
		String sql="";
		if(dd.getTarget()==0||dd.getTarget()==3){//城市||全国
			String whereStr=" ";
			if(dd.getCityId()!=null&&!"".equals(dd.getCityId())){
				whereStr=" where tdc.id="+dd.getCityId();
			}
			sql = "SELECT a.name,a.employee_no,b.storeName,a.sex,a.authorizedtype,a.topostdate,b.city_name FROM " +
					"`t_humanresources` a INNER JOIN (select t.store_id,name as storeName,t.city_name from t_store t  " +
					" inner join  (select tdc.id,tdc.cityname from t_dist_citycode tdc "+whereStr+")"+
					" t1 on t.city_name  = t1.cityname and ifnull(t.estate,'')!='闭店中') b " +
					" on a.store_id = b.store_id and  a.humanstatus =1 where a.name not like '%测试%' ";
			
		}else if(dd.getTarget()==1){//省
			sql = "SELECT a.name,a.employee_no,b.storeName,a.sex,a.authorizedtype,a.topostdate,b.city_name FROM " +
					"`t_humanresources` a INNER JOIN (select t.store_id,name as storeName,t.city_name from t_store t  " +
					" inner join  (select id from t_city where province_id="+dd.getProvinceId()+")"+
					" t1 on t.city_id = t1.id and ifnull(t.estate,'')!='闭店中') b " +
					" on a.store_id = b.store_id and  a.humanstatus =1 where a.name not like '%测试%' ";
		}
		
		if(dd.getEmployeeName()!=null&&!"".equals(dd.getEmployeeName())){
			sql = sql+" and a.name like '%"+dd.getEmployeeName()+"%'";
		}
		
		if(dd.getStoreName()!=null&&!"".equals(dd.getStoreName())){
			sql = sql+" and b.storeName like '%"+dd.getStoreName()+"%'";
		}
		
		if(dd.getCityName()!=null&&!"".equals(dd.getCityName())){
			sql = sql+" and b.city_name like '%"+dd.getCityName()+"%'";
		}
		
		Map<String,Object> map_result = new HashMap<String,Object>();
		
		String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
		Object total = query_count.uniqueResult();
		pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<?>  list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

		
		Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
		map_result.put("pageinfo",pageInfo);
		map_result.put("data", list);
		map_result.put("status","success");
		map_result.put("totalPage", total_pages);
		return map_result;
	}

	@Override
	public List<Map<String, Object>> getLastMonthGMVStoreChinaRanking(DynamicDto dd) {
		String  sql = "SELECT a.name,a.employee_no,b.storeName,a.sex,a.authorizedtype,a.topostdate FROM " +
				"`t_humanresources` a INNER JOIN (select t.store_id,name as storeName from t_store t  " +
				"inner join  (select tdc.id,tdc.cityname from t_dist_city a  INNER JOIN t_dist_citycode tdc " +
				"on  a.citycode = tdc.citycode and a.pk_userid="+dd.getEmployeeId()+") t1 on t.city_name  = t1.cityname) b " +
				"on a.store_id = b.store_id and  a.humanstatus =1 where t.storeno is not null ";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public Map<String, Object> selectAreaRankingOfStore(DynamicDto dynamicDto,PageInfo pageInfo) {
	    //String sql = "select SUM(IFNULL(pesgmv,0)) as amount,area_no,area_name as name  from ds_areatrade where storeno='"+dynamicDto.getStoreNo()+"' and year="+dynamicDto.getYear()+" and month="+dynamicDto.getMonth()+" group by area_no ORDER BY amount desc";
		//String sql = "select SUM(IFNULL(pesgmv,0)) as amount,area_no,area_name as name  from ds_areatrade where storeno='"+dynamicDto.getStoreNo()+"' and year=2017 and month=11 group by area_no ORDER BY amount desc";
		
		String  sql="SELECT SUM(IFNULL(a.gmv_price,0)) as amount,a.area_code as area_no,ifnull(b.name,a.area_code) as name "+
						" FROM df_mass_order_monthly a LEFT JOIN t_area  b on a.area_code = b.area_no " +
						" where DATE_FORMAT(a.sign_time,'%Y-%m') ='"+dynamicDto.getBeginDate()+"'" +
						" and a.area_code is not null and a.store_code='"+dynamicDto.getStoreNo()+"'" +
						" GROUP BY a.area_code ORDER BY amount desc";
		String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";
		
		Map<String,Object> map_result = new HashMap<String,Object>();
		List<?> list=null;
		if(pageInfo!=null){
			Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
			Object total = query_count.uniqueResult();
			pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

			Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
					pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

			
			Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
			map_result.put("pageinfo",pageInfo);
			map_result.put("totalPage", total_pages);
			map_result.put("totalRecords", total);
		}else{
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		map_result.put("gmv", list);
		return map_result;
	}


	
	@Override
	public List<Map<String, Object>> selectDeptRankingOfStore(DynamicDto dynamicDto) {
//		String sql="select convert(SUM(d.gmv),decimal(20,2))  as gmv,storeno,career_group,storename from "+
//					" (select a.*,b.store_name,b.department_name,IFNULL(b.gmv,0) as gmv from "+
//					" (select ts.storeno,ts.name as storename,ts.store_id,td.career_group from t_store ts,t_data_human_type td where storeno='"+dynamicDto.getStoreNo()+"') a LEFT JOIN "+ 
//					" (select store_name,store_code,department_name,SUM(IFNULL(gmv_price,0)) as gmv from df_mass_order_monthly where DATE_FORMAT(sign_time,'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m') and store_code='"+dynamicDto.getStoreNo()+"' GROUP BY store_code,department_name) b  on a.storeno = b.store_code and a.career_group like CONCAT('%',b.department_name,'%') "+
//					" ) d  GROUP BY d.storeno,d.career_group";
		String sql="select SUM(d.gmv) as gmv,storeno,career_group,storename from "+
				" (select a.*,b.store_name,b.dep_name,IFNULL(b.gmv,0) as gmv from "+
				" (select ts.storeno,ts.name as storename,ts.store_id,td.career_group from t_store ts,t_data_human_type td where storeno='"+dynamicDto.getStoreNo()+"') a LEFT JOIN "+ 
				" (select store_name,storeno,dep_name,SUM(IFNULL(order_amount,0)) as gmv from ds_storetrade_channel where year= "+dynamicDto.getYear()+" and month="+dynamicDto.getMonth()+" and storeno='"+dynamicDto.getStoreNo()+"' GROUP BY storeno,dep_name) b  on a.storeno = b.storeno and  a.career_group like CONCAT('%',b.dep_name,'%') "+
				" ) d  GROUP BY d.storeno,d.career_group";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public Map<String, Object> selectChannelRankingOfStore(DynamicDto dynamicDto,PageInfo pageInfo) {
		//String sql="select convert(SUM(IFNULL(gmv_price,0)),decimal(20,2)) as amount,channel_name as name from df_mass_order_monthly where DATE_FORMAT(sign_time,'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m') and store_code='"+dynamicDto.getStoreNo()+"'  GROUP BY channel_name order by amount desc";
	    //String sql="select SUM(IFNULL(order_amount,0)) as amount,channel_name as name from ds_storetrade_channel where year = 2017 and month=11 and storeno='"+dynamicDto.getStoreNo()+"'  GROUP BY channel_name order by amount desc";
		String sql="select SUM(IFNULL(order_amount,0)) as amount,channel_name as name from ds_storetrade_channel where year = "+dynamicDto.getYear()+" and month="+dynamicDto.getMonth()+" and storeno='"+dynamicDto.getStoreNo()+"'  GROUP BY channel_name order by amount desc";

		String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";
		Map<String,Object> map_result = new HashMap<String,Object>();
		List<?> list=null;
		if(pageInfo!=null){
			Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
			Object total = query_count.uniqueResult();
			pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

			Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
					pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();
			Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
			map_result.put("pageinfo",pageInfo);
			map_result.put("totalPage", total_pages);
			map_result.put("totalRecords", total);			
		}else{
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			
		}
		map_result.put("gmv", list);
		return map_result;
	}

	//cityId
	@Override
	public Map<String, Object> CityOrderRankingTop10(DynamicDto dd,PageInfo pageInfo) {
		String province_id = dd.getProvinceId()==null?"":String.valueOf(dd.getProvinceId());
		String city_id = dd.getCityId()==null?"":String.valueOf(dd.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		Map<String, Object> maps = new HashMap<String, Object>();
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND t.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and d.id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and d.id='"+province_id+"' ";
		}
		String  sql = "SELECT d.id as city_id,t.city_name as city_name,t.store_id as store_id,ds.store_name as store_name,sum(ds.order_count) AS order_count,t.province_id " +
				"FROM ds_storetrade ds LEFT JOIN t_store t ON ds.storeno=t.storeno left join t_dist_citycode d on d.cityname=t.city_name  WHERE t.storeno is not null and " +
				"ds.`month`='"+dd.getMonth()+"' and ds.`year`='"+dd.getYear()+"'"+provinceStr+cityStr+" GROUP BY ds.store_name ORDER BY " +
				"order_count DESC ";
		String sql_count = "SELECT count(tdd.store_name) as store_order_count from ("+sql+") tdd ";
		Query query_count = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql_count);
		List<?> total = query_count
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(pageInfo!=null){
			if(total!=null&&total.size()>0){
				maps = (Map<String, Object>) total.get(0);
				pageInfo.setTotalRecords(Integer.valueOf(maps.get("store_order_count").toString()));
			}else{
				pageInfo.setTotalRecords(Integer.valueOf(0));
			}
		}
		Query query = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);

		List<?> list = null;
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
		Map<String, Object> map_result = new HashMap<String, Object>();
		if(pageInfo!=null){
			Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
			map_result.put("pageinfo", pageInfo);
			map_result.put("total_pages", total_pages);
		}
		map_result.put("gmv", list);
		return map_result;
	}



	@Override
	public Map<String, Object> selectEmployeeRankingOfStore(DynamicDto dynamicDto,PageInfo pageInfo) {
		//String sql = "select SUM(IFNULL(pesgmv,0)) as amount,employee_a_no,employee_a_name as name  from ds_areatrade where storeno='"+dynamicDto.getStoreNo()+"' and year="+dynamicDto.getYear()+" and month="+dynamicDto.getMonth()+" group by employee_a_no ORDER BY amount desc";
		//String sql = "select SUM(IFNULL(pesgmv,0)) as amount,employee_a_no,employee_a_name as name  from ds_areatrade where storeno='"+dynamicDto.getStoreNo()+"' and year=2017 and month=11 group by employee_a_no ORDER BY amount desc";

		String sql=" select employee_no as employee_a_no,employee_name as name,IFNULL(pesgmv,0) as amount "+
				" from ds_emptrade where year ="+dynamicDto.getYear()+" and month = "+dynamicDto.getMonth()+" and storeno= '"+dynamicDto.getStoreNo()+"'  ORDER BY amount desc";
		String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";
		Map<String,Object> map_result = new HashMap<String,Object>();
		List<?> list=null;
		if(pageInfo!=null){
			Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
			Object total = query_count.uniqueResult();
			pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

			Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
					pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();
			
			Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
			
			map_result.put("pageinfo",pageInfo);
			map_result.put("totalPage", total_pages);
			map_result.put("totalRecords", total);
		}else{
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			
		}
		map_result.put("gmv", list);
		return map_result;
	}



	@Override
	public List<Map<String, Object>> selectDeptServerRanking(DynamicDto dynamicDto) {
//		String sql = "select convert(SUM(d.gmv),decimal(20,2)) as gmv,name,storeno,career_group,storename,employee_no from (select a.*,b.store_name,b.department_name,IFNULL(b.gmv,0) as gmv,ifnull(c.name,'') as name,ifnull(c.employee_no,'') as employee_no from "+
//				" (select ts.storeno,ts.name as storename,ts.store_id,td.career_group from t_store ts,t_data_human_type td where storeno = '"+dynamicDto.getStoreNo()+"') a LEFT JOIN "+
//				" (select store_name,store_code,department_name,SUM(IFNULL(gmv_price,0)) as gmv from df_mass_order_monthly where DATE_FORMAT(sign_time,'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m') GROUP BY store_code,department_name) b "+
//				" on a.storeno = b.store_code and a.career_group like CONCAT('%',b.department_name,'%') LEFT JOIN (select th.name,th.employee_no,th.career_group,ts.storeno  from t_humanresources th INNER JOIN t_store ts on th.store_id = ts.store_id  and   th.zw='服务专员' and th.humanstatus = 1 and ts.storeno='"+dynamicDto.getStoreNo()+"') c on  a.career_group  = c.career_group and c.storeno = a.storeno) d  GROUP BY d.storeno,d.career_group,d.employee_no having d.employee_no is not null and d.employee_no!=''  order by gmv desc ";
		String sql = "select SUM(d.gmv) as gmv,name,storeno,career_group,storename,employee_no from (select a.*,b.store_name,b.dep_name,IFNULL(b.gmv,0) as gmv,ifnull(c.name,'') as name,ifnull(c.employee_no,'') as employee_no from "+
				" (select ts.storeno,ts.name as storename,ts.store_id,td.career_group from t_store ts,t_data_human_type td where storeno = '"+dynamicDto.getStoreNo()+"') a LEFT JOIN "+
				" (select store_name,storeno,dep_name,SUM(IFNULL(order_amount,0)) as gmv from ds_storetrade_channel where year="+dynamicDto.getYear()+"  and month="+dynamicDto.getMonth()+" GROUP BY storeno,dep_name) b "+
				" on a.storeno = b.storeno and  a.career_group like CONCAT('%',b.dep_name,'%') LEFT JOIN (select th.name,th.employee_no,th.career_group,ts.storeno  from t_humanresources th INNER JOIN t_store ts on th.store_id = ts.store_id  and   th.zw='服务专员' and th.humanstatus = 1) c on  a.career_group  = c.career_group and c.storeno = a.storeno) d  GROUP BY d.storeno,d.career_group,d.employee_no having d.employee_no is not null and d.employee_no!=''  order by gmv desc ";
	SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	return lst_data;
	}


	
	@Override
	public Map<String, Object> selectAreaOrderRanking(DynamicDto dynamicDto,PageInfo pageInfo) {
		String sql = "select sum(ifnull(other_order_count,0)) as amount,employee_a_no,employee_a_name as name  from ds_areatrade where storeno='"+dynamicDto.getStoreNo()+"' and year="+dynamicDto.getYear()+" and month="+dynamicDto.getMonth()+" group by employee_a_no ORDER BY amount desc ";
		//String sql = "select sum(ifnull(other_order_count,0)) as amount,employee_a_no,employee_a_name as name  from ds_areatrade where storeno='"+dynamicDto.getStoreNo()+"' and year=2017 and month=11 group by employee_a_no ORDER BY amount desc ";

		String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";
		Map<String,Object> map_result = new HashMap<String,Object>();
		List<?> list=null;
		if(pageInfo!=null){
			Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
			Object total = query_count.uniqueResult();
			pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

			Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
					pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

			
			Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
			
			map_result.put("pageinfo",pageInfo);
			map_result.put("totalRecords", total);
			map_result.put("totalPage", total_pages);
		}else{
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		map_result.put("gmv", list);
		return map_result;
	}


	
	@Override
	public Map<String, Object> selectChannelOrderRanking(DynamicDto dynamicDto,PageInfo pageInfo) {
		//String sql="select count(1)  as amount,channel_name as name from df_mass_order_monthly where DATE_FORMAT(sign_time,'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m') and store_code='"+dynamicDto.getStoreNo()+"'  GROUP BY channel_name order by amount desc ";
		//String sql="select sum(ifnull(order_count,0))  as amount,channel_name as name from ds_storetrade_channel where year = 2017 and month=11 and storeno='"+dynamicDto.getStoreNo()+"'  GROUP BY channel_name order by amount desc ";
		String sql="select sum(ifnull(order_count,0))  as amount,channel_name as name from ds_storetrade_channel where year = "+dynamicDto.getYear()+" and month="+dynamicDto.getMonth()+" and storeno='"+dynamicDto.getStoreNo()+"'  GROUP BY channel_name order by amount desc ";

		String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";
		
		Map<String,Object> map_result = new HashMap<String,Object>();
		List<?> list=null;
		if(pageInfo!=null){
			Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
			Object total = query_count.uniqueResult();
			pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

			Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
					pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

			Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
			map_result.put("pageinfo",pageInfo);
			map_result.put("totalPage", total_pages);
			map_result.put("totalRecords", total);
		}else{
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		
		map_result.put("gmv", list);
		return map_result;
	}


	
	@Override
	public List<Map<String, Object>> selectGMVOfStore(DynamicDto dynamicDto) {
		String sql="select * from ds_storetrade where year = "+dynamicDto.getYear()+" and month="+dynamicDto.getMonth()+" and storeno='"+dynamicDto.getStoreNo()+"'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}



	@Override
	public Map<String, Object> queryOrderCountByChannelName(DynamicDto dynamicDto,PageInfo pageInfo) {
		String province_id = dynamicDto.getProvinceId()==null?"":String.valueOf(dynamicDto.getProvinceId());
		String city_id = dynamicDto.getCityId()==null?"":String.valueOf(dynamicDto.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		Map<String, Object> maps = new HashMap<String, Object>();
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND ts.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and d.id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and d.id='"+province_id+"' ";
		}
		String sql ="select dsch.id as channel_id,channel_name,ifnull(sum(order_count),0) as order_count from ds_storetrade_channel dsch "+
				"left join t_store ts on (dsch.storeno = ts.storeno) left join t_dist_citycode d on d.cityname=ts.city_name  "+
				"where year ="+dynamicDto.getYear()+" and month = "+dynamicDto.getMonth()+" and channel_name is not null "+
				 provinceStr + cityStr +
				"group by channel_name "+ 
				"order by sum(order_count) desc ";
		String sql_count = "SELECT count(tdd.channel_name) as chanel_count from ("+sql+") tdd ";
		Query query_count = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql_count);
		List<?> total = query_count
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(pageInfo!=null){
			if(total!=null&&total.size()>0){
				maps = (Map<String, Object>) total.get(0);
				pageInfo.setTotalRecords(Integer.valueOf(maps.get("chanel_count").toString()));
			}else{
				pageInfo.setTotalRecords(Integer.valueOf(0));
			}
		}
		Query query = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);

		List<?> list = null;
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
		Map<String, Object> map_result = new HashMap<String, Object>();
		if(pageInfo!=null){
			Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
			map_result.put("pageinfo", pageInfo);
			map_result.put("total_pages", total_pages);
		}
		map_result.put("gmv", list);
		return map_result;
	}

	//总部当月营业额
	@Override
	public Map<String, Object> queryTradeSumByMonth(DynamicDto dynamicDto) {
		String province_id = dynamicDto.getProvinceId()==null?"":String.valueOf(dynamicDto.getProvinceId());
		String city_id = dynamicDto.getCityId()==null?"":String.valueOf(dynamicDto.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String lastMonthSql = "";
		int lastMonth;
		int lastYear;
		String zx = "no";
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND ts.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and d.id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and d.id='"+province_id+"' ";
		}
		if(dynamicDto.getMonth()>1){
			lastMonth = dynamicDto.getMonth()-1;
			lastYear = dynamicDto.getYear();
		}else{
			lastMonth = 12;
			lastYear = dynamicDto.getYear()-1;
		}
		String sql ="select ifnull(sum(order_amount),0) as order_amount,ifnull(sum(order_count),0) as order_count from ds_storetrade dst "+
				"left join t_store ts on (dst.storeno = ts.storeno) left join t_dist_citycode d on d.cityname=ts.city_name "+
				" where dst.store_name not like '%测试%' and year ="+dynamicDto.getYear()+" and month ="+dynamicDto.getMonth()+" "+
				  provinceStr + cityStr ;
		lastMonthSql = "select ifnull(sum(order_amount),0) as order_amount,ifnull(sum(order_count),0) as order_count from ds_storetrade dst "+
				"left join t_store ts on (dst.storeno = ts.storeno) left join t_dist_citycode d on d.cityname=ts.city_name "+
				" where dst.store_name not like '%测试%' and year ="+lastYear+" and month ="+lastMonth+" "+
				  provinceStr + cityStr ;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		SQLQuery last_query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(lastMonthSql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		List<Map<String, Object>> lastmont_lst_data = last_query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		Map<String, Object> resuMap = new HashMap<String,Object>();
		resuMap.put("cur_order_amount",Double.valueOf(String.valueOf(lst_data.get(0).get("order_amount"))));
		resuMap.put("month_order_count",Double.valueOf(String.valueOf(lst_data.get(0).get("order_count"))));
		resuMap.put("last_order_count",Double.valueOf(String.valueOf(lastmont_lst_data.get(0).get("order_count"))));
		return resuMap;
	}


	@Override
	public Map<String, Object> queryAreaTradeByEmp(DynamicDto dynamicDto,PageInfo pageInfo) {
		String province_id = dynamicDto.getProvinceId()==null?"":String.valueOf(dynamicDto.getProvinceId());
		String city_id = dynamicDto.getCityId()==null?"":String.valueOf(dynamicDto.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		Map<String, Object> maps = new HashMap<String, Object>();
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND ts.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and d.id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and d.id='"+province_id+"' ";
		}
		String sql ="select dat.city_name as city_name,d.id as city_id,dat.store_name as store_name,dat.employee_name AS employee_a_name,dat.employee_no AS employee_no,pesgmv as pesgmv from ds_emptrade dat "+
				"left join t_store ts on (dat.storeno = ts.storeno) left join t_dist_citycode d on d.cityname=ts.city_name "+
				"where year ="+dynamicDto.getYear()+" and month = "+dynamicDto.getMonth()+" "+
				 provinceStr + cityStr +"order by pesgmv desc ";
		String sql_count = "SELECT count(tdd.employee_a_name) as employee_a_count  from ( "+sql+") tdd ";
		Query query_count = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql_count);
		List<?> total = query_count
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(pageInfo!=null){
			if(total!=null&&total.size()>0){
				maps = (Map<String, Object>) total.get(0);
				pageInfo.setTotalRecords(Integer.valueOf(maps.get("employee_a_count").toString()));
			}else{
				pageInfo.setTotalRecords(Integer.valueOf(0));
			}
		}
		Query query = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);

		List<?> list = null;
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
		Map<String, Object> map_result = new HashMap<String, Object>();
		if(pageInfo!=null){
			Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
			map_result.put("pageinfo", pageInfo);
			map_result.put("total_pages", total_pages);
		}
		map_result.put("gmv", list);
		return map_result;
	}


	@Override
	public List<Map<String, Object>> queryServerTradeByEmp(DynamicDto dynamicDto) {
		String province_id = dynamicDto.getProvinceId()==null?"":String.valueOf(dynamicDto.getProvinceId());
		String city_id = dynamicDto.getCityId()==null?"":String.valueOf(dynamicDto.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" where ts.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" where ts.city_id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" where ts.city_id='"+province_id+"' ";
		}
		String sql = "select name as employee_name,sum(gmv) gmv from (select SUM(d.gmv) as gmv,name,storeno,career_group,storename,employee_no from (select a.*,b.store_name,b.dep_name,IFNULL(b.gmv,0) as gmv,ifnull(c.name,'') as name,ifnull(c.employee_no,'') as employee_no from "+
				" (select ts.storeno,ts.name as storename,ts.store_id,td.career_group from t_store ts,t_data_human_type td "+provinceStr+cityStr+") a LEFT JOIN "+
				" (select store_name,storeno,dep_name,SUM(IFNULL(order_amount,0)) as gmv from ds_storetrade_channel where year="+dynamicDto.getYear()+"  and month="+dynamicDto.getMonth()+" GROUP BY storeno,dep_name) b "+
				" on a.storeno = b.storeno and b.dep_name like CONCAT('%',a.career_group,'%') "+ 
				" LEFT JOIN (select th.name,th.employee_no,th.career_group,ts.storeno  from t_humanresources th INNER JOIN t_store ts on th.store_id = ts.store_id  and   th.zw='服务专员' and th.humanstatus = 1) c on  a.career_group  = c.career_group and c.storeno = a.storeno) d  "+ 
				" GROUP BY d.storeno,d.career_group,d.employee_no) tmpline " +
				" where tmpline.employee_no!='' group by employee_no order by sum(gmv) desc limit 10";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

//cityId
	@Override
	public Map<String, Object> queryTradeSumOfHistory(DynamicDto dynamicDto) {
		String province_id = dynamicDto.getProvinceId()==null?"":String.valueOf(dynamicDto.getProvinceId());
		String city_id = dynamicDto.getCityId()==null?"":String.valueOf(dynamicDto.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND ts.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and d.id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and d.id='"+province_id+"' ";
		}
		String sql ="select ifnull(sum(order_amount),0) as order_amount,ifnull(sum(order_count),0) as history_order_count from ds_storetrade_history dst "+
				" left join t_store ts on (dst.storeno = ts.storeno) left join t_dist_citycode d on d.cityname=ts.city_name "+
				" where dst.storename not like '%测试%' "+ provinceStr + cityStr ;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		Map<String, Object> resuMap = new HashMap<String,Object>();
		if(lst_data!=null&&lst_data.size()>0){
			Map<String, Object> map_lst = (Map<String, Object>)lst_data.get(0);
			resuMap.put("history_order_amount",map_lst.get("order_amount"));
			resuMap.put("history_order_count",map_lst.get("history_order_count"));
		}
		return resuMap;
	}


	
	@Override
	public List<Map<String, Object>> selectHistoryGMVOfStore(DynamicDto dynamicDto) {
		String sql="select * from ds_storetrade_history where  storeno='"+dynamicDto.getStoreNo()+"'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	@Override
	public Map<String, Object> queryProductCityOrder(DynamicDto dynamicDto,PageInfo pageInfo) {
		String province_name = dynamicDto.getProvinceName()==null?"":String.valueOf(dynamicDto.getProvinceName());
		String city_name = dynamicDto.getCityName()==null?"":String.valueOf(dynamicDto.getCityName());
		String provinceStr = "";
		String cityStr = "";
		Map<String, Object> maps = new HashMap<String, Object>();
		if(city_name!=null&&city_name!=""){
			cityStr+=" and tpc.city_name like '%"+city_name+"%' ";
		}else if(province_name!=null&&province_name!=""){
			provinceStr+=" and tpc.province_name like '%"+province_name+"%' ";
		}
		String sql = " select product_name,SUM(product_count) as product_count,city_name from ds_product_city tpc where 1=1 "+
					   cityStr+provinceStr +
					 " GROUP BY city_name,product_name order by product_count desc "  ;
		String sql_count = "SELECT count(tdd.product_name) as product_count from ( "+sql+") tdd ";
		Query query_count = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql_count);
		List<?> total = query_count
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(pageInfo!=null){
			if(total!=null&&total.size()>0){
				maps = (Map<String, Object>) total.get(0);
				pageInfo.setTotalRecords(Integer.valueOf(maps.get("product_count").toString()));
			}else{
				pageInfo.setTotalRecords(Integer.valueOf(0));
			}
		}
		Query query = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);

		List<?> list = null;
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
		Map<String, Object> map_result = new HashMap<String, Object>();
		if(pageInfo!=null){
			Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
			map_result.put("pageinfo", pageInfo);
			map_result.put("total_pages", total_pages);
		}
		map_result.put("gmv", list);
		return map_result;
	}


	
	@Override
	public Map<String, Object> employeeOfAreaGmv(DynamicDto dynamicDto, PageInfo pageInfo) {
		String sql=" select city_name,storeno,store_name,employee_no,employee_name,pesgmv,pes_sendgmv,pes_areagmv,pes_assigngmv,pes_pergmv"+
					" from ds_emptrade where year ="+dynamicDto.getYear()+" and month = "+dynamicDto.getMonth()+" and storeno in ("+dynamicDto.getStoreNo()+")";
		if(dynamicDto.getEmployeeNo()!=null&&!"".equals(dynamicDto.getEmployeeNo())){
			sql=sql+" and employee_no like '%"+dynamicDto.getEmployeeNo()+"%'";
		}
		
		if(dynamicDto.getEmployeeName()!=null&&!"".equals(dynamicDto.getEmployeeName())){
			sql=sql+" and employee_name like '%"+dynamicDto.getEmployeeName()+"%'";
		}
		Map<String, Object> map_result = new HashMap<String, Object>();
		List<?> list=null;
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		if(pageInfo!=null){
			String sql_count = "SELECT count(1) as total from ("+sql+") c";
			Query query_count = this.getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createSQLQuery(sql_count);
			pageInfo.setTotalRecords(Integer.valueOf(query_count.uniqueResult().toString()));
			list =query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(pageInfo.getRecordsPerPage()*(pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();
			
			Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
			
			map_result.put("pageinfo", pageInfo);
			map_result.put("total_pages", total_pages);
		}else{
			list =query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		map_result.put("gmv", list);
		return map_result;
	}


	
	@Override
	public Map<String, Object> storeGmv(DynamicDto dynamicDto, PageInfo pageInfo) {
		List<?> list=null;
		Map<String, Object> map_result = new HashMap<String, Object>();
		
		String sql=" select city_name,storeno,store_name, pesgmv,order_amount, other_order_amount,returned_amount, other_returned_amount, order_count,other_order_count, returned_count, other_returned_count "+
				   " from ds_storetrade where year="+dynamicDto.getYear()+" and month="+dynamicDto.getMonth()+" and storeno in ("+dynamicDto.getStoreNo()+")";
		Query query = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);
		
		if(pageInfo!=null){
			String sql_count = "SELECT count(1) from ("+sql+") c ";
			Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
			
			pageInfo.setTotalRecords(Integer.valueOf(query_count.uniqueResult().toString()));

			list =query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(
						pageInfo.getRecordsPerPage()
								* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
			
			
			Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
			map_result.put("pageinfo", pageInfo);
			map_result.put("total_pages", total_pages);
		}else{
			list =query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		map_result.put("gmv", list);
		return map_result;
	}


	
	@Override
	public Map<String, Object> storeNewaddcus(DynamicDto dynamicDto, PageInfo pageInfo) {
		String sql="select  tor.store_code,tor.store_name,tor.store_city_name,sum(case when customer_isnew_flag >='10' and customer_isnew_flag !='-1' then 1 else 0 end) as new_10_count ,count(DISTINCT(tor.customer_id)) as total"+
				   " from df_mass_order_monthly tor where  tor.customer_id not like 'fakecustomer%' "+
				   " and DATE_FORMAT(tor.sign_time,'%Y-%m-%d') >='"+dynamicDto.getBeginDate()+"' and DATE_FORMAT(tor.sign_time,'%Y-%m-%d') <='"+dynamicDto.getEndDate()+"' "+
				   " and tor.store_code in ("+dynamicDto.getStoreNo()+")"+
				   " and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA' "+
				   " and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0 group by store_id ";
		List<?> list=null;
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		Map<String, Object> map_result = new HashMap<String, Object>();
		if(pageInfo!=null){
			
		
			String sql_count = "SELECT count(1) as total from ("+sql+") c ";
			Query query_count = this.getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createSQLQuery(sql_count);
			pageInfo.setTotalRecords(Integer.valueOf(query_count.uniqueResult().toString()));
			list =query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(
						pageInfo.getRecordsPerPage()
								* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
			Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
			map_result.put("pageinfo", pageInfo);
			map_result.put("total_pages", total_pages);
		}else{
			list =query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		
		map_result.put("gmv", list);
		return map_result;
	}


	
	@Override
	public Map<String, Object> employeeOfAreaNewaddcus(DynamicDto dynamicDto, PageInfo pageInfo) {
		String sql="";
		if("pre".equals(dynamicDto.getSearchstr())){//上个月
			sql="select ifnull(a.new_10_count,0) as new_10_count,ifnull(a.total,0) as total,ts.city_name as store_city_name,ts.name as store_name,ts.storeno as store_code, th.name,th.employee_no as info_employee_a_no  from (select info_employee_a_no as employee_a_no,ifnull(sum(case when customer_isnew_flag >='10' and customer_isnew_flag !='-1' then 1 else 0 end),0) as new_10_count,count(DISTINCT(customer_id)) as total"+
					" from df_mass_order_monthly tor "+
					" where tor.customer_id not like 'fakecustomer%'"+
					" and DATE_FORMAT(tor.sign_time,'%Y-%m-%d') >='"+dynamicDto.getBeginDate()+"' and DATE_FORMAT(tor.sign_time,'%Y-%m-%d') <='"+dynamicDto.getEndDate()+"'"+
					" and tor.store_code in ("+dynamicDto.getStoreNo()+")"+
					" and info_employee_a_no is not null and info_employee_a_no !=''"+
					" and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA' "+
					" and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0"+
					" group by info_employee_a_no) a right join (select username as name,employeeno as employee_no,storeid as store_id from ds_topdata where   year ="+dynamicDto.getYear()+" and month ="+dynamicDto.getMonth()+" and zw ='国安侠' and humanstatus =1 and storeid IN ("+dynamicDto.getStoreIds()+")) th   on a.employee_a_no = th.employee_no   left join t_store ts on th.store_id = ts.store_id ";
		}else if("cur".equals(dynamicDto.getSearchstr())){//当前月
			 sql="select ifnull(a.new_10_count,0) as new_10_count,ifnull(a.total,0) as total,ts.city_name as store_city_name,ts.name as store_name,ts.storeno as store_code, th.name,th.employee_no as info_employee_a_no  from (select info_employee_a_no as employee_a_no,ifnull(sum(case when customer_isnew_flag >='10' and customer_isnew_flag !='-1' then 1 else 0 end),0) as new_10_count,count(DISTINCT(customer_id)) as total"+
					" from df_mass_order_monthly tor "+
					" where tor.customer_id not like 'fakecustomer%'"+
					" and DATE_FORMAT(tor.sign_time,'%Y-%m-%d') >='"+dynamicDto.getBeginDate()+"' and DATE_FORMAT(tor.sign_time,'%Y-%m-%d') <='"+dynamicDto.getEndDate()+"'"+
					" and tor.store_code in ("+dynamicDto.getStoreNo()+")"+
					" and info_employee_a_no is not null and info_employee_a_no !=''"+
					" and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA' "+
					" and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0"+
					" group by info_employee_a_no) a right join (select name,employee_no,store_id from t_humanresources where humanstatus = 1 and  zw = '国安侠' and store_id IN ("+dynamicDto.getStoreIds()+")) th   on a.employee_a_no = th.employee_no   left join t_store ts on th.store_id = ts.store_id ";
		}
		
		sql=sql+" where 1=1 ";
		if(dynamicDto.getEmployeeNo()!=null&&!"".equals(dynamicDto.getEmployeeNo())){
			sql=sql+" and th.employee_no like '%"+dynamicDto.getEmployeeNo()+"%'";
		}
		
		if(dynamicDto.getEmployeeName()!=null&&!"".equals(dynamicDto.getEmployeeName())){
			sql=sql+" and th.name like '%"+dynamicDto.getEmployeeName()+"%'";
		}
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		Map<String, Object> map_result = new HashMap<String, Object>();
		List<?> list= null;
		if(pageInfo!=null){
			String sql_count = "SELECT count(1) as total from ("+sql+") c ";
			Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
			pageInfo.setTotalRecords(Integer.valueOf(query_count.uniqueResult().toString()));
			list =query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(pageInfo.getRecordsPerPage()* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
			Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
			map_result.put("pageinfo", pageInfo);
			map_result.put("total_pages", total_pages);
		}else{
			list =query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		
		map_result.put("gmv", list);
		return map_result;
	}

	
	@Override
	public Map<String, Object> getGaxOfAreaNewaddcus(DynamicDto dynamicDto, PageInfo pageInfo) {
		String sql="select ifnull(a.new_10_count,0) as amount, th.name,th.employee_no as info_employee_a_no  from (select info_employee_a_no as employee_a_no,ifnull(sum(case when customer_isnew_flag >='10' then 1 else 0 end),0) as new_10_count"+
				" from df_mass_order_monthly tor "+
				" where customer_isnew_flag !='-1'"+
				" and DATE_FORMAT(tor.sign_time,'%Y-%m-%d') >='"+dynamicDto.getBeginDate()+"' and DATE_FORMAT(tor.sign_time,'%Y-%m-%d') <= '"+dynamicDto.getEndDate()+"'"+
				" and tor.store_code = '"+dynamicDto.getStoreNo()+"'"+
				" and info_employee_a_no is not null and info_employee_a_no !=''"+
				" and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA' "+
				" and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0"+
				" group by info_employee_a_no) a right join (select name,employee_no,store_id from t_humanresources where humanstatus = 1 and  zw = '国安侠' and store_id IN ("+dynamicDto.getStoreId()+")) th   on a.employee_a_no = th.employee_no order by amount desc";
	
	Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	Map<String, Object> map_result = new HashMap<String, Object>();
	List<?> list= null;
	if(pageInfo!=null){
		String sql_count = "SELECT count(1) as total from ("+sql+") c ";
		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
		pageInfo.setTotalRecords(Integer.valueOf(query_count.uniqueResult().toString()));
		list =query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
			.setFirstResult(pageInfo.getRecordsPerPage()* (pageInfo.getCurrentPage() - 1))
			.setMaxResults(pageInfo.getRecordsPerPage()).list();
		Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("totalPage", total_pages);
		map_result.put("totalRecords", pageInfo.getTotalRecords());	
	}else{
		list =query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
		map_result.put("gmv", list);
		return map_result;
	}

	@Override
	public List<Map<String, Object>> queryPlatformidByStoreId(String storeId) {
		String sql = "select store_id from t_store where platformid= '"+storeId+"' " ;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			Query query = this.getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createSQLQuery(sql);
			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public List<Map<String, Object>> queryAllCityCode() {
		String sql="select cityno from t_dist_citycode";
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	    //获得查询数据
	    List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	    return lst_data;
	}

	@Override
	public List<Map<String, Object>> queryCityAllDept() {
		String sql="select career_group as dep_name,0 AS order_amount FROM t_data_human_type ";
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	    //获得查询数据
	    List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	    return lst_data;
	}
	@Override
	public List<Map<String, Object>> queryMonthCustomerCount(DynamicDto dd) {
		String province_id = dd.getProvinceId()==null?"":String.valueOf(dd.getProvinceId());
		String city_id = dd.getCityId()==null?"":String.valueOf(dd.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String lastMonthSqlStr = "";
		String zx = "no";
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND ds_cus.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and ds_cus.city_id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and ds_cus.city_id='"+province_id+"' ";
		}
		String monthStr = "";
		String monthArr[] = dd.getBeginDate().split("-");
		String lastMonthStr = "";
		int month = Integer.parseInt(monthArr[1]);
		int lastYear = Integer.parseInt(monthArr[0]);
		int lastMonth = 0;
		if(month==1){
			lastMonth = 12;
			lastYear = lastYear-1;
		}else{
			lastMonth = month-1;
		}
		monthStr = monthArr[0]+(month<10?("0"+month):month);
		lastMonthStr=lastYear+""+(lastMonth<10?("0"+lastMonth):lastMonth);
		String sql = "SELECT SUM(ds_cus.pay_count) AS  customer_count FROM  ds_cusum_month_city ds_cus " +
				"LEFT JOIN t_dist_citycode d ON d.id = ds_cus.city_id WHERE 1 = 1 AND " +
				"ds_cus.order_ym = '"+monthStr+"'"+ cityStr+provinceStr;
		
		lastMonthSqlStr = "SELECT SUM(ds_cus.pay_count) AS  last_customer_count FROM  ds_cusum_month_city ds_cus " +
				"LEFT JOIN t_dist_citycode d ON d.id = ds_cus.city_id WHERE 1 = 1 AND " +
				"ds_cus.order_ym = '"+lastMonthStr+"'"+ cityStr+provinceStr;
		List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
		try{
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			SQLQuery lastQuery = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(lastMonthSqlStr);
			List<?> lst_data = query
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			List<?> last_lst_data = lastQuery
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			if(lst_data != null){
				for(Object obj : lst_data){
					Map<String,Object> map_data = (Map<String,Object>)obj;
					Map<String,Object> map_content = (Map<String,Object>)obj;
					if(map_data.get("customer_count")==null){
						map_content.put("customer_count",0);
					}else{
						map_content.put("customer_count",map_data.get("customer_count"));
					}
					 if(last_lst_data != null){
						 Map<String,Object> map_data2 = (Map<String,Object>)last_lst_data.get(0);
						 if(map_data2.get("last_customer_count")==null){
							 map_content.put("last_customer_count",0);
						 }else{
							 map_content.put("last_customer_count",map_data2.get("last_customer_count"));
						 }
			           }
					lst_result.add(map_content);
				}
			}
		}catch (Exception e){
            e.printStackTrace();
        }
		return lst_result;
	}
	@Override
	public List<Map<String, Object>> queryMonthCustomerCountGroup(DynamicDto dd, List<Map<String, Object>> cityNO,
			List<Map<String, Object>> provinceNO, String key) {
		String sqlStr = "";
		String whereStr = "";
		String cityNo = "";
		String monthStr = "";
		if(cityNO!=null&&cityNO.size()>0){
			cityNo = String.valueOf(cityNO.get(0).get("cityno"));
			if(cityNo!=null&&cityNo.startsWith("00")){
				cityNo = cityNo.substring(1,cityNo.length());
			}
			whereStr+=" AND dcomt.store_city_code='"+cityNo+"' ";
		}
		if(provinceNO!=null&&provinceNO.size()>0){
			whereStr+=" and dcomt.store_province_code='"+provinceNO.get(0).get("gb_code")+"' ";
		}
		if(key!=null&&!"".equals(key)){
			monthStr = " AND dcomt.sign_time LIKE '%"+dd.getYear()+"-"+(dd.getMonth()<10?("0"+dd.getMonth()):dd.getMonth())+"%'";
		}
		sqlStr = "SELECT count(DISTINCT(dcomt.customer_id)) AS month_customer_count,dcomt.store_province_code,dcomt.store_city_code,dcomt.store_city_name " +
					"FROM df_mass_order_monthly dcomt WHERE 1=1  " +
					monthStr+whereStr+" GROUP BY dcomt.store_city_code ORDER BY month_customer_count DESC ";
		List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
		try{
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlStr);
			List<?> lst_data = query
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            if(lst_data != null){
                for(Object obj : lst_data){
                    Map<String,Object> map_data = (Map<String,Object>)obj;
                    Map<String,Object> map_content = (Map<String,Object>)obj;
                    map_content.put("customer_count",map_data.get("customer_count"));
                    if(String.valueOf(map_data.get("store_city_name")).lastIndexOf("市")>0){
                    	map_content.put("store_city_name",String.valueOf(map_data.get("store_city_name")).substring(0,String.valueOf(map_data.get("store_city_name")).length()-1));
                    }else{
                    	map_content.put("store_city_name",map_data.get("store_city_name"));
                    }
                    lst_result.add(map_content);
                }
            }
		}catch (Exception e){
            e.printStackTrace();
        }
		return lst_result;
	}
	@Override
	public List<Map<String, Object>> queryYearSumGMV(DynamicDto dd,String cityId,
			String provinceId, String key) {
		String sqlStr = "";
		String monthStr = "";
		String province_id = dd.getProvinceId()==null?"":String.valueOf(dd.getProvinceId());
		String city_id = dd.getCityId()==null?"":String.valueOf(dd.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND ts.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and d.id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and d.id='"+province_id+"' ";
		}
		if(key!=null&&!"".equals(key)){
			monthStr = " AND dcomt.year="+dd.getYear()+"";
		}
		sqlStr = "SELECT sum(dcomt.pesgmv) as year_sum_gmv " +
					" FROM ds_storetrade dcomt left join t_store ts on dcomt.storeno=ts.storeno left join t_dist_citycode d on d.cityname=ts.city_name " +
					"WHERE 1=1  " +
					monthStr+provinceStr + cityStr;
		List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
		try{
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlStr);
			List<?> lst_data = query
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            if(lst_data != null){
                for(Object obj : lst_data){
                    Map<String,Object> map_data = (Map<String,Object>)obj;
                    Map<String,Object> map_content = (Map<String,Object>)obj;
                    map_content.put("year_sum_gmv",map_data.get("year_sum_gmv"));
                    lst_result.add(map_content);
                }
            }
		}catch (Exception e){
            e.printStackTrace();
        }
		return lst_result;
	}
	public Map<String, Object> queryCustomerCountByTime(DynamicDto dd) {
		String province_id = dd.getProvinceId()==null?"":String.valueOf(dd.getProvinceId());
		String city_id = dd.getCityId()==null?"":String.valueOf(dd.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND ds_cus.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and ds_cus.city_id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and ds_cus.city_id='"+province_id+"' ";
		}
		String sql = "SELECT SUM(ds_cus.pay_count) AS month_customer_count,SUM(ds_cus.new_count) AS new_customer_count," +
				"date_format(ds_cus.sign_date, '%m-%d') AS week_date,d.id FROM ds_cusum_day_city ds_cus left join  t_dist_citycode d on d.id=ds_cus.city_id WHERE 1=1 "+cityStr+provinceStr+
				" and ds_cus.sign_date >='"+dd.getBeginDate()+"' AND ds_cus.sign_date <='"+dd.getEndDate()+"' GROUP BY ds_cus.sign_date ";
		
		List<Map<String, Object>> lst_data = null;
	 	Map<String, Object> map_all = new HashMap<String, Object>();
	     try{
	    	 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	    	 lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	     }catch (Exception e){
	         e.printStackTrace();
	     }
	     Integer total_pages = 0;
	     map_all.put("lst_data", lst_data);
	     map_all.put("totalPage", total_pages);
		return map_all;
	}
	@Override
	public Map<String, Object> queryTurnoverByTime(String cityNo,String beginDate,
			String endDate) {
		String sqlStr = "";
		String cityStr1 = "";
		List<Float> arrSort = new ArrayList<Float>();
		Map<String, Object> maxMap = new HashMap<String, Object>();
		if(cityNo.startsWith("00")){
			cityNo = cityNo.substring(1,cityNo.length());
		}
		cityStr1+=" and store_city_code='"+cityNo+"' ";
		sqlStr = "SELECT IFNULL(COUNT(trading_price), 0) AS checked_order_count,IFNULL(SUM(total_quantity), 0) " +
				"AS total_order_count,IFNULL(SUM(trading_price), 0) AS storecur_all_price,IFNULL(SUM(total_quantity), 0) " +
				"AS total_quantity,DAY (sign_time) AS week_date,DATE(sign_time) FROM df_mass_order_monthly WHERE sign_time >= '"+
				beginDate+"'AND sign_time <= '"+endDate+"'"+cityStr1+" GROUP BY DATE(sign_time)";
		List<Map<String, Object>> lst_data = null;
	 	List<Map<String, Object>> max_data = new ArrayList<Map<String,Object>>();
	 	Map<String, Object> map_all = new HashMap<String, Object>();
	     try{
	    	 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlStr);
	    	 lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	     }catch (Exception e){
	         e.printStackTrace();
	     }
	     Integer total_pages = 0;
	     if(lst_data!=null&&lst_data.size()>0){
	    	 for (Map<String, Object> lst_data_map : lst_data) {
	    		 Float checkedCount = Float.valueOf(String.valueOf(lst_data_map.get("storecur_all_price")));
	    		 arrSort.add(checkedCount);
	    	 }
	    	 maxMap.put("max_all_price", Collections.max(arrSort));
	     }else{
	    	 maxMap.put("max_all_price", 0);
	     }
	     max_data.add(maxMap);
	     map_all.put("lst_data", lst_data);
	     map_all.put("max_price", max_data);
	     map_all.put("totalPage", total_pages);
		return map_all;
	}
	@Override
	public Map<String, Object> queryNewMonthUserCount(DynamicDto dd,List<Map<String, Object>> cityNO,
			List<Map<String, Object>> provinceNO) {
		Map<String, Object> map_all = new HashMap<String, Object>();
		String province_id = dd.getProvinceId()==null?"":String.valueOf(dd.getProvinceId());
		String city_id = dd.getCityId()==null?"":String.valueOf(dd.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND ds_cus.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and ds_cus.city_id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and ds_cus.city_id='"+province_id+"' ";
		}
		String sql = "SELECT SUM(ds_cus.new_count) AS new_cus_count,SUM(ds_cus.pay_count) AS pay_cus_count," +
				"date_format(ds_cus.sign_date, '%m-%d') AS week_date,d.id FROM ds_cusum_day_city ds_cus left join  t_dist_citycode " +
				"d on d.id=ds_cus.city_id WHERE 1=1 "+cityStr+provinceStr+" and ds_cus.sign_date>='"
				+dd.getBeginDate()+"' and ds_cus.sign_date <='"+dd.getEndDate()+"' GROUP BY ds_cus.sign_date ORDER BY week_date ";
		
		List<Map<String, Object>> lst_data = null;
		try{
	    	 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	    	 lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	     }catch (Exception e){
	         e.printStackTrace();
	     }
		map_all.put("lst_data", lst_data);
		return map_all;
	}
	@Override
	public Map<String, Object> getWeekCustomerOrderRate(DynamicDto dd) {
		Map<String, Object> map_all = new HashMap<String, Object>();
		String province_id = dd.getProvinceId()==null?"":String.valueOf(dd.getProvinceId());
		String city_id = dd.getCityId()==null?"":String.valueOf(dd.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND ds_cus.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and ds_cus.city_id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and ds_cus.city_id='"+province_id+"' ";
		}
		String sql = "SELECT SUM(ds_cus.new_count) AS new_cus_count,SUM(ds_cus.pay_count) AS pay_cus_count," +
				"date_format(ds_cus.sign_date, '%m-%d') AS week_date,d.id FROM ds_cusum_day_city ds_cus left join  t_dist_citycode d on d.id=ds_cus.city_id WHERE 1=1 "+cityStr+provinceStr+
				" and ds_cus.sign_date >='"+dd.getBeginDate()+"' AND ds_cus.sign_date <='"+dd.getEndDate()+"' GROUP BY ds_cus.sign_date ";
		List<Map<String, Object>> lst_data = null;
		try{
	    	 SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	    	 lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	     }catch (Exception e){
	         e.printStackTrace();
	     }
		map_all.put("lst_data", lst_data);
		return map_all;
	}
	@Override
	public List<Map<String, Object>> queryHistoryCustomerCount(DynamicDto dd) {
		String province_id = dd.getProvinceId()==null?"":String.valueOf(dd.getProvinceId());
		String city_id = dd.getCityId()==null?"":String.valueOf(dd.getCityId());
		String provinceStr = "";
		String cityStr = "";
		String zx = "no";
		if("1".equals(province_id)||"2".equals(province_id)||"3".equals(province_id)){
			zx = "yes";
		}
		if(province_id!=null&&province_id!=""&&"no".equals(zx)){
			provinceStr+=" AND ds_cus.province_id='"+province_id+"' ";
		}
		if(city_id!=null&&city_id!=""){
			cityStr+=" and ds_cus.city_id='"+city_id+"' ";
		}else if("yes".equals(zx)){
			cityStr+=" and ds_cus.city_id='"+province_id+"' ";
		}
		String monthStr = dd.getBeginDate().substring(0,dd.getBeginDate().lastIndexOf("-")).replace("-", "");
		String sql = "SELECT SUM(ds_cus.pay_count) AS  history_customer_count FROM ds_cusum_month_city ds_cus LEFT JOIN " +
				"t_dist_citycode d ON d.id = ds_cus.city_id WHERE 1 = 1 AND " +
				"ds_cus.order_ym < '"+monthStr+"'"+ cityStr+provinceStr;
		
		List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
		try{
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			List<?> lst_data = query
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			if(lst_data != null){
				for(Object obj : lst_data){
					Map<String,Object> map_data = (Map<String,Object>)obj;
					Map<String,Object> map_content = (Map<String,Object>)obj;
					if(map_data.get("history_customer_count")==null){
						map_content.put("history_customer_count",0);
					}else{
						map_content.put("history_customer_count",map_data.get("history_customer_count"));
					}
					lst_result.add(map_content);
				}
			}
		}catch (Exception e){
            e.printStackTrace();
        }
		return lst_result;
	}
	@Override
	public List<Map<String, Object>> queryMonthZbCustomerCount(DynamicDto dd) {
		List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
		String monthStr = "";
		String monthArr[] = dd.getBeginDate().split("-");
		String lastMonthStr = "";
		int month = Integer.parseInt(monthArr[1]);
		int lastYear = Integer.parseInt(monthArr[0]);
		int lastMonth = 0;
		if(month==1){
			lastMonth = 12;
			lastYear = lastYear-1;
		}else{
			lastMonth = month-1;
		}
		monthStr = monthArr[0]+(month<10?("0"+month):month);
		lastMonthStr=lastYear+""+(lastMonth<10?("0"+lastMonth):lastMonth);
		String sql = "select count(distinct customer_id) as customer_count from df_customer_order_month_trade_new where order_ym ='"+monthStr+"'";
		String lastMonthSqlStr = "select count(distinct customer_id) as last_customer_count from df_customer_order_month_trade_new where order_ym ='"+lastMonthStr+"'";
		try{
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			SQLQuery lastQuery = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(lastMonthSqlStr);
			List<?> lst_data = query
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			List<?> last_lst_data = lastQuery
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	    	 if(lst_data != null){
					for(Object obj : lst_data){
						Map<String,Object> map_data = (Map<String,Object>)obj;
						Map<String,Object> map_content = (Map<String,Object>)obj;
						if(map_data.get("customer_count")==null){
							map_content.put("customer_count",0);
						}else{
							map_content.put("customer_count",map_data.get("customer_count"));
						}
						if(last_lst_data != null){
							 Map<String,Object> map_data2 = (Map<String,Object>)last_lst_data.get(0);
							 if(map_data2.get("last_customer_count")==null){
								 map_content.put("last_customer_count",0);
							 }else{
								 map_content.put("last_customer_count",map_data2.get("last_customer_count"));
							 }
				        }
						lst_result.add(map_content);
					}
				}
	     }catch (Exception e){
	         e.printStackTrace();
	     }
		return lst_result;
	}

	@Override
	public List<Map<String, Object>> queryHistoryZbCustomerCount(DynamicDto dd) {
		List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
		String sql = "select count(1) as customer_count from df_user_profile ";
		try{
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			List<?> lst_data = query
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	    	 if(lst_data != null){
					for(Object obj : lst_data){
						Map<String,Object> map_data = (Map<String,Object>)obj;
						Map<String,Object> map_content = (Map<String,Object>)obj;
						if(map_data.get("customer_count")==null){
							map_content.put("history_customer_count",0);
						}else{
							map_content.put("history_customer_count",map_data.get("customer_count"));
						}
						lst_result.add(map_content);
					}
				}
	     }catch (Exception e){
	         e.printStackTrace();
	     }
		return lst_result;
	}
}
