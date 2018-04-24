package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.StoreDynamic;

public interface StoreDynamicDao extends IDAO {
	/**
	 * 根据城市获取当前最大的门店编码序号
	 * 
	 * @param string
	 * @return
	 */
	StoreDynamic findMaxOrdnumber(String string);

	/**
	 * 查询城市最大的门店编码
	 * 
	 * @param store
	 * @return
	 */
	public String getMaxStoreDynamicNo(StoreDynamic storeDynamic);

	List<Map<String, Object>> getStoreDynamicInfoList(String where, PageInfo pageInfo);

	// 根据store_id回写 店长的skid
	public int updateStoreskid(String store_ids, Long userid);

	// 根据store_id回写 运营经理的rmid
	public int updateStorermid(String store_ids, Long userid);

	// 根据城市给门店排序(有开店时间)
	List<StoreDynamic> findStoreDynamicByCity_nameorderByOpentime(String city_name);

	// 根据城市给没有开店时间的门店排序
	public Integer findMaxStoreDynamicOreNumber(String city_name);

	List<StoreDynamic> findStoreDynamicIsnullOrdnumber(String city_name);
}
