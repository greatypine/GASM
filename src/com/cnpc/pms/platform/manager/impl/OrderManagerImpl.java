package com.cnpc.pms.platform.manager.impl;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.dynamic.dao.DynamicDao;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.inter.dao.InterDao;
import com.cnpc.pms.personal.dao.DsAbnormalOrderDao;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.platform.dao.OrderDao;
import com.cnpc.pms.platform.manager.OrderManager;
import com.cnpc.pms.slice.dao.AreaDao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 订单表查询
 * Created by liuxiao on 2016/10/25.
 */
public class OrderManagerImpl extends BizBaseCommonManager implements OrderManager {

    @Override
    public int getOrderCount(String store_id,String employee_id,String year_month) {
        OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
        return orderDao.getOrderCount(store_id,employee_id,year_month);
    }

    @Override
    public Map<String, Object> queryOrderEmployeeCountByStore(PageInfo pageInfo, String store_id, String date_value) {
        OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
        return orderDao.queryOrderEmployeeCountByStore(pageInfo,store_id,date_value);
    }

    @Override
    public List<Map<String, Object>> getOrderEmployeeData(String store_id, String date_value) {
        OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
        return orderDao.getOrderEmployeeData(store_id,date_value);
    }
    
    /**
     * PC 分片查询订单信息
     */
    @Override
    public Map<String, Object> queryOrderListByArea(String employee_no,PageInfo pageInfo,Long area_id){
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	
    	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
    	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
    	User user = null;
    	if(employee_no==null){
    		user = new User();
    		user.setStore_id(userManager.getCurrentUserDTO().getStore_id());
    	}else{
    		user = userManager.findEmployeeByEmployeeNo(employee_no);
    	}
    	Map<String, Object> store = storeManager.getStoreById(user.getStore_id());
    	AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
    	String area_names = areaDao.querytinvillagebyemployeeno(employee_no,area_id);
    	return orderDao.queryOrderListByArea(store.get("platformid").toString(), area_names,pageInfo);
    }
    
    
    
    
    
    
    @Override
    public Map<String, Object> queryOrderListByEmployeeNo(String employee_no,PageInfo pageInfo,Long area_id){
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
    	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
    	User user = userManager.findEmployeeByEmployeeNo(employee_no);
    	if(user!=null){
    		Map<String, Object> store = storeManager.getStoreById(user.getStore_id());
        	AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
        	String area_names = areaDao.querytinvillagebyemployeeno(employee_no,area_id);
        	if(store!=null){
            	return orderDao.queryOrderListByEmployeeNo(store.get("platformid").toString(), employee_no,area_names,pageInfo);
        	}else{
        		return null;
        	}
    	}else{
    		return null;
    	}
    	
    }
    
    @Override
    public List<Map<String, Object>> queryOrderFourMonth(String employee_no,Long area_id){
    	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
    	UserDTO userDTO = userManager.getCurrentUserDTO();
    	AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
    	String area_names = areaDao.querytinvillagebyemployeeno(employee_no,area_id);
    	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
    	Map<String, Object> store = storeManager.getStoreById(userDTO.getStore_id());
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	List<Map<String, Object>> lst_list = orderDao.queryOrderFourMonth(store.get("platformid").toString(), area_names);
    	return lst_list;
    }
    
    
    
    /**
     * APP手机国安侠分片 查询五个月的图表
     */
    @Override
    public List<Map<String, Object>> queryOrderFiveMonth(Long store_id,String employee_no,Long area_id){
    	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
    	AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
    	String area_names = areaDao.querytinvillagebyemployeeno(employee_no,area_id);
    	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
    	User user = null;
    	if(employee_no==null){
    		user = new User();
    		user.setStore_id(store_id);
    	}else{
    		user = userManager.findEmployeeByEmployeeNo(employee_no);
    	}
    	Map<String, Object> store = storeManager.getStoreById(user.getStore_id());
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	List<Map<String, Object>> lst_list = orderDao.queryOrderFiveMonth(store.get("platformid").toString(), area_names);
    	return lst_list;
    }
    
    
    
