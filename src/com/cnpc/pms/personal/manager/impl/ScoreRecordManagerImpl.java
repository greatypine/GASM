package com.cnpc.pms.personal.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.swing.Spring;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.h2.mvstore.DataUtils;
import org.springframework.jmx.export.SpringModelMBean;
import org.springframework.util.FileCopyUtils;

import com.aliyun.oss.common.utils.RangeSpec.Type;
import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.IPage;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.ScoreRecordDao;
import com.cnpc.pms.personal.dao.WorkRecordDao;
import com.cnpc.pms.personal.entity.Company;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.DsTopData;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.ScoreRecord;
import com.cnpc.pms.personal.entity.ScoreRecordTotal;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.WorkInfo;
import com.cnpc.pms.personal.entity.WorkMonth;
import com.cnpc.pms.personal.entity.WorkRecord;
import com.cnpc.pms.personal.entity.WorkRecordTotal;
import com.cnpc.pms.personal.manager.CompanyManager;
import com.cnpc.pms.personal.manager.DsTopDataManager;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
import com.cnpc.pms.personal.manager.ScoreRecordManager;
import com.cnpc.pms.personal.manager.ScoreRecordTotalManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.WorkInfoManager;
import com.cnpc.pms.personal.manager.WorkMonthManager;
import com.cnpc.pms.personal.manager.WorkRecordManager;
import com.cnpc.pms.personal.manager.WorkRecordTotalManager;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;
import com.cnpc.pms.utils.excel.PinyinUtil;

import freemarker.cache.StrongCacheStorage;

@SuppressWarnings("all")
public class ScoreRecordManagerImpl extends BaseManagerImpl implements ScoreRecordManager{

	PropertiesValueUtil propertiesValueUtil = null;

    private String folder_path = null;
	
    CellStyle cellStyle_common = null;
    CellStyle cellLeftStyle_common = null;
    CellStyle cellStrongCenterStyle_common=null;
    
	@Override
	public List<Humanresources> queryHumanresourceList() {
		UserManager manager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = manager.getCurrentUserDTO();
		HumanresourcesManager hManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
		List<Humanresources> humList = hManager.queryHumanresourceListByStoreId(userDTO.getStore_id());
		return humList;
	}
	
