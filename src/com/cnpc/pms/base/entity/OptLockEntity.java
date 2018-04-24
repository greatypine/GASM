package com.cnpc.pms.base.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * PMS Entity Class.<br>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn/
 * 
 * @author Zhou Zaiqing
 * @since 2010/12/14
 */
@MappedSuperclass
public abstract class OptLockEntity implements IEntity {

	/** The Constant serialVersionUID. */
	protected static final long serialVersionUID = 1L;

	/** The version. */
	@Column(name = "version")
	@Version
	private Long version;

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

}
