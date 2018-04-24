package com.cnpc.pms.bid.manager.impl;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.entity.IndexModel;
import com.cnpc.pms.bid.entity.IndexModelSelect;
import com.cnpc.pms.bid.manager.IndexModelManager;
import com.cnpc.pms.bid.manager.IndexModelSelectManager;
import com.cnpc.pms.bid.manager.dto.IndexModelSelectDTO;

public class IndexModelSelectManagerImpl extends BaseManagerImpl implements IndexModelSelectManager {
	
	public IndexModelManager getIndexModelManager() {
		return (IndexModelManager) SpringHelper.getBean("indexModelManager");
	}
	
	public void saveSelectModel(IndexModelSelectDTO dto) {
		UserSession userSession = SessionManager.getUserSession();
		Map sessionData = userSession.getSessionData();
		Long userId = (Long) sessionData.get("userId");
		List<IndexModelSelect> selLs = dto.getSelectIm();
		List<IndexModelSelect> unSelLs = dto.getUnSelectIm();
		selLs.addAll(unSelLs);
		List<IndexModelSelect> imsObj = (List<IndexModelSelect>) this.getObjects(FilterFactory.getSimpleFilter("userId = " + userId));
		for (IndexModelSelect obj : imsObj) {
			this.remove(obj);	
		}
		for (IndexModelSelect ims : selLs) {
			ims.setUserId(userId);
			this.preSave(ims);
			this.save(ims);
		}
	}

	public IndexModelSelectDTO getSelectModelList() {
		UserSession userSession = SessionManager.getUserSession();
		Map sessionData = userSession.getSessionData();
		Long userId = (Long) sessionData.get("userId");
		IndexModelSelectDTO dto = new IndexModelSelectDTO();
		FSP unSelFsp = new FSP();
		unSelFsp.setUserFilter(FilterFactory.getSimpleFilter("userId = " + userId + " and displayStatus = 0"));
		unSelFsp.setSort(new Sort("orderNum", Sort.ASC));
		FSP selFsp = new FSP();
		selFsp.setUserFilter(FilterFactory.getSimpleFilter("userId = " + userId + " and displayStatus = 1"));
		selFsp.setSort(new Sort("orderNum", Sort.ASC));
		List<IndexModelSelect> unSelLs = (List<IndexModelSelect>) this.getObjects(unSelFsp);
		List<IndexModelSelect> selLs = (List<IndexModelSelect>) this.getObjects(selFsp);
		// 如果没有个性化配置，已默认值显示
		if (unSelLs.size() == 0 && selLs.size() == 0) {
			FSP defaultFsp = new FSP();
			defaultFsp.setSort(new Sort("orderNum", Sort.ASC));
			List<IndexModel> defaultLs = (List<IndexModel>) getIndexModelManager().getObjects(defaultFsp);
			for (IndexModel im : defaultLs) {
				IndexModelSelect ims = new IndexModelSelect();
				ims.setModelCode(im.getModelCode());
				ims.setModelText(im.getModelText());
				ims.setDisplayStatus(1);
				ims.setOrderNum(im.getOrderNum());
				ims.setUserId(userId);
				selLs.add(ims);
			}
		}
		
		dto.setSelectIm(selLs);
		dto.setUnSelectIm(unSelLs);
		return dto;
	}

}
