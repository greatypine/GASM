package com.cnpc.pms.base.file.manager;

import com.cnpc.pms.base.manager.IManager;

public interface ExcelUploadManager extends IManager{
	void uploadToDataSource();
	void readOfficeToDataSource();
	void readBusinseeToDataSource();
}
