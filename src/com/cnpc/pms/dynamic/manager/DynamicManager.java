/**
 * gaobaolei
 */
package com.cnpc.pms.dynamic.manager;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.AbnormalOrderDto;
import com.cnpc.pms.dynamic.entity.DynamicDto;

import ar.com.fdvs.dj.domain.constants.Page;


/**
 * @author gaobaolei
 * 数据动态
 */
public interface DynamicManager extends IManager{

	/**
	 *
	 * TODO 社区动态数据卡 (非当前月)
	 * 2017年7月26日
	 * @author gaobaolei
	 * @param dd
	 * @return
	 */
	public  Map<String, Object> getDataCardInfo(DynamicDto dd);

	/**
	 *
	 * TODO   社区动态数据卡 (当前月)
	 * 2017年8月7日
	 * @author gaobaolei
	 * @param dd
	 * @return
	 */
	public Map<String, Object> getDataCardInfoOfCurMonth(DynamicDto dd);

	/**
	 *
	 * TODO  社区动态数据卡 (当天)
	 * 2017年8月7日
	 * @author gaobaolei
	 * @param dd
	 * @return
	 */
	public Map<String, Object> getDataCardInfoOfCurDay(DynamicDto dd);

	/**
	 * TODO  社区动态-当日成交额,成交量,成交用户量 (当天)
	 * @param dd
	 * @return
	 */
	public Map<String, Object> getDailyOrderOfCurDay(DynamicDto dd);
	/**
	 *
	 * TODO 获取每天的动态数据 
	 * 2017年7月27日
	 * @author gaobaolei
	 * @return
	 */
	public Map<String, Object> getDailyData(DynamicDto dd);

	/**
	 *
	 * TODO 获取某月的门店GMV数据 -按照GMV排序
	 * 2017年11月21日
	 * @author zhangli
	 * @return
	 */
	public Map<String, Object> getMonthData(DynamicDto dd);
	
	/**
	 *
	 * TODO 获取某月的门店城市GMV数据 -按照GMV排序
	 * 2017年12月18日
	 * @author zhangli
	 * @return
	 */
	public Map<String, Object> getMonthDataCity(DynamicDto dd);
	
	/**
	 *
	 * TODO 获取某月的门店GMV数据 -按照增幅排序
	 * 2017年11月22日
	 * @author zhangli
	 * @return
	 */
	public Map<String, Object> getMonthPesgmvPriordData(DynamicDto dd);
	
	/**
	 *
	 * TODO 搜索城市管辖的门店 
	 * 2017年7月31日
	 * @author gaobaolei
	 * @param employeeId
	 * @param cityId
	 * @param search_str
	 * @return
	 */
	public Map<String, Object> getStoreByCity(Integer target,Long employeeId,Long cityId,String search_str);

	/**
	 *
	 * TODO 付费用户查询
	 * 2017年8月1日
	 * @author gaobaolei
	 * @param query_date
	 * @param query_id
	 * @return
	 */
	public Map<String, Object> queryNewaddcus(DynamicDto dynamicDto,PageInfo pageInfo);

	/**
	 *
	 * TODO 导出付费用户 
	 * 2017年8月10日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> exportNewaddcus(DynamicDto dynamicDto);

	/**
	 *
	 * TODO 复购用户查询 
	 * 2017年8月1日
	 * @author gaobaolei
	 * @param query_date
	 * @param query_id
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryRebuycus(DynamicDto dynamicDto,PageInfo pageInfo);

	/**
	 *
	 * TODO 导出复购用户 
	 * 2017年8月10日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> exportRebuycus(DynamicDto dynamicDto);

	/**
	 *
	 * TODO 交易额（根据交易类型）查询 
	 * 2017年8月3日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryStoreTradeByType(DynamicDto dynamicDto,PageInfo pageInfo);

	/**
	 *
	 * TODO 交易额（根据交易类型）导出 
	 * 2017年8月8日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> exportStoreTradeByType(DynamicDto dynamicDto);

	/**
	 *
	 * TODO 交易额（根据频道部门）查询 
	 * 2017年8月3日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryStoreTradeByChannel(DynamicDto dynamicDto,PageInfo pageInfo);

	/**
	 *
	 * TODO  交易额（根据频道部门）导出
	 * 2017年8月8日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> exportStoreTradeByChannel(DynamicDto dynamicDto);

	/**
	 *
	 * TODO 查询门店送单（按照类型） 
	 * 2017年8月8日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> querySendOrderSumByType(DynamicDto dynamicDto,PageInfo pageInfo);

	/**
	 *
	 * TODO  导出门店送单（按照类型） 
	 * 2017年8月8日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> exportSendOrderSumByType(DynamicDto dynamicDto);

	/**
	 *
	 * TODO  查询门店送单（按照频道） 
	 * 2017年8月8日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> querySendOrderSumByChannel(DynamicDto dynamicDto,PageInfo pageInfo);

	/**
	 *
	 * TODO  导出门店送单（按照频道） 
	 * 2017年8月8日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> exportSendOrderSumByChannel(DynamicDto dynamicDto);

	/**
	 *
	 * TODO 查询 好评次数
	 * 2017年8月8日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryRewardTimes(DynamicDto dynamicDto,PageInfo pageInfo);

	/**
	 *
	 * TODO  导出好评次数
	 * 2017年8月8日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> exportRewardTimes(DynamicDto dynamicDto);

	/**
	 *
	 * TODO 查询商业信息 
	 * 2017年8月9日
	 * @author gaobaolei
	 * @param dd
	 * @param pageinfo
	 * @return
	 */
	public Map<String, Object> queryBusiness(DynamicDto dd,PageInfo pageinfo);

