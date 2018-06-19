package com.cnpc.pms.personal.manager.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import BI.IF;

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
import com.cnpc.pms.mongodb.manager.MongoDBManager;
import com.cnpc.pms.personal.dao.StoreOrderInfoDao;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.StoreOrderInfo;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
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
		
		FSP fsp = new FSP();
		IFilter iFilter =FilterFactory.getSimpleFilter(cond.toString());
		fsp.setPage(pageInfo);
		fsp.setUserFilter(iFilter);
		List<StoreOrderInfo> lst_List = (List<StoreOrderInfo>) this.getList(fsp);
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", lst_List);
		return returnMap;
	}
	
	@Override
	public StoreOrderInfo saveStoreOrderInfo(StoreOrderInfo storeOrderInfo) {
		//处理工单编号
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
		UserDTO userDTO = userManager.getCurrentUserDTO();
		String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String work_no = "GD"+dateString+(int)((Math.random()*9+1)*100000);
		storeOrderInfo.setWorder_sn(work_no);
		storeOrderInfo.setStore_id(userDTO.getStore_id()); 
		//根据员工编号查询员工电话
		IFilter iFilter =FilterFactory.getSimpleFilter("humanstatus=1 and employee_no='"+storeOrderInfo.getEmployee_no()+"'");
		List<Humanresources> human_list = (List<Humanresources>) humanresourcesManager.getList(iFilter);
		if(human_list!=null&&human_list.size()>0){
			Humanresources humanresources = human_list.get(0);
			storeOrderInfo.setEmployee_phone(humanresources.getPhone());
		}
		
		if(storeOrderInfo.getWorder_status()==2){
			storeOrderInfo.setConfirm_date(new Date());
		}
		
		preSaveObject(storeOrderInfo);
		this.saveObject(storeOrderInfo);
		
		
		//如果状态为已确认 则插入mongo表中
		if(storeOrderInfo.getWorder_status()==2){
			MongoDBManager mongoDBManager = (MongoDBManager) SpringHelper.getBean("mongoDBManager");
			mongoDBManager.saveStoreOrderInfo(storeOrderInfo);
		}
		
		
		return storeOrderInfo;
	}
	
	
	@Override
	public StoreOrderInfo saveStoreOrderInfoForApp(StoreOrderInfo storeOrderInfo) {
		HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
		String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String work_no = "GD"+dateString+(int)((Math.random()*9+1)*100000);
		storeOrderInfo.setWorder_sn(work_no);
		storeOrderInfo.setStore_id(storeOrderInfo.getStore_id());
		
		//根据员工编号查询员工电话
		IFilter iFilter =FilterFactory.getSimpleFilter("humanstatus=1 and employee_no='"+storeOrderInfo.getEmployee_no()+"'");
		List<Humanresources> human_list = (List<Humanresources>) humanresourcesManager.getList(iFilter);
		if(human_list!=null&&human_list.size()>0){
			Humanresources humanresources = human_list.get(0);
			storeOrderInfo.setEmployee_phone(humanresources.getPhone());
		}
				
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
	public Map<String,Object> queryStoreOrderInfoListByPhone(PageInfo pageInfo,String phone,String inputnum){
		StoreOrderInfoDao storeOrderInfoDao = (StoreOrderInfoDao) SpringHelper.getBean(StoreOrderInfoDao.class.getName());
		Map<String, Object> lst_List = storeOrderInfoDao.queryStoreOrderInfoListByPhone(pageInfo, phone, inputnum);
		return lst_List;
	}
	
	@Override
	public StoreOrderInfo updateStoreOrderInfo(StoreOrderInfo storeOrderInfo) {
		StoreOrderInfo updateStoreOrderInfo = null;
		if(storeOrderInfo!=null&&storeOrderInfo.getId()!=null){
			updateStoreOrderInfo = (StoreOrderInfo) this.getObject(storeOrderInfo.getId());
			updateStoreOrderInfo.setWcontent(storeOrderInfo.getWcontent());
			updateStoreOrderInfo.setEmployee_name(storeOrderInfo.getEmployee_name());
			updateStoreOrderInfo.setEmployee_no(storeOrderInfo.getEmployee_no());
			//根据员工编号查询员工电话
			HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
			IFilter iFilter =FilterFactory.getSimpleFilter("humanstatus=1 and employee_no='"+storeOrderInfo.getEmployee_no()+"'");
			List<Humanresources> human_list = (List<Humanresources>) humanresourcesManager.getList(iFilter);
			if(human_list!=null&&human_list.size()>0){
				Humanresources humanresources = human_list.get(0);
				updateStoreOrderInfo.setEmployee_phone(humanresources.getPhone());
			}
			
			updateStoreOrderInfo.setUsername(storeOrderInfo.getUsername());
			updateStoreOrderInfo.setPhone(storeOrderInfo.getPhone());
			updateStoreOrderInfo.setRcv_phone(storeOrderInfo.getRcv_phone());
			updateStoreOrderInfo.setAddress(storeOrderInfo.getAddress());
			updateStoreOrderInfo.setWorder_status(storeOrderInfo.getWorder_status());
			updateStoreOrderInfo.setWorder_type(storeOrderInfo.getWorder_type());
			updateStoreOrderInfo.setRcv_name(storeOrderInfo.getRcv_name());
			
			
			if(updateStoreOrderInfo.getWorder_status()==2){
				updateStoreOrderInfo.setConfirm_date(new Date());
			}
			
			preSaveObject(updateStoreOrderInfo);
			this.saveObject(updateStoreOrderInfo);
			
			
			//如果状态为已确认 则插入mongo表中
			if(updateStoreOrderInfo.getWorder_status()==2){
				MongoDBManager mongoDBManager = (MongoDBManager) SpringHelper.getBean("mongoDBManager");
				mongoDBManager.saveStoreOrderInfo(updateStoreOrderInfo);
			}
			
		}
		return updateStoreOrderInfo;
	}
	
	@Override
	public StoreOrderInfo updateStoreOrderInfoBySN(String worder_sn,int worder_status){
		StoreOrderInfo updateStoreOrderInfo = null;
		StoreOrderInfoManager storeOrderInfoManager = (StoreOrderInfoManager) SpringHelper.getBean("storeOrderInfoManager");
		IFilter iFilter =FilterFactory.getSimpleFilter("worder_sn='"+worder_sn+"'");
		List<StoreOrderInfo> storeOrderInfos = (List<StoreOrderInfo>) storeOrderInfoManager.getList(iFilter);
		if(storeOrderInfos!=null&&storeOrderInfos.size()==1){
			updateStoreOrderInfo=storeOrderInfos.get(0);
			updateStoreOrderInfo.setWorder_status(worder_status);
			preSaveObject(updateStoreOrderInfo);
			storeOrderInfoManager.saveObject(updateStoreOrderInfo);
			
			//如果工单 状态为1。已确认的 。插入mongo
			if(updateStoreOrderInfo!=null&&updateStoreOrderInfo.getWorder_status()==2){
				updateStoreOrderInfo.setConfirm_date(new Date());
				MongoDBManager mongoDBManager = (MongoDBManager) SpringHelper.getBean("mongoDBManager");
				mongoDBManager.saveStoreOrderInfo(updateStoreOrderInfo);
			}
			
		}
		return updateStoreOrderInfo;
	}
	
	
	
	
	
	
	
	
	@Override
	public Map<String, Object> queryStoreOrderInfoListApp(PageInfo pageInfo,String employee_no,String types,String inputnum){
		StoreOrderInfoDao storeOrderInfoDao = (StoreOrderInfoDao) SpringHelper.getBean(StoreOrderInfoDao.class.getName());
		Map<String, Object> maps = storeOrderInfoDao.queryStoreOrderInfoListApp(pageInfo, employee_no, types, inputnum);
		return maps;
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
