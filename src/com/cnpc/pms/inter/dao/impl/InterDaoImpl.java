package com.cnpc.pms.inter.dao.impl;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.DateUtil;
import com.cnpc.pms.inter.dao.InterDao;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口数据库层实现类
 * Created by liuxiao on 2016/8/23 0023.
 */
public class InterDaoImpl extends DAORootHibernate implements InterDao {
    @Override
    public Map<String,List<String>> getCurrentVersion() {
        Map<String,List<String>> map_result = new HashMap<String, List<String>>();
        List<String> lst_result = new ArrayList<String>();
        List<String> lst_filename = new ArrayList<String>();
        List<String> lst_message = new ArrayList<String>();
        List<String> lst_tiptype = new ArrayList<String>();
        List<String> lst_isupdate = new ArrayList<String>();
        String sql = "select version,file_name,message,tiptype,isupdate from t_app_version order by id";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        List lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        for(Object obj : lst_data){
            Map<String,Object> map_row = (Map<String,Object>)obj;
            lst_result.add(map_row.get("version").toString());
            lst_filename.add(map_row.get("file_name") == null?null:map_row.get("file_name").toString());
            lst_message.add(map_row.get("message") == null?null:map_row.get("message").toString());
            lst_tiptype.add(map_row.get("tiptype")== null?null:map_row.get("tiptype").toString());
            lst_isupdate.add(map_row.get("isupdate")== null?null:map_row.get("isupdate").toString());
        }
        map_result.put("version",lst_result);
        map_result.put("filename",lst_filename);
        map_result.put("message",lst_message);
        map_result.put("tiptype",lst_tiptype);
        map_result.put("isupdate",lst_isupdate);
        return map_result;
    }

    @Override
    public Integer getExpressCountByCurrentMonth(String employee_no) {
        String sql = "SELECT IFNULL(COUNT(1),0) FROM t_express WHERE express_date >= DATE_ADD(curdate(),interval -day(curdate())+1 day) AND express_date <= last_day(curdate()) AND employee_no = '"+employee_no+"'";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        return Integer.valueOf(query.uniqueResult().toString());
    }

    @Override
    public Integer getCustomerCountByCurrentMonth(String employee_no) {
        String sql = "SELECT IFNULL(COUNT(1),0) FROM t_customer  " +
                " WHERE  " +
                "  (create_time >= DATE_ADD(curdate(),interval -day(curdate())+1 day) OR update_time >= DATE_ADD(curdate(),interval -day(curdate())+1 day)) " +
                "  AND employee_no = '"+employee_no+"'";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        return Integer.valueOf(query.uniqueResult().toString());
    }

    
    /**
     * 手机接口 取得 员工数据卡中 拜访记录的条数 
     */
    @Override
    public Integer getRelationCountByCurrentMonth(String employee_no) {
        String sql = "select count(DISTINCT c.id) from t_relation r LEFT JOIN t_customer c ON c.id = r.customer_id" +
                " WHERE r.status = 0 and r.employee_no = '"+employee_no+"'" +
                " AND r.relation_date >= DATE_ADD(curdate(),interval -day(curdate())+1 day)" +
                " AND r.relation_date <= last_day(curdate())";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        return Integer.valueOf(query.uniqueResult().toString());
    }

    @Override
    public Integer getExpressStoreCountByCurrentMonth(Long store_id) {
        String sql = "SELECT IFNULL(COUNT(1),0) FROM t_express WHERE express_date >= DATE_ADD(curdate(),interval -day(curdate())+1 day) AND express_date <= last_day(curdate()) " +
                "AND status=0 and (express_status<>0 or express_status is null) AND store_id = '"+store_id+"'";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        return Integer.valueOf(query.uniqueResult().toString());
    }

    @Override
    public Integer getCustomerStoreCountByCurrentMonth(Long store_id) {
        String sql = "SELECT IFNULL(COUNT(1),0) FROM t_customer c LEFT JOIN tb_bizbase_user u ON u.employeeId = c.employee_no " +
                " WHERE (c.create_time >= DATE_ADD(curdate(),interval -day(curdate())+1 day)  " +
                "   OR c.update_time >= DATE_ADD(curdate(),interval -day(curdate())+1 day)) " +
                "   AND u.store_id = '"+store_id+"'";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        return Integer.valueOf(query.uniqueResult().toString());
    }

    /**
     * 手机 门店数据卡接口数据 
     */
    @Override
    public Integer getRelationStoreCountByCurrentMonth(Long store_id) {
        String sql = "select count(DISTINCT c.id) from t_relation r LEFT JOIN t_customer c ON c.id = r.customer_id " +
                " LEFT JOIN tb_bizbase_user u ON u.employeeId = r.employee_no " +
                " WHERE r.status = 0 and u.store_id =  " + store_id +
                " AND r.relation_date >= DATE_ADD(curdate(),interval -day(curdate())+1 day) " +
                " AND r.relation_date <= last_day(curdate())";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        return Integer.valueOf(query.uniqueResult().toString());
    }
    
    
    @Override
    public Integer getExpressCountByCurrentMonth(String employee_no,String month) {
    	String monthstr=null;
    	if(month!=null){
    		monthstr="'"+month+"-01'";
    	}else{
    		monthstr="curdate()";
    	}
        String sql = "SELECT IFNULL(COUNT(1),0) FROM t_express WHERE express_date >= DATE_ADD("+monthstr+",interval -day("+monthstr+")+1 day) AND express_date <= last_day("+monthstr+") AND employee_no = '"+employee_no+"'";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        return Integer.valueOf(query.uniqueResult().toString());
    }

    @Override
    public Integer getCustomerCountByCurrentMonth(String employee_no,String month) {
    	/*String monthstr=null;
    	if(month!=null){
    		monthstr="'"+month+"-01'";
    	}else{
    		monthstr="curdate()";
    	}
        String sql = "SELECT IFNULL(COUNT(1),0) FROM t_customer  " +
                " WHERE  " +
                "  (create_time >= DATE_ADD("+monthstr+",interval -day("+monthstr+")+1 day) OR update_time >= DATE_ADD("+monthstr+",interval -day("+monthstr+")+1 day)) " +
                "  AND employee_no = '"+employee_no+"'";*/
    	
    	String monthstr=null;
		if(month!=null && !DateUtil.isThisTime(month,"yyyy-MM")){
			monthstr=" (DATE_FORMAT(thc.six_date,'%Y-%m') ='"+month+"')";
		}else{
			monthstr=" (DATE_FORMAT(thc.six_date,'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m') OR thc.six_date is null)" ;
		}

    	String sql = "SELECT " +
				"  SUM( " +
				"    CASE WHEN " +
				"      (T.面积 is not null AND T.面积 != '') AND " +
				"      (T.朝向 is not null AND T.朝向 != '') AND " +
				"      (T.户型 is not null AND T.户型 != '') AND " +
				"      ((T.户型图 is not null AND T.户型图 != '') OR T.`房屋类型` = 0) AND " +
				"      (T.姓名 is not null AND T.姓名 != '') AND " +
				"      (T.性别 is not null) AND " +
				"      (T.电话 is not null AND T.电话 != '') AND " +
				"      (T.年龄 is not null ) AND " +
				"      (T.照片 is not null AND T.照片 != '') AND " +
				"      (T.民族 is not null AND T.民族 != '') AND " +
				"      (T.住房性质 is not null AND T.住房性质 != '') AND " +
				"      (T.职业 is not null AND T.职业 != '') AND" +
				"      (T.客户收入 is not null AND T.客户收入 != '') AND " +
				"      (T.加班 is not null AND T.加班 != '') AND" +
				"      (T.家庭人口数 is not null ) AND " +
				"      (T.学龄前人数 is not null ) AND " +
				"      (T.未成年人数 is not null ) AND " +
				"      (T.宠物类型 is not null AND T.宠物类型 != '')" +
				"    THEN 1 ELSE 0 END " +
				"  ) AS 第二等级个数 " +
				" FROM " +
				"( " +
				"  SELECT " +
				"    thc.id AS 客户户型id, " +
				"    tc.id AS 客户id, " +
				"    th.house_type AS 房屋类型, " +
				"    th.building_unit_no AS 单元号, " +
				"    th.commercial_floor_number AS 楼层, " +
				"    th.building_house_no AS 房间号, " +
				"    hs.house_area AS 面积, " +
				"    hs.house_toward AS 朝向, " +
				"    hs.house_style AS 户型, " +
				"    hs.house_pic AS 户型图, " +
				"    tc.`name` AS 姓名, " +
				"    tc.sex AS 性别, " +
				"    tc.age AS 年龄, " +
				"    tc.mobilephone AS 电话, " +
				"    tc.customer_pic AS 照片, " +
				"    tc.nationality AS 民族, " +
				"    tc.house_property 住房性质, " +
				"    tc.job AS 职业, " +
				"    tc.income AS 客户收入, " +
				"    tc.work_overtime AS 加班, " +
				"    tc.family_num AS 家庭人口数, " +
				"    tc.preschool_num AS 学龄前人数, " +
				"    tc.minor_num AS 未成年人数, " +
				"    tc.pet_type AS 宠物类型, " +
				"    u.employeeId AS 员工编码, " +
				"    u.`name` AS 员工姓名, " +
				"    u.store_id AS 门店id, " +
				"    s.`name` AS 门店名称," +
				"   thc.one_pay AS 第一等级支付," +
				"   thc.six_pay AS 第二等级支付," +
				"   thc.six_pay AS 第三等级支付" +
				"  FROM " +
				"    t_customer tc " +
				"  LEFT JOIN (select thc2.* from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id) thc ON tc.id = thc.customer_id " +
				"  LEFT JOIN t_house th ON th.id = thc.house_id " +
				"  LEFT JOIN t_building tb ON tb.id = th.building_id " +
				"  LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id " +
				"  LEFT JOIN t_house_style hs ON hs.house_id = th.id " +
				"  LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no " +
				"  LEFT JOIN t_store s ON s.store_id = u.store_id" +
				"  WHERE " + monthstr +
				//"  WHERE " + monthstr +" and u.pk_usergroup='3224' "+
				"  AND u.employeeId = '"+employee_no+"' " +
				") T " +
				"WHERE T.员工编码 != '' AND T.员工编码 IS NOT NULL AND T.员工姓名 NOT LIKE '%测试%'" +
				"GROUP BY T.`门店名称` ORDER BY T.门店id";
    	
    	
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        System.out.println(query.uniqueResult());
        return Integer.valueOf(query.uniqueResult()!=null?query.uniqueResult().toString():"0");
    }

