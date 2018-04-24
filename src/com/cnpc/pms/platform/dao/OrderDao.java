package com.cnpc.pms.platform.dao;

import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.dynamic.entity.EshopPurchaseDto;
import com.cnpc.pms.dynamic.entity.UserOperationStatDto;

import java.util.List;
import java.util.Map;

/**
 * APP接口数据库操作
 * Created by liu on 2016/8/23 0023.
 */
public interface OrderDao {

    Integer getOrderCount(String store_id,String employee_id,String year_month);

    Map<String,Object> queryOrderEmployeeCountByStore(PageInfo pageInfo, String store_id, String date_value);

    List<Map<String,Object>> getOrderEmployeeData(String store_id, String date_value);
    
    /**
     * crm 根据划片信息查询 当月的 订单详情列表
     */
    public Map<String, Object> queryOrderListByArea(String store_id,String area,PageInfo pageInfo);
    
    /**
     * 根据订单sn编号 查询明细信息 
     * @param order_sn
     * @return
     */
    public Map<String, Object> queryOrderInfoBySN(String order_sn);
    
    
    public Integer gettotalOrderCount(String store_id,String employee_id);
   
    
    public Map<String, Object> queryOrderListByEmployeeNo(String store_id,String employee_no,String area_names,PageInfo pageInfo);
    
    public List<Map<String, Object>> queryOrderFourMonth(String store_id,String areaInfo);
    
    
    public List<Map<String, Object>> queryOrderItemInfoById(String order_sn);
    
    
    /**
     * CRM店长 根据门店 查询 送单量排序  图表的方法  
     * @param store_id
     * @return
     */
    public List<Map<String, Object>> queryOrderCountByStoreId(String store_id);
    
    public List<Map<String, Object>> queryOrderListByArea(String store_id,String area_name);
    
    /**
     * 店长CRM 根据门店ID取得今年的 每个月的订单数及金额
     * @param store_id
     */
    public List<Map<String, Object>> queryOrderCountByMonthStoreId(String store_id);
    
    
    /**
     * APP 个人中心 不分片 查询订单 图表 
     * @param store_id
     * @param employee_no
     * @return
     */
    public List<Map<String, Object>> queryOrderFiveMonthOrderApp(String store_id,String employee_no);
    
    
    /**
     * 查询统计图 近五个月的数据
     * @return
     */
    public List<Map<String, Object>> queryOrderFiveMonth(String store_id,String areaInfo);
    
    /**
     * 
     * TODO crm-城市总监、区域经理查询各个门店的订单等信息 
     * 2017年6月21日
     * @author gaobaolei
     * @param employee_no
     * @param role
     * @param cityId
     * @return
     */
    public List<Map<String, Object>> queryOrderListOfStore_CSZJ_QYJL(Object store);
    
    /**
     * 
     * TODO crm-城市总监、区域经理查询当年的订单 
     * 2017年6月21日
     * @author gaobaolei
     * @param store
     * @return
     */
    public List<Map<String, Object>> queryOrderCountOfMonth_CSZJ_QYJL(Object store);
    
    /**
     * 
     * TODO 查询门店的所有订单 
     * 2017年6月29日
     * @author gaobaolei
     * @param storeId
     * @return
     */
    public Map<String, Object> getAllOrderOfStore(String storeId);
    
    /**
     * 
     * TODO 查找订单的物流信息 
     * 2017年9月18日
     * @author gaobaolei
     * @param order_id
     * @param status
     * @return
     */
    public Map<String, Object> getOrderFlow(String order_id,String status);
    
    
    public List<Map<String, Object>> queryOrderSetInterval(String store_ids,String nowDate);
    
    /**
     * 
     * TODO app混片消费记录
     * 2017年11月7日
     * @author gaobaolei
     * @param employee_no
     * @param month
     * @return
     */
    public Map<String, Object> queryOrderOfArea(String employee_no,PageInfo pageInfo,String order_sn);
    
    /**
     * 
     * TODO  根据订单id查询订单
     * 2017年11月8日
     * @author gaobaolei
     * @param orderid
     * @return
     */
    public Map<String, Object> getOrderByOrderSN(String order_sn);
    

    /**
     * 
     * TODO 查询订单地址
     * 2017年11月29日
     * @author gaobaolei
     * @param addressId
     * @return
     */
    public List<Map<String, Object>> getOrderAddressByAddressId(String addressId);
    
    /**
     * 
     * TODO  查询订单订单详情
     * 2017年11月29日
     * @author gaobaolei
     * @param orderSn
     * @return
     */
    public List<Map<String, Object>> getOrderItemByOrderId(String orderSn);
    

    /**
	 * 
	 * TODO 查询城市总监或区域经理管辖的门店 的当日成交额,成交量,用户成交量
	 * 2017年11月29日
	 * @author zhangli
	 * @param where
	 * @return
	 */
	public List<Map<String, Object>> getDailyOrderOfCurDay(String cityNo,DynamicDto dd);
	
	/**
	 * 
	 * TODO 查询gemini上的员工 
	 * 2017年11月30日
	 * @author gaobaolei
	 * @param id
	 * @return
	 */
	public Map<String, Object> getEmployeeByIdOfGemini(String id);
	
	/**
	 * 
	 * TODO 查询当月订单地图分布量 
	 * 2017年12月12日
	 * @author gaoll
	 * @param id
	 * @return
	 */
    public List<Map<String, Object>> getLagLatByOrder(String string_sql,String beginDate,String endDate);
    
