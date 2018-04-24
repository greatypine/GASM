package com.cnpc.pms.base.file.comm.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Function：集合类工具，用于各种集合类的处理
 * @author：chenchuang
 * @date:2017-12-27 上午10:04:12
 *
 * @version V1.0
 */
public class CollectionUtils {

	/**
	 * List<Map<key,value>>转换成List<value>
	 * @param targetList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<String> listConvert(List<Map<String, Object>> targetList){
		List<String> result = new ArrayList<String>();
		for (Map m : targetList) {  
            Iterator<Map.Entry<Object, Object>> it = m.entrySet().iterator();  
            while (it.hasNext()) {  
                Map.Entry<Object, Object> entry = it.next();  
                result.add(entry.getValue().toString());
            }  
        }  
		return result;
	}
	
}
