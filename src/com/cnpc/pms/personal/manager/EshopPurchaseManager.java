package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.EshopPurchaseDto;

/**
 * @Function：企业购门店信息管理(用于异常订单)
 * @author：chenchuang
 * @date:2017-12-25 下午1:33:20
 *
 * @version V1.0
 */
public interface EshopPurchaseManager extends IManager {
	
	/**
	 * 查询清洗的店铺信息
	 * @param abnormalOrderDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryBaseEshopPurchase(EshopPurchaseDto eshopPurchaseDto,PageInfo pageInfo);
	
	/**
	 * 删除清洗的店铺信息
	 * @param eshop_id
	 * @return
	 */
	public Map<String, Object> deleteEshopPurchaseById(String eshop_id);
	
	/**
	 * 查询清洗的店铺模式类型
	 * @return
	 */
	public List<Map<String, Object>> queryBusinessModelType();
	
	/**
	 * 查询店铺模式基础类型
	 * @return
	 */
	public List<Map<String, Object>> queryBusinessModelBaseType();
	
	/**
	 * 查询E店铺商业模式基础数据
	 * @return
	 */
	public Map<String, Object> queryEshopModelBaseInfo(EshopPurchaseDto shopPurchaseDto,PageInfo pageInfo);
	
	/**
	 * 保存店铺信息
	 * @param citySelect
	 * @param workmonth
	 * @return
	 */
	public Map<String, Object> saveEshopPurchase(String eshopModels); 
}
