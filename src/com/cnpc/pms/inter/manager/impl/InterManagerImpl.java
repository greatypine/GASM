package com.cnpc.pms.inter.manager.impl;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.Spring;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.file.comm.utils.DateUtil;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.IPage;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.dynamic.dao.DynamicDao;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.dynamic.manager.UserOperationStatManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.inter.dao.InterDao;
import com.cnpc.pms.inter.manager.InterManager;
import com.cnpc.pms.messageModel.entity.Message;
import com.cnpc.pms.messageModel.manager.MessageNewManager;
import com.cnpc.pms.mongodb.manager.MongoDBManager;
import com.cnpc.pms.personal.dao.CustomerDao;
import com.cnpc.pms.personal.dao.ExpressDao;
import com.cnpc.pms.personal.dao.HouseCustomerDao;
import com.cnpc.pms.personal.dao.RelationDao;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.dao.TinyVillageDao;
import com.cnpc.pms.personal.dto.BannerInfoDto;
import com.cnpc.pms.personal.dto.SiteSelectionDto;
import com.cnpc.pms.personal.dto.StoreAddressDto;
import com.cnpc.pms.personal.dto.StoreReqAddressDto;
import com.cnpc.pms.personal.dto.StoreRequirementDto;
import com.cnpc.pms.personal.dto.StoreStandardDto;
import com.cnpc.pms.personal.entity.AppDownloadLog;
import com.cnpc.pms.personal.entity.AppVersion;
import com.cnpc.pms.personal.entity.CodeLogin;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.Humanresources;

import com.cnpc.pms.personal.entity.SendMessage;

import com.cnpc.pms.personal.entity.BannerInfo;
import com.cnpc.pms.personal.entity.SiteSelection;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreAddress;
import com.cnpc.pms.personal.entity.StoreOrderInfo;
import com.cnpc.pms.personal.entity.StoreRequirement;
import com.cnpc.pms.personal.entity.StoreStandard;
import com.cnpc.pms.personal.entity.UserLoginLog;
import com.cnpc.pms.personal.entity.WorkRecordTotal;
import com.cnpc.pms.personal.entity.WxUserAuth;
import com.cnpc.pms.personal.manager.AppDownLoadLogManager;
import com.cnpc.pms.personal.manager.AppVersionManager;
import com.cnpc.pms.personal.manager.BannerInfoManager;
import com.cnpc.pms.personal.manager.CodeLoginManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.ExpressManager;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
import com.cnpc.pms.personal.manager.SendMessageManager;
import com.cnpc.pms.personal.manager.SiteSelectionManager;
import com.cnpc.pms.personal.manager.StoreAddressManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.StoreOrderInfoManager;
import com.cnpc.pms.personal.manager.StoreRequirementManager;
import com.cnpc.pms.personal.manager.StoreStandardManager;
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.personal.manager.UserLoginLogManager;
import com.cnpc.pms.personal.manager.WorkRecordTotalManager;
import com.cnpc.pms.personal.manager.WxUserAuthManager;
import com.cnpc.pms.platform.dao.OrderDao;
import com.cnpc.pms.platform.entity.PlatformEmployee;
import com.cnpc.pms.platform.manager.OrderManager;
import com.cnpc.pms.platform.manager.PlatformEmployeeManager;
import com.cnpc.pms.slice.dao.AreaDao;
import com.cnpc.pms.slice.entity.Area;
import com.cnpc.pms.slice.manager.AreaManager;
import com.cnpc.pms.utils.BarCodeUtils;
import com.cnpc.pms.utils.MD5Utils;
import com.cnpc.pms.utils.ValidationCode;

import com.cnpc.pms.utils.PhoneFormatCheckUtils;

import com.cnpc.pms.utils.DateUtils;

import com.cnpc.pms.utils.ValueUtil;

/**
 * App接口实现类interDao
 * Created by liu on 2016/8/23 0023.
 */
public class InterManagerImpl extends BizBaseCommonManager implements InterManager {

    /**
     * 获取当前版本号
     * @return 当前版本号
     */
    @Override
    public String getCurrentVersion(String app_version) {
        InterDao interDao = (InterDao) SpringHelper.getBean(InterDao.class.getName());
        Map<String,List<String>> map_result = interDao.getCurrentVersion();
        List<String> lst_version = map_result.get("version");
        List<String> lst_filename = map_result.get("filename");
//        int n_next = Collections.binarySearch(lst_version,app_version) + 1;
        int n_next = lst_version.indexOf(app_version) + 1;
        if(n_next != 0 && n_next < lst_version.size()){
            return lst_filename.get(lst_filename.size() - 1);
        }
        return null;
    }
    
    @Override
    public Result getNowCurrentVersion(String app_version) {
    	 IFilter iFilter =FilterFactory.getSimpleFilter(" version='"+app_version+"'");
    	 AppVersionManager appVersionManager = (AppVersionManager) SpringHelper.getBean("appVersionManager");
	     List<?> lst_groupList = appVersionManager.getList(iFilter);
	     Result result = null;
    	 if(lst_groupList!=null&&lst_groupList.size()>0){
    		 result = new Result();
    		 AppVersion appVersion = (AppVersion) lst_groupList.get(0);
    		 result.setAppVersion(appVersion);
             result.setCode(CodeEnum.success.getValue());
             result.setMessage(CodeEnum.success.getDescription());
    		 return result;
    	 }
    	 return result;
    }
    
    @Override
    public Result getNewCurrentVersion(String app_version) {
        InterDao interDao = (InterDao) SpringHelper.getBean(InterDao.class.getName());
        Map<String,List<String>> map_result = interDao.getCurrentVersion();
        List<String> lst_version = map_result.get("version");
        List<String> lst_filename = map_result.get("filename");
        List<String> lst_message = map_result.get("message");
        List<String> lst_tiptype = map_result.get("tiptype");
        List<String> lst_isupdate = map_result.get("isupdate");
        Result result = null;
//        int n_next = Collections.binarySearch(lst_version,app_version) + 1;
        int n_next = lst_version.indexOf(app_version) + 1;
        if(n_next != 0 && n_next < lst_version.size()){
        	AppVersion appVersion = new AppVersion();
            String file_name =  lst_filename.get(lst_filename.size() - 1);
            String version = lst_version.get(lst_filename.size() - 1);
            String message = lst_message.get(lst_message.size() - 1);
            String tiptype = lst_tiptype.get(lst_tiptype.size() - 1);
            String isupdate = lst_isupdate.get(lst_isupdate.size() - 1);
            appVersion.setFile_name(file_name);
            appVersion.setVersion(version);
            appVersion.setMessage(message);
            appVersion.setTiptype(Long.parseLong(tiptype));
            appVersion.setIsupdate(Long.parseLong(isupdate));
            result = new Result();
            result.setAppVersion(appVersion);
            result.setCode(CodeEnum.success.getValue());
            result.setMessage(CodeEnum.success.getDescription());
            return result;
        }
        return result;
    }
    
    
    
    private int binarySearch(List<String> list,String value){
    	int result = -1;
    	for(int i = 0;i < list.size();i++){
    		if(value == null){
    			if(list.get(i) == null){
    				result = i;
    				break;
    			}
    		}else if(value.equals(list.get(i))){
    			result = i;
				break;
    		}
    	}
    	return result;
    }

    
    @Override
    public Map<String, Object> getExpressAndCustomerCount(String employee_no) {
        Map<String,Object> map = new HashMap<String, Object>();
        InterDao interDao = (InterDao) SpringHelper.getBean(InterDao.class.getName());
        PlatformEmployeeManager platformEmployeeManager = (PlatformEmployeeManager)SpringHelper.getBean("platformEmployeeManager");
        OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");
        PlatformEmployee platformEmployee = platformEmployeeManager.findPlatformEmployeeByEmployeeNo(employee_no);
        map.put("Express",interDao.getExpressCountByCurrentMonth(employee_no));
        map.put("Customer",interDao.getCustomerCountByCurrentMonth(employee_no,null));
        map.put("Relation",interDao.getRelationCountByCurrentMonth(employee_no));
        if(platformEmployee != null){//暂时注释掉
            map.put("Order",0);
        }else{
            map.put("Order",0);
        }
        return map;
    }
//查询门店的所有数据
    @Override
    public Map<String, Object> getExpressAndCustomerStoreCount(Long store_id) {
        Map<String,Object> map = new HashMap<String, Object>();
        InterDao interDao = (InterDao) SpringHelper.getBean(InterDao.class.getName());
        OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");
        StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
        map.put("Express",interDao.getExpressStoreCountByCurrentMonth(store_id));
        map.put("Customer",interDao.getCustomerStoreCountByCurrentMonth(store_id,null));
        map.put("Relation",interDao.getRelationStoreCountByCurrentMonth(store_id));
        Store store = storeManager.findStore(store_id);
        if(store != null && store.getPlatformid() != null){
            map.put("Order",orderManager.getOrderCount(store.getPlatformid(),null,null));
        }else{
            map.put("Order",0);
        }
        return map;
    }
    
    
    
    
    @Override
    public Map<String, Object> getExpressAndCustomerCountNew(Express express) {
        Map<String,Object> map = new HashMap<String, Object>();
        InterDao interDao = (InterDao) SpringHelper.getBean(InterDao.class.getName());
        PlatformEmployeeManager platformEmployeeManager = (PlatformEmployeeManager)SpringHelper.getBean("platformEmployeeManager");
        OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");
        PlatformEmployee platformEmployee = platformEmployeeManager.findPlatformEmployeeByEmployeeNo(express.getEmployee_no());
        map.put("Express",interDao.getExpressCountByCurrentMonth(express.getEmployee_no(),express.getUpdate_user()));
        map.put("Customer",interDao.getCustomerCountByCurrentMonth(express.getEmployee_no(),express.getUpdate_user(),express.getExpress_no(),express.getStore_id()));
        map.put("Relation",interDao.getRelationCountByCurrentMonth(express.getEmployee_no(),express.getUpdate_user()));
        /*if(platformEmployee != null){
            map.put("Order",orderManager.getOrderCount(null,platformEmployee.getId(),express.getUpdate_user()));
        }else{
            map.put("Order",0);
        }*/
        return map;
    }

    @Override
    public Map<String, Object> getExpressAndCustomerStoreCountNew(Express express) {
        Map<String,Object> map = new HashMap<String, Object>();
        OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");
        StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
        InterDao interDao = (InterDao) SpringHelper.getBean(InterDao.class.getName());
        map.put("Express",interDao.getExpressStoreCountByCurrentMonth(express.getStore_id(),express.getUpdate_user()));//快递
        map.put("Customer",interDao.getCustomerStoreCountByCurrentMonth(express.getStore_id(),express.getUpdate_user(),express.getExpress_no()));//用户画像
        map.put("Relation",interDao.getRelationStoreCountByCurrentMonth(express.getStore_id(),express.getUpdate_user()));
        map.put("XXExpress",interDao.getXXExpressStoreCountByCurrentMonth(express.getStore_id(),express.getUpdate_user()));
        map.put("SelfRelation",interDao.getSelfExpressStoreCountByCurrentMonth(express.getStore_id(),express.getUpdate_user()));
        Store store = storeManager.findStore(express.getStore_id());
        if(store != null && store.getPlatformid() != null){
            map.put("Order",orderManager.getOrderCount(store.getPlatformid(),null,express.getUpdate_user()));
        }else{
            map.put("Order",0);
        }
        return map;
    }

    @Override
    public Result getCustomerListForMonth(Customer customer) {
        CustomerManager customerManager = (CustomerManager)SpringHelper.getBean("customerManager");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
        customer.setCreate_time(calendar.getTime());
        return customerManager.findCustomerList(customer);
    }

    @Override
    public Result getExpressListForMonth(PageInfo pageInfo, Express express) {
        ExpressManager expressManager = (ExpressManager)SpringHelper.getBean("expressManager");
        return expressManager.queryExpressListByCurrentMonth(pageInfo,express);
    }

    @Override
    public Map<String, Object> queryDataCardList(QueryConditions conditions) {
        String employee_no = null;
        Long store_id = null;
        List<User> lst_user = null;
        StringBuilder str_employee_nos = new StringBuilder();
        IPage page = conditions.getPageinfo();

        UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
        InterDao interDao = (InterDao)SpringHelper.getBean(InterDao.class.getName());

        for(Map<String,Object> map_condition : conditions.getConditions()){
            if("store_id".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
                store_id = Long.valueOf(map_condition.get("value").toString());
            }

            if("employee_no".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
                employee_no = map_condition.get("value").toString();
            }
        }
        if(employee_no != null){
            lst_user = new ArrayList<User>();
            lst_user.add(userManager.findEmployeeByEmployeeNo(employee_no));
            page.setTotalRecords(1);
            str_employee_nos.append("'").append(employee_no).append("'");

        }else if(store_id != null){
            FSP fsp = new FSP();
            fsp.setUserFilter(FilterFactory.getSimpleFilter("store_id",store_id).appendAnd(FilterFactory.getSimpleFilter("usergroup.id","3224")));
            fsp.setPage(page);
            lst_user = (List<User>)userManager.getList(fsp);
            if(lst_user != null && lst_user.size() > 0){
                for(User obj_user : lst_user){
                    if(str_employee_nos.length() == 0){
                        str_employee_nos.append("'").append(obj_user.getEmployeeId()).append("'");
                    }else{
                        str_employee_nos.append(",'").append(obj_user.getEmployeeId()).append("'");
                    }
                }
            }
        }
        System.out.println(str_employee_nos.toString());
        List<Map<String,Object>> lst_data = new ArrayList<Map<String, Object>>();
        Map<String,Object> map_result = new HashMap<String, Object>();
        if(lst_user != null && lst_user.size() > 0){
            Map<String,Integer> map_customer = interDao.getCustomerStoreCountByCurrentMonth(str_employee_nos.toString());
            Map<String,Integer> map_express = interDao.getExpressStoreCountByCurrentMonth(str_employee_nos.toString());
            Map<String,Integer> map_relation = interDao.getRelationStoreCountByCurrentMonth(str_employee_nos.toString());
            for(User user : lst_user){
                Map<String,Object> map_row = new HashMap<String,Object>();
                map_row.put("employee_no",user.getEmployeeId());
                map_row.put("name",user.getName());
                map_row.put("customer_count",!map_customer.containsKey(user.getEmployeeId()) ? 0:map_customer.get(user.getEmployeeId()));
                map_row.put("customer_edit","查看");
                map_row.put("express_count",!map_express.containsKey(user.getEmployeeId()) ? 0:map_express.get(user.getEmployeeId()));
                map_row.put("express_edit","查看");
                map_row.put("relation_count",!map_relation.containsKey(user.getEmployeeId()) ? 0:map_relation.get(user.getEmployeeId()));
                map_row.put("relation_edit","查看");
                map_row.put("order","暂无");
                lst_data.add(map_row);
            }
        }
        map_result.put("pageinfo", page);
        map_result.put("header", "");
        map_result.put("data", lst_data);
        return map_result;
    }

