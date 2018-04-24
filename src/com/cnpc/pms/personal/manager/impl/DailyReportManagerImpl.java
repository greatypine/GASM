package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.inter.entity.DailyReport;
import com.cnpc.pms.personal.dao.DailyReportDao;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.DailyReportManager;
import com.cnpc.pms.personal.manager.StoreManager;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 类名: DailyReportManagerImpl  
 * 功能描述: 门店日报的接口的实现类 
 * 作者: 常鹏飞  
 * 时间: 2016-3-1   
 *   
 */
public class DailyReportManagerImpl extends BizBaseCommonManager implements DailyReportManager {

    /**
     * 门店日常
     * @param dailyReport 门店日常对象
     * @return : Result 返回结果
     */
    @Override
    public Result saveOrUpdateStoreDaily(DailyReport dailyReport) {
        Result obj_result = new Result();

        Long store_id = dailyReport.getStore_id();
        //当前登录用户
        String create_user = dailyReport.getCreate_user();


        if( store_id == null || dailyReport.getEnter_num() == null || dailyReport.getTest_num() == null
                || dailyReport.getReg_num() == null || dailyReport.getPurchase_num() == null
                || dailyReport.getLife_card() == null|| dailyReport.getLife_test() == null
                || dailyReport.getHealth_request() == null || create_user == null){

            obj_result.setCode(CodeEnum.paramErr.getValue());
            obj_result.setMessage(CodeEnum.paramErr.getDescription());

            return obj_result;
        }
        List<?> lst_daily =  super.getList(FilterFactory.getSimpleFilter("report_date",dailyReport.getReport_date())
                .appendAnd(FilterFactory.getSimpleFilter("store_id",store_id)));

        DailyReport obj_save_daily;
        if(lst_daily != null && lst_daily.size() > 0){
            obj_save_daily = (DailyReport)lst_daily.get(0);
        }else{
            obj_save_daily = new DailyReport();
        }
        dailyReport.setId(obj_save_daily.getId());
        BeanUtils.copyProperties(dailyReport,obj_save_daily);
        //保存数据库
        saveObject(obj_save_daily);
        obj_result.setCode(CodeEnum.success.getValue());
        obj_result.setMessage(CodeEnum.success.getDescription());

        return obj_result;
    }

    @Override
    public DailyReport findDailyReportByStoreIdAndDate(DailyReport dailyReport) {
        List<?> lst_daily =  super.getList(FilterFactory.getSimpleFilter("report_date",dailyReport.getReport_date())
                .appendAnd(FilterFactory.getSimpleFilter("store_id",dailyReport.getStore_id())));
        if(lst_daily != null && lst_daily.size() > 0){
            return (DailyReport)lst_daily.get(0);
        }
        return null;
    }

    @Override
    public Map<String, Object> findDailyReportByPageInfo(PageInfo pageInfo, Long store_id) {
        FSP fsp = new FSP();
        fsp.setPage(pageInfo);
        fsp.setSort(SortFactory.createSort("report_date",ISort.DESC));
        List<DailyReport> lst_daily = (List<DailyReport>)super.getList(fsp);

        Map<String,Object> map_result = new HashMap<String,Object>();
        map_result.put("page",pageInfo);
        map_result.put("data",lst_daily);
        return map_result;
    }

	@Override
	public DailyReport saveStoreDaily(DailyReport dailyReport) {
		UserManager userManager=(UserManager)SpringHelper.getBean("userManager");
		User sessionUser = null;
		if(null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User)SessionManager.getUserSession().getSessionData().get("user");
		}
		sessionUser=userManager.getUserEntity(sessionUser.getId());
		if( sessionUser.getStore_id() == null || dailyReport.getEnter_num() == null || dailyReport.getTest_num() == null
                || dailyReport.getReg_num() == null || dailyReport.getPurchase_num() == null
                || dailyReport.getLife_card() == null|| dailyReport.getLife_test() == null
                || dailyReport.getHealth_request() == null){
            return null;
        }
		 List<?> lst_daily =  super.getList(FilterFactory.getSimpleFilter("report_date",dailyReport.getReport_date())
	                .appendAnd(FilterFactory.getSimpleFilter("store_id",sessionUser.getStore_id())));

