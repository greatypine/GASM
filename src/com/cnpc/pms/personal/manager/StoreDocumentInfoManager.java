package com.cnpc.pms.personal.manager;

import java.io.File;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.StoreDocumentInfo;

public interface StoreDocumentInfoManager extends IManager {
	/**
	 * 保存门店补充信息
	 * 
	 * @param storeDocumentInfo
	 * @return
	 */
	StoreDocumentInfo saveOrUpdateStoreDocumentInfo(StoreDocumentInfo storeDocumentInfo);

	/**
	 * 根据门店id查询门店补充信息
	 *
	 * @param store_id
	 * @return
	 */
	StoreDocumentInfo findStoreDocumentInfoByStoreId(Long store_id);

	/**
	 * 根据id查询门店补充信息
	 * 
	 * @param store_id
	 * @return
	 */
	StoreDocumentInfo findStoreDocumentInfoById(Long id);

	/**
	 * 获取所有门店补充信息
	 * 
	 * @param conditions
	 * @return
	 */
	Map<String, Object> showStoreDocumentInfoData(QueryConditions conditions);

	/**
	 * 根据work_id查询门店补充信息
	 * 
	 * @param store_id
	 * @return
	 */
	StoreDocumentInfo findStoreDocumentInfoByWorkId(String work_id);

	/**
	 * 导出门店详情
	 * 
	 * @return
	 * @throws Exception
	 */
	File exprotStoreDetail() throws Exception;

}
