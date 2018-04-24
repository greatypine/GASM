package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.FerryPush;

public interface FerryPushManager extends IManager{
	//保存摆渡车数据
	FerryPush saveFerryPushList(List<Map<String, Object>> listFerry,String str_month,Long store_id,Integer status_type);
	//根据员工编号和月份查询摆渡车数据
	FerryPush findFerryPush(String str_month,String employee_no);
	List<FerryPush> getFerryPushList(Long store_id,String str_month);

}
