package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.SiteSelection;

public interface SiteSelectionManager extends IManager{
	//保存选址人用户信息
	public SiteSelection saveSiteSelection(SiteSelection selection);
	
	public SiteSelection updateSiteSelection(SiteSelection selection);
	
	public SiteSelection querySiteSelectionByPhone(String mobilephone);
}
