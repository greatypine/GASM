package com.cnpc.pms.activity.manager;

import com.cnpc.pms.activity.entity.ActivityExamination;

import java.util.Map;

/**
 * @Title : ActivityManager.java
 * @Package : com.cnpc.pms.activity.manager
 * @Description : 活动审批
 * @CreateDate : 2017年10月17日
 * @Version : V1.0
 */
public interface ActivityExaminationManager {

    /**
     * @Title : saveActivityExamination
     * @Description : 保存活动审批
     * @CreateDate : 2017年10月17日
     * @return   : void
     * @Version : V1.0
     */
    public ActivityExamination saveActivityExamination(ActivityExamination activityExamination);

}
