package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_desc")
public class CustomerDesc extends DataEntity{
	//用户ID
	@Column(name = "customer_id")
	private Long customer_id;
	//民族
	@Column(name = "nationality")
	private String  nationality;
	//住宅id
	@Column(name = "house_id")
	private Long house_id;
	//住宅种类 
	@Column(name = "house_type")
	private Integer house_type;
	// 手机品牌
	@Column(name = "mb_brand")
	private String mb_brand;
	@Column(name = "job")
	private String job;
	// 岗位
	@Column(name = "position")
	private String position;
	//职位高低
	@Column(name = "position_level")
	private String  position_level;
	//工作地点
	@Column(name = "working_place")
	private String working_place;
	//上班时间
	@Column(name = "working_from")
	private Date working_from;
	//下班时间
	@Column(name = "working_to")
	private Date working_to;
	//周末休假天数
	@Column(name = "weekend_days")
	private Integer weekend_days;
	//加班强度
	@Column(name = "overtime_level")
	private String overtime_level;	
	// 婚姻情况
	@Column(name = "marital_status")
	private Integer marital_status;
	// 子女情况
	@Column(name = "children_status")
	private String children_status;
	// 消费标签
	@Column(name = "consume_tag")
	private String consume_tag;
	// 用户标记
	@Column(name = "member_flag")
	private Integer member_flag;
	// 地址
	@Column(name = "address")
	private String address;
	
	
	
	public Long getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public Long getHouse_id() {
		return house_id;
	}
	public void setHouse_id(Long house_id) {
		this.house_id = house_id;
	}
	public Integer getHouse_type() {
		return house_type;
	}
	public void setHouse_type(Integer house_type) {
		this.house_type = house_type;
	}
	public String getMb_brand() {
		return mb_brand;
	}
	public void setMb_brand(String mb_brand) {
		this.mb_brand = mb_brand;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getPosition_level() {
		return position_level;
	}
	public void setPosition_level(String position_level) {
		this.position_level = position_level;
	}
	public String getWorking_place() {
		return working_place;
	}
	public void setWorking_place(String working_place) {
		this.working_place = working_place;
	}
	public Date getWorking_from() {
		return working_from;
	}
	public void setWorking_from(Date working_from) {
		this.working_from = working_from;
	}
	public Date getWorking_to() {
		return working_to;
	}
	public void setWorking_to(Date working_to) {
		this.working_to = working_to;
	}
	public Integer getWeekend_days() {
		return weekend_days;
	}
	public void setWeekend_days(Integer weekend_days) {
		this.weekend_days = weekend_days;
	}
	public String getOvertime_level() {
		return overtime_level;
	}
	public void setOvertime_level(String overtime_level) {
		this.overtime_level = overtime_level;
	}
	public Integer getMarital_status() {
		return marital_status;
	}
	public void setMarital_status(Integer marital_status) {
		this.marital_status = marital_status;
	}
	public String getChildren_status() {
		return children_status;
	}
	public void setChildren_status(String children_status) {
		this.children_status = children_status;
	}
	public String getConsume_tag() {
		return consume_tag;
	}
	public void setConsume_tag(String consume_tag) {
		this.consume_tag = consume_tag;
	}
	public Integer getMember_flag() {
		return member_flag;
	}
	public void setMember_flag(Integer member_flag) {
		this.member_flag = member_flag;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}
