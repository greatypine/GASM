package com.cnpc.pms.bizbase.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.personal.entity.Maintenance;
import com.cnpc.pms.personal.manager.MaintenanceManager;
import com.cnpc.pms.personal.util.ExcelHandle;
import com.cnpc.pms.personal.util.MaintenanceUtils;
import com.ibm.db2.jcc.am.i;

/**
 * 根据模板导出维修检测报告
 * @author zhao_xuguang
 *
 */
@SuppressWarnings("all")
public class DownloadTempateAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String id = request.getParameter("id");
    	String ids = request.getParameter("ids");
    	//////////单条导出////////
    	if(id!=null&&id.length()>0){
    		String baseUrl = request.getSession().getServletContext().getRealPath("/");
        	String tempFilePath =baseUrl+"template/template.xls";
        	MaintenanceManager maintenanceManager = (MaintenanceManager)SpringHelper.getBean("maintenanceManager");
        	Maintenance maint = maintenanceManager.exportExcel(id);
        	String finalFileName = URLEncoder.encode(maint.getName()+"_"+maint.getMobilephone()+".xls","UTF8");
        	//调用生成file
    		File file = preFile(baseUrl, tempFilePath, maint, finalFileName);
    		/////////启动下载开始///////
    		try {
    		   String contentType = "application/x-download";
    		   response.setContentType(contentType);
    		   response.setHeader("Content-Disposition", "attachment;filename="  + new String(finalFileName));
    		   ServletOutputStream out = response.getOutputStream();
    		   byte[] bytes = new byte[0xffff];
    		   InputStream is = new FileInputStream(file);
    		   int b = 0;
    		   while ((b = is.read(bytes, 0, 0xffff)) > 0) {
    		       out.write(bytes, 0, b);
    		   }
    		   is.close();
    		   out.flush();
    		   out.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}else{
    		//////////批量导出////////
    		String baseUrl = request.getSession().getServletContext().getRealPath("/");
        	String tempFilePath =baseUrl+"template/template.xls";
    		if(ids!=null&&ids.length()>0){
    			String[] subids = ids.split(",");
    			List<File> files = new ArrayList<File>();
    			for(int m=0;m<subids.length;m++){
                	MaintenanceManager maintenanceManager = (MaintenanceManager)SpringHelper.getBean("maintenanceManager");
                	Maintenance maint = maintenanceManager.exportExcel(subids[m]);
                	String finalFileName = maint.getName()+"_"+maint.getMobilephone()+"_"+m+".xls";
                	//循环调用生成file
                	File file = preFile(baseUrl, tempFilePath, maint, finalFileName);
            		files.add(file);
    			}
    			//调用生成zip
    			File zipfile = builderZip(files,request);
    			/////////启动下载开始zip//////
        		try {
        		   String contentType = "application/x-download";
        		   response.setContentType(contentType);
        		   response.setHeader("Content-Disposition", "attachment;filename="  + new String(zipfile.getName()));
        		   ServletOutputStream out = response.getOutputStream();
        		   byte[] bytes = new byte[0xffff];
        		   InputStream is = new FileInputStream(zipfile);
        		   int b = 0;
        		   while ((b = is.read(bytes, 0, 0xffff)) > 0) {
        		       out.write(bytes, 0, b);
        		   }
        		   is.close();
        		   out.flush();
        		   out.close();
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
    		}
    	}
    	
    }

    //生成file
	private File preFile(String baseUrl, String tempFilePath,
			Maintenance maint, String finalFileName) throws IOException,
			FileNotFoundException {
		//////////////////写入单个开始/////////////////////
		ExcelHandle handle = new  ExcelHandle();
		List<String> dataCell = new ArrayList<String>();
		Field[] fieldsNames = MaintenanceUtils.class.getDeclaredFields();
		for(int i=0;i<fieldsNames.length;i++){
			Field f = fieldsNames[i];
			String fieldName = f.toString().substring(f.toString().lastIndexOf(".")+1,f.toString().length());
			dataCell.add(fieldName);
		}
		MaintenanceUtils maintenance = new MaintenanceUtils();
		try {
			BeanUtils.copyProperties(maint, maintenance);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Map<String,Object> dataMap = new  HashMap<String, Object>();
		try {
			dataMap = getStringMap(maintenance);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		handle.writeData(tempFilePath, dataCell, dataMap, 0);
//    		/*sheet1*/
//    		List<String> dataCell1 = new ArrayList<String>();
//    		dataCell1.add("address1");
//    		Map<String,Object> dataMap1 = new  HashMap<String, Object>();
//    		dataMap1.put("address1", maintenance.getAddress());
//    		handle.writeData(tempFilePath, dataCell1, dataMap1, 1);
		//////////////////写入单个结束/////////////////////
		File file = new File(baseUrl+"/template/"+finalFileName);
		OutputStream os = new FileOutputStream(file);
		//写到输出流并关闭资源
		handle.writeAndClose(tempFilePath, os);
		os.flush();
		os.close();
		handle.readClose(tempFilePath);
		return file;
	}
    
    //生成zip方法
  	private File builderZip(List<File> file1,HttpServletRequest request) throws FileNotFoundException, IOException {
  		   byte[] buffer = new byte[1024];   
  		   String zipUrl = request.getSession().getServletContext().getRealPath("/")+"/template/batchexport"+System.currentTimeMillis()+".zip";
  	       ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipUrl));   
  	       for(int i=0;i<file1.size();i++) {   
  	           FileInputStream fis = new FileInputStream(file1.get(i));   
  	           out.putNextEntry(new ZipEntry(file1.get(i).getName()));   
  	           int len;   
  	           //读入需要下载的文件的内容，打包到zip文件   
  	           while((len = fis.read(buffer))>0) {   
  	            out.write(buffer,0,len);    
  	           }   
  	           out.closeEntry();   
  	           fis.close();   
  	       }   
  	       out.close();
  	       return new File(zipUrl);
  	}
  	
  	//实体类赋值
    public Map<String,Object> getStringMap(Object object) throws Exception {
		// 获得对象类型
		Map<String,Object> dataMap = new  HashMap<String, Object>();
		Class classType = object.getClass();
		Field fields[] = classType.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			String firstLetter = fieldName.substring(0, 1).toUpperCase(); // 获得和属性对应的getXXX()方法的名字
			String getMethodName = "get" + firstLetter + fieldName.substring(1); // 获得和属性对应的getXXX()方法的名字
			if(fieldName.indexOf("_1")>0||fieldName.indexOf("_2")>0){
				getMethodName=getMethodName+"_t";
			}
			Method getMethod = classType.getMethod(getMethodName,new Class[] {}); // 获得和属性对应的getXXX()方法
			Object value = getMethod.invoke(object, new Object[] {});
			dataMap.put(fieldName, value);
		}
		return dataMap;
	}
    

}
