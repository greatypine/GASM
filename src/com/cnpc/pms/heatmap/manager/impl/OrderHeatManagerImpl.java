package com.cnpc.pms.heatmap.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.heatmap.dao.DfCustomerMonthOrderDao;
import com.cnpc.pms.heatmap.dao.OrderHeatDao;
import com.cnpc.pms.heatmap.entity.OrderHeatDto;
import com.cnpc.pms.heatmap.manager.OrderHeatManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.mongodb.common.MongoDbUtil;
import com.cnpc.pms.mongodb.dao.MongoDBDao;
import com.cnpc.pms.mongodb.manager.MongoDBManager;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.platform.dao.DfCustomerOrderDao;
import com.cnpc.pms.platform.dao.OrderDao;
import com.cnpc.pms.slice.manager.AreaManager;
import com.gexin.fastjson.JSONArray;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class OrderHeatManagerImpl extends BizBaseCommonManager implements OrderHeatManager{

	@Override
	public Map<String, Object> getLagLatByOrder(OrderHeatDto orderHeatDto) {
		OrderDao orderHeatDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		MongoDBManager mongoDBManager = (MongoDBManager)SpringHelper.getBean("mongoDBManager");
		DfCustomerOrderDao dfCustomerOrderDao = (DfCustomerOrderDao)SpringHelper.getBean(DfCustomerOrderDao.class.getName());
		DfCustomerMonthOrderDao dfCustomerMonthOrderDao = (DfCustomerMonthOrderDao)SpringHelper.getBean(DfCustomerMonthOrderDao.class.getName());
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		AreaManager areaManager = (AreaManager)SpringHelper.getBean("areaManager");
		Map<String, Object> mapStr = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
			if(orderHeatDto.getStore_id() != null){//城市中的门店或者是单个门店，展示该门店服务范围，及小区，及门店位置，鼠标点击或移过显示小区名，国安侠
				Store store  = storeManager.findStore(orderHeatDto.getStore_id());
				/*//门店服务区
				Map<String, Object> storeServiceArea = mongoDBManager.getStoreServiceArea(store.getPlatformid());*/
				//门店小区
				Map<String, Object> selecTinyVillageCoord = mongoDBManager.selecTinyVillageCoord(orderHeatDto.getStore_id());
				/*Map<String, Object> storePosition = mongoDBManager.getStorePosition(orderHeatDto.getStore_id());*/
				Map<String, Object> storeServiceAreaAndPosition = areaManager.getStoreServiceAreaAndPosition(orderHeatDto.getStore_id());
				mapStr.put("serviceAreaAndPosition", storeServiceAreaAndPosition);
				mapStr.put("tinyVillageCoord", selecTinyVillageCoord);
				//mapStr.put("storePosition", storePosition);
				sb.append("'"+store.getStoreno()+"'");
			}else{//城市下所有门店，需要先通过city_id查询store_id,通过store_id查询服务区并进行展示，鼠标点击或划过展示门店名称
				UserManager umanager = (UserManager)SpringHelper.getBean("userManager");
				UserDTO userDTO = umanager.getCurrentUserDTO();
				//这里需要先判断角色
				List<Map<String, Object>> storeList = storeDao.getAllStoreOfCRM(userDTO.getId(),orderHeatDto.getCity_id(), "CSZJ");//获取门店
				for(int i=0;i<storeList.size();i++){//获取门店platformId
					Map<String, Object> teMap = storeList.get(i);
					if(teMap.get("storeno") != null && teMap.get("storeno").toString() != ""){
						String store_no = teMap.get("storeno").toString();
						sb.append("'"+store_no+"'");
						if(i<storeList.size()-1){
							sb.append(",");
						}
					}
				}
				Map<String, Object> allStoreServiceAreaOfCity = mongoDBManager.getAllStoreServiceAreaOfCity(orderHeatDto.getCity_id());
				mapStr.put("serviceArea", allStoreServiceAreaOfCity);
			}
			List<Map<String, Object>> lagLatByOrder = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> lagLatCount = new ArrayList<Map<String,Object>>();
			if(orderHeatDto.getType().equals("ddl")){
				lagLatByOrder = orderHeatDao.getLagLatByOrder(sb.toString(),orderHeatDto.getBeginDate(),orderHeatDto.getEndDate());
				lagLatCount = orderHeatDao.getLagLatCount(sb.toString(),orderHeatDto.getBeginDate(),orderHeatDto.getEndDate());
			} else if(orderHeatDto.getType().equals("yye")){
				lagLatByOrder= orderHeatDao.getLagLatTradingPriceByOrder(sb.toString(),orderHeatDto.getBeginDate(),orderHeatDto.getEndDate());
				lagLatCount = orderHeatDao.getLagLatTradingPriceCount(sb.toString(),orderHeatDto.getBeginDate(),orderHeatDto.getEndDate());
			} else if(orderHeatDto.getType().equals("yhl")){
				if(orderHeatDto.getStore_id() != null){
					//lagLatByOrder = dfCustomerMonthOrderDao.findCustomerLatLngByStore(orderHeatDto.getStore_id());
					//lagLatCount = dfCustomerMonthOrderDao.findCustomerLatLngCountByStore(orderHeatDto.getStore_id());
					lagLatByOrder = orderHeatDao.getLatlngCustomerByStore(sb.toString(), orderHeatDto.getBeginDate(),orderHeatDto.getEndDate());
					lagLatCount = orderHeatDao.getLatlngCustomerCountByStore(sb.toString(), orderHeatDto.getBeginDate(),orderHeatDto.getEndDate());
				}else{
					//lagLatByOrder = dfCustomerMonthOrderDao.findCustomerLatLngByCity(orderHeatDto.getCity_id());
					//lagLatCount = dfCustomerMonthOrderDao.findCustomerLatLngCountByCity(orderHeatDto.getCity_id());
					List<Map<String, Object>> cityNOOfCityById = storeDao.getCityNOOfCityById(orderHeatDto.getCity_id());
					String cityNo = String.valueOf(cityNOOfCityById.get(0).get("cityno"));
					if(cityNo.startsWith("00")){
						cityNo = cityNo.substring(1,cityNo.length());
					}
					lagLatByOrder = orderHeatDao.getLatlngCustomerByCity(cityNo, orderHeatDto.getBeginDate(),orderHeatDto.getEndDate());
					lagLatCount = orderHeatDao.getLatlngCustomerCountByCity(cityNo, orderHeatDto.getBeginDate(),orderHeatDto.getEndDate());
				}
				//lagLatByOrder = dfCustomerOrderDao.findCustomerLatLng(sb.toString());
				//lagLatCount = dfCustomerOrderDao.findCustomerLatLngCount(sb.toString());
			}else{
				Map<String, Object> coordinateOfCity = mongoDBManager.getAllTinyVillageCoordinateOfCity(orderHeatDto.getCity_id());
				
			}
		mapStr.put("orderCount", lagLatCount);
		mapStr.put("orderHeat",lagLatByOrder);
		return mapStr;
	}

	@Override
	public Map<String, Object> getLatLngByVillage(OrderHeatDto orderHeatDto) {
		OrderHeatDao orderHeatDao = (OrderHeatDao)SpringHelper.getBean(OrderHeatDao.class.getName());
		MongoDBManager mongoDBManager = (MongoDBManager)SpringHelper.getBean("mongoDBManager");
		DfCustomerOrderDao dfCustomerOrderDao = (DfCustomerOrderDao)SpringHelper.getBean(DfCustomerOrderDao.class.getName());
		DfCustomerMonthOrderDao dfCustomerMonthOrderDao = (DfCustomerMonthOrderDao)SpringHelper.getBean(DfCustomerMonthOrderDao.class.getName());
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		AreaManager areaManager = (AreaManager)SpringHelper.getBean("areaManager");
		Map<String, Object> mapStr = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
			if(orderHeatDto.getStore_id() != null){//城市中的门店或者是单个门店，展示该门店服务范围，及小区，及门店位置，鼠标点击或移过显示小区名，国安侠
				Store store  = storeManager.findStore(orderHeatDto.getStore_id());
				Map<String, Object> storeServiceAreaAndPosition = areaManager.getStoreServiceAreaAndPosition(orderHeatDto.getStore_id());
				mapStr.put("serviceAreaAndPosition", storeServiceAreaAndPosition);
				Map<String, Object> selecTinyVillageCoord = selecTinyVillageCoord(orderHeatDto);
				mapStr.put("tinyVillageCoord", selecTinyVillageCoord);
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				if(orderHeatDto.getType().equals("ddl")){
					list = orderHeatDao.getTinyVillageOrderCount(orderHeatDto.getStore_id(),orderHeatDto.getBeginDate(), orderHeatDto.getEndDate());
				}else if(orderHeatDto.getType().equals("yye")){
					list = orderHeatDao.getTinyVillageGMVCount(orderHeatDto.getStore_id(),orderHeatDto.getBeginDate(), orderHeatDto.getEndDate());
				}else if(orderHeatDto.getType().equals("yhl")){
					list = orderHeatDao.getTinyVillageCustomerCount(orderHeatDto.getStore_id(),orderHeatDto.getBeginDate(), orderHeatDto.getEndDate());
				}
				mapStr.put("orderCount", list);
			}else{//城市下所有门店，需要先通过city_id查询store_id,通过store_id查询服务区并进行展示，鼠标点击或划过展示门店名称
				StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
				UserManager umanager = (UserManager)SpringHelper.getBean("userManager");
				UserDTO userDTO = umanager.getCurrentUserDTO();
				//这里需要先判断角色
				List<Map<String, Object>> storeList = storeDao.getAllStoreOfCRM(userDTO.getId(),orderHeatDto.getCity_id(), "CSZJ");//获取门店
				for(int i=0;i<storeList.size();i++){//获取门店platformId
					Map<String, Object> teMap = storeList.get(i);
					if(teMap.get("storeno") != null && teMap.get("storeno").toString() != ""){
						String store_no = teMap.get("storeno").toString();
						sb.append("'"+store_no+"'");
						if(i<storeList.size()-1){
							sb.append(",");
						}
					}
				}
				Map<String, Object> allStoreServiceAreaOfCity = mongoDBManager.getAllStoreServiceAreaOfCity(orderHeatDto.getCity_id());
				mapStr.put("serviceArea", allStoreServiceAreaOfCity);
				Map<String, Object> tinyVillageCoord = selecTinyVillageCoordByCity(orderHeatDto);
				mapStr.put("tinyVillageCoord", tinyVillageCoord);
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				if(orderHeatDto.getType().equals("ddl")){
					list = orderHeatDao.getTinyVillageOrderCountByCity(orderHeatDto.getCity_id(),orderHeatDto.getBeginDate(), orderHeatDto.getEndDate());
				}else if(orderHeatDto.getType().equals("yye")){
					list = orderHeatDao.getTinyVillageGMVCountByCity(orderHeatDto.getCity_id(),orderHeatDto.getBeginDate(), orderHeatDto.getEndDate());
				}else if(orderHeatDto.getType().equals("yhl")){
					list = orderHeatDao.getTinyVillageCustomerCountByCity(orderHeatDto.getCity_id(),orderHeatDto.getBeginDate(), orderHeatDto.getEndDate());
				}
				mapStr.put("orderCount", list);
			}
		
		return mapStr;
	}

	@Override
	public Map<String, Object> getOrderHeatMap(OrderHeatDto orderHeatDto) {
		Integer fromVillage = orderHeatDto.getFromVillage();
		if(fromVillage == 1){//小区查询
			return getLatLngByVillage(orderHeatDto);
		}else{//经纬度查询
			return getLagLatByOrder(orderHeatDto);
		}
	}
	
	@Override
	public Map<String, Object> selecTinyVillageCoord(OrderHeatDto orderHeatDto) {
		Map<String,Object> result = new HashMap<String,Object>();
		OrderHeatDao orderHeatDao = (OrderHeatDao)SpringHelper.getBean(OrderHeatDao.class.getName());
		MongoDBDao mDao = (MongoDBDao)SpringHelper.getBean(MongoDBDao.class.getName());
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		
	    // 注意这里的数据类型是document  
		try {
			List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
			if(orderHeatDto.getType().equals("ddl")){
				orderList = orderHeatDao.getTinyVillageOrderInfo(orderHeatDto.getStore_id(),orderHeatDto.getBeginDate(), orderHeatDto.getEndDate());
			}else if(orderHeatDto.getType().equals("yye")){
				orderList = orderHeatDao.getTinyVillageGMVInfo(orderHeatDto.getStore_id(),orderHeatDto.getBeginDate(), orderHeatDto.getEndDate());
			}else if(orderHeatDto.getType().equals("yhl")){
				orderList = orderHeatDao.getTinyVillageCustomerInfo(orderHeatDto.getStore_id(),orderHeatDto.getBeginDate(), orderHeatDto.getEndDate());
			}
			List<Map<String, Object>> list = mDao.queryAllTinyAreaOfStore(orderHeatDto.getStore_id());
			Store store = storeManager.findStore(orderHeatDto.getStore_id());
			if(list==null||list.size()==0){
				result.put("code",CodeEnum.nullData.getValue());
				result.put("message","门店所辖所有小区的小区编号不存在");
				return result;
			}else{ 
				org.json.JSONArray jArray = new org.json.JSONArray();
				JSONObject jObject = null;
				
				List<Object> codeList = new ArrayList<Object>();
				Map<String, Object> codeIds = new HashMap<String,Object>();
				for(int i=0;i<list.size();i++){
					Object code = list.get(i).get("code");
					Object id = list.get(i).get("id");
					if(code!=null){
						codeList.add(code);
						codeIds.put(code.toString(), id);
					}
					
				}
				
				MongoDbUtil mDbUtil = (MongoDbUtil)SpringHelper.getBean("mongodb");
				MongoDatabase database = mDbUtil.getDatabase();
			    // 注意这里的数据类型是document  
				MongoCollection<Document> collection = database.getCollection("tiny_area");
				
				
				//Bson query = Filters.in("code",codeArray);
				FindIterable<Document> dIterable = collection.find(new Document("code",new BasicDBObject("$in",codeList)).append("storeNo", store.getStoreno()));
				//FindIterable<Document> dIterable = collection.find(query);
			    JSONObject coordJson = null;
			    MongoCursor<Document> cursor = dIterable.iterator();  
			    if(cursor==null){
			    	result.put("data", jArray);
					result.put("code",CodeEnum.nullData.getValue());
					result.put("message","门店所辖小区坐标范围不存在");
					return result;
			    }
		        while (cursor.hasNext()) {  
		           Document doc = cursor.next();  
		           jObject = new JSONObject();
				   jObject.put("id", codeIds.get(doc.get("code")));
				   jObject.put("storeNo",doc.get("storeNo"));
				   jObject.put("code", doc.get("code"));
				   jObject.put("storeName", store.getName());
				   //jObject.put("location",doc.get("location"));
				   jObject.put("tinyVillageName", doc.get("name"));
				   jObject.put("employee_a_no", doc.get("employee_a_no"));
				   jObject.put("employee_b_no", doc.get("employee_b_no"));
				   Document crObj = (Document)doc.get("coordinate_range");
				   coordJson = new JSONObject(doc.get("coordinate_range"));
				   jObject.put("data", crObj.get("coordinates"));
				   for(int i = 0;i<orderList.size();i++){
					   Map<String, Object> map = orderList.get(i);
					   if(map.get("code").equals(doc.get("code"))){
						   jObject.put("priceOrCount", map.get("count"));
						   break;
					   }
				   }
				   jArray.put(jObject);
		        }  
				
				
				result.put("data", JSONArray.parse(jArray.toString()));
				result.put("code",CodeEnum.success.getValue());
				result.put("message", CodeEnum.success.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();
			//mBean.getMongoClient().close();
			result.put("code",CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
		return result;
	}

	@Override
	public Map<String, Object> selecTinyVillageCoordByCity(OrderHeatDto orderHeatDto) {
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		MongoDBDao mDao = (MongoDBDao)SpringHelper.getBean(MongoDBDao.class.getName());
		UserManager umanager = (UserManager)SpringHelper.getBean("userManager");
		OrderHeatDao orderHeatDao = (OrderHeatDao)SpringHelper.getBean(OrderHeatDao.class.getName());
		
		UserDTO userDTO = umanager.getCurrentUserDTO();
		Map<String, Object> result = new HashMap<String, Object>();
		
		
		try {
			List<Map<String, Object>> storeList = storeDao.getAllStoreOfCRM(userDTO.getId(),orderHeatDto.getCity_id(), "CSZJ");//获取门店
			Object[] storeNoArray = new Object[storeList.size()];
			List<Object>  storeNoList = new ArrayList<Object>();
			Map<String, Object>  storeMap = new HashMap<String,Object>();
			for(int i=0;i<storeList.size();i++){//获取门店platformId
				Map<String, Object> teMap = storeList.get(i);
				if(teMap.get("storeno")==null||"".equals(teMap.get("storeno"))){
					continue;
				}
				storeNoArray[i] = teMap.get("storeno");
				storeMap.put(teMap.get("storeno").toString(), teMap.get("name"));
			}
			
			
			MongoDbUtil mDbUtil = (MongoDbUtil)SpringHelper.getBean("mongodb");
			MongoDatabase database = mDbUtil.getDatabase();
		    // 先查询门店服务范围
			MongoCollection<Document> collection = database.getCollection("tiny_area");
			BasicDBObject query = new BasicDBObject();
			query.append("storeNo", new BasicDBObject("$in",storeNoArray));
			FindIterable<Document> dIterable = collection.find(query);
			MongoCursor<Document> cursor = dIterable.iterator(); 
	
			if(dIterable==null){
				result.put("code",CodeEnum.nullData.getValue());
				result.put("message","所有小区没有绑定坐标");
			}else{//服务范围存在 
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				if(orderHeatDto.getType().equals("ddl")){
					list = orderHeatDao.getTinyVillageOrderInfoByCity(orderHeatDto.getCity_id(), orderHeatDto.getBeginDate(), orderHeatDto.getEndDate());
				}else if(orderHeatDto.getType().equals("yye")){
					list = orderHeatDao.getTinyVillageGMVInfoByCity(orderHeatDto.getCity_id(), orderHeatDto.getBeginDate(), orderHeatDto.getEndDate());
				}else if(orderHeatDto.getType().equals("yhl")){
					list = orderHeatDao.getTinyVillageCustomerInfoByCity(orderHeatDto.getCity_id(), orderHeatDto.getBeginDate(), orderHeatDto.getEndDate());
				}
				org.json.JSONArray tmp_jarray = new org.json.JSONArray();
				//List<Map<String, Object>> tinyvillageArealist = mDao.getAreaOfTinyVillage("");
				while(cursor.hasNext()){
					Document teDocument = cursor.next();
					JSONObject jObject = new JSONObject(teDocument.toJson());
					String code = jObject.getString("code");
					String storeNo = teDocument.getString("storeNo");
					jObject.put("storeName", storeMap.get(storeNo)==null?"": storeMap.get(storeNo));
					for(int i = 0; i<list.size(); i++){
						Map<String, Object> map = list.get(i);
						if(code.equals(map.get("code"))){
							jObject.put("priceOrCount", map.get("count"));
						}
					}
					/*for(Map<String, Object> m:tinyvillageArealist){
						if(code.equals(m.get("code"))){
							jObject.put("areaName", m.get("name"));
							break;
						}
					}*/
					
					tmp_jarray.put(jObject);
				}
				
				
				result.put("data",JSONArray.parse(tmp_jarray.toString()));
				result.put("code",CodeEnum.success.getValue());
				result.put("message", CodeEnum.success.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();
			//mBean.getMongoClient().close();
			result.put("code",CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
		return result;
	}


}
