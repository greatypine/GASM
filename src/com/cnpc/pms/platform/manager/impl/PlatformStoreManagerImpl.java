package com.cnpc.pms.platform.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.json.JSONObject;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.mongodb.common.MongoDbUtil;
import com.cnpc.pms.platform.dao.PlatformStoreDao;
import com.cnpc.pms.platform.entity.PlatformStore;
import com.cnpc.pms.platform.manager.PlatformStoreManager;
import com.gexin.fastjson.JSONArray;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * 平台门店表查询 Created by liuxiao on 2016/10/25.
 */
public class PlatformStoreManagerImpl extends BizBaseCommonManager implements PlatformStoreManager {

	@Override
	public PlatformStore findPlatformStoreById(String id) {
		return (PlatformStore) this.getObject(id);
	}

	@Override
	public PlatformStore findPlatformStoreByName(String name) {
		List<?> lst_store = this.getList(FilterFactory.getSimpleFilter("name", name));
		if (lst_store != null && lst_store.size() > 0 && lst_store.size() < 2) {
			return (PlatformStore) lst_store.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> showPlatformStoreData(QueryConditions conditions) {
		PlatformStoreDao platformStoreDao = (PlatformStoreDao) SpringHelper.getBean(PlatformStoreDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();

		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND name like '").append(map_where.get("value")).append("'");
			}
		}
		/*
		 * System.out.println(sb_where); map_result.put("pageinfo", obj_page);
		 * map_result.put("header", "平台门店列表"); map_result.put("data",
		 * platformStoreDao.getPlatformStoreList(sb_where.toString(),
		 * obj_page));
		 */
		return platformStoreDao.getPlatformStoreInfoList(sb_where.toString(), obj_page);
	}

	@Override
	public PlatformStore findPlatStoreById(String id) {
		List<?> lst_store = this.getList(FilterFactory.getSimpleFilter("id", id));
		if (lst_store != null && lst_store.size() > 0 && lst_store.size() < 2) {
			return (PlatformStore) lst_store.get(0);
		}
		return null;
	}

	@Override
	public PlatformStore findPlatStoreByCode(String code) {
		List<?> lst_store = getList(FilterFactory.getSimpleFilter("code", code));
		if (lst_store != null && lst_store.size() > 0 && lst_store.size() < 2) {
			return (PlatformStore) lst_store.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> findroundEmployeeByCitycode(String citycode) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {

			PlatformStoreDao platformStoreDao = (PlatformStoreDao) SpringHelper
					.getBean(PlatformStoreDao.class.getName());
			List<Map<String, Object>> list = platformStoreDao.getEmployeeByCity(citycode, true);
			List<Object> list1 = new ArrayList<Object>();
			for (int i = 0; i < list.size(); i++) {
				list1.add(list.get(i).get("employeeId"));
			}
			MongoDbUtil mDbUtil = (MongoDbUtil) SpringHelper.getBean("mongodb");
			MongoDatabase database = mDbUtil.getDatabase();
			MongoCollection<Document> collection = database.getCollection("employee_position");
			org.json.JSONArray jArray = new org.json.JSONArray();
			BasicDBObject query = new BasicDBObject();
			query.append("employeeId", new BasicDBObject("$in", list1));
			FindIterable<Document> projection = collection.find(query)
					.projection(new Document("_id", "$employeeId").append("employeeId", 1).append("position", 1));
			MongoCursor<Document> cursor = projection.iterator();
			JSONObject jObject = null;
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				jObject = new JSONObject();
				jObject.put("locations", doc.get("position"));
				jObject.put("id", doc.get("employeeId"));
				// jObject.put("position", doc.get("position"));
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					if (map.get("employeeId").equals(doc.get("employeeId"))) {
						jObject.put("phone", (map.get("mobilephone") == null || map.get("mobilephone") == "") ? ""
								: map.get("mobilephone"));
						jObject.put("employeeNo", map.get("employeeNo"));
						jObject.put("storeName", map.get("storeName"));
						jObject.put("employeeName", map.get("employeeName"));
						jObject.put("platformid", map.get("platformid"));
						break;
					}
				}
				jArray.put(jObject);
			}
			result.put("code", CodeEnum.success.getValue());
			result.put("message", CodeEnum.success.getDescription());
			result.put("data", JSONArray.parse(jArray.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
		return result;
	}
}
