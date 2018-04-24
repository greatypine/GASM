package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;

public interface BusinessInfoDao extends IDAO{
	/**
	 * 根据省,社区,街道信息,查询商业信息
	 * @return
	 */
	List<Map<String, Object>> getBusinessInfoList(String where, PageInfo pageInfo);
	
	/**
     * 获取导出的商业数据
     * @param store_id
     * @return
     */
	List<Map<String, Object>> getBussinessInfo(String where);
	/**
	 * 
	 */
	public int updateData(String sql);
}
