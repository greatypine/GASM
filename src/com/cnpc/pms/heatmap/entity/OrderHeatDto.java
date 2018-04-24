package com.cnpc.pms.heatmap.entity;
/**
 * 
 * @author gaoll
 *
 */
public class OrderHeatDto {
	
	private Long store_id;
	private Long city_id;
	private Long employee_id;
	private Integer target;
	private String beginDate;
	private String endDate;
	private String type;
	private Integer fromVillage;
	public Integer getFromVillage() {
		return fromVillage;
	}
	public void setFromVillage(Integer fromVillage) {
		this.fromVillage = fromVillage;
	}
	public Long getStore_id() {
		return store_id;
	}
	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}
	public Long getCity_id() {
		return city_id;
	}
	public void setCity_id(Long city_id) {
		this.city_id = city_id;
	}
	public Long getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(Long employee_id) {
		this.employee_id = employee_id;
	}
	public Integer getTarget() {
		return target;
	}
	public void setTarget(Integer target) {
		this.target = target;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
