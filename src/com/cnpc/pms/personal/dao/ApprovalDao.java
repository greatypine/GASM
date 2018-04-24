package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;

public interface ApprovalDao extends IDAO{
	/**
	 * 获取待审核的数据
	 * @param where
	 * @param pageInfo
	 * @return
	 */
	List<Map<String, Object>> getApprovalInfoList(String where, PageInfo pageInfo);

}
