package com.cnpc.pms.task.manager;

import java.util.List;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.task.entity.TaskInfo;

public interface TaskInfoManager extends IManager {

	/**
	 * 根据模块查询任务集合
	 * 
	 * @return
	 */
	public List<TaskInfo> queryTasksByModule(String module);

}
