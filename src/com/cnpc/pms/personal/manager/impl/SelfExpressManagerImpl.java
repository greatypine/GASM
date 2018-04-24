package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.*;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.entity.SelfExpress;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.SelfExpressManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.utils.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 快递业务实现类
 * Created by liuxiao on 2016/6/15 0015.
 */
public class SelfExpressManagerImpl extends BizBaseCommonManager implements SelfExpressManager {

    /**
     * 解析Excel导入自提数据
     * @param lst_import_excel 自提Excel
     */
    @Override
    public void importSelfExpressForExcel(List<File> lst_import_excel) throws Exception {
        for(File file_excel : lst_import_excel) {
        	///////处理文件名开始/////
        	Long store_id=null;
        	//查找登录人
        	UserManager manager = (UserManager)SpringHelper.getBean("userManager");
    		UserDTO userDTO = manager.getCurrentUserDTO();
    		if(userDTO.getStore_id()!=null){
    			store_id = userDTO.getStore_id();
    		}else{
    			///////处理文件名开始/////
            	String file_name = file_excel.getName();
            	if(file_name!=null&&file_name.length()>0){
            		try {
						file_name=file_name.split("-")[1];
					} catch (Exception e) {
						e.printStackTrace();
					}
            	}
            	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
            	Store store = storeManager.findStoreByName(file_name);
            	store_id=store==null?null:store.getStore_id();
    		}
        	///////处理文件名结束/////
        	
            //读取excel文件
            InputStream is_excel = new FileInputStream(file_excel);
            //Excel工作簿
            Workbook wb_excel;
            Sheet sheet_data;
            try {
                //解析2003的xls模式的excel
                wb_excel = new HSSFWorkbook(is_excel);
            } catch (Exception e) {
                //如果2003模式解析异常，尝试解析2007模式
                wb_excel = new XSSFWorkbook(file_excel.getAbsolutePath());
            }
            int sheetCount = wb_excel.getNumberOfSheets();
            
            //临时保存国安侠姓名，ID
            Map<String, Object> employMap = new HashMap<String, Object>();
            
            for(int i = 0; i < sheetCount;i++){
                sheet_data = wb_excel.getSheetAt(i);
                for(int nRowIndex = 1;nRowIndex < sheet_data.getPhysicalNumberOfRows();nRowIndex++){
                    Row row_express = sheet_data.getRow(nRowIndex+1);
                    if(row_express == null){
                        continue;
                    }
                    String express_no = getCellValue(row_express.getCell(3));
                    if(express_no == null || "".equals(express_no)){
                        continue;
                    }
                    List<?> lst_express_object = this.getList(FilterFactory.getSimpleFilter("express_no",express_no));
                    SelfExpress obj_express;
                    if(lst_express_object != null && lst_express_object.size() > 0){
                        obj_express = (SelfExpress)lst_express_object.get(0);
                    }else{
                        obj_express = new SelfExpress();
                    }
                    
                    //收件日期
                    String str_express_date = getCellValue(row_express.getCell(0));
                    if(str_express_date == null || "".equals(str_express_date)){
                        obj_express.setExpress_date(null);
                    }else{
                    	String format_express_date = formatDate(str_express_date);
                    	obj_express.setExpress_date(DateUtils.str_to_date(format_express_date.trim(), "yyyy-MM-dd"));
                    }
                    
                    //签收日期
                    String str_take_express_date = getCellValue(row_express.getCell(1));
                    if(str_take_express_date == null || "".equals(str_take_express_date)){
                        obj_express.setTake_express_date(null);
                    }else{
                    	String format_express_date = formatDate(str_take_express_date);
                    	obj_express.setTake_express_date(DateUtils.str_to_date(format_express_date.trim(),"yyyy-MM-dd"));
                    }
                    
                    
                    obj_express.setExpress_no(express_no);
                    obj_express.setExpress_company(getCellValue(row_express.getCell(2)));
                    obj_express.setRcv_name(getCellValue(row_express.getCell(4)));
                    obj_express.setRcv_tel(getCellValue(row_express.getCell(5)));
                    obj_express.setRcv_address(getCellValue(row_express.getCell(6)));
                    obj_express.setObj_name(getCellValue(row_express.getCell(7)));
                    obj_express.setRemark(getCellValue(row_express.getCell(8)));
                    
                    obj_express.setStore_id(store_id);
                    preObject(obj_express);
                    obj_express.setStatus(0);
                    saveObject(obj_express);
                }
            }
            is_excel.close();
        }
    }
    
    
    /**
     * 导入excel模板数据，只含 运单号 提货时间 订单来源	和 物流公司

     * @param lst_import_excel
     * @throws Exception
     */
    @Override
    public void importSelfExpressTemplateForExcel(List<File> lst_import_excel) throws Exception {
        for(File file_excel : lst_import_excel) {
        	///////取得当前登录人门店 /////
        	UserManager manager = (UserManager)SpringHelper.getBean("userManager");
    		UserDTO userDTO = manager.getCurrentUserDTO();
    		Long store_id = userDTO.getStore_id();
        	///////取得当前登录人门店/////
        	
            //读取excel文件
            InputStream is_excel = new FileInputStream(file_excel);
            //Excel工作簿
            Workbook wb_excel;
            Sheet sheet_data;
            try {
                //解析2003的xls模式的excel
                wb_excel = new HSSFWorkbook(is_excel);
            } catch (Exception e) {
                //如果2003模式解析异常，尝试解析2007模式
                wb_excel = new XSSFWorkbook(file_excel.getAbsolutePath());
            }
            int sheetCount = wb_excel.getNumberOfSheets();
            
            //临时保存国安侠姓名，ID
            Map<String, Object> employMap = new HashMap<String, Object>();
            
            for(int i = 0; i < sheetCount;i++){
                sheet_data = wb_excel.getSheetAt(i);
                for(int nRowIndex = 1;nRowIndex < sheet_data.getPhysicalNumberOfRows();nRowIndex++){
                    Row row_express = sheet_data.getRow(nRowIndex+1);
                    if(row_express == null){
                        continue;
                    }
                    //快递号
                    String express_no = getCellValue(row_express.getCell(0));
                    if(express_no == null || "".equals(express_no)){
                        continue;
                    }
                    List<?> lst_express_object = this.getList(FilterFactory.getSimpleFilter("express_no",express_no));
                    SelfExpress obj_express;
                    if(lst_express_object != null && lst_express_object.size() > 0){
                        obj_express = (SelfExpress)lst_express_object.get(0);
                    }else{
                        obj_express = new SelfExpress();
                    }
                    /*
                    //收件日期
                    String str_express_date = getCellValue(row_express.getCell(0));
                    if(str_express_date == null || "".equals(str_express_date)){
                        obj_express.setExpress_date(null);
                    }else{
                    	String format_express_date = formatDate(str_express_date);
                    	obj_express.setExpress_date(DateUtils.str_to_date(format_express_date.trim(), "yyyy-MM-dd"));
                    }*/
                    //签收日期
                    String str_take_express_date = getCellValue(row_express.getCell(1));
                    if(str_take_express_date == null || "".equals(str_take_express_date)){
                        obj_express.setTake_express_date(null);
                    }else{
                    	String format_express_date = formatDate(str_take_express_date);
                    	obj_express.setTake_express_date(DateUtils.str_to_date(format_express_date.trim(),"yyyy-MM-dd"));
                    }
                    obj_express.setExpress_no(express_no);
                    obj_express.setExpress_company(getCellValue(row_express.getCell(3)));
                    obj_express.setRemark(getCellValue(row_express.getCell(2)));
                    obj_express.setStore_id(store_id);
                    preObject(obj_express);
                    obj_express.setStatus(2);//此状态为导入的
                    saveObject(obj_express);
                }
            }
            is_excel.close();
        }
    }

