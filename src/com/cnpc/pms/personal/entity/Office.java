package com.cnpc.pms.personal.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@Table(name = "t_office_info")
public class Office {
	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue
	private Integer office_id;
	/**
	 * 街道id
	 */
	@Column(length = 45, name = "town_id")
	private Long town_id;
	/**
	 *社区id
	 */
	@Column(length = 45, name = "village_id")
	private Long village_id;
	/**
	 * 写字楼名称
	 */
	@Column(length = 255, name = "office_name")
	private String office_name;
	/**
	 * 地址
	 */
	@Column(length = 255, name = "office_address")
	private String office_address;
	/**
	 * 类型
	 */
	@Column(length = 255, name = "office_type")
	private String office_type;
	/**
	 * 面积
	 */
	@Column(length = 255, name = "office_area")
	private String office_area;
	/**
	 * 楼层
	 */
	@Column(length = 255, name = "office_floor")
	private String office_floor;
	/**
	 * 停车位
	 */
	@Column(length = 255, name = "office_parking")
	private String office_parking;
	
	//员工编号
	@Column(length = 255, name = "employee_no")
		private String employee_no;
	//创建日期
	@Temporal(TemporalType.TIMESTAMP)
		@Column( name = "create_time")
			private Date create_time;
	//创建人
	@Column( length = 255,name = "create_user")
	private String create_user;
   
	//国安侠外键
	@Column(name = "user_id")
	private Long user_id;
	
	//是否检测
	@Column(name = "is_check")
	private Integer is_check;
	
	//修改日期
	@Temporal(TemporalType.TIMESTAMP)
	@Column( name = "update_time")
				private Date update_time;
		//修改人
		@Column( length = 255,name = "update_user")
		private String update_user;
		
		
		@Column(length = 45, name = "create_user_id")
		private Integer create_user_id;
		@Column(length = 45, name = "update_user_id")
		private Integer update_user_id;
		/** 拜访客户记录. */
		//@OneToMany(targetEntity = Company.class, cascade = CascadeType.ALL, mappedBy = "office_id")
		@Transient
		private Set<Company> company;
		
		@Transient
		private String province_name;
		@Transient
		private Long province_id;
		@Transient
		private String city_name;
		@Transient
		private Long city_id;
		@Transient
		private String county_name;
		@Transient
		private Long county_id;
		@Transient
		private String town_name;
		@Transient
		private String village_name;
		/**
		 *负责人
		 */
		@Column(length = 1055, name = "job")
		private String job;
		@Transient
		private String showSelectName;
		
		public String getJob() {
			return job;
		}
		public void setJob(String job) {
			this.job = job;
		}
		public String getShowSelectName() {
			return showSelectName;
		}
		public void setShowSelectName(String showSelectName) {
			this.showSelectName = showSelectName;
		}
		public Set<Company> getCompany() {
			return company;
		}
		public void setCompany(Set<Company> company) {
			this.company = company;
		}
		public Integer getCreate_user_id() {
			return create_user_id;
		}
		public void setCreate_user_id(Integer create_user_id) {
			this.create_user_id = create_user_id;
		}
		public Integer getUpdate_user_id() {
			return update_user_id;
		}
		public void setUpdate_user_id(Integer update_user_id) {
			this.update_user_id = update_user_id;
		}
		public Date getUpdate_time() {
			return update_time;
		}
		public void setUpdate_time(Date update_time) {
			this.update_time = update_time;
		}
		public String getUpdate_user() {
			return update_user;
		}
		public void setUpdate_user(String update_user) {
			this.update_user = update_user;
		}
		public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
		public String getEmployee_no() {
			return employee_no;
		}
		public void setEmployee_no(String employee_no) {
			this.employee_no = employee_no;
		}
	
	public Long getTown_id() {
		return town_id;
	}
	public void setTown_id(Long town_id) {
		this.town_id = town_id;
	}
	public Long getVillage_id() {
		return village_id;
	}
	public void setVillage_id(Long village_id) {
		this.village_id = village_id;
	}
	public String getOffice_name() {
		return office_name;
	}
	public void setOffice_name(String office_name) {
		this.office_name = office_name;
	}
	public String getOffice_address() {
		return office_address;
	}
	public void setOffice_address(String office_address) {
		this.office_address = office_address;
	}
	public String getOffice_type() {
		return office_type;
	}
	public void setOffice_type(String office_type) {
		this.office_type = office_type;
	}
	public String getOffice_area() {
		return office_area;
	}
	public void setOffice_area(String office_area) {
		this.office_area = office_area;
	}
	public String getOffice_floor() {
		return office_floor;
	}
	public void setOffice_floor(String office_floor) {
		this.office_floor = office_floor;
	}
	public String getOffice_parking() {
		return office_parking;
	}
	public void setOffice_parking(String office_parking) {
		this.office_parking = office_parking;
	}
	/*@Override
	public String toString() {
		return "Office [id=" + id + ", town_id=" + town_id + ", village_id=" + village_id + ", office_name="
				+ office_name + ", office_address=" + office_address + ", office_type=" + office_type + ", office_area="
				+ office_area + ", office_floor=" + office_floor + ", office_parking=" + office_parking + "]";
	}*/
	public Integer getOffice_id() {
		return office_id;
	}
	public void setOffice_id(Integer office_id) {
		this.office_id = office_id;
	}
	
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public Integer getIs_check() {
		return is_check;
	}
	public void setIs_check(Integer is_check) {
		this.is_check = is_check;
	}
	public String getProvince_name() {
		return province_name;
	}
	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}
	public Long getProvince_id() {
		return province_id;
	}
	public void setProvince_id(Long province_id) {
		this.province_id = province_id;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public Long getCity_id() {
		return city_id;
	}
	public void setCity_id(Long city_id) {
		this.city_id = city_id;
	}
	public String getCounty_name() {
		return county_name;
	}
	public void setCounty_name(String county_name) {
		this.county_name = county_name;
	}
	public Long getCounty_id() {
		return county_id;
	}
	public void setCounty_id(Long county_id) {
		this.county_id = county_id;
	}
	public String getTown_name() {
		return town_name;
	}
	public void setTown_name(String town_name) {
		this.town_name = town_name;
	}
	public String getVillage_name() {
		return village_name;
	}
	public void setVillage_name(String village_name) {
		this.village_name = village_name;
	}
	
	
	
}
