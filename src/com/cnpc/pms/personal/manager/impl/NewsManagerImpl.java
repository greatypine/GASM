package com.cnpc.pms.personal.manager.impl;

import java.util.List;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.entity.News;
import com.cnpc.pms.personal.manager.NewsManager;

public class NewsManagerImpl extends BizBaseCommonManager implements NewsManager{

	@Override
	public News getNewsBy(Integer type_id, Long key_id) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("type_id = "+type_id + " and key_id = "+key_id));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (News)lst_vilage_data.get(0);

        }
        return null;
	}

}
