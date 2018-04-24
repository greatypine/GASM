/**
 * gaobaolei
 */
package com.cnpc.pms.personal.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.AbnormalOrderDto;

/**
 * @author gaobaolei
 *
 */
public interface DsAbnormalOrderDao extends IDAO{
	
	/**
	 * 
	 * TODO 更新异常订单状态 
	 * 2017年9月25日
	 * @author gaobaolei
	 * @param id
	 * @param updateDate
	 */
	public void updateOrder(Long id,String updateDate);
	
	/**
	 * 
	 * TODO 查询异常订单库 
	 * 2017年9月25日
	 * @author gaobaolei
	 * @param abnormalOrderDto
	 * @return
	 */
	public List<Map<String, Object>> queryAbnormalDown(AbnormalOrderDto abnormalOrderDto);
	
	/**
	 * 
	 * TODO 查询基础异常订单数据 
	 * 2017年10月17日
	 * @author gaobaolei
	 * @param abnormalOrderDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryBaseAbnormalOrder(AbnormalOrderDto abnormalOrderDto,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 查询导出的基础异常订单 
	 * 2017年10月19日
	 * @author gaobaolei
	 * @param abnormalOrderDto
	 * @return
	 */
	public List<Map<String, Object>> queryBaseAbnormalOrderNoPage(AbnormalOrderDto abnormalOrderDto);
	
	/**
	 * 
	 * TODO 查询异常订单结果（门店） 
	 * 2017年10月19日
	 * @author gaobaolei
	 * @param abnormalOrderDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryAbnormalOrderResult(AbnormalOrderDto abnormalOrderDto,PageInfo pageInfo);
	
	public List<Map<String, Object>> queryAbnormalOrderResult(AbnormalOrderDto abnormalOrderDto);

	/**
	 * 事业群首页的方法
	 * @return
	 */
	public Map<String, Object> queryOrderAmountByMonth(int year,int month,String dep_name);
	public List<Map<String, Object>> queryOrderAmountByGroupCity(int year,int month,String dep_name);
	public List<Map<String, Object>> queryOrderAmountByChannel(int year,int month,String dep_name);
	
	/**
	 * 事业群 服务专员页 中的查询方法
	 * @return
	 */
	public Map<String, Object> querynewaddcusbystoreno(int year,int month,String storeno);
	public Map<String, Object> queryorderamountbycareergroup(int year,int month,String storeno,String careergroup);
	public Map<String, Object> queryorderamountbystoreno(int year,int month,String gstoreno);
}
