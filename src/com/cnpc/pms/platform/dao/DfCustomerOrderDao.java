package com.cnpc.pms.platform.dao;

import java.util.List;
import java.util.Map;

public interface DfCustomerOrderDao {

	// 查询消费用户数
	Integer findCustomerCount();

	// 根据门店查询消费用户数
	Integer findCustomerCountByStore(String storeCode);

	// 查询用户人力定位
	List<Map<String, Object>> findCustomerLatLng(String storeNo);

	// 查询用户人力定位最大值最小值
	List<Map<String, Object>> findCustomerLatLngCount(String storeNo);

	/**
	 * 同步小区已消费人数
	 * 
	 * @author sunning
	 * @param tiny_code
	 * @return
	 */
	void syncTinyVillageCustomer();

	/**
	 * 根据小区code获取小区已消费用户数量
	 * 
	 * @author sunning
	 * @param tinycode
	 * @return
	 */
	Integer findCustomerNumberBytinyvillageCode(String tinycode);

	//查询小区的消费人数
	Integer findCustomerByTinyVillageCode(String codes);
}
