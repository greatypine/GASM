package com.cnpc.pms.personal.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.activiti.engine.task.Comment;

import com.cnpc.pms.base.entity.DataEntity;

@Entity
@Table(name = "t_human_vacation")
public class HumanVacation extends DataEntity{
	private static final long serialVersionUID = 1L;

	@Column(name = "store_id")
	private Long store_id;//标题
	
	@Column(name = "store_name",length=64)
	private String store_name;//门店名称
	
	@Column(name="employee_no",length=64)
	private String employee_no;//员工编号
	
	@Column(name="employee_name",length=64)
	private String employee_name;//员工姓名
	
	@Column(name="work_date",length=64)
	private String work_date;//参加工作时间 
	
	@Column(name="topostdate",length=64)
	private String topostdate;//入职日期
	
	@Column(name="vacation_reason",length=255)
	private String vacation_reason;//请假原因 
	
	@Column(name="start_date",length=64)
	private String start_date;//开始日期
	
	@Column(name="end_date",length=64)
	private String end_date;//结束日期
	
	//①事假②病假③婚假④产假⑤年休假⑥丧假⑦补休⑧其他
	@Column(name="vacation_type",length=64)
	private String vacation_type;//类别
	
	@Column(name="work_relay",length=255)
	private String work_relay;//工作交接情况 

	
	//流程ID
	@Column(name="processInstanceId",length=64)
	private String processInstanceId;
	
	
	@Column(name="process_status")
	private Integer process_status;//流程状态  1审批中   2通过  3驳回 
	
	@Transient
	private String re_content;
	
	@Transient
	private List<Comment> processlog;
	
	
	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public String getWork_date() {
		return work_date;
	}

	public void setWork_date(String work_date) {
		this.work_date = work_date;
	}

	public String getTopostdate() {
		return topostdate;
	}

	public void setTopostdate(String topostdate) {
		this.topostdate = topostdate;
	}

	public String getVacation_reason() {
		return vacation_reason;
	}

	public void setVacation_reason(String vacation_reason) {
		this.vacation_reason = vacation_reason;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getVacation_type() {
		return vacation_type;
	}

	public void setVacation_type(String vacation_type) {
		this.vacation_type = vacation_type;
	}

	public String getWork_relay() {
		return work_relay;
	}

	public void setWork_relay(String work_relay) {
		this.work_relay = work_relay;
	}

	public Integer getProcess_status() {
		return process_status;
	}

	public void setProcess_status(Integer process_status) {
		this.process_status = process_status;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public List<Comment> getProcesslog() {
		return processlog;
	}

	public void setProcesslog(List<Comment> processlog) {
		this.processlog = processlog;
	}

	public String getRe_content() {
		return re_content;
	}

	public void setRe_content(String re_content) {
		this.re_content = re_content;
	}

	
	
}
