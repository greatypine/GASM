package com.cnpc.pms.toolkit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.AlternativeDS;

/**
 * 能用顺序号（流水号）实体
 * 
 * @author GuoJian
 * @since 2013-7-17
 */
@Entity
@Table(name = "td_general_seq")

public class GeneralSequenceNo {

	@Id
	@Column(name = "seqKey", length = 60, nullable = false)
	private String seqKey;

	@Column(name = "seqVal", nullable = false)
	private Long seqVal;

	@Column(name = "note")
	private String note;

	public String getSeqKey() {
		return seqKey;
	}

	public void setSeqKey(String seqKey) {
		this.seqKey = seqKey;
	}

	public Long getSeqVal() {
		return seqVal;
	}

	public void setSeqVal(Long seqVal) {
		this.seqVal = seqVal;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
