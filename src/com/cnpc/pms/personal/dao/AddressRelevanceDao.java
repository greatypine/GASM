package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;

public interface AddressRelevanceDao extends IDAO{
	List<Map<String, Object>> getAddressRelevanceList(String where, PageInfo pageInfo);
	//排序
	void upxuhao(String string);
	void deleteXiangtong();
	List<Map<String, Object>> getOrderAddressRelevanceList(String where, PageInfo pageInfo);
	//清除订单区块绑定小区表中的重复数据
	void deleteComnunityAreaData();
}
