package com.cnpc.pms.base.entity;

import org.springframework.stereotype.Component;

/**
 * Created by cyh(275923233@qq.com) on 2015/9/14.
 */
@Component
public class CommunityInfo {
    private int id; //编号
    private String cname; //社区名称
    private String  scname; //小区名称
    private double totalfloor; //楼栋数
    private double totalfamily; //户数
    private double avgprice; //销售均价
    private double rent; //租金

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

    public String getScname() {
        return scname;
    }

    public void setScname(String scname) {
        this.scname = scname;
    }

    public double getTotalfloor() {
        return totalfloor;
    }

    public void setTotalfloor(double totalfloor) {
        this.totalfloor = totalfloor;
    }

    public double getTotalfamily() {
        return totalfamily;
    }

    public void setTotalfamily(double totalfamily) {
        this.totalfamily = totalfamily;
    }

    public double getAvgprice() {
        return avgprice;
    }

    public void setAvgprice(double avgprice) {
        this.avgprice = avgprice;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }
}
