package com.cnpc.pms.personal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.cnpc.pms.base.entity.IEntity;
import com.cnpc.pms.base.entity.OptLockEntity;

@Entity
@Table(name = "t_topdata_human")
public class DsTopData implements IEntity{

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length = 50, name = "cityname")
	private String cityname;
	
	@Column(length = 50, name = "employeeno")
	private String employeeno;
	
	@Column(length = 50, name = "username")
	private String username;
	
	@Column(name="humanstatus")
	private Long humanstatus;
	
	@Column(name="storeid")
	private Long storeid;
	
	@Column(length = 50, name = "storeno")
	private String storeno;
	
	@Column(length = 50, name = "storename")
	private String storename;
	
	@Column(length = 50, name = "zw")
	private String zw;
	
	@Column(name = "persontype")
	private Integer persontype;
	
	@Column(length = 65, name = "authorizedtype")
	private String authorizedtype;
	
	@Column(length = 65, name = "professnallevel")
	private String professnallevel;
	
	@Column(length = 255, name = "remark")
	private String remark;
	
	@Column(length = 65,name="leavedate")
	private String leavedate;
	
	@Column(length = 65, name = "topostdate")
	private String topostdate;
	
	@Column(length = 45,name="score")
	private String score;
	
	@Column(length = 20,name="yearmonth")
	private String yearmonth;
	
	@Column(name = "createtime")
	private Date createtime;

	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmployeeno() {
		return employeeno;
	}

	public void setEmployeeno(String employeeno) {
		this.employeeno = employeeno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getHumanstatus() {
		return humanstatus;
	}

	public void setHumanstatus(Long humanstatus) {
		this.humanstatus = humanstatus;
	}

	public Long getStoreid() {
		return storeid;
	}

	public void setStoreid(Long storeid) {
		this.storeid = storeid;
	}

	public String getStorename() {
		return storename;
	}

	public void setStorename(String storename) {
		this.storename = storename;
	}

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	public Integer getPersontype() {
		return persontype;
	}

	public void setPersontype(Integer persontype) {
		this.persontype = persontype;
	}

	public String getAuthorizedtype() {
		return authorizedtype;
	}

	public void setAuthorizedtype(String authorizedtype) {
		this.authorizedtype = authorizedtype;
	}

	public String getProfessnallevel() {
		return professnallevel;
	}

	public void setProfessnallevel(String professnallevel) {
		this.professnallevel = professnallevel;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLeavedate() {
		return leavedate;
	}

	public void setLeavedate(String leavedate) {
		this.leavedate = leavedate;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getYearmonth() {
		return yearmonth;
	}

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getStoreno() {
		return storeno;
	}

	public void setStoreno(String storeno) {
		this.storeno = storeno;
	}

	public String getTopostdate() {
		return topostdate;
	}

	public void setTopostdate(String topostdate) {
		this.topostdate = topostdate;
	}
	


}
