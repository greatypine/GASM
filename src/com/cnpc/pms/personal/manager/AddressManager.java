package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.Address;

public interface AddressManager extends IManager{
	/**
	 * 根据街道gb_code查询地址行政区划
	 * @param town_gb_code
	 * @return
	 */
	Address getAddressBytown_gb_code(String town_gb_code);

}
