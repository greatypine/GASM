package com.cnpc.pms.personal.manager;

import java.util.List;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;
import com.cnpc.pms.personal.entity.SysUserGroupOpera;

public interface SysUserGroupOperaManager extends IManager{
	
	public SysUserGroupOpera queryGroupOperaById(Long id);
	
	public SysUserGroupOpera saveSysUserGroupOpera(SysUserGroupOpera sysUserGroupOpera);
	
	public SysUserGroupOpera querySysUserGroupOperasByGroupId(Long sys_usergroup_id);
	
	public List<UserGroup> querySysGroupByCurUser();
	
	public User saveSysUser(User user);
	
	
	public UserDTO queryUserById(Long userid);
}
