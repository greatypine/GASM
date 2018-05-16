package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;

@Entity
@Table(name = "t_notice")
public class MsgNotice extends DataEntity{
	private static final long serialVersionUID = 1L;

	@Column(name = "title",length=255)
	private String title;//标题
	
	@Column(name = "content",length=2000)
	private String content;//内容
	
	@Column(name="noticeNo",length=20)
	private String noticeNo;//编号
	
	@Column(name="releaseUnit")
	private String releaseUnit;//发布单位
	
	@Column(name="type",length=1)
	private Integer type;//公告类型 1：事务 2：业务
	
	@Column(name="grade",length=1)
	private Integer grade;//公告等级  通知方式高级 1：国安数据APP 和短信通知 中级2：国安APP通知  低级3：不通知
	
	@Column(name="cityes")
	private String cityes;
	
	@Column(name="stores")
	private String stores;
	
	@Column(name="zw")
	private String zw;
	
	@Column(name="filePath",length=200)
	private String filePath;//附件路径
	
	@Column(name="fileName",length=50)
	private String fileName;
	
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

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getReleaseUnit() {
		return releaseUnit;
	}

	public void setReleaseUnit(String releaseUnit) {
		this.releaseUnit = releaseUnit;
	}

	public String getCityes() {
		return cityes;
	}

	public void setCityes(String cityes) {
		this.cityes = cityes;
	}

	public String getStores() {
		return stores;
	}

	public void setStores(String stores) {
		this.stores = stores;
	}

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
