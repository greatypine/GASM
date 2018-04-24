/**
 * gaobaolei
 */
package com.cnpc.pms.defaultConfig.manager.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.defaultConfig.dao.DefaultConfigDao;
import com.cnpc.pms.defaultConfig.dto.DefaultConfigDTO;
import com.cnpc.pms.defaultConfig.entity.DefaultConfig;
import com.cnpc.pms.defaultConfig.manager.DefaultConfigManager;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.slice.manager.AreaManager;

/**
 * @author gaobaolei
 *
 */
public class DefaultConfigManagerImpl extends BizBaseCommonManager implements DefaultConfigManager{

	
	@Override
	public DefaultConfigDTO saveOrUpdateDefaultConfig(DefaultConfig config) {
		//只保留最新的默认配置
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 DefaultConfig defaultConfig=null;
		 DefaultConfigDao dConfigDao = (DefaultConfigDao)SpringHelper.getBean(DefaultConfigDao.class.getName());
		 String employee_no = "";
		 if(config.getEmployee_no()==null){
			 employee_no="null";
		 }else{
			 employee_no=config.getEmployee_no();
		 }
		 List<?> lst_store = getList(FilterFactory.getSimpleFilter("employee_no",employee_no).appendAnd(FilterFactory.getSimpleFilter("employee_name",config.getEmployee_name())));
         if(lst_store != null && lst_store.size() > 0){
         	defaultConfig = (DefaultConfig)lst_store.get(0);
         }else{
        	 defaultConfig = new DefaultConfig();
         }
		
		
	     User sessionUser = null;
	     String createUser = "";
	     String duty = "";
		 if(null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User)SessionManager.getUserSession().getSessionData().get("user");
			createUser = sessionUser.getName();
			duty = sessionUser.getUsergroup().getCode();
		 }else{
			 createUser = config.getCreate_user();
			 duty = config.getDuty();
		 }
		 
		 if(lst_store==null||lst_store.size()==0){//新增
			 defaultConfig.setCreate_user(createUser);
			 defaultConfig.setDuty(duty);
			 defaultConfig.setCreate_time(sdf.format(new Date()));
			 defaultConfig.setUpdate_time(sdf.format(new Date()));
			 defaultConfig.setEmployee_no(config.getEmployee_no());
			 defaultConfig.setDefault_system(config.getDefault_system());
			 defaultConfig.setEmployee_name(config.getEmployee_name());
			 defaultConfig.setVersion(1l);
			 dConfigDao.saveDefaultConfig(defaultConfig);
		 }else{
			 defaultConfig.setDuty(duty);
			 defaultConfig.setDefault_system(config.getDefault_system());
			 defaultConfig.setEmployee_no(config.getEmployee_no()==null?"null":config.getEmployee_no());
			 defaultConfig.setEmployee_name(config.getEmployee_name());
			 defaultConfig.setUpdate_time(sdf.format(new Date()));
			 dConfigDao.updateDefaultConfig(defaultConfig);
		 }
		
	     DefaultConfigDTO defaultConfigDTO = new DefaultConfigDTO();
	     BeanUtils.copyProperties(defaultConfig, defaultConfigDTO);
		return defaultConfigDTO;
	}

	
	@Override
	public Map<String, Object> getDefaultConfig(String employee_no,String employee_name) {
		DefaultConfigDao dConfigDao = (DefaultConfigDao)SpringHelper.getBean(DefaultConfigDao.class.getName());
		Map<String, Object> dcMap =  dConfigDao.checkDefaultConfig(employee_no,employee_name);
		
		//判断是否是A国安侠
		AreaManager areaManager = (AreaManager) SpringHelper.getBean("areaManager");
		Map<String, Object> map = areaManager.queryAreaByEmployeeNo(employee_no,null);
		if(map!=null&&dcMap!=null){
			String areaName = map.get("area")==null?"":map.get("area").toString();
			if(areaName!=null&&areaName.length()>0){
				dcMap.put("isA", "1");
			}else{
				dcMap.put("isA", "0");
			}
		}
		return dcMap;
	}


	
	@Override
	public void deleteDefaultConfig(String employee_no) {
		List<?> list_defaultConfig = (List<?>)super.getList(FilterFactory.getSimpleFilter("employee_no",employee_no));
        if(list_defaultConfig != null && list_defaultConfig.size() > 0){
            for(Object dconfig : list_defaultConfig){
                super.removeObject(dconfig);
            }
        }
		
	}

}
