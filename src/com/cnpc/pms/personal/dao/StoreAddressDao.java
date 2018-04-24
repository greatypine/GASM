package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;

public interface StoreAddressDao  extends IDAO {

	public List<Map<String, Object>> queryStoreAddressList(Long id);
	
}