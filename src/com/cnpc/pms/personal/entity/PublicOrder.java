package com.cnpc.pms.personal.entity;

import java.util.Date;

import com.cnpc.pms.base.entity.IEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "df_order_pubseas_monthly")
public class PublicOrder implements IEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 45, name = "df_order_id")
	private String order_id;
	
	@Column(length = 45, name = "df_store_name")
	private String storename;
	
	@Column(length = 45, name = "df_order_sn")
	private String ordersn;
	
	@Column(name = "df_signed_time")
    private Date df_signed_time;

	
	//用于修改
	@Column(length = 45, name = "employee_no")
	private String employeeno;
	@Column(length = 45, name = "employee_name")
	private String employeename;
	@Column(length = 255, name = "area_nos")
	private String area_nos;
	@Column(length = 255, name = "area_names")
	private String area_names;
	
    @Column(name = "update_time")
    private Date updatetime;
    @Column(length = 36, name = "update_user")
    private String updateuser;
    @Column(name = "update_user_id")
    private Long updateuserid;
	
	//用于参数
	@Transient
	private String signedtime;
	@Transient
	private String storeno;
	@Transient
	private String autostatus;
	@Transient
	private String storenos;
	@Transient
	private String cityname;
	@Transient
	private String ordertype;
	
	
	
	@Transient
	private String df_order_placename; //区块 
	@Transient
	private String df_tiny_village_name; //小区 
	@Transient
	private String df_area_name; //片区 
	@Transient
	private String df_employee_a_name; //国安侠 
	@Transient
	private String df_dep_name; //事业群
	@Transient
	private String df_channel_name; //频道 
	@Transient
	private String searchordertype; //订单类型 
	/*@Transient
	private String df_signed_time;//签收时间  
	*/
	
	
	
	@Transient
	private String signedtimeymd;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getStorename() {
		return storename;
	}

	public void setStorename(String storename) {
		this.storename = storename;
	}

	public String getEmployeeno() {
		return employeeno;
	}

	public void setEmployeeno(String employeeno) {
		this.employeeno = employeeno;
	}

	public String getEmployeename() {
		return employeename;
	}

	public void setEmployeename(String employeename) {
		this.employeename = employeename;
	}

	public String getOrdersn() {
		return ordersn;
	}

	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}

	public String getArea_nos() {
		return area_nos;
	}

	public void setArea_nos(String area_nos) {
		this.area_nos = area_nos;
	}

	public String getArea_names() {
		return area_names;
	}

	public void setArea_names(String area_names) {
		this.area_names = area_names;
	}


	public String getStoreno() {
		return storeno;
	}

	public void setStoreno(String storeno) {
		this.storeno = storeno;
	}

	public String getSignedtime() {
		return signedtime;
	}

	public void setSignedtime(String signedtime) {
		this.signedtime = signedtime;
	}

	public String getAutostatus() {
		return autostatus;
	}

	public void setAutostatus(String autostatus) {
		this.autostatus = autostatus;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getUpdateuser() {
		return updateuser;
	}

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}

	public Long getUpdateuserid() {
		return updateuserid;
	}

	public void setUpdateuserid(Long updateuserid) {
		this.updateuserid = updateuserid;
	}

	public String getStorenos() {
		return storenos;
	}

	public void setStorenos(String storenos) {
		this.storenos = storenos;
	}

	public String getSignedtimeymd() {
		return signedtimeymd;
	}

	public void setSignedtimeymd(String signedtimeymd) {
		this.signedtimeymd = signedtimeymd;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public Date getDf_signed_time() {
		return df_signed_time;
	}

	public void setDf_signed_time(Date df_signed_time) {
		this.df_signed_time = df_signed_time;
	}

	public String getDf_order_placename() {
		return df_order_placename;
	}

	public void setDf_order_placename(String df_order_placename) {
		this.df_order_placename = df_order_placename;
	}

	public String getDf_tiny_village_name() {
		return df_tiny_village_name;
	}

	public void setDf_tiny_village_name(String df_tiny_village_name) {
		this.df_tiny_village_name = df_tiny_village_name;
	}

	public String getDf_area_name() {
		return df_area_name;
	}

	public void setDf_area_name(String df_area_name) {
		this.df_area_name = df_area_name;
	}

	public String getDf_employee_a_name() {
		return df_employee_a_name;
	}

	public void setDf_employee_a_name(String df_employee_a_name) {
		this.df_employee_a_name = df_employee_a_name;
	}

	public String getDf_dep_name() {
		return df_dep_name;
	}

	public void setDf_dep_name(String df_dep_name) {
		this.df_dep_name = df_dep_name;
	}

	public String getDf_channel_name() {
		return df_channel_name;
	}

	public void setDf_channel_name(String df_channel_name) {
		this.df_channel_name = df_channel_name;
	}

	public String getSearchordertype() {
		return searchordertype;
	}

	public void setSearchordertype(String searchordertype) {
		this.searchordertype = searchordertype;
	}
	
	
	
	
}
