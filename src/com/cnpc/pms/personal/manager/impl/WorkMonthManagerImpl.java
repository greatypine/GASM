package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.paging.*;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.entity.AppMessage;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreKeeper;
import com.cnpc.pms.personal.entity.WorkMonth;
import com.cnpc.pms.personal.manager.AppMessageManager;
import com.cnpc.pms.personal.manager.DsTopDataManager;
import com.cnpc.pms.personal.manager.StoreKeeperManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.WorkMonthManager;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 工时计算月份
 * Created by liuxiao on 2016/10/25.
 */
public class WorkMonthManagerImpl extends BizBaseCommonManager implements WorkMonthManager {


    @Override
    public WorkMonth getMaxWorkMonth() {
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
        List<?> lst_data = this.getList(fsp);
        if(lst_data != null && lst_data.size() > 0){
            return (WorkMonth)lst_data.get(0);
        }
        return null;
    }

    @Override
    public WorkMonth getNextWorkMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date_next = null;
        FSP fsp = new FSP();
        IPage pageInfo = new PageInfo();
        pageInfo.setCurrentPage(1);
        pageInfo.setRecordsPerPage(1);
        fsp.setPage(pageInfo);
        fsp.setSort(SortFactory.createSort("id", ISort.DESC));
        List<?> lst_data = this.getList(fsp);
        WorkMonth return_workMonth = new WorkMonth();
        if(lst_data != null && lst_data.size() > 0){
            WorkMonth workMonth =  (WorkMonth)lst_data.get(0);
            try {
                date_next = dateFormat.parse(workMonth.getWork_month());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(date_next == null){
            date_next = new Date();
            return_workMonth.setWork_month(dateFormat.format(date_next));
        }else{
            Calendar calendar_next = Calendar.getInstance();
            calendar_next.setTime(date_next);
            calendar_next.add(Calendar.MONTH,1);
            return_workMonth.setWork_month(dateFormat.format(calendar_next.getTime()));
        }
        return return_workMonth;
    }

    @Override
    public WorkMonth saveWorkMonth(WorkMonth workMonth) {
        WorkMonth save_workmonth = findWorkMonthByCity(workMonth.getWork_month(),workMonth.getCityname());
        if(save_workmonth == null){
            save_workmonth = new WorkMonth();
            //如果查不到发起记录，则为新发起考勤，调用方法 。
            //备份所发起月份 的数据到t_topdata_human表中。
            DsTopDataManager dsTopDataManager = (DsTopDataManager) SpringHelper.getBean("dsTopDataManager");
            dsTopDataManager.saveTopDataHumanresources(workMonth.getCityname(), workMonth.getWork_month());
        }
        save_workmonth.setCityname(workMonth.getCityname());
        save_workmonth.setWork_month(workMonth.getWork_month());
        save_workmonth.setTotalworktime(workMonth.getTotalworktime());
        preObject(save_workmonth);
        this.saveObject(save_workmonth);
        return save_workmonth;
    }

    @Override
    public WorkMonth findWorkMonthByCity(String workMonth,String cityname) {
        List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("work_month='"+workMonth+"' and cityname ='"+cityname+"'"));
        if(lst_data != null && lst_data.size() > 0){
            return (WorkMonth)lst_data.get(0);
        }
        return null;
    }

	@Override
	public WorkMonth findWorkMonth(String workMonth) {
		UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(userManager.getCurrentUserDTO().getStore_id());
		 List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("work_month='"+workMonth+"' and cityname ='"+store.getCityName()+"'"));
	        if(lst_data != null && lst_data.size() > 0){
	            return (WorkMonth)lst_data.get(0);
	        }
	        return null;
	}
}
