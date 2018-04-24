package com.cnpc.pms.slice.manager.impl;

import com.cnpc.pms.base.MyException;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.mongodb.manager.MongoDBManager;
import com.cnpc.pms.personal.entity.Relation;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.personal.manager.TinyVillageManager;
import com.cnpc.pms.slice.dao.AreaInfoDao;
import com.cnpc.pms.slice.entity.Area;
import com.cnpc.pms.slice.entity.AreaInfo;
import com.cnpc.pms.slice.manager.AreaInfoManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.beanutils.BeanFactory;
import org.hibernate.annotations.GenerationTime;
import org.jfree.chart.labels.BubbleXYItemLabelGenerator;

/**
 * Created by liuxi on 2017/2/27.
 */
public class AreaInfoManagerImpl extends BizBaseCommonManager implements AreaInfoManager {

    @Override
    public void deleteAreaInfoByAreaId(Area area) throws MyException {
    	
    	Map<String,Object> result = new HashMap<String,Object>();
    	MongoDBManager mongoDBManager = (MongoDBManager)SpringHelper.getBean("mongoDBManager");
    	Area updateArea = new Area();
    	updateArea.setEmployee_a_no(null);
    	updateArea.setEmployee_b_no(null);
    	updateArea.setStore_id(area.getStore_id());
    	updateArea.setId(area.getId());
		Map<String, Object> resetResult = mongoDBManager.updateEmployeeOfTinyArea(updateArea);
		if(Integer.parseInt(resetResult.get("code").toString())==200){
			List<?> list_area_info = (List<?>)super.getList(FilterFactory.getSimpleFilter("area_id",area.getId()));
            if(list_area_info != null && list_area_info.size() > 0){
                for(Object areaInfo : list_area_info){
                    super.removeObject(areaInfo);
                }
            }
		}else {
			throw new MyException("更新tiny_area或者mongodb国安侠失败");
		}
        	
		
    }
    
    
    
    
    @Override
    public List<Map<String, Object>> queryAreaInfoByAreaId(String area_id) {
    	AreaInfoDao areaInfoDao = (AreaInfoDao) SpringHelper.getBean(AreaInfoDao.class.getName());
    	return areaInfoDao.queryAreaInfoByAreaId(area_id);
    }




	@Override
	public List<AreaInfo> findAreaInfoByTinyvillageId(Long tinyvillageId) {
		List<?> list_data = this.getList(FilterFactory.getSimpleFilter("tin_village_id="+tinyvillageId+" and status=0 "));
		if(list_data != null && list_data.size() > 0){
            return (List<AreaInfo>)list_data;
        }
		return null;
	}




	@Override
	public List<TinyVillage> findAreaInfoByTinyvillageIds(String tinyids) {
		List<TinyVillage> list = new ArrayList<TinyVillage>();
		TinyVillageManager tinyVillageManager=(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
		AreaInfoDao areaInfoDao=(AreaInfoDao)SpringHelper.getBean(AreaInfoDao.class.getName());
		List<AreaInfo> list_data = areaInfoDao.findAreaInfoByString(tinyids);
		if(list_data != null && list_data.size() > 0){
			for (AreaInfo areaInfo : list_data) {
				TinyVillage tinyVillage = tinyVillageManager.getTinyVillageById(areaInfo.getTin_village_id());
				if(tinyVillage!=null){
					list.add(tinyVillage);
				}
			}
            return list;
        }
		return null;
	}




	@Override
	public TinyVillage findTinyVillageAreaByTinyId(Long tinyId) {
		AreaInfoDao areaInfoDao=(AreaInfoDao)SpringHelper.getBean(AreaInfoDao.class.getName());
		List<Map<String, Object>> list = areaInfoDao.findAreaInfoBytinyId(tinyId);
		if(list!=null&&list.size()>0){
			TinyVillageManager tinyVillageManager =(TinyVillageManager)SpringHelper.getBean("tinyVillageManager");
			TinyVillage village = tinyVillageManager.getTinyVillageById(tinyId);
			return village;
		}
		return null;
	}




	@Override
	public List<TinyVillage> findTinyVillageAreaByTindIds(String tinyIds) {
		ArrayList<TinyVillage> list = new ArrayList<TinyVillage>();
		String[] split = tinyIds.split(",");
		for (String string : split) {
			TinyVillage tinyVillage = findTinyVillageAreaByTinyId(Long.parseLong(string));
			if(tinyVillage!=null){
				list.add(tinyVillage);
			}
		}
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
    
    
    
}
