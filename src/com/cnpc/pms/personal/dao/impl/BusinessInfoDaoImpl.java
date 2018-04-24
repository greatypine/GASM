package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.BusinessInfoDao;

public class BusinessInfoDaoImpl extends BaseDAOHibernate implements BusinessInfoDao{

	@Override
	public List<Map<String, Object>> getBusinessInfoList(String where, PageInfo pageInfo) {
		//sql查询列，用于分页计算数据总数
        String str_count_sql = "select count(bus.id) "+
        		"from t_town_business_info bus "+
        		"LEFT JOIN t_village vill ON bus.village_id=vill.id "+
        		"LEFT JOIN t_town town ON bus.town_id = town.id "+
        		"LEFT JOIN t_county coun ON town.county_id=coun.id "+
        		"LEFT JOIN t_city city ON coun.city_id=city.id "+
        		"LEFT JOIN tb_bizbase_user user ON bus.employee_no=user.employeeId  "+
        		"LEFT JOIN t_store store ON user.store_id=store.store_id  "+
        		"LEFT JOIN t_province pro ON city.province_id=pro.id WHERE 1=1 and bus.status=0 "+where;
        System.out.println(str_count_sql);
        //sql查询列，用于页面展示所有的数据
        String find_sql="SELECT bus.id,pro.`name` as province_name, "+
	"town.`name` as town_name, "+
	"vill.`name` as village_name, "+
	"bus.business_name, "+
	"bus.two_level_index, "+
	"bus.three_level_index, "+
	"bus.four_level_index, "+
	"bus.five_level_index, "+
	"user.`name` as employee_name,"+
	"store.`name` as store_name,"+
	"bus.shop_area "+
 "from t_town_business_info bus "+
"LEFT JOIN t_village vill ON bus.village_id=vill.id "+
"LEFT JOIN t_town town ON bus.town_id = town.id "+
"LEFT JOIN t_county coun ON town.county_id=coun.id "+
"LEFT JOIN t_city city ON coun.city_id=city.id "+
"LEFT JOIN tb_bizbase_user user ON bus.employee_no=user.employeeId  "+
"LEFT JOIN t_store store ON user.store_id=store.store_id  "+
"LEFT JOIN t_province pro ON city.province_id=pro.id WHERE 1=1 and bus.status=0 ";
        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(find_sql);
        sb_sql.append(where +"  order by bus.id desc");
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
	public List<Map<String, Object>> getBussinessInfo(String where) {
		String find_sql="SELECT bus.id,pro.`name` as province_name, "+
				"CASE WHEN city.`name`='市辖区' THEN pro.`name` ELSE city.`name` end  as city_name, "+
				"coun.`name` as county_name, "+
	"town.`name` as town_name,  "+
	"vill.`name` as village_name,  "+
	"vill.gb_code as village_gb_code, "+
	"bus.address, "+
	"bus.business_name,  "+
	"bus.two_level_index,  "+
	"bus.three_level_index,  "+
	"bus.four_level_index,  "+
	"bus.five_level_index,  "+
	"bus.shop_area, "+
	"bus.range_eshop, "+
	"bus.isdistribution, "+
	"bus.start_business_house, "+
	"user.`name` as employee_name,"+
	"store.`name` as store_name,"+
	"bus.end_business_house "+
 "from t_town_business_info bus  "+
"LEFT JOIN t_village vill ON bus.village_id=vill.id  "+
"LEFT JOIN t_town town ON vill.town_id = town.id  "+
"LEFT JOIN t_county coun ON town.county_id=coun.id  "+
"LEFT JOIN t_city city ON coun.city_id=city.id  "+
"LEFT JOIN t_province pro ON city.province_id=pro.id "+
"LEFT JOIN tb_bizbase_user user ON bus.employee_no=user.employeeId  "+
"LEFT JOIN t_store store ON store.store_id=user.store_id  "+
"WHERE 1=1 " + where;
		 SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession().createSQLQuery(find_sql);
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
	public int updateData(String sql){
		 SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession().createSQLQuery(sql);
		 int executeUpdate = query.executeUpdate();
		
		return executeUpdate;
	}

}
