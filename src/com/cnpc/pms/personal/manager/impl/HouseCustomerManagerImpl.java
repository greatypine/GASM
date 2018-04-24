package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.dao.ExpressDao;
import com.cnpc.pms.personal.dao.HouseCustomerDao;
import com.cnpc.pms.personal.entity.HouseCustomer;
import com.cnpc.pms.personal.manager.HouseCustomerManager;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 户型客户业务类
 * Created by liuxiao on 2016/8/8 0008.
 */
public class HouseCustomerManagerImpl extends BizBaseCommonManager implements HouseCustomerManager {

	PropertiesValueUtil propertiesValueUtil = null;
	
    /**
     * 保存房屋用户关联表
     * @param houseCustomer 房屋用户对象
     */
    @Override
    public void saveHouseCustomer(HouseCustomer houseCustomer) {
    	IFilter filter = FilterFactory.getSimpleFilter("customer_id= " + houseCustomer.getCustomer_id() + " order by id desc");
    	
    	
        List<?> lst_house_customer_cur =  this.getList(FilterFactory.getSimpleFilter("house_id",houseCustomer.getHouse_id())
                .appendAnd(FilterFactory.getSimpleFilter("customer_id",houseCustomer.getCustomer_id())));
        if(lst_house_customer_cur == null || lst_house_customer_cur.size() == 0){
        	List<?> lst_house_customer_last =  this.getList(filter);
        	if(lst_house_customer_last!=null&&lst_house_customer_last.size()>0){
        		HouseCustomer hCustomer = (HouseCustomer)lst_house_customer_last.get(0);
        		houseCustomer.setOne_pay(hCustomer.getOne_pay());
        		houseCustomer.setOne_date(hCustomer.getOne_date());
        		houseCustomer.setSix_pay(hCustomer.getSix_pay());
        		houseCustomer.setSix_date(hCustomer.getSix_date());
        		houseCustomer.setThird_grade_pay(hCustomer.getThird_grade_pay());
        		houseCustomer.setThird_grade_date(hCustomer.getThird_grade_date());
        	}
            preObject(houseCustomer);
            this.saveObject(houseCustomer);
        }
    }

    @Override
    public Integer checkedHouseCustomer(HouseCustomer houseCustomer) {
        IFilter filter = FilterFactory.getSimpleFilter("customer_id",houseCustomer.getCustomer_id())
                .appendAnd(FilterFactory.getSimpleFilter("house_id",houseCustomer.getHouse_id()));
        List<?> list = super.getList(filter);
        if(list != null){
            return list.size();
        }
        return 0;
    }

