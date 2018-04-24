package com.cnpc.pms.base.common.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.cnpc.pms.base.common.model.ClientResponseObject;
import com.cnpc.pms.base.file.entity.PMSFile;

/**
 * <p>
 * <b>Shower Action</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2011/3/28
 */
public class ShowerAction extends DownloaderAction {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doResponse(ClientResponseObject responseObj, HttpServletResponse response) throws IOException {
		if (responseObj.isResult() == true) {
			PMSFile file = (PMSFile) responseObj.getTemporary();
			response.reset();
			// TODO: if the file is in large size, this way will consume the memory!
			response.getOutputStream().write(file.obtainFileContent());
			response.flushBuffer();
		} else {
			super.doResponse(responseObj, response);
		}
	}
}
