package com.cnpc.pms.personal.manager.impl;

import java.io.*;
import java.math.BigDecimal;
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
import javax.swing.Spring;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.util.FileCopyUtils;
import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
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
import com.cnpc.pms.personal.dao.CityHumanresourcesDao;
import com.cnpc.pms.personal.dao.ExpressDao;
import com.cnpc.pms.personal.dao.HouseStyleDao;
import com.cnpc.pms.personal.dao.HumanenteachDao;
import com.cnpc.pms.personal.dao.HumanresourcesDao;
import com.cnpc.pms.personal.dao.ImportHumanresourcesDao;
import com.cnpc.pms.personal.dao.RelationDao;
import com.cnpc.pms.personal.dao.StoreKeeperDao;
import com.cnpc.pms.personal.dao.YyMicrDataDao;
import com.cnpc.pms.personal.entity.CityHumanresources;
import com.cnpc.pms.personal.entity.CityHumanresourcesChange;
import com.cnpc.pms.personal.entity.Company;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.DistCityCode;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.Humanenteach;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.HumanresourcesChange;
import com.cnpc.pms.personal.entity.ImportHumanresources;
import com.cnpc.pms.personal.entity.OuterCompany;
import com.cnpc.pms.personal.entity.SelfExpress;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreKeeper;
import com.cnpc.pms.personal.entity.XxExpress;
import com.cnpc.pms.personal.manager.BuildingManager;
import com.cnpc.pms.personal.manager.CityHumanresourcesChangeManager;
import com.cnpc.pms.personal.manager.CityHumanresourcesManager;
import com.cnpc.pms.personal.manager.CompanyManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.DistCityCodeManager;
import com.cnpc.pms.personal.manager.DistCityManager;
import com.cnpc.pms.personal.manager.ExpressManager;
import com.cnpc.pms.personal.manager.HouseManager;
import com.cnpc.pms.personal.manager.HumanenteachManager;
import com.cnpc.pms.personal.manager.HumanresourcesChangeManager;
import com.cnpc.pms.personal.manager.HumanresourcesLogManager;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
import com.cnpc.pms.personal.manager.ImportHumanresourcesManager;
import com.cnpc.pms.personal.manager.OuterCompanyManager;
import com.cnpc.pms.personal.manager.StoreKeeperManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.platform.dao.OrderDao;
import com.cnpc.pms.platform.entity.PlatformEmployee;
import com.cnpc.pms.platform.manager.OrderManager;
import com.cnpc.pms.platform.manager.PlatformEmployeeManager;
import com.cnpc.pms.utils.ChineseToEnglish;
import com.cnpc.pms.utils.CompressFile;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.EntityEquals;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;
import com.ibm.db2.jcc.am.up;

@SuppressWarnings("all")
public class CityHumanresourcesManagerImpl extends BizBaseCommonManager implements CityHumanresourcesManager {
    
	PropertiesValueUtil propertiesValueUtil = null;

    private String folder_path = null;

    /**
     * 到处户型excel单元格公共样式
     */
    CellStyle cellStyle_common = null;
    
    
    
    /**
     * 查询列表 
     */
    @Override
    public Map<String, Object> queryCityHumanList(QueryConditions condition) {
    	UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
    	StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
    	/*UserDTO userDTO = manager.getCurrentUserDTO();
		Long store_id = userDTO.getStore_id();*/
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		String name = null;
		String employee_no = null;
		String humanstatus=null;
		String zw = null;
		String citySelect=null;
		for(Map<String, Object> map : condition.getConditions()){
			if("name".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				name = map.get("value").toString();
			}
			if("employee_no".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				employee_no = map.get("value").toString();
			}
			if("humanstatus".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				humanstatus = map.get("value").toString();
			}
			if("zw".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				zw = map.get("value").toString();
			}
			if("citySelect".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				citySelect = map.get("value").toString();
			}
			
		}
		List<?> lst_data = null;
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("id", ISort.DESC));
		StringBuffer sbfCondition = new StringBuffer(); 
		