	//查询当前日期离职人员
	@Override
	public List<Humanresources> queryHumanresourceListLz(String month) {
		UserManager manager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = manager.getCurrentUserDTO();
		HumanresourcesManager hManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
		List<Humanresources> humList = hManager.queryHumanresourceListByStoreIdLz(userDTO.getStore_id(),month);
		return humList;
	}
	//查询当前日期入职人员
	@Override
	public List<Humanresources> queryHumanresourceListRz(String month) {
		UserManager manager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = manager.getCurrentUserDTO();
		HumanresourcesManager hManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
		List<Humanresources> humList = hManager.queryHumanresourceListByStoreIdRz(userDTO.getStore_id(),month);
		return humList;
	}
	
	
	//score_record.html查主询所有的人员方法 （原）
	@Override
	public List<ScoreRecord> queryScoreRecordList(String work_month,Long store_id){
		UserManager manager = (UserManager)SpringHelper.getBean("userManager");
		if(store_id == null){
			UserDTO userDTO = manager.getCurrentUserDTO();
			store_id = userDTO.getStore_id();
		}

		ScoreRecordTotalManager scoreRecordTotalManager = (ScoreRecordTotalManager)SpringHelper.getBean("scoreRecordTotalManager");
		ScoreRecordManager scoreRecordManager = (ScoreRecordManager)SpringHelper.getBean("scoreRecordManager");
		IFilter iFiltermain =FilterFactory.getSimpleFilter(" store_id=" + store_id + " and work_month='"+work_month+"' ");
        List<ScoreRecordTotal> lst_wrList = (List<ScoreRecordTotal>)scoreRecordTotalManager.getList(iFiltermain);
        List<ScoreRecord> retWrList = new ArrayList<ScoreRecord>();
        //有值
        if(lst_wrList!=null&&lst_wrList.size()>0){
        	ScoreRecordTotal scoreRecordTotal = lst_wrList.get(0);
        	IFilter iFilter =FilterFactory.getSimpleFilter(" scorerecord_id="+scoreRecordTotal.getId());
            //查 询已经录入的考勤
        	List<ScoreRecord> lst_wrListSub = (List<ScoreRecord>)scoreRecordManager.getList(iFilter);
        	HumanresourcesManager humanresourcesManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
        	IFilter humanFilter =FilterFactory.getSimpleFilter(" store_id="+store_id+" and humanstatus in(1,2) ");
    		List<Humanresources> humanresourcesList = (List<Humanresources>) humanresourcesManager.getList(humanFilter);
    		//查询此月离职的人
    		List<Humanresources> lzHumanresources = queryHumanresourceListLz(work_month);
    		//查询此月入职的人
    		List<Humanresources> rzHumanresources = queryHumanresourceListRz(work_month);
    		
    		List<String> employeeNo = new ArrayList<String>();
    		for(ScoreRecord w:lst_wrListSub){
    			employeeNo.add(w.getEmployee_no());
    		}
    		
    		if(scoreRecordTotal.getCommit_status()!=1&&scoreRecordTotal.getCommit_status()!=3){
    			//如果有离职的人 当月
        		if(lzHumanresources!=null&&lzHumanresources.size()>0){
        			for(Humanresources lzh:lzHumanresources){
        				if(!employeeNo.contains(lzh.getEmployee_no())){
        					ScoreRecord scoreRecord = new ScoreRecord();
        					scoreRecord.setEmployee_no(lzh.getEmployee_no());
        					scoreRecord.setStore_name(lzh.getStorename());
        					scoreRecord.setEmployee_name(lzh.getName());
        					scoreRecord.setLeavedate(lzh.getLeavedate());
        					scoreRecord.setHumanstatus(lzh.getHumanstatus());
        					buildDays(scoreRecord);
        					lst_wrListSub.add(scoreRecord);
        				}
        			}
        		}
        		//如果有入职的人当月
        		if(rzHumanresources!=null&&rzHumanresources.size()>0){
        			for(Humanresources rzh:rzHumanresources){
        				if(!employeeNo.contains(rzh.getEmployee_no())){
        					ScoreRecord scoreRecord = new ScoreRecord();
        					scoreRecord.setEmployee_no(rzh.getEmployee_no());
        					scoreRecord.setStore_name(rzh.getStorename());
        					scoreRecord.setEmployee_name(rzh.getName());
        					scoreRecord.setTopostdate(rzh.getTopostdate());
        					scoreRecord.setHumanstatus(rzh.getHumanstatus());
        					buildDays(scoreRecord);
        					lst_wrListSub.add(scoreRecord);
        				}
        			}
        		}
    		}
    		
    		
        	for(ScoreRecord w:lst_wrListSub){
        		String name = "";
        		String topostdate="";
        		String leavedate="";
        		Long humanstatus=null;
        		if(humanresourcesList!=null){
        			for(Humanresources h:humanresourcesList){
        				if(w.getEmployee_no()!=null&&w.getEmployee_no().equals(h.getEmployee_no())){
        					name=h.getName();
        					topostdate=h.getTopostdate();
        					leavedate=h.getLeavedate();
        					humanstatus=h.getHumanstatus();
        				}
        			}
        		}
        		
        		if(name!=null&&name.length()>0){
        			w.setEmployee_name(name);
        			w.setTopostdate(topostdate);
            		w.setLeavedate(leavedate);
            		w.setHumanstatus(humanstatus);
        		}else{
        			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
        			User user = userManager.findEmployeeByEmployeeNo(w.getEmployee_no());
        			w.setEmployee_name(user==null?"":user.getName());
        			
                	IFilter hFilter =FilterFactory.getSimpleFilter("employee_no",w.getEmployee_no());
            		List<Humanresources> hList = (List<Humanresources>) humanresourcesManager.getList(hFilter);
            		if(hList!=null&&hList.size()>0){
            			Humanresources humanres = hList.get(0);
            			w.setHumanstatus(humanres.getHumanstatus());
            			w.setTopostdate(humanres.getTopostdate());
            			w.setLeavedate(humanres.getLeavedate());
            		}
        			
        		}
        		
    			StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
    			w.setStore_name(storeManager.findStore(store_id).getName());
        		w.setStore_id(store_id);
        		retWrList.add(w);
        	}
        }else{
        	List<Humanresources> humanList = queryHumanresourceList();
        	if(humanList!=null&&humanList.size()>0){
        		for(Humanresources h:humanList){
        			ScoreRecord sRecord = new ScoreRecord();
        			sRecord.setEmployee_no(h.getEmployee_no());
        			sRecord.setStore_name(h.getStorename());
        			sRecord.setStore_id(h.getStore_id());
        			sRecord.setEmployee_name(h.getName());
        			sRecord.setHumanstatus(h.getHumanstatus());
        			sRecord.setTopostdate(h.getTopostdate());
        			sRecord.setLeavedate(h.getLeavedate());
        			buildDays(sRecord);
        			retWrList.add(sRecord);
        		}
        	}
        	
        	//第一次填报考勤，查询本月离职人员。
        	List<Humanresources> lzHumanresources = queryHumanresourceListLz(work_month);
        	if(lzHumanresources!=null&&lzHumanresources.size()>0){
        		for(Humanresources h:lzHumanresources){
        			ScoreRecord lzScoreRecord = new ScoreRecord();
        			lzScoreRecord.setEmployee_no(h.getEmployee_no());
        			lzScoreRecord.setStore_name(h.getStorename());
        			lzScoreRecord.setStore_id(h.getStore_id());
        			lzScoreRecord.setEmployee_name(h.getName());
        			lzScoreRecord.setLeavedate(h.getLeavedate());
        			lzScoreRecord.setHumanstatus(h.getHumanstatus());
        			buildDays(lzScoreRecord);
        			retWrList.add(lzScoreRecord);
        		}
        	}
        	
/*        	//特殊情况  如果本月离职，查询上个月的时候 需要加入 本月离职的员工。因为上个月员工在职。
        	if(work_month!=null){
        		String ym = work_month.split("-")[0]+""+work_month.split("-")[1];
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        		sdf.format(new Date());
        	}
*/        	
        	
        }
        
        List<ScoreRecord> retList = new ArrayList<ScoreRecord>();
        //处理门店名//取store表 防止门店名称变更
        StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
        HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
        if(retWrList!=null&&retWrList.size()>0){
        	for(ScoreRecord w:retWrList){
        		Store store = storeManager.findStore(w.getStore_id());
        		Humanresources humanresources = humanresourcesManager.getEmployeeInfoByEmployeeNo(w.getEmployee_no());
        		if(store!=null){
        			w.setStore_name(store.getName());
        		}
        		if(humanresources!=null&&humanresources.getZw()!=null){
        			w.setZw(humanresources.getZw());
        		}
        		retList.add(w);
        	}
        }
        return retList;
	}
	
	
	
