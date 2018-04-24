package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_humanresources_change")
public class HumanresourcesChange extends DataEntity{
	
	
	/***************************完善*******************************/
	/**
	 * 所属机构
	 */
	@Column(length = 65, name = "orgname")
	private String orgname;
	
	
	/**
	 * 所属机构变更后
	 */
	@Column(length = 65, name = "changeorgname")
	private String changeorgname;
	
	/**
	 * 一级部门
	 */
	@Column(length = 65, name = "deptlevel1")
	private String deptlevel1;
	
	
	/**
	 * 一级部门变更后
	 */
	@Column(length = 65, name = "changedeptlevel1")
	private String changedeptlevel1;
	
	/**
	 * 二级部门
	 */
	@Column(length = 65, name = "deptlevel2")
	private String deptlevel2;
	
	/**
	 * 二级部门变更后
	 */
	@Column(length = 65, name = "changedeptlevel2")
	private String changedeptlevel2;
	
	
	/**
	 * 三级部门
	 */
	@Column(length = 65, name = "deptlevel3")
	private String deptlevel3;
	
	
	/**
	 * 三级部门变更后
	 */
	@Column(length = 65, name = "changedeptlevel3")
	private String changedeptlevel3;
	
	/**
	 * 汇报上级
	 */
	@Column(length = 65, name = "reporthigher")
	private String reporthigher;
	
	
	/**
	 * 汇报上级变更后
	 */
	@Column(length = 65, name = "changereporthigher")
	private String changereporthigher;

	/**
	 * 职级
	 */
	@Column(length = 65, name = "professnallevel")
	private String professnallevel;
	
	
	/**
	 * 职级变更后
	 */
	@Column(length = 65, name = "changeprofessnallevel")
	private String changeprofessnallevel;
	
	
	/**
	 * 职位
	 */
	@Column(length = 65, name = "zw")
	private String zw;
	
	/**
	 * 职位变更后
	 */
	@Column(length = 65, name = "changezw")
	private String changezw;
	
	
	/**
	 * 事业群 
	 */
	@Column(length = 65, name = "career_group")
	private String career_group;
	
	/**
	 * 变更后事业群 
	 */
	@Column(length = 65, name = "changecareer_group")
	private String changecareer_group;
	
	
	
	/** 
	 * 门店编码 
	 */
	@Column(name="store_id")
	private Long store_id;
	
	
	/** 
	 * 门店名称
	 */
	@Column(length = 65,name="storename")
	private String storename;
	
	
	
	
	/** 
	 * 门店名称
	 */
	@Column(length = 65,name="changestorename")
	private String changestorename;
	
	public String getChangestorename() {
		return changestorename;
	}

	public void setChangestorename(String changestorename) {
		this.changestorename = changestorename;
	}

	public String getStorename() {
		return storename;
	}

	public void setStorename(String storename) {
		this.storename = storename;
	}

	/** 
	 * 门店编码 变更后
	 */
	@Column(name="changestore_id")
	private Long changestore_id;
	
	/**
	 * 异动日期
	 */
	@Column(length = 65, name = "changedate")
	private String changedate;
	
	/**
	 * 父ID
	 */
	@Column(length = 65,name = "employee_no")
	private String employee_no;

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getDeptlevel1() {
		return deptlevel1;
	}

	public void setDeptlevel1(String deptlevel1) {
		this.deptlevel1 = deptlevel1;
	}

	public String getDeptlevel2() {
		return deptlevel2;
	}

	public void setDeptlevel2(String deptlevel2) {
		this.deptlevel2 = deptlevel2;
	}

	public String getDeptlevel3() {
		return deptlevel3;
	}

	public void setDeptlevel3(String deptlevel3) {
		this.deptlevel3 = deptlevel3;
	}

	public String getReporthigher() {
		return reporthigher;
	}

	public void setReporthigher(String reporthigher) {
		this.reporthigher = reporthigher;
	}

	public String getProfessnallevel() {
		return professnallevel;
	}

	public void setProfessnallevel(String professnallevel) {
		this.professnallevel = professnallevel;
	}

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public String getChangedate() {
		return changedate;
	}

	public void setChangedate(String changedate) {
		this.changedate = changedate;
	}


	public String getChangeorgname() {
		return changeorgname;
	}

	public void setChangeorgname(String changeorgname) {
		this.changeorgname = changeorgname;
	}

	public String getChangedeptlevel1() {
		return changedeptlevel1;
	}

	public void setChangedeptlevel1(String changedeptlevel1) {
		this.changedeptlevel1 = changedeptlevel1;
	}

	public String getChangedeptlevel2() {
		return changedeptlevel2;
	}

	public void setChangedeptlevel2(String changedeptlevel2) {
		this.changedeptlevel2 = changedeptlevel2;
	}

	public String getChangedeptlevel3() {
		return changedeptlevel3;
	}

	public void setChangedeptlevel3(String changedeptlevel3) {
		this.changedeptlevel3 = changedeptlevel3;
	}

	public String getChangereporthigher() {
		return changereporthigher;
	}

	public void setChangereporthigher(String changereporthigher) {
		this.changereporthigher = changereporthigher;
	}

	public String getChangeprofessnallevel() {
		return changeprofessnallevel;
	}

	public void setChangeprofessnallevel(String changeprofessnallevel) {
		this.changeprofessnallevel = changeprofessnallevel;
	}

	public String getChangezw() {
		return changezw;
	}

	public void setChangezw(String changezw) {
		this.changezw = changezw;
	}

	public Long getChangestore_id() {
		return changestore_id;
	}

	public void setChangestore_id(Long changestore_id) {
		this.changestore_id = changestore_id;
	}

	public String getCareer_group() {
		return career_group;
	}

	public void setCareer_group(String career_group) {
		this.career_group = career_group;
	}

	public String getChangecareer_group() {
		return changecareer_group;
	}

	public void setChangecareer_group(String changecareer_group) {
		this.changecareer_group = changecareer_group;
	}

	
}
