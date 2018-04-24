package com.cnpc.pms.base.query.manager.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.dto.PMSDTO;
import com.cnpc.pms.base.entity.IEntity;
import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.ColumnsPageInfo;
import com.cnpc.pms.base.paging.impl.ColumnsSort;
import com.cnpc.pms.base.paging.impl.StringFilter;
import com.cnpc.pms.base.paging.operator.ConditionOperator;
import com.cnpc.pms.base.query.QueryDefinition;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.query.manager.QueryManager;
import com.cnpc.pms.base.query.model.PMSColumn;
import com.cnpc.pms.base.query.model.PMSQuery;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.BeanUtil;
import com.cnpc.pms.base.util.DateUtil;
import com.cnpc.pms.base.util.JSonHelper;
import com.cnpc.pms.base.util.PMSPropertyUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.base.util.StrUtil;

/**
 * Query Manager Implementation.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation ,
 * http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/27
 */
public class QueryManagerImpl extends NxQueryManagerImpl implements QueryManager {

	/** the cached query request objects map key in user session. */
	protected static final String QUERY_REQUEST_OBJECTS = "queryRequestObjects";
	/** The Constant COLUMNS_KEY. */
	protected static final String COLUMNS_KEY = "columns";
	/** The Constant CONDITIONS_KEY. */
	protected static final String CONDITIONS_KEY = "conditions";