		//score_record.html  score_record_reference.html查询所有的人员方法 （新）
		@Override
		public List<ScoreRecord> queryScoreRecordListByTop(String work_month,Long store_id){
			UserManager manager = (UserManager)SpringHelper.getBean("userManager");
			if(store_id == null){
				UserDTO userDTO = manager.getCurrentUserDTO();
				store_id = userDTO.getStore_id();
			}

			ScoreRecordTotalManager scoreRecordTotalManager = (ScoreRecordTotalManager)SpringHelper.getBean("scoreRecordTotalManager");
			ScoreRecordManager scoreRecordManager = (ScoreRecordManager)SpringHelper.getBean("scoreRecordManager");
			IFilter iFiltermain =FilterFactory.getSimpleFilter(" store_id=" + store_id + " and work_month='"+work_month+"' and str_work_info_id='绩效打分录入' ");
	        List<ScoreRecordTotal> lst_wrList = (List<ScoreRecordTotal>)scoreRecordTotalManager.getList(iFiltermain);
	        List<ScoreRecord> retWrList = new ArrayList<ScoreRecord>();
	        //有值
	        if(lst_wrList!=null&&lst_wrList.size()>0){
	        	ScoreRecordTotal scoreRecordTotal = lst_wrList.get(0);
	        	IFilter iFilter =FilterFactory.getSimpleFilter(" scorerecord_id="+scoreRecordTotal.getId());
	            //查 询已经录入的考勤
	        	List<ScoreRecord> lst_wrListSub = (List<ScoreRecord>)scoreRecordManager.getList(iFilter);
	        	
	        	DsTopDataManager dsTopDataManager = (DsTopDataManager) SpringHelper.getBean("dsTopDataManager");
	        	//查询考勤发起月有多少员工 。
	        	List<DsTopData> dsTopDatas = dsTopDataManager.queryDSTopData(store_id, work_month);
	        	
	        	/*HumanresourcesManager humanresourcesManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
	        	IFilter humanFilter =FilterFactory.getSimpleFilter(" store_id="+store_id+" and humanstatus in(1,2) ");
	    		List<Humanresources> humanresourcesList = (List<Humanresources>) humanresourcesManager.getList(humanFilter);*/
	    		
	    		List<String> employeeNo = new ArrayList<String>();
	    		for(ScoreRecord w:lst_wrListSub){
	    			employeeNo.add(w.getEmployee_no());
	    		}
	    		
	        	for(ScoreRecord w:lst_wrListSub){
	        		String name = "";
	        		String topostdate="";
	        		String leavedate="";
	        		Long humanstatus=null;
	        		String zw="";
	        		if(dsTopDatas!=null){
	        			for(DsTopData h:dsTopDatas){
	        				if(w.getEmployee_no()!=null&&w.getEmployee_no().equals(h.getEmployeeno())){
	        					name=h.getUsername();
	        					topostdate=h.getTopostdate();
	        					leavedate=h.getLeavedate();
	        					humanstatus=h.getHumanstatus();
	        					zw=h.getZw();
	        				}
	        			}
	        		}
	        		
	        		if(name!=null&&name.length()>0){
	        			w.setEmployee_name(name);
	        			w.setTopostdate(topostdate);
	            		w.setLeavedate(leavedate);
	            		w.setHumanstatus(humanstatus);
	            		w.setZw(zw);
	        		}else{
	        			DsTopData dsTopData = dsTopDataManager.queryDSTopDataByEmployeeNo(w.getEmployee_no(), work_month);
	    	            String username = dsTopData==null?"":dsTopData.getUsername();
	        			w.setEmployee_name(username);
	        			
	                	IFilter hFilter =FilterFactory.getSimpleFilter("employeeno",w.getEmployee_no()).appendAnd(FilterFactory.getSimpleFilter("yearmonth",work_month));
	            		List<DsTopData> hList = (List<DsTopData>) dsTopDataManager.getList(hFilter);
	            		if(hList!=null&&hList.size()>0){
	            			DsTopData humanres = hList.get(0);
	            			w.setEmployee_no(humanres.getEmployeeno());
		        			w.setStore_name(humanres.getStorename());
		        			w.setStore_id(humanres.getStoreid());
		        			w.setEmployee_name(humanres.getUsername());
		        			w.setZw(humanres.getZw());
		        			
	            			w.setHumanstatus(humanres.getHumanstatus());
	            			w.setTopostdate(humanres.getTopostdate());
	            			w.setLeavedate(humanres.getLeavedate());
	            			w.setZw(humanres.getZw());
	            		}
	        		}
	        		
	    			/*StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
	    			w.setStore_name(storeManager.findStore(store_id).getName());*/
	        		w.setStore_id(store_id);
	        		retWrList.add(w);
	        	}
	        }else{//如果没值  是第一次填报  
	        	//List<Humanresources> humanList = queryHumanresourceList();
	        	DsTopDataManager dsTopDataManager = (DsTopDataManager) SpringHelper.getBean("dsTopDataManager");
	        	//查询考勤发起月有多少员工 。
	        	List<DsTopData> dsTopDatas = dsTopDataManager.queryDSTopData(store_id, work_month);
	        	if(dsTopDatas!=null&&dsTopDatas.size()>0){
	        		for(DsTopData ds:dsTopDatas){
	        			ScoreRecord sRecord = new ScoreRecord();
	        			sRecord.setEmployee_no(ds.getEmployeeno());
	        			sRecord.setStore_name(ds.getStorename());
	        			sRecord.setStore_id(ds.getStoreid());
	        			sRecord.setEmployee_name(ds.getUsername());
	        			sRecord.setHumanstatus(ds.getHumanstatus());
	        			sRecord.setZw(ds.getZw());
	        			sRecord.setTopostdate(ds.getTopostdate());
	        			sRecord.setLeavedate(ds.getLeavedate());
	        			buildDays(sRecord);
	        			retWrList.add(sRecord);
	        		}
	        	}
	        	//查询所填报考勤月份离职的人员 
	        	//第一次填报考勤，查询本月离职人员。
	        	List<DsTopData> dsTopDatas_lz = dsTopDataManager.queryDSTopDataLz(store_id, work_month);
	        	if(dsTopDatas_lz!=null&&dsTopDatas_lz.size()>0){
	        		for(DsTopData ds_lz:dsTopDatas_lz){
	        			ScoreRecord sRecord = new ScoreRecord();
	        			sRecord.setEmployee_no(ds_lz.getEmployeeno());
	        			sRecord.setStore_name(ds_lz.getStorename());
	        			sRecord.setStore_id(ds_lz.getStoreid());
	        			sRecord.setEmployee_name(ds_lz.getUsername());
	        			sRecord.setHumanstatus(ds_lz.getHumanstatus());
	        			sRecord.setZw(ds_lz.getZw());
	        			sRecord.setTopostdate(ds_lz.getTopostdate());
	        			sRecord.setLeavedate(ds_lz.getLeavedate());
	        			buildDays(sRecord);
	        			retWrList.add(sRecord);
	        		}
	        	}
	        	
	        }
	        
	        List<ScoreRecord> retList = new ArrayList<ScoreRecord>();
	        //处理门店名//取store表 防止门店名称变更
	        StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
	        //HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
	        DsTopDataManager dsTopDataManager = (DsTopDataManager) SpringHelper.getBean("dsTopDataManager");
	        if(retWrList!=null&&retWrList.size()>0){
	        	for(ScoreRecord w:retWrList){
	        		Store store = storeManager.findStore(w.getStore_id());
	        		DsTopData dsTopData = dsTopDataManager.queryDSTopDataByEmployeeNo(w.getEmployee_no(), work_month); //
	        		//Humanresources humanresources = humanresourcesManager.getEmployeeInfoByEmployeeNoExtend(w.getEmployee_no());
	        		if(store!=null){
	        			w.setStore_name(store.getName());
	        		}
	        		
	        		String src_zw = dsTopData.getRemark();
        			String topzw = dsTopData.getZw();
        			if(topzw.equals("副店长")&&src_zw!=null&&src_zw.equals("服务专员")){
        				//如果是副店长 判断之前是否是 服务专员
        				w.setZw("副店长服务专员");
        			}
        			if(topzw.equals("副店长")&&src_zw!=null&&src_zw.equals("综合专员")){
        				//如果是副店长 判断之前是否是综合专员
        				w.setZw("副店长综合专员");
        			}
        			if(topzw.equals("副店长")&&src_zw!=null&&src_zw.equals("综合管理")){
        				//如果是副店长 判断之前是否是 综合管理 
        				w.setZw("副店长综合管理");
        			}
        			if(topzw.equals("副店长")&&src_zw!=null&&src_zw.equals("国安侠")){
        				//如果是副店长 判断之前是否是 综合管理 
        				w.setZw("副店长");
        			}
	        		
	        		/*if(humanresources!=null&&humanresources.getZw()!=null){
	        			//w.setZw(humanresources.getZw());
	        			w.setTopostdate(humanresources.getTopostdate());
	        		}*/
	        		retList.add(w);
	        	}
	        }
	        return retList;
		}
	
	
	
	
	
	
	
	

