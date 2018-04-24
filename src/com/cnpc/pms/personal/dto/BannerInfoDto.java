package com.cnpc.pms.personal.dto;

public class BannerInfoDto{
	
	/**
	 * 功能模块
	 */
	private String function_name;
	
	//图片
	private String banner_url;
	
	//图片顺序
	private Long ordernum;
	
	//打开连接
	private String open_url;
	


	public String getBanner_url() {
		return banner_url;
	}

	public void setBanner_url(String banner_url) {
		this.banner_url = banner_url;
	}

	public Long getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Long ordernum) {
		this.ordernum = ordernum;
	}

	public String getFunction_name() {
		return function_name;
	}

	public void setFunction_name(String function_name) {
		this.function_name = function_name;
	}

	public String getOpen_url() {
		return open_url;
	}

	public void setOpen_url(String open_url) {
		this.open_url = open_url;
	}
	
	
	
	
	
}
