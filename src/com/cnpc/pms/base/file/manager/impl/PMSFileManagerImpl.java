package com.cnpc.pms.base.file.manager.impl;

import com.cnpc.pms.base.file.entity.PMSFile;
import com.cnpc.pms.base.file.manager.PMSFileManager;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.util.PMSPropertyUtil;
import com.cnpc.pms.base.util.StrUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * <b>Abstract file manager.</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2011-3-10
 */
public class PMSFileManagerImpl extends BaseManagerImpl implements PMSFileManager {

	/** The Constant log. */
	protected static final Logger LOG = LoggerFactory.getLogger(PMSFileManagerImpl.class);

	/** The properties path. */
	private static String propertiesPath = "classpath:conf/fileconfig.properties";

	/**
	 * Creates the folder.
	 * 
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String createFolder() throws IOException {

		String key = "file.root";
		String root = PMSPropertyUtil.getValueOfProperties(key);

		String folderPath = root + generateFolderPath();

		File folder = new File(folderPath);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				throw new IOException();
			}
		}

		return folderPath;
	}

	public String createFolderForReport() throws IOException {

		String key = "file.root.reportTask";
		String root = PMSPropertyUtil.getValueOfProperties(key);

		String folderPath = root + generateFolderPath();

		File folder = new File(folderPath);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				throw new IOException();
			}
		}

		return folderPath;
	}

	/**
	 * Generate folder path.
	 * 
	 * @return the string
	 */
	public String generateFolderPath(){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		return year + File.separator + month + File.separator + day + File.separator;
	}

	/**
	 * Save objects.
	 * 
	 * @param businessId
	 *            the business id
	 * @param businessType
	 *            the business type
	 * @param items
	 *            the items
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileUploadException
	 *             the file upload exception
	 */
	public PMSFile saveObjects(Long businessId, String businessType, List items) throws IOException,
		FileUploadException {

		String sizeMax = PMSPropertyUtil.getValueOfProperties(propertiesPath, "file." + businessType + ".sizeMax");
		String suffixsStr = PMSPropertyUtil.getValueOfProperties(propertiesPath, "file." + businessType + ".suffixs");

		if (suffixsStr == null || suffixsStr.equals("")) {
			suffixsStr = PMSPropertyUtil.getValueOfProperties(propertiesPath, "file.suffixs");
			if (suffixsStr == null || suffixsStr.equals("")) {
				throw new FileUploadException("Please set file suffixs that allowed");
			}
		}

		if (sizeMax == null || sizeMax.equals("")) {
			sizeMax = PMSPropertyUtil.getValueOfProperties(propertiesPath, "file.sizeMax");
			if (sizeMax == null || sizeMax.equals("")) {
				throw new FileUploadException("Please set file sizeMax that allowed");
			}
		}

		String[] suffixs = suffixsStr.split(",");

		boolean isAllowedSuffix = false;

		String id = null;
		// Create upload folder.
		String folderPath = this.createFolder();

		PMSFile uploadedFile = null;

		// Process the uploaded items
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();

			if (!item.isFormField()) {
				id = StrUtil.generateUUID();

				item.getFieldName();

				String fileName = item.getName();
				String name = fileName.substring(0, fileName.lastIndexOf("."));
				String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
				if (name.indexOf("\\") >= 0) {
					name = name.substring(name.lastIndexOf("\\") + 1);
				}

				if (suffixsStr.indexOf("*") >= 0) {
					isAllowedSuffix = true;
				} else {
					for (int i = 0; i < suffixs.length; i++) {
						if (fileSuffix.equalsIgnoreCase(suffixs[i])) {
							isAllowedSuffix = true;
							break;
						}
					}
				}

				if (!isAllowedSuffix) {
					throw new FileUploadException("File suffix is not allowed.");
				}

				long sizeInBytes = item.getSize();
				if (sizeInBytes > Long.parseLong(sizeMax)) {
					throw new FileUploadException("File size is too large.");
				}
				File uploadFile = new File(folderPath, id + "." + fileSuffix);
				try {
					item.write(uploadFile);
				} catch (Exception e) {
					throw new FileUploadException("Error that the file was writen in the disk.");
				}

				uploadedFile = new PMSFile();
				uploadedFile.setId(id);
				uploadedFile.setName(name);
				uploadedFile.setFileSuffix(fileSuffix);
				uploadedFile.setFileSize(sizeInBytes);
				uploadedFile.setLastUploaded(new Date());
				uploadedFile.setBusinessId(businessId);
				uploadedFile.setFilePath(generateFolderPath() + id + "." + fileSuffix);
				this.saveObject(uploadedFile);
			}
		}
		return uploadedFile;
	}

	public void saveObjectsForReport(String id, String fileName) {

		String name = fileName.substring(0, fileName.lastIndexOf("."));
		String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);

		PMSFile uploadedFile = new PMSFile();
		uploadedFile.setId(id);
		uploadedFile.setName(name);
		uploadedFile.setFileSuffix(fileSuffix);
		uploadedFile.setLastUploaded(new Date());
		// uploadedFile.setBusinessId(businessId);
		uploadedFile.setFilePath(generateFolderPath() + id + "." + fileSuffix);
		this.saveObject(uploadedFile);

		// return uploadedFile;
	}

	public void newTranSaveObjectsForReport(String id, String fileName) {
		saveObjectsForReport(id, fileName);
	}

	/**
	 * Gets the object.
	 * 
	 * @param id
	 *            the id
	 * @return the object
	 */
	@Override
	public Object getObject(Serializable id) {
		return super.getObject(id);
	}

	/**
	 * Removes the object by id.
	 * 
	 * @param id
	 *            the id
	 */
	@Override
	public void removeObjectById(Serializable id) {
		PMSFile pmsFile = (PMSFile) this.getObject(id);

		// Create upload folder.
		String folderPath = null;
		try {
			folderPath = this.createFolder();
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}

		File uploadFile = new File(folderPath, id + "." + pmsFile.getFileSuffix());
		if (uploadFile.exists()) {
			uploadFile.delete();
		}
		super.removeObjectById(id);

	}

}
