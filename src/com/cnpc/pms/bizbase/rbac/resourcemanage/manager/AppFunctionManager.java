package com.cnpc.pms.bizbase.rbac.resourcemanage.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.AddFunctionDTO;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.AppFunctionDTO;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.FunctionDTO;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.Status;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.AppFunction;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * App功能菜单接口
 * @author liuxiao
 */
public interface AppFunctionManager extends IManager {

	List<AppFunctionDTO> getAllFuncTree();

    List<AppFunctionDTO> getAllChildsByParentId(Long parentId);

    AppFunctionDTO getFunctionById(Long id);

    AppFunctionDTO add(AppFunctionDTO dto);

    AppFunctionDTO modify(AppFunctionDTO dto) throws IOException;

    List<AppFunctionDTO> getChildren(Long roleId);

    List<AppFunctionDTO> getChildrenByParentId(Long roleId, Long parentId);
}
