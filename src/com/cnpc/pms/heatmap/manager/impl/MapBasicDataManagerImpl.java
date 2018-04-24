package com.cnpc.pms.heatmap.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Spring;

import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.heatmap.dao.DfCustomerMonthOrderDao;
import com.cnpc.pms.heatmap.manager.MapBasicDataManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.mongodb.dao.MongoDBDao;
import com.cnpc.pms.personal.dao.BuildingDao;
import com.cnpc.pms.personal.dao.CityDao;
import com.cnpc.pms.personal.dao.CountyDao;
import com.cnpc.pms.personal.dao.HouseDao;
import com.cnpc.pms.personal.dao.ProvinceDao;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.dao.TinyAreaDao;
import com.cnpc.pms.personal.dao.TinyVillageDao;
import com.cnpc.pms.personal.dao.TownDao;
import com.cnpc.pms.personal.dao.VillageDao;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.platform.dao.DfCustomerOrderDao;

public class MapBasicDataManagerImpl extends BizBaseCommonManager implements MapBasicDataManager{

	@Override
	public Map<String, Object> getChinaBasicData() {
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			ProvinceDao provinceDao = (ProvinceDao)SpringHelper.getBean(ProvinceDao.class.getName());
			CityDao cityDao = (CityDao)SpringHelper.getBean(CityDao.class.getName());
			TinyVillageDao tinyVillageDao = (TinyVillageDao)SpringHelper.getBean(TinyVillageDao.class.getName());
			//DfCustomerOrderDao dfCustomerOrderDao = (DfCustomerOrderDao)SpringHelper.getBean(DfCustomerOrderDao.class.getName());
			DfCustomerMonthOrderDao dfCustomerMonthOrderDao = (DfCustomerMonthOrderDao)SpringHelper.getBean(DfCustomerMonthOrderDao.class.getName());
			Integer provinceCount = provinceDao.getProvinceCount();
			Integer conProvinceCount = provinceDao.getProvinceCountByStore();
			Integer residentsHouseCount = tinyVillageDao.findResidentsHouseCount();
			//Integer customerCount = dfCustomerOrderDao.findCustomerCount();
			Integer customerCount = dfCustomerMonthOrderDao.findAllCustomer();
			Integer cityCount = cityDao.getCityCount();
			Integer conCityCount = cityDao.getConCityCount();
			result.put("provinceCount", provinceCount);
			result.put("conProvinceCount", conProvinceCount);
			result.put("cityCount", cityCount);
			result.put("conCityCount", conCityCount);
			result.put("residentsHouseCount", residentsHouseCount);
			result.put("customerCount", customerCount);
			result.put("code", CodeEnum.success.getValue());
			result.put("message", CodeEnum.success.getDescription());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
	}

	@Override
	public Map<String, Object> getProvinceBasicData(Long province_id) {
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			//ProvinceDao provinceDao = (ProvinceDao)SpringHelper.getBean(ProvinceDao.class.getName());
			//StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
			CityDao cityDao = (CityDao)SpringHelper.getBean(CityDao.class.getName());
			TinyVillageDao tinyVillageDao = (TinyVillageDao)SpringHelper.getBean(TinyVillageDao.class.getName());
			//DfCustomerOrderDao dfCustomerOrderDao = (DfCustomerOrderDao)SpringHelper.getBean(DfCustomerOrderDao.class.getName());
			DfCustomerMonthOrderDao dfCustomerMonthOrderDao = (DfCustomerMonthOrderDao)SpringHelper.getBean(DfCustomerMonthOrderDao.class.getName());
			Integer cityCount = cityDao.getCityCountByProvinceId(province_id);
			Integer conCityCount = cityDao.getConCityByProvinceId(province_id);
			Integer residentsHouseCount = tinyVillageDao.findResidentsHouseCountByProvince(province_id);
			/*List<Store> storeList = storeManager.findStoreByProvinceId(province_id);
			StringBuilder sb = new StringBuilder();
			if(storeList != null && storeList.size()>0){
				for(int i = 0; i < storeList.size(); i++){
					Store store = storeList.get(i);
					String storeno = store.getStoreno();
					sb.append("'"+storeno+"'");
					if(i<storeList.size()-1){
						sb.append(",");
					}
				}
			}
			Integer customerCount = dfCustomerOrderDao.findCustomerCountByStore(sb.toString());*/
			Integer customerCount = dfCustomerMonthOrderDao.findCustomerByProvinceId(province_id);
			result.put("cityCount", cityCount);
			result.put("conCityCount", conCityCount);
			result.put("residentsHouseCount", residentsHouseCount);
			result.put("customerCount", customerCount);
			result.put("code", CodeEnum.success.getValue());
			result.put("message", CodeEnum.success.getDescription());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
	}

