package com.cnpc.pms.personal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.PMSEntity;

@Entity
@Table(name = "t_oss_ref_file")
public class OssRefFile extends PMSEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "create_time")
    private Date create_time;
	
	@Column(name="create_user_id")
	private Long create_user_id;
    
	@Column(length = 255, name = "oss_url")
    private String oss_url;
    
	@Column(length = 64, name = "oss_name")
    private String oss_name;
    
	@Column(length = 255, name = "file_url")
    private String file_url;
    
	@Column(length = 64, name = "file_name")
    private String file_name;
    
	@Column(length = 64, name = "suffix")
    private String suffix;

	
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getOss_url() {
		return oss_url;
	}

	public void setOss_url(String oss_url) {
		this.oss_url = oss_url;
	}

	public String getOss_name() {
		return oss_name;
	}

	public void setOss_name(String oss_name) {
		this.oss_name = oss_name;
	}

	public String getFile_url() {
		return file_url;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Long getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(Long create_user_id) {
		this.create_user_id = create_user_id;
	}
}
