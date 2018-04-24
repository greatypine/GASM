package com.cnpc.pms.base.file.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;

import com.cnpc.pms.base.file.manager.ExcelManager;
import com.cnpc.pms.base.file.utils.ExcelDataFormat;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.manager.AttachmentManager;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.Building;
import com.cnpc.pms.personal.entity.House;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.AttachmentsManager;
import com.cnpc.pms.personal.manager.BuildingManager;
import com.cnpc.pms.personal.manager.HouseManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TinyVillageCodeManager;
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.personal.manager.VillageManager;

/**
 * Created by zhangjy on 2015/8/5.
 */
public class ExcelManagerImpl extends BizBaseCommonManager implements ExcelManager {

	/**
	 * 多文件 根据文件路径存储excel中的数据 返回值 0 保存成功 1 社区不存在 2 社区存在多个 3 社区已审核 4 excel解析错误
	 * 
	 * @param
	 */
	@Transactional
	public String saveFileExcelData(User user, InputStream inp, String fileName, int type, String filePath,
			String remark, String ip) throws Exception {
		System.out.println("调用saveFileExcelData" + fileName);
		Attachment attachment = new Attachment();
		attachment.setFile_name(fileName + ip);
		System.out.println(filePath);
		attachment.setFile_path(filePath);
		// excel
		attachment.setFile_type_name("地址文件");
		// 未审核
		attachment.setApprove_status(0);
		preObject(attachment);
		Long attachmentId = null;
		System.out.println(filePath);
		Map<String, Object> map = ExcelDataFormat.getMapDataFromExcel(filePath);
		// 社区
		Village village = (Village) map.get("village");
		// 小区+楼房+住宅
		Map<TinyVillage, List<Map<Building, List<House>>>> buildMap = (Map<TinyVillage, List<Map<Building, List<House>>>>) map
				.get("build");
		// 小区+平房住宅
		Map<TinyVillage, List<House>> bungalowMap = (Map<TinyVillage, List<House>>) map.get("bungalow");
		// 小区+写字楼楼层
		Map<TinyVillage, List<House>> shangYeMap = (Map<TinyVillage, List<House>>) map.get("shangye");
		// 小区+广场
		Map<TinyVillage, List<House>> guangChangMap = (Map<TinyVillage, List<House>>) map.get("guangchang");
		// 小区+其他
		Map<TinyVillage, List<House>> otherMap = (Map<TinyVillage, List<House>>) map.get("other");
		// 获取门店名称和门店编码
		String store_name = village.getStore_name();
		String store_code = village.getStore_code();
		Store store = findStoreByStoreNo(store_code);
		if (store == null) {
			throw new RuntimeException("门店编码不正确或门店不存在");
		}
		attachment.setStoreName(store.getName());
		// 根据门店名称模糊查询门店
		// 根据文件名里的gb_code取得社区
		VillageManager villageManager = (VillageManager) SpringHelper.getBean("villageManager");
		String gb_code = fileName.split("-")[0];// 国标
		Village vlist = getVillageByGb_code(fileName.split("-")[0]);
		if (vlist == null) {
			throw new RuntimeException("社区国标码" + gb_code + "不存在");
		}
		Long villageId = vlist.getId();
		System.out.print(villageId + "-----villageId");
		if (!"备注".equals(remark)) {
			attachment.setRemark(remark);
		}
		// 保存附件
		System.out.println("-----------开始添加附件--------------" + villageId);
		Attachment saveAttach = saveAttachment(attachment);
		System.out.println("-----------添加附件完成--------------" + villageId);
		attachmentId = saveAttach.getId();
		// 保存社区
		vlist.setAttachment_id(attachmentId);
		vlist.setApprove_status(0);
		vlist.setCommittee_address(village.getCommittee_address());
		vlist.setSquare_area(village.getSquare_area());
		vlist.setHousehold_number(village.getHousehold_number());
		vlist.setResident_population_number(village.getResident_population_number());
		vlist.setCommittee_phone(village.getCommittee_phone());
		vlist.setIntroduction(village.getIntroduction());
		preObject(vlist);
		// 更新社区
		updateVillage(vlist);
		// 保存小区，住宅楼，商业楼，平房
		saveBuildData(buildMap, villageId, user, attachmentId, null, null, store);
		saveShangyeData(shangYeMap, villageId, user, attachmentId, null, null, store);
		saveBungalow(bungalowMap, villageId, user, attachmentId, null, null, store);
		saveGuangChang(guangChangMap, villageId, user, attachmentId, null, null, store);
		saveOther(otherMap, villageId, user, attachmentId, null, null, store);
		return "";
	}