	/*@Override
	public String saveScoreRecordForExcel(Long scorerecord_id){
		
		 	String str_file_name = "work_record_template";
	        String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
	        //配置文件中的路径
	        String str_filepath = strRootpath.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));
	
	        File file_template = new File(str_filepath);
	       
	        WorkRecordTotalManager workRecordTotalManager = (WorkRecordTotalManager) SpringHelper.getBean("workRecordTotalManager");
	        IFilter iFiltermain =FilterFactory.getSimpleFilter(" id="+scorerecord_id+" ");
	        List<WorkRecordTotal> lst_wrtotalList = (List<WorkRecordTotal>)workRecordTotalManager.getList(iFiltermain);
	        WorkRecordTotal workRecordTotal = lst_wrtotalList.get(0);
	        
	        StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
	        Store store = storeManager.findStore(workRecordTotal.getStore_id());
	        
	        StringBuffer queryCondition=new StringBuffer();
	        IFilter iFilter =FilterFactory.getSimpleFilter(" scorerecord_id="+scorerecord_id+" ");
	        WorkRecordManager workRecordManager = (WorkRecordManager) SpringHelper.getBean("workRecordManager");
	        List<WorkRecord> lst_wrList = (List<WorkRecord>)workRecordManager.getList(iFilter);
	        
	        
	        WorkMonthManager workMonthManager = (WorkMonthManager)SpringHelper.getBean("workMonthManager");
	        WorkMonth workMonth = workMonthManager.findWorkMonthByCity(workRecordTotal.getWork_month(),store.getCityName());
	        
	        //处理月的天数
	        int days = DateUtils.getDaysByMonths(workRecordTotal.getWork_month());
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
	        
	        UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
	        
	        String str_file_dir_path = PropertiesUtil.getValue("file.root");
	        
	        String exportFileName = "";
	        try {
				exportFileName = PinyinUtil.getPingYin(store.getName())+workRecordTotal.getWork_month()+".xls";
			} catch (BadHanyuPinyinOutputFormatCombination e1) {
				exportFileName = "file_template"+workRecordTotal.getWork_month()+".xls";
			}
	        
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
             String work_month=workRecordTotal.getWork_month();
	         Row obj_row_title = sh_data.getRow(0);
	         setStrongCenterCellValue(obj_row_title, 0, ValueUtil.getStringValue(work_month+"份员工考勤情况统计表"));
	        	
	         //第二行 填报单位
	         Row obj_row_org = sh_data.getRow(2);
	         //setCellValue(obj_row_org, 1, ValueUtil.getStringValue("填报单位："));
	         setStrongCenterCellValue(obj_row_org, 2, ValueUtil.getStringValue(store.getName()));
             
	         //第三行 填报单位
	         Row obj_row_title1 = sh_data.getRow(3);
	         CellRangeAddress isauth=new CellRangeAddress(3, 3, 2, days+1);
	         sh_data.addMergedRegion(isauth); 
	         setCellValue(obj_row_title1, 2, ValueUtil.getStringValue("日       期"));
	         
	         CellRangeAddress monthSum=new CellRangeAddress(3, 3, days+2, days+18);
	         sh_data.addMergedRegion(monthSum);
	         setCellValue(obj_row_title1, 33+appendDays, ValueUtil.getStringValue("月  累  计"));
	         
	          CellRangeAddress fb=new CellRangeAddress(3, 3, days+15, days+16);
	         sh_data.addMergedRegion(fb);
	         setCellValue(obj_row_title1, 46+appendDays, ValueUtil.getStringValue("饭补"));
	         
	         //列宽
	         sh_data.setColumnWidth(50+appendDays, "个人签字".getBytes().length*2*156);
	         setCellValue(obj_row_title1, 50+appendDays, ValueUtil.getStringValue("个人签字"));
	         sh_data.setColumnWidth(51+appendDays, "绩效分".getBytes().length*2*156);
	         setCellValue(obj_row_title1, 51+appendDays, ValueUtil.getStringValue("绩效分"));
	         
	         CellRangeAddress signName=new CellRangeAddress(3, 5, 50+appendDays, 50+appendDays);
	         sh_data.addMergedRegion(signName);
	         CellRangeAddress score=new CellRangeAddress(3, 5, 51+appendDays, 51+appendDays);
	         sh_data.addMergedRegion(score);
	         
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
	         
	         //excel的尾部
	         int bottom_id1=lst_wrList.size()+7;
	         int bottom_id2=lst_wrList.size()+8;
	         int bottom_id3=lst_wrList.size()+9;
	         int bottom_id4=lst_wrList.size()+10;
	         int bottom_id5=lst_wrList.size()+12;
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

	         //填充人员考勤
    	        int nRowIndex=6;
    	        for (Object obj_wr_object : lst_wrList) {
    	        	
    	        	WorkRecord workRecord = (WorkRecord) obj_wr_object;
    	        	sh_data.createRow(nRowIndex);
    	        	Row obj_row = sh_data.getRow(nRowIndex);
    	        	
    	        	setCellValue(obj_row, 0, ValueUtil.getStringValue(workRecord.getEmployee_no()));
    	        	User user = userManager.findEmployeeByEmployeeNo(workRecord.getEmployee_no());
    	        	setCellValue(obj_row, 1, ValueUtil.getStringValue(user.getName()));
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
    	        	setCellValue(obj_row, 51+appendDays, ValueUtil.getStringValue(workRecord.getScore()));
    	        	
    	        	nRowIndex++;
    	        }
    	        
    	        //sh_data.setColumnHidden(32, true);
    	        
    	        FileOutputStream fis_out_excel = new FileOutputStream(file_new);
    	        wb_wrinfo.write(fis_out_excel);
    	        fis_out_excel.close();
    	        fis_input_excel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	        
	  return PropertiesUtil.getValue("file.web.root")+"/"+exportFileName;
		
		
	}*/
	
	
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
    
	
	private void buildDays(ScoreRecord sRecord) {
		/*wRecord.setDay1("");
		wRecord.setDay2("");
		wRecord.setDay3("");
		wRecord.setDay4("");
		wRecord.setDay5("");
		wRecord.setDay6("");
		wRecord.setDay7("");
		wRecord.setDay8("");
		wRecord.setDay9("");
		wRecord.setDay10("");
		wRecord.setDay11("");
		wRecord.setDay12("");
		wRecord.setDay13("");
		wRecord.setDay14("");
		wRecord.setDay15("");
		wRecord.setDay16("");
		wRecord.setDay17("");
		wRecord.setDay18("");
		wRecord.setDay19("");
		wRecord.setDay20("");
		wRecord.setDay21("");
		wRecord.setDay22("");
		wRecord.setDay23("");
		wRecord.setDay24("");
		wRecord.setDay25("");
		wRecord.setDay26("");
		wRecord.setDay27("");
		wRecord.setDay28("");
		wRecord.setDay29("");
		wRecord.setDay30("");
		wRecord.setDay31("");
		
		wRecord.setWorkdays("");//出勤
		wRecord.setAdjholiday("");//倒休
		wRecord.setEventday("");//事假	
		wRecord.setBadday("");//病假	
		wRecord.setWedday("");//婚假
		wRecord.setBadday("");//病假
		wRecord.setProday("");//产假
		wRecord.setLoseday("");//丧假
		wRecord.setYearholiday("");//年休假
		wRecord.setTripday("");//出差
		wRecord.setWorkhurtday("");//工伤假
		wRecord.setAbsenceday("");//旷工
		wRecord.setLateday("");//迟到
		wRecord.setLeaveearlyday("");//早退
		wRecord.setOvertime("");//是否正式
		wRecord.setRealovertime(0);
		wRecord.setTotalovertime(0);
		wRecord.setAllowdays("");//饭补天数
*/		
		
		sRecord.setMixedType_repeatBuyCostomer("");//绩效分
		sRecord.setTurnover("");//绩效分
		sRecord.setScore("");//绩效分
		sRecord.setStoreroom("");
		sRecord.setScorerecord_id(null);
	}

