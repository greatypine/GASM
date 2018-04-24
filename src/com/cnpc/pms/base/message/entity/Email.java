package com.cnpc.pms.base.message.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.PMSEntity;

/**
 * The Class Email.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation ,
 * http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
@Entity
@Table(name = "ts_base_email")
public class Email extends PMSEntity {

	private static final long serialVersionUID = -7961141811616210748L;

	/** 发件人. */
	@Column(length = 20, name = "mailfrom")
	private String from;

	/** 主题. */
	@Column(length = 20, name = "subject")
	private String subject;

	/** 收件人. */
	@Column(length = 20, name = "recipient")
	private String recipient;

	/** 邮件内容. */
	@Column(length = 20, name = "mailbody")
	private String mailbody;

	/** 状态. */
	@Column(name = "status")
	private int status;

	/** The error reason of fail */
	@Column(length = 20, name = "error")
	private String error;

	@Column(name = "timestoretry")
	private int timesToRetry = 3;

	/** The displayname. */
	@Column(length = 20, name = "displayname")
	private String displayname;

	/** The cc. */
	@Column(length = 20, name = "cc")
	private String cc;

	/** The bcc. */
	@Column(length = 20, name = "bcc")
	private String bcc;

	/** 附件. */
	@Column(length = 20, name = "attachment")
	private String attachment;

	/** The statements. */
	@Column(length = 20, name = "statements")
	private String statements;

	/** The createby. */
	@Column(length = 20, name = "createby")
	private String createby;

	/** The create_date. */
	@Column(name = "createdate")
	private Date createDate;

	/** The should_sent_date. */
	@Column(name = "shouldsentdate")
	private Date shouldSentDate;

	/** The actual_sent_date. */
	@Column(name = "actualsentdate")
	private Date actualSentDate;

	/** The last_update_date. */
	@Column(name = "lastupdatedate")
	private Date lastUpdateDate;

	/**
	 * Gets the actual_sent_date.
	 * 
	 * @return the actual_sent_date
	 */
	public Date getActualSentDate() {
		Date newActualSentDate = actualSentDate;
		return newActualSentDate;
	}

	/**
	 * Gets the attachment.
	 * 
	 * @return the attachment
	 */
	public String getAttachment() {
		return attachment;
	}

	/**
	 * Gets the bcc.
	 * 
	 * @return the bcc
	 */
	public String getBcc() {
		return bcc;
	}

	/**
	 * Gets the cc.
	 * 
	 * @return the cc
	 */
	public String getCc() {
		return cc;
	}

	/**
	 * Gets the create_date.
	 * 
	 * @return the create_date
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
	 * Gets the displayname.
	 * 
	 * @return the displayname
	 */
	public String getDisplayname() {
		return displayname;
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
	 * Gets the mail body.
	 * 
	 * @return the mail body
	 */
	public String getMailbody() {
		return mailbody;
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
	 * Gets the should_sent_date.
	 * 
	 * @return the should_sent_date
	 */
	public Date getShouldSentDate() {
		Date newShouldSentDate = shouldSentDate;
		return newShouldSentDate;
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
	 * Sets the actual_sent_date.
	 * 
	 * @param actualSentDate
	 *            the new actual_sent_date
	 */
	public void setActualSentDate(Date actualSentDate) {
		if (actualSentDate == null) {
			this.actualSentDate = null;
		} else {
			this.actualSentDate = (Date) actualSentDate.clone();
		}
	}

	/**
	 * Sets the attachment.
	 * 
	 * @param attachment
	 *            the new attachment
	 */
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	/**
	 * Sets the bcc.
	 * 
	 * @param bcc
	 *            the new bcc
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	/**
	 * Sets the cc.
	 * 
	 * @param cc
	 *            the new cc
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * Sets the create_date.
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
	 * Sets the displayname.
	 * 
	 * @param displayname
	 *            the new displayname
	 */
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
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
	 * Sets the mail body.
	 * 
	 * @param mailbody
	 *            the new mail body
	 */
	public void setMailbody(String mailbody) {
		this.mailbody = mailbody;
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
	 * Sets the should_sent_date.
	 * 
	 * @param shouldSentDate
	 *            the new should_sent_date
	 */
	public void setShouldSentDate(Date shouldSentDate) {
		if (shouldSentDate == null) {
			this.shouldSentDate = null;
		} else {
			this.shouldSentDate = (Date) shouldSentDate.clone();
		}
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

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getTimesToRetry() {
		return timesToRetry;
	}

	public void setTimesToRetry(int timesToRetry) {
		this.timesToRetry = timesToRetry;
	}

}
