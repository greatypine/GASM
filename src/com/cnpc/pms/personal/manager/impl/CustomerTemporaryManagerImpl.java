package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.dict.entity.Dict;
import com.cnpc.pms.base.dict.manager.DictManager;
import com.cnpc.pms.base.paging.*;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.messageModel.entity.Message;
import com.cnpc.pms.messageModel.manager.MessageNewManager;
import com.cnpc.pms.messageModel.manager.MessageTemplateManager;
import com.cnpc.pms.personal.entity.*;
import com.cnpc.pms.personal.manager.*;
import com.cnpc.pms.utils.ValueUtil;
import com.ibm.db2.jcc.a.a;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 客户业务实现类
 * Created by liuxiao on 2016/6/13 0013.
 */
public class CustomerTemporaryManagerImpl extends BizBaseCommonManager implements CustomerTemporaryManager {

    @Override
    public void saveCustomerTemporary(Customer customer,CustomerOperateRecord coRecord) {
        CustomerTemporary ct = new CustomerTemporary();
        HouseStyleTemporaryManager houseStyleTemporaryManager = (HouseStyleTemporaryManager) SpringHelper.getBean("houseStyleTemporaryManager");
        FamilyTemporaryManager familyTemporaryManager = (FamilyTemporaryManager) SpringHelper.getBean("familyTemporaryManager");
        MessageNewManager appMessageManager = (MessageNewManager)SpringHelper.getBean("messageNewManager");
        UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
        //MessageTemplateManager mTemplateManager = (MessageTemplateManager)SpringHelper.getBean("messageTemplateManager");
       
        BeanUtils.copyProperties(customer,ct,new String[]{"version"});
        ct.setId(null);
        ct.setCustomer_id(customer.getId());
        ct.setEmployee_update_no(coRecord.getEmployee_update_no());
        
        
        ViewAddressCustomer addressCustomer = customer.getCustomer_address();
        if(customer.getCustomer_address() != null && customer.getCustomer_address().getHouse_id() != null
                && (checkValue(addressCustomer.getHouse_style()) || checkValue(addressCustomer.getHouse_area())
                || checkValue(addressCustomer.getHouse_toward()) || checkValue(addressCustomer.getHouse_pic()) || checkValue(addressCustomer.getHouse_type()))){
            HouseStyleTemporary houseStyleTemporary = new HouseStyleTemporary();
            houseStyleTemporary.setHouse_id(addressCustomer.getHouse_id());
            houseStyleTemporary.setHouse_area(addressCustomer.getHouse_area());
            houseStyleTemporary.setHouse_style(addressCustomer.getHouse_style());
            houseStyleTemporary.setHouse_toward(addressCustomer.getHouse_toward());
            houseStyleTemporary.setHouse_pic(addressCustomer.getHouse_pic());
            houseStyleTemporary.setHouse_type(addressCustomer.getHouse_type());
            houseStyleTemporary.setEmployee_no(ct.getEmployee_no());
            houseStyleTemporaryManager.saveObject(houseStyleTemporary);
            ct.setStyle_temporary_id(houseStyleTemporary.getId());
        }
        Set<Family> familySet = customer.getChilds();
        Date sdate = new Date();
        if(ct.getCreate_time() == null){
            ct.setCreate_time(sdate);
        }
        ct.setUpdate_time(sdate);
        ct.setCreate_user_id(customer.getUpdate_user_id());
        ct.setCreate_user(customer.getUpdate_user());
        ct.setChilds(null);
        saveObject(ct);
        
      
        
        if(familySet != null){
            for (Family family : familySet) {
                FamilyTemporary ft = new FamilyTemporary();
                BeanUtils.copyProperties(family,ft,new String[]{"version"});
                ft.setCustomer_temporary_id(ct.getId());
                ft.setCustomer_id(ct.getCustomer_id());
                preObject(ft);
               
                familyTemporaryManager.saveObject(ft);
            }
        }

        //发送消息
        Message appMessage  = new Message();
        //Map<String, Object> mtemplate = mTemplateManager.queryMessageTemplateByCode("YHHX");
        
        appMessage.setTitle("用户画像");
        appMessage.setType("customer_edit");
        //appMessage.setTemplateCode(mtemplate.get("code").toString());
        User create_user = userManager.findEmployeeByEmployeeNo(customer.getEmployee_no());
        User update_user = userManager.getUserEntity(customer.getUpdate_user_id());
        appMessage.setContent(update_user.getName() +" 修改您的用户画像，请审核！");
        appMessage.setPk_id(ct.getId());
        appMessage.setJump_path("message_monomer_portrait");
        //appMessage.setType(mtemplate.get("model").toString());
        if(create_user != null){
            
            appMessage.setCreate_user_id(update_user.getId());
            appMessage.setCreate_user(update_user.getName());
            appMessage.setUpdate_user_id(update_user.getId());
            appMessage.setUpdate_user(update_user.getName());
        }
        appMessage.setTo_employee_id(create_user.getId());
        appMessage.setReceiveId(create_user.getEmployeeId());
        appMessage.setSendId(update_user.getEmployeeId());
        appMessage.setIsRead(0);
        appMessage.setIsDelete(0);
        appMessage.setCreate_time(sdate);
        appMessage.setUpdate_time(sdate);
        appMessage.setDelete_manager("CustomerTemporaryManager");
        appMessage.setDelete_method("deleteCustomerTemporaryForId");
        appMessage.setReview_method("saveReviewCustomerTemporary");
        appMessageManager.sendMessageAuto(create_user, appMessage);//保存并发送消息
        
        
        /*appMessageManager.saveMessageAndPush(create_user,appMessage);*/
    }