	public Village getVillageByGb_code(String gb_code) {
		System.out.println("调用getVillageByGb_code");
		VillageManager villageManager = (VillageManager) SpringHelper.getBean("villageManager");
		List<?> lst_vilage_data = villageManager.getList(FilterFactory.getSimpleFilter("gb_code", gb_code));
		if (lst_vilage_data != null && lst_vilage_data.size() > 0) {
			return (Village) lst_vilage_data.get(0);
		}
		return null;
	}

	/**
	 * 保存广场、公园数据
	 * 
	 * @param map
	 * @param villageId
	 * @param user
	 * @param attachmentId
	 * @param createUserId
	 * @param createUserName
	 */
	public void saveGuangChang(Map<TinyVillage, List<House>> map, Long villageId, User user, Long attachmentId,
			Long createUserId, String createUserName, Store store) {
		TinyVillageCodeManager tinyVillageCodeManager = (TinyVillageCodeManager) SpringHelper
				.getBean("tinyVillageCodeManager");
		HouseManager houseManager = (HouseManager) SpringHelper.getBean("houseManager");
		Long townId = null;
		TinyVillage tv = null;
		Building building = null;
		TinyVillage tinyVillage = null;
		// 取得当前社区的街道ID
		townId = getTownByVillage(villageId);
		for (Map.Entry<TinyVillage, List<House>> entry : map.entrySet()) {
			tv = entry.getKey();
			// 插入小区数据 t_tiny_village
			tinyVillage = getTinyVillageByName(tv.getName(), villageId);
			if (tinyVillage == null) {
				tv.setStore_id(store.getStore_id());
				tv.setVillage_id(villageId);
				tv.setTown_id(townId);
				tv.setAttachment_id(attachmentId);
				preObject(tv);
				if (tv.getCreate_user_id() == null) {
					tv.setCreate_user(createUserName);
					tv.setCreate_user_id(createUserId);
					tv.setUpdate_user(createUserName);
					tv.setUpdate_user_id(createUserId);
				}
				tinyVillage = saveTinyVillage(tv);
				tinyVillageCodeManager.saveTinyVillageCode(tinyVillage);
			} else {
				// 如果小区不为空判断小区是否有门店
				Long store_id = tinyVillage.getStore_id();
				if (store_id == null || "".equals(store_id) || tinyVillage.getResidents_number() == null
						|| "".equals(tinyVillage.getResidents_number())) {
					tinyVillage.setStore_id(store.getStore_id());
					tinyVillage.setResidents_number(tv.getResidents_number());
					tinyVillage = saveTinyVillage(tinyVillage);
				}
			}
			// 插入楼房数据 t_building
			building = new Building();
			building.setType(3); // 3=商业楼宇
			building.setName(tv.getName());
			building.setTinyvillage_id(tinyVillage.getId());
			building.setVillage_id(villageId);
			building.setAttachment_id(attachmentId);
			BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
			Building saveBuilding = buildingManager.findBuildingBynameAndTinyIdAndtype(building);
			if (saveBuilding == null) {
				preObject(building);
				if (building.getCreate_user_id() == null) {
					building.setCreate_user(createUserName);
					building.setCreate_user_id(createUserId);
					building.setUpdate_user(createUserName);
					building.setUpdate_user_id(createUserId);

				}
				saveBuilding = saveBuilding(building);
			}
			House house = new House();
			house.setAttachment_id(attachmentId);
			house.setTinyvillage_id(tinyVillage.getId());
			house.setBuilding_id(saveBuilding.getId());
			house.setApprove_status(0);
			house.setHouse_type(3);
			house.setBuilding_room_number("0");
			house.setBuilding_house_no(101 + "");
			preObject(house);
			house.setCreate_user(createUserName);
			house.setCreate_user_id(createUserId);
			house.setUpdate_user(createUserName);
			house.setUpdate_user_id(createUserId);
			houseManager.saveObject(house);
		}
	}

