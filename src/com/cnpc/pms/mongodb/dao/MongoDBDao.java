/**
 * gaobaolei
 */
package com.cnpc.pms.mongodb.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;

/**
 * @author gaobaolei
 *
 */
public interface MongoDBDao {
	
	/**
	 * 
	 * TODO 查询门店所辖小区信息 
	 * 2017年11月10日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> queryAllTinyVillageByStore(Long storeId);
	
	/**
	 * 
	 * TODO 查询门店已经存在的范围
	 * 2017年11月13日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> queryAllTinyAreaOfStore(Long storeId);
	
	/**
	 * 
	 * TODO 获取片区管辖的小区 
	 * 2017年11月14日
	 * @author gaobaolei
	 * @param area_id
	 * @return
	 */
	public List<Map<String, Object>> queryAllTinyVillageOfArea(Long area_id);
	
	/**
	 * 
	 * TODO 获取片区管辖的小区 
	 * 2018年1月23日
	 * @author gaobaolei
	 * @param storeId
	 * @param areaNo
	 * @return
	 */
	public List<Map<String, Object>> queryAllTinyVillageOfArea(Long storeId,String areaNo);
	
	/**
	 * 
	 * TODO 根据小区编号查找小区所在片区 
	 * 2017年11月24日
	 * @author gaobaolei
	 * @param code
	 * @return
	 */
	public List<Map<String, Object>> getAreaOfTinyVillage(String code);
	
	/**
	 * 
	 * TODO 根据片区编号获得片区管辖的小区 
	 * 2018年1月11日
	 * @author gaoll
	 * @param area_no
	 * @return
	 */
	public List<Map<String, Object>> getAllTinyVillageOdAreaNo(String area_no); 
	
	
}
