package com.cnpc.pms.base.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;


@MappedSuperclass
public abstract class PMSEntity extends OptLockEntity {

	/** The Constant serialVersionUID. */
	protected static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "generator", allocationSize = 10)
	//When using MySQL
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//When using ORACLE
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="generator")
	@AccessType("property")
	protected Long id;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "@" + this.getId();
	}
}
