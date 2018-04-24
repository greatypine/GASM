package com.cnpc.pms.bizbase.rbac.datapermission.manager;

import java.util.List;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2013-8-29
 */
public interface CopyRecordManager extends IManager {
	/**
	 * 添加下达记录
	 * @param usercodes 用户编码，多个以","分开
	 * @param sheetId 业务表单id
	 * @param sheetType 业务类型
	 */
	public void saveCopyRecord(String usercodes, Long sheetId, String sheetType);
	
	/**
	 * 若业务数据被删除，则该数据相关下达记录也应被删除，不可见
	 * @param sheetId 业务表单id
	 */
	public void deleteCopyRecord(Long sheetId);
	
	/**
	 * 根据业务id获取该业务所有接受人
	 * @param sheetId
	 * @return
	 */
	public List<User> getUsersBySheetId(Long sheetId);
}
