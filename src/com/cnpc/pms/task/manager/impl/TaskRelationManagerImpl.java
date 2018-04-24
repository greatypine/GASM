package com.cnpc.pms.task.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.task.dto.TaskRelationDTO;
import com.cnpc.pms.task.entity.TaskInfo;
import com.cnpc.pms.task.entity.TaskRelation;
import com.cnpc.pms.task.manager.TaskInfoManager;
import com.cnpc.pms.task.manager.TaskRelationManager;
import com.cnpc.pms.task.util.TaskConstants;

public class TaskRelationManagerImpl extends BizBaseCommonManager implements
		TaskRelationManager {

	public TaskInfoManager getTaskManager() {
		return (TaskInfoManager) SpringHelper.getBean("taskInfoManager");
	}

	public void addInitTaskRelation(TaskRelationDTO dto) {
		// TODO Auto-generated method stub
		/*String module = dto.getModule();
		Long projectId = dto.getProjectId();
		List<TaskInfo> ls = getTaskManager().queryTasksByModule(module);
		for (TaskInfo task : ls) {
			String taskStatus = task.getStatus();
			TaskRelation tr = new TaskRelation();
			tr.setModule(module);
			tr.setTaskId(task.getTaskId());	// 2013-9-23 王硕修改加入节点ID
			tr.setTaskName(task.getTaskName());
			tr.setTaskParentId(task.getParentId());
			tr.setTaskUrl(task.getTaskUrl());
			tr.setStatus(taskStatus);
			tr.setProjectId(projectId);
			this.preSaveObject(tr);
			this.saveObject(tr);
		}*/
	}

	public List<TaskRelationDTO> queryTasksRelationByProjectId(Long projectId) {
		// TODO Auto-generated method stub
		FSP fsp = new FSP();
		fsp.setUserFilter(FilterFactory.getSimpleFilter("projectId", projectId));
		fsp.setSort(new Sort("taskId", Sort.ASC));
		List<TaskRelation> ls = (List<TaskRelation>) this.getObjects(fsp);
		List<TaskRelationDTO> retLs = new ArrayList<TaskRelationDTO>();
		for (TaskRelation tr : ls) {
			TaskRelationDTO dto = new TaskRelationDTO();
			BeanUtils.copyProperties(tr, dto);
			// 设置树需要的参数
			dto.setId(tr.getTaskId());
			dto.setName(tr.getTaskName());
			dto.setpId(tr.getTaskParentId());
			if (dto.getStatus() == null
					|| TaskConstants.TASK_STATUS_UNENFORCEABLE.equals(dto
							.getStatus())) {
				dto.setTaskUrl("");
			}
			dto.setOpen(true);
			retLs.add(dto);
		}
		return retLs;
	}

	public void updateTaskRelation(TaskRelationDTO dto) {
		if(dto.getProjectId()!=null&&dto.getTaskId()!=null){
			// TODO Auto-generated method stub
			IFilter filter = FilterFactory.getSimpleFilter(" PROJECT_ID = '"
					+ dto.getProjectId() + "' AND TASK_ID = '" + dto.getTaskId()
					+ "'");
			TaskRelation tr = (TaskRelation) this.getUniqueObject(filter);
			if (tr != null) {
				if (dto.getStatus() != null && !"".equals(dto.getStatus())) {
					tr.setStatus(dto.getStatus());
				}
				if (dto.getTaskUrl() != null && !"".equals(dto.getTaskUrl())) {
					tr.setTaskUrl(dto.getTaskUrl());
				}
				if (dto.getTaskName() != null && !"".equals(dto.getTaskName())) {
					tr.setTaskName(dto.getTaskName());
				}
				this.preSaveObject(tr);
				this.saveObject(tr);
			}
		}
	}

	public TaskRelationDTO getTaskRelationDTO(TaskRelationDTO dto) {
		IFilter filter = FilterFactory.getSimpleFilter(" PROJECT_ID = '"
				+ dto.getProjectId() + "' AND TASK_ID = '" + dto.getTaskId()
				+ "'");
		TaskRelation tr = (TaskRelation) this.getUniqueObject(filter);
		TaskRelationDTO taskRelationDTO = new TaskRelationDTO(); 
		BeanUtils.copyProperties(tr, taskRelationDTO);
		return taskRelationDTO;
	}

}
