package com.cnpc.pms.personal.dao.impl;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.personal.dao.HouseCustomerDao;
import com.cnpc.pms.personal.entity.HouseCustomer;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import java.util.List;
import java.util.Map;

/**
 * 房屋数据层实现类
 * Created by liuxiao on 2016/5/24 0024.
 */
public class HouseCustomerDaoImpl extends BaseDAOHibernate implements HouseCustomerDao {

    @Override
    public void updateSixPayStatus(String work_month)  throws Exception{
        String sql = "UPDATE t_house_customer SET t_house_customer.six_pay = 1,t_house_customer.six_date = STR_TO_DATE('"+work_month+"','%Y-%m-%d') " +
                " WHERE t_house_customer.id in  " +
                " ( " +
                "  SELECT " +
                "   T.id " +
                "  FROM " +
                "  ( " +
                "   SELECT " +
                "    thc.id, " +
                "    tc.update_time, " +
                "    thc.one_date, " +
                "    hs.house_pic, " +
                "    tc.employee_no " +
                "   FROM " +
                "    t_customer tc " +
                "   LEFT JOIN (select thc2.* from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id) thc ON tc.id = thc.customer_id " +
                "   LEFT JOIN t_house th ON th.id = thc.house_id " +
                "   LEFT JOIN t_building tb ON tb.id = th.building_id " +
                "   LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id " +
                "   LEFT JOIN t_house_style hs ON hs.house_id = th.id " +
                "   LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no " +
                "   LEFT JOIN t_store s ON s.store_id = u.store_id " +
                "   WHERE  " +
                "     (hs.house_area is not null AND hs.house_area != '') AND " +
                "     (hs.house_toward is not null AND hs.house_toward != '') AND " +
                "     (hs.house_style is not null AND hs.house_style != '') AND " +
                "     ((hs.house_pic is not null AND hs.house_pic != '') OR th.`house_type` = 0) AND " +
                "     (tc.`name` is not null AND tc.`name` != '') AND " +
                "     (tc.sex is not null) AND " +
                "     (tc.mobilephone is not null AND tc.mobilephone != '') AND " +
                "     (tc.age is not null ) AND " +
                "     (tc.customer_pic is not null AND tc.customer_pic != '') AND " +
                "     (tc.nationality is not null AND tc.nationality != '') AND " +
                "     (tc.house_property is not null AND tc.house_property != '') AND " +
                "     (tc.income is not null AND tc.income != '') AND " +
                "     (tc.work_overtime is not null AND tc.work_overtime != '') AND " +
                "     (tc.family_num is not null ) AND " +
                "     (tc.preschool_num is not null ) AND " +
                "     (tc.minor_num is not null ) AND " +
                "     (tc.pet_type is not null AND tc.pet_type != '')  " +
//                "      AND EXISTS(SELECT 1 FROM t_family tf WHERE tf.customer_id = tc.id) AND " +
                "    AND thc.six_date IS NULL AND tc.update_time < DATE_ADD(STR_TO_DATE('"+work_month+"','%Y-%m-%d'),INTERVAL 1 DAY) " +
                "  ) T " +
                " )";
        Query query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
        query.executeUpdate();
    }

