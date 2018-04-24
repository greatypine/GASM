package com.cnpc.pms.bid.manager.dto;

import java.util.Date;

import com.cnpc.pms.base.dto.PMSDTO;

/**
 * 文件存储DTO
 * @author IBM
 *
 */
public class BaseFileDTO extends PMSDTO {

	private String id;
	/** 业务ID */
	private Long businessId;
	
	/** 文件路径 */
	private String filePath;

	/** 文件大小 */
	private Long fileSize;
	
	/** 文件后缀 */
	private String fileSuffix;
	
	/** 最后上传日期 */
	private Date lastUploaded;
	
	/** 文件名字 */
	private String name;

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public Date getLastUploaded() {
		return lastUploaded;
	}

	public void setLastUploaded(Date lastUploaded) {
		this.lastUploaded = lastUploaded;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
