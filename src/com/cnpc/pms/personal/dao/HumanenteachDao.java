package com.cnpc.pms.personal.dao;

import java.util.List;

import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.Humanenteach;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.ImportHumanresources;
import com.cnpc.pms.personal.entity.YyMicrData;

public interface HumanenteachDao {

	public void deleteHumanenTeachByParentId(Long hr_id);
	
	public List<Humanenteach> queryHumanenTeachByParentId(Long hr_id);
	
}