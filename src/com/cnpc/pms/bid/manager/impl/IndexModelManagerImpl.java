package com.cnpc.pms.bid.manager.impl;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.entity.IndexModel;
import com.cnpc.pms.bid.entity.IndexModelSelect;
import com.cnpc.pms.bid.manager.IndexModelManager;
import com.cnpc.pms.bid.manager.IndexModelSelectManager;

public class IndexModelManagerImpl extends BaseManagerImpl implements IndexModelManager {
	
	public IndexModelSelectManager getIndexModelManager() {
		return (IndexModelSelectManager) SpringHelper.getBean("indexModelSelectManager");
	}
	
	public void saveDefaultModel() {
		UserSession userSession = SessionManager.getUserSession();
		Map sessionData = userSession.getSessionData();
		Long userId = (Long) sessionData.get("userId");
		List<IndexModelSelect> imsObj = (List<IndexModelSelect>) getIndexModelManager().getObjects(FilterFactory.getSimpleFilter("userId = " + userId));
		for (IndexModelSelect obj : imsObj) {
			getIndexModelManager().remove(obj);	
		}
		List<IndexModel> ls = this.getDefaultModel();
		for (IndexModel im : ls) {
			IndexModelSelect ims = new IndexModelSelect();
			ims.setModelCode(im.getModelCode());
			ims.setModelText(im.getModelText());
			ims.setOrderNum(im.getOrderNum());
			ims.setDisplayStatus(1);
			ims.setUserId(userId);
			this.preSave(ims);
			this.save(ims);
		}
	}

	public List<IndexModel> getDefaultModel() {
		return (List<IndexModel>) this.getObjects();
	}

}
