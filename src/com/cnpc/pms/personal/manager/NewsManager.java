package com.cnpc.pms.personal.manager;


import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.News;

public interface NewsManager extends IManager{
	News getNewsBy(Integer type_id,Long key_id);
}
