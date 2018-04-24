package com.cnpc.pms.personal.manager.impl;

import java.util.List;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.StoreRequirement;
import com.cnpc.pms.personal.manager.StoreRequirementManager;


public class StoreRequirementManagerImpl extends BaseManagerImpl implements StoreRequirementManager {

	@Override
	public StoreRequirement saveStoreRequirement(StoreRequirement storeRequirement) {
		IFilter iFilter =FilterFactory.getSimpleFilter("store_name='"+storeRequirement.getStore_name().trim()+"'");
		List<StoreRequirement> sRequirements = (List<StoreRequirement>) this.getList(iFilter);
		if(sRequirements!=null&&sRequirements.size()>0){
			return null;
		}
		preSaveObject(storeRequirement);
		saveObject(storeRequirement);
		return storeRequirement;
	}
	
	@Override
	public StoreRequirement saveStoreRequirementExtend(StoreRequirement storeRequirement){
		preSaveObject(storeRequirement);
		saveObject(storeRequirement);
		return storeRequirement;
	}
	
	
	@Override
	public StoreRequirement queryStoreRequirementById(Long id){
		StoreRequirement storeRequirement = (StoreRequirement) this.getObject(id);
		return storeRequirement;
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
