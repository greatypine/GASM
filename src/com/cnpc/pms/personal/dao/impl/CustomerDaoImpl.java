package com.cnpc.pms.personal.dao.impl;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.CustomerDao;
import com.cnpc.pms.personal.entity.Customer;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDaoImpl extends BaseDAOHibernate implements CustomerDao{

	@Override
	public Map<String, Object> queryAchievements(String employee_no,String start_date,String end_date,PageInfo pageInfo) {
		String sql = "SELECT " +
				"  T.`门店名称`," +
				"  T.员工编码," +
				"  T.`员工姓名`," +
				"  SUM(" +
				"    CASE WHEN " +
				"      (T.面积 is not null AND T.面积 != '') AND" +
				"      (T.朝向 is not null AND T.朝向 != '') AND" +
				"      (T.户型 is not null AND T.户型 != '') AND" +
				"      ((T.户型图 is not null AND T.户型图 != '') OR T.`房屋类型` = 0) AND" +
				"      (T.姓名 is not null AND T.姓名 != '') AND" +
				"      (T.性别 is not null) AND" +
				"      (T.电话 is not null AND T.电话 != '') AND" +
				"      (T.年龄 is not null AND T.年龄 != '') AND" +
				"      (T.一元支付 = 0)" +
				"    THEN 1 ELSE 0 END" +
				"  ) AS 一元个数," +
				"    SUM(" +
				"    CASE WHEN " +
				"      (T.面积 is not null AND T.面积 != '') AND" +
				"      (T.朝向 is not null AND T.朝向 != '') AND" +
				"      (T.户型 is not null AND T.户型 != '') AND" +
				"      ((T.户型图 is not null AND T.户型图 != '') OR T.`房屋类型` = 0) AND" +
				"      (T.姓名 is not null AND T.姓名 != '') AND" +
				"      (T.性别 is not null) AND" +
				"      (T.电话 is not null AND T.电话 != '') AND" +
				"      (T.年龄 is not null AND T.年龄 != '') AND" +
				"      (T.照片 is not null AND T.照片 != '') AND" +
				"      (T.民族 is not null AND T.民族 != '') AND" +
				"      (T.住房性质 is not null AND T.住房性质 != '') AND" +
				"      (T.客户收入 is not null AND T.客户收入 != '') AND" +
				"      (T.加班 is not null AND T.加班 != '') AND" +
				"      (T.家庭人口数 is not null AND T.家庭人口数 != '') AND" +
				"      (T.学龄前人数 is not null AND T.学龄前人数 != '') AND" +
				"      (T.未成年人数 is not null AND T.未成年人数 != '') AND" +
				"      (T.宠物类型 is not null AND T.宠物类型 != '') AND" +
				"      (T.一元支付 = 0) AND" +
				"      (tf.customer_id is not null) " +
				"    THEN 1 ELSE 0 END" +
				"  ) AS 六元个数" +
				" FROM " +
				"(" +
				"  SELECT" +
				"    thc.id AS 客户户型id," +
				"    tc.id AS 客户id," +
				"    ttv.`name` AS 小区名," +
				"    ttv.address AS 小区地址," +
				"    th.house_type AS 房屋类型," +
				"    tb.`name` AS 楼号," +
				"    th.building_unit_no AS 单元号," +
				"    th.commercial_floor_number AS 楼层," +
				"    th.building_house_no AS 房间号," +
				"    hs.house_area AS 面积," +
				"    hs.house_toward AS 朝向," +
				"    hs.house_style AS 户型," +
				"    hs.house_pic AS 户型图," +
				"    tc.`name` AS 姓名," +
				"    tc.sex AS 性别," +
				"    tc.age AS 年龄," +
				"    tc.mobilephone AS 电话," +
				"    tc.customer_pic AS 照片," +
				"    tc.nationality AS 民族," +
				"    tc.house_property 住房性质," +
				"    tc.job AS 职业," +
				"    tc.income AS 客户收入," +
				"    tc.work_overtime AS 加班," +
				"    tc.family_num AS 家庭人口数," +
				"    tc.preschool_num AS 学龄前人数," +
				"    tc.minor_num AS 未成年人数," +
				"    tc.pet_type AS 宠物类型," +
				"    u.employeeId AS 员工编码," +
				"    u.`name` AS 员工姓名," +
				"    u.store_id AS 门店id," +
				"    s.`name` AS 门店名称," +
				"    thc.one_pay AS 一元支付," +
				"    thc.six_pay AS 六元支付" +
				"  FROM" +
				"    t_customer tc" +
				"  LEFT JOIN t_house_customer thc ON tc.id = thc.customer_id" +
				"  LEFT JOIN t_house th ON th.id = thc.house_id" +
				"  LEFT JOIN t_building tb ON tb.id = th.building_id" +
				"  LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id" +
				"  LEFT JOIN t_house_style hs ON hs.house_id = th.id" +
				"  LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no" +
				"  LEFT JOIN t_store s ON s.store_id = u.store_id" +
				"  WHERE DATE_FORMAT(tc.update_time,'%Y-%m-%d') >= '"+start_date+"' AND DATE_FORMAT(tc.update_time,'%Y-%m-%d') <= '"+end_date+"' AND u.disabledFlag = 1" +
				") T LEFT JOIN (SELECT DISTINCT customer_id FROM t_family) AS tf ON tf.customer_id = T.`客户id`" +
				"WHERE T.员工编码 != '' AND T.员工编码 IS NOT NULL " +
				(employee_no != null && !"".equals(employee_no)?" AND T.员工编码 LIKE '%"+employee_no+"%'":"") +
				"GROUP BY T.员工编码,T.`员工姓名` HAVING 一元个数 > 0 ORDER BY T.门店id,T.员工编码";

		String sql_count = "SELECT COUNT(1) FROM ("+sql+") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
		pageInfo.setTotalRecords(Integer.valueOf(query_count.uniqueResult().toString()));

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<?> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

		Map<String,Object> map_result = new HashMap<String,Object>();
		map_result.put("pageinfo",pageInfo);
		map_result.put("header", "");
		map_result.put("data", list);
		return map_result;
	}

	
	@Override
	public Long getHouseOfArea(String employeeNo,Long area_id) {
		String sqlStr = "";
		if(area_id!=null){
			sqlStr = " AND a.id="+area_id;
		}
		String findSql = "select count(1) FROM t_house bb where bb.tinyvillage_id in"
				+ " ( select tin_village_id from ("
							+" SELECT b.tin_village_id FROM t_area AS a INNER JOIN t_area_info AS b ON  a.id = b.area_id  AND b.tin_village_id IS NOT NULL AND a.status=0 "
							+sqlStr
							+" UNION" 						 
							+" SELECT c.id AS tin_village_id FROM t_area AS a INNER JOIN t_area_info AS b ON  a.id = b.area_id  AND b.tin_village_id IS  NULL AND a.status=0 "
							+sqlStr
							+" INNER JOIN t_tiny_village as c ON b.village_id = c.village_id) aa   )";
		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);
		Long count = Long.parseLong(query_count.uniqueResult().toString());
		return count;
	}


	
	@Override
	public List<Map<String, Object>> getCustomerByPhone(String phone) {
		String sql = "select name,mobilephone from t_customer where mobilephone='"+phone+"'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getCustomerOfYear(Long storeId) {
		String sql = "select count(1) total, DATE_FORMAT(create_time,'%c月') AS create_date from tb_bizbase_user as a INNER JOIN t_customer as b on a.employeeId =b.employee_no AND a.disabledFlag = 1 and a.name not like '%测试%' AND a.store_id="+storeId+"  AND (a.pk_usergroup=3224 or  a.employeeId IN (select employee_no from t_humanresources where zw ='副店长' and remark='国安侠' and store_id="+storeId+")) AND YEAR(b.create_time) = YEAR(CURDATE()) GROUP BY DATE_FORMAT(b.create_time,'%Y-%m') ;";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getCustomerOfArea(Long storeId,String query_date) {
		String sql="SELECT store_area.id AS areaId,store_area.name AS areaName ,SUM(CASE WHEN cus.name is null then 0 ELSE 1 END) AS total FROM "+
				"(SELECT b.tin_village_id,a.name,a.id FROM t_area AS a INNER JOIN t_area_info AS b ON a.id = b.area_id "+
				"AND a.store_id= "+storeId+
				" AND b.tin_village_id IS NOT NULL AND a.status=0 "+
				"UNION "+
				"SELECT c.id AS tin_village_id,a.name,a.id FROM t_area AS a "+
				"INNER JOIN t_area_info AS b ON a.id = b.area_id "+
				"AND a.store_id="+storeId+
				" AND b.tin_village_id IS NULL AND a.status=0 "+
				"INNER JOIN t_tiny_village AS c ON b.village_id = c.village_id) AS store_area "+
				"LEFT JOIN t_house as h ON store_area.tin_village_id = h.tinyvillage_id "+
				"LEFT JOIN (select thc1.* from t_house_customer thc1 inner join (select max(id) as id from t_house_customer group by customer_id) as thc2 on thc1.id = thc2.id)    AS hc ON hc.house_id = h.id "+
				"LEFT JOIN (select * from t_customer where  DATE_FORMAT(create_time,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m'))  AS cus ON cus.id =  hc.customer_id "+
				"GROUP BY areaId,areaName order by areaId";
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getCustomerOfEmployee(Long storeId,String query_date) {
		String sql = "select u.employeeId,sum(case when cus.name is null then 0 else 1 end) as total ,u.name from (select * from tb_bizbase_user as bu where bu.store_id="+storeId+" and bu.disabledFlag = 1 and bu.name not like '%测试%' and (bu.pk_usergroup=3224 or  bu.employeeId IN (select employee_no from t_humanresources where zw ='副店长' and remark='国安侠' and store_id="+storeId+"))) as u "+
				" LEFT JOIN t_customer AS cus on u.employeeId = cus.employee_no  AND DATE_FORMAT(cus.create_time,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m')"+
				" GROUP BY u.employeeId";

	SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	return lst_data;
	}


	
	@Override
	public List<Map<String, Object>> queryCustomerOfEmployee_month(String employeeNo,Long area_id) {
		
		String sqlStr = "";
		if(employeeNo!=null&&!"".equals(employeeNo)){
			sqlStr = " AND LOWER(a.employee_a_no) = LOWER('"+employeeNo+"') ";
		}
		
		if(area_id!=null){
			sqlStr=sqlStr+" AND a.id="+area_id;
		}
		String sql = "select  DATE_FORMAT(cus.create_time,'%Y-%m') as createTime,count(1) as total from t_customer as cus "
				+" INNER  JOIN (select thc1.* from t_house_customer thc1 inner join (select max(id) as id from t_house_customer group by customer_id) as thc2 on thc1.id = thc2.id) AS f ON cus.id = f.customer_id INNER JOIN t_house AS g ON f.house_id=g.id "
				+" inner join ("
				+" SELECT b.tin_village_id FROM t_area AS a INNER JOIN t_area_info AS b ON  a.id = b.area_id AND a.status=0  AND b.tin_village_id IS NOT NULL"
				+sqlStr
				+" UNION "
				+" SELECT c.id AS tin_village_id FROM t_area AS a INNER JOIN t_area_info AS b ON  a.id = b.area_id AND a.status=0  AND b.tin_village_id IS  NULL"
				+sqlStr
				+" INNER JOIN t_tiny_village as c ON b.village_id = c.village_id "
				+" ) AS h on h.tin_village_id = g.tinyvillage_id"
				+" AND (DATE_FORMAT(cus.create_time,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m')"
				+" OR DATE_FORMAT(cus.create_time,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -1 MONTH),'%Y-%m')" 
				+" OR  DATE_FORMAT(cus.create_time,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -2 MONTH),'%Y-%m')" 
				+" OR  DATE_FORMAT(cus.create_time,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -3 MONTH),'%Y-%m')"
				+" OR  DATE_FORMAT(cus.create_time,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -4 MONTH),'%Y-%m'))"
				+ " group by createTime order by createTime";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	@Override
	public void updateCustomerSortById(String idString) {
		deleteCustomerSortById();
		String sb_sql="update t_customer SET sort=1 WHERE id in ("+idString+")";//添加排序
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sb_sql.toString());
		query.executeUpdate();
		
	}


	@Override
	public void deleteCustomerSortById() {
		String sb_sql="update t_customer SET sort=NULL";//清空的sql
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sb_sql.toString());
		query.executeUpdate();
	}


	
	@Override
	public List<Map<String, Object>> getCustomerOfYear_CSZJ_QYJL(String employeeId,Long cityId,String role) {
		String whereStr= "";
		
		
		if("CSZJ".equals(role)){//城市总监
			whereStr="select t.store_id from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employeeId+"' and a.id = b.gmid and b.ctid="+cityId+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	 on t.city_name  = t1.cityname";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select store_id from t_store where rmid = (select id from tb_bizbase_user where employeeId='"+employeeId+"')";
		}
		
		String sql = "SELECT count(1) total,DATE_FORMAT(create_time, '%c月') AS create_date FROM tb_bizbase_user AS a"+
					" INNER JOIN ("+whereStr+") c"+
					" on a.store_id = c.store_id  AND a.disabledFlag = 1 AND a. NAME NOT LIKE '%测试%'"+
					" AND (a.pk_usergroup = 3224 OR a.employeeId IN ( SELECT employee_no FROM t_humanresources t1 INNER JOIN "+
					" ("+whereStr+") t2"+
					" on t1.store_id=t2.store_id and t1.humanstatus=1 AND t1.zw = '副店长' AND t1.remark = '国安侠'))"+
					" INNER JOIN t_customer AS b ON a.employeeId = b.employee_no"+
					" AND YEAR (b.create_time) = YEAR (CURDATE())"+
					" GROUP BY DATE_FORMAT(b.create_time, '%Y-%m');";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getCustomerOfStore_CSZJ_QYJL(String employeeId,Long cityId,String role) {
		String whereStr= "";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select t.store_id,t.name,t.skid,tbu.name as employeeName from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employeeId+"' and a.id = b.gmid and b.ctid="+cityId+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	 on t.city_name  = t1.cityname"+
					" 	INNER JOIN tb_bizbase_user as tbu on t.skid = tbu.id";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select t.store_id,t.name,t.skid,tbu.name as employeeName from t_store t left  join  tb_bizbase_user as tbu on t.skid = tbu.id where rmid = (select id from tb_bizbase_user where employeeId='"+employeeId+"')";
		}
		
		
		String sql = "SELECT ifnull(temp.total,0) as total,ts.store_id,ts.name,ts.skid,ts.employeeName FROM  (SELECT count(1) total,a.store_id FROM tb_bizbase_user AS a"+
				" INNER JOIN ("+whereStr+") c"+
				" on a.store_id = c.store_id  AND a.disabledFlag = 1 AND a. NAME NOT LIKE '%测试%'"+
				" AND (a.pk_usergroup = 3224 OR a.employeeId IN ( SELECT employee_no FROM t_humanresources t1 INNER JOIN "+
				" ("+whereStr+") t2"+
				" on t1.store_id=t2.store_id and t1.humanstatus=1 AND t1.zw = '副店长' AND t1.remark = '国安侠'))"+
				" INNER JOIN (select * from t_customer where DATE_FORMAT(create_time,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m'))  AS b ON a.employeeId = b.employee_no"+
				" GROUP BY a.store_id) temp RIGHT JOIN ("+whereStr+") ts on ts.store_id = temp.store_id ORDER BY total desc limit 20";
	SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	return lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getCustomerOfEmployee_CSZJ_QYJL(String employeeId,Long cityId,String role) {
		
		String whereStr= "";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select t.store_id,t.name from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employeeId+"' and a.id = b.gmid and b.ctid="+cityId+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	 on t.city_name  = t1.cityname";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select store_id,name from t_store where rmid = (select id from tb_bizbase_user where employeeId='"+employeeId+"')";
		}
		String sql = "select sum(case when d.customer_id is null then 0 else 1 end) as total ,d.employeeId,d.name from "+
				 " (select u.employeeId,u.name,tc.id as customer_id  from ("+
				 " select bu.* from tb_bizbase_user as bu "+
				 " inner join ("+whereStr+") ts"+
				 " on bu.store_id = ts.store_id "+
				 " and bu.disabledFlag = 1 and bu.name not like '%测试%' and (bu.pk_usergroup=3224 or  bu.employeeId IN ( SELECT employee_no FROM t_humanresources t1 INNER JOIN "+
				 " ("+whereStr+") t2"+
				 " ON t1.store_id=t2.store_id and t1.humanstatus=1 AND t1.zw = '副店长' AND t1.remark = '国安侠'))) as u "+
				 " LEFT JOIN t_customer AS tc on u.employeeId = tc.employee_no  AND DATE_FORMAT(tc.create_time,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m')"+
				 " ) d GROUP BY d.employeeId ";

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}



	@Override
	public List<Map<String, Object>> getCustomerOfPrevMonthOfStore(String month) {
		String sql = "SELECT count(1) amount,a.store_id,c.name as store_name,'"+month+"' as bind_date FROM tb_bizbase_user AS a"+
				" INNER JOIN t_store  c"+
				" on a.store_id = c.store_id  AND a.disabledFlag = 1 AND a. NAME NOT LIKE '%测试%'"+
				" AND (a.pk_usergroup = 3224 OR a.employeeId IN ( SELECT employee_no FROM t_humanresources t1 INNER JOIN "+
				" t_store t2"+
				" on t1.store_id=t2.store_id and t1.humanstatus=1 AND t1.zw = '副店长' AND t1.remark = '国安侠'))"+
				" INNER JOIN (select * from t_customer where DATE_FORMAT(create_time,'%Y-%m')='"+month+"')  AS b ON a.employeeId = b.employee_no"+
				" GROUP BY a.store_id";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getCustomerOfYear_CSZJ_QYJL_before(String employeeId, Long cityId, String role) {
		String whereStr="";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select t.store_id from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employeeId+"' and a.id = b.gmid and b.ctid="+cityId+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	 on t.city_name  = t1.cityname";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select store_id from t_store where rmid = (select id from tb_bizbase_user where employeeId='"+employeeId+"')";
		}
		
		String sql = "select  sum(ifnull(amount,0)) as  total,DATE_FORMAT(STR_TO_DATE(bind_date,'%Y-%m'), '%c月') AS create_date   from ("+whereStr+") as a left join  t_before_date_customer b on a.store_id = b.store_id  group by bind_date ";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public List<Map<String, Object>> getCustomerOfStore_CSZJ_QYJL_before(String employeeId, Long cityId, String role,
			String q_date) {
		String whereStr="";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select t.store_id,t.name,t.skid from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employeeId+"' and a.id = b.gmid and b.ctid="+cityId+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	 on t.city_name  = t1.cityname";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select store_id,name,skid from t_store where rmid = (select id from tb_bizbase_user where employeeId='"+employeeId+"')";
		}
		
		String sql = "select  ifnull(b.amount,0) as  total, a.store_id ,a.skid,a.name,tbu.name as employeeName  from ("+whereStr+") as a left join  t_before_date_customer b on a.store_id = b.store_id   and b.bind_date='"+q_date+"' left join tb_bizbase_user as tbu on a.skid = tbu.id order by total desc limit 20";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public Integer getAllCustomerOfStore(String storeId) {
		String sql = "select count(1)  as total  from (select * from tb_bizbase_user as bu where bu.store_id in "+storeId+" and bu.disabledFlag = 1 and bu.name not like '%测试%' and (bu.pk_usergroup=3224 or  bu.employeeId IN (select employee_no from t_humanresources where zw ='副店长' and remark='国安侠' and store_id in "+storeId+"))) as u "+
				" INNER JOIN t_customer AS cus on u.employeeId = cus.employee_no  ";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		Integer result = query.uniqueResult()==null?0:Integer.parseInt(query.uniqueResult().toString());
		return result;
	}


	
	@Override
	public Map<String, Object> queryStoreKeeper(Long  store_id) {
		String  sql="select id,employeeId,name from tb_bizbase_user where id in (select  skid  from t_store where store_id="+store_id+")";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(lst_data!=null&&lst_data.size()>0){
			return lst_data.get(0);
		}
		return null;
	}


	
	@Override
	public List<Map<String, Object>> selectUniqueHouseCustomer(Long houseId) {
		String sql = "select  b.id,b.house_id,b.customer_id from (select max(id) as id from t_house_customer  group by customer_id) a inner join t_house_customer b on a.id = b.id and  b.house_id = "+houseId+"";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public List<Map<String, Object>> selectCustomerByTinyVillage(String idsStr) {
		String  sql = " select t3.name,t3.mobilephone,t3.id,t3.address  from (select id from t_house where tinyvillage_id in  ("+idsStr+") and status=0) t1 INNER JOIN (select house_id,customer_id from t_house_customer a INNER JOIN (select max(id) as id from t_house_customer GROUP BY customer_id) b on a.id = b.id ) t2 on t1.id = t2.house_id"
					  +" INNER JOIN t_customer t3 on t2.customer_id = t3.id";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	
	@Override
	public void updateCustomerAddress(Customer customer) {
		String  sql="update t_customer set address='"+customer.getAddress()+"'  where id="+customer.getId();
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	} 
	
}
