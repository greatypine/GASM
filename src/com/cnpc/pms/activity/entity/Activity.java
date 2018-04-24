package com.cnpc.pms.activity.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.*;

/**
 *
 * 活动策划方案
 * Created by litianyu on 2017/10/12.
 */
@Entity
@Table(name="t_store_activity")
public class Activity extends DataEntity {

    @Id
    @GeneratedValue
    private Long id;

    /**活动名称*/
    @Column(name="activity_name")
    private String activity_name;

    /**门店名称*/
    @Column(name="store_name")
    private String store_name;

    /**活动状态 0.已发起 1.*/
    @Column(name="activity_status")
    private String activity_status;

    /**活动发起人**/
    @Column(name="activity_initiator")
    private String activity_initiator;

    /**活动时间*/
    @Column(name="activity_time")
    private String activity_time;


    /**活动使用范围*/
    @Column(name="scope_ofapplication")
    private String scope_ofapplication;


    /**活动手册*/
    @Column(name="activity_book")
    private String activity_book;

    /**城市id*/
    @Column(name="cityname")
    private String cityname;

    /**门店id*/
    @Column(name="storeid")
    private Long storeid;




    public void setId(Long id) {
        this.id = id;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public void setActivity_status(String activity_status) {
        this.activity_status = activity_status;
    }

    public void setActivity_initiator(String activity_initiator) {
        this.activity_initiator = activity_initiator;
    }

    public void setActivity_time(String activity_time) {
        this.activity_time = activity_time;
    }

    public void setScope_ofapplication(String scope_ofapplication) {
        this.scope_ofapplication = scope_ofapplication;
    }

    public void setActivity_book(String activity_book) {
        this.activity_book = activity_book;
    }

    public void setStoreid(Long storeid) {
        this.storeid = storeid;
    }

    public Long getId() {
        return id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public String getStore_name() {
        return store_name;
    }

    public String getActivity_status() {
        return activity_status;
    }

    public String getActivity_initiator() {
        return activity_initiator;
    }

    public String getActivity_time() {
        return activity_time;
    }

    public String getScope_ofapplication() {
        return scope_ofapplication;
    }

    public String getActivity_book() {
        return activity_book;
    }

    public Long getStoreid() {
        return storeid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }
}
