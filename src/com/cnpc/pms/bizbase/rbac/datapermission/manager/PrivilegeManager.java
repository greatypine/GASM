package com.cnpc.pms.bizbase.rbac.datapermission.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cnpc.pms.base.dict.entity.Dict;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.bizbase.rbac.datapermission.dto.ConditionDTO;
import com.cnpc.pms.bizbase.rbac.datapermission.dto.PrivilegeDTO;

/**
 * 数据权限接口
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-31
 */
public interface PrivilegeManager extends IManager {

	/**
	 * 添加一条权限. 
	 * 
	 * @param privilege the privilege
	 * @return the privilege dto
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public PrivilegeDTO addPrivilege(PrivilegeDTO privilege) throws IOException;

	/**
	 * 通过id获取privilege实体.
	 * 
	 * @param id the id
	 * @return the privilege dto
	 */
	public PrivilegeDTO getPrivilegeDTO(Long id);

	/**
	 * 修改PrivilegeDTO基本信息,不包括conditions.
	 * 
	 * @param dto the dto
	 * @return the privilege dto
	 */
	public PrivilegeDTO modifyPrivilege(PrivilegeDTO dto);

	/**
	 * 修改ConditonDTO.
	 * 
	 * @param condition the condition
	 * @return the condition dto
	 * @throws IOException
	 */
	public ConditionDTO modifyConditionDTO(ConditionDTO condition) throws IOException;

	/**
	 * 通过id, 获取ConditionDTO.
	 * 
	 * @param id the id
	 * @return the condition dto
	 */
	public ConditionDTO getConditionDTO(Long id);

	/**
	 * 为角色组分配数据权限.
	 * 
	 * @param privileges the privileges
	 * @return the sets the
	 */
	public Set<Map> updateUserGroupPivilege(Set<Map> privileges);

	/**
	 * 为数据权限添加条件.
	 * 
	 * @param conditions the conditions
	 * @return the sets the
	 * @throws IOException
	 */
	public Set<Map> addCondition(Set<Map> conditions) throws IOException;

	/**
	 * 获取所有数据权限(并判断是否已非配给当前角色组).
	 * 
	 * @param id the id
	 * @return the privilege
	 */
	public List<PrivilegeDTO> getPrivilege(Long id);

	/**
	 * 获取角色组所拥有的数据权限.
	 * 
	 * @param id the id
	 * @return the user group privilege
	 */
	public List<PrivilegeDTO> getUserGroupPrivilege(Long id);

	/**
	 * 角色组数据权限修改,先删除原有的再添加重新分配的.
	 * 
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean deleteBeforeAssign(Long id);

	/**
	 * 删除权限条件.
	 * 
	 * @param queryId the query id
	 * @param id the id
	 * @throws IOException
	 */
	public void removeCondition(String queryid, Long id) throws ClassNotFoundException, IOException;

	/**
	 * 获取用户可分配的数据权限列表
	 * 
	 * @param privilegeType 数据权限类型
	 * @param dictName 字典表名
	 */
	public List<Dict> getListByUserPrivilege(String privilegeType, String dictName);
}
