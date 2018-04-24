package com.cnpc.pms.personal.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.personal.dao.TinyAreaDao;
import com.cnpc.pms.personal.dao.TinyVillageDao;
import com.cnpc.pms.personal.entity.TinyArea;

public class TinyAreaDaoImpl extends BaseDAOHibernate implements TinyAreaDao {

	@Override
	public TinyArea saveTinyArea(TinyArea tinyArea) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(date);
		String addStoreSql = "INSERT INTO tiny_area (`code`,`version`,`create_time`,`create_user_id`,`name`,`tiny_village_id`,`update_time`, `update_user_id`) VALUES ('"
				+ tinyArea.getCode() + "','0','" + format + "'," + tinyArea.getCreate_user_id() + ",'"
				+ tinyArea.getName().replace("'", "''") + "'," + tinyArea.getTiny_village_id() + ",'" + format + "',"
				+ tinyArea.getUpdate_user_id() + ")";
		System.out.println(addStoreSql);
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(addStoreSql);
		int update = query.executeUpdate();
		return tinyArea;
	}

	@Override
	public Integer findMaxTinyArea(String town_gb_code) {
		String Sql = "SELECT MAX(RIGHT(code,10)) FROM tiny_area  WHERE LEFT(code,12)='" + town_gb_code + "'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(Sql);
		List list = query.list();
		Object object = list.get(0);
		return object == null ? null : Integer.parseInt(object + "");
	}

	@Override
	public List<Map<String, Object>> selectinyAreaByTinyVillageId(Long id) {
		String sql = "SELECT id,code,tiny_village_id FROM tiny_area  WHERE status=0 and tiny_village_id=" + id;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public void updateVallageAreaByCode(String code, String area) {
		String update_sql = "update tiny_area set vallage_area = " + area + " where code = '" + code
				+ "' and status = 0";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(update_sql);
		int update = query.executeUpdate();
	}

	@Override
	public List<Map<String, Object>> selectinyAreaWithoutArea() {
		String sql = "select code from tiny_area where status = 0 and vallage_area is null";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public Integer selectVillageCountByStore(String storeno) {
		String sql = "select count(*) from tiny_area where store_no = '" + storeno + "' and status = 0";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public Map<String, Object> findTinyVillageByStoreNo(String storeName, String city_name, String where,
			PageInfo pageInfo) {
		int total_pages = 0;
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		Map<String, Object> map_result = new HashMap<String, Object>();
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultData = new ArrayList<Map<String, Object>>();
		String sql = "";
		if (city_name != null && city_name.length() > 0) {
			sql = "SELECT ifnull(area.vallage_area,0),area.tiny_village_id,store.`name` as store_name FROM tiny_area area LEFT JOIN t_store store ON area.store_no=store.storeno where store.name='"
					+ storeName + "' and store.city_name='" + city_name
					+ "'  and area.status=0 and IFNULL(store.estate,'')!='闭店中' AND store.flag=0";
		} else {
			sql = "SELECT area.vallage_area,area.tiny_village_id,store.`name` as store_name FROM tiny_area area LEFT JOIN t_store store ON area.store_no=store.storeno where store.name='"
					+ storeName + "' and area.status=0 and IFNULL(store.estate,'')!='闭店中' AND store.flag=0";
		}

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (lst_data != null && lst_data.size() > 0) {
			for (Map<String, Object> map : lst_data) {
				Map<String, Object> byTinyId = tinyVillageDao
						.findTinyVillageInfoByTinyId(map.get("tiny_village_id") + "", where);
				if (byTinyId != null) {
					byTinyId.put("vallage_area", map.get("vallage_area") == null ? 0 : map.get("vallage_area"));
					byTinyId.put("store_name", map.get("store_name"));
					byTinyId.put("tiny_map", map.get("有"));
					listData.add(byTinyId);
				}
			}
		}
		pageInfo.setTotalRecords(listData.size());
		total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		for (int i = pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1); i < pageInfo.getRecordsPerPage()
				* (pageInfo.getCurrentPage()); i++) {
			if (i > listData.size() - 1)
				break;
			Map<String, Object> map = listData.get(i);
			if (map != null) {
				resultData.add(map);
			}
		}
		map_result.put("pageinfo", pageInfo);
		map_result.put("data", resultData);
		map_result.put("status", "success");
		map_result.put("totalPage", total_pages);
		return map_result;
	}

	
	@Override
	public void updateTinyAreaEmployeeIsNull_A(String employeeNo) {
		String sql = "update tiny_area set employee_a_no=null where employee_a_no='"+employeeNo+"'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		int update = query.executeUpdate();
		
	}
	
	public void updateTinyAreaEmployeeIsNull_B(String employeeNo) {
		String sql = "update tiny_area set employee_b_no=null where employee_b_no='"+employeeNo+"'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		int update = query.executeUpdate();
		
	}

}
