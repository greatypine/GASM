package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.AppBanner;

import java.util.List;

/**
 * 消息框内容
 * Created by liuxiao on 2016/10/25.
 */
public interface AppBannerManager extends IManager {

    /**
    
     * @return 轮播列表集合
     */
    List<AppBanner> getAppBannerList();

   

}
