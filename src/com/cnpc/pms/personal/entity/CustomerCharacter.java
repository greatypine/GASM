package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_character")
public class CustomerCharacter extends DataEntity{

	//客户id
	@Column(name = "customer_id")
	private Long customer_id;
	
	//外向与否
	@Column(name = "personality")
	private Integer personality;
	
	//是否好相处
	@Column(name = "name")
	private Integer name;
	
	//热心程度
	@Column(name = "enthusiasm")
	private Integer enthusiasm;
	
	//表达能力
	@Column(name = "expression")
	private Integer expression;
	
	//开朗与否
	@Column(name = "optimism")
	private Integer optimism;
	
	//自信与否
	@Column(name = "confidence")
	private Integer confidence;
	
	//是否幽默
	@Column(name = "humor")
	private Integer humor;

	public Long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}

	public Integer getPersonality() {
		return personality;
	}

	public void setPersonality(Integer personality) {
		this.personality = personality;
	}

	public Integer getName() {
		return name;
	}

	public void setName(Integer name) {
		this.name = name;
	}

	public Integer getEnthusiasm() {
		return enthusiasm;
	}

	public void setEnthusiasm(Integer enthusiasm) {
		this.enthusiasm = enthusiasm;
	}

	public Integer getExpression() {
		return expression;
	}

	public void setExpression(Integer expression) {
		this.expression = expression;
	}

	public Integer getOptimism() {
		return optimism;
	}

	public void setOptimism(Integer optimism) {
		this.optimism = optimism;
	}

	public Integer getConfidence() {
		return confidence;
	}

	public void setConfidence(Integer confidence) {
		this.confidence = confidence;
	}

	public Integer getHumor() {
		return humor;
	}

	public void setHumor(Integer humor) {
		this.humor = humor;
	}

}
