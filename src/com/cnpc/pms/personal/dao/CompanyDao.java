package com.cnpc.pms.personal.dao;

import java.util.List;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.personal.entity.Company;

public interface CompanyDao extends IDAO{
	Company getCompany(String office_company,String office_floor_of_company,int office_id);
	void saveOrUpdate(Company company);
	//将修改后删除的数据进行删除
	void getComPanybyOffice_idandCompany_id(Integer office,String Companyids);
	
	

}
