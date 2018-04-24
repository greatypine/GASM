package com.cnpc.pms.personal.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONObject;
import org.springframework.util.FileCopyUtils;

import com.cnpc.pms.base.entity.DataEntity;
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
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.dynamic.manager.DynamicManager;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.dao.StoreKeeperDao;
import com.cnpc.pms.personal.entity.CityHumanresources;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreKeeper;
import com.cnpc.pms.personal.entity.StoreKeeperChange;
import com.cnpc.pms.personal.entity.SyncDataLog;
import com.cnpc.pms.personal.manager.CityHumanresourcesManager;
import com.cnpc.pms.personal.manager.StoreDynamicManager;
import com.cnpc.pms.personal.manager.StoreKeeperChangeManager;
import com.cnpc.pms.personal.manager.StoreKeeperManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.SyncDataLogManager;
import com.cnpc.pms.utils.ChineseToEnglish;
import com.cnpc.pms.utils.MD5Utils;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.RegExpUtil;
import com.cnpc.pms.utils.ValueUtil;

@SuppressWarnings("all")
public class StoreKeeperManagerImpl extends BizBaseCommonManager implements StoreKeeperManager {

	PropertiesValueUtil propertiesValueUtil = null;

	CellStyle cellStyle_common = null;

	/**
	 * 验证是否存在该店长
	 */
	@Override
	public StoreKeeper queryStoreKeeperByPhone(String phone) {
		IFilter iFilter = FilterFactory.getSimpleFilter("phone", phone);
		List<?> lst_groupList = this.getList(iFilter);
		if (lst_groupList != null && lst_groupList.size() > 0) {
			return (StoreKeeper) lst_groupList.get(0);
		}
		return null;
	}

	/**
	 * 根据姓名和电话查询是否存在该店长
	 */
	@Override
	public StoreKeeper queryStoreKeeperByNamePhone(String name, String phone) {
		IFilter iFilter = FilterFactory.getSimpleFilter("phone", phone)
				.appendAnd(FilterFactory.getSimpleFilter("name", name));
		List<?> lst_groupList = this.getList(iFilter);
		if (lst_groupList != null && lst_groupList.size() > 0) {
			return (StoreKeeper) lst_groupList.get(0);
		}
		return null;
	}

	/**
	 * 保存店长信息
	 */
	// 根据取得的最大员工编号值，生成新员工号
	private String getNewEmployeeNo(String maxEmployeeNo) {
		if (maxEmployeeNo != null && maxEmployeeNo.length() > 0) {
			String tmpEmpNo = maxEmployeeNo.substring(maxEmployeeNo.length() - 5, maxEmployeeNo.length());
			String tmpMaxNo = (Integer.parseInt(tmpEmpNo) + 1) + "";
			// 补零
			for (int i = 0; i < 6; i++) {
				if (tmpMaxNo.length() < 5) {
					tmpMaxNo = "0" + tmpMaxNo;
				} else {
					break;
				}
			}
			return tmpMaxNo;
		} else {
			return "00001";
		}
	}