	/**
	 * 保存其他数据
	 * 
	 * @param map
	 * @param villageId
	 * @param user
	 * @param attachmentId
	 * @param createUserId
	 * @param createUserName
	 */
	public void saveOther(Map<TinyVillage, List<House>> map, Long villageId, User user, Long attachmentId,
			Long createUserId, String createUserName, Store store) {
		TinyVillageCodeManager tinyVillageCodeManager = (TinyVillageCodeManager) SpringHelper
				.getBean("tinyVillageCodeManager");
		HouseManager houseManager = (HouseManager) SpringHelper.getBean("houseManager");
		Long townId = null;
		TinyVillage tv = null;
		Building building = null;
		TinyVillage tinyVillage = null;
		// 取得当前社区的街道ID
		townId = getTownByVillage(villageId);
		for (Map.Entry<TinyVillage, List<House>> entry : map.entrySet()) {
			tv = entry.getKey();
			// 插入小区数据 t_tiny_village
			tinyVillage = getTinyVillageByName(tv.getName(), villageId);
			if (tinyVillage == null) {
				tv.setStore_id(store.getStore_id());
				tv.setVillage_id(villageId);
				tv.setTown_id(townId);
				tv.setAttachment_id(attachmentId);
				preObject(tv);
				if (tv.getCreate_user_id() == null) {
					tv.setCreate_user(createUserName);
					tv.setCreate_user_id(createUserId);
					tv.setUpdate_user(createUserName);
					tv.setUpdate_user_id(createUserId);
				}
				tinyVillage = saveTinyVillage(tv);
				tinyVillageCodeManager.saveTinyVillageCode(tinyVillage);
			} else {
				// 如果小区不为空判断小区是否有门店
				Long store_id = tinyVillage.getStore_id();
				if (store_id == null || "".equals(store_id) || tinyVillage.getResidents_number() == null
						|| "".equals(tinyVillage.getResidents_number())) {
					tinyVillage.setStore_id(store.getStore_id());
					tinyVillage.setResidents_number(tv.getResidents_number());
					tinyVillage = saveTinyVillage(tinyVillage);
				}
			}
			// 插入楼房数据 t_building
			building = new Building();
			building.setType(5); // 5=其他类型
			building.setName(tv.getName());
			building.setTinyvillage_id(tinyVillage.getId());
			building.setVillage_id(villageId);
			building.setAttachment_id(attachmentId);
			BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
			Building saveBuilding = buildingManager.findBuildingBynameAndTinyIdAndtype(building);
			if (saveBuilding == null) {
				preObject(building);
				if (building.getCreate_user_id() == null) {
					building.setCreate_user(createUserName);
					building.setCreate_user_id(createUserId);
					building.setUpdate_user(createUserName);
					building.setUpdate_user_id(createUserId);

				}
				saveBuilding = saveBuilding(building);
			}
			House house = new House();
			house.setAttachment_id(attachmentId);
			house.setTinyvillage_id(tinyVillage.getId());
			house.setBuilding_id(saveBuilding.getId());
			house.setApprove_status(0);
			house.setHouse_type(5);
			house.setBuilding_room_number("0");
			house.setBuilding_house_no(101 + "");
			preObject(house);
			house.setCreate_user(createUserName);
			house.setCreate_user_id(createUserId);
			house.setUpdate_user(createUserName);
			house.setUpdate_user_id(createUserId);
			houseManager.saveObject(house);
		}
	}

