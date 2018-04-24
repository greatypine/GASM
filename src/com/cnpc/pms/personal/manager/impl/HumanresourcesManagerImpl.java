package com.cnpc.pms.personal.manager.impl;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
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

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.util.FileCopyUtils;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.paging.impl.Sort;
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
import com.cnpc.pms.dynamic.manager.DynamicManager;
import com.cnpc.pms.personal.dao.ExpressDao;
import com.cnpc.pms.personal.dao.HouseStyleDao;
import com.cnpc.pms.personal.dao.HumanenteachDao;
import com.cnpc.pms.personal.dao.HumanresourcesDao;
import com.cnpc.pms.personal.dao.ImportHumanresourcesDao;
import com.cnpc.pms.personal.dao.PublicOrderDao;
import com.cnpc.pms.personal.dao.RelationDao;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.dao.YyMicrDataDao;
import com.cnpc.pms.personal.entity.Company;
import com.cnpc.pms.personal.entity.DataHumanType;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.DistCityCode;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.Humanenteach;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.HumanresourcesChange;
import com.cnpc.pms.personal.entity.HumanresourcesLog;
import com.cnpc.pms.personal.entity.ImportHumanresources;
import com.cnpc.pms.personal.entity.OuterCompany;
import com.cnpc.pms.personal.entity.PublicOrder;
import com.cnpc.pms.personal.entity.ScoreRecordTotal;
import com.cnpc.pms.personal.entity.SelfExpress;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.SysUserGroupOpera;
import com.cnpc.pms.personal.entity.XxExpress;
import com.cnpc.pms.personal.manager.BuildingManager;
import com.cnpc.pms.personal.manager.CompanyManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.DistCityCodeManager;
import com.cnpc.pms.personal.manager.DistCityManager;
import com.cnpc.pms.personal.manager.DsAbnormalOrderManager;
import com.cnpc.pms.personal.manager.ExpressManager;
import com.cnpc.pms.personal.manager.HouseManager;
import com.cnpc.pms.personal.manager.HumanenteachManager;
import com.cnpc.pms.personal.manager.HumanresourcesChangeManager;
import com.cnpc.pms.personal.manager.HumanresourcesLogManager;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
import com.cnpc.pms.personal.manager.ImportHumanresourcesManager;
import com.cnpc.pms.personal.manager.OuterCompanyManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.SysUserGroupOperaManager;
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.platform.dao.OrderDao;
import com.cnpc.pms.platform.entity.PlatformEmployee;
import com.cnpc.pms.platform.manager.OrderManager;
import com.cnpc.pms.platform.manager.PlatformEmployeeManager;
import com.cnpc.pms.slice.manager.AreaManager;
import com.cnpc.pms.utils.ChineseToEnglish;
import com.cnpc.pms.utils.CompressFile;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.EntityEquals;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;
import com.google.gson.JsonObject;

@SuppressWarnings("all")
public class HumanresourcesManagerImpl extends BizBaseCommonManager implements HumanresourcesManager {
    
	PropertiesValueUtil propertiesValueUtil = null;

    private String folder_path = null;

    private XSSFCellStyle style_header = null;
    private CellStyle cellStyle_commonall = null;
    /**
     * 到处户型excel单元格公共样式
     */
    CellStyle cellStyle_common = null;
    
    
    /**
     * 批量同步信息 将导入的信息 同步到 分配门店表中
     */
    @Override
    public String saveHumanresource(String ids){
    	String rcvmsg=null;
    	if(ids!=null&&ids.length()>0){
    		String[] id = ids.split(",");
    		ids=ids.substring(0,ids.length()-1);
    		//调用方法 批量更改状态
    		ImportHumanresourcesDao importHumanresourcesDao = (ImportHumanresourcesDao) SpringHelper.getBean(ImportHumanresourcesDao.class.getName());
    		importHumanresourcesDao.updateImportHumanresourcesId(ids);
    		
    		//存待保存的员工
    		List<Humanresources> humanList = new ArrayList<Humanresources>();
    		
    		for(String i:id){
    			ImportHumanresourcesManager hManager = (ImportHumanresourcesManager)SpringHelper.getBean("importHumanresourcesManager");
    			ImportHumanresources importHumanresources = (ImportHumanresources) hManager.getObject(Long.parseLong(i));
    			Humanresources humanresources = new Humanresources();
    			BeanUtils.copyProperties(importHumanresources, humanresources);
    			humanresources.setId(null);
    			
    			//处理合同日期
    			try{
    				String[] strs = importHumanresources.getContractdate().split("-");
    				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    				Date startdate = sdf.parse(strs[0].replace(".", "/"));
    				Date enddate = sdf.parse(strs[1].replace(".", "/"));
    				String startdatestr = sdf.format(startdate);
    				String enddatestr = sdf.format(enddate);
    				humanresources.setContractdatestart(startdatestr);
    				humanresources.setContractdateend(enddatestr);
    			}catch(Exception e){
    				System.out.println("日期处理失败！");
    				rcvmsg="合同日期格式错误！ 导入失败！";
    				return rcvmsg;
    			}
    			
    			
    			//设置刚同步的数据为未分配门店 
    			humanresources.setHumanstatus(Long.parseLong("0"));
    			
    			if(humanresources.getZw()!=null&&humanresources.getZw().equals("市场专员")){
    				humanresources.setHumanstatus(Long.parseLong("1"));
    			}
    			
    			//取得当前登录人城市 
    			UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
    			DistCityManager distCityManager = (DistCityManager)SpringHelper.getBean("distCityManager");
    			DistCity distCity = distCityManager.queryDistCitysByUserId(userManager.getCurrentUserDTO().getId());
    			if(distCity!=null){
    				humanresources.setCitySelect(distCity.getCity1()==null?"北京":distCity.getCity1());
    			}else{
    				rcvmsg="当前登录人所属城市为空！ 导入失败！";
    				return rcvmsg;
    			}
    			
    			if(humanresources.getCardnumber()==null||humanresources.getCardnumber().trim().equals("")){
    				rcvmsg="身份证号存在空值！ 导入失败！";
    				return rcvmsg;
    			}
    			humanList.add(humanresources);
    		}
    		
    		
    		if(humanList!=null&&humanList.size()>0){
    			StringBuffer sbf = new StringBuffer();
    			StringBuffer cardnos = new StringBuffer();
				for(Humanresources h:humanList){
        			sbf.append("'");
        			sbf.append(h.getEmployee_no());
        			sbf.append("',");
        			
        			cardnos.append("'");
        			cardnos.append(h.getCardnumber());
        			cardnos.append("',");
        		}
				IFilter repFilter =FilterFactory.getSimpleFilter(" humanstatus in(0,1) and (employee_no in("+sbf.toString().substring(0,sbf.toString().length()-1)+") or cardnumber in("+cardnos.toString().substring(0,cardnos.toString().length()-1)+"))");
            	List<?> rep_list = this.getList(repFilter);
    			if(rep_list!=null&&rep_list.size()>0){
    				Humanresources repHuman = (Humanresources) rep_list.get(0);
    				rcvmsg="存在相同数据！<br> 姓名:"+repHuman.getName()+"<br> 员工号："+repHuman.getEmployee_no()+" <br> 身份证:"+repHuman.getCardnumber();
            		return rcvmsg;
    			}
    			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
    			for(Humanresources h:humanList){
    				preSaveObject(h);
        			saveObject(h);
        			
        			if(h.getZw()!=null&&h.getZw().equals("市场专员")){
        				User user = new User();
        				user.setName(h.getName());
        				user.setCode(ChineseToEnglish.getPingYin(h.getName()));
        				user.setDisabledFlag(1);
        				user.setDoctype(0);
        				user.setEmail("123@123.com");
        				user.setEmployeeId(h.getEmployee_no());
        				user.setEnablestate(1);
        				user.setPassword("e10adc3949ba59abbe56e057f20f883e"); //123456
        				user.setPk_org(Long.parseLong("40284"));
        				user.setMobilephone(h.getPhone());
        				UserGroupManager u = (UserGroupManager)SpringHelper.getBean("userGroupManager");
        				String groupzw = h.getZw();
        				if(groupzw.equals("服务专员")||groupzw.equals("综合专员")||groupzw.equals("事务管理")){
        					groupzw = "综合管理";
        				}
            	       
            			UserGroup userGroup = null;
            			if(groupzw!=null&&groupzw.equals("综合管理")){
            				IFilter iFilter =FilterFactory.getSimpleFilter("code","ZHGLY");
            		        List<?> lst_groupList = u.getList(iFilter);
            		        userGroup = (UserGroup) lst_groupList.get(0);
            			}else{
            		        IFilter iFilter =FilterFactory.getSimpleFilter(" name like '%"+groupzw+"%'");
            		        List<?> lst_groupList = u.getList(iFilter);
            		        userGroup = (UserGroup) lst_groupList.get(0);
            			}
            			
            	        user.setZw(h.getZw());
            	        user.setUsergroup(userGroup);
        				user.setLogicDel(0);
        				preSaveObject(user);
        				userManager.saveObject(user);
        			}
        			
        			
            	}
    		}
    	}
    	return rcvmsg;
    }
    
    /**
     * 岗前培训修改查询
     */	
    @Override
    public Humanresources queryHumanresourceById(Long id){
    	Humanresources humanresources = (Humanresources)this.getObject(id);
    	return humanresources;
    }
    
    
    
    /**
     * 岗后培训修改查询
     */
    @Override
    public Humanresources queryHumanenTeachById(Long id){
    	Humanresources humanresources = (Humanresources)this.getObject(id);
    	//根据你ID查询所有子信息
    	HumanenteachDao hrDao = (HumanenteachDao)SpringHelper.getBean(HumanenteachDao.class.getName()); 
    	List<Humanenteach> teList = hrDao.queryHumanenTeachByParentId(id);
    	humanresources.setHumanenteachs(teList);
    	return humanresources;
    }
    
