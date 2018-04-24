package com.cnpc.pms.personal.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.ApprovalDao;
import com.cnpc.pms.personal.dao.BusinessInfoDao;
import com.cnpc.pms.personal.dao.FerryPushDao;
import com.cnpc.pms.personal.entity.Approval;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.manager.ApprovalManager;
import com.cnpc.pms.personal.manager.DistCityManager;

public class ApprovalManagerImpl extends BizBaseCommonManager implements ApprovalManager{

	@Override
	public Approval findApproval(Long store_id, String type_id, String str_month) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("store_id="+store_id+" and type_id="+type_id+" and str_month='"+str_month+"'"));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (Approval)lst_vilage_data.get(0);
        }
        return null;
	}
	@Override
	public Approval saveApproval(Approval approval) {
		Approval findApproval = findApproval(approval.getStore_id(),approval.getTypeId(),approval.getStrMonth());
		if(findApproval==null){
			findApproval=new Approval();
		}
		findApproval.setCityName(approval.getCityName());
		findApproval.setStore_id(approval.getStore_id());
		findApproval.setName(approval.getName());
		findApproval.setStateType(approval.getStateType());
		findApproval.setTypeId(approval.getTypeId());
		findApproval.setStrMonth(approval.getStrMonth());
		findApproval.setStore_name(approval.getStore_name());
		preObject(findApproval);
		ApprovalManager approvalManager=(ApprovalManager)SpringHelper.getBean("approvalManager");
		approvalManager.saveObject(findApproval);
		return findApproval;
	}
	@Override
	public Map<String, Object> getApprovalList(QueryConditions conditions) {
		ApprovalDao approvalDao=(ApprovalDao)SpringHelper.getBean(ApprovalDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();
        for (Map<String, Object> map_where : conditions.getConditions()) {
            if ("store_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND store_name like '").append(map_where.get("value")).append("'");
            }
            if ("str_month".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND str_month='").append(map_where.get("value")).append("'");
			}
        }
        
        //获取当前登录人
        User sessionUser = null;
		if (null != SessionManager.getUserSession()
				&& null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession()
					.getSessionData().get("user");
		}
		UserManager userManager=(UserManager)SpringHelper.getBean("userManager");
		sessionUser=userManager.findUserById(sessionUser.getId());
		DistCityManager distCityManager =(DistCityManager)SpringHelper.getBean("distCityManager");
		List<DistCity> list = distCityManager.queryDistCityListByUserId(sessionUser.getId());
		if(list!=null&&list.size()>0){
			String cityName="";
			for (DistCity distCity : list) {
				cityName+=",'"+distCity.getCityname()+"'";
			}
			cityName=cityName.substring(1, cityName.length());
			sb_where.append(" and cityName in (").append(cityName).append(")");
		}else{
			sb_where.append(" and cityName in (").append("''").append(")");
		}
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "审核列表");
        map_result.put("data", approvalDao.getApprovalInfoList(sb_where.toString(), obj_page));
        return map_result;
	}
	@Override
	public Approval updateApproval(Approval approval) {
		Approval findApproval = findApproval(approval.getStore_id(),approval.getTypeId(),approval.getStrMonth());
		FerryPushDao ferryPushDao=(FerryPushDao)SpringHelper.getBean(FerryPushDao.class.getName());
		findApproval.setStateType(approval.getStateType());
		findApproval.setRemark(approval.getRemark());
		preObject(findApproval);
		ApprovalManager approvalManager=(ApprovalManager)SpringHelper.getBean("approvalManager");
		approvalManager.saveObject(findApproval);
		ferryPushDao.updateFerryPushStatus(approval.getStore_id(), approval.getStrMonth(), approval.getStateType());
		return approval;
	}
	@Override
	public void updateStoreNameById(Long store_id, String store_name) {
		if(store_id!=null){
			IFilter repFilter =FilterFactory.getSimpleFilter("store_id",store_id);
	    	List<?> rep_list = this.getList(repFilter);
			if(rep_list!=null&&rep_list.size()>0){
				for(int i=0;i<rep_list.size();i++){
					Approval approval = (Approval) rep_list.get(i);
					approval.setStore_name(store_name);
					preSaveObject(approval);
        			saveObject(approval);
				}
			}
		}
		
	}

}
