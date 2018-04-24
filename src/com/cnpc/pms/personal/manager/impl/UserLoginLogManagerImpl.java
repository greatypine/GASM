package com.cnpc.pms.personal.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Spring;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.dao.UserLoginDAO;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.UserLoginLogDao;
import com.cnpc.pms.personal.dto.UserLoginLogDTO;
import com.cnpc.pms.personal.entity.UserLoginLog;
import com.cnpc.pms.personal.manager.UserLoginLogManager;


public class UserLoginLogManagerImpl extends BaseManagerImpl implements UserLoginLogManager {

	/**
	 * 保存登录记录
	 */
	@Override
	public UserLoginLog saveUserLoginLog(User userInfo) {
		UserDTO userDTO = getCurrentUserInfo();
		java.util.Date date = new java.util.Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		UserLoginLog userLoginLog = new UserLoginLog();
		userLoginLog.setUserId(userDTO.getId());
		userLoginLog.setUserName(userDTO.getName());
		userLoginLog.setLoginDate(sdate); //登录时间
		userLoginLog.setUserGroupName(userDTO.getUsergroup().getName());
		userLoginLog.setUserGroupId(userDTO.getUsergroup().getId());
		userLoginLog.setUserLoginType("登录");
		
		userLoginLog.setLoginIP(userInfo.getLoginip());
		userLoginLog.setLoginToken(userInfo.getToken());
		userLoginLog.setCityName(userInfo.getCitynames());
		
		this.saveObject(userLoginLog);
		return userLoginLog;
	}
	//保存访问五个系统记录
	@Override
	public UserLoginLog saveUserAccessLog(User userInfo) {
		UserDTO userDTO = getCurrentUserInfo();
		java.util.Date date = new java.util.Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		UserLoginLog userLoginLog = new UserLoginLog();
		userLoginLog.setUserId(userDTO.getId());
		userLoginLog.setUserName(userDTO.getName());
		userLoginLog.setAccessDate(sdate);  //访问时间
		userLoginLog.setUserGroupName(userDTO.getUsergroup().getName());
		userLoginLog.setUserGroupId(userDTO.getUsergroup().getId());
		userLoginLog.setUserLoginType("访问系统");
		
		userLoginLog.setAccessSystem(userInfo.getBusinessType());//用BusinessType存一下访问了哪个系统名
		userLoginLog.setLoginIP(userInfo.getLoginip());
		userLoginLog.setLoginToken(userInfo.getToken());
		userLoginLog.setCityName(userInfo.getCitynames());
		
		this.saveObject(userLoginLog);
		return userLoginLog;
	}
	
	    //保存手机访问记录
		@Override
		public UserLoginLog saveAppUserAccessLog(User userInfo) {
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			UserLoginLog userLoginLog = new UserLoginLog();
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			User userDTO = (User) userManager.getObject(userInfo.getId());
			if(userDTO!=null){
				userLoginLog.setUserId(userDTO.getId());
				userLoginLog.setUserName(userDTO.getName());
				userLoginLog.setAccessDate(sdate);  //访问时间
				userLoginLog.setUserGroupName(userDTO.getUsergroup().getName());
				userLoginLog.setUserGroupId(userDTO.getUsergroup().getId());
				userLoginLog.setUserLoginType("APP访问功能");
				
				userLoginLog.setAccessSystem(userInfo.getBusinessType());//用BusinessType存一下访问了哪个系统名
				userLoginLog.setLoginIP(userInfo.getLoginip());
				userLoginLog.setLoginToken(userInfo.getToken());
				userLoginLog.setCityName(userInfo.getCitynames());
				
				this.saveObject(userLoginLog);
			}
			return userLoginLog;
		}
	
	//保存刷新
	@Override
	public UserLoginLog saveUserRefreshLog(User userInfo) {
		UserDTO userDTO = getCurrentUserInfo();
		java.util.Date date = new java.util.Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		UserLoginLog userLoginLog = new UserLoginLog();
		userLoginLog.setUserId(userDTO.getId());
		userLoginLog.setUserName(userDTO.getName());
		userLoginLog.setRefreshDate(sdate);
		userLoginLog.setUserGroupName(userDTO.getUsergroup().getName());
		userLoginLog.setUserGroupId(userDTO.getUsergroup().getId());
		userLoginLog.setUserLoginType("刷新首页");
		
		userLoginLog.setLoginIP(userInfo.getLoginip());
		userLoginLog.setLoginToken(userInfo.getToken());
		userLoginLog.setCityName(userInfo.getCitynames());
		
		this.saveObject(userLoginLog);
		return userLoginLog;
	}
	
	private UserDTO getCurrentUserInfo(){
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		return userManager.getCurrentUserDTO();
	}
	
	
	