    @Override
    public void updateOnePayStatus(String work_month) throws Exception {
        String sql = "UPDATE t_house_customer SET t_house_customer.one_pay = 1,t_house_customer.one_date = STR_TO_DATE('"+work_month+"','%Y-%m-%d') " +
                " WHERE t_house_customer.id in  " +
                " ( " +
                "  SELECT " +
                "   T.id " +
                "  FROM " +
                "  ( " +
                "   SELECT " +
                "    thc.id, " +
                "    tc.update_time, " +
                "    thc.one_date, " +
                "    hs.house_pic, " +
                "    tc.employee_no " +
                "   FROM " +
                "    t_customer tc " +
                "   LEFT JOIN (select thc2.* from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id) thc ON tc.id = thc.customer_id " +
                "   LEFT JOIN t_house th ON th.id = thc.house_id " +
                "   LEFT JOIN t_building tb ON tb.id = th.building_id " +
                "   LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id " +
                "   LEFT JOIN t_house_style hs ON hs.house_id = th.id " +
                "   LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no " +
                "   LEFT JOIN t_store s ON s.store_id = u.store_id " +
                "   WHERE  " +
                "     (hs.house_area is not null AND hs.house_area != '') AND " +
                "     (hs.house_toward is not null AND hs.house_toward != '') AND " +
                "     (hs.house_style is not null AND hs.house_style != '') AND " +
                "     ((hs.house_pic is not null AND hs.house_pic != '') OR th.`house_type` = 0) AND " +
                "     (tc.`name` is not null AND tc.`name` != '') AND " +
                "     (tc.sex is not null) AND " +
                "     (tc.mobilephone is not null AND tc.mobilephone != '') AND " +
                "     (tc.age is not null ) AND " +
                "     thc.one_date IS NULL AND tc.update_time < DATE_ADD(STR_TO_DATE('"+work_month+"','%Y-%m-%d'),INTERVAL 1 DAY) " +
                "  ) T " +
                " ) ";
        Query query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
        query.executeUpdate();
    }

