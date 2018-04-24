package com.cnpc.pms.personal.entity;


import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.entity.PMSAuditEntity;

import javax.persistence.*;
import java.util.Date;


/**
 * 
 * 类名: UserAdvice  
 * 功能描述: 门店用户建议实体类 点击门店日常按钮进入页面之后客户意见的实体类
 * 作者: 常鹏飞  
 * 时间: 2016-3-1   
 *   
 */

@Entity
@Table(name = "t_user_advice")

public class UserAdvice extends DataEntity{

	/**
	 */
	private static final long serialVersionUID = 1L;	
    //用户建议ID
	//门店ID
	@Column(name="store_id")
	private Long store_id;
	//建议日期
	@Column(name="advice_date")
	private Date advice_date;
	//姓名
	@Column(name="name")
	private String name;
	//性别
	@Column(name="sex")
	private int sex;
	//年龄
	@Column(name="reg_num")
	private int reg_num;
	//电话
	@Column(name="mobilephone")
	private String mobilephone;
	//咨询内容
	@Column(name="request")
	private String request;
	//地址
	@Column(name="address")
	private String address;
	//合理化建议
	@Column(name="advice_content")
	private String advice_content;

	
	/*性别**/
	@Transient
	private String sex_2;
	
	/*年龄**/
	@Transient
	private String reg_num_2;
	
	/*状态标志位**/
	@Transient
	private String status_2;
	
	@Transient
	private String createUserId;
	
	
	
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getSex_2() {
		return sex_2;
	}
	public void setSex_2(String sex_2) {
		this.sex_2 = sex_2;
	}
	public String getReg_num_2() {
		return reg_num_2;
	}
	public void setReg_num_2(String reg_num_2) {
		this.reg_num_2 = reg_num_2;
	}
	public String getStatus_2() {
		return status_2;
	}
	public void setStatus_2(String status_2) {
		this.status_2 = status_2;
	}
	/**
	 * 返回: store_id
	 */
	public Long getStore_id() {
		return store_id;
	}
	/**
	 * 设置:store_id
	 */
	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}
	/**
	 * 返回: advice_date
	 */
	public Date getAdvice_date() {
		return advice_date;
	}
	/**
	 * 设置:advice_date
	 */
	public void setAdvice_date(Date advice_date) {
		this.advice_date = advice_date;
	}
	/**
	 * 返回: name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置:name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 返回: sex
	 */
	public int getSex() {
		return sex;
	}
	/**
	 * 设置:sex
	 */
	public void setSex(int sex) {
		this.sex = sex;
	}
	/**
	 * 返回: reg_num
	 */
	public int getReg_num() {
		return reg_num;
	}
	/**
	 * 设置:reg_num
	 */
	public void setReg_num(int reg_num) {
		this.reg_num = reg_num;
	}
	/**
	 * 返回: mobilephone
	 */
	public String getMobilephone() {
		return mobilephone;
	}
	/**
	 * 设置:mobilephone
	 */
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	/**
	 * 返回: request
	 */
	public String getRequest() {
		return request;
	}
	/**
	 * 设置:request
	 */
	public void setRequest(String request) {
		this.request = request;
	}
	/**
	 * 返回: address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置:address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 返回: advice_content
	 */
	public String getAdvice_content() {
		return advice_content;
	}
	/**
	 * 设置:advice_content
	 */
	public void setAdvice_content(String advice_content) {
		this.advice_content = advice_content;
	}
	
}
