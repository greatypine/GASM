package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;

import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.apache.poi.hssf.record.formula.functions.If;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.CompanyDao;
import com.cnpc.pms.personal.dao.NewsDao;
import com.cnpc.pms.personal.dao.OfficeDao;
import com.cnpc.pms.personal.dao.PersonDutyDao;
import com.cnpc.pms.personal.dto.CompanyDTO;
import com.cnpc.pms.personal.entity.City;
import com.cnpc.pms.personal.entity.Company;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.Office;
import com.cnpc.pms.personal.entity.PersonDuty;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.CityManager;
import com.cnpc.pms.personal.manager.CompanyManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.OfficeManager;
import com.cnpc.pms.personal.manager.PersonDutyManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.personal.util.DataTransfromUtil;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;


public class OfficeManagerImpl extends BaseManagerImpl implements OfficeManager {

	PropertiesValueUtil propertiesValueUtil = null;

	@Override
	public Office getOfficeByOffice_name(String office_name, Long village_id) {
		List<?> list = this.getList(FilterFactory
				.getSimpleFilter("village_id = " + village_id + " and office_name = '" + office_name + "'"));
		if (list != null && list.size() > 0) {
			return (Office) list.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> getOfficeList(QueryConditions conditions) {
		OfficeDao officeDao = (OfficeDao) SpringHelper.getBean(OfficeDao.class.getName());
		// 查询的数据条件
		StringBuilder sb_where = new StringBuilder();
		// 分页对象
		PageInfo obj_page = conditions.getPageinfo();
		// 返回的对象，包含数据集合、分页对象等
		Map<String, Object> map_result = new HashMap<String, Object>();

		for (Map<String, Object> map_where : conditions.getConditions()) {
			if ("province_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND pro.name like '").append(map_where.get("value")).append("'");
			}
			if ("town_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND town.name like '").append(map_where.get("value")).append("'");
			}
			if ("store_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				String string = DataTransfromUtil.getTownIdByStoreName(map_where.get("value")+"");
				if(string.length()>0){
					sb_where.append(" AND town.id in ( ").append(string).append(")");
				}else{
					sb_where.append(" AND 1=0 ");
				}
				
			}
			if ("office_id".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				String id = map_where.get("value").toString().replace("%", "");
				sb_where.append(" AND off.office_id = '").append(id).append("'");
			}
			if ("office_name".equals(map_where.get("key")) && null != map_where.get("value")
					&& !"".equals(map_where.get("value"))) {
				sb_where.append(" AND off.office_name like '").append(map_where.get("value")).append("'");
			}

		}
		 User sessionUser = null;
   		if (null != SessionManager.getUserSession()
   				&& null != SessionManager.getUserSession().getSessionData()) {
   			sessionUser = (User) SessionManager.getUserSession()
   					.getSessionData().get("user");
   		}
   		UserManager userManager=(UserManager)SpringHelper.getBean("userManager");
   		sessionUser=userManager.findUserById(sessionUser.getId());
   		StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
   		if(3224==sessionUser.getUsergroup().getId()){
   			//sb_where.append(" AND bus.employee_no='").append(sessionUser.getEmployeeId()).append("'");
   			Store store = storeManager.findStore(sessionUser.getStore_id());
   			if(store!=null){
   				if(store.getTown_id()!=null&&!store.getTown_id().equals("")){
   					sb_where.append(" AND town.id in (").append(store.getTown_id()).append(") ");
   				}else{
   					sb_where.append(" AND town.id=").append(0);
   				}
   				
   			}
   		}else if(3231==sessionUser.getUsergroup().getId()||3223==sessionUser.getUsergroup().getId()||3225==sessionUser.getUsergroup().getId()||3229==sessionUser.getUsergroup().getId()){
   			Store store = storeManager.findStore(sessionUser.getStore_id());
   			if(store!=null){
   				if(store.getTown_id()!=null&&!store.getTown_id().equals("")){
   					sb_where.append(" AND town.id in (").append(store.getTown_id()).append(") ");
   				}else{
   					sb_where.append(" AND town.id=").append(0);
   				}
   				
   			}
   		}else if(sessionUser.getZw().equals("地址采集")||sessionUser.getCode().equals("sunning1")){
			sb_where.append(" and 1=1 ");
		}else{
			//取得当前登录人 所管理城市
			String cityssql = "";
			List<DistCity> distCityList = userManager.getCurrentUserCity();
			if(distCityList!=null&&distCityList.size()>0){
				for(DistCity d:distCityList){
					cityssql += "'"+d.getCityname()+"',";
				}
				cityssql=cityssql.substring(0, cityssql.length()-1);
			}
			if(cityssql!=""&&cityssql.length()>0){
				String cityId = getCityId(cityssql);
				if(!"".equals(cityId)){
					cityId=cityId.substring(0, cityId.length()-1);
					sb_where.append(" and city.id in ("+cityId+")");
				}else{
					sb_where.append(" and 0=1 ");
				}
				
			}else{
				sb_where.append(" and 0=1 ");
			}
		};
		
		System.out.println(sb_where);
		map_result.put("pageinfo", obj_page);
		map_result.put("header", "写字楼信息");
		map_result.put("data", officeDao.getOfficeList(sb_where.toString(), obj_page));
		return map_result;
	}

