package com.cnpc.pms.inter.entity;


import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.entity.PMSAuditEntity;

import javax.persistence.*;
import java.util.Date;


/**
 * 
 * 类名: DailyReport  
 * 功能描述: 门店日报实体类 点击门店日常按钮进入页面之后门店体验率的实体类
 * 作者: 常鹏飞  
 * 时间: 2016-3-1   
 *   
 */

@Entity
@Table(name = "t_daily_report")

public class DailyReport extends DataEntity{

	/**
	 */
	private static final long serialVersionUID = 1L;	
    //门店日报ID
	@Id
	@Column(name="id")
	private Long id;
	//门店ID
	@Column(name="store_id")
	private Long store_id;
	//报告日期
	@Column(name="report_date")
	private Date report_date;
	//进店人数
	@Column(name="enter_num")
	private Integer enter_num;
	//体验人数
	@Column(name="test_num")
	private Integer test_num;
	//店内注册APP
	@Column(name="reg_num")
	private Integer reg_num;
	//店内下单
	@Column(name="purchase_num")
	private Integer purchase_num;
	//生活宝试用
	@Column(name="life_test")
	private Integer life_test;
	//生活宝办卡
	@Column(name="life_card")
	private Integer life_card;
	//健康屋问询
	@Column(name="health_request")
	private Integer health_request;

	@Transient
	private String createUser;
	
	@Transient
	private String createUserId;
	
	
	
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * 返回: id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置:id
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * 返回: report_date
	 */
	public Date getReport_date() {
		return report_date;
	}
	/**
	 * 设置:report_date
	 */
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	/**
	 * 返回: enter_num
	 */
	public Integer getEnter_num() {
		return enter_num;
	}
	/**
	 * 设置:enter_num
	 */
	public void setEnter_num(Integer enter_num) {
		this.enter_num = enter_num;
	}
	/**
	 * 返回: test_num
	 */
	public Integer getTest_num() {
		return test_num;
	}
	/**
	 * 设置:test_num
	 */
	public void setTest_num(Integer test_num) {
		this.test_num = test_num;
	}
	/**
	 * 返回: reg_num
	 */
	public Integer getReg_num() {
		return reg_num;
	}
	/**
	 * 设置:reg_num
	 */
	public void setReg_num(Integer reg_num) {
		this.reg_num = reg_num;
	}
	/**
	 * 返回: purchase_num
	 */
	public Integer getPurchase_num() {
		return purchase_num;
	}
	/**
	 * 设置:purchase_num
	 */
	public void setPurchase_num(Integer purchase_num) {
		this.purchase_num = purchase_num;
	}
	/**
	 * 返回: life_test
	 */
	public Integer getLife_test() {
		return life_test;
	}
	/**
	 * 设置:life_test
	 */
	public void setLife_test(Integer life_test) {
		this.life_test = life_test;
	}
	/**
	 * 返回: life_card
	 */
	public Integer getLife_card() {
		return life_card;
	}
	/**
	 * 设置:life_card
	 */
	public void setLife_card(Integer life_card) {
		this.life_card = life_card;
	}
	/**
	 * 返回: health_request
	 */
	public Integer getHealth_request() {
		return health_request;
	}
	/**
	 * 设置:health_request
	 */
	public void setHealth_request(Integer health_request) {
		this.health_request = health_request;
	}
}
