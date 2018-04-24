package com.cnpc.pms.task.manager.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.task.entity.TaskInfo;
import com.cnpc.pms.task.manager.TaskInfoManager;

public class TaskInfoManagerImpl extends BizBaseCommonManager implements
		TaskInfoManager {

	@SuppressWarnings("unchecked")
	public List<TaskInfo> queryTasksByModule(String module) {
		List<TaskInfo> ls = (List<TaskInfo>) this.getObjects(FilterFactory
				.getSimpleFilter("module", module));
		Collections.sort(ls, new Comparator<TaskInfo>() {
			public int compare(TaskInfo arg0, TaskInfo arg1) {
				return arg0.getOrderCode().compareTo(arg1.getOrderCode());
			}
		});
		return ls;
	}
}
