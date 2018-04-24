package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name="t_tiny_coordinate")
public class TinyCoordinate extends DataEntity{
	@Column(name="tiny_id")
	private Long tiny_id;
	@Column(name="gaode_coordinate_x",length=30)
	private String gaode_coordinate_x;
	@Column(name="gaode_coordinate_y",length=30)
	private String gaode_coordinate_y;
	@Transient
	private String coordinates;
	
	public String getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	public Long getTiny_id() {
		return tiny_id;
	}
	public void setTiny_id(Long tiny_id) {
		this.tiny_id = tiny_id;
	}
	public String getGaode_coordinate_x() {
		return gaode_coordinate_x;
	}
	public void setGaode_coordinate_x(String gaode_coordinate_x) {
		this.gaode_coordinate_x = gaode_coordinate_x;
	}
	public String getGaode_coordinate_y() {
		return gaode_coordinate_y;
	}
	public void setGaode_coordinate_y(String gaode_coordinate_y) {
		this.gaode_coordinate_y = gaode_coordinate_y;
	}
	
	
	

}
