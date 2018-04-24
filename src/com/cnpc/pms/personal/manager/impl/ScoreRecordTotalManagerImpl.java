package com.cnpc.pms.personal.manager.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.manager.AttachmentManager;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.messageModel.manager.MessageNewManager;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.DsAbnormalOrder;
import com.cnpc.pms.personal.entity.FlowConfig;
import com.cnpc.pms.personal.entity.FlowDetail;
import com.cnpc.pms.personal.entity.ScoreRecordTotal;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreDocumentInfo;
import com.cnpc.pms.personal.entity.StoreDynamic;
import com.cnpc.pms.personal.entity.WorkInfo;
import com.cnpc.pms.personal.manager.DistCityManager;
import com.cnpc.pms.personal.manager.DsAbnormalOrderManager;
import com.cnpc.pms.personal.manager.FlowConfigManager;
import com.cnpc.pms.personal.manager.FlowDetailManager;
import com.cnpc.pms.personal.manager.ScoreRecordTotalManager;
import com.cnpc.pms.personal.manager.StoreDocumentInfoManager;
import com.cnpc.pms.personal.manager.StoreDynamicManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.WorkInfoManager;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;

public class ScoreRecordTotalManagerImpl extends BizBaseCommonManager implements ScoreRecordTotalManager {

	PropertiesValueUtil propertiesValueUtil = null;

	private String folder_path = null;

	CellStyle cellStyle_common = null;
	CellStyle cellLeftStyle_common = null;
	CellStyle cellStrongCenterStyle_common = null;

	@Override
	public Map<String, Object> queryScoreRecordTotalBySubmit(QueryConditions conditions) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
		FlowDetailManager flowDetailManager = (FlowDetailManager) SpringHelper.getBean("flowDetailManager");

		Map<Long, StoreDynamic> map_store = new HashMap<Long, StoreDynamic>();

		String store_name = null;
		StringBuilder sb_where = new StringBuilder();
		sb_where.append(" 1=1 ");

