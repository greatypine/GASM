package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_app_message")
public class AppMessage extends DataEntity{

	/**
	 * 标题
	 */
	@Column(name = "title",length = 50)
	private String title;

	/**
	 * 内容
	 */
	@Column(name = "content",length = 255)
	private String content;

	/**
	 * 是否读取
	 */
	@Column(name = "isReady")
	private Boolean isReady = false;

	/**
	 * 读取时间
	 */
	@Column(name = "readDate")
	private Date readDate;

	/**
	 * 类型
	 */
	@Column(name = "type",length = 50)
	private String type;

	/**
	 * 跳转主键
	 */
	@Column(name = "pk_id")
	private Long pk_id;

	/**
	 * 跳转路径或id
	 */
	@Column(name = "jump_path")
	private String jump_path;

	/**
	 * 删除业务类
	 */
	@Column(name = "delete_manager",length = 100)
	private String delete_manager;

	/**
	 * 删除方法
	 */
	@Column(name = "delete_method",length = 100)
	private String delete_method;


	/**
	 * 审核方法
	 */
	@Column(name = "review_method",length = 100)
	private String review_method;

	/**
	 * 收信人
	 */
	@Column(name = "to_employee_id",length = 20)
	private Long to_employee_id;

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

	public Boolean getReady() {
		return isReady;
	}

	public void setReady(Boolean ready) {
		isReady = ready;
	}

	public Date getReadDate() {
		return readDate;
	}

	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getPk_id() {
		return pk_id;
	}

	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}

	public String getJump_path() {
		return jump_path;
	}

	public void setJump_path(String jump_path) {
		this.jump_path = jump_path;
	}

	public Long getTo_employee_id() {
		return to_employee_id;
	}

	public void setTo_employee_id(Long to_employee_id) {
		this.to_employee_id = to_employee_id;
	}

	public String getDelete_manager() {
		return delete_manager;
	}

	public void setDelete_manager(String delete_manager) {
		this.delete_manager = delete_manager;
	}

	public String getDelete_method() {
		return delete_method;
	}

	public void setDelete_method(String delete_method) {
		this.delete_method = delete_method;
	}

	public String getReview_method() {
		return review_method;
	}

	public void setReview_method(String review_method) {
		this.review_method = review_method;
	}
}
