package com.cnpc.pms.personal.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dto.UserProfileDto;

/**
 * @Function：用户档案Manager
 * @author：chenchuang
 * @date:2018年3月8日 上午11:32:40
 *
 * @version V1.0
 */
public interface UserProfileManager extends IManager {

	public Map<String, Object> queryUserProfile(UserProfileDto userProfile,PageInfo pageInfo);
	
	public Map<String, Object> exportUserProfile(UserProfileDto userProfile);
}
