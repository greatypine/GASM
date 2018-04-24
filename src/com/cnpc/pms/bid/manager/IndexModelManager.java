package com.cnpc.pms.bid.manager;

import java.util.List;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.bid.entity.IndexModel;

public interface IndexModelManager extends IManager {

	
	/**
	 * 保存默认选择和排序的模块
	 */
	public void saveDefaultModel();
	
	/**
	 * 获取默认的选择和排序
	 * @return
	 */
	public List<IndexModel> getDefaultModel();
}
