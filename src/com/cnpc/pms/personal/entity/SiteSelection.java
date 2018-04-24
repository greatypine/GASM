package com.cnpc.pms.personal.entity;


import com.cnpc.pms.base.entity.DataEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_site_selection")
public class SiteSelection extends DataEntity{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 真实姓名
	 */
	@Column(length = 45, name = "realname")
	private String real_name;
	
	/**
	 * 手机号码
	 */
	@Column(length = 45, name = "mobilephone")
	private String mobilephone;
	
	
	@Column(length = 45, name = "password")
	private String password;
	
	/**
	 * 是否为国安社区员工
	 */
	@Column(length = 45, name = "isemployee")
	private String employee_flag;
	
	/**
	 * 是否有专业选址经验
	 */
	@Column(length = 45, name = "isselection")
	private String selection_flag;
	
	/**
	 * 上传名片正面
	 */
	@Column(length = 255, name = "cards")
	private String cards;

	
	/**
	 * 上传名片返面
	 */
	@Column(length = 255, name = "cards_back")
	private String cards_back;
	
	
	/**
	 * 状态｛0，申请  1通过  2未通过｝
	 */
	@Column(name="selectionstatus")
	private Long selection_status;
	
	
	/**
	 * 原因
	 */
	@Column(length = 255, name = "reason")
	private String reason;
	
	
	@Transient
	private String nickname;
	
	@Transient
	private Long user_id;
	

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}


	public String getCards() {
		return cards;
	}

	public void setCards(String cards) {
		this.cards = cards;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getEmployee_flag() {
		return employee_flag;
	}

	public void setEmployee_flag(String employee_flag) {
		this.employee_flag = employee_flag;
	}

	public String getSelection_flag() {
		return selection_flag;
	}

	public void setSelection_flag(String selection_flag) {
		this.selection_flag = selection_flag;
	}

	public String getCards_back() {
		return cards_back;
	}

	public void setCards_back(String cards_back) {
		this.cards_back = cards_back;
	}

	public Long getSelection_status() {
		return selection_status;
	}

	public void setSelection_status(Long selection_status) {
		this.selection_status = selection_status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

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


	
}
