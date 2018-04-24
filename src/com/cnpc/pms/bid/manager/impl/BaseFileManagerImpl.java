package com.cnpc.pms.bid.manager.impl;

import com.cnpc.pms.base.entity.PMSAuditEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.dao.BaseFileDAO;
import com.cnpc.pms.bid.manager.BaseFileManager;
import com.cnpc.pms.bid.manager.dto.BaseFileDTO;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
//import com.cnpc.pms.forecast.dao.ProjectForecastDAO;

public class BaseFileManagerImpl extends BizBaseCommonManager implements BaseFileManager {
	
	public BaseFileDTO saveBaseFile(BaseFileDTO baseFileDTO) {
//		BaseFileDAO dao = (BaseFileDAO) SpringHelper.getBean(BaseFileDAO.class.getName());
		BaseFileDAO dao = (BaseFileDAO) this.getDao();
		dao.addBaseFile(baseFileDTO);
		return baseFileDTO;
	}

}
