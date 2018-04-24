/**
 * gaobaolei
 */
package com.cnpc.pms.messageModel.dao;

import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.impl.values.StringEnumValue;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.messageModel.entity.MessageTemplate;

/**
 * @author gaobaolei
 *  消息模板数据接口
 */
public interface MessageTemplateDao extends IDAO{
	
	/**
	 * 
	 * TODO 保存消息模板 
	 * 2017年7月13日
	 * @author gaobaolei
	 * @param list
	 */
	public void saveMessageTemplate(List<MessageTemplate> list);
	
	/**
	 * 
	 * TODO  查询消息模板
	 * 2017年7月13日
	 * @author gaobaolei
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryMessageTemplate(String whereStr,PageInfo pInfo);
	
	/**
	 * 
	 * TODO 根据模板类型查询消息模板 
	 * 2017年7月18日
	 * @author gaobaolei
	 * @param model
	 * @return
	 */
	public List<Map<String, Object>> queryMessageTemplateByModel(String model);
	
	/**
	 * 
	 * TODO 根据模板编码查找消息模板 
	 * 2017年7月18日
	 * @author gaobaolei
	 * @param code
	 * @return
	 */
	public List<Map<String, Object>> queryMessageTemplateByCode(String code);
	
	/**
	 * 
	 * TODO 查找消息定义信息 
	 * 2017年7月20日
	 * @author gaobaolei
	 * @param code
	 * @return
	 */
	public List<Map<String, Object>> getMessageTemplate(String code);
	
	/**
	 * 
	 * TODO  物理删除消息定义回调
	 * 2017年7月21日
	 * @author gaobaolei
	 * @param code
	 */
	public void deleteMessageTemplateCallBack(String code);
}
