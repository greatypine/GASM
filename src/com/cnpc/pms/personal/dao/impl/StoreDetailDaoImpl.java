package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.StoreDetailDao;

public class StoreDetailDaoImpl extends BaseDAOHibernate implements StoreDetailDao{

	@Override
	public List<Map<String, Object>> getStoreDetailData(String where, PageInfo pageInfo) {
		String find_count_sql="SELECT count(tt.id) FROM t_store_detail tt LEFT JOIN t_store stt ON tt.store_id=stt.store_id where 1=1 "+where;
		String find_data_sql="SELECT tt.id,stt.storeno,stt.`name`,tt.store_area,tt.sign_date,tt.enter_date,tt.accept_date,tt.business_license_date,tt.food_circulation_date,stt.open_shop_time,stt.estate FROM t_store_detail tt LEFT JOIN t_store stt ON tt.store_id=stt.store_id where 1=1 "+where+" order by tt.id desc";
		 //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(find_data_sql.toString());

        //查询数据量对象
        SQLQuery countQuery = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(find_count_sql);
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
	public List<Map<String, Object>> exportStoreDetail(String where) {
		String find_data_sql="SELECT stt.`name`,tt.ownership,IFNULL(stt.address,stt.detail_address) as address,tt.store_area,ROUND(tt.unit_price,2) as unit_price,tt.agency_fee,"+
				"case WHEN tt.rent_free_start_data is NULL THEN 0 ELSE CONCAT(DATE_FORMAT(tt.rent_free_start_data,'%Y/%c/%e'),'-',DATE_FORMAT(tt.rent_free_end_data, '%Y/%c/%e')) END as rent_free_data,"+
				"tt.payment_method,DATE_FORMAT(tt.sign_date, '%Y/%c/%e') AS sign_date,stt.storetypename,stt.superMicro,"+
				"DATE_FORMAT(tt.submit_date, '%Y/%c/%e') AS submit_date,"+
				"DATE_FORMAT(tt.audit_date, '%Y/%c/%e') AS audit_date,"+
				"DATE_FORMAT(tt.enter_date, '%Y/%c/%e') AS enter_date,"+
				"DATE_FORMAT(tt.place_date, '%Y/%c/%e') AS place_date,"+
				"DATE_FORMAT(tt.prop_date, '%Y/%c/%e') AS prop_date,"+
				"DATE_FORMAT(tt.accept_date, '%Y/%c/%e') AS accept_date,"+
				"DATE_FORMAT(tt.business_license_date, '%Y/%c/%e') AS business_license_date,"+
				"DATE_FORMAT(tt.food_circulation_date, '%Y/%c/%e') AS food_circulation_date,"+
				"DATE_FORMAT(tt.gather_start_date, '%Y/%c/%e') AS gather_start_date,"+
				"DATE_FORMAT(tt.gather_end_date, '%Y/%c/%e') AS gather_end_date,"+
				"DATE_FORMAT(stt.open_shop_time, '%Y/%c/%e') AS open_shop_time,"+
				"DATE_FORMAT(tt.line_date, '%Y/%c/%e') AS line_date,"+
				"stt.estate,see.`name` as town_name,cc.name as ename,cc.phone,couu.`name` as countyname "+
				"FROM "+
				"t_store_detail tt "+
				"LEFT JOIN t_store stt ON tt.store_id = stt.store_id "+
				"LEFT JOIN (SELECT * FROM tb_bizbase_user WHERE zw='店长') cc ON cc.id=stt.skid "+
				"LEFT JOIN t_town see ON see.id in (stt.town_id) "+
				"LEFT JOIN t_county couu on couu.id=see.county_id "+
				"WHERE "+
	"1=1 "+where;
		 SQLQuery query = getHibernateTemplate().getSessionFactory()
	                .getCurrentSession().createSQLQuery(find_data_sql);
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

}
