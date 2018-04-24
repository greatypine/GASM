package com.cnpc.pms.personal.manager.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.StoreStandard;
import com.cnpc.pms.personal.manager.StoreStandardManager;


public class StoreStandardManagerImpl extends BaseManagerImpl implements StoreStandardManager {

	@Override
	public List<StoreStandard> queryStoreStandardList(){
		List<StoreStandard> lstList = (List<StoreStandard>) this.getList();
		return lstList;
	}
	
	
	@Override
	public StoreStandard queryStoreStandardById(Long id) {
		StoreStandard storeStandard = (StoreStandard) this.getObject(id);
		return storeStandard;
	}
	
	
	@Override
	public StoreStandard updateStoreStandard(StoreStandard storeStandard){
		StoreStandard updateStoreStandard = (StoreStandard) this.getObject(storeStandard.getId());
		BeanUtils.copyProperties(storeStandard, updateStoreStandard);
		preSaveObject(updateStoreStandard);
		this.saveObject(updateStoreStandard);
		return updateStoreStandard;
	}
	
	
	@Override
	public StoreStandard saveStoreStandard(StoreStandard storeStandard) {
		preSaveObject(storeStandard);
		saveObject(storeStandard);
		return storeStandard;
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