	/**
	 * 保存手机登录记录
	 */
	@Override
	public UserLoginLog saveUserAppLoginLog(User userInfo) {
		java.util.Date date = new java.util.Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		UserLoginLog userLoginLog = new UserLoginLog();
		try {
			userLoginLog.setUserId(userInfo.getId());
			userLoginLog.setUserName(userInfo.getName());
			userLoginLog.setLoginDate(sdate); //登录时间
			userLoginLog.setUserGroupName(userInfo.getUsergroup().getName());
			userLoginLog.setUserGroupId(userInfo.getUsergroup().getId());
			userLoginLog.setUserLoginType("APP登录");
			userLoginLog.setClient_type("APP");
			
			userLoginLog.setLoginIP(userInfo.getLoginip());
			userLoginLog.setLoginToken(userInfo.getToken());
			userLoginLog.setCityName(userInfo.getCitynames());
			
			this.saveObject(userLoginLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userLoginLog;
	}
	
	
	
	@Override
	public Map<String, Object> getUserLoginLogList(QueryConditions condition){
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
		
		
		UserLoginLogDao userLoginLogDao = (UserLoginLogDao) SpringHelper.getBean(UserLoginLogDao.class.getName());
		
		List<UserLoginLogDTO> rt_dto = new ArrayList<UserLoginLogDTO>();
		List<Map<String,Object>> userLogMaps = userLoginLogDao.getUserLoginLogList(cond.toString(), pageInfo);
		
		List<Map<String,Object>> userLogUvMaps = userLoginLogDao.getUserLoginLogDayMonthUvList(cond.toString(), pageInfo);
		List<Map<String,Object>> userLogPvMaps = userLoginLogDao.getUserLoginLogDayMonthPvList(cond.toString(), pageInfo);
		
		for(Object map : userLogMaps){
			UserLoginLogDTO userLoginLogDTO = new UserLoginLogDTO();
			userLoginLogDTO.setUserGroupName(((Object[])map)[0].toString());
			
			String uvVal = "0/0/"+((Object[])map)[1].toString();
			for(Object uv:userLogUvMaps){
				String groupName = ((Object[])uv)[0].toString();
				if(groupName!=null&&groupName.equals(userLoginLogDTO.getUserGroupName())){
					uvVal = ((Object[])uv)[1].toString()+"/"+((Object[])uv)[2].toString()+"/"+((Object[])map)[1].toString();
					break;
				}
			}
			
			String pvVal = "0/0/"+((Object[])map)[2].toString();
			for(Object pv:userLogPvMaps){
				String groupName = ((Object[])pv)[0].toString();
				if(groupName!=null&&groupName.equals(userLoginLogDTO.getUserGroupName())){
					pvVal = ((Object[])pv)[1].toString()+"/"+((Object[])pv)[2].toString()+"/"+((Object[])map)[2].toString();
					break;
				}
			}
			
			userLoginLogDTO.setUv(uvVal);
			userLoginLogDTO.setPv(pvVal);
			
			/*userLoginLogDTO.setUv(((Object[])map)[1].toString());
			userLoginLogDTO.setPv(((Object[])map)[2].toString());*/
			
			userLoginLogDTO.setDaqweb(((Object[])map)[3].toString());
			userLoginLogDTO.setCrm(((Object[])map)[4].toString());
			userLoginLogDTO.setMap(((Object[])map)[5].toString());
			userLoginLogDTO.setService(((Object[])map)[6].toString());
			userLoginLogDTO.setReport(((Object[])map)[7].toString());
			
			rt_dto.add(userLoginLogDTO);
		}
		
		System.out.println(userLogMaps.size());
		
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", rt_dto);
		return returnMap;
	}
	
	
	
	
	
	
	@Override
	public Map<String, Object> getAppUserLoginLogList(QueryConditions condition){
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
		
		
		UserLoginLogDao userLoginLogDao = (UserLoginLogDao) SpringHelper.getBean(UserLoginLogDao.class.getName());
		
		List<UserLoginLogDTO> rt_dto = new ArrayList<UserLoginLogDTO>();
		List<Map<String,Object>> userLogMaps = userLoginLogDao.getAppUserLoginLogList(cond.toString(), pageInfo);
		
		List<Map<String,Object>> userLogUvMaps = userLoginLogDao.getAppUserLoginLogDayMonthUvList(cond.toString(), pageInfo);
		//List<Map<String,Object>> userLogPvMaps = userLoginLogDao.getUserLoginLogDayMonthPvList(cond.toString(), pageInfo);
		
		
		for(Object map : userLogMaps){
			UserLoginLogDTO userLoginLogDTO = new UserLoginLogDTO();
			userLoginLogDTO.setUserGroupName(((Object[])map)[0].toString());
			
			String uvVal = "0/0/"+((Object[])map)[1].toString();
			for(Object uv:userLogUvMaps){
				String groupName = ((Object[])uv)[0].toString();
				if(groupName!=null&&groupName.equals(userLoginLogDTO.getUserGroupName())){
					uvVal = ((Object[])uv)[1].toString()+"/"+((Object[])uv)[2].toString()+"/"+((Object[])map)[1].toString();
					break;
				}
			}
			
			/*String pvVal = "0/0/"+((Object[])map)[2].toString();
			for(Object pv:userLogPvMaps){
				String groupName = ((Object[])pv)[0].toString();
				if(groupName!=null&&groupName.equals(userLoginLogDTO.getUserGroupName())){
					pvVal = ((Object[])pv)[1].toString()+"/"+((Object[])pv)[2].toString()+"/"+((Object[])map)[2].toString();
					break;
				}
			}
			*/
			userLoginLogDTO.setAppuv(uvVal);
			//userLoginLogDTO.setPv(pvVal);
			
			/*userLoginLogDTO.setUv(((Object[])map)[1].toString());
			userLoginLogDTO.setPv(((Object[])map)[2].toString());*/
			
			userLoginLogDTO.setAppmap(((Object[])map)[2].toString());
			userLoginLogDTO.setApparea(((Object[])map)[3].toString());
			userLoginLogDTO.setAppmessage(((Object[])map)[4].toString());
			userLoginLogDTO.setAppinput(((Object[])map)[5].toString());
			
			rt_dto.add(userLoginLogDTO);
		}
		
		
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", rt_dto);
		return returnMap;
	}
	
}
