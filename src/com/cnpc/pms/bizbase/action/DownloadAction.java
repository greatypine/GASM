package com.cnpc.pms.bizbase.action;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cnpc.pms.base.file.comm.utils.StringUtils;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.manager.AttachmentManager;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.BusinessInfoManager;
import com.cnpc.pms.personal.manager.CityHumanresourcesManager;
import com.cnpc.pms.personal.manager.ExpressManager;
import com.cnpc.pms.personal.manager.HouseCustomerManager;
import com.cnpc.pms.personal.manager.HumanVacationManager;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
import com.cnpc.pms.personal.manager.OfficeManager;
import com.cnpc.pms.personal.manager.StoreDocumentInfoManager;
import com.cnpc.pms.personal.manager.StoreKeeperManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.utils.CompressFile;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.DownloadUtil;
import com.cnpc.pms.utils.PropertiesValueUtil;

/**
 * 上传Excel的Aciton create : liuxiao version Version1.0
 */
public class DownloadAction extends HttpServlet {

	private String folder_path = null;
	/**
	 * 配置文件获取帮助类
	 */
	private PropertiesValueUtil propertiesValueUtil;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		String str_file_name = req.getParameter("fileName");
		String express_month = req.getParameter("month");
		String store_id = req.getParameter("store_id");
		String employee_no=req.getParameter("employee_no");
		String vacation_id=req.getParameter("vacation_id");
		
