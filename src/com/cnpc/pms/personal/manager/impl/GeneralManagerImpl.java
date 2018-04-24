package com.cnpc.pms.personal.manager.impl;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;

import org.apache.commons.configuration.beanutils.BeanHelper;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
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
import com.cnpc.pms.personal.dao.DistCityDao;
import com.cnpc.pms.personal.dao.GeneralDao;
import com.cnpc.pms.personal.dao.HouseStyleDao;
import com.cnpc.pms.personal.dao.HumanenteachDao;
import com.cnpc.pms.personal.dao.HumanresourcesDao;
import com.cnpc.pms.personal.dao.ImportHumanresourcesDao;
import com.cnpc.pms.personal.dao.StoreKeeperDao;
import com.cnpc.pms.personal.dao.YyMicrDataDao;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.DistCityCode;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.General;
import com.cnpc.pms.personal.entity.Humanenteach;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.HumanresourcesChange;
import com.cnpc.pms.personal.entity.ImportHumanresources;
import com.cnpc.pms.personal.entity.SelfExpress;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreKeeper;
import com.cnpc.pms.personal.entity.XxExpress;
import com.cnpc.pms.personal.manager.BuildingManager;
import com.cnpc.pms.personal.manager.DistCityCodeManager;
import com.cnpc.pms.personal.manager.DistCityManager;
import com.cnpc.pms.personal.manager.GeneralManager;
import com.cnpc.pms.personal.manager.HouseManager;
import com.cnpc.pms.personal.manager.HumanenteachManager;
import com.cnpc.pms.personal.manager.HumanresourcesChangeManager;
import com.cnpc.pms.personal.manager.HumanresourcesLogManager;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
import com.cnpc.pms.personal.manager.ImportHumanresourcesManager;
import com.cnpc.pms.personal.manager.StoreKeeperManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.utils.ChineseToEnglish;
import com.cnpc.pms.utils.CompressFile;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.EntityEquals;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;

/**
 * 城市总监管理
 * @author zhaoxg 2017-6-14
 */
@SuppressWarnings("all")
public class GeneralManagerImpl extends BizBaseCommonManager implements GeneralManager {
	
	
	/**
	 * 保存城市总监方法 
	 * @param general
	 * @return
	 */
	@Override
	public General saveGeneral(General general){
		GeneralDao generalDao = (GeneralDao) SpringHelper.getBean(GeneralDao.class.getName());
		//根据姓名，电话号判断是否重复
		IFilter iFilter =FilterFactory.getSimpleFilter("phone",general.getPhone());
		List<StoreKeeper> lst_data = (List<StoreKeeper>) this.getList(iFilter);
		if(lst_data!=null&&lst_data.size()>0){
			return null;
		}
		//拼音码
		general.setCode(ChineseToEnglish.getPingYin(general.getName()));
		//自动生成编号
		String stcode = "GM";
		String max_employee_no = generalDao.queryMaxEmployee_no();
		if(max_employee_no!=null&&max_employee_no.length()>0){
			int ret = Integer.parseInt(max_employee_no.substring(2,max_employee_no.length()))+1;
			String str_no = "000000"+ret;
			max_employee_no = stcode + str_no.substring(str_no.length()-5,str_no.length());
		}else{
			max_employee_no = "GM00001";
		}
		general.setEmployee_no(max_employee_no);
		//保存本身表
		preSaveObject(general);
		saveObject(general);
		
		
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		User user = userManager.findEmployeeByEmployeeNo(general.getEmployee_no());
		if(user==null){
			addBizUser(general);
		}
		
		
		/*//插入系统user表 
		if(general.getCitySelect()!=null&&general.getCitySelect().length()>0){
			User user = addBizUser(general);
			//插入中间表 与城市关联的表
			Long gmid = general.getId();
			String citys = general.getCitySelect();
			if(citys!=null){
				String[] cs = citys.split(",");
				String vals = "";
				for(String c:cs){
					if(c!=null&&c.length()>0){
						Long id = generalDao.queryMaxid()+1;
						String oneval = "("+gmid+","+Long.parseLong(c)+")";
						vals += oneval+",";
						
					}
				}
				if(vals!=""){
					vals=vals.substring(0,vals.length()-1);
					generalDao.saveGeneralCityReference(gmid,vals);
				}
			}
		}*/
		return general;
	}
	
	
	
	
	/**
	 * 修改城市总监方法 
	 * @param general
	 * @return
	 */
	@Override
	public General updateGeneral(General general){
		General gene_obj = (General) this.getObject(general.getId());
		if(gene_obj.getPhone()!=null&&!gene_obj.getPhone().equals(general.getPhone())){
			//根据姓名，电话号判断是否重复
			IFilter iFilter =FilterFactory.getSimpleFilter("phone",general.getPhone());
			List<StoreKeeper> lst_data = (List<StoreKeeper>) this.getList(iFilter);
			if(lst_data!=null&&lst_data.size()>0){
				return null;
			}
		}
		gene_obj.setName(general.getName());
		gene_obj.setPhone(general.getPhone());
		gene_obj.setSex(general.getSex());
		GeneralDao generalDao = (GeneralDao) SpringHelper.getBean(GeneralDao.class.getName());
		//拼音码
		//gene_obj.setCode(ChineseToEnglish.getPingYin(general.getName()));
		gene_obj.setCitySelect(general.getCitySelect());
		//保存本身表
		preSaveObject(gene_obj);
		saveObject(gene_obj);
		//修改user系统表
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		User u = userManager.findEmployeeByEmployeeNo(gene_obj.getEmployee_no());
		u.setName(gene_obj.getName());
		u.setMobilephone(gene_obj.getPhone());
		preSaveObject(u);
		userManager.saveObject(u);
		
		//插入中间表 与城市关联的中间表
		/*Long gmid = gene_obj.getId();
		String citys = gene_obj.getCitySelect();
		if(citys!=null){
			String[] cs = citys.split(",");
			String vals = "";
			for(String c:cs){
				if(c!=null&&c.length()>0){
					Long id = generalDao.queryMaxid()+1;
					String oneval = "("+gmid+","+Long.parseLong(c)+")";
					vals += oneval+",";
					
				}
			}
			if(vals!=""){
				vals=vals.substring(0,vals.length()-1);
				generalDao.saveGeneralCityReference(gmid,vals);
			}
			
		}*/
		return gene_obj;
	}
	
	
	/**
	 * 查询列表方法 
	 */
	 @Override
	 public Map<String, Object> queryGeneralList(QueryConditions condition) {
	    	UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
	    	StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
	    	GeneralDao generalDao = (GeneralDao) SpringHelper.getBean(GeneralDao.class.getName());
			Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
			PageInfo pageInfo = condition.getPageinfo();
			String name = null;
			String citynames = null;
			for(Map<String, Object> map : condition.getConditions()){
				if("name".equals(map.get("key"))&&map.get("value")!=null){//查询条件
					name = map.get("value").toString();
				}
				if("citynames".equals(map.get("key"))&&map.get("value")!=null){//查询条件
					citynames = map.get("value").toString();
				}
				
			}
			FSP fsp = new FSP();
			fsp.setSort(SortFactory.createSort("id", ISort.DESC));
			StringBuffer sbfCondition = new StringBuffer();
			sbfCondition.append(" 1=1 ");
			if(name!=null&&name.length()>0){
				sbfCondition.append(" and name like '%"+name+"%' ");
	        }
			if(citynames!=null&&citynames.length()>0){
				//根据ID查找所有的总监 在中间表中 
				String in_ids = generalDao.queryGeneralIdsByCity(citynames);
				if(in_ids!=null&&in_ids.length()>0){
					sbfCondition.append(" and id in ("+in_ids+")");
				}else{
					sbfCondition.append(" and 1=0 ");
				}
	        }
			
			sbfCondition.append(" order by id DESC ");
			
			DistCityManager distCityManager = (DistCityManager) SpringHelper.getBean("distCityManager");
			IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition.toString());
			fsp.setPage(pageInfo);
			fsp.setUserFilter(iFilter);
			List<General> lst_data = (List<General>) this.getList(fsp);
			List<General> ret_data = null;
			if(lst_data!=null&&lst_data.size()>0){
				ret_data = new ArrayList<General>();
				for(General gm:lst_data){
					//查询所管理城市
					User user = userManager.findEmployeeByEmployeeNo(gm.getEmployee_no());
					DistCity distCity = distCityManager.queryDistCitysByUserId(user.getId());
					String cityname = "";
					if(distCity!=null){
						if(distCity.getCity1()!=null){
							cityname+=distCity.getCity1()+",";
						}
						if(distCity.getCity2()!=null){
							cityname+=distCity.getCity2()+",";
						}
						if(distCity.getCity3()!=null){
							cityname+=distCity.getCity3()+",";
						}
						if(distCity.getCity4()!=null){
							cityname+=distCity.getCity4()+",";
						}
						if(distCity.getCity5()!=null){
							cityname+=distCity.getCity5()+",";
						}
					}
					gm.setCitySelect(cityname);
					ret_data.add(gm);
				}
				
			}
			
