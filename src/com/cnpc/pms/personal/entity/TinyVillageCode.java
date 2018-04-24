package com.cnpc.pms.personal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.OptLockEntity;

@Entity
@Table(name = "tiny_village_code")
public class TinyVillageCode extends OptLockEntity {
	// 小区编码
	@Id
	@Column(name = "code", length = 22)
	private String code;
	// 小区id
	@Column(name = "tiny_village_id")
	private Long tiny_village_id;
	// 小区名字
	@Column(name = "tiny_village_name", length = 255)
	private String tiny_village_name;
	/**
	 * 创建者id
	 */
	@Column(name = "create_user_id")
	private Long create_user_id;
	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date create_time;

	/**
	 * 修改者id
	 */
	@Column(name = "update_user_id")
	private Long update_user_id;
	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Date update_time;
	// 已消费用户数2017年以前的包含2017年
	@Column(name = "consumer_number", length = 255)
	private String consumer_number;
	// 已消费用户数2018年数据
	@Column(name = "consumer_number_2018", length = 255)
	private String consumer_number_after;

	public String getConsumer_number_after() {
		return consumer_number_after;
	}

	public void setConsumer_number_after(String consumer_number_after) {
		this.consumer_number_after = consumer_number_after;
	}

	public String getConsumer_number() {
		return consumer_number;
	}

	public void setConsumer_number(String consumer_number) {
		this.consumer_number = consumer_number;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getTiny_village_id() {
		return tiny_village_id;
	}

	public void setTiny_village_id(Long tiny_village_id) {
		this.tiny_village_id = tiny_village_id;
	}

	public String getTiny_village_name() {
		return tiny_village_name;
	}

	public void setTiny_village_name(String tiny_village_name) {
		this.tiny_village_name = tiny_village_name;
	}

	public Long getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(Long create_user_id) {
		this.create_user_id = create_user_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Long getUpdate_user_id() {
		return update_user_id;
	}

	public void setUpdate_user_id(Long update_user_id) {
		this.update_user_id = update_user_id;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

}