		sbfCondition.append(" 1=1 ");
		
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
			sbfCondition.append(" and citySelect in ("+cityssql+")");
		}else{
			sbfCondition.append(" and 0=1 ");
		}
		
		
		if(name!=null){
			sbfCondition.append(" and name like '%"+name+"%'");
		}
		if(employee_no!=null){
			sbfCondition.append(" and employee_no like '%"+employee_no+"%'");
		}
		if(humanstatus!=null){
			sbfCondition.append(" and humanstatus = '"+humanstatus+"'");
		}
		if(zw!=null){
			sbfCondition.append(" and zw like '%"+zw+"%'");
		}
		if(citySelect!=null){
			sbfCondition.append(" and citySelect like '%"+citySelect+"%'");
		}
		
		sbfCondition.append(" order by humanstatus ASC,DATE_FORMAT(topostdate,'%Y-%m-%d') DESC,update_time DESC ");
		
		IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition.toString());
		fsp.setPage(pageInfo);
		fsp.setUserFilter(iFilter);
		lst_data = this.getList(fsp);
		List<CityHumanresources> cityHumanresources = null;
		if(lst_data!=null&&lst_data.size()>0){
			cityHumanresources = new ArrayList<CityHumanresources>();
			for(Object o:lst_data){
				CityHumanresources showCityHumanresources = (CityHumanresources) o;
				
				Calendar try_calendar = Calendar.getInstance();
				
				String val_trytip = "";
				String tryEnd  = showCityHumanresources.getTrydateend();
				
				if(showCityHumanresources.getHumanstatus()!=2L&&showCityHumanresources.getTrydateend()!=null&&showCityHumanresources.getTrydateend().length()>7&&showCityHumanresources.getTrydateend().length()<=10){
					if(tryEnd!=null&&tryEnd.length()>0){
						try {
							Date try_date = new SimpleDateFormat("yyyy-MM-dd").parse(showCityHumanresources.getTrydateend().replace("/", "-"));
							int try_days = differentDays(try_calendar.getTime(),try_date);
							if(try_days>0){
								val_trytip="离转正还有"+try_days+"天";
							}else{
								val_trytip="试用期已过";
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}else{
					val_trytip="无";
				}
				
				showCityHumanresources.setTrytipmessage(val_trytip);
				cityHumanresources.add(showCityHumanresources);
			}
		}else{
			cityHumanresources = new ArrayList<CityHumanresources>();
		}
		
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", cityHumanresources);
		return returnMap;
	}
    
    
    
    /**
     * 导入城市员工 的数据
     */
    @Override
    public String saveHumanresourceCSHuman(List<File> lst_import_excel) throws Exception {
    	String rcvmsg = null;
    	StoreKeeperManager storeKeeperManager = (StoreKeeperManager) SpringHelper.getBean("storeKeeperManager");
    	StoreKeeperDao storeKeeperDao = (StoreKeeperDao) SpringHelper.getBean(StoreKeeperDao.class.getName());
    	for(File file_excel : lst_import_excel) {
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
            /* 
                                                公司	部门	板块	岗位	职级	姓名	性别	民族	籍贯	户籍	户口性质	出生年月	年龄	学历	学位	毕业院校	专业	职称	职业资格	婚姻状况	
              	政治面貌	入党时间	参加工作时间	进中信时间	身份证号	到岗日	"劳动合同签订主体"	合同类型	开始时间	终止时间	提醒	"签订次数"	试用开始日	试用期截止日	联系方式	发薪主体	备注	情况说明	家庭通信地址	户籍所在地	试用期提醒
             */
           
            Map<String, Object> employMaphs = new HashMap<String, Object>();
            String[] cityhumantitles = {"公司","部门","板块","岗位","职级","姓名","员工编号","性别","民族","籍贯","户籍","户口性质","出生年月","年龄","学历","学位","毕业院校","专业","职称","职业资格","婚姻状况","政治面貌","入党时间","参加工作时间","进中信时间","身份证号","到岗日","签订主体","合同类型","开始时间","终止时间","提醒","次数","试用开始日","试用期截止日","联系方式","发薪主体","备注","情况说明","家庭通信地址","户籍所在地","试用期提醒"};           
             
            Map<String, Integer> maps = new HashMap<String, Integer>();
            String deptName = "";
            //只读第一个sheet页
            sheet_data = wb_excel.getSheetAt(0);
            int ret = sheet_data.getPhysicalNumberOfRows();
            
            
            //取当前登录人所管理城市 
            UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
            List<DistCity> distCities = userManager.getCurrentUserCity();
            String citySelect = "";
            String citySelectCode = "";
            if(distCities!=null&&distCities.size()>0){
            	DistCity dCity = distCities.get(0);
            	citySelect = dCity.getCityname();
            	citySelectCode = dCity.getCitycode();
            }else{
            	rcvmsg = "当前登录人无城市权限！ 导入失败！ ";
            	return rcvmsg;
            }
            
            //查询当前城市最大的员工号
            String employee_no_type="CS"+citySelectCode;  //固定城市人员前缀+城市的code
            CityHumanresourcesDao cityHumanresourcesDao = (CityHumanresourcesDao) SpringHelper.getBean(CityHumanresourcesDao.class.getName());
            
            String maxNo = cityHumanresourcesDao.queryMaxEmpNo(employee_no_type);
            String maxEmployee_no = "";
            if(maxNo=="00000"){
            	maxEmployee_no = employee_no_type+"00000";
            }else{
            	maxEmployee_no = maxNo;
            }
            
            //取得店长的最大员工号
            String storekeeper_maxemployee_no = storeKeeperDao.queryMaxNo("SK");
            String storekeeper_maxemployee_no_rm = storeKeeperDao.queryMaxNo("RM");
            
            List<CityHumanresources> cityHumanresourcesList = new ArrayList<CityHumanresources>();
           
            int employee_num = 1;
            int storekeeper_num = 1;
            int storekeeper_num_rm = 1;
            for(int nRowIndex = 0;nRowIndex < sheet_data.getPhysicalNumberOfRows();nRowIndex++){
            	Row row_human = sheet_data.getRow(nRowIndex);
            	 if(row_human == null){
 	            	rcvmsg="导入文件格式不正确！导入失败！行号："+(nRowIndex+1);
                 	return rcvmsg;
 	            }
            	if(row_human == null){
	            	continue;
	            }
            	
            	if(maps.size() < 3){
                	int nCellSize = row_human.getPhysicalNumberOfCells();
                	for(int nCellIndex = 0;nCellIndex < nCellSize ;nCellIndex ++){
                		String str_value = getCellValue(row_human.getCell(nCellIndex));
                		String str_title = isHave(cityhumantitles,str_value);
                		if(str_title != null){
                			maps.put(str_title, nCellIndex);
                		}
                	}
                	if(maps.size() >= 3){
                		maps.put("nStartRow", nRowIndex);
                	}
                }
            	
            	if(nRowIndex==1&&maps.size()<43){
 	            	rcvmsg="导入文件格式不正确！导入失败！行号："+(nRowIndex+1);
                 	return rcvmsg;
 	            }
 	            
 	             Integer nStartRow =  maps.get("nStartRow");
                 if(nStartRow != null && nRowIndex <= nStartRow){
                 	continue;
                 }
                 if(nStartRow == null){
                 	continue;
                 }
                 /*String total_sum = getStringMapValue(row_human, maps, "总数");
                 if(total_sum!=null&&total_sum.contains("1.0")){total_sum = "1"; }*/
                 String citycompany = getMapValue(row_human, maps, "公司");
                 if(citycompany!=null&&citycompany.length()>0){
                	 citycompany = citycompany.replaceAll("\\s*", "");
                 }
                 
                 String deptname = getMapValue(row_human, maps, "部门");
                 if(deptname!=null&&deptname.length()>0){
                	 deptname = deptname.replaceAll("\\s*", "");
                 }
                 
                 String deptlevel1 = getMapValue(row_human, maps, "板块");
                 if(deptlevel1!=null&&deptlevel1.length()>0){
                	 deptlevel1 = deptlevel1.replaceAll("\\s*", "");
                 }
                 
                 
                 String zw = getMapValue(row_human, maps, "岗位");
                 if(zw!=null&&zw.length()>0){
                	 zw = zw.replaceAll("\\s*", "");
                 }
                  
                 String professnallevel = getMapValue(row_human, maps, "职级");
                 if(professnallevel!=null&&professnallevel.length()>0){
                	 professnallevel = professnallevel.replaceAll("\\s*", "");
                 }
                 String name = getMapValue(row_human, maps, "姓名");
                 
                 String employee_no = getMapValue(row_human, maps, "员工编号");
                 if(employee_no!=null&&employee_no.trim().length()>0){
                	rcvmsg="存在员工编号！导入失败！行号："+(nRowIndex+1);
                  	return rcvmsg;
                 }
                 
                 String sex = getMapValue(row_human, maps, "性别");
                 String nation = getMapValue(row_human, maps, "民族");
                 String nativeplace = getMapValue(row_human, maps, "籍贯");
                 String censusregister = getMapValue(row_human, maps, "户籍");
                 String censusregistertype = getMapValue(row_human, maps, "户口性质");
                 //String birthday = getCellValueFormulaEvaluator(row_human, maps, "出生年月",wb_excel);
                 String age = getMapValue(row_human, maps, "年龄");
                 
                 String education = getMapValue(row_human, maps, "学历");
                 String educationlevel = getMapValue(row_human, maps, "学位");
                 String school = getMapValue(row_human, maps, "毕业院校");
                 String profession = getMapValue(row_human, maps, "专业");
                 String professiontitle = getMapValue(row_human, maps, "职称");
                 String credentials = getMapValue(row_human, maps, "职业资格");
                 String marriage = getMapValue(row_human, maps, "婚姻状况");
                 String partisan = getMapValue(row_human, maps, "政治面貌");
                 String partisandate = getMapValue(row_human, maps, "入党时间");
                 String workdate = getMapValue(row_human, maps, "参加工作时间");
                 
                 String entrydate = getMapValue(row_human, maps, "进中信时间");
                 String cardnumber = getMapValue(row_human, maps, "身份证号");
                 if(cardnumber==null||cardnumber.trim().equals("")){
                	 rcvmsg="身份证号不能为空！导入失败！行号："+(nRowIndex+1);
                   	 return rcvmsg;
                 }
                 if(!cardnumber.equals("/")){
                	 if(cardnumber!=null&&cardnumber.length()!=18){
                    	 rcvmsg="身份证号格式错误！导入失败！行号："+(nRowIndex+1);
                       	 return rcvmsg;
                     }
                 }
              	
                 String topostdate = getMapValue(row_human, maps, "到岗日");
                 String contractcompany = getMapValue(row_human, maps, "签订主体");
                 if(contractcompany!=null&&contractcompany.length()>0){
                	 contractcompany = contractcompany.replaceAll("\\s*", "");
                 }
                 
                 String contracttype = getMapValue(row_human, maps, "合同类型");
                 String contractdatestart = getMapValue(row_human, maps, "开始时间");
                 String contractdateend = getMapValue(row_human, maps, "终止时间");
                 String tipmessage = getMapValue(row_human, maps, "提醒");
                 String signcount = getMapValue(row_human, maps, "次数");
                 String trydatestart = getMapValue(row_human, maps, "试用开始日");
                 
                 String trydateend = getMapValue(row_human, maps, "试用期截止日");
                 String phone = getMapValue(row_human, maps, "联系方式");
                 String paycompany = getMapValue(row_human, maps, "发薪主体");
                 String remark = getMapValue(row_human, maps, "备注");
                 String other = getMapValue(row_human, maps, "情况说明");
                 String houseaddress = getMapValue(row_human, maps, "家庭通信地址");
                 String cardaddress = getMapValue(row_human, maps, "户籍所在地");
                 String trytipmessage = getMapValue(row_human, maps, "试用期提醒");
                 
                 CityHumanresources cityHumanresources = new CityHumanresources();
                 
                 String birthday_calc = "";
                 if(cardnumber.length()==18){
                	 //处理生日
                     String year = cardnumber.substring(6,10);
              		 String month = cardnumber.substring(10,12);
              		 String days = cardnumber.substring(12,14);
              		 birthday_calc = year+"-"+month+"-"+days;
                     //处理性别
              		 String sexnum = cardnumber.substring(cardnumber.length()-2,cardnumber.length()-1);
              		 if("13579".contains(sexnum)){
            			sex="男";
            		 }else{
            			sex="女";
            		 }
              		 
              		 Calendar a=Calendar.getInstance();
              		 int now_year = a.get(Calendar.YEAR);
              		 int birth_year = Integer.parseInt(year);
              		 age = (now_year-birth_year)+"";
                 }
                 //默认在职
          		 cityHumanresources.setHumanstatus(Long.parseLong("1"));
          		 //处理年龄
          		 
          		 //cityHumanresources.setTotalsum(total_sum);//总数
          		 
                 cityHumanresources.setCitycompany(citycompany);//公司
                 cityHumanresources.setDeptname(deptname);//部门
                 cityHumanresources.setDeptlevel1(deptlevel1);//板块 
                 
                 cityHumanresources.setZw(zw);//岗位
                 cityHumanresources.setProfessnallevel(professnallevel);//职级
                 cityHumanresources.setName(name);//姓名
                 cityHumanresources.setSex(sex);//性别
                 cityHumanresources.setNation(nation);//民族
                 cityHumanresources.setNativeplace(nativeplace);//籍贯
                 cityHumanresources.setCensusregister(censusregistertype);//户籍
                 cityHumanresources.setCensusregistertype(censusregistertype);//户口性质
                 cityHumanresources.setBirthday(birthday_calc);//出生年月 
                 cityHumanresources.setAge(age);//年龄
                 
                 cityHumanresources.setEducation(education);//学历
                 cityHumanresources.setEducationlevel(educationlevel);//学位
                 cityHumanresources.setSchool(school);//毕业院校
                 cityHumanresources.setProfession(profession);//专业
                 cityHumanresources.setProfessiontitle(professiontitle);//职称
                 cityHumanresources.setCredentials(credentials);//职业资格
                 cityHumanresources.setMarriage(marriage);//婚姻状况
                 cityHumanresources.setPartisan(partisan);//政治面貌
                 cityHumanresources.setPartisandate(partisandate);//入党时间
                 cityHumanresources.setWorkdate(workdate);//参加工作时间
                 
                 cityHumanresources.setEntrydate(entrydate);//进中信时间
                 cityHumanresources.setCardnumber(cardnumber);//身份证号
                 cityHumanresources.setTopostdate(topostdate);//到岗日
                 cityHumanresources.setContractcompany(contractcompany);//签订主体
                 cityHumanresources.setContracttype(contracttype);//合同类型
                 cityHumanresources.setContractdatestart(contractdatestart);//开始时间
                 cityHumanresources.setContractdateend(contractdateend);//终止时间
                 cityHumanresources.setTipmessage(tipmessage);//提醒
                 cityHumanresources.setSigncount(signcount);//次数
                 cityHumanresources.setTrydatestart(trydatestart);//试用开始日
                 
                 
                 cityHumanresources.setTrydateend(trydateend);//试用期截止日
                 cityHumanresources.setPhone(phone);//联系方式
                 cityHumanresources.setPaycompany(paycompany);//发薪主体
                 cityHumanresources.setRemark(remark);//备注
                 cityHumanresources.setOther(other);//情况说明
                 cityHumanresources.setHouseaddress(houseaddress);//家庭通信地址
                 cityHumanresources.setCardaddress(cardaddress);//所在地
                 cityHumanresources.setTrytipmessage(trytipmessage);//试用期提醒
                 
                 String new_employee_no = "";
                 Long new_humanstatus = cityHumanresources.getHumanstatus();
                 //如果是店长 员工编号规则 为SK
                 if(deptname!=null&&deptname.equals("门店管理中心")&&zw.contains("店长")){
                	 //根据姓名和电话查询 是否存在店长，如果存在 取出员工号，如果不存在则生成sk的员工号并保存店长表中并返回 。
                     StoreKeeper storeKeeper = storeKeeperManager.queryStoreKeeperByNamePhone(name, phone);
                     if(storeKeeper!=null&&storeKeeper.getEmployee_no()!=null){
                         new_employee_no = storeKeeper.getEmployee_no();
                         new_humanstatus = storeKeeper.getHumanstatus();
                     }else{
                    	 //生成员工号
                         String num = storekeeper_maxemployee_no.substring(storekeeper_maxemployee_no.length()-5,storekeeper_maxemployee_no.length());
                         int new_num = Integer.parseInt(num)+storekeeper_num;
                         String new_employee_no_num = ("00000"+new_num);
                         new_employee_no = "SK" + new_employee_no_num.substring(new_employee_no_num.length()-5, new_employee_no_num.length());
                         storekeeper_num ++;
                         
                         //保存到storekeeper 表中 
                         StoreKeeper save_storeKeeper = new StoreKeeper();
                         save_storeKeeper.setCitySelect(citySelect);
                         save_storeKeeper.setEmployee_no(new_employee_no);
                         save_storeKeeper.setHumanstatus(new_humanstatus);
                         save_storeKeeper.setName(name);
                         save_storeKeeper.setPhone(phone);
                         save_storeKeeper.setZw("店长");
                         preSaveObject(save_storeKeeper);
                         storeKeeperManager.saveObject(save_storeKeeper);
                     }
                 }else if(deptname!=null&&deptname.equals("门店管理中心")&&zw.contains("经理")&&zw.contains("运营")){
                	//根据姓名和电话查询 是否存在运营经理，如果存在 取出员工号，如果不存在则生成rm的员工号并保存店长表中并返回 。
                     StoreKeeper storeKeeper = storeKeeperManager.queryStoreKeeperByNamePhone(name, phone);
                     if(storeKeeper!=null&&storeKeeper.getEmployee_no()!=null){
                         new_employee_no = storeKeeper.getEmployee_no();
                         new_humanstatus = storeKeeper.getHumanstatus();
                     }else{
                    	 //生成员工号
                         String num = storekeeper_maxemployee_no_rm.substring(storekeeper_maxemployee_no_rm.length()-5,storekeeper_maxemployee_no_rm.length());
                         int new_num = Integer.parseInt(num)+storekeeper_num_rm;
                         String new_employee_no_num = ("00000"+new_num);
                         new_employee_no = "RM" + new_employee_no_num.substring(new_employee_no_num.length()-5, new_employee_no_num.length());
                         storekeeper_num_rm ++;
                         
                         //保存到storekeeper 表中 
                         StoreKeeper save_storeKeeper = new StoreKeeper();
                         save_storeKeeper.setCitySelect(citySelect);
                         save_storeKeeper.setEmployee_no(new_employee_no);
                         save_storeKeeper.setHumanstatus(new_humanstatus);
                         save_storeKeeper.setName(name);
                         save_storeKeeper.setPhone(phone);
                         save_storeKeeper.setZw("运营经理");
                         preSaveObject(save_storeKeeper);
                         storeKeeperManager.saveObject(save_storeKeeper);
                     }
                 }else{
                	 //生成员工号
                     String num = maxEmployee_no.substring(maxEmployee_no.length()-5,maxEmployee_no.length());
                     int new_num = Integer.parseInt(num)+employee_num;
                     String new_employee_no_num = ("00000"+new_num);
                     new_employee_no = employee_no_type + new_employee_no_num.substring(new_employee_no_num.length()-5, new_employee_no_num.length());
                     employee_num ++;
                     
                 }
                 cityHumanresources.setHumanstatus(new_humanstatus);
                 cityHumanresources.setEmployee_no(new_employee_no);
                 cityHumanresources.setCitySelect(citySelect);
                 cityHumanresourcesList.add(cityHumanresources);
                 
            }
            

            if(cityHumanresourcesList!=null&&cityHumanresourcesList.size()>0){
            	//校验是否存在相同的人（身份证号）验证身份证号重复
            	StringBuilder sb_cardnums = new StringBuilder();
            	for(int i=0;i<cityHumanresourcesList.size();i++){
            		if(cityHumanresourcesList.get(i).getCardnumber()!=null&&cityHumanresourcesList.get(i).getCardnumber().equals("/")){
        				continue;
        			}
            		if(sb_cardnums!=null&&sb_cardnums.toString().length()>0){
            			sb_cardnums.append(",'");
            			sb_cardnums.append(cityHumanresourcesList.get(i).getCardnumber());
            			sb_cardnums.append("'");
            		}else{
            			sb_cardnums.append("'");
            			sb_cardnums.append(cityHumanresourcesList.get(0).getCardnumber());
            			sb_cardnums.append("'");
            		}
            	}
            	IFilter repFilter =FilterFactory.getSimpleFilter("cardnumber in("+sb_cardnums.toString()+")");
		    	List<?> rep_list = this.getList(repFilter);
				if(rep_list!=null&&rep_list.size()>0){
					CityHumanresources cHumanresources = (CityHumanresources) rep_list.get(0);
					rcvmsg="存在相同数据！<br> 姓名:"+cHumanresources.getName()+" <br> 身份证:"+cHumanresources.getCardnumber();
					return rcvmsg;
				}
				
				//保存操作
            	for(CityHumanresources chs : cityHumanresourcesList){
            		preSaveObject(chs);
            		saveObject(chs);
            	}
            }
            
            
    	}
    	return rcvmsg;
	}
    
	
	
    
    //导出城市员工花名册的功能 
    @Override
	public File exportCityHumanExcel() throws Exception {
		String str_file_name = "export_cityhuman_template";
		String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
		//配置文件中的路径
		String str_filepath = strRootpath.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));
		File file_template = new File(str_filepath);

		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("update_time",ISort.ASC));
		
		
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
			sbfCondition.append(" citySelect in ("+cityssql+")");
		}else{
			sbfCondition.append(" 0=1 ");
		}
		
		IFilter iFilter = FilterFactory.getSimpleFilter(sbfCondition.toString());
		fsp.setUserFilter(iFilter);
		
		List<CityHumanresources> lst_cityhumanList = (List<CityHumanresources>)this.getList(fsp);

		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_newfilepath = str_file_dir_path + "cityhuman_list.xls";
		File file_new = new File(str_newfilepath);
		if(file_new.exists()){
			file_new.delete();
		}

		FileCopyUtils.copy(file_template, file_new);
		FileInputStream fis_input_excel = new FileInputStream(file_new);
		FileOutputStream fis_out_excel = null;
		Workbook wb_cityhumaninfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
		try{
			setCellStyle_common(wb_cityhumaninfo);

			Sheet csh_job = wb_cityhumaninfo.getSheetAt(0);//花名册
			Sheet csh_sb = wb_cityhumaninfo.getSheetAt(1);//社保
			Sheet csh_topost = wb_cityhumaninfo.getSheetAt(2);//入职
			Sheet csh_leave = wb_cityhumaninfo.getSheetAt(3);//离职
			Sheet csh_change = wb_cityhumaninfo.getSheetAt(4);//异动
			
			int csh_jobIndex = 2;
			int csh_sbIndex = 2;
			int csh_topostIndex = 2;
			int csh_leaveIndex = 2;
			int csh_changeIndex = 2;
			
			Map<String,CityHumanresources> map_cityhumanresources = new HashMap<String, CityHumanresources>();
			//所有在职的人员
			for (CityHumanresources cityHumanresources : lst_cityhumanList) {
				if(cityHumanresources.getHumanstatus()!=1L){
					continue;
				}
				Row obj_row = null;
				map_cityhumanresources.put(cityHumanresources.getEmployee_no(),cityHumanresources);
				int cellIndex = 0;
				
				//创建一行在sheet1
				csh_job.createRow(csh_jobIndex);
				obj_row = csh_job.getRow(csh_jobIndex);
				//setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getTotalsum()));//总数
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue((csh_jobIndex - 1)));//分序
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getEmployee_no()));//员工编号
				//公司	部门	板块	岗位	职级	姓名	性别	民族	籍贯	户籍	户口性质	出生年月	年龄	学历	学位	毕业院校	专业	职称	职业资格	婚姻状况	政治面貌	入党时间	参加工作时间	进中信时间	身份证号	到岗日	"劳动合同
				//签订主体"	合同类型	开始时间	终止时间	提醒	"签订
				//次数"	试用开始日	试用期截止日	联系方式	发薪主体	备注	情况说明	家庭通信地址	户籍所在地	试用期提醒
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getCitycompany()));//公司
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getDeptname()));//部门
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getDeptlevel1()));//板块 
				
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getZw()));//岗位 
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getProfessnallevel()));//职级
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getName()));//姓名
				
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getSex()));//性别
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getNation()));//民族
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getNativeplace()));//籍贯
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getCensusregister()));//户籍
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getCensusregistertype()));//户口性质
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getBirthday()));//出生年月  
				String val_age = cityHumanresources.getAge();
				if(val_age!=null&&val_age.contains("IF")){
					val_age="="+val_age;
				}
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(val_age));//年龄 
				
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getEducation()));//学历
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getEducationlevel()));//学位
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getSchool()));//毕业院校
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getProfession()));//专业
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getProfessiontitle()));//职称
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getCredentials()));//职业资格
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getMarriage()));//婚姻状况
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getPartisan()));//政治面貌
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getPartisandate()));//入党时间
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getWorkdate()));//参加工作时间
				
				
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getEntrydate()));//进中信时间
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getCardnumber()));//身份证号
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getTopostdate()));//到岗日
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getContractcompany()));//签订主体
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getContracttype()));//合同类型
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getContractdatestart()));//开始时间
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getContractdateend()));//终止时间
				
				//导出时 处理试用期提示信息
				Calendar contract_calendar = Calendar.getInstance();
				String contractdateend_str = cityHumanresources.getContractdateend();
				String val_tip = "";
				if(contractdateend_str!=null&&contractdateend_str.length()>7&&cityHumanresources.getContractdateend().length()<=10){
					Date contract_date = new SimpleDateFormat("yyyy-MM-dd").parse(cityHumanresources.getContractdateend().replace("/", "-"));
					int contract_days = differentDays(contract_calendar.getTime(),contract_date);
					
					if(contract_days<=40){
						val_tip="提醒";
					}else{
						val_tip="wei";
					}
				}
				
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(val_tip));//提醒
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getSigncount()));//次数
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getTrydatestart()));//试用开始日

				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getTrydateend()));//试用期截止日
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getPhone()));//联系方式
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getPaycompany()));//发薪主体
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getRemark()));//备注
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getOther()));//情况说明
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getHouseaddress()));//家庭通信地址
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getCardaddress()));//所在地
				
				//导出时 处理试用期提示信息
				Calendar try_calendar = Calendar.getInstance();
				String trydateend_str = cityHumanresources.getTrydateend();
				String val_trytip = "";
				if(trydateend_str!=null&&trydateend_str.length()>7&&trydateend_str.length()<=10){
					Date try_date = new SimpleDateFormat("yyyy-MM-dd").parse(cityHumanresources.getTrydateend().replace("/", "-"));
					int try_days = differentDays( try_calendar.getTime(),try_date);
					if(try_days>0){
						val_trytip="离转正还有"+try_days+"天";
					}else{
						val_trytip="试用期已过";
					}
				}
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(val_trytip));//试用期提醒
				
				csh_jobIndex++;
			}
			
			//第二个sheet页 社保  csh_sb
			for (CityHumanresources cityHumanresources : lst_cityhumanList) {
				if(cityHumanresources.getHumanstatus()!=1L){
					continue;
				}
				Row obj_row = null;
				int cellIndex = 0;
				
				//创建一行在sheet1
				csh_sb.createRow(csh_sbIndex);
				obj_row = csh_sb.getRow(csh_sbIndex);
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue((csh_sbIndex - 1)));//序号
				/**
				 * 序号	公司	部门	板块	岗位	
				 */
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getCitycompany()));//公司
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getDeptname()));//部门
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getDeptlevel1()));//板块 
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getZw()));//岗位 
				
				/**
				 * 银行卡号	身份证号	姓名	参加工作时间	入职时间	手机号	
				 */
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getBankcardnumber()));//银行卡号
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getCardnumber()));//身份证号
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getName()));//姓名
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getWorkdate()));//参加工作时间
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getTopostdate()));//到岗日
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getPhone()));//联系方式
				
				/**
				 * 试用期	试用期截止期	转正	劳动合同期限	劳动合同类型	目前签订次数	劳动合同主体	发薪主体	
				 */
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getTrydatestart()));//试用开始日
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getTrydateend()));//试用期截止日
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(""));//转正
				String contract_start_end = cityHumanresources.getContractdatestart()+"--"+cityHumanresources.getContractdateend();
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(contract_start_end));//合同
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getContracttype()));//合同类型
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getSigncount()));//次数
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getContractcompany()));//签订主体
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getPaycompany()));//发薪主体
				
				/**
				 * 户口性质	备注	子女情况	子女姓名	子/女	    出生年月	身份证号	开户行
				 */
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getCensusregistertype()));//户口性质
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getSecremark()));//备注
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getChildren()));//子女情况
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getChildrenname()));//子女性名
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getChildrensex()));//子/女
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getChildrenbirthday()));//出生年月
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getChildrencardnumber()));//身份证号
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getBankname()));//开户行
				
				csh_sbIndex++;
			}
			
			
			//第三个sheet页 入职情况  csh_topostIndex
			for (CityHumanresources cityHumanresources : lst_cityhumanList) {
				Row obj_row = null;
				int cellIndex = 0;
				if(cityHumanresources.getHumanstatus()==1L){
					//创建一行在sheet1
					csh_topost.createRow(csh_topostIndex);
					obj_row = csh_topost.getRow(csh_topostIndex);
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue((csh_topostIndex - 1)));//序号
					//序号	姓名	入职部门	板块	岗位	入职时间	劳动合同签订主体	是否领取劳动合同
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getName()));//姓名
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getDeptname()));//部门
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getDeptlevel1()));//板块 
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getZw()));//岗位 
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getTopostdate()));//入职时间
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getContractcompany()));//劳动合同签订主体
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(""));//是否领取劳动合同
					csh_topostIndex ++;
				}
			}
			
			
			//第四个sheet页 离职情况  
			for (CityHumanresources cityHumanresources : lst_cityhumanList) {
				Row obj_row = null;
				int cellIndex = 0;
				if(cityHumanresources.getHumanstatus()==2L){
					csh_leave.createRow(csh_leaveIndex);
					obj_row = csh_leave.getRow(csh_leaveIndex);
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue((csh_leaveIndex - 1)));//序号
					//部门	板块	职位	姓名	性别	
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getDeptname()));//部门
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getDeptlevel1()));//板块 
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getZw()));//岗位 
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getName()));//姓名
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getSex()));//性别
					
					//民族	籍贯	户籍	出生年月	年龄	学历	学位	毕业学校	所学专业	
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getNation()));//民族
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getNativeplace()));//籍贯
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getCensusregister()));//户籍
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getBirthday()));//出生年月  
					String val_age = cityHumanresources.getAge();
					if(val_age!=null&&val_age.contains("IF")){
						val_age="="+val_age;
					}
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(val_age));//年龄 
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getEducation()));//学历
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getEducationlevel()));//学位
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getSchool()));//毕业院校
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getProfession()));//专业
					
					//职称	职称资格	政治面貌	入党时间	参加工作时间	进中信时间	身份证号	到岗日	离职时间	联系方式	离职原因
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getProfessiontitle()));//职称
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getCredentials()));//职业资格
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getPartisan()));//政治面貌
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getPartisandate()));//入党时间
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getWorkdate()));//参加工作时间
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getEntrydate()));//进中信时间
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getCardnumber()));//身份证号
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getTopostdate()));//到岗日
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getLeavedate()));//离职时间
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getPhone()));//联系方式
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityHumanresources.getLeavereason()));//离职原因
					csh_leaveIndex ++;
					
				}
			}
			
			//第五个sheet页 异动情况 调配
			String citySelect_condition = "";
			if(cityssql!=""&&cityssql.length()>0){
				citySelect_condition = " citySelect in ("+cityssql+")";
			}else{
				citySelect_condition = " 0=1 ";
			}
			
			CityHumanresourcesChangeManager cityHumanresourcesChangeManager = (CityHumanresourcesChangeManager) SpringHelper.getBean("cityHumanresourcesChangeManager");
			IFilter cityFilter =FilterFactory.getSimpleFilter(citySelect_condition);
			List<CityHumanresourcesChange> cHumanresourcesChanges = (List<CityHumanresourcesChange>) cityHumanresourcesChangeManager.getList(cityFilter);
			for (CityHumanresourcesChange chChange : cHumanresourcesChanges) {
				Row obj_row = null;
				int cellIndex = 0;
				//创建一行在sheet1
				csh_change.createRow(csh_changeIndex);
				obj_row = csh_change.getRow(csh_changeIndex);
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue((csh_changeIndex - 1)));//序号
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getName()));//姓名
				//序号	姓名	入职部门	板块	岗位	入职时间	劳动合同签订主体	是否领取劳动合同
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(""));//类别
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getCitycompany()));//原公司
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getDeptname()));//原部门
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getDeptlevel1()));//原版块
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getZw()));//原岗位
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getProfessnallevel()));//原职级
				
				
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getChangecitycompany()));//调配后公司 
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getChangedeptname()));//调配后部门
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getChangedeptlevel1()));//调配后版块
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getChangezw()));//调配后岗位
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getChangeprofessnallevel()));//调配后职级
				
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getChangedate()));//调配时间
				
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getContractcompany()));//原劳动合同主体
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(chChange.getChangecontractcompany()));//调配后劳动合同主体 
				
				csh_changeIndex ++;
			}
			
			fis_out_excel = new FileOutputStream(file_new);
			wb_cityhumaninfo.write(fis_out_excel);
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
	
	
    
    
    @Override
    public CityHumanresources queryCityHumanresourceById(Long id){
    	CityHumanresources cityHumanresources = (CityHumanresources) this.getObject(id);
    	return cityHumanresources;
    }
    
    /*
     * 新增增加城市人员的方法
     */
    @Override
    public CityHumanresources saveCityHumanresources(CityHumanresources cityHumanresources ){
    	 //自动生成员工号
    	 UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
    	 StoreKeeperManager storeKeeperManager = (StoreKeeperManager) SpringHelper.getBean("storeKeeperManager");
    	 StoreKeeperDao storeKeeperDao = (StoreKeeperDao) SpringHelper.getBean(StoreKeeperDao.class.getName());
    	 //取得店长的最大员工号
         String storekeeper_maxemployee_no = storeKeeperDao.queryMaxNo("SK");
         String storekeeper_maxemployee_no_rm = storeKeeperDao.queryMaxNo("RM");
         
         List<DistCity> distCities = userManager.getCurrentUserCity();
         String citySelect = "";
         String citySelectCode = "";
         if(distCities!=null&&distCities.size()>0){
         	DistCity dCity = distCities.get(0);
         	citySelect = dCity.getCityname();
         	citySelectCode = dCity.getCitycode();
         }
         String deptname = cityHumanresources.getDeptname();
         String zw = cityHumanresources.getZw();
         String name = cityHumanresources.getName();
         String phone = cityHumanresources.getPhone();
         
         if(citySelectCode!=null&&citySelectCode.length()>0&&deptname!=null&&zw!=null){
        	 
        	 if(deptname!=null&&deptname.trim().equals("门店管理中心")&&zw.contains("店长")){
        		//根据姓名和电话查询 是否存在店长，如果存在 取出员工号，如果不存在则生成sk的员工号并保存店长表中并返回 。
                 StoreKeeper storeKeeper = storeKeeperManager.queryStoreKeeperByNamePhone(name, phone);
                 String new_employee_no = "";
                 if(storeKeeper!=null&&storeKeeper.getEmployee_no()!=null){
                     new_employee_no = storeKeeper.getEmployee_no();
                 }else{
                	 //生成员工号
                     String num = storekeeper_maxemployee_no.substring(storekeeper_maxemployee_no.length()-5,storekeeper_maxemployee_no.length());
                     int new_num = Integer.parseInt(num)+1;
                     String new_employee_no_num = ("00000"+new_num);
                     new_employee_no = "SK" + new_employee_no_num.substring(new_employee_no_num.length()-5, new_employee_no_num.length());
                     
                     //保存到storekeeper 表中 
                     StoreKeeper save_storeKeeper = new StoreKeeper();
                     save_storeKeeper.setCitySelect(citySelect);
                     save_storeKeeper.setEmployee_no(new_employee_no);
                     save_storeKeeper.setHumanstatus(cityHumanresources.getHumanstatus());
                     save_storeKeeper.setName(name);
                     save_storeKeeper.setPhone(phone);
                     save_storeKeeper.setZw("店长");
                     preSaveObject(save_storeKeeper);
                     storeKeeperManager.saveObject(save_storeKeeper);
                 }
                 cityHumanresources.setEmployee_no(new_employee_no);
                 cityHumanresources.setCitySelect(citySelect);
                 
        	 }else if(deptname!=null&&deptname.trim().equals("门店管理中心")&&zw.contains("经理")&&zw.contains("运营")){
        		//根据姓名和电话查询 是否存在运营经理，如果存在 取出员工号，如果不存在则生成rm的员工号并保存店长表中并返回 。
                 StoreKeeper storeKeeper = storeKeeperManager.queryStoreKeeperByNamePhone(name, phone);
                 String new_employee_no = "";
                 if(storeKeeper!=null&&storeKeeper.getEmployee_no()!=null){
                     new_employee_no = storeKeeper.getEmployee_no();
                 }else{
                	 //生成员工号
                     String num = storekeeper_maxemployee_no_rm.substring(storekeeper_maxemployee_no_rm.length()-5,storekeeper_maxemployee_no_rm.length());
                     int new_num = Integer.parseInt(num)+1;
                     String new_employee_no_num = ("00000"+new_num);
                     new_employee_no = "RM" + new_employee_no_num.substring(new_employee_no_num.length()-5, new_employee_no_num.length());
                     
                     //保存到storekeeper 表中 
                     StoreKeeper save_storeKeeper = new StoreKeeper();
                     save_storeKeeper.setCitySelect(citySelect);
                     save_storeKeeper.setEmployee_no(new_employee_no);
                     save_storeKeeper.setHumanstatus(cityHumanresources.getHumanstatus());
                     save_storeKeeper.setName(name);
                     save_storeKeeper.setPhone(phone);
                     save_storeKeeper.setZw("运营经理");
                     preSaveObject(save_storeKeeper);
                     storeKeeperManager.saveObject(save_storeKeeper);
                 }
                 cityHumanresources.setEmployee_no(new_employee_no);
                 cityHumanresources.setCitySelect(citySelect);
        	 }else{
        		 //查询当前城市最大的员工号
                 String employee_no_type="CS"+citySelectCode;  //固定城市人员前缀+城市的code
                 CityHumanresourcesDao cityHumanresourcesDao = (CityHumanresourcesDao) SpringHelper.getBean(CityHumanresourcesDao.class.getName());
                 String maxNo = cityHumanresourcesDao.queryMaxEmpNo(employee_no_type);
                 //生成员工号
                 String num = maxNo.substring(maxNo.length()-5,maxNo.length());
                 int new_num = Integer.parseInt(num)+1;
                 String new_employee_no_num = ("00000"+new_num);
                 String new_employee_no = employee_no_type + new_employee_no_num.substring(new_employee_no_num.length()-5, new_employee_no_num.length());
                 cityHumanresources.setEmployee_no(new_employee_no);
                 cityHumanresources.setCitySelect(citySelect);
        	 }
        	 cityHumanresources.setId(null);
        	 
        	 //计算年龄,生日,性别
        	 String cardnumber = cityHumanresources.getCardnumber();
        	 if(cityHumanresources.getAge()==null&&cardnumber!=null&&cardnumber.length()==18){
        		//处理生日
                 String year = cardnumber.substring(6,10);
          		 String month = cardnumber.substring(10,12);
          		 String days = cardnumber.substring(12,14);
          		 String birthday_calc = year+"-"+month+"-"+days;
                 //处理性别
          		 String sex = "";
          		 String sexnum = cardnumber.substring(cardnumber.length()-2,cardnumber.length()-1);
          		 if("13579".contains(sexnum)){
        			sex="男";
        		 }else{
        			sex="女";
        		 }
          		 
          		 Calendar a=Calendar.getInstance();
          		 int now_year = a.get(Calendar.YEAR);
          		 int birth_year = Integer.parseInt(year);
          		 String age = (now_year-birth_year)+"";
          		 
          		 cityHumanresources.setAge(age);
          		 cityHumanresources.setBirthday(birthday_calc);
          		 cityHumanresources.setSex(sex);
        	 }
        	 
        	 preSaveObject(cityHumanresources);
             saveObject(cityHumanresources);
             return cityHumanresources;
         }
        
    	return null;
    }
    
    
    @Override
    public CityHumanresources validateCardNumber(String cardnumber){
    	//判断身份号是否重复
    	IFilter repFilter =FilterFactory.getSimpleFilter("cardnumber",cardnumber);
    	List<?> rep_list = this.getList(repFilter);
    	CityHumanresources cHumanresources = null;
		if(rep_list!=null&&rep_list.size()>0){
			cHumanresources = (CityHumanresources) rep_list.get(0);
			return cHumanresources;
		}
		return cHumanresources;
    }
    @Override
    public CityHumanresources validatePhone(String phone){
    	//判断身份号是否重复
    	IFilter repFilter =FilterFactory.getSimpleFilter("phone",phone);
    	List<?> rep_list = this.getList(repFilter);
    	CityHumanresources cHumanresources = null;
		if(rep_list!=null&&rep_list.size()>0){
			cHumanresources = (CityHumanresources) rep_list.get(0);
			return cHumanresources;
		}
		return cHumanresources;
    }
    
    @Override
    public CityHumanresources queryCityHumanresourcesByEmployeeNo(String employee_no){
    	//判断身份号是否重复
    	IFilter repFilter =FilterFactory.getSimpleFilter("employee_no",employee_no);
    	List<?> rep_list = this.getList(repFilter);
    	CityHumanresources cHumanresources = null;
		if(rep_list!=null&&rep_list.size()>0){
			cHumanresources = (CityHumanresources) rep_list.get(0);
			return cHumanresources;
		}
		return cHumanresources;
    }
    
    
    @Override
    public CityHumanresources updateCityHumanresources(CityHumanresources cityHumanresources){
    	CityHumanresources update_CityHumanresources = (CityHumanresources) this.getObject(cityHumanresources.getId());
    	if(update_CityHumanresources.getHumanstatus().equals(Long.parseLong("2"))){
    		update_CityHumanresources.setLeavereason(cityHumanresources.getLeavereason());//离职原因
            update_CityHumanresources.setLeavedate(cityHumanresources.getLeavedate());//离职日期 
            
            preSaveObject(update_CityHumanresources);
        	saveObject(update_CityHumanresources);
        	
        	
        	StoreKeeperManager storeKeeperManager = (StoreKeeperManager) SpringHelper.getBean("storeKeeperManager");
    		StoreKeeper storeKeeper = storeKeeperManager.findStoreKeeperByEmployeeId(update_CityHumanresources.getEmployee_no());
    		if(storeKeeper!=null){
        		storeKeeper.setLeavereason(update_CityHumanresources.getLeavereason());
        		storeKeeper.setLeavedate(update_CityHumanresources.getLeavedate());
        		preSaveObject(storeKeeper);
        		storeKeeperManager.saveObject(storeKeeper);
    		}
    		
    		return update_CityHumanresources;
    	}
    	
    	
    	boolean isChangeFlag = false;//默认无变化
    	//原公司	原部门	原板块	原岗位	原职级
    	String o_citycompany = update_CityHumanresources.getCitycompany()==null?"":update_CityHumanresources.getCitycompany().trim();//原公司
    	String o_deptname = update_CityHumanresources.getDeptname()==null?"":update_CityHumanresources.getDeptname().trim();//原部门
    	String o_deptlevel1 = update_CityHumanresources.getDeptlevel1()==null?"":update_CityHumanresources.getDeptlevel1().trim();//原板块
    	String o_zw = update_CityHumanresources.getZw()==null?"":update_CityHumanresources.getZw().trim();//原岗位
    	String o_professnallevel = update_CityHumanresources.getProfessnallevel()==null?"":update_CityHumanresources.getProfessnallevel().trim();//原职级
    	String o_contractcompany = update_CityHumanresources.getContractcompany()==null?"":update_CityHumanresources.getContractcompany().trim();//原合同主体 
    	
    	String n_citycompany = cityHumanresources.getCitycompany()==null?"":cityHumanresources.getCitycompany().trim();//原公司
    	String n_deptname = cityHumanresources.getDeptname()==null?"":cityHumanresources.getDeptname().trim();//原部门
    	String n_deptlevel1 = cityHumanresources.getDeptlevel1()==null?"":cityHumanresources.getDeptlevel1().trim();//原板块
    	String n_zw = cityHumanresources.getZw()==null?"":cityHumanresources.getZw().trim();//原岗位
    	String n_professnallevel = cityHumanresources.getProfessnallevel()==null?"":cityHumanresources.getProfessnallevel().trim();//原职级
    	String n_contractcompany = cityHumanresources.getContractcompany()==null?"":cityHumanresources.getContractcompany().trim();//原合同主体 
    	
    	if(o_citycompany.equals(n_citycompany)&&o_deptname.equals(n_deptname)&&o_deptlevel1.equals(n_deptlevel1)&&o_zw.equals(n_zw)&&o_professnallevel.equals(n_professnallevel)&&o_contractcompany.equals(n_contractcompany)){
    		isChangeFlag = false;
    	}else{
    		isChangeFlag = true;
    	}
    	
    	if(isChangeFlag){
    		//保存 调配表中数据 
    		CityHumanresourcesChange cityHumanChange = new CityHumanresourcesChange();
    		
    		cityHumanChange.setName(update_CityHumanresources.getName());
    		
    		cityHumanChange.setCitycompany(o_citycompany);
    		cityHumanChange.setDeptname(o_deptname);
    		cityHumanChange.setDeptlevel1(o_deptlevel1);
    		cityHumanChange.setZw(o_zw);
    		cityHumanChange.setProfessnallevel(o_professnallevel);
    		cityHumanChange.setContractcompany(o_contractcompany);
    		
    		cityHumanChange.setChangecitycompany(n_citycompany);
    		cityHumanChange.setChangedeptname(n_deptname);
    		cityHumanChange.setChangedeptlevel1(n_deptlevel1);
    		cityHumanChange.setChangezw(n_zw);
    		cityHumanChange.setChangeprofessnallevel(n_professnallevel);
    		cityHumanChange.setChangecontractcompany(n_contractcompany);
    		
    		//变更时间
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    		String changedate = sdf.format(new Date());
    		
    		cityHumanChange.setChangedate(changedate);
    		
    		cityHumanChange.setCitySelect(update_CityHumanresources.getCitySelect());
    		
    		preSaveObject(cityHumanChange);
    		saveObject(cityHumanChange);
    		
    	}
    	
    	update_CityHumanresources.setCitycompany(cityHumanresources.getCitycompany());//公司
    	update_CityHumanresources.setDeptname(cityHumanresources.getDeptname());//部门
    	update_CityHumanresources.setDeptlevel1(cityHumanresources.getDeptlevel1());//板块 
        
    	update_CityHumanresources.setZw(cityHumanresources.getZw());//岗位
    	update_CityHumanresources.setProfessnallevel(cityHumanresources.getProfessnallevel());//职级
    	update_CityHumanresources.setName(cityHumanresources.getName());//姓名
    	update_CityHumanresources.setSex(cityHumanresources.getSex());//性别
        update_CityHumanresources.setNation(cityHumanresources.getNation());//民族
        update_CityHumanresources.setNativeplace(cityHumanresources.getNativeplace());//籍贯
        update_CityHumanresources.setCensusregister(cityHumanresources.getCensusregister());//户籍
        update_CityHumanresources.setCensusregistertype(cityHumanresources.getCensusregistertype());//户口性质
        update_CityHumanresources.setBirthday(cityHumanresources.getBirthday());//出生年月 
        update_CityHumanresources.setAge(cityHumanresources.getAge());//年龄
        
        
        update_CityHumanresources.setEducation(cityHumanresources.getEducation());//学历
        update_CityHumanresources.setEducationlevel(cityHumanresources.getEducationlevel());//学位
        update_CityHumanresources.setSchool(cityHumanresources.getSchool());//毕业院校
        update_CityHumanresources.setProfession(cityHumanresources.getProfession());//专业
        update_CityHumanresources.setProfessiontitle(cityHumanresources.getProfessiontitle());//职称
        update_CityHumanresources.setCredentials(cityHumanresources.getCredentials());//职业资格
        update_CityHumanresources.setMarriage(cityHumanresources.getMarriage());//婚姻状况
        update_CityHumanresources.setPartisan(cityHumanresources.getPartisan());//政治面貌
        update_CityHumanresources.setPartisandate(cityHumanresources.getPartisandate());//入党时间
        update_CityHumanresources.setWorkdate(cityHumanresources.getPartisandate());//参加工作时间
        
        
        update_CityHumanresources.setEntrydate(cityHumanresources.getEntrydate());//进中信时间
        update_CityHumanresources.setCardnumber(cityHumanresources.getCardnumber());//身份证号
        update_CityHumanresources.setTopostdate(cityHumanresources.getTopostdate());//到岗日
        update_CityHumanresources.setContractcompany(cityHumanresources.getContractcompany());//签订主体
        update_CityHumanresources.setContracttype(cityHumanresources.getContracttype());//合同类型
        update_CityHumanresources.setContractdatestart(cityHumanresources.getContractdatestart());//开始时间
        update_CityHumanresources.setContractdateend(cityHumanresources.getContractdateend());//终止时间
        //update_CityHumanresources.setTipmessage(cityHumanresources.getTipmessage());//提醒
        update_CityHumanresources.setSigncount(cityHumanresources.getSigncount());//次数
        update_CityHumanresources.setTrydatestart(cityHumanresources.getTrydatestart());//试用开始日
        
        
        update_CityHumanresources.setTrydateend(cityHumanresources.getTrydateend());//试用期截止日
        update_CityHumanresources.setPhone(cityHumanresources.getPhone());//联系方式
        update_CityHumanresources.setPaycompany(cityHumanresources.getPaycompany());//发薪主体
        update_CityHumanresources.setRemark(cityHumanresources.getRemark());//备注
        update_CityHumanresources.setOther(cityHumanresources.getOther());//情况说明
        update_CityHumanresources.setHouseaddress(cityHumanresources.getHouseaddress());//家庭通信地址
        update_CityHumanresources.setCardaddress(cityHumanresources.getCardaddress());//所在地
        //update_CityHumanresources.setTrytipmessage(cityHumanresources.getTrytipmessage());//试用期提醒
        
        //update_CityHumanresources.setTotalsum(cityHumanresources.getTotalsum());//总数 
    	
        update_CityHumanresources.setId(cityHumanresources.getId());
        
        update_CityHumanresources.setHumanstatus(cityHumanresources.getHumanstatus());//状态
        boolean isLeave = false;
        if(cityHumanresources.getHumanstatus()==2L){
        	isLeave = true;
        	update_CityHumanresources.setLeavereason(cityHumanresources.getLeavereason());//离职原因
            update_CityHumanresources.setLeavedate(cityHumanresources.getLeavedate());//离职日期 
        }else{
        	update_CityHumanresources.setLeavereason("");//离职原因
            update_CityHumanresources.setLeavedate("");//离职日期 
        }
        
        //社保信息
        update_CityHumanresources.setChildren(cityHumanresources.getChildren());//子女情况
        update_CityHumanresources.setChildrenname(cityHumanresources.getChildrenname());//子女姓名
        update_CityHumanresources.setChildrensex(cityHumanresources.getChildrensex());//子女性别
        update_CityHumanresources.setChildrenbirthday(cityHumanresources.getChildrenbirthday());//出生年月
        update_CityHumanresources.setChildrencardnumber(cityHumanresources.getChildrencardnumber());//身份证号
        update_CityHumanresources.setBankcardnumber(cityHumanresources.getBankcardnumber());//银行卡号
        update_CityHumanresources.setBankname(cityHumanresources.getBankname());//开户行 
        update_CityHumanresources.setSecremark(cityHumanresources.getSecremark());//社保、工资备注
        
    	preSaveObject(update_CityHumanresources);
    	saveObject(update_CityHumanresources);
    	
    	if(isLeave){
    		//如果离职、同时也修改storekeeper，tb_bizbase_user tb为离职
    		StoreKeeperManager storeKeeperManager = (StoreKeeperManager) SpringHelper.getBean("storeKeeperManager");
    		StoreKeeper storeKeeper = storeKeeperManager.findStoreKeeperByEmployeeId(update_CityHumanresources.getEmployee_no());
    		if(storeKeeper!=null){
    			storeKeeper.setHumanstatus(Long.parseLong("2"));
        		storeKeeper.setLeavereason(update_CityHumanresources.getLeavereason());
        		storeKeeper.setLeavedate(update_CityHumanresources.getLeavedate());
        		preSaveObject(storeKeeper);
        		storeKeeperManager.saveObject(storeKeeper);
    		}
    		
    		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
    		User user = userManager.findEmployeeByEmployeeNo(update_CityHumanresources.getEmployee_no());
    		if(user!=null){
    			user.setDisabledFlag(0);
    			preSaveObject(user);
    			userManager.saveObject(user);
    		}
    	}
    	return update_CityHumanresources;
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
    
    
    //处理电话 联系人 身份证 字符串
    private String getStringCellValue(Cell cell) {
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
            	value = new BigDecimal(value).toPlainString();
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
	
    
	
	 private String getMapValue(Row row_human,Map<String, Integer> maps,String key){
	    	if(maps.get(key)!=null){
	    		String ret = getCellValue(row_human.getCell(maps.get(key)));
	    		return ret;
	    	}
	    	return "";
	    }
	    
	    
	    //处理电话 联系人 身份证 字符串
	    private String getStringMapValue(Row row_human,Map<String, Integer> maps,String key){
	    	if(maps.get(key)!=null){
	    		String ret = getStringCellValue(row_human.getCell(maps.get(key)));
	    		return ret;
	    	}
	    	return "";
	    }
	    
	    
	    private String isHave(String[] titiles,String str_value){
	    	for(String titleString : titiles){
	    		if("试用期提醒".equals(str_value)){
	    			return str_value;
	    		}else if("提醒".equals(str_value)){
	    			return str_value;
	    		}else if("户籍所在地".equals(str_value)){
	    			return str_value;
	    		}else if("户籍".equals(str_value)){
	    			return str_value;
	    		}else{
		    		if(str_value!=null){
		    			if(str_value.contains(titleString)){
			    			return titleString;
			    		}
		    		}
	    		}
	    	}
	    	return null;
	    }
	    
	    
	    
	    
	    private String getNewEmployeeNo(String maxEmployeeNo){
			if(maxEmployeeNo!=null&&maxEmployeeNo.length()>0){
				String tmpEmpNo = maxEmployeeNo.substring(maxEmployeeNo.length()-5,maxEmployeeNo.length());
				String tmpMaxNo = (Integer.parseInt(tmpEmpNo)+1)+"";
				//补零
				for(int i=0;i<6;i++){
					if(tmpMaxNo.length()<5){
						tmpMaxNo="0"+tmpMaxNo;
	    			}else{
	    				break;
	    			}
				}
				return tmpMaxNo;
			}else{
				return "00001";
			}
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
	    public void setCellValue(Row obj_row,int nCellIndex,Object value){
	        Cell cell=obj_row.createCell(nCellIndex);
	        cell.setCellStyle(getCellStyle_common());
	        cell.setCellValue(new HSSFRichTextString(value==null?null:value.toString()));
	    }
	    public CellStyle getCellStyle_common(){
	        return cellStyle_common;
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
	    
	    public int differentDays(Date date1,Date date2)
	    {
	        Calendar cal1 = Calendar.getInstance();
	        cal1.setTime(date1);
	        
	        Calendar cal2 = Calendar.getInstance();
	        cal2.setTime(date2);
	       int day1= cal1.get(Calendar.DAY_OF_YEAR);
	        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
	        
	        int year1 = cal1.get(Calendar.YEAR);
	        int year2 = cal2.get(Calendar.YEAR);
	        if(year1 != year2)   //同一年
	        {
	            int timeDistance = 0 ;
	            for(int i = year1 ; i < year2 ; i ++)
	            {
	                if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
	                {
	                    timeDistance += 366;
	                }
	                else    //不是闰年
	                {
	                    timeDistance += 365;
	                }
	            }
	            
	            return timeDistance + (day2-day1) ;
	        }
	        else    //不同年
	        {
	            return day2-day1;
	        }
	    }
	    
	    
	    
	    
}