	/*@Override
	public WorkRecordTotal saveWorkRecordList(List<Map<String, Object>> objList,String work_month,Long store_id){
		if(objList!=null&&objList.size()>0){
			UserManager manager = (UserManager)SpringHelper.getBean("userManager");
			UserDTO userDTO = manager.getCurrentUserDTO();
			StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
			Store store = null;
			if(store_id!=null&&store_id>0){
				store = storeManager.findStore(store_id);
			}else{
				store = storeManager.findStore(userDTO.getStore_id());
			}
			
			//保存、修改主表
			WorkRecordTotal workRecordTotal = new WorkRecordTotal();
			workRecordTotal.setStore_id(store.getStore_id());
			workRecordTotal.setCityname(store.getCityName());
			workRecordTotal.setCommit_status(Long.parseLong("0"));
			workRecordTotal.setWork_month(work_month);
			
			if(objList.get(0).get("workrecord_id")!=null&&!objList.get(0).get("workrecord_id").toString().equals("null")){
				Long workrecord_id = Long.parseLong(objList.get(0).get("workrecord_id").toString());
				workRecordTotal.setId(Long.parseLong(objList.get(0).get("workrecord_id").toString()));
				
				WorkRecordTotalManager workRecordTotalManager = (WorkRecordTotalManager)SpringHelper.getBean("workRecordTotalManager");
				WorkRecordTotal upRecordTotal = (WorkRecordTotal) workRecordTotalManager.getObject(workrecord_id);
				upRecordTotal.setCityname(store.getCityName());
				upRecordTotal.setCommit_status(Long.parseLong("0"));
				preSaveObject(upRecordTotal);
				saveObject(upRecordTotal);
			}else{
				preSaveObject(workRecordTotal);
				saveObject(workRecordTotal);
			}
			
			//主表结束
			
			
			//子表开始
			if(workRecordTotal.getId()!=null){
				WorkRecordDao workRecordDao = (WorkRecordDao)SpringHelper.getBean(WorkRecordDao.class.getName());
				workRecordDao.delWorkRecoedById(workRecordTotal.getId());
			}
			for(Map<String, Object> objMap : objList){
				WorkRecord wRecord = new WorkRecord();
				//取得界面上隐藏的ID如果有 赋值。为修改数据。如果没有为新增
				if(objMap.get("id")!=null&&!objMap.get("id").toString().equals("null")){
					//wRecord.setId(Long.parseLong(objMap.get("id").toString()));
					//删除子表数据
					this.removeById(Long.parseLong(objMap.get("id").toString()));
				}
				
				wRecord.setWorkrecord_id(workRecordTotal.getId());
				wRecord.setStore_name(userDTO.getStore_name());
				
				wRecord.setEmployee_no(objMap.get("employee_no").toString());
				wRecord.setEmployee_name(objMap.get("employee_name").toString());
				wRecord.setWork_month(work_month);
				
				wRecord.setDay1(buildDay(objMap.get("day1")));
				wRecord.setDay2(buildDay(objMap.get("day2")));
				wRecord.setDay3(buildDay(objMap.get("day3")));
				wRecord.setDay4(buildDay(objMap.get("day4")));
				wRecord.setDay5(buildDay(objMap.get("day5")));
				wRecord.setDay6(buildDay(objMap.get("day6")));
				wRecord.setDay7(buildDay(objMap.get("day7")));
				wRecord.setDay8(buildDay(objMap.get("day8")));
				wRecord.setDay9(buildDay(objMap.get("day9")));
				wRecord.setDay10(buildDay(objMap.get("day10")));
				wRecord.setDay11(buildDay(objMap.get("day11")));
				wRecord.setDay12(buildDay(objMap.get("day12")));
				wRecord.setDay13(buildDay(objMap.get("day13")));
				wRecord.setDay14(buildDay(objMap.get("day14")));
				wRecord.setDay15(buildDay(objMap.get("day15")));
				wRecord.setDay16(buildDay(objMap.get("day16")));
				wRecord.setDay17(buildDay(objMap.get("day17")));
				wRecord.setDay18(buildDay(objMap.get("day18")));
				wRecord.setDay19(buildDay(objMap.get("day19")));
				wRecord.setDay20(buildDay(objMap.get("day20")));
				wRecord.setDay21(buildDay(objMap.get("day21")));
				wRecord.setDay22(buildDay(objMap.get("day22")));
				wRecord.setDay23(buildDay(objMap.get("day23")));
				wRecord.setDay24(buildDay(objMap.get("day24")));
				wRecord.setDay25(buildDay(objMap.get("day25")));
				wRecord.setDay26(buildDay(objMap.get("day26")));
				wRecord.setDay27(buildDay(objMap.get("day27")));
				wRecord.setDay28(buildDay(objMap.get("day28")));
				wRecord.setDay29(buildDay(objMap.get("day29")));
				wRecord.setDay30(buildDay(objMap.get("day30")));
				wRecord.setDay31(buildDay(objMap.get("day31")));
				
				
				wRecord.setOvertime(buildDay(objMap.get("overtime")));//法定加班工时
				wRecord.setAdjholiday(buildDay(objMap.get("adjholiday")));//倒休
				
				//统计月累计
				execMonthCount(wRecord);
				
				
				wRecord.setWorkdays(buildDay(objMap.get("workdays")));//出勤
				wRecord.setAdjholiday(buildDay(objMap.get("adjholiday")));//倒休
				wRecord.setEventday(buildDay(objMap.get("eventday")));//事假	
				wRecord.setBadday(buildDay(objMap.get("badday")));//病假	
				wRecord.setWedday(buildDay(objMap.get("wedday")));//婚假
				wRecord.setBadday(buildDay(objMap.get("badday")));//病假
				wRecord.setProday(buildDay(objMap.get("proday")));//产假
				wRecord.setLoseday(buildDay(objMap.get("loseday")));//丧假
				wRecord.setYearholiday(buildDay(objMap.get("yearholiday")));//年休假
				wRecord.setTripday(buildDay(objMap.get("tripday")));//出差
				wRecord.setWorkhurtday(buildDay(objMap.get("workhurtday")));//工伤假
				wRecord.setAbsenceday(buildDay(objMap.get("absenceday")));//旷工
				wRecord.setLateday(buildDay(objMap.get("lateday")));//迟到
				wRecord.setLeaveearlyday(buildDay(objMap.get("leaveearlyday")));//早退
				wRecord.setAllowdays(buildDay(objMap.get("allowdays")));//饭补天数
				wRecord.setScore(buildDay(objMap.get("score")));//绩效分
				
				preSaveObject(wRecord);
				saveObject(wRecord);
				
			}
			return workRecordTotal;
		}
		return null;
	}*/
	
	
	/**
	 * 保存的时候计算 月统计
	 * @param o
	 * @return
	 */
	private WorkRecord execMonthCount(WorkRecord wRecord){
		
		List<String> days = new ArrayList<String>();
		
		try {
			Field[] fields = wRecord.getClass().getDeclaredFields();
			for(Field f:fields){
				f.setAccessible(true);
				String name = f.getName();
				if(name.startsWith("day")){
					Object obj = f.get(wRecord);
					
					if(obj!=null){
						days.add(obj.toString());
					}else{
						days.add("");
					}
				}
			}
		} catch (Exception e) {
		}
		//事假∧,病假△,婚假☆,丧假□,产假﹢,年休假◎,
		double workdays=0,adjholiday=0,eventday=0,badday=0,weddday=0,proday=0,loseday=0,yearholiday=0,tripday=0,workhurtday=0,absenceday=0,lateday=0,leaveearlyday=0;
		int eatsup=0;//餐补 
		double overtime=0;//加班总工时 
		
		double totalovertime=0;//排班总工时 
		double realovertime = 0;//出勤总工时 
		
		Map<String,Integer> map_resultMap = new HashMap<String, Integer>();
		//事假∧，病假△，婚假☆，丧假□，产假﹢，年休假◎，出差※，工伤◇，旷工×，迟到﹤，早退﹥
		for(String val : days){
			//事假∧ 计算天数及工时数
			if(val!=null&&val.contains("∧")){
				if(val.contains("0.5")){
					eventday=eventday+0.5;
					workdays=workdays+0.5;
					//工时
					if(val.contains("A")){
						totalovertime=totalovertime+10;
						realovertime=realovertime+5;
					}else if(val.contains("B")){
						totalovertime=totalovertime+10;
						realovertime=realovertime+5;
					}else if(val.contains("C")){
						totalovertime=totalovertime+8;
						realovertime=realovertime+4;
					}else if(val.contains("D")){
						totalovertime=totalovertime+8;
						realovertime=realovertime+4;
					}
				}else{
					eventday++;
					//工时
					if(val.contains("A")){
						totalovertime=totalovertime+10;
					}else if(val.contains("B")){
						totalovertime=totalovertime+10;
					}else if(val.contains("C")){
						totalovertime=totalovertime+8;
					}else if(val.contains("D")){
						totalovertime=totalovertime+8;
					}
				}
			}
			//病假 △ 计算天数及工时数
			if(val!=null&&val.contains("△")){
				if(val.contains("0.5")){
					badday=badday+0.5;
					workdays=workdays+0.5;
					//工时
					if(val.contains("A")){
						totalovertime=totalovertime+10;
						realovertime=realovertime+5;
					}else if(val.contains("B")){
						totalovertime=totalovertime+10;
						realovertime=realovertime+5;
					}else if(val.contains("C")){
						totalovertime=totalovertime+8;
						realovertime=realovertime+4;
					}else if(val.contains("D")){
						totalovertime=totalovertime+8;
						realovertime=realovertime+4;
					}
				}else{
					badday++;
					//工时
					if(val.contains("A")){
						totalovertime=totalovertime+10;
					}else if(val.contains("B")){
						totalovertime=totalovertime+10;
					}else if(val.contains("C")){
						totalovertime=totalovertime+8;
					}else if(val.contains("D")){
						totalovertime=totalovertime+8;
					}
				}
			}
			
			//旷工 计算天数及工时数
			if(val!=null&&val.contains("×")){
				if(val.contains("0.5")){
					absenceday=absenceday+0.5;
					workdays=workdays+0.5;
					//工时
					if(val.contains("A")){
						totalovertime=totalovertime+10;
						realovertime=realovertime+5;
					}else if(val.contains("B")){
						totalovertime=totalovertime+10;
						realovertime=realovertime+5;
					}else if(val.contains("C")){
						totalovertime=totalovertime+8;
						realovertime=realovertime+4;
					}else if(val.contains("D")){
						totalovertime=totalovertime+8;
						realovertime=realovertime+4;
					}
				}else{
					absenceday++;
					//工时
					if(val.contains("A")){
						totalovertime=totalovertime+10;
					}else if(val.contains("B")){
						totalovertime=totalovertime+10;
					}else if(val.contains("C")){
						totalovertime=totalovertime+8;
					}else if(val.contains("D")){
						totalovertime=totalovertime+8;
					}
				}
			}
			
			//婚假 计算天数及工时数
			if(val!=null&&val.contains("☆")){
				weddday++;//婚假
				if(val.contains("A")){
					totalovertime=totalovertime+10;
				}else if(val.contains("B")){
					totalovertime=totalovertime+10;
				}else if(val.contains("C")){
					totalovertime=totalovertime+8;
				}else if(val.contains("D")){
					totalovertime=totalovertime+8;
				}
			}
			//丧假 计算天数及工时数
			if(val!=null&&val.contains("□")){
				loseday++;//丧假
				if(val.contains("A")){
					totalovertime=totalovertime+10;
				}else if(val.contains("B")){
					totalovertime=totalovertime+10;
				}else if(val.contains("C")){
					totalovertime=totalovertime+8;
				}else if(val.contains("D")){
					totalovertime=totalovertime+8;
				}
			}
			//产假 计算天数及工时数
			if(val!=null&&val.contains("﹢")){
				proday++;//产假
				if(val.contains("A")){
					totalovertime=totalovertime+10;
				}else if(val.contains("B")){
					totalovertime=totalovertime+10;
				}else if(val.contains("C")){
					totalovertime=totalovertime+8;
				}else if(val.contains("D")){
					totalovertime=totalovertime+8;
				}
			}
			//年休假 计算天数
			if(val!=null&&val.contains("◎")){
				yearholiday++;//年休假 
				totalovertime=totalovertime+8;
			}
			//出差 计算天数及工时数
			if(val!=null&&val.contains("※")){
				tripday++;//出差
				workdays++;
				totalovertime=totalovertime+8;
				realovertime=realovertime+8;
			}
			//工伤 计算天数及工时数
			if(val!=null&&val.contains("◇")){
				workhurtday++;//工伤 
				if(val.contains("A")){
					totalovertime=totalovertime+10;
				}else if(val.contains("B")){
					totalovertime=totalovertime+10;
				}else if(val.contains("C")){
					totalovertime=totalovertime+8;
				}else if(val.contains("D")){
					totalovertime=totalovertime+8;
				}
			}
			/*//旷工  计算天数及工时数 
			if(val!=null&&val.contains("×")){
				absenceday++;//旷工 
				if(val.contains("A")){
					totalovertime=totalovertime+10;
				}else if(val.contains("B")){
					totalovertime=totalovertime+10;
				}else if(val.contains("C")){
					totalovertime=totalovertime+8;
				}else if(val.contains("D")){
					totalovertime=totalovertime+8;
				}
			}*/
			//迟到  计算天数及工时数 
			if(val!=null&&val.contains("﹤")){
				lateday++;//迟到 
				workdays++;
				if(val.contains("A")){
					totalovertime=totalovertime+10;
					realovertime=realovertime+10;
				}else if(val.contains("B")){
					totalovertime=totalovertime+10;
					realovertime=realovertime+10;
				}else if(val.contains("C")){
					totalovertime=totalovertime+8;
					realovertime=realovertime+8;
				}else if(val.contains("D")){
					totalovertime=totalovertime+8;
					realovertime=realovertime+8;
				}
			}
			//早退 计算天数及工时数
			if(val!=null&&val.contains("﹥")){
				leaveearlyday++;
				workdays++;
				if(val.contains("A")){
					totalovertime=totalovertime+10;
					realovertime=realovertime+10;
				}else if(val.contains("B")){
					totalovertime=totalovertime+10;
					realovertime=realovertime+10;
				}else if(val.contains("C")){
					totalovertime=totalovertime+8;
					realovertime=realovertime+8;
				}else if(val.contains("D")){
					totalovertime=totalovertime+8;
					realovertime=realovertime+8;
				}
			}
			
			
			
			
			
			//正常的A 计算天数及工时 
			if(val!=null&&val.equals("A")){
				workdays++;
				totalovertime=totalovertime+10;
				realovertime=realovertime+10;
			}
			//正常的B 计算天数及工时
			if(val!=null&&val.equals("B")){
				workdays++;
				totalovertime=totalovertime+10;
				realovertime=realovertime+10;
			}
			//正常的C 计算天数及工时 
			if(val!=null&&val.equals("C")){
				workdays++;
				totalovertime=totalovertime+8;
				realovertime=realovertime+8;
			}
			//正常的D
			if(val!=null&&val.equals("D")){
				workdays++;
				totalovertime=totalovertime+8;
				realovertime=realovertime+8;
			}
			
			
			//出勤1  正常数字的工时 计算 
			if(val!=null&&val.equals("1")){
				workdays++;
				totalovertime=totalovertime+1;
				realovertime=realovertime+1;
			}
			//出勤2 正常数字的工时 计算 
			if(val!=null&&val.equals("2")){
				workdays++;
				totalovertime=totalovertime+2;
				realovertime=realovertime+2;
			}
			//出勤3 正常数字的工时 计算 
			if(val!=null&&val.equals("3")){
				workdays++;
				totalovertime=totalovertime+3;
				realovertime=realovertime+3;
			}
			//出勤4 正常数字的工时 计算 
			if(val!=null&&val.equals("4")){
				workdays++;
				totalovertime=totalovertime+4;
				realovertime=realovertime+4;
			}
			//出勤5 正常数字的工时 计算 
			if(val!=null&&val.equals("5")){
				workdays++;
				totalovertime=totalovertime+5;
				realovertime=realovertime+5;
			}
			//出勤6 正常数字的工时 计算 
			if(val!=null&&val.equals("6")){
				workdays++;
				totalovertime=totalovertime+6;
				realovertime=realovertime+6;
			}
			//出勤7 正常数字的工时 计算 
			if(val!=null&&val.equals("7")){
				workdays++;
				totalovertime=totalovertime+7;
				realovertime=realovertime+7;
			}
			//出勤8正常数字的工时 计算 
			if(val!=null&&val.equals("8")){
				workdays++;
				totalovertime=totalovertime+8;
				realovertime=realovertime+8;
			}
			//出勤9 正常数字的工时 计算 
			if(val!=null&&val.equals("9")){
				workdays++;
				totalovertime=totalovertime+9;
				realovertime=realovertime+9;
			}
			
		}
		
		
		wRecord.setEventday(eventday==0?"":formatnum(eventday)+"");//事假	
		wRecord.setBadday(badday==0?"":formatnum(badday)+"");//病假	
		wRecord.setWedday(weddday==0?"":formatnum(weddday)+"");//婚假
		wRecord.setProday(proday==0?"":formatnum(proday)+"");//产假
		wRecord.setLoseday(loseday==0?"":formatnum(loseday)+"");//丧假
		wRecord.setYearholiday(yearholiday==0?"":formatnum(yearholiday)+"");//年休假
		wRecord.setTripday(tripday==0?"":formatnum(tripday)+"");//出差
		wRecord.setWorkhurtday(workhurtday==0?"":formatnum(workhurtday)+"");//工伤假
		wRecord.setAbsenceday(absenceday==0?"":formatnum(absenceday)+"");//旷工
		wRecord.setLateday(lateday==0?"":formatnum(lateday)+"");//迟到
		wRecord.setLeaveearlyday(leaveearlyday==0?"":formatnum(leaveearlyday)+"");//早退
		
		//倒休不算出勤 
		/*double ajdholiday = wRecord.getAdjholiday()==""?0:Double.parseDouble(wRecord.getAdjholiday());
		workdays=workdays+ajdholiday;*/
		
		wRecord.setWorkdays(workdays==0?"":formatnum(workdays)+"");//出勤天数
		
		//餐补 实际出勤/8取整
		eatsup = (int)realovertime/8;
		wRecord.setAllowdays(workdays==0?"":formatnum(eatsup)+"");//饭补天数
		
		//排班工时
		wRecord.setTotalovertime(totalovertime);
		//实际出勤工时
		wRecord.setRealovertime(realovertime);
		
		return wRecord;
	}
	
	
	
