package com.cnpc.pms.platform.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dto.StoreDTO;
import com.cnpc.pms.platform.entity.PlatformStore;

public interface PlatformStoreDao {
	List<PlatformStore> getPlatformStoreList(String where, PageInfo pageInfo)  ;
	Map<String, Object> getPlatformStoreInfoList(String where, PageInfo pageInfo);
	//获取数据的总条数
	int getPlatformStoreCount(String where);
	/**
	 * 
	 * TODO 通过门店编号查询门店国安侠
	 * 2017年12月14日
	 * @author gaoll
	 * @param storeNo
	 * @return
	 */
	List<Map<String,Object>> getEmployeeByStore(String storeNo,Boolean isRealtime);
	
	/**
	 * 
	 * TODO 通过门店编号查询门店国安侠
	 * 2017年3月22日
	 * @author gaoll
	 * @param city_code 
	 */
	List<Map<String,Object>> getEmployeeByCity(String city_code,Boolean isRealtime);
	
	/**
	 * 
	 * TODO 通过员工编号查询国安侠
	 * 2018年1月3日
	 * @author gaoll
	 * @param storeNo
	 * @return
	 */
	List<Map<String,Object>> getEmployeeByEmployeeNo(String employeeNo,Boolean isRealtime);
	
	/**
	 * 
	 * TODO 通过员工编号查询国安侠是否在线
	 * 2017年1月3日
	 * @author gaoll
	 * @param storeNo
	 * @return
	 */
	Map<String,Object> getEmployeeStatus(String employeeNo);
	
	
}
