package com.cnpc.pms.bid.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.manager.AttachmentManager;
import com.cnpc.pms.bid.manager.dto.AttachmentDTO;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.entity.Attachment;

/**
 * AttachmentManager Implementation. Copyright(c) 2011 China National Petroleum
 * Corporation , http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-8-1
 */
public class AttachmentManagerImpl extends BizBaseCommonManager implements AttachmentManager {

	@Override
	public String deleteByAttachmentId(String attachmentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AttachmentDTO> getAttachmentDTOS(String appTableName, Long apptableId) throws InvalidFilterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveAttachement(String apptable, long apptableId, List<AttachmentDTO> attachmentDTOs) {
		// TODO Auto-generated method stub

	}

	@Override
	public AttachmentDTO addAttachment(AttachmentDTO attachmentDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 事件：点击上传表格组件的删除按钮将触发此方法
	 * 
	 * @param attachmentId
	 *            附件的id
	 * @return String 返回处理的结果 成功为"success"
	 * 
	 *         public String deleteByAttachmentId(String attachmentId) {
	 *         Attachment attachmentEntity = (Attachment)
	 *         getUniqueObject(FilterFactory .getSimpleFilter("attachmentId",
	 *         attachmentId)); removeObject(attachmentEntity); return "success";
	 *         }
	 */

	@Override
	public Attachment findAttachmentByName(String name, String file_type_name) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter(
				"file_name='" + name + "' AND uploadType='上传中' AND file_type_name='" + file_type_name + "'"));
		if (list != null && list.size() > 0) {
			return (Attachment) list.get(0);
		}
		return null;
	}

	@Override
	public List<Attachment> getAttachmentByName(String name, String file_type_name) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter(
				"file_name='" + name + "' AND uploadType='上传中' AND file_type_name='" + file_type_name + "'"));
		if (list != null && list.size() > 0) {
			return (List<Attachment>) list;
		}
		return null;
	}

	@Override
	public void updateAttachmentUploadType(String name, String file_type_name, String message, String uploadType,
			String storeName) {
		// TODO Auto-generated method stub
		AttachmentManager attachmentManager = (AttachmentManager) SpringHelper.getBean("attachmentManager");
		List<Attachment> list = getAttachmentByName(name, file_type_name);
		if (list != null && list.size() > 0) {
			for (Attachment attachment : list) {
				attachment.setMessage(message);
				attachment.setUploadType(uploadType);
				attachment.setStoreName(storeName);
				attachmentManager.saveObject(attachment);
			}

		}

	}

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
	 * 
	 * 			@SuppressWarnings("unchecked") public List
	 *             <AttachmentDTO> getAttachmentDTOS(String appTableName, Long
	 *             apptableId) throws InvalidFilterException { IFilter filter =
	 *             FilterFactory .getSimpleFilter("apptable", appTableName)
	 *             .appendAnd( FilterFactory.getSimpleFilter("apptableId",
	 *             apptableId)); List<Attachment> attachments = (List
	 *             <Attachment>) this .getObjects(filter); List
	 *             <AttachmentDTO> attachmentDTOs = new ArrayList
	 *             <AttachmentDTO>(); PMSFileManager pmsManager =
	 *             (PMSFileManager) SpringHelper .getBean("PMSFileManager");
	 * 
	 *             for (Attachment attachment : attachments) { AttachmentDTO
	 *             attachmentDTO = new AttachmentDTO();
	 *             attachmentDTO.setApptable(appTableName);
	 *             attachmentDTO.setApptableId(apptableId);
	 *             attachmentDTO.setAttachmentId(attachment.getAttachmentId());
	 *             attachmentDTO.setAttachmentComment(attachment.
	 *             getAttachmentComment());
	 *             attachmentDTO.setPackageIdStr(attachment.getPackageIdStr());
	 *             attachmentDTO.setPackageNameStr(attachment.getPackageNameStr(
	 *             ));
	 * 
	 *             // 文件项目类型 attachmentDTO.setItem(attachment.getItem());
	 * 
	 *             PMSFile pmsFile = (PMSFile)
	 *             pmsManager.getObject(attachment.getAttachmentId()); if (null
	 *             != pmsFile) { attachmentDTO.setFileName(pmsFile.getName());
	 *             attachmentDTO.setFileType(pmsFile.getFileSuffix()); }
	 *             attachmentDTOs.add(attachmentDTO);
	 * 
	 *             } return attachmentDTOs; }
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bid.manager.AttachmentManager#saveAttachement(java.lang.
	 * String, long, java.util.List)
	 */
	/**
	 * 保存投标人上传的附件
	 * 
	 * @param apptable
	 *            业务表名
	 * @param apptableId
	 *            业务表id
	 * @param attachmentDTOs
	 *            附件数据
	 * 
	 *            public void saveAttachement(String apptable, long apptableId,
	 *            List<AttachmentDTO> attachmentDTOs) { if (null ==
	 *            attachmentDTOs) { return; } for (AttachmentDTO attachmentDTO :
	 *            attachmentDTOs) { Attachment attachmentEntity = new
	 *            Attachment(); attachmentEntity.setApptable(apptable);
	 *            attachmentEntity.setApptableId(apptableId);
	 *            attachmentEntity.setAttachmentId(attachmentDTO.getAttachmentId
	 *            ()); attachmentEntity.setAttachmentComment(attachmentDTO
	 *            .getAttachmentComment());
	 *            attachmentEntity.setPackageIdStr(attachmentDTO.getPackageIdStr
	 *            ()); attachmentEntity.setPackageNameStr(attachmentDTO
	 *            .getPackageNameStr()); saveObject(attachmentEntity); } }
	 * 
	 *            public AttachmentDTO addAttachment(AttachmentDTO
	 *            attachmentDTO) { Attachment attachment = null; if
	 *            (attachmentDTO.getId() != null && attachmentDTO.getId() != 0)
	 *            { attachment = (Attachment)
	 *            this.getObject(attachmentDTO.getId()); } else { attachment =
	 *            new Attachment(); } BeanUtils.copyProperties(attachmentDTO,
	 *            attachment); this.saveObject(attachment);
	 *            BeanUtils.copyProperties(attachment, attachmentDTO); return
	 *            attachmentDTO; }
	 */

	@Override
	public List<Attachment> findAttachmentByOrderSN(String order_sn, int file_type) {
		List<Attachment> list = (List<Attachment>) this
				.getList(FilterFactory.getSimpleFilter("business_type='" + order_sn + "' and file_type=" + file_type));
		List<Attachment> ret_attAttachments = null;
		String str_web_path = PropertiesUtil.getValue("file.web.root");
		if (list != null && list.size() > 0) {
			ret_attAttachments = new ArrayList<Attachment>();
			try {
				for (Attachment a : list) {
					String file_path = a.getFile_path();
					String file_name = file_path.split("exceptionorder")[1];
					a.setFile_path(str_web_path + "exceptionorder" + file_name);
					ret_attAttachments.add(a);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return (List<Attachment>) ret_attAttachments;
		}
		return null;
	}

	@Override
	public Attachment findAttachmentByStoreIdType(Long store_id, int file_type) {
		List<?> list = this.getList(FilterFactory
				.getSimpleFilter("store_id=" + store_id + " and file_type=" + file_type + " order by id desc"));
		if (list != null && list.size() > 0) {
			return (Attachment) list.get(0);
		}
		return null;
	}

}