    /**
     * 拜访记录列表 
     */
	@Override
	public Map<String, Object> queryRelationDataCardList(QueryConditions conditions) {
		String employee_no = null;
        Long store_id = null;
        String date_valler=null;
        StringBuilder str_employee_nos = new StringBuilder();
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
        InterDao interDao = (InterDao)SpringHelper.getBean(InterDao.class.getName());
        for(Map<String,Object> map_condition : conditions.getConditions()){
            if("store_id".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
                store_id = Long.valueOf(map_condition.get("value").toString());
            }
            if("employee_no".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
                employee_no = map_condition.get("value").toString();
            }
            if("date_valler".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
            	date_valler = map_condition.get("value").toString();
            }
        }
        if(employee_no != null){
        	String ver=" and u.employeeId='"+employee_no+"' ";
        	str_employee_nos.append(ver);
        }else if(store_id != null){
        	String ver=" and u.store_id ="+store_id +" ";
        	str_employee_nos.append(ver);
        }
       
        Map<String,Object> map_result = new HashMap<String, Object>();
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "");
        map_result.put("data", interDao.getRelationDataByStoreAndMouth_tu(str_employee_nos.toString(),obj_page,date_valler,store_id));
        return map_result;
	}

	@Override
	public Map<String, Object> queryCustomerDataCardList(QueryConditions conditions) {
		String employee_no = null;
        Long store_id = null;
        String date_valler=null;
        String  grade = null;
        StringBuilder str_employee_nos = new StringBuilder();
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
        InterDao interDao = (InterDao)SpringHelper.getBean(InterDao.class.getName());
        for(Map<String,Object> map_condition : conditions.getConditions()){
            if("store_id".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
                store_id = Long.valueOf(map_condition.get("value").toString());
            }
            if("employee_no".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
                employee_no = map_condition.get("value").toString();
            }
            if("date_valler".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
            	date_valler = map_condition.get("value").toString();
            }
            
            if("grade".equals(map_condition.get("key"))&&ValueUtil.checkValue(map_condition.get("value"))){
            	grade = map_condition.get("value").toString();
            }
        }
        if(employee_no != null){
        	String ver=" and u.employeeId='"+employee_no+"' ";
        	str_employee_nos.append(ver);
        }else if(store_id != null){
        	String ver=" and u.store_id ="+store_id +" ";
        	str_employee_nos.append(ver);
        }
       
        Map<String,Object> map_result = new HashMap<String, Object>();
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "");
        map_result.put("data", interDao.getCustomerDataByStoreAndMouth_tu(str_employee_nos.toString(),obj_page,date_valler,grade,store_id));
        return map_result;
	}

	@Override
	public Map<String, Object> queryExpressDataCardList(QueryConditions conditions) {
		String employee_no = null;
        Long store_id = null;
        String date_valler=null;
        StringBuilder str_employee_nos = new StringBuilder();
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
        InterDao interDao = (InterDao)SpringHelper.getBean(InterDao.class.getName());
        for(Map<String,Object> map_condition : conditions.getConditions()){
            if("store_id".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
                store_id = Long.valueOf(map_condition.get("value").toString());
            }
            if("employee_no".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
                employee_no = map_condition.get("value").toString();
            }
            if("date_valler".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
            	date_valler = map_condition.get("value").toString();
            }
        }
        if(employee_no != null){
        	String ver=" and u.employeeId='"+employee_no+"' ";
        	str_employee_nos.append(ver);
        }else if(store_id != null){
        	String ver=" and u.store_id ="+store_id +" ";
        	str_employee_nos.append(ver);
        }
       
        Map<String,Object> map_result = new HashMap<String, Object>();
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "");
        map_result.put("data", interDao.getExpressDataByStoreAndMouth(str_employee_nos.toString(),obj_page,date_valler,store_id));
        return map_result;
	}

    @Override
    public Map<String, Object> queryOrderDataCardList(QueryConditions conditions) {

        OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");

        StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
        Long store_id = null;
        String store_code = null;
        String date_valler=null;
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();

        Store store = null;

        for(Map<String,Object> map_condition : conditions.getConditions()){
            if("store_id".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
                store_id = Long.valueOf(map_condition.get("value").toString());
                store = storeManager.findStore(store_id);
            }

            if("date_valler".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
                date_valler = map_condition.get("value").toString();
            }
        }
        if(store == null || store.getPlatformid() == null){
            Map<String,Object> map_result = new HashMap<String, Object>();
            map_result.put("pageinfo", obj_page);
            map_result.put("header", "");
            map_result.put("data", new ArrayList<Map<String,Object>>());
            return map_result;
        }
        store_code = store.getPlatformid();
        return orderManager.queryOrderEmployeeCountByStore(obj_page,store_code,date_valler);
    }

    
    /**
     *点击用户卡中的 拜访记录 查询
     */
    @Override
	public List<Map<String, Object>> queryRelationListDataByStore(Express express) {
		List<User> lst_user = null;
        StringBuilder str_employee_nos = new StringBuilder();

        UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
        InterDao interDao = (InterDao)SpringHelper.getBean(InterDao.class.getName());
            FSP fsp = new FSP();
            fsp.setUserFilter(FilterFactory.getSimpleFilter("store_id",express.getStore_id()).appendAnd(FilterFactory.getSimpleFilter("usergroup.id","3224")).appendAnd(FilterFactory.getSimpleFilter("disabledFlag","1")));
            lst_user = (List<User>)userManager.getList(fsp);
            
            //判断门店的副店长 之前是什么角色
            HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
            List<Humanresources> hList = humanresourcesManager.queryHumanresourceListByStoreId(express.getStore_id());
            if(hList!=null&&hList.size()>0){
            	for(Humanresources h:hList){
            		if(h.getZw()!=null&&h.getZw().equals("副店长")&&h.getRemark()!=null&&h.getRemark().equals("国安侠")){
            			User user = userManager.findEmployeeByEmployeeNo(h.getEmployee_no());
            			lst_user.add(user);
            			break;
            		}
            	}
            }
            
            
            if(lst_user != null && lst_user.size() > 0){
                for(User obj_user : lst_user){
                    if(str_employee_nos.length() == 0){
                        str_employee_nos.append("'").append(obj_user.getEmployeeId()).append("'");
                    }else{
                        str_employee_nos.append(",'").append(obj_user.getEmployeeId()).append("'");
                    }
                }
            }
        System.out.println(str_employee_nos.toString());
        List<Map<String,Object>> lst_data = new ArrayList<Map<String, Object>>();
        Map<String,Object> map_result = new HashMap<String, Object>();
        if(lst_user != null && lst_user.size() > 0){
        	List<?> listRelat = interDao.getRelationDataByStoreAndMouth(str_employee_nos.toString(),express.getUpdate_user());
        	Map<String,Object> map_row =null;
            for(User user : lst_user){
                 map_row = new HashMap<String,Object>();
                 if(listRelat!=null&&listRelat.size()>0){
                	 for (Object obj : listRelat) {
                     	Object[] str=(Object[])obj;
     					if(str[0].equals(user.getEmployeeId())){
     						map_row.put("value",str[4]);
     						break;
     					}
     				}
                 }
                if(map_row.isEmpty()){
					map_row.put("value",0);
                }
                //map_row.put("employee_no",user.getEmployeeId());
                map_row.put("name",user.getName());
                map_row.put("id",user.getEmployeeId());
                lst_data.add(map_row);
            }
        }
        return lst_data;
	}

	@Override
	public List<Map<String, Object>> queryCustomerListDataByStore(Express express) {

        UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
        InterDao interDao = (InterDao)SpringHelper.getBean(InterDao.class.getName());

        String ver=" and u.store_id ="+express.getStore_id() +" ";

        List<Map<String,Object>> lst_data = new ArrayList<Map<String, Object>>();
        List<?> listCusto = interDao.getCustomerDataByStoreAndMouth(ver,express.getUpdate_user(),express.getExpress_no(),express.getStore_id());
        if(listCusto != null && listCusto.size() > 0){
            for(Object obj : listCusto){
                Map<String,Object> map_row = (Map<String,Object>)obj;
               // map_row.put("employee_no",user.getEmployeeId());
                map_row.put("value",map_row.get("data_amount"));
                map_row.put("name",map_row.get("name"));
                map_row.put("id",map_row.get("employeeId"));
                lst_data.add(map_row);
            }
        }
        return lst_data;
	}

	@Override
	public List<Map<String, Object>> queryExpressListDataByStore(Express express){
        List<User> lst_user = null;
        StringBuilder str_employee_nos = new StringBuilder();

        UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
        InterDao interDao = (InterDao)SpringHelper.getBean(InterDao.class.getName());
            FSP fsp = new FSP();
            fsp.setUserFilter(FilterFactory.getSimpleFilter("store_id",express.getStore_id()).appendAnd(FilterFactory.getSimpleFilter("usergroup.id","3224")).appendAnd(FilterFactory.getSimpleFilter("disabledFlag","1")));
            lst_user = (List<User>)userManager.getList(fsp);
            
            //判断门店的副店长 之前是什么角色
            HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
            List<Humanresources> hList = humanresourcesManager.queryHumanresourceListByStoreId(express.getStore_id());
            if(hList!=null&&hList.size()>0){
            	for(Humanresources h:hList){
            		if(h.getZw()!=null&&h.getZw().equals("副店长")&&h.getRemark()!=null&&h.getRemark().equals("国安侠")){
            			User user = userManager.findEmployeeByEmployeeNo(h.getEmployee_no());
            			lst_user.add(user);
            			break;
            		}
            	}
            }
            
            
            
            
            if(lst_user != null && lst_user.size() > 0){
                for(User obj_user : lst_user){
                    if(str_employee_nos.length() == 0){
                        str_employee_nos.append("'").append(obj_user.getEmployeeId()).append("'");
                    }else{
                        str_employee_nos.append(",'").append(obj_user.getEmployeeId()).append("'");
                    }
                }
            }
        System.out.println(str_employee_nos.toString());
        List<Map<String,Object>> lst_data = new ArrayList<Map<String, Object>>();
        Map<String,Object> map_result = new HashMap<String, Object>();
        if(lst_user != null && lst_user.size() > 0){
        	List<?> listExpre = interDao.getExpressDataByStoreAndMouth_tu(str_employee_nos.toString(),express.getUpdate_user());
        	Map<String,Object> map_row =null;
            for(User user : lst_user){
                 map_row = new HashMap<String,Object>();
                 if(listExpre!=null&&listExpre.size()>0){
                	 for (Object obj : listExpre) {
                     	Object[] str=(Object[])obj;
     					if(str[0].equals(user.getEmployeeId())){
     						map_row.put("value",str[3]);
     						break;
     					}
     				}
                 }
                if(map_row.isEmpty()){
					map_row.put("value",0);
                }
               // map_row.put("employee_no",user.getEmployeeId());
                map_row.put("name",user.getName());
                map_row.put("id",user.getEmployeeId());
                lst_data.add(map_row);
            }
        }
        return lst_data;
	}

    @Override
    public List<Map<String, Object>> queryOrderListDataByStore(Express express) {


        OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");

        StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
        //分页对象

        Store store = storeManager.findStore(express.getStore_id());


        if(store == null || store.getPlatformid() == null){
            return new ArrayList<Map<String,Object>>();
        }
        return orderManager.getOrderEmployeeData(store.getPlatformid(),express.getUpdate_user());
    }

    @Override
	public List<Map<String, Object>> queryRelationListDataByEmployee(Express express) {
	        List<User> lst_user = null;
	        StringBuilder str_employee_nos = new StringBuilder();
	        UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
	        InterDao interDao = (InterDao)SpringHelper.getBean(InterDao.class.getName());

	        if(express.getEmployee_no() != null){
	            lst_user = new ArrayList<User>();
	            lst_user.add(userManager.findEmployeeByEmployeeNo(express.getEmployee_no()));
	            str_employee_nos.append("'").append(express.getEmployee_no()).append("'");

	        }
	        System.out.println(str_employee_nos.toString());
	        List<Map<String,Object>> lst_data = new ArrayList<Map<String, Object>>();
	        Map<String,Object> map_result = new HashMap<String, Object>();
	        if(lst_user != null && lst_user.size() > 0){
	        	List<?> listRelat = interDao.getRelationDataByStoreAndMouth(str_employee_nos.toString(),null);
	        	Map<String,Object> map_row =null;
	            for(User user : lst_user){
	                 map_row = new HashMap<String,Object>();
	                 if(listRelat!=null&&listRelat.size()>0){
	                	 for (Object obj : listRelat) {
	 	                	Object[] str=(Object[])obj;
	 						if(str[0].equals(user.getEmployeeId())){
	 							map_row.put("value",str[4]);
	 							break;
	 						}
	 					}
	                 }
	                if(map_row.isEmpty()){
						map_row.put("value",0);
	                }
	                map_row.put("name",user.getName());
	                map_row.put("id",user.getEmployeeId());
	                lst_data.add(map_row);
	            }
	        }
	        return lst_data;
	}

	@Override
	public List<Map<String, Object>> queryCustomerListDataByEmployee(Express express) {
        UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
        InterDao interDao = (InterDao)SpringHelper.getBean(InterDao.class.getName());

        String ver=" and u.employeeId ='"+express.getEmployee_no() +"'";

        List<Map<String,Object>> lst_data = new ArrayList<Map<String, Object>>();
        List<?> listCusto = interDao.getCustomerDataByStoreAndMouth(ver,express.getUpdate_user(),express.getExpress_no(),express.getStore_id());
        if(listCusto != null && listCusto.size() > 0){
            for(Object obj : listCusto){
                Map<String,Object> map_row = (Map<String,Object>)obj;
                // map_row.put("employee_no",user.getEmployeeId());
                map_row.put("value",map_row.get("data_amount"));
                map_row.put("name",map_row.get("name"));
                map_row.put("id",map_row.get("employeeId"));
                lst_data.add(map_row);
            }
        }
        return lst_data;
	}

	@Override
	public List<Map<String, Object>> queryExpressListDataByEmployee(Express express) {
        List<User> lst_user = null;
        StringBuilder str_employee_nos = new StringBuilder();
        UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
        InterDao interDao = (InterDao)SpringHelper.getBean(InterDao.class.getName());
        if(express.getEmployee_no() != null){
            lst_user = new ArrayList<User>();
            lst_user.add(userManager.findEmployeeByEmployeeNo(express.getEmployee_no()));
            str_employee_nos.append("'").append(express.getEmployee_no()).append("'");

        }
        System.out.println(str_employee_nos.toString());
        List<Map<String,Object>> lst_data = new ArrayList<Map<String, Object>>();
        Map<String,Object> map_result = new HashMap<String, Object>();
        if(lst_user != null && lst_user.size() > 0){
        	List<?> listExpre = interDao.getExpressDataByStoreAndMouth_tu(str_employee_nos.toString(),express.getUpdate_user());
        	Map<String,Object> map_row =null;
            for(User user : lst_user){
                 map_row = new HashMap<String,Object>();
                 if(listExpre!=null&&listExpre.size()>0){
                	 for (Object obj : listExpre) {
                     	Object[] str=(Object[])obj;
     					if(str[0].equals(user.getEmployeeId())){
     						map_row.put("value",str[3]);
     						break;
     					}
     				}
                 }
                if(map_row.isEmpty()){
					map_row.put("value",0);
                }
                map_row.put("name",user.getName());
                map_row.put("id",user.getEmployeeId());
                lst_data.add(map_row);
            }
        }
        return lst_data;
	}

    @Override
    public WorkRecordTotal findWorkRecordTotalById(Long id) {
        WorkRecordTotalManager workRecordTotalManager = (WorkRecordTotalManager)SpringHelper.getBean("workRecordTotalManager");
        return workRecordTotalManager.findWorkRecordTotalById(id);
    }

	@Override
	public List<Map<String,Object>> getIndexByStoreCountNew(Express express) {

        StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
        OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");

		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map= new HashMap<String, Object>();
        InterDao interDao = (InterDao) SpringHelper.getBean(InterDao.class.getName());
        map.put("name","快递代送");
		map.put("value", interDao.getExpressStoreCountByCurrentMonth(express.getStore_id(),express.getUpdate_user()));
		list.add(map);
		Map<String,Object> map1= new HashMap<String, Object>();
		map1.put("name","用户画像");
		map1.put("value", interDao.getCustomerStoreCountByCurrentMonth(express.getStore_id(),express.getUpdate_user(),express.getExpress_no()));
		list.add(map1);
		
		Map<String,Object> map2= new HashMap<String, Object>();
		map2.put("name","拜访记录");
		map2.put("value", interDao.getRelationStoreCountByCurrentMonth(express.getStore_id(),express.getUpdate_user()));
		list.add(map2);

        /*Store store = storeManager.findStore(express.getStore_id());
        Map<String,Object> map3= new HashMap<String, Object>();
        map3.put("name","订单记录");
        if(store != null && store.getPlatformid() != null){
            map3.put("value",orderManager.getOrderCount(store.getPlatformid(),null,express.getUpdate_user()));
        }else{
            map3.put("value",0);
        }
        list.add(map3);*/
		/*Map<String,Object> map3= new HashMap<String, Object>();
		map3.put("name","订单");
		map3.put("value", 100);
		list.add(map3);*/
        return list;
	}

	@Override
	public List<Map<String,Object>> getIndexByEmployeeCountNew(Express express) {

        PlatformEmployeeManager platformEmployeeManager = (PlatformEmployeeManager)SpringHelper.getBean("platformEmployeeManager");
        OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");
        PlatformEmployee platformEmployee = platformEmployeeManager.findPlatformEmployeeByEmployeeNo(express.getEmployee_no());

		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map= new HashMap<String, Object>();
        InterDao interDao = (InterDao) SpringHelper.getBean(InterDao.class.getName());
        map.put("name","快递代送");
		map.put("value", interDao.getExpressCountByCurrentMonth(express.getEmployee_no(),express.getUpdate_user()));
		list.add(map);
		Map<String,Object> map1= new HashMap<String, Object>();
		map1.put("name","用户画像");
		map1.put("value", interDao.getCustomerCountByCurrentMonth(express.getEmployee_no(),express.getUpdate_user(),express.getExpress_no(),express.getStore_id()));
		list.add(map1);
		
		Map<String,Object> map2= new HashMap<String, Object>();
		map2.put("name","拜访记录");
		map2.put("value", interDao.getRelationCountByCurrentMonth(express.getEmployee_no(),express.getUpdate_user()));
		list.add(map2);

       /* Map<String,Object> map3= new HashMap<String, Object>();
        map3.put("name","订单记录");
        if(platformEmployee != null){
            map3.put("value",orderManager.getOrderCount(null,platformEmployee.getId(),express.getUpdate_user()));
        }else{
            map3.put("value",0);
        }
        list.add(map3);*/
		/*Map<String,Object> map3= new HashMap<String, Object>();
		map3.put("name","订单");
		map3.put("value", 100);
		list.add(map3);*/
        return list;
	}


	@Override
	public Result getStoreOwner(String employee_no) {
		Result res = new Result();
		UserManager usermanager=(UserManager) SpringHelper.getBean("userManager");
		StoreManager storemanager=(StoreManager) SpringHelper.getBean("storeManager");
		MessageNewManager appMessageManager = (MessageNewManager)SpringHelper.getBean("messageNewManager");
		//MessageTemplateManager mTemplateManager = (MessageTemplateManager)SpringHelper.getBean("messageTemplateManager");
		User user_employee=usermanager.findEmployeeByEmployeeNo(employee_no);
		
		if(user_employee == null){
			res.setCode(300);
			res.setMessage("没有" + employee_no + "此用户");
			return res;
		}
		
		Store store=storemanager.findStore(user_employee.getStore_id());
		if(store == null){
			res.setCode(400);
			res.setMessage("没有找到" + employee_no + "所属门店");
			return res;
		}
		
		User user_storeowner=usermanager.getUserEntity(store.getSkid());
		if(user_storeowner == null){
			res.setCode(500);
			res.setMessage(employee_no + "所属门店没有店长信息");
			return res;
		}
		Date sdate = new Date();
		Message appMessage  = new Message();
		appMessage.setCreate_time(sdate);
		appMessage.setCreate_user(user_employee.getName());
	    appMessage.setCreate_user_id(user_employee.getId());
	    appMessage.setTo_employee_id(user_storeowner.getId());
	    appMessage.setUpdate_time(sdate);
		appMessage.setTitle("重置密码提示");
        appMessage.setContent(user_employee.getName() +" 忘记密码，请重置！");
        appMessage.setJump_path("message_update_password");
        appMessage.setType("update_password");
        appMessage.setPk_id(user_employee.getId());
        appMessage.setIsRead(0);
        appMessage.setIsDelete(0);
        appMessage.setSendId(user_employee.getEmployeeId());
        appMessage.setReceiveId(user_storeowner.getEmployeeId());
        appMessageManager.saveObject(appMessage);
        appMessageManager.sendMessageAuto(user_storeowner, appMessage);
		res.setCode(200);
		res.setMessage("忘记密码消息发送成功,请通知店长重置密码！");
		return res;
		
	}
	


	@Override
	public Map<String, Object> queryCountDataByEmployee(Express express) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		InterDao interDao=(InterDao)SpringHelper.getBean(InterDao.class.getName());
		List<Map<String,Object>> listCustomer = interDao.getMonthDataCustomerByEmployee(express.getEmployee_no(), express.getUpdate_user());
		int[] arr1 = getMap(listCustomer);
		List<Map<String, Object>> listExpress = interDao.getMonthDataExpressByEmployee(express.getEmployee_no(), express.getUpdate_user());
		int[] arr2 = getMap(listExpress);
		List<Map<String, Object>> listRelation = interDao.getMonthDataRelationByEmployee(express.getEmployee_no(), express.getUpdate_user());
		int[] arr3 = getMap(listRelation);
		map.put("listCustomer", arr1);
		map.put("listExpress", arr2);
		map.put("listRelation", arr3); 
		map.put("monthData", getMapString(listRelation));
		return map;
	}
	
	public int[] getMap(List<Map<String,Object>> list){
		int i=0;
		int[] arr=new int[12];
		for (Map<String, Object> map : list) {
			System.out.println(map.get("total"));
			arr[i]=Integer.parseInt(String.valueOf(map.get("total")));
			i++;
		}
		return arr;
	}
	public String[] getMapString(List<Map<String,Object>> list){
		int i=0;
		String[] arr=new String[12];
		for (Map<String, Object> map : list) {
			arr[i]=map.get("month")+"";
			i++;
		}
		return arr;
	}

	@Override
	public List<Map<String, Object>> getCustomerListForMonthData(Customer customer) {
		String whString="";
		if(customer.getEmployee_no()!=null){
			whString=" and T.employeeId='"+customer.getEmployee_no()+"'";
		}else{
			whString=" and T.store_id='"+customer.getStore_id()+"'";
		}
		InterDao interDao=(InterDao)SpringHelper.getBean(InterDao.class.getName());
		return interDao.getCustomerByMonth(whString,null);
	}
	
