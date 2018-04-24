package com.cnpc.pms.personal.manager.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
import org.json.JSONArray;
import org.json.JSONObject;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.dynamic.dao.DynamicDao;
import com.cnpc.pms.dynamic.entity.AbnormalOrderDto;
import com.cnpc.pms.personal.dao.DsAbnormalOrderDao;
import com.cnpc.pms.personal.entity.DsAbnormalOrder;
import com.cnpc.pms.personal.entity.DsAbnormalType;
import com.cnpc.pms.personal.manager.DsAbnormalOrderManager;
import com.cnpc.pms.personal.manager.DsAbnormalTypeManager;
import com.ibm.db2.jcc.a.f;

import jxl.write.WritableWorkbook;
import net.sf.jasperreports.components.barbecue.BarcodeProviders.NW7Provider;

public class DsAbnormalOrderManagerImpl extends BizBaseCommonManager implements DsAbnormalOrderManager {
	
	private static HSSFCellStyle style_header = null;
	private static CellStyle cellStyle_common = null;
	private  static Integer SheetRecord  = 40000;
	
	
	public HSSFCellStyle getStyle_header() {
		return style_header;
	}

	public void setStyle_header(HSSFCellStyle style_header) {
		this.style_header = style_header;
	}

	public void setCellStyle_common(CellStyle cellStyle_common) {
		this.cellStyle_common = cellStyle_common;
	}

	@Override
	public DsAbnormalOrder queryDsAbnormalOrderBySN(String order_sn){
		DsAbnormalOrder dsAbnormalOrder = null;
		IFilter iFilter =FilterFactory.getSimpleFilter("ordersn='"+order_sn+"'");
		DsAbnormalOrderManager dsAbnormalOrderManager = (DsAbnormalOrderManager) SpringHelper.getBean("dsAbnormalOrderManager");
		List<DsAbnormalOrder> lst_orders = (List<DsAbnormalOrder>) dsAbnormalOrderManager.getList(iFilter);
		if(lst_orders!=null&&lst_orders.size()>0){
			dsAbnormalOrder = lst_orders.get(0);
		}
		return dsAbnormalOrder;
	}
	
	@Override
	public DsAbnormalOrder saveDsAbnormalOrder(DsAbnormalOrder dsAbnormalOrder){		
		DsAbnormalOrderManager dsAbnormalOrderManager = (DsAbnormalOrderManager) SpringHelper.getBean("dsAbnormalOrderManager");
		dsAbnormalOrderManager.saveObject(dsAbnormalOrder);
		return dsAbnormalOrder;
	}
	
