package com.cnpc.pms.utils;

/**
 * 值类型获取
 * Created by liu on 2016/7/7 0007.
 */
public class ValueUtil {
    /**
     * 检查值是否为空
     * @param value 值对象
     * @return 返回结果 null 返回false，不为空返回true
     */
    public static Boolean checkValue(Object value){
        if(null == value || "".equals(value)){
            return false;
        }
        return true;
    }

    /**
     * 检查值是否为空
     * @param value 值对象
     * @return 返回结果 null 返回false，不为空返回true
     */
    public static String getStringValue(Object value){
        if(null == value || "".equals(value)){
            return "";
        }
        return value.toString();
    }
}
