package com.cnpc.pms.personal.manager.impl;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.dao.StoreAddressDao;
import com.cnpc.pms.personal.entity.StoreAddress;
import com.cnpc.pms.personal.manager.StoreAddressManager;


public class StoreAddressManagerImpl extends BaseManagerImpl implements StoreAddressManager {

	@Override
	public List<Map<String, Object>> queryStoreAddressList(Long id){
		StoreAddressDao storeAddressDao = (StoreAddressDao) SpringHelper.getBean(StoreAddressDao.class.getName());
		List<Map<String, Object>> rsList = storeAddressDao.queryStoreAddressList(id);
		return rsList;
	}
	
	@Override
	public StoreAddress queryStoreAddressById(Long id){
		StoreAddress storeAddress = (StoreAddress) this.getObject(id);
		return storeAddress;
	}
	
	@Override
	public StoreAddress saveStoreAddressRemark(StoreAddress storeAddress){
		StoreAddress saveStoreAddress = (StoreAddress) this.getObject(storeAddress.getId());
		saveStoreAddress.setStatus_remarks(storeAddress.getStatus_remarks());
		saveStoreAddress.setStore_status(storeAddress.getStore_status());
		saveStoreAddress.setAdditional_remarks(storeAddress.getAdditional_remarks());
		preSaveObject(saveStoreAddress);
		saveObject(saveStoreAddress);
		return saveStoreAddress;
	}
	
	@Override
	public StoreAddress saveStoreAddress(StoreAddress storeAddress) {
		preSaveObject(storeAddress);
		saveObject(storeAddress);
		return storeAddress;
	}
	
	@Override
	public StoreAddress updateStoreAddress(StoreAddress storeAddress){
		StoreAddress savestoreaddress = (StoreAddress) this.getObject(storeAddress.getId());
		if(savestoreaddress!=null){
			savestoreaddress.setStore_status(storeAddress.getStore_status());
			preSaveObject(savestoreaddress);
			saveObject(savestoreaddress);
		}
		return savestoreaddress;
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
