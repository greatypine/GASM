package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 家庭成员
 * Created by liu on 2016/7/21 0021.
 */
@Entity
@Table(name="t_family_temporary")
public class FamilyTemporary extends DataEntity {

    @Column(name="customer_temporary_id")
    private Long customer_temporary_id;

    @Column(name="customer_id")
    private Long customer_id;

    /**
     * 家庭成员关系
     */
    @Column(length = 45, name = "family_relation")
    private String family_relation;

    /**
     * 家庭成员姓名
     */
    @Column(length = 45, name = "family_name")
    private String family_name;

    /**
     * 家庭成员 电话
     */
    @Column(length = 45, name = "family_phone")
    private String family_phone;

    /**
     * 家庭成员年龄
     */
    @Column(name = "family_age")
    private Integer family_age;

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public String getFamily_relation() {
        return family_relation;
    }

    public void setFamily_relation(String family_relation) {
        this.family_relation = family_relation;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getFamily_phone() {
        return family_phone;
    }

    public void setFamily_phone(String family_phone) {
        this.family_phone = family_phone;
    }

    public Integer getFamily_age() {
        return family_age;
    }

    public void setFamily_age(Integer family_age) {
        this.family_age = family_age;
    }

    public Long getCustomer_temporary_id() {
        return customer_temporary_id;
    }

    public void setCustomer_temporary_id(Long customer_temporary_id) {
        this.customer_temporary_id = customer_temporary_id;
    }
}
