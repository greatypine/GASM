package com.cnpc.pms.personal.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.CustomerData;
import com.cnpc.pms.personal.entity.CustomerOperateRecord;
import com.cnpc.pms.personal.manager.CustomerDataManager;
import com.cnpc.pms.personal.manager.CustomerDataTemporaryManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.CustomerOperateRecordManager;

public class CustomerDataManagerImpl extends BizBaseCommonManager implements CustomerDataManager {

	@Override
	public CustomerData saveCustomerData(CustomerData customerData) {
		CustomerManager customerManager=(CustomerManager)SpringHelper.getBean("customerManager");
		CustomerData find_customerData =null;
		CustomerOperateRecordManager coRecordManager = (CustomerOperateRecordManager) SpringHelper.getBean("customerOperateRecordManager");
	    CustomerOperateRecord coRecord = new CustomerOperateRecord();//操作记录
	    boolean isAdd = false;
	    Date sdDate = new Date();
	    
		if(customerData.getCustomer_id()!=null){
			 find_customerData = getCustomerDataByCustomerId(customerData.getCustomer_id());
			 
			 if(find_customerData==null){
				 find_customerData=new CustomerData();
				 isAdd = true;
			 }
		}
		
		
		if(find_customerData.getId()!=null){
			find_customerData.setUpdate_user(customerData.getCreate_user());
			find_customerData.setUpdate_user_id(customerData.getCreate_user_id());
			find_customerData.setUpdate_time(sdDate);
			customerData.setId(find_customerData.getId());
			
		}else{
			find_customerData.setUpdate_user(customerData.getCreate_user());
			find_customerData.setUpdate_user_id(customerData.getCreate_user_id());
			find_customerData.setUpdate_time(sdDate);
			find_customerData.setCreate_user(customerData.getCreate_user());
			find_customerData.setCreate_user_id(customerData.getCreate_user_id());
			find_customerData.setCreate_time(sdDate);
			find_customerData.setEmployee_no(customerData.getEmployee_no());
		}
		Customer customer = customerManager.findCustomerById(customerData.getCustomer_id());
		if(!customer.getEmployee_no().equals(customerData.getEmployee_no())){//更新其他国安侠客户信息
			/*BeanUtils.copyProperties(customerData, find_customerData
					,new String[]{"id","version","status","create_user","create_user_id","create_time"
								,"update_user","update_user_id","update_time"});*/
			CustomerDataTemporaryManager customerDataTemporaryManager=(CustomerDataTemporaryManager)SpringHelper.getBean("customerDataTemporaryManager");
			
			customerDataTemporaryManager.saveCustomerDataTemporary(customerData,customer.getEmployee_no());
			return null;
		}
		if("1".equals(customerData.getIs_car())){//没有汽车
			customerData.setAutomobile_brand("");
		}
		preObject(customerData);
		saveObject(customerData);
		
		BeanUtils.copyProperties(customerData,coRecord,new String[]{"id","version"});
		if(isAdd){//新增
			coRecord.setType(0);
			coRecord.setEmployee_create_no(customerData.getEmployee_no());
            coRecord.setEmployee_update_no(customerData.getEmployee_no());
            
		}else{
			coRecord.setType(1);
			coRecord.setEmployee_create_no(find_customerData.getEmployee_no());
            coRecord.setEmployee_update_no(customerData.getEmployee_no());
		}
		
		coRecord.setCreate_time(sdDate);
		coRecord.setUpdate_user(customerData.getCreate_user());
		coRecord.setUpdate_user_id(customerData.getCreate_user_id());
		coRecord.setUpdate_time(sdDate);
		coRecord.setIsMoreInfo(1);
		coRecordManager.saveObject(coRecord);
		return find_customerData;
	}

	@Override
	public CustomerData getCustomerDataByCustomerId(Long customerId) {
		List<?> list = getList(FilterFactory.getSimpleFilter("customer_id",customerId));
		if(list!=null&&list.size()>0){
			return (CustomerData)list.get(0);
		}
		return null;
	}

	
}
