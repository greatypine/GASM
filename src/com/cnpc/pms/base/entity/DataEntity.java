package com.cnpc.pms.base.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 数据采集所有的实体类父类
 * Created by liuxiao on 2016/5/25 0025.
 */
@MappedSuperclass
public class DataEntity extends PMSEntity {

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date create_time;

    /**
     * 更新用户
     */
    @Column(length = 36, name = "update_user")
    private String update_user;

    /**
     * 创建者
     */
    @Column(length = 36, name = "create_user")
    private String create_user;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date update_time;

    @Column(name = "create_user_id")
    private Long create_user_id;

    @Column(name = "update_user_id")
    private Long update_user_id;

    /**
     * 状态标志位
     */
    @Column(name = "status")
    private Integer status = 0;

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
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

    public Long getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(Long create_user_id) {
        this.create_user_id = create_user_id;
    }

    public Long getUpdate_user_id() {
        return update_user_id;
    }

    public void setUpdate_user_id(Long update_user_id) {
        this.update_user_id = update_user_id;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
