package com.cnpc.pms.base.message.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.PMSEntity;

/**
 * The Class PhoneMessage.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
@Entity
@Table(name = "ts_base_phonemessage")
public class PhoneMessage extends PMSEntity {

	/** The Constant serialVersionUID. */
	protected static final long serialVersionUID = 1L;

	/** 主题. */
	@Column(length = 20, name = "subject")
	private String subject;

	/** 收件人. */
	@Column(length = 20, name = "recipient")
	private String recipient;

	/** 短信内容. */
	@Column(length = 20, name = "mailbody")
	private String messagebody;

	/** The statements. */
	@Column(length = 20, name = "statements")
	private String statements;

	/** The createby. */
	@Column(length = 20, name = "createby")
	private String createby;

	/** The priority. */
	@Column(name = "priority")
	private int priority;

	/** The create_date. */
	@Column(name = "createdate")
	private Date createDate;

	/** The last_update_date. */
	@Column(name = "lastupdatedate")
	private Date lastUpdateDate;

	/** The spa1. */
	@Column(name = "spare1")
	private String spa1;

	// //这里配置关系，并且确定关系维护端和被维护端。mappBy表示关系被维护端，只有关系端有权去更新外键。
	// //这里还有注意OneToMany默认的加载方式是赖加载。当看到设置关系中最后一个单词是Many，那么该加载默认为懒加载
	// @OneToMany(cascade = {CascadeType.REFRESH,
	// CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE},mappedBy
	// ="province")
	// private Set<City> cities = new HashSet<City>();

	/**
	 * Gets the createDate.
	 * 
	 * @return the createDate
	 */
	public Date getCreateDate() {
		Date newCreateDate = createDate;
		return newCreateDate;
	}

	/**
	 * Gets the createby.
	 * 
	 * @return the createby
	 */
	public String getCreateby() {
		return createby;
	}

	/**
	 * Gets the last_update_date.
	 * 
	 * @return the last_update_date
	 */
	public Date getLastUpdateDate() {
		Date newLastUpdateDate = lastUpdateDate;
		return newLastUpdateDate;
	}

	/**
	 * Gets the messagebody.
	 * 
	 * @return the messagebody
	 */
	public String getMessagebody() {
		return messagebody;
	}

	/**
	 * Gets the priority.
	 * 
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Gets the recipient.
	 * 
	 * @return the recipient
	 */
	public String getRecipient() {
		return recipient;
	}

	/**
	 * Gets the spa1.
	 * 
	 * @return the spa1
	 */
	public String getSpa1() {
		return spa1;
	}

	/**
	 * Gets the statements.
	 * 
	 * @return the statements
	 */
	public String getStatements() {
		return statements;
	}

	/**
	 * Gets the subject.
	 * 
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the createDate.
	 * 
	 * @param createDate
	 *            the new create_date
	 */
	public void setCreateDate(Date createDate) {
		if (createDate == null) {
			this.createDate = null;
		} else {
			this.createDate = (Date) createDate.clone();
		}
	}

	/**
	 * Sets the createby.
	 * 
	 * @param createby
	 *            the new createby
	 */
	public void setCreateby(String createby) {
		this.createby = createby;
	}

	/**
	 * Sets the last_update_date.
	 * 
	 * @param lastUpdateDate
	 *            the new last_update_date
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		if (lastUpdateDate == null) {
			this.lastUpdateDate = null;
		} else {
			this.lastUpdateDate = (Date) lastUpdateDate.clone();
		}
	}

	/**
	 * Sets the messagebody.
	 * 
	 * @param messagebody
	 *            the new messagebody
	 */
	public void setMessagebody(String messagebody) {
		this.messagebody = messagebody;
	}

	/**
	 * Sets the priority.
	 * 
	 * @param priority
	 *            the new priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Sets the recipient.
	 * 
	 * @param recipient
	 *            the new recipient
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	/**
	 * Sets the spa1.
	 * 
	 * @param spa1
	 *            the new spa1
	 */
	public void setSpa1(String spa1) {
		this.spa1 = spa1;
	}

	/**
	 * Sets the statements.
	 * 
	 * @param statements
	 *            the new statements
	 */
	public void setStatements(String statements) {
		this.statements = statements;
	}

	/**
	 * Sets the subject.
	 * 
	 * @param subject
	 *            the new subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/*	*//**
	 * @return the version
	 */
	/*
	 * public final int getVersion() { return version; }
	 *//**
	 * @set the version
	 */
	/*
	 * public final void setVersion(int version) { this.version = version; }
	 */
}
