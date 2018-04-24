package com.cnpc.pms.personal.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.FileCopyUtils;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.manager.AttachmentManager;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.StoreDocumentInfoDao;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreDocumentInfo;
import com.cnpc.pms.personal.manager.StoreDocumentInfoManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.WorkInfoManager;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;

public class StoreDocumentInfoManagerImpl extends BizBaseCommonManager implements StoreDocumentInfoManager {
	PropertiesValueUtil propertiesValueUtil = null;
	/**
	 * 到处户型excel单元格公共样式
	 */
	CellStyle cellStyle_common = null;

	@Override
	public StoreDocumentInfo saveOrUpdateStoreDocumentInfo(StoreDocumentInfo storeDocumentInfo) {
		long millis = System.currentTimeMillis();
		Boolean flag = true;
		WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
		Integer auditorbefo = 0;
		// 根据门店id查询门店补充信息
		StoreDocumentInfo documentInfo = findStoreDocumentInfoByStoreId(storeDocumentInfo.getStore_id());
		if (documentInfo == null) {
			flag = false;
			documentInfo = new StoreDocumentInfo();
			documentInfo.setWork_id(millis + "");
		}
		auditorbefo = documentInfo.getAudit_status();// 获取之前的状态
		if (auditorbefo == null || auditorbefo == 3 || auditorbefo == 0) {
			documentInfo.setWork_id(millis + "");
		}
		documentInfo.setAudit_date(storeDocumentInfo.getAudit_date());
		if (storeDocumentInfo.getAudit_status() != null && storeDocumentInfo.getAudit_status() == 0) {
			documentInfo.setAudit_status(0);
		} else {
			documentInfo.setAudit_status(1);
		}
		documentInfo.setSubmit_date(storeDocumentInfo.getSubmit_date());
		documentInfo.setEnter_date(storeDocumentInfo.getEnter_date());
		documentInfo.setEnter_end_date(storeDocumentInfo.getEnter_end_date());
		documentInfo.setCard_content(storeDocumentInfo.getCard_content());
		documentInfo.setStore_id(storeDocumentInfo.getStore_id());
		preObject(documentInfo);
		this.saveObject(documentInfo);
		if (storeDocumentInfo.getAudit_status() != null && storeDocumentInfo.getAudit_status() == 0) {

		} else {
			// 添加审核任务
			if (!flag) {// 当flag为false时为添加数据
				Date date = new Date();
				SimpleDateFormat myFmt1 = new SimpleDateFormat("yyyy-MM");
				workInfoManager.StartFlow(documentInfo.getStore_id(), "门店筹备审核", myFmt1.format(date),
						documentInfo.getWork_id(), "save", "");
			} else {
				if (auditorbefo == null || auditorbefo == 3 || auditorbefo == 0) {
					Date date = new Date();
					SimpleDateFormat myFmt1 = new SimpleDateFormat("yyyy-MM");
					workInfoManager.StartFlow(documentInfo.getStore_id(), "门店筹备审核", myFmt1.format(date),
							documentInfo.getWork_id(), "save", "");
				} else {
					Date date = new Date();
					SimpleDateFormat myFmt1 = new SimpleDateFormat("yyyy-MM");
					workInfoManager.StartFlow(documentInfo.getStore_id(), "门店筹备审核", myFmt1.format(date),
							documentInfo.getWork_id(), "update", "");
				}
			}
		}

		return documentInfo;
	}

