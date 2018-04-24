package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_outercompany")
public class OuterCompany extends DataEntity{

	/**
	 * 外包公司名称
	 */
	@Column(length = 45, name = "companyname")
	private String companyname;
	
	/**
	 * 外包公司代码
	 */
	@Column(length = 45, name = "companycode")
	private String companycode;
	
	/**
	 * 公司全称
	 */
	@Column(length = 45, name = "companyallname")
	private String companyallname;
	

	public String getCompanyname() {
		return companyname;
	}


	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}


	public String getCompanycode() {
		return companycode;
	}


	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}


	public String getCompanyallname() {
		return companyallname;
	}


	public void setCompanyallname(String companyallname) {
		this.companyallname = companyallname;
	}


	
	
	
	
	
	
	
	
	
	
	
	
}
