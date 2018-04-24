package com.cnpc.pms.personal.dao;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.IPage;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.Relation;

import java.util.List;
import java.util.Map;

public interface RelationDao extends IDAO {

	Map<String,Object> queryRelation(List<Map<String,Object>> lst_condition, IPage pageInfo);
	
	
	public Map<String, Object> queryRelationList(IPage pageInfoj,UserDTO userDTO,String sbfCondition);
	
	public List<Customer> queryRelationListByUserCard(Long store_id,String employee_no,String cardtype);
	
	
	public Relation queryMaxDateCount(Long customer_id);
	

	/**
	 * 
	 * TODO 查询柱状图拜访记录 （crm）
	 * 2017年4月11日
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public  List<Map<String, Object>> findRelation_chart_crm(String employeeNo,Long area_id);
	
	/**
	 * 
	 * TODO 查询最新拜访记录（crm） 
	 * 2017年4月11日
	 * @author gaobaolei
	 * @param page
	 * @param employeeNo
	 * @return
	 */
	public Map<String, Object> findRelation_allList_crm(PageInfo page,String employeeNo,Long area_id);
	
	/**
	 * 
	 * TODO 查询个人中心拜访记录
	 * 2017年4月13日
	 * @author gaobaolei
	 * @param page
	 * @param employeeNo
	 * @return
	 */
	public Map<String, Object> findrelation_employee_crm(PageInfo page,String employeeNo);
	
	/**
	 * 
	 * TODO 查询用户的所有拜访记录
	 * 2017年4月13日
	 * @author gaobaolei
	 * @param page
	 * @param customer_id
	 * @return
	 */
	public Map<String,Object> findRelation_customer_crm(PageInfo page,Long customer_id);
	


	public List<Customer> queryRelationListForApp(Long store_id,String employee_no,String cardtype,StringBuffer sbfCondition);
	
	/**
	 * 
	 * TODO 查询国安侠负责的片区的拜访记录 
	 * 2017年4月24日
	 * @author gaobaolei
	 * @param pager
	 * @param employeeNo
	 * @return
	 */
	public Map<String, Object> findRelation_area_employee_app(PageInfo pager,String employeeNo,Long area_id);
	
	/**
	 * 
	 * TODO 手机端 查询用户的拜访记录
	 * 2017年5月9日
	 * @author gaobaolei
	 * @param page
	 * @param customer_id
	 * @return
	 */
	public Map<String,Object> findRelation_customer_crm_app(PageInfo page,Long customer_id);
	
	/**
	 * 
	 * TODO 查询门店今年的拜访记录 
	 * 2017年5月12日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> getRelationOfYear(Long storeId);
	
	/**
	 * 
	 * TODO 查询门店所有片区的拜访记录 
	 * 2017年5月12日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> getRelationOfArea(Long storeId,String query_date);
	
	/**
	 * 
	 * TODO查询门店所有员工的拜访记录 
	 * 2017年5月12日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> getRelationOfEmployee(Long storeId,String query_date);
	
	/**
	 * 
	 * TODO 查询员工五个月的拜访记录 
	 * 2017年5月15日
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public List<Map<String, Object>> getRelationOfEmployee_month(String employeeNo,Long area_id);
	/**
	 * 
	 * TODO 个人动态拜访记录
	 * 2017年5月19日
	 * @author gaobaolei
	 * @param pageInfo
	 * @param employeeNo
	 * @return
	 */
	public Map<String, Object> findrelation_employee_crm_app(PageInfo pageInfo, String employeeNo);
	
	/**
	 * 
	 * TODO 查询国安侠近五个月的拜访记录 
	 * 2017年5月19日
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public List<Map<String, Object>> findRelationOfEmployee_byFiveMonth(String employeeNo);
	
	public List<Map<String, Object>> selectAllEmployeeOfStore(Long storeId);
	
	/**
	 * 
	 * TODO 查询国安侠拜访记录总数 
	 * 2017年5月25日
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public Integer getRelationCount(String employeeNo);
	
	/**
	 * 
	 * TODO 查询门店每个月的拜访记录总数 
	 * 2017年5月25日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public Integer getRelationCount(Long storeId);
	
	/**
	 * 
	 * TODO crm 运营经理查询区域内所有门店当年的拜访记录 
	 * 2017年6月16日
	 * @author gaobaolei
	 * @param employeeId
	 * @param role (CSZJ/QYJL)
	 * @return
	 */
	public List<Map<String, Object>> getRelationOfYear_CSZJ_QYJL(String  employeeId,Long cityId,String role);
	
	/**
	 * 
	 * TODO  运营经理查询所管辖各个门店的拜访记录
	 * 2017年6月16日
	 * @author gaobaolei
	 * @param employeeId
	 * @param role
	 * @return
	 */
	public List<Map<String, Object>> getRelationOfStore_CSZJ_QYJL(String  employeeId,Long cityId,String role);
	
	/**
	 * 
	 * TODO  运营经理查询所管辖各个员工的的拜访记录
	 * 2017年6月16日
	 * @author gaobaolei
	 * @param employeeId
	 * @param role 
	 * @return
	 */
	public List<Map<String, Object>> getRelationOfEmployee_CSZJ_QYJL(String  employeeId,Long cityId,String role);
	
	/**
	 * 
	 * TODO 查询上个月每个门店的拜访记录总数 
	 * 2017年6月23日
	 * @author gaobaolei
	 * @param month
	 * @return
	 */
	public List<Map<String, Object>> getRelationOfPrevMonthOfStore(String month);
	
	/**
	 * 
	 * TODO crm 从预备数据运营经理查询区域内所有门店当年的拜访记录 
	 * 2017年6月23日
	 * @author gaobaolei
	 * @param employeeId
	 * @param role (CSZJ/QYJL)
	 * @return
	 */
	public List<Map<String, Object>> getRelationOfYear_CSZJ_QYJL_before(String  employeeId,Long cityId,String role);
	
	
	/**
	 * 
	 * TODO  从预备数据运营经理查询所管辖各个门店的拜访记录
	 * 2017年6月23日
	 * @author gaobaolei
	 * @param employeeId
	 * @param role
	 * @return
	 */
	public List<Map<String, Object>> getRelationOfStore_CSZJ_QYJL_before(String  employeeId,Long cityId,String role,String q_date);
	
	/**
	 * 
	 * TODO 获取门店的所有拜访记录
	 * 2017年6月29日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public Integer getAllRelationOfStore(String storeId);
}