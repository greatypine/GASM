/**
 * gaobaolei
 */
package com.cnpc.pms.slice.dto;

/**
 * @author gaobaolei
 * 
 */
public class AreaDto {
	
	//门店ID
	private Long store_id;
	//片区ID
	private Long area_id;
	//片区名称
	private String name;
	//社区
	private String villageName;
	//小区
	private String tinVillageName;
	
	private String employeeName;
	
	private Long city_id;
	
	private Integer target;
	
	
	private String area_no;
	
	private String townName;
	
	public Long getArea_id() {
		return area_id;
	}
	public void setArea_id(Long area_id) {
		this.area_id = area_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVillageName() {
		return villageName;
	}
	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}
	public String getTinVillageName() {
		return tinVillageName;
	}
	public void setTinVillageName(String tinVillageName) {
		this.tinVillageName = tinVillageName;
	}
	public Long getStore_id() {
		return store_id;
	}
	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Long getCity_id() {
		return city_id;
	}
	public void setCity_id(Long city_id) {
		this.city_id = city_id;
	}
	public Integer getTarget() {
		return target;
	}
	public void setTarget(Integer target) {
		this.target = target;
	}
	public String getArea_no() {
		return area_no;
	}
	public void setArea_no(String area_no) {
		this.area_no = area_no;
	}
	public String getTownName() {
		return townName;
	}
	public void setTownName(String townName) {
		this.townName = townName;
	}
	
	
	
	
	
}
