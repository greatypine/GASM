package com.cnpc.pms.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

/**
 * 下载的工具类
 * create : lihai
 * version Version1.0
 */
public class DownloadUtil {

    public static boolean downLoadFile(String filePath,
                                       HttpServletResponse response, String fileName, String fileType)
            throws Exception {
        File file = new File(filePath);  //根据文件路径获得File文件
        //设置文件类型(这样设置就不止是下Excel文件了，一举多得)
        if("pdf".equals(fileType)){
            response.setContentType("application/pdf;charset=utf-8");
        }else if("xls".equals(fileType)){
            response.setContentType("application/msexcel;charset=utf-8");
        }else if("doc".equals(fileType)){
            response.setContentType("application/msword;charset=utf-8");
        }
        //文件名
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=\""
                + new String(fileName.getBytes(), "ISO8859-1") + "\"");
        response.setContentLength((int) file.length());
//        byte[] buffer = new byte[4096];// 缓冲区
        BufferedOutputStream output = null;
        BufferedInputStream input = null;
        ServletOutputStream outputStream = response.getOutputStream();
        InputStreamReader isr_inputReader = null;
        BufferedReader bf_reader = null;
        try {
            output = new BufferedOutputStream(response.getOutputStream());
            input = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
//            isr_inputReader = new InputStreamReader(input);
//            bf_reader = new BufferedReader(isr_inputReader);
//            StringBuilder sb_content = new StringBuilder();
//            //遍历，开始下载
//            while (bf_reader.ready()) {
//                sb_content.append(((char)bf_reader.read()));
//            }
            output.write(buffer);
            output.flush();   //不可少
            response.flushBuffer();//不可少
            outputStream.flush();
        } catch (Exception e) {
            //异常自己捕捉
        } finally {
            if(bf_reader != null)
                bf_reader.close();
            if(isr_inputReader != null)
                isr_inputReader.close();
            //关闭流，不可少
            if (input != null)
                input.close();
            if (output != null)
                output.close();
            if (outputStream != null)
                outputStream.close();
        }
        return false;
    }
}