	/**
	 * 存储住宅楼
	 * 
	 * @param map
	 * @param villageId
	 *            //@param attachmentId
	 * @param user
	 */
	public void saveBuildData(Map<TinyVillage, List<Map<Building, List<House>>>> map, Long villageId, User user,
			Long attachmentId, Long createUserId, String createUserName, Store store) {
		System.out.println("调用saveBuildData");
		TinyVillageCodeManager tinyVillageCodeManager = (TinyVillageCodeManager) SpringHelper
				.getBean("tinyVillageCodeManager");
		TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
		List<House> houseList = null;
		List<House> newHouseList = null;
		List<Map<Building, List<House>>> list = null;
		Long townId = null;
		TinyVillage tinyVillage = null;
		Long buildingId = null;
		TinyVillage tv = null;
		Building building = null;

		// 取得当前社区的街道ID
		Long town_id = getTownByVillage(villageId);
		for (Map.Entry<TinyVillage, List<Map<Building, List<House>>>> entry : map.entrySet()) {
			tv = entry.getKey();
			// 判断小区是否存在，如果小区不存在则插入新小区，否则不变
			tinyVillage = getTinyVillageByName(tv.getName(), villageId);
			if (tinyVillage == null) {
				// 插入小区数据 t_tiny_village
				tv.setVillage_id(villageId);
				tv.setTown_id(town_id);
				tv.setAttachment_id(attachmentId);
				tv.setStore_id(store.getStore_id());
				preObject(tv);
				if (tv.getCreate_user_id() == null) {
					tv.setCreate_user_id(createUserId);
					tv.setCreate_user(createUserName);
					tv.setUpdate_user_id(createUserId);
					tv.setUpdate_user(createUserName);
				}
				tinyVillage = saveTinyVillage(tv);
				tinyVillageCodeManager.saveTinyVillageCode(tinyVillage);
			} else {
				// 如果小区不为空判断小区是否有门店
				Long store_id = tinyVillage.getStore_id();
				if (store_id == null || "".equals(store_id) || tinyVillage.getResidents_number() == null
						|| "".equals(tinyVillage.getResidents_number())) {
					tinyVillage.setStore_id(store.getStore_id());
					tinyVillage.setResidents_number(tv.getResidents_number());
					tinyVillage = saveTinyVillage(tinyVillage);
				}
			}
			// System.out.println("--tinyVillage---------------villageId="+villageId+"------townId="+townId+"----tinyvillageId="+tinyVillageId);

			list = entry.getValue();
			for (Map<Building, List<House>> bhMap : list) {
				BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
				for (Map.Entry<Building, List<House>> entrys : bhMap.entrySet()) {

					// 插入楼房数据 t_building
					building = entrys.getKey();
					building.setType(1); // 1=普通楼房住宅
					building.setVillage_id(villageId);
					building.setTinyvillage_id(tinyVillage.getId());
					building.setAttachment_id(attachmentId);
					Building buildingByTinyvillageAndName = buildingManager
							.findBuildingBynameAndTinyIdAndtype(building);
					if (buildingByTinyvillageAndName == null) {
						preObject(building);
						if (building.getCreate_user_id() == null) {
							building.setCreate_user_id(createUserId);
							building.setCreate_user(createUserName);
							building.setUpdate_user_id(createUserId);
							building.setUpdate_user(createUserName);
						}
						building = saveBuilding(building);
					} else {
						building = buildingByTinyvillageAndName;
					}
					// 插入住宅数据 t_house
					houseList = entrys.getValue();
					newHouseList = new ArrayList();
					for (House house : houseList) {
						house.setAttachment_id(attachmentId);
						house.setTinyvillage_id(tinyVillage.getId());
						house.setBuilding_id(building.getId());
						newHouseList.add(house);
					}
					saveHouseList(newHouseList, createUserId, createUserName);
				}
			}
		}
	}

