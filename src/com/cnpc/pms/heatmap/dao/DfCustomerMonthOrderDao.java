package com.cnpc.pms.heatmap.dao;

import java.util.List;
import java.util.Map;
/**
 * 
 * @author gaoll
 *
 */
public interface DfCustomerMonthOrderDao {

	/**
	 * 
	 * TODO 查询全国消费用户数
	 * 2018年3月6日
	 * @author gaoll
	 * @return
	 */
	Integer findAllCustomer();
	
	/**
	 * 
	 * TODO 根据省份id查询该省消费用户数
	 * 2018年3月6日
	 * @author gaoll
	 * @param province_id
	 * @return
	 */
	Integer findCustomerByProvinceId(Long province_id);
	
	/**
	 * 
	 * TODO 根据城市id查询该市消费用户数
	 * 2018年3月6日
	 * @author gaoll
	 * @param city_id
	 * @return
	 */
	Integer findCustomerByCityId(Long city_id);
	
	/**
	 * 
	 * TODO 根据门店id查询该门店消费用户数
	 * 2018年3月6日
	 * @author gaoll
	 * @param store_id
	 * @return
	 */
	Integer findCustomerByStoreId(Long store_id);
	
	/**
	 * 
	 * TODO 根据片区编号查询该片区消费用户数
	 * 2018年3月6日
	 * @author gaoll
	 * @param area_no
	 * @return
	 */
	Integer findCustomerByAreaNo(String area_no);
	
	/**
	 * 
	 * TODO 根据小区code查询该小区消费用户数
	 * 2018年3月6日
	 * @author gaoll
	 * @param  code
	 * @return
	 */
	Integer findCustomerByVillageCode(String code);
	// 城市查询用户人力定位
	List<Map<String, Object>> findCustomerLatLngByCity(Long city_id);
	// 城市查询用户人力定位最大值最小值
	List<Map<String, Object>> findCustomerLatLngCountByCity(Long city_id);
	// 门店查询用户人力定位
	List<Map<String, Object>> findCustomerLatLngByStore(Long store_id);
	// 门店查询用户人力定位最大值最小值
	List<Map<String, Object>> findCustomerLatLngCountByStore(Long store_id);
	

}
