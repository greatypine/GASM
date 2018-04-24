package com.cnpc.pms.personal.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.AddressRelevanceDao;
import com.cnpc.pms.personal.dao.VillageDao;
import com.cnpc.pms.personal.entity.AddressRelevance;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.AddressRelevanceManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.platform.dao.OrderAddressDao;

public class AddressRelevanceManagerImpl extends BizBaseCommonManager implements AddressRelevanceManager{

	@Override
	public void syncOrderAddress() {
		AddressRelevanceManager addressRelevanceManager=(AddressRelevanceManager)SpringHelper.getBean("addressRelevanceManager");
		OrderAddressDao orderAddressDao =(OrderAddressDao)SpringHelper.getBean(OrderAddressDao.class.getName());
		List<Map<String, Object>> orderAddress = orderAddressDao.getOrderAddress();
		if(orderAddress!=null&&orderAddress.size()>0){
			for (Map<String, Object> map : orderAddress) {
				String ad_code= map.get("ad_code")+"";
				String placename = map.get("placename")+"";
				String ad_name = map.get("ad_name")+"";
				String city_code = map.get("city_code")+"";
				String city_name = map.get("city_name")+"";
				String province_code = map.get("province_code")+"";
				String province_name = map.get("province_name")+"";
				String code = map.get("code")+"";
				Double latitude =null;
				Double longitude =null;
				if(map.get("latitude")!=null){
					 latitude = Double.valueOf(map.get("latitude")+"");
					 longitude = Double.valueOf(map.get("longitude")+"");
				}
				AddressRelevance addressRelevance = findAddressRelevanceBystorenoAndPlaceName(code,placename);
				if(addressRelevance==null){
					addressRelevance=new AddressRelevance();
					addressRelevance.setCity_code(city_code);
					addressRelevance.setCity_name(city_name);
					addressRelevance.setPlacename(placename);
					addressRelevance.setProvince_code(province_code);
					addressRelevance.setProvince_name(province_name);
					addressRelevance.setCounty_code(ad_code);
					addressRelevance.setCounty_name(ad_name);
					addressRelevance.setStoreno(code);
					if(map.get("latitude")!=null){
						addressRelevance.setLatitude(latitude);
						addressRelevance.setLongitude(longitude);
					}
					addressRelevanceManager.saveObject(addressRelevance);
				}else{
					if(map.get("latitude")!=null){
						addressRelevance.setLatitude(latitude);
						addressRelevance.setLongitude(longitude);
						addressRelevanceManager.saveObject(addressRelevance);
					}
					
				}
			}
			
		}
	}

	
	@Override
	public AddressRelevance findAddressRelevanceByCountyIdAndPlaceName(String ad_code, String placename) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("county_code = '"+ad_code + "' and placename = '"+placename+"'"));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0){
           return (AddressRelevance)lst_vilage_data.get(0);
        }
        return null;
	}

	@Override
	public List<AddressRelevance> findAddressRelevanceByIds(String ids) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("id in("+ids+")"));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0){
           return (List<AddressRelevance>)lst_vilage_data;
        }
        return null;
	}

	@Override
	public Map<String, Object> showAddressRelevanceData(QueryConditions conditions) {
		AddressRelevanceDao addressRelevanceDao = (AddressRelevanceDao) SpringHelper.getBean(AddressRelevanceDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();

        for (Map<String, Object> map_where : conditions.getConditions()) {
            if ("city_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND city_name like '").append(map_where.get("value")).append("'");
            }
            if ("county_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND county_name like '").append(map_where.get("value")).append("'");
			}
            if ("placename".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND placename like '").append(map_where.get("value")).append("'");
			}
            if ("tinyvillage_id".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" and id not in (SELECT address_relevance_id from t_comnunity_block WHERE communityId!= "+map_where.get("value")+") ") ;
			}
        }
        User sessionUser = null;
		if (null != SessionManager.getUserSession()
				&& null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession()
					.getSessionData().get("user");
		}
		UserManager userManager=(UserManager)SpringHelper.getBean("userManager");
		sessionUser=userManager.findUserById(sessionUser.getId());
		StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(sessionUser.getStore_id());
		if(3224==sessionUser.getUsergroup().getId()){
			if(store!=null){
				sb_where.append(" AND storeno='").append(store.getStoreno()).append("'");
				
			}else{
				sb_where.append(" AND 1=0");
			}
			
		}else if(3231==sessionUser.getUsergroup().getId()||3223==sessionUser.getUsergroup().getId()||3225==sessionUser.getUsergroup().getId()||3229==sessionUser.getUsergroup().getId()){
			if(store!=null){
				sb_where.append(" AND storeno='").append(store.getStoreno()).append("'");
			}else{
				sb_where.append(" AND 1=0");
			}
		}else if(sessionUser.getZw().equals("地址采集")||sessionUser.getCode().equals("sunning1")){
			sb_where.append(" and 1=1 ");
		}else{
			sb_where.append(" and 0=1 ");
		}
        
        System.out.println(sb_where);
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "片区信息");
        map_result.put("data", addressRelevanceDao.getAddressRelevanceList(sb_where.toString(), obj_page));
        return map_result;
	}

	@Override
	public AddressRelevance findAddressRelevanceById(Long id) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("id="+id));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0){
           return (AddressRelevance)lst_vilage_data.get(0);
        }
        return null;
	}

	@Override
	public void UpdateNUmber(String pids) {
		AddressRelevanceDao addressRelevanceDao =(AddressRelevanceDao)SpringHelper.getBean(AddressRelevanceDao.class.getName());
		addressRelevanceDao.upxuhao(pids);
	}


	@Override
	public void syncdeleteOrderAddress() {
		AddressRelevanceDao addressRelevanceDao =(AddressRelevanceDao)SpringHelper.getBean(AddressRelevanceDao.class.getName());
		addressRelevanceDao.deleteXiangtong();
		addressRelevanceDao.deleteComnunityAreaData();
	}


	@Override
	public Map<String, Object> showOrderAddressRelevanceData(QueryConditions conditions) {
		AddressRelevanceDao addressRelevanceDao = (AddressRelevanceDao) SpringHelper.getBean(AddressRelevanceDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();

        for (Map<String, Object> map_where : conditions.getConditions()) {
            if ("village_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND vill.name like '").append(map_where.get("value")).append("'");
            }
            if ("tinyvillage_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND tiny.name like '").append(map_where.get("value")).append("'");
			}
            if ("placename".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND reaa.placename like '").append(map_where.get("value")).append("'");
			}
            if("communityAreastatus".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))){
            	if(map_where.get("value").equals("1")){
					sb_where.append(" AND tiny.`name` is not NULL");
				}else if(map_where.get("value").equals("2")){
					sb_where.append(" AND tiny.`name` is NULL");
				}
            }
            if("publicarea".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))){
            	if(map_where.get("value").equals("1")){
					sb_where.append(" AND reaa.publicarea=1");
				}else if(map_where.get("value").equals("2")){
					sb_where.append(" AND (reaa.publicarea=0 or reaa.publicarea is null)");
				}
            }
            
            
        }
        User sessionUser = null;
		if (null != SessionManager.getUserSession()
				&& null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession()
					.getSessionData().get("user");
		}
		UserManager userManager=(UserManager)SpringHelper.getBean("userManager");
		sessionUser=userManager.findUserById(sessionUser.getId());
		StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(sessionUser.getStore_id());
		if(3224==sessionUser.getUsergroup().getId()){
			if(store!=null){
				sb_where.append(" AND reaa.storeno='").append(store.getStoreno()).append("'");
				
			}else{
				sb_where.append(" AND 1=0");
			}
			
		}else if(3231==sessionUser.getUsergroup().getId()||3223==sessionUser.getUsergroup().getId()||3225==sessionUser.getUsergroup().getId()||3229==sessionUser.getUsergroup().getId()){
			if(store!=null){
				sb_where.append(" AND storeno='").append(store.getStoreno()).append("'");
			}else{
				sb_where.append(" AND 1=0");
			}
		}else if(sessionUser.getZw().equals("地址采集")||sessionUser.getCode().equals("sunning1")){
			sb_where.append(" and 1=1 ");
		}else{
			sb_where.append(" and 0=1 ");
		}
        
        System.out.println(sb_where);
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "订单区块列表");
        map_result.put("data", addressRelevanceDao.getOrderAddressRelevanceList(sb_where.toString(), obj_page));
        return map_result;
	}


	@Override
	public AddressRelevance findAddressRelevanceBystorenoAndPlaceName(String storeno, String placename) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("storeno = '"+storeno + "' and placename = '"+placename+"'"));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0){
           return (AddressRelevance)lst_vilage_data.get(0);
        }
        return null;
	}


	@Override
	public List<AddressRelevance> getAddressRelevanceDataByStoreNo(String storeno) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("storeno ='"+storeno+"'"));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0){
           return (List<AddressRelevance>)lst_vilage_data;
        }
        return null;
	}

}
