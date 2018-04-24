package com.cnpc.pms.heatmap.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.heatmap.entity.RequestInfoDto;

public interface GAXDriveRecodeManager {
	
	/**
	 * 
	 * TODO  国安侠位置历史分布展示
	 * 2017年12月20日
	 * @author gaoll
	 * @param requestInfoDto
	 * @return
	 */
	public Map<String,Object> getDriveRecode(RequestInfoDto requestInfoDto);
	
	/**
	 * 
	 * TODO  查询城市管理下门店的服务范围及门店位置
	 * 2017年12月20日
	 * @author gaoll
	 * @param list
	 * @return
	 */
	public Map<String, Object> getStoreServiceAreaAndPosition(RequestInfoDto requestInfoDto);
	
	
	/**
	 * 
	 * TODO  查询国安侠当前是否在线
	 * 2017年1月05日
	 * @author gaoll
	 * @param list
	 * @return
	 */
	public Map<String,Object> getEmployeeStatus(String employeeNo);
	
	/**
	 * 
	 * TODO  国安侠分布事实展示
	 * 2017年12月20日
	 * @author gaoll
	 * @param requestInfoDto
	 * @return
	 */
	public Map<String,Object> getDynamicDriveRecode(RequestInfoDto requestInfoDto);
	
	/**
	 * 
	 * TODO  查询国安侠最新位置
	 * 2017年12月21日
	 * @author gaoll
	 * @param list
	 * @return
	 */
	public Map<String, Object> getEmployeePositions(List<Map<String,Object>> list);

}
