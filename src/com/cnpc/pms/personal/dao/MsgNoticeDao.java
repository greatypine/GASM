package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;

public interface MsgNoticeDao extends IDAO{
	public List<Map<String, Object>> queryNoticeList(Long num,String employee_no,Long storeId);
	
	public List<Map<String, Object>> queryMessageList(Long num);
	
	public void updateNoticeReadByNo(String noticeNo,String employee_no);
}
