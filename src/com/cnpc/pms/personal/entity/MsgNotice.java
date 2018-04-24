package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;

@Entity
@Table(name = "t_msg_notice")
public class MsgNotice extends DataEntity{
	private static final long serialVersionUID = 1L;

	@Column(name = "title",length=255)
	private String title;
	
	@Column(name = "content",length=2000)
	private String content;
	
	@Column(name = "notice_date",length=32)
	private String notice_date;
	
	@Column(name = "notice_user",length=64)
	private String notice_user;

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNotice_date() {
		return notice_date;
	}

	public void setNotice_date(String notice_date) {
		this.notice_date = notice_date;
	}

	public String getNotice_user() {
		return notice_user;
	}

	public void setNotice_user(String notice_user) {
		this.notice_user = notice_user;
	}

}
