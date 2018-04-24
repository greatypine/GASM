package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;

public interface OfficeDao extends IDAO{
	/**
	 * 根据省,街道信息,查询写字楼信息
	 * @return
	 */
	List<Map<String, Object>> getOfficeList(String where, PageInfo pageInfo);
	
	/**
	 * 根据省,街道信息,查询写字楼打印列表(用于check数据)
	 * @return
	 */
	List<Map<String, Object>> getPrintOfficeList(String where, Integer limitCount);
	
	/**
	 * 根据省,街道信息,查询写字楼信息打印列表(全部数据)
	 * @return
	 */
	List<Map<String, Object>> getPrintWholeOfficeList(String where, Integer limitCount);
	
}
