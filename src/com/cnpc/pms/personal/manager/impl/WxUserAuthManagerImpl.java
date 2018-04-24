package com.cnpc.pms.personal.manager.impl;

import java.util.List;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.WxUserAuth;
import com.cnpc.pms.personal.manager.WxUserAuthManager;

public class WxUserAuthManagerImpl extends BizBaseCommonManager implements WxUserAuthManager {

	@Override
	public WxUserAuth queryWxUserAuthByPhoneCode(Long user_id,String mobilephone,String wx_code){
   	 	IFilter iFilter =FilterFactory.getSimpleFilter("user_id="+user_id+" and mobilephone='"+mobilephone+"' and wx_code = '"+wx_code+"'");
   	 	List<WxUserAuth> lst_wxuser = (List<WxUserAuth>) this.getList(iFilter);
   	 	if(lst_wxuser!=null&&lst_wxuser.size()>0){
   	 		return lst_wxuser.get(0);
   	 	}
   	 	return null;
	}
	
	
	@Override
	public WxUserAuth queryWxUserAuthByPhone(Long user_id,String mobilephone){
   	 	IFilter iFilter =FilterFactory.getSimpleFilter("user_id ="+user_id+" and mobilephone='"+mobilephone+"'");
   	 	List<WxUserAuth> lst_wxuser = (List<WxUserAuth>) this.getList(iFilter);
   	 	if(lst_wxuser!=null&&lst_wxuser.size()>0){
   	 		return lst_wxuser.get(0);
   	 	}
   	 	return null;
	}
	
	
	@Override
	public WxUserAuth saveWxUserAuth(WxUserAuth wxUserAuth){
		preSaveObject(wxUserAuth);
		saveObject(wxUserAuth);
		return wxUserAuth;
	}
	
	
	@Override
	public WxUserAuth updateWxUserAuth(WxUserAuth wxUserAuth){
		WxUserAuth update_WxUserAuth = (WxUserAuth) this.getObject(wxUserAuth.getId());
		update_WxUserAuth.setAuthstatus(1L);
		update_WxUserAuth.setWx_code(wxUserAuth.getWx_code());
		update_WxUserAuth.setNickname(wxUserAuth.getNickname());
		preSaveObject(update_WxUserAuth);
		saveObject(update_WxUserAuth);
		return update_WxUserAuth;
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