	/**
	 * 存储商业楼宇
	 * 
	 * @param map
	 * @param villageId
	 *            //@param attachmentId
	 * @param user
	 */
	public void saveShangyeData(Map<TinyVillage, List<House>> map, Long villageId, User user, Long attachmentId,
			Long createUserId, String createUserName, Store store) {
		TinyVillageCodeManager tinyVillageCodeManager = (TinyVillageCodeManager) SpringHelper
				.getBean("tinyVillageCodeManager");
		System.out.println("调用saveShangyeData");
		List<House> houseList = null;
		List<House> newHouseList = null;
		Long tinyVillageId = null;
		Long townId = null;
		Long buildingId = null;
		TinyVillage tv = null;
		Building building = null;
		TinyVillage tinyVillage = null;
		// 取得当前社区的街道ID
		townId = getTownByVillage(villageId);

		for (Map.Entry<TinyVillage, List<House>> entry : map.entrySet()) {
			tv = entry.getKey();
			// 插入小区数据 t_tiny_village
			tinyVillage = getTinyVillageByName(tv.getName(), villageId);
			if (tinyVillage == null) {
				tv.setStore_id(store.getStore_id());
				tv.setVillage_id(villageId);
				tv.setTown_id(townId);
				tv.setAttachment_id(attachmentId);
				preObject(tv);
				if (tv.getCreate_user_id() == null) {
					tv.setCreate_user(createUserName);
					tv.setCreate_user_id(createUserId);
					tv.setUpdate_user(createUserName);
					tv.setUpdate_user_id(createUserId);
				}
				tinyVillage = saveTinyVillage(tv);
				tinyVillageCodeManager.saveTinyVillageCode(tinyVillage);
			} else {
				// 如果小区不为空判断小区是否有门店
				Long store_id = tinyVillage.getStore_id();
				if (store_id == null || "".equals(store_id) || tinyVillage.getResidents_number() == null
						|| "".equals(tinyVillage.getResidents_number())) {
					tinyVillage.setStore_id(store.getStore_id());
					tinyVillage.setResidents_number(tv.getResidents_number());
					tinyVillage = saveTinyVillage(tinyVillage);
				}
			}
			// 插入楼房数据 t_building
			building = new Building();
			building.setType(2); // 2=商业楼宇
			building.setName(tv.getName());
			building.setTinyvillage_id(tinyVillage.getId());
			building.setVillage_id(villageId);
			building.setAttachment_id(attachmentId);
			BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
			Building saveBuilding = buildingManager.findBuildingBynameAndTinyIdAndtype(building);
			if (saveBuilding == null) {
				preObject(building);
				if (building.getCreate_user_id() == null) {
					building.setCreate_user(createUserName);
					building.setCreate_user_id(createUserId);
					building.setUpdate_user(createUserName);
					building.setUpdate_user_id(createUserId);

				}
				saveBuilding = saveBuilding(building);
			}
			// 插入住宅数据 t_house 写字楼每层为一个house数据
			houseList = entry.getValue();
			newHouseList = new ArrayList();
			for (House house : houseList) {
				house.setAttachment_id(attachmentId);
				house.setTinyvillage_id(tinyVillage.getId());
				house.setBuilding_id(saveBuilding.getId());
				newHouseList.add(house);
			}
			saveHouseList(newHouseList, createUserId, createUserName);
		}
	}

