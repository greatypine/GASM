/**  
* @Title: ExpertUserDTO.java
* @Package com.cnpc.pms.bizbase.rbac.usermanage.dto
* @Description: TODO(用一句话描述该文件做什么)
* @author IBM
* @date 2013-9-9 下午03:59:19
*/ 
package com.cnpc.pms.bizbase.rbac.usermanage.dto;

import javax.persistence.Column;

import com.cnpc.pms.base.dto.PMSDTO;

/**
 * @ClassName: ExpertUserDTO
 * @Description:TODO(专家DTO)
 * @author IBM
 * @date 2013-9-9 下午03:59:19
 */
public class ExpertUserDTO extends PMSDTO {
	private Long id;
	/**
	 * 是否院内专家
	 */
	private Integer isInner;
	/**
	 * 专业
	 */
	private Integer major;
	/**
	 * 研究方向
	 */
	private Integer research ;
	/**
	 * 姓名
	 */
	private String userName;
	/**
	 * 职务
	 */
	private String zw;
	/**
	 * 职称
	 */
	private String zc;
	/**
	 * 工作单位
	 *（院内专家默认为勘探院，院外专家手填）
	 */
	private String unit;
	/**
	 * 联系方式
	 */
	private String phone;
	/**
	 * 身份证号
	 */
	private String card;
	/**
	 * 是否停用
	 */
	private Integer isUsed;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * email
	 */
	private String email;
	/**
	 * userid
	 */
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
