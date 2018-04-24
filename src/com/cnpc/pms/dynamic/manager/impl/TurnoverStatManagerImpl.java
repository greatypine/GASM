package com.cnpc.pms.dynamic.manager.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.cnpc.pms.dynamic.dao.TurnoverStatDao;
import com.cnpc.pms.dynamic.entity.MassOrderDto;
import com.cnpc.pms.dynamic.entity.TurnoverStatDto;
import com.cnpc.pms.dynamic.manager.TurnoverStatManager;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.PropertiesValueUtil;

public class TurnoverStatManagerImpl  extends BizBaseCommonManager implements TurnoverStatManager {
	
	PropertiesValueUtil propertiesValueUtil = null;

    private XSSFCellStyle style_header = null;
    
    CellStyle cellStyle_common = null;

	public Map<String, Object> queryStoreStat(TurnoverStatDto storeStatDto,PageInfo pageInfo){
		TurnoverStatDao turnoverStatDao = (TurnoverStatDao)SpringHelper.getBean(TurnoverStatDao.class.getName());
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(), format.format(new Date()))==0){
				result = turnoverStatDao.queryStoreStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(),preMonthFirst)>=0){
				result = turnoverStatDao.queryStoreStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				result = turnoverStatDao.queryStoreStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
			}
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		
		return result;
	}
	
	public Map<String, Object> exportStoreStat(TurnoverStatDto storeStatDto){
		TurnoverStatDao turnoverStatDao = (TurnoverStatDao)SpringHelper.getBean(TurnoverStatDao.class.getName());
		
  		Map<String, Object> result = new HashMap<String,Object>();
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		try {
  			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(), format.format(new Date()))==0){
				list=turnoverStatDao.exportStoreStat(storeStatDto, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(),preMonthFirst)>=0){
				list=turnoverStatDao.exportStoreStat(storeStatDto, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				list=turnoverStatDao.exportStoreStat(storeStatDto, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
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
  	        XSSFSheet sheet = wb.createSheet("门店营业额统计数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段 
  	        String[] str_headers = {"城市","门店	","交易额","交易额（去除快周边）","退货交易额","退货交易额（去除快周边）","订单量","订单量（去除快周边）","退货量","退货量（去除快周边）"};
  	        String[] headers_key = {"city_name","store_name","gmv_price","gmv_price_profit","return_price","return_price_profit","order_num","order_num_profit","return_num","return_num_profit"};
  	       
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

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_turnoverstorelist.xlsx");
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
	
	public Map<String, Object> queryAreaStat(TurnoverStatDto storeStatDto,PageInfo pageInfo){
		TurnoverStatDao turnoverStatDao = (TurnoverStatDao)SpringHelper.getBean(TurnoverStatDao.class.getName());
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(), format.format(new Date()))==0){
				result = turnoverStatDao.queryAreaStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(),preMonthFirst)>=0){
				result = turnoverStatDao.queryAreaStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				result = turnoverStatDao.queryAreaStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
			}
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		
		return result;
	}
	
	public Map<String, Object> exportAreaStat(TurnoverStatDto storeStatDto){
		TurnoverStatDao turnoverStatDao = (TurnoverStatDao)SpringHelper.getBean(TurnoverStatDao.class.getName());
		
  		Map<String, Object> result = new HashMap<String,Object>();
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		try {
  			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(), format.format(new Date()))==0){
				list=turnoverStatDao.exportAreaStat(storeStatDto, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(),preMonthFirst)>=0){
				list=turnoverStatDao.exportAreaStat(storeStatDto, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				list=turnoverStatDao.exportAreaStat(storeStatDto, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
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
  	        XSSFSheet sheet = wb.createSheet("片区营业额统计数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段 
  	        String[] str_headers = {"城市","门店	","片区编号","国安A侠编号","交易额","交易额（去除快周边）","退货交易额","退货交易额（去除快周边）","订单量","订单量（去除快周边）","退货量","退货量（去除快周边）"};
  	        String[] headers_key = {"city_name","store_name","area_code","employee_a_no","gmv_price","gmv_price_profit","return_price","return_price_profit","order_num","order_num_profit","return_num","return_num_profit"};
  	       
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

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_turnoverarealist.xlsx");
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
	
	public Map<String, Object> queryDeptStat(TurnoverStatDto storeStatDto,PageInfo pageInfo){
		TurnoverStatDao turnoverStatDao = (TurnoverStatDao)SpringHelper.getBean(TurnoverStatDao.class.getName());
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(), format.format(new Date()))==0){
				result = turnoverStatDao.queryDeptStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(),preMonthFirst)>=0){
				result = turnoverStatDao.queryDeptStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				result = turnoverStatDao.queryDeptStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
			}
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		
		return result;
	}
	
	public Map<String, Object> exportDeptStat(TurnoverStatDto storeStatDto){
		TurnoverStatDao turnoverStatDao = (TurnoverStatDao)SpringHelper.getBean(TurnoverStatDao.class.getName());
		
  		Map<String, Object> result = new HashMap<String,Object>();
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		try {
  			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(), format.format(new Date()))==0){
				list=turnoverStatDao.exportDeptStat(storeStatDto, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(),preMonthFirst)>=0){
				list=turnoverStatDao.exportDeptStat(storeStatDto, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				list=turnoverStatDao.exportDeptStat(storeStatDto, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
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
  	        XSSFSheet sheet = wb.createSheet("事业群营业额统计数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段 
  	        String[] str_headers = {"城市","门店	","事业群","交易额","退货交易额","订单量","退货量"};
  	        String[] headers_key = {"city_name","store_name","department_name","gmv_price","return_price","order_num","return_num"};
  	       
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

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_turnoverarealist.xlsx");
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
	
	public Map<String, Object> queryChannelStat(TurnoverStatDto storeStatDto,PageInfo pageInfo){
		TurnoverStatDao turnoverStatDao = (TurnoverStatDao)SpringHelper.getBean(TurnoverStatDao.class.getName());
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(), format.format(new Date()))==0){
				result = turnoverStatDao.queryChannelStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(),preMonthFirst)>=0){
				result = turnoverStatDao.queryChannelStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				result = turnoverStatDao.queryChannelStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
			}
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		
		return result;
	}
	
	public Map<String, Object> exportChannelStat(TurnoverStatDto storeStatDto){
		TurnoverStatDao turnoverStatDao = (TurnoverStatDao)SpringHelper.getBean(TurnoverStatDao.class.getName());
		
  		Map<String, Object> result = new HashMap<String,Object>();
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		try {
  			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(), format.format(new Date()))==0){
				list=turnoverStatDao.exportChannelStat(storeStatDto, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(),preMonthFirst)>=0){
				list=turnoverStatDao.exportChannelStat(storeStatDto, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				list=turnoverStatDao.exportChannelStat(storeStatDto, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
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
  	        XSSFSheet sheet = wb.createSheet("频道营业额统计数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段 
  	        String[] str_headers = {"城市","门店	","频道","交易额","退货交易额","订单量","退货量"};
  	        String[] headers_key = {"city_name","store_name","channel_name","gmv_price","return_price","order_num","return_num"};
  	       
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

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_turnoverchannellist.xlsx");
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
	
	public Map<String, Object> queryEshopStat(TurnoverStatDto storeStatDto,PageInfo pageInfo){
		TurnoverStatDao dynamicDataStatDao = (TurnoverStatDao)SpringHelper.getBean(TurnoverStatDao.class.getName());
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(), format.format(new Date()))==0){
				result = dynamicDataStatDao.queryEshopStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(),preMonthFirst)>=0){
				result = dynamicDataStatDao.queryEshopStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				result = dynamicDataStatDao.queryEshopStat(storeStatDto, pageInfo, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
			}
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		
		return result;
	}
	
	public Map<String, Object> exportEshopStat(TurnoverStatDto storeStatDto){
		TurnoverStatDao turnoverStatDao = (TurnoverStatDao)SpringHelper.getBean(TurnoverStatDao.class.getName());
		
  		Map<String, Object> result = new HashMap<String,Object>();
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		try {
  			String preMonthFirst = DateUtils.getPreMonthFirstDay(new Date()); //上月1号
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(), format.format(new Date()))==0){
				list=turnoverStatDao.exportEshopStat(storeStatDto, MassOrderDto.TimeFlag.CUR_DAY.code);
			}else if(StringUtils.isNotEmpty(storeStatDto.getBeginDate()) && DateUtils.compareDate(storeStatDto.getBeginDate(),preMonthFirst)>=0){
				list=turnoverStatDao.exportEshopStat(storeStatDto, MassOrderDto.TimeFlag.LATEST_MONTH.code);
			}else{
				list=turnoverStatDao.exportEshopStat(storeStatDto, MassOrderDto.TimeFlag.HISTORY_MONTH.code);
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
  	        XSSFSheet sheet = wb.createSheet("E店营业额统计数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段 
  	        String[] str_headers = {"城市","门店	","E店","交易额","退货交易额","订单量","退货量"};
  	        String[] headers_key = {"city_name","store_name","eshop_name","gmv_price","return_price","order_num","return_num"};
  	       
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

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_turnovereshoplist.xlsx");
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
	
	public Map<String, Object> queryAreaByCode(String area_code){
		TurnoverStatDao turnoverStatDao = (TurnoverStatDao)SpringHelper.getBean(TurnoverStatDao.class.getName());
    	
    	Map<String,Object> order_obj = turnoverStatDao.queryAreaByCode(area_code);
    	
    	return order_obj;
	}
	
	public Map<String, Object> queryEmployeeByNO(String employee_no){
		TurnoverStatDao turnoverStatDao = (TurnoverStatDao)SpringHelper.getBean(TurnoverStatDao.class.getName());
    	
    	Map<String,Object> order_obj = turnoverStatDao.queryEmployeeByNO(employee_no);
    	
    	return order_obj;
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
