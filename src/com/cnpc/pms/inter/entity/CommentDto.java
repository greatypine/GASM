package com.cnpc.pms.inter.entity;

import java.util.Date;

public class CommentDto {

	private Date createtime;
	private String message;
	
	
	
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
