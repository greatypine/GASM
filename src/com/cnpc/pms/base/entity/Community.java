package com.cnpc.pms.base.entity;

import org.springframework.stereotype.Component;

/**
 * Created by cyh(275923233@qq.com) on 2015/9/10.
 */

@Component
public class Community {

    public Community() {
    }

    private int id; //编号
    private String cname; //社区（街道）名
    private String cclass; //区分
    private String code; //代码
    private double totalfamily; //总户数
    private double totalperson; //常住人口
    private double totalarea; //总面积
    private String introduction; //社区介绍
    private String townid; //区
    private String cityid; //市
    private String provinceid; //省

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCclass() {
        return cclass;
    }

    public void setCclass(String cclass) {
        this.cclass = cclass;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getTotalfamily() {
        return totalfamily;
    }

    public void setTotalfamily(double totalfamily) {
        this.totalfamily = totalfamily;
    }

    public double getTotalperson() {
        return totalperson;
    }

    public void setTotalperson(double totalperson) {
        this.totalperson = totalperson;
    }

    public double getTotalarea() {
        return totalarea;
    }

    public void setTotalarea(double totalarea) {
        this.totalarea = totalarea;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }


    public String getTownid() {
        return townid;
    }

    public void setTownid(String townid) {
        this.townid = townid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