    @Override
    public List<Map<String, Object>> getAchievementsList(String work_month) {
        String sql = "SELECT " +
                "  T.`门店名称`, " +
                "  T.员工编码, " +
                "  T.`员工姓名`, " +
                "  SUM( " +
                "    CASE WHEN " +
                "      (DATE_FORMAT(T.`第一等级支付时间`,'%Y-%m') = DATE_FORMAT(STR_TO_DATE('"+work_month+"','%Y-%m-%d'),'%Y-%m')) " +
                "    THEN 1 ELSE 0 END " +
                "  ) AS 6字段个数, " +
                "    SUM( " +
                "    CASE WHEN " +
                "       (DATE_FORMAT(T.`第二等级支付时间`,'%Y-%m') = DATE_FORMAT(STR_TO_DATE('"+work_month+"','%Y-%m-%d'),'%Y-%m'))  " +
                "    THEN 1 ELSE 0 END " +
                "  ) AS 18字段个数, " +
                "    SUM( " +
                "    CASE WHEN " +
                "       (DATE_FORMAT(T.`第三等级支付时间`,'%Y-%m') = DATE_FORMAT(STR_TO_DATE('"+work_month+"','%Y-%m-%d'),'%Y-%m'))  " +
                "    THEN 1 ELSE 0 END " +
                "  ) AS 35字段个数 " +
                
                " FROM " +
                "( " +
                "  SELECT " +
                "    thc.id AS 客户户型id, " +
                "    u.employeeId AS 员工编码, " +
                "    u.`name` AS 员工姓名, " +
                "    u.store_id AS 门店id, " +
                "    s.`name` AS 门店名称, " +
                "    thc.one_date AS 第一等级支付时间, " +
                "    thc.six_date AS 第二等级支付时间, " +
                "    thc.third_grade_date AS 第三等级支付时间"+
                "  FROM " +
                "    t_customer tc " +
                "  LEFT JOIN (select thc2.id,thc2.house_id,thc2.customer_id,thc2.one_pay,thc2.six_pay,thc2.one_date,thc2.six_date,thc2.third_grade_pay,thc2.third_grade_date from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id) thc ON tc.id = thc.customer_id " +
                "  LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no " +
                "  LEFT JOIN t_store s ON s.store_id = u.store_id " +
                "  WHERE  " +
                "      (DATE_FORMAT(thc.one_date,'%Y-%m') = DATE_FORMAT(STR_TO_DATE('"+work_month+"','%Y-%m-%d'),'%Y-%m')  " +
                "         OR DATE_FORMAT(thc.six_date,'%Y-%m') = DATE_FORMAT(STR_TO_DATE('"+work_month+"','%Y-%m-%d'),'%Y-%m') " +
                "         OR DATE_FORMAT(thc.third_grade_date,'%Y-%m') = DATE_FORMAT(STR_TO_DATE('"+work_month+"','%Y-%m-%d'),'%Y-%m')) " +
                ") T " +
                "WHERE T.员工编码 != '' AND T.员工编码 IS NOT NULL AND T.员工姓名 NOT LIKE '%测试%'" +
                "GROUP BY T.员工编码,T.`员工姓名`  ORDER BY T.门店id,T.员工编码";

        Query query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
        List<?> result = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if(null != result && 0 < result.size()){
            return (List<Map<String,Object>>)result;
        }
        return null;
    }
    
    
    
    
    @Override
    public List<Map<String,Object>> queryUserHouseCount(){
    	String sql ="SELECT"+
    			"  T.`门店id` as store_id,"+
    			"  T.`门店名称` as store_name,"+
    			"  SUM("+
    			"    CASE WHEN"+
    			"      (T.面积 is not null AND T.面积 != '') AND"+
    			"      (T.朝向 is not null AND T.朝向 != '') AND"+
    			"      (T.户型 is not null AND T.户型 != '') AND"+
    			"      ((T.户型图 is not null AND T.户型图 != '') OR T.`房屋类型` = 0) AND"+
    			"      (T.姓名 is not null AND T.姓名 != '') AND"+
    			"      (T.性别 is not null) AND"+
    			"      (T.电话 is not null AND T.电话 != '') AND"+
    			"      (T.年龄 is not null AND T.年龄 != '')"+
    			"    THEN 1 ELSE 0 END"+
    			"  ) AS yiyuangeshu,"+
    			"	SUM("+
    			"    CASE WHEN"+
    			"      (T.面积 is not null AND T.面积 != '') AND"+
    			"      (T.朝向 is not null AND T.朝向 != '') AND"+
    			"      (T.户型 is not null AND T.户型 != '') AND"+
    			"      (T.姓名 is not null AND T.姓名 != '') AND"+
    			"      (T.性别 is not null) AND"+
    			"      (T.电话 is not null AND T.电话 != '') AND"+
    			"      (T.年龄 is not null AND T.年龄 != '')"+
    			"    THEN 1 ELSE 0 END"+
    			"  ) AS liugeziduan,"+
    			"    SUM("+
    			"    CASE WHEN"+
    			"      (T.面积 is not null AND T.面积 != '') AND"+
    			"      (T.朝向 is not null AND T.朝向 != '') AND"+
    			"      (T.户型 is not null AND T.户型 != '') AND"+
    			"      ((T.户型图 is not null AND T.户型图 != '') OR T.`房屋类型` = 0) AND"+
    			"      (T.姓名 is not null AND T.姓名 != '') AND"+
    			"      (T.性别 is not null AND T.性别 != '') AND"+
    			"      (T.电话 is not null AND T.电话 != '') AND"+
    			"      (T.年龄 is not null AND T.年龄 != '') AND"+
    			"      (T.照片 is not null AND T.照片 != '') AND"+
    			"      (T.民族 is not null AND T.民族 != '') AND"+
    			"      (T.住房性质 is not null AND T.住房性质 != '') AND"+
    			"      (T.客户收入 is not null AND T.客户收入 != '') AND"+
    			"      (T.加班 is not null AND T.加班 != '') AND"+
    			"      (T.家庭人口数 is not null AND T.家庭人口数 != '') AND"+
    			"      (T.学龄前人数 is not null AND T.学龄前人数 != '') AND"+
    			"      (T.未成年人数 is not null AND T.未成年人数 != '') AND"+
    			"      (T.宠物类型 is not null AND T.宠物类型 != '') AND "+
    			"			(tf.customer_id IS NOT NULL)"+
    			"    THEN 1 ELSE 0 END"+
    			"  ) AS liuyuangeshu"+
    			" FROM"+
    			"("+
    			"  SELECT"+
    			"    thc.id AS 客户户型id,"+
    			"    tc.id AS 客户id,"+
    			"    ttv.`name` AS 小区名,"+
    			"    ttv.address AS 小区地址,"+
    			"    th.house_type AS 房屋类型,"+
    			"    tb.`name` AS 楼号,"+
    			"    th.building_unit_no AS 单元号,"+
    			"    th.commercial_floor_number AS 楼层,"+
    			"    th.building_house_no AS 房间号,"+
    			"    hs.house_area AS 面积,"+
    			"    hs.house_toward AS 朝向,"+
    			"    hs.house_style AS 户型,"+
    			"    hs.house_pic AS 户型图,"+
    			"    tc.`name` AS 姓名,"+
    			"    tc.sex AS 性别,"+
    			"    tc.age AS 年龄,"+
    			"    tc.mobilephone AS 电话,"+
    			"    tc.customer_pic AS 照片,"+
    			"    tc.nationality AS 民族,"+
    			"    tc.house_property 住房性质,"+
    			"    tc.job AS 职业,"+
    			"    tc.income AS 客户收入,"+
    			"    tc.work_overtime AS 加班,"+
    			"    tc.family_num AS 家庭人口数,"+
    			"    tc.preschool_num AS 学龄前人数,"+
    			"    tc.minor_num AS 未成年人数,"+
    			"    tc.pet_type AS 宠物类型,"+
    			"    u.employeeId AS 员工编码,"+
    			"    u.`name` AS 员工姓名,"+
    			"    u.store_id AS 门店id,"+
    			"    s.`name` AS 门店名称,"+
    			"		thc.one_pay AS 一元支付,"+
    			"		thc.six_pay AS 六元支付"+
    			"  FROM"+
    			"    t_customer tc"+
    			"  LEFT JOIN (select thc2.id,thc2.house_id,thc2.customer_id,thc2.one_pay,thc2.six_pay,thc2.one_date,thc2.six_date,thc2.third_grade_pay,thc2.third_grade_date from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id) thc ON tc.id = thc.customer_id"+
    			"  LEFT JOIN t_house th ON th.id = thc.house_id"+
    			"  LEFT JOIN t_building tb ON tb.id = th.building_id"+
    			"  LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id"+
    			"  LEFT JOIN t_house_style hs ON hs.house_id = th.id"+
    			"  LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no"+
    			"  LEFT JOIN t_store s ON s.store_id = u.store_id) T "+
    			"LEFT JOIN (SELECT DISTINCT customer_id FROM t_family) AS tf ON tf.customer_id = T.`客户id`"+
    			"WHERE T.员工编码 != '' AND T.员工编码 IS NOT NULL "+
    			"GROUP BY T.门店id HAVING yiyuangeshu > 0  AND store_id is not null  ORDER BY T.门店id";
    	
    	
    	 Query query = getHibernateTemplate().getSessionFactory()
                 .getCurrentSession().createSQLQuery(sql);
         List<?> result = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
         if(null != result && 0 < result.size()){
             return (List<Map<String,Object>>)result;
         }
		 return null;
    }

