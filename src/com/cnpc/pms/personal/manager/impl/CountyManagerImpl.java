package com.cnpc.pms.personal.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.CountyDao;
import com.cnpc.pms.personal.dao.TownDao;
import com.cnpc.pms.personal.entity.County;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.manager.CountyManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TownManager;

public class CountyManagerImpl extends BizBaseCommonManager implements CountyManager {

	@Override
	public County getCountyById(Long id) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("id", id));
		if (lst_vilage_data != null && lst_vilage_data.size() > 0) {
			return (County) lst_vilage_data.get(0);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getCountyDataByCityID(Long cityId) {
		List list = new ArrayList();
		County county = null;
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("city_id", cityId));
		if (lst_vilage_data != null && lst_vilage_data.size() > 0) {
			for (Object object : lst_vilage_data) {
				Map<String, Object> map = new HashMap<String, Object>();
				county = (County) object;
				map.put("county_name", county.getName());
				map.put("county_id", county.getId());
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public County getCountyByGb_code(String gb_code) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("gb_code", gb_code));
		if (lst_vilage_data != null && lst_vilage_data.size() > 0) {
			return (County) lst_vilage_data.get(0);
		}
		return null;
	}

	@Override
	public List<County> getCountyData(County county) {
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
			strwhere = " AND city.`name` like '%" + store.getCityName() + "%' ";
		}
		TownDao townDao = (TownDao) SpringHelper.getBean(TownDao.class.getName());
		List<County> list = townDao.getCountyData(null, county.getName(), county.getProvince_name(), strwhere);
		return list;
	}

	@Override
	public List<Map<String, Object>> findCountyDataByCityID(County countys) {
		List list = new ArrayList<Map<String, Object>>();
		// 根据门店id获取所有的区id
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(countys.getStore_id());
		// 根据街道id获取所有的区id
		TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
		List<Town> townlist = townManager.findlistTown(store.getTown_id());
		String countids = "";
		if (townlist != null && townlist.size() > 0) {
			for (Town town : townlist) {
				countids += town.getCounty_id() + ",";
			}
			if (countids != null) {
				countids = countids.substring(0, countids.length() - 1);
			}
		}

		List<County> findCountyDataByCityIdandstoreid = findCountyDataByCityIdandstoreid(countys.getCity_id(),
				countids);
		if (findCountyDataByCityIdandstoreid != null && findCountyDataByCityIdandstoreid.size() > 0) {
			for (County county : findCountyDataByCityIdandstoreid) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("county_name", county.getName());
				map.put("county_id", county.getId());
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public List<County> findCountyDataByCityIdandstoreid(Long cityId, String countids) {
		List<?> lst_vilage_data = getList(
				FilterFactory.getSimpleFilter("city_id=" + cityId + " and id in (" + countids + ")"));
		if (lst_vilage_data != null && lst_vilage_data.size() > 0) {
			return (List<County>) lst_vilage_data;
		}
		return null;
	}

	@Override
	public Map<String, Object> getCountyListBycityName(QueryConditions conditions) {
		CountyDao townDao = (CountyDao) SpringHelper.getBean(CountyDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();

		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND county.name like '%").append(map_where.get("value")).append("%'");
			}
			if ("cityName".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND city.name like '%").append(map_where.get("value")).append("%'");
			}
		}

		System.out.println(sb_where);
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "区域信息");
		map_result.put("data", townDao.getCountyListByCityName(sb_where.toString(), obj_page));
		return map_result;
	}

	@Override
	public void updatecountySortById(String ids) {
		CountyDao countyDao = (CountyDao) SpringHelper.getBean(CountyDao.class.getName());
		countyDao.updateCountySortByIds(ids);
	}

}