		// 判断当前登录人是什么角色，如果HR则取得当前所管理城市，如果为运营经理。则取得所有管理门店。
		UserDTO userDTO = userManager.getCurrentUserDTO();
		if (userDTO.getUsergroup().getCode().equals("QYJL")) {// 运营经理
			// 取得所管理门店
			List<Store> stores = storeManager.findStoreByrmid(userDTO.getId());
			if (stores != null && stores.size() > 0) {
				String store_ids = "";
				for (Store s : stores) {
					store_ids += s.getStore_id() + ",";
				}
				store_ids = store_ids.substring(0, store_ids.length() - 1);
				sb_where.append(" and store_id in(" + store_ids + ") ");
				sb_where.append(" and str_work_info_id='绩效打分录入' ");
			} else {
				sb_where.append(" and 0=1 ");
			}
		} else if (userDTO.getUsergroup().getCode().equals("CSMDKQSPJSZ")) {// 线上
																			// MDKQSPJSZ
																			// 本地KQGLJSZ
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
				sb_where.append(" and cityname in (" + cityssql + ")");
				sb_where.append(" and str_work_info_id='绩效打分录入' ");
			} else {
				sb_where.append(" and 0=1 ");
			}
		} else if (userDTO.getUsergroup().getCode().equals("GLY")) {// 管理员组
			sb_where.append(" and 1=1 ");
		} else if (userDTO.getUsergroup().getCode().equals("PTYYZXYCDDZ")) {
			sb_where.append(" and str_work_info_id='异常订单申诉' ");
		} else if (userDTO.getUsergroup().getCode().equals("MDXZSHJSZ")) {// 门店开店审核人
			sb_where.append(" and str_work_info_id='门店选址审核' ");
		} else if (userDTO.getUsergroup().getCode().equals("MDCBSHJSZ")) {// 门店开店审核人
			sb_where.append(" and str_work_info_id='门店筹备审核' ");
		} else {
			sb_where.append(" and 0=1 ");// 如果不是考勤组和运营经理组和 门店审核组
		}

		for (Map<String, Object> condition : conditions.getConditions()) {
			if ("store_name".equals(condition.get("key")) && ValueUtil.checkValue(condition.get("value"))) {
				store_name = condition.get("value").toString();
				// List<Store> storeList =
				// storeManager.findStoreListByName(store_name);
				List<StoreDynamic> storeDynamicList = storeDynamicManager.findStoreDynamicListByName(store_name);
				if (storeDynamicList != null && storeDynamicList.size() > 0) {
					StringBuilder sb_ids = new StringBuilder();
					for (StoreDynamic storeDynamic : storeDynamicList) {
						map_store.put(storeDynamic.getStore_id(), storeDynamic);
						if (sb_ids.length() > 0) {
							sb_ids.append(",'" + storeDynamic.getStore_id() + "'");
						} else {
							sb_ids.append("'" + storeDynamic.getStore_id() + "'");
						}
					}
					sb_where.append(" and store_id in (").append(sb_ids.toString()).append(")");
				} else {
					sb_where.append(" and 0 = 1");
				}
			}

			if ("citySelect".equals(condition.get("key")) && ValueUtil.checkValue(condition.get("value"))) {
				String citySelect = condition.get("value").toString();
				// List<Store> storeSelects =
				// storeManager.findStoreByCityData("'" + citySelect + "'");
				List<StoreDynamic> storeDynamicList = storeDynamicManager
						.findStoreDynamicByCityData("'" + citySelect + "'");
				if (storeDynamicList != null && storeDynamicList.size() > 0) {
					StringBuilder sb_ids = new StringBuilder();
					for (StoreDynamic storeDynamic : storeDynamicList) {
						map_store.put(storeDynamic.getStore_id(), storeDynamic);
						if (sb_ids.length() > 0) {
							sb_ids.append(",'" + storeDynamic.getStore_id() + "'");
						} else {
							sb_ids.append("'" + storeDynamic.getStore_id() + "'");
						}
					}
					sb_where.append(" and store_id in (").append(sb_ids.toString()).append(")");
				} else {
					sb_where.append(" and 0 = 1");
				}

			}

			// 取得当前登录人组 可看哪些审批

			if ("start_date".equals(condition.get("key")) && ValueUtil.checkValue(condition.get("value"))) {
				sb_where.append(" and commit_date >= date_format('").append(condition.get("value").toString())
						.append("','%Y-%m-%d')");
			}

			if ("end_date".equals(condition.get("key")) && ValueUtil.checkValue(condition.get("value"))) {
				sb_where.append(" and commit_date <= date_format('").append(condition.get("value").toString())
						.append("','%Y-%m-%d')");
			}

			if ("work_month".equals(condition.get("key")) && ValueUtil.checkValue(condition.get("value"))) {
				sb_where.append(" and work_month = '").append(condition.get("value").toString()).append("'");
			}

			if ("commit_status".equals(condition.get("key")) && ValueUtil.checkValue(condition.get("value"))) {
				sb_where.append(" and commit_status = '").append(condition.get("value").toString()).append("'");
			}
		}

		FSP fsp = new FSP();
		IFilter filter = FilterFactory.getStringFilter(sb_where.toString());
		fsp.setUserFilter(filter);
		fsp.setPage(conditions.getPageinfo());
		if (userDTO.getUsergroup().getCode().equals("CSMDKQSPJSZ")) {
			fsp.setSort(SortFactory.createSort("commit_status", ISort.ASC).appendSort(SortFactory
					.createSort("remark", ISort.ASC).appendSort(SortFactory.createSort("update_time", ISort.ASC))));
		} else if (userDTO.getUsergroup().getCode().equals("QYJL")) {
			fsp.setSort(SortFactory.createSort("commit_status", ISort.ASC).appendSort(SortFactory
					.createSort("remark", ISort.DESC).appendSort(SortFactory.createSort("update_time", ISort.ASC))));
		} else {
			fsp.setSort(SortFactory.createSort("commit_status", ISort.ASC).appendSort(SortFactory
					.createSort("remark", ISort.ASC).appendSort(SortFactory.createSort("update_time", ISort.ASC))));
		}
		List<ScoreRecordTotal> lst_total_data = (List<ScoreRecordTotal>) this.getList(filter);
		if (lst_total_data != null && lst_total_data.size() > 0) {
			conditions.getPageinfo().setTotalRecords(lst_total_data.size());
		}
		List<ScoreRecordTotal> lst_data = (List<ScoreRecordTotal>) this.getList(fsp);
		if (lst_data != null && lst_data.size() > 0) {
			for (ScoreRecordTotal total : lst_data) {
				if (!map_store.containsKey(total.getStore_id())) {
					// Store store =
					// storeManager.findStore(total.getStore_id());
					StoreDynamic storeDynamic = storeDynamicManager.findStoreDynamic(total.getStore_id());
					if (storeDynamic != null) {
						map_store.put(storeDynamic.getStore_id(), storeDynamic);
					}
				}
				String cityname = total.getCityname();
				if (map_store.get(total.getStore_id()) != null) {
					total.setStore_name(cityname + "-" + map_store.get(total.getStore_id()).getName());
				}
				total.setStr_commit_date(dateFormat.format(total.getCommit_date()));
				if (total.getCommit_status() == 0) {
					total.setStr_commit_status("已保存");
				} else if (total.getCommit_status() == 1) {
					total.setStr_commit_status("待审批");
				} else if (total.getCommit_status() == 2) {
					total.setStr_commit_status("已退回");
				} else if (total.getCommit_status() == 3) {
					total.setStr_commit_status("已通过");
				}

				WorkInfo workInfo = (WorkInfo) workInfoManager.getObject(total.getWork_info_id());
				total.setStr_work_info_id(workInfo.getWork_type());

				// 添加当前审批人显示
				// 根据流程表里。查询审批进度。根据work_info_id查询，如果表中有一条记录
				// 则为第一审批人，如果有两条，则为第二审批人，三条 第三审批人
				List<?> lst_flowList = flowDetailManager.queryFlowDetailByWorkId(total.getWork_info_id());
				FlowConfigManager flowConfigManager = (FlowConfigManager) SpringHelper.getBean("flowConfigManager");
				DistCityManager distCityManager = (DistCityManager) SpringHelper.getBean("distCityManager");
				UserGroupManager userGroupManager = (UserGroupManager) SpringHelper.getBean("userGroupManager");
				if (lst_flowList != null && lst_flowList.size() == 1) {
					// 显示第一审批人

					IFilter first_iFilter = FilterFactory
							.getSimpleFilter("work_name='" + total.getStr_work_info_id() + "'");
					List<?> lsg_flList = flowConfigManager.getList(first_iFilter);
					FlowConfig flowConfig = (FlowConfig) lsg_flList.get(0);

					List<User> users = (List<User>) flowConfigManager
							.queryUserListByGroupId(flowConfig.getFirst_usergroup_id());

					String first_ids_names = initApproveNames(workInfo, distCityManager, users, "1");

					total.setCurr_appro_id_name(first_ids_names);

					if (flowConfig.getIsshowname() != null && flowConfig.getIsshowname().equals(Long.parseLong("1"))) {
						UserGroup userGroup = (UserGroup) userGroupManager
								.getObject(flowConfig.getFirst_usergroup_id());
						total.setCurr_appro_group_name(userGroup == null ? "" : userGroup.getName());
					} else {
						total.setCurr_appro_group_name(first_ids_names);
					}

				} else if (lst_flowList != null && lst_flowList.size() == 2) {
					// 显示第二审批人

					IFilter first_iFilter = FilterFactory
							.getSimpleFilter("work_name='" + total.getStr_work_info_id() + "'");
					List<?> lsg_flList = flowConfigManager.getList(first_iFilter);
					FlowConfig flowConfig = (FlowConfig) lsg_flList.get(0);
					Long appro_usergroup_id = flowConfig.getSecond_usergroup_id();
					// 证明有第二审批人
					if (appro_usergroup_id != null) {
						List<User> users = (List<User>) flowConfigManager
								.queryUserListByGroupId(flowConfig.getSecond_usergroup_id());
						String second_ids_names = initApproveNames(workInfo, distCityManager, users, "2");

						total.setCurr_appro_id_name(second_ids_names);

						if (flowConfig.getIsshowname() != null
								&& flowConfig.getIsshowname().equals(Long.parseLong("1"))) {
							UserGroup userGroup = (UserGroup) userGroupManager
									.getObject(flowConfig.getSecond_usergroup_id());
							total.setCurr_appro_group_name(userGroup == null ? "" : userGroup.getName());
						} else {
							total.setCurr_appro_group_name(second_ids_names);
						}

					} else {
						total.setCurr_appro_id_name("无");
					}
				} else if (lst_flowList != null && lst_flowList.size() == 3) {
					// 显示第三审批人
					IFilter first_iFilter = FilterFactory
							.getSimpleFilter("work_name='" + total.getStr_work_info_id() + "'");
					List<?> lsg_flList = flowConfigManager.getList(first_iFilter);
					FlowConfig flowConfig = (FlowConfig) lsg_flList.get(0);

					Long appro_usergroup_id = flowConfig.getThird_usergroup_id();
					if (appro_usergroup_id != null) {
						// 证明有第三审批人
						List<User> users = (List<User>) flowConfigManager
								.queryUserListByGroupId(flowConfig.getThird_usergroup_id());
						String third_ids_names = initApproveNames(workInfo, distCityManager, users, "3");
						total.setCurr_appro_id_name(third_ids_names);

						if (flowConfig.getIsshowname() != null
								&& flowConfig.getIsshowname().equals(Long.parseLong("1"))) {
							UserGroup userGroup = (UserGroup) userGroupManager
									.getObject(flowConfig.getThird_usergroup_id());
							total.setCurr_appro_group_name(userGroup == null ? "" : userGroup.getName());
						} else {
							total.setCurr_appro_group_name(third_ids_names);
						}

					} else {
						total.setCurr_appro_id_name("无");
					}
				} else {
					total.setCurr_appro_id_name("无");
				}

			}
		}
		Map<String, Object> map_result = new HashMap<String, Object>();
		map_result.put("header", "");
		map_result.put("pageinfo", conditions.getPageinfo());
		map_result.put("data", lst_data);
		return map_result;
	}

	private String initApproveNames(WorkInfo workInfo, DistCityManager distCityManager, List<User> users, String step) {
		List<User> commitUsers = new ArrayList<User>();
		// 取得当前登录人的用户组
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		FlowConfigManager flowConfigManager = (FlowConfigManager) SpringHelper.getBean("flowConfigManager");
		UserGroupManager userGroupManager = (UserGroupManager) SpringHelper.getBean("userGroupManager");
		FlowConfig flowConfig = flowConfigManager.queryFlowConfigByWorkName(workInfo.getWork_type());
		// UserDTO userDTO = userManager.getCurrentUserDTO();
		Long group_id = null;
		if (step.equals("1")) {
			// 第一审批组
			group_id = flowConfig.getFirst_usergroup_id();
		} else if (step.equals("2")) {
			group_id = flowConfig.getSecond_usergroup_id();
		} else if (step.equals("3")) {
			group_id = flowConfig.getThird_usergroup_id();
		}
		UserGroup userGroup = (UserGroup) userGroupManager.getObject(group_id);
		if (userGroup.getCode().equals("QYJL")) {
			Store store = (Store) storeManager.getObject(workInfo.getStore_id());
			if (users != null && users.size() > 0) {
				for (Object o : users) {
					User u = (User) o;
					if (u.getId().equals(store.getRmid())) {
						commitUsers.add(u);
					}
				}
			}
		} else if (userGroup.getCode().equals("CSMDKQSPJSZ")) {// 线上 MDKQSPJSZ
																// 本地KQGLJSZ
			if (users != null && users.size() > 0) {
				for (Object o : users) {
					User u = (User) o;
					List<DistCity> distCities = distCityManager.queryDistCityByUserIdCity(u.getId(),
							workInfo.getCityname());
					if (distCities != null && distCities.size() > 0) {
						commitUsers.add(u);
					}
				}
			}
		} else if (userGroup.getCode().equals("PTYYZXYCDDZ")) {// 总部平台运营中心-异常订单组
			if (users != null && users.size() > 0) {
				for (Object o : users) {
					User u = (User) o;
					List<DistCity> distCities = distCityManager.queryDistCityByUserIdCity(u.getId(),
							workInfo.getCityname());
					if (distCities != null && distCities.size() > 0) {
						commitUsers.add(u);
					}
				}
			}
		} else if (userGroup.getCode().equals("MDXZSHJSZ")) {// 门店选址审批角色组
			if (users != null && users.size() > 0) {
				for (Object o : users) {
					User u = (User) o;
					List<DistCity> distCities = distCityManager.queryDistCityByUserIdCity(u.getId(),
							workInfo.getCityname());
					if (distCities != null && distCities.size() > 0) {
						commitUsers.add(u);
					}
				}
			}
		} else if (userGroup.getCode().equals("MDCBSHJSZ")) {// 门店选址审批角色组
			if (users != null && users.size() > 0) {
				for (Object o : users) {
					User u = (User) o;
					List<DistCity> distCities = distCityManager.queryDistCityByUserIdCity(u.getId(),
							workInfo.getCityname());
					if (distCities != null && distCities.size() > 0) {
						commitUsers.add(u);
					}
				}
			}
		}
		// PTYYZXYCDDZ 总部平台运营中心-异常订单组

		// commitUsers为提交的HR
		String first_ids = "";
		String first_ids_names = "";
		if (commitUsers != null && commitUsers.size() > 0) {
			for (User u : commitUsers) {
				first_ids += u.getId() + ",";
				first_ids_names += u.getName() + ",";
			}
			first_ids = first_ids.substring(0, first_ids.length() - 1);
			first_ids_names = first_ids_names.substring(0, first_ids_names.length() - 1);
		}
		return first_ids_names;
	}

	/**
	 * 提交方法 更改状态 取得提交的用户组的 相对应城市的HR。更新到审批人项中。
	 */
	@Override
	public ScoreRecordTotal updateCommitStatus(Long id) {
		Date d = new Date();
		// 更改主表为审批中
		ScoreRecordTotal scoreRecordTotal = (ScoreRecordTotal) this.getObject(id);
		scoreRecordTotal.setCommit_date(d);
		scoreRecordTotal.setCommit_status(Long.parseLong("1"));
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		UserDTO userDTO = userManager.getCurrentUserDTO();
		scoreRecordTotal.setSubmit_user_id(userDTO.getId());
		scoreRecordTotal.setUpdate_user(userDTO.getName());
		scoreRecordTotal.setUpdate_time(d);
		scoreRecordTotal.setUpdate_user_id(userDTO.getId());
		scoreRecordTotal.setRemark("");

		saveObject(scoreRecordTotal);

		// 更改审批表为审批中 以及 查询审批人 存入审批表
		WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");

		workInfoManager.updateCommitStatus(scoreRecordTotal.getWork_info_id());

		// 将操作存入 审批记录表
		FlowDetailManager flowDetailManager = (FlowDetailManager) SpringHelper.getBean("flowDetailManager");
		FlowDetail flowDetail = new FlowDetail();
		flowDetail.setWork_info_id(scoreRecordTotal.getWork_info_id());
		flowDetail.setUser_id(userDTO.getId());
		flowDetailManager.saveFlowDetail(flowDetail);

		return scoreRecordTotal;
	}

	@Override
	public ScoreRecordTotal queryScoreRecordTotal(String work_month, Long store_id) {
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		ScoreRecordTotalManager scoreRecordTotalManager = (ScoreRecordTotalManager) SpringHelper
				.getBean("scoreRecordTotalManager");
		if (store_id == null) {
			UserDTO userDTO = userManager.getCurrentUserDTO();
			store_id = userDTO.getStore_id();
		}
		IFilter iFiltermain = FilterFactory.getSimpleFilter(
				" store_id=" + store_id + " and work_month='" + work_month + "' and str_work_info_id='绩效打分录入' ");
		List<ScoreRecordTotal> lst_wrList = (List<ScoreRecordTotal>) scoreRecordTotalManager.getList(iFiltermain);
		if (lst_wrList != null && lst_wrList.size() > 0) {
			return lst_wrList.get(0);
		}
		return null;
	}

	@Override
	public ScoreRecordTotal queryScoreRecordTotalByWorkId(Long work_info_id) {
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		ScoreRecordTotalManager scoreRecordTotalManager = (ScoreRecordTotalManager) SpringHelper
				.getBean("scoreRecordTotalManager");
		IFilter iFiltermain = FilterFactory.getSimpleFilter(" work_info_id=" + work_info_id);
		List<ScoreRecordTotal> lst_wrList = (List<ScoreRecordTotal>) scoreRecordTotalManager.getList(iFiltermain);
		if (lst_wrList != null && lst_wrList.size() > 0) {
			return lst_wrList.get(0);
		}
		return null;
	}

	// 导出全部的考勤所用方法
	@Override
	public List<ScoreRecordTotal> queryScoreRecordTotalListByWorkIds(String work_info_ids) {
		ScoreRecordTotalManager scoreRecordTotalManager = (ScoreRecordTotalManager) SpringHelper
				.getBean("scoreRecordTotalManager");
		IFilter iFiltermain = FilterFactory.getSimpleFilter(" work_info_id in(" + work_info_ids + ")");
		List<ScoreRecordTotal> lst_wrList = (List<ScoreRecordTotal>) scoreRecordTotalManager.getList(iFiltermain);
		if (lst_wrList != null && lst_wrList.size() > 0) {
			return lst_wrList;
		}
		return null;
	}

	/*
	 * @Override public Map<String, Object>
	 * queryScoreRecordTotalByCheck(QueryConditions conditions) {
	 * ScoreMonthManager workMonthManager =
	 * (WorkMonthManager)SpringHelper.getBean("workMonthManager");
	 * WorkRecordTotalDao workRecordTotalDao =
	 * (WorkRecordTotalDao)SpringHelper.getBean(WorkRecordTotalDao.class.getName
	 * ()); WorkMonth workMonth = workMonthManager.getMaxWorkMonth();
	 * Map<String,Object> map_result = new HashMap<String,Object>(); PageInfo
	 * pageInfo = conditions.getPageinfo(); if(workMonth == null){
	 * pageInfo.setTotalRecords(0); map_result.put("pageinfo", pageInfo);
	 * map_result.put("header", ""); map_result.put("data", new
	 * ArrayList<Object>()); }else{ //取得登录人 所管理的城市 UserManager userManager =
	 * (UserManager)SpringHelper.getBean("userManager"); StringBuffer
	 * sbfCondition = new StringBuffer(); String cityssql = ""; List<DistCity>
	 * distCityList = userManager.getCurrentUserCity();
	 * if(distCityList!=null&&distCityList.size()>0){ for(DistCity
	 * d:distCityList){ cityssql += "'"+d.getCityname()+"',"; }
	 * cityssql=cityssql.substring(0, cityssql.length()-1); }
	 * 
	 * map_result =
	 * workRecordTotalDao.queryWorkRecordTotalByCheck(workMonth.getWork_month(),
	 * pageInfo,cityssql); } return map_result; }
	 */

	@Override
	public ScoreRecordTotal findScoreRecordTotalById(Long score_record_id) {
		return (ScoreRecordTotal) this.getObject(score_record_id);
	}

	@Override
	public int getMaxSubmitCount(String work_month, String cityname) {
		// 取得当前登录人 所管理城市
		StringBuilder sbfCondition = new StringBuilder();
		if (cityname != null && cityname.length() > 0) {
			sbfCondition.append(" cityname = '" + cityname + "'");
		} else {
			sbfCondition.append(" 0=1 ");
		}

		IFilter filter = FilterFactory.getSimpleFilter("commit_status", 1L)
				.appendAnd(FilterFactory.getSimpleFilter("work_month", work_month))
				.appendAnd(FilterFactory.getSimpleFilter(sbfCondition.toString()));
		List<?> lst_data = this.getList(filter);
		if (lst_data != null) {
			return lst_data.size();
		}
		return 0;
	}

	/*
	 * @Override public String saveScoreRecordForExcel(String work_month,String
	 * cityname) { UserManager userManager =
	 * (UserManager)SpringHelper.getBean("userManager");
	 * 
	 * ScoreMonthManager workMonthManager =
	 * (WorkMonthManager)SpringHelper.getBean("workMonthManager");
	 * 
	 * String str_file_name = "work_record_template"; String strRootpath =
	 * Thread.currentThread().getContextClassLoader().getResource(File.separator
	 * ).getPath(); //配置文件中的路径 String str_filepath =
	 * strRootpath.concat(getPropertiesValueUtil().getStringValue(str_file_name)
	 * .replace("/", File.separator));
	 * 
	 * File file_template = new File(str_filepath);
	 * 
	 * StringBuilder sb_condition = new StringBuilder(); sb_condition.append(
	 * "work_month = '" + work_month +"'");
	 * 
	 * List<DistCity> distCityList = userManager.getCurrentUserCity();
	 * if(cityname == null){ StringBuilder sb_citynames = new StringBuilder();
	 * for(int i = 0;i < distCityList.size();i++){ if(i != 0){
	 * sb_citynames.append(","); }
	 * sb_citynames.append("'"+distCityList.get(i).getCityname()+"'"); }
	 * if(sb_citynames.length() > 0){ sb_condition.append("cityname in (" +
	 * sb_citynames +")"); } }else{ sb_condition.append(" and cityname = '" +
	 * cityname +"'"); }
	 * 
	 * List<WorkMonth> lst_workmonth =
	 * (List<WorkMonth>)workMonthManager.getList(FilterFactory.getSimpleFilter(
	 * sb_condition.toString())); sb_condition.append(
	 * " and commit_status in (1,3) "); IFilter iFiltermain =
	 * FilterFactory.getSimpleFilter(sb_condition.toString());
	 * List<WorkRecordTotal> lst_wrtotalList =
	 * (List<WorkRecordTotal>)this.getList(iFiltermain);
	 * 
	 * if(lst_wrtotalList == null || lst_wrtotalList.size() == 0){ return null;
	 * } WorkMonth workMonth = lst_workmonth.get(0);
	 * 
	 * //处理月的天数 int days = DateUtils.getDaysByMonths(work_month); int appendDays
	 * =0; switch (days) { case 28: appendDays=-3; break; case 29:
	 * appendDays=-2; break; case 30: appendDays=-1; break; case 31:
	 * appendDays=0; break; default: appendDays=0; break; }
	 * 
	 * 
	 * String str_file_dir_path = PropertiesUtil.getValue("file.root");
	 * 
	 * String exportFileName = "work_record_"+work_month+".xls";
	 * 
	 * String str_newfilepath = str_file_dir_path + "/"+exportFileName;
	 * 
	 * File file_new = new File(str_newfilepath); if(file_new.exists()){
	 * file_new.delete(); } try { FileCopyUtils.copy(file_template, file_new);
	 * FileInputStream fis_input_excel = new FileInputStream(file_new); Workbook
	 * wb_wrinfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
	 * 
	 * setCellStyle_common(wb_wrinfo); setLeftCellStyle_common(wb_wrinfo);
	 * setStrongCenterCellStyle_common(wb_wrinfo);
	 * 
	 * Sheet sh_data = wb_wrinfo.getSheetAt(0);
	 * 
	 * //标题 Row obj_row_title = sh_data.getRow(0);
	 * setStrongCenterCellValue(obj_row_title, 0,
	 * ValueUtil.getStringValue(work_month+"份员工考勤情况统计表"));
	 * 
	 * //第二行 填报单位 // Row obj_row_org = sh_data.getRow(2); //
	 * //setCellValue(obj_row_org, 1, ValueUtil.getStringValue("填报单位：")); //
	 * setStrongCenterCellValue(obj_row_org, 2,
	 * ValueUtil.getStringValue(store.getName()));
	 * 
	 * //第三行 填报单位 Row obj_row_title1 = sh_data.getRow(3); CellRangeAddress
	 * isauth=new CellRangeAddress(3, 3, 2, days+1);
	 * sh_data.addMergedRegion(isauth); setCellValue(obj_row_title1, 2,
	 * ValueUtil.getStringValue("日       期"));
	 * 
	 * CellRangeAddress monthSum=new CellRangeAddress(3, 3, days+2, days+18);
	 * sh_data.addMergedRegion(monthSum); setCellValue(obj_row_title1,
	 * 33+appendDays, ValueUtil.getStringValue("月  累  计"));
	 * 
	 * CellRangeAddress fb=new CellRangeAddress(3, 3, days+15, days+16);
	 * sh_data.addMergedRegion(fb); setCellValue(obj_row_title1, 46+appendDays,
	 * ValueUtil.getStringValue("饭补"));
	 * 
	 * //列宽 sh_data.setColumnWidth(50+appendDays,
	 * "个人签字".getBytes().length*2*156); setCellValue(obj_row_title1,
	 * 50+appendDays, ValueUtil.getStringValue("个人签字"));
	 * sh_data.setColumnWidth(51+appendDays, "绩效分".getBytes().length*2*156);
	 * setCellValue(obj_row_title1, 51+appendDays,
	 * ValueUtil.getStringValue("绩效分"));
	 * 
	 * CellRangeAddress signName=new CellRangeAddress(3, 5, 50+appendDays,
	 * 50+appendDays); sh_data.addMergedRegion(signName); CellRangeAddress
	 * score=new CellRangeAddress(3, 5, 51+appendDays, 51+appendDays);
	 * sh_data.addMergedRegion(score);
	 * 
	 * //第四行 填报单位 Row obj_row_title2 = sh_data.getRow(4); Row
	 * obj_row_title2_week = sh_data.getRow(5); //星期 int year =
	 * Integer.parseInt(work_month.split("-")[0]); int month =
	 * Integer.parseInt(work_month.split("-")[1]); String firstWeek =
	 * DateUtils.getFirstDayWeek(year, month); String weekdaystr = "日一二三四五六";
	 * String retWeek
	 * =weekdaystr.substring(weekdaystr.indexOf(firstWeek),weekdaystr.length());
	 * for(int i=0;i<5;i++){ retWeek+=weekdaystr; }
	 * retWeek=retWeek.substring(0,days); char[] weeks = retWeek.toCharArray();
	 * //日期1-31 for(int i=1;i<(days+1);i++){ int columnnum = 1;
	 * setCellValue(obj_row_title2, columnnum+i,
	 * ValueUtil.getStringValue(i+"")); setCellValue(obj_row_title2_week,
	 * columnnum+i, ValueUtil.getStringValue(weeks[i-1])); }
	 * 
	 * //月累计部分 setCellValue(obj_row_title2, 33+appendDays,
	 * ValueUtil.getStringValue("出勤")); setCellValue(obj_row_title2,
	 * 34+appendDays, ValueUtil.getStringValue("倒休"));
	 * setCellValue(obj_row_title2, 35+appendDays,
	 * ValueUtil.getStringValue("事假")); setCellValue(obj_row_title2,
	 * 36+appendDays, ValueUtil.getStringValue("病假"));
	 * setCellValue(obj_row_title2, 37+appendDays,
	 * ValueUtil.getStringValue("婚假")); setCellValue(obj_row_title2,
	 * 38+appendDays, ValueUtil.getStringValue("产假"));
	 * setCellValue(obj_row_title2, 39+appendDays,
	 * ValueUtil.getStringValue("丧假")); setCellValue(obj_row_title2,
	 * 40+appendDays, ValueUtil.getStringValue("年休"));
	 * setCellValue(obj_row_title2, 41+appendDays,
	 * ValueUtil.getStringValue("出差")); setCellValue(obj_row_title2,
	 * 42+appendDays, ValueUtil.getStringValue("工伤"));
	 * setCellValue(obj_row_title2, 43+appendDays,
	 * ValueUtil.getStringValue("旷工")); setCellValue(obj_row_title2,
	 * 44+appendDays, ValueUtil.getStringValue("迟到"));
	 * setCellValue(obj_row_title2, 45+appendDays,
	 * ValueUtil.getStringValue("早退"));
	 * 
	 * for(int i=33;i<50;i++){ CellRangeAddress totalStyle=new
	 * CellRangeAddress(4, 5, i+appendDays, i+appendDays);
	 * sh_data.addMergedRegion(totalStyle); }
	 * 
	 * 
	 * sh_data.setColumnWidth(46+appendDays, "法定加班[时]".getBytes().length*2*96);
	 * setCellValue(obj_row_title2, 46+appendDays,
	 * ValueUtil.getStringValue("法定加班[时]"));
	 * sh_data.setColumnWidth(47+appendDays, "饭补天数".getBytes().length*2*96);
	 * setCellValue(obj_row_title2, 47+appendDays,
	 * ValueUtil.getStringValue("饭补天数"));
	 * 
	 * //新增加排班工时和实际工时 sh_data.setColumnWidth(48+appendDays,
	 * "排班工时".getBytes().length*2*96); setCellValue(obj_row_title2,
	 * 48+appendDays, ValueUtil.getStringValue("排班工时"));
	 * sh_data.setColumnWidth(49+appendDays, "实际工时".getBytes().length*2*96);
	 * setCellValue(obj_row_title2, 49+appendDays,
	 * ValueUtil.getStringValue("实际工时"));
	 * 
	 * 
	 * //补空 setCellValue(obj_row_title2, 50+appendDays,
	 * ValueUtil.getStringValue("")); setCellValue(obj_row_title2,
	 * 51+appendDays, ValueUtil.getStringValue(""));
	 * setCellValue(obj_row_title2_week, 50+appendDays,
	 * ValueUtil.getStringValue("")); setCellValue(obj_row_title2_week,
	 * 51+appendDays, ValueUtil.getStringValue(""));
	 * 
	 * 
	 * //填充人员考勤 int nRowIndex=6; for(WorkRecordTotal total : lst_wrtotalList){
	 * for(WorkRecord workRecord : total.getChilds()){
	 * sh_data.createRow(nRowIndex); Row obj_row = sh_data.getRow(nRowIndex);
	 * 
	 * setCellValue(obj_row, 0,
	 * ValueUtil.getStringValue(workRecord.getEmployee_no())); User user =
	 * userManager.findEmployeeByEmployeeNo(workRecord.getEmployee_no());
	 * setCellValue(obj_row, 1, ValueUtil.getStringValue(user.getName()));
	 * setCellValue(obj_row, 2, ValueUtil.getStringValue(workRecord.getDay1()));
	 * setCellValue(obj_row, 3, ValueUtil.getStringValue(workRecord.getDay2()));
	 * setCellValue(obj_row, 4, ValueUtil.getStringValue(workRecord.getDay3()));
	 * setCellValue(obj_row, 5, ValueUtil.getStringValue(workRecord.getDay4()));
	 * setCellValue(obj_row, 6, ValueUtil.getStringValue(workRecord.getDay5()));
	 * setCellValue(obj_row, 7, ValueUtil.getStringValue(workRecord.getDay6()));
	 * setCellValue(obj_row, 8, ValueUtil.getStringValue(workRecord.getDay7()));
	 * setCellValue(obj_row, 9, ValueUtil.getStringValue(workRecord.getDay8()));
	 * setCellValue(obj_row, 10,
	 * ValueUtil.getStringValue(workRecord.getDay9())); setCellValue(obj_row,
	 * 11, ValueUtil.getStringValue(workRecord.getDay10()));
	 * 
	 * setCellValue(obj_row, 12,
	 * ValueUtil.getStringValue(workRecord.getDay11())); setCellValue(obj_row,
	 * 13, ValueUtil.getStringValue(workRecord.getDay12()));
	 * setCellValue(obj_row, 14,
	 * ValueUtil.getStringValue(workRecord.getDay13())); setCellValue(obj_row,
	 * 15, ValueUtil.getStringValue(workRecord.getDay14()));
	 * setCellValue(obj_row, 16,
	 * ValueUtil.getStringValue(workRecord.getDay15())); setCellValue(obj_row,
	 * 17, ValueUtil.getStringValue(workRecord.getDay16()));
	 * setCellValue(obj_row, 18,
	 * ValueUtil.getStringValue(workRecord.getDay17())); setCellValue(obj_row,
	 * 19, ValueUtil.getStringValue(workRecord.getDay18()));
	 * setCellValue(obj_row, 20,
	 * ValueUtil.getStringValue(workRecord.getDay19())); setCellValue(obj_row,
	 * 21, ValueUtil.getStringValue(workRecord.getDay20()));
	 * 
	 * setCellValue(obj_row, 22,
	 * ValueUtil.getStringValue(workRecord.getDay21())); setCellValue(obj_row,
	 * 23, ValueUtil.getStringValue(workRecord.getDay22()));
	 * setCellValue(obj_row, 24,
	 * ValueUtil.getStringValue(workRecord.getDay23())); setCellValue(obj_row,
	 * 25, ValueUtil.getStringValue(workRecord.getDay24()));
	 * setCellValue(obj_row, 26,
	 * ValueUtil.getStringValue(workRecord.getDay25())); setCellValue(obj_row,
	 * 27, ValueUtil.getStringValue(workRecord.getDay26()));
	 * setCellValue(obj_row, 28,
	 * ValueUtil.getStringValue(workRecord.getDay27())); setCellValue(obj_row,
	 * 29, ValueUtil.getStringValue(workRecord.getDay28()));
	 * setCellValue(obj_row, 30,
	 * ValueUtil.getStringValue(workRecord.getDay29())); setCellValue(obj_row,
	 * 31, ValueUtil.getStringValue(workRecord.getDay30()));
	 * setCellValue(obj_row, 32,
	 * ValueUtil.getStringValue(workRecord.getDay31()));
	 * 
	 * setCellValue(obj_row, 33+appendDays,
	 * ValueUtil.getStringValue(workRecord.getWorkdays()));
	 * setCellValue(obj_row, 34+appendDays,
	 * ValueUtil.getStringValue(workRecord.getAdjholiday()));
	 * setCellValue(obj_row, 35+appendDays,
	 * ValueUtil.getStringValue(workRecord.getEventday()));
	 * setCellValue(obj_row, 36+appendDays,
	 * ValueUtil.getStringValue(workRecord.getBadday())); setCellValue(obj_row,
	 * 37+appendDays, ValueUtil.getStringValue(workRecord.getWedday()));
	 * setCellValue(obj_row, 38+appendDays,
	 * ValueUtil.getStringValue(workRecord.getProday())); setCellValue(obj_row,
	 * 39+appendDays, ValueUtil.getStringValue(workRecord.getLoseday()));
	 * setCellValue(obj_row, 40+appendDays,
	 * ValueUtil.getStringValue(workRecord.getYearholiday()));
	 * setCellValue(obj_row, 41+appendDays,
	 * ValueUtil.getStringValue(workRecord.getTripday())); setCellValue(obj_row,
	 * 42+appendDays, ValueUtil.getStringValue(workRecord.getWorkhurtday()));
	 * setCellValue(obj_row, 43+appendDays,
	 * ValueUtil.getStringValue(workRecord.getAbsenceday()));
	 * setCellValue(obj_row, 44+appendDays,
	 * ValueUtil.getStringValue(workRecord.getLateday())); setCellValue(obj_row,
	 * 45+appendDays, ValueUtil.getStringValue(workRecord.getLeaveearlyday()));
	 * 
	 * 
	 * setCellValue(obj_row, 46+appendDays,
	 * ValueUtil.getStringValue(workRecord.getOvertime()));
	 * setCellValue(obj_row, 47+appendDays,
	 * ValueUtil.getStringValue(workRecord.getAllowdays()));
	 * 
	 * //排班工时 与实际工时 double totalovertime = workRecord.getTotalovertime(); double
	 * realovertime = workRecord.getRealovertime(); setCellValue(obj_row,
	 * 48+appendDays,
	 * ValueUtil.getStringValue(totalovertime==0?"":(int)totalovertime));
	 * setCellValue(obj_row, 49+appendDays,
	 * ValueUtil.getStringValue(realovertime==0?"":(int)realovertime));
	 * 
	 * 
	 * setCellValue(obj_row, 50+appendDays,
	 * ValueUtil.getStringValue(workRecord.getSignname()));
	 * setCellValue(obj_row, 51+appendDays,
	 * ValueUtil.getStringValue(workRecord.getScore()));
	 * 
	 * nRowIndex++; } }
	 * 
	 * 
	 * //excel的尾部 int bottom_id1=nRowIndex + 7; int bottom_id2=nRowIndex + 8;
	 * int bottom_id3=nRowIndex + 9; int bottom_id4=nRowIndex + 10; int
	 * bottom_id5=nRowIndex + 12; sh_data.createRow(bottom_id1);
	 * sh_data.createRow(bottom_id2); sh_data.createRow(bottom_id3);
	 * sh_data.createRow(bottom_id4); sh_data.createRow(bottom_id5); Row
	 * obj_row_bottom1 = sh_data.getRow(bottom_id1); Row obj_row_bottom2 =
	 * sh_data.getRow(bottom_id2); Row obj_row_bottom3 =
	 * sh_data.getRow(bottom_id3); Row obj_row_bottom4 =
	 * sh_data.getRow(bottom_id4); Row obj_row_bottom5 =
	 * sh_data.getRow(bottom_id5); CellRangeAddress cra1=new
	 * CellRangeAddress(bottom_id1, bottom_id1, 1, 47); CellRangeAddress
	 * cra2=new CellRangeAddress(bottom_id2, bottom_id2, 1, 47);
	 * CellRangeAddress cra3=new CellRangeAddress(bottom_id3, bottom_id3, 1,
	 * 47); CellRangeAddress cra4=new CellRangeAddress(bottom_id4, bottom_id4,
	 * 1, 47); CellRangeAddress cra5=new CellRangeAddress(bottom_id5,
	 * bottom_id5, 1, 6); CellRangeAddress cra6=new CellRangeAddress(bottom_id5,
	 * bottom_id5, 29, 36); //在sheet里增加合并单元格 sh_data.addMergedRegion(cra1);
	 * sh_data.addMergedRegion(cra2); sh_data.addMergedRegion(cra3);
	 * sh_data.addMergedRegion(cra4); sh_data.addMergedRegion(cra5);
	 * sh_data.addMergedRegion(cra6);
	 * 
	 * 
	 * obj_row_bottom1.setHeight((short)600); setLeftCellValue(obj_row_bottom1,
	 * 1, ValueUtil.getStringValue(
	 * "1、出勤根据A、B、C、D四种班次分别进行标注,A、B班工时数为10小时,C、D班工时数为8小时,班次总工时须满本月法定工时"+
	 * workMonth.getTotalworktime()+
	 * "小时,事假∧,病假△,婚假☆,丧假□,产假﹢,年休假◎,出差※,工伤◇,旷工×,迟到﹤,早退﹥；"));
	 * setLeftCellValue(obj_row_bottom2, 1,
	 * ValueUtil.getStringValue("2、考勤表由考勤员如实填写；"));
	 * setLeftCellValue(obj_row_bottom3, 1, ValueUtil.getStringValue(
	 * "3、月累计栏：出勤、事假、病假、婚假、丧假、年休假、出差、工伤假、旷工以天数累计,迟到、早退以次数累计；"));
	 * setLeftCellValue(obj_row_bottom4, 1,
	 * ValueUtil.getStringValue("4、考勤表电子版每月2日前交人力资源中心,纸质版每月15号前提交。"));
	 * setLeftCellValue(obj_row_bottom5, 1,
	 * ValueUtil.getStringValue("人力中心负责人：")); setLeftCellValue(obj_row_bottom5,
	 * 29, ValueUtil.getStringValue("部门（中心负责人）："));
	 * //sh_data.setColumnHidden(32, true);
	 * 
	 * FileOutputStream fis_out_excel = new FileOutputStream(file_new);
	 * wb_wrinfo.write(fis_out_excel); fis_out_excel.close();
	 * fis_input_excel.close(); } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * return PropertiesUtil.getValue("file.web.root")+exportFileName; }
	 */

	// 批量审批的方法
	@Override
	public List<ScoreRecordTotal> updateScoreRecordTotalToReturnMult(List<Map<String, Object>> arr) {
		List<ScoreRecordTotal> ret_array = null;
		if (arr != null && arr.size() > 0) {
			try {
				Long appro_type = null;
				ret_array = new ArrayList<ScoreRecordTotal>();
				List<WorkInfo> workInfos = new ArrayList<WorkInfo>();
				for (Map<String, Object> srt : arr) {
					ScoreRecordTotal scoreRecordTotal = new ScoreRecordTotal();
					scoreRecordTotal.setId(Long.parseLong(srt.get("id").toString()));
					scoreRecordTotal.setRemark(srt.get("remark").toString());
					scoreRecordTotal.setCommit_status(Long.parseLong(srt.get("commit_status").toString()));
					ScoreRecordTotal rt_scoreRecordTotal = updateScoreRecordTotalMult(scoreRecordTotal);
					appro_type = scoreRecordTotal.getCommit_status();
					ret_array.add(rt_scoreRecordTotal);
				}

				String ids = "";
				if (ret_array != null & ret_array.size() > 0) {
					for (ScoreRecordTotal srt : ret_array) {
						ids += srt.getWork_info_id() + ",";
					}
					ids = ids.substring(0, ids.length() - 1);
				}
				WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
				IFilter filter = FilterFactory.getStringFilter("id in (" + ids + ")");
				List<WorkInfo> wInfos = (List<WorkInfo>) workInfoManager.getList(filter);
				pushSPMsg(wInfos, appro_type);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret_array;
	}

	// 审批方法
	@Override
	public ScoreRecordTotal updateScoreRecordTotalToReturn(ScoreRecordTotal scoreRecordTotal) {
		Long appro_type = scoreRecordTotal.getCommit_status();
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		// AppMessageManager appMessageManager =
		// (AppMessageManager)SpringHelper.getBean("appMessageManager");
		UserDTO userdto = userManager.getCurrentUserDTO();
		// 查找有几级审批。
		FlowConfigManager flowConfigManager = (FlowConfigManager) SpringHelper.getBean("flowConfigManager");
		ScoreRecordTotal save_scoreRecordTotal = (ScoreRecordTotal) this.getObject(scoreRecordTotal.getId());

		WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
		WorkInfo workInfo = (WorkInfo) workInfoManager.getObject(save_scoreRecordTotal.getWork_info_id());
		FlowConfig flowConfig = flowConfigManager.queryFlowConfigByWorkName(workInfo.getWork_type());

		int app_num = 0;
		if (flowConfig.getThird_usergroup_id() != null) {
			app_num++;
		}
		if (flowConfig.getSecond_usergroup_id() != null) {
			app_num++;
		}
		if (flowConfig.getFirst_usergroup_id() != null) {
			app_num++;
		}

		DistCityManager distCityManager = (DistCityManager) SpringHelper.getBean("distCityManager");
		// 查看已经有多少条记录
		FlowDetailManager flowDetailManager = (FlowDetailManager) SpringHelper.getBean("flowDetailManager");
		List<?> lst_flowList = flowDetailManager.queryFlowDetailByWorkId(workInfo.getId());
		if (lst_flowList.size() == app_num) {// 最后一个审批的
			save_scoreRecordTotal.setCommit_status(scoreRecordTotal.getCommit_status());
			// 如果审批通过，更改用户发起状态
			workInfo.setCommit_status(scoreRecordTotal.getCommit_status());
		} else {
			save_scoreRecordTotal.setCommit_status(Long.parseLong("1"));
		}

		// 驳回了
		if (scoreRecordTotal.getCommit_status().equals(Long.parseLong("2"))) {
			save_scoreRecordTotal.setCommit_status(Long.parseLong("2"));
			workInfo.setCommit_status(Long.parseLong("2"));
		}

		save_scoreRecordTotal.setRemark(scoreRecordTotal.getRemark());

		if (app_num == 1) {
			// 更改审批人为first
			FlowConfig fcConfig = flowConfigManager.queryFlowConfigByWorkName(workInfo.getWork_type());
			List<User> users = (List<User>) flowConfigManager.queryUserListByGroupId(fcConfig.getFirst_usergroup_id());
			String first_ids = initApproveIds(workInfo, distCityManager, users, "1");
			workInfo.setCurr_appro_id(first_ids);
		}
		if (app_num == 2) {
			// 更改审批人为second
			FlowConfig fcConfig = flowConfigManager.queryFlowConfigByWorkName(workInfo.getWork_type());
			List<User> users = (List<User>) flowConfigManager.queryUserListByGroupId(fcConfig.getSecond_usergroup_id());
			String second_ids = initApproveIds(workInfo, distCityManager, users, "2");
			workInfo.setCurr_appro_id(second_ids);
		}
		if (app_num == 3) {
			// 更改审批人为third
			FlowConfig fcConfig = flowConfigManager.queryFlowConfigByWorkName(workInfo.getWork_type());
			List<User> users = (List<User>) flowConfigManager.queryUserListByGroupId(fcConfig.getThird_usergroup_id());
			String third_ids = initApproveIds(workInfo, distCityManager, users, "3");
			workInfo.setCurr_appro_id(third_ids);
		}

		preSaveObject(workInfo);
		workInfoManager.saveObject(workInfo);

		save_scoreRecordTotal.setCommit_status(workInfo.getCommit_status());

		preObject(save_scoreRecordTotal);
		this.saveObject(save_scoreRecordTotal);

		// 保存审批记录
		FlowDetail flowDetail = new FlowDetail();
		flowDetail.setApprov_content(scoreRecordTotal.getRemark());
		flowDetail.setApprov_ret(save_scoreRecordTotal.getCommit_status().toString());
		Date date = new Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		flowDetail.setApprov_time(sdate);
		flowDetail.setWork_info_id(save_scoreRecordTotal.getWork_info_id());
		flowDetail.setUser_id(userdto.getId());

		preSaveObject(flowDetail);
		flowDetailManager.saveObject(flowDetail);

		// 推送消息
		/*
		 * AppMessage appMessage = new AppMessage();
		 * appMessage.setTitle("考勤提示"); User submit_user =
		 * userManager.getUserEntity(save_scoreRecordTotal.getSubmit_user_id());
		 * UserDTO current_user = userManager.getCurrentUserDTO();
		 * if(scoreRecordTotal.getCommit_status() == 2){
		 * appMessage.setContent("您提交的"+save_scoreRecordTotal.getWork_month()+
		 * "考勤数据被" + current_user.getName() + "退回。原因是\n" +
		 * scoreRecordTotal.getRemark()); }else
		 * if(scoreRecordTotal.getCommit_status() == 3){
		 * appMessage.setContent("您提交的"+save_scoreRecordTotal.getWork_month()+
		 * "考勤数据审核通过。"); } appMessage.setPk_id(save_scoreRecordTotal.getId());
		 * appMessage.setJump_path("score_record_result");
		 * appMessage.setType("score_record");
		 * appMessage.setTo_employee_id(submit_user.getId());
		 * preObject(appMessage);
		 * 
		 * appMessageManager.saveMessageAndPush(submit_user,appMessage);
		 */

		// 审核时，同时更改清洗过的订单状态 。
		// 更改清洗的订单状态
		if (workInfo.getWork_type() != null && workInfo.getWork_type().equals("异常订单申诉")) {
			DsAbnormalOrderManager dsAbnormalOrderManager = (DsAbnormalOrderManager) SpringHelper
					.getBean("dsAbnormalOrderManager");
			DsAbnormalOrder dsAbnormalOrder = dsAbnormalOrderManager.queryDsAbnormalOrderBySN(workInfo.getOrder_sn());
			if (dsAbnormalOrder != null) {
				dsAbnormalOrder.setStatus(save_scoreRecordTotal.getCommit_status().toString());
				dsAbnormalOrder.setUpdatetime(workInfo.getUpdate_time());
				dsAbnormalOrderManager.saveDsAbnormalOrder(dsAbnormalOrder);
			}
		}

		// 修改门店状态
		if (workInfo.getWork_type() != null && workInfo.getWork_type().equals("门店选址审核")) {
			StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
			StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");
			StoreDynamic storeDynamic = storeDynamicManager.findStoreDynamicByWorkId(workInfo.getOrder_sn());
			if (storeDynamic != null) {
				storeDynamic.setAuditor_status(Integer.parseInt(save_scoreRecordTotal.getCommit_status().toString()));
				storeDynamic.setUpdate_time(workInfo.getUpdate_time());
				if (storeDynamic.getAuditor_status() == 3) {
					if (!"运营中".equals(storeDynamic.getEstate()) && !"待开业".equals(storeDynamic.getEstate())) {
						storeDynamic.setEstate("筹备中");
					}
					// 调用sync同步门店方法
					try {
						Store store = storeManager.insertStoresyncDynamicStore(storeDynamic);
						storeManager.syncStore(store);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				storeDynamicManager.saveObject(storeDynamic);

			}

		}
		// 修改门店状态
		if (workInfo.getWork_type() != null && workInfo.getWork_type().equals("门店筹备审核")) {
			StoreDocumentInfoManager storeDocumentInfoManager = (StoreDocumentInfoManager) SpringHelper
					.getBean("storeDocumentInfoManager");
			StoreDocumentInfo storeDocumentInfo = storeDocumentInfoManager
					.findStoreDocumentInfoByWorkId(workInfo.getOrder_sn());
			StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");
			if (storeDocumentInfo != null) {
				storeDocumentInfo
						.setAudit_status(Integer.parseInt(save_scoreRecordTotal.getCommit_status().toString()));
				storeDocumentInfo.setUpdate_time(workInfo.getUpdate_time());
				if (storeDocumentInfo.getAudit_status() == 3) {
					AttachmentManager attachmentManager = (AttachmentManager) SpringHelper.getBean("attachmentManager");
					StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
					Attachment attachment = attachmentManager
							.findAttachmentByStoreIdType(storeDocumentInfo.getStore_id(), 7);
					if (attachment != null) {
						StoreDynamic storeDynamic = storeDynamicManager
								.findStoreDynamic(storeDocumentInfo.getStore_id());
						if (storeDynamic != null && !"运营中".equals(storeDynamic.getEstate())) {
							storeDynamic.setEstate("待开业");
							storeDynamicManager.saveObject(storeDynamic);
							storeManager.insertStoresyncDynamicStore(storeDynamic);
						}
						/*
						 * Store store =
						 * storeManager.findStore(storeDocumentInfo.getStore_id(
						 * )); if (store != null &&
						 * !"运营中".equals(storeDynamic.getEstate())) {
						 * store.setEstate("待开业");
						 * storeManager.saveObject(store); }
						 */
					}
				}
				storeDocumentInfoManager.saveObject(storeDocumentInfo);
			}
		}

		List<WorkInfo> workInfos = new ArrayList<WorkInfo>();
		workInfos.add(workInfo);
		pushSPMsg(workInfos, appro_type);

		return save_scoreRecordTotal;
	}

	// 批量 审批方法
	@Override
	public ScoreRecordTotal updateScoreRecordTotalMult(ScoreRecordTotal scoreRecordTotal) {
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");

		// AppMessageManager appMessageManager =
		// (AppMessageManager)SpringHelper.getBean("appMessageManager");
		UserDTO userdto = userManager.getCurrentUserDTO();
		// 查找有几级审批。
		FlowConfigManager flowConfigManager = (FlowConfigManager) SpringHelper.getBean("flowConfigManager");
		ScoreRecordTotal save_scoreRecordTotal = (ScoreRecordTotal) this.getObject(scoreRecordTotal.getId());

		WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
		WorkInfo workInfo = (WorkInfo) workInfoManager.getObject(save_scoreRecordTotal.getWork_info_id());
		FlowConfig flowConfig = flowConfigManager.queryFlowConfigByWorkName(workInfo.getWork_type());

		int app_num = 0;
		if (flowConfig.getThird_usergroup_id() != null) {
			app_num++;
		}
		if (flowConfig.getSecond_usergroup_id() != null) {
			app_num++;
		}
		if (flowConfig.getFirst_usergroup_id() != null) {
			app_num++;
		}

		DistCityManager distCityManager = (DistCityManager) SpringHelper.getBean("distCityManager");
		// 查看已经有多少条记录
		FlowDetailManager flowDetailManager = (FlowDetailManager) SpringHelper.getBean("flowDetailManager");
		List<?> lst_flowList = flowDetailManager.queryFlowDetailByWorkId(workInfo.getId());
		if (lst_flowList.size() == app_num) {// 最后一个审批的
			save_scoreRecordTotal.setCommit_status(scoreRecordTotal.getCommit_status());
			// 如果审批通过，更改用户发起状态
			workInfo.setCommit_status(scoreRecordTotal.getCommit_status());
		} else {
			save_scoreRecordTotal.setCommit_status(Long.parseLong("1"));
		}

		// 驳回了
		if (scoreRecordTotal.getCommit_status().equals(Long.parseLong("2"))) {
			save_scoreRecordTotal.setCommit_status(Long.parseLong("2"));
			workInfo.setCommit_status(Long.parseLong("2"));
		}

		save_scoreRecordTotal.setRemark(scoreRecordTotal.getRemark());

		if (app_num == 1) {
			// 更改审批人为first
			FlowConfig fcConfig = flowConfigManager.queryFlowConfigByWorkName(workInfo.getWork_type());
			List<User> users = (List<User>) flowConfigManager.queryUserListByGroupId(fcConfig.getFirst_usergroup_id());
			String first_ids = initApproveIds(workInfo, distCityManager, users, "1");
			workInfo.setCurr_appro_id(first_ids);
		}
		if (app_num == 2) {
			// 更改审批人为second
			FlowConfig fcConfig = flowConfigManager.queryFlowConfigByWorkName(workInfo.getWork_type());
			List<User> users = (List<User>) flowConfigManager.queryUserListByGroupId(fcConfig.getSecond_usergroup_id());
			String second_ids = initApproveIds(workInfo, distCityManager, users, "2");
			workInfo.setCurr_appro_id(second_ids);
		}
		if (app_num == 3) {
			// 更改审批人为third
			FlowConfig fcConfig = flowConfigManager.queryFlowConfigByWorkName(workInfo.getWork_type());
			List<User> users = (List<User>) flowConfigManager.queryUserListByGroupId(fcConfig.getThird_usergroup_id());
			String third_ids = initApproveIds(workInfo, distCityManager, users, "3");
			workInfo.setCurr_appro_id(third_ids);
		}

		preSaveObject(workInfo);
		workInfoManager.saveObject(workInfo);

		save_scoreRecordTotal.setCommit_status(workInfo.getCommit_status());

		preObject(save_scoreRecordTotal);
		this.saveObject(save_scoreRecordTotal);

		// 保存审批记录
		FlowDetail flowDetail = new FlowDetail();
		flowDetail.setApprov_content(scoreRecordTotal.getRemark());
		flowDetail.setApprov_ret(save_scoreRecordTotal.getCommit_status().toString());
		Date date = new Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		flowDetail.setApprov_time(sdate);
		flowDetail.setWork_info_id(save_scoreRecordTotal.getWork_info_id());
		flowDetail.setUser_id(userdto.getId());

		preSaveObject(flowDetail);
		flowDetailManager.saveObject(flowDetail);

		// 审核时，同时更改清洗过的订单状态 。
		// 更改清洗的订单状态
		if (workInfo.getWork_type() != null && workInfo.getWork_type().equals("异常订单申诉")) {
			DsAbnormalOrderManager dsAbnormalOrderManager = (DsAbnormalOrderManager) SpringHelper
					.getBean("dsAbnormalOrderManager");
			DsAbnormalOrder dsAbnormalOrder = dsAbnormalOrderManager.queryDsAbnormalOrderBySN(workInfo.getOrder_sn());
			if (dsAbnormalOrder != null) {
				dsAbnormalOrder.setStatus(save_scoreRecordTotal.getCommit_status().toString());
				dsAbnormalOrder.setUpdatetime(workInfo.getUpdate_time());
				dsAbnormalOrderManager.saveDsAbnormalOrder(dsAbnormalOrder);
			}
		}

		// 修改门店状态
		if (workInfo.getWork_type() != null && workInfo.getWork_type().equals("门店选址审核")) {
			StoreDynamic storeDynamic = storeDynamicManager.findStoreDynamicByWorkId(workInfo.getOrder_sn());
			if (storeDynamic != null) {
				storeDynamic.setAuditor_status(Integer.parseInt(save_scoreRecordTotal.getCommit_status().toString()));
				storeDynamic.setUpdate_time(workInfo.getUpdate_time());
				if (storeDynamic.getAuditor_status() == 3) {
					if ((storeDynamic.getEstate() == null || storeDynamic.getEstate() == "")
							&& !"运营中".equals(storeDynamic.getEstate()) && !"待开业".equals(storeDynamic.getEstate())) {
						storeDynamic.setEstate("筹备中");
					}
					// 调用sync同步门店方法
					try {
						storeDynamicManager.syncStore(storeDynamic);
					} catch (Exception e) {
						e.printStackTrace();
					}
					storeDynamicManager.saveObject(storeDynamic);
					// 更新t_store
					storeManager.insertStoresyncDynamicStore(storeDynamic);
				}

			}
		}
		// 修改门店状态
		if (workInfo.getWork_type() != null && workInfo.getWork_type().equals("门店筹备审核")) {
			StoreDocumentInfoManager storeDocumentInfoManager = (StoreDocumentInfoManager) SpringHelper
					.getBean("storeDocumentInfoManager");
			StoreDocumentInfo storeDocumentInfo = storeDocumentInfoManager
					.findStoreDocumentInfoByWorkId(workInfo.getOrder_sn());
			if (storeDocumentInfo != null) {
				storeDocumentInfo
						.setAudit_status(Integer.parseInt(save_scoreRecordTotal.getCommit_status().toString()));
				storeDocumentInfo.setUpdate_time(workInfo.getUpdate_time());
				if (storeDocumentInfo.getAudit_status() == 3) {
					AttachmentManager attachmentManager = (AttachmentManager) SpringHelper.getBean("attachmentManager");
					Attachment attachment = attachmentManager
							.findAttachmentByStoreIdType(storeDocumentInfo.getStore_id(), 7);
					if (attachment != null) {
						StoreDynamic storeDynamic = storeDynamicManager
								.findStoreDynamic(storeDocumentInfo.getStore_id());
						if (storeDynamic != null) {
							if (!"运营中".equals(storeDynamic.getEstate())) {
								storeDynamic.setEstate("待开业");
							}
							storeDynamicManager.saveObject(storeDynamic);
							storeManager.insertStoresyncDynamicStore(storeDynamic);
						}
					}
				}
				storeDocumentInfoManager.saveObject(storeDocumentInfo);
			}
		}

		return save_scoreRecordTotal;
	}

	public void pushSPMsg(List<WorkInfo> workInfos, Long type) {
		if (workInfos.size() > 0) {
			MessageNewManager messageNewManager = (MessageNewManager) SpringHelper.getBean("messageNewManager");
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			UserDTO userDTO = userManager.getCurrentUserDTO();

			StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
			Store store = storeManager.findStore(workInfos.get(0).getStore_id());
			String user_id = "";
			if (store != null && store.getSkid() != null) {
				user_id = store.getSkid().toString();
			}
			if (type != null && type.equals(Long.parseLong("2"))) {
				// 预留 驳回 与 审批通过 发的消息 不一致的情况。默认是一致的。 修改 type
				// abnormal_order_approve
				messageNewManager.sendMessage_common(userDTO.getId().toString(), user_id, "abnormal_order_approve",
						workInfos);
			} else if (type != null && type.equals(Long.parseLong("3"))) {
				messageNewManager.sendMessage_common(userDTO.getId().toString(), user_id, "abnormal_order_approve",
						workInfos);
			} else {
				messageNewManager.sendMessage_common(userDTO.getId().toString(), user_id, "abnormal_order_approve",
						workInfos);
			}
		}
	}

	private String initApproveIds(WorkInfo workInfo, DistCityManager distCityManager, List<User> users, String step) {
		List<User> commitUsers = new ArrayList<User>();
		// 取得当前登录人的用户组
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		FlowConfigManager flowConfigManager = (FlowConfigManager) SpringHelper.getBean("flowConfigManager");
		UserGroupManager userGroupManager = (UserGroupManager) SpringHelper.getBean("userGroupManager");
		FlowConfig flowConfig = flowConfigManager.queryFlowConfigByWorkName(workInfo.getWork_type());
		// UserDTO userDTO = userManager.getCurrentUserDTO();
		Long group_id = null;
		if (step.equals("1")) {
			// 第一审批组
			group_id = flowConfig.getFirst_usergroup_id();
		} else if (step.equals("2")) {
			group_id = flowConfig.getSecond_usergroup_id();
		} else if (step.equals("3")) {
			group_id = flowConfig.getThird_usergroup_id();
		}
		UserGroup userGroup = (UserGroup) userGroupManager.getObject(group_id);
		if (userGroup.getCode().equals("QYJL")) {
			Store store = (Store) storeManager.getObject(workInfo.getStore_id());
			if (users != null && users.size() > 0) {
				for (Object o : users) {
					User u = (User) o;
					if (u.getId().equals(store.getRmid())) {
						commitUsers.add(u);
					}
				}
			}
		} else if (userGroup.getCode().equals("CSMDKQSPJSZ")) {// 线上 MDKQSPJSZ
																// 本地KQGLJSZ
			if (users != null && users.size() > 0) {
				for (Object o : users) {
					User u = (User) o;
					List<DistCity> distCities = distCityManager.queryDistCityByUserIdCity(u.getId(),
							workInfo.getCityname());
					if (distCities != null && distCities.size() > 0) {
						commitUsers.add(u);
					}
				}
			}
		}
		// commitUsers为提交的HR
		String first_ids = "";
		String first_ids_names = "";
		if (commitUsers != null && commitUsers.size() > 0) {
			for (User u : commitUsers) {
				first_ids += u.getId() + ",";
				first_ids_names += u.getName() + ",";
			}
			first_ids = first_ids.substring(0, first_ids.length() - 1);
			first_ids_names = first_ids_names.substring(0, first_ids_names.length() - 1);
		}
		return first_ids;
	}

	public void setCellValue(Row obj_row, int nCellIndex, Object value) {
		Cell cell = obj_row.createCell(nCellIndex);
		cell.setCellStyle(getCellStyle_common());
		if (value == null || value.equals("")) {
			value = null;
			return;
		}
		cell.setCellValue(new HSSFRichTextString(value == null ? null : value.toString()));
	}

	public void setLeftCellValue(Row obj_row, int nCellIndex, Object value) {
		Cell cell = obj_row.createCell(nCellIndex);
		cell.setCellStyle(getLeftCellStyle_common());
		cell.setCellValue(new HSSFRichTextString(value == null ? null : value.toString()));
	}

	public void setStrongCenterCellValue(Row obj_row, int nCellIndex, Object value) {
		Cell cell = obj_row.createCell(nCellIndex);
		cell.setCellStyle(getStrongCenterCellStyle_common());
		cell.setCellValue(new HSSFRichTextString(value == null ? null : value.toString()));
	}

	public CellStyle getCellStyle_common() {
		return cellStyle_common;
	}

	public CellStyle getLeftCellStyle_common() {
		return cellLeftStyle_common;
	}

	public CellStyle getStrongCenterCellStyle_common() {
		return cellStrongCenterStyle_common;
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

	// 无边框居左
	public void setLeftCellStyle_common(Workbook wb) {
		cellLeftStyle_common = wb.createCellStyle();
		cellLeftStyle_common.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平居
		cellLeftStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 垂直居中
		cellLeftStyle_common.setWrapText(true);// 设置自动换行
	}

	// 加粗 居中
	public void setStrongCenterCellStyle_common(Workbook wb) {
		cellStrongCenterStyle_common = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cellStrongCenterStyle_common.setFont(font);
		cellStrongCenterStyle_common.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居
		cellStrongCenterStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
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
	public Map<String, Object> queryScoreRecordTotalByCheck(QueryConditions conditions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveScoreRecordForExcel(String work_month, String cityname) {
		// TODO Auto-generated method stub
		return null;
	}

}
