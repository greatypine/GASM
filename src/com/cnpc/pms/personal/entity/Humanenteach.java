package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_human_teach")
public class Humanenteach extends DataEntity{
	
	/** 
	 * 培训主表ID
	 */
	@Column(name="hr_id")
	private Long hr_id;
	
	/**
	 * 岗后培训 开始时间
	 */
	@Column(name="startdate")
	private Date startdate;
	
	/**
	 * 培训 时长
	 */
	@Column(length = 65, name = "teachdate")
	private String teachdate;
	
	/**
	 * 培训 内容
	 */
	@Column(length = 255, name = "content")
	private String content;
	
	/**
	 *成绩
	 */
	@Column(name = "score")
	private double score;

	
	
	public Long getHr_id() {
		return hr_id;
	}

	public void setHr_id(Long hr_id) {
		this.hr_id = hr_id;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public String getTeachdate() {
		return teachdate;
	}

	public void setTeachdate(String teachdate) {
		this.teachdate = teachdate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	
}
