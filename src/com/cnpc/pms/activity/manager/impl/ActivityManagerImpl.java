package com.cnpc.pms.activity.manager.impl;

import com.cnpc.pms.activity.entity.ActivityPlanScheme;
import com.cnpc.pms.activity.manager.ActivityManager;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.manager.AppVersionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title : ActivityExaminationManagerImpl.java
 * @Package : com.cnpc.pms.activity.manager.impl
 * @Description : 活动列表展示
 * @CreateDate : 2017年10月17日
 * @Version : V1.0
 */
public class ActivityManagerImpl extends BizBaseCommonManager implements ActivityManager {


    /**
     * 保存活动策划方案
     **/
    @Override
    public Map<String, String> saveActivityPlanScheme(ActivityPlanScheme activityPlanScheme) {
        ActivityManager activityManager = (ActivityManager)
                SpringHelper.getBean("activityManager");
        IFilter iFilter = FilterFactory.getSimpleFilter(" version='"+"'");
        List<?> activityFilter = activityManager.getList(iFilter);
        activityManager.save(activityPlanScheme);
        Map<String,String> resultMap = new HashMap<String,String>();
        resultMap.put("success","保存成功");
        return resultMap;
    }


}
