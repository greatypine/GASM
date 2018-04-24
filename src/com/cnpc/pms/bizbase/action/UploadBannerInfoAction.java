package com.cnpc.pms.bizbase.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.manager.AttachmentManager;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.BannerInfo;
import com.cnpc.pms.personal.manager.BannerInfoManager;

public class UploadBannerInfoAction extends HttpServlet{
	private static final long serialVersionUID = 1L;
	String FILE_ROOT = PropertiesUtil.getValue("file.root");
	//String picPath = PropertiesUtil.getValue("file.root").concat("user_image").concat(File.separator);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("调用文件上传的方法");
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        String function_name= req.getParameter("function_name");
        String id = req.getParameter("id");
        String ordernum = req.getParameter("ordernum");
        String open_url = req.getParameter("open_url");
        String fileLoad="upload_folder";

        AttachmentManager attachmentManager=(AttachmentManager)SpringHelper.getBean("attachmentManager");
        Attachment attachment =null;
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
        BannerInfoManager bannerInfoManager = (BannerInfoManager) SpringHelper.getBean("bannerInfoManager");
        //获取要使用的dao对象
        try {
        	List<File> lst_excelfile = new ArrayList<File>();
        	List<?> list = servletFileUpload.parseRequest(req);
            System.out.println("调用文件上传的方法："+list.size());
        	for (Object obj_item : list) {
        		 FileItem item = (FileItem)obj_item;
        		 String file_name = item.getName();
        		 if(file_name==null||file_name==""){
        			 BannerInfo bannerInfo = null;
                     if(id!=null){
                    	 bannerInfo =(BannerInfo) bannerInfoManager.getObject(Long.parseLong(id));
                    	 bannerInfo.setFunction_name(function_name);
                         bannerInfo.setOrdernum(Long.parseLong(ordernum));
                         bannerInfo.setOpen_url(open_url);
                         bannerInfoManager.saveBannerInfo(bannerInfo);
                    }
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
                    
                     String file_url = copyFile(file_upload, "upload_folder/banner");
                     
                     String[] split = item.getName().split("-");
                   
                     //调用保存方法 
                     BannerInfo bannerInfo = null;
                     if(id!=null){
                    	 bannerInfo =(BannerInfo) bannerInfoManager.getObject(Long.parseLong(id));
                    }else{
                    	 bannerInfo = new BannerInfo();
                    }
                     bannerInfo.setFunction_name(function_name);
                     bannerInfo.setOrdernum(Long.parseLong(ordernum));
                     bannerInfo.setBanner_url(name);
                     bannerInfo.setOpen_url(open_url);
                     bannerInfoManager.saveBannerInfo(bannerInfo);
                     
                 }
                  
			}
        	
        	System.gc();          
            writer.write("<script type='text/javascript'>window.parent.location.href='hr/banner_info_list.html';</script>" );
		}catch (NullPointerException e) {
            e.printStackTrace();
            writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \"失败\");</script>" );
        } catch (Exception e) {
        	e.printStackTrace();
            writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \""+e.getMessage()+"\");</script>" );
		}
        writer.flush();
        writer.close();
	}
	
	//如果导入数据库成功将文件复制到Tomcat文件夹下
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
		}
	
	
}
