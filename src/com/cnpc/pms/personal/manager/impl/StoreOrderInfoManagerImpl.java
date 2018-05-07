package com.cnpc.pms.personal.manager.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.entity.StoreOrderInfo;
import com.cnpc.pms.personal.manager.StoreOrderInfoManager;



public class StoreOrderInfoManagerImpl extends BaseManagerImpl implements StoreOrderInfoManager {
	@Override
	public Map<String, Object> queryStoreOrderInfoList(QueryConditions condition){
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		StringBuffer cond = new StringBuffer();
		String employee_name = null;
		String employee_no = null;
		for(Map<String, Object> map : condition.getConditions()){
			if("employee_name".equals(map.get("key"))&&map.get("value")!=null){//组织机构
				employee_name = map.get("value").toString();
			}
			if("employee_no".equals(map.get("key"))&&map.get("value")!=null){//组织机构
				employee_no = map.get("value").toString();
			}
		}
		cond.append(" 1=1 ");
		if(employee_name!=null){
			cond.append(" and employee_name like '%"+employee_name+"%'");
		}
		if(employee_no!=null){
			cond.append(" and employee_no like '%"+employee_no+"%'");
		}
		
		UserDTO userDTO = userManager.getCurrentUserDTO();
		cond.append(" and store_id ="+userDTO.getStore_id());
		
		IFilter iFilter =FilterFactory.getSimpleFilter(cond.toString());
		List<StoreOrderInfo> lst_List = (List<StoreOrderInfo>) this.getList(iFilter);
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", lst_List);
		return returnMap;
	}
	
	@Override
	public StoreOrderInfo saveStoreOrderInfo(StoreOrderInfo storeOrderInfo) {
		//处理工单编号
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		UserDTO userDTO = userManager.getCurrentUserDTO();
		String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String work_no = "GD"+dateString+(int)((Math.random()*9+1)*100000);
		storeOrderInfo.setWorder_sn(work_no);
		storeOrderInfo.setStore_id(userDTO.getStore_id());
		preSaveObject(storeOrderInfo);
		this.saveObject(storeOrderInfo);
		return storeOrderInfo;
	}
	
	@Override
	public StoreOrderInfo queryStoreOrderInfoById(Long id){
		StoreOrderInfo storeOrderInfo = (StoreOrderInfo) this.getObject(id);
		return storeOrderInfo;
	}
	
	@Override
	public StoreOrderInfo updateStoreOrderInfo(StoreOrderInfo storeOrderInfo) {
		StoreOrderInfo updateStoreOrderInfo = null;
		if(storeOrderInfo!=null&&storeOrderInfo.getId()!=null){
			updateStoreOrderInfo = (StoreOrderInfo) this.getObject(storeOrderInfo.getId());
			updateStoreOrderInfo.setWcontent(storeOrderInfo.getWcontent());
			updateStoreOrderInfo.setEmployee_name(storeOrderInfo.getEmployee_name());
			updateStoreOrderInfo.setEmployee_no(storeOrderInfo.getEmployee_no());
			updateStoreOrderInfo.setPhone(storeOrderInfo.getPhone());
			updateStoreOrderInfo.setWorder_status(storeOrderInfo.getWorder_status());
			preSaveObject(updateStoreOrderInfo);
			this.saveObject(updateStoreOrderInfo);
		}
		return updateStoreOrderInfo;
	}
	
	
	
	
	
	
	
	
	
	@Override
	public Map<String, Object> queryStoreOrderInfoListApp(PageInfo pageInfo,String employee_no){
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		StringBuffer cond = new StringBuffer();
		cond.append(" 1=1 ");
		if(employee_no!=null){
			cond.append(" and employee_no = '"+employee_no+"'");
		}else{
			cond.append(" and 1=0 ");
		}
		FSP fsp = new FSP();
		IFilter iFilter =FilterFactory.getSimpleFilter(cond.toString());
		fsp.setUserFilter(iFilter);
		fsp.setPage(pageInfo);
		fsp.setSort(new Sort("id",Sort.DESC));
		List<StoreOrderInfo> lst_List = (List<StoreOrderInfo>) this.getList(fsp);
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", lst_List);
		return returnMap;
	}
	
	
	
	
	protected void preSaveObject(Object o) {
		if (o instanceof DataEntity) {
			User sessionUser = null;
			if (null != SessionManager.getUserSession()
					&& null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession()
						.getSessionData().get("user");
			}
			DataEntity dataEntity = (DataEntity) o;
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			// insert处理时添加创建人和创建时间
			if (null == dataEntity.getCreate_time()) {
				dataEntity.setCreate_time(sdate);
				if (null != sessionUser) {
					dataEntity.setCreate_user(sessionUser.getCode());
					dataEntity.setCreate_user_id(sessionUser.getId());
				}
			}
			dataEntity.setUpdate_time(sdate);
			if (null != sessionUser) {
				dataEntity.setUpdate_user(sessionUser.getCode());
				dataEntity.setUpdate_user_id(sessionUser.getId());
			}
		}
	}
	
}
