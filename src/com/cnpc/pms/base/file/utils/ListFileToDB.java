package com.cnpc.pms.base.file.utils;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by cyh(275923233@qq.com) on 2015/9/15.
 */
@Component
public class ListFileToDB {

    private Properties properties;

    public String filePath(){
        String filePath = "D:\\采集处理数据\\月店\\未处理";
        //获取文件路径
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("upload.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        filePath = properties.getProperty("listfile_path");
        return filePath;
    }

    public void showAllFiles(File dir){
        File[] fs = dir.listFiles();
        for(int i=0; i<fs.length; i++){
            //System.out.println(fs[i].getAbsolutePath());
            //文件夹
            if(fs[i].isDirectory()){
                try{
                    showAllFiles(fs[i]);
                }catch(Exception e){}
            }else{
                //文件
                //此处进行文件入库操作
                System.out.println("逐个文件入库处理");
                ListOneToDB listOneToDB = new ListOneToDB();
                listOneToDB.exlToDB(fs[i].getPath());
                System.out.println(fs[i].getPath());
            }
        }
    }

    public static void main(String args[]){
        ListFileToDB listFileToDB = new ListFileToDB();
        File root = new File(listFileToDB.filePath());
        new ListFileToDB().showAllFiles(root);
    }
}
