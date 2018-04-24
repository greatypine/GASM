package com.cnpc.pms.personal.manager.impl;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.dao.BusinessTypeDao;
import com.cnpc.pms.personal.entity.BusinessType;
import com.cnpc.pms.personal.manager.BusinessTypeManager;

public class BusinessTypeManagerImpl extends BizBaseCommonManager implements BusinessTypeManager {

	@Override
	public BusinessType getBusinessTypeByStringArray(String level1_name, String level2_name, String[] level3,String level4) {
		String string=null;
		if(level3.length>2){
			string="level1_name LIKE '%"+level1_name+"%' AND level2_name LIKE '%"+level2_name+"%' AND level3_name LIKE '%"+level3[1]+"%' AND level3_name LIKE '%"+level3[2]+"%' AND level4_name LIKE '%"+level4+"%'";
		}else if(level3.length==1){
			string="level1_name LIKE '%"+level1_name+"%' AND level2_name LIKE '%"+level2_name+"%' AND level3_name LIKE '%"+level3[0]+"%' AND level4_name LIKE '%"+level4+"%'";
		}else{
			string="level1_name LIKE '%"+level1_name+"%' AND level2_name LIKE '%"+level2_name+"%' AND level3_name LIKE '%"+level3[1]+"%' AND level4_name LIKE '%"+level4+"%'";
		}
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter(string));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (BusinessType)lst_vilage_data.get(0);
        }
        return null;
	}

	@Override
	public List<Map<String, Object>> getTwoCodeByCondition(){
		BusinessTypeDao businessTypeDao=(BusinessTypeDao)SpringHelper.getBean(BusinessTypeDao.class.getName());
		List<Map<String, Object>> list = businessTypeDao.getTwolevelCode();
		return list;
	}

	@Override
	public List<Map<String, Object>> getThreeCodeByCondition(String two_level) {
		BusinessTypeDao businessTypeDao=(BusinessTypeDao)SpringHelper.getBean(BusinessTypeDao.class.getName());
		List<Map<String, Object>> list = businessTypeDao.getThreeCode(two_level);
		return list;
	}

	@Override
	public List<Map<String, Object>> getFourCodeByCondition(String three_level) {
		BusinessTypeDao businessTypeDao=(BusinessTypeDao)SpringHelper.getBean(BusinessTypeDao.class.getName());
		List<Map<String, Object>> list = businessTypeDao.getFourCode(three_level);
		return list;
	}

	@Override
	public List<Map<String, Object>> getFiveCodeByCondition(String four_level) {
		BusinessTypeDao businessTypeDao=(BusinessTypeDao)SpringHelper.getBean(BusinessTypeDao.class.getName());
		List<Map<String, Object>> list = businessTypeDao.getFiveCode(four_level);
		return list;
	}

	@Override
	public BusinessType getBusinessTypeByCondition(String level1_code, String level2_code, String level3_code,String level4_code) {
		
		String string="level1_code = '"+level1_code+"' AND level2_code = '"+level2_code+"' AND level3_code = '"+level3_code+"' AND level4_code = '"+level4_code+"'";
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter(string));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (BusinessType)lst_vilage_data.get(0);
        }
        return null;
	}






}
