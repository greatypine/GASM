package com.cnpc.pms.heatmap.dao;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author gaoll
 *
 */
public interface OrderHeatDao {
	
	/**
	 * 
	 * TODO 查询既定时间下的门店所辖小区信息及订单数
	 * 2018年3月7日
	 * @author gaoll
	 * @param storeId
	 * @return
	 */
	public List<Map<String,Object>> getTinyVillageOrderInfo(Long store_id,String beginDate,String endDate);
	
	/**
	 * 
	 * TODO 查询既定时间下的门店所辖小区订单数最大值，最小值
	 * 2018年3月7日
	 * @author gaoll
	 * @param storeId
	 * @return
	 */
	public List<Map<String,Object>> getTinyVillageOrderCount(Long store_id,String beginDate,String endDate);
	
	/**
	 * 
	 * TODO 查询既定时间下的门店所辖小区信息及营业额
	 * 2018年3月7日
	 * @author gaoll
	 * @param storeId
	 * @return
	 */
	public List<Map<String,Object>> getTinyVillageGMVInfo(Long store_id,String beginDate,String endDate);
	
	/**
	 * 
	 * TODO 查询既定时间下的门店所辖小区营业额最大值，最小值
	 * 2018年3月7日
	 * @author gaoll
	 * @param storeId
	 * @return
	 */
	public List<Map<String,Object>> getTinyVillageGMVCount(Long store_id,String beginDate,String endDate);
	
	/**
	 * 
	 * TODO 查询既定时间下的门店所辖小区信息及用户量
	 * 2018年3月7日
	 * @author gaoll
	 * @param storeId
	 * @return
	 */
	public List<Map<String,Object>> getTinyVillageCustomerInfo(Long store_id,String beginDate,String endDate);
	
	/**
	 * 
	 * TODO 查询既定时间下的门店所辖小区用户量最大值，最小值
	 * 2018年3月7日
	 * @author gaoll
	 * @param storeId
	 * @return
	 */
	public List<Map<String,Object>> getTinyVillageCustomerCount(Long store_id,String beginDate,String endDate);
	
	/**
	 * 
	 * TODO 查询既定时间下的城市所辖小区信息及订单数
	 * 2018年3月8日
	 * @author gaoll
	 * @param city_id
	 * @return
	 */
	public List<Map<String,Object>> getTinyVillageOrderInfoByCity(Long city_id,String beginDate,String endDate);
	
	/**
	 * 
	 * TODO 查询既定时间下的城市所辖小区订单数最大值，最小值
	 * 2018年3月8日
	 * @author gaoll
	 * @param city_id
	 * @return
	 */
	public List<Map<String,Object>> getTinyVillageOrderCountByCity(Long city_id,String beginDate,String endDate);
	
	/**
	 * 
	 * TODO 查询既定时间下的城市所辖小区信息及营业额
	 * 2018年3月8日
	 * @author gaoll
	 * @param city_id
	 * @return
	 */
	public List<Map<String,Object>> getTinyVillageGMVInfoByCity(Long city_id,String beginDate,String endDate);
	
	/**
	 * 
	 * TODO 查询既定时间下的城市所辖小区营业额最大值，最小值
	 * 2018年3月8日
	 * @author gaoll
	 * @param city_id
	 * @return
	 */
	public List<Map<String,Object>> getTinyVillageGMVCountByCity(Long city_id,String beginDate,String endDate);
	
	/**
	 * 
	 * TODO 查询既定时间下的城市所辖小区信息及用户量
	 * 2018年3月8日
	 * @author gaoll
	 * @param city_id
	 * @return
	 */
	public List<Map<String,Object>> getTinyVillageCustomerInfoByCity(Long city_id,String beginDate,String endDate);
	
	/**
	 * 
	 * TODO 查询既定时间下的城市所辖小区用户量最大值，最小值
	 * 2018年3月8日
	 * @author gaoll
	 * @param city_id
	 * @return
	 */
	public List<Map<String,Object>> getTinyVillageCustomerCountByCity(Long city_id,String beginDate,String endDate);

}
