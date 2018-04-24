package com.cnpc.pms.heatmap.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.json.JSONObject;

import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.heatmap.entity.RequestInfoDto;
import com.cnpc.pms.heatmap.manager.GAXDriveRecodeManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.mongodb.common.MongoDbUtil;
import com.cnpc.pms.mongodb.manager.MongoDBManager;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.platform.dao.PlatformStoreDao;
import com.cnpc.pms.slice.manager.AreaManager;
import com.gexin.fastjson.JSONArray;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class GAXDriveRecodeManagerImpl extends BizBaseCommonManager implements GAXDriveRecodeManager{

	@Override
	public Map<String, Object> getDriveRecode(RequestInfoDto requestInfoDto) {
		PlatformStoreDao platformStoreDao = (PlatformStoreDao)SpringHelper.getBean(PlatformStoreDao.class.getName());
		MongoDBManager mongoDBManager = (MongoDBManager)SpringHelper.getBean("mongoDBManager");
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		Map<String,Object> result = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		if(requestInfoDto.getEmployeeNo() != null){
			List<Map<String,Object>> employeeList = new ArrayList<Map<String,Object>>();
			if(requestInfoDto.getIsRealtime() != null){
				employeeList = platformStoreDao.getEmployeeByEmployeeNo(requestInfoDto.getEmployeeNo(),true);
			}else{
				employeeList = platformStoreDao.getEmployeeByEmployeeNo(requestInfoDto.getEmployeeNo(),false);
			}
			Map<String, Object> selectEmployeeDriveRecode = new HashMap<String, Object>();
			if(employeeList != null && employeeList.size()>0){
				selectEmployeeDriveRecode = mongoDBManager.selectEmployeeDriveRecode(employeeList,requestInfoDto.getBeginDate(),requestInfoDto.getEndDate());
			}
			result.put("diverRecode", selectEmployeeDriveRecode);
		}else{
			List<Map<String,Object>> employeeList = new ArrayList<Map<String,Object>>();
			if(requestInfoDto.getStoreId() == null){
				StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
				List<Map<String, Object>> cityNOOfCityById = storeDao.getCityNOOfCityById(requestInfoDto.getCityId());
				String cityNo = String.valueOf(cityNOOfCityById.get(0).get("cityno"));
				if(cityNo.startsWith("00")){
					cityNo = cityNo.substring(1,cityNo.length());
				}
				if(requestInfoDto.getIsRealtime() != null){
					employeeList = platformStoreDao.getEmployeeByCity(cityNo,true);
				}else{
					employeeList = platformStoreDao.getEmployeeByCity(cityNo,false);

				}
			}else{
				Store store  = storeManager.findStore(requestInfoDto.getStoreId());
				if(requestInfoDto.getIsRealtime() != null){
					employeeList = platformStoreDao.getEmployeeByStore(store.getStoreno(),true);
				}else{
					employeeList = platformStoreDao.getEmployeeByStore(store.getStoreno(),false);
				}
				
			}
			
			Map<String, Object> selectEmployeeDriveRecode = new HashMap<String, Object>();
			if(employeeList != null && employeeList.size()>0){
				selectEmployeeDriveRecode = mongoDBManager.selectEmployeeDriveRecode(employeeList,requestInfoDto.getBeginDate(),requestInfoDto.getEndDate());
			}
			result.put("diverRecode", selectEmployeeDriveRecode);
		}
		return result;
	}

	@Override
	public Map<String, Object> getStoreServiceAreaAndPosition(RequestInfoDto requestInfoDto) {
		Map<String,Object> result = new HashMap<String,Object>();
		if(requestInfoDto.getStoreId() == null){
			UserManager umanager = (UserManager)SpringHelper.getBean("userManager");
			UserDTO userDTO = umanager.getCurrentUserDTO();
			StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
			List<Map<String, Object>> storeList = storeDao.getAllStoreOfCRM(userDTO.getId(),requestInfoDto.getCityId(), "CSZJ");//获取门店
			List<Object> list = new ArrayList<Object>();
			for(int i=0;i<storeList.size();i++){//获取门店platformId
				Map<String, Object> teMap = storeList.get(i);
				if(teMap.get("storeno") != null && teMap.get("storeno").toString() != ""){
					String store_no = teMap.get("storeno").toString();
					if(!store_no.equals("0021Y0020")){
						list.add(teMap.get("platformid"));
					}
				}
			}
			try {
				MongoDbUtil mDbUtil = (MongoDbUtil)SpringHelper.getBean("mongodb");
				MongoDatabase database = mDbUtil.getDatabase();
			    // 先查询门店服务范围
				MongoCollection<Document> collection = database.getCollection("store_service_area");
				FindIterable<Document> dIterable = collection.find(new BasicDBObject("storeId", new BasicDBObject("$in",list)));
				MongoCursor<Document> iterator = dIterable.iterator();
				org.json.JSONArray jArray = new org.json.JSONArray();
				JSONObject jObject = null;
				 while (iterator.hasNext()) {  
			           Document doc = iterator.next();  
			           jObject = new JSONObject();
					   jObject.put("storeServiceArea", doc.get("vertex"));
					   jObject.put("platformid", doc.get("storeId"));
					   jArray.put(jObject);
			    }
				
				MongoCollection<Document> collection2 = database.getCollection("store_position");
				FindIterable<Document> dIterable2 = collection2.find(new BasicDBObject("_id", new BasicDBObject("$in",list)));
				org.json.JSONArray jArray2 = new org.json.JSONArray();
				MongoCursor<Document> iterator2 = dIterable2.iterator();
				JSONObject jObject2 = null;
				 while (iterator2.hasNext()) {  
			           Document doc = iterator2.next();  
			           jObject2 = new JSONObject();
			           for(int i=0;i<storeList.size();i++){
			        	   Map<String, Object> teMap = storeList.get(i);
			        	   if(teMap.get("platformid") != null&&teMap.get("platformid").equals(doc.get("_id"))){
							   jObject2.put("storeName", teMap.get("name"));
			        	   }
			           }
					   jObject2.put("position", doc.get("location"));
					   jArray2.put(jObject2);
			    }
				result.put("serviceArea", JSONArray.parse(jArray.toString()));
				result.put("position", JSONArray.parse(jArray2.toString()));
				result.put("code",CodeEnum.success.getValue());
				result.put("message", CodeEnum.success.getDescription());
			} catch (Exception e) {
				e.printStackTrace();
				result.put("code",CodeEnum.error.getValue());
				result.put("message", CodeEnum.error.getDescription());
			}
			return result;
		}else{
			AreaManager areaManager = (AreaManager)SpringHelper.getBean("areaManager");
			Map<String, Object> storeServiceAreaAndPosition = areaManager.getStoreServiceAreaAndPosition(requestInfoDto.getStoreId());
			result.put("serviceArea", storeServiceAreaAndPosition);
			return result;
		}
		
	}

	@Override
	public Map<String, Object> getEmployeeStatus(String employeeNo) {
		Map<String,Object> result = new HashMap<String,Object>();
		PlatformStoreDao platformStoreDao = (PlatformStoreDao)SpringHelper.getBean(PlatformStoreDao.class.getName());
		try {
			Map<String, Object> employeeStatus = platformStoreDao.getEmployeeStatus(employeeNo);
			result.put("code",CodeEnum.success.getValue());
			result.put("message", CodeEnum.success.getDescription());
			result.put("data", employeeStatus);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
	}

	@Override
	public Map<String, Object> getDynamicDriveRecode(RequestInfoDto requestInfoDto) {
		PlatformStoreDao platformStoreDao = (PlatformStoreDao)SpringHelper.getBean(PlatformStoreDao.class.getName());
		//MongoDBManager mongoDBManager = (MongoDBManager)SpringHelper.getBean("mongoDBManager");
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		Map<String,Object> result = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		List<String> list = new ArrayList<String>();
		if(requestInfoDto.getEmployeeNo() != null){
			List<Map<String,Object>> employeeList = platformStoreDao.getEmployeeByEmployeeNo(requestInfoDto.getEmployeeNo(),true);
			Map<String, Object> employeePosition = getEmployeePositions(employeeList);
			result.put("diverRecode", employeePosition);
		}else{
			List<Map<String,Object>> employeeList = new ArrayList<Map<String,Object>>();
			if(requestInfoDto.getStoreId() == null){
				List<Map<String, Object>> cityNOOfCityById = storeDao.getCityNOOfCityById(requestInfoDto.getCityId());
				String cityNo = String.valueOf(cityNOOfCityById.get(0).get("cityno"));
				if(cityNo.startsWith("00")){
					cityNo = cityNo.substring(1,cityNo.length());
				}
				employeeList = platformStoreDao.getEmployeeByCity(cityNo,true);
			}else{
				Store store  = storeManager.findStore(requestInfoDto.getStoreId());
				employeeList = platformStoreDao.getEmployeeByStore(store.getStoreno(),true);
			}
			if(employeeList != null && employeeList.size()>0){
				Map<String, Object> employeePosition = getEmployeePositions(employeeList);
				result.put("diverRecode", employeePosition);
			}
		}
		
		
		return result;
	}

	@Override
	public Map<String, Object> getEmployeePositions(List<Map<String,Object>> list) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			List<Object> list1 = new ArrayList<Object>();
			for (int i = 0; i < list.size(); i++) {
				list1.add(list.get(i).get("employeeId"));
				
			}
			
			MongoDbUtil mDbUtil = (MongoDbUtil)SpringHelper.getBean("mongodb");
			MongoDatabase database = mDbUtil.getDatabase();
			MongoCollection<Document> collection = database.getCollection("employee_position");
			org.json.JSONArray jArray = new org.json.JSONArray();
			BasicDBObject query = new BasicDBObject();
			query.append("employeeId", new BasicDBObject("$in",list1));
			FindIterable<Document> projection = collection.find(query).projection(new Document("_id","$employeeId").append("employeeId", 1).append("position", 1));
			MongoCursor<Document> cursor = projection.iterator();
			JSONObject jObject = null;
			while (cursor.hasNext()) {  
		           Document doc = cursor.next();  
		           jObject = new JSONObject();
				   jObject.put("locations", doc.get("position"));
				   jObject.put("id", doc.get("employeeId"));
				   //jObject.put("position", doc.get("position"));
				   for(int i = 0; i < list.size(); i++){
					    Map<String, Object> map = list.get(i);
					   if(map.get("employeeId").equals(doc.get("employeeId"))){
				        	jObject.put("employeeNo", map.get("employeeNo"));
							jObject.put("storeName", map.get("storeName"));
							jObject.put("employeeName",map.get("employeeName"));
							jObject.put("platformid", map.get("platformid"));
							jObject.put("online", map.get("online"));
							break;
					   } 
				   }
				   jArray.put(jObject);
		    }
	        result.put("code",CodeEnum.success.getValue());
			result.put("message", CodeEnum.success.getDescription());
			result.put("data", JSONArray.parse(jArray.toString()));
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
		return result;
	}
	
	


}
