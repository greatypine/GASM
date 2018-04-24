package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_score_record")
public class ScoreRecord extends DataEntity{
	
	@Column(name = "scorerecord_id")
	private Long scorerecord_id;
	/**
	 * 工号
	 */
	@Column(length = 45, name = "employee_no")
	private String employee_no;
	/**
	 * 月份
	 */
	@Column(length = 45, name = "score_month")
	private String work_month;
	
	//成绩1
	@Column(name = "mixedType_repeatBuyCostomer")
	private String mixedType_repeatBuyCostomer;
	//营业额
	@Column(name = "turnover")
	private String turnover;
	//绩效
	@Column(name = "score")
	private String score;
	
	//绩效
	@Column(name = "emprise")
	private String emprise;
		
	
	@Column(name = "storeroom")
	private String storeroom;
	
	@Transient
	private String store_name;
	@Transient
	private Long store_id;
	@Transient
	private String employee_name;
	@Transient
	private String authorizedtype;
	@Transient
	private String leavedate;
	@Transient
	private String topostdate;
	@Transient
	private Long humanstatus;
	
	@Transient
	private String zw;
	
	
	
	
	
	
	/*@Transient
	private String employee_name;
	//绩效分   ??什么类型？？？
	@Column(length = 45,name = "score")
	private String score;*/

	
	
	
	public String getEmployee_no() {
		return employee_no;
	}
	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}
	public String getWork_month() {
		return work_month;
	}
	public void setWork_month(String work_month) {
		this.work_month = work_month;
	}
	public Long getScorerecord_id() {
		return scorerecord_id;
	}
	public void setScorerecord_id(Long scorerecord_id) {
		this.scorerecord_id = scorerecord_id;
	}
	public String getMixedType_repeatBuyCostomer() {
		return mixedType_repeatBuyCostomer;
	}
	public void setMixedType_repeatBuyCostomer(String mixedType_repeatBuyCostomer) {
		this.mixedType_repeatBuyCostomer = mixedType_repeatBuyCostomer;
	}
	public String getTurnover() {
		return turnover;
	}
	public void setTurnover(String turnover) {
		this.turnover = turnover;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public Long getStore_id() {
		return store_id;
	}
	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getAuthorizedtype() {
		return authorizedtype;
	}
	public void setAuthorizedtype(String authorizedtype) {
		this.authorizedtype = authorizedtype;
	}
	public String getEmprise() {
		return emprise;
	}
	public void setEmprise(String emprise) {
		this.emprise = emprise;
	}
	public String getLeavedate() {
		return leavedate;
	}
	public void setLeavedate(String leavedate) {
		this.leavedate = leavedate;
	}
	public String getTopostdate() {
		return topostdate;
	}
	public void setTopostdate(String topostdate) {
		this.topostdate = topostdate;
	}
	public Long getHumanstatus() {
		return humanstatus;
	}
	public void setHumanstatus(Long humanstatus) {
		this.humanstatus = humanstatus;
	}
	public String getStoreroom() {
		return storeroom;
	}
	public void setStoreroom(String storeroom) {
		this.storeroom = storeroom;
	}
	public String getZw() {
		return zw;
	}
	public void setZw(String zw) {
		this.zw = zw;
	}
	
}
