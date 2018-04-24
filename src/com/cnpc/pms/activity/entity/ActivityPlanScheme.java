package com.cnpc.pms.activity.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.*;
import java.io.File;

/**
 * 活动策划方案
 * Created by litianyu on 2017/10/16.
 */
@Entity
@Table(name="t_store_activityPlanScheme")
public class ActivityPlanScheme extends DataEntity {

    @Id
    @GeneratedValue
    private Long id;

    //活动列表主键id
    @Column(name="activiyId")
    private Long activiyId;

    /**活动背景(为什么做这个活动？有何依据?)*/
    @Column(name="activity_background")
    private String activity_background;

    /**活动主题（或名称）*/
    @Column(name="activity_topic")
    private String activity_topic;


    /**活动内容（简单概括）*/
    @Column(name="activity_content")
    private File activity_content;

    /**活动类型(大类加小类)*/
    @Column(name="activity_type")
    private String activity_type;


    /**活动招商点位介绍 (1)商家类别*/
    @Column(name="business_category")
    private String business_category;

    /**活动招商点位介绍 (2)合作模式*/
    @Column(name="cooperative_mode")
    private String cooperative_mode;


    /**活动招商点位介绍 (3)已确定合作商家*/
    @Column(name="determineCooperation_merchant")
    private File determineCooperation_merchant;


    /**活动招商点位介绍 (3)已确定合作商家*/
    @Column(name="determineCooperation_merchant_file")
    private File determineCooperation_merchant_file;


    /**活动目标（活动想达到什么目标？写明预估新增用户量、下单量、销售额）*/
    @Column(name="activity_target")
    private String activity_target;

    /**活动目标(1)新增用户量*/
    @Column(name="activitytarget_newAddusers")
    private String activitytarget_newAddusers;


    /**活动目标(2)订单量*/
    @Column(name="activitytarget_orderquantity")
    private String activitytarget_orderquantity;


    /**活动目标(2)GMV*/
    @Column(name="activitytarget_GMV")
    private String activitytarget_GMV;


    /**活动对象（预估人数）(1)活动对象人群*/
    @Column(name="activeobjects_crowd")
    private String activeobjects_crowd;


    /**活动对象（预估人数）(1)活动对象人群*/
    @Column(name="activeobjects_forecastPeople")
    private String activeobjects_forecastPeople;


    /**活动时间*/
    @Column(name="activity_time")
    private String activity_time;


    /**活动地点*/
    @Column(name="activity_site")
    private String activity_site;


    /**活动预算（需有预算明细）(1)总金额*/
    @Column(name="activity_totalamount")
    private String activity_totalamount;


    /**活动预算（需有预算明细）(1)预算明细*/
    @Column(name="budget_detail")
    private String budget_detail;


    /**突发情况解决（可能的突发情况是什么？解决方案是什么？）*/
    @Column(name="emergency")
    private String emergency;

    /**活动发起人*/
    @Transient
    private String activity_user;


    /**门店主键*/
    @Transient
    private Long storeId;


    @Transient
    private String storeName;

    @Transient
    private String cityname;



    //getter and setter ......

    public void setId(Long id) {
        this.id = id;
    }

    public void setActivity_background(String activity_background) {
        this.activity_background = activity_background;
    }

    public void setActivity_topic(String activity_topic) {
        this.activity_topic = activity_topic;
    }

    public void setActivity_content(File activity_content) {
        this.activity_content = activity_content;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public void setBusiness_category(String business_category) {
        this.business_category = business_category;
    }

    public void setCooperative_mode(String cooperative_mode) {
        this.cooperative_mode = cooperative_mode;
    }

    public void setDetermineCooperation_merchant(File determineCooperation_merchant) {
        this.determineCooperation_merchant = determineCooperation_merchant;
    }

    public void setDetermineCooperation_merchant_file(File determineCooperation_merchant_file) {
        this.determineCooperation_merchant_file = determineCooperation_merchant_file;
    }

    public void setActivity_target(String activity_target) {
        this.activity_target = activity_target;
    }

    public void setActivitytarget_newAddusers(String activitytarget_newAddusers) {
        this.activitytarget_newAddusers = activitytarget_newAddusers;
    }

    public void setActivitytarget_orderquantity(String activitytarget_orderquantity) {
        this.activitytarget_orderquantity = activitytarget_orderquantity;
    }

    public void setActivitytarget_GMV(String activitytarget_GMV) {
        this.activitytarget_GMV = activitytarget_GMV;
    }

    public void setActiveobjects_crowd(String activeobjects_crowd) {
        this.activeobjects_crowd = activeobjects_crowd;
    }

    public void setActiveobjects_forecastPeople(String activeobjects_forecastPeople) {
        this.activeobjects_forecastPeople = activeobjects_forecastPeople;
    }

    public void setActivity_time(String activity_time) {
        this.activity_time = activity_time;
    }

    public void setActivity_site(String activity_site) {
        this.activity_site = activity_site;
    }

    public void setActivity_totalamount(String activity_totalamount) {
        this.activity_totalamount = activity_totalamount;
    }

    public void setBudget_detail(String budget_detail) {
        this.budget_detail = budget_detail;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public Long getId() {
        return id;
    }

    public String getActivity_background() {
        return activity_background;
    }

    public String getActivity_topic() {
        return activity_topic;
    }

    public File getActivity_content() {
        return activity_content;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public String getBusiness_category() {
        return business_category;
    }

    public String getCooperative_mode() {
        return cooperative_mode;
    }

    public File getDetermineCooperation_merchant() {
        return determineCooperation_merchant;
    }

    public File getDetermineCooperation_merchant_file() {
        return determineCooperation_merchant_file;
    }

    public String getActivity_target() {
        return activity_target;
    }

    public String getActivitytarget_newAddusers() {
        return activitytarget_newAddusers;
    }

    public String getActivitytarget_orderquantity() {
        return activitytarget_orderquantity;
    }

    public String getActivitytarget_GMV() {
        return activitytarget_GMV;
    }

    public String getActiveobjects_crowd() {
        return activeobjects_crowd;
    }

    public String getActiveobjects_forecastPeople() {
        return activeobjects_forecastPeople;
    }

    public String getActivity_time() {
        return activity_time;
    }

    public String getActivity_site() {
        return activity_site;
    }

    public String getActivity_totalamount() {
        return activity_totalamount;
    }

    public String getBudget_detail() {
        return budget_detail;
    }

    public String getEmergency() {
        return emergency;
    }

    public Long getActiviyId() {
        return activiyId;
    }

    public void setActiviyId(Long activiyId) {
        this.activiyId = activiyId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getActivity_user() {
        return activity_user;
    }

    public void setActivity_user(String activity_user) {
        this.activity_user = activity_user;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }
}