	public Result saveOffice(CompanyDTO companydto) {
		Result res = new Result();
		Office office=new Office();
		office.setOffice_address(companydto.getOffice_address());
		office.setOffice_area(companydto.getOffice_area());
		office.setOffice_floor(companydto.getOffice_floor());
		office.setOffice_name(companydto.getOffice_name());
		office.setOffice_parking(companydto.getOffice_parking());
		office.setOffice_type(companydto.getOffice_type());
		office.setVillage_id(companydto.getVillage_id());
		office.setTown_id(companydto.getTown_id());
		//office.setCreate_user(companydto.getCreate_user());
		office.setEmployee_no(companydto.getEmployee_no());
		//
		Office officeSele = getOfficeByOffice_name_address(companydto.getOffice_name(),companydto.getVillage_id(),companydto.getOffice_address());
		if(officeSele==null){
			officeSele=new Office();
			officeSele.setCreate_user(companydto.getCreate_user());
			officeSele.setCreate_user_id(companydto.getCreate_user_id());
			officeSele.setCreate_time(new Date());
		}
		officeSele.setUpdate_user(companydto.getCreate_user());
		officeSele.setUpdate_user_id(companydto.getCreate_user_id());
		officeSele.setUpdate_time(new Date());
		BeanUtils.copyProperties(office, officeSele
				,new String[]{"office_id","create_user","create_user_id","create_time"
							,"update_user","update_user_id","update_time"});
		
		save(officeSele);
		if(companydto.getOffice_company()!=null&&!companydto.getOffice_company().equals("")){
			Company company=new Company();
			company.setOffice_company(companydto.getOffice_company());
			company.setOffice_floor_of_company(companydto.getOffice_floor_of_company());
			company.setOffice_id(officeSele.getOffice_id());
			
			CompanyManager companyManager=(CompanyManager)SpringHelper.getBean("companyManager");
			Company companyByOffice_company = companyManager.getCompanyByOffice_company(companydto.getOffice_company(),companydto.getOffice_floor_of_company(),officeSele.getOffice_id());
			if(companyByOffice_company==null){
				companyManager.save(company);
			}
		}
		res.setCode(200);
		res.setMessage("添加成功！");
		return res;
	}


	@Override
	public Map<String, Object> getOfficeById(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		IFilter filter = FilterFactory.getSimpleFilter("office_id", id);
		if (filter != null) {
			Office office = (Office) getUnique(filter);
			map.put("office_name", office.getOffice_name());
			map.put("office_address", office.getOffice_address());
			map.put("office_area", office.getOffice_area());
			map.put("office_id", office.getOffice_id());
			map.put("office_floor", office.getOffice_floor());
			map.put("office_type", office.getOffice_type());
		}
		return map;
	}

