package com.cnpc.pms.personal.entity;


import com.cnpc.pms.base.entity.DataEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_store_standard")
public class StoreStandard extends DataEntity{
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * 别名 供新增需求的时候选
	 */
	@Column(length = 64, name = "standardname")
	private String standardname;
	
	
	@Transient
	private String editbutton;
	
	/**
	 * 租金
	 */
	@Column(length = 1000, name = "rent")
	private String rent;
	
	/**
	 * 产权
	 */
	@Column(length = 100, name = "property")
	private String property;
	
	/**
	 * 面积
	 */
	@Column(length = 100, name = "area")
	private String area;
	
	/**
	 * 户型
	 */
	@Column(length = 200, name = "type")
	private String type;
	
	/**
	 * 楼层
	 */
	@Column(length = 50, name = "floor")
	private String floor;
	
	/**
	 * 电
	 */
	@Column(length = 200, name = "electric")
	private String electric;
	
	/**
	 * 室外机
	 */
	@Column(length = 200, name = "sidestation")
	private String sidestation;
	
	/**
	 * 上下水
	 */
	@Column(length = 200, name = "water")
	private String water;
	
	/**
	 * 排污
	 */
	@Column(length = 120, name = "blowdown")
	private String blowdown;
	
	/**
	 * 消防
	 */
	@Column(length = 45, name = "firecontrol")
	private String firecontrol;
	
	/**
	 * 图纸
	 */
	@Column(length = 100, name = "designpaper")
	private String designpaper;
	
	/**
	 * 门头灯箱
	 */
	@Column(length = 100, name = "lamphouse")
	private String lamphouse;

	
	/**
	 * 层高
	 */
	@Column(length = 50, name = "floorheight")
	private String floorheight;

	/**
	 * 租期
	 */
	@Column(length = 50, name = "leasetime")
	private String leasetime;
	
	/**
	 * 位置
	 */
	@Column(length = 50, name = "address")
	private String address;
	
	/**
	 * 网络
	 */
	@Column(length = 50, name = "network")
	private String network;

	
	/**
	 * 谈判条件 
	 */
	@Column(length = 1000, name = "tocondition")
	private String tocondition;

	

	public String getRent() {
		return rent;
	}


	public void setRent(String rent) {
		this.rent = rent;
	}


	public String getProperty() {
		return property;
	}


	public void setProperty(String property) {
		this.property = property;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getFloor() {
		return floor;
	}


	public void setFloor(String floor) {
		this.floor = floor;
	}


	public String getElectric() {
		return electric;
	}


	public void setElectric(String electric) {
		this.electric = electric;
	}


	public String getSidestation() {
		return sidestation;
	}


	public void setSidestation(String sidestation) {
		this.sidestation = sidestation;
	}


	public String getWater() {
		return water;
	}


	public void setWater(String water) {
		this.water = water;
	}


	public String getBlowdown() {
		return blowdown;
	}


	public void setBlowdown(String blowdown) {
		this.blowdown = blowdown;
	}


	public String getFirecontrol() {
		return firecontrol;
	}


	public void setFirecontrol(String firecontrol) {
		this.firecontrol = firecontrol;
	}


	public String getDesignpaper() {
		return designpaper;
	}


	public void setDesignpaper(String designpaper) {
		this.designpaper = designpaper;
	}


	public String getLamphouse() {
		return lamphouse;
	}


	public void setLamphouse(String lamphouse) {
		this.lamphouse = lamphouse;
	}


	public String getFloorheight() {
		return floorheight;
	}


	public void setFloorheight(String floorheight) {
		this.floorheight = floorheight;
	}


	public String getLeasetime() {
		return leasetime;
	}


	public void setLeasetime(String leasetime) {
		this.leasetime = leasetime;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getNetwork() {
		return network;
	}


	public void setNetwork(String network) {
		this.network = network;
	}


	public String getTocondition() {
		return tocondition;
	}


	public void setTocondition(String tocondition) {
		this.tocondition = tocondition;
	}


	public String getEditbutton() {
		return editbutton;
	}


	public void setEditbutton(String editbutton) {
		this.editbutton = editbutton;
	}


	public String getStandardname() {
		return standardname;
	}


	public void setStandardname(String standardname) {
		this.standardname = standardname;
	}


	
	
}
