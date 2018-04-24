package com.cnpc.pms.base.file.collection;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import com.cnpc.pms.base.file.manager.ExcelManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.manager.AttachmentManager;
import com.cnpc.pms.personal.dao.VillageDao;
import com.cnpc.pms.personal.dao.impl.VillageDaoImpl;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.BuildingManager;
import com.cnpc.pms.personal.manager.HouseManager;
import com.cnpc.pms.personal.manager.HouseStyleManager;
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.base.file.utils.VerifyExcelDataFormat;
import com.cnpc.pms.base.paging.FilterFactory;

public class ReadExcel {
	
	
	/**
     * 地采Excel文件读入Batch（Linux）
     * @param
     */
    public void batchExcel(List<File> listfile,String str,String remark,String ip) throws Exception {
        //File file = new File(path);
        //File[] files = file.listFiles();
    	ExcelManager excelManager=(ExcelManager) SpringHelper.getBean("excelManager");
        for(File f:listfile)
        {
            //遇到目录，打开目录取文件
        	if(f.isDirectory())
            {
        		System.out.println("这是一个文件夹");
                //batchExcel(f.getPath());
            }else{
            	
            	//文件是Excel
                if(f.getName().indexOf("xlsx")>0
                        ||f.getName().indexOf("xls")>0
                        ||f.getName().indexOf("xlsm")>0)
                {
                	//检验文件格式是否正确
                	System.out.println("正在上传的文件名称"+f.getName());
                	StringBuffer buffer =  VerifyExcelDataFormat
                            .verifyExcelFile(new FileInputStream(f.getPath()), f.getPath());
                	if(buffer==null){
                		if("thod".equals(str)) {
                    		String gb_code=f.getName().split("-")[0];
                    		Village villageByGb_code = getVillageByGb_code(gb_code);
                    		if(villageByGb_code!=null){
                    			VillageDao villageDao=(VillageDao)SpringHelper.getBean(VillageDao.class.getName());
                    				villageDao.deleteDataByVillageId(villageByGb_code.getId());
							}
                		}
                		System.out.println(f.getName()+"------------循环遍历上传的文件");
                    	excelManager.saveFileExcelData(null, new BufferedInputStream(new FileInputStream(f.getPath())), f.getName(), 1, f.getPath(),remark,ip);
                	}else{
                		throw new RuntimeException(buffer.toString());
                	}
                	}
                }
            }
        }
    
    
    /**
     * 地采Excel文件读入Batch（Linux）
     * @param
     */
    public void batchExcelData(File f,String str,Attachment attachment) throws Exception {
    	ExcelManager excelManager=(ExcelManager) SpringHelper.getBean("excelManager");
    	AttachmentManager attachmentManager=(AttachmentManager)SpringHelper.getBean("attachmentManager");
                if(f.getName().indexOf("xlsx")>0
                        ||f.getName().indexOf("xls")>0
                        ||f.getName().indexOf("xlsm")>0)
                {
                	//检验文件格式是否正确
                	System.out.println("正在上传的文件名称"+f.getName());
                	StringBuffer buffer =  VerifyExcelDataFormat
                            .verifyExcelFile(new FileInputStream(f.getPath()), f.getPath());
                	if(buffer==null){
                		if("thod".equals(str)) {
                    		String gb_code=f.getName().split("-")[0];
                    		Village villageByGb_code = getVillageByGb_code(gb_code);
                    		if(villageByGb_code!=null){
                    			VillageDao villageDao=(VillageDao)SpringHelper.getBean(VillageDao.class.getName());
                    				villageDao.deleteDataByVillageId(villageByGb_code.getId());
							}
                		}
                		System.out.println(f.getName()+"------------循环遍历上传的文件");
                    	String string = excelManager.saveFileExcel(null, new BufferedInputStream(new FileInputStream(f.getPath())), f.getName(), 1, f.getPath(),attachment);
                    	attachment.setMessage("上传成功");
        	        	attachment.setUploadType("上传成功");
        	        	attachment.setStoreName(string);
        	        	attachmentManager.saveObject(attachment);
        	        	attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType(),string);
                	}else{
                		attachment.setMessage(buffer.toString());
        	        	attachment.setUploadType("上传失败");
        	        	attachmentManager.saveObject(attachment);
        	        	attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType(),null);
                		throw new RuntimeException(buffer.toString());
                	}
                	}
                }
    

    /**
     * 根据gb_code获得社区
     * @param gb_code
     * @return
     */
    public Village getVillageByGb_code(String gb_code){
        VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
        List<?> lst_vilage_data = villageManager.getList(FilterFactory.getSimpleFilter("gb_code",gb_code));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0){
            return (Village)lst_vilage_data.get(0);
        }
    	return null;
    }
    /**
     * 根据社区id获得楼房的id字符集
     * @param village_id
     * @return
     */
    public String getDataByVillageId(Long village_id){
    	String VillageIdStr="";
    	TinyVillageManager tinyVillageManager=(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
    	List<TinyVillage> list = tinyVillageManager.getTinyVillageByVillage_Id(village_id);
    	if(list!=null){
    		for (TinyVillage tinyVillage : list) {
    			VillageIdStr+=tinyVillage.getId()+",";
			}
    		VillageIdStr=VillageIdStr.substring(0, VillageIdStr.length()-1);
    	}
    	
    	return VillageIdStr;
    }


}
