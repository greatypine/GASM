package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.paging.*;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.messageModel.dao.MessageNewDao;
import com.cnpc.pms.messageModel.entity.Message;
import com.cnpc.pms.messageModel.manager.MessageNewManager;
import com.cnpc.pms.personal.dao.WorkRecordTotalDao;
import com.cnpc.pms.personal.entity.*;
import com.cnpc.pms.personal.manager.DsTopDataManager;
import com.cnpc.pms.personal.manager.ScoreRecordManager;
import com.cnpc.pms.personal.manager.ScoreRecordTotalManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.WorkInfoManager;
import com.cnpc.pms.personal.manager.WorkMonthManager;
import com.cnpc.pms.personal.manager.WorkRecordTotalManager;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.MessagePushThread;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class WorkRecordTotalManagerImpl extends BizBaseCommonManager implements WorkRecordTotalManager{

	PropertiesValueUtil propertiesValueUtil = null;

	private String folder_path = null;

	CellStyle cellStyle_common = null;
	CellStyle cellLeftStyle_common = null;
	CellStyle cellStrongCenterStyle_common=null;

	@Override
	public Map<String, Object> queryWorkRecordTotalBySubmit(QueryConditions conditions) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		UserManager userManager = (UserManager)SpringHelper.getBean("userManager");

		Map<Long,Store> map_store = new HashMap<Long, Store>();

		String store_name = null;
		StringBuilder sb_where = new StringBuilder();
		sb_where.append("1=1 and commit_status in (1,3)");

		
		
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
			sb_where.append(" and cityname in ("+cityssql+")");
		}else{
			sb_where.append(" and 0=1 ");
		}

		
		
		for(Map<String,Object> condition : conditions.getConditions()){
			if("store_name".equals(condition.get("key")) && ValueUtil.checkValue(condition.get("value"))){
				store_name = condition.get("value").toString();
				List<Store> storeList = storeManager.findStoreListByName(store_name);
				if(storeList != null && storeList.size() > 0){
					StringBuilder sb_ids = new StringBuilder();
					for(Store store : storeList){
						map_store.put(store.getStore_id(),store);
						if(sb_ids.length() > 0){
							sb_ids.append(",'"+store.getStore_id()+"'");
						}else{
							sb_ids.append("'"+store.getStore_id()+"'");
						}
					}
					sb_where.append(" and store_id in (").append(sb_ids.toString()).append(")");
				}else{
					sb_where.append(" and 0 = 1");
				}
			}

			if("start_date".equals(condition.get("key")) && ValueUtil.checkValue(condition.get("value"))){
				sb_where.append(" and commit_date >= date_format('").append(condition.get("value").toString()).append("','%Y-%m-%d')");
			}

			if("end_date".equals(condition.get("key")) && ValueUtil.checkValue(condition.get("value"))){
				sb_where.append(" and commit_date <= date_format('").append(condition.get("value").toString()).append("','%Y-%m-%d')");
			}

			if("work_month".equals(condition.get("key")) && ValueUtil.checkValue(condition.get("value"))){
				sb_where.append(" and work_month = '").append(condition.get("value").toString()).append("'");
			}
		}

		FSP fsp = new FSP();
		IFilter filter = FilterFactory.getStringFilter(sb_where.toString());
		fsp.setUserFilter(filter);
		fsp.setPage(conditions.getPageinfo());
		fsp.setSort(SortFactory.createSort("commit_status", ISort.ASC));
		List<?> lst_total_data = this.getList(filter);
		if(lst_total_data != null && lst_total_data.size() > 0){
			conditions.getPageinfo().setTotalRecords(lst_total_data.size());
		}
		List<WorkRecordTotal> lst_data = (List<WorkRecordTotal>)this.getList(fsp);
		if(lst_data != null && lst_data.size() > 0){
			for(WorkRecordTotal total : lst_data){
				if(!map_store.containsKey(total.getStore_id())){
					Store store = storeManager.findStore(total.getStore_id());
					map_store.put(store.getStore_id(),store);
				}
				total.setStore_name(map_store.get(total.getStore_id()).getName());
				total.setStr_commit_date(dateFormat.format(total.getCommit_date()));
				if (total.getCommit_status() == 0){
					total.setStr_commit_status("已保存");
				}else if (total.getCommit_status() == 1){
					total.setStr_commit_status("待审批");
				}else if (total.getCommit_status() == 2){
					total.setStr_commit_status("已退回");
				}else if (total.getCommit_status() == 3){
					total.setStr_commit_status("已通过");
				}
			}
		}
		Map<String,Object> map_result = new HashMap<String,Object>();
		map_result.put("header","");
		map_result.put("pageinfo",conditions.getPageinfo());
		map_result.put("data",lst_data);
		return map_result;
	}

	@Override
	public WorkRecordTotal updateCommitStatus(Long id){
		Date d = new Date();
		WorkRecordTotal workRecordTotal = (WorkRecordTotal) this.getObject(id);
		workRecordTotal.setCommit_date(d);
		workRecordTotal.setCommit_status(Long.parseLong("1"));

		UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = userManager.getCurrentUserDTO();
		workRecordTotal.setSubmit_user_id(userDTO.getId());
		workRecordTotal.setUpdate_user(userDTO.getName());
		workRecordTotal.setUpdate_time(d);
		workRecordTotal.setUpdate_user_id(userDTO.getId());
		saveObject(workRecordTotal);
		return workRecordTotal;
	}

	@Override
	public WorkRecordTotal queryWorkRecordTotal(String work_month,Long store_id){
		UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
		WorkRecordTotalManager workRecordTotalManager = (WorkRecordTotalManager)SpringHelper.getBean("workRecordTotalManager");
		if(store_id == null){
			UserDTO userDTO = userManager.getCurrentUserDTO();
			store_id = userDTO.getStore_id();
		}
		IFilter iFiltermain =FilterFactory.getSimpleFilter(" store_id="+store_id+" and work_month='"+work_month+"' ");
		List<WorkRecordTotal> lst_wrList = (List<WorkRecordTotal>)workRecordTotalManager.getList(iFiltermain);
		if(lst_wrList!=null&&lst_wrList.size()>0){
			return lst_wrList.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> queryWorkRecordTotalByCheck(QueryConditions conditions) {
		WorkMonthManager workMonthManager = (WorkMonthManager)SpringHelper.getBean("workMonthManager");
		WorkRecordTotalDao workRecordTotalDao = (WorkRecordTotalDao)SpringHelper.getBean(WorkRecordTotalDao.class.getName());
		WorkMonth workMonth = workMonthManager.getMaxWorkMonth();
		Map<String,Object> map_result = new HashMap<String,Object>();
		PageInfo pageInfo = conditions.getPageinfo();
		if(workMonth == null){
			pageInfo.setTotalRecords(0);
			map_result.put("pageinfo", pageInfo);
			map_result.put("header", "");
			map_result.put("data", new ArrayList<Object>());
		}else{
			//取得登录人 所管理的城市 
			UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
	    	StringBuffer sbfCondition = new StringBuffer();
			String cityssql = "";
			List<DistCity> distCityList = userManager.getCurrentUserCity();
			if(distCityList!=null&&distCityList.size()>0){
				for(DistCity d:distCityList){
					cityssql += "'"+d.getCityname()+"',";
				}
				cityssql=cityssql.substring(0, cityssql.length()-1);
			}
			
			map_result = workRecordTotalDao.queryWorkRecordTotalByCheck(workMonth.getWork_month(),pageInfo,cityssql);
		}
		return map_result;
	}

	@Override
	public WorkRecordTotal findWorkRecordTotalById(Long work_record_id) {
		return (WorkRecordTotal)this.getObject(work_record_id);
	}

	@Override
	public int getMaxSubmitCount(String work_month,String cityname) {
		//取得当前登录人 所管理城市
		StringBuilder sbfCondition = new StringBuilder();
		if(cityname != null && cityname.length()>0){
			sbfCondition.append(" cityname = '"+cityname+"'");
		}else{
			sbfCondition.append(" 0=1 ");
		}
		
		IFilter filter = FilterFactory.getSimpleFilter("commit_status",1L)
				.appendAnd(FilterFactory.getSimpleFilter("work_month",work_month))
				.appendAnd(FilterFactory.getSimpleFilter(sbfCondition.toString()));
		List<?> lst_data = this.getList(filter);
		if(lst_data != null){
			return lst_data.size();
		}
		return 0;
	}

	@Override
	public String saveWorkRecordForExcel(String work_month,String cityname) {
		UserManager userManager = (UserManager)SpringHelper.getBean("userManager");

		WorkMonthManager workMonthManager = (WorkMonthManager)SpringHelper.getBean("workMonthManager");
		
		DsTopDataManager dsTopDataManager = (DsTopDataManager) SpringHelper.getBean("dsTopDataManager");
		
		String str_file_name = "work_record_template";
		String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
		//配置文件中的路径
		String str_filepath = strRootpath.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));

		File file_template = new File(str_filepath);

		StringBuilder sb_condition = new StringBuilder();
		sb_condition.append("work_month = '" + work_month +"'");
		
		List<DistCity> distCityList = userManager.getCurrentUserCity();
		if(cityname == null){
			StringBuilder sb_citynames = new StringBuilder();
			for(int i = 0;i < distCityList.size();i++){
				if(i != 0){
					sb_citynames.append(",");
				}
				sb_citynames.append("'"+distCityList.get(i).getCityname()+"'");
			}
			if(sb_citynames.length() > 0){
				sb_condition.append("cityname in (" + sb_citynames +")");
			}
		}else{
			sb_condition.append(" and cityname = '" + cityname +"'");
		}
		
		List<WorkMonth> lst_workmonth = (List<WorkMonth>)workMonthManager.getList(FilterFactory.getSimpleFilter(sb_condition.toString()));
		sb_condition.append(" and commit_status in (1,3) ");
		IFilter iFiltermain = FilterFactory.getSimpleFilter(sb_condition.toString());
		List<WorkRecordTotal> lst_wrtotalList = (List<WorkRecordTotal>)this.getList(iFiltermain);

		if(lst_wrtotalList == null || lst_wrtotalList.size() == 0){
			return null;
		}
		
		WorkMonth workMonth = lst_workmonth.get(0);

		//导出绩效分
        WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
        ScoreRecordManager scoreRecordManager = (ScoreRecordManager) SpringHelper.getBean("scoreRecordManager");
        List<WorkInfo> lst_worInfos = workInfoManager.queryWorkInfosByAdopt(null, workMonth.getWork_month(), "绩效打分录入");
        List<ScoreRecord> lst_scoreRecords = null;
        if(lst_worInfos!=null&&lst_worInfos.size()>0){
        	String work_ids = "";
        	for(WorkInfo wInfo :lst_worInfos){
        		if(work_ids==""){
        			work_ids+=wInfo.getId();
        		}else{
        			work_ids+=","+wInfo.getId();
        		}
        	}
        	ScoreRecordTotalManager scoreRecordTotalManager = (ScoreRecordTotalManager) SpringHelper.getBean("scoreRecordTotalManager");
        	List<ScoreRecordTotal> scoreRecordTotal = scoreRecordTotalManager.queryScoreRecordTotalListByWorkIds(work_ids);
        	
        	if(scoreRecordTotal!=null&&scoreRecordTotal.size()>0){
        		String score_total_ids = "";
        		for(ScoreRecordTotal srRecordTotal :scoreRecordTotal){
            		if(score_total_ids==""){
            			score_total_ids+=srRecordTotal.getId();
            		}else{
            			score_total_ids+=","+srRecordTotal.getId();
            		}
            	}
        		IFilter scr =FilterFactory.getSimpleFilter(" scorerecord_id in("+score_total_ids+") ");
            	lst_scoreRecords = (List<ScoreRecord>) scoreRecordManager.getList(scr);
        	}
        }
		
		//处理月的天数
		int days = DateUtils.getDaysByMonths(work_month);
		int appendDays =0;
		switch (days) {
			case 28:
				appendDays=-3;
				break;
			case 29:
				appendDays=-2;
				break;
			case 30:
				appendDays=-1;
				break;
			case 31:
				appendDays=0;
				break;
			default:
				appendDays=0;
				break;
		}


		String str_file_dir_path = PropertiesUtil.getValue("file.root");

		String exportFileName = "work_record_"+work_month+".xls";

		String str_newfilepath = str_file_dir_path + "/"+exportFileName;

		File file_new = new File(str_newfilepath);
		if(file_new.exists()){
			file_new.delete();
		}
		try {
			FileCopyUtils.copy(file_template, file_new);
			FileInputStream fis_input_excel = new FileInputStream(file_new);
			Workbook wb_wrinfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));

			setCellStyle_common(wb_wrinfo);
			setLeftCellStyle_common(wb_wrinfo);
			setStrongCenterCellStyle_common(wb_wrinfo);

			Sheet sh_data = wb_wrinfo.getSheetAt(0);

			//标题
			Row obj_row_title = sh_data.getRow(0);
			setStrongCenterCellValue(obj_row_title, 0, ValueUtil.getStringValue(work_month+"份员工考勤情况统计表"));

			//第二行 填报单位
