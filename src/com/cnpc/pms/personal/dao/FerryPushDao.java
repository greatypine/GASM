package com.cnpc.pms.personal.dao;

import com.cnpc.pms.base.dao.IDAO;

public interface FerryPushDao extends IDAO{
	void updateFerryPushStatus(Long store_id,String str_month,Integer state_type);
}
