package com.cnpc.pms.personal.manager.impl;

import java.util.List;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.entity.PersonDuty;
import com.cnpc.pms.personal.manager.PersonDutyManager;

public class PersonDutyManagerImpl  extends BizBaseCommonManager implements PersonDutyManager{

	@Override
	public PersonDuty findPersonDutyBycustomerIdAnddutyIdAndtype(Long dutyId, Integer type,Long customer_id) {
		List<?> lst_personDuty_data = getList(FilterFactory.getSimpleFilter(" duty_id = "+dutyId+" and customer_id="+customer_id+"  AND type="+type));
        if(lst_personDuty_data != null && lst_personDuty_data.size() > 0) {
           return (PersonDuty)lst_personDuty_data.get(0);
        }
        return null;
	}
	
	
	@Override
	public List<?> findPersonDutyBycustomerId(Long customer_id) {
		List<?> lst_personDuty_data = getList(FilterFactory.getSimpleFilter("customer_id",customer_id));
        return lst_personDuty_data;
	}

}