    /**
     * 保存员工基础信息 点击新增 保存方法 
     * @param humanresources
     * @return
     */
    @Override
    public String saveHumanresourcesInfo(Humanresources humanresources){
    	String rcv = null;
    	//人员
    	HumanresourcesDao humanresourcesDao = (HumanresourcesDao) SpringHelper.getBean(HumanresourcesDao.class.getName());
    	//城市编码
    	DistCityCodeManager distCityCodeManager = (DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");
    	//公司编码
    	OuterCompanyManager outerCompanyManager = (OuterCompanyManager) SpringHelper.getBean("outerCompanyManager");
    	//门店
    	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
    	
    	//验证是否存在重复 身份证
		IFilter repFilter =FilterFactory.getSimpleFilter(" humanstatus in(0,1) and cardnumber='"+humanresources.getCardnumber()+"'");
    	List<?> rep_list = this.getList(repFilter);
		if(rep_list!=null&&rep_list.size()>0){
			Humanresources repHuman = (Humanresources) rep_list.get(0);
			rcv="存在相同数据！<br> 姓名:"+repHuman.getName()+" <br> 身份证:"+repHuman.getCardnumber();
    		return rcv;
		}
    	
    	String employee_No = humanresources.getEmployee_no();
    	if(employee_No!=null&&employee_No.length()>0){
    		//此处为有员工号的 为海格数据  只校验是否重复
    		Humanresources existHuman = check_employee_no(employee_No);
    		if(existHuman!=null&&existHuman.getEmployee_no().length()>0){
    			rcv = "该员工号已存在！保存失败！ ";
    			return rcv;
    		}else{
    			//保存数据
    			if(!humanresources.getZw().equals("市场专员")&&!humanresources.getZw().equals("线上服务专员")){
    				Store store = storeManager.findStoreByName(humanresources.getStorename());
        	    	if(store!=null){
        	    		humanresources.setStore_id(store.getStore_id());
        	    	}else{
        	    		rcv = "不存在的门店 ，保存失败！ ";
        	    		return rcv;
        	    	}
    			}
    	    	
    			humanresources.setId(null);
    			preSaveObject(humanresources);
    			saveObject(humanresources);
    			
    			//保存user
    			
    			saveUserExtend(humanresources);
    			
    		}
    	}else{
    		//此处为无员工号 需要自动生成  
    		humanresources.setId(null);
    		String author = humanresources.getAuthorizedtype();//人员类别
    		String citySelect = humanresources.getCitySelect();//城市
    		//根据人员类别 查找人员类别代码
    		OuterCompany outerCompany = outerCompanyManager.queryOuterCompanyByName(author.replace("正式", "").replace("实习", ""));
    		//根据城市 查找城市代码 
    		DistCityCode distCityCode = distCityCodeManager.queryDistCityCodeByName(citySelect);
    		StringBuffer sbf = new StringBuffer();
    		//sbf.append("GA");
    		if(outerCompany!=null&&distCityCode!=null&&outerCompany.getCompanycode()!=null&&distCityCode.getCitycode()!=null){
    			sbf.append(outerCompany.getCompanycode());
    			sbf.append(distCityCode.getCitycode());
    		}
    		if(sbf.length()==4){
    			//查询最大编号 
    			String maxEmployeeNo = humanresourcesDao.queryMaxEmpNo(sbf.toString());
    			if(maxEmployeeNo=="00000"){
    				maxEmployeeNo="GA"+sbf.toString()+maxEmployeeNo;
    			}
    			String new_employee_no = initMaxEmployee(maxEmployeeNo);
    			humanresources.setEmployee_no(new_employee_no);
    			if(!humanresources.getZw().equals("市场专员")&&!humanresources.getZw().equals("线上服务专员")){
    				Store store = storeManager.findStoreByName(humanresources.getStorename());
        	    	if(store!=null){
        	    		humanresources.setStore_id(store.getStore_id());
        	    	}else{
        	    		rcv = "不存在的门店 ，保存失败！ ";
        	    		return rcv;
        	    	}
    			}
    		}else{
    			rcv = "不存在的城市、人员类别 ，保存失败！ ";
	    		return rcv;
    		}
			preSaveObject(humanresources);
			saveObject(humanresources);
			
			//保存user
			saveUserExtend(humanresources);
			
    	}
    	
    	//操作完 进行同步 方法 
    	try {
    		syncEmployee(humanresources);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return rcv;
    }
    
    
    public User saveUserExtend(Humanresources hr){
    	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
    	//新增加一个user  市场专员
    	User user = new User();
		user.setName(hr.getName());
		user.setCode(ChineseToEnglish.getPingYin(hr.getName()));
		user.setDisabledFlag(1);
		user.setDoctype(0);
		user.setEmail("123@123.com");
		user.setEmployeeId(hr.getEmployee_no());
		user.setEnablestate(1);
		user.setPassword("e10adc3949ba59abbe56e057f20f883e"); //123456
		user.setPk_org(Long.parseLong("40284"));
		user.setMobilephone(hr.getPhone());
		user.setStore_id(hr.getStore_id());
		user.setCareergroup(hr.getCareer_group());
		UserGroupManager u = (UserGroupManager)SpringHelper.getBean("userGroupManager");
		String groupzw = hr.getZw();
		if(groupzw.equals("服务专员")||groupzw.equals("综合专员")||groupzw.equals("事务管理")){
			groupzw = "综合管理";
		}
		UserGroup userGroup = null;
		if(groupzw!=null&&groupzw.equals("综合管理")){
			IFilter iFilter =FilterFactory.getSimpleFilter("code","ZHGLY");
	        List<?> lst_groupList = u.getList(iFilter);
	        userGroup = (UserGroup) lst_groupList.get(0);
		}else{
	        IFilter iFilter =FilterFactory.getSimpleFilter(" name like '%"+groupzw+"%'");
	        List<?> lst_groupList = u.getList(iFilter);
	        userGroup = (UserGroup) lst_groupList.get(0);
		}
		
        
        user.setZw(hr.getZw());
        user.setUsergroup(userGroup);
		user.setLogicDel(0);
		preSaveObject(user);
		userManager.saveObject(user);
		return user;
    }
    /**
     * 检查员工号是否存在。
     * @param employee_no
     * @return
     */
    public Humanresources check_employee_no(String employee_no){
    	HumanresourcesManager hManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
    	IFilter iFilter =FilterFactory.getSimpleFilter("employee_no='"+employee_no+"'");
        List<Humanresources> lst_humanList = (List<Humanresources>)hManager.getList(iFilter); 
        if(lst_humanList!=null&&lst_humanList.size()>0){
        	return lst_humanList.get(0);
        }else{
        	return null;
        }
    }
    //处理最大员工号 +1
    public static String initMaxEmployee(String maxEmployeeNo){
		String nums = "0123456789";
		int num_index = 0;
		String[] strs = maxEmployeeNo.split("");
		for(int i=0;i<20;i++){
			String s = strs[i];
			if(s==""||s==null){
				i--;
			}
			if(s!=null&&s.length()>0&&nums.contains(s)){
				num_index=i;
				break;
			}
		}
		String num = maxEmployeeNo.substring(num_index,maxEmployeeNo.length());
		String temp = ("00000"+(Integer.parseInt(num)+1));
		String new_employee_no = maxEmployeeNo.substring(0,num_index)+temp.substring(temp.length()-num.length(),temp.length());
		return new_employee_no;
	}
    
    /**
     * 修改信息 分配门店 
     */
    @Override
    public Humanresources updateHumanresources(Humanresources humanresources){
    	Humanresources hr = (Humanresources)this.getObject(humanresources.getId());
    	if(hr.getHumanstatus().equals(Long.parseLong("2"))){
    		
    		hr.setLeavedate(humanresources.getLeavedate());
    		hr.setLeavereason(humanresources.getLeavereason());
    		hr.setLeavetype(humanresources.getLeavetype());
    		hr.setLeavercvlistdate(humanresources.getLeavercvlistdate());
    		
    		preSaveObject(hr);
    		saveObject(hr);
    		
    		//保存完成 、最后 插入记录表
        	HumanresourcesLogManager h = (HumanresourcesLogManager)SpringHelper.getBean("humanresourcesLogManager");
        	h.saveHumanresources(hr);
        	
    		return hr;
    	}
    	String authorizedtype = humanresources.getAuthorizedtype();
    	StoreManager s = (StoreManager) SpringHelper.getBean("storeManager");
    	Store store = s.findStoreByName(humanresources.getStorename());
    	if(store!=null){
    		humanresources.setStore_id(store.getStore_id());
    	}
    	//判断保存的八项值 是否有变化。如果有变化 则保存到humanresourceschange表中。
    	HumanresourcesChangeManager changeManager = (HumanresourcesChangeManager)SpringHelper.getBean("humanresourcesChangeManager");
    	HumanresourcesChange hChange =  new HumanresourcesChange();
    	boolean noChange=false;
    	
    	List<String> changekeys = new ArrayList<String>();
//    	changekeys.add("orgname");//1.组织机构
//    	changekeys.add("deptlevel1");//2.一级部门
//    	changekeys.add("deptlevel2");//3.二级部门
//    	changekeys.add("deptlevel3");//4.三级部门
    	changekeys.add("career_group");//5.事业群
    	changekeys.add("professnallevel");//6.职级
    	changekeys.add("zw");//7.职务
    	changekeys.add("store_id");//8.门店
    	noChange = EntityEquals.classOfSrc(humanresources, hr, changekeys);
    	
    	//如果有变化  则将变化值插入 变更表中
    	if(!noChange){
    		//组织机构
    		hChange.setOrgname(hr.getOrgname());
    		hChange.setChangeorgname(humanresources.getOrgname());
    		//一级部门
    		hChange.setDeptlevel1(hr.getDeptlevel1());
    		hChange.setChangedeptlevel1(humanresources.getDeptlevel1());
    		//二级部门
    		hChange.setDeptlevel2(hr.getDeptlevel2());
    		hChange.setChangedeptlevel2(humanresources.getDeptlevel2());
    		//三级部门
    		hChange.setDeptlevel3(hr.getDeptlevel3());
    		hChange.setChangedeptlevel3(humanresources.getDeptlevel3());
    		//汇报上级
    		hChange.setReporthigher(hr.getReporthigher());
    		hChange.setChangereporthigher(humanresources.getReporthigher());
    		//职级
    		hChange.setProfessnallevel(hr.getProfessnallevel());
    		hChange.setChangeprofessnallevel(humanresources.getProfessnallevel());
    		//职务
    		hChange.setZw(hr.getZw());
    		hChange.setChangezw(humanresources.getZw());
    		//门店
    		hChange.setStore_id(hr.getStore_id());
    		hChange.setChangestore_id(humanresources.getStore_id());
    		//门店名称
    		hChange.setStorename(hr.getStorename());
    		hChange.setChangestorename(humanresources.getStorename());
    		//事业群
    		hChange.setCareer_group(hr.getCareer_group());
    		hChange.setChangecareer_group(humanresources.getCareer_group());
    		
    		
    		//变更时间
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    		String changedate = sdf.format(new Date());
    		hChange.setChangedate(changedate);
    		//父ID
    		hChange.setEmployee_no(hr.getEmployee_no());
    		
    		//数据库中 有门店才算变更
    		if(hr.getStorename()!=null&&hr.getStorename().length()>0){
    			
    			//门店发生变动时，调用方法 清空原门店的划片信息 。
    			if(!hChange.getStore_id().equals(hChange.getChangestore_id())){
    				AreaManager areaManager = (AreaManager) SpringHelper.getBean("areaManager");
    				try {
    					areaManager.updateEmployeeIsNullOfArea(hChange.getStore_id(), hChange.getEmployee_no());
					} catch (Exception e) {
						e.printStackTrace();
					}
    			}
    			
    			preSaveObject(hChange);
        		changeManager.saveHumanresourcesChange(hChange);
    		}
    		
    	}
    	
    	//如果名字和电话有变动 则更新系统表
    	UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
		UserGroupManager userGroupManager = (UserGroupManager) SpringHelper.getBean("userGroupManager");
		User user = userManager.findEmployeeByEmployeeNo(hr.getEmployee_no());
		
		if(user!=null){
			if(!user.getName().equals(humanresources.getName())||!user.getPhone().equals(humanresources.getPhone())){
				//同步系统表
				user.setName(humanresources.getName());
				user.setPhone(humanresources.getPhone());	
				user.setMobilephone(humanresources.getPhone());
				user.setCareergroup(humanresources.getCareer_group());
				preSaveObject(user);
				userManager.saveObject(user);
			}else{
				//修改员工基础数据中所属事业群时，同步到系统user表中。
				user.setCareergroup(humanresources.getCareer_group());
				preSaveObject(user);
				userManager.saveObject(user);
			}
			
		}
		
    	hr.setName(humanresources.getName());
    	hr.setTopostdate(humanresources.getTopostdate());
    	hr.setBirthday(humanresources.getBirthday());
    	hr.setSex(humanresources.getSex());
    	hr.setCardnumber(humanresources.getCardnumber());
    	hr.setRemark(humanresources.getRemark());
    	hr.setSchool(humanresources.getSchool());
    	hr.setEducation(humanresources.getEducation());
    	hr.setProfession(humanresources.getProfession());
    	hr.setIsofficial(humanresources.getIsofficial());
    	hr.setRelationtype(humanresources.getRelationtype());
    	hr.setTel(humanresources.getTel());
    	hr.setRelationname(humanresources.getRelationname());
    	hr.setNation(humanresources.getNation());
    	
    	hr.setMarriage(humanresources.getMarriage());
    	hr.setPhone(humanresources.getPhone());
    	hr.setCensusregister(humanresources.getCensusregister());
    	
    	hr.setOrgname(humanresources.getOrgname());
    	hr.setDeptlevel1(humanresources.getDeptlevel1());
    	hr.setDeptlevel2(humanresources.getDeptlevel2());
    	hr.setDeptlevel3(humanresources.getDeptlevel3());
    	hr.setReporthigher(humanresources.getReporthigher());
    	hr.setProfessnallevel(humanresources.getProfessnallevel());
    	hr.setHumanstatus(humanresources.getHumanstatus());
    	hr.setAuthorizedtype(humanresources.getAuthorizedtype());
    	hr.setJobchannel(humanresources.getJobchannel());
    	hr.setOffername(humanresources.getOffername());
    	hr.setCitySelect(humanresources.getCitySelect());
    	hr.setInterndate(humanresources.getInterndate());
    	hr.setContractdatestart(humanresources.getContractdatestart());
    	hr.setContractdateend(humanresources.getContractdateend());
    	hr.setCareer_group(humanresources.getCareer_group());
    	
    	boolean isLeave = false;
    	//如果状态为离职状态
    	if(humanresources.getHumanstatus().equals(Long.parseLong("2"))){
    		hr.setLeavedate(humanresources.getLeavedate());
    		hr.setLeavereason(humanresources.getLeavereason());
    		hr.setLeavetype(humanresources.getLeavetype());
    		hr.setLeavercvlistdate(humanresources.getLeavercvlistdate());
    		//此处调用宝磊离职处理其它数据接口
    		isLeave = true;
    	}else{
    		hr.setLeavedate("");
    		hr.setLeavereason("");
    		hr.setLeavetype("");
    		hr.setLeavercvlistdate("");
    	}
    	
    	hr.setStorename(humanresources.getStorename());
    	
    	//正常分配门店
		System.out.println("正常分配门店信息-------------");
    	//设置状态为正式
		if(hr.getHumanstatus().equals(Long.parseLong("0"))){
			hr.setHumanstatus(Long.parseLong("1"));
		}
		if(humanresources.getZw()!=null&&humanresources.getZw().equals("市场专员")){
			store = null;
		}
		if(humanresources.getZw()!=null&&humanresources.getZw().equals("线上服务专员")){
			store = null;
		}
		// 如果界面上选择的门店 为空 那此人为 星店店长 或市场专员
    	//根据门店名。取得相应的ID
    	if(store!=null&&store.getStore_id()!=null){
    		hr.setStore_id(store.getStore_id());
    		hr.setZw(humanresources.getZw());
    		
    		try {
    			//如果之前存在门店，此为更改门店。则同步系统用户表中的store_id
        		if(hr.getStorename()!=null&&hr.getHumanstatus().equals(Long.parseLong("1"))){
        			//这里执行同步、此操作为修改门店以及职务，使其权限不同
        			System.out.println("修改系统表tb_bizbase_user门店-----------");
        			
        			if(user!=null&&user.getEmployeeId()!=null){
        				//如果存在这个用户 更改门店以及权限
        				user.setStore_id(hr.getStore_id());
            			user.setZw(hr.getZw());
            			String groupzw = hr.getZw();
            			
            			if(groupzw.equals("服务专员")||groupzw.equals("综合专员")||groupzw.equals("事务管理")){
            				groupzw = "综合管理";
            			}
            			UserGroupManager u = (UserGroupManager)SpringHelper.getBean("userGroupManager");
            			UserGroup userGroup = null;
            			if(groupzw!=null&&groupzw.equals("综合管理")){
            				IFilter iFilter =FilterFactory.getSimpleFilter("code","ZHGLY");
            		        List<?> lst_groupList = u.getList(iFilter);
            		        userGroup = (UserGroup) lst_groupList.get(0);
            			}else{
            		        IFilter iFilter =FilterFactory.getSimpleFilter(" name like '%"+groupzw+"%'");
            		        List<?> lst_groupList = u.getList(iFilter);
            		        userGroup = (UserGroup) lst_groupList.get(0);
            			}
            			
            	        user.setUsergroup(userGroup);
            			preSaveObject(user);
            			userManager.saveObject(user);
        			}
        			
        		}
    			
            	preSaveObject(hr);
            	saveObject(hr);
            	
            	//更新系统表｛修改、创建用户使可登录｝
            	changeSystemUser(hr, hr.getHumanstatus());
            	
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}else{
    		//如果门店为空 也可以保存
    		hr.setZw(humanresources.getZw());
    		
    		if(user!=null&&user.getEmployeeId()!=null){
				//如果存在这个用户 更改门店以及权限
    			user.setZw(hr.getZw());
    			user.setStore_id(null);
    			UserGroupManager u = (UserGroupManager)SpringHelper.getBean("userGroupManager");
    			String groupzw = hr.getZw();
    			if(groupzw.equals("服务专员")||groupzw.equals("综合专员")||groupzw.equals("事务管理")){
    				groupzw = "综合管理";
    			}
    	        
    			UserGroup userGroup = null;
    			if(groupzw!=null&&groupzw.equals("综合管理")){
    				IFilter iFilter =FilterFactory.getSimpleFilter("code","ZHGLY");
    		        List<?> lst_groupList = u.getList(iFilter);
    		        userGroup = (UserGroup) lst_groupList.get(0);
    			}else{
    		        IFilter iFilter =FilterFactory.getSimpleFilter(" name like '%"+groupzw+"%'");
    		        List<?> lst_groupList = u.getList(iFilter);
    		        userGroup = (UserGroup) lst_groupList.get(0);
    			}
    			
    	        user.setUsergroup(userGroup);
    			preSaveObject(user);
    			userManager.saveObject(user);
			}else{
				//新增加一个user  市场专员
				user = new User();
				user.setName(hr.getName());
				user.setCode(ChineseToEnglish.getPingYin(hr.getName()));
				user.setDisabledFlag(1);
				user.setDoctype(0);
				user.setEmail("123@123.com");
				user.setEmployeeId(hr.getEmployee_no());
				user.setEnablestate(1);
				user.setPassword("e10adc3949ba59abbe56e057f20f883e"); //123456
				user.setPk_org(Long.parseLong("40284"));
				user.setMobilephone(humanresources.getPhone());
				user.setCareergroup(hr.getCareer_group());
				UserGroupManager u = (UserGroupManager)SpringHelper.getBean("userGroupManager");
				String groupzw = humanresources.getZw();
    			if(groupzw.equals("服务专员")||groupzw.equals("综合专员")||groupzw.equals("事务管理")){
    				groupzw = "综合管理";
    			}
    			
    			UserGroup userGroup = null;
    			if(groupzw!=null&&groupzw.equals("综合管理")){
    				IFilter iFilter =FilterFactory.getSimpleFilter("code","ZHGLY");
    		        List<?> lst_groupList = u.getList(iFilter);
    		        userGroup = (UserGroup) lst_groupList.get(0);
    			}else{
    		        IFilter iFilter =FilterFactory.getSimpleFilter(" name like '%"+groupzw+"%'");
    		        List<?> lst_groupList = u.getList(iFilter);
    		        userGroup = (UserGroup) lst_groupList.get(0);
    			}
    			
    	        user.setZw(humanresources.getZw());
    	        user.setUsergroup(userGroup);
				user.setLogicDel(0);
				preSaveObject(user);
				userManager.saveObject(user);
				
			}
    		hr.setStore_id(null);
    		hr.setStorename(null);
    		preSaveObject(hr);
        	saveObject(hr);
    	}
    	
    	//保存完成 、最后 插入记录表
    	HumanresourcesLogManager h = (HumanresourcesLogManager)SpringHelper.getBean("humanresourcesLogManager");
    	h.saveHumanresources(hr);
    	
    	if(isLeave){
    		//调用 如果人员离职。更新用户画像等其它地方的方法 。
    		humanLeaveConfig(hr);
    	}
    	
    	//修改人员信息操作完，同步人员信息
    	try {
    		syncEmployee(hr);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return hr;
    }
    
    //同步人员信息 
    public String syncEmployee(Humanresources hr){
    	String rt="";
    	DynamicManager dynamicManager = (DynamicManager) SpringHelper.getBean("dynamicManager");
    	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
    	Store store = storeManager.findStoreByName(hr.getStorename());
    	if(store!=null&&store.getStoreno()!=null&&hr.getEmployee_no()!=null&&hr.getName()!=null&&hr.getPhone()!=null){
    		boolean sync=true;
    		if(store.getStoreno().contains("W")){//未知门店 不同步 
    			sync=false;
    		}
    		if(store.getStoreno().contains("C")){//仓店 不同步
    			sync=false;
    		}
    		if(store.getName().contains("储备店")){//储备店 不同步 
    			sync=false;
    		}
    		if(store.getName().contains("办公室")){//办公室 不同步 
    			sync=false;
    		}
    		if(sync){
    			JSONObject jsonObject = dynamicManager.insertNewEmployee(store.getStoreno(), hr.getEmployee_no(), hr.getName(), hr.getPhone());
    			rt=jsonObject.toString();
    		}
    	}
    	return rt;
    }
    
    //员工离职后，更新 用户画像、划片等方法  。
    private void humanLeaveConfig(Humanresources humanresources){
    	System.out.println("doLeaveConfig start ");
    	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
    	UserDTO userDTO = userManager.getCurrentUserDTO();
    	String employee_no = humanresources.getEmployee_no();
    	if(employee_no!=null&&employee_no.length()>0){
    		/**
    		 * 接口 ：AreaManager         
    		 * 方法： clearAreaOfEmployee(String employeeNo1,String employeeNo2) 
    		 * 参数： 	employeeNo1 离职员工编号       employeeNo2 HR 的ID
    		 */
			try {
				AreaManager areaManager = (AreaManager) SpringHelper.getBean("areaManager");
	    		areaManager.clearAreaOfEmployee(humanresources.getEmployee_no(),userDTO.getId().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    	}
    	
    	System.out.println("doLeaveConfig end ");
    }
    
    
    /**
     * 修改信息编制
     */
    @Override
    public void updateAuthorizedtype(Humanresources humanresources){
    	Humanresources hr = (Humanresources)this.getObject(humanresources.getId());
    	hr.setAuthorizedtype(humanresources.getAuthorizedtype());
    	preSaveObject(hr);
    	saveObject(hr);
    }

	@Override
	public File exportHumanExcel() throws Exception {
		String str_file_name = "export_human_template";
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
		
		List<Humanresources> lst_humanList = (List<Humanresources>)this.getList(fsp);

		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_newfilepath = str_file_dir_path + "human_list.xls";
		File file_new = new File(str_newfilepath);
		if(file_new.exists()){
			file_new.delete();
		}

		FileCopyUtils.copy(file_template, file_new);
		FileInputStream fis_input_excel = new FileInputStream(file_new);
		FileOutputStream fis_out_excel = null;
		Workbook wb_humaninfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
		try{
			setCellStyle_common(wb_humaninfo);

			Sheet sh_job = wb_humaninfo.getSheetAt(0);
			Sheet sh_quit = wb_humaninfo.getSheetAt(1);
			int nJobIndex = 2;
			int nQuitIndex = 2;
			Map<String,Humanresources> map_humanresources = new HashMap<String, Humanresources>();

			for (Humanresources humanresources : lst_humanList) {
				Row obj_row = null;
				map_humanresources.put(humanresources.getEmployee_no(),humanresources);
				int cellIndex = 0;
				if(humanresources.getHumanstatus() == 1L){
					sh_job.createRow(nJobIndex);
					obj_row = sh_job.getRow(nJobIndex);
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue((nJobIndex - 1)));//序号
				}else if(humanresources.getHumanstatus() == 2L){
					sh_quit.createRow(nQuitIndex);
					obj_row = sh_quit.getRow(nQuitIndex);
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue((nQuitIndex - 1)));//序号
					setCellValue(obj_row, cellIndex ++,ValueUtil.getStringValue(humanresources.getLeavedate()));//序号
				}else{
					continue;
				}
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getCitySelect()));//城市
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getEmployee_no()));//员工号
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getName()));//姓名
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getAuthorizedtype()));//人员类别
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getOrgname()));//所属机构
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getDeptlevel1()));//一级部门
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getDeptlevel2()));//二级部门
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getDeptlevel3()));//三级部门
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getStorename()));//门店
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getReporthigher()));//汇报上级
				String expzw = humanresources.getZw();
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(expzw));//职位
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getProfessnallevel()));//职级

				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getInterndate()));//外包/实习生入职日期T
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getTopostdate()));//正式员工入职日期
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getContractdatestart()));//合同开始日期
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getContractdateend()));//合同结束日期
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getSigncount()));//签订次数

				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getCardnumber()));//身份证号码
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getSex()));//性别
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getBirthday()));//出生日期
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getNation()));//民族
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getEducation()));//学历
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getSchool()));//毕业学校
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getProfession()));//专业
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getCensusregister()));//户籍
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getMarriage()));//婚姻状况
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getPhone()));//本人电话
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getRelationname()));//紧急联系人姓名
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getTel()));//紧急联系人电话
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getRelationtype()));//与本人关系
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getJobchannel()));//招聘渠道
				if(humanresources.getHumanstatus() == 2L){
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getLeavereason()));//离职原因
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getLeavetype()));//离职类型
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getLeavercvlistdate()));//收单日期
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getCareer_group()));//事业群
					nQuitIndex ++;
				}else{
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getOffername()));//推荐人姓名
					
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getCareer_group()));//事业群
					
					nJobIndex++;
				}

			}

			Sheet sh_change = wb_humaninfo.getSheetAt(2);

			int nChangeIndex = 2;
			fsp = new FSP();
			fsp.setSort(SortFactory.createSort("id",ISort.ASC));
			HumanresourcesChangeManager humanresourcesChangeManager = (HumanresourcesChangeManager)SpringHelper.getBean("humanresourcesChangeManager");
			List<HumanresourcesChange> lst_humanChangeList = (List<HumanresourcesChange>)humanresourcesChangeManager.getList(fsp);
			for(HumanresourcesChange humanresourcesChange : lst_humanChangeList){
				sh_change.createRow(nChangeIndex);
				Row obj_row = sh_change.getRow(nChangeIndex);
				if(!map_humanresources.containsKey(humanresourcesChange.getEmployee_no())){
					continue;
				}
				int cellIndex = 0;
				Humanresources humanresources = map_humanresources.get(humanresourcesChange.getEmployee_no());
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue((nChangeIndex - 1)));//序号
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getChangedate()));//序号
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getCitySelect()));//城市
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getEmployee_no()));//员工号
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getName()));//姓名
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getAuthorizedtype()));//人员类别
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getOrgname()));//所属机构

				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getDeptlevel1()));//一级部门
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getDeptlevel2()));//二级部门
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getDeptlevel3()));//三级部门
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getStorename()));//门店
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getReporthigher()));//汇报上级
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getZw()));//职位
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getProfessnallevel()));//职级
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getCareer_group()));//事业群
				
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getChangeorgname()));//所属机构
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getChangedeptlevel1()));//一级部门
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getChangedeptlevel2()));//二级部门
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getChangedeptlevel3()));//三级部门
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getChangestorename()));//门店
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getChangereporthigher()));//汇报上级
				String changezw = humanresourcesChange.getChangezw();
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(changezw));//职位
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getChangeprofessnallevel()));//职级
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresourcesChange.getChangecareer_group()));//更改后事业群


				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getInterndate()));//外包/实习生入职日期T
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getTopostdate()));//正式员工入职日期
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getContractdatestart()));//合同开始日期
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getContractdateend()));//合同结束日期
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getSigncount()));//签订次数

				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getCardnumber()));//身份证号码
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getSex()));//性别
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getBirthday()));//出生日期
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getNation()));//民族
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getEducation()));//学历
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getSchool()));//毕业学校
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getProfession()));//专业
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getCensusregister()));//户籍
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getMarriage()));//婚姻状况
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getPhone()));//本人电话
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getRelationname()));//紧急联系人姓名
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getTel()));//紧急联系人电话
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getRelationtype()));//与本人关系
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getJobchannel()));//招聘渠道
				setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(humanresources.getOffername()));//推荐人姓名
				nChangeIndex ++;
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

	private void changeSystemUser(Humanresources hr, Long humanstatus) {
		try {
    		//进行判断 如果是正式，则同步系统用户
        	if(humanstatus!=null){
        		UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
    			UserGroupManager userGroupManager = (UserGroupManager) SpringHelper.getBean("userGroupManager");
    			User user = userManager.findEmployeeByEmployeeNo(hr.getEmployee_no());
        		if(humanstatus.equals(Long.parseLong("0"))||humanstatus.equals(Long.parseLong("2"))){
        			//查询系统表。如果存在 则更改状态，不允许登录 如果不存在。则不做任何操作。
        			if(user!=null&&user.getEmployeeId()!=null){
        				user.setDisabledFlag(0);
            			preSaveObject(user);
            			userManager.saveObject(user);
        			}else{
        				//系统表 创建一条新用户/离职的 不可用的。
        				user = new User();
        				user.setName(hr.getName());
        				user.setCode(ChineseToEnglish.getPingYin(hr.getName()));
        				user.setDisabledFlag(0);
        				user.setDoctype(0);
        				user.setEmail("123@123.com");
        				user.setEmployeeId(hr.getEmployee_no());
        				user.setEnablestate(1);
        				user.setPassword("e10adc3949ba59abbe56e057f20f883e"); //123456
        				user.setPk_org(Long.parseLong("40284"));
        				user.setMobilephone(hr.getPhone());
        				UserGroupManager u = (UserGroupManager)SpringHelper.getBean("userGroupManager");
        				String groupzw = hr.getZw();
        				if(groupzw.equals("服务专员")||groupzw.equals("综合专员")||groupzw.equals("事务管理")){
        					groupzw = "综合管理";
        				}
        				
            			UserGroup userGroup = null;
            			if(groupzw!=null&&groupzw.equals("综合管理")){
            				IFilter iFilter =FilterFactory.getSimpleFilter("code","ZHGLY");
            		        List<?> lst_groupList = u.getList(iFilter);
            		        userGroup = (UserGroup) lst_groupList.get(0);
            			}else{
            		        IFilter iFilter =FilterFactory.getSimpleFilter(" name like '%"+groupzw+"%'");
            		        List<?> lst_groupList = u.getList(iFilter);
            		        userGroup = (UserGroup) lst_groupList.get(0);
            			}
            			
            	        
            	        user.setZw(hr.getZw());
            	        user.setUsergroup(userGroup);
        				user.setLogicDel(0);
        				user.setStore_id(hr.getStore_id());
        				user.setCareergroup(hr.getCareer_group());
        				preSaveObject(user);
        				userManager.saveObject(user);
        			}
        		}else{
        			//查询系统表。如果存在 则更改状态,允许登录   如果不存在。则创建一条。
        			if(user!=null&&user.getEmployeeId()!=null){
        				user.setDisabledFlag(1);
            			preSaveObject(user);
            			userManager.saveObject(user);
        			}else{
        				//系统表 创建一条新用户
        				user = new User();
        				
        				user.setName(hr.getName());
        				user.setCode(ChineseToEnglish.getPingYin(hr.getName()));
        				user.setDisabledFlag(1);
        				user.setDoctype(0);
        				user.setEmail("123@123.com");
        				user.setEmployeeId(hr.getEmployee_no());
        				user.setEnablestate(1);
        				user.setPassword("e10adc3949ba59abbe56e057f20f883e"); //123456
        				user.setPk_org(Long.parseLong("40284"));
        				user.setMobilephone(hr.getPhone());
        				
        				String groupzw = hr.getZw();
        				if(groupzw.equals("服务专员")||groupzw.equals("综合专员")||groupzw.equals("事务管理")){
        					groupzw = "综合管理";
        				}
        				UserGroupManager u = (UserGroupManager)SpringHelper.getBean("userGroupManager");
            			UserGroup userGroup = null;
            			if(groupzw!=null&&groupzw.equals("综合管理")){
            				IFilter iFilter =FilterFactory.getSimpleFilter("code","ZHGLY");
            		        List<?> lst_groupList = u.getList(iFilter);
            		        userGroup = (UserGroup) lst_groupList.get(0);
            			}else{
            		        IFilter iFilter =FilterFactory.getSimpleFilter(" name like '%"+groupzw+"%'");
            		        List<?> lst_groupList = u.getList(iFilter);
            		        userGroup = (UserGroup) lst_groupList.get(0);
            			}
            			
            	        user.setZw(hr.getZw());
            	        user.setUsergroup(userGroup);
            	        
        				/*if(hr.getZw()!=null&&hr.getZw().equals("国安侠")){
        					user.setZw(hr.getZw());
        					UserGroup userGroup = (UserGroup) userGroupManager.getObject(Long.parseLong("3224"));
        					user.setUsergroup(userGroup);
        				}else if(hr.getZw()!=null&&hr.getZw().equals("综合管理")){
        					user.setZw(hr.getZw());
        					UserGroup userGroup = (UserGroup) userGroupManager.getObject(Long.parseLong("3225"));
        					user.setUsergroup(userGroup);
        				}else if(hr.getZw()!=null&&hr.getZw().contains("副店长")){
        					user.setZw(hr.getZw());
        					UserGroup userGroup = (UserGroup) userGroupManager.getObject(Long.parseLong("3223"));
        					user.setUsergroup(userGroup);
        				}*/
        				
        				user.setLogicDel(0);
        				user.setStore_id(hr.getStore_id());
        				user.setCareergroup(hr.getCareer_group());
        				preSaveObject(user);
        				userManager.saveObject(user);
        			}
        		}
        	}
        	/*//保存完成 、最后 插入记录表
        	HumanresourcesLogManager h = (HumanresourcesLogManager)SpringHelper.getBean("humanresourcesLogManager");
        	h.saveHumanresources(hr);*/
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    
    
    
    
    /**
     * 岗前培训修改
     */
    @Override
    public Humanresources updateHumanresourcesTeach(Humanresources humanresources){
    	Humanresources hr = (Humanresources)this.getObject(humanresources.getId());

    	hr.setTheorystartdate(humanresources.getTheorystartdate());
    	hr.setTheoryenddate(humanresources.getTheoryenddate());
    	hr.setTheorystorename(humanresources.getTheorystorename());
    	hr.setOnlinestartdate(humanresources.getOnlinestartdate());
    	hr.setOnlinescore(humanresources.getOnlinescore());
    	hr.setOnlinedate(humanresources.getOnlinedate());
    	hr.setOfflinestartdate(humanresources.getOfflinestartdate());
    	hr.setOfflinescore(humanresources.getOfflinescore());
    	hr.setOfflinedate(humanresources.getOfflinedate());
    	hr.setRealitystartdate(humanresources.getRealitystartdate());
    	hr.setRealityenddate(humanresources.getRealityenddate());
    	hr.setRealitystorename(humanresources.getRealitystorename());
    	hr.setRealityscore(humanresources.getRealityscore());
    	
    	preSaveObject(hr);
    	saveObject(hr);
    	return hr;
    }
    
    
    /**
     * 岗后培训更改
     */
    @Override
    public Humanresources updateHumanresourcesenTeach(Humanresources humanresources){
    	System.out.println(humanresources.getId());
		HumanenteachManager htManager = (HumanenteachManager)SpringHelper.getBean("humanenteachManager");
		if(humanresources!=null&&humanresources.getHumanenteachs().size()>0){
			//删除该条数据下 所有 子信息
			htManager.deleteHumanTeachByParentId(humanresources.getId());
			//重新保存所有子信息
			for(Humanenteach ht:humanresources.getHumanenteachs()){
				ht.setHr_id(humanresources.getId());
				htManager.saveHumanTeach(ht);
			}
		}
    	return humanresources;
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
      * 导出培训模板
      */
     @Override
	 public String saveHumanTeachForExcel(Humanresources hr) throws Exception {
	        String str_file_name = "human_teach_template";
	        String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
	        //配置文件中的路径
	        String str_filepath = strRootpath.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));
	
	        File file_template = new File(str_filepath);
	       
	        HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
	        
	        
	        StringBuffer queryCondition=new StringBuffer();
	        queryCondition.append(" humanstatus<>0 ");
	        if(hr.getName()!=null&&hr.getName().length()>0){
	        	queryCondition.append(" and name like '%"+hr.getName()+"%' ");
	        }
	        if(hr.getEmployee_no()!=null&&hr.getEmployee_no().length()>0){
	        	queryCondition.append(" and employee_no like '%"+hr.getEmployee_no()+"%' ");
	        }
	        if(hr.getCitySelect()!=null&&hr.getCitySelect().length()>0){
	        	queryCondition.append(" and citySelect like '%"+hr.getCitySelect()+"%'");
	        }
	        if(hr.getZw()!=null&&hr.getZw().length()>0){
	        	queryCondition.append(" and zw like '%"+hr.getZw()+"%'");
	        }
	        if(hr.getStorename()!=null&&hr.getStorename().length()>0){
	        	queryCondition.append(" and storename like '%"+hr.getStorename()+"%'");
	        }
	        
	        if(hr.getTopostdate()!=null&&hr.getTopostdate().length()>0){
	        	queryCondition.append(" and DATE_FORMAT(topostdate,'%Y-%m-%d') >='"+hr.getTopostdate()+"'");
			}
			if(hr.getWorkdate()!=null&&hr.getWorkdate().length()>0){
				queryCondition.append(" and DATE_FORMAT(topostdate,'%Y-%m-%d') <='"+hr.getWorkdate()+"'");
			}
			
			if(hr.getHumanstatus()!=null&&hr.getHumanstatus().toString().length()>0){
				queryCondition.append(" and humanstatus = '"+hr.getHumanstatus().toString()+"'");
			}
			
	        
	        FSP fsp = new FSP();
	        IFilter iFilter =FilterFactory.getSimpleFilter(queryCondition.toString());
	        fsp.setUserFilter(iFilter);
	        List<?> lst_humanList = humanresourcesManager.getList(fsp);
	
	        String str_file_dir_path = PropertiesUtil.getValue("file.root");
	        String str_newfilepath = str_file_dir_path + "/file_template.xls";
            File file_new = new File(str_newfilepath);
	        
            FileCopyUtils.copy(file_template, file_new);
            FileInputStream fis_input_excel = new FileInputStream(file_new);
            Workbook wb_humaninfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
            
            setCellStyle_common(wb_humaninfo);
            
            Sheet sh_data = wb_humaninfo.getSheetAt(0);
	        int nRowIndex=2;
	        for (Object obj_human_object : lst_humanList) {
	        	Humanresources humanresources = (Humanresources) obj_human_object;
	        	sh_data.createRow(nRowIndex);
	        	Row obj_row = sh_data.getRow(nRowIndex);
	        	
	        	setCellValue(obj_row, 0, ValueUtil.getStringValue(humanresources.getId()));//序号
	        	setCellValue(obj_row, 1, ValueUtil.getStringValue(humanresources.getEmployee_no()));//工号
	        	setCellValue(obj_row, 2, ValueUtil.getStringValue(humanresources.getName()));//姓名
	        	setCellValue(obj_row, 3, ValueUtil.getStringValue(humanresources.getZw()));//岗位
	        	setCellValue(obj_row, 4, ValueUtil.getStringValue(humanresources.getAuthorizedtype()));//编制
	        	setCellValue(obj_row, 5, ValueUtil.getStringValue(humanresources.getStorename()));//所在门店
	        	setCellValue(obj_row, 6, ValueUtil.getStringValue(humanresources.getPhone()));//手机号
	        	String t_status = "";
	        	if(humanresources.getHumanstatus()!=null&&humanresources.getHumanstatus().toString().equals("1")){
	        		t_status="在职";
	        	}else{
	        		t_status="离职";
	        	}
	        	setCellValue(obj_row, 7, ValueUtil.getStringValue(t_status));//状态
	        	
	        	setCellValue(obj_row, 8, ValueUtil.getStringValue(humanresources.getTheorystartdate()));//理论：开始时间
	        	setCellValue(obj_row, 9, ValueUtil.getStringValue(humanresources.getTheoryenddate()));//理论：结束时间
	        	setCellValue(obj_row, 10, ValueUtil.getStringValue(humanresources.getTheorystorename()));//理论：见习门店
	        	
	        	setCellValue(obj_row, 11, ValueUtil.getStringValue(humanresources.getOnlinestartdate()));//岗前线上：开始时间
	        	setCellValue(obj_row, 12, ValueUtil.getStringValue(isZero(humanresources.getOnlinescore())));//岗前线上：成绩
	        	setCellValue(obj_row, 13, ValueUtil.getStringValue(humanresources.getOnlinedate()));//岗前线上：时长
	        	
	        	setCellValue(obj_row, 14, ValueUtil.getStringValue(humanresources.getOfflinestartdate()));//岗前线下：开始时间
	        	setCellValue(obj_row, 15, ValueUtil.getStringValue(isZero(humanresources.getOfflinescore())));//岗前线下：成绩
	        	setCellValue(obj_row, 16, ValueUtil.getStringValue(humanresources.getOfflinedate()));//岗前线下：时长
	        	
	        	setCellValue(obj_row, 17, ValueUtil.getStringValue(humanresources.getRealitystartdate()));//实操：开始时间
	        	setCellValue(obj_row, 18, ValueUtil.getStringValue(humanresources.getRealityenddate()));//实操：结束时间
	        	setCellValue(obj_row, 19, ValueUtil.getStringValue(humanresources.getRealitystorename()));//实操：见习门店
	        	setCellValue(obj_row, 20, ValueUtil.getStringValue(isZero(humanresources.getRealityscore())));//实操：成绩
	        	
	        	nRowIndex++;
	        }
	        
	        
	        FileOutputStream fis_out_excel = new FileOutputStream(file_new);
	        wb_humaninfo.write(fis_out_excel);
	        fis_out_excel.close();
	        fis_input_excel.close();
	        
	        return PropertiesUtil.getValue("file.web.root")+"/file_template.xls";
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
    
    
    public Object isZero(Double d){
    	try {
    		if(d!=null){
        		return d;
        	}else{
        		return "";
        	}
		} catch (Exception e) {
			return "";
		}
    }
    
    
    
    /**
     * 获取上传文件夹路径
     * @return 上传文件夹路径
     */
    public String getFolder_path() {
        if(folder_path == null){
            String FILE_ROOT = PropertiesUtil.getValue("file.root");
            folder_path = FILE_ROOT.concat(File.separator).concat("template").concat(File.separator);
        }
        return folder_path;
    }

    public void setFolder_path(String folder_path) {
        this.folder_path = folder_path;
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
    
    /**
     * 基础信息列表
     */
    @Override
    public Map<String, Object> queryhumanbaseList(QueryConditions condition) {
    	UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
    	StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
    	/*UserDTO userDTO = manager.getCurrentUserDTO();
		Long store_id = userDTO.getStore_id();*/
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		String name = null;
		String employee_no = null;
		String begindate=null;
		String enddate = null;
		String humanstatus=null;
		String storename=null;
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
			if("storename".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				storename = map.get("value").toString();
			}
			if("zw".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				zw = map.get("value").toString();
				/*if(zw!=null&&zw.contains("事务")){
					zw=zw.replace("事务", "综合");
				}*/
			}
			if("citySelect".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				citySelect = map.get("value").toString();
			}
			
		}
		List<?> lst_data = null;
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("id", ISort.DESC));
		StringBuffer sbfCondition = new StringBuffer(); 
		
		//取得当前登录人 所管理城市
		String cityssql = "";
		List<DistCity> distCityList = userManager.getCurrentUserCity();
		if(distCityList!=null&&distCityList.size()>0){
			for(DistCity d:distCityList){
				cityssql += "'"+d.getCityname()+"',";
			}
			cityssql=cityssql.substring(0, cityssql.length()-1);
		}
		
		sbfCondition.append(" status!=1 ");
		
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
		if(storename!=null&&storename.length()>0){
			Store store = storeManager.findStoreByName(storename);
			if(store!=null){
				sbfCondition.append(" and store_id="+store.getStore_id());
			}else{
				sbfCondition.append(" and store_id=0 ");
			}
			
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
		
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", lst_data);
		return returnMap;
	}
    
    
    
    
    
    /**
     * 基础信息列表-店长访问 
     */
    @Override
    public Map<String, Object> queryhumanbaseDZList(QueryConditions condition) {
    	UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
    	StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
    	UserDTO userDTO = userManager.getCurrentUserDTO();
		Long store_id = userDTO.getStore_id();
		
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		String name = null;
		String employee_no = null;
		String humanstatus=null;
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
			
		}
		List<?> lst_data = new ArrayList();
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("id", ISort.DESC));
		StringBuffer sbfCondition = new StringBuffer(); 
		sbfCondition.append(" 1=1 ");
		if(name!=null){
			sbfCondition.append(" and name like '%"+name+"%'");
		}
		if(employee_no!=null){
			sbfCondition.append(" and employee_no like '%"+employee_no+"%'");
		}
		if(humanstatus!=null){
			sbfCondition.append(" and humanstatus = '"+humanstatus+"'");
		}
		
		sbfCondition.append(" and store_id="+store_id);
		
		
		sbfCondition.append(" order by humanstatus ASC,DATE_FORMAT(topostdate,'%Y-%m-%d') DESC,update_time DESC ");
		
		IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition.toString());
		fsp.setPage(pageInfo);
		fsp.setUserFilter(iFilter);
		lst_data = this.getList(fsp);
		if(lst_data==null){
			lst_data = new ArrayList();
		}
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", lst_data);
		return returnMap;
	}
    
    
    
    //店长修改 事业群
    @Override
    public Humanresources updateHumanresourceCareerGroupById(Humanresources hr){
    	HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
    	HumanresourcesLogManager humanresourcesLogManager = (HumanresourcesLogManager) SpringHelper.getBean("humanresourcesLogManager");
    	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
    	
    	//查询 修改 保存 同步 user 添加历史记录
    	Humanresources humanresources = (Humanresources) humanresourcesManager.getObject(hr.getId());
    	//判断是否发生变更了
    	boolean ischange = false;
    	HumanresourcesChange hChange = new HumanresourcesChange();
    	if(humanresources.getCareer_group()!=null&&!humanresources.getCareer_group().equals(hr.getCareer_group()==null?"":hr.getCareer_group())){
    		ischange = true;
    		//事业群
    		hChange.setCareer_group(humanresources.getCareer_group());
    		hChange.setChangecareer_group(hr.getCareer_group());
    	}
    	if(hr.getCareer_group()!=null&&!hr.getCareer_group().equals(humanresources.getCareer_group()==null?"":humanresources.getCareer_group())){
    		ischange = true;
    		//事业群
    		hChange.setCareer_group(humanresources.getCareer_group());
    		hChange.setChangecareer_group(hr.getCareer_group());
    	}
    	
    	
    	humanresources.setCareer_group(hr.getCareer_group());
    	preSaveObject(humanresources);
    	humanresourcesManager.saveObject(humanresources);
    	//同步到user表
    	User user = userManager.findEmployeeByEmployeeNo(humanresources.getEmployee_no());
    	user.setCareergroup(humanresources.getCareer_group());
    	preSaveObject(user);
    	userManager.saveObject(user);
    	
    	//添加花名册变更记录
    	if(ischange){
    		HumanresourcesChangeManager humanresourcesChangeManager = (HumanresourcesChangeManager) SpringHelper.getBean("humanresourcesChangeManager");
        	//组织机构
    		hChange.setOrgname(humanresources.getOrgname());
    		hChange.setChangeorgname(humanresources.getOrgname());
    		//一级部门
    		hChange.setDeptlevel1(humanresources.getDeptlevel1());
    		hChange.setChangedeptlevel1(humanresources.getDeptlevel1());
    		//二级部门
    		hChange.setDeptlevel2(humanresources.getDeptlevel2());
    		hChange.setChangedeptlevel2(humanresources.getDeptlevel2());
    		//三级部门
    		hChange.setDeptlevel3(humanresources.getDeptlevel3());
    		hChange.setChangedeptlevel3(humanresources.getDeptlevel3());
    		//汇报上级
    		hChange.setReporthigher(humanresources.getReporthigher());
    		hChange.setChangereporthigher(humanresources.getReporthigher());
    		//职级
    		hChange.setProfessnallevel(humanresources.getProfessnallevel());
    		hChange.setChangeprofessnallevel(humanresources.getProfessnallevel());
    		//职务
    		hChange.setZw(humanresources.getZw());
    		hChange.setChangezw(humanresources.getZw());
    		//门店
    		hChange.setStore_id(humanresources.getStore_id());
    		hChange.setChangestore_id(humanresources.getStore_id());
    		//门店名称
    		hChange.setStorename(humanresources.getStorename());
    		hChange.setChangestorename(humanresources.getStorename());
    		
    		//变更时间
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    		String changedate = sdf.format(new Date());
    		hChange.setChangedate(changedate);
    		//父ID
    		hChange.setEmployee_no(humanresources.getEmployee_no());
    		
			preSaveObject(hChange);
			humanresourcesChangeManager.saveHumanresourcesChange(hChange);
    	}
    	
    	//添加历史记录
    	humanresourcesLogManager.saveHumanresources(humanresources);
    	
    	return humanresources;
    }
    
    
    
    
    
    /**
     * 培训信息列表  status!=1 and humanstatus=1
     * @param condition
     * @return
     */
    @Override
    public Map<String, Object> queryhumanList(QueryConditions condition) {
    	/*UserManager manager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = manager.getCurrentUserDTO();
		Long store_id = userDTO.getStore_id();*/
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		String name = null;
		String employee_no = null;
		String begindate=null;
		String enddate = null;
		String humanstatus=null;
		String citySelect = null;
		String storename = null;
		String zw = null;
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
			//城市 
			if("citySelect".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				citySelect = map.get("value").toString();
			}
			
			//城市 
			if("zw".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				zw = map.get("value").toString();
			}
			
			//门店 
			if("storename".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				storename = map.get("value").toString();
			}
			
			if("topostdate".equals(map.get("key"))&&map.get("value")!=null){//查询条件
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
		sbfCondition.append(" status!=1 and humanstatus!=0 ");
		if(name!=null){
			sbfCondition.append(" and name like '%"+name+"%'");
		}
		if(employee_no!=null){
			sbfCondition.append(" and employee_no like '%"+employee_no+"%'");
		}
		if(citySelect!=null){
			sbfCondition.append(" and citySelect like '"+citySelect+"'");
		}
		if(zw!=null){
			sbfCondition.append(" and zw like '"+zw+"'");
		}
		if(storename!=null){
			sbfCondition.append(" and storename like '%"+storename+"%'");
		}
		if(begindate!=null){
			sbfCondition.append(" and DATE_FORMAT(topostdate,'%Y-%m-%d') >='"+begindate+"'");
		}
		if(enddate!=null){
			sbfCondition.append(" and DATE_FORMAT(topostdate,'%Y-%m-%d') <='"+enddate+"'");
		}
		if(humanstatus!=null){
			sbfCondition.append(" and humanstatus = '"+humanstatus+"'");
		}
		
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
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
		
		
		sbfCondition.append(" order by DATE_FORMAT(topostdate,'%Y-%m-%d') DESC ");
		
		IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition.toString());
		fsp.setPage(pageInfo);
		fsp.setUserFilter(iFilter);
		lst_data = this.getList(fsp);
		
		List<Humanresources> rList = new ArrayList<Humanresources>();
		HumanenteachDao humanenteachDao = (HumanenteachDao)SpringHelper.getBean(HumanenteachDao.class.getName());
		for(int i = 0 ; i < lst_data.size() ; i++){
			Humanresources hi = (Humanresources) lst_data.get(i);
			//查询子集信息
			List<Humanenteach> humanenteachs = humanenteachDao.queryHumanenTeachByParentId(hi.getId());
			hi.setOfflinescount(humanenteachs.size());
			
			//处理培训信息 返回 四个 岗前培训信息中 最新的 显示界面列表中。
			
			String theorystartdate=hi.getTheorystartdate()==null?hi.getTheorystartdate():hi.getTheorystartdate().replaceAll("[^0-9]", ""); //理论见习培训 开始时间
			String onlinestartdate=hi.getOnlinestartdate()==null?hi.getOnlinestartdate():hi.getOnlinestartdate().replaceAll("[^0-9]", ""); //岗前线上培训 开始时间
			String offlinestartdate=hi.getOfflinestartdate()==null?hi.getOfflinestartdate():hi.getOfflinestartdate().replaceAll("[^0-9]", "");//岗前线下培训 开始时间
			String realitystartdate=hi.getRealitystartdate()==null?hi.getRealitystartdate():hi.getRealitystartdate().replaceAll("[^0-9]", "");//实操见习培训 开始时间
			
			
			
			if(realitystartdate!=null&&realitystartdate.length()>0){
				hi.setTeachmsgstr("实操见习培训");
			}else if(offlinestartdate!=null&&offlinestartdate.length()>0){
				hi.setTeachmsgstr("岗前线下培训");
			}else if(onlinestartdate!=null&&onlinestartdate.length()>0){
				hi.setTeachmsgstr("岗前线上培训");
			}else{
				hi.setTeachmsgstr("理论见习培训");
			}
			
			rList.add(hi);
		}
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", rList);
		return returnMap;
	}
    
    
    /**
     * 导入汇思，天坤的数据
     */
    @Override
    public String saveHumanresourceHSTK(List<File> lst_import_excel) throws Exception {
    	String rcvmsg = null;
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
            
            //      汇思的 表头 //序号    城市     员工编号	姓名	人员类别	所属机构	一级部门	二级部门	三级部门	门店	汇报上级	职位	职级	外包/实习生入职日期	正式员工入职日期	合同签订情况			身份证号码	性别	生日	民族	学历信息			户籍	婚姻状况	联系电话				//紧急联系人姓名	紧急联系人电话	与紧急联系人关系	招聘渠道	推荐人姓名
            Map<String, Object> employMaphs = new HashMap<String, Object>();
            String[] hstitles ={"城市","员工编号","姓名","人员类别","所属机构","一级部门","二级部门","三级部门","门店","汇报上级","职位","职级","实习生入职日期","正式员工入职日期","开始日期","终止日期","签订次数","身份证号码","性别","生日","民族","学历","毕业院校","专业","户籍","婚姻状况","本人电话","紧急联系人姓名","紧急联系人电话","与紧急联系人关系","招聘渠道","推荐人"};
            
            Map<String, Integer> maps = new HashMap<String, Integer>();
            String deptName = "";
            //只读第一个sheet页
            sheet_data = wb_excel.getSheetAt(0);
            int ret = sheet_data.getPhysicalNumberOfRows();
            DistCityCodeManager distCityCodeManager = (DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");
            //城市set
            Set<String> citysets = new HashSet<String>();
            //根据人员类别将EXCEL排序
            Set<String> sets = new HashSet<String>();
            List<Row> sheetRowList = new ArrayList<Row>();
            for(int nRowIndex = 0;nRowIndex < sheet_data.getPhysicalNumberOfRows();nRowIndex++){
            	Row row_human = sheet_data.getRow(nRowIndex);
            	 if(row_human == null){
 	            	rcvmsg="导入文件格式不正确！导入失败！行号："+(nRowIndex+1);
                 	return rcvmsg;
 	            }
            	if(row_human == null){
	            	continue;
	            }
            	if(maps.size() < 32){
                	int nCellSize = row_human.getPhysicalNumberOfCells();
                	for(int nCellIndex = 0;nCellIndex < nCellSize ;nCellIndex ++){
                		String str_value = getCellValue(row_human.getCell(nCellIndex));
                		if(str_value!=null&&str_value.equals("编制")){
                			str_value="人员类别";
                		}
                		if(str_value!=null&&str_value.equals("联系电话")){
                			str_value="本人电话";
                		}
                		String str_title = isHave(hstitles,str_value);
                		if(str_title != null){
                			maps.put(str_title, nCellIndex);
                		}
                	}
                	if(maps.size() >= 32){
                		maps.put("nStartRow", nRowIndex);
                	}
                }
	            
	            if(nRowIndex==1&&maps.size()<33){
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
                
                String citySelect = getMapValue(row_human, maps, "城市");
                if(citySelect!=null&&citySelect.length()>0){
                	citysets.add(citySelect.trim());
                }else{
                	rcvmsg="城市不能为空！导入失败！行号："+(nRowIndex+1);
                	return rcvmsg;
                }
            	//PropertiesValueUtil proValueUtil = new PropertiesValueUtil("classpath:conf/citycode.properties");
            	DistCityCode cityCode = distCityCodeManager.queryDistCityCodeByName(citySelect);
            	//String cityCode = proValueUtil.getStringValue(citySelect);
                
            	if(cityCode==null||cityCode.getCitycode()==""){
            		rcvmsg="不存在的城市！ 导入失败！行号："+(nRowIndex+1);
                	break;
            	}
            	
                String employee_no = getMapValue(row_human, maps, "员工编号");
                if(employee_no!=null&&employee_no.length()>0){// 如果有员工编号  不导入
                	rcvmsg="存在员工编号！ 导入失败！行号："+(nRowIndex+1);
                	return rcvmsg;
                }
                String name = getMapValue(row_human, maps, "姓名");
                if(name==null||name.equals("")){
                	rcvmsg="姓名不能为空！导入失败！行号："+(nRowIndex+1);
                	return rcvmsg;
                }
                //身份证号码
                String cardnumber = getStringMapValue(row_human, maps, "身份证号码");
                if(cardnumber==null||cardnumber.equals("")){
                	rcvmsg="身份证号为空！ 导入失败！行号："+(nRowIndex+1);
            	}
                
                String storename = getMapValue(row_human, maps, "门店");
                if(storename!=null&&storename.length()>0){//如果有门店 不导入
                	rcvmsg="存在门店！导入失败！行号："+(nRowIndex+1);
                	return rcvmsg;
                }
                String authorizedtype = getCellValue(row_human.getCell(maps.get("人员类别")));
                if(authorizedtype!=null&&authorizedtype.length()>1){
                	if(authorizedtype.substring(0,2).equals("正式")){
                		rcvmsg="人员类别格式错误！ 导入失败！行号："+(nRowIndex+1);
                		return rcvmsg;
                	}
                	if(authorizedtype.substring(0,2).equals("实习")){
                		rcvmsg="人员类别格式错误！ 导入失败！行号："+(nRowIndex+1);
                		return rcvmsg;
                	}
                }else{
                	rcvmsg="人员类别错误！导入失败！行号："+(nRowIndex+1);
                	return rcvmsg;
                }
                if(authorizedtype!=null&&authorizedtype.length()>0){
                	sets.add(authorizedtype!=null&&authorizedtype.length()>1?authorizedtype.substring(0,2):"");
                }else{
                	rcvmsg="人员类别为空！导入失败！行号："+(nRowIndex+1);
                	return rcvmsg;
                }
                
                String professnallevel = getMapValue(row_human, maps, "职级");
                if(professnallevel!=null&&professnallevel.length()>0){
                	if(!"一级二级三级四级五级".contains(professnallevel)){
                    	rcvmsg="职级格式错误！导入失败！行号："+(nRowIndex+1)+" <br />参考值：一级、二级、三级、四级、五级";
                    	return rcvmsg;
                    }
                }else{
                	rcvmsg="职级格式错误！导入失败！行号："+(nRowIndex+1)+" <br />参考值：一级、二级、三级、四级、五级";
                	return rcvmsg;
                }
                sheetRowList.add(row_human);
            }
            
            
            if(citysets!=null&&citysets.size()>0){
            	
            	for(String cityname : citysets){
            		List<Row> citySheetRowList = new ArrayList<Row>();
            		for(Row row:sheetRowList){
            			String citySelect = getMapValue(row, maps, "城市");
    		    		if(cityname.equals(citySelect)){
    		    			citySheetRowList.add(row);
    		    		}
    		    	}
            		rcvmsg = buildcityhumanlist(rcvmsg, maps, sets, citySheetRowList);
            	}
            	
            }else{
            	rcvmsg="不存在城市！";
            	return rcvmsg;
            }
            
        }
        return rcvmsg;
    }

	private String buildcityhumanlist(String rcvmsg, Map<String, Integer> maps,
			Set<String> sets, List<Row> sheetRowList) {
		List<Humanresources> humanList = new ArrayList<Humanresources>();
		//开始处理一个城市 
		List<Row> totalRows = new ArrayList<Row>();
		if(sets.size()>0){
			for(String setval:sets){
				List<Row> tmpRows = new ArrayList<Row>();
		    	for(Row row:sheetRowList){
		    		String authorizedtype = getCellValue(row.getCell(maps.get("人员类别")));
		    		if(setval.equals(authorizedtype.substring(0,2))){
		    			tmpRows.add(row);
		    		}
		    	}
		    	totalRows.addAll(tmpRows);
		    }
		}
		
		HumanresourcesDao humanresourcesDao = (HumanresourcesDao)SpringHelper.getBean(HumanresourcesDao.class.getName());
		DistCityCodeManager distCityCodeManager = (DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");
		
		int hsemp_count=1;//人数记数器   用于生成人员编号
		String maxEmployeeNo = null;
		String tmpCompanyName = null; //存临时公司
		String changeCompany="";
		boolean isChangeCompany=false;//是否一个excel中有多个不同的外包公司 
		for(int nRowIndex = 0;nRowIndex < totalRows.size();nRowIndex++){
		    Row row_human = totalRows.get(nRowIndex);
		    int errorrowid = row_human.getRowNum();
		   
		    Humanresources obj_humanresources = new Humanresources();
		    //String[] hstitles ={"城市","员工编号","姓名","人员类别","所属机构","一级部门","二级部门","三级部门",
		    //"门店","汇报上级","职位","职级","实习生入职日期","正式员工入职日期","合同签订情况","身份证号码","性别","生日",
		    //"民族","学历信息","户籍","婚姻状况","联系电话"};
		    //"城市"
		    String citySelect = getMapValue(row_human, maps, "城市");
		    obj_humanresources.setCitySelect(citySelect);
		    //"员工编号"
		    String employee_no = getMapValue(row_human, maps, "员工编号");
		    obj_humanresources.setEmployee_no(employee_no);
		    //姓名
		    String name = getMapValue(row_human, maps, "姓名");
		    obj_humanresources.setName(name);
		    //人员类别
		    String authorizedtype = getCellValue(row_human.getCell(maps.get("人员类别")));
		    obj_humanresources.setAuthorizedtype(authorizedtype);
		   
		    //"所属机构"
		    String orgname = getMapValue(row_human, maps, "所属机构");
		    obj_humanresources.setOrgname(orgname);
		    //一级部门
		    String deptlevel1 = getMapValue(row_human, maps, "一级部门");
		    obj_humanresources.setDeptlevel1(deptlevel1);
		    //二级部门
		    String deptlevel2 = getMapValue(row_human, maps, "二级部门");
		    obj_humanresources.setDeptlevel2(deptlevel2);
		    //三级部门
		    String deptlevel3 = getMapValue(row_human, maps, "三级部门");
		    obj_humanresources.setDeptlevel3(deptlevel3);
		    //汇报上级
		    String reporthigher = getMapValue(row_human, maps, "汇报上级");
		    obj_humanresources.setReporthigher(reporthigher);
		    //职位
		    String zw = getMapValue(row_human, maps, "职位");
		    obj_humanresources.setZw(zw);
		    //职级
		    String professnallevel = getMapValue(row_human, maps, "职级");
		    obj_humanresources.setProfessnallevel(professnallevel);
		    //实习生入职日期
		    String interndate = getMapValue(row_human, maps, "实习生入职日期");
		    obj_humanresources.setInterndate(interndate);
		    //正式员工入职日期
		    String topostdate = getMapValue(row_human, maps, "正式员工入职日期");
		    obj_humanresources.setTopostdate(topostdate);
		    //合同开始日期
		    String contractdatestart = getMapValue(row_human, maps, "开始日期"); //合同开始日期
		    obj_humanresources.setContractdatestart(contractdatestart);
		    //合同结束日期
		    String contractdateend = getMapValue(row_human, maps, "终止日期");  //合同结束日期
		    obj_humanresources.setContractdateend(contractdateend);
		    //签订次数
		    String signcount = getMapValue(row_human, maps, "签订次数");  //签订次数
		    obj_humanresources.setSigncount(signcount);
		    
		    //String employee_no = getMapValue(row_human, maps, "员工编号");
		    //身份证号码
		    String cardnumber = getStringMapValue(row_human, maps, "身份证号码");
		    obj_humanresources.setCardnumber(cardnumber);
		    
		    //性别"
		    String sex = getMapValue(row_human, maps, "性别");
		    obj_humanresources.setSex(sex);
		    //,"生日
		    String birthday = getMapValue(row_human, maps, "生日");
		    obj_humanresources.setBirthday(birthday);
		    //民族",
		    String nation = getMapValue(row_human, maps, "民族");
		    obj_humanresources.setNation(nation);
		    //"学历信息"
		    String education = getMapValue(row_human, maps, "学历");
		    obj_humanresources.setEducation(education);
		    //毕业院校
		    String school = getMapValue(row_human, maps, "毕业院校");
		    obj_humanresources.setSchool(school);
		    //专业
		    String profession = getMapValue(row_human, maps, "专业");
		    obj_humanresources.setProfession(profession);
		    
		    //"户籍"
		    String censusregister = getMapValue(row_human, maps, "户籍");
		    obj_humanresources.setCensusregister(censusregister);
		    //婚姻状况
		    String marriage = getMapValue(row_human, maps, "婚姻状况");
		    obj_humanresources.setMarriage(marriage);
		    //"联系电话"
		    String phone = getStringMapValue(row_human, maps, "本人电话");
		    obj_humanresources.setPhone(phone);

		    //紧急联系人姓名
		    String relationname = getMapValue(row_human, maps, "紧急联系人姓名");
		    obj_humanresources.setRelationname(relationname);
		    //紧急联系人电话
		    String tel = getStringMapValue(row_human, maps, "紧急联系人电话");
		    obj_humanresources.setTel(tel);
		    //与紧急联系人关系
		    String relationtype = getMapValue(row_human, maps, "与紧急联系人关系");
		    obj_humanresources.setRelationtype(relationtype);
		    //招聘渠道
		    String jobchannel = getMapValue(row_human, maps, "招聘渠道");
		    obj_humanresources.setJobchannel(jobchannel);
		    //推荐人姓名
		    String offername = getMapValue(row_human, maps, "推荐人姓名");
		    obj_humanresources.setOffername(offername);
		    
		    
		    //自动处理员工编号 
		    //查询数据库汇思、天坤最大值
			//PropertiesValueUtil proValueUtil = new PropertiesValueUtil("classpath:conf/citycode.properties");
			DistCityCode cityCodeObj = distCityCodeManager.queryDistCityCodeByName(citySelect);
			String cityCode = "";
			if(cityCodeObj!=null&&cityCodeObj.getCitycode()!=null){
				cityCode = cityCodeObj.getCitycode();
			}else{
				rcvmsg="不存在的城市！ 导入失败！行号："+(errorrowid+1);
			}
			
			//String cityCode = proValueUtil.getStringValue(citySelect);
			
			OuterCompanyManager outerCompanyManager = (OuterCompanyManager) SpringHelper.getBean("outerCompanyManager");
			List<OuterCompany> outList = outerCompanyManager.queryOutCompanys();
			
			//如果一个excel中存在两个外包公司的人处理
			if(tmpCompanyName==null){
				tmpCompanyName=authorizedtype.substring(0,2);
			}else{
				if(!tmpCompanyName.equals(authorizedtype.substring(0,2))){
					tmpCompanyName=authorizedtype.substring(0,2);
					maxEmployeeNo=null;
				}
			}
			
		    if(authorizedtype.contains("汇思")){
		    	String max_hsemployee_no="";
		    	if(maxEmployeeNo!=null&&maxEmployeeNo.length()>0){
		    		max_hsemployee_no=maxEmployeeNo;
		    	}else{
		    		max_hsemployee_no = humanresourcesDao.queryMaxEmpNo("HS"+cityCode);
		    	}
		        String max_hsret = "";//汇思最大值
		        int numCount = 0;
		        if(max_hsemployee_no!=null){
		        	//取到汇思后四位的值
		        	numCount = findNumCount(max_hsemployee_no);
		        	max_hsret = max_hsemployee_no.substring(max_hsemployee_no.length()-numCount, max_hsemployee_no.length());
		        }
		        String new_employee_no =(Integer.parseInt(max_hsret)+1)+"";
		        if(numCount==0){
		        	 numCount=5;//如果初始化导入 默认5位。
		        }
		        //补零操作
		        for(int i=0;i<5;i++){
					if(new_employee_no.length()<numCount){
						new_employee_no="0"+new_employee_no;
					}else{
						break;
					}
				}
		    	employee_no="GAHS"+cityCode+new_employee_no;
		    	maxEmployeeNo=employee_no;
		   }else if(authorizedtype.contains("天坤")){
		    	String max_tkemployee_no="";
		    	if(maxEmployeeNo!=null&&maxEmployeeNo.length()>0){
		    		max_tkemployee_no=maxEmployeeNo;
		    	}else{
		    		max_tkemployee_no = humanresourcesDao.queryMaxEmpNo("TK"+cityCode);
		    	}
		    	
		        String max_tkret = "";//天坤最大值
		        if(max_tkemployee_no!=null){
		        	//取到汇思后五位的值
		        	max_tkret = max_tkemployee_no.substring(max_tkemployee_no.length()-5, max_tkemployee_no.length());
		        }
		        String new_employee_no =(Integer.parseInt(max_tkret)+1)+"";
		        //补零操作
		        for(int i=0;i<5;i++){
					if(new_employee_no.length()<5){
						new_employee_no="0"+new_employee_no;
					}else{
						break;
					}
				}
		    	employee_no="GATK"+cityCode+new_employee_no;
		    	maxEmployeeNo=employee_no;
		    }else{
		    	//其它公司
		    	if(outList!=null&&outList.size()>0){
		    		boolean iscontain = true;
		    		for(OuterCompany oc:outList){
		    			String compcode = oc.getCompanycode();
		    			String compname = oc.getCompanyname();
		    			if(authorizedtype.contains(compname)){
		    				String max_othemployee_no="";
		    				if(maxEmployeeNo!=null&&maxEmployeeNo.length()>0){
		    					max_othemployee_no=maxEmployeeNo;
		    				}else{
		    					max_othemployee_no = humanresourcesDao.queryMaxEmpNo(compcode+cityCode);
		    				}
		                    String max_otherret = "";//天坤最大值
		                    if(max_othemployee_no!=null){
		                    	//取到后五位的值
		                    	max_otherret = max_othemployee_no.substring(max_othemployee_no.length()-5, max_othemployee_no.length());
		                    }
		                    if(max_otherret==""){
		                    	max_otherret="0";
		                    }
		                    String new_employee_no =(Integer.parseInt(max_otherret)+1)+"";
		                    //补零操作
		                    for(int i=0;i<5;i++){
		            			if(new_employee_no.length()<5){
		            				new_employee_no="0"+new_employee_no;
		            			}else{
		            				break;
		            			}
		            		}
		                	employee_no="GA"+compcode+cityCode+new_employee_no;
		                	maxEmployeeNo = employee_no;
		                	iscontain=true;
		                	break;
		    			}else{
		    				iscontain=false;
		    			}
		    		}
		    		
		    		if(!iscontain){
		    			rcvmsg="请检查人员类别！导入失败！行号："+(errorrowid+1);
		    		}
		    	}else{
		    		rcvmsg="不存在的人员类别！导入失败！行号："+(errorrowid+1);
		    	}
		    }
		    obj_humanresources.setEmployee_no(employee_no);
		    
		    //preObject(obj_humanresources);
		    obj_humanresources.setStatus(0);
		    obj_humanresources.setVersion(new Long(0));
		    obj_humanresources.setHumanstatus(Long.parseLong("0"));
		    if(obj_humanresources.getZw()!=null&&obj_humanresources.getZw().equals("市场专员")){
		    	obj_humanresources.setHumanstatus(Long.parseLong("1"));
		    }
		    //saveObject(obj_humanresources);
		    humanList.add(obj_humanresources);
		    
		    hsemp_count++;
		}
		
		if(rcvmsg!=null&&rcvmsg.length()>0){
			return rcvmsg;
		}else{
			StringBuffer sbf = new StringBuffer();
			StringBuffer cardnos = new StringBuffer();
			if(humanList!=null&&humanList.size()>0){
				for(Humanresources h:humanList){
					sbf.append("'");
					sbf.append(h.getEmployee_no());
					sbf.append("',");
					
					cardnos.append("'");
					cardnos.append(h.getCardnumber());
					cardnos.append("',");
				}
				
				IFilter repFilter =FilterFactory.getSimpleFilter(" humanstatus in(0,1) and cardnumber in("+cardnos.toString().substring(0,cardnos.toString().length()-1)+")");
		    	List<?> rep_list = this.getList(repFilter);
				if(rep_list!=null&&rep_list.size()>0){
					Humanresources repHuman = (Humanresources) rep_list.get(0);
					rcvmsg="存在相同数据！<br> 姓名:"+repHuman.getName()+" <br> 身份证:"+repHuman.getCardnumber();
		    		return rcvmsg;
				}
		    	
				IFilter iFilter =FilterFactory.getSimpleFilter(" employee_no in("+sbf.toString().substring(0,sbf.toString().length()-1)+")");
		    	List<?> lst_list = this.getList(iFilter);
		    	if(lst_list!=null&&lst_list.size()>0){
		    		rcvmsg="存在冲突 ，导入失败！";
		    		return rcvmsg;
		    	}else{
		    		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		    		for(Humanresources h:humanList){
		        		preObject(h);
		        		saveObject(h);
		        		
		        		
		        		if(h.getZw()!=null&&h.getZw().equals("市场专员")){
	        				User user = new User();
	        				user.setName(h.getName());
	        				user.setCode(ChineseToEnglish.getPingYin(h.getName()));
	        				user.setDisabledFlag(1);
	        				user.setDoctype(0);
	        				user.setEmail("123@123.com");
	        				user.setEmployeeId(h.getEmployee_no());
	        				user.setEnablestate(1);
	        				user.setPassword("e10adc3949ba59abbe56e057f20f883e"); //123456
	        				user.setPk_org(Long.parseLong("40284"));
	        				user.setMobilephone(h.getPhone());
	        				UserGroupManager u = (UserGroupManager)SpringHelper.getBean("userGroupManager");
	        				String groupzw = h.getZw();
	        				if(groupzw.equals("服务专员")||groupzw.equals("综合专员")||groupzw.equals("事务管理")){
	        					groupzw = "综合管理";
	        				}
	            	       
	            			UserGroup userGroup = null;
	            			if(groupzw!=null&&groupzw.equals("综合管理")){
	            				IFilter iFi=FilterFactory.getSimpleFilter("code","ZHGLY");
	            		        List<?> lst_groupList = u.getList(iFi);
	            		        userGroup = (UserGroup) lst_groupList.get(0);
	            			}else{
	            		        IFilter iFi =FilterFactory.getSimpleFilter(" name like '%"+groupzw+"%'");
	            		        List<?> lst_groupList = u.getList(iFi);
	            		        userGroup = (UserGroup) lst_groupList.get(0);
	            			}
	            			
	            	        user.setZw(h.getZw());
	            	        user.setUsergroup(userGroup);
	        				user.setLogicDel(0);
	        				preSaveObject(user);
	        				userManager.saveObject(user);
	        			}
		        		
		        	}
		    	}
			}else{
				return null;
			}
		}
		return rcvmsg;
	}
    
    
    
   
    /**
     * 导入培训模板信息
     */
    @Override
    public void importHumanresourceTeach(List<File> lst_import_excel) throws Exception {
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
            
            //临时保存国安侠姓名，ID
            Map<String, Object> employMap = new HashMap<String, Object>();
            
            Map<String, Integer> maps = new HashMap<String, Integer>();
            String deptName = "";
            sheet_data = wb_excel.getSheetAt(0);
            int ret = sheet_data.getPhysicalNumberOfRows();
            for(int nRowIndex = 2;nRowIndex < sheet_data.getPhysicalNumberOfRows();nRowIndex++){
                Row row_human = sheet_data.getRow(nRowIndex);
                if(row_human == null){
                    continue;
                }
                
                String employee_no = getCellValue(row_human.getCell(1)); //工号
                String name=getCellValue(row_human.getCell(2));  //姓名
                String zw=getCellValue(row_human.getCell(3));  //岗位
                String authorizedtype=getCellValue(row_human.getCell(4));  //编制
                String store_name=getCellValue(row_human.getCell(5));  //所在门店
                String phone=getCellValue(row_human.getCell(6));  //手机号
                
                String theorystartdate=getCellValue(row_human.getCell(8));  //理论：开始时间
                String theoryenddate=getCellValue(row_human.getCell(9));  //理论：结束时间
                String theorystorename=getCellValue(row_human.getCell(10)); //理论：见习门店
                
                String onlinestartdate=getCellValue(row_human.getCell(11));  //岗前线上：开始时间
                String onlinescore=getCellValue(row_human.getCell(12));  //岗前线上：成绩
                String onlinedate=getCellValue(row_human.getCell(13));  //岗前线上：时长
                
                String offlinestartdate=getCellValue(row_human.getCell(14));  //岗前线下：开始时间
                String offlinescore=getCellValue(row_human.getCell(15));  //岗前线下：成绩
                String offlinedate=getCellValue(row_human.getCell(16));  //岗前线下：时长
                		
                String realitystartdate=getCellValue(row_human.getCell(17));  //实操：开始时间
                String realityenddate=getCellValue(row_human.getCell(18));  //实操：结束时间
                String realitystorename=getCellValue(row_human.getCell(19));  //实操：见习门店
                String realityscore=getCellValue(row_human.getCell(20));  //实操：成绩
                
                //根据员工编号，姓名查询出该员工信息，然后赋值 
                //查询状态为已分配门店的员工
                List<?> lst_employee_object = this.getList(FilterFactory.getSimpleFilter("employee_no",employee_no));
                Humanresources humanresources;
                if(lst_employee_object != null && lst_employee_object.size() > 0){
                	humanresources = (Humanresources)lst_employee_object.get(0);
                	humanresources.setAuthorizedtype(authorizedtype);
                	
                	//导入的时候 判断 导入的 编制状态 是否为 淘汰或是离职
                	if(authorizedtype!=null){
                		UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
            			User user = userManager.findEmployeeByEmployeeNo(humanresources.getEmployee_no());
                		if(authorizedtype.equals("淘汰")||authorizedtype.equals("离职")){
                			//查询系统表。如果存在 则更改状态，不允许登录 如果不存在。则不做任何操作。
                			if(user!=null&&user.getEmployeeId()!=null){
                				user.setDisabledFlag(0);
                    			preSaveObject(user);
                    			userManager.saveObject(user);
                			}
                		}else{
                			//查询系统表。如果存在 则更改状态，允许登录 如果不存在。则不做任何操作。
                			if(user!=null&&user.getEmployeeId()!=null){
                				user.setDisabledFlag(1);
                    			preSaveObject(user);
                    			userManager.saveObject(user);
                			}
                		}
                	}
                	
                	humanresources.setTheorystartdate(theorystartdate);
                	humanresources.setTheoryenddate(theoryenddate);
                	humanresources.setTheorystorename(theorystorename);
                	
                	humanresources.setOnlinestartdate(onlinestartdate);
                	if(onlinescore!=null&&onlinescore.length()>0){
                		humanresources.setOnlinescore(Double.parseDouble(onlinescore));
                	}else{
                		humanresources.setOnlinescore(Double.parseDouble("0"));
                	}
                	
                	humanresources.setOnlinedate(onlinedate);
                	
                	humanresources.setOfflinestartdate(offlinestartdate);
                	if(offlinescore!=null&&offlinescore.length()>0){
                		humanresources.setOfflinescore(Double.parseDouble(offlinescore));	
                	}else{
                		humanresources.setOfflinescore(Double.parseDouble("0"));
                	}
                	
                	humanresources.setOfflinedate(offlinedate);
                	
                	humanresources.setRealitystartdate(realitystartdate);
                	humanresources.setRealityenddate(realityenddate);
                	humanresources.setRealitystorename(realitystorename);
                	if(realityscore!=null&&realityscore.length()>0){
                		humanresources.setRealityscore(Double.parseDouble(realityscore));
                	}else{
                		humanresources.setRealityscore(Double.parseDouble("0"));
                	}
                	
                	preSaveObject(humanresources);
                	saveObject(humanresources);
                }
                
            }
            is_excel.close();
        }
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
    
    
    
    
    
    private String isHave(String[] titiles,String str_value){
    	for(String titleString : titiles){
    		if("紧急联系人姓名".equals(str_value)){
    			return str_value;
    		}else if("推荐人姓名".equals(str_value)){
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
    
    
    /**
     * 保存店长信息
     */
    @Override
    public void saveStoreKeeper(Humanresources humanresources){
    	//自动生成员工编号
    	//取得最大员工编号
    	HumanresourcesDao humanresourcesDao = (HumanresourcesDao)SpringHelper.getBean(HumanresourcesDao.class.getName());
        
    	String dzMaxNo = humanresourcesDao.queryMaxStoreKeeperNo();
    	String max_dzret = "0";//店长最大值
        if(dzMaxNo!=null){
        	//取到店长后四位的值
        	max_dzret = dzMaxNo.substring(dzMaxNo.length()-4, dzMaxNo.length());
        }
        String new_dz_no =(Integer.parseInt(max_dzret)+1)+"";
        //补零操作
        for(int i=0;i<4;i++){
			if(new_dz_no.length()<4){
				new_dz_no="0"+new_dz_no;
			}else{
				break;
			}
		}
    	humanresources.setEmployee_no("DZ"+new_dz_no);
    	
    	//设置值为类别为正式
    	humanresources.setHumanstatus(Long.parseLong("1"));
    	preSaveObject(humanresources);
    	saveObject(humanresources);
    }
    
    
    
    
    /**
     * 根据门店 查询该门店所有在职员工的集合
     */
    @Override
    public List<Humanresources> queryHumanresourceListByStoreId(Long store_id){
    	HumanresourcesManager hManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
        IFilter iFilter =FilterFactory.getSimpleFilter(" humanstatus=1 and store_id="+store_id);
        List<Humanresources> lst_humanList = (List<Humanresources>)hManager.getList(iFilter);
    	return lst_humanList;
    }
    
    
    /**
     * 根据门店 查询该门店所有在职员工的集合(查国安侠)
     */
    @Override
    public List<Humanresources> queryHumanresourceGAXListByStoreId(Long store_id){
    	HumanresourcesManager hManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
        IFilter iFilter =FilterFactory.getSimpleFilter(" humanstatus=1 and zw='国安侠' and store_id="+store_id);
        List<Humanresources> lst_humanList = (List<Humanresources>)hManager.getList(iFilter);
        
        IFilter iFilterFdz =FilterFactory.getSimpleFilter(" humanstatus=1 and zw='副店长' and remark='国安侠' and store_id="+store_id);
        List<Humanresources> fdz = (List<Humanresources>)hManager.getList(iFilterFdz);
        if(fdz!=null&&fdz.size()>0){
        	lst_humanList.add(fdz.get(0));
        }
    	return lst_humanList;
    }
    
    
    /**
     * 根据门店 查询该门店所有离职员工的集合（填报的日期）
     */
    @Override
    public List<Humanresources> queryHumanresourceListByStoreIdLz(Long store_id,String month){
    	HumanresourcesManager hManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
    	String minDay = getMinMonthDate(month+"-01");
    	String maxDay = getMaxMonthDate(month+"-01");
    	String datecondition = " DATE_FORMAT(leavedate,'%Y-%m-%d') >= '"+minDay+"' AND DATE_FORMAT(leavedate,'%Y-%m-%d')<= '"+maxDay+"' ";
        IFilter iFilter =FilterFactory.getSimpleFilter(" humanstatus=2 and store_id="+store_id+" and "+datecondition);
        List<Humanresources> lst_humanList = (List<Humanresources>)hManager.getList(iFilter);
    	return lst_humanList;
    }
    /**
     * 根据门店 查询该门店所有入职员工的集合（填报的日期）
     */
    @Override
    public List<Humanresources> queryHumanresourceListByStoreIdRz(Long store_id,String month){
    	HumanresourcesManager hManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
    	String minDay = getMinMonthDate(month+"-01");
    	String maxDay = getMaxMonthDate(month+"-01");
    	String datecondition = " DATE_FORMAT(topostdate,'%Y-%m-%d') >= '"+minDay+"' AND DATE_FORMAT(topostdate,'%Y-%m-%d')<= '"+maxDay+"' ";
        IFilter iFilter =FilterFactory.getSimpleFilter(" humanstatus=1 and store_id="+store_id+" and "+datecondition);
        List<Humanresources> lst_humanList = (List<Humanresources>)hManager.getList(iFilter);
    	return lst_humanList;
    }
    
    
    
    
    public int findNumCount(String employee_no){
    	Matcher match = Pattern.compile("\\d").matcher(employee_no);
		int count = 0;
        while(match.find()){
        	 count++;
        }
        return count;
    }
    
    
    
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	 //取得当前月第一天
	public static String getMinMonthDate(String date) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(dateFormat.parse(date));
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			return dateFormat.format(calendar.getTime());
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 取得当前月最后一天
	public static String getMaxMonthDate(String date) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(dateFormat.parse(date));
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			return dateFormat.format(calendar.getTime());
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	@Override
	public int findEmployeeInfoByNo(String employee_no){
		HumanresourcesManager hManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
		IFilter iFilter =FilterFactory.getSimpleFilter("employee_no='"+employee_no+"' AND humanstatus=1 ");
        List<Humanresources> lst_humanList = (List<Humanresources>)hManager.getList(iFilter); 
        if(lst_humanList!=null&&lst_humanList.size()>0){
        	return lst_humanList.size();
        }else{
        	return 0;
        }
	}
	
	
	@Override
	public Map<String, Object> queryEmployeeInfoByNo(String employee_no){
		Map<String, Object> map = new HashMap<String, Object>();
		HumanresourcesManager hManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
		OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
		CustomerManager customerManager = (CustomerManager) SpringHelper.getBean("customerManager");
		/////////取得基本信息开始//////////
		IFilter iFilter =FilterFactory.getSimpleFilter("employee_no='"+employee_no+"' AND humanstatus=1 ");
        List<Humanresources> lst_humanList = (List<Humanresources>)hManager.getList(iFilter); 
        Humanresources humanresources = new Humanresources();
        int month = 0;
        if(lst_humanList!=null&&lst_humanList.size()>0){
        	humanresources = lst_humanList.get(0);
        	//计算工作时间 
        	if(humanresources.getTopostdate()!=null&&humanresources.getTopostdate().length()>0){
        		String topost = humanresources.getTopostdate();
        		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        		SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd");
        		try {
        			month = getMonthDiff(new Date(), sdf.parse(topost));
				} catch (ParseException e) {
					try {
						month = getMonthDiff(new Date(), df.parse(topost));
					} catch (ParseException e1) {
						month = 999;
					}
					
				}
        	}
        }
        if(month==999){
        	humanresources.setRemark("暂无");
        }else{
        	int ret = month/12;
        	if(ret==0){
        		humanresources.setRemark("一年以下");
        	}else{
        		humanresources.setRemark(ret+"年以上");
        	}
        }
        /////////取得基本信息结束//////////
        
        
        /////////取得订单,快递,拜访记录,单体画像 总数//////////
       // PlatformEmployeeManager platformEmployeeManager = (PlatformEmployeeManager)SpringHelper.getBean("platformEmployeeManager");
       // OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");
        ExpressDao expressDao = (ExpressDao) SpringHelper.getBean(ExpressDao.class.getName());
        RelationDao relationDao = (RelationDao)SpringHelper.getBean(RelationDao.class.getName());
        //PlatformEmployee platformEmployee = platformEmployeeManager.findPlatformEmployeeByEmployeeNo(employee_no);
        //int ordercount = orderDao.gettotalOrderCount(null, platformEmployee.getId());
        int expresscount = expressDao.gettotalExpressCount(employee_no);
        //int relationcount = expressDao.getTotalRelationCount(employee_no);
        int relationcount = relationDao.getRelationCount(employee_no);
        Object customerCount = customerManager.getCustomerCount(employee_no).get("total");
        
        map.put("relationcount", relationcount);
        map.put("expresscount", expresscount);
        //map.put("ordercount", ordercount);
        map.put("human", humanresources);
        map.put("customercount", customerCount);
		return map;
	}
	
	public static int getMonthDiff(Date d1, Date d2) {  
        Calendar c1 = Calendar.getInstance();  
        Calendar c2 = Calendar.getInstance();  
        c1.setTime(d1);  
        c2.setTime(d2);  
        if(c1.getTimeInMillis() < c2.getTimeInMillis()) return 0;  
        int year1 = c1.get(Calendar.YEAR);  
        int year2 = c2.get(Calendar.YEAR);  
        int month1 = c1.get(Calendar.MONTH);  
        int month2 = c2.get(Calendar.MONTH);  
        int day1 = c1.get(Calendar.DAY_OF_MONTH);  
        int day2 = c2.get(Calendar.DAY_OF_MONTH);  
        // 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30  
        int yearInterval = year1 - year2;  
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数  
        if(month1 < month2 || month1 == month2 && day1 < day2) yearInterval --;  
        // 获取月数差值  
        int monthInterval =  (month1 + 12) - month2  ;  
        if(day1 < day2) monthInterval --;  
        monthInterval %= 12;  
        return yearInterval * 12 + monthInterval;  
    }
	
	/**
	 * 根据门店名ID 更改门店名字 
	 * @param store_id
	 * @param store_name
	 */
	@Override
	public void updateStoreNameById(Long store_id,String store_name){
		if(store_id!=null){
			IFilter repFilter =FilterFactory.getSimpleFilter("store_id",store_id);
	    	List<?> rep_list = this.getList(repFilter);
			if(rep_list!=null&&rep_list.size()>0){
				for(int i=0;i<rep_list.size();i++){
					Humanresources repHuman = (Humanresources) rep_list.get(i);
					repHuman.setStorename(store_name);
					preSaveObject(repHuman);
        			saveObject(repHuman);
				}
			}
		}
	}
	
	
	
	@Override
	public String removeHumanresourceById(Long human_id){
		String rcv = null;
		HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
		Humanresources humanresources = (Humanresources) humanresourcesManager.getObject(human_id);
		
		if(humanresources!=null&&humanresources.getStore_id()!=null){
			rcv = "已分配门店，删除失败！  ";
			return rcv;
		}
		
		//插入记录表
    	HumanresourcesLogManager h = (HumanresourcesLogManager)SpringHelper.getBean("humanresourcesLogManager");
    	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
    	humanresources.setRemark(userManager.getCurrentUserDTO().getName()+"删除员工"+humanresources.getEmployee_no());
    	h.saveHumanresources(humanresources);
		
		humanresourcesManager.removeObject(humanresources);
		
		//判断此条 是否为当前登录人导入的 未分配门店的最大值 
		/*String employee_no_code = humanresources.getEmployee_no().replaceAll("\\d", "").trim();
		IFilter repFilter =FilterFactory.getSimpleFilter("humanstatus="+0).appendAnd(FilterFactory.getSimpleFilter(" create_user_id = "+humanresources.getCreate_user_id()).appendAnd(FilterFactory.getSimpleFilter(" employee_no like '"+employee_no_code+"%' ")));
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("employee_no",ISort.DESC));
		fsp.setUserFilter(repFilter);
		
		List<?> rep_list = this.getList(fsp);
		Humanresources maxHumanresources = null;
		if(rep_list!=null&&rep_list.size()>0){
			maxHumanresources = (Humanresources) rep_list.get(0);
		}
		if(maxHumanresources!=null&&maxHumanresources.getEmployee_no().equals(humanresources.getEmployee_no())){
			//删除操作
			humanresourcesManager.removeObject(humanresources);
		}else{
			rcv = "员工编号不是最大值！ 删除失败！ ";
		}*/
		
		return rcv;
	}
	
	
	@Override
	public Humanresources getEmployeeInfoByEmployeeNo(String employee_no){
		HumanresourcesManager hManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
		IFilter iFilter =FilterFactory.getSimpleFilter("employee_no='"+employee_no+"' AND humanstatus=1 ");
        List<Humanresources> lst_humanList = (List<Humanresources>)hManager.getList(iFilter); 
        if(lst_humanList!=null&&lst_humanList.size()>0){
        	return lst_humanList.get(0);
        }else{
        	return null;
        }
	}
	
	@Override
	public Humanresources getEmployeeInfoByEmployeeNoExtend(String employee_no){
		HumanresourcesManager hManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
		IFilter iFilter =FilterFactory.getSimpleFilter("employee_no='"+employee_no+"' ");
        List<Humanresources> lst_humanList = (List<Humanresources>)hManager.getList(iFilter); 
        if(lst_humanList!=null&&lst_humanList.size()>0){
        	return lst_humanList.get(0);
        }else{
        	return null;
        }
	}
	
	
	/**
	 * 批量分配门店方法
	 * @param arr
	 * @return
	 */
	@Override
	public List<Humanresources> updateHumanresourcesMult(List<Map<String, Object>> arr,String storename){
		List<Humanresources> ret_array = null;
		Long store_id = null;
		StoreManager s = (StoreManager) SpringHelper.getBean("storeManager");
		HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
    	UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		UserGroupManager userGroupManager = (UserGroupManager)SpringHelper.getBean("userGroupManager");

		Store store = s.findStoreByName(storename);
    	if(store!=null){
    		store_id=store.getStore_id();
    	}
		
		if(arr!=null&&arr.size()>0&&store_id!=null){
			try {
				ret_array = new ArrayList<Humanresources>();
				for(Map<String, Object> srt:arr){
					Humanresources humanresources = (Humanresources) humanresourcesManager.getObject(Long.parseLong(srt.get("id").toString()));
					humanresources.setId(Long.parseLong(srt.get("id").toString()));
					humanresources.setStorename(srt.get("storename").toString());
					humanresources.setStore_id(store_id);
					humanresources.setHumanstatus(Long.parseLong("1"));
					
					preSaveObject(humanresources);
					humanresourcesManager.saveObject(humanresources);
					//ret_array.add(rt_scoreRecordTotal);
					//同时插入tb_bizbase_user表中数据  
					User u = userManager.findEmployeeByEmployeeNo(humanresources.getEmployee_no());
					User save_user = null;
					if(u!=null){
						save_user = u;
						save_user.setStore_id(store_id);
					}else{
						save_user = new User();
						save_user.setName(humanresources.getName());
						save_user.setCode(ChineseToEnglish.getPingYin(humanresources.getName()));
						save_user.setDisabledFlag(1);
						save_user.setDoctype(0);
						save_user.setEmail("123@123.com");
						save_user.setEmployeeId(humanresources.getEmployee_no());
						save_user.setEnablestate(1);
						save_user.setPassword("e10adc3949ba59abbe56e057f20f883e"); //123456
						save_user.setPk_org(Long.parseLong("40284"));
						save_user.setMobilephone(humanresources.getPhone());
						save_user.setPhone(humanresources.getPhone());
						
						save_user.setStore_id(store_id);
						
        				String groupzw = humanresources.getZw();
        				if(groupzw.equals("服务专员")||groupzw.equals("综合专员")||groupzw.equals("事务管理 ")){
        					groupzw = "综合管理";
        				}
            	       
            			UserGroup userGroup = null;
            			if(groupzw!=null&&groupzw.trim().equals("综合管理")){
            				IFilter iFilter =FilterFactory.getSimpleFilter("code","ZHGLY");
            		        List<?> lst_groupList = userGroupManager.getList(iFilter);
            		        userGroup = (UserGroup) lst_groupList.get(0);
            			}else{
            		        IFilter iFilter =FilterFactory.getSimpleFilter(" name like '%"+groupzw+"%'");
            		        List<?> lst_groupList = userGroupManager.getList(iFilter);
            		        userGroup = (UserGroup) lst_groupList.get(0);
            			}
            			
            			save_user.setZw(humanresources.getZw());
            			save_user.setUsergroup(userGroup);
            			save_user.setLogicDel(0);
        			}
					preSaveObject(save_user);
    				userManager.saveObject(save_user);
    				
    				
    				//批量分配门店后。同步数据
    				syncEmployee(humanresources);
    				
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret_array;
	}
	
	
	
	//根据城市查询所有的人员信息，进行备份操作 
	@Override
	public List<Humanresources> queryHumanresourceListByCity(String citySelect){
		HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
		IFilter iFilter =FilterFactory.getSimpleFilter("citySelect='"+citySelect+"'");
        List<Humanresources> lst_humanList = (List<Humanresources>)humanresourcesManager.getList(iFilter); 
        return lst_humanList;
	}
	
	
	
	//根据事业群组 查 询组下 所有的服务专员 
	@Override
	public Map<String, Object> queryHumanresourcesByCareerName(String careername){
		IFilter iFilter = FilterFactory.getSimpleFilter("career_group = '"+careername+"' and humanstatus=1 and zw = '服务专员' ");
		HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
		List<Humanresources> lst_human_list = (List<Humanresources>) humanresourcesManager.getList(iFilter);
		
		Map<String, Object> rt_map = new HashMap<String, Object>();
		
		rt_map.put("careerlist", lst_human_list);//所有的服务专员 
		
		String sb_city = "";
		//查询目前开了多少个城市 
		DistCityCodeManager distCityCodeManager = (DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");
		List<DistCityCode> lst_distCityCodes = (List<DistCityCode>) distCityCodeManager.getList();
		rt_map.put("citynameslist", lst_distCityCodes);//所有的城市 
		
		IFilter iFilter_store = FilterFactory.getSimpleFilter(" career_group='"+careername+"' AND zw='服务专员' AND humanstatus=1 GROUP BY store_id");
		List<Humanresources> lst_human_list_store = (List<Humanresources>) humanresourcesManager.getList(iFilter_store);
		String store_ids = "";
		if(lst_human_list_store!=null&&lst_human_list_store.size()>0){
			rt_map.put("storecount", lst_human_list_store.size());//所有的门店数 
			store_ids+=lst_human_list_store.get(0).getStore_id();
			for(int i=1;i<lst_human_list_store.size();i++){
				store_ids+=","+lst_human_list_store.get(i).getStore_id();
			}
		}
		
		if(store_ids!=""){
			IFilter iFilter_allemp = FilterFactory.getSimpleFilter("store_id in("+store_ids+") AND humanstatus = 1");
			List<Humanresources> lst_human_list_emp = (List<Humanresources>) humanresourcesManager.getList(iFilter_allemp);
			rt_map.put("employeesize", lst_human_list_emp.size());
		}else{
			rt_map.put("employeesize", 0);
		}
		
		
		
		
		
		
		return rt_map;
	}
	
	
	
	/**
     * 根据门店 查询该门店所有在职员工的集合(只查国安侠)公海订单分配用
     */
    @Override
    public List<Humanresources> queryHumanGAXListByStoreId(String storeno){
    	StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
    	IFilter iFilter =FilterFactory.getSimpleFilter(" storeno='"+storeno+"' ");
    	List<Store> rt_storeList = (List<Store>) storeManager.getList(iFilter);
    	Store store = null;
    	if(rt_storeList!=null&&rt_storeList.size()>0){
    		store = rt_storeList.get(0);
    	}
    	
    	HumanresourcesManager hManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
    	if(store!=null){
		 	IFilter hum_filter =FilterFactory.getSimpleFilter(" humanstatus=1 and zw='国安侠' and store_id="+store.getStore_id());
	        List<Humanresources> lst_humanList = (List<Humanresources>)hManager.getList(hum_filter);
	    	return lst_humanList;
    	}
    	return null;
       
    }
    
    
    
    
    
    
    
    
    
    /**
     * 数据动态 查询员工数据
     */
    @Override
    public Map<String, Object> queryHumanresourcesList(Humanresources humanresources, PageInfo pageInfo) {
		HumanresourcesDao humanresourcesDao = (HumanresourcesDao)SpringHelper.getBean(HumanresourcesDao.class.getName());
		Map<String, Object> returnMap = null;
		try {
			returnMap =new HashMap<String,Object>();
			returnMap = humanresourcesDao.queryHumanresourcesList(humanresources, pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}
    
    
    /**
     * 门店运营 数据查询
     */
    @Override
    public Map<String, Object> queryStoreOperationList(Store store, PageInfo pageInfo) {
		HumanresourcesDao humanresourcesDao = (HumanresourcesDao)SpringHelper.getBean(HumanresourcesDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Map<String, Object> returnMap = null;
		try {
			returnMap =new HashMap<String,Object>();
			returnMap = humanresourcesDao.queryStoreOperationList(store, pageInfo);
			Map<String, Object> leaveData = humanresourcesDao.queryStoreOperationLeaveList();
			returnMap.put("leaveData", leaveData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}
    
    
    
    
    @Override
  	public Map<String, Object> exportStoreOperationHuman(Store store) {
  		Map<String, Object> result = new HashMap<String,Object>();
		HumanresourcesDao humanresourcesDao = (HumanresourcesDao)SpringHelper.getBean(HumanresourcesDao.class.getName());
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		Map<String, Object> leaveData= null;
  		try {
  			list=humanresourcesDao.exportStoreOperationHuman(store);
  			leaveData = humanresourcesDao.queryStoreOperationLeaveList();
  		} catch (Exception e) {
  			e.printStackTrace();
  			return null;
  		}
  		if(list!=null&&list.size()>0){//成功返回数据
  			String str_file_dir_path = PropertiesUtil.getValue("file.root");
  			String str_web_path = PropertiesUtil.getValue("file.web.root");

  			/*2003版excel
  			 * HSSFWorkbook wb = new HSSFWorkbook();
  			// 创建Excel的工作sheet,对应到一个excel文档的tab  
  	        setCellStyle_common(wb);
  	        setHeaderStyle(wb);
  	        HSSFSheet sheet = wb.createSheet("公海订单");
  	        HSSFRow row = sheet.createRow(0);*/
  	        
  	        
  	        XSSFWorkbook wb = new XSSFWorkbook();   
  	        setCellStyle_common(wb);
  	        setHeaderStyle(wb);
  	        XSSFSheet sheet = wb.createSheet("门店人员数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段 
  	        String[] str_headers = {"门店名称","国安侠","综合管理","事务管理","服务专员","综合专员","副店长","星店店长","订单处理员","配送员","库房管理员","仓库主管","配送主管","在职/职离"};
  	        String[] headers_key = {"storename","gax","zhgl","swgl","fwzy","zhzy","fdz","xddz","ddcly","psy","kfgly","ckzg","pszg","zzlz"};
  	       
  	        for(int i = 0;i < str_headers.length;i++){
  	            XSSFCell cell = row.createCell(i);
  	            cell.setCellStyle(getHeaderStyle());
  	            cell.setCellValue(new XSSFRichTextString(str_headers[i]));
  	        }
  	        
  	        for(int i = 0;i < list.size();i++){
  	        	 row = sheet.createRow(i+1);
        	 	 String list_storename = (String) list.get(i).get("storename");
        	 	 BigDecimal gax = (BigDecimal) list.get(i).get("gax");
            	 BigDecimal zhgl = (BigDecimal) list.get(i).get("zhgl");
            	 BigDecimal swgl = (BigDecimal) list.get(i).get("swgl");
            	 BigDecimal fwzy = (BigDecimal) list.get(i).get("fwzy");
            	 BigDecimal zhzy = (BigDecimal) list.get(i).get("zhzy");
            	 BigDecimal fdz = (BigDecimal) list.get(i).get("fdz");
            	 BigDecimal xddz = (BigDecimal) list.get(i).get("xddz");
            	 BigDecimal ddcly = (BigDecimal) list.get(i).get("ddcly");
            	 BigDecimal psy = (BigDecimal) list.get(i).get("psy");
            	 BigDecimal kfgly = (BigDecimal) list.get(i).get("kfgly");
            	 BigDecimal ckzg = (BigDecimal) list.get(i).get("ckzg");
            	 BigDecimal pszg = (BigDecimal) list.get(i).get("pszg");
            	 String totalZz=gax.add(zhgl).add(swgl).add(fwzy).add(zhzy).add(fdz).add(xddz).add(ddcly).add(psy).add(kfgly).add(ckzg).add(pszg)+"";
	            	
  	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
  	            	if(headers_key[cellIndex].toString().equals("zzlz")){
  	            		List leaveList = (List) leaveData.get("data");
  	            		boolean leave0=true;
  	            		for(Object o:leaveList){
  	            			Object[] obj = (Object[])o;
  	  	            		String storename = (String) obj[0];
  	  	            		BigInteger leavenum =(BigInteger) obj[1];
  	  	            		if(list_storename!=null&&list_storename.equals(storename)){
  	  	            			leave0=false;
  	  	            			setCellValueall(row, cellIndex, totalZz+"/"+leavenum+"");
  	  	            		}
  	            		}
  	            		if(leave0){
  	            			setCellValueall(row, cellIndex, totalZz+"/0");
  	            		}
  	            	}else{
  	            		setCellValueall(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
  	            	}
  	            	
  	             }
  	        }

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_humanlist.xlsx");
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
    
    
    
    //数据动态 - 门店运营 - 导出门店状态tab2
    @Override
  	public Map<String, Object> exportStoreOperationStore(Store store) {
  		Map<String, Object> result = new HashMap<String,Object>();
  		StoreDao storeDao = (StoreDao) SpringHelper.getBean(StoreDao.class.getName());
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		Map<String, Object> leaveData= null;
  		try {
  			list=storeDao.exportStoreStatusList(store);
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
  	        XSSFSheet sheet = wb.createSheet("门店状态数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	        //定义表头 以及 要填入的 字段  
  	        String[] str_headers = {"门店名称","门店类型","门店排序","门店状态","开店日期"};
  	        String[] headers_key = {"name","storetypename","ordnumber","estate","open_shop_time"};
  	       
  	        for(int i = 0;i < str_headers.length;i++){
  	            XSSFCell cell = row.createCell(i);
  	            cell.setCellStyle(getHeaderStyle());
  	            cell.setCellValue(new XSSFRichTextString(str_headers[i]));
  	        }
  	        
  	        for(int i = 0;i < list.size();i++){
  	        	 row = sheet.createRow(i+1);
        	 	 String list_storename = (String) list.get(i).get("city_name");
        	 	 Integer ordnumber = (Integer) list.get(i).get("ordnumber");
	             String storenumstr = list_storename+" 第"+ordnumber+"家店";
  	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
  	            	if(headers_key[cellIndex].toString().equals("ordnumber")){
  	            		setCellValueall(row, cellIndex, storenumstr);
  	            	}else if(headers_key[cellIndex].toString().equals("open_shop_time")){
  	            		String create_time = list.get(i).get("open_shop_time")==null?"":list.get(i).get("open_shop_time").toString();
  	            		if(create_time!=""&&create_time.length()>9){
  	            			String format_create_time = create_time.substring(0,10);
  	  	            		setCellValueall(row, cellIndex, format_create_time);
  	            		}else{
  	            			setCellValueall(row, cellIndex, create_time);
  	            		}
  	            	}else{
  	            		setCellValueall(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
  	            	}
  	            	
  	             }
  	        }

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_humanlist.xlsx");
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
  	public Map<String, Object> exportHuman(Humanresources humanresources) {
  		Map<String, Object> result = new HashMap<String,Object>();
		HumanresourcesDao humanresourcesDao = (HumanresourcesDao)SpringHelper.getBean(HumanresourcesDao.class.getName());
  		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
  		try {
  			list=humanresourcesDao.exportHuman(humanresources);
  		} catch (Exception e) {
  			e.printStackTrace();
  			return null;
  		}
  		if(list!=null&&list.size()>0){//成功返回数据
  			String str_file_dir_path = PropertiesUtil.getValue("file.root");
  			String str_web_path = PropertiesUtil.getValue("file.web.root");

  			/*2003版excel
  			 * HSSFWorkbook wb = new HSSFWorkbook();
  			// 创建Excel的工作sheet,对应到一个excel文档的tab  
  	        setCellStyle_common(wb);
  	        setHeaderStyle(wb);
  	        HSSFSheet sheet = wb.createSheet("公海订单");
  	        HSSFRow row = sheet.createRow(0);*/
  	        
  	        
  	        XSSFWorkbook wb = new XSSFWorkbook();   
  	        setCellStyle_common(wb);
  	        setHeaderStyle(wb);
  	        XSSFSheet sheet = wb.createSheet("员工数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	      //定义表头 以及 要填入的 字段 
  	      String[] str_headers = {"员工姓名","员工编号","门店","城市","岗位","事业群","入职日期"};
		  String[] headers_key = {"name","employee_no","storename","citySelect","zw","career_group","topostdate"};
  	       if(humanresources!=null&&humanresources.getHumanstatus()!=null&&humanresources.getHumanstatus().equals(1L)){
				//在职
  	    	    str_headers[6]="入职日期";
  	    	    headers_key[6]="topostdate";
			}else if(humanresources!=null&&humanresources.getHumanstatus()!=null&&humanresources.getHumanstatus().equals(2L)){
				//离职
				str_headers[6]="离职日期";
				headers_key[6]="leavedate";
			}
  	       
  	        for(int i = 0;i < str_headers.length;i++){
  	            XSSFCell cell = row.createCell(i);
  	            cell.setCellStyle(getHeaderStyle());
  	            cell.setCellValue(new XSSFRichTextString(str_headers[i]));
  	        }
  	        
  	        for(int i = 0;i < list.size();i++){
  	        	 row = sheet.createRow(i+1);
  	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
  	            	setCellValueall(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
  	             }
  	        }

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_humanlist.xlsx");
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
  	public Map<String, Object> exportSysUser(Humanresources humanresources) {
  		UserManager manager = (UserManager)SpringHelper.getBean("userManager");
  		
  		String name = humanresources.getName();//存姓名 
  		String groupname = humanresources.getZw();//存用户组
  		String mobilephone = humanresources.getPhone();//存电话 
  		Long disabledFlag = humanresources.getHumanstatus();//状态  
  		
  		
  		StringBuffer sbfCondition = new StringBuffer();
  		sbfCondition.append(" 1=1 ");
		if(name!=null&&name.length()>0){
			sbfCondition.append(" and name like '%"+name+"%' ");
		}
		if(groupname!=null&&groupname.length()>0){
			//组得进行特殊处理。根据组查询组的ID 用ID查询 
			UserGroupManager userGroupManager = (UserGroupManager) SpringHelper.getBean("userGroupManager");
			IFilter iFilter =FilterFactory.getSimpleFilter(" name like '%"+groupname+"%'");
			List<UserGroup> lst_groupList = (List<UserGroup>) userGroupManager.getList(iFilter);
			
			if(lst_groupList!=null&&lst_groupList.size()>0){
				String groupids = "";
				for(UserGroup userGroup:lst_groupList){
					groupids+=userGroup.getId()+",";
				}
				groupids=groupids.substring(0,groupids.length()-1);
				sbfCondition.append(" and pk_usergroup in("+groupids+")");
			}else{
				sbfCondition.append(" and 1=0 ");
			}
		}
		if(mobilephone!=null&&mobilephone.length()>0){
			sbfCondition.append(" and mobilephone like '%"+mobilephone+"%' ");
		}
		
		if(disabledFlag!=null&&disabledFlag.equals(1L)){
			sbfCondition.append(" and disabledFlag=1 ");
		}
		if(disabledFlag!=null&&disabledFlag.equals(0L)){
			sbfCondition.append(" and disabledFlag=0 ");
		}
		UserDTO userDTO = manager.getCurrentUserDTO();
		Long group_id = userDTO.getUsergroup().getId();
		String groupcode = userDTO.getUsergroup().getCode();
		boolean isAdmin = false;
		if(groupcode!=null&&(groupcode.equals("GLY")||groupcode.equals("administrator"))){
			isAdmin=true;
		}
		if(!isAdmin){
			//取当前登录人的用户组，查询 判断当前登录人 可操作哪些角色组
			SysUserGroupOperaManager sysUserGroupOperaManager = (SysUserGroupOperaManager) SpringHelper.getBean("sysUserGroupOperaManager");
			SysUserGroupOpera rt_sysGroupOperas = sysUserGroupOperaManager.querySysUserGroupOperasByGroupId(group_id);
			if(rt_sysGroupOperas!=null&&rt_sysGroupOperas.getSub_usergroup_ids()!=null&&rt_sysGroupOperas.getSub_usergroup_ids().length()>0){
			    String groupids = "";
			    groupids = rt_sysGroupOperas.getSub_usergroup_ids().substring(1,rt_sysGroupOperas.getSub_usergroup_ids().length()-1);
				sbfCondition.append(" and pk_usergroup in("+groupids+")");
			}else{
				sbfCondition.append(" and 1=0 ");
			}
		}
		sbfCondition.append(" order by id DESC ");
		IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition.toString());
		List<User> lst_data = (List<User>) manager.getList(iFilter);
		for(User user:lst_data){
			user.setUsergroupname(user.getUsergroup()==null?"":user.getUsergroup().getName());
		}
  		
		//导出操作开始 
  		Map<String, Object> result = new HashMap<String,Object>();
  		if(lst_data!=null&&lst_data.size()>0){//成功返回数据
  			String str_file_dir_path = PropertiesUtil.getValue("file.root");
  			String str_web_path = PropertiesUtil.getValue("file.web.root");

  	        XSSFWorkbook wb = new XSSFWorkbook();   
  	        setCellStyle_common(wb);
  	        setHeaderStyle(wb);
  	        XSSFSheet sheet = wb.createSheet("员工数据");
  	        XSSFRow row = sheet.createRow(0);
  	        
  	      //定义表头 以及 要填入的 字段 
  	      String[] str_headers = {"员工姓名","登录名","电话","人员状态"};
		  String[] headers_key = {"name","code","mobilephone","disabledFlag"};
  	       
  	        for(int i = 0;i < str_headers.length;i++){
  	            XSSFCell cell = row.createCell(i);
  	            cell.setCellStyle(getHeaderStyle());
  	            cell.setCellValue(new XSSFRichTextString(str_headers[i]));
  	        }
  	        
  	        for(int i = 0;i < lst_data.size();i++){
  	        	 row = sheet.createRow(i+1);
  	        	setCellValueall(row, 0, lst_data.get(i).getName());
  	        	setCellValueall(row, 1, lst_data.get(i).getCode());
  	        	setCellValueall(row, 2, lst_data.get(i).getMobilephone());
  	        	setCellValueall(row, 3, lst_data.get(i).getDisabledFlag()==0?"离职":"在职");
  	        }

  			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_sysuser.xlsx");
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

	private void setCellStyle_commonall(Workbook wb){
		cellStyle_commonall=wb.createCellStyle();
		cellStyle_commonall.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 水平居中
		cellStyle_commonall.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);//垂直居中
		
	}

	private CellStyle getCellStyle_commonall() {
		return cellStyle_commonall;
	}

	public void setCellValueall(Row obj_row, int nCellIndex, Object value){
		Cell cell=obj_row.createCell(nCellIndex);
		cell.setCellStyle(getCellStyle_common());
		cell.setCellValue(new XSSFRichTextString(value==null?null:value.toString()));
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
	}
	
	
	public int compare_dateAll(String DATE1, String DATE2) {
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
    
	
	
	
	/**
     * 根据电话  查询系统中是否存在相同的电话信息 
     */
    @Override
    public List<Humanresources> queryHumanresourceListByPhone(String phone){
    	HumanresourcesManager hManager = (HumanresourcesManager)SpringHelper.getBean("humanresourcesManager");
        IFilter iFilter =FilterFactory.getSimpleFilter(" humanstatus=1 and phone='"+phone+"'");
        List<Humanresources> lst_humanList = (List<Humanresources>)hManager.getList(iFilter);
        if(lst_humanList!=null&&lst_humanList.size()>0){
        	return lst_humanList;
        }
    	return null;
    }
}
