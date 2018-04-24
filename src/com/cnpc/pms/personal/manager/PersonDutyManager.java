package com.cnpc.pms.personal.manager;

import java.util.List;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.PersonDuty;

public interface PersonDutyManager extends IManager{
	PersonDuty findPersonDutyBycustomerIdAnddutyIdAndtype(Long dutyId,Integer type,Long customer_id);
	public List<?> findPersonDutyBycustomerId(Long customer_id);
}
