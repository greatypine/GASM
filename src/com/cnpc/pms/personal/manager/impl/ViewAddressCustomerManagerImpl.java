package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.entity.Building;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.House;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.personal.entity.ViewAddressCustomer;
import com.cnpc.pms.personal.manager.BuildingManager;
import com.cnpc.pms.personal.manager.HouseManager;
import com.cnpc.pms.personal.manager.TinyVillageCodeManager;
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.personal.manager.ViewAddressCustomerManager;

import java.util.Date;
import java.util.List;

/**
 * 客户地址信息相关业务类
 * Created by liuxiao on 2016/5/25 0025.
 */
public class ViewAddressCustomerManagerImpl extends BizBaseCommonManager implements ViewAddressCustomerManager {


    @Override
    public ViewAddressCustomer saveOrUpdateAddressInfo(ViewAddressCustomer addressCustomer) {
        //小区业务类对象
        TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
        //楼房业务类对象
        BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
        //房屋业务类对象
        HouseManager houseManager = (HouseManager) SpringHelper.getBean("houseManager");

        TinyVillage tinyVillage = null;

        Date sdate = new Date();
        if(addressCustomer.getTv_id() != null){
            tinyVillage = tinyVillageManager.getTinyVillageById(addressCustomer.getTv_id());
        } else if(addressCustomer.getTv_name() != null){
        	TinyVillage tVillage = new TinyVillage();
        	tVillage.setTown_id(addressCustomer.getTown_id());
        	tVillage.setName(addressCustomer.getTv_name());
        	tVillage.setTinyvillage_type(addressCustomer.getHouse_type());
            tinyVillage = tinyVillageManager.getTinyVilageByNameByTiny(tVillage);
        }
        
        if(tinyVillage == null){
            tinyVillage = new TinyVillage();
            addressCustomer.setBuilding_id(null);
            tinyVillage.setCreate_user_id(addressCustomer.getUpdate_user_id());
            tinyVillage.setCreate_user(addressCustomer.getUpdate_user());
            tinyVillage.setCreate_time(sdate);
            tinyVillage.setName(addressCustomer.getTv_name());
            tinyVillage.setAddress(addressCustomer.getTv_address());
            tinyVillage.setUpdate_user_id(addressCustomer.getUpdate_user_id());
            tinyVillage.setUpdate_user(addressCustomer.getUpdate_user());
            tinyVillage.setUpdate_time(sdate);
            tinyVillage.setStore_id(addressCustomer.getStore_id());
            tinyVillage.setTinyvillage_type(addressCustomer.getHouse_type());
            tinyVillage.setStatus(0);
            preObject(tinyVillage);
            tinyVillageManager.saveObject(tinyVillage);
            TinyVillageCodeManager tinyVillageCodeManager=(TinyVillageCodeManager)SpringHelper.getBean("tinyVillageCodeManager");
            tinyVillageCodeManager.saveTinyVillageCode(tinyVillage);
        }
       
        addressCustomer.setTv_id(tinyVillage.getId());
       
        House obj_house = null;
        if(addressCustomer.getHouse_type() == 0){//平房
        	
            obj_house = houseManager.queryHouseByTinVilligeAndHouseNo(tinyVillage.getId(), addressCustomer.getHouse_no());
            if(obj_house==null){
            	obj_house = new House();
                obj_house.setCreate_user_id(addressCustomer.getUpdate_user_id());
                obj_house.setCreate_user(addressCustomer.getUpdate_user());
                obj_house.setCreate_time(sdate);
                obj_house.setBuilding_id(0L);
                obj_house.setTinyvillage_id(addressCustomer.getTv_id());
                obj_house.setHouse_no(addressCustomer.getHouse_no());//平房门牌号存储house_no
                obj_house.setHouse_type(addressCustomer.getHouse_type());
                obj_house.setUpdate_user_id(addressCustomer.getUpdate_user_id());
                obj_house.setUpdate_user(addressCustomer.getUpdate_user());
                obj_house.setUpdate_time(sdate);
                obj_house.setApprove_status(0);
                preObject(obj_house);
                houseManager.saveObject(obj_house);
            }
            
            
        }else{//楼房
        	
        	 Building obj_building = null;
             if(addressCustomer.getBuilding_id() != null){
                 obj_building = buildingManager.getBuildingByBuildingIdAndTinVillageId(addressCustomer.getBuilding_id(),tinyVillage.getId());
             }else{
                 obj_building = buildingManager.getBuildingByName(tinyVillage.getId(), addressCustomer.getBuilding_name());
             }
             if(obj_building==null){
                 obj_building = new Building();
                 addressCustomer.setHouse_id(null);
                 obj_building.setCreate_user_id(addressCustomer.getUpdate_user_id());
                 obj_building.setCreate_user(addressCustomer.getUpdate_user());
                 obj_building.setCreate_time(sdate);
                 obj_building.setUpdate_user_id(addressCustomer.getUpdate_user_id());
                 obj_building.setUpdate_user(addressCustomer.getUpdate_user());
                 obj_building.setUpdate_time(sdate);
                 obj_building.setName(addressCustomer.getBuilding_name());
                 obj_building.setTinyvillage_id(tinyVillage.getId());
                 obj_building.setType(1);
                 obj_building.setStatus(0);
                 preObject(obj_building);
                 buildingManager.saveObject(obj_building);
             }
             
             addressCustomer.setBuilding_id(obj_building.getId());
             obj_house = houseManager.getHouseByBuildingAndNo(addressCustomer.getBuilding_id(),addressCustomer.getHouse_no(),addressCustomer.getUnit_no());
             if(obj_house==null){
             	obj_house = new House();
                 obj_house.setCreate_user_id(addressCustomer.getUpdate_user_id());
                 obj_house.setCreate_user(addressCustomer.getUpdate_user());
                 obj_house.setCreate_time(sdate);
                 obj_house.setTinyvillage_id(addressCustomer.getTv_id());
                 obj_house.setBuilding_id(addressCustomer.getBuilding_id());
                 obj_house.setBuilding_house_no(addressCustomer.getHouse_no());//楼房门牌号存储building_house_no
                 obj_house.setBuilding_unit_no(addressCustomer.getUnit_no());
                 obj_house.setHouse_type(addressCustomer.getHouse_type());
                 obj_house.setUpdate_user_id(addressCustomer.getUpdate_user_id());
                 obj_house.setUpdate_user(addressCustomer.getUpdate_user());
                 obj_house.setUpdate_time(sdate);
                 obj_house.setApprove_status(0);
                 preObject(obj_house);
                 houseManager.saveObject(obj_house);
             }
             
        	
        }
        
        addressCustomer.setHouse_id(obj_house.getId());
        return addressCustomer;
    }

