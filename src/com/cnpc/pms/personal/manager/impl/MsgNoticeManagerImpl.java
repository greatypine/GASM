package com.cnpc.pms.personal.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.entity.MsgNotice;
import com.cnpc.pms.personal.manager.MsgNoticeManager;

public class MsgNoticeManagerImpl extends BizBaseCommonManager implements MsgNoticeManager {
	
	@Override
	public List<MsgNotice> queryMsgNoticeList(Long num){
		List<MsgNotice> retList = new ArrayList<MsgNotice>();
		if(num!=null){
			IFilter repFilter =FilterFactory.getSimpleFilter("1=1");
			FSP fsp = new FSP();
			PageInfo pageInfo = new PageInfo();
			pageInfo.setRecordsPerPage(Integer.parseInt(num+""));
			fsp.setPage(pageInfo);
			fsp.setUserFilter(repFilter);
			retList = (List<MsgNotice>)this.getList(fsp); 
		}else{
			retList = (List<MsgNotice>)this.getList();
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
	}

	
	
	
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