			returnMap.put("pageinfo", pageInfo);
			returnMap.put("header", "");
			returnMap.put("data", ret_data);
			return returnMap;
		}
	
	 @Override
	 public General queryGeneralById(Long id){
		General general = (General) this.getObject(id);
		//查询所管理的城市
		GeneralDao generalDao = (GeneralDao) SpringHelper.getBean(GeneralDao.class.getName());
		Map<String, Object> citys = generalDao.queryCityNamesById(general.getId());
		String cityids = generalDao.queryCityNamesById(general.getId()).get("cityids").toString();
		general.setCitySelect(citys.get("citynames").toString());
		general.setSelectIds(citys.get("cityids").toString());
		return general;
	 }
	 
	 @Override
	 public General queryGeneralByEmployee_no(String employee_no){
		IFilter iFilter =FilterFactory.getSimpleFilter("employee_no",employee_no);
		List<General> lst_data = (List<General>) this.getList(iFilter);
		if(lst_data!=null&&lst_data.size()>0){
			return lst_data.get(0);
		}
		return null;
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
    
    
    private User addBizUser(General general){
    	UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
		User user = new User();
		
		user.setName(general.getName());
		user.setCode(ChineseToEnglish.getPingYin(general.getName()));
		user.setDisabledFlag(1);
		user.setDoctype(0);
		user.setEmail("gm@123.com");
		user.setEmployeeId(general.getEmployee_no());
		user.setEnablestate(1);
		user.setPassword("e10adc3949ba59abbe56e057f20f883e"); //123456
		user.setPk_org(Long.parseLong("40284"));
		user.setMobilephone(general.getPhone());
		
		
		UserGroupManager u = (UserGroupManager)SpringHelper.getBean("userGroupManager");
        IFilter iFilter =FilterFactory.getSimpleFilter(" code = 'CSZJJSZ' ");
        List<?> lst_groupList = u.getList(iFilter);
        UserGroup userGroup = (UserGroup) lst_groupList.get(0);
        user.setZw("城市总监");
        user.setUsergroup(userGroup);
        user.setLogicDel(0);
        
        preSaveObject(user);
		userManager.saveObject(user);
		
		return user;
    }
    
}
