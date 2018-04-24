package com.cnpc.pms.base.common.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cnpc.pms.base.common.model.ClientRequestObject;
import com.cnpc.pms.base.common.model.ClientResponseObject;
import com.cnpc.pms.base.exception.DispatcherException;
import com.cnpc.pms.base.file.entity.PMSFile;

/**
 * <p>
 * <b>Downloader Action</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2011/3/28
 */
public class DownloaderAction extends DispatcherAction {

	public static final String CONTENT_TYPE = "application/x-msdownload";

	public static final String HEADER_NAME = "Content-Disposition";

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	private static final String MANAGER_NAME = "PMSFileManager";
	private static final String METHOD_NAME = "getObject";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		execute(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		log.warn("Should not invoke this servlet in post method. {}", request.getRequestURI());
		if (log.isWarnEnabled()) {
			if (request.getHeaderNames().hasMoreElements()) {
				Object name = request.getHeaderNames().nextElement();
				log.warn("\\{} = {}", name, request.getHeader(name.toString()));
			}
		}
		doGet(request, response);
	}

	@Override
	protected ClientRequestObject getClientRequestObject(HttpServletRequest request) {
		ClientRequestObject requestObj = new ClientRequestObject();
		requestObj.setManagerName(MANAGER_NAME);
		requestObj.setMethodName(METHOD_NAME);
		String id = request.getParameter("id");
		Object[] paras = { id };
		requestObj.setParameters(paras);
		return requestObj;
	}

	@Override
	protected void afterInvoke(Object resultValue, ClientResponseObject responseObj, HttpServletRequest request,
			ClientRequestObject requestObj) throws DispatcherException {
		if (null == resultValue) {
			String id = requestObj.getParameters()[0].toString();
			log.error("Fail to get file, id: {}", id);
			throw new DispatcherException("Fail to get file, id: " + id);
		} else {
			responseObj.setResult(true);
			responseObj.setTemporary(resultValue);
		}
	}

	@Override
	protected void doResponse(ClientResponseObject responseObj, HttpServletResponse response) throws IOException {
		if (responseObj.isResult() == true) {
			PMSFile file = (PMSFile) responseObj.getTemporary();
			response.reset();
			response.setContentType(CONTENT_TYPE);
			String s = "attachment; filename=" + new String(file.obtainFileName().getBytes("gb2312"), "ISO-8859-1");
			response.setHeader(HEADER_NAME, s); // 以上输出文件元信息
			// TODO: if the file is in large size, this way will consume the memory!
			response.getOutputStream().write(file.obtainFileContent());
			response.flushBuffer();
		} else {
			super.doResponse(responseObj, response);
		}
	}

}
