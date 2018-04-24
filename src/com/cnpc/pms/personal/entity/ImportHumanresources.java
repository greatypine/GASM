package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_import_humanresources")
public class ImportHumanresources extends DataEntity{
	
	/**
	 * 部门
	 */
	@Column(length = 45, name = "deptname")
	private String deptname;
	
	/**
	 * 分序
	 */
	@Column(name = "deptno")
	private Double deptno;
	
	/**
	 * 员工编号
	 */
	@Column(length = 45, name = "employee_no")
	private String employee_no;
	/**
	 * 职级/岗位
	 */
	@Column(length = 45, name = "zw")
	private String zw;
	/**
	 * 姓名
	 */
	@Column(length = 45, name = "name")
	private String name;
	
	/**
	 * 性别
	 */
	@Column(length = 45, name = "sex")
	private String sex;
	
	/**
	 * 民族
	 */
	@Column(length = 45, name = "nation")
	private String nation;
	
	/**
	 * 籍贯
	 */
	@Column(length = 45, name = "nativeplace")
	private String nativeplace;
	
	/**
	 * 户籍
	 */
	@Column(length = 45, name = "censusregister")
	private String censusregister;
	
	/**
	 * 出生年月
	 */
	@Column(length = 45, name = "birthday")
	private String birthday;
	
	/**
	 * 学历
	 */
	@Column(length = 45, name = "education")
	private String education;
	
	/**
	 * 学位
	 */
	@Column(length = 45, name = "educationlevel")
	private String educationlevel;
	
	/**
	 * 毕业学校
	 */
	@Column(length = 65, name = "school")
	private String school;
	
	/**
	 * 专业
	 */
	@Column(length = 65, name = "profession")
	private String profession;
	
	/**
	 * 职称
	 */
	@Column(length = 65, name = "professiontitle")
	private String professiontitle;
	
	/**
	 * 职业   资格
	 */
	@Column(length = 65, name = "credentials")
	private String credentials;
	
	/**
	 * 婚姻状况
	 */
	@Column(length = 65, name = "marriage")
	private String marriage;
	
	/**
	 * 党派
	 */
	@Column(length = 65, name = "partisan")
	private String partisan;
	
	/**
	 * 入党时间
	 */
	@Column(length = 65, name = "partisandate")
	private String partisandate;
	
	/**
	 * 参加工作时间
	 */
	@Column(length = 65, name = "workdate")
	private String workdate;
	
	
	/**
	 * 进中信 时间
	 */
	@Column(length = 65, name = "entrydate")
	private String entrydate;
	
	
	/**
	 * 身份证号
	 */
	@Column(length = 65, name = "cardnumber")
	private String cardnumber;
	
	/**
	 * 到岗日
	 */
	@Column(length = 65, name = "topostdate")
	private String topostdate;
	
	/**
	 * 劳动合同期限
	 */
	@Column(length = 65, name = "contractdate")
	private String contractdate;
	
	/**
	 * 试用期
	 */
	@Column(length = 65, name = "trydate")
	private String trydate;
	
	/**
	 * 签订次数
	 */
	@Column(length = 65, name = "signcount")
	private String signcount;
	
	/**
	 * 是否转正
	 */
	@Column(length = 65, name = "isofficial")
	private String isofficial;
	
	
	/**
	 * 补充医保
	 */
	@Column(length = 65, name = "tomedical")
	private String tomedical;
	
	/**
	 * 联系方式
	 */
	@Column(length = 65, name = "phone")
	private String phone;
	
	/**
	 * 紧急联络人姓名
	 */
	@Column(length = 65, name = "relationname")
	private String relationname;
	
	/**
	 * 电话
	 */
	@Column(length = 65, name = "tel")
	private String tel;
	
	/**
	 * 与本人关系
	 */
	@Column(length = 65, name = "relationtype")
	private String relationtype;
	
	/**
	 * 备  注
	 */
	@Column(length = 255, name = "remark")
	private String remark;
	
	/**
	 * 是否签订劳动合同
	 */
	@Column(length = 65, name = "issigncontract")
	private String issigncontract;
	
	/**
	 * 身份证地址
	 */
	@Column(length = 255, name = "cardaddress")
	private String cardaddress;
	
	/**
	 * 导入状态0，导入 1，确定同步到分配门店表中
	 */
	@Column(name = "importstatus")
	private Integer importstatus;
	

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getNativeplace() {
		return nativeplace;
	}

	public void setNativeplace(String nativeplace) {
		this.nativeplace = nativeplace;
	}

	public String getCensusregister() {
		return censusregister;
	}

	public void setCensusregister(String censusregister) {
		this.censusregister = censusregister;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getEducationlevel() {
		return educationlevel;
	}

	public void setEducationlevel(String educationlevel) {
		this.educationlevel = educationlevel;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getProfessiontitle() {
		return professiontitle;
	}

	public void setProfessiontitle(String professiontitle) {
		this.professiontitle = professiontitle;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public String getPartisan() {
		return partisan;
	}

	public void setPartisan(String partisan) {
		this.partisan = partisan;
	}

	public String getPartisandate() {
		return partisandate;
	}

	public void setPartisandate(String partisandate) {
		this.partisandate = partisandate;
	}

	public String getWorkdate() {
		return workdate;
	}

	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}

	public String getEntrydate() {
		return entrydate;
	}

	public void setEntrydate(String entrydate) {
		this.entrydate = entrydate;
	}

	public String getCardnumber() {
		return cardnumber;
	}

	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}

	public String getTopostdate() {
		return topostdate;
	}

	public void setTopostdate(String topostdate) {
		this.topostdate = topostdate;
	}

	public String getContractdate() {
		return contractdate;
	}

	public void setContractdate(String contractdate) {
		this.contractdate = contractdate;
	}

	public String getTrydate() {
		return trydate;
	}

	public void setTrydate(String trydate) {
		this.trydate = trydate;
	}

	public String getSigncount() {
		return signcount;
	}

	public void setSigncount(String signcount) {
		this.signcount = signcount;
	}

	public String getIsofficial() {
		return isofficial;
	}

	public void setIsofficial(String isofficial) {
		this.isofficial = isofficial;
	}

	public String getTomedical() {
		return tomedical;
	}

	public void setTomedical(String tomedical) {
		this.tomedical = tomedical;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRelationname() {
		return relationname;
	}

	public void setRelationname(String relationname) {
		this.relationname = relationname;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getRelationtype() {
		return relationtype;
	}

	public void setRelationtype(String relationtype) {
		this.relationtype = relationtype;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIssigncontract() {
		return issigncontract;
	}

	public void setIssigncontract(String issigncontract) {
		this.issigncontract = issigncontract;
	}

	public String getCardaddress() {
		return cardaddress;
	}

	public void setCardaddress(String cardaddress) {
		this.cardaddress = cardaddress;
	}

	public Integer getImportstatus() {
		return importstatus;
	}

	public void setImportstatus(Integer importstatus) {
		this.importstatus = importstatus;
	}

	public Double getDeptno() {
		return deptno;
	}

	public void setDeptno(Double deptno) {
		this.deptno = deptno;
	}
	
	
	
}
