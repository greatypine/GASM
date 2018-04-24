package com.cnpc.pms.heatmap.entity;
/**
 * 
 * @author gaoll
 *
 */
public class RequestInfoDto {
	private Long cityId;
	private Long storeId;
	private String beginDate;
	private String endDate;
	private String employeeNo;
	private String isRealtime;
	public String getIsRealtime() {
		return isRealtime;
	}
	public void setIsRealtime(String isRealtime) {
		this.isRealtime = isRealtime;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	
	
}
