package com.cnpc.pms.personal.entity;


import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Date;

@Entity
@Table(name = "t_xx_express")
public class XxExpress extends DataEntity{
	
	/** 到店日期 */
	@Column(name="express_date")
	private Date express_date;

	@Transient
	private String take_express_date_str;
	@Transient
	private String express_date_str;
	
	/** 取货日期 */
	@Column(name="take_express_date")
	private Date take_express_date;
	
	/** 托寄物名称 */
	@Column(name="obj_name",length=255)
	private String obj_name;

	/** 收件人姓名 */
	@Column(name="rcv_name",length=255)
	private String rcv_name;

	/** 收件人电话 */
	@Column(name="rcv_tel",length=100)
	private String rcv_tel;

	/** 备注 */
	@Column(name="remark",length=255)
	private String remark;
	
	/** 门店编码 */
	@Column(name="store_id")
	private Long store_id;
	
	@Transient
	private String store_name;
	

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

	public String getObj_name() {
		return obj_name;
	}

	public void setObj_name(String obj_name) {
		this.obj_name = obj_name;
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

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	


	



	
}
