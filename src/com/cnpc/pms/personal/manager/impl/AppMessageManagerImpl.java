package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.messageModel.dao.MessageNewDao;
import com.cnpc.pms.messageModel.entity.Message;
import com.cnpc.pms.messageModel.manager.MessageNewManager;
import com.cnpc.pms.personal.entity.AppMessage;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.manager.AppMessageManager;
import com.cnpc.pms.personal.manager.AttachmentsManager;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消息框内容
 * Created by liuxiao on 2016/10/25.
 */
public class AppMessageManagerImpl extends BizBaseCommonManager implements AppMessageManager {


	/* @Override
    public List<AppMessage> getAppMessageList(Long employee_id) {
        return (List<AppMessage>)this.getList(FilterFactory.getSimpleFilter("to_employee_id",employee_id)
                .appendAnd(FilterFactory.getSimpleFilter("status",0)));
    }*/
 
    
    
    @Override
    public Integer getAppMessageCount(Long employee_id) {
       /* List<?> list = this.getList(FilterFactory.getSimpleFilter("to_employee_id",employee_id)
                .appendAnd(FilterFactory.getSimpleFilter("status",0)).appendAnd(FilterFactory.getSimpleFilter("isReady",false)));
        if(list != null){
            return list.size();
        }
        return 0;*/
    	
    	MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
    	return messageNewDao.getUnReadMessage(employee_id);
    }

    @Override
    public AppMessage getAppMessage(Long id) {
        return (AppMessage)this.getObject(id);
    }

    @Override
    public Boolean updateReadyMessage(Long id) {
        try{
        	/* Date sdate = new Date();
            AppMessage appMessage = this.getAppMessage(id);
            appMessage.setReadDate(sdate);
            appMessage.setReady(true);
            appMessage.setUpdate_time(sdate);
            appMessage.setUpdate_user_id(appMessage.getTo_employee_id());*/
        	MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
    		Message message = new Message();
    		message.setId(id);
        	messageNewDao.readMessage(message);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean deleteMessage(Long id) {
        try{
           /* Date sdate = new Date();
            AppMessage appMessage = this.getAppMessage(id);
            appMessage.setStatus(1);
            appMessage.setUpdate_time(sdate);
            appMessage.setUpdate_user_id(appMessage.getTo_employee_id());
            this.saveObject(appMessage);*/
        	MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
    		Message message = new Message();
    		message.setId(id);
        	messageNewDao.deleteMessage(message);
        	
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public Boolean deleteAllMessage(Long employee_id) {
        try{
        /*	Date sdate = new Date();
        	List<AppMessage> amList = (List<AppMessage>)this.getList(FilterFactory.getSimpleFilter("to_employee_id",employee_id)
                    .appendAnd(FilterFactory.getSimpleFilter("status",0)));
        	if(amList != null){
        		for(AppMessage appMessage : amList){
        			appMessage.setStatus(1);
                    appMessage.setUpdate_time(sdate);
                    appMessage.setUpdate_user_id(appMessage.getTo_employee_id());
                    this.saveObject(appMessage);
        		}
        	}*/
        	
        	MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
        	messageNewDao.deleteMessage(employee_id);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean saveMessageAndPush(User user, AppMessage appMessage) {
        try{
            PropertiesValueUtil valueUtil = new PropertiesValueUtil("conf/apppush.properties");
            this.saveObject(appMessage);
            String url = valueUtil.getStringValue("url");
            String appKey = valueUtil.getStringValue("appKey");
            String masterSecret = valueUtil.getStringValue("masterSecret");
            String appId = valueUtil.getStringValue("appId");
            IGtPush push = new IGtPush(url, appKey, masterSecret);
            if(user.getOs() != null){
                if("android".equals(user.getOs().toLowerCase())){
                    NotificationTemplate template = new NotificationTemplate();
                    template.setAppId(appId);
                    template.setAppkey(appKey);
                    template.setTransmissionContent(appMessage.getContent());
                    template.setTransmissionType(1);
                    template.setTitle(appMessage.getTitle());
                    template.setText(appMessage.getContent());
                    template.setLogo("");
                    template.setLogoUrl("");

                    // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
                    SingleMessage message = new SingleMessage ();
                    message.setData(template);

                    message.setOffline(true);
                    message.setOfflineExpireTime(1000 * 600);

                    Target target = new Target();
                    target.setAppId(appId);
                    target.setClientId(user.getClient_id());
                    IPushResult ret = push.pushMessageToSingle(message, target);
                    return ret != null;
                }else{
                    APNPayload.DictionaryAlertMsg dictionaryAlertMsg = new APNPayload.DictionaryAlertMsg();
                    dictionaryAlertMsg.setTitle(appMessage.getTitle());
                    dictionaryAlertMsg.setBody(appMessage.getContent());

                    APNPayload apnPayload = new APNPayload();
                    apnPayload.setAutoBadge("-1");
                    apnPayload.setAlertMsg(dictionaryAlertMsg);
                    TransmissionTemplate transmissionTemplate = new TransmissionTemplate();
                    transmissionTemplate.setAppId(appId);
                    transmissionTemplate.setAppkey(appKey);
                    transmissionTemplate.setTransmissionContent(appMessage.getContent());
                    transmissionTemplate.setTransmissionType(1);
                    transmissionTemplate.setAPNInfo(apnPayload);

                    SingleMessage message1 = new SingleMessage ();
                    message1.setData(transmissionTemplate);

                    message1.setOffline(true);
                    message1.setOfflineExpireTime(1000 * 600);

                    Target target1 = new Target();
                    target1.setAppId(appId);
                    target1.setClientId(user.getClient_id());
                    if(user.getToken() == null || "".equals(user.getToken())){
                        return false;
                    }

                    IPushResult ret1 = push.pushAPNMessageToSingle(appId, user.getToken(), message1);
                    return ret1 != null;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

	
	@Override
	public List<Map<String, Object>> getAppMessageList(Long employee_id) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		return messageNewDao.getMessageByEmployee(employee_id);
	}
}
