package com.cnpc.pms.personal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name="t_store_detail")
public class StoreDetail extends DataEntity{
	//房屋产权所属
	@Column(length = 45, name = "ownership")
	private String ownership;
	//门店建筑面积
	@Column(length = 45, name = "store_area")
	private String store_area;
	//租赁单价
	@Column(length = 45, name = "unit_price")
	private String unit_price;
	//中介费
	@Column(length = 45, name = "agency_fee")
	private String agency_fee;
	//免租期开始时间
	@Column( name = "rent_free_start_data")
	private Date rent_free_start_data;
	//免租期结束时间
	@Column( name = "rent_free_end_data")
	private Date rent_free_end_data;
	//付款方式
	@Column( name = "payment_method")
	private String payment_method;
	//门店租赁合同签约时间
	@Column( name = "sign_date")
	private Date sign_date;
	//功能布局图提交日期
	@Column( name = "submit_date")
	private Date submit_date;
	//功能布局图审核通过日期
	@Column( name = "audit_date")
	private Date audit_date;
	//装修进场日期
	@Column( name = "enter_date")
	private Date enter_date;
	//设备下单日期
	@Column( name = "place_date")
	private Date place_date;
	//道具下单日期
	@Column( name = "prop_date")
	private Date prop_date;
	//装修验收合格日期
	@Column( name = "accept_date")
	private Date accept_date;
	//营业执照发放日期
	@Column( name = "business_license_date")
	private Date business_license_date;
	//食品流通证发放日期
	@Column( name = "food_circulation_date")
	private Date food_circulation_date;
	//信息采集开始日
	@Column( name = "gather_start_date")
	private Date gather_start_date;
	//信息采集完成并提交日期
	@Column( name = "gather_end_date")
	private Date gather_end_date;
	//线上中台开通日期
	@Column( name = "line_date")
	private Date line_date;
	//门店id
	@Column( name = "store_id")
	private Long store_id;
	@Column( name = "business_pic",length=255)
	private String business_pic;
	@Column( name = "food_pic",length=255)
	private String food_pic;
	
	
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
	//门店名字
	@Transient
	private String name;
	//开店时间
	@Transient
	private Date open_shop_time;
	//是否微超
	@Transient
	private String superMicro;
	//目前状态
	@Transient
	private String estate;
	//操作
		@Transient
		private String caozuo;
		
	public String getCaozuo() {
			return caozuo;
		}
		public void setCaozuo(String caozuo) {
			this.caozuo = caozuo;
		}
	public Date getOpen_shop_time() {
		return open_shop_time;
	}
	public void setOpen_shop_time(Date open_shop_time) {
		this.open_shop_time = open_shop_time;
	}
	public String getSuperMicro() {
		return superMicro;
	}
	public void setSuperMicro(String superMicro) {
		this.superMicro = superMicro;
	}
	public String getEstate() {
		return estate;
	}
	public void setEstate(String estate) {
		this.estate = estate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwnership() {
		return ownership;
	}
	public void setOwnership(String ownership) {
		this.ownership = ownership;
	}
	public String getStore_area() {
		return store_area;
	}
	public void setStore_area(String store_area) {
		this.store_area = store_area;
	}
	public String getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}
	public String getAgency_fee() {
		return agency_fee;
	}
	public void setAgency_fee(String agency_fee) {
		this.agency_fee = agency_fee;
	}
	public Date getRent_free_start_data() {
		return rent_free_start_data;
	}
	public void setRent_free_start_data(Date rent_free_start_data) {
		this.rent_free_start_data = rent_free_start_data;
	}
	public Date getRent_free_end_data() {
		return rent_free_end_data;
	}
	public void setRent_free_end_data(Date rent_free_end_data) {
		this.rent_free_end_data = rent_free_end_data;
	}
	public String getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
	public Date getSign_date() {
		return sign_date;
	}
	public void setSign_date(Date sign_date) {
		this.sign_date = sign_date;
	}
	public Date getSubmit_date() {
		return submit_date;
	}
	public void setSubmit_date(Date submit_date) {
		this.submit_date = submit_date;
	}
	public Date getAudit_date() {
		return audit_date;
	}
	public void setAudit_date(Date audit_date) {
		this.audit_date = audit_date;
	}
	public Date getEnter_date() {
		return enter_date;
	}
	public void setEnter_date(Date enter_date) {
		this.enter_date = enter_date;
	}
	public Date getPlace_date() {
		return place_date;
	}
	public void setPlace_date(Date place_date) {
		this.place_date = place_date;
	}
	public Date getProp_date() {
		return prop_date;
	}
	public void setProp_date(Date prop_date) {
		this.prop_date = prop_date;
	}
	public Date getAccept_date() {
		return accept_date;
	}
	public void setAccept_date(Date accept_date) {
		this.accept_date = accept_date;
	}
	public Date getBusiness_license_date() {
		return business_license_date;
	}
	public void setBusiness_license_date(Date business_license_date) {
		this.business_license_date = business_license_date;
	}
	public Date getFood_circulation_date() {
		return food_circulation_date;
	}
	public void setFood_circulation_date(Date food_circulation_date) {
		this.food_circulation_date = food_circulation_date;
	}
	public Date getGather_start_date() {
		return gather_start_date;
	}
	public void setGather_start_date(Date gather_start_date) {
		this.gather_start_date = gather_start_date;
	}
	public Date getGather_end_date() {
		return gather_end_date;
	}
	public void setGather_end_date(Date gather_end_date) {
		this.gather_end_date = gather_end_date;
	}
	public Date getLine_date() {
		return line_date;
	}
	public void setLine_date(Date line_date) {
		this.line_date = line_date;
	}
	public Long getStore_id() {
		return store_id;
	}
	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}
	
	
}
