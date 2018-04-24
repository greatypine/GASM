package com.cnpc.pms.personal.manager;

import java.util.List;

import com.cnpc.pms.personal.entity.OuterCompany;

public interface OuterCompanyManager {
    
	public OuterCompany queryOuterCompanyById(Long id);
	
	public OuterCompany saveOuterCompany(String companyname,String companycode,String companyallname);
	
	public OuterCompany queryOuterCompanyByCode(String companycode);
	
	public List<OuterCompany> queryOutCompanys();
	
	public OuterCompany queryOuterCompanyByName(String companyName);
}
