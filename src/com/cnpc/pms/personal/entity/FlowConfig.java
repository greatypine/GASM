package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "t_flow_config")
public class FlowConfig extends DataEntity{
	/**
	 * 任务名称 
	 */
	@Column(length = 45, name = "work_name")
	private String work_name;
	
	/**
	 * 发起角色组
	 */
	@Column(name = "start_usergroup_id")
	private Long start_usergroup_id;
	
	@Transient
	private String start_usergroup_id_name;
	/**
	 * 第一审批角色
	 */
	@Column(name = "first_usergroup_id")
	private Long first_usergroup_id;
	
	@Transient
	private String first_usergroup_id_name;
	
	/**
	 * 第二审批角色
	 */
	@Column(name = "second_usergroup_id")
	private Long second_usergroup_id;
	
	@Transient
	private String second_usergroup_id_name;
	
	/**
	 * 第三审批角色
	 */
	@Column(name = "third_usergroup_id")
	private Long third_usergroup_id;
	
	@Transient
	private String third_usergroup_id_name;
	
	
	@Column(name = "isshowname")
	private Long isshowname;

	public String getWork_name() {
		return work_name;
	}

	public void setWork_name(String work_name) {
		this.work_name = work_name;
	}

	public Long getStart_usergroup_id() {
		return start_usergroup_id;
	}

	public void setStart_usergroup_id(Long start_usergroup_id) {
		this.start_usergroup_id = start_usergroup_id;
	}

	public String getStart_usergroup_id_name() {
		return start_usergroup_id_name;
	}

	public void setStart_usergroup_id_name(String start_usergroup_id_name) {
		this.start_usergroup_id_name = start_usergroup_id_name;
	}

	public Long getFirst_usergroup_id() {
		return first_usergroup_id;
	}

	public void setFirst_usergroup_id(Long first_usergroup_id) {
		this.first_usergroup_id = first_usergroup_id;
	}

	public String getFirst_usergroup_id_name() {
		return first_usergroup_id_name;
	}

	public void setFirst_usergroup_id_name(String first_usergroup_id_name) {
		this.first_usergroup_id_name = first_usergroup_id_name;
	}

	public Long getSecond_usergroup_id() {
		return second_usergroup_id;
	}

	public void setSecond_usergroup_id(Long second_usergroup_id) {
		this.second_usergroup_id = second_usergroup_id;
	}

	public String getSecond_usergroup_id_name() {
		return second_usergroup_id_name;
	}

	public void setSecond_usergroup_id_name(String second_usergroup_id_name) {
		this.second_usergroup_id_name = second_usergroup_id_name;
	}

	public Long getThird_usergroup_id() {
		return third_usergroup_id;
	}

	public void setThird_usergroup_id(Long third_usergroup_id) {
		this.third_usergroup_id = third_usergroup_id;
	}

	public String getThird_usergroup_id_name() {
		return third_usergroup_id_name;
	}

	public void setThird_usergroup_id_name(String third_usergroup_id_name) {
		this.third_usergroup_id_name = third_usergroup_id_name;
	}

	public Long getIsshowname() {
		return isshowname;
	}

	public void setIsshowname(Long isshowname) {
		this.isshowname = isshowname;
	}
	
	
	
	
	
}