    /**
     * 国安侠数据卡 【查询当前月份 该国安侠的 拜访记录 条数】
     */
    @Override
    public Integer getRelationCountByCurrentMonth(String employee_no,String month) {
    	String monthstr=null;
    	if(month!=null){
    		monthstr="'"+month+"-01'";
    	}else{
    		monthstr="curdate()";
    	}
        String sql = "select count(DISTINCT c.id)  from t_relation r LEFT JOIN t_customer c ON c.id = r.customer_id" +
                " WHERE r.status = 0 and  r.employee_no = '"+employee_no+"'" +
                " AND r.relation_date >= DATE_ADD("+monthstr+",interval -day("+monthstr+")+1 day)" +
                " AND r.relation_date <= last_day("+monthstr+")";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        return Integer.valueOf(query.uniqueResult().toString());
    }

    @Override
    public Integer getExpressStoreCountByCurrentMonth(Long store_id,String month) {
    	String monthstr=null;
    	if(month!=null){
    		monthstr="'"+month+"-01'";
    	}else{
    		monthstr="curdate()";
    	}
        String sql = "SELECT IFNULL(COUNT(1),0) FROM t_express WHERE  DATE_FORMAT(express_date,'%Y-%m')=DATE_FORMAT("+monthstr+",'%Y-%m')" +
                "AND status=0 and (express_status<>0 or express_status is null) AND store_id = '"+store_id+"'";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        return Integer.valueOf(query.uniqueResult().toString());
    }

    @Override
    public Integer getCustomerStoreCountByCurrentMonth(Long store_id,String month) {
    	String monthstr=null;
 		if( month!=null && !DateUtil.isThisTime(month,"yyyy-MM")){
			monthstr=" (DATE_FORMAT(thc.six_date,'%Y-%m') ='"+month+"')";
		}else{
			monthstr=" (DATE_FORMAT(thc.six_date,'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m') OR thc.six_date is null)" ;
		}

    	String sql = "SELECT " +
				"  SUM( " +
				"    CASE WHEN " +
				"      (T.面积 is not null AND T.面积 != '') AND " +
				"      (T.朝向 is not null AND T.朝向 != '') AND " +
				"      (T.户型 is not null AND T.户型 != '') AND " +
				"      ((T.户型图 is not null AND T.户型图 != '') OR T.`房屋类型` = 0) AND " +
				"      (T.姓名 is not null AND T.姓名 != '') AND " +
				"      (T.性别 is not null) AND " +
				"      (T.电话 is not null AND T.电话 != '') AND " +
				"      (T.年龄 is not null ) AND " +
				"      (T.照片 is not null AND T.照片 != '') AND " +
				"      (T.民族 is not null AND T.民族 != '') AND " +
				"      (T.住房性质 is not null AND T.住房性质 != '') AND " +
				"      (T.职业 is not null AND T.职业 != '') AND" +
				"      (T.客户收入 is not null AND T.客户收入 != '') AND " +
				"      (T.加班 is not null AND T.加班 != '') AND" +
				"      (T.家庭人口数 is not null ) AND " +
				"      (T.学龄前人数 is not null ) AND " +
				"      (T.未成年人数 is not null ) AND " +
				"      (T.宠物类型 is not null AND T.宠物类型 != '')" +
				"      THEN 1 ELSE 0 END " +
				"  ) AS 第二等级个数 " +
				" FROM " +
				"( " +
				"  SELECT " +
				"    thc.id AS 客户户型id, " +
				"    tc.id AS 客户id, " +
				"    th.house_type AS 房屋类型, " +
				"    th.building_unit_no AS 单元号, " +
				"    th.commercial_floor_number AS 楼层, " +
				"    th.building_house_no AS 房间号, " +
				"    hs.house_area AS 面积, " +
				"    hs.house_toward AS 朝向, " +
				"    hs.house_style AS 户型, " +
				"    hs.house_pic AS 户型图, " +
				"    tc.`name` AS 姓名, " +
				"    tc.sex AS 性别, " +
				"    tc.age AS 年龄, " +
				"    tc.mobilephone AS 电话, " +
				"    tc.customer_pic AS 照片, " +
				"    tc.nationality AS 民族, " +
				"    tc.house_property 住房性质, " +
				"    tc.job AS 职业, " +
				"    tc.income AS 客户收入, " +
				"    tc.work_overtime AS 加班, " +
				"    tc.family_num AS 家庭人口数, " +
				"    tc.preschool_num AS 学龄前人数, " +
				"    tc.minor_num AS 未成年人数, " +
				"    tc.pet_type AS 宠物类型, " +
				"    u.employeeId AS 员工编码, " +
				"    u.`name` AS 员工姓名, " +
				"    u.store_id AS 门店id, " +
				"    s.`name` AS 门店名称," +
				"   thc.one_pay AS 第一等级支付," +
				"   thc.six_pay AS 第二等级支付," +
				"   thc.third_grade_pay AS 第三等级支付" +
				"  FROM " +
				"    t_customer tc " +
				"  LEFT JOIN (select thc2.* from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id) thc ON tc.id = thc.customer_id " +
				"  LEFT JOIN t_house th ON th.id = thc.house_id " +
				"  LEFT JOIN t_building tb ON tb.id = th.building_id " +
				"  LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id " +
				"  LEFT JOIN t_house_style hs ON hs.house_id = th.id " +
				"  LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no " +
				"  LEFT JOIN t_store s ON s.store_id = u.store_id" +
				"  WHERE " + monthstr+
				//"  WHERE " + monthstr +" and u.pk_usergroup='3224' "+
				"  AND s.`store_id` = '"+store_id+"' " +
				") T " +
				"WHERE T.员工编码 != '' AND T.员工编码 IS NOT NULL AND T.员工姓名 NOT LIKE '%测试%'" +
				"GROUP BY T.`门店名称` ORDER BY T.门店id";


//        String sql = "SELECT IFNULL(COUNT(1),0) FROM t_customer c LEFT JOIN tb_bizbase_user u ON u.employeeId = c.employee_no " +
//                " WHERE (DATE_FORMAT(c.create_time,'%Y-%m')=DATE_FORMAT("+monthstr+",'%Y-%m') OR DATE_FORMAT(c.update_time,'%Y-%m')=DATE_FORMAT("+monthstr+",'%Y-%m'))" +
//                "   AND u.store_id = '"+store_id+"'";
        System.out.println(sql);
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
		List list = query.list();
        return Integer.valueOf(query.uniqueResult() == null?"0":query.uniqueResult().toString());
    }

    
    /**
     * 门店数据卡 根据门店查询 当前门店的 拜访记录条数 
     */
    @Override
    public Integer getRelationStoreCountByCurrentMonth(Long store_id,String month) {
    	String monthstr=null;
    	if(month!=null){
    		monthstr="'"+month+"-01'";
    	}else{
    		monthstr="curdate()";
    	}
        String sql = "SELECT case when sum(a.count) is null then 0 else sum(a.count) end from (select count(DISTINCT c.id) as count from t_relation r  " +
                " LEFT JOIN tb_bizbase_user u ON u.employeeId = r.employee_no LEFT JOIN t_customer c on c.id=r.customer_id  " +
                " WHERE r.status = 0 and  u.store_id = "+store_id+ " AND u.pk_usergroup=3224 "+
                " AND DATE_FORMAT(r.relation_date,'%Y-%m')=DATE_FORMAT("+monthstr+",'%Y-%m')  GROUP BY u.employeeId) a ";
        System.out.println(sql);
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        return Integer.valueOf(query.uniqueResult().toString());
    }

