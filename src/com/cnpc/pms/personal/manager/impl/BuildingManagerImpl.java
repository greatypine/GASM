package com.cnpc.pms.personal.manager.impl;


import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.BuildingDao;
import com.cnpc.pms.personal.dao.HouseDao;
import com.cnpc.pms.personal.entity.Building;
import com.cnpc.pms.personal.entity.City;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.House;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.BuildingManager;
import com.cnpc.pms.personal.manager.CityManager;
import com.cnpc.pms.personal.manager.HouseManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.personal.manager.VillageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 楼房相关业务实现类
 * Created by liuxiao on 2016/5/25 0025.
 */
public class BuildingManagerImpl extends BizBaseCommonManager implements BuildingManager {

    private static final String Building = null;

	/**
     * 根据小区主键和楼房名称获取楼房对象zz
     * @param l_tinyVillageId 小区主键
     * @param str_name 楼房名称
     * @return 楼房对象
     */
    @Override
    public Building getBuildingByName(Long l_tinyVillageId, String str_name) {
        List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tinyvillage_id",l_tinyVillageId)
                .appendAnd(FilterFactory.getSimpleFilter("name",str_name)).appendAnd(FilterFactory.getSimpleFilter("status",0)));

        if(lst_data != null && lst_data.size() > 0){
            return (Building)lst_data.get(0);
        }
        return null;
    }

    /**
     * 根据小区名获取所有的小区内的楼房Map，以楼房号做Key
     * @param l_tiny_village_id 小区ID
     * @return 楼房MAP集合
     */
    @Override
    public Map<String, Building> getBuildingMapByTinyVillageId(Long l_tiny_village_id) {

        Map<String,Building> map_building = new HashMap<String, Building>();
        List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tinyvillage_id",l_tiny_village_id).appendAnd(FilterFactory.getSimpleFilter("status",0)));
        if(lst_data != null && lst_data.size() > 0) {
            for (Object obj_data : lst_data) {
                Building obj_building = (Building)obj_data;
                map_building.put(obj_building.getName(),obj_building);
            }
        }
        return map_building;
    }

	@Override
	public Building getBuildingByBuildingId(Long id) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("id",id));
		if(list!=null&&list.size()>0){
			TinyVillageManager tinyVillageManager=(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
			Building building=(Building)list.get(0);
			TinyVillage tinyVillageById = tinyVillageManager.getTinyVillageById(building.getTinyvillage_id());
			if(tinyVillageById!=null){
				building.setTiny_village_name(tinyVillageById.getName());
				if(tinyVillageById.getVillage_id()!=null&&!"".equals(tinyVillageById.getVillage_id())){
					VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
					Village villageByIdInfo = villageManager.getVillageByIdInfo(tinyVillageById.getVillage_id());
					building.setVillage_id(villageByIdInfo.getId());
					building.setVillage_name(villageByIdInfo.getName());
					TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
					Town town = townManager.getTownById(villageByIdInfo.getTown_id());
					building.setTown_id(town.getId());
					building.setTown_name(town.getName());
				}else{
					TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
					Town town = townManager.getTownById(tinyVillageById.getTown_id());
					building.setTown_id(town.getId());
					building.setTown_name(town.getName());
				}
			}
			return building;
		}
		return null;
	}

    @Override
    public List<Building> getBuildingListByTinyVillageId(Long l_tiny_village_id) {
        List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tinyvillage_id",l_tiny_village_id).appendAnd(FilterFactory.getSimpleFilter("status",0)));
        if(lst_data != null && lst_data.size() > 0) {
            return (List<Building>)lst_data;
        }
        return new ArrayList<Building>();
    }

    @Override
    public Map<String, Object> queryBuilding(QueryConditions queryConditions) {
        BuildingDao buildingDao = (BuildingDao) SpringHelper.getBean(BuildingDao.class.getName());
        //查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = queryConditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();

        for (Map<String, Object> map_where : queryConditions.getConditions()) {
            if ("village_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" and tv.`name` like '").append(map_where.get("value")).append("'");
            }else if ("town_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" and towt.`name` like '").append(map_where.get("value")).append("'");
            }else if ("tiny_village_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" and ttv.`name` like '").append(map_where.get("value")).append("'");
            }else if ("name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" and tb.`name` like '").append(map_where.get("value")).append("'");
            }
        }
        User sessionUser = null;
  		if (null != SessionManager.getUserSession()
  				&& null != SessionManager.getUserSession().getSessionData()) {
  			sessionUser = (User) SessionManager.getUserSession()
  					.getSessionData().get("user");
  		}
        UserManager userManager=(UserManager)SpringHelper.getBean("userManager");
  		sessionUser=userManager.findUserById(sessionUser.getId());
  		StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
  		if(3224==sessionUser.getUsergroup().getId()){
  			//sb_where.append(" AND bus.employee_no='").append(sessionUser.getEmployeeId()).append("'");
  			Store store = storeManager.findStore(sessionUser.getStore_id());
  			if(store!=null){
  				if(store.getTown_id()!=null&&!store.getTown_id().equals("")){
  					sb_where.append(" AND towt.id in(").append(store.getTown_id()).append(")");
  				}else{
  					sb_where.append(" AND towt.id=").append(0);
  				}
  				
  			}
  		}else if(3231==sessionUser.getUsergroup().getId()||3223==sessionUser.getUsergroup().getId()||3225==sessionUser.getUsergroup().getId()||3229==sessionUser.getUsergroup().getId()){
  			Store store = storeManager.findStore(sessionUser.getStore_id());
  			if(store!=null){
  				if(store.getTown_id()!=null&&!store.getTown_id().equals("")){
  					sb_where.append(" AND towt.id in(").append(store.getTown_id()).append(")");
  				}else{
  					sb_where.append(" AND towt.id=").append(0);
  				}
  				
  			}
  		}else if(sessionUser.getZw().equals("地址采集")||sessionUser.getCode().equals("sunning1")){
			sb_where.append(" and 1=1 ");
		}else{
			//取得当前登录人 所管理城市
			String cityssql = "";
			List<DistCity> distCityList = userManager.getCurrentUserCity();
			if(distCityList!=null&&distCityList.size()>0){
				for(DistCity d:distCityList){
					cityssql += "'"+d.getCityname()+"',";
				}
				cityssql=cityssql.substring(0, cityssql.length()-1);
			}
			if(cityssql!=""&&cityssql.length()>0){
				String cityId = getCityId(cityssql);
				if(!"".equals(cityId)){
					cityId=cityId.substring(0, cityId.length()-1);
					sb_where.append(" and citt.id in ("+cityId+")");
				}else{
					sb_where.append(" and 0=1 ");
				}
				
			}else{
				sb_where.append(" and 0=1 ");
			}
		};
        System.out.println(sb_where);
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "楼房信息");
        map_result.put("data", buildingDao.queryBuilding(sb_where.toString(), obj_page));
        return map_result;
    }

	
	@Override
	public Building getBuildingByBuildingIdAndTinVillageId(Long id, Long tinvillageId) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tinyvillage_id",tinvillageId)
	                .appendAnd(FilterFactory.getSimpleFilter("id",id)).appendAnd(FilterFactory.getSimpleFilter("status",0)));

        if(lst_data != null && lst_data.size() > 0){
            return (Building)lst_data.get(0);
        }
	    return null;
	}

	@Override
	public Map<String, Object> queryBuildingData(QueryConditions queryConditions) {
		BuildingDao buildingDao = (BuildingDao) SpringHelper.getBean(BuildingDao.class.getName());
        //查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = queryConditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();
        for (Map<String, Object> map_where : queryConditions.getConditions()) {
        	if ("name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" and name like '").append(map_where.get("value")).append("'");
            }else if("atherbuildings".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))){
            	sb_where.append(" and id not in(").append(map_where.get("value")).append(")");
            }else if("tinyvillageid".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))){
            	sb_where.append(" and tinyvillage_id=").append(map_where.get("value"));
            }
        }
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "楼房信息");
        map_result.put("data", buildingDao.queryBuildingdata(sb_where.toString(), obj_page));
        return map_result;
        }

	@Override
	public List<Building> getBuildingDataByids(String ids) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("id in("+ids+")").appendAnd(FilterFactory.getSimpleFilter("status",0)));
        if(lst_data != null && lst_data.size() > 0) {
            return (List<Building>)lst_data;
        }
        return null;
	}
	/**
	 * 根据当前登录的用户获取所有的城市id
	 */
	public String getCityId(String string){
		CityManager cityManager=(CityManager)SpringHelper.getBean("cityManager");
		String[] cityName = string.split(",");
		String cityId="";
		for(int i=0;i<cityName.length;i++){
			List<City> list = cityManager.getCityDataByName(cityName[i].replaceAll("'", ""));
			if(list!=null&&list.size()>0){
				for (City city : list) {
					cityId+=city.getId()+",";
				}
			}
		}
		return cityId;
	}

	@Override
	public Building getBuildingByTinyvillageAndName(Building building) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tinyvillage_id="+building.getTinyvillage_id()+" and name='"+building.getName()+"' and status=0"));
        if(lst_data != null && lst_data.size() > 0) {
            return (Building)lst_data.get(0);
        }
        return null;
	}

	@Override
	public Building saveOrUpdateBuilding(Building building) {
		TinyVillageManager tinyVillageManager=(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
		BuildingManager buildingManager=(BuildingManager)SpringHelper.getBean("buildingManager");
		if(building.getId()!=null){
			TinyVillage tinyVillage = tinyVillageManager.getTinyVillageById(building.getTinyvillage_id());
			if(tinyVillage.getVillage_id()==null||"".equals(tinyVillage.getVillage_id())){
				tinyVillage.setVillage_id(building.getVillage_id());
				preObject(tinyVillage);
				tinyVillageManager.saveObject(tinyVillage);
			}
			Building buildingByBuildingId = getBuildingByBuildingId(building.getId());
			buildingByBuildingId.setVillage_id(building.getVillage_id());
			buildingByBuildingId.setTinyvillage_id(building.getTinyvillage_id());
			buildingByBuildingId.setName(building.getName());
			buildingByBuildingId.setType(1);
			preObject(buildingByBuildingId);
			buildingManager.saveObject(buildingByBuildingId);
			return buildingByBuildingId;
		}else{
			HouseManager houseManager=(HouseManager)SpringHelper.getBean("houseManager");
			TinyVillage tinyVillage = tinyVillageManager.getTinyVillageById(building.getTinyvillage_id());
			if(tinyVillage.getVillage_id()==null||"".equals(tinyVillage.getVillage_id())){
				tinyVillage.setVillage_id(building.getVillage_id());
				preObject(tinyVillage);
				tinyVillageManager.saveObject(tinyVillage);
			}
			building.setType(1);
			preObject(building);
			buildingManager.saveObject(building);
			House house = new House();
			house.setBuilding_unit_no(building.getBuilding_unit_no());
			house.setHouse_no(building.getHouse_no());
			house.setBuilding_house_no(building.getBuilding_house_no());
			house.setHouse_type(1);
			house.setTinyvillage_id(building.getTinyvillage_id());
			house.setBuilding_id(building.getId());
			house.setApprove_status(0);
			preObject(house);
			houseManager.saveObject(house);
			return building;
		}
	}

	@Override
	public Building findBuildingByTinyVillageIdAndName(Building building) {
		Building buildingByTinyvillageAndName = getBuildingByTinyvillageAndName(building);
		if(buildingByTinyvillageAndName==null){
			return null;
		}else{
			if(!building.getId().equals(buildingByTinyvillageAndName.getId())){
				return buildingByTinyvillageAndName;
			}
		}
		return null;
	}

	@Override
	public Integer findHouseById(Long id) {
		//根据楼房id查看改楼房信息下是否有房屋信息
		HouseManager houseManager =(HouseManager)SpringHelper.getBean("houseManager");
		List<House> list = houseManager.findHouseDataByBuilding(id);
		if(list!=null&&list.size()>0){
			return 1;
		}
		return 0;
	}

	@Override
	public Building deleteBuildingById(Long id) {
		Building building2 = getBuildingByBuildingId(id);
		building2.setStatus(1);
		preObject(building2);
		return building2;
	}

	@Override
	public List<Building> findBuildingByTinyvillageId(Long tinyvillage_id) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tinyvillage_id="+tinyvillage_id+" and type=1 and status=0 order by building_index"));
        if(lst_data != null && lst_data.size() > 0) {
            return (List<Building>)lst_data;
        }
        return null;
	}

	@Override
	public List<Building> findBuildingPingandbusinessByTinyvillageId(Long tinyvillage_id) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tinyvillage_id="+tinyvillage_id+" and status=0"));
        if(lst_data != null && lst_data.size() > 0) {
            return (List<Building>)lst_data;
        }
        return null;
	}

	@Override
	public List<Building> findHouseByBuild(String ids) {
		HouseManager houseManager =(HouseManager)SpringHelper.getBean("houseManager");
		String[] split = ids.split(",");
		List<Building> list = new ArrayList<Building>();
		for (String string : split) {
			List<House> listhouse = houseManager.findHouseDataByBuilding(Long.parseLong(string));
			if(listhouse!=null&&listhouse.size()>0){
				Building buildingId = getBuildingByBuildingId(Long.parseLong(string));
				list.add(buildingId);
			}
		}
		if(list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public Building deletemouthBuilding(String idString) {
		BuildingManager buildingManager =(BuildingManager)SpringHelper.getBean("buildingManager");
		String[] split = idString.split(",");
		for (String string : split) {
			Building byBuildingId = getBuildingByBuildingId(Long.parseLong(string));
			byBuildingId.setStatus(1);
			preObject(byBuildingId);
			buildingManager.saveObject(byBuildingId);
		}
		return null;
	}

	@Override
	public Building saveBuilding(Building building) {
		BuildingManager buildingManager =(BuildingManager)SpringHelper.getBean("buildingManager");
		//根据小区id查询小区信息
		TinyVillageManager tinyVillageManager=(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
		TinyVillage tinyVillage = tinyVillageManager.getTinyVillageById(building.getTinyvillage_id());
		//修改楼房
		if(building.getId()!=null&&!"".equals(building.getId())){
			//根据楼房id查询楼房
			com.cnpc.pms.personal.entity.Building buildingByBuildingId = getBuildingByBuildingId(building.getId());
			buildingByBuildingId.setName(building.getName());
			buildingByBuildingId.setVillage_id(tinyVillage.getVillage_id());
			buildingByBuildingId.setTinyvillage_id(building.getTinyvillage_id());
			buildingByBuildingId.setType(1);
			preObject(buildingByBuildingId);
			buildingManager.saveObject(buildingByBuildingId);
			return buildingByBuildingId;
		}else{
			building.setVillage_id(tinyVillage.getVillage_id());
			preObject(building);
			buildingManager.saveObject(building);
		}
		
		return building;
	}

	@Override
	public Building findBuildingBusiness(Building building) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tinyvillage_id="+building.getTinyvillage_id()+" and name='"+building.getName()+"' and type=2 and status=0"));
        if(lst_data != null && lst_data.size() > 0) {
            return (Building)lst_data.get(0);
        }
        return null;
	}

	@Override
	public Building deleteBusiness(Long building_id) {
		BuildingManager buildingManager=(BuildingManager)SpringHelper.getBean("buildingManager");
		Building byBuildingId = getBuildingByBuildingId(building_id);
		byBuildingId.setStatus(1);
		preObject(byBuildingId);
		buildingManager.saveObject(byBuildingId);
		HouseManager houseManager =(HouseManager)SpringHelper.getBean("houseManager");
		List<House> findBusinessBuliding = houseManager.findBusinessByBuilding_id(building_id);
		if(findBusinessBuliding!=null&&findBusinessBuliding.size()>0){
			for (House house : findBusinessBuliding) {
				house.setStatus(1);
				preObject(house);
				houseManager.saveObject(house);
			}
		}
		return byBuildingId;
	}

	@Override
	public Building addBuildingList(List<Map<String, Object>> list) {
		Building building=null;
		if(list!=null&&list.size()>0){
			for (Map<String, Object> map : list) {
				Object building_id = map.get("id");
				if(building_id!=null&&!"".equals(building_id)){
					building=getBuildingByBuildingId(Long.parseLong(building_id+""));
				}else{
					building=new Building();
				}
				building.setName(map.get("name")+"");
				building.setTinyvillage_id(Long.parseLong(map.get("tinyvillage_id")+""));
				if(map.get("village_id")!=null&&!"".equals(map.get("village_id"))){
					building.setVillage_id(Long.parseLong(map.get("village_id")+""));
				}
				building.setBuilding_index(Integer.parseInt(map.get("index")+""));
				building.setType(1);
				preObject(building);
				this.save(building);
			}
		}
		return building;
	}

	@Override
	public List<Building> findBussinessByTinyId(Long tinyId) {
		List<Building> list=null;
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tinyvillage_id="+tinyId+" and type=2 and status=0 order by building_index"));
        if(lst_data != null && lst_data.size() > 0) {
        	list=new ArrayList<Building>();
        	HouseDao houseDao =(HouseDao)SpringHelper.getBean(HouseDao.class.getName());
        	for (Object object : lst_data) {
        		Building building=	(Building)object;
        		//根据商业楼宇查询最大楼层
        		String string = houseDao.findBusinessMaxHouseNo(building.getId(),tinyId);
        		if(string!=null&&!"".equals(string)){
        			building.setHouse_no(string);
        			list.add(building);
        		}
			}
        }
        return list;
	}

	@Override
	public Building addBusinessData(List<Map<String, Object>> list) {
		HouseManager houseManager=(HouseManager)SpringHelper.getBean("houseManager");
		//根据小区id和商业楼宇名称查找商业楼宇
		Building building=null;
		if(list!=null&&list.size()>0){
			for (Map<String, Object> map : list) {
				Object building_id = map.get("building_id");
				if(building_id!=null&&!"".equals(building_id)){
					//根据id查询楼房商业楼宇
					building = getBuildingByBuildingId(Long.parseLong(building_id+""));
				}else{
					building=new Building();
					building.setTinyvillage_id(Long.parseLong(map.get("tinyvillage_id")+""));
					building.setName(map.get("building_name")+"");
					Building findBuildingBusiness = findBuildingBusiness(building);
					if(findBuildingBusiness!=null){
						building=findBuildingBusiness;
					}
				}
				building.setTinyvillage_id(Long.parseLong(map.get("tinyvillage_id")+""));
				building.setName(map.get("building_name")+"");
				building.setVillage_id(map.get("village_id")!=null&&!"".equals(map.get("village_id"))?Long.parseLong(map.get("village_id")+""):null);
				building.setType(2);
				building.setBuilding_index(Integer.parseInt(map.get("index")+""));
				this.saveObject(building);
				HouseDao houseDao=(HouseDao)SpringHelper.getBean(HouseDao.class.getName());
				String string = houseDao.findBusinessMaxHouseNo(building.getId(),building.getTinyvillage_id());
				if(string==null||!string.equals(map.get("house_no"))){
					List<House> findBusinessBuliding = houseManager.findBusinessByBuilding_id(building.getId());
					if(findBusinessBuliding!=null&&findBusinessBuliding.size()>0){
						for (House house : findBusinessBuliding) {
							house.setStatus(1);
							preObject(house);
							houseManager.saveObject(house);
						}
					}
					House house=null;
					for(int i=1;i<=Integer.parseInt(map.get("house_no")+"");i++){
						house=new House();
						house.setHouse_type(2);
						house.setTinyvillage_id(Long.parseLong(map.get("tinyvillage_id")+""));
						house.setBuilding_id(building.getId());
						house.setBuilding_room_number("0");
						house.setApprove_status(0);
						house.setHouse_no(i+"");
						preObject(house);
						houseManager.saveObject(house);
				}
				}
			}
		}
		return building;
	}

	@Override
	public Building findBuildingBynameAndTinyIdAndtype(Building building) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tinyvillage_id="+building.getTinyvillage_id()+" and name='"+building.getName()+"' and type="+building.getType()+" and status=0"));
        if(lst_data != null && lst_data.size() > 0) {
            return (Building)lst_data.get(0);
        }
        return null;
	}
	
	
	
	
}
