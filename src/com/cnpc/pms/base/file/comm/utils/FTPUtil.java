package com.cnpc.pms.base.file.comm.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

public class FTPUtil {
    private FTPClient ftp;

    /**
     * FTPClient构造函数,主要进行初始化配置连接FTP服务器。
     *
     * @param host
     *            FTP服务器的IP地址
     * @param port
     *            FTP服务器的端口
     * @param userName
     *            FTP服务器的用户名
     * @param passWord
     *            FTP服务器的密码
     */
    public FTPUtil(String host, int port, String userName, String passWord) {
        ftp = new FTPClient();
        ftp.setControlEncoding("UTF-8");
        try {
            ftp.connect(host, port);// 连接FTP服务器
            ftp.login(userName, passWord);// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                System.out.println("未连接到FTP，用户名或密码错误。");
                ftp.disconnect();
            } else {
                System.out.println("FTP连接成功。");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FTP的端口错误,请正确配置。");
        }
    }

    /**
     * 上传文件到FTP服务器
     *
     * @param local
     *            本地文件名称，绝对路径
     * @param remote
     *            远程文件路径,支持多级目录嵌套，支持递归创建不存在的目录结构
     * @throws IOException
     */
    public void upload(String local, String remote) throws IOException {
        // 设置PassiveMode传输
        ftp.enterLocalPassiveMode();
        // 设置以二进制流的方式传输
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        // 对远程目录的处理
        String remoteFileName = remote;
        if (remote.contains("/")) {
            remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
            // 创建服务器远程目录结构，创建失败直接返回
//            if (!createDirecroty(remote)) {
//                return;
//            }
        }
     //   FTPFile[] files = ftp.listFiles(new String(remoteFileName));
        File f = new File(local);
        uploadFile(remoteFileName, f);
    }

    public void uploadFile(String remoteFile, File localFile)
            throws IOException {
        InputStream in = new FileInputStream(localFile);
        ftp.storeFile(remoteFile, in);
        in.close();
    }

