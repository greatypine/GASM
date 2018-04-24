/**
 * gaobaolei
 */
package com.cnpc.pms.dynamic.manager.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.dynamic.common.EncryptUtils;
import com.cnpc.pms.dynamic.common.HttpClientUtil;
import com.cnpc.pms.dynamic.dao.DynamicDao;
import com.cnpc.pms.dynamic.entity.AbnormalOrderDto;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.dynamic.manager.DynamicManager;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.dao.ExpressDao;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.entity.DsAbnormalOrder;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.SyncDataLog;
import com.cnpc.pms.personal.manager.DsAbnormalOrderManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.SyncDataLogManager;
import com.cnpc.pms.platform.dao.OrderDao;
import com.cnpc.pms.slice.dao.AreaDao;
import com.cnpc.pms.utils.Base64Encoder;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.MD5Utils;

/**
 * @author gaobaolei
 *
 */
public class DynamicManagerImpl extends BizBaseCommonManager implements DynamicManager{
	//门店维度
	private static final String SECRET="0syT93kIiG#WG3bk7Friw^t2jLeW*c$0";
	
	static String platform_key = PropertiesUtil.getValue("platform.key");
	static String platform_store = PropertiesUtil.getValue("platform.store");
	static String platform_employee = PropertiesUtil.getValue("platform.employee");
	//测试密钥
	//private static final String KEY="c09bad16-cd92-49ea-8898-82ad2eeead70";
	//线上密钥
	private static final String KEY=platform_key;
	//测试连通地址
	//private static final String THIRD_PART_TEST_URL = "https://gasq-web-thirdparty.guoanshequ.wang/gasq-web-thirdparty/protest/inserttest";
	
	//线上同步地址
	private static final String THIRD_PART_STORE_URL = platform_store;
	private static final String THIRD_PART_EMP_URL = platform_employee;
	
	
	private static final String VALIDATION_USER="http://10.16.31.242/daqWeb/dispatcher.action";
	//测试同步地址
	//private static final String THIRD_PART_STORE_URL = "https://gasq-web-thirdparty.guoanshequ.wang/gasq-web-thirdparty/storeSynchro/store";
	//private static final String THIRD_PART_EMP_URL = "https://gasq-web-thirdparty.guoanshequ.wang/gasq-web-thirdparty/storeSynchro/employee";
	
//	private static final String DAILYDATA_GMV_URL = "http://10.16.31.245:8090/ds/rest/queryStoreTradesOrderByGMV";
//	private static final String DAILYDATA_ORDER_URL = "http://10.16.31.245:8090/ds/rest/queryStoreTradesOrderByOrderNum";
//	private static final String DAILYDATA_RELATION_URL = "http://10.16.31.245:8090/ds/rest/queryRelationsStoreByDate";
//	private static final String NEWADDCUS_URL = "http://10.16.31.245:8090/ds/rest/queryNewaddCus";
//	private static final String NEWADDCUS_URL_NOT_CUR = "http://10.16.31.245:8090/ds/rest/queryTNewaddCus";
//	private static final String REBUYCUS_URL = "http://10.16.31.245:8090/ds/rest/queryRebuyCus";
//	private static final String REBUYCUS_URL_NOT_CUR = "http://10.16.31.245:8090/ds/rest/queryTRebuyCus";
//	private static final String STORETRADE_TYPE_URL = "http://10.16.31.245:8090/ds/rest/queryStoreTrades";
//	private static final String STORETRADE_TYPE_URL_NOT_CUR = "http://10.16.31.245:8090/ds/rest/queryTStoreTrades";
//	private static final String STORETRADE_CHANNEL_URL = "http://10.16.31.245:8090/ds/rest/queryStoreTradeChannels";
//	private static final String STORETRADE_CHANNEL_URL_NOT_CUR = "http://10.16.31.245:8090/ds/rest/queryTStoreTradeChannel";
//	private static final String SENDORDER_TYPE_URL = "http://10.16.31.245:8090/ds/rest/querySendOrderSum";
//	private static final String SENDORDER_TYPE_URL_NOT_CUR = "http://10.16.31.245:8090/ds/rest/queryTSendOrderSum";
//	private static final String SENDORDER_CHANNEL_URL = "http://10.16.31.245:8090/ds/rest/querySendOrders";
//	private static final String SENDORDER_CHANNEL_URL_NOT_CUR = "http://10.16.31.245:8090/ds/rest/queryTSendOrders";
//	private static final String REWARDTIMES_URL = "http://10.16.31.245:8090/ds/rest/queryRewardTimes";
//	private static final String REWARDTIMES_URL_NOT_CUR = "http://10.16.31.245:8090/ds/rest/queryTRewardTimes";
//	private static final String RELATION_LIST_URL = "http://10.16.31.245:8090/ds/rest/queryRelationsStoreByDate";
//	private static final String REBUYCUS_COUNT_URL = "http://10.16.31.245:8090/ds/rest/queryRebuyCusSumByDate";
//	private static final String STORETRADE_COUNT_URL = "http://10.16.31.245:8090/ds/rest/queryStoreTradesSumByDate";
//	private static final String SENDORDER_COUNT_URL = "http://10.16.31.245:8090/ds/rest/querySendOrdersSumByDate";
//	private static final String RELATION_COUNT_URL = "http://10.16.31.245:8090/ds/rest/queryRelationsStoreSumByDate";
//	private static final String NEWADDCUS_COUNT_URL = "http://10.16.31.245:8090/ds/rest/queryNewaddCusSumByDate";
//	private static final String REWARDTIMES_COUNT_URL = "http://10.16.31.245:8090/ds/rest/queryRewardTimesSumByDate";
//	private static final String CUSTOMER_COUNT_URL = "http://10.16.31.245:8090/ds/rest/queryCustomerSecondStoreSumByDate";
//	private static final String GMV_URL = "http://10.16.31.245:8090/ds/rest/queryGmvPercentByDate";
//	private static final String GMV_URL_NOT_CUR = "http://10.16.31.245:8090/ds/rest/queryTGmvPercent";
//	private static final String CUSTOMER_COUNT_URL_GAX = "http://10.16.31.245:8090/ds/rest/queryCustomerSecondEmployeeSumByDate";
//	private static final String SENDORDER_COUNT_URL_GAX = "http://10.16.31.245:8090/ds/rest/querySendOrderEmployeeSumByDate";
//	private static final String RELATION_COUNT_URL_GAX = "http://10.16.31.245:8090/ds/rest/queryRelationsEmployeeByDate";
	// 片区内拉新GMV (片区某门店GMV = 落入片区拉新总GMV + 公海GMV)
   // private static final String NEWADDCUSTOMERGMV_FORMDETAIL = "http://10.16.31.245:8090/ds/rest/queryTAreaNewaddCus";
 // 片区GMV (片区某门店GMV = 落入片区拉新总GMV + 公海GMV)
   // private static final String AREAGMV_FORMDETAIL = "http://10.16.31.245:8090/ds/rest/queryTAreaTrade";
 // 片区重点产品GMV (片区某门店GMV = 落入片区拉新总GMV + 公海GMV)
    //private static final String EMPHASSESPRODUCTSGMV_FORMDETAIL = "http://10.16.31.245:8090/ds/rest/queryTAreaZdGmv";
    // 国安侠片区GMV总数 
//	private static final String AREATRADEAMOUNT="http://10.16.31.245:8090/ds/rest/queryTAreaTradeByMonth";
	// 国安侠片重点产品GMV总数 
//	private static final String AREAZDGMVAMOUNT="http://10.16.31.245:8090/ds/rest/queryTAreaZdGmvByMonth";
	// 国安侠片片区拉新
//	private static final String AREANEWADDCUSAMOUNT="http://10.16.31.245:8090/ds/rest/queryTAreaNewaddCusByMonth";
	// 最新国安侠GMV总数包括详细数量
//  private static final String NEWAREATRADEAMOUNT="http://10.16.31.245:8090/ds/rest/queryEmployeePesgmvByEmp";

	private static final String CUSTOMER_COUNT_URL_GAX = "https://ds.guoanshequ.cn/ds/rest/queryCustomerSecondEmployeeSumByDate";
	private static final String SENDORDER_COUNT_URL_GAX = "https://ds.guoanshequ.cn/ds/rest/querySendOrderEmployeeSumByDate";
	private static final String RELATION_COUNT_URL_GAX = "https://ds.guoanshequ.cn/ds/rest/queryRelationsEmployeeByDate";
	private static final String DAILYDATA_GMV_URL = "https://ds.guoanshequ.cn/ds/rest/queryStoreTradesOrderByGMV";
	private static final String DAILYDATA_ORDER_URL = "https://ds.guoanshequ.cn/ds/rest/queryStoreTradesOrderByOrderNum";
	private static final String DAILYDATA_RELATION_URL = "https://ds.guoanshequ.cn/ds/rest/queryRelationsStoreByDate";
	private static final String NEWADDCUS_URL = "https://ds.guoanshequ.cn/ds/rest/queryNewaddCus";
	private static final String NEWADDCUS_URL_NOT_CUR = "https://ds.guoanshequ.cn/ds/rest/queryTNewaddCus";
	private static final String REBUYCUS_URL = "https://ds.guoanshequ.cn/ds/rest/queryRebuyCus";
	private static final String REBUYCUS_URL_NOT_CUR = "https://ds.guoanshequ.cn/ds/rest/queryTRebuyCus";
	private static final String STORETRADE_TYPE_URL = "https://ds.guoanshequ.cn/ds/rest/queryStoreTrades";
	private static final String STORETRADE_TYPE_URL_NOT_CUR = "https://ds.guoanshequ.cn/ds/rest/queryTStoreTrades";
	private static final String STORETRADE_CHANNEL_URL = "https://ds.guoanshequ.cn/ds/rest/queryStoreTradeChannels";
	private static final String STORETRADE_CHANNEL_URL_NOT_CUR = "https://ds.guoanshequ.cn/ds/rest/queryTStoreTradeChannel";
	private static final String SENDORDER_TYPE_URL = "https://ds.guoanshequ.cn/ds/rest/querySendOrderSum";
	private static final String SENDORDER_TYPE_URL_NOT_CUR = "https://ds.guoanshequ.cn/ds/rest/queryTSendOrderSum";
	private static final String SENDORDER_CHANNEL_URL = "https://ds.guoanshequ.cn/ds/rest/querySendOrders";
	private static final String SENDORDER_CHANNEL_URL_NOT_CUR = "https://ds.guoanshequ.cn/ds/rest/queryTSendOrders";
	private static final String REWARDTIMES_URL = "https://ds.guoanshequ.cn/ds/rest/queryRewardTimes";
	private static final String REWARDTIMES_URL_NOT_CUR = "https://ds.guoanshequ.cn/ds/rest/queryTRewardTimes";
	private static final String RELATION_LIST_URL = "https://ds.guoanshequ.cn/ds/rest/queryRelationsStoreByDate";
	private static final String REBUYCUS_COUNT_URL = "https://ds.guoanshequ.cn/ds/rest/queryRebuyCusSumByDate";
	private static final String STORETRADE_COUNT_URL = "https://ds.guoanshequ.cn/ds/rest/queryStoreTradesSumByDate";
	private static final String SENDORDER_COUNT_URL = "https://ds.guoanshequ.cn/ds/rest/querySendOrdersSumByDate";
	private static final String RELATION_COUNT_URL = "https://ds.guoanshequ.cn/ds/rest/queryRelationsStoreSumByDate";
	private static final String NEWADDCUS_COUNT_URL = "https://ds.guoanshequ.cn/ds/rest/queryNewaddCusSumByDate";
	private static final String REWARDTIMES_COUNT_URL = "https://ds.guoanshequ.cn/ds/rest/queryRewardTimesSumByDate";
	private static final String CUSTOMER_COUNT_URL = "https://ds.guoanshequ.cn/ds/rest/queryCustomerSecondStoreSumByDate";
	private static final String GMV_URL = "https://ds.guoanshequ.cn/ds/rest/queryGmvPercentByDate";
	private static final String GMV_URL_NOT_CUR = "https://ds.guoanshequ.cn/ds/rest/queryTGmvPercent";
    //客户画像_以门店为单位查询该门店下所有国安侠客 18字段  注：使用的PES系统的WIKI接口 ---->  接口名称：获取单体画像接口信息
    private static final String CUSTOMERPROTITE = "https://ds.guoanshequ.cn/ds/rest/queryCustomers";
    //客户画像 6字段 和 18字段
    private static final String CUSTOMERPROTITE_SIXFIELD = "https://ds.guoanshequ.cn/ds/rest/queryCustomers";
    //拜访记录_以门店为单位查询该门店下所有国安侠客    注：使用的PES系统的WIKI接口 ----> 接口名称：获取拜访记录接口信息
    private static final String VISITRECORD_FORMDETAIL = "https://ds.guoanshequ.cn/ds/rest/queryRelations";
    // 片区GMV (片区某门店GMV = 落入片区拉新总GMV + 公海GMV)
    private static final String AREAGMV_FORMDETAIL = "https://ds.guoanshequ.cn/ds/rest/queryTAreaTrade";
    // 片区内拉新GMV (片区某门店GMV = 落入片区拉新总GMV + 公海GMV)
    private static final String NEWADDCUSTOMERGMV_FORMDETAIL = "https://ds.guoanshequ.cn/ds/rest/queryTAreaNewaddCus";
    // 片区重点产品GMV (片区某门店GMV = 落入片区拉新总GMV + 公海GMV)
    private static final String EMPHASSESPRODUCTSGMV_FORMDETAIL = "https://ds.guoanshequ.cn/ds/rest/queryTAreaZdGmv";
   // 国安侠片区GMV总数 
 	private static final String AREATRADEAMOUNT="https://ds.guoanshequ.cn/ds/rest/queryTAreaTradeByMonth";
 	// 国安侠片重点产品GMV总数 
// 	private static final String AREAZDGMVAMOUNT="https://ds.guoanshequ.cn/ds/rest/queryTAreaZdGmvByMonth";
 	// 国安侠片片区拉新
 	private static final String AREANEWADDCUSAMOUNT="https://ds.guoanshequ.cn/ds/rest/queryTAreaNewaddCusByMonth";
 // 最新国安侠GMV总数包括详细数量
    private static final String NEWAREATRADEAMOUNT="https://ds.guoanshequ.cn/ds/rest/queryEmployeePesgmvByEmp";
   
    private HSSFCellStyle style_header = null;
	private CellStyle cellStyle_common = null;

	@Override
	public Map<String, Object> getDataCardInfo(DynamicDto dd) {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		if(dd.getTarget()==2){//店长
			Store store = storeManager.findStore(dd.getStoreId());
			dd.setStoreNo(store.getStoreno());
		}
		
		Map<String, Object> result = new HashMap<String,Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar1 = Calendar.getInstance();
		Integer day = calendar1.get(Calendar.DAY_OF_MONTH);
		
		if(day==1){//当前日期是当月第一天
			calendar1.set(Calendar.DAY_OF_MONTH,1);
			calendar1.add(Calendar.DAY_OF_MONTH, -1);
			calendar1.set(Calendar.DAY_OF_MONTH,1);
		}else{
			calendar1.set(Calendar.DAY_OF_MONTH,1);
		}
        
        String firstDay = sdf.format(calendar1.getTime());
       
        Calendar calendar2 = Calendar.getInstance();
        String curDay = sdf.format(calendar2.getTime());
        dd.setBeginDate(firstDay);
        dd.setEndDate(curDay);
		
		Integer relation = dynamicDao.getRelation(dd);
		Integer customer = dynamicDao.getCustomer(dd);
		Integer sendorders = dynamicDao.getSendorders(dd);


		if(dd.getTarget()==1){
			//Integer business = dynamicDao.getBusinessInfo(dd);
			//Integer office = dynamicDao.getOfficeInfo(dd);
			//result.put("business", business);
			//result.put("office", office);
		}else if(dd.getTarget()==3){//国安侠
			result.put("sendorders", sendorders);//送单量
			result.put("relation", relation);//拜访记录
			result.put("customer", customer);//用户画像
			return result;
		}

		Integer newaddcus = dynamicDao.getNewaddcus(dd);//门店拉新
		Integer newAddcus_gax = dynamicDao.getNewaddcusOfGAX(dd);//门店拉新
		
		Double storetrade = dynamicDao.getStoretrade(dd);//门店gmv
		Double gaxtrade = dynamicDao.getStoretradeOfGAX(dd);//国安侠gmv
		Integer rewardtimes = dynamicDao.getRewardtimes(dd);
		//Integer rebuycus = dynamicDao.getRebuycus(dd);
		result.put("newaddcus", newaddcus);
		result.put("newaddcus_gax", newAddcus_gax);
		result.put("storetrade", storetrade);
		result.put("storetrade_gax", gaxtrade);
		result.put("rewardtimes", rewardtimes);
		
		result.put("sendorders", sendorders);
		result.put("relation", relation);
		result.put("rebuycus", "--");
		result.put("customer", customer);
		
		return result;
	}







	/**
	 *
	 * TODO 获取每日GMV Top10动态
	 * 2017年7月28日
	 * @author gaobaolei
	 * @param dd
	 * @return
	 */
	private String getGMVData(DynamicDto dd,String store,String limit){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String url = null;
		HttpClientUtil hClientUtil = null;
		Date date = new Date();
		String beginDate = sdf.format(date);
		String endDate = sdf.format(date);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("app_key", "1002");
		jsonObject.put("stamp",System.currentTimeMillis());
		jsonObject.put("nonce",UUID.randomUUID().toString());
		jsonObject.put("begindate", beginDate);
		jsonObject.put("enddate", endDate);
		jsonObject.put("storeids", store);
		if("Y".equals(limit)){
			jsonObject.put("limitcond", 10);
		}else{
			jsonObject.put("limitcond", 10000);
		}
		String sign = EncryptUtils.getMD5(jsonObject.toString()+SECRET);

		hClientUtil = new HttpClientUtil();
		url = DAILYDATA_GMV_URL+"?sign="+sign;
		String dailyData = hClientUtil.getRemoteData(url, jsonObject);
		return dailyData;



	}

	/**
	 *
	 * TODO 获取门店GMV数据
	 * 2017年11月21日
	 * @author zhangli
	 * @param dd
	 * @return
	 */
	private String getGMVMonthData(DynamicDto dd,int month,List<Map<String,Object>> storeMonthList){
		JSONArray json = new JSONArray();
        for(Map<String,Object> storeMonthGMV : storeMonthList){
            JSONObject jo = new JSONObject();
            jo.put("pesgmvpriord", storeMonthGMV.get("pesgmvpriord"));
            jo.put("city_name", storeMonthGMV.get("city_name"));
            jo.put("pesgmv", storeMonthGMV.get("pesgmv"));
            jo.put("storeno", storeMonthGMV.get("storeno"));
            jo.put("month", storeMonthGMV.get("month"));
            jo.put("year", storeMonthGMV.get("year"));
            jo.put("store_name", storeMonthGMV.get("store_name"));
            json.put(jo);
        }
        return json.toString();
	}
	
	/**
	 *
	 * TODO 获取每日订单数量
	 * 2017年7月28日
	 * @author gaobaolei
	 * @return
	 */
	private String getOrderData(DynamicDto dd,String store,String limit){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String url = null;
		HttpClientUtil hClientUtil = null;


		Date date = new Date();
		String beginDate = sdf.format(date);
		String endDate = sdf.format(date);


		JSONObject jsonObject = new JSONObject();
		jsonObject.put("app_key", "1002");
		jsonObject.put("stamp",System.currentTimeMillis());
		jsonObject.put("nonce",UUID.randomUUID().toString());
		jsonObject.put("begindate", beginDate);
		jsonObject.put("enddate", endDate);
		jsonObject.put("storeids", store);
		if("Y".equals(limit)){
			jsonObject.put("limitcond", 10);
		}else{
			jsonObject.put("limitcond", 10000);
		}

		String sign = EncryptUtils.getMD5(jsonObject.toString()+SECRET);

		hClientUtil = new HttpClientUtil();
		url = DAILYDATA_ORDER_URL+"?sign="+sign;
		String dailyData = hClientUtil.getRemoteData(url, jsonObject);
		return dailyData;


	}


	private String getRelationData(DynamicDto dd,String store,String limit){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String url = null;
		HttpClientUtil hClientUtil = null;


		Date date = new Date();
		String beginDate = sdf.format(date);
		String endDate = sdf.format(date);


		JSONObject jsonObject = new JSONObject();
		jsonObject.put("app_key", "1002");
		jsonObject.put("stamp",System.currentTimeMillis());
		jsonObject.put("nonce",UUID.randomUUID().toString());
		jsonObject.put("begindate", beginDate);
		jsonObject.put("enddate", endDate);
		jsonObject.put("storeids", store);
		if("Y".equals(limit)){
			jsonObject.put("limitcond", 10);
		}else{
			jsonObject.put("limitcond", 10000);
		}
		String sign = EncryptUtils.getMD5(jsonObject.toString()+SECRET);

		hClientUtil = new HttpClientUtil();
		url = DAILYDATA_RELATION_URL+"?sign="+sign;
		String dailyData = hClientUtil.getRemoteData(url, jsonObject);
		return dailyData;


	}

	@Override
	public Map<String, Object> getDailyData(DynamicDto dd) {

		Map<String,Object> result = new HashMap<String,Object>();
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dd.getEmployeeId(), dd.getCityId(), "CSZJ");//获取门店
		StringBuilder storeNO = new StringBuilder();
		for(Map<String, Object> map:storeList){
			if(map.get("number")==null||"".equals(map.get("number"))){
				continue;
			}
			storeNO.append(","+map.get("number"));
		}
		if(storeList!=null&&storeList.size()>0){
			storeNO = storeNO.deleteCharAt(0);
		}else{
			result.put("gmv", 0);
			result.put("order", 0);
			result.put("relation", 0);
			return result;
		}

