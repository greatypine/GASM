package com.cnpc.pms.bid.manager.dto;

import com.cnpc.pms.base.dto.PMSDTO;

/**
 * 附件DTO.
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-3
 */
public class AttachmentDTO extends PMSDTO {

	/** 附件表流水号 */
	private Long id;

	/** 业务表id -储存业务表的ID流水号 */
	private Long apptableId;

	/** 业务表type -储存业务表的名称 */
	private String apptable;

	/** 附件表ID */
	private String attachmentId;

	/** 附件描述 */
	private String attachmentComment;

	/** 文件名 */
	private String fileName;
	/** 文件类型 */
	private String fileType;

	/** 包id集合 */
	private String packageIdStr;

	/** 包名称集合 */
	private String packageNameStr;
	
	/** 文件项目类型 */
	private String item;
	
	/** 附件分类 */
	private String attachmentType;
	
	/** 成果类型 DICT: DICT_ACHIEVEMENT_TYPE*/
	private Integer achievementType;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getApptableId() {
		return this.apptableId;
	}

	public void setApptableId(Long apptableId) {
		this.apptableId = apptableId;
	}

	public String getApptable() {
		return this.apptable;
	}

	public void setApptable(String apptable) {
		this.apptable = apptable;
	}

	public String getAttachmentId() {
		return this.attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public void setAttachmentComment(String attachmentComment) {
		this.attachmentComment = attachmentComment;
	}

	public String getAttachmentComment() {
		return this.attachmentComment;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getPackageIdStr() {
		return packageIdStr;
	}

	public void setPackageIdStr(String packageIdStr) {
		this.packageIdStr = packageIdStr;
	}

	public String getPackageNameStr() {
		return packageNameStr;
	}

	public void setPackageNameStr(String packageNameStr) {
		this.packageNameStr = packageNameStr;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}

	public Integer getAchievementType() {
		return achievementType;
	}

	public void setAchievementType(Integer achievementType) {
		this.achievementType = achievementType;
	}

}
