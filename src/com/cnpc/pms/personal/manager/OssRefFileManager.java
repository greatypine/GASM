package com.cnpc.pms.personal.manager;

import java.io.File;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.OssRefFile;

public interface OssRefFileManager extends IManager{

	public String uploadOssFile(File f,String suffix,String urlLocation);
	public OssRefFile saveOSSRefFile(OssRefFile ossRefFile);
}
