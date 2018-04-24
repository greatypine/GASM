package com.cnpc.pms.personal.dto;

public class StoreAddressDto{
	

	/**
	 * 填写用户ID  
	 */
	private Long site_selection_id;
	
	/**
	 * 店铺需求ID 
	 */
	private Long site_requirement_id;
	
	/**
	 * 店铺详细地址
	 */
	private String store_address;
	
	/**
	 * 计租面积
	 */
	private Double total_area;
	
	/**
	 * 使用面积
	 */
	private Double used_area;
	
	/**
	 * 预计租金
	 */
	private Double store_rent;
	
	/**
	 * 租金是否含税提供选项进行勾选：是、否   
	 */
	private String renttax_flag;
	
	/**
	 * 店铺电量 electric_quantity
	 */
	private Double electric_quantity;							 
		  
	/**
	 * 店铺格局	提供选项进行勾选：较方正、长方形、异形
	 */
	private String store_style;
	
	/**
	 * 室内立柱或承重墙是否超过4个	提供选项进行勾选：是、否
	 */
	private String fourpillar_flag;
	
	/**
	 * 内部层高
	 */
	private Double inside_height;
	 
	/**
	 * 是否有自建二层	提供选项进行勾选：是、否 
	 */
	private String twofloor_flag;
	
	
	/**
	 * 门面宽度
	 */
	private Double door_size;
	
	
	/**
	 * 招牌尺寸	提供两个输入栏：高、宽，均填写阿拉伯数字
	 */
	private String sign_size;
	
	
	/**
	 * 是否有上下水	提供选项进行勾选：有、没有 
	 */
	private String water_flag;
	
	/**
	 * 是否为纯一层	提供选项进行勾选：是、否 
	 */
	private String onefloor_flag;  
	             
	/**
	 * 是否有消防验收	提供选项进行勾选：有、没有 
	 */
	private String firecontrol_flag; 
	
	
	/**
	 * 是否为房东直租	提供选项进行勾选：是、否 
	 */
	private String houseowner_flag; 
	           
	 
	/**
	 * 出租方联系人
	 */
	private String contact_name;
	
	
	/**
	 *出租方联系方式
	 */
	private String contact_tel;
	
	
	/**
	 *店铺情况介绍
	 */
	private String store_remark;
	
	
	
	/**
	 *店铺正面近景照片
	 */
	private String store_pic;
	/**
	 *店铺右侧街道照片
	 */
	private String store_rightpic;
	/**
	 *店铺左侧街道照片
	 */
	private String store_leftpic;
	/**
	 *店铺对面照片
	 */
	private String store_oppositepic;
	
	
	
	/**
	 *店铺室内照片
	 */
	private String store_pic1;
	/**
	 *店铺室内照片
	 */
	private String store_pic2;
	/**
	 *店铺室内照片
	 */
	private String store_pic3;
	/**
	 *店铺室内照片
	 */
	private String store_pic4;
	/**
	 *店铺室内照片
	 */
	private String store_pic5;
	
	/**
	 * 状态  1 待审核  2驳回    3待勘察    4勘察驳回   5待签约   6签约失败   7签约成功
	 */
	private Long store_status;
	
