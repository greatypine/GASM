package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.ScoreRecord;
import com.cnpc.pms.personal.entity.ScoreRecordTotal;
import com.cnpc.pms.personal.entity.WorkMonth;

import java.util.List;
import java.util.Map;

public interface ScoreRecordManager extends IManager{
	
	public List<Humanresources> queryHumanresourceList();
	
	public List<ScoreRecord> queryScoreRecordList(String worktime,Long store_id);
	
	public ScoreRecordTotal saveScoreRecordList(List<Map<String, Object>> objList,String work_month,Long store_id,Long work_info_id);
	
	public String saveScoreRecordForExcel(Long scorerecord_id);
	
	public List<Humanresources> queryHumanresourceListLz(String month);
	
	public List<Humanresources> queryHumanresourceListRz(String month);
	
	
	public WorkMonth queryMaxWorkMonth();
	
	
	public List<ScoreRecord> queryScoreRecordListByTop(String work_month,Long store_id);
	
}