    /**
     * 递归创建远程服务器目录
     * @param remote
     * @return 目录创建是否成功
     * @throws IOException
     */
    public boolean createDirecroty(String remote) throws IOException {
        boolean success = true;
        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        //System.out.println(directory);
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/")
                && !ftp.changeWorkingDirectory(new String(directory))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            while (true){
                String subDirectory = new String(remote.substring(start, end));
                if (!ftp.changeWorkingDirectory(subDirectory)) {
                    if (ftp.makeDirectory(subDirectory)) {
                        ftp.changeWorkingDirectory(subDirectory);
                        System.out.println("创建目录成功 subDirectory=" + subDirectory);
                    } else {
                        System.out.println("创建目录失败 subDirectory="+subDirectory);
                        success = false;
                        return success;
                    }
                }
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

    public boolean uploadAll(String filename, String uploadpath)
            throws Exception {
        boolean success = false;
        File file = new File(filename);
        // 要上传的是否存在
        if (!file.exists()) {
            return success;
        }
        // 要上传的是否是文件夹
        if (!file.isDirectory()) {
            return success;
        }
        File[] flles = file.listFiles();
        for (File files : flles) {
            if (files.exists()) {
                if (files.isDirectory()) {
                    this.uploadAll(files.getAbsoluteFile().toString(),uploadpath);
                } else {
                    String local = files.getCanonicalPath().replaceAll("\\\\", "/");
                    String remote = uploadpath + local.substring(local.indexOf("/") + 1);
                    upload(local, remote);
                    ftp.changeWorkingDirectory("/");
                }
            }
        }
        return true;
    }
    /**
     * 在服务器上创建一个文件夹
     *
     * @param dir
     *            文件夹名称，不能含有特殊字符，如 \ 、/ 、: 、* 、?、 "、 <、>...
     */
    public boolean makeDirectory(String dir) {
        boolean flag = true;
        try {
            flag = ftp.makeDirectory(dir);
            if (flag) {
                System.out.println("make Directory " +dir +" succeed");
            } else {
                System.out.println("make Directory " +dir+ " false");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean changeWorkDir(String subDirectory) throws IOException {
        return ftp.changeWorkingDirectory(subDirectory);
    }

    public boolean uploadAllFile(String filename, String uploadpath)
            throws Exception {
        boolean success = false;
        File file = new File(filename);
        // 要上传的是否存在
        if (!file.exists()) {
            return success;
        }
        // 要上传的是否是文件夹
        if (!file.isDirectory()) {
            return success;
        }
        for(String s:getPaths(uploadpath)){
            if(!changeWorkDir(s)){
                makeDirectory(s);
            }
            changeWorkDir(s);
        }
        if(!changeWorkDir(file.getName())) {
            makeDirectory(file.getName());
        }
        changeWorkDir(file.getName());
        File[] flles = file.listFiles();
        for (File files : flles) {
            if (files.exists()) {
                if (files.isDirectory()) {
                    if(!changeWorkDir(files.getName())) {
                        makeDirectory(files.getName());
                    }
                    changeWorkDir(files.getName());
                    //this.uploadAllFile(files.getAbsoluteFile().toString(),uploadpath);
                    File[] f = files.listFiles();
                    for (File ff : f) {
                        String local = ff.getCanonicalPath().replaceAll("\\\\", "/");
                        String remote = uploadpath + local.substring(local.indexOf("/") + 1);
                        upload(local, remote);
                    }
                } else {
                    String local = files.getCanonicalPath().replaceAll("\\\\", "/");
                    String remote = uploadpath + local.substring(local.indexOf("/") + 1);
                    upload(local, remote);
                   // ftp.changeWorkingDirectory("/");
                }
            }
            ftp.changeWorkingDirectory("..");
        }
        ftp.changeWorkingDirectory("/");
        return true;
    }
    public String[] getPaths(String s){
        s = s.substring(1,s.length()-1);
        return s.split("/");
    }
    public static void main(String[] args) {
        // FileInputStream in = new FileInputStream(new File("D:/text.txt"));
        FTPUtil ftp = new FTPUtil("10.16.0.100", 21, "temp", "temp");
        // ftp.upload("bb/cc", "text.txt", in);
        try{
            ftp.uploadAllFile("E:\\617\\Taurus\\target\\taurus\\upload\\01-椿树街道 - 副本", "/piclib/excel/");
//            System.out.println(ftp.changeWorkDir("piclib"));
//            System.out.println(ftp.changeWorkDir("excel"));
//            if(!ftp.changeWorkDir("西城区")) {
//                ftp.makeDirectory("西城区");
//            }
//            ftp.changeWorkDir("西城区");
//            if(!ftp.changeWorkDir("数据")) {
//                ftp.makeDirectory("数据");
//            }
//            ftp.changeWorkDir("数据");
//            ftp.upload("E:/西城区/数据/大安澜营社区.xlsx", "/piclib/excel/西城区/大安澜营社区.xlsx");
//            ftp.upload("E:/西城区/数据/百顺街道.xlsx", "/piclib/excel/西城区/百顺街道.xlsx");
//            ftp.upload("E:/西城区/数据/大栅栏西街社区.xlsx", "/piclib/excel/西城区/大栅栏西街社区.xlsx");
//            ftp.changeWorkDir("..");
//            if(!ftp.changeWorkDir("地图")) {
//                ftp.makeDirectory("地图");
//            }
//            ftp.changeWorkDir("地图");
//            ftp.upload("E:/西城区/地图/椿树园社区.png", "/piclib/excel/西城区/地图/椿树园社区.png");
//            ftp.upload("E:/西城区/地图/红线社区.png", "/piclib/excel/西城区/地图/红线社区.png");
            //ftp.upload("E:/椿树街道房屋数据.zip", "/piclib/excel/椿树街道房屋数据.zip");
//            ftp.changeWorkDir("/");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}