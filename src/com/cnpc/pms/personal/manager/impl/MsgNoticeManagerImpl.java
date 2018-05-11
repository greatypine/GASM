package com.cnpc.pms.personal.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.MsgNoticeDao;
import com.cnpc.pms.personal.entity.MsgNotice;
import com.cnpc.pms.personal.manager.MsgNoticeManager;

public class MsgNoticeManagerImpl extends BizBaseCommonManager implements MsgNoticeManager {
	
	@Override
	public List<Map<String, Object>> queryMsgNoticeList(Long num){
		List<Map<String, Object>> retList = new ArrayList<Map<String,Object>>();
		MsgNoticeDao msgNoticeDao = (MsgNoticeDao) SpringHelper.getBean(MsgNoticeDao.class.getName());
		UserManager  userManager = (UserManager) SpringHelper.getBean("userManager");
		UserDTO userDTO = userManager.getCurrentUserDTO();
		if(num!=null){
			//查询通知表t_notice t_notice_receiver
			retList = msgNoticeDao.queryNoticeList(num,userDTO.getEmployeeId(),userDTO.getStore_id());
		}else{
			retList = msgNoticeDao.queryNoticeList(null,userDTO.getEmployeeId(),userDTO.getStore_id());
		}
		
		return retList;
	}
	
	
	@Override
	public MsgNotice queryMsgNoticeById(Long id){
		MsgNotice msgNotice = (MsgNotice) this.getObject(id); 
		return msgNotice;
	}
	
	
	
	@Override
	public MsgNotice saveMsgNotice(MsgNotice msgNotice) {
		preSaveObject(msgNotice);
		this.saveObject(msgNotice);
		return msgNotice;
	}
	
	@Override
	public void updateNoticeReadByNo(Long id){
		MsgNotice msgNotice = (MsgNotice) this.getObject(id);
		MsgNoticeDao msgNoticeDao = (MsgNoticeDao) SpringHelper.getBean(MsgNoticeDao.class.getName());
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		String noticeNo = msgNotice.getNoticeNo();
		String employee_no = userManager.getCurrentUserDTO().getEmployeeId();
		msgNoticeDao.updateNoticeReadByNo(noticeNo, employee_no);
	}
	
	
	/*@Override
	public MsgNotice updateMsgNotice(MsgNotice msgNotice) {
		MsgNotice updateMsgNotice = null;
		if(msgNotice!=null&&msgNotice.getId()!=null){
			updateMsgNotice=(MsgNotice) this.getObject(msgNotice.getId());
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			updateMsgNotice.setTitle(msgNotice.getTitle());
			updateMsgNotice.setContent(msgNotice.getContent());
			updateMsgNotice.setNotice_date(msgNotice.getNotice_date());
			updateMsgNotice.setNotice_user(userManager.getCurrentUserDTO().getName());
			preSaveObject(updateMsgNotice);
			this.saveObject(updateMsgNotice);
		}
		return updateMsgNotice;
	}*/

	
	
	
	protected void preSaveObject(Object o) {
		if (o instanceof DataEntity) {
			User sessionUser = null;
			if (null != SessionManager.getUserSession()
					&& null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession()
						.getSessionData().get("user");
			}
			DataEntity dataEntity = (DataEntity) o;
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			// insert处理时添加创建人和创建时间
			if (null == dataEntity.getCreate_time()) {
				dataEntity.setCreate_time(sdate);
				if (null != sessionUser) {
					dataEntity.setCreate_user(sessionUser.getCode());
					dataEntity.setCreate_user_id(sessionUser.getId());
				}
			}
			dataEntity.setUpdate_time(sdate);
			if (null != sessionUser) {
				dataEntity.setUpdate_user(sessionUser.getCode());
				dataEntity.setUpdate_user_id(sessionUser.getId());
			}
		}
	}
	
}
