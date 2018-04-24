package com.cnpc.pms.base.init.manager;

import com.cnpc.pms.base.init.script.IScriptContext;
import com.cnpc.pms.base.manager.IManager;

/**
 * Init data.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author lushu
 * @since May 7, 2011
 */
public interface InitializeDataManager extends IManager {

	void importData();

	void generateDate();

	void importContext(IScriptContext context);

}