	/**
	 * 新增店长 运营经理的方法
	 */
	@Override
	public void saveStoreKeeper(StoreKeeper storeKeeper) {
		// 取得最大店长管理编号
		StoreKeeperDao storeKeeperDao = (StoreKeeperDao) SpringHelper.getBean(StoreKeeperDao.class.getName());
		// 两种角色 SK店长 | RM 运营经理
		if (storeKeeper.getZw() != null && storeKeeper.getZw().equals("SK")) {
			// 店长
			String maxSkEmployeeNo = storeKeeperDao.queryMaxNo(storeKeeper.getZw());
			String newEmpNo = storeKeeper.getZw() + getNewEmployeeNo(maxSkEmployeeNo);
			storeKeeper.setEmployee_no(newEmpNo);
			storeKeeper.setZw("店长");
		} else {
			// 运营经理
			String maxRmEmployeeNo = storeKeeperDao.queryMaxNo(storeKeeper.getZw());
			String newEmpNo = storeKeeper.getZw() + getNewEmployeeNo(maxRmEmployeeNo);
			storeKeeper.setEmployee_no(newEmpNo);
			storeKeeper.setZw("运营经理");
			storeKeeper.setRemark(storeKeeper.getRegion());
		}

		preSaveObject(storeKeeper);
		saveObject(storeKeeper);
		// 添加系统
		if (storeKeeper.getStore_ids() != null && storeKeeper.getStore_ids().length() > 0
				&& storeKeeper.getHumanstatus() == 1) {
			User user = addBizbaseUser(storeKeeper);
			if (user.getId() != null) {
				// 回写到t_store表中的skid或rmid
				String storeids = storeKeeper.getStore_ids();
				while (true) {
					if (storeids.startsWith(",")) {
						storeids = storeids.substring(1, storeids.length());
					}
					if (storeids.endsWith(",")) {
						storeids = storeids.substring(0, storeids.length() - 1);
					} else {
						break;
					}
				}
				StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
				if (storeKeeper.getZw().equals("运营经理")) {
					storeManager.updateStorermid(storeids, user.getId());
				} else {
					storeManager.updateStoreskid(storeids, user.getId());
				}
			}

			// 同步店长数据
			try {
				syncStoreKeeper(storeKeeper);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public User addBizbaseUser(StoreKeeper storeKeeper) {
		// 系统表 创建一条新用户
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		User user = new User();

		user.setName(storeKeeper.getName());
		user.setCode(ChineseToEnglish.getPingYin(storeKeeper.getName()));
		user.setDisabledFlag(1);
		user.setDoctype(0);
		user.setEmail("dz@123.com");
		user.setEmployeeId(storeKeeper.getEmployee_no());
		user.setEnablestate(1);
		user.setPassword("e10adc3949ba59abbe56e057f20f883e"); // 123456
		user.setPk_org(Long.parseLong("40284"));
		user.setMobilephone(storeKeeper.getPhone());

		UserGroupManager u = (UserGroupManager) SpringHelper.getBean("userGroupManager");
		String groupzw = storeKeeper.getZw();
		if (groupzw.equals("服务专员") || groupzw.equals("综合专员")) {
			groupzw = "综合管理";
		}

		UserGroup userGroup = null;
		if (groupzw != null && groupzw.equals("综合管理")) {
			IFilter iFilter = FilterFactory.getSimpleFilter("code", "ZHGLY");
			List<?> lst_groupList = u.getList(iFilter);
			userGroup = (UserGroup) lst_groupList.get(0);
		} else {
			IFilter iFilter = FilterFactory.getSimpleFilter(" name like '%" + groupzw + "%' AND name not LIKE '%副店长%'");
			List<?> lst_groupList = u.getList(iFilter);
			userGroup = (UserGroup) lst_groupList.get(0);
		}

		user.setZw(storeKeeper.getZw());
		user.setUsergroup(userGroup);

		user.setLogicDel(0);
		if (storeKeeper.getStore_ids() != null) {
			String storeids = "";
			if (storeKeeper.getStore_ids().startsWith(",")) {
				storeids = storeKeeper.getStore_ids().substring(1, storeKeeper.getStore_ids().length());
			} else {
				storeids = storeKeeper.getStore_ids();
			}
			user.setStore_id(Long.parseLong(storeids.split(",")[0]));
		} else {
			user.setStore_id(Long.parseLong("99999"));
		}

		preSaveObject(user);
		userManager.saveObject(user);

		// 查询ID
		return userManager.findEmployeeByEmployeeNo(storeKeeper.getEmployee_no());
	}

	@Override
	public StoreKeeper updateStoreKeeper(StoreKeeper storeKeeper) {
		StoreKeeper sKeeper = (StoreKeeper) this.getObject(storeKeeper.getId());
		if (sKeeper.getHumanstatus().equals(Long.parseLong("2"))) {
			sKeeper.setLeavereason(storeKeeper.getLeavereason());// 离职原因
			sKeeper.setLeavedate(storeKeeper.getLeavedate());// 离职日期

			preSaveObject(sKeeper);
			saveObject(sKeeper);
			CityHumanresourcesManager cityHumanresourcesManager = (CityHumanresourcesManager) SpringHelper
					.getBean("cityHumanresourcesManager");
			CityHumanresources cityHumanresources = cityHumanresourcesManager
					.queryCityHumanresourcesByEmployeeNo(sKeeper.getEmployee_no());
			if (cityHumanresources != null) {
				cityHumanresources.setLeavereason(sKeeper.getLeavereason());
				cityHumanresources.setLeavedate(sKeeper.getLeavedate());
				cityHumanresources.setHumanstatus(Long.parseLong("2"));
				preSaveObject(cityHumanresources);
				cityHumanresourcesManager.saveObject(cityHumanresources);
			}

			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			User user = userManager.findEmployeeByEmployeeNo(storeKeeper.getEmployee_no());
			if (user != null) {
				user.setDisabledFlag(0);
				preSaveObject(user);
				userManager.saveObject(user);
			}
			return sKeeper;
		} else {

			if (storeKeeper.getHumanstatus().equals(Long.parseLong("2"))) {

				sKeeper.setHumanstatus(Long.parseLong("2"));
				sKeeper.setLeavereason(storeKeeper.getLeavereason());// 离职原因
				sKeeper.setLeavedate(storeKeeper.getLeavedate());// 离职日期
				preSaveObject(sKeeper);
				saveObject(sKeeper);

				CityHumanresourcesManager cityHumanresourcesManager = (CityHumanresourcesManager) SpringHelper
						.getBean("cityHumanresourcesManager");
				CityHumanresources cityHumanresources = cityHumanresourcesManager
						.queryCityHumanresourcesByEmployeeNo(sKeeper.getEmployee_no());
				if (cityHumanresources != null) {
					cityHumanresources.setLeavereason(sKeeper.getLeavereason());
					cityHumanresources.setLeavedate(sKeeper.getLeavedate());
					cityHumanresources.setHumanstatus(Long.parseLong("2"));
					preSaveObject(cityHumanresources);
					cityHumanresourcesManager.saveObject(cityHumanresources);
				}

				UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
				User user = userManager.findEmployeeByEmployeeNo(storeKeeper.getEmployee_no());
				if (user != null) {
					user.setDisabledFlag(0);
					preSaveObject(user);
					userManager.saveObject(user);
				}
				return sKeeper;
			}

			// 店长异动记录
			StoreKeeperChangeRecord(storeKeeper, sKeeper);

			sKeeper.setName(storeKeeper.getName());
			sKeeper.setPhone(storeKeeper.getPhone());
			sKeeper.setZw(storeKeeper.getZw().equals("SK") ? "店长" : "运营经理");
			sKeeper.setHumanstatus(storeKeeper.getHumanstatus());
			sKeeper.setCitySelect(storeKeeper.getCitySelect());
			sKeeper.setStore_ids(storeKeeper.getStore_ids());
			sKeeper.setStorenames(storeKeeper.getStorenames());

			sKeeper.setLeavedate(storeKeeper.getLeavedate());
			sKeeper.setLeavercvlistdate(storeKeeper.getLeavercvlistdate());
			sKeeper.setLeavereason(storeKeeper.getLeavereason());
			sKeeper.setLeavetype(storeKeeper.getLeavetype());
			// start 2017-09-08 by litianyu
			// sKeeper.setStorenames(storeKeeper.getStorenames());
			// end 2017-09-08 by litianyu
			sKeeper.setRemark(storeKeeper.getRegion());

			preSaveObject(sKeeper);
			saveObject(sKeeper);

			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			User user = userManager.findEmployeeByEmployeeNo(sKeeper.getEmployee_no());

			if (user == null) {
				if (sKeeper.getHumanstatus() == 1) {
					user = addBizbaseUser(sKeeper);
				}
			}

			if (sKeeper.getHumanstatus() == 1) {
				if (user.getId() != null) {
					// 回写到t_store表中的skid或rmid
					String storeids = sKeeper.getStore_ids();
					while (true) {
						if (storeids.startsWith(",")) {
							storeids = storeids.substring(1, storeids.length());
						}
						if (storeids.endsWith(",")) {
							storeids = storeids.substring(0, storeids.length() - 1);
						} else {
							break;
						}
					}
					StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
					StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper
							.getBean("storeDynamicManager");
					if (sKeeper.getZw().equals("运营经理")) {
						storeDynamicManager.updateStorermid(storeids, user.getId());
						storeManager.updateStorermid(storeids, user.getId());
					} else {
						storeDynamicManager.updateStoreskid(storeids, user.getId());
						storeManager.updateStoreskid(storeids, user.getId());
						// 按门店修改用户表
						String[] strs = storeids.split(",");
						for (String store_idstr : strs) {
							if (store_idstr != null && store_idstr.length() > 0) {
								storeManager.updateUserStoreId(Long.parseLong(store_idstr));
							}
						}
					}
					user.setDisabledFlag(1);
					user.setName(sKeeper.getName());
					user.setPhone(sKeeper.getPhone());
					user.setMobilephone(sKeeper.getPhone());
					user.setStore_id(Long.parseLong(storeids.split(",")[0]));
					user.setUsergroup(getUserGroup(sKeeper.getZw()));
					user.setZw(sKeeper.getZw());
					preSaveObject(user);
					userManager.saveObject(user);
				}
			} else {
				// 更改系统表 不允许登录
				if (user != null) {
					user.setDisabledFlag(0);
					user.setName(sKeeper.getName());
					user.setPhone(sKeeper.getPhone());
					user.setUsergroup(getUserGroup(sKeeper.getZw()));
					preSaveObject(user);
					userManager.saveObject(user);
				}
			}
		}

		// 同步店长信息
		try {
			syncStoreKeeper(sKeeper);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sKeeper;

	}

	// 同步人员信息
	public String syncStoreKeeper(StoreKeeper storeKeeper) {
		String rt = "";
		DynamicManager dynamicManager = (DynamicManager) SpringHelper.getBean("dynamicManager");
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		SyncDataLogManager syncDataLogManager = (SyncDataLogManager) SpringHelper.getBean("syncDataLogManager");
		if (storeKeeper.getStore_ids() != null && storeKeeper.getStore_ids().length() > 1) {
			String storeids = storeKeeper.getStore_ids();

			String[] ids = storeids.split(",");
			List<String> existStore = new ArrayList<String>();
			for (String id : ids) {
				if (id != null && id.length() > 0) {
					existStore.add(id);
				}
			}

			if (existStore != null && existStore.size() > 1) {
				// 如果超过2个店 先查询 店 是否之前同步过
				// 根据员工号查询 是否之前同步过
				IFilter iFilter = FilterFactory
						.getSimpleFilter("employeeno like '" + storeKeeper.getEmployee_no() + "%'");
				List<SyncDataLog> lstDataLogs = (List<SyncDataLog>) syncDataLogManager.getList(iFilter);
				if (lstDataLogs != null && lstDataLogs.size() > 0) {
					// 之前同步过 判断哪些是未同步的门店（即新增的门店，只同步新增的）。
					List<String> newStoreIds = new ArrayList<String>();
					int j = 0;
					for (int i = 0; i < existStore.size(); i++) {// 循环页面选择的门店
						Store store = (Store) storeManager.getObject(Long.parseLong(existStore.get(i)));
						boolean isbreak = false;
						for (SyncDataLog syncDataLog : lstDataLogs) {// 同步的门店
							if (store != null && store.getStoreno().equals(syncDataLog.getStorecode())) {
								isbreak = true;// 如果之前同步过 则外循环中的数据不操作。
								break;
							}
						}
						if (!isbreak) {// 如果为未同步的店 则同步 编号+1
							j++;
							if (store != null && store.getStoreno() != null && storeKeeper.getEmployee_no() != null
									&& storeKeeper.getName() != null && storeKeeper.getPhone() != null) {
								String employeeno = storeKeeper.getEmployee_no();
								employeeno = employeeno + "-" + (lstDataLogs.size() + j - 1);// 因为零的时候不显示
																								// 所以要-1

								boolean sync = true;
								if (store.getStoreno().contains("W")) {// 未知门店
																		// 不同步
									sync = false;
								}
								if (store.getStoreno().contains("C")) {// 仓店 不同步
									sync = false;
								}
								if (store.getName().contains("储备店")) {// 储备店 不同步
									sync = false;
								}
								if (store.getName().contains("办公室")) {// 办公室 不同步
									sync = false;
								}
								if (sync) {
									JSONObject jsonObject = dynamicManager.insertNewEmployee(store.getStoreno(),
											employeeno, storeKeeper.getName(), storeKeeper.getPhone());
									rt = jsonObject == null ? "" : jsonObject.toString();
								}

							}
						}

					}

				} else {
					// 之前没同步过
					for (int i = 0; i < existStore.size(); i++) {
						Store store = (Store) storeManager.getObject(Long.parseLong(existStore.get(i)));
						if (store != null && store.getStoreno() != null && storeKeeper.getEmployee_no() != null
								&& storeKeeper.getName() != null && storeKeeper.getPhone() != null) {
							String employeeno = storeKeeper.getEmployee_no();
							if (i > 0) {// 如果超过二个 则后边加 -1 -2 -3 以此类推
								employeeno = employeeno + "-" + i;
							}

							boolean sync = true;
							if (store.getStoreno().contains("W")) {// 未知门店 不同步
								sync = false;
							}
							if (store.getStoreno().contains("C")) {// 仓店 不同步
								sync = false;
							}
							if (store.getName().contains("储备店")) {// 储备店 不同步
								sync = false;
							}
							if (store.getName().contains("办公室")) {// 办公室 不同步
								sync = false;
							}
							if (sync) {
								JSONObject jsonObject = dynamicManager.insertNewEmployee(store.getStoreno(), employeeno,
										storeKeeper.getName(), storeKeeper.getPhone());
								rt = jsonObject == null ? "" : jsonObject.toString();
							}

						}

					}
				}

			} else if (existStore != null && existStore.size() == 1) {
				// 如果只有一个
				Store store = (Store) storeManager.getObject(Long.parseLong(existStore.get(0)));

				// 根据员工号查询 是否之前同步过
				IFilter oneiFilter = FilterFactory
						.getSimpleFilter("employeeno like '" + storeKeeper.getEmployee_no() + "%'");
				List<SyncDataLog> oneDataLogs = (List<SyncDataLog>) syncDataLogManager.getList(oneiFilter);
				if (oneDataLogs == null || oneDataLogs.size() < 1) {
					if (store != null && store.getStoreno() != null && storeKeeper.getEmployee_no() != null
							&& storeKeeper.getName() != null && storeKeeper.getPhone() != null) {

						boolean sync = true;
						if (store.getStoreno().contains("W")) {// 未知门店 不同步
							sync = false;
						}
						if (store.getStoreno().contains("C")) {// 仓店 不同步
							sync = false;
						}
						if (store.getName().contains("储备店")) {// 储备店 不同步
							sync = false;
						}
						if (store.getName().contains("办公室")) {// 办公室 不同步
							sync = false;
						}
						if (sync) {
							JSONObject jsonObject = dynamicManager.insertNewEmployee(store.getStoreno(),
									storeKeeper.getEmployee_no(), storeKeeper.getName(), storeKeeper.getPhone());
							rt = jsonObject == null ? "" : jsonObject.toString();
						}

					}
				}

			}
			System.out.println(storeids);

		}

		// JSONObject jsonObject = dynamicManager.insertNewTest("测试测试",
		// "2017-12-27");
		/*
		 * if(store!=null&&store.getStoreno()!=null&&hr.getEmployee_no()!=null&&
		 * hr.getName()!=null&&hr.getPhone()!=null){ JSONObject jsonObject =
		 * dynamicManager.insertNewEmployee(store.getStoreno(),
		 * hr.getEmployee_no(), hr.getName(), hr.getPhone());
		 * rt=jsonObject.toString(); }
		 */
		return rt;
	}

	public UserGroup getUserGroup(String zw) {
		UserGroupManager u = (UserGroupManager) SpringHelper.getBean("userGroupManager");
		if (zw.equals("服务专员") || zw.equals("综合专员")) {
			zw = "综合管理";
		}

		UserGroup userGroup = null;
		if (zw != null && zw.equals("综合管理")) {
			IFilter iFilter = FilterFactory.getSimpleFilter("code", "ZHGLY");
			List<?> lst_groupList = u.getList(iFilter);
			userGroup = (UserGroup) lst_groupList.get(0);
		} else {
			IFilter iFilter = FilterFactory.getSimpleFilter(" name like '%" + zw + "%' AND name not LIKE '%副店长%'");
			List<?> lst_groupList = u.getList(iFilter);
			userGroup = (UserGroup) lst_groupList.get(0);
		}

		return userGroup;
	}

	private void changeSystemUser(Humanresources hr, Long humanstatus) {
		try {
			// 进行判断 如果是正式，则同步系统用户
			if (humanstatus != null) {
				UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
				UserGroupManager userGroupManager = (UserGroupManager) SpringHelper.getBean("userGroupManager");
				User user = userManager.findEmployeeByEmployeeNo(hr.getEmployee_no());
				if (humanstatus.equals(Long.parseLong("0")) || humanstatus.equals(Long.parseLong("2"))) {
					// 查询系统表。如果存在 则更改状态，不允许登录 如果不存在。则不做任何操作。
					if (user != null && user.getEmployeeId() != null) {
						user.setDisabledFlag(0);
						preSaveObject(user);
						userManager.saveObject(user);
					}
				} else {
					// 查询系统表。如果存在 则更改状态,允许登录 如果不存在。则创建一条。
					if (user != null && user.getEmployeeId() != null) {
						user.setDisabledFlag(1);
						preSaveObject(user);
						userManager.saveObject(user);
					} else {
						// 系统表 创建一条新用户
						user = new User();

						user.setName(hr.getName());
						user.setCode(ChineseToEnglish.getPingYin(hr.getName()));
						user.setDisabledFlag(1);
						user.setDoctype(0);
						user.setEmail("123@123.com");
						user.setEmployeeId(hr.getEmployee_no());
						user.setEnablestate(1);
						user.setPassword("e10adc3949ba59abbe56e057f20f883e"); // 123456
						user.setPk_org(Long.parseLong("40284"));
						user.setMobilephone(hr.getPhone());

						UserGroupManager u = (UserGroupManager) SpringHelper.getBean("userGroupManager");
						String groupzw = hr.getZw();
						if (groupzw.equals("服务专员") || groupzw.equals("综合专员")) {
							groupzw = "综合管理";
						}

						UserGroup userGroup = null;
						if (groupzw != null && groupzw.equals("综合管理")) {
							IFilter iFilter = FilterFactory.getSimpleFilter("code", "ZHGLY");
							List<?> lst_groupList = u.getList(iFilter);
							userGroup = (UserGroup) lst_groupList.get(0);
						} else {
							IFilter iFilter = FilterFactory.getSimpleFilter(" name like '%" + groupzw + "%'");
							List<?> lst_groupList = u.getList(iFilter);
							userGroup = (UserGroup) lst_groupList.get(0);
						}

						user.setZw(hr.getZw());
						user.setUsergroup(userGroup);

						user.setLogicDel(0);
						user.setStore_id(hr.getStore_id());
						preSaveObject(user);
						userManager.saveObject(user);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void preSaveObject(Object o) {
		if (o instanceof DataEntity) {
			User sessionUser = null;
			if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
			}
			DataEntity dataEntity = (DataEntity) o;
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			// insert处理时添加创建人和创建时间
			if (null == dataEntity.getCreate_time()) {
				dataEntity.setCreate_time(sdate);
				if (null != sessionUser) {
					dataEntity.setCreate_user(sessionUser.getCode());
					dataEntity.setCreate_user_id(sessionUser.getId());
				}
			}
			dataEntity.setUpdate_time(sdate);
			if (null != sessionUser) {
				dataEntity.setUpdate_user(sessionUser.getCode());
				dataEntity.setUpdate_user_id(sessionUser.getId());
			}
		}
	}

	@Override
	public StoreKeeper queryStoreKeeperById(Long id) {
		StoreKeeper storeKeeper = (StoreKeeper) this.getObject(id);
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		// 查询的时候 去store表中查询当前店长或是运营经理 所属的门店
		String t_storenames = "";
		String t_storeids = "";
		if (storeKeeper.getEmployee_no() != null && storeKeeper.getEmployee_no().length() > 0) {
			if (storeKeeper.getZw().equals("店长")) {
				User u = userManager.findEmployeeByEmployeeNo(storeKeeper.getEmployee_no());
				if (u != null) {
					List<Store> stores = storeManager.findStoreByskid(u.getId());
					if (stores != null && stores.size() > 0) {
						for (Store s : stores) {
							t_storenames += "," + s.getName() + ",";
							t_storeids += "," + s.getStore_id() + ",";
						}
					}
				}
			} else {
				User u = userManager.findEmployeeByEmployeeNo(storeKeeper.getEmployee_no());
				if (u != null) {
					List<Store> stores = storeManager.findStoreByrmid(u.getId());
					if (stores != null && stores.size() > 0) {
						for (Store s : stores) {
							t_storenames += "," + s.getName() + ",";
							t_storeids += "," + s.getStore_id() + ",";
						}
					}
				}
			}
		}
		storeKeeper.setStorenames(t_storenames);
		storeKeeper.setStore_ids(t_storeids);

		return storeKeeper;
	}

	@Override
	public Map<String, Object> querystorekeeperList(QueryConditions condition) {
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		/*
		 * UserDTO userDTO = manager.getCurrentUserDTO(); Long store_id =
		 * userDTO.getStore_id();
		 */
		Map<String, Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		String name = null;
		String storenames = null;
		String humanstatus = null;
		String citySelect = null;
		for (Map<String, Object> map : condition.getConditions()) {
			if ("name".equals(map.get("key")) && map.get("value") != null) {// 查询条件
				name = map.get("value").toString();
			}
			if ("storenames".equals(map.get("key")) && map.get("value") != null) {// 查询条件
				storenames = map.get("value").toString();
			}
			if ("humanstatus".equals(map.get("key")) && map.get("value") != null) {// 查询条件
				humanstatus = map.get("value").toString();
			}
			if ("citySelect".equals(map.get("key")) && map.get("value") != null) {// 查询条件
				citySelect = map.get("value").toString();
			}

		}
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("id", ISort.DESC));
		StringBuffer sbfCondition = new StringBuffer();

		// 取得当前登录人 所管理城市
		String cityssql = "";
		List<DistCity> distCityList = userManager.getCurrentUserCity();
		if (distCityList != null && distCityList.size() > 0) {
			for (DistCity d : distCityList) {
				cityssql += "'" + d.getCityname() + "',";
			}
			cityssql = cityssql.substring(0, cityssql.length() - 1);
		}

		sbfCondition.append(" status!=1 and humanstatus!=0 ");

		if (cityssql != "" && cityssql.length() > 0) {
			sbfCondition.append(" and citySelect in (" + cityssql + ")");
		} else {
			sbfCondition.append(" and 0=1 ");
		}

		if (name != null) {
			sbfCondition.append(" and name like '%" + name + "%'");
		}
		if (storenames != null && storenames.length() > 0) {
			List<Store> stores = storeManager.findStoreListByName(storenames);
			List<Long> storekeepers = null;
			List<String> skEmloyeeIds = null;
			// 查门店表中的 所有的 用户ID
			if (stores != null && stores.size() > 0) {
				storekeepers = new ArrayList<Long>();
				for (Store s : stores) {
					if (s.getSkid() != null) {
						storekeepers.add(s.getSkid());
					}
					if (s.getRmid() != null) {
						storekeepers.add(s.getRmid());
					}
				}
			}
			// 去user表中查询所有的店长
			if (storekeepers != null) {
				skEmloyeeIds = new ArrayList<String>();
				for (Long skid : storekeepers) {
					User u = (User) userManager.getObject(skid);
					if (u != null) {
						skEmloyeeIds.add(u.getEmployeeId());
					}

				}
			}
			// 如果有店长的员工号
			if (skEmloyeeIds != null && skEmloyeeIds.size() > 0) {
				String employees = "";
				for (String employee_no : skEmloyeeIds) {
					employees += "'" + employee_no + "',";
				}
				if (employees != "") {
					sbfCondition.append(" and employee_no in(" + employees.substring(0, employees.length() - 1) + ")");
				}
			} else {
				sbfCondition.append(" and 1=0 ");
			}

		}
		if (humanstatus != null) {
			sbfCondition.append(" and humanstatus = '" + humanstatus + "'");
		}
		if (citySelect != null) {
			sbfCondition.append(" and citySelect like '" + citySelect + "'");
		}

		sbfCondition.append(" order by id DESC ");

		IFilter iFilter = FilterFactory.getSimpleFilter(sbfCondition.toString());
		fsp.setPage(pageInfo);
		fsp.setUserFilter(iFilter);
		List<StoreKeeper> lst_data = (List<StoreKeeper>) this.getList(fsp);
		List<StoreKeeper> ret_data = new ArrayList<StoreKeeper>();
		if (lst_data != null && lst_data.size() > 0) {
			// 处理门店 动态查找。
			for (StoreKeeper sk : lst_data) {
				String t_storename = "";
				if (sk.getEmployee_no() != null && sk.getEmployee_no().length() > 0) {
					if (sk.getZw().equals("店长")) {
						User u = userManager.findEmployeeByEmployeeNo(sk.getEmployee_no());
						if (u != null) {
							List<Store> stores = storeManager.findStoreByskid(u.getId());
							if (stores != null && stores.size() > 0) {
								for (Store s : stores) {
									t_storename += s.getName() + ",";
								}
							}
						}
					} else {
						User u = userManager.findEmployeeByEmployeeNo(sk.getEmployee_no());
						if (u != null) {
							List<Store> stores = storeManager.findStoreByrmid(u.getId());
							if (stores != null && stores.size() > 0) {
								for (Store s : stores) {
									t_storename += s.getName() + ",";
								}
							}
						}
					}
				}

				sk.setStorenames(t_storename);
				ret_data.add(sk);
			}

		}
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", ret_data);
		return returnMap;
	}

	@Override
	public List<Map<String, Object>> getShopManagerList(String cityname) {
		String zwValue = "店长";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<?> lst_StoreKeeper_data = getList(FilterFactory
				.getSimpleFilter("zw = '" + zwValue + "' AND humanstatus=1 and citySelect like '%" + cityname + "%'"));
		if (lst_StoreKeeper_data != null && lst_StoreKeeper_data.size() > 0) {
			StoreKeeper storeKeeper = null;
			Map<String, Object> map = null;
			for (Object object : lst_StoreKeeper_data) {
				storeKeeper = (StoreKeeper) object;
				map = new HashMap<String, Object>();
				map.put("user_id", storeKeeper.getId());
				map.put("user_name", storeKeeper.getName());
				list.add(map);
			}
			return list;
		}
		return null;
	}

	@Override
	public StoreKeeper findStoreKeeperByEmployeeId(String Employeeid) {
		List<?> lst_StoreKeeper = getList(FilterFactory.getSimpleFilter("employee_no", Employeeid));
		if (lst_StoreKeeper != null && lst_StoreKeeper.size() > 0) {
			return (StoreKeeper) lst_StoreKeeper.get(0);
		}
		return null;
	}

	/**
	 * 修改门店店长 的密码
	 */
	@Override
	public StoreKeeper updatestorekeeperpassword(String storepassword, String confstorepassword, String employee_no) {
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		User user = userManager.findEmployeeByEmployeeNo(employee_no);
		StoreKeeper storeKeeper = new StoreKeeper();
		if (user != null && user.getEmployeeId() != null) {
			storeKeeper.setEmployee_no(user.getEmployeeId());
			// 修改user密码
			user.setPassword(MD5Utils.getMD5Str(confstorepassword));
			Result r = userManager.updatePwd(user);
			return storeKeeper;
		} else {
			return null;
		}
	}

	/**
	 * 店长异动记录
	 * 
	 * @param storeKeeper
	 *            新记录
	 * @param sKeeper
	 *            旧记录
	 */
	public void StoreKeeperChangeRecord(StoreKeeper storeKeeper, StoreKeeper sKeeper) {

		// 判断保存的值 是否有变化,如果有变化则保存到storekeeperchange表中。
		StoreKeeperChangeManager changeManager = (StoreKeeperChangeManager) SpringHelper
				.getBean("storeKeeperChangeManager");
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		StoreKeeperChange storeKeeperChange = new StoreKeeperChange();

		boolean flag = false;
		String oldZw = sKeeper.getZw();
		String newZw = storeKeeper.getZw().equals("SK") ? "店长" : "运营经理";
		String oldcitySelect = sKeeper.getCitySelect();
		String newcitySelect = storeKeeper.getCitySelect();
		Long oldHumanstatus = sKeeper.getHumanstatus();
		Long newHumanStatus = storeKeeper.getHumanstatus();
		// 实时获取原来店长所管理的门店列表
		String employeeno = sKeeper.getEmployee_no();
		// 员工号获取对应的Id
		User user = userManager.findEmployeeByEmployeeNo(employeeno);
		if (user != null) {
			Long userid = user.getId();
			List<Store> oldStore = null;
			String oldStore_ids = "";
			String odlStore_names = "";
			if ("店长".equals(oldZw)) {// 如果是店长则通过skid获取原来门店列表
				oldStore = storeManager.findStoreByskid(userid);
				for (Store store : oldStore) {
					oldStore_ids = oldStore_ids + "," + store.getStore_id().toString();
					odlStore_names = odlStore_names + "," + store.getName().toString();
				}
			} else if ("运营经理".equals(oldZw)) {// 如果是运营经理则通过rmid获取原来门店列表
				oldStore = storeManager.findStoreByrmid(userid);
				for (Store store : oldStore) {
					oldStore_ids = oldStore_ids + "," + store.getStore_id().toString();
					odlStore_names = odlStore_names + "," + store.getName().toString();
				}
			}
			// String oldStore_ids = sKeeper.getStore_ids();
			// 获取当前页面所选择的门店列表
			String newStore_ids = storeKeeper.getStore_ids();
			newStore_ids = newStore_ids == null ? "" : newStore_ids;
			List<String> oldStoreList = RegExpUtil.getNumberGroup(oldStore_ids);
			List<String> newStoreList = RegExpUtil.getNumberGroup(newStore_ids);
			boolean storeflag = RegExpUtil.compare(oldStoreList, newStoreList);
			if (oldZw.equals(newZw) && oldcitySelect.equals(newcitySelect) && oldHumanstatus.equals(newHumanStatus)
					&& storeflag == true) {
				flag = false;
			} else {
				flag = true;
			}
			// 如果有变化 则将变化值插入 t_storekeeper_change变更表中
			if (flag) {
				storeKeeperChange.setZw(sKeeper.getZw());
				storeKeeperChange.setChangezw(storeKeeper.getZw().equals("SK") ? "店长" : "运营经理");
				storeKeeperChange.setCitySelect(sKeeper.getCitySelect());
				storeKeeperChange.setChangecitySelect(storeKeeper.getCitySelect());
				storeKeeperChange.setHumanstatus(sKeeper.getHumanstatus());
				storeKeeperChange.setChangehumanstatus(storeKeeper.getHumanstatus());
				storeKeeperChange.setStore_ids(oldStore_ids);
				storeKeeperChange.setChangestore_ids(storeKeeper.getStore_ids());
				storeKeeperChange.setStorenames(odlStore_names);
				storeKeeperChange.setChangestorenames(storeKeeper.getStorenames());
				storeKeeperChange.setEmployee_no(sKeeper.getEmployee_no());
				storeKeeperChange.setChangeemployee_no(sKeeper.getEmployee_no());
				// 变更时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String changedate = sdf.format(new Date());
				storeKeeperChange.setChangedate(changedate);

				changeManager.saveStoreKeeperChange(storeKeeperChange);
			}
		}
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

	public void setCellValue(Row obj_row, int nCellIndex, Object value) {
		Cell cell = obj_row.createCell(nCellIndex);
		cell.setCellStyle(getCellStyle_common());
		cell.setCellValue(new HSSFRichTextString(value == null ? null : value.toString()));
	}

	public CellStyle getCellStyle_common() {
		return cellStyle_common;
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
	}

	/**
	 * 导出门店经理excel
	 */
	@Override
	public File exportStoreKeeperExcel() throws IOException, Exception {
		String str_file_name = "export_storekeeper_template";
		String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
		// 配置文件中的路径
		String str_filepath = strRootpath
				.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));
		File file_template = new File(str_filepath);

		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("update_time", ISort.ASC));

		// 取得当前登录人 所管理城市
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		StringBuffer sbfCondition = new StringBuffer();
		String cityssql = "";
		List<DistCity> distCityList = userManager.getCurrentUserCity();
		if (distCityList != null && distCityList.size() > 0) {
			for (DistCity d : distCityList) {
				cityssql += "'" + d.getCityname() + "',";
			}
			cityssql = cityssql.substring(0, cityssql.length() - 1);
		}

		if (cityssql != "" && cityssql.length() > 0) {
			sbfCondition.append(" citySelect in (" + cityssql + ")");
		} else {
			sbfCondition.append(" 0=1 ");
		}

		IFilter iFilter = FilterFactory.getSimpleFilter(sbfCondition.toString());
		fsp.setUserFilter(iFilter);

		List<StoreKeeper> lst_storeKeeperList = (List<StoreKeeper>) this.getList(fsp);

		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_newfilepath = str_file_dir_path + "storekeeper_list.xls";
		File file_new = new File(str_newfilepath);
		if (file_new.exists()) {
			file_new.delete();
		}

		FileCopyUtils.copy(file_template, file_new);
		FileInputStream fis_input_excel = new FileInputStream(file_new);
		FileOutputStream fis_out_excel = null;
		Workbook wb_storekeeperinfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
		try {

			setCellStyle_common(wb_storekeeperinfo);

			Sheet sh_job = wb_storekeeperinfo.getSheetAt(0);
			Sheet sh_quit = wb_storekeeperinfo.getSheetAt(1);
			int nJobIndex = 2;
			int nQuitIndex = 2;
			Map<String, StoreKeeper> map_storeKeepers = new HashMap<String, StoreKeeper>();

			for (StoreKeeper storeKeepers : lst_storeKeeperList) {
				Row obj_row = null;
				map_storeKeepers.put(storeKeepers.getEmployee_no(), storeKeepers);
				int cellIndex = 0;
				if (storeKeepers.getHumanstatus() == 1L) {
					sh_job.createRow(nJobIndex);
					obj_row = sh_job.getRow(nJobIndex);
					setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue((nJobIndex - 1)));// 序号
				} else if (storeKeepers.getHumanstatus() == 2L) {
					sh_quit.createRow(nQuitIndex);
					obj_row = sh_quit.getRow(nQuitIndex);
					setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue((nQuitIndex - 1)));// 序号
					setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeepers.getLeavedate()));// 序号
				} else {
					continue;
				}
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeepers.getCitySelect()));// 城市
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeepers.getEmployee_no()));// 员工号
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeepers.getName()));// 姓名

				// 根据员工号查询所管理门店
				IFilter ufilter = FilterFactory
						.getSimpleFilter(" employeeId = '" + storeKeepers.getEmployee_no() + "'");
				List<User> userList = (List<User>) userManager.getList(ufilter);
				String storenames = "";
				if (userList != null && userList.size() > 0) {
					User user = userList.get(0);
					if (user.getZw() != null && user.getZw().equals("店长")) {
						List<Store> stores = storeManager.findStoreByskid(user.getId());
						for (Store s : stores) {
							storenames += s.getName() + ",";
						}
					} else {
						List<Store> stores = storeManager.findStoreByrmid(user.getId());
						for (Store s : stores) {
							storenames += s.getName() + ",";
						}
					}
					setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storenames));// 门店
				} else {
					setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeepers.getStorenames()));// 门店
				}

				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeepers.getZw()));// 职位

				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeepers.getPhone()));// 本人电话
				if (storeKeepers.getHumanstatus() == 2L) {
					setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeepers.getLeavereason()));// 离职原因
					setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeepers.getLeavetype()));// 离职类型
					nQuitIndex++;
				} else {
					nJobIndex++;
				}
			}

			Sheet sh_change = wb_storekeeperinfo.getSheetAt(2);

			int nChangeIndex = 2;
			fsp = new FSP();
			fsp.setSort(SortFactory.createSort("id", ISort.ASC));
			StoreKeeperChangeManager storeKeeperChangeManager = (StoreKeeperChangeManager) SpringHelper
					.getBean("storeKeeperChangeManager");
			List<StoreKeeperChange> lst_storeKeeperChangeList = (List<StoreKeeperChange>) storeKeeperChangeManager
					.getList(fsp);
			for (StoreKeeperChange storeKeeperChange : lst_storeKeeperChangeList) {
				sh_change.createRow(nChangeIndex);
				Row obj_row = sh_change.getRow(nChangeIndex);
				if (!map_storeKeepers.containsKey(storeKeeperChange.getEmployee_no())) {
					continue;
				}
				int cellIndex = 0;
				StoreKeeper storeKeeper = map_storeKeepers.get(storeKeeperChange.getEmployee_no());
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue((nChangeIndex - 1)));// 序号
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeeperChange.getChangedate()));// 变更日期
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeeper.getEmployee_no()));// 员工号
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeeper.getName()));// 姓名

				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeeperChange.getCitySelect()));// 变更前城市
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeeperChange.getZw()));// 变更前职位
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeeperChange.getStore_ids()));// 变更前门店号
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeeperChange.getStorenames()));// 变更前门店名称
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeeperChange.getHumanstatus()));// 变更前人员状态
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeeperChange.getChangecitySelect()));// 变更后城市
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeeperChange.getChangezw()));// 变更后职位
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeeperChange.getChangestore_ids()));// 变更后门店编号
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeeperChange.getChangestorenames()));// 变更后门店名称
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(storeKeeperChange.getChangehumanstatus()));// 变更后人员状态

				nChangeIndex++;
			}

			fis_out_excel = new FileOutputStream(file_new);
			wb_storekeeperinfo.write(fis_out_excel);

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

}
