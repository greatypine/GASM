package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "t_score_record_total")
public class ScoreRecordTotal extends DataEntity{
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
	private String curr_appro_id_name;

	@Transient
	private String str_commit_date;

	@Column(name = "submit_user_id")
	private Long submit_user_id;
	
	
	@Column(name = "work_info_id")
	private Long work_info_id;
	
	
	@Column(length = 45, name = "str_work_info_id")
	private String str_work_info_id;
	
	
	@Transient
	private String curr_appro_group_name;
	
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
	@OneToMany(targetEntity = ScoreRecord.class, cascade = CascadeType.ALL, mappedBy = "scorerecord_id")
	private Set<ScoreRecord> childs;

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

	public Set<ScoreRecord> getChilds() {
		return childs;
	}

	public void setChilds(Set<ScoreRecord> childs) {
		this.childs = childs;
	}

	public Long getWork_info_id() {
		return work_info_id;
	}

	public void setWork_info_id(Long work_info_id) {
		this.work_info_id = work_info_id;
	}

	public String getStr_work_info_id() {
		return str_work_info_id;
	}

	public void setStr_work_info_id(String str_work_info_id) {
		this.str_work_info_id = str_work_info_id;
	}

	public String getCurr_appro_id_name() {
		return curr_appro_id_name;
	}

	public void setCurr_appro_id_name(String curr_appro_id_name) {
		this.curr_appro_id_name = curr_appro_id_name;
	}

	public String getCurr_appro_group_name() {
		return curr_appro_group_name;
	}

	public void setCurr_appro_group_name(String curr_appro_group_name) {
		this.curr_appro_group_name = curr_appro_group_name;
	}
}
