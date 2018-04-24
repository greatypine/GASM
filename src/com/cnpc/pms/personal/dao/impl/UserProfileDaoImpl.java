package com.cnpc.pms.personal.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.UserProfileDao;
import com.cnpc.pms.personal.dto.UserProfileDto;

/**
 * @Function：用户档案Dao实现
 * @author：chenchuang
 * @date:2018年3月8日 下午3:31:31
 *
 * @version V1.0
 */
public class UserProfileDaoImpl extends BaseDAOHibernate implements UserProfileDao {

	@Override
	public Map<String, Object> queryUserProfile(UserProfileDto userProfile, PageInfo pageInfo) {
		String sql = "SELECT IFNULL(dup.customer_name,'未填写') as customer_name, dup.customer_phone, IFNULL(dup.trading_price_sum,0) as trading_price_sum,IFNULL(dup.order_count,0) as order_count,"
				+ "dup.first_order_time, dup.last_order_time, IFNULL(dup.area_code,'') as area_code, IFNULL(dup.tiny_village_code,'') as tiny_village_code, "
				+ "dup.regist_time,DATEDIFF(now(), dup.last_order_time) as slient_time,CASE WHEN dup.user_model='1' THEN '有'  ELSE '无' END AS user_model,"
				+ "CASE WHEN tag.usertag='B' THEN '集采用户' ELSE '' END usertag,IFNULL(dup.trading_price_max,0) as trading_price_max FROM df_user_profile dup "
				+ "LEFT JOIN df_userprofile_tag tag ON dup.customer_id=tag.customer_id where 1=1 ";
		
		if(StringUtils.isNotEmpty(userProfile.getCustomer_name())){
			sql=sql+" AND dup.customer_name ='"+userProfile.getCustomer_name()+"'";
		}
		if(StringUtils.isNotEmpty(userProfile.getCustomer_phone())){
			sql=sql+" AND dup.customer_phone ='"+userProfile.getCustomer_phone()+"'";
		}
		if(StringUtils.isNotEmpty(userProfile.getRegist_time_begin())){
			sql = sql + " AND (dup.regist_time between '" + userProfile.getRegist_time_begin() + " 00:00:00' and '"
					+ userProfile.getRegist_time_end() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(userProfile.getFirst_order_time_begin())){
			sql = sql + " AND (dup.first_order_time between '" + userProfile.getFirst_order_time_begin() + " 00:00:00' and '"
					+ userProfile.getFirst_order_time_end() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(userProfile.getLast_order_time_begin())){
			sql = sql + " AND (dup.last_order_time between '" + userProfile.getLast_order_time_begin() + " 00:00:00' and '"
					+ userProfile.getLast_order_time_end() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(userProfile.getTrading_price_min())){
			sql=sql+" AND dup.trading_price_sum >="+userProfile.getTrading_price_min();
		}
		if(StringUtils.isNotEmpty(userProfile.getTrading_price_max())){
			sql=sql+" AND dup.trading_price_sum <="+userProfile.getTrading_price_max();
		}
		if(StringUtils.isNotEmpty(userProfile.getOrder_count_min())){
			sql=sql+" AND dup.order_count >="+userProfile.getOrder_count_min();
		}
		if(StringUtils.isNotEmpty(userProfile.getOrder_count_max())){
			sql=sql+" AND dup.order_count <="+userProfile.getOrder_count_max();
		}
		if(StringUtils.isNotEmpty(userProfile.getUser_model())){
			sql=sql+" AND dup.user_model >="+userProfile.getUser_model();
		}
		if(StringUtils.isNotEmpty(userProfile.getSlient_time_min()) && "B".equals(userProfile.getSlient_time_min())){
			sql=sql+" AND tag.usertag ='"+userProfile.getSlient_time_min()+"'";
		}
		if(StringUtils.isNotEmpty(userProfile.getSlient_time_min()) || StringUtils.isNotEmpty(userProfile.getSlient_time_max())){
			sql=sql+" HAVING ";
			if(StringUtils.isNotEmpty(userProfile.getSlient_time_min()) && !"B".equals(userProfile.getSlient_time_min())){
				sql=sql+" slient_time >= " + userProfile.getSlient_time_min() ;
			}else{
				sql=sql+" slient_time >= 0" ;
			}
			if(StringUtils.isNotEmpty(userProfile.getSlient_time_max())){
				sql=sql+" AND slient_time <= " + userProfile.getSlient_time_max() ;
			}
		}
		sql = sql + " ORDER BY dup.last_order_time desc ";
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

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> exportUserProfile(UserProfileDto userProfile){
		String sql = "SELECT IFNULL(dup.customer_name,'未填写') as customer_name, dup.customer_phone, IFNULL(dup.trading_price_sum,0) as trading_price_sum,IFNULL(dup.order_count,0) as order_count,"
				+ "dup.first_order_time, dup.last_order_time, IFNULL(dup.area_code,'') as area_code, IFNULL(dup.tiny_village_code,'') as tiny_village_code, "
				+ "dup.regist_time,DATEDIFF(now(), dup.last_order_time) as slient_time,CASE WHEN dup.user_model='1' THEN '有'  ELSE '无' END AS user_model,"
				+ "CASE WHEN tag.usertag='B' THEN '集采用户' ELSE '' END usertag FROM df_user_profile dup LEFT JOIN df_userprofile_tag tag ON dup.customer_id=tag.customer_id where 1=1 ";
		
		if(StringUtils.isNotEmpty(userProfile.getCustomer_name())){
			sql=sql+" AND dup.customer_name ='"+userProfile.getCustomer_name()+"'";
		}
		if(StringUtils.isNotEmpty(userProfile.getCustomer_phone())){
			sql=sql+" AND dup.customer_phone ='"+userProfile.getCustomer_phone()+"'";
		}
		if(StringUtils.isNotEmpty(userProfile.getRegist_time_begin())){
			sql = sql + " AND (dup.regist_time between '" + userProfile.getRegist_time_begin() + " 00:00:00' and '"
					+ userProfile.getRegist_time_end() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(userProfile.getFirst_order_time_begin())){
			sql = sql + " AND (dup.first_order_time between '" + userProfile.getFirst_order_time_begin() + " 00:00:00' and '"
					+ userProfile.getFirst_order_time_end() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(userProfile.getLast_order_time_begin())){
			sql = sql + " AND (dup.last_order_time between '" + userProfile.getLast_order_time_begin() + " 00:00:00' and '"
					+ userProfile.getLast_order_time_end() + " 23:59:59')";
		}
		if(StringUtils.isNotEmpty(userProfile.getTrading_price_min())){
			sql=sql+" AND dup.trading_price_sum >="+userProfile.getTrading_price_min();
		}
		if(StringUtils.isNotEmpty(userProfile.getTrading_price_max())){
			sql=sql+" AND dup.trading_price_sum <="+userProfile.getTrading_price_max();
		}
		if(StringUtils.isNotEmpty(userProfile.getOrder_count_min())){
			sql=sql+" AND dup.order_count >="+userProfile.getOrder_count_min();
		}
		if(StringUtils.isNotEmpty(userProfile.getOrder_count_max())){
			sql=sql+" AND dup.order_count <="+userProfile.getOrder_count_max();
		}
		if(StringUtils.isNotEmpty(userProfile.getUser_model())){
			sql=sql+" AND dup.user_model >="+userProfile.getUser_model();
		}
		if(StringUtils.isNotEmpty(userProfile.getSlient_time_min()) || StringUtils.isNotEmpty(userProfile.getSlient_time_max())){
			sql=sql+" HAVING ";
			if(StringUtils.isNotEmpty(userProfile.getSlient_time_min())){
				sql=sql+" slient_time >= " + userProfile.getSlient_time_min() ;
			}
			if(StringUtils.isNotEmpty(userProfile.getSlient_time_max())){
				sql=sql+" AND slient_time <= " + userProfile.getSlient_time_max() ;
			}
		}
		sql = sql + " ORDER BY dup.last_order_time desc ";

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
}
