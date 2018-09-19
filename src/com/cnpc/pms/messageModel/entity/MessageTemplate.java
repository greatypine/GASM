/**
 * gaobaolei
 */
package com.cnpc.pms.messageModel.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;

/**
 * @author gaobaolei
 * 定义消息
 */
@Entity
@Table(name="t_messageTemplate")
public class MessageTemplate extends DataEntity{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 模板类型（MMXG：密码修改 YHHX：用户画像 QT：自定义）
	 */
	@Column(name="model")
	private String model;
	
	/**
	 * 模板编号（唯一标识）
	 */
	@Column(name="code")
	private String code;
	
	/**
	 * 模板名称
	 */
	@Column(name="name")
	private String name;
	
	/**
	 * 内容
	 */
	@Column(name="content")
	private String content;
	
	/**
	 * 审批人（审批模板需要，非审批模板不需要）
	 */
	@Column(name="approver")
	private String approver;
	
	
	@Transient
	private List<String> callBackFunction;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public List<String> getCallBackFunction() {
		return callBackFunction;
	}

	public void setCallBackFunction(List<String> callBackFunction) {
		this.callBackFunction = callBackFunction;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
}
