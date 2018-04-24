package com.cnpc.pms.personal.dao;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.personal.entity.HouseCustomer;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by liu on 2016/7/13 0013.
 */
public interface HouseCustomerDao extends IDAO {

    /**
     * 修改满足六元支付绩效的状态
     * @param work_month 下载月份
     * @throws Exception 抛出的异常
     */
    void updateSixPayStatus(String work_month) throws Exception;

    /**
     * 修改满足一元支付绩效的状态
     * @param work_month 下载月份
     * @throws Exception 抛出的异常
     */
    void updateOnePayStatus(String work_month) throws Exception;

    /**
     * 导出选中月份的绩效
     * @param work_month 选中的月份
     * @return 绩效集合
     */
    List<Map<String,Object>> getAchievementsList(String work_month);
    
    
    /**
     * 查询统计的用户画像条数
     * @return
     */
    public List<Map<String,Object>> queryUserHouseCount();
    
    /**
     * 设置满足于35个条件的第三等级支付状态
     * @param work_month
     * @throws Exception
     */
    public void updateThirdGradePayStatus(String work_month) throws Exception;
    
    /**
     * 
     * TODO 查询用户的房子 
     * 2017年5月10日
     * @author gaobaolei
     * @param customerId
     * @return
     */
    public Map<String, Object> selectHouseByCustomer(Long customerId);
    
    //根据楼房id查找用户信息
    HouseCustomer findHouseCustomerByHouseId(Long houseid);
    
    
    
    
    
}
