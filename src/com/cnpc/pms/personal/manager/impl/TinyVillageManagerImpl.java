package com.cnpc.pms.personal.manager.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.communityArea.entity.ComnunityArea;
import com.cnpc.pms.communityArea.manager.ComnunityAreaManager;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.mongodb.dto.TinyVillageCoordDto;
import com.cnpc.pms.mongodb.manager.MongoDBManager;
import com.cnpc.pms.personal.dao.BuildingDao;
import com.cnpc.pms.personal.dao.HouseDao;
import com.cnpc.pms.personal.dao.NewsDao;
import com.cnpc.pms.personal.dao.PersonDutyDao;
import com.cnpc.pms.personal.dao.ProvinceDao;
import com.cnpc.pms.personal.dao.TinyAreaDao;
import com.cnpc.pms.personal.dao.TinyVillageDao;
import com.cnpc.pms.personal.entity.Building;
import com.cnpc.pms.personal.entity.City;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.House;
import com.cnpc.pms.personal.entity.HouseBuilding;
import com.cnpc.pms.personal.entity.PersonDuty;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.TinyCoordinate;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.personal.entity.TinyVillageCode;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.BuildingManager;
import com.cnpc.pms.personal.manager.CityManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.HouseBuildingManager;
import com.cnpc.pms.personal.manager.HouseManager;
import com.cnpc.pms.personal.manager.PersonDutyManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TinyCoordinateManager;
import com.cnpc.pms.personal.manager.TinyVillageCodeManager;
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.slice.entity.AreaInfo;
import com.cnpc.pms.slice.manager.AreaInfoManager;
import com.cnpc.pms.utils.ValueUtil;
import com.gexin.fastjson.JSONObject;

/**
 * 小区相关业务实现类 Created by liu on 2016/5/25 0025.
 */
public class TinyVillageManagerImpl extends BizBaseCommonManager implements TinyVillageManager {

