package com.cnpc.pms.inter.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.SiteSelection;
import com.cnpc.pms.personal.entity.StoreAddress;
import com.cnpc.pms.personal.entity.UserLoginLog;
import com.cnpc.pms.personal.entity.WorkRecordTotal;
import com.cnpc.pms.personal.entity.WxUserAuth;
import com.cnpc.pms.slice.entity.Area;

import java.util.List;
import java.util.Map;

/**
 * App接口
 * Created by liu on 2016/8/23 0023.
 */
public interface InterManager extends IManager {

    /**
     * 获取当前版本
     * @return 当前版本
     */
	String getCurrentVersion(String app_version);
	
	public Result getNewCurrentVersion(String app_version);
	
	public Result getNowCurrentVersion(String app_version);

    Map<String,Object> getExpressAndCustomerCount(String employee_no);

    Map<String,Object> getExpressAndCustomerStoreCount(Long store_id);
    
    /**
     * 
     * TODO 国安侠数据卡（用户画像、快递代送、订单等）
     * 2017年3月8日
     * @author liuxiao
     * @update gaobaolei
     * @param express
     * @return
     */
    Map<String,Object> getExpressAndCustomerCountNew(Express express);
    
    /**
     * 
     * TODO 门店数据卡（用户画像、快递代送、订单等）
     * 2017年3月8日
     * @author liuxiao
     * @update gaobaolei
     * @param express
     * @return
     */
    Map<String,Object> getExpressAndCustomerStoreCountNew(Express express);

    Result getCustomerListForMonth(Customer customer);

    Result getExpressListForMonth(PageInfo pageInfo,Express express);

    Map<String,Object> queryDataCardList(QueryConditions conditions);
  
    Map<String,Object> queryRelationDataCardList(QueryConditions conditions);
    /**
     * 
     * TODO 用户画像表格数据
     * 2017年3月8日
     * @author gaobaolei
     * @param conditions
     * @return
     */
    Map<String,Object> queryCustomerDataCardList(QueryConditions conditions);
  
    Map<String,Object> queryExpressDataCardList(QueryConditions conditions);

    Map<String,Object> queryOrderDataCardList(QueryConditions conditions);
    
    List<Map<String,Object>> queryRelationListDataByStore(Express express);
    
    /**
     * 
     * TODO 用户画像圆形报表 （门店数据卡）
     * 2017年3月22日
     * @author gaobaolei
     * @param express
     * @return
     */
    List<Map<String,Object>> queryCustomerListDataByStore(Express express);
    
    List<Map<String,Object>> queryExpressListDataByStore(Express express);

    List<Map<String,Object>> queryOrderListDataByStore(Express express);
    
    List<Map<String,Object>> queryRelationListDataByEmployee(Express express);
    
    /**
     * 
     * TODO 用户画像圆形报表（国安侠） 
     * 2017年3月22日
     * @author gaobaolei
     * @param express
     * @return
     */
    List<Map<String,Object>> queryCustomerListDataByEmployee(Express express);
    
    List<Map<String,Object>> queryExpressListDataByEmployee(Express express);

    WorkRecordTotal findWorkRecordTotalById(Long id);
    
    //获取主页面的数据通过门店id
    List<Map<String,Object>> getIndexByStoreCountNew(Express express);
  //获取主页面的数据通过员工id
    List<Map<String,Object>> getIndexByEmployeeCountNew(Express express);
    //获取门店店长信息通过员工号
    Result getStoreOwner(String employee_no);
    //根据用户id获取图表数据
    Map<String,Object> queryCountDataByEmployee(Express express);
    /**
     * 根据门店id获取所有的单体画像
     * @param customer
     * @return
     */
    List<Map<String,Object>> getCustomerListForMonthData(Customer customer);
    
    /**
     * APP 分片查询订单信息
     * @param employee_no
	 * @param pageInfo
	 * @return
	 */
    public Result queryOrderListAppByArea(Long store_id,String employee_no,PageInfo pageInfo,Long area_id);
    