//	
//	//手机端 用户卡 点击数字拜访记录 
//	public List<Map<String, Object>> getRelationListForMonthData(Customer customer) {
//		String whString="";
//		if(customer.getEmployee_no()!=null){
//			whString=" and T.employeeId='"+customer.getEmployee_no()+"'";
//		}else{
//			whString=" and T.store_id='"+customer.getStore_id()+"'";
//		}
//		InterDao interDao=(InterDao)SpringHelper.getBean(InterDao.class.getName());
//		return interDao.getCustomerRelationByMonth(whString,null);
//	}
	
	
	/*******************CRM接口***********************/
	
	/**
	 *APP 验证是否是 A国安侠
	* @param employee_no
	 * @return
	 */
	@Override
    public Area queryAreaAppByCurrEmployeeNo(String employee_no){
		AreaManager areaManager = (AreaManager) SpringHelper.getBean("areaManager");
		if(employee_no!=null&&employee_no.length()>0){
			 IFilter iFilter =FilterFactory.getSimpleFilter("employee_a_no",employee_no);
	         List<Area> areaInfoList = (List<Area>) areaManager.getList(iFilter);
	         if(areaInfoList!=null&&areaInfoList.size()>0){
	        	 return areaInfoList.get(0);
	         }
		}
        return null;
    }
	
	/**
     * APP 分片查询订单信息
     * @param employee_no
	 * @param pageInfo
	 * @return
	 */
    @Override
    public Result queryOrderListAppByArea(Long store_id,String employee_no,PageInfo pageInfo,Long area_id){
    	Result result = new Result();
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
    	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
    	Map<String, Object> retMap = null;
    	try {
    		User user = null;
    		if(employee_no==null){
    			user = new User();
    			user.setStore_id(store_id);
    		}else{
    			user = userManager.findEmployeeByEmployeeNo(employee_no);
    		}
        	Map<String, Object> store = storeManager.getStoreById(user.getStore_id());
        	AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
        	String area_names = areaDao.querytinvillagebyemployeeno(employee_no,area_id);
        	retMap = orderDao.queryOrderListByArea(store.get("platformid").toString(), area_names,pageInfo);
        	result.setDataMap(retMap);
        	result.setCode(CodeEnum.success.getValue()); 
            result.setMessage(CodeEnum.success.getDescription());
    	} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
    }
	
	/**
	 * APP 查询当前登录国安侠a的 当月的  所有快递信息
	 * @param employee_no
	 * @param pageInfo
	 * @return
	 */
	@Override
	public Result queryExpressAPPByEmployeeNo(String employee_no,PageInfo pageInfo){
		Result result = new Result();
		ExpressDao expressDao = (ExpressDao) SpringHelper.getBean(ExpressDao.class.getName());
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		try {
			User user = userManager.findEmployeeByEmployeeNo(employee_no);
			Map<String, Object> retMap = expressDao.queryExpressByEmployeeNo(user.getStore_id(), employee_no, pageInfo);
			result.setDataMap(retMap);
			result.setCode(CodeEnum.success.getValue());
	        result.setMessage(CodeEnum.success.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	@Override
	public Result findRelation_area_employee_app(String employeeNo,PageInfo pageInfo,Long area_id) {
		Result result = new Result();
		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		
       
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			map = relationDao.findRelation_area_employee_app(pageInfo, employeeNo,area_id);
		} catch (Exception e) {
			e.printStackTrace();
			result.setDataMap(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
		result.setDataMap(map);
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
		return result;
	}

	
	@Override
	public Result findRelation_customer_app(Long customer_id, PageInfo pageInfo) {
		Result result = new Result();
		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		HouseCustomerDao hcdao = (HouseCustomerDao) SpringHelper.getBean(HouseCustomerDao.class.getName());
        CustomerManager cManager = (CustomerManager)SpringHelper.getBean("customerManager");
		Map<String,Object> map = new HashMap<String,Object>();
		Customer customer = new Customer();
		try {
			Map<String, Object> houseMap = hcdao.selectHouseByCustomer(customer_id);
			if(houseMap!=null){
				Object houseId = houseMap.get("house_id");
				Long house_id = houseId==null?null:Long.parseLong(String.valueOf(houseId));
				Customer customer2 = cManager.findCustomerByCustomerIdAndHouseId_crm(customer_id,house_id, null);
				customer.setAddress(customer2.getAddress());
				customer.setName(customer2.getName());
				customer.setAge(customer2.getAge());
				customer.setMobilephone(customer2.getMobilephone());
				customer.setCus_pic(customer2.getCus_pic());
				customer.setSex(customer2.getSex());
			}
			map = relationDao.findRelation_customer_crm_app(pageInfo, customer_id);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setDataMap(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
		result.setDataMap(map);
		result.setCustomer(customer);
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
		return result;
	}

	
	
	/**
     * 根据订单sn编号 查询明细信息 
     * @param order_sn
     * @return
     */
    @Override
    public Result queryOrderInfoBySN(String order_sn){
    	Result result = new Result();
    	Map<String,Object> map = new HashMap<String,Object>();
    	try {
    		OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    		map = orderDao.getOrderByOrderSN(order_sn);
			String order_id = map.get("id")==null?"":map.get("id").toString();
			List<Map<String, Object>> item_list = orderDao.queryOrderItemInfoById(order_id);
			map.put("employee", "");
			if(map.get("employee_id")!=null){
				Map<String, Object> employeeInfo = orderDao.getEmployeeByIdOfGemini(map.get("employee_id").toString());
				map.put("employee", employeeInfo);
			}
			
			map.put("item_list", item_list);
			map.put("create_time", map.get("create_time"));
		} catch (Exception e) {
			e.printStackTrace();
			result.setDataMap(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
		result.setDataMap(map);
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
    	return result;
    }

	
	@Override
	public Result getRelationOfEmployee_month_app(String employeeNo,Long area_id) {
		
		Result result = new Result();
		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		Map<String,Object> map = new HashMap<String,Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
		List<Map<String, Object>>	list = relationDao.getRelationOfEmployee_month(employeeNo,area_id);
		String[] dateArr = new String[5];
		Date cur_date = new Date();
		Calendar cal = Calendar.getInstance();
		Map<String, Object> map1 = null;
		
		for(int i=1;i<dateArr.length;i++){
			cal.setTime(cur_date);
			cal.add(Calendar.MONTH, -(dateArr.length-i));
			map1 = new HashMap<String,Object>();
			map1.put("relationDate",cal.get(Calendar.MONTH) + 1+"月");
			map1.put("total", 0);
			map1.put("flagDate", sdf.format(cal.getTime()));
			resultList.add(map1);
		}
		cal.setTime(cur_date);
		map1 = new HashMap<String,Object>();
		map1.put("relationDate",cal.get(Calendar.MONTH) + 1+"月");
		map1.put("total", 0);
		map1.put("flagDate", sdf.format(cur_date));
		resultList.add(map1);
		for(int j=0;j<resultList.size();j++){
			Object dateStr = resultList.get(j).get("flagDate");
			for(int m=0;m<list.size();m++){
				Object dateStr1 = list.get(m).get("relationDate");
				if(dateStr.equals(dateStr1)){
					resultList.get(j).put("total", list.get(m).get("total"));
					break;
				}
			}
		}
		
		
		map.put("relation", resultList);
		} catch (Exception e) {
			e.printStackTrace();
			result.setDataMap(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
		result.setDataMap(map);
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
		return result;
	}

	
	@Override
	public Result getCustomerOfEmployee_month_app(String employeeNo,Long area_id) {
		Result result = new Result();
		CustomerDao customerDao = (CustomerDao) SpringHelper.getBean(CustomerDao.class.getName());
		Map<String,Object> map = new HashMap<String,Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
		List<Map<String, Object>>	list = customerDao.queryCustomerOfEmployee_month(employeeNo,area_id);
		String[] dateArr = new String[5];
		Calendar cal = Calendar.getInstance();
		Date cur_date = new Date();
		cal.setTime(cur_date);
		Map<String, Object> map1 = null;
		
		
		for(int i=1;i<dateArr.length;i++){
			cal.setTime(cur_date);
			cal.add(Calendar.MONTH, -(dateArr.length-i));
			map1 = new HashMap<String,Object>();
			map1.put("createTime",cal.get(Calendar.MONTH) + 1+"月");
			map1.put("total", 0);
			map1.put("flagDate", sdf.format(cal.getTime()));
			resultList.add(map1);
		}
		cal.setTime(cur_date);
		map1 = new HashMap<String,Object>();
		map1.put("createTime",cal.get(Calendar.MONTH) + 1+"月");
		map1.put("total", 0);
		map1.put("flagDate", sdf.format(cur_date));
		resultList.add(map1);
		for(int j=0;j<resultList.size();j++){
			Object dateStr = resultList.get(j).get("flagDate");
			for(int m=0;m<list.size();m++){
				Object dateStr1 = list.get(m).get("createTime");
				if(dateStr.equals(dateStr1)){
					resultList.get(j).put("total", list.get(m).get("total"));
					break;
				}
			}
		}
		map.put("customer", resultList);
		} catch (Exception e) {
			e.printStackTrace();
			result.setDataMap(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
		result.setDataMap(map);
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
		return result;
	}
	
    
    /**
     * APP手机国安侠分片 查询五个月的图表
     */
    @Override
    public Result queryOrderFiveMonth(Long store_id,String employee_no,Long area_id){
    	Result result = new Result();
    	Map<String,Object> map = new HashMap<String,Object>();
    	try {
    		OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");
    		List<Map<String, Object>> mapList = orderManager.queryOrderFiveMonth(store_id,employee_no,area_id);
    		map.put("order_count", mapList);
		} catch (Exception e) {
			e.printStackTrace();
			result.setDataMap(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
    	result.setDataMap(map);
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
    	return result;
    }

    
    
    /**
     * APP个人动态 的送单记录 根据员工号 查询订单。
     * @param employee_no
     * @param pageInfo
     * @return
     */
    @Override
    public Result queryOrderListByEmployeeNo(String employee_no, PageInfo pageInfo,Long area_id){
    	Result result = new Result();
    	Map<String,Object> map = null;
    	try {
    		OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");
    		map = orderManager.queryOrderListByEmployeeNo(employee_no, pageInfo,area_id);
		} catch (Exception e) {
			e.printStackTrace();
			result.setDataMap(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
    	result.setDataMap(map);
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
    	return result;
    }
    
    /**
     * APP端，个人中心 根据员工号 查询订单图表 
     * @param employee_no
     * @return
     */
    @Override
    public Result queryOrderFiveMonthOrderApp(String employee_no){
    	Result result = new Result();
    	Map<String,Object> map = new HashMap<String, Object>();
    	try {
    		OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");
    		List<Map<String, Object>> mapList = orderManager.queryOrderFiveMonthOrderApp(employee_no);
    		map.put("order_list", mapList);
		} catch (Exception e) {
			e.printStackTrace();
			result.setDataMap(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
    	result.setDataMap(map);
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
    	return result;
    }
    
    

	@Override
	public Result getRelationOfEmployee_app(PageInfo page, String employeeNo) {
		Result result = new Result();
    	Map<String,Object> map = new HashMap<String,Object>();
    	try {
    		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
    		Map<String, Object> mapList = relationDao.findrelation_employee_crm_app(page, employeeNo);
    		result.setDataMap(mapList);
		} catch (Exception e) {
			e.printStackTrace();
			result.setDataMap(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
    	
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
    	return result;
		
	}


	@Override
	public Result findRelationOfEmployee_byFiveMonth(String employeeNo) {
		Result result = new Result();
    	Map<String,Object> map = new HashMap<String,Object>();
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
    		List<Map<String, Object>> mapList = relationDao.findRelationOfEmployee_byFiveMonth(employeeNo);
    		String[] dateArr = new String[5];
    		Date cur_date = new Date();
    		Calendar cal = Calendar.getInstance();
    		Map<String, Object> map1 = null;
    		
    		for(int i=1;i<dateArr.length;i++){
    			cal.setTime(cur_date);
    			cal.add(Calendar.MONTH, -(dateArr.length-i));
    			map1 = new HashMap<String,Object>();
    			map1.put("relationDate",cal.get(Calendar.MONTH) + 1+"月");
    			map1.put("total", 0);
    			map1.put("flagDate", sdf.format(cal.getTime()));
    			resultList.add(map1);
    		}
    		cal.setTime(cur_date);
    		map1 = new HashMap<String,Object>();
    		map1.put("relationDate",cal.get(Calendar.MONTH) + 1+"月");
    		map1.put("total", 0);
    		map1.put("flagDate", sdf.format(cur_date));
    		resultList.add(map1);
    		for(int j=0;j<resultList.size();j++){
    			Object dateStr = resultList.get(j).get("relationDate");
    			for(int m=0;m<mapList.size();m++){
    				Object dateStr1 = mapList.get(m).get("relationDate");
    				if(dateStr.equals(dateStr1)){
    					resultList.get(j).put("total", mapList.get(m).get("total"));
    					break;
    				}
    			}
    		}
    		map.put("relation", resultList);
    		result.setDataMap(map);
		} catch (Exception e) {
			e.printStackTrace();
			result.setDataMap(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
    	
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
    	return result;
	}

	
	@Override
	public Result findEmployee_areaInfo(String employeeNo,Long area_id) {
		Result result = new Result();
		Map<String, Object> resultMap = new HashMap<String,Object>();
		try {
			HumanresourcesManager humanresourcesManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
			Map<String, Object> map1 = humanresourcesManager.queryEmployeeInfoByNo(employeeNo);
			AreaManager areaManager = (AreaManager)SpringHelper.getBean("areaManager");
			Map<String, Object> map2 = areaManager.queryAreaByEmployeeNo(employeeNo,area_id);
			OrderManager orderManager = (OrderManager)SpringHelper.getBean("orderManager");
			PageInfo pageInfo = new PageInfo();
			pageInfo.setRecordsPerPage(10);
			pageInfo.setCurrentPage(1);
			Map<String, Object> map3 =  orderManager.queryOrderListByEmployeeNo(employeeNo,pageInfo,null);
			map1.put("orderTotal", map3.get("totalpage"));
			resultMap.put("allAreaInfo", map2);
			resultMap.put("employeeInfo", map1);
			result.setDataMap(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setDataMap(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
		
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
    	return result;
	}

	
	
	public Result getAllAreaOfEmployee(String employeeNo) {
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
        List<Map<String,Object>> lst_area_list = areaDao.getAllAreaOfEmployee(employeeNo);
        Result result = new Result();
        result.setData(lst_area_list);
        result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
    	return result;
	}

	
	@Override
	public Result getRelationAndCustomerOfCrm_app(Long storeId, String query_date) {
		Map<String,Object> map = new HashMap<String,Object>();
		Result result = new Result();
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		RelationDao relationDao = (RelationDao)SpringHelper.getBean(RelationDao.class.getName());
		List<Map<String, Object>> customerList = customerDao.getCustomerOfArea(storeId,query_date);
		List<Map<String, Object>> relationList = relationDao.getRelationOfArea(storeId,query_date);
		map.put("relation", relationList);
		map.put("customer", customerList);
		result.setData(map);
		return result;
	}

	
	@Override
	public Result getRelationOfCrm_app(Long storeId, String query_date) {
		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		Result result = new Result();
		result.setData(relationDao.getRelationOfEmployee(storeId,query_date));
		return result;
		
	}

	
	@Override
	public Result getCustomerOfCrm_app(Long storeId, String query_date) {
		Result result = new Result();
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		List<Map<String, Object>> customerList = customerDao.getCustomerOfEmployee(storeId,query_date);
		result.setData(customerList);
		return result;
		
	}

	
	@Override
	public Result queryRelationAndCustomerOfYear_crm_app(Long storeId) {
		Map<String,Object> teMap = new HashMap<String,Object>();
		Result result = new Result();
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		RelationDao relationDao = (RelationDao)SpringHelper.getBean(RelationDao.class.getName());
		//OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		List<Map<String, Object>> customerList = customerDao.getCustomerOfYear(storeId);
		List<Map<String, Object>> relationList = relationDao.getRelationOfYear(storeId);
    	Store store = storeManager.findStore(storeId);
    	//List<Map<String, Object>> maps = orderDao.queryOrderCountByMonthStoreId(store.getPlatformid());
    	
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
		teMap.put("relation", r_List);
		teMap.put("customer", c_List);
		//teMap.put("ordermonth", maps);
		result.setData(teMap);
		return result;
		
	}

	
	
	/**
	 * app_CRM店长 查询 当年 每月 订单数量及金额 图表用
	 * @param store_id
	 * @return
	 */
	@Override
	public Result queryOrderInfoOfYear_crm_app(Long storeId) {
		Result result = new Result();
		OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");
		Map<String, Object> teMap = orderManager.queryOrderCountByMonthStoreId(storeId);
		result.setData(teMap);
		return result;
	}
	/**
	 * app_CRM店长 查询 分片区订单统计数量 以及 统计金额 图表用
	 * @param store_id
	 * @return
	 */
	@Override
	public Result queryOrderTotalByArea_crm_app(Long store_id){
		Result result = new Result();
		OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");
		Map<String, Object> teMap = orderManager.queryOrderTotalByArea(store_id);
		result.setData(teMap);
		return result;
	}
	
	/**
	 * app_crm店长 根据门店 查询 送单量 图表用
	 * @param store_id
	 * @return
	 */
	@Override
	public Result queryOrderCountByStoreId_crm_app(Long store_id){
		Result result = new Result();
		OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");
		Map<String, Object> teMap = orderManager.queryOrderCountByStoreId(store_id);
		result.setData(teMap);
		return result;
	}
	
	/**
	 * app_crm店长 根据门店查询 所有国安侠 快递代送量 图表用 
	 * @param store_id
	 * @return
	 */
	@Override
	public Result queryExpressListByStoreId_crm_app(Long store_id){
		Result result = new Result();
		ExpressManager expressManager = (ExpressManager) SpringHelper.getBean("expressManager");
		Map<String, Object> teMap = expressManager.queryExpressListByStoreId(store_id);
		result.setData(teMap);
		return result;
	}
	
	
	public Result getRelationAndCustomerOfStore(Long storeId, String query_date){
		
		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		Result result = new Result();
		List<Map<String, Object>> relation_list = relationDao.getRelationOfEmployee(storeId,query_date);
		List<Map<String, Object>> customerList = customerDao.getCustomerOfEmployee(storeId,query_date);
		List<Map<String, Object>> result_list = new ArrayList<Map<String, Object>>();
		for(int i=0;i<relation_list.size();i++){
			Map<String, Object> r_map = relation_list.get(i);
			r_map.put("customer_total", 0);
			Object employee_no = r_map.get("employeeId");
			for(int j=0;j<customerList.size();j++){
				Map<String, Object> c_map = customerList.get(j);
				if(employee_no.equals(c_map.get("employeeId"))){
					r_map.put("customer_total", c_map.get("total"));
					break;
				}
			}
			result_list.add(r_map);
			
		}
		result.setData(result_list);
		return result;
		
	}

	
	@Override
	public Result findAreaInfoOfEmployee_app(String employeeNo, Long area_id) {
		Result result = new Result();
		Map<String, Object> resultMap = new HashMap<String,Object>();
		try {
			HumanresourcesManager humanresourcesManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
			
			AreaManager areaManager = (AreaManager)SpringHelper.getBean("areaManager");
			Map<String, Object> map2 = areaManager.queryAreaByEmployeeNo(employeeNo,area_id);
			resultMap.put("allAreaInfo", map2);
			
			CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
			Long houseCount =  customerDao.getHouseOfArea(employeeNo,area_id);
			resultMap.put("housetotal", houseCount);
			
			result.setDataMap(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setDataMap(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
		
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
    	return result;
	}

	
	@Override
	public Result findEmployeePerformance_app(String employeeNo, Long area_id) {
		Result result = new Result();
		Map<String, Object> resultMap = new HashMap<String,Object>();
		try {
			HumanresourcesManager humanresourcesManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
			Map<String, Object> map1 = humanresourcesManager.queryEmployeeInfoByNo(employeeNo);
			
			
			
			OrderManager orderManager = (OrderManager)SpringHelper.getBean("orderManager");
			PageInfo pageInfo = new PageInfo();
			pageInfo.setRecordsPerPage(10);
			pageInfo.setCurrentPage(1);
			//Map<String, Object> map3 =  orderManager.queryOrderListByEmployeeNo(employeeNo,pageInfo,null);//暂时不放开
			//map1.put("orderTotal", map3.get("totalpage"));
			resultMap.put("employeeInfo", map1);
			
			result.setDataMap(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setDataMap(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
		
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
    	return result;
	}

	
	@Override
	public Result queryOrderOfAreaByApp(String employee_no, PageInfo pageInfo,String order_sn) {
		
		Result result = new Result();
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	Map<String, Object> r_map = null;
    	StringBuilder orderSb= new StringBuilder();
    	StringBuilder orderSb2 = new StringBuilder();
    	List<Map<String, Object>> orderList = new ArrayList<Map<String,Object>>();
    	List<Map<String, Object>> orderList2 = new ArrayList<Map<String,Object>>();
    	List<Map<String, Object>> orderAddressList = new ArrayList<Map<String,Object>>();
    	try {
    		
    		r_map = orderDao.queryOrderOfArea(employee_no, pageInfo,order_sn);
    		if(r_map.get("data")!=null){
    			orderList = (List<Map<String,Object>>)r_map.get("data");
    			for(int i=0;i<orderList.size();i++){
    				orderSb.append(",'").append(orderList.get(i).get("order_address_id")).append("'");
        		}
    			
    			Map<String, Object> orderAddressMap = new HashMap<String,Object>();
    			if(orderSb.length()>0){
    				orderAddressList = orderDao.getOrderAddressByAddressId(orderSb.toString().substring(1));
    				for(int j=0;j<orderAddressList.size();j++){
        				Map<String, Object> tMap = orderAddressList.get(j);
        				Object order_address_id_tmp = tMap.get("id");
        				if(order_address_id_tmp!=null){
        					orderAddressMap.put(order_address_id_tmp.toString(),tMap.get("address"));
        				}
        				
        			}
    			}
    			
    			System.out.println("获取订单地址addr----");
    			
    			for(int i=0;i<orderList.size();i++){
    				Map<String, Object> tMap = orderList.get(i);
    				
    				tMap.put("address", orderAddressMap.get(tMap.get("order_address_id")));
    				
    				orderList2.add(tMap);
        		}
    			r_map.put("data", orderList2);
    			System.out.println("结束");
    			
    		}
    		
    		
        	result.setDataMap(r_map);
        	result.setCode(CodeEnum.success.getValue()); 
            result.setMessage(CodeEnum.success.getDescription());
    	} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
	}

	/**
	 * 国安侠、店长获取门店服务范围、所管理小区、热力图信息
	 */
	@Override
	public Map<String, Object> selecTinyVillageCoordByEmployeeNo(String employeeNo,String storeNo) {
		Map<String, Object> mapStr = new HashMap<String, Object>();
		MongoDBManager mongoManager = (MongoDBManager)SpringHelper.getBean("mongoDBManager");
		
		//小区信息
		Map<String, Object> tinyarea = mongoManager.selecTinyVillageCoordByEmployeeNo(employeeNo);
		mapStr.put("tinyarea", tinyarea);
		
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		String platformid = null;
		Store store = (Store) storeManager.findStoreByStoreNo(storeNo);
		if(store!=null){
			platformid = store.getPlatformid();
		}
		//门店服务区
		Map<String, Object> storeServiceArea = mongoManager.getStoreServiceArea(platformid);
		mapStr.put("storearea", storeServiceArea);
		
		//热力图信息
		SimpleDateFormat df =  new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal=Calendar.getInstance();//获取当前日期 
        cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String firstDay = df.format(cal.getTime());
        String toDay = df.format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("'"+storeNo+"'");
		OrderDao orderHeatDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		List<Map<String, Object>> lagLatByOrder = orderHeatDao.getLagLatByOrder(sb.toString(),firstDay,toDay);
		if(lagLatByOrder.size()>0){
			if(lagLatByOrder.get(0).get("lnt") == null){
				lagLatByOrder.remove(0);
			}
		}
		mapStr.put("storeorderheat",lagLatByOrder);

		return mapStr;
	}
	@Override
	public Result queryVillageInfoByAreaNoApp(String area_id) {
		Result result = new Result();
		MongoDBManager mongoDBManager = (MongoDBManager)SpringHelper.getBean("mongoDBManager");
//		List tinyOrderLastMonthCount = mongoDBManager.selecTinyOrderLastMonthCount();
		TinyVillageDao tinyVillageDao = (TinyVillageDao) SpringHelper.getBean(TinyVillageDao.class.getName());
		Map<String, Object> r_map = null;
    	Map<String, Object> r_map2 = new HashMap<String, Object>();
    	List<Map<String, Object>> villageList = new ArrayList<Map<String,Object>>();
    	List<Map<String, Object>> villageList2 = new ArrayList<Map<String,Object>>();
    	try {
    		
    		r_map = tinyVillageDao.queryVillageInfo(area_id);
    		villageList = (List<Map<String,Object>>)r_map.get("data");
    		List<JSONObject> custmer_count =  mongoDBManager.selecTinyCustomerLastMonthCountByVillage();
    		List<JSONObject> orderCount =  mongoDBManager.selecTinyOrderLastMonthCountByVillage();
    		for (int i = 0; i < villageList.size(); i++) {
    			Map<String, Object> mapVillage = villageList.get(i);
    			String tiny_village_code = (String) mapVillage.get("tiny_code");
    			for (JSONObject orderC : orderCount) {
    				JSONObject map = (JSONObject) orderC.get("_id");
    				String tiny_village_code_a = map.get("tiny_village_code")==null?"":map.get("tiny_village_code").toString();
    				String tiny_village_code_b = orderC.get("count")==null?"":orderC.get("count").toString();
					if(tiny_village_code.equals(tiny_village_code_a)){
						mapVillage.put("order_count", tiny_village_code_b);
					}
				}
    			for (JSONObject custmerC : custmer_count) {
    				JSONObject map = (JSONObject) custmerC.get("_id");
    				String tiny_village_code_c = map.get("tiny_village_code")==null?"":map.get("tiny_village_code").toString();
    				String tiny_village_code_d = custmerC.get("count")==null?"0":custmerC.get("count").toString();
					if(tiny_village_code.equals(tiny_village_code_c)){
						mapVillage.put("custmer_count", tiny_village_code_d);
					}
				}
    			villageList2.add(mapVillage);
			}
    		r_map2.put("data", villageList2);
        	result.setDataMap(r_map2);
        	result.setCode(CodeEnum.success.getValue()); 
            result.setMessage(CodeEnum.success.getDescription());
    	} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
	}

	@Override
	public Result findTinyTownByVillageId(String villageId) {
		Result result = new Result();
		TinyVillageManager tinyVillageManager = (TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
		Map<String,Object> tinyVillageInfoMap = tinyVillageManager.getTinyVillageInformationById(Long.parseLong(villageId));
		result.setDataMap(tinyVillageInfoMap);
		result.setCode(CodeEnum.success.getValue()); 
        result.setMessage(CodeEnum.success.getDescription());
		return result;
	}

	@Override
	public Result findEmployeePicByEmployeeId(String employee_id) {
		Result result = new Result();
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
		Map<String, Object> map = orderDao.getEmployeeByEmployeeIdOfGemini(employee_id);
		result.setDataMap(map);
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
    	return result;
	}

	@Override
	public Result getEmployeeOfStore(Long storeId, String query_date) {

		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
		Result result = new Result();
		List<Map<String, Object>> relation_list = relationDao.getRelationOfEmployee(storeId,query_date);
		List<Map<String, Object>> customerList = customerDao.getCustomerOfEmployee(storeId,query_date);
		List<Map<String, Object>> result_list = new ArrayList<Map<String, Object>>();
		for(int i=0;i<relation_list.size();i++){
			Map<String, Object> r_map = relation_list.get(i);
			r_map.put("customer_total", 0);
			r_map.put("employee_pic", "");
			Object employee_no = r_map.get("employeeId");
			for(int j=0;j<customerList.size();j++){
				Map<String, Object> c_map = customerList.get(j);
				if(employee_no.equals(c_map.get("employeeId"))){
					r_map.put("customer_total", c_map.get("total"));
						Map<String, Object> employeeInfo = orderDao.getEmployeeByEmployeeIdOfGemini(c_map.get("employeeId").toString());
						r_map.put("employee_pic", employeeInfo);
					break;
				}
			}
			result_list.add(r_map);
			
		}
		result.setData(result_list);
		return result;
	}

	@Override
	public Result queryOrderListAppByAreaNew(Long store_id, String order_sn, PageInfo pageInfo, Long area_id) {
		Result result = new Result();
		OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	AreaManager areaManager = (AreaManager) SpringHelper.getBean("areaManager");
    	Map<String, Object> retMap = null;
    	try {
    		Area queryArea = areaManager.queryArea(area_id);
        	retMap = orderDao.queryOrderOfAreaForApp(queryArea==null?"":queryArea.getArea_no(),pageInfo,order_sn);
        	result.setDataMap(retMap);
        	result.setCode(CodeEnum.success.getValue()); 
            result.setMessage(CodeEnum.success.getDescription());
    	} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
	}
	
	@Override
	public Result queryCustomerStatBycity(String city_id){
		Result result = new Result();
		Map<String, Object> retMap = null;
		UserOperationStatManager userOperationStatManager = (UserOperationStatManager) SpringHelper.getBean("userOperationStatManager");
		try {
        	retMap = userOperationStatManager.queryCustomerStatBycity(city_id);
        	result.setDataMap(retMap);
        	result.setCode(CodeEnum.success.getValue()); 
            result.setMessage(CodeEnum.success.getDescription());
    	} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
	}
	
	//加载时 显示二维码 
	@Override
	public String showBarCodeImage(){
		CodeLoginManager codeLoginManager = (CodeLoginManager) SpringHelper.getBean("codeLoginManager");
		Map<String, String> codeurlMap = BarCodeUtils.createBarCodeImage();
		//插入数据库当前的标识。
		String uuid = codeurlMap.get("uuid");
		CodeLogin codeLogin = new CodeLogin();
		codeLogin.setUuid(uuid);
		codeLogin.setUsed(0L);//0未登录 1 登录中  2已登录 
		codeLogin.setInvalid(0L);//0 未失效  1失效 
		codeLoginManager.saveCodeLogin(codeLogin);
		return codeurlMap.get("url");
	}
	
	//轮询方法 未完待续
	public CodeLogin loopVerification(String uuid){
		CodeLoginManager codeLoginManager = (CodeLoginManager) SpringHelper.getBean("codeLoginManager");
		IFilter iFilter =FilterFactory.getSimpleFilter("uuid='"+uuid+"'");
		List<CodeLogin> lstCodeLogins = (List<CodeLogin>) codeLoginManager.getList(iFilter);
		CodeLogin codeLogin = null;
		if(lstCodeLogins!=null&&lstCodeLogins.size()>0){
			codeLogin = lstCodeLogins.get(0);
			Long used=codeLogin.getUsed();//0未登录 1 登录中  2已登录 
			Long invalid=codeLogin.getInvalid();//0 未失效
			
			if(used.equals(1L)&&invalid.equals(0L)){
				codeLogin.setUsed(2L);//已登录
			}
			codeLoginManager.saveCodeLogin(codeLogin);
			
		}
		return codeLogin;
	}
	//验证登录通过 修改数据 
	@Override
	public Result doVerification(String uuid,Long userid,String md5Pwd,String token){
		Result result = new Result();
		CodeLoginManager codeLoginManager = (CodeLoginManager) SpringHelper.getBean("codeLoginManager");
		IFilter iFilter =FilterFactory.getSimpleFilter("uuid='"+uuid+"'");
		List<CodeLogin> lstCodeLogins = (List<CodeLogin>) codeLoginManager.getList(iFilter);
		if(lstCodeLogins!=null&&lstCodeLogins.size()>0){
			//验证用户
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			User user = (User) userManager.getObject(userid);
			boolean isVerificationSuccess=false;
			if(user!=null&&user.getPassword().equals(md5Pwd)){
				//通过
				isVerificationSuccess=true;
			}
			
			CodeLogin codeLogin = lstCodeLogins.get(0);
			if(isVerificationSuccess&&codeLogin.getInvalid().equals(Long.parseLong("0"))&&codeLogin.getUsed().equals("0")){
				result.setCode(200);//正常
				//处理数据  设置登录状态
				codeLogin.setSuccess("success");
				codeLogin.setClicktoken(token);
				codeLogin.setUsed(1L);//设置状态为登录中...0未登录 1 登录中  2已登录 
				codeLogin.setUserid(user.getId());//当前登录的用户ID
				codeLoginManager.saveCodeLogin(codeLogin);
				result.setMessage("登录成功！");
			}else{
				result.setCode(201);//异常
				result.setMessage("登录异常！");
			}
		}
		return result;
	}

	
	//手机访问记录
	@Override
	public UserLoginLog saveAppUserAccessLog(User userInfo){
		UserLoginLogManager userLoginLogManager = (UserLoginLogManager) SpringHelper.getBean("userLoginLogManager");
		UserLoginLog rt_obj = userLoginLogManager.saveAppUserAccessLog(userInfo);
		return rt_obj;
	}
	
	//手机下载记录
	@Override
	public String saveDownloadLog(String type,String dip,String cityname){
		//取得当前最新的版本。
		AppVersionManager appVersionManager = (AppVersionManager) SpringHelper.getBean("appVersionManager");
		AppDownLoadLogManager appDownLoadLogManager = (AppDownLoadLogManager) SpringHelper.getBean("appDownLoadLogManager");
		AppVersion appVersion = appVersionManager.queryAppVersionNew();
		AppDownloadLog appDownloadLog = new AppDownloadLog();
		appDownloadLog.setDownloadtype(type);
		appDownloadLog.setDownloadVersion(appVersion.getVersion());
		appDownloadLog.setDip(dip);
		appDownloadLog.setCityname(cityname);
		appDownLoadLogManager.saveAppDownLoadLog(appDownloadLog);
		return type;
	}
	
	//接口根据ID查询用户 
	@Override
	public Result querySiteSelectionById(Long id){
		Result result = new Result();
		SiteSelectionManager siteSelectionManager = (SiteSelectionManager) SpringHelper.getBean("siteSelectionManager");
		SiteSelection siteSelection = (SiteSelection) siteSelectionManager.getObject(id);
		if(siteSelection!=null){
			result.setCode(CodeEnum.success.getValue());
	        result.setMessage(CodeEnum.success.getDescription());
	        SiteSelectionDto siteSelectionDto = new SiteSelectionDto();
	        BeanUtils.copyProperties(siteSelection, siteSelectionDto);
	        result.setData(siteSelectionDto);
		}else{
			result.setCode(CodeEnum.nullData.getValue());
	        result.setMessage(CodeEnum.nullData.getDescription());
		}
		return result;
	}
	
	
	//接口 用户注册
	@Override
	public Result saveSiteSelection(SiteSelection siteSelection){
		Result result = new Result();
		SiteSelectionManager siteSelectionManager = (SiteSelectionManager) SpringHelper.getBean("siteSelectionManager");
		try {
			if(siteSelection==null){
				result.setCode(CodeEnum.error.getValue());
		        result.setMessage("参数不能为空！");
		        return result;
			}
			if(siteSelection.getReal_name()==null||siteSelection.getReal_name().trim()==""){
				result.setCode(CodeEnum.error.getValue());
		        result.setMessage("姓名不能为空！");
		        return result;
			}
			if(siteSelection.getMobilephone()==null||siteSelection.getMobilephone().trim()==""){
				result.setCode(CodeEnum.error.getValue());
		        result.setMessage("电话不能为空！");
		        return result;
			}
			
			//String mobilephone = siteSelection.getMobilephone();
			/*//验证电话是否重复
			SiteSelection existSiteSelection = siteSelectionManager.querySiteSelectionByPhone(mobilephone);
			if(existSiteSelection!=null){
				result.setCode(CodeEnum.error.getValue());
		        result.setMessage("该电话已注册！");
		        return result;
			}*/
			
			//处理siteselection图片只存文件名
			if(siteSelection.getCards()!=null&&siteSelection.getCards().length()>0){
				siteSelection.setCards(siteSelection.getCards().substring(siteSelection.getCards().length()-36,siteSelection.getCards().length()));
			}
			if(siteSelection.getCards_back()!=null&&siteSelection.getCards_back().length()>0){
				siteSelection.setCards_back(siteSelection.getCards_back().substring(siteSelection.getCards_back().length()-36,siteSelection.getCards_back().length()));
			}
			SiteSelectionDto selectionDto = new SiteSelectionDto();
			if(siteSelection.getId()!=null){
				SiteSelection saveSiteSelection = (SiteSelection) siteSelectionManager.getObject(siteSelection.getId());
				saveSiteSelection.setReal_name(siteSelection.getReal_name());
				saveSiteSelection.setMobilephone(siteSelection.getMobilephone());
				saveSiteSelection.setEmployee_flag(siteSelection.getEmployee_flag());
				saveSiteSelection.setSelection_flag(siteSelection.getSelection_flag());
				saveSiteSelection.setCards(siteSelection.getCards());
				saveSiteSelection.setCards_back(siteSelection.getCards_back());
				saveSiteSelection.setSelection_status(3L);
				siteSelectionManager.saveSiteSelection(saveSiteSelection);
				result.setCode(CodeEnum.success.getValue());
		        result.setMessage(CodeEnum.success.getDescription());
		        
		        selectionDto.setId(siteSelection.getId());
		        selectionDto.setReal_name(siteSelection.getReal_name());
		        selectionDto.setMobilephone(siteSelection.getMobilephone());
		        selectionDto.setEmployee_flag(siteSelection.getEmployee_flag());
		        selectionDto.setSelection_flag(siteSelection.getSelection_flag());
		        
		        if(saveSiteSelection.getCards()!=null&&saveSiteSelection.getCards().length()>0){
		        	selectionDto.setCards(getFileRoot()+"upload_folder"+File.separator+"siteselection"+File.separator+saveSiteSelection.getCards());
		        }
		        if(saveSiteSelection.getCards_back()!=null&&saveSiteSelection.getCards_back().length()>0){
		        	selectionDto.setCards_back(getFileRoot()+"upload_folder"+File.separator+"siteselection"+File.separator+saveSiteSelection.getCards_back());
		        }
		        selectionDto.setSelection_status(siteSelection.getSelection_status());
			}else{
				result.setCode(CodeEnum.error.getValue());
		        result.setMessage(CodeEnum.error.getDescription());
			}
	        result.setData(selectionDto);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(CodeEnum.error.getValue());
	        result.setMessage(CodeEnum.error.getDescription());
		}
		return result;
	}
	
	//接口 发送验证码
	@Override
	public Result sendMessage(String mobilephone){
		SendMessageManager sendMessageManager = (SendMessageManager) SpringHelper.getBean("sendMessageManager");
		Result result = new Result();
		if(mobilephone!=null&&mobilephone.length()>0){
			//如果是合法手机号
			if(PhoneFormatCheckUtils.isPhoneLegal(mobilephone)){
				SendMessage sendMessage = new SendMessage();
				//随机生成四位数
				//String code = "您的验证码是"+randomcode()+"，十分钟内有效。";
				String code = randomcode();
				String sendcode = "选铺小程序的验证码是"+code+"，十分钟内有效。";
				String rt="";
				try {
					String sendcode_gb2312 = URLEncoder.encode(sendcode,"utf8");
					System.out.println(sendcode_gb2312);
					//您的验证码是111111，十分钟内有效。
					//String url = "http://q.hl95.com:8061/?username=gasjyz&password=Gasj0121&message="+sendcode_gb2312+"&phone="+mobilephone+"&epid=123743&linkid=&subcode=";
					String url = "https://datatest.guoanshequ.top/eprj/dispatcher.action?phone="+mobilephone+"&sendcode="+sendcode_gb2312;
					System.out.println(url);
					rt = get(url);
					if(rt==null||rt==""){
						try{
						    Thread thread = Thread.currentThread();
						    thread.sleep(5000);//暂停1.5秒后程序继续执行
						}catch (InterruptedException e) {
						    e.printStackTrace();
						}
						rt = get(url);
					}
					
					if(rt==null||rt==""){
						try{
						    Thread thread = Thread.currentThread();
						    thread.sleep(5000);//暂停1.5秒后程序继续执行
						}catch (InterruptedException e) {
						    e.printStackTrace();
						}
						rt = get(url);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				sendMessage.setFunctionname("微信程序登录验证码");
				sendMessage.setMobilephone(mobilephone);
				sendMessage.setCode(code);
				sendMessage.setRcvmessage(rt);
				sendMessage.setMsgstatus(0L);//未使用 
				sendMessageManager.saveSendMessage(sendMessage);
				result.setCode(CodeEnum.success.getValue());
		        result.setMessage(CodeEnum.success.getDescription());
			}else{
				result.setCode(CodeEnum.illegalPhone.getValue());
		        result.setMessage(CodeEnum.illegalPhone.getDescription());
			}
		}else{
			result.setCode(CodeEnum.error.getValue());
	        result.setMessage(CodeEnum.error.getDescription());
		}
		return result;
	}
	
	//接口 用户登录
	@Override
	public Result login(SiteSelection siteSelection){
		Result result = new Result();
		//根据短信和电话号去查询是否10分钟内存在 短信数据
		SendMessageManager sendMessageManager = (SendMessageManager) SpringHelper.getBean("sendMessageManager");
		SiteSelectionManager siteSelectionManager = (SiteSelectionManager) SpringHelper.getBean("siteSelectionManager");
		Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -10);
        String tenMin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
        FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("create_time",ISort.DESC));
        IFilter messageFilter =FilterFactory.getSimpleFilter("functionname='微信程序登录验证码' and mobilephone='"+siteSelection.getMobilephone()+"' and create_time>'"+tenMin+"'");
        fsp.setUserFilter(messageFilter);
        List<SendMessage> sendMessages = (List<SendMessage>) sendMessageManager.getList(fsp);
		if(sendMessages!=null&&sendMessages.size()>0){
			SendMessage sendMessage = sendMessages.get(0);
			if(siteSelection.getPassword()!=null&&sendMessage.getMsgstatus().equals(0L)&&sendMessage.getCode().equals(siteSelection.getPassword())){
				sendMessage.setMsgstatus(1L);
				sendMessageManager.saveSendMessage(sendMessage);
				
				//登录成功后。保存用户信息 并返回 
				IFilter loginFilter =FilterFactory.getSimpleFilter("mobilephone='"+siteSelection.getMobilephone()+"'");
				List<SiteSelection> lst = (List<SiteSelection>) siteSelectionManager.getList(loginFilter);
				SiteSelection selection = null;
				if(lst!=null&&lst.size()>0){//之前登录过
					selection = lst.get(0);
				}else{//新用户注册 
					selection = new SiteSelection();
					selection.setMobilephone(siteSelection.getMobilephone());
					selection.setSelection_status(0L);
					siteSelectionManager.saveSiteSelection(selection);
				}
				result.setCode(CodeEnum.success.getValue());
		        result.setMessage(CodeEnum.success.getDescription());
		        
		        //更新验证码状态 
		        
		        
		        SiteSelectionDto selectionDto = new SiteSelectionDto();
		        BeanUtils.copyProperties(selection, selectionDto);
		        result.setData(selectionDto);
			}else{
				result.setCode(CodeEnum.error.getValue());
				result.setMessage("验证码错误");
			}
		}else{
			result.setCode(CodeEnum.error.getValue());
	        result.setMessage("用户名/验证码错误");
		}
   	    return result;
	}
	
	
	
	
	//接口 店铺需求列表
	@Override
	public Result queryStoreRequirementList(){
		Result result = new Result();
		try {
			StoreRequirementManager storeRequirementManager = (StoreRequirementManager) SpringHelper.getBean("storeRequirementManager");
			IFilter storeRequirmentFilter =FilterFactory.getSimpleFilter("require_status!=3");
			List<StoreRequirement> storeRequirements = (List<StoreRequirement>) storeRequirementManager.getList(storeRequirmentFilter);
	        List<StoreRequirementDto> storeRequirementDtos = new ArrayList<StoreRequirementDto>();
	        if(storeRequirements!=null&&storeRequirements.size()>0){
	        	for(StoreRequirement sr : storeRequirements){
	        		StoreRequirementDto srdto = new StoreRequirementDto();
	        		srdto.setId(sr.getId());
	        		srdto.setStore_name(sr.getStore_name());
	        		srdto.setBonus(sr.getBonus());
	        		srdto.setArea(sr.getArea());
	        		srdto.setRequirement(sr.getRequirement());
	        		srdto.setAddress(sr.getAddress());
	        		srdto.setRequire_status(sr.getRequire_status());
	        		srdto.setStore_standard_id(sr.getStore_standard_id());
	        		storeRequirementDtos.add(srdto);
	        	}
	        	result.setCode(CodeEnum.success.getValue());
		        result.setMessage(CodeEnum.success.getDescription());
	        }else{
	        	result.setCode(CodeEnum.nullData.getValue());
		        result.setMessage(CodeEnum.nullData.getDescription());
	        }
	        result.setData(storeRequirementDtos);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(CodeEnum.error.getValue());
	        result.setMessage(CodeEnum.error.getDescription());
	        result.setData(null);
		}
		return result;
	}
	
	
	
	
	//接口 根据ID查询店铺需求明细 
	@Override
	public Result queryStoreRequiremenById(Long id){
		Result result = new Result();
		try {
			StoreRequirementManager storeRequirementManager = (StoreRequirementManager) SpringHelper.getBean("storeRequirementManager");
			StoreRequirement sr = (StoreRequirement) storeRequirementManager.getObject(id);
			StoreRequirementDto srdto = null;
			if(sr!=null){
				srdto = new StoreRequirementDto();
				srdto.setId(sr.getId());
        		srdto.setStore_name(sr.getStore_name());
        		srdto.setBonus(sr.getBonus());
        		srdto.setArea(sr.getArea());
        		srdto.setRequirement(sr.getRequirement());
        		srdto.setAddress(sr.getAddress());
        		srdto.setRequire_status(sr.getRequire_status());
        		srdto.setStore_standard_id(sr.getStore_standard_id());
	        	result.setCode(CodeEnum.success.getValue());
		        result.setMessage(CodeEnum.success.getDescription());
	        }else{
	        	result.setCode(CodeEnum.nullData.getValue());
		        result.setMessage(CodeEnum.nullData.getDescription());
	        }
	        result.setData(srdto);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(CodeEnum.error.getValue());
	        result.setMessage(CodeEnum.error.getDescription());
	        result.setData(null);
		}
		return result;
	}
		
		
	
	
	//接口 立地标准列表
	@Override
	public Result queryStoreStandardList(){
		Result result = new Result();
		try {
			StoreStandardManager storeStandardManager = (StoreStandardManager) SpringHelper.getBean("storeStandardManager");
			List<StoreStandard> storeStandards = (List<StoreStandard>) storeStandardManager.getList();
			result.setCode(CodeEnum.success.getValue());
	        result.setMessage(CodeEnum.success.getDescription());
	        result.setData(storeStandards);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(CodeEnum.error.getValue());
	        result.setMessage(CodeEnum.error.getDescription());
	        result.setData(null);
		}
		return result;
	}

	//接口  查询立地标准明细
	@Override
	public Result queryStoreStandardById(Long id){
		Result result = new Result();
		try {
			StoreStandardManager storeStandardManager = (StoreStandardManager) SpringHelper.getBean("storeStandardManager");
			StoreStandard ss = (StoreStandard) storeStandardManager.getObject(id);
			StoreStandardDto storeStandardDto = null;
			if(ss!=null){
				storeStandardDto = new StoreStandardDto();
				result.setCode(CodeEnum.success.getValue());
		        result.setMessage(CodeEnum.success.getDescription());
		        storeStandardDto.setRent(ss.getRent());
		        storeStandardDto.setProperty(ss.getProperty());
		        storeStandardDto.setArea(ss.getArea());
		        storeStandardDto.setType(ss.getType());
		        storeStandardDto.setFloor(ss.getFloor());
		        storeStandardDto.setElectric(ss.getElectric());
		        storeStandardDto.setSidestation(ss.getSidestation());
		        storeStandardDto.setWater(ss.getWater());
		        storeStandardDto.setBlowdown(ss.getBlowdown());
		        storeStandardDto.setFirecontrol(ss.getFirecontrol());
		        storeStandardDto.setDesignpaper(ss.getDesignpaper());
		        storeStandardDto.setLamphouse(ss.getLamphouse());
		        storeStandardDto.setFloorheight(ss.getFloorheight());
		        storeStandardDto.setLeasetime(ss.getLeasetime());
		        storeStandardDto.setAddress(ss.getAddress());
		        storeStandardDto.setNetwork(ss.getNetwork());
		        storeStandardDto.setTocondition(ss.getTocondition());
			}else{
				result.setCode(CodeEnum.nullData.getValue());
		        result.setMessage(CodeEnum.nullData.getDescription());
			}
			result.setData(storeStandardDto);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(CodeEnum.error.getValue());
	        result.setMessage(CodeEnum.error.getDescription());
	        result.setData(null);
		}
		return result;
	}
		
		
	
	//接口 保存房源信息 
	@Override
	public Result saveStoreAddress(StoreAddress storeAddress){
		Result result = new Result();
		try {
			StoreAddressManager storeAddressManager = (StoreAddressManager) SpringHelper.getBean("storeAddressManager");
			//处理图片 店铺正面近景照片
			if(storeAddress.getStore_pic()!=null&&storeAddress.getStore_pic().length()>0){
				storeAddress.setStore_pic(storeAddress.getStore_pic().substring(storeAddress.getStore_pic().length()-36,storeAddress.getStore_pic().length()));
	        }
			//处理图片 店铺右侧街道照片
			if(storeAddress.getStore_leftpic()!=null&&storeAddress.getStore_leftpic().length()>0){
				storeAddress.setStore_leftpic(storeAddress.getStore_leftpic().substring(storeAddress.getStore_leftpic().length()-36,storeAddress.getStore_leftpic().length()));
	        }
			//处理图片店铺左侧街道照片 
			if(storeAddress.getStore_rightpic()!=null&&storeAddress.getStore_rightpic().length()>0){
				storeAddress.setStore_rightpic(storeAddress.getStore_rightpic().substring(storeAddress.getStore_rightpic().length()-36,storeAddress.getStore_rightpic().length()));
	        }
			//处理图片 店铺对面照片
			if(storeAddress.getStore_oppositepic()!=null&&storeAddress.getStore_oppositepic().length()>0){
				storeAddress.setStore_oppositepic(storeAddress.getStore_oppositepic().substring(storeAddress.getStore_oppositepic().length()-36,storeAddress.getStore_oppositepic().length()));
	        }
			
			//处理图片 室内图片1
			if(storeAddress.getStore_pic1()!=null&&storeAddress.getStore_pic1().length()>0){
				storeAddress.setStore_pic1(storeAddress.getStore_pic1().substring(storeAddress.getStore_pic1().length()-36,storeAddress.getStore_pic1().length()));
	        }
			//处理图片 室内图片2
			if(storeAddress.getStore_pic2()!=null&&storeAddress.getStore_pic2().length()>0){
				storeAddress.setStore_pic2(storeAddress.getStore_pic2().substring(storeAddress.getStore_pic2().length()-36,storeAddress.getStore_pic2().length()));
	        }
			//处理图片 室内图片3
			if(storeAddress.getStore_pic3()!=null&&storeAddress.getStore_pic3().length()>0){
				storeAddress.setStore_pic3(storeAddress.getStore_pic3().substring(storeAddress.getStore_pic3().length()-36,storeAddress.getStore_pic3().length()));
	        }
			//处理图片 室内图片4
			if(storeAddress.getStore_pic4()!=null&&storeAddress.getStore_pic4().length()>0){
				storeAddress.setStore_pic4(storeAddress.getStore_pic4().substring(storeAddress.getStore_pic4().length()-36,storeAddress.getStore_pic4().length()));
	        }
			//处理图片 室内图片5
			if(storeAddress.getStore_pic5()!=null&&storeAddress.getStore_pic5().length()>0){
				storeAddress.setStore_pic5(storeAddress.getStore_pic5().substring(storeAddress.getStore_pic5().length()-36,storeAddress.getStore_pic5().length()));
	        }
			
			storeAddressManager.saveStoreAddress(storeAddress);
		    
			result.setCode(CodeEnum.success.getValue());
	        result.setMessage(CodeEnum.success.getDescription());
	        
	        //构建 storeaddressdto
	        StoreAddressDto storeAddressDto = new StoreAddressDto();
	        BeanUtils.copyProperties(storeAddress, storeAddressDto);
	        
	        String picroot = getFileRoot()+"upload_folder"+File.separator+"siteselection"+File.separator;
	        if(storeAddress.getStore_pic()!=null&&storeAddress.getStore_pic().length()>0){
	        	storeAddressDto.setStore_pic(picroot+storeAddress.getStore_pic());
	        }
	        if(storeAddress.getStore_leftpic()!=null&&storeAddress.getStore_leftpic().length()>0){
	        	storeAddressDto.setStore_leftpic(picroot+storeAddress.getStore_leftpic());
	        }
	        if(storeAddress.getStore_rightpic()!=null&&storeAddress.getStore_rightpic().length()>0){
	        	storeAddressDto.setStore_rightpic(picroot+storeAddress.getStore_rightpic());
	        }
	        if(storeAddress.getStore_oppositepic()!=null&&storeAddress.getStore_oppositepic().length()>0){
	        	storeAddressDto.setStore_oppositepic(picroot+storeAddress.getStore_oppositepic());
	        }
	        
	        if(storeAddress.getStore_pic1()!=null&&storeAddress.getStore_pic1().length()>0){
	        	storeAddressDto.setStore_pic1(picroot+storeAddress.getStore_pic1());
	        }
	        if(storeAddress.getStore_pic2()!=null&&storeAddress.getStore_pic2().length()>0){
	        	storeAddressDto.setStore_pic2(picroot+storeAddress.getStore_pic2());
	        }
	        if(storeAddress.getStore_pic3()!=null&&storeAddress.getStore_pic3().length()>0){
	        	storeAddressDto.setStore_pic3(picroot+storeAddress.getStore_pic3());
	        }
	        if(storeAddress.getStore_pic4()!=null&&storeAddress.getStore_pic4().length()>0){
	        	storeAddressDto.setStore_pic4(picroot+storeAddress.getStore_pic4());
	        }
	        if(storeAddress.getStore_pic5()!=null&&storeAddress.getStore_pic5().length()>0){
	        	storeAddressDto.setStore_pic5(picroot+storeAddress.getStore_pic5());
	        }
	        
	        result.setData(storeAddressDto);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(CodeEnum.error.getValue());
	        result.setMessage(CodeEnum.error.getDescription());
	        result.setData(null);
		}
		
		return result;
	}
	
	
	
	//接口 房源信息列表 根据ID
	@Override
	public Result queryStoreAddressList(Long siteSelectionId){
		Result result = new Result();
		try {
			StoreAddressManager storeAddressManager = (StoreAddressManager) SpringHelper.getBean("storeAddressManager");
			
			IFilter siteSelectionFilter =FilterFactory.getSimpleFilter("site_selection_id="+siteSelectionId+"");
			/*FSP fsp = new FSP();
			fsp.setSort(SortFactory.createSort("id",ISort.DESC));
			fsp.setUserFilter(siteSelectionFilter);*/
			List<Map<String, Object>> storeAddresses = (List<Map<String, Object>>) storeAddressManager.queryStoreAddressList(siteSelectionId);
			List<StoreReqAddressDto> storeReqAddressDtos = null;
			if(storeAddresses!=null&&storeAddresses.size()>0){
				result.setCode(CodeEnum.success.getValue());
		        result.setMessage(CodeEnum.success.getDescription());
		        storeReqAddressDtos = new ArrayList<StoreReqAddressDto>();
		        for(Map<String, Object> sa:storeAddresses){
		        	StoreReqAddressDto storeReqAddressDto = new StoreReqAddressDto();
		        	storeReqAddressDto.setId(Long.parseLong(sa.get("id").toString()));
		        	storeReqAddressDto.setStore_name(sa.get("store_name")==null?"":sa.get("store_name").toString());
		        	storeReqAddressDto.setStore_address(sa.get("store_address")==null?"":sa.get("store_address").toString());
		        	storeReqAddressDto.setStore_rent(Double.parseDouble(sa.get("store_rent")==null?"0":sa.get("store_rent").toString()));
		        	storeReqAddressDto.setTotal_area(sa.get("total_area")==null?"":sa.get("total_area").toString());
		        	storeReqAddressDto.setCreate_time(sa.get("create_time").toString());
		        	storeReqAddressDto.setBonus(sa.get("bonus")==null?"":sa.get("bonus").toString());
		        	storeReqAddressDto.setRequire_status(Long.parseLong(sa.get("storestatus").toString()));
		        	storeReqAddressDtos.add(storeReqAddressDto);
		        }
			}else{
				result.setCode(CodeEnum.nullData.getValue());
		        result.setMessage(CodeEnum.nullData.getDescription());
			}
	        result.setData(storeReqAddressDtos);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(CodeEnum.error.getValue());
	        result.setMessage(CodeEnum.error.getDescription());
	        result.setData(null);
		}
		return result;
	}
	
	
	
	//接口 查询房源信息明细 根据ID
	@Override
	public Result queryStoreAddressById(Long id){
		Result result = new Result();
		StoreAddressManager storeAddressManager = (StoreAddressManager) SpringHelper.getBean("storeAddressManager");
		StoreAddress storeAddress = (StoreAddress) storeAddressManager.getObject(id);
		StoreAddressDto storeAddressDto = null;
		if(storeAddress!=null){
			storeAddressDto = new StoreAddressDto();
			result.setCode(CodeEnum.success.getValue());
	        result.setMessage(CodeEnum.success.getDescription());
	        BeanUtils.copyProperties(storeAddress, storeAddressDto);
	        
	        
	        String picroot = getFileRoot()+"upload_folder"+File.separator+"siteselection"+File.separator;
	        if(storeAddress.getStore_pic()!=null&&storeAddress.getStore_pic().length()>0){
	        	storeAddressDto.setStore_pic(picroot+storeAddress.getStore_pic());
	        }
	        if(storeAddress.getStore_leftpic()!=null&&storeAddress.getStore_leftpic().length()>0){
	        	storeAddressDto.setStore_leftpic(picroot+storeAddress.getStore_leftpic());
	        }
	        if(storeAddress.getStore_rightpic()!=null&&storeAddress.getStore_rightpic().length()>0){
	        	storeAddressDto.setStore_rightpic(picroot+storeAddress.getStore_rightpic());
	        }
	        if(storeAddress.getStore_oppositepic()!=null&&storeAddress.getStore_oppositepic().length()>0){
	        	storeAddressDto.setStore_oppositepic(picroot+storeAddress.getStore_oppositepic());
	        }
	        if(storeAddress.getStore_pic1()!=null&&storeAddress.getStore_pic1().length()>0){
	        	storeAddressDto.setStore_pic1(picroot+storeAddress.getStore_pic1());
	        }
	        if(storeAddress.getStore_pic2()!=null&&storeAddress.getStore_pic2().length()>0){
	        	storeAddressDto.setStore_pic2(picroot+storeAddress.getStore_pic2());
	        }
	        if(storeAddress.getStore_pic3()!=null&&storeAddress.getStore_pic3().length()>0){
	        	storeAddressDto.setStore_pic3(picroot+storeAddress.getStore_pic3());
	        }
	        if(storeAddress.getStore_pic4()!=null&&storeAddress.getStore_pic4().length()>0){
	        	storeAddressDto.setStore_pic4(picroot+storeAddress.getStore_pic4());
	        }
	        if(storeAddress.getStore_pic5()!=null&&storeAddress.getStore_pic5().length()>0){
	        	storeAddressDto.setStore_pic5(picroot+storeAddress.getStore_pic5());
	        }
		}else{
			result.setCode(CodeEnum.nullData.getValue());
	        result.setMessage(CodeEnum.nullData.getDescription());
		}
		result.setData(storeAddressDto);
		return result;
	}
	
	
	//接口 微信小程序banner图片
	@Override
	public Result queryBannerInfoList(){
		Result result = new Result();
		try {
			BannerInfoManager bannerInfoManager = (BannerInfoManager) SpringHelper.getBean("bannerInfoManager");
			IFilter iFilter =FilterFactory.getSimpleFilter("function_name=1");			
			List<BannerInfo> bannerInfoList = (List<BannerInfo>) bannerInfoManager.getList(iFilter);
			if(bannerInfoList!=null&&bannerInfoList.size()>0){
				List<BannerInfoDto> dtos = new ArrayList<BannerInfoDto>();
				for(BannerInfo bi:bannerInfoList){
					BannerInfoDto bidto = new BannerInfoDto();
					bidto.setFunction_name(bi.getFunction_name());
					bidto.setOrdernum(bi.getOrdernum());
					bidto.setOpen_url(bi.getOpen_url());
					String picroot = getFileRoot()+"upload_folder"+File.separator+"banner"+File.separator;
			        if(bi.getBanner_url()!=null&&bi.getBanner_url().length()>0){
			        	bidto.setBanner_url(picroot+bi.getBanner_url());
			        }	
			        dtos.add(bidto);
				}
				result.setCode(CodeEnum.success.getValue());
		        result.setMessage(CodeEnum.success.getDescription());
		        result.setData(dtos);
			}else{
				result.setCode(CodeEnum.nullData.getValue());
		        result.setMessage(CodeEnum.nullData.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(CodeEnum.error.getValue());
	        result.setMessage(CodeEnum.error.getDescription());
		}
		return result;
	}
	
	
	//接口 保存补充信息
	@Override
	public Result updateStoreAddress(StoreAddress storeAddress){
		Result result = new Result();
		try {
			StoreAddressManager storeAddressManager = (StoreAddressManager) SpringHelper.getBean("storeAddressManager");
			StoreAddress updateStoreAddress = (StoreAddress) storeAddressManager.getObject(storeAddress.getId());
			if(updateStoreAddress!=null){
				updateStoreAddress.setAdditional_remarks(storeAddress.getAdditional_remarks());
				
				storeAddressManager.saveStoreAddress(updateStoreAddress);
				result.setCode(CodeEnum.success.getValue());
		        result.setMessage(CodeEnum.success.getDescription());
		        //构建 storeaddressdto
		        StoreAddressDto storeAddressDto = new StoreAddressDto();
		        BeanUtils.copyProperties(updateStoreAddress, storeAddressDto);
		        String picroot = getFileRoot()+"upload_folder"+File.separator+"siteselection"+File.separator;
		        if(storeAddress.getStore_pic()!=null&&storeAddress.getStore_pic().length()>0){
		        	storeAddressDto.setStore_pic(picroot+storeAddress.getStore_pic());
		        }
		        if(storeAddress.getStore_leftpic()!=null&&storeAddress.getStore_leftpic().length()>0){
		        	storeAddressDto.setStore_leftpic(picroot+storeAddress.getStore_leftpic());
		        }
		        if(storeAddress.getStore_rightpic()!=null&&storeAddress.getStore_rightpic().length()>0){
		        	storeAddressDto.setStore_rightpic(picroot+storeAddress.getStore_rightpic());
		        }
		        if(storeAddress.getStore_oppositepic()!=null&&storeAddress.getStore_oppositepic().length()>0){
		        	storeAddressDto.setStore_oppositepic(picroot+storeAddress.getStore_oppositepic());
		        }
		        
		        if(storeAddress.getStore_pic1()!=null&&storeAddress.getStore_pic1().length()>0){
		        	storeAddressDto.setStore_pic1(picroot+storeAddress.getStore_pic1());
		        }
		        if(storeAddress.getStore_pic2()!=null&&storeAddress.getStore_pic2().length()>0){
		        	storeAddressDto.setStore_pic2(picroot+storeAddress.getStore_pic2());
		        }
		        if(storeAddress.getStore_pic3()!=null&&storeAddress.getStore_pic3().length()>0){
		        	storeAddressDto.setStore_pic3(picroot+storeAddress.getStore_pic3());
		        }
		        if(storeAddress.getStore_pic4()!=null&&storeAddress.getStore_pic4().length()>0){
		        	storeAddressDto.setStore_pic4(picroot+storeAddress.getStore_pic4());
		        }
		        if(storeAddress.getStore_pic5()!=null&&storeAddress.getStore_pic5().length()>0){
		        	storeAddressDto.setStore_pic5(picroot+storeAddress.getStore_pic5());
		        }
		        
		        result.setData(storeAddressDto);
			}else{
				result.setCode(CodeEnum.nullData.getValue());
		        result.setMessage(CodeEnum.nullData.getDescription());
		        result.setData(null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(CodeEnum.error.getValue());
	        result.setMessage(CodeEnum.error.getDescription());
	        result.setData(null);
		}
		
		return result;
	}
		
	
	public String randomcode(){
		String str = "0123456789";
		StringBuilder sb = new StringBuilder(4);
		for (int i = 0; i < 4; i++) {
			char ch = str.charAt(new Random().nextInt(str.length()));
			sb.append(ch);
		}
		return sb.toString();
	}
	
	public String getFileRoot() {
        return PropertiesUtil.getValue("file.web.root");
    }
	
	
	public String get(String url) {  
        BufferedReader in = null;  
        try {  
            URL realUrl = new URL(url);  
            InetSocketAddress addr = new InetSocketAddress("10.0.1.11",3128);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理 
            // 打开和URL之间的连接  
            URLConnection connection = realUrl.openConnection(proxy);  
            // 设置通用的请求属性  
            connection.setRequestProperty("accept", "*/*");  
            connection.setRequestProperty("connection", "Keep-Alive");  
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
            connection.setConnectTimeout(5000);  
            connection.setReadTimeout(5000);  
            // 建立实际的连接  
            connection.connect();  
            // 定义 BufferedReader输入流来读取URL的响应  
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));  
            StringBuffer sb = new StringBuffer();  
            String line;  
            while ((line = in.readLine()) != null) {  
                sb.append(line);  
            }  
            return sb.toString();  
        } catch (Exception e) {  
            e.printStackTrace();
        }  
        // 使用finally块来关闭输入流  
        finally {  
            try {  
                if (in != null) {  
                    in.close();  
                }  
            } catch (Exception e2) {  
                e2.printStackTrace();  
            }  
        }  
        return null;  
    }
	
	
	public String utf8Togb2312(String str){   
        StringBuffer sb = new StringBuffer();   
        for ( int i=0; i<str.length(); i++) {   
            char c = str.charAt(i);   
            switch (c) {   
               case '+' :   
                   sb.append( ' ' );   
               break ;   
               case '%' :   
                   try {   
                        sb.append(( char )Integer.parseInt (   
                        str.substring(i+1,i+3),16));   
                   }   
                   catch (NumberFormatException e) {   
                       throw new IllegalArgumentException();   
                  }   
                  i += 2;   
                  break ;   
               default :   
                  sb.append(c);   
                  break ;   
             }   
        }   
        String result = sb.toString();   
        String res= null ;   
        try {   
             byte [] inputBytes = result.getBytes( "8859_1" );   
            res= new String(inputBytes, "UTF-8" );   
        }   
        catch (Exception e){}   
        return res;   
  } 


	@Override
	public Result queryDailyTurnover(String cityId) {
		Result result = new Result();
		DynamicDao dynamicDao = (DynamicDao) SpringHelper.getBean(DynamicDao.class.getName());
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		Map<String, Object> r_map = null;
		List<Map<String,Object>> cityNo = new ArrayList<Map<String,Object>>();
		Map<String,Object> teMap = new HashMap<String,Object>();
    	try {
    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    		Calendar cale = null;
    		cale = Calendar.getInstance();
    		cale.add(Calendar.MONTH, 0);
    		cale.set(Calendar.DAY_OF_MONTH, 1);
    		String beginDate = format.format(cale.getTime());
    		cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 1);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            String endDate = DateUtil.curDate();
    		String lastDate = DateUtils.lastDate();
    		if(cityId!=null){
    			cityNo = storeDao.getCityNOOfCityById(Long.parseLong(cityId));
    		}
    		r_map = dynamicDao.queryTurnoverByTime(String.valueOf(cityNo.get(0).get("cityno")),beginDate,endDate);
//    		r_map = dynamicDao.queryTurnoverByTime(String.valueOf(cityNo.get(0).get("cityno")),"2017-12-04","2017-12-10");
    		List<Map<String,Object>> lst_data = (List<Map<String, Object>>) r_map.get("lst_data");
    		Date dateOne = format.parse(beginDate);
    		Date dateTwo = format.parse(lastDate);
//    		Date dateOne = format.parse("2017-12-04");
//            Date dateTwo = format.parse("2017-12-10");
    		Calendar calendar = Calendar.getInstance();
            
            calendar.setTime(dateOne);
             
            while(calendar.getTime().before(format.parse(getSpecifiedDayAfter(format.format(dateTwo))))){
            	String timeStr = format.format(calendar.getTime());
            	String dayStr = timeStr.substring(timeStr.lastIndexOf("-")+1,timeStr.length());
            	if("0".equals(dayStr.subSequence(0, 1))){
            		dayStr = dayStr.substring(1,dayStr.length());
            	}
            	Map<String,Object> lst_map = new HashMap<String, Object>();
            	lst_map.put("week_date", dayStr);
				lst_map.put("checked_order_count", 0);
				lst_map.put("total_order_count", 0);
				lst_map.put("storecur_all_price", 0);
				lst_map.put("total_quantity", 0);
				lst_map.put("df_signed_time", null);
				for(int i=0;i<lst_data.size();i++){
	    			Map<String,Object> lst_map_week = lst_data.get(i);
	    			String dateStr = String.valueOf(lst_map_week.get("week_date"));
	    			if(dayStr.equals(dateStr)){
	    				lst_data.remove(i);
	    				lst_map.put("week_date", lst_map_week.get("week_date"));
	    				lst_map.put("checked_order_count", lst_map_week.get("checked_order_count"));
	    				lst_map.put("total_order_count", lst_map_week.get("total_order_count"));
	    				lst_map.put("storecur_all_price", lst_map_week.get("storecur_all_price"));
	    				lst_map.put("total_quantity", lst_map_week.get("total_quantity"));
	    				lst_map.put("df_signed_time", lst_map_week.get("df_signed_time"));
	    			}
	    			
				}
				lst_data.add(lst_map);
                calendar.add(Calendar.DAY_OF_MONTH, 1);               
            }
    		teMap.put("lst_data", r_map.get("lst_data"));
    		teMap.put("max_price", r_map.get("max_price"));
    		result.setData(teMap);
    	} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
	}

	@Override
	public Result queryActualMothlyTurnover(DynamicDto dd) {
		Result result = new Result();
		Map<String,Object> map_data = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Double monthHistory = 0d;
		Double dailySum = 0d;
		Double totalSum = 0d;
		Long city_id = dd.getCityId();
		String province_id = dd.getProvinceId();
		List<Map<String, Object>> cityNO = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> provinceNO = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> dailyStoreOrderList = new ArrayList<Map<String,Object>>();
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		Calendar a=Calendar.getInstance();
		String month = (a.get(Calendar.MONTH)+1)<10?("0"+(a.get(Calendar.MONTH)+1)):((a.get(Calendar.MONTH)+1)+"");
		String beginDate = a.get(Calendar.YEAR)+"-"+month+"-"+(a.get(Calendar.DATE)<10?("0"+a.get(Calendar.DATE)):a.get(Calendar.DATE));   
		String endDate = a.get(Calendar.YEAR)+"-"+month+"-"+(a.get(Calendar.DATE)<10?("0"+a.get(Calendar.DATE)):a.get(Calendar.DATE));
		dd.setYear(a.get(Calendar.YEAR));
		dd.setBeginDate(beginDate);
		dd.setEndDate(endDate);
		dd.setMonth(a.get(Calendar.MONTH)+1);
//		dd.setBeginDate("2017-12-13");
//		dd.setEndDate("2017-12-13");
//		dd.setMonth(1);
		Map<String,Object> monthResult = dynamicDao.queryTradeSumByMonth(dd);
		if (!monthResult.isEmpty()) {//当月累计营业额
			monthHistory =Double.valueOf(String.valueOf(monthResult.get("cur_order_amount")));
		}
		if(city_id!=null){
			cityNO = storeDao.getCityNOOfCityById(city_id);
		}
		if(province_id!=null&&province_id!=""){
			provinceNO = storeDao.getProvinceNOOfCSZJ(province_id);
		}
		//当日实时累计营业额
		dailyStoreOrderList = orderDao.getDailyStoreOrderOfCurDay(dd,cityNO,provinceNO);
		dailySum = Double.valueOf(String.valueOf(dailyStoreOrderList.get(0).get("storecur_all_price")));
		totalSum = monthHistory+dailySum;
		map_data.put("history_week_amount", totalSum);
		result.setDataMap(map_data);
		result.setCode(CodeEnum.success.getValue()); 
        result.setMessage(CodeEnum.success.getDescription());
		return result;
	}
	@Override
	public Result queryTradeDepMonthlyGMV(DynamicDto dynamicDto) {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Map<String,Object> map_data = new HashMap<String,Object>();
		Result result = new Result();
		Map<String,Object> dynamicMap = null;
		Calendar a=Calendar.getInstance();
		dynamicDto.setYear(a.get(Calendar.YEAR));
		dynamicDto.setMonth(a.get(Calendar.MONTH)+1);
//		dynamicDto.setMonth(1);
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurrentPage(1);
			pageInfo.setRecordsPerPage(5);
			dynamicMap = dynamicDao.queryTradeByDepName(dynamicDto,pageInfo);
		}catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map_result = new HashMap<String, Object>();
		List<Map<String, Object>> gmvList = (List<Map<String, Object>>)dynamicMap.get("gmv");
		if(gmvList!=null&&gmvList.size()>0){
			map_data.put("trade_dep_monthly_amount", dynamicMap);
		}else{
			List<Map<String, Object>> allDeptList = dynamicDao.queryCityAllDept();
			map_result.put("gmv", allDeptList);
			map_data.put("trade_dep_monthly_amount", map_result);
		}
		result.setDataMap(map_data);
		result.setCode(CodeEnum.success.getValue()); 
        result.setMessage(CodeEnum.success.getDescription());
		return result;
	}

	@Override
	public Result queryTradeChannelGMV(DynamicDto dynamicDto) {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Map<String,Object> map_data = new HashMap<String,Object>();
		Result result = new Result();
		Map<String,Object> dynamicMap = null;
		Calendar a=Calendar.getInstance();
		dynamicDto.setYear(a.get(Calendar.YEAR));
//		dynamicDto.setMonth(1);
		dynamicDto.setMonth(a.get(Calendar.MONTH)+1);
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurrentPage(1);
			pageInfo.setRecordsPerPage(5);
			dynamicMap = dynamicDao.queryTradeByChannelName(dynamicDto,pageInfo);
		}catch (Exception e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> gmvList = (List<Map<String, Object>>)dynamicMap.get("gmv");
		if(gmvList!=null&&gmvList.size()>0){
			map_data.put("trade_channel_monthly_amount", dynamicMap);
		}else{
			Map<String, Object> map_str = new HashMap<String, Object>();
			map_str.put("order_amount", 0);
			map_str.put("channel_name", "");
			List<Map<String, Object>> gmv = new ArrayList<Map<String,Object>>();
			gmv.add(map_str);
			Map<String, Object> map_result = new HashMap<String, Object>();
			map_result.put("gmv", gmv);
			map_data.put("trade_channel_monthly_amount", map_result);
		}
		result.setDataMap(map_data);
		result.setCode(CodeEnum.success.getValue()); 
        result.setMessage(CodeEnum.success.getDescription());
		return result;
	}

	@Override
	public Result queryCustomerCountForWeek(String cityId) {
		Result result = new Result();
		DynamicDao dynamicDao = (DynamicDao) SpringHelper.getBean(DynamicDao.class.getName());
		Map<String, Object> r_map = null;
		Map<String,Object> teMap = new HashMap<String,Object>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		DynamicDto dynamicDto = new DynamicDto();
    	try {
    		String beginDate = DateUtils.getCurrentMonday();
    		//String endDate = DateUtils.getPreviousSunday();
    		String curDate = DateUtil.curDate();
    		String endDate = curDate;
//    		String beginDate = "2018-03-19";
//    		String endDate = "2018-03-25";
//    		String curDate = "2018-01-06";
    		if(cityId!=null){
    			dynamicDto.setCityId(Long.parseLong(cityId));
    		}
    		dynamicDto.setBeginDate(beginDate);
    		dynamicDto.setEndDate(endDate);
    		r_map = dynamicDao.queryCustomerCountByTime(dynamicDto);
    		List<Map<String,Object>> lst_data = (List<Map<String, Object>>) r_map.get("lst_data");
    		Calendar dd = Calendar.getInstance();//定义日期实例
    		dd.setTime(format.parse(beginDate));//设置日期起始时间
    		while(dd.getTime().before(format.parse(getSpecifiedDayAfter(endDate)))){//判断是否到结束日期
    			Map<String,Object> lst_map = new HashMap<String, Object>();
    			String timeStr = format.format(dd.getTime());
            	String dayStr = timeStr.substring(timeStr.indexOf("-")+1,timeStr.length());
            	lst_map.put("week_date", dayStr);
            	lst_map.put("month_customer_count", 0);
            	lst_map.put("new_customer_count", 0);
            	for(int i=0;i<lst_data.size();i++){
	    			Map<String,Object> lst_map_week = lst_data.get(i);
	    			String dateStr = String.valueOf(lst_map_week.get("week_date"));
	    			if(dayStr.equals(dateStr)){
	    				lst_data.remove(i);
	    				lst_map.put("week_date", lst_map_week.get("week_date"));
	    				lst_map.put("month_customer_count", lst_map_week.get("month_customer_count"));
	    				lst_map.put("new_customer_count", lst_map_week.get("new_customer_count"));
	    			}
				}
            	lst_data.add(lst_map);
            	dd.add(Calendar.DAY_OF_MONTH, 1); 
    		}
    		teMap.put("lst_data", r_map.get("lst_data"));
    		result.setData(teMap);
    	} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
	}

	@Override
	public Result queryMonthCustomerCount(String cityId) {
		Result result = new Result();
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		List<Map<String,Object>> cityNO = new ArrayList<Map<String,Object>>();
		Map<String,Object> teMap = new HashMap<String,Object>();
		List<Map<String,Object>> dailyUserList = new ArrayList<Map<String,Object>>();
		int monthCustomerCount = 0;
		int dailyCustomerCount = 0;
		DynamicDto dd = new DynamicDto();
    	try {
    		Calendar a=Calendar.getInstance();
    		int month = a.get(Calendar.MONTH)+1;
    		dd.setMonth(month);
    		dd.setYear(a.get(Calendar.YEAR));
    		String beginDate = a.get(Calendar.YEAR)+"-"+month+"-"+(a.get(Calendar.DATE)<10?("0"+a.get(Calendar.DATE)):a.get(Calendar.DATE));   
    		String endDate = a.get(Calendar.YEAR)+"-"+month+"-"+(a.get(Calendar.DATE)<10?("0"+a.get(Calendar.DATE)):a.get(Calendar.DATE));
    		dd.setBeginDate(beginDate);
    		dd.setEndDate(endDate);
    		dd.setCityId(Long.parseLong(cityId));
    		//查询某月用户量
    		if(cityId!=null){
    			cityNO = storeDao.getCityNOOfCityById(Long.parseLong(cityId));
    		}
    		List<Map<String, Object>> customerMonthCountList = dynamicDao.queryMonthCustomerCount(dd);
    		List<Map<String,Object>> dailyStoreOrderList = orderDao.getDailyStoreOrderOfCurDay(dd,cityNO,null);
    		if(dailyStoreOrderList!=null&&dailyStoreOrderList.size()>0){
    			dailyUserList = orderDao.getDailyUserOfCurDay(dd,cityNO,null);
    			//获得当天实时用户量
    			if(dailyUserList!=null&&dailyUserList.size()>0){
    				dailyCustomerCount = Integer.parseInt(String.valueOf(dailyUserList.get(0).get("customer_count")));
    			}else{
    				dailyCustomerCount = 0;
    			}
    		}
    		if(customerMonthCountList!=null&&customerMonthCountList.size()>0){
    			//获得当月用户量
    			monthCustomerCount = Integer.parseInt(String.valueOf(customerMonthCountList.get(0).get("customer_count")));
    		}else{
				monthCustomerCount = 0;
			}
    		teMap.put("month_customer_count", monthCustomerCount+dailyCustomerCount);
    		result.setData(teMap);
    	} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
	}
	/** 
	* 获得指定日期的后一天 
	* @param specifiedDay 
	* @return 
	*/ 
	public String getSpecifiedDayAfter(String specifiedDay){ 
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);
		String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}
	@Override
	public Map<String, Object> getCityWebViewData(DynamicDto dynamicDto) {
		Map<String,Object> map = new HashMap<String, Object>();
            //事业群饼图
            map.put("TradeDepMonthlyGMV",queryTradeDepMonthlyGMV(dynamicDto));
            //频道GMV饼图
            map.put("TradeChannelGMV",queryTradeChannelGMV(dynamicDto));
            //当月每日营业额
            map.put("DailyTurnover",queryDailyTurnover(dynamicDto.getCityId().toString()));
            //当月每周营业额
            map.put("CustomerCountForWeek",queryCustomerCountForWeek(dynamicDto.getCityId().toString()));
            //当月消费用户
            map.put("MonthCustomerCount",queryMonthCustomerCount(dynamicDto.getCityId().toString()));
            //当月拉新、周拉新、消费用户
            map.put("CustomerStatBycity",queryCustomerStatBycity(dynamicDto.getCityId().toString()));
       
        
        return map;
	}
	
	
	
	    //国安数据接口 微信验证电话是否在国安数据中存在
		@Override
	    public Result wxValiExist(String mobilephone){
	    	Result result = new Result();
	    	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			IFilter iFilter =FilterFactory.getSimpleFilter("mobilephone='"+mobilephone+"' and disabledFlag=1");
			List<User> lst_user = (List<User>) userManager.getList(iFilter);
			if(lst_user!=null&&lst_user.size()>0){
				//存在
				User user = lst_user.get(0);
				user.setUsergroupname(user.getUsergroup().getCode());
				result.setCode(CodeEnum.success.getValue());
		        result.setMessage(CodeEnum.success.getDescription());
		        result.setData(user);
			}else{
				result.setCode(CodeEnum.telePhoneNotExist.getValue());
		        result.setMessage("该电话未在国安数据中注册 ，请联系管理员！ ");
			}
	    	return result;
	    }
	
		
		
		//国安数据接口 微信验证电话openid是否是已认证过的
		@Override
		public Result wxAuthUser(WxUserAuth wxUserAuth){
			Result result = new Result();
	    	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			WxUserAuthManager wxUserAuthManager = (WxUserAuthManager) SpringHelper.getBean("wxUserAuthManager");
	    	IFilter iFilter =FilterFactory.getSimpleFilter("mobilephone='"+wxUserAuth.getMobilephone()+"' and disabledFlag=1");
			List<User> lst_user = (List<User>) userManager.getList(iFilter);
			if(lst_user!=null&&lst_user.size()>1){
				//证明 有2个以上的相同电话的用户。提示电话重复
				result.setCode(CodeEnum.repeatData.getValue());
		        result.setMessage("该手机号已绑定多个国安数据账号!");
		        return result;
			}
			if(lst_user!=null&&lst_user.size()>0){
				User user = lst_user.get(0);
				user.setUsergroupname(user.getUsergroup().getCode());
				//存在 验证 openid是否存在
		    	IFilter authFilter =FilterFactory.getSimpleFilter("user_id="+user.getId()+" and mobilephone='"+wxUserAuth.getMobilephone()+"' and wx_code='"+wxUserAuth.getWx_code()+"'");
		    	List<WxUserAuth> lst_wxuserList = (List<WxUserAuth>) wxUserAuthManager.getList(authFilter);
				if(lst_wxuserList!=null&&lst_wxuserList.size()>0){
					result.setCode(CodeEnum.success.getValue());
			        result.setMessage(CodeEnum.success.getDescription());
			        result.setData(user);
				}else{
					result.setCode(CodeEnum.illegalPhone.getValue());
			        result.setMessage("账号认证失败！");
			        result.setData(user);	
				}
			}else{
				result.setCode(CodeEnum.telePhoneNotExist.getValue());
		        result.setMessage("该电话未在国安数据中注册 ，请联系管理员！ ");
			}
			return result;
		}
	
	
	    //国安数据接口 微信发送验证码
		@Override
		public Result wxSendMessage(String mobilephone){
			Result result = new Result();
			SendMessageManager sendMessageManager = (SendMessageManager) SpringHelper.getBean("sendMessageManager");
			WxUserAuthManager wxUserAuthManager = (WxUserAuthManager) SpringHelper.getBean("wxUserAuthManager");
			if(mobilephone!=null&&mobilephone.length()>0){
				//如果是合法手机号
				if(PhoneFormatCheckUtils.isPhoneLegal(mobilephone)){
					SendMessage sendMessage = new SendMessage();
					SendMessage saveSendMessage = null;
					//判断该电话是否注册国安数据 
					UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
					IFilter iFilter =FilterFactory.getSimpleFilter("mobilephone='"+mobilephone+"' and disabledFlag=1");
					List<User> lst_user = (List<User>) userManager.getList(iFilter);
					String rt="";
					Long msgstatus=0L;
					String savecode = "";
					if(lst_user==null||lst_user.size()==0){
						result.setCode(CodeEnum.telePhoneNotExist.getValue());
				        result.setMessage("该电话未在国安数据中注册 ，请联系管理员！ ");
				        rt="外部用户";
				        msgstatus=1L;
					}else{
						//随机生成四位数
						//String code = "您的验证码是"+randomcode()+"，十分钟内有效。";
						String code = randomcode();
						savecode=code;
						String sendcode = "您的验证码是"+code+"，十分钟内有效。";
						try {
							//String sendcode_gb2312 = URLEncoder.encode(sendcode,"gb2312");
							//String url = "https://datatest.guoanshequ.top/eprj/dispatcher.action?phone="+mobilephone+"&sendcode="+sendcode_gb2312;
							//查询 如果一分钟内存在
							Calendar calendar = Calendar.getInstance();
					        calendar.add(Calendar.MINUTE, -1);
					        String oneMin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
					        FSP fsp = new FSP();
							fsp.setSort(SortFactory.createSort("create_time",ISort.DESC));
					        IFilter messageFilter =FilterFactory.getSimpleFilter("msgstatus=0 and functionname='微信国安数据登录验证码' and mobilephone='"+mobilephone+"' and create_time>='"+oneMin+"'");
					        fsp.setUserFilter(messageFilter);
					        List<SendMessage> sendMessages = (List<SendMessage>) sendMessageManager.getList(fsp);
					        if(sendMessages==null||sendMessages.size()==0){
					        	rt = commonSendMessage(mobilephone, sendcode, "微信国安数据登录验证码");
					        	result.setCode(CodeEnum.success.getValue());
						        result.setMessage(CodeEnum.success.getDescription());
						        //插入认证表中数据。如果存在 不动 如果不存在。则插入，标记未认证
						        User user = lst_user.get(0);
						        WxUserAuth wxUserAuth = wxUserAuthManager.queryWxUserAuthByPhone(user.getId(),mobilephone);
						        if(wxUserAuth==null){
						        	wxUserAuth = new WxUserAuth();
						        	wxUserAuth.setUser_id(lst_user.get(0).getId());
						        	wxUserAuth.setAuthstatus(0L);
						        	wxUserAuth.setWx_code("");
						        	wxUserAuth.setMobilephone(mobilephone);
						        	wxUserAuthManager.saveWxUserAuth(wxUserAuth);
						        }
						        
					        	sendMessage.setFunctionname("微信国安数据登录验证码");
								sendMessage.setMobilephone(mobilephone);
								sendMessage.setCode(savecode);
								sendMessage.setRcvmessage(rt);
								sendMessage.setMsgstatus(msgstatus);//未使用 
								sendMessageManager.saveSendMessage(sendMessage);
					        }
					        
						} catch (Exception e) {
							e.printStackTrace();
							result.setCode(CodeEnum.error.getValue());
					        result.setMessage("Error:"+e.getMessage());
						}
						
					}
					
				}else{
					result.setCode(CodeEnum.illegalPhone.getValue());
			        result.setMessage(CodeEnum.illegalPhone.getDescription());
				}
			}else{
				result.setCode(CodeEnum.error.getValue());
		        result.setMessage(CodeEnum.error.getDescription());
			}
			return result;
		}
		
		
		//手机接口 微信用户登录
		@Override
		public Result wxLogin(SiteSelection siteSelection){
			Result result = new Result();
			//根据短信和电话号去查询是否10分钟内存在 短信数据
			WxUserAuthManager wxUserAuthManager = (WxUserAuthManager) SpringHelper.getBean("wxUserAuthManager");
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			SendMessageManager sendMessageManager = (SendMessageManager) SpringHelper.getBean("sendMessageManager");
			
			IFilter existiFilter =FilterFactory.getSimpleFilter("mobilephone='"+siteSelection.getMobilephone()+"' and disabledFlag=1");
			List<User> existlst_user = (List<User>) userManager.getList(existiFilter);
			if(existlst_user!=null&&existlst_user.size()>1){
				//证明 有2个以上的相同电话的用户。提示电话重复
				result.setCode(CodeEnum.repeatData.getValue());
		        result.setMessage("该手机号已绑定多个国安数据账号!");
		        return result;
			}
			
			//先查看 是否用户认证过 如果认证过 跳过验证码登录。
			WxUserAuth wxUserAuth = wxUserAuthManager.queryWxUserAuthByPhoneCode(siteSelection.getUser_id(),siteSelection.getMobilephone(),siteSelection.getReal_name());
			if(wxUserAuth!=null&&wxUserAuth.getAuthstatus().equals(1L)){
				//直接 登录
				User user = (User) userManager.getObject(wxUserAuth.getUser_id());
				user.setUsergroupname(user.getUsergroup().getCode());
				result.setCode(CodeEnum.success.getValue());
		        result.setMessage(CodeEnum.success.getDescription());
		        result.setData(user);
		        return result;
			}
			
			Calendar calendar = Calendar.getInstance();
	        calendar.add(Calendar.MINUTE, -10);
	        String tenMin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
	        FSP fsp = new FSP();
			fsp.setSort(SortFactory.createSort("create_time",ISort.DESC));
	        IFilter messageFilter =FilterFactory.getSimpleFilter("functionname='微信国安数据登录验证码' and mobilephone='"+siteSelection.getMobilephone()+"' and create_time>'"+tenMin+"'");
	        fsp.setUserFilter(messageFilter);
	        List<SendMessage> sendMessages = (List<SendMessage>) sendMessageManager.getList(fsp);
			if(sendMessages!=null&&sendMessages.size()>0){
				SendMessage sendMessage = sendMessages.get(0);
				if(siteSelection.getPassword()!=null&&sendMessage.getMsgstatus().equals(0L)&&sendMessage.getCode().equals(siteSelection.getPassword())){
					sendMessage.setMsgstatus(1L);
					sendMessageManager.saveSendMessage(sendMessage);
					
					//登录成功后。返回用户信息 
					IFilter iFilter =FilterFactory.getSimpleFilter("id="+siteSelection.getUser_id()+" and mobilephone='"+sendMessage.getMobilephone()+"' and disabledFlag=1");
					List<User> lst_user = (List<User>) userManager.getList(iFilter);
					if(lst_user!=null&&lst_user.size()>0){//存在 返回
						User user = lst_user.get(0);
						//保存 微信号 以及 电话的关系。或微信号与User表的关系。
						WxUserAuth wxUserAuth_up = wxUserAuthManager.queryWxUserAuthByPhone(siteSelection.getUser_id(),siteSelection.getMobilephone());
						wxUserAuth_up.setAuthstatus(1L);
						wxUserAuth_up.setWx_code(siteSelection.getReal_name());
						wxUserAuth_up.setNickname(siteSelection.getNickname());
						wxUserAuthManager.updateWxUserAuth(wxUserAuth_up);
						
						result.setCode(CodeEnum.success.getValue());
				        result.setMessage(CodeEnum.success.getDescription());
				        user.setUsergroupname(user.getUsergroup().getCode());
				        result.setData(user);
					}else{//不存在 异常了
						result.setCode(CodeEnum.error.getValue());
						result.setMessage("获取信息异常！");
					}
				}else{
					result.setCode(CodeEnum.error.getValue());
					result.setMessage("验证码错误");
				}
			}else{
				result.setCode(CodeEnum.error.getValue());
		        result.setMessage("用户名/验证码错误");
			}
	   	    return result;
		}
		
		
		@Override
		public String requestWxApi(String code){
			String url = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=%s";
			String appid = PropertiesUtil.getValue("wx.appid");
			String secret= PropertiesUtil.getValue("wx.secret");
			String grant_type=PropertiesUtil.getValue("wx.grant_type");
			String proxyset = PropertiesUtil.getValue("iproxy.set");
			String resultString = null;
			try {
				HttpHost proxy = new HttpHost("10.0.1.11", 3128, "http");
				RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
				/** 上线时，添加代理设置 **/
				CloseableHttpClient httpclient = null;
				if(proxyset!=null&&proxyset.equals("ON")){
					httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
				}else{
					httpclient = HttpClients.createDefault();
				}
				HttpGet httpGet = new HttpGet(String.format(url, new Object[]{appid,secret,code,grant_type}));
				CloseableHttpResponse response = httpclient.execute(httpGet);
				resultString = EntityUtils.toString(response.getEntity(), "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
				resultString = "Error:" + e.getMessage();
			}
			return resultString;
		}
		
		/**
		 * 通用发送短信的方法  ，参数 手机号 发送内容 发送的功能模块名称
		 * @return
		 */
		@Override
		public String commonSendMessage(String mobilephone,String content,String functionname){
			String resultString = "";
			String proxyip = PropertiesUtil.getValue("iproxy.sendip");
			int proxyport = Integer.parseInt(PropertiesUtil.getValue("iproxy.sendport"));
			try {
				String sendcode_gb2312 = URLEncoder.encode(content,"utf8");
				System.out.println(sendcode_gb2312);
				//String url = "http://q.hl95.com:8061/?username=gasjyz&password=Gasj0121&message="+sendcode_gb2312+"&phone="+mobilephone+"&epid=123743&linkid=&subcode=";
				String url = "https://datatest.guoanshequ.top/eprj/dispatcher.action?phone=%s&sendcode=%s";
				HttpHost proxy = new HttpHost(proxyip, proxyport, "http");
				RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
				/** 上线时，添加代理设置 **/
				CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();;
				//CloseableHttpClient httpclient = HttpClients.createDefault();
				HttpGet httpGet = new HttpGet(String.format(url, new Object[]{mobilephone,sendcode_gb2312}));
				CloseableHttpResponse response = httpclient.execute(httpGet);
				resultString = EntityUtils.toString(response.getEntity(), "utf-8");
				
				//保存发送记录 
				/*SendMessage sendMessage = new SendMessage();
				sendMessage.setFunctionname(functionname);
				sendMessage.setMobilephone(mobilephone);
				sendMessage.setCode(content);
				sendMessage.setRcvmessage(resultString);
				sendMessage.setMsgstatus(0L);//未使用 
				sendMessageManager.saveSendMessage(sendMessage);*/
				
			} catch (Exception e) {
				e.printStackTrace();
				resultString="Error:"+e.getMessage();
			}
			
			return resultString;
		}
		
		@Override
		public User commonValidUser(String userCode,String password){
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			User userEntity = userManager.isValidUser(userCode, password); 
			if(userEntity!=null){
				return userEntity;
			}
			return null;
		}
		
		//工单手机列表
		@Override
		public Result queryStoreOrderListForApp(PageInfo pageInfo,String employee_no,String types){
			Result result = new Result();
			StoreOrderInfoManager storeOrderInfoManager = (StoreOrderInfoManager) SpringHelper.getBean("storeOrderInfoManager");
			try {
				Map<String, Object> rt_maps = storeOrderInfoManager.queryStoreOrderInfoListApp(pageInfo, employee_no,types);
				result.setCode(CodeEnum.success.getValue());
	            result.setMessage(CodeEnum.success.getDescription());
	            result.setData(rt_maps);
			} catch (Exception e) {
				result.setCode(CodeEnum.error.getValue());
				result.setMessage(CodeEnum.error.getDescription());
			}
   		 	return result;
		}
		//工单手机新增
		@Override
		public Result saveStoreOrderInfoForApp(StoreOrderInfo storeOrderInfo){
			Result result = new Result();
			StoreOrderInfoManager storeOrderInfoManager = (StoreOrderInfoManager) SpringHelper.getBean("storeOrderInfoManager");
			if(storeOrderInfo!=null){
				if(storeOrderInfo.getEmployee_no()==null||storeOrderInfo.getEmployee_name()==null||storeOrderInfo.getEmployee_no().trim()==""){
					result.setCode(CodeEnum.nullData.getValue());
					result.setMessage("员工为空值！保存失败！");
				}else if(storeOrderInfo.getWcontent()==null||storeOrderInfo.getWcontent().trim()==""){
					result.setCode(CodeEnum.nullData.getValue());
					result.setMessage("工单内容为空值！保存失败！");
				}else if(storeOrderInfo.getPhone()==null||storeOrderInfo.getPhone().trim()==""){
					result.setCode(CodeEnum.nullData.getValue());
					result.setMessage("客户电话为空值！保存失败！");
				}else if(storeOrderInfo.getRcv_phone()==null||storeOrderInfo.getRcv_phone().trim()==""){
					result.setCode(CodeEnum.nullData.getValue());
					result.setMessage("收货电话为空值！保存失败！");
				}else if(storeOrderInfo.getAddress()==null||storeOrderInfo.getAddress().trim()==""){
					result.setCode(CodeEnum.nullData.getValue());
					result.setMessage("收货地址为空值！保存失败！");
				}else if(storeOrderInfo.getStore_id()==null){
					result.setCode(CodeEnum.nullData.getValue());
					result.setMessage("门店ID为空值！保存失败！");
				}else{
					storeOrderInfoManager.saveStoreOrderInfoForApp(storeOrderInfo);
					result.setCode(CodeEnum.success.getValue());
					result.setMessage(CodeEnum.success.getDescription());
					result.setData(storeOrderInfo);
				}
			}
			return result;
		}
		
		
		
		//工单手机修改
		@Override
		public Result updateStoreOrderInfoForApp(StoreOrderInfo storeOrderInfo){
			Result result = new Result();
			StoreOrderInfoManager storeOrderInfoManager = (StoreOrderInfoManager) SpringHelper.getBean("storeOrderInfoManager");
			if(storeOrderInfo!=null&&storeOrderInfo.getId()!=null){
				StoreOrderInfo updateStoreOrderInfo = storeOrderInfoManager.updateStoreOrderInfo(storeOrderInfo);
				result.setCode(CodeEnum.success.getValue());
				result.setMessage(CodeEnum.success.getDescription());
				result.setData(updateStoreOrderInfo);
			}else{
				result.setCode(CodeEnum.error.getValue());
				result.setMessage(CodeEnum.error.getDescription());
			}
			return result;
		}
				
		
		/**
		 * 手机找回密码 验证手机是否是系统里的可用用户 
		 * @return
		 */
		@Override
		public Result validateUserPhone(String phone){
			Result result = new Result();
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			SendMessageManager sendMessageManager = (SendMessageManager) SpringHelper.getBean("sendMessageManager");
			if(phone!=null&&phone.length()>0&&phone.length()==11){
				IFilter iFilter =FilterFactory.getSimpleFilter("mobilephone='"+phone+"' and disabledFlag=1 ");
				List<User> userList = (List<User>) userManager.getList(iFilter);
				if(userList!=null&&userList.size()==1){
					//发送短信
					String code = randomcode();
					String sendcode = "您的验证码是"+code+"，十分钟内有效。";
					String resultString ="";
					//resultString = commonSendMessage(phone, sendcode, "找回密码验证");
					
					//保存发送记录 
					SendMessage sendMessage = new SendMessage();
					sendMessage.setFunctionname("找回密码验证");
					sendMessage.setMobilephone(phone);
					sendMessage.setCode(code);
					sendMessage.setRcvmessage(resultString);
					sendMessage.setMsgstatus(0L);//未使用 
					sendMessageManager.saveSendMessage(sendMessage);
					
					result.setCode(CodeEnum.success.getValue());
					result.setMessage(CodeEnum.success.getDescription());
					result.setData(userList.get(0));
				}else{
					result.setCode(CodeEnum.repeatData.getValue());
					result.setMessage("无效电话");
				}
			}else{
				result.setCode(CodeEnum.nullData.getValue());
				result.setMessage("无效电话");
			}
			return result;
		}
		
		//根据手机号 员工编号 查询一个用户 如果存在多个，则返回异常
		@Override
		public Result initReset(String inputcode){
			Result result = new Result();
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
	        IFilter userFilter = (FilterFactory.getSimpleFilter("employeeId",inputcode)
			.appendOr(FilterFactory.getSimpleFilter("mobilephone", inputcode)))
			.appendAnd(FilterFactory.getSimpleFilter("disabledFlag", "1"));
	        List<User> lst_users = (List<User>) userManager.getList(userFilter);
	        if(lst_users!=null&&lst_users.size()>0){
	        	//账号正常
	        	if(lst_users.size()==1){
	        		result.setCode(CodeEnum.success.getValue());
					result.setMessage(CodeEnum.success.getDescription());
					result.setData(lst_users.get(0));
	        	}else{
	        		result.setCode(CodeEnum.repeatData.getValue());
					result.setMessage("该账号存在异常");
	        	}
	        }else{
	        	result.setCode(CodeEnum.nullData.getValue());
				result.setMessage("您输入的账户名不存在，请核对后重新输入。");
	        }
			return result;
		}
		
		
		//根据ID查询一个用户 返回电话
		@Override
		public Result queryuserbyid(String id){
			Result result = new Result();
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
	        
			if(id!=null&&id.length()>0){
				try {
					User user = (User) userManager.getObject(Long.parseLong(id));
					result.setCode(CodeEnum.success.getValue());
					result.setMessage(CodeEnum.success.getDescription());
					result.setData(user);
				} catch (Exception e) {
					result.setCode(CodeEnum.repeatData.getValue());
					result.setMessage("该账号存在异常");
				}
			}else{
				result.setCode(CodeEnum.nullData.getValue());
				result.setMessage("该账号不存在");
			}
			return result;
		}
		
		@Override
		public Result codeValidation(String phone,String code){
			SendMessageManager sendMessageManager = (SendMessageManager) SpringHelper.getBean("sendMessageManager");
			Result result = new Result();
			if(code!=null&&code.trim().length()>0&&phone!=null&&phone.trim().length()>0){
				Calendar calendar = Calendar.getInstance();
		        calendar.add(Calendar.MINUTE, -10);
		        String tenMin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
				FSP fsp = new FSP();
				fsp.setSort(SortFactory.createSort("id",ISort.DESC));
		        IFilter messageFilter =FilterFactory.getSimpleFilter("functionname='找回密码验证' and mobilephone='"+phone+"' and create_time>'"+tenMin+"'");
		        fsp.setUserFilter(messageFilter);
		        List<SendMessage> sendMessages = (List<SendMessage>) sendMessageManager.getList(fsp);
		        if(sendMessages!=null&&sendMessages.size()>0){
					SendMessage sendMessage = sendMessages.get(0);
					if(sendMessage.getMsgstatus().equals(0L)&&sendMessage.getCode().equals(code)){
						sendMessage.setMsgstatus(1L);//已使用
						sendMessageManager.saveSendMessage(sendMessage);
						result.setCode(CodeEnum.success.getValue());
						result.setMessage(CodeEnum.success.getDescription());
						result.setData(sendMessage);
					}else{
						result.setCode(CodeEnum.nullData.getValue());
						result.setMessage("手机验证码错误");
					}
		        }else{
		        	result.setCode(CodeEnum.nullData.getValue());
					result.setMessage("无效电话/手机验证码");
		        }
			}else{
				result.setCode(CodeEnum.nullData.getValue());
				result.setMessage("无效电话/手机验证码");
			}
			return result;
		}
		//查询一次 防止刷URL
		@Override
		public Result queryUserByPhoneCode(String phone,String code){
			SendMessageManager sendMessageManager = (SendMessageManager) SpringHelper.getBean("sendMessageManager");
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			Result result = new Result();
			Calendar calendar = Calendar.getInstance();
	        calendar.add(Calendar.MINUTE, -2);
	        String tenMin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
			if(phone!=null&&phone.length()==11&&code!=null&&code.length()==4){
				IFilter messageFilter =FilterFactory.getSimpleFilter("functionname='找回密码验证' and mobilephone='"+phone+"' and code = '"+code+"' and create_time>'"+tenMin+"'");
		        List<SendMessage> sendMessages = (List<SendMessage>) sendMessageManager.getList(messageFilter);
		        if(sendMessages!=null&&sendMessages.size()>0){
		        	SendMessage sendMessage = sendMessages.get(0);
		        	String mobilephone = sendMessage.getMobilephone();
		        	IFilter iFilter =FilterFactory.getSimpleFilter("mobilephone='"+mobilephone+"' and disabledFlag=1 ");
					List<User> userList = (List<User>) userManager.getList(iFilter);
					if(userList!=null&&userList.size()>0){
						result.setCode(CodeEnum.success.getValue());
						result.setMessage(CodeEnum.success.getDescription());
						result.setData(userList.get(0));
					}else{
						result.setCode(CodeEnum.nullData.getValue());
						result.setMessage("无效电话/验证码");
					}
		        }else{
		        	result.setCode(CodeEnum.nullData.getValue());
					result.setMessage("无效电话/验证码");
		        }
			}else{
				result.setCode(CodeEnum.nullData.getValue());
				result.setMessage("无效电话/验证码");
			}
	        return result;
		}
		@Override
		public String updatePassword(User user){
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			String ret_msg = userManager.modifyStoreUserPassword(user);
			return ret_msg;
		}
		
		
		@Override
		public Result queryStoreOrderInfoByPhone(PageInfo pageinfo,String phone){
			Result result = new Result();
			StoreOrderInfoManager storeOrderInfoManager = (StoreOrderInfoManager) SpringHelper.getBean("storeOrderInfoManager");
			Map<String, Object> storeOrderInfos = storeOrderInfoManager.queryStoreOrderInfoListByPhone(pageinfo,phone);
			if(storeOrderInfos!=null&&storeOrderInfos.size()>0){
				result.setCode(CodeEnum.success.getValue());
				result.setMessage(CodeEnum.success.getDescription());
				result.setData(storeOrderInfos);
			}else{
				result.setCode(CodeEnum.nullData.getValue());
				result.setMessage(CodeEnum.nullData.getDescription());
			}
			return result;
		}
		
		//修改工单状态(根据工单编号)
		@Override
		public Result updateStoreOrderStatusByNo(String worder_sn,int worder_status){
			Result result = new Result();
			StoreOrderInfoManager storeOrderInfoManager = (StoreOrderInfoManager) SpringHelper.getBean("storeOrderInfoManager");
			StoreOrderInfo storeOrderInfo = storeOrderInfoManager.updateStoreOrderInfoBySN(worder_sn, worder_status);
			if(storeOrderInfo!=null&&storeOrderInfo.getWorder_sn().equals(worder_sn)){
				result.setCode(CodeEnum.success.getValue());
				result.setMessage(CodeEnum.success.getDescription());
				result.setData(storeOrderInfo);
			}else{
				result.setCode(CodeEnum.error.getValue());
				result.setMessage(CodeEnum.error.getDescription());
			}
			return result;
		}
		
		@Override
		public Result createCode(){
			Result result = new Result();
			String validcode = ValidationCode.createCode();
			result.setCode(CodeEnum.success.getValue());
			result.setMessage(CodeEnum.success.getDescription());
			result.setData(validcode);
			return result;
		}
		
		
		//根据工单编号  查询一条工单 
		@Override
		public Result queryStoreOrderByOrderSN(String orderSN){
			Result result = new Result();
	    	if(orderSN!=null&&orderSN.length()>0){
	    		IFilter iFilter =FilterFactory.getSimpleFilter("worder_sn='"+orderSN+"'");
	    		StoreOrderInfoManager storeOrderInfoManager = (StoreOrderInfoManager) SpringHelper.getBean("storeOrderInfoManager");
	    		List<StoreOrderInfo> storeOrderInfos = (List<StoreOrderInfo>) storeOrderInfoManager.getList(iFilter);
	    		if(storeOrderInfos!=null&&storeOrderInfos.size()>0){
	    			result.setCode(CodeEnum.success.getValue());
	    			result.setMessage(CodeEnum.success.getDescription());
	    			result.setData(storeOrderInfos.get(0));
	    		}else{
	    			result.setCode(CodeEnum.repeatData.getValue());
	    			result.setMessage(CodeEnum.repeatData.getDescription());
	    		}
	    	}else{
	    		result.setCode(CodeEnum.nullData.getValue());
				result.setMessage(CodeEnum.nullData.getDescription());
	    	}
			return result;
		}
}
