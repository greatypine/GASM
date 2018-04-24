/**
 * gaobaolei
 */
package com.cnpc.pms.mongodb.dto;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author gaobaolei
 * 小区高德坐标
 */
public class TinyVillageCoordDto {
	/**
	 * 小区ID
	 */
	private Long tiny_village_id;
	/**
	 * A国安侠 
	 */
	private String employee_a_no;
	/**
	 * B国安侠
	 */
	private String employee_b_no;
	
	/**
	 * 片区编号
	 */
	private String area_no;
	
	/**
	 * 门店ID
	 */
	private Long store_id;
	/**
	 * 片区名称
	 */
	private String area_name;
	/**
	 * 小区面积
	 */
	private String vallage_area;
	public String getArea_no() {
		return area_no;
	}
	public void setArea_no(String area_no) {
		this.area_no = area_no;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	/**
	 * 坐标集合
	 */
	private String coord;
	public Long getTiny_village_id() {
		return tiny_village_id;
	}
	public void setTiny_village_id(Long tiny_village_id) {
		this.tiny_village_id = tiny_village_id;
	}
	public String getEmployee_a_no() {
		return employee_a_no;
	}
	public void setEmployee_a_no(String employee_a_no) {
		this.employee_a_no = employee_a_no;
	}
	public String getEmployee_b_no() {
		return employee_b_no;
	}
	public void setEmployee_b_no(String employee_b_no) {
		this.employee_b_no = employee_b_no;
	}
	public String getCoord() {
		return coord;
	}
	public void setCoord(String coord) {
		this.coord = coord;
	}
	public Long getStore_id() {
		return store_id;
	}
	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}
	public String getVallage_area() {
		return vallage_area;
	}
	public void setVallage_area(String vallage_area) {
		this.vallage_area = vallage_area;
	}
	
	
	
	
	
	
	
}