	@Override
	public Office updateOffice(Office office1) {
		User sessionUser = null;
		if (null != SessionManager.getUserSession()
				&& null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession()
					.getSessionData().get("user");
		}
		OfficeManager officeManager=(OfficeManager)SpringHelper.getBean("officeManager");
		CompanyManager companyManager=(CompanyManager)SpringHelper.getBean("companyManager");
		NewsDao newsDao=(NewsDao)SpringHelper.getBean(NewsDao.class.getName());
		CompanyDao companyDao=(CompanyDao)SpringHelper.getBean(CompanyDao.class.getName());
		Office officeSele = getOfficeByOffice_name_address(office1.getOffice_name(),office1.getVillage_id(),office1.getOffice_address());
		if(office1.getOffice_id()!=null){
			//修改写字楼表
			 Office updateoffice = getOfficeDataById(office1.getOffice_id());
			 BeanUtils.copyProperties(office1, updateoffice
						,new String[]{"office_id","create_user","create_user_id","create_time"
									,"update_user","update_user_id","update_time","employee_no"});
			 updateoffice.setUpdate_user(sessionUser.getName());
			 updateoffice.setUpdate_user_id(Integer.parseInt(sessionUser.getId()+""));
			 updateoffice.setUpdate_time(new Date());
			if(updateoffice.getEmployee_no()==null){
				if(officeSele!=null&&officeSele.getEmployee_no()!=null){
					updateoffice.setEmployee_no(sessionUser.getEmployeeId());
				}else{
					updateoffice.setEmployee_no(sessionUser.getEmployeeId());
				}
			}
			officeManager.saveObject(updateoffice);
			Set<Company> companySet = office1.getCompany();
			if(companySet.size()>0){
				//将已走的入驻公司删除
				String string="";
				for (Company company : companySet) {
					if(company.getCompany_id()!=null){
						string+=","+company.getCompany_id();
					}
				}
				if(!"".equals(string)&&string.length()>0){
					string=string.substring(1,string.length());
					companyDao.getComPanybyOffice_idandCompany_id(office1.getOffice_id(), string);
				}
				//修改或添加入驻公司
				for (Company company : companySet) {
					if(company.getOffice_company()!=null&&!company.getOffice_company().equals("")){
						Company company1=new Company();
						company1.setOffice_company(company.getOffice_company());
						company1.setOffice_floor_of_company(company.getOffice_floor_of_company());
						company1.setOffice_id(updateoffice.getOffice_id());
						if(company.getCompany_id()!=null){
							company1.setCompany_id(company.getCompany_id());
						}
						//Company companyByOffice_company = companyManager.getCompanyByOffice_company(company.getOffice_company(),company.getOffice_floor_of_company(),officeSele.getOffice_id());
						Company companyByOffice_company = companyDao.getCompany(company.getOffice_company(),company.getOffice_floor_of_company(),updateoffice.getOffice_id());
						if(companyByOffice_company==null){
							companyDao.saveOrUpdate(company1);
						}
					}
				}
			}else{
				Set<Company> set = companyManager.getComPanyByOfficeID(office1.getOffice_id());
				Iterator<Company> it = set.iterator();
				while(it.hasNext()){
					newsDao.deleteObject((Company)it.next());
				}
			}	 
			if(officeSele!=null&&!officeSele.getOffice_id().equals(office1.getOffice_id())){
			Set<Company> set = companyManager.getComPanyByOfficeID(officeSele.getOffice_id());
			Iterator<Company> it = set.iterator();
			while(it.hasNext()){
				newsDao.deleteObject((Company)it.next());
			}
			newsDao.deleteObject(officeSele);
			
		}
			updateOrinsertDuty(Long.valueOf(updateoffice.getOffice_id()),updateoffice.getJob(),5,sessionUser.getEmployeeId());
		}else{
			Office office=new Office();
			office.setOffice_address(office1.getOffice_address());
			office.setOffice_area(office1.getOffice_area());
			office.setOffice_floor(office1.getOffice_floor());
			office.setOffice_name(office1.getOffice_name());
			office.setOffice_parking(office1.getOffice_parking());
			office.setOffice_type(office1.getOffice_type());
			office.setVillage_id(office1.getVillage_id());
			office.setTown_id(office1.getTown_id());
			office.setEmployee_no(office1.getEmployee_no());
			office.setJob(office1.getJob());
			if(officeSele==null){
				officeSele=new Office();
				officeSele.setCreate_user(sessionUser.getName());
				officeSele.setCreate_user_id(Integer.parseInt(sessionUser.getId()+""));
				officeSele.setCreate_time(new Date());
			}
			officeSele.setUpdate_user(sessionUser.getName());
			officeSele.setUpdate_user_id(Integer.parseInt(sessionUser.getId()+""));
			officeSele.setUpdate_time(new Date());
			BeanUtils.copyProperties(office, officeSele
					,new String[]{"office_id","create_user","create_user_id","create_time"
								,"update_user","update_user_id","update_time","employee_no"});
			if(officeSele.getEmployee_no()==null){
				officeSele.setEmployee_no(sessionUser.getEmployeeId());
			}
			officeManager.saveObject(officeSele);
			
				Set<Company> set = companyManager.getComPanyByOfficeID(officeSele.getOffice_id());
				Iterator<Company> it = set.iterator();
				while(it.hasNext()){
					newsDao.deleteObject((Company)it.next());
				}
			Set<Company> companySet = office1.getCompany();
			if(companySet!=null&&companySet.size()>0){
				for (Company company : companySet) {
					if(company.getOffice_company()!=null&&!company.getOffice_company().equals("")){
						Company company1=new Company();
						company1.setOffice_company(company.getOffice_company());
						company1.setOffice_floor_of_company(company.getOffice_floor_of_company());
						company1.setOffice_id(officeSele.getOffice_id());
						Company companyByOffice_company = companyManager.getCompanyByOffice_company(company.getOffice_company(),company.getOffice_floor_of_company(),officeSele.getOffice_id());
						if(companyByOffice_company==null){
							//companyManager.save(company1);
							companyDao.saveOrUpdate(company1);
						}
					}
				}
			}
			updateOrinsertDuty(Long.valueOf(officeSele.getOffice_id()),officeSele.getJob(),5,sessionUser.getEmployeeId());
		}
		
		return office1;
	}
	

