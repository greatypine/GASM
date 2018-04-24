package com.cnpc.pms.personal.entity;


import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.entity.PMSEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_relation_content")
public class RelationContent extends PMSEntity {

	@Column(name = "relation_id")
	private Long relation_id;

	@Column(name = "content_code",length = 45)
	private String content_code;

	@Column(name = "option_code",length = 45)
	private String option_code;

	@Column(name = "content")
	private String content;

	public Long getRelation_id() {
		return relation_id;
	}

	public void setRelation_id(Long relation_id) {
		this.relation_id = relation_id;
	}

	public String getContent_code() {
		return content_code;
	}

	public void setContent_code(String content_code) {
		this.content_code = content_code;
	}

	public String getOption_code() {
		return option_code;
	}

	public void setOption_code(String option_code) {
		this.option_code = option_code;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
