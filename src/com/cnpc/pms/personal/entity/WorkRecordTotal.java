package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "t_work_record_total")
public class WorkRecordTotal extends DataEntity{
	/**
	 * 门店ID
	 */
	@Column(name = "store_id")
	private Long store_id;

	@Transient
	private String store_name;
	
	/**
	 * 考勤月份
	 */
	@Column(length = 45, name = "work_month")
	private String work_month;
	
	/**
	 * 是否提交状态
	 */
	@Column(name = "commit_status")
	private Long commit_status;

	/**
	 * 提交状态
	 */
	@Transient
	private String str_commit_status;
	
	/**
	 * 提交时间
	 */
	@Column(name = "commit_date")
	private Date commit_date;

	@Transient
	private String str_commit_date;

	@Column(name = "submit_user_id")
	private Long submit_user_id;
	
	
	/**
	 * 城市
	 */
	@Column(length = 45, name = "cityname")
	private String cityname;
	
	
	/**
	 * 备注
	 */
	@Column(length = 225, name = "remark")
	private String remark;

	/** 下级功能节点. */
	@OneToMany(targetEntity = WorkRecord.class, cascade = CascadeType.ALL, mappedBy = "workrecord_id")
	private Set<WorkRecord> childs;

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public String getWork_month() {
		return work_month;
	}

	public void setWork_month(String work_month) {
		this.work_month = work_month;
	}

	public Long getCommit_status() {
		return commit_status;
	}

	public void setCommit_status(Long commit_status) {
		this.commit_status = commit_status;
	}

	public Date getCommit_date() {
		return commit_date;
	}

	public void setCommit_date(Date commit_date) {
		this.commit_date = commit_date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getStr_commit_date() {
		return str_commit_date;
	}

	public void setStr_commit_date(String str_commit_date) {
		this.str_commit_date = str_commit_date;
	}

	public Long getSubmit_user_id() {
		return submit_user_id;
	}

	public void setSubmit_user_id(Long submit_user_id) {
		this.submit_user_id = submit_user_id;
	}

	public String getStr_commit_status() {
		return str_commit_status;
	}

	public void setStr_commit_status(String str_commit_status) {
		this.str_commit_status = str_commit_status;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public Set<WorkRecord> getChilds() {
		return childs;
	}

	public void setChilds(Set<WorkRecord> childs) {
		this.childs = childs;
	}
}
