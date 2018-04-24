package com.cnpc.pms.personal.manager.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.personal.entity.Company;
import com.cnpc.pms.personal.manager.CompanyManager;

public class CompanyManagerImpl extends BaseManagerImpl implements CompanyManager{

	@Override
	public Company getCompanyByOffice_company(String office_company,String office_floor_of_company,int office_id) {
		String string=null;
		if("".equals(office_floor_of_company)||office_floor_of_company==null){
			string=" office_id='"+office_id+"' and office_company like '%"+office_company+"%'";
		}else{
			string=" office_id='"+office_id+"' and office_floor_of_company='"+office_floor_of_company+"' and office_company = '"+office_company+"'";
		}
		List<?> list = getList(FilterFactory.getSimpleFilter(string));
		
		if(list!=null&&list.size()>0){
			return (Company)list.get(0);
		}
		return null;
	}

	@Override
	public Set<Company> getComPanyByOfficeID(int office_id) {
		Set<Company> set= new HashSet<Company>();
		List<?> list = this.getList(FilterFactory.getSimpleFilter("office_id",Long.parseLong(office_id+"")));
		if(list!=null&&list.size()>0){
			List<Company> listCom=(List<Company>)list;
			for (Company company : listCom) {
				set.add(company);
			}
			
		}
		return set;
	}

}