	@Override
	public DsAbnormalType queryDsAbnormalTypeBySN(String order_sn){
		DsAbnormalOrder dsAbnormalOrder = queryDsAbnormalOrderBySN(order_sn);
		DsAbnormalType dsAbnormalType = null;
		if(dsAbnormalOrder!=null){
			DsAbnormalTypeManager dsAbnormalTypeManager = (DsAbnormalTypeManager) SpringHelper.getBean("dsAbnormalTypeManager");
			dsAbnormalType = dsAbnormalTypeManager.queryDsAbnormalTypeByAbnortype(dsAbnormalOrder.getAbnortype());
		}
		return dsAbnormalType;
	}

	
	@Override
	public Map<String, Object> updateDsAbnormalTypeBySN(String order_id) {
		DsAbnormalOrderDao dsAbnormalOrderDao = (DsAbnormalOrderDao)SpringHelper.getBean(DsAbnormalOrderDao.class.getName());
        Date updateDate =new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Map<String, Object> result = new HashMap<String, Object>();
		try {
			if(order_id!=null&&!"".equals(order_id)){
				String[] orderArr = order_id.split(",");
				for(String id:orderArr){
					dsAbnormalOrderDao.updateOrder(Long.parseLong(id),simpleDateFormat.format(updateDate));
				}
			}
			result.put("status", "success");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "fail");
			return result;
		}
		
		
	}

	
	private static HSSFCellStyle getHeaderStyle(){
        return style_header;
    }

    private static void setHeaderStyle(HSSFWorkbook wb){

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
	
	
	 private static void setCellStyle_common(Workbook wb){
	        cellStyle_common=wb.createCellStyle();
	        cellStyle_common.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
	        cellStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);//垂直居中
	 }

    private static CellStyle getCellStyle_common() {
        return cellStyle_common;
    }
    
    public static void setCellValue(Row obj_row, int nCellIndex, Object value){
        Cell cell=obj_row.createCell(nCellIndex);
        cell.setCellStyle(getCellStyle_common());
        cell.setCellValue(new HSSFRichTextString(value==null?null:value.toString()));
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    }
	
    /** 
     * 确定分页的个数 
     * 
     * @param rCount 总得记录条数 
     * @return 
     */  
    private static int getSheetCount(int rCount) {  
        
        if (rCount <= 0) {
        	 return 1;  
        }
        int n = rCount % (SheetRecord); // 余数  
        if (n == 0) {  
            return rCount / SheetRecord;  
        } else {  
            return (int) (rCount / (SheetRecord - 1)) + 1;  
        }  
    }  
    
	
	@Override
	public Map<String, Object> downAbnormalOrders(AbnormalOrderDto abnormalOrderDto) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			Map<String, Object> result = new HashMap<String,Object>();
			
			
	       
	        
	        
	       
			DsAbnormalOrderDao dsAbnormalOrderDao = (DsAbnormalOrderDao)SpringHelper.getBean(DsAbnormalOrderDao.class.getName());
			if("cur_month".equals(abnormalOrderDto.getsDate())){
				
				abnormalOrderDto.setYear(calendar.get(Calendar.YEAR));
				abnormalOrderDto.setMonth(calendar.get(Calendar.MONTH)+1);
			}else if("prev_month".equals(abnormalOrderDto.getsDate())){
				calendar.add(Calendar.MONTH, -1);
				abnormalOrderDto.setYear(calendar.get(Calendar.YEAR));
				abnormalOrderDto.setMonth(calendar.get(Calendar.MONTH)+1);
			}
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
			
			try {  
				list = dsAbnormalOrderDao.queryAbnormalDown(abnormalOrderDto);
				String path = batchExport(list);
				if("fail".equals(path)){
					result.put("message","请重新操作！");
					result.put("status","fail");
					return result;
				}
				
	            String fpath = craeteZipPath();  
	            result.put("data", fpath);
	           
	            result.put("message","下载成功！");
				result.put("status","success");
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }     
				        
			return result;
	}
	
	
	private static String batchExport(List<Map<String, Object>> list){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Map<String, Object> result = new HashMap<String,Object>();
		
		String str_file_dir_path = PropertiesUtil.getValue("file.root")+"\\abnormal";
        String str_web_path = PropertiesUtil.getValue("file.web.root")+"\\abnormal";
        File file = new File(str_file_dir_path);  
        
        if(!file.exists()){  
           file.mkdir(); //创建文件夹    
        }else{
        	if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                  f.delete();
                }
            }
        }  
		
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();  
	    WritableWorkbook book = null;  
	    try { 
	    		int excelCount=getSheetCount(list.size());//文件个数  
	    		
	        	//导出每个excel  
	        	int fromIndex=0;  
	        	int toIndex=list.size()<SheetRecord?list.size():SheetRecord;  
	        	for(int excel=0;excel<excelCount;excel++){  
	        		List<Map<String, Object>> listExcel=null;  
		            if(toIndex>list.size()){  
		                listExcel=list.subList(fromIndex, list.size());  
		            }else{  
		            	listExcel=list.subList(fromIndex,toIndex);  
		            }  
	  
		            HSSFWorkbook wb = new HSSFWorkbook();
			        // 创建Excel的工作sheet,对应到一个excel文档的tab

			        setCellStyle_common(wb);
			        setHeaderStyle(wb);
			        HSSFSheet sheet = wb.createSheet("手动异常订单");
			        HSSFRow row = sheet.createRow(0);
			        String[] str_headers = {"状态","城市","门店编号","门店名称","事业部","频道","年","月","订单号","订单金额","异常类型"};
			        String[] headers_key = {"state","cityname","storeno","storename","deptname","channelname","year","month","ordersn","tradingprice","description"};
			        for(int i = 0;i < str_headers.length;i++){
			            HSSFCell cell = row.createCell(i);
			            cell.setCellStyle(getHeaderStyle());
			            cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			        }
			        
			        for(int i = 0;i < listExcel.size();i++){
			        	 row = sheet.createRow(i+1);
			             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
			                 setCellValue(row, cellIndex, listExcel.get(i).get(headers_key[cellIndex]));
			             }
			        }

			       

			        File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_down_order"+excel+".xls");
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
			        
					fromIndex+=SheetRecord;  
				    toIndex+=SheetRecord;  
	        	}
	    }catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return str_web_path;
	}
	
	/** 
     * 生成.zip文件; 
     * @param path 
     * @throws IOException  
     */  
    public static String craeteZipPath() throws IOException{  
        ZipOutputStream zipOutputStream = null;  
        String zipfilename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"_abnormalorder.zip";  
        String filespath  = PropertiesUtil.getValue("file.root")+"\\abnormal";
        String str_web_path = PropertiesUtil.getValue("file.web.root")+zipfilename;
        String lj=PropertiesUtil.getValue("file.root")+zipfilename;  
        File file = new File(lj);  
        zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));  
        File[] files = new File(filespath).listFiles();  
        FileInputStream fileInputStream = null;  
        byte[] buf = new byte[1024];  
        int len = 0;  
        if(files!=null && files.length > 0){  
            for(File excelFile:files){  
                String fileName = excelFile.getName(); 
                fileInputStream = new FileInputStream(excelFile);  
                //放入压缩zip包中;  
                zipOutputStream.putNextEntry(new ZipEntry(fileName));  
                //读取文件;  
                while((len=fileInputStream.read(buf)) >0){  
                    zipOutputStream.write(buf, 0, len);  
                }  
                //关闭;  
                zipOutputStream.closeEntry();  
                if(fileInputStream != null){  
                    fileInputStream.close();  
                }  
            }  
        }  
          
        if(zipOutputStream !=null){  
            zipOutputStream.close();  
        }  

        
        return str_web_path;
    }

	
	@Override
	public Map<String, Object> queryBaseAbnormalOrder(AbnormalOrderDto abnormalOrderDto, PageInfo pageInfo) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		DsAbnormalOrderDao dsAbnormalOrderDao = (DsAbnormalOrderDao)SpringHelper.getBean(DsAbnormalOrderDao.class.getName());

