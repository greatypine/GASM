package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.*;
import javax.swing.text.View;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "t_customer")
public class Customer extends DataEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
	@Column(length = 45, name = "name")
	private String name;

	/**
	 * 移动电话
	 */
	@Column(length = 45, name = "mobilephone")
	private String mobilephone;


	@Column(length = 255, name = "customer_pic")
	private String customer_pic;

	@Transient
	private String cus_pic;

	/**
	 * 性别
	 */
	@Column(name = "sex")
	private Integer sex;

	/**
	 * 年龄
	 */
	@Column(name = "age")
	private Integer age;

	/**
	 * 民族
	 */
	@Column(length = 45, name = "nationality")
	private String nationality;

	/**
	 * 家庭住址
	 */
	@Column(length = 255, name = "address")
	private String address;

	/**
	 * 住房性质
	 */
	@Column(length = 45, name = "house_property")
	private String house_property;

	/**
	 * 职业
	 */
	@Column(length = 45, name = "job")
	private String job;

	/**
	 * 收入
	 */
	@Column(length = 45, name = "income")
	private String income;

	/**
	 * 加班频度
	 */
	@Column(length = 45, name = "work_overtime")
	private String work_overtime;

	/**
	 * 其它信息
	 */
	@Column(length = 1000, name = "other")
	private String other;

	/**
	 * 家庭人口数
	 */
	@Column(name = "family_num",length = 20)
	private String family_num;

	/**
	 * 学龄前人数
	 */
	@Column(name = "preschool_num")
	private Integer preschool_num;

	/**
	 * 未成年人数
	 */
	@Column(name = "minor_num")
	private Integer minor_num;

	/**
	 * 宠物种类
	 */
	@Column(length = 45, name = "pet_type")
	private String pet_type;

	/**
	 * 国安侠id
     */
	@Column(name = "employee_id")
	private Long employee_id;

    /**
     * 国安侠id
     */
    @Column(name = "employee_no",length = 32)
    private String employee_no;
    
    /**
	 *排序
	 */
	@Column(name = "sort")
	private Integer sort;
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Transient
	private Long store_id;

	/** 下级功能节点. */
	@OneToMany(targetEntity = Family.class, cascade = CascadeType.ALL, mappedBy = "customer_id")
	private Set<Family> childs;

	/** 拜访客户记录. */
	@OneToMany(targetEntity = Relation.class, cascade = CascadeType.ALL, mappedBy = "customer_id")
	private Set<Relation> relations;

	@Transient
	private ViewAddressCustomer customer_address;

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

	public String getCustomer_pic() {
		return customer_pic;
	}

	public void setCustomer_pic(String customer_pic) {
		this.customer_pic = customer_pic;
	}

	public Long getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(Long employee_id) {
		this.employee_id = employee_id;
	}

	public Set<Family> getChilds() {
		return childs;
	}

	public void setChilds(Set<Family> childs) {
		this.childs = childs;
	}

    public String getEmployee_no() {
        return employee_no;
    }

    public void setEmployee_no(String employee_no) {
        this.employee_no = employee_no;
    }

	public ViewAddressCustomer getCustomer_address() {
		return customer_address;
	}

	public void setCustomer_address(ViewAddressCustomer customer_address) {
		this.customer_address = customer_address;
	}


	public String getCus_pic() {
		return cus_pic;
	}

	public void setCus_pic(String cus_pic) {
		this.cus_pic = cus_pic;
	}

	public Set<Relation> getRelations() {
		return relations;
	}

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public void setRelations(Set<Relation> relations) {
//		this.relations = relations;
		if(relations == null){
			this.relations = relations;
		}else{
			this.relations = new TreeSet<Relation>();
			for(Relation res : relations){
				this.relations.add(res);
			}
		}

	}
}
