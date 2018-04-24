/**
 * gaobaolei
 */
package com.cnpc.pms.platform.dao;

import java.util.List;
import java.util.Map;

/**
 * @author gaobaolei
 * 
 */
public interface PlatformEmployeeDao {
	
	/**
	 * 
	 * TODO 根据GASM 员工编号查询gemini 的员工 
	 * 2018年3月7日
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeByEmployeeNo(String employeeNo);
}
