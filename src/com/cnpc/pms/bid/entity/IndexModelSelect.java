package com.cnpc.pms.bid.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.PMSEntity;
/**
 * 首页模块选择情况
 * @author IBM
 *
 */
@Entity
@Table(name = "tb_indexmodel_select")
public class IndexModelSelect extends PMSEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1034527192515320943L;
	
	/** 模块编号 */
	@Column(name = "modelCode", length = 100)
	private String modelCode;
	
	/** 模块显示名称 */
	@Column(name = "modelText", length = 500)
	private String modelText;
	
	/**
	 * 是否显示
	 * 0:不显示;1:显示
	 */
	@Column(name = "displayStatus")
	private Integer displayStatus;
	
	/** 排序编号 */
	@Column(name = "orderNum")
	private Integer orderNum;
	
	/** 用户id */
	@Column(name = "userId")
	private Long userId;

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelText() {
		return modelText;
	}

	public void setModelText(String modelText) {
		this.modelText = modelText;
	}

	public Integer getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(Integer displayStatus) {
		this.displayStatus = displayStatus;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
