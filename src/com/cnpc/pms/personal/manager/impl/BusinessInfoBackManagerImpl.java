package com.cnpc.pms.personal.manager.impl;

import java.util.List;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.dao.NewsDao;
import com.cnpc.pms.personal.entity.BusinessInfoBack;
import com.cnpc.pms.personal.entity.BusinessType;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.News;
import com.cnpc.pms.personal.manager.BusinessInfoBackManager;
import com.cnpc.pms.personal.manager.BusinessTypeManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.NewsManager;

public class BusinessInfoBackManagerImpl extends BizBaseCommonManager implements BusinessInfoBackManager{

	@Override
	public BusinessInfoBack saveBusinessInfoBack(BusinessInfoBack businessInfoBack) {
		User sessionUser = null;
		if (null != SessionManager.getUserSession()
				&& null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession()
					.getSessionData().get("user");
		}
		BusinessInfoBack infoBack = getBusinessInfoBackBybusiness_id(businessInfoBack.getBusiness_id());
		if(infoBack==null){
			infoBack=new BusinessInfoBack();
		}
		infoBack.setBusiness_id(businessInfoBack.getBusiness_id());
		infoBack.setAddress(businessInfoBack.getAddress());
		infoBack.setBusiness_name(businessInfoBack.getBusiness_name());
		infoBack.setEnd_business_house(businessInfoBack.getEnd_business_house());
		infoBack.setStart_business_house(businessInfoBack.getStart_business_house());
		BusinessTypeManager businessTypeManager=(BusinessTypeManager)SpringHelper.getBean("businessTypeManager");
		BusinessType condition = businessTypeManager.getBusinessTypeByCondition(businessInfoBack.getTwo_level_code(),businessInfoBack.getThree_level_code(),businessInfoBack.getFour_level_code(),businessInfoBack.getFive_level_code());
		infoBack.setEmployee_no(sessionUser.getEmployeeId());
		infoBack.setVillage_id(businessInfoBack.getVillage_id());
		infoBack.setTown_id(businessInfoBack.getTown_id());
		infoBack.setTwo_level_index(condition.getLevel1_name());
		infoBack.setThree_level_index(condition.getLevel2_name());
		infoBack.setFour_level_index(condition.getLevel3_name());
		infoBack.setFive_level_index(condition.getLevel4_name());
		infoBack.setTwo_level_code(condition.getLevel1_code());
		infoBack.setThree_level_code(condition.getLevel2_code());
		infoBack.setFour_level_code(condition.getLevel3_code());
		infoBack.setFive_level_code(condition.getLevel4_code());
		infoBack.setType_id(condition.getId());
		infoBack.setRange_eshop(businessInfoBack.getRange_eshop());
		infoBack.setShop_area(businessInfoBack.getShop_area());
		infoBack.setIsdistribution(businessInfoBack.getIsdistribution());
		infoBack.setJob(businessInfoBack.getJob());
		preObject(infoBack);
		BusinessInfoBackManager businessInfoBackManager=(BusinessInfoBackManager)SpringHelper.getBean("businessInfoBackManager");
		businessInfoBackManager.saveObject(infoBack);
		NewsManager newsManager=(NewsManager)SpringHelper.getBean("newsManager");
		News news = newsManager.getNewsBy(3, infoBack.getBusiness_id());
		if(news==null){
			news=new News();
		}
		news.setKey_id(infoBack.getBusiness_id());
		news.setName(infoBack.getBusiness_name());
		news.setType_id(3);
		news.setEmployee_no(sessionUser.getEmployeeId());
		preObject(news);
		newsManager.saveObject(news);
		return infoBack;
	}

	@Override
	public BusinessInfoBack getBusinessInfoBackBybusiness_id(Long business_id) {
		 List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("business_id",business_id));
	        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
	        	BusinessInfoBack businessInfoBack=	(BusinessInfoBack)lst_vilage_data.get(0);
	        	if(businessInfoBack!=null){
	   			 if(businessInfoBack.getJob()!=null&&!"".equals(businessInfoBack.getJob())){
	   	        	 CustomerManager customerManager=(CustomerManager)SpringHelper.getBean("customerManager");
	   	        	 String stts="";
	   	        	 String[] split = businessInfoBack.getJob().split(",");
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
	   	        	businessInfoBack.setShowSelectName(stts.substring(1,stts.length()));
	   	         }
	   			
	   			
	   			return businessInfoBack;
	   		}
	        }
	        return null;
	}

	@Override
	public void deleteObject(Long business_Id) {
		BusinessInfoBackManager businessInfoBackManager=(BusinessInfoBackManager)SpringHelper.getBean("businessInfoBackManager");
		BusinessInfoBack bybusiness_id = businessInfoBackManager.getBusinessInfoBackBybusiness_id(business_Id);
		NewsDao newsDao=(NewsDao)SpringHelper.getBean(NewsDao.class.getName());
		newsDao.deleteObject(bybusiness_id);
		NewsManager newsManager=(NewsManager)SpringHelper.getBean("newsManager");
		News news = newsManager.getNewsBy(3, business_Id);
		newsDao.deleteObject(news);
	}
	
	
	

}