    private String getCellValue(Cell cell) {
        String value;
        if(cell == null){
            return null;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString().trim();
                break;
            case Cell.CELL_TYPE_NUMERIC:
            	value = cell.getNumericCellValue() == 0D ? null : String.valueOf(cell.getNumericCellValue());
            	if (cell.getCellStyle().getDataFormat() == 177||cell.getCellStyle().getDataFormat() == 58||cell.getCellStyle().getDataFormat() == 31){
                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                     Date date = org.apache.poi.ss.usermodel.DateUtil
                             .getJavaDate(Double.valueOf(value));
                     value = sdf.format(date);
                     return value;
                 }else if(DateUtil.isCellDateFormatted(cell)){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                }
                if(value != null && value.contains("E")){
                    value = new BigDecimal(value).toPlainString();
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue()).trim();
                break;
            case Cell.CELL_TYPE_FORMULA:
                value = cell.getCellFormula();
                break;
            default:
                value = "";
        }
        return value;
    }
    
    /**
     * 格式化日期 将日期格式处理为 yyyy-MM-dd
     * @param str
     * @return
     */
    public String formatDate(String str){
		Calendar calendar=Calendar.getInstance();
    	str = str.replaceAll("[^0-9\\s]","-");
		str = str.endsWith("-")?str.substring(0,str.length()-1):str;
		Pattern pat = Pattern.compile("\\d{1,2}-\\d{1,2}");
		Matcher matcher = pat.matcher(str);
		if(matcher.matches()){
			str = calendar.get(calendar.YEAR)+"-" + str;
		}
		return str;
    }
    
    
    /**
     * 保存菜鸟驿站信息 
     */
    @Override
    public SelfExpress saveSelfExpress(SelfExpress selfExpress){
    	UserManager manager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = manager.getCurrentUserDTO();
		Long store_id = userDTO.getStore_id();
		if(selfExpress.getStore_id()!=null){
			selfExpress.setStore_id(selfExpress.getStore_id());
		}else{
			selfExpress.setStore_id(store_id);
		}
    	preSaveObject(selfExpress);
    	selfExpress.setStatus(0);
    	save(selfExpress);
    	return selfExpress;
    }
    
