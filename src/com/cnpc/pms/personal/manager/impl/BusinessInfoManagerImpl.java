package com.cnpc.pms.personal.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
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
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.dao.BusinessInfoDao;
import com.cnpc.pms.personal.dao.NewsDao;
import com.cnpc.pms.personal.dao.PersonDutyDao;
import com.cnpc.pms.personal.entity.BusinessInfo;
import com.cnpc.pms.personal.entity.BusinessInfoBack;
import com.cnpc.pms.personal.entity.BusinessType;
import com.cnpc.pms.personal.entity.City;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.News;
import com.cnpc.pms.personal.entity.Office;
import com.cnpc.pms.personal.entity.PersonDuty;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.BusinessInfoBackManager;
import com.cnpc.pms.personal.manager.BusinessInfoManager;
import com.cnpc.pms.personal.manager.BusinessTypeManager;
import com.cnpc.pms.personal.manager.CityManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.NewsManager;
import com.cnpc.pms.personal.manager.PersonDutyManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.personal.util.DataTransfromUtil;
import com.cnpc.pms.utils.ValueUtil;

public class BusinessInfoManagerImpl extends BizBaseCommonManager implements BusinessInfoManager{
	
	@Override
	public File exportBusiNessList(Map<String,String> param) throws Exception {
		
		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_newfilepath = str_file_dir_path + "business_list.xls";
		File file_new = new File(str_newfilepath);
		if (file_new.exists()) {
			file_new.delete();
		}
		FileOutputStream fis_out_excel = null;
		// 表头数据（确保name 和 key 一一对应）
		String[] tHeadName = {"编号","省份","城市","区县","街道","社区","社区gb_code","具体地址",
				"商家名称","二级指标","三级指标","四级指标","五级指标",
				"经营规模（平方米，m2）","距离月店（m）","是否配送","营业开始时间","营业结束时间","门店名","国安侠"};
		String[] tHeadKey = {"id","province_name","city_name","county_name","town_name","village_name","village_gb_code","address",
				"business_name","two_level_index","three_level_index","four_level_index","five_level_index",
				"shop_area","range_eshop","isdistribution","start_business_house","end_business_house","store_name","employee_name"};
		
		BusinessInfoDao businessInfoDao = (BusinessInfoDao) SpringHelper.getBean(BusinessInfoDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        for ( String key: param.keySet()) {
        	 if ("province_name".equals(key)) {
                 sb_where.append(" AND pro.name like '%").append(param.get(key)).append("%'");
             }
             if ("town_name".equals(key)) {
             	sb_where.append(" AND town.name like '%").append(param.get(key)).append("%'");
 			}
             if ("village_name".equals(key)) {
             	sb_where.append(" AND vill.name like '%").append(param.get(key)).append("%'");
 			}if ("store_name".equals(key)) {
             	sb_where.append(" AND store.name like '%").append(param.get(key)).append("%'");
 			}
 			if ("business_id".equals(key)) {
             	sb_where.append(" AND bus.id = ").append(param.get(key)).append(" ");
 			}
             
		}
        System.out.println(sb_where);
        List<Map<String, Object>> bussinessInfo = businessInfoDao.getBussinessInfo(sb_where.toString());
        
        try {
			Workbook officeWorkBook = this.resultSetToExcel(bussinessInfo, "office_lsit", tHeadName, tHeadKey);
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
	
	/**
	 * 生成我rkbook
	 * 
	 * @param resultList
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public Workbook resultSetToExcel(List<Map<String, Object>> resultList, String sheetName, String[] tHeadName,
			String[] tHeadKey) throws Exception {
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

		for (int i = 0; i < tHeadName.length; i++) {
			setCellValue(row, i, tHeadName[i], style);
		}
		rowIndex++;
		/**
		 * 设置数据
		 */
		// 数据样式
		style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
		style.setWrapText(true);
		for (Map<String, Object> map : resultList) {
			row = sheet.createRow(rowIndex);
				for (int i = 0; i < tHeadKey.length; i++) {
					setCellValue(row, i, map.get(tHeadKey[i]), style);
				}
			rowIndex++;
		}
		return workbook;
	}
	
	public void setCellValue(Row row, int nCellIndex, Object value, CellStyle style) {
		Cell cell = row.createCell(nCellIndex);
		cell.setCellStyle(style);
		cell.setCellValue(new HSSFRichTextString(value == null ? null : value.toString()));
	}

	
	
	
	@Override
	public Map<String, Object> getBussinessInfoList(QueryConditions conditions) {
		BusinessInfoDao businessInfoDao = (BusinessInfoDao) SpringHelper.getBean(BusinessInfoDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();

        for (Map<String, Object> map_where : conditions.getConditions()) {
            if ("province_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND pro.name like '").append(map_where.get("value")).append("'");
            }
            if ("town_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND town.name like '").append(map_where.get("value")).append("'");
			}
            if ("village_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND vill.name like '").append(map_where.get("value")).append("'");
			}  
            if ("store_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	String string = DataTransfromUtil.getTownIdByStoreName(map_where.get("value")+"");
				if(string.length()>0){
					sb_where.append(" AND bus.town_id in ( ").append(string).append(")");
				}else{
					sb_where.append(" AND 1=0 ");
				}
			}
            if ("business_id".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	String id = map_where.get("value").toString().replace("%", "");
            	sb_where.append(" AND bus.id = '").append(id).append("'");
			}
            if ("business_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	String id = map_where.get("value").toString().replace("%", "");
            	 String string = map_where.get("value").toString().replaceAll("\'", "\'\'");
            	sb_where.append(" AND bus.business_name like '").append(string).append("'");
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
   					sb_where.append(" AND bus.town_id in (").append(store.getTown_id()).append(") ");
   				}else{
   					sb_where.append(" AND bus.town_id=").append(0);
   				}
   				
   			}
   		}else if(3231==sessionUser.getUsergroup().getId()||3223==sessionUser.getUsergroup().getId()||3225==sessionUser.getUsergroup().getId()||3229==sessionUser.getUsergroup().getId()){
   			Store store = storeManager.findStore(sessionUser.getStore_id());
   			if(store!=null){
   				if(store.getTown_id()!=null&&!store.getTown_id().equals("")){
   					sb_where.append(" AND bus.town_id in (").append(store.getTown_id()).append(") ");
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
        map_result.put("header", "商业信息");
        map_result.put("data", businessInfoDao.getBusinessInfoList(sb_where.toString(), obj_page));
        return map_result;
	}

	@Override
	public Result saveBussinessInfo(BusinessInfo businessInfo) {
		Result res = new Result();
		if("暂无信息".equals(businessInfo.getIsdistribution())){
			businessInfo.setIsdistribution("");
		}
		BusinessInfo infoBy = getBusinessInfoBy(businessInfo.getVillage_id(),businessInfo.getBusiness_name().replaceAll("\'", "\'\'"),businessInfo.getAddress());
		if(infoBy==null){
			infoBy = new BusinessInfo();
			infoBy.setCreate_user(businessInfo.getCreate_user());
			infoBy.setCreate_user_id(businessInfo.getCreate_user_id());
			
		}
		
		infoBy.setUpdate_user(businessInfo.getCreate_user());
		infoBy.setUpdate_user_id(businessInfo.getCreate_user_id());
		BeanUtils.copyProperties(businessInfo, infoBy
				,new String[]{"id","version","status","create_user","create_user_id","create_time"
							,"update_user","update_user_id","update_time"});
		BusinessTypeManager businessTypeManager=(BusinessTypeManager)SpringHelper.getBean("businessTypeManager");
		BusinessType businessType = businessTypeManager.getBusinessTypeByCondition(businessInfo.getTwo_level_code(), businessInfo.getThree_level_code(), businessInfo.getFour_level_code(), businessInfo.getFive_level_code());
		if(businessType!=null){
			infoBy.setType_id(businessType.getId());
		}
		preObject(infoBy);
		if(infoBy.getUpdate_user()==null){
			infoBy.setUpdate_user(infoBy.getCreate_user());
		}
    	save(infoBy);
    	res.setCode(200);
		res.setMessage("保存成功");
		return res;
	}

	@Override
	public BusinessInfo getBusinessInfoBy(Long id, String name, String address) {
		List<?> list = this.getList(FilterFactory.getSimpleFilter("village_id = "+id + " and business_name = '"+name+"' and address='"+address+"' and status=0"));
		if(list!=null&&list.size()>0){
			return (BusinessInfo)list.get(0);
		}
		return null;
	}
	
	@Override
	public BusinessInfo getBusinessInfoById(Long id) {
		IFilter simpleFilter = FilterFactory.getSimpleFilter("id",id);
		BusinessInfo businessInfo = (BusinessInfo) getUnique(simpleFilter);
		if(businessInfo!=null){
			if(businessInfo.getVillage_id()!=null&&!businessInfo.getVillage_id().equals("")&&businessInfo.getVillage_id()!=0){
				VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
				 Village idInfo = villageManager.getVillageByIdInfo(businessInfo.getVillage_id());
				 businessInfo.setVillage_name(idInfo.getName());
			}
			 if(businessInfo.getJob()!=null&&!"".equals(businessInfo.getJob())){
	        	 CustomerManager customerManager=(CustomerManager)SpringHelper.getBean("customerManager");
	        	 String stts="";
	        	 String[] split = businessInfo.getJob().split(",");
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
	        	 businessInfo.setShowSelectName(stts.substring(1,stts.length()));
	         }
			
			
			return businessInfo;
		}
		return null;
	}
	//添加时验证是否存在
	@Override
	public Result getBussinessInfo(BusinessInfo businessInfo) {
		Result res = new Result();
		BusinessInfo infoBy = getBusinessInfoBy(businessInfo.getVillage_id(),businessInfo.getBusiness_name().replaceAll("\'", "\'\'"),businessInfo.getAddress());
		if(infoBy!=null){
			res.setCode(300);
			res.setMessage( businessInfo.getBusiness_name()+ "已存在");
			return res;
		}
		res.setCode(200);
		res.setMessage( businessInfo.getBusiness_name()+ "不存在");
		return res;
	}
	
	
	@Override
	public int saveBusinessInfo(BusinessInfo businessInfo){
		User sessionUser = null;
		if (null != SessionManager.getUserSession()
				&& null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession()
					.getSessionData().get("user");
		}
		BusinessInfo result = null;
		if (businessInfo.getId() != null) {
			result = (BusinessInfo) this.getUnique(FilterFactory.getSimpleFilter("id="+ businessInfo.getId()));
			result.setAddress(businessInfo.getAddress());
			result.setBusiness_name(businessInfo.getBusiness_name());
			result.setEnd_business_house(businessInfo.getEnd_business_house());
			result.setStart_business_house(businessInfo.getStart_business_house());
			BusinessTypeManager businessTypeManager=(BusinessTypeManager)SpringHelper.getBean("businessTypeManager");
			BusinessType condition = businessTypeManager.getBusinessTypeByCondition(businessInfo.getTwo_level_code(),businessInfo.getThree_level_code(),businessInfo.getFour_level_code(),businessInfo.getFive_level_code());
			if(condition==null){
				return 0;
			}
			result.setVillage_id(businessInfo.getVillage_id());
			result.setTown_id(businessInfo.getTown_id());
			result.setTwo_level_index(condition.getLevel1_name());
			result.setThree_level_index(condition.getLevel2_name());
			result.setFour_level_index(condition.getLevel3_name());
			result.setFive_level_index(condition.getLevel4_name());
			result.setTwo_level_code(condition.getLevel1_code());
			result.setThree_level_code(condition.getLevel2_code());
			result.setFour_level_code(condition.getLevel3_code());
			result.setFive_level_code(condition.getLevel4_code());
			result.setType_id(condition.getId());
			result.setRange_eshop(businessInfo.getRange_eshop());
			result.setShop_area(businessInfo.getShop_area());
			result.setIsdistribution(businessInfo.getIsdistribution());
			result.setJob(businessInfo.getJob());
		}else{
			 result = new BusinessInfo();
			result.setAddress(businessInfo.getAddress());
			result.setBusiness_name(businessInfo.getBusiness_name());
			result.setEnd_business_house(businessInfo.getEnd_business_house());
			result.setStart_business_house(businessInfo.getStart_business_house());
			BusinessTypeManager businessTypeManager=(BusinessTypeManager)SpringHelper.getBean("businessTypeManager");
			BusinessType condition = businessTypeManager.getBusinessTypeByCondition(businessInfo.getTwo_level_code(),businessInfo.getThree_level_code(),businessInfo.getFour_level_code(),businessInfo.getFive_level_code());
			if(condition==null){
				return 0;
			}
			result.setEmployee_no(sessionUser.getEmployeeId());
			result.setVillage_id(businessInfo.getVillage_id());
			result.setTown_id(businessInfo.getTown_id());
			result.setTwo_level_index(condition.getLevel1_name());
			result.setThree_level_index(condition.getLevel2_name());
			result.setFour_level_index(condition.getLevel3_name());
			result.setFive_level_index(condition.getLevel4_name());
			result.setTwo_level_code(condition.getLevel1_code());
			result.setThree_level_code(condition.getLevel2_code());
			result.setFour_level_code(condition.getLevel3_code());
			result.setFive_level_code(condition.getLevel4_code());
			result.setType_id(condition.getId());
			result.setRange_eshop(businessInfo.getRange_eshop());
			result.setShop_area(businessInfo.getShop_area());
			result.setIsdistribution(businessInfo.getIsdistribution());
			result.setJob(businessInfo.getJob());
		}
		preSaveObject(result);
		saveObject(result);
		updateOrinsertDuty(result.getId(),result.getJob(),3,sessionUser.getEmployeeId());
		
		/*if (businessInfo.getId() != null) {
			BusinessInfoBackManager businessInfoBackManager=(BusinessInfoBackManager)SpringHelper.getBean("businessInfoBackManager");
			BusinessInfoBack bybusiness_id = businessInfoBackManager.getBusinessInfoBackBybusiness_id(result.getId());
			NewsDao newsDao=(NewsDao)SpringHelper.getBean(NewsDao.class.getName());
			newsDao.deleteObject(bybusiness_id);
			NewsManager newsManager=(NewsManager)SpringHelper.getBean("newsManager");
			News news = newsManager.getNewsBy(3, businessInfo.getId());
			newsDao.deleteObject(news);
		}*/
		
		
		return 1;
		
//		update t_town_business_info 
//		set address = '',
//		SET business_name = '',
//		SET two_level_index = '',
//		set three_level_index = '',
//		set four_level_index = '',
//		set five_level_index = '',
//		set range_eshop = '',
//		SET shop_area = '',
//		set start_business_house = '',
//		SET end_business_house = '',
//		SET isdistribution = ''
//		where id = '';
		
//		StringBuffer sb = new StringBuffer();
//		sb.append("update t_town_business_info ");
//		sb.append("set address = '"+businessInfo.getAddress()+"', ");
//		sb.append("business_name = '"+businessInfo.getBusiness_name()+"', ");
//		sb.append(" two_level_index = '"+businessInfo.getTwo_level_index()+"', ");
//		sb.append(" three_level_index = '"+businessInfo.getThree_level_index()+"', ");
//		sb.append(" four_level_index = '"+businessInfo.getFour_level_index()+"', ");
//		sb.append(" five_level_index = '"+businessInfo.getFive_level_index()+"', ");
//		sb.append(" range_eshop = '"+businessInfo.getRange_eshop()+"', ");
//		sb.append(" shop_area = '"+businessInfo.getShop_area()+"', ");
//		sb.append(" start_business_house = '"+businessInfo.getStart_business_house()+"', ");
//		sb.append(" end_business_house = '"+businessInfo.getEnd_business_house()+"', ");
//		sb.append(" isdistribution = '"+businessInfo.getIsdistribution()+"'  ");
//		sb.append("where id = "+businessInfo.getId());
//		BusinessInfoDao businessInfoDao = (BusinessInfoDao) SpringHelper.getBean(BusinessInfoDao.class.getName());
//		
//		
//		return businessInfoDao.updateData(sb.toString());
		
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
					}
				}
				dataEntity.setUpdate_time(sdate);
				if (null != sessionUser) {
					dataEntity.setUpdate_user(sessionUser.getName());
					dataEntity.setUpdate_user_id(sessionUser.getId());
				}
			}
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

