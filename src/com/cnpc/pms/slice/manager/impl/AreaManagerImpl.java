package com.cnpc.pms.slice.manager.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.Document;
import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.MyException;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.dynamic.common.HttpClientUtil;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.messageModel.dao.MessageNewDao;
import com.cnpc.pms.messageModel.entity.Message;
import com.cnpc.pms.messageModel.manager.MessageNewManager;
import com.cnpc.pms.mongodb.common.MongoDbUtil;
import com.cnpc.pms.mongodb.dao.MongoDBDao;
import com.cnpc.pms.mongodb.manager.MongoDBManager;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.dao.TinyAreaDao;
import com.cnpc.pms.personal.dao.TinyVillageDao;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.slice.dao.AreaDao;
import com.cnpc.pms.slice.dto.AreaDto;
import com.cnpc.pms.slice.entity.Area;
import com.cnpc.pms.slice.entity.AreaHistory;
import com.cnpc.pms.slice.entity.AreaInfo;
import com.cnpc.pms.slice.entity.AreaInfoHistory;
import com.cnpc.pms.slice.manager.AreaHistoryManager;
import com.cnpc.pms.slice.manager.AreaInfoHistoryManager;
import com.cnpc.pms.slice.manager.AreaInfoManager;
import com.cnpc.pms.slice.manager.AreaManager;
import com.gexin.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * Created by liuxi on 2017/2/27.
 */
public class AreaManagerImpl extends BizBaseCommonManager implements AreaManager {

	@Override
	public Area queryArea(Long id) {

		AreaInfoManager areaInfoManager = (AreaInfoManager) SpringHelper.getBean("areaInfoManager");

		Area area = (Area) this.getObject(id);

		List<AreaInfo> lst_areaInfos = (List<AreaInfo>) areaInfoManager
				.getList(FilterFactory.getSimpleFilter("area_id", area.getId()));
		if (lst_areaInfos != null && lst_areaInfos.size() > 0) {
			area.setChildrens(lst_areaInfos);
		}
		return area;
	}

