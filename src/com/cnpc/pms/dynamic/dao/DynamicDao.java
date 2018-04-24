/**
 * gaobaolei
 */
package com.cnpc.pms.dynamic.dao;

import java.io.DataOutput;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.AbnormalOrderDto;
import com.cnpc.pms.dynamic.entity.DynamicDto;



/**
 * @author gaobaolei
 *  社区动态
 */
public interface DynamicDao extends IDAO{
	
	/**
	 * 
	 * TODO 门店拉新
	 * 2017年7月26日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param year
	 * @param month
	 * @return
	 */
	public int  getNewaddcus(DynamicDto dd);
	
	/**
	 * 
	 * TODO  国安侠拉新
	 * 2018年1月26日
	 * @author gaobaolei
	 * @param dd
	 * @return
	 */
	public int  getNewaddcusOfGAX(DynamicDto dd);
	
	/**
	 * 
	 * TODO 获取复购客户数量 
	 * 2017年7月26日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param year
	 * @param month
	 * @return
	 */
	public int  getRebuycus(DynamicDto dd);
	
	/**
	 * 
	 * TODO 门店gmv
	 * 2017年7月26日
	 * @author gaobaolei
	 * @edit 2018年1月26日
	 * @param cityId
	 * @param employeeId
	 * @param year
	 * @param month
	 * @return
	 */
	public double  getStoretrade(DynamicDto dd);
	
	/**
	 * 
	 * TODO 国安侠GMV 
	 * 2018年1月26日
	 * @author gaobaolei
	 * @param dd
	 * @return
	 */
	public double  getStoretradeOfGAX(DynamicDto dd);
	
	/**
	 * 
	 * TODO 获取送单量
	 * 2017年7月26日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param year
	 * @param month
	 * @return
	 */
	public int  getSendorders (DynamicDto dd);
	
	/**
	 * 
	 * TODO  获取好评次数
	 * 2017年7月26日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param year
	 * @param month
	 * @return
	 */
	public int  getRewardtimes(DynamicDto dd);
	
	/**
	 * 
	 * TODO 获取拜访记录 
	 * 2017年7月26日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param year
	 * @param month
	 * @return
	 */
	public int  getRelation(DynamicDto dd);
	
	/**
	 * 
	 * TODO 获取单体画像 
	 * 2017年7月26日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param year
	 * @param month
	 * @return
	 */
	public int  getCustomer(DynamicDto dd);
	
	/**
	 * 
	 * TODO 商业信息 
	 * 2017年7月26日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param year
	 * @param month
	 * @return
	 */
	public int  getBusinessInfo(DynamicDto dd);
	
	/**
	 * 
	 * TODO 查询商业信息 
	 * 2017年8月9日
	 * @author gaobaolei
	 * @param dd
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryBusiness(DynamicDto dd,PageInfo pageInfo);
	
	public List<Map<String, Object>> queryBusiness(DynamicDto dd);
	
	/**
	 * 
	 * TODO 商业楼宇 
	 * 2017年7月26日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param year
	 * @param month
	 * @return
	 */
	public int  getOfficeInfo(DynamicDto dd);
	
	/**
	 * 
	 * TODO  查询写字楼
	 * 2017年8月9日
	 * @author gaobaolei
	 * @param dd
	 * @param pageinfo
	 * @return
	 */
	public Map<String,Object> queryOffice(DynamicDto dd,PageInfo pageinfo);
	
	public List<Map<String,Object>> queryOffice(DynamicDto dd);
	
	/**
	 * 
	 * TODO 查询交易额top10 
	 * 2017年7月26日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Map<String, Object>>  getStoretradeList(Long cityId,Long employeeId,Integer year,Integer month,String flag);
	
	/**
	 * 
	 * TODO 查询订单top10 
	 * 2017年7月26日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Map<String, Object>>  getStoreOrderList(Long cityId,String employeeId,Integer year,Integer month,String flag);
	
	/**
	 * 
	 * TODO 查询拜访记录top10 
	 * 2017年7月26日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Map<String, Object>>  getRelationList(Long cityId,String employeeId,Integer year,Integer month,String flag);
	
	/**
	 * 
	 * TODO 获取覆盖的住户 
	 * 2017年7月28日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @return
	 */
	public int getAllHouseAmount(Long cityId,Long employeeId,Integer house_type);
	