    @Override
    public void saveReviewCustomerTemporary(Long customerTemporary_id) {
        CustomerManager customerManager = (CustomerManager)SpringHelper.getBean("customerManager");
        HouseStyleTemporaryManager houseStyleTemporaryManager = (HouseStyleTemporaryManager)SpringHelper.getBean("houseStyleTemporaryManager");
        HouseStyleManager houseStyleManager = (HouseStyleManager)SpringHelper.getBean("houseStyleManager");
        FamilyManager familyManager = (FamilyManager)SpringHelper.getBean("familyManager");
        FamilyTemporaryManager familyTemporaryManager = (FamilyTemporaryManager)SpringHelper.getBean("familyTemporaryManager");
        HouseCustomerManager houseCustomerManager = (HouseCustomerManager)SpringHelper.getBean("houseCustomerManager");
        RelationManager relationManager = (RelationManager) SpringHelper.getBean("relationManager");
        CustomerOperateRecordManager coRecordManager = (CustomerOperateRecordManager) SpringHelper.getBean("customerOperateRecordManager");
        
        CustomerOperateRecord coRecord = new CustomerOperateRecord();//客户操作记录
        Date sdate = new Date();
        StringBuilder familySb  = new StringBuilder();
        CustomerTemporary customerTemporary = (CustomerTemporary)this.getObject(customerTemporary_id);
        Customer customer = (Customer)customerManager.getObject(customerTemporary.getCustomer_id());
        if(customerTemporary.getStyle_temporary_id() != null){
            HouseStyleTemporary houseStyleTemporary = (HouseStyleTemporary)houseStyleTemporaryManager.getObject(customerTemporary.getStyle_temporary_id());
            HouseStyle houseStyle = houseStyleManager.getHouseStyleByHouseId(houseStyleTemporary.getHouse_id());
            if (houseStyle == null){
                houseStyle = new HouseStyle();
                houseStyle.setCreate_time(sdate);
                houseStyle.setCreate_user(houseStyleTemporary.getCreate_user());
                houseStyle.setCreate_user_id(houseStyleTemporary.getCreate_user_id());
            }
            houseStyle.setHouse_id(houseStyleTemporary.getHouse_id());
            houseStyle.setHouse_style(houseStyleTemporary.getHouse_style());
            houseStyle.setHouse_area(houseStyleTemporary.getHouse_area());
            houseStyle.setHouse_toward(houseStyleTemporary.getHouse_toward());
            houseStyle.setHouse_pic(houseStyleTemporary.getHouse_pic());
            houseStyle.setEmployee_no(houseStyleTemporary.getEmployee_no());
            houseStyle.setUpdate_time(sdate);
            houseStyle.setUpdate_user(houseStyleTemporary.getUpdate_user());
            houseStyle.setUpdate_user_id(houseStyleTemporary.getUpdate_user_id());
            houseStyleManager.saveObject(houseStyle);
            HouseCustomer houseCustomer = new HouseCustomer();
            houseCustomer.setCustomer_id(customer.getId());
            houseCustomer.setHouse_id(houseStyle.getHouse_id());
            if(houseCustomerManager.checkedHouseCustomer(houseCustomer) == 0){
                houseCustomerManager.saveHouseCustomer(houseCustomer);
            }
            //操作客户信息记录
            coRecord.setHouse_id(houseStyleTemporary.getHouse_id());
            coRecord.setHouse_id(houseStyleTemporary.getHouse_id());
            coRecord.setHouse_style(houseStyleTemporary.getHouse_style());
            coRecord.setHouse_area(houseStyleTemporary.getHouse_area());
            coRecord.setHouse_toward(houseStyleTemporary.getHouse_toward());
            coRecord.setHouse_pic(houseStyleTemporary.getHouse_pic());
            
            houseStyleTemporaryManager.removeObject(houseStyleTemporary);
           
        }
//        if(customer.getChilds() != null){
//            for (Family family : customer.getChilds()) {
//                familyManager.removeObject(family);
//            }
//        }
        customerTemporary.setUpdate_time(sdate);
        Long customer_id = customer.getId();
        BeanUtils.copyProperties(customerTemporary,customer);
        customer.setId(customer_id);
        Set<FamilyTemporary> familySet = customerTemporary.getChilds();
        customer.setChilds(null);
        customerManager.saveObject(customer);
        
        if(familySet != null){
            for (FamilyTemporary familyTemporary : familySet) {
                Family family = new Family();
                BeanUtils.copyProperties(familyTemporary,family);
                family.setId(null);
                family.setCustomer_id(customer.getId());
                family.setCreate_user(customer.getCreate_user());
                family.setCreate_user_id(customer.getCreate_user_id());
                family.setUpdate_user(customer.getUpdate_user());
                family.setUpdate_user_id(customer.getUpdate_user_id());
                family.setUpdate_time(customer.getUpdate_time());
                familyManager.saveObject(family);
                familySb.append(family.getFamily_name()+"_"+family.getFamily_phone()+",");//关联的家庭成员
                familyTemporaryManager.removeObject(familyTemporary);
            }
            coRecord.setFamily_ids(familySb.toString());
        }
       
        super.removeObject(customerTemporary);
        
        
        BeanUtils.copyProperties(customer, coRecord,new String[]{"version"});
        
        coRecord.setId(null);
        coRecord.setCustomer_id(customer.getId());
        coRecord.setEmployee_create_no(customerTemporary.getEmployee_no());
        coRecord.setEmployee_update_no(customerTemporary.getEmployee_update_no());
        coRecord.setType(1);
        coRecord.setCreate_user(customer.getUpdate_user());
        coRecord.setCreate_user_id(customer.getUpdate_user_id());
        coRecord.setCreate_time(customerTemporary.getUpdate_time());
        coRecord.setUpdate_time(sdate);
        coRecord.setUpdate_user(customer.getUpdate_user());
        coRecord.setUpdate_user_id(customer.getUpdate_user_id());
        coRecord.setIsMoreInfo(0);
        coRecordManager.saveObject(coRecord);
    }

