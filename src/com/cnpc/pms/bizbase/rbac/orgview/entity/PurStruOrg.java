package com.cnpc.pms.bizbase.rbac.orgview.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSAuditEntity;
//import com.cnpc.pms.bizbase.rbac.position.entity.Position;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.DisableFlagEnum;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;

/**
 * 组织机构实体
 * 
 *  Copyright(c) 2014 Yadea Technology Group ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-11
 */
@Entity
@Table(name = "tb_bizbase_psorg")
@JsonIgnoreProperties(value = { "childs", "roleGroups", "userGroups",
		"positions", "parentEntity" })
@Inheritance(strategy = InheritanceType.JOINED)

public class PurStruOrg extends PMSAuditEntity implements Comparable<Object> {

	/** serialVersionUID. */
	private static final long serialVersionUID = 551969014036773750L;
	
	
	/** 单位编码. */
	@Column(length = 40, name = "code")
	private String code;

	/** 单位名称. */
	@Column(length = 200, name = "name")
	private String name;

	/** 单位简称. */
	@Column(length = 200, name = "shortname")
	private String shortname;

	/** 组织机构所属路径. */
	@Column(name = "path")
	private String path;

	/** 停用标识. */
	@Column(name = "enablestate")
	private Integer enablestate;

	/** 单位地址. */
	@Column(length = 100, name = "postaddr")
	private String postaddr;

	/** 邮政编码. */
	@Column(length = 10, name = "zipcode")
	private String zipcode;

	/** 电话. */
	@Column(length = 60, name = "phone")
	private String phone;

	/** 传真. */
	@Column(length = 60, name = "fax")
	private String fax;

	/** 联系人. */
	@Column(length = 40, name = "linkman")
	private String linkman;

	/** 电子邮箱. */
	@Column(length = 60, name = "email")
	private String email;

	/** 单位属性 0(集团)、1(内部外部)、2(部门/地区) 3、部门/科室/经销商 */
	@Column(length = 4, name = "EntityOrgFlag")
	private String entityOrgFlag;

	/** 单位网址. */
	@Column(length = 200, name = "website")
	private String website;

	/** 组织机构代码. */
	@Column(length = 40, name = "orgCode")
	private String orgCode;

	/** 电报挂号. */
	@Column(length = 40, name = "cable")
	private String cable;

	/** 上级组织. */
	@ManyToOne
	@JoinColumn(name = "parent_id")
	@OrderBy("parent_id ASC")
	private PurStruOrg parentEntity;

