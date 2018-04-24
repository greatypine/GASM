package com.cnpc.pms.bid.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.bid.manager.dto.IndexModelSelectDTO;

public interface IndexModelSelectManager extends IManager {

	/**
	 * 保存选择和排序的模块
	 * @param ims
	 */
	public void saveSelectModel(IndexModelSelectDTO dto);
	
	/**
	 * 获取已选择的模块和未选择的模块
	 * @return
	 */
	public IndexModelSelectDTO getSelectModelList();
	
}
