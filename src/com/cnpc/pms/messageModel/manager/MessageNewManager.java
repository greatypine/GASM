/**
 * gaobaolei
 */
package com.cnpc.pms.messageModel.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.messageModel.entity.Message;
import com.cnpc.pms.personal.entity.AppMessage;
import com.google.inject.internal.MapMaker;

/**
 * @author gaobaolei
 *
 */
public interface MessageNewManager extends IManager{
	
	/**
	 * 
	 * TODO 根据模板编码查询消息 
	 * 2017年7月17日
	 * @author gaobaolei
	 * @param code
	 * @return
	 */
	public List<Message> getMessageByTemplateCode(String code);
	
	/**
	 * 
	 * TODO 查询审批消息 
	 * 2017年7月18日
	 * @author gaobaolei
	 * @param whereStr
	 * @return
	 */
	public Map<String,Object> queryApproveMessage(QueryConditions queryConditions);
	
	/**
	 * 
	 * TODO 查询非审批消息
	 * 2017年7月18日
	 * @author gaobaolei
	 * @param whereStr
	 * @return
	 */
	public Map<String,Object> queryNoApproveMessage(QueryConditions queryConditions);
	
	/**
	 * 
	 * TODO 发送审批消息
	 * 2017年7月18日
	 * @author gaobaolei
	 * @param message
	 * @return
	 */
	public  Map<String,Object> saveApproveMessage(Message message);
	
	/**
	 * 
	 * TODO 获取消息详情 
	 * 2017年7月18日
	 * @author gaobaolei
	 * @param message
	 * @return
	 */
	public Map<String,Object> getApproveMessageDetailInfo(Message message);
	
	/**
	 * 
	 * TODO 查询消息回调 
	 * 2017年7月20日
	 * @author gaobaolei
	 * @return
	 */
	public Map<String,Object> getMessageCallBack();
	
	/**
	 * 
	 * TODO 获取信息接收城市 
	 * 2017年7月21日
	 * @author gaobaolei
	 * @param queryConditions
	 * @return
	 */
	public Map<String,Object> getReceiveCity(QueryConditions queryConditions);
	
	/**
	 * 
	 * TODO 获取信息接收门店
	 * 2017年7月21日
	 * @author gaobaolei
	 * @param queryConditions
	 * @return
	 */
	public Map<String,Object> getReceiveStore(QueryConditions queryConditions);
	
	/**
	 * 
	 * TODO  获取信息接收职务
	 * 2017年7月24日
	 * @author gaobaolei
	 * @param queryConditions
	 * @return
	 */
	public Map<String,Object> getReceiveZW(QueryConditions queryConditions);
	
	/**
	 * 
	 * TODO  获取信息接收员工
	 * 2017年7月24日
	 * @author gaobaolei
	 * @param queryConditions
	 * @return
	 */
	public Map<String,Object> getReceiveEmployee(QueryConditions queryConditions);
	
	/**
	 * 
	 * TODO 自动发送消息 
	 * 2017年8月15日
	 * @author gaobaolei
	 * @param user
	 * @param message
	 */
	public boolean sendMessageAuto(User user, Message message);
	
	/**
	 * 
	 * TODO  获取最新的未读消息
	 * 2017年8月15日
	 * @author gaobaolei
	 * @param message
	 * @return
	 */
	public Result queryMessageOfNew(Message message);
	
	/**
	 * 
	 * TODO 获取所有消息历史记录 
	 * 2017年8月15日
	 * @author gaobaolei
	 * @param message
	 * @return
	 */
	public Result queryMessageOfHistory(Message message,PageInfo pageinfo);
	
	/**
	 * 
	 * TODO 阅读消息 
	 * 2017年8月16日
	 * @author gaobaolei
	 * @param message
	 */
	public void readMessage(Message message);
	
	/**
	 * 
	 * TODO 删除消息 
	 * 2017年8月16日
	 * @author gaobaolei
	 * @param message
	 */
	public void delateMessage(Message message);
	
	/**
	 * 
	 * TODO  片区动态发送消息
	 * 2017年8月17日
	 * @author gaobaolei
	 * @param model 1:下订单 2：新增客户画像 3：拜访 
	 * @param send_user //动作触发者 例如 下单人电话或者是被拜访人用户ID或者是新增用户的ID
	 */
	public void  sendMessage_area_news(Integer model,String send_user);
	
	/**
	 * 
	 * TODO 获取未读的消息数量 
	 * 2017年8月17日
	 * @author gaobaolei
	 * @param message
	 * @return
	 */
	public Integer getUnReadMessageAmount(Message message);
	
	/**
	 * 
	 * TODO 获取最新的片区动态 
	 * 2017年8月18日
	 * @author gaobaolei
	 * @param employeeId
	 * @return
	 */
	public Result getMessageOfNews(Message message);
	
	/**
	 * 
	 * TODO 清空消息 
	 * 2017年8月23日
	 * @author gaobaolei
	 * @param message
	 */
	public void deleteAllMessage(Message message);
	
	/**
	 * 
	 * TODO 发送消息 
	 * 2017年9月18日
	 * @author gaobaolei
	 * @param sendUser 发送人员工编号或者ID
	 * @param receiveUser 接收者员工编号或者ID
	 * @param model 消息类型
	 */
	public void sendMessage_common(String sendUser,String receiveUser,String model,Object obj);
	
}
