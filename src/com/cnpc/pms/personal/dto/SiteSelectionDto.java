package com.cnpc.pms.personal.dto;

public class SiteSelectionDto{
	
	private Long id;
	/**
	 * 真实姓名
	 */
	private String real_name;
	
	/**
	 * 手机号码
	 */
	private String mobilephone;
	
	private String password;
	
	/**
	 * 是否为国安社区员工
	 */
	private String employee_flag;
	
	/**
	 * 是否有专业选址经验
	 */
	private String selection_flag;
	
	/**
	 * 上传名片正面
	 */
	private String cards;

	
	/**
	 * 上传名片返面
	 */
	private String cards_back;
	
	
	/**
	 * 状态｛0认识中  1通过  2未通过｝
	 */
	private Long selection_status;
	
	

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	
}