    @Override
    public Map<String, Integer> getExpressStoreCountByCurrentMonth(String employee_no) {
        String sql = "SELECT employee_no,COUNT(1) as express_count FROM t_express WHERE express_date >= DATE_ADD(curdate(),interval -day(curdate())+1 day) AND express_date <= last_day(curdate()) " +
                "AND status=0 and (express_status<>0 or express_status is null) AND employee_no in ("+employee_no+") GROUP BY employee_no";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        Map<String,Integer> map_result = new HashMap<String, Integer>();
        if(lst_data != null && lst_data.size() > 0){
            for(Object obj_data : lst_data){
                Map<String,Object> map_data = (Map<String,Object>)obj_data;
                if(map_data.get("employee_no") == null){
                    continue;
                }
                map_result.put(map_data.get("employee_no").toString(),Integer.valueOf(map_data.get("express_count").toString()));
            }
        }
        return map_result;
    }

    @Override
    public Map<String, Integer> getCustomerStoreCountByCurrentMonth(String employee_no) {
        String sql = "SELECT c.employee_no,COUNT(1) AS customer_count FROM t_customer c" +
                "        WHERE (c.create_time >= DATE_ADD(curdate(),interval -day(curdate())+1 day)" +
                "                OR c.update_time >= DATE_ADD(curdate(),interval -day(curdate())+1 day))" +
                "        AND c.employee_no in ("+employee_no+")";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        Map<String,Integer> map_result = new HashMap<String, Integer>();
        if(lst_data != null && lst_data.size() > 0){
            for(Object obj_data : lst_data){
                Map<String,Object> map_data = (Map<String,Object>)obj_data;
                if(map_data.get("employee_no") == null){
                    continue;
                }
                map_result.put(map_data.get("employee_no").toString(),Integer.valueOf(map_data.get("customer_count").toString()));
            }
        }
        return map_result;
    }

    @Override
    public Map<String, Integer> getRelationStoreCountByCurrentMonth(String employee_no) {
        String sql = "SELECT c.employee_no,count(1) AS relation_count from t_relation r LEFT JOIN t_customer c ON c.id = r.customer_id" +
                "        WHERE r.status = 0 and c.employee_no in ("+employee_no+")" +
                "        AND r.relation_date >= DATE_ADD(curdate(),interval -day(curdate())+1 day)" +
                "        AND r.relation_date <= last_day(curdate()) GROUP BY c.employee_no";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        Map<String,Integer> map_result = new HashMap<String, Integer>();
        if(lst_data != null && lst_data.size() > 0){
            for(Object obj_data : lst_data){
                Map<String,Object> map_data = (Map<String,Object>)obj_data;
                if(map_data.get("employee_no") == null){
                    continue;
                }
                map_result.put(map_data.get("employee_no").toString(),Integer.valueOf(map_data.get("relation_count").toString()));
            }
        }
        return map_result;
    }
    
    /**
     * 拜访记录 饼图
     */
	@Override
	public List<?> getRelationDataByStoreAndMouth(String employee_no,String month) {
		String monthString=null;
		if(month!=null){
			monthString="'"+month+"-01'";
		}else{
			monthString="curdate()";
		}
		String sql = "SELECT t.employee_no, t.employee_name, "+
	"SUM(t.two_month_amount) AS two_mouths_ago, "+
	"SUM(t.one_month_amount) AS one_mouths_ago, "+
	"SUM(t.curr_month_amount) AS this_mouth "+
"FROM(SELECT empamou.employee_no, "+
		"	empamou.employee_name, "+
			"CASE "+
			"	WHEN empamou.mounth = DATE_FORMAT( DATE_SUB("+monthString+", INTERVAL 2 MONTH), '%Y-%m' ) THEN "+
			"		empamou.data_amount "+
			"	ELSE 0 END AS two_month_amount, "+
			"CASE "+
			"	WHEN empamou.mounth = DATE_FORMAT(DATE_SUB("+monthString+", INTERVAL 1 MONTH),'%Y-%m') THEN "+
			"		empamou.data_amount "+
			"	ELSE 0 END AS one_month_amount, "+
			"CASE "+
			"	WHEN empamou.mounth = DATE_FORMAT("+monthString+", '%Y-%m') THEN "+
			"		empamou.data_amount "+
			"	ELSE 0 END AS curr_month_amount "+
		"FROM "+
		"	( "+
		"	select r.employee_name, "+
		"	r.employee_no, "+
			"		DATE_FORMAT(r.relation_date, '%Y-%m') AS mounth, "+
			"		COUNT(distinct c.id) AS data_amount from t_relation r  "+
              "   LEFT JOIN tb_bizbase_user u ON u.employeeId = r.employee_no LEFT JOIN t_customer c ON c.id=r.customer_id "+
	"WHERE r.status = 0 and r.employee_no in ("+employee_no+") AND "+
					"r.relation_date >= DATE_FORMAT( "+
					"	DATE_SUB("+monthString+", INTERVAL 2 MONTH), "+
					"	'%Y-%m' "+
				"	) "+
				" GROUP BY "+
				"	r.employee_no, "+
				"	DATE_FORMAT(r.relation_date, '%Y-%m') "+
			") empamou "+
	") t "+
"GROUP BY t.employee_no ";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        List<?> lst_data = query.list();
        
        return lst_data;
        
        
	}

	@Override
	public List<?> getCustomerDataByStoreAndMouth(String where,String month) {
		String monthString=null;
		if( month!=null && !DateUtil.isThisTime(month,"yyyy-MM")){
			monthString=" ( DATE_FORMAT(thc.six_date,'%Y-%m') ='"+month+"')";
		}else{
			monthString=" ( DATE_FORMAT(thc.six_date,'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m') OR thc.six_date is null)" ;
		}
		String sql="SELECT " +
				"  u.employeeId, " +
				"  u.`name`, " +
				"  u.disabledFlag," +
				"  SUM( " +
				"    CASE WHEN " +
				"      (T.面积 is not null AND T.面积 != '') AND " +
				"      (T.朝向 is not null AND T.朝向 != '') AND " +
				"      (T.户型 is not null AND T.户型 != '') AND " +
				"      ((T.户型图 is not null AND T.户型图 != '') OR T.`房屋类型` = 0) AND " +
				"      (T.姓名 is not null AND T.姓名 != '') AND " +
				"      (T.性别 is not null) AND " +
				"      (T.电话 is not null AND T.电话 != '') AND " +
				"      (T.年龄 is not null ) AND" +
				"      (T.照片 is not null AND T.照片 != '') AND " +
				"      (T.民族 is not null AND T.民族 != '') AND " +
				"      (T.住房性质 is not null AND T.住房性质 != '') AND " +
				"      (T.职业 is not null AND T.职业 != '') AND" +
				"      (T.客户收入 is not null AND T.客户收入 != '') AND " +
				"      (T.加班 is not null AND T.加班 != '') AND" +
				"      (T.家庭人口数 is not null ) AND " +
				"      (T.学龄前人数 is not null ) AND " +
				"      (T.未成年人数 is not null ) AND " +
				"      (T.宠物类型 is not null AND T.宠物类型 != '')" +
				"    THEN 1 ELSE 0 END " +
				"  ) AS data_amount " +
				" FROM " +
				"( " +
				"  SELECT " +
				"    thc.id AS 客户户型id, " +
				"    tc.id AS 客户id, " +
				"    th.building_unit_no AS 单元号, " +
				"    th.commercial_floor_number AS 楼层, " +
				"    th.building_house_no AS 房间号, " +
				"    th.house_type AS 房屋类型, " +
				"    hs.house_area AS 面积, " +
				"    hs.house_toward AS 朝向, " +
				"    hs.house_style AS 户型, " +
				"    hs.house_pic AS 户型图, " +
				"    tc.`name` AS 姓名, " +
				"    tc.sex AS 性别, " +
				"    tc.age AS 年龄, " +
				"    tc.mobilephone AS 电话, " +
				"    tc.customer_pic AS 照片, " +
				"    tc.nationality AS 民族, " +
				"    tc.house_property 住房性质, " +
				"    tc.job AS 职业, " +
				"    tc.income AS 客户收入, " +
				"    tc.work_overtime AS 加班, " +
				"    tc.family_num AS 家庭人口数, " +
				"    tc.preschool_num AS 学龄前人数, " +
				"    tc.minor_num AS 未成年人数, " +
				"    tc.pet_type AS 宠物类型, " +
				"    tc.employee_no AS 员工编码, " +
				"    thc.one_pay AS 第一等级支付," +
				"    thc.six_pay AS 第二等级支付" +
				"  FROM " +
				"    t_customer tc" +
				"  LEFT JOIN (select thc2.* from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id) thc thc ON tc.id = thc.customer_id " +
				"  LEFT JOIN t_house th ON th.id = thc.house_id " +
				"  LEFT JOIN t_building tb ON tb.id = th.building_id " +
				"  LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id " +
				"  LEFT JOIN t_house_style hs ON hs.house_id = th.id " +
				"  LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no " +
				"  LEFT JOIN t_store s ON s.store_id = u.store_id" +
				"  WHERE (" + monthString + ") " +
				"  AND tc.employee_no != '' AND tc.employee_no IS NOT NULL " +
				") T RIGHT JOIN tb_bizbase_user u ON u.employeeId = T.`员工编码` " +
				"WHERE u.pk_usergroup=3224 AND u.name NOT LIKE '%测试%'" + where +
				"GROUP BY u.employeeId,u.`name` HAVING (u.disabledFlag = 1 ) ORDER BY data_amount desc";

		//SQL查询对象
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);

