package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "yy_house_analysis")
public class YyStore extends DataEntity{
	private static final long serialVersionUID = 1L;
	/**
	 * 名称
	 */
	@Column(length = 32, name = "name")
	private String name;
	
	/**
	 * 数量
	 */
	@Column(name = "num")
	private Integer num;
	
	/**
	 * 日期
	 */
	@Column(name = "date")
	private Date date;
	
	/**
	 * 备注
	 */
	@Column(length = 500, name = "remark")
	private String remark;
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}