	/**
	 * 
	 * TODO 获取覆盖的小区 
	 * 2017年7月28日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @return
	 */
	public int getAllTinyVillageAmount(Long cityId,Long employeeId);
	
	/**
	 * 
	 * TODO 获取覆盖的社区 
	 * 2017年7月28日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @return
	 */
	public int getAllVillageAmount(Long cityId,Long employeeId);
	
	/**
	 * 
	 * TODO 获取覆盖的街道 
	 * 2017年7月28日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @return
	 */
	public int getAllTownAmount(Long cityId,Long employeeId);
	
	

	/**
     * 
     * TODO 查找异常订单 (分页)
     * 2017年9月19日
     * @author gaobaolei
     * @param param
     * @return
     */
    public Map<String, Object> queryAbnormalOrder(AbnormalOrderDto abnormalOrderDto,PageInfo pageInfo);
    
    
    public List<Map<String, Object>> queryAbnormalOrder(AbnormalOrderDto abnormalOrderDto);
    
    /**
     * 
     * TODO  查找异常订单类型
     * 2017年9月19日
     * @author gaobaolei
     * @return
     */
    public List<Map<String, Object>> queryAbnormalType();
    
    /**
     * 
     * TODO 查询异常订单类型是否存在 
     * 2017年10月18日
     * @author gaobaolei
     * @param descrip
     * @return
     */
    public List<Map<String, Object>> queryAbnormalType(String descrip);
    
    /**
     * 
     * TODO 根据订单号查询异常订单 
     * 2017年9月21日
     * @author gaobaolei
     * @param ordersn
     * @return
     */
    public List<Map<String, Object>> queryAbnormalByOrderSn(String ordersn);
    
    /**
     * 
     * TODO 查找员工管理的城市 
     * 2017年9月27日
     * @author gaobaolei
     * @param userid
     * @param name
     * @return
     */
    public List<Map<String, Object>> queryCityByUser(Integer target,Long userid,String name);
    
    /***
     * 
     * TODO  查询各门店事业部的服务专员交易额
     * 2017年10月24日
     * @author gaobaolei
     * @param storeno
     * @return
     */
    public List<Map<String, Object>>  queryStoreTradeOfDept(DynamicDto dynamicDto);
    
    
    /***
     * 
     * TODO  查询城市上个月GMV排名-带分页
     * 2017年12月14日
     * @author zhangli
     * @param dd
     * @return
     */
	public Map<String, Object> getLastMonthGMVCityRankingTop10(DynamicDto dd,PageInfo pageInfo);
	
	 /***
     * 
     * TODO  查询门店上个月GMV排名
     * 2017年12月14日
     * @author zhangli
     * @param dd
     * @return
     */
	public Map<String, Object> getLastMonthGMVStoreRankingTop10(DynamicDto dd,PageInfo pageInfo);
    /**
     * 
     * TODO 查询已开店的所有城市 
     * 2017年12月13日
     * @author gaobaolei
     * @return
     */
    public List<Map<String, Object>> selectAllCity();
    /**
     * 总部查看数据：按事业部交易额排名
     */
    public Map<String, Object>  queryTradeByDepName(DynamicDto dynamicDto,PageInfo pageInfo);
    /**
     * 总部查看数据：按频道交易额排名
     */
    public Map<String, Object>  queryTradeByChannelName(DynamicDto dynamicDto,PageInfo pageInfo);
    /**
     * 总部查看数据：按频道订单量排名
     */
    public Map<String, Object>  queryOrderCountByChannelName(DynamicDto dynamicDto,PageInfo pageInfo);
    
    /**
     * 查询城市个数
     */
    /**
     * 
     * TODO update
     * 2017年12月27日
     * @author gaobaolei 
     * @param dd
     * @return
     */
	public List<Map<String, Object>> findCityCount(DynamicDto dd);

