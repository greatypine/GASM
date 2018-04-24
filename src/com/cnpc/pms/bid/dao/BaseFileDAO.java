package com.cnpc.pms.bid.dao;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.bid.manager.dto.BaseFileDTO;

public interface BaseFileDAO extends IDAO {

	/**
	 * 新增记录
	 * @param baseFileDAO
	 */
	public void addBaseFile(BaseFileDTO baseFileDTO);
}
