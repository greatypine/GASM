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
@Table(name = "t_work_record")
public class WorkRecord extends DataEntity{
	/**
	 * 门店ID
	 */
	/*@Column(name = "store_id")
	private Long store_id;*/
	@Column(name = "workrecord_id")
	private Long workrecord_id;
	/**
	 * 工号
	 */
	@Column(length = 45, name = "employee_no")
	private String employee_no;
	
	/**
	 * 考勤月份
	 */
	@Column(length = 45, name = "work_month")
	private String work_month;
	
	/**
	 * 提交日期
	 *//*
	@Column(length = 45,name = "commit_date")
	private String commit_date;*/
	
	/**
	 * 提交状态 是否提交状态
	 *//*
	@Column(name = "commit_status")
	private Long commit_status;*/
	
	@Transient
	private String store_name;
	
	@Transient
	private Long store_id;
	
	@Transient
	private String employee_name;
	@Transient
	private String authorizedtype;
	
	//离职日期
	@Transient
	private String leavedate;
	@Transient
	private String topostdate;
	@Transient
	private Long humanstatus;

	

	@Column(length = 45, name = "day1")
	private String day1;
	@Column(length = 45, name = "day2")
	private String day2;
	@Column(length = 45, name = "day3")
	private String day3;
	@Column(length = 45, name = "day4")
	private String day4;
	@Column(length = 45, name = "day5")
	private String day5;
	@Column(length = 45, name = "day6")
	private String day6;
	@Column(length = 45, name = "day7")
	private String day7;
	@Column(length = 45, name = "day8")
	private String day8;
	@Column(length = 45, name = "day9")
	private String day9;
	@Column(length = 45, name = "day10")
	private String day10;
	@Column(length = 45, name = "day11")
	private String day11;
	@Column(length = 45, name = "day12")
	private String day12;
	@Column(length = 45, name = "day13")
	private String day13;
	@Column(length = 45, name = "day14")
	private String day14;
	@Column(length = 45, name = "day15")
	private String day15;
	@Column(length = 45, name = "day16")
	private String day16;
	@Column(length = 45, name = "day17")
	private String day17;
	@Column(length = 45, name = "day18")
	private String day18;
	@Column(length = 45, name = "day19")
	private String day19;
	@Column(length = 45, name = "day20")
	private String day20;
	@Column(length = 45, name = "day21")
	private String day21;
	@Column(length = 45, name = "day22")
	private String day22;
	@Column(length = 45, name = "day23")
	private String day23;
	@Column(length = 45, name = "day24")
	private String day24;
	@Column(length = 45, name = "day25")
	private String day25;
	@Column(length = 45, name = "day26")
	private String day26;
	@Column(length = 45, name = "day27")
	private String day27;
	@Column(length = 45, name = "day28")
	private String day28;
	@Column(length = 45, name = "day29")
	private String day29;
	@Column(length = 45, name = "day30")
	private String day30;
	@Column(length = 45, name = "day31")
	private String day31;
	
	
	//出勤	
	@Column(length = 45,name = "workdays")
	private String workdays;
	
	//倒休					
	@Column(length = 45,name = "adjholiday")
	private String adjholiday;
	
	//事假	
	@Column(length = 45,name = "eventday")
	private String eventday;
	
	//病假	
	@Column(length = 45,name = "badday")
	private String badday;
	
	//婚假
	@Column(length = 45,name = "wedday")
	private String wedday;
	
	//产假
	@Column(length = 45,name = "proday")
	private String proday;
	
	//丧假	
	@Column(length = 45,name = "loseday")
	private String loseday;
	
	//年休假
	@Column(length = 45,name = "yearholiday")
	private String yearholiday;
	
	//出差
	@Column(length = 45,name = "tripday")
	private String tripday;
	
	//工伤假
	@Column(length = 45,name = "workhurtday")
	private String workhurtday;
	
	//旷工
	@Column(length = 45,name = "absenceday")
	private String absenceday;
	
	//迟到
	@Column(length = 45,name = "lateday")
	private String lateday;
	
	//早退
	@Column(length = 45,name = "leaveearlyday")
	private String leaveearlyday;
	
	//是否正式
	@Column(length = 10, name = "isauth")
	private String isauth;
	
	//加班工时填报
	@Column(length = 45,name = "overtime")
	private String overtime;
	//排班总工时
	@Column(name = "totalovertime")
	private Double totalovertime;
	//出勤总工时
	@Column(name = "realovertime")
	private Double realovertime;
	
	//饭补天数
	@Column(length = 45,name = "allowdays")
	private String allowdays;
	
	//个人签字
	@Column(length = 45,name = "signname")
	private String signname;
	
	//绩效分 
	@Column(length = 45,name = "score")
	private String score;

	

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


	public String getDay1() {
		return day1;
	}

	public void setDay1(String day1) {
		this.day1 = day1;
	}

	public String getDay2() {
		return day2;
	}

	public void setDay2(String day2) {
		this.day2 = day2;
	}

	public String getDay3() {
		return day3;
	}

	public void setDay3(String day3) {
		this.day3 = day3;
	}

	public String getDay4() {
		return day4;
	}

	public void setDay4(String day4) {
		this.day4 = day4;
	}

	public String getDay5() {
		return day5;
	}

	public void setDay5(String day5) {
		this.day5 = day5;
	}

	public String getDay6() {
		return day6;
	}

	public void setDay6(String day6) {
		this.day6 = day6;
	}

	public String getDay7() {
		return day7;
	}

	public void setDay7(String day7) {
		this.day7 = day7;
	}

	public String getDay8() {
		return day8;
	}

