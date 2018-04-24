/**  
* @Title: ExpertUserManager.java
* @Package com.cnpc.pms.bizbase.rbac.usermanage.manager
* @Description: TODO(用一句话描述该文件做什么)
* @author zhaobinbin
* @date 2013-9-9 下午03:54:56
*/ 
package com.cnpc.pms.bizbase.rbac.usermanage.manager;

import java.util.List;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.ExpertUserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.ExpertUser;

/**
 * @ClassName: ExpertUserManager
 * @Description:TODO(专家manager)
 * @author zhaobinbin
 * @date 2013-9-9 下午03:54:56
 */
public interface ExpertUserManager extends IManager{
	public ExpertUserDTO saveExpertUser(ExpertUserDTO dto);
	public ExpertUserDTO excuteExpertUser(Long id);
	
	public List<ExpertUser> excuteExpertUsers(String ids);
}