    /**
     * 根据ID 查询一条信息
     * @param id
     * @return
     */
    @Override
    public SelfExpress querySelfExpressById(Long id){
    	SelfExpress selfExpress=(SelfExpress) this.getObject(id); 
    	return selfExpress;
    }
    
    @Override
    public SelfExpress querySelfExpressByNext(SelfExpress selfExpress){
    	UserManager manager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = manager.getCurrentUserDTO();
		Long store_id = userDTO.getStore_id();
		
    	StringBuffer fiterString  = new StringBuffer();
		fiterString.append(" store_id="+store_id+" and status=2 ");
		
    	IFilter filter = FilterFactory.getSimpleFilter(fiterString.toString());
    	List<?> sliList = this.getList(filter);
    	if(sliList!=null&&sliList.size()>0){
    		selfExpress = (SelfExpress)sliList.get(0);
    		selfExpress.setSelf_express_count(sliList.size());
    		return selfExpress;
    	}
    	return selfExpress;
    }
    
    
    /**
     * 判断是否存在重复的菜鸟驿站快递号 
     */
    @Override
    public int querySelfExpressByExpressNo(SelfExpress selfExpress){
        List<?> lst_express_object = this.getList(FilterFactory.getSimpleFilter(" express_no ="+selfExpress.getExpress_no()+" and status!=1 "));
        if(lst_express_object!=null&&lst_express_object.size()!=0){
        	boolean flag = false;
        	for(Object o:lst_express_object){
        		SelfExpress exp=(SelfExpress) o;
        		if(!exp.getId().equals(selfExpress.getId())){
        			flag=true;
        		}
        	}
        	if(flag){
        		return 1;
        	}else{
        		return 0;
        	}
        }else{
        	return 0;
        }
    }
    
    
    @Override
    public Map<String, Object> querySelfExpressList(QueryConditions condition) {
    	UserManager manager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = manager.getCurrentUserDTO();
		Long store_id = userDTO.getStore_id();
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		String express_no = null;
		String rcv_name = null;
		String begindate=null;
		String enddate = null;
		for(Map<String, Object> map : condition.getConditions()){
			if("express_no".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				express_no = map.get("value").toString();
			}
			if("rcv_name".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				rcv_name = map.get("value").toString();
			}
			
			if("express_date".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				String[] dates = map.get("value").toString().split(",");
				if(dates.length!=0){
					if(dates.length==1){
						if(dates[0]!=null&&dates[0].length()>0){
							begindate=dates[0];
						}
					}else{
						if(dates[0]!=null&&dates[0].length()>0){
							begindate=dates[0];
						}
						if(dates[1]!=null&&dates[1].length()>0){
							enddate=dates[1];
						}
					}
				}
			}
			
		}
		List<?> lst_data = null;
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("id", ISort.DESC));
		StringBuffer sbfCondition = new StringBuffer();
		sbfCondition.append(" status=0 ");
		
