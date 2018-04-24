package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_banner")
public class AppBanner extends DataEntity{

	/**
	 * 轮播图片ID
	 */
	@Column(name = "banner_id")
	private Long banner_id;
	
	/**
	 * 轮播图片主题
	 */
	@Column(name = "banner_title",length = 100)
	private String banner_title;
	
	/**
	 * 跳转页面名称
	 */
	@Column(length = 45, name = "banner_to")
	private String banner_to;
	
	/**
	 * 轮播图片
	 */
	@Column(length = 100, name = "banner_pic")
	private String banner_pic;

	public Long getBanner_id() {
		return banner_id;
	}

	public void setBanner_id(Long banner_id) {
		this.banner_id = banner_id;
	}

	public String getBanner_title() {
		return banner_title;
	}

	public void setBanner_title(String banner_title) {
		this.banner_title = banner_title;
	}

	public String getBanner_to() {
		return banner_to;
	}

	public void setBanner_to(String banner_to) {
		this.banner_to = banner_to;
	}

	public String getBanner_pic() {
		return banner_pic;
	}

	public void setBanner_pic(String banner_pic) {
		this.banner_pic = banner_pic;
	}
	



}