	private String formatnum(double ddd) {
		String excDouble = ""+ddd;
		if(!excDouble.contains(".5")){
			excDouble=""+(int)(ddd);
		}
		return excDouble;
	}
	
	
	
	
	
	
	protected String buildDay(Object o){
		try {
			return o==null?"":o.toString();
		} catch (Exception e) {
		}
		return "";
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

	//保存打分信息 
	@Override
	public ScoreRecordTotal saveScoreRecordList(
			List<Map<String, Object>> objList, String work_month, Long store_id,Long work_info_id) {
		try {
			if(objList!=null&&objList.size()>0){
				UserManager manager = (UserManager)SpringHelper.getBean("userManager");
				UserDTO userDTO = manager.getCurrentUserDTO();
				StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
				Store store = null;
				if(store_id!=null&&store_id>0){
					store = storeManager.findStore(store_id);
				}else{
					store = storeManager.findStore(userDTO.getStore_id());
				}
				
				//修改workinfo.commit_status = 0;
				WorkInfo workInfo = null;
				if(work_info_id!=null){
					WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
					workInfo = (WorkInfo) workInfoManager.getObject(work_info_id);
					workInfo.setCommit_status(Long.parseLong("0"));
					preSaveObject(work_info_id);
					workInfoManager.saveObject(workInfo);
				}
				
				//保存、修改主表
				ScoreRecordTotal scoreRecordTotal = new ScoreRecordTotal();
				scoreRecordTotal.setStore_id(store.getStore_id());
				scoreRecordTotal.setCityname(store.getCityName());
				scoreRecordTotal.setCommit_status(Long.parseLong("0"));
				scoreRecordTotal.setWork_month(work_month);
				scoreRecordTotal.setWork_info_id(work_info_id);
				scoreRecordTotal.setStr_work_info_id(workInfo==null?"":workInfo.getWork_type());
				
				if(objList.get(0).get("scorerecord_id")!=null&&!objList.get(0).get("scorerecord_id").toString().equals("null")){
					Long scorerecord_id = Long.parseLong(objList.get(0).get("scorerecord_id").toString());
					scoreRecordTotal.setId(Long.parseLong(objList.get(0).get("scorerecord_id").toString()));
					
					ScoreRecordTotalManager scoreRecordTotalManager = (ScoreRecordTotalManager)SpringHelper.getBean("scoreRecordTotalManager");
					ScoreRecordTotal scRecordTotal = (ScoreRecordTotal) scoreRecordTotalManager.getObject(scorerecord_id);
					scRecordTotal.setCityname(store.getCityName());
					scRecordTotal.setCommit_status(Long.parseLong("0"));
					preSaveObject(scRecordTotal);
					saveObject(scRecordTotal);
				}else{
					preSaveObject(scoreRecordTotal);
					saveObject(scoreRecordTotal);
				}
				
				//主表结束
				//子表开始
				if(scoreRecordTotal.getId()!=null){
					ScoreRecordDao scoreRecordDao = (ScoreRecordDao)SpringHelper.getBean(ScoreRecordDao.class.getName());
					scoreRecordDao.delScoreRecoedById(scoreRecordTotal.getId());
				}
				for(Map<String, Object> objMap : objList){
					ScoreRecord sRecord = new ScoreRecord();
					
					sRecord.setScorerecord_id(scoreRecordTotal.getId());
					sRecord.setStore_name(userDTO.getStore_name());
					
					sRecord.setEmployee_no(objMap.get("employee_no").toString());
					sRecord.setEmployee_name(objMap.get("employee_name").toString());
					sRecord.setWork_month(work_month);
					
					sRecord.setScore(buildDay(objMap.get("score")));//绩效分
					/*sRecord.setMixedType_repeatBuyCostomer(buildDay(objMap.get("mixedType_repeatBuyCostomer")));//绩效分
					sRecord.setTurnover(buildDay(objMap.get("turnover")));//绩效分
					sRecord.setStoreroom(buildDay(objMap.get("storeroom")));//绩效分
					sRecord.setEmprise(buildDay(objMap.get("emprise")));//绩效分
*/					
					preSaveObject(sRecord);
					saveObject(sRecord);
					
				}
				return scoreRecordTotal;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String saveScoreRecordForExcel(Long scorerecord_id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//取得考勤发起表中的已经发起的最大的月份。
	@Override
	public WorkMonth queryMaxWorkMonth(){
		WorkMonthManager workMonthManager = (WorkMonthManager) SpringHelper.getBean("workMonthManager");
		//取得当前登录人 所管理城市
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
		
		if(cityssql!=""&&cityssql.length()>0){
			sbfCondition.append(" cityname in ("+cityssql+")");
		}else{
			sbfCondition.append(" 0=1 ");
		}
		
		IFilter iFilter = FilterFactory.getSimpleFilter(sbfCondition.toString());
    	
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date_next = null;
        FSP fsp = new FSP();
        IPage pageInfo = new PageInfo();
        pageInfo.setCurrentPage(1);
        pageInfo.setRecordsPerPage(1);
        fsp.setPage(pageInfo);
        
        fsp.setUserFilter(iFilter);
        
        fsp.setSort(SortFactory.createSort("id", ISort.DESC));
        List<?> lst_data = workMonthManager.getList(fsp);
        if(lst_data != null && lst_data.size() > 0){
            return (WorkMonth)lst_data.get(0);
        }
        return null;
	}
	
	
	
	
}