    public Result queryExpressAPPByEmployeeNo(String employee_no,PageInfo pageInfo);
    
    /**
	 *APP 验证是否是 A国安侠
	* @param employee_no
	 * @return
	 */
    public Area queryAreaAppByCurrEmployeeNo(String employee_no);
	
    /**
     * 
     * TODO  app端查询国安侠负责片区的拜访记录
     * 2017年4月24日
     * @author gaobaolei
     * @param employeeNo
     * @param pageSize
     * @param currentPage
     * @return
     */
    public Result findRelation_area_employee_app(String employeeNo,PageInfo pageInfo,Long area_id);
    
    
    /**
     * 根据订单sn编号 查询明细信息 
     * @param order_sn
     * @return
     */
    public Result queryOrderInfoBySN(String order_sn);
    
    
    public Result findRelation_customer_app(Long customer_id,PageInfo pageInfo);
    
    
    /**
     * APP手机国安侠分片 查询五个月的订单
     * @param employeeNo
     * @return
     */
    public Result queryOrderFiveMonth(Long store_id,String employee_no,Long area_id);
    
    /**
     * TODO 查询国安侠五个月的拜访记录
     * 2017年5月15日
     * @author gaobaolei
     * @param employeeNo
     * @return
     */
    public Result getRelationOfEmployee_month_app(String employeeNo,Long area_id);
    
    /**
     * 
     * TODO 查询国安侠五个月的用户画像 
     * 2017年5月15日
     * @author gaobaolei
     * @param employeeNo
     * @return
     */
    public Result getCustomerOfEmployee_month_app(String employeeNo,Long area_id);

    
    /**
     * 
     * TODO  app个人动态拜访记录
     * 2017年5月19日
     * @author gaobaolei
     * @param page
     * @param employee
     * @return
     */
    public Result getRelationOfEmployee_app(PageInfo page,String employee);
    
    /**
     * 
     * TODO app个人动态拜访记录柱状图 
     * 2017年5月19日
     * @author gaobaolei
     * @param employeeNo
     * @return
     */
    public Result findRelationOfEmployee_byFiveMonth(String employeeNo);
    
    /**
     * 
     * TODO app个人动态 
     * 2017年6月2日
     * @author gaobaolei
     * @param employeeNo
     * @return
     */
    public Result findEmployee_areaInfo(String employeeNo,Long area_id);

    
    
    /**
     * APP个人动态 的送单记录 根据员工号 查询订单。
     * @param employee_no
     * @param pageInfo
     * @return
     */
    public Result queryOrderListByEmployeeNo(String employee_no, PageInfo pageInfo,Long area_id);
    
    
    /**
     * APP端，个人中心 根据员工号 查询 五个月的订单图表 
     * @param employee_no
     * @return
     */
    public Result queryOrderFiveMonthOrderApp(String employee_no);
    
    /**
     * 
     * TODO 查询某员工所有片区 
     * 2017年6月2日
     * @author gaobaolei
     * @param employeeNo
     * @return
     */
    public Result getAllAreaOfEmployee(String employeeNo);
    
    /**
     * 
     * TODO app-crm店长(拜访记录和用户画像柱状图) 
     * 2017年6月6日
     * @author gaobaolei
     * @param storeId
     * @param query_date
     * @return
     */
    public Result getRelationAndCustomerOfCrm_app(Long storeId,String query_date);
    
    /**
     * 
     * TODO app-crm店长(拜访记录饼状图) 
     * 2017年6月6日
     * @author gaobaolei
     * @param storeId
     * @param query_date
     * @return
     */
    public Result getRelationOfCrm_app(Long storeId,String query_date);
    
    /**
     * 
     * TODO app-crm店长(用户画像饼状图) 
     * 2017年6月6日
     * @author gaobaolei
     * @param storeId
     * @param query_date
     * @return
     */
    public Result getCustomerOfCrm_app(Long storeId,String query_date);
    
