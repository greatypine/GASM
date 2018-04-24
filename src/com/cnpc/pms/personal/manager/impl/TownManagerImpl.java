package com.cnpc.pms.personal.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.dao.PersonDutyDao;
import com.cnpc.pms.personal.dao.TownDao;
import com.cnpc.pms.personal.entity.City;
import com.cnpc.pms.personal.entity.County;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.PersonDuty;
import com.cnpc.pms.personal.entity.Province;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.manager.AuditManager;
import com.cnpc.pms.personal.manager.CityManager;
import com.cnpc.pms.personal.manager.CountyManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.PersonDutyManager;
import com.cnpc.pms.personal.manager.ProvinceManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.utils.ValueUtil;

public class TownManagerImpl extends BizBaseCommonManager implements TownManager {

	@Override
	public Map<String, Object> getTownByGb_Code(String gb_code) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<?> list = this.getList(FilterFactory.getSimpleFilter("gb_code", gb_code));
		if (list != null && list.size() > 0) {
			Town town = (Town) list.get(0);
			map.put("id", town.getId());
			map.put("town_name", town.getName());
			return map;
		}
		return null;
	}

	@Override
	public Town getTownById(Long id) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("id", id));
		if (list != null && list.size() > 0) {
			Town town = (Town) list.get(0);
			if (town.getJob() != null && !"".equals(town.getJob())) {
				CustomerManager customerManager = (CustomerManager) SpringHelper.getBean("customerManager");
				String stts = "";
				String[] split = town.getJob().split(",");
				if (split.length > 0) {
					for (int i = 0; i < split.length; i++) {
						String[] strings = split[i].split("-");
						Customer customer = customerManager.findCustomerById(Long.parseLong(strings[0]));
						if ("请选择".equals(strings[1])) {
							stts += "," + customer.getName();
						} else {
							stts += "," + customer.getName() + "(" + strings[1] + ")";
						}

					}
				}
				town.setShowSelectName(stts.substring(1, stts.length()));
			}

			return town;
		}
		return null;
	}

	@Override
	public Map<String, Object> showTownData(QueryConditions conditions) {
		TownDao townDao = (TownDao) SpringHelper.getBean(TownDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();

		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("town_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND town.name like '").append(map_where.get("value")).append("'");
			}
			if ("county_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND county.name like '").append(map_where.get("value")).append("'");
			}

			if ("gb_code".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				String str = (String) map_where.get("value");
				String string = str.substring(1, str.length() - 1);
				sb_where.append(" and town.gb_code ='").append(string.trim()).append("'");
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
		map_result.put("header", "街道信息");
		map_result.put("data", townDao.getTownList(sb_where.toString(), obj_page));
		return map_result;
	}

	@Override
	public Town getTownByTown_name(String town_name) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("name", town_name));
		if (list != null && list.size() > 0) {
			return (Town) list.get(0);
		}
		return null;
	}

	@Override
	public List getTownByTown_name_Info(Town townPro) {
		String strwhere = "";
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		sessionUser = userManager.findUserById(sessionUser.getId());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(sessionUser.getStore_id());
		if (store != null) {
			strwhere = " AND town.id in(" + store.getTown_id() + ") ";
		}
		String cityssql = "";
		if (store == null) {
			List<DistCity> distCityList = userManager.getCurrentUserCity();
			if (distCityList != null && distCityList.size() > 0) {
				if (distCityList.size() < 2) {
					for (DistCity d : distCityList) {
						cityssql = " and city.`name` LIKE '%" + d.getCityname() + "%' ";
					}
				} else {
					for (DistCity d : distCityList) {
						cityssql += "or city.`name` LIKE '%" + d.getCityname() + "%' ";
					}
					cityssql = " and (" + cityssql.substring(2, cityssql.length()) + ")";
				}
				/*
				 * for(DistCity d:distCityList){ cityssql += " "; }
				 * cityssql=cityssql.substring(0, cityssql.length()-1);
				 */
			}
		}
		TownDao townDao = (TownDao) SpringHelper.getBean(TownDao.class.getName());
		List<Town> list = townDao.getTown(townPro.getId(), townPro.getName(), townPro.getIntroduction(),
				strwhere + cityssql);

		return list;
	}

	@Override
	public List<Map<String, Object>> getTownDataByCountyID(Long countyId) {
		List list = new ArrayList();
		Town town = null;
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("county_id", countyId));
		if (lst_vilage_data != null && lst_vilage_data.size() > 0) {
			for (Object object : lst_vilage_data) {
				Map<String, Object> map = new HashMap<String, Object>();
				town = (Town) object;
				map.put("town_name", town.getName());
				map.put("town_id", town.getId());
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public Town saveOrUpdateTown(Town town) {
		/*
		 * AddressManager
		 * addressManager=(AddressManager)SpringHelper.getBean("addressManager")
		 * ; Address address =
		 * addressManager.getAddressBytown_gb_code(town.getGb_code());
		 * if(address==null){ return null; } CountyManager
		 * countyManager=(CountyManager)SpringHelper.getBean("countyManager");
		 * County county =
		 * countyManager.getCountyByGb_code(address.getCounty_gb_code());
		 * if(county==null){ county=new County();
		 * county.setName(address.getCounty_name());
		 * county.setGb_code(address.getCounty_gb_code()); CityManager
		 * cityManager=(CityManager)SpringHelper.getBean("cityManager"); City
		 * city = cityManager.getCityBygb_code(address.getCity_gb_code());
		 * county.setCity_id(city.getId()); preObject(county);
		 * countyManager.saveObject(county); }
		 */
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		sessionUser = userManager.findUserById(sessionUser.getId());
		Town saveTown = null;
		if (town.getId() != null) {
			saveTown = getTownById(town.getId());
			if (town.getGb_code() != null && (!saveTown.getGb_code().equals(town.getGb_code())
					|| !saveTown.getName().equals(town.getName()))) {
				AuditManager auditManager = (AuditManager) SpringHelper.getBean("auditManager");
				auditManager.insertAuditTown(saveTown);
			}
		} else {
			saveTown = new Town();
		}
		saveTown.setName(town.getName());
		saveTown.setGb_code(town.getGb_code());
		saveTown.setCounty_id(town.getCounty_id());
		saveTown.setIntroduction(town.getIntroduction());
		saveTown.setSquare_area(town.getSquare_area());
		saveTown.setHousehold_number(town.getHousehold_number());
		saveTown.setResident_population_number(town.getResident_population_number());
		saveTown.setJob(town.getJob());
		saveTown.setEmployee_no(sessionUser.getEmployeeId());
		preObject(saveTown);
		TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
		townManager.saveObject(saveTown);
		if (town.getId() == null) {
			AuditManager auditManager = (AuditManager) SpringHelper.getBean("auditManager");
			auditManager.insertAuditTown(saveTown);
		}
		updateOrinsertDuty(saveTown.getId(), saveTown.getJob(), 1, saveTown.getEmployee_no());

		/*
		 * if (town.getId() != null) { TownBackManager
		 * townBackManager=(TownBackManager)SpringHelper.getBean(
		 * "townBackManager"); TownBack townBack =
		 * townBackManager.getTownBackInfoBytown_id(saveTown.getId()); NewsDao
		 * newsDao=(NewsDao)SpringHelper.getBean(NewsDao.class.getName());
		 * newsDao.deleteObject(townBack); NewsManager
		 * newsManager=(NewsManager)SpringHelper.getBean("newsManager"); News
		 * news = newsManager.getNewsBy(1, saveTown.getId());
		 * newsDao.deleteObject(news); }
		 */
		return saveTown;
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
	public Map<String, Object> getTownListById(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<?> list = this.getList(FilterFactory.getSimpleFilter("id", id));
		CountyManager countyManager = (CountyManager) SpringHelper.getBean("countyManager");
		if (list != null && list.size() > 0) {
			Town town = (Town) list.get(0);
			if (town.getJob() != null && !"".equals(town.getJob())) {
				CustomerManager customerManager = (CustomerManager) SpringHelper.getBean("customerManager");
				String stts = "";
				String[] split = town.getJob().split(",");
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
				town.setShowSelectName(stts.substring(1, stts.length()));

			}
			if (town.getCounty_id() != null && !"".equals(town.getCounty_id())) {
				County county = countyManager.getCountyById(town.getCounty_id());
				map.put("county_name", county.getName());
				map.put("county_gb_code", county.getGb_code());
			}
			map.put("name", town.getName());
			map.put("employee_no", town.getEmployee_no());
			map.put("gb_code", town.getGb_code());
			map.put("job", town.getJob());
			map.put("county_id", town.getCounty_id());
			map.put("square_area", town.getSquare_area());
			map.put("introduction", town.getIntroduction());
			map.put("household_number", town.getHousehold_number());
			map.put("resident_population_number", town.getResident_population_number());
			map.put("id", town.getId());
			map.put("showSelectName", town.getShowSelectName());
			return map;
		}
		return null;
	}

	@Override
	public Map<String, Object> getTownParentInfoByTown_id(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("id", id));
		if (lst_vilage_data != null && lst_vilage_data.size() > 0) {
			Town town = (Town) lst_vilage_data.get(0);
			map.put("town_id", town.getId());
			map.put("town_name", town.getName());
			map.put("town_gb_code", town.getGb_code());
			CountyManager countyManager = (CountyManager) SpringHelper.getBean("countyManager");
			County county = countyManager.getCountyById(town.getCounty_id());
			map.put("county_id", county.getId());
			map.put("county_name", county.getName());
			map.put("county_gb_code", county.getGb_code());
			CityManager cityManager = (CityManager) SpringHelper.getBean("cityManager");
			City city = cityManager.getCityById(county.getCity_id());
			map.put("city_id", city.getId());
			map.put("city_name", city.getName());
			map.put("city_gb_code", city.getGb_code());
			ProvinceManager provinceManager = (ProvinceManager) SpringHelper.getBean("provinceManager");
			Province province = provinceManager.getProvinceById(city.getProvince_id());
			map.put("province_id", province.getId());
			map.put("province_name", province.getName());
			map.put("province_gb_code", province.getGb_code());
			return map;
		}
		return null;
	}

	@Override
	public Result getAppTownInfo(Town town) {
		TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
		Result result = new Result();
		List<Town> list = new ArrayList<Town>();
		IFilter filter = FilterFactory.getSimpleFilter("1=1");

		if (ValueUtil.checkValue(town.getName())) {
			filter = filter.appendAnd(FilterFactory.getEq("name", town.getName()));
		}
		List<?> lst_result = this.getList(filter);
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
		if (lst_result == null || lst_result.size() == 0) {
			result.setTownList(new ArrayList<Town>());
		} else {
			List<Town> listbus = (List<Town>) lst_result;
			for (Town town1 : listbus) {
				Map<String, Object> map = townManager.getTownParentInfoByTown_id(town1.getId());
				town1.setProvince_name(map.get("province_name").toString());
				town1.setProvince_id(Long.valueOf(String.valueOf(map.get("province_id").toString())).longValue());
				town1.setCity_id(Long.valueOf(String.valueOf(map.get("city_id").toString())).longValue());
				town1.setCity_name(map.get("city_name").toString());
				town1.setCounty_id(Long.valueOf(String.valueOf(map.get("county_id").toString())).longValue());
				town1.setCounty_name(map.get("county_name").toString());
				list.add(town1);
			}

			result.setTownList(list);
		}
		return result;
	}

	@Override
	public Map<String, Object> getTownByStore(Long storeId) {
		TownDao townDao = (TownDao) SpringHelper.getBean(TownDao.class.getName());
		return townDao.getTownInfoByStore(storeId);
	}

	@Override
	public List<Map<String, Object>> selectAllTownByName(String name, Long city_id) {
		TownDao townDao = (TownDao) SpringHelper.getBean(TownDao.class.getName());
		List<Map<String, Object>> list = townDao.selectAllTownByName(city_id, name);

		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	@Override
	public Town verifyUpdate(Town town) {
		Town townByGb_code = getTownByGb_code_vir(town.getGb_code());
		if (townByGb_code != null) {
			if (!townByGb_code.getId().equals(town.getId())) {
				return townByGb_code;
			}
		}
		return null;
	}

	@Override
	public Town getTownByGb_code_vir(String gb_code) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("gb_code", gb_code));
		if (list != null && list.size() > 0) {
			return (Town) list.get(0);
		}
		return null;
	};

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
	public Map<String, Object> getTownListBycityName(QueryConditions conditions) {
		TownDao townDao = (TownDao) SpringHelper.getBean(TownDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();

		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND town.name like '%").append(map_where.get("value")).append("%'");
			}
			if ("cityName".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND city.name like '%").append(map_where.get("value")).append("%'");
			}
			if ("county_ids".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND county.id in (" + map_where.get("value") + ") ");
			}
		}

		System.out.println(sb_where);
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "街道信息");
		map_result.put("data", townDao.getTownListByCityName(sb_where.toString(), obj_page));
		return map_result;
	}

	@Override
	public void updateTownSortById(String idString) {
		TownDao townDao = (TownDao) SpringHelper.getBean(TownDao.class.getName());
		townDao.updateTownSortById(idString);
	}

	@Override
	public List<Town> findlistTown(String Ids) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("id in (" + Ids + ")"));
		if (list != null && list.size() > 0) {
			return (List<Town>) list;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getTownDataByCountyIDAndStoreId(Town town) {
		// 根据门店id获得门店信息
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(town.getStore_id());
		List list = new ArrayList();
		Town towns = null;
		List<?> lst_vilage_data = getList(FilterFactory
				.getSimpleFilter("county_id=" + town.getCounty_id() + " and id in (" + store.getTown_id() + ")"));
		if (lst_vilage_data != null && lst_vilage_data.size() > 0) {
			for (Object object : lst_vilage_data) {
				Map<String, Object> map = new HashMap<String, Object>();
				towns = (Town) object;
				map.put("town_name", towns.getName());
				map.put("town_id", towns.getId());
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public Town findTownById(Long id) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("id", id));
		if (list != null && list.size() > 0) {
			return (Town) list.get(0);
		}
		return null;
	}

	@Override
	public List<Town> findTownListByName(String name) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("name like'" + name + "'"));
		if (list != null && list.size() > 0) {
			return (List<Town>) list;
		}
		return null;
	}
}
