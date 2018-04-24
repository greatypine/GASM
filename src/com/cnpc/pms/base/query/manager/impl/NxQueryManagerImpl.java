// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NxQueryManagerImpl.java

package com.cnpc.pms.base.query.manager.impl;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.exception.PMSManagerException;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.*;
import com.cnpc.pms.base.paging.impl.StringFilter;
import com.cnpc.pms.base.paging.operator.ConditionOperator;
import com.cnpc.pms.base.query.QueryDefinition;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.query.manager.NxQueryManager;
import com.cnpc.pms.base.query.model.*;
import com.cnpc.pms.base.queryconfig.entity.QueryConfig;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.*;
import java.lang.reflect.Method;
import java.util.*;
import org.slf4j.Logger;

public class NxQueryManagerImpl extends BaseManagerImpl implements
		NxQueryManager {

	public NxQueryManagerImpl() {
	}

	protected PMSQuery getQuery(String queryId) {
		PMSQuery query = QueryDefinition.getQueryById(queryId);
		if (query == null) {
			log.error("Fail to find query with ID: {}", queryId);
			throw new PMSManagerException((new StringBuilder())
					.append("Fail to find query with ID:").append(queryId)
					.toString());
		} else {
			return query;
		}
	}

	public void refreshQuery() {
		QueryDefinition.getInstance().update();
	}

	public boolean operationAllowed(QueryConditions conditions, String operation) {
		int maxSize = PropertiesUtil.getIntValue(operation, 1000);
		int count = queryCount(conditions).intValue();
		boolean result = count <= maxSize;
		return result;
	}

	public QueryConditions getCachedCondition(String cacheKey) {
		QueryConditions reqObj = getCachedQueryObj(cacheKey);
		return reqObj;
	}

	private IFilter getCompinedFilter(QueryConditions conditions) {
		PMSQuery query = getQuery(conditions.getQueryId());
		String businessId = query.getBusinessId();
		IFilter filter = getACLFilter(businessId);
		IFilter customFilter = generateFilter(conditions);
		if (null != filter)
			filter = filter.appendAnd(customFilter);
		else
			filter = customFilter;
		return filter;
	}

	private IFilter getACLFilter(String businessId) {
		IFilter filter = null;
		if (StrUtil.isNotBlank(businessId))
			try {
				Map filterMap = (Map) SessionManager.getUserSession()
						.getSessionData().get("dataACL");
				filter = (IFilter) filterMap.get(businessId);
			} catch (Exception e) {
				log.error("Fail to get ACL filter of businessId[{}],: {}",
						businessId, e.getMessage());
			}
		return filter;
	}

	private ConditionOperator getOperator(PMSColumn column, String operator,
			Object value) {
		try {
			ConditionOperator oper;
			if (StrUtil.isBlank(operator)) {
				if ("like_or_in".equalsIgnoreCase(column.getOperator())) {
					if (value != null && value.toString().indexOf(",") > 0)
						oper = ConditionOperator.IN;
					else
						oper = ConditionOperator.LIKE;
				} else {
					oper = column.getOperatorObject();
				}
			} else {
				oper = ConditionOperator.getOperator(operator);
			}
			return oper;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	IFilter generateFilter(QueryConditions condition)

	{
		try {
			PMSQuery query;
			IFilter filter;
			Iterator i$;
			query = getQuery(condition.getQueryId());
			filter = null;
			if (StrUtil.isNotBlank(query.getFilter()))
				filter = new StringFilter(query.getFilter());
			i$ = condition.getConditions().iterator();
			// _L1:
			boolean l1 = true;
			while (l1) {
				if (!i$.hasNext())
					// break MISSING_BLOCK_LABEL_757;
					break; // û���µ�Column������,�˳�ѭ��.
				
				
				Map<String, Object> map = (Map) i$.next();
				String key = (String) map.get("key");
				Object value = map.get("value");
				PMSColumn column = query.getColumn(key);
				System.out.println("key,value=" + key + "," + value);

				// �ж��Ƿ�Ҫִ������Ĵ���
				if (value == null || !StrUtil.isNotBlank(value.toString())
						|| column == null || !column.isAllowSearch()) {
					continue;
				}

				ConditionOperator operator;
				Class clazz;
				String values[] = {};
				operator = getOperator(column, (String) map.get("operator"),
						value);

				clazz = JSonHelper.getClass(column.getType());

				// Step1:����values��������

				// if(!operator.equals(ConditionOperator.BETWEEN))
				// break MISSING_BLOCK_LABEL_313;
				// values = (new
				// StringBuilder()).append(value.toString()).append(" ").toString().split(",");
				// if(values.length != 0 && (!StrUtil.isBlank(values[0]) ||
				// !StrUtil.isBlank(values[1]))) goto _L2; else goto _L1
				if (operator.equals(ConditionOperator.BETWEEN)) {
					values = (new StringBuilder()).append(value.toString())
							.append(" ").toString().split(",");
				} else if (!operator.equals(ConditionOperator.IN)
						&& !operator.equals(ConditionOperator.NOT_IN)) {
					values = value.toString().split(",");
				} else {
					// ���������ŵ������,values����������
				}

				// _L2:
				// ��values ת����List����,�����list֮��

				Object newValue;
				List list;
				list = new ArrayList();
				String arr$[] = values;
				int len$ = arr$.length;
				for (int i$1 = 0; i$1 < len$; i$1++) {
					String v = arr$[i$1];
					if (StrUtil.isBlank(v))
						list.add(null);
					else
						list.add(JSonHelper.getValue(clazz, v));
				}

				// list�����ŵ�newValue����ȥ
				newValue = list;
				// break MISSING_BLOCK_LABEL_440;
				// if(!operator.equals(ConditionOperator.IN) &&
				// !operator.equals(ConditionOperator.NOT_IN))
				// break MISSING_BLOCK_LABEL_431;

				// list = new ArrayList();
				// String arr$1[] = values;
				// int len$1 = arr$1.length;
				// for(int i$1 = 0; i$1 < len$1; i$1++)
				// {
				// String v = arr$1[i$1];
				// if(StrUtil.isNotBlank(v))
				// list.add(JSonHelper.getValue(clazz, v));
				// }

				// if(list.size() != 0) goto _L3; else goto _L1
				// if(list.size() != 0){
				// //goto _L3;
				// //Do nothing then goto _L3;
				// }
				// else{
				// //goto _L1
				// continue; // continue then goto _L1;
				// }
				// _L3:
				// newValue = list;
				// break MISSING_BLOCK_LABEL_440;
				// newValue = JSonHelper.getValue(clazz, value);

				if ((java.util.Date.class.equals(clazz) || java.util.Date.class
						.equals(clazz.getSuperclass()))
						&& value.toString().indexOf(':') < 0)
					if (operator.equals(ConditionOperator.BETWEEN)) {
						list = (List) newValue;
						if (list.get(1) != null) {
							Object nextValue = DateUtil.getLastMilliSecond(list
									.get(1));
							list.set(1, nextValue);
						}
					} else if (operator.equals(ConditionOperator.EQ)
							|| operator.equals(ConditionOperator.GT)
							|| operator.equals(ConditionOperator.GE)) {
						Object nextValue = DateUtil
								.getLastMilliSecond(newValue);
						if (operator.equals(ConditionOperator.EQ)) {
							operator = ConditionOperator.BETWEEN;
							newValue = Arrays.asList(new Object[] { newValue,
									nextValue });
						} else {
							newValue = nextValue;
						}
					}
				// ��Զ�����ϵĶ��壬�������е�ʵ��ı���
				String alias = null;
//				if (query.getJoin().size() > 0 && key.indexOf(".") > 0) {
//					alias = key.substring(0, key.indexOf("."));
//					if (query.getJoinerPosition(alias) == -1)
//						alias = null;
//					else
//						key = key.substring(key.indexOf(".") + 1);
//				}
				// ���������newValue�Ĺ��,���newValue�Ժ�����newValue�����Filter
				// �ٽ����Filter��������γ�����ʹ�õ�Filter
				IFilter simpleFilter = null;
				simpleFilter = operator.getFilter(alias, key, newValue);
				if (simpleFilter != null)
					filter = simpleFilter.appendAnd(filter);
				// goto _L1
			}
			// _L1���������Filter�Ĵ�ѭ��,����ǵ�һ��ѭ���Ľ���

			// Exception e;
			// e;
			// log.error("Fail to generate filter:{}, with conditions {}",
			// query.getId(), condition.getConditions());
			// throw new InvalidFilterException("Fail to generate filter: ", e);
			System.out.println("filter:"+filter.getString());
			System.out.println("filter.values:"+filter.getValues());
			return filter;
		} catch (Exception e) {
			System.out.println("Exception happened." + e.toString());
			e.printStackTrace();
			return null;
		}
	}

	Integer queryCount(QueryConditions conditions) {
		PMSQuery query = getQuery(conditions.getQueryId());
		IFilter filter = getCompinedFilter(conditions);
		Integer result;
		if (StrUtil.isBlank(query.getDistinct()))
			result = getDao().getCount(query.getTargetClass(), filter);
		else
			result = getDao().getDistinctCount(query.getTargetClass(), filter,
					query.getDistinct());
		return result;
	}

	public Map getMetaInfo(String queryId) {
		PMSQuery query = getQuery(queryId);
		Map metadata = new HashMap();
		metadata.put("header", query.getHeader());
		metadata.put("order", query.getOrder());
		metadata.put("columns", query.getColumns());
		metadata.put("uniondatakey", query.getUniondatakey());
		return metadata;
	}

	public Map getCustomizedMetaInfo(String queryId, String pageUrl) {
		Map metadata = getMetaInfo(queryId);
		List columns = (List) metadata.get("columns");
		List selected = new ArrayList();
		List forSelection = new ArrayList();
		Iterator i$ = columns.iterator();
		do {
			if (!i$.hasNext())
				break;
			PMSColumn column = (PMSColumn) i$.next();
			if (column.isDisplay())
				forSelection.add(column.getKey());
		} while (true);
		Long userId = getUserId();
		QueryConfig queryConfig = getQueryConfig(queryId, pageUrl, userId);
		log.debug("Get QueryConfig for User[{}] of Query[{}] on Page[{}].",
				new Object[] { userId, queryId, pageUrl });
		String queryDataId = null;
		Long queryConfigId = null;
		List selectedNames = null;
		if (queryConfig != null) {
			queryDataId = queryConfig.getQueryId();
			queryConfigId = queryConfig.getId();
			selectedNames = queryConfig.getColumns();
			metadata.put("operatorShow", queryConfig.getOperatorShow());
		}
		if (selectedNames != null) {
			selected = selectedNames;
			forSelection.removeAll(selected);
		} else {
			selected = forSelection;
			forSelection = null;
		}
		metadata.put("selected", selected);
		metadata.put("unSelected", forSelection);
		metadata.put("queryDataId", queryDataId);
		metadata.put("queryConfigId", queryConfigId);
		return metadata;
	}

	private QueryConfig getQueryConfig(String queryId, String pageUrl,
			Long userId) {
		QueryConfig queryConfig = new QueryConfig();
		queryConfig.setPageName(pageUrl);
		queryConfig.setQueryId(queryId);
		queryConfig.setUserId(userId);
		List list = getDao().findByExample(queryConfig);
		if (list.size() == 0)
			return null;
		if (list.size() > 1)
			log.warn(
					"There is {} records( more than one) of Query Config with User[{}], Page[{}, Query[{}]",
					new Object[] { Integer.valueOf(list.size()), userId,
							pageUrl, queryId });
		return (QueryConfig) list.get(0);
	}

	private Long getUserId() {
		Long userId = null;
		UserSession session = SessionManager.getUserSession();
		if (session != null) {
			Map map = session.getSessionData();
			userId = (Long) map.get("userId");
		}
		return userId;
	}

	public Map executeQuery(QueryConditions conditions)
			throws InvalidFilterException {
		if (conditions.isCacheable())
			cacheQueryObj(conditions);
		return doQuery(conditions);
	}

	public String executeStatistics(QueryConditions conditions)
			throws InvalidFilterException {
		PMSQuery query = getQuery(conditions.getQueryId());
		if (query.hasStatistics()) {
			IFilter filter = getCompinedFilter(conditions);
			List results = getDao().queryProperties(query.getTargetClass(),
					filter, null, null, query.getStatString());
			List statItems = query.getStatItems();
			List statFormat = query.getStatFormat();
			int statItemsSize = statItems.size();
			if (results.size() > 0) {
				StringBuffer result = new StringBuffer();
				if (statItemsSize == 1) {
					result.append((String) statItems.get(0));
					result.append(processStatResult(results.get(0),
							(String) statFormat.get(0)).toString());
				} else {
					Object objects[] = (Object[]) (Object[]) results.get(0);
					for (int i = 0; i < statItemsSize; i++) {
						result.append(' ').append((String) statItems.get(i));
						result.append(processStatResult(objects[i],
								(String) statFormat.get(i)).toString());
						result.append(',');
					}

					result.deleteCharAt(result.length() - 1);
				}
				return result.toString();
			}
		}
		return "";
	}

	private Object processStatResult(Object object, String format) {
		if (object == null)
			return Integer.valueOf(0);
		if (format == null)
			return object;
		else
			return BeanUtil.formatByType(object, format);
	}

	private Map doQuery(QueryConditions conditions)
			throws InvalidFilterException {
		PMSQuery query = getQuery(conditions.getQueryId());
		String managerMethod = query.getMethod();
		if (StrUtil.isNotBlank(managerMethod))
			return invokeQueryMethod(managerMethod, conditions);
		IFilter filter = getCompinedFilter(conditions);
		FSP fsp = new FSP();
		fsp.setPage(conditions.getPageinfo());
		fsp.setUserFilter(filter);
		if (conditions.getSortinfo() == null && query.getSort() != null)
			fsp.setSort(query.getSort());
		else
			fsp.setSort(conditions.getSortinfo());
		List objects = null;
		if (StrUtil.isNotBlank(query.getDistinct()))
			objects = getDao().getDistinctList(query.getTargetClass(), fsp,
					query.getDistinct());
		else if (!query.getJoin().isEmpty()) {
			IJoin join = null;
			for (Iterator i$ = query.getJoin().iterator(); i$.hasNext();) {
				PMSJoin j = (PMSJoin) i$.next();
				IJoin tmp = JoinFactory.createJoin(j.getTargetClass(),
						j.getAlias(), j.getProperties().split(","),
						"_default_", j.getMainProperties().split(","), 3);
				if (join == null)
					join = tmp;
				else
					join = join.appendJoin(tmp);
			}

			objects = getDao().getListWithJoin(query.getTargetClass(), null,
					fsp, join);
		} else {
			objects = getDao().getList(query.getTargetClass(), fsp);
		}
		String statisticsInfo = executeStatistics(conditions);
		String header = query.getHeader();
		List dtos = convertObjects(query, objects);
		List unionDataKeyList = getUnionDataKeyList(dtos, query);
		Map map = new HashMap();
		String columnscurrentpage = null;
		Iterator i$ = conditions.getConditions().iterator();
		do {
			if (!i$.hasNext())
				break;
			Map condition = (Map) i$.next();
			if ("columnscurrentpage".equals(condition.get("key")))
				columnscurrentpage = (String) condition.get("value");
		} while (true);
		if (columnscurrentpage == null)
			map.put("columnscurrentpage", Integer.valueOf(1));
		else
			map.put("columnscurrentpage", columnscurrentpage);
		map.put("header", header);
		map.put("data", dtos);
		map.put("StatisticsInfo", statisticsInfo);
		map.put("pageinfo", fsp.getPage());
		map.put("sort", fsp.getSort());
		map.put("unionDataKeyList", unionDataKeyList);
		return map;
	}

	private Map invokeQueryMethod(String managerMethod,
			QueryConditions conditions) {
		Map map = null;
		if (managerMethod.indexOf('.') != -1) {
			String managerName = managerMethod.substring(0,
					managerMethod.indexOf('.'));
			String methodName = managerMethod.substring(managerMethod
					.indexOf('.') + 1);
			try {
				Object manager = SpringHelper.getBean(managerName);
				Method method = manager
						.getClass()
						.getMethod(
								methodName,
								new Class[] { com.cnpc.pms.base.query.json.QueryConditions.class });
				map = (Map) method.invoke(manager, new Object[] { conditions });
			} catch (Exception e) {
				log.error("Fail to invoke query method: {}", managerMethod, e);
				throw new PMSManagerException((new StringBuilder())
						.append("Error in invoke Query Method: ")
						.append(managerMethod).append(", Reason: ")
						.append(e.getMessage()).toString());
			}
		} else {
			throw new PMSManagerException((new StringBuilder())
					.append("Wrong format of Query Method definition: ")
					.append(managerMethod).toString());
		}
		return map;
	}

	private List convertObjects(PMSQuery query, List objects) {
		List results = new ArrayList();
		try {
			String dtoClassName = query.getDtoClass();
			if (StrUtil.isNotBlank(dtoClassName))
				results = BeanUtil.convertToBeans(query, objects);
			else
				results = BeanUtil.convertToMap(query, objects);
		} catch (Exception e) {
			log.error((new StringBuilder()).append("Convert entities error: ")
					.append(e.getMessage()).toString());
			e.printStackTrace();
		}
		return results;
	}

	private List getUnionDataKeyList(List list, PMSQuery query) {
		List keyList = new ArrayList();
		if (list == null)
			return keyList;
		String unionDataKey = query.getUniondatakey();
		if (unionDataKey != null)
			if (unionDataKey.indexOf(",") != -1) {
				String unionDataKeys[] = unionDataKey.split(",");
				for (int i = 0; i < unionDataKeys.length; i++) {
					String temp = unionDataKeys[i];
					String arr[] = temp.split("\\.");
					String unionDataKeysValue = "";
					for (int j = 0; j < list.size(); j++) {
						Object oList = list.get(j);
						Object o = null;
						if (oList instanceof HashMap) {
							String arr$[] = arr;
							int len$ = arr$.length;
							for (int i$ = 0; i$ < len$; i$++) {
								String tempKey = arr$[i$];
								Map map1 = (Map) oList;
								o = map1.get(tempKey);
							}

						} else {
							o = getFieldValueByName(unionDataKeys[i], oList);
						}
						unionDataKeysValue = (new StringBuilder())
								.append(unionDataKeysValue)
								.append(o.toString()).append(",").toString();
					}

					Map map = new HashMap();
					map.put("key", unionDataKeys[i].replaceAll("\\.", "-"));
					if (unionDataKeysValue.length() > 0)
						map.put("value", unionDataKeysValue.substring(0,
								unionDataKeysValue.lastIndexOf(",")));
					else
						map.put("value", "");
					keyList.add(map);
				}

			} else {
				Map map = new HashMap();
				String unionDataKeysValue = "";
				for (int j = 0; j < list.size(); j++) {
					Object o = getFieldValueByName(unionDataKey, list.get(j));
					unionDataKeysValue = (new StringBuilder())
							.append(unionDataKeysValue).append(o.toString())
							.append(",").toString();
				}

				map.put("key", unionDataKey);
				if (unionDataKeysValue.length() > 0)
					map.put("value",
							unionDataKeysValue.substring(0,
									unionDataKeysValue.lastIndexOf(",")));
				else
					map.put("value", "");
				keyList.add(map);
			}
		return keyList;
	}

	private Object getFieldValueByName(String fieldName, Object o) {
		try {
			Map mapObj = (Map) o;
			Object value = mapObj.get(fieldName);
			return value;
		} catch (ClassCastException classCastException) {
			try {
				String firstLetter = fieldName.substring(0, 1).toUpperCase();
				String getter = (new StringBuilder()).append("get")
						.append(firstLetter).append(fieldName.substring(1))
						.toString();
				Method method = o.getClass().getMethod(getter, new Class[0]);
				Object value = method.invoke(o, new Object[0]);
				return value;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	private HashMap getCachedMap() {
		HashMap cacheConditions = null;
		UserSession session = SessionManager.getUserSession();
		if (null == session) {
			log.info("Current session is null, we can not cache the query conditions");
			return new HashMap();
		}
		cacheConditions = (HashMap) session.get("queryRequestObjects");
		if (null == cacheConditions) {
			cacheConditions = new HashMap();
			session.put("queryRequestObjects", cacheConditions);
		}
		return cacheConditions;
	}

	private void cacheQueryObj(QueryConditions conditions) {
		log.debug("Cache query with key[{}] conditions:{}",
				conditions.getCacheKey(), conditions.getConditions());
		getCachedMap().put(conditions.getCacheKey(), conditions);
	}

	private QueryConditions getCachedQueryObj(String cacheKey) {
		QueryConditions conditions = (QueryConditions) getCachedMap().get(
				cacheKey);
		log.debug("Get cached query with key [{}] conditions:{}", cacheKey,
				conditions);
		return conditions;
	}
}
