package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.BannerInfo;
import com.cnpc.pms.personal.manager.BannerInfoManager;

public class BannerInfoManagerImpl extends BizBaseCommonManager implements BannerInfoManager {

	
	@Override
	public BannerInfo queryBannerInfoById(Long id){
		BannerInfo bannerInfo = (BannerInfo) this.getObject(id);
		if(bannerInfo!=null){
			return bannerInfo;
		}
		return null;
	}
	
	@Override
	public void deleteBannerInfo(Long id){
		BannerInfo bannerInfo = (BannerInfo) this.getObject(id);
		this.removeObject(bannerInfo);
	}
	
	@Override
	public BannerInfo saveBannerInfo(BannerInfo bannerInfo) {
		preSaveObject(bannerInfo);
		this.saveObject(bannerInfo);
		return bannerInfo;
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
