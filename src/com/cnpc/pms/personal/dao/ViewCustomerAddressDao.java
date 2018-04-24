package com.cnpc.pms.personal.dao;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;

import java.util.List;
import java.util.Map;

public interface ViewCustomerAddressDao extends IDAO{
	/**
	 * 获取用户视图信息
	 * @param where
	 * @param pageInfo
	 * @return
	 */
	List<Map<String, Object>> getViewCustomerAddressList(String where, PageInfo pageInfo);
	
	/**
	 * 获取用户视图信息
	 * @param where
	 * @param pageInfo
	 * @return
	 */
	List<Map<String, Object>> getViewCustomerAddressListData(String where,String month, String complete_status,PageInfo pageInfo,String grade);

}
