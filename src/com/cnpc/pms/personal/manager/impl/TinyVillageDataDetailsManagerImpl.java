package com.cnpc.pms.personal.manager.impl;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.dao.TinyVillageDao;
import com.cnpc.pms.personal.entity.City;
import com.cnpc.pms.personal.entity.County;
import com.cnpc.pms.personal.entity.DistCityCode;
import com.cnpc.pms.personal.entity.TinyArea;
import com.cnpc.pms.personal.entity.TinyVillageCode;
import com.cnpc.pms.personal.entity.TinyVillageDataDetails;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.CityManager;
import com.cnpc.pms.personal.manager.CountyManager;
import com.cnpc.pms.personal.manager.DistCityCodeManager;
import com.cnpc.pms.personal.manager.TinyAreaManager;
import com.cnpc.pms.personal.manager.TinyVillageCodeManager;
import com.cnpc.pms.personal.manager.TinyVillageDataDetailsManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.personal.manager.VillageManager;

public class TinyVillageDataDetailsManagerImpl extends BizBaseCommonManager implements TinyVillageDataDetailsManager{

	@Override
	public TinyVillageDataDetails findTinyVillageDataDetailsByTinyId(Long tiny_village_id) {
    	List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tiny_village_id="+tiny_village_id));
    	if(lst_data!=null&&lst_data.size()>0){
    		return (TinyVillageDataDetails) lst_data.get(0);
    	}
		return null;
	}

	@Override
	public void syncTinyVillageDataDetails() {
		TinyAreaManager tinyAreaManager=(TinyAreaManager) SpringHelper.getBean("tinyAreaManager");
		VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
		TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
		CountyManager countyManager=(CountyManager) SpringHelper.getBean("countyManager");
		CityManager cityManager=(CityManager) SpringHelper.getBean("cityManager");
		TinyVillageCodeManager tinyVillageCodeManager=(TinyVillageCodeManager) SpringHelper.getBean("tinyVillageCodeManager");
		//获取所有小区数据
		TinyVillageDao tinyVillageDao=(TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		List<Map<String, Object>> list = tinyVillageDao.findTinyVillageDataDetails();
		if(list!=null&&list.size()>0){
			for (Map<String, Object> map : list) {
				Object tinyId = map.get("tinyvillageid");
				Long county_id=null;
				//根据小区id获取小区数据详情
				TinyVillageDataDetails detailsByTinyId = findTinyVillageDataDetailsByTinyId(Long.parseLong(tinyId+""));
				if(detailsByTinyId==null){
					detailsByTinyId=new TinyVillageDataDetails();
				}
				detailsByTinyId.setTiny_village_id(Long.parseLong(tinyId+""));
				if(map.get("tiny_village_type")!=null&&!"".equals(map.get("tiny_village_type"))){
					detailsByTinyId.setTiny_village_type(Integer.parseInt(map.get("tiny_village_type")+""));
				}
				detailsByTinyId.setTiny_village_name(map.get("tinyvillage_name")+"");
				detailsByTinyId.setBuilding_count(Integer.parseInt(map.get("sumbuilding")+""));
				detailsByTinyId.setHouse_count(Integer.parseInt(map.get("sumhouse")+""));
				//根据小区id获取所在社区和街道，如果社区不存在直接根据街道id获取街道
				if(map.get("village_id")!=null&&!"".equals(map.get("village_id"))){
					System.out.println(map.get("village_id"));
					Village village = villageManager.getVillageByIdInfo(Long.parseLong(map.get("village_id")+""));
					if(village!=null){
						Town town = townManager.getTownById(village.getTown_id());
						detailsByTinyId.setVillage_id(village.getId());
						detailsByTinyId.setVillage_name(village.getName());
						detailsByTinyId.setTown_id(town.getId());
						detailsByTinyId.setTown_name(town.getName());
						county_id=town.getCounty_id();
					}else{
						if(map.get("town_id")!=null&&!"".equals(map.get("town_id"))){
							Town town = townManager.getTownById(Long.parseLong(map.get("town_id")+""));
							detailsByTinyId.setVillage_id(null);
							detailsByTinyId.setVillage_name(null);
							detailsByTinyId.setTown_id(town.getId());
							detailsByTinyId.setTown_name(town.getName());
							county_id=town.getCounty_id();
						}
					}
					
					
				}else{
					if(map.get("town_id")!=null&&!"".equals(map.get("town_id"))){
						Town town = townManager.getTownById(Long.parseLong(map.get("town_id")+""));
						detailsByTinyId.setVillage_id(null);
						detailsByTinyId.setVillage_name(null);
						detailsByTinyId.setTown_id(town.getId());
						detailsByTinyId.setTown_name(town.getName());
						county_id=town.getCounty_id();
					}
				}
				//根据街道获取小区所在的区
				County county = countyManager.getCountyById(county_id);
				City city = cityManager.getCityById(county.getCity_id());
				detailsByTinyId.setCity_name(city.getName());
				TinyVillageCode villageCode = tinyVillageCodeManager.findTinyVillageCodeByTinyId(detailsByTinyId.getTiny_village_id());
				//查询小区编码
				if(villageCode!=null){
					detailsByTinyId.setTiny_village_code(villageCode.getCode());
				}
				//查询是否有录入坐标
				TinyArea tinyArea = tinyAreaManager.findTinyAreaByTinyId(detailsByTinyId.getTiny_village_id());
				if(tinyArea!=null){
					detailsByTinyId.setIfcoordinates("是");
				}else{
					detailsByTinyId.setIfcoordinates("否");
				}
				preObject(detailsByTinyId);
				this.saveObject(detailsByTinyId);
			}
		}
	}

	@Override
	public List<Map<String, Object>> findTinyVillageDetailsPage(TinyVillageDataDetails tinyVillageDataDetails,
			PageInfo pageInfo) {
		DistCityCodeManager distCityCodeManager=(DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		StringBuffer stringBuffer=new StringBuffer();
		if(tinyVillageDataDetails.getTown_name()!=null&&!"".equals(tinyVillageDataDetails.getTown_name())){
			stringBuffer.append(" and tin.town_name like '%"+tinyVillageDataDetails.getTown_name()+"%' ");
		}
		if(tinyVillageDataDetails.getVillage_name()!=null&&!"".equals(tinyVillageDataDetails.getVillage_name())){
			stringBuffer.append(" and tin.village_name like '%"+tinyVillageDataDetails.getVillage_name()+"%' ");
		}
		if(tinyVillageDataDetails.getStore_name()!=null&&!"".equals(tinyVillageDataDetails.getStore_name())){
			stringBuffer.append(" and s.name like '%"+tinyVillageDataDetails.getStore_name()+"%' ");
		}
		if(tinyVillageDataDetails.getIfcoordinates()!=null&&!"".equals(tinyVillageDataDetails.getIfcoordinates())){
			stringBuffer.append(" and tin.ifcoordinates= '"+tinyVillageDataDetails.getIfcoordinates()+"' ");
		}
		DistCityCode distCityCode = distCityCodeManager.queryDistCityCodeByCode(tinyVillageDataDetails.getCityId());
		List<Map<String, Object>> tinyDetails = storeDao.findTinyDetails(distCityCode.getCityname(),stringBuffer.toString(),pageInfo);
		
		return tinyDetails;
	}

}
