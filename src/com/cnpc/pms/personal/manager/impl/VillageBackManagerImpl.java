package com.cnpc.pms.personal.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.dao.NewsDao;
import com.cnpc.pms.personal.dao.TownDao;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.News;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.entity.VillageBack;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.NewsManager;
import com.cnpc.pms.personal.manager.VillageBackManager;

public class VillageBackManagerImpl extends BizBaseCommonManager implements VillageBackManager{

	@Override
	public VillageBack saveVillageBack(VillageBack villageBack) {
		User sessionUser = null;
		if (null != SessionManager.getUserSession()
				&& null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession()
					.getSessionData().get("user");
		}
		VillageBack back = getVillageBackInfoBytown_id(villageBack.getVillage_id());
		if(back==null){
			back=new VillageBack();
		}
		back.setVillage_id(villageBack.getVillage_id());
		back.setName(villageBack.getName());
		back.setGb_code(villageBack.getGb_code());
		back.setHousehold_number(villageBack.getHousehold_number());
		back.setResident_population_number(villageBack.getResident_population_number());
		back.setSquare_area(villageBack.getSquare_area());
		back.setTown_id(villageBack.getTown_id()==null?0:villageBack.getTown_id());
		back.setCommittee_address(villageBack.getCommittee_address());
		back.setJob(villageBack.getJob());
		back.setEmployee_no(sessionUser.getEmployeeId());
		preObject(back);
		VillageBackManager villageBackManager=(VillageBackManager)SpringHelper.getBean("villageBackManager");
		villageBackManager.saveObject(back);
		NewsManager newsManager=(NewsManager)SpringHelper.getBean("newsManager");
		News news = newsManager.getNewsBy(2, back.getVillage_id());
		if(news==null){
			news=new News();
		}
		news.setKey_id(back.getVillage_id());
		news.setName(back.getName());
		news.setType_id(2);
		news.setEmployee_no(sessionUser.getEmployeeId());
		preObject(news);
		newsManager.saveObject(news);
		return back;
	}

	@Override
	public Map<String, Object> getVillageBackBytown_id(Long village_id) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("village_id",village_id));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
        VillageBack villageBack=(VillageBack) lst_vilage_data.get(0);
         map.put("name", villageBack.getName());
         map.put("gb_code", villageBack.getGb_code());
         map.put("square_area", villageBack.getSquare_area());
         map.put("household_number", villageBack.getHousehold_number());
         map.put("resident_population_number", villageBack.getResident_population_number());
         map.put("committee_address", villageBack.getCommittee_address());
         map.put("job", villageBack.getJob());
         map.put("village_id", villageBack.getVillage_id());
         if(villageBack.getJob()!=null&&!"".equals(villageBack.getJob())){
        	 CustomerManager customerManager=(CustomerManager)SpringHelper.getBean("customerManager");
        	 String stts="";
        	 String[] split = villageBack.getJob().split(",");
        	 if(split.length>0){
        		 for (int i = 0; i < split.length; i++) {
        			 String[] strings = split[i].split("-");
        			 Customer customer = customerManager.findCustomerById(Long.parseLong(strings[0]));
        			 if("请选择".equals(strings[1])){
        				 stts +=","+customer.getName();
        			 }else{
        				 stts +=","+customer.getName()+"("+strings[1]+")";
        			 }
        			
        		 }
        	 } 
        	 map.put("showSelectName", stts.substring(1,stts.length())); 
         }
         TownDao townDao=(TownDao)SpringHelper.getBean(TownDao.class.getName());
         if(villageBack.getTown_id()!=null&&villageBack.getTown_id()>0){
        	 Town town = townDao.getTownByID(villageBack.getTown_id());
        	 if(town!=null){
        		 map.put("town_gb_code", town.getGb_code());
        		 map.put("town_id", town.getId());
            	 map.put("town_name", town.getName());
        	 }
         }
         
        }
        return map;
	}

	@Override
	public void deleteObject(Long village_id) {
		VillageBackManager villageBackManager=(VillageBackManager)SpringHelper.getBean("villageBackManager");
		 VillageBack villageBack = villageBackManager.getVillageBackInfoBytown_id(village_id);
		NewsDao newsDao=(NewsDao)SpringHelper.getBean(NewsDao.class.getName());
		newsDao.deleteObject(villageBack);
		NewsManager newsManager=(NewsManager)SpringHelper.getBean("newsManager");
		News news = newsManager.getNewsBy(2, village_id);
		newsDao.deleteObject(news);
	}

	@Override
	public VillageBack getVillageBackInfoBytown_id(Long village_id) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("village_id",village_id));
		if(list!=null&&list.size()>0){
			return (VillageBack)list.get(0);
		}
		return null;
	}

}