	/**
	 * 存储平房
	 * 
	 * @param map
	 * @param villageId
	 *            //@param attachmentId
	 */
	public void saveBungalow(Map<TinyVillage, List<House>> map, Long villageId, User user, Long attachmentId,
			Long createUserId, String createUserName, Store store) {
		TinyVillageCodeManager tinyVillageCodeManager = (TinyVillageCodeManager) SpringHelper
				.getBean("tinyVillageCodeManager");
		System.out.println("调用saveBungalow");
		List<House> houseList = null;
		List<House> newHouseList = null;
		Long townId = null;
		Long tinyVillageId = null;
		TinyVillage tv = null;
		// 取得当前社区的街道ID
		townId = getTownByVillage(villageId);
		for (Map.Entry<TinyVillage, List<House>> entry : map.entrySet()) {
			tv = entry.getKey();
			// 判断小区是否存在，如果小区不存在则插入新小区，否则不变
			TinyVillage tinyVillage = getTinyVillageByName(tv.getName(), villageId);
			if (tinyVillage == null) {
				tv.setStore_id(store.getStore_id());
				// 插入小区数据 t_tiny_village
				tv.setVillage_id(villageId);
				tv.setTown_id(townId);
				tv.setAttachment_id(attachmentId);
				preObject(tv);
				if (tv.getCreate_user_id() == null) {
					tv.setCreate_user(createUserName);
					tv.setCreate_user_id(createUserId);
					tv.setUpdate_user(createUserName);
					tv.setUpdate_user_id(createUserId);
				}
				tinyVillage = saveTinyVillage(tv);
				tinyVillageCodeManager.saveTinyVillageCode(tinyVillage);
				tinyVillageId = tinyVillage.getId();
			} else {
				// 如果小区不为空判断小区是否有门店
				Long store_id = tinyVillage.getStore_id();
				if (store_id == null || "".equals(store_id) || tinyVillage.getResidents_number() == null
						|| "".equals(tinyVillage.getResidents_number())) {
					tinyVillage.setStore_id(store.getStore_id());
					tinyVillage.setResidents_number(tv.getResidents_number());
					tinyVillage = saveTinyVillage(tinyVillage);
				}
				tinyVillageId = tinyVillage.getId();
			}

			houseList = entry.getValue();
			newHouseList = new ArrayList();
			for (House house : houseList) {
				// 插入住宅数据 t_house 平房的building和house是一对一的关系
				house.setBuilding_id(null);
				house.setAttachment_id(attachmentId);
				house.setTinyvillage_id(tinyVillageId);
				newHouseList.add(house);
			}
			saveHouseList(newHouseList, createUserId, createUserName);
		}
	}

	/**
	 * 根据社区id取得街道id
	 * 
	 * @param id
	 */
	public Long getTownByVillage(Long id) {
		VillageManager villageManager = (VillageManager) SpringHelper.getBean("villageManager");
		Village object = (Village) villageManager.getObject(id);
		return object.getTown_id();
	}

	/**
	 * 根据社区ID和小区名取得小区
	 * 
	 * @param tinyvillageName
	 * @param townId
	 * @param type
	 *            1:楼房/2：写字楼/0：平房
	 */
	public TinyVillage getTinyVillageByName(String tinyvillageName, Long village_id) {
		TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
		List<?> list = tinyVillageManager.getList(FilterFactory
				.getSimpleFilter("village_id=" + village_id + " and name='" + tinyvillageName + "' and status=0"));
		if (list != null && list.size() > 0) {
			return (TinyVillage) list.get(0);
		}
		return null;
	}

	/**
	 * 保存小区 返回ID
	 * 
	 * @param tinyVillage
	 * @return
	 */
	public TinyVillage saveTinyVillage(final TinyVillage tinyVillage) {
		TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
		tinyVillageManager.saveObject(tinyVillage);
		return tinyVillage;
	}

	/**
	 * 保存楼房 返回ID
	 * 
	 * @param building
	 * @return
	 */
	public Building saveBuilding(final Building building) {
		BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
		buildingManager.saveObject(building);
		return building;

	}

