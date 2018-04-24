package com.cnpc.pms.activity.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.*;

/**
 *
 * 活动审批
 * Created by litianyu on 2017/10/17.
 */
@Entity
@Table(name="t_store_activityexamination")
public class ActivityExamination extends DataEntity {


    @Id
    @GeneratedValue
    private Long id;

    /**城市Id*/
    @Column(name="cityId")
    private Long cityId;

    /**门店Id*/
    @Column(name="storeId")
    private Long storeId;

    //活动列表主键id
    @Column(name="activiyId")
    private Long activiyId;

    /**活动最终方案*/
    @Column(name="activityFinalPlan")
    private String activityFinalPlan;


    /**活动门店资料*/
    @Column(name="activityStoreInfo")
    private String activityStore_Info;


    /**城市公司活动管理岗的活动反馈*/
    @Column(name="activityFeedback")
    private String activityFeedback;

    /**活动管理岗打分部分*/
    @Column(name="activityManagmentWork_score")
    private String activityManagmentWork_score;


    /**店长得分情况*/
    @Column(name="storemanager_score")
    private String storemanager_score;


    /**国安侠得分情况*/
    @Column(name="gax_score")
    private String gax_score;


    /**服务专员得分情况*/
    @Column(name="serviceommissioner_score")
    private String serviceommissioner_score;

    /**创建日期*/
    @Column(name="createTime")
    private String createTime;


    //getter and setter ......

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public void setActivityFinalPlan(String activityFinalPlan) {
        this.activityFinalPlan = activityFinalPlan;
    }

    public void setActivityStore_Info(String activityStore_Info) {
        this.activityStore_Info = activityStore_Info;
    }

    public void setActivityFeedback(String activityFeedback) {
        this.activityFeedback = activityFeedback;
    }

    public void setActivityManagmentWork_score(String activityManagmentWork_score) {
        this.activityManagmentWork_score = activityManagmentWork_score;
    }

    public void setStoremanager_score(String storemanager_score) {
        this.storemanager_score = storemanager_score;
    }

    public void setGax_score(String gax_score) {
        this.gax_score = gax_score;
    }

    public void setServiceommissioner_score(String serviceommissioner_score) {
        this.serviceommissioner_score = serviceommissioner_score;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Long getCityId() {
        return cityId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public String getActivityFinalPlan() {
        return activityFinalPlan;
    }

    public String getActivityStore_Info() {
        return activityStore_Info;
    }

    public String getActivityFeedback() {
        return activityFeedback;
    }

    public String getActivityManagmentWork_score() {
        return activityManagmentWork_score;
    }

    public String getStoremanager_score() {
        return storemanager_score;
    }

    public String getGax_score() {
        return gax_score;
    }

    public String getServiceommissioner_score() {
        return serviceommissioner_score;
    }

    public String getCreateTime() {
        return createTime;
    }
}
