package com.cnpc.pms.base.init.script;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnpc.pms.base.entity.EntityHelper;
import com.cnpc.pms.base.entity.IEntity;
import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.util.BeanUtil;
import com.cnpc.pms.base.util.DateUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.base.util.StrUtil;

/**
 * This class should be called after Spring Initialized.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author lushu
 * @since May 7, 2011
 */
public class EntityAgent extends ScriptableObject {

	static final String[] METHODS = { "createBy", "loadBy", "findAllBy", "getEntity", "save", "getDate", "halt",
			"importJava", "importBean", "detachEntity", "sleep" };

	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(EntityAgent.class);

	private final Map<String, Map<String, Object>> entityCache = new HashMap<String, Map<String, Object>>();

	private final static Map<Class<?>, Class<?>> PRIMITIVE_MAP = new HashMap<Class<?>, Class<?>>();
	static {
		PRIMITIVE_MAP.put(float.class, Float.class);
		PRIMITIVE_MAP.put(double.class, Double.class);
		PRIMITIVE_MAP.put(int.class, Integer.class);
		PRIMITIVE_MAP.put(long.class, Long.class);
		PRIMITIVE_MAP.put(char.class, Character.class);
		PRIMITIVE_MAP.put(byte.class, Byte.class);
		PRIMITIVE_MAP.put(boolean.class, Boolean.class);
		PRIMITIVE_MAP.put(short.class, Short.class);
	}

	public EntityAgent() {
		defineFunctionProperties(METHODS, this.getClass(), ScriptableObject.DONTENUM);
	}