	/**
	 *
	 * TODO 导出 商业信息
	 * 2017年8月9日
	 * @author gaobaolei
	 * @param dd
	 * @return
	 */
	public Map<String, Object> exportBusiness(DynamicDto dd);


	/**
	 *
	 * TODO 查询写字楼 
	 * 2017年8月9日
	 * @author gaobaolei
	 * @param dd
	 * @param pageinfo
	 * @return
	 */
	public Map<String, Object> queryOffice(DynamicDto dd,PageInfo pageinfo);

	/**
	 *
	 * TODO 导出写字楼
	 * 2017年8月9日
	 * @author gaobaolei
	 * @param dd
	 * @return
	 */
	public Map<String, Object> exportOffice(DynamicDto dd);

	/**
	 *
	 * TODO  查询拜访记录
	 * 2017年8月9日
	 * @author gaobaolei
	 * @param dd
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryRelation(DynamicDto dd,PageInfo pageInfo);

	/**
	 *
	 * TODO 查询用户画像 
	 * 2017年8月9日
	 * @author gaobaolei
	 * @param dd
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryCustomer(DynamicDto dd,PageInfo pageInfo);


	/**
	 *
	 * TODO  查询gmv占比
	 * 2017年8月22日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryGMV(DynamicDto dynamicDto,PageInfo pageInfo);

	/**
	 *
	 * TODO 导出gmv占比 
	 * 2017年8月22日
	 * @author gaobaolei
	 * @param dd
	 * @return
	 */
	public Map<String, Object> exportGMV(DynamicDto dd);

	/**
	 *
	 * TODO 查询异常订单 
	 * 2017年9月19日
	 * @author gaobaolei
	 * @param abnormalOrderDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String,Object> queryAbnormalOrder(AbnormalOrderDto abnormalOrderDto,PageInfo pageInfo);

	/**
	 *
	 * TODO 导出异常订单 
	 * 2017年9月20日
	 * @author gaobaolei
	 * @param abnormalOrderDto
	 * @return
	 */
	public Map<String, Object> exportAbnormalOrder(AbnormalOrderDto abnormalOrderDto);
	/**
	 *
	 * TODO 查询异常订单类型
	 * 2017年9月19日
	 * @author gaobaolei
	 * @return
	 */
	public List<Map<String, Object>> queryAbnormalOrderType();

	/**
	 *
	 * TODO 导入异常订单 
	 * 2017年9月21日
	 * @author gaobaolei
	 * @param list
	 * @return
	 */
	public String importAbnormalOrder(List<File> list,String dataType) throws Exception;


	/**
	 * 门店用户画像表格明细
	 */
	public Map<String, Object> customerPortraitCount(DynamicDto dd,PageInfo pageInfo);


	/**
	 * 门店拜访记录表格明细
	 */
	public Map<String, Object> visitRecodeFormDetail(DynamicDto dd,PageInfo pageInfo);