    @Override
    public ViewAddressCustomer findAddressByHouse_id(Long house_id) {
        List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("house_id",house_id));
        if(lst_data != null && lst_data.size() > 0){
            return (ViewAddressCustomer)lst_data.get(0);
        }
        return null;
    }

	
	@Override
	public ViewAddressCustomer saveOrUpdateCustomerAddressInfo(Customer customer) {
		
		ViewAddressCustomer addressCustomer = customer.getCustomer_address();
		
		TinyVillageCodeManager tinyVillageCodeManager=(TinyVillageCodeManager)SpringHelper.getBean("tinyVillageCodeManager");
		 //小区业务类对象
        TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
        //楼房业务类对象
        BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
        //房屋业务类对象
        HouseManager houseManager = (HouseManager) SpringHelper.getBean("houseManager");

        TinyVillage tinyVillage = null;

        Date sdate = new Date();
        
        //小区
        if(addressCustomer.getTv_id() != null){
            tinyVillage = tinyVillageManager.getTinyVillageById(addressCustomer.getTv_id());
        }else if(addressCustomer.getTv_name() != null){
        	TinyVillage tVillage = new TinyVillage();
        	tVillage.setTown_id(addressCustomer.getTown_id());
        	tVillage.setName(addressCustomer.getTv_name());
        	tVillage.setTinyvillage_type(addressCustomer.getHouse_type());
            tinyVillage = tinyVillageManager.getTinyVilageByNameByTiny(tVillage);
        }

        if(tinyVillage == null){
            tinyVillage = new TinyVillage();
            addressCustomer.setBuilding_id(null);
            tinyVillage.setCreate_user_id(addressCustomer.getUpdate_user_id());
            tinyVillage.setCreate_user(addressCustomer.getUpdate_user());
            tinyVillage.setCreate_time(sdate);
            tinyVillage.setTown_id(addressCustomer.getTown_id());
            tinyVillage.setVillage_id(addressCustomer.getVillage_id());
            tinyVillage.setUpdate_user_id(addressCustomer.getUpdate_user_id());
            tinyVillage.setUpdate_user(addressCustomer.getUpdate_user());
            tinyVillage.setUpdate_time(sdate);
            tinyVillage.setName(addressCustomer.getTv_name());
            tinyVillage.setAddress(addressCustomer.getTv_address());
            tinyVillage.setStore_id(addressCustomer.getStore_id());
            tinyVillage.setTinyvillage_type(addressCustomer.getHouse_type());
            tinyVillage.setStatus(0);
            preObject(tinyVillage);
            tinyVillageManager.saveObject(tinyVillage);
            
            tinyVillageCodeManager.saveTinyVillageCode(tinyVillage);
            
        }
       
        addressCustomer.setTv_id(tinyVillage.getId());
       
        House obj_house = null;
        if(addressCustomer.getHouse_type() == 0){//平房
        	obj_house = new House();
            obj_house.setCreate_user_id(addressCustomer.getUpdate_user_id());
            obj_house.setCreate_user(addressCustomer.getUpdate_user());
            obj_house.setCreate_time(sdate);
            obj_house.setBuilding_id(0L);
            obj_house.setTinyvillage_id(addressCustomer.getTv_id());
            obj_house.setHouse_no(addressCustomer.getHouse_no());//平房门牌号存储house_no
            obj_house.setHouse_type(addressCustomer.getHouse_type());
            obj_house.setUpdate_user_id(addressCustomer.getUpdate_user_id());
            obj_house.setUpdate_user(addressCustomer.getUpdate_user());
            obj_house.setUpdate_time(sdate);
            obj_house.setApprove_status(0);
            preObject(obj_house);
            houseManager.saveObject(obj_house);
        }else{
        	 Building obj_building = null;
        	 if(addressCustomer.getBuilding_id() != null){
                 obj_building = buildingManager.getBuildingByBuildingIdAndTinVillageId(addressCustomer.getBuilding_id(),tinyVillage.getId());
             }else{
                 obj_building = buildingManager.getBuildingByName(tinyVillage.getId(), addressCustomer.getBuilding_name());
             }
        	 
        	 if(obj_building==null){
                 obj_building = new Building();
                 addressCustomer.setHouse_id(null);
                 obj_building.setCreate_user_id(addressCustomer.getUpdate_user_id());
                 obj_building.setCreate_user(addressCustomer.getUpdate_user());
                 obj_building.setCreate_time(sdate);
                 obj_building.setUpdate_user_id(addressCustomer.getUpdate_user_id());
                 obj_building.setUpdate_user(addressCustomer.getUpdate_user());
                 obj_building.setUpdate_time(sdate);
                 obj_building.setName(addressCustomer.getBuilding_name());
                 obj_building.setTinyvillage_id(tinyVillage.getId());
                 obj_building.setType(1);
                 obj_building.setStatus(0);
                 preObject(obj_building);
                 buildingManager.saveObject(obj_building);
             }
             
             addressCustomer.setBuilding_id(obj_building.getId());
             obj_house = houseManager.getHouseByBuildingAndNo(addressCustomer.getBuilding_id(),addressCustomer.getHouse_no(),addressCustomer.getUnit_no());
             if(obj_house == null){
                 obj_house = new House();
                 obj_house.setCreate_user_id(addressCustomer.getUpdate_user_id());
                 obj_house.setCreate_user(addressCustomer.getUpdate_user());
                 obj_house.setCreate_time(sdate);
                 obj_house.setBuilding_house_no(addressCustomer.getHouse_no());//楼房门牌号存储building_house_no
                 obj_house.setBuilding_unit_no(addressCustomer.getUnit_no());
                 obj_house.setBuilding_id(obj_building.getId());
                 obj_house.setHouse_type(addressCustomer.getHouse_type());
                 obj_house.setUpdate_user_id(addressCustomer.getUpdate_user_id());
                 obj_house.setUpdate_user(addressCustomer.getUpdate_user());
                 obj_house.setUpdate_time(sdate);
                 obj_house.setTinyvillage_id(addressCustomer.getTv_id());
                 obj_house.setApprove_status(0);
                 preObject(obj_house);
                 houseManager.saveObject(obj_house);
             }
        	 
        }
            
        addressCustomer.setHouse_id(obj_house.getId());
        return addressCustomer;
	}
}
