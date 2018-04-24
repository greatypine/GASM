package com.cnpc.pms.task.manager;

import java.util.List;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.task.dto.TaskRelationDTO;

public interface TaskRelationManager extends IManager {

	/**
	 * 初始化任务关系表 需传入：module模块,projectId项目Id
	 * 
	 * @param dto
	 */
	public void addInitTaskRelation(TaskRelationDTO dto);

	/**
	 * 根据项目ID查询任务关系集合
	 * 
	 * @return
	 */
	public List<TaskRelationDTO> queryTasksRelationByProjectId(Long projectId);

	/**
	 * 修改节点样式、任务状态、链接地址 需传入：taskId任务节点Id,projectId项目Id,url链接地址,taskStatus任务状态
	 * 
	 * @param dto
	 */
	public void updateTaskRelation(TaskRelationDTO dto);
	
	/**
	 * 根据taskId任务节点Id,projectId项目Id获取DTO对象
	 * @description TODO
	 * @param 
	 * @result
	 */
	public TaskRelationDTO getTaskRelationDTO(TaskRelationDTO dto);

}
