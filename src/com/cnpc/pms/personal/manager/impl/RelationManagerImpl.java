package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.dao.CustomerDao;
import com.cnpc.pms.personal.dao.RelationDao;
import com.cnpc.pms.personal.entity.BeforeDateCustomer;
import com.cnpc.pms.personal.entity.BeforeDateRelation;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.Relation;
import com.cnpc.pms.personal.entity.ViewAddressCustomer;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.RelationManager;
import com.cnpc.pms.personal.manager.ViewAddressCustomerManager;
import com.cnpc.pms.platform.manager.OrderManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.h2.store.PageStoreInDoubtTransaction;

import org.hibernate.SQLQuery;


/**
 * 客户关系实现类
 */
public class RelationManagerImpl extends BaseManagerImpl implements RelationManager {
	/**
	 * pc查询列表 用户拜访记录 列表 
	 */
	@Override
    public Map<String, Object> queryRelationList(QueryConditions condition) {
		UserManager manager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = manager.getCurrentUserDTO();
		//Long store_id = userDTO.getStore_id();
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		String name = null;
		String mobilephone = null;
		for(Map<String, Object> map : condition.getConditions()){
			if("name".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				name = map.get("value").toString();
			}
			if("mobilephone".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				mobilephone = map.get("value").toString();
			}
		}
		StringBuffer sbfCondition = new StringBuffer();
		sbfCondition.append(" where 1=1 and status = 0 ");
		if(name!=null){
			sbfCondition.append(" and a.name like '%"+name+"%'");
		}
		if(mobilephone!=null){
			sbfCondition.append(" and a.mobilephone like '%"+mobilephone+"%'");
		}
		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		returnMap = relationDao.queryRelationList(condition.getPageinfo(),userDTO,sbfCondition.toString());
		return returnMap;
    }
	
	
	
	 /**
     * APP拜访记录中 根据电话和姓名  查询拜访记录列表
     */
	@Override
    public Result queryRelationListForApp(Long store_id,String employee_no,String cardtype,String name,String mobilephone) {
		Result result = new Result();
		StringBuffer sbfCondition = new StringBuffer();
		sbfCondition.append(" where 1=1 and status = 0 ");
		if(name!=null&&name.length()>0){
			sbfCondition.append(" and a.name like '%"+name+"%'");
		}
		if(mobilephone!=null&&mobilephone.length()>0){
			sbfCondition.append(" and a.mobilephone like '%"+mobilephone+"%'");
		}
		CustomerManager customerManager = (CustomerManager) SpringHelper.getBean("customerManager");
        ViewAddressCustomerManager viewAddressCustomerManager = (ViewAddressCustomerManager)SpringHelper.getBean("viewAddressCustomerManager");
		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		List<Customer> lst_data = relationDao.queryRelationListForApp(store_id, employee_no, cardtype, sbfCondition);
		List<Customer> returnMap = new ArrayList<Customer>();
		for(Customer cust:lst_data){
			Customer customer = (Customer) customerManager.getObject(cust.getId());
			customer.setMinor_num(cust.getMinor_num());
			customer.setWork_overtime(cust.getWork_overtime());
			customer.setName(cust.getName());
			customer.setMobilephone(cust.getMobilephone());
			
			
			 FSP fsp = new FSP();
	         fsp.setSort(SortFactory.createSort("house_id", ISort.DESC));
	         fsp.setUserFilter(FilterFactory.getSimpleFilter("customer_id",customer.getId()));
	         List<?> lst_address = viewAddressCustomerManager.getList(fsp);
	         if(lst_address != null && lst_address.size() > 0){
	        	 customer.setCustomer_address((ViewAddressCustomer)lst_address.get(0));
	         }
			returnMap.add(customer);
		}
		result.setData(returnMap);
		result.setCode(CodeEnum.success.getValue());
        result.setMessage(CodeEnum.success.getDescription());
		
		return result;
    }
	
	
	
	/**
	 * 根据customer_id查找拜访记录 
	 */
	@Override
	public List<Relation> findRelationsBycustomerId(Long customer_id) {
        CustomerManager customerManager = (CustomerManager)SpringHelper.getBean("customerManager");
        IFilter iFilter =FilterFactory.getSimpleFilter(" status = 0 and customer_id = "+customer_id);
        List<Relation> lst_relations_list = (List<Relation>) this.getList(iFilter);
        return lst_relations_list;
    }
	
	
	/**
	 * APP点击 用户卡中的 拜访记录数字 查询  
	 */
	@Override
	public Result queryRelationListByUserCard(Long store_id,String employee_no,String cardtype){
		Result result = new Result();
		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		List<Customer> lst_relation_grp = relationDao.queryRelationListByUserCard(store_id, employee_no, cardtype);
		result.setData(lst_relation_grp);
		result.setCode(CodeEnum.success.getValue());
        result.setMessage(CodeEnum.success.getDescription());
		return result;
	}
	
	
	
