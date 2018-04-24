package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.Relation;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.UserTag;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.RelationManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.UserPurchaseHabitManager;
import com.cnpc.pms.personal.manager.UserTagManager;
import com.cnpc.pms.platform.dao.OrderDao;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserTagManagerImpl extends BaseManagerImpl implements UserTagManager {

	@Override
	public Map<String, Object> getUserTagByPhone(String mobilephone) {
		UserPurchaseHabitManager userPurchaseHabitManager = (UserPurchaseHabitManager) SpringHelper.getBean("userPurchaseHabitManager");
		List<?> lst_vilage_data =this.getList(FilterFactory.getSimpleFilter("mobilephone",mobilephone));
		UserTag userTag = null;
		Map<String, Object> rt_obj = new HashMap<String, Object>();
        if(lst_vilage_data != null && lst_vilage_data.size() > 0){
        	userTag = (UserTag)lst_vilage_data.get(0);
        	rt_obj.put("userTab", userTag);
        	//根据customer_id查询 订单频道
        	if(userTag!=null&&userTag.getCustomer_id()!=null){
        		List<?> lst_pur_order =userPurchaseHabitManager.getList(FilterFactory.getSimpleFilter("customer_id",userTag.getCustomer_id()));
        		rt_obj.put("channels", lst_pur_order);
        		
        		OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
        		List<Map<String, Object>> lst_orders = orderDao.queryOrderByCustomerIdTop20(userTag.getCustomer_id());
        		List<Map<String, Object>> rt_orderList = new ArrayList<Map<String,Object>>();
        		if(lst_orders!=null&&lst_orders.size()>0){
        			StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
        			for(Map<String, Object> map : lst_orders){
        				String platformId = map.get("store_id")==null?"":map.get("store_id").toString();
        				List<Store> storeLists =(List<Store>) storeManager.getList(FilterFactory.getSimpleFilter("platformid",platformId));
        				if(storeLists!=null&&storeLists.size()>0){
        					map.put("store_name", storeLists.get(0).getName());
        				}
        				rt_orderList.add(map);
        			}
            		
        		}
        		rt_obj.put("orders", rt_orderList);
        		
        	}
        	
        	//查询拜访记录 
        	CustomerManager customerManager = (CustomerManager) SpringHelper.getBean("customerManager");
        	List<Customer> lst_customer_list =(List<Customer>) customerManager.getList(FilterFactory.getSimpleFilter("mobilephone",mobilephone));
        	if(lst_customer_list!=null&&lst_customer_list.size()>0){
        		String customer_ids = "";
        		String customer_pic = lst_customer_list.get(0).getCustomer_pic();
        		String web = PropertiesUtil.getValue("file.web.root").concat("user_image").concat(File.separator);
                String picPath = PropertiesUtil.getValue("file.root").concat("user_image").concat(File.separator);
        		if(customer_pic != null && !"".equals(customer_pic)){
                    File pic_dir = new File(picPath);
                    final String customer_pic1 = customer_pic;
                    File[] file_pics = pic_dir.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.getName().contains(customer_pic1);
                        }
                    });
                    String full_name = file_pics == null || file_pics.length == 0 ? null : file_pics[0].getName();
                    if(full_name != null) {
                        String full_path = web.concat(full_name);
                        rt_obj.put("face_pic", full_path);
                    }
                    
                }
        		customer_ids+=lst_customer_list.get(0).getId();
        		for(int i=1;i<lst_customer_list.size();i++){
        			customer_ids+=","+lst_customer_list.get(i).getId();
        		}
        		//根据customer_ids查询 拜访记录数据 
        		RelationManager relationManager = (RelationManager) SpringHelper.getBean("relationManager");
        		IFilter iFilter = FilterFactory.getSimpleFilter("customer_id in("+customer_ids+")");
        		FSP fsp = new FSP();
        		fsp.setUserFilter(iFilter);
        		fsp.setSort(new Sort("create_time",ISort.DESC));
        		List<Relation> lst_relationList = (List<Relation>) relationManager.getList(fsp);
        		List<Relation> max_Relations= new ArrayList<Relation>();
        		if(lst_relationList!=null&&lst_relationList.size()>5){
        			max_Relations.add(lst_relationList.get(0));
        			max_Relations.add(lst_relationList.get(1));
        			max_Relations.add(lst_relationList.get(2));
        			max_Relations.add(lst_relationList.get(3));
        			max_Relations.add(lst_relationList.get(4));
        			rt_obj.put("relations", max_Relations);
        		}else{
        			rt_obj.put("relations", lst_relationList);
        		}
        		if(lst_relationList!=null){
        			rt_obj.put("relations_count", lst_relationList.size());
        		}else{
        			rt_obj.put("relations_count", "0");
        		}
        		
        	}
        	
        	return rt_obj;
        }
        return null;
	}
    
	
}