    @Override
    public String exportAchievements(String work_month) throws Exception {
        HouseCustomerDao houseCustomerDao = (HouseCustomerDao) SpringHelper.getBean(HouseCustomerDao.class.getName());
        String str_file_dir_path = PropertiesUtil.getValue("file.root");
        String str_web_path = PropertiesUtil.getValue("file.web.root");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date_month = sdf.parse(work_month);

        Calendar calendar_month  =  Calendar.getInstance();
        calendar_month.setTime(date_month);
        calendar_month.set(Calendar.DAY_OF_MONTH,1);
        calendar_month.add(Calendar.MONTH, 1);// 月份减1
        calendar_month.add(Calendar.DATE, -1);// 日期减1

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str_month = sdf.format(calendar_month.getTime());
        System.out.println(str_month);
        houseCustomerDao.updateOnePayStatus(str_month);//设置满足6个条件的
        houseCustomerDao.updateSixPayStatus(str_month);//设置满足18个条件的
        houseCustomerDao.updateThirdGradePayStatus(str_month);//设置满足35个条件的

        List<Map<String,Object>> lst_result = houseCustomerDao.getAchievementsList(str_month);
        if(lst_result == null){
            return null;
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建Excel的工作sheet,对应到一个excel文档的tab

        setCellStyle_common(wb);
        setHeaderStyle(wb);
        HSSFSheet sheet = wb.createSheet("单体画像完成量");
        HSSFRow row = sheet.createRow(0);
        String[] str_headers = {"门店名称","员工编码","员工姓名","6字段个数","18字段个数","35字段个数"};
        for(int i = 0;i < str_headers.length;i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(getHeaderStyle());
            cell.setCellValue(new HSSFRichTextString(str_headers[i]));
        }

        int index = 1;

        for(Map<String,Object> map_row : lst_result){
            HSSFRow row_data = sheet.createRow(index);
            for(int cellIndex = 0;cellIndex < str_headers.length; cellIndex ++){
                setCellValue(row_data, cellIndex, map_row.get(str_headers[cellIndex]));
            }
            index ++;
        }

        File file_xls = new File(str_file_dir_path + File.separator + "dantihuaxiang_" +work_month+".xls");
        if(file_xls.exists()){
            file_xls.delete();
        }
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file_xls.getAbsoluteFile());
            wb.write(os);
        } finally {
            if(os != null){
                os.close();
            }
        }
        return str_web_path.concat(file_xls.getName());
    }

    public void setCellValue(Row obj_row, int nCellIndex, Object value){
        Cell cell=obj_row.createCell(nCellIndex);
        cell.setCellStyle(getCellStyle_common());
        cell.setCellValue(new HSSFRichTextString(value==null?null:value.toString()));
    }

    private HSSFCellStyle style_header = null;
    private CellStyle cellStyle_common = null;

    private HSSFCellStyle getHeaderStyle(){
        return style_header;
    }

    private void setHeaderStyle(HSSFWorkbook wb){

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

    private void setCellStyle_common(Workbook wb){
        cellStyle_common=wb.createCellStyle();
        cellStyle_common.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        cellStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);//垂直居中
    }

    private CellStyle getCellStyle_common() {
        return cellStyle_common;
    }
    
    @Override
    public File queryUserHouseCount() throws Exception{
    	
		String str_file_name = "export_userhouse_template";
		String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
		//配置文件中的路径
		String str_filepath = strRootpath.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));
		File file_template = new File(str_filepath);

		HouseCustomerDao houseCustomerDao = (HouseCustomerDao) SpringHelper.getBean(HouseCustomerDao.class.getName());
		List<Map<String, Object>>  objList =houseCustomerDao.queryUserHouseCount();
		
		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_newfilepath = str_file_dir_path + "user_house_count.xls";
		File file_new = new File(str_newfilepath);
		if(file_new.exists()){
			file_new.delete();
		}

		FileCopyUtils.copy(file_template, file_new);
		FileInputStream fis_input_excel = new FileInputStream(file_new);
		FileOutputStream fis_out_excel = null;
		Workbook wb_userhouseinfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
		try{
			setCellStyle_common(wb_userhouseinfo);

			Sheet sh_job = wb_userhouseinfo.getSheetAt(0);
			
			int nIndex=1;
			if(objList!=null&&objList.size()>0){
				for (Map<String, Object> obj : objList) {
					Row obj_row = null;
					int cellIndex = 0;
					if(obj==null){
						continue;
					}
					sh_job.createRow(nIndex);
					obj_row = sh_job.getRow(nIndex);
					
					String store_id = obj.get("store_id")==null?"":obj.get("store_id").toString();
					String store_name = obj.get("store_name")==null?"":obj.get("store_name").toString();
					String yiyuangeshu = obj.get("yiyuangeshu")==null?"":obj.get("yiyuangeshu").toString();
					String liugeziduan = obj.get("liugeziduan")==null?"":obj.get("liugeziduan").toString();
					String liuyuangeshu = obj.get("liuyuangeshu")==null?"":obj.get("liuyuangeshu").toString();
					
					
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(store_name));
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(yiyuangeshu));
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(liugeziduan));
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(liuyuangeshu));

					nIndex++;
				}
			}
			
			fis_out_excel = new FileOutputStream(file_new);
			wb_userhouseinfo.write(fis_out_excel);
		}catch (Exception e){
			throw e;
		} finally {
			if(fis_out_excel!=null){
				fis_out_excel.close();
			}
			if(fis_input_excel!=null){
				fis_input_excel.close();
			}
		}
    	return file_new;
    }
    
    
    
    public PropertiesValueUtil getPropertiesValueUtil(){
        if(propertiesValueUtil==null){
            propertiesValueUtil = new PropertiesValueUtil(File.separator+"conf"+File.separator+"download_config.properties");
        }
        return propertiesValueUtil;
    }

	
	
	public void changeCustomerPayStatus() {
		log.info("定时任务设置用户支付状态");
		System.out.println("定时任务设置用户支付状态");
		HouseCustomerDao houseCustomerDao = (HouseCustomerDao) SpringHelper.getBean(HouseCustomerDao.class.getName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); 
		Calendar calendar_month  =  Calendar.getInstance();
        calendar_month.set(Calendar.DAY_OF_MONTH,1);
        calendar_month.add(Calendar.DATE, -1);// 日期减1

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str_month = sdf.format(calendar_month.getTime());
        System.out.println(str_month);
        try {
			houseCustomerDao.updateOnePayStatus(str_month);//设置满足6个条件的
			houseCustomerDao.updateSixPayStatus(str_month);//设置满足18个条件的
		    houseCustomerDao.updateThirdGradePayStatus(str_month);//设置满足35个条件的
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error("定时任务设置用户支付状态"+e.getMessage());
		}
       
		
	}

	@Override
	public HouseCustomer findHouseCustomerByHouseId(Long house_id) {
		IFilter filter = FilterFactory.getSimpleFilter("house_id="+house_id);
        List<?> list = super.getList(filter);
        if(list != null&&list.size()>0){
            return (HouseCustomer)list.get(0);
        }
        return null;
	}
    
}