    /**
     * APP手机国安侠 个人中心 不分片 查询五个月的图表
     */
    @Override
    public List<Map<String, Object>> queryOrderFiveMonthOrderApp(String employee_no){
    	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
    	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
    	User user = userManager.findEmployeeByEmployeeNo(employee_no);
    	Map<String, Object> store = storeManager.getStoreById(user.getStore_id());
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	List<Map<String, Object>> lst_list = orderDao.queryOrderFiveMonthOrderApp(store.get("platformid").toString(), employee_no);
    	return lst_list;
    }
    
    
    
    
    /**
     * 根据订单sn编号 查询明细信息 
     * @param order_sn
     * @return
     */
    @Override
    public Map<String, Object> queryOrderInfoBySN(String order_sn){
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	Map<String,Object> order_obj = orderDao.queryOrderInfoBySN(order_sn);
    	String order_id = order_obj.get("id")==null?"":order_obj.get("id").toString();
    	List<Map<String, Object>> item_list = orderDao.queryOrderItemInfoById(order_id);
    	Map<String, Object> orderFlow = orderDao.getOrderFlow(order_id, "signed");
    	if(orderFlow==null){
    		order_obj.put("receivedTime","");
    	}else{
    		order_obj.put("receivedTime",orderFlow.get("create_time"));
    	}
    	order_obj.put("item_list", item_list);
    	order_obj.put("create_time", order_obj.get("create_time"));
    	
    	return order_obj;
    }
    
    
    
    /**
     * CRM店长 根据门店 查询 送单量排序  图表的方法  
     * @param store_id
     * @return
     */
    @Override
    public Map<String, Object> queryOrderCountByStoreId(Long store_id){
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	Map<String, Object> map = new HashMap<String, Object>();
    	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
    	Store store = storeManager.findStore(store_id);
    	List<Map<String, Object>> order_List = orderDao.queryOrderCountByStoreId(store.getPlatformid());
    	map.put("orders", order_List);
    	return map;
    	
    }
    
    
    
