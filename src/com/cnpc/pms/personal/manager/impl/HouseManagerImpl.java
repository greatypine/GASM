package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.dao.HouseCustomerDao;
import com.cnpc.pms.personal.dao.HouseDao;
import com.cnpc.pms.personal.dao.NewsDao;
import com.cnpc.pms.personal.dao.ProvinceDao;
import com.cnpc.pms.personal.entity.Building;
import com.cnpc.pms.personal.entity.City;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.House;
import com.cnpc.pms.personal.entity.HouseCustomer;
import com.cnpc.pms.personal.entity.HouseStyle;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.BuildingManager;
import com.cnpc.pms.personal.manager.CityManager;
import com.cnpc.pms.personal.manager.HouseCustomerManager;
import com.cnpc.pms.personal.manager.HouseManager;
import com.cnpc.pms.personal.manager.HouseStyleManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TinyVillageCodeManager;
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.utils.ValueUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;


/**
 * 房屋相关实现类
 * Created by liuxiao on 2016/5/25 0025.
 */
public class HouseManagerImpl extends BizBaseCommonManager implements HouseManager {

    /**
     * 根据楼房编码和房间号查找房屋对象
     * @param buliding_id 楼房编号
     * @param house_no 房间号
     * @return 房屋对象
     */
    @Override
    public House getHouseByBuildingAndNo(Long buliding_id, String house_no,String unit_no) {
        List<?> lst_data = getList(FilterFactory.getSimpleFilter("building_id",buliding_id)
                .appendAnd(FilterFactory.getSimpleFilter("building_house_no",house_no)).appendAnd(FilterFactory.getSimpleFilter("building_unit_no",unit_no)).appendAnd(FilterFactory.getSimpleFilter("status",0)));
        if(lst_data != null && lst_data.size() > 0 ){
            return (House)lst_data.get(0);
        }
        return null;
    }

    /**
     * 根据楼房id获取所有的房间和户型
     * @param map_building 楼房id集合
     * @return 房间map集合，以“单元号，房间号”作为主键
     */
    @Override
    public Map<Long, Map<String,House>> getHouseMapByBuildingId(Map<String,Building> map_building) {

		Map<Long, Map<String,House>> map_result = new HashMap<Long, Map<String, House>>();

        List<Long> lst_building_id = new ArrayList<Long>();

        for(Building obj_building : map_building.values()){
            lst_building_id.add(obj_building.getId());
        }
        if(lst_building_id.size() == 0){
            return map_result;
        }
        List<?> lst_data = this.getList(FilterFactory.getInFilter("building_id",lst_building_id).appendAnd(FilterFactory.getSimpleFilter("status",0)));
        if(lst_data != null && lst_data.size() > 0) {
            for (Object obj_data : lst_data) {
                House obj_house = (House)obj_data;
				if(!map_result.containsKey(obj_house.getBuilding_id())){
					map_result.put(obj_house.getBuilding_id(),new HashMap<String, House>());
				}
				map_result.get(obj_house.getBuilding_id()).put(obj_house.getBuilding_unit_no()+","+obj_house.getHouse_no(),obj_house);
            }
        }
        return map_result;
    }

