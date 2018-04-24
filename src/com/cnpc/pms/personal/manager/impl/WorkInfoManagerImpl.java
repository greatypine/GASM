package com.cnpc.pms.personal.manager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.FlowConfig;
import com.cnpc.pms.personal.entity.FlowDetail;
import com.cnpc.pms.personal.entity.ScoreRecordTotal;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreDynamic;
import com.cnpc.pms.personal.entity.WorkInfo;
import com.cnpc.pms.personal.manager.DistCityManager;
import com.cnpc.pms.personal.manager.FlowConfigManager;
import com.cnpc.pms.personal.manager.FlowDetailManager;
import com.cnpc.pms.personal.manager.ScoreRecordTotalManager;
import com.cnpc.pms.personal.manager.StoreDynamicManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.WorkInfoManager;

//发起任务主列表
public class WorkInfoManagerImpl extends BizBaseCommonManager implements WorkInfoManager{
	
	//根据门店及月份查询 是否审批通过
	@Override
	public List<WorkInfo> queryWorkInfosByAdopt(Long store_id,String work_month,String work_type){
		StringBuilder sb_where = new StringBuilder();
		if(store_id!=null&&work_month!=null&&work_type!=null){
			sb_where.append(" work_type='"+work_type+"' ");
			sb_where.append(" and work_month='"+work_month+"' ");
			sb_where.append(" and store_id="+store_id+" ");
		}else{
			//取得当前登录人城市
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			List<DistCity> distCityList = userManager.getCurrentUserCity();
			String cityssql = "";
			if(distCityList!=null&&distCityList.size()>0){
				for(DistCity d:distCityList){
					cityssql += "'"+d.getCityname()+"',";
				}
				cityssql=cityssql.substring(0, cityssql.length()-1);
			}
			sb_where.append(" work_type='"+work_type+"' ");
			sb_where.append(" and work_month='"+work_month+"' ");
			if(cityssql!=null&&cityssql.length()>0){
				sb_where.append(" and cityname in("+cityssql+")");
			}else{
				sb_where.append(" and 1=0 ");
			}
		}
		IFilter iFilter =FilterFactory.getSimpleFilter(sb_where.toString());
		List<WorkInfo> re_lst_user = null;
		List<WorkInfo> lst_user = (List<WorkInfo>) this.getList(iFilter);//组下的所有人
		if(lst_user!=null&&lst_user.size()>0){
			re_lst_user = new ArrayList<WorkInfo>();
			if(store_id!=null){
				for(WorkInfo wi:lst_user){
					if(wi.getCommit_status().equals(Long.parseLong("3"))){
						re_lst_user.add(wi);
					}
				}
			}else{
				for(WorkInfo wi:lst_user){
					if(wi.getCommit_status().equals(Long.parseLong("3"))||wi.getCommit_status().equals(Long.parseLong("1"))){
						re_lst_user.add(wi);
					}
				}
			}
		}
		return re_lst_user;
	}
	
