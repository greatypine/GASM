package com.cnpc.pms.activity.manager.impl;

import com.cnpc.pms.activity.entity.Activity;
import com.cnpc.pms.activity.entity.ActivityExamination;
import com.cnpc.pms.activity.manager.ActivityExaminationManager;
import com.cnpc.pms.activity.manager.ActivityManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.dynamic.common.DateUtil2;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title : ActivityExaminationManagerImpl.java
 * @Package : com.cnpc.pms.activity.manager.impl
 * @Description : 活动审批
 * @CreateDate : 2017年10月17日
 * @Version : V1.0
 */
public class ActivityExaminationManagerImpl extends BizBaseCommonManager
        implements ActivityExaminationManager {



    /**
     * @Title : saveActivityExamination
     * @Description : 保存活动审批
     * @CreateDate : 2017年10月17日
     * @return   : void
     * @Version : V1.0
     */
    @Override
    public ActivityExamination saveActivityExamination(ActivityExamination activityExamination) {
        ActivityExaminationManager activityExaminationManager =
                (ActivityExaminationManager)SpringHelper.getBean("activityExaminationManager");
        Assert.notNull(activityExamination);
        this.save(activityExamination);

        Map<String,String> resultMap = new HashMap<String,String>();
        resultMap.put("suc","保存成功");
        return activityExamination;
    }

}
