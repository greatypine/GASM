package com.cnpc.pms.activity.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * 活动反馈
 * Created by litianyu on 2017/10/17.
 */
public class ActivityFeedBack extends DataEntity {

    @Id
    @GeneratedValue
    private Long id;

    /**城市id*/
    @Column(name="cityId")
    private String cityId;

    /**活动策划方案主键id*/
    @Column(name = "activityPlanScheme_Id")
    private String activityPlanScheme_Id;

    /**门店id*/

    @Column(name="storeId")
    private String storeId;


    //活动列表主键id
    @Column(name="activiyId")
    private Long activiyId;

    /**活动对象（预估人数）与活动策划方案保持一直*/
    @Column(name="activeobjects_forecastPeople")
    private String activeobjects_forecastPeople;

    /**活动的预估人数*/
    @Column(name="preestimate_People")
    private String preestimate_People;

    /**实际参与人数*/
    @Column(name="practicalParticipation_People")
    private String practicalParticipation_People;


    /**达到参与预估人数原因分析*/
    @Column(name="achievePreestimatePeop_analysis")
    private String achievePreestimatePeop_analysis;



    /**下一步提升办法(从方案、前期预热、现场执行及服务等各方面)*/
    @Column(name="nextStepPromotion_way")
    private String nextStepPromotion_way;


    /**活动新增用户数(预估值)*/
    @Column(name="newAddUser_preestimateValue")
    private String newAddUser_preestimateValue;


    /**活动新增用户数(实际值)*/
    @Column(name="newAddUser_actualValue")
    private String newAddUser_actualValue;


    /**活动实际GMV*/
    @Column(name="activity_actualGMV")
    private String activity_actualGMV;


    /**活动开场照片*/
    @Column(name="activity_openingPhotos")
    private String activity_openingPhotos;






}
