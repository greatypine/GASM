package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.MassOrderDto;

/**
 * @Function：清洗出的订单Dao
 * @author：chenchuang
 * @date:2018年1月9日 下午3:31:04
 *
 * @version V1.0
 */
public interface MassOrderDao extends IDAO{

	/**
	 * 查询订单数据列表
	 * @param massOrderDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryMassOrder(MassOrderDto massOrderDto,PageInfo pageInfo,String timeFlag);
	
	/**
	 * 导出订单数据列表
	 * @param massOrderDto
	 * @return
	 */
	public List<Map<String, Object>> exportOrder(MassOrderDto massOrderDto,String timeFlag);
	
	/**
	 * 根据订单号查询订单
	 * @param order_sn
	 * @return
	 */
	public Map<String, Object> queryOrderInfoBySN(String order_sn);
	
	/**
	 * 根据片区编号查询信息
	 * @param area_code
	 * @return
	 */
	public Map<String, Object> queryAreaDetailByCode(String area_code, String order_sn, String timeFlag);
	
	/**
	 * 根据订单号查国安侠信息
	 * @param order_sn
	 * @return
	 */
	public Map<String, Object> queryEmployeeBySN(String order_sn, String timeFlag);
	
	/**
	 * 根据门店编号查Platformid
	 * @param storeno
	 * @return
	 */
	public Map<String, Object> queryPlatformidByCode(String storeno);
	
}
