package com.cnpc.pms.personal.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.mongodb.manager.MongoDBManager;
import com.cnpc.pms.personal.dao.TinyAreaDao;
import com.cnpc.pms.personal.entity.TinyArea;
import com.cnpc.pms.personal.manager.TinyAreaManager;

public class TinyAreaManagerImpl extends BizBaseCommonManager implements TinyAreaManager {

	@Override
	public TinyArea findTinyAreaByTinyId(Long tinyId) {
		TinyAreaManager tinyAreaManager = (TinyAreaManager) SpringHelper.getBean("tinyAreaManager");
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tiny_village_Id=" + tinyId)
				.appendAnd(FilterFactory.getSimpleFilter("status=0")));
		if (lst_data != null && lst_data.size() > 0) {
			return (TinyArea) lst_data.get(0);
		}
		return null;
	}

	// @Override
	// public TinyArea saveTinyArea(TinyVillage tinyVillage) {
	// Date date = new Date();
	// java.sql.Date sdate = new java.sql.Date(date.getTime());
	// //根据小区查询是否存在小区编码
	// TinyArea tinyArea = findTinyAreaByTinyId(tinyVillage.getId());
	// if(tinyArea==null){
	// TinyAreaDao
	// tinyAreaDao=(TinyAreaDao)SpringHelper.getBean(TinyAreaDao.class.getName());
	// tinyArea=new TinyArea();
	// //根据街道id获取街道
	// TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
	// Town town = townManager.findTownById(tinyVillage.getTown_id());
	// Integer maxnum = tinyAreaDao.findMaxTinyArea(town.getGb_code());
	// if(maxnum!=null){
	// String strss=maxnum+1+"";
	// if(strss.length()==1){
	// tinyArea.setCode(town.getGb_code()+"000000000"+strss);
	// }else if(strss.length()==2){
	// tinyArea.setCode(town.getGb_code()+"00000000"+strss);
	// }else if(strss.length()==3){
	// tinyArea.setCode(town.getGb_code()+"0000000"+strss);
	// }else if(strss.length()==4){
	// tinyArea.setCode(town.getGb_code()+"000000"+strss);
	// }else if(strss.length()==5){
	// tinyArea.setCode(town.getGb_code()+"00000"+strss);
	// }else if(strss.length()==6){
	// tinyArea.setCode(town.getGb_code()+"0000"+strss);
	// }else if(strss.length()==7){
	// tinyArea.setCode(town.getGb_code()+"000"+strss);
	// }else if(strss.length()==8){
	// tinyArea.setCode(town.getGb_code()+"00"+strss);
	// }else if(strss.length()==9){
	// tinyArea.setCode(town.getGb_code()+"0"+strss);
	// }
	// }else{
	// tinyArea.setCode(town.getGb_code()+"0000000001");
	// }
	// tinyArea.setCreate_time(sdate);
	// tinyArea.setCreate_user_id(tinyVillage.getCreate_user_id());
	// tinyArea.setName(tinyVillage.getName());
	// tinyArea.setTiny_village_id(tinyVillage.getId());
	// tinyArea.setUpdate_time(sdate);
	// tinyArea.setUpdate_user_id(tinyVillage.getUpdate_user_id());
	// tinyAreaDao.saveTinyArea(tinyArea);
	// }else{
	// TinyAreaManager tinyAreaManager
	// =(TinyAreaManager)SpringHelper.getBean("tinyAreaManager");
	// tinyArea.setName(tinyVillage.getName());
	// tinyArea.setTiny_village_id(tinyVillage.getId());
	// tinyArea.setUpdate_time(sdate);
	// tinyArea.setUpdate_user_id(tinyVillage.getUpdate_user_id());
	// tinyAreaManager.saveObject(tinyArea);
	// }
	// return tinyArea;
	// }

	@Override
	public Map<String, Object> updateTinyAreaOfCoord(TinyArea tinyArea) {
		Map<String, Object> result = new HashMap<String, Object>();
		TinyArea tinyArea1 = this.findTinyAreaByTinyId(tinyArea.getTiny_village_id());
		try {
			if (tinyArea1 != null) {
				BeanUtils.copyProperties(tinyArea, tinyArea1,
						new String[] { "id", "version", "create_time", "create_user", "create_user_id" });
				preObject(tinyArea1);
				this.saveObject(tinyArea1);
			} else {

				preObject(tinyArea);
				this.saveObject(tinyArea);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "-1");
			result.put("message", "update fail");
			return result;
		}

		result.put("code", "1");
		result.put("message", "update success");
		return result;
	}

	@Override
	public Map<String, Object> getTinyVillageCoordAndServiceArea(Long cityId) {
		MongoDBManager mongoDBManager = (MongoDBManager) SpringHelper.getBean("mongoDBManager");
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> serviceArea = mongoDBManager.getAllStoreServiceAreaOfCity(cityId);// 门店服务范围
		Map<String, Object> coord = mongoDBManager.getAllTinyVillageCoordinateOfCity(cityId);// 小区坐标
		result.put("serviceArea", serviceArea);
		result.put("coordinate", coord);
		return result;
	}

	@Override
	public Map<String, Object> updateVallageAreaByCode(List<Map<String, Object>> list) {
		TinyAreaDao tinyAreaDao = (TinyAreaDao) SpringHelper.getBean(TinyAreaDao.class.getName());
		Map<String, Object> map_result = new HashMap<String, Object>();
		try {
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					String code = map.get("code").toString();
					String vallage_area = map.get("vallage_area").toString();
					tinyAreaDao.updateVallageAreaByCode(code, vallage_area);
				}
			}
			map_result.put("code", "1");
			map_result.put("message", "update success");
			return map_result;
		} catch (Exception e) {
			map_result.put("code", "-1");
			map_result.put("message", "update fail");
			return map_result;
		}

	}

	@Override
	public TinyArea findTinyAreaByTinyId(Long tinyId, String storeNo) {
		TinyAreaManager tinyAreaManager = (TinyAreaManager) SpringHelper.getBean("tinyAreaManager");
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tiny_village_Id=" + tinyId)
				.appendAnd(FilterFactory.getSimpleFilter("status=0"))
				.appendAnd(FilterFactory.getSimpleFilter("store_no='" + storeNo + "'")));
		if (lst_data != null && lst_data.size() > 0) {
			return (TinyArea) lst_data.get(0);
		}
		return null;
	}

	@Override
	public TinyArea getTinyAreaByTinyId(Long tinyId) {
		TinyAreaManager tinyAreaManager = (TinyAreaManager) SpringHelper.getBean("tinyAreaManager");
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tiny_village_Id=" + tinyId)
				.appendAnd(FilterFactory.getSimpleFilter("status=0")));
		if (lst_data != null && lst_data.size() > 0) {
			return (TinyArea) lst_data.get(0);
		}
		return null;
	}

}
