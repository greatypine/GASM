package com.cnpc.pms.personal.manager.impl;

import java.util.List;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.entity.Address;
import com.cnpc.pms.personal.manager.AddressManager;

public class AddressManagerImpl extends BizBaseCommonManager implements AddressManager {
	@Override
	public Address getAddressBytown_gb_code(String town_gb_code) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("town_gb_code",town_gb_code));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (Address)lst_vilage_data.get(0);
        }
        return null;
	}

}
