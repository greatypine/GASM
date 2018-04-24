package com.cnpc.pms.personal.manager.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.manager.AttachmentManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.dynamic.manager.DynamicManager;
import com.cnpc.pms.personal.dao.ProvinceDao;
import com.cnpc.pms.personal.dao.StoreDynamicDao;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.County;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.DistCityCode;
import com.cnpc.pms.personal.entity.Province;
import com.cnpc.pms.personal.entity.StoreDynamic;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.manager.ApprovalManager;
import com.cnpc.pms.personal.manager.CountyManager;
import com.cnpc.pms.personal.manager.DistCityCodeManager;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
import com.cnpc.pms.personal.manager.StoreDynamicManager;
import com.cnpc.pms.personal.manager.StoreKeeperManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.personal.manager.WorkInfoManager;
import com.cnpc.pms.personal.util.DataTransfromUtil;
import com.cnpc.pms.platform.entity.PlatformStore;
import com.cnpc.pms.platform.entity.SysArea;
import com.cnpc.pms.platform.manager.PlatformStoreManager;
import com.cnpc.pms.platform.manager.SysAreaManager;

public class StoreDynamicManagerImpl extends BaseManagerImpl implements StoreDynamicManager {

	@Override
	public StoreDynamic insertStoreDynamicData(StoreDynamic storeDynamic) throws Exception {
		long millis = System.currentTimeMillis();
		StoreDynamicDao storeDynamicDao = (StoreDynamicDao) SpringHelper.getBean(StoreDynamicDao.class.getName());
		User sessionUser = null;
		StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");
		StoreKeeperManager storeKeeperManager = (StoreKeeperManager) SpringHelper.getBean("storeKeeperManager");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		PlatformStoreManager platformStoreManager = (PlatformStoreManager) SpringHelper.getBean("platformStoreManager");
		PlatformStore platformStore = platformStoreManager.findPlatStoreById(storeDynamic.getPlatformid());
		DistCityCodeManager distCityCodeManager = (DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");
		DistCityCode distCityCode = distCityCodeManager.queryDistCityCodeByName(storeDynamic.getCityName());
		WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
		SysAreaManager sysAreaManager = (SysAreaManager) SpringHelper.getBean("sysAreaManager");
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		StoreDynamic update_store_dynamic = null;
		if (storeDynamic.getStore_id() == null) {
			if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
			}
			if (platformStore != null) {
				storeDynamic.setPlatformid(platformStore.getId());
				storeDynamic.setNumber(platformStore.getNumber());
				storeDynamic.setId(platformStore.getId());
				storeDynamic.setWhite(platformStore.getWhite());
				storeDynamic.setPlatformname(platformStore.getName());
			} else {
				storeDynamic.setPlatformid(null);
				storeDynamic.setNumber(null);
				storeDynamic.setId(null);
				storeDynamic.setWhite(null);
				storeDynamic.setPlatformname(null);
			}
			storeDynamic.setCityNo(distCityCode.getCityno());
			storeDynamic.setCreate_user(sessionUser.getName());
			storeDynamic.setCreate_time(new Date());
			TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
			Town town = townManager.getTownById(DataTransfromUtil.TownIdTransFrom(storeDynamic.getTown_id()));
			if (town != null) {
				storeDynamic.setCounty_id(town.getCounty_id());
				CountyManager countyManager = (CountyManager) SpringHelper.getBean("countyManager");
				County county = countyManager.getCountyById(town.getCounty_id());
				storeDynamic.setCity_id(county.getCity_id());
				ProvinceDao provinceDao = (ProvinceDao) SpringHelper.getBean(ProvinceDao.class.getName());
				Province province = provinceDao
						.getProvinceInfoByTown_id(DataTransfromUtil.TownIdTransFrom(storeDynamic.getTown_id()));
				storeDynamic.setProvince_id(province.getId());
			}
			// 门店排序
			StoreDynamic findMaxOrdnumber = storeDynamicDao.findMaxOrdnumber(storeDynamic.getCityName());
			if (findMaxOrdnumber != null && findMaxOrdnumber.getOrdnumber() != null) {
				storeDynamic.setOrdnumber(findMaxOrdnumber.getOrdnumber() + 1);
			} else {
				storeDynamic.setOrdnumber(1);
			}
			if (!"V".equals(storeDynamic.getStoretype())) {
				storeDynamic.setAuditor_status(1);
			}
			storeDynamic.setWork_id(millis + "");
			if (storeDynamic.getGaode_cityCode() != null && !"".equals(storeDynamic.getGaode_cityCode())) {
				SysArea sysArea = sysAreaManager.findSysAreaByCityCode(storeDynamic.getGaode_cityCode());
				if (sysArea != null) {
					storeDynamic.setGaode_provinceCode(sysArea.getParentCode());
				}
			}
			storeDynamicManager.saveObject(storeDynamic);
			updateStoreDynamicNo(storeDynamic);
			// 同步门店
			try {
				// 虚拟店不调用审批流程
				if (!"V".equals(storeDynamic.getStoretype())) {
					Date date = new Date();
					SimpleDateFormat myFmt1 = new SimpleDateFormat("yyyy-MM");
					workInfoManager.StartFlow(storeDynamic.getStore_id(), "门店选址审核", myFmt1.format(date),
							storeDynamic.getWork_id(), "save", "");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ("V".equals(storeDynamic.getStoretype())) {
				storeManager.insertStoresyncDynamicStore(storeDynamic);
			}
			// 同步门店
			try {
				syncStore(storeDynamic);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return storeDynamic;
		} else {

			update_store_dynamic = this.findStoreDynamic(storeDynamic.getStore_id());
			Integer auditorbefore = update_store_dynamic.getAuditor_status();
			// 当门店类型为未知并且门店编号不为空的时候可以修改门店编号
			if (update_store_dynamic.getStoreno() != null && "W".equals(update_store_dynamic.getStoretype())
					&& !"W".equals(storeDynamic.getStoretype())
					&& "W".equals(update_store_dynamic.getStoreno().substring(4, 5))) {
				String att1 = update_store_dynamic.getStoreno().substring(0, 4);
				String att2 = update_store_dynamic.getStoreno().substring(5, 9);
				String att3 = att1 + storeDynamic.getStoretype() + att2;
				update_store_dynamic.setStoreno(att3);
			}
			User user = null;
			boolean isupdate = false;
			if (update_store_dynamic.getName() != null && !update_store_dynamic.equals(storeDynamic.getName())) {
				isupdate = true;
			}
			if (platformStore != null) {
				update_store_dynamic.setPlatformid(platformStore.getId());
				update_store_dynamic.setNumber(platformStore.getNumber());
				update_store_dynamic.setId(platformStore.getId());
				update_store_dynamic.setWhite(platformStore.getWhite());
				update_store_dynamic.setPlatformname(platformStore.getName());
			} else {
				update_store_dynamic.setPlatformid(null);
				update_store_dynamic.setNumber(null);
				update_store_dynamic.setId(null);
				update_store_dynamic.setWhite(null);
				update_store_dynamic.setPlatformname(null);
			}
			update_store_dynamic.setSuperMicro(storeDynamic.getSuperMicro());
			update_store_dynamic.setEstate(storeDynamic.getEstate());
			update_store_dynamic.setStoretype(storeDynamic.getStoretype());
			update_store_dynamic.setStoretypename(storeDynamic.getStoretypename());
			update_store_dynamic.setOpen_shop_time(storeDynamic.getOpen_shop_time());
			update_store_dynamic.setAddress(storeDynamic.getAddress());
			update_store_dynamic.setMobilephone(storeDynamic.getMobilephone());
			update_store_dynamic.setName(storeDynamic.getName());
			update_store_dynamic.setTown_id(storeDynamic.getTown_id());
			update_store_dynamic.setTown_name(storeDynamic.getTown_name());
			update_store_dynamic.setVillage_id(storeDynamic.getVillage_id());
			update_store_dynamic.setTinyvillage_Id(storeDynamic.getTinyvillage_Id());
			update_store_dynamic.setType(1);
			update_store_dynamic.setCounty_ids(storeDynamic.getCounty_ids());
			update_store_dynamic.setNature(storeDynamic.getNature());
			update_store_dynamic.setTenancyTerm(storeDynamic.getTenancyTerm());
			update_store_dynamic.setRental(storeDynamic.getRental());
			update_store_dynamic.setPayment_method(storeDynamic.getPayment_method());
			update_store_dynamic.setRent_area(storeDynamic.getRent_area());
			update_store_dynamic.setUsable_area(storeDynamic.getUsable_area());
			update_store_dynamic.setIncrease(storeDynamic.getIncrease());
			update_store_dynamic.setRent_free(storeDynamic.getRent_free());
			update_store_dynamic.setTaxes(storeDynamic.getTaxes());
			update_store_dynamic.setAgency_fee(storeDynamic.getAgency_fee());
			update_store_dynamic.setIncrease_fee(storeDynamic.getIncrease_fee());
			update_store_dynamic.setStore_position(storeDynamic.getStore_position());
			update_store_dynamic.setPlace_town_id(storeDynamic.getPlace_town_id());
			update_store_dynamic.setGaode_adCode(storeDynamic.getGaode_adCode());
			update_store_dynamic.setGaode_address(storeDynamic.getGaode_address());
			update_store_dynamic.setGaode_cityCode(storeDynamic.getGaode_cityCode());
			if (storeDynamic.getGaode_cityCode() != null && !"".equals(storeDynamic.getGaode_cityCode())) {
				SysArea sysArea = sysAreaManager.findSysAreaByCityCode(storeDynamic.getGaode_cityCode());
				if (sysArea != null) {
					update_store_dynamic.setGaode_provinceCode(sysArea.getParentCode());
				}
			}
			if (!"V".equals(storeDynamic.getStoretype())) {
				update_store_dynamic.setAuditor_status(1);
			}
			if (storeDynamic.getRemark() != null && !"".equals(storeDynamic.getRemark())) {
				update_store_dynamic.setRemark(storeDynamic.getRemark());
			}
			update_store_dynamic.setSkid(storeDynamic.getSkid());
			TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
			Town town = townManager.getTownById(DataTransfromUtil.TownIdTransFrom(storeDynamic.getTown_id()));
			if (town != null) {
				update_store_dynamic.setCounty_id(town.getCounty_id());
				CountyManager countyManager = (CountyManager) SpringHelper.getBean("countyManager");
				County county = countyManager.getCountyById(town.getCounty_id());
				update_store_dynamic.setCity_id(county.getCity_id());
				ProvinceDao provinceDao = (ProvinceDao) SpringHelper.getBean(ProvinceDao.class.getName());
				Province province = provinceDao
						.getProvinceInfoByTown_id(DataTransfromUtil.TownIdTransFrom(storeDynamic.getTown_id()));
				update_store_dynamic.setProvince_id(province.getId());
			}
			update_store_dynamic.setCityName(storeDynamic.getCityName());
			if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
			}
			update_store_dynamic.setUpdate_user(sessionUser.getName());
			update_store_dynamic.setUpdate_time(new Date());
			update_store_dynamic.setCityNo(distCityCode.getCityno());
			if (!"V".equals(update_store_dynamic.getStoretype())) {
				update_store_dynamic.setAuditor_status(1);
			}
			if (auditorbefore != null && auditorbefore == 3) {
				update_store_dynamic.setWork_id(millis + "");
			}
			storeDynamicManager.saveObject(update_store_dynamic);
			// this.updateUserStoreId(update_store_dynamic.getStore_id());//修改用户的门店id

			if (isupdate) {
				HumanresourcesManager hManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
				hManager.updateStoreNameById(update_store_dynamic.getStore_id(), update_store_dynamic.getName());
				ApprovalManager approvalManager = (ApprovalManager) SpringHelper.getBean("approvalManager");
				approvalManager.updateStoreNameById(update_store_dynamic.getStore_id(), update_store_dynamic.getName());
			}
			if ("V".equals(storeDynamic.getStoretype())) {
				storeManager.insertStoresyncDynamicStore(update_store_dynamic);
			}

			// 同步门店
			try {
				// 虚拟店不调用审批流程
				if (!"V".equals(update_store_dynamic.getStoretype())) {
					Date date = new Date();
					SimpleDateFormat myFmt1 = new SimpleDateFormat("yyyy-MM");
					if (auditorbefore == 2) {
						workInfoManager.StartFlow(update_store_dynamic.getStore_id(), "门店选址审核", myFmt1.format(date),
								update_store_dynamic.getWork_id(), "update", "");
					} else if (auditorbefore == 3) {
						workInfoManager.StartFlow(update_store_dynamic.getStore_id(), "门店选址审核", myFmt1.format(date),
								update_store_dynamic.getWork_id() + "", "save", "");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			syncStore(update_store_dynamic);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return update_store_dynamic;
	}

	@Override
	public Map<String, Object> queryStoreDynamicList(QueryConditions conditions) {
		StoreDynamicDao storeDynamicDao = (StoreDynamicDao) SpringHelper.getBean(StoreDynamicDao.class.getName());
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
		map_result.put("data", storeDynamicDao.getStoreDynamicInfoList(sb_where.toString(), obj_page));
		return map_result;
	}

	// 修改门店编号
	public void updateStoreDynamicNo(StoreDynamic storeDynamic) throws Exception {
		String storetype = "";
		// 如果门店为虚拟门店的时候根据虚拟门店判断门店生成独立的编码
		if (storeDynamic.getStoreno() == null || "".equals(storeDynamic.getStoreno())) {
			StoreDynamicDao storeDynamicDao = (StoreDynamicDao) SpringHelper.getBean(StoreDynamicDao.class.getName());
			DistCityCodeManager distCityCodeManager = (DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");
			DistCityCode distCityCode = distCityCodeManager.queryDistCityCodeByName(storeDynamic.getCityName());
			if (distCityCode == null) {
				throw new Exception("请联系管理员,维护城市编码");
			}
			String string = storeDynamicDao.getMaxStoreDynamicNo(storeDynamic);
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
				storetype = distCityCode.getCityno() + storeDynamic.getStoretype() + string;
			} else {
				storetype = distCityCode.getCityno() + storeDynamic.getStoretype() + "0001";
			}
			storeDynamic.setStoreno(storetype);
			StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");
			storeDynamicManager.saveObject(storeDynamic);
		}
	}

	@Override
	public StoreDynamic findStoreDynamic(Long storeid) {
		StoreDynamic storeDynamic = null;
		StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");

		IFilter filter = FilterFactory.getSimpleFilter("store_id", storeid);

		List<StoreDynamic> list = (List<StoreDynamic>) storeDynamicManager.getList(filter);
		if (list.size() > 0 && list != null) {
			storeDynamic = list.get(0);
		}
		return storeDynamic;
	}

	@Override
	public Map<String, Object> getStoreDynamicById(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		StoreDynamic storeDynamic = null;
		StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");
		TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
		CountyManager countyManager = (CountyManager) SpringHelper.getBean("countyManager");
		AttachmentManager attachmentManager = (AttachmentManager) SpringHelper.getBean("attachmentManager");
		IFilter filter = FilterFactory.getSimpleFilter("store_id", id);
		List<StoreDynamic> list = (List<StoreDynamic>) storeDynamicManager.getList(filter);
		if (list.size() > 0 && list != null) {
			storeDynamic = list.get(0);
			map.put("platformid", storeDynamic.getPlatformid());
			map.put("platformname", storeDynamic.getPlatformname());
			map.put("name", storeDynamic.getName());
			map.put("mobilephone", storeDynamic.getMobilephone());
			map.put("address", storeDynamic.getAddress());
			map.put("open_shop_time", transfromTime(storeDynamic.getOpen_shop_time()));
			map.put("detail_address", storeDynamic.getDetail_address());
			map.put("cityName", storeDynamic.getCityName());
			map.put("skid", storeDynamic.getSkid());
			if (storeDynamic.getSkid() != null && !"".equals(storeDynamic.getSkid())) {
				UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
				User sessionUser = userManager.findUserById(storeDynamic.getSkid());
				if (sessionUser != null) {
					map.put("storemanager", sessionUser.getName());
				}
			}
			if (storeDynamic.getTown_id() != null && !"".equals(storeDynamic.getTown_id())) {
				String[] split = storeDynamic.getTown_id().split(",");
				String stt = "";
				for (String string : split) {
					Town town = townManager.findTownById(Long.parseLong(string));
					stt += town.getName() + ",";
				}
				map.put("town_name", stt.substring(0, stt.length() - 1));
			} else {
				map.put("town_name", "");
			}
			if (storeDynamic.getPlace_town_id() != null && !"".equals(storeDynamic.getPlace_town_id())) {
				Town town = townManager.findTownById(storeDynamic.getPlace_town_id());
				map.put("place_town_name", town.getName());
			} else {
				map.put("place_town_name", "");
			}
			map.put("place_town_id", storeDynamic.getPlace_town_id());
			map.put("town_id", storeDynamic.getTown_id());
			map.put("remark", storeDynamic.getRemark());
			map.put("storetype", storeDynamic.getStoretype());
			map.put("storeno", storeDynamic.getStoreno());
			map.put("superMicro", storeDynamic.getSuperMicro());
			map.put("estate", storeDynamic.getEstate());
			map.put("county_ids", storeDynamic.getCounty_ids());
			if (storeDynamic.getCounty_ids() != null && !"".equals(storeDynamic.getCounty_ids())) {
				String countyName = "";
				String[] split = storeDynamic.getCounty_ids().split(",");
				for (String string : split) {
					County county = countyManager.getCountyById(Long.parseLong(string));
					countyName += county.getName() + ",";
				}
				map.put("county_name", countyName.substring(0, countyName.length() - 1));
			}
			map.put("nature", storeDynamic.getNature());
			map.put("tenancyTerm", storeDynamic.getTenancyTerm());
			map.put("rental", storeDynamic.getRental());
			map.put("payment_method", storeDynamic.getPayment_method());
			map.put("rent_area", storeDynamic.getRent_area());
			map.put("usable_area", storeDynamic.getUsable_area());
			map.put("increase", storeDynamic.getIncrease());
			map.put("rent_free", storeDynamic.getRent_free());
			map.put("taxes", storeDynamic.getTaxes());
			map.put("agency_fee", storeDynamic.getAgency_fee());
			map.put("increase_fee", storeDynamic.getIncrease_fee());
			map.put("auditor_status", storeDynamic.getAuditor_status());
			map.put("store_position", storeDynamic.getStore_position());
			map.put("gaode_citycode", storeDynamic.getGaode_cityCode());
			map.put("gaode_address", storeDynamic.getGaode_address());
			map.put("gaode_adcode", storeDynamic.getGaode_adCode());
			Attachment attachment = attachmentManager.findAttachmentByStoreIdType(storeDynamic.getStore_id(), 3);
			if (attachment != null) {
				map.put("contract", attachment.getFile_name());
				map.put("url", attachment.getFile_path().split("contract")[1]);
			} else {
				map.put("contract", "");
			}
		}
		return map;
	}

	@Override
	public StoreDynamic getStoreDynamicByCityAndName(StoreDynamic storeDynamic) {
		StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");
		IFilter filter = FilterFactory.getSimpleFilter("name like '%" + storeDynamic.getName()
				+ "%' and city_name like '%" + storeDynamic.getCityName() + "%'");
		List<StoreDynamic> list = (List<StoreDynamic>) storeDynamicManager.getList(filter);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public String transfromTime(Date time) {
		if (time == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String string = format.format(time);
		return string;
	}

	@Override
	public StoreDynamic findStoreDynamicByWorkId(String work_id) {
		StoreDynamic storeDynamic = null;
		StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");

		IFilter filter = FilterFactory.getSimpleFilter("work_id", work_id);

		List<StoreDynamic> list = (List<StoreDynamic>) storeDynamicManager.getList(filter);
		if (list.size() > 0 && list != null) {
			storeDynamic = list.get(0);
		}
		return storeDynamic;
	}

	// 根据store_id回写 店长的skid
	@Override
	public int updateStoreskid(String store_ids, Long userid) {
		StoreDynamicDao storeDynamicDao = (StoreDynamicDao) SpringHelper.getBean(StoreDynamicDao.class.getName());
		int updateStoreskid = storeDynamicDao.updateStoreskid(store_ids, userid);
		return updateStoreskid;
	}

	// 根据store_id回写 运营经理的rmid
	@Override
	public int updateStorermid(String store_ids, Long userid) {
		StoreDynamicDao storeDynamicDao = (StoreDynamicDao) SpringHelper.getBean(StoreDynamicDao.class.getName());
		int updateStorermid = storeDynamicDao.updateStorermid(store_ids, userid);
		return updateStorermid;
	}

	@Override
	public StoreDynamic saveCityStoreDynamic(StoreDynamic storeDynamic) {
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		storeDynamic.setCreate_user(sessionUser.getName());
		Date date = new Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		storeDynamic.setCreate_time(sdate);
		StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");
		storeDynamicManager.saveObject(storeDynamic);
		return storeDynamic;
	}

	// 同步门店信息
	@Override
	public String syncStore(StoreDynamic storeDynamic) {
		String rt = "";
		DynamicManager dynamicManager = (DynamicManager) SpringHelper.getBean("dynamicManager");
		// JSONObject jsonObject = dynamicManager.insertNewTest("测试测试",
		// "2017-12-27");
		// 判断 如果门店编号中含有W（未知门店 ）和储备店 以及测试门店。不推送
		if (storeDynamic != null && storeDynamic.getStoreno() != null && storeDynamic.getName() != null) {
			boolean sync = true;
			if (storeDynamic.getStoreno().contains("W")) {// 未知门店 不同步
				sync = false;
			}
			if (storeDynamic.getStoreno().contains("C")) {// 仓店 不同步
				sync = false;
			}
			if (storeDynamic.getStoreno().contains("V")) {// 虚拟店 不同步
				sync = false;
			}
			if (storeDynamic.getName().contains("储备店")) {// 储备店 不同步
				sync = false;
			}
			if (storeDynamic.getName().contains("测试")) {// 测试店 不同步
				sync = false;
			}
			if (storeDynamic.getName().contains("办公室")) {// 办公室 不同步
				sync = false;
			}
			if (sync) {
				JSONObject jsonObject = dynamicManager.insertNewStore(storeDynamic.getStoreno(), storeDynamic.getName(),
						storeDynamic.getGaode_provinceCode(), storeDynamic.getGaode_cityCode(),
						storeDynamic.getGaode_adCode(), storeDynamic.getGaode_address());
				rt = jsonObject.toString();
			}

		}
		return rt;
	}

	// 根据门店名称查询门店
	@SuppressWarnings("unchecked")
	@Override
	public List<StoreDynamic> findStoreDynamicListByName(String store_name) {
		StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");

		IFilter filter = FilterFactory.getSimpleFilter("name like '%" + store_name + "%' and flag=0");

		List<StoreDynamic> list = (List<StoreDynamic>) storeDynamicManager.getList(filter);
		if (list.size() > 0 && list != null) {
			return list;
		}
		return null;
	}

	@Override
	public List<StoreDynamic> findStoreDynamicByCityData(String cityName) {
		List<?> lst_store = getList(FilterFactory.getSimpleFilter("cityName in (" + cityName + ")"));
		if (lst_store != null && lst_store.size() > 0) {
			return (List<StoreDynamic>) lst_store;
		}
		return null;
	}

	@Override
	public StoreDynamic updateStoreEstate(Long store_id) {
		// 根据门店id查询门店信息
		StoreDynamic storeDynamic = this.findStoreDynamic(store_id);
		storeDynamic.setEstate("运营中");
		StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");
		storeDynamicManager.saveObject(storeDynamic);
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		storeManager.insertStoresyncDynamicStore(storeDynamic);
		return storeDynamic;
	}

}
