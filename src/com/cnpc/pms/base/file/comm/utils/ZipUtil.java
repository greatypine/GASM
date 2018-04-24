package com.cnpc.pms.base.file.comm.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;

/**
 * Created by zhangjy on 2015/8/21.
 */
public class ZipUtil {

    public static void unZipFiles(String dirPath,String zipPath)throws ZipException {
        //根据路径取得需要解压的Zip文件
        final ZipFile zipFile = new ZipFile(zipPath);
        zipFile.setFileNameCharset("GBK");
        //压缩包文件解压路径
        zipFile.extractAll(dirPath);
    }

    /**
     * 根据路径删除文件或文件夹
     * @param file
     */
    public static void deleteFiles( File file){
        if(file.exists()){
            if(file.isFile()){
                file.delete();
            }else if(file.isDirectory()){
                File files[] = file.listFiles();
                for(int i=0;i<files.length;i++){
                   // System.out.println(files[i].getName());
                    if(files[i].isDirectory())
                        deleteFiles(files[i]);
                    files[i].delete();
                }
            }
            file.delete();
        }
    }

    /**
     * 根据路径删除文件或文件夹
     * @param file
     */
    public static void deleteZipFiles( File file){
        if(file.exists()){
            if(file.isFile()){
                file.delete();
            }else if(file.isDirectory()){
                File files[] = file.listFiles();
                for(int i=0;i<files.length;i++){
                   // System.out.println(files[i].getName());
                    if(files[i].isDirectory())
                        deleteFiles(files[i]);
                    files[i].delete();
                }
            }
        }
    }

    /**
     * 删除zip文件和文件夹
     * @param path
     */
    public static void deleteFiles(String path){
        File file = new File(path+".zip");
        File fileDir = new File(path);
        deleteFiles(file);
        deleteFiles(fileDir);
    }

    public static void main(String[] args) throws Exception {
        //unZipFiles("e:\\0821\\椿树街道房屋数据11","e:\\0821\\椿树街道房屋数据11.zip");
        File file = new File("E:\\617\\Taurus\\target\\taurus\\upload\\01-椿树街道 - 副本");
        deleteFiles(file);
    }
}
