package com.cnpc.pms.base.init.dao;

import com.cnpc.pms.base.init.script.AutoGenerateContext;
import com.cnpc.pms.base.init.script.IScriptContext;

public interface InitializeDataDAO {
	void importData(IScriptContext context);

	void importDataInThread(IScriptContext context);

	void generateData(AutoGenerateContext context);

}
