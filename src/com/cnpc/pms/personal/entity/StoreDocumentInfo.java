package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.pms.base.entity.DataEntity;

@Table
@Entity(name = "t_store_document_info")
public class StoreDocumentInfo extends DataEntity {
	// 门店id
	@Column(name = "store_id")
	private Long store_id;
	// 功能方案提交日期
	@Column(name = "submit_date")
	private String submit_date;
	// 功能方案通过日期
	@Column(name = "audit_date")
	private String audit_date;
	// 装修进场日期
	@Column(name = "enter_date")
	private String enter_date;
	// 装修竣工日期
	@Column(name = "enter_end_date")
	private String enter_end_date;
	// 证照内容
	@Column(name = "card_content", length = 200)
	private String card_content;
	// 审核状态
	@Column(name = "audit_status")
	private Integer audit_status;
	// 流程id
	@Column(name = "work_id", length = 255)
	private String work_id;

	public String getWork_id() {
		return work_id;
	}

	public void setWork_id(String work_id) {
		this.work_id = work_id;
	}

	@Transient
	private String name;// 门店名称
	@Transient
	private String audit_pic;
	@Transient
	private String plane_pic;
	@Transient
	private String record_pic;
	@Transient
	private String business_pic;
	@Transient
	private String food_pic;
	@Transient
	private String tobacco_pic;
	@Transient
	private String book_pic;
	@Transient
	private String xx_pic;
	@Transient
	private String other_pic;
	@Transient
	private String caozuo;

	public String getCaozuo() {
		return caozuo;
	}

	public void setCaozuo(String caozuo) {
		this.caozuo = caozuo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAudit_pic() {
		return audit_pic;
	}

	public void setAudit_pic(String audit_pic) {
		this.audit_pic = audit_pic;
	}

	public String getPlane_pic() {
		return plane_pic;
	}

	public void setPlane_pic(String plane_pic) {
		this.plane_pic = plane_pic;
	}

	public String getRecord_pic() {
		return record_pic;
	}

	public void setRecord_pic(String record_pic) {
		this.record_pic = record_pic;
	}

	public String getBusiness_pic() {
		return business_pic;
	}

	public void setBusiness_pic(String business_pic) {
		this.business_pic = business_pic;
	}

	public String getFood_pic() {
		return food_pic;
	}

	public void setFood_pic(String food_pic) {
		this.food_pic = food_pic;
	}

	public String getTobacco_pic() {
		return tobacco_pic;
	}

	public void setTobacco_pic(String tobacco_pic) {
		this.tobacco_pic = tobacco_pic;
	}

	public String getBook_pic() {
		return book_pic;
	}

	public void setBook_pic(String book_pic) {
		this.book_pic = book_pic;
	}

	public String getXx_pic() {
		return xx_pic;
	}

	public void setXx_pic(String xx_pic) {
		this.xx_pic = xx_pic;
	}

	public String getOther_pic() {
		return other_pic;
	}

	public void setOther_pic(String other_pic) {
		this.other_pic = other_pic;
	}

	public Integer getAudit_status() {
		return audit_status;
	}

	public void setAudit_status(Integer audit_status) {
		this.audit_status = audit_status;
	}

	public String getCard_content() {
		return card_content;
	}

	public void setCard_content(String card_content) {
		this.card_content = card_content;
	}

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public String getSubmit_date() {
		return submit_date;
	}

	public void setSubmit_date(String submit_date) {
		this.submit_date = submit_date;
	}

	public String getAudit_date() {
		return audit_date;
	}

	public void setAudit_date(String audit_date) {
		this.audit_date = audit_date;
	}

	public String getEnter_date() {
		return enter_date;
	}

	public void setEnter_date(String enter_date) {
		this.enter_date = enter_date;
	}

	public String getEnter_end_date() {
		return enter_end_date;
	}

	public void setEnter_end_date(String enter_end_date) {
		this.enter_end_date = enter_end_date;
	}

}
