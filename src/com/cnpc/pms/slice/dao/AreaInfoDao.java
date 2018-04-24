package com.cnpc.pms.slice.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.slice.dto.AreaDto;
import com.cnpc.pms.slice.entity.Area;
import com.cnpc.pms.slice.entity.AreaInfo;

/**
 * 划片
 * @author gaobaolei
 *
 */
public interface AreaInfoDao {
	
	public List<Map<String, Object>> queryAreaInfoByAreaId(String area_id);
	
	List<AreaInfo> findAreaInfoByString(String idString);
	
	List<Map<String, Object>> findAreaInfoBytinyId(Long tinyId);
}
