package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.StoreKeeper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface StoreKeeperManager extends IManager {

	public void saveStoreKeeper(StoreKeeper storeKeeper);
	
	public StoreKeeper queryStoreKeeperById(Long id);
	
	public StoreKeeper updateStoreKeeper(StoreKeeper storeKeeper);
	
	public Map<String, Object> querystorekeeperList(QueryConditions condition);
	
	List<Map<String, Object>> getShopManagerList(String cityname);

	public User addBizbaseUser(StoreKeeper storeKeeper);
	
	StoreKeeper findStoreKeeperByEmployeeId(String Employeeid);
	
	public StoreKeeper updatestorekeeperpassword(String storepassword,String confstorepassword,String employee_no);
	
	File exportStoreKeeperExcel() throws IOException, Exception;
	
	 /**
     * 验证是否存在该店长
     */
	public StoreKeeper queryStoreKeeperByPhone(String phone);
	
	/**
     * 根据姓名和电话查询是否存在该店长 
     */
	public StoreKeeper queryStoreKeeperByNamePhone(String name,String phone);
	
}