	/**
	 * 导出拜访记录
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> exportVisitRecode(DynamicDto dynamicDto);


	/**
	 * 客户画像导出
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> exportCustomerPortrait(DynamicDto dynamicDto);


	/**
	 * 通过时间查询快递信息
	 * @param dynamicDto
	 * @return Map<String,Object>
	 */
	public List<Map<String , Object>> queryExpressByYearMonth(DynamicDto dynamicDto,PageInfo pageInfo);
	
	
	/**
	 * 
	 * TODO 查找员工所管理的 城市
	 * 2017年9月27日
	 * @author gaobaolei
	 * @param userid
	 * @param rank 权限
	 * @return
	 */
	public List<Map<String, Object>> getCityByUser(Integer target,Long userid,String name);
	
	/**
	 * 
	 * TODO 查询门店事业部数据 
	 * 2017年10月24日
	 * @author gaobaolei
	 * @param month
	 * @param storeNo
	 * @return
	 */
	public Map<String, Object> queryStoreTradeOfDept(DynamicDto dynamicDto);
	
	/**
	 * 
	 * TODO 导出门店事业部数据 
	 * 2017年10月25日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> exportStoreTradeOfDept(DynamicDto dynamicDto);


	/**
	 *
	 * TODO 片区GMV(新)
	 * 2017年10月19日
	 * @title : areaGMV
	 * @author gaobaolei
	 * @param dynamicDto , pageInfo
	 * @return Map<String,Object>
	 */
	public Map<String, Object> employeeOfAreaGmv(DynamicDto dynamicDto,PageInfo pageInfo);


	/**
	 * TODO 片区GMV 导出
	 * @param dynamicDto
	 * @return Map<String, Object>
	 */
	public Map<String, Object> exportAreaGMV(DynamicDto dynamicDto);



	/**
	 *
	 * TODO 拉新GMV(国安侠)
	 * 2017年10月19日
	 * @title : newAddCustomerGMV
	 * @author 15149006102@139.com
	 * @param  dynamicDto , pageInfo
	 * @return Map<String,Object>
	 */
	public Map<String,Object> newAddCustomerGMV(DynamicDto dynamicDto,PageInfo pageInfo);


	/**
	 * TODO 拉新GMV 导出
	 * @param dynamicDto
	 * @return Map<String, Object>
	 */
	public Map<String, Object> exportNewAddCustomerGMV(DynamicDto dynamicDto);




	/**
	 * TODO 重点产品GMV
	 * 2017年10月19日
	 * @title : emphasesProductsGMV
	 * @author 15149006102@139.com
	 * @param dynamicDto , pageInfo
	 * @return Map<String,Object>
	 */
	public Map<String,Object> emphasesProductsGMV(DynamicDto dynamicDto,PageInfo pageInfo);


	/**
	 * TODO 重点产品GMV 导出
	 * @param dynamicDto
	 * @return Map<String, Object>
	 */
	public Map<String, Object> exportEmphasesProductsGMV(DynamicDto dynamicDto);

	/**
	 * 
	 * TODO 导出快递代送 
	 * 2017年10月26日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String,Object> exportExpress(DynamicDto dynamicDto);
	
	/**
	 * TODO 国安侠片区交易额(片区GMV)
	 * @author gaolei
	 * @param employee_id 国安侠号
	 * @param year 查询年
	 * @param month 查询月
	 * @return
	 */
	public String getAreaTradeAmount(String employee_no,String year,String month);
	/**
	 * TODO 国安侠片区交易额(片区GMV)包括所包含的详细数据
	 * @author gaolei
	 * @param employee_id 国安侠号
	 * @param year 查询年
	 * @param month 查询月
	 * @return
	 */
	public String getNewAreaTradeAmount(String employee_no,String year,String month);
	
	
	/**
	 * 
	 * TODO 片区重点产品GMV
	 * @author gaolei
	 * @param employee_no
	 * @param year
	 * @param month
	 * @return
	 */
//	public String getAreaZdGmvAmount(String employee_no,String year,String month);
	
	/**
	 * 
	 * TODO 片区拉新
	 * @author gaolei
	 * @param employee_no
	 * @param year
	 * @param month
	 * @return
	 */
	public String getAreaNewaddCusAmount(String employee_no,String year,String month);
	
	/**
	 * 
	 * TODO 国安侠片区动态数据(片区拉新、片区GMV、片区重点产品 )
	 * @author gaolei
	 * @param employee_no
	 * @param year
	 * @param month
	 * @return
	 */
	public Map<String, Object> getAreaData(String employee_no,String year,String month);
	/**
	 * 
	 * TODO 国安侠片区动态数据(片区拉新、片区GMV)
	 * @author gaolei
	 * @param employee_no
	 * @param year
	 * @param month
	 * @return
	 */
	public Map<String, Object> getNewAreaData(String employee_no,String year,String month);
	