	//提交更新状态
	@Override
	public WorkInfo updateCommitStatus(Long id){
		WorkInfo workInfo = (WorkInfo) this.getObject(id);
		//查询审批人
		String work_name = workInfo.getWork_type();
		FlowConfigManager flowConfigManager = (FlowConfigManager) SpringHelper.getBean("flowConfigManager");
		FlowConfig flowConfig = flowConfigManager.queryFlowConfigByWorkName(work_name);
		Long first_group_id = flowConfig.getFirst_usergroup_id();//第一审批人组
		
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		DistCityManager distCityManager = (DistCityManager) SpringHelper.getBean("distCityManager");
		IFilter iFilter =FilterFactory.getSimpleFilter("pk_usergroup="+first_group_id);
		List<?> lst_user = userManager.getList(iFilter);//组下的所有人
		//城市权限分配中的城市去比对。
		List<User> commitUsers = new ArrayList<User>();
		if(lst_user!=null&&lst_user.size()>0){
			for(Object o:lst_user){
				User u = (User) o;
				List<DistCity> distCities = distCityManager.queryDistCityByUserIdCity(u.getId(), workInfo.getCityname());
				if(distCities!=null&&distCities.size()>0){
					commitUsers.add(u);
				}
			}
		}
		
		//commitUsers为提交的HR
		String first_ids = "";
		String first_ids_names = "";
		if(commitUsers!=null&&commitUsers.size()>0){
			for(User u :commitUsers){
				first_ids +=u.getId()+",";
				first_ids_names+=u.getName()+",";
			}
			first_ids = first_ids.substring(0,first_ids.length()-1);
			first_ids_names = first_ids_names.substring(0,first_ids_names.length()-1);
		}
		
		workInfo.setCurr_appro_id(first_ids);
		workInfo.setCurr_appro_id_name(first_ids_names);
		
		workInfo.setCommit_status(Long.parseLong("1"));
		
		UserDTO userDTO = userManager.getCurrentUserDTO();
		workInfo.setSubmit_user_id(userDTO.getId());
		java.util.Date date = new java.util.Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		workInfo.setCommit_date(sdate);
		
		preSaveObject(workInfo);
		saveObject(workInfo);
		return workInfo;
	}
	
	
	//保存
	@Override
    public WorkInfo saveWorkInfo(WorkInfo workInfo){
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		StoreDynamicManager storeDynamicManager = (StoreDynamicManager)SpringHelper.getBean("storeDynamicManager");
		UserDTO userDTO = userManager.getCurrentUserDTO();
		
		workInfo.setUser_id(userDTO.getId());
		workInfo.setCommit_status(Long.parseLong("0"));
		if(workInfo.getStore_id()==null){
			workInfo.setStore_id(userDTO.getStore_id());
		}
		//保存当前发起人的门店城市（未完成）
		StoreDynamic storeDynamic = storeDynamicManager.findStoreDynamic(workInfo.getStore_id());
		workInfo.setStore_name(storeDynamic.getName());
		workInfo.setCityname(storeDynamic.getCityName());
		
    	preSaveObject(workInfo);
    	this.saveObject(workInfo);
    	return workInfo;
    }
	
