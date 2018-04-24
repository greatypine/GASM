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
import com.cnpc.pms.personal.entity.BusinessInfoBack;
import com.cnpc.pms.personal.entity.County;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.News;
import com.cnpc.pms.personal.entity.TownBack;
import com.cnpc.pms.personal.manager.BusinessInfoBackManager;
import com.cnpc.pms.personal.manager.CountyManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.NewsManager;
import com.cnpc.pms.personal.manager.TownBackManager;

public class TownBackManagerImpl extends BizBaseCommonManager implements TownBackManager {



	@Override
	public Map<String, Object> getTownBackBytown_id(Long town_id) {
		Map<String,Object> map=new HashMap<String,Object>();
		List<?> list = this.getList(FilterFactory.getSimpleFilter("town_id",town_id));
		CountyManager countyManager=(CountyManager)SpringHelper.getBean("countyManager");
		if(list!=null&&list.size()>0){
			TownBack townBack=(TownBack)list.get(0);
			if(townBack.getJob()!=null&&!"".equals(townBack.getJob())){
	        	 CustomerManager customerManager=(CustomerManager)SpringHelper.getBean("customerManager");
	        	 String stts="";
	        	 String[] split = townBack.getJob().split(",");
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
	        	 map.put("showSelectName",stts.substring(1,stts.length()));
	        	
	         }
			if(townBack.getCounty_id()!=null&&!"".equals(townBack.getCounty_id())){
       		 County county = countyManager.getCountyById(townBack.getCounty_id());
       		 map.put("county_name",county.getName());
       		 map.put("county_gb_code", county.getGb_code());
       	 	}
			map.put("name", townBack.getName());
        	map.put("gb_code", townBack.getGb_code());
        	map.put("job", townBack.getJob());
 			map.put("county_id",townBack.getCounty_id() );
 			map.put("square_area",townBack.getSquare_area() );
 			map.put("introduction",townBack.getIntroduction() );
 			map.put("household_number",townBack.getHousehold_number() );
 			map.put("resident_population_number",townBack.getResident_population_number());
 			map.put("town_id",townBack.getTown_id());
			
			return map;
		}
		return null;
	}

	@Override
	public void deleteObject(Long town_id) {
		TownBackManager townBackManager=(TownBackManager)SpringHelper.getBean("townBackManager");
		 TownBack townBack = townBackManager.getTownBackInfoBytown_id(town_id);
		NewsDao newsDao=(NewsDao)SpringHelper.getBean(NewsDao.class.getName());
		newsDao.deleteObject(townBack);
		NewsManager newsManager=(NewsManager)SpringHelper.getBean("newsManager");
		News news = newsManager.getNewsBy(1, town_id);
		newsDao.deleteObject(news);
	}

	@Override
	public TownBack saveTownBack(TownBack townBack) {
		User sessionUser = null;
		if (null != SessionManager.getUserSession()
				&& null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession()
					.getSessionData().get("user");
		}
		TownBack back = getTownBackInfoBytown_id(townBack.getTown_id());
		if(back==null){
			back=new TownBack();
		}
		back.setTown_id(townBack.getTown_id());
		back.setName(townBack.getName());
		back.setGb_code(townBack.getGb_code());
		back.setCounty_id(townBack.getCounty_id());
		back.setIntroduction(townBack.getIntroduction());
		back.setSquare_area(townBack.getSquare_area());
		back.setHousehold_number(townBack.getHousehold_number());
		back.setResident_population_number(townBack.getResident_population_number());
		back.setJob(townBack.getJob());
		back.setEmployee_no(sessionUser.getEmployeeId());
		preObject(back);
		TownBackManager townBackManager=(TownBackManager)SpringHelper.getBean("townBackManager");
		townBackManager.saveObject(back);
		NewsManager newsManager=(NewsManager)SpringHelper.getBean("newsManager");
		News news = newsManager.getNewsBy(1, back.getTown_id());
		if(news==null){
			news=new News();
		}
		news.setKey_id(back.getTown_id());
		news.setName(back.getName());
		news.setType_id(1);
		news.setEmployee_no(sessionUser.getEmployeeId());
		preObject(news);
		newsManager.saveObject(news);
		return back;
	}

	@Override
	public TownBack getTownBackInfoBytown_id(Long town_id) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("town_id",town_id));
		if(list!=null&&list.size()>0){
			return (TownBack)list.get(0);
		}
		return null;
	}

}
