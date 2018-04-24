package com.cnpc.pms.personal.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.file.comm.utils.CollectionUtils;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.dynamic.entity.EshopPurchaseDto;
import com.cnpc.pms.personal.dao.EshopPurchaseDao;
import com.cnpc.pms.personal.manager.EshopPurchaseManager;
import com.cnpc.pms.platform.dao.OrderDao;

/**
 * @Function：企业购门店信息管理实现类(用于异常订单)
 * @author：chenchuang
 * @date:2017-12-25 下午5:25:56
 *
 * @version V1.0
 */
public class EshopPurchaseManagerImpl extends BizBaseCommonManager implements EshopPurchaseManager {

	@Override
	public Map<String, Object> queryBaseEshopPurchase(EshopPurchaseDto eshopPurchaseDto, PageInfo pageInfo) {
		EshopPurchaseDao eshopPurchaseDao = (EshopPurchaseDao)SpringHelper.getBean(EshopPurchaseDao.class.getName());

		Map<String, Object> result =new HashMap<String,Object>();
		try {
			result = eshopPurchaseDao.queryBaseEshopPurchase(eshopPurchaseDto, pageInfo);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}
	
	@Override
	public Map<String, Object> deleteEshopPurchaseById(String eshopIds){
		EshopPurchaseDao eshopPurchaseDao = (EshopPurchaseDao)SpringHelper.getBean(EshopPurchaseDao.class.getName());
        
        Map<String, Object> result = new HashMap<String, Object>();
		try {
			if(eshopIds!=null&&!"".equals(eshopIds)){
				String[] eshopPurchaseArr = eshopIds.split(",");
				for(String eshopId:eshopPurchaseArr){
					eshopPurchaseDao.deleteEshopPurchase(eshopId);
				}
			}
			result.put("status", "success");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "fail");
			return result;
		}
	}
	
	@Override
	public List<Map<String, Object>> queryBusinessModelType(){
		EshopPurchaseDao eshopPurchaseDao = (EshopPurchaseDao)SpringHelper.getBean(EshopPurchaseDao.class.getName());
		
		return eshopPurchaseDao.queryBusinessModelType();
	}
	
	@Override
	public List<Map<String, Object>> queryBusinessModelBaseType(){
		OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
		
		return orderDao.queryBusinessModelBaseType();
	}
	
	@Override
	public Map<String, Object> queryEshopModelBaseInfo(EshopPurchaseDto shopPurchaseDto,PageInfo pageInfo){
		OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
		EshopPurchaseDao eshopPurchaseDao = (EshopPurchaseDao)SpringHelper.getBean(EshopPurchaseDao.class.getName());
		
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			List<Map<String,Object>> eshopList = eshopPurchaseDao.queryAllEshopPurchases();
			
			result = orderDao.queryEshopModelBaseInfo(CollectionUtils.listConvert(eshopList), shopPurchaseDto, pageInfo);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}

	@Override
	public Map<String, Object> saveEshopPurchase(String eshopModels){
		EshopPurchaseDao eshopPurchaseDao = (EshopPurchaseDao)SpringHelper.getBean(EshopPurchaseDao.class.getName());
        
        Map<String, Object> result = new HashMap<String, Object>();
		try {
			if(eshopModels!=null&&!"".equals(eshopModels)){
				String[] eshopModel = eshopModels.split(",");
				for(String models:eshopModel){
					eshopPurchaseDao.saveEshopPurchase(models);
				}
			}
			result.put("status", "success");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "fail");
			return result;
		}
		
	}
	
}
