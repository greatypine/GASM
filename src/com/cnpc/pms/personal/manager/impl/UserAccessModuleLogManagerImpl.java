package com.cnpc.pms.personal.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.UserAccessModuleLogDao;
import com.cnpc.pms.personal.dto.UserAccessModuleLogDTO;
import com.cnpc.pms.personal.entity.UserAccessModuleLog;
import com.cnpc.pms.personal.manager.UserAccessModuleLogManager;

public class UserAccessModuleLogManagerImpl extends BaseManagerImpl implements UserAccessModuleLogManager {

	@Override
	public UserAccessModuleLog saveAccessModuleLog(User user){
		UserAccessModuleLog userAccessModuleLog = new UserAccessModuleLog();
		UserDTO userDTO = getCurrentUserInfo();
		java.util.Date date = new java.util.Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		userAccessModuleLog.setUserId(userDTO.getId());
		userAccessModuleLog.setUserName(userDTO.getName());
		userAccessModuleLog.setAccessModuleDate(sdate); //访问时间
		userAccessModuleLog.setUserGroupName(userDTO.getUsergroup().getName());
		userAccessModuleLog.setUserGroupId(userDTO.getUsergroup().getId());
		
		//传的值
		userAccessModuleLog.setAccessSystem(user.getBusinessType());//哪个系统
		userAccessModuleLog.setAccessModule(user.getModulefunc());//哪个功能
		userAccessModuleLog.setCityName(user.getCitynames());//城市
		userAccessModuleLog.setLoginIP(user.getLoginip());//ip
		userAccessModuleLog.setClient_type(user.getUsertype()==0?"PC":"APP");
		userAccessModuleLog.setLoginToken(user.getToken());//token
		
		saveObject(userAccessModuleLog);
		return userAccessModuleLog;
		
	}
	
	
	
	
	private UserDTO getCurrentUserInfo(){
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		return userManager.getCurrentUserDTO();
	}
	
	@Override
	public Map<String, Object> getUserAccessModuleLogList(QueryConditions condition){
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		StringBuffer cond = new StringBuffer();
		for(Map<String, Object> map : condition.getConditions()){
			if("date".equals(map.get("key"))&&map.get("value")!=null){//组织机构
				cond.append(map.get("value").toString());
			}
		}
		
		Sort sort = condition.getSortinfo();
		if(sort!=null){
			cond.append(" order by ");
			cond.append(sort.getVariableName());
			if(sort.getDirection()==1){
				cond.append(" desc ");
			}else{
				cond.append(" asc ");
			}
		}
		
		
		UserAccessModuleLogDao userAccessModuleLogDao = (UserAccessModuleLogDao) SpringHelper.getBean(UserAccessModuleLogDao.class.getName());
		
		List<UserAccessModuleLogDTO> rt_dto = new ArrayList<UserAccessModuleLogDTO>();
		List<Map<String,Object>> iseraccessmoduleList = userAccessModuleLogDao.getUserAccessModuleLogList(cond.toString(), pageInfo);
		
		
		for(Object map : iseraccessmoduleList){
			UserAccessModuleLogDTO userAccessModuleLogDTO = new UserAccessModuleLogDTO();
			userAccessModuleLogDTO.setAccessSystem(((Object[])map)[0].toString());//用户组
			userAccessModuleLogDTO.setUserGroupName(((Object[])map)[1].toString());//用户组
			
			userAccessModuleLogDTO.setOrder_record(((Object[])map)[2].toString());//订单档案
			userAccessModuleLogDTO.setEmp_record(((Object[])map)[3].toString());//员工档案
			
			userAccessModuleLogDTO.setOther1(((Object[])map)[4].toString());//其它1
			userAccessModuleLogDTO.setOther2(((Object[])map)[5].toString());//其它1
			userAccessModuleLogDTO.setOther3(((Object[])map)[6].toString());//其它1
			userAccessModuleLogDTO.setOther4(((Object[])map)[7].toString());//其它1
			
			rt_dto.add(userAccessModuleLogDTO);
		}
		
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", rt_dto);
		return returnMap;
	}
	
	
	
	
}