		@Override
		public void deleteBusinessById(Long id) {
			BusinessInfoManager businessInfoManager=(BusinessInfoManager)SpringHelper.getBean("businessInfoManager");
			BusinessInfo businessInfo = businessInfoManager.getBusinessInfoById(id);
			businessInfo.setStatus(1);
			preObject(businessInfo);
			businessInfoManager.saveObject(businessInfo);
			
		}

		@Override
		public Result findBusinessInfoList(BusinessInfo businessInfo) {
			VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
			TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
			Result result = new Result();
			List<BusinessInfo> list = new ArrayList<BusinessInfo>();
			 IFilter filter = FilterFactory.getSimpleFilter("1=1 and status=0 ");

		        if(ValueUtil.checkValue(businessInfo.getBusiness_name())){
		            filter = filter.appendAnd(FilterFactory.getEq("business_name",businessInfo.getBusiness_name()));
		        }

		        if(ValueUtil.checkValue(businessInfo.getAddress())){
		            filter =  filter.appendAnd(FilterFactory.getEq("address",businessInfo.getAddress()));
		        }
		        List<?> lst_result = this.getList(filter);
		        result.setCode(CodeEnum.success.getValue());
		        result.setMessage(CodeEnum.success.getDescription());
		        if(lst_result == null || lst_result.size() == 0){
		            result.setListBusinessInfo(new ArrayList<BusinessInfo>());
		        }else{
		        	List<BusinessInfo> listbus=(List<BusinessInfo>) lst_result;
		        	for (BusinessInfo business : listbus) {
		        		if(business.getVillage_id()!=null){
		        			Map<String, Object> map = villageManager.getVillageTownInfoByVillage_id(business.getVillage_id());
		        			business.setProvince_name(map.get("province_name").toString());
		        			business.setProvince_id(Long.valueOf(String.valueOf(map.get("province_id").toString())).longValue());
		        			business.setCity_id(Long.valueOf(String.valueOf(map.get("city_id").toString())).longValue());
		        			business.setCity_name(map.get("city_name").toString());
		        			business.setVillage_id(Long.valueOf(String.valueOf(map.get("village_id").toString())).longValue());
		        			business.setVillage_name(map.get("village_name").toString());
		        			business.setTown_id(Long.valueOf(String.valueOf(map.get("town_id").toString())).longValue());
		        			business.setTown_name(map.get("town_name").toString());
		        			business.setCounty_id(Long.valueOf(String.valueOf(map.get("county_id").toString())).longValue());
		        			business.setCounty_name(map.get("county_name").toString());
		        			list.add(business);
		        		}else if(business.getTown_id()!=null){
		        			Map<String, Object> map = townManager.getTownParentInfoByTown_id(business.getTown_id());
		        			business.setProvince_name(map.get("province_name").toString());
		        			business.setProvince_id(Long.valueOf(String.valueOf(map.get("province_id").toString())).longValue());
		        			business.setCity_id(Long.valueOf(String.valueOf(map.get("city_id").toString())).longValue());
		        			business.setCity_name(map.get("city_name").toString());
		        			business.setTown_id(Long.valueOf(String.valueOf(map.get("town_id").toString())).longValue());
		        			business.setTown_name(map.get("town_name").toString());
		        			business.setCounty_id(Long.valueOf(String.valueOf(map.get("county_id").toString())).longValue());
		        			business.setCounty_name(map.get("county_name").toString());
		        			list.add(business);
		        		}
		        		
					}
		            result.setListBusinessInfo(list);
		        }   
			return result;
		}

