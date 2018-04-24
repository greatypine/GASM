package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.StoreAddress;

public interface StoreAddressManager extends IManager{
	//保存选址人用户信息
	public StoreAddress saveStoreAddress(StoreAddress storeAddress);
	
	public StoreAddress updateStoreAddress(StoreAddress selection);
	
	public List<Map<String, Object>> queryStoreAddressList(Long id);
	
	public StoreAddress queryStoreAddressById(Long id);
	
	public StoreAddress saveStoreAddressRemark(StoreAddress storeAddress);
}
