package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.personal.entity.City;
import com.cnpc.pms.personal.entity.Province;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.manager.CityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunning on 2016/7/21.
 * @param
 *
 */
public class CityManagerImpl extends BaseManagerImpl implements CityManager {
    public City getCityByName(String name){
        List<?> lst_vilage_data =this.getList(FilterFactory.getSimpleFilter("name",name));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0){
           return (City)lst_vilage_data.get(0);
        }
        return null;
    }

	@Override
	public List<Map<String, Object>> getCityDataByProvinceID(Long provinceId) {
		List list=new ArrayList();
		City city=null;
		 List<?> lst_vilage_data = this.getList(FilterFactory.getSimpleFilter("province_id",provinceId));
		 if(lst_vilage_data!=null&&lst_vilage_data.size()>0){
			 for (Object object : lst_vilage_data) {
				Map<String, Object> map=new HashMap<String, Object>();
				city=(City)object;
				map.put("city_name", city.getName());
				map.put("city_id", city.getId());
				list.add(map);
			}
		 }
		return list;
	}

	@Override
	public City getCityById(Long id) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("id",id));
		if(list!=null&&list.size()>0){
			return (City)list.get(0);
		}
		return null;
	}

	@Override
	public City getCityBygb_code(String gb_code) {
		 List<?> lst_vilage_data =this.getList(FilterFactory.getSimpleFilter("gb_code",gb_code));
	        if(lst_vilage_data != null && lst_vilage_data.size() > 0){
	           return (City)lst_vilage_data.get(0);
	        }
	        return null;
	}

	@Override
	public List<City> getCityDataByName(String name) {
		 List<?> lst_vilage_data =this.getList(FilterFactory.getSimpleFilter("name like '%"+name+"%'"));
	        return (List<City>)lst_vilage_data;
	};
}
