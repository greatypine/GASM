package com.cnpc.pms.bizbase.rbac.usermanage.manager.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserLoginInfo;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserLoginInfoManager;
//import com.yadea.crm.common.mail.Mail;
//import com.yadea.crm.common.util.SystemUtil;

/**
 * 用户管理接口实现类 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-7-23
 */
public class UserLoginInfoManagerImpl extends BizBaseCommonManager implements
		UserLoginInfoManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(UserLoginInfoManagerImpl.class);

	/**
	 * 增加一条新的用户登录访问记录
	 * 
	 * @param userDTO
	 *            the user dto
	 * @return the user dto
	 */
	public Long addNewUserLoginInfo(UserLoginInfo userDTO) {
		super.save(userDTO);
		return 1L;
	}
	
	@Override
	public Map<String, String> getTime() {
		Map<String,String> map = new HashMap<String,String>();
		String startTime = "";
		String endTime = "";
		Date date = new Date();
		Long end = date.getTime();
		Long start = end - 60*60*1000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		endTime = sdf.format(date);
		startTime = sdf.format(new Date(start));
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return map;
	}

}