	/**
	 * 
	 * TODO  查询已开店的所有城市 
	 * 2017年12月13日
	 * @author gaobaolei
	 * @return
	 */
	public List<Map<String,Object>> selectAllCity();
	/**
	 * TODO 查询上月GMV城市(省里城市)排名
	 * @author zhangli
	 * @return
	 */
	public Map<String, Object> getLastMonthCityRankingTop10(DynamicDto dd,PageInfo pageInfo,String sign);
	/**
	 * TODO 查询上月GMV(省内)门店排名-带分页
	 * @author zhangli
	 * @return
	 */
	public Map<String, Object> getLastMonthStoreRankingTop10(DynamicDto dd,PageInfo pageInfo,String sign);
	
	/**
	 * TODO 查询当日门店总成交额
	 * @author zhangli
	 * @return
	 */
	public Map<String, Object> getDailyStoreTotlePrice(DynamicDto dd);
	/**
	 * TODO 查询城市数,门店数,门店人数
	 * @author zhangli
	 * @return
	 */
	public Map<String, Object> getAllCountOfCityStoreGax(DynamicDto dd,PageInfo pageInfo);
	/**
	 * TODO 查询门店在全国的排名
	 * @author zhangli
	 * @return
	 */
	public Map<String, Object> getLastMonthStoreChinaRankingTop10(DynamicDto dd);

	
    /**
     * 总部查看数据：按事业部交易额排名-带分页
     */
    public Map<String, Object>  queryTradeByDepName(DynamicDto dd,PageInfo pageInfo,String str);
    /**
     * 总部查看数据：按频道交易额排名-带分页
     */
    public Map<String, Object>  queryTradeByChannelName(DynamicDto dd,PageInfo pageInfo,String str);
    /**
     * 总部查看数据：按频道订单量排名(带分页)
     */
    public Map<String, Object>  queryOrderCountByChannelName(DynamicDto dd,PageInfo pageInfo,String str);
    
	/**
	 * 
	 * TODO 查询门店各个片区的GMV 
	 * 2017年12月19日
	 * @author gaobaolei
	 * @param storeNo
	 * @return
	 */
	public Map<String, Object> selectAreaRankingOfStore(Long storeId,PageInfo pageInfo); 
	
	/**
	 * 
	 * TODO 查询门店各个事业部的GMV 
	 * 2017年12月19日
	 * @author gaobaolei
	 * @param storeNo
	 * @return
	 */
	public Map<String, Object> selectDeptRankingOfStore(Long storeId);
	
	/**
	 * 
	 * TODO 查询门店各个渠道的GMV 
	 * 2017年12月19日
	 * @author gaobaolei
	 * @param storeNo
	 * @return
	 */
	public Map<String, Object> selectChannelRankingOfStore(Long storeId,PageInfo pageInfo);
    
    /**
     * TODO 查询全国门店订单数排名-带分页
	 * @author zhangli
	 * @return
     */
    public Map<String, Object>  queryCityOrderRankingTop10(DynamicDto dd,PageInfo pageInfo);

    /**
     * 
     * TODO 查询国安侠gmv
     * 2017年12月20日
     * @author gaobaolei
     * @param storeId
     * @return
     */
    public  Map<String,Object> selectEmployeeRankingOfStore(Long storeId,PageInfo pageInfo);
    
    /**
     * 
     * TODO 查询E店gmv 
     * 2017年12月20日
     * @author gaobaolei
     * @param storeId
     * @return
     */
    public  Map<String,Object> selectEStoreRankingOfStore(Long storeId,PageInfo pageInfo);
    
    
    /**
	 * 
	 * TODO 查询门店事业部服务专员gmv 
	 * 2017年12月21日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> selectDeptServerRanking(Long storeId);
	
	/**
	 * 
	 * TODO 查询片区订单量 
	 * 2017年12月21日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> selectAreaOrderRanking(Long storeId,PageInfo pageInfo);
	
	/**
	 * 查询频道订单量
	 * TODO  
	 * 2017年12月21日
	 * @author gaobaolei
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> selectChannelOrderRanking(Long storeId,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 查询门店的交易额 
	 * 2017年12月21日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public Map<String, Object> selectGMVOfStore(Long storeId);

    /**
     * TODO 查询全国当月总交易额
	 * @author caops
	 * @return
     */
    public Map<String, Object>  queryTradeSumByMonth(DynamicDto dd);
    
