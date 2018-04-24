package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.exception.PMSManagerException;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.TinyCoordinate;

public interface TinyCoordinateManager extends IManager{
	TinyCoordinate saOrUpdateTinyCoor(TinyCoordinate tinyCoordinate);
	//根据小区id获得所有绑定的坐标
	List<TinyCoordinate>  findTinyCoordinateByTinyId(Long tinyId);
	//获取非当前划片小区的坐标
	List<Map<String, List<TinyCoordinate>>> findCoordinateData(Long tinyId);
	
	

}