		@Override
		public Result getBussinessInfoData(BusinessInfo businessInfo) {
				Result res = new Result();
				BusinessInfo infoBy = getBusinessInfoBy(businessInfo.getVillage_id(),businessInfo.getBusiness_name().replaceAll("\'", "\'\'"),businessInfo.getAddress());
				if(infoBy!=null){
					if(businessInfo.getId()!=null){
						if(!infoBy.getId().equals(businessInfo.getId())){
							res.setCode(300);
							res.setMessage( businessInfo.getBusiness_name()+ "已存在");
							return res;
						}
					}
					if(businessInfo.getId()==null){
						res.setCode(300);
						res.setMessage( businessInfo.getBusiness_name()+ "已存在");
						return res;
					}
				}
				res.setCode(200);
				res.setMessage( businessInfo.getBusiness_name()+ "不存在");
				return res;
		}

		@Override
		public Result saveBussinessInfoData(BusinessInfo businessInfo) {
			BusinessInfoManager businessInfoManager=(BusinessInfoManager)SpringHelper.getBean("businessInfoManager");
			Result res = new Result();
			if("暂无信息".equals(businessInfo.getIsdistribution())){
				businessInfo.setIsdistribution("");
			}
			BusinessInfo infoBy = getBusinessInfoBy(businessInfo.getVillage_id(),businessInfo.getBusiness_name().replaceAll("\'", "\'\'"),businessInfo.getAddress());
			if(businessInfo.getId()!=null){
				BusinessInfo updateBuss = getBusinessInfoById(businessInfo.getId());
				BeanUtils.copyProperties(businessInfo, updateBuss
						,new String[]{"id","version","status","create_user","create_user_id","create_time"
									,"update_user","update_user_id","update_time","employee_no","job"});
				preObject(updateBuss);
				updateBuss.setUpdate_user(businessInfo.getCreate_user());
				if(updateBuss.getEmployee_no()==null){
					if(infoBy!=null&&infoBy.getEmployee_no()!=null){
						updateBuss.setEmployee_no(infoBy.getEmployee_no());
					}else{
						updateBuss.setEmployee_no(businessInfo.getEmployee_no());
					}
				}
				BusinessTypeManager businessTypeManager=(BusinessTypeManager)SpringHelper.getBean("businessTypeManager");
				BusinessType businessType = businessTypeManager.getBusinessTypeByCondition(businessInfo.getTwo_level_code(), businessInfo.getThree_level_code(), businessInfo.getFour_level_code(), businessInfo.getFive_level_code());
				if(businessType!=null){
					updateBuss.setType_id(businessType.getId());
				}
				businessInfoManager.saveObject(updateBuss);
				if(infoBy!=null&&!infoBy.getId().equals(updateBuss.getId())){
					NewsDao newsDao=(NewsDao)SpringHelper.getBean(NewsDao.class.getName());
					newsDao.deleteObject(infoBy);
				}
			}else{
				if(infoBy==null){
					infoBy = new BusinessInfo();
					infoBy.setCreate_user(businessInfo.getCreate_user());
					infoBy.setCreate_user_id(businessInfo.getCreate_user_id());
				}
				infoBy.setUpdate_user(businessInfo.getCreate_user());
				infoBy.setUpdate_user_id(businessInfo.getCreate_user_id());
				BeanUtils.copyProperties(businessInfo, infoBy
						,new String[]{"id","version","status","create_user","create_user_id","create_time"
									,"update_user","update_user_id","update_time","employee_no","job"});
				if(infoBy.getEmployee_no()==null){
					infoBy.setEmployee_no(businessInfo.getEmployee_no());
				}
				BusinessTypeManager businessTypeManager=(BusinessTypeManager)SpringHelper.getBean("businessTypeManager");
				BusinessType businessType = businessTypeManager.getBusinessTypeByCondition(businessInfo.getTwo_level_code(), businessInfo.getThree_level_code(), businessInfo.getFour_level_code(), businessInfo.getFive_level_code());
				if(businessType!=null){
					infoBy.setType_id(businessType.getId());
				}
				preObject(infoBy);
				if(infoBy.getUpdate_user()==null){
					infoBy.setUpdate_user(infoBy.getCreate_user()); 
				}
				businessInfoManager.saveObject(infoBy);
			}
	    	res.setCode(200);
			res.setMessage("保存成功");
			return res;
		}

		@Override
		public BusinessInfo verisionBusinessInfo(BusinessInfo businessInfo) {
			BusinessInfo info = getBusinessInfoBy(businessInfo.getVillage_id(),businessInfo.getBusiness_name(),businessInfo.getAddress());
			return info;
		};
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
		
		
		
		
}
