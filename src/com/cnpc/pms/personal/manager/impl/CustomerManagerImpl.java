package com.cnpc.pms.personal.manager.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.dict.entity.Dict;
import com.cnpc.pms.base.dict.manager.DictManager;
import com.cnpc.pms.base.file.comm.utils.StringUtils;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.messageModel.manager.MessageNewManager;
import com.cnpc.pms.personal.dao.CustomerDao;
import com.cnpc.pms.personal.dao.RelationDao;
import com.cnpc.pms.personal.entity.BeforeDateCustomer;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.CustomerData;
import com.cnpc.pms.personal.entity.CustomerOperateRecord;
import com.cnpc.pms.personal.entity.Family;
import com.cnpc.pms.personal.entity.HouseCustomer;
import com.cnpc.pms.personal.entity.HouseStyle;
import com.cnpc.pms.personal.entity.Relation;
import com.cnpc.pms.personal.entity.RelationContent;
import com.cnpc.pms.personal.entity.ViewAddressCustomer;
import com.cnpc.pms.personal.manager.CustomerDataManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.CustomerOperateRecordManager;
import com.cnpc.pms.personal.manager.CustomerTemporaryManager;
import com.cnpc.pms.personal.manager.FamilyManager;
import com.cnpc.pms.personal.manager.HouseCustomerManager;
import com.cnpc.pms.personal.manager.HouseStyleManager;
import com.cnpc.pms.personal.manager.RelationManager;
import com.cnpc.pms.personal.manager.ViewAddressCustomerManager;
import com.cnpc.pms.utils.ValueUtil;

/**
 * 客户业务实现类
 * Created by liuxiao on 2016/6/13 0013.
 */
public class CustomerManagerImpl extends BizBaseCommonManager implements CustomerManager {

    /**
     * 根据导入的Excel文件导入数据
     * @param lst_customer_excel Excel文件集合
     */
    @Override
    public void saveOrUpdateCustomerByExcel(List<File> lst_customer_excel) throws Exception {

        DictManager dictManager = (DictManager) SpringHelper.getBean("dictManager");

        for(File file_excel : lst_customer_excel) {
            //读取excel文件
            InputStream is_excel = new FileInputStream(file_excel);
            //Excel工作簿
            Workbook wb_excel;
            Sheet sheet_data;
            try {
                //解析2003的xls模式的excel
                wb_excel = new HSSFWorkbook(is_excel);
            } catch (Exception e) {
                //如果2003模式解析异常，尝试解析2007模式
                wb_excel = new XSSFWorkbook(file_excel.getAbsolutePath());
            }
            if(wb_excel.getNumberOfSheets() == 5){
                sheet_data = wb_excel.getSheetAt(4);
            }else{
                sheet_data = wb_excel.getSheetAt(0);
            }

            //客户性别
            Map<String,Dict> map_sex_dict = dictManager.getDict("customer_sex");

            //租房性质
            Map<String,Dict> map_house_pro_dict = dictManager.getDict("house_property_resource");

            //加班频率
            Map<String,Dict> map_work_overtime_dict = dictManager.getDict("work_overtime_resource");


            //获取行数
            int n_rowCount = sheet_data.getPhysicalNumberOfRows();
            //如果只有两行说明，只有表头没有导入数据
            if(n_rowCount <= 2){
                throw new Exception("工作簿中不足两行，缺少导入数据");
            }
            for(int i = 2;i < n_rowCount;i++){
                //每行的数据值
                Row row_excel_data = sheet_data.getRow(i);
                String str_name = getCellValue(row_excel_data.getCell(1));
                if("".equals(str_name) || str_name == null){
                    continue;
                }
                String str_phone = getCellValue(row_excel_data.getCell(2));

                String str_address = getCellValue(row_excel_data.getCell(7));

                List<?> lst_customer_object = super.getList(FilterFactory.getSimpleFilter("name",str_name)
                        .appendAnd(FilterFactory.getSimpleFilter("mobilephone",str_phone).appendOr(FilterFactory.getSimpleFilter("address",str_address))));
                Customer customer = null;
                if(lst_customer_object != null && lst_customer_object.size() > 0){
                    customer = (Customer)lst_customer_object.get(0);
                }else{
                    customer = new Customer();
                }
                customer.setName(str_name);
                customer.setMobilephone(str_phone);
                customer.setCustomer_pic(getCellValue(row_excel_data.getCell(3)));
                String str_sex_name = getCellValue(row_excel_data.getCell(4));
                for(Dict obj_sex_dict : map_sex_dict.values()){
                    if(str_sex_name.equals(obj_sex_dict.getDictText())){
                        customer.setSex(Integer.valueOf(obj_sex_dict.getDictCode()));
                        break;
                    }
                }
                customer.setAge(getIntegerValue(getCellValue(row_excel_data.getCell(5))));
                customer.setNationality(getCellValue(row_excel_data.getCell(6)));
                customer.setAddress(str_address);

                String str_house_property = getCellValue(row_excel_data.getCell(8));

                for(Dict obj_hp_dict : map_house_pro_dict.values()){
                    if(str_house_property.trim().equals(obj_hp_dict.getDictText().trim())){
                        customer.setHouse_property(obj_hp_dict.getDictCode());
                        break;
                    }
                }
                customer.setJob(getCellValue(row_excel_data.getCell(9)));


                customer.setIncome(getCellValue(row_excel_data.getCell(10)));

                String str_work_overtime = getCellValue(row_excel_data.getCell(11));
                for(Dict obj_wo_dict : map_work_overtime_dict.values()){
                    if(str_work_overtime.trim().equals(obj_wo_dict.getDictText().trim())){
                        customer.setWork_overtime(obj_wo_dict.getDictCode());
                        break;
                    }
                }

                customer.setOther(getCellValue(row_excel_data.getCell(12)));
                customer.setFamily_num(getCellValue(row_excel_data.getCell(13)));
                customer.setPreschool_num(getIntegerValue(getCellValue(row_excel_data.getCell(14))));
                customer.setMinor_num(getIntegerValue(getCellValue(row_excel_data.getCell(15))));
                customer.setPet_type(getCellValue(row_excel_data.getCell(16)));
                preObject(customer);
                saveObject(customer);
            }
            is_excel.close();
        }
    }

    
    
    /**
     * 拜访记录中 根据电话和姓名  查询新增的用户
     */
    @Override
    public Result findCustomerListByNamePhone(Customer customer){
    	Result result = new Result();
    	CustomerManager customerManager = (CustomerManager) SpringHelper.getBean("customerManager");
    	
    	StringBuffer sbf = new StringBuffer();
    	sbf.append(" 1=1 ");
    	if(customer.getMobilephone()!=null&&customer.getMobilephone().length()>0){
    		sbf.append(" and mobilephone='"+customer.getMobilephone()+"' ");
    	}
    	if(customer.getName()!=null&&customer.getName().length()>0){
    		sbf.append(" and name='"+customer.getName()+"' ");
    	}
    	
    	IFilter iFilter = FilterFactory.getSimpleFilter(sbf.toString());
    	List<Customer> grp_list = (List<Customer>) customerManager.getList(iFilter);
    	RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
    	List<Customer> lst_relation_grp = new ArrayList<Customer>();
    	if(grp_list!=null&&grp_list.size()>0){
    		for(Customer c:grp_list){
    			Relation relation = relationDao.queryMaxDateCount(c.getId());
    			String count = relation.getTotalcount()==null?"0":relation.getTotalcount();
    			c.setMinor_num(Integer.parseInt(count));
    			c.setWork_overtime(relation.getLastdate());
    			lst_relation_grp.add(c);
    		}
    	}
    	
    	result.setData(lst_relation_grp);
		result.setCode(CodeEnum.success.getValue());
        result.setMessage(CodeEnum.success.getDescription());
    	
    	return result;
    }
    

