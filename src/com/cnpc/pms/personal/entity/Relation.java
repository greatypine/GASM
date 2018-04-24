package com.cnpc.pms.personal.entity;


import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "t_relation")
public class Relation extends DataEntity implements Comparable {

	@Column(name = "customer_id")
	private Long customer_id;

	@Transient
	private Customer customer;

	@Column(name = "employee_no", length = 45)
	private String employee_no;

	@Column(name = "employee_name", length = 45)
	private String employee_name;

	@Column(name = "relation_date")
	private Date relation_date;

	@Column(name = "relation_type", length = 40)
	private String relation_type;

	
	@Column(name = "product_services")
	private String product_services;

	@Column(name = "cultural_activity")
	private String cultural_activity;

	@Column(name = "common_activity")
	private String common_activity;
	
	@Column(name = "relation_reason", length = 255)
	private String relation_reason;
	
	@Column(name = "relation_rcv", length = 255)
	private String relation_rcv;
	
	@Column(name = "customer_type")
	private Long customer_type;
	
	//拜访次数
	@Transient
	private String totalcount;
	//最新拜访日期
	@Transient
	private String lastdate;
	
	
	/**
	 * 下级功能节点.
	 */
	@OneToMany(targetEntity = RelationContent.class, cascade = CascadeType.ALL, mappedBy = "relation_id")
	private Set<RelationContent> childs;

	public Long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
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

	public Date getRelation_date() {
		return relation_date;
	}

	public void setRelation_date(Date relation_date) {
		this.relation_date = relation_date;
	}

	public String getRelation_type() {
		return relation_type;
	}

	public void setRelation_type(String relation_type) {
		this.relation_type = relation_type;
	}

	public Set<RelationContent> getChilds() {
		return childs;
	}

	public void setChilds(Set<RelationContent> childs) {
		this.childs = childs;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public int compareTo(Object o) {
		Long time1 = this.getRelation_date().getTime();
		Long time2 = ((Relation) o).getRelation_date().getTime();
		if (time1 > time2) {
			return 1;
		} else if (time1 == time2) {
			return 0;
		} else {
			return -1;
		}
	}

	public String getProduct_services() {
		return product_services;
	}

	public void setProduct_services(String product_services) {
		this.product_services = product_services;
	}

	public String getCultural_activity() {
		return cultural_activity;
	}

	public void setCultural_activity(String cultural_activity) {
		this.cultural_activity = cultural_activity;
	}

	public String getCommon_activity() {
		return common_activity;
	}

	public void setCommon_activity(String common_activity) {
		this.common_activity = common_activity;
	}

	public String getRelation_reason() {
		return relation_reason;
	}

	public void setRelation_reason(String relation_reason) {
		this.relation_reason = relation_reason;
	}

	public String getRelation_rcv() {
		return relation_rcv;
	}

	public void setRelation_rcv(String relation_rcv) {
		this.relation_rcv = relation_rcv;
	}

	public Long getCustomer_type() {
		return customer_type;
	}

	public void setCustomer_type(Long customer_type) {
		this.customer_type = customer_type;
	}

	public String getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(String totalcount) {
		this.totalcount = totalcount;
	}

	public String getLastdate() {
		return lastdate;
	}

	public void setLastdate(String lastdate) {
		this.lastdate = lastdate;
	}
}