	public void setDay8(String day8) {
		this.day8 = day8;
	}

	public String getDay9() {
		return day9;
	}

	public void setDay9(String day9) {
		this.day9 = day9;
	}

	public String getDay10() {
		return day10;
	}

	public void setDay10(String day10) {
		this.day10 = day10;
	}

	public String getDay11() {
		return day11;
	}

	public void setDay11(String day11) {
		this.day11 = day11;
	}

	public String getDay12() {
		return day12;
	}

	public void setDay12(String day12) {
		this.day12 = day12;
	}

	public String getDay13() {
		return day13;
	}

	public void setDay13(String day13) {
		this.day13 = day13;
	}

	public String getDay14() {
		return day14;
	}

	public void setDay14(String day14) {
		this.day14 = day14;
	}

	public String getDay15() {
		return day15;
	}

	public void setDay15(String day15) {
		this.day15 = day15;
	}

	public String getDay16() {
		return day16;
	}

	public void setDay16(String day16) {
		this.day16 = day16;
	}

	public String getDay17() {
		return day17;
	}

	public void setDay17(String day17) {
		this.day17 = day17;
	}

	public String getDay18() {
		return day18;
	}

	public void setDay18(String day18) {
		this.day18 = day18;
	}

	public String getDay19() {
		return day19;
	}

	public void setDay19(String day19) {
		this.day19 = day19;
	}

	public String getDay20() {
		return day20;
	}

	public void setDay20(String day20) {
		this.day20 = day20;
	}

	public String getDay21() {
		return day21;
	}

	public void setDay21(String day21) {
		this.day21 = day21;
	}

	public String getDay22() {
		return day22;
	}

	public void setDay22(String day22) {
		this.day22 = day22;
	}

	public String getDay23() {
		return day23;
	}

	public void setDay23(String day23) {
		this.day23 = day23;
	}

	public String getDay24() {
		return day24;
	}

	public void setDay24(String day24) {
		this.day24 = day24;
	}

	public String getDay25() {
		return day25;
	}

	public void setDay25(String day25) {
		this.day25 = day25;
	}

	public String getDay26() {
		return day26;
	}

	public void setDay26(String day26) {
		this.day26 = day26;
	}

	public String getDay27() {
		return day27;
	}

	public void setDay27(String day27) {
		this.day27 = day27;
	}

	public String getDay28() {
		return day28;
	}

	public void setDay28(String day28) {
		this.day28 = day28;
	}

	public String getDay29() {
		return day29;
	}

	public void setDay29(String day29) {
		this.day29 = day29;
	}

	public String getDay30() {
		return day30;
	}

	public void setDay30(String day30) {
		this.day30 = day30;
	}

	public String getDay31() {
		return day31;
	}

	public void setDay31(String day31) {
		this.day31 = day31;
	}

	public String getWorkdays() {
		return workdays;
	}

	public void setWorkdays(String workdays) {
		this.workdays = workdays;
	}

	public String getAdjholiday() {
		return adjholiday;
	}

	public void setAdjholiday(String adjholiday) {
		this.adjholiday = adjholiday;
	}

	public String getEventday() {
		return eventday;
	}

	public void setEventday(String eventday) {
		this.eventday = eventday;
	}

	public String getBadday() {
		return badday;
	}

	public void setBadday(String badday) {
		this.badday = badday;
	}

	public String getWedday() {
		return wedday;
	}

	public void setWedday(String wedday) {
		this.wedday = wedday;
	}

	public String getProday() {
		return proday;
	}

	public void setProday(String proday) {
		this.proday = proday;
	}

	public String getLoseday() {
		return loseday;
	}

	public void setLoseday(String loseday) {
		this.loseday = loseday;
	}

	public String getYearholiday() {
		return yearholiday;
	}

	public void setYearholiday(String yearholiday) {
		this.yearholiday = yearholiday;
	}

	public String getTripday() {
		return tripday;
	}

	public void setTripday(String tripday) {
		this.tripday = tripday;
	}

	public String getWorkhurtday() {
		return workhurtday;
	}

	public void setWorkhurtday(String workhurtday) {
		this.workhurtday = workhurtday;
	}

	public String getAbsenceday() {
		return absenceday;
	}

	public void setAbsenceday(String absenceday) {
		this.absenceday = absenceday;
	}

	public String getLateday() {
		return lateday;
	}

	public void setLateday(String lateday) {
		this.lateday = lateday;
	}

	public String getLeaveearlyday() {
		return leaveearlyday;
	}

	public void setLeaveearlyday(String leaveearlyday) {
		this.leaveearlyday = leaveearlyday;
	}

	public String getIsauth() {
		return isauth;
	}

	public void setIsauth(String isauth) {
		this.isauth = isauth;
	}

	public String getAllowdays() {
		return allowdays;
	}

	public void setAllowdays(String allowdays) {
		this.allowdays = allowdays;
	}

	public String getSignname() {
		return signname;
	}

	public void setSignname(String signname) {
		this.signname = signname;
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

	public Long getWorkrecord_id() {
		return workrecord_id;
	}

	public void setWorkrecord_id(Long workrecord_id) {
		this.workrecord_id = workrecord_id;
	}

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
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

	public String getOvertime() {
		return overtime;
	}

	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}

	public Double getTotalovertime() {
		return totalovertime;
	}

	public void setTotalovertime(double totalovertime) {
		this.totalovertime = totalovertime;
	}

	public Double getRealovertime() {
		return realovertime;
	}

	public void setRealovertime(double realovertime) {
		this.realovertime = realovertime;
	}
	
}
