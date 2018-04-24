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
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.UserAdvice;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.UserAdviceManager;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Session;
import javax.swing.Spring;

/**
 * 
 * 类名: UserAdviceManagerImpl  
 * 功能描述: 门店用户建议的接口的实现类
 * 作者: 常鹏飞  
 * 时间: 2016-3-1   
 *   
 */
public class UserAdviceManagerImpl extends BizBaseCommonManager implements UserAdviceManager {

	
	
	/**
	 * 用户意见
	 * @param  : UserAdvice
	 * @return : Result
	 */
	@Override
	public Result userOpinion(UserAdvice userAdvice) {
		
		Result result = new Result();
		
		//门店id
		Long store_id = userAdvice.getStore_id();
		String sex = userAdvice.getSex_2();
		String reg_num = userAdvice.getReg_num_2();
		String status = userAdvice.getStatus_2();
		String createUserId = userAdvice.getCreateUserId();
		
		if( store_id == null || StringUtils.isEmpty(userAdvice.getName()) || StringUtils.isEmpty(userAdvice.getMobilephone())
				|| StringUtils.isEmpty(userAdvice.getReg_num_2()) || StringUtils.isEmpty(userAdvice.getSex_2())){
			
			result.setCode(CodeEnum.paramErr.getValue());
			result.setMessage(CodeEnum.paramErr.getDescription());
			return result;
		}
		
		int sex_3 = Integer.valueOf(sex);
		int reg_num_3 = Integer.valueOf(reg_num);
		int status_3 = Integer.valueOf(status);
		
		userAdvice.setSex(sex_3);
		userAdvice.setReg_num(reg_num_3);
		userAdvice.setStatus(status_3);
		userAdvice.setCreate_user_id(Long.valueOf(createUserId));
		
		Date date = new Date();
		userAdvice.setCreate_time(date);
		
		UserAdviceManager userAdviceManager = (UserAdviceManager)SpringHelper.getBean("userAdviceManager");
		
		userAdviceManager.save(userAdvice);
		
		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
		
		return result;
	}

	@Override
	public Result qieryUserAdviceList(PageInfo pageInfo, UserAdvice userAdvice) {
		Result result = new Result();

        //门店id
        Long store_id = userAdvice.getStore_id();

        if( store_id == null){
            result.setCode(CodeEnum.paramErr.getValue());
            result.setMessage(CodeEnum.paramErr.getDescription());
            return result;
        }
        FSP fsp  = new FSP();
        fsp.setSort(SortFactory.createSort("create_time", ISort.DESC)
                .appendSort(SortFactory.createSort("update_time", ISort.ASC)));
        fsp.setUserFilter(FilterFactory.getSimpleFilter("store_id = "+store_id));
        fsp.setPage(pageInfo);
        //该门店下的所有的快递
        List<UserAdvice> userAdviceList = (List<UserAdvice>)super.getList(fsp);
        if( userAdviceList != null && userAdviceList.size() > 0 ){
        	UserManager userManager=(UserManager)SpringHelper.getBean("userManager");
        	 List<UserAdvice> userAdviceListData=new ArrayList<UserAdvice>();
        	 for (UserAdvice userAdvice2 : userAdviceList) {
        		 User user = userManager.findUserById(userAdvice2.getCreate_user_id());
        		 if(user!=null){
        			 userAdvice2.setCreate_user(user.getName());
        		 }
        		 userAdviceListData.add(userAdvice2);
			}
            result.setUserAdviceList(userAdviceListData);
            result.setCode(CodeEnum.success.getValue());
            result.setMessage(CodeEnum.success.getDescription());
        }else{
            result.setCode(CodeEnum.nullData.getValue());
            result.setMessage(CodeEnum.nullData.getDescription());
        }
        return result;
	}

	@Override
	public UserAdvice doSaveUserAdvice(UserAdvice userAdvice) {
		UserAdviceManager userAdviceManager=(UserAdviceManager)SpringHelper.getBean("userAdviceManager");
		User sessionUser = null;
		if(null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User)SessionManager.getUserSession().getSessionData().get("user");
		}
		userAdvice.setStore_id(sessionUser.getStore_id());
		preObject(userAdvice);
		userAdviceManager.saveObject(userAdvice);
		return null;
	}

	@Override
	public UserAdvice findUserAdviceById(Long id) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("id",id));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (UserAdvice)lst_vilage_data.get(0);
        }
        return null;
	}

	@Override
	public Map<String, Object> getUserAdviceDataList(QueryConditions conditions) {
		// TODO Auto-generated method stub
		 //分页对象
        PageInfo pageInfo = conditions.getPageinfo();
        StringBuilder sb_where = new StringBuilder();
        for (Map<String, Object> map_where : conditions.getConditions()) {
            if ("name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" and name like '").append(map_where.get("value")).append("'");
            }
            if ("mobilephone".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND mobilephone like '").append(map_where.get("value")).append("'");
			}
        }
        User sessionUser = null;
		if(null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User)SessionManager.getUserSession().getSessionData().get("user");
		}
		if(sessionUser.getStore_id()!=null&&!"".equals(sessionUser.getStore_id())){
			sb_where.append(" and store_id="+sessionUser.getStore_id());
		}else{
			//取得当前登录人 所管理城市
	  		String cityssql = "";
	  		UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
	  		List<DistCity> distCityList = userManager.getCurrentUserCity();
	  		if(distCityList!=null&&distCityList.size()>0){
	  			for(DistCity d:distCityList){
	  				cityssql += "'"+d.getCityname()+"',";
	  			}
	  			cityssql=cityssql.substring(0, cityssql.length()-1);
	  		}
	  		if(cityssql!=""&&cityssql.length()>0){
	  			//sb_where.append(" and sto.city_name in ("+cityssql+")");
	  			StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
	  			List<Store> list = storeManager.findStoreByCityData(cityssql);
	  			if(list!=null&&list.size()>0){
	  				String idString="";
	  				for (Store store : list) {
	  					idString+=store.getStore_id()+",";
					}
	  				if(idString.length()>1){
	  					idString=idString.substring(0, idString.length()-1);
	  					sb_where.append(" and store_id in ("+idString+") ");
	  				}
	  			}
	  			
			}else{
				sb_where.append(" and 0=1 ");
			}
		}
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();
        FSP fsp  = new FSP();
        fsp.setSort(SortFactory.createSort("create_time", ISort.DESC)
                .appendSort(SortFactory.createSort("update_time", ISort.ASC)));
      fsp.setUserFilter(FilterFactory.getSimpleFilter("1=1 "+sb_where.toString()));
        List<UserAdvice> userAdviceList1 = (List<UserAdvice>)super.getList(fsp);
        fsp.setPage(pageInfo);
        //该门店下的所有的快递
        List<UserAdvice> userAdviceList = (List<UserAdvice>)super.getList(fsp);
        pageInfo.setTotalRecords(userAdviceList1.size());
        map_result.put("pageinfo", pageInfo);
        map_result.put("header", "客户意见列表");
        map_result.put("data", userAdviceList);
		return map_result;
	}

}
