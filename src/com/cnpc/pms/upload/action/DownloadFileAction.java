package com.cnpc.pms.upload.action;

import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.utils.CompressFile;
import com.cnpc.pms.utils.DownloadUtil;
import com.cnpc.pms.utils.PropertiesValueUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 上传Excel的Aciton
 * create : liuxiao
 * version Version1.0
 */
public class DownloadFileAction extends HttpServlet {

    public String folder_path = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String str_folder_name = req.getParameter("folder_name");
        String str_file_name = URLDecoder.decode(req.getParameter("file_name"),"UTF-8");
        if(str_file_name.split("%").length > 2) {
            str_file_name = URLDecoder.decode(str_file_name, "UTF-8");
        }
        
        //配置文件中的路径
        String str_filepath = getFolder_path().concat(str_folder_name).concat(File.separator).concat(str_file_name);

        //获取文件名
        String str_suffix_name = str_file_name.substring(str_file_name.lastIndexOf(".") + 1,str_file_name.length());
        try {
            DownloadUtil.downLoadFile(str_filepath,resp,str_file_name,str_suffix_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * 获取上传文件夹路径
     * @return 上传文件夹路径
     */
    private String getFolder_path() {
        if(folder_path == null){
            String FILE_ROOT = PropertiesUtil.getValue("file.root");
            folder_path = FILE_ROOT.concat("upload_folder").concat(File.separator);
        }
        return folder_path;
    }

    public void setFolder_path(String folder_path) {
        this.folder_path = folder_path;
    }

}
