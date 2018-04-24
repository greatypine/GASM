package com.cnpc.pms.activity.manager;

import com.cnpc.pms.activity.entity.ActivityPlanScheme;
import com.cnpc.pms.base.manager.IManager;

import java.util.Map;

/**
 * @Title : ActivityExaminationManagerImpl.java
 * @Package : com.cnpc.pms.activity.manager.impl
 * @Description : 活动列表展示
 * @CreateDate : 2017年10月17日
 * @Version : V1.0
 */
public interface ActivityManager extends IManager {

    /**
     * @Title : saveActivityPlanScheme
     * @Description : 保存活动策划方案
     * @CreateDate : 2017年10月17日
     * @return   : void
     * @Version : V1.0
     */
   public Map<String, String> saveActivityPlanScheme(ActivityPlanScheme activityPlanScheme);


}
