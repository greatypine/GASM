// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DownloaderAction.java

package com.cnpc.pms.bizbase.action;

import com.cnpc.pms.base.common.action.DispatcherAction;
import com.cnpc.pms.base.common.model.ClientRequestObject;
import com.cnpc.pms.base.common.model.ClientResponseObject;
import com.cnpc.pms.base.exception.DispatcherException;
import com.cnpc.pms.base.file.entity.PMSFile;
//import com.yadea.crm.common.util.SystemUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package com.cnpc.pms.base.common.action:
//            DispatcherAction

public class PictureAction extends HttpServlet {
	Logger log = LoggerFactory.getLogger(this.getClass());

	public PictureAction() {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 获取参数名
		String pictureName = request.getParameter("pictureName");
		
		// 本地Windows开发环境下使用此图片目录,
		String localfilepath = "C://picture//";
		
		//这一方法仅在本地测试的时候使用,localfilepath应当从Servlet的参数里面获取
				
//		if(SystemUtil.isWindows()){
//			log.debug("Find C://Windows,Using LocalMode.");
//			//开发环境下,文件名与路径转换为小写
//			pictureName = pictureName.toLowerCase();
//			//开发环境下,转换到本机目录来查找图片文件
//			pictureName = pictureName.replace("/crmdms/", localfilepath);
//		}else{
//			pictureName = pictureName.toLowerCase();
//		}
		
		
		log.debug("pictureName:"+pictureName);
		File file = new File(pictureName);

		if (file.isFile()) {
			FileInputStream fis = new FileInputStream(file);
			byte[] buf = new byte[(int) file.length()];
			fis.read(buf);
			fis.close();

			response.reset();
			response.setContentType("image/jpeg");

			response.getOutputStream().write(buf);
			response.flushBuffer();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.warn("Should not invoke this servlet in post method. {}",
				request.getRequestURI());
		if (log.isWarnEnabled() && request.getHeaderNames().hasMoreElements()) {
			Object name = request.getHeaderNames().nextElement();
			log.warn("\\{} = {}", name, request.getHeader(name.toString()));
		}
		doGet(request, response);
	}

	protected ClientRequestObject getClientRequestObject(
			HttpServletRequest request) {
		ClientRequestObject requestObj = new ClientRequestObject();
		requestObj.setManagerName("PMSFileManager");
		requestObj.setMethodName("getObject");
		String id = request.getParameter("id");
		Object paras[] = { id };
		requestObj.setParameters(paras);
		return requestObj;
	}

	protected void afterInvoke(Object resultValue,
			ClientResponseObject responseObj, HttpServletRequest request,
			ClientRequestObject requestObj) throws DispatcherException {
		if (null == resultValue) {
			String id = requestObj.getParameters()[0].toString();
			log.error("Fail to get file, id: {}", id);
			throw new DispatcherException((new StringBuilder())
					.append("Fail to get file, id: ").append(id).toString());
		} else {
			responseObj.setResult(true);
			responseObj.setTemporary(resultValue);
			return;
		}
	}

	protected void doResponse(ClientResponseObject responseObj,
			HttpServletResponse response) throws IOException {
		if (responseObj.isResult()) {
			PMSFile file = (PMSFile) responseObj.getTemporary();
			response.reset();
			response.setContentType("application/x-msdownload");
			String s = (new StringBuilder())
					.append("attachment; filename=")
					.append(new String(
							file.obtainFileName().getBytes("gb2312"),
							"ISO-8859-1")).toString();
			response.setHeader("Content-Disposition", s);
			response.getOutputStream().write(file.obtainFileContent());
			response.flushBuffer();
		} else {
			// super.doResponse(responseObj, response);
		}
	}

	public static final String CONTENT_TYPE = "application/x-msdownload";
	public static final String HEADER_NAME = "Content-Disposition";
	private static final long serialVersionUID = 1L;

}
