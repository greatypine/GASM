package com.cnpc.pms.personal.dao;

import java.util.List;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.YyMicrData;

public interface WorkRecordDao {

	public void delWorkRecoedById(Long workrecord_id);
}