package com.cnpc.pms.personal.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
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
import com.cnpc.pms.personal.entity.XxExpress;
import com.cnpc.pms.personal.manager.SelfExpressManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.XxExpressManager;
import com.cnpc.pms.utils.DateUtils;

/**
 * 许鲜自提业务实现
 * @author zhaoxg 2016-8-2
 *
 */
public class XxExpressManagerImpl extends BizBaseCommonManager implements XxExpressManager {

    /**
     * 解析Excel导入自提数据
     * @param lst_import_excel 自提Excel
     */
    @Override
    public void importXxExpressForExcel(List<File> lst_import_excel) throws Exception {
    	//EmployeeDao employeeDao = (EmployeeDao) SpringHelper.getBean(EmployeeDao.class.getName());
        for(File file_excel : lst_import_excel) {
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
            String[] titles ={"到","取","品类","姓名","电话","备注"};
            Map<String, Integer> maps = new HashMap<String, Integer>();
            
            for(int i = 0; i < sheetCount;i++){
                sheet_data = wb_excel.getSheetAt(i);
                for(int nRowIndex = 0;nRowIndex < sheet_data.getPhysicalNumberOfRows();nRowIndex++){
                    Row row_express = sheet_data.getRow(nRowIndex);
                    if(row_express == null){
                        continue;
                    }
                    //开始读取列
                    //如果电话为空 不处理 
//                    String rcv_tel = getCellValue(row_express.getCell(4));
//                    if(rcv_tel == null || "".equals(rcv_tel)){
//                        continue;
//                    }
                    
                    if(maps.size() < 3){
                    	int nCellSize = row_express.getPhysicalNumberOfCells();
                    	for(int nCellIndex = 0;nCellIndex < nCellSize ;nCellIndex ++){
                    		String str_value = getCellValue(row_express.getCell(nCellIndex));
                    		String str_title = isHave(titles,str_value);
                    		if(str_title != null){
                    			maps.put(str_title, nCellIndex);
                    		}
                    	}
                    	if(maps.size() >= 3){
                    		maps.put("nStartRow", nRowIndex);
                    	}
                    }
//                    for(int m=0;m<titles.length;m++){
//                    	if(sheet_data.getRow(1).getCell(m).toString().equals(titles[m])){
//                    		maps.put(titles[m], m);
//                    	}
//                    }

                    Integer nStartRow =  maps.get("nStartRow");
                    if(nStartRow != null && nRowIndex <= nStartRow){
                    	continue;
                    }
                    if(nStartRow == null){
                    	continue;
                    }
                    
                    String rcv_tel = getMapValue(row_express, maps, "电话");
                    if(rcv_tel == null || "".equals(rcv_tel)){
                      continue;
                    }
                    
                    XxExpress obj_express = new XxExpress();
                    //收件日期
                    String str_express_date = getMapValue(row_express, maps, "到");
                    if(str_express_date == null || "".equals(str_express_date)){
                        obj_express.setExpress_date(null);
                    }else{
                    	String format_express_date = formatDate(str_express_date);
                    	obj_express.setExpress_date(DateUtils.str_to_date(format_express_date.trim(), "yyyy-MM-dd"));
                    }
                    //签收日期
                    String str_take_express_date = getMapValue(row_express, maps, "取");
                    if(str_take_express_date == null || "".equals(str_take_express_date)){
                        obj_express.setTake_express_date(null);
                    }else{
                    	String format_express_date = formatDate(str_take_express_date);
                    	obj_express.setTake_express_date(DateUtils.str_to_date(format_express_date.trim(),"yyyy-MM-dd"));
                    }
                    obj_express.setObj_name(getMapValue(row_express, maps, "品类"));
                    obj_express.setRcv_name(getMapValue(row_express, maps, "姓名"));
                    obj_express.setRcv_tel(getMapValue(row_express, maps, "电话"));
                    obj_express.setRemark(getMapValue(row_express, maps, "备注"));
                    
                    obj_express.setStore_id(store_id);
                    
                    preObject(obj_express);
                    saveObject(obj_express);
                }
            }
            is_excel.close();
        }
    }
    
    
    
    /**
     * 根据ID 查询一条信息
     * @param id
     * @return
     */
    @Override
    public XxExpress queryxxExpressById(Long id){
    	XxExpress xxExpress=(XxExpress) this.getObject(id); 
    	return xxExpress;
    }
    
    
    private String getMapValue(Row row_express,Map<String, Integer> maps,String key){
    	if(maps.get(key)!=null){
    		String ret = getCellValue(row_express.getCell(maps.get(key)));
    		return ret;
    	}
    	return "";
    }
    
    private String isHave(String[] titiles,String str_value){
    	for(String titleString : titiles){
    		if(str_value.contains(titleString)){
    			return titleString;
    		}
    	}
    	return null;
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
     * 保存许鲜信息 
     */
    @Override
    public XxExpress saveXxExpress(XxExpress xxExpress){
    	UserManager manager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = manager.getCurrentUserDTO();
		Long store_id = userDTO.getStore_id();
		if(xxExpress.getStore_id()!=null){
			xxExpress.setStore_id(xxExpress.getStore_id());
		}else{
			xxExpress.setStore_id(store_id);
		}
		preSaveObject(xxExpress);
    	save(xxExpress);
    	return xxExpress;
    }
    
    /**
     * 根据ID删除许鲜信息 
     * @param id
     * @return
     */
    @Override
    public XxExpress deleteXxExpress(Long id){
    	XxExpress xxExpress=(XxExpress) this.getObject(id); 
    	xxExpress.setStatus(1);
    	preSave(xxExpress);
    	save(xxExpress);
    	return xxExpress;
    }
    
    
    
    @Override
    public Map<String, Object> queryXxExpressList(QueryConditions condition) {
    	UserManager manager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = manager.getCurrentUserDTO();
		Long store_id = userDTO.getStore_id();
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		String rcv_tel = null;
		String rcv_name = null;
		String begindate=null;
		String enddate = null;
		for(Map<String, Object> map : condition.getConditions()){
			if("rcv_name".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				rcv_name = map.get("value").toString();
			}
			if("rcv_tel".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				rcv_tel = map.get("value").toString();
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
		sbfCondition.append(" status!=1 ");
		if(rcv_tel!=null){
			sbfCondition.append(" and rcv_tel like '%"+rcv_tel+"%'");
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
		
		List<XxExpress> rList = new ArrayList<XxExpress>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(int i = 0 ; i < lst_data.size() ; i++){
			XxExpress hi = (XxExpress) lst_data.get(i);
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
