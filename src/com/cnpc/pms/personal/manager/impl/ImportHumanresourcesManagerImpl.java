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

import javax.swing.Spring;

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
import com.cnpc.pms.personal.dao.ImportHumanresourcesDao;
import com.cnpc.pms.personal.dao.YyMicrDataDao;
import com.cnpc.pms.personal.entity.ImportHumanresources;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.XxExpress;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
import com.cnpc.pms.personal.manager.ImportHumanresourcesManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.utils.DateUtils;

public class ImportHumanresourcesManagerImpl extends BizBaseCommonManager implements ImportHumanresourcesManager {

	
	/**
	 * 导入员工总表 
	 */
    @Override
    public String importHumanresourceForExcel(List<File> lst_import_excel) throws Exception {
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
            
//            //查询数据库最大值
//            ImportHumanresourcesDao h = (ImportHumanresourcesDao)SpringHelper.getBean(ImportHumanresourcesDao.class.getName());
//            Map<String, Double> maxmap = h.queryMaxImportDept();
//            
            
            String rcvmsg = "";
            //临时保存国安侠姓名，ID
            Map<String, Object> employMap = new HashMap<String, Object>();
            String[] titles ={"正式","分序","部门","岗位","姓名","性别","民族","籍贯","户籍","出生","学历","学位","学校","专业","职称","资格","婚姻状况","党派","入党时间","参加工作","进中信","身份证号","到岗日","劳动合同期限","试用期","签订次数","是否转正","补充医保","联系方式","紧急联络人姓名","电话","与本人关系","备","是否签订劳动合同","身份证地址"};
            
            Map<String, Integer> maps = new HashMap<String, Integer>();
            String deptName = "";
            //for(int i = 0; i < sheetCount;i++){
                //sheet_data = wb_excel.getSheetAt(0);
                sheet_data = wb_excel.getSheet("国安社区");
                if(sheet_data==null){
                	rcvmsg="文件格式错误！ 导入失败！";
    				return rcvmsg;
                }
                int ret = sheet_data.getPhysicalNumberOfRows();
                for(int nRowIndex = 0;nRowIndex < sheet_data.getPhysicalNumberOfRows();nRowIndex++){
                    Row row_human = sheet_data.getRow(nRowIndex);
                    if(row_human == null){
                        continue;
                    }
                    
                    String isStartStr = getCellValue(row_human.getCell(0));
/*//                    if(isStartStr!=null&&!isStartStr.equals("总序")){
//                    	continue;
//                    }
*/                    //开始读取列
                    //如果电话为空 不处理 
//                    String rcv_tel = getCellValue(row_express.getCell(4));
//                    if(rcv_tel == null || "".equals(rcv_tel)){
//                        continue;
//                    }
                    
                    if(maps.size() < 3){
                    	int nCellSize = row_human.getPhysicalNumberOfCells();
                    	for(int nCellIndex = 0;nCellIndex < nCellSize ;nCellIndex ++){
                    		String str_value = getCellValue(row_human.getCell(nCellIndex));
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
                    
                    ImportHumanresources obj_humanresources = new ImportHumanresources();
                    
                    //"正式"
                    String employee_no = getMapValue(row_human, maps, "正式");
                    if(employee_no==null||employee_no.equals("")){
                    	rcvmsg="员工编号不存在！ 导入失败！";
        				return rcvmsg;
                    }
                    obj_humanresources.setEmployee_no(employee_no);
                    
                    
                    
                    //"分序"
                    String deptno = getMapValue(row_human, maps, "分序");
                    if(deptno!=null&&deptno.length()>0){
                    	obj_humanresources.setDeptno(Double.parseDouble(getMapValue(row_human, maps, "分序")));
                    }
                    
                    //"部门"
                    String dept = getMapValue(row_human, maps, "部门");
                    if(dept!=null&&dept.length()>0){
                    	deptName = dept;
                    	
                    }
                    obj_humanresources.setDeptname(deptName);
                    //"岗位"
                    obj_humanresources.setZw(getMapValue(row_human, maps, "岗位"));
                    //"姓名"
                    String name = getMapValue(row_human, maps, "姓名");
                    obj_humanresources.setName(name);
                    //"性别"
                    obj_humanresources.setSex(getMapValue(row_human, maps, "性别"));
                    //"民族"
                    obj_humanresources.setNation(getMapValue(row_human, maps, "民族"));
                    //"籍贯"
                    obj_humanresources.setNativeplace(getMapValue(row_human, maps, "籍贯"));
                    //"户籍"
                    obj_humanresources.setCensusregister(getMapValue(row_human, maps, "户籍"));
                    //"出生"
                    obj_humanresources.setBirthday(getMapValue(row_human, maps, "出生"));
                    //"学历"
                    obj_humanresources.setEducation(getMapValue(row_human, maps, "学历"));
                    //"学位"
                    obj_humanresources.setEducationlevel(getMapValue(row_human, maps, "学位"));
                    
                    //"学校"
                    obj_humanresources.setSchool(getMapValue(row_human, maps, "学校"));
                    //"专业"
                    obj_humanresources.setProfession(getMapValue(row_human, maps, "专业"));
                    //"职称"
                    obj_humanresources.setProfessiontitle(getMapValue(row_human, maps, "职称"));
                    //"资格"
                    obj_humanresources.setCredentials(getMapValue(row_human, maps, "资格"));
                    //"婚姻状况"
                    obj_humanresources.setMarriage(getMapValue(row_human, maps, "婚姻状况"));
                    //"党派"
                    obj_humanresources.setPartisan(getMapValue(row_human, maps, "党派"));
                    //"党派"
                    obj_humanresources.setPartisandate(getMapValue(row_human, maps, "入党时间"));
                    //"参加工作"
                    obj_humanresources.setWorkdate(getMapValue(row_human, maps, "参加工作"));
                    //"进中信"
                    obj_humanresources.setEntrydate(getMapValue(row_human, maps, "进中信"));
                    //"身份证号"
                    String cardnumber=getMapValue(row_human, maps, "身份证号");
                    obj_humanresources.setCardnumber(cardnumber);
                    
                    //"到岗日"
                    obj_humanresources.setTopostdate(getMapValue(row_human, maps, "到岗日"));
                    //"劳动合同期限"
                    obj_humanresources.setContractdate(getMapValue(row_human, maps, "劳动合同期限"));
                    //"试用期"
                    obj_humanresources.setTrydate(getMapValue(row_human, maps, "试用期"));
                    //"签订次数"
                    obj_humanresources.setSigncount(getMapValue(row_human, maps, "签订次数"));
                    //"是否转正"
                    obj_humanresources.setIsofficial(getMapValue(row_human, maps, "是否转正"));
                    //"补充医保"
                    obj_humanresources.setTomedical(getMapValue(row_human, maps, "补充医保"));
                    //"联系方式"
                    obj_humanresources.setPhone(getMapValue(row_human, maps, "联系方式"));
                    //"紧急联络人"
                    obj_humanresources.setRelationname(getMapValue(row_human, maps, "紧急联络人姓名"));
                    //"电话"
                    obj_humanresources.setTel(getMapValue(row_human, maps, "电话"));
                    //"与本人关系"
                    obj_humanresources.setRelationtype(getMapValue(row_human, maps, "与本人关系"));
                    //"备"
                    obj_humanresources.setRemark(getMapValue(row_human, maps, "备"));
                    //"是否签订劳动合同"
                    obj_humanresources.setIssigncontract(getMapValue(row_human, maps, "是否签订劳动合同"));
                    //"身份证地址"
                    obj_humanresources.setCardaddress(getMapValue(row_human, maps, "身份证地址"));
                    //System.out.println("nRowIndex--------------->"+nRowIndex);
//                    if(maxmap.containsKey(obj_humanresources.getDeptname())&&maxmap.get(obj_humanresources.getDeptname())>=obj_humanresources.getDeptno()){
//                    	continue;
//                    }
                    
                    //obj_express.setStore_id(store_id);
                   /* int isExist = queryImportHumanByCardNumber(cardnumber, name);
                    if(isExist!=0){
                    	continue;
                    }*/
                    
                    preObject(obj_humanresources);
                    obj_humanresources.setImportstatus(0);
                    obj_humanresources.setStatus(0);
                    obj_humanresources.setVersion(new Long(0));
                    saveObject(obj_humanresources);
                }
            //}
            is_excel.close();
        }
        
        //查询所有状态为0的信息 保存到humanresource表中
        ImportHumanresourcesManager importManager = (ImportHumanresourcesManager) SpringHelper.getBean("importHumanresourcesManager");
        HumanresourcesManager humanManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");

        FSP fsp = new FSP();
        //IFilter iFilter =FilterFactory.getSimpleFilter(" importstatus=0 and deptname ='社区门店' ");
        IFilter iFilter =FilterFactory.getSimpleFilter(" importstatus=0 ");
        fsp.setUserFilter(iFilter);
        List<?> lst_humanList = importManager.getList(fsp);
        String ids = "";
        for(Object obj_human:lst_humanList){
        	ImportHumanresources importHumanresources = (ImportHumanresources) obj_human;
        	ids+=importHumanresources.getId()+",";
        }
        return humanManager.saveHumanresource(ids);
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
    		if("紧急联络人姓名".equals(str_value)){
    			return str_value;
    		}else if("姓名".equals(str_value)){
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
    
    public int queryImportHumanByCardNumber(String cardnumber,String name){
		ImportHumanresourcesDao importHumanresourcesDao = (ImportHumanresourcesDao) SpringHelper.getBean(ImportHumanresourcesDao.class.getName());
		List<ImportHumanresources> imList = importHumanresourcesDao.queryImportHumanresourcesByCard(cardnumber, name);
    	if(imList!=null&&imList.size()>0){
    		return 1;
    	}
    	return 0;
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
