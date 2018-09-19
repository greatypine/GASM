package com.cnpc.pms.inter.dao;

import com.cnpc.pms.base.paging.impl.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * APP接口数据库操作
 * Created by liu on 2016/8/23 0023.
 */
public interface InterDao {

    /**
     * 获取当前版本
     * @return 当前版本
     */
    Map<String,List<String>> getCurrentVersion();

    Integer getExpressCountByCurrentMonth(String employee_no);

    Integer getCustomerCountByCurrentMonth(String employee_no);

    Integer getRelationCountByCurrentMonth(String employee_no);

    Integer getExpressStoreCountByCurrentMonth(Long store_id);
    
    /**
     * 
     * TODO 用户画像基础字段完成量的总和
     * 2017年3月8日
     * @author liuxiao
     * @update gaobaolei
     * @param store_id
     * @return
     */
    Integer getCustomerStoreCountByCurrentMonth(Long store_id);

    Integer getRelationStoreCountByCurrentMonth(Long store_id);
    
    Integer getExpressCountByCurrentMonth(String employee_no,String month);

    Integer getCustomerCountByCurrentMonth(String employee_no,String month);

    Integer getRelationCountByCurrentMonth(String employee_no,String month);

    Integer getExpressStoreCountByCurrentMonth(Long store_id,String month);

    Integer getCustomerStoreCountByCurrentMonth(Long store_id,String month);

    Integer getRelationStoreCountByCurrentMonth(Long store_id,String month);
    
    Integer getXXExpressStoreCountByCurrentMonth(Long store_id,String month);
    
    Integer getSelfExpressStoreCountByCurrentMonth(Long store_id,String month);

    Map<String,Integer> getExpressStoreCountByCurrentMonth(String employee_no);

    Map<String,Integer> getCustomerStoreCountByCurrentMonth(String employee_no);

    Map<String,Integer> getRelationStoreCountByCurrentMonth(String employee_no);
    
    /**
     * 根据门店获取拜访记录的数据
     */
    List<?> getRelationDataByStoreAndMouth(String employee_no,String month);
    /**
     * 根据门店获取用户画像的数据
     */
    List<?> getCustomerDataByStoreAndMouth(String whhere,String month);
    /**
     * 根据门店获取快递的数据
     */
    List<Map<String, Object>> getExpressDataByStoreAndMouth(String where, PageInfo pageInfo,String month,Long store_id);
    
    public List<?> getExpressDataByStoreAndMouth_tu(String employee_no,String month);
    
    List<Map<String, Object>> getCustomerDataByStoreAndMouth_tu(String where, PageInfo pageInfo,String month,String grade,Long storeId);
    
    List<Map<String, Object>> getRelationDataByStoreAndMouth_tu(String where, PageInfo pageInfo,String month,Long store_id);
    
    //根据用户获取当前日期12个月前的数据
    List<Map<String, Object>> getMonthDataCustomerByEmployee(String employee,String month);
    List<Map<String, Object>> getMonthDataRelationByEmployee(String employee,String month);
    List<Map<String, Object>> getMonthDataExpressByEmployee(String employee,String month);
    /**
     * 根据门店或着员工号查询所有合格的单体画像
     * @param where
     * @param month
     * @return
     */
    List<Map<String, Object>> getCustomerByMonth(String where,String month);
    
    /**
     * 
     * TODO 根据基础字段完成等级查询用户画像总数 (国安侠数据卡)
     * 2017年3月21日
     * @author gaobaolei
     * @param employee_no
     * @param month
     * @param grade
     * @return
     */
    public int  getCustomerCountByCurrentMonth(String employee_no,String month,String grade,Long storeId);
    
    /**
     * 
     * TODO 根据基础字段完成等级查询用户画像总数 (门店数据卡)
     * 2017年3月21日
     * @author gaobaolei
     * @param store_id
     * @param month
     * @param grade
     * @return
     */
    public Integer getCustomerStoreCountByCurrentMonth(Long store_id,String month,String grade);
    
    /**
     * 
     * TODO  根据基础字段完成等级查询用户画像圆形列报表 (门店数据卡)
     * 2017年3月22日
     * @author gaobaolei
     * @param where
     * @param month
     * @param grade
     * @return
     */
    public List<?> getCustomerDataByStoreAndMouth(String where,String month,String grade,Long storeId);
    
    
    /**
     * 
     * TODO crm-从预备数据城市总监、区域经理查询当年的订单 
     * 2017年6月21日
     * @author gaobaolei
     * @param store
     * @return
     */
    public List<Map<String, Object>> queryOrderCountOfMonth_CSZJ_QYJL_before(String employeeId, Long cityId, String role);
    
    /**
     * 
     * TODO crm-从预备数据城市总监、区域经理查询各个门店的订单等信息 
     * 2017年6月23日
     * @author gaobaolei
     * @param employee_no
     * @param role
     * @param cityId
     * @return
     */
    public List<Map<String, Object>> queryOrderListOfStore_CSZJ_QYJL_before(Long employeeId, Long cityId, String role,String q_date);
}
