package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "t_work_info")
public class WorkInfo extends DataEntity{
	/**
	 * 类型 
	 */
	@Column(length = 45, name = "work_type")
	private String work_type;
	
	/**
	 * 发起人 
	 */
	@Column(name = "user_id")
	private Long user_id;
	
	@Transient
	private String user_id_name;
	
	@Transient
	private String curr_appro_id_name;
	
	@Column(name = "curr_appro_id")
	private String curr_appro_id;
	
	/**
     * 发起时间
     */
    @Column(name = "start_time")
    private Date start_time;
	
	/**
	 * 门店ID
	 */
	@Column(name = "store_id")
	private Long store_id;

	@Transient
	private String store_name;
	
	/**
	 * 月份
	 */
	@Column(length = 45, name = "work_month")
	private String work_month;
	
	/**
	 * 提交状态
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

	
	
	/**
	 * 备注
	 */
	@Column(length = 225, name = "order_sn")
	private String order_sn;
	/**
	 * 申诉原因
	 */
	@Column(length = 2000, name = "app_reason")
	private String app_reason;
	
	
	@Transient
	private String curr_appro_group_name;
	
	
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

	public String getWork_type() {
		return work_type;
	}

	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getCurr_appro_id() {
		return curr_appro_id;
	}

	public void setCurr_appro_id(String curr_appro_id) {
		this.curr_appro_id = curr_appro_id;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public String getUser_id_name() {
		return user_id_name;
	}

	public void setUser_id_name(String user_id_name) {
		this.user_id_name = user_id_name;
	}

	public String getCurr_appro_id_name() {
		return curr_appro_id_name;
	}

	public void setCurr_appro_id_name(String curr_appro_id_name) {
		this.curr_appro_id_name = curr_appro_id_name;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getApp_reason() {
		return app_reason;
	}

	public void setApp_reason(String app_reason) {
		this.app_reason = app_reason;
	}

	public String getCurr_appro_group_name() {
		return curr_appro_group_name;
	}

	public void setCurr_appro_group_name(String curr_appro_group_name) {
		this.curr_appro_group_name = curr_appro_group_name;
	}
}