    /**
     * CRM店长图表 根据门店及片区，查询所有订单数据
     * @param store_id
     */
    @Override
    public Map<String, Object> queryOrderTotalByArea(Long store_id){
    	Map<String, Object> map = new HashMap<String, Object>();
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
    	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
    	Store store = storeManager.findStore(store_id);
    	//门店下的 所有的 小区 名称
    	List<Map<String, Object>> tinyList = areaDao.queryTinVillageByStoreId(store_id);
    	StringBuffer ret = new StringBuffer();
    	String area_name = null;
    	Set<String> sets = new HashSet<String>();
        if(tinyList!=null&&tinyList.size()>0){
        	for(Map<String, Object> obj : tinyList){
        		Object  o = obj.get("tiny_name");
        		Object areaname = obj.get("name");
        		if(o!=null&&o.toString().length()>0){
        			ret.append("'");
        			ret.append(o.toString());
        			ret.append("',");
        		}
        		if(areaname!=null&&areaname.toString().length()>0){
        			sets.add(areaname.toString());
        		}
        	}
        }
        if(ret!=null&&ret.length()>0){
        	area_name = ret.toString().substring(0,ret.toString().length()-1);
        }
    	//根据门店及所有小区 的订单总数 及总钱数
    	List<Map<String, Object>> orderList = orderDao.queryOrderListByArea(store.getPlatformid(), area_name);
    	
    	List<Map<String, Object>> retList = new ArrayList<Map<String,Object>>();
    	for(String areaname:sets){
    		Map<String, Object> onearea = new HashMap<String, Object>();
    		int count = 0;
    		double price = 0;
    		for(Map<String, Object> obj:orderList){
    			String place = obj.get("placename").toString();
    			int ordercount = obj.get("ordercount")==null?0:Integer.parseInt(obj.get("ordercount").toString());
    			double orderprice = obj.get("total_price")==null?0:Double.parseDouble(obj.get("total_price").toString());
    			String areaplace = "";
    			for(Map<String, Object> tinObj : tinyList){
    				Object  o = tinObj.get("tiny_name");
    				if(place!=null&&place.equals(o.toString())){
    					areaplace = tinObj.get("name").toString();
            		}
    			}
    			
    			if(areaname.equals(areaplace)){
    				count = count + ordercount;
    				price = price + orderprice;
    			}
    		}
    		
    		BigDecimal b = new BigDecimal(price);
    		int price_ret = b.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    		
    		onearea.put("placename", areaname);
    		onearea.put("ordercount", count);
    		onearea.put("total_price", price_ret);
    		retList.add(onearea);
    	}
    	map.put("areacount", retList);
    	return map;
    }
    
    
    /**
     * 店长CRM 根据门店ID取得今年的 每个月的订单数及金额
     * @param store_id
     */
    @Override
    public Map<String, Object> queryOrderCountByMonthStoreId(Long store_id){
    	Map<String, Object> map = new HashMap<String, Object>();
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
    	Store store = storeManager.findStore(store_id);
    	List<Map<String, Object>> maps = orderDao.queryOrderCountByMonthStoreId(store.getPlatformid());
    	map.put("ordermonth", maps);
    	return map;
    }

	
	@Override
	public Map<String, Object> queryOrderTotalOfStore_CSZJ_QYJL(Long cityId,Long employee_no,String role,String q_date) {
		Map<String, Object> map = new HashMap<String, Object>();
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
    	List<Map<String, Object>> storeList = storeDao.getAllStoreOfCRM(employee_no, cityId, role);
    	
		StringBuilder storeIdSb = new StringBuilder();
		storeIdSb.append("(");
		
		for(int i=0;i<storeList.size();i++){
			storeIdSb.append("'"+storeList.get(i).get("platformid")+"',");
		
		}
		if(storeIdSb.indexOf(",")>0){
			storeIdSb = storeIdSb.deleteCharAt(storeIdSb.length()-1);
		}else{
			storeIdSb.append("''");
		}
		
		storeIdSb.append(")");
    	List<Map<String, Object>> orderList = orderDao.queryOrderListOfStore_CSZJ_QYJL(storeIdSb);
    	for(int j=0;j<storeList.size();j++){
    		Map<String,Object> storeMap = storeList.get(j);
    		String platformid = storeMap.get("platformid").toString();
    		storeMap.put("ordercount",0);
			storeMap.put("total_price",0);
    		for(int m=0;m<orderList.size();m++){
    			Map<String,Object> tempMap = orderList.get(m);
    			String storeId = tempMap.get("store_id").toString();
    			
    			if(platformid.equals(storeId)){
    				storeMap.put("ordercount",tempMap.get("ordercount"));
    				storeMap.put("total_price",tempMap.get("total_price"));
    				break;
    			}
    		}
        }
    	map.put("ordercount", storeList);
    	return map;
	}

	
	@Override
	public Map<String, Object> queryOrderCountOfMonth_CSZJ_QYJL(Long cityId, Long employee_no, String role) {
		Map<String, Object> map = new HashMap<String, Object>();
    	OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
    	StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
    	List<Map<String, Object>> storeList = storeDao.getAllStoreOfCRM(employee_no, cityId, role);
    	
		StringBuilder storeIdSb = new StringBuilder();
		storeIdSb.append("(");
		
		for(int i=0;i<storeList.size();i++){
			storeIdSb.append("'"+storeList.get(i).get("platformid")+"',");
		
		}
		if(storeIdSb.indexOf(",")>0){
			storeIdSb = storeIdSb.deleteCharAt(storeIdSb.length()-1);
		}else{
			storeIdSb.append("''");
		}
		
		storeIdSb.append(")");
    	List<Map<String, Object>> orderList = orderDao.queryOrderCountOfMonth_CSZJ_QYJL(storeIdSb);
    	
    	Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		List<Map<String, Object>> o_List = new ArrayList<Map<String,Object>>();
		for(int i=1;i<=month;i++){
			String monthStr = i+"月";
			Map<String, Object> orderMap = new HashMap<String,Object>();
			orderMap.put("total_count", 0);
			orderMap.put("total_price", 0);
			orderMap.put("date", monthStr);
			o_List.add(orderMap);
			for(Map rMap:orderList){
				if(monthStr.equals(rMap.get("order_date"))){
					orderMap.put("total_count", rMap.get("ordercount"));
					orderMap.put("total_price", rMap.get("total_price"));
					break;
				}
				
			}
		}
		
		map.put("ordermonth", o_List);
		return map;
	}

	
	@Override
	public Map<String, Object> queryOrderCountOfMonth_CSZJ_QYJL_before(Long cityId, String employee_no, String role) {
		Map<String, Object> map = new HashMap<String, Object>();
		InterDao orderDao = (InterDao) SpringHelper.getBean(InterDao.class.getName());
    	
    	List<Map<String, Object>> orderList = orderDao.queryOrderCountOfMonth_CSZJ_QYJL_before(employee_no, cityId, role);
    	
    	Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		List<Map<String, Object>> o_List = new ArrayList<Map<String,Object>>();
		for(int i=1;i<=month;i++){
			String monthStr = i+"月";
			Map<String, Object> orderMap = new HashMap<String,Object>();
			orderMap.put("total_count", 0);
			orderMap.put("total_price", 0);
			orderMap.put("date", monthStr);
			o_List.add(orderMap);
			for(Map rMap:orderList){
				if(monthStr.equals(rMap.get("order_date"))){
					orderMap.put("total_count", rMap.get("ordercount"));
					orderMap.put("total_price", rMap.get("total_price"));
					break;
				}
				
			}
		}
		
		map.put("ordermonth", o_List);
		return map;
	}

	
	@Override
	public Map<String, Object> queryOrderTotalOfStore_CSZJ_QYJL_before(Long cityId, Long employee_no, String role,
			String q_date) {
		Map<String, Object> map = new HashMap<String, Object>();
    	InterDao orderDao = (InterDao) SpringHelper.getBean(InterDao.class.getName());
    	List<Map<String, Object>> orderList = orderDao.queryOrderListOfStore_CSZJ_QYJL_before(employee_no, cityId, role, q_date);
    	map.put("ordercount", orderList);
    	List<Map<String, Object>> orderList1 = new ArrayList<Map<String, Object>>();
    	orderList1.addAll(orderList);
    	Comparator<Map> descComparator = Collections.reverseOrder(new OrderComparator());
    	Collections.sort(orderList1, descComparator);
    	map.put("orderprice", orderList1);
    	return map;
	}
	
