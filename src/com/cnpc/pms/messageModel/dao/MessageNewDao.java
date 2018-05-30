/**
 * gaobaolei
 */
package com.cnpc.pms.messageModel.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.messageModel.entity.Message;

/**
 * @author gaobaolei
 *
 */
public interface MessageNewDao extends IDAO{
	
	/**
	 * 
	 * TODO 根据模板编号查询消息 
	 * 2017年7月17日
	 * @author gaobaolei
	 * @param code
	 * @return
	 */
	public List<Map<String,Object>> queryMessageByCode(String code);
	
	/**
	 * 
	 * TODO 查询审批消息列表 
	 * 2017年7月18日
	 * @author gaobaolei
	 * @param whereStr
	 * @param pageInfo
	 * @return
	 */
	public  List<Map<String,Object>> queryApproveMessage(String whereStr,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 查询非审批消息列表 
	 * 2017年7月18日
	 * @author gaobaolei
	 * @param whereStr
	 * @param pageInfo
	 * @return
	 */
	public List<Map<String,Object>> queryNoApproveMessage(String whereStr,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 查询审批消息详情
	 * 2017年7月18日
	 * @author gaobaolei
	 * @param message
	 * @return
	 */
	public List<Map<String,Object>> getApproveMessageDetailInfo(Message message);
	
	/**
	 * 
	 * TODO 查询消息回调
	 * 2017年7月20日
	 * @author gaobaolei
	 * @return
	 */
	public List<Map<String,Object>> getMessageCallBack();
	
	/**
	 * 
	 * TODO 获取 城市信息
	 * 2017年7月21日
	 * @author gaobaolei
	 * @param whereStr
	 * @return
	 */
	public List<Map<String, Object>> getReceiveCity(String whereStr,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 获取门店信息
	 * 2017年7月21日
	 * @author gaobaolei
	 * @param whereStr
	 * @param pageInfo
	 * @return
	 */
	public List<Map<String, Object>> getReceiveStore(String cityStr,String otherWhere,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO  获取职务
	 * 2017年7月24日
	 * @author gaobaolei
	 * @param otherWhere
	 * @param pageInfo
	 * @return
	 */
	public List<Map<String, Object>> getReceiveZW(String otherWhere,PageInfo pageInfo);
	
	
	/**
	 * 
	 * TODO 获取员工信息 
	 * 2017年7月21日
	 * @author gaobaolei
	 * @param whereStr
	 * @param pageInfo
	 * @return
	 */
	public List<Map<String, Object>> getReceiveEmployee(String whereStr,PageInfo pageInfo);
	
	/**
	 * 
	 * TODO  获取信息接收人
	 * 2017年7月24日
	 * @author gaobaolei
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getReceiveEmployee(Map<String,Object> param);
	
	/**
	 * 
	 * TODO 查询最新的未读消息 
	 * 2017年8月15日
	 * @author gaobaolei
	 * @param message
	 * @return
	 */
	public List<Map<String, Object>> queryMessageNew(Message message);
	
	/**
	 * 
	 * TODO 查询历史的消息 
	 * 2017年8月15日
	 * @author gaobaolei
	 * @param message
	 * @return
	 */
	public List<Map<String, Object>> queryMessageHistory(Message message,PageInfo pageinfo);
	
	
	/**
	 * 
	 * TODO 阅读信息 
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
	public void deleteMessage(Message message);
	
	/**
	 * 
	 * TODO 查询未读的消息 
	 * 2017年8月17日
	 * @author gaobaolei
	 * @param message
	 * @return
	 */
	public Integer getUnReadMessage(Message message);
	
	/**
	 * 
	 * TODO 获取用户的房子 
	 * 2017年8月17日
	 * @author gaobaolei
	 * @param id
	 * @return
	 */
	public Map<String, Object> getCustomerHouse(Long id);
	
	/**
	 * 
	 * TODO 根据楼或者放假查找片区信息 
	 * 2017年8月17日
	 * @author gaobaolei
	 * @param id
	 * @param model
	 * @return
	 */
	public Map<String, Object> getAreaOfCustomer(Long id,Integer model);
	
	/**
	 * 
	 * TODO 根据小区id 查找片区 
	 * 2017年8月17日
	 * @author gaobaolei
	 * @param tinyvillageId
	 * @return
	 */
	public Map<String, Object> getAreaOfCustomerByTinyVillage(Long tinyvillageId);
	
	/**
	 * 
	 * TODO 根据社区id 查找片区
	 * 2017年8月17日
	 * @author gaobaolei
	 * @param villageId
	 * @return
	 */
	public Map<String, Object> getAreaOfCustomerByVillage(Long villageId);
	
	/**
	 * 
	 * TODO 查询片区最新动态 
	 * 2017年8月18日
	 * @author gaobaolei
	 * @param message
	 * @return
	 */
	public  List<Map<String, Object>> getMessageOfNews(Message message);
	
	/**
	 * 
	 * TODO 获取消息列表 
	 * 2017年8月21日
	 * @author gaobaolei
	 * @param employee_id
	 * @return
	 */
	public  List<Map<String, Object>> getMessageByEmployee(Long employee_id);
	
	/**
	 * 
	 * TODO 获取未读消息数量 
	 * 2017年8月21日
	 * @author gaobaolei
	 * @param employee_id
	 * @return
	 */
	public Integer getUnReadMessage(Long  employee_id);
	
	/**
	 * 
	 * TODO 删除 
	 * 2017年8月21日
	 * @author gaobaolei
	 * @param message
	 */
	public void deleteMessage(Long  employee_id);
	
	/**
	 * 
	 * TODO 清空 
	 * 2017年8月23日
	 * @author gaobaolei
	 * @param employeeId
	 */
	public void deleteAllMessage(String employeeId);
	
	/**
	 * 
	 * TODO 删除考勤记录 
	 * 2017年9月1日
	 * @author gaobaolei
	 * @param message
	 */
	public void deleteWorkRecord(Message message);
	
	/**
	 * 
	 * TODO 获取店长 
	 * 2017年9月1日
	 * @author gaobaolei
	 * @param store_id
	 * @return
	 */
	public Map<String, Object> getStorekeeper(Long store_id);
	
	/**
	 * 
	 * TODO 获取信息城市 
	 * 2017年9月5日
	 * @author gaobaolei
	 * @param employeeId
	 * @return
	 */
	public List<Map<String, Object>> queryCityInfo(Long employeeId);
	
	/**
	 * 
	 * TODO 根据城市查询门店 
	 * 2017年9月5日
	 * @author gaobaolei
	 * @param cityInfo
	 * @return
	 */
	public List<Map<String, Object>> queryStoreByCity(String cityInfo);
	
	
	public List<Map<String, Object>> queryMessageByStoreKeeperId(String storeKeeperId);
	
	public void updateMessageReadById(Long id);
	
	
	
	//点击显示更多消息列表 
    public List<Map<String, Object>> queryMoreMessageByStoreKeeperId(String storeKeeperId, PageInfo pageInfo);
    
    
}
