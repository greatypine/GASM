package com.cnpc.pms.personal.entity;


import com.cnpc.pms.base.entity.IEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_data_human_type")
public class DataHumanType implements IEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(length = 65, name = "career_group")
	private String career_group;
	
	@Column(length = 65, name = "groupcode")
	private String groupcode;
	
	@Column(length = 255, name = "remark")
	private String remark;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCareer_group() {
		return career_group;
	}

	public void setCareer_group(String career_group) {
		this.career_group = career_group;
	}

	public String getGroupcode() {
		return groupcode;
	}

	public void setGroupcode(String groupcode) {
		this.groupcode = groupcode;
	}
	
	
}
