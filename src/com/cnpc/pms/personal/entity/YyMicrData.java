package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "yy_micr_data_collect")
public class YyMicrData extends DataEntity{
	private static final long serialVersionUID = 1L;
	/**
	 * 名称
	 */
	@Column(length = 32, name = "name")
	private String name;
	
	/**
	 * 名称
	 */
	@Column(length = 32, name = "micr_type")
	private String micr_type;
	/**
	 *周
	 */
	@Column(name = "week")
	private double week;
	/**
	 * 月
	 */
	@Column(name = "month")
	private double month;
	
	/**
	 * 总计
	 **/
	@Column(name = "total")
	private double total;
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
	/**
	 *类型
	 */
	@Column(length = 20, name = "type")
	private String type;
	
	
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMicr_type() {
		return micr_type;
	}
	public void setMicr_type(String micr_type) {
		this.micr_type = micr_type;
	}
	public double getWeek() {
		return week;
	}
	public void setWeek(double week) {
		this.week = week;
	}
	public double getMonth() {
		return month;
	}
	public void setMonth(double month) {
		this.month = month;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

	

	
}
