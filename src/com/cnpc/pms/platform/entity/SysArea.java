package com.cnpc.pms.platform.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.OptLockEntity;

@Entity
@Table(name = "t_sys_area")
@AlternativeDS(source = "APP")
public class SysArea extends OptLockEntity {
	/**
	 * 主键
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;
	/**
	 * 名称
	 */
	@Column(name = "name", length = 100)
	private String name;
	/**
	 * code
	 */
	@Column(name = "code", length = 20)
	private String code;
	/**
	 * level
	 */
	@Column(name = "level")
	private Integer level;
	/**
	 * seq
	 */
	@Column(name = "seq")
	private Integer seq;
	/**
	 * 父code
	 */
	@Column(name = "parent_code", length = 50)
	private String parentCode;
	/**
	 * 状态标志位
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 创建者用户
	 */
	@Column(name = "create_user", length = 36)
	private String create_user;

	/**
	 * 记录创建时间
	 */
	@Column(name = "create_time")
	private Date create_time;

	/**
	 * 更新用户
	 */
	@Column(name = "update_user", length = 36)
	private String update_user;

	/**
	 * 记录更新时间
	 */
	@Column(name = "update_time")
	private Date update_time;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_user_id", length = 32)
	private String create_user_id;

	/**
	 * 更新人ID
	 */
	@Column(name = "update_user_id", length = 32)
	private String update_user_id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public String getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(String create_user_id) {
		this.create_user_id = create_user_id;
	}

	public String getUpdate_user_id() {
		return update_user_id;
	}

	public void setUpdate_user_id(String update_user_id) {
		this.update_user_id = update_user_id;
	}

}
