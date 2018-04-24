package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
/**
 * 保存的为每次修改的楼房数据信息
 * @author sunning
 *
 */
@Entity
@Table(name = "t_house_building")
public class HouseBuilding extends DataEntity{
	@Column(name="buildingid",length=255)
	private String buildingid;
	@Column(name="tinyvillageId",length=20)
	private Long tinyvillageId;
	
	public String getBuildingid() {
		return buildingid;
	}
	public void setBuildingid(String buildingid) {
		this.buildingid = buildingid;
	}
	public Long getTinyvillageId() {
		return tinyvillageId;
	}
	public void setTinyvillageId(Long tinyvillageId) {
		this.tinyvillageId = tinyvillageId;
	}
	
	
}
