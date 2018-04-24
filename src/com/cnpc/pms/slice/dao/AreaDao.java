package com.cnpc.pms.slice.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.dao.DynamicDao;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.slice.entity.Area;
import com.cnpc.pms.slice.entity.AreaInfo;

/**
 * 划片
 * 
 * @author gaobaolei
 *
 */
public interface AreaDao {

	/**
	 * 
	 * TODO 搜索社区\小区\楼 2017年3月6日
	 * 
	 * @author gaobaolei
	 * @param ai
	 */
	public List<Map<String, Object>> selectAreaInfo(AreaInfo ai, String actionType);

	/**
	 * 
	 * TODO 搜索片区 2017年3月9日
	 * 
	 * @author gaobaolei
	 * @param area
	 * @param actionType
	 * @return
	 */
	public int selectArea(Area area, String actionType) throws Exception;

	/**
	 * 
	 * TODO 搜索片区合并信息 2017年3月9日
	 * 
	 * @author gaobaolei
	 * @param ad
	 * @return
	 */
	public List<Map<String, Object>> selectAreaDto(String whereStr, PageInfo pi) throws Exception;

	/**
	 * 
	 * TODO 搜索片区信息（非分页） 2017年4月5日
	 * 
	 * @author gaobaolei
	 * @param whereStr
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectAreaDto(Map<String, Object> param) throws Exception;

	/**
	 * 根据国安侠a的 员工编号 查询所划片里的所有小区
	 */
	public String querytinvillagebyemployeeno(String employee_no, Long area_id);

	/**
	 * 分片区查询片区下的所有小区
	 * 
	 * @param store_id
	 */
	public List<Map<String, Object>> queryTinVillageByStoreId(Long store_id);

	/**
	 * 
	 * TODO 查询某员工的 所有片区 2017年6月2日
	 * 
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public List<Map<String, Object>> getAllAreaOfEmployee(String employeeNo);

	public List<Map<String, Object>> getAreaInfoOfStore(Long store_id);

	/**
	 * 
	 * TODO 获取片区详情 2017年9月13日
	 * 
	 * @author gaobaolei
	 * @param area_id
	 * @return
	 */
	public List<AreaInfo> queryAreaInfoByAreaId(Long area_id);

	/**
	 * 
	 * TODO 查询国安侠管理片区 2017年9月13日
	 * 
	 * @author gaobaolei
	 * @param employeeNo
	 * @param storeId
	 * @return
	 */
	public List<Area> queryAreaByEmployeeAndStore(String employeeNo, Long storeId, String ab);

	/**
	 * 
	 * TODO 查询门店未划片的小区 2017年9月27日
	 * 
	 * @author gaobaolei
	 * @param store_id
	 * @return
	 */
	public List<Map<String, Object>> queryNoTinyVillageByStore(Long store_id);

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
	 * TODO 更新片区国安侠为空 2017年11月24日
	 * 
	 * @author gaobaolei
	 * @param storeId
	 * @param employeeId
	 * @return
	 */
	public void updateEmployeeIsNullOfArea(Long storeId, String employeeId) throws Exception;

	public void updateEmployeeIsNullOfAreaInfo(Long storeId, String employeeId) throws Exception;

	/**
	 * 
	 * TODO 获取门店的片区 2017年11月29日
	 * 
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> selectAreaOfStore(Long storeId);

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
	 * TODO 查询划片小区是否录入坐标 2018年1月17日
	 * 
	 * @author gaobaolei
	 * @param area
	 * @return
	 */
	public List<Map<String, Object>> selectTinyArea(AreaInfo areaInfo);

	/**
	 * 
	 * TODO 查询片区小区信息 2018年1月24日
	 * 
	 * @author gaobaolei
	 * @param areaNo
	 * @return
	 */
	public List<Map<String, Object>> selectTinyVillageOfArea(String areaNo);

	/**
	 * 
	 * TODO 查询片区住户 2018年1月24日
	 * 
	 * @author gaobaolei
	 * @param areaNo
	 * @return
	 */
	public List<Map<String, Object>> selectHouseAmountOfArea(String areaNo);

	/**
	 * 
	 * TODO 片区信息 2018年1月24日
	 * 
	 * @author gaobaolei
	 * @param areaNo
	 * @return
	 */
	public List<Map<String, Object>> selectAreaByAreaNo(String areaNo);

	/**
	 * 根据小区id查询片区及A国安侠信息
	 * 
	 * @param tinyId
	 * @return
	 */
	List<Map<String, Object>> findAreaInfoById(Long tinyId);
	
	/**
	 * 
	 * TODO 查询片区相关绩效数据（订单） 
	 * 2018年3月6日
	 * @author gaobaolei
	 * @param areaNo
	 * @return
	 */
	public List<Map<String, Object>> queryOrderOfArea(DynamicDto dynamicDto);
	
	/**
	 * 
	 * TODO 查询片区绩效_事业群
	 * 2018年3月6日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public List<Map<String, Object>> queryPerfOfAreaByDept(DynamicDto dynamicDto);
	
	/**
	 * 
	 * TODO 查询片区绩效_频道
	 * 2018年3月6日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public List<Map<String, Object>> queryPerfOfAreaByChannel(DynamicDto dynamicDto);
	
	/**
	 * 
	 * TODO 查询片区热力图（消费用户、订单、gmv） 
	 * 2018年3月6日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public List<Map<String, Object>> queryHeatMapOfArea(DynamicDto dynamicDto);
	

	
	/**
	 * 
	 * TODO 查询片区一段时间内每天营业额
	 * 2018年3月13日
	 * @author gaobaolei
	 * @return
	 */
	public List<Map<String, Object>> queryGMVOfEveryDayByArea(DynamicDto dynamicDto);
	
	/**
	 * 
	 * TODO 查询一段时间内每天消费用户 
	 * 2018年3月13日
	 * @author gaobaolei
	 * @return
	 */
	public List<Map<String, Object>> queryConsumerOfEveryDayByArea(DynamicDto dynamicDto);
	
	
	/**
	 * 
	 * TODO 查询一段时间内每天的拉新用户 
	 * 2018年3月13日
	 * @author gaobaolei
	 * @return
	 */
	public List<Map<String, Object>> queryNewCustomeOfEveryDayByArea(DynamicDto dynamicDto);
	
	
	/**
	 * 
	 * TODO 查询片区住户数 
	 * 2018年3月14日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public List<Map<String, Object>> queryHouseHoldNumber(DynamicDto dynamicDto);
}