	/**
	 * Create an entity with the giving type and params.
	 * 
	 * @param entityName
	 *            - Entity Class Name
	 * @param params
	 *            - wrapped params
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws InvalidFilterException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public Object createBy(String entityName, NativeObject params) throws ClassNotFoundException, SecurityException,
			InvalidFilterException, IllegalAccessException, InstantiationException, IllegalArgumentException,
			NoSuchMethodException, InvocationTargetException {
		Map<String, Object> map = getEntityMetaData(entityName);
		Object[] obj = getManageContext(entityName, params);
		IManager manager = (IManager) obj[0];
		IFilter filter = (IFilter) obj[1];
		Object entity = null;
		if (filter != null) {
			entity = manager.getUniqueObject(filter);
		}
		log.debug("The target entity is: {}", entity);
		if (entity == null) {
			Class<?> entityClass = (Class<?>) map.get("entityClass");
			Map<String, Class<?>> fieldsType = (Map<String, Class<?>>) map.get("fieldsType");
			entity = entityClass.newInstance();
			assembleEntity(entity, fieldsType, params);
		}
		log.debug("We get entity: {} with {}", entity, params);
		NativeObject agent = new NativeObject();
		agent.defineProperty("entity", entity, ScriptableObject.DONTENUM);
		agent.defineProperty("metadata", map, ScriptableObject.DONTENUM);
		return agent;
	}

	/**
	 * Load an entity with the giving type and params.
	 * 
	 * @param entityName
	 *            - Entity Class Name
	 * @param params
	 *            - wrapped params
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws InvalidFilterException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public Object loadBy(String entityName, NativeObject params) throws ClassNotFoundException, SecurityException,
			InvalidFilterException, IllegalAccessException, InstantiationException, IllegalArgumentException,
			NoSuchMethodException, InvocationTargetException {
		Object[] obj = getManageContext(entityName, params);
		IManager manager = (IManager) obj[0];
		IFilter filter = (IFilter) obj[1];
		Object entity = null;
		if (filter != null) {
			// Class<?> entityClass = EntityHelper.getClass(entityName);
			// AlternativeDS ds = entityClass.getAnnotation(AlternativeDS.class);
			// if (DataSourceConfigurer.isMainDataSource(ds) == false) {
			// CurrentDSContext.setCurrent(ds.source());
			// log.debug("Alternative DataSource Query, source[{}], change the current DS Context", ds);
			// }

			entity = manager.getUniqueObject(filter);

			// if (DataSourceConfigurer.isMainDataSource(ds) == false) {
			// CurrentDSContext.switchToDefault();
			// }

		}
		if (entity == null) {
			log.warn("We get NULL Object for {}, with {}", entityName, params);
			return Undefined.instance;
		} else {
			log.debug("We get entity: {} with {}", entity, params);
			NativeObject agent = new NativeObject();
			agent.defineProperty("entity", entity, ScriptableObject.DONTENUM);
			return agent;
		}
	}

	/**
	 * Find all entity objects with the giving type and params.
	 * 
	 * @param entityName
	 *            - Entity Class Name
	 * @param params
	 *            - wrapped params
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InvalidFilterException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 */
	public List<?> findAllBy(String entityName, NativeObject params) throws ClassNotFoundException,
			InvalidFilterException, SecurityException, IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		Object[] obj = getManageContext(entityName, params);
		IManager manager = (IManager) obj[0];
		IFilter filter = (IFilter) obj[1];
		List<?> list = null;
		if (filter != null) {
			list = manager.getObjects(filter);
		}
		return list;
	}

	/**
	 * Get an updated but not flushed entity from the JavaScript Object
	 * 
	 * @param o
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public Object getEntity(Object o) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		Object[] obj = getEntityAndManager(o);
		Object entity = obj[1];
		return entity;
	}

	/**
	 * Detach an entity from the current wrapper. User for getEntity without trigger the hibernate flush.
	 * 
	 * @param o
	 * @throws Exception 
	 */
	public void detachEntity(Object o) throws Exception {
		Object[] obj = getEntityAndManager(o);
		Object entity = obj[1];
		Object newEntity = entity.getClass().newInstance();
		BeanUtil.copyProperties(newEntity, entity,new ArrayList());
		NativeObject agent = (NativeObject) o;
		agent.defineProperty("entity", newEntity, ScriptableObject.DONTENUM);
	}

	/**
	 * Save a wrapped entity
	 * 
	 * @param o
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public void save(Object o) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		Object[] obj = getEntityAndManager(o);
		IManager manager = (IManager) obj[0];
		Object entity = obj[1];
		log.debug("Save entity[{}]", entity);
		manager.saveObject(entity);
	}

	/**
	 * Remove a wrapped entity
	 * 
	 * @param o
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public void remove(Object o) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		Object[] obj = getEntityAndManager(o);
		IManager manager = (IManager) obj[0];
		Object entity = obj[1];
		log.debug("Remove entity[{}]", entity);
		manager.removeObject(entity);
	}

	/**
	 * Get the Java Date Object
	 * 
	 * @param o
	 *            - the String contains the Date
	 * @param format
	 *            - the date format
	 * @return
	 * @throws ParseException
	 */
	public Date getDate(Object o, String format) throws ParseException {
		Object javaObj = Context.jsToJava(o, Object.class);
		return DateUtil.parseDate(String.valueOf(javaObj), format);
	}

	/**
	 * Halt the import operation when error occurs.
	 * 
	 * @param msg
	 * @throws Exception
	 */
	public void halt(String msg) throws ScriptExecutionException {
		log.error("Data import ERROR: {}", msg);
		throw new ScriptExecutionException(msg);
	}

	public void sleep(String time) throws InterruptedException {
		Thread.sleep(Long.valueOf(time));
	}

	/**
	 * Get the manager and native entity from the wrapped object, used for save(), remove()
	 * 
	 * @param o
	 *            - NativeObject
	 * @return - Object[] with {manager, entity }
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	private Object[] getEntityAndManager(Object o) throws SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		NativeObject agent = (NativeObject) o;
		Map<String, Object> map = (Map<String, Object>) agent.get("metadata", null);
		Object entity = agent.get("entity", null);
		Map<String, Class<?>> fieldsType = (Map<String, Class<?>>) map.get("fieldsType");
		assembleEntity(entity, fieldsType, o);
		IManager manager = (IManager) map.get("manager");
		return new Object[] { manager, entity };
	}

	/**
	 * Assemble the entity from the wrapped object
	 * 
	 * @param entity
	 *            - the target entity object
	 * @param fieldsType
	 *            - the fields type of the entity
	 * @param o
	 *            - wrapped object
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private void assembleEntity(Object entity, Map<String, Class<?>> fieldsType, Object o) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			InstantiationException {
		NativeObject agent = (NativeObject) o;
		for (Object key : agent.getAllIds()) {
			Class<?> type = fieldsType.get(key.toString());
			if (type == null)
				continue;
			Object rawValue = agent.get(key.toString(), null);
			log.debug("Get {} with raw value {}", key, rawValue);
			if (rawValue == null || rawValue.getClass() == Undefined.class)
				continue;
			Object value = getJavaObject(type, rawValue);
			log.debug("Set {}[{}] with value {}", new Object[] { key, type, value });
			Method method = entity.getClass().getMethod("set" + StrUtil.capitalize(key.toString()), type);
			if (value != null) {
				method.invoke(entity, value);
			}
		}
	}

	private Object getJavaObject(Class<?> type, Object rawValue) throws SecurityException, IllegalArgumentException,
			NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Object value;
		String convertType = "Default Mode";
		if (java.util.Date.class.equals(type) || java.util.Date.class.equals(type.getSuperclass())) {
			value = getDiversedDateValue(rawValue, type);
			convertType = "Date type";
		} else if (java.lang.String.class.equals(type)) {
			value = getString(rawValue);
			convertType = "String type";
		} else if (type.isPrimitive()) {
			value = getNumberValue(rawValue, getWrappedClass(type));
			convertType = "Boxed Primitive type";
		} else if (PRIMITIVE_MAP.containsValue(type)) {
			value = getNumberValue(rawValue, type);
			convertType = "Primitive type";
		} else if (IEntity.class.isAssignableFrom(type)) {
			NativeObject entityAgent = (NativeObject) rawValue;
			value = entityAgent.get("entity", null);
			convertType = "Wrapped Entity";
		} else {
			value = getObject(rawValue);
		}
		log.debug("-->Convert Type: {}", convertType);
		return value;
	}

	private Object getDiversedDateValue(Object raw, Class<?> clazz) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Object tmp = Context.jsToJava(raw, Date.class);
		long time = ((Date) tmp).getTime();
		Constructor<?> constructor = clazz.getConstructor(long.class);
		Object value = constructor.newInstance(time);
		return value;
	}

	private String getString(Object o) {
		return String.valueOf(getObject(o)).trim();
	}

	private static Class<?> getWrappedClass(Class<?> clazz) {
		Class<?> wrappedClass = PRIMITIVE_MAP.get(clazz);
		log.debug("original class [{}], wrapped with [{}]", clazz, wrappedClass);
		return wrappedClass;
	}

	private Object getNumberValue(Object raw, Class<?> clazz) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		String tmp = Context.toString(raw).trim();
		if (tmp.length() == 0)
			return null;
		if (tmp.endsWith(".0")) {
			tmp = tmp.substring(0, tmp.length() - 2);
		}
		Constructor<?> constructor = clazz.getConstructor(String.class);
		Object value = constructor.newInstance(tmp);
		return value;
	}

	private Object getObject(Object o) {
		return Context.jsToJava(o, Object.class);
	}

	private Map<String, Object> getEntityMetaData(String entityName) throws ClassNotFoundException,
			IllegalAccessException, InstantiationException {
		Map<String, Object> map = entityCache.get(entityName);
		if (map == null) {
			log.debug("Start to register new Entity: {}", entityName);
			map = new HashMap<String, Object>();
			init(entityName, map);
			entityCache.put(entityName, map);
		}
		log.debug("Return metadata of Entity: {}", entityName);
		return map;
	}

	private void init(String entityName, Map<String, Object> map) throws ClassNotFoundException,
			IllegalAccessException, InstantiationException {
		Class<?> entityClass = EntityHelper.getClass(entityName);
		if (entityClass == null) {
			entityClass = Class.forName(entityName);
		}
		log.debug("Initialize for Entity class: {}", entityClass.getCanonicalName());
		Map<String, Class<?>> fieldsType = new HashMap<String, Class<?>>();
		for (Class<?> clazz = entityClass; clazz != null; clazz = clazz.getSuperclass()) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				log.debug(" |-Register field [{}] with type: {}", field.getName(), field.getType().getCanonicalName());
				fieldsType.put(field.getName(), field.getType());
			}
		}

		// Get specific manager
		String managerName = SpringHelper.getManagerNameByEntity(entityName);
		IManager manager = SpringHelper.getManagerByClass(managerName);
		// boolean alternativeDS = false;
		if (manager != null) {
			if (manager.getEntityClass() == null) {
				log.error("Unset entityclass{} in manager", entityName, new Exception("Unset entityClass"));
			}
			manager.setEntityClass(entityClass);
		} else {
			// For a non-existent Manager, we will create a new instance of BaseManager and
			// set the entity class and basedao.
			// Since 2011-12-22, the missing manager will be created in PMSConfigHandler
			log.error("Unset Manager for [{}]", entityName, new Exception("Unset Manager"));
			// alternativeDS = ClassUtils.getAllInterfacesForClassAsSet(entityClass).contains(IAlternativeDS.class);
			// String suffix = null;
			// if (alternativeDS) {
			// suffix = ((IAlternativeDS) entityClass.newInstance()).getTargetDS();
			// }
			// String daoBeanName = "basedao";
			// if (StrUtil.isNotBlank(suffix)) {
			// daoBeanName = daoBeanName + "_" + suffix;
			// }
			// manager = new BaseManagerImpl();
			// ((BaseManagerImpl) manager).setDao((IDAO) SpringHelper.getBean(daoBeanName));
			// manager.setEntityClass(entityClass);
		}
		// log.debug("Get Entity Manager: {}. Alternative DS? {} ", manager, alternativeDS);
		log.debug(" \\-Get Entity Manager: {}.  ", manager);
		map.put("entityClass", entityClass);
		map.put("fieldsType", fieldsType);
		map.put("manager", manager);
		// map.put("alternativeDS", alternativeDS);
	}

	/**
	 * Get the manager-layer operation context, for loadBy, findAllBy
	 * 
	 * @param entityName
	 *            - the entity class name
	 * @param params
	 *            - the wrapped params
	 * @return - Object[]{manager,filter}
	 * @throws ClassNotFoundException
	 * @throws InvalidFilterException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 */
	@SuppressWarnings("unchecked")
	private Object[] getManageContext(String entityName, NativeObject params) throws ClassNotFoundException,
			InvalidFilterException, SecurityException, IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		Map<String, Object> map = getEntityMetaData(entityName);
		IManager manager = (IManager) map.get("manager");
		Map<String, Class<?>> fieldsType = (Map<String, Class<?>>) map.get("fieldsType");
		IFilter filter = null;
		if (params != null) {
			for (Object name : params.getIds()) {
				Class<?> type = fieldsType.get(name.toString());
				Object rawValue = params.get(name.toString(), null);
				if (type != null) {
					Object value = getJavaObject(type, rawValue);// getObject(params.get(name.toString(), null));
					log.debug("get field[{}] with Type[{}] Value[{}]", new Object[] { name, type, value });
					filter = FilterFactory.getEq(name.toString(), value).appendAnd(filter);
				} else
					log.warn("Didn't find field[{}] in {}", name, entityName);
			}
		}
		return new Object[] { manager, filter };
	}

	@Override
	public String getClassName() {
		return "EntityAgent";
	}

	public void importJava(String className, String beanName, boolean factory) throws ClassNotFoundException,
			IllegalAccessException, InstantiationException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException {
		if (ScriptableObject.hasProperty(this, beanName)) {
			log.debug("The property {} is alreay registerd. Skiped.", beanName);
		} else {
			Class<?> clazz = Class.forName(className);
			Object object;
			if (factory) {
				Method getInstance = clazz.getDeclaredMethod("getInstance");
				object = getInstance.invoke(null);
			} else {
				object = clazz.newInstance();
			}
			Object wrappedBean = Context.javaToJS(object, this);
			ScriptableObject.putProperty(this, beanName, wrappedBean);
		}
	}

	/**
	 * Import Spring Beans into JavaScript Context
	 * 
	 * @param injectBeans
	 */
	public void importBean(String injectBeans) {
		if (StrUtil.isBlank(injectBeans) == false) {
			String[] beans = injectBeans.split(",");
			for (String beanName : beans) {
				if (ScriptableObject.hasProperty(this, beanName)) {
					log.warn("The property {} is alreay registerd. Skiped.", beanName);
				} else {
					Object javaBean = SpringHelper.getBean(beanName);
					if (javaBean == null) {
						log.error("Fail to find the inject bean: {}", beanName);
					} else {
						Object wrappedBean = Context.javaToJS(javaBean, this);
						ScriptableObject.putProperty(this, beanName, wrappedBean);
					}
				}
			}
		}
	}
}
