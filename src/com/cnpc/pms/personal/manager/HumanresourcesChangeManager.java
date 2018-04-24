package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.HumanresourcesChange;
import com.cnpc.pms.personal.entity.HumanresourcesLog;


public interface HumanresourcesChangeManager extends IManager {

    public HumanresourcesChange saveHumanresourcesChange(HumanresourcesChange humanresourcesChange);
    
}
