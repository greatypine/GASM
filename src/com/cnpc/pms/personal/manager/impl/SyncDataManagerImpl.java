package com.cnpc.pms.personal.manager.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.dao.YyMicrDataDao;
import com.cnpc.pms.personal.dao.YyStoreDao;
import com.cnpc.pms.personal.dto.YyHouseDTO;
import com.cnpc.pms.personal.entity.YyMicrData;
import com.cnpc.pms.personal.entity.YyStore;
import com.cnpc.pms.personal.manager.SyncDataManager;
import com.cnpc.pms.personal.manager.YyStoreManager;

/**
 * 数据同步实现类
 */
@SuppressWarnings("all")
public class SyncDataManagerImpl extends BaseManagerImpl implements SyncDataManager{
	
	/**
	 * 数据同步 daqWeb >>> gasqDW_OFF
	 */
	@Override
	public Result syncDatagasqDWOFFSet() {
		Result result = new Result();
		String fileUrl = PropertiesUtil.getValue("shfile.url");
		//String teString = "cmd /c start D:\\ipc.bat";
		int i=1;
		try {
			i = Runtime.getRuntime().exec(fileUrl).waitFor();
		} catch (InterruptedException e) {
			result.setMessage("中断故障(异常)!");
		} catch (IOException e) {
			result.setMessage("不能读取配置文件！");
		}
		result.setCode(i);
		return result;
	}
	
	
	
	/**
	 * 数据同步 daqWeb >>> daqWeb.sql
	 */
	@Override
	public Result syncBackUpDaqWeb() {
		Result result = new Result();
		String fileUrl = PropertiesUtil.getValue("dbfile.url");
		int i=1;
		try {
			i = Runtime.getRuntime().exec(fileUrl).waitFor();
		} catch (InterruptedException e) {
			result.setMessage("中断故障(异常)!");
		} catch (IOException e) {
			result.setMessage("不能读取配置文件！");
		}
		result.setCode(i);
		return result;
	}
	
}
