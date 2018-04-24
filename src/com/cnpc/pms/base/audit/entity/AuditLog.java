package com.cnpc.pms.base.audit.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import com.cnpc.pms.base.audit.annotation.Audit;
import com.cnpc.pms.base.audit.annotation.ExtractorType;
import com.cnpc.pms.base.entity.PMSEntity;
import com.cnpc.pms.base.util.StrUtil;

/**
 * Audit Log
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Aug 14, 2013
 */
@Entity
@Table(name = "ts_base_audit_log")
public class AuditLog extends PMSEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private String userCode;
	private String userName;
	private Date operationTime;
	@Column(nullable = false)
	private String service;
	@Column(nullable = false)
	private String method;
	private int operationType;
	private String invokeData;
	// 0 for succeeded, otherwise for failed.
	private int status = 0;
	// exception information
	private String exception;

	public String getService() {
		return service;
	}

	public String getMethod() {
		return method;
	}

	public String getUserName() {
		return userName;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	public String getInvokeData() {
		return invokeData;
	}

	public void setInvokeData(String invokeData) {
		this.invokeData = invokeData;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public void setAudit(Audit anno, Object[] args) {
		this.setOperationType(anno.type().getValue());
		String template = anno.template();
		if (StrUtil.isNotBlank(template)) {
			String invokeData = null;
			if (anno.extractor() == ExtractorType.Script) {// TODO: maybe need optimize efficiency here.
				Context context = Context.enter();
				context.setLanguageVersion(Context.VERSION_1_7);

				ScriptableObject shell = new ScriptableObject() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getClassName() {
						return this.getClassName();
					}
				};
				context.initStandardObjects(shell);
				ScriptableObject.putProperty(shell, "a", args);
				invokeData = context.evaluateString(shell, template, null, 1, null).toString();
			} else {
				invokeData = template;
				for (int i = 0; i < args.length; i++) {
					invokeData = invokeData.replaceAll("\\$" + i, args[i].toString());
				}
			}
			this.setInvokeData(invokeData);
		}
	}
}