	/**
	 * 用户画像卡中 点击数字 查询
	 */
    @Override
    public Map<String, Object> queryRelation(QueryConditions queryConditions) {
        List<Map<String,Object>> lst_conditions = queryConditions.getConditions();
        RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
        return relationDao.queryRelation(lst_conditions,queryConditions.getPageinfo());
    }

    @Override
    public Relation findRelationById(Long id) {
        CustomerManager customerManager = (CustomerManager)SpringHelper.getBean("customerManager");
        Relation relation = (Relation)this.getObject(id);
        relation.setCustomer((Customer)customerManager.getObject(relation.getCustomer_id()));
        relation.getCustomer().setRelations(null);
        return relation;
    }

	
	@Override
	public Map<String, Object> findRelation_newestList_crm(String employeeNo,Integer pageSize,Integer currentPage,Long area_id) {
        RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrentPage(currentPage);
        pageInfo.setRecordsPerPage(pageSize);
        
        return relationDao.findRelation_allList_crm(pageInfo, employeeNo,area_id);
	}

	
	@Override
	public Map<String, Object> findRelation_chart_crm(String employeeNo,Long area_id) {
		 Map<String, Object> maps =new HashMap<String, Object>();
		 OrderManager orderManager = (OrderManager) SpringHelper.getBean("orderManager");
		 List<Map<String, Object>> orderList = orderManager.queryOrderFourMonth(employeeNo,area_id);
		 RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		 List<Map<String, Object>> relationList = relationDao.findRelation_chart_crm(employeeNo,area_id);
		 maps.put("orderList", orderList);
		 maps.put("relationList", relationList);
	     return maps;
	}

	
	@Override
	public Map<String, Object> findRelation_employee_crm(String employeeNo, Integer pageSize, Integer currentPage) {
		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrentPage(currentPage);
        pageInfo.setRecordsPerPage(pageSize);
        Map<String, Object> result = new HashMap<String,Object>();
        result = relationDao.findrelation_employee_crm(pageInfo, employeeNo);
        Integer total = relationDao.getRelationCount(employeeNo);
        result.put("total", total);
		return result;
	}

	
	@Override
	public Map<String, Object> findRelation_customer_crm(Long customer_id, Integer pageSize, Integer currentPage) {
		
		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrentPage(currentPage);
        pageInfo.setRecordsPerPage(pageSize);
		return relationDao.findRelation_customer_crm(pageInfo, customer_id);
	}



	
	@Override
	public Relation delRelation(int id) {
		Relation rt = (Relation)this.getObject(id);
		rt.setStatus(1);
		preSave(rt);
		this.saveObject(rt);
		return rt;
	}



	
	@Override
	public Map<String, Object> getRelationOfEmployee(Long storeId,String query_date) {
		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("relation",relationDao.getRelationOfEmployee(storeId,query_date));
		return result;
	}



	
	@Override
	public Map<String, Object> getRelationOfEmployee_CSZJ_QYJL(String employeeId,Long city_id, String query_date,String role) {
		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("relation",relationDao.getRelationOfEmployee_CSZJ_QYJL(employeeId,city_id,role));
		return result;
	}



	@Override
	public void saveBeforeDateRelation_timedTask() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH,-1);
		String prev_month = simpleDateFormat.format(calendar.getTime());
		Map<String,Object> result = new HashMap<String,Object>();
		RelationDao relationDao = (RelationDao) SpringHelper.getBean(RelationDao.class.getName());
		List<Map<String, Object>> realtionList = relationDao.getRelationOfPrevMonthOfStore(prev_month);
		simpleDateFormat = new SimpleDateFormat("yyyy-M");
		for(int i=0;i<realtionList.size();i++){
			BeforeDateRelation bDateRelation = new BeforeDateRelation();
			Object amount = realtionList.get(i).get("amount");
			Object store_id = realtionList.get(i).get("store_id");
			Object store_name = realtionList.get(i).get("store_name");
			bDateRelation.setAmount(amount==null?0:Integer.parseInt(amount.toString()));
			bDateRelation.setBind_date(simpleDateFormat.format(calendar.getTime()));
			bDateRelation.setStore_id(Long.parseLong(store_id.toString()));
			bDateRelation.setStore_name(store_name.toString());
			bDateRelation.setCreate_time(date);
			saveObject(bDateRelation);
		}
		
	}
}