	 @Override
	    public Map<String, Object> queryWorkInfoList(QueryConditions condition) {
		    UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		    StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		    StoreDynamicManager storeDynamicManager = (StoreDynamicManager) SpringHelper.getBean("storeDynamicManager");
		    FlowConfigManager flowConfigManager = (FlowConfigManager) SpringHelper.getBean("flowConfigManager");
		    
			Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
			PageInfo pageInfo = condition.getPageinfo();
			String store_name = null;
			String work_type = null;
			for(Map<String, Object> map : condition.getConditions()){
				if("store_name".equals(map.get("key"))&&map.get("value")!=null){//查询条件
					store_name = map.get("value").toString();
				}
				if("work_type".equals(map.get("key"))&&map.get("value")!=null){//查询条件
					work_type = map.get("value").toString();
				}
				
			}
			List<?> lst_data = null;
			FSP fsp = new FSP();
			fsp.setSort(SortFactory.createSort("id", ISort.DESC));
			StringBuffer sbfCondition = new StringBuffer();
			sbfCondition.append(" 1=1 ");
			UserDTO userDTO = userManager.getCurrentUserDTO();
			//sbfCondition.append(" and user_id = '"+userDTO.getId()+"'");
			//判断显示哪些任务
			if(userDTO.getUsergroup().getCode().equals("MDXZTXJSZ")||userDTO.getUsergroup().getCode().equals("CSMDGLZ")){//门店选址填写角色组、城市门店管理组 
				sbfCondition.append(" and work_type ='门店选址审核'");
				List<DistCity> lst_citys = userManager.getCurrentUserCity();
				//填写人 分城市  按城市权限分配查询 
				if(lst_citys!=null&&lst_citys.size()>0){
					String citynames = "'"+lst_citys.get(0).getCityname()+"'";
					for(int i=1;i<lst_citys.size();i++){
						String tname = ",'"+lst_citys.get(i).getCityname()+"'";
						citynames+=tname;
					}
					sbfCondition.append(" and cityname in ("+citynames+")");
				}else{
					sbfCondition.append(" and 1=0 ");
				}
			}else if(userDTO.getUsergroup().getCode().equals("MDCBTXJSZ")){
				//门店筹备填写角色组
				sbfCondition.append(" and work_type='门店筹备审核'");
				List<DistCity> lst_citys= userManager.getCurrentUserCity();
				//填写人 分城市 按城市权限分配查询
				if(lst_citys!=null&&lst_citys.size()>0){
					String citynames = "'"+lst_citys.get(0).getCityname()+"'";
					for(int i=1;i<lst_citys.size();i++){
						String tname = ",'"+lst_citys.get(i).getCityname()+"'";
						citynames+=tname;
					}
					sbfCondition.append(" and cityname in("+citynames+")");
				}else{
					//sbfCondition
					sbfCondition.append(" and 1=0 ");
				}
				
			}else if(userDTO.getUsergroup().getCode().equals("QYJL")){
				sbfCondition.append(" and work_type ='门店选址审核'");
				//区域经理 可以查看自己所管理门店
				//到得当前登录人 userDTO 已有
				//根据登录人ID 去查询t_store表中的rmid
				List<Store>lst_storeList = storeManager.findStoreByrmid(userDTO.getId());
				//取得store_id拼成  id,id,id的形式 
				if(lst_storeList!=null&&lst_storeList.size()>0){
					String ids = lst_storeList.get(0).getStore_id().toString();
					for(int i=1;i<lst_storeList.size();i++){
						ids+=","+lst_storeList.get(i).getStore_id();
					}
					sbfCondition.append(" and store_id in ("+ids+")");
				}
				//sbfCondition.append   store_id in (id,id,id,id)
			}else{
				sbfCondition.append(" and store_id ="+userDTO.getStore_id());
			}
			
			
			
			if(store_name!=null){
				Long store_id = null;
				Store store = storeManager.findStoreByName(store_name);
				if(store!=null){
					store_id = store.getStore_id();
				}
				sbfCondition.append(" and store_id ="+store_id);
			}
			if(work_type!=null){
				sbfCondition.append(" and work_type like '%"+work_type+"%'");
			}
			
			IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition.toString());
			fsp.setPage(pageInfo);
			fsp.setUserFilter(iFilter);
			lst_data = this.getList(fsp);

			List<WorkInfo> workInfos = new ArrayList<WorkInfo>();
			if(lst_data!=null&&lst_data.size()>0){
				for(int i=0;i<lst_data.size();i++){
					WorkInfo workInfo = (WorkInfo) lst_data.get(i);
					User u = userManager.findUserById(workInfo.getUser_id());
					workInfo.setUser_id_name(u.getName());
					if(workInfo.getCommit_status().equals(Long.parseLong("0"))){
						workInfo.setStr_commit_status("保存");
					}else if(workInfo.getCommit_status().equals(Long.parseLong("1"))){
						workInfo.setStr_commit_status("审核中");
					}else if(workInfo.getCommit_status().equals(Long.parseLong("3"))){
						workInfo.setStr_commit_status("审核通过");
					}else if(workInfo.getCommit_status().equals(Long.parseLong("2"))){
						workInfo.setStr_commit_status("驳回");
					}
					String cur_appro_id = workInfo.getCurr_appro_id();
					List<User> users = null;
					if(cur_appro_id!=null&&cur_appro_id.length()>0){
						IFilter curFilter =FilterFactory.getSimpleFilter("id in("+cur_appro_id+")");
						users = (List<User>) userManager.getList(curFilter);
					}
					String uNames = "";
					String uGroupName = null;
					if(users!=null&&users.size()>0){
						for(User cuUser :users){
							uNames+=cuUser.getName()+",";
							if(uGroupName==null){
								uGroupName = cuUser.getUsergroup().getName();
							}
						}
						//此处判断 业务类型 取配置中的是否显示审批人 
						String typename = workInfo.getWork_type();
						FlowConfig flowConfig = flowConfigManager.queryFlowConfigByWorkName(typename);
						if(flowConfig!=null&&flowConfig.getIsshowname()!=null&&flowConfig.getIsshowname().equals(Long.parseLong("1"))){//1显示用户组  0显示用户名
							workInfo.setCurr_appro_group_name(uGroupName);
						}else{
							workInfo.setCurr_appro_group_name(uNames);
						}
						
						uNames = uNames.substring(0,uNames.length()-1);
					}
					if(workInfo.getCommit_status().equals(Long.parseLong("1"))){
						workInfo.setCurr_appro_id_name(uNames);
					}else{
						workInfo.setCurr_appro_id_name("无");
					}
					
					StoreDynamic storeDynamic = (StoreDynamic) storeDynamicManager.getObject(workInfo.getStore_id());
					workInfo.setStore_name(storeDynamic==null?"":storeDynamic.getName());
					
					workInfos.add(workInfo);
				}
			}
			
