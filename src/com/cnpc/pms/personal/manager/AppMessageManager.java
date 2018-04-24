package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.messageModel.entity.Message;
import com.cnpc.pms.personal.entity.AppMessage;
import com.cnpc.pms.personal.entity.Attachment;

import java.util.List;
import java.util.Map;

/**
 * 消息框内容
 * Created by liuxiao on 2016/10/25.
 */
public interface AppMessageManager extends IManager {

    /**
     * 获取employee_id 所有的信息
     * @param employee_id 员工号
     * @return 消息集合
     */
    //List<AppMessage> getAppMessageList(Long employee_id);
    
	/**
	 * 
	 * TODO 兼容旧版APP消息 
	 * 2017年8月21日
	 * @author gaobaolei
	 * @param employee_id
	 * @return
	 */
    public List<Map<String, Object>> getAppMessageList(Long employee_id);

    Integer getAppMessageCount(Long employee_id);

    /**
     * 获取id的信息
     * @param id 消息主键
     * @return 消息内容
     */
    AppMessage getAppMessage(Long id);

    /**
     * 设置消息已读和读取时间（当前时间）
     * @return false设置失败   true 设置成功
     */
    Boolean updateReadyMessage(Long id);

    /**
     * 删除消息
     * @return false删除失败   true 删除成功
     */
    Boolean deleteMessage(Long id);
    
    public Boolean deleteAllMessage(Long employee_id);

    Boolean saveMessageAndPush(User user,AppMessage appMessage);

}
