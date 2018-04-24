package com.cnpc.pms.base.cache.support;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tree map sort implementation.
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2013-1-10
 */
public class TreeMapSort implements ISortStrategy {
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.cache.support.ISortStrategy#sort(java.util.Map, java.lang.String)
	 */
	public List sort(Map<Object, Map> datas, String sortField) {
		long begin = System.currentTimeMillis();
		List sortedKey = new ArrayList<String>();
		CombinedKey combinedKey = null;
		TreeMap<CombinedKey, Map> treeDatas = new TreeMap<CombinedKey, Map>(new FieldComparator());
		for (Entry<Object, Map> entry : datas.entrySet()) {
			combinedKey = new CombinedKey();
			combinedKey.setKey(entry.getKey());
			combinedKey.setSortFieldValue(entry.getValue().get(sortField));
			treeDatas.put(combinedKey, entry.getValue());
		}
		Iterator<CombinedKey> iter = treeDatas.keySet().iterator();
		while (iter.hasNext()) {
			sortedKey.add(iter.next().getKey());
		}
		long end = System.currentTimeMillis();
		this.log.debug("sort time:" + (end - begin) + "(ms)");
		return sortedKey;
	}

	private class FieldComparator<T> implements Comparator<T> {

		/*
		 * (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(T o1, T o2) {
			// TODO Auto-generated method stub
			Object value1 = ((CombinedKey) o1).getSortFieldValue();
			Object value2 = ((CombinedKey) o2).getSortFieldValue();

			if (value1 instanceof Integer && value2 instanceof Integer) {
				return ((Integer) value1).compareTo((Integer) value2);
			} else if (value1 instanceof Long && value2 instanceof Long) {
				return ((Long) value1).compareTo((Long) value2);
			} else if (value1 instanceof Float && value2 instanceof Float) {
				return ((Float) value1).compareTo((Float) value2);
			} else if (value1 instanceof Double && value2 instanceof Double) {
				return ((Double) value1).compareTo((Double) value2);
			} else {
				return ((String) value1).compareToIgnoreCase((String) value2);
			}
		}

	}

	private class CombinedKey {
		Object key;

		public Object getKey() {
			return key;
		}

		public void setKey(Object key) {
			this.key = key;
		}

		public Object getSortFieldValue() {
			return sortFieldValue;
		}

		public void setSortFieldValue(Object sortFieldValue) {
			this.sortFieldValue = sortFieldValue;
		}

		Object sortFieldValue;
	}
}
