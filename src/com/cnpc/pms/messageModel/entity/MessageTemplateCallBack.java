/**
 * gaobaolei
 */
package com.cnpc.pms.messageModel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;

/**
 * @author gaobaolei
 * 定义消息回调
 */
@Entity
@Table(name="t_messageTemplateCallBack")
public class MessageTemplateCallBack extends DataEntity{
	
	/**
	 * 消息定义编码
	 */
	@Column(name="templateCode")
	private String templateCode;
	
	/**
	 * 回调方法类型
	 * "delete":删除 ,"read":已读,"jump":确认跳转,"yes":通过,"no":不通过
	 */
	@Column(name="type")
	private String type;

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
