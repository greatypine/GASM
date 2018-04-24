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
@Table(name = "t_dist_city")
public class DistCity extends DataEntity{
	
	/**
	 * 用户ID
	 */
	@Column(name = "pk_userid")
	private Long pk_userid;
	
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
	
	@Transient
	private Long cityid;
	@Transient
	private String username;
	@Transient
	private String city1;
	@Transient
	private String city2;
	@Transient
	private String city3;
	@Transient
	private String city4;
	@Transient
	private String city5;
	@Transient
	private Boolean isSelectAll;

	public Long getPk_userid() {
		return pk_userid;
	}

	public void setPk_userid(Long pk_userid) {
		this.pk_userid = pk_userid;
	}

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCity1() {
		return city1;
	}

	public void setCity1(String city1) {
		this.city1 = city1;
	}

	public String getCity2() {
		return city2;
	}

	public void setCity2(String city2) {
		this.city2 = city2;
	}

	public String getCity3() {
		return city3;
	}

	public void setCity3(String city3) {
		this.city3 = city3;
	}

	public String getCity4() {
		return city4;
	}

	public void setCity4(String city4) {
		this.city4 = city4;
	}

	public String getCity5() {
		return city5;
	}

	public void setCity5(String city5) {
		this.city5 = city5;
	}

	public Boolean getIsSelectAll() {
		return isSelectAll;
	}

	public void setIsSelectAll(Boolean isSelectAll) {
		this.isSelectAll = isSelectAll;
	}

	public Long getCityid() {
		return cityid;
	}

	public void setCityid(Long cityid) {
		this.cityid = cityid;
	}
	
}