//			Row obj_row_org = sh_data.getRow(2);
//			//setCellValue(obj_row_org, 1, ValueUtil.getStringValue("填报单位："));
//			setStrongCenterCellValue(obj_row_org, 2, ValueUtil.getStringValue(store.getName()));

			//第三行 填报单位
			Row obj_row_title1 = sh_data.getRow(3);
			CellRangeAddress isauth=new CellRangeAddress(3, 3, 2, days+1);
			sh_data.addMergedRegion(isauth);
			setCellValue(obj_row_title1, 2, ValueUtil.getStringValue("日       期"));

			CellRangeAddress monthSum=new CellRangeAddress(3, 3, days+2, days+18);
			sh_data.addMergedRegion(monthSum);
			setCellValue(obj_row_title1, 33+appendDays, ValueUtil.getStringValue("月  累  计"));

			/*CellRangeAddress fb=new CellRangeAddress(3, 3, days+15, days+16);
			sh_data.addMergedRegion(fb);
			setCellValue(obj_row_title1, 46+appendDays, ValueUtil.getStringValue("饭补"));*/

			
			 CellRangeAddress signName=new CellRangeAddress(3, 5, 50+appendDays, 50+appendDays);
	         sh_data.addMergedRegion(signName);
	         CellRangeAddress score=new CellRangeAddress(3, 5, 51+appendDays, 51+appendDays);
	         sh_data.addMergedRegion(score);
	         
	         CellRangeAddress mixedType_repeatBuyCostomer=new CellRangeAddress(3, 5, 52+appendDays, 52+appendDays);
	         sh_data.addMergedRegion(mixedType_repeatBuyCostomer);
	         CellRangeAddress turnover=new CellRangeAddress(3, 5, 53+appendDays, 53+appendDays);
	         sh_data.addMergedRegion(turnover);
	         CellRangeAddress storeroom=new CellRangeAddress(3, 5, 54+appendDays, 54+appendDays);
	         sh_data.addMergedRegion(storeroom);
	         
	         CellRangeAddress emprise=new CellRangeAddress(3, 5, 55+appendDays, 55+appendDays);
	         sh_data.addMergedRegion(emprise);
	         
	         
			 //列宽
	         sh_data.setColumnWidth(50+appendDays, "个人签字".getBytes().length*2*156);
	         setCellValue(obj_row_title1, 50+appendDays, ValueUtil.getStringValue("个人签字"));
	        
	         sh_data.setColumnWidth(51+appendDays, "行为表现".getBytes().length*2*156);
	         setCellValue(obj_row_title1, 51+appendDays, ValueUtil.getStringValue("员工工作行为表现"));
	         sh_data.setColumnWidth(52+appendDays, "拉新客".getBytes().length*2*156);
	         setCellValue(obj_row_title1, 52+appendDays, ValueUtil.getStringValue("拉新客户数"));
	         sh_data.setColumnWidth(53+appendDays, "营业额".getBytes().length*2*156);
	         setCellValue(obj_row_title1, 53+appendDays, ValueUtil.getStringValue("营业额"));
	         sh_data.setColumnWidth(54+appendDays, "店面库房".getBytes().length*2*156);
	         setCellValue(obj_row_title1, 54+appendDays, ValueUtil.getStringValue("店面和库房管理"));
	         sh_data.setColumnWidth(55+appendDays, "重点产品增长".getBytes().length*2*156);
	         setCellValue(obj_row_title1, 55+appendDays, ValueUtil.getStringValue("重点产品增长率打分"));
	         

			//第四行 填报单位
			Row obj_row_title2 = sh_data.getRow(4);
			Row obj_row_title2_week = sh_data.getRow(5);
			//星期
			int year = Integer.parseInt(work_month.split("-")[0]);
			int month = Integer.parseInt(work_month.split("-")[1]);
			String firstWeek = DateUtils.getFirstDayWeek(year, month);
			String weekdaystr = "日一二三四五六";
			String retWeek =weekdaystr.substring(weekdaystr.indexOf(firstWeek),weekdaystr.length());
			for(int i=0;i<5;i++){
				retWeek+=weekdaystr;
			}
			retWeek=retWeek.substring(0,days);
			char[] weeks = retWeek.toCharArray();
			//日期1-31
			for(int i=1;i<(days+1);i++){
				int columnnum = 1;
				setCellValue(obj_row_title2, columnnum+i, ValueUtil.getStringValue(i+""));
				setCellValue(obj_row_title2_week, columnnum+i, ValueUtil.getStringValue(weeks[i-1]));
			}

			//月累计部分
			setCellValue(obj_row_title2, 33+appendDays, ValueUtil.getStringValue("出勤"));
			setCellValue(obj_row_title2, 34+appendDays, ValueUtil.getStringValue("倒休"));
			setCellValue(obj_row_title2, 35+appendDays, ValueUtil.getStringValue("事假"));
			setCellValue(obj_row_title2, 36+appendDays, ValueUtil.getStringValue("病假"));
			setCellValue(obj_row_title2, 37+appendDays, ValueUtil.getStringValue("婚假"));
			setCellValue(obj_row_title2, 38+appendDays, ValueUtil.getStringValue("产假"));
			setCellValue(obj_row_title2, 39+appendDays, ValueUtil.getStringValue("丧假"));
			setCellValue(obj_row_title2, 40+appendDays, ValueUtil.getStringValue("年休"));
			setCellValue(obj_row_title2, 41+appendDays, ValueUtil.getStringValue("出差"));
			setCellValue(obj_row_title2, 42+appendDays, ValueUtil.getStringValue("工伤"));
			setCellValue(obj_row_title2, 43+appendDays, ValueUtil.getStringValue("旷工"));
			setCellValue(obj_row_title2, 44+appendDays, ValueUtil.getStringValue("迟到"));
			setCellValue(obj_row_title2, 45+appendDays, ValueUtil.getStringValue("早退"));

			for(int i=33;i<50;i++){
				CellRangeAddress totalStyle=new CellRangeAddress(4, 5, i+appendDays, i+appendDays);
				sh_data.addMergedRegion(totalStyle);
			}


			 sh_data.setColumnWidth(46+appendDays, "法定加班[时]".getBytes().length*2*96);
	         setCellValue(obj_row_title2, 46+appendDays, ValueUtil.getStringValue("法定加班[时]"));
	         sh_data.setColumnWidth(47+appendDays, "饭补天数".getBytes().length*2*96);
	         setCellValue(obj_row_title2, 47+appendDays, ValueUtil.getStringValue("饭补天数"));

	         //新增加排班工时和实际工时 
	         sh_data.setColumnWidth(48+appendDays, "排班工时".getBytes().length*2*96);
	         setCellValue(obj_row_title2, 48+appendDays, ValueUtil.getStringValue("排班工时"));
	         sh_data.setColumnWidth(49+appendDays, "实际工时".getBytes().length*2*96);
	         setCellValue(obj_row_title2, 49+appendDays, ValueUtil.getStringValue("实际工时"));
	         
	         
	         //补空
	         setCellValue(obj_row_title2, 50+appendDays, ValueUtil.getStringValue(""));
	         setCellValue(obj_row_title2, 51+appendDays, ValueUtil.getStringValue(""));
	         setCellValue(obj_row_title2_week, 50+appendDays, ValueUtil.getStringValue(""));
	         setCellValue(obj_row_title2_week, 51+appendDays, ValueUtil.getStringValue(""));


			//填充人员考勤
			int nRowIndex=6;
			for(WorkRecordTotal total : lst_wrtotalList){
				for(WorkRecord workRecord : total.getChilds()){
					sh_data.createRow(nRowIndex);
					Row obj_row = sh_data.getRow(nRowIndex);

					setCellValue(obj_row, 0, ValueUtil.getStringValue(workRecord.getEmployee_no()));
					DsTopData dsTopData = dsTopDataManager.queryDSTopDataByEmployeeNo(workRecord.getEmployee_no(), workRecord.getWork_month());
    	            String username = dsTopData==null?"":dsTopData.getUsername();
					setCellValue(obj_row, 1, ValueUtil.getStringValue(username));
					setCellValue(obj_row, 2, ValueUtil.getStringValue(workRecord.getDay1()));
					setCellValue(obj_row, 3, ValueUtil.getStringValue(workRecord.getDay2()));
					setCellValue(obj_row, 4, ValueUtil.getStringValue(workRecord.getDay3()));
					setCellValue(obj_row, 5, ValueUtil.getStringValue(workRecord.getDay4()));
					setCellValue(obj_row, 6, ValueUtil.getStringValue(workRecord.getDay5()));
					setCellValue(obj_row, 7, ValueUtil.getStringValue(workRecord.getDay6()));
					setCellValue(obj_row, 8, ValueUtil.getStringValue(workRecord.getDay7()));
					setCellValue(obj_row, 9, ValueUtil.getStringValue(workRecord.getDay8()));
					setCellValue(obj_row, 10, ValueUtil.getStringValue(workRecord.getDay9()));
					setCellValue(obj_row, 11, ValueUtil.getStringValue(workRecord.getDay10()));

					setCellValue(obj_row, 12, ValueUtil.getStringValue(workRecord.getDay11()));
					setCellValue(obj_row, 13, ValueUtil.getStringValue(workRecord.getDay12()));
					setCellValue(obj_row, 14, ValueUtil.getStringValue(workRecord.getDay13()));
					setCellValue(obj_row, 15, ValueUtil.getStringValue(workRecord.getDay14()));
					setCellValue(obj_row, 16, ValueUtil.getStringValue(workRecord.getDay15()));
					setCellValue(obj_row, 17, ValueUtil.getStringValue(workRecord.getDay16()));
					setCellValue(obj_row, 18, ValueUtil.getStringValue(workRecord.getDay17()));
					setCellValue(obj_row, 19, ValueUtil.getStringValue(workRecord.getDay18()));
					setCellValue(obj_row, 20, ValueUtil.getStringValue(workRecord.getDay19()));
					setCellValue(obj_row, 21, ValueUtil.getStringValue(workRecord.getDay20()));

					setCellValue(obj_row, 22, ValueUtil.getStringValue(workRecord.getDay21()));
					setCellValue(obj_row, 23, ValueUtil.getStringValue(workRecord.getDay22()));
					setCellValue(obj_row, 24, ValueUtil.getStringValue(workRecord.getDay23()));
					setCellValue(obj_row, 25, ValueUtil.getStringValue(workRecord.getDay24()));
					setCellValue(obj_row, 26, ValueUtil.getStringValue(workRecord.getDay25()));
					setCellValue(obj_row, 27, ValueUtil.getStringValue(workRecord.getDay26()));
					setCellValue(obj_row, 28, ValueUtil.getStringValue(workRecord.getDay27()));
					setCellValue(obj_row, 29, ValueUtil.getStringValue(workRecord.getDay28()));
					setCellValue(obj_row, 30, ValueUtil.getStringValue(workRecord.getDay29()));
					setCellValue(obj_row, 31, ValueUtil.getStringValue(workRecord.getDay30()));
					setCellValue(obj_row, 32, ValueUtil.getStringValue(workRecord.getDay31()));

					setCellValue(obj_row, 33+appendDays, ValueUtil.getStringValue(workRecord.getWorkdays()));
					setCellValue(obj_row, 34+appendDays, ValueUtil.getStringValue(workRecord.getAdjholiday()));
					setCellValue(obj_row, 35+appendDays, ValueUtil.getStringValue(workRecord.getEventday()));
					setCellValue(obj_row, 36+appendDays, ValueUtil.getStringValue(workRecord.getBadday()));
					setCellValue(obj_row, 37+appendDays, ValueUtil.getStringValue(workRecord.getWedday()));
					setCellValue(obj_row, 38+appendDays, ValueUtil.getStringValue(workRecord.getProday()));
					setCellValue(obj_row, 39+appendDays, ValueUtil.getStringValue(workRecord.getLoseday()));
					setCellValue(obj_row, 40+appendDays, ValueUtil.getStringValue(workRecord.getYearholiday()));
					setCellValue(obj_row, 41+appendDays, ValueUtil.getStringValue(workRecord.getTripday()));
					setCellValue(obj_row, 42+appendDays, ValueUtil.getStringValue(workRecord.getWorkhurtday()));
					setCellValue(obj_row, 43+appendDays, ValueUtil.getStringValue(workRecord.getAbsenceday()));
					setCellValue(obj_row, 44+appendDays, ValueUtil.getStringValue(workRecord.getLateday()));
					setCellValue(obj_row, 45+appendDays, ValueUtil.getStringValue(workRecord.getLeaveearlyday()));

					setCellValue(obj_row, 46+appendDays, ValueUtil.getStringValue(workRecord.getOvertime()));
    	        	setCellValue(obj_row, 47+appendDays, ValueUtil.getStringValue(workRecord.getAllowdays()));
    	        	
    	        	//排班工时 与实际工时 
    	        	double totalovertime = workRecord.getTotalovertime();
    	        	double realovertime = workRecord.getRealovertime();
    	        	setCellValue(obj_row, 48+appendDays, ValueUtil.getStringValue(totalovertime==0?"":(int)totalovertime));
    	        	setCellValue(obj_row, 49+appendDays, ValueUtil.getStringValue(realovertime==0?"":(int)realovertime));
    	        	
    	        	
    	        	setCellValue(obj_row, 50+appendDays, ValueUtil.getStringValue(workRecord.getSignname()));
    	        	//setCellValue(obj_row, 51+appendDays, ValueUtil.getStringValue(workRecord.getScore()));
    	        	
    	        	setCellValue(obj_row, 51+appendDays, ValueUtil.getStringValue(""));
    				setCellValue(obj_row, 52+appendDays, ValueUtil.getStringValue(""));
    	        	setCellValue(obj_row, 53+appendDays, ValueUtil.getStringValue(""));
    	        	setCellValue(obj_row, 54+appendDays, ValueUtil.getStringValue(""));
    	        	
    	        	if(lst_scoreRecords!=null){
    	        		for(ScoreRecord s:lst_scoreRecords){
    	        			if(workRecord.getEmployee_no()!=null&&workRecord.getEmployee_no().equals(s.getEmployee_no())){
    	        				setCellValue(obj_row, 51+appendDays, ValueUtil.getStringValue(s.getScore()));
    	        				setCellValue(obj_row, 52+appendDays, ValueUtil.getStringValue(s.getMixedType_repeatBuyCostomer()));
    	        	        	setCellValue(obj_row, 53+appendDays, ValueUtil.getStringValue(s.getTurnover()));
    	        	        	setCellValue(obj_row, 54+appendDays, ValueUtil.getStringValue(s.getStoreroom()));
    	        	        	setCellValue(obj_row, 55+appendDays, ValueUtil.getStringValue(s.getEmprise()));
    	        			}
    	        		}
    	        	}
    	        	
					nRowIndex++;
				}
			}


			//excel的尾部
			int bottom_id1=nRowIndex + 7;
			int bottom_id2=nRowIndex + 8;
			int bottom_id3=nRowIndex + 9;
			int bottom_id4=nRowIndex + 10;
			int bottom_id5=nRowIndex + 12;
			sh_data.createRow(bottom_id1);
			sh_data.createRow(bottom_id2);
			sh_data.createRow(bottom_id3);
			sh_data.createRow(bottom_id4);
			sh_data.createRow(bottom_id5);
			Row obj_row_bottom1 = sh_data.getRow(bottom_id1);
			Row obj_row_bottom2 = sh_data.getRow(bottom_id2);
			Row obj_row_bottom3 = sh_data.getRow(bottom_id3);
			Row obj_row_bottom4 = sh_data.getRow(bottom_id4);
			Row obj_row_bottom5 = sh_data.getRow(bottom_id5);
			CellRangeAddress cra1=new CellRangeAddress(bottom_id1, bottom_id1, 1, 47);
			CellRangeAddress cra2=new CellRangeAddress(bottom_id2, bottom_id2, 1, 47);
			CellRangeAddress cra3=new CellRangeAddress(bottom_id3, bottom_id3, 1, 47);
			CellRangeAddress cra4=new CellRangeAddress(bottom_id4, bottom_id4, 1, 47);
			CellRangeAddress cra5=new CellRangeAddress(bottom_id5, bottom_id5, 1, 6);
			CellRangeAddress cra6=new CellRangeAddress(bottom_id5, bottom_id5, 29, 36);
			//在sheet里增加合并单元格
			sh_data.addMergedRegion(cra1);
			sh_data.addMergedRegion(cra2);
			sh_data.addMergedRegion(cra3);
			sh_data.addMergedRegion(cra4);
			sh_data.addMergedRegion(cra5);
			sh_data.addMergedRegion(cra6);


			obj_row_bottom1.setHeight((short)600);
			setLeftCellValue(obj_row_bottom1, 1, ValueUtil.getStringValue("1、出勤根据A、B、C、D四种班次分别进行标注,A、B班工时数为10小时,C、D班工时数为8小时,班次总工时须满本月法定工时"+workMonth.getTotalworktime()+"小时,事假∧,病假△,婚假☆,丧假□,产假﹢,年休假◎,出差※,工伤◇,旷工×,迟到﹤,早退﹥；"));
			setLeftCellValue(obj_row_bottom2, 1, ValueUtil.getStringValue("2、考勤表由考勤员如实填写；"));
			setLeftCellValue(obj_row_bottom3, 1, ValueUtil.getStringValue("3、月累计栏：出勤、事假、病假、婚假、丧假、年休假、出差、工伤假、旷工以天数累计,迟到、早退以次数累计；"));
			setLeftCellValue(obj_row_bottom4, 1, ValueUtil.getStringValue("4、考勤表电子版每月2日前交人力资源中心,纸质版每月15号前提交。"));
			setLeftCellValue(obj_row_bottom5, 1, ValueUtil.getStringValue("人力中心负责人："));
			setLeftCellValue(obj_row_bottom5, 29, ValueUtil.getStringValue("部门（中心负责人）："));
			//sh_data.setColumnHidden(32, true);

			FileOutputStream fis_out_excel = new FileOutputStream(file_new);
			wb_wrinfo.write(fis_out_excel);
			fis_out_excel.close();
			fis_input_excel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return PropertiesUtil.getValue("file.web.root")+exportFileName;
	}

	@Override
	public WorkRecordTotal updateWorkRecordTotalToReturn(WorkRecordTotal workRecordTotal) {
		UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
		MessageNewManager appMessageManager = (MessageNewManager)SpringHelper.getBean("messageNewManager");
		WorkRecordTotal save_workRecordTotal = (WorkRecordTotal)this.getObject(workRecordTotal.getId());
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		save_workRecordTotal.setRemark(workRecordTotal.getRemark());
		save_workRecordTotal.setCommit_status(workRecordTotal.getCommit_status());
		preObject(save_workRecordTotal);
		this.saveObject(save_workRecordTotal);
		//AppMessage appMessage = new AppMessage();
		Message appMessage  = new Message();
		appMessage.setTitle("考勤提示");
		
		User submit_user = userManager.getUserEntity(save_workRecordTotal.getSubmit_user_id());
		UserDTO current_user = userManager.getCurrentUserDTO();
		if(workRecordTotal.getCommit_status() == 2){
			appMessage.setContent("您提交的"+save_workRecordTotal.getWork_month()+"考勤数据被" + current_user.getName() + "退回。原因是\n" + workRecordTotal.getRemark());
		}else if(workRecordTotal.getCommit_status() == 3){
			appMessage.setContent("您提交的"+save_workRecordTotal.getWork_month()+"考勤数据审核通过。");
		}
		
		MessagePushThread t = new MessagePushThread(appMessage, save_workRecordTotal, submit_user, submit_user, messageNewDao, appMessageManager);
		Thread thread = new Thread(t);
		thread.start();
		System.out.println("-------------------------开始返回-------------------------");
		
		/*
		appMessage.setPk_id(save_workRecordTotal.getId());
		appMessage.setJump_path("work_record_result");
		appMessage.setType("work_record");
		appMessage.setTo_employee_id(submit_user.getId());
		appMessage.setIsDelete(0);
		appMessage.setIsRead(0);
		appMessage.setReceiveId(submit_user.getEmployeeId());
		appMessage.setSendId(current_user.getEmployeeId());
		messageNewDao.deleteWorkRecord(appMessage);//设置之前的这条考勤为删除，考勤通过\驳回 workRecordTotal都是同一条记录
		preObject(appMessage);
		appMessageManager.sendMessageAuto(submit_user, appMessage);//保存并发送消息*/
		//appMessageManager.saveMessageAndPush(submit_user,appMessage);
		return save_workRecordTotal;
	}
	
	public void setCellValue(Row obj_row,int nCellIndex,Object value){
		Cell cell=obj_row.createCell(nCellIndex);
		cell.setCellStyle(getCellStyle_common());
		if(value==null||value.equals("")){
        	value=null;
        	return;
        }
		cell.setCellValue(new HSSFRichTextString(value==null?null:value.toString()));
	}

	public void setLeftCellValue(Row obj_row,int nCellIndex,Object value){
		Cell cell=obj_row.createCell(nCellIndex);
		cell.setCellStyle(getLeftCellStyle_common());
		cell.setCellValue(new HSSFRichTextString(value==null?null:value.toString()));
	}

	public void setStrongCenterCellValue(Row obj_row,int nCellIndex,Object value){
		Cell cell=obj_row.createCell(nCellIndex);
		cell.setCellStyle(getStrongCenterCellStyle_common());
		cell.setCellValue(new HSSFRichTextString(value==null?null:value.toString()));
	}


	public CellStyle getCellStyle_common(){
		return cellStyle_common;
	}

	public CellStyle getLeftCellStyle_common(){
		return cellLeftStyle_common;
	}
	public CellStyle getStrongCenterCellStyle_common(){
		return cellStrongCenterStyle_common;
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
	//无边框居左
	public void setLeftCellStyle_common(Workbook wb){
		cellLeftStyle_common=wb.createCellStyle();
		cellLeftStyle_common.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平居
		cellLeftStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);//垂直居中
		cellLeftStyle_common.setWrapText(true);//设置自动换行
	}
	//加粗 居中
	public void setStrongCenterCellStyle_common(Workbook wb){
		cellStrongCenterStyle_common=wb.createCellStyle();
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cellStrongCenterStyle_common.setFont(font);
		cellStrongCenterStyle_common.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居
		cellStrongCenterStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
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

}