	/**
	 * 补充说明 
	 */
	private String additional_remarks;
	
	
	public Long getSite_selection_id() {
		return site_selection_id;
	}
	public void setSite_selection_id(Long site_selection_id) {
		this.site_selection_id = site_selection_id;
	}
	public String getStore_address() {
		return store_address;
	}
	public void setStore_address(String store_address) {
		this.store_address = store_address;
	}
	public Double getTotal_area() {
		return total_area;
	}
	public void setTotal_area(Double total_area) {
		this.total_area = total_area;
	}
	public Double getUsed_area() {
		return used_area;
	}
	public void setUsed_area(Double used_area) {
		this.used_area = used_area;
	}
	public Double getStore_rent() {
		return store_rent;
	}
	public void setStore_rent(Double store_rent) {
		this.store_rent = store_rent;
	}
	public String getRenttax_flag() {
		return renttax_flag;
	}
	public void setRenttax_flag(String renttax_flag) {
		this.renttax_flag = renttax_flag;
	}
	public Double getElectric_quantity() {
		return electric_quantity;
	}
	public void setElectric_quantity(Double electric_quantity) {
		this.electric_quantity = electric_quantity;
	}
	public String getStore_style() {
		return store_style;
	}
	public void setStore_style(String store_style) {
		this.store_style = store_style;
	}
	public String getFourpillar_flag() {
		return fourpillar_flag;
	}
	public void setFourpillar_flag(String fourpillar_flag) {
		this.fourpillar_flag = fourpillar_flag;
	}
	public Double getInside_height() {
		return inside_height;
	}
	public void setInside_height(Double inside_height) {
		this.inside_height = inside_height;
	}
	public String getTwofloor_flag() {
		return twofloor_flag;
	}
	public void setTwofloor_flag(String twofloor_flag) {
		this.twofloor_flag = twofloor_flag;
	}
	public Double getDoor_size() {
		return door_size;
	}
	public void setDoor_size(Double door_size) {
		this.door_size = door_size;
	}
	public String getSign_size() {
		return sign_size;
	}
	public void setSign_size(String sign_size) {
		this.sign_size = sign_size;
	}
	public String getWater_flag() {
		return water_flag;
	}
	public void setWater_flag(String water_flag) {
		this.water_flag = water_flag;
	}
	public String getOnefloor_flag() {
		return onefloor_flag;
	}
	public void setOnefloor_flag(String onefloor_flag) {
		this.onefloor_flag = onefloor_flag;
	}
	public String getFirecontrol_flag() {
		return firecontrol_flag;
	}
	public void setFirecontrol_flag(String firecontrol_flag) {
		this.firecontrol_flag = firecontrol_flag;
	}
	public String getHouseowner_flag() {
		return houseowner_flag;
	}
	public void setHouseowner_flag(String houseowner_flag) {
		this.houseowner_flag = houseowner_flag;
	}
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}
	public String getContact_tel() {
		return contact_tel;
	}
	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}
	public String getStore_remark() {
		return store_remark;
	}
	public void setStore_remark(String store_remark) {
		this.store_remark = store_remark;
	}
	public String getStore_pic1() {
		return store_pic1;
	}
	public void setStore_pic1(String store_pic1) {
		this.store_pic1 = store_pic1;
	}
	public String getStore_pic2() {
		return store_pic2;
	}
	public void setStore_pic2(String store_pic2) {
		this.store_pic2 = store_pic2;
	}
	public String getStore_pic3() {
		return store_pic3;
	}
	public void setStore_pic3(String store_pic3) {
		this.store_pic3 = store_pic3;
	}
	public String getStore_pic4() {
		return store_pic4;
	}
	public void setStore_pic4(String store_pic4) {
		this.store_pic4 = store_pic4;
	}
	public String getStore_pic5() {
		return store_pic5;
	}
	public void setStore_pic5(String store_pic5) {
		this.store_pic5 = store_pic5;
	}
	public String getStore_pic() {
		return store_pic;
	}
	public void setStore_pic(String store_pic) {
		this.store_pic = store_pic;
	}
	public String getStore_rightpic() {
		return store_rightpic;
	}
	public void setStore_rightpic(String store_rightpic) {
		this.store_rightpic = store_rightpic;
	}
	public String getStore_leftpic() {
		return store_leftpic;
	}
	public void setStore_leftpic(String store_leftpic) {
		this.store_leftpic = store_leftpic;
	}
	public String getStore_oppositepic() {
		return store_oppositepic;
	}
	public void setStore_oppositepic(String store_oppositepic) {
		this.store_oppositepic = store_oppositepic;
	}
	public Long getStore_status() {
		return store_status;
	}
	public void setStore_status(Long store_status) {
		this.store_status = store_status;
	}
	public Long getSite_requirement_id() {
		return site_requirement_id;
	}
	public void setSite_requirement_id(Long site_requirement_id) {
		this.site_requirement_id = site_requirement_id;
	}
	public String getAdditional_remarks() {
		return additional_remarks;
	}
	public void setAdditional_remarks(String additional_remarks) {
		this.additional_remarks = additional_remarks;
	}
	
	
	
}
