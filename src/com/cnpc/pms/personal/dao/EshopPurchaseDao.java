package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.EshopPurchaseDto;

/**
 * @Function：企业购Dao
 * @author：chenchuang
 * @date:2017-12-25 下午5:35:55
 *
 * @version V1.0
 */
public interface EshopPurchaseDao extends IDAO{
	
	/**
	 * 查询店铺信息列表
	 * @param shopPurchaseDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryBaseEshopPurchase(EshopPurchaseDto shopPurchaseDto,PageInfo pageInfo);
	
	/**
	 * 查询All店铺
	 * @return
	 */
	public List<Map<String,Object>> queryAllEshopPurchases();
	
	/**
	 * 删除店铺信息
	 * @param id
	 * @param updateDate
	 */
	public void deleteEshopPurchase(String eshopId);
	
	/**
	 * 查询店铺商业模式类型
	 * @return
	 */
	public List<Map<String, Object>> queryBusinessModelType();
	
	/**
	 * 保存E店铺商业模式
	 * @param eshopId
	 * @param eshopCode
	 * @param eshopName
	 * @param eshopModel
	 * @return
	 */
	public void saveEshopPurchase(String models);
	
}
