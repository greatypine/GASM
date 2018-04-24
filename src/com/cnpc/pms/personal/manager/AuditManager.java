package com.cnpc.pms.personal.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.Audit;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.entity.Village;

public interface AuditManager extends IManager{

	void insertAuditVillage(Village village);
	void insertAuditTown(Town town);
	Map<String, Object> showAuditData(QueryConditions conditions);
	Map<String, Object> findAuditById(Long id);
	Audit saveOrUpdateAudit(Audit audit);
	Audit getAuditById(Long id);
	
	
}