    /**
     * 查询客户集合
     * @param customer 客户对象，要查询的条件
     * @return 客户对象
     */
    @Override
    public Result findCustomerList(Customer customer) {
        ViewAddressCustomerManager viewAddressCustomerManager = (ViewAddressCustomerManager)SpringHelper.getBean("viewAddressCustomerManager");
        UserManager userManager = (UserManager)SpringHelper.getBean("userManager");

        Result result = new Result();

        IFilter filter = FilterFactory.getSimpleFilter("1=1");

        if(ValueUtil.checkValue(customer.getName())){
            filter = filter.appendAnd(FilterFactory.getEq("name",customer.getName()));
        }

        if(ValueUtil.checkValue(customer.getMobilephone())){
            filter =  filter.appendAnd(FilterFactory.getEq("mobilephone",customer.getMobilephone()));
        }

        if(ValueUtil.checkValue(customer.getAddress())){
            filter =  filter.appendAnd(FilterFactory.getSimpleFilter("address like '%"+customer.getAddress()+"%'"));
        } 

        if(ValueUtil.checkValue(customer.getCreate_time())){
            Date sdate = new Date();
            filter =  filter.appendAnd(FilterFactory.getBetween("create_time",customer.getCreate_time(),sdate)
                    .appendOr(FilterFactory.getBetween("update_time",customer.getCreate_time(),sdate)));
        }

        if(ValueUtil.checkValue(customer.getEmployee_no())){
            filter =  filter.appendAnd(FilterFactory.getSimpleFilter("employee_no",customer.getEmployee_no()));
        }

        if(ValueUtil.checkValue(customer.getStore_id())){
            List<User> lst_employee = userManager.findNamesBySid(customer.getStore_id().toString()).getUserList();
            if(lst_employee != null && lst_employee.size() > 0){
                List<String> lst_employeeno = new ArrayList<String>();
                for(User user : lst_employee){
                    lst_employeeno.add(user.getEmployeeId());
                }
                filter =  filter.appendAnd(FilterFactory.getInFilter("employee_no",lst_employeeno));
            }
        }

        List<?> lst_result = this.getList(filter);

        result.setCode(CodeEnum.success.getValue());
        result.setMessage(CodeEnum.success.getDescription());


        if(lst_result == null || lst_result.size() == 0){
            result.setListCustomer(new ArrayList<Customer>());
        }else{
            result.setListCustomer((List<Customer>) lst_result);

            String web = PropertiesUtil.getValue("file.web.root").concat("user_image").concat(File.separator);

            String picPath = PropertiesUtil.getValue("file.root").concat("user_image").concat(File.separator);

            for(Customer obj_customer : result.getListCustomer()){
                obj_customer.setRelations(obj_customer.getRelations());
                FSP fsp = new FSP();
                fsp.setSort(SortFactory.createSort("house_id", ISort.DESC));
                fsp.setUserFilter(FilterFactory.getSimpleFilter("customer_id",obj_customer.getId()));
                List<?> lst_address = viewAddressCustomerManager.getList(fsp);
                if(lst_address != null && lst_address.size() > 0){
                    obj_customer.setCustomer_address((ViewAddressCustomer)lst_address.get(0));
                    if(obj_customer.getCustomer_address().getHouse_type() == 1){
                    	
                    	String building_name = obj_customer.getCustomer_address().getBuilding_name()==null?"":obj_customer.getCustomer_address().getBuilding_name();
                        String unit_no = obj_customer.getCustomer_address().getUnit_no()==null?"":obj_customer.getCustomer_address().getUnit_no();
                    	String house_no = obj_customer.getCustomer_address().getHouse_no()==null?"":obj_customer.getCustomer_address().getHouse_no();
                       
                    	obj_customer.setAddress(obj_customer.getCustomer_address().getTv_name()
                                .concat(building_name.concat("号楼"))
                                .concat(unit_no.concat("单元"))
                                .concat(house_no.concat("号")));
                    }else if(obj_customer.getCustomer_address().getHouse_type() == 0){
                    	String house_no = obj_customer.getCustomer_address().getHouse_no()==null?"":obj_customer.getCustomer_address().getHouse_no();
                        obj_customer.setAddress(obj_customer.getCustomer_address().getTv_name()
                                .concat(house_no.concat("号")));
                    }
                }
                if(obj_customer.getCustomer_pic() != null && !"".equals(obj_customer.getCustomer_pic())){
                    File pic_dir = new File(picPath);
                    final String customer_pic = obj_customer.getCustomer_pic();
                    File[] file_pics = pic_dir.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.getName().contains(customer_pic);
                        }
                    });
                    obj_customer.setCus_pic(file_pics == null || file_pics.length == 0 ? null : file_pics[0].getName());
                    if(obj_customer.getCus_pic() != null) {
                        obj_customer.setCus_pic(web.concat(obj_customer.getCus_pic()));
                    }
                }
            }
        }

        return result;
    }

    /**
     * 查询客户集合
     * @param customer 客户对象，要查询的条件
     * @return 客户对象
     */
    @Override
    public Customer findCustomer(Customer customer) {


        IFilter filter = FilterFactory.getSimpleFilter("1=1");

        if(ValueUtil.checkValue(customer.getName())){
            filter = filter.appendAnd(FilterFactory.getEq("name",customer.getName()));
        }

        if(ValueUtil.checkValue(customer.getMobilephone())){
            filter =  filter.appendAnd(FilterFactory.getEq("mobilephone",customer.getMobilephone()));
        }

        if(ValueUtil.checkValue(customer.getAddress())){
            filter =  filter.appendAnd(FilterFactory.getSimpleFilter("address like '%"+customer.getAddress()+"%'"));
        }

        List<?> lst_result = this.getList(filter);

        if(lst_result != null && lst_result.size() > 0){
            return (Customer) lst_result.get(0);
        }
        return null;
    }

    @Override
    public Customer saveOrUpdateCustomer(Customer customer) {
        FamilyManager familyManager = (FamilyManager) SpringHelper.getBean("familyManager");
        Customer save_customer = null;
        if(ValueUtil.checkValue(customer.getId())){
            save_customer = (Customer)this.getObject(customer.getId());
        }
        if(save_customer == null){
            save_customer = new Customer();
        }else{
            customer.setCreate_user(save_customer.getCreate_user());
            customer.setCreate_user_id(save_customer.getCreate_user_id());
            customer.setCreate_time(save_customer.getCreate_time());
            if(save_customer.getChilds() != null){
                for (Family family : save_customer.getChilds()) {
                    familyManager.removeObject(family);
                }
            }
        }
        BeanUtils.copyProperties(customer,save_customer);
        Set<Family> familySet = customer.getChilds();
        save_customer.setChilds(null);
        preObject(save_customer);
        saveObject(save_customer);
        if(familySet != null){
            for (Family family : familySet) {
                family.setCustomer_id(save_customer.getId());
                family.setCreate_user(save_customer.getCreate_user());
                family.setCreate_user_id(save_customer.getCreate_user_id());
                family.setCreate_time(save_customer.getCreate_time());
                family.setUpdate_user(save_customer.getUpdate_user());
                family.setUpdate_user_id(save_customer.getUpdate_user_id());
                family.setUpdate_time(save_customer.getUpdate_time());
                familyManager.saveObject(family);
            }
        }
        return save_customer;
    }


    private Integer getIntegerValue(Object value){
        if(value != null){
            return Double.valueOf(value.toString()).intValue();
        }
        return null;
    }


    /**
     * 查询客户集合
     * @param customer 客户对象，要查询的条件
     * @return 客户对象
     */
    @Override
    public Customer findCustomerInfo(Customer customer) {
        ViewAddressCustomerManager viewAddressCustomerManager = (ViewAddressCustomerManager)SpringHelper.getBean("viewAddressCustomerManager");
        FamilyManager familyManager = (FamilyManager)SpringHelper.getBean("familyManager");
        String[] str_relations = {"配偶","祖父母","父母","子女","孙儿/孙女","其他"};
        String[] str_matchRelations = {"配偶","孙儿/孙女","子女","父母","祖父母","其他"};
        IFilter filter = FilterFactory.getSimpleFilter("1=1");
        Long customer_id = null;
        if(ValueUtil.checkValue(customer.getName())){
            filter = filter.appendAnd(FilterFactory.getEq("name",customer.getName()));
        }

        if(ValueUtil.checkValue(customer.getMobilephone())){
            filter =  filter.appendAnd(FilterFactory.getEq("mobilephone",customer.getMobilephone()));
        }

        String web = PropertiesUtil.getValue("file.web.root").concat("user_image").concat(File.separator);

        String picPath = PropertiesUtil.getValue("file.root").concat("user_image").concat(File.separator);

        List<?> lst_result = this.getList(filter);
        Customer obj_customer = null;
        if(lst_result != null && lst_result.size() > 0){
            obj_customer =  (Customer) lst_result.get(0);
            obj_customer.setRelations(obj_customer.getRelations());
            customer_id = obj_customer.getId();
        }else if(!StringUtils.isBlank(customer.getName()) && !StringUtils.isBlank(customer.getMobilephone())){
            filter = FilterFactory.getEq("family_name",customer.getName()).appendAnd(FilterFactory.getEq("family_phone",customer.getMobilephone()));
            List<Family> lst_family = (List<Family>)familyManager.getList(filter);
            if(lst_family != null && lst_family.size() > 0){
                List<Long> lst_customer_id = new ArrayList<Long>();
                Map<Long,String> map_relation = new HashMap<Long, String>();
                for(Family family : lst_family){
                    map_relation.put(family.getCustomer_id(),family.getFamily_relation());
                    lst_customer_id.add(family.getCustomer_id());
                }
                List<Customer> lst_customer = (List<Customer>)this.getList(FilterFactory.getInFilter("id",lst_customer_id));//家人
                Family family = lst_family.get(0);
                obj_customer = new Customer();
                obj_customer.setName(family.getFamily_name());
                obj_customer.setMobilephone(family.getFamily_phone());
                obj_customer.setAge(family.getFamily_age());
                customer_id = lst_customer.get(0).getId();
                obj_customer.setChilds(new HashSet<Family>());
                for(Customer family_customer : lst_customer){
                    Family customer_family = new Family();
                    customer_family.setFamily_name(family_customer.getName());
                    customer_family.setFamily_age(family_customer.getAge());
                    customer_family.setFamily_phone(family_customer.getMobilephone());
                    String str_relation = map_relation.get(family_customer.getId());
                    if(!StringUtils.isBlank(str_relation)){
                        int nRelIndex = Arrays.binarySearch(str_relations, str_relation.trim());
                        if(nRelIndex > -1){
                            customer_family.setFamily_relation(str_matchRelations[nRelIndex]);
                        }else {
                            customer_family.setFamily_relation(str_relation);
                        }
                    }
                    obj_customer.getChilds().add(customer_family);
                }
            }
        }
        if(obj_customer != null){
            FSP fsp = new FSP();
            fsp.setSort(SortFactory.createSort("house_id", ISort.DESC));
            fsp.setUserFilter(FilterFactory.getSimpleFilter("customer_id",customer_id));
            List<?> lst_address = viewAddressCustomerManager.getList(fsp);
            if(lst_address != null && lst_address.size() > 0){
                obj_customer.setCustomer_address((ViewAddressCustomer)lst_address.get(0));
                
               
               
            	if(obj_customer.getCustomer_address().getHouse_type() == 1){
            		String tv_name=obj_customer.getCustomer_address().getTv_name()==null?"":obj_customer.getCustomer_address().getTv_name();
            		String building_name = obj_customer.getCustomer_address().getBuilding_name()==null?"":obj_customer.getCustomer_address().getBuilding_name();
                    String unit_no = obj_customer.getCustomer_address().getUnit_no()==null?"":obj_customer.getCustomer_address().getUnit_no();
                 	String house_no = obj_customer.getCustomer_address().getHouse_no()==null?"":obj_customer.getCustomer_address().getHouse_no();
            		
                 	obj_customer.setAddress(tv_name.concat(building_name.concat("号楼"))
                            .concat(unit_no.concat("单元"))
                            .concat(house_no.concat("号")));
                }else if(obj_customer.getCustomer_address().getHouse_type() == 0){
                	String house_no = obj_customer.getCustomer_address().getHouse_no()==null?"":obj_customer.getCustomer_address().getHouse_no();
            		String tv_name=obj_customer.getCustomer_address().getTv_name()==null?"":obj_customer.getCustomer_address().getTv_name();
                	obj_customer.setAddress(tv_name.concat(house_no.concat("号")));
                }
            }
            if(obj_customer.getCustomer_pic() != null && !"".equals(obj_customer.getCustomer_pic())){
                File pic_dir = new File(picPath);
                final String customer_pic = obj_customer.getCustomer_pic();
                File[] file_pics = pic_dir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().contains(customer_pic);
                    }
                });
                obj_customer.setCus_pic(file_pics == null || file_pics.length == 0 ? null : file_pics[0].getName());
                if(obj_customer.getCus_pic() != null) {
                    obj_customer.setCus_pic(web.concat(obj_customer.getCus_pic()));
                }
            }
        }
        return obj_customer;
    }

    public Customer getCustomerByNameAndMobilephone(Customer customer){
        List<?> lst_result = this.getList(FilterFactory.getSimpleFilter("name",customer.getName())
                .appendAnd(FilterFactory.getSimpleFilter("mobilephone",customer.getMobilephone())));
        if(lst_result != null && lst_result.size() > 0){
            return (Customer)lst_result.get(0);
        }
        return null;
    }


    @Override
    public Customer saveOrUpdateCustomerAndHouse(Customer customer) {
        FamilyManager familyManager = (FamilyManager) SpringHelper.getBean("familyManager");
        CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
        HouseCustomerManager houseCustomerManager = (HouseCustomerManager) SpringHelper.getBean("houseCustomerManager");
        HouseStyleManager houseStyleManager = (HouseStyleManager) SpringHelper.getBean("houseStyleManager");
        CustomerTemporaryManager customerTemporaryManager = (CustomerTemporaryManager) SpringHelper.getBean("customerTemporaryManager");
        RelationManager relationManager = (RelationManager) SpringHelper.getBean("relationManager");
        UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
        CustomerOperateRecordManager coRecordManager = (CustomerOperateRecordManager) SpringHelper.getBean("customerOperateRecordManager");
        MessageNewManager messageManager = (MessageNewManager)SpringHelper.getBean("messageNewManager");
        StringBuilder familySb  = new StringBuilder();
        Date sdate = new Date();
        Customer save_customer = null;
        CustomerOperateRecord coRecord = null;//操作记录
        Relation lastRelation=null;
        if(ValueUtil.checkValue(customer.getId())){//当前顾客已存在
            save_customer = (Customer)this.getObject(customer.getId());
        }else if(ValueUtil.checkValue(customer.getName()) && ValueUtil.checkValue(customer.getMobilephone())){//当前顾客已存在
            save_customer = getCustomerByNameAndMobilephone(customer);
        }
        boolean isAdd = false;
        coRecord = new CustomerOperateRecord();
        if(save_customer == null){//当前客户是新客户（还不存在）
            isAdd = true;
            save_customer = new Customer();
            
        }else{//当前客户已存在
            Set<Relation> relationSet = customer.getRelations();
            if(relationSet != null){//拜访记录（暂时无用）
                for (Relation relation : relationSet) {
                    if(relation.getId() != null){
                        continue;
                    }
                    Set<RelationContent> relationContentSet = relation.getChilds();
                    relation.setChilds(null);
                    relation.setCustomer_id(save_customer.getId());
                    relation.setCreate_user(relation.getCreate_user());
                    relation.setCreate_user_id(relation.getCreate_user_id());
                    relation.setCreate_time(sdate);
                    relation.setUpdate_user(relation.getUpdate_user());
                    relation.setUpdate_user_id(relation.getUpdate_user_id());
                    relation.setUpdate_time(sdate);
                    relationManager.saveObject(relation);
                    if(relation.getChilds() == null){
                        relation.setChilds(new HashSet<RelationContent>());
                    }
                    if(relationContentSet != null){
                        for(RelationContent content : relationContentSet){
                            content.setRelation_id(relation.getId());
                            relation.getChilds().add(content);
                        }
                    }

                    relationManager.saveObject(relation);
                    if(save_customer.getRelations() == null){
                        save_customer.setRelations(new TreeSet<Relation>());
                    }
                    save_customer.getRelations().add(relation);
                }
            }
            if(save_customer.getEmployee_no()!=null){
            	
            }
           
           
            if(save_customer.getEmployee_no() != null && !"".equals(save_customer.getEmployee_no())){
            	 User employee = userManager.findEmployeeByEmployeeNo(save_customer.getEmployee_no());//原国安侠
                 
                 Map<String, Object> storekeeper = customerDao.queryStoreKeeper(employee.getStore_id());//原国安侠所在门店店长
            	
                 if(!storekeeper.get("employeeId").equals(customer.getEmployee_no())){
            		 if(employee != null && employee.getDisabledFlag() == 1&&!customer.getEmployee_no().equals(save_customer.getEmployee_no())){//客户原国安侠还有效且当前国安侠不是原国安侠
                         coRecord.setEmployee_create_no(save_customer.getEmployee_no());//原创建国安侠
                         coRecord.setEmployee_update_no(customer.getEmployee_no());//更新操作国安侠
                         coRecord.setIsMoreInfo(0);//基本信息 0 更多信息1
                         customer.setEmployee_no(save_customer.getEmployee_no());
                         customerTemporaryManager.saveCustomerTemporary(customer,coRecord);//保存临时记录
                         return customer;

                     }
                }

            }
            if(save_customer.getChilds() != null){//原客户信息有家庭成员
                for (Family family : save_customer.getChilds()) {
                    familyManager.removeObject(family);
                }
            }
        }
        //国安侠操作客户记录(新建或者修改自己的维护客户)
        BeanUtils.copyProperties(customer, coRecord,new String[]{"version","create_time","create_user","create_user_id"});
        coRecord.setCreate_time(sdate);
      	coRecord.setUpdate_time(sdate);
        if(isAdd){
       	 coRecord.setEmployee_create_no(customer.getEmployee_no());//新增创建人为当前国安侠
       	 coRecord.setEmployee_update_no(customer.getEmployee_no());
       	 coRecord.setType(0);
       	 
       }else{
       	 coRecord.setEmployee_create_no(save_customer.getEmployee_no());//修改创建人为原国安侠
       	 coRecord.setEmployee_update_no(customer.getEmployee_no());
       	 coRecord.setType(1);
       	 
       }
        
        if(isAdd){
            BeanUtils.copyProperties(customer,save_customer,new String[]{"id","create_time","create_user","create_user_id"});
        }else{
            BeanUtils.copyProperties(customer,save_customer,new String[]{"id","employee_no","create_time","create_user","create_user_id"});
        }
        if(save_customer.getCreate_user_id()  == null){
            save_customer.setCreate_time(sdate);
            save_customer.setCreate_user(customer.getUpdate_user());
            save_customer.setCreate_user_id(customer.getUpdate_user_id());
        }
        save_customer.setUpdate_time(sdate);
        Set<Family> familySet = customer.getChilds();
        save_customer.setChilds(null);
        save_customer.setRelations(null);
        preObject(save_customer);
        saveObject(save_customer);
       
        //国安侠操作客户记录
        if(coRecord.getCreate_user_id()  == null){
        	coRecord.setCreate_user(customer.getUpdate_user());
        	coRecord.setCreate_user_id(customer.getUpdate_user_id());
        }
       
        coRecord.setIsMoreInfo(0);//基本信息 0 更多信息1
        
        coRecord.setEmployee_update_no(customer.getEmployee_no());
        coRecord.setCustomer_id(save_customer.getId());
        if(isAdd){
        	
            Set<Relation> relationSet = customer.getRelations();
            if(relationSet != null){
            	
                for (Relation relation : relationSet) {
                    if(relation.getId() != null){
                        continue;
                    }
                    Set<RelationContent> relationContentSet = relation.getChilds();
                    relation.setChilds(null);
                    relation.setCustomer_id(save_customer.getId());
                    relation.setCreate_user(relation.getCreate_user());
                    relation.setCreate_user_id(relation.getCreate_user_id());
                    relation.setCreate_time(sdate);
                    relation.setUpdate_user(relation.getUpdate_user());
                    relation.setUpdate_user_id(relation.getUpdate_user_id());
                    relation.setUpdate_time(sdate);
                    relationManager.saveObject(relation);
                    if(relation.getChilds() == null){
                        relation.setChilds(new HashSet<RelationContent>());
                    }
                    if(relationContentSet != null){
                        for(RelationContent content : relationContentSet){
                            content.setRelation_id(relation.getId());
                            relation.getChilds().add(content);
                        }
                    }
                    relationManager.saveObject(relation);
                    if(save_customer.getRelations() == null){
                        save_customer.setRelations(new TreeSet<Relation>());
                    }
                    save_customer.getRelations().add(relation);
                    lastRelation = relation;
                }
                
            }
        }
        if(familySet != null){
            for (Family family : familySet) {
            	
                family.setCustomer_id(save_customer.getId());
                family.setCreate_user(save_customer.getCreate_user());
                family.setCreate_user_id(save_customer.getCreate_user_id());
                family.setCreate_time(save_customer.getCreate_time());
                family.setUpdate_user(save_customer.getUpdate_user());
                family.setUpdate_user_id(save_customer.getUpdate_user_id());
                family.setUpdate_time(save_customer.getUpdate_time());
                family.setId(null);
                familyManager.saveObject(family);
                familySb.append(family.getFamily_name()+"_"+family.getFamily_phone()+",");
            }
            coRecord.setFamily_ids(familySb.toString());
        }

        if(customer.getCustomer_address() != null && customer.getCustomer_address().getHouse_id() != null){
        	
        	coRecord.setHouse_id(customer.getCustomer_address().getHouse_id());
        	
            HouseCustomer houseCustomer = new HouseCustomer();
            houseCustomer.setHouse_id(customer.getCustomer_address().getHouse_id());
            houseCustomer.setCustomer_id(save_customer.getId());
            int house_customer_count = houseCustomerManager.checkedHouseCustomer(houseCustomer);
            if(house_customer_count == 0){
                preObject(houseCustomer);
                houseCustomerManager.saveHouseCustomer(houseCustomer);
                coRecord.setHouse_id(houseCustomer.getHouse_id());
            }
            if(isAdd){
            	messageManager.sendMessage_area_news(2,save_customer.getId().toString());//发送动态消息
            }
            HouseStyle houseStyle = houseStyleManager.getHouseStyleByHouseId(houseCustomer.getHouse_id());
            if(houseStyle == null){
                houseStyle = new HouseStyle();
                houseStyle.setHouse_id(houseCustomer.getHouse_id());
                houseStyle.setCreate_user(save_customer.getCreate_user());
                houseStyle.setCreate_user_id(save_customer.getCreate_user_id());
                houseStyle.setCreate_time(sdate);
            }
            houseStyle.setHouse_area(customer.getCustomer_address().getHouse_area()==""?null:customer.getCustomer_address().getHouse_area());
            houseStyle.setHouse_toward(customer.getCustomer_address().getHouse_toward());
            houseStyle.setHouse_style(customer.getCustomer_address().getHouse_style());
            houseStyle.setHouse_pic(customer.getCustomer_address().getHouse_pic());
            houseStyle.setUpdate_user(save_customer.getUpdate_user());
            houseStyle.setUpdate_user_id(save_customer.getUpdate_user_id());
            houseStyle.setUpdate_time(save_customer.getUpdate_time());
            houseStyle.setEmployee_no(save_customer.getEmployee_no());
            houseStyleManager.saveObject(houseStyle);
          
            //房屋户型等
            coRecord.setHouse_area(customer.getCustomer_address().getHouse_area()==""?null:customer.getCustomer_address().getHouse_area());
            coRecord.setHouse_toward(customer.getCustomer_address().getHouse_toward());
            coRecord.setHouse_style(customer.getCustomer_address().getHouse_style());
            coRecord.setHouse_pic(customer.getCustomer_address().getHouse_pic());
           
        }
        coRecordManager.saveObject(coRecord);//保存操作记录
        
        if(lastRelation!=null){
        	messageManager.sendMessage_area_news(3,lastRelation.getId().toString());//发送动态消息
        }
        return save_customer;
    }


    @Override
    public Customer findCustomerIdAndHouseId(Long customer_id, Long house_id) {
        ViewAddressCustomerManager viewAddressCustomerManager = (ViewAddressCustomerManager)SpringHelper.getBean("viewAddressCustomerManager");

        Object object = this.getObject(customer_id);
        String web = PropertiesUtil.getValue("file.web.root").concat("user_image").concat(File.separator);

        String picPath = PropertiesUtil.getValue("file.root").concat("user_image").concat(File.separator);
        if(object != null){
            Customer obj_customer =  (Customer) object;
            obj_customer.setRelations(obj_customer.getRelations());
            FSP fsp = new FSP();
            if(house_id != null){
                fsp.setSort(SortFactory.createSort("house_id", ISort.DESC));
                fsp.setUserFilter(FilterFactory.getSimpleFilter("house_id",house_id));
                List<?> lst_address = viewAddressCustomerManager.getList(fsp);
                if(lst_address != null && lst_address.size() > 0){
                    obj_customer.setCustomer_address((ViewAddressCustomer)lst_address.get(0));
                    if(obj_customer.getCustomer_address().getHouse_type() == 1){
                    	String building_name = obj_customer.getCustomer_address().getBuilding_name()==null?"":obj_customer.getCustomer_address().getBuilding_name();
                        String unit_no = obj_customer.getCustomer_address().getUnit_no()==null?"":obj_customer.getCustomer_address().getUnit_no();
                    	String house_no = obj_customer.getCustomer_address().getHouse_no()==null?"":obj_customer.getCustomer_address().getHouse_no();
                    	String  tiny_village =  obj_customer.getCustomer_address().getTv_name()==null?"":obj_customer.getCustomer_address().getTv_name();
                    	obj_customer.setAddress(tiny_village
                                .concat(building_name.concat("号楼"))
                                .concat(unit_no.concat("单元"))
                                .concat(house_no.concat("号")));
                    }else if(obj_customer.getCustomer_address().getHouse_type() == 0){
                    	String house_no = obj_customer.getCustomer_address().getHouse_no()==null?"":obj_customer.getCustomer_address().getHouse_no();
                    	String  tiny_village =  obj_customer.getCustomer_address().getTv_name()==null?"":obj_customer.getCustomer_address().getTv_name();

                    	obj_customer.setAddress(tiny_village
                                .concat(house_no.concat("号")));
                    }
                }
            }

            if(obj_customer.getCustomer_pic() != null && !"".equals(obj_customer.getCustomer_pic())){
                File pic_dir = new File(picPath);
                final String customer_pic = obj_customer.getCustomer_pic();
                File[] file_pics = pic_dir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().contains(customer_pic);
                    }
                });
                obj_customer.setCus_pic(file_pics == null || file_pics.length == 0 ? null : file_pics[0].getName());
                if(obj_customer.getCus_pic() != null) {
                    obj_customer.setCus_pic(web.concat(obj_customer.getCus_pic()));
                }
                
                
            }
            return obj_customer;
        }
        return null;
    }

    @Override
    public List<Customer> findCustomerByHouseId(Long house_id) {
//        HouseCustomerManager houseCustomerManager = (HouseCustomerManager)SpringHelper.getBean("houseCustomerManager");
//        List<?> lst_house_customer = houseCustomerManager.getList(FilterFactory.getSimpleFilter("house_id",house_id));
//        if(lst_house_customer != null && lst_house_customer.size() > 0){
//            List<Long> lst_customer_id = new ArrayList<Long>();
//            for(Object object : lst_house_customer){
//                HouseCustomer houseCustomer = (HouseCustomer)object;
//                lst_customer_id.add(houseCustomer.getCustomer_id());
//            }
//            return (List<Customer>)this.getList(FilterFactory.getInFilter("id",lst_customer_id));
//        }
        
        CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
        List<Map<String, Object>> list = customerDao.selectUniqueHouseCustomer(house_id);
        if(list!=null&&list.size()>0){
        	 List<Long> lst_customer_id = new ArrayList<Long>();
             for(Map m : list){
                 Long customerId = m.get("customer_id")==null?0:Long.parseLong(m.get("customer_id").toString());
                 
                 lst_customer_id.add(customerId);
             }
             return (List<Customer>)this.getList(FilterFactory.getInFilter("id",lst_customer_id));
        }
        
        return null;
    }

    @Override
    public Customer saveCustomerAndAddress(Customer customer,CustomerData customerData) {
        ViewAddressCustomerManager viewAddressCustomerManager = (ViewAddressCustomerManager)SpringHelper.getBean("viewAddressCustomerManager");
        CustomerDataManager customerDataManager = (CustomerDataManager)SpringHelper.getBean("customerDataManager");
        MessageNewManager messageManager = (MessageNewManager)SpringHelper.getBean("messageNewManager");
        if(customer.getCustomer_address()!= null){
            ViewAddressCustomer addressCustomer = viewAddressCustomerManager.saveOrUpdateAddressInfo(customer.getCustomer_address());
            addressCustomer.setHouse_id(addressCustomer.getHouse_id());
        }
       
        Customer cus = this.saveOrUpdateCustomerAndHouse(customer);
       
        if(customerData!=null){
        	
            customerData.setCustomer_id(cus.getId());
            customerData.setCreate_user_id(customer.getUpdate_user_id());
            customerData.setCreate_user(customer.getUpdate_user());
            customerDataManager.saveCustomerData(customerData);
        }
        return cus;
    }
    
    
    /**
     * 保存拜访记录 
     */
    @Override
    public Customer saveCustomerAndRelation(Customer customer) {
        return this.saveOrUpdateCustomerRelations(customer);
    }
    
    //保存修改拜访记录 
    @Override
    public Customer saveOrUpdateCustomerRelations(Customer customer) {
        RelationManager relationManager = (RelationManager) SpringHelper.getBean("relationManager");
        MessageNewManager messageManager = (MessageNewManager)SpringHelper.getBean("messageNewManager");

        Date sdate = new Date();
        Customer save_customer = null;
        if(ValueUtil.checkValue(customer.getId())){
            save_customer = (Customer)this.getObject(customer.getId());
        }
        if(save_customer == null){
        	save_customer = saveOrUpdateCustomerAndHouse(customer);
        }
        
        //取得所有的记录
        Set<Relation> relationSet = customer.getRelations();
        if(relationSet != null){
            for (Relation relation : relationSet) {
                if(relation.getId() != null){
                    continue;
                }
                Set<RelationContent> relationContentSet = relation.getChilds();
                relation.setChilds(null);
                relation.setCustomer_id(save_customer.getId());
                relation.setCreate_user(relation.getCreate_user());
                relation.setCreate_user_id(relation.getCreate_user_id());
                relation.setCreate_time(sdate);
                relation.setUpdate_user(relation.getUpdate_user());
                relation.setUpdate_user_id(relation.getUpdate_user_id());
                relation.setUpdate_time(sdate);
                relationManager.saveObject(relation);
                if(relation.getChilds() == null){
                    relation.setChilds(new HashSet<RelationContent>());
                }
                if(relationContentSet != null){
                    for(RelationContent content : relationContentSet){
                        content.setRelation_id(relation.getId());
                        relation.getChilds().add(content);
                    }
                }

                relationManager.saveObject(relation);
                messageManager.sendMessage_area_news(3,relation.getId().toString());//发送动态消息
                if(save_customer.getRelations() == null){
                    save_customer.setRelations(new TreeSet<Relation>());
                }
                save_customer.getRelations().add(relation); 
            }
        }
        return save_customer;
    }
    
    

    @Override
    public Map<String, Object> queryAchievements(QueryConditions queryConditions) {

        CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());

        String employee_no = null;
        String start_date = null;
        String end_date = null;
        PageInfo pageInfo = queryConditions.getPageinfo();

        for(Map<String,Object> map_condition : queryConditions.getConditions()){
            if("start_date".equals(map_condition.get("key")) && null != map_condition.get("value")){
                start_date = map_condition.get("value").toString();
            }

            if("end_date".equals(map_condition.get("key")) && null != map_condition.get("value")){
                end_date = map_condition.get("value").toString();
            }

            if("employee_no".equals(map_condition.get("key")) && null != map_condition.get("value")){
                end_date = map_condition.get("value").toString();
            }
        }
        return customerDao.queryAchievements(employee_no,start_date,end_date,pageInfo);
    }


    private String getCellValue(Cell cell) {
                String cellValue;
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        cellValue = cell.getRichStringCellValue().getString().trim();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        cellValue = cell.getNumericCellValue() == 0D ? null : String.valueOf(cell.getNumericCellValue());
                        if(cellValue != null && cellValue.contains("E")){
                            cellValue = new BigDecimal(cellValue).toPlainString();
                        }
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
                break;
            case Cell.CELL_TYPE_FORMULA:
                cellValue = cell.getCellFormula();
                break;
            default:
                cellValue = null;
        }
        return cellValue;
    }


	@Override
	public Customer findCustomerById(Long id) {
		List<?> lst_result = this.getList(FilterFactory.getSimpleFilter("id",id));
        if(lst_result != null && lst_result.size() > 0){
            return (Customer)lst_result.get(0);
        }
        return null;
	}
	
	
    @Override
    public Customer findCustomerByCustomerIdAndHouseId_crm(Long customer_id, Long house_id,String phone) {
        ViewAddressCustomerManager viewAddressCustomerManager = (ViewAddressCustomerManager)SpringHelper.getBean("viewAddressCustomerManager");

        Object object = this.getObject(customer_id);
        String web = PropertiesUtil.getValue("file.web.root").concat("user_image").concat(File.separator);

        String picPath = PropertiesUtil.getValue("file.root").concat("user_image").concat(File.separator);
        
        String web_house = PropertiesUtil.getValue("file.web.root").concat("house_type_image").concat(File.separator);

        String picPath_house = PropertiesUtil.getValue("file.root").concat("house_type_image").concat(File.separator);
        if(object != null){
            Customer obj_customer =  (Customer) object;
            obj_customer.setRelations(obj_customer.getRelations());
            FSP fsp = new FSP();
            if(house_id != null){
                fsp.setSort(SortFactory.createSort("house_id", ISort.DESC));
                fsp.setUserFilter(FilterFactory.getSimpleFilter("house_id",house_id));
                List<?> lst_address = viewAddressCustomerManager.getList(fsp);
                if(lst_address != null && lst_address.size() > 0){
                    obj_customer.setCustomer_address((ViewAddressCustomer)lst_address.get(0));
                    if(obj_customer.getCustomer_address().getHouse_type()!=null){
                    	if(obj_customer.getCustomer_address().getHouse_type() == 1){
                    		
                    		String building_name = obj_customer.getCustomer_address().getBuilding_name()==null?"":obj_customer.getCustomer_address().getBuilding_name();
                            String unit_no = obj_customer.getCustomer_address().getUnit_no()==null?"":obj_customer.getCustomer_address().getUnit_no();
                        	String house_no = obj_customer.getCustomer_address().getHouse_no()==null?"":obj_customer.getCustomer_address().getHouse_no();
                    		
                        	obj_customer.setAddress(obj_customer.getCustomer_address().getTv_name()
                                .concat(building_name.concat("号楼"))
                                .concat(unit_no.concat("单元"))
                                .concat(house_no.concat("号")));
	                    }else if(obj_customer.getCustomer_address().getHouse_type() == 0){
                        	String house_no = obj_customer.getCustomer_address().getHouse_no()==null?"":obj_customer.getCustomer_address().getHouse_no();

	                    	obj_customer.setAddress(obj_customer.getCustomer_address().getTv_name()
	                                .concat(house_no.concat("号")));
	                    }
                    }
                }
                if(obj_customer.getCustomer_address()!=null&&!"".equals(obj_customer.getCustomer_address())){
                	  if(obj_customer.getCustomer_address().getHouse_pic() != null && !"".equals(obj_customer.getCustomer_address().getHouse_pic())){
                          File pic_dir = new File(picPath_house);
                          final String house_pic = obj_customer.getCustomer_address().getHouse_pic();
                          File[] file_pics = pic_dir.listFiles(new FileFilter() {
                              @Override
                              public boolean accept(File pathname) {
                                  return pathname.getName().contains(house_pic);
                              }
                          });
                          obj_customer.getCustomer_address().setHouse_pic(file_pics == null || file_pics.length == 0 ? null : file_pics[0].getName());
                          if( obj_customer.getCustomer_address().getHouse_pic() != null) {
                          	obj_customer.getCustomer_address().setHouse_pic(web_house.concat(obj_customer.getCustomer_address().getHouse_pic()));
                          }
                      }
                }
              
            }

            if(obj_customer.getCustomer_pic() != null && !"".equals(obj_customer.getCustomer_pic())){
                File pic_dir = new File(picPath);
                final String customer_pic = obj_customer.getCustomer_pic();
                File[] file_pics = pic_dir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().contains(customer_pic);
                    }
                });
                obj_customer.setCus_pic(file_pics == null || file_pics.length == 0 ? null : file_pics[0].getName());
                if(obj_customer.getCus_pic() != null) {
                    obj_customer.setCus_pic(web.concat(obj_customer.getCus_pic()));
                }
                
                
            }
            
           
            return obj_customer;
        }
        return null;
    }



	
	@Override
	public Customer findCustomerByCustomerPhone_crm(Long customer_id, Long house_id, String phone) {
		
		ViewAddressCustomerManager viewAddressCustomerManager = (ViewAddressCustomerManager)SpringHelper.getBean("viewAddressCustomerManager");

        
        List<?> lst_result = this.getList(FilterFactory.getSimpleFilter("mobilephone",phone));
       
        String web = PropertiesUtil.getValue("file.web.root").concat("user_image").concat(File.separator);

        String picPath = PropertiesUtil.getValue("file.root").concat("user_image").concat(File.separator);
        
        String web_house = PropertiesUtil.getValue("file.web.root").concat("house_type_image").concat(File.separator);

        String picPath_house = PropertiesUtil.getValue("file.root").concat("house_type_image").concat(File.separator);
        if(lst_result != null && lst_result.size() > 0){
            Customer obj_customer =  (Customer) lst_result.get(0);
            obj_customer.setRelations(obj_customer.getRelations());
            FSP fsp = new FSP();
            if(house_id != null){
                fsp.setSort(SortFactory.createSort("house_id", ISort.DESC));
                fsp.setUserFilter(FilterFactory.getSimpleFilter("house_id",house_id));
                List<?> lst_address = viewAddressCustomerManager.getList(fsp);
                if(lst_address != null && lst_address.size() > 0){
                    obj_customer.setCustomer_address((ViewAddressCustomer)lst_address.get(0));
                    if(obj_customer.getCustomer_address().getHouse_type()!=null){
                    	if(obj_customer.getCustomer_address().getHouse_type() == 1){
                    		String buildingName = obj_customer.getCustomer_address().getBuilding_name()==null?"":obj_customer.getCustomer_address().getBuilding_name();
                            String unitNo = obj_customer.getCustomer_address().getUnit_no()==null?"":obj_customer.getCustomer_address().getUnit_no();
                    		String houseNo = obj_customer.getCustomer_address().getHouse_no()==null?"":obj_customer.getCustomer_address().getHouse_no();
                            obj_customer.setAddress(obj_customer.getCustomer_address().getTv_name()
                                    .concat(buildingName.concat("号楼"))
                                    .concat(unitNo.concat("单元"))
                                    .concat(houseNo.concat("号")));
                        }else if(obj_customer.getCustomer_address().getHouse_type() == 0){
                        	String houseNo = obj_customer.getCustomer_address().getHouse_no()==null?"":obj_customer.getCustomer_address().getHouse_no();
                            obj_customer.setAddress(obj_customer.getCustomer_address().getTv_name()
                                    .concat(houseNo.concat("号")));
                        }
                    }
                    
                }
                
                if(obj_customer.getCustomer_address().getHouse_pic() != null && !"".equals(obj_customer.getCustomer_address().getHouse_pic())){
                    File pic_dir = new File(picPath_house);
                    final String house_pic = obj_customer.getCustomer_address().getHouse_pic();
                    File[] file_pics = pic_dir.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.getName().contains(house_pic);
                        }
                    });
                    obj_customer.getCustomer_address().setHouse_pic(file_pics == null || file_pics.length == 0 ? null : file_pics[0].getName());
                    if( obj_customer.getCustomer_address().getHouse_pic() != null) {
                    	obj_customer.getCustomer_address().setHouse_pic(web_house.concat(obj_customer.getCustomer_address().getHouse_pic()));
                    }
                    
                    
                }
            }

            if(obj_customer.getCustomer_pic() != null && !"".equals(obj_customer.getCustomer_pic())){
                File pic_dir = new File(picPath);
                final String customer_pic = obj_customer.getCustomer_pic();
                File[] file_pics = pic_dir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().contains(customer_pic);
                    }
                });
                obj_customer.setCus_pic(file_pics == null || file_pics.length == 0 ? null : file_pics[0].getName());
                if(obj_customer.getCus_pic() != null) {
                    obj_customer.setCus_pic(web.concat(obj_customer.getCus_pic()));
                }
                
                
            }
            
           
            return obj_customer;
        }
        return null;
	}



	
	@Override
	public Map<String, Object> getCustomerCount(String employeeNo) {
		Map<String, Object> result = new HashMap<String,Object>();
		List<?> lst_result = this.getList(FilterFactory.getSimpleFilter("employee_no",employeeNo));
		if(lst_result!=null){
			result.put("total", lst_result.size());
		}else{
			result.put("total", 0);
		}
		return result;
	}



	
	@Override
	public Map<String, Object> getHouseCountAndServiceHouse(String employee_no,Long area_id) {
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		Long houseCount =  customerDao.getHouseOfArea(employee_no,area_id);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("housetotal", houseCount);
		return result;
	}
	
	
	public Map<String, Object> checkCustomerByPhone(String phone){
		Map<String,Object> result = new HashMap<String,Object>();
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		List<Map<String, Object>> list = customerDao.getCustomerByPhone(phone);
		result.put("count", list.size());
		return result;
	}



	
	@Override
	public Map<String, Object> queryRelationAndCustomerOfYear(Long storeId) {
		Map<String,Object> result = new HashMap<String,Object>();
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		RelationDao relationDao = (RelationDao)SpringHelper.getBean(RelationDao.class.getName());
		List<Map<String, Object>> customerList = customerDao.getCustomerOfYear(storeId);
		List<Map<String, Object>> relationList = relationDao.getRelationOfYear(storeId);
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		List<Map<String, Object>> c_List = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> r_List = new ArrayList<Map<String,Object>>();
		for(int i=1;i<=month;i++){
			String monthStr = i+"月";
			Map<String, Object> relationMap = new HashMap<String,Object>();
			relationMap.put("total", 0);
			relationMap.put("date", monthStr);
			r_List.add(relationMap);
			for(Map rMap:relationList){
				if(monthStr.equals(rMap.get("relation_date"))){
					relationMap.put("total", rMap.get("total"));
					break;
				}
				
			}
		}
		
		for(int i=1;i<=month;i++){
			String monthStr = i+"月";
			Map<String, Object> customerMap = new HashMap<String,Object>();
			customerMap.put("date", monthStr);
			customerMap.put("total", 0);
			c_List.add(customerMap);
			for(Map cMap:customerList){
				if(monthStr.equals(cMap.get("create_date"))){
					customerMap.put("total", cMap.get("total"));
					break;
				}
				
			}
		}
		result.put("relation", r_List);
		result.put("customer", c_List);
		return result;
	}



	
	@Override
	public Map<String, Object> queryRelationAndCustomerOfArea(Long storeId,String query_date) {
		Map<String,Object> result = new HashMap<String,Object>();
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		RelationDao relationDao = (RelationDao)SpringHelper.getBean(RelationDao.class.getName());
		List<Map<String, Object>> customerList = customerDao.getCustomerOfArea(storeId,query_date);
		List<Map<String, Object>> relationList = relationDao.getRelationOfArea(storeId,query_date);
		result.put("relation", relationList);
		result.put("customer", customerList);
		return result;
	}



	
	@Override
	public Map<String, Object> queryCustomerOfEmployee(Long storeId,String query_date) {
		Map<String,Object> result = new HashMap<String,Object>();
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		List<Map<String, Object>> customerList = customerDao.getCustomerOfEmployee(storeId,query_date);
		result.put("customer", customerList);
		return result;
	}



	@Override
	public void updateCustomerSortById(String ids) {
		CustomerDao customerDao=(CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		customerDao.updateCustomerSortById(ids);
	}



	
	@Override
	public Map<String, Object> queryRelationAndCustomerOfYear_CSZJ_QYJL(String employeeId,Long cityId,String role) {
		Map<String,Object> result = new HashMap<String,Object>();
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		RelationDao relationDao = (RelationDao)SpringHelper.getBean(RelationDao.class.getName());
		List<Map<String, Object>> customerList = customerDao.getCustomerOfYear_CSZJ_QYJL(employeeId,cityId,role);
		List<Map<String, Object>> relationList = relationDao.getRelationOfYear_CSZJ_QYJL(employeeId,cityId,role);
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		List<Map<String, Object>> c_List = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> r_List = new ArrayList<Map<String,Object>>();
		for(int i=1;i<=month;i++){
			String monthStr = i+"月";
			Map<String, Object> relationMap = new HashMap<String,Object>();
			relationMap.put("total", 0);
			relationMap.put("date", monthStr);
			r_List.add(relationMap);
			for(Map rMap:relationList){
				if(monthStr.equals(rMap.get("relation_date"))){
					relationMap.put("total", rMap.get("total"));
					break;
				}
				
			}
		}
		
		for(int i=1;i<=month;i++){
			String monthStr = i+"月";
			Map<String, Object> customerMap = new HashMap<String,Object>();
			customerMap.put("date", monthStr);
			customerMap.put("total", 0);
			c_List.add(customerMap);
			for(Map cMap:customerList){
				if(monthStr.equals(cMap.get("create_date"))){
					customerMap.put("total", cMap.get("total"));
					break;
				}
				
			}
		}
		result.put("relation", r_List);
		result.put("customer", c_List);
		return result;
	}



	
	@Override
	public Map<String, Object> queryRelationAndCustomerOfStore_CSZJ_QYJL(String employeeId,Long city_id,String role,String q_date) {
		Map<String,Object> result = new HashMap<String,Object>();
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		RelationDao relationDao = (RelationDao)SpringHelper.getBean(RelationDao.class.getName());
		List<Map<String, Object>> customerList = customerDao.getCustomerOfStore_CSZJ_QYJL(employeeId,city_id,role);
		List<Map<String, Object>> relationList = relationDao.getRelationOfStore_CSZJ_QYJL(employeeId,city_id,role);
		result.put("relation", relationList);
		result.put("customer", customerList);
		return result;
	}



	
	@Override
	public Map<String, Object> queryRelationAndCustomerOfEmployee_CSZJ_QYJL(String employeeId,Long city_id,String query_date,String role) {
		Map<String,Object> result = new HashMap<String,Object>();
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		List<Map<String, Object>> customerList = customerDao.getCustomerOfEmployee_CSZJ_QYJL(employeeId,city_id,role);
		result.put("customer", customerList);
		return result;
	}



	
	@Override
	public void saveBeforeDateCustomer_timedTask() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH,-1);
		String prev_month = simpleDateFormat.format(calendar.getTime());
		Map<String,Object> result = new HashMap<String,Object>();
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		List<Map<String, Object>> customerList = customerDao.getCustomerOfPrevMonthOfStore(prev_month);
		simpleDateFormat = new SimpleDateFormat("yyyy-M");
		for(int i=0;i<customerList.size();i++){
			BeforeDateCustomer bDateCustomer = new BeforeDateCustomer();
			Object amount = customerList.get(i).get("amount");
			Object store_id = customerList.get(i).get("store_id");
			Object store_name = customerList.get(i).get("store_name");
			bDateCustomer.setAmount(amount==null?0:Integer.parseInt(amount.toString()));
			bDateCustomer.setBind_date(simpleDateFormat.format(calendar.getTime()));
			bDateCustomer.setStore_id(Long.parseLong(store_id.toString()));
			bDateCustomer.setStore_name(store_name.toString());
			bDateCustomer.setCreate_time(date);
			saveObject(bDateCustomer);
		}
		
		
	}



	
	@Override
	public Map<String, Object> queryRelationAndCustomerOfYear_CSZJ_QYJL_before(String employeeId, Long city_id,
			String role) {
		
		Map<String,Object> result = new HashMap<String,Object>();
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		RelationDao relationDao = (RelationDao)SpringHelper.getBean(RelationDao.class.getName());
		List<Map<String, Object>> customerList = customerDao.getCustomerOfYear_CSZJ_QYJL_before(employeeId,city_id,role);
		List<Map<String, Object>> relationList = relationDao.getRelationOfYear_CSZJ_QYJL_before(employeeId,city_id,role);
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		List<Map<String, Object>> c_List = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> r_List = new ArrayList<Map<String,Object>>();
		for(int i=1;i<=month;i++){
			String monthStr = i+"月";
			Map<String, Object> relationMap = new HashMap<String,Object>();
			relationMap.put("total", 0);
			relationMap.put("date", monthStr);
			r_List.add(relationMap);
			for(Map rMap:relationList){
				if(monthStr.equals(rMap.get("relation_date"))){
					relationMap.put("total", rMap.get("total"));
					break;
				}
				
			}
		}
		
		for(int i=1;i<=month;i++){
			String monthStr = i+"月";
			Map<String, Object> customerMap = new HashMap<String,Object>();
			customerMap.put("date", monthStr);
			customerMap.put("total", 0);
			c_List.add(customerMap);
			for(Map cMap:customerList){
				if(monthStr.equals(cMap.get("create_date"))){
					customerMap.put("total", cMap.get("total"));
					break;
				}
				
			}
		}
		result.put("relation", r_List);
		result.put("customer", c_List);
		return result;
	}



	
	@Override
	public Map<String, Object> queryRelationAndCustomerOfStore_CSZJ_QYJL_before(String employeeId, Long city_id,
			String role, String q_date) {
		Map<String,Object> result = new HashMap<String,Object>();
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		RelationDao relationDao = (RelationDao)SpringHelper.getBean(RelationDao.class.getName());
		List<Map<String, Object>> customerList = customerDao.getCustomerOfStore_CSZJ_QYJL_before(employeeId, city_id, role, q_date);
		List<Map<String, Object>> relationList = relationDao.getRelationOfStore_CSZJ_QYJL_before(employeeId, city_id, role, q_date);
		result.put("relation", relationList);
		result.put("customer", customerList);
		return result;
	}



	
	@Override
	public Map<String, Object> getStoreKepper(String employeeId) {
		UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		User employee = userManager.findEmployeeByEmployeeNo(employeeId);
        Map<String, Object> storekeeper = customerDao.queryStoreKeeper(employee.getStore_id());
        return storekeeper;
	}



	
	@Override
	public Map<String, Object> selectCustomerByTinyVillage(String idsStr) {
		
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        Map<String,Object> result = new HashMap<String,Object>();
		try {
        	list = customerDao.selectCustomerByTinyVillage(idsStr);
        	result.put("customer", list);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        return result;
	}



	
	@Override
	public Map<String, Object> editCustomerAddress(ViewAddressCustomer addressCustomer) {
		UserManager umanager = (UserManager)SpringHelper.getBean("userManager");
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		UserDTO userDTO = umanager.getCurrentUserDTO();
		addressCustomer.setUpdate_user_id(userDTO.getId());
		addressCustomer.setUpdate_user(userDTO.getEmployeeId());
		ViewAddressCustomerManager viewAddressCustomerManager = (ViewAddressCustomerManager)SpringHelper.getBean("viewAddressCustomerManager");
	    CustomerDataManager customerDataManager = (CustomerDataManager)SpringHelper.getBean("customerDataManager");
	    MessageNewManager messageManager = (MessageNewManager)SpringHelper.getBean("messageNewManager");
	    HouseCustomerManager houseCustomerManager = (HouseCustomerManager) SpringHelper.getBean("houseCustomerManager");
	    Map<String, Object> result = new HashMap<String,Object>();
 	    try {
	    	ViewAddressCustomer addressCustomer_back = viewAddressCustomerManager.saveOrUpdateAddressInfo(addressCustomer);
		    addressCustomer.setHouse_id(addressCustomer.getHouse_id());
		    HouseCustomer houseCustomer = new HouseCustomer();
	        houseCustomer.setHouse_id(addressCustomer_back.getHouse_id());
	        houseCustomer.setCustomer_id(addressCustomer.getCustomer_id());
	        preObject(houseCustomer);
	        houseCustomerManager.saveHouseCustomer(houseCustomer);
	        String address = "";
	        if(addressCustomer_back.getHouse_type()==0){//平房
	        	address = addressCustomer_back.getTv_name()+"街(道) " +addressCustomer_back.getHouse_no()+ "号";
	        }else if(addressCustomer_back.getHouse_type()==1){//楼
	        	address = addressCustomer_back.getTv_name()+"小区 "+ addressCustomer_back.getBuilding_name() + "号楼"+ addressCustomer_back.getUnit_no()+ "单元" + addressCustomer_back.getHouse_no() + "号";
	        }
	        
	        //修改地址
	        Customer customer  = new Customer();
	        customer.setAddress(address);
	        customer.setId(addressCustomer.getCustomer_id());
	        customerDao.updateCustomerAddress(customer);
	        
	        
	        result.put("code",CodeEnum.success.getValue());
			result.put("message","编辑新地址成功");
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",CodeEnum.error.getValue());
			result.put("message","编辑新地址失败");
			return result;
		}
        
		return result;
	}

	
}
