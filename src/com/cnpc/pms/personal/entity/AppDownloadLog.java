package com.cnpc.pms.personal.entity;

import java.util.Date;

import com.cnpc.pms.base.entity.DataEntity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_app_download_log")
public class AppDownloadLog extends DataEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(length = 45, name = "cityname")
	private String cityname;
	
	@Column(length = 45, name = "dip")
	private String dip;
	
	@Column(length = 45, name = "downloadtype")
	private String downloadtype;
	
	@Column(name="downloadDate")
	private Date downloadDate;
	
	@Column(length = 45, name = "downloadVersion")
	private String downloadVersion;

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getDip() {
		return dip;
	}

	public void setDip(String dip) {
		this.dip = dip;
	}

	public String getDownloadtype() {
		return downloadtype;
	}

	public void setDownloadtype(String downloadtype) {
		this.downloadtype = downloadtype;
	}

	public Date getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(Date downloadDate) {
		this.downloadDate = downloadDate;
	}

	public String getDownloadVersion() {
		return downloadVersion;
	}

	public void setDownloadVersion(String downloadVersion) {
		this.downloadVersion = downloadVersion;
	}
	
	
}
