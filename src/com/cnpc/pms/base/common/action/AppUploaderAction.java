package com.cnpc.pms.base.common.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.cnpc.pms.base.common.model.ClientRequestObject;
import com.cnpc.pms.base.common.model.ClientResponseObject;
import com.cnpc.pms.base.exception.DispatcherException;
import com.cnpc.pms.base.util.StrUtil;

/**
 * <p>
 * <b>Uploader Action</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2011/3/21
 */
public class AppUploaderAction extends DispatcherAction {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;
	private static final String MANAGER_NAME = "PMSFileManager";
	private static final String METHOD_NAME = "saveObjects";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.warn("Should not invoke this servlet in get method.{}", request.getRequestURI());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		execute(request, response);
	}

	@Override
	protected ClientRequestObject getClientRequestObject(HttpServletRequest request) throws DispatcherException {
		FileItemFactory factory = new DiskFileItemFactory();// Create a factory for disk-based file items
		ServletFileUpload upload = new ServletFileUpload(factory);// Create a new file upload handler
		// Parse the request
		List<?> items;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			throw new DispatcherException("Fail to get uploaded file: " + e.getMessage());
		}
		String businessIdStr = request.getParameter("businessId");
		Long businessId = null;
		if (StrUtil.isNotEmpty(businessIdStr)) {
			businessId = Long.parseLong(businessIdStr);
		}
		String businessType = request.getParameter("businessType");
		Object[] paras = new Object[] { businessId, businessType, items };

		String clientInvokeString = request.getParameter(REQUEST_STRING);
		ClientRequestObject requestObj;
		// In some component (excelImportManager, eg.), the client would give a specific manager (not PMSFileManager) to
		// handle the upload request.
		if (StrUtil.isNotEmpty(clientInvokeString)) {
			requestObj = super.getClientRequestObject(request);
			// TEST: change to paras.addAll(Arrays.asList(requestObj.getParameters()))
			List<Object> arrayList = new ArrayList<Object>();
			arrayList.addAll(Arrays.asList(paras));
			arrayList.addAll(Arrays.asList(requestObj.getParameters()));
			requestObj.setParameters(arrayList.toArray());
		} else {
			requestObj = new ClientRequestObject();
			requestObj.setManagerName(MANAGER_NAME);
			requestObj.setMethodName(METHOD_NAME);
			requestObj.setParameters(paras);
		}
		return requestObj;
	}

	@Override
	protected void doResponse(ClientResponseObject responseObj, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.getWriter().write(responseObj.toJSON());
	}

}