	@Override
	public void updateThirdGradePayStatus(String work_month) throws Exception {
		
		String sql = "UPDATE t_house_customer SET t_house_customer.third_grade_pay = 1,t_house_customer.third_grade_date = STR_TO_DATE('"+work_month+"','%Y-%m-%d') " +
                " WHERE t_house_customer.id in  " +
                " ( " +
                "  SELECT " +
                "   T.id " +
                "  FROM " +
                "  ( " +
                "   SELECT " +
                "    thc.id, " +
                "    tc.update_time, " +
                "    thc.one_date, " +
                "    hs.house_pic, " +
                "    tc.employee_no " +
                "   FROM " +
                "    t_customer tc " +
                "   LEFT JOIN (select thc2.* from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id) thc ON tc.id = thc.customer_id " +
                " 	LEFT JOIN t_customer_data tcd ON tc.id = tcd.customer_id "  +
                "   LEFT JOIN t_house th ON th.id = thc.house_id " +
                "   LEFT JOIN t_building tb ON tb.id = th.building_id " +
                "   LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id " +
                "   LEFT JOIN t_house_style hs ON hs.house_id = th.id " +
                "   LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no " +
                "   LEFT JOIN t_store s ON s.store_id = u.store_id " +
                "   WHERE  " +
                "     (hs.house_area is not null AND hs.house_area != '') AND " +
                "     (hs.house_toward is not null AND hs.house_toward != '') AND " +
                "     (hs.house_style is not null AND hs.house_style != '') AND " +
                "     ((hs.house_pic is not null AND hs.house_pic != '') OR th.`house_type` = 0) AND " +
                "     (tc.`name` is not null AND tc.`name` != '') AND " +
                "     (tc.sex is not null) AND " +
                "     (tc.mobilephone is not null AND tc.mobilephone != '') AND " +
                "     (tc.age is not null AND tc.age != '') AND " +
                "     (tc.customer_pic is not null AND tc.customer_pic != '') AND " +
                "     (tc.nationality is not null AND tc.nationality != '') AND " +
                "     (tc.house_property is not null AND tc.house_property != '') AND " +
                "     (tc.income is not null AND tc.income != '') AND " +
                "     (tc.work_overtime is not null AND tc.work_overtime != '') AND " +
                "     (tc.family_num is not null AND tc.family_num != '') AND " +
                "     (tc.preschool_num is not null AND tc.preschool_num != '') AND " +
                "     (tc.minor_num is not null AND tc.minor_num != '') AND " +
                "     (tc.pet_type is not null AND tc.pet_type != '') AND " +
 				"     (tcd.nationality is not null AND tcd.nationality != '') AND " +
 				"     (tcd.native_place_pro is not null AND tcd.native_place_pro != '') AND " +		
 				"     (tcd.native_place_city is not null AND tcd.native_place_city != '') AND " +
 				"     (tcd.local_city is not null AND tcd.local_city != '') AND " +
 				"     (tcd.local_pro is not null AND tcd.local_pro != '') AND " +
 				"     (tcd.health_condition is not null AND tcd.health_condition != '') AND " +
 				"     (tcd.marital_status is not null AND tcd.marital_status != '') AND " +
 				"     (tcd.degree_of_education is not null AND tcd.degree_of_education != '') AND " +
 				"     (tcd.school_name is not null AND tcd.school_name != '') AND " +
 				"     (tcd.is_car is not null AND tcd.is_car != '') AND " +
 				"     (tcd.automobile_brand is not null AND tcd.automobile_brand != '') AND " +
 				"     (tcd.money_manage is not null AND tcd.money_manage != '') AND " +
 				"     ((tcd.hobby_one is not null AND tcd.hobby_one != '') or (tcd.hobby_two is not null AND tcd.hobby_two != '') or (tcd.hobby_three is not null AND tcd.hobby_three != '')) AND " +
 				"     (tcd.character_data is not null AND tcd.character_data != '') AND " +
 				"     (tcd.household_income is not null AND tcd.household_income != '') AND " +
 				"     (tcd.shopping_channel is not null AND tcd.shopping_channel != '') AND " +
//                "      EXISTS(SELECT 1 FROM t_family tf WHERE tf.customer_id = tc.id) AND " +
                "     thc.third_grade_date IS NULL AND tc.update_time < DATE_ADD(STR_TO_DATE('"+work_month+"','%Y-%m-%d'),INTERVAL 1 DAY) " +
                "  ) T " +
                " )";
        Query query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
        query.executeUpdate();
	}

	
	@Override
	public Map<String, Object> selectHouseByCustomer(Long customerId) {
		String sql ="select * from t_house_customer where customer_id="+customerId +" order by id desc";
		Query query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
        List<?> result = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if(null != result && 0 < result.size()){
            return (Map<String,Object>)result.get(0);
        }
		return null;
	}

	@Override
	public HouseCustomer findHouseCustomerByHouseId(Long houseid) {
		String sql ="SELECT thc2.*	FROM	(	SELECT "+
												"	max( "+
												"		`t_house_customer`.`id` "+
												"	) AS `id` "+
											"	FROM "+
											"		`t_house_customer` "+
											"	GROUP BY "+
												"	`t_house_customer`.`customer_id` "+
									"	) `thc1` "+
									"	JOIN `t_house_customer` `thc2` ON (`thc1`.`id` = `thc2`.`id`) WHERE thc2.house_id="+houseid;
		
		Session session = getHibernateTemplate().getSessionFactory().openSession();
        try{
        	SQLQuery sqlQuery = session.createSQLQuery(sql);
        	List<HouseCustomer> list = sqlQuery.addEntity(HouseCustomer.class).list();
            if(list!=null&&list.size()>0){
            	return list.get(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
	}
    
    
    
}