	/**
	 * 根据名称查找小区对象
	 * 
	 * @param str_name
	 *            小区名称
	 * @return 小区对象
	 */
	@Override
	public TinyVillage getTinyVilageByName(Long town_id, String str_name) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("name", str_name)
				.appendAnd(FilterFactory.getSimpleFilter("town_id", town_id))
				.appendAnd(FilterFactory.getSimpleFilter("status", 0)));
		if (lst_data != null && lst_data.size() > 0) {
			return (TinyVillage) lst_data.get(0);
		}
		return null;
	}

	/**
	 * 根据社区Id查找小区对象
	 * 
	 * @return 小区对象集合
	 */
	@Override
	public List<TinyVillage> getTinyVillageByVillage_Id(Long village_id) {
		List<TinyVillage> list = new ArrayList<TinyVillage>();
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter(
				"village_id=" + village_id + " and status=0 AND (attachment_id is not NULL OR attachment_id!='')"));
		if (lst_data != null && lst_data.size() > 0) {
			for (Object object : lst_data) {
				list.add((TinyVillage) object);
			}
			return list;
		}

		return null;
	}

	/**
	 * 获取小区对象
	 */
	@Override
	public Map<String, String> getTinyVillage() {
		Map<String, String> map = new HashMap<String, String>();
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		TinyVillage tinyVillage = tinyVillageDao.getTinyVillage();
		if (tinyVillage != null) {
			map.put("name", tinyVillage.getName());
			map.put("address", tinyVillage.getAddress() == null || tinyVillage.getAddress() == ""
					? tinyVillage.getName() : tinyVillage.getAddress());
			map.put("id", tinyVillage.getId().toString());
			if (tinyVillage.getTown_id() != null && tinyVillage.getTown_id() != null
					&& !"".equals(tinyVillage.getTown_id())) {
				ProvinceDao provinceDao = (ProvinceDao) SpringHelper.getBean(ProvinceDao.class.getName());
				String cityName = provinceDao.getProvinceByTown_id(tinyVillage.getTown_id());
				if (cityName != null && cityName.length() > 0) {
					map.put("cityName", cityName);
				} else {
					map.put("cityName", "全国");
				}
			} else {
				map.put("cityName", "全国");
			}
			return map;
		}
		return null;

	}

	@Override
	public TinyVillage getTinyVillageById(Long id) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("id", id));
		if (lst_data != null && lst_data.size() > 0) {
			return (TinyVillage) lst_data.get(0);
		}
		return null;
	}

	@Override
	public TinyVillage saveTinyVillageForMapCon(TinyVillage tinyVillage) {
		TinyVillage save_tiny_village = null;
		if (tinyVillage.getId() == null) {
			return null;
		}
		save_tiny_village = (TinyVillage) this.getObject(tinyVillage.getId());
		save_tiny_village.setBaidu_coordinate_x(tinyVillage.getBaidu_coordinate_x());
		save_tiny_village.setBaidu_coordinate_y(tinyVillage.getBaidu_coordinate_y());
		save_tiny_village.setGaode_coordinate_x(tinyVillage.getGaode_coordinate_x());
		save_tiny_village.setGaode_coordinate_y(tinyVillage.getGaode_coordinate_y());
		save_tiny_village.setSogou_coordinate_x(tinyVillage.getSogou_coordinate_x());
		save_tiny_village.setSogou_coordinate_y(tinyVillage.getSogou_coordinate_y());
		preObject(save_tiny_village);
		saveObject(save_tiny_village);
		return save_tiny_village;
	}

	@Override
	public Map<String, Object> getTinyVillageBygb_code(QueryConditions conditions) {
		/*
		 * List<?> list=null; FSP fsp = new FSP();
		 * fsp.setPage(conditions.getPageinfo()); for (Map<String, Object>
		 * map_where : conditions.getConditions()) { if
		 * ("gb_code".equals(map_where.get("key")) && null !=
		 * map_where.get("value") && !"".equals(map_where.get("value"))) {
		 * String gb_code=(String) map_where.get("value");
		 * fsp.setUserFilter(FilterFactory.getSimpleFilter("gb_code",gb_code));
		 * } } list = this.getList(fsp); Map<String,Object> map = new
		 * HashMap<String, Object>(); map.put("header", ""); map.put("data",
		 * list); map.put("pageInfo",conditions.getPageinfo());
		 */
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();

		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("tinyvillage_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and (tiny.name like '").append(map_where.get("value"))
						.append("' or tiny.othername like '").append(map_where.get("value")).append("')");
			} else if ("name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and vill.name like '").append(map_where.get("value")).append("'");
			} else if ("ifNull".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				System.out.println(map_where.get("value"));
				if (map_where.get("value").equals("1")) {
					sb_where.append(" AND tiny.village_id is NULL");
				} else if (map_where.get("value").equals("0")) {
					sb_where.append(" AND tiny.village_id is not NULL");
				}
			} else if ("village_id".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND tiny.village_id in (" + map_where.get("value").toString() + ")");
			} else if ("town_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and town.name like '").append(map_where.get("value")).append("'");
			} else if ("type_id".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				System.out.println(map_where.get("value"));
				if (map_where.get("value").equals("1")) {
					sb_where.append(" AND tiny.tinyvillage_type=1");
				} else if (map_where.get("value").equals("0")) {
					sb_where.append(" AND tiny.tinyvillage_type=0");
				}
			}
		}
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		sessionUser = userManager.findUserById(sessionUser.getId());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(sessionUser.getStore_id());
		if (3224 == sessionUser.getUsergroup().getId()) {
			if (store != null) {
				sb_where.append(" AND town.id in(").append(store.getTown_id()).append(")");
			} else {
				sb_where.append(" AND 1=0");
			}

		} else if (3231 == sessionUser.getUsergroup().getId() || 3223 == sessionUser.getUsergroup().getId()
				|| 3225 == sessionUser.getUsergroup().getId() || 3229 == sessionUser.getUsergroup().getId()) {
			if (store != null) {
				sb_where.append(" AND town.id in(").append(store.getTown_id()).append(")");
			} else {
				sb_where.append(" AND 1=0");
			}
		} else if (sessionUser.getZw().equals("地址采集") || sessionUser.getCode().equals("sunning1")) {
			sb_where.append(" and 1=1 ");
		} else {
			// 取得当前登录人 所管理城市
			String cityssql = "";
			List<DistCity> distCityList = userManager.getCurrentUserCity();
			if (distCityList != null && distCityList.size() > 0) {
				for (DistCity d : distCityList) {
					cityssql += "'" + d.getCityname() + "',";
				}
				cityssql = cityssql.substring(0, cityssql.length() - 1);
			}
			if (cityssql != "" && cityssql.length() > 0) {
				String cityId = getCityId(cityssql);
				if (!"".equals(cityId)) {
					cityId = cityId.substring(0, cityId.length() - 1);
					sb_where.append(" and city.id in (" + cityId + ")");
				} else {
					sb_where.append(" and 0=1 ");
				}

			} else {
				sb_where.append(" and 0=1 ");
			}
		}
		;

		System.out.println(sb_where);
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "小区信息");
		map_result.put("data", tinyVillageDao.getTinyVillageList(sb_where.toString(), obj_page));
		return map_result;
	}

	@Override
	public List<TinyVillage> getTinyVillageByName(String name, Integer tinyVillage_type) {
		PageInfo pageInfo = new PageInfo();
		pageInfo.setCurrentPage(0);
		pageInfo.setTotalRecords(10);
		pageInfo.setRecordsPerPage(10);
		FSP fsp = new FSP();
		fsp.setPage(pageInfo);
		fsp.setSort(SortFactory.createSort("id", ISort.ASC));
		IFilter filter = FilterFactory.getStringFilter("name like '%" + name + "%'");
		if (tinyVillage_type != null) {
			filter.appendAnd(FilterFactory.getSimpleFilter("tinyvillage_type", tinyVillage_type)
					.appendAnd(FilterFactory.getSimpleFilter("status", 0)));
		}
		fsp.setUserFilter(filter);
		List<?> lst_data = this.getList(fsp);
		if (lst_data != null && lst_data.size() > 0) {
			return (List<TinyVillage>) lst_data;
		}
		return new ArrayList<TinyVillage>();
	}

	@Override
	public Map<String, Object> getTinyVillageInformationById(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("id", id));
		if (lst_data != null && lst_data.size() > 0) {
			for (Object object : lst_data) {
				TinyVillage tinyVillage = (TinyVillage) object;
				map.put("name", tinyVillage.getName());
				map.put("address", tinyVillage.getAddress());
				if (tinyVillage.getOthername() == null || "null".equals(tinyVillage.getOthername())) {
					map.put("othername", "");
				} else {
					map.put("othername", tinyVillage.getOthername());
				}
				map.put("residents_number", tinyVillage.getResidents_number());
				map.put("sogou_coordinate_x", tinyVillage.getSogou_coordinate_x());
				map.put("sogou_coordinate_y", tinyVillage.getSogou_coordinate_y());
				map.put("gaode_coordinate_y", tinyVillage.getGaode_coordinate_y());
				map.put("gaode_coordinate_x", tinyVillage.getGaode_coordinate_x());
				map.put("baidu_coordinate_y", tinyVillage.getBaidu_coordinate_y());
				map.put("baidu_coordinate_x", tinyVillage.getBaidu_coordinate_x());
				map.put("tinyvillage_type", tinyVillage.getTinyvillage_type());
				map.put("dellable", tinyVillage.getDellable());
				if (tinyVillage.getVillage_id() != null) {
					VillageManager villageManager = (VillageManager) SpringHelper.getBean("villageManager");
					Village village = villageManager.getVillageByIdInfo(tinyVillage.getVillage_id());
					map.put("village_name", village.getName());
					map.put("village_gb_code", village.getGb_code());
					map.put("village_id", village.getId());
					TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
					Town town = townManager.getTownById(village.getTown_id());
					map.put("town_name", town.getName());
					map.put("town_gb_code", town.getGb_code());
					map.put("town_id", town.getId());
				} else {
					if (tinyVillage.getTown_id() != null) {
						TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
						Town town = townManager.getTownById(tinyVillage.getTown_id());
						map.put("town_name", town.getName());
						map.put("town_id", town.getId());
						map.put("town_gb_code", town.getGb_code());
					}
				}
				if (tinyVillage.getJob() != null && !"".equals(tinyVillage.getJob())) {
					CustomerManager customerManager = (CustomerManager) SpringHelper.getBean("customerManager");
					String stts = "";
					String[] split = tinyVillage.getJob().split(",");
					if (split.length > 0) {
						for (int i = 0; i < split.length; i++) {
							String[] strings = split[i].split("-");
							Customer customer = customerManager.findCustomerById(Long.parseLong(strings[0]));
							if ("请选择".equals(strings[2])) {
								stts += "," + customer.getName();
							} else {
								stts += "," + customer.getName() + "(" + strings[2] + ")";
							}
						}
					}
					map.put("showSelectName", stts.substring(1, stts.length()));
				}
				map.put("job", tinyVillage.getJob());

			}
			return map;
		}
		return null;
	}

	@Override
	public TinyVillage updateTinyVillage(TinyVillage tinyVillage) {
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		sessionUser = userManager.findUserById(sessionUser.getId());
		TinyVillage byName = getTinyVilageByName(tinyVillage.getTown_id(), tinyVillage.getName());
		TinyVillage save_tinyVillage = null;
		if (tinyVillage.getId() != null) {
			save_tinyVillage = (TinyVillage) this.getObject(tinyVillage.getId());
		} else if (byName != null) {
			save_tinyVillage = byName;
		} else {
			save_tinyVillage = new TinyVillage();
		}
		save_tinyVillage.setOthername(tinyVillage.getOthername());
		save_tinyVillage.setTinyvillage_type(tinyVillage.getTinyvillage_type());
		save_tinyVillage.setTown_id(tinyVillage.getTown_id());
		save_tinyVillage.setVillage_id(tinyVillage.getVillage_id());
		save_tinyVillage.setAddress(tinyVillage.getAddress());
		save_tinyVillage.setName(tinyVillage.getName());
		save_tinyVillage.setResidents_number(tinyVillage.getResidents_number());
		save_tinyVillage.setJob(tinyVillage.getJob());
		save_tinyVillage.setDellable(tinyVillage.getDellable());
		// save_tinyVillage.setTinyvillage_type(tinyVillage.getTinyvillage_type());
		preObject(save_tinyVillage);
		TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
		tinyVillageManager.saveObject(save_tinyVillage);
		updateOrinsertDuty(save_tinyVillage.getId(), save_tinyVillage.getJob(), 4, sessionUser.getEmployeeId());
		TinyVillageCodeManager tinyVillageCodeManager = (TinyVillageCodeManager) SpringHelper
				.getBean("tinyVillageCodeManager");
		tinyVillageCodeManager.saveTinyVillageCode(save_tinyVillage);
		return save_tinyVillage;
	}

	@Override
	public Map<String, Object> getTinyVillageInfo(QueryConditions conditions) {
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "错误信息");
		map_result.put("data", tinyVillageDao.getTinyVillageInfo(null, obj_page));
		return map_result;
	}

	@Override
	public Map<String, Object> getTinyVillInformation() {
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		Map<String, Object> map = tinyVillageDao.getInfomation();
		return map;
	}

	@Override
	public TinyVillage updateTinyVillageInfo(TinyVillage tinyVillage) {
		TinyVillage save_tinyVillage = null;
		if (tinyVillage.getId() != null) {
			save_tinyVillage = (TinyVillage) this.getObject(tinyVillage.getId());
		} else {
			save_tinyVillage = new TinyVillage();
		}
		save_tinyVillage.setAddress(tinyVillage.getAddress());
		save_tinyVillage.setBaidu_coordinate_x(tinyVillage.getBaidu_coordinate_x());
		save_tinyVillage.setBaidu_coordinate_y(tinyVillage.getBaidu_coordinate_y());
		save_tinyVillage.setSogou_coordinate_x(tinyVillage.getSogou_coordinate_x());
		save_tinyVillage.setSogou_coordinate_y(tinyVillage.getSogou_coordinate_y());
		save_tinyVillage.setGaode_coordinate_x(tinyVillage.getGaode_coordinate_x());
		save_tinyVillage.setGaode_coordinate_y(tinyVillage.getGaode_coordinate_y());
		preObject(save_tinyVillage);
		save(save_tinyVillage);

		return save_tinyVillage;
	}

	@Override
	public Map<String, Object> getTinyVillageInfo(String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<?> lst_data = this.getList(
				FilterFactory.getSimpleFilter("name", name).appendAnd(FilterFactory.getSimpleFilter("status", 0)));
		if (lst_data != null && lst_data.size() > 0 && lst_data.size() < 2) {
			for (Object object : lst_data) {
				TinyVillage tinyVillage = (TinyVillage) object;
				map.put("tinyvillage_name", tinyVillage.getName());
				map.put("tinyvillage_Id", tinyVillage.getId());
			}
			return map;
		}
		return null;
	}

	@Override
	public TinyVillage getTinyVillageInfoByName(String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<?> lst_data = this.getList(
				FilterFactory.getSimpleFilter("name", name).appendAnd(FilterFactory.getSimpleFilter("status", 0)));
		if (lst_data != null && lst_data.size() > 0) {
			return (TinyVillage) lst_data.get(0);
		}
		return null;
	}

	/**
	 * 维护责任表
	 * 
	 * @param id
	 * @param str
	 * @param type
	 */
	public void updateOrinsertDuty(Long id, String str, Integer type, String employee_no) {
		PersonDutyManager personDutyManager = (PersonDutyManager) SpringHelper.getBean("personDutyManager");
		String idString = "";
		if (str != null && !"".equals(str)) {
			String[] slit = str.split(",");
			if (slit.length > 0) {
				for (int i = 0; i < slit.length; i++) {
					String[] strings = slit[i].split("-");
					PersonDuty personDuty = personDutyManager.findPersonDutyBycustomerIdAnddutyIdAndtype(id, type,
							Long.parseLong(strings[0]));
					if (personDuty == null) {
						personDuty = new PersonDuty();
					}
					personDuty.setDuty_id(id);
					personDuty.setType(type);
					personDuty.setCustomer_id(Long.parseLong(strings[0]));
					personDuty.setDuty("请选择".equals(strings[2]) ? null : strings[2]);
					personDuty.setEmployee_no(employee_no);
					preObject(personDuty);
					personDutyManager.saveObject(personDuty);
					idString += "," + strings[0];
				}

			}
		}
		if (idString.length() > 0) {
			idString = idString.substring(1, idString.length());
		}
		PersonDutyDao personDutyDao = (PersonDutyDao) SpringHelper.getBean(PersonDutyDao.class.getName());
		personDutyDao.deletePersonDuty(idString, type, id);
	}

	@Override
	public Result getAppTinyVillageInfo(TinyVillage tinyVillage) {
		VillageManager villageManager = (VillageManager) SpringHelper.getBean("villageManager");
		TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
		Result result = new Result();
		List<TinyVillage> list = new ArrayList<TinyVillage>();
		IFilter filter = FilterFactory.getSimpleFilter("1=1");

		if (ValueUtil.checkValue(tinyVillage.getName())) {
			filter = filter.appendAnd(FilterFactory.getEq("name", tinyVillage.getName())
					.appendAnd(FilterFactory.getSimpleFilter("status", 0)));
		}
		List<?> lst_result = this.getList(filter);
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
		if (lst_result == null || lst_result.size() == 0) {
			result.setTinyVillageList(new ArrayList<TinyVillage>());
		} else {
			List<TinyVillage> listbus = (List<TinyVillage>) lst_result;
			for (TinyVillage tinyVillage1 : listbus) {
				if (tinyVillage1.getVillage_id() != null) {
					Map<String, Object> map = villageManager
							.getVillageTownInfoByVillage_id(tinyVillage1.getVillage_id());
					tinyVillage1.setProvince_name(map.get("province_name").toString());
					tinyVillage1.setProvince_id(
							Long.valueOf(String.valueOf(map.get("province_id").toString())).longValue());
					tinyVillage1.setCity_id(Long.valueOf(String.valueOf(map.get("city_id").toString())).longValue());
					tinyVillage1.setCity_name(map.get("city_name").toString());
					tinyVillage1
							.setVillage_id(Long.valueOf(String.valueOf(map.get("village_id").toString())).longValue());
					tinyVillage1.setVillage_name(map.get("village_name").toString());
					tinyVillage1.setTown_id(Long.valueOf(String.valueOf(map.get("town_id").toString())).longValue());
					tinyVillage1.setTown_name(map.get("town_name").toString());
					tinyVillage1
							.setCounty_id(Long.valueOf(String.valueOf(map.get("county_id").toString())).longValue());
					tinyVillage1.setCounty_name(map.get("county_name").toString());
					list.add(tinyVillage1);
				} else if (tinyVillage1.getTown_id() != null) {
					Map<String, Object> map = townManager.getTownParentInfoByTown_id(tinyVillage1.getTown_id());
					tinyVillage1.setProvince_name(map.get("province_name").toString());
					tinyVillage1.setProvince_id(
							Long.valueOf(String.valueOf(map.get("province_id").toString())).longValue());
					tinyVillage1.setCity_id(Long.valueOf(String.valueOf(map.get("city_id").toString())).longValue());
					tinyVillage1.setCity_name(map.get("city_name").toString());
					tinyVillage1.setTown_id(Long.valueOf(String.valueOf(map.get("town_id").toString())).longValue());
					tinyVillage1.setTown_name(map.get("town_name").toString());
					tinyVillage1
							.setCounty_id(Long.valueOf(String.valueOf(map.get("county_id").toString())).longValue());
					tinyVillage1.setCounty_name(map.get("county_name").toString());
					list.add(tinyVillage1);
				}
			}
			result.setTinyVillageList(list);
		}
		return result;
	}

	@Override
	public Result getAppRepeatTinyVillageInfo(TinyVillage tinyVillage) {
		Result res = new Result();
		TinyVillage villageinfoBy = getAppTinyVillage(tinyVillage);
		if (villageinfoBy != null) {
			if (tinyVillage.getId() != null) {
				if (!villageinfoBy.getId().equals(tinyVillage.getId())) {
					res.setCode(300);
					res.setMessage(tinyVillage.getName() + "已存在");
					return res;
				}
			}
			if (tinyVillage.getId() == null) {
				res.setCode(300);
				res.setMessage(villageinfoBy.getName() + "已存在");
				return res;
			}

		}
		res.setCode(200);
		res.setMessage(tinyVillage.getName() + "不存在");
		return res;
	}

	@Override
	public TinyVillage getAppTinyVillage(TinyVillage tinyVillage) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter(
				"town_id = " + tinyVillage.getTown_id() + " and status=0 and name ='" + tinyVillage.getName() + "'"));
		if (lst_vilage_data != null && lst_vilage_data.size() > 0) {
			return (TinyVillage) lst_vilage_data.get(0);
		}
		return null;
	}

	@Override
	public Result saveOrUpdateTinyVillageApp(TinyVillage tinyVillage) {
		TinyVillageCodeManager tinyVillageCodeManager = (TinyVillageCodeManager) SpringHelper
				.getBean("tinyVillageCodeManager");
		// 调用查询是否绑定区块验证
		NewsDao newsDao = (NewsDao) SpringHelper.getBean(NewsDao.class.getName());
		TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
		TinyVillage appTinyVillage = getAppTinyVillage(tinyVillage);
		Result result = new Result();
		if (tinyVillage.getId() != null) {
			TinyVillage tinyVillage2 = getTinyVillageById(tinyVillage.getId());
			AreaInfoManager areaInfoManager = (AreaInfoManager) SpringHelper.getBean("areaInfoManager");
			List<AreaInfo> list = areaInfoManager.findAreaInfoByTinyvillageId(tinyVillage.getId());
			if (list != null && list.size() > 0) {
				if (!tinyVillage2.getVillage_id().equals(tinyVillage.getVillage_id())) {
					result.setCode(300);
					result.setMessage("保存失败");
					return result;
				}
			}
			BeanUtils.copyProperties(tinyVillage, tinyVillage2,
					new String[] { "id", "version", "status", "create_user", "create_user_id", "create_time",
							"update_user", "update_user_id", "update_time", "employee_no", "job", "gaode_coordinate_x",
							"gaode_coordinate_y", "sogou_coordinate_x", "sogou_coordinate_y", "baidu_coordinate_x",
							"baidu_coordinate_y" });
			if (tinyVillage2.getEmployee_no() == null) {
				tinyVillage2.setEmployee_no(tinyVillage.getEmployee_no());
			}
			if (tinyVillage2.getCreate_user() == null) {
				tinyVillage2.setCreate_user(tinyVillage.getCreate_user());
				tinyVillage2.setCreate_user_id(tinyVillage.getCreate_user_id());
			}
			preObject(tinyVillage2);
			tinyVillage2.setUpdate_user(tinyVillage.getCreate_user());
			tinyVillage2.setUpdate_user_id(tinyVillage.getCreate_user_id());
			tinyVillageManager.saveObject(tinyVillage2);
			tinyVillageCodeManager.saveTinyVillageCode(tinyVillage2);
			if (appTinyVillage != null && !appTinyVillage.getId().equals(tinyVillage2.getId())) {
				// newsDao.deleteObject(appTinyVillage);
			}
		} else {
			if (appTinyVillage == null) {
				appTinyVillage = new TinyVillage();
				appTinyVillage.setCreate_user(tinyVillage.getCreate_user());
				appTinyVillage.setCreate_user_id(tinyVillage.getCreate_user_id());
			}
			BeanUtils.copyProperties(tinyVillage, appTinyVillage,
					new String[] { "id", "version", "status", "create_user", "create_user_id", "create_time",
							"update_user", "update_user_id", "update_time", "employee_no", "job", "gaode_coordinate_x",
							"gaode_coordinate_y", "sogou_coordinate_x", "sogou_coordinate_y", "baidu_coordinate_x",
							"baidu_coordinate_y" });

			if (appTinyVillage.getEmployee_no() == null) {
				appTinyVillage.setEmployee_no(tinyVillage.getEmployee_no());
			}
			preObject(appTinyVillage);
			appTinyVillage.setUpdate_user(tinyVillage.getCreate_user());
			appTinyVillage.setUpdate_user_id(tinyVillage.getCreate_user_id());
			tinyVillageManager.saveObject(appTinyVillage);
			tinyVillageCodeManager.saveTinyVillageCode(appTinyVillage);
		}
		result.setCode(200);
		result.setMessage("保存成功");
		return result;
	}

	@Override
	public TinyVillage getTinyVilageByNameByTiny(TinyVillage tinyVillage) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("name", tinyVillage.getName())
				.appendAnd(FilterFactory.getSimpleFilter("town_id", tinyVillage.getTown_id()))
				.appendAnd(FilterFactory.getSimpleFilter("status", 0)));
		if (tinyVillage.getId() != null && !"".equals(tinyVillage.getId())) {
			if (lst_data != null && lst_data.size() > 0) {
				TinyVillage tinyVillage2 = (TinyVillage) lst_data.get(0);
				if (!tinyVillage2.getId().equals(tinyVillage.getId())) {
					return (TinyVillage) lst_data.get(0);
				}
				return null;
			}
		} else {
			if (lst_data != null && lst_data.size() > 0) {
				return (TinyVillage) lst_data.get(0);
			}
		}

		return null;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageByNameAndTown(String name, Integer tinyVillage_type, String town_id,
			String village_id) {
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());

		List<Map<String, Object>> list = tinyVillageDao.getAllTinVillageByNameAndTown(name, tinyVillage_type, town_id,
				village_id);
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	@Override
	public List<TinyVillage> getTinyVillageByNameAndTown_id(TinyVillage tinyVillage) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter(
				"town_id=" + tinyVillage.getTown_id() + " AND status=0 And name like '%" + tinyVillage.getName()
						+ "%' and (tinyvillage_type=1 OR tinyvillage_type is NULL or tinyvillage_type='')"));
		if (lst_data != null && lst_data.size() > 0) {
			return (List<TinyVillage>) lst_data;
		}
		return null;
	}

	/**
	 * 根据当前登录的用户获取所有的城市id
	 */
	public String getCityId(String string) {
		CityManager cityManager = (CityManager) SpringHelper.getBean("cityManager");
		String[] cityName = string.split(",");
		String cityId = "";
		for (int i = 0; i < cityName.length; i++) {
			List<City> list = cityManager.getCityDataByName(cityName[i].replaceAll("'", ""));
			if (list != null && list.size() > 0) {
				for (City city : list) {
					cityId += city.getId() + ",";
				}
			}
		}
		return cityId;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageByNameAndCity(String name, Integer tinyVillage_type, Long city_id) {
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		List<Map<String, Object>> list = tinyVillageDao.getAllTinVillageByNameAndCity(name, tinyVillage_type, city_id);
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	@Override
	public List<TinyVillage> getTinyVillageByNameAndVillage_id(TinyVillage tinyVillage) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("village_id=" + tinyVillage.getVillage_id()
				+ " AND status=0 And name like '%" + tinyVillage.getName() + "%'"));
		if (lst_data != null && lst_data.size() > 0) {
			return (List<TinyVillage>) lst_data;
		}
		return null;
	}

	@Override
	public List<TinyVillage> getTinyVillageByVillageId(Long VillageId) {
		List<TinyVillage> list = new ArrayList<TinyVillage>();
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("status=0 and village_id=" + VillageId));
		if (lst_data != null && lst_data.size() > 0) {
			for (Object object : lst_data) {
				list.add((TinyVillage) object);
			}
			return list;
		}

		return null;
	}

	@Override
	public TinyVillage saveTinyvillageList(List<Map<String, Object>> tiny) {
		TinyVillageCodeManager tinyVillageCodeManager = (TinyVillageCodeManager) SpringHelper
				.getBean("tinyVillageCodeManager");
		HouseBuildingManager houseBuildingManager = (HouseBuildingManager) SpringHelper.getBean("houseBuildingManager");
		User sessionUser = null;
		HouseBuilding houseBuilding = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
		HouseDao houseDao = (HouseDao) SpringHelper.getBean(HouseDao.class.getName());
		BuildingDao buildingDao = (BuildingDao) SpringHelper.getBean(BuildingDao.class.getName());
		TinyVillage tinyVillages = null;
		// 执行修改小区的方法
		// 社区id,街道id;
		Long village_id = null, town_id = null;
		// 小区类型
		int tinyvillage_type = 0;
		System.out.println("---------执行修改小区的方法");
		if (tiny != null && tiny.size() > 0) {
			for (Map<String, Object> map : tiny) {
				String idString = map.get("id") + "";
				String name = map.get("name") + "";
				String buildings = map.get("buildings") + "";
				String othername = map.get("othername") + "";
				if (idString != null && !"".equals(idString) && !"null".equals(idString)) {
					TinyVillage tinyVillage = getTinyVillageById(Long.parseLong(idString));
					tinyvillage_type = tinyVillage.getTinyvillage_type();
					village_id = tinyVillage.getVillage_id();
					town_id = tinyVillage.getTown_id();
					tinyVillage.setName(name);
					tinyVillage.setOthername(othername);
					preObject(tinyVillage);
					tinyVillageManager.saveObject(tinyVillage);
					tinyVillageCodeManager.saveTinyVillageCode(tinyVillage);
				} else {
					tinyVillages = new TinyVillage();
					tinyVillages.setName(name);
					tinyVillages.setTinyvillage_type(tinyvillage_type);
					tinyVillages.setOthername(othername);
					tinyVillages.setVillage_id(village_id);
					tinyVillages.setTown_id(town_id);
					preObject(tinyVillages);
					tinyVillageManager.saveObject(tinyVillages);
					tinyVillageCodeManager.saveTinyVillageCode(tinyVillages);
					houseBuilding = new HouseBuilding();
					houseBuilding.setBuildingid(buildings);
					houseBuilding.setTinyvillageId(tinyVillages.getId());
					preObject(houseBuilding);
					houseBuildingManager.saveObject(houseBuilding);
					// houseDao.updateHousetinyvillage(buildings,
					// tinyVillages.getId(),sessionUser);
					buildingDao.updateBuildingtinyvillage(buildings, tinyVillages.getId(), sessionUser);
				}
			}
		}
		return tinyVillages;
	}

	@Override
	public List<TinyVillage> findTinyVillageByvillage_idOrtown_id(TinyVillage tinyVillage) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("village_id=" + tinyVillage.getVillage_id()
				+ " and status=0 and name like'%" + tinyVillage.getName() + "%'"));
		if (lst_data != null && lst_data.size() > 0) {
			return (List<TinyVillage>) lst_data;
		} else {
			List<?> list_data = this.getList(FilterFactory.getSimpleFilter("town_id=" + tinyVillage.getTown_id()
					+ " and status=0 and name like'%" + tinyVillage.getName() + "%'"));
			if (list_data != null && list_data.size() > 0) {
				return (List<TinyVillage>) list_data;
			}
		}
		return null;
	}

	@Override
	public Integer findHouseOrBuilding(Long id) {
		// 根据小区查找所有的楼房信息
		BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
		HouseManager houseManager = (HouseManager) SpringHelper.getBean("houseManager");
		AreaInfoManager areaInfoManager = (AreaInfoManager) SpringHelper.getBean("areaInfoManager");
		List<Building> list = buildingManager.findBuildingByTinyvillageId(id);
		List<House> findHouseDataBytinyvillageId = houseManager.findHouseDataBytinyvillageId(id);
		// List<AreaInfo> findAreaInfoByTinyvillageId =
		// areaInfoManager.findAreaInfoByTinyvillageId(id);
		TinyVillage tinyVillageAreaByTinyId = areaInfoManager.findTinyVillageAreaByTinyId(id);
		if (list == null && findHouseDataBytinyvillageId == null && tinyVillageAreaByTinyId == null) {
			return 0;
		}
		return 1;
	}

	@Override
	public TinyVillage deleteTinyVillageById(Long id) {

		ComnunityAreaManager comnunityAreaManager = (ComnunityAreaManager) SpringHelper.getBean("comnunityAreaManager");
		TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
		BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
		List<Map<String, Object>> tinyvillageId = findTinyAreaByTinyvillageId(id);
		if (tinyvillageId != null && tinyvillageId.size() > 0) {
			MongoDBManager mongoDBManager = (MongoDBManager) SpringHelper.getBean("mongoDBManager");
			TinyVillageCoordDto coordDto = new TinyVillageCoordDto();
			coordDto.setTiny_village_id(id);
			Map<String, Object> map = mongoDBManager.deleteTinyVillageCoord(coordDto);
			if (!"200".equals((map.get("code") + ""))) {
				return null;
			}
		}
		List<Building> list = buildingManager.findBuildingPingandbusinessByTinyvillageId(id);
		if (list != null && list.size() > 0) {
			for (Building building : list) {
				building.setStatus(1);
				preObject(building);
				buildingManager.saveObject(building);
			}
		}
		List<ComnunityArea> areaByTinyvillageId = comnunityAreaManager.findComnunityAreaByTinyvillageId(id);
		if (areaByTinyvillageId != null && areaByTinyvillageId.size() > 0) {
			for (ComnunityArea comnunityArea : areaByTinyvillageId) {
				comnunityAreaManager.removeObject(comnunityArea);
			}
		}
		TinyVillage tinyVillage = getTinyVillageById(id);
		tinyVillage.setStatus(1);
		preObject(tinyVillage);
		tinyVillageManager.saveObject(tinyVillage);
		return tinyVillage;
	}

	@Override
	public Map<String, Object> findTinyvillageandplaceData(QueryConditions conditions) {
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();

		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("tinyvillage_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and (tiny.name like '").append(map_where.get("value"))
						.append("' or tiny.othername like '").append(map_where.get("value")).append("')");
			} else if ("placename".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and coss.placename like '").append(map_where.get("value")).append("'");
			} else if ("village_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and vill.name like '").append(map_where.get("value")).append("'");
			} else if ("communityAreastatus".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				System.out.println(map_where.get("value"));
				if (map_where.get("value").equals("1")) {
					sb_where.append(" AND coss.placename is not NULL");
				} else if (map_where.get("value").equals("2")) {
					sb_where.append(" AND coss.placename is NULL");
				}
			}
		}
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		sessionUser = userManager.findUserById(sessionUser.getId());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(sessionUser.getStore_id());
		if (3224 == sessionUser.getUsergroup().getId()) {
			if (store != null) {
				sb_where.append(" AND town.id in(").append(store.getTown_id()).append(")");
			} else {
				sb_where.append(" AND 1=0");
			}

		} else if (3231 == sessionUser.getUsergroup().getId() || 3223 == sessionUser.getUsergroup().getId()
				|| 3225 == sessionUser.getUsergroup().getId() || 3229 == sessionUser.getUsergroup().getId()) {
			if (store != null) {
				sb_where.append(" AND town.id in(").append(store.getTown_id()).append(")");
			} else {
				sb_where.append(" AND 1=0");
			}
		} else if (sessionUser.getZw().equals("地址采集") || sessionUser.getCode().equals("sunning1")) {
			sb_where.append(" and 1=1 ");
		} else {
			// 取得当前登录人 所管理城市
			String cityssql = "";
			List<DistCity> distCityList = userManager.getCurrentUserCity();
			if (distCityList != null && distCityList.size() > 0) {
				for (DistCity d : distCityList) {
					cityssql += "'" + d.getCityname() + "',";
				}
				cityssql = cityssql.substring(0, cityssql.length() - 1);
			}
			if (cityssql != "" && cityssql.length() > 0) {
				String cityId = getCityId(cityssql);
				if (!"".equals(cityId)) {
					cityId = cityId.substring(0, cityId.length() - 1);
					sb_where.append(" and city.id in (" + cityId + ")");
				} else {
					sb_where.append(" and 0=1 ");
				}

			} else {
				sb_where.append(" and 0=1 ");
			}
		}
		;

		Integer integer = tinyVillageDao.getCount(sb_where.toString());
		obj_page.setTotalRecords(integer);
		System.out.println(sb_where);
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "小区片区信息");
		map_result.put("data", tinyVillageDao.findTinyVillageList(sb_where.toString(), obj_page, store.getStoreno()));
		return map_result;
	}

	@Override
	public Map<String, Object> getTinyVillageDataOrderAddress(QueryConditions conditions) {
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();

		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("tinyvillage_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and (tiny.name like '").append(map_where.get("value"))
						.append("' or tiny.othername like '").append(map_where.get("value")).append("')");
			} else if ("name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and vill.name like '").append(map_where.get("value")).append("'");
			} else if ("ifNull".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				System.out.println(map_where.get("value"));
				if (map_where.get("value").equals("1")) {
					sb_where.append(" AND tiny.village_id is NULL");
				} else if (map_where.get("value").equals("0")) {
					sb_where.append(" AND tiny.village_id is not NULL");
				}
			} else if ("village_id".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND tiny.village_id in (" + map_where.get("value").toString() + ")");
			} else if ("town_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and town.name like '").append(map_where.get("value")).append("'");
			} else if ("type_id".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				System.out.println(map_where.get("value"));
				if (map_where.get("value").equals("1")) {
					sb_where.append(" AND tiny.tinyvillage_type=1");
				} else if (map_where.get("value").equals("0")) {
					sb_where.append(" AND tiny.tinyvillage_type=0");
				}
			}
		}
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		sessionUser = userManager.findUserById(sessionUser.getId());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(sessionUser.getStore_id());
		if (3224 == sessionUser.getUsergroup().getId()) {
			if (store != null) {
				sb_where.append(" AND town.id in(").append(store.getTown_id()).append(")");
			} else {
				sb_where.append(" AND 1=0");
			}

		} else if (3231 == sessionUser.getUsergroup().getId() || 3223 == sessionUser.getUsergroup().getId()
				|| 3225 == sessionUser.getUsergroup().getId() || 3229 == sessionUser.getUsergroup().getId()) {
			if (store != null) {
				sb_where.append(" AND town.id in(").append(store.getTown_id()).append(")");
			} else {
				sb_where.append(" AND 1=0");
			}
		} else if (sessionUser.getZw().equals("地址采集") || sessionUser.getCode().equals("sunning1")) {
			sb_where.append(" and 1=1 ");
		} else {
			// 取得当前登录人 所管理城市
			String cityssql = "";
			List<DistCity> distCityList = userManager.getCurrentUserCity();
			if (distCityList != null && distCityList.size() > 0) {
				for (DistCity d : distCityList) {
					cityssql += "'" + d.getCityname() + "',";
				}
				cityssql = cityssql.substring(0, cityssql.length() - 1);
			}
			if (cityssql != "" && cityssql.length() > 0) {
				String cityId = getCityId(cityssql);
				if (!"".equals(cityId)) {
					cityId = cityId.substring(0, cityId.length() - 1);
					sb_where.append(" and city.id in (" + cityId + ")");
				} else {
					sb_where.append(" and 0=1 ");
				}

			} else {
				sb_where.append(" and 0=1 ");
			}
		}
		;
		System.out.println(sb_where);
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "小区信息");
		map_result.put("data", tinyVillageDao.getOrderAddressTinyvillageList(sb_where.toString(), obj_page));
		return map_result;
	}

	@Override
	public void UpdateNumbertiny(String ids) {
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		tinyVillageDao.uptinyvillagexuhao(ids);
	}

	@Override
	public List<TinyVillage> findTinyvillageofHouseandBuilding(String str) {
		List<TinyVillage> list = new ArrayList<TinyVillage>();
		String[] split = str.split(",");
		for (String string : split) {
			Integer integer = findHouseOrBuilding(Long.parseLong(string));
			if (integer > 0) {
				TinyVillage tinyVillage = getTinyVillageById(Long.parseLong(string));
				list.add(tinyVillage);
			}
		}
		if (list.size() > 0) {
			return list;
		}
		return null;
	}

	@Override
	public TinyVillage deletemouthTinyVillage(String str) {
		TinyVillage tinyVillage = null;
		String[] split = str.split(",");
		for (String string : split) {
			tinyVillage = deleteTinyVillageById(Long.parseLong(string));
			if (tinyVillage == null) {
				return null;
			}
		}
		return tinyVillage;
	}

	@Override
	public List<Map<String, Object>> findmouthTinyvillage(Long id) {
		TinyCoordinateManager tinyCoordinateManager = (TinyCoordinateManager) SpringHelper
				.getBean("tinyCoordinateManager");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		// 根据当前用户获得当前登录的门店
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		Store store = userManager.findUserStore();
		// 根据当前门店获取该门店所有已经划片的小区
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		List<TinyVillage> dataBystoreId = tinyVillageDao.findMounthTinyVillageDataBystoreId(store.getStore_id());
		if (dataBystoreId != null && dataBystoreId.size() > 0) {
			for (TinyVillage tinyVillage : dataBystoreId) {
				map = new HashMap<String, Object>();
				List<TinyCoordinate> findTinyCoordinateByTinyId = tinyCoordinateManager
						.findTinyCoordinateByTinyId(tinyVillage.getId());
				map.put("tinyvillage", tinyVillage);
				map.put("tinyCoors", findTinyCoordinateByTinyId);
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public TinyVillage addtinyvillagedellable(String ids) {
		TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
		TinyVillage tinyVillage = null;
		String[] split = ids.split(",");
		for (String string : split) {
			tinyVillage = getTinyVillageById(Long.parseLong(string));
			tinyVillage.setDellable(1);
			preObject(tinyVillage);
			tinyVillageManager.saveObject(tinyVillage);
		}
		return tinyVillage;
	}

	@Override
	public List<Map<String, Object>> findTinyvillagetinycoodData(Long id) {
		TinyCoordinateManager tinyCoordinateManager = (TinyCoordinateManager) SpringHelper
				.getBean("tinyCoordinateManager");
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		// 根据当前用户获得当前登录的门店
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		Store store = userManager.findUserStore();
		List<TinyVillage> dataBytownId = tinyVillageDao.findTinyCoordDataBytownId(store.getTown_id(), id);
		if (dataBytownId != null && dataBytownId.size() > 0) {
			for (TinyVillage tinyVillage : dataBytownId) {
				List<TinyCoordinate> tinyId = tinyCoordinateManager.findTinyCoordinateByTinyId(tinyVillage.getId());
				if (tinyId != null && tinyId.size() > 0) {
					map = new HashMap<String, Object>();
					map.put("tinycood", tinyId);
					list.add(map);
				}
			}
			return list;
		}
		return null;
	}

	@Override
	public void syncTinyVillageCood() {
		TinyVillageCodeManager tinyVillageCodeManager = (TinyVillageCodeManager) SpringHelper
				.getBean("tinyVillageCodeManager");
		// 获取所有的小区
		List<TinyVillage> lst_data = (List<TinyVillage>) this
				.getList(FilterFactory.getSimpleFilter("name!='' and name is not NULL and town_id is not null"));
		if (lst_data != null && lst_data.size() > 0) {
			for (TinyVillage tinyVillage : lst_data) {
				if (tinyVillage.getTown_id() != null && !"".equals(tinyVillage.getTown_id())) {
					TinyVillageCode saveTinyVillageCode = tinyVillageCodeManager.saveTinyVillageCode(tinyVillage);
				}
			}
		}

	}

	@Override
	public List<Map<String, Object>> findTinyvillageByTownId(Long store_id) {
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(store_id);
		MongoDBManager mongoDBManager = (MongoDBManager) SpringHelper.getBean("mongoDBManager");
		Map<String, Object> storeServiceArea = mongoDBManager.getStoreServiceArea(store.getPlatformid());
		Map<String, Object> allTinyVillageOfStore = mongoDBManager.getAllTinyVillageOfStore(store.getStore_id());
		Map<String, Object> selecTinyVillageCoord = mongoDBManager.selecTinyVillageCoord(store.getStore_id());
		Map<String, Object> storeMap = new HashMap<String, Object>();
		storeMap.put("store", store);
		System.out.println(storeServiceArea.get("data"));
		System.out.println(allTinyVillageOfStore.get("data"));
		System.out.println(selecTinyVillageCoord.get("data"));
		list.add(storeServiceArea);
		list.add(allTinyVillageOfStore);
		list.add(selecTinyVillageCoord);
		list.add(storeMap);
		return list;
	}

	@Override
	public List<TinyVillage> getTinyVillageNotDellable(Long villageId) {
		List<TinyVillage> list = new ArrayList<TinyVillage>();
		List<?> lst_data = this.getList(FilterFactory
				.getSimpleFilter("(dellable <> 1 or dellable is null) and status=0 and village_id=" + villageId));
		if (lst_data != null && lst_data.size() > 0) {
			for (Object object : lst_data) {
				list.add((TinyVillage) object);
			}
			return list;
		}

		return null;
	}

	@Override
	public TinyVillage updateTinyvillageType(TinyVillage tinyVillage) {
		TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
		TinyVillage tinyVillageById = getTinyVillageById(tinyVillage.getId());
		tinyVillageById.setTinyvillage_type(tinyVillage.getTinyvillage_type());
		preObject(tinyVillageById);
		tinyVillageManager.saveObject(tinyVillageById);
		return tinyVillageById;
	}

	@Override
	public List<Map<String, Object>> findTinyAreaByTinyvillageId(Long tinyvillageId) {
		TinyAreaDao tinyAreaDao = (TinyAreaDao) SpringHelper.getBean(TinyAreaDao.class.getName());
		List<Map<String, Object>> list = tinyAreaDao.selectinyAreaByTinyVillageId(tinyvillageId);
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findTinyAreaByTinyIds(String tinyids) {
		String[] split = tinyids.split(",");
		for (String string : split) {
			List<Map<String, Object>> list = findTinyAreaByTinyvillageId(Long.parseLong(string));
			if (list != null && list.size() > 0) {
				return list;
			}
		}
		return null;
	}

	@Override
	public Map<String, Object> queryAboutOfTinyvillage(DynamicDto dynamicDto, PageInfo pageInfo) {
		TinyAreaDao tinyAreaDao = (TinyAreaDao) SpringHelper.getBean(TinyAreaDao.class.getName());
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder sb_where = new StringBuilder();
		// 获取当前用户
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		try {
			if (dynamicDto.getTarget() == 0) {// 总部
				if (dynamicDto.getCityName() != null && !"".equals(dynamicDto.getCityName())) {
					sb_where.append(" and city.name like '%" + dynamicDto.getCityName() + "%'");
				}
				if (dynamicDto.getTinyvillageName() != null && !"".equals(dynamicDto.getTinyvillageName())) {
					sb_where.append(" and tiny.name like '%" + dynamicDto.getTinyvillageName() + "%'");
				}
				if (dynamicDto.getTinyvillageCode() != null && !"".equals(dynamicDto.getTinyvillageCode())) {
					sb_where.append(" and codd.code ='" + dynamicDto.getTinyvillageCode() + "'");
				}
				if (dynamicDto.getTownName() != null && !"".equals(dynamicDto.getTownName())) {
					sb_where.append(" and town.name like '%" + dynamicDto.getTownName() + "%'");
				}
				if (dynamicDto.getVillageName() != null && !"".equals(dynamicDto.getVillageName())) {
					sb_where.append(" and vill.name like '%" + dynamicDto.getVillageName() + "%'");
				}
				if (dynamicDto.getStoreName() != null && !"".equals(dynamicDto.getStoreName())) {
					result = tinyAreaDao.findTinyVillageByStoreNo(dynamicDto.getStoreName(), dynamicDto.getCityName(),
							sb_where.toString(), pageInfo);
				} else {
					result = tinyVillageDao.queryAboutTinyvillage(sb_where.toString(), pageInfo);
				}

			} else if (dynamicDto.getTarget() == 1) {// 城市总监
				if (dynamicDto.getCityName() != null && !"".equals(dynamicDto.getCityName())) {
					sb_where.append(" and city.name like '%" + dynamicDto.getCityName() + "%'");
				}
				if (dynamicDto.getTinyvillageName() != null && !"".equals(dynamicDto.getTinyvillageName())) {
					sb_where.append(" and tiny.name like '%" + dynamicDto.getTinyvillageName() + "%'");
				}
				if (dynamicDto.getTinyvillageCode() != null && !"".equals(dynamicDto.getTinyvillageCode())) {
					sb_where.append(" and codd.code ='" + dynamicDto.getTinyvillageCode() + "'");
				}
				if (dynamicDto.getTownName() != null && !"".equals(dynamicDto.getTownName())) {
					sb_where.append(" and town.name like '%" + dynamicDto.getTownName() + "%'");
				}
				if (dynamicDto.getVillageName() != null && !"".equals(dynamicDto.getVillageName())) {
					sb_where.append(" and vill.name like '%" + dynamicDto.getVillageName() + "%'");
				}
				if (dynamicDto.getStoreName() != null && !"".equals(dynamicDto.getStoreName())) {
					result = tinyAreaDao.findTinyVillageByStoreNo(dynamicDto.getStoreName(), dynamicDto.getCityName(),
							sb_where.toString(), pageInfo);
				} else {
					result = tinyVillageDao.queryAboutTinyvillage(sb_where.toString(), pageInfo);
				}
			} else if (dynamicDto.getTarget() == 2) {// 店长
				if (dynamicDto.getCityName() != null && !"".equals(dynamicDto.getCityName())) {
					sb_where.append(" and city.name like '%" + dynamicDto.getCityName() + "%'");
				}
				if (dynamicDto.getTinyvillageName() != null && !"".equals(dynamicDto.getTinyvillageName())) {
					sb_where.append(" and tiny.name like '%" + dynamicDto.getTinyvillageName() + "%'");
				}
				if (dynamicDto.getTinyvillageCode() != null && !"".equals(dynamicDto.getTinyvillageCode())) {
					sb_where.append(" and codd.code ='" + dynamicDto.getTinyvillageCode() + "'");
				}
				if (dynamicDto.getTownName() != null && !"".equals(dynamicDto.getTownName())) {
					sb_where.append(" and town.name like '%" + dynamicDto.getTownName() + "%'");
				}
				if (dynamicDto.getVillageName() != null && !"".equals(dynamicDto.getVillageName())) {
					sb_where.append(" and vill.name like '%" + dynamicDto.getVillageName() + "%'");
				}
				if (dynamicDto.getStoreName() != null && !"".equals(dynamicDto.getStoreName())) {
					result = tinyAreaDao.findTinyVillageByStoreNo(dynamicDto.getStoreName(), dynamicDto.getCityName(),
							sb_where.toString(), pageInfo);
				} else {
					result = tinyVillageDao.queryAboutTinyvillage(sb_where.toString(), pageInfo);
				}
			} else {
				JSONObject temp = new JSONObject();
				temp.put("data", "");
				temp.put("message", "该用户无权限");
				result.put("status", "storefail");
				result.put("data", temp.toString());
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject temp = new JSONObject();
			temp.put("data", "");
			temp.put("message", "系统错误！");
			result.put("status", "storefail");
			return result;
		}
		return result;
	}

	@Override
	public Map<String, Object> exportAboutOfTinyvillage(DynamicDto dynamicDto) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = this.exportAboutOfTinyvillageInfo(dynamicDto);
		if (map.get("data") != null) {// 成功返回数据

			List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("data");
			if (list != null && list.size() > 0) {
				String str_file_dir_path = PropertiesUtil.getValue("file.root");
				String str_web_path = PropertiesUtil.getValue("file.web.root");

				HSSFWorkbook wb = new HSSFWorkbook();
				// 创建Excel的工作sheet,对应到一个excel文档的tab

				setCellStyle_common(wb);
				setHeaderStyle(wb);
				HSSFSheet sheet = wb.createSheet("小区信息");
				HSSFRow row = sheet.createRow(0);
				String[] str_headers = { "城市", "小区名称", "小区编号", "小区居民户数", "小区面积", "已消费人数", "小区地图", "删除标签", "所属门店",
						"所属街道", "所属社区", "小区类型" };
				String[] headers_key = { "city_name", "name", "tinyvillage_code", "residents_number", "vallage_area",
						"consumer_number", "tiny_map", "dellable", "store_name", "town_name", "village_name",
						"tinyvillage_type" };
				for (int i = 0; i < str_headers.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(getHeaderStyle());
					cell.setCellValue(new HSSFRichTextString(str_headers[i]));
				}

				for (int i = 0; i < list.size(); i++) {
					row = sheet.createRow(i + 1);
					for (int cellIndex = 0; cellIndex < headers_key.length; cellIndex++) {
						setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
					}
				}

				File file_xls = new File(
						str_file_dir_path + File.separator + System.currentTimeMillis() + "_aboutTinyvillage.xls");
				if (file_xls.exists()) {
					file_xls.delete();
				}
				FileOutputStream os = null;
				try {
					os = new FileOutputStream(file_xls.getAbsoluteFile());
					wb.write(os);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				result.put("message", "导出成功！");
				result.put("status", "success");
				result.put("data", str_web_path.concat(file_xls.getName()));// 测试环境修改str_file_dir_path
			} else {
				result.put("message", "没有数据！");
				result.put("status", "null");
			}

		} else {
			result.put("message", "请重新操作！");
			result.put("status", "fail");
		}

		return result;
	}

	private void setCellStyle_common(Workbook wb) {
		cellStyle_common = wb.createCellStyle();
		cellStyle_common.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
		cellStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 垂直居中
	}

	private CellStyle getCellStyle_common() {
		return cellStyle_common;
	}

	private HSSFCellStyle style_header = null;
	private CellStyle cellStyle_common = null;

	private HSSFCellStyle getHeaderStyle() {
		return style_header;
	}

	private void setHeaderStyle(HSSFWorkbook wb) {

		// 创建单元格样式
		style_header = wb.createCellStyle();
		style_header.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style_header.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style_header.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		style_header.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// 设置边框
		style_header.setBottomBorderColor(HSSFColor.BLACK.index);
		style_header.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style_header.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style_header.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style_header.setBorderTop(HSSFCellStyle.BORDER_THIN);

	}

	public void setCellValue(Row obj_row, int nCellIndex, Object value) {
		Cell cell = obj_row.createCell(nCellIndex);
		cell.setCellStyle(getCellStyle_common());
		if (nCellIndex == 11) {
			if (value != null) {
				if (Integer.parseInt(value + "") == 0) {
					value = "平房";
				} else if (Integer.parseInt(value + "") == 1) {
					value = "楼房";
				} else if (Integer.parseInt(value + "") == 2) {
					value = "商业";
				} else if (Integer.parseInt(value + "") == 3) {
					value = "公园/广场";
				} else if (Integer.parseInt(value + "") == 4) {
					value = "混合类型";
				} else if (Integer.parseInt(value + "") == 5) {
					value = "其它";
				}
			} else {
				value = "";
			}

		}
		cell.setCellValue(new HSSFRichTextString(value == null ? null : value.toString()));
	}

	@Override
	public Map<String, Object> exportAboutOfTinyvillageInfo(DynamicDto dynamicDto) {
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder sb_where = new StringBuilder();
		// 获取当前用户
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		try {
			if (dynamicDto.getTarget() == 0) {// 总部
				if (dynamicDto.getCityName() != null && !"".equals(dynamicDto.getCityName())) {
					sb_where.append(" and city.name like '%" + dynamicDto.getCityName() + "%'");
				}
				if (dynamicDto.getStoreName() != null && !"".equals(dynamicDto.getStoreName())) {
					sb_where.append(" and store.name ='" + dynamicDto.getStoreName() + "'");
				}
				if (dynamicDto.getTinyvillageName() != null && !"".equals(dynamicDto.getTinyvillageName())) {
					sb_where.append(" and tiny.name like '%" + dynamicDto.getTinyvillageName() + "%'");
				}
				if (dynamicDto.getTinyvillageCode() != null && !"".equals(dynamicDto.getTinyvillageCode())) {
					sb_where.append(" and codd.code ='" + dynamicDto.getTinyvillageCode() + "'");
				}
				if (dynamicDto.getTownName() != null && !"".equals(dynamicDto.getTownName())) {
					sb_where.append(" and town.name like '%" + dynamicDto.getTownName() + "%'");
				}
				if (dynamicDto.getVillageName() != null && !"".equals(dynamicDto.getVillageName())) {
					sb_where.append(" and vill.name like '%" + dynamicDto.getVillageName() + "%'");
				}
				result = tinyVillageDao.exportAboutTinyvillage(sb_where.toString());
			} else if (dynamicDto.getTarget() == 1) {// 城市总监
				if (dynamicDto.getCityName() != null && !"".equals(dynamicDto.getCityName())) {
					sb_where.append(" and city.name like '%" + dynamicDto.getCityName() + "%'");
				}
				if (dynamicDto.getStoreName() != null && !"".equals(dynamicDto.getStoreName())) {
					sb_where.append(" and store.name ='" + dynamicDto.getStoreName() + "'");
				}
				if (dynamicDto.getTinyvillageName() != null && !"".equals(dynamicDto.getTinyvillageName())) {
					sb_where.append(" and tiny.name like '%" + dynamicDto.getTinyvillageName() + "%'");
				}
				if (dynamicDto.getTinyvillageCode() != null && !"".equals(dynamicDto.getTinyvillageCode())) {
					sb_where.append(" and codd.code ='" + dynamicDto.getTinyvillageCode() + "'");
				}
				if (dynamicDto.getTownName() != null && !"".equals(dynamicDto.getTownName())) {
					sb_where.append(" and town.name like '%" + dynamicDto.getTownName() + "%'");
				}
				if (dynamicDto.getVillageName() != null && !"".equals(dynamicDto.getVillageName())) {
					sb_where.append(" and vill.name like '%" + dynamicDto.getVillageName() + "%'");
				}
				result = tinyVillageDao.exportAboutTinyvillage(sb_where.toString());
			} else {
				JSONObject temp = new JSONObject();
				temp.put("data", "");
				temp.put("message", "该用户无权限");
				result.put("status", "storefail");
				result.put("data", temp.toString());
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject temp = new JSONObject();
			temp.put("data", "");
			temp.put("message", "系统错误！");
			result.put("status", "storefail");
			return result;
		}

		return result;
	}

	@Override
	public Map<String, Object> queryTinyVillageInfoByVillagecode(String code) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			TinyVillageDao tinyVillageDao = (TinyVillageDao)SpringHelper.getBean(TinyVillageDao.class.getName());
			List<Map<String, Object>> findTinyVillageInfoByCode = tinyVillageDao.findTinyVillageInfoByCode(code);
			result.put("data",findTinyVillageInfoByCode.get(0));
			result.put("code",CodeEnum.success.getValue());
			result.put("message", CodeEnum.success.getDescription());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject temp = new JSONObject();
			result.put("code",CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
	}

}
