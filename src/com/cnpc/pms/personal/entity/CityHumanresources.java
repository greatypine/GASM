package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_city_humanresources")
public class CityHumanresources extends DataEntity{
	
	/*//总数
	@Column(length = 45, name = "totalsum")
	private String totalsum;*/
	
	/**
	 * 城市公司名称
	 */
	@Column(length = 45, name = "citycompany")
	private String citycompany;
	
	/**
	 * 部门
	 */
	@Column(length = 45, name = "deptname")
	private String deptname;
	
	/**
	 * 版块
	 */
	@Column(length = 65, name = "deptlevel1")
	private String deptlevel1;
	
	/**
	 * 职级/岗位
	 */
	@Column(length = 45, name = "zw")
	private String zw;
	
	/**
	 * 职级
	 */
	@Column(length = 65, name = "professnallevel")
	private String professnallevel;
	
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
	 * 户口性质
	 */
	@Column(length = 45, name = "censusregistertype")
	private String censusregistertype;
	
	/**
	 * 出生年月
	 */
	@Column(length = 45, name = "birthday")
	private String birthday;
	
	/**
	 * 年龄
	 */
	@Column(length = 45, name = "age")
	private String age;
	
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
	 * 政治面貌
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
	 * 劳动合同签订主体
	 */
	@Column(length = 65, name = "contractcompany")
	private String contractcompany;
	
	/**
	 * 劳动合类型
	 */
	@Column(length = 65, name = "contracttype")
	private String contracttype;
	
	
	/**
	 * 合同开始时间
	 */
	@Column(length = 65, name = "contractdatestart")
	private String contractdatestart;
	/**
	 * 合同结束时间
	 */
	@Column(length = 65, name = "contractdateend")
	private String contractdateend;
	
	/**
	 * 提醒
	 */
	@Column(length = 65, name = "tipmessage")
	private String tipmessage;
	
	
	
	/**
	 * 签订次数
	 */
	@Column(length = 65, name = "signcount")
	private String signcount;
	
	/**
	 * 试用期开始
	 */
	@Column(length = 65, name = "trydatestart")
	private String trydatestart;
	
	/**
	 * 试用期结束
	 */
	@Column(length = 65, name = "trydateend")
	private String trydateend;
	
	/**
	 * 联系方式
	 */
	@Column(length = 65, name = "phone")
	private String phone;
	
	
	/**
	 *发薪主体 
	 */
	@Column(length = 65, name = "paycompany")
	private String paycompany;
	
	
	/**
	 * 备  注
	 */
	@Column(length = 255, name = "remark")
	private String remark;
	
	
	/**
	 * 情况说明
	 */
	@Column(length = 255, name = "other")
	private String other;
	
	
	
	/**
	 * 家庭通信地址
	 */
	@Column(length = 255, name = "houseaddress")
	private String houseaddress;
	
	/**
	 * 户籍所在地
	 */
	@Column(length = 255, name = "cardaddress")
	private String cardaddress;
	
	/**
	 * 试用期提醒
	 */
	@Column(length = 255, name = "trytipmessage")
	private String trytipmessage;
	
	/////////////////////excel表格中的数据 结束///////////////////////////
	
	
	////////////////////社保报表开始////////////////////////////
	/**
	 * 子女情况
	 */
	@Column(length = 65, name = "children")
	private String children;
	
	/**
	 * 子女姓名
	 */
	@Column(length = 65, name = "childrenname")
	private String childrenname;
	
	/**
	 * 子/女
	 */
	@Column(length = 65, name = "childrensex")
	private String childrensex;
	
	/**
	 * 	出生年月
	 */
	@Column(length = 65, name = "childrenbirthday")
	private String childrenbirthday;
	
	/**
	 * 身份证号
	 */
	@Column(length = 65, name = "childrencardnumber")
	private String childrencardnumber;
	
	/**
	 *银行卡号
	 */
	@Column(length = 65, name = "bankcardnumber")
	private String bankcardnumber;
	
	
	/**
	 *开户行
	 */
	@Column(length = 65, name = "bankname")
	private String bankname;
	
	
	/**
	 * 社保、工资备注
	 */
	@Column(length = 255, name = "secremark")
	private String secremark;
	
    ////////////////////社保报表结束////////////////////////////
	
	
	
	//////////////////////系统分配////////////////////////
	
	/**
	 * 员工编号
	 */
	@Column(length = 45, name = "employee_no")
	private String employee_no;
	
	/**
	 * 状态｛0，未分配门店  1入职  2离职｝
	 */
	@Column(name="humanstatus")
	private Long humanstatus;
	

	/**
	 * 城市 
	 */
	@Column(length = 65,name="citySelect")
	private String citySelect;
	
	
	/**
	 * 编辑按钮 
	 */
	@Transient
	public String editdelete;
	