		String gmv = this.getGMVData(dd, storeNO.toString(),dd.getLimit());
		String order = this.getOrderData(dd, storeNO.toString(),dd.getLimit());
		String relation = this.getRelationData(dd,storeNO.toString(),dd.getLimit());
		result.put("gmv", gmv);
		result.put("order", order);
		result.put("relation", relation);
		return result;

	}


	@Override
	public Map<String, Object> getMonthData(DynamicDto dd) {

		Map<String,Object> result = new HashMap<String,Object>();
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dd.getEmployeeId(), dd.getCityId(), "CSZJ");//获取门店
		if(storeList!=null&&storeList.size()>0){
		}else{
			result.put("gmv", 0);
			return result;
		}

		List<Map<String,Object>> storeMonthList = storeDao.getAllStoreGMVMonthOfCRM(dd.getEmployeeId(), dd.getCityId(), "CSZJ",dd,"pesgmv");//获取门店
		String gmv = this.getGMVMonthData(dd,dd.getMonth(),storeMonthList);
		result.put("gmv", gmv);
		return result;

	}
	@Override
	public Map<String, Object> getMonthDataCity(DynamicDto dd) {

		Map<String,Object> result = new HashMap<String,Object>();
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		List<Map<String,Object>> storeMonthList = storeDao.getAllStoreGMVMonthOfCRM(dd.getEmployeeId(), dd.getCityId(), "CSZJ",dd,"pesgmv");//获取门店
		String gmv = this.getGMVMonthData(dd,dd.getMonth(),storeMonthList);
		result.put("gmv", gmv);
		return result;

	}
	public String getDailyOrderData(DynamicDto dd,List<Map<String,Object>> dailyOrderList) {
		JSONArray json = new JSONArray();
        for(Map<String,Object> dailyOrder : dailyOrderList){
            JSONObject jo = new JSONObject();
            jo.put("checked_order_count", dailyOrder.get("checked_order_count"));
            jo.put("totle_price", dailyOrder.get("totle_price"));
            jo.put("total_order_count", dailyOrder.get("total_order_count"));
            jo.put("customer_count", dailyOrder.get("customer_count"));
            json.put(jo);
        }
        return json.toString();
	}
	@Override
	public Map<String, Object> getMonthPesgmvPriordData(DynamicDto dd) {

		Map<String,Object> result = new HashMap<String,Object>();
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dd.getEmployeeId(), dd.getCityId(), "CSZJ");//获取门店
		if(storeList!=null&&storeList.size()>0){
		}else{
			result.put("gmv", 0);
			return result;
		}

		List<Map<String,Object>> storeMonthList = storeDao.getAllStoreGMVMonthOfCRM(dd.getEmployeeId(), dd.getCityId(), "CSZJ",dd,"pesgmvpriord");//获取门店
		String gmv = this.getGMVMonthData(dd,dd.getMonth(),storeMonthList);
		result.put("gmv", gmv);
		return result;

	}
	
	@Override
	public Map<String, Object> getStoreByCity(Integer target,Long employeeId, Long cityId, String search_str) {
		Map<String,Object> result = new HashMap<String,Object>();
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		List<Map<String, Object>> list = storeDao.getStoreByCity(target,cityId, employeeId, search_str);
		result.put("storelist", list);
		return result;
	}



	@Override
	public Map<String, Object> queryNewaddcus(DynamicDto dynamicDto,PageInfo pageInfo) {
		Map<String, Object> result = new HashedMap();
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		try {
			if(dynamicDto.getTarget()==0){//总部
				if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店

					StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
					List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "ZB");//获取门店
					
					StringBuilder storeNO = new StringBuilder();
					for(Map<String, Object> map:storeList){
						
						if(map.get("storeno")==null||"".equals(map.get("storeno"))){
							continue;
						}
						
						storeNO.append(",'"+map.get("storeno")+"'");
					}
					if(storeNO.length()>0){
					
						storeNO = storeNO.deleteCharAt(0);

					}else{
						JSONObject temp = new JSONObject();
						temp.put("data", "");
						temp.put("message", "系统查无此条件的数据！");
						result.put("status","storefail");
						result.put("data",temp.toString());
						return result;
					}

					
					dynamicDto.setStoreNo(storeNO.toString());
				}else  if(dynamicDto.getStoreId()==-10000){
					dynamicDto.setStoreNo("-10000");
				}else{
					
					Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
					dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
				}
			}else if(dynamicDto.getTarget()==1){//城市总监
				if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店

					StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
					List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "CSZJ");//获取门店
					
					StringBuilder storeNO = new StringBuilder();
					for(Map<String, Object> map:storeList){
						
						if(map.get("storeno")==null||"".equals(map.get("storeno"))){
							continue;
						}
						
						storeNO.append(",'"+map.get("storeno")+"'");
					}
					if(storeNO.length()>0){
						
						storeNO = storeNO.deleteCharAt(0);

					}else{
						JSONObject temp = new JSONObject();
						temp.put("data", "");
						temp.put("message", "系统查无此条件的数据！");
						result.put("status","storefail");
						result.put("data",temp.toString());
						return result;
					}

					dynamicDto.setStoreNo(storeNO.toString());
				}else  if(dynamicDto.getStoreId()==-10000){
					dynamicDto.setStoreNumer("-10000");
					dynamicDto.setStoreNo("-10000");
				}else{
					
					Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
					dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
				}
			}else if(dynamicDto.getTarget()==2){//店长
				Store store = (Store)storeManager.getObject(dynamicDto.getStoreId());
				dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno())+"'");
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if("prev_month".equals(dynamicDto.getBeginDate())){
				Calendar calendar1 = Calendar.getInstance();
	            calendar1.add(Calendar.MONTH, -1);
	           
	            calendar1.set(Calendar.DAY_OF_MONTH,1);
	            String firstDay = sdf.format(calendar1.getTime());
	            //获取前一个月最后一天
	            Calendar calendar2 = Calendar.getInstance();
	            calendar2.set(Calendar.DAY_OF_MONTH, 0);
	            String lastDay = sdf.format(calendar2.getTime());
	            dynamicDto.setBeginDate(firstDay);
	            dynamicDto.setEndDate(lastDay);
			}else if("cur_month".equals(dynamicDto.getBeginDate())){
				Calendar c = Calendar.getInstance();    
		        c.add(Calendar.MONTH, 0);
		        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		        String first = sdf.format(c.getTime());
			        
		        //获取当前月最后一天
		        Calendar ca = Calendar.getInstance();    
		        //ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
		        String last = sdf.format(ca.getTime());
		        
		        dynamicDto.setBeginDate(first);
	            dynamicDto.setEndDate(last);
			}
			
			result= dynamicDao.storeNewaddcus(dynamicDto, pageInfo);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
			return result;
			
		}
		return result;
	}



	@Override
	public Map<String, Object> queryRebuycus(DynamicDto dynamicDto, PageInfo pageInfo) {
		Map<String, Object> result = new HashMap<String,Object>();
		if("prev_month".equals(dynamicDto.getBeginDate())||"cur_month".equals(dynamicDto.getBeginDate())){
			result = this.queryDataOfList(dynamicDto,REBUYCUS_URL_NOT_CUR , pageInfo);

		}else if("cur_day".equals(dynamicDto.getBeginDate())){//当天
			result = this.queryDataOfList(dynamicDto,REBUYCUS_URL, pageInfo);
		}
		return result;


	}



	@Override
	public Map<String, Object> queryStoreTradeByType(DynamicDto dynamicDto, PageInfo pageInfo) {
		Map<String, Object> result = new HashedMap();
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		try {
			if(dynamicDto.getTarget()==0){//总部
				if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店

					StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
					List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "ZB");//获取门店
					
					StringBuilder storeNO = new StringBuilder();
					for(Map<String, Object> map:storeList){
						
						if(map.get("storeno")==null||"".equals(map.get("storeno"))){
							continue;
						}
						
						storeNO.append(",'"+map.get("storeno")+"'");
					}
					if(storeNO.length()>0){
					
						storeNO = storeNO.deleteCharAt(0);

					}else{
						JSONObject temp = new JSONObject();
						temp.put("data", "");
						temp.put("message", "系统查无此条件的数据！");
						result.put("status","storefail");
						result.put("data",temp.toString());
						return result;
					}

					
					dynamicDto.setStoreNo(storeNO.toString());
				}else  if(dynamicDto.getStoreId()==-10000){
					dynamicDto.setStoreNo("-10000");
				}else{
					
					Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
					dynamicDto.setStoreNumer(store.getNumber()==null?"-10000":String.valueOf(store.getNumber()));
					dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
				}
			}else if(dynamicDto.getTarget()==1){//城市总监
				if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店

					StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
					List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "CSZJ");//获取门店
					
					StringBuilder storeNO = new StringBuilder();
					for(Map<String, Object> map:storeList){
						
						if(map.get("storeno")==null||"".equals(map.get("storeno"))){
							continue;
						}
						
						storeNO.append(",'"+map.get("storeno")+"'");
					}
					if(storeNO.length()>0){
						
						storeNO = storeNO.deleteCharAt(0);

					}else{
						JSONObject temp = new JSONObject();
						temp.put("data", "");
						temp.put("message", "系统查无此条件的数据！");
						result.put("status","storefail");
						result.put("data",temp.toString());
						return result;
					}

					dynamicDto.setStoreNo(storeNO.toString());
				}else  if(dynamicDto.getStoreId()==-10000){
					dynamicDto.setStoreNumer("-10000");
					dynamicDto.setStoreNo("-10000");
				}else{
					
					Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
					dynamicDto.setStoreNumer(store.getNumber()==null?"-10000":String.valueOf(store.getNumber()));
					dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
				}
			}else if(dynamicDto.getTarget()==2){//店长
				Store store = (Store)storeManager.getObject(dynamicDto.getStoreId());
				dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno())+"'");
			}

			Calendar calendar = Calendar.getInstance();
			System.out.println(dynamicDto.getBeginDate());
			if("prev_month".equals(dynamicDto.getBeginDate())){
	            calendar.add(Calendar.MONTH, -1);
	            dynamicDto.setYear(calendar.get(Calendar.YEAR));
	            dynamicDto.setMonth(calendar.get(Calendar.MONTH)+1);
			}else if("cur_month".equals(dynamicDto.getBeginDate())){
	            dynamicDto.setYear(calendar.get(Calendar.YEAR));
	            dynamicDto.setMonth(calendar.get(Calendar.MONTH)+1);
			}
			
			result= dynamicDao.storeGmv(dynamicDto, pageInfo);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
			return result;
			
		}
		return result;
	}



	@Override
	public Map<String, Object> queryStoreTradeByChannel(DynamicDto dynamicDto, PageInfo pageInfo) {
		Map<String, Object> result = new HashMap<String,Object>();
		if("prev_month".equals(dynamicDto.getBeginDate())||"cur_month".equals(dynamicDto.getBeginDate())){
			result = this.queryDataOfList(dynamicDto,STORETRADE_CHANNEL_URL_NOT_CUR, pageInfo);

		}else if("cur_day".equals(dynamicDto.getBeginDate())){//当天
			result = this.queryDataOfList(dynamicDto,STORETRADE_CHANNEL_URL, pageInfo);
		}
		return result;


	}





	/**
	 *
	 * TODO 根据门店查询数据
	 * 2017年8月7日
	 * @author gaobaolei
	 * @param dd
	 * @param source
	 * @return
	 */
	private JSONObject getInfo_CurMonth_Curday(DynamicDto dd,String url,String source){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		HttpClientUtil hClientUtil = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("app_key", "1002");
		jsonObject.put("stamp",System.currentTimeMillis());
		jsonObject.put("nonce",UUID.randomUUID().toString());
		jsonObject.put("begindate", dd.getBeginDate());
		jsonObject.put("enddate", dd.getEndDate());
		if("customer".contentEquals(source)||"relation".equals(source)){
			jsonObject.put("storeids", dd.getStoreIds());
		}else{
			jsonObject.put("storeids", dd.getStoreNumer());
		}


		String sign = EncryptUtils.getMD5(jsonObject.toString()+SECRET);

		hClientUtil = new HttpClientUtil();
		url = url+"?sign="+sign;
		String dailyData = hClientUtil.getRemoteData(url, jsonObject);
		return new JSONObject(dailyData);


	}

	/**
	 *
	 * TODO 根据国安侠 查询数据
	 * 2017年8月16日
	 * @author gaobaolei
	 * @param dd
	 * @param url
	 * @return
	 */
	private JSONObject getInfo_CurMonth_Curday_employee(DynamicDto dd,String url){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		HttpClientUtil hClientUtil = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("app_key", "1002");
		jsonObject.put("stamp",System.currentTimeMillis());
		jsonObject.put("nonce",UUID.randomUUID().toString());
		jsonObject.put("begindate", dd.getBeginDate());
		jsonObject.put("enddate", dd.getEndDate());
		jsonObject.put("employeeno", dd.getEmployeeNo());

		String sign = EncryptUtils.getMD5(jsonObject.toString()+SECRET);

		hClientUtil = new HttpClientUtil();
		url = url+"?sign="+sign;
		String dailyData = hClientUtil.getRemoteData(url, jsonObject);
		return new JSONObject(dailyData);


	}

	private JSONObject getRebuycusInfo_CurMonth_Curday(DynamicDto dd,String url){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		HttpClientUtil hClientUtil = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("app_key", "1002");
		jsonObject.put("stamp",System.currentTimeMillis());
		jsonObject.put("nonce",UUID.randomUUID().toString());
		jsonObject.put("year", dd.getYear());
		String monthStr = "";
		if(dd.getMonth()<10){
			monthStr="0"+dd.getMonth();
		}
		jsonObject.put("month", monthStr);
		jsonObject.put("storeids", dd.getStoreNumer());

		String sign = EncryptUtils.getMD5(jsonObject.toString()+SECRET);

		hClientUtil = new HttpClientUtil();
		url = url+"?sign="+sign;
		String dailyData = hClientUtil.getRemoteData(url, jsonObject);
		return new JSONObject(dailyData);


	}


	@Override
	public Map<String, Object> getDataCardInfoOfCurMonth(DynamicDto dd) {

		Map<String, Object> result = new HashMap<String,Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,1);
		String beginDate = sdf.format(calendar.getTime());

		String endDate = sdf.format(new Date());
		dd.setBeginDate(beginDate);
		dd.setEndDate(endDate);
		List<Map<String, Object>> storeList = new ArrayList<Map<String,Object>>();
		JSONObject jObject = null;
		if(dd.getTarget()==1){//城市总监
			StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
			storeList = storeDao.getAllStoreOfCRM(dd.getEmployeeId(), dd.getCityId(), "CSZJ");//获取门店
			StringBuilder storeNO = new StringBuilder();
			StringBuilder storeID = new StringBuilder();
			for(Map<String, Object> map:storeList){
				if(map.get("store_id")==null||"".equals(map.get("store_id"))){
					continue;
				}
				storeID.append(","+map.get("store_id"));
				if(map.get("number")==null||"".equals(map.get("number"))){
					continue;
				}
				storeNO.append(","+map.get("number"));
			}
			if(storeList!=null&&storeList.size()>0){
				storeID = storeID.deleteCharAt(0);
				storeNO = storeNO.deleteCharAt(0);
			}else{
				result.put("newaddcus",0);
				result.put("rebuycus",0);
				result.put("storetrade",0);
				result.put("sendorders",0);
				result.put("relation",0);
				result.put("customer",0);
				result.put("rewardtimes",0);
				return result;
			}

			dd.setStoreNumer(storeNO.toString());
			dd.setStoreIds(storeID.toString());
		}else if(dd.getTarget()==2){//店长
			StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
			Store store = (Store)storeManager.getObject(dd.getStoreId());
			dd.setStoreNumer(String.valueOf(store.getNumber()));
			dd.setStoreIds(store.getStore_id().toString());
		}else  if(dd.getTarget()==3){//国安侠



			jObject =  this.getInfo_CurMonth_Curday_employee(dd, RELATION_COUNT_URL_GAX);
			if("2000000".equals(jObject.getString("status"))){//成功返回数据
				result.put("relation",jObject.getJSONArray("data").getJSONObject(0).get("relationsum"));
			}else{
				result.put("relation",0);
			}

			jObject = this.getInfo_CurMonth_Curday_employee(dd, CUSTOMER_COUNT_URL_GAX);
			if("2000000".equals(jObject.getString("status"))){//成功返回数据
				result.put("customer", jObject.getJSONArray("data").getJSONObject(0).get("amount"));
			}else{
				result.put("customer",0);
			}
			dd.setEmployeeNo(dd.getEmployeeNo()+"+"+dd.getEmployeeName());
			jObject =  this.getInfo_CurMonth_Curday_employee(dd, SENDORDER_COUNT_URL_GAX);
			if("2000000".equals(jObject.getString("status"))){//成功返回数据
				result.put("sendorders", jObject.getJSONArray("data").getJSONObject(0).get("dashang"));
			}else{
				result.put("sendorders",0);
			}
			return result;
		}

		JSONObject newaddcus = this.getInfo_CurMonth_Curday(dd, NEWADDCUS_COUNT_URL,"newaddcus");
		JSONObject rebuycus = this.getRebuycusInfo_CurMonth_Curday(dd, REBUYCUS_COUNT_URL);
		JSONObject storetrade = this.getInfo_CurMonth_Curday(dd, STORETRADE_COUNT_URL,"storetrade");
		JSONObject sendorders = this.getInfo_CurMonth_Curday(dd, SENDORDER_COUNT_URL,"sendorders");
		JSONObject relation = this.getInfo_CurMonth_Curday(dd, RELATION_COUNT_URL,"relation");
		JSONObject customer = this.getInfo_CurMonth_Curday(dd, CUSTOMER_COUNT_URL,"customer");
		JSONObject rewardtimes = this.getInfo_CurMonth_Curday(dd, REWARDTIMES_COUNT_URL,"rewardtimes");

		if("2000000".equals(newaddcus.getString("status"))){//成功返回数据 付费用户
			result.put("newaddcus", newaddcus.getJSONArray("data").getJSONObject(0).get("newaddsum"));
		}else{
			result.put("newaddcus",0);
		}

		if("2000000".equals(rebuycus.getString("status"))){//成功返回数据 复购用户
			result.put("rebuycus", rebuycus.getJSONArray("data").getJSONObject(0).get("rebuysum"));
		}else{
			result.put("rebuycus",0);
		}

		if("2000000".equals(storetrade.getString("status"))){//成功返回数据 订单
			result.put("storetrade", storetrade.getJSONArray("data").getJSONObject(0).get("storetradesum"));
		}else{
			result.put("storetrade",0);
		}

		if("2000000".equals(sendorders.getString("status"))){//成功返回数据 送单
			result.put("sendorders", sendorders.getJSONArray("data").getJSONObject(0).get("sendordersum"));
		}else{
			result.put("sendorders",0);
		}

		if("2000000".equals(relation.getString("status"))){//成功返回数据
			result.put("relation", relation.getJSONArray("data").getJSONObject(0).get("relationsum"));
		}else{
			result.put("relation",0);
		}

		if("2000000".equals(customer.getString("status"))){//成功返回数据
			result.put("customer", customer.getJSONArray("data").getJSONObject(0).get("customersum"));
		}else{
			result.put("customer",0);
		}

		if("2000000".equals(rewardtimes.getString("status"))){//成功返回数据
			result.put("rewardtimes", rewardtimes.getJSONArray("data").getJSONObject(0).get("dashangsum"));
		}else{
			result.put("rewardtimes",0);
		}
		return result;
	}



	@Override
	public Map<String, Object> getDataCardInfoOfCurDay(DynamicDto dd) {


		Map<String, Object> result = new HashMap<String,Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String beginDate = sdf.format(date);

		String endDate = sdf.format(date);
		dd.setBeginDate(beginDate);
		dd.setEndDate(endDate);
		JSONObject jObject = null;
		List<Map<String,Object>> storeList = new ArrayList<Map<String,Object>>();
		if(dd.getTarget()==1){//城市总监
			StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
			storeList = storeDao.getAllStoreOfCRM(dd.getEmployeeId(), dd.getCityId(), "CSZJ");//获取门店
			StringBuilder storeNO = new StringBuilder();
			for(Map<String, Object> map:storeList){
				if(map.get("number")==null||"".equals(map.get("number"))){
					continue;
				}
				storeNO.append(","+map.get("number"));
			}
			if(storeList!=null&&storeList.size()>0){
				storeNO = storeNO.deleteCharAt(0);
			}else{
				result.put("newaddcus",0);
				result.put("storetrade",0);
				result.put("sendorders",0);
				result.put("relation",0);
				result.put("rewardtimes",0);
				return result;
			}

			dd.setStoreNumer(storeNO.toString());
		}else if(dd.getTarget()==2){//店长
			StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
			Store store = (Store)storeManager.getObject(dd.getStoreId());
			dd.setStoreNumer(String.valueOf(store.getNumber()));
		}else  if(dd.getTarget()==3){//国安侠



			jObject =  this.getInfo_CurMonth_Curday_employee(dd, RELATION_COUNT_URL_GAX);
			if("2000000".equals(jObject.getString("status"))){//成功返回数据
				result.put("relation",jObject.getJSONArray("data").getJSONObject(0).get("relationsum"));
			}else{
				result.put("relation",0);
			}

			jObject = this.getInfo_CurMonth_Curday_employee(dd, CUSTOMER_COUNT_URL_GAX);
			if("2000000".equals(jObject.getString("status"))){//成功返回数据
				result.put("customer", jObject.getJSONArray("data").getJSONObject(0).get("amount"));
			}else{
				result.put("customer",0);
			}

			dd.setEmployeeNo(dd.getEmployeeNo()+"+"+dd.getEmployeeName());
			jObject =  this.getInfo_CurMonth_Curday_employee(dd, SENDORDER_COUNT_URL_GAX);
			if("2000000".equals(jObject.getString("status"))){//成功返回数据
				result.put("sendorders", jObject.getJSONArray("data").getJSONObject(0).get("dashang"));
			}else{
				result.put("sendorders",0);
			}

			return result;
		}



		JSONObject newaddcus = this.getInfo_CurMonth_Curday(dd, NEWADDCUS_COUNT_URL,"newaddcus");
		//String rebuycus = this.getInfo_CurMonth_Curday(dd, REBUYCUS_COUNT_URL);
		JSONObject storetrade = this.getInfo_CurMonth_Curday(dd, STORETRADE_COUNT_URL,"storetrade");
		JSONObject sendorders = this.getInfo_CurMonth_Curday(dd, SENDORDER_COUNT_URL,"sendorders");
		//JSONObject relation = this.getInfo_CurMonth_Curday(dd, RELATION_COUNT_URL);

		JSONObject rewardtimes = this.getInfo_CurMonth_Curday(dd, REWARDTIMES_COUNT_URL,"rewardtimes");

		if("2000000".equals(newaddcus.getString("status"))){//成功返回数据
			result.put("newaddcus", newaddcus.getJSONArray("data").getJSONObject(0).get("newaddsum"));
		}else{
			result.put("newaddcus",0);
		}

		if("2000000".equals(storetrade.getString("status"))){//成功返回数据
			result.put("storetrade", storetrade.getJSONArray("data").getJSONObject(0).get("storetradesum"));
		}else{
			result.put("storetrade",0);
		}

		if("2000000".equals(sendorders.getString("status"))){//成功返回数据
			result.put("sendorders", sendorders.getJSONArray("data").getJSONObject(0).get("sendordersum"));
		}else{
			result.put("sendorders",0);
		}

		/*if("2000000".equals(relation.getString("status"))){//成功返回数据
			result.put("relation", relation.getJSONArray("data").getJSONObject(0).get("relationsum"));
		}else{
			result.put("relation",0);
		}*/

		if("2000000".equals(rewardtimes.getString("status"))){//成功返回数据
			result.put("rewardtimes", rewardtimes.getJSONArray("data").getJSONObject(0).get("dashangsum"));
		}else{
			result.put("rewardtimes",0);
		}

		return result;

	}

	@Override
	public Map<String, Object> getDailyOrderOfCurDay(DynamicDto dd) {


		Map<String, Object> result = new HashMap<String,Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String beginDate = sdf.format(date);
		String endDate = sdf.format(date);
		dd.setBeginDate(beginDate);
		dd.setEndDate(endDate);
		List<Map<String,Object>> storeList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> dailyOrderList = new ArrayList<Map<String,Object>>();
		OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		StringBuilder storeNO = new StringBuilder();
		String cityNo = "";
		if(dd.getTarget()==1){//城市总监
			StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
			storeList = storeDao.getCityNOOfCSZJ(dd.getCityId());//获取城市编码-cityno
			if(storeList!=null&&storeList.size()>0){
				cityNo = storeList.get(0).get("cityno").toString();
			}
			dd.setStoreNumer(storeNO.toString());
		}else if(dd.getTarget()==2){//店长
			StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
			Store store = (Store)storeManager.getObject(dd.getStoreId());
			dd.setStoreNo(String.valueOf(store.getStoreno()));
		}else  if(dd.getTarget()==3){//国安侠
			dd.setEmployeeId(dd.getEmployeeId());
		}
		dailyOrderList = orderDao.getDailyOrderOfCurDay(cityNo,dd);
		String dailyOrder = this.getDailyOrderData(dd,dailyOrderList);
		result.put("daily", dailyOrder);
		return result;
	}
	private HSSFCellStyle getHeaderStyle(){
		return style_header;
	}

	private void setHeaderStyle(HSSFWorkbook wb){

		// 创建单元格样式
		style_header = wb.createCellStyle();
		style_header.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style_header.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style_header.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		style_header.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// 设置边框
		style_header.setBottomBorderColor(HSSFColor.BLACK.index);
		style_header.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style_header.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style_header.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style_header.setBorderTop(HSSFCellStyle.BORDER_THIN);

	}

	private void setCellStyle_common(Workbook wb){
		cellStyle_common=wb.createCellStyle();
		cellStyle_common.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
		cellStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);//垂直居中
		
	}

	private CellStyle getCellStyle_common() {
		return cellStyle_common;
	}

	public void setCellValue(Row obj_row, int nCellIndex, Object value){
		Cell cell=obj_row.createCell(nCellIndex);
		cell.setCellStyle(getCellStyle_common());
		cell.setCellValue(new HSSFRichTextString(value==null?null:value.toString()));
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	}

	@Override
	public Map<String, Object> exportStoreTradeByType(DynamicDto dynamicDto) {
		Map<String,Object> result  = new HashMap<String,Object>();
		Map<String,Object> map  = this.queryStoreTradeByType(dynamicDto, null);
		if("success".equals(map.get("status"))){//成功返回数据
			List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("gmv");
			if(list==null||list.size()==0){
				result.put("message","没有符合条件的数据！");
				result.put("status","null");
				return result;
			}
			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");

			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab

			setCellStyle_common(wb);
			setHeaderStyle(wb);
			HSSFSheet sheet = wb.createSheet("门店GMV");
			HSSFRow row = sheet.createRow(0);
			String[] str_headers = {"城市","门店名称","门店编号","绩效GMV"};
			String[] headers_key = {"city_name","store_name","storeno","pesgmv"};
			for(int i = 0;i < str_headers.length;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(getHeaderStyle());
				cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			}

			for(int i = 0;i < list.size();i++){
				row = sheet.createRow(i+1);
				for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
				}
			}



			File file_xls = new File(str_file_dir_path + File.separator+System.currentTimeMillis()+"_storetrade.xls");
			if(file_xls.exists()){
				file_xls.delete();
			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file_xls.getAbsoluteFile());
				wb.write(os);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			result.put("message","导出成功！");
			result.put("status","success");
			result.put("data", str_web_path.concat(file_xls.getName()));
		}else{
			result.put("message","请重新操作！");
			result.put("status","fail");
		}


		return result;
	}



	@Override
	public Map<String, Object> exportStoreTradeByChannel(DynamicDto dynamicDto) {
		Map<String,Object> result  = new HashMap<String,Object>();
		Map<String,Object> map  = this.queryStoreTradeByChannel(dynamicDto, null);
		if("2000000".equals(map.get("code"))){//成功返回数据
			JSONObject jObject = new JSONObject(map.get("data").toString());
			JSONArray storeTrade = jObject.getJSONArray("data");
			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");

			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab

			setCellStyle_common(wb);
			setHeaderStyle(wb);
			HSSFSheet sheet = wb.createSheet("交易额");
			HSSFRow row = sheet.createRow(0);
			String[] str_headers = {"城市","门店","事业部","频道","交易额","订单量"};
			String[] headers_key = {"city_name","store_name","dep_name","channel_name","order_amount","order_count"};
			for(int i = 0;i < str_headers.length;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(getHeaderStyle());
				cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			}

			for(int i = 0;i < storeTrade.length();i++){
				row = sheet.createRow(i+1);
				for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					setCellValue(row, cellIndex, storeTrade.getJSONObject(i).get(headers_key[cellIndex]));
				}
			}



			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_storetrade2.xls");
			if(file_xls.exists()){
				file_xls.delete();
			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file_xls.getAbsoluteFile());
				wb.write(os);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			result.put("message","导出成功！");
			result.put("status","success");
			result.put("data", str_web_path.concat(file_xls.getName()));
		}else{
			result.put("message","请重新操作！");
			result.put("status","fail");
		}

		return result;
	}



	@Override
	public Map<String, Object> querySendOrderSumByType(DynamicDto dynamicDto, PageInfo pageInfo) {

		Map<String, Object> result = new HashMap<String,Object>();
		if("prev_month".equals(dynamicDto.getBeginDate())||"cur_month".equals(dynamicDto.getBeginDate())){
			result = this.queryDataOfList(dynamicDto,SENDORDER_TYPE_URL_NOT_CUR, pageInfo);

		}else if("cur_day".equals(dynamicDto.getBeginDate())){//当天
			result = this.queryDataOfList(dynamicDto,SENDORDER_TYPE_URL, pageInfo);
		}
		return result;
	}



	@Override
	public Map<String, Object> querySendOrderSumByChannel(DynamicDto dynamicDto, PageInfo pageInfo) {
		Map<String, Object> result = new HashMap<String,Object>();
		if("prev_month".equals(dynamicDto.getBeginDate())||"cur_month".equals(dynamicDto.getBeginDate())){
			result = this.queryDataOfList(dynamicDto,SENDORDER_CHANNEL_URL_NOT_CUR, pageInfo);

		}else if("cur_day".equals(dynamicDto.getBeginDate())){//当天
			result = this.queryDataOfList(dynamicDto,SENDORDER_CHANNEL_URL, pageInfo);
		}
		return result;
	}



	@Override
	public Map<String, Object> exportSendOrderSumByType(DynamicDto dynamicDto) {
		Map<String,Object> result  = new HashMap<String,Object>();
		Map<String,Object> map  = this.querySendOrderSumByType(dynamicDto, null);
		if("2000000".equals(map.get("code"))){//成功返回数据
			JSONObject jObject = new JSONObject(map.get("data").toString());
			JSONArray storeTrade = jObject.getJSONArray("data");
			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");

			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab

			setCellStyle_common(wb);
			setHeaderStyle(wb);
			HSSFSheet sheet = wb.createSheet("门店送单量");
			HSSFRow row = sheet.createRow(0);
			String[] str_headers = {"城市","门店","国安侠","总送单量","总送单量（去除快周边）"};
			String[] headers_key = {"cityname","storename","employeename","dashang","good"};
			for(int i = 0;i < str_headers.length;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(getHeaderStyle());
				cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			}

			for(int i = 0;i < storeTrade.length();i++){
				row = sheet.createRow(i+1);
				for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					setCellValue(row, cellIndex, storeTrade.getJSONObject(i).get(headers_key[cellIndex]));
				}
			}



			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_sendorder1.xls");
			if(file_xls.exists()){
				file_xls.delete();
			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file_xls.getAbsoluteFile());
				wb.write(os);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			result.put("message","导出成功！");
			result.put("status","success");
			result.put("data", str_web_path.concat(file_xls.getName()));
		}else{
			result.put("message","请重新操作！");
			result.put("status","fail");
		}

		return result;
	}



	@Override
	public Map<String, Object> exportSendOrderSumByChannel(DynamicDto dynamicDto) {
		Map<String,Object> result  = new HashMap<String,Object>();
		Map<String,Object> map  = this.querySendOrderSumByChannel(dynamicDto, null);
		if("2000000".equals(map.get("code"))){//成功返回数据
			JSONObject jObject = new JSONObject(map.get("data").toString());
			JSONArray storeTrade = jObject.getJSONArray("data");
			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");

			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab

			setCellStyle_common(wb);
			setHeaderStyle(wb);
			HSSFSheet sheet = wb.createSheet("订单量");
			HSSFRow row = sheet.createRow(0);
			String[] str_headers = {"城市","门店","国安侠","事业部","频道","总送单量"};
			String[] headers_key = {"cityname","storename","username","deptname","channelname","datanum"};
			for(int i = 0;i < str_headers.length;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(getHeaderStyle());
				cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			}

			for(int i = 0;i < storeTrade.length();i++){
				row = sheet.createRow(i+1);
				for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					setCellValue(row, cellIndex, storeTrade.getJSONObject(i).get(headers_key[cellIndex]));
				}
			}



			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_sendorder2.xls");
			if(file_xls.exists()){
				file_xls.delete();
			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file_xls.getAbsoluteFile());
				wb.write(os);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			result.put("message","导出成功！");
			result.put("status","success");
			result.put("data", str_web_path.concat(file_xls.getName()));
		}else{
			result.put("message","请重新操作！");
			result.put("status","fail");
		}

		return result;
	}



	@Override
	public Map<String, Object> queryRewardTimes(DynamicDto dynamicDto, PageInfo pageInfo) {
		Map<String, Object> result = new HashMap<String,Object>();
		if("prev_month".equals(dynamicDto.getBeginDate())||"cur_month".equals(dynamicDto.getBeginDate())){
			result = this.queryDataOfList(dynamicDto,REWARDTIMES_URL_NOT_CUR , pageInfo);

		}else if("cur_day".equals(dynamicDto.getBeginDate())){//当天
			result = this.queryDataOfList(dynamicDto,REWARDTIMES_URL, pageInfo);
		}
		return result;

	}



	@Override
	public Map<String, Object> exportRewardTimes(DynamicDto dynamicDto) {
		Map<String,Object> result  = new HashMap<String,Object>();
		Map<String,Object> map  = this.queryRewardTimes(dynamicDto, null);
		if("2000000".equals(map.get("code"))){//成功返回数据
			JSONObject jObject = new JSONObject(map.get("data").toString());
			JSONArray storeTrade = jObject.getJSONArray("data");
			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");

			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab

			setCellStyle_common(wb);
			setHeaderStyle(wb);
			HSSFSheet sheet = wb.createSheet("国安侠好评次数");
			HSSFRow row = sheet.createRow(0);
			String[] str_headers = {"城市","门店","国安侠","客户ID","订单编号","打赏次数"};
			String[] headers_key = {"cityname","storename","employeename","cusid","ordersn","dashang"};
			for(int i = 0;i < str_headers.length;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(getHeaderStyle());
				cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			}

			for(int i = 0;i < storeTrade.length();i++){
				row = sheet.createRow(i+1);
				for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					setCellValue(row, cellIndex, storeTrade.getJSONObject(i).get(headers_key[cellIndex]));
				}
			}



			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_rewardtimes.xls");
			if(file_xls.exists()){
				file_xls.delete();
			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file_xls.getAbsoluteFile());
				wb.write(os);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			result.put("message","导出成功！");
			result.put("status","success");
			result.put("data", str_web_path.concat(file_xls.getName()));
		}else{
			result.put("message","请重新操作！");
			result.put("status","fail");
		}

		return result;
	}




	@Override
	public Map<String, Object> queryBusiness(DynamicDto dd, PageInfo pageInfo) {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		pageInfo.setRecordsPerPage(10);
		if(dd.getSearchstr()==null){
			dd.setSearchstr("");
		}
		return dynamicDao.queryBusiness(dd, pageInfo);
	}

	@Override
	public Map<String, Object> queryOffice(DynamicDto dd, PageInfo pageInfo) {

		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		pageInfo.setRecordsPerPage(10);
		if(dd.getSearchstr()==null){
			dd.setSearchstr("");
		}
		return dynamicDao.queryOffice(dd, pageInfo);
	}



	@Override
	public Map<String, Object> queryRelation(DynamicDto dynamicDto, PageInfo pageInfo) {
		String beginData = "";
		String endDate = "";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

		Map<String, Object> result = new HashMap<String,Object>();
		if(dynamicDto.getBeginDate()!=null&&!"".equals(dynamicDto.getBeginDate())){
			beginData = dynamicDto.getBeginDate().split("-")[0];
			endDate = dynamicDto.getBeginDate().split("-")[1];
			beginData = sdf.format(new Date(beginData));
			endDate = sdf.format(new Date(endDate));
		}


		String url = null;
		HttpClientUtil hClientUtil = null;


		JSONObject param =   new JSONObject();
		if(dynamicDto.getTarget()==1){//城市总监
			if(dynamicDto.getStoreNumer()==null||"".equals(dynamicDto.getStoreNumer())){//查询所有

				StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
				List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "CSZJ");//获取门店
				StringBuilder storeNO = new StringBuilder();
				for(Map<String, Object> map:storeList){
					if(map.get("number")==null||"".equals(map.get("number"))){
						continue;
					}
					storeNO.append(","+map.get("number"));
				}
				if(storeList!=null&&storeList.size()>0){
					storeNO = storeNO.deleteCharAt(0);
				}else{
					result.put("status","success");
					result.put("data",null);
					return result;
				}
				dynamicDto.setStoreNumer(storeNO.toString());
			}
		}else if(dynamicDto.getTarget()==2){//店长
			StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
			Store store = (Store)storeManager.getObject(dynamicDto.getStoreId());
			dynamicDto.setStoreNumer(String.valueOf(store.getNumber()));
		}

		pageInfo.setRecordsPerPage(10);
		Integer firstRecordIndex = pageInfo.getRecordsPerPage()* (pageInfo.getCurrentPage() - 1);//每页起始行数
		param.put("limitcond",firstRecordIndex+","+pageInfo.getRecordsPerPage());
		param.put("storeids", dynamicDto.getStoreNumer());
		param.put("app_key", "1002");
		param.put("stamp",System.currentTimeMillis());
		param.put("nonce",UUID.randomUUID().toString());
		param.put("begindate", beginData);
		param.put("enddate", endDate);


		String sign = EncryptUtils.getMD5(param.toString()+SECRET);

		hClientUtil = new HttpClientUtil();
		url = RELATION_LIST_URL+"?sign="+sign;
		String data = hClientUtil.getRemoteData(url, param);
		JSONObject jObject = new JSONObject(data);
		if("2000000".equals(jObject.get("status"))){
			Object datacount = jObject.get("datacount")==null?0:jObject.get("datacount");
			pageInfo.setTotalRecords(Integer.parseInt(datacount.toString()));
			result.put("totalPage", (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1);
		}
		result.put("status","success");
		result.put("data",data);
		return result;
	}


	@Override
	public Map<String, Object> queryCustomer(DynamicDto dynamicDto, PageInfo pageInfo) {
		String beginData = "";
		String endDate = "";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

		Map<String, Object> result = new HashMap<String,Object>();
		if(dynamicDto.getBeginDate()!=null&&!"".equals(dynamicDto.getBeginDate())){
			beginData = dynamicDto.getBeginDate().split("-")[0];
			endDate = dynamicDto.getBeginDate().split("-")[1];
			beginData = sdf.format(new Date(beginData));
			endDate = sdf.format(new Date(endDate));
		}


		String url = null;
		HttpClientUtil hClientUtil = null;


		JSONObject param =   new JSONObject();
		if(dynamicDto.getTarget()==1){//城市总监
			if(dynamicDto.getStoreNumer()==null||"".equals(dynamicDto.getStoreNumer())){//查询所有

				StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
				List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "CSZJ");//获取门店
				StringBuilder storeNO = new StringBuilder();
				for(Map<String, Object> map:storeList){
					if(map.get("number")==null||"".equals(map.get("number"))){
						continue;
					}
					storeNO.append(","+map.get("number"));
				}
				if(storeList!=null&&storeList.size()>0){
					storeNO = storeNO.deleteCharAt(0);
				}else{
					result.put("status","success");
					result.put("data",null);
					return result;
				}
				dynamicDto.setStoreNumer(storeNO.toString());
			}
		}else if(dynamicDto.getTarget()==2){//店长
			StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
			Store store = (Store)storeManager.getObject(dynamicDto.getStoreId());
			dynamicDto.setStoreNumer(String.valueOf(store.getNumber()));
		}

		pageInfo.setRecordsPerPage(10);
		Integer firstRecordIndex = pageInfo.getRecordsPerPage()* (pageInfo.getCurrentPage() - 1);//每页起始行数
		param.put("limitcond",firstRecordIndex+","+pageInfo.getRecordsPerPage());
		param.put("storeids", dynamicDto.getStoreNumer());
		param.put("app_key", "1002");
		param.put("stamp",System.currentTimeMillis());
		param.put("nonce",UUID.randomUUID().toString());
		param.put("begindate", beginData);
		param.put("enddate", endDate);


		String sign = EncryptUtils.getMD5(param.toString()+SECRET);

		hClientUtil = new HttpClientUtil();
		url = REWARDTIMES_URL+"?sign="+sign;
		String data = hClientUtil.getRemoteData(url, param);
		JSONObject jObject = new JSONObject(data);
		if("2000000".equals(jObject.get("status"))){
			Object datacount = jObject.get("datacount")==null?0:jObject.get("datacount");
			pageInfo.setTotalRecords(Integer.parseInt(datacount.toString()));
			result.put("totalPage", (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1);
		}
		result.put("status","success");
		result.put("data",data);
		return result;
	}


	@Override
	public Map<String, Object> exportOffice(DynamicDto dd) {
		Map<String, Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());

		if(dd.getSearchstr()==null){
			dd.setSearchstr("");
		}
		List<Map<String, Object>> list =  dynamicDao.queryOffice(dd);
		if(list==null||list.size()==0){

			result.put("status","fail");
			return result;
		}
		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_web_path = PropertiesUtil.getValue("file.web.root");

		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建Excel的工作sheet,对应到一个excel文档的tab

		setCellStyle_common(wb);
		setHeaderStyle(wb);
		HSSFSheet sheet = wb.createSheet("写字楼");
		HSSFRow row = sheet.createRow(0);
		String[] str_headers = {"城市","街道","写字楼"};
		String[] headers_key = {"cityname","townname","office_name"};
		for(int i = 0;i < str_headers.length;i++){
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(getHeaderStyle());
			cell.setCellValue(new HSSFRichTextString(str_headers[i]));
		}

		for(int i = 0;i < list.size();i++){
			row = sheet.createRow(i+1);
			for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
				setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
			}
		}



		File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_office.xls");
		if(file_xls.exists()){
			file_xls.delete();
		}
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file_xls.getAbsoluteFile());
			wb.write(os);
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		result.put("message","导出成功！");
		result.put("status","success");
		result.put("data", str_web_path.concat(file_xls.getName()));
		return  result;
	}



	@Override
	public Map<String, Object> exportBusiness(DynamicDto dd) {
		Map<String, Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());

		if(dd.getSearchstr()==null){
			dd.setSearchstr("");
		}
		List<Map<String, Object>> list =  dynamicDao.queryBusiness(dd);
		if(list==null||list.size()==0){

			result.put("status","fail");
			return result;
		}
		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_web_path = PropertiesUtil.getValue("file.web.root");

		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建Excel的工作sheet,对应到一个excel文档的tab

		setCellStyle_common(wb);
		setHeaderStyle(wb);
		HSSFSheet sheet = wb.createSheet("商铺");
		HSSFRow row = sheet.createRow(0);
		String[] str_headers = {"城市","街道","商铺"};
		String[] headers_key = {"cityname","townname","business_name"};
		for(int i = 0;i < str_headers.length;i++){
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(getHeaderStyle());
			cell.setCellValue(new HSSFRichTextString(str_headers[i]));
		}

		for(int i = 0;i < list.size();i++){
			row = sheet.createRow(i+1);
			for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
				setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
			}
		}



		File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_business.xls");
		if(file_xls.exists()){
			file_xls.delete();
		}
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file_xls.getAbsoluteFile());
			wb.write(os);
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		result.put("message","导出成功！");
		result.put("status","success");
		result.put("data", str_web_path.concat(file_xls.getName()));
		return  result;
	}








	@Override
	public Map<String, Object> exportNewaddcus(DynamicDto dynamicDto) {
		Map<String,Object> result  = new HashMap<String,Object>();
		Map<String,Object> map  = this.queryNewaddcus(dynamicDto, null);
		if("success".equals(map.get("status"))){//成功返回数据
			List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("gmv");
			if(list==null||list.size()==0){
				result.put("message","没有符合条件的数据！");
				result.put("status","null");
				return result;
			}
			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");

			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab

			setCellStyle_common(wb);
			setHeaderStyle(wb);
			HSSFSheet sheet = wb.createSheet("门店用户");
			HSSFRow row = sheet.createRow(0);
			String[] str_headers = {"城市","门店名称","门店编号","拉新用户超10元","消费用户"};
			String[] headers_key = {"store_city_name","store_name","store_code","new_10_count","total"};
			for(int i = 0;i < str_headers.length;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(getHeaderStyle());
				cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			}

			for(int i = 0;i <list.size();i++){
				row = sheet.createRow(i+1);
				for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
				}
			}



			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_store_newaddcus.xls");
			if(file_xls.exists()){
				file_xls.delete();
			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file_xls.getAbsoluteFile());
				wb.write(os);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			result.put("message","导出成功！");
			result.put("status","success");
			result.put("data", str_web_path.concat(file_xls.getName()));
		}else{
			result.put("message","请重新操作！");
			result.put("status","fail");
		}


		return result;
	}



	@Override
	public Map<String, Object> exportRebuycus(DynamicDto dynamicDto) {
		Map<String,Object> result  = new HashMap<String,Object>();
		Map<String,Object> map  = this.queryRebuycus(dynamicDto, null);
		if("2000000".equals(map.get("code"))){//成功返回数据
			JSONObject jObject = new JSONObject(map.get("data").toString());
			JSONArray storeTrade = jObject.getJSONArray("data");
			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");

			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab

			setCellStyle_common(wb);
			setHeaderStyle(wb);
			HSSFSheet sheet = wb.createSheet("复购用户");
			HSSFRow row = sheet.createRow(0);
			String[] str_headers = {"城市","门店","复购人数","超过20元用户复购人数","超过10元用户复购人数"};
			String[] headers_key = {"city_name","store_name","rebuy_count","rebuy_20_count","rebuy_10_count"};
			for(int i = 0;i < str_headers.length;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(getHeaderStyle());
				cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			}

			for(int i = 0;i < storeTrade.length();i++){
				row = sheet.createRow(i+1);
				for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					setCellValue(row, cellIndex, storeTrade.getJSONObject(i).get(headers_key[cellIndex]));
				}
			}



			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_storetrade1.xls");
			if(file_xls.exists()){
				file_xls.delete();
			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file_xls.getAbsoluteFile());
				wb.write(os);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			result.put("message","导出成功！");
			result.put("status","success");
			result.put("data", str_web_path.concat(file_xls.getName()));
		}else{
			result.put("message","请重新操作！");
			result.put("status","fail");
		}


		return result;
	}



	@Override
	public Map<String, Object> queryGMV(DynamicDto dynamicDto, PageInfo pageInfo) {
		Map<String, Object> result = new HashMap<String,Object>();
		if("prev_month".equals(dynamicDto.getBeginDate())||"cur_month".equals(dynamicDto.getBeginDate())){
			result = this.queryDataOfList(dynamicDto,GMV_URL_NOT_CUR , pageInfo);

		}else if("cur_day".equals(dynamicDto.getBeginDate())){//当天
			result = this.queryDataOfList(dynamicDto,GMV_URL, pageInfo);
		}
		return result;
	}



	@Override
	public Map<String, Object> exportGMV(DynamicDto dynamicDto) {
		Map<String,Object> result  = new HashMap<String,Object>();
		Map<String,Object> map  = this.queryGMV(dynamicDto, null);
		if("2000000".equals(map.get("code"))){//成功返回数据
			JSONObject jObject = new JSONObject(map.get("data").toString());
			JSONArray storeTrade = jObject.getJSONArray("data");
			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");

			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab

			setCellStyle_common(wb);
			setHeaderStyle(wb);
			HSSFSheet sheet = wb.createSheet("重点产品GMV统计");
			HSSFRow row = sheet.createRow(0);
			String[] str_headers = {"城市","门店","微超GMV（去除快周边、去除退货）","养老GMV（去除快周边、去除退货）","家务事GMV（去除快周边、去除退货）"};
			String[] headers_key = {"city_name","store_name","weichaoamount","yanglaoamount","jwsamount"};
			for(int i = 0;i < str_headers.length;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(getHeaderStyle());
				cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			}

			for(int i = 0;i < storeTrade.length();i++){
				row = sheet.createRow(i+1);
				for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					setCellValue(row, cellIndex, storeTrade.getJSONObject(i).get(headers_key[cellIndex]));
				}
			}



			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_gmv.xls");
			if(file_xls.exists()){
				file_xls.delete();
			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file_xls.getAbsoluteFile());
				wb.write(os);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			result.put("message","导出成功！");
			result.put("status","success");
			result.put("data", str_web_path.concat(file_xls.getName()));
		}else{
			result.put("message","请重新操作！");
			result.put("status","fail");
		}

		return result;
	}


	private   Map<String, Object> queryDataOfList(DynamicDto dynamicDto,String url,PageInfo pageInfo) {

		String beginData = "";
		String endDate = "";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

		Map<String, Object> result = new HashMap<String,Object>();
		/*if(dynamicDto.getBeginDate()!=null&&!"".equals(dynamicDto.getBeginDate())){
			beginData = dynamicDto.getBeginDate().split("-")[0];
			endDate = dynamicDto.getBeginDate().split("-")[1];
			beginData = sdf.format(new Date(beginData));
			endDate = sdf.format(new Date(endDate));
		}*/


		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		HttpClientUtil hClientUtil = null;
		JSONObject param =   new JSONObject();
		if(dynamicDto.getTarget()==0){//总部
			if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店

				StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
				List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "ZB");//获取门店
				StringBuilder storeNumber = new StringBuilder();
				StringBuilder storeNO = new StringBuilder();
				for(Map<String, Object> map:storeList){
					if(map.get("number")==null||"".equals(map.get("number"))){
						continue;
					}
					if(map.get("storeno")==null||"".equals(map.get("storeno"))){
						continue;
					}
					storeNumber.append(","+map.get("number"));
					storeNO.append(",'"+map.get("storeno")+"'");
				}
				if(storeNumber.length()>0&&storeNO.length()>0){
					storeNumber = storeNumber.deleteCharAt(0);
					storeNO = storeNO.deleteCharAt(0);

				}else{
					JSONObject temp = new JSONObject();
					temp.put("data", "");
					temp.put("message", "系统查无此条件的数据！");
					result.put("status","storefail");
					result.put("data",temp.toString());
					return result;
				}

				dynamicDto.setStoreNumer(storeNumber.toString());
				dynamicDto.setStoreNo(storeNO.toString());
			}else  if(dynamicDto.getStoreId()==-10000){
				dynamicDto.setStoreNumer("-10000");
				dynamicDto.setStoreNo("-10000");
			}else{
				
				Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
				dynamicDto.setStoreNumer(store.getNumber()==null?"-10000":String.valueOf(store.getNumber()));
				dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
			}
		}else if(dynamicDto.getTarget()==1){//城市总监
			if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店

				StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
				List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "CSZJ");//获取门店
				StringBuilder storeNumber = new StringBuilder();
				StringBuilder storeNO = new StringBuilder();
				for(Map<String, Object> map:storeList){
					if(map.get("number")==null||"".equals(map.get("number"))){
						continue;
					}
					if(map.get("storeno")==null||"".equals(map.get("storeno"))){
						continue;
					}
					storeNumber.append(","+map.get("number"));
					storeNO.append(",'"+map.get("storeno")+"'");
				}
				if(storeNumber.length()>0&&storeNO.length()>0){
					storeNumber = storeNumber.deleteCharAt(0);
					storeNO = storeNO.deleteCharAt(0);

				}else{
					JSONObject temp = new JSONObject();
					temp.put("data", "");
					temp.put("message", "系统查无此条件的数据！");
					result.put("status","storefail");
					result.put("data",temp.toString());
					return result;
				}

				dynamicDto.setStoreNumer(storeNumber.toString());
				dynamicDto.setStoreNo(storeNO.toString());
			}else  if(dynamicDto.getStoreId()==-10000){
				dynamicDto.setStoreNumer("-10000");
				dynamicDto.setStoreNo("-10000");
			}else{
				
				Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
				dynamicDto.setStoreNumer(store.getNumber()==null?"-10000":String.valueOf(store.getNumber()));
				dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
			}
		}else if(dynamicDto.getTarget()==2){//店长
			Store store = (Store)storeManager.getObject(dynamicDto.getStoreId());
			dynamicDto.setStoreNumer(store.getNumber()==null?"-10000":String.valueOf(store.getNumber()));
			dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno())+"'");
		}

		Calendar calendar = Calendar.getInstance();
		System.out.println(dynamicDto.getBeginDate());
		if("prev_month".equals(dynamicDto.getBeginDate())){
            calendar.add(Calendar.MONTH, -1);
            param.put("app_key", "1002");
            //String times = DateUtil2.theLast();
			String yearmonth = this.getPrevDate();

            param.put("yearmonth", yearmonth);
            param.put("year", String.valueOf(calendar.get(Calendar.YEAR)));
            param.put("month",String.valueOf(calendar.get(Calendar.MONTH)+1));
            param.put("stamp",System.currentTimeMillis());
            param.put("nonce",UUID.randomUUID().toString());
            param.put("grade", "2");
            param.put("storeids", dynamicDto.getStoreNo());
		}else if("cur_month".equals(dynamicDto.getBeginDate())){
            param.put("app_key", "1002");
            //String time = DateUtil2.curDate();
			String yearmonth = this.getCurDate();
            param.put("yearmonth", yearmonth);
            param.put("year", String.valueOf(calendar.get(Calendar.YEAR)));
            param.put("month",String.valueOf(calendar.get(Calendar.MONTH)+1));
            param.put("stamp",System.currentTimeMillis());
            param.put("nonce",UUID.randomUUID().toString());
            param.put("grade", "2");
            param.put("storeids", dynamicDto.getStoreNo());
		} if("cur_day".equals(dynamicDto.getBeginDate())){//当天
			beginData = sdf.format(new Date());
			endDate = sdf.format(new Date());
			if(pageInfo!=null){
				pageInfo.setRecordsPerPage(10);
				Integer firstRecordIndex = pageInfo.getRecordsPerPage()* (pageInfo.getCurrentPage() - 1);//每页起始行数
				param.put("limitcond",firstRecordIndex+","+pageInfo.getRecordsPerPage());
			}

			param.put("storeids", dynamicDto.getStoreNumer());
			param.put("app_key", "1002");
			param.put("stamp",System.currentTimeMillis());
			param.put("nonce",UUID.randomUUID().toString());
			param.put("begindate", beginData);
			param.put("enddate", endDate);

		}

		String sign = EncryptUtils.getMD5(param.toString()+SECRET);
		url = url+"?sign="+sign;
		hClientUtil = new HttpClientUtil();
		String data = hClientUtil.getRemoteData(url, param);
		JSONObject jObject = new JSONObject(data);
		if("2000000".equals(jObject.get("status"))){
			Object datacount = jObject.get("datacount")==null?0:jObject.get("datacount");
			if(pageInfo!=null&&"cur_day".equals(dynamicDto.getBeginDate())){
				pageInfo.setTotalRecords(Integer.parseInt(datacount.toString()));
				result.put("totalPage", (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1);
			}else{
				result.put("totalPage",1);
			}

			result.put("code","2000000");
		}
		result.put("status","success");
		result.put("data",data);

		return result;

	}


	@Override
	public Map<String, Object> queryAbnormalOrder(AbnormalOrderDto abnormalOrderDto, PageInfo pageInfo) {
		// TODO Auto-generated method stub

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
//		if("cur_month".equals(abnormalOrderDto.getsDate())){
//			
//			abnormalOrderDto.setYear(calendar.get(Calendar.YEAR));
//			abnormalOrderDto.setMonth(calendar.get(Calendar.MONTH)+1);
//		}else if("prev_month".equals(abnormalOrderDto.getsDate())){
//			calendar.add(Calendar.MONTH, -1);
//			abnormalOrderDto.setYear(calendar.get(Calendar.YEAR));
//			abnormalOrderDto.setMonth(calendar.get(Calendar.MONTH)+1);
//		}
		
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			result = dynamicDao.queryAbnormalOrder(abnormalOrderDto, pageInfo);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
		}
		return result;
	}


	public Map<String, Object> exportAbnormalOrder(AbnormalOrderDto abnormalOrderDto) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Map<String, Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
