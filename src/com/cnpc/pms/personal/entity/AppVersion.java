package com.cnpc.pms.personal.entity;

import java.util.Date;

import com.cnpc.pms.base.entity.IEntity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_app_version")
public class AppVersion implements IEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(length = 36, name = "version")
	private String version;
	
	@Column(length = 45, name = "file_name")
	private String file_name;
	
	@Column(length = 255, name = "message")
	private String message;
	
	@Column(name="tiptype")
	private Long tiptype;
	
	@Column(name="isupdate")
	private Long isupdate;
	
	
    @Column(name = "create_time")
    private Date create_time;

    @Column(length = 36, name = "update_user")
    private String update_user;

    @Column(length = 36, name = "create_user")
    private String create_user;

    @Column(name = "update_time")
    private Date update_time;

    @Column(name = "create_user_id")
    private Long create_user_id;

    @Column(name = "update_user_id")
    private Long update_user_id;

    @Column(name = "status")
    private Integer status = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public Long getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(Long create_user_id) {
		this.create_user_id = create_user_id;
	}

	public Long getUpdate_user_id() {
		return update_user_id;
	}

	public void setUpdate_user_id(Long update_user_id) {
		this.update_user_id = update_user_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTiptype() {
		return tiptype;
	}

	public void setTiptype(Long tiptype) {
		this.tiptype = tiptype;
	}

	public Long getIsupdate() {
		return isupdate;
	}

	public void setIsupdate(Long isupdate) {
		this.isupdate = isupdate;
	}

	
	
}
