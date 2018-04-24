package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.BannerInfo;

public interface BannerInfoManager extends IManager{
	
	public BannerInfo saveBannerInfo(BannerInfo bannerInfo);
	
	public BannerInfo queryBannerInfoById(Long id);
	
	public void deleteBannerInfo(Long id);
	
	
}