//		if("cur_month".equals(abnormalOrderDto.getsDate())){
//			
//			abnormalOrderDto.setYear(calendar.get(Calendar.YEAR));
//			abnormalOrderDto.setMonth(calendar.get(Calendar.MONTH)+1);
//		}else if("prev_month".equals(abnormalOrderDto.getsDate())){
//			calendar.add(Calendar.MONTH, -1);
//			abnormalOrderDto.setYear(calendar.get(Calendar.YEAR));
//			abnormalOrderDto.setMonth(calendar.get(Calendar.MONTH)+1);
//		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = dynamicDao.queryAbnormalOrder(abnormalOrderDto);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if(list!=null&&list.size()>0){//成功返回数据

			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");

			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab
	        setCellStyle_common(wb);
	        setHeaderStyle(wb);
	        HSSFSheet sheet = wb.createSheet("异常订单");
	        HSSFRow row = sheet.createRow(0);
	        String[] str_headers = {"状态","城市","门店编号","门店名称","E店名称","事业部","频道","订单签收日期","订单号","订单金额","应付金额","有效金额","异常类型"};
	        String[] headers_key = {"state","cityname","storeno","storename","eshopname","deptname","channelname","signedtime","ordersn","tradingprice","payableprice","gmv_price","description"};
	        for(int i = 0;i < str_headers.length;i++){
	            HSSFCell cell = row.createCell(i);
	            cell.setCellStyle(getHeaderStyle());
	            cell.setCellValue(new HSSFRichTextString(str_headers[i]));
	        }
	        
	        for(int i = 0;i < list.size();i++){
	        	 row = sheet.createRow(i+1);
	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
	                 setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
	             }
	        }

			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_abnormalorder.xls");
			if(file_xls.exists()){
				file_xls.delete();
			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file_xls.getAbsoluteFile());
				wb.write(os);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			result.put("message","导出成功！");
			result.put("status","success");
			result.put("data", str_web_path.concat(file_xls.getName()));
		}else{
			result.put("message","请重新操作！");
			result.put("status","fail");
		}


		return result;
	}


	@Override
	public List<Map<String, Object>> queryAbnormalOrderType() {
		// TODO Auto-generated method stub
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		return dynamicDao.queryAbnormalType();
	}



	@Override
	public String importAbnormalOrder(List<File> list,String dataType)throws Exception {

		Date date = new Date();
		StringBuilder infosb = new StringBuilder();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(File file_excel : list) {

        	///////处理文件名开始/////
        	Long store_id=null;
        	//查找登录人
        	UserManager manager = (UserManager)SpringHelper.getBean("userManager");
        	StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
        	DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
        	DsAbnormalOrderManager dsAbnormalOrderManager =  (DsAbnormalOrderManager)SpringHelper.getBean("dsAbnormalOrderManager");
    		UserDTO userDTO = manager.getCurrentUserDTO();
    		
        	System.out.println(userDTO.getId()+"上传异常订单");
        	
            //读取excel文件
            InputStream is_excel = new FileInputStream(file_excel);
            //Excel工作簿
            Workbook wb_excel;
            Sheet sheet_data;
            try {
                //解析2003的xls模式的excel
                wb_excel = new HSSFWorkbook(is_excel);
            } catch (Exception e) {
                //如果2003模式解析异常，尝试解析2007模式
                wb_excel = new XSSFWorkbook(file_excel.getAbsolutePath());
            }
            int sheetCount = wb_excel.getNumberOfSheets();
            
           
            Map<String, Object> employMap = new HashMap<String, Object>();
            
            for(int i = 0; i < sheetCount;i++){
                sheet_data = wb_excel.getSheetAt(i);
                for(int nRowIndex = 1;nRowIndex < sheet_data.getPhysicalNumberOfRows();nRowIndex++){
                    Row row_order = sheet_data.getRow(nRowIndex);
                    if(row_order == null){
                        continue;
                    }
                    int cellnum = row_order.getLastCellNum();
                    if(cellnum<11||cellnum>11){
                    	infosb.append("<span>文件第"+(i+1)+"页"+"第"+(nRowIndex+1)+"行数据列数不符，请参照'异常订单基础数据导出文件模板'</span></br>");
                    	continue;
                    }
                    String cityname =  getCellValue(row_order.getCell(0));
                    String storeno = getCellValue(row_order.getCell(1));
                    if(storeno==null||"".equals(storeno)){
                    	infosb.append("<span>文件第"+(i+1)+"页"+"第"+(nRowIndex+1)+"行门店编码无效</span></br>");
                    	continue;
                    }
                    Object store = storeManager.getUniqueObject(FilterFactory.getSimpleFilter(" status=0 and storeno='"+storeno+"'"));
                    if(store == null){
                    	infosb.append("<span>文件第"+(i+1)+"页"+"第"+(nRowIndex+1)+"行门店编码无效</span></br>");
                    	continue;
                    }
                    
                    String storename =  getCellValue(row_order.getCell(2));
                    String eshopname = getCellValue(row_order.getCell(3));
                    String deptname =  getCellValue(row_order.getCell(4));
                    
                    String channelname =  getCellValue(row_order.getCell(5));
//                    String year =  getCellValue(row_order.getCell(6));
//                    if(year==null||"".equals(year)){
//                    	infosb.append("<span>文件第"+(i+1)+"页"+"第"+nRowIndex+"行'年份'无效</span></br>");
//                    	continue;
//                    }
//                    String month =  getCellValue(row_order.getCell(7));
//                    if(month==null||"".equals(month)){
//                    	infosb.append("<span>文件第"+(i+1)+"页"+"第"+nRowIndex+"行'月份'无效</span></br>");
//                    	continue;
//                    }
                    String signedtime = getCellValue(row_order.getCell(6));
                    if(signedtime==null||"".equals(signedtime)){
                    	infosb.append("<span>文件第"+(i+1)+"页"+"第"+(nRowIndex+1)+"行'日期'无效</span></br>");
                    	continue;
                    }
                    String ordersn = getCellValue(row_order.getCell(7));
                    if(ordersn==null||"".equals(ordersn)){
                    	infosb.append("<span>文件第"+(i+1)+"页"+"第"+(nRowIndex+1)+"行订单号无效</span></br>");
                    	continue;
                    }
                    List<Map<String, Object>> orderlist = dynamicDao.queryAbnormalByOrderSn(ordersn);
                    if(orderlist!=null&&orderlist.size()>0){
                    	infosb.append("<span>文件第"+(i+1)+"页"+"第"+(nRowIndex+1)+"行订单号已存在</span></br>");
                    	continue;
                    }
                    
                    String price = getCellValue(row_order.getCell(8));
                    if(price==null||"".equals(price)){
                    	infosb.append("<span>文件第"+(i+1)+"页"+"第"+(nRowIndex+1)+"行订单金额无效</span></br>");
                    	continue;
                    }
                    
                    String payprice = getCellValue(row_order.getCell(9));
                    
                    
                    String type = getCellValue(row_order.getCell(10));
                    if(type==null||"".equals(type)){
                    	infosb.append("<span>文件第"+(i+1)+"页"+"第"+(nRowIndex+1)+"行异常类型无效(请参照 '异常订单'、'代客下单异常'、'团购刷单'、'下架异常订单')</span></br>");
                    	continue;
                    }
                    List<Map<String, Object>> typelist = dynamicDao.queryAbnormalType(type);
                    if(typelist==null||typelist.size()==0){
                    	infosb.append("<span>文件第"+(i+1)+"页"+"第"+(nRowIndex+1)+"行异常类型无效(请参照 '异常订单'、'代客下单异常'、'团购刷单'、'下架异常订单')</span></br>");
                    	continue;
                    }
                    if(type.length()>200){
                    	infosb.append("<span>文件第"+(i+1)+"页"+"第"+(nRowIndex+1)+"行异常类型数据长度应小于200</span></br>");
                    	continue;
                    }
                    
                    
                    DsAbnormalOrder dsAbnormalOrder = new DsAbnormalOrder();
                    dsAbnormalOrder.setAbnortype(type);
                    dsAbnormalOrder.setOrdersn(ordersn);
                    dsAbnormalOrder.setStoreno(storeno);
                    dsAbnormalOrder.setStorename(storename);
                    dsAbnormalOrder.setEshopname(eshopname);
                    dsAbnormalOrder.setDeptname(deptname);
                    dsAbnormalOrder.setChannelname(channelname);
                    dsAbnormalOrder.setTradingprice(Double.parseDouble(price));
                    if(payprice!=null&&!"".equals(payprice)){
                    	 dsAbnormalOrder.setPayableprice(Double.parseDouble(payprice));
                    }
                   
                    dsAbnormalOrder.setCreatetime(date);
                    dsAbnormalOrder.setUpdatetime(date);
                    dsAbnormalOrder.setStatus("0");
                    dsAbnormalOrder.setDatatype(dataType);
                    dsAbnormalOrder.setCityname(cityname);
                    dsAbnormalOrder.setSignedtime(sDateFormat.parse(signedtime));
                    dsAbnormalOrderManager.saveObject(dsAbnormalOrder);
                }
            }
            is_excel.close();
        }
		return infosb.toString();
	}


	private String getCellValue(Cell cell) {
		String value;
		if(cell == null){
			return null;
		}
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				value = cell.getRichStringCellValue().getString().trim();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				value = cell.getNumericCellValue() == 0D ? null : String.valueOf(cell.getNumericCellValue());
				if (cell.getCellStyle().getDataFormat() == 177||cell.getCellStyle().getDataFormat() == 58||cell.getCellStyle().getDataFormat() == 31){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date = org.apache.poi.ss.usermodel.DateUtil
							.getJavaDate(Double.valueOf(value));
					value = sdf.format(date);
					return value;
				}else if(DateUtil.isCellDateFormatted(cell)){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					return sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
				}
				if(value != null && value.contains("E")){
					value = new BigDecimal(value).toPlainString();
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				value = String.valueOf(cell.getBooleanCellValue()).trim();
				break;
			case Cell.CELL_TYPE_FORMULA:
				value = cell.getCellFormula();
				break;
			default:
				value = "";
		}
		return value;
	}


    /**
     *
     * 门店用户画像统计   统计：6字段 18字段
     * @param dynamicDto
     * @param pageInfo
     * @return Map<String, Object>
     */
    @Override
    public Map<String,Object> customerPortraitCount(DynamicDto dynamicDto, PageInfo pageInfo) {
        Map<String, Object> result = new HashMap<String,Object>();
        if("prev_month".equals(dynamicDto.getBeginDate())||"cur_month".equals(dynamicDto.getBeginDate())){
            result = this.queryData(dynamicDto,CUSTOMERPROTITE_SIXFIELD , pageInfo , null);
		}else if("cur_day".equals(dynamicDto.getBeginDate())){//当天
			//18字段
            result = this.queryData(dynamicDto,CUSTOMERPROTITE_SIXFIELD, pageInfo , null);
        }
        return result;
    }



    /**
     * 门店拜访记录表格明细
     * @param dynamicDto
     * @param pageInfo
     * @return
     */
    @Override
    public Map<String, Object> visitRecodeFormDetail(DynamicDto dynamicDto, PageInfo pageInfo) {
        Map<String, Object> result = new HashMap<String,Object>();
        if("prev_month".equals(dynamicDto.getBeginDate())||"cur_month".equals(dynamicDto.getBeginDate())){
            result = this.queryDataOfList(dynamicDto , VISITRECORD_FORMDETAIL , pageInfo);
        }else if("cur_day".equals(dynamicDto.getBeginDate())){//当天
            result = this.queryDataOfList(dynamicDto , VISITRECORD_FORMDETAIL , pageInfo);
        }
        return result;
    }

    /**
     * 导出拜访记录
     * @param dynamicDto
     * @return
     */
    @Override
    public Map<String, Object> exportVisitRecode(DynamicDto dynamicDto) {
        Map<String,Object> result  = new HashMap<String,Object>();
        Map<String,Object> map  = this.visitRecodeFormDetail(dynamicDto, null);
        if("2000000".equals(map.get("code"))){//成功返回数据
            JSONObject jObject = new JSONObject(map.get("data").toString());
            JSONArray storeTrade = jObject.getJSONArray("data");
            String str_file_dir_path = PropertiesUtil.getValue("file.root");
            String str_web_path = PropertiesUtil.getValue("file.web.root");
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            setCellStyle_common(wb);
            setHeaderStyle(wb);
            HSSFSheet sheet = wb.createSheet("拜访记录");
            HSSFRow row = sheet.createRow(0);
            String[] str_headers = {"门店名称","员工编号","员工姓名","拜访次数"};
            String[] headers_key = {"storename","employeeno","username","num"};
            for(int i = 0;i < str_headers.length;i++){
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(getHeaderStyle());
                cell.setCellValue(new HSSFRichTextString(str_headers[i]));
            }
            for(int i = 0;i < storeTrade.length();i++){
                row = sheet.createRow(i+1);
                for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
                    setCellValue(row, cellIndex, storeTrade.getJSONObject(i).get(headers_key[cellIndex]));
                }
            }
            File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"visitRecode.xls");
            if(file_xls.exists()){
                file_xls.delete();
            }
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file_xls.getAbsoluteFile());
                wb.write(os);
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(os != null){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            result.put("message","导出成功！");
            result.put("status","success");
            result.put("data", str_web_path.concat(file_xls.getName()));
        }else{
            result.put("message","请重新操作！");
            result.put("status","fail");
        }
        return result;
    }


    /**
     * 客户画像导出
     * @param dynamicDto
     * @return
     */
    @Override
    public Map<String, Object> exportCustomerPortrait(DynamicDto dynamicDto) {
        Map<String,Object> result  = new HashMap<String,Object>();
        Map<String,Object> map  = this.customerPortraitCount(dynamicDto, null);
        if("2000000".equals(map.get("code"))){//成功返回数据
            JSONObject jObject = new JSONObject(map.get("data").toString());
            JSONArray storeTrade = jObject.getJSONArray("data");
            String str_file_dir_path = PropertiesUtil.getValue("file.root");
            String str_web_path = PropertiesUtil.getValue("file.web.root");
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            setCellStyle_common(wb);
            setHeaderStyle(wb);
            HSSFSheet sheet = wb.createSheet("用户画像");
            HSSFRow row = sheet.createRow(0);
            String[] str_headers = {"门店名称","员工编号","员工姓名","用户数量(18字段)","用户数量(6字段)"};
            String[] headers_key = {"storename","employeeno","username","datacount","cus1count"};
            for(int i = 0;i < str_headers.length;i++){
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(getHeaderStyle());
                cell.setCellValue(new HSSFRichTextString(str_headers[i]));
            }
            for(int i = 0;i < storeTrade.length();i++){
                row = sheet.createRow(i+1);
                for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
                    setCellValue(row, cellIndex, storeTrade.getJSONObject(i).get(headers_key[cellIndex]));
                }
            }
            File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"visitRecode.xls");
            if(file_xls.exists()){
                file_xls.delete();
            }
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file_xls.getAbsoluteFile());
                wb.write(os);
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(os != null){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            result.put("message","导出成功！");
            result.put("status","success");
            result.put("data", str_web_path.concat(file_xls.getName()));
        }else{
            result.put("message","请重新操作！");
            result.put("status","fail");
        }
        return result;
    }

	/**
	 * 通过时间查询快递信息
	 * @param dynamicDto
	 * @return Map<String,Object>
	 */
	@Override
	public List<Map<String , Object>> queryExpressByYearMonth(DynamicDto dynamicDto,PageInfo pageInfo) {
		ExpressDao expressDao = (ExpressDao) SpringHelper.getBean(ExpressDao.class.getName());
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		String storeno="";
		List<Map<String , Object>>  last_result = new ArrayList<Map<String , Object>>();
		List<Map<String , Object>>  result = new ArrayList<Map<String , Object>>();
		if(dynamicDto.getTarget()==0){//总部
			
			List<Map<String,Object>> storeList = expressDao.queryExpressByCityId(dynamicDto.getCityId());
					
			String cityName = "";
			for(int i = 0 ; i < storeList.size() ; i++ ){
				Map<String,Object> map = storeList.get(i);
				cityName = map.get("cityname").toString();
			}
			if(dynamicDto.getStoreId()!=null){
				if(dynamicDto.getStoreId()==-10000){
					return null;
				}else{
					Store store = storeManager.findStore(dynamicDto.getStoreId());
					if(store!=null){
						storeno = store.getStoreno();
					}
				}
			}
			if( dynamicDto.getBeginDate() != null ){
				if(dynamicDto.getBeginDate().equals("prev_month")){// 总部上个月
					String currentDate = this.getPrevDate();
					result = expressDao.queryExpressCount(currentDate);
					for(int i = 0 ; i < result.size() ; i++ ){
						Map<String,Object> map = result.get(i);
						String exit_cityName = map.get("cityName").toString();
						if(dynamicDto.getStoreId()!=null){//总部某一个门店
							if(storeno!=null&&!"".equals(storeno)&&storeno.equals(map.get("storeNo"))){
								last_result.add(map);
							}
						}else{//总部全部门店
							
							
							if(dynamicDto.getCityId()!=null&&dynamicDto.getCityId()!=-10000){
								if(cityName.equals(exit_cityName)){
									last_result.add(map);
								}
							}else{
								last_result.add(map);
							}
						}
						
					}
				}else if(dynamicDto.getBeginDate().equals("cur_month")){ //城市总监当前月
					String preDate = this.getCurDate();
					result = expressDao.queryExpressCount(preDate);
					for(int j = 0 ; j < result.size() ; j++ ){
						Map<String,Object> map = result.get(j);
						String exit_cityName = map.get("cityName").toString();
						if(dynamicDto.getStoreId()!=null){//总部某一个门店
							if(storeno!=null&&!"".equals(storeno)&&storeno.equals(map.get("storeNo"))){
								last_result.add(map);
							}
						}else{//总部全部门店
							if(dynamicDto.getCityId()!=null&&dynamicDto.getCityId()!=-10000){
								if(cityName.equals(exit_cityName)){
									last_result.add(map);
								}
							}else{
								last_result.add(map);
							}
						}
					}
				}
			}
		}else if( dynamicDto.getTarget() == 1 ){  //城市总监级别
				List<Map<String,Object>> storeList = expressDao.
						queryExpressByCityId(dynamicDto.getCityId());
				String cityName = null;
				for(int i = 0 ; i < storeList.size() ; i++ ){
					Map<String,Object> map = storeList.get(i);
					cityName = map.get("cityname").toString();
				}
				if(dynamicDto.getStoreId()!=null){
					if(dynamicDto.getStoreId()==-10000){
						return null;
					}else{
						Store store = storeManager.findStore(dynamicDto.getStoreId());
						if(store!=null){
							storeno = store.getStoreno();
						}
					}
				}
				if( dynamicDto.getBeginDate() != null ){
					if(dynamicDto.getBeginDate().equals("prev_month")){// 城市总监上个月
						String currentDate = this.getPrevDate();
						result = expressDao.queryExpressCount(currentDate);
						for(int i = 0 ; i < result.size() ; i++ ){
							Map<String,Object> map = result.get(i);
							String exit_cityName = map.get("cityName").toString();
							if(dynamicDto.getStoreId()!=null){//城市某一个门店
								if(cityName.equals(exit_cityName)&&storeno!=null&&!"".equals(storeno)&&storeno.equals(map.get("storeNo"))){
									last_result.add(map);
								}
							}else{//城市全部门店
								if(cityName.equals(exit_cityName)){
									last_result.add(map);
								}
							}
							
						}
					}else if(dynamicDto.getBeginDate().equals("cur_month")){ //城市总监当前月
						String preDate = this.getCurDate();
						result = expressDao.queryExpressCount(preDate);
						for(int j = 0 ; j < result.size() ; j++ ){
							Map<String,Object> map = result.get(j);
							String exit_cityName = map.get("cityName").toString();
							if(dynamicDto.getStoreId()!=null){//城市某一个门店
								if(cityName.equals(exit_cityName)&&storeno!=null&&!"".equals(storeno)&&storeno.equals(map.get("storeNo"))){
									last_result.add(map);
								}
							}else{//城市全部门店
								if(cityName.equals(exit_cityName)){
									last_result.add(map);
								}
							}
						}
					}
				}
		}else{   //店长级别
			if( dynamicDto.getBeginDate() != null ){
				if(dynamicDto.getBeginDate().equals("prev_month")){ //店长上个月
					Long storeId = dynamicDto.getStoreId();
					String currentDate = this.getPrevDate();
//					result = expressDao.queryExpressCount_cityLevel(currentDate);
//					result = expressDao.queryExpressCount(currentDate);
					//店长 -- 通过门店主键当前月日期
					last_result = expressDao.queryExpressInfoByStoreIdAndMonth(storeId,currentDate);
					int size = result.size();
					for( int x = 0 ; x < result.size() ; x++ ){
						Map<String,Object> map = result.get(x);
						//分页使用此datacount来分页
						map.put("datacount",size);
					}
				}else if(dynamicDto.getBeginDate().equals("cur_month")){ //店长当前月
					Long storeId = dynamicDto.getStoreId();
					String preDate = this.getCurDate();
					//店长 -- 通过门店主键上个月日期
					last_result = expressDao.queryExpressInfoByStoreIdAndMonth(storeId,preDate);
					int size = result.size();
					for( int x = 0 ; x < result.size() ; x++ ){
						Map<String,Object> map = result.get(x);
						//分页使用此datacount来分页
						map.put("datacount",size);
					}
				}
			}
		}
		return last_result;
	}

	
	
	

	/**
	 *  含有客户画像的 6字段 18字段
	 * @param dynamicDto
	 * @param url
	 * @param pageInfo
	 * @return
	 */
	private   Map<String, Object> queryData(DynamicDto dynamicDto,String url,PageInfo pageInfo,String grade) {
		String beginData = "";
		String endDate = "";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> result = new HashMap<String,Object>();
		HttpClientUtil hClientUtil = null;
		JSONObject param =   new JSONObject();
		if(dynamicDto.getTarget()==0){//总部
			if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店
				StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
				List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "ZB");//获取门店
				StringBuilder storeNumber = new StringBuilder();
				StringBuilder storeNO = new StringBuilder();
				for(Map<String, Object> map:storeList){
					if(map.get("number")==null||"".equals(map.get("number"))){
						continue;
					}
					if(map.get("storeno")==null||"".equals(map.get("storeno"))){
						continue;
					}
					storeNumber.append(","+map.get("number"));
					storeNO.append(",'"+map.get("storeno")+"'");
				}
				if(storeNumber.length()>0&&storeNO.length()>0){
					storeNumber = storeNumber.deleteCharAt(0);
					storeNO = storeNO.deleteCharAt(0);

				}else{
					JSONObject temp = new JSONObject();
					temp.put("data", "");
					temp.put("message", "系统查无此条件的数据！");
					result.put("status","storefail");
					result.put("data",temp.toString());
					return result;
				}
				dynamicDto.setStoreNumer(storeNumber.toString());
				dynamicDto.setStoreNo(storeNO.toString());
			}else  if(dynamicDto.getStoreId()==-10000){
				dynamicDto.setStoreNumer("-10000");
				dynamicDto.setStoreNo("-10000");
			}else{
				StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
				Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
				dynamicDto.setStoreNumer(store.getNumber()==null?"-10000":String.valueOf(store.getNumber()));
				dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
			}
		}else if(dynamicDto.getTarget()==1){//城市总监
			if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店
				StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
				List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "CSZJ");//获取门店
				StringBuilder storeNumber = new StringBuilder();
				StringBuilder storeNO = new StringBuilder();
				for(Map<String, Object> map:storeList){
					if(map.get("number")==null||"".equals(map.get("number"))){
						continue;
					}
					if(map.get("storeno")==null||"".equals(map.get("storeno"))){
						continue;
					}
					storeNumber.append(","+map.get("number"));
					storeNO.append(",'"+map.get("storeno")+"'");
				}
				if(storeNumber.length()>0&&storeNO.length()>0){
					storeNumber = storeNumber.deleteCharAt(0);
					storeNO = storeNO.deleteCharAt(0);

				}else{
					JSONObject temp = new JSONObject();
					temp.put("data", "");
					temp.put("message", "系统查无此条件的数据！");
					result.put("status","storefail");
					result.put("data",temp.toString());
					return result;
				}
				dynamicDto.setStoreNumer(storeNumber.toString());
				dynamicDto.setStoreNo(storeNO.toString());
			}else  if(dynamicDto.getStoreId()==-10000){
				dynamicDto.setStoreNumer("-10000");
				dynamicDto.setStoreNo("-10000");
			}else{
				StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
				Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
				dynamicDto.setStoreNumer(store.getNumber()==null?"-10000":String.valueOf(store.getNumber()));
				dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
			}
		}else if(dynamicDto.getTarget()==2){//店长
			StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
			Store store = (Store)storeManager.getObject(dynamicDto.getStoreId());
			dynamicDto.setStoreNumer(store.getNumber()==null?"-10000":String.valueOf(store.getNumber()));
			dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno())+"'");
		}
		Calendar calendar = Calendar.getInstance();
		System.out.println(dynamicDto.getBeginDate());
		if("prev_month".equals(dynamicDto.getBeginDate())){
			calendar.add(Calendar.MONTH, -1);
			param.put("app_key", "1002");
			//String times = DateUtil2.theLast();
			String yearmonth = this.getPrevDate();
			param.put("yearmonth", yearmonth);
			param.put("year", String.valueOf(calendar.get(Calendar.YEAR)));
			param.put("month",String.valueOf(calendar.get(Calendar.MONTH)+1));
			param.put("stamp",System.currentTimeMillis());
			param.put("nonce",UUID.randomUUID().toString());
			param.put("grade", "0");
			param.put("storeids", dynamicDto.getStoreNo());
		}else if("cur_month".equals(dynamicDto.getBeginDate())){
			param.put("app_key", "1002");
			//String time = DateUtil2.curDate();
			String yearmonth = this.getCurDate();
			param.put("yearmonth", yearmonth);
			param.put("year", String.valueOf(calendar.get(Calendar.YEAR)));
			param.put("month",String.valueOf(calendar.get(Calendar.MONTH)+1));
			param.put("stamp",System.currentTimeMillis());
			param.put("nonce",UUID.randomUUID().toString());
			param.put("grade", "0");
			param.put("storeids", dynamicDto.getStoreNo());
		} if("cur_day".equals(dynamicDto.getBeginDate())){//当天
			beginData = sdf.format(new Date());
			endDate = sdf.format(new Date());
			if(pageInfo!=null){
				pageInfo.setRecordsPerPage(10);
				Integer firstRecordIndex = pageInfo.getRecordsPerPage()* (pageInfo.getCurrentPage() - 1);//每页起始行数
				param.put("limitcond",firstRecordIndex+","+pageInfo.getRecordsPerPage());
			}
			param.put("storeids", dynamicDto.getStoreNumer());
			param.put("app_key", "1002");
			param.put("stamp",System.currentTimeMillis());
			param.put("nonce",UUID.randomUUID().toString());
			param.put("begindate", beginData);
			param.put("enddate", endDate);
		}
		String sign = EncryptUtils.getMD5(param.toString()+SECRET);
		url = url+"?sign="+sign;
		hClientUtil = new HttpClientUtil();
		String data = hClientUtil.getRemoteData(url, param);
		JSONObject jObject = new JSONObject(data);
		if("2000000".equals(jObject.get("status"))){
			Object datacount = jObject.get("datacount")==null?0:jObject.get("datacount");
			if(pageInfo!=null&&"cur_day".equals(dynamicDto.getBeginDate())){
				pageInfo.setTotalRecords(Integer.parseInt(datacount.toString()));
				result.put("totalPage", (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1);
			}else{
				result.put("totalPage",1);
			}
			result.put("code","2000000");
		}
		result.put("status","success");
		result.put("data",data);
		return result;
	}



	
	@Override
	public List<Map<String, Object>> getCityByUser(Integer target,Long userid,String name) {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		List<Map<String, Object>> list  = dynamicDao.queryCityByUser(target,userid, name);
		return list;
	}





	
	@Override
	public Map<String, Object> queryStoreTradeOfDept(DynamicDto dynamicDto) {
		Calendar calendar = Calendar.getInstance();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Map<String, Object> result = new HashedMap();
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		
		
		if(dynamicDto.getTarget()==1){//城市总监
			if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店

				StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
				List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "CSZJ");//获取门店
				StringBuilder storeNumber = new StringBuilder();
				StringBuilder storeNO = new StringBuilder();
				for(Map<String, Object> map:storeList){
					
					if(map.get("storeno")==null||"".equals(map.get("storeno"))){
						continue;
					}
					
					storeNO.append(",'"+map.get("storeno")+"'");
				}
				if(storeNO.length()>0){
					storeNO = storeNO.deleteCharAt(0);

				}else{
					JSONObject temp = new JSONObject();
					temp.put("data", "");
					temp.put("message", "系统查无此条件的数据！");
					result.put("status","storefail");
					result.put("data",temp.toString());
					return result;
				}

				
				dynamicDto.setStoreNo(storeNO.toString());
			}else  if(dynamicDto.getStoreId()==-10000){
				
				dynamicDto.setStoreNo("-10000");
			}else{
				Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
				dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
			}
		}else if(dynamicDto.getTarget()==2){//店长
			Store store = (Store)storeManager.getObject(dynamicDto.getStoreId());
			dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno())+"'");
		}else if(dynamicDto.getTarget()==0){//总部
			if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店

				StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
				List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "ZB");//获取门店
				StringBuilder storeNumber = new StringBuilder();
				StringBuilder storeNO = new StringBuilder();
				for(Map<String, Object> map:storeList){
					
					if(map.get("storeno")==null||"".equals(map.get("storeno"))){
						continue;
					}
					
					storeNO.append(",'"+map.get("storeno")+"'");
				}
				if(storeNO.length()>0){
					storeNO = storeNO.deleteCharAt(0);

				}else{
					JSONObject temp = new JSONObject();
					temp.put("data", "");
					temp.put("message", "系统查无此条件的数据！");
					result.put("status","storefail");
					result.put("data",temp.toString());
					return result;
				}

				
				dynamicDto.setStoreNo(storeNO.toString());
			}else  if(dynamicDto.getStoreId()==-10000){
				dynamicDto.setStoreNo("-10000");
			}else{
				Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
				dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
			}
		}
		
		
			
			if("prev_month".equals(dynamicDto.getBeginDate())){//上月
				
				calendar.add(Calendar.MONTH, -1);
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH )+1;
				dynamicDto.setYear(year);
				dynamicDto.setMonth(month);
				
			}else if("cur_month".equals(dynamicDto.getBeginDate())){//当月
				
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH )+1;
				dynamicDto.setYear(year);
				dynamicDto.setMonth(month);
				
			}
			
			try {
				List<Map<String, Object>> list = dynamicDao.queryStoreTradeOfDept(dynamicDto);
				result.put("status","success");
				result.put("data",list);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("status","fail");
				result.put("data",null);
			}
		
			return result;
	}



	@Override
	public Map<String, Object> exportStoreTradeOfDept(DynamicDto dynamicDto) {
		Calendar calendar = Calendar.getInstance();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Map<String, Object> result = new HashedMap();
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		
		
		if(dynamicDto.getTarget()==1){//城市总监
			if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店

				StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
				List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "CSZJ");//获取门店
				StringBuilder storeNumber = new StringBuilder();
				StringBuilder storeNO = new StringBuilder();
				for(Map<String, Object> map:storeList){
					
					if(map.get("storeno")==null||"".equals(map.get("storeno"))){
						continue;
					}
					
					storeNO.append(",'"+map.get("storeno")+"'");
				}
				if(storeNO.length()>0){
					storeNO = storeNO.deleteCharAt(0);

				}else{
					JSONObject temp = new JSONObject();
					temp.put("data", "");
					temp.put("message", "系统查无此条件的数据！");
					result.put("status","storefail");
					result.put("data",temp.toString());
					return result;
				}

				
				dynamicDto.setStoreNo(storeNO.toString());
			}else  if(dynamicDto.getStoreId()==-10000){
				
				dynamicDto.setStoreNo("-10000");
			}else{
				Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
				dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
			}
		}else if(dynamicDto.getTarget()==2){//店长
			Store store = (Store)storeManager.getObject(dynamicDto.getStoreId());
			dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno())+"'");
		}else if(dynamicDto.getTarget()==0){//总部
			if(dynamicDto.getStoreId()!=null){
				Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
				dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
			}else{
				StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
				List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "ZB");//总部获取门店
				StringBuilder storeNumber = new StringBuilder();
				StringBuilder storeNO = new StringBuilder();
				for(Map<String, Object> map:storeList){
					
					if(map.get("storeno")==null||"".equals(map.get("storeno"))){
						continue;
					}
					
					storeNO.append(",'"+map.get("storeno")+"'");
				}
				if(storeNO.length()>0){
					storeNO = storeNO.deleteCharAt(0);

				}else{
					JSONObject temp = new JSONObject();
					temp.put("data", "");
					temp.put("message", "系统查无此条件的数据！");
					result.put("status","storefail");
					result.put("data",temp.toString());
					return result;
				}

				
				dynamicDto.setStoreNo(storeNO.toString());
			}
		}
		
		
			
			if("prev_month".equals(dynamicDto.getBeginDate())){//上月
				
				calendar.add(Calendar.MONTH, -1);
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH )+1;
				dynamicDto.setYear(year);
				dynamicDto.setMonth(month);
				
			}else if("cur_month".equals(dynamicDto.getBeginDate())){//当月
				
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH )+1;
				dynamicDto.setYear(year);
				dynamicDto.setMonth(month);
				
			}
			
			List<Map<String, Object>> list = dynamicDao.queryStoreTradeOfDept(dynamicDto);
			if(list==null||list.size()==0){
	
				result.put("status","fail");
				return result;
			}
			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");
	
			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab
	
			setCellStyle_common(wb);
			setHeaderStyle(wb);
			HSSFSheet sheet = wb.createSheet("事业部");
			HSSFRow row = sheet.createRow(0);
			String[] str_headers = {"门店名称","门店编号","事业部","服务专员","员工编号","交易额"};
			String[] headers_key = {"storename","storeno","career_group","name","employee_no","gmv"};
			for(int i = 0;i < str_headers.length;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(getHeaderStyle());
				cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			}
	
			for(int i = 0;i < list.size();i++){
				row = sheet.createRow(i+1);
				for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
				}
			}
	
	
	
			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_dept_gmv.xls");
			if(file_xls.exists()){
				file_xls.delete();
			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file_xls.getAbsoluteFile());
				wb.write(os);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	
			result.put("message","导出成功！");
			result.put("status","success");
			result.put("data", str_web_path.concat(file_xls.getName()));
			return  result;
	}
   
	
	@Override
	public Map<String, Object> employeeOfAreaGmv(DynamicDto dynamicDto, PageInfo pageInfo) {
		Map<String, Object> result = new HashedMap();
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		try {
			if(dynamicDto.getTarget()==0){//总部
				if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店

					StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
					List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "ZB");//获取门店
					
					StringBuilder storeNO = new StringBuilder();
					for(Map<String, Object> map:storeList){
						
						if(map.get("storeno")==null||"".equals(map.get("storeno"))){
							continue;
						}
						
						storeNO.append(",'"+map.get("storeno")+"'");
					}
					if(storeNO.length()>0){
					
						storeNO = storeNO.deleteCharAt(0);

					}else{
						JSONObject temp = new JSONObject();
						temp.put("data", "");
						temp.put("message", "系统查无此条件的数据！");
						result.put("status","storefail");
						result.put("data",temp.toString());
						return result;
					}

					
					dynamicDto.setStoreNo(storeNO.toString());
				}else  if(dynamicDto.getStoreId()==-10000){
					dynamicDto.setStoreNo("-10000");
				}else{
					
					Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
					dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
				}
			}else if(dynamicDto.getTarget()==1){//城市总监
				if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店

					StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
					List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "CSZJ");//获取门店
					
					StringBuilder storeNO = new StringBuilder();
					for(Map<String, Object> map:storeList){
						
						if(map.get("storeno")==null||"".equals(map.get("storeno"))){
							continue;
						}
						
						storeNO.append(",'"+map.get("storeno")+"'");
					}
					if(storeNO.length()>0){
						
						storeNO = storeNO.deleteCharAt(0);

					}else{
						JSONObject temp = new JSONObject();
						temp.put("data", "");
						temp.put("message", "系统查无此条件的数据！");
						result.put("status","storefail");
						result.put("data",temp.toString());
						return result;
					}

					dynamicDto.setStoreNo(storeNO.toString());
				}else  if(dynamicDto.getStoreId()==-10000){
					dynamicDto.setStoreNumer("-10000");
					dynamicDto.setStoreNo("-10000");
				}else{
					
					Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
					dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
				}
			}else if(dynamicDto.getTarget()==2){//店长
				Store store = (Store)storeManager.getObject(dynamicDto.getStoreId());
				dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno())+"'");
			}

			Calendar calendar = Calendar.getInstance();
			System.out.println(dynamicDto.getBeginDate());
			if("prev_month".equals(dynamicDto.getBeginDate())){
	            calendar.add(Calendar.MONTH, -1);
	            dynamicDto.setYear(calendar.get(Calendar.YEAR));
	            dynamicDto.setMonth(calendar.get(Calendar.MONTH)+1);
			}else if("cur_month".equals(dynamicDto.getBeginDate())){
	            dynamicDto.setYear(calendar.get(Calendar.YEAR));
	            dynamicDto.setMonth(calendar.get(Calendar.MONTH)+1);
			}
			
			result= dynamicDao.employeeOfAreaGmv(dynamicDto, pageInfo);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
			return result;
			
		}
		return result;
	}





	/**
	 * TODO 拉新GMV(国安侠)
	 * 2017年10月19日
	 * @title : newAddCustomerGMV
	 * @author 15149006102@139.com
	 * @param dynamicDto , pageInfo
	 * @return Map<String,Object>
	 */
	@Override
	public Map<String, Object> newAddCustomerGMV(DynamicDto dynamicDto, PageInfo pageInfo) {
		Map<String, Object> result = new HashedMap();
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		try {
			if(dynamicDto.getTarget()==0){//总部
				if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店

					StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
					List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "ZB");//获取门店
					
					StringBuilder storeNO = new StringBuilder();
					StringBuilder storeIds = new StringBuilder();
					for(Map<String, Object> map:storeList){
						
						if(map.get("storeno")==null||"".equals(map.get("storeno"))){
							continue;
						}
						storeIds.append(","+map.get("store_id"));
						storeNO.append(",'"+map.get("storeno")+"'");
					}
					if(storeNO.length()>0){
					
						storeNO = storeNO.deleteCharAt(0);
						storeIds = storeIds.deleteCharAt(0);

					}else{
						JSONObject temp = new JSONObject();
						temp.put("data", "");
						temp.put("message", "系统查无此条件的数据！");
						result.put("status","storefail");
						result.put("data",temp.toString());
						return result;
					}

					
					dynamicDto.setStoreNo(storeNO.toString());
					dynamicDto.setStoreIds(storeIds.toString());
				}else  if(dynamicDto.getStoreId()==-10000){
					dynamicDto.setStoreNo("-10000");
					dynamicDto.setStoreIds("-10000");
				}else{
					
					Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
					dynamicDto.setStoreIds(store.getStore_id().toString());
					dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
				}
			}else if(dynamicDto.getTarget()==1){//城市总监
				if(dynamicDto.getStoreId()==null||"".equals(dynamicDto.getStoreId())){//查询所有城市的门店

					StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
					List<Map<String,Object>> storeList = storeDao.getAllStoreOfCRM(dynamicDto.getEmployeeId(), dynamicDto.getCityId(), "CSZJ");//获取门店
					
					StringBuilder storeNO = new StringBuilder();
					StringBuilder storeIds = new StringBuilder();
					for(Map<String, Object> map:storeList){
						
						if(map.get("storeno")==null||"".equals(map.get("storeno"))){
							continue;
						}
						storeIds.append(","+map.get("store_id"));
						storeNO.append(",'"+map.get("storeno")+"'");
					}
					if(storeNO.length()>0){
						
						storeNO = storeNO.deleteCharAt(0);
						storeIds = storeIds.deleteCharAt(0);

					}else{
						JSONObject temp = new JSONObject();
						temp.put("data", "");
						temp.put("message", "系统查无此条件的数据！");
						result.put("status","storefail");
						result.put("data",temp.toString());
						return result;
					}

					dynamicDto.setStoreNo(storeNO.toString());
					dynamicDto.setStoreIds(storeIds.toString());
				}else  if(dynamicDto.getStoreId()==-10000){
					dynamicDto.setStoreIds("-10000");
					dynamicDto.setStoreNo("-10000");
				}else{
					
					Store  store = (Store)storeManager.getObject(dynamicDto.getStoreId());
					dynamicDto.setStoreIds(store.getStore_id().toString());
					dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno()==null?"-10000":store.getStoreno())+"'");
				}
			}else if(dynamicDto.getTarget()==2){//店长
				Store store = (Store)storeManager.getObject(dynamicDto.getStoreId());
				dynamicDto.setStoreNo("'"+String.valueOf(store.getStoreno())+"'");
				dynamicDto.setStoreIds(store.getStore_id().toString());
			}

			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if("prev_month".equals(dynamicDto.getBeginDate())){
				Calendar calendar1 = Calendar.getInstance();
	            calendar1.add(Calendar.MONTH, -1);
	           
	            calendar1.set(Calendar.DAY_OF_MONTH,1);
	            String firstDay = sdf.format(calendar1.getTime());
	            //获取前一个月最后一天
	            Calendar calendar2 = Calendar.getInstance();
	            calendar2.set(Calendar.DAY_OF_MONTH, 0);
	            String lastDay = sdf.format(calendar2.getTime());
	            dynamicDto.setBeginDate(firstDay);
	            dynamicDto.setEndDate(lastDay);
	            dynamicDto.setSearchstr("pre");//暂时表示上个月
	            dynamicDto.setYear(calendar1.get(Calendar.YEAR));
	            dynamicDto.setMonth(calendar1.get(Calendar.MONTH)+1);
			}else if("cur_month".equals(dynamicDto.getBeginDate())){
				Calendar c = Calendar.getInstance();    
		        c.add(Calendar.MONTH, 0);
		        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		        String first = sdf.format(c.getTime());
			        
		        //获取当前月最后一天
		        Calendar ca = Calendar.getInstance();    
		        //ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
		        String last = sdf.format(ca.getTime());
		        
		        dynamicDto.setBeginDate(first);
	            dynamicDto.setEndDate(last);
	            dynamicDto.setSearchstr("cur");
			}
			
			result= dynamicDao.employeeOfAreaNewaddcus(dynamicDto, pageInfo);
			result.put("status","success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status","fail");
			return result;
			
		}
		return result;
	}



	/**
	 * TODO 重点产品GMV
	 * @Date: 2017年10月19日
	 * @title : emphasesProductsGMV
	 * @author 15149006102@139.com
	 * @param dynamicDto , pageInfo
	 * @return Map<String,Object>
	 */
	@Override
	public Map<String, Object> emphasesProductsGMV(DynamicDto dynamicDto, PageInfo pageInfo) {
		Map<String, Object> result = new HashMap<String,Object>();
		if("prev_month".equals(dynamicDto.getBeginDate())||"cur_month".equals(dynamicDto.getBeginDate())){
			result = this.queryData(dynamicDto,EMPHASSESPRODUCTSGMV_FORMDETAIL , pageInfo , null);
		}else if("cur_day".equals(dynamicDto.getBeginDate())){//当天
			//18字段
			result = this.queryData(dynamicDto,EMPHASSESPRODUCTSGMV_FORMDETAIL, pageInfo , null);
		}
		return result;
	}


	/**
	 * TODO 国安侠（片区）GMV导出
	 * @Date: 2017年10月19日
	 * @param dynamicDto
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> exportAreaGMV(DynamicDto dynamicDto) {
		Map<String,Object> result  = new HashMap<String,Object>();
		Map<String,Object> map  = this.employeeOfAreaGmv(dynamicDto, null);
		if("success".equals(map.get("status"))){//成功返回数据
			List<Map<String, Object>> list  = (List<Map<String, Object>>)map.get("gmv");
			if(list==null||list.size()==0){
				result.put("message","没有符合条件的数据！");
				result.put("status","null");
				return result;
			}
			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");
			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab
			setCellStyle_common(wb);
			setHeaderStyle(wb);
			HSSFSheet sheet = wb.createSheet("国安侠GMV");
			HSSFRow row = sheet.createRow(0);
			String[] str_headers = {"城市","门店名称","门店编码","员工姓名","员工编号","绩效GMV","服务GMV","片区GMV","手动分配GMV","人均分配GMV"};
			String[] headers_key = {"city_name","store_name","storeno","employee_name","employee_no","pesgmv","pes_sendgmv","pes_areagmv","pes_assigngmv","pes_pergmv"};
			for(int i = 0;i < str_headers.length;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(getHeaderStyle());
				cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			}
			for(int i = 0;i < list.size();i++){
				row = sheet.createRow(i+1);
				for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
				}
			}
			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"areaGMV.xls");
			if(file_xls.exists()){
				file_xls.delete();
			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file_xls.getAbsoluteFile());
				wb.write(os);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			result.put("message","导出成功！");
			result.put("status","success");
			result.put("data", str_web_path.concat(file_xls.getName()));
		}else{
			result.put("message","请重新操作！");
			result.put("status","fail");
		}
		return result;
	}


	/**
	 * TODO 拉新GMV导出
	 * @param dynamicDto
	 * @return
	 */
	@Override
	public Map<String, Object> exportNewAddCustomerGMV(DynamicDto dynamicDto) {
		Map<String,Object> result  = new HashMap<String,Object>();
		Map<String,Object> map  = this.newAddCustomerGMV(dynamicDto, null);
		if("success".equals(map.get("status"))){//成功返回数据
			List<Map<String, Object>> list  = (List<Map<String, Object>>)map.get("gmv");
			if(list==null||list.size()==0){
				result.put("message","没有符合条件的数据！");
				result.put("status","null");
				return result;
			}
			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");
			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab
			setCellStyle_common(wb);
			setHeaderStyle(wb);
			HSSFSheet sheet = wb.createSheet("国安侠用户");
			HSSFRow row = sheet.createRow(0);
			String[] str_headers = {"城市","门店名称","门店编号","员工姓名","员工编号","拉新用户超10元","消费用户"};
			String[] headers_key = {"store_city_name","store_name","store_code","name","info_employee_a_no","new_10_count","total"};
			for(int i = 0;i < str_headers.length;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(getHeaderStyle());
				cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			}
			for(int i = 0;i <list.size();i++){
				row = sheet.createRow(i+1);
				for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
				}
			}
			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"employee_newAddCus.xls");
			if(file_xls.exists()){
				file_xls.delete();
			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file_xls.getAbsoluteFile());
				wb.write(os);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			result.put("message","导出成功！");
			result.put("status","success");
			result.put("data", str_web_path.concat(file_xls.getName()));
		}else{
			result.put("message","请重新操作！");
			result.put("status","fail");
		}
		return result;
	}


	/**
	 * TODO 重点产品GMV 导出
	 * @param dynamicDto
	 * @return
	 */
	@Override
	public Map<String, Object> exportEmphasesProductsGMV(DynamicDto dynamicDto) {
		Map<String,Object> result  = new HashMap<String,Object>();
		Map<String,Object> map  = this.emphasesProductsGMV(dynamicDto, null);
		if("2000000".equals(map.get("code"))){//成功返回数据
			JSONObject jObject = new JSONObject(map.get("data").toString());
			JSONArray storeTrade = jObject.getJSONArray("data");
			String str_file_dir_path = PropertiesUtil.getValue("file.root");
			String str_web_path = PropertiesUtil.getValue("file.web.root");
			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab
			setCellStyle_common(wb);
			setHeaderStyle(wb);
			HSSFSheet sheet = wb.createSheet("片区内重点产品GMV");
			HSSFRow row = sheet.createRow(0);
			String[] str_headers = {"城市","门店名称","门店编码","员工编号","员工名称","重点产品绩效GMV","重点产品片区GMV","重点产品公海分配GMV","重点产品公海人均GMV"};
			String[] headers_key = {"city_name","store_name","storeno","employee_a_no","employee_a_name","totalsum","pesgmv","assign_gmv","pubseas"};
			for(int i = 0;i < str_headers.length;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(getHeaderStyle());
				cell.setCellValue(new HSSFRichTextString(str_headers[i]));
			}
			for(int i = 0;i < storeTrade.length();i++){
				row = sheet.createRow(i+1);
				for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
					setCellValue(row, cellIndex, storeTrade.getJSONObject(i).get(headers_key[cellIndex]));
				}
			}
			File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"emphasesProductsGMV.xls");
			if(file_xls.exists()){
				file_xls.delete();
			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file_xls.getAbsoluteFile());
				wb.write(os);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			result.put("message","导出成功！");
			result.put("status","success");
			result.put("data", str_web_path.concat(file_xls.getName()));
		}else{
			result.put("message","请重新操作！");
			result.put("status","fail");
		}
		return result;
	}



	@Override
	public Map<String, Object> exportExpress(DynamicDto dynamicDto) {
		ExpressDao expressDao = (ExpressDao) SpringHelper.getBean(ExpressDao.class.getName());
		List<Map<String , Object>>  list = new ArrayList<Map<String , Object>>();
		List<Map<String , Object>>  list_result = new ArrayList<Map<String , Object>>();
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		String storeno="";
		Map<String, Object> result = new HashedMap();
		try {
			if( dynamicDto.getTarget() == 1 ){  //城市总监级别
				List<Map<String,Object>> storeList = expressDao.queryExpressByCityId(dynamicDto.getCityId());
				String cityName = null;
				for(int i = 0 ; i < storeList.size() ; i++ ){
					Map<String,Object> map = storeList.get(i);
					cityName = map.get("cityname").toString();
				}
				
				if(dynamicDto.getStoreId()!=null){
					if(dynamicDto.getStoreId()==-10000){
						return null;
					}else{
						Store store = storeManager.findStore(dynamicDto.getStoreId());
						if(store!=null){
							storeno = store.getStoreno();
						}
					}
				}
				if( dynamicDto.getBeginDate() != null ){
					if(dynamicDto.getBeginDate().equals("prev_month")){  // 城市总监上个月
						String currentDate = this.getPrevDate();
						list_result = expressDao.queryExpressCount(currentDate);
						for(int i = 0 ; i < list_result.size() ; i++ ){
							Map<String,Object> map = list_result.get(i);
							String exit_cityName = map.get("cityName").toString();
							if(dynamicDto.getStoreId()!=null){//城市某一个门店
								if(cityName.equals(exit_cityName)&&storeno!=null&&!"".equals(storeno)&&storeno.equals(map.get("storeNo"))){
									list.add(map);
								}
							}else{//城市全部门店
								if(cityName.equals(exit_cityName)){
									list.add(map);
								}
							}
						}
					}else if(dynamicDto.getBeginDate().equals("cur_month")){ //城市总监当前月
						String preDate = this.getCurDate();
						list_result = expressDao.queryExpressCount(preDate);
						for(int j = 0 ; j < list_result.size() ; j++ ){
							Map<String,Object> map = list_result.get(j);
							String exit_cityName = map.get("cityName").toString();
							if(dynamicDto.getStoreId()!=null){//城市某一个门店
								if(cityName.equals(exit_cityName)&&storeno!=null&&!"".equals(storeno)&&storeno.equals(map.get("storeNo"))){
									list.add(map);
								}
							}else{//城市全部门店
								if(cityName.equals(exit_cityName)){
									list.add(map);
								}
							}
						}
					}
				}
		}else{   //店长级别
			if( dynamicDto.getBeginDate() != null ){
				if(dynamicDto.getBeginDate().equals("prev_month")){ //店长上个月
					Long storeId = dynamicDto.getStoreId();
					String currentDate = this.getPrevDate();
//					result = expressDao.queryExpressCount_cityLevel(currentDate);
//					result = expressDao.queryExpressCount(currentDate);
					//店长 -- 通过门店主键当前月日期
					list = expressDao.queryExpressInfoByStoreIdAndMonth(storeId,currentDate);
					int size = list.size();
					for( int x = 0 ; x < list.size() ; x++ ){
						Map<String,Object> map = list.get(x);
						//分页使用此datacount来分页
						map.put("datacount",size);
					}
				}else if(dynamicDto.getBeginDate().equals("cur_month")){ //店长当前月
					Long storeId = dynamicDto.getStoreId();
					String preDate = this.getCurDate();
					//店长 -- 通过门店主键上个月日期
					list = expressDao.queryExpressInfoByStoreIdAndMonth(storeId,preDate);
					int size = list.size();
					for( int x = 0 ; x < list.size() ; x++ ){
						Map<String,Object> map = list.get(x);
						//分页使用此datacount来分页
						map.put("datacount",size);
					}
				}
			}
		}
			
			if(list==null||list.size()==0){
				result.put("message","请重新操作！");
				result.put("status","fail");
				return result;
			}
			
		    String str_file_dir_path = PropertiesUtil.getValue("file.root");
            String str_web_path = PropertiesUtil.getValue("file.web.root");
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            setCellStyle_common(wb);
            setHeaderStyle(wb);
            HSSFSheet sheet = wb.createSheet("快递代送");
            HSSFRow row = sheet.createRow(0);
            String[] str_headers = {"城市","门店名称","门店编码","员工编号","员工姓名","数量"};
            String[] headers_key = {"cityName","storename","storeNo","employee_no","employee_name","express_count"};
            for(int i = 0;i < str_headers.length;i++){
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(getHeaderStyle());
                cell.setCellValue(new HSSFRichTextString(str_headers[i]));
            }
            for(int i = 0;i < list.size();i++){
                row = sheet.createRow(i+1);
                for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
                    setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
                }
            }
            File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"express.xls");
            if(file_xls.exists()){
                file_xls.delete();
            }
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file_xls.getAbsoluteFile());
                wb.write(os);
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(os != null){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            result.put("message","导出成功！");
            result.put("status","success");
            result.put("data", str_web_path.concat(file_xls.getName()));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("message","请重新操作！");
            result.put("status","fail");
		}
        
        return result;
	}

	
	private String getCurDate(){
		String yearmonth="";
		Calendar calendar = Calendar.getInstance();
        Integer month =  calendar.get(Calendar.MONTH); 
        if(month<9){
        	yearmonth = String.valueOf(calendar.get(Calendar.YEAR))+"-0"+String.valueOf(month+1);
        }else if(month>=9){
        	yearmonth = String.valueOf(calendar.get(Calendar.YEAR))+"-"+String.valueOf(month+1);

        }
        
        return yearmonth;
            
	}
	
	private String getPrevDate(){
		String yearmonth="";
		Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Integer month =  calendar.get(Calendar.MONTH); 
        if(month<9){
        	yearmonth = String.valueOf(calendar.get(Calendar.YEAR))+"-0"+String.valueOf(month+1);
        }else if(month>=9){
        	yearmonth = String.valueOf(calendar.get(Calendar.YEAR))+"-"+String.valueOf(month+1);

        }
        
        return yearmonth;
	}

	@Override
	public String getAreaTradeAmount(String employee_no, String year, String month) {
		HttpClientUtil hClientUtil = null;
		String url = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("app_key", "1002");
		jsonObject.put("stamp",System.currentTimeMillis());
		jsonObject.put("nonce",UUID.randomUUID().toString());
		jsonObject.put("year",year);
		jsonObject.put("month",month);
		jsonObject.put("employee_no", employee_no);

		String sign = EncryptUtils.getMD5(jsonObject.toString()+SECRET);

		hClientUtil = new HttpClientUtil();
		url = AREATRADEAMOUNT+"?sign="+sign;
		String dailyData = hClientUtil.getRemoteData(url, jsonObject);
		return dailyData;
	}