	/**
	 * 保存房间集合 批处理
	 * 
	 * @param houseList
	 * @return
	 */
	public void saveHouseList(final List<House> houseList, Long createUserId, String createUserName) {
		System.out.println("调用saveHouseList");
		House housevalue = null;
		HouseManager houseManager = (HouseManager) SpringHelper.getBean("houseManager");
		for (House house : houseList) {

			housevalue = new House();
			housevalue.setHouse_type(house.getHouse_type());
			housevalue.setTinyvillage_id(house.getTinyvillage_id());
			housevalue.setBuilding_id(house.getBuilding_id() == null ? 0 : house.getBuilding_id());
			housevalue.setBuilding_unit_no(house.getBuilding_unit_number());
			housevalue.setBuilding_house_no(
					house.getBuilding_room_number() == null ? "0" : house.getBuilding_room_number() + "");
			if (house.getHouse_type() == 2) {
				housevalue.setHouse_no(house.getCommercial_floor_number() + "");
			} else {
				housevalue.setHouse_no(house.getBungalow_number());
			}
			housevalue.setUsed_price(0);
			housevalue.setRent(0);
			housevalue.setApprove_status(0);
			housevalue.setAddress(house.getAddress() == null ? "" : house.getAddress());
			housevalue.setAttachment_id(house.getAttachment_id());
			preObject(housevalue);
			if (housevalue.getCreate_user_id() == null) {
				housevalue.setCreate_user(createUserName);
				housevalue.setCreate_user_id(createUserId);
				housevalue.setUpdate_user(createUserName);
				housevalue.setUpdate_user_id(createUserId);
			}
			houseManager.saveObject(housevalue);
		}
	}

