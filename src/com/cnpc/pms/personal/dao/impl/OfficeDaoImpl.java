package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.OfficeDao;

public class OfficeDaoImpl extends BaseDAOHibernate implements OfficeDao {

	@Override
	public List<Map<String, Object>> getOfficeList(String where, PageInfo pageInfo) {
		//sql查询列，用于分页计算数据总数
        String str_count_sql = "select COUNT(1) "+
        		"FROM "+
        		"t_office_info off "+
        	"LEFT JOIN t_company_info com ON off.office_id = com.office_id "+
        	"LEFT JOIN t_town town ON off.town_id = town.id "+
        	"LEFT JOIN t_county coun ON town.county_id=coun.id "+
        	"LEFT JOIN t_city city ON coun.city_id=city.id "+
        	"LEFT JOIN tb_bizbase_user user ON off.employee_no=user.employeeId "+
        	"LEFT JOIN t_store store ON user.store_id=store.store_id "+
        	"LEFT JOIN t_province pro ON city.province_id=pro.id WHERE 1=1 "+where;
        System.out.println(str_count_sql);
        //sql查询列，用于页面展示所有的数据
    /*    String find_sql="SELECT off.office_id, "+
	"pro.`name` as province_name, "+
	"town.`name` as town_name, "+
	"concat_ws(' ',pro.`name`,town.`name`)  as whold_address,"+
	"user.`name` as user_name,"+
	"store.`name` as store_name,"+
	"off.is_check,"+
	"off.office_name, "+
	"off.office_type, "+
	"off.office_area, "+
	"com.office_floor_of_company, "+
	"com.office_company "+
"FROM "+
	"t_office_info off "+
"LEFT JOIN t_company_info com ON off.office_id = com.office_id "+
"LEFT JOIN t_town town ON off.town_id = town.id "+
"LEFT JOIN t_county coun ON town.county_id=coun.id "+
"LEFT JOIN t_city city ON coun.city_id=city.id "+
"LEFT JOIN tb_bizbase_user user ON off.employee_no=user.employeeId "+
"LEFT JOIN t_store store ON user.store_id=store.store_id "+
"LEFT JOIN t_province pro ON city.province_id=pro.id WHERE 1=1 ";*/
        String find_sql="SELECT off.office_id, "+
        		"pro.`name` as province_name, "+
        		"town.`name` as town_name, "+
        		"off.office_address  as whold_address,"+
        		"user.`name` as user_name,"+
        		"store.`name` as store_name,"+
        		"off.is_check,"+
        		"off.office_name, "+
        		"off.office_type, "+
        		"off.office_area, "+
        		"com.office_floor_of_company, "+
        		"com.office_company "+
        	"FROM "+
        		"t_office_info off "+
        	"LEFT JOIN t_company_info com ON off.office_id = com.office_id "+
        	"LEFT JOIN t_town town ON off.town_id = town.id "+
        	"LEFT JOIN t_county coun ON town.county_id=coun.id "+
        	"LEFT JOIN t_city city ON coun.city_id=city.id "+
        	"LEFT JOIN tb_bizbase_user user ON off.employee_no=user.employeeId "+
        	"LEFT JOIN t_store store ON user.store_id=store.store_id "+
        	"LEFT JOIN t_province pro ON city.province_id=pro.id WHERE 1=1 ";
        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(find_sql);
        sb_sql.append(where +" order by off.office_id desc");
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
	
	/**
	 * 查询打印的列表
	 */
	@Override
	public List<Map<String, Object>> getPrintOfficeList(String where, Integer limitCount) {
        String find_sql="SELECT off.office_id, "+
        		"off.office_name, "+
        		"off.office_address, "+
        		"off.office_area, "+
        		"off.office_parking, "+
        		"off.office_floor, "+
        		"off.office_type, "+
        		"town.`id` as town_id, "+
        		"town.`name` as town_name, "+
        		"com.`company_id` as company_id, "+
        		"com.`office_company` as company_name, "+
        		"com.`office_floor_of_company` as company_floor, "+
        		"village.`id` as village_id, "+
        		"village.`name` as village_name"+
			" FROM "+
				"t_office_info off "+
			"LEFT JOIN t_company_info com ON off.office_id = com.office_id "+
			"LEFT JOIN t_town town ON off.town_id = town.id "+
			"LEFT JOIN tb_bizbase_user user ON off.employee_no=user.employeeId "+
			"LEFT JOIN t_store store ON user.store_id=store.store_id "+
			"LEFT JOIN t_county coun ON town.county_id=coun.id "+
		    "LEFT JOIN t_city city ON coun.city_id=city.id "+
		    "LEFT JOIN t_province pro ON city.province_id=pro.id "+
			"LEFT JOIN t_village village ON off.office_id=village.id WHERE  ";
        
        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(find_sql);
        sb_sql.append(where);
        sb_sql.append(" order by off.office_id desc");
        //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sb_sql.toString());
        //获得查询数据
        List<?> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(0)
                .setMaxResults(limitCount).list();

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
	 * 查询打印的列表
	 */
	@Override
	public List<Map<String, Object>> getPrintWholeOfficeList(String where, Integer limitCount) {
        String find_sql="SELECT off.office_id, "+
        		"pro.`name` as province_name,"+
        		"city.`name` as city_name,"+
        		"county.`name` as county_name,"+
        		"town.`name` as town_name, "+
        		"village.`name` as village_name,"+
        		"village.`gb_code` as gb_code,"+
        		"off.office_address,"+
        		"off.office_name, "+
        		"off.office_type, "+
        		"off.office_area, "+
        		"off.office_floor, "+
        		"off.office_parking, "+
        		"com.`office_company` as company_name, "+
        		"com.`office_floor_of_company` as company_floor, "+
        		"store.`name` as store_name, "+
        		"user.`name` as user_name"+
			" FROM "+
				"t_office_info off "+
			"LEFT JOIN t_company_info com ON off.office_id = com.office_id "+
			"LEFT JOIN t_town town ON off.town_id = town.id "+
			"LEFT JOIN tb_bizbase_user user ON off.employee_no=user.employeeId "+
			"LEFT JOIN t_store store ON user.store_id=store.store_id "+
			"LEFT JOIN t_county county ON town.county_id=county.id "+
		    "LEFT JOIN t_city city ON county.city_id=city.id "+
		    "LEFT JOIN t_province pro ON city.province_id=pro.id "+
			"LEFT JOIN t_village village ON off.office_id=village.id WHERE  ";
        
        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(find_sql);
        sb_sql.append(where);
        sb_sql.append(" order by off.office_id asc");
        //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sb_sql.toString());
        //获得查询数据
        List<?> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(0)
                .setMaxResults(limitCount).list();

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
