
package com.cnpc.pms.bizbase.rbac.resourcemanage.manager.impl;

import com.cnpc.pms.base.entity.PMSAuditEntity;
import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.QueryDefinition;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.query.model.PMSQuery;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.funcpermision.entity.RoleAppFunc;
import com.cnpc.pms.bizbase.rbac.funcpermision.entity.RoleFunc;
import com.cnpc.pms.bizbase.rbac.funcpermision.manager.RoleAppFuncManager;
import com.cnpc.pms.bizbase.rbac.funcpermision.manager.RoleFuncManager;
import com.cnpc.pms.bizbase.rbac.orgview.entity.PurStruOrg;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.AddFunctionDTO;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.AppFunctionDTO;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.FunctionDTO;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.Status;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.AppFunction;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.Function;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.RoleFunctionView;
import com.cnpc.pms.bizbase.rbac.resourcemanage.manager.AppFunctionManager;
import com.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupRoleManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.pingplusplus.model.App;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn 功能权限实现类
 * 
 * @author IBM
 * @since 2011-4-19
 */


public class AppFunctionManagerImpl extends BaseManagerImpl implements AppFunctionManager {

	@Override
	public List<AppFunctionDTO> getAllFuncTree() {
		AppFunction function = (AppFunction) this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
		List<AppFunctionDTO> nodes = new ArrayList<AppFunctionDTO>();
		if (function != null && function.getChilds() != null) {
            AppFunctionDTO node = new AppFunctionDTO();
			node.setId(function.getId());
			node.setActivityCode(function.getActivityCode());
			node.setActivityName(function.getActivityName());
			if (function.getChilds().size() > 0) {
				node.setIsParent(true);
			}
			nodes.add(node);
		}
		Collections.sort(nodes);
		return nodes;
	}

    public List<AppFunctionDTO> getAllChildsByParentId(Long parentId) {
        AppFunction root = (AppFunction) this.getObject(parentId);
        if (null == root) {
            return null;
        }
        List<AppFunctionDTO> modules = new ArrayList<AppFunctionDTO>();
        Iterator<AppFunction> iterator = root.getChilds().iterator();
        while (iterator.hasNext()) {
            AppFunction child = iterator.next();
            AppFunctionDTO dto = new AppFunctionDTO();
            dto.setId(child.getId());
            dto.setActivityName(child.getActivityName()+(child.getRemark()==null?"":"("+child.getRemark()+")"));
//			dto.setActivityName(child.getActivityName());
            dto.setPath(child.getPath());
            dto.setActivityCode(child.getActivityCode());
            // 判断时候是否有下级为授权树节点加是父节点的标记
            if (child.getChilds().size() > 0) {
                dto.setIsParent(true);
            }
            modules.add(dto);
        }

        return modules;
    }

    public AppFunctionDTO getFunctionById(Long id) {

        AppFunction func = (AppFunction) this.getObject(id);
        AppFunctionDTO functionDTO = new AppFunctionDTO();
        BeanUtils.copyProperties(func, functionDTO, new String[] { "createUser", "lastModifyUser" });
        if (func.getParentEntity() != null) {
            functionDTO.setParentCode(func.getParentEntity().getId());
        }
        return functionDTO;
    }

    /**
     * 添加方法
     *
     * @param dto
     * @return
     */
    public AppFunctionDTO add(AppFunctionDTO dto) {
        AppFunction function = new AppFunction();
        BeanUtils.copyProperties(dto, function);

        // 找到父功能
        AppFunction parentFunc = (AppFunction) this.getObject(dto.getParentCode());
        // 找到父功能的path
        String parentPath = parentFunc.getPath();
        // 设置父功能
        function.setParentEntity(parentFunc);
        // 设置路径
        String resultPath = parentPath + dto.getActivityCode() + ",";
        function.setPath(resultPath);
        this.saveObject(function);
        return dto;
    }

    public AppFunctionDTO modify(AppFunctionDTO dto) throws IOException {

        AppFunction entity = (AppFunction) this.getObject(dto.getId());

        entity.setActivityName(dto.getActivityName());
        entity.setUrl(dto.getUrl());
        entity.setOrderNo(dto.getOrderNo());
        entity.setActivityCode(dto.getActivityCode());
        entity.setRemark(dto.getRemark());
        entity.setIcon(dto.getIcon());

        saveObject(entity);

        return dto;
    }


    public List<AppFunctionDTO> getChildren(Long roleId) {
        // 获取虚拟根节点
        AppFunction root = (AppFunction) this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
        if (null == root) {
            return null;
        }
        List<AppFunctionDTO> modules = new ArrayList<AppFunctionDTO>();
        // 获取角色所拥有的功能权限的id
        List<Long> roleACL = getRoleAcl(roleId);
        // 遍历添加节点
        Iterator<AppFunction> iterator = root.getChilds().iterator();
        while (iterator.hasNext()) {
            AppFunction child = iterator.next();
            AppFunctionDTO dto = new AppFunctionDTO();
            dto.setId(child.getId());
            dto.setName(child.getActivityName());
            dto.setActivityCode(child.getActivityCode());
            dto.setPath(child.getPath());
            if (roleACL.contains(child.getId())) {
                dto.setChecked(true);
            }
            if (child.getChilds().size() > 0) {
                dto.setIsParent(true);
            }
            modules.add(dto);
        }
        return modules;
    }

    /**
     * 获取角色所拥有的功能权限的id.
     *
     * @param roleId the role id
     * @return the role acl
     */
    public List<Long> getRoleAcl(Long roleId) {
        List<Long> roleACL = new ArrayList<Long>();
        RoleAppFuncManager roleFuncManager = (RoleAppFuncManager) SpringHelper.getBean("roleAppFuncManager");
        List<RoleAppFunc> entities = (List<RoleAppFunc>) roleFuncManager.getObjects(FilterFactory.getSimpleFilter("roleEntity.id", roleId));
        for (int i = 0; i < entities.size(); i++) {
            // 通过级联关系获取角色所拥有的权限数据的id集合
            roleACL.add(entities.get(i).getAppFunctionEntity().getId());
        }
        return roleACL;
    }

    public List<AppFunctionDTO> getChildrenByParentId(Long roleId, Long parentId) {
        // 获取该功能菜单的直接子级
        AppFunction root = (AppFunction) this.getObject(parentId);
        if (null == root) {
            return null;
        }
        List<AppFunctionDTO> modules = new ArrayList<AppFunctionDTO>();
        List<Long> roleACL = getRoleAcl(roleId);
        Iterator<AppFunction> iterator = root.getChilds().iterator();
        while (iterator.hasNext()) {
            AppFunction child = iterator.next();
            AppFunctionDTO dto = new AppFunctionDTO();
            dto.setId(child.getId());
            dto.setName(child.getActivityName()+(child.getRemark()==null?"":"("+child.getRemark()+")"));
            dto.setPath(child.getPath());
            dto.setActivityCode(child.getActivityCode());
            // 通过角色拥有的功能权限id集合加上已分配的标记
            if (roleACL.contains(child.getId())) {
                dto.setChecked(true);
            }
            // 判断时候是否有下级为授权树节点加是父节点的标记
            if (child.getChilds().size() > 0) {
                dto.setIsParent(true);
            }
            modules.add(dto);
        }
        return modules;
    }

}