	@Override
	public Area saveArea(Area area) {
		AreaInfoManager areaInfoManager = (AreaInfoManager) SpringHelper.getBean("areaInfoManager");
		AreaHistoryManager areaHistoryManager = (AreaHistoryManager) SpringHelper.getBean("areaHistoryManager");
		AreaInfoHistoryManager areaInfoHistoryManager = (AreaInfoHistoryManager) SpringHelper
				.getBean("areaInfoHistoryManager");
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		MongoDBManager mongoDBManager = (MongoDBManager) SpringHelper.getBean("mongoDBManager");
		Store store = (Store) storeManager.getObject(area.getStore_id());
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		Area save_area = null;
		AreaHistory areaHistory = null;
		List<AreaInfo> areaInfoList = null;
		try {

			if (null != area.getId()) {
				save_area = (Area) this.getObject(area.getId());

				// 保存片区原始数据记录
				areaHistory = new AreaHistory();
				BeanUtils.copyProperties(save_area, areaHistory, new String[] { "id" });
				areaHistory.setArea_id(area.getId());
				areaHistory.setFlag(1);
				areaHistoryManager.saveObject(areaHistory);

				areaInfoList = (List<AreaInfo>) areaDao.queryAreaInfoByAreaId(area.getId());
				for (AreaInfo info : areaInfoList) {
					AreaInfoHistory areaInfoHistory = new AreaInfoHistory();
					BeanUtils.copyProperties(info, areaInfoHistory, new String[] { "id" });
					areaInfoHistory.setAreainfo_id(info.getId());
					areaInfoHistory.setFlag(1);
					areaInfoHistoryManager.saveObject(areaInfoHistory);
				}

				areaInfoManager.deleteAreaInfoByAreaId(area);// 删除原始片区详情

			} else {
				List<Area> area_list = (List<Area>) this
						.getList(FilterFactory.getSimpleFilter("store_id", area.getStore_id()));
				String temp_no = "";
				if (area_list == null || area_list.size() == 0) {
					temp_no = "00001";
				} else if (area_list.size() > 0 && area_list.size() < 9) {
					temp_no = "0000" + (area_list.size() + 1);
				} else if (area_list.size() >= 9 && area_list.size() < 99) {
					temp_no = "000" + (area_list.size() + 1);
				} else if (area_list.size() >= 99 && area_list.size() < 999) {
					temp_no = "00" + (area_list.size() + 1);
				} else if (area_list.size() >= 999 && area_list.size() < 9999) {
					temp_no = "0" + (area_list.size() + 1);
				} else if (area_list.size() >= 9999) {
					temp_no = String.valueOf(area_list.size() + 1);
				}
				save_area = new Area();
				String area_no = store.getStoreno() + temp_no;
				save_area.setArea_no(area_no);
			}
			save_area.setName(area.getName());
			// save_area.setTown_id(area.getTown_id());
			save_area.setStore_id(area.getStore_id());
			save_area.setEmployee_a_name(area.getEmployee_a_name());
			save_area.setEmployee_a_no(area.getEmployee_a_no());
			save_area.setEmployee_b_no(area.getEmployee_b_no());
			save_area.setEmployee_b_name(area.getEmployee_b_name());

			preObject(save_area);
			this.saveObject(save_area);

			for (AreaInfo info : area.getChildrens()) {
				info.setStore_id(area.getStore_id());
				info.setArea_id(save_area.getId());
				info.setArea_no(save_area.getArea_no());
				preObject(info);
				areaInfoManager.saveObject(info);
			}
			save_area.setChildrens(area.getChildrens());
			Map<String, Object> result = mongoDBManager.updateTinyAreaOfEmployee(save_area);
			if (Integer.parseInt(String.valueOf(result.get("code"))) != CodeEnum.success.getValue()) {
				throw new MyException("更新tiny_area或者mongodb国安侠失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return save_area;
	}

	@Override
	public Area deleteArea(Long id) {

		Area save_area = (Area) this.getObject(id);

		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		AreaInfoManager areaInfoManager = (AreaInfoManager) SpringHelper.getBean("areaInfoManager");
		MongoDBManager mongoDBManager = (MongoDBManager) SpringHelper.getBean("mongoDBManager");

		try {
			Area updateArea = new Area();
			updateArea.setId(id);
			updateArea.setEmployee_a_no(null);
			updateArea.setEmployee_b_no(null);
			updateArea.setStore_id(save_area.getStore_id());
			Map<String, Object> result = mongoDBManager.updateEmployeeOfTinyArea(updateArea);
			if (Integer.parseInt(String.valueOf(result.get("code"))) != CodeEnum.success.getValue()) {
				throw new MyException("更新tiny_area或者mongodb国安侠失败");
			}

			save_area.setStatus(1);
			preObject(save_area);
			this.saveObject(save_area);

			List<AreaInfo> areaInfos = areaDao.queryAreaInfoByAreaId(id);
			if (areaInfos != null) {
				AreaInfo aInfo = null;
				for (int i = 0; i < areaInfos.size(); i++) {
					aInfo = areaInfos.get(i);
					aInfo.setStatus(1);
					preObject(aInfo);
					areaInfoManager.saveObject(aInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return save_area;
	}

	@Override
	public Area updateAreaEmployeeNo(Area area) {
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		Area save_area = (Area) this.getObject(area.getId());

		AreaHistoryManager areaHistoryManager = (AreaHistoryManager) SpringHelper.getBean("areaHistoryManager");
		AreaInfoHistoryManager areaInfoHistoryManager = (AreaInfoHistoryManager) SpringHelper
				.getBean("areaInfoHistoryManager");
		AreaInfoManager areaInfoManager = (AreaInfoManager) SpringHelper.getBean("areaInfoManager");
		AreaManager areaManager = (AreaManager) SpringHelper.getBean("areaManager");
		MongoDBManager mongoDBManager = (MongoDBManager) SpringHelper.getBean("mongoDBManager");
		try {

			AreaHistory areaHistory = new AreaHistory();
			BeanUtils.copyProperties(save_area, areaHistory, new String[] { "id" });
			areaHistory.setArea_id(area.getId());
			areaHistory.setFlag(0);
			areaHistoryManager.saveObject(areaHistory);

			save_area.setEmployee_a_name(area.getEmployee_a_name());
			save_area.setEmployee_a_no(area.getEmployee_a_no());
			save_area.setEmployee_b_no(area.getEmployee_b_no());
			save_area.setEmployee_b_name(area.getEmployee_b_name());

			preObject(save_area);
			areaManager.saveObject(save_area);

			List<AreaInfo> list = (List<AreaInfo>) areaDao.queryAreaInfoByAreaId(area.getId());
			if (list != null && list.size() > 0) {
				for (AreaInfo ai : list) {
					AreaInfoHistory areaInfoHistory = new AreaInfoHistory();
					BeanUtils.copyProperties(ai, areaInfoHistory, new String[] { "id" });
					areaInfoHistory.setAreainfo_id(ai.getId());
					areaInfoHistory.setFlag(0);
					areaInfoHistoryManager.saveObject(areaInfoHistory);

					ai.setEmployee_a_no(area.getEmployee_a_no());
					ai.setEmployee_b_no(area.getEmployee_b_no());
					preObject(ai);
					areaInfoManager.saveObject(ai);
				}
			}

			Map<String, Object> result = mongoDBManager.updateEmployeeOfTinyArea(save_area);
			if (Integer.parseInt(String.valueOf(result.get("code"))) != 200) {
				throw new MyException("更新tiny_area或者mongodb国安侠失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return save_area;
	}

	@Override
	public List<AreaInfo> checkAreaIsRepeat(Area area, String actionType) {

		String name = area.getName();// 片区名称
		List<AreaInfo> list = area.getChildrens();
		// 已经存在的片区信息
		List<AreaInfo> repeatList = new ArrayList<AreaInfo>();

		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());

		for (AreaInfo ai : list) {

			// 检查是片区信息是否存在
			List<Map<String, Object>> result = areaDao.selectAreaInfo(ai, actionType);

			if (result != null && result.size() > 0) {
				StringBuilder tinyvillageSb = new StringBuilder();
				StringBuilder villageSb = new StringBuilder();
				String tinyvillage = "";
				String village = "";
				for (int i = 0; i < result.size(); i++) {
					Map<String, Object> obj = result.get(i);
					if (obj.get("flag") != null && Long.parseLong(obj.get("flag").toString()) == 1) {// 全部小区
						villageSb.append("、").append(obj.get("name").toString());
					} else if (obj.get("flag") != null && Long.parseLong(obj.get("flag").toString()) == 2) {// 个别小区
						tinyvillageSb.append("、").append(obj.get("name").toString());
					}
				}

				if (tinyvillageSb.toString().contains("、")) {
					tinyvillage = tinyvillageSb.toString().substring(1);
				}

				if (villageSb.toString().contains("、")) {
					village = villageSb.toString().substring(1);
				}

				ai.setTiny_village_name(tinyvillage);
				ai.setVillage_name(village);
				repeatList.add(ai);
			}
		}
		return repeatList;
	}

	@Override
	public boolean checkNameIsExist(Area area, String actionType) {

		String name = area.getName();// 片区名称
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		Integer result = null;
		try {
			result = areaDao.selectArea(area, actionType);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		if (result > 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public Map<String, Object> selectAreaDto(QueryConditions queryConditions) {
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		UserDTO userDTO = userManager.getCurrentUserDTO();
		List<AreaDto> list = new ArrayList<AreaDto>();
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 分页对象
		PageInfo obj_page = queryConditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();
		sb_where.append(" having 1=1 ");
		for (Map<String, Object> map_where : queryConditions.getConditions()) {
			if ("store_id".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and store_id = ").append(map_where.get("value"));
			} else if ("name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and name like '").append(map_where.get("value")).append("'");
			} else if ("townName".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and townName like '").append(map_where.get("value")).append("'");
			} else if ("villageName".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and villageName like '").append(map_where.get("value")).append("'");
			} else if ("tinVillageName".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and tinVillageName like '").append(map_where.get("value")).append("'");
			} else if ("employeeName".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and employeeName like '").append(map_where.get("value")).append("'");
			} else if ("area_no".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" and area_no like '").append(map_where.get("value")).append("'");
			} else if ("city_id".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {

				List<Map<String, Object>> storelist = storeDao.getAllStoreOfCSZJ(userDTO.getId(),
						Long.parseLong(map_where.get("value").toString()));
				StringBuilder storeSb = new StringBuilder();
				if (storelist != null && storelist.size() > 0) {
					for (int i = 0; i < storelist.size(); i++) {

						storeSb.append("," + storelist.get(i).get("store_id"));
					}
					storeSb.deleteCharAt(0);
				} else {
					storeSb.append("-10000000");
				}
				sb_where.append(" and store_id in (").append(storeSb.toString()).append(")");
			}

		}
		try {
			map_result.put("data", areaDao.selectAreaDto(sb_where.toString(), obj_page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "楼房信息");
		return map_result;
	}

	public void setCellValue(Row obj_row, int nCellIndex, Object value) {
		Cell cell = obj_row.createCell(nCellIndex);
		cell.setCellStyle(getCellStyle_common());
		cell.setCellValue(new HSSFRichTextString(value == null ? null : value.toString()));
	}

	private HSSFCellStyle style_header = null;
	private CellStyle cellStyle_common = null;

	private HSSFCellStyle getHeaderStyle() {
		return style_header;
	}

	private void setHeaderStyle(HSSFWorkbook wb) {

		// 创建单元格样式
		style_header = wb.createCellStyle();
		style_header.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style_header.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style_header.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		style_header.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// 设置边框
		style_header.setBottomBorderColor(HSSFColor.BLACK.index);
		style_header.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style_header.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style_header.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style_header.setBorderTop(HSSFCellStyle.BORDER_THIN);

	}

	private void setCellStyle_common(Workbook wb) {
		cellStyle_common = wb.createCellStyle();
		cellStyle_common.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
		cellStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 垂直居中
	}

	private CellStyle getCellStyle_common() {
		return cellStyle_common;
	}

	@Override
	public String exportAreaInfo(AreaDto areaDto) throws Exception {
		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_web_path = PropertiesUtil.getValue("file.web.root");
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		UserDTO userDTO = userManager.getCurrentUserDTO();
		StringBuilder storeSb = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		if (areaDto.getTarget() == 1) {// 城市
			if (areaDto.getStore_id() == null || "".equals(areaDto.getStore_id())) {
				List<Map<String, Object>> storelist = storeDao.getAllStoreOfCSZJ(userDTO.getId(), areaDto.getCity_id());

				if (storelist != null && storelist.size() > 0) {
					for (int i = 0; i < storelist.size(); i++) {

						storeSb.append("," + storelist.get(i).get("store_id"));
					}
					storeSb.deleteCharAt(0);
				} else {
					storeSb.append("-10000000");
				}
			} else {
				storeSb.append(areaDto.getStore_id());
			}

			param.put("storeId", storeSb.toString());
		} else if (areaDto.getTarget() == 2) {// 店长
			param.put("storeId", areaDto.getStore_id());
		} else if (areaDto.getTarget() == 4) {// 运营经理
			if (areaDto.getStore_id() == null || "".equals(areaDto.getStore_id())) {
				List<Map<String, Object>> storelist = storeDao.getAllStoreOfCRM(userDTO.getId(), areaDto.getCity_id(),
						"QYJL");
				if (storelist != null && storelist.size() > 0) {
					for (int i = 0; i < storelist.size(); i++) {

						storeSb.append("," + storelist.get(i).get("store_id"));
					}
					storeSb.deleteCharAt(0);
				} else {
					storeSb.append("-10000000");
				}
			} else {
				storeSb.append(areaDto.getStore_id());
			}

			param.put("storeId", storeSb.toString());
		}

		param.put("name", areaDto.getName());
		param.put("townName", areaDto.getTownName());
		param.put("villageName", areaDto.getVillageName());
		param.put("tinVillageName", areaDto.getTinVillageName());
		param.put("employeeName", areaDto.getEmployeeName());
		param.put("area_no", areaDto.getArea_no());
		List<Map<String, Object>> list = null;
		try {
			list = areaDao.selectAreaDto(param);
		} catch (Exception e) {
			e.printStackTrace();
		}

		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建Excel的工作sheet,对应到一个excel文档的tab

		setCellStyle_common(wb);
		setHeaderStyle(wb);
		HSSFSheet sheet = wb.createSheet("门店划片信息");
		HSSFRow row = sheet.createRow(0);
		String[] str_headers = { "城市", "门店编号", "门店名称", "片区名称", "片区编号", "街道", "社区", "小区", "A国安侠", "B国安侠" };
		for (int i = 0; i < str_headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(getHeaderStyle());
			cell.setCellValue(new HSSFRichTextString(str_headers[i]));
		}

		int index = 1;

		for (Map<String, Object> map_row : list) {
			HSSFRow row_data = sheet.createRow(index);
			for (int cellIndex = 0; cellIndex < str_headers.length; cellIndex++) {
				setCellValue(row_data, cellIndex, map_row.get(str_headers[cellIndex]));
			}
			index++;
		}

		File file_xls = new File(str_file_dir_path + File.separator + "huapian.xls");
		if (file_xls.exists()) {
			file_xls.delete();
		}
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file_xls.getAbsoluteFile());
			wb.write(os);
		} finally {
			if (os != null) {
				os.close();
			}
		}
		return str_web_path.concat(file_xls.getName());

	}

	@Override
	public Map<String, Object> queryAreaByEmployeeNo(String employee_no, Long area_id) {
		AreaInfoManager areaInfoManager = (AreaInfoManager) SpringHelper.getBean("areaInfoManager");

		StringBuilder filterStr = new StringBuilder();
		filterStr.append("status=0");
		if (employee_no != null && !"".equals(employee_no)) {
			filterStr.append(" AND employee_a_no='" + employee_no + "' ");
		}
		IFilter areaFilter = FilterFactory.getSimpleFilter(filterStr.toString());
		List<Area> all_area_list = (List<Area>) this.getList(areaFilter);
		if (area_id != null) {
			filterStr.append(" AND id=" + area_id);
		}
		areaFilter = FilterFactory.getSimpleFilter(filterStr.toString());
		List<Area> area_list = (List<Area>) this.getList(areaFilter);
		Area area = null;
		List<Map<String, Object>> areaInfos = null;
		if (area_list != null && area_list.size() > 0) {

			StringBuffer sbf = new StringBuffer();
			area = (Area) area_list.get(0);
			sbf.append(area.getId());
			areaInfos = areaInfoManager.queryAreaInfoByAreaId(sbf.toString());
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("area", area);
		obj.put("areaInfo", areaInfos);
		obj.put("areaList", all_area_list);
		return obj;

	}

	/**
	 * PC 验证是否是A国安侠
	 */
	@Override
	public Area queryAreaByCurrEmployeeNo() {
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		UserDTO userDTO = userManager.getCurrentUserDTO();
		if (userDTO != null && userDTO.getEmployeeId() != null) {
			IFilter iFilter = FilterFactory.getSimpleFilter("employee_a_no", userDTO.getEmployeeId())
					.appendAnd(FilterFactory.getSimpleFilter("status", 0));
			List<Area> areaInfoList = (List<Area>) this.getList(iFilter);
			if (areaInfoList != null && areaInfoList.size() > 0) {
				return areaInfoList.get(0);
			} else {
				Area area = new Area();
				area.setEmployee_a_no(userDTO.getEmployeeId());
				return area;
			}
		}
		return null;
	}

	/**
	 * 根据名区名 查询A国安侠
	 * 
	 * @param area_name
	 * @return
	 */
	@Override
	public Area queryAreaByAreaName(String area_name, Long store_id) {
		IFilter iFilter = FilterFactory.getSimpleFilter("name", area_name)
				.appendAnd(FilterFactory.getSimpleFilter("store_id", store_id));
		List<Area> areaInfoList = (List<Area>) this.getList(iFilter);
		if (areaInfoList != null && areaInfoList.size() > 0) {
			return areaInfoList.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.slice.manager.AreaManager#clearAreaInfo()
	 */
	@Override
	public void clearAreaInfo() {
		// TODO Auto-generated method stub
		// AreaInfoManager areaInfoManager =
		// (AreaInfoManager)SpringHelper.getBean("areaInfoManager");
		// AreaManager areaManager =
		// (AreaManager)SpringHelper.getBean("areaManager");
		// StoreManager storeManager =
		// (StoreManager)SpringHelper.getBean("storeManager");
		// List<Store> list = (List<Store>)storeManager.getList();
		// StringBuilder sqlSb = new StringBuilder();
		// for(int i=0;i<list.size();i++){
		// Store store = list.get(i);
		// List<Area> area_list =
		// (List<Area>)areaManager.getList(FilterFactory.getSimpleFilter("store_id",store.getStore_id()));
		// System.out.println("--------------------------------------------------");
		//
		// for(int j=0;j<area_list.size();j++){
		// String temp_no = "";
		// if(j==0){
		// temp_no = "00001";
		// }else if(j>0&&j<9){
		// temp_no = "0000"+(j+1);
		// }else if(j>=9&&j<99){
		// temp_no = "000"+(j+1);
		// }else if(j>=99&&j<999){
		// temp_no = "00"+(j+1);
		// }else if(j>=999&&j<9999){
		// temp_no = "0"+(j+1);
		// }else if(j>=9999){
		// temp_no = String.valueOf(j+1);
		// }
		//
		// String sql = "update t_area set area_no =
		// '"+store.getStoreno()+temp_no+"' where
		// id="+area_list.get(j).getId()+";";
		// sqlSb.append(sql+"\n");
		// System.out.println(sql+"\n");
		// }
		// }
		// System.out.println(sqlSb.toString());
	}

	@Override
	public String getAreaInfoOfStore(Long store_id) {
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		List<Map<String, Object>> list = areaDao.getAreaInfoOfStore(store_id);
		StringBuilder employeeSb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			employeeSb.append("," + list.get(i).get("name"));
		}
		if (list != null && list.size() > 0) {
			employeeSb.deleteCharAt(0);
		} else {
			employeeSb.append("all");
		}
		return employeeSb.toString();
	}

	@Override
	public Area checkAreaByEmployeeNo(String employeeNo, String actionType, Long area_id) {
		StringBuilder filterStr = new StringBuilder();
		filterStr.append("status=0");
		filterStr.append(" AND employee_a_no='" + employeeNo + "' ");
		if ("edit".equals(actionType)) {
			filterStr.append(" AND id!=" + area_id);
		}
		IFilter areaFilter = FilterFactory.getSimpleFilter(filterStr.toString());
		List<Area> areaInfoList = (List<Area>) this.getList(areaFilter);

		if (areaInfoList != null && areaInfoList.size() > 0) {
			return areaInfoList.get(0);
		}
		return null;

	}

	@Override
	public String clearAreaOfEmployee(String employeeNo1, String employeeNo2) {
		// TODO Auto-generated method stub
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		AreaManager areaManager = (AreaManager) SpringHelper.getBean("areaManager");
		AreaHistoryManager areaHistoryManager = (AreaHistoryManager) SpringHelper.getBean("areaHistoryManager");
		AreaInfoHistoryManager areaInfoHistoryManager = (AreaInfoHistoryManager) SpringHelper
				.getBean("areaInfoHistoryManager");
		AreaInfoManager areaInfoManager = (AreaInfoManager) SpringHelper.getBean("areaInfoManager");
		MongoDBManager mongoDBManager = (MongoDBManager) SpringHelper.getBean("mongoDBManager");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		List<User> userlist = (List<User>) userManager
				.getObjects(FilterFactory.getSimpleFilter("employeeId", employeeNo1));
		if (userlist != null && userlist.size() > 0) {
			try {
				Long store_id = userlist.get(0).getStore_id();
				List<Area> listA = areaDao.queryAreaByEmployeeAndStore(employeeNo1, store_id, "A");// 查询国安侠A的片区
				List<Area> listB = areaDao.queryAreaByEmployeeAndStore(employeeNo1, store_id, "B");// 查询国安侠B的片区
				List<AreaInfo> areaInfoList = null;

				if (listA != null && listA.size() > 0) {
					for (Area area : listA) {

						AreaHistory areaHistory = new AreaHistory();
						BeanUtils.copyProperties(area, areaHistory, new String[] { "id" });
						areaHistory.setArea_id(area.getId());
						areaHistory.setFlag(2);
						areaHistoryManager.saveObject(areaHistory);

						areaInfoList = (List<AreaInfo>) areaDao.queryAreaInfoByAreaId(area.getId());
						if (areaInfoList != null && areaInfoList.size() > 0) {
							for (AreaInfo ai : areaInfoList) {
								AreaInfoHistory areaInfoHistory = new AreaInfoHistory();
								BeanUtils.copyProperties(ai, areaInfoHistory, new String[] { "id" });
								areaInfoHistory.setAreainfo_id(ai.getId());
								areaInfoHistory.setFlag(2);
								areaInfoHistoryManager.saveObject(areaInfoHistory);

								ai.setEmployee_a_no(null);
								preObject(ai);
								areaInfoManager.saveObject(ai);
							}
						}

						Area tempArea = new Area();
						BeanUtils.copyProperties(area, tempArea);
						area.setEmployee_a_name(null);
						area.setEmployee_a_no(null);
						preObject(area);
						areaManager.saveObject(area);

						// 给店长发送信息提示

						sendMessage_area(tempArea, employeeNo2, "A");
					}

				}

				if (listB != null && listB.size() > 0) {
					for (Area area : listB) {
						AreaHistory areaHistory = new AreaHistory();
						BeanUtils.copyProperties(area, areaHistory, new String[] { "id" });
						areaHistory.setArea_id(area.getId());
						areaHistory.setFlag(2);
						areaHistoryManager.saveObject(areaHistory);

						areaInfoList = (List<AreaInfo>) areaDao.queryAreaInfoByAreaId(area.getId());
						if (areaInfoList != null && areaInfoList.size() > 0) {
							for (AreaInfo ai : areaInfoList) {
								AreaInfoHistory areaInfoHistory = new AreaInfoHistory();
								BeanUtils.copyProperties(ai, areaInfoHistory, new String[] { "id" });
								areaInfoHistory.setAreainfo_id(ai.getId());
								areaInfoHistory.setFlag(2);
								areaInfoHistoryManager.saveObject(areaInfoHistory);

								ai.setEmployee_b_no(null);
								preObject(ai);
								areaInfoManager.saveObject(ai);
							}
						}

						Area tempArea = new Area();
						BeanUtils.copyProperties(area, tempArea);
						area.setEmployee_b_name(null);
						area.setEmployee_b_no(null);
						preObject(area);
						areaManager.saveObject(area);
						// 给店长发送信息提示
						sendMessage_area(tempArea, employeeNo2, "B");
					}
				}

				//设置mongodb的A国安侠为空
				mongoDBManager.updateTinyAreaEmployeeIdNull(store_id, employeeNo1);
			} catch (Exception e) {
				e.printStackTrace();
				return "false";
			}
		}

		return "success";

	}

	/**
	 * 
	 * TODO 变更国安侠需要给店长提示 2017年9月13日
	 * 
	 * @author gaobaolei
	 * @param area
	 */
	private void sendMessage_area(Area area, String employeeNo, String ab) {
		MessageNewDao messageNewDao = (MessageNewDao) SpringHelper.getBean(MessageNewDao.class.getName());
		UserManager usermanager = (UserManager) SpringHelper.getBean("userManager");
		MessageNewManager messageManager = (MessageNewManager) SpringHelper.getBean("messageNewManager");

		Map<String, Object> storekeeper = messageNewDao.getStorekeeper(area.getStore_id());
		User user = usermanager.findEmployeeByEmployeeNo(storekeeper.get("employeeId").toString());

		Message message = new Message();

		if ("A".equals(ab)) {
			message.setContent("片区 '" + area.getName() + " '负责国安侠A " + area.getEmployee_a_name() + " 已离职，请重置片区负责人");
		} else if ("B".equals(ab)) {
			message.setContent("片区 '" + area.getName() + " '负责国安侠B " + area.getEmployee_b_name() + " 已离职，请重置片区负责人");
		}

		message.setTitle("通知");
		message.setType("other_notice");
		message.setSendId(employeeNo);
		message.setJump_path("message_notice");
		message.setReceiveId(storekeeper.get("employeeId").toString());
		preObject(message);
		messageManager.saveObject(message);// 保存消息
		messageManager.sendMessageAuto(user, message);
	}

	@Override
	public Map<String, Object> getAreaInfoOfTinVillageByStore(Long store_id) {
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		List<Map<String, Object>> list = areaDao.queryNoTinyVillageByStore(store_id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("info", list);
		return result;
	}

	@Override
	public List<Map<String, Object>> getStoreOfByCSZJ_QYJL(Long employeeId, Long cityId, String role) {
		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
		List<Map<String, Object>> storeList = storeDao.getAllStoreOfCRM(employeeId, cityId, role);
		return storeList;
	}

	@Override
	public List<Map<String, Object>> queryEmployeeOfGAX(Long storeId) {
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		List<Map<String, Object>> list = areaDao.queryEmployeeOfGAX(storeId);
		return list;
	}

	@Override
	public List<Map<String, Object>> queryAreaByEmployee(String employeeNo) {
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		List<Map<String, Object>> list = areaDao.getAllAreaOfEmployee(employeeNo);
		return list;
	}

	@Override
	public Map<String, Object> updateEmployeeIsNullOfArea(Long storeId, String employeeNo) {
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			areaDao.updateEmployeeIsNullOfArea(storeId, employeeNo);
			areaDao.updateEmployeeIsNullOfAreaInfo(storeId, employeeNo);
			result.put("status", CodeEnum.success.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			result.put("staus", CodeEnum.error.getValue());
			return result;
		}
		return result;
	}

	@Override
	public Map<String, Object> getTinyVillageCoord(Long storeId) {
		MongoDBDao mDao = (MongoDBDao) SpringHelper.getBean(MongoDBDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Map<String, Object> result = new HashMap<String, Object>();
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		TinyVillageDao tinyVillageDao = (TinyVillageDao)SpringHelper.getBean(TinyVillageDao.class.getName());
		try {
			Store store = storeManager.findStore(storeId);// 查询门店
			MongoDbUtil mDbUtil = (MongoDbUtil) SpringHelper.getBean("mongodb");
			MongoDatabase database = mDbUtil.getDatabase();
			// 先查询门店服务范围

			MongoCollection<Document> collection = database.getCollection("store_service_area");
			FindIterable<Document> dIterable = collection.find(Filters.eq("storeId", store.getPlatformid()));
			Document document = dIterable.first();

			if (document == null) {
				result.put("store", store);
				result.put("code", CodeEnum.nullData.getValue());
				result.put("message", "门店服务范围不存在");
				return result;
			} else {
				org.json.JSONObject jObject = new org.json.JSONObject(document.toJson());
				result.put("serviceArea", JSONArray.parse(jObject.get("vertex").toString()));
			}

			// 查询小区坐标
			collection = database.getCollection("tiny_area");
			dIterable = collection.find(Filters.eq("storeNo", store.getStoreno()));
			MongoCursor<Document> cursor = dIterable.iterator();

			List<Object> tinyVillageCoordList = new ArrayList<Object>();
			if (dIterable == null) {
				result.put("store", store);
				result.put("code", CodeEnum.nullData.getValue());
				result.put("message", "门店管辖小区没有绑定坐标");
				return result;
			} else {
				org.json.JSONArray tmp_jarray = new org.json.JSONArray();
				Map<String, Object> tinyAreaInfo = new HashMap<String, Object>();
				List<Map<String, Object>> tinyvillageArealist = mDao.getAreaOfTinyVillage(storeId.toString());
				List<Map<String, Object>> tinyVillageDetail = null;
				while (cursor.hasNext()) {
					Document teDocument = cursor.next();
					org.json.JSONObject jObject = new org.json.JSONObject(teDocument.toJson());
					jObject.put("areaName", "");
					jObject.put("areaNo", "");
					String code = jObject.getString("code");
					tinyVillageDetail = tinyVillageDao.findTinyVillageInfoByCode(code);
					if(tinyVillageDetail!=null&&tinyVillageDetail.size()>0){
						jObject.put("tinyVillage_id", tinyVillageDetail.get(0).get("tiny_village_id"));
						jObject.put("village_id",tinyVillageDetail.get(0).get("village_id"));
						jObject.put("town_id",tinyVillageDetail.get(0).get("town_id"));
					}
					for (Map<String, Object> m : tinyvillageArealist) {
						if (code.equals(m.get("code"))) {
							jObject.put("areaName", m.get("name"));
							jObject.put("areaNo", m.get("area_no"));
							break;
						}
					}

					tmp_jarray.put(jObject);
				}

				List<Map<String, Object>> areaList = areaDao.selectAreaOfStore(storeId);
				result.put("store", store);
				result.put("areas", areaList);
				result.put("coordinate", JSONArray.parse(tmp_jarray.toString()));
				result.put("code", CodeEnum.success.getValue());
				result.put("message", CodeEnum.success.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();

			result.put("code", CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
		return result;
	}

	@Override
	public Map<String, Object> getTinyVillageCoordOfArea(Long storeId, String areaNo) {
		MongoDBDao mDao = (MongoDBDao) SpringHelper.getBean(MongoDBDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Map<String, Object> result = new HashMap<String, Object>();
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		try {
			Store store = storeManager.findStore(storeId);// 查询门店
			MongoDbUtil mDbUtil = (MongoDbUtil) SpringHelper.getBean("mongodb");
			MongoDatabase database = mDbUtil.getDatabase();

			// 查询小区坐标
			MongoCollection<Document> collection = database.getCollection("tiny_area");
			FindIterable<Document> dIterable = collection.find(Filters.eq("storeNo", store.getStoreno()));
			MongoCursor<Document> cursor = dIterable.iterator();

			List<Object> tinyVillageCoordList = new ArrayList<Object>();
			if (dIterable == null) {
				result.put("store", store);
				result.put("code", CodeEnum.nullData.getValue());
				result.put("message", "门店管辖小区没有绑定坐标");
				return result;
			} else {
				org.json.JSONArray tmp_jarray = new org.json.JSONArray();
				Map<String, Object> tinyAreaInfo = new HashMap<String, Object>();
				List<Map<String, Object>> tinyvillageArealist = mDao.getAreaOfTinyVillage(storeId.toString());
				while (cursor.hasNext()) {
					Document teDocument = cursor.next();
					org.json.JSONObject jObject = new org.json.JSONObject(teDocument.toJson());
					jObject.put("areaName", "");
					jObject.put("areaNo", "");
					String code = jObject.getString("code");

					for (Map<String, Object> m : tinyvillageArealist) {
						if (code.equals(m.get("code"))) {
							jObject.put("areaName", m.get("name"));
							jObject.put("areaNo", m.get("area_no"));
							break;
						}
					}

					tmp_jarray.put(jObject);
				}

				List<Map<String, Object>> areaList = areaDao.selectAreaOfStore(storeId);
				result.put("store", store);
				result.put("areas", areaList);
				result.put("coordinate", JSONArray.parse(tmp_jarray.toString()));
				result.put("code", CodeEnum.success.getValue());
				result.put("message", CodeEnum.success.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();

			result.put("code", CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
		return result;
	}

	@Override
	public Map<String, Object> checkCurrentStore(Long storeId) {
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Store store = userManager.findUserStore();
			Long store_id = store.getStore_id();
			if (storeId != null && store_id != null) {
				if (!storeId.equals(store_id)) {
					result.put("cur_store", "2");
					result.put("cur_name", store.getName());
				} else {
					result.put("cur_store", "1");

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("cur_store", "3");
			return result;
		}

		return result;
	}

	@Override
	public Map<String, Object> getStoreServiceAreaAndPosition(Long storeId) {

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
			MongoDbUtil mDbUtil = (MongoDbUtil) SpringHelper.getBean("mongodb");
			MongoDatabase database = mDbUtil.getDatabase();
			// 先查询门店服务范围
			Store store = storeManager.findStore(storeId);
			MongoCollection<Document> collection = database.getCollection("store_service_area");
			FindIterable<Document> dIterable = collection.find(Filters.eq("storeId", store.getPlatformid()));
			Document document = dIterable.first();

			if (document == null) {
				result.put("code", CodeEnum.nullData.getValue());
				result.put("message", "门店服务范围不存在");
				return result;
			} else {
				org.json.JSONObject jObject = new org.json.JSONObject(document.toJson());
				result.put("serviceArea", JSONArray.parse(jObject.get("vertex").toString()));
			}

			MongoCollection<Document> collection2 = database.getCollection("store_position");
			FindIterable<Document> dIterable2 = collection2.find(Filters.eq("_id", store.getPlatformid()));
			Document document2 = dIterable2.first();

			if (document2 == null) {
				result.put("code", CodeEnum.nullData.getValue());
				result.put("message", "门店位置坐标不存在");
			} else {
				org.json.JSONObject jObject = new org.json.JSONObject(document2.toJson());
				result.put("position", JSONArray.parse(jObject.get("location").toString()));
				result.put("store",store);
			}
			result.put("code", CodeEnum.success.getValue());
			result.put("message", CodeEnum.success.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}

		return result;
	}

	@Override
	public Integer getAreaCountOfStore(Long storeId) {
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		Integer count = 0;
		try {
			List<Map<String, Object>> list = areaDao.selectAreaOfStore(storeId);
			if (list != null) {
				count = list.size();
			}
		} catch (Exception e) {
			return 0;
		}
		return count;
	}

	@Override
	public Map<String, Object> queryAboutOfArea(DynamicDto dynamicDto, PageInfo pageInfo) {
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Map<String, Object> result = new HashMap<String, Object>();
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		HttpClientUtil hClientUtil = null;
		JSONObject param = new JSONObject();
		try {

			if (dynamicDto.getTarget() == 0) {//总部
				if (dynamicDto.getStoreId() == null || "".equals(dynamicDto.getStoreId())) {// 查询所有城市的门店

					StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
					List<Map<String, Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(),
							dynamicDto.getCityId(), "ZB");// 获取门店
					
					StringBuilder storeNO = new StringBuilder();
					StringBuilder storeID = new StringBuilder();
					for (Map<String, Object> map : storeList) {
						if (map.get("store_id") == null || "".equals(map.get("store_id"))) {
							continue;
						}

						storeID.append("," + map.get("store_id"));
					}
					if (storeID.length() > 0) {
						storeID = storeID.deleteCharAt(0);
					} else {
						JSONObject temp = new JSONObject();
						temp.put("data", "");
						temp.put("message", "系统查无此条件的数据！");
						result.put("status", "storefail");
						result.put("data", temp.toString());
						return result;
					}

					dynamicDto.setStoreIds(storeID.toString());
				} else if (dynamicDto.getStoreId() == -10000) {
					dynamicDto.setStoreIds("-10000");
				} else {

					Store store = (Store) storeManager.getObject(dynamicDto.getStoreId());
					dynamicDto
							.setStoreIds(store.getStore_id() == null ? "-10000" : String.valueOf(store.getStore_id()));
				}
			} else if (dynamicDto.getTarget() == 1) {// 城市总监
				if (dynamicDto.getStoreId() == null || "".equals(dynamicDto.getStoreId())) {// 查询所有城市的门店

					StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
					List<Map<String, Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(),
							dynamicDto.getCityId(), "CSZJ");// 获取门店
					StringBuilder storeID = new StringBuilder();
					for (Map<String, Object> map : storeList) {
						if (map.get("store_id") == null || "".equals(map.get("store_id"))) {
							continue;
						}

						storeID.append(",'" + map.get("store_id") + "'");
					}
					if (storeID.length() > 0) {

						storeID = storeID.deleteCharAt(0);

					} else {
						JSONObject temp = new JSONObject();
						temp.put("data", "");
						temp.put("message", "系统查无此条件的数据！");
						result.put("status", "storefail");
						result.put("data", temp.toString());
						return result;
					}

					dynamicDto.setStoreIds(storeID.toString());
				} else if (dynamicDto.getStoreId() == -10000) {
					dynamicDto.setStoreIds("-10000");
				} else {
                    
					Store store = (Store) storeManager.getObject(dynamicDto.getStoreId());
					dynamicDto
							.setStoreIds(store.getStore_id() == null ? "-10000" : String.valueOf(store.getStore_id()));
				}
			} else if (dynamicDto.getTarget() == 2) {// 店长
				Store store = (Store) storeManager.getObject(dynamicDto.getStoreId());
				dynamicDto.setStoreIds(store.getStore_id() == null ? "-10000" : String.valueOf(store.getStore_id()));
			}
			result = areaDao.queryAboutOfArea(dynamicDto, pageInfo);

		} catch (Exception e) {
			e.printStackTrace();
			JSONObject temp = new JSONObject();
			temp.put("data", "");
			temp.put("message", "系统错误！");
			result.put("status", "storefail");
			return result;
		}

		return result;
	}

	@Override
	public Map<String, Object> exportAboutOfArea(DynamicDto dynamicDto) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = this.queryAboutOfArea(dynamicDto, null);
		if (map.get("data") != null) {// 成功返回数据

			List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("data");
			if (list != null && list.size() > 0) {
				String str_file_dir_path = PropertiesUtil.getValue("file.root");
				String str_web_path = PropertiesUtil.getValue("file.web.root");

				HSSFWorkbook wb = new HSSFWorkbook();
				// 创建Excel的工作sheet,对应到一个excel文档的tab

				setCellStyle_common(wb);
				setHeaderStyle(wb);
				HSSFSheet sheet = wb.createSheet("片区信息");
				HSSFRow row = sheet.createRow(0);
				String[] str_headers = { "城市", "门店", "片区名称", "片区编号", "国安侠姓名（A）", "国安侠编号（A）", "国安侠姓名（B）", "国安侠编号（B）" };
				String[] headers_key = { "city_name", "storeName", "name", "area_no", "employee_a_name",
						"employee_a_no", "employee_b_name", "employee_b_no" };
				for (int i = 0; i < str_headers.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(getHeaderStyle());
					cell.setCellValue(new HSSFRichTextString(str_headers[i]));
				}

				for (int i = 0; i < list.size(); i++) {
					row = sheet.createRow(i + 1);
					for (int cellIndex = 0; cellIndex < headers_key.length; cellIndex++) {
						setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
					}
				}

				File file_xls = new File(
						str_file_dir_path + File.separator + System.currentTimeMillis() + "_aboutArea.xls");
				if (file_xls.exists()) {
					file_xls.delete();
				}
				FileOutputStream os = null;
				try {
					os = new FileOutputStream(file_xls.getAbsoluteFile());
					wb.write(os);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				result.put("message", "导出成功！");
				result.put("status", "success");
				result.put("data", str_web_path.concat(file_xls.getName()));
			} else {
				result.put("message", "没有数据！");
				result.put("status", "null");
			}

		} else {
			result.put("message", "请重新操作！");
			result.put("status", "fail");
		}

		return result;
	}

	@Override
	public Area queryAreaByAreaNo(String area_no) {
		IFilter iFilter = FilterFactory.getSimpleFilter("area_no", area_no);
		List<Area> areaInfoList = (List<Area>) this.getList(iFilter);
		if (areaInfoList != null && areaInfoList.size() > 0) {
			return areaInfoList.get(0);
		} else {
			Area area = new Area();
			return area;
		}
	}

	@Override
	public List<AreaInfo> checkTinyVillageAboutCoordinate(Area area) {

		List<AreaInfo> list = area.getChildrens();
		// 已经存在的片区信息
		List<AreaInfo> noCoordinateList = new ArrayList<AreaInfo>();

		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());

		for (AreaInfo ai : list) {

			// 检查是片区信息是否存在
			List<Map<String, Object>> result = areaDao.selectTinyArea(ai);

			if (result != null && result.size() > 0) {
				StringBuilder tinyvillageSb = new StringBuilder();
				String tinyvillage = "";
				String village = "";
				for (int i = 0; i < result.size(); i++) {
					Map<String, Object> obj = result.get(i);
					if (obj.get("tiny_village_id") == null) {// 小区未划定坐标
						tinyvillageSb.append("、").append(obj.get("name").toString());
					}
				}

				if (tinyvillageSb.toString().contains("、")) {
					tinyvillage = tinyvillageSb.toString().substring(1);
					ai.setTiny_village_name(tinyvillage);
					noCoordinateList.add(ai);
				}
			}
		}

		return noCoordinateList;
	}

	@Override
	public Map<String, Object> getDataOfArea(String areaNo) {
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> house = areaDao.selectHouseAmountOfArea(areaNo);
			List<Map<String, Object>> tinyVillage = areaDao.selectTinyVillageOfArea(areaNo);
			List<Map<String, Object>> area = areaDao.selectAreaByAreaNo(areaNo);
			result.put("house", house.get(0).get("total"));
			result.put("tinyVillage", tinyVillage.size());
			result.put("area", area.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	@Override
	public Map<String, Object> findAreaInfoByTinyId(Long tinyId) {
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		List<Map<String, Object>> list = areaDao.findAreaInfoById(tinyId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	
	@Override
	public Map<String, Object> selectTinyVillageCoordOfArea(Long storeId, String areaNo) {
		MongoDBDao mDao = (MongoDBDao) SpringHelper.getBean(MongoDBDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Map<String, Object> result = new HashMap<String, Object>();
		AreaDao areaDao = (AreaDao) SpringHelper.getBean(AreaDao.class.getName());
		try {
			Store store = storeManager.findStore(storeId);// 查询门店
			MongoDbUtil mDbUtil = (MongoDbUtil) SpringHelper.getBean("mongodb");
			MongoDatabase database = mDbUtil.getDatabase();

			
			MongoCollection<Document> collection = database.getCollection("store_service_area");
			FindIterable<Document> dIterable = collection.find(Filters.eq("storeId", store.getPlatformid()));
			Document document = dIterable.first();

			
			if (document == null) {
				result.put("store", store);
				result.put("code", CodeEnum.nullData.getValue());
				result.put("message", "门店服务范围不存在");
				return result;
			} else {
				org.json.JSONObject jObject = new org.json.JSONObject(document.toJson());
				result.put("serviceArea", JSONArray.parse(jObject.get("vertex").toString()));
			}
			
			// 查询小区坐标
			collection = database.getCollection("tiny_area");
			dIterable = collection.find(Filters.eq("storeNo", store.getStoreno()));
			MongoCursor<Document> cursor = dIterable.iterator();

			List<Object> tinyVillageCoordList = new ArrayList<Object>();
			if (dIterable == null) {
				result.put("store", store);
				result.put("code", CodeEnum.nullData.getValue());
				result.put("message", "门店管辖小区没有绑定坐标");
				return result;
			} else {
				org.json.JSONArray tmp_jarray = new org.json.JSONArray();
				Map<String, Object> tinyAreaInfo = new HashMap<String, Object>();
				List<Map<String, Object>> tinyvillageArealist = mDao.queryAllTinyVillageOfArea(storeId,areaNo);
				Map<String, Object> tinyvillage_map = new HashMap<String,Object>();
				while (cursor.hasNext()) {
					Document teDocument = cursor.next();
					org.json.JSONObject jObject = new org.json.JSONObject(teDocument.toJson());
					String code_mb = jObject.getString("code");
					tinyvillage_map.put(code_mb, jObject);
				}
				for (Map<String, Object> m : tinyvillageArealist) {
					
					String code=m.get("code")==null?"":m.get("code").toString();
					Object temp_obj = tinyvillage_map.get(code);
					if(temp_obj!=null){
						tmp_jarray.put(temp_obj);
					}
					
				}

				
				result.put("store", store);
				result.put("tinyVillage", tinyvillageArealist);
				result.put("coordinate", JSONArray.parse(tmp_jarray.toString()));
				result.put("code", CodeEnum.success.getValue());
				result.put("message", CodeEnum.success.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();

			result.put("code", CodeEnum.error.getValue());
			result.put("message", CodeEnum.error.getDescription());
			return result;
		}
		return result;
	}


}
