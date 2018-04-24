package com.cnpc.pms.platform.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.inter.common.Result;


import java.util.List;
import java.util.Map;

/**
 * 订单表
 * Created by liuxi on 2016/12/19.
 */
public interface OrderManager extends IManager {

    int getOrderCount(String store_id,String employee_id,String year_month);

    Map<String,Object> queryOrderEmployeeCountByStore(PageInfo pageInfo, String store_id, String date_value);

    List<Map<String,Object>> getOrderEmployeeData(String store_id, String date_value);
    
    public Map<String, Object> queryOrderListByArea(String employee_no,PageInfo pageInfo,Long area_id);
    
    public Map<String, Object> queryOrderListByEmployeeNo(String employee_no,PageInfo pageInfo,Long area_id);
    
    
    public List<Map<String, Object>> queryOrderFourMonth(String employee_no,Long area_id);
    
    /**
     * 根据订单sn编号 查询明细信息 
     * @param order_sn
     * @return
     */
    public Map<String, Object> queryOrderInfoBySN(String order_sn);
    
    
    /**
     * CRM店长 根据门店 查询 送单量排序  图表的方法  
     * @param store_id
     * @return
     */
    public Map<String, Object> queryOrderCountByStoreId(Long store_id);
    
    
    /**
     * CRM店长图表 根据门店及片区，查询所有订单数据
     * @param store_id
     * @param area_name
     */
    public Map<String, Object> queryOrderTotalByArea(Long store_id);
    
    /**
     * 店长CRM 根据门店ID取得今年的 每个月的订单数及金额
     * @param store_id
     */
    public Map<String, Object> queryOrderCountByMonthStoreId(Long store_id);
    
    
    /**
     * APP手机国安侠分片 查询五个月的图表
     */
    public List<Map<String, Object>> queryOrderFiveMonth(Long store_id,String employee_no,Long area_id);
    
    
    /**
     * APP手机国安侠 个人中心 不分片 查询五个月的图表
     */
    public List<Map<String, Object>> queryOrderFiveMonthOrderApp(String employee_no);
    
    /**
     * 
     * TODO crm-城市总监、区域经理查询各个门店的订单等信息 
     * 2017年6月21日
     * @author gaobaolei
     * @param store_id
     * @return
     */
    public Map<String, Object> queryOrderTotalOfStore_CSZJ_QYJL(Long cityId,Long employee_no,String role,String q_date);
    
    /**
     * 
     * TODO crm-城市总监、区域经理查询当年的订单信息
     * 2017年6月21日
     * @author gaobaolei
     * @param cityId
     * @param employee_no
     * @param role
     * @return
     */
    public Map<String, Object> queryOrderCountOfMonth_CSZJ_QYJL(Long cityId,Long employee_no,String role);
    
    /**
     * 
     * TODO crm-从预备数据城市总监、区域经理查询当年的订单信息
     * 2017年6月23日
     * @author gaobaolei
     * @param cityId
     * @param employee_no
     * @param role
     * @return
     */
    public Map<String, Object> queryOrderCountOfMonth_CSZJ_QYJL_before(Long cityId,String employee_no,String role);
    
    /**
     * 
     * TODO crm-从预备数据城市总监、区域经理查询各个门店的订单等信息 
     * 2017年6月21日
     * @author gaobaolei
     * @param store_id
     * @return
     */
    public Map<String, Object> queryOrderTotalOfStore_CSZJ_QYJL_before(Long cityId,Long employee_no,String role,String q_date);
    
    
    /**
     * 根据门店查询当日订单
     */
    public Map<String, Object> queryOrderSetInterval(String store_ids);
    
    public Map<String, Object> queryOrderAmountByMonth(String careergroup);
    
    //根据事业群分城市
    public List<Map<String, Object>> queryOrderAmountByGroupCity(String careername);
    //根据事业群分频道
    public List<Map<String, Object>> queryOrderAmountByChannel(String careername);
    
    //事业群中，点击服务专员 查询明细 
    public Map<String, Object> querystoreamountbyemployeeno(String employee_no);
    
    //总部查看eshop交易额排名
    public Map<String, Object> queryTradeByEshop(DynamicDto dd);
    
//查询门店用户量
    public Map<String,Object> queryCustomerCount(DynamicDto dd,PageInfo pageInfo,String sign);}
