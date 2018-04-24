package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.Humanresources;

public interface ExpressDao extends IDAO{

	/**
	 * 店长级别查询快递信息
	 * @param month
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> queryExpressCount(String month);


	/**
	 * 店长级别通过们店主键(id) 和 月份查询快递信息
	 * @param storeId,month
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> queryExpressInfoByStoreIdAndMonth(Long storeId,String month);


	/**
	 * 城市总监级别查询快递信息
	 * @param month
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> queryExpressCount_cityLevel(String month);
	
	/**
	 * crm 查询当前登录国安侠a的 当月的  所有快递信息
	 * @param store_id
	 * @param employee_no
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryExpressByEmployeeNo(Long store_id,String employee_no,PageInfo pageInfo);
	
	 public Integer gettotalExpressCount(String employee_no);
	 
	 public Integer getTotalRelationCount(String employee_no);
	 
	 
	 /**
	  * CRM店长 根据门店 查询 快递代送排序 图表的方法  
	  * @param store_id
	  * @param date
	  * @return
	  */
	 public List<Map<String, Object>> queryExpressListByStoreId(Long store_id,List<Humanresources> humanresources);
	 
	 /**
	  * 
	  * TODO  crm-城市总监/运营经理查询快递
	  * 2017年6月20日
	  * @author gaobaolei
	  * @param city_id
	  * @param employee_id
	  * @param humanresources
	  * @return
	  */
	 public List<Map<String, Object>> queryExpressList_CSZJ_QYJL(Long city_id,String employee_id,String role);
	 
	 
	
	/**
	  * 
	  * TODO  crm-从预备数据城市总监/运营经理查询快递
	  * 2017年6月23日
	  * @author gaobaolei
	  * @param city_id
	  * @param employee_id
	  * @param humanresources
	  * @return
	  */
	 public List<Map<String, Object>> queryExpressList_CSZJ_QYJL_before(Long city_id,String employee_id,String role,String q_date);
	 
	 /**
	 * 
	 * TODO 查询上个月每个门店的快递代送总数 
	 * 2017年6月23日
	 * @author gaobaolei
	 * @param month
	 * @return
	 */
	public List<Map<String, Object>> getExpressOfPrevMonthOfStore(String month);
	
	/**
	 * 
	 * TODO 查询门店的快递代送 
	 * 2017年6月29日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public Integer getAllExpressOfStore(String storeId);


	/**
	 * 通過城市Id查詢該城市信息
	 * @param cityId
	 * @return
	 */
	public List<Map<String,Object>> queryExpressByCityId(Long cityId);
	 
}
