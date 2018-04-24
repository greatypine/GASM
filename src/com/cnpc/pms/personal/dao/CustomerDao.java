package com.cnpc.pms.personal.dao;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerDao extends IDAO {

	Map<String, Object> queryAchievements(String employee_no, String start_date, String end_date, PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 获取片区的 用户数
	 * 2017年4月14日
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public Long getHouseOfArea(String employeeNo,Long area_id);
	
	/**
	 * 
	 * TODO 根据电话获取用户画像信息
	 * 2017年5月4日
	 * @author gaobaolei
	 * @param phone
	 * @return
	 */
	public List<Map<String,Object>> getCustomerByPhone(String phone);
	
	/**
	 * 
	 * TODO 查询今年的用户画像 
	 * 2017年5月12日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> getCustomerOfYear(Long storeId);
	
	
	/**
	 * 
	 * TODO 查询门店片区的用户画像 
	 * 2017年5月12日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> getCustomerOfArea(Long storeId,String query_date);
	
	/**
	 * 
	 * 
	 * TODO 查询门店所有员工的当月用户画像 
	 * 2017年5月15日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> getCustomerOfEmployee(Long storeId,String query_date);
	
	/**
	 * 
	 * TODO 查询国安侠五个月的用户画像 
	 * 2017年5月15日
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public List<Map<String, Object>> queryCustomerOfEmployee_month(String employeeNo,Long area_id);
	
	/**
	 * 添加或修改街道的排序
	 * @param idString
	 */
	void updateCustomerSortById(String idString);
	
	/**
	 * 清空街道的排序
	 * @param idString
	 */
	void deleteCustomerSortById();
	
	/**
	 * 
	 * TODO 查询运营经理当年的用户画像 
	 * 2017年6月15日
	 * @author gaobaolei
	 * @param employeeId
	 * @param role (QYJL/CSZJ)
	 * @return
	 */
	public List<Map<String, Object>> getCustomerOfYear_CSZJ_QYJL(String employeeId,Long cityId,String role);
	
	/**
	 * 
	 * TODO  查询运营经理所管辖各个门店的用户画像总数
	 * 2017年6月16日
	 * @author gaobaolei
	 * @param employeeId
	 * @param role
	 * @return
	 */
	public List<Map<String, Object>> getCustomerOfStore_CSZJ_QYJL(String employeeId,Long cityId,String role);
	
	/**
	 * 
	 * TODO 查询运营经理所管辖各个国安侠的用户画像 
	 * 2017年6月19日
	 * @author gaobaolei
	 * @param employeeId
	 * @param role
	 * @return
	 */
	public List<Map<String, Object>> getCustomerOfEmployee_CSZJ_QYJL(String employeeId,Long cityId,String role);
	
	/**
	 * 
	 * TODO 查询上个月每个门店的单体画像总数 
	 * 2017年6月23日
	 * @author gaobaolei
	 * @param month
	 * @return
	 */
	public List<Map<String, Object>> getCustomerOfPrevMonthOfStore(String month);
	
	
	/**
	 * 
	 * TODO 从已预备数据查询运营经理当年的用户画像 
	 * 2017年6月23日
	 * @author gaobaolei
	 * @param employeeId
	 * @param role (QYJL/CSZJ)
	 * @return
	 */
	public List<Map<String, Object>> getCustomerOfYear_CSZJ_QYJL_before(String employeeId,Long cityId,String role);
	
	
	/**
	 * 
	 * TODO 从预备数据查询运营经理所管辖各个门店的用户画像总数
	 * 2017年6月23日
	 * @author gaobaolei
	 * @param employeeId
	 * @param role
	 * @return
	 */
	public List<Map<String, Object>> getCustomerOfStore_CSZJ_QYJL_before(String employeeId,Long cityId,String role,String q_date);
	
	/**
	 * 
	 * TODO 查询门店所有的单体画像 
	 * 2017年6月29日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public Integer getAllCustomerOfStore(String storeId);
	
	/**
	 * 
	 * TODO 获取店长 
	 * 2017年9月25日
	 * @author gaobaolei
	 * @param employeeId
	 * @return
	 */
	public Map<String,Object> queryStoreKeeper(Long store_id);
	
	/**
	 * 
	 * TODO 搜索唯一的用户房间关系 
	 * 2017年10月10日
	 * @author gaobaolei
	 * @param houseId
	 * @return
	 */
	public List<Map<String, Object>> selectUniqueHouseCustomer(Long houseId);
	
	/**
	 * 
	 * TODO 查询小区下的 所有用户画像
	 * 2018年1月9日
	 * @author gaobaolei
	 * @param idsStr
	 * @return
	 */
	public List<Map<String, Object>> selectCustomerByTinyVillage(String idsStr);
	
	/**
	 * 
	 * TODO 修改客户画像Address 
	 * 2018年1月12日
	 * @author gaobaolei
	 * @param customer
	 */
	public void updateCustomerAddress(Customer customer);
	
}
