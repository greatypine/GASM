package com.cnpc.pms.activity.manager.impl;

import com.cnpc.pms.activity.entity.Activity;
import com.cnpc.pms.activity.entity.ActivityPlanScheme;
import com.cnpc.pms.activity.manager.ActivityManager;
import com.cnpc.pms.activity.manager.ActivityPlanSchemeManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.dynamic.common.DateUtil2;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title : ActivityPlanSchemeManagerImpl.java
 * @Package : com.cnpc.pms.activity.manager.impl
 * @Description : 活动策划
 * @CreateDate : 2017年10月17日
 * @Version : V1.0
 */
public class ActivityPlanSchemeManagerImpl extends BizBaseCommonManager
        implements ActivityPlanSchemeManager {


    /**
     * @Title : saveActivityPlanScheme
     * @Description : 保存活动策划
     * @CreateDate : 2017年10月17日
     * @return   : Map<String,String>
     * @Version : V1.0
     */
    public Map<String, String> saveActivityPlanScheme(ActivityPlanScheme activityPlanScheme){
        ActivityPlanSchemeManager activityPlanSchemeManager = (ActivityPlanSchemeManager)
                SpringHelper.getBean("activityPlanSchemeManager");
        Activity activity = new Activity();
        ActivityManager activityManager = (ActivityManager)SpringHelper.getBean("ActivityManager");
        //活动手册
        activity.setActivity_book(""+activityPlanScheme.getActivity_content());
        //活动发起人
        activity.setActivity_initiator(activityPlanScheme.getActivity_user());
        //活动名称
        activity.setActivity_name(activityPlanScheme.getActivity_topic());
        //活动状态 0.已发起 1.
        activity.setActivity_status("0");
        //活动时间
        activity.setActivity_time(DateUtil2.curDate());
        //活动门店名称
        activity.setStore_name(activityPlanScheme.getStoreName());
        //活动使用范围
//        activity.setScope_ofapplication();
        if( activityPlanScheme.getStoreId() != null ){
            //门店主键
            activity.setStoreid(activityPlanScheme.getStoreId());
        }
        //城市id
        activity.setCityname(activityPlanScheme.getCityname());
        activityManager.save(activity);

        Assert.notNull(activityPlanScheme);
        this.save(activityPlanScheme);
        Map<String,String> resultMap = new HashMap<String,String>();
        resultMap.put("success","保存成功");
        return resultMap;
    }

}