	@Override
	public Map<String, Object> getCityBasicData(Long city_id) {
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			CityDao cityDao = (CityDao)SpringHelper.getBean(CityDao.class.getName());
			String cityName = cityDao.getCityByCityId(city_id);
			//StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
			CountyDao countyDao = (CountyDao)SpringHelper.getBean(CountyDao.class.getName());
			TinyVillageDao tinyVillageDao = (TinyVillageDao)SpringHelper.getBean(TinyVillageDao.class.getName());
			//DfCustomerOrderDao dfCustomerOrderDao = (DfCustomerOrderDao)SpringHelper.getBean(DfCustomerOrderDao.class.getName());
			DfCustomerMonthOrderDao dfCustomerMonthOrderDao = (DfCustomerMonthOrderDao)SpringHelper.getBean(DfCustomerMonthOrderDao.class.getName());
			VillageDao villageDao = (VillageDao)SpringHelper.getBean(VillageDao.class.getName());
			TownDao townDao = (TownDao)SpringHelper.getBean(TownDao.class.getName());
			Integer countyCount = countyDao.getCountyCountByCityId(city_id);
			Integer conCountyCount = countyDao.getConCountyCountByCityId(city_id);
			Integer townCount = townDao.findTownCountByCityId(city_id);
			Integer conTownCount = townDao.findConTownCountByCityId(city_id);
			Integer residentsHouseCount = tinyVillageDao.findResidentsHouseCountByCity(city_id);
			Integer villageCount = villageDao.findVillageCountByCityName(cityName);
			Integer conVillageCount = villageDao.findConVillageCountByCityName(cityName);
			/*List<Map<String,Object>> storeList = storeDao.selectStoreByCityId(city_id);
			StringBuilder sb = new StringBuilder();
			if(storeList != null && storeList.size()>0){
				for(int i = 0; i < storeList.size(); i++){
					Map<String, Object> map = storeList.get(i);
					String storeno = map.get("storeno").toString();
					sb.append("'"+storeno+"'");
					if(i<storeList.size()-1){
						sb.append(",");
					}
				}
			}
			Integer customerCount = dfCustomerOrderDao.findCustomerCountByStore(sb.toString());*/
			Integer customerCount = dfCustomerMonthOrderDao.findCustomerByCityId(city_id);
			result.put("countyCount", countyCount);
			result.put("conCountyCount", conCountyCount);
			result.put("townCount",townCount);
			result.put("conTownCount", conTownCount);
			result.put("villageCount", villageCount);
			result.put("conVillageCount", conVillageCount);
			result.put("residentsHouseCount", residentsHouseCount);
			result.put("customerCount", customerCount);
			result.put("code", CodeEnum.success.getValue());
			result.put("message", CodeEnum.success.getDescription());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
	}

