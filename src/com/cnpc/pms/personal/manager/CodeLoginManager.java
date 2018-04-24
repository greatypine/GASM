package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.CodeLogin;

public interface CodeLoginManager extends IManager{

	public CodeLogin saveCodeLogin(CodeLogin codeLogin);
}