    /**
     * 根据平房的楼房id获取
     * @param building_id 楼房id
     * @return 房屋对象
     */
    @Override
    public House getBungalowHouseByBuildingId(Long building_id) {
        List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("building_id",building_id).appendAnd(FilterFactory.getSimpleFilter("status",0)));
        if(lst_data != null && lst_data.size() > 0){
            return (House)lst_data.get(0);
        }
        return null;
    }

	@Override
	public Map<String, String> getHouse(Long id){
		Map<String,String> map = new HashMap<String, String>();
		HouseDao houseDao=(HouseDao)SpringHelper.getBean(HouseDao.class.getName());
		House house = houseDao.getHouse(id);
		if(house!=null){
			 map.put("address", house.getAddress());
			 map.put("id", house.getId().toString());
			 if(house.getTinyvillage_id()!=null&&house.getTinyvillage_id()>0){
					TinyVillageManager tinyVillageManager=(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
				 TinyVillage tinyVillageById = tinyVillageManager.getTinyVillageById(house.getTinyvillage_id());
				 if(tinyVillageById!=null&&tinyVillageById.getTown_id()!=null&&!"".equals(tinyVillageById.getTown_id())){
					 ProvinceDao provinceDao=(ProvinceDao)SpringHelper.getBean(ProvinceDao.class.getName());
					 String cityName = provinceDao.getProvinceByTown_id(tinyVillageById.getTown_id());
					 if(cityName!=null&&cityName.length()>0){
						 map.put("cityName", cityName);
					 }else{
						 map.put("cityName", "全国");
					 }
				 }else{
					 map.put("cityName", "全国");
				 }
			 }else{
				 map.put("cityName", "全国");
			 }
			 return map;
		}
		return null;
	}

	@Override
	public House getHouseById(Long id) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("id",id).appendAnd(FilterFactory.getSimpleFilter("status",0)));
        if(lst_data != null && lst_data.size() > 0){
            return (House)lst_data.get(0);
        }
		return null;
	}

	@Override
	public House saveHouseMapConn(House source_house) {
		BuildingManager buildingManager=(BuildingManager)SpringHelper.getBean("buildingManager");
		House house = this.getHouseById(source_house.getId());
		house.setBaidu_coordinate_x(source_house.getBaidu_coordinate_x());
		house.setBaidu_coordinate_y(source_house.getBaidu_coordinate_y());
		house.setGaode_coordinate_x(source_house.getGaode_coordinate_x());
		house.setGaode_coordinate_y(source_house.getGaode_coordinate_y());
		house.setSogou_coordinate_x(source_house.getSogou_coordinate_x());
		house.setSogou_coordinate_y(source_house.getSogou_coordinate_y());
		this.save(house);
		if(house.getBuilding_id()!=null&&house.getBuilding_id()>0){
			Building building = buildingManager.getBuildingByBuildingId(house.getBuilding_id());
			if(building!=null&&building.getBaidu_coordinate_x()==null&&building.getGaode_coordinate_x()==null&&building.getSogou_coordinate_x()==null){
				building.setBaidu_coordinate_x(source_house.getBaidu_coordinate_x());
				building.setBaidu_coordinate_y(source_house.getBaidu_coordinate_y());
				building.setGaode_coordinate_x(source_house.getGaode_coordinate_x());
				building.setGaode_coordinate_y(source_house.getGaode_coordinate_y());
				building.setSogou_coordinate_x(source_house.getSogou_coordinate_x());
				building.setSogou_coordinate_y(source_house.getSogou_coordinate_y());
				buildingManager.save(building);
			}
		}
		return house;
	}

	@Override
	public Map<String, Object> showHouseData(QueryConditions conditions) {
		HouseDao houseDao = (HouseDao) SpringHelper.getBean(HouseDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();

        for (Map<String, Object> map_where : conditions.getConditions()) {
            if ("village_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND vill.name like '").append(map_where.get("value")).append("'");
            }
            if ("tinyvillage_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND tiny.name like '").append(map_where.get("value")).append("'");
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
      					sb_where.append(" AND town.id in(").append(store.getTown_id()).append(")");
      				}else{
      					sb_where.append(" AND town.id=").append(0);
      				}
      				
      			}
      		}else if(3231==sessionUser.getUsergroup().getId()||3223==sessionUser.getUsergroup().getId()||3225==sessionUser.getUsergroup().getId()||3229==sessionUser.getUsergroup().getId()){
      			Store store = storeManager.findStore(sessionUser.getStore_id());
      			if(store!=null){
      				if(store.getTown_id()!=null&&!store.getTown_id().equals("")){
      					sb_where.append(" AND town.id in(").append(store.getTown_id()).append(")");
      				}else{
      					sb_where.append(" AND town.id=").append(0);
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
    					sb_where.append(" and city.id in ("+cityId+")");
    				}else{
    					sb_where.append(" and 0=1 ");
    				}
    				
    			}else{
    				sb_where.append(" and 0=1 ");
    			}
    		};
        System.out.println(sb_where);
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "房屋信息");
        map_result.put("data", houseDao.getHouseList(sb_where.toString(), obj_page));
        return map_result;
	}

	@Override
	public Map<String,Object> getHouseListByBuildingId(String str_building_id) {
		List<?> list = super.getList(FilterFactory.getSimpleFilter("building_id",Long.valueOf(str_building_id)).appendAnd(FilterFactory.getSimpleFilter("status",0)));
		Map<String,Object> map_result = new HashMap<String,Object>();
		List<String> lst_unit = new ArrayList<String>();
		Map<String,List<House>> map_house = new HashMap<String, List<House>>();
		if(list != null && list.size() > 0){
			for(Object obj : list){
				House obj_house = (House)obj;
				if(!lst_unit.contains(obj_house.getBuilding_unit_no())){
					lst_unit.add(obj_house.getBuilding_unit_no());
					map_house.put(obj_house.getBuilding_unit_no(),new ArrayList<House>());
				}
				map_house.get(obj_house.getBuilding_unit_no()).add(obj_house);
			}
			map_result.put("unit",lst_unit);
			map_result.put("house",map_house);
		}
		return map_result;
	}

    @Override
    public House getHouseByBuildingId(String str_building_id,String unit_no, String house_no) {
        Object obj_house = super.getUniqueObject(FilterFactory.getSimpleFilter("building_id",Long.valueOf(str_building_id))
                .appendAnd(FilterFactory.getSimpleFilter("building_unit_no",unit_no))
                .appendAnd(FilterFactory.getSimpleFilter("building_house_no",house_no)).appendAnd(FilterFactory.getSimpleFilter("status",0)));
        if(obj_house == null){
            return null;
        }
        return (House)obj_house;
    }

	@Override
	public Map<String, Object> getPageInfoHouse(QueryConditions conditions) {
		HouseDao houseDao=(HouseDao)SpringHelper.getBean(HouseDao.class.getName());
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "错误信息");
        map_result.put("data", houseDao.getHouseListInfomation(null, obj_page));
        return map_result;
	}

	@Override
	public Map<String, Object> getHouseInformation(Long id) {
		HouseDao houseDao=(HouseDao)SpringHelper.getBean(HouseDao.class.getName());
		Map<String, Object> map = houseDao.getHouseInfomation(id);
		return map;
	}

	@Override
	public House updateHouse(House house) {
		House save_house=null;
		if(house.getId()!=null){
			save_house=(House)this.getObject(house.getId());
		}else{
			save_house=new House();
		}
		save_house.setAddress(house.getAddress());
		save_house.setBaidu_coordinate_x(house.getBaidu_coordinate_x());
		save_house.setBaidu_coordinate_y(house.getBaidu_coordinate_y());
		save_house.setSogou_coordinate_x(house.getSogou_coordinate_x());
		save_house.setSogou_coordinate_y(house.getSogou_coordinate_y());
		save_house.setGaode_coordinate_x(house.getGaode_coordinate_x());
		save_house.setGaode_coordinate_y(house.getGaode_coordinate_y());
		preObject(save_house);
		save(save_house);
		return save_house;
	}

	@Override
	public Result getHouseApp(House house) {
		Map<String ,Object> mapdata=null;
		VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
		TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
		HouseDao houseDao=(HouseDao)SpringHelper.getBean(HouseDao.class.getName());
		TinyVillageManager tinyVillageManager=(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
		BuildingManager buildingManager=(BuildingManager)SpringHelper.getBean("buildingManager");
		Result result = new Result();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			StringBuffer stringBuffer=new StringBuffer();
			StringBuffer stringBuffer1=new StringBuffer();
	        if(ValueUtil.checkValue(house.getTinyvillage_name())){
	        	stringBuffer.append(" AND tiny.name='"+house.getTinyvillage_name()+"'");
	        }
	        if(ValueUtil.checkValue(house.getBuilding_name())){
	        	stringBuffer.append(" AND build.name='"+house.getBuilding_name()+"'");
	        }
	        if(ValueUtil.checkValue(house.getHouse_type())){
	        	stringBuffer.append(" AND house.house_type="+house.getHouse_type());
	        }
	        if(ValueUtil.checkValue(house.getBuilding_unit_no())){//单元
	        	stringBuffer.append(" AND house.building_unit_no='"+house.getBuilding_unit_no()+"'");
	        	stringBuffer1.append(" AND building_unit_no='"+house.getBuilding_unit_no()+"'");
	        }
	        if(ValueUtil.checkValue(house.getHouse_no())){//楼层
	        	stringBuffer.append(" AND house.house_no='"+house.getHouse_no()+"'");
	        	stringBuffer1.append(" AND house_no='"+house.getHouse_no()+"'");
	        }
	        if(ValueUtil.checkValue(house.getBuilding_house_no())){
	        	stringBuffer.append(" AND house.building_house_no='"+house.getBuilding_house_no()+"'");
	        	stringBuffer1.append(" AND building_house_no='"+house.getBuilding_house_no()+"'");
	        }
	        if(ValueUtil.checkValue(house.getAddress())){
	        	stringBuffer.append(" AND house.address='"+house.getAddress()+"'");
	        	stringBuffer1.append(" AND address='"+house.getAddress()+"'");
	        }
	        	         List<House> housegroup = houseDao.getHousegroup(stringBuffer.toString());
	        result.setCode(CodeEnum.success.getValue());
	        result.setMessage(CodeEnum.success.getDescription());
	       if(housegroup == null || housegroup.size() == 0){
	    	  // mapdata=new HashMap<String, Object>();
	    	  // list.add(mapdata);
	            result.setHouseList(list);
	        }else{
	        	//List<House> listHouse = new ArrayList<House>();
	        	for (House house2 : housegroup) {
	        		mapdata=new HashMap<String, Object>();
	        		List<House> condition =null;
	        		if(house.getHouse_type()==1){
		        		 condition = houseDao.getHouseByCondition(house2.getTinyvillage_id(), house2.getBuilding_id(),stringBuffer1.toString());
	        		}else if(house.getHouse_type()==2){
		        		 condition = houseDao.getHouseByConditionBySHangye(house2.getTinyvillage_id(), house2.getBuilding_id(),stringBuffer1.toString());
	        		}else if(house.getHouse_type()==0){
		        		 condition = houseDao.getHouseByCondition(house2.getTinyvillage_id(), house2.getBuilding_id(),stringBuffer1.toString());
	        		}
	        		Building building = buildingManager.getBuildingByBuildingId(house2.getBuilding_id());
	        		mapdata.put("houseData", condition);
	        		TinyVillage tinyVillage = tinyVillageManager.getTinyVillageById(house2.getTinyvillage_id());
	        		if(tinyVillage==null){
	        			result.setHouseList(list);
	        		}else if(tinyVillage.getVillage_id()!=null){
	        			Map<String, Object> map = villageManager.getVillageTownInfoByVillage_id(tinyVillage.getVillage_id());
	        			mapdata.put("province_name", map.get("province_name").toString());
        				mapdata.put("province_id", Long.valueOf(String.valueOf(map.get("province_id").toString())).longValue());
        				mapdata.put("city_id", Long.valueOf(String.valueOf(map.get("city_id").toString())).longValue());
        				mapdata.put("city_name", map.get("city_name").toString());
        				mapdata.put("village_id", Long.valueOf(String.valueOf(map.get("village_id").toString())).longValue());
        				mapdata.put("village_name", map.get("village_name").toString());
        				mapdata.put("town_id", Long.valueOf(String.valueOf(map.get("town_id").toString())).longValue());
        				mapdata.put("town_name", map.get("town_name").toString());
        				mapdata.put("county_id", Long.valueOf(String.valueOf(map.get("county_id").toString())).longValue());
        				mapdata.put("county_name", map.get("county_name").toString());
        				mapdata.put("tinyvillage_id", tinyVillage.getId());
        				mapdata.put("tinyvillage_name", tinyVillage.getName());
        				mapdata.put("tinyvillage_address", tinyVillage.getAddress());
        				 if(building!=null){
        					 mapdata.put("building_id", building.getId());
        					 mapdata.put("building_name", building.getName());
        				 }
        				 list.add(mapdata);

	        		}else if(tinyVillage.getTown_id()!=null){
	        			Map<String, Object> map = townManager.getTownParentInfoByTown_id(tinyVillage.getTown_id());
	        			mapdata.put("province_name", map.get("province_name").toString());
        				mapdata.put("province_id", Long.valueOf(String.valueOf(map.get("province_id").toString())).longValue());
        				mapdata.put("city_id", Long.valueOf(String.valueOf(map.get("city_id").toString())).longValue());
        				mapdata.put("city_name", map.get("city_name").toString());
        				mapdata.put("town_id", Long.valueOf(String.valueOf(map.get("town_id").toString())).longValue());
        				mapdata.put("town_name", map.get("town_name").toString());
        				mapdata.put("county_id", Long.valueOf(String.valueOf(map.get("county_id").toString())).longValue());
        				mapdata.put("county_name", map.get("county_name").toString());
        				mapdata.put("tinyvillage_id", tinyVillage.getId());
        				mapdata.put("tinyvillage_name", tinyVillage.getName());
        				mapdata.put("tinyvillage_address", tinyVillage.getAddress());
        				 if(building!=null){
        					 mapdata.put("building_id", building.getId());
        					 mapdata.put("building_name", building.getName());
        				 }
        				 list.add(mapdata);
	        		}
	        	}
	        	result.setHouseList(list);
	        } 	
		return result;
	}

	@Override
	public Result saveOrUpdate(List<Map<String, Object>> list){
		Result result = new Result();
		TinyVillageCodeManager tinyVillageCodeManager=(TinyVillageCodeManager)SpringHelper.getBean("tinyVillageCodeManager");
		NewsDao newsDao=(NewsDao)SpringHelper.getBean(NewsDao.class.getName());
		HouseStyleManager houseStyleManager=(HouseStyleManager)SpringHelper.getBean("houseStyleManager");
		HouseDao houseDao=(HouseDao)SpringHelper.getBean(HouseDao.class.getName());
		TinyVillageManager tinyVillageManager=(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
		BuildingManager buildingManager=(BuildingManager)SpringHelper.getBean("buildingManager");
		String string="";//删除的房屋id集合
		House saveHouse=null;
		HouseManager houseManager=(HouseManager)SpringHelper.getBean("houseManager");
		if(list!=null){
			House house=null;
			for (int i=0;i<list.size();i++){
				house=new House();
				Map<String, Object> map = list.get(i);
				TinyVillage tinyVillage = tinyVillageManager.getTinyVilageByName(Long.parseLong(map.get("town_id")+""), map.get("tingvillage_name")+"");
				Building building =null;
				if(map.get("house_type").equals("0")||map.get("house_type").equals("2")){
					 building = buildingManager.getBuildingByName(tinyVillage!=null?tinyVillage.getId():null,map.get("tingvillage_name")+"");
				}else{
					 building = buildingManager.getBuildingByName(tinyVillage!=null?tinyVillage.getId():null,map.get("building_name")+"");
				}
				if(tinyVillage==null){
					tinyVillage=new TinyVillage();
					tinyVillage.setName(map.get("tingvillage_name")+"");
					tinyVillage.setVillage_id(!map.get("village_id").equals("")?Long.parseLong(map.get("village_id")+""):null);
					tinyVillage.setTown_id(map.get("town_id")!=null?Long.parseLong(map.get("town_id")+""):null);
					tinyVillage.setCreate_time(new Date());
					if(map.get("house_type").equals("1")){
						tinyVillage.setAddress(map.get("tingvillage_address")+"");
					}else if(map.get("house_type").equals("2")){
						tinyVillage.setAddress(map.get("address")+"");
					}
					tinyVillage.setCreate_user(map.get("create_user")+"");
					tinyVillage.setCreate_user_id(Long.parseLong(map.get("create_user_id")+""));
					tinyVillage.setTinyvillage_type(Integer.parseInt(map.get("house_type")+""));
					tinyVillageManager.saveObject(tinyVillage);
					tinyVillageCodeManager.saveTinyVillageCode(tinyVillage);
				}else{
					tinyVillage.setVillage_id(!map.get("village_id").equals("")?Long.parseLong(map.get("village_id")+""):null);
					if(map.get("house_type").equals("1")){
						tinyVillage.setAddress(map.get("tingvillage_address")+"");
					}else if(map.get("house_type").equals("2")){
						tinyVillage.setAddress(map.get("address")+"");
					}
					tinyVillageManager.saveObject(tinyVillage);
					tinyVillageCodeManager.saveTinyVillageCode(tinyVillage);
				}
				if(building==null&&Integer.parseInt(map.get("house_type")+"")>0){
					building=new Building();
					building.setType(Integer.parseInt(map.get("house_type")+""));
					building.setName(map.get("building_name")+"");
					building.setTinyvillage_id(tinyVillage.getId());
					building.setVillage_id(!map.get("village_id").equals("")?Long.parseLong(map.get("village_id")+""):null);
					buildingManager.saveObject(building);
				}
				house.setTinyvillage_id(tinyVillage!=null?tinyVillage.getId():null);
				house.setBuilding_id(building!=null?building.getId():0);
				house.setVillage_id(!map.get("village_id").equals("")?Long.parseLong(map.get("village_id")+""):null);
				house.setTown_id(!map.get("town_id").equals("")?Long.parseLong(map.get("town_id")+""):null);
				house.setTinyvillage_name(map.get("tinyvillage_name")+"");
				house.setBuilding_name(map.get("building_name")+"");
				house.setBuilding_unit_no(map.get("building_unit_no")+"");
				house.setHouse_type(Integer.parseInt(map.get("house_type")+""));
				house.setBuilding_house_no(map.get("building_house_no")+"");
				house.setHouse_no(map.get("house_no")+"");
				if(!map.get("id").equals("")){
					house.setId(Long.parseLong(map.get("id")+""));
				}
				house.setApprove_status(0);
				House house2=null;
				if(map.get("house_type").equals("0")){
					if(house.getTinyvillage_id()!=null){
						house2=getHouseByBuildingIdAndHouse(0+"",map.get("house_no")+"",tinyVillage.getId());
					}
				}else if(map.get("house_type").equals("1")){
					if(house.getTinyvillage_id()!=null&&house.getBuilding_id()!=null){
					house2=getHouseByBuildingIdandvillage_id(house.getBuilding_id()+"",house.getBuilding_unit_no(),house.getBuilding_house_no(),house.getTinyvillage_id());
						}
					}else if(map.get("house_type").equals("2")){
						if(house.getTinyvillage_id()!=null&&house.getBuilding_id()!=null){
							house2=getHouseByBuildingIdandvillage_idShangyelo(house.getBuilding_id()+"",house.getHouse_no(),house.getTinyvillage_id());
								}
					}
				if(house.getId()!=null&&house.getId()>0){
					saveHouse = houseManager.getHouseById(house.getId());
				}else{
					saveHouse=new House();
					saveHouse.setCreate_user(map.get("create_user")+"");
					saveHouse.setCreate_user_id(Long.parseLong(map.get("create_user_id")+""));
				}
				BeanUtils.copyProperties(house, saveHouse
						,new String[]{"id","version","status","create_user","create_user_id","create_time"
									,"update_user","update_user_id","update_time","gaode_coordinate_x","gaode_coordinate_y","sogou_coordinate_x","sogou_coordinate_y","baidu_coordinate_x","baidu_coordinate_y","house_price","room_num"});
				saveHouse.setUpdate_user_id(Long.parseLong(map.get("create_user_id")+""));
				saveHouse.setUpdate_user(map.get("create_user")+"");
				if(house.getHouse_type().equals(1)){
					saveHouse.setAddress(map.get("tingvillage_name")+house.getBuilding_name()+house.getBuilding_unit_no()+"单元"+house.getBuilding_house_no()+"号");
				}else if(house.getHouse_type().equals(2)){
					saveHouse.setAddress(map.get("address")+"");
				}else if(house.getHouse_type().equals(0)){
					saveHouse.setAddress(map.get("tingvillage_name")+""+map.get("house_no")+"号");
				}
				preObject(saveHouse);
				//
				if(saveHouse.getEmployee_no()==null){
					saveHouse.setEmployee_no(map.get("employee_no")+"");
				}
				houseManager.saveObject(saveHouse);
				
				if(house2!=null){
					if(!saveHouse.getId().equals(house2.getId())){
						HouseStyle houseStyle = houseStyleManager.getHouseStyleByHouseId(house2.getId());
						if(houseStyle==null){
							newsDao.deleteObject(house2);
						}
					}
				}
				string+=","+saveHouse.getId();
			}
			/*if(string.length()>1){
				string=string.substring(1, string.length());
				houseDao.deleteHouse(string, saveHouse.getTinyvillage_id(), saveHouse.getBuilding_id());
			}*/
		}
		result.setCode(200);
		result.setMessage("保存成功");
		return result;
	}

	@Override
	public Result findHouseApp(List<Map<String, Object>> list) {
		Result res = new Result();
		HouseManager houseManager=(HouseManager)SpringHelper.getBean("houseManager");
		TinyVillageManager tinyVillageManager=(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
		BuildingManager buildingManager=(BuildingManager)SpringHelper.getBean("buildingManager");
		if(list!=null){
			for (int i=0;i<list.size();i++) {
				House house2=null;
				House house=new House();
				Map<String, Object> map = list.get(i);
				TinyVillage tinyVillage = tinyVillageManager.getTinyVilageByName(Long.parseLong(map.get("town_id")+""), map.get("tingvillage_name")+"");
				Building building=null;
				if(map.get("house_type").equals("0")||map.get("house_type").equals("2")){
					 building = buildingManager.getBuildingByName(tinyVillage!=null?tinyVillage.getId():null,map.get("tingvillage_name")+"");
				}else{
					 building = buildingManager.getBuildingByName(tinyVillage!=null?tinyVillage.getId():null,map.get("building_name")+"");
				}
				house.setTinyvillage_id(tinyVillage!=null?tinyVillage.getId():null);
				if(map.get("house_type").equals("0")){
					house.setBuilding_id(building!=null?building.getId():0);
				}else{
					house.setBuilding_id(building!=null?building.getId():null);
				}
				house.setVillage_id(!map.get("village_id").equals("")?Long.parseLong(map.get("village_id")+""):null);
				house.setTown_id(map.get("town_id")!=null?Long.parseLong(map.get("town_id")+""):null);
				house.setTinyvillage_name(map.get("tingvillage_name")+"");
				house.setBuilding_name(map.get("building_name")+"");
				house.setBuilding_unit_no(map.get("building_unit_no")+"");
				house.setHouse_type(Integer.parseInt(map.get("house_type")+""));
				house.setBuilding_house_no(map.get("building_house_no")+"");
				house.setHouse_no(map.get("house_no")+"");
				if(!map.get("id").equals("")){
					house.setId(Long.parseLong(map.get("id")+""));
				}
				//house.setId(!"".equals(map.get("id"))?Long.parseLong(map.get("id")+""):0);
				if(map.get("house_type").equals("0")){
					if(house.getTinyvillage_id()!=null){
						house2=getHouseByBuildingIdAndHouse(0+"",map.get("house_no")+"",house.getTinyvillage_id());
					}
				}else if(map.get("house_type").equals("1")){
					if(house.getTinyvillage_id()!=null&&house.getBuilding_id()!=null){
					house2=getHouseByBuildingIdandvillage_id(house.getBuilding_id()+"",house.getBuilding_unit_no(),house.getBuilding_house_no(),house.getTinyvillage_id());
						}
					}else if(map.get("house_type").equals("2")){
						if(house.getTinyvillage_id()!=null&&house.getBuilding_id()!=null){
							house2=getHouseByBuildingIdandvillage_idShangyelo(house.getBuilding_id()+"",house.getHouse_no(),house.getTinyvillage_id());
								}
					} 
				if(house2!=null){
					if(house.getId()!=null&&house.getId()>0){
						if(house2!=null&&!house2.getId().equals(house.getId())){
							res.setCode(300);
							res.setMessage( house.getAddress()+ "已存在");
							return res;
						}
					}
					if(house2.getId()!=null&&house.getId()==null){
						res.setCode(300);
						res.setMessage( house.getAddress()+ "已存在");
						return res;
					}
				}
			}
		}
		res.setCode(200);
		res.setMessage( ""+ "不存在");
		return res;
	}

	@Override
	public House getHouseByBuildingIdAndHouse(String str_building_id, String house_no,Long tinyvillage_id) {
		Object obj_house = super.getUniqueObject(FilterFactory.getSimpleFilter("building_id",Long.valueOf(str_building_id))
				.appendAnd(FilterFactory.getSimpleFilter("tinyvillage_id",tinyvillage_id))
                .appendAnd(FilterFactory.getSimpleFilter("house_no",house_no)).appendAnd(FilterFactory.getSimpleFilter("status",0)));
        if(obj_house == null){
            return null;
        }
        return (House)obj_house;
	}

	@Override
	public House getHouseByBuildingIdandvillage_id(String str_building_id, String unit_no, String house_no,
			Long tinyvillage_id) {
		Object obj_house = super.getUniqueObject(FilterFactory.getSimpleFilter("building_id",Long.valueOf(str_building_id))
				.appendAnd(FilterFactory.getSimpleFilter("building_unit_no",unit_no))
                .appendAnd(FilterFactory.getSimpleFilter("building_house_no",house_no))
				.appendAnd(FilterFactory.getSimpleFilter("tinyvillage_id",tinyvillage_id)).appendAnd(FilterFactory.getSimpleFilter("status",0)));
        if(obj_house == null){
            return null;
        }
        return (House)obj_house;
	}
	
	@Override
	public House getHouseByBuildingIdandvillage_idShangyelo(String str_building_id, String house_no,
			Long tinyvillage_id) {
		Object obj_house = super.getUniqueObject(FilterFactory.getSimpleFilter("building_id",Long.valueOf(str_building_id))
				.appendAnd(FilterFactory.getSimpleFilter("house_no",house_no))
				.appendAnd(FilterFactory.getSimpleFilter("tinyvillage_id",tinyvillage_id))
				.appendAnd(FilterFactory.getSimpleFilter("house_type",2)).appendAnd(FilterFactory.getSimpleFilter("status",0)));
        if(obj_house == null){
            return null;
        }
        return (House)obj_house;
	}

	@Override
	public House saveWebHouse(House house) {
		TinyVillageCodeManager tinyVillageCodeManager=(TinyVillageCodeManager)SpringHelper.getBean("tinyVillageCodeManager");
		House updateHouse=null;
		if(house.getId()!=null){
			updateHouse=getHouseById(house.getId());
		}else{
			updateHouse=new House();
		}
		HouseManager houseManager=(HouseManager)SpringHelper.getBean("houseManager");
		TinyVillageManager tinyVillageManager=(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
		BuildingManager buildingManager=(BuildingManager)SpringHelper.getBean("buildingManager");
		TinyVillage tinyVillage = tinyVillageManager.getTinyVilageByName(house.getTown_id(), house.getTinyvillage_name());
		if(tinyVillage==null){
			tinyVillage=new TinyVillage();
			tinyVillage.setName(house.getTinyvillage_name());
			tinyVillage.setAddress(house.getAddress());
			tinyVillage.setVillage_id(house.getVillage_id());
			tinyVillage.setTown_id(house.getTown_id());
			tinyVillage.setTinyvillage_type(house.getHouse_type());
			preObject(tinyVillage);
			tinyVillageManager.saveObject(tinyVillage);
			tinyVillageCodeManager.saveTinyVillageCode(tinyVillage);
		}else if(tinyVillage.getVillage_id()==null){
			tinyVillage.setVillage_id(house.getVillage_id());
			preObject(tinyVillage);
			tinyVillageManager.saveObject(tinyVillage);
			tinyVillageCodeManager.saveTinyVillageCode(tinyVillage);
		}
		updateHouse.setTinyvillage_id(tinyVillage.getId());
		Building buildingByName = buildingManager.getBuildingByName(tinyVillage.getId(), house.getBuilding_name());
		if(house.getHouse_type().equals(1)){
			if (buildingByName==null) {
				buildingByName=new Building();
				buildingByName.setType(1);
				buildingByName.setName(house.getBuilding_name());
				buildingByName.setTinyvillage_id(tinyVillage.getId());
				buildingByName.setVillage_id(house.getVillage_id());
				preObject(buildingByName);
				buildingManager.saveObject(buildingByName);
			}
			updateHouse.setBuilding_id(buildingByName.getId());
			updateHouse.setBuilding_house_no(house.getBuilding_house_no());
			updateHouse.setHouse_no(house.getHouse_no());
			updateHouse.setBuilding_unit_no(house.getBuilding_unit_no());
			updateHouse.setAddress(house.getTinyvillage_name()+house.getBuilding_name()+"号楼"+house.getBuilding_unit_no()+"单元"+house.getBuilding_house_no()+"号");
		}else if(house.getHouse_type().equals(2)){
			if (buildingByName==null) {
				buildingByName=new Building();
				buildingByName.setType(2);
				buildingByName.setName(house.getBuilding_name());
				buildingByName.setTinyvillage_id(tinyVillage.getId());
				buildingByName.setVillage_id(house.getVillage_id());
				preObject(buildingByName);
				buildingManager.saveObject(buildingByName);
			}
			updateHouse.setBuilding_id(buildingByName.getId());
			updateHouse.setHouse_no(house.getHouse_no());
			updateHouse.setAddress(house.getAddress()+house.getBuilding_name());
		}else{
			updateHouse.setBuilding_id(Long.parseLong(0+""));
			updateHouse.setAddress(house.getTinyvillage_name()+house.getHouse_no()+"号");
			updateHouse.setHouse_no(house.getHouse_no());
		}
		updateHouse.setHouse_type(house.getHouse_type());
		updateHouse.setApprove_status(0);
		preObject(updateHouse);
		houseManager.saveObject(updateHouse);
		return updateHouse;
	}

	@Override
	public House findWebHouse(House house) {
		HouseManager houseManager=(HouseManager)SpringHelper.getBean("houseManager");
		TinyVillageManager tinyVillageManager=(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
		BuildingManager buildingManager=(BuildingManager)SpringHelper.getBean("buildingManager");
		TinyVillage vilageByName = tinyVillageManager.getTinyVilageByName(house.getTown_id(), house.getTinyvillage_name());
		if(vilageByName==null){
			return null;
		}
		Building building = buildingManager.getBuildingByName(vilageByName.getId(), house.getBuilding_name());
		if ((house.getHouse_type().equals(1)||house.getHouse_type().equals(2))&&building==null) {
			return null;
		}
		House house2=null;
		if(house.getHouse_type().equals(1)){
			house2=getHouseByBuildingIdandvillage_id(building.getId()+"",house.getBuilding_unit_no(),house.getBuilding_house_no(),vilageByName.getId());
		}else if(house.getHouse_type().equals(2)){
			house2=getHouseByBuildingIdandvillage_idShangyelo(building.getId()+"",house.getHouse_no(),vilageByName.getId());
		}else if(house.getHouse_type().equals(0)){
			house2=getHouseByBuildingIdAndHouse(0+"",house.getHouse_no(),vilageByName.getId());
		}
		return house2;
	}

	@Override
	public House findHouseById(Long id) {
		TinyVillageManager tinyVillageManager=(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
		TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
		VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
		BuildingManager buildingManager=(BuildingManager)SpringHelper.getBean("buildingManager");
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("id",id));
        if(lst_data != null && lst_data.size() > 0){
        	House house2=(House)lst_data.get(0);
        	if(house2.getTinyvillage_id()!=null){
        		TinyVillage tinyVillageById = tinyVillageManager.getTinyVillageById(house2.getTinyvillage_id());
        		if(tinyVillageById!=null){
        			house2.setTinyvillage_name(tinyVillageById.getName());
        			house2.setAddress(tinyVillageById.getAddress());
        			house2.setVillage_id(tinyVillageById.getVillage_id());
        			house2.setTown_id(tinyVillageById.getTown_id());
        			if(tinyVillageById.getVillage_id()!=null&&!"".equals(tinyVillageById.getVillage_id())){
        				Village info = villageManager.getVillageByIdInfo(tinyVillageById.getVillage_id());
        				if(info!=null){
        					house2.setVillage_name(info.getName());
        				}
        			}
        			if(tinyVillageById.getTown_id()!=null&&!"".equals(tinyVillageById.getTown_id())){
        				Town townById = townManager.getTownById(tinyVillageById.getTown_id());
        				if(townById!=null){
        					house2.setTown_name(townById.getName());
        				}
        			}
        		}
        	}
        	if(house2.getBuilding_id()!=null&&!house2.getBuilding_id().equals(0)){
        		Building building = buildingManager.getBuildingByBuildingId(house2.getBuilding_id());
        		if(building!=null){
        			house2.setBuilding_name(building.getName());
        		}
        	}
        	return house2;
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
	public House queryHouseByTinVilligeAndHouseNo(Long tinVillageId, String house_no) {
		 List<?> lst_data = getList(FilterFactory.getSimpleFilter("tinyvillage_id",tinVillageId)
	                .appendAnd(FilterFactory.getSimpleFilter("house_no",house_no)).appendAnd(FilterFactory.getSimpleFilter("house_type",0)).appendAnd(FilterFactory.getSimpleFilter("status",0)));
         if(lst_data != null && lst_data.size() > 0 ){
            return (House)lst_data.get(0);
         }
         return null;
	}

	
	@Override
	public Map<String, Object> getHouseOfTinyVillage(Long tinVillageId) {
		 Map<String,Object> result = new HashMap<String,Object>();
		 List<?> lst_data = getList(FilterFactory.getSimpleFilter("tinyvillage_id",tinVillageId)
	                .appendAnd(FilterFactory.getSimpleFilter("house_type",0)).appendAnd(FilterFactory.getSimpleFilter("status",0)));
		 result.put("houselist", lst_data);
     
		 return result;
	}

	@Override
	public House deleteHouseById(Long id) {
		HouseManager houseManager =(HouseManager)SpringHelper.getBean("houseManager");
		House house = getHouseById(id);
		if(house!=null){
			house.setStatus(1);
		}
		preObject(house);
		houseManager.saveObject(house);
		return house;
	}

	@Override
	public Integer findHouseCustomerOrHouseStyle(Long houseid) {
		HouseCustomerManager houseCustomerManager=(HouseCustomerManager)SpringHelper.getBean("houseCustomerManager");
		//HouseStyleManager houseStyleManager =(HouseStyleManager)SpringHelper.getBean("houseStyleManager");
		//HouseCustomer customer = houseCustomerManager.findHouseCustomerByHouseId(houseid);
		//HouseStyle style = houseStyleManager.getHouseStyleByHouseId(houseid);
		HouseCustomerDao houseCustomerDao=(HouseCustomerDao)SpringHelper.getBean(HouseCustomerDao.class.getName());
		HouseCustomer customer = houseCustomerDao.findHouseCustomerByHouseId(houseid);
		if(customer!=null){
			return 1;
		}		
		return 0;
	}

	@Override
	public List<House> findHouseDataByBuilding(Long building) {
		List<?> lst_data = getList(FilterFactory.getSimpleFilter("building_id="+building+" and status=0"));
		if(lst_data!=null&&lst_data.size()>0){
			return (List<House>)lst_data;
		}
		return null;
	}

	@Override
	public List<House> findHouseDataBytinyvillageId(Long tinyVillageId) {
		List<?> lst_data = getList(FilterFactory.getSimpleFilter("status=0 and tinyvillage_id="+tinyVillageId));
        if(lst_data != null && lst_data.size() > 0 ){
            return (List<House>)lst_data;
        }
        return null;
	}

	@Override
	public List<House> findHouseByCustomer(String houseids) {
		HouseCustomerDao houseCustomerDao=(HouseCustomerDao)SpringHelper.getBean(HouseCustomerDao.class.getName());
		List<House> list = new ArrayList<House>();
		String[] split = houseids.split(",");
		//HouseCustomerManager houseCustomerManager=(HouseCustomerManager)SpringHelper.getBean("houseCustomerManager");
		for (String string : split) {
			//HouseCustomer customer = houseCustomerManager.findHouseCustomerByHouseId(Long.parseLong(string));
			HouseCustomer customer = houseCustomerDao.findHouseCustomerByHouseId(Long.parseLong(string));
			if(customer!=null){
				House house = findHouseById(Long.parseLong(string));
				list.add(house);
			}
		}
		if(list.size()>0){
			return list;
		}
		
		return null;
	}

	@Override
	public House deletemouthHouse(String houseids) {
		HouseManager houseManager =(HouseManager)SpringHelper.getBean("houseManager");
		String[] split = houseids.split(",");
		for (String string : split) {
			House house = getHouseById(Long.parseLong(string));
			if(house!=null){
				house.setStatus(1);
			}
			preObject(house);
			houseManager.saveObject(house);
		}
		return null;
	}

	@Override
	public List<House> findHouseDataBytinyvillageIdandhouseType(Long tinyvillageId) {
		List<?> lst_data = getList(FilterFactory.getSimpleFilter("status=0 and tinyvillage_id="+tinyvillageId+" and house_type=0 order by house_index"));
        if(lst_data != null && lst_data.size() > 0 ){
            return (List<House>)lst_data;
        }
        return null;
	}

	@Override
	public House findHouseDataByInsert(House houses) {
		String[] split = houses.getHousenos().split("、");
		//根据房间号和小区id查找平房
		for (String string : split) {
			House house = findHouseBYtinyIdandhousenohousetype(houses.getTinyvillage_id(),string);
			if(house!=null){
				return house;
			}
		}
		return null;
	}

	@Override
	public House findHouseBYtinyIdandhousenohousetype(Long tinyid, String houseno) {
		List<?> lst_data = getList(FilterFactory.getSimpleFilter("status=0 and tinyvillage_id="+tinyid+" and house_type=0 and house_no='"+houseno+"'"));
        if(lst_data != null && lst_data.size() > 0 ){
            return (House)lst_data.get(0);
        }
        return null;
	}

	@Override
	public House saveHouseorUpdate(House houses) {
		HouseManager houseManager=(HouseManager)SpringHelper.getBean("houseManager");
		String[] split = houses.getHousenos().split("、");
		for (String string : split) {
			House house = findHouseBYtinyIdandhousenohousetype(houses.getTinyvillage_id(),string);
			if(house==null){
				house=new House();
				house.setHouse_type(0);
			}
			house.setTinyvillage_id(houses.getTinyvillage_id());
			house.setBuilding_id(Long.parseLong(0+""));
			house.setHouse_no(string);
			house.setApprove_status(0);
			house.setBuilding_room_number("0");
			preObject(house);
			houseManager.saveObject(house);
		}
		return houses;
	}

	@Override
	public House updateHouseVerify(House house) {
		//根据平房房间号和小区id验证平房是否存在
		House findHouseBYtinyIdandhousenohousetype = findHouseBYtinyIdandhousenohousetype(house.getTinyvillage_id(),house.getHouse_no());
		if(findHouseBYtinyIdandhousenohousetype!=null){
			if(house.getId()!=findHouseBYtinyIdandhousenohousetype.getId()){
				return findHouseBYtinyIdandhousenohousetype;
			}
		}
		return null;
	}

	@Override
	public House updateHouseBungalow(House house) {
		House houseById = getHouseById(house.getId());
		houseById.setHouse_no(house.getHouse_no());
		houseById.setHouse_type(0);
		preObject(houseById);
		return houseById;
	}

	@Override
	public House queryfindbungalowhouse(House house) {
		List<?> lst_data = getList(FilterFactory.getSimpleFilter("status=0 and tinyvillage_id="+house.getTinyvillage_id()+" and house_type=0 and house_no='"+house.getHouse_no()+"'"));
        if(lst_data != null && lst_data.size() > 0 ){
            return (House)lst_data.get(0);
        }
        return null;
	}

	@Override
	public House insertHouse(House house) {
		HouseManager houseManager=(HouseManager)SpringHelper.getBean("houseManager");
		House houses = findHouseBYtinyIdandhousenohousetype(house.getTinyvillage_id(),house.getHouse_no());
		if(houses==null){
			houses=new House();
			houses.setHouse_type(0);
		}
		houses.setTinyvillage_id(house.getTinyvillage_id());
		houses.setBuilding_id(Long.parseLong(0+""));
		houses.setHouse_no(house.getHouse_no());
		houses.setApprove_status(0);
		houses.setBuilding_room_number("0");
		preObject(houses);
		houseManager.saveObject(houses);
		return houses;
	}

	@Override
	public House updateHouseVersion(House house) {
		House queryfindbungalowhouse = queryfindbungalowhouse(house);
		if(queryfindbungalowhouse!=null){
			if(!queryfindbungalowhouse.getId().equals(house.getId())){
				return queryfindbungalowhouse;
			}
		}
		return null;
	}

	@Override
	public List<House> findBuildingHouseForBuildingId(Long building_id) {
		HouseDao houseDao=(HouseDao)SpringHelper.getBean(HouseDao.class.getName());
		List<House> list = houseDao.findBuildingHouseByBuildingId(building_id);
		return list;
	}

	@Override
	public House insertBuildingHouseData(List<Map<String, Object>> list) {
		HouseManager houseManager=(HouseManager)SpringHelper.getBean("houseManager");
		House house=null;
		for (Map<String, Object> map : list) {
			house=new House();
			//小区id
			long tinyvillage_id = Long.parseLong(map.get("tinyvillage_id")+"");
			long building_id = Long.parseLong(map.get("building_id")+"");//楼房id
			String building_unit_no= map.get("building_unit_no")+"";//单元号
			String building_house_no= map.get("building_house_no")+"";//单元号
			house.setTinyvillage_id(tinyvillage_id);
			house.setBuilding_id(building_id);
			house.setBuilding_unit_no(building_unit_no);
			house.setBuilding_house_no(building_house_no);
			house.setHouse_type(1);
			house.setHouse_no("0");
			house.setApprove_status(0);
			house.setBuilding_room_number("0");
			preObject(house);
			houseManager.saveObject(house);
		}
		return house;
	}

	@Override
	public List<House> findBusinessBuliding(Long tiny_id) {
		HouseDao houseDao=(HouseDao)SpringHelper.getBean(HouseDao.class.getName());
		List<House> list = houseDao.findBusinessBuilding(tiny_id);
		return list;
	}

	@Override
	public House insertBusiness(House house) {
		HouseManager houseManager=(HouseManager)SpringHelper.getBean("houseManager");
		//根据小区查询小区信息
		TinyVillageManager tinyVillageManager=(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
		TinyVillage tinyVillage = tinyVillageManager.getTinyVillageById(house.getTinyvillage_id());
		//查询商业楼宇信息
		BuildingManager buildingManager=(BuildingManager)SpringHelper.getBean("buildingManager");
		Building business = buildingManager.getBuildingByBuildingId(house.getBuilding_id());
		if(business==null){
			business=new Building();
			business.setName(house.getBuilding_name());
			business.setTinyvillage_id(house.getTinyvillage_id());
			business.setVillage_id(tinyVillage.getVillage_id());
			business.setType(2);
			preObject(business);
			buildingManager.saveObject(business);
		}else{
			business.setName(house.getBuilding_name());
			business.setTinyvillage_id(house.getTinyvillage_id());
			business.setVillage_id(tinyVillage.getVillage_id());
			business.setType(2);
			preObject(business);
			buildingManager.saveObject(business);
		}
		//根据商业楼宇查看下面的房间
		List<House> list = findBusinessByBuilding_id(business.getId());
		if(list!=null&&list.size()>0){
			for (House house2 : list) {
				house2.setStatus(1);
				houseManager.saveObject(house2);
			}
		}
		House updateHouse = new House();
		updateHouse.setHouse_type(2);
		updateHouse.setTinyvillage_id(house.getTinyvillage_id());
		updateHouse.setBuilding_id(business.getId());
		updateHouse.setBuilding_room_number("0");
		updateHouse.setApprove_status(0);
		updateHouse.setHouse_no(house.getHouse_no());
		preObject(updateHouse);
		houseManager.saveObject(updateHouse);
		return updateHouse;
	}

	@Override
	public List<House> findBusinessByBuilding_id(Long building_id) {
		List<?> lst_data = getList(FilterFactory.getSimpleFilter("building_id="+building_id+" and status=0 and house_type=2"));
        if(lst_data != null && lst_data.size() > 0 ){
            return (List<House>)lst_data;
        }
        return null;
	}

	@Override
	public House addBungalowHouse(List<Map<String, Object>> listhouse) {
		// TODO Auto-generated method stub
		House house=null;
		if(listhouse!=null&&listhouse.size()>0){
			for (Map<String, Object> map : listhouse) {
				Object house_id=map.get("house_id");
				if(house_id!=null&&!"".equals(house_id)){
					house=getHouseById(Long.parseLong(house_id+""));
				}else{
					house=new House();
				}
				house.setHouse_no(map.get("house_no")+"");
				house.setHouse_type(0);
				house.setTinyvillage_id(Long.parseLong(map.get("tinyvillage_id")+""));
				house.setBuilding_room_number("0");
				house.setApprove_status(0);
				house.setBuilding_id(Long.parseLong("0"));
				house.setAddress(map.get("tinyvillage_name")+""+map.get("house_no")+"号");
				house.setHouse_index(Integer.parseInt(map.get("index")+""));
				preObject(house);
				this.saveObject(house);
			}
		}
		return house;
	}
	
	
}


