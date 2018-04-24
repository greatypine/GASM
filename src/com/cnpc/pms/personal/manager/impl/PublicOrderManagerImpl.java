package com.cnpc.pms.personal.manager.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.dynamic.dao.DynamicDao;
import com.cnpc.pms.dynamic.entity.AbnormalOrderDto;
import com.cnpc.pms.personal.dao.PublicOrderDao;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.PublicOrder;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.PublicOrderManager;
import com.cnpc.pms.personal.manager.StoreManager;

@SuppressWarnings("all")
public class PublicOrderManagerImpl extends BizBaseCommonManager implements PublicOrderManager {

	private XSSFCellStyle style_header = null;
	private CellStyle cellStyle_common = null;
	
	@Override
	public PublicOrder saveAppVersion(PublicOrder publicOrder) {
		
		return null;
	}
	
	
	@Override
	public Map<String, Object> queryPublicOrder(PublicOrder publicOrder, PageInfo pageInfo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		PublicOrderDao publicOrderDao = (PublicOrderDao)SpringHelper.getBean(PublicOrderDao.class.getName());
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			//取得当前登录人 所管理城市
			
			String cityname = publicOrder.getCityname();
			
			String cityssql = "'"+cityname+"'";
			
			String storenos = "";
			if(cityssql!=""){
				List<Store> lst_store = storeManager.findStoreByCityData(cityssql);
				if(lst_store!=null&&lst_store.size()>0){
					for(Store s:lst_store){
						storenos+="'"+s.getStoreno()+"',";
					}
					storenos=storenos.substring(0, storenos.length()-1);
				}
			}
			publicOrder.setStorenos(storenos);
			
			if(publicOrder.getStorename()!=null&&publicOrder.getStorename().length()>0){
				Store store = storeManager.getStoreByName(publicOrder.getStorename());
				if(store!=null){
					publicOrder.setStoreno(store.getStoreno());
				}
			}
			
			result = publicOrderDao.queryPublicOrder(publicOrder, pageInfo);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}
	
	
	
	
	
