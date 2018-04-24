package com.cnpc.pms.personal.manager.impl;

import java.util.List;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.SiteSelection;
import com.cnpc.pms.personal.manager.SiteSelectionManager;


public class SiteSelectionManagerImpl extends BaseManagerImpl implements SiteSelectionManager {

	@Override
	public SiteSelection saveSiteSelection(SiteSelection selection) {
		preSaveObject(selection);
		saveObject(selection);
		return selection;
	}
	
	@Override
	public SiteSelection querySiteSelectionByPhone(String mobilephone){
		IFilter iFilter =FilterFactory.getSimpleFilter("mobilephone='"+mobilephone+"'");
		List<SiteSelection> rtList = (List<SiteSelection>) this.getList(iFilter);
		if(rtList!=null&&rtList.size()>0){
			return rtList.get(0);
		}
		return null;
	}
	
	@Override
	public SiteSelection updateSiteSelection(SiteSelection selection){
		SiteSelection saveSiteSelection = (SiteSelection) this.getObject(selection.getId());
		if(saveSiteSelection!=null){
			saveSiteSelection.setSelection_status(selection.getSelection_status());
			saveSiteSelection.setReason(selection.getReason());
			preSaveObject(saveSiteSelection);
			saveObject(saveSiteSelection);
		}
		return selection;
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
