package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.StoreDocumentInfoDao;

public class StoreDocumentInfoDaoImpl extends BaseDAOHibernate implements StoreDocumentInfoDao {

	@Override
	public List<Map<String, Object>> getStoreDocumentInfoData(String where, PageInfo pageInfo) {
		String find_count_sql = "SELECT count(doc.id) FROM t_store_document_info doc LEFT JOIN t_store sto ON doc.store_id=sto.store_id WHERE 1=1"
				+ where;
		String find_data_sql = "SELECT doc.id,sto.city_name,sto.storeno,sto.`name`,doc.enter_date,doc.enter_end_date,doc.submit_date,doc.audit_date,sto.tenancy_term,doc.audit_status,sto.nature FROM t_store_document_info doc LEFT JOIN t_store sto ON doc.store_id=sto.store_id WHERE 1=1 "
				+ where + " order by doc.id desc";
		// SQL查询对象
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(find_data_sql.toString());

		// 查询数据量对象
		SQLQuery countQuery = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(find_count_sql);
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
	public List<Map<String, Object>> exportStoreDetail(String where) {
		String find_data_sql = "SELECT couu.`name` as countyname,see.`name` as town_name,IFNULL(stt.address,stt.detail_address) as address,stt.rent_area,stt.agency_fee,stt.increase,stt.increase_fee"
				+ ",stt.rent_free,stt.payment_method,stt.rental,stt.taxes,stt.tenancy_term,stt.nature,stt.superMicro,tt.submit_date,tt.audit_date,tt.enter_date,tt.enter_end_date,tt.card_content"
				+ ",stt.estate,DATE_FORMAT(stt.open_shop_time, '%Y/%c/%e') AS open_shop_time,stt.`name`,cc.name as ename,cc.phone"
				+ "	FROM " + "	t_store_document_info tt " + "	LEFT JOIN t_store stt ON tt.store_id = stt.store_id "
				+ "	LEFT JOIN (SELECT * FROM tb_bizbase_user WHERE zw='店长') cc ON cc.id=stt.skid "
				+ "	LEFT JOIN t_town see ON see.id in (stt.town_id) "
				+ "	LEFT JOIN t_county couu on couu.id=see.county_id " + "WHERE " + " tt.audit_status=3 " + where;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(find_data_sql);
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
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

}
