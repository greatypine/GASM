package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "t_flow_detail")
public class FlowDetail extends DataEntity{
	/**
	 * 任务ID 
	 */
	@Column(name = "work_info_id")
	private Long work_info_id;
	
	/**
	 * 审批人 
	 */
	@Column(name = "user_id")
	private Long user_id;
	
	/**
     * 时间
     */
    @Column(name = "approv_time")
    private Date approv_time;
	
    /**
	 * 内容
	 */
	@Column(length = 255, name = "approv_content")
	private String approv_content;
	
	/**
	 * 结果
	 */
	@Column(length = 45, name = "approv_ret")
	private String approv_ret;
	
	
	
	


	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Date getApprov_time() {
		return approv_time;
	}

	public void setApprov_time(Date approv_time) {
		this.approv_time = approv_time;
	}

	public String getApprov_content() {
		return approv_content;
	}

	public void setApprov_content(String approv_content) {
		this.approv_content = approv_content;
	}

	public String getApprov_ret() {
		return approv_ret;
	}

	public void setApprov_ret(String approv_ret) {
		this.approv_ret = approv_ret;
	}

	public Long getWork_info_id() {
		return work_info_id;
	}

	public void setWork_info_id(Long work_info_id) {
		this.work_info_id = work_info_id;
	}
    
    
    
}