//	@Override
//	public String getAreaZdGmvAmount(String employee_no, String year, String month) {
//		HttpClientUtil hClientUtil = null;
//		String url = null;
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("app_key", "1002");
//		jsonObject.put("stamp",System.currentTimeMillis());
//		jsonObject.put("nonce",UUID.randomUUID().toString());
//		jsonObject.put("year",year);
//		jsonObject.put("month",month);
//		jsonObject.put("employee_no", employee_no);
//
//		String sign = EncryptUtils.getMD5(jsonObject.toString()+SECRET);
//
//		hClientUtil = new HttpClientUtil();
//		url = AREAZDGMVAMOUNT+"?sign="+sign;
//		String dailyData = hClientUtil.getRemoteData(url, jsonObject);
//		return dailyData;
//	}
	
	
	@Override
	public String getAreaNewaddCusAmount(String employee_no, String year, String month) {
		HttpClientUtil hClientUtil = null;
		String url = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("app_key", "1002");
		jsonObject.put("stamp",System.currentTimeMillis());
		jsonObject.put("nonce",UUID.randomUUID().toString());
		jsonObject.put("year",year);
		jsonObject.put("month",month);
		jsonObject.put("employee_no", employee_no);

		String sign = EncryptUtils.getMD5(jsonObject.toString()+SECRET);

		hClientUtil = new HttpClientUtil();
		url = AREANEWADDCUSAMOUNT+"?sign="+sign;
		String dailyData = hClientUtil.getRemoteData(url, jsonObject);
		return dailyData;
	}


	@Override
	public Map<String, Object> getAreaData(String employee_no, String year, String month) {
		Map<String, Object> result=new HashMap<String, Object>();
	    String	areatradeamount=this.getAreaTradeAmount(employee_no, year, month);
	    JSONObject jObject = new JSONObject(areatradeamount);
		if("2000000".equals(jObject.getString("status"))){//成功返回数据
			result.put("areaTradeAmount", jObject.getJSONArray("data").getJSONObject(0).get("areaTradeAmount"));
		}else{
			result.put("areaTradeAmount","0");
		}
//	    String	zdgmv=this.getAreaZdGmvAmount(employee_no, year, month);
//	    JSONObject jObject_zdgmv= new JSONObject(zdgmv);
//	    if("2000000".equals(jObject.getString("status"))){//成功返回数据
////			result.put("areaZdGmvAmount",jObject_zdgmv.getJSONArray("data").getJSONObject(0).get("areaZdGmvAmount"));
//	    	result.put("areaZdGmvAmount","--");
//		}else{
//			result.put("areaZdGmvAmount","--");
//		}
	    String	newaddcus=this.getAreaNewaddCusAmount(employee_no, year, month);
	    JSONObject jObject_newaddcus= new JSONObject(newaddcus);
	    if("2000000".equals(jObject.getString("status"))){//成功返回数据
			result.put("areaNewaddCusSum",jObject_newaddcus.getJSONArray("data").getJSONObject(0).get("areaNewaddCusSum"));
		}else{
			result.put("areaNewaddCusSum","0");
		}
	    result.put("areaZdGmvAmount","--");
		return result;
	}



	@Override
	public List<Map<String, Object>> selectAllCity() {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = dynamicDao.selectAllCity();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}


	@Override
	public Map<String, Object> getLastMonthCityRankingTop10(DynamicDto dd,PageInfo pageInfo,String sign) {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		DynamicDto dd1 = dd;
		Map<String,Object> dynamicMap = null;
		Map<String,Object> dynamicAllMap = null;
		Map<String,Object> result = new HashMap<String,Object>();
		try{
			dynamicMap = (Map<String, Object>)dynamicDao.getLastMonthGMVCityRankingTop10(dd,pageInfo);
			dd1.setCityId(null);
			dd1.setProvinceId(null);
			dynamicAllMap = (Map<String, Object>) dynamicDao.getLastMonthGMVCityRankingTop10(dd1,null);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		String gmv = this.getRankDataJson(dynamicMap,dynamicAllMap,dd,sign).toString();
		result.put("gmv", gmv);
		result.put("pageinfo", dynamicMap.get("pageinfo"));
		result.put("total_pages", dynamicMap.get("total_pages"));
		return result;
	}
	public JSONArray getRankDataJson(Map<String,Object> dynamicMap,Map<String,Object> dynamicAllMap,DynamicDto ddDto,String sign){
		JSONArray json = new JSONArray();
		List<Map<String,Object>> dynamicList = null;
		List<Map<String,Object>> dynamicAllList = null;
		Map<String,Map<String,Object>> dynamicByCityMap = new HashMap<String, Map<String,Object>>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Map<String,Object> dynamicByCity = null;
		List<Map<String,Object>> dynamicByCityList = null;
		dynamicList = (List<Map<String, Object>>) dynamicMap.get("gmv");
		dynamicAllList = (List<Map<String, Object>>) dynamicAllMap.get("gmv");
		for (int i = 0; i < dynamicList.size(); i++) {
				JSONObject jo = new JSONObject();
				Map<String,Object> dynamic = (Map<String,Object>)dynamicList.get(i);
				jo.put("province_id", dynamic.get("province_id"));
				jo.put("city_id", dynamic.get("city_id"));
	            jo.put("city_name", dynamic.get("city_name"));
	            jo.put("name", dynamic.get("name"));
	            jo.put("gmv_sum", dynamic.get("gmv_sum"));
	            if(sign!=null){
	            	for (int j = 0; j < dynamicAllList.size(); j++) {
						if(dynamic.get("city_name").toString().equals(dynamicAllList.get(j).get("city_name").toString())){
							jo.put("rank", j+1);
						}
					}
					ddDto.setCityId(Long.parseLong(String.valueOf(dynamic.get("city_id"))));
					ddDto.setCityName(String.valueOf(dynamic.get("city_name")));
					if(dynamicByCityMap.get(String.valueOf(dynamic.get("city_name")))==null){
						dynamicByCity = (Map<String, Object>) dynamicDao.getLastMonthGMVCityRankingTop10(ddDto,null);
						dynamicByCityMap.put(String.valueOf(dynamic.get("city_name")), dynamicByCity);
					}
					dynamicByCityList = (List<Map<String, Object>>)dynamicByCityMap.get(dynamic.get("city_name")).get("gmv");
					for(int k = 0; k < dynamicByCityList.size(); k++){
						if(dynamic.get("city_name").toString().equals(dynamicByCityList.get(k).get("city_name").toString())){
							jo.put("cityrank", k+1);
						}
					}
	            }
				json.put(jo);
			}
		return json;
	}
	public List<Map<String,Object>> getRankList(Map<String,Object> dynamicMap,Map<String,Object> dynamicAllMap,String keyName,DynamicDto ddDto,String methodKey){
		List<Map<String,Object>> dynamicList = null;
		List<Map<String,Object>> dynamicAllList = null;
		OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Map<String,Object> dynamicByCity = null;
		List<Map<String,Object>> dynamicByCityList = null;
		Map<String,Map<String,Object>> dynamicByCityMap = new HashMap<String, Map<String,Object>>();
		dynamicList = (List<Map<String, Object>>) dynamicMap.get("gmv");
		dynamicAllList = (List<Map<String, Object>>) dynamicAllMap.get("gmv");
		for (int i = 0; i < dynamicList.size(); i++) {
				JSONObject jo = new JSONObject();
				Map<String,Object> dynamic = (Map<String,Object>)dynamicList.get(i);
				for (int j = 0; j < dynamicAllList.size(); j++) {
					if("product_name".equals(keyName)){
						if(dynamic.get(keyName).toString().equals(dynamicAllList.get(j).get(keyName).toString())
								&&dynamic.get("city_name").toString().equals(dynamicAllList.get(j).get("city_name").toString())){
							dynamic.put("rank", j+1);;
						}
					}else{
						if(dynamic.get(keyName).toString().equals(dynamicAllList.get(j).get(keyName).toString())){
							dynamic.put("rank", j+1);
						}
					}
				}
				ddDto.setCityName(String.valueOf(dynamic.get("city_name")));
				if(dynamicByCityMap.get(String.valueOf(dynamic.get("city_name")))==null){
					if("1".equals(methodKey)){
						ddDto.setCityId(Long.parseLong(String.valueOf(dynamic.get("city_id"))));
						if(dynamicByCityMap.get(String.valueOf(dynamic.get("city_name")))==null){
							dynamicByCity = (Map<String, Object>) dynamicDao.getLastMonthGMVCityRankingTop10(ddDto,null);
							dynamicByCityMap.put(String.valueOf(dynamic.get("city_name")), dynamicByCity);
						}
					}else if("2".equals(methodKey)){
						ddDto.setCityId(Long.parseLong(String.valueOf(dynamic.get("city_id"))));
						if(dynamicByCityMap.get(String.valueOf(dynamic.get("city_name")))==null){
							dynamicByCity = (Map<String, Object>) dynamicDao.getLastMonthGMVStoreRankingTop10(ddDto,null);
							dynamicByCityMap.put(String.valueOf(dynamic.get("city_name")), dynamicByCity);
						}
					}else if("3".equals(methodKey)){
						List<Map<String, Object>> cityNO = new ArrayList<Map<String,Object>>();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("cityno", dynamic.get("city_code"));
						cityNO.add(map);
						if(dynamicByCityMap.get(String.valueOf(dynamic.get("city_code")))==null){
							dynamicByCity = (Map<String, Object>) orderDao.queryStoreCustmerCount(ddDto,cityNO,null,null);
							dynamicByCityMap.put(String.valueOf(dynamic.get("city_code")), dynamicByCity);
						}
					}else if("4".equals(methodKey)){
						ddDto.setCityId(Long.parseLong(String.valueOf(dynamic.get("city_id"))));
						if(dynamicByCityMap.get(String.valueOf(dynamic.get("city_name")))==null){
							dynamicByCity = (Map<String, Object>) dynamicDao.queryAreaTradeByEmp(ddDto,null);
							dynamicByCityMap.put(String.valueOf(dynamic.get("city_name")), dynamicByCity);
						}
					}else if("5".equals(methodKey)){
						if(dynamicByCityMap.get(String.valueOf(dynamic.get("city_name")))==null){
							dynamicByCity = (Map<String, Object>) dynamicDao.queryProductCityOrder(ddDto,null);
							dynamicByCityMap.put(String.valueOf(dynamic.get("city_name")), dynamicByCity);
						}
					}
				}
				if("3".equals(methodKey)){
					dynamicByCityList = (List<Map<String, Object>>)dynamicByCityMap.get(dynamic.get("city_code")).get("gmv");
				}else{
					dynamicByCityList = (List<Map<String, Object>>)dynamicByCityMap.get(dynamic.get("city_name")).get("gmv");
				}
				for(int k = 0; k < dynamicByCityList.size(); k++){
					if(String.valueOf(dynamic.get(keyName)).equals(String.valueOf(dynamicByCityList.get(k).get(keyName)))){
						dynamic.put("cityrank", k+1);
					}
				}
		}
		return dynamicList;
	}
	public List<Map<String,Object>> getRankOtherList(Map<String,Object> dynamicMap){
		List<Map<String,Object>> dynamicList = null;
		dynamicList = (List<Map<String, Object>>) dynamicMap.get("gmv");
			for (int i = 0; i < dynamicList.size(); i++) {
				JSONObject jo = new JSONObject();
				Map<String,Object> dynamic = (Map<String,Object>)dynamicList.get(i);
				dynamic.put("rank", i+1);
			}
		return dynamicList;
	}
	@Override
	public Map<String, Object> getLastMonthStoreRankingTop10(DynamicDto dd,PageInfo pageInfo,String sign) {
		
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		DynamicDto dd1 = dd;
		Map<String,Object> dynamicMap = null;
		Map<String,Object> dynamicAllMap = null;
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			dynamicMap = dynamicDao.getLastMonthGMVStoreRankingTop10(dd,pageInfo);//获取门店
			dd1.setCityId(null);
			dd1.setProvinceId(null);
			dynamicAllMap = (Map<String, Object>) dynamicDao.getLastMonthGMVStoreRankingTop10(dd1,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String gmv = this.getRankStoreDataJson(dynamicMap,dynamicAllMap,dd,sign).toString();
		result.put("gmv", gmv);
		result.put("pageinfo", dynamicMap.get("pageinfo"));
		result.put("total_pages", dynamicMap.get("total_pages"));
		return result;
	}
	public JSONArray getRankStoreDataJson(Map<String,Object> dynamicMap,Map<String,Object> dynamicAllMap,DynamicDto ddDto,String sign){
		JSONArray json = new JSONArray();
		List<Map<String,Object>> dynamicList = null;
		List<Map<String,Object>> dynamicAllList = null;
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Map<String,Object> dynamicByCity = null;
		List<Map<String,Object>> dynamicByCityList = null;
		Map<String,Map<String,Object>> dynamicByCityMap = new HashMap<String, Map<String,Object>>();
		dynamicList = (List<Map<String, Object>>) dynamicMap.get("gmv");
		dynamicAllList = (List<Map<String, Object>>) dynamicAllMap.get("gmv");
		if(dynamicList.size() == dynamicAllList.size()){
			for (int i = 0; i < dynamicList.size(); i++) {
				JSONObject jo = new JSONObject();
				Map<String,Object> dynamic = (Map<String,Object>)dynamicList.get(i);
				jo.put("city_id", dynamic.get("city_id"));
				jo.put("city_name", dynamic.get("city_name"));
				jo.put("store_id", dynamic.get("store_id"));
	            jo.put("store_name", dynamic.get("store_name"));
	            jo.put("store_pesgmv", dynamic.get("store_pesgmv"));
	            jo.put("province_id", dynamic.get("province_id"));
	            if(sign!=null){
		            jo.put("rank", i+1);
		            jo.put("cityrank", i+1);
	            }
	            json.put(jo);
			}
		}else{
			for (int i = 0; i < dynamicList.size(); i++) {
				JSONObject jo = new JSONObject();
				Map<String,Object> dynamic = (Map<String,Object>)dynamicList.get(i);
				jo.put("city_id", dynamic.get("city_id"));
				jo.put("city_name", dynamic.get("city_name"));
				jo.put("store_id", dynamic.get("store_id"));
	            jo.put("store_name", dynamic.get("store_name"));
	            jo.put("store_pesgmv", dynamic.get("store_pesgmv"));
	            if(sign!=null){
					for (int j = 0; j < dynamicAllList.size(); j++) {
						if(dynamic.get("store_name").toString().equals(dynamicAllList.get(j).get("store_name").toString())){
							jo.put("rank", j+1);
						}
					}
					ddDto.setCityId(Long.parseLong(String.valueOf(dynamic.get("city_id"))));
					ddDto.setCityName(String.valueOf(dynamic.get("city_name")));
					if(dynamicByCityMap.get(String.valueOf(dynamic.get("city_name")))==null){
						dynamicByCity = (Map<String, Object>) dynamicDao.getLastMonthGMVStoreRankingTop10(ddDto,null);
						dynamicByCityMap.put(String.valueOf(dynamic.get("city_name")), dynamicByCity);
					}
					dynamicByCityList = (List<Map<String, Object>>)dynamicByCityMap.get(dynamic.get("city_name")).get("gmv");
					for(int k = 0; k < dynamicByCityList.size(); k++){
						if(dynamic.get("store_name").toString().equals(dynamicByCityList.get(k).get("store_name").toString())){
							jo.put("cityrank", k+1);
						}
					}
	            }
				json.put(jo);
			}
		}
		return json;
	}
	@Override
	public Map<String, Object> getDailyStoreTotlePrice(DynamicDto dd) {
		Long city_id = dd.getCityId();
		String province_id = dd.getProvinceId();
		List<Map<String, Object>> cityNO = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> provinceNO = new ArrayList<Map<String,Object>>();
		Map<String, Object> result = new HashMap<String,Object>();
		List<Map<String,Object>> dailyStoreOrderList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> dailyUserList = new ArrayList<Map<String,Object>>();
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		if(city_id!=null){
			cityNO = storeDao.getCityNOOfCityById(city_id);
		}
		if(province_id!=null&&province_id!=""){
			provinceNO = storeDao.getProvinceNOOfCSZJ(province_id);
		}
		dailyStoreOrderList = orderDao.getDailyStoreOrderOfCurDay(dd,cityNO,provinceNO);
		if(dailyStoreOrderList!=null&&dailyStoreOrderList.size()>0){
			Map<String,Object> dailyStoreOrderMap = dailyStoreOrderList.get(0);
			dailyUserList = orderDao.getDailyUserOfCurDay(dd,cityNO,provinceNO);
			if(dailyUserList.size()>0){
				dailyStoreOrderMap.put("customer_count", dailyUserList.get(0).get("customer_count"));
			}else{
				dailyStoreOrderMap.put("customer_count", 0);
			}
		}
		String dailyOrder = this.getDailyStoreOrderData(dd,dailyStoreOrderList);
		result.put("daily", dailyOrder);
		return result;
	}
	public String getDailyStoreOrderData(DynamicDto dd,List<Map<String,Object>> dailyStoreOrderList) {
		JSONArray json = new JSONArray();
        for(Map<String,Object> dailyOrder : dailyStoreOrderList){
            JSONObject jo = new JSONObject();
            jo.put("storecur_all_price", dailyOrder.get("storecur_all_price"));
            jo.put("customer_count", dailyOrder.get("customer_count"));
            jo.put("checked_order_count", dailyOrder.get("checked_order_count"));
            json.put(jo);
        }
        return json.toString();
	}

	@Override
	public Map<String, Object> queryTradeByDepName(DynamicDto dd,PageInfo pageInfo,String str) {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Map<String,Object> dynamicList = null;
		try{
			dynamicList = dynamicDao.queryTradeByDepName(dd,pageInfo);//获取门店
		}catch (Exception e) {
			e.printStackTrace();
		}
		return dynamicList;
	}

	@Override
	public Map<String, Object> queryTradeByChannelName(DynamicDto dd,PageInfo pageInfo,String str) {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Map<String,Object> dynamicList = null;
		try {
			dynamicList = dynamicDao.queryTradeByChannelName(dd,pageInfo);//获取门店
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dynamicList;
	}

	@Override
	public Map<String, Object> getAllCountOfCityStoreGax(DynamicDto dd,PageInfo pageInfo) {
		Map<String, Object> result  = new HashMap<String, Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		try {
			List<Map<String,Object>> cityCountList = dynamicDao.findCityCount(dd);
			List<Map<String,Object>> storeCountList = dynamicDao.findStoreCount(dd);
			List<Map<String,Object>> storeKeeperCountList = dynamicDao.findStoreKeeperCount(dd);
			Map<String,Object> storeGaxCountList = dynamicDao.findGaxCount(dd,pageInfo);
			if(cityCountList!=null&&cityCountList.size()>0){
				result.put("cityCountList", cityCountList);
			}
			if(storeCountList!=null&&storeCountList.size()>0){
				result.put("storeCountList", storeCountList);
			}
			result.put("storeKeeper", storeKeeperCountList);
			result.put("employee", storeGaxCountList);
			result.put("status", "sucess");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "fail");
		}
		return result;
	}

	//TODO--------
	@Override
	public Map<String, Object> getLastMonthStoreChinaRankingTop10(DynamicDto dd) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		List<Map<String,Object>> dynamicList = dynamicDao.getLastMonthGMVStoreChinaRanking(dd);//获取门店
		String gmv = this.getLastMonthGMVStoreChinaRankingData(dd,dynamicList);
		result.put("gmv", gmv);
		return result;
	}
	private String getLastMonthGMVStoreChinaRankingData(DynamicDto dd,List<Map<String, Object>> dynamicList) {
		JSONArray json = new JSONArray();
        for(Map<String,Object> storeMonthGMV : dynamicList){
            JSONObject jo = new JSONObject();
            jo.put("store_name", storeMonthGMV.get("store_name"));
            jo.put("store_pesgmv", storeMonthGMV.get("store_pesgmv"));
            json.put(jo);
        }
        return json.toString();
	}


	@Override
	public Map<String, Object> selectAreaRankingOfStore(Long storeId,PageInfo pageInfo) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		
		try {
			DynamicDto dynamicDto = new DynamicDto();
			Calendar calendar = Calendar.getInstance();
			Integer day = calendar.get(Calendar.DAY_OF_MONTH);
			if(day==1){//当前日期是当月第一天
				calendar.set(Calendar.DAY_OF_MONTH,1);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				calendar.set(Calendar.DAY_OF_MONTH,1);
			}else{
				calendar.set(Calendar.DAY_OF_MONTH,1);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			
	        String first = sdf.format(calendar.getTime());
		        
	       
			Store store = storeManager.findStore(storeId);
			
			
			dynamicDto.setBeginDate(first);
			
			dynamicDto.setStoreNo(store.getStoreno());
			result = dynamicDao.selectAreaRankingOfStore(dynamicDto,pageInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("gmv", null);
		}
		return result;
	}


	@Override
	public Map<String, Object> selectDeptRankingOfStore(Long storeId) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		
		try {
			Calendar calendar = Calendar.getInstance();
			Integer day = calendar.get(Calendar.DAY_OF_MONTH);
			if(day==1){//当前日期是当月第一天
				calendar.set(Calendar.DAY_OF_MONTH,1);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				calendar.set(Calendar.DAY_OF_MONTH,1);
			}else{
				calendar.set(Calendar.DAY_OF_MONTH,1);
			}
			
			
			Store store = storeManager.findStore(storeId);
			DynamicDto dynamicDto = new DynamicDto();
			dynamicDto.setYear(calendar.get(Calendar.YEAR));
			dynamicDto.setMonth(calendar.get(Calendar.MONTH)+1);
			dynamicDto.setStoreNo(store.getStoreno());
			
			List<Map<String,Object>> dynamicList = dynamicDao.selectDeptRankingOfStore(dynamicDto);
			result.put("gmv", dynamicList);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("gmv", null);
		}
		return result;
	}


	@Override
	public Map<String, Object> selectChannelRankingOfStore(Long storeId,PageInfo pageInfo) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		try {
			
			Calendar calendar = Calendar.getInstance();
			Integer day = calendar.get(Calendar.DAY_OF_MONTH);
			if(day==1){//当前日期是当月第一天
				calendar.set(Calendar.DAY_OF_MONTH,1);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				calendar.set(Calendar.DAY_OF_MONTH,1);
			}else{
				calendar.set(Calendar.DAY_OF_MONTH,1);
			}
			
			
			Store store = storeManager.findStore(storeId);
			DynamicDto dynamicDto = new DynamicDto();
			dynamicDto.setYear(calendar.get(Calendar.YEAR));
			dynamicDto.setMonth(calendar.get(Calendar.MONTH)+1);
			dynamicDto.setStoreNo(store.getStoreno());
			result = dynamicDao.selectChannelRankingOfStore(dynamicDto,pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("gmv", null);
		}
		return result;
	}
	@Override
	public Map<String, Object> queryCityOrderRankingTop10(DynamicDto dd,PageInfo pageInfo) {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Map<String,Object> dynamicList = null;
		try {
			dynamicList = dynamicDao.CityOrderRankingTop10(dd,pageInfo);//获取门店
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dynamicList;
	}


	@Override
	public Map<String, Object> selectEmployeeRankingOfStore(Long storeId,PageInfo pageInfo) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		try {
			
			Calendar calendar = Calendar.getInstance();
			Integer day = calendar.get(Calendar.DAY_OF_MONTH);
			if(day==1){//当前日期是当月第一天
				calendar.set(Calendar.DAY_OF_MONTH,1);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				calendar.set(Calendar.DAY_OF_MONTH,1);
			}else{
				calendar.set(Calendar.DAY_OF_MONTH,1);
			}
		
		
			Store store = storeManager.findStore(storeId);
			DynamicDto dynamicDto = new DynamicDto();
			dynamicDto.setYear(calendar.get(Calendar.YEAR));
			dynamicDto.setMonth(calendar.get(Calendar.MONTH)+1);
			dynamicDto.setStoreNo(store.getStoreno());
			result = dynamicDao.selectEmployeeRankingOfStore(dynamicDto,pageInfo);
		
		} catch (Exception e) {
			e.printStackTrace();
			result.put("gmv", null);
		}
		return result;
	}

	@Override
	public Map<String, Object> selectEStoreRankingOfStore(Long storeId,PageInfo pageInfo) {
		Map<String,Object> result = new HashMap<String,Object>();
		OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		try {
			
			Calendar calendar1 = Calendar.getInstance();
			Integer day = calendar1.get(Calendar.DAY_OF_MONTH);
			if(day==1){//当前日期是当月第一天
				calendar1.set(Calendar.DAY_OF_MONTH,1);
				calendar1.add(Calendar.DAY_OF_MONTH, -1);
				calendar1.set(Calendar.DAY_OF_MONTH,1);
			}else{
				calendar1.set(Calendar.DAY_OF_MONTH,1);
			}
			
			String beginDate = sdf.format(calendar1.getTime());  
			Store store = storeManager.findStore(storeId);
			DynamicDto dynamicDto = new DynamicDto();
			dynamicDto.setBeginDate(beginDate);
			dynamicDto.setStoreIds(store.getPlatformid());
			result = orderDao.selectEStoreRankingOfStore(dynamicDto,pageInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("gmv", null);
		}
		return result;
	}


	@Override
	public Map<String, Object> selectDeptServerRanking(Long storeId) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		try {
			
			Calendar calendar1 = Calendar.getInstance();
			Integer day = calendar1.get(Calendar.DAY_OF_MONTH);
			if(day==1){//当前日期是当月第一天
				calendar1.set(Calendar.DAY_OF_MONTH,1);
				calendar1.add(Calendar.DAY_OF_MONTH, -1);
				calendar1.set(Calendar.DAY_OF_MONTH,1);
			}else{
				calendar1.set(Calendar.DAY_OF_MONTH,1);
			}
		
		
			Store store = storeManager.findStore(storeId);
			DynamicDto dynamicDto = new DynamicDto();
			dynamicDto.setYear(calendar1.get(Calendar.YEAR));
			dynamicDto.setMonth(calendar1.get(Calendar.MONTH)+1);
			dynamicDto.setStoreNo(store.getStoreno());
			List<Map<String,Object>> dynamicList = dynamicDao.selectDeptServerRanking(dynamicDto);
			result.put("gmv", dynamicList);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("gmv", null);
		}
		return result;
	}

	
	@Override
	public Map<String, Object> selectAreaOrderRanking(Long storeId,PageInfo pageInfo) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		try {
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH,1);
		
		
			Store store = storeManager.findStore(storeId);
			DynamicDto dynamicDto = new DynamicDto();
			dynamicDto.setYear(calendar.get(Calendar.YEAR));
			dynamicDto.setMonth(calendar.get(Calendar.MONTH)+1);
			dynamicDto.setStoreNo(store.getStoreno());
			result = dynamicDao.selectAreaOrderRanking(dynamicDto,pageInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("gmv", null);
		}
		return result;
	}



	@Override
	public Map<String, Object> selectChannelOrderRanking(Long storeId,PageInfo pageInfo) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		try {
			
			Calendar calendar1 = Calendar.getInstance();
			Integer day = calendar1.get(Calendar.DAY_OF_MONTH);
			if(day==1){//当前日期是当月第一天
				calendar1.set(Calendar.DAY_OF_MONTH,1);
				calendar1.add(Calendar.DAY_OF_MONTH, -1);
				calendar1.set(Calendar.DAY_OF_MONTH,1);
			}else{
				calendar1.set(Calendar.DAY_OF_MONTH,1);
			}
		  
		
			Store store = storeManager.findStore(storeId);
			DynamicDto dynamicDto = new DynamicDto();
			dynamicDto.setYear(calendar1.get(Calendar.YEAR));
			dynamicDto.setMonth(calendar1.get(Calendar.MONTH)+1);
			dynamicDto.setStoreNo(store.getStoreno());
			result = dynamicDao.selectChannelOrderRanking(dynamicDto,pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("gmv", null);
		}
		return result;
	}


	@Override
	public Map<String, Object> selectGMVOfStore(Long storeId) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		try {
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH,1);
		
		
			Store store = storeManager.findStore(storeId);
			DynamicDto dynamicDto = new DynamicDto();
			dynamicDto.setYear(calendar.get(Calendar.YEAR));
			dynamicDto.setMonth(calendar.get(Calendar.MONTH)+1);
			dynamicDto.setStoreNo(store.getStoreno());
			List<Map<String,Object>> dynamicList = dynamicDao.selectGMVOfStore(dynamicDto);
			result.put("gmv", dynamicList);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("gmv", null);
		}
		return result;
	}



	@Override
	public Map<String, Object> queryOrderCountByChannelName(DynamicDto dd,PageInfo pageInfo,String str) {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Map<String, Object> returnMap = null;
		try{
			returnMap =new HashMap<String,Object>();
			returnMap = dynamicDao.queryOrderCountByChannelName(dd,pageInfo);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}

	@Override
	public Map<String, Object> queryTradeSumByMonth(DynamicDto dd) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		result = dynamicDao.queryTradeSumByMonth(dd);
		Calendar a=Calendar.getInstance();
		int month = a.get(Calendar.MONTH)+1;
		dd.setMonth(month);
		dd.setYear(a.get(Calendar.YEAR));
		String beginDate = a.get(Calendar.YEAR)+"-"+(month<10?("0"+month):month)+"-"+(a.get(Calendar.DATE)<10?("0"+a.get(Calendar.DATE)):a.get(Calendar.DATE));   
		String endDate = a.get(Calendar.YEAR)+"-"+(month<10?("0"+month):month)+"-"+(a.get(Calendar.DATE)<10?("0"+a.get(Calendar.DATE)):a.get(Calendar.DATE));
		dd.setBeginDate(beginDate);
		dd.setEndDate(endDate);
		List<Map<String, Object>> customerMonthCountList = new ArrayList<Map<String,Object>>();
		//查询某月用户量
		if(dd.getCityId()==null&&dd.getProvinceId()==null){//总部
			customerMonthCountList = dynamicDao.queryMonthZbCustomerCount(dd);
		}else{//城市公司
			customerMonthCountList = dynamicDao.queryMonthCustomerCount(dd);
		}
		//查询某月的订单量
//		List<Map<String, Object>> orderMonthCountList = orderDao.queryMonthOrderCount(dd,cityNO,provinceNO,"0");
		if (result.isEmpty()) {
			result.put("order_amount", "0");
		}
		if(customerMonthCountList!=null&&customerMonthCountList.size()>0){
			result.put("customer_count", customerMonthCountList.get(0).get("customer_count"));
			result.put("last_customer_count", customerMonthCountList.get(0).get("last_customer_count"));
		}else{
			result.put("customer_count", "0");
			result.put("last_customer_count", "0");
		}
		return result;
	}



	@Override
	public Map<String, Object> queryAreaTradeByEmp(DynamicDto dd,PageInfo pageInfo,String sign) {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		DynamicDto dd1 = dd;
		Map<String,Object> dynamicMap = null;
		Map<String,Object> dynamicAllMap = null;
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			dynamicMap = dynamicDao.queryAreaTradeByEmp(dd,pageInfo);
			dd1.setCityId(null);
			dd1.setProvinceId(null);
			dynamicAllMap = (Map<String, Object>) dynamicDao.queryAreaTradeByEmp(dd1,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String gmv = this.getRankAreaTradeDataJson(dynamicMap,dynamicAllMap,dd,sign).toString();
		result.put("gmv", gmv);
		result.put("pageinfo", dynamicMap.get("pageinfo"));
		result.put("total_pages", dynamicMap.get("total_pages"));
		return result;
	}
	public JSONArray getRankAreaTradeDataJson(Map<String,Object> dynamicMap,Map<String,Object> dynamicAllMap,DynamicDto ddDto,String sign){
		JSONArray json = new JSONArray();
		List<Map<String,Object>> dynamicList = null;
		List<Map<String,Object>> dynamicAllList = null;
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Map<String,Object> dynamicByCity = null;
		List<Map<String,Object>> dynamicByCityList = null;
		Map<String,Map<String,Object>> dynamicByCityMap = new HashMap<String, Map<String,Object>>();
		dynamicList = (List<Map<String, Object>>) dynamicMap.get("gmv");
		dynamicAllList = (List<Map<String, Object>>) dynamicAllMap.get("gmv");
		if(dynamicList.size() == dynamicAllList.size()){
			for (int i = 0; i < dynamicList.size(); i++) {
				JSONObject jo = new JSONObject();
				Map<String,Object> dynamic = (Map<String,Object>)dynamicList.get(i);
				jo.put("city_name", dynamic.get("city_name"));
				jo.put("store_name", dynamic.get("store_name"));
				jo.put("employee_a_name", dynamic.get("employee_a_name"));
	            jo.put("pesgmv", dynamic.get("pesgmv"));
	            if(sign!=null){
	            	jo.put("rank", i+1);
	            	jo.put("cityrank", i+1);
	            }
	            json.put(jo);
			}
		}else{
			for (int i = 0; i < dynamicList.size(); i++) {
				JSONObject jo = new JSONObject();
				Map<String,Object> dynamic = (Map<String,Object>)dynamicList.get(i);
				jo.put("city_name", dynamic.get("city_name"));
				jo.put("store_name", dynamic.get("store_name"));
				jo.put("employee_a_name", dynamic.get("employee_a_name"));
	            jo.put("pesgmv", dynamic.get("pesgmv"));
	            if(sign!=null){
	            	for (int j = 0; j < dynamicAllList.size(); j++) {
						if(dynamic.get("employee_no").toString().equals(dynamicAllList.get(j).get("employee_no").toString())){
							jo.put("rank", j+1);
						}
					}
					ddDto.setCityId(Long.parseLong(String.valueOf(dynamic.get("city_id"))));
					ddDto.setCityName(String.valueOf(dynamic.get("city_name")));
					if(dynamicByCityMap.get(String.valueOf(dynamic.get("city_name")))==null){
						dynamicByCity = (Map<String, Object>) dynamicDao.queryAreaTradeByEmp(ddDto,null);
						dynamicByCityMap.put(String.valueOf(dynamic.get("city_name")), dynamicByCity);
					}
					dynamicByCityList = (List<Map<String, Object>>)dynamicByCityMap.get(dynamic.get("city_name")).get("gmv");
					for(int k = 0; k < dynamicByCityList.size(); k++){
						if(dynamic.get("employee_no").toString().equals(dynamicByCityList.get(k).get("employee_no").toString())){
							jo.put("cityrank", k+1);
						}
					}
	            }
				json.put(jo);
			}
		}
		return json;
	}

	@Override
	public Map<String, Object> queryServerTradeByEmp(DynamicDto dd) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		List<Map<String,Object>> dynamicList = dynamicDao.queryServerTradeByEmp(dd);
		if(!dynamicList.isEmpty()){
			JSONArray json = new JSONArray();
	        for(Map<String,Object> storeMonthGMV : dynamicList){
	            JSONObject jo = new JSONObject();
	            jo.put("employee_name", storeMonthGMV.get("employee_name"));
	            jo.put("gmv", storeMonthGMV.get("gmv"));
	            json.put(jo);
	        }
	        String gmv = json.toString();
	        result.put("gmv", gmv);
		}else{
			result.put("gmv", null);
		}
		return result;
	}


	@Override
	public Map<String, Object> queryTradeSumOfHistory(DynamicDto dd) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Calendar a=Calendar.getInstance();
		int month = a.get(Calendar.MONTH)+1;
		String beginDate = a.get(Calendar.YEAR)+"-"+(month<10?("0"+month):month)+"-"+(a.get(Calendar.DATE)<10?("0"+a.get(Calendar.DATE)):a.get(Calendar.DATE));   
		String endDate = a.get(Calendar.YEAR)+"-"+(month<10?("0"+month):month)+"-"+(a.get(Calendar.DATE)<10?("0"+a.get(Calendar.DATE)):a.get(Calendar.DATE));
		dd.setBeginDate(beginDate);
		dd.setEndDate(endDate);
		result = dynamicDao.queryTradeSumOfHistory(dd);
		//查询历史用户量
		List<Map<String, Object>> customerHistoryCountList = new ArrayList<Map<String,Object>>();
		if(dd.getCityId()==null&&dd.getProvinceId()==null){//总部
			customerHistoryCountList = dynamicDao.queryHistoryZbCustomerCount(dd);
		}else{//城市公司
			customerHistoryCountList = dynamicDao.queryHistoryCustomerCount(dd);
		}
		if (result.isEmpty()) {
			result.put("history_order_amount", "0");
		}
		if(customerHistoryCountList!=null&&customerHistoryCountList.size()>0){
			result.put("history_customer_count", customerHistoryCountList.get(0).get("history_customer_count"));
		}else{
			result.put("history_customer_count", 0);
		}
		return result;
	}



	@Override
	public Map<String, Object> selectHistoryGmvOfStore(Long storeId) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		try {
			
			Store store = storeManager.findStore(storeId);
			DynamicDto dynamicDto = new DynamicDto();
			dynamicDto.setStoreNo(store.getStoreno());
			List<Map<String,Object>> dynamicList = dynamicDao.selectHistoryGMVOfStore(dynamicDto);
			result.put("gmv", dynamicList);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("gmv", null);
		}
		return result;
	}


	@Override
	public Map<String, Object> queryTradeByProduct(DynamicDto dd) {
		Long city_id = dd.getCityId();
		String province_id = dd.getProvinceId();
		List<Map<String, Object>> cityNO = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> provinceNO = new ArrayList<Map<String,Object>>();
		Map<String, Object> result = new HashMap<String,Object>();
		List<Map<String,Object>> porductOrderList = new ArrayList<Map<String,Object>>();
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		if(city_id!=null){
			cityNO = storeDao.getCityNOOfCityById(city_id);
		}
		if(province_id!=null&&province_id!=""){
			provinceNO = storeDao.getProvinceNOOfCSZJ(province_id);
		}
		porductOrderList = orderDao.queryTradeByProduct(dd, cityNO, provinceNO);
		if(!porductOrderList.isEmpty()){
			JSONArray json = new JSONArray();
	        for(Map<String,Object> productMap : porductOrderList){
	            JSONObject jo = new JSONObject();
	            jo.put("product_name", productMap.get("product_name"));
	            jo.put("product_count", productMap.get("product_count"));
	            json.put(jo);
	        }
	        String gmv = json.toString();
	        result.put("gmv", gmv);
		}
		return result;
	}
	
	
	@Override
	public JSONObject insertNewTest(String testcode,String testdate){
		HttpClientUtil hClientUtil = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("proId", testcode);
		jsonObject.put("requestTime", testdate);
		System.out.println("param -> "+jsonObject.toString());
		String body = Base64Encoder.encode(jsonObject.toString()).replace("\r", "").replace("\n", "");
		String md5code = MD5Utils.getMD5Str(body+KEY);
        System.out.println("body -> " +body);
        System.out.println("MD5 -> "+MD5Utils.getMD5Str(body+KEY));
		hClientUtil = new HttpClientUtil();
		//String rtObj = hClientUtil.insRemoteData(THIRD_PART_TEST_URL, md5code, body);
		//savesynclog("","","","",jsonObject.toString(), rtObj);
		String rtObj = null;
		if(rtObj!=null&&rtObj.length()>0){
			try {
				return new JSONObject(rtObj);
			} catch (Exception e) {
				return new JSONObject("{\"msg\":\"非法JSON\"}");
			}
		}else{
			return new JSONObject("{}");
		}
	}
	
	/**
	 * 同步门店的方法
	 */
	@Override
	public JSONObject insertNewStore(String storeCode,String storeName,String provinceCode,String cityCode,String adCode,String address){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HttpClientUtil hClientUtil = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("storeName", storeName);
		jsonObject.put("storeCode", storeCode);
		jsonObject.put("provinceCode", provinceCode==null?"":provinceCode);
		jsonObject.put("cityCode", cityCode==null?"":cityCode);
		jsonObject.put("adCode", adCode==null?"":adCode);
		jsonObject.put("address", address==null?"":address);
		System.out.println("param -> "+jsonObject.toString());
		String body = Base64Encoder.encode(jsonObject.toString()).replace("\r", "").replace("\n", "");
		System.out.println("URL ->"+THIRD_PART_STORE_URL);
		String md5code = MD5Utils.getMD5Str(body+KEY);
        System.out.println("body -> " +body);
		hClientUtil = new HttpClientUtil();
		String rtObj = hClientUtil.insRemoteData(THIRD_PART_STORE_URL, md5code, body);
		savesynclog("",storeCode,storeName,"",provinceCode,cityCode,adCode,address,jsonObject.toString(), rtObj);
		if(rtObj!=null&&rtObj.length()>0){
			try {
				return new JSONObject(rtObj);
			} catch (Exception e) {
				return new JSONObject("{\"msg\":\"非法JSON\"}");
			}
		}else{
			return new JSONObject("{}");
		}
	}
	/**
	 * 同步员工的方法 
	 */
	@Override
	public JSONObject insertNewEmployee(String storeCode,String employeeCode,String employeeName,String telephone){
		HttpClientUtil hClientUtil = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("storeCode", storeCode);
		jsonObject.put("employeeCode", employeeCode);
		jsonObject.put("employeeName",employeeName);
		jsonObject.put("telephone", telephone);
		System.out.println("param -> "+jsonObject.toString());
		String body = Base64Encoder.encode(jsonObject.toString()).replace("\r", "").replace("\n", "");
		String md5code = MD5Utils.getMD5Str(body+KEY);
        System.out.println("body -> " +body);
		hClientUtil = new HttpClientUtil();
		String rtObj = hClientUtil.insRemoteData(THIRD_PART_EMP_URL, md5code, body);
		savesynclog(employeeCode,storeCode,"",telephone,"","","","",jsonObject.toString(), rtObj);
		if(rtObj!=null&&rtObj.contains("门店编码无效")){//如果不存在门店 
			//先同步一下门店 
			StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
			Store store = storeManager.findStoreByStoreNo(storeCode);
			insertNewStore(storeCode, store.getName(),store.getGaode_provinceCode(),store.getGaode_cityCode(),store.getGaode_adCode(),store.getGaode_address());
			String newRtObj = hClientUtil.insRemoteData(THIRD_PART_EMP_URL, md5code, body);
			savesynclog(employeeCode,storeCode,"",telephone,store.getGaode_provinceCode(),store.getGaode_cityCode(),store.getGaode_adCode(),store.getGaode_address(),jsonObject.toString(), newRtObj);
		}
		
		if(rtObj!=null&&rtObj.length()>0){
			try {
				return new JSONObject(rtObj);
			} catch (Exception e) {
				return new JSONObject("{\"msg\":\"非法JSON\"}");
			}
		}else{
			return new JSONObject("{}");
		}
	}
	
	
	@Override
	public String validateUser(String username,String pwd){
		HttpClientUtil hClientUtil = new HttpClientUtil();
		net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
		String[] arr = {username,pwd};
		jsonObject.put("managerName", "InterManager");
		jsonObject.put("methodName", "commonValidUser");
		jsonObject.put("parameters", arr);
		System.out.println("param -> "+jsonObject.toString());
		String rtObj = hClientUtil.validateRemoteData(VALIDATION_USER, "requestString="+jsonObject.toString());
		return rtObj;
	}
	
	
	private void savesynclog(String employeeCode,String storecode,String storename,String phone,String provinceCode,String cityCode,String adCode,String address,String jsonObject,String rtObj){
		SyncDataLogManager syncDataLogManager = (SyncDataLogManager) SpringHelper.getBean("syncDataLogManager");
    	SyncDataLog syncDataLog = new SyncDataLog();
    	syncDataLog.setStorecode(storecode);
    	syncDataLog.setEmployeeno(employeeCode);
    	syncDataLog.setPhone(phone);
    	syncDataLog.setStorename(storename);
    	syncDataLog.setProvinceCode(provinceCode);
    	syncDataLog.setCityCode(cityCode);
    	syncDataLog.setAdCode(adCode);
    	syncDataLog.setAddress(address);
    	syncDataLog.setSendsyncdata(jsonObject.toString());
    	syncDataLog.setRcvsyncdata(rtObj);
    	syncDataLogManager.saveSyncDataLog(syncDataLog);
	}


	@Override
	public Map<String, Object> queryProductCityOrder(DynamicDto dynamicDto,PageInfo pageInfo,String sign) {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		DynamicDto dd1 = dynamicDto;
		Map<String,Object> dynamicMap = null;
		Map<String,Object> dynamicAllMap = null;
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			dynamicMap = dynamicDao.queryProductCityOrder(dynamicDto,pageInfo);
			dd1.setCityName(null);
			dd1.setProvinceName(null);
			dynamicAllMap = (Map<String, Object>) dynamicDao.queryProductCityOrder(dd1,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String gmv = this.getRankProductOrderDataJson(dynamicMap,dynamicAllMap,dynamicDto,sign).toString();
		result.put("gmv", gmv);
		result.put("pageinfo", dynamicMap.get("pageinfo"));
		result.put("total_pages", dynamicMap.get("total_pages"));
		return result;
	}
	public JSONArray getRankProductOrderDataJson(Map<String,Object> dynamicMap,Map<String,Object> dynamicAllMap,DynamicDto ddDto,String sign){
		JSONArray json = new JSONArray();
		List<Map<String,Object>> dynamicList = null;
		List<Map<String,Object>> dynamicAllList = null;
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Map<String,Object> dynamicByCity = null;
		List<Map<String,Object>> dynamicByCityList = null;
		Map<String,Map<String,Object>> dynamicByCityMap = new HashMap<String, Map<String,Object>>();
		dynamicList = (List<Map<String, Object>>) dynamicMap.get("gmv");
		dynamicAllList = (List<Map<String, Object>>) dynamicAllMap.get("gmv");
		if(dynamicList.size() == dynamicAllList.size()){
			for (int i = 0; i < dynamicList.size(); i++) {
				JSONObject jo = new JSONObject();
				Map<String,Object> dynamic = (Map<String,Object>)dynamicList.get(i);
				jo.put("product_name", dynamic.get("product_name"));
				jo.put("product_count", dynamic.get("product_count"));
				if(sign!=null){
					jo.put("rank", i+1);
					jo.put("cityrank", i+1);
				}
	            json.put(jo);
			}
		}else{
			for (int i = 0; i < dynamicList.size(); i++) {
				JSONObject jo = new JSONObject();
				Map<String,Object> dynamic = (Map<String,Object>)dynamicList.get(i);
				jo.put("product_name", dynamic.get("product_name"));
				jo.put("product_count", dynamic.get("product_count"));
				if(sign!=null){
					for (int j = 0; j < dynamicAllList.size(); j++) {
						if(dynamic.get("product_name").toString().equals(dynamicAllList.get(j).get("product_name").toString())
								&&dynamic.get("city_name").toString().equals(dynamicAllList.get(j).get("city_name").toString())){
							jo.put("rank", j+1);
						}
					}
					ddDto.setCityName(String.valueOf(dynamic.get("city_name")));
					if(dynamicByCityMap.get(String.valueOf(dynamic.get("city_name")))==null){
						dynamicByCity = (Map<String, Object>) dynamicDao.queryProductCityOrder(ddDto,null);
						dynamicByCityMap.put(String.valueOf(dynamic.get("city_name")), dynamicByCity);
					}
					dynamicByCityList = (List<Map<String, Object>>)dynamicByCityMap.get(dynamic.get("city_name")).get("gmv");
					for(int k = 0; k < dynamicByCityList.size(); k++){
						if(dynamic.get("product_name").toString().equals(dynamicByCityList.get(k).get("product_name").toString())
								&&dynamic.get("city_name").toString().equals(dynamicByCityList.get(k).get("city_name").toString())){
							jo.put("cityrank", k+1);
						}
					}
				}
				json.put(jo);
			}
		}
		return json;
	}
	@Override
	public Map<String, Object> exportData(DynamicDto dynamicDto,String str) {
		Map<String, Object> result = new HashMap<String,Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> dynamicList = null;
		Map<String,Object> dynamicAllList = null;
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		String[] str_headers = new String[4];
		String[] headers_key = new String[4];
		String tableName = "";
		int target = dynamicDto.getTarget();
		String cityStr = String.valueOf(dynamicDto.getCityId());
		DynamicDto dynamicDto2 = new DynamicDto();
		dynamicDto2.setCityId(null);
		dynamicDto2.setProvinceId(null);
		dynamicDto2.setYear(dynamicDto.getYear());
		dynamicDto2.setMonth(dynamicDto.getMonth());
		str_headers[0]="所在城市排名";
		str_headers[1]="全国排名";
		headers_key[0]="cityrank";
		try {
			if(target==0){
				dynamicList = dynamicDao.getLastMonthGMVCityRankingTop10(dynamicDto,null);
				dynamicAllList = dynamicDao.getLastMonthGMVCityRankingTop10(dynamicDto2,null);
				list = this.getRankList(dynamicList, dynamicAllList,"city_name",dynamicDto,"1");
				tableName = "城市(本月GMV)排名";
				//定义表头 以及 要填入的 字段 
				str_headers[2]="城市名称";
				str_headers[3]="GMV(元)";
				headers_key[1]="rank";
				headers_key[2]="city_name";
				headers_key[3]="gmv_sum";
			}else if(target==1){
				dynamicList = dynamicDao.getLastMonthGMVStoreRankingTop10(dynamicDto,null);
				dynamicAllList = dynamicDao.getLastMonthGMVStoreRankingTop10(dynamicDto2,null);
				list = this.getRankList(dynamicList, dynamicAllList,"store_name",dynamicDto,"2");
				tableName = "门店(本月GMV)排名";
				str_headers[2]="门店名称";
				str_headers[3]="GMV(元)";
				headers_key[1]="rank";
				headers_key[2]="store_name";
				headers_key[3]="store_pesgmv";
			}else if(target==2){
				List<Map<String, Object>> cityNO = new ArrayList<Map<String,Object>>();
				List<Map<String, Object>> provinceNO = new ArrayList<Map<String,Object>>();
				StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
				Long city_id = dynamicDto.getCityId();
				String province_id = dynamicDto.getProvinceId();
				if(city_id!=null){
					cityNO = storeDao.getCityNOOfCityById(city_id);
				}
				if(province_id!=null&&province_id!=""){
					provinceNO = storeDao.getProvinceNOOfCSZJ(province_id);
				}
				dynamicList = orderDao.queryStoreCustmerCount(dynamicDto,cityNO,provinceNO,null);
				dynamicAllList = orderDao.queryStoreCustmerCount(dynamicDto2,null,null,null);
				list = this.getRankList(dynamicList, dynamicAllList,"store_name",dynamicDto,"3");
				tableName = "门店(本月新用户量)排名";
				str_headers[2]="门店名称";
				str_headers[3]="用户量(人)";
				headers_key[1]="rank";
				headers_key[2]="store_name";
				headers_key[3]="customer_count";
			}else if(target==3){
				dynamicList = dynamicDao.queryAreaTradeByEmp(dynamicDto,null);
				dynamicAllList = dynamicDao.queryAreaTradeByEmp(dynamicDto2,null);
				list = this.getRankList(dynamicList, dynamicAllList,"employee_no",dynamicDto,"4");
				tableName = "国安侠(本月GMV)排名";
				str_headers[2]="国安侠";
				str_headers[3]="GMV(元)";
				headers_key[1]="rank";
				headers_key[2]="employee_a_name";
				headers_key[3]="pesgmv";
			}else if(target==4){
				dynamicList = dynamicDao.queryProductCityOrder(dynamicDto,null);
				dynamicAllList = dynamicDao.queryProductCityOrder(dynamicDto2,null);
				list = this.getRankList(dynamicList, dynamicAllList,"product_name",dynamicDto,"5");
				tableName = "商品(本月GMV)排名";
				str_headers[2]="商品名称";
				str_headers[3]="GMV(元)";
				headers_key[1]="rank";
				headers_key[2]="product_name";
				headers_key[3]="product_count";
			}else if(target==5){
				dynamicList = dynamicDao.queryTradeByDepName(dynamicDto,null);
				//list=(List<Map<String, Object>>)dynamicList.get("gmv");
				list = this.getRankOtherList(dynamicList);
				tableName = "事业群(本月GMV)排名";
				str_headers = new String[3];
				headers_key = new String[3];
				str_headers[0]="排名";
				str_headers[1]="事业群名称";
				str_headers[2]="GMV(元)";
				headers_key[0]="rank";
				headers_key[1]="dep_name";
				headers_key[2]="order_amount";
			}else if(target==6){
				dynamicList = dynamicDao.queryTradeByChannelName(dynamicDto,null);
				//list=(List<Map<String, Object>>)dynamicList.get("gmv");
				list = this.getRankOtherList(dynamicList);
				tableName = "频道(本月GMV)排名";
				str_headers = new String[3];
				headers_key = new String[3];
				str_headers[0]="排名";
				str_headers[1]="频道名称";
				str_headers[2]="GMV(元)";
				headers_key[0]="rank";
				headers_key[1]="channel_name";
				headers_key[2]="order_amount";
			}else if(target==7){
				dynamicList = dynamicDao.queryOrderCountByChannelName(dynamicDto,null);
				//list=(List<Map<String, Object>>)dynamicList.get("gmv");
				list = this.getRankOtherList(dynamicList);
				tableName = "频道(本月订单量)排名";
				str_headers = new String[3];
				headers_key = new String[3];
				str_headers[0]="排名";
				str_headers[1]="频道名称";
				str_headers[2]="订单量(元)";
				headers_key[0]="rank";
				headers_key[1]="channel_name";
				headers_key[2]="order_count";
			}
  		} catch (Exception e) {
  			e.printStackTrace();
  			return null;
  		}
		if(list!=null&&list.size()>0){//成功返回数据
  			String str_file_dir_path = PropertiesUtil.getValue("file.root");
  			String str_web_path = PropertiesUtil.getValue("file.web.root");

  			HSSFWorkbook wb = new HSSFWorkbook();   
  	        setCellStyle_common(wb);
  	        setHeaderStyle(wb);
  	        HSSFSheet sheet = wb.createSheet(tableName);
  	        HSSFRow row = sheet.createRow(0);
  	        
  	       
  	        for(int i = 0;i < str_headers.length;i++){
  	        	HSSFCell cell = row.createCell(i);
  	            cell.setCellStyle(getHeaderStyle());
  	            cell.setCellValue(new HSSFRichTextString(str_headers[i]));
  	        }
  	        
  	        for(int i = 0;i < list.size();i++){
  	        	 row = sheet.createRow(i+1);
  	             for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
  	            	setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
  	             }
  	        }
  	        
	  	    File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"exportData.xls");
	        if(file_xls.exists()){
	              file_xls.delete();
	          }
  			FileOutputStream os = null;
  			try {
  				os = new FileOutputStream(file_xls.getAbsoluteFile());
  				wb.write(os);
  			}catch (Exception e) {
  				e.printStackTrace();
  			} finally {
  				if(os != null){
  					try {
  						os.close();
  					} catch (IOException e) {
  						e.printStackTrace();
  					}
  				}
  			}

  			result.put("message","导出成功！");
  			result.put("status","success");
  			result.put("data", str_web_path.concat(file_xls.getName()));
  		}else{
  			result.put("message","请重新操作！");
  			result.put("status","fail");
  		}
  		return result;
	}
	@Override
	public Map<String, Object> exportRanking(Long storeId, String flag) {
		Map<String,Object> result = new HashedMap();
		Map<String,Object> returnData = new HashedMap();
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(storeId);
		String  title = "";
		String[] str_headers = new String[2];
		String[] headers_key ={"name","amount"};
		if("areaRanking_gmv".equals(flag)){//门店片区gmv
			result  = selectAreaRankingOfStore(storeId,null);
			title = store.getStoreno()+" area_ranking(gmv)";
			str_headers[0]="片区名称";
			str_headers[1]="gmv(元)";
			
		}else if("deptRanking_gmv".equals(flag)){//事业部gmv
			//暂不提供
		}else if("channelRanking_gmv".equals(flag)){//频道gmv
			result = selectChannelRankingOfStore(storeId, null);
			title = store.getStoreno()+" channel_ranking(gmv)";
			str_headers[0]="频道名称";
			str_headers[1]="gmv(元)";
			
		}else if("employeeRanking_gmv".equals(flag)){//国安侠gmv
			result = selectEmployeeRankingOfStore(storeId, null);
			title = store.getStoreno()+" gax_ranking(gmv)";
			str_headers[0]="国安侠名称";
			str_headers[1]="gmv(元)";
		}else if("employeeRanking_order".equals(flag)){//国安侠订单量
			result = selectAreaOrderRanking(storeId, null);
			title = store.getStoreno()+" gax_ranking(order)";
			str_headers[0]="国安侠名称";
			str_headers[1]="订单量(单)";
		}else if("eStoreRanking_gmv".equals(flag)){//e店gmv
			result = selectEStoreRankingOfStore(storeId, null);
			title = store.getStoreno()+" E-store_ranking(gmv)";
			str_headers[0]="E店名称";
			str_headers[1]="gmv(元)";
		}else if("deptServerRanking_gmv".equals(flag)){//服务专员gmv
			//暂不提供
		}else if("channelRanking_order".equals(flag)){//频道订单量
			result= selectChannelOrderRanking(storeId, null);
			title = store.getStoreno()+" channel_ranking(order)";
			str_headers[0]="频道名称";
			str_headers[1]="订单量(单)";
		}else if("gaxNewCusRanking".equals(flag)){
			result= getGaxOfAreaNewaddcus(storeId, null);
			title = store.getStoreno()+" new_cus";
			str_headers[0]="国安侠名称";
			str_headers[1]="新用户量";
		}
		
		if(result.get("gmv")!=null){//成功返回数据
			List<Map<String, Object>> list = (List<Map<String, Object>>)result.get("gmv");
			if(list!=null&&list.size()>0){
				String str_file_dir_path = PropertiesUtil.getValue("file.root");
				String str_web_path = PropertiesUtil.getValue("file.web.root");

				HSSFWorkbook wb = new HSSFWorkbook();
				// 创建Excel的工作sheet,对应到一个excel文档的tab

				setCellStyle_common(wb);
				setHeaderStyle(wb);
				HSSFSheet sheet = wb.createSheet(title);
				HSSFRow row = sheet.createRow(0);
				for(int i = 0;i < str_headers.length;i++){
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(getHeaderStyle());
					cell.setCellValue(new HSSFRichTextString(str_headers[i]));
				}

				for(int i = 0;i < list.size();i++){
					row = sheet.createRow(i+1);
					for(int cellIndex = 0;cellIndex < headers_key.length; cellIndex ++){
						setCellValue(row, cellIndex, list.get(i).get(headers_key[cellIndex]));
					}
				}



				File file_xls = new File(str_file_dir_path + File.separator +System.currentTimeMillis()+"_"+title+".xls");
				if(file_xls.exists()){
					file_xls.delete();
				}
				FileOutputStream os = null;
				try {
					os = new FileOutputStream(file_xls.getAbsoluteFile());
					wb.write(os);
				}catch (Exception e) {
					e.printStackTrace();
				} finally {
					if(os != null){
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				returnData.put("message","导出成功！");
				returnData.put("status","success");
				returnData.put("data", str_web_path.concat(file_xls.getName()));
			}else{
				returnData.put("message","请重新操作！");
				returnData.put("status","fail");
			}

		}else{
			returnData.put("message","请重新操作！");
			returnData.put("status","fail");
		}
		
		return returnData;
	}

	@Override
	public String getNewAreaTradeAmount(String employee_no, String year, String month) {
		HttpClientUtil hClientUtil = null;
		String url = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("app_key", "1002");
		jsonObject.put("stamp",System.currentTimeMillis());
		jsonObject.put("nonce",UUID.randomUUID().toString());
		jsonObject.put("year",year);
		jsonObject.put("month",month);
		jsonObject.put("employee_no", employee_no);

		String sign = EncryptUtils.getMD5(jsonObject.toString()+SECRET);

		hClientUtil = new HttpClientUtil();
		url = NEWAREATRADEAMOUNT+"?sign="+sign;
		String dailyData = hClientUtil.getRemoteData(url, jsonObject);
		return dailyData;
	}

	@Override
	public Map<String, Object> getNewAreaData(String employee_no, String year, String month) {
		Map<String, Object> result=new HashMap<String, Object>();
	    String	areatradeamount=this.getNewAreaTradeAmount(employee_no, year, month);
	    JSONObject jObject = new JSONObject(areatradeamount);
		if("2000000".equals(jObject.getString("status"))){//成功返回数据
			//国安侠GMV总和
			result.put("pesgmv", jObject.getJSONArray("data").getJSONObject(0).get("pesgmv")==null?"0":jObject.getJSONArray("data").getJSONObject(0).get("pesgmv"));
			//手动分配GMV
			result.put("pes_assigngmv", jObject.getJSONArray("data").getJSONObject(0).get("pes_assigngmv")==null?"0":jObject.getJSONArray("data").getJSONObject(0).get("pes_assigngmv"));
			//片区GMV
			result.put("pes_areagmv", jObject.getJSONArray("data").getJSONObject(0).get("pes_areagmv")==null?"0":jObject.getJSONArray("data").getJSONObject(0).get("pes_areagmv"));
			//服务GMV
			result.put("pes_sendgmv", jObject.getJSONArray("data").getJSONObject(0).get("pes_sendgmv")==null?"0":jObject.getJSONArray("data").getJSONObject(0).get("pes_sendgmv"));
			//平均分配GMV
			result.put("pes_pergmv", jObject.getJSONArray("data").getJSONObject(0).get("pes_pergmv")==null?"0":jObject.getJSONArray("data").getJSONObject(0).get("pes_pergmv"));
			
		}
	    String	newaddcus=this.getAreaNewaddCusAmount(employee_no, year, month);
	    JSONObject jObject_newaddcus= new JSONObject(newaddcus);
	    if("2000000".equals(jObject.getString("status"))){//成功返回数据
			result.put("areaNewaddCusSum",jObject_newaddcus.getJSONArray("data").getJSONObject(0).get("areaNewaddCusSum"));
		}else{
			result.put("areaNewaddCusSum","0");
		}
		return result;
	}







	
	@Override
	public Map<String, Object> getStoreCustomerRanking(Long storeId, PageInfo pageInfo) {
		OrderDao orderDao  = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		Store store  = storeManager.findStore(storeId);
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			result = orderDao.queryStoreCustomerOfMonth(store.getPlatformid(),pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}


	@Override
	public Map<String, Object> getGaxOfAreaNewaddcus(Long storeId, PageInfo pageInfo) {
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		Store store  = storeManager.findStore(storeId);
		Map<String,Object> result = new HashMap<String,Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			DynamicDto dynamicDto = new DynamicDto();
			dynamicDto.setStoreNo(store.getStoreno());
			dynamicDto.setStoreId(storeId);
			Calendar calendar1 = Calendar.getInstance();
			Integer day = calendar1.get(Calendar.DAY_OF_MONTH);
			String firstDay;
			String lastDay;
			if(day==1){//当前日期是当月第一天
				calendar1.set(Calendar.DAY_OF_MONTH,1);
				calendar1.add(Calendar.DAY_OF_MONTH, -1);
				lastDay = sdf.format(calendar1.getTime());
				calendar1.set(Calendar.DAY_OF_MONTH,1);
				firstDay = sdf.format(calendar1.getTime());
				
			}else{
				lastDay = sdf.format(calendar1.getTime());
				calendar1.set(Calendar.DAY_OF_MONTH,1);
				firstDay = sdf.format(calendar1.getTime());
			}
	        
	       
	       
	       
	        
	        dynamicDto.setBeginDate(firstDay);
	        dynamicDto.setEndDate(lastDay);
			result = dynamicDao.getGaxOfAreaNewaddcus(dynamicDto, pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> queryPlatformidByStoreId(String storeId) {
		Map<String, Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		List<Map<String, Object>> list  = dynamicDao.queryPlatformidByStoreId(storeId);
		result.put("storeList", list);
		return result;
	}
	@Override
	public Map<String, Object> getCustomerOfCurMonth(DynamicDto dd) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		Long city_id = dd.getCityId();
		String province_id = dd.getProvinceId();
		List<Map<String, Object>> cityNO = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> provinceNO = new ArrayList<Map<String,Object>>();
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		if(city_id!=null){
			cityNO = storeDao.getCityNOOfCityById(city_id);
		}
		if(province_id!=null&&province_id!=""){
			provinceNO = storeDao.getProvinceNOOfCSZJ(province_id);
		}
		//查询某月用户量(按照城市分组)
		List<Map<String, Object>> customerMonthCountList = dynamicDao.queryMonthCustomerCountGroup(dd,cityNO,provinceNO,"0");
		result.put("month_customer_count", customerMonthCountList);
		return result;
	}
	@Override
	public Map<String, Object> getSumOfCurYear(DynamicDto dd) {
		Map<String,Object> result = new HashMap<String,Object>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		String city_id = String.valueOf(dd.getCityId());
		String province_id = String.valueOf(dd.getProvinceId());
		//查询某年营业额
		List<Map<String, Object>> sumYearGMVList = dynamicDao.queryYearSumGMV(dd,city_id,province_id,"0");
		result.put("year_gmv_sum", sumYearGMVList);
		return result;
	}
	/** 
	* 获得指定日期的后一天 
	* @param specifiedDay 
	* @return 
	*/ 
	public String getSpecifiedDayAfter(String specifiedDay){ 
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);
		String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}
	@Override
	public Map<String, Object> getNewMonthUserCount(DynamicDto dd) {
		Map<String,Object> result = new HashMap<String,Object>();
		Long city_id = dd.getCityId();
		String province_id = dd.getProvinceId();
		List<Map<String, Object>> cityNO = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> provinceNO = new ArrayList<Map<String,Object>>();
		StoreDao storeDao = (StoreDao)SpringHelper.getBean(StoreDao.class.getName());
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String curDate = com.cnpc.pms.base.file.comm.utils.DateUtil.curDate();
		String beginDate = DateUtils.getBeforeDate(curDate,-30);
		String endDate = DateUtils.lastDate();
		String lastDate = DateUtils.lastDate();
		dd.setBeginDate(beginDate);
		dd.setEndDate(endDate);
		try {
			if(city_id!=null){
				cityNO = storeDao.getCityNOOfCityById(city_id);
			}
			if(province_id!=null&&province_id!=""){
				provinceNO = storeDao.getProvinceNOOfCSZJ(province_id);
			}
			Map<String, Object> newMonthUserCount = dynamicDao.queryNewMonthUserCount(dd,cityNO,provinceNO);
			List<Map<String,Object>> lst_data = (List<Map<String, Object>>) newMonthUserCount.get("lst_data");
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
			String maxDateStr = lastDate;    
		    String minDateStr = "";    
		    Calendar calc =Calendar.getInstance();      
	         for(int i=0;i<30;i++){  
	        	 calc.setTime(sdf.parse(maxDateStr));    
	             calc.add(calc.DATE, -i);    
	             Date minDate = calc.getTime();    
	             minDateStr = sdf.format(minDate);   
	           	Map<String,Object> lst_map = new HashMap<String, Object>();
	           	lst_map.put("week_date", minDateStr.substring((minDateStr.indexOf("-")+1),minDateStr.length()));
	           	lst_map.put("new_cus_count", 0);
				lst_map.put("pay_cus_count", 0);
				for(int j=0;j<lst_data.size();j++){
	    			Map<String,Object> lst_map_week = lst_data.get(j);
	    			String dateStr = String.valueOf(lst_map_week.get("week_date"));
	    			if(minDateStr.contains(dateStr)){
	    				lst_data.remove(j);
	    				String week_date = String.valueOf(lst_map_week.get("week_date"));
	    				lst_map.put("week_date", week_date);
	    				lst_map.put("new_cus_count", lst_map_week.get("new_cus_count"));
	    				lst_map.put("pay_cus_count", lst_map_week.get("pay_cus_count"));
	    				
	    			}
				}
				lst_data.add(lst_map);
	         }
			result.put("new_month_userCount", newMonthUserCount);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public Map<String, Object> querySeveralDaysPerformanceOfArea(DynamicDto dynamicDto) {
		AreaDao areaDao = (AreaDao)SpringHelper.getBean(AreaDao.class.getName());
		Map<String, Object>  result = new HashedMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		String endDate = sdf.format(calendar.getTime());
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.DAY_OF_MONTH, -6);
		
		String beginDate = sdf.format(calendar2.getTime());
		dynamicDto.setBeginDate(beginDate);
		dynamicDto.setEndDate(endDate);
		List<String> dateList = new ArrayList<String>();
		for(int i=1;i<=7;i++){
			String temp_date = sdf.format(calendar2.getTime());
			calendar2.add(Calendar.DAY_OF_MONTH, 1);
			dateList.add(temp_date);
		}
	
		try {
			List<Map<String, Object>> houseHold=null;
			if("gmv".equals(dynamicDto.getDataType())){
				houseHold = areaDao.queryHouseHoldNumber(dynamicDto);
			}
			
			List<Map<String, Object>> data1 = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
			
			List<Map<String, Object>> data1_r = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> data2_r = new ArrayList<Map<String, Object>>();
			if("gmv".equals(dynamicDto.getDataType())){//营业额
				data1 = areaDao.queryGMVOfEveryDayByArea(dynamicDto);
				for(int m=0;m<dateList.size();m++){
					String key_date = dateList.get(m);
					Map<String, Object> temp_map = new HashedMap();
					temp_map.put("gmv",0);
					temp_map.put("signTime", key_date);
					for(int n=0;n<data1.size();n++){
						if(data1.get(n).get("signTime").equals(key_date)){
							temp_map.put("gmv", data1.get(n).get("gmv"));
							break;
						}
					}
					
					data1_r.add(temp_map);
				}
			}else if("consumer".equals(dynamicDto.getDataType())){//消费用户
				data1 = areaDao.queryNewCustomeOfEveryDayByArea(dynamicDto);//拉新用户
				data2 = areaDao.queryConsumerOfEveryDayByArea(dynamicDto);//消费用户
				
				
				for(int m=0;m<dateList.size();m++){
					String key_date = dateList.get(m);
					Map<String, Object> temp_map = new HashedMap();
					temp_map.put("customer",0);
					temp_map.put("signTime", key_date);
					for(int n=0;n<data1.size();n++){
						if(data1.get(n).get("signTime").equals(key_date)){
							temp_map.put("customer", data1.get(n).get("customer"));
							break;
						}
					}
					
					data1_r.add(temp_map);
				}
				
				
				for(int m=0;m<dateList.size();m++){
					String key_date = dateList.get(m);
					Map<String, Object> temp_map = new HashedMap();
					temp_map.put("consumer",0);
					temp_map.put("signTime", key_date);
					for(int n=0;n<data2.size();n++){
						if(data2.get(n).get("signTime").equals(key_date)){
							temp_map.put("consumer", data2.get(n).get("consumer"));
							break;
						}
					}
					
					data2_r.add(temp_map);
				}
			}
			
			
			result.put("houseHold", (houseHold!=null&&houseHold.size()>0)?houseHold.get(0).get("houseHoldNumber"):0);
			result.put("data1",data1_r);
			result.put("data2", data2_r);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return result;
	}


	
	@Override
	public Map<String, Object> queryCurMonthPerformanceOfArea(DynamicDto dynamicDto) {
		AreaDao areaDao = (AreaDao)SpringHelper.getBean(AreaDao.class.getName());
		Map<String, Object>  result = new HashedMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,1);
		String beginDate = sdf.format(calendar.getTime());
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.DAY_OF_MONTH, 1);
		String endDate = sdf.format(calendar2.getTime());
		dynamicDto.setBeginDate(beginDate);
		dynamicDto.setEndDate(endDate);
		try {
			List<Map<String, Object>> order = areaDao.queryOrderOfArea(dynamicDto);
			List<Map<String, Object>> deptData = areaDao.queryPerfOfAreaByDept(dynamicDto);
			List<Map<String, Object>> channelData = areaDao.queryPerfOfAreaByChannel(dynamicDto);
			List<Map<String, Object>> tinyVillageData = areaDao.queryHeatMapOfArea(dynamicDto);
			
			result.put("order", (order!=null&&order.size()>0)?order.get(0).get("amount"):0);
			result.put("deptData", deptData);
			result.put("channelData", channelData);
			result.put("tinyVillageData",tinyVillageData);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return result;
	}

	@Override
	public Map<String, Object> getWeekCustomerOrderRate(DynamicDto dd) {
		Map<String, Object>  result = new HashedMap();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		try{
//			String beginDate = "2018-03-19";
//    		String endDate = "2018-03-25";
			String cur = com.cnpc.pms.base.file.comm.utils.DateUtil.curDate();
			String curDate = DateUtils.lastDate();
			String beginDate = DateUtils.getBeforeDate(cur,-7);
			dd.setBeginDate(beginDate);
			dd.setEndDate(curDate);
			Map<String, Object> customerOrderRate = dynamicDao.getWeekCustomerOrderRate(dd);
			List<Map<String,Object>> lst_data = (List<Map<String, Object>>) customerOrderRate.get("lst_data");
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
			String maxDateStr = curDate;    
		    String minDateStr = "";    
		    Calendar calc =Calendar.getInstance();      
	         for(int i=0;i<7;i++){  
	        	 calc.setTime(sdf.parse(maxDateStr));    
	             calc.add(calc.DATE, -i);    
	             Date minDate = calc.getTime();    
	             minDateStr = sdf.format(minDate);   
	           	Map<String,Object> lst_map = new HashMap<String, Object>();
	           	lst_map.put("week_date", minDateStr.substring((minDateStr.indexOf("-")+1),minDateStr.length()));
	           	lst_map.put("new_cus_count", 0);
				lst_map.put("pay_cus_count", 0);
				for(int j=0;j<lst_data.size();j++){
	    			Map<String,Object> lst_map_week = lst_data.get(j);
	    			String dateStr = String.valueOf(lst_map_week.get("week_date"));
	    			if(minDateStr.contains(dateStr)){
	    				lst_data.remove(j);
	    				String week_date = String.valueOf(lst_map_week.get("week_date"));
	    				lst_map.put("week_date", week_date);
	    				lst_map.put("new_cus_count", lst_map_week.get("new_cus_count"));
	    				lst_map.put("pay_cus_count", lst_map_week.get("pay_cus_count"));
	    				
	    			}
				}
				lst_data.add(lst_map);
	         }
			result.put("lst_data", lst_data);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public Map<String, Object> getDailyFirstOrderCity() {
		Map<String, Object>  result = new HashedMap();
		List<Map<String,Object>> dailyStoreOrderCityList = new ArrayList<Map<String,Object>>();
		DynamicDao dynamicDao = (DynamicDao)SpringHelper.getBean(DynamicDao.class.getName());
		OrderDao orderDao = (OrderDao)SpringHelper.getBean(OrderDao.class.getName());
		List<Map<String, Object>> allCityList = dynamicDao.selectAllCity();
		dailyStoreOrderCityList = orderDao.getDailyFirstOrderCity();
		for (int i = 0; i < allCityList.size(); i++) {
			Map<String, Object> cityMap = allCityList.get(i);
			String cityno = String.valueOf(cityMap.get("cityno"));
			if(cityno.startsWith("00")){
				cityno = cityno.substring(1,cityno.length());
			}
			for (int j = 0; j < dailyStoreOrderCityList.size(); j++) {
				Map<String, Object> orderCityMap = dailyStoreOrderCityList.get(j);
				String cityCode = String.valueOf(orderCityMap.get("city_code"));
				if(cityno.equals(cityCode)){
					orderCityMap.put("city_name", cityMap.get("cityname"));
				}
			}
		}
		result.put("daily", dailyStoreOrderCityList);
		return result;
	}
}
