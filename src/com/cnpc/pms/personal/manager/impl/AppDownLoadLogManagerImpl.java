package com.cnpc.pms.personal.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.dao.AppDownloadLogDao;
import com.cnpc.pms.personal.dao.UserLoginLogDao;
import com.cnpc.pms.personal.dto.AppDownloadLogDTO;
import com.cnpc.pms.personal.dto.UserLoginLogDTO;
import com.cnpc.pms.personal.entity.AppDownloadLog;
import com.cnpc.pms.personal.manager.AppDownLoadLogManager;



public class AppDownLoadLogManagerImpl extends BaseManagerImpl implements AppDownLoadLogManager {

	
	@Override
	public Map<String, Object> getAppDownloadLogList(QueryConditions condition){
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		StringBuffer cond = new StringBuffer();
		String downloadversion = null;
		for(Map<String, Object> map : condition.getConditions()){
			if("downloadVersion".equals(map.get("key"))&&map.get("value")!=null){//组织机构
				downloadversion = map.get("value").toString();
			}
		}
		cond.append(" 1=1 ");
		if(downloadversion!=null){
			cond.append(" and b.downloadVersion like '%"+downloadversion+"%'");
		}
		
		AppDownloadLogDao appDownloadLogDao = (AppDownloadLogDao) SpringHelper.getBean(AppDownloadLogDao.class.getName());
		List<AppDownloadLogDTO> rt_dto = new ArrayList<AppDownloadLogDTO>();
		List<Map<String,Object>> downloadLogMaps = appDownloadLogDao.getAppDownloadLogList(cond.toString(), pageInfo);
		for(Object map : downloadLogMaps){
			AppDownloadLogDTO appDownloadLogDTO = new AppDownloadLogDTO();
			appDownloadLogDTO.setDownloadVersion(((Object[])map)[0].toString());
			appDownloadLogDTO.setDownloadtype(((Object[])map)[1].toString());
			appDownloadLogDTO.setCurdays(((Object[])map)[2].toString());
			appDownloadLogDTO.setCurmonths(((Object[])map)[3].toString());
			appDownloadLogDTO.setTotalcounts(((Object[])map)[4].toString());
			rt_dto.add(appDownloadLogDTO);
		}
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", rt_dto);
		return returnMap;
	}
	
	@Override
	public AppDownloadLog saveAppDownLoadLog(AppDownloadLog appDownloadLog) {
		preSaveObject(appDownloadLog);
		appDownloadLog.setDownloadDate(appDownloadLog.getCreate_time());
		this.saveObject(appDownloadLog);
		return appDownloadLog;
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
