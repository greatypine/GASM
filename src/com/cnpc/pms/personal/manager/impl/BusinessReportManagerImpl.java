package com.cnpc.pms.personal.manager.impl;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.util.FileCopyUtils;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.dynamic.manager.DynamicManager;
import com.cnpc.pms.personal.dao.ChartStatDao;
import com.cnpc.pms.personal.dao.ExpressDao;
import com.cnpc.pms.personal.dao.HouseStyleDao;
import com.cnpc.pms.personal.dao.HumanenteachDao;
import com.cnpc.pms.personal.dao.HumanresourcesDao;
import com.cnpc.pms.personal.dao.ImportHumanresourcesDao;
import com.cnpc.pms.personal.dao.PublicOrderDao;
import com.cnpc.pms.personal.dao.RelationDao;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.dao.YyMicrDataDao;
import com.cnpc.pms.personal.dto.ChartStatDto;
import com.cnpc.pms.personal.entity.Company;
import com.cnpc.pms.personal.entity.DataHumanType;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.DistCityCode;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.Humanenteach;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.HumanresourcesChange;
import com.cnpc.pms.personal.entity.HumanresourcesLog;
import com.cnpc.pms.personal.entity.ImportHumanresources;
import com.cnpc.pms.personal.entity.OuterCompany;
import com.cnpc.pms.personal.entity.PublicOrder;
import com.cnpc.pms.personal.entity.ScoreRecordTotal;
import com.cnpc.pms.personal.entity.SelfExpress;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.SysUserGroupOpera;
import com.cnpc.pms.personal.entity.XxExpress;
import com.cnpc.pms.personal.manager.BuildingManager;
import com.cnpc.pms.personal.manager.BusinessReportManager;
import com.cnpc.pms.personal.manager.CompanyManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.DistCityCodeManager;
import com.cnpc.pms.personal.manager.DistCityManager;
import com.cnpc.pms.personal.manager.DsAbnormalOrderManager;
import com.cnpc.pms.personal.manager.ExpressManager;
import com.cnpc.pms.personal.manager.HouseManager;
import com.cnpc.pms.personal.manager.HumanenteachManager;
import com.cnpc.pms.personal.manager.HumanresourcesChangeManager;
import com.cnpc.pms.personal.manager.HumanresourcesLogManager;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
import com.cnpc.pms.personal.manager.ImportHumanresourcesManager;
import com.cnpc.pms.personal.manager.OuterCompanyManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.SysUserGroupOperaManager;
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.platform.dao.OrderDao;
import com.cnpc.pms.platform.entity.PlatformEmployee;
import com.cnpc.pms.platform.manager.OrderManager;
import com.cnpc.pms.platform.manager.PlatformEmployeeManager;
import com.cnpc.pms.slice.manager.AreaManager;
import com.cnpc.pms.utils.ChineseToEnglish;
import com.cnpc.pms.utils.CompressFile;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.EntityEquals;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;
import com.google.gson.JsonObject;

@SuppressWarnings("all")
public class BusinessReportManagerImpl extends BizBaseCommonManager implements BusinessReportManager {
	
	@Override
	public List<Map<String, Object>> queryTurnoverByMonth(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryTurnoverByMonth(csd);
    	return lst_data;
	}
	
	@Override
	public List<Map<String, Object>> queryTargetByMonth(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryTargetByMonth(csd);
    	return lst_data;
	}
	
	@Override
	public List<Map<String, Object>> queryTurnoverByWeek(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryTurnoverByWeek(csd);
    	return lst_data;
	}
	
	@Override
	public List<String> getDateByWeek(){
		return DateUtils.getDateByWeek();
	}
	


	@Override
	public List<Map<String, Object>> queryContainsStoreDistCityList(){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryContainsStoreDistCityList();
    	return lst_data;
	}
	
	/*@Override
	public List<Map<String, Object>> queryAllDept(){
		OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		List<Map<String, Object>> lst_data = orderDao.queryAllDept();
    	return lst_data;
	}
	
	@Override
	public List<Map<String, Object>> queryAllChannel(){
		OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		List<Map<String, Object>> lst_data = orderDao.queryAllChannel();
    	return lst_data;
	}
	
	@Override
	public List<Map<String, Object>> findChannelByDept(String deptId){
		OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		List<Map<String, Object>> lst_data = orderDao.findChannelByDept(deptId);
    	return lst_data;
	}*/
	
	@Override
	public Map<String, Object> queryDayTurnover(ChartStatDto csd) {
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		Map<String,Object> order_obj = chartStatDao.queryDayTurnover(csd);
		return order_obj;
	}
	
	@Override
	public Map<String, Object> queryMonthTurnover(ChartStatDto csd) {
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		Map<String,Object> order_obj = chartStatDao.queryMonthTurnover(csd);
    	return order_obj;
	}
	
	@Override
	public Map<String, Object> queryOnlineOfflineTurnover(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		Map<String,Object> order_obj = chartStatDao.queryOnlineOfflineTurnover(csd);
    	return order_obj;
	}
	
	@Override
	public List<Map<String, Object>> queryTurnoverByHour(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryTurnoverByHour(csd);
    	return lst_data;
	}
	
	@Override
	public List<Map<String, Object>> queryTurnoverByDay(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryTurnoverByDay(csd);
    	return lst_data;
	}
	
	@Override
	public List<Map<String, Object>> queryDataOfScatterplot(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryDataOfScatterplot(csd);
    	return lst_data;
	}
	
	@Override
	public Map<String, Object> queryDayUser(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		Map<String,Object> order_obj = chartStatDao.queryDayUser(csd);
		return order_obj;
	}
	
	@Override
	public List<Map<String, Object>> queryUserByHour(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryUserByHour(csd);
    	return lst_data;
	}
	
	@Override
	public List<Map<String, Object>> queryUserByDay(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryUserByDay(csd);
    	return lst_data;
	}
	
	@Override
	public List<Map<String, Object>> queryUserByWeek(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryUserByWeek(csd);
    	return lst_data;
	}
	@Override
	public List<Map<String, Object>> queryUserByMonth(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryUserByMonth(csd);
    	return lst_data;
	}
	
	@Override
	public Map<String, Object> queryOnlineOfflineUser(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		Map<String,Object> order_obj = chartStatDao.queryOnlineOfflineUser(csd);
    	return order_obj;
	}

	@Override
	public List<Map<String, Object>> querUserOfScatterplot(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.querUserOfScatterplot(csd);
		return lst_data;
	}

	@Override
	public Map<String, Object> queryCurMonthUser(ChartStatDto csd){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		Map<String,Object> order_obj = chartStatDao.queryCurMonthUser(csd);
		return order_obj;
	}
}
