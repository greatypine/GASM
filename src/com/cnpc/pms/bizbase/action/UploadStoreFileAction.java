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
import com.cnpc.pms.personal.util.DataTransfromUtil;

public class UploadStoreFileAction extends HttpServlet {
	String FILE_ROOT = PropertiesUtil.getValue("file.root");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("调用文件上传的方法");
		String model = req.getParameter("model");
		String store_id = req.getParameter("store_id");

		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		String fileLoad = "";
		if ("contract".equals(model)) {
			fileLoad = "contract";
		} else if ("card".equals(model)) {
			fileLoad = "card";
		}
		AttachmentManager attachmentManager = (AttachmentManager) SpringHelper.getBean("attachmentManager");
		Attachment attachment = null;
		// 创建文件项目工厂对象
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置文件上传路径
		String upload = this.getServletContext().getRealPath(File.separator + fileLoad);
		// 获取系统默认的临时文件保存路径，该路径为Tomcat根目录下的temp文件夹
		String temp = System.getProperty("java.io.tmpdir");
		// 设置缓冲区大小为 5M
		factory.setSizeThreshold(1024 * 1024 * 20);
		// 将文件上传到upload文件夹(或者直接使用流进行读取)
		factory.setRepository(new File(upload));
		// 用工厂实例化上传组件,ServletFileUpload 用来解析文件上传请求
		ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
		// 获取参数,拿到要进行插入的表名称
		// 获取要使用的dao对象
		try {
			List<File> lst_excelfile = new ArrayList<File>();
			List<?> list = servletFileUpload.parseRequest(req);
			System.out.println("调用文件上传的方法：" + list.size());
			for (Object obj_item : list) {
				FileItem item = (FileItem) obj_item;
				if ("contract_file".equals(item.getFieldName()) || "audit_file".equals(item.getFieldName())
						|| "plane_plan".equals(item.getFieldName()) || "record_drawing".equals(item.getFieldName())
						|| "business_license".equals(item.getFieldName()) || "food_card".equals(item.getFieldName())
						|| "tobacco_card".equals(item.getFieldName()) || "book_card".equals(item.getFieldName())
						|| "xx_card".equals(item.getFieldName()) || "other_card".equals(item.getFieldName())) {
					String file_name = item.getName();
					if (file_name == null || file_name == "") {
						continue;
					}
					if (!item.isFormField()) {// 验证是否为文件类型
						String name = item.getName();
						String file_type = "";
						if (name != null && name.contains(".")) {
							file_type = name.substring(name.lastIndexOf("."), name.length());
							name = UUID.randomUUID().toString().replaceAll("-", "") + file_type;
						}
						// 新建文件
						String str_filepath = upload + File.separator;// File.separator
																		// 在windows是
																		// \
																		// unix是
																		// /
						File file_dir_upload = new File(str_filepath);
						boolean bResult = true;
						if (!file_dir_upload.exists()) {// 判断文件夹是否存在
							bResult = file_dir_upload.mkdir();
						}
						File file_upload = new File(str_filepath + name);
						if (!file_upload.exists()) {
							bResult = file_upload.createNewFile();
						}
						if (!bResult) {
							throw new Exception("创建上传文件夹活创建上传文件失败！");
						}
						// 将临时文件输出到本地
						item.write(file_upload);
						String file_url = "";
						if ("contract".equals(model)) {
							file_url = copyFile(file_upload, "contract");
						} else {
							file_url = copyFile(file_upload, "card");
						}

						String[] split = item.getName().split("-");
						// 判断是否是修改
						/*
						 * if ("exceptionorderupdate".equals(model)) {
						 * List<Attachment> attachments =
						 * attachmentManager.findAttachmentByOrderSN(order_sn,
						 * 2); if (attachments != null && attachments.size() >
						 * 0) { attachment = attachments.get(0); } else {
						 * attachment = new Attachment(); } } else { attachment
						 * = new Attachment(); }
						 */
						attachment = new Attachment();
						attachment.setFile_name(item.getName());
						attachment.setFile_path(file_url.replaceAll("-", ""));
						attachment.setApprove_status(0);
						attachment.setStoreId(Long.parseLong(store_id));
						if ("contract".equals(model)) {
							attachment.setFile_type_name("合同");
							attachment.setFile_type(3);
						} else if ("card".equals(model)) {
							if ("audit_file".equals(item.getFieldName())) {
								attachment.setFile_type_name("audit_file");
								attachment.setFile_type(4);
							} else if ("plane_plan".equals(item.getFieldName())) {
								attachment.setFile_type_name("plane_plan");
								attachment.setFile_type(5);
							} else if ("record_drawing".equals(item.getFieldName())) {
								attachment.setFile_type_name("record_drawing");
								attachment.setFile_type(6);
							} else if ("business_license".equals(item.getFieldName())) {
								attachment.setFile_type_name("business_license");
								attachment.setFile_type(7);
							} else if ("food_card".equals(item.getFieldName())) {
								attachment.setFile_type_name("food_card");
								attachment.setFile_type(8);
							} else if ("tobacco_card".equals(item.getFieldName())) {
								attachment.setFile_type_name("tobacco_card");
								attachment.setFile_type(9);
							} else if ("book_card".equals(item.getFieldName())) {
								attachment.setFile_type_name("book_card");
								attachment.setFile_type(10);
							} else if ("xx_card".equals(item.getFieldName())) {
								attachment.setFile_type(11);
								attachment.setFile_type_name("xx_card");
							} else if ("other_card".equals(item.getFieldName())) {
								attachment.setFile_type(12);
								attachment.setFile_type_name("other_card");
							}
						}
						if (name != null && name.length() > 0) {
							DataTransfromUtil.preObject(attachment);
							attachmentManager.saveObject(attachment);
						}
					}

				}
			}
			if ("contract".equals(model)) {
				writer.write(
						"<script type='text/javascript'>window.location.href = 'data_access/store_list.html';</script>");
			}
			if ("card".equals(model)) {
				writer.write(
						"<script type='text/javascript'>window.location.href = 'data_access/storeDocumentList.html';</script>");
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \"导入失败\");</script>");
		} catch (Exception e) {
			e.printStackTrace();
			writer.write("<script type='text/javascript'>window.parent.$$.showMessage(\"系统信息\", \"" + e.getMessage()
					+ "\");</script>");
		}
		writer.flush();
		writer.close();

	}

	// 如果导入数据库成功将文件复制到Tomcat文件夹下
	public String copyFile(File file, String target_dir) throws Exception {
		String str_webroot = FILE_ROOT.concat("/" + target_dir);
		File file_target_dir = new File(str_webroot);
		boolean bResult = true;
		if (!file_target_dir.exists()) {
			if (!file_target_dir.isDirectory()) {
				bResult = file_target_dir.mkdir();
			}
		}
		File file_target_image = new File(str_webroot + File.separator + file.getName());
		if (file_target_image.exists()) {
			bResult = file_target_image.delete();
		}
		FileUtils.copyFile(file, file_target_image);
		System.gc();
		if (file.exists()) {
			bResult = file.delete();
		}
		return str_webroot + File.separator + file.getName();
	}

	// 删除文件
	public void deleteFile(File file) throws Exception {
		if (file.isFile() && file.exists()) {
			file.delete();
		} else {
			throw new Exception("文件删除失败！");
		}
	}

}
