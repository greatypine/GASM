package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.PMSEntity;

@Entity
@Table(name = "t_human_recontent")
public class HumanReContent extends PMSEntity{
	private static final long serialVersionUID = 1L;

	@Column(name="vacationid")
	private Long vacationid;
	
	@Column(name="employee_name",length=64)
	private String employee_name;
	
	@Column(name="employee_no",length=64)
	private String employee_no;
	
	@Column(name="processInstanceId",length=64)
	private String processInstanceId;
	
	@Column(name="re_content",length=255)
	private String re_content;

	
	
	
	public Long getVacationid() {
		return vacationid;
	}

	public void setVacationid(Long vacationid) {
		this.vacationid = vacationid;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getRe_content() {
		return re_content;
	}

	public void setRe_content(String re_content) {
		this.re_content = re_content;
	}

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	
	
}
