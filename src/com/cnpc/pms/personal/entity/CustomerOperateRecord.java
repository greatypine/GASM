/**
 * gaobaolei
 */
package com.cnpc.pms.personal.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.pms.base.entity.DataEntity;

/**
 * @author gaobaolei
 * 对客户操作的记录
 */
@Entity
@Table(name = "t_customer_operate_record")
public class CustomerOperateRecord  extends DataEntity{
	
	private static final long serialVersionUID = 1L;
	
	//操作类型 0:新建 1：更新
	@Column(length=2,name="type")
	private int type;
	//是否是更多信息 0 否 1 是
	@Column(length=2,name="isMoreInfo")
	private int isMoreInfo;
	
	@Column(name = "customer_id")
    private Long customer_id;
	//姓名
	@Column(length = 45, name = "name")
	private String name;

	
	//移动电话
	@Column(length = 45, name = "mobilephone")
	private String mobilephone;


	@Column(length = 255, name = "customer_pic")
	private String customer_pic;

	
	//性别
	@Column(name = "sex")
	private Integer sex;

	
	//年龄
	@Column(name = "age")
	private Integer age;

	
	//民族
	@Column(length = 45, name = "nationality")
	private String nationality;

	
	//家庭住址
	@Column(length = 255, name = "address")
	private String address;

	
	//住房性质
	@Column(length = 45, name = "house_property")
	private String house_property;

	
	//职业
	@Column(length = 45, name = "job")
	private String job;

	
	//收入
	@Column(length = 45, name = "income")
	private String income;

	
	//加班频度
	@Column(length = 45, name = "work_overtime")
	private String work_overtime;

	
	//其它信息
	@Column(length = 1000, name = "other")
	private String other;

	
	//家庭人口数
	@Column(name = "family_num",length = 20)
	private String family_num;

	
	//学龄前人数
	@Column(name = "preschool_num")
	private Integer preschool_num;

	//未成年人数
	@Column(name = "minor_num")
	private Integer minor_num;
	
	//宠物种类
	@Column(length = 45, name = "pet_type")
	private String pet_type;


	//初始国安侠编号
	@Column(name = "employee_create_no")
	private String employee_create_no;
	
	//更新国安侠
	@Column(name = "employee_update_no")
	private String employee_update_no;

    //小区id
	@Column(name="tv_id")
    private Long tv_id;
   
    //楼ID
    @Column(name="building_id")
    private Long building_id;

    //房屋ID
    @Column(name="house_id")
    private Long house_id;

    //楼房单元
    @Column(name="building_unit_number")
    private String building_unit_number;
   
    //楼房房间号
    @Column(name="building_room_number")
    private  String building_room_number;
    
    //房屋
    @Column(name="house_style_id")
    private Long house_style_id;
    
    //房屋朝向
    @Column(name="house_toward")
    private String house_toward;
    
    //房屋户型
    @Column(name="house_style")
    private String house_style;
    
    //房屋面积
    @Column(name="house_area")
    private String house_area;
    
    //房屋户型图
    @Column(name="house_pic")
    private String house_pic;
    
    //房屋类型
    @Column(name="house_type")
    private Integer house_type;

    //国籍
  	@Column(length = 255, name = "state")
  	private String state;
  	
  	//籍贯省
  	@Column(length = 255, name = "native_place_pro")
  	private String native_place_pro;
  	
  	//籍贯市
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

  	@Column(length = 500, name = "family_ids")
	private String family_ids;
  	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getCustomer_pic() {
		return customer_pic;
	}

	public void setCustomer_pic(String customer_pic) {
		this.customer_pic = customer_pic;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHouse_property() {
		return house_property;
	}

	public void setHouse_property(String house_property) {
		this.house_property = house_property;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getWork_overtime() {
		return work_overtime;
	}

	public void setWork_overtime(String work_overtime) {
		this.work_overtime = work_overtime;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getFamily_num() {
		return family_num;
	}

	public void setFamily_num(String family_num) {
		this.family_num = family_num;
	}

	public Integer getPreschool_num() {
		return preschool_num;
	}

	public void setPreschool_num(Integer preschool_num) {
		this.preschool_num = preschool_num;
	}

	public Integer getMinor_num() {
		return minor_num;
	}

	public void setMinor_num(Integer minor_num) {
		this.minor_num = minor_num;
	}

	public String getPet_type() {
		return pet_type;
	}

	public void setPet_type(String pet_type) {
		this.pet_type = pet_type;
	}

	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	

	public String getEmployee_create_no() {
		return employee_create_no;
	}

	public void setEmployee_create_no(String employee_create_no) {
		this.employee_create_no = employee_create_no;
	}

	public String getEmployee_update_no() {
		return employee_update_no;
	}

	public void setEmployee_update_no(String employee_update_no) {
		this.employee_update_no = employee_update_no;
	}

	public Long getTv_id() {
		return tv_id;
	}

	public void setTv_id(Long tv_id) {
		this.tv_id = tv_id;
	}

	public Long getBuilding_id() {
		return building_id;
	}

	public void setBuilding_id(Long building_id) {
		this.building_id = building_id;
	}

	public Long getHouse_id() {
		return house_id;
	}

	public void setHouse_id(Long house_id) {
		this.house_id = house_id;
	}

	public String getBuilding_unit_number() {
		return building_unit_number;
	}

	public void setBuilding_unit_number(String building_unit_number) {
		this.building_unit_number = building_unit_number;
	}

	public String getBuilding_room_number() {
		return building_room_number;
	}

	public void setBuilding_room_number(String building_room_number) {
		this.building_room_number = building_room_number;
	}

	public Long getHouse_style_id() {
		return house_style_id;
	}

	public void setHouse_style_id(Long house_style_id) {
		this.house_style_id = house_style_id;
	}

	public String getHouse_toward() {
		return house_toward;
	}

	public void setHouse_toward(String house_toward) {
		this.house_toward = house_toward;
	}

	public String getHouse_style() {
		return house_style;
	}

	public void setHouse_style(String house_style) {
		this.house_style = house_style;
	}

	public String getHouse_area() {
		return house_area;
	}

	public void setHouse_area(String house_area) {
		this.house_area = house_area;
	}

	public String getHouse_pic() {
		return house_pic;
	}

	public void setHouse_pic(String house_pic) {
		this.house_pic = house_pic;
	}

	public Integer getHouse_type() {
		return house_type;
	}

	public void setHouse_type(Integer house_type) {
		this.house_type = house_type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getLocal_city() {
		return local_city;
	}

	public void setLocal_city(String local_city) {
		this.local_city = local_city;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	public String getFamily_ids() {
		return family_ids;
	}

	public void setFamily_ids(String family_ids) {
		this.family_ids = family_ids;
	}

	public Long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}

	public int getIsMoreInfo() {
		return isMoreInfo;
	}

	public void setIsMoreInfo(int isMoreInfo) {
		this.isMoreInfo = isMoreInfo;
	}
    
	
	
}
