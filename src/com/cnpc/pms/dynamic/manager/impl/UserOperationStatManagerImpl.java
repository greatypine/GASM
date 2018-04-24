package com.cnpc.pms.dynamic.manager.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.dynamic.dao.UserOperationStatDao;
import com.cnpc.pms.dynamic.entity.MassOrderDto;
import com.cnpc.pms.dynamic.entity.UserOperationStatDto;
import com.cnpc.pms.dynamic.manager.UserOperationStatManager;
import com.cnpc.pms.platform.dao.OrderDao;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.PropertiesValueUtil;

public class UserOperationStatManagerImpl extends BizBaseCommonManager implements UserOperationStatManager {

	PropertiesValueUtil propertiesValueUtil = null;

    private XSSFCellStyle style_header = null;
    
    CellStyle cellStyle_common = null;
	
	@Override
	public Map<String, Object> queryNewCustomerStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo) {
		OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			result = orderDao.queryUserOpsNewCusStat(userOperationStatDto, pageInfo);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}
	
	public Map<String, Object> exportNewCustomerStat(UserOperationStatDto userOperationStatDto){
		OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
		
  		Map<String, Object> result = new HashMap<String,Object>();
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		try {
			list=orderDao.exportUserOpsNewCusStat(userOperationStatDto);
  		} catch (Exception e) {
  			e.printStackTrace();
  			return null;
  		}
  		if(list!=null&&list.size()>0){//成功返回数据
  			if(list.size()>50000){
  				result.put("message","导出条目过多，请重新筛选条件导出！");
  	  			result.put("status","more");
  	  			return result;
  			}
  			String str_file_dir_path = PropertiesUtil.getValue("file.root");
  			String str_web_path = PropertiesUtil.getValue("file.web.root");

  	        XSSFWorkbook wb = new XSSFWorkbook();   
  	        setCellStyle_common(wb);
  	        setHeaderStyle(wb);
  	        XSSFSheet sheet = wb.createSheet("用户运营消费用户（月）统计数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段 
  	        String[] str_headers = {"城市","门店	","片区","拉新用户","拉新用户超10元","拉新用户超20元"};
  	        String[] headers_key = {"city_name","store_name","area_name","new_count","new_10_count","new_20_count"};
  	       
  	        for(int i = 0;i < str_headers.length;i++){
  	            XSSFCell cell = row.createCell(i);
  	            cell.setCellStyle(getHeaderStyle());
  	            cell.setCellValue(new XSSFRichTextString(str_headers[i]));
  	        }
  	        
  	        for(int i = 0;i < list.size();i++){
  	        	 row = sheet.createRow(i+1);
  	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
  	            	setCellValueall(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
  	             }
  	        }

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_useropsnewcuslist.xlsx");
  			if(file_xls.exists()){
  				file_xls.delete();
  			}
  			FileOutputStream os = null;
  			try {
  				os = new FileOutputStream(file_xls.getAbsoluteFile());
  				wb.write(os);
  			}catch (Exception e) {
  				e.printStackTrace();
  			} finally {
  				if(os != null){
  					try {
  						os.close();
  					} catch (IOException e) {
  						e.printStackTrace();
  					}
  				}
  			}

  			result.put("message","导出成功！");
  			result.put("status","success");
  			result.put("data", str_web_path.concat(file_xls.getName()));
  		}else{
  			result.put("message","请重新操作！");
  			result.put("status","fail");
  		}
  		return result;
	}
	
	@Override
	public Map<String, Object> queryPayStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo) {
		OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			result = orderDao.queryUserOpsPayStat(userOperationStatDto, pageInfo);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}
	
	public Map<String, Object> exportPayStat(UserOperationStatDto userOperationStatDto){
		OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
		
  		Map<String, Object> result = new HashMap<String,Object>();
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		try {
			list=orderDao.exportUserOpsPayStat(userOperationStatDto);
  		} catch (Exception e) {
  			e.printStackTrace();
  			return null;
  		}
  		if(list!=null&&list.size()>0){//成功返回数据
  			if(list.size()>50000){
  				result.put("message","导出条目过多，请重新筛选条件导出！");
  	  			result.put("status","more");
  	  			return result;
  			}
  			String str_file_dir_path = PropertiesUtil.getValue("file.root");
  			String str_web_path = PropertiesUtil.getValue("file.web.root");

  	        XSSFWorkbook wb = new XSSFWorkbook();   
  	        setCellStyle_common(wb);
  	        setHeaderStyle(wb);
  	        XSSFSheet sheet = wb.createSheet("用户运营消费用户（月）统计数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段 
  	        String[] str_headers = {"城市","门店	","片区","消费用户","消费用户超10元","消费用户超20元"};
  	        String[] headers_key = {"city_name","store_name","area_name","pay_count","pay_10_count","pay_20_count"};
  	       
  	        for(int i = 0;i < str_headers.length;i++){
  	            XSSFCell cell = row.createCell(i);
  	            cell.setCellStyle(getHeaderStyle());
  	            cell.setCellValue(new XSSFRichTextString(str_headers[i]));
  	        }
  	        
  	        for(int i = 0;i < list.size();i++){
  	        	 row = sheet.createRow(i+1);
  	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
  	            	setCellValueall(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
  	             }
  	        }

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_useropspaylist.xlsx");
  			if(file_xls.exists()){
  				file_xls.delete();
  			}
  			FileOutputStream os = null;
  			try {
  				os = new FileOutputStream(file_xls.getAbsoluteFile());
  				wb.write(os);
  			}catch (Exception e) {
  				e.printStackTrace();
  			} finally {
  				if(os != null){
  					try {
  						os.close();
  					} catch (IOException e) {
  						e.printStackTrace();
  					}
  				}
  			}

  			result.put("message","导出成功！");
  			result.put("status","success");
  			result.put("data", str_web_path.concat(file_xls.getName()));
  		}else{
  			result.put("message","请重新操作！");
  			result.put("status","fail");
  		}
  		return result;
	}
	
	@Override
	public Map<String, Object> queryEmployeeMoreStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo){
		UserOperationStatDao userOperationStatDao = (UserOperationStatDao) SpringHelper.getBean(UserOperationStatDao.class.getName());
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(), format.format(new Date()))==0){
				result = userOperationStatDao.queryEmployeeMoreStat(userOperationStatDto, pageInfo, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(),preMonthFirst)>=0){
				result = userOperationStatDao.queryEmployeeMoreStat(userOperationStatDto, pageInfo, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				result = userOperationStatDao.queryEmployeeMoreStat(userOperationStatDto, pageInfo, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
			}
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}

	public Map<String, Object> exportEmployeeMoreStat(UserOperationStatDto userOperationStatDto){
		UserOperationStatDao userOperationStatDao = (UserOperationStatDao) SpringHelper.getBean(UserOperationStatDao.class.getName());
		
  		Map<String, Object> result = new HashMap<String,Object>();
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		try {
  			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(), format.format(new Date()))==0){
				list=userOperationStatDao.exportEmployeeMoreStat(userOperationStatDto, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(),preMonthFirst)>=0){
				list=userOperationStatDao.exportEmployeeMoreStat(userOperationStatDto, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				list=userOperationStatDao.exportEmployeeMoreStat(userOperationStatDto, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
			}
  		} catch (Exception e) {
  			e.printStackTrace();
  			return null;
  		}
  		if(list!=null&&list.size()>0){//成功返回数据
  			if(list.size()>50000){
  				result.put("message","导出条目过多，请重新筛选条件导出！");
  	  			result.put("status","more");
  	  			return result;
  			}
  			String str_file_dir_path = PropertiesUtil.getValue("file.root");
  			String str_web_path = PropertiesUtil.getValue("file.web.root");

  	        XSSFWorkbook wb = new XSSFWorkbook();   
  	        setCellStyle_common(wb);
  	        setHeaderStyle(wb);
  	        XSSFSheet sheet = wb.createSheet("用户运营1人500户统计数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段 
  	        String[] str_headers = {"城市","门店编号	","门店名称","国安侠编号","消费用户","新增消费用户"};
  	        String[] headers_key = {"city_name","store_code","store_name","employee_a_no","cus_count","new_cus_count"};
  	       
  	        for(int i = 0;i < str_headers.length;i++){
  	            XSSFCell cell = row.createCell(i);
  	            cell.setCellStyle(getHeaderStyle());
  	            cell.setCellValue(new XSSFRichTextString(str_headers[i]));
  	        }
  	        
  	        for(int i = 0;i < list.size();i++){
  	        	 row = sheet.createRow(i+1);
  	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
  	            	setCellValueall(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
  	             }
  	        }

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_useropsmorelist.xlsx");
  			if(file_xls.exists()){
  				file_xls.delete();
  			}
  			FileOutputStream os = null;
  			try {
  				os = new FileOutputStream(file_xls.getAbsoluteFile());
  				wb.write(os);
  			}catch (Exception e) {
  				e.printStackTrace();
  			} finally {
  				if(os != null){
  					try {
  						os.close();
  					} catch (IOException e) {
  						e.printStackTrace();
  					}
  				}
  			}

  			result.put("message","导出成功！");
  			result.put("status","success");
  			result.put("data", str_web_path.concat(file_xls.getName()));
  		}else{
  			result.put("message","请重新操作！");
  			result.put("status","fail");
  		}
  		return result;
	}
	
	public Map<String, Object> queryEmployeeAreamoreStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo){
		UserOperationStatDao userOperationStatDao = (UserOperationStatDao) SpringHelper.getBean(UserOperationStatDao.class.getName());
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(), format.format(new Date()))==0){
				result = userOperationStatDao.queryEmployeeAreamoreStat(userOperationStatDto, pageInfo, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(),preMonthFirst)>=0){
				result = userOperationStatDao.queryEmployeeAreamoreStat(userOperationStatDto, pageInfo, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				result = userOperationStatDao.queryEmployeeAreamoreStat(userOperationStatDto, pageInfo, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
			}
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}
	
	public Map<String, Object> exportEmployeeAreamoreStat(UserOperationStatDto userOperationStatDto){
		UserOperationStatDao userOperationStatDao = (UserOperationStatDao) SpringHelper.getBean(UserOperationStatDao.class.getName());
		
  		Map<String, Object> result = new HashMap<String,Object>();
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		try {
  			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(), format.format(new Date()))==0){
				list=userOperationStatDao.exportEmployeeAreamoreStat(userOperationStatDto, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(),preMonthFirst)>=0){
				list=userOperationStatDao.exportEmployeeAreamoreStat(userOperationStatDto, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				list=userOperationStatDao.exportEmployeeAreamoreStat(userOperationStatDto, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
			}
  		} catch (Exception e) {
  			e.printStackTrace();
  			return null;
  		}
  		if(list!=null&&list.size()>0){//成功返回数据
  			if(list.size()>50000){
  				result.put("message","导出条目过多，请重新筛选条件导出！");
  	  			result.put("status","more");
  	  			return result;
  			}
  			String str_file_dir_path = PropertiesUtil.getValue("file.root");
  			String str_web_path = PropertiesUtil.getValue("file.web.root");

  	        XSSFWorkbook wb = new XSSFWorkbook();   
  	        setCellStyle_common(wb);
  	        setHeaderStyle(wb);
  	        XSSFSheet sheet = wb.createSheet("用户运营1人500户统计数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段 
  	        String[] str_headers = {"城市","门店编号	","门店名称","片区编号","A国安侠编号","消费用户","新增消费用户"};
  	        String[] headers_key = {"city_name","store_code","store_name","area_code","employee_a_no","cus_count","new_cus_count"};
  	       
  	        for(int i = 0;i < str_headers.length;i++){
  	            XSSFCell cell = row.createCell(i);
  	            cell.setCellStyle(getHeaderStyle());
  	            cell.setCellValue(new XSSFRichTextString(str_headers[i]));
  	        }
  	        
  	        for(int i = 0;i < list.size();i++){
  	        	 row = sheet.createRow(i+1);
  	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
  	            	setCellValueall(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
  	             }
  	        }

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_useropsmorelist.xlsx");
  			if(file_xls.exists()){
  				file_xls.delete();
  			}
  			FileOutputStream os = null;
  			try {
  				os = new FileOutputStream(file_xls.getAbsoluteFile());
  				wb.write(os);
  			}catch (Exception e) {
  				e.printStackTrace();
  			} finally {
  				if(os != null){
  					try {
  						os.close();
  					} catch (IOException e) {
  						e.printStackTrace();
  					}
  				}
  			}

  			result.put("message","导出成功！");
  			result.put("status","success");
  			result.put("data", str_web_path.concat(file_xls.getName()));
  		}else{
  			result.put("message","请重新操作！");
  			result.put("status","fail");
  		}
  		return result;
	}
	
	public Map<String, Object> queryMassNewCusStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo){
		UserOperationStatDao userOperationStatDao = (UserOperationStatDao) SpringHelper.getBean(UserOperationStatDao.class.getName());
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(), format.format(new Date()))==0){
				result = userOperationStatDao.queryNewCusStat(userOperationStatDto, pageInfo, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(),preMonthFirst)>=0){
				result = userOperationStatDao.queryNewCusStat(userOperationStatDto, pageInfo, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				result = userOperationStatDao.queryNewCusStat(userOperationStatDto, pageInfo, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
			}
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}
	
	public Map<String, Object> exportMassNewCusStat(UserOperationStatDto userOperationStatDto){
		UserOperationStatDao userOperationStatDao = (UserOperationStatDao) SpringHelper.getBean(UserOperationStatDao.class.getName());
		
  		Map<String, Object> result = new HashMap<String,Object>();
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		try {
  			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(), format.format(new Date()))==0){
				list=userOperationStatDao.exportNewCusStat(userOperationStatDto, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(),preMonthFirst)>=0){
				list=userOperationStatDao.exportNewCusStat(userOperationStatDto, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				list=userOperationStatDao.exportNewCusStat(userOperationStatDto, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
			}
  		} catch (Exception e) {
  			e.printStackTrace();
  			return null;
  		}
  		if(list!=null&&list.size()>0){//成功返回数据
  			if(list.size()>50000){
  				result.put("message","导出条目过多，请重新筛选条件导出！");
  	  			result.put("status","more");
  	  			return result;
  			}
  			String str_file_dir_path = PropertiesUtil.getValue("file.root");
  			String str_web_path = PropertiesUtil.getValue("file.web.root");

  	        XSSFWorkbook wb = new XSSFWorkbook();   
  	        setCellStyle_common(wb);
  	        setHeaderStyle(wb);
  	        XSSFSheet sheet = wb.createSheet("用户运营拉新用户（日）统计数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段 
  	        String[] str_headers = {"城市","门店名称	","片区编号","拉新用户","拉新用户超10元","拉新用户超20元"};
  	        String[] headers_key = {"city_name","store_name","area_code","new_count","new_10_count","new_20_count"};
  	       
  	        for(int i = 0;i < str_headers.length;i++){
  	            XSSFCell cell = row.createCell(i);
  	            cell.setCellStyle(getHeaderStyle());
  	            cell.setCellValue(new XSSFRichTextString(str_headers[i]));
  	        }
  	        
  	        for(int i = 0;i < list.size();i++){
  	        	 row = sheet.createRow(i+1);
  	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
  	            	setCellValueall(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
  	             }
  	        }

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_massnewcuslist.xlsx");
  			if(file_xls.exists()){
  				file_xls.delete();
  			}
  			FileOutputStream os = null;
  			try {
  				os = new FileOutputStream(file_xls.getAbsoluteFile());
  				wb.write(os);
  			}catch (Exception e) {
  				e.printStackTrace();
  			} finally {
  				if(os != null){
  					try {
  						os.close();
  					} catch (IOException e) {
  						e.printStackTrace();
  					}
  				}
  			}

  			result.put("message","导出成功！");
  			result.put("status","success");
  			result.put("data", str_web_path.concat(file_xls.getName()));
  		}else{
  			result.put("message","请重新操作！");
  			result.put("status","fail");
  		}
  		return result;
	}
	
	public Map<String, Object> queryMassPayCusStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo){
		UserOperationStatDao userOperationStatDao = (UserOperationStatDao) SpringHelper.getBean(UserOperationStatDao.class.getName());
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(), format.format(new Date()))==0){
				result = userOperationStatDao.queryPayCusStat(userOperationStatDto, pageInfo, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(),preMonthFirst)>=0){
				result = userOperationStatDao.queryPayCusStat(userOperationStatDto, pageInfo, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				result = userOperationStatDao.queryPayCusStat(userOperationStatDto, pageInfo, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
			}
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}
	
	public Map<String, Object> exportMassPayCusStat(UserOperationStatDto userOperationStatDto){
		UserOperationStatDao userOperationStatDao = (UserOperationStatDao) SpringHelper.getBean(UserOperationStatDao.class.getName());
		
  		Map<String, Object> result = new HashMap<String,Object>();
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		try {
  			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(), format.format(new Date()))==0){
				list=userOperationStatDao.exportPayCusStat(userOperationStatDto, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(userOperationStatDto.getBeginDate()) && DateUtils.compareDate(userOperationStatDto.getBeginDate(),preMonthFirst)>=0){
				list=userOperationStatDao.exportPayCusStat(userOperationStatDto, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				list=userOperationStatDao.exportPayCusStat(userOperationStatDto, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
			}
  		} catch (Exception e) {
  			e.printStackTrace();
  			return null;
  		}
  		if(list!=null&&list.size()>0){//成功返回数据
  			if(list.size()>50000){
  				result.put("message","导出条目过多，请重新筛选条件导出！");
  	  			result.put("status","more");
  	  			return result;
  			}
  			String str_file_dir_path = PropertiesUtil.getValue("file.root");
  			String str_web_path = PropertiesUtil.getValue("file.web.root");

  	        XSSFWorkbook wb = new XSSFWorkbook();   
  	        setCellStyle_common(wb);
  	        setHeaderStyle(wb);
  	        XSSFSheet sheet = wb.createSheet("用户运营消费用户（日）统计数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段 
  	        String[] str_headers = {"城市","门店名称	","片区编号","消费用户","消费用户超10元","消费用户超20元"};
  	        String[] headers_key = {"city_name","store_name","area_code","pay_count","pay_10_count","pay_20_count"};
  	       
  	        for(int i = 0;i < str_headers.length;i++){
  	            XSSFCell cell = row.createCell(i);
  	            cell.setCellStyle(getHeaderStyle());
  	            cell.setCellValue(new XSSFRichTextString(str_headers[i]));
  	        }
  	        
  	        for(int i = 0;i < list.size();i++){
  	        	 row = sheet.createRow(i+1);
  	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
  	            	setCellValueall(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
  	             }
  	        }

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_masspaycuslist.xlsx");
  			if(file_xls.exists()){
  				file_xls.delete();
  			}
  			FileOutputStream os = null;
  			try {
  				os = new FileOutputStream(file_xls.getAbsoluteFile());
  				wb.write(os);
  			}catch (Exception e) {
  				e.printStackTrace();
  			} finally {
  				if(os != null){
  					try {
  						os.close();
  					} catch (IOException e) {
  						e.printStackTrace();
  					}
  				}
  			}

  			result.put("message","导出成功！");
  			result.put("status","success");
  			result.put("data", str_web_path.concat(file_xls.getName()));
  		}else{
  			result.put("message","请重新操作！");
  			result.put("status","fail");
  		}
  		return result;
	}
	
	@Override
	public Map<String, Object> queryAreaInfoByCode(String area_code){
		UserOperationStatDao userOperationStatDao = (UserOperationStatDao) SpringHelper.getBean(UserOperationStatDao.class.getName());
		Map<String,Object> order_obj = userOperationStatDao.queryAreaInfoByCode(area_code);
		return order_obj;
	}
		
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> queryCustomerStatBycity(String city_id){
		Map<String, Object> result = new HashMap<String,Object>();
		String curMonthFirst = DateUtils.getCurrMonthFirstDate("yyyyMM");
		String curWeekMonday = DateUtils.getCurrentMonday();//当周周一
		String curWeekSunday = DateUtils.getPreviousSunday();//当周周日
		UserOperationStatDao userOperationStatDao = (UserOperationStatDao) SpringHelper.getBean(UserOperationStatDao.class.getName());
		result = userOperationStatDao.queryCustomerStatBycity(city_id, curMonthFirst);
		return result;
	}
	
	@Override
	public Map<String, Object> queryPayBasicStat(UserOperationStatDto userOperationStatDto){
		UserOperationStatDao userOperationStatDao = (UserOperationStatDao) SpringHelper.getBean(UserOperationStatDao.class.getName());
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			result = userOperationStatDao.queryPayBasicStat(userOperationStatDto);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> exportPayBasicStat(UserOperationStatDto userOperationStatDto){
		UserOperationStatDao userOperationStatDao = (UserOperationStatDao) SpringHelper.getBean(UserOperationStatDao.class.getName());
		Map<String, Object> result = new HashMap<String,Object>();
		Map<String, Object> queryresult =new HashMap<String,Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			queryresult = userOperationStatDao.queryPayBasicStat(userOperationStatDto);
			list = (List<Map<String, Object>>) queryresult.get("comparelist");
		} catch (Exception e) {
			e.printStackTrace();
  			return null;
		}
		if(list!=null&&list.size()>0){//成功返回数据
  			if(list.size()>50000){
  				result.put("message","导出条目过多，请重新筛选条件导出！");
  	  			result.put("status","more");
  	  			return result;
  			}
  			String str_file_dir_path = PropertiesUtil.getValue("file.root");
  			String str_web_path = PropertiesUtil.getValue("file.web.root");

  	        XSSFWorkbook wb = new XSSFWorkbook();   
  	        setCellStyle_common(wb);
  	        setHeaderStyle(wb);
  	        XSSFSheet sheet = wb.createSheet("用户留存分析数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段 
  	        String[] str_headers = {"基准月","基准月下单金额","基准月客户数","对比日期","对比月下单日期","对比月客户数","留存率"};
  	        String[] headers_key = {"order_ym","trading_price","customers","order_ym","trading_price","customers","compare_data"};
  	       
  	        for(int i = 0;i < str_headers.length;i++){
  	            XSSFCell cell = row.createCell(i);
  	            cell.setCellStyle(getHeaderStyle());
  	            cell.setCellValue(new XSSFRichTextString(str_headers[i]));
  	        }
  	        
  	        for(int i = 0;i < list.size();i++){
  	        	 row = sheet.createRow(i+1);
  	        	 BigDecimal customers = BigDecimal.ZERO;
  	        	 BigDecimal comparecus = BigDecimal.ZERO;
  	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					if (cellIndex < 3) {
						if(cellIndex==2){
							customers = new BigDecimal(queryresult.get(headers_key[cellIndex]).toString());
						}
						setCellValueall(row, cellIndex, queryresult.get(headers_key[cellIndex]));
  	            	}else if(cellIndex>=3 && cellIndex!=headers_key.length-1){
  	            		if(cellIndex==headers_key.length-2){
  	            			comparecus = new BigDecimal(list.get(i).get(headers_key[cellIndex]).toString());
						}
  	            		setCellValueall(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
  	            	}else{
  	            		NumberFormat percent = NumberFormat.getPercentInstance();     //建立百分比格式化用  
  	            		percent.setMaximumFractionDigits(2);
  	            		setCellValueall(row, cellIndex, percent.format(comparecus.divide(customers, 6, BigDecimal.ROUND_HALF_UP)));
  	            	}
  	             }
  	        }

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_comparelist.xlsx");
  			if(file_xls.exists()){
  				file_xls.delete();
  			}
  			FileOutputStream os = null;
  			try {
  				os = new FileOutputStream(file_xls.getAbsoluteFile());
  				wb.write(os);
  			}catch (Exception e) {
  				e.printStackTrace();
  			} finally {
  				if(os != null){
  					try {
  						os.close();
  					} catch (IOException e) {
  						e.printStackTrace();
  					}
  				}
  			}

  			result.put("message","导出成功！");
  			result.put("status","success");
  			result.put("data", str_web_path.concat(file_xls.getName()));
  		}else{
  			result.put("message","请重新操作！");
  			result.put("status","fail");
  		}
  		return result;
	}
	
	private XSSFCellStyle getHeaderStyle(){
		return style_header;
	}

	private void setHeaderStyle(XSSFWorkbook wb){

		// 创建单元格样式
		style_header = wb.createCellStyle();
		style_header.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style_header.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		style_header.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		style_header.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

		// 设置边框
		//style_header.setBottomBorderColor(HSSFColor.BLACK.index);
		style_header.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style_header.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style_header.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style_header.setBorderTop(XSSFCellStyle.BORDER_THIN);

	}

	public void setCellValueall(Row obj_row, int nCellIndex, Object value){
		Cell cell=obj_row.createCell(nCellIndex);
		cell.setCellStyle(getCellStyle_common());
		cell.setCellValue(new XSSFRichTextString(value==null?null:value.toString()));
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
	}
	
	public CellStyle getCellStyle_common(){
        return cellStyle_common;
    }
    
    public void setCellStyle_common(Workbook wb){
        cellStyle_common=wb.createCellStyle();
        cellStyle_common.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle_common.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle_common.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle_common.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle_common.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        cellStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);//垂直居中
        cellStyle_common.setWrapText(true);//设置自动换行
    }
	
}
