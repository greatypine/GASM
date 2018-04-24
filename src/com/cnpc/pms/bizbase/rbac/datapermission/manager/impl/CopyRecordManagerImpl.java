package com.cnpc.pms.bizbase.rbac.datapermission.manager.impl;

import java.util.List;

import com.cnpc.pms.base.entity.PMSAuditEntity;
import com.cnpc.pms.base.exception.PMSManagerException;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.datapermission.entity.CopyRecord;
import com.cnpc.pms.bizbase.rbac.datapermission.manager.CopyRecordManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;

public class CopyRecordManagerImpl extends BizBaseCommonManager implements CopyRecordManager {
	
	
	@SuppressWarnings("unchecked")
	public void saveCopyRecord(String usercodes, Long sheetId, String sheetType) {
		if(usercodes!=null&&!"".equals(usercodes.trim())) {
			String[] uscodes = usercodes.split(",");
			for(int i=0;i<uscodes.length;++i) {
				//首先进行排重处理(同一条业务，同一个人即视为重复)
				FSP fsp = new FSP();
				fsp.setUserFilter(FilterFactory.getSimpleFilter(" sheetId=" + sheetId + " and userCode = '" + uscodes[i] + "' and sheetStatus = '0' "));
				List rlist = this.getObjects(fsp);
				if(rlist!=null&&rlist.size()>0) {
					continue;
				}
				
				CopyRecord record = new CopyRecord();
				record.setSheetId(sheetId);
				record.setUserCode(uscodes[i]);
				record.setSheetType(sheetType);
				record.setSheetStatus("0");
				preSaveObject(record);
				this.saveObject(record);
			}
		}else {
			throw new PMSManagerException("接受人不能为空，保存失败。");
		}
	}

	public void deleteCopyRecord(Long sheetId) {
		if(sheetId==null) {
			throw new PMSManagerException("业务id不能为空，删除失败。");
		}
		UserDAO userDAO = (UserDAO)SpringHelper.getBean("com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO");
		try {
			userDAO.deleteRecords(sheetId);
		} catch (Exception e) {
			throw new PMSManagerException("删除失败,请联系苦逼的开发人员！");
		}
		
	}
	
	public List<User> getUsersBySheetId(Long sheetId) {
		UserDAO userDAO = (UserDAO)SpringHelper.getBean("com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO");
		List<User> list = null;
		try {
			list = userDAO.getUsersBySheetId(sheetId);
		} catch (Exception e) {
			throw new PMSManagerException("获取人员列表失败,请联系苦逼的开发人员！");
		}
		return list;
	}
}
