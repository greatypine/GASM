package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.TinyVillageDataDetails;

public interface TinyVillageDataDetailsManager extends IManager{
	/**
	 * 根据小区id查询小区数据信息
	 * @author sunning
	 * @param tiny_village_id
	 * @return
	 */
	TinyVillageDataDetails findTinyVillageDataDetailsByTinyId(Long tiny_village_id);
	/**
	 * @author sunning
	 * 同步更新小区数据
	 */
	void syncTinyVillageDataDetails();
	/**
	 * 获取小区详情并分页
	 * @author sunning
	 * @param tinyVillageDataDetails
	 * @param pageInfo
	 * @return
	 */
	List<Map<String, Object>> findTinyVillageDetailsPage(TinyVillageDataDetails tinyVillageDataDetails,PageInfo pageInfo);
}
