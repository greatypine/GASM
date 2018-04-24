package com.cnpc.pms.toolkit.manager.impl;

import com.cnpc.pms.base.query.manager.impl.QueryManagerImpl;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.toolkit.dao.GeneralSequenceNoDAO;
import com.cnpc.pms.toolkit.manager.GeneralSequenceNoManager;

public class GeneralSequenceNoManagerImpl extends QueryManagerImpl implements
		GeneralSequenceNoManager {

	public Long getSequenceNo(String key, Integer stepSize) throws Exception {
		GeneralSequenceNoDAO dao = (GeneralSequenceNoDAO) SpringHelper
				.getBean(GeneralSequenceNoDAO.class.getName());
		return dao.getSequenceNo(key, stepSize);
	}

	public String getSequenceNoStr(String key, Integer stepSize,
			Integer codeLength) throws Exception {
		GeneralSequenceNoDAO dao = (GeneralSequenceNoDAO) SpringHelper
				.getBean(GeneralSequenceNoDAO.class.getName());
		return dao.getSequenceNoStr(key, stepSize, codeLength);
	}

	public Long getDefSequenceNo(String key) throws Exception {
		GeneralSequenceNoDAO dao = (GeneralSequenceNoDAO) SpringHelper
				.getBean(GeneralSequenceNoDAO.class.getName());
		return dao.getDefSequenceNo(key);
	}

	public String getDefSequenceNoStr(String key, Integer codeLength)
			throws Exception {
		GeneralSequenceNoDAO dao = (GeneralSequenceNoDAO) SpringHelper
				.getBean(GeneralSequenceNoDAO.class.getName());
		return dao.getDefSequenceNoStr(key, codeLength);
	}
}
