package com.cnpc.pms.personal.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.entity.City;
import com.cnpc.pms.personal.entity.Province;
import com.cnpc.pms.personal.manager.ProvinceManager;

public class ProvinceManagerImpl extends BizBaseCommonManager implements ProvinceManager {

	@Override
	public List<Map<String, Object>> getProvince() {
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		Province province=null;
		 List<?> lst_vilage_data = getList();
		 if(lst_vilage_data!=null&&lst_vilage_data.size()>0){
			 for (Object object : lst_vilage_data) {
				Map<String, Object> map=new HashMap<String, Object>();
				province=(Province)object;
				map.put("province_name", province.getName());
				map.put("province_id", province.getId());
				list.add(map);
			}
		 }
		return list;
	}

	@Override
	public Province getProvinceById(Long id) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("id",id));
		if(list!=null&&list.size()>0){
			return (Province)list.get(0);
		}
		return null;
	}

	

}
