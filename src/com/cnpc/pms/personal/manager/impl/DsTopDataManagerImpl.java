package com.cnpc.pms.personal.manager.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.DsTopData;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.manager.DsTopDataManager;
import com.cnpc.pms.personal.manager.HumanresourcesManager;

public class DsTopDataManagerImpl extends BizBaseCommonManager implements DsTopDataManager {

	/**
	 * 根据月份 门店 查询门店的员工。（考勤和绩效分 查询上个月的人 员 ）
	 * @param Long store_id,String month
	 */
	@Override
    public List<DsTopData> queryDSTopData(Long store_id,String yearmonth){
    	StringBuilder sb_where = new StringBuilder();
    	sb_where.append(" 1=1 ");
    	if(store_id!=null){
    		sb_where.append(" and storeid="+store_id);
    	}
    	if(yearmonth!=null&&yearmonth.length()>0){
    		sb_where.append(" and yearmonth='"+yearmonth+"'");
    	}
    	//添加查询条件 只查综合管理和国安侠  persontype 2是 非店长   ， 1是店长
    	sb_where.append(" and persontype =2 and humanstatus =1 order by employeeno ");  //只查非店长 在职 人员
    	
		IFilter iFilter =FilterFactory.getSimpleFilter(sb_where.toString());
        List<DsTopData> lst_humanList = (List<DsTopData>)this.getList(iFilter);
    	if(lst_humanList!=null){
    		return lst_humanList;
    	}
    	return null;
    }
	
	//根据员工号查询某月时候的数据
	@Override
	public DsTopData queryDSTopDataByEmployeeNo(String employeeno,String yearmonth){
    	StringBuilder sb_where = new StringBuilder();
    	sb_where.append(" 1=1 ");
    	if(employeeno!=null){
    		sb_where.append(" and employeeno='"+employeeno+"'");
    	}
    	if(yearmonth!=null&&yearmonth.length()>0){
    		sb_where.append(" and yearmonth='"+yearmonth+"'");
    	}
    	//添加查询条件 只查综合管理和国安侠 
    	//sb_where.append(" and persontype =2 and humanstatus =1 order by employeeno ");  //只查非店长 在职 人员
    	
		IFilter iFilter =FilterFactory.getSimpleFilter(sb_where.toString());
        List<DsTopData> lst_humanList = (List<DsTopData>)this.getList(iFilter);
    	if(lst_humanList!=null&&lst_humanList.size()>0){
    		return lst_humanList.get(0);
    	}
    	return null;
    }
	
	
	@Override
    public List<DsTopData> queryDSTopDataLz(Long store_id,String yearmonth){
    	StringBuilder sb_where = new StringBuilder();
    	sb_where.append(" 1=1 ");
    	if(store_id!=null){
    		sb_where.append(" and storeid="+store_id);
    	}
    	if(yearmonth!=null&&yearmonth.length()>0){
    		sb_where.append(" and yearmonth='"+yearmonth+"'");
    	}
    	sb_where.append(" and persontype =2 and humanstatus =2 ");  //只查非店长  离职人员
    	String minDay = getMinMonthDate(yearmonth+"-01");
    	String maxDay = getMaxMonthDate(yearmonth+"-01");
    	String datecondition = " and DATE_FORMAT(leavedate,'%Y-%m-%d') >= '"+minDay+"' AND DATE_FORMAT(leavedate,'%Y-%m-%d')<= '"+maxDay+"' ";
        IFilter iFilter =FilterFactory.getSimpleFilter(sb_where.toString()+datecondition);
    	//添加查询条件 只查综合管理和国安侠 
        List<DsTopData> lst_humanList = (List<DsTopData>)this.getList(iFilter);
    	if(lst_humanList!=null){
    		return lst_humanList;
    	}
    	return null;
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
	  public String saveTopDataHumanresources(String citySelect,String workmonth){
		  //根据城市及月份查询，有数据 不执行
		  DsTopDataManager dsTopDataManager = (DsTopDataManager) SpringHelper.getBean("dsTopDataManager");
		  IFilter iFilter =FilterFactory.getSimpleFilter("cityname='"+citySelect+"' and yearmonth='"+workmonth+"'");
	      List<DsTopData> lst_dsdataList = (List<DsTopData>)dsTopDataManager.getList(iFilter); 
	      if(lst_dsdataList!=null&&lst_dsdataList.size()>0){
	    	  return citySelect+workmonth+"已存在";
	      }else{
	    	  HumanresourcesManager humanresourcesManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
			  List<Humanresources> cityHumanresources = humanresourcesManager.queryHumanresourceListByCity(citySelect);
			  java.util.Date date = new java.util.Date();
			  java.sql.Date sdate = new java.sql.Date(date.getTime());
				
			  if(cityHumanresources!=null&&cityHumanresources.size()>0){
				  for(Humanresources h : cityHumanresources){
					  DsTopData dsTopData = new DsTopData();
					  dsTopData.setCityname(h.getCitySelect());
					  dsTopData.setEmployeeno(h.getEmployee_no());
					  dsTopData.setUsername(h.getName());
					  dsTopData.setHumanstatus(h.getHumanstatus());
					  dsTopData.setStoreid(h.getStore_id());
					  dsTopData.setStorename(h.getStorename());
					  dsTopData.setZw(h.getZw());
					  dsTopData.setPersontype(2);
					  dsTopData.setAuthorizedtype(h.getAuthorizedtype());
					  dsTopData.setProfessnallevel(h.getProfessnallevel());
					  dsTopData.setRemark(h.getRemark());
					  dsTopData.setLeavedate(h.getLeavedate());
					  dsTopData.setTopostdate(h.getTopostdate());
					  dsTopData.setYearmonth(workmonth);
					  dsTopData.setCreatetime(sdate);
					  preSaveObject(dsTopData);
					  saveObject(dsTopData);
				  }
			  }
			  return citySelect+workmonth+"执行完成";
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
	  
	  
}
