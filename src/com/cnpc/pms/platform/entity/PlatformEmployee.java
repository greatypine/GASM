package com.cnpc.pms.platform.entity;


import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.OptLockEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 *
 * Created by liuxi on 2016/12/19.
 */
@Entity
@Table(name = "t_employee")
@AlternativeDS(source = "APP")
public class PlatformEmployee extends OptLockEntity {
    /**
     * 主键
     */
    @Id
    @Column(name="id",length=32)
    private String id;

    /**
     * 姓名
     */
    @Column(name="name",length=36)
    private String name;

    /**
     * 性别   (male)男，(female)女，(secrecy)保密
     */
    @Column(name="sex")
    private String sex;

    /**
     * 身份证号
     */
    @Column(name="idcard_no",length=18)
    private String idcard_no;

    /**
     * 主管经理id
     */
    @Column(name="manager_id",length=32)
    private String manager_id;

    /**
     * 门店id
     */
    @Column(name="store_id",length=32)
    private String store_id;

    /**
     * 手机
     */
    @Column(name="mobilephone",length=11)
    private String mobilephone;

    /**
     * 生日
     */
    @Column(name="birthday")
    private Date birthday;

    /**
     * 头像
     */
    @Column(name="avatar",length=1000)
    private String avatar;

    /**
     * 登录名
     */
    @Column(name="login_name",length=36)
    private String login_name;

    /**
     * 密码
     */
    @Column(name="password",length=64)
    private String password;

    /**
     * 邮箱
     */
    @Column(name="email",length=64)
    private String email;

    /**
     * 座机电话
     */
    @Column(name="tel",length=36)
    private String tel;

    /**
     * 排序号
     */
    @Column(name="sort")
    private Integer sort;

    /**
     * 在线状态 (offline:离线，online:在线)
     */
    @Column(name="online",length=10)
    private String online;

    /**
     * 备注
     */
    @Column(name="remark",length=1000)
    private String remark;

    /**
     * 400登录名
     */
    @Column(name="callcenter_login_name",length=100)
    private String callcenter_login_name;

    /**
     * 400登录密码
     */
    @Column(name="callcenter_password",length=100)
    private String callcenter_password;

    /**
     * token
     */
    @Column(name="app_token",length=50)
    private String app_token;

    /**
     * 状态标志位
     */
    @Column(name="status")
    private Integer status;

    /**
     * 创建人ID
     */
    @Column(name="create_user_id",length=32)
    private String create_user_id;

    /**
     * 创建者
     */
    @Column(name="create_user",length=36)
    private String create_user;

    /**
     * 创建时间
     */
    @Column(name="create_time")
    private Date create_time;

    /**
     * 更新人ID
     */
    @Column(name="update_user_id",length=32)
    private String update_user_id;

    /**
     * 更新用户
     */
    @Column(name="update_user",length=36)
    private String update_user;

    /**
     * 更新时间
     */
    @Column(name="update_time")
    private Date update_time;

    /**
     * 国安侠标签
     */
    @Column(name="tag",length=1000)
    private String tag;

    /**
     * 经度
     */
    @Column(name="latitude")
    private Double latitude;

    /**
     * 纬度
     */
    @Column(name="longitude")
    private Double longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIdcard_no() {
        return idcard_no;
    }

    public void setIdcard_no(String idcard_no) {
        this.idcard_no = idcard_no;
    }

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCallcenter_login_name() {
        return callcenter_login_name;
    }

    public void setCallcenter_login_name(String callcenter_login_name) {
        this.callcenter_login_name = callcenter_login_name;
    }

    public String getCallcenter_password() {
        return callcenter_password;
    }

    public void setCallcenter_password(String callcenter_password) {
        this.callcenter_password = callcenter_password;
    }

    public String getApp_token() {
        return app_token;
    }

    public void setApp_token(String app_token) {
        this.app_token = app_token;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_user_id() {
        return update_user_id;
    }

    public void setUpdate_user_id(String update_user_id) {
        this.update_user_id = update_user_id;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
