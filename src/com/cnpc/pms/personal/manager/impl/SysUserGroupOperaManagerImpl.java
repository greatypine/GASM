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
import com.cnpc.pms.personal.dao.HouseStyleDao;
import com.cnpc.pms.personal.dao.HumanenteachDao;
import com.cnpc.pms.personal.dao.HumanresourcesDao;
import com.cnpc.pms.personal.dao.ImportHumanresourcesDao;
import com.cnpc.pms.personal.dao.StoreKeeperDao;
import com.cnpc.pms.personal.dao.YyMicrDataDao;
import com.cnpc.pms.personal.entity.AppVersion;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.Humanenteach;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.HumanresourcesChange;
import com.cnpc.pms.personal.entity.ImportHumanresources;
import com.cnpc.pms.personal.entity.OuterCompany;
import com.cnpc.pms.personal.entity.SelfExpress;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreKeeper;
import com.cnpc.pms.personal.entity.SysUserChangeLog;
import com.cnpc.pms.personal.entity.SysUserGroupOpera;
import com.cnpc.pms.personal.entity.XxExpress;
import com.cnpc.pms.personal.manager.AppVersionManager;
import com.cnpc.pms.personal.manager.BuildingManager;
import com.cnpc.pms.personal.manager.DistCityManager;
import com.cnpc.pms.personal.manager.HouseManager;
import com.cnpc.pms.personal.manager.HumanenteachManager;
import com.cnpc.pms.personal.manager.HumanresourcesChangeManager;
import com.cnpc.pms.personal.manager.HumanresourcesLogManager;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
import com.cnpc.pms.personal.manager.ImportHumanresourcesManager;
import com.cnpc.pms.personal.manager.OuterCompanyManager;
import com.cnpc.pms.personal.manager.StoreKeeperManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.SysUserChangeLogManager;
import com.cnpc.pms.personal.manager.SysUserGroupOperaManager;
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.utils.ChineseToEnglish;
import com.cnpc.pms.utils.CompressFile;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.EntityEquals;
import com.cnpc.pms.utils.MD5Utils;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;

@SuppressWarnings("all")
public class SysUserGroupOperaManagerImpl extends BizBaseCommonManager implements SysUserGroupOperaManager {
	
	@Override
	public SysUserGroupOpera queryGroupOperaById(Long id){
		return (SysUserGroupOpera) this.getObject(id);
	}
	
	@Override
	public SysUserGroupOpera querySysUserGroupOperasByGroupId(Long sys_usergroup_id){
		SysUserGroupOperaManager sysUserGroupOperaManager = (SysUserGroupOperaManager) SpringHelper.getBean("sysUserGroupOperaManager");
		IFilter iFilter =FilterFactory.getSimpleFilter(" sys_usergroup_id ="+sys_usergroup_id);
		List<SysUserGroupOpera> lst_userGroupOperas = (List<SysUserGroupOpera>) sysUserGroupOperaManager.getList(iFilter);
		if(lst_userGroupOperas!=null&&lst_userGroupOperas.size()>0){
			return lst_userGroupOperas.get(0);
		}
		return null;
	}
	
	
	
	@Override
	public List<UserGroup> querySysGroupByCurUser(){
		SysUserGroupOperaManager sysUserGroupOperaManager = (SysUserGroupOperaManager) SpringHelper.getBean("sysUserGroupOperaManager");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		UserGroupManager userGroupManager = (UserGroupManager) SpringHelper.getBean("userGroupManager");
		
		Long cur_groupid = userManager.getCurrentUserDTO().getUsergroup().getId();
		String groupcode = userManager.getCurrentUserDTO().getUsergroup().getCode();
		boolean isAdmin = false;
		if(groupcode!=null&&(groupcode.equals("GLY")||groupcode.equals("administrator"))){
			isAdmin=true;
		}
		
		if(!isAdmin){
			IFilter iFilter =FilterFactory.getSimpleFilter(" sys_usergroup_id ="+cur_groupid);
			List<SysUserGroupOpera> lst_userGroupOperas = (List<SysUserGroupOpera>) sysUserGroupOperaManager.getList(iFilter);
			if(lst_userGroupOperas!=null&&lst_userGroupOperas.size()>0){
				SysUserGroupOpera sysUserGroupOpera =  lst_userGroupOperas.get(0);
				String groupids = sysUserGroupOpera.getSub_usergroup_ids();
				if(groupids!=null&&groupids.length()>0){
					groupids = groupids.substring(1,groupids.length()-1);
					IFilter subFilter =FilterFactory.getSimpleFilter(" id in("+groupids+")");
					List<UserGroup> lst_usergrGroups = (List<UserGroup>) userGroupManager.getList(subFilter);
					return lst_usergrGroups;
				}
			}
		}else{
			List<UserGroup> lst_usergrGroups = (List<UserGroup>) userGroupManager.queryAllUserGroup();
			return lst_usergrGroups;
		}
		return null;
	}
	
	
	
