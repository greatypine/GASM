package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.PublicOrder;

public interface PublicOrderDao extends IDAO{
	
    public Map<String, Object> queryPublicOrder(PublicOrder publicOrder,PageInfo pageInfo);

    public List<Map<String, Object>> exportPublicOrder(PublicOrder publicOrder);
    
    public Map<String, Object> querySearchOrder(PublicOrder publicOrder,
			PageInfo pageInfo);
    
    public List<Map<String, Object>> exportSearchOrder(PublicOrder publicOrder);

}
