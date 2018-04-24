/**  
 * @Title: ExpertUser.java
 * @Package com.cnpc.pms.bizbase.rbac.usermanage.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author zhaobinbin
 * @date 2013-9-9 下午03:44:58
 */
package com.cnpc.pms.bizbase.rbac.usermanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSAuditEntity;

/**
 * @ClassName: ExpertUser
 * @Description:TODO(专家用户表)
 * @author zhaobinbin
 * @date 2013-9-9 下午03:44:58
 */
@Entity
@Table(name = "tb_bizbase_expertUser")

public class ExpertUser extends PMSAuditEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 是否院内专家
	 */
	@Column(name = "isInner")
	private Integer isInner;
	/**
	 * 专业
	 */
	@Column(name = "major")
	private Integer major;
	/**
	 * 研究方向
	 */
	@Column(name = "research")
	private Integer research;
	/**
	 * 姓名
	 */
	@Column(name = "userName")
	private String userName;
	/**
	 * 职务
	 */
	@Column(name = "zw")
	private String zw;
	/**
	 * 职称
	 */
	@Column(name = "zc")
	private String zc;
	/**
	 * 工作单位 （院内专家默认为勘探院，院外专家手填）
	 */
	@Column(name = "unit")
	private String unit;
	/**
	 * 联系方式
	 */
	@Column(name = "phone")
	private String phone;
	/**
	 * 身份证号
	 */
	@Column(name = "card")
	private String card;
	/**
	 * 是否停用
	 */
	@Column(name = "isUsed")
	private Integer isUsed;
	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;
	/**
	 * email
	 */
	@Column(name = "email")
	private String email;
	/**
	 * userid
	 */
	@Column(name = "userid")
	private Integer userid;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	public String getZc() {
		return zc;
	}

	public void setZc(String zc) {
		this.zc = zc;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public Integer getIsInner() {
		return isInner;
	}

	public void setIsInner(Integer isInner) {
		this.isInner = isInner;
	}

	public Integer getMajor() {
		return major;
	}

	public void setMajor(Integer major) {
		this.major = major;
	}

	public Integer getResearch() {
		return research;
	}

	public void setResearch(Integer research) {
		this.research = research;
	}

	public Integer getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}


}
