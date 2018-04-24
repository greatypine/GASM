package com.cnpc.pms.bid.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.PMSEntity;
/**
 * 首页模块字典
 * @author IBM
 *
 */
@Entity
@Table(name = "tb_indexmodel")
public class IndexModel extends PMSEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1722364639630175243L;

	/** 模块编号 */
	@Column(name = "modelCode", length = 100)
	private String modelCode;
	
	/** 模块显示名称 */
	@Column(name = "modelText", length = 500)
	private String modelText;
	
	/** 排序编号 */
	@Column(name = "orderNum")
	private Integer orderNum;

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

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
}