	        DailyReport obj_save_daily;
	        if(lst_daily != null && lst_daily.size() > 0){
	            obj_save_daily = (DailyReport)lst_daily.get(0);
	        }else{
	            obj_save_daily = new DailyReport();
	        }
	        dailyReport.setId(obj_save_daily.getId());
	        dailyReport.setStore_id(sessionUser.getStore_id());
	        BeanUtils.copyProperties(dailyReport,obj_save_daily,new String[]{"create_user","create_user_id","create_time"
					,"update_user","update_user_id","update_time"});
	        preObject(obj_save_daily);
	        //保存数据库
	        saveObject(obj_save_daily);
		return obj_save_daily;
	}

	@Override
	public Map<String, Object> queryDailyReportDate(QueryConditions conditions) {
		DailyReportDao dailyReportDao=(DailyReportDao)SpringHelper.getBean(DailyReportDao.class.getName());
		UserManager userManager=(UserManager)SpringHelper.getBean("userManager");
		 StringBuilder sb_where = new StringBuilder();
	        //分页对象
	        PageInfo obj_page = conditions.getPageinfo();
	        //返回的对象，包含数据集合、分页对象等
	        Map<String, Object> map_result = new HashMap<String, Object>();
	        for (Map<String, Object> map_where : conditions.getConditions()) {
	            if ("name".equals(map_where.get("key"))
	                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
	                sb_where.append(" AND stoe.`name` like '").append(map_where.get("value")).append("'");
	            }
	            if ("city_name".equals(map_where.get("key"))
	                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
	            	sb_where.append(" AND stoe.city_name like '").append(map_where.get("value")).append("'");
				}
	            if ("report_date".equals(map_where.get("key"))
	                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
	            	sb_where.append(" AND DATE_FORMAT(repp.report_date,'%Y-%m-%d') ='").append(map_where.get("value")).append("'");
				}
	        }
	        User sessionUser = null;
			if (null != SessionManager.getUserSession()
					&& null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession()
						.getSessionData().get("user");
			}
			 sessionUser =userManager.getUserEntity(sessionUser.getId());
			
			
			StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
			Store store = storeManager.findStore(sessionUser.getStore_id());
	        if(3224==sessionUser.getUsergroup().getId()){
				if(store!=null){
					sb_where.append(" AND stoe.store_id="+store.getStore_id());
				}else{
					sb_where.append(" AND 1=0");
				}
				
			}else if(3231==sessionUser.getUsergroup().getId()||3223==sessionUser.getUsergroup().getId()||3225==sessionUser.getUsergroup().getId()||3229==sessionUser.getUsergroup().getId()){
				if(store!=null){
					sb_where.append(" AND stoe.store_id="+store.getStore_id());
				}else{
					sb_where.append(" AND 1=0");
				}
			}else {
				  
			      //取得当前登录人 所管理城市
					String cityssql = "";
					List<DistCity> distCityList = userManager.getCurrentUserCity();
					if(distCityList!=null&&distCityList.size()>0){
						for(DistCity d:distCityList){
							cityssql += "'"+d.getCityname()+"',";
						}
						cityssql=cityssql.substring(0, cityssql.length()-1);
					}
					if(cityssql.length()>0){
						sb_where.append(" and stoe.city_name in ("+cityssql+")");
					}
			}
	        map_result.put("pageinfo", obj_page);
	        map_result.put("header", "门店经营指数");
	        map_result.put("data", dailyReportDao.getDailyReportList(sb_where.toString(), obj_page));
	        return map_result;
	}

	@Override
	public DailyReport findDailyReportById(Long id) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("id",id));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
        	return (DailyReport)lst_vilage_data.get(0);
        }
		return null;
	}

	@Override
	public DailyReport findDailyReportByDayAndStore_id(DailyReport dailyReport) {
		List<?> lst_daily =  getList(FilterFactory.getSimpleFilter("report_date",dailyReport.getReport_date())
                .appendAnd(FilterFactory.getSimpleFilter("store_id",dailyReport.getStore_id())));
		if(lst_daily != null && lst_daily.size() > 0) {
        	return (DailyReport)lst_daily.get(0);
        }
		return null;
	}


}
