package com.cnpc.pms.bid.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.bid.manager.dto.BaseFileDTO;
/**
 * 文件存储mananger
 * @author IBM
 *
 */
public interface BaseFileManager extends IManager {

	/**
	 * 保存文件信息
	 * @param baseFileDTO
	 * @return
	 */
	public BaseFileDTO saveBaseFile(BaseFileDTO baseFileDTO);
}