    @Override
    public CustomerTemporary findCustomerTemporaryForId(Long customerTemporary_id) {
        CustomerManager customerManager = (CustomerManager)SpringHelper.getBean("customerManager");

        HouseStyleTemporaryManager houseStyleTemporaryManager = (HouseStyleTemporaryManager)SpringHelper.getBean("houseStyleTemporaryManager");

        String web = PropertiesUtil.getValue("file.web.root").concat("user_image").concat(File.separator);

        String picPath = PropertiesUtil.getValue("file.root").concat("user_image").concat(File.separator);

        CustomerTemporary customerTemporary = (CustomerTemporary)this.getObject(customerTemporary_id);
        if(customerTemporary.getStyle_temporary_id() != null){
            customerTemporary.setHouseStyle((HouseStyleTemporary)houseStyleTemporaryManager.getObject(customerTemporary.getStyle_temporary_id()));
        }
        Customer customer = (Customer)customerManager.getObject(customerTemporary.getCustomer_id());
        customerTemporary.setCustomer(customer);
        if(customerTemporary.getCustomer_pic() != null && !"".equals(customerTemporary.getCustomer_pic())){
            File pic_dir = new File(picPath);
            final String customer_pic = customerTemporary.getCustomer_pic();
            File[] file_pics = pic_dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().contains(customer_pic);
                }
            });
            customerTemporary.setCus_pic(file_pics == null || file_pics.length == 0 ? null : file_pics[0].getName());
            if(customerTemporary.getCus_pic() != null) {
                customerTemporary.setCus_pic(web.concat(customerTemporary.getCus_pic()));
            }
        }
        return customerTemporary;
    }

    @Override
    public void deleteCustomerTemporaryForId(Long customerTemporary_id) {
        HouseStyleTemporaryManager houseStyleTemporaryManager = (HouseStyleTemporaryManager) SpringHelper.getBean("houseStyleTemporaryManager");
        FamilyTemporaryManager familyTemporaryManager = (FamilyTemporaryManager) SpringHelper.getBean("familyTemporaryManager");
        
        CustomerOperateRecordManager coRecordManager = (CustomerOperateRecordManager) SpringHelper.getBean("customerOperateRecordManager");
        CustomerTemporary customerTemporary = (CustomerTemporary)this.getObject(customerTemporary_id);
        if(customerTemporary!=null){
        	Set<FamilyTemporary> familySet = customerTemporary.getChilds();
            if(familySet != null){
                for (FamilyTemporary familyTemporary : familySet) {
                    familyTemporaryManager.removeObject(familyTemporary);
                }
            }

            if(customerTemporary.getStyle_temporary_id() != null) {
                HouseStyleTemporary houseStyleTemporary = (HouseStyleTemporary) houseStyleTemporaryManager.getObject(customerTemporary.getStyle_temporary_id());
                houseStyleTemporaryManager.removeObject(houseStyleTemporary);
            }

            super.removeObject(customerTemporary);
        }
        
        
    }


    private Boolean checkValue(Object value){
        return !(value == null || "".equals(value));
    }

}
