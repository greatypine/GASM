package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;

public interface StoreDocumentInfoDao extends IDAO {
	List<Map<String, Object>> getStoreDocumentInfoData(String where, PageInfo pageInfo);

	public List<Map<String, Object>> exportStoreDetail(String where);
}
