package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name = "t_customer_data")
public class CustomerData extends DataEntity{
	//国籍
	@Column(length = 255, name = "nationality")
	private String nationality;
	
	//籍贯省
	@Column(length = 255, name = "native_place_pro")
	private String native_place_pro;
	
	//籍贯省
	@Column(length = 255, name = "native_place_city")
	private String native_place_city;

	//所在城市(户籍所在地_市)
	@Column(length = 255, name = "local_city")
	private String local_city;
	//所在城市(户籍所在地_省)
	@Column(length = 255, name = "local_pro")
	private String local_pro;
	
	//健康状况
	@Column(length = 255, name = "health_condition")
	private String health_condition;

	//婚姻状况
	@Column(length = 255, name = "marital_status")
	private String marital_status;

	//教育程度
	@Column(length = 255, name = "degree_of_education")
	private String degree_of_education;
	
	//学校名称
	@Column(length = 255, name = "school_name")
	private String school_name;
	
	//是否有车(0:有车,1:无)
	@Column(length = 255, name = "is_car")
	private String is_car;
	
	//汽车品牌
	@Column(length = 255, name = "automobile_brand")
	private String automobile_brand;
	
	//理财习惯
	@Column(length = 255, name = "money_manage")
	private String money_manage;
	
	//兴趣爱好
	@Column(length = 255, name = "hobby_one")
	private String hobby_one;
	
	//兴趣爱好
	@Column(length = 255, name = "hobby_two")
	private String hobby_two;
	
	//兴趣爱好
	@Column(length = 255, name = "hobby_three")
	private String hobby_three;
	
	//性格
	@Column(length = 255, name = "character_data")
	private String character_data;
	
	//家庭收入
	@Column(length = 255, name = "household_income")
	private String household_income;
	
	//购物渠道
	@Column(length = 255, name = "shopping_channel")
	private String shopping_channel;
	
	//员工号
	@Column(length = 255, name = "employee_no")
	private String employee_no;
	
	//所在门店
	@Column(length = 255, name = "store_id")
	private Long store_id;
	
	//关联用户id
	@Column(length = 255, name = "customer_id")
	private Long customer_id;
	
	

	public Long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	

	public String getLocal_city() {
		return local_city;
	}

	public void setLocal_city(String local_city) {
		this.local_city = local_city;
	}

	public String getMarital_status() {
		return marital_status;
	}

	public void setMarital_status(String marital_status) {
		this.marital_status = marital_status;
	}

	public String getDegree_of_education() {
		return degree_of_education;
	}

	public void setDegree_of_education(String degree_of_education) {
		this.degree_of_education = degree_of_education;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public String getIs_car() {
		return is_car;
	}

	public void setIs_car(String is_car) {
		this.is_car = is_car;
	}

	public String getAutomobile_brand() {
		return automobile_brand;
	}

	public void setAutomobile_brand(String automobile_brand) {
		this.automobile_brand = automobile_brand;
	}

	public String getMoney_manage() {
		return money_manage;
	}

	public void setMoney_manage(String money_manage) {
		this.money_manage = money_manage;
	}



	public String getCharacter_data() {
		return character_data;
	}

	public void setCharacter_data(String character_data) {
		this.character_data = character_data;
	}

	public String getHousehold_income() {
		return household_income;
	}

	public void setHousehold_income(String household_income) {
		this.household_income = household_income;
	}

	public String getShopping_channel() {
		return shopping_channel;
	}

	public void setShopping_channel(String shopping_channel) {
		this.shopping_channel = shopping_channel;
	}

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public String getNative_place_pro() {
		return native_place_pro;
	}

	public void setNative_place_pro(String native_place_pro) {
		this.native_place_pro = native_place_pro;
	}

	public String getNative_place_city() {
		return native_place_city;
	}

	public void setNative_place_city(String native_place_city) {
		this.native_place_city = native_place_city;
	}

	public String getLocal_pro() {
		return local_pro;
	}

	public void setLocal_pro(String local_pro) {
		this.local_pro = local_pro;
	}

	public String getHealth_condition() {
		return health_condition;
	}

	public void setHealth_condition(String health_condition) {
		this.health_condition = health_condition;
	}

	public String getHobby_one() {
		return hobby_one;
	}

	public void setHobby_one(String hobby_one) {
		this.hobby_one = hobby_one;
	}

	public String getHobby_two() {
		return hobby_two;
	}

	public void setHobby_two(String hobby_two) {
		this.hobby_two = hobby_two;
	}

	public String getHobby_three() {
		return hobby_three;
	}

	public void setHobby_three(String hobby_three) {
		this.hobby_three = hobby_three;
	}
	
	


}
