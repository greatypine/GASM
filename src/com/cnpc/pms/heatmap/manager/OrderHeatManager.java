package com.cnpc.pms.heatmap.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.heatmap.entity.OrderHeatDto;
/**
 * 
 * @author gaoll
 *
 */
public interface OrderHeatManager{
	
	public Map<String,Object> getLagLatByOrder(OrderHeatDto orderHeatDto);
	
	public Map<String,Object> getLatLngByVillage(OrderHeatDto orderHeatDto);
	
	public Map<String,Object> getOrderHeatMap(OrderHeatDto orderHeatDto);
	
	public Map<String, Object> selecTinyVillageCoord(OrderHeatDto orderHeatDto);
	
	public Map<String,Object> selecTinyVillageCoordByCity(OrderHeatDto orderHeatDto);

}
