package com.cnpc.pms.slice.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.slice.dto.AreaDto;
import com.cnpc.pms.slice.entity.Area;
import com.cnpc.pms.slice.entity.AreaInfo;

/**
 * 片区业务类 Created by liuxiao on 2016/10/25.
 */
public interface AreaManager extends IManager {

	Area queryArea(Long id);

	Area saveArea(Area area);

	Area deleteArea(Long id);

	Area updateAreaEmployeeNo(Area area);

	Area queryAreaByAreaNo(String area_no);

	/**
	 * 
	 * TODO 检查片区信息是否已经存在 2017年3月3日
	 * 
	 * @author gaobaolei
	 */
	public List<AreaInfo> checkAreaIsRepeat(Area area, String actionType);

	/**
	 * 
	 * TODO 检测相同街道或者门店的片区名称是否重复 2017年3月9日
	 * 
	 * @author gaobaolei
	 * @param area
	 * @param actionType
	 * @return
	 */
	public boolean checkNameIsExist(Area area, String actionType);

	/**
	 * 
	 * TODO 搜索片区合并的信息 2017年3月9日
	 * 
	 * @author gaobaolei
	 * @param ad
	 * @return
	 */
	public Map<String, Object> selectAreaDto(QueryConditions queryConditions);

	/**
	 * 
	 * TODO 导出片区信息 2017年4月5日
	 * 
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 * @throws Exception
	 */
	public String exportAreaInfo(AreaDto areaDto) throws Exception;

	public Map<String, Object> queryAreaByEmployeeNo(String employee_no, Long area_id);

	public Area queryAreaByCurrEmployeeNo();

	public Area queryAreaByAreaName(String area_name, Long store_id);

	public String getAreaInfoOfStore(Long store_id);

	public void clearAreaInfo();

	/**
	 * 
	 * TODO 检测员工是否已经负责某个片区 2017年9月7日
	 * 
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public Area checkAreaByEmployeeNo(String employeeNo, String actionType, Long area_id);

	/**
	 * 
	 * TODO 重置当前离职国安侠所管理片区信息 2017年9月13日
	 * 
	 * @author gaobaolei
	 * @param employeeNo1
	 *            离职人员编号
	 * @param employeeNo2
	 *            hrID
	 * 
	 * @return
	 */
	public String clearAreaOfEmployee(String employeeNo1, String employeeNo2);

	/**
	 * 
	 * TODO 2017年9月27日
	 * 
	 * @author gaobaolei
	 * @param store_id
	 * @return
	 */
	public Map<String, Object> getAreaInfoOfTinVillageByStore(Long store_id);

	/**
	 * 
	 * TODO 查询城市级别 和运营经理管理的门店 2017年10月10日
	 * 
	 * @author gaobaolei
	 * @param employeeId
	 * @param role
	 * @return
	 */
	public List<Map<String, Object>> getStoreOfByCSZJ_QYJL(Long employeeId, Long cityId, String role);

	/**
	 * 
	 * TODO 查询需要划片的国安侠 2017年10月17日
	 * 
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> queryEmployeeOfGAX(Long storeId);

	/**
	 * 
	 * TODO 获取国安侠所有片区 2017年11月10日
	 * 
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public List<Map<String, Object>> queryAreaByEmployee(String employeeNo);

	/**
	 * 
	 * TODO 设置岗位异动国安侠置空 2017年11月24日
	 * 
	 * @author gaobaolei
	 * @param storeNo
	 * @param employeeNo
	 * @return
	 */
	public Map<String, Object> updateEmployeeIsNullOfArea(Long storeId, String employeeNo);

	/**
	 * 
	 * TODO 2017年11月28日
	 * 
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public Map<String, Object> getTinyVillageCoord(Long storeId);

	/**
	 * 
	 * TODO 查询门店 片区 2018年1月23日
	 * 
	 * @author gaobaolei
	 * @param areaNo
	 * @return
	 */
	public Map<String, Object> getTinyVillageCoordOfArea(Long storeId, String areaNo);

	/**
	 * 
	 * TODO 验证当前门店 2017年12月1日
	 * 
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public Map<String, Object> checkCurrentStore(Long storeId);

	/**
	 * 
	 * TODO 查询门店服务范围和门店中心 2017年12月5日
	 * 
	 * @author gaobaolei
	 * @return
	 */
	public Map<String, Object> getStoreServiceAreaAndPosition(Long storeId);

	/**
	 * 
	 * TODO 获取门店片区数量 2017年12月6日
	 * 
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public Integer getAreaCountOfStore(Long storeId);

	/**
	 * 
	 * TODO 查询片区相关信息 2018年1月11日
	 * 
	 * @author gaobaolei
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryAboutOfArea(DynamicDto dynamicDto, PageInfo pageInfo);

	/**
	 * 
	 * TODO 导出片区相关信息 2018年1月12日
	 * 
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> exportAboutOfArea(DynamicDto dynamicDto);

	/**
	 * 
	 * TODO 检测划片小区是否绑定坐标 2018年1月17日
	 * 
	 * @author gaobaolei
	 * @param area
	 * @return
	 */
	public List<AreaInfo> checkTinyVillageAboutCoordinate(Area area);

	/**
	 * 
	 * TODO 获取片区住户、小区等信息 2018年1月24日
	 * 
	 * @author gaobaolei
	 * @param areaNo
	 * @return
	 */
	public Map<String, Object> getDataOfArea(String areaNo);

	/**
	 * 根据小区获取片区详情
	 * 
	 * @author sunning
	 * @param tinyId
	 * @return
	 */
	Map<String, Object> findAreaInfoByTinyId(Long tinyId);
	
	/**
	 * 
	 * TODO 查询片区所辖小区坐标录入信息
	 * 2018年2月2日
	 * @author gaobaolei
	 * @param storeId
	 * @param areaNo
	 * @return
	 */
	public Map<String, Object> selectTinyVillageCoordOfArea(Long storeId,String areaNo);
	
	
}
