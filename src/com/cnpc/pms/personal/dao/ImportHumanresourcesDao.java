package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.ImportHumanresources;
import com.cnpc.pms.personal.entity.YyMicrData;

public interface ImportHumanresourcesDao {

	public List<ImportHumanresources> queryImportHumanresourcesByCard(String cardNumber,String name);

	public void updateImportHumanresourcesId(String ids);
	
	public Map<String,Double> queryMaxImportDept();
	
}