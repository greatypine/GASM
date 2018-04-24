package com.cnpc.pms.personal.manager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Array;

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
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.FlowConfig;
import com.cnpc.pms.personal.entity.FlowDetail;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.WorkInfo;
import com.cnpc.pms.personal.manager.DistCityManager;
import com.cnpc.pms.personal.manager.FlowConfigManager;
import com.cnpc.pms.personal.manager.FlowDetailManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.WorkInfoManager;

//发起任务主列表
public class FlowDetailManagerImpl extends BizBaseCommonManager implements FlowDetailManager{
	
	@Override
	public List<?> queryFlowDetailByWorkId(Long work_info_id){
		List<?> ret_lst = null;
		//先查询是否有被驳回记录 
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("create_time",ISort.DESC));
		IFilter allfilter = FilterFactory.getSimpleFilter("work_info_id",work_info_id).appendAnd(FilterFactory.getSimpleFilter("approv_ret='2'"));
		fsp.setUserFilter(allfilter);
		List<?> lst_allflowList = this.getList(fsp);
		if(lst_allflowList!=null&&lst_allflowList.size()>0){
			//有驳回记录 ，取驳回记录日期之后的数据。
			FlowDetail flowDetail = (FlowDetail) lst_allflowList.get(0);
			Date date = flowDetail.getUpdate_time();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String update_time_str = sdf.format(date);
			IFilter refilter = FilterFactory.getSimpleFilter("work_info_id",work_info_id).appendAnd(FilterFactory.getSimpleFilter("update_time>'"+update_time_str+"'"));
			ret_lst = this.getList(refilter);
			if(ret_lst!=null&&ret_lst.size()==2){
				//判断是否是重复数据/bug
				try {
					Long user_id1=((FlowDetail)ret_lst.get(0)).getUser_id();
					Long workinfoid1=((FlowDetail)ret_lst.get(0)).getWork_info_id();
					Long user_id2=((FlowDetail)ret_lst.get(1)).getUser_id();
					Long workinfoid2=((FlowDetail)ret_lst.get(1)).getWork_info_id();
					if(user_id1!=null&&user_id1.equals(user_id2)&&workinfoid1!=null&&workinfoid1.equals(workinfoid2)){
						ret_lst.remove(1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			//无驳回记录
			IFilter filter = FilterFactory.getSimpleFilter("work_info_id",work_info_id);
			ret_lst = this.getList(filter);
			if(ret_lst!=null&&ret_lst.size()==2){
				//判断是否是重复数据/bug
				try {
					Long user_id1=((FlowDetail)ret_lst.get(0)).getUser_id();
					Long workinfoid1=((FlowDetail)ret_lst.get(0)).getWork_info_id();
					Long user_id2=((FlowDetail)ret_lst.get(1)).getUser_id();
					Long workinfoid2=((FlowDetail)ret_lst.get(1)).getWork_info_id();
					if(user_id1!=null&&user_id1.equals(user_id2)&&workinfoid1!=null&&workinfoid1.equals(workinfoid2)){
						ret_lst.remove(1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ret_lst;
	}
	
	@Override
	public List<FlowDetail> queryAllFlowDetailByWorkId(Long work_info_id){
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
		FlowConfigManager flowConfigManager = (FlowConfigManager) SpringHelper.getBean("flowConfigManager");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//先查询是否有被驳回记录 
		List<FlowDetail> retFlowDetails = new ArrayList<FlowDetail>();
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("create_time",ISort.ASC));
		IFilter allfilter = FilterFactory.getSimpleFilter("work_info_id",work_info_id);
		fsp.setUserFilter(allfilter);
		List<?> lst_allflowList = this.getList(fsp);
		
		//根据work_info_id查询work_type  根据work_type判断显示类型
		WorkInfo workInfo = (WorkInfo) workInfoManager.getObject(work_info_id);
		FlowConfig flowConfig = flowConfigManager.queryFlowConfigByWorkName(workInfo.getWork_type());
		boolean isshowname = true;
		if(flowConfig!=null&&flowConfig.getIsshowname()!=null&&flowConfig.getIsshowname().equals(Long.parseLong("1"))){
			isshowname = false;
		}
		
		//取出登录人 显示 姓名 。。
		for(Object o:lst_allflowList){
			FlowDetail flowDetail = (FlowDetail) o;
			User user = (User) userManager.getObject(flowDetail.getUpdate_user_id());
			//flowDetail.setCreate_user(user.getName());
			//流程显示用户组 不显示 用户名 
			if(!isshowname){
				flowDetail.setCreate_user(user.getUsergroup().getName());
			}else{
				flowDetail.setCreate_user(user.getName());
			}
			
			
			flowDetail.setUpdate_user(sdf.format(flowDetail.getUpdate_time()));
			String ret = flowDetail.getApprov_ret();
			if(ret==null||ret==""){
				ret="发起";
				flowDetail.setCreate_user(user.getName());//发起状态、取用户姓名 
			}else if(ret.equals("1")){
				ret="审批中";
			}else if(ret.equals("2")){
				ret="驳回";
			}else if(ret.equals("3")){
				ret="审核通过";
			}
			flowDetail.setApprov_ret(ret);
			retFlowDetails.add(flowDetail);
		}
		return retFlowDetails;
	}
	
	
	@Override
	public List<FlowDetail> queryAllFlowDetailByOrderSN(String order_sn){
		WorkInfoManager workInfoManager = (WorkInfoManager) SpringHelper.getBean("workInfoManager");
		WorkInfo workInfo = workInfoManager.queryWorkInfoByOrderSN(order_sn);
		List<FlowDetail> flowDetails = null;
		if(workInfo!=null){
			flowDetails = queryAllFlowDetailByWorkId(workInfo.getId());
		}
		return flowDetails;
	}
	
	
	//保存
	@Override
    public FlowDetail saveFlowDetail(FlowDetail flowDetail){
    	preSaveObject(flowDetail);
    	flowDetail.setUser_id(flowDetail.getUpdate_user_id());
    	flowDetail.setApprov_time(flowDetail.getUpdate_time());
    	this.saveObject(flowDetail);
    	return flowDetail;
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
