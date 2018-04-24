package com.cnpc.pms.personal.manager.impl;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.entity.Approval;
import com.cnpc.pms.personal.entity.FerryPush;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.ApprovalManager;
import com.cnpc.pms.personal.manager.FerryPushManager;
import com.cnpc.pms.personal.manager.StoreManager;

public class FerryPushManagerImpl extends BizBaseCommonManager implements FerryPushManager{

	@Override
	public FerryPush saveFerryPushList(List<Map<String, Object>> listFerry, String str_month, Long store_id,Integer status_type) {
		FerryPush updateFerryPush=null;
		FerryPushManager ferryPushManager=(FerryPushManager)SpringHelper.getBean("ferryPushManager");
		ApprovalManager approvalManager=(ApprovalManager)SpringHelper.getBean("approvalManager");
		StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
		Store findStore = storeManager.findStore(store_id);
		if(listFerry!=null&&listFerry.size()>0){
			for (Map<String, Object> ferryPush : listFerry){
				updateFerryPush = findFerryPush(str_month,ferryPush.get("employee_no")+"");
				if(updateFerryPush==null){
					updateFerryPush=new FerryPush();
				}
				updateFerryPush.setMorningShift(getObjectTransfromInteger(ferryPush.get("morning_shift")));
				updateFerryPush.setNightShift(getObjectTransfromInteger(ferryPush.get("night_shift")));
				updateFerryPush.setTotalShift(getObjectTransfromInteger(ferryPush.get("total_shift")));
				updateFerryPush.setFerryHouse(getObjectTransfromInteger(ferryPush.get("ferry_house")));
				updateFerryPush.setTotalHouse(getObjectTransfromInteger(ferryPush.get("total_house")));
				updateFerryPush.setHourlyOrderNumber(getObjectTransfromInteger(ferryPush.get("hourly_order_number")));
				updateFerryPush.setTotalSingular(getObjectTransfromInteger(ferryPush.get("total_singular")));
				updateFerryPush.setEmployeeNo(ferryPush.get("employee_no")+"");
				updateFerryPush.setUserName(ferryPush.get("user_name")+"");
				updateFerryPush.setStrMonth(str_month);
				updateFerryPush.setStateType(status_type);    
				if(findStore!=null){
					updateFerryPush.setStore_id(findStore.getStore_id());
					updateFerryPush.setCityName(findStore.getCityName());
				}
				preObject(updateFerryPush);
				ferryPushManager.saveObject(updateFerryPush);
			}
		}
		Approval approval = new Approval();
		approval.setCityName(updateFerryPush.getCityName());
		approval.setStore_id(updateFerryPush.getStore_id());
		approval.setName("摆渡车");
		approval.setStateType(status_type);
		approval.setTypeId(1+"");
		approval.setStrMonth(str_month);
		approval.setStore_name(findStore.getName());
		approvalManager.saveApproval(approval);
		return updateFerryPush;
	}

	@Override
	public FerryPush findFerryPush(String str_month, String employee_no) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("employee_no = '"+employee_no + "' and str_month = '"+str_month+"'"));
		if(list!=null&&list.size()>0){
			return (FerryPush)list.get(0);
		}
		return null;
	}
	public Integer getObjectTransfromInteger(Object obj){
		if(obj!=null&&!"".equals(obj)){
			int int1 = Integer.parseInt(obj+"");
			return int1;
		}
		return null;
	}

	@Override
	public List<FerryPush> getFerryPushList(Long store_id, String str_month) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("store_id = "+store_id + " and str_month = '"+str_month+"'"));
		if(list!=null&&list.size()>0){
			return (List<FerryPush>)list;
		}
		return null;
	}
	

}