	/*
	 * 读取文件内的省份名称
	 */
	public String getCityName(String filePath) {
		System.out.println("调用getCityName");
		String cityName = null;
		String SHEQU = "1-社区信息";
		HSSFWorkbook hssFWork = null;
		HSSFSheet hssfSheet = null;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(filePath));
			hssFWork = new HSSFWorkbook();
			String sheetName = null;
			for (int i = 0; i < hssFWork.getNumberOfSheets(); i++) {
				hssfSheet = hssFWork.getSheetAt(i);
				if (hssfSheet != null && "".equals(hssfSheet)) {

					sheetName = hssfSheet.getSheetName();
					if (sheetName != null && "".equals(sheetName)) {
						if (SHEQU.equals(sheetName)) {
							HSSFRow hssfRow = hssfSheet.getRow(4);
							cityName = hssfRow.getCell(1).toString();
							System.out.print("省份名称" + cityName);
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return cityName;
	}

	/**
	 * 添加附件
	 */
	public Attachment saveAttachment(Attachment attachment) {
		System.out.println("--------------调用添加附件" + attachment.getFile_name());
		AttachmentsManager attachmentsManager = (AttachmentsManager) SpringHelper.getBean("attachmentsManager");
		attachmentsManager.saveObject(attachment);
		return attachment;
	}

	/**
	 * 修改社区
	 */
	public void updateVillage(Village village) {
		VillageManager villageManager = (VillageManager) SpringHelper.getBean("villageManager");
		villageManager.saveObject(village);
	}

	@Override
	public List<String> getTinyVillageByVillage_id(List<String> list_file) {
		System.out.println("调用getTinyVillageByVillage_id");
		List<String> list = new ArrayList<String>();
		TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
		for (String obj : list_file) {
			String gb_code = obj.split("-")[0];
			Village villageByGb_code = getVillageByGb_code(gb_code);
			if (villageByGb_code != null) {
				List<TinyVillage> TinyVillageList = tinyVillageManager
						.getTinyVillageByVillage_Id(villageByGb_code.getId());
				if (TinyVillageList != null && TinyVillageList.size() > 0) {
					String village_name = villageByGb_code.getGb_code() + "-" + villageByGb_code.getName();
					list.add(village_name);
				}
			}
		}
		// if(list!=null&&list.size()>0){
		// return list;
		// }

		return list;

	}

	@Override
	public String saveFileExcel(User user, InputStream inp, String fileName, int type, String filePath,
			Attachment attachment) throws Exception {
		AttachmentManager attachmentManager = (AttachmentManager) SpringHelper.getBean("attachmentManager");
		Long attachmentId = attachment.getId();
		Map<String, Object> map = ExcelDataFormat.getMapDataFromExcel(filePath);
		// 社区
		Village village = (Village) map.get("village");
		// 小区+楼房+住宅
		Map<TinyVillage, List<Map<Building, List<House>>>> buildMap = (Map<TinyVillage, List<Map<Building, List<House>>>>) map
				.get("build");
		// 小区+平房住宅
		Map<TinyVillage, List<House>> bungalowMap = (Map<TinyVillage, List<House>>) map.get("bungalow");
		// 小区+写字楼楼层
		Map<TinyVillage, List<House>> shangYeMap = (Map<TinyVillage, List<House>>) map.get("shangye");
		// 小区+广场
		Map<TinyVillage, List<House>> guangChangMap = (Map<TinyVillage, List<House>>) map.get("guangchang");
		// 小区+其他
		Map<TinyVillage, List<House>> otherMap = (Map<TinyVillage, List<House>>) map.get("other");
		// 获取门店名称和门店编码
		// String store_name = village.getStore_name();
		String store_code = village.getStore_code();
		Store store = findStoreByStoreNo(store_code);
		if (store == null) {
			attachment.setMessage("门店编码不正确或门店不存在");
			attachment.setUploadType("失败");
			attachment.setStoreName(store.getName());
			attachmentManager.saveObject(attachment);
			throw new RuntimeException("门店编码不正确或门店不存在");
		}
		attachment.setStoreName(store.getName());

		// 根据文件名里的gb_code取得社区
		VillageManager villageManager = (VillageManager) SpringHelper.getBean("villageManager");
		String gb_code = fileName.split("-")[0];// 国标
		Village vlist = getVillageByGb_code(fileName.split("-")[0]);
		if (vlist == null) {
			attachment.setMessage("社区国标码" + gb_code + "不存在");
			attachment.setUploadType("失败");
			attachment.setStoreName(store.getName());
			attachmentManager.saveObject(attachment);
			throw new RuntimeException("社区国标码" + gb_code + "不存在");
		}

		Long villageId = vlist.getId();
		// 保存社区
		vlist.setAttachment_id(attachmentId);
		vlist.setApprove_status(0);
		vlist.setCommittee_address(village.getCommittee_address());
		vlist.setSquare_area(village.getSquare_area());
		vlist.setHousehold_number(village.getHousehold_number());
		vlist.setResident_population_number(village.getResident_population_number());
		vlist.setCommittee_phone(village.getCommittee_phone());
		vlist.setIntroduction(village.getIntroduction());
		preObject(vlist);
		// 更新社区
		updateVillage(vlist);
		// 保存小区，住宅楼，商业楼，平房
		saveBuildData(buildMap, villageId, user, attachmentId, attachment.getCreate_user_id(),
				attachment.getCreate_user(), store);
		saveShangyeData(shangYeMap, villageId, user, attachmentId, attachment.getCreate_user_id(),
				attachment.getCreate_user(), store);
		saveBungalow(bungalowMap, villageId, user, attachmentId, attachment.getCreate_user_id(),
				attachment.getCreate_user(), store);
		saveGuangChang(guangChangMap, villageId, user, attachmentId, null, null, store);
		saveOther(otherMap, villageId, user, attachmentId, null, null, store);
		return store.getName();
	}

	@Override
	public String getTinyVillageByVillage(String fileName) {
		TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
		String gb_code = fileName.split("-")[0];
		Village villageByGb_code = getVillageByGb_code(gb_code);
		if (villageByGb_code != null) {
			List<TinyVillage> TinyVillageList = tinyVillageManager.getTinyVillageByVillage_Id(villageByGb_code.getId());
			if (TinyVillageList != null && TinyVillageList.size() > 0) {
				String village_name = villageByGb_code.getGb_code() + "-" + villageByGb_code.getName();
				return village_name;
			}
		}
		return null;
	}

	private Store findStoreByStoreNo(String storeno) {
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		return storeManager.findStoreByStoreNo(storeno);
	}

}