	@Override
	public StoreDocumentInfo findStoreDocumentInfoByStoreId(Long store_id) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("store_id=" + store_id));
		if (list != null && list.size() > 0) {
			StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
			AttachmentManager attachmentManager = (AttachmentManager) SpringHelper.getBean("attachmentManager");
			StoreDocumentInfo storeDocumentInfo = (StoreDocumentInfo) list.get(0);
			Store store = storeManager.findStore(storeDocumentInfo.getStore_id());
			for (int i = 4; i <= 12; i++) {
				Attachment attachment = attachmentManager.findAttachmentByStoreIdType(storeDocumentInfo.getStore_id(),
						i);
				if (attachment != null) {
					if ("other_card".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setOther_pic(attachment.getFile_name());
					} else if ("xx_card".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setXx_pic(attachment.getFile_name());
					} else if ("book_card".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setBook_pic(attachment.getFile_name());
					} else if ("tobacco_card".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setTobacco_pic(attachment.getFile_name());
					} else if ("food_card".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setFood_pic(attachment.getFile_name());
					} else if ("business_license".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setBusiness_pic(attachment.getFile_name());
					} else if ("record_drawing".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setRecord_pic(attachment.getFile_name());
					} else if ("plane_plan".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setPlane_pic(attachment.getFile_name());
					} else if ("audit_file".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setAudit_pic(attachment.getFile_name());
					}
				}
			}
			storeDocumentInfo.setName(store.getName());
			return storeDocumentInfo;
		}
		return null;
	}

	@Override
	public StoreDocumentInfo findStoreDocumentInfoById(Long id) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("id=" + id));
		if (list != null && list.size() > 0) {
			StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
			AttachmentManager attachmentManager = (AttachmentManager) SpringHelper.getBean("attachmentManager");
			StoreDocumentInfo storeDocumentInfo = (StoreDocumentInfo) list.get(0);
			Store store = storeManager.findStore(storeDocumentInfo.getStore_id());
			for (int i = 4; i <= 12; i++) {
				Attachment attachment = attachmentManager.findAttachmentByStoreIdType(storeDocumentInfo.getStore_id(),
						i);
				if (attachment != null) {
					if ("other_card".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setOther_pic(attachment.getFile_name());
					} else if ("xx_card".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setXx_pic(attachment.getFile_name());
					} else if ("book_card".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setBook_pic(attachment.getFile_name());
					} else if ("tobacco_card".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setTobacco_pic(attachment.getFile_name());
					} else if ("food_card".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setFood_pic(attachment.getFile_name());
					} else if ("business_license".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setBusiness_pic(attachment.getFile_name());
					} else if ("record_drawing".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setRecord_pic(attachment.getFile_name());
					} else if ("plane_plan".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setPlane_pic(attachment.getFile_name());
					} else if ("audit_file".equals(attachment.getFile_type_name())) {
						storeDocumentInfo.setAudit_pic(attachment.getFile_name());
					}
				}
			}
			storeDocumentInfo.setName(store.getName());
			return storeDocumentInfo;
		}
		return null;
	}

	@Override
	public Map<String, Object> showStoreDocumentInfoData(QueryConditions conditions) {
		StoreDocumentInfoDao storeDocumentInfoDao = (StoreDocumentInfoDao) SpringHelper
				.getBean(StoreDocumentInfoDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND sto.`name` like '").append(map_where.get("value")).append("'");
			} else if ("cityName".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND sto.city_name like '").append(map_where.get("value")).append("'");
			} else if ("storeno".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND sto.storeno like '").append(map_where.get("value")).append("'");
			} else if ("estate".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				System.out.println(map_where.get("value"));
				sb_where.append(" AND sto.estate like '").append(map_where.get("value")).append("'");
			}
		}
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		if ("QYJL".equals(sessionUser.getUsergroup().getCode())) {
			sb_where.append(" and sto.rmid=" + sessionUser.getId() + " ");
		}
		// 取得当前登录人 所管理城市
		String cityssql = "";
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		List<DistCity> distCityList = userManager.getCurrentUserCity();
		if (distCityList != null && distCityList.size() > 0) {
			for (DistCity d : distCityList) {
				cityssql += "'" + d.getCityname() + "',";
			}
			cityssql = cityssql.substring(0, cityssql.length() - 1);
		}
		if (cityssql != "" && cityssql.length() > 0) {
			sb_where.append(" and sto.city_name in (" + cityssql + ")");
		} else {
			sb_where.append(" and 0=1 ");
		}
		System.out.println(sb_where);
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "门店详情");
		map_result.put("data", storeDocumentInfoDao.getStoreDocumentInfoData(sb_where.toString(), obj_page));
		return map_result;
	}

	@Override
	public StoreDocumentInfo findStoreDocumentInfoByWorkId(String work_id) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("work_id='" + work_id + "'"));
		if (list != null && list.size() > 0) {
			return (StoreDocumentInfo) list.get(0);
		}
		return null;
	}

	@Override
	public File exprotStoreDetail() throws Exception {
		String str_file_name = "export_storedetail_list";
		String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
		// 配置文件中的路径
		String str_filepath = strRootpath
				.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));
		File file_template = new File(str_filepath);

		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_newfilepath = str_file_dir_path + "storedetail_list_info.xls";
		File file_new = new File(str_newfilepath);
		if (file_new.exists()) {
			file_new.delete();
		}
		FileCopyUtils.copy(file_template, file_new);
		FileInputStream fis_input_excel = new FileInputStream(file_new);
		FileOutputStream fis_out_excel = null;
		Workbook wb_humaninfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
		try {
			setCellStyle_common(wb_humaninfo);
			Sheet sh_job = wb_humaninfo.getSheetAt(0);
			int nJobIndex = 3;
			StoreDocumentInfoDao storeDocumentInfoDao = (StoreDocumentInfoDao) SpringHelper
					.getBean(StoreDocumentInfoDao.class.getName());
			StringBuffer sb_where = new StringBuffer();
			// 取得当前登录人 所管理城市
			String cityssql = "";
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			List<DistCity> distCityList = userManager.getCurrentUserCity();
			if (distCityList != null && distCityList.size() > 0) {
				for (DistCity d : distCityList) {
					cityssql += "'" + d.getCityname() + "',";
				}
				cityssql = cityssql.substring(0, cityssql.length() - 1);
			}

			if (cityssql != "" && cityssql.length() > 0) {
				sb_where.append(" and stt.city_name in (" + cityssql + ")");
			} else {
				sb_where.append(" and 0=1 ");
			}
			System.out.println(sb_where);
			List<Map<String, Object>> bussinessInfo = storeDocumentInfoDao.exportStoreDetail(sb_where.toString());
			for (Map<String, Object> map : bussinessInfo) {
				Row obj_row = null;
				int cellIndex = 0;
				sh_job.createRow(nJobIndex);
				obj_row = sh_job.getRow(nJobIndex);
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue((nJobIndex - 2)));// 序号
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("countyname")));// 区域
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("town_name")));// 街道
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("address")));// 位置
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("rent_area")));// 计租面积
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("agency_fee")));// 中介费
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("increase")));// 递增
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("increase_fee")));// 增容费
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("rent_free")));// 免租期
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("payment_method")));// 付款方式
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("rental")));// 租金
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("taxes")));// 税金
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("tenancy_term")));// 租期
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("nature")));// 属性
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("superMicro")));// 是否微超
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("submit_date")));// 功能方案提交日期
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("audit_date")));// 功能方案通过日期
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("enter_date")));// 装修进场日期
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("enter_end_date")));// 装修竣工日期
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("card_content")));// 证照信息
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("estate")));// 目前状态
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("open_shop_time")));// 门店开业时间
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("name")));// 店名
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("ename")));// 店长
				setCellValue(obj_row, cellIndex++, ValueUtil.getStringValue(map.get("phone")));// 门店电话
				nJobIndex++;
			}
			fis_out_excel = new FileOutputStream(file_new);
			wb_humaninfo.write(fis_out_excel);

		} catch (Exception e) {
			throw e;
		} finally {
			if (fis_out_excel != null) {
				fis_out_excel.close();
			}
			if (fis_input_excel != null) {
				fis_input_excel.close();
			}
		}
		return file_new;
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

	public void setCellStyle_common(Workbook wb) {
		cellStyle_common = wb.createCellStyle();
		cellStyle_common.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		cellStyle_common.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle_common.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		cellStyle_common.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		cellStyle_common.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
		cellStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 垂直居中
		cellStyle_common.setWrapText(true);// 设置自动换行
		Font font = wb.createFont();
		font.setFontName("微软雅黑");
		font.setFontHeightInPoints((short) 10);
		cellStyle_common.setFont(font);
	}

	public CellStyle getCellStyle_common() {
		return cellStyle_common;
	}

	public void setCellValue(Row obj_row, int nCellIndex, Object value) {
		Cell cell = obj_row.createCell(nCellIndex);
		cell.setCellStyle(getCellStyle_common());
		cell.setCellValue(new HSSFRichTextString(value == null ? null : value.toString()));
	}
}
