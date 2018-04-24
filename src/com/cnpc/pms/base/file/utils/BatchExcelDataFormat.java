package com.cnpc.pms.base.file.utils;


import com.cnpc.pms.base.file.comm.utils.DateUtil;
import com.cnpc.pms.base.file.comm.utils.FileUtil;
import com.cnpc.pms.base.file.manager.ExcelManager;
import com.cnpc.pms.base.util.SpringHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by zhangjy on 2015/9/8.
 */
@Component
public class BatchExcelDataFormat {
    private static Log logger = LogFactory.getLog(BatchExcelDataFormat.class);
    ExcelManager excelManager = null;
    public void batchExcel(String path) throws Exception {

  
        ExcelManager em = (ExcelManager)SpringHelper.getBean("excelManager");
        this.excelManager = em;

        File file = new File(path);
        File[] files = file.listFiles();
        for(File f:files)
        {
            if(f.isDirectory())
            {
                batchExcel(f.getPath());
            }else{
                if(f.getName().indexOf("xlsx")>0
                        ||f.getName().indexOf("xls")>0
                            ||f.getName().indexOf("xlsm")>0)
                {
                  StringBuffer buffer =  VerifyExcelDataFormat
                          .verifyExcelFile(new FileInputStream(f.getPath()), f.getPath());
                    if(buffer == null)
                    {
                        excelManager
                                .saveFileExcelData(null, new BufferedInputStream(new FileInputStream(f)), f.getName(), 1, f.getPath(),null,null);
                        new File(SysData.UserAdmin.YICHULI+"\\"+ DateUtil.getCurrentDate()).mkdir();
                        FileUtil.copyFile(f, new File(SysData.UserAdmin.YICHULI + "\\" + DateUtil.getCurrentDate() + "\\" + f.getName()));
                        FileUtil.deleteFile(f);
                    }else
                    {
                    logger.error(buffer.toString());
                    new File(SysData.UserAdmin.CHULICUOWU+"\\"+DateUtil.getCurrentDate()).mkdir();
                    FileUtil.copyFile(f, new File(SysData.UserAdmin.CHULICUOWU + "\\" + DateUtil.getCurrentDate() + "\\" + f.getName()));
                    FileUtil.deleteFile(f);
                    }
                }
            }
        }
    }

    public void init(){
        //new BatchExcelDataFormat().batchExcel("E:\\01-椿树街道 - 副本");
        ExcelManager em = (ExcelManager)SpringHelper.getBean("excelManager");
        excelManager = em;
        System.out.println(excelManager);
    }

    //@Scheduled(cron="0/5 * *  * * ? ")
    public void myTest(){
        System.out.println("进入测试");
    }

    public static void main(String[] args) throws Exception {
       new BatchExcelDataFormat().batchExcel(SysData.UserAdmin.WEICHULI);
        logger.info("fasdfsdafasdfdsfasdf");
    }
}