	@Override
	public Map<String, Object> querySearchOrder(PublicOrder publicOrder, PageInfo pageInfo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		PublicOrderDao publicOrderDao = (PublicOrderDao)SpringHelper.getBean(PublicOrderDao.class.getName());
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			//取得当前登录人 所管理城市
			String cityname = publicOrder.getCityname();
			String cityssql = "'"+cityname+"'";
			String storenos = "";
			if(cityssql!=""){
				List<Store> lst_store = storeManager.findStoreByCityData(cityssql);
				if(lst_store!=null&&lst_store.size()>0){
					for(Store s:lst_store){
						storenos+="'"+s.getStoreno()+"',";
					}
					storenos=storenos.substring(0, storenos.length()-1);
				}
			}
			publicOrder.setStorenos(storenos);
			if(publicOrder.getStorename()!=null&&publicOrder.getStorename().length()>0){
				Store store = storeManager.getStoreByName(publicOrder.getStorename());
				if(store!=null){
					publicOrder.setStoreno(store.getStoreno());
				}
			}
			result = publicOrderDao.querySearchOrder(publicOrder, pageInfo);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}
	
	
	@Override
	public Map<String, Object> updatePublicOrderByIds(List<Map<String, Object>> publicOrders){
		Map<String, Object> rt_map = new HashMap<String, Object>();
		List<Map<String, Object>> orderObjs = publicOrders;
		if(orderObjs!=null&&orderObjs.size()>0){
			PublicOrderManager publicOrderManager = (PublicOrderManager) SpringHelper.getBean("publicOrderManager");
			User sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			
			//判断日期是否大于当月1号18点
			for(Map<String, Object> mapObj:orderObjs){
				String order_id = mapObj.get("df_order_id")==null?"":mapObj.get("df_order_id").toString();
				PublicOrder publicOrder = (PublicOrder) publicOrderManager.getObject(order_id);
				String signtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(publicOrder.getDf_signed_time());
				
				
				SimpleDateFormat simpleDateFormatYM = new SimpleDateFormat("yyyy-MM");
				SimpleDateFormat simpleDateFormatNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    Calendar calendar= Calendar.getInstance(); 
			    calendar.setTime(new Date());
			    calendar.add(Calendar.MONTH, 0);
				String cur_month_one = simpleDateFormatYM.format(calendar.getTime())+"-01 18:00:00";
				String now_month_day = simpleDateFormatNow.format(new Date());
				int ret = compare_date(now_month_day, cur_month_one);
				if(ret>0&&signtime.length()>7){
					/*System.out.println("当前时间："+now_month_day);
					System.out.println("本月六点："+cur_month_one);
					System.out.println("判断如果是 上个月的数据不可以分配，为公海均分！！！");*/
					calendar.add(Calendar.MONTH, -1);
					System.out.println("上个月："+simpleDateFormatYM.format(calendar.getTime()));
					String pre_month = simpleDateFormatYM.format(calendar.getTime());
					String sign_month = signtime.substring(0,7);
					System.out.println("签收月："+sign_month);
					if(pre_month.equals(sign_month)){
						System.out.println("此条标红！ ");
						rt_map.put("msg", "存在上个月公海订单，请刷新后重新选择分配！ ");
						return rt_map;
					}
				}
			}
			
			
			for(Map<String, Object> obj:orderObjs){
				String employeeno = obj.get("employeeno")==null?"":obj.get("employeeno").toString();
				String employeename = obj.get("employeename")==null?"":obj.get("employeename").toString();
				String area_nos = obj.get("area_nos")==null?"":obj.get("area_nos").toString();
				String area_names = obj.get("area_names")==null?"":obj.get("area_names").toString();
				String order_id = obj.get("df_order_id")==null?"":obj.get("df_order_id").toString();
				
				PublicOrder publicOrder = (PublicOrder) publicOrderManager.getObject(order_id);
				publicOrder.setOrder_id(order_id);
				publicOrder.setArea_names(area_names);
				publicOrder.setArea_nos(area_nos);
				publicOrder.setEmployeename(employeename);
				publicOrder.setEmployeeno(employeeno);
				
				//当前登录人及时间  
				publicOrder.setUpdatetime(sdate);
				publicOrder.setUpdateuser(sessionUser.getCode());
				publicOrder.setUpdateuserid(sessionUser.getId());
				
				publicOrderManager.saveObject(publicOrder);
				
			}
		}
		
		return rt_map;
	}
	
	
	
