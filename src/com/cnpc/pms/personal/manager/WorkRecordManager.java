package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.WorkRecord;
import com.cnpc.pms.personal.entity.WorkRecordTotal;

import java.util.List;
import java.util.Map;

public interface WorkRecordManager extends IManager{
	
	public List<Humanresources> queryHumanresourceList();
	
	public List<WorkRecord> queryWorkRecordList(String worktime,Long store_id);
	
	public WorkRecordTotal saveWorkRecordList(List<Map<String, Object>> objList,String work_month,Long store_id);
	
	public String saveWorkRecordForExcel(Long workrecord_id);
	
	public List<Humanresources> queryHumanresourceListLz(String month);
	
	public List<Humanresources> queryHumanresourceListRz(String month);
	
	
	public String queryScoreIsExport(Long workrecord_id);
	
	
	public List<WorkRecord> queryWorkRecordListByTop(String work_month,Long store_id);
	
}
