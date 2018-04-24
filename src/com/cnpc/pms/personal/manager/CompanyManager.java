package com.cnpc.pms.personal.manager;

import java.util.Set;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.Company;

public interface CompanyManager extends IManager{
	/**
	 * 根据入驻公司名称判断公司是否存在
	 * @param office_company
	 * @return
	 */
	Company getCompanyByOffice_company(String office_company,String office_floor_of_company,int office_id);
	
	Set<Company> getComPanyByOfficeID(int office_id);
	
	
	
}
