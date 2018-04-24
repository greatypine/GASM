package com.cnpc.pms.bid.manager;

import java.util.List;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.bid.manager.dto.AttachmentDTO;
import com.cnpc.pms.personal.entity.Attachment;

/**
 * Attachment Interface.
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author IBM
 * @since 2011-8-1
 */
public interface AttachmentManager extends IManager {

	/**
	 * 事件：点击上传表格组件的删除按钮将触发此方法
	 * 
	 * @param attachmentId
	 *            附件的id
	 * @return String 返回处理的结果 成功为"success"
	 */
	public String deleteByAttachmentId(String attachmentId);

	/**
	 * 根据业务Id和业务表名获取到对应的附件集合
	 * 
	 * @param appTableName
	 *            : 附件对应的业务表的表名
	 * @param apptableId
	 *            : 附件对应的业务表的id
	 * @return List <AttachmentDTO> 返回指定业务表中指定id的记录的附件
	 * @throws InvalidFilterException
	 *             抛出不合适filter异常
	 */
	public List<AttachmentDTO> getAttachmentDTOS(String appTableName, Long apptableId) throws InvalidFilterException;

	/**
	 * 保存投标人上传的附件
	 * 
	 * @param apptable
	 *            业务表名
	 * @param apptableId
	 *            业务id
	 * @param attachmentDTOs
	 *            附件数据
	 */
	public void saveAttachement(String apptable, long apptableId, List<AttachmentDTO> attachmentDTOs);

	/**
	 * 根据业务Id和业务表名获取到对应的附件集合
	 * 
	 * @param appTableName
	 *            : 附件对应的业务表的表名
	 * @param apptableId
	 *            : 附件对应的业务表的id
	 * @return List <AttachmentDTO> 返回指定业务表中指定id的记录的附件
	 * @throws InvalidFilterException
	 *             抛出不合适filter异常
	 */
	// public List<AttachmentDTO> getAttachmentDTOSForDownload(String
	// appTableName, Long apptableId) throws InvalidFilterException;

	/**
	 * 保存附近信息
	 * 
	 * @param attachmentDTO
	 * @return
	 */
	public AttachmentDTO addAttachment(AttachmentDTO attachmentDTO);

	Attachment findAttachmentByName(String name, String file_type_name);

	List<Attachment> getAttachmentByName(String name, String file_type_name);

	void updateAttachmentUploadType(String name, String file_type_name, String message, String uploadType,
			String storeName);

	public List<Attachment> findAttachmentByOrderSN(String businessType, int file_type);

	public Attachment findAttachmentByStoreIdType(Long store_id, int file_type);

}
