package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_cityhumanresources_change")
public class CityHumanresourcesChange extends DataEntity{
	
	/**
	 * 姓名
	 */
	@Column(length = 60, name = "name")
	private String name;
	
	
	/**
	 * 类别
	 */
	@Column(length = 60, name = "cityhumanresourcestype")
	private String cityhumanresourcestype;
	
	
	/**
	 * 城市公司名称
	 */
	@Column(length = 60, name = "citycompany")
	private String citycompany;
	
	/**
	 * 部门
	 */
	@Column(length = 60, name = "deptname")
	private String deptname;
	
	/**
	 * 版块
	 */
	@Column(length = 60, name = "deptlevel1")
	private String deptlevel1;
	
	/**
	 * 职级/岗位
	 */
	@Column(length = 60, name = "zw")
	private String zw;
	
	/**
	 * 职级
	 */
	@Column(length = 65, name = "professnallevel")
	private String professnallevel;
	
	
	/**
	 * 劳动合同签订主体
	 */
	@Column(length = 65, name = "contractcompany")
	private String contractcompany;
	
	//-----------调配后的-------------
	
	/**
	 * 调配后 城市公司名称
	 */
	@Column(length = 60, name = "changecitycompany")
	private String changecitycompany;
	
	/**
	 * 调配后 部门
	 */
	@Column(length = 60, name = "changedeptname")
	private String changedeptname;
	
	/**
	 * 调配后 版块
	 */
	@Column(length = 60, name = "changedeptlevel1")
	private String changedeptlevel1;
	
	/**
	 * 调配后 职级/岗位
	 */
	@Column(length = 60, name = "changezw")
	private String changezw;
	
	/**
	 * 调配后 职级
	 */
	@Column(length = 65, name = "changeprofessnallevel")
	private String changeprofessnallevel;
	
	/**
	 * 劳动合同签订主体
	 */
	@Column(length = 65, name = "changecontractcompany")
	private String changecontractcompany;
	
	/**
	 * 异动日期
	 */
	@Column(length = 65, name = "changedate")
	private String changedate;
	
	/**
	 * 备注
	 */
	@Column(length = 255, name = "remark")
	private String remark;
	
	
	/**
	 * 城市
	 */
	@Column(length = 65, name = "citySelect")
	private String citySelect;



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getCityhumanresourcestype() {
		return cityhumanresourcestype;
	}



	public void setCityhumanresourcestype(String cityhumanresourcestype) {
		this.cityhumanresourcestype = cityhumanresourcestype;
	}



	public String getCitycompany() {
		return citycompany;
	}



	public void setCitycompany(String citycompany) {
		this.citycompany = citycompany;
	}



	public String getDeptname() {
		return deptname;
	}



	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}



	public String getDeptlevel1() {
		return deptlevel1;
	}



	public void setDeptlevel1(String deptlevel1) {
		this.deptlevel1 = deptlevel1;
	}



	public String getZw() {
		return zw;
	}



	public void setZw(String zw) {
		this.zw = zw;
	}



	public String getProfessnallevel() {
		return professnallevel;
	}



	public void setProfessnallevel(String professnallevel) {
		this.professnallevel = professnallevel;
	}



	public String getContractcompany() {
		return contractcompany;
	}



	public void setContractcompany(String contractcompany) {
		this.contractcompany = contractcompany;
	}



	public String getChangecitycompany() {
		return changecitycompany;
	}



	public void setChangecitycompany(String changecitycompany) {
		this.changecitycompany = changecitycompany;
	}



	public String getChangedeptname() {
		return changedeptname;
	}



	public void setChangedeptname(String changedeptname) {
		this.changedeptname = changedeptname;
	}



	public String getChangedeptlevel1() {
		return changedeptlevel1;
	}



	public void setChangedeptlevel1(String changedeptlevel1) {
		this.changedeptlevel1 = changedeptlevel1;
	}



	public String getChangezw() {
		return changezw;
	}



	public void setChangezw(String changezw) {
		this.changezw = changezw;
	}



	public String getChangeprofessnallevel() {
		return changeprofessnallevel;
	}



	public void setChangeprofessnallevel(String changeprofessnallevel) {
		this.changeprofessnallevel = changeprofessnallevel;
	}



	public String getChangecontractcompany() {
		return changecontractcompany;
	}



	public void setChangecontractcompany(String changecontractcompany) {
		this.changecontractcompany = changecontractcompany;
	}



	public String getChangedate() {
		return changedate;
	}



	public void setChangedate(String changedate) {
		this.changedate = changedate;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getCitySelect() {
		return citySelect;
	}



	public void setCitySelect(String citySelect) {
		this.citySelect = citySelect;
	}
	
	
	
	
	
}