	@Override
	public Result getOfficeByOfficeAddress_name(CompanyDTO companydto) {
		Result res = new Result();
		
		Office office = getOfficeByOffice_name_address(companydto.getOffice_name(),companydto.getVillage_id(),companydto.getOffice_address());
		
		if(office!=null){
			res.setCode(300);
			res.setMessage( companydto.getOffice_name()+ "已存在");
			return res;
		}
		res.setCode(200);
		res.setMessage( companydto.getOffice_name()+ "不存在");
		return res;
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
	@Override
	public Office getOfficeByOffice_name_address(String office_name, Long village_id,String address) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("village_id = "+village_id + " and office_name = '"+office_name+"' and office_address='"+address+"'"));
		if(list!=null&&list.size()>0){
			return (Office)list.get(0);
		}
		return null;
	}

	public void setCellValue(Row row, int nCellIndex, Object value, CellStyle style,DataFormat df) {
		boolean isInteger = false;
		if(value != null){
			isInteger=value.toString().matches("^[-\\+]?[\\d]*$");
		}
		if (isInteger) {
			style.setDataFormat(df.getFormat("#,#0"));//数据格式只显示整数
        }
		
		Cell cell = row.createCell(nCellIndex);
		cell.setCellStyle(style);
		cell.setCellValue(new HSSFRichTextString(value == null ? null : value.toString()));
	}

	/**
	 * 生成我rkbook
	 * 
	 * @param resultList
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public Workbook resultSetToExcel(List<Map<String, Object>> resultList, String sheetName, String[] tHeadName,
			String[] tHeadKey,int[] repead) throws Exception {
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet(sheetName);
		sheet.setDefaultColumnWidth(20);
		sheet.setAutobreaks(true);
		Row row = null;
		int rowIndex = 0;
		/**
		 * 设置表头
		 */
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		CellStyle style = null;
		Font font = null;
		// 头样式
		style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
		style.setWrapText(true);

		font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);// HSSFColor.VIOLET.index //字体颜色
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
		style.setFont(font);
		
		DataFormat df = workbook.createDataFormat(); // 此处设置数据格式

		for (int i = 0; i < tHeadName.length; i++) {
			setCellValue(row, i, tHeadName[i], style,df);
		}
		rowIndex++;
		/**
		 * 设置数据
		 */
		String tempid = null;
		// 数据样式
		style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
		style.setWrapText(true);
		
		for (Map<String, Object> map : resultList) {
			row = sheet.createRow(rowIndex);
			if (tempid != null && tempid.equals(String.valueOf(map.get("office_id")))) {
				for (int i = 0; i < tHeadKey.length; i++) {
					setCellValue(row, i, null, style ,df);
				}
				
				for (int rep : repead) {
					setCellValue(row, rep, map.get(tHeadKey[rep]), style,df);
				}
			} else {
				for (int i = 0; i < tHeadKey.length; i++) {
					setCellValue(row, i, map.get(tHeadKey[i]), style,df);
				}
				tempid = String.valueOf(map.get("office_id"));
			}
			rowIndex++;
		}
		return workbook;
	}

	@Override
	public File exportOfficeExce(Map<String ,Object > param) throws Exception {
		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_newfilepath = str_file_dir_path + "office_list.xls";
		File file_new = new File(str_newfilepath);
		if (file_new.exists()) {
			file_new.delete();
		}
		FileOutputStream fis_out_excel = null;
		// 表头数据（确保name 和 key 一一对应）
		String[] tHeadName = { /* "城市id","城市名", */ "写字楼id", "写字楼名","街道id", "街道名", "社区id", "社区名", "写字楼地址", "写字楼类型",
				"写字楼面积", "写字楼车位数", "楼层数", "公司id", "公司名", "公司楼层" };
		String[] tHeadKey = { /* "city_id","city_name", */"office_id","office_name","town_id", "town_name", "village_id", "village_name",
				 "office_address", "office_type", "office_area", "office_parking",
				"office_floor", "company_id", "company_name", "company_floor" };
		OfficeDao officeDao = (OfficeDao) SpringHelper.getBean(OfficeDao.class.getName());
		param.put("is_check", "0");//只导出未被check的数据
		//条件筛选
		String where = createWhereStr(param);
		List<Map<String, Object>> resultList = officeDao.getPrintOfficeList(where, 65530);//sheet最多只能容下65532条数据
		try {
			Workbook officeWorkBook = this.resultSetToExcel(resultList, "office_lsit", tHeadName, tHeadKey,new int[]{11,12,13});
			fis_out_excel = new FileOutputStream(file_new);
			officeWorkBook.write(fis_out_excel);
		} catch (Exception e) {
			throw e;
		} finally {
			if (fis_out_excel != null) {
				fis_out_excel.close();
			}
		}
		return file_new;
	}
	
	@Override
	public File exportWholeOfficeExce(Map<String ,Object > param) throws Exception {
		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_newfilepath = str_file_dir_path + "office_list.xls";
		File file_new = new File(str_newfilepath);
		if (file_new.exists()) {
			file_new.delete();
		}
		
		// 表头数据（确保name 和 key 一一对应）
		String[] tHeadName = { "编号", "省份","城市", "区县", "街道", "社区", 
				"社区gb_code", "具体地址","写字楼名称","写字楼级别/物业类型","写字楼建筑面积（平方米，m2）", "层数（写字楼总层数）",
				"停车位", "入驻公司名称", "楼层","门店名","国安侠" };
		String[] tHeadKey = {"office_id","province_name","city_name","county_name","town_name","village_name",
				"gb_code","office_address","office_name","office_type","office_area","office_floor",
				"office_parking","company_name","company_floor","store_name","user_name"};
		OfficeDao officeDao = (OfficeDao) SpringHelper.getBean(OfficeDao.class.getName());
		//条件筛选
		String where = createWhereStr(param);
		List<Map<String, Object>> resultList = officeDao.getPrintWholeOfficeList(where, 65530);//sheet最多只能容下65532条数据
		FileOutputStream fis_out_excel = null;
		try {
			Workbook officeWorkBook = this.resultSetToExcel(resultList, "office_whole_list", tHeadName, tHeadKey,new int[] {13,14,15,16});
			fis_out_excel = new FileOutputStream(file_new);
			officeWorkBook.write(fis_out_excel);
		} catch (Exception e) {
			throw e;
		} finally {
			if (fis_out_excel != null) {
				fis_out_excel.close();
			}
		}
		return file_new;
	}
	

	/*
	 * 组织where语句
	 */
	String createWhereStr(Map<String ,Object > param){
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(" 1=1 ");
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			if("is_check".equals(entry.getKey())){
				sbuffer.append(" and (");
				sbuffer.append("off.is_check = ").append(entry.getValue());
				sbuffer.append(" or ");
				sbuffer.append("off.is_check is ").append("NULL").append(") ");
			}
			if("town_name".equals(entry.getKey())){
				sbuffer.append(" and ");
				sbuffer.append("town.name like '").append("%").append(entry.getValue()).append("%'");
			}
			if("store_name".equals(entry.getKey())){
				sbuffer.append(" and ");
				sbuffer.append("store.name like '").append("%").append(entry.getValue()).append("%'");
			}
		}
		return sbuffer.toString();
	}

	@Override
	public void saveList(List<Office> officeList,List<Company> companyList) {
		User sessionUser = null;
		if(null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User)SessionManager.getUserSession().getSessionData().get("user");
		}
		Date date = new Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		OfficeDao officeDao = (OfficeDao) SpringHelper.getBean(OfficeDao.class.getName());
		for (Office office : officeList) {
			office.setUpdate_time(sdate);
			office.setUpdate_user(sessionUser.getName());
			office.setUpdate_user_id(sessionUser.getId().intValue());
			officeDao.save(office);
		}
		CompanyManager companyManager = (CompanyManager) SpringHelper.getBean("companyManager");
		for (Company company : companyList) {
			companyManager.save(company);
		}
	}

	@Override
	public Result saveOfficeData(Office office1) {
		Result res = new Result();
		Office office=new Office();
		office.setOffice_address(office1.getOffice_address());
		office.setOffice_area(office1.getOffice_area());
		office.setOffice_floor(office1.getOffice_floor());
		office.setOffice_name(office1.getOffice_name());
		office.setOffice_parking(office1.getOffice_parking());
		office.setOffice_type(office1.getOffice_type());
		office.setVillage_id(office1.getVillage_id());
		office.setTown_id(office1.getTown_id());
		//office.setCreate_user(companydto.getCreate_user());
		office.setEmployee_no(office1.getEmployee_no());
		//
		Office officeSele = getOfficeByOffice_name_address(office1.getOffice_name(),office1.getVillage_id(),office1.getOffice_address());
		if(officeSele==null){
			officeSele=new Office();
			officeSele.setCreate_user(office1.getCreate_user());
			officeSele.setCreate_user_id(office1.getCreate_user_id());
			officeSele.setCreate_time(new Date());
		}
		officeSele.setUpdate_user(office1.getCreate_user());
		officeSele.setUpdate_user_id(office1.getCreate_user_id());
		officeSele.setUpdate_time(new Date());
		BeanUtils.copyProperties(office, officeSele
				,new String[]{"office_id","create_user","create_user_id","create_time"
							,"update_user","update_user_id","update_time"});
		
		save(officeSele);
		Set<Company> companySet = office1.getCompany();
		if(companySet!=null){
			for (Company company : companySet) {
				if(company.getOffice_company()!=null&&!company.getOffice_company().equals("")){
					Company company1=new Company();
					company1.setOffice_company(company.getOffice_company());
					company1.setOffice_floor_of_company(company.getOffice_floor_of_company());
					company1.setOffice_id(officeSele.getOffice_id());
					
					CompanyManager companyManager=(CompanyManager)SpringHelper.getBean("companyManager");
					Company companyByOffice_company = companyManager.getCompanyByOffice_company(company.getOffice_company(),company.getOffice_floor_of_company(),officeSele.getOffice_id());
					if(companyByOffice_company==null){
						companyManager.save(company1);
					}
				}
			}
		}
		
		res.setCode(200);
		res.setMessage("添加成功！");
		return res;
	}
	
	
	public Result getOfficeByOfficeAddress_name_data(Office office1) {
		Result res = new Result();
		
		Office office = getOfficeByOffice_name_address(office1.getOffice_name(),office1.getVillage_id(),office1.getOffice_address());
		
		if(office!=null){
			res.setCode(300);
			res.setMessage( office1.getOffice_name()+ "已存在");
			return res;
		}
		res.setCode(200);
		res.setMessage( office1.getOffice_name()+ "不存在");
		return res;
	}

	@Override
	public Result getOfficeByOfficeAddressandnamedata(Office office1) {
		VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
		TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
		CompanyManager companyManager=(CompanyManager)SpringHelper.getBean("companyManager");
		Result result = new Result();
		List<Office> list = new ArrayList<Office>();
		 IFilter filter = FilterFactory.getSimpleFilter("1=1");

	        if(ValueUtil.checkValue(office1.getOffice_name())){
	            filter = filter.appendAnd(FilterFactory.getEq("office_name",office1.getOffice_name()));
	        }

	        if(ValueUtil.checkValue(office1.getOffice_address())){
	            filter =  filter.appendAnd(FilterFactory.getEq("office_address",office1.getOffice_address()));
	        }
	        List<?> lst_result = this.getList(filter);
	        result.setCode(CodeEnum.success.getValue());
	        result.setMessage(CodeEnum.success.getDescription());
	        if(lst_result == null || lst_result.size() == 0){
	            result.setListOffice(new ArrayList<Office>());
	        }else{
	        	List<Office> listbus=(List<Office>) lst_result;
	        	for (Office office : listbus) {
	        		Set<Company> set = companyManager.getComPanyByOfficeID(office.getOffice_id());
	        		office.setCompany(set);
	        		if(office.getVillage_id()!=0){
	        			Map<String, Object> map = villageManager.getVillageTownInfoByVillage_id(office.getVillage_id());
	        			office.setProvince_name(map.get("province_name").toString());
	        			office.setProvince_id(Long.valueOf(String.valueOf(map.get("province_id").toString())).longValue());
	        			office.setCity_id(Long.valueOf(String.valueOf(map.get("city_id").toString())).longValue());
	        			office.setCity_name(map.get("city_name").toString());
	        			office.setVillage_id(Long.valueOf(String.valueOf(map.get("village_id").toString())).longValue());
	        			office.setVillage_name(map.get("village_name").toString());
	        			office.setTown_id(Long.valueOf(String.valueOf(map.get("town_id").toString())).longValue());
	        			office.setTown_name(map.get("town_name").toString());
	        			office.setCounty_id(Long.valueOf(String.valueOf(map.get("county_id").toString())).longValue());
	        			office.setCounty_name(map.get("county_name").toString());
	        			list.add(office);
	        		}else if(office.getTown_id()!=0){
	        			Map<String, Object> map = townManager.getTownParentInfoByTown_id(office.getTown_id());
	        			office.setProvince_name(map.get("province_name").toString());
	        			office.setProvince_id(Long.valueOf(String.valueOf(map.get("province_id").toString())).longValue());
	        			office.setCity_id(Long.valueOf(String.valueOf(map.get("city_id").toString())).longValue());
	        			office.setCity_name(map.get("city_name").toString());
	        			office.setTown_id(Long.valueOf(String.valueOf(map.get("town_id").toString())).longValue());
	        			office.setTown_name(map.get("town_name").toString());
	        			office.setCounty_id(Long.valueOf(String.valueOf(map.get("county_id").toString())).longValue());
	        			office.setCounty_name(map.get("county_name").toString());
	        			list.add(office);
	        		}
				}
	        	result.setListOffice(list);
	        }
	        return result;
	}
	
	
	
	@Override
	public Result findOfficeByOfficeAddressandnamedata(Office office1) {
		VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
		TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
		CompanyManager companyManager=(CompanyManager)SpringHelper.getBean("companyManager");
		Result result = new Result();
		List<Office> list = new ArrayList<Office>();
		 IFilter filter = FilterFactory.getSimpleFilter("1=1");

	        if(ValueUtil.checkValue(office1.getOffice_name())){
	            filter = filter.appendAnd(FilterFactory.getEq("office_name",office1.getOffice_name()));
	        }

	        if(ValueUtil.checkValue(office1.getTown_id())){
	            filter =  filter.appendAnd(FilterFactory.getEq("town_id",office1.getTown_id()));
	        }
	        List<?> lst_result = this.getList(filter);
	        result.setCode(CodeEnum.success.getValue());
	        result.setMessage(CodeEnum.success.getDescription());
	        if(lst_result == null || lst_result.size() == 0){
	            result.setListOffice(new ArrayList<Office>());
	        }else{
	        	List<Office> listbus=(List<Office>) lst_result;
	        	for (Office office : listbus) {
	        		Set<Company> set = companyManager.getComPanyByOfficeID(office.getOffice_id());
	        		office.setCompany(set);
	        		if(office.getVillage_id()!=0){
	        			Map<String, Object> map = villageManager.getVillageTownInfoByVillage_id(office.getVillage_id());
	        			office.setProvince_name(map.get("province_name").toString());
	        			office.setProvince_id(Long.valueOf(String.valueOf(map.get("province_id").toString())).longValue());
	        			office.setCity_id(Long.valueOf(String.valueOf(map.get("city_id").toString())).longValue());
	        			office.setCity_name(map.get("city_name").toString());
	        			office.setVillage_id(Long.valueOf(String.valueOf(map.get("village_id").toString())).longValue());
	        			office.setVillage_name(map.get("village_name").toString());
	        			office.setTown_id(Long.valueOf(String.valueOf(map.get("town_id").toString())).longValue());
	        			office.setTown_name(map.get("town_name").toString());
	        			office.setCounty_id(Long.valueOf(String.valueOf(map.get("county_id").toString())).longValue());
	        			office.setCounty_name(map.get("county_name").toString());
	        			list.add(office);
	        		}else if(office.getTown_id()!=0){
	        			Map<String, Object> map = townManager.getTownParentInfoByTown_id(office.getTown_id());
	        			office.setProvince_name(map.get("province_name").toString());
	        			office.setProvince_id(Long.valueOf(String.valueOf(map.get("province_id").toString())).longValue());
	        			office.setCity_id(Long.valueOf(String.valueOf(map.get("city_id").toString())).longValue());
	        			office.setCity_name(map.get("city_name").toString());
	        			office.setTown_id(Long.valueOf(String.valueOf(map.get("town_id").toString())).longValue());
	        			office.setTown_name(map.get("town_name").toString());
	        			office.setCounty_id(Long.valueOf(String.valueOf(map.get("county_id").toString())).longValue());
	        			office.setCounty_name(map.get("county_name").toString());
	        			list.add(office);
	        		}
				}
	        	result.setListOffice(list);
	        }
	        return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public Result getOfficeByOfficeAddressnamedata(Office office1) {
			Result res = new Result();
		
		Office office = getOfficeByOffice_name_address(office1.getOffice_name(),office1.getVillage_id(),office1.getOffice_address());
		
		if(office!=null){
			if(office1.getOffice_id()!=null&&!office1.getOffice_id().equals(office.getOffice_id())){
				res.setCode(300);
				res.setMessage( office1.getOffice_name()+ "已存在");
				return res;
			}
			if(office1.getOffice_id()==null){
			res.setCode(300);
			res.setMessage( office1.getOffice_name()+ "已存在");
			return res;
			}
		}
		res.setCode(200);
		res.setMessage( office1.getOffice_name()+ "不存在");
		return res;
	}

	@Override
	public Result save_OfficeData(Office office1) {
		OfficeManager officeManager=(OfficeManager)SpringHelper.getBean("officeManager");
		CompanyManager companyManager=(CompanyManager)SpringHelper.getBean("companyManager");
		NewsDao newsDao=(NewsDao)SpringHelper.getBean(NewsDao.class.getName());
		CompanyDao companyDao=(CompanyDao)SpringHelper.getBean(CompanyDao.class.getName());
		Result res = new Result();
		Office officeSele = getOfficeByOffice_name_address(office1.getOffice_name(),office1.getVillage_id(),office1.getOffice_address());
		if(office1.getOffice_id()!=null){
			//修改写字楼表
			 Office updateoffice = getOfficeDataById(office1.getOffice_id());
			 BeanUtils.copyProperties(office1, updateoffice
						,new String[]{"office_id","create_user","create_user_id","create_time"
									,"update_user","update_user_id","update_time","employee_no"});
			 updateoffice.setUpdate_user(office1.getCreate_user());
			 updateoffice.setUpdate_user_id(office1.getCreate_user_id());
			 updateoffice.setUpdate_time(new Date());
			if(updateoffice.getEmployee_no()==null){
				if(officeSele!=null&&officeSele.getEmployee_no()!=null){
					updateoffice.setEmployee_no(officeSele.getEmployee_no());
				}else{
					updateoffice.setEmployee_no(office1.getEmployee_no());
				}
			}
			officeManager.saveObject(updateoffice);
			Set<Company> companySet = office1.getCompany();
			if(companySet.size()>0){
				//将已走的入驻公司删除
				String string="";
				for (Company company : companySet) {
					if(company.getCompany_id()!=null){
						string+=","+company.getCompany_id();
					}
				}
				if(!"".equals(string)&&string.length()>0){
					string=string.substring(1,string.length());
					companyDao.getComPanybyOffice_idandCompany_id(office1.getOffice_id(), string);
				}
				//修改或添加入驻公司
				for (Company company : companySet) {
					if(company.getOffice_company()!=null&&!company.getOffice_company().equals("")){
						Company company1=new Company();
						company1.setOffice_company(company.getOffice_company());
						company1.setOffice_floor_of_company(company.getOffice_floor_of_company());
						company1.setOffice_id(updateoffice.getOffice_id());
						if(company.getCompany_id()!=null){
							company1.setCompany_id(company.getCompany_id());
						}
						//Company companyByOffice_company = companyManager.getCompanyByOffice_company(company.getOffice_company(),company.getOffice_floor_of_company(),officeSele.getOffice_id());
						Company companyByOffice_company = companyDao.getCompany(company.getOffice_company(),company.getOffice_floor_of_company(),updateoffice.getOffice_id());
						if(companyByOffice_company==null){
							companyDao.saveOrUpdate(company1);
						}
					}
				}
			}else{
				Set<Company> set = companyManager.getComPanyByOfficeID(office1.getOffice_id());
				Iterator<Company> it = set.iterator();
				while(it.hasNext()){
					newsDao.deleteObject((Company)it.next());
				}
			}	 
			if(officeSele!=null&&!officeSele.getOffice_id().equals(office1.getOffice_id())){
			Set<Company> set = companyManager.getComPanyByOfficeID(officeSele.getOffice_id());
			Iterator<Company> it = set.iterator();
			while(it.hasNext()){
				newsDao.deleteObject((Company)it.next());
			}
			newsDao.deleteObject(officeSele);
			
		}
			
		}else{
			Office office=new Office();
			office.setOffice_address(office1.getOffice_address());
			office.setOffice_area(office1.getOffice_area());
			office.setOffice_floor(office1.getOffice_floor());
			office.setOffice_name(office1.getOffice_name());
			office.setOffice_parking(office1.getOffice_parking());
			office.setOffice_type(office1.getOffice_type());
			office.setVillage_id(office1.getVillage_id());
			office.setTown_id(office1.getTown_id());
			office.setEmployee_no(office1.getEmployee_no());
			if(officeSele==null){
				officeSele=new Office();
				officeSele.setCreate_user(office1.getCreate_user());
				officeSele.setCreate_user_id(office1.getCreate_user_id());
				officeSele.setCreate_time(new Date());
			}
			officeSele.setUpdate_user(office1.getCreate_user());
			officeSele.setUpdate_user_id(office1.getCreate_user_id());
			officeSele.setUpdate_time(new Date());
			BeanUtils.copyProperties(office, officeSele
					,new String[]{"office_id","create_user","create_user_id","create_time"
								,"update_user","update_user_id","update_time","employee_no"});
			if(officeSele.getEmployee_no()==null){
				officeSele.setEmployee_no(office1.getEmployee_no());
			}
			officeManager.saveObject(officeSele);
			
				Set<Company> set = companyManager.getComPanyByOfficeID(officeSele.getOffice_id());
				Iterator<Company> it = set.iterator();
				while(it.hasNext()){
					newsDao.deleteObject((Company)it.next());
				}
			Set<Company> companySet = office1.getCompany();
			if(companySet.size()>0){
				for (Company company : companySet) {
					if(company.getOffice_company()!=null&&!company.getOffice_company().equals("")){
						Company company1=new Company();
						company1.setOffice_company(company.getOffice_company());
						company1.setOffice_floor_of_company(company.getOffice_floor_of_company());
						company1.setOffice_id(officeSele.getOffice_id());
						Company companyByOffice_company = companyManager.getCompanyByOffice_company(company.getOffice_company(),company.getOffice_floor_of_company(),officeSele.getOffice_id());
						if(companyByOffice_company==null){
							//companyManager.save(company1);
							companyDao.saveOrUpdate(company1);
						}
					}
				}
			}
		}
		res.setCode(200);
		res.setMessage("添加成功！");
		return res;
	}

	@Override
	public Office getOfficeDataById(Integer id) {
		CompanyManager companyManager=(CompanyManager)SpringHelper.getBean("companyManager"); 
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("office_id",id));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0){
        	Office office=(Office)lst_vilage_data.get(0);
        	Set<Company> set = companyManager.getComPanyByOfficeID(office.getOffice_id());
        	office.setCompany(set);
        	if(office.getVillage_id()!=null&&!office.getVillage_id().equals("")){
				VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
				 Village idInfo = villageManager.getVillageByIdInfo(office.getVillage_id());
				 office.setVillage_name(idInfo.getName());
			}
        	
        	if(office.getJob()!=null&&!"".equals(office.getJob())){
	        	 CustomerManager customerManager=(CustomerManager)SpringHelper.getBean("customerManager");
	        	 String stts="";
	        	 String[] split = office.getJob().split(",");
	        	 if(split.length>0){
	        		 for (int i = 0; i < split.length; i++) {
	        			 String[] strings = split[i].split("-");
	        			 Customer customer = customerManager.findCustomerById(Long.parseLong(strings[0]));
	        			 if("请选择".equals(strings[2])){
	        				 stts +=","+customer.getName();
	        			 }else{
	        				 stts +=","+customer.getName()+"("+strings[2]+")";
	        			 }
	        			
	        		 }
	        	 } 
	        	 office.setShowSelectName(stts.substring(1,stts.length()));
	         }
        	
        	
           return office;
        }
        return null;
	}

	@Override
	public Office versionOfficeInfo(Office office) {
		return getOfficeByOffice_name_address(office.getOffice_name(),office.getVillage_id(),office.getOffice_address());
	}
	/**
	 * 根据当前登录的用户获取所有的城市id
	 */
	public String getCityId(String string){
		CityManager cityManager=(CityManager)SpringHelper.getBean("cityManager");
		String[] cityName = string.split(",");
		String cityId="";
		for(int i=0;i<cityName.length;i++){
			List<City> list = cityManager.getCityDataByName(cityName[i].replaceAll("'", ""));
			if(list!=null&&list.size()>0){
				for (City city : list) {
					cityId+=city.getId()+",";
				}
			}
		}
		return cityId;
	}
	  /**
     * 维护责任表
     * @param id
     * @param str 
     * @param type
     */
	public void updateOrinsertDuty(Long id,String str,Integer type,String employee_no){
		PersonDutyManager personDutyManager=(PersonDutyManager)SpringHelper.getBean("personDutyManager");
		String idString="";
		if(str!=null&&!"".equals(str)){
			String[] slit = str.split(",");
			if(slit.length>0){
				for (int i = 0; i < slit.length; i++) {
					String[] strings = slit[i].split("-");
					PersonDuty personDuty = personDutyManager.findPersonDutyBycustomerIdAnddutyIdAndtype(id, type,Long.parseLong(strings[0]));
					if(personDuty==null){
						personDuty=new PersonDuty();
					}
					personDuty.setDuty_id(id);
					personDuty.setType(type);
					personDuty.setCustomer_id(Long.parseLong(strings[0]));
					personDuty.setDuty("请选择".equals(strings[2])?null:strings[2]);
					personDuty.setEmployee_no(employee_no);
					preObject(personDuty);
					personDutyManager.saveObject(personDuty);
					idString+=","+strings[0];
				}
				
			}
		}
		if(idString.length()>0){
			idString=idString.substring(1,idString.length());
		}
		PersonDutyDao personDutyDao=(PersonDutyDao)SpringHelper.getBean(PersonDutyDao.class.getName());
		personDutyDao.deletePersonDuty(idString, type, id);
	}

	protected void preObject(Object o) {
		
			User sessionUser = null;
			if(null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User)SessionManager.getUserSession().getSessionData().get("user");
			}

			DataEntity commonEntity = (DataEntity)o;
			Date date = new Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			if(null == commonEntity.getCreate_time()) {
				commonEntity.setCreate_time(sdate);
				if(null != sessionUser) {
					commonEntity.setCreate_user_id(sessionUser.getId());
					commonEntity.setCreate_user(sessionUser.getName());
				}
			}

			commonEntity.setUpdate_time(sdate);
			if(null != sessionUser) {
				commonEntity.setUpdate_user_id(sessionUser.getId());
				commonEntity.setUpdate_user(sessionUser.getName());
			}
		

	}

}
