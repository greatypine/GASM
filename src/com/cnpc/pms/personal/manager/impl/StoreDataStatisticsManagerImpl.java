package com.cnpc.pms.personal.manager.impl;

import java.util.List;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.dao.BuildingDao;
import com.cnpc.pms.personal.dao.HouseDao;
import com.cnpc.pms.personal.dao.TinyVillageDao;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreDataStatistics;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.StoreDataStatisticsManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.VillageManager;

public class StoreDataStatisticsManagerImpl extends BizBaseCommonManager implements StoreDataStatisticsManager{

	@Override
	public StoreDataStatistics findStoreDataStatisticsByStoreId(Long storeId) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("store_id",storeId));
        if(lst_data != null && lst_data.size() > 0){
            return (StoreDataStatistics)lst_data.get(0);
        }
        return null;
	}

	@Override
	public void syncStoreDataStatistics() {
		HouseDao houseDao=(HouseDao)SpringHelper.getBean(HouseDao.class.getName());
		BuildingDao buildingDao=(BuildingDao)SpringHelper.getBean(BuildingDao.class.getName());
		TinyVillageDao tinyVillageDao =(TinyVillageDao)SpringHelper.getBean(TinyVillageDao.class.getName());
		VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
		StoreDataStatisticsManager storeDataStatisticsManager=(StoreDataStatisticsManager)SpringHelper.getBean("storeDataStatisticsManager");
		//获取所有的门店
		StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
		List<Store> storeAll = storeManager.findStoreAll();
		for (Store store : storeAll) {
			StoreDataStatistics storeDataStatistics = findStoreDataStatisticsByStoreId(store.getStore_id());
			if(storeDataStatistics==null){
				storeDataStatistics=new StoreDataStatistics();
			}
			//获取门店所管辖的街道
			storeDataStatistics.setStore_id(store.getStore_id());
			storeDataStatistics.setName(store.getName());
			if(store.getTown_id()!=null&&!"".equals(store.getTown_id())){
				String[] split = store.getTown_id().split(",");
				storeDataStatistics.setStoreTowncount(split.length);
				List<Village> villageByTownIds = villageManager.findVillageByTownIds(store.getTown_id());
				if(villageByTownIds==null){
					storeDataStatistics.setStoreVillagecount(0);
				}else{
					storeDataStatistics.setStoreVillagecount(villageByTownIds.size());
				}
				List<TinyVillage> list = tinyVillageDao.findTinyVillageByTownIds(store.getTown_id());
				storeDataStatistics.setStoreTinyvillagecount(list.size());
				Integer idse = buildingDao.findBuildingByTownIdse(store.getTown_id());
				storeDataStatistics.setStoreBuildingcount(idse);
				Integer townIdse = houseDao.findHouseByTownIdse(store.getTown_id());
				storeDataStatistics.setStoreHousecount(townIdse);
			}else{
				storeDataStatistics.setStoreTowncount(0);
				storeDataStatistics.setStoreTinyvillagecount(0);
				storeDataStatistics.setStoreVillagecount(0);
				storeDataStatistics.setStoreBuildingcount(0);
				storeDataStatistics.setStoreHousecount(0);
			}
			preObject(storeDataStatistics);
			storeDataStatisticsManager.saveObject(storeDataStatistics);
		}
		
	}

}
