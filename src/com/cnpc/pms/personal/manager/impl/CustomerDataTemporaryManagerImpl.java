package com.cnpc.pms.personal.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.messageModel.entity.Message;
import com.cnpc.pms.messageModel.manager.MessageNewManager;
import com.cnpc.pms.personal.entity.AppMessage;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.CustomerData;
import com.cnpc.pms.personal.entity.CustomerDataTemporary;
import com.cnpc.pms.personal.entity.CustomerOperateRecord;
import com.cnpc.pms.personal.manager.AppMessageManager;
import com.cnpc.pms.personal.manager.CustomerDataManager;
import com.cnpc.pms.personal.manager.CustomerDataTemporaryManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.CustomerOperateRecordManager;

public class CustomerDataTemporaryManagerImpl extends BizBaseCommonManager implements CustomerDataTemporaryManager {

	@Override
	public void saveCustomerDataTemporary(CustomerData customerData,String employee_create_no) {
		CustomerDataTemporary ct = new CustomerDataTemporary();
		MessageNewManager appMessageManager = (MessageNewManager)SpringHelper.getBean("messageNewManager");
        UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
        Date sdate = new Date();
        BeanUtils.copyProperties(customerData,ct,new String[]{"id","version","create_time","create_user","create_user_id"});
        ct.setCreate_time(sdate);
        ct.setCreate_user(customerData.getUpdate_user());
        ct.setCreate_user_id(customerData.getUpdate_user_id());
        ct.setId(null);
        ct.setEmployee_update_no(customerData.getEmployee_no());
        User  update_user= userManager.findEmployeeByEmployeeNo(customerData.getEmployee_no());
        
        User create_user = userManager.findEmployeeByEmployeeNo(employee_create_no);
        preObject(ct);
        saveObject(ct);
        
        Message appMessage  = new Message();
        appMessage.setTitle("用户画像");
        CustomerManager customerManager=(CustomerManager)SpringHelper.getBean("customerManager");
		Customer findcustomerData = customerManager.findCustomerById(customerData.getCustomer_id());
		
        appMessage.setContent(update_user.getName() + "修改您的单体画像数据，请审核。");
        appMessage.setPk_id(ct.getId());
        appMessage.setJump_path("message_more_information");
        appMessage.setType("monomer");
        if(create_user != null){
           
            appMessage.setCreate_user_id(update_user.getId());
            appMessage.setCreate_user(update_user.getName());
            appMessage.setUpdate_user_id(update_user.getId());
            appMessage.setUpdate_user(update_user.getName());
        }
        appMessage.setCreate_time(sdate);
        appMessage.setUpdate_time(sdate);
        appMessage.setReceiveId(create_user.getEmployeeId());
        appMessage.setSendId(update_user.getEmployeeId());
        appMessage.setIsRead(0);
        appMessage.setIsDelete(0);
        appMessageManager.sendMessageAuto(create_user, appMessage);//保存并发送消息
		
	}

	@Override
	public CustomerDataTemporary findCustomerDataTemporaryForId(Long customerData_id){
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("customer_id",customerData_id));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (CustomerDataTemporary)lst_vilage_data.get(0);
        }
        return null;
	}

	@Override
	public void deleteCustomerDataTemporaryForId(Long id) {
		CustomerDataTemporary customerDataTemporary = findCustomerDataTemporaryForById(id);
		super.removeObject(customerDataTemporary);
	}

	@Override
	public void saveReceiveData(Long id) {
		CustomerDataTemporary customerDataTemporary = findCustomerDataTemporaryForById(id);
		CustomerDataManager customerDataManager=(CustomerDataManager)SpringHelper.getBean("customerDataManager");
		CustomerManager customerManager=(CustomerManager)SpringHelper.getBean("customerManager");
		Customer customer = customerManager.findCustomerById(customerDataTemporary.getCustomer_id());
		CustomerData customerData = customerDataManager.getCustomerDataByCustomerId(customerDataTemporary.getCustomer_id());
		CustomerOperateRecordManager coRecordManager = (CustomerOperateRecordManager) SpringHelper.getBean("customerOperateRecordManager");
		Date sdate = new Date();
        CustomerOperateRecord coRecord = new CustomerOperateRecord();//客户操作记录
		if(customerData==null){
			customerData=new CustomerData();
			customerData.setCreate_user(customerDataTemporary.getCreate_user());
			customerData.setCreate_user_id(customerDataTemporary.getCreate_user_id());
		}
		BeanUtils.copyProperties(customerDataTemporary, customerData
				,new String[]{"id","version","status","create_user","create_user_id","create_time"
							,"update_time","employee_no"});
		customerData.setEmployee_no(customer.getEmployee_no());
		customerData.setUpdate_user(customerDataTemporary.getCreate_user());
		customerData.setUpdate_user_id(customerDataTemporary.getCreate_user_id());
		preObject(customerData);
		customerDataManager.save(customerData);
		
		BeanUtils.copyProperties(customerDataTemporary, coRecord,new  String[]{"id","version","status","update_time"});
		coRecord.setEmployee_create_no(customerDataTemporary.getEmployee_no());
		coRecord.setEmployee_update_no(customerDataTemporary.getEmployee_update_no());
		coRecord.setIsMoreInfo(1);
		coRecord.setType(1);
		coRecord.setUpdate_time(sdate);
		coRecordManager.saveObject(coRecord);
		
		super.removeObject(customerDataTemporary);
		
	}

	@Override
	public CustomerDataTemporary findCustomerDataTemporaryForById(Long id) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("id",id));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (CustomerDataTemporary)lst_vilage_data.get(0);
        }
        return null;
	}

	
}
