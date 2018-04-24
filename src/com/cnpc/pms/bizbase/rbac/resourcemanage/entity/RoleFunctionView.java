package com.cnpc.pms.bizbase.rbac.resourcemanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.IEntity;

@Entity
@Table(name = "view_roleFunctionView")

public class RoleFunctionView implements IEntity {

	/**
	 * 角色对功能视图
	 */
	private static final long serialVersionUID = 1L;
	/** The id. */
	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "funcRoleId")
	private Long funcRoleId;

	@Column(name = "functionId")
	private Long functionId;

	@Column(name = "menuName")
	private String menuName;

	@Column(name = "buttonName")
	private String buttonName;

	public Long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFuncRoleId() {
		return funcRoleId;
	}

	public void setFuncRoleId(Long funcRoleId) {
		this.funcRoleId = funcRoleId;
	}

}
