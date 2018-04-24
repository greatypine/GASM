package com.cnpc.pms.dynamic.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Function：清洗出的订单数据
 * @author：chenchuang
 * @date:2018年1月9日 下午2:37:28
 *
 * @version V1.0
 */
public class MassOrderDto {

	private String id;
	private String order_sn;
	private BigDecimal trading_price;
	private BigDecimal payable_price;
	private Integer status;
	private Date sign_time;
	private String eshop_id;
	private String eshop_name;
	private String store_no;
	private String city_name;
	private String department_name;
	private String channel_name;
	private String customer_isnew;
	private String area_name;
	private String area_code;
	private String village_name;
	private String beginDate;
	private String endDate;
	private String customer_name;
	private String customer_phone;
	private String employee_a_phone;
	private String employee_a_no;
	private String employee_a_phone_a;
	private String employee_a_no_a;
	private String employee_no;
	private String employee_mobile;
	private String department_names;
	private String order_labels;
	private String addr_customer_name;
	private String addr_customer_phone;
	
	public enum TimeFlag{
		CUR_DAY("0", "当天"),
		LATEST_MONTH("1", "最近两个月"),
		HISTORY_MONTH("2", "历史月"),
    	;
    	
    	public String code;
    	public String desc;
    	
    	TimeFlag(String code, String desc){
    		this.code = code;
    		this.desc = desc;
    	}
    }
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public BigDecimal getTrading_price() {
		return trading_price;
	}
	public void setTrading_price(BigDecimal trading_price) {
		this.trading_price = trading_price;
	}
	public BigDecimal getPayable_price() {
		return payable_price;
	}
	public void setPayable_price(BigDecimal payable_price) {
		this.payable_price = payable_price;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getSign_time() {
		return sign_time;
	}
	public void setSign_time(Date sign_time) {
		this.sign_time = sign_time;
	}
	public String getEshop_id() {
		return eshop_id;
	}
	public void setEshop_id(String eshop_id) {
		this.eshop_id = eshop_id;
	}
	public String getEshop_name() {
		return eshop_name;
	}
	public void setEshop_name(String eshop_name) {
		this.eshop_name = eshop_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public String getStore_no() {
		return store_no;
	}
	public void setStore_no(String store_no) {
		this.store_no = store_no;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getDepartment_name() {
		return department_name;
	}
	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}
	public String getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}
	public String getCustomer_isnew() {
		return customer_isnew;
	}
	public void setCustomer_isnew(String customer_isnew) {
		this.customer_isnew = customer_isnew;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	public String getVillage_name() {
		return village_name;
	}
	public void setVillage_name(String village_name) {
		this.village_name = village_name;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getCustomer_phone() {
		return customer_phone;
	}
	public void setCustomer_phone(String customer_phone) {
		this.customer_phone = customer_phone;
	}
	public String getEmployee_a_phone() {
		return employee_a_phone;
	}
	public void setEmployee_a_phone(String employee_a_phone) {
		this.employee_a_phone = employee_a_phone;
	}
	public String getEmployee_a_no() {
		return employee_a_no;
	}
	public void setEmployee_a_no(String employee_a_no) {
		this.employee_a_no = employee_a_no;
	}
	public String getEmployee_a_phone_a() {
		return employee_a_phone_a;
	}
	public void setEmployee_a_phone_a(String employee_a_phone_a) {
		this.employee_a_phone_a = employee_a_phone_a;
	}
	public String getEmployee_a_no_a() {
		return employee_a_no_a;
	}
	public void setEmployee_a_no_a(String employee_a_no_a) {
		this.employee_a_no_a = employee_a_no_a;
	}
	public String getEmployee_no() {
		return employee_no;
	}
	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}
	public String getEmployee_mobile() {
		return employee_mobile;
	}
	public void setEmployee_mobile(String employee_mobile) {
		this.employee_mobile = employee_mobile;
	}
	public String getDepartment_names() {
		return department_names;
	}
	public void setDepartment_names(String department_names) {
		this.department_names = department_names;
	}
	public String getOrder_labels() {
		return order_labels;
	}
	public void setOrder_labels(String order_labels) {
		this.order_labels = order_labels;
	}
	public String getAddr_customer_name() {
		return addr_customer_name;
	}
	public void setAddr_customer_name(String addr_customer_name) {
		this.addr_customer_name = addr_customer_name;
	}
	public String getAddr_customer_phone() {
		return addr_customer_phone;
	}
	public void setAddr_customer_phone(String addr_customer_phone) {
		this.addr_customer_phone = addr_customer_phone;
	}
	
}
