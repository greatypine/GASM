package com.cnpc.pms.bizbase.action;

import com.cnpc.pms.base.file.collection.ReadCompanyInfo;
import com.cnpc.pms.base.file.collection.ReadExcel;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.personal.manager.*;
import com.cnpc.pms.utils.CompressFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.mapping.Array;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 上传Excel的Aciton
 * create : lihai
 * version Version1.0
 */
public class UpLoadExcelAction extends HttpServlet {
    String FILE_ROOT = PropertiesUtil.getValue("file.root");



    @Override
    protected void doGet(HttpServletRequest objRequest, HttpServletResponse objResponse) throws ServletException, IOException {
        doPost(objRequest, objResponse);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	System.out.println("调用文件上传的方法");
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        String model = req.getParameter("model");
        InetAddress addr=InetAddress.getLocalHost(); 
        System.out.println("执行上传的服务器addr:"+addr);
        //String ip=addr.getHostAddress().toString();//获得本机IP　　  
        String address=addr.getHostName().toString();//获得本机名称  
      String ip= addr.toString();
      
        System.out.println("执行上传的服务器ip:"+ip);
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
            List<File> lst_image = new ArrayList<File>();
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
                        throw new Exception("创建上传文件夹活创建上传文件失败！");
                    }
                    //将临时文件输出到本地
                    item.write(file_upload);
                    if (name.endsWith("zip")) {
                        CompressFile.unZipFiles(file_upload,str_filepath);
                        getExcelList(lst_excelfile,lst_image,str_filepath);
                    }else if(name.endsWith("rar")){
                        CompressFile.unRarFile(file_upload.getAbsolutePath(),str_filepath);
                        getExcelList(lst_excelfile,lst_image,str_filepath);
                    }else if(name.endsWith("xls") || name.endsWith("xlsx") ){
                        lst_excelfile.add(file_upload);
                    }else{
                        writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \"导入失败，文件类型不是xls、rar或zip\");</script>" );
                        return;
                    }
                }else{
                	remark=item.getString("UTF-8");
           
                	System.out.println(remark);
                }
                
            }
            if("house_info_template".equals(model)){
                importHouseType(lst_excelfile,lst_image,"house_type_image");
            }else if("customer_info_template".equals(model)){
                importUserInfo(lst_excelfile,lst_image,"user_image");
            }else if("self_express_template".equals(model)){
            	importSelfExpress(lst_excelfile);
            }else if("xx_express_template".equals(model)){
            	importXxExpress(lst_excelfile);
            }else if("self_express_template_tmp".equals(model)){
            	importSelfExpressTmp(lst_excelfile);
            }else if("human_template".equals(model)){
            	String retmsg = importHumanresources(lst_excelfile);
            	if(retmsg!=null){
            		throw new Exception(retmsg);
            	}
            }else if("human_tech_template".equals(model)){
            	importHumantech(lst_excelfile);
            }else if("human_template_hstk".equals(model)){
            	String retmsg = importHumanHSTK(lst_excelfile);
            	if(retmsg!=null){
            		throw new Exception(retmsg);
            	}
            }else if("human_template_cshuman".equals(model)){
            	String retmsg = importHumanCSHuman(lst_excelfile);
            	if(retmsg!=null){
            		throw new Exception(retmsg);
            	}
            }else if("t_commtity".equals(model)){
            	String string = req.getParameter("metho");
                System.out.println(string+"metho");
                System.out.println("上传地址文件的文件数量："+lst_excelfile.size());
                List<File> repeat = removeRepeat(lst_excelfile);
               new ReadExcel().batchExcel(repeat,string,remark,ip);
            }else if("t_buildInfo".equals(model)){
                System.out.println("上传写字楼楼信息");
                List<File> repeat = removeRepeat(lst_excelfile);
               new ReadCompanyInfo().readExcel(repeat,ip);
            }else if("t_update_company".equals(model)){
                System.out.println("更新写字楼信息");
               new ReadCompanyInfo().updateOffice(lst_excelfile);
            }else if("t_businessInfo".equals(model)){
                System.out.println("上传街道商业楼信息");
                System.out.println("上传商业信息文件数量"+lst_excelfile.size());
                List<File> repeat = removeRepeat(lst_excelfile);
               new ReadCompanyInfo().readBusinessExcel(repeat,ip);
            }
            writer.write("<script type='text/javascript'>window.parent.importSuccess();</script>" );
            System.gc();
            deleteDir(new File(upload));
        }catch (NumberFormatException e) {
            e.printStackTrace();
            writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \"导入失败,请核对数据类型\");</script>" );
        } catch (NullPointerException e) {
            e.printStackTrace();
            writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \"导入失败\");</script>" );
        }catch(RuntimeException e){
        	e.printStackTrace();
        	deleteDir(new File(upload));
        	writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \""+(e.getMessage().replaceAll("\\\\", "/"))+"\");</script>" );
        }catch (FileUploadException e) {
            e.printStackTrace();
            writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \"导入失败\");</script>" );
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \""+e.getMessage()+"\");</script>" );
        } 
        writer.flush();
        writer.close();
    }

    
    
    public void importSelfExpress(List<File> lst_excelfile) throws Exception {
        if(lst_excelfile.size() > 0){
        	SelfExpressManager selfExpressManager = (SelfExpressManager)SpringHelper.getBean("selfExpressManager");
            selfExpressManager.importSelfExpressForExcel(lst_excelfile);
        }
    }
    
    public void importSelfExpressTmp(List<File> lst_excelfile) throws Exception {
        if(lst_excelfile.size() > 0){
        	SelfExpressManager selfExpressManager = (SelfExpressManager)SpringHelper.getBean("selfExpressManager");
            selfExpressManager.importSelfExpressTemplateForExcel(lst_excelfile);
        }
    }
    
    public String importHumanresources(List<File> lst_excelfile) throws Exception {
        if(lst_excelfile.size() > 0){
        	ImportHumanresourcesManager importHumanresourcesManager = (ImportHumanresourcesManager)SpringHelper.getBean("importHumanresourcesManager");
        	return importHumanresourcesManager.importHumanresourceForExcel(lst_excelfile);
        }
        return null;
    }
    
    
    public void importHumantech(List<File> lst_excelfile) throws Exception {
        if(lst_excelfile.size() > 0){
        	HumanresourcesManager humanresourcesManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
        	humanresourcesManager.importHumanresourceTeach(lst_excelfile);
        }
    }
    
    
    public String importHumanHSTK(List<File> lst_excelfile) throws Exception {
        if(lst_excelfile.size() > 0){
        	HumanresourcesManager humanresourcesManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
        	return humanresourcesManager.saveHumanresourceHSTK(lst_excelfile);
        }
        return null;
    }
    
    
    public String importHumanCSHuman(List<File> lst_excelfile) throws Exception {
        if(lst_excelfile.size() > 0){
        	CityHumanresourcesManager cityHumanresourcesManager = (CityHumanresourcesManager) SpringHelper.getBean("cityHumanresourcesManager");
        	return cityHumanresourcesManager.saveHumanresourceCSHuman(lst_excelfile);
        }
        return null;
    }
    
    
    
    public void importXxExpress(List<File> lst_excelfile) throws Exception {
        if(lst_excelfile.size() > 0){
        	XxExpressManager xxExpressManager = (XxExpressManager)SpringHelper.getBean("xxExpressManager");
        	xxExpressManager.importXxExpressForExcel(lst_excelfile);
        }
    }
    
    /**
     * 将解压出的图片文件放入目标文件夹中
     * @param lst_image 图片集合
     * @param target_dir 目标文件夹
     * @throws Exception
     */
    private void moveImage(List<File> lst_image,String target_dir) throws Exception{
        String str_webroot = FILE_ROOT.concat(target_dir);
        for(File file_image : lst_image){
            File file_target_dir = new File(str_webroot);
            boolean bResult = true;
            if(!file_target_dir.exists()){
                bResult = file_target_dir.mkdir();
            }
            File file_target_image =  new File(str_webroot +  File.separator + file_image.getName());
            if(file_target_image.exists()){
                bResult = file_target_image.delete();
            }
            bResult = file_image.renameTo(file_target_image);
            if(!bResult){
                throw  new Exception("移动图片失败！");
            }
        }
    }

    private void importUserInfo(List<File> lst_excelfile,List<File> lst_image,String target_dir) throws Exception{
        moveImage(lst_image,target_dir);
//        if(lst_excelfile.size() > 0){
//            CustomerManager customerManager = (CustomerManager)SpringHelper.getBean("customerManager");
//            customerManager.saveOrUpdateCustomerByExcel(lst_excelfile);
//        }
    }

    /**
     * 导入房屋户型信息
     * @param lst_excelfile 导入数据的excel文档集合
     * @param lst_image 导入的户型图片
     * @param target_dir 户型图片目标文件夹
     * @throws Exception
     */
    private void importHouseType(List<File> lst_excelfile,List<File> lst_image,String target_dir) throws Exception{
        moveImage(lst_image,target_dir);
        if(lst_excelfile.size() > 0){
            HouseStyleManager houseStyleManager = (HouseStyleManager)SpringHelper.getBean("houseStyleManager");
            houseStyleManager.saveOrUpdateHouseStyleAndCustomer(lst_excelfile);
        }
    }


    /**
     * 获取所有的excel文件
     * @param str_upload_dir 上传文件路径
     * @param lst_excelfile 文件集合
     */
    private void getExcelList(List<File> lst_excelfile,List<File> lst_image,String str_upload_dir){
        File file_upload_dir = new File(str_upload_dir);
        if(!file_upload_dir.isDirectory()){
            return;
        }
        File[] file_uploadfiles = file_upload_dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().endsWith("xls") || pathname.getName().endsWith("xlsx")
                        || pathname.getName().endsWith("jpg") || pathname.getName().endsWith("png");
            }
        });

        for(File file_excel : file_uploadfiles){
            if(file_excel.isDirectory()){
                getExcelList(lst_excelfile,lst_image,file_excel.getAbsolutePath());
            }else if(file_excel.getName().endsWith("xls") || file_excel.getName().endsWith("xlsx")){
                lst_excelfile.add(file_excel);
            }else{
                lst_image.add(file_excel);
            }
        }
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
    public String testGetIp() {
    	String ipaddress=null;
        BufferedReader br = null;
        try {
            URL url = new URL("http://ip.chinaz.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String s = "";
            StringBuffer sb = new StringBuffer();
            while ( (s = br.readLine() ) != null) {
                sb.append(s);
            }
            Pattern p = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
            Matcher m = p.matcher(sb.toString());
            if (m.find()){
                System.out.println(m.group());
                ipaddress=m.group();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != br)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ipaddress;
    }

    //去除list中的重复记录
    public List<File> removeRepeat(List<File> list){
    	//将所有的文件名存到list中
    	List<String> FileNameList= new ArrayList<String>();
    	for (File file : list) {
    		FileNameList.add(file.getName());
		}
    	List<String> listTemp= new ArrayList<String>();
    	Iterator<String> it=FileNameList.iterator();  
    	while(it.hasNext()){  
    		   String str = it.next();  
    		  if(listTemp.contains(str)){  
    		   it.remove();  
    		  }  
    		  else{  
    		   listTemp.add(str);  
    		  }  
    		 }  
    	System.out.println("处理后的文件数量"+listTemp.size());
    	if(list.size()>listTemp.size()){
    		 throw new RuntimeException("文件重复上传了");
    	}
    	ArrayList<File> arrayList = new ArrayList<File>();
		for (File file  : list) {
			for (String string : listTemp){
				if(file.getName().equals(string)){
					arrayList.add(file);
				}
			}
		}
    	
    	return arrayList;
    }
    
    
    
}
