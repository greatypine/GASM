package com.cnpc.pms.personal.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.FileCopyUtils;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.dao.StoreDetailDao;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreDetail;
import com.cnpc.pms.personal.manager.StoreDetailManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;

public class StoreDetailManagerImpl extends BizBaseCommonManager implements StoreDetailManager{
	PropertiesValueUtil propertiesValueUtil = null;
	/**
     * 到处户型excel单元格公共样式
     */
    CellStyle cellStyle_common = null;
	@Override
	public Map<String, Object> showStoreDetailData(QueryConditions conditions) {
		StoreDetailDao storeDetailDao = (StoreDetailDao) SpringHelper.getBean(StoreDetailDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        for (Map<String, Object> map_where : conditions.getConditions()) {
        	if ("town_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND stt.town_name like '").append(map_where.get("value")).append("'");
            }else if ("name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND stt.`name` like '").append(map_where.get("value")).append("'");
			}else if ("cityName".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND stt.city_name like '").append(map_where.get("value")).append("'");
			}else if ("storeno".equals(map_where.get("key"))
                   && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
           	sb_where.append(" AND stt.storeno like '").append(map_where.get("value")).append("'");
			}else if ("standUP".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
				System.out.println(map_where.get("value"));
            	sb_where.append(" AND stt.open_shop_time >=date_format('"+map_where.get("value")+"', '%Y-%m-%d') ");
			}else if ("endUP".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
				System.out.println(map_where.get("value"));
            	sb_where.append(" AND stt.open_shop_time <=date_format('"+map_where.get("value")+"', '%Y-%m-%d') ");
			}else if ("estate".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
				System.out.println(map_where.get("value"));
            	sb_where.append(" AND stt.estate like '").append(map_where.get("value")).append("'");
			}
        }
      //取得当前登录人 所管理城市
  		String cityssql = "";
  		UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
  		List<DistCity> distCityList = userManager.getCurrentUserCity();
  		if(distCityList!=null&&distCityList.size()>0){
  			for(DistCity d:distCityList){
  				cityssql += "'"+d.getCityname()+"',";
  			}
  			cityssql=cityssql.substring(0, cityssql.length()-1);
  		}
        
