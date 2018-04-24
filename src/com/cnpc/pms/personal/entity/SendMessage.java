package com.cnpc.pms.personal.entity;


import com.cnpc.pms.base.entity.DataEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_send_message")
public class SendMessage extends DataEntity{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 功能模块
	 */
	@Column(length = 64, name = "functionname")
	private String functionname;
	
	/**
	 * 手机号
	 */
	@Column(length = 64, name = "mobilephone")
	private String mobilephone;
	
	/**
	 * 验证码
	 */
	@Column(length = 64, name = "code")
	private String code;
		
	/**
	 * 返回值 
	 */
	@Column(length = 2000, name = "rcvmessage")
	private String rcvmessage;
	
	//状态 标记是否用过
	@Column(name = "msgstatus")
	private Long msgstatus;
	
	

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRcvmessage() {
		return rcvmessage;
	}

	public void setRcvmessage(String rcvmessage) {
		this.rcvmessage = rcvmessage;
	}

	public String getFunctionname() {
		return functionname;
	}

	public void setFunctionname(String functionname) {
		this.functionname = functionname;
	}

	public Long getMsgstatus() {
		return msgstatus;
	}

	public void setMsgstatus(Long msgstatus) {
		this.msgstatus = msgstatus;
	}
	
}
