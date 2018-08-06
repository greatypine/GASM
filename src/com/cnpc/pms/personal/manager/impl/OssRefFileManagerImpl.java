package com.cnpc.pms.personal.manager.impl;

import java.io.File;

import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.OssRefFile;
import com.cnpc.pms.personal.manager.OssRefFileManager;
import com.cnpc.pms.utils.OSSUploadUtil;

public class OssRefFileManagerImpl extends BizBaseCommonManager implements OssRefFileManager {
	
	
	@Override
	public String uploadOssFile(File f,String suffix,String urlLocation) {
		String oss_url = OSSUploadUtil.uploadOssFile(f, suffix, urlLocation);
    	OssRefFile ossObj = new OssRefFile();
    	try {
    		String url = f.getPath();
        	String fileName=url.substring(url.lastIndexOf(File.separator)+1);
        	if(fileName==null||fileName=="") {
        		fileName=url.substring(url.lastIndexOf("template")+9);
        	}
        	String ossName=oss_url.substring(oss_url.lastIndexOf("/")+1);
        	ossObj.setFile_name(fileName);
        	ossObj.setOss_name(ossName);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	ossObj.setFile_url(f.getPath());
    	ossObj.setOss_url(oss_url);
    	ossObj.setSuffix(suffix);
    	saveOSSRefFile(ossObj);
    	return ossObj.getOss_url();
	}
	
	
	@Override
	public OssRefFile saveOSSRefFile(OssRefFile ossRefFile) {
		java.util.Date date = new java.util.Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		User sessionUser = null;
		if (null != SessionManager.getUserSession()
				&& null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession()
					.getSessionData().get("user");
		}
		ossRefFile.setCreate_user_id(sessionUser==null?null:sessionUser.getId());
		ossRefFile.setCreate_time(sdate);
		this.saveObject(ossRefFile);
        return ossRefFile;
	}

}
