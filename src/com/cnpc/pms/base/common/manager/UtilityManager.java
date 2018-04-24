package com.cnpc.pms.base.common.manager;

import com.cnpc.pms.base.common.model.ClientSuggestObject;
import com.cnpc.pms.base.manager.IManager;

/**
 * Contains non-entity related, common utility services.
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Jul 22, 2013
 */
public interface UtilityManager extends IManager {
	String getSuggestion(ClientSuggestObject suggestObj);
}
