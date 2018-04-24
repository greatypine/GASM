package com.cnpc.pms.personal.dto;

/**
 * 
 * @Function：用户档案请求数据对象
 * @author：chenchuang
 * @date:2018年3月8日 上午11:35:18
 *
 * @version V1.0
 */
public class UserProfileDto {

	private String customer_phone;
	private String customer_name;
	private String regist_time_begin;
	private String regist_time_end;
	private String first_order_time_begin;
	private String first_order_time_end;
	private String last_order_time_begin;
	private String last_order_time_end;
	private String user_label;
	private String user_model;
	private String trading_price_min;
	private String trading_price_max;
	private String order_count_min;
	private String order_count_max;
	private String slient_time_min;
	private String slient_time_max;
	private String hidden_flag;
	
	
	public String getCustomer_phone() {
		return customer_phone;
	}
	public void setCustomer_phone(String customer_phone) {
		this.customer_phone = customer_phone;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getRegist_time_begin() {
		return regist_time_begin;
	}
	public void setRegist_time_begin(String regist_time_begin) {
		this.regist_time_begin = regist_time_begin;
	}
	public String getRegist_time_end() {
		return regist_time_end;
	}
	public void setRegist_time_end(String regist_time_end) {
		this.regist_time_end = regist_time_end;
	}
	public String getFirst_order_time_begin() {
		return first_order_time_begin;
	}
	public void setFirst_order_time_begin(String first_order_time_begin) {
		this.first_order_time_begin = first_order_time_begin;
	}
	public String getFirst_order_time_end() {
		return first_order_time_end;
	}
	public void setFirst_order_time_end(String first_order_time_end) {
		this.first_order_time_end = first_order_time_end;
	}
	public String getLast_order_time_begin() {
		return last_order_time_begin;
	}
	public void setLast_order_time_begin(String last_order_time_begin) {
		this.last_order_time_begin = last_order_time_begin;
	}
	public String getLast_order_time_end() {
		return last_order_time_end;
	}
	public void setLast_order_time_end(String last_order_time_end) {
		this.last_order_time_end = last_order_time_end;
	}
	public String getUser_label() {
		return user_label;
	}
	public void setUser_label(String user_label) {
		this.user_label = user_label;
	}
	public String getUser_model() {
		return user_model;
	}
	public void setUser_model(String user_model) {
		this.user_model = user_model;
	}
	public String getTrading_price_min() {
		return trading_price_min;
	}
	public void setTrading_price_min(String trading_price_min) {
		this.trading_price_min = trading_price_min;
	}
	public String getTrading_price_max() {
		return trading_price_max;
	}
	public void setTrading_price_max(String trading_price_max) {
		this.trading_price_max = trading_price_max;
	}
	public String getOrder_count_min() {
		return order_count_min;
	}
	public void setOrder_count_min(String order_count_min) {
		this.order_count_min = order_count_min;
	}
	public String getOrder_count_max() {
		return order_count_max;
	}
	public void setOrder_count_max(String order_count_max) {
		this.order_count_max = order_count_max;
	}
	public String getSlient_time_min() {
		return slient_time_min;
	}
	public void setSlient_time_min(String slient_time_min) {
		this.slient_time_min = slient_time_min;
	}
	public String getSlient_time_max() {
		return slient_time_max;
	}
	public void setSlient_time_max(String slient_time_max) {
		this.slient_time_max = slient_time_max;
	}
	public String getHidden_flag() {
		return hidden_flag;
	}
	public void setHidden_flag(String hidden_flag) {
		this.hidden_flag = hidden_flag;
	}
	
}
