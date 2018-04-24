package com.cnpc.pms.base.file.manager.impl;

import com.cnpc.pms.base.file.manager.ExcelUploadManager;
import com.cnpc.pms.bizbase.action.UploadGatherInfoAction;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;


public class ExcelUploadManagerImpl extends BizBaseCommonManager implements ExcelUploadManager{

	@Override
	public void uploadToDataSource() {
		try {
			new UploadGatherInfoAction().uploadExcelToDataSource();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void readOfficeToDataSource() {
		try {
			new UploadGatherInfoAction().uploadOfficeExcelToDataSource();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void readBusinseeToDataSource() {
		try {
			new UploadGatherInfoAction().uploadBusinessExcelToDataSource();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