    /**
     * 
     * TODOapp-crm店长（现状图） 
     * 2017年6月6日
     * @author gaobaolei
     * @param storeId
     * @return
     */
    public Result queryRelationAndCustomerOfYear_crm_app(Long storeId);
    
    
    /**
     * 
     * TODOapp-crm店长（现状图） 订单数量及金额
     * 2017年6月6日
     * @author zhaoxg
     * @param storeId
     * @return
     */
    public Result queryOrderInfoOfYear_crm_app(Long storeId);
    
    /**
	 * app_CRM店长 查询 分片区订单统计数量 以及 统计金额 图表用ss
	 * @param store_id
	 * @return
	 */
	public Result queryOrderTotalByArea_crm_app(Long store_id);
	
	
	/**
	 * app_crm店长 根据门店 查询 送单量 图表用
	 * @param store_id
	 * @return
	 */
	public Result queryOrderCountByStoreId_crm_app(Long store_id);
	
	/**
	 * app_crm店长 根据门店查询 所有国安侠 快递代送量 图表用 
	 * @param store_id
	 * @return
	 */
	public Result queryExpressListByStoreId_crm_app(Long store_id);
   
    /**
     * 
     * TODO  crm-app店长门店员工信息
     * 2017年6月6日
     * @author gaobaolei
     * @param storeId
     * @param query_date
     * @return
     */
    public Result getRelationAndCustomerOfStore(Long storeId, String query_date);
    
    /**
     * 
     * TODO app-个人片区动态（片区信息） 
     * 2017年7月10日
     * @author gaobaolei
     * @param employeeNo
     * @param area_id
     * @return
     */
    public Result findAreaInfoOfEmployee_app(String employeeNo,Long area_id);
    
    /**
     * 
     * TODO app-片区动态（业绩总数） 
     * 2017年7月10日
     * @author gaobaolei
     * @param employeeNo
     * @param area_id
     * @return
     */
    public Result findEmployeePerformance_app(String employeeNo,Long area_id);
    
    /**
     * 
     * TODO app-查询国安侠混片送单 
     * 2017年11月7日
     * @author gaobaolei
     * @param employee_no
     * @param pageInfo
     * @param month
     * @return
     */
    public Result queryOrderOfAreaByApp(String employee_no,PageInfo pageInfo,String order_sn);
 
    /**TODO app-查询小区信息(楼房数,户数,小区名称,小区编码,小区面积,片区名称,片区编号,门店名称,所在城市,
     * 满足6字段的用户画像数,上月订单数,上月用户数,片区国安侠a,片区国安侠b)
     * 2017年12月11日
     * @author zhangli
     * @param area_id
     */
    public Result queryVillageInfoByAreaNoApp(String area_id);
	/**
	 * 
	 * TODO 国安侠所管理的小区 
	 * 2017年12月06日
	 * @author cps
	 * @param storeId
	 * @return
	 */
	public Map<String,Object> selecTinyVillageCoordByEmployeeNo(String employeeNo,String storeNO);
	/**
	 * 
	 * TODO 通过小区id获得社区和街道
	 * 2017年12月13日
	 * @author zhangli
	 * @param villageId
	 * @return
	 */
	public Result findTinyTownByVillageId(String villageId);
	/**
	 * 
	 * TODO 通过员工编号获取员工头像
	 * 2017年12月15日
	 * @author gaolei
	 * @param employee_id
	 * @return
	 */
	public Result findEmployeePicByEmployeeId(String employee_id);
	
	/**
     * 
     * TODO app店长门店员工信息
     * 2017年12月18日
     * @author gaolei
     * @param storeId
     * @param query_date
     * @return
     */
    public Result getEmployeeOfStore(Long storeId, String query_date);
    /**
     * APP 分片查询订单信息
     * @param employee_no
	 * @param pageInfo
	 * @return
	 */
    public Result queryOrderListAppByAreaNew(Long store_id,String employee_no,PageInfo pageInfo,Long area_id);
    
