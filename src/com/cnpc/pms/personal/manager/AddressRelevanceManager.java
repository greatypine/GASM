package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.AddressRelevance;

public interface AddressRelevanceManager extends IManager{
	//同步订单地址数据
	void syncOrderAddress();
	//查找订单地址
	AddressRelevance findAddressRelevanceByCountyIdAndPlaceName(String ad_code,String placename);
	//根据片区id查询所有的片区
	List<AddressRelevance> findAddressRelevanceByIds(String ids);
	//查询片区信息（小区视角）
	Map<String, Object> showAddressRelevanceData(QueryConditions conditions);
	AddressRelevance findAddressRelevanceById(Long id);
	
	void UpdateNUmber(String pids);
	
	//清除重复数据
	void syncdeleteOrderAddress();
	
	//查询所有片区信息（订单区块订单视角）
	Map<String, Object> showOrderAddressRelevanceData(QueryConditions conditions);
	
	//根据区块名字和门店编码查找订单地址
	AddressRelevance findAddressRelevanceBystorenoAndPlaceName(String storeno,String placename);
	
	//根据门店编号查找所有的区块id编码集合
	List<AddressRelevance> getAddressRelevanceDataByStoreNo(String storeno);
	
	

}