	/**
     * 查询门店个数
     */
	/**
	 * 
	 * TODO update 
	 * 2017年12月27日
	 * @author gaobaolei
	 * @param dd
	 * @return
	 */
	/**
	 * 
	 * TODO update 
	 * 2017年12月27日
	 * @author gaobaolei
	 * @param dd
	 * @return
	 */
	public List<Map<String, Object>> findStoreCount(DynamicDto dd);
	/**
     * 查询门店人数
     */
	public List<Map<String, Object>> findStoreKeeperCount(DynamicDto dd);
	/**
     * 查询国安侠人数
     */
	Map<String, Object> findGaxCount(DynamicDto dd,PageInfo pageInfo);
	/**
     * 查询门店在全国的排名
     */
	public List<Map<String, Object>> getLastMonthGMVStoreChinaRanking(DynamicDto dd);

	/**
	 * 查询全国门店订单数排名
	 */
	public Map<String, Object> CityOrderRankingTop10(DynamicDto dd,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 查询门店各个片区的GMV 
	 * 2017年12月19日
	 * @author gaobaolei
	 * @param storeNo
	 * @return
	 */
	public Map<String, Object> selectAreaRankingOfStore(DynamicDto dynamicDto,PageInfo pageInfo); 
	
	/**
	 * 
	 * TODO 查询门店各个事业部的GMV 
	 * 2017年12月19日
	 * @author gaobaolei
	 * @param storeNo
	 * @return
	 */
	public List<Map<String, Object>> selectDeptRankingOfStore(DynamicDto dynamicDto);
	
	/**
	 * 
	 * TODO 查询门店各个渠道的GMV 
	 * 2017年12月19日
	 * @author gaobaolei
	 * @param storeNo
	 * @return
	 */
	public Map<String, Object> selectChannelRankingOfStore(DynamicDto dynamicDto,PageInfo pageInfo);
	/**
	 * 
	 * TODO 查询当月累计营业额
	 * 2017年12月20日
	 * @author caops
	 * @param storeNo
	 * @return
	 */
	public Map<String, Object> queryTradeSumByMonth(DynamicDto dynamicDto);
	

	
	/**
	 * 
	 * TODO 查询国安侠的GMV
	 * 2017年12月20日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> selectEmployeeRankingOfStore(DynamicDto dynamicDto,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 查询门店事业部服务专员gmv 
	 * 2017年12月21日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public List<Map<String, Object>> selectDeptServerRanking(DynamicDto dynamicDto);
	
	/**
	 * 
	 * TODO 查询片区订单量 
	 * 2017年12月21日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> selectAreaOrderRanking(DynamicDto dynamicDto,PageInfo pageInfo);
	
	/**
	 * 查询频道订单量
	 * TODO  
	 * 2017年12月21日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> selectChannelOrderRanking(DynamicDto dynamicDto,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 查询门店交易额
	 * 2017年12月21日
	 * @author gaobaolei
	 * @param dynamicDao
	 * @return
	 */
	public List<Map<String, Object>> selectGMVOfStore(DynamicDto dynamicDto);

	/**
	 * 
	 * TODO 查询当月国安侠Gmv
	 * 2017年12月20日
	 * @author caops
	 * @param storeNo
	 * @return
	 */
	public Map<String, Object> queryAreaTradeByEmp(DynamicDto dynamicDto,PageInfo pageInfo);
	/**
	 * 
	 * TODO 查询当月服务专员Gmv
	 * 2017年12月20日
	 * @author caops
	 * @param storeNo
	 * @return
	 */
	public List<Map<String, Object>> queryServerTradeByEmp(DynamicDto dynamicDto);
	/**
	 * 
	 * TODO 查询历史累计营业额
	 * 2017年12月20日
	 * @author caops
	 * @param storeNo
	 * @return
	 */
	public Map<String, Object> queryTradeSumOfHistory(DynamicDto dynamicDto);
	
	/**
	 * 
	 * TODO 查询门店了历史交易额 
	 * 2017年12月22日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public List<Map<String, Object>> selectHistoryGMVOfStore(DynamicDto dynamicDto);
	
	/**
	 * 
	* @Title: queryProductCityOrder 
	* @Description: 从GASM中ds_product_city表获取数据
	* @param @param dynamicDto
	* @param @return    设定文件 
	* @return List<Map<String,Object>>    返回类型 
	* @throws
	 */
	public Map<String, Object> queryProductCityOrder(DynamicDto dynamicDto,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 门店GMV （新）
	 * 2018年1月24日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> storeGmv(DynamicDto dynamicDto,PageInfo pageInfo);

	
	/**
	 * 
	 * TODO 国安侠（片区）GMV （新）
	 * 2018年1月24日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> employeeOfAreaGmv(DynamicDto dynamicDto,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 门店拉新（新） 
	 * 2018年1月24日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public  Map<String, Object> storeNewaddcus(DynamicDto dynamicDto,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 国安侠（片区）拉新 （新） 
	 * 2018年1月24日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public  Map<String, Object> employeeOfAreaNewaddcus(DynamicDto dynamicDto,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 获取国安侠拉新（每月） 
	 * 2018年1月29日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public  Map<String, Object> getGaxOfAreaNewaddcus(DynamicDto dynamicDto,PageInfo pageInfo);
	
	/**
	 * 通过genmini里store_id查询GASM中store_id
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> queryPlatformidByStoreId(String storeId);
	
	/**
	 * 通过genmini里store_id查询GASM中store_id
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> queryAllCityCode();
	
	
	/**
	 * 查询所有的事业群
	 * @return
	 */
	public List<Map<String, Object>> queryCityAllDept();
	
	
	/**
	 * 查询当月成交用户量
	 * @param dd
	 * @return
	 */
	List<Map<String, Object>> queryMonthCustomerCount(DynamicDto dd);

	/**
	 * 查询本周每日用户量
	 * @param cityNo
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	Map<String, Object> queryCustomerCountByTime(DynamicDto dd);
	/**
	 * 查询指定时间内每日营业额
	 * @param beginData
	 * @param endData
	 * @return
	 */
	Map<String, Object> queryTurnoverByTime(String cityNo,String beginData,String endData);
	/**
	 * 查询本月累计用户量(按照城市进行分组)
	 * @param dd
	 * @param cityNO
	 * @param provinceNO
	 * @param string
	 * @return
	 */
	List<Map<String, Object>> queryMonthCustomerCountGroup(DynamicDto dd,List<Map<String, Object>> cityNO,List<Map<String, Object>> provinceNO, String key);

	/**
	 * 查询某年累计营业额
	 * @param dd
	 * @param cityNO
	 * @param provinceNO
	 * @param string
	 * @return
	 */
	public List<Map<String, Object>> queryYearSumGMV(DynamicDto dd,String cityId,String provinceId, String string);
	/**
	 * 查询本月每日拉新用户量
	 * @param dd
	 * @param cityNO
	 * @param provinceNO
	 * @return
	 */
	public Map<String, Object> queryNewMonthUserCount(DynamicDto dd,List<Map<String, Object>> cityNO,List<Map<String, Object>> provinceNO);
	/**
	 * 查询本周每日复购率,拉新用户量,消费用户量
	 * @param dd
	 * @return
	 */
	public Map<String, Object> getWeekCustomerOrderRate(DynamicDto dd);
	
	/**
	 * 查询历史成交用户量
	 * @param dd
	 * @param cityNO
	 * @param provinceNO
	 * @param object
	 * @return
	 */
	public List<Map<String, Object>> queryHistoryCustomerCount(DynamicDto dd);
	/**
	 * 查询总部当月成交用户量
	 * @param dd
	 * @return
	 */
	public List<Map<String, Object>> queryMonthZbCustomerCount(DynamicDto dd);
	/**
	 * 查询总部历史成交用户量
	 * @param dd
	 * @return
	 */
	public List<Map<String, Object>> queryHistoryZbCustomerCount(DynamicDto dd);
}
