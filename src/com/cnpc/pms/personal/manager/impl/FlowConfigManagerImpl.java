package com.cnpc.pms.personal.manager.impl;

import java.util.ArrayList;
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
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.entity.FlowConfig;
import com.cnpc.pms.personal.entity.WorkInfo;
import com.cnpc.pms.personal.manager.FlowConfigManager;
import com.cnpc.pms.personal.manager.WorkInfoManager;


public class FlowConfigManagerImpl extends BizBaseCommonManager implements FlowConfigManager{
	
	
	@Override
	public List<?> queryUserListByGroupId(Long user_group_id){
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		IFilter repFilter =FilterFactory.getSimpleFilter(" pk_usergroup="+user_group_id+" and disabledFlag=1 ");
    	List<?> rep_list = userManager.getList(repFilter);
		return rep_list;
	}
	
	@Override
	public FlowConfig queryFlowConfigByWorkName(String work_name){
		IFilter repFilter = FilterFactory.getSimpleFilter(" work_name='"+work_name+"'");
		List<?> rep_list = this.getList(repFilter);
		FlowConfig flowConfig = null;
		if(rep_list!=null){
			flowConfig = (FlowConfig) rep_list.get(0);
		}
		return flowConfig;
	}
	
	
	@Override
	public List<?> queryAllFlowConfigs(){
		List<?> rep_list = this.getList();
		return rep_list;
	}
	
	
	@Override
    public FlowConfig saveFlowConfig(FlowConfig flowConfig){
    	preSaveObject(flowConfig);
    	this.saveObject(flowConfig);
    	return flowConfig;
    }
	
	@Override
	public FlowConfig queryFlowConfigObjectById(Long id){
		FlowConfig flowConfig = (FlowConfig) this.getObject(id);
		return flowConfig;
	}
	
	
	@Override
    public Map<String, Object> queryFlowConfigList(QueryConditions condition) {
	    UserGroupManager userGroupManager = (UserGroupManager) SpringHelper.getBean("userGroupManager");
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		String work_name = null;
		for(Map<String, Object> map : condition.getConditions()){
			if("work_name".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				work_name = map.get("value").toString();
			}
		}
		List<?> lst_data = null;
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("id", ISort.DESC));
		StringBuffer sbfCondition = new StringBuffer();
		sbfCondition.append(" 1=1 ");
		
		if(work_name!=null){
			sbfCondition.append(" and work_name like '%"+work_name+"%'");
		}
		
		IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition.toString());
		fsp.setPage(pageInfo);
		fsp.setUserFilter(iFilter);
		lst_data = this.getList(fsp);

		List<FlowConfig> flowConfigs = new ArrayList<FlowConfig>();
		if(lst_data!=null&&lst_data.size()>0){
			for(int i=0;i<lst_data.size();i++){
				FlowConfig flowConfig = (FlowConfig) lst_data.get(i);
				UserGroup start_userGroup = (UserGroup) userGroupManager.getObject(flowConfig.getStart_usergroup_id());
				UserGroup first_userGroup = (UserGroup) userGroupManager.getObject(flowConfig.getFirst_usergroup_id());
				if(flowConfig.getSecond_usergroup_id()!=null){
					UserGroup second_userGroup = (UserGroup) userGroupManager.getObject(flowConfig.getSecond_usergroup_id());
					flowConfig.setSecond_usergroup_id_name(second_userGroup.getName());
				}
				if(flowConfig.getThird_usergroup_id()!=null){
					UserGroup third_userGroup = (UserGroup) userGroupManager.getObject(flowConfig.getThird_usergroup_id());
					flowConfig.setThird_usergroup_id_name(third_userGroup.getName());
				}
				flowConfig.setStart_usergroup_id_name(start_userGroup.getName());
				flowConfig.setFirst_usergroup_id_name(first_userGroup.getName());
				flowConfigs.add(flowConfig);
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
}
