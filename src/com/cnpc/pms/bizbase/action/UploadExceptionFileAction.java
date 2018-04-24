package com.cnpc.pms.bizbase.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.Spring;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.file.collection.ReadCompanyInfo;
import com.cnpc.pms.base.file.collection.ReadExcel;
import com.cnpc.pms.base.file.manager.ExcelManager;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.manager.AttachmentManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.messageModel.manager.MessageNewManager;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.DsAbnormalOrder;
import com.cnpc.pms.personal.entity.FlowDetail;
import com.cnpc.pms.personal.entity.ScoreRecordTotal;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.entity.WorkInfo;
import com.cnpc.pms.personal.manager.DsAbnormalOrderManager;
import com.cnpc.pms.personal.manager.FlowDetailManager;
import com.cnpc.pms.personal.manager.ScoreRecordManager;
import com.cnpc.pms.personal.manager.ScoreRecordTotalManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.personal.manager.WorkInfoManager;
import com.cnpc.pms.personal.util.DataTransfromUtil;

public class UploadExceptionFileAction extends HttpServlet{
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
        String app_reason= req.getParameter("app_reason");
        app_reason  = new String(app_reason.getBytes("ISO-8859-1"),"UTF-8");
        String order_sn = req.getParameter("order_sn");
        String model = req.getParameter("model");
        String store_no = req.getParameter("store_no");
        String fileLoad="";

        
        if("exceptionorder".equals(model)){
        	fileLoad="exceptionorder";
        }
        if("exceptionorderupdate".equals(model)){
        	fileLoad="exceptionorder";
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
                    
                     String file_url = copyFile(file_upload, "exceptionorder");
                     
                     String[] split = item.getName().split("-");
                     //判断是否是修改 
                     if("exceptionorderupdate".equals(model)){
                    	 List<Attachment> attachments = attachmentManager.findAttachmentByOrderSN(order_sn,2);
                    	 if(attachments!=null&&attachments.size()>0){
                    		 attachment = attachments.get(0);
                    	 }else{
                    		 attachment = new Attachment();
                    	 }
                     }else{
                    	 attachment = new Attachment();
                     }
                     attachment.setFile_type(2);
                     attachment.setBusiness_type(order_sn);
                     attachment.setFile_name(item.getName());
                     attachment.setFile_path(file_url.replaceAll("-", ""));
                     attachment.setApprove_status(0);
                     attachment.setMessage("");
                     attachment.setRemark(app_reason);
                    if("exceptionorder".equals(model)){
                    	attachment.setFile_type_name("异常订单申诉文件");
                    }
                    if("exceptionorderupdate".equals(model)){
                    	attachment.setFile_type_name("异常订单申诉文件");
                    }
                    if(name!=null&&name.length()>0){
                    	 DataTransfromUtil.preObject(attachment);
                         attachmentManager.saveObject(attachment);
                    }
                 }else{
                	 app_reason=item.getString("UTF-8");
                     System.out.println(app_reason);
                 }
                  
			}
        	
        	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
        	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
        	IFilter filter = FilterFactory.getSimpleFilter("storeno='"+store_no+"'");
        	List<?> lst_storeList = storeManager.getList(filter);
        	Store store = null;
        	if(lst_storeList!=null&&lst_storeList.size()>0){
        		store = (Store) lst_storeList.get(0);
        	}
        	if(store==null){
        		store = new Store();
        		store.setStore_id(userManager.getCurrentUserDTO().getStore_id());
        	}
            //上传完成后 。保存 申诉信息 
            if("exceptionorder".equals(model)){
            	  WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
            	  WorkInfo workInfo = new WorkInfo();
            	  workInfo.setApp_reason(app_reason);
            	  workInfo.setOrder_sn(order_sn);
            	  workInfo.setWork_type("异常订单申诉");
            	  String workmonth = new SimpleDateFormat("yyyy-MM").format(new Date());
            	  workInfo.setWork_month(workmonth);
            	  
            	  workInfo.setStore_id(store.getStore_id());
            	  //保存任务列表
            	  workInfoManager.saveWorkInfo(workInfo);
            	  WorkInfo new_workInfo = workInfoManager.updateCommitStatus(workInfo.getId());
            	  
            	  //更改清洗的订单状态 
            	  DsAbnormalOrderManager dsAbnormalOrderManager = (DsAbnormalOrderManager) SpringHelper.getBean("dsAbnormalOrderManager");
            	  DsAbnormalOrder dsAbnormalOrder = dsAbnormalOrderManager.queryDsAbnormalOrderBySN(workInfo.getOrder_sn());
            	  dsAbnormalOrder.setStatus("1");
            	  dsAbnormalOrder.setUpdatetime(workInfo.getUpdate_time());
            	  dsAbnormalOrderManager.saveDsAbnormalOrder(dsAbnormalOrder);
            	  
            	  //保存审批列表
            	  ScoreRecordTotalManager scoreRecordTotalManager = (ScoreRecordTotalManager) SpringHelper.getBean("scoreRecordTotalManager");
            	  ScoreRecordTotal scoreRecordTotal = new ScoreRecordTotal();
            	  scoreRecordTotal.setCityname(new_workInfo.getCityname());
            	  scoreRecordTotal.setCommit_date(new_workInfo.getCommit_date());
            	  scoreRecordTotal.setCommit_status(new_workInfo.getCommit_status());
            	  scoreRecordTotal.setStore_id(new_workInfo.getStore_id());
            	  scoreRecordTotal.setWork_month(new_workInfo.getWork_month());
            	  scoreRecordTotal.setWork_info_id(new_workInfo.getId());
            	  scoreRecordTotal.setStr_work_info_id(new_workInfo.getWork_type());
            	  preSaveObject(scoreRecordTotal);
            	  scoreRecordTotalManager.saveObject(scoreRecordTotal);
            	  
            	  
            	//将操作存入 审批记录表
          		FlowDetailManager flowDetailManager = (FlowDetailManager) SpringHelper.getBean("flowDetailManager");
          		FlowDetail flowDetail = new FlowDetail();
          		flowDetail.setWork_info_id(scoreRecordTotal.getWork_info_id());
          		flowDetail.setApprov_content(app_reason);
          		flowDetailManager.saveFlowDetail(flowDetail);
          		
          		//推送
          		//pushDZMsg(new_workInfo);
          		
          		writer.write("<script type='text/javascript'>window.parent.importSuccess('"+store_no+"');</script>" );
                System.gc();
                
            }else if("exceptionorderupdate".equals(model)){
            	
            	 WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
	           	  WorkInfo workInfo = workInfoManager.queryWorkInfoByOrderSN(order_sn);
	           	  workInfo.setApp_reason(app_reason);
	           	  workInfo.setOrder_sn(order_sn);
	           	  workInfo.setWork_type("异常订单申诉");
	           	  String workmonth = new SimpleDateFormat("yyyy-MM").format(new Date());
	           	  workInfo.setWork_month(workmonth);
	           	  
	           	  workInfo.setStore_id(store.getStore_id());
	           	
	           	  //保存任务列表
	           	  workInfoManager.saveWorkInfo(workInfo);
	           	  WorkInfo new_workInfo = workInfoManager.updateCommitStatus(workInfo.getId());
            	
	           	  
	           	  //更改清洗的订单状态 
            	  DsAbnormalOrderManager dsAbnormalOrderManager = (DsAbnormalOrderManager) SpringHelper.getBean("dsAbnormalOrderManager");
            	  DsAbnormalOrder dsAbnormalOrder = dsAbnormalOrderManager.queryDsAbnormalOrderBySN(workInfo.getOrder_sn());
            	  dsAbnormalOrder.setStatus("1");
            	  dsAbnormalOrder.setUpdatetime(workInfo.getUpdate_time());
            	  dsAbnormalOrderManager.saveDsAbnormalOrder(dsAbnormalOrder);
            	  
	           	  
	           	  //保存审批列表
            	  ScoreRecordTotalManager scoreRecordTotalManager = (ScoreRecordTotalManager) SpringHelper.getBean("scoreRecordTotalManager");
            	  ScoreRecordTotal scoreRecordTotal = scoreRecordTotalManager.queryScoreRecordTotalByWorkId(workInfo.getId());
            	  scoreRecordTotal.setCityname(new_workInfo.getCityname());
            	  scoreRecordTotal.setCommit_date(new_workInfo.getCommit_date());
            	  scoreRecordTotal.setCommit_status(new_workInfo.getCommit_status());
            	  scoreRecordTotal.setStore_id(new_workInfo.getStore_id());
            	  scoreRecordTotal.setWork_month(new_workInfo.getWork_month());
            	  scoreRecordTotal.setWork_info_id(new_workInfo.getId());
            	  scoreRecordTotal.setStr_work_info_id(new_workInfo.getWork_type());
            	  preSaveObject(scoreRecordTotal);
            	  scoreRecordTotalManager.saveObject(scoreRecordTotal);
            	  
            	
            	
            	//将操作存入 审批记录表
          		FlowDetailManager flowDetailManager = (FlowDetailManager) SpringHelper.getBean("flowDetailManager");
          		FlowDetail flowDetail = new FlowDetail();
          		flowDetail.setWork_info_id(scoreRecordTotal.getWork_info_id());
          		flowDetail.setApprov_content(app_reason);
          		flowDetailManager.saveFlowDetail(flowDetail);
          		
          		//推送
          		//pushDZMsg(new_workInfo);
          		
          		writer.write("<script type='text/javascript'>window.parent.importSuccess('"+store_no+"');</script>" );
                System.gc();
                
            }
          
            
            
            
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
	
	
	public void pushDZMsg(WorkInfo new_workInfo){
		MessageNewManager messageNewManager = (MessageNewManager) SpringHelper.getBean("messageNewManager");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		UserDTO userDTO = userManager.getCurrentUserDTO();
		messageNewManager.sendMessage_common(userDTO.getEmployeeId(), new_workInfo.getCurr_appro_id(), "abnormal_order_apply", new_workInfo.getOrder_sn());
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
	//删除文件
	public void deleteFile(File file) throws Exception{
		if (file.isFile() && file.exists()) {  
	        file.delete();
	    }else{
	    	throw  new Exception("文件删除失败！");
	    }
	}
	
	/*public void uploadBusinessExcelToDataSource(){
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
					attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType());
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
					attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType());
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
	}*/
	
	
	protected void preSaveObject(Object o) {
		if (o instanceof DataEntity) {
			User sessionUser = null;
			if (null != SessionManager.getUserSession()
					&& null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession()
						.getSessionData().get("user");
			}
			DataEntity dataEntity = (DataEntity) o;
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			// insert处理时添加创建人和创建时间
			if (null == dataEntity.getCreate_time()) {
				dataEntity.setCreate_time(sdate);
				if (null != sessionUser) {
					dataEntity.setCreate_user(sessionUser.getCode());
					dataEntity.setCreate_user_id(sessionUser.getId());
				}
			}
			dataEntity.setUpdate_time(sdate);
			if (null != sessionUser) {
				dataEntity.setUpdate_user(sessionUser.getCode());
				dataEntity.setUpdate_user_id(sessionUser.getId());
			}
		}
	}
	
}
