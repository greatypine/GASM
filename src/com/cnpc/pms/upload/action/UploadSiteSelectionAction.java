package com.cnpc.pms.upload.action;

import com.cnpc.pms.base.common.action.DispatcherAction;
import com.cnpc.pms.base.common.model.ClientRequestObject;
import com.cnpc.pms.base.common.model.ClientResponseObject;
import com.cnpc.pms.base.exception.DispatcherException;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.base.util.StrUtil;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.DsAbnormalOrder;
import com.cnpc.pms.personal.entity.FlowDetail;
import com.cnpc.pms.personal.entity.ScoreRecordTotal;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.WorkInfo;
import com.cnpc.pms.personal.manager.DsAbnormalOrderManager;
import com.cnpc.pms.personal.manager.FlowDetailManager;
import com.cnpc.pms.personal.manager.ScoreRecordTotalManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.WorkInfoManager;
import com.cnpc.pms.personal.util.DataTransfromUtil;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by liu on 2016/7/6 0006.
 */
public class UploadSiteSelectionAction extends DispatcherAction {
	String FILE_ROOT = PropertiesUtil.getValue("file.root");
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
         response.setContentType("text/html");
         PrintWriter writer = response.getWriter();
         try{
             execute(request, response);
         }catch (Exception e){
             e.printStackTrace();
         }

         writer.flush();
         writer.close();
    	
    	/*System.out.println("调用文件上传的方法");
    	response.setContentType("text/html");
        PrintWriter writer = response.getWriter();	
        String fileLoad="siteselection";
        //创建文件项目工厂对象
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //设置文件上传路径
        String upload = this.getServletContext().getRealPath(File.separator + fileLoad);
        // 获取系统默认的临时文件保存路径，该路径为Tomcat根目录下的temp文件夹
        String temp = System.getProperty("java.io.tmpdir");
        // 设置缓冲区大小为 5M
        factory.setSizeThreshold(1024 * 1024 * 20);
        // 将文件上传到upload文件夹(或者直接使用流进行读取)
        factory.setRepository(new File(upload));
        // 用工厂实例化上传组件,ServletFileUpload 用来解析文件上传请求 
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        //获取参数,拿到要进行插入的表名称
        //获取要使用的dao对象
        String fileUrl = "";
        try {
        	List<File> lst_excelfile = new ArrayList<File>();
        	List<?> list = servletFileUpload.parseRequest(request);
            System.out.println("调用文件上传的方法："+list.size());
        	for (Object obj_item : list) {
        		 FileItem item = (FileItem)obj_item;
        		 String file_name = item.getName();
        		 if(file_name==null||file_name==""){
        			 break;
        		 }
                 if(!item.isFormField()){//验证是否为文件类型
                	 String name = item.getName();
                	 String file_type = "";
                	 if(name!=null&&name.contains(".")){
                		 file_type = name.substring(name.lastIndexOf("."), name.length());
                		 name = UUID.randomUUID().toString().replaceAll("-", "")+file_type;
                	 }
                     //新建文件
                     String str_filepath = upload + File.separator;//File.separator 在windows是 \  unix是 / 
                     File file_dir_upload = new File(str_filepath);
                     boolean bResult = true;
                     if(!file_dir_upload.exists()){//判断文件夹是否存在
                         bResult = file_dir_upload.mkdir();
                     }
                     File file_upload = new File(str_filepath + name);
                     if(!file_upload.exists()){
                         bResult = file_upload.createNewFile();
                     }
                     if(!bResult){
                         throw new Exception("创建上传文件夹活创建上传文件失败！");
                     }
                     //将临时文件输出到本地
                     item.write(file_upload);
                    
                     String file_url = copyFile(file_upload, "siteselection");
                     
                     String[] split = item.getName().split("-");
                     
                     
                     fileUrl =str_filepath + name;
                 }
                  
			}
            //上传完成后 。保存 申诉信息 
            System.gc();
		}catch (NullPointerException e) {
            e.printStackTrace();
            //writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \"上传失败\");</script>" );
        } catch (Exception e) {
        	e.printStackTrace();
        	//uploader_pic
        	//writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \""+e.getMessage()+"\");</script>" );
		}
        writer.write(fileUrl);
        response.getWriter().write(fileUrl);
        writer.flush();
        writer.close();*/

    }

    
    
/*  //如果导入数据库成功将文件复制到Tomcat文件夹下
  	public String copyFile(File file,String target_dir) throws Exception{
  		String str_webroot = FILE_ROOT.concat("/"+target_dir);
  		 File file_target_dir = new File(str_webroot);
           boolean bResult = true;
           if(!file_target_dir.exists()){
          	 if (!file_target_dir.isDirectory()) {
               bResult = file_target_dir.mkdir();
          	 }
           }
           File file_target_image =  new File(str_webroot +  File.separator + file.getName());
           if(file_target_image.exists()){
          	 bResult= file_target_image.delete();
           }
            FileUtils.copyFile(file,file_target_image);
            System.gc();
            if(file.exists()){
           	 bResult= file.delete();
            }
           return str_webroot +  File.separator + file.getName();
  	}*/
  	
  	
    @Override
    protected ClientRequestObject getClientRequestObject(HttpServletRequest request) throws DispatcherException {
        FileItemFactory factory = new DiskFileItemFactory();// Create a factory for disk-based file items
        ServletFileUpload upload = new ServletFileUpload(factory);// Create a new file upload handler
        // Parse the request
        List<?> items;
        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException e) {
            throw new DispatcherException("Fail to get uploaded file: " + e.getMessage());
        }

        Iterator i$ = items.iterator();
        String fileName = null;
        String img_type = null;
        String fname = null;
        while(i$.hasNext()) {
            Object o = i$.next();
            FileItem item = (FileItem) o;
            if(item.isFormField()){
                if("file_name".equals(item.getFieldName())){
                    fileName = item.getString();
                }
                if("img_type".equals(item.getFieldName())){
                    img_type = item.getString();
                }
                if("fname".equals(item.getFieldName())){
                    try {
                        fname = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        throw new DispatcherException(e.getMessage());
                    }
                }
            }
        }

        if(img_type == null){
            img_type = request.getParameter("img_type");
        }
        if(fileName == null){
            fileName = request.getParameter("file_name");
        }

        Object[] paras = null;
        String clientInvokeString = request.getParameter(REQUEST_STRING);
        ClientRequestObject requestObj = new ClientRequestObject();
        requestObj.setManagerName("UploadFolderManager");
        paras = new Object[] {"siteselection", items };
        requestObj.setMethodName("uploadObjectsSiteSelection");
        requestObj.setParameters(paras);
        return requestObj;
    }

    @Override
    protected void doResponse(ClientResponseObject responseObj, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().write(responseObj.toJSON());
    }

}
