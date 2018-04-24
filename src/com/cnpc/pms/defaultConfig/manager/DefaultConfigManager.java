/**
 * gaobaolei
 */
package com.cnpc.pms.defaultConfig.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.defaultConfig.dto.DefaultConfigDTO;
import com.cnpc.pms.defaultConfig.entity.DefaultConfig;

/**
 * @author gaobaolei
 *
 */
public interface DefaultConfigManager extends IManager{
	
	/**
	 * 
	 * TODO 保存/更新默认配置信息 
	 * 2017年5月17日
	 * @author gaobaolei
	 * @param config
	 * @return
	 */
	public DefaultConfigDTO saveOrUpdateDefaultConfig(DefaultConfig config);
	
	/**
	 * 
	 * TODO 查询默认配置信息 
	 * 2017年5月17日
	 * @author gaobaolei
	 * @param employee_no
	 * @return
	 */
	public Map<String, Object> getDefaultConfig(String employee_no,String employee_name);
	
	/**
	 * 
	 * TODO 删除用户默认配置 
	 * 2017年5月17日
	 * @author gaobaolei
	 * @param employee_no
	 */
	public  void deleteDefaultConfig(String employee_no);
}
