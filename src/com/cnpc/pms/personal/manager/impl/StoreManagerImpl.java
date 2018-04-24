package com.cnpc.pms.personal.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.util.FileCopyUtils;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.manager.AttachmentManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.dynamic.dao.DynamicDao;
import com.cnpc.pms.dynamic.manager.DynamicManager;
import com.cnpc.pms.mongodb.common.MongoDbUtil;
import com.cnpc.pms.personal.dao.ProvinceDao;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.dao.StoreDynamicDao;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.City;
import com.cnpc.pms.personal.entity.CityDataStatistics;
import com.cnpc.pms.personal.entity.County;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.DistCityCode;
import com.cnpc.pms.personal.entity.Province;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreDynamic;
import com.cnpc.pms.personal.entity.StoreKeeper;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.manager.ApprovalManager;
import com.cnpc.pms.personal.manager.CityDataStatisticsManager;
import com.cnpc.pms.personal.manager.CityManager;
import com.cnpc.pms.personal.manager.CountyManager;
import com.cnpc.pms.personal.manager.DistCityCodeManager;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
import com.cnpc.pms.personal.manager.ProvinceManager;
import com.cnpc.pms.personal.manager.StoreDynamicManager;
import com.cnpc.pms.personal.manager.StoreKeeperManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.personal.manager.WorkInfoManager;
import com.cnpc.pms.personal.util.DataTransfromUtil;
import com.cnpc.pms.platform.dao.PlatformStoreDao;
import com.cnpc.pms.platform.entity.PlatformStore;
import com.cnpc.pms.platform.entity.SysArea;
import com.cnpc.pms.platform.manager.PlatformStoreManager;
import com.cnpc.pms.platform.manager.SysAreaManager;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * 门店业务实现类 Created by liuxiao on 2016/6/6 0006.
 */
public class StoreManagerImpl extends BaseManagerImpl implements StoreManager {
	PropertiesValueUtil propertiesValueUtil = null;
	/**
	 * 到处户型excel单元格公共样式
	 */
	CellStyle cellStyle_common = null;

	/**
	 * 根据门店名称获取门店对象
	 * 
	 * @param str_store_name
	 *            门店名称
	 * @return 门店对象
	 */
	@Override
	public Store getStoreByName(String str_store_name) {
		List<?> lst_store = getList(FilterFactory.getSimpleFilter("name", str_store_name));
		if (lst_store != null && lst_store.size() > 0) {
			return (Store) lst_store.get(0);
		}
		return null;
	}

	// 查询员工所属门店
	@SuppressWarnings("unchecked")
	@Override
	public Store findStore(Long storeid) {
		Store store = null;
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");

		IFilter filter = FilterFactory.getSimpleFilter("store_id", storeid);

		List<Store> list = (List<Store>) storeManager.getList(filter);
		if (list.size() > 0 && list != null) {
			store = list.get(0);
		}
		return store;
	}

	// 根据门店名称查询门店
	@SuppressWarnings("unchecked")
	@Override
	public Store findStoreByName(String store_name) {
		Store store = null;
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");

		IFilter filter = FilterFactory.getSimpleFilter("name", store_name);

		List<Store> list = (List<Store>) storeManager.getList(filter);
		if (list.size() > 0 && list != null) {
			store = list.get(0);
		}

		return store;
	}

	// 根据门店名称查询门店
	@SuppressWarnings("unchecked")
	@Override
	public List<Store> findStoreListByName(String store_name) {
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");

		IFilter filter = FilterFactory.getSimpleFilter("name like '%" + store_name + "%' and flag=0");

		List<Store> list = (List<Store>) storeManager.getList(filter);
		if (list.size() > 0 && list != null) {
			return list;
		}
		return null;
	}

	@Override
	public List<Store> findStoreAll() {
		List<Store> lst_store = (List<Store>) this.getList(FilterFactory.getSimpleFilter("flag=0"));
		return lst_store;
	}

	@Override
	public List<Store> findStoresByCurrCitys() {
		// 取得当前登录人的城市
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 取得当前登录人 所管理城市
		String cityssql = "";
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		List<DistCity> distCityList = userManager.getCurrentUserCity();
		if (distCityList != null && distCityList.size() > 0) {
			for (DistCity d : distCityList) {
				cityssql += "'" + d.getCityname() + "',";
			}
			cityssql = cityssql.substring(0, cityssql.length() - 1);
		}
		sb_where.append(" 1=1 ");
		if (cityssql != "" && cityssql.length() > 0) {
			sb_where.append(" and city_name in (" + cityssql + ")");
		} else {
			sb_where.append(" and 0=1 ");
		}
		List<Store> lst_store = (List<Store>) this.getList(FilterFactory.getSimpleFilter(sb_where.toString()));
		return lst_store;
	}

