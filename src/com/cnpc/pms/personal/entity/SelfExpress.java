package com.cnpc.pms.personal.entity;


import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Date;

@Entity
@Table(name = "t_self_express")
public class SelfExpress extends DataEntity{
	/** 日期 */
	@Column(name="express_date")
	private Date express_date;

	@Transient
	private String take_express_date_str;
	@Transient
	private String express_date_str;
	
	/** 取件日期 */
	@Column(name="take_express_date")
	private Date take_express_date;
	
	/** 快递单号 */
	@Column(name="express_no",length=20)
	private String express_no;

	/** 快递公司 */
	@Column(name="express_company",length=100)
	private String express_company;
	
	/** 收件人姓名 */
	@Column(name="rcv_name",length=255)
	private String rcv_name;

	/** 收件人电话 */
	@Column(name="rcv_tel",length=20)
	private String rcv_tel;

	/** 收件人地址 */
	@Column(name="rcv_address",length=200)
	private String rcv_address;

	/** 托寄物名称 */
	@Column(name="obj_name",length=255)
	private String obj_name;

	/** 备注 */
	@Column(name="remark",length=255)
	private String remark;
	
	/** 门店编码 */
	@Column(name="store_id")
	private Long store_id;
	
	@Transient
	private String store_name;
	
	
	@Transient
	private int self_express_count;
	
	public Date getExpress_date() {
		return express_date;
	}

	public void setExpress_date(Date express_date) {
		this.express_date = express_date;
	}

	public Date getTake_express_date() {
		return take_express_date;
	}

	public void setTake_express_date(Date take_express_date) {
		this.take_express_date = take_express_date;
	}

	public String getExpress_no() {
		return express_no;
	}

	public void setExpress_no(String express_no) {
		this.express_no = express_no;
	}

	public String getExpress_company() {
		return express_company;
	}

	public void setExpress_company(String express_company) {
		this.express_company = express_company;
	}

	public String getRcv_name() {
		return rcv_name;
	}

	public void setRcv_name(String rcv_name) {
		this.rcv_name = rcv_name;
	}

	public String getRcv_tel() {
		return rcv_tel;
	}

	public void setRcv_tel(String rcv_tel) {
		this.rcv_tel = rcv_tel;
	}

	public String getRcv_address() {
		return rcv_address;
	}

	public void setRcv_address(String rcv_address) {
		this.rcv_address = rcv_address;
	}

	public String getObj_name() {
		return obj_name;
	}

	public void setObj_name(String obj_name) {
		this.obj_name = obj_name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public String getTake_express_date_str() {
		return take_express_date_str;
	}

	public void setTake_express_date_str(String take_express_date_str) {
		this.take_express_date_str = take_express_date_str;
	}

	public String getExpress_date_str() {
		return express_date_str;
	}

	public void setExpress_date_str(String express_date_str) {
		this.express_date_str = express_date_str;
	}

	public int getSelf_express_count() {
		return self_express_count;
	}

	public void setSelf_express_count(int self_express_count) {
		this.self_express_count = self_express_count;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	



	
}
