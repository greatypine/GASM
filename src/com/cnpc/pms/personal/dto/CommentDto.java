package com.cnpc.pms.personal.dto;

import java.util.Date;

import org.activiti.engine.task.Comment;

public class CommentDto implements Comment {

	
	private String message;
	
	@Override
	public String getFullMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProcessInstanceId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTaskId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
