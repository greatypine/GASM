package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_dist_citycode")
public class DistCityCode extends DataEntity{
	
	/**
	 * 城市名称
	 */
	@Column(length = 45, name = "cityname")
	private String cityname;
	
	/**
	 * 城市代码
	 */
	@Column(length = 45, name = "citycode")
	private String citycode;
	/**
	 * 城市编码
	 */
	@Column(length = 45, name = "cityno")
	private String cityno;
	
	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getCityno() {
		return cityno;
	}

	public void setCityno(String cityno) {
		this.cityno = cityno;
	}
	

	
}
