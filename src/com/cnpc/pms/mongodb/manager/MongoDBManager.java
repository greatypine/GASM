/**
 * gaobaolei
 */
package com.cnpc.pms.mongodb.manager;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.json.JSONObject;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.mongodb.dto.TinyVillageCoordDto;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreOrderInfo;
import com.cnpc.pms.slice.entity.Area;
import com.cnpc.pms.slice.entity.AreaInfo;
import com.mongodb.client.MongoCollection;


/**
 * @author gaobaolei
 * 高德地理坐标
 */
public interface MongoDBManager extends IManager{
	
	/**
	 * 
	 * TODO 获取门店所辖所有小区 
	 * 2017年11月10日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public Map<String,Object> getAllTinyVillageOfStore(Long storeId);
	
	
	/**
	 * 
	 * TODO 获取门店服务范围坐标 
	 * 2017年11月10日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public Map<String,Object> getStoreServiceArea(String paltformId);
	
	/**
	 * 
	 * TODO 国安侠所管理的小区 
	 * 2017年12月06日
	 * @author cps
	 * @param storeId
	 * @return
	 */
	public Map<String,Object> selecTinyVillageCoordByEmployeeNo(String employeeNo);
	
	/**
	 * 
	 * TODO 保存小区坐标地址 
	 * 2017年11月10日
	 * @author gaobaolei
	 * @param tCoordDto
	 * @return
	 */
	public Map<String, Object> saveTinyVillageCoord(TinyVillageCoordDto tCoordDto);
	
	/**
	 * 
	 * TODO 查询小区坐标地址 
	 * 2017年11月13日
	 * @author gaobaolei
	 * @param tCoordDto
	 * @return
	 */
	public Map<String, Object> selecTinyVillageCoord(Long  storeId);
	
	/**
	 * 
	 * TODO 删除小区坐标 
	 * 2017年11月13日
	 * @author gaobaolei
	 * @param tCoordDto
	 * @return
	 */
	public Map<String,Object> deleteTinyVillageCoord(TinyVillageCoordDto tCoordDto);
	
	/**
	 * 
	 * TODO 查询小区交集 
	 * 2017年11月14日
	 * @author gaobaolei
	 * @param tCoordDto
	 * @return
	 */
	public  Map<String, Object> getTinyVillageCoordOfIntersection(String code,List<Double[]> coordList);
	
	/**
	 * 
	 * TODO 获取 门店中心坐标
	 * 2017年11月14日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public  Map<String,Object> getStorePosition(Long storeId);
	
	/**
	 * 
	 * TODO 更新国安侠 
	 * 2017年11月14日
	 * @author gaobaolei
	 * @param areaId
	 * @return
	 */
	public Map<String, Object> updateEmployeeOfTinyArea(Area area);
	
	/**
	 * 
	 * TODO  查询小区是否已经被绑定
	 * 2017年11月16日
	 * @author gaobaolei
	 * @param collection
	 * @param storeNo
	 * @param tinyVillageCode
	 * @return
	 */
	public Map<String,Object> getExistCoordOfTinyVillage(Store store,MongoCollection<Document> collection,String tinyVillageCode);
	
	/**
	 * 
	 * TODO 查询城市的小区坐标
	 * 2017年11月23日
	 * @author gaobaolei
	 * @param city_id
	 * @return
	 */
	public Map<String,Object> getAllTinyVillageCoordinateOfCity(Long city_id);
	
	/**
	 * 
	 * TODO  查询城市的门店服务范围
	 * 2017年11月24日
	 * @author gaobaolei
	 * @param city_id
	 * @return
	 */
	public Map<String,Object> getAllStoreServiceAreaOfCity(Long city_id);
	
	/**
	 * 
	 * TODO  查询所有小区坐标
	 * 2017年12月7日
	 * @author gaoll
	 * @return
	 */
	public Map<String,Object> getAllCoordinates();
	
	/**
	 * 
	 * TODO  查询未保存面积的小区坐标
	 * 2017年12月8日
	 * @author gaoll
	 * @return
	 */
	public Map<String,Object> getCoordinatesWithoutArea();
	
	/**
	 * 
	 * TODO  统计时间段某个小区下订单的总数
	 * 2017年12月12日
	 * @author caops
	 * @return
	 */
	public  int  selecTinyOrderCount(String beginDate,String endDate,String tinycode);
	/**
	 * 
	 * TODO  查询某个门店下所有小区
	 * 2017年12月12日
	 * @author caops
	 * @return
	 */
	public  Map<String, Object>  selectStoreTinies();
	
	/**
	 * 
	 * TODO  查询小区上月的订单数
	 * 2017年12月13日
	 * @author zhangli
	 * @return
	 */
	List<JSONObject>  selecTinyOrderLastMonthCountByVillage();

	/**
	 * 
	 * TODO  查询小区上月的用户数
	 * 2017年12月13日
	 * @author zhangli
	 * @return
	 */
	public List<JSONObject> selecTinyCustomerLastMonthCountByVillage();
	
	/**
	 * 总部获取高德门店范围
	 * @param city_id
	 * @return
	 */
	public Map<String,Object> getAllStoreServiceAreaOfContry(Long city_id);
	

	/**
	 * 
	 * TODO  根据员工id查询国安侠行驶记录
	 * 2017年12月14日
	 * @author gaoll
	 * @return
	 */
	public Map<String,Object> selectEmployeeDriveRecode(List<Map<String, Object>> list,String beginDate,String endDate);
	
	/**
	 * 
	 * TODO 重置新的片区关联的小区坐标国安侠 
	 * 2018年1月19日
	 * @author gaobaolei
	 * @param area
	 * @return
	 */
	public Map<String, Object> updateTinyAreaOfEmployee(Area area);
	

	/**
	 * 
	 * TODO  查询片区下的小区范围
	 * 2018年1月11日
	 * @author gaoll
	 * @return
	 */
	public Map<String,Object> selectCoordinatesOfArea(String area_no);
	
	/**
	 * 
	 * TODO  根据小区code查询小区坐标范围
	 * 2018年1月12日
	 * @author gaoll
	 * @return
	 */
	public Map<String,Object> selectCoordinatesOfCode(String code);
	
	/**
	 * 
	 * TODO 设置tiny_area A国安侠为空 
	 * 2018年1月30日
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public  Map<String,Object> updateTinyAreaEmployeeIdNull(Long storeId,String employeeNo);
	
	/**
	 * 
	 * TODO 查询国安侠运行轨迹 
	 * 2018年3月7日
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public Map<String, Object> queryEmployeeDiveRecord(String employeeNo);
	
	
	
	//保存mongo工单信息
	public Map<String, Object> saveStoreOrderInfo(StoreOrderInfo storeOrderInfo);
}
