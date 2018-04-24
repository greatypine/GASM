package com.cnpc.pms.personal.manager.impl;

import java.util.List;

import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.entity.AppBanner;
import com.cnpc.pms.personal.manager.AppBannerManager;



/**
 * 轮播内容
 * Created by liuxiao on 2016/10/25.
 */
public class AppBannerManagerImpl extends BizBaseCommonManager implements AppBannerManager {
	 
	@Override
	public List<AppBanner> getAppBannerList() {
		// TODO Auto-generated method stub
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("banner_id", ISort.DESC));
		List<AppBanner> list=(List<AppBanner>) this.getList(fsp);
		return list;
	}


}
