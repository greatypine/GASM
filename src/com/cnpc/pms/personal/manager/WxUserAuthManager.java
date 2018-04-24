package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.WxUserAuth;

public interface WxUserAuthManager extends IManager{

	public WxUserAuth saveWxUserAuth(WxUserAuth wxUserAuth);
	
	public WxUserAuth queryWxUserAuthByPhone(Long user_id,String mobilephone);
	
	public WxUserAuth queryWxUserAuthByPhoneCode(Long user_id,String mobilephone,String wx_code);
	
	public WxUserAuth updateWxUserAuth(WxUserAuth wxUserAuth);
}