	@Override
	public SysUserGroupOpera saveSysUserGroupOpera(SysUserGroupOpera sysUserGroupOpera){
		preSaveObject(sysUserGroupOpera);
		this.saveObject(sysUserGroupOpera);
		return sysUserGroupOpera;
		
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
	
	
	@Override
	public User saveSysUser(User user){
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		UserGroupManager userGroupManager = (UserGroupManager)SpringHelper.getBean("userGroupManager");
		User saveUser = null;
		if(user.getId()!=null){
			saveUser = (User) userManager.getObject(user.getId());
			saveUser.setName(user.getName());
			saveUser.setMobilephone(user.getMobilephone());
			saveUser.setPhone(user.getPhone());
			saveUser.setCode(user.getCode());
			saveUser.setZw(user.getZw());
			UserGroup userGroup = (UserGroup) userGroupManager.getObject(user.getcOrgId());
			saveUser.setUsergroup(userGroup);
			saveUser.setDisabledFlag(user.getDisabledFlag());
			//判断密码
			if(user.getPassword()!=null&&user.getPassword().trim().length()>0){
				String savepassword = MD5Utils.getMD5Str(user.getPassword());
				saveUser.setPassword(savepassword);
			}
			preSaveObject(saveUser);
			userManager.saveObject(saveUser);
		}else{
			saveUser = new User();
			saveUser.setName(user.getName());
			saveUser.setCode(user.getCode());
			saveUser.setDisabledFlag(1);
			saveUser.setDoctype(0);
			saveUser.setEmail("123@123.com");
			saveUser.setEnablestate(1);
			String savepassword = MD5Utils.getMD5Str(user.getPassword());
			saveUser.setPassword(savepassword);
			saveUser.setPk_org(Long.parseLong("40284"));
			saveUser.setMobilephone(user.getMobilephone());
			saveUser.setPhone(user.getPhone());
			saveUser.setZw(user.getZw());
			UserGroup userGroup = (UserGroup) userGroupManager.getObject(user.getcOrgId());
			saveUser.setUsergroup(userGroup);
			saveUser.setLogicDel(0);
			preSaveObject(saveUser);
			userManager.saveObject(saveUser);
		}
		
		//无论新增还是修改 都  保存历史记录
		SysUserChangeLog sysUserChangeLog = new SysUserChangeLog();
		SysUserChangeLogManager sysUserChangeLogManager = (SysUserChangeLogManager) SpringHelper.getBean("sysUserChangeLogManager");
		sysUserChangeLog.setSys_user_id(saveUser.getId());
		sysUserChangeLog.setSys_user_name(saveUser.getName());
		sysUserChangeLog.setSys_user_code(saveUser.getCode());
		sysUserChangeLog.setSys_user_phone(saveUser.getMobilephone());
		sysUserChangeLog.setSys_user_zw(saveUser.getZw());
		sysUserChangeLog.setDisabledFlag(saveUser.getDisabledFlag());
		sysUserChangeLog.setSys_user_password(saveUser.getPassword());
		sysUserChangeLog.setSys_usergroup_id(saveUser.getUsergroup().getId());
		sysUserChangeLog.setSys_usergroup_name(saveUser.getUsergroup().getName());
		sysUserChangeLogManager.saveSysUserChangeLog(sysUserChangeLog);
		return saveUser;
	}
	
	@Override
	public UserDTO queryUserById(Long userid){
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		UserDTO rtUser = (UserDTO) userManager.getUserDTO(userid);
		return rtUser;
		
	}
	
}
