package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "t_work_month")
public class WorkMonth extends DataEntity{

	private String work_month;

	private String totalworktime;
	
	/**
	 * 城市
	 */
	@Column(length = 45, name = "cityname")
	private String cityname;

	public String getWork_month() {
		return work_month;
	}

	public void setWork_month(String work_month) {
		this.work_month = work_month;
	}

	public String getTotalworktime() {
		return totalworktime;
	}

	public void setTotalworktime(String totalworktime) {
		this.totalworktime = totalworktime;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
}
