package com.cnpc.pms.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtil {
	
	/**
	 * 获取字符串的数字组
	 * @param strValue
	 * @return
	 */
	public static List<String> getNumberGroup(String strValue){
		  Pattern p=Pattern.compile("(\\d+)");
		  Matcher m=p.matcher(strValue);
		  List<String> resultList = new ArrayList<String>(); 
		  while(m.find()){
			  resultList.add(m.group());
		  }
		  return resultList;
	}
	
	/**
	 * 比较两个list集合中数据是否相同
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
	    if(a.size() != b.size())
	        return false;
	    Collections.sort(a);
	    Collections.sort(b);
	    for(int i=0;i<a.size();i++){
	        if(!a.get(i).equals(b.get(i)))
	            return false;
	    }
	    return true;
	}
}
