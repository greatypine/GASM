package com.cnpc.pms.personal.manager.impl;

import java.util.List;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.dao.BuildingDao;
import com.cnpc.pms.personal.dao.HouseDao;
import com.cnpc.pms.personal.dao.TinyVillageDao;
import com.cnpc.pms.personal.dao.TownDao;
import com.cnpc.pms.personal.dao.VillageDao;
import com.cnpc.pms.personal.entity.CityDataStatistics;
import com.cnpc.pms.personal.entity.DistCityCode;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.CityDataStatisticsManager;
import com.cnpc.pms.personal.manager.DistCityCodeManager;
import com.cnpc.pms.personal.manager.StoreDataStatisticsManager;

public class CityDataStatisticsManagerImpl extends BizBaseCommonManager implements CityDataStatisticsManager{
	
	@Override
	public CityDataStatistics findCityDataStatisticsByCityId(Long cityId) {
		// TODO Auto-generated method stub
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("city_id",cityId));
        if(lst_data != null && lst_data.size() > 0){
            return (CityDataStatistics)lst_data.get(0);
        }
        return null;
	}

	@Override
	public void syncCityDataStatistics() {
		HouseDao houseDao=(HouseDao)SpringHelper.getBean(HouseDao.class.getName());
		BuildingDao buildingDao=(BuildingDao)SpringHelper.getBean(BuildingDao.class.getName());
		CityDataStatisticsManager cityDataStatisticsManager=(CityDataStatisticsManager)SpringHelper.getBean("cityDataStatisticsManager");
		TownDao townDao=(TownDao)SpringHelper.getBean(TownDao.class.getName());
		VillageDao villageDao=(VillageDao)SpringHelper.getBean(VillageDao.class.getName());
		TinyVillageDao tinyVillageDao=(TinyVillageDao)SpringHelper.getBean(TinyVillageDao.class.getName());
		//获取所有城市
		DistCityCodeManager distCityCodeManager=(DistCityCodeManager)SpringHelper.getBean("distCityCodeManager");
		List<DistCityCode> allDistCityList = distCityCodeManager.queryAllDistCityList();
		for (DistCityCode distCityCode : allDistCityList) {
			CityDataStatistics findCityDataStatisticsByCityId = findCityDataStatisticsByCityId(distCityCode.getId());
			if(findCityDataStatisticsByCityId==null){
				findCityDataStatisticsByCityId=new CityDataStatistics();
			}
			findCityDataStatisticsByCityId.setCity_id(distCityCode.getId());
			findCityDataStatisticsByCityId.setCity_name(distCityCode.getCityname());
			//根据城市名称查询所有的街道
			List<Town> findTownCountByCityName = townDao.findTownCountByCityName(distCityCode.getCityname());
			//根据城市查询所有的社区
			List<Village> findVillageByCityName = villageDao.findVillageByCityName(distCityCode.getCityname());
			//获取所有的小区
			List<TinyVillage> findTinyVillageByCityName = tinyVillageDao.findTinyVillageByCityName(distCityCode.getCityname());
			if(findTownCountByCityName!=null){
				findCityDataStatisticsByCityId.setCityTowncount(findTownCountByCityName.size());
			}else{
				findCityDataStatisticsByCityId.setCityTowncount(0);
			}
			if(findVillageByCityName!=null){
				findCityDataStatisticsByCityId.setCityVillagecount(findVillageByCityName.size());
			}else{
				findCityDataStatisticsByCityId.setCityVillagecount(0);
			}
			if(findTinyVillageByCityName!=null){
				findCityDataStatisticsByCityId.setCityTinyvillagecount(findTinyVillageByCityName.size());
			}else{
				findCityDataStatisticsByCityId.setCityTinyvillagecount(0);
			}
			findCityDataStatisticsByCityId.setCityBuildingcount(buildingDao.findBuildingByCityName(distCityCode.getCityname()));
			findCityDataStatisticsByCityId.setCityHousecount(houseDao.findHouseByCityName(distCityCode.getCityname()));
			preObject(findCityDataStatisticsByCityId);
			cityDataStatisticsManager.saveObject(findCityDataStatisticsByCityId);
			
		}
		StoreDataStatisticsManager storeDataStatisticsManager=(StoreDataStatisticsManager)SpringHelper.getBean("storeDataStatisticsManager");
		storeDataStatisticsManager.syncStoreDataStatistics();
	}
	
	

}
