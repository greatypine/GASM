package com.cnpc.pms.base.file.entity;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.io.FileUtils;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.OptLockEntity;
import com.cnpc.pms.base.util.PMSPropertyUtil;

/**
 * <p>
 * <b>File entity.</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2011-3-21
 */
@Entity
@Table(name = "ts_base_file")

public class PMSFile extends OptLockEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The business id. */
	private Long businessId;

	/** The file size. */
	private Long fileSize;

	/** The file suffix. */
	private String fileSuffix;

	/** The id. */
	@Id
	private String id;

	/** The last uploaded. */
	private Date lastUploaded;

	/** The name. */
	private String name;

	/** The file path. */
	private String filePath;

	/**
	 * Gets the file path.
	 * 
	 * @return the file path
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Sets the file path.
	 * 
	 * @param filePath
	 *            the new file path
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Gets the business id.
	 * 
	 * @return the business id
	 */
	public Long getBusinessId() {
		return businessId;
	}

	/**
	 * Gets the file size.
	 * 
	 * @return the file size
	 */
	public Long getFileSize() {
		return fileSize;
	}

	/**
	 * Gets the file suffix.
	 * 
	 * @return the file suffix
	 */
	public String getFileSuffix() {
		return fileSuffix;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the last uploaded.
	 * 
	 * @return the last uploaded
	 */
	public Date getLastUploaded() {
		return lastUploaded;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the business id.
	 * 
	 * @param businessId
	 *            the new business id
	 */
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	/**
	 * Sets the file size.
	 * 
	 * @param fileSize
	 *            the new file size
	 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * Sets the file suffix.
	 * 
	 * @param fileSuffix
	 *            the new file suffix
	 */
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the last uploaded.
	 * 
	 * @param lastUploaded
	 *            the new last uploaded
	 */
	public void setLastUploaded(Date lastUploaded) {
		this.lastUploaded = lastUploaded;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtain full file path.
	 * 
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String obtainFullFilePath() throws IOException {
		// Get root.
		String key = "file.root";
		String root = PMSPropertyUtil.getValueOfProperties(key);

		return root + this.filePath;

	}

	/**
	 * Obtain file name.
	 * 
	 * @return the string
	 */
	public String obtainFileName() {
		return this.name + "." + this.fileSuffix;
	}

	/**
	 * Obtain file content.
	 * 
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public byte[] obtainFileContent() throws IOException {
		return FileUtils.readFileToByteArray(new File(this.obtainFullFilePath()));
	}
}
