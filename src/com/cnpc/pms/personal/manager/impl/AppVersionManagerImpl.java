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
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.utils.ChineseToEnglish;
import com.cnpc.pms.utils.CompressFile;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.EntityEquals;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;

@SuppressWarnings("all")
public class AppVersionManagerImpl extends BizBaseCommonManager implements AppVersionManager {
	
	@Override
	public AppVersion saveAppVersion(AppVersion args){
		AppVersion dbVersion = queryAppVersionByVersion(args.getVersion());
		AppVersion appVersion = null;
		if(dbVersion!=null){
			return null;
		}else{
			appVersion=new AppVersion();
			appVersion.setVersion(args.getVersion());
			appVersion.setFile_name(args.getFile_name());
			appVersion.setTiptype(args.getTiptype());
			appVersion.setMessage(args.getMessage());
			appVersion.setIsupdate(args.getIsupdate());
			
			User sessionUser = null;
			if (null != SessionManager.getUserSession()
					&& null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession()
						.getSessionData().get("user");
			}
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			// insert处理时添加创建人和创建时间
			if (null == appVersion.getCreate_time()) {
				appVersion.setCreate_time(sdate);
				if (null != sessionUser) {
					appVersion.setCreate_user(sessionUser.getCode());
				}
			}
			appVersion.setUpdate_time(sdate);
			if (null != sessionUser) {
				appVersion.setUpdate_user(sessionUser.getCode());
			}
			
			saveObject(appVersion);
		}
		return appVersion;
	}
	
	
	@Override
	public AppVersion updateAppVersion(AppVersion appVersion){
		AppVersion dbVersion = (AppVersion) this.getObject(appVersion.getId());
		if(dbVersion!=null){
			dbVersion.setIsupdate(appVersion.getIsupdate());
			dbVersion.setTiptype(appVersion.getTiptype());
			dbVersion.setMessage(appVersion.getMessage());
			User sessionUser = null;
			if (null != SessionManager.getUserSession()
					&& null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession()
						.getSessionData().get("user");
			}
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			dbVersion.setUpdate_time(sdate);
			if (null != sessionUser) {
				dbVersion.setUpdate_user(sessionUser.getCode());
			}
			saveObject(dbVersion);
			return dbVersion;
		}
		return null;
		
	}
	
	
	
	
	
	@Override
	public AppVersion queryAppVersionById(Long id){
		AppVersion appVersion = (AppVersion) this.getObject(id);
		return appVersion;
	}
	
	
	public AppVersion queryAppVersionByVersion(String version){
		if(version!=null&&version.length()>0){
			version=version.trim().toUpperCase();
		}
		IFilter iFilter =FilterFactory.getSimpleFilter(" version = '"+version+"'");
        List<?> lst_groupList = this.getList(iFilter);
        if(lst_groupList!=null&&lst_groupList.size()>0){
        	AppVersion appVersion = (AppVersion) lst_groupList.get(0);
        	 return appVersion;
        }else{
        	 return null;
        }
	}
	
	
	@Override
	public AppVersion queryAppVersionNew(){
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("id", ISort.DESC));
		List<?> lst_groupList = this.getList(fsp);
        if(lst_groupList!=null&&lst_groupList.size()>0){
        	AppVersion appVersion = (AppVersion) lst_groupList.get(0);
        	 return appVersion;
        }else{
        	 return null;
        }
	}
	
    
}
