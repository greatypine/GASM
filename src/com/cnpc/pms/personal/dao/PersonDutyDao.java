package com.cnpc.pms.personal.dao;

import com.cnpc.pms.base.dao.IDAO;

public interface PersonDutyDao extends IDAO{
	void deletePersonDuty(String ids,Integer type,Long id);

}
