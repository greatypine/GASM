package com.cnpc.pms.personal.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.DistCityCode;
import com.cnpc.pms.personal.entity.StoreDynamic;
import com.cnpc.pms.personal.manager.DistCityCodeManager;
import com.cnpc.pms.personal.manager.DistCityManager;
import com.cnpc.pms.personal.manager.StoreDynamicManager;
import com.cnpc.pms.personal.manager.StoreManager;

@SuppressWarnings("all")
public class DistCityCodeManagerImpl extends BizBaseCommonManager implements DistCityCodeManager {

	/**
	 * 新增城市编码维护 以及 添加该城市的默认的储备店
	 */
	@Override
	public DistCityCode saveDistCityCode(DistCityCode args) {
		DistCityCode distCityCode = queryDistCityCodeByCode(args.getCitycode());
		DistCityCode distCityCode_name = queryDistCityCodeByName(args.getCityname());
		DistCityCode distCityCode_no = queryDistCityCodeByCityNo(args.getCityno());
		DistCityCode retCityCode = null;
		if (distCityCode != null || distCityCode_name != null || distCityCode_no != null) {
			return null;
		} else {
			retCityCode = new DistCityCode();
			retCityCode.setCityname(args.getCityname());
			retCityCode.setCitycode(args.getCitycode());
			retCityCode.setCityno(args.getCityno());

			User sessionUser = null;
			if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
			}
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			// insert处理时添加创建人和创建时间
			if (null == retCityCode.getCreate_time()) {
				retCityCode.setCreate_time(sdate);
				if (null != sessionUser) {
					retCityCode.setCreate_user(sessionUser.getCode());
					retCityCode.setCreate_user_id(sessionUser.getId());
				}
			}
			retCityCode.setUpdate_time(sdate);
			if (null != sessionUser) {
				retCityCode.setUpdate_user(sessionUser.getCode());
				retCityCode.setUpdate_user_id(sessionUser.getId());
			}

			// 保存完城市 ，将该城市 添加到 城市权限分配中，为全部的 人员中。
			List<DistCityCode> distCityCodes = this.queryAllDistCityList();
			List<Long> pk_userList = new ArrayList<Long>();
			DistCityManager distCityManager = (DistCityManager) SpringHelper.getBean("distCityManager");
			List<?> codeList = distCityManager.queryDistCityCount();
			if (codeList != null && codeList.size() > 0) {
				for (Object o : codeList) {
					Map<String, Object> obj = (Map<String, Object>) o;
					if (obj.get("citycount") != null
							&& Integer.parseInt(obj.get("citycount").toString()) == distCityCodes.size()) {
						pk_userList.add(Long.parseLong(obj.get("pk_userid").toString()));
					}
				}
			}
			// 如果是全部的用户 则在全部的基础上 添加 新增加的城市
			if (pk_userList != null && pk_userList.size() > 0) {
				for (Long userid : pk_userList) {
					DistCity d = new DistCity();
					d.setCityname(retCityCode.getCityname());
					d.setCitycode(retCityCode.getCitycode());
					d.setPk_userid(userid);
					distCityManager.saveDistCity(d);
				}
			}
			saveObject(retCityCode);

			// 存完之后，添加 该城市的默认的 储备门店
			String storename = retCityCode.getCityname() + "储备店";
			String cityname = retCityCode.getCityname();
			String storeno = retCityCode.getCityno() + "W0000";
			// ..调用storemanager的保存方法只存城市和名称，编号，类型 及类型名称
			StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
			StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");
			StoreDynamic storeDynamic = new StoreDynamic();
			storeDynamic.setName(storename);
			storeDynamic.setCityName(cityname);
			storeDynamic.setStoreno(storeno);
			storeDynamic.setStoretype("W");
			storeDynamic.setStoretypename("未知");
			try {
				StoreDynamic dynamic = storeDynamicManager.saveCityStoreDynamic(storeDynamic);
				storeManager.insertStoresyncDynamicStore(dynamic);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		return retCityCode;
	}

	@Override
	public DistCityCode queryDistCityCodeByName(String name) {
		IFilter iFilter = FilterFactory.getSimpleFilter(" cityname = '" + name + "'");
		List<?> lst_groupList = this.getList(iFilter);
		if (lst_groupList != null && lst_groupList.size() > 0) {
			DistCityCode distCityCode = (DistCityCode) lst_groupList.get(0);
			return distCityCode;
		} else {
			return null;
		}
	}

	@Override
	public DistCityCode queryDistCityCodeByCityNo(String cityno) {
		IFilter iFilter = FilterFactory.getSimpleFilter(" cityno = '" + cityno + "'");
		List<?> lst_groupList = this.getList(iFilter);
		if (lst_groupList != null && lst_groupList.size() > 0) {
			DistCityCode distCityCode = (DistCityCode) lst_groupList.get(0);
			return distCityCode;
		} else {
			return null;
		}
	}

	/**
	 * 取得全部城市
	 * 
	 * @return
	 */
	@Override
	public List<DistCityCode> queryAllDistCityList() {
		List<DistCityCode> lst_groupList = (List<DistCityCode>) this.getList();
		return lst_groupList;
	}

	public DistCityCode queryDistCityCodeByCode(String code) {
		if (code != null && code.length() > 0) {
			code = code.trim().toUpperCase();
		}
		IFilter iFilter = FilterFactory.getSimpleFilter(" citycode = '" + code + "'");
		List<?> lst_groupList = this.getList(iFilter);
		if (lst_groupList != null && lst_groupList.size() > 0) {
			DistCityCode distCityCode = (DistCityCode) lst_groupList.get(0);
			return distCityCode;
		} else {
			return null;
		}
	}

	/**
	 * 根据ID查找一条城市信息
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public DistCityCode queryDistCityCodeByCode(Long id) {
		DistCityCodeManager distCityCodeManager = (DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");
		IFilter filter = FilterFactory.getSimpleFilter("id", id);
		List<DistCityCode> list = (List<DistCityCode>) distCityCodeManager.getList(filter);
		if (list.size() > 0 && list != null) {
			return list.get(0);
		} else {
			return null;
		}
	}

}
