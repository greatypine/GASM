package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.CountyDao;

public class CountyDaoImpl extends BaseDAOHibernate implements CountyDao {

	@Override
	public List<Map<String, Object>> getCountyListByCityName(String where, PageInfo pageInfo) {
		// sql查询列，用于分页计算数据总数
		String str_count_sql = "SELECT count(1) from t_county county LEFT JOIN t_city city ON county.city_id=city.id WHERE 1=1 "
				+ where;
		System.out.println(str_count_sql);
		// sql查询列，用于页面展示所有的数据
		String find_sql = "SELECT county.id,county.`name` from t_county county LEFT JOIN t_city city ON county.city_id=city.id WHERE 1=1 ";
		StringBuilder sb_sql = new StringBuilder();
		sb_sql.append(find_sql);
		sb_sql.append(where + " order by county.sort desc");
		// SQL查询对象
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sb_sql.toString());

		// 查询数据量对象
		SQLQuery countQuery = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(str_count_sql);
		pageInfo.setTotalRecords(Integer.valueOf(countQuery.list().get(0).toString()));
		// 获得查询数据
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();

		List<Map<String, Object>> lst_result = new ArrayList<Map<String, Object>>();

		// 如果没有数据返回
		if (lst_data == null || lst_data.size() == 0) {
			return lst_result;
		}
		// 转换成需要的数据形式
		for (Object obj_data : lst_data) {
			lst_result.add((Map<String, Object>) obj_data);
		}
		return lst_result;
	}

	@Override
	public void updateCountySortByIds(String idString) {
		deleteCountySortByIds(idString);
		String sb_sql = "update t_county SET sort=1 where id in (" + idString + ")";// 清空的sql
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sb_sql.toString());
		query.executeUpdate();
	}

	@Override
	public void deleteCountySortByIds(String ids) {
		String sb_sql = "update t_county SET sort=NULL";// 清空的sql
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sb_sql.toString());
		query.executeUpdate();

	}

	@Override
	public Integer getCountyCountByCityId(Long city_id) {
		String sql = "SELECT count(*) FROM t_county WHERE t_county.city_id in(SELECT t_city.id FROM t_city WHERE t_city.name like "
				+"CONCAT('%',(SELECT t_dist_citycode.cityname FROM t_dist_citycode WHERE t_dist_citycode.id="+city_id+"),'%'))";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(sql);
		 List list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 return Integer.parseInt(list.get(0)+"");
		 }
		return null;
	}

	@Override
	public Integer getConCountyCountByCityId(Long city_id) {
		
		String sql = "select COUNT(1) from (select county_id from t_store ts inner join t_dist_citycode tdc ON ts.city_name=tdc.cityname WHERE tdc.id ="+city_id +" and county_id is not null and ts.flag=0 and ifnull(ts.estate,'')!='闭店中' GROUP BY county_id) t";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(sql);
		 List list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 return Integer.parseInt(list.get(0)+"");
		 }
		return null;
	}
	
	

}
