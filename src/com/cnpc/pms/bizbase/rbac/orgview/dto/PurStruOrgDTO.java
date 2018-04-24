package com.cnpc.pms.bizbase.rbac.orgview.dto;

import java.util.List;

import javax.persistence.Column;

import com.cnpc.pms.base.dto.PMSDTO;

/**
 * 物采组织机构DTO
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn * @author IBM
 * 
 * @since 2011-4-19
 */
public class PurStruOrgDTO extends PMSDTO implements Comparable<Object> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** 组织机构主键. */
	private Long id;

	/** 组织机构编码. */
	private String code;

	/** 组织机构称. */
	private String name;

	/** 组织机构简称. */
	private String shortname;

	/** 启用状态. */
	private Integer enablestate;

	/** 通讯地址. */
	private String postaddr;

	/** 邮政编码. */
	private String zipcode;

	/** 电话. */
	private String phone;

	/** 传真. */
	private String fax;

	/** 联系人. */
	private String linkman;

	/** 电子邮箱. */
	private String email;

	/** 0(板块)、1(单位)、2(部门). */
	private String entityOrgFlag;

	/** 上级采购组织. */
	private Long parent_id;

	/** 上级采购组织名称. */
	private String parentName;

	/**
	 * 上级组织机构编码
	 */
	private String parentCode;

	/** 单位网址. */
	private String website;

	/** 组织机构代码. */
	private String orgCode;

	/** 电报挂号. */
	private String cable;

	/** The nodes. */
	private List<PurStruOrgDTO> nodes;

	/** The is parent. */
	private boolean isParent;

	/** The has enabled child. */
	private boolean hasEnabledChild;

	/** The has enabled group. */
	private boolean hasEnabledGroup;

	/** The parent disabled. */
	private boolean parentDisabled;

	/** 组织机构所属路径. */
	private String path;

	/** 是否被选中状态. */
	private boolean checked;

	/** 是否有子节点被选中. */
	private boolean check_False_Full = true;

	/** 排序号. */
	private Long orderNo;

	/** 是否后勤单位标志 */
	private Integer logisticsFlag;

	/** 业务类型 */
	private Integer businessType;
	/** 工作日志排序 */
	private Long logOrderNo;

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
	 * Instantiates a new pur stru org dto.
	 */
	public PurStruOrgDTO() {

	}

	/**
	 * Instantiates a new pur stru org dto.
	 * 
	 * @param id
	 *            the id
	 * @param code
	 *            the code
	 * @param name
	 *            the name
	 */
	public PurStruOrgDTO(Long id, String code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	/**
	 * Checks if is parent disabled.
	 * 
	 * @return true, if is parent disabled
	 */
	public boolean isParentDisabled() {
		return parentDisabled;
	}

	/**
	 * Sets the parent disabled.
	 * 
	 * @param parentDisabled
	 *            the new parent disabled
	 */
	public void setParentDisabled(boolean parentDisabled) {
		this.parentDisabled = parentDisabled;
	}

	/**
	 * Checks if is checks for enabled child.
	 * 
	 * @return true, if is checks for enabled child
	 */
	public boolean isHasEnabledChild() {
		return hasEnabledChild;
	}

	/**
	 * Sets the checks for enabled child.
	 * 
	 * @param hasEnabledChild
	 *            the new checks for enabled child
	 */
	public void setHasEnabledChild(boolean hasEnabledChild) {
		this.hasEnabledChild = hasEnabledChild;
	}

	/**
	 * Checks if is checks for enabled group.
	 * 
	 * @return true, if is checks for enabled group
	 */
	public boolean isHasEnabledGroup() {
		return hasEnabledGroup;
	}

	/**
	 * Sets the checks for enabled group.
	 * 
	 * @param hasEnabledGroup
	 *            the new checks for enabled group
	 */
	public void setHasEnabledGroup(boolean hasEnabledGroup) {
		this.hasEnabledGroup = hasEnabledGroup;
	}

	/**
	 * Gets the checks if is parent.
	 * 
	 * @return the checks if is parent
	 */
	public boolean getIsParent() {
		return isParent;
	}

	/**
	 * Sets the checks if is parent.
	 * 
	 * @param isParent
	 *            the new checks if is parent
	 */
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return code;
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
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
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
		return shortname;
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
		return postaddr;
	}

	/**
	 * Sets the postaddr.
	 * 
	 * @param postaddr
	 *            the new postaddr
	 */
	public void setPostaddr(String postaddr) {
		if (null == postaddr) {
			postaddr = "";
		}
		this.postaddr = postaddr;
	}

	/**
	 * Gets the zipcode.
	 * 
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * Sets the zipcode.
	 * 
	 * @param zipcode
	 *            the new zipcode
	 */
	public void setZipcode(String zipcode) {
		if (null == zipcode) {
			zipcode = "";
		}
		this.zipcode = zipcode;
	}

	/**
	 * Gets the phone.
	 * 
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Sets the phone.
	 * 
	 * @param phone
	 *            the new phone
	 */
	public void setPhone(String phone) {
		if (null == phone) {
			phone = "";
		}
		this.phone = phone;
	}

	/**
	 * Gets the fax.
	 * 
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * Sets the fax.
	 * 
	 * @param fax
	 *            the new fax
	 */
	public void setFax(String fax) {
		if (null == fax) {
			fax = "";
		}
		this.fax = fax;
	}

	/**
	 * Gets the linkman.
	 * 
	 * @return the linkman
	 */
	public String getLinkman() {
		return linkman;
	}

	/**
	 * Sets the linkman.
	 * 
	 * @param linkman
	 *            the new linkman
	 */
	public void setLinkman(String linkman) {
		if (null == linkman) {
			linkman = "";
		}
		this.linkman = linkman;
	}

	/**
	 * Gets the email.
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 * 
	 * @param email
	 *            the new email
	 */
	public void setEmail(String email) {
		if (null == email) {
			email = "";
		}
		this.email = email;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param isParent
	 *            the new parent
	 */
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	/**
	 * Gets the website.
	 * 
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * Sets the website.
	 * 
	 * @param website
	 *            the new website
	 */
	public void setWebsite(String website) {
		if (null == website) {
			website = "";
		}
		this.website = website;
	}

	/**
	 * Gets the org code.
	 * 
	 * @return the org code
	 */
	public String getOrgCode() {
		return orgCode;
	}

	/**
	 * Sets the org code.
	 * 
	 * @param orgCode
	 *            the new org code
	 */
	public void setOrgCode(String orgCode) {
		if (null == orgCode) {
			orgCode = "";
		}
		this.orgCode = orgCode;
	}

	/**
	 * Gets the cable.
	 * 
	 * @return the cable
	 */
	public String getCable() {
		return cable;
	}

	/**
	 * Sets the cable.
	 * 
	 * @param cable
	 *            the new cable
	 */
	public void setCable(String cable) {
		if (null == cable) {
			cable = "";
		}
		this.cable = cable;
	}

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public List<PurStruOrgDTO> getNodes() {
		return this.nodes;
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodes
	 *            the new nodes
	 */
	public void setNodes(List<PurStruOrgDTO> nodes) {
		this.nodes = nodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		if (o instanceof PurStruOrgDTO) {
			PurStruOrgDTO p = (PurStruOrgDTO) o;
			return (int) (getId() - p.getId());
		}
		return -1;
	}

	/**
	 * Gets the entity org flag.
	 * 
	 * @return the entity org flag
	 */
	public String getEntityOrgFlag() {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (null == id) {
			result = prime * result;
		} else {
			result = prime * result + id.hashCode();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PurStruOrgDTO other = (PurStruOrgDTO) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if is checked.
	 * 
	 * @return true, if is checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * Sets the checked.
	 * 
	 * @param checked
	 *            the new checked
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
	 * Checks if is check_ false_ full.
	 * 
	 * @return true, if is check_ false_ full
	 */
	public boolean isCheck_False_Full() {
		return check_False_Full;
	}

	/**
	 * Sets the check_ false_ full.
	 * 
	 * @param checkFalseFull
	 *            the new check_ false_ full
	 */
	public void setCheck_False_Full(boolean checkFalseFull) {
		check_False_Full = checkFalseFull;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * Gets the parent_id.
	 * 
	 * @return the parent_id
	 */
	public Long getParent_id() {
		return this.parent_id;
	}

	/**
	 * Sets the parent_id.
	 * 
	 * @param parentId
	 *            the new parent_id
	 */
	public void setParent_id(Long parentId) {
		this.parent_id = parentId;
	}

	/**
	 * Gets the parent name.
	 * 
	 * @return the parent name
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * Sets the parent name.
	 * 
	 * @param parentName
	 *            the new parent name
	 */
	public void setParentName(String parentName) {
		if (null == parentName) {
			parentName = "";
		}
		this.parentName = parentName;
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
}