	@Override
	public Map<String, Object> getStoreList(QueryConditions conditions) {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 获取当前登录人
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		if ("QYJL".equals(sessionUser.getUsergroup().getCode())) {
			sb_where.append(" and sto.rmid=" + sessionUser.getId() + " ");
		}
		// 取得当前登录人 所管理城市
		String cityssql = "";
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		List<DistCity> distCityList = userManager.getCurrentUserCity();
		if (distCityList != null && distCityList.size() > 0) {
			for (DistCity d : distCityList) {
				cityssql += "'" + d.getCityname() + "',";
			}
			cityssql = cityssql.substring(0, cityssql.length() - 1);
		}

		if (cityssql != "" && cityssql.length() > 0) {
			sb_where.append(" and sto.city_name in (" + cityssql + ")");
		} else {
			sb_where.append(" and 0=1 ");
		}
		TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();
		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("town_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				// sb_where.append(" AND town.name like
				// '").append(map_where.get("value")).append("'");
				List<Town> list = townManager.findTownListByName(map_where.get("value") + "");
				if (list == null) {
					sb_where.append(" and 1=0");
				} else {
					String string = "";
					for (Town town : list) {
						string += " FIND_IN_SET(" + town.getId() + ",sto.town_id) or";
					}
					string = string.substring(0, string.length() - 2);
					sb_where.append(" and (" + string + ") ");
				}
			} else if ("name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND sto.`name` like '").append(map_where.get("value")).append("'");
			} else if ("cityName".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND sto.city_name like '").append(map_where.get("value")).append("'");
			} else if ("storeno".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND sto.storeno like '").append(map_where.get("value")).append("'");
			} else if ("access".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				System.out.println(map_where.get("value"));
				sb_where.append(" AND (sto.open_shop_time is null or sto.open_shop_time='') ");
			} else if ("standUP".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				System.out.println(map_where.get("value"));
				sb_where.append(" AND sto.open_shop_time >=date_format('" + map_where.get("value") + "', '%Y-%m-%d') ");
			} else if ("endUP".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				System.out.println(map_where.get("value"));
				sb_where.append(" AND sto.open_shop_time <=date_format('" + map_where.get("value") + "', '%Y-%m-%d') ");
			}

		}
		System.out.println(sb_where);
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "门店信息");
		map_result.put("data", storeDao.getStoreInfoList(sb_where.toString(), obj_page));
		return map_result;
	}

	@Override
	public Store saveStore(Store store) {
		long save_store_id = 0;
		Store save_store = null;
		if (store.getStore_id() != null) {
			save_store = (Store) this.getObject(store.getStore_id());
		} else {
			save_store = new Store();
		}
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		Store maxStoreData = storeDao.getMaxStoreData();
		Long store_id = maxStoreData.getStore_id();
		if (store_id > 1000) {
			save_store_id = store_id + 1;
		} else {
			save_store_id = 9900000 + store_id;
		}
		save_store.setStore_id(save_store_id);
		save_store.setAddress(store.getAddress());
		save_store.setMobilephone(store.getMobilephone());
		save_store.setName(store.getName());
		save_store.setTown_id(store.getTown_id());
		TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
		Town town = townManager.getTownById(DataTransfromUtil.TownIdTransFrom(store.getTown_id()));
		save_store.setCounty_id(town.getCounty_id());
		CountyManager countyManager = (CountyManager) SpringHelper.getBean("countyManager");
		County county = countyManager.getCountyById(town.getCounty_id());
		save_store.setCity_id(county.getCity_id());
		ProvinceDao provinceDao = (ProvinceDao) SpringHelper.getBean(ProvinceDao.class.getName());
		Province province = provinceDao.getProvinceInfoByTown_id(DataTransfromUtil.TownIdTransFrom(store.getTown_id()));
		save_store.setProvince_id(province.getId());
		save_store.setType(1);
		save_store.setSkid(store.getSkid());
		save_store.setCityName(store.getCityName());
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		save_store.setCreate_user(sessionUser.getName());
		Date date = new Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		save_store.setCreate_time(sdate);
		storeDao.saveStore(save_store);
		return save_store;
	}

	@Override
	public Store updateStore(Store store) throws Exception {
		long millis = System.currentTimeMillis();
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		// TownDao townDao = (TownDao)
		// SpringHelper.getBean(TownDao.class.getName());
		// townDao.deleteTownSortById();
		User sessionUser = null;
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		StoreKeeperManager storeKeeperManager = (StoreKeeperManager) SpringHelper.getBean("storeKeeperManager");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		PlatformStoreManager platformStoreManager = (PlatformStoreManager) SpringHelper.getBean("platformStoreManager");
		PlatformStore platformStore = platformStoreManager.findPlatStoreById(store.getPlatformid());
		DistCityCodeManager distCityCodeManager = (DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");
		DistCityCode distCityCode = distCityCodeManager.queryDistCityCodeByName(store.getCityName());
		WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
		SysAreaManager sysAreaManager = (SysAreaManager) SpringHelper.getBean("sysAreaManager");
		Store update_store = null;
		if (store.getStore_id() == null) {
			if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
			}
			if (platformStore != null) {
				store.setPlatformid(platformStore.getId());
				store.setNumber(platformStore.getNumber());
				store.setId(platformStore.getId());
				store.setWhite(platformStore.getWhite());
				store.setPlatformname(platformStore.getName());
			} else {
				store.setPlatformid(null);
				store.setNumber(null);
				store.setId(null);
				store.setWhite(null);
				store.setPlatformname(null);
			}
			store.setCityNo(distCityCode.getCityno());
			store.setCreate_user(sessionUser.getName());
			store.setCreate_time(new Date());
			TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
			Town town = townManager.getTownById(DataTransfromUtil.TownIdTransFrom(store.getTown_id()));
			if (town != null) {
				store.setCounty_id(town.getCounty_id());
				CountyManager countyManager = (CountyManager) SpringHelper.getBean("countyManager");
				County county = countyManager.getCountyById(town.getCounty_id());
				store.setCity_id(county.getCity_id());
				ProvinceDao provinceDao = (ProvinceDao) SpringHelper.getBean(ProvinceDao.class.getName());
				Province province = provinceDao
						.getProvinceInfoByTown_id(DataTransfromUtil.TownIdTransFrom(store.getTown_id()));
				store.setProvince_id(province.getId());
			}
			// 门店排序
			Store findMaxOrdnumber = storeDao.findMaxOrdnumber(store.getCityName());
			if (findMaxOrdnumber != null && findMaxOrdnumber.getOrdnumber() != null) {
				store.setOrdnumber(findMaxOrdnumber.getOrdnumber() + 1);
			} else {
				store.setOrdnumber(1);
			}
			if (!"V".equals(store.getStoretype())) {
				store.setAuditor_status(1);
			}
			store.setWork_id(millis + "");
			if (store.getGaode_cityCode() != null && !"".equals(store.getGaode_cityCode())) {
				SysArea sysArea = sysAreaManager.findSysAreaByCityCode(store.getGaode_cityCode());
				if (sysArea != null) {
					store.setGaode_provinceCode(sysArea.getParentCode());
				}
			}
			store.setType(1);
			storeDao.insertStore(store);
			storeManager.saveObject(store);
			updateStoreNo(store);
			// 同步门店
			try {
				// 虚拟店不调用审批流程
				if (!"V".equals(store.getStoretype())) {
					Date date = new Date();
					SimpleDateFormat myFmt1 = new SimpleDateFormat("yyyy-MM");
					workInfoManager.StartFlow(store.getStore_id(), "门店选址审核", myFmt1.format(date), store.getWork_id(),
							"save", "");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 同步门店
			try {
				syncStore(store);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return store;
		} else {

			update_store = this.findStore(store.getStore_id());
			Integer auditorbefore = update_store.getAuditor_status();
			// 当门店类型为未知并且门店编号不为空的时候可以修改门店编号
			if (update_store.getStoreno() != null && "W".equals(update_store.getStoretype())
					&& !"W".equals(store.getStoretype()) && "W".equals(update_store.getStoreno().substring(4, 5))) {
				String att1 = update_store.getStoreno().substring(0, 4);
				String att2 = update_store.getStoreno().substring(5, 9);
				String att3 = att1 + store.getStoretype() + att2;
				update_store.setStoreno(att3);
			}
			User user = null;
			boolean isupdate = false;
			if (update_store.getName() != null && !update_store.equals(store.getName())) {
				isupdate = true;
			}
			if (platformStore != null) {
				update_store.setPlatformid(platformStore.getId());
				update_store.setNumber(platformStore.getNumber());
				update_store.setId(platformStore.getId());
				update_store.setWhite(platformStore.getWhite());
				update_store.setPlatformname(platformStore.getName());
			} else {
				update_store.setPlatformid(null);
				update_store.setNumber(null);
				update_store.setId(null);
				update_store.setWhite(null);
				update_store.setPlatformname(null);
			}
			update_store.setSuperMicro(store.getSuperMicro());
			update_store.setEstate(store.getEstate());
			update_store.setStoretype(store.getStoretype());
			update_store.setStoretypename(store.getStoretypename());
			update_store.setOpen_shop_time(store.getOpen_shop_time());
			update_store.setAddress(store.getAddress());
			update_store.setMobilephone(store.getMobilephone());
			update_store.setName(store.getName());
			update_store.setTown_id(store.getTown_id());
			update_store.setTown_name(store.getTown_name());
			update_store.setVillage_id(store.getVillage_id());
			update_store.setTinyvillage_Id(store.getTinyvillage_Id());
			update_store.setType(1);
			update_store.setCounty_ids(store.getCounty_ids());
			update_store.setNature(store.getNature());
			update_store.setTenancyTerm(store.getTenancyTerm());
			update_store.setRental(store.getRental());
			update_store.setPayment_method(store.getPayment_method());
			update_store.setRent_area(store.getRent_area());
			update_store.setUsable_area(store.getUsable_area());
			update_store.setIncrease(store.getIncrease());
			update_store.setRent_free(store.getRent_free());
			update_store.setTaxes(store.getTaxes());
			update_store.setAgency_fee(store.getAgency_fee());
			update_store.setIncrease_fee(store.getIncrease_fee());
			update_store.setStore_position(store.getStore_position());
			update_store.setPlace_town_id(store.getPlace_town_id());
			update_store.setGaode_adCode(store.getGaode_adCode());
			update_store.setGaode_address(store.getGaode_address());
			update_store.setGaode_cityCode(store.getGaode_cityCode());
			if (store.getGaode_cityCode() != null && !"".equals(store.getGaode_cityCode())) {
				SysArea sysArea = sysAreaManager.findSysAreaByCityCode(store.getGaode_cityCode());
				if (sysArea != null) {
					update_store.setGaode_provinceCode(sysArea.getParentCode());
				}
			}
			if (!"V".equals(store.getStoretype())) {
				update_store.setAuditor_status(1);
			}
			if (store.getRemark() != null && !"".equals(store.getRemark())) {
				update_store.setRemark(store.getRemark());
			}
			update_store.setSkid(store.getSkid());
			TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
			Town town = townManager.getTownById(DataTransfromUtil.TownIdTransFrom(store.getTown_id()));
			if (town != null) {
				update_store.setCounty_id(town.getCounty_id());
				CountyManager countyManager = (CountyManager) SpringHelper.getBean("countyManager");
				County county = countyManager.getCountyById(town.getCounty_id());
				update_store.setCity_id(county.getCity_id());
				ProvinceDao provinceDao = (ProvinceDao) SpringHelper.getBean(ProvinceDao.class.getName());
				Province province = provinceDao
						.getProvinceInfoByTown_id(DataTransfromUtil.TownIdTransFrom(store.getTown_id()));
				update_store.setProvince_id(province.getId());
			}
			update_store.setCityName(store.getCityName());
			if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
			}
			update_store.setUpdate_user(sessionUser.getName());
			update_store.setUpdate_time(new Date());
			update_store.setCityNo(distCityCode.getCityno());
			if (!"V".equals(update_store.getStoretype())) {
				update_store.setAuditor_status(1);
			}
			if (auditorbefore != null && auditorbefore == 3) {
				update_store.setWork_id(millis + "");
			}
			storeManager.saveObject(update_store);
			this.updateUserStoreId(update_store.getStore_id());

			if (isupdate) {
				HumanresourcesManager hManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
				hManager.updateStoreNameById(update_store.getStore_id(), update_store.getName());
				ApprovalManager approvalManager = (ApprovalManager) SpringHelper.getBean("approvalManager");
				approvalManager.updateStoreNameById(update_store.getStore_id(), update_store.getName());
			}

			// 同步门店
			try {
				// 虚拟店不调用审批流程
				if (!"V".equals(update_store.getStoretype())) {
					Date date = new Date();
					SimpleDateFormat myFmt1 = new SimpleDateFormat("yyyy-MM");
					if (auditorbefore == 2) {
						workInfoManager.StartFlow(update_store.getStore_id(), "门店选址审核", myFmt1.format(date),
								update_store.getWork_id(), "update", "");
					} else if (auditorbefore == 3) {
						workInfoManager.StartFlow(update_store.getStore_id(), "门店选址审核", myFmt1.format(date),
								update_store.getWork_id() + "", "save", "");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 同步门店
		try {
			syncStore(update_store);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return update_store;
	}

	public User getUserBySkid(Long store_ids, Long skid) {
		StoreKeeperManager storeKeeperManager = (StoreKeeperManager) SpringHelper.getBean("storeKeeperManager");
		StoreKeeper keeperById = storeKeeperManager.queryStoreKeeperById(skid);
		if (keeperById != null) {
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			User user = userManager.findEmployeeByEmployeeNo(keeperById.getEmployee_no());
			if (user == null) {
				if (keeperById.getHumanstatus() == 1) {
					keeperById.setStore_ids("," + store_ids + ",");
					user = storeKeeperManager.addBizbaseUser(keeperById);
				}
			}
			return user;
		}
		return null;

	}

	public void updateUserStoreId(Long store_id) {
		// 根据门店的id查找门店
		Store store = this.findStore(store_id);// 这是修改过的门店数据
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		User user = userManager.findUserByStore_id(store_id);// 根据门店的id查询用户表查询修改前的用户
		if (user != null) {
			List<Store> list = this.findStoreByskid(user.getId());
			user.setStore_id(list != null && list.size() > 0 ? list.get(0).getStore_id() : null);
			userManager.saveObject(user);
		}
		// 修改当前门店店长的门店id
		if (store.getSkid() != null && !"".equals(store.getSkid())) {
			User findUserById = userManager.findUserById(store.getSkid());// 根据用户id获取当前用户(查找当前门店的店长)
			if (findUserById != null) {
				findUserById.setStore_id(store_id);
				userManager.saveObject(findUserById);
			}
		}

	}

	@Override
	public Map<String, Object> getStoreById(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Store store = null;
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
		CountyManager countyManager = (CountyManager) SpringHelper.getBean("countyManager");
		AttachmentManager attachmentManager = (AttachmentManager) SpringHelper.getBean("attachmentManager");
		IFilter filter = FilterFactory.getSimpleFilter("store_id", id);
		List<Store> list = (List<Store>) storeManager.getList(filter);
		if (list.size() > 0 && list != null) {
			store = list.get(0);
			map.put("platformid", store.getPlatformid());
			map.put("platformname", store.getPlatformname());
			map.put("name", store.getName());
			map.put("mobilephone", store.getMobilephone());
			map.put("address", store.getAddress());
			map.put("open_shop_time", transfromTime(store.getOpen_shop_time()));
			map.put("detail_address", store.getDetail_address());
			map.put("cityName", store.getCityName());
			map.put("skid", store.getSkid());
			if (store.getSkid() != null && !"".equals(store.getSkid())) {
				UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
				User sessionUser = userManager.findUserById(store.getSkid());
				if (sessionUser != null) {
					map.put("storemanager", sessionUser.getName());
				}
			}
			if (store.getTown_id() != null && !"".equals(store.getTown_id())) {
				String[] split = store.getTown_id().split(",");
				String stt = "";
				for (String string : split) {
					Town town = townManager.findTownById(Long.parseLong(string));
					stt += town.getName() + ",";
				}
				map.put("town_name", stt.substring(0, stt.length() - 1));
			} else {
				map.put("town_name", "");
			}
			if (store.getPlace_town_id() != null && !"".equals(store.getPlace_town_id())) {
				Town town = townManager.findTownById(store.getPlace_town_id());
				map.put("place_town_name", town.getName());
			} else {
				map.put("place_town_name", "");
			}
			map.put("place_town_id", store.getPlace_town_id());
			map.put("town_id", store.getTown_id());
			map.put("remark", store.getRemark());
			map.put("storetype", store.getStoretype());
			map.put("storeno", store.getStoreno());
			map.put("superMicro", store.getSuperMicro());
			map.put("estate", store.getEstate());
			map.put("county_ids", store.getCounty_ids());
			if (store.getCounty_ids() != null && !"".equals(store.getCounty_ids())) {
				String countyName = "";
				String[] split = store.getCounty_ids().split(",");
				for (String string : split) {
					County county = countyManager.getCountyById(Long.parseLong(string));
					countyName += county.getName() + ",";
				}
				map.put("county_name", countyName.substring(0, countyName.length() - 1));
			}
			map.put("nature", store.getNature());
			map.put("tenancyTerm", store.getTenancyTerm());
			map.put("rental", store.getRental());
			map.put("payment_method", store.getPayment_method());
			map.put("rent_area", store.getRent_area());
			map.put("usable_area", store.getUsable_area());
			map.put("increase", store.getIncrease());
			map.put("rent_free", store.getRent_free());
			map.put("taxes", store.getTaxes());
			map.put("agency_fee", store.getAgency_fee());
			map.put("increase_fee", store.getIncrease_fee());
			map.put("auditor_status", store.getAuditor_status());
			map.put("store_position", store.getStore_position());
			map.put("gaode_citycode", store.getGaode_cityCode());
			map.put("gaode_address", store.getGaode_address());
			map.put("gaode_adcode", store.getGaode_adCode());
			Attachment attachment = attachmentManager.findAttachmentByStoreIdType(store.getStore_id(), 3);
			if (attachment != null) {
				map.put("contract", attachment.getFile_name());
				map.put("url", attachment.getFile_path().split("contract")[1]);
			} else {
				map.put("contract", "");
			}

		}
		return map;
	}

	/**
	 * 查询某店长 管理的门店
	 */
	@Override
	public Map<String, Object> getStoreListBySid(QueryConditions conditions) {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();
		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("type".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				if (map_where.get("value").equals("SK")) {
					sb_where.append(" AND skid=" + map_where.get("value"));
				} else {
					sb_where.append(" AND rmid=" + map_where.get("value"));
				}
			}
		}
		sb_where.append(" order by update_time asc ");
		IFilter iFilter = FilterFactory.getSimpleFilter(" 1=0 " + sb_where.toString());
		/*
		 * fsp.setPage(pageInfo); fsp.setUserFilter(iFilter); lst_data =
		 * this.getList(fsp);
		 */
		System.out.println(sb_where);
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "门店信息");
		// map_result.put("data", storeDao.getStoreInfoList(sb_where.toString(),
		// obj_page));
		map_result.put("data", this.getList(iFilter));
		return map_result;
	}

	// 根据store_id回写 店长的skid
	@Override
	public int updateStoreskid(String store_ids, Long userid) {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		int ret = storeDao.updateStoreskid(store_ids, userid);
		return ret;
	}

	// 根据store_id回写 运营经理的rmid
	@Override
	public int updateStorermid(String store_ids, Long userid) {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		int ret = storeDao.updateStorermid(store_ids, userid);
		return ret;
	}

	// 根据店长id skid查询所管理的门店
	@Override
	public List<Store> findStoreByskid(Long skid) {
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		IFilter filter = FilterFactory.getSimpleFilter("skid", skid);
		List<Store> list = (List<Store>) storeManager.getList(filter);
		return list;
	}

	// 根据区域管理id rmid查询所管理的门店
	@Override
	public List<Store> findStoreByrmid(Long rmid) {
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		IFilter filter = FilterFactory.getSimpleFilter("rmid", rmid);
		List<Store> list = (List<Store>) storeManager.getList(filter);
		return list;
	}

	@Override
	public Map<String, Object> getStoreInfoById(Store store) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (store.getTown_id() != null) {
			TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
			Town town = townManager.getTownById(DataTransfromUtil.TownIdTransFrom(store.getTown_id()));
			map.put("town_name", town.getName());
			map.put("town_id", town.getId());
			CountyManager countyManager = (CountyManager) SpringHelper.getBean("countyManager");
			County county = countyManager.getCountyById(town.getCounty_id());
			map.put("cou_name", county.getName());
			map.put("cou_id", county.getId());
			CityManager cityManager = (CityManager) SpringHelper.getBean("cityManager");
			City city = cityManager.getCityById(county.getCity_id());
			map.put("city_name", city.getName());
			map.put("city_id", city.getId());
			ProvinceManager provinceManager = (ProvinceManager) SpringHelper.getBean("provinceManager");
			Province province = provinceManager.getProvinceById(city.getProvince_id());
			map.put("pro_name", province.getName());
			map.put("pro_id", province.getId());
			return map;
		}
		return null;
	}

	public long getSkidByUserId(Long userId) {
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		User user = userManager.findUserById(userId);
		if (user != null && user.getEmployeeId() != null) {
			StoreKeeperManager storeKeeperManager = (StoreKeeperManager) SpringHelper.getBean("storeKeeperManager");
			StoreKeeper storeKeeper = storeKeeperManager.findStoreKeeperByEmployeeId(user.getEmployeeId());
			if (storeKeeper != null) {
				return storeKeeper.getId();
			}
		}
		return 0;
	}

	@Override
	public Store getStoreByCityAndName(Store store) {
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		IFilter filter = FilterFactory.getSimpleFilter(
				"name like '%" + store.getName() + "%' and city_name like '%" + store.getCityName() + "%'");
		List<Store> list = (List<Store>) storeManager.getList(filter);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	// 同步平台3.0的t_store数据
	public void updateSyncPlatStore() {
		List<Store> platIdisnullList = findStoreListByPlatIdisnull();
		PlatformStoreManager platformStoreManager = (PlatformStoreManager) SpringHelper.getBean("platformStoreManager");
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		if (platIdisnullList != null) {
			for (Store store : platIdisnullList) {
				PlatformStore platformStore = platformStoreManager.findPlatStoreByCode(store.getStoreno());
				if (platformStore != null) {
					store.setPlatformid(platformStore.getId());
					store.setNumber(platformStore.getNumber());
					store.setId(platformStore.getId());
					store.setWhite(platformStore.getWhite());
					// store.setAddress(platformStore.getAddress());
					store.setPlatformname(platformStore.getName());
					storeManager.saveObject(store);
				} else {
					store.setPlatformid(null);
					store.setNumber(null);
					store.setId(null);
					store.setWhite(null);
					// store.setAddress(platformStore.getAddress());
					store.setPlatformname(null);
					storeManager.saveObject(store);
				}
			}

		}
	}

	@Override
	public List<Store> findStoreListByPlatIdisnull() {
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		IFilter filter = FilterFactory.getSimpleFilter("1=1");
		List<Store> list = (List<Store>) storeManager.getList(filter);
		if (list.size() > 0 && list != null) {
			return list;
		}
		return null;
	}

	// 将timestamp转换成string
	public String transfromTime(Date time) {
		if (time == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String string = format.format(time);
		return string;
	}

	@Override
	public Map<String, Object> getStoreProvinceInfoById() {
		Map<String, Object> map = new HashMap<String, Object>();
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		sessionUser = userManager.findUserById(sessionUser.getId());
		Store store = findStore(sessionUser.getStore_id());
		if (store != null) {
			if (store.getTown_id() != null) {
				TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
				Town town = townManager.getTownById(DataTransfromUtil.TownIdTransFrom(store.getTown_id()));
				CountyManager countyManager = (CountyManager) SpringHelper.getBean("countyManager");
				County county = countyManager.getCountyById(town.getCounty_id());
				CityManager cityManager = (CityManager) SpringHelper.getBean("cityManager");
				City city = cityManager.getCityById(county.getCity_id());
				ProvinceManager provinceManager = (ProvinceManager) SpringHelper.getBean("provinceManager");
				Province province = provinceManager.getProvinceById(city.getProvince_id());
				map.put("pro_id", province.getId());
			}
		} else {
			map.put("pro_id", 0);
		}
		return map;
	}

	@Override
	public Store getStoreByCityName(String cityName) {
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		IFilter filter = FilterFactory
				.getSimpleFilter("city_name =" + cityName + " and town_id is not null and town_id !=''");
		List<Store> list = (List<Store>) storeManager.getList(filter);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> getStoreCityInfoById(Store store) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (store.getTown_id() != null) {
			TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
			Town town = townManager.getTownById(DataTransfromUtil.TownIdTransFrom(store.getTown_id()));
			List<Town> findlistTown = townManager.findlistTown(store.getTown_id());
			map.put("townData", findlistTown);
			CountyManager countyManager = (CountyManager) SpringHelper.getBean("countyManager");
			County county = countyManager.getCountyById(town.getCounty_id());
			map.put("cou_name", county.getName());
			map.put("cou_id", county.getId());
			CityManager cityManager = (CityManager) SpringHelper.getBean("cityManager");
			City city = cityManager.getCityById(county.getCity_id());
			map.put("city_name", city.getName());
			map.put("city_id", city.getId());
			ProvinceManager provinceManager = (ProvinceManager) SpringHelper.getBean("provinceManager");
			Province province = provinceManager.getProvinceById(city.getProvince_id());
			map.put("pro_name", province.getName());
			map.put("pro_id", province.getId());
			return map;
		}
		return null;
	}

	@Override
	public Map<String, Object> getPlatformStore(QueryConditions conditions) {
		System.out.println("调用getPlatformStore方法");
		PlatformStoreDao platformStoreDao = (PlatformStoreDao) SpringHelper.getBean(PlatformStoreDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();

		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND name like '").append(map_where.get("value")).append("'");
			}
			if ("code".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND code like '").append(map_where.get("value")).append("'");
			}
		}
		List<Map<String, Object>> rList = new ArrayList<Map<String, Object>>();
		List<PlatformStore> storeList = platformStoreDao.getPlatformStoreList(sb_where.toString(), obj_page);
		Map<String, Object> map = null;
		for (int i = 0; i < storeList.size(); i++) {
			PlatformStore platformStore = storeList.get(i);
			map = new HashMap<String, Object>();
			map.put("name", platformStore.getName());
			map.put("platformid", platformStore.getId());
			map.put("code", platformStore.getCode());
			/*
			 * store.setPlatformid(platformStore.getId());
			 * store.setName(platformStore.getName());
			 * store.setStore_id(Long.parseLong(i+"")); rList.add(store);
			 */
			rList.add(map);
		}
		obj_page.setTotalRecords(platformStoreDao.getPlatformStoreCount(sb_where.toString()));
		System.out.println(sb_where);
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "平台门店列表");
		map_result.put("data", rList);
		return map_result;
	}

	@Override
	public void updateStoreSortById(String ids) {
		System.out.println(ids);
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		storeDao.updateStoreSortById(ids);
	}

	@Override
	public Map<String, Object> getStoreListData(QueryConditions conditions) {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 取得当前登录人 所管理城市
		String cityssql = "";
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		List<DistCity> distCityList = userManager.getCurrentUserCity();
		if (distCityList != null && distCityList.size() > 0) {
			for (DistCity d : distCityList) {
				cityssql += "'" + d.getCityname() + "',";
			}
			cityssql = cityssql.substring(0, cityssql.length() - 1);
		}

		if (cityssql != "" && cityssql.length() > 0) {
			sb_where.append(" and sto.city_name in (" + cityssql + ")");
		} else {
			sb_where.append(" and 0=1 ");
		}
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();
		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("town_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND sto.town_name like '").append(map_where.get("value")).append("'");
			} else if ("name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND sto.`name` like '").append(map_where.get("value")).append("'");
			} else if ("cityName".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND sto.city_name like '").append(map_where.get("value")).append("'");
			}

		}
		sb_where.append(" AND sto.platformname is null ");
		obj_page.setTotalRecords(storeDao.getCountStore(sb_where.toString()));
		System.out.println(sb_where);
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "门店信息");
		map_result.put("data", storeDao.getStoreInfoListData(sb_where.toString(), obj_page));
		return map_result;
	}

	@Override
	public Map<String, Object> getNotFindPlatformStore(QueryConditions conditions) {
		System.out.println("调用getPlatformStore方法");
		PlatformStoreDao platformStoreDao = (PlatformStoreDao) SpringHelper.getBean(PlatformStoreDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();

		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND name like '").append(map_where.get("value")).append("'");
			}
		}
		String string = getnotplatformId();
		if (string != null) {
			sb_where.append(" and id not in (" + string + ")");
		} else {
			sb_where.append(" and 1=0");
		}
		List<Map<String, Object>> rList = new ArrayList<Map<String, Object>>();
		List<PlatformStore> storeList = platformStoreDao.getPlatformStoreList(sb_where.toString(), obj_page);
		Map<String, Object> map = null;
		for (int i = 0; i < storeList.size(); i++) {
			PlatformStore platformStore = storeList.get(i);
			map = new HashMap<String, Object>();
			map.put("name", platformStore.getName());
			map.put("platformid", platformStore.getId());
			/*
			 * store.setPlatformid(platformStore.getId());
			 * store.setName(platformStore.getName());
			 * store.setStore_id(Long.parseLong(i+"")); rList.add(store);
			 */
			rList.add(map);
		}
		obj_page.setTotalRecords(platformStoreDao.getPlatformStoreCount(sb_where.toString()));
		System.out.println(sb_where);
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "平台门店列表");
		map_result.put("data", rList);
		return map_result;
	}

	@Override
	public String getnotplatformId() {
		String string = "";
		List<Store> lst_store = (List<Store>) getList(FilterFactory.getSimpleFilter("platformname is NOT NULL"));
		if (lst_store != null && lst_store.size() > 0) {
			for (Store object : lst_store) {
				string += ",'" + object.getPlatformid() + "'";
			}
			return string.substring(1, string.length());
		}
		return null;
	}

	@Override
	public void updateStoreKeepStoreIds(Long skid) {// skid是店长表中的id
		String storeIds = "";
		String storeNames = "";
		StoreKeeperManager storeKeeperManager = (StoreKeeperManager) SpringHelper.getBean("storeKeeperManager");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		StoreKeeper employeeId = storeKeeperManager.queryStoreKeeperById(skid);
		User user = userManager.findEmployeeByEmployeeNo(employeeId.getEmployee_no());
		List<Store> list = findStoreByskid(user.getId());
		if (list != null && list.size() > 0) {
			for (Store store : list) {
				storeIds += "," + store.getStore_id();
				storeNames += "," + store.getName();
			}
		}
		if (employeeId != null) {
			if (storeIds.length() > 0) {
				employeeId.setStore_ids(storeIds + ",");
				employeeId.setStorenames(storeNames + ",");
				storeKeeperManager.saveObject(employeeId);
			} else {
				employeeId.setStore_ids(storeIds);
				employeeId.setStorenames(storeNames);
				storeKeeperManager.saveObject(employeeId);
			}

		}
	}

	@Override
	public void updateStoreKeepStoreIdsFormUser(Long userID) {
		String storeIds = "";
		String storeNames = "";
		StoreKeeperManager storeKeeperManager = (StoreKeeperManager) SpringHelper.getBean("storeKeeperManager");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		User user = userManager.findUserById(userID);
		StoreKeeper employeeId = storeKeeperManager.findStoreKeeperByEmployeeId(user.getEmployeeId());
		List<Store> list = findStoreByskid(userID);
		if (list != null && list.size() > 0) {
			for (Store store : list) {
				storeIds += "," + store.getStore_id();
				storeNames += "," + store.getName();
			}
		}
		if (employeeId != null) {
			if (storeIds.length() > 0) {
				employeeId.setStore_ids(storeIds + ",");
				employeeId.setStorenames(storeNames + ",");
				storeKeeperManager.saveObject(employeeId);
			} else {
				employeeId.setStore_ids(storeIds);
				employeeId.setStorenames(storeNames);
				storeKeeperManager.saveObject(employeeId);
			}
		}

	}

	/**
	 * @param store_ids
	 *            所选择的门店
	 * @param employee_no
	 *            员工号
	 * @param type
	 *            类型 SK 、 RM 验证 区域经理 与 店长 保存时的方法 验证选择的门店 是否被分配过
	 */
	@Override
	public Store findStoreByStoreIds(String store_ids, String employee_no, String type) {
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		String store_id_condition = "";
		if (store_ids != null && store_ids.endsWith(",") && store_ids.startsWith(",")) {
			store_id_condition = store_ids.substring(1, store_ids.length() - 1);
			IFilter filter = FilterFactory.getSimpleFilter("store_id in (" + store_id_condition + ")");
			List<Store> list = (List<Store>) storeManager.getList(filter);
			if (list != null && list.size() > 0) {
				Set<Long> rmids = new HashSet<Long>();
				Set<Long> skids = new HashSet<Long>();
				for (Store s : list) {
					if (s.getRmid() != null && type.equals("RM")) {
						rmids.add(s.getRmid());
					}
					if (s.getSkid() != null && type.equals("SK")) {
						skids.add(s.getSkid());
					}
				}
				// 根据rmid查询员工 ，如果与参数不一至 则为其它人。
				UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
				Store retStore = null;
				if (rmids != null && rmids.size() > 0) {
					String storenames = "";
					for (Long rmid : rmids) {
						User user = userManager.findUserById(rmid);
						if (employee_no == null) {
							// 此外为新增时的判断
							for (Store s : list) {
								if (rmid.equals(s.getRmid())) {
									storenames += s.getName() + ",";
								}
							}
						}

						if (employee_no != null && !employee_no.equals(user.getEmployeeId())) {
							List<Store> ss = this.findStoreByrmid(user.getId());
							for (Store s : ss) {
								storenames += s.getName() + ",";
							}
						}
					}
					if (storenames != "") {
						retStore = new Store();
						retStore.setName(storenames);
						return retStore;
					}
				}
				if (skids != null && skids.size() > 0) {
					String storenames = "";
					for (Long skid : skids) {
						User user = userManager.findUserById(skid);

						if (employee_no == null) {
							// 此外为新增时的判断
							for (Store s : list) {
								if (skid.equals(s.getSkid())) {
									storenames += s.getName() + ",";
								}
							}
						}

						if (employee_no != null && !employee_no.equals(user.getEmployeeId())) {
							List<Store> ss = this.findStoreByskid(user.getId());
							for (Store s : ss) {
								storenames += s.getName() + ",";
							}
						}
					}
					if (storenames != "") {
						retStore = new Store();
						retStore.setName(storenames);
						return retStore;
					}
				}

			}
		}
		return null;
	}

	@Override
	public Map<String, Object> getAllTotalOfArea(Long id, Long employee_no) {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		UserDAO userDAO = (UserDAO) SpringHelper.getBean(UserDAO.class.getName());
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list1 = storeDao.getStoreById(id);// 门店数
		List<Map<String, Object>> list2 = storeDao.getStoreKeeper(id);// 店长数
		List<Map<String, Object>> gaxlist = userDAO.getEmployeeOfStore(null, employee_no, "QYJL");
		result.put("store", list1);
		result.put("storeKeeper", list2);
		result.put("storeTotal", list1 == null ? 0 : list1.size());
		result.put("storeKeeperTotal", list2 == null ? 0 : list2.size());
		result.put("gax", gaxlist);
		result.put("gaxTotal", gaxlist == null ? 0 : gaxlist.size());
		return result;
	}

	// 修改门店编号
	public void updateStoreNo(Store store) throws Exception {
		String storetype = "";
		// 如果门店为虚拟门店的时候根据虚拟门店判断门店生成独立的编码
		if (store.getStoreno() == null || "".equals(store.getStoreno())) {
			StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
			DistCityCodeManager distCityCodeManager = (DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");
			DistCityCode distCityCode = distCityCodeManager.queryDistCityCodeByName(store.getCityName());
			if (distCityCode == null) {
				throw new Exception("请联系管理员,维护城市编码");
			}
			String string = storeDao.getMaxStoreNo(store);
			if (string != null) {
				int number = Integer.parseInt(string);
				number = number + 1;
				string = number + "";
				switch (string.length()) {
				case 1:
					string = "000" + string;
					break;
				case 2:
					string = "00" + string;
					break;
				case 3:
					string = "0" + string;
					break;
				default:
					string = string;
					break;
				}
				storetype = distCityCode.getCityno() + store.getStoretype() + string;
			} else {
				storetype = distCityCode.getCityno() + store.getStoretype() + "0001";
			}
			store.setStoreno(storetype);
			StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
			storeManager.saveObject(store);
		}
	}

	public Map<String, Object> getCityNameOfCSZJ(Long employeeId, Long city_id) {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> allCity = storeDao.getCityNameOfCSZJ(employeeId, null);
		List<Map<String, Object>> initCity = storeDao.getCityNameOfCSZJ(employeeId, city_id);
		if (allCity != null && allCity.size() > 0) {
			result.put("citylist", allCity);
		} else {
			result.put("citylist", null);
		}

		if (initCity != null && initCity.size() > 0) {
			result.put("city", initCity.get(0));
		} else {
			result.put("city", null);
		}
		return result;
	}

	@Override
	public Map<String, Object> getAllStoreOfCSZJ(Long employeeId, Long cityId) {
		CityDataStatisticsManager cityDataStatisticsManager = (CityDataStatisticsManager) SpringHelper
				.getBean("cityDataStatisticsManager");
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		UserDAO userDAO = (UserDAO) SpringHelper.getBean(UserDAO.class.getName());
		DynamicDao dynamicDao = (DynamicDao) SpringHelper.getBean(DynamicDao.class.getName());
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list1 = storeDao.getAllStoreOfCSZJ(employeeId, cityId);
		List<Map<String, Object>> list2 = storeDao.getAllStoreKeeperOfCSZJ(employeeId, cityId);
		List<Map<String, Object>> gaxlist = userDAO.getEmployeeOfStore(cityId, employeeId, "CSZJ");
		// Integer houseTotal_P = dynamicDao.getAllHouseAmount(cityId,
		// employeeId, 0);//平房总数
		// Integer houseTotal_L = dynamicDao.getAllHouseAmount(cityId,
		// employeeId, 1);//楼房总数
		/*
		 * Integer tinyVillageTotal = dynamicDao.getAllTinyVillageAmount(cityId,
		 * employeeId); Integer villageTotal =
		 * dynamicDao.getAllVillageAmount(cityId, employeeId); Integer townTotal
		 * = dynamicDao.getAllTownAmount(cityId, employeeId);
		 */
		CityDataStatistics cityDataStatistics = cityDataStatisticsManager.findCityDataStatisticsByCityId(cityId);
		result.put("storeTotal", list1 == null ? 0 : list1.size());
		result.put("store", list1);
		result.put("storeKeeperTotal", list2 == null ? 0 : list2.size());
		result.put("storeKeeper", list2);
		result.put("gaxTotal", gaxlist == null ? 0 : gaxlist.size());
		result.put("gax", gaxlist);
		// result.put("house_p",houseTotal_P);
		// result.put("house_l",houseTotal_L);
		result.put("tinyVillage", cityDataStatistics == null ? 0 : cityDataStatistics.getCityTinyvillagecount());
		result.put("village", cityDataStatistics == null ? 0 : cityDataStatistics.getCityVillagecount());
		result.put("town", cityDataStatistics == null ? 0 : cityDataStatistics.getCityTowncount());
		return result;
	}

	@Override
	public File exportExcelStore(Map<String, Object> param) throws Exception {
		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_newfilepath = str_file_dir_path + "store_list.xls";
		File file_new = new File(str_newfilepath);
		if (file_new.exists()) {
			file_new.delete();
		}
		FileOutputStream fis_out_excel = null;
		// 表头数据（确保name 和 key 一一对应）
		String[] tHeadName = { "门店编码", "门店名称", "门店类型", "城市", "平台门店id(参考)", "平台门店名称(参考)" };
		String[] tHeadKey = { "storeno", "name", "storetypename", "city_name", "platformid", "platformname" };

		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		for (String key : param.keySet()) {
			if ("cityName".equals(key)) {
				sb_where.append(" AND sto.city_name like '%").append(param.get(key)).append("%'");
			}
			if ("town_name".equals(key)) {
				sb_where.append(" AND town.name like '%").append(param.get(key)).append("%'");
			}
			if ("name".equals(key)) {
				sb_where.append(" AND sto.name like '%").append(param.get(key)).append("%'");
			}
			if ("access".equals(key)) {
				System.out.println(param.get(key));
				if (param.get(key) != null && "1".equals(param.get(key))) {
					sb_where.append(" AND (sto.open_shop_time is null or sto.open_shop_time='') ");
				}
			}
			if ("standUP".equals(key)) {
				sb_where.append(" AND sto.open_shop_time >=date_format('" + param.get(key) + "', '%Y-%m-%d') ");
			}
			if ("endUP".equals(key)) {
				sb_where.append(" AND sto.open_shop_time <=date_format('" + param.get(key) + "', '%Y-%m-%d') ");
			}
			if ("storeno".equals(key)) {
				sb_where.append(" AND sto.storeno like '").append(param.get(key)).append("'");
			}
		}
		// 取得当前登录人 所管理城市
		String cityssql = "";
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		List<DistCity> distCityList = userManager.getCurrentUserCity();
		if (distCityList != null && distCityList.size() > 0) {
			for (DistCity d : distCityList) {
				cityssql += "'" + d.getCityname() + "',";
			}
			cityssql = cityssql.substring(0, cityssql.length() - 1);
		}

		if (cityssql != "" && cityssql.length() > 0) {
			sb_where.append(" and sto.city_name in (" + cityssql + ")");
		} else {
			sb_where.append(" and 0=1 ");
		}
		System.out.println(sb_where);
		List<Map<String, Object>> bussinessInfo = storeDao.getStoreDate(sb_where.toString());
		try {
			Workbook officeWorkBook = this.resultSetToExcel(bussinessInfo, "门店信息", tHeadName, tHeadKey);
			fis_out_excel = new FileOutputStream(file_new);
			officeWorkBook.write(fis_out_excel);
		} catch (Exception e) {
			throw e;
		} finally {
			if (fis_out_excel != null) {
				fis_out_excel.close();
			}
		}
		return file_new;
	}

	/**
	 * 生成我rkbook
	 * 
	 * @param resultList
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public Workbook resultSetToExcel(List<Map<String, Object>> resultList, String sheetName, String[] tHeadName,
			String[] tHeadKey) throws Exception {
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet(sheetName);
		sheet.setDefaultColumnWidth(25);
		sheet.setAutobreaks(true);
		Row row = null;
		int rowIndex = 0;
		/**
		 * 设置表头
		 */
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		CellStyle style = null;
		Font font = null;
		// 头样式
		style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
		style.setWrapText(true);

		font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);// HSSFColor.VIOLET.index //字体颜色
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
		style.setFont(font);

		for (int i = 0; i < tHeadName.length; i++) {
			setCellValue(row, i, tHeadName[i], style);
		}
		rowIndex++;
		/**
		 * 设置数据
		 */
		// 数据样式
		style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中

		style.setWrapText(true);
		for (Map<String, Object> map : resultList) {
			row = sheet.createRow(rowIndex);
			for (int i = 0; i < tHeadKey.length; i++) {
				setCellValue(row, i, map.get(tHeadKey[i]), style);
			}
			rowIndex++;
		}
		return workbook;
	}

	public void setCellValue(Row row, int nCellIndex, Object value, CellStyle style) {
		Cell cell = row.createCell(nCellIndex);
		cell.setCellStyle(style);
		cell.setCellValue(new HSSFRichTextString(value == null ? null : value.toString()));
	}

	/**
	 * 添加城市储备店
	 * 
	 * @param store
	 * @return
	 */
	@Override
	public Store saveCityStore(Store store) {
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		store.setCreate_user(sessionUser.getName());
		Date date = new Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		store.setCreate_time(sdate);
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		storeManager.saveObject(store);
		return store;
	}

	@Override
	public List<Store> findStoreByCityData(String cityName) {
		List<?> lst_store = getList(FilterFactory.getSimpleFilter("cityName in (" + cityName + ")"));
		if (lst_store != null && lst_store.size() > 0) {
			return (List<Store>) lst_store;
		}
		return null;
	}

	@Override
	public File exportStoreExcelData() throws Exception {
		String str_file_name = "export_store_list";
		String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
		// 配置文件中的路径
		String str_filepath = strRootpath
				.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));
		File file_template = new File(str_filepath);

		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_newfilepath = str_file_dir_path + "store_list_info.xls";
		File file_new = new File(str_newfilepath);
		if (file_new.exists()) {
			file_new.delete();
		}
		FileCopyUtils.copy(file_template, file_new);
		FileInputStream fis_input_excel = new FileInputStream(file_new);
		FileOutputStream fis_out_excel = null;
		Workbook wb_humaninfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
		try {
			setCellStyle_common(wb_humaninfo);
			Sheet sh_job = wb_humaninfo.getSheetAt(0);
			int nJobIndex = 1;
			StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
			StringBuffer sb_where = new StringBuffer();
			// 获取当前登录人
			User sessionUser = null;
			if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
			}
			if ("QYJL".equals(sessionUser.getUsergroup().getCode())) {
				sb_where.append(" and store.rmid=" + sessionUser.getId() + " ");
			}
			// 取得当前登录人 所管理城市
			String cityssql = "";
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			List<DistCity> distCityList = userManager.getCurrentUserCity();
			if (distCityList != null && distCityList.size() > 0) {
				for (DistCity d : distCityList) {
					cityssql += "'" + d.getCityname() + "',";
				}
				cityssql = cityssql.substring(0, cityssql.length() - 1);
			}

			if (cityssql != "" && cityssql.length() > 0) {
				sb_where.append(" and store.city_name in (" + cityssql + ")");
			} else {
				sb_where.append(" and 0=1 ");
			}
			System.out.println(sb_where);
			List<Map<String, Object>> bussinessInfo = storeDao.getStoreListDate(sb_where.toString());
			for (Map<String, Object> map : bussinessInfo) {
				Row obj_row = null;
				int cellIndex = 0;
				sh_job.createRow(nJobIndex);
				obj_row = sh_job.getRow(nJobIndex);
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue((nJobIndex)));// 序号
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("countname")));// 区域
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("townname")));// 街道
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("storetypename")));// 属性
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("superMicro")));// 微超
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("estate")));// 状态
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("open_shop_time")));// 开业时间
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("nature")));// 门店属性
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("tenancyTerm")));// 租期
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("rental")));// 租金
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("payment_method")));// 支付方式
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("detail_address")));// 位置
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("name")));// 店名
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("shopmanager")));// 店长
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("mobilephone")));// 门店电话
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("storeno")));// 门店编号
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("ordnumber")));// 开店序号
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("city_name")));// 城市
				nJobIndex++;
			}
			fis_out_excel = new FileOutputStream(file_new);
			wb_humaninfo.write(fis_out_excel);

		} catch (Exception e) {
			throw e;
		} finally {
			if (fis_out_excel != null) {
				fis_out_excel.close();
			}
			if (fis_input_excel != null) {
				fis_input_excel.close();
			}
		}
		return file_new;
	}

	/**
	 * 获取配置文件
	 * 
	 * @return 配置文件对象
	 */
	public PropertiesValueUtil getPropertiesValueUtil() {
		if (propertiesValueUtil == null) {
			propertiesValueUtil = new PropertiesValueUtil(
					File.separator + "conf" + File.separator + "download_config.properties");
		}
		return propertiesValueUtil;
	}

	public void setCellStyle_common(Workbook wb) {
		cellStyle_common = wb.createCellStyle();
		cellStyle_common.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		cellStyle_common.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle_common.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		cellStyle_common.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		cellStyle_common.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
		cellStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 垂直居中
		cellStyle_common.setWrapText(true);// 设置自动换行
		Font font = wb.createFont();
		font.setFontName("微软雅黑");
		font.setFontHeightInPoints((short) 10);
		cellStyle_common.setFont(font);
	}

	public CellStyle getCellStyle_common() {
		return cellStyle_common;
	}

	public void setCellValue(Row obj_row, int nCellIndex, Object value) {
		Cell cell = obj_row.createCell(nCellIndex);
		cell.setCellStyle(getCellStyle_common());
		cell.setCellValue(new HSSFRichTextString(value == null ? null : value.toString()));
	}

	@Override
	public void renewalStoreOrdNumber() {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		StoreDynamicDao storeDynamicDao = (StoreDynamicDao) SpringHelper.getBean(StoreDynamicDao.class.getName());
		StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");
		// 获取所有有门店的城市
		DistCityCodeManager distCityCodeManager = (DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");
		List<DistCityCode> queryAllDistCityList = distCityCodeManager.queryAllDistCityList();
		for (DistCityCode distCityCode : queryAllDistCityList) {
			int t = 1, j = 1, n = 1, m = 1;
			// 根据城市获取每个城市下的门店(如果有开店时间按开店时间排序如果没有按创建时间排序)
			List<Store> opentime = storeDao.findStoreByCity_nameorderByOpentime(distCityCode.getCityname());
			if (opentime != null && opentime.size() > 0) {
				for (Store store : opentime) {
					store.setOrdnumber(t);
					storeManager.saveObject(store);
					t++;
				}
			}
			List<StoreDynamic> byOpentime = storeDynamicDao
					.findStoreDynamicByCity_nameorderByOpentime(distCityCode.getCityname());
			if (byOpentime != null && byOpentime.size() > 0) {
				for (StoreDynamic storeDynamic : byOpentime) {
					storeDynamic.setOrdnumber(n);
					storeDynamicManager.saveObject(storeDynamic);
					n++;
				}
			}

			// 判断当前城市的最大排序如果有在当前基础上相加如果没有从一开始
			Integer number = storeDao.findMaxStoreOreNumber(distCityCode.getCityname());
			List<Store> isnullOrdnumber = storeDao.findStoreIsnullOrdnumber(distCityCode.getCityname());
			if (isnullOrdnumber != null && isnullOrdnumber.size() > 0) {
				for (Store store : isnullOrdnumber) {
					store.setOrdnumber(j + number);
					storeManager.saveObject(store);
					j++;
				}

			}
			Integer oreNumber = storeDynamicDao.findMaxStoreDynamicOreNumber(distCityCode.getCityname());
			List<StoreDynamic> dynamicIsnullOrdnumber = storeDynamicDao
					.findStoreDynamicIsnullOrdnumber(distCityCode.getCityname());
			if (dynamicIsnullOrdnumber != null && dynamicIsnullOrdnumber.size() > 0) {
				for (StoreDynamic storeDynamic : dynamicIsnullOrdnumber) {
					storeDynamic.setOrdnumber(m + oreNumber);
					storeDynamicManager.saveObject(storeDynamic);
					m++;
				}
			}

		}
	}

	@Override
	public List<Store> getStoreEstateList() {
		List<?> lst_store = getList(FilterFactory.getSimpleFilter("estate is not NULL and estate!='' GROUP BY estate"));
		if (lst_store != null && lst_store.size() > 0) {
			return (List<Store>) lst_store;
		}
		return null;
	}

	// 取得当前登录人门店
	@Override
	public Store queryCurrentStoreInfo() {
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		Long store_id = userManager.getCurrentUserDTO().getStore_id();
		if (store_id != null) {
			return (Store) this.getObject(store_id);
		}
		return null;
	}

	@Override
	public void syncStoreEstate() {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		List<Store> list = storeDao.findStoreDateEstate();
		if (list != null && list.size() > 0) {
			for (Store store : list) {
				store.setEstate("试运营");
				this.saveObject(store);
			}
		}

	}

	@Override
	public List<Store> findStoreDataByName(Store store) {
		// 取得当前登录人 所管理城市
		StringBuilder stringBuilder = new StringBuilder();
		String cityssql = "";
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		List<DistCity> distCityList = userManager.getCurrentUserCity();
		if (distCityList != null && distCityList.size() > 0) {
			for (DistCity d : distCityList) {
				cityssql += "'" + d.getCityname() + "',";
			}
			cityssql = cityssql.substring(0, cityssql.length() - 1);
		}
		if (cityssql != "" && cityssql.length() > 0) {
			stringBuilder.append(" and city_name in (" + cityssql + ")");
		} else {
			stringBuilder.append(" and 0=1 ");
		}
		List<?> lst_store = getList(FilterFactory
				.getSimpleFilter("name like '%" + store.getName() + "%' and flag=0 " + stringBuilder.toString()));
		if (lst_store != null && lst_store.size() > 0) {
			return (List<Store>) lst_store;
		}
		return null;
	}

	@Override
	public Store findStoreByStoreNo(String storeNo) {
		List<?> lst_store = getList(FilterFactory.getSimpleFilter("storeno", storeNo));
		if (lst_store != null && lst_store.size() > 0) {
			return (Store) lst_store.get(0);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findStoreByCurUser(UserDTO userDto, Long city_id) {
		String code = userDto.getUsergroup().getCode();
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		Long id = userDto.getId();
		List<Map<String, Object>> storeList = new ArrayList<Map<String, Object>>();
		if (code.equals("DZ")) {
			List<Store> findStoreByskid = findStoreByskid(id);
			if (findStoreByskid != null && findStoreByskid.size() > 0) {
				for (int i = 0; i < findStoreByskid.size(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					Store store = findStoreByskid.get(i);
					Long store_id = store.getStore_id();
					String name = store.getName();
					map.put("storeno", store.getStoreno());
					map.put("store_id", store_id);
					map.put("name", name);
					storeList.add(map);
				}

			}
			return storeList;
		} else {
			String regex_zb = "^(ZB|zb)\\w*";
			String regex_cs = "^(CS|cs)\\w*";
			// 编译正则表达式
			Pattern pattern_cs = Pattern.compile(regex_cs);
			Pattern pattern_zb = Pattern.compile(regex_zb);
			if (pattern_cs.matcher(code).matches()) {
				code = "CSZJ";
			} else if (code.equals("QYJL")) {
				code = "QYJL";
			} else if (pattern_zb.matcher(code).matches()) {
				code = "ZB";
			} else {
				code = "CSZJ";
			}
			List<Map<String, Object>> allStoreOfCRM = storeDao.getAllStoreOfCRM(id, city_id, code);
			return allStoreOfCRM;
		}
	}

	@Override
	public List<Map<String, Object>> insertVillageInfoData() {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		try {
			boolean deleteStatus = storeDao.cleanDataInfoTable();
			if (deleteStatus) {
				List<Map<String, Object>> list = storeDao.queryBatchResultForVillage();
				for (int i = 0; i < list.size(); i++) {
					String inserSql = "INSERT INTO `df_tinyarea_datainfo` VALUES ";
					Map<String, Object> map_data = (Map<String, Object>) list.get(i);
					/*
					 * INSERT INTO `df_tinyarea_datainfo` VALUES ('1', '18',
					 * '0010X012000001', '芒果牛奶', '5343', '锦什坊街96号楼',
					 * '1101010020000000000011', '张三', '李四', '', '', '42', '42',
					 * '1200.54', '景山店\r\n', '北京', '10', '6', '20', '2017',
					 * '11', '56', '2017-12-12 13:40:25', '', null, '', null,
					 * '2017-12-12 13:40:43');
					 */
					String area_id = map_data.get("area_id") == null ? "" : map_data.get("area_id").toString();
					String area_no = map_data.get("area_no") == null ? "" : map_data.get("area_no").toString();
					String area_name = map_data.get("area_name") == null ? "" : map_data.get("area_name").toString();
					String tinid = map_data.get("tinid") == null ? "" : map_data.get("tinid").toString();
					String tiny_name = map_data.get("tinyname") == null ? "" : map_data.get("tinyname").toString();
					String tiny_code = map_data.get("code_vill") == null ? "" : map_data.get("code_vill").toString();
					String employee_a_name = map_data.get("employee_a_name") == null ? ""
							: map_data.get("employee_a_name").toString();
					String employee_b_name = map_data.get("employee_b_name") == null ? ""
							: map_data.get("employee_b_name").toString();
					String employee_a_no = map_data.get("employee_a_no") == null ? ""
							: map_data.get("employee_a_no").toString();
					String employee_b_no = map_data.get("employee_b_no") == null ? ""
							: map_data.get("employee_b_no").toString();
					String sumbuilding = map_data.get("sumbuilding") == null ? ""
							: map_data.get("sumbuilding").toString();
					String sumhouse = map_data.get("sumhouse") == null ? "" : map_data.get("sumhouse").toString();
					String vallage_area = map_data.get("vallage_area") == null ? ""
							: map_data.get("vallage_area").toString();
					String store_name = map_data.get("store_name") == null ? "" : map_data.get("store_name").toString();
					String city_name = map_data.get("city_name") == null ? "" : map_data.get("city_name").toString();
					String custmer_count = map_data.get("custmer_count") == null ? ""
							: map_data.get("custmer_count").toString();
					String order_count = map_data.get("order_count") == null ? ""
							: map_data.get("order_count").toString();
					String user_count_6 = map_data.get("user_count_6") == null ? ""
							: map_data.get("user_count_6").toString();
					String user_count_18 = map_data.get("user_count_18") == null ? ""
							: map_data.get("user_count_18").toString();
					String store_id = map_data.get("store_id") == null ? "" : map_data.get("store_id").toString();
					inserSql += "('" + i + "','" + area_id + "','" + area_no + "','" + area_name + "','" + tinid + "','"
							+ tiny_name + "','" + tiny_code + "','" + employee_a_name + "','" + employee_b_name + "','"
							+ employee_a_no + "','" + employee_b_no + "','" + sumbuilding + "','" + sumhouse + "','"
							+ vallage_area + "','" + store_name + "','" + city_name + "','" + custmer_count + "','"
							+ order_count + "','" + user_count_18 + "','" + user_count_6 + "','" + store_id + "','"
							+ "2016-09-29 19:02:53" + "','" + "系统管理员" + "','" + "99999" + "','" + "系统管理员" + "','"
							+ "99999" + "','" + "2016-09-29 19:02:53" + "') ";
					storeDao.insertIntoVillageInfo(inserSql);
				}
				System.out.println("-------------查询完毕---------------");
				// List<Map<String, Object>> listDataInfo =
				// storeDao.queryBatchResultForViDataInfo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateVillageInfoUserCount() {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		List<Map<String, Object>> listSix = storeDao.queryAllTinyareaDatainfoUserCountSix();
		List<Map<String, Object>> listEighteen = storeDao.queryAllTinyareaDatainfoUserCountEighteen();
		boolean updateAll = false;
		for (int i = 0; i < listSix.size(); i++) {
			Map<String, Object> userCountSixMap = listSix.get(i);
			String tinyvillage_id6 = userCountSixMap.get("tinyvillage_id") == null ? ""
					: userCountSixMap.get("tinyvillage_id").toString();
			String user_count_6 = userCountSixMap.get("customer_count") == null ? ""
					: userCountSixMap.get("customer_count").toString();
			updateAll = storeDao.updateVillageInfoDataUserCountSix(tinyvillage_id6, user_count_6);
		}
		for (int i = 0; i < listEighteen.size(); i++) {
			Map<String, Object> userCountSixMap = listEighteen.get(i);
			String tinyvillage_id2 = userCountSixMap.get("tinyvillage_id") == null ? ""
					: userCountSixMap.get("tinyvillage_id").toString();
			String user_count_18 = userCountSixMap.get("customer_count") == null ? ""
					: userCountSixMap.get("customer_count").toString();
			updateAll = storeDao.updateVillageInfoDataUserCountEighteen(tinyvillage_id2, user_count_18);
		}

		return updateAll;
	}

	// 同步门店信息
	@Override
	public String syncStore(Store store) {
		String rt = "";
		DynamicManager dynamicManager = (DynamicManager) SpringHelper.getBean("dynamicManager");
		// JSONObject jsonObject = dynamicManager.insertNewTest("测试测试",
		// "2017-12-27");
		// 判断 如果门店编号中含有W（未知门店 ）和储备店 以及测试门店。不推送
		if (store != null && store.getStoreno() != null && store.getName() != null) {
			boolean sync = true;
			if (store.getStoreno().contains("W")) {// 未知门店 不同步
				sync = false;
			}
			if (store.getStoreno().contains("C")) {// 仓店 不同步
				sync = false;
			}
			if (store.getStoreno().contains("V")) {// 虚拟店 不同步
				sync = false;
			}
			if (store.getName().contains("储备店")) {// 储备店 不同步
				sync = false;
			}
			if (store.getName().contains("测试")) {// 测试店 不同步
				sync = false;
			}
			if (store.getName().contains("办公室")) {// 办公室 不同步
				sync = false;
			}
			if (sync) {
				JSONObject jsonObject = dynamicManager.insertNewStore(store.getStoreno(), store.getName(),
						store.getGaode_provinceCode(), store.getGaode_cityCode(), store.getGaode_adCode(),
						store.getGaode_address());
				rt = jsonObject.toString();
			}

		}
		return rt;
	}

	@Override
	public Map<String, Object> selectStoreKeeperInfoByStoreId(Long storeId) {
		Map<String, Object> result = new HashMap<String, Object>();
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		try {
			List<Map<String, Object>> list = storeDao.selectStoreKeeperInfoByStoreId(storeId);
			result.put("storeKeeper", list);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	@Override
	public List<Store> findStoreByNameAndauditor_status(String name) {
		// 获取当前登录人管理的城市
		String cityssql = "";
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		List<DistCity> distCityList = userManager.getCurrentUserCity();
		if (distCityList != null && distCityList.size() > 0) {
			for (DistCity d : distCityList) {
				cityssql += "'" + d.getCityname() + "',";
			}
			cityssql = cityssql.substring(0, cityssql.length() - 1);
		}
		String citycodd = "";
		if (cityssql.length() > 0) {
			citycodd = " and city_name in (" + cityssql + ")";
		} else {
			citycodd = " and 1=0";
		}
		List<?> lst_store = this
				.getList(FilterFactory.getSimpleFilter("name like '%" + name + "%' and flag=0 " + citycodd));
		if (lst_store != null && lst_store.size() > 0) {
			return (List<Store>) lst_store;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Store findStoreByWorkId(String work_id) {
		Store store = null;
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");

		IFilter filter = FilterFactory.getSimpleFilter("work_id", work_id);

		List<Store> list = (List<Store>) storeManager.getList(filter);
		if (list.size() > 0 && list != null) {
			store = list.get(0);
		}
		return store;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Store> findStoreByProvinceId(Long province_id) {
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");

		IFilter filter = FilterFactory.getSimpleFilter("province_id", province_id);

		List<Store> list = (List<Store>) storeManager.getList(filter);
		if (list.size() > 0 && list != null) {
			return list;
		}
		return null;
	}

	// 门店运营 查询状态列表
	@Override
	public Map<String, Object> queryStoreStatusList(Store store, PageInfo pageInfo) {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		Map<String, Object> returnMap = null;
		try {
			returnMap = storeDao.queryStoreStatusList(store, pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}

	@Override
	public Map<String, Object> findStoreNot(QueryConditions conditions) {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 取得当前登录人 所管理城市
		String cityssql = "";
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		List<DistCity> distCityList = userManager.getCurrentUserCity();
		if (distCityList != null && distCityList.size() > 0) {
			for (DistCity d : distCityList) {
				cityssql += "'" + d.getCityname() + "',";
			}
			cityssql = cityssql.substring(0, cityssql.length() - 1);
		}

		if (cityssql != "" && cityssql.length() > 0) {
			sb_where.append(" and sto.city_name in (" + cityssql + ")");
		} else {
			sb_where.append(" and 0=1 ");
		}
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// obj_page.setRecordsPerPage(4);
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();
		System.out.println(sb_where);
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "门店基础信息");
		map_result.put("data", storeDao.findStoreXuanZhi(sb_where.toString(), obj_page));
		return map_result;
	}

	@Override
	public Store insertStoresyncDynamicStore(StoreDynamic storeDynamic) {
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		Store store = this.findStore(storeDynamic.getStore_id());
		if (store != null) {
			store.setSuperMicro(storeDynamic.getSuperMicro());
			store.setEstate(storeDynamic.getEstate());
			store.setStoretype(storeDynamic.getStoretype());
			store.setStoretypename(storeDynamic.getStoretypename());
			store.setOpen_shop_time(storeDynamic.getOpen_shop_time());
			store.setAddress(storeDynamic.getAddress());
			store.setMobilephone(storeDynamic.getMobilephone());
			store.setName(storeDynamic.getName());
			store.setTown_id(storeDynamic.getTown_id());
			store.setTown_name(storeDynamic.getTown_name());
			store.setVillage_id(storeDynamic.getVillage_id());
			store.setTinyvillage_Id(storeDynamic.getTinyvillage_Id());
			store.setType(1);
			store.setCounty_ids(storeDynamic.getCounty_ids());
			store.setNature(storeDynamic.getNature());
			store.setTenancyTerm(storeDynamic.getTenancyTerm());
			store.setRental(storeDynamic.getRental());
			store.setPayment_method(storeDynamic.getPayment_method());
			store.setRent_area(storeDynamic.getRent_area());
			store.setUsable_area(storeDynamic.getUsable_area());
			store.setIncrease(storeDynamic.getIncrease());
			store.setRent_free(storeDynamic.getRent_free());
			store.setTaxes(storeDynamic.getTaxes());
			store.setAgency_fee(storeDynamic.getAgency_fee());
			store.setIncrease_fee(storeDynamic.getIncrease_fee());
			store.setStore_position(storeDynamic.getStore_position());
			store.setPlace_town_id(storeDynamic.getPlace_town_id());
			store.setGaode_adCode(storeDynamic.getGaode_adCode());
			store.setGaode_address(storeDynamic.getGaode_address());
			store.setGaode_cityCode(storeDynamic.getGaode_cityCode());
			store.setPlatformid(storeDynamic.getPlatformid());
			store.setNumber(storeDynamic.getNumber());
			store.setId(storeDynamic.getId());
			store.setWhite(storeDynamic.getWhite());
			store.setPlatformname(storeDynamic.getPlatformname());
			store.setGaode_provinceCode(storeDynamic.getGaode_provinceCode());
			store.setRemark(storeDynamic.getRemark());
			store.setSkid(storeDynamic.getSkid());
			store.setCounty_id(storeDynamic.getCounty_id());
			store.setCity_id(storeDynamic.getCity_id());
			store.setProvince_id(storeDynamic.getProvince_id());
			store.setCityName(storeDynamic.getCityName());
			store.setUpdate_user(storeDynamic.getUpdate_user());
			store.setUpdate_time(new Date());
			store.setCityNo(storeDynamic.getCityNo());
			store.setStoreno(storeDynamic.getStoreno());
			store.setVersion(storeDynamic.getVersion());
			store.setCreate_user(storeDynamic.getCreate_user());
			store.setOrdnumber(storeDynamic.getOrdnumber());
			storeManager.saveObject(store);
		} else {
			store = new Store();
			store.setStore_id(storeDynamic.getStore_id());
			store.setSuperMicro(storeDynamic.getSuperMicro());
			store.setEstate(storeDynamic.getEstate());
			store.setStoretype(storeDynamic.getStoretype());
			store.setStoretypename(storeDynamic.getStoretypename());
			store.setOpen_shop_time(storeDynamic.getOpen_shop_time());
			store.setAddress(storeDynamic.getAddress());
			store.setMobilephone(storeDynamic.getMobilephone());
			store.setName(storeDynamic.getName());
			store.setTown_id(storeDynamic.getTown_id());
			store.setTown_name(storeDynamic.getTown_name());
			store.setVillage_id(storeDynamic.getVillage_id());
			store.setTinyvillage_Id(storeDynamic.getTinyvillage_Id());
			store.setType(1);
			store.setCounty_ids(storeDynamic.getCounty_ids());
			store.setNature(storeDynamic.getNature());
			store.setTenancyTerm(storeDynamic.getTenancyTerm());
			store.setRental(storeDynamic.getRental());
			store.setPayment_method(storeDynamic.getPayment_method());
			store.setRent_area(storeDynamic.getRent_area());
			store.setUsable_area(storeDynamic.getUsable_area());
			store.setIncrease(storeDynamic.getIncrease());
			store.setRent_free(storeDynamic.getRent_free());
			store.setTaxes(storeDynamic.getTaxes());
			store.setAgency_fee(storeDynamic.getAgency_fee());
			store.setIncrease_fee(storeDynamic.getIncrease_fee());
			store.setStore_position(storeDynamic.getStore_position());
			store.setPlace_town_id(storeDynamic.getPlace_town_id());
			store.setGaode_adCode(storeDynamic.getGaode_adCode());
			store.setGaode_address(storeDynamic.getGaode_address());
			store.setGaode_cityCode(storeDynamic.getGaode_cityCode());
			store.setPlatformid(storeDynamic.getPlatformid());
			store.setNumber(storeDynamic.getNumber());
			store.setId(storeDynamic.getId());
			store.setWhite(storeDynamic.getWhite());
			store.setPlatformname(storeDynamic.getPlatformname());
			store.setGaode_provinceCode(storeDynamic.getGaode_provinceCode());
			store.setRemark(storeDynamic.getRemark());
			store.setSkid(storeDynamic.getSkid());
			store.setCounty_id(storeDynamic.getCounty_id());
			store.setCity_id(storeDynamic.getCity_id());
			store.setProvince_id(storeDynamic.getProvince_id());
			store.setCityName(storeDynamic.getCityName());
			store.setUpdate_user(storeDynamic.getUpdate_user());
			store.setUpdate_time(new Date());
			store.setCityNo(storeDynamic.getCityNo());
			store.setStoreno(storeDynamic.getStoreno());
			store.setVersion(storeDynamic.getVersion());
			store.setCreate_user(storeDynamic.getCreate_user());
			store.setOrdnumber(storeDynamic.getOrdnumber());
			storeDao.insertStore(store);
		}
		return store;
	}

	@Override
	public List<Map<String, Object>> findStoreData() {
		System.out.println("111111111111");
		List<Store> storeList = (List<Store>) this.getList(FilterFactory.getSimpleFilter(
				"name NOT LIKE '%储备%' AND name not LIKE '%测试%' AND name not LIKE '%办公室%' AND name not LIKE '%暂停%' AND flag=0 AND platformid is not null  AND platformid!=''"));
		Object[] platformIdArray = new Object[storeList.size()];
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < storeList.size(); i++) {// 获取门店platformId
			Store store = storeList.get(i);
			platformIdArray[i] = store.getPlatformid();
		}
		// 查询门店中心坐标点
		MongoDbUtil mDbUtil = (MongoDbUtil) SpringHelper.getBean("mongodb");
		MongoDatabase database = mDbUtil.getDatabase();
		MongoCollection<Document> collection2 = database.getCollection("store_position");
		BasicDBObject query2 = new BasicDBObject();
		query2.append("_id", new BasicDBObject("$in", platformIdArray));
		FindIterable<Document> dIterable2 = collection2.find(query2);
		MongoCursor<Document> cursor2 = dIterable2.iterator();
		while (cursor2.hasNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			Document teDocument2 = cursor2.next();
			for (Store store : storeList) {
				if (store.getPlatformid().equals(teDocument2.get("_id"))) {
					map.put("pid", teDocument2.get("_id"));
					map.put("location", teDocument2.get("location"));
					map.put("name", store.getName());
					map.put("phone", store.getMobilephone());
					list.add(map);
					break;
				}
			}

		}
		return list;
	}
}
