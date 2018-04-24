package com.cnpc.pms.personal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.cnpc.pms.base.entity.IEntity;
import com.cnpc.pms.base.entity.OptLockEntity;

@Entity
@Table(name = "ds_abnormal_order")
public class DsAbnormalOrder implements IEntity{

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length = 50, name = "storeno")
	private String storeno;
	
	@Column(length = 50, name = "ordersn")
	private String ordersn;
	
	@Column(length = 50, name = "cityname")
	private String cityname;
	
	@Column(length = 50, name = "storename")
	private String storename;
	
	@Column(length = 50, name = "deptname")
	private String deptname;
	
	@Column(length = 50, name = "channelname")
	private String channelname;
	
	@Column(name = "tradingprice")
	private Double tradingprice;
	
	@Column(length = 50, name = "abnortype")
	private String abnortype;
	
	@Column(length = 50, name = "datatype")
	private String datatype;
	
	@Column(length = 50, name = "status")
	private String status;

	@Column(name = "createtime")
    private Date createtime;

	@Column(name = "updatetime")
    private Date updatetime;
	
	@Column(name = "year")
	private Integer year;
	@Column(name = "month")
	private Integer month;
	
	@Column(name="signedtime")
	private Date signedtime;
	
	@Column(name="eshopname")
	private  String eshopname;
	
	@Column(name="payableprice")
	private Double payableprice;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStoreno() {
		return storeno;
	}

	public void setStoreno(String storeno) {
		this.storeno = storeno;
	}

	public String getOrdersn() {
		return ordersn;
	}

	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}

	public Double getTradingprice() {
		return tradingprice;
	}

	public void setTradingprice(Double tradingprice) {
		this.tradingprice = tradingprice;
	}

	public String getAbnortype() {
		return abnortype;
	}

	public void setAbnortype(String abnortype) {
		this.abnortype = abnortype;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getStorename() {
		return storename;
	}

	public void setStorename(String storename) {
		this.storename = storename;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Date getSignedtime() {
		return signedtime;
	}

	public void setSignedtime(Date signedtime) {
		this.signedtime = signedtime;
	}

	public String getEshopname() {
		return eshopname;
	}

	public void setEshopname(String eshopname) {
		this.eshopname = eshopname;
	}

	public Double getPayableprice() {
		return payableprice;
	}

	public void setPayableprice(Double payableprice) {
		this.payableprice = payableprice;
	}

	
    
	
	


}