	@Override
	public Map<String, Object> getStoreBasicData(Long store_id) {
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
			Store store = storeManager.findStore(store_id);
			String storeno = store.getStoreno();
			TownDao townDao = (TownDao)SpringHelper.getBean(TownDao.class.getName());
			VillageDao villageDao = (VillageDao)SpringHelper.getBean(VillageDao.class.getName());
			TinyVillageDao tinyVillageDao = (TinyVillageDao)SpringHelper.getBean(TinyVillageDao.class.getName());
			//DfCustomerOrderDao dfCustomerOrderDao = (DfCustomerOrderDao)SpringHelper.getBean(DfCustomerOrderDao.class.getName());
			DfCustomerMonthOrderDao dfCustomerMonthOrderDao = (DfCustomerMonthOrderDao)SpringHelper.getBean(DfCustomerMonthOrderDao.class.getName());
			TinyAreaDao tinyAreaDao = (TinyAreaDao)SpringHelper.getBean(TinyAreaDao.class.getName());
			
			Integer townCount = townDao.getTownCountByStore(store_id);
			Integer conTownCount = townDao.getConTownCountByStore(storeno);
			Integer villageCount = villageDao.findVillageCountByStore(store_id);
			Integer conVillageCount = villageDao.findConVillageCountByStore(storeno);
			Integer tinyVillageCount = tinyVillageDao.findTinyVillageCountByStore(store_id);
			Integer conTinyVillageCount = tinyAreaDao.selectVillageCountByStore(storeno);
			Integer residentsHouseCount = tinyVillageDao.findResidentsHouseCountByStore(store_id);
			//Integer customerCount = dfCustomerOrderDao.findCustomerCountByStore("'"+storeno+"'");
			Integer customerCount = dfCustomerMonthOrderDao.findCustomerByStoreId(store_id);
			
			result.put("townCount", townCount);
			result.put("conTownCount", conTownCount);
			result.put("villageCount", villageCount);
			result.put("conVillageCount", conVillageCount);
			result.put("tinyVillageCount", tinyVillageCount);
			result.put("conTinyVillageCount", conTinyVillageCount);
			result.put("residentsHouseCount",residentsHouseCount);
			result.put("customerCount",customerCount);
			result.put("code", CodeEnum.success.getValue());
			result.put("message", CodeEnum.success.getDescription());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		} finally {
		}
	}

	@Override
	public Map<String, Object> getAreaBasicData(String areaNo,Long storeId) {
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			MongoDBDao mongoDBDao = (MongoDBDao)SpringHelper.getBean(MongoDBDao.class.getName());
			TinyVillageDao tinyVillageDao = (TinyVillageDao)SpringHelper.getBean(TinyVillageDao.class.getName());
			// dfCustomerOrderDao = (DfCustomerOrderDao)SpringHelper.getBean(DfCustomerOrderDao.class.getName());
			DfCustomerMonthOrderDao dfCustomerMonthOrderDao = (DfCustomerMonthOrderDao)SpringHelper.getBean(DfCustomerMonthOrderDao.class.getName());
			TinyAreaDao tinyAreaDao = (TinyAreaDao)SpringHelper.getBean(TinyAreaDao.class.getName());
			BuildingDao buildingDao = (BuildingDao)SpringHelper.getBean(BuildingDao.class.getName());
			HouseDao houseDao = (HouseDao)SpringHelper.getBean(HouseDao.class.getName());
			Integer tinyVillageCount = tinyVillageDao.findVillageCountByAreaNo(areaNo);
			Integer buildingCount = buildingDao.findBuildingCountByAreaNo(areaNo);
			Integer residentsHouseCount = tinyVillageDao.findResidentsHouseCountByArea(areaNo);
			Integer houseCount = houseDao.findHouseCountByArea(areaNo);
			/*List<Map<String,Object>> tinyVillageList = mongoDBDao.queryAllTinyVillageOfArea(storeId, areaNo);
			StringBuilder sb = new StringBuilder();
			for(int i = 0;i < tinyVillageList.size(); i++){
				Map<String, Object> map = tinyVillageList.get(i);
				String code = map.get("code").toString();
				sb.append("'"+code+"'");
				if(i != tinyVillageList.size()-1){
					sb.append(",");
				}
			}
			Integer customerCount = dfCustomerOrderDao.findCustomerByTinyVillageCode(sb.toString());*/
			Integer customerCount = dfCustomerMonthOrderDao.findCustomerByAreaNo(areaNo);
			//Integer customerCount = dfCustomerOrderDao.findCustomerCountByStore("'"+storeno+"'");
			result.put("tinyVillageCount", tinyVillageCount);
			result.put("buildingCount", buildingCount);
			result.put("houseCount", houseCount);
			result.put("residentsHouseCount",residentsHouseCount);
			result.put("customerCount",customerCount);
			result.put("code", CodeEnum.success.getValue());
			result.put("message", CodeEnum.success.getDescription());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
	}

	@Override
	public Map<String, Object> getVillageBasicData(String code) {
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			BuildingDao buildingDao = (BuildingDao)SpringHelper.getBean(BuildingDao.class.getName());
			HouseDao houseDao = (HouseDao)SpringHelper.getBean(HouseDao.class.getName());
			//DfCustomerOrderDao dfCustomerOrderDao = (DfCustomerOrderDao)SpringHelper.getBean(DfCustomerOrderDao.class.getName());
			DfCustomerMonthOrderDao dfCustomerMonthOrderDao = (DfCustomerMonthOrderDao)SpringHelper.getBean(DfCustomerMonthOrderDao.class.getName());
			TinyVillageDao tinyVillageDao = (TinyVillageDao)SpringHelper.getBean(TinyVillageDao.class.getName());
			Integer buildingCount = buildingDao.findBuildingCountByVillageCode(code);
			Integer houseCount = houseDao.findHouseCountByVillageCode(code);
			Integer residentsHouseCount = tinyVillageDao.findResidentsHouseCountByVillageCode(code);
			//Integer customerCount = dfCustomerOrderDao.findCustomerNumberBytinyvillageCode(code);
			Integer customerCount = dfCustomerMonthOrderDao.findCustomerByVillageCode(code);
			result.put("buildingCount", buildingCount);
			result.put("houseCount", houseCount);
			result.put("residentsHouseCount",residentsHouseCount);
			result.put("customerCount",customerCount);
			result.put("code", CodeEnum.success.getValue());
			result.put("message", CodeEnum.success.getDescription());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
	}

}
