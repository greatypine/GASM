package com.cnpc.pms.personal.util;

import java.util.Date;
import java.util.List;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.StoreManager;

public class DataTransfromUtil {
	/**
	 * 当根据门店街道获取所属城市的时候调用此方法
	 * @param string
	 * @return
	 */
	public static Long TownIdTransFrom(String string){
		if(string!=null&&!"".endsWith(string)){
			String[] strings=string.split(",");
			String strtownId=strings[0];
			return Long.parseLong(strtownId);
		}
		return null;
	};
	/**
	 * 根据门店名称模糊查询获取所有街道的id
	 * @param str
	 * @return
	 */
	public static String getTownIdByStoreName(String str){
		String string="";
		StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
		List<Store> list = storeManager.findStoreListByName(str);
		if(list!=null&&list.size()>0){
			for (Store store : list) {
				String town_id = store.getTown_id();
				if(town_id.length()>0){
					string+=","+town_id;
				}
			}
		}
		if(string.length()>0){
			string=string.substring(1,string.length());
		}
		return string;
	}
	public static void preObject(Object o){
		User sessionUser = null;
		if(null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User)SessionManager.getUserSession().getSessionData().get("user");
		}

		DataEntity commonEntity = (DataEntity)o;
		Date date = new Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		if(null == commonEntity.getCreate_time()) {
			commonEntity.setCreate_time(sdate);
			if(null != sessionUser) {
				commonEntity.setCreate_user_id(sessionUser.getId());
				commonEntity.setCreate_user(sessionUser.getName());
			}
		}

		commonEntity.setUpdate_time(sdate);
		if(null != sessionUser) {
			commonEntity.setUpdate_user_id(sessionUser.getId());
			commonEntity.setUpdate_user(sessionUser.getName());
		}
	}
	

}