	//导出公海订单数据  
	@Override
	public Map<String, Object> exportPublicOrder(PublicOrder publicOrder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Map<String, Object> result = new HashMap<String,Object>();
		PublicOrderDao publicOrderDao = (PublicOrderDao)SpringHelper.getBean(PublicOrderDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			//取得当前登录人 所管理城市
			String cityname = publicOrder.getCityname();
			
			String cityssql = "'"+cityname+"'";
			
			String storenos = "";
			if(cityssql!=""){
				List<Store> lst_store = storeManager.findStoreByCityData(cityssql);
				if(lst_store!=null&&lst_store.size()>0){
					for(Store s:lst_store){
						storenos+="'"+s.getStoreno()+"',";
					}
					storenos=storenos.substring(0, storenos.length()-1);
				}
			}
			publicOrder.setStorenos(storenos);
			
			if(publicOrder.getStorename()!=null&&publicOrder.getStorename().length()>0){
				Store store = storeManager.getStoreByName(publicOrder.getStorename());
				if(store!=null){
					publicOrder.setStoreno(store.getStoreno());
				}
			}
			list = publicOrderDao.exportPublicOrder(publicOrder);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if(list!=null&&list.size()>0){//成功返回数据

			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");

			/*HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab
	        setCellStyle_common(wb);
	        setHeaderStyle(wb);
	        HSSFSheet sheet = wb.createSheet("公海订单");
	        HSSFRow row = sheet.createRow(0);*/
	        
	        
	        XSSFWorkbook wb = new XSSFWorkbook();   
	        setCellStyle_common(wb);
	        setHeaderStyle(wb);
	        XSSFSheet sheet = wb.createSheet("公海订单");
	        XSSFRow row = sheet.createRow(0);
	        
	        //定义表头 以及 要填入的 字段 
	        //String[] str_headers = {"公海分配员工编号","公海分配员工","订单号","订单金额","下单用户","下单用户电话","所属片区信息","签收时间","门店名称"};
	        //String[] headers_key = {"employee_no","employee_name","df_order_sn","df_trading_price","df_customer_name","df_customer_phone","err_msg","df_signed_time","df_store_name","df_customer_id"};
	        String[] str_headers = {"公海分配员工编号","公海分配员工","订单号","订单金额","下单用户","下单用户电话","签收时间","门店名称"};
	        String[] headers_key = {"employee_no","employee_name","df_order_sn","df_trading_price","df_customer_name","df_customer_phone","df_signed_time","df_store_name","df_customer_id"};
	       
	        for(int i = 0;i < str_headers.length;i++){
	            XSSFCell cell = row.createCell(i);
	            cell.setCellStyle(getHeaderStyle());
	            cell.setCellValue(new XSSFRichTextString(str_headers[i]));
	        }
	        
	        for(int i = 0;i < list.size();i++){
	        	 row = sheet.createRow(i+1);
	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
	            	 if(headers_key[cellIndex].toString().equals("err_msg")){
	            		 String df_order_placename = list.get(i).get("df_order_placename")==null?"":list.get(i).get("df_order_placename").toString();
	            		 String df_tiny_village_name = list.get(i).get("df_tiny_village_name")==null?"":list.get(i).get("df_tiny_village_name").toString();
	            		 String df_area_no = list.get(i).get("df_area_no")==null?"":list.get(i).get("df_area_no").toString();
	            		 String df_employee_a_no = list.get(i).get("df_employee_a_no")==null?"":list.get(i).get("df_employee_a_no").toString();
	            		 
	    			       String nomessage="";
	    			       if(df_order_placename.equals("")){
	    			    	   nomessage="无订单区块";
	    			       }else if(df_tiny_village_name.equals("")){
	    			    	   nomessage="未绑定小区";
	    			       }else if(df_area_no.equals("")){
	    			    	   nomessage="未绑定片区";
	    			       }else if(df_employee_a_no.equals("")){
	    			    	   nomessage="片区未指定A国安侠";
	    			       }
	            		 setCellValue(row, cellIndex, nomessage);
	            	 }else if(headers_key[cellIndex].toString().equals("employee_name")){
	            		 String df_customer_id = list.get(i).get("df_customer_id")==null?"":list.get(i).get("df_customer_id").toString();
	            		 String df_signed_time = list.get(i).get("df_signed_time")==null?"":list.get(i).get("df_signed_time").toString();

	            		 if(df_customer_id.contains("fakecustomer")){
	            			 setCellValue(row, cellIndex, "门店均分");
	    			     }else{
	    			    	 
	    			    	 
	    			    	 
	    			    	 
	    			    	 SimpleDateFormat simpleDateFormatYM = new SimpleDateFormat("yyyy-MM");
	    						SimpleDateFormat simpleDateFormatNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    					    Calendar expcalendar= Calendar.getInstance(); 
	    					    expcalendar.setTime(new Date());
	    					    expcalendar.add(Calendar.MONTH, 0);
	    						String cur_month_one = simpleDateFormatYM.format(expcalendar.getTime())+"-01 18:00:00";
	    						String now_month_day = simpleDateFormatNow.format(new Date());
	    						int ret = compare_date(now_month_day, cur_month_one);
	    						if(ret>0&&df_signed_time.length()>7){
	    							/*System.out.println("当前时间："+now_month_day);
	    							System.out.println("本月六点："+cur_month_one);
	    							System.out.println("判断如果是 上个月的数据不可以分配，为公海均分！！！");*/
	    							expcalendar.add(Calendar.MONTH, -1);
	    							System.out.println("上个月："+simpleDateFormatYM.format(expcalendar.getTime()));
	    							String pre_month = simpleDateFormatYM.format(expcalendar.getTime());
	    							String sign_month = df_signed_time.substring(0,7);
	    							System.out.println("签收月："+sign_month);
	    		            		String employee_no = list.get(i).get("employee_no")==null?"":list.get(i).get("employee_no").toString();
	    							System.out.println(employee_no);
	    							if(pre_month.equals(sign_month)&&employee_no==""){
	    								 setCellValue(row, cellIndex, "无");
	    							}else{
	    								setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
	    							}
	    						}else{
	    							setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
	    						}
	    			    	 
	    			    	 
	    			     }
	            	 }else if(headers_key[cellIndex].toString().equals("df_customer_id")){
	            	 
	            	 }else {
	            		 setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
	            	 }
	                 
	             }
	        }

			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_publicorder.xlsx");
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
	
	
	
	
	/**
	 * 导出 订单查询结果 
	 */
	@Override
	public Map<String, Object> exportSearchOrder(PublicOrder publicOrder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Map<String, Object> result = new HashMap<String,Object>();
		PublicOrderDao publicOrderDao = (PublicOrderDao)SpringHelper.getBean(PublicOrderDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			//取得当前登录人 所管理城市
			String cityname = publicOrder.getCityname();
			
			String cityssql = "'"+cityname+"'";
			
			String storenos = "";
			if(cityssql!=""){
				List<Store> lst_store = storeManager.findStoreByCityData(cityssql);
				if(lst_store!=null&&lst_store.size()>0){
					for(Store s:lst_store){
						storenos+="'"+s.getStoreno()+"',";
					}
					storenos=storenos.substring(0, storenos.length()-1);
				}
			}
			publicOrder.setStorenos(storenos);
			
			if(publicOrder.getStorename()!=null&&publicOrder.getStorename().length()>0){
				Store store = storeManager.getStoreByName(publicOrder.getStorename());
				if(store!=null){
					publicOrder.setStoreno(store.getStoreno());
				}
			}
			list = publicOrderDao.exportSearchOrder(publicOrder);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if(list!=null&&list.size()>0){//成功返回数据

			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");

	        XSSFWorkbook wb = new XSSFWorkbook();   
	        setCellStyle_common(wb);
	        setHeaderStyle(wb);
	        XSSFSheet sheet = wb.createSheet("公海订单");
	        XSSFRow row = sheet.createRow(0);
	        
	        //定义表头 以及 要填入的 字段 
	        String[] str_headers = {"订单编号","门店名称","所属事业群","所属频道","签收时间","退款时间","所属区块","所属小区","所属片区","国安侠","有效金额","定单金额","支付金额"};
	        String[] headers_key = {"df_order_sn","df_store_name","df_dep_name","df_channel_name","df_signed_time","df_return_time","df_order_placename","df_order_placename","df_area_name","df_employee_a_name","df_gmv_price","df_trading_price","df_payable_price"};
	       
	        for(int i = 0;i < str_headers.length;i++){
	            XSSFCell cell = row.createCell(i);
	            cell.setCellStyle(getHeaderStyle());
	            cell.setCellValue(new XSSFRichTextString(str_headers[i]));
	        }
	        
	        for(int i = 0;i < list.size();i++){
	        	 row = sheet.createRow(i+1);
	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
	            	 setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
	             }
	        }

			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_searchorder.xlsx");
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

	private void setCellStyle_common(Workbook wb){
		cellStyle_common=wb.createCellStyle();
		cellStyle_common.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 水平居中
		cellStyle_common.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);//垂直居中
		
	}

	private CellStyle getCellStyle_common() {
		return cellStyle_common;
	}

	public void setCellValue(Row obj_row, int nCellIndex, Object value){
		Cell cell=obj_row.createCell(nCellIndex);
		cell.setCellStyle(getCellStyle_common());
		cell.setCellValue(new XSSFRichTextString(value==null?null:value.toString()));
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
	}
	
	
	public int compare_date(String DATE1, String DATE2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
	
	
	
	//判断是否是当月第一天
	@Override
	public boolean isMonthFirstDay(){
		Calendar cal = Calendar.getInstance(); 
	     cal.setTime(new Date());
	     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String cur_date = sdf.format(cal.getTime());
	     SimpleDateFormat first_month = new SimpleDateFormat("yyyy-MM");
	     String cur_month = first_month.format(cal.getTime())+"-01";
	     if(cur_date.equals(cur_month)){
	    	 return true;
	     }else{
	    	 return false;
	     }
	}
    
}
