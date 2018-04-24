package com.cnpc.pms.utils;

import java.util.Date;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.ContextLoader;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.messageModel.dao.MessageNewDao;
import com.cnpc.pms.messageModel.entity.Message;
import com.cnpc.pms.messageModel.manager.MessageNewManager;
import com.cnpc.pms.personal.entity.WorkRecordTotal;

public class MessagePushThread implements Runnable {

	private Message appMessage;
	private WorkRecordTotal save_workRecordTotal;
	private User submit_user;
	private User current_user;
	private MessageNewDao messageNewDao;
	private MessageNewManager appMessageManager;
	public MessagePushThread(Message pushMessage,WorkRecordTotal workRecordTotal,User user,User cur_user,MessageNewDao newDao,MessageNewManager newManager){
		appMessage = pushMessage;
		save_workRecordTotal = workRecordTotal;
		submit_user = user;
		current_user = cur_user;
		messageNewDao = newDao;
		appMessageManager = newManager;
	}
	
	@Override
	public void run() {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		PlatformTransactionManager txManager = (PlatformTransactionManager) SpringHelper.getBean("transactionManager");
		TransactionStatus status = txManager.getTransaction(def);
		appMessage.setPk_id(save_workRecordTotal.getId());
		appMessage.setJump_path("work_record_result");
		appMessage.setType("work_record");
		appMessage.setTo_employee_id(submit_user.getId());
		appMessage.setIsDelete(0);
		appMessage.setIsRead(0);
		appMessage.setReceiveId(submit_user.getEmployeeId());
		appMessage.setSendId(current_user.getEmployeeId());
		System.out.println("-------------------------开始推送消息-------------------------");
		try {
			messageNewDao.deleteWorkRecord(appMessage);//设置之前的这条考勤为删除，考勤通过\驳回 workRecordTotal都是同一条记录
			preObject(appMessage);
			appMessageManager.sendMessageAuto(submit_user, appMessage);//保存并发送消息
			txManager.commit(status);
		} catch (Exception e) {
			txManager.rollback(status); // 回滚事务
		}
		System.out.println("-------------------------推送结束-------------------------");
	}
	
	protected void preObject(Object o) {
		if(o instanceof DataEntity) {
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

}