	/** 下级组织. */
	@OneToMany(targetEntity = PurStruOrg.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentEntity")
	private Set<PurStruOrg> childs;

	/** The user groups. */
	@OneToMany(targetEntity = UserGroup.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orgEntity")
	private Set<UserGroup> userGroups;

//	/** 岗位. */
//	@OneToMany(targetEntity = Position.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "purStruOrg")
//	private Set<Position> positions;

	/** 排序号. */
	@Column(name = "orderno")
	private Long orderNo;

	/** 是否后勤单位标志 */
	@Column(name = "logisticsFlag")
	private Integer logisticsFlag;
	
	/** 业务类型 */
	@Column (name="businessType")
	private Integer businessType;
	
	/** 工作日志排序 */
	@Column (name="logOrderNo")
	private Long logOrderNo;
	
	//针对雅迪情况，增加的经销商对应CODE
	//这一字段仅对外部的经销商机构是有效的，其余情况字段为空
	@Column(name="AGENT_CODE")
	private String agentCode;
	
	/**省*/
	@Column(name="province")
	private String province;
	/**市*/
	@Column(name="city")
	private String city;
	/**区*/
	@Column(name="area")
	private String area;
	/**街道**/
	@Column(name="street")
	private String street;
	/**机构层级，健康屋位于第四等级**/
	@Column(length = 1, name="orgLevel")
	private String orgLevel;
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		if (this.code == null) {
			this.code = "";
		}
		return this.code;
	}

	/**
	 * Sets the code.
	 * 
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the path.
	 * 
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path.
	 * 
	 * @param path
	 *            the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		if (this.name == null) {
			this.name = "";
		}
		return this.name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the shortname.
	 * 
	 * @return the shortname
	 */
	public String getShortname() {
		if (this.shortname == null) {
			this.shortname = "";
		}
		return this.shortname;
	}

	/**
	 * Sets the shortname.
	 * 
	 * @param shortname
	 *            the new shortname
	 */
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	/**
	 * Gets the enablestate.
	 * 
	 * @return the enablestate
	 */
	public Integer getEnablestate() {
		// if (this.enablestate == null) {
		// this.enablestate = 1;
		// }
		return enablestate;
	}

	/**
	 * Sets the enablestate.
	 * 
	 * @param enablestate
	 *            the new enablestate
	 */
	public void setEnablestate(Integer enablestate) {
		this.enablestate = enablestate;
	}

	/**
	 * Gets the postaddr.
	 * 
	 * @return the postaddr
	 */
	public String getPostaddr() {
		if (this.postaddr == null) {
			this.postaddr = "";
		}
		return postaddr;
	}

	/**
	 * Sets the postaddr.
	 * 
	 * @param postaddr
	 *            the new postaddr
	 */
	public void setPostaddr(String postaddr) {
		this.postaddr = postaddr;
	}

	/**
	 * Gets the zipcode.
	 * 
	 * @return the zipcode
	 */
	public String getZipcode() {
		if (this.zipcode == null) {
			this.zipcode = "";
		}
		return zipcode;
	}

	/**
	 * Sets the zipcode.
	 * 
	 * @param zipcode
	 *            the new zipcode
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * Gets the phone.
	 * 
	 * @return the phone
	 */
	public String getPhone() {
		if (this.phone == null) {
			this.phone = "";
		}
		return phone;
	}

	/**
	 * Sets the phone.
	 * 
	 * @param phone
	 *            the new phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Gets the fax.
	 * 
	 * @return the fax
	 */
	public String getFax() {
		if (this.fax == null) {
			this.fax = "";
		}
		return fax;
	}

	/**
	 * Sets the fax.
	 * 
	 * @param fax
	 *            the new fax
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * Gets the linkman.
	 * 
	 * @return the linkman
	 */
	public String getLinkman() {
		if (this.linkman == null) {
			this.linkman = "";
		}
		return linkman;
	}

	/**
	 * Sets the linkman.
	 * 
	 * @param linkman
	 *            the new linkman
	 */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	/**
	 * Gets the email.
	 * 
	 * @return the email
	 */
	public String getEmail() {
		if (this.email == null) {
			this.email = "";
		}
		return email;
	}

	/**
	 * Sets the email.
	 * 
	 * @param email
	 *            the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the parent entity.
	 * 
	 * @return the parent entity
	 */
	public PurStruOrg getParentEntity() {
		return this.parentEntity;
	}

	/**
	 * Sets the parent entity.
	 * 
	 * @param parentEntity
	 *            the new parent entity
	 */
	public void setParentEntity(PurStruOrg parentEntity) {
		this.parentEntity = parentEntity;
	}

	/**
	 * Gets the childs.
	 * 
	 * @return the childs
	 */
	public Set<PurStruOrg> getChilds() {
		return this.childs;
	}

	/**
	 * Sets the childs.
	 * 
	 * @param childs
	 *            the new childs
	 */
	public void setChilds(Set<PurStruOrg> childs) {
		this.childs = childs;
	}

	/**
	 * Gets the website.
	 * 
	 * @return the website
	 */
	public String getWebsite() {
		if (this.website == null) {
			this.website = "";
		}
		return website;
	}

	/**
	 * Sets the website.
	 * 
	 * @param website
	 *            the new website
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * Gets the org code.
	 * 
	 * @return the org code
	 */
	public String getOrgCode() {
		if (this.orgCode == null) {
			this.orgCode = "";
		}
		return orgCode;
	}

	/**
	 * Sets the org code.
	 * 
	 * @param orgCode
	 *            the new org code
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	/**
	 * Gets the cable.
	 * 
	 * @return the cable
	 */
	public String getCable() {
		if (this.cable == null) {
			this.cable = "";
		}
		return cable;
	}

	/**
	 * Sets the cable.
	 * 
	 * @param cable
	 *            the new cable
	 */
	public void setCable(String cable) {
		this.cable = cable;
	}

	/**
	 * Gets the entity org flag.
	 * 
	 * @return the entity org flag
	 */
	public String getEntityOrgFlag() {
		if (entityOrgFlag == null) {
			entityOrgFlag = "";
		}
		return entityOrgFlag;
	}

	/**
	 * Sets the entity org flag.
	 * 
	 * @param entityOrgFlag
	 *            the new entity org flag
	 */
	public void setEntityOrgFlag(String entityOrgFlag) {
		this.entityOrgFlag = entityOrgFlag;
	}

	/**
	 * Checks for enabled child.
	 * 
	 * @return true, if successful
	 */
	public boolean hasEnabledChild() {
		Set<PurStruOrg> orgs = this.getChilds();
		for (PurStruOrg org : orgs) {
			if (DisableFlagEnum.OFF.getDisabledFlag().equals(
					org.getEnablestate())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the entity userGroups.
	 * 
	 * @param userGroups
	 *            the new entity userGroups
	 */
	public void setUserGroups(Set<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	/**
	 * Gets the entity userGroups.
	 * 
	 * @return the entity userGroups
	 */
	public Set<UserGroup> getUserGroups() {
		return userGroups;
	}

	/**
	 * Sets the entity positions.
	 * 
	 * @param positions
	 *            the new entity positions
	 */
//	public void setPositions(Set<Position> positions) {
//		this.positions = positions;
//	}
//
//	/**
//	 * Gets the entity positions.
//	 *
//	 * @return the entity positions
//	 */
//	public Set<Position> getPositions() {
//		return positions;
//	}

	/**
	 * Checks for enabled group.
	 * 
	 * @return true, if successful
	 */
	public boolean hasEnabledGroup() {
		Set<UserGroup> groups = this.getUserGroups();
		for (UserGroup userGroup : groups) {
			if (DisableFlagEnum.OFF.getDisabledFlag().equals(
					userGroup.getDisabledFlag())) {
				return true;
			}
		}
		return false;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getLogisticsFlag() {
		return logisticsFlag;
	}

	public void setLogisticsFlag(Integer logisticsFlag) {
		this.logisticsFlag = logisticsFlag;
	}
	
	public Integer getBusinessType() {
    return businessType;
  }

  public void setBusinessType(Integer businessType) {
    this.businessType = businessType;
  }

  public int compareTo(Object o) {
		if (o instanceof PurStruOrg) {
			PurStruOrg p = (PurStruOrg) o;
			return (Integer.parseInt(p.getEntityOrgFlag()) - Integer
					.parseInt(getEntityOrgFlag()));
		}
		return -1;
	}

public Long getLogOrderNo() {
	return logOrderNo;
}

public void setLogOrderNo(Long logOrderNo) {
	this.logOrderNo = logOrderNo;
}

public String getAgentCode() {
	return agentCode;
}

public void setAgentCode(String agentCode) {
	this.agentCode = agentCode;
}

public static long getSerialversionuid() {
	return serialVersionUID;
}

}