  		if(cityssql!=""&&cityssql.length()>0){
  			sb_where.append(" and stt.city_name in ("+cityssql+")");
		}else{
			sb_where.append(" and 0=1 ");
		}
        System.out.println(sb_where);
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "门店详情");
        map_result.put("data", storeDetailDao.getStoreDetailData(sb_where.toString(), obj_page));
        return map_result;
	}

	@Override
	public StoreDetail seeOrUpdateStoreDetail(StoreDetail storeDetail) {
		StoreDetailManager storeDetailManager=(StoreDetailManager)SpringHelper.getBean("storeDetailManager");
		StoreManager storeManager =(StoreManager)SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(storeDetail.getStore_id());
		store.setOpen_shop_time(storeDetail.getOpen_shop_time());
		store.setEstate(storeDetail.getEstate());
		store.setSuperMicro(storeDetail.getSuperMicro());
		preObject(store);
		storeManager.saveObject(store);
		//根据门店id查找门店详情
		StoreDetail detail = findStoreDetailBystore_id(storeDetail.getStore_id());
		if(detail!=null){
			detail.setOwnership(storeDetail.getOwnership());
			detail.setStore_area(storeDetail.getStore_area());
			detail.setUnit_price(storeDetail.getUnit_price());
			detail.setAgency_fee(storeDetail.getAgency_fee());
			detail.setRent_free_start_data(storeDetail.getRent_free_start_data());
			detail.setRent_free_end_data(storeDetail.getRent_free_end_data());
			detail.setPayment_method(storeDetail.getPayment_method());
			detail.setSign_date(storeDetail.getSign_date());
			detail.setSubmit_date(storeDetail.getSubmit_date());
			detail.setAudit_date(storeDetail.getAudit_date());
			detail.setEnter_date(storeDetail.getEnter_date());
			detail.setPlace_date(storeDetail.getPlace_date());
			detail.setProp_date(storeDetail.getProp_date());
			detail.setAccept_date(storeDetail.getAccept_date());
			detail.setBusiness_license_date(storeDetail.getBusiness_license_date());
			detail.setFood_circulation_date(storeDetail.getFood_circulation_date());
			detail.setGather_start_date(storeDetail.getGather_start_date());
			detail.setGather_end_date(storeDetail.getGather_end_date());
			detail.setLine_date(storeDetail.getLine_date());
			detail.setBusiness_pic(storeDetail.getBusiness_pic());
			detail.setFood_pic(storeDetail.getFood_pic());
			preObject(detail);
			storeDetailManager.saveObject(detail);
			return detail;
		}else{
			preObject(storeDetail);
			storeDetailManager.saveObject(storeDetail);
		}
		return detail;
	}

	@Override
	public StoreDetail findStoreDetailById(Long id) {
		StoreDetail storeDetail=null;
		IFilter filter = FilterFactory.getSimpleFilter("id="+id);
        List<StoreDetail> list = (List<StoreDetail>) this.getList(filter);
        if(list.size()>0 && list !=null){
        	storeDetail = list.get(0);
        	StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
        	Store store = storeManager.findStore(storeDetail.getStore_id());
        	storeDetail.setName(store.getName());
        	storeDetail.setSuperMicro(store.getSuperMicro());
        	storeDetail.setOpen_shop_time(store.getOpen_shop_time());
        	storeDetail.setEstate(store.getEstate());
        }
		return storeDetail;
	}

	@Override
	public File exprotStoreDetail() throws Exception{
		String str_file_name = "export_storedetail_list";
		String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
		//配置文件中的路径
		String str_filepath = strRootpath.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));
		File file_template = new File(str_filepath);
		
		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_newfilepath = str_file_dir_path + "storedetail_list_info.xls";
		File file_new = new File(str_newfilepath);
		if (file_new.exists()) {
			file_new.delete();
		}
		FileCopyUtils.copy(file_template, file_new);
		FileInputStream fis_input_excel = new FileInputStream(file_new);
		FileOutputStream fis_out_excel = null;
		Workbook wb_humaninfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
		try {
			setCellStyle_common(wb_humaninfo);
			Sheet sh_job = wb_humaninfo.getSheetAt(0);
			int nJobIndex = 3;
			StoreDetailDao storeDetailDao = (StoreDetailDao) SpringHelper.getBean(StoreDetailDao.class.getName());
			StringBuffer sb_where=new StringBuffer();
			//取得当前登录人 所管理城市
	  		String cityssql = "";
	  		UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
	  		List<DistCity> distCityList = userManager.getCurrentUserCity();
	  		if(distCityList!=null&&distCityList.size()>0){
	  			for(DistCity d:distCityList){
	  				cityssql += "'"+d.getCityname()+"',";
	  			}
	  			cityssql=cityssql.substring(0, cityssql.length()-1);
	  		}
	        
	  		if(cityssql!=""&&cityssql.length()>0){
	  			sb_where.append(" and stt.city_name in ("+cityssql+")");
			}else{
				sb_where.append(" and 0=1 ");
			}
	        System.out.println(sb_where);
	        List<Map<String, Object>> bussinessInfo = storeDetailDao.exportStoreDetail(sb_where.toString());
			for (Map<String, Object> map : bussinessInfo) {
				Row obj_row = null;
				int cellIndex = 0;
				sh_job.createRow(nJobIndex);
				obj_row = sh_job.getRow(nJobIndex);
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue((nJobIndex-2)));//序号
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("countyname")));//区域
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("town_name")));//街道
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("ownership")));//房屋产权属性
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("address")));//位置
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("store_area")));//建筑面积
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("unit_price")));//租赁单价
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("agency_fee")));//中介费
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("rent_free_data")));//免租期
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("payment_method")));//付款方式
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("sign_date")));//门店租赁合同签约时间
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("storetypename")));//属性
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("superMicro")));//是否有微超
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("submit_date")));//功能布局图提交日期
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("audit_date")));//功能布局图审核通过日期
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("enter_date")));//装修进场日期
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("place_date")));//设备下单日期
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("prop_date")));//道具下单日期
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("accept_date")));//装修验收合格日期
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("business_license_date")));//营业执照发放日期
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("food_circulation_date")));//食品流通日期
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("gather_start_date")));//信息采集开始日期
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("gather_end_date")));//信息采集提交日期
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("line_date")));//线上开通日期	        	
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("estate")));//目前状态
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("open_shop_time")));//门店开业时间
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("name")));//店名
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("ename")));//店长	        	
	        	setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(map.get("phone")));//门店电话
	        	nJobIndex++;
			}
			fis_out_excel = new FileOutputStream(file_new);
			wb_humaninfo.write(fis_out_excel);
			
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
	 /**
     * 获取配置文件
     * @return 配置文件对象
     */
    public PropertiesValueUtil getPropertiesValueUtil(){
        if(propertiesValueUtil==null){
            propertiesValueUtil = new PropertiesValueUtil(File.separator+"conf"+File.separator+"download_config.properties");
        }
        return propertiesValueUtil;
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
        Font font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 10);
        cellStyle_common.setFont(font);
    }
    public CellStyle getCellStyle_common(){
        return cellStyle_common;
    }
    public void setCellValue(Row obj_row,int nCellIndex,Object value){
        Cell cell=obj_row.createCell(nCellIndex);
        cell.setCellStyle(getCellStyle_common());
        cell.setCellValue(new HSSFRichTextString(value==null?null:value.toString()));
    }

	@Override
	public StoreDetail findStoreDetailBystore_id(Long store_id) {
		StoreDetail storeDetail=null;
		IFilter filter = FilterFactory.getSimpleFilter("store_id="+ store_id);
        List<StoreDetail> list = (List<StoreDetail>) this.getList(filter);
        if(list.size()>0 && list !=null){
        	storeDetail = list.get(0);
        	StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
        	Store store = storeManager.findStore(storeDetail.getStore_id());
        	storeDetail.setName(store.getName());
        	storeDetail.setSuperMicro(store.getSuperMicro());
        	storeDetail.setOpen_shop_time(store.getOpen_shop_time());
        	storeDetail.setEstate(store.getEstate());
        }else{
        	storeDetail=new StoreDetail();
        	StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
        	Store store = storeManager.findStore(store_id);
        	if(store!=null){
        		storeDetail.setName(store.getName());
            	storeDetail.setSuperMicro(store.getSuperMicro());
            	storeDetail.setOpen_shop_time(store.getOpen_shop_time());
            	storeDetail.setEstate(store.getEstate());
            	storeDetail.setStore_id(store.getStore_id());
        	}
        }
        return storeDetail;
	}

}
