package com.cnpc.pms.activity.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.*;

/**
 * 预算明细
 * Created by litianyu on 2017/10/16.
 */
@Entity
@Table(name="t_store_activitybudgetdetail")
public class ActivityDudgetDetail extends DataEntity {


    @Id
    @GeneratedValue
    private Long id;

    //活动列表主键id
    @Column(name="activiyId")
    private Long activiyId;


    /**物品名称*/
    @Column(name="item_name")
    private String item_name;


    /**物品数量*/
    @Column(name="item_quantity")
    private String item_quantity;


    /**统计金额*/
    @Column(name="statistical_money")
    private String statistical_money;


    /**物品用途*/
    @Column(name="item_uses")
    private String item_uses;


    //getter and setter ......


    public void setId(Long id) {
        this.id = id;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public void setStatistical_money(String statistical_money) {
        this.statistical_money = statistical_money;
    }

    public void setItem_uses(String item_uses) {
        this.item_uses = item_uses;
    }

    public Long getId() {
        return id;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public String getStatistical_money() {
        return statistical_money;
    }

    public String getItem_uses() {
        return item_uses;
    }


}
