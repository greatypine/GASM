package com.cnpc.pms.bizbase.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.cnpc.pms.base.file.collection.ReadCompanyInfo;
import com.cnpc.pms.base.file.collection.ReadExcel;
import com.cnpc.pms.base.file.manager.ExcelManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.manager.AttachmentManager;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.personal.util.DataTransfromUtil;

public class UploadGatherInfoAction extends HttpServlet{
	String FILE_ROOT = PropertiesUtil.getValue("file.root");
	String picPath = PropertiesUtil.getValue("file.root").concat("user_image").concat(File.separator);
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
       String remark= req.getParameter("remark");
        String model = req.getParameter("model");
        String fileLoad="";
        TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
        VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
        if("t_commtity".equals(model)){
        	fileLoad="house";
        }else if ("t_buildInfo".equals(model)) {
        	fileLoad="office";
		}else if ("t_businessInfo".equals(model)) {
			fileLoad="business";
		}
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
        //获取要使用的dao对象
        try {
        	List<File> lst_excelfile = new ArrayList<File>();
        	List<?> list = servletFileUpload.parseRequest(req);
            System.out.println("调用文件上传的方法："+list.size());
        	for (Object obj_item : list) {
        		 FileItem item = (FileItem)obj_item;
                 if(!item.isFormField()){//验证是否为文件类型
                	 String name = item.getName();
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
                     if(name.endsWith("xls") || name.endsWith("xlsx") ){
                         lst_excelfile.add(file_upload);
                     }else{
                         writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \"导入失败，文件类型不是xls、xlsx、rar或zip\");</script>" );
                         return;
                     }
                     String[] split = item.getName().split("-");
                     attachment = new Attachment();
                     attachment.setFile_name(item.getName());
                     attachment.setFile_path(file_upload.getPath());
                     attachment.setApprove_status(0);
                     attachment.setMessage("上传中");
                     attachment.setRemark(remark);
                    if("t_commtity".equals(model)){
                    	attachment.setFile_type_name("地址文件");
                    	if(split.length==2){
                    		Village village = villageManager.getVillageByGb_code(split[0]);
                    		if(village==null){
                    			attachment.setUploadType("上传失败");
                        		attachment.setMessage("街道不存在");
                        		file_upload.delete();
                    		}else{
                    			Town town = townManager.getTownById(village.getTown_id());
                    			if(town==null){
                    				attachment.setUploadType("上传失败");
                            		attachment.setMessage("街道不存在");
                            		file_upload.delete();
                    			}else{
                    				attachment.setUploadType("上传中");
                            		attachment.setTownName(town.getName());;
                    			}
                    		}
                    	}else{
                    		attachment.setUploadType("上传失败");
                    		attachment.setMessage("文件名格式不正确");
                    		file_upload.delete();
                    	}
                    }else if ("t_buildInfo".equals(model)) {
                    	if(split.length==2){
                    		Town town = townManager.getTownByGb_code_vir(split[0]);
                    		if(town==null){
                				attachment.setUploadType("上传失败");
                        		attachment.setMessage("街道不存在");
                        		file_upload.delete();
                			}else{
                				attachment.setUploadType("上传中");
                        		attachment.setTownName(town.getName());;
                			}
                    	}else{
                    		attachment.setUploadType("上传失败");
                    		attachment.setMessage("文件名格式不正确");
                    		file_upload.delete();
                    	}
                    	attachment.setFile_type_name("写字楼文件");
            		}else if ("t_businessInfo".equals(model)) {
            			if(split.length==2){
                    		Town town = townManager.getTownByGb_code_vir(split[0]);
                    		if(town==null){
                				attachment.setUploadType("上传失败");
                        		attachment.setMessage("街道不存在");
                        		file_upload.delete();
                			}else{
                				attachment.setUploadType("上传中");
                        		attachment.setTownName(town.getName());;
                			}
                    	}else{
                    		attachment.setUploadType("上传失败");
                    		attachment.setMessage("文件名格式不正确");
                    		file_upload.delete();
                    	}
            			attachment.setFile_type_name("商业信息文件");
            		}
                    DataTransfromUtil.preObject(attachment);
                    attachmentManager.saveObject(attachment);
                 }else{
                 	remark=item.getString("UTF-8");
                    System.out.println(remark);
                 }
                  
			}
        	writer.write("<script type='text/javascript'>window.parent.importSuccess();</script>" );
            System.gc();
		}catch (NullPointerException e) {
            e.printStackTrace();
            writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \"导入失败\");</script>" );
        } catch (Exception e) {
        	e.printStackTrace();
            writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \""+e.getMessage()+"\");</script>" );
		}
        writer.flush();
        writer.close();
	}
	 /**
	  * 将文件中的数据上传到数据库中
	 * @throws Exception 
	  */
	public void uploadExcelToDataSource(){
		
		AttachmentManager attachmentManager=(AttachmentManager)SpringHelper.getBean("attachmentManager");
		ExcelManager excelManager =(ExcelManager)SpringHelper.getBean("excelManager");
		//扫描文件夹下是否有文件
		String upload = this.getClass().getClassLoader().getResource("../../").getPath()+"house";
		File file=new File(upload);
		boolean bResult = true;
		 if(!file.exists()){
			 if (!file.isDirectory()) {
             bResult = file.mkdir();
			 }
         }
		 File[] listFiles = file.listFiles();
		 if(listFiles.length>0){
			 for (File file2 : listFiles) {//读取文件
				 String str=null;
				//文件是Excel
				 String village = excelManager.getTinyVillageByVillage(file2.getName());
				 Attachment attachment = attachmentManager.findAttachmentByName(file2.getName(),"地址文件");
				 if(village!=null){str="thod";}
				 
				 try {
					new ReadExcel().batchExcelData(file2, str, attachment);
					copyFile(file2,"house");
				}catch (NullPointerException e){
					attachment.setUploadType("上传失败");
					attachment.setMessage("NullPointerException");
					attachmentManager.saveObject(attachment);
					attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType(),null);
					try {
						copyFile(file2,"error");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}catch(RuntimeException e){
					attachment.setUploadType("上传失败");
					attachment.setMessage(e.getMessage());
					attachmentManager.saveObject(attachment);
					attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType(),null);
					try {
						copyFile(file2,"error");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception e) {
					try {
						copyFile(file2,"error");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		 }
	}
	 /**
	  * 将文件中的写字楼数据上传到数据库中
	 * @throws Exception 
	  */
	public void uploadOfficeExcelToDataSource(){
		AttachmentManager attachmentManager=(AttachmentManager)SpringHelper.getBean("attachmentManager");
		//扫描文件夹下是否有文件
		String upload = this.getClass().getClassLoader().getResource("../../").getPath()+"office";
		File file=new File(upload);
		boolean bResult = true;
		 if(!file.exists()){
			 if (!file.isDirectory()) {
            bResult = file.mkdir();
			 }
        }
		 File[] listFiles = file.listFiles();
		 if(listFiles.length>0){
			 for (File file2 : listFiles) {//读取文件
				 String str=null;
				 Attachment attachment = attachmentManager.findAttachmentByName(file2.getName(),"写字楼文件");
				 try {
					 new ReadCompanyInfo().readOfficeExcel(file2,attachment);
					copyFile(file2,"office");
				}catch (NullPointerException e){
					attachment.setUploadType("上传失败");
					attachment.setMessage("NullPointerException");
					attachmentManager.saveObject(attachment);
					attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType(),null);
					try {
						copyFile(file2,"error");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}catch (RuntimeException e){
					attachment.setUploadType("上传失败");
					attachment.setMessage(e.getMessage());
					attachmentManager.saveObject(attachment);
					attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType(),null);
					try {
						copyFile(file2,"error");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception e) {
					try {
						copyFile(file2,"error");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		 }
	}
	
	 /**
	  * 将文件中的商业信息数据上传到数据库中
	 * @throws Exception 
	  */
	public void uploadBusinessExcelToDataSource(){
		AttachmentManager attachmentManager=(AttachmentManager)SpringHelper.getBean("attachmentManager");
		//扫描文件夹下是否有文件
		String upload = this.getClass().getClassLoader().getResource("../../").getPath()+"business";
		File file=new File(upload);
		boolean bResult = true;
		 if(!file.exists()){
			 if (!file.isDirectory()) {
           bResult = file.mkdir();
			 }
       }
		 File[] listFiles = file.listFiles();
		 if(listFiles.length>0){
			 for (File file2 : listFiles) {//读取文件
				 String str=null;
				 Attachment attachment = attachmentManager.findAttachmentByName(file2.getName(),"商业信息文件");
				 try {
					 new ReadCompanyInfo().readBusinessDataExcel(file2,attachment);
					copyFile(file2,"business");
				} catch (NullPointerException e){
					attachment.setUploadType("上传失败");
					attachment.setMessage("NullPointerException");
					attachmentManager.saveObject(attachment);
					attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType(),null);
					try {
						copyFile(file2,"error");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}catch (RuntimeException e){
					attachment.setUploadType("上传失败");
					attachment.setMessage(e.getMessage());
					attachmentManager.saveObject(attachment);
					attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType(),null);
					try {
						copyFile(file2,"error");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}catch (Exception e) {
					try {
						copyFile(file2,"error");
					} catch (Exception e1) {
						System.out.println(e.getMessage());
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		 }
	}
	
	
	
	
	//如果导入数据库成功将文件复制到Tomcat文件夹下
	public void copyFile(File file,String target_dir) throws Exception{
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
         
	}
	//删除文件
	public void deleteFile(File file) throws Exception{
		if (file.isFile() && file.exists()) {  
	        file.delete();
	    }else{
	    	throw  new Exception("文件删除失败！");
	    }
	}
	
	
	
	
	
	
}
