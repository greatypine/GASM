package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.SendMessage;
import com.cnpc.pms.personal.manager.SendMessageManager;


public class SendMessageManagerImpl extends BaseManagerImpl implements SendMessageManager {
 
	//保存验证码信息
	@Override
	public SendMessage saveSendMessage(SendMessage sendMessage) {
		preSaveObject(sendMessage);
		saveObject(sendMessage);
		return sendMessage;
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
