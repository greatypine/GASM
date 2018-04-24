package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.AbnormalOrderDto;
import com.cnpc.pms.personal.entity.DsAbnormalOrder;
import com.cnpc.pms.personal.entity.DsAbnormalType;
import com.cnpc.pms.personal.entity.DsTopData;


public interface DsAbnormalOrderManager extends IManager {

	public DsAbnormalOrder queryDsAbnormalOrderBySN(String order_sn);
	
	public DsAbnormalOrder saveDsAbnormalOrder(DsAbnormalOrder dsAbnormalOrder);
	
	public DsAbnormalType queryDsAbnormalTypeBySN(String order_sn);
	
	/**
	 * 
	 * TODO 更新异常订单状态 
	 * 2017年9月22日
	 * @author gaobaolei
	 * @param order_sn
	 */
	public Map<String, Object> updateDsAbnormalTypeBySN(String order_id);
	
	/**
	 * 
	 * TODO 从订单库下载订单 
	 * 2017年9月25日
	 * @author gaobaolei
	 * @return
	 */
	public Map<String,Object> downAbnormalOrders(AbnormalOrderDto abnormalOrderDto);
	
	/**
	 * 
	 * TODO 查询异常订单基础数据 
	 * 2017年10月17日
	 * @author gaobaolei
	 * @param abnormalOrderDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryBaseAbnormalOrder(AbnormalOrderDto abnormalOrderDto,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 导出异常订单基础数据 
	 * 2017年10月17日
	 * @author gaobaolei
	 * @param abnormalOrderDto
	 * @return
	 */
	public Map<String, Object> exportBaseAbnormalOrder(AbnormalOrderDto abnormalOrderDto);
	
	/**
	 * 
	 * TODO 查询异常订单结果 
	 * 2017年10月19日
	 * @author gaobaolei
	 * @param abnormalOrderDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String,Object> queryAbnormalOrderResult(AbnormalOrderDto abnormalOrderDto,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 导出异常订单结果 
	 * 2017年10月19日
	 * @author gaobaolei
	 * @param abnormalOrderDto
	 * @return
	 */
	public Map<String, Object> exportAbnormalOrderResult(AbnormalOrderDto abnormalOrderDto);
	
}
