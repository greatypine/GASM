package com.cnpc.pms.personal.entity;


import com.cnpc.pms.base.entity.DataEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_banner_info")
public class BannerInfo extends DataEntity{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 功能模块
	 */
	@Column(length = 64, name = "function_name")
	private String function_name;
	
	//图片
	@Column(length = 128, name = "banner_url")
	private String banner_url;
	
	//图片顺序
	@Column(name="ordernum")
	private Long ordernum;
	
	//打开的连接
	@Column(length = 255, name = "open_url")
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
