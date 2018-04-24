package com.cnpc.pms.personal.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.MassOrderDto;

/**
 * @Function：订单数据
 * @author：chenchuang
 * @date:2018年1月9日 下午2:35:41
 *
 * @version V1.0
 */
public interface MassOrderManager extends IManager {

	/**
	 * 查询订单信息
	 * @param massOrderDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryMassOrder(MassOrderDto massOrderDto,PageInfo pageInfo);
	
	/**
	 * 导出订单信息
	 * @param massOrderDto
	 * @return
	 */
	public Map<String, Object> exportOrder(MassOrderDto massOrderDto);
	
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
	public Map<String, Object> queryAreaDetailByCode(String area_code, String order_sn,String beginDate);
	
	/**
	 * 根据订单号查询国安侠信息
	 * @param order_sn
	 * @return
	 */
	public Map<String, Object> queryEmployeeBySN(String order_sn,String beginDate);
	
}
