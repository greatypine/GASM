package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.MsgNotice;

public interface MsgNoticeManager extends IManager{
	
	public List<Map<String, Object>> queryMsgNoticeList(Long num);
	
	public MsgNotice saveMsgNotice(MsgNotice msgNotice);
	
	/*public MsgNotice updateMsgNotice(MsgNotice msgNotice);*/
	
	public MsgNotice queryMsgNoticeById(Long id);
	

}
