package com.cnpc.pms.base.file.manager;

import java.io.IOException;
import java.util.List;

import org.apache.commons.fileupload.FileUploadException;

import com.cnpc.pms.base.file.entity.PMSFile;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.util.PropertiesUtil;

/**
 * <p>
 * <b>File manager.</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2011-3-10
 */
public interface PMSFileManager extends IManager {

	public static final String PROPERTY_FILE_ROOT = "file.root";

	public static final String FILE_ROOT = PropertiesUtil.getValue(PMSFileManager.PROPERTY_FILE_ROOT);

	/**
	 * Creates the folder.
	 * 
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	String createFolder() throws IOException;

	String createFolderForReport() throws IOException;

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
	PMSFile saveObjects(Long businessId, String businessType, List items) throws IOException, FileUploadException;

	void saveObjectsForReport(String id, String fileName);

	void newTranSaveObjectsForReport(String id, String fileName);

}
