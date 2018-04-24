package com.cnpc.pms.base.manager;

/**
 * Update initial object and record audit info 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Xiang ZhongMing
 * @since 2011-3-24
 */
public interface IAuditManager {
	
	/**
	 * Save audit object.
	 *
	 * @param iniObject the initial object
	 * @param auditObject the audit object to record the history info
	 */
	public void saveAuditObject(Object iniObject, Object auditObject);

}
