/**
 * gaobaolei
 */
package com.cnpc.pms.messageModel.manager;

import java.util.Map;

import org.apache.xmlbeans.impl.values.StringEnumValue;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.messageModel.entity.MessageTemplate;

/**
 * @author gaobaolei
 * 消息模板接口
 */
public interface MessageTemplateManager extends IManager{
	
	/**
	 * 
	 * TODO 新增消息模板 
	 * 2017年7月13日
	 * @author gaobaolei
	 */
	public  MessageTemplate saveMessageTemplate(MessageTemplate mt);
	
	
	/**
	 * 
	 * TODO 删除消息模板
	 * 2017年7月13日
	 * @author gaobaolei
	 * @param mt
	 */
	public  Map<String, Object> deleteMessageTemplate(MessageTemplate mt);
	
	/**
	 * 
	 * TODO  查询消息模板
	 * 2017年7月13日
	 * @author gaobaolei
	 * @param mt
	 * @return
	 */
	public Map<String,Object> queryMessageTemplate(QueryConditions queryConditions);
	
	/**
	 * 
	 * TODO 检测模板名称是否已存在 
	 * 2017年7月17日
	 * @author gaobaolei
	 * @param mt
	 * @return
	 */
	public Map<String, Object> checkTemplateIsExist(MessageTemplate mt);
	
	/**
	 * 
	 * TODO 
	 * 2017年7月18日
	 * @author gaobaolei
	 * @param model
	 * @return
	 */
	public Map<String, Object> getMessageTemplateByModel(String model);
	
	/**
	 * 
	 * TODO 获取消息定义信息 
	 * 2017年7月20日
	 * @author gaobaolei
	 * @param code
	 * @return
	 */
	public Map<String, Object> getMessageTemplateByCode(String code);
	
	/**
	 * 
	 * TODO 获取消息模板 
	 * 2017年8月15日
	 * @author gaobaolei
	 * @param model
	 * @return
	 */
	public Map<String,Object> queryMessageTemplateByCode(String model);
}