	////////////////////////入、离职////////////////////////
	/**
	 * 离职原因
	 */
	@Column(length = 255,name="leavereason")
	private String leavereason;
	
	/**
	 * 离职时间
	 */
	@Column(length = 65,name="leavedate")
	private String leavedate;
	
	
	
	
	
	

	public String getCitycompany() {
		return citycompany;
	}

	public void setCitycompany(String citycompany) {
		this.citycompany = citycompany;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getDeptlevel1() {
		return deptlevel1;
	}

	public void setDeptlevel1(String deptlevel1) {
		this.deptlevel1 = deptlevel1;
	}

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	public String getProfessnallevel() {
		return professnallevel;
	}

	public void setProfessnallevel(String professnallevel) {
		this.professnallevel = professnallevel;
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

	public String getCensusregistertype() {
		return censusregistertype;
	}

	public void setCensusregistertype(String censusregistertype) {
		this.censusregistertype = censusregistertype;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
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

	public String getContractcompany() {
		return contractcompany;
	}

	public void setContractcompany(String contractcompany) {
		this.contractcompany = contractcompany;
	}

	public String getContracttype() {
		return contracttype;
	}

	public void setContracttype(String contracttype) {
		this.contracttype = contracttype;
	}

	public String getContractdatestart() {
		return contractdatestart;
	}

	public void setContractdatestart(String contractdatestart) {
		this.contractdatestart = contractdatestart;
	}

	public String getContractdateend() {
		return contractdateend;
	}

	public void setContractdateend(String contractdateend) {
		this.contractdateend = contractdateend;
	}

	public String getTipmessage() {
		return tipmessage;
	}

	public void setTipmessage(String tipmessage) {
		this.tipmessage = tipmessage;
	}

	public String getSigncount() {
		return signcount;
	}

	public void setSigncount(String signcount) {
		this.signcount = signcount;
	}

	public String getTrydatestart() {
		return trydatestart;
	}

	public void setTrydatestart(String trydatestart) {
		this.trydatestart = trydatestart;
	}

	public String getTrydateend() {
		return trydateend;
	}

	public void setTrydateend(String trydateend) {
		this.trydateend = trydateend;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPaycompany() {
		return paycompany;
	}

	public void setPaycompany(String paycompany) {
		this.paycompany = paycompany;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getHouseaddress() {
		return houseaddress;
	}

	public void setHouseaddress(String houseaddress) {
		this.houseaddress = houseaddress;
	}

	public String getCardaddress() {
		return cardaddress;
	}

	public void setCardaddress(String cardaddress) {
		this.cardaddress = cardaddress;
	}

	public String getTrytipmessage() {
		return trytipmessage;
	}

	public void setTrytipmessage(String trytipmessage) {
		this.trytipmessage = trytipmessage;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public String getChildrenname() {
		return childrenname;
	}

	public void setChildrenname(String childrenname) {
		this.childrenname = childrenname;
	}

	public String getChildrensex() {
		return childrensex;
	}

	public void setChildrensex(String childrensex) {
		this.childrensex = childrensex;
	}

	public String getChildrenbirthday() {
		return childrenbirthday;
	}

	public void setChildrenbirthday(String childrenbirthday) {
		this.childrenbirthday = childrenbirthday;
	}

	public String getChildrencardnumber() {
		return childrencardnumber;
	}

	public void setChildrencardnumber(String childrencardnumber) {
		this.childrencardnumber = childrencardnumber;
	}

	public String getBankcardnumber() {
		return bankcardnumber;
	}

	public void setBankcardnumber(String bankcardnumber) {
		this.bankcardnumber = bankcardnumber;
	}

	public String getSecremark() {
		return secremark;
	}

	public void setSecremark(String secremark) {
		this.secremark = secremark;
	}

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	public Long getHumanstatus() {
		return humanstatus;
	}

	public void setHumanstatus(Long humanstatus) {
		this.humanstatus = humanstatus;
	}

	public String getCitySelect() {
		return citySelect;
	}

	public void setCitySelect(String citySelect) {
		this.citySelect = citySelect;
	}

	public String getLeavereason() {
		return leavereason;
	}

	public void setLeavereason(String leavereason) {
		this.leavereason = leavereason;
	}

	public String getLeavedate() {
		return leavedate;
	}

	public void setLeavedate(String leavedate) {
		this.leavedate = leavedate;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	/*public String getTotalsum() {
		return totalsum;
	}

	public void setTotalsum(String totalsum) {
		this.totalsum = totalsum;
	}*/

	public String getEditdelete() {
		return editdelete;
	}

	public void setEditdelete(String editdelete) {
		this.editdelete = editdelete;
	}
	
	
	
	
}
