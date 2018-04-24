package com.cnpc.pms.bizbase.rbac.rolemanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.IEntity;
import com.cnpc.pms.base.entity.PMSAuditEntity;

/**
 * 分区排序后角色信息
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author ZhaoqQingdong
 * @since 2012-8-30
 */
@Entity
@Table(name = "view_role_order")
//@Inheritance(strategy = InheritanceType.JOINED)
//@JsonIgnoreProperties(value = { "fuctions", "userGroup" })

public class ViewRoleOrder implements IEntity {

	/** serialVersionUID. */
	private static final long serialVersionUID = -8009734541662650242L;
	
	@Id
	@Column (name="id")
	private Long id;

	// /** 角色排序编码. */
	// @Column(length = 20, name = "orderid")
	// private Long orderId;

	/** 角色编码. */
	@Column(length = 40, name = "code")
	private String code;

	/** 角色名称. */
	@Column(length = 200, name = "name")
	private String name;

	/** 角色说明. */
	@Column(length = 200, name = "note")
	private String note;

	/**
	 * 角色类型,暂定--211111:其他级,21:总部级 211-地区公司级 2111--二级单位级 21111--三级单位级
	 */
	// @Column(name = "type")
	// private String type;

	/** 停用标志,0是(停用),1否(未停用). */
	@Column
	private Integer disabledFlag;

	/**
	 * 角色所属类型 pur 物采 bid 招标
	 */
	// @Column(length = 20, name = "roleAttribute")
	// private String roleAttribute;

	/** The fuctions. */
	// @OneToMany(targetEntity = RoleFunc.class, cascade = CascadeType.ALL,
	// fetch = FetchType.LAZY, mappedBy = "roleEntity")
	// private Set<RoleFunc> fuctions;
	//
	// /** The user group. */
	// @OneToMany(targetEntity = UserGroupRole.class, cascade = CascadeType.ALL,
	// fetch = FetchType.LAZY, mappedBy = "role")
	// private Set<UserGroupRole> userGroup;

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		if (code == null) {
			return "";
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
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		if (name == null) {
			return "";
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
	 * Gets the note.
	 * 
	 * @return the note
	 */
	public String getNote() {
		if (note == null) {
			return "";
		}
		return this.note;
	}

	/**
	 * Sets the note.
	 * 
	 * @param note
	 *            the new note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	// public String getType() {
	// return this.type;
	// }
	//
	// /**
	// * Sets the type.
	// *
	// * @param type
	// * the new type
	// */
	// public void setType(String type) {
	// this.type = type;
	// }

	/**
	 * Gets the fuctions.
	 * 
	 * @return the fuctions
	 */
	// public Set<RoleFunc> getFuctions() {
	// return this.fuctions;
	// }
	//
	// /**
	// * Sets the fuctions.
	// *
	// * @param fuctions
	// * the new fuctions
	// */
	// public void setFuctions(Set<RoleFunc> fuctions) {
	// this.fuctions = fuctions;
	// }
	//
	// /**
	// * Gets the user group.
	// *
	// * @return the user group
	// */
	// public Set<UserGroupRole> getUserGroup() {
	// return userGroup;
	// }
	//
	// /**
	// * Sets the user group.
	// *
	// * @param userGroup
	// * the new user group
	// */
	// public void setUserGroup(Set<UserGroupRole> userGroup) {
	// this.userGroup = userGroup;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if ((this.name == null) || (this.code == null)) {
			if (this.name != null) {
				return this.name;
			}
			if (this.code != null) {
				return this.code;
			}
		}
		if ((this.name != null) && (this.code != null)) {
			return this.name + "|" + this.code;
		}
		return super.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
//	public int compareTo(ViewRoleOrder o) {
//		return (int) (getId() - o.getId());
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (code == null) {
			result = prime * result;
		} else {
			result = prime * result + code.hashCode();
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
		ViewRoleOrder other = (ViewRoleOrder) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}

	/**
	 * Sets the disabled flag.
	 * 
	 * @param disabledFlag
	 *            the new disabled flag
	 */
	public void setDisabledFlag(Integer disabledFlag) {
		this.disabledFlag = disabledFlag;
	}

	/**
	 * Gets the disabled flag.
	 * 
	 * @return the disabled flag
	 */
	public Integer getDisabledFlag() {
		// if (disabledFlag == null) {
		// disabledFlag = 1;
		// }
		return disabledFlag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// public String getRoleAttribute() {
	// return roleAttribute;
	// }
	//
	// public void setRoleAttribute(String roleAttribute) {
	// this.roleAttribute = roleAttribute;
	// }

	// public Long getOrderId() {
	// return orderId;
	// }
	//
	// public void setOrderId(Long orderId) {
	// this.orderId = orderId;
	// }
}
