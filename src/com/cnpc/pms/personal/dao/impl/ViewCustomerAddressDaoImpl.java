package com.cnpc.pms.personal.dao.impl;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.DateUtil;
import com.cnpc.pms.personal.dao.ViewCustomerAddressDao;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewCustomerAddressDaoImpl extends BaseDAOHibernate implements ViewCustomerAddressDao {

	@Override
	public List<Map<String, Object>> getViewCustomerAddressList(String where, PageInfo pageInfo) {
		//sql查询列，用于分页计算数据总数
        String str_count_sql = "SELECT count(*) FROM view_customer_of_address WHERE 1=1 "+where;
        System.out.println(str_count_sql);
        //sql查询列，用于页面展示所有的数据
        String find_sql="select * from  view_customer_of_address WHERE 1=1 ";
        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(find_sql);
        sb_sql.append(where);
        //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sb_sql.toString());

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
	public List<Map<String, Object>> getViewCustomerAddressListData(String where, String month, String complete_status,PageInfo pageInfo,String grade) {

		String str_where_month = null;
		String target = "thc.six_date";
		if("first".equals(grade)){
			target = "thc.one_date";
		}else if("second".equals(grade)){
			target = "thc.six_date";
		}
		
		if("1".equals(complete_status)){//是否完成基础字段 1：未完成 0：完成 (18个字段)
			str_where_month = " and DATE_FORMAT(tc.update_time,'%Y-%m') ='"+month+"' ";
		}else if (DateUtil.isThisTime(month,"yyyy-MM")){//查询月是当前月
			str_where_month = " and (DATE_FORMAT("+target+",'%Y-%m') ='"+month+"' or "+target+" is null) ";
		}else{//其他
			str_where_month = " and DATE_FORMAT("+target+",'%Y-%m') ='"+month+"' ";
		}

		String find_sql="SELECT "+
				"	thc.id AS 客户户型id, "+
				"	tc.id AS id, "+
				"	th.house_type AS 房屋类型, "+
				"	th.id AS house_id, "+
				"	thc.one_date, "+
				"	th.building_unit_no AS unit_no, "+
				"	th.commercial_floor_number AS 楼层, "+
				"	th.building_house_no AS house_no, "+
				"	hs.house_area AS house_area, "+
				"	hs.house_toward AS house_toward, "+
				"	hs.house_style AS house_style, "+
				"	(CASE WHEN ( "+
				"			isnull(hs.house_pic)  "+
				"			OR (trim(hs.house_pic) = '') "+
				"			OR (trim(hs.house_pic) = '无') "+
				"		) THEN "+
				"			'无' "+
				"		ELSE "+
				"			'有' "+
				"		END "+
				"	) AS is_pic, "+
				"	tc.`name` AS uname, "+
				"	CASE "+
			"	WHEN tc.sex = 0 THEN "+
			"		'男' "+
			"	ELSE "+
			"		'女' "+
			"	END sex, "+
			"	tc.age AS age, "+
			"	tc.mobilephone AS mobilephone, "+
			"	tc.customer_pic AS 照片, "+
			"	tc.nationality AS 民族, "+
			"	tc.house_property 住房性质, "+
			"	tc.job AS 职业, "+
			"	CASE "+
			"	WHEN (th.`house_type` = 0) THEN "+
			"		concat( "+
			"			ttv.`name`, "+
			"			'街', "+
			"			ifnull(th.`house_no`,'--'), "+
			"			'号' "+
			"		) "+
			"	ELSE "+
			"		concat( "+
			"			ttv.`name`, "+
			"			tb.`name`, "+
			"			'号楼', "+
			"			th.`building_unit_no`, "+
			"			'单元', "+
			"			th.`building_house_no`, "+
			"			'号' "+
			"		) "+
			"	END AS `address`, "+
			"	tc.income AS 客户收入, "+
			"	tc.work_overtime AS 加班, "+
			"	tc.family_num AS 家庭人口数, "+
			"	tc.preschool_num AS 学龄前人数, "+
			"	tc.minor_num AS 未成年人数, "+
			"	tc.pet_type AS 宠物类型, "+
			"	u.employeeId AS employeeId, "+
			"	u.`name` as 国安侠, "+
			"	tc.`name`, "+
			"	u.store_id AS store_id, "+
			"	s.`name` AS 门店名称, "+
			"	thc.one_pay AS 第一等级支付, "+
			"	thc.six_pay AS 第二等级支付, "+
			"	thc.third_grade_pay AS 第三等级支付, "+
			"	tc.create_time, "+
			"	tc.update_time "+
		"	FROM "+
			"	t_customer tc "+
		"	LEFT JOIN (select thc2.* from (select max(id) as id from t_house_customer GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 on thc1.id=thc2.id) thc ON tc.id = thc.customer_id "+
		"	LEFT JOIN t_house th ON th.id = thc.house_id "+
		"	LEFT JOIN t_building tb ON tb.id = th.building_id "+
		"	LEFT JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id "+
		"	LEFT JOIN t_house_style hs ON hs.house_id = th.id "+
		"	LEFT JOIN tb_bizbase_user u ON u.employeeId = tc.employee_no "+
		"	LEFT JOIN t_store s ON s.store_id = u.store_id "+
		"	WHERE 1=1   and u.name NOT LIKE '%测试%'" + str_where_month;
		//"	WHERE 1=1  and u.pk_usergroup=3224 and u.name NOT LIKE '%测试%'" + str_where_month;
		if("0".equals(complete_status)){//完成
			find_sql += "	AND (hs.house_area IS NOT NULL AND hs.house_area != '') "+
					"		AND (hs.house_toward IS NOT NULL AND hs.house_toward != '') "+
					"		AND (hs.house_style IS NOT NULL AND hs.house_style != '') "+
					"		AND (hs.house_pic IS NOT NULL AND (trim(hs.house_pic) != '') AND (trim(hs.house_pic) != '无') OR th.house_type = 0) "+
					"		AND (tc.`name` IS NOT NULL AND tc.`name` != '') AND tc.sex IS NOT NULL "+
					"		AND (tc.mobilephone IS NOT NULL AND tc.mobilephone != '') "+
					"		AND (tc.age IS NOT NULL )"+
					"       AND (tc.sex is not null)";
					if("second".equals(grade)){
				        find_sql+= "     AND (tc.customer_pic is not null AND tc.customer_pic != '')"
						+ "		AND (tc.nationality is not null AND tc.nationality != '')"
						+ "		AND (tc.house_property is not null AND tc.house_property != '')"
						+ "		AND (tc.income is not null AND tc.income != '')"
						+ "		AND (tc.work_overtime is not null AND tc.work_overtime != '')"
						+ "     AND (tc.family_num is not null )"
						+ "		AND (tc.preschool_num is not null )"
						+ "		AND (tc.minor_num is not null )"
						+ "		AND (tc.pet_type is not null AND tc.pet_type != '')";
					}
					
					
		} else {//未完成
			String tempSql = "";
			tempSql = "	(hs.house_area IS NOT NULL AND hs.house_area != '') "+
					"		AND (hs.house_toward IS NOT NULL AND hs.house_toward != '') "+
					"		AND (hs.house_style IS NOT NULL AND hs.house_style != '') "+
					"		AND (hs.house_pic IS NOT NULL AND (trim(hs.house_pic) != '') AND (trim(hs.house_pic) != '无') OR th.house_type = 0) "+
					"		AND (tc.`name` IS NOT NULL AND tc.`name` != '') AND tc.sex IS NOT NULL "+
					"		AND (tc.mobilephone IS NOT NULL AND tc.mobilephone != '') "+
					"		AND (tc.age IS NOT NULL )" +
					"       AND (tc.sex is not null)";
					if("second".equals(grade)){
						tempSql += "   AND (tc.customer_pic is not null AND tc.customer_pic != '')"
								+ "		AND (tc.nationality is not null AND tc.nationality != '')"
								+ "		AND (tc.house_property is not null AND tc.house_property != '')"
								+ "		AND (tc.income is not null AND tc.income != '')"
								+ "		AND (tc.work_overtime is not null AND tc.work_overtime != '')"
								+ "     AND (tc.family_num is not null )"
								+ "		AND (tc.preschool_num is not null )"
								+ "		AND (tc.minor_num is not null )"
								+ "		AND (tc.pet_type is not null AND tc.pet_type != '')";
					}
					
					find_sql += "AND !("+tempSql+")";
		}

		find_sql+=where;
	    
		String str_count_sql = "Select count(1) from ( "+find_sql+" ) mmm ";
     /*   StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(find_sql);
        sb_sql.append(where);
    	System.out.println("----------------------------");
		System.out.println(str_count_sql);
		System.out.println("--------------------------------");
		System.out.println(sb_sql.toString());
		System.out.println("--------------------------------");*/
        
        //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(find_sql);

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

	

}
