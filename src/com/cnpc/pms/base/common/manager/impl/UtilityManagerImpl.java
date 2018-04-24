package com.cnpc.pms.base.common.manager.impl;

import java.util.List;

import com.cnpc.pms.base.common.manager.UtilityManager;
import com.cnpc.pms.base.common.model.ClientSuggestObject;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.paging.operator.ConditionOperator;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.base.util.StrUtil;

public class UtilityManagerImpl extends BaseManagerImpl implements UtilityManager {

	public String getSuggestion(ClientSuggestObject suggestObj) {
		String[] properties = suggestObj.getProperties();
		String resultString = "";
		if (properties != null && properties.length > 0) {
			PageInfo page = new PageInfo();
			page.setRecordsPerPage(suggestObj.getReturnSize());
			String formattedKeyword = formatQueryKeyword(suggestObj.getKeyword());
			IFilter filter = FilterFactory.getFilter(properties[0], formattedKeyword, ConditionOperator.LIKE);
			String selectItems = properties[0];
			ISort sort = SortFactory.createSort(properties[0]);
			for (int i = 1; i < properties.length; i++) {
				filter = filter.appendOr(FilterFactory.getFilter(properties[i], formattedKeyword,
						ConditionOperator.LIKE));
				selectItems = selectItems + "," + properties[i];
			}
			IManager manager = SpringHelper.getManagerByClass(SpringHelper.getManagerNameByEntity(suggestObj
					.getClassName()));
			List<?> results = getDao().queryProperties(manager.getEntityClass(), filter, sort, page, selectItems);
			if (results != null) {
				for (Object result : results) {
					String tmp = "";
					if (result instanceof Object[]) {
						tmp = StrUtil.joinArray((Object[]) result, ",");
					} else {
						tmp = result.toString();
					}
					resultString = resultString + tmp + "\r\n";
				}
			}
		}
		return resultString;
	}

	/**
	 * Format query keyword.
	 * 
	 * @param keyword
	 *            the keyword
	 * @return the string
	 */
	private String formatQueryKeyword(String keyword) {
		if (StrUtil.isEmpty(keyword)) {
			return "%%";
		}
		if (!keyword.startsWith("%")) {
			keyword = "%" + keyword;
		}
		if (!keyword.endsWith("%")) {
			keyword = keyword + "%";
		}
		return keyword;
	}
}
