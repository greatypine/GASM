package com.cnpc.pms.personal.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "t_company_info")
public class Company {
	/*
	 * 公司主键
	 */
	@Id
	@GeneratedValue
	private Integer company_id;
	/*
	 * 写字楼主键
	 */
	@Column(length = 20, name = "office_id")
	private long office_id;
	/*
	 * 入驻公司名称
	 */
	@Column(length = 255, name = "office_company")
	private String office_company;
	/*
	 * 入驻公司名称
	 */
	@Column(length = 255, name = "company_name")
	private String company_name;
	/*
	 * 入驻公司所在楼层
	 */
	@Column(length = 255, name = "office_floor_of_company")
	private String office_floor_of_company;
	/*
	 * 预留房间号
	 */
	@Column(length = 255, name = "office_room_id")
	private String office_room_id;
	
	public long getOffice_id() {
		return office_id;
	}
	public void setOffice_id(long office_id) {
		this.office_id = office_id;
	}
	public String getOffice_company() {
		return office_company;
	}
	public void setOffice_company(String office_company) {
		this.office_company = office_company;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getOffice_floor_of_company() {
		return office_floor_of_company;
	}
	public void setOffice_floor_of_company(String office_floor_of_company) {
		this.office_floor_of_company = office_floor_of_company;
	}
	public String getOffice_room_id() {
		return office_room_id;
	}
	public void setOffice_room_id(String office_room_id) {
		this.office_room_id = office_room_id;
	}
	/*@Override
	public String toString() {
		return "Company [id=" + id + ", office_id=" + office_id + ", office_company=" + office_company
				+ ", company_name=" + company_name + ", office_floor_of_company=" + office_floor_of_company
				+ ", office_room_id=" + office_room_id + "]";
	}
	*/
	public Integer getCompany_id() {
		return company_id;
	}
	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}
	

}