    /**
     * 总部查看数据：按人员Gmv排名-带分页
     */
    public Map<String, Object>  queryAreaTradeByEmp(DynamicDto dd,PageInfo pageInfo,String sign);
    /**
     * 总部查看数据：按服务专员Gmv排名
     */
    public Map<String, Object>  queryServerTradeByEmp(DynamicDto dd);
    /**
     * TODO 查询门店历史总交易额
	 * @author caops
	 * @return
     */
    public Map<String, Object>  queryTradeSumOfHistory(DynamicDto dd);
    
    /**
     * 
     * TODO 查询门店的历史交易额 
     * 2017年12月22日
     * @author gaobaolei
     * @param storeId
     * @return
     */
    public Map<String, Object> selectHistoryGmvOfStore(Long storeId);
    
    
    /**
     * 
     * TODO 查询商品总量排名
     * 2017年12月25日
     * @author cps
     * @return
     */
    public Map<String, Object> queryTradeByProduct(DynamicDto dd);
    /**
     * 
     * TODO 查询商品总量排名-带分页
     * 2018年1月10日
     * @author cps
     * @return
     */
    public Map<String, Object> queryProductCityOrder(DynamicDto dd,PageInfo pageInfo,String sign);
    
    /**
     * 
     * @param 导出(总部)数据
     * @return
     */
  	public Map<String, Object> exportData(DynamicDto dd,String str);
  	
    public JSONObject insertNewTest(String storeCode,String storeName);
    public JSONObject insertNewStore(String storeCode,String storeName,String provinceCode,String cityCode,String adCode,String address);
    public JSONObject insertNewEmployee(String storeCode,String employeeCode,String employeeName,String telephone);

    
    /**
     * 
     * TODO 导出更多信息 
     * 2018年1月16日
     * @author gaobaolei
     * @param storeId
     * @param flag 数据类别标识 areaRanking_gmv、deptRanking_gmv、...
     * @return
     */
    public  Map<String,Object> exportRanking(Long storeId,String flag);
    
    /**
     * 
     * TODO 获取门店每月消费客户排名 
     * 2018年1月29日
     * @author gaobaolei
     * @param dd
     * @param pageInfo
     * @return
     */
    public  Map<String, Object> getStoreCustomerRanking(Long storeId,PageInfo pageInfo);
    
    /**
     * 
     * TODO 国安侠拉新排序 
     * 2018年1月29日
     * @author gaobaolei
     * @param storeId
     * @param pageInfo
     * @return
     */
    public  Map<String, Object> getGaxOfAreaNewaddcus(Long storeId,PageInfo pageInfo);
    /**
     * 根据gemin里面的store_id查询GASM中的store_id
     * @param storeId
     * @return
     */
    public Map<String , Object> queryPlatformidByStoreId(String storeId);
    
    /**
     * 
     * TODO  查询片区某几天相关绩效数据（订单，营业额，用户）
     * 2018年3月13日
     * @author gaobaolei
     * @param areaNo
     * @return
     */
    public Map<String, Object> querySeveralDaysPerformanceOfArea(DynamicDto dynamicDto);
    
    /**
     * 
     * TODO 查询片区当月相关绩效数据（订单，营业额，用户）
     * 2018年3月14日
     * @author gaobaolei
     * @param dynamicDto
     * @return
     */
    public Map<String, Object> queryCurMonthPerformanceOfArea(DynamicDto dynamicDto);
	/**
     * 查询某月累计用户量
     * @param dd
     * @return
     */
    public Map<String, Object> getCustomerOfCurMonth(DynamicDto dd);
    /**
     * 查询某年的累计营业额
     * @param dd
     * @return
     */
    public Map<String, Object> getSumOfCurYear(DynamicDto dd);
    /**
     * 查询本月拉新和消费用户数
     * @param dd
     * @return
     */
    public Map<String, Object> getNewMonthUserCount(DynamicDto dd);
    
    /**
     * 查询本周每日拉新用户数,消费用户数,复购率
     * @param dd
     * @return
     */
    public Map<String, Object> getWeekCustomerOrderRate(DynamicDto dd);
    /**
     * 查询最新的订单所在的城市
     * @param dd
     * @return
     */
    public Map<String, Object> getDailyFirstOrderCity();
    
    
    public String validateUser(String username,String pwd);
}