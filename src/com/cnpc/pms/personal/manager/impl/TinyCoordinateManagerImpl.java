package com.cnpc.pms.personal.manager.impl;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.entity.TinyCoordinate;
import com.cnpc.pms.personal.manager.TinyCoordinateManager;

public class TinyCoordinateManagerImpl extends BizBaseCommonManager implements TinyCoordinateManager{

	@Override
	public TinyCoordinate saOrUpdateTinyCoor(TinyCoordinate tinyCoordinate) {
		TinyCoordinate updatetinyCoordinate=null;
		TinyCoordinateManager tinyCoordinateManager=(TinyCoordinateManager)SpringHelper.getBean("tinyCoordinateManager");
		String string = tinyCoordinate.getCoordinates();
		Long tiny_id = tinyCoordinate.getTiny_id();
		//根据小区id获取所有绑定的坐标
		List<TinyCoordinate> list = findTinyCoordinateByTinyId(tiny_id);
		if(list!=null&&list.size()>0){
			for (TinyCoordinate tinyCoordinate2 : list) {
				tinyCoordinateManager.remove(tinyCoordinate2);
			}
		}
		String[] split = string.split(";");//坐标数组
		if(split!=null&&split.length>0){
			for (String string2 : split) {
				String[] split2 = string2.split(",");
				updatetinyCoordinate=new TinyCoordinate();
				updatetinyCoordinate.setGaode_coordinate_x(split2[0]);
				updatetinyCoordinate.setGaode_coordinate_y(split2[1]);
				updatetinyCoordinate.setTiny_id(tiny_id);
				preObject(updatetinyCoordinate);
				tinyCoordinateManager.saveObject(updatetinyCoordinate);
			}
			return updatetinyCoordinate;
		}
		return null;
	}

	@Override
	public List<TinyCoordinate> findTinyCoordinateByTinyId(Long tinyId) {
		 List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tiny_id",tinyId));
	        if(lst_data != null && lst_data.size() > 0){
	            return (List<TinyCoordinate>)lst_data;
	        }
	        return null;
	}

	@Override
	public List<Map<String, List<TinyCoordinate>>> findCoordinateData(Long tinyId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	

}