   public class OrderComparator   implements Comparator<Map>{
		@Override
		public int compare(Map o1, Map o2) {
			
			return Integer.parseInt(o1.get("total_price").toString())-Integer.parseInt(o2.get("total_price").toString());
		}
	   
   }
    
   
   
   
   
   
   
   
   @Override
   public Map<String, Object> queryOrderSetInterval(String career_group) {
		Map<String, Object> map = new HashMap<String, Object>();
		HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
		//根据事业部查询所有的门店 
		/*IFilter iFilter_store = FilterFactory.getSimpleFilter(" career_group='"+career_group+"' AND zw='服务专员' AND humanstatus=1 GROUP BY store_id");
		List<Humanresources> lst_human_list_store = (List<Humanresources>) humanresourcesManager.getList(iFilter_store);
		String store_ids = "";
		if(lst_human_list_store!=null&&lst_human_list_store.size()>0){
			store_ids+=lst_human_list_store.get(0).getStore_id();
			for(int i=1;i<lst_human_list_store.size();i++){
				store_ids+=","+lst_human_list_store.get(i).getStore_id();
			}
		}*/
		
		OrderDao orderDao =  (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
		String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		//String platformstoreid = "";
		
		/*if(store_ids!=""){
			IFilter iFilter_storeplatformid = FilterFactory.getSimpleFilter(" store_id in("+store_ids+")");
			List<Store> lst_storeList = (List<Store>) storeManager.getList(iFilter_storeplatformid);
			if(lst_storeList!=null&&lst_storeList.size()>0){
				if(lst_storeList.get(0).getPlatformid()!=null){
					platformstoreid+="'"+lst_storeList.get(0).getPlatformid()+"'";
				}
				for(int i=1;i<lst_storeList.size();i++){
					if(lst_storeList.get(i).getPlatformid()!=null){
						platformstoreid+=",'"+lst_storeList.get(i).getPlatformid()+"'";
					}
				}
			}
		}*/
		
		
		List<Map<String, Object>> lst_orderList = null;
		lst_orderList = orderDao.queryOrderSetInterval(career_group, nowDate);
		
		if(lst_orderList!=null){
			map.put("ordercount", lst_orderList.size());
			
			double totalPrice = 0;
			for(Map<String, Object> objs : lst_orderList){
				double trading_price = 0;
				try {
					trading_price = Double.parseDouble(objs.get("trading_price")==null?"0":objs.get("trading_price").toString());
				} catch (Exception e) {
					trading_price =0;
				}
				totalPrice+=trading_price;
			}
			
			BigDecimal b = new BigDecimal(totalPrice);
			double totalprice = b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			map.put("totalprice", totalprice);
			
		}else{
			map.put("ordercount", 0);
			map.put("totalprice", 0);
		}
	   	return map;
   }
   
   //根据事业群 定时查询
   @Override
   public Map<String, Object> queryOrderAmountByMonth(String careergroup){
	   Map<String, Object> map = new HashMap<String, Object>();
		DsAbnormalOrderDao dsAbnormalOrderDao = (DsAbnormalOrderDao) SpringHelper.getBean(DsAbnormalOrderDao.class.getName());
	    Calendar cal = Calendar.getInstance(); 
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		Map<String, Object> month_order_obj = dsAbnormalOrderDao.queryOrderAmountByMonth(year, month,careergroup);
		double monthprice = 0;
		if(month_order_obj!=null){
			monthprice = Double.parseDouble(month_order_obj.get("order_amount")==null?"0":month_order_obj.get("order_amount").toString());
		}
		BigDecimal month_price = new BigDecimal(monthprice);
		double format_month_price = month_price.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		map.put("monthtotal", format_month_price);
		
		return map;
		
   }
   
   
   
   //事业群 分城市 查询营业额
 	@Override
 	public List<Map<String, Object>> queryOrderAmountByGroupCity(String careername){
 		DsAbnormalOrderDao dsAbnormalOrderDao = (DsAbnormalOrderDao) SpringHelper.getBean(DsAbnormalOrderDao.class.getName());
 		Calendar cal = Calendar.getInstance(); 
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
 		List<Map<String, Object>> lst_orderamountList = dsAbnormalOrderDao.queryOrderAmountByGroupCity(year, month, careername);
 		
 		if(lst_orderamountList!=null&&lst_orderamountList.size()>0){
 			List<Map<String, Object>> rt_map = new ArrayList<Map<String,Object>>();
 			for(Map<String, Object> o:lst_orderamountList){
 				Map<String, Object> rt_o = new HashMap<String, Object>();
 				rt_o.put("name", o.get("city_name"));
 				rt_o.put("value", o.get("totalamount"));
 				rt_map.add(rt_o);
 			}
 			return rt_map;
 		}
 		return null;
 	}
 	
 	
 	//事业群 分频道 查询营业额
 	 	@Override
 	 	public List<Map<String, Object>> queryOrderAmountByChannel(String careername){
 	 		DsAbnormalOrderDao dsAbnormalOrderDao = (DsAbnormalOrderDao) SpringHelper.getBean(DsAbnormalOrderDao.class.getName());
 	 		Calendar cal = Calendar.getInstance(); 
 			int year = cal.get(Calendar.YEAR);
 			int month = cal.get(Calendar.MONTH)+1;
 	 		List<Map<String, Object>> lst_orderamountList = dsAbnormalOrderDao.queryOrderAmountByChannel(year, month, careername);
 	 		
 	 		if(lst_orderamountList!=null&&lst_orderamountList.size()>0){
 	 			List<Map<String, Object>> rt_map = new ArrayList<Map<String,Object>>();
 	 			for(Map<String, Object> o:lst_orderamountList){
 	 				Map<String, Object> rt_o = new HashMap<String, Object>();
 	 				rt_o.put("name", o.get("channel_name"));
 	 				rt_o.put("value", o.get("totalamount"));
 	 				rt_map.add(rt_o);
 	 			}
 	 			return rt_map;
 	 		}
 	 		return null;
 	 	}
 	 	
 	 	
 	 	@Override
 	 	public Map<String, Object> querystoreamountbyemployeeno(String employee_no){
 	 		Map<String, Object> ret_map = new HashMap<String, Object>();
 	 		
 	 		HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
 	 		Humanresources humanresources = humanresourcesManager.getEmployeeInfoByEmployeeNo(employee_no);
 	 		String storeno = "";
 	 		String career_group = "";
 	 		if(humanresources!=null&&humanresources.getStore_id()!=null){
 	 			career_group = humanresources.getCareer_group();
 	 			StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
 	 			Store store = storeManager.findStore(humanresources.getStore_id());
 	 			storeno = store.getStoreno();
 	 		}
 	 		DsAbnormalOrderDao dsAbnormalOrderDao = (DsAbnormalOrderDao) SpringHelper.getBean(DsAbnormalOrderDao.class.getName());
 	 		Calendar cal = Calendar.getInstance(); 
 			int year = cal.get(Calendar.YEAR);
 			int month = cal.get(Calendar.MONTH)+1;
 			
 			//拉新客户
 			Map<String, Object> obj_addcust = dsAbnormalOrderDao.querynewaddcusbystoreno(year, month, storeno);
 			String retaddcust = "0";
 			if(obj_addcust!=null){
 	 			retaddcust = obj_addcust.get("new_count")==null?"0":obj_addcust.get("new_count").toString();
 			}
 			//门店营业额
 			Map<String, Object> obj_amount = dsAbnormalOrderDao.queryorderamountbystoreno(year, month, storeno);
 			String retamount = "0";
 			if(obj_amount!=null){
 				retamount = obj_amount.get("order_amount")==null?"0":obj_amount.get("order_amount").toString();
 			}
 			//门店事业群营业额
 			String retcareergroup = "0";
 			if(career_group!=null&&career_group.length()>0){
 				Map<String, Object> obj_careergroup = dsAbnormalOrderDao.queryorderamountbycareergroup(year, month, storeno, career_group);
 	 			if(obj_careergroup!=null){
 	 				retcareergroup = obj_careergroup.get("order_amount_career")==null?"0":obj_careergroup.get("order_amount_career").toString();
 	 			}
 			}
 			
 			ret_map.put("retaddcust", retaddcust);
 			ret_map.put("retamount", retamount);
 			ret_map.put("retcareergroup", retcareergroup);
 			
 	 		return ret_map;
 	 	}

		@Override
		public Map<String, Object> queryTradeByEshop(DynamicDto dd) {
			Map<String,Object> result = new HashMap<String,Object>();
			OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
			List<Map<String,Object>> dynamicList = orderDao.queryTradeByEshop(dd);
			if(!dynamicList.isEmpty()){
				JSONArray json = new JSONArray();
		        for(Map<String,Object> storeMonthGMV : dynamicList){
		            JSONObject jo = new JSONObject();
		            jo.put("eshop_name", storeMonthGMV.get("eshop_name"));
		            jo.put("order_amount", storeMonthGMV.get("order_amount"));
		            json.put(jo);
		        }
		        String gmv = json.toString();
		        result.put("gmv", gmv);
			}
			return result;
		}
		@Override
		public Map<String, Object> queryCustomerCount(DynamicDto dd,PageInfo pageInfo,String sign) {
			Long city_id = dd.getCityId();
			String province_id = dd.getProvinceId();
			List<Map<String, Object>> cityNO = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> provinceNO = new ArrayList<Map<String,Object>>();
			Map<String,Object> customercList = new HashMap<String, Object>();
			Map<String,Object> dynamicAllMap = null;
			Map<String,Object> result = new HashMap<String,Object>();
			StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
			OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
			DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
			List<Map<String, Object>> cityList = dynamicDao.selectAllCity();
			if(city_id!=null){
				cityNO = storeDao.getCityNOOfCityById(city_id);
			}
			if(province_id!=null&&province_id!=""){
				provinceNO = storeDao.getProvinceNOOfCSZJ(province_id);
			}
			customercList = orderDao.queryStoreCustmerCount(dd,cityNO,provinceNO,pageInfo);
			DynamicDto dd1 = dd;
			dd1.setCityId(null);
			dd1.setProvinceId(null);
			dynamicAllMap = orderDao.queryStoreCustmerCount(dd1,null,null,null);
			customercList.put("cityList", cityList);
			String gmv = this.getRankUserDataJson(customercList,dynamicAllMap,dd,sign).toString();
			result.put("gmv", gmv);
			result.put("pageinfo", customercList.get("pageinfo"));
			result.put("total_pages", customercList.get("total_pages"));
			result.put("cityList", cityList);;
			return result;
		}
		public JSONArray getRankUserDataJson(Map<String,Object> dynamicMap,Map<String,Object> dynamicAllMap,DynamicDto ddDto,String sign){
			JSONArray json = new JSONArray();
			OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
			List<Map<String,Object>> dynamicList = null;
			List<Map<String,Object>> dynamicAllList = null;
			Map<String,Object> dynamicByCity = null;
			Map<String,Map<String,Object>> dynamicByCityMap = new HashMap<String, Map<String,Object>>();
			List<Map<String,Object>> dynamicByCityList = new ArrayList<Map<String,Object>>();
			dynamicList = (List<Map<String, Object>>) dynamicMap.get("gmv");
			dynamicAllList = (List<Map<String, Object>>) dynamicAllMap.get("gmv");
			List<Map<String, List<Map<String, Object>>>> citycodeList = new ArrayList<Map<String,List<Map<String,Object>>>>();
			if(dynamicList.size() == dynamicAllList.size()){
				for (int i = 0; i < dynamicList.size(); i++) {
					JSONObject jo = new JSONObject();
					Map<String,Object> dynamic = (Map<String,Object>)dynamicList.get(i);
					jo.put("customer_count", dynamic.get("customer_count"));
					jo.put("store_name", dynamic.get("store_name"));
					jo.put("city_code", dynamic.get("city_code"));
		            jo.put("store_id", dynamic.get("store_id"));
		            if(sign!=null){
		            	jo.put("cityrank", i+1);
		            	jo.put("rank", i+1);
		            }
		            json.put(jo);
				}
			}else{
				for (int i = 0; i < dynamicList.size(); i++) {
					JSONObject jo = new JSONObject();
					Map<String,Object> dynamic = (Map<String,Object>)dynamicList.get(i);
					jo.put("customer_count", dynamic.get("customer_count"));
					jo.put("store_name", dynamic.get("store_name"));
					jo.put("city_code", dynamic.get("city_code"));
		            jo.put("store_id", dynamic.get("store_id"));
		            if(sign!=null){
		            	for (int j = 0; j < dynamicAllList.size(); j++) {
							if(dynamic.get("store_name").toString().equals(dynamicAllList.get(j).get("store_name").toString())){
								jo.put("rank", j+1);
							}
						}
						List<Map<String, Object>> cityNO = new ArrayList<Map<String,Object>>();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("cityno", dynamic.get("city_code"));
						cityNO.add(map);
						if(dynamicByCityMap.get(String.valueOf(dynamic.get("city_code")))==null){
							dynamicByCity = (Map<String, Object>) orderDao.queryStoreCustmerCount(ddDto,cityNO,null,null);
							dynamicByCityMap.put(String.valueOf(dynamic.get("city_code")), dynamicByCity);
						}
						dynamicByCityList = (List<Map<String, Object>>)dynamicByCityMap.get(dynamic.get("city_code")).get("gmv");
						for(int k = 0; k < dynamicByCityList.size(); k++){
							if(dynamic.get("store_name").toString().equals(dynamicByCityList.get(k).get("store_name").toString())){
								jo.put("cityrank", k+1);
							}
						}
		            }
					json.put(jo);
				}
			}
			return json;
		}
}
