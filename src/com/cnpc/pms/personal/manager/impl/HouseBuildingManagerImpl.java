package com.cnpc.pms.personal.manager.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.dao.HouseDao;
import com.cnpc.pms.personal.entity.HouseBuilding;
import com.cnpc.pms.personal.manager.HouseBuildingManager;

public class HouseBuildingManagerImpl  extends BizBaseCommonManager implements HouseBuildingManager{

	@Override
	public void syncHouseBuilding() {
		HouseDao houseDao=(HouseDao)SpringHelper.getBean(HouseDao.class.getName());
		Date date = new Date();
		User user = null;
		HouseBuilding houseBuilding=null;
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		SimpleDateFormat sdf1 =   new SimpleDateFormat( "yyyy-MM-dd" );
		String format = sdf.format(date);
		String format2 = sdf1.format(date);
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        Date date1= calendar.getTime();  
        String format1 = sdf.format(date1);
        if("2017-12-20".equals(format2)){
        	List<?> lst_data = this.getList();
	        if(lst_data != null && lst_data.size() > 0){
	            for(Object obj:lst_data){
	            	 houseBuilding=	(HouseBuilding)obj;
	            	 user = new User();
	            	 user.setName(houseBuilding.getCreate_user());
	            	 user.setId(houseBuilding.getCreate_user_id());
	            	 houseDao.updateHousetinyvillage(houseBuilding.getBuildingid(), houseBuilding.getTinyvillageId(),user);
	            }
	        }
        }else{
        	List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s')<='"+format+"' and DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s')>'"+format1+"'" ));
	        if(lst_data != null && lst_data.size() > 0){
	            for(Object obj:lst_data){
	            	 houseBuilding=	(HouseBuilding)obj;
	            	 user = new User();
	            	 user.setName(houseBuilding.getCreate_user());
	            	 user.setId(houseBuilding.getCreate_user_id());
	            	 houseDao.updateHousetinyvillage(houseBuilding.getBuildingid(), houseBuilding.getTinyvillageId(),user);
	            }
	        }
        }
		 
	}

}
