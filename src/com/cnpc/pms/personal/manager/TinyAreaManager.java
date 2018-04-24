package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.TinyArea;

/**
 * 小区坐标
 * 
 * @author gaobaolei
 *
 */
public interface TinyAreaManager extends IManager {
	// 根据小区id查询小区的编码
	TinyArea findTinyAreaByTinyId(Long tinyId);
	// //保存小区片区编码
	// TinyArea saveTinyArea(TinyVillage tinyVillage);

	/**
	 * 
	 * TODO 更新小区范围 2017年11月13日
	 * 
	 * @author gaobaolei
	 * @param tinyArea
	 * @return
	 */
	public Map<String, Object> updateTinyAreaOfCoord(TinyArea tinyArea);

	/**
	 * 
	 * TODO 查询门店坐标信息 2017年11月24日
	 * 
	 * @author gaobaolei
	 * @param cityId
	 * @param storeNo
	 * @return
	 */
	public Map<String, Object> getTinyVillageCoordAndServiceArea(Long cityId);

	/**
	 * 
	 * TODO public 2017年12月5日
	 * 
	 * @author gaoll
	 * @param cityId
	 * @param storeNo
	 * @return
	 */
	public Map<String, Object> updateVallageAreaByCode(List<Map<String, Object>> list);

	/**
	 * 
	 * TODO 查询门店小区坐标 2018年1月17日
	 * 
	 * @author gaobaolei
	 * @param tinyId
	 * @param storeNo
	 * @return
	 */
	public TinyArea findTinyAreaByTinyId(Long tinyId, String storeNo);

	TinyArea getTinyAreaByTinyId(Long tinyId);

}
