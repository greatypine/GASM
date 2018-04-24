package com.cnpc.pms.personal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.cnpc.pms.base.entity.IEntity;
import com.cnpc.pms.base.entity.OptLockEntity;

@Entity
@Table(name = "ds_abnormal_type")
public class DsAbnormalType implements IEntity{

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length = 50, name = "abnortype")
	private String abnortype;
	
	@Column(length = 255, name = "description")
	private String description;
	
	@Column(length = 1000, name = "detail")
	private String detail;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAbnortype() {
		return abnortype;
	}

	public void setAbnortype(String abnortype) {
		this.abnortype = abnortype;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	
	


}