    /**
	 * 
	 * TODO 查询当月订单地图分布最大量及最小量
	 * 2017年12月13日
	 * @author gaoll
	 * @param id
	 * @return
	 */
    public List<Map<String, Object>> getLagLatCount(String string_sql,String beginDate,String endDate);
    
    
    /**
	 * 
	 * TODO 查询当月订单营业额地图分布量 
	 * 2017年12月12日
	 * @author gaoll
	 * @param id
	 * @return
	 */
    public List<Map<String, Object>> getLagLatTradingPriceByOrder(String string_sql,String beginDate,String endDate);

    /**
	 * 
	 * TODO 查询当月订单地图分布量 
	 * 2017年12月13日
	 * @author gaoll
	 * @param id
	 * @return
	 */
    public List<Map<String, Object>> getLagLatTradingPriceCount(String string_sql,String beginDate,String endDate);
    
    /**
	 * TODO 查询当月用户量分布量 
	 * 2018年3月13日
	 * @author cityNo
	 * @param id
	 * @return
	 */
    public List<Map<String,Object>> getLatlngCustomerByCity(String cityNo,String beginDate,String endDate);
    
    /**
	 * TODO 查询当月用户量分布量 
	 * 2018年3月14日
	 * @author gaoll
	 * @param cityNo
	 * @return
	 */
    public List<Map<String,Object>> getLatlngCustomerCountByCity(String cityNo,String beginDate,String endDate);
    
    /**
	 * TODO 查询当月用户量分布量 
	 * 2018年3月14日
	 * @author gaoll
	 * @param storeNo
	 * @return
	 */
    public List<Map<String,Object>> getLatlngCustomerByStore(String storeNo,String beginDate,String endDate);
    
    /**
	 * TODO 查询当月用户量分布量 
	 * 2018年3月14日
	 * @author gaoll
	 * @param storeNo
	 * @return
	 */
    public List<Map<String,Object>> getLatlngCustomerCountByStore(String storeNo,String beginDate,String endDate);
    
    /**
     * 总部查看：根据日期按E店交易额排名
     */
    public List<Map<String, Object>> queryTradeByEshop(DynamicDto dd);

    /**
	 * 
	 * TODO 查询当日的所有门店的成交额 
	 * 2017年12月14日
	 * @author zhangli
	 * @return
	 */
	List<Map<String, Object>> getDailyStoreOrderOfCurDay(DynamicDto dd,List<Map<String, Object>> cityNO,List<Map<String, Object>> provinceNO);
	
	/**
	 * 
	 * TODO 根据员工编号查询gemini上的员工 
	 * 2017年12月15日
	 * @author gaolei
	 * @param id
	 * @return
	 */
	public Map<String, Object> getEmployeeByEmployeeIdOfGemini(String id);
	 /**
     * 
     * TODO app混片消费记录(店长权限)
     * 2017年12月21日
     * @author gaolei
     * @param employee_no
     * @param 
     * @return
     */
    public Map<String, Object> queryOrderOfAreaForApp(String employee_no,PageInfo pageInfo,String order_sn);
	
	/**
	 * 
	 * TODO 查询门店E店gmv 
	 * 2017年12月20日
	 * @author gaobaolei
	 * @param platformId
	 * @return
	 */
	public Map<String, Object> selectEStoreRankingOfStore(DynamicDto dynamicDto,PageInfo pageInfo);
	
    /**
	 * 
	 * TODO 总部查看：商品排名
	 * 2017年12月25日
	 * @author cps
	 * @return
	 */
	List<Map<String, Object>> queryTradeByProduct(DynamicDto dd,List<Map<String, Object>> cityNO,List<Map<String, Object>> provinceNO);
	
	/**
	 * 查询gemini库店铺商业模式基础类型
	 * @return
	 */
	public List<Map<String, Object>> queryBusinessModelBaseType();
	
	/**
	 * 查询gemini库店铺商业模式基础数据
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryEshopModelBaseInfo(List<String> eshopList, EshopPurchaseDto shopPurchaseDto, PageInfo pageInfo);
	
	/**
	 * 查询gemini库新客数据
	 * @param userOperationStatDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryUserOpsNewCusStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo);
	
	/**
	 * 查询gemini库付费数据
	 * @param userOperationStatDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryUserOpsPayStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo);
	
	public List<Map<String, Object>> exportUserOpsNewCusStat(UserOperationStatDto userOperationStatDto);
	
	public List<Map<String, Object>> exportUserOpsPayStat(UserOperationStatDto userOperationStatDto);
	
	
	public List<Map<String, Object>> queryOrderByCustomerIdTop20(String customer_id);
	
	/**
	 * 查询gemini库门店用户量
	 * @param dd
	 * @param pageInfo
	 * @return
	 */
	Map<String, Object> queryStoreCustmerCount(DynamicDto dd, List<Map<String, Object>> cityNO,List<Map<String, Object>> provinceNO,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO  门店每月消费客户量排名
	 * 2018年1月26日
	 * @author gaobaolei
	 * @param plateformId
	 * @return
	 */
	public Map<String, Object> queryStoreCustomerOfMonth(String plateformId,PageInfo pageInfo);
	
	/**
	 * 通过订单号查询所在位置
	 * @param order_sn
	 * @return
	 */
	public Map<String, Object> queryPositionByOrdersn(String order_sn);
	
	/**
	 * 查询当日实时累计用户量
	 * @param dd
	 * @param cityNO
	 * @param provinceNO
	 * @return
	 */
	List<Map<String, Object>> getDailyUserOfCurDay(DynamicDto dd,List<Map<String, Object>> cityNO,List<Map<String, Object>> provinceNO);
	/**
	 * 获得最新的订单所在的城市
	 * @param dd
	 * @param cityNO
	 * @param provinceNO
	 * @return
	 */
	List<Map<String, Object>> getDailyFirstOrderCity();

}