		//获得查询数据
		List<?> lst_data = query
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		return lst_data;
	}
	/**
	 * 表调用的快递代送(国安侠)
	 */
	@Override
	public List<Map<String, Object>> getExpressDataByStoreAndMouth(String where, PageInfo pageInfo,String month,Long store_id) {
		String monthString=null;
		if(month!=null){
			monthString=" DATE_FORMAT(t_express.express_date,'%Y-%m') ='"+month+"' ";
		}else{
			monthString="DATE_FORMAT(t_express.express_date,'%Y-%m')= DATE_FORMAT(curdate(), '%Y-%m')" ;
		}
		String str_count_sql="SELECT COUNT(*) from ( "+
		" select * from (SELECT "+
		"	u.employeeId,u.name, "+
		"	SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) AS data_amount "+
		"FROM "+
		"	tb_bizbase_user u  "+
		"	LEFT JOIN  "+
		"	(SELECT * FROM t_express WHERE status=0 and (express_status<>0 or express_status is null) and "+monthString+" and store_id="+store_id+") ex  "+
		"	ON ex.employee_no = u.employeeId "+
		"WHERE	1=1  "+
		" AND  u.disabledFlag=1 and u.pk_usergroup = 3224 "+where+
		" GROUP BY "+
		"	u.employeeId "+
		"order by SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) DESC  ) a union select * from ( "+
		" SELECT u.employeeId,u.name, SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) AS data_amount "+
		" FROM 	tb_bizbase_user u  	LEFT JOIN  	"+
		" (SELECT * FROM t_express WHERE "+
		" status=0 and (express_status<>0 or express_status is null) and  DATE_FORMAT(t_express.express_date,'%Y-%m')= DATE_FORMAT(curdate(), '%Y-%m')) ex  	"+
		" ON ex.employee_no = u.employeeId WHERE	1=1   AND u.employeeId in (SELECT th.employee_no FROM t_humanresources th WHERE th.store_id="+store_id+" AND th.zw='副店长'and th.humanstatus=1 AND th.remark = '国安侠')  and u.store_id ="+store_id +  
		" GROUP BY 	u.employeeId order by SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) DESC) b"+
") fst";
		
		
		String fid_sql=" select * from (SELECT "+
			"u.employeeId,u.name, "+
			"SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) AS data_amount "+
		"FROM "+
		"	tb_bizbase_user u  "+
		"	LEFT JOIN  "+
		"	(SELECT * FROM t_express WHERE status=0 and (express_status<>0 or express_status is null) and  "+monthString+") ex  "+
		"	ON ex.employee_no = u.employeeId "+
		"WHERE	1=1  "+
		" AND  u.disabledFlag=1 and u.pk_usergroup = 3224 "+where+
		" GROUP BY "+
		"	u.employeeId "+
		" order by SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) DESC) a union select * from ( "+
		" SELECT u.employeeId,u.name, SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) AS data_amount "+
		" FROM 	tb_bizbase_user u  	LEFT JOIN  	"+
		" (SELECT * FROM t_express WHERE "+
		" status=0 and (express_status<>0 or express_status is null) and  DATE_FORMAT(t_express.express_date,'%Y-%m')= DATE_FORMAT(curdate(), '%Y-%m')) ex  	"+
		" ON ex.employee_no = u.employeeId WHERE	1=1   AND u.employeeId in (SELECT th.employee_no FROM t_humanresources th WHERE th.store_id="+store_id+" AND th.zw='副店长' and th.humanstatus=1 AND th.remark = '国安侠')  and u.store_id ="+store_id+  
		" GROUP BY 	u.employeeId order by SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) DESC) b";
		
		
		System.out.println(fid_sql);
        //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(fid_sql);

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
	
	
	
	/**
	 * 表调用的快递代送(副店长)
	 */
	public List<Map<String, Object>> getExpressDataByStoreFDZAndMouth(String where, PageInfo pageInfo,String month,Long store_id) {
		String monthString=null;
		if(month!=null){
			monthString=" DATE_FORMAT(t_express.express_date,'%Y-%m') ='"+month+"' ";
		}else{
			monthString="DATE_FORMAT(t_express.express_date,'%Y-%m')= DATE_FORMAT(curdate(), '%Y-%m')" ;
		}
		String str_count_sql="SELECT COUNT(*) from ( "+
		"SELECT "+
		"	u.employeeId,u.name, "+
		"	SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) AS data_amount "+
		"FROM "+
		"	tb_bizbase_user u  "+
		"	LEFT JOIN  "+
		"	(SELECT * FROM t_express WHERE status=0 and (express_status<>0 or express_status is null) and "+monthString+" and store_id="+store_id+") ex  "+
		"	ON ex.employee_no = u.employeeId "+
		"WHERE	1=1  "+
		" AND u.pk_usergroup = 3224 "+where+
		" GROUP BY "+
		"	u.employeeId "+
		"order by SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) DESC  "+
") fst";
		
		
		String fid_sql="SELECT "+
			"u.employeeId,u.name, "+
			"SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) AS data_amount "+
		"FROM "+
		"	tb_bizbase_user u  "+
		"	LEFT JOIN  "+
		"	(SELECT * FROM t_express WHERE status=0 and (express_status<>0 or express_status is null) and  "+monthString+") ex  "+
		"	ON ex.employee_no = u.employeeId "+
		"WHERE	1=1  "+
		" AND u.pk_usergroup = 3224 "+where+
		" GROUP BY "+
		"	u.employeeId "+
		"order by SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) DESC";
		System.out.println(fid_sql);
        //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(fid_sql);

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
	
	
	
	
	
	
	
	
	
	
	/**
	 * 饼图调用的快递代送
	 */
	@Override
	public List<?> getExpressDataByStoreAndMouth_tu(String employee_no,String month) {
		String monthString=null;
		if(month!=null){
			monthString="'"+month+"-01'";
		}else{
			monthString="curdate()";
		}
		String sql="SELECT t.employee_no, "+
				"SUM(t.two_month_amount) AS two_mouths_ago,  "+
				"SUM(t.one_month_amount) AS one_mouths_ago,  "+
				"SUM(t.curr_month_amount) AS this_mouth  "+
			"FROM (SELECT empamou.employee_no,  "+
					"	CASE WHEN empamou.mounth = DATE_FORMAT( DATE_SUB("+monthString+", INTERVAL 2 MONTH), '%Y-%m' ) THEN empamou.data_amount  "+
						"	ELSE 0 END AS two_month_amount,  "+
					"	CASE WHEN empamou.mounth = DATE_FORMAT(DATE_SUB("+monthString+", INTERVAL 1 MONTH),'%Y-%m') THEN  "+
					"			empamou.data_amount ELSE 0 END AS one_month_amount,  "+
					"	CASE WHEN empamou.mounth = DATE_FORMAT("+monthString+", '%Y-%m') THEN empamou.data_amount  "+
					"		ELSE 0 END AS curr_month_amount FROM (  "+
					"	SELECT ex.employee_no,DATE_FORMAT(ex.express_date, '%Y-%m') AS mounth,COUNT(1) as data_amount from t_express ex WHERE ex.status=0 and (ex.express_status<>0 or ex.express_status is null) AND ex.express_date >= DATE_FORMAT(  "+
						"			DATE_SUB("+monthString+", INTERVAL 2 MONTH), '%Y-%m'  "+
						"		) AND ex.employee_no in ("+employee_no+")  GROUP BY ex.employee_no, DATE_FORMAT(ex.express_date, '%Y-%m')  "+
					"	) empamou ) t GROUP BY t.employee_no";
					SQLQuery query = getHibernateTemplate().getSessionFactory()
			                .getCurrentSession()
			                .createSQLQuery(sql);
			        List<?> lst_data = query.list();
			        return lst_data;
	}
/**
 * 
 * @param where
 * @param pageInfo
 * @param month 格式为年-月 例如：2016-11
 * @return
 */
	@Override
	public List<Map<String, Object>> getCustomerDataByStoreAndMouth_tu(String where, PageInfo pageInfo,String month,String grade,Long storeId) {
		String monthString=null;
		String target = "thc.six_date";
		
		if("first".equals(grade)){
			target = "thc.one_date";
		}else if("second".equals(grade)){
			target = "thc.six_date";
		}else if(grade==null||"".equals(grade)){
			grade = "second";
		}
		if( month!=null && !DateUtil.isThisTime(month,"yyyy-MM")){
			monthString=" ( DATE_FORMAT("+target+",'%Y-%m') ='"+month+"')";
		}else{
			monthString=" ( DATE_FORMAT("+target+",'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m') OR "+target+" is null)" ;
		}

		
		String sql="SELECT " +
				"  u.employeeId, " +
				"  u.`name`, " +
				"  u.disabledFlag," +
				"  SUM( " +
				"    CASE WHEN " +
				"      (T.面积 is not null AND T.面积 != '') AND " +
				"      (T.朝向 is not null AND T.朝向 != '') AND " +
				"      (T.户型 is not null AND T.户型 != '') AND " +
				"      ((T.户型图 is not null AND T.户型图 != '') OR T.`房屋类型` = 0) AND " +
				"      (T.姓名 is not null AND T.姓名 != '') AND " +
				"      (T.性别 is not null) AND " +
				"      (T.电话 is not null AND T.电话 != '') AND " +
				"      (T.年龄 is not null ) " ;
				if("second".equals(grade)){
					sql +=  "   AND (T.照片 is not null AND T.照片 != '') AND" +
							"      (T.民族 is not null AND T.民族 != '') AND" +
							"      (T.住房性质 is not null AND T.住房性质 != '') AND" +
							"      (T.职业 is not null AND T.职业 != '') AND" +
							"      (T.客户收入 is not null AND T.客户收入 != '') AND" +
							"      (T.加班 is not null AND T.加班 != '') AND" +
							"      (T.家庭人口数 is not null ) AND" +
							"      (T.学龄前人数 is not null ) AND" +
							"      (T.未成年人数 is not null ) AND" +
							"      (T.宠物类型 is not null AND T.宠物类型 != '')";
				}
				
				sql += "    THEN 1 ELSE 0 END " +
				"  ) AS data_amount " +
				" FROM " +
				"( " +
				"  SELECT " +
				"    thc.id AS 客户户型id, " +
				"    tc.id AS 客户id, " +
				"    th.building_unit_no AS 单元号, " +
				"    th.commercial_floor_number AS 楼层, " +
				"    th.building_house_no AS 房间号, " +
				"    th.house_type AS 房屋类型, " +
				"    hs.house_area AS 面积, " +
				"    hs.house_toward AS 朝向, " +
				"    hs.house_style AS 户型, " +
				"    hs.house_pic AS 户型图, " +
				"    tc.`name` AS 姓名, " +
				"    tc.sex AS 性别, " +
				"    tc.age AS 年龄, " +
				"    tc.mobilephone AS 电话, " +
				"    tc.customer_pic AS 照片, " +
				"    tc.nationality AS 民族, " +
				"    tc.house_property 住房性质, " +
				"    tc.job AS 职业, " +
				"    tc.income AS 客户收入, " +
				"    tc.work_overtime AS 加班, " +
				"    tc.family_num AS 家庭人口数, " +
				"    tc.preschool_num AS 学龄前人数, " +
				"    tc.minor_num AS 未成年人数, " +
				"    tc.pet_type AS 宠物类型, " +
				"    tc.employee_no AS 员工编码, " +
				"    thc.one_pay AS 第一等级支付," +
				"    thc.six_pay AS 第二等级支付," +
				"    thc.third_grade_pay AS 第三等级支付" +
				"  FROM " +
				"    t_customer tc" +
				"  LEFT JOIN (select thc2.* from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id) thc ON tc.id = thc.customer_id " +
				"  LEFT JOIN t_house th ON th.id = thc.house_id " +
				"  LEFT JOIN t_building tb ON tb.id = th.building_id " +
				"  LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id " +
				"  LEFT JOIN t_house_style hs ON hs.house_id = th.id " +
				"  WHERE " + monthString + " " +
				"  AND tc.employee_no != '' AND tc.employee_no IS NOT NULL " +
				") T RIGHT JOIN tb_bizbase_user u ON u.employeeId = T.`员工编码` " +
				"WHERE (u.pk_usergroup=3224 or  u.employeeId IN (select employee_no from t_humanresources where zw ='副店长' and remark='国安侠' and store_id="+storeId+"))   AND u.name NOT LIKE '%测试%'" + where +
				"GROUP BY u.employeeId,u.`name` HAVING (u.disabledFlag = 1 ) ORDER BY data_amount desc";
		
		
		
		String str_count_sql = "SELECT COUNT(1) FROM ("+sql+") C";

		//SQL查询对象
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);

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

	@Override
	public List<Map<String, Object>> getRelationDataByStoreAndMouth_tu(String where, PageInfo pageInfo,String month,Long store_id) {
		String monthString=null;
		if(month!=null){
			monthString=" DATE_FORMAT(relation_date,'%Y-%m') ='"+month+"' ";
		}else{
			monthString="DATE_FORMAT(relation_date,'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m')" ;
		}
		
		String str_count_sql="SELECT COUNT(*) from (select * from (SELECT u.employeeId,u.`name`,SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) as data_amount FROM tb_bizbase_user u LEFT JOIN (  "+
" select * from t_relation WHERE status = 0 and "+monthString+
" ) ex ON u.employeeId=ex.employee_no  "+
                   "    WHERE u.disabledFlag=1 and u.pk_usergroup=3224  "+where+
" GROUP BY u.employeeId ORDER BY SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) DESC ) a union select * from (SELECT u.employeeId,u.`name`,SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) as data_amount "+ 
					"	FROM tb_bizbase_user u LEFT JOIN (  select * from t_relation WHERE status = 0 and DATE_FORMAT(relation_date,'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m')   "+
					"	GROUP BY t_relation.customer_id,t_relation.employee_no) ex ON u.employeeId=ex.employee_no      "+
					"	WHERE 1=1 AND u.employeeId in(SELECT th.employee_no FROM t_humanresources th WHERE th.store_id="+store_id+" AND th.zw='副店长' and th.humanstatus=1 AND th.remark = '国安侠')   "+
					"	and 1=1 "+where+"  GROUP BY u.employeeId ORDER BY SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) DESC) b) fst";
			
			
			String fid_sql="select * from (SELECT u.employeeId,u.`name`,SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) as data_amount FROM tb_bizbase_user u LEFT JOIN ( "+
					" select * from t_relation WHERE status = 0 and "+monthString+
					"  GROUP BY t_relation.customer_id,t_relation.employee_no) ex ON u.employeeId=ex.employee_no "+
					"    WHERE u.disabledFlag=1 and u.pk_usergroup=3224 "+where+
					" GROUP BY u.employeeId ORDER BY SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) DESC) a union select * from (SELECT u.employeeId,u.`name`,SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) as data_amount "+ 
					"	FROM tb_bizbase_user u LEFT JOIN (  select * from t_relation WHERE status = 0 and DATE_FORMAT(relation_date,'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m')   "+
					"	GROUP BY t_relation.customer_id,t_relation.employee_no) ex ON u.employeeId=ex.employee_no      "+
					"	WHERE 1=1 AND u.employeeId in(SELECT th.employee_no FROM t_humanresources th WHERE th.store_id="+store_id+" AND th.zw='副店长' and th.humanstatus=1 AND th.remark = '国安侠')   "+
					"	and 1=1 "+where+"  GROUP BY u.employeeId ORDER BY SUM(CASE WHEN ex.id is not null THEN 1 ELSE 0 END) DESC) b";
	        //SQL查询对象
	        SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession().createSQLQuery(fid_sql);

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

	@Override
	public Integer getXXExpressStoreCountByCurrentMonth(Long store_id, String month) {
		String monthstr=null;
    	if(month!=null){
    		monthstr="'"+month+"-01'";
    	}else{
    		monthstr="curdate()";
    	}
        String sql = "SELECT IFNULL(COUNT(1),0) FROM t_xx_express WHERE express_date >= DATE_ADD("+monthstr+",interval -day("+monthstr+")+1 day) AND express_date <= last_day("+monthstr+") " +
                "AND status<>1 AND store_id = '"+store_id+"'";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        return Integer.valueOf(query.uniqueResult().toString());
	}

	@Override
	public Integer getSelfExpressStoreCountByCurrentMonth(Long store_id, String month) {
		String monthstr=null;
    	if(month!=null){
    		monthstr="'"+month+"-01'";
    	}else{
    		monthstr="curdate()";
    	}
        String sql = "SELECT IFNULL(COUNT(1),0) FROM t_self_express WHERE express_date >= DATE_ADD("+monthstr+",interval -day("+monthstr+")+1 day) AND express_date <= last_day("+monthstr+") " +
                "AND status=0 AND store_id = '"+store_id+"'";
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        return Integer.valueOf(query.uniqueResult().toString());
	}

	@Override
	public List<Map<String,Object>> getMonthDataCustomerByEmployee(String employee, String month) {
		String monthstr=null;
    	if(month!=null){
    		monthstr="'"+month+"-01'";
    	}else{
    		monthstr="curdate()";
    	}
    	String find_sql="SELECT past.month,IFNULL(tms.total_data,0) as total FROM ( "+
	"SELECT DATE_FORMAT("+monthstr+", '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 1 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 2 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 3 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 4 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 5 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 6 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 7 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 8 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 9 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 10 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 11 MONTH), '%Y-%m') AS `month` "+
") past LEFT JOIN ( "+
"SELECT DATE_FORMAT(create_time,'%Y-%m') as month_data,COUNT(*) as total_data FROM t_customer WHERE employee_no='"+employee+"' AND (DATE_FORMAT(create_time,'%Y-%m')>DATE_FORMAT(date_sub("+monthstr+", interval 12 month),'%Y-%m') "+
" OR DATE_FORMAT(update_time,'%Y-%m')>DATE_FORMAT(date_sub("+monthstr+", interval 12 month),'%Y-%m')) GROUP BY DATE_FORMAT(create_time,'%Y-%m') "+
") tms ON past.month=tms.month_data ORDER BY past.month";
    	 SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession().createSQLQuery(find_sql);
    	 List<?> lst_data = query
                 .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                 .list();

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

	@Override
	public List<Map<String,Object>> getMonthDataRelationByEmployee(String employee, String month) {
		String monthstr=null;
    	if(month!=null){
    		monthstr="'"+month+"-01'";
    	}else{
    		monthstr="curdate()";
    	}
    	String find_sql="SELECT past.month,IFNULL(tms.total_data,0) as total FROM ( "+
	"SELECT DATE_FORMAT("+monthstr+", '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 1 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 2 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 3 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 4 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 5 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 6 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 7 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 8 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 9 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 10 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 11 MONTH), '%Y-%m') AS `month` "+
") past LEFT JOIN ( "+
"SELECT DATE_FORMAT(relation_date,'%Y-%m') as month_data,COUNT(*) as total_data from t_relation WHERE status = 0 and  DATE_FORMAT(relation_date,'%Y-%m')>DATE_FORMAT(date_sub("+monthstr+", interval 12 month),'%Y-%m') "+
" AND DATE_FORMAT(relation_date,'%Y-%m')<=DATE_FORMAT(date_sub("+monthstr+", interval 0 month),'%Y-%m') AND employee_no='"+employee+"' GROUP BY DATE_FORMAT(relation_date,'%Y-%m') "+
") tms ON past.month=tms.month_data ORDER BY past.month";
    	 SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession().createSQLQuery(find_sql);
    	 List<?> lst_data = query
                 .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                 .list();
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

	@Override
	public List<Map<String,Object>> getMonthDataExpressByEmployee(String employee, String month) {
		String monthstr=null;
    	if(month!=null){
    		monthstr="'"+month+"-01'";
    	}else{
    		monthstr="curdate()";
    	}
    	String find_sql="SELECT past.month,IFNULL(tms.total_data,0) as total FROM ( "+
	"SELECT DATE_FORMAT("+monthstr+", '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 1 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 2 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 3 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 4 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 5 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 6 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 7 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 8 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 9 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 10 MONTH), '%Y-%m') AS `month`  "+
   " UNION SELECT DATE_FORMAT(("+monthstr+" - INTERVAL 11 MONTH), '%Y-%m') AS `month` "+
") past LEFT JOIN ( "+
"SELECT DATE_FORMAT(express_date,'%Y-%m') as month_data,COUNT(*) as total_data from t_express  "+
"WHERE status=0 and (express_status<>0 or express_status is null) AND employee_no='"+employee+"'  "+
"AND DATE_FORMAT(express_date,'%Y-%m')>DATE_FORMAT(date_sub("+monthstr+", interval 12 month),'%Y-%m') "+
"AND DATE_FORMAT(express_date,'%Y-%m')<=DATE_FORMAT(date_sub("+monthstr+", interval 0 month),'%Y-%m') "+
"GROUP BY DATE_FORMAT(express_date,'%Y-%m') "+
") tms ON past.month=tms.month_data ORDER BY past.month";
    	 SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession().createSQLQuery(find_sql);
    	 List<?> lst_data = query
                 .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                 .list();

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

	@Override
	public List<Map<String, Object>> getCustomerByMonth(String where, String month) {
		String string_month="";
		if(month!=null){
			string_month=" and ( DATE_FORMAT(T.six_date,'%Y-%m') ='"+month+"' OR T.six_date is null)";
		}else{
			string_month=" and ( DATE_FORMAT(T.six_date,'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m') OR T.six_date is null)" ;
		}
		String sql="Select s.* from ( SELECT T.* FROM (SELECT "+
				   " thc.id AS 客户户型id,  "+
				  "  tc.id AS id,  "+
				  "  th.house_type AS 房屋类型,  "+
				  "  th.id AS house_id,  "+
				  "  thc.one_date, "+
				  "  thc.six_date,"+
				  "  th.building_unit_no AS unit_no,  "+
				  "  th.commercial_floor_number AS 楼层,  "+
				  "  th.building_house_no AS house_no,  "+
				  "  hs.house_area AS house_area,  "+
				  "  hs.house_toward AS house_toward,  "+
				  "  hs.house_style AS house_style,  "+
				   " (CASE WHEN (isnull(hs.house_pic) OR (trim(hs.house_pic) = '') OR (trim(hs.house_pic) = '无')) THEN '无' ELSE '有' END ) AS is_pic,  "+
				  "  tc.`name` AS name,  "+
				  "  case when tc.sex=0 then '男' ELSE '女' end sex,  "+
				  "  tc.age AS age,  "+
				  "  tc.mobilephone AS mobilephone,  "+
				  "  tc.customer_pic AS 照片,  "+
				  "  tc.nationality AS 民族,  "+
				   " tc.house_property 住房性质,  "+
				  "  tc.job AS 职业, "+
					"	CASE "+
		" WHEN (th.house_type = 0) THEN "+
		"	concat( "+
			"	ttv.`name`, "+
			"	'街', "+
			"	ifnull(th.`house_no`,'--'), "+
			"	'号' "+
			") "+
		"ELSE "+
			"concat( "+
			"	ttv.`name`, "+
			"	tb.`name`, "+
			"	'号楼', "+
			"	th.`building_unit_no`, "+
			"	'单元', "+
			"	th.`building_house_no`, "+
			"	'号' "+
			") "+
		"END "+
	 "AS `address`, "+
				  "  tc.income AS 客户收入,  "+
				  "   tc.work_overtime AS 加班,  "+
				  "  tc.family_num AS 家庭人口数,  "+
				  "  tc.preschool_num AS 学龄前人数,  "+
				  "  tc.minor_num AS 未成年人数,  "+
				  " tc.pet_type AS 宠物类型,  "+
				  "  u.employeeId AS employeeId,  "+
				  "   u.`name` AS 员工姓名,  "+
				  "    u.store_id AS store_id,  "+
				  "    s.`name` AS 门店名称, "+
				  "   thc.one_pay AS 一元支付, "+
				  "   thc.six_pay AS 六元支付, "+
				  "	tc.create_time, "+
				  " tc.update_time "+
				  "  FROM  "+
				  "   t_customer tc  "+
				  "  LEFT JOIN (select thc2.* from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id) thc ON tc.id = thc.customer_id  "+
				  "  LEFT JOIN t_house th ON th.id = thc.house_id  "+
				  "  LEFT JOIN t_building tb ON tb.id = th.building_id  "+
				  "  LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id  "+
				  "  LEFT JOIN t_house_style hs ON hs.house_id = th.id  "+
				  " LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no  "+
				  " LEFT JOIN t_store s ON s.store_id = u.store_id "+
				 
	          ") T WHERE (T.house_area is not null AND T.house_area != '') AND  "+
				   "   (T.house_toward is not null AND T.house_toward != '') AND  "+
				   "   (T.house_style is not null AND T.house_style != '') AND  "+
				   "   ((T.is_pic ='有') OR T.`房屋类型` = 0) AND  "+
				    "  (T.`name` is not null AND T.`name` != '') AND  "+
				    "  (T.sex is not null) AND  "+
				    "  (T.mobilephone is not null AND T.mobilephone != '') AND  "+
				    "  (T.age is not null AND T.age != '') "+
				    "  AND (T.照片 is not null AND T.照片 != '')"
					+ "		AND (T.民族 is not null AND T.民族 != '')"
					+ "		AND (T.住房性质 is not null AND T.住房性质 != '')"
					+ "		AND (T.客户收入 is not null AND T.客户收入 != '')"
					+ "		AND (T.加班 is not null AND T.加班 != '')"
					+ "     AND (T.家庭人口数 is not null )"
					+ "		AND (T.学龄前人数 is not null )"
					+ "		AND (T.未成年人数 is not null )"
					+ "		AND (T.宠物类型 is not null AND T.宠物类型 != '') "
				    +string_month+where+" GROUP BY T.id ) s";
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql);
		List<?> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
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

	
	@Override
	public int getCustomerCountByCurrentMonth(String employee_no, String month, String grade,Long storeId) {
		
		String monthstr=null;
		String  targetDate = "thc.six_date";
		if("first".equals(grade)){
			targetDate="thc.one_date";
		}else if("second".equals(grade)){
			targetDate="thc.six_date";
		}else if(grade==null||"".equals(grade)){
			grade = "second";
		}
		if(month!=null && !DateUtil.isThisTime(month,"yyyy-MM")){
			monthstr=" (DATE_FORMAT("+targetDate+",'%Y-%m') ='"+month+"')";
		}else{
			monthstr=" (DATE_FORMAT("+targetDate+",'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m') OR "+targetDate+" is null)" ;
		}
        
    	String sql = "SELECT " +
				"  SUM( " +
				"    CASE WHEN " +
				"      (T.面积 is not null AND T.面积 != '') AND " +
				"      (T.朝向 is not null AND T.朝向 != '') AND " +
				"      (T.户型 is not null AND T.户型 != '') AND " +
				"      ((T.户型图 is not null AND T.户型图 != '') OR T.`房屋类型` = 0) AND " +
				"      (T.姓名 is not null AND T.姓名 != '') AND " +
				"      (T.性别 is not null) AND " +
				"      (T.电话 is not null AND T.电话 != '') AND " +
				"      (T.年龄 is not null )  " ;
				if("second".equals(grade)){//第二等级基础字段量
					sql+= "   AND   (T.照片 is not null AND T.照片 != '') AND " +
					"      (T.民族 is not null AND T.民族 != '') AND " +
					"      (T.住房性质 is not null AND T.住房性质 != '') AND " +
					"      (T.职业 is not null AND T.职业 != '') AND" +
					"      (T.客户收入 is not null AND T.客户收入 != '') AND " +
					"      (T.加班 is not null AND T.加班 != '') AND" +
					"      (T.家庭人口数 is not null ) AND " +
					"      (T.学龄前人数 is not null ) AND " +
					"      (T.未成年人数 is not null ) AND " +
					"      (T.宠物类型 is not null AND T.宠物类型 != '')" ;
				}
				
				sql+= " THEN 1 ELSE 0 END ) AS 个数  FROM " +
				"( " +
				"  SELECT " +
				"    thc.id AS 客户户型id, " +
				"    tc.id AS 客户id, " +
				"    th.house_type AS 房屋类型, " +
				"    th.building_unit_no AS 单元号, " +
				"    th.commercial_floor_number AS 楼层, " +
				"    th.building_house_no AS 房间号, " +
				"    hs.house_area AS 面积, " +
				"    hs.house_toward AS 朝向, " +
				"    hs.house_style AS 户型, " +
				"    hs.house_pic AS 户型图, " +
				"    tc.`name` AS 姓名, " +
				"    tc.sex AS 性别, " +
				"    tc.age AS 年龄, " +
				"    tc.mobilephone AS 电话, " +
				"    tc.customer_pic AS 照片, " +
				"    tc.nationality AS 民族, " +
				"    tc.house_property 住房性质, " +
				"    tc.job AS 职业, " +
				"    tc.income AS 客户收入, " +
				"    tc.work_overtime AS 加班, " +
				"    tc.family_num AS 家庭人口数, " +
				"    tc.preschool_num AS 学龄前人数, " +
				"    tc.minor_num AS 未成年人数, " +
				"    tc.pet_type AS 宠物类型, " +
				"    u.employeeId AS 员工编码, " +
				"    u.`name` AS 员工姓名, " +
				"    u.store_id AS 门店id, " +
				"    s.`name` AS 门店名称," +
				"   thc.one_pay AS 第一等级支付," +
				"   thc.six_pay AS 第二等级支付," +
				"   thc.six_pay AS 第三等级支付" +
				"  FROM " +
				"    t_customer tc " +
				"  LEFT JOIN (select thc2.id,thc2.house_id,thc2.customer_id,thc2.one_pay,thc2.six_pay,thc2.one_date,thc2.six_date,thc2.third_grade_pay,thc2.third_grade_date from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id)  thc ON tc.id = thc.customer_id " +
				"  LEFT JOIN t_house th ON th.id = thc.house_id " +
				"  LEFT JOIN t_building tb ON tb.id = th.building_id " +
				"  LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id " +
				"  LEFT JOIN t_house_style hs ON hs.house_id = th.id " +
				"  LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no " +
				"  LEFT JOIN t_store s ON s.store_id = u.store_id" +
				"  WHERE " + monthstr+
				//"  WHERE " + monthstr+" and u.pk_usergroup='3224'"+
				"  AND u.employeeId = '"+employee_no+"' " +
				") T " +
				"WHERE T.员工编码 != '' AND T.员工编码 IS NOT NULL AND T.员工姓名 NOT LIKE '%测试%'" +
				"GROUP BY T.`门店名称` ORDER BY T.门店id";
    	
    	
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
        System.out.println(query.uniqueResult());
        return Integer.valueOf(query.uniqueResult()!=null?query.uniqueResult().toString():"0");
	}


	public Integer getCustomerStoreCountByCurrentMonth(Long store_id,String month,String grade) {
    	String monthstr=null;
    	String  targetDate = "thc.six_date";
		if("first".equals(grade)){
			targetDate="thc.one_date";
		}else if("second".equals(grade)){
			targetDate="thc.six_date";
		}else if(grade==null||"".equals(grade)){
			grade = "second";
		}
 		if( month!=null && !DateUtil.isThisTime(month,"yyyy-MM")){
			monthstr=" (DATE_FORMAT("+targetDate+",'%Y-%m') ='"+month+"')";
		}else{
			monthstr=" (DATE_FORMAT("+targetDate+",'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m') OR "+targetDate+" is null)" ;
		}

    	String sql = "SELECT " +
				"  SUM( " +
				"    CASE WHEN " +
				"      (T.面积 is not null AND T.面积 != '') AND " +
				"      (T.朝向 is not null AND T.朝向 != '') AND " +
				"      (T.户型 is not null AND T.户型 != '') AND " +
				"      ((T.户型图 is not null AND T.户型图 != '') OR T.`房屋类型` = 0) AND " +
				"      (T.姓名 is not null AND T.姓名 != '') AND " +
				"      (T.性别 is not null) AND " +
				"      (T.电话 is not null AND T.电话 != '') AND " +
				"      (T.年龄 is not null )  " ;
    	if("second".equals(grade)){//第二等级基础字段量
				sql += "  AND    (T.照片 is not null AND T.照片 != '') AND " +
				"      (T.民族 is not null AND T.民族 != '') AND " +
				"      (T.住房性质 is not null AND T.住房性质 != '') AND " +
				"      (T.职业 is not null AND T.职业 != '') AND" +
				"      (T.客户收入 is not null AND T.客户收入 != '') AND " +
				"      (T.加班 is not null AND T.加班 != '') AND" +
				"      (T.家庭人口数 is not null ) AND " +
				"      (T.学龄前人数 is not null ) AND " +
				"      (T.未成年人数 is not null ) AND " +
				"      (T.宠物类型 is not null AND T.宠物类型 != '')" ;
    	}
				sql += "      THEN 1 ELSE 0 END  ) AS 个数 " +
				" FROM " +
				"( " +
				"  SELECT " +
				"    thc.id AS 客户户型id, " +
				"    tc.id AS 客户id, " +
				"    th.house_type AS 房屋类型, " +
				"    th.building_unit_no AS 单元号, " +
				"    th.commercial_floor_number AS 楼层, " +
				"    th.building_house_no AS 房间号, " +
				"    hs.house_area AS 面积, " +
				"    hs.house_toward AS 朝向, " +
				"    hs.house_style AS 户型, " +
				"    hs.house_pic AS 户型图, " +
				"    tc.`name` AS 姓名, " +
				"    tc.sex AS 性别, " +
				"    tc.age AS 年龄, " +
				"    tc.mobilephone AS 电话, " +
				"    tc.customer_pic AS 照片, " +
				"    tc.nationality AS 民族, " +
				"    tc.house_property 住房性质, " +
				"    tc.job AS 职业, " +
				"    tc.income AS 客户收入, " +
				"    tc.work_overtime AS 加班, " +
				"    tc.family_num AS 家庭人口数, " +
				"    tc.preschool_num AS 学龄前人数, " +
				"    tc.minor_num AS 未成年人数, " +
				"    tc.pet_type AS 宠物类型, " +
				"    u.employeeId AS 员工编码, " +
				"    u.`name` AS 员工姓名, " +
				"    u.store_id AS 门店id, " +
				"    s.`name` AS 门店名称," +
				"   thc.one_pay AS 第一等级支付," +
				"   thc.six_pay AS 第二等级支付," +
				"   thc.third_grade_pay AS 第三等级支付" +
				"  FROM " +
				"    t_customer tc " +
				"  LEFT JOIN (select thc2.id,thc2.house_id,thc2.customer_id,thc2.one_pay,thc2.six_pay,thc2.one_date,thc2.six_date,thc2.third_grade_pay,thc2.third_grade_date from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id)  thc ON tc.id = thc.customer_id " +
				"  LEFT JOIN t_house th ON th.id = thc.house_id " +
				"  LEFT JOIN t_building tb ON tb.id = th.building_id " +
				"  LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id " +
				"  LEFT JOIN t_house_style hs ON hs.house_id = th.id " +
				"  LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no " +
				"  LEFT JOIN t_store s ON s.store_id = u.store_id" +
				"  WHERE " + monthstr +" and u.disabledFlag = 1 and (u.pk_usergroup='3224' OR u.employeeId IN (select employee_no from t_humanresources where zw ='副店长' and remark='国安侠' and store_id="+store_id+"))"+
				"  AND s.`store_id` = "+store_id+" " +
				") T " +
				"WHERE T.员工编码 != '' AND T.员工编码 IS NOT NULL AND T.员工姓名 NOT LIKE '%测试%'" +
				"GROUP BY T.`门店名称` ORDER BY T.门店id";


//        String sql = "SELECT IFNULL(COUNT(1),0) FROM t_customer c LEFT JOIN tb_bizbase_user u ON u.employeeId = c.employee_no " +
//                " WHERE (DATE_FORMAT(c.create_time,'%Y-%m')=DATE_FORMAT("+monthstr+",'%Y-%m') OR DATE_FORMAT(c.update_time,'%Y-%m')=DATE_FORMAT("+monthstr+",'%Y-%m'))" +
//                "   AND u.store_id = '"+store_id+"'";
        System.out.println(sql);
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql);
		List list = query.list();
        return Integer.valueOf(query.uniqueResult() == null?"0":query.uniqueResult().toString());
    }
	
	
	
	public List<?> getCustomerDataByStoreAndMouth(String where,String month,String grade,Long storeId) {
		String monthString=null;
		String target="thc.six_date";
		if("first".equals(grade)){
			target="thc.one_date";
		}else if("second".equals(grade)){
			target="thc.six_date";
		}else if(grade==null||"".equals(grade)){
			grade = "second";
		}
		if( month!=null && !DateUtil.isThisTime(month,"yyyy-MM")){
			monthString=" (DATE_FORMAT("+target+",'%Y-%m') ='"+month+"')";
		}else{
			monthString=" ( DATE_FORMAT("+target+",'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m') OR "+target+" is null)" ;
		}
		String sql="SELECT " +
				"  u.employeeId, " +
				"  u.`name`, " +
				"  u.disabledFlag," +
				"  SUM( " +
				"    CASE WHEN " +
				"      (T.面积 is not null AND T.面积 != '') AND " +
				"      (T.朝向 is not null AND T.朝向 != '') AND " +
				"      (T.户型 is not null AND T.户型 != '') AND " +
				"      ((T.户型图 is not null AND T.户型图 != '') OR T.`房屋类型` = 0) AND " +
				"      (T.姓名 is not null AND T.姓名 != '') AND " +
				"      (T.性别 is not null) AND " +
				"      (T.电话 is not null AND T.电话 != '') AND " +
				"      (T.年龄 is not null ) " ;
				if("second".equals(grade)){
					sql +=  "   AND   (T.照片 is not null AND T.照片 != '') AND " +
							"      (T.民族 is not null AND T.民族 != '') AND " +
							"      (T.住房性质 is not null AND T.住房性质 != '') AND " +
							"      (T.职业 is not null AND T.职业 != '') AND" +
							"      (T.客户收入 is not null AND T.客户收入 != '') AND " +
							"      (T.加班 is not null AND T.加班 != '') AND" +
							"      (T.家庭人口数 is not null ) AND " +
							"      (T.学龄前人数 is not null ) AND " +
							"      (T.未成年人数 is not null ) AND " +
							"      (T.宠物类型 is not null AND T.宠物类型 != '')" ;
				}
				
				sql += "    THEN 1 ELSE 0 END " +
				"  ) AS data_amount " +
				" FROM " +
				"( " +
				"  SELECT " +
				"    thc.id AS 客户户型id, " +
				"    tc.id AS 客户id, " +
				"    th.building_unit_no AS 单元号, " +
				"    th.commercial_floor_number AS 楼层, " +
				"    th.building_house_no AS 房间号, " +
				"    th.house_type AS 房屋类型, " +
				"    hs.house_area AS 面积, " +
				"    hs.house_toward AS 朝向, " +
				"    hs.house_style AS 户型, " +
				"    hs.house_pic AS 户型图, " +
				"    tc.`name` AS 姓名, " +
				"    tc.sex AS 性别, " +
				"    tc.age AS 年龄, " +
				"    tc.mobilephone AS 电话, " +
				"    tc.customer_pic AS 照片, " +
				"    tc.nationality AS 民族, " +
				"    tc.house_property 住房性质, " +
				"    tc.job AS 职业, " +
				"    tc.income AS 客户收入, " +
				"    tc.work_overtime AS 加班, " +
				"    tc.family_num AS 家庭人口数, " +
				"    tc.preschool_num AS 学龄前人数, " +
				"    tc.minor_num AS 未成年人数, " +
				"    tc.pet_type AS 宠物类型, " +
				"    tc.employee_no AS 员工编码, " +
				"    thc.one_pay AS 第一等级支付," +
				"    thc.six_pay AS 第二等级支付" +
				"  FROM " +
				"    t_customer tc" +
				"  LEFT JOIN (select thc2.* from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id) thc ON tc.id = thc.customer_id " +
				"  LEFT JOIN t_house th ON th.id = thc.house_id " +
				"  LEFT JOIN t_building tb ON tb.id = th.building_id " +
				"  LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id " +
				"  LEFT JOIN t_house_style hs ON hs.house_id = th.id " +
				"  LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no " +
				"  LEFT JOIN t_store s ON s.store_id = u.store_id" +
				"  WHERE (" + monthString + ") " +
				"  AND tc.employee_no != '' AND tc.employee_no IS NOT NULL " +
				") T RIGHT JOIN tb_bizbase_user u ON u.employeeId = T.`员工编码` " +
				"WHERE (u.pk_usergroup=3224 or u.employeeId IN (select employee_no from t_humanresources  where zw ='副店长' and remark='国安侠' and store_id="+storeId+")) AND u.name NOT LIKE '%测试%'" + where +
				"GROUP BY u.employeeId,u.`name` HAVING (u.disabledFlag = 1 ) ORDER BY data_amount desc";

		//SQL查询对象
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		//获得查询数据
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
				
		return lst_data;
	}
	
	
	
	@Override
	public List<Map<String, Object>> queryOrderCountOfMonth_CSZJ_QYJL_before(String employeeId, Long cityId, String role) {
		String whereStr="";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select t.store_id,t.platformid from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employeeId+"' and a.id = b.gmid and b.ctid="+cityId+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	 on t.city_name  = t1.cityname";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select store_id,platformid from t_store where rmid = (select id from tb_bizbase_user where employeeId='"+employeeId+"')";
		}
		
		String sql = "select  sum(ifnull(b.order_count,0)) as  ordercount,round(SUM(ifnull(b.order_amount,0)),0) as total_price, DATE_FORMAT(STR_TO_DATE(concat(b.year,'-',b.month),'%Y-%m'), '%c月') AS order_date   from ("+whereStr+") as a left join  ds_storetrade b on a.platformid = b.platformid  group by concat(b.year,'-',b.month) ";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> queryOrderListOfStore_CSZJ_QYJL_before(Long employeeId, Long cityId,
			String role,String q_date) {
		String whereStr="";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select t.store_id,t.platformid,t.name,t.skid,tbu.name as employeeName from t_store t  inner join  (select tdc.id,tdc.cityname from t_dist_city a"+  
					
					"   INNER JOIN t_dist_citycode tdc on a.citycode = tdc.citycode and a.pk_userid="+employeeId+" and tdc.id="+cityId+" ) t1"+
					"	 on t.city_name  = t1.cityname"+
					"   left join tb_bizbase_user as tbu on t.skid = tbu.id";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select t.store_id,t.platformid,t.name,t.skid,tbu.name as employeeName from t_store t left join tb_bizbase_user as tbu on t.skid = tbu.id  where rmid ="+employeeId;
		}
		
		String sql = "select  ifnull(b.order_count,0) as  ordercount,round(ifnull(b.order_amount,0),0) as total_price,a.store_id,a.name,a.skid,a.employeeName   from ("+whereStr+") as a left join  ds_storetrade b on a.platformid = b.platformid  and concat(b.year,'-',b.month)='"+q_date+"' order by ordercount desc limit 20";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
}
