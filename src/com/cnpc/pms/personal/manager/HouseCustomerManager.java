package com.cnpc.pms.personal.manager;

import java.io.File;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.HouseCustomer;

/**
 * 房屋用户关联表
 * Created by liuxiao on 2016/8/8 0008.
 */
public interface HouseCustomerManager extends IManager {

    /**
     * 保存房屋用户关联表
     * @param houseCustomer 房屋用户对象
     */
    void saveHouseCustomer(HouseCustomer houseCustomer);

    /**
     * 保存房屋用户关联表
     * @param houseCustomer 房屋用户对象
     */
    Integer checkedHouseCustomer(HouseCustomer houseCustomer);

    /**
     * 下载选中的月份的单体画像绩效
     * @param work_month 选中月份
     * @return 下载路径
     */
    String exportAchievements(String work_month) throws Exception;
    
    
    /**
     * 导出用户画像统计条数信息
     * @return
     */
    public File queryUserHouseCount() throws Exception;
    
    /**
     * 
     * TODO 定时任务设置用户画像的支付状态 
     * 2017年5月9日
     * @author gaobaolei
     */
    public void changeCustomerPayStatus();
    
    //根据房屋信息查找用户画像
    HouseCustomer findHouseCustomerByHouseId(Long house_id);

}
