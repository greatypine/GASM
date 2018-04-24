package com.cnpc.pms.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityEquals {

	  /**
     * 源目标为非MAP类型时
     * @param source
     * @param target
     * @param rv
     * @return
     */
	public static boolean classOfSrc(Object source, Object target,List<String> changefields) {
    	boolean rv=true;
        Class<?> srcClass = source.getClass();
        Field[] fields = srcClass.getDeclaredFields();
        for (Field field : fields) {
            String nameKey = field.getName();
            if(changefields.contains(nameKey)){
            	if (target instanceof Map) {
                    HashMap<String, String> tarMap = new HashMap<String, String>();
                    tarMap = (HashMap) target;
                    String srcValue = getClassValue(source, nameKey) == null ? "" : getClassValue(source, nameKey)
                            .toString();
                    if(tarMap.get(nameKey)==null){
                        rv = false;
                        break;
                    }
                    if (!tarMap.get(nameKey).equals(srcValue)) {
                        rv = false;
                        break;
                    }
                } else {
                    String srcValue = getClassValue(source, nameKey) == null ? "" : getClassValue(source, nameKey)
                            .toString();
                    String tarValue = getClassValue(target, nameKey) == null ? "" : getClassValue(target, nameKey)
                            .toString();
                    if (!srcValue.equals(tarValue)) {
                        rv = false;
                        break;
                    }
                }
            }
        }
        return rv;
    }

    /**
     * 根据字段名称取值
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getClassValue(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }
        try {
            Class beanClass = obj.getClass();
            Method[] ms = beanClass.getMethods();
            for (int i = 0; i < ms.length; i++) {
                // 非get方法不取
                if (!ms[i].getName().startsWith("get")) {
                    continue;
                }
                Object objValue = null;
                try {
                    objValue = ms[i].invoke(obj, new Object[] {});
                } catch (Exception e) {
                     System.out.println("反射取值出错："+e.getMessage());
                    continue;
                }
                if (objValue == null) {
                    continue;
                }
                if (ms[i].getName().toUpperCase().equals(fieldName.toUpperCase())
                        || ms[i].getName().substring(3).toUpperCase().equals(fieldName.toUpperCase())) {
                    return objValue;
                } else if (fieldName.toUpperCase().equals("SID")
                        && (ms[i].getName().toUpperCase().equals("ID") || ms[i].getName().substring(3).toUpperCase()
                                .equals("ID"))) {
                    return objValue;
                }
            }
        } catch (Exception e) {
            // logger.info("取方法出错！" + e.toString());
        }
        return null;
    }
}