//		if("cur_month".equals(abnormalOrderDto.getsDate())){
//			
//			abnormalOrderDto.setYear(calendar.get(Calendar.YEAR));
//			abnormalOrderDto.setMonth(calendar.get(Calendar.MONTH)+1);
//		}else if("prev_month".equals(abnormalOrderDto.getsDate())){
//			calendar.add(Calendar.MONTH, -1);
//			abnormalOrderDto.setYear(calendar.get(Calendar.YEAR));
//			abnormalOrderDto.setMonth(calendar.get(Calendar.MONTH)+1);
//		}
		
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			result = dsAbnormalOrderDao.queryBaseAbnormalOrder(abnormalOrderDto, pageInfo);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}  
	
	
	
	public Map<String, Object> exportBaseAbnormalOrder(AbnormalOrderDto abnormalOrderDto) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Map<String, Object> result = new HashMap<String,Object>();
		DsAbnormalOrderDao dsAbnormalOrderDao = (DsAbnormalOrderDao)SpringHelper.getBean(DsAbnormalOrderDao.class.getName());
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = dsAbnormalOrderDao.queryBaseAbnormalOrderNoPage(abnormalOrderDto);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if(list!=null&&list.size()>0){//成功返回数据

			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");

			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab
	        setCellStyle_common(wb);
	        setHeaderStyle(wb);
	        HSSFSheet sheet = wb.createSheet("异常订单基础数据");
	        HSSFRow row = sheet.createRow(0);
	        String[] str_headers = {"城市","门店编号","门店名称","E店名称","事业部","频道","订单签收日期","订单号","订单金额","应付金额","异常类型"};
	        String[] headers_key = {"cityname","storeno","storename","eshopname","deptname","channelname","signedtime","ordersn","tradingprice","payableprice","description"};
	        for(int i = 0;i < str_headers.length;i++){
	            HSSFCell cell = row.createCell(i);
	            cell.setCellStyle(getHeaderStyle());
	            cell.setCellValue(new HSSFRichTextString(str_headers[i]));
	        }
	        
	        for(int i = 0;i < list.size();i++){
	        	 row = sheet.createRow(i+1);
	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
	                 setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
	             }
	        }

			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_base_abnormalorder.xls");
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
	public Map<String, Object> queryAbnormalOrderResult(AbnormalOrderDto abnormalOrderDto, PageInfo pageInfo) {
		// TODO Auto-generated method stub
		DsAbnormalOrderDao dsAbnormalOrderDao = (DsAbnormalOrderDao)SpringHelper.getBean(DsAbnormalOrderDao.class.getName());
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String beginDate="";
		String endDate="";
		if(abnormalOrderDto.getMonth()==0){//上个月
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH,1);
			beginDate = sDateFormat.format(calendar.getTime());
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = sDateFormat.format(calendar.getTime());
		}else if(abnormalOrderDto.getMonth()==1){//当前月
			endDate = sDateFormat.format(calendar.getTime());
			calendar.set(Calendar.DAY_OF_MONTH,1);
			beginDate = sDateFormat.format(calendar.getTime());
		}
		
		abnormalOrderDto.setBeginDate(beginDate);
		abnormalOrderDto.setEndDate(endDate);
	
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			result = dsAbnormalOrderDao.queryAbnormalOrderResult(abnormalOrderDto, pageInfo);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}


	@Override
	public Map<String, Object> exportAbnormalOrderResult(AbnormalOrderDto abnormalOrderDto) {
		DsAbnormalOrderDao dsAbnormalOrderDao = (DsAbnormalOrderDao)SpringHelper.getBean(DsAbnormalOrderDao.class.getName());
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String beginDate="";
		String endDate="";
		if(abnormalOrderDto.getMonth()==0){//上个月
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH,1);
			beginDate = sDateFormat.format(calendar.getTime());
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = sDateFormat.format(calendar.getTime());
		}else if(abnormalOrderDto.getMonth()==1){//当前月
			endDate = sDateFormat.format(calendar.getTime());
			calendar.set(Calendar.DAY_OF_MONTH,1);
			beginDate = sDateFormat.format(calendar.getTime());
		}
		
		abnormalOrderDto.setBeginDate(beginDate);
		abnormalOrderDto.setEndDate(endDate);
	
		Map<String, Object> result =new HashMap<String,Object>();
		List<Map<String, Object>> list  = new ArrayList<Map<String,Object>>();
		
		
		try {
			list = dsAbnormalOrderDao.queryAbnormalOrderResult(abnormalOrderDto);
			
			if(list==null||list.size()==0){
				result.put("message","请重新操作！");
				result.put("status","fail");
				return result;
			}
			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");

			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab

			setCellStyle_common(wb);
			setHeaderStyle(wb);
			HSSFSheet sheet = wb.createSheet("虚假交易结果统计");
			HSSFRow row = sheet.createRow(0);
			String[] str_headers = {"城市","门店编码","门店","订单量","交易额"};
			String[] headers_key = {"cityname","storeno","storename","total","gmv"};
			for(int i = 0;i < str_headers.length;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(getHeaderStyle());
				cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			}

			for(int i = 0;i < list.size();i++){
				row = sheet.createRow(i+1);
				for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					setCellValue(row, cellIndex,list.get(i).get(headers_key[cellIndex]));
				}
			}



			File file_xls = new File(str_file_dir_path + File.separator+System.currentTimeMillis()+"_storetrade1.xls");
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
		} catch (Exception e) {
			e.printStackTrace();
			result.put("message","请重新操作！");
			result.put("status","fail");
		}
		
		return result;
	}
	 
}
