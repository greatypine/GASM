/**
 * gaobaolei
 */
package com.cnpc.pms.bizbase.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.cnpc.pms.base.file.collection.ReadCompanyInfo;
import com.cnpc.pms.base.file.collection.ReadExcel;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.dynamic.manager.DynamicManager;
import com.cnpc.pms.personal.manager.SelfExpressManager;
import com.cnpc.pms.utils.CompressFile;

/**
 * @author gaobaolei
 * 手动上传异常订单
 */
public class uploadAbnormalOrderAction extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("调用异常订单文件上传的方法");
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        InetAddress addr=InetAddress.getLocalHost(); 
        System.out.println("执行上传的服务器addr:"+addr);
        String address=addr.getHostName().toString();//获得本机名称  
        String ip= addr.toString();
        String businessType = req.getParameter("");
        System.out.println("执行上传的服务器ip:"+ip);
        String dataType = req.getParameter("dataType");
        //创建文件项目工厂对象
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //设置文件上传路径
        String upload = this.getServletContext().getRealPath(File.separator + "upload");
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
        String remark =null;
        try {
            List<File> lst_excelfile = new ArrayList<File>();
            
            List<?> list = servletFileUpload.parseRequest(req);
            System.out.println("调用文件上传的方法："+list.size());
            for (Object obj_item : list) {
                FileItem item = (FileItem)obj_item;
                if(!item.isFormField()){
                	String name = item.getName();
                    //新建文件
                    String str_filepath = upload + File.separator;
                    File file_dir_upload = new File(str_filepath);
                    boolean bResult = true;
                    if(!file_dir_upload.exists()){
                        bResult = file_dir_upload.mkdir();
                    }
                    File file_upload = new File(str_filepath + name);
                    if(!file_upload.exists()){
                        bResult = file_upload.createNewFile();
                    }
                    if(!bResult){
                        throw new Exception("创建上传异常订单文件失败！");
                    }
                    //将临时文件输出到本地
                    item.write(file_upload);
                    if(name.endsWith("xls") || name.endsWith("xlsx") ){
                        lst_excelfile.add(file_upload);
                    }else{
                        writer.write("<script type='text/javascript'>window.parent.showUploadResult(\"文件类型不是xls\");</script>");
                        return;
                    }
                }else{
                	System.out.println(remark);
                	remark=item.getString("UTF-8");
                	writer.write("<script type='text/javascript'>window.parent.showUploadResult(\""+remark+"\");</script>");
                    return;
                	
                }
                
            }
           
            
            String result = importAbnormalOrder(lst_excelfile,dataType);
            writer.write("<script type='text/javascript'>window.parent.showUploadResult(\""+result+"\");</script>");
            System.gc();
            deleteDir(new File(upload));
        }catch (NumberFormatException e) {
            e.printStackTrace();
            writer.write("<script type='text/javascript'>window.parent.showUploadResult(\"上传失败\");</script>");
           
        } catch (NullPointerException e) {
            e.printStackTrace();
            writer.write("<script type='text/javascript'>window.parent.showUploadResult(\"上传失败\");</script>");
        }catch(RuntimeException e){
        	e.printStackTrace();
        	deleteDir(new File(upload));
        	writer.write("<script type='text/javascript'>window.parent.showUploadResult(\""+e.getMessage()+"\");</script>");
        }catch (FileUploadException e) {
            e.printStackTrace();
            writer.write("<script type='text/javascript'>window.parent.showUploadResult(\"上传失败\");</script>");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("<script type='text/javascript'>window.parent.showUploadResult(\""+e.getMessage()+"\");</script>");
        } 
        writer.flush();
        writer.close();
	}
	
	 /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
	
	private  String importAbnormalOrder(List<File> lst_excelfile,String dataType) throws Exception {
        if(lst_excelfile.size() > 0){
        	try {
        		DynamicManager dynamicManager = (DynamicManager)SpringHelper.getBean("dynamicManager");
            	String result = dynamicManager.importAbnormalOrder(lst_excelfile,dataType);
            	if("".equals(result)){
            		return "sucess";
            	}
            	return result;
			} catch (Exception e) {
				e.printStackTrace();
				return "上传失败";
			}
        }
        
        return  "sucess";
    }
}