	/** The Constant DO_NOT_CACHE. */
	public static final int DO_NOT_CACHE = 0;
	/** The Constant DO_CACHE. */
	public static final int DO_CACHE = 1;
	/** The Constant USE_CACHE. */
	public static final int USE_CACHE = 2;

	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMetadataByArgs(String queryId, String pageinfostr, ColumnsSort columnsSort, List<Map<String, Object>> listConditions, boolean usecache) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<PMSColumn> pmsColumnList = new ArrayList<PMSColumn>();
		ColumnsPageInfo columnpageinfo = new ColumnsPageInfo();
		if(pageinfostr != null){
			String[] pageinfos = pageinfostr.split(",");
			columnpageinfo.setCurrentPage(Integer.valueOf(pageinfos[0]));
			columnpageinfo.setRecordsCountPage(Integer.valueOf(pageinfos[1]));
			columnpageinfo.setRecordsPerPage(Integer.valueOf(pageinfos[2]));
		}
		Map<String, Object> map = null;
		PMSQuery query = getQuery(queryId);
		// 自己定义的查询方法
		String managerMethod = query.getMetadataMethod();
		if (StrUtil.isNotBlank(managerMethod)) {
			map = invokeQueryMetadataMethod(managerMethod,columnpageinfo,columnsSort,listConditions);
		}
		List<Map<String, Object>> pagecolumnlist = (List<Map<String, Object>>)map.get("columnsList");//从业务获取的list
		List<Map<String, Object>> dataColumnList = new ArrayList<Map<String, Object>>();//传到客户端list
		if(pagecolumnlist != null){
			for(Map<String, Object> pagecolumnMap : pagecolumnlist){
				Map<String, Object> datacolumnMap = new HashMap<String, Object>();//传到客户端map
				List<Map<String, Object>> paralist = (List<Map<String, Object>>)pagecolumnMap.get("keylist");
				List<String> strList = new ArrayList<String>();
				for(Map<String, Object> strmap : paralist){
					strList.add(strmap.get("key").toString()+":"+strmap.get("header").toString());
				}
				datacolumnMap.put(pagecolumnMap.get("name").toString(), strList);
				dataColumnList.add(datacolumnMap);
				for(Map<String, Object> keymap : paralist){
					PMSColumn col = new PMSColumn();
					col.setKey(keymap.get("key").toString());
					col.setHeader(keymap.get("header").toString());
					pmsColumnList.add(col);
				}
			}
		}else{
			pmsColumnList = (List<PMSColumn>)map.get("columns");
		}
		if(pmsColumnList != null){
			for (PMSColumn col : pmsColumnList) {
				Map<String, Object> columnMap = new HashMap<String, Object>();
				for (String name : new String[] {"header", "key", "align", "width", "display", "allowSearch", "allowSort",
						"tips", "dict", "columnSort", }) {
					try {
						Object value = PropertyUtils.getSimpleProperty(col, name);
						columnMap.put(name, value);
					} catch (Exception e) {
						log.warn("Fail to get Property {} from PMSColumn {}", name, col.getKey());
					}
				}
				list.add(columnMap);
			}
		}
		map.put("columnsList", dataColumnList);
		map.put("columnpageinfo", columnpageinfo);
		map.put("columns", list);
		map.put("columnsSort", columnsSort);
		return map;
	}
	public Map<String, Object> getMetadata(String queryId, boolean usecache) {
		PMSQuery query = getQuery(queryId);
		Map<String, Object> metadata = new HashMap<String, Object>();
		metadata.put("header", query.getHeader());
		metadata.put("order", query.getOrder());
		metadata.put("uniondatakey", query.getUniondatakey());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (PMSColumn col : query.getColumns()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (String name : new String[] {"header", "key", "align", "width", "display", "allowSearch", "allowSort",
					"tips", "dict", "columnSort", }) {
				try {
					Object value = PropertyUtils.getSimpleProperty(col, name);
					map.put(name, value);
				} catch (Exception e) {
					log.warn("Fail to get Property {} from PMSColumn {}", name, col.getKey());
				}
			}
			list.add(map);
		}
		metadata.put("columns", list);

		if (usecache == true) { // if use cache, get the last query conditions
								// from session, and send to client.
			Map<String, Map<String, Object>> conditions = new HashMap<String, Map<String, Object>>();
			QueryConditions reqObj = this.getCachedQueryObj(queryId);
			if (null != reqObj) {
				for (Map<String, Object> map : reqObj.getConditions()) {
					conditions.put(map.get("key").toString(), map);
				}
			}
			metadata.put("conditions", conditions);
		}

		return metadata;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.cnpc.pms.base.manager.QueryManager#getQueryColumns(java.lang.String)
	 */
	/**
	 * Gets the query columns.
	 * 
	 * @param queryId
	 *            the query id
	 * @return the query columns
	 */
	@Deprecated
	public List<Map<String, Object>> getQueryColumns(String queryId) {
		PMSQuery query = getQuery(queryId);
		// query.getSort();
		// query.getHeader();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (PMSColumn col : query.getColumns()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (String name : new String[] {"header", "key", "align", "width", "display", "allowSearch", "allowSort",
					"tips", "dict", }) {
				try {
					Object value = PropertyUtils.getSimpleProperty(col, name);
					map.put(name, value);
				} catch (Exception e) {
					log.warn("Fail to get Property {} from PMSColumn {}", name, col.getKey());
				}
			}
			list.add(map);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.cnpc.pms.base.manager.QueryManager#getColumnsAndCachedConditions(
	 * java.lang.String)
	 */
	/**
	 * Gets the columns and cached conditions in user session.
	 * 
	 * @param queryId
	 *            the query id
	 * @return the columns and cached conditions
	 */
	@Deprecated
	public Map<String, Object> getColumnsAndCachedConditions(String queryId) {
		Map<String, Object> columnsAndConditions = new HashMap<String, Object>();
		List<Map<String, Object>> columns = this.getQueryColumns(queryId);
		columnsAndConditions.put(COLUMNS_KEY, columns);
		// 将缓存的查询条件以map的形式返回到UI端
		// Map<String, PMSColumn> conditions = new HashMap<String, PMSColumn>();
		Map<String, Map<String, Object>> conditions = new HashMap<String, Map<String, Object>>();
		QueryConditions reqObj = this.getCachedQueryObj(queryId);
		if (null != reqObj) {
			for (Map<String, Object> map : reqObj.getConditions()) {
				conditions.put(map.get("key").toString(), map);
				;
			}
			// for (PMSColumn col : reqObj.getColumns()) {
			// if (null != col.getValue() &&
			// StrUtil.isNotBlank(String.valueOf(col.getValue()))) {
			// conditions.put(col.getKey(), col);
			// }
			// }
		}
		columnsAndConditions.put(CONDITIONS_KEY, conditions);
		return columnsAndConditions;
	}

	private Map<String, Object> getCount(QueryConditions conditions, String propName) throws InvalidFilterException {
		Boolean result = true;
		Long resultsize = 0l;
		String querymaxsize = null;
		Map<String, Object> map = new HashMap<String, Object>();

		PMSQuery query = getQuery(conditions.getQueryId());
		String businessId = query.getBusinessId();
		IFilter filter = getACLFilter(businessId);
		IFilter customFilter = generateFilter(conditions, query);
		if (null != filter) {
			filter = filter.appendAnd(customFilter);
		} else {
			filter = customFilter;
		}

		resultsize = this.getDao().getObjectsCount(query, filter);
		try {
			querymaxsize = PMSPropertyUtil.getValueOfProperties(propName);
			if (null != querymaxsize) {
				if (resultsize > Long.parseLong(querymaxsize)) {
					result = false;
				}
			}
		} catch (IOException e) {
			result = false;
			log.error(e.getMessage(), e);
		}

		map.put("querymaxsize", querymaxsize);
		map.put("count", resultsize.toString());
		map.put("result", result);
		return map;

	}

	/**
	 * Get count.
	 * 
	 * @param conditions
	 *            the query conditions
	 * @return result size
	 * @throws InvalidFilterException
	 *             InvalidFilterException
	 */
	public Map<String, Object> getCount(QueryConditions conditions) throws InvalidFilterException {
		return getCount(conditions, "querymaxsize");
	}

	public Map<String, Object> getCountForOffline(QueryConditions conditions) throws InvalidFilterException {
		return getCount(conditions, "offlineQuerymaxsize");
	}

	public Map<String, Object> executeQuery(QueryConditions conditions) throws InvalidFilterException {
		// 暂时禁用缓存
		// if (conditions.getCache() == DO_CACHE) {
		// cacheQueryObj(conditions);
		// } else if (conditions.getCache() == USE_CACHE) {
		// // get cached request object from user session.
		// QueryConditions cachedObj =
		// getCachedQueryObj(conditions.getQueryId());
		// if (null != cachedObj) {
		// 仅缓存查询条件
		// conditions.setConditions(cachedObj.getConditions());
		// // conditions = cachedObj;
		// }
		// }
		return this.doQuery(conditions);
	}

	/**
	 * Gets the cached query request object map in user session.
	 * 
	 * @return the cached map
	 */
	@SuppressWarnings("unchecked")
	protected HashMap<String, QueryConditions> getCachedMap() {
		HashMap<String, QueryConditions> cacheConditions = null;
		UserSession session = SessionManager.getUserSession();
		if (null == session) {
			// TODO process when user session is null.
			return new HashMap<String, QueryConditions>();
		}
		cacheConditions = (HashMap<String, QueryConditions>) session.get(QUERY_REQUEST_OBJECTS);
		if (null == cacheConditions) {
			cacheConditions = new HashMap<String, QueryConditions>();
			session.put(QUERY_REQUEST_OBJECTS, cacheConditions);
		}
		return cacheConditions;
	}

	/**
	 * Cache query request object into user session.
	 * 
	 * @param conditions
	 *            the req obj
	 */
	protected void cacheQueryObj(QueryConditions conditions) {
		this.getCachedMap().put(conditions.getQueryId(), conditions);
	}

	/**
	 * Gets the cached query request object from user session.
	 * 
	 * @param queryId
	 *            the query id
	 * @return the cached query obj
	 */
	protected QueryConditions getCachedQueryObj(String queryId) {
		return this.getCachedMap().get(queryId);
	}

	/**
	 * Converts the entity objects to PMSDTO or HashMap objects. Translate the
	 * dicts. Format date and numbers.
	 * 
	 * @param query
	 * @param objects
	 * @return
	 */
	@SuppressWarnings({"unchecked" })
	protected List<?> convertObjects(PMSQuery query, List<IEntity> objects) {
		String dtoClassName = query.getDtoClass();
		List results = new ArrayList();
		try {
			if (StrUtil.isNotBlank(dtoClassName)) {
				Class<? extends PMSDTO> dtoClass = (Class<? extends PMSDTO>) Class.forName(dtoClassName);
				for (IEntity entity : objects) {
					PMSDTO dto = dtoClass.newInstance();
					dto.fromEntity(entity, query.getColumns());
					results.add(dto);
				}
			} else {
				for (IEntity entity : objects) {
					HashMap dto = new HashMap();
					BeanUtil.copyProperties(entity, dto, query.getColumns());
					results.add(dto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Convert entities error: " + e.getMessage());
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> doQuery(QueryConditions conditions) throws InvalidFilterException {
		PMSQuery query = getQuery(conditions.getQueryId());
		// 自己定义的查询方法
		String managerMethod = query.getMethod();
		if (StrUtil.isNotBlank(managerMethod)) {
			return invokeQueryMethod(managerMethod, conditions);
		}

		// 数据权限
		String businessId = query.getBusinessId();
		IFilter filter = getACLFilter(businessId);

		// 通过客户端传过来的查询条件组装IFilter
		IFilter customFilter = generateFilter(conditions, query);
		if (null != filter) {
			filter = filter.appendAnd(customFilter);
		} else {
			filter = customFilter;
		}

		FSP fsp = new FSP();
		fsp.setPage(conditions.getPageinfo());
		fsp.setUserFilter(filter);
		if (conditions.getSortinfo() == null && query.getSort() != null) {
			fsp.setSort(query.getSort());
		} else {
			fsp.setSort(conditions.getSortinfo());
		}
		List<IEntity> objects = null;
		// 需要distinct处理
		if (StrUtil.isNotBlank(query.getDistinct())) {
			objects = getDao(query.getTargetClass()).getObjects(query, fsp);
		} else {
			objects = getDao(query.getTargetClass()).getObjects(query.getTargetClass(), fsp);
		}
		String statisticsInfo = executeStatistics(conditions);
		String header = query.getHeader();
		// always return dto/map.
		List<?> dtos = convertObjects(query, objects);
		
		List<Map<String,String>> unionDataKeyList = this.getUnionDataKeyList(dtos, query);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String columnscurrentpage = null;
		for(Map<String, Object> condition : conditions.getConditions()){
			if("columnscurrentpage".equals(condition.get("key"))){
				columnscurrentpage = (String)condition.get("value");
			}
		}
		if(columnscurrentpage == null){
			map.put("columnscurrentpage", 1);
		}else{
			map.put("columnscurrentpage", columnscurrentpage);
		}
		map.put("header", header);
		map.put("data", dtos);
		map.put("StatisticsInfo", statisticsInfo);
		map.put("pageinfo", fsp.getPage());
		map.put("sort", fsp.getSort());
		map.put("unionDataKeyList", unionDataKeyList);
		return map;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> invokeQueryMethod(String managerMethod, QueryConditions conditions) {
		Map<String, Object> map = null;
		if (managerMethod.indexOf('.') != -1) {
			String managerName = managerMethod.substring(0, managerMethod.indexOf('.'));
			String methodName = managerMethod.substring(managerMethod.indexOf('.') + 1);
			IManager manager = (IManager) SpringHelper.getBean(managerName);
			if (null != manager && manager instanceof IManager) {
				try {
					Method method = manager.getClass().getMethod(methodName, QueryConditions.class);
					map = (Map<String, Object>) method.invoke(manager, conditions);
				} catch (Exception e) {
					log.error("Fail to invoke method: " + managerMethod);
				}
			}
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> invokeQueryMetadataMethod(String managerMethod, ColumnsPageInfo columnpageinfo, ColumnsSort columnsSort, List<Map<String,Object>> conditions) {
		Map<String, Object> map = null;
		QueryConditions queryConditions = new QueryConditions();
		queryConditions.setConditions(conditions);
		queryConditions.setColumnpageinfo(columnpageinfo);
		queryConditions.setColumnsSort(columnsSort);
		if (managerMethod.indexOf('.') != -1) {
			String managerName = managerMethod.substring(0, managerMethod.indexOf('.'));
			String methodName = managerMethod.substring(managerMethod.indexOf('.') + 1);
			IManager manager = (IManager) SpringHelper.getBean(managerName);
			if (null != manager && manager instanceof IManager) {
				try {
					Method method = manager.getClass().getMethod(methodName,QueryConditions.class);
					map = (Map<String, Object>) method.invoke(manager, queryConditions);
				} catch (Exception e) {
					log.error("Fail to invoke method: " + managerMethod);
				}
			}
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.cnpc.pms.base.manager.QueryManager#executeStatistics(com.gatz.leadersapp
	 * .base.common.model.PMSQueryRequestObject)
	 */
	/**
	 * Execute statistics.
	 * 
	 * @param conditions
	 *            the QueryConditions obj
	 * @return the string
	 * @throws InvalidFilterException
	 *             the invalid filter exception
	 */
	public String executeStatistics(QueryConditions conditions) throws InvalidFilterException {
		PMSQuery query = getQuery(conditions.getQueryId());
		if (query.hasStatistics()) {
			IFilter filter = generateFilter(conditions, query);
			List<?> ret = this.getDao().getStatisticsInfo(query.getTargetClass(), filter, query.getStatString());
			List<String> statItems = query.getStatItems();
			List<String> statFormat = query.getStatFormat();
			int statItemsSize = statItems.size();
			if (ret.size() > 0) {
				StringBuffer resultSb = new StringBuffer();
				if (statItemsSize == 1) {
					resultSb.append(statItems.get(0)).append(
						processStatResult(ret.get(0), statFormat.get(0)).toString());
				} else {
					Object[] objects = (Object[]) ret.get(0);
					for (int i = 0; i < statItemsSize; i++) {
						resultSb.append(' ').append(statItems.get(i))
								.append(processStatResult(objects[i], statFormat.get(i)).toString()).append(',');
					}
					resultSb.deleteCharAt(resultSb.length() - 1);
				}
				return resultSb.toString();
			}
		}
		return "";
	}

	/**
	 * Process null object.
	 * 
	 * @param object
	 *            the object
	 * @return the object
	 */
	private Object processStatResult(Object object, String format) {
		if (object == null) {
			return 0;
		}
		if (format == null) {
			return object;
		}
		return BeanUtil.formatByType(object, format);
	}

	protected PMSQuery getQuery(String queryId) {
		PMSQuery query = QueryDefinition.getQueryById(queryId);
		return query;
	}

	@SuppressWarnings("unchecked")
	protected IFilter generateFilter(QueryConditions condition, PMSQuery query) throws InvalidFilterException {
		IFilter filter = null;
		if (StrUtil.isNotBlank(query.getFilter())) {
			filter = new StringFilter(query.getFilter());
		}
		for (Map<String, Object> map : condition.getConditions()) {
			String key = (String) map.get("key");
			Object value = map.get("value");
			PMSColumn column = query.getColumn(key);
			if (value != null && StrUtil.isNotBlank(value.toString()) && column != null && column.isAllowSearch()) {
				try {
					ConditionOperator operator = getOperator(column, (String) map.get("operator"), value);
					Class<?> clazz = JSonHelper.getClass(column.getType());
					Object newValue;
					if (operator.equals(ConditionOperator.BETWEEN)) {
						String[] values = (value.toString() + " ").split(",");
						if (values.length == 0 || (StrUtil.isBlank(values[0]) && StrUtil.isBlank(values[1]))) {
							continue;
						} else {
							List<Object> list = new ArrayList<Object>();
							for (String v : values) {
								if (StrUtil.isBlank(v)) {
									list.add(null);
								} else {
									list.add(JSonHelper.getValue(clazz, v));
								}
							}
							newValue = list;
						}
					} else if (operator.equals(ConditionOperator.IN) || operator.equals(ConditionOperator.NOT_IN)) {
						String[] values = value.toString().split(",");
						List<Object> list = new ArrayList<Object>();
						for (String v : values) {
							if (StrUtil.isNotBlank(v)) {
								list.add(JSonHelper.getValue(clazz, v));
							}
						}
						if (list.size() == 0) {
							continue;
						}
						newValue = list;
					} else {
						newValue = JSonHelper.getValue(clazz, value);
					}
					IFilter simpleFilter = null;
					// NOTE In case of Date, should do some adjust
					if ((Date.class.equals(clazz) || Date.class.equals(clazz.getSuperclass()))
									&& value.toString().indexOf(':') < 0) {
						if (operator.equals(ConditionOperator.BETWEEN)) {
							List<Object> list = (List<Object>) newValue;
							if (list.get(1) != null) {
								Object nextValue = DateUtil.getLastMilliSecond(list.get(1));
								list.set(1, nextValue);
							}
						} else if (operator.equals(ConditionOperator.EQ) || operator.equals(ConditionOperator.GT)
										|| operator.equals(ConditionOperator.GE)) {
							Object nextValue = DateUtil.getLastMilliSecond(newValue);
							if (operator.equals(ConditionOperator.EQ)) {// turn
																		// the
																		// eq to
																		// a
																		// between
																		// condition
								operator = ConditionOperator.BETWEEN;
								newValue = Arrays.asList(new Object[] {newValue, nextValue });
							} else {// >,>=,Up max the Date value
								newValue = nextValue;
							}
						}
					}
					simpleFilter = operator.getFilter(key, newValue);
					if (simpleFilter != null) {
						filter = simpleFilter.appendAnd(filter);// NOTE:
																// Conjunction
																// ignored here.
					}
				} catch (Exception e) {
					log.error("Fail to generate filter:{}, with conditions {}", query.getId(),
						condition.getConditions());
					throw new InvalidFilterException("Fail to generate filter: ", e);
				}
			}
		}
		return filter;
	}

	private IFilter getACLFilter(String businessId) {
		IFilter filter = null;
		if (StrUtil.isNotBlank(businessId)) {
			try {
				Map<String, IFilter> filterMap =
								(Map<String, IFilter>) SessionManager.getUserSession().getSessionData().get("dataACL");
				filter = filterMap.get(businessId);
			} catch (Exception e) {
				log.error("Fail to get filter by businessId:" + e.getMessage());
			}
		}
		return filter;
	}

	
	// NOTE: can change the logic where to get the operator
	@Deprecated
	private ConditionOperator getOperator(PMSColumn column, String operator) throws InvalidFilterException {
		ConditionOperator oper;
		if (StrUtil.isBlank(operator)) {
			oper = column.getOperatorObject();
		} else {
			oper = ConditionOperator.getOperator(operator);
		}
		return oper;
	}
	
	//郝成杰  重写：添加value参数，解决operate加入LIKE OR IN
	private ConditionOperator getOperator(PMSColumn column, String operator, Object value) throws InvalidFilterException {
		ConditionOperator oper;
		if (StrUtil.isBlank(operator)) {
			if ("like_or_in".equalsIgnoreCase(column.getOperator())){
				if (value !=null && value.toString().indexOf(",") >0 ){
					oper = ConditionOperator.IN;
				} else {
					oper = ConditionOperator.LIKE;
				}
			} else {
				oper = column.getOperatorObject();
			}
		} else {
			oper = ConditionOperator.getOperator(operator);
		}
		return oper;
	}

	public void refreshQuery() {
		QueryDefinition.getInstance().update();
	}

	private IDAO getDao(Class<?> entity) {
		IDAO relativedDao = null;
		try {
			String managerName = SpringHelper.getManagerNameByEntity(entity.getCanonicalName());
			IManager manager = (IManager) SpringHelper.getBean(managerName);
			relativedDao = manager.getDao();
		} catch (Exception e) {
			relativedDao = this.getDao();
			log.warn("No relatived dao found in the spring context for the entity:{}", entity.getName());
		}
		return relativedDao;
	}
	
	/**
	 * 得到要传入下一个query的数据集
	 * @param fieldName
	 * @param o
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String,String>> getUnionDataKeyList(List<?> list,PMSQuery query){
		List<Map<String,String>> keyList = new ArrayList<Map<String,String>>();
		if(list == null) {
			return keyList;
		}
		String unionDataKey = query.getUniondatakey();
		if(unionDataKey != null){
			if(unionDataKey.indexOf(",") != -1){
				String[] unionDataKeys = unionDataKey.split(",");
				for(int i=0;i<unionDataKeys.length;i++){
					String temp = unionDataKeys[i];
					String [] arr = temp.split("\\.");
					String unionDataKeysValue = "";
					for(int j=0;j<list.size();j++){
						Object oList = list.get(j);
						Object o = null;
						if(oList instanceof HashMap){
							for(String tempKey : arr){
								Map<String,Object> map1 = (Map<String,Object>)oList;
								o = map1.get(tempKey);								
							}

						}else{
							o = this.getFieldValueByName(unionDataKeys[i], oList);
						}
						
						unionDataKeysValue = unionDataKeysValue + o.toString() +",";
					}
					Map<String,String> map = new HashMap<String,String>();

					map.put("key", unionDataKeys[i].replaceAll("\\.", "-"));
					if(unionDataKeysValue.length() > 0) {
						map.put("value", unionDataKeysValue.substring(0, unionDataKeysValue.lastIndexOf(",")));
					} else {
						map.put("value", "");
					}
					keyList.add(map);
					
				}
			}else{
				Map<String,String> map = new HashMap<String,String>();
				String unionDataKeysValue = "";
				for(int j=0;j<list.size();j++){
					Object o = this.getFieldValueByName(unionDataKey, list.get(j));
					unionDataKeysValue = unionDataKeysValue + o.toString() +",";
				}
				map.put("key", unionDataKey);
				if(unionDataKeysValue.length() > 0) {
					map.put("value", unionDataKeysValue.substring(0, unionDataKeysValue.lastIndexOf(",")));
				} else {
					map.put("value", "");
				}
				keyList.add(map);
			}
		}
		return keyList;
	}
	
	/**
	 * 根据类的属性得到属性值
	 * @param fieldName
	 * @param o
	 * @return
	 */
	private Object getFieldValueByName(String fieldName, Object o) {  
		       try {    
		    	   Map mapObj = (Map)o;
		    	   Object value = mapObj.get(fieldName);
		           return value;    
		       } catch(ClassCastException classCastException) {
		    	   try {
			           String firstLetter = fieldName.substring(0, 1).toUpperCase();    
			           String getter = "get" + firstLetter + fieldName.substring(1);    
			           Method method = o.getClass().getMethod(getter, new Class[] {});    
			           Object value = method.invoke(o, new Object[] {});   
			           return value;   
		    	   }catch(Exception e) {
			           log.error(e.getMessage(),e);    
			           return null;  		    		   
		    	   }
		       }catch (Exception e) {    
		           log.error(e.getMessage(),e);    
		           return null;    
		       }    
		   }
	//@Override
	public QueryConditions getCachedCondition(String s) {
		// TODO Auto-generated method stub
		return null;
	}
	//@Override
	public Map<String, Object> getCustomizedMetaInfo(String queryId, String pageUrl) {
		// TODO Auto-generated method stub
		return super.getCustomizedMetaInfo(queryId, pageUrl);
	}
	//@Override
	public Map<String, Object> getMetaInfo(String s) {
		// TODO Auto-generated method stub
		return super.getMetaInfo(s);
	}
	//@Override
	public boolean operationAllowed(QueryConditions queryconditions, String s) {
		// TODO Auto-generated method stub
		return false;
	}  
}
