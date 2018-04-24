package com.cnpc.pms.base.chat.support;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

//import nl.justobjects.pushlet.core.Command;
//import nl.justobjects.pushlet.core.Controller;
//import nl.justobjects.pushlet.core.Event;

import com.cnpc.pms.base.chat.entity.ChatMsg;
import com.cnpc.pms.base.chat.manager.ChatMsgManager;
import com.cnpc.pms.base.util.SpringHelper;

/**
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2012-11-6
 */
public class PmsController  {
//	extends Controller

//	@Override
//	protected void doPublish(Command aCommand) {
//
//		// TODO Auto-generated method stub
//		super.doPublish(aCommand);
//
//		Event event = aCommand.reqEvent;
//		String sessionid = event.getField("p_id");
//		String[] userRoom = sessionid.split("_");
//		String usercode = userRoom[0];
//		String roomid = event.getSubject();
//		String username = event.getField("nick");
//		String msg = event.getField("msg");
//
//		ChatMsg chatMsg = new ChatMsg();
//
//		if (msg == null) {
//			msg = "sys:login";
//		}
//		try {
//			msg = URLDecoder.decode(msg, "UTF-8");
//			username = URLDecoder.decode(username, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			super.warn("doPublish() warn: UnsupportedEncodingException. " + e.getMessage());
//		}
//		chatMsg.setSessionId(sessionid);
//		chatMsg.setUserCode(usercode);
//		chatMsg.setRoomId(roomid);
//		chatMsg.setUserName(username);
//		chatMsg.setMsg(msg);
//		chatMsg.setSendTime(new Date());
//		ChatMsgManager chatMsgManager = (ChatMsgManager) SpringHelper.getBean("chatMsgManager");
//		chatMsgManager.saveObject(chatMsg);
//		super.info("doPublish() info: usercode=" + usercode + ";username=" + username + ";msg=" + msg);
//	}

}
