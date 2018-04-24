/**
 * gaobaolei
 */
package com.cnpc.pms.messageModel.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;

/**
 * @author gaobaolei
 * 消息详情
 */
@Entity
@Table(name="t_message")
public class Message extends DataEntity{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 标题
	 */
	@Column(name = "title",length = 50)
	private String title;

	/**
	 * 内容
	 */
	@Column(name = "content",length = 255)
	private String content;
	
	@Column(name = "to_employee_id",length = 20)
	private Long to_employee_id;
	/**
	 * 模板编号
	 */
	@Column(name = "templateCode",length = 255)
	private  String templateCode;

	/**
	 * 消息一致性编码
	 */
	@Column(name = "messageCode",length = 255)
	private String messageCode;
	

	
	@Column(name="receiveId")
	private String receiveId;
	
	@Transient
	private User receiveUser;
	
	@Column(name="type")
	private String type;//work_record:考勤  customer:用户画像  other_notice:其他 password:密码修改
	
	@Column(name="pk_id")
	private Long pk_id;
    
	@Column(name="isRead")
	private  int isRead;
	
	@Column(name="readDate")
	private Date readDate;
	
	@Column(name="isDelete")
	private int isDelete;
	
	@Column(name="deleteDate")
	private Date deleteDate;
	
	@Column(name="jump_path")
	private String jump_path;
	
	/**
	 * 删除业务类
	 */
	@Column(name = "delete_manager",length = 100)
	private String delete_manager;

	/**
	 * 删除方法
	 */
	@Column(name = "delete_method",length = 100)
	private String delete_method;


	/**
	 * 审核方法
	 */
	@Column(name = "review_method",length = 100)
	private String review_method;
	
	/**
	 * 发信人
	 */
	@Column(name = "sendId",length = 20)
	private String sendId;
	
	

	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getTemplateCode() {
		return templateCode;
	}


	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}


	public String getMessageCode() {
		return messageCode;
	}


	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}


	public String getSendId() {
		return sendId;
	}


	public void setSendId(String sendId) {
		this.sendId = sendId;
	}


	public User getReceiveUser() {
		return receiveUser;
	}


	public void setReceiveUser(User receiveUser) {
		this.receiveUser = receiveUser;
	}


	public String getReceiveId() {
		return receiveId;
	}


	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Long getPk_id() {
		return pk_id;
	}


	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}


	public int getIsRead() {
		return isRead;
	}


	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}


	public Date getReadDate() {
		return readDate;
	}


	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}


	public int getIsDelete() {
		return isDelete;
	}


	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}


	public Date getDeleteDate() {
		return deleteDate;
	}


	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}


	


	public Long getTo_employee_id() {
		return to_employee_id;
	}


	public void setTo_employee_id(Long to_employee_id) {
		this.to_employee_id = to_employee_id;
	}


	public String getDelete_manager() {
		return delete_manager;
	}


	public void setDelete_manager(String delete_manager) {
		this.delete_manager = delete_manager;
	}


	public String getDelete_method() {
		return delete_method;
	}


	public void setDelete_method(String delete_method) {
		this.delete_method = delete_method;
	}


	public String getReview_method() {
		return review_method;
	}


	public void setReview_method(String review_method) {
		this.review_method = review_method;
	}


	public String getJump_path() {
		return jump_path;
	}


	public void setJump_path(String jump_path) {
		this.jump_path = jump_path;
	}
	
	
	
}
