package com.cnpc.pms.heatmap.manager;

import java.util.Map;

public interface MapBasicDataManager {
	
	/**
	 * 
	 * TODO  全国基础数据展示
	 * 2018年1月22日
	 * @author gaoll
	 * @return
	 */
	public Map<String, Object> getChinaBasicData();
	
	/**
	 * 
	 * TODO  省基础数据展示
	 * 2018年1月23日
	 * @author gaoll
	 * @param province_id;
	 * @return
	 */
	public Map<String,Object> getProvinceBasicData(Long province_id);
	
	/**
	 * 
	 * TODO  市基础数据展示
	 * 2018年1月23日
	 * @author gaoll
	 * @param city_id;
	 * @return
	 */
	public Map<String,Object> getCityBasicData(Long city_id);
	
	/**
	 * 
	 * TODO  门店基础数据展示
	 * 2018年1月25日
	 * @author gaoll
	 * @param store_id;
	 * @return
	 */
	public Map<String,Object> getStoreBasicData(Long store_id);
	
	/**
	 * 
	 * TODO  片区基础数据展示
	 * 2018年1月26日
	 * @author gaoll
	 * @param areaNo;
	 * @return
	 */
	public Map<String,Object> getAreaBasicData(String areaNo,Long storeId);
	
	/**
	 * 
	 * TODO  小区基础数据展示
	 * 2018年1月26日
	 * @author gaoll
	 * @param code;
	 * @return
	 */
	public Map<String,Object> getVillageBasicData(String code);
	
}