		AttachmentManager attachmentManager = (AttachmentManager) SpringHelper.getBean("attachmentManager");
		String strRootpath;
		if ("house_info_proofread".equals(str_file_name)) {
			String str_store_id = req.getParameter("store_id");
			// String str_store_name = new
			// String(req.getParameter("store_name").getBytes("ISO8859-1"),
			// "UTF-8");
			try {
				StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
				Store obj_store = (Store) storeManager.getObject(str_store_id);
				strRootpath = getFolder_path().concat(obj_store.getName()).concat(".zip");

				File file_zip = new File(strRootpath);
				if (!file_zip.exists()) {
					file_zip.createNewFile();
					CompressFile.ZipFiles(new File[0], file_zip);
				}
				DownloadUtil.downLoadFile(file_zip.getAbsolutePath(), resp, obj_store.getName() + ".zip", "zip");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("human_list".equals(str_file_name)) {
			try {
				HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper
						.getBean("humanresourcesManager");
				File file = humanresourcesManager.exportHumanExcel();
				DownloadUtil.downLoadFile(file.getAbsolutePath(), resp,
						"humanList" + DateUtils.dateFormat(new Date()) + ".xls", "xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if ("vacation_list".equals(str_file_name)) {
			try {
				HumanVacationManager humanVacationManager = (HumanVacationManager) SpringHelper.getBean("humanVacationManager");
				File file = humanVacationManager.exportVacationInfo(vacation_id);
				DownloadUtil.downLoadFile(file.getAbsolutePath(), resp,
						""+ employee_no + DateUtils.dateFormat(new Date(),"YYYYMMddHHmmSS") + ".doc", "doc");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("city_human_list".equals(str_file_name)) {
			try {
				// HumanresourcesManager humanresourcesManager =
				// (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
				CityHumanresourcesManager cityHumanresourcesManager = (CityHumanresourcesManager) SpringHelper
						.getBean("cityHumanresourcesManager");
				File file = cityHumanresourcesManager.exportCityHumanExcel();
				DownloadUtil.downLoadFile(file.getAbsolutePath(), resp,
						"cityhumanList" + DateUtils.dateFormat(new Date()) + ".xls", "xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("storekeeper_list".equals(str_file_name)) {
			try {
				StoreKeeperManager storeKeeperManager = (StoreKeeperManager) SpringHelper.getBean("storeKeeperManager");
				File file = storeKeeperManager.exportStoreKeeperExcel();
				DownloadUtil.downLoadFile(file.getAbsolutePath(), resp,
						"storeKeeperList" + DateUtils.dateFormat(new Date()) + ".xls", "xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("office_list".equals(str_file_name) || "office_whole_list".equals(str_file_name)) {
			try {
				String town_name = new String(req.getParameter("town_name").getBytes("iso8859-1"), "UTF-8");
				String store_name = new String(req.getParameter("store_name").getBytes("iso8859-1"), "UTF-8");
				String province_name = new String(req.getParameter("province_name").getBytes("iso8859-1"), "UTF-8");
				Map<String, Object> param = new HashMap<String, Object>();
				if (!StringUtils.isBlank(town_name)) {
					param.put("town_name", town_name);
				}
				if (!StringUtils.isBlank(store_name)) {
					param.put("store_name", store_name);
				}
				if (!StringUtils.isBlank(province_name)) {
					param.put("province_name", province_name);
				}
				OfficeManager officeManager = (OfficeManager) SpringHelper.getBean("officeManager");
				File file = null;
				if ("office_list".equals(str_file_name)) {
					file = officeManager.exportOfficeExce(param);
				} else if ("office_whole_list".equals(str_file_name)) {
					file = officeManager.exportWholeOfficeExce(param);
				}

				DownloadUtil.downLoadFile(file.getAbsolutePath(), resp,
						"officelist_" + DateUtils.dateFormat(new Date()) + ".xls", "xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("store_list".equals(str_file_name)) {
			try {
				String town_name = new String(req.getParameter("town_name").getBytes("iso8859-1"), "UTF-8");
				String name = new String(req.getParameter("name").getBytes("iso8859-1"), "UTF-8");
				String cityName = new String(req.getParameter("cityName").getBytes("iso8859-1"), "UTF-8");
				String storeno = new String(req.getParameter("storeno").getBytes("iso8859-1"), "UTF-8");
				String standUP = new String(req.getParameter("standUP").getBytes("iso8859-1"), "UTF-8");
				String endUP = new String(req.getParameter("endUP").getBytes("iso8859-1"), "UTF-8");
				String access = new String(req.getParameter("access").getBytes("iso8859-1"), "UTF-8");
				Map<String, Object> param = new HashMap<String, Object>();
				if (!StringUtils.isBlank(town_name)) {
					param.put("town_name", town_name);
				}
				if (!StringUtils.isBlank(name)) {
					param.put("name", name);
				}
				if (!StringUtils.isBlank(cityName)) {
					param.put("cityName", cityName);
				}
				if (!StringUtils.isBlank(storeno)) {
					param.put("storeno", storeno);
				}
				if (!StringUtils.isBlank(standUP)) {
					param.put("standUP", standUP);
				}
				if (!StringUtils.isBlank(endUP)) {
					param.put("endUP", endUP);
				}
				if (!StringUtils.isBlank(access)) {
					param.put("access", access);
				}
				StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
				File file = null;
				if ("store_list".equals(str_file_name)) {
					file = storeManager.exportExcelStore(param);
				}
				DownloadUtil.downLoadFile(file.getAbsolutePath(), resp,
						"门店信息" + DateUtils.dateFormat(new Date()) + ".xls", "xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("business_list".equals(str_file_name)) {
			try {
				String town_name = new String(req.getParameter("town_name").getBytes("iso8859-1"), "UTF-8");
				String business_id = new String(req.getParameter("business_id").getBytes("iso8859-1"), "UTF-8");
				String village_name = new String(req.getParameter("village_name").getBytes("iso8859-1"), "UTF-8");
				String province_name = new String(req.getParameter("province_name").getBytes("iso8859-1"), "UTF-8");
				String store_name = new String(req.getParameter("store_name").getBytes("iso8859-1"), "UTF-8");
				Map<String, String> param = new HashMap<String, String>();
				if (!StringUtils.isBlank(town_name)) {
					param.put("town_name", town_name);
				}
				if (!StringUtils.isBlank(village_name)) {
					param.put("village_name", village_name);
				}
				if (!StringUtils.isBlank(province_name)) {
					param.put("province_name", province_name);
				}
				if (!StringUtils.isBlank(store_name)) {
					param.put("store_name", store_name);
				}
				if (!StringUtils.isBlank(business_id)) {
					param.put("business_id", business_id);
				}
				// OfficeManager officeManager =
				// (OfficeManager)SpringHelper.getBean("officeManager");
				BusinessInfoManager businessManager = (BusinessInfoManager) SpringHelper.getBean("businessInfoManager");
				File file = businessManager.exportBusiNessList(param);

				DownloadUtil.downLoadFile(file.getAbsolutePath(), resp,
						"businesslist_" + DateUtils.dateFormat(new Date()) + ".xls", "xls");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if ("storeDetaillist".equals(str_file_name)) {
			try {
				StoreDocumentInfoManager storeDocumentInfoManager = (StoreDocumentInfoManager) SpringHelper
						.getBean("storeDocumentInfoManager");
				File file = storeDocumentInfoManager.exprotStoreDetail();
				DownloadUtil.downLoadFile(file.getAbsolutePath(), resp,
						"门店详情" + DateUtils.dateFormat(new Date()) + ".xls", "xls");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if ("store_list_info".equals(str_file_name)) {
			try {
				StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
				File file = storeManager.exportStoreExcelData();
				DownloadUtil.downLoadFile(file.getAbsolutePath(), resp,
						"门店开业信息台账" + DateUtils.dateFormat(new Date()) + ".xls", "xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("express_count".equals(str_file_name)) {
			try {
				ExpressManager expressManager = (ExpressManager) SpringHelper.getBean("expressManager");
				File file = expressManager.exportExpressExcel(express_month);
				DownloadUtil.downLoadFile(file.getAbsolutePath(), resp, "express" + express_month + ".xls", "xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("userhouse_count".equals(str_file_name)) {
			try {
				HouseCustomerManager houseCustomerManager = (HouseCustomerManager) SpringHelper
						.getBean("houseCustomerManager");
				File file = houseCustomerManager.queryUserHouseCount();
				DownloadUtil.downLoadFile(file.getAbsolutePath(), resp, "userhouse.xls", "xls");
				/*
				 * ExpressManager expressManager = (ExpressManager)
				 * SpringHelper.getBean("expressManager"); File file =
				 * expressManager.exportExpressExcel(express_month);
				 * DownloadUtil.downLoadFile(file.getAbsolutePath(),resp,
				 * "express"+express_month+".xls","xls");
				 */
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("audit_file".equals(str_file_name)) {
			try {
				if (store_id != null && !"".equals(store_id)) {
					long storeid = Long.parseLong(store_id);
					Attachment attachment = attachmentManager.findAttachmentByStoreIdType(storeid, 4);
					String[] split = attachment.getFile_path().split("card");
					String str_file_dir_path = PropertiesUtil.getValue("file.root");
					str_file_dir_path = str_file_dir_path + "card" + split[1];
					System.out.println(str_file_dir_path);
					String[] split2 = attachment.getFile_name().split("\\.");
					System.out.println(split2[1]);
					DownloadUtil.downLoadFile(str_file_dir_path, resp, attachment.getFile_name(), split2[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("plane_plan".equals(str_file_name)) {
			try {
				if (store_id != null && !"".equals(store_id)) {
					long storeid = Long.parseLong(store_id);
					Attachment attachment = attachmentManager.findAttachmentByStoreIdType(storeid, 5);
					String[] split = attachment.getFile_path().split("card");
					String str_file_dir_path = PropertiesUtil.getValue("file.root");
					str_file_dir_path = str_file_dir_path + "card" + split[1];
					System.out.println(str_file_dir_path);
					String[] split2 = attachment.getFile_name().split("\\.");
					System.out.println(split2[1]);
					DownloadUtil.downLoadFile(str_file_dir_path, resp, attachment.getFile_name(), split2[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("record_drawing".equals(str_file_name)) {
			try {
				if (store_id != null && !"".equals(store_id)) {
					long storeid = Long.parseLong(store_id);
					Attachment attachment = attachmentManager.findAttachmentByStoreIdType(storeid, 6);
					String[] split = attachment.getFile_path().split("card");
					String str_file_dir_path = PropertiesUtil.getValue("file.root");
					str_file_dir_path = str_file_dir_path + "card" + split[1];
					System.out.println(str_file_dir_path);
					String[] split2 = attachment.getFile_name().split("\\.");
					System.out.println(split2[1]);
					DownloadUtil.downLoadFile(str_file_dir_path, resp, attachment.getFile_name(), split2[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("business_license".equals(str_file_name)) {
			try {
				if (store_id != null && !"".equals(store_id)) {
					long storeid = Long.parseLong(store_id);
					Attachment attachment = attachmentManager.findAttachmentByStoreIdType(storeid, 7);
					String[] split = attachment.getFile_path().split("card");
					String str_file_dir_path = PropertiesUtil.getValue("file.root");
					str_file_dir_path = str_file_dir_path + "card" + split[1];
					System.out.println(str_file_dir_path);
					String[] split2 = attachment.getFile_name().split("\\.");
					System.out.println(split2[1]);
					DownloadUtil.downLoadFile(str_file_dir_path, resp, attachment.getFile_name(), split2[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("food_card".equals(str_file_name)) {
			try {
				if (store_id != null && !"".equals(store_id)) {
					long storeid = Long.parseLong(store_id);
					Attachment attachment = attachmentManager.findAttachmentByStoreIdType(storeid, 8);
					String[] split = attachment.getFile_path().split("card");
					String str_file_dir_path = PropertiesUtil.getValue("file.root");
					str_file_dir_path = str_file_dir_path + "card" + split[1];
					System.out.println(str_file_dir_path);
					String[] split2 = attachment.getFile_name().split("\\.");
					System.out.println(split2[1]);
					DownloadUtil.downLoadFile(str_file_dir_path, resp, attachment.getFile_name(), split2[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("tobacco_card".equals(str_file_name)) {
			try {
				if (store_id != null && !"".equals(store_id)) {
					long storeid = Long.parseLong(store_id);
					Attachment attachment = attachmentManager.findAttachmentByStoreIdType(storeid, 9);
					String[] split = attachment.getFile_path().split("card");
					String str_file_dir_path = PropertiesUtil.getValue("file.root");
					str_file_dir_path = str_file_dir_path + "card" + split[1];
					System.out.println(str_file_dir_path);
					String[] split2 = attachment.getFile_name().split("\\.");
					System.out.println(split2[1]);
					DownloadUtil.downLoadFile(str_file_dir_path, resp, attachment.getFile_name(), split2[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("book_card".equals(str_file_name)) {
			try {
				if (store_id != null && !"".equals(store_id)) {
					long storeid = Long.parseLong(store_id);
					Attachment attachment = attachmentManager.findAttachmentByStoreIdType(storeid, 10);
					String[] split = attachment.getFile_path().split("card");
					String str_file_dir_path = PropertiesUtil.getValue("file.root");
					str_file_dir_path = str_file_dir_path + "card" + split[1];
					System.out.println(str_file_dir_path);
					String[] split2 = attachment.getFile_name().split("\\.");
					System.out.println(split2[1]);
					DownloadUtil.downLoadFile(str_file_dir_path, resp, attachment.getFile_name(), split2[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("xx_card".equals(str_file_name)) {
			try {
				if (store_id != null && !"".equals(store_id)) {
					long storeid = Long.parseLong(store_id);
					Attachment attachment = attachmentManager.findAttachmentByStoreIdType(storeid, 11);
					String[] split = attachment.getFile_path().split("card");
					String str_file_dir_path = PropertiesUtil.getValue("file.root");
					str_file_dir_path = str_file_dir_path + "card" + split[1];
					System.out.println(str_file_dir_path);
					String[] split2 = attachment.getFile_name().split("\\.");
					System.out.println(split2[1]);
					DownloadUtil.downLoadFile(str_file_dir_path, resp, attachment.getFile_name(), split2[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("other_card".equals(str_file_name)) {
			try {
				if (store_id != null && !"".equals(store_id)) {
					long storeid = Long.parseLong(store_id);
					Attachment attachment = attachmentManager.findAttachmentByStoreIdType(storeid, 12);
					String[] split = attachment.getFile_path().split("card");
					String str_file_dir_path = PropertiesUtil.getValue("file.root");
					str_file_dir_path = str_file_dir_path + "card" + split[1];
					System.out.println(str_file_dir_path);
					String[] split2 = attachment.getFile_name().split("\\.");
					System.out.println(split2[1]);
					DownloadUtil.downLoadFile(str_file_dir_path, resp, attachment.getFile_name(), split2[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("contract_file".equals(str_file_name)) {
			try {
				if (store_id != null && !"".equals(store_id)) {
					long storeid = Long.parseLong(store_id);
					Attachment attachment = attachmentManager.findAttachmentByStoreIdType(storeid, 3);
					String[] split = attachment.getFile_path().split("contract");
					String str_file_dir_path = PropertiesUtil.getValue("file.root");
					str_file_dir_path = str_file_dir_path + "contract" + split[1];
					System.out.println(str_file_dir_path);
					String[] split2 = attachment.getFile_name().split("\\.");
					System.out.println(split2[1]);
					DownloadUtil.downLoadFile(str_file_dir_path, resp, attachment.getFile_name(), split2[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			strRootpath = req.getSession().getServletContext().getRealPath(File.separator);
			// 配置文件中的路径
			String str_filepath = getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator);
			strRootpath = strRootpath.concat(str_filepath);
			// 获取文件名
			String strFileName = strRootpath.substring(strRootpath.lastIndexOf(File.separator) + 1,
					strRootpath.length());
			try {
				DownloadUtil.downLoadFile(strRootpath, resp, strFileName, "xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取配置文件
	 * 
	 * @return 配置文件对象
	 */
	public PropertiesValueUtil getPropertiesValueUtil() {
		if (propertiesValueUtil == null) {
			propertiesValueUtil = new PropertiesValueUtil(
					File.separator + "conf" + File.separator + "download_config.properties");
		}
		return propertiesValueUtil;
	}

	public String getFolder_path() {
		if (folder_path == null) {
			String FILE_ROOT = PropertiesUtil.getValue("file.root");
			folder_path = FILE_ROOT.concat(File.separator).concat("template").concat(File.separator);
		}
		return folder_path;
	}

}