		if(express_no!=null){
			sbfCondition.append(" and express_no like '%"+express_no+"%'");
		}
		if(rcv_name!=null){
			sbfCondition.append(" and rcv_name like '%"+rcv_name+"%'");
		}
		if(begindate!=null){
			sbfCondition.append(" and express_date >='"+begindate+" 00:00:00'");
		}
		if(enddate!=null){
			sbfCondition.append(" and express_date <='"+enddate+" 23:59:59'");
		}
		
		
		if(store_id==null){
			IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition.toString());
			fsp.setPage(pageInfo);
			fsp.setUserFilter(iFilter);
			lst_data = this.getList(fsp);
		}else{
			IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition+" and store_id ="+store_id);
			fsp.setPage(pageInfo);
			fsp.setUserFilter(iFilter);
			lst_data = this.getList(fsp);
		}
		
		String store_name = null;
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager"); 
		if(store_id!=null){
			//查询当前门店
			Store store = (Store)storeManager.getObject(store_id);
			store_name=store.getName();
		}
		
		
		List<SelfExpress> rList = new ArrayList<SelfExpress>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(int i = 0 ; i < lst_data.size() ; i++){
			SelfExpress hi = (SelfExpress) lst_data.get(i);
			if(hi.getExpress_date()!=null){
				String express_date_str = sdf.format(hi.getExpress_date());
				hi.setExpress_date_str(express_date_str);
			}
			if(hi.getTake_express_date()!=null){
				String take_express_date_str = sdf.format(hi.getTake_express_date());
				hi.setTake_express_date_str(take_express_date_str);
			}
			if(store_name!=null){
				hi.setStore_name(store_name);
			}else{
				try {
					if(hi.getStore_id()!=null){
						Store store = (Store)storeManager.getObject(hi.getStore_id());
						store_name=store.getName();
						hi.setStore_name(store_name);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			rList.add(hi);
		}
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", rList);
		return returnMap;
	}
    
    
    @Override
    public SelfExpress deleteSelfExpress(Long id){
    	SelfExpress selfExpress=(SelfExpress) this.getObject(id); 
    	selfExpress.setStatus(1);
    	preSave(selfExpress);
    	save(selfExpress);
    	return selfExpress;
    }
    
    
    protected void preSaveObject(Object o) {
		if (o instanceof DataEntity) {
			User sessionUser = null;
			if (null != SessionManager.getUserSession()
					&& null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession()
						.getSessionData().get("user");
			}
			DataEntity dataEntity = (DataEntity) o;
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			// insert处理时添加创建人和创建时间
			if (null == dataEntity.getCreate_time()) {
				dataEntity.setCreate_time(sdate);
				if (null != sessionUser) {
					dataEntity.setCreate_user(sessionUser.getCode());
					dataEntity.setCreate_user_id(sessionUser.getId());
				}
			}
			dataEntity.setUpdate_time(sdate);
			if (null != sessionUser) {
				dataEntity.setUpdate_user(sessionUser.getCode());
				dataEntity.setUpdate_user_id(sessionUser.getId());
			}
		}
	}
}
