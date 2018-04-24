package com.cnpc.pms.personal.entity;


import com.cnpc.pms.base.entity.DataEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_wx_userauth")
public class WxUserAuth extends DataEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "user_id")
	private Long user_id;
	
	/**
	 * 微信号
	 */
	@Column(length = 64, name = "wx_code")
	private String wx_code;
	
	/**
	 *电话
	 */
	@Column(length = 64, name = "mobilephone")
	private String mobilephone;
		
	//状态 标记是验证通过 0未通过  1通过了
	@Column(name = "authstatus")
	private Long authstatus;
	
	
	@Column(length = 64, name = "nickname")
	private String nickname;
	

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}


	public String getWx_code() {
		return wx_code;
	}

	public void setWx_code(String wx_code) {
		this.wx_code = wx_code;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public Long getAuthstatus() {
		return authstatus;
	}

	public void setAuthstatus(Long authstatus) {
		this.authstatus = authstatus;
	}
	
}