			returnMap.put("pageinfo", pageInfo);
			returnMap.put("header", "");
			returnMap.put("data", lst_data);
			return returnMap;
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	protected void preSaveObject(Object o) {
		if (o instanceof DataEntity) {
			User sessionUser = null;
			if (null != SessionManager.getUserSession()
					&& null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession()
						.getSessionData().get("user");
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
	public WorkInfo queryWorkInfoById(Long id){
		WorkInfo workInfo = (WorkInfo) this.getObject(id);
		//如果驳回 取得驳回信息
		if(workInfo!=null){
			ScoreRecordTotalManager scoreRecordTotalManager = (ScoreRecordTotalManager) SpringHelper.getBean("scoreRecordTotalManager");
			ScoreRecordTotal scoreRecordTotal = scoreRecordTotalManager.queryScoreRecordTotalByWorkId(workInfo.getId());
			workInfo.setRemark(scoreRecordTotal.getRemark());
		}
		return workInfo;
	}
	
	@Override
	public WorkInfo queryWorkInfoByOrderSN(String order_sn){
		IFilter iFilter =FilterFactory.getSimpleFilter("order_sn='"+order_sn+"'  ");
		List<?> lst_list = this.getList(iFilter);
		
		if(lst_list!=null&&lst_list.size()>0){
			return (WorkInfo) lst_list.get(0);
		}
		return null;
	}
	
	@Override
	public WorkInfo queryWorkInfoByOrderSNExtend(String order_sn){
		IFilter iFilter =FilterFactory.getSimpleFilter("order_sn='"+order_sn+"' and commit_status in (1,3) ");
		List<?> lst_list = this.getList(iFilter);
		
		if(lst_list!=null&&lst_list.size()>0){
			return (WorkInfo) lst_list.get(0);
		}
		return null;
	}
	
	/**
	 * 添加任务流程的接口   （任务发起接口）
	 * @param store_id    发起活动门店的store_id
	 * @param work_name   任务的名称 在流程配置中的 name名称  如“异常订单审核”“绩效打分录入” work_name暂未用
	 * @param work_month  任务发起的月份 格式 YYYY-MM
	 * @param work_id     发起任务的ID ，如异常订单中 order_sn 订单流水号（唯一）
	 * @param execute_type执行类型  是第一次提交  还是 驳回后提交    save  update
	 * @param reason 原因。如申诉原因。
	 */
	@Override
	public void StartFlow(Long store_id,String work_name,String work_month,String work_id,String execute_type,String reason){
		if(execute_type.contains("save")){
			WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
	  	    ScoreRecordTotalManager scoreRecordTotalManager = (ScoreRecordTotalManager) SpringHelper.getBean("scoreRecordTotalManager");
	  	    FlowDetailManager flowDetailManager = (FlowDetailManager) SpringHelper.getBean("flowDetailManager");
	  	    
	  	    WorkInfo workInfo = new WorkInfo();
	  	    workInfo.setApp_reason(reason);
	  	    workInfo.setOrder_sn(work_id);
	  	    workInfo.setWork_type(work_name);
	  	    workInfo.setWork_month(work_month);
	  	    
	  	    workInfo.setStore_id(store_id);
	  	    //保存任务列表
	  	    workInfoManager.saveWorkInfo(workInfo);
	  	    //提交任务
	  	    WorkInfo new_workInfo = workInfoManager.updateCommitStatus(workInfo.getId());
	  	    
	  	    //保存审批列表
	  	    ScoreRecordTotal scoreRecordTotal = new ScoreRecordTotal();
	  	    scoreRecordTotal.setCityname(new_workInfo.getCityname());
	  	    scoreRecordTotal.setCommit_date(new_workInfo.getCommit_date());
	  	    scoreRecordTotal.setCommit_status(new_workInfo.getCommit_status());
	  	    scoreRecordTotal.setStore_id(new_workInfo.getStore_id());
	  	    scoreRecordTotal.setWork_month(new_workInfo.getWork_month());
	  	    scoreRecordTotal.setWork_info_id(new_workInfo.getId());
	  	    scoreRecordTotal.setStr_work_info_id(new_workInfo.getWork_type());
	  	    preSaveObject(scoreRecordTotal);
	  	    scoreRecordTotalManager.saveObject(scoreRecordTotal);
	  	    
	  	    //将操作存入 审批记录表
      		FlowDetail flowDetail = new FlowDetail();
      		flowDetail.setWork_info_id(scoreRecordTotal.getWork_info_id());
      		flowDetail.setApprov_content(new_workInfo.getApp_reason());
      		flowDetailManager.saveFlowDetail(flowDetail);
	  	    
		}else if(execute_type.contains("update")){
			WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
	  	    ScoreRecordTotalManager scoreRecordTotalManager = (ScoreRecordTotalManager) SpringHelper.getBean("scoreRecordTotalManager");
	  	    FlowDetailManager flowDetailManager = (FlowDetailManager) SpringHelper.getBean("flowDetailManager");
	  	    
	  	    WorkInfo workInfo = workInfoManager.queryWorkInfoByOrderSN(work_id);
	  	    workInfo.setApp_reason(reason);
	  	    workInfo.setOrder_sn(work_id);
	  	    workInfo.setWork_type(work_name);
	  	    workInfo.setWork_month(work_month);
	  	    
	  	    workInfo.setStore_id(store_id);
	  	    //保存任务列表
	  	    workInfoManager.saveWorkInfo(workInfo);
	  	    //提交任务
	  	    WorkInfo new_workInfo = workInfoManager.updateCommitStatus(workInfo.getId());
	  	    
	  	    //保存审批列表
	  	    ScoreRecordTotal scoreRecordTotal = scoreRecordTotalManager.queryScoreRecordTotalByWorkId(workInfo.getId());
	  	    scoreRecordTotal.setCityname(new_workInfo.getCityname());
	  	    scoreRecordTotal.setCommit_date(new_workInfo.getCommit_date());
	  	    scoreRecordTotal.setCommit_status(new_workInfo.getCommit_status());
	  	    scoreRecordTotal.setStore_id(new_workInfo.getStore_id());
	  	    scoreRecordTotal.setWork_month(new_workInfo.getWork_month());
	  	    scoreRecordTotal.setWork_info_id(new_workInfo.getId());
	  	    scoreRecordTotal.setStr_work_info_id(new_workInfo.getWork_type());
	  	    preSaveObject(scoreRecordTotal);
	  	    scoreRecordTotalManager.saveObject(scoreRecordTotal);
	  	    
	  	    
	  	    //将操作存入 审批记录表
      		FlowDetail flowDetail = new FlowDetail();
      		flowDetail.setWork_info_id(scoreRecordTotal.getWork_info_id());
      		flowDetail.setApprov_content(new_workInfo.getApp_reason());
      		flowDetailManager.saveFlowDetail(flowDetail);
	  	    
	  	    
		}
		
		//流程走完，设置原表的状态
		//。。。。。
		
		
		
		
		
	}
	
	
	
	
	
	
	
}
