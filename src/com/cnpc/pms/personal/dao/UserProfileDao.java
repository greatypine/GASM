package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dto.UserProfileDto;

/**
 * @Function：用户档案Dao
 * @author：chenchuang
 * @date:2018年3月8日 下午3:30:38
 *
 * @version V1.0
 */
public interface UserProfileDao {

	public Map<String, Object> queryUserProfile(UserProfileDto userProfile,PageInfo pageInfo);
	
	public List<Map<String, Object>> exportUserProfile(UserProfileDto userProfile);
	
}
