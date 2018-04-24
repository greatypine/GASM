package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name = "t_ferry_push")
public class FerryPush extends DataEntity{
	//早班
	@Column(name = "morning_shift")
	private Integer morningShift;
	//晚班
	@Column(name = "night_shift")
	private Integer nightShift;
	//共计班次(早班+晚班)
	@Column(name = "total_shift")
	private Integer totalShift;
	//百度车小时数
	@Column(name = "ferry_house")
	private Integer ferryHouse;
	//共计小时数(共计班次*摆渡车小时数)
	@Column(name = "total_house")
	private Integer totalHouse;
	//摆渡车单量每小时单量
	@Column(name = "hourly_order_number")
	private Integer hourlyOrderNumber;
	//共计单数(每小时单量*共计小时)
	@Column(name = "total_singular")
	private Integer totalSingular;
	//员工编号
	@Column(name = "employee_no",length=45)
	private String employeeNo;
	//员工姓名
	@Column(name = "user_name",length=45)
	private String userName;
	//填报月份
	@Column(name = "str_month",length=45)
	private String strMonth;
	//所属门店
	@Column(name = "store_id",length=45)
	private Long store_id;
	//所属城市
	@Column(name = "cityName",length=45)
	private String cityName;
	//状态(0.等待审核，1.通过审核，2.退回)
	@Column(name = "state_type",length=45)
	private Integer stateType;
	public Integer getMorningShift() {
		return morningShift;
	}
	public void setMorningShift(Integer morningShift) {
		this.morningShift = morningShift;
	}
	public Integer getNightShift() {
		return nightShift;
	}
	public void setNightShift(Integer nightShift) {
		this.nightShift = nightShift;
	}
	public Integer getTotalShift() {
		return totalShift;
	}
	public void setTotalShift(Integer totalShift) {
		this.totalShift = totalShift;
	}
	public Integer getFerryHouse() {
		return ferryHouse;
	}
	public void setFerryHouse(Integer ferryHouse) {
		this.ferryHouse = ferryHouse;
	}
	public Integer getTotalHouse() {
		return totalHouse;
	}
	public void setTotalHouse(Integer totalHouse) {
		this.totalHouse = totalHouse;
	}
	public Integer getHourlyOrderNumber() {
		return hourlyOrderNumber;
	}
	public void setHourlyOrderNumber(Integer hourlyOrderNumber) {
		this.hourlyOrderNumber = hourlyOrderNumber;
	}
	public Integer getTotalSingular() {
		return totalSingular;
	}
	public void setTotalSingular(Integer totalSingular) {
		this.totalSingular = totalSingular;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStrMonth() {
		return strMonth;
	}
	public void setStrMonth(String strMonth) {
		this.strMonth = strMonth;
	}
	public Long getStore_id() {
		return store_id;
	}
	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Integer getStateType() {
		return stateType;
	}
	public void setStateType(Integer stateType) {
		this.stateType = stateType;
	}
	
	

}
