package com.cnpc.pms.personal.entity;


import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Date;

@Entity
@Table(name = "t_express")
public class Express extends DataEntity{
	/** 日期 */
	@Column(name="express_date")
	private Date express_date;
	
	@Transient
	private String express_date_str;

	/** 快递单号 */
	@Column(name="express_no",length=100)
	private String express_no;

	/** 快递公司 */
	@Column(name="express_company",length=100)
	private String express_company;

	/** 寄件人姓名 */
	@Column(name="send_name",length=100)
	private String send_name;

	/** 寄件人电话 */
	@Column(name="send_tel",length=100)
	private String send_tel;

	/** 寄件人地址 */
	@Column(name="send_address",length=200)
	private String send_address;

	/** 收件人姓名 */
	@Column(name="rcv_name",length=20)
	private String rcv_name;

	/** 收件人电话 */
	@Column(name="rcv_tel",length=100)
	private String rcv_tel;

	/** 收件人地址 */
	@Column(name="rcv_address",length=200)
	private String rcv_address;

	/** 托寄物名称 */
	@Column(name="obj_name",length=32)
	private String obj_name;

	/** 备注 */
	@Column(name="remark",length=255)
	private String remark;

	/** 门店编码 */
	@Column(name="store_id")
	private Long store_id;

	/** 图片路径 */
	@Column(name="expressURL",length=255)
	private String expressURL;

	/** 员工编码 */
	@Column(name="employee_id")
	private Long employee_id;

	/** 员工名称 */
	@Column(name="employee_name",length=20)
	private String employee_name;
	
	/** 员工电话*/
	@Column(name="employee_phone",length=32)
	private String employee_phone;

	/** 员工编号*/
	@Column(name="employee_no",length=32)
	private String employee_no;
	
	
	/** 创建人员工编号  */
	@Column(name="create_employee_no",length=32)
	private String create_employee_no;
	
	/** 快递的状态 0 拍照未写 1或空填写完成 **/
	@Column(name="express_status")
	private Integer express_status; 
	
	/**业务类型 0 普通快递  1 信用卡快递 可不写收件人电话	 */
	@Column(name="express_type")
	private Integer express_type;
	
	@Transient
	private int express_count;
	
	
	public String getCreate_employee_no() {
		return create_employee_no;
	}

	public void setCreate_employee_no(String create_employee_no) {
		this.create_employee_no = create_employee_no;
	}

	@Transient
	private String store_name;
	
	@Transient
	private String do_edit;
	

	public Date getExpress_date() {
		return express_date;
	}

	public void setExpress_date(Date express_date) {
		this.express_date = express_date;
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

	public String getSend_name() {
		return send_name;
	}

	public void setSend_name(String send_name) {
		this.send_name = send_name;
	}

	public String getSend_tel() {
		return send_tel;
	}

	public void setSend_tel(String send_tel) {
		this.send_tel = send_tel;
	}

	public String getSend_address() {
		return send_address;
	}

	public void setSend_address(String send_address) {
		this.send_address = send_address;
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

	public String getExpressURL() {
		return expressURL;
	}

	public void setExpressURL(String expressURL) {
		this.expressURL = expressURL;
	}

	public Long getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(Long employee_id) {
		this.employee_id = employee_id;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public String getEmployee_phone() {
		return employee_phone;
	}

	public void setEmployee_phone(String employee_phone) {
		this.employee_phone = employee_phone;
	}

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getExpress_date_str() {
		return express_date_str;
	}

	public void setExpress_date_str(String express_date_str) {
		this.express_date_str = express_date_str;
	}

	public Integer getExpress_status() {
		return express_status;
	}

	public void setExpress_status(Integer express_status) {
		this.express_status = express_status;
	}

	public int getExpress_count() {
		return express_count;
	}

	public void setExpress_count(int express_count) {
		this.express_count = express_count;
	}

	public String getDo_edit() {
		return do_edit;
	}

	public void setDo_edit(String do_edit) {
		this.do_edit = do_edit;
	}

	public Integer getExpress_type() {
		return express_type;
	}

	public void setExpress_type(Integer express_type) {
		this.express_type = express_type;
	}
}