    /**
     * APP通过城市查询拉新|消费信息
     * @param cityname
     * @return
     */
    public Result queryCustomerStatBycity(String cityname);
    
    public String showBarCodeImage();
    public Result doVerification(String uuid,Long userid,String md5Pwd,String token);
    
    public String saveDownloadLog(String type,String dip,String cityname);
    
    
    public UserLoginLog saveAppUserAccessLog(User userInfo);
    
    
    //接口 用于小程序添加用户
    public Result saveSiteSelection(SiteSelection siteSelection);
    //接口 用于小程序添加门店地址 
  	public Result saveStoreAddress(StoreAddress storeAddress);
    //接口 用于店铺需求列表 
  	public Result queryStoreStandardList();
    //接口 点击查 询店铺需求明细
  	public Result queryStoreStandardById(Long id);
  	//接口 用户登录 
  	public Result login(SiteSelection siteSelection);
  	//接口 店铺需求列表
  	public Result queryStoreRequirementList();
    //接口 根据ID查询店铺需求明细 
  	public Result queryStoreRequiremenById(Long id);
  	//接口 房源信息列表 根据ID
  	public Result queryStoreAddressList(Long siteSelectionId);
  	//接口 查询房源信息明细 根据ID
  	public Result queryStoreAddressById(Long id);
  	//接口 发送短信的接口
  	public Result sendMessage(String mobilephone);
  	//接口 根据ID 取得用户信息
  	public Result querySiteSelectionById(Long id);
    //接口 保存补充信息
  	public Result updateStoreAddress(StoreAddress storeAddress);
    //接口 微信小程序banner图片
  	public Result queryBannerInfoList();
  	
  	//国安数据接口 微信发送验证码
  	public Result wxSendMessage(String mobilephone);
  	//国安数据接口 微信用户登录
  	public Result wxLogin(SiteSelection siteSelection);
  	//国安数据接口 验证电话是否在国安数据中存在
    public Result wxValiExist(String mobilephone);
    //国安数据接口 微信验证电话openid是否是已认证过的
    public Result wxAuthUser(WxUserAuth wxUserAuth);
    //调用微信 接口
    public String requestWxApi(String code);
    //通用发送短信的方法  ，参数 手机号 发送内容 发送的功能模块名称
	public String commonSendMessage(String mobilephone,String content,String functionname);
  	
    /**
     * 查当月内每日营业额
     * @param cityId
     * @return
     */
    public Result queryDailyTurnover(String cityId);
    
    /**
     * 查询当月累计实时营业额
     * @param dynamicDao
     * @return
     */
    public Result queryActualMothlyTurnover(DynamicDto dynamicDto);
    
    /**
     * 查询当月事业群GMV
     * @param dynamicDto
     * @return
     */
    public Result queryTradeDepMonthlyGMV(DynamicDto dynamicDto);
    
    /**
     * 查询当月频道GMV
     * @param dynamicDto
     * @return
     */
    public Result queryTradeChannelGMV(DynamicDto dynamicDto);
    
    /**
     * 查询本周每日用户量
     * @param cityId
     * @return
     */
    public Result queryCustomerCountForWeek(String cityId);
    /**
     * 查询当月实时用户量
     * @param cityId
     * @return
     */
    public Result queryMonthCustomerCount(String cityId);
    /**
	 * 
	 * TODO app城市管理动态webview页面数据
	 * 2018年3月12日
	 * @author gaolei
	 * @param 
	 * @return
	 */
	public Map<String,Object> getCityWebViewData(DynamicDto dynamicDto);
    
	
	//系统校验用户是否存在的方法。外部系统登录用。
	public User commonValidUser(String userCode,String password);
	
	public Result queryStoreOrderListForApp(PageInfo pageInfo,String employee_no);
	
}
