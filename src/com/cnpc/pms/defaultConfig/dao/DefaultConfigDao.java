/**
 * gaobaolei
 */
package com.cnpc.pms.defaultConfig.dao;

import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.defaultConfig.entity.DefaultConfig;

/**
 * 用户默认配置
 * @author gaobaolei
 *
 */
public interface DefaultConfigDao extends IDAO{
	
	
	/**
	 * 
	 * TODO 保存默认配置 
	 * 2017年5月17日
	 * @author gaobaolei
	 * @param defaultConfig
	 */
	public void saveDefaultConfig(DefaultConfig defaultConfig);
	
	public void updateDefaultConfig(DefaultConfig defaultConfig);
	
	/**
	 * 
	 * TODO 检查用户的默认配置 
	 * 2017年5月17日
	 * @author gaobaolei
	 * @param employee_no
	 * @return
	 */
	public Map<String, Object> checkDefaultConfig(String employee_no,String employee_name);
	
}
