package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;

public interface StoreDetailDao extends IDAO{
	List<Map<String, Object>> getStoreDetailData(String where, PageInfo pageInfo);
	List<Map<String, Object>> exportStoreDetail(String where);
}
