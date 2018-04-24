package com.cnpc.pms.activity.manager;

import com.cnpc.pms.activity.entity.ActivityPlanScheme;

import java.util.Map;

/**
 * @Title : ActivityPlanSchemeManager.java
 * @Package : com.cnpc.pms.activity.manager
 * @Description : 活动策划
 * @CreateDate : 2017年10月17日
 * @Version : V1.0
 */
public interface ActivityPlanSchemeManager {


    /**
     * @Title : saveActivityPlanScheme
     * @Description : 保存活动策划
     * @CreateDate : 2017年10月17日
     * @return   : void
     * @Version : V1.0
     */
    public Map<String, String> saveActivityPlanScheme(ActivityPlanScheme activityPlanScheme);


}
