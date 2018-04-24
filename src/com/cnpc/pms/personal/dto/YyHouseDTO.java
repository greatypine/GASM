package com.cnpc.pms.personal.dto;

import java.util.Date;

/**
 * 物业事业部数据DTO
 * @author zhaoxg 2016-7-22
 *
 */
public class YyHouseDTO {

	private Long id;
	private String name;
	private Date date;
	private String createuser;
	
	private String date_str;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getDate_str() {
		return date_str;
	}
	public void setDate_str(String date_str) {
		this.date_str = date_str;
	}
}
