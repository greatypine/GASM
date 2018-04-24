package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 房屋客户关联表
 * Created by liuxiao on 2016/8/8 0008.
 */
@Entity
@Table(name="t_house_customer")
public class HouseCustomer extends DataEntity {

    @Column(name = "house_id")
    private Long house_id;

    @Column(name = "customer_id")
    private Long customer_id;

    @Column(name = "one_pay")
    private Integer one_pay = 0;
    //第一等级支付时间
    @Column(name = "one_date")
    private Date one_date;
    //第二等级支付
    @Column(name = "six_pay")
    private Integer six_pay = 0;
    //第二等级支付时间
    @Column(name = "six_date")
    private Date six_date;
   //第三等级支付状态 0:无 1:已支付
    @Column(name="third_grade_pay")
    private Integer third_grade_pay=0;
   //第三等级支付时间
    @Column(name="third_grade_date")
    private Date third_grade_date;
   
    public Long getHouse_id() {
        return house_id;
    }

    public void setHouse_id(Long house_id) {
        this.house_id = house_id;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public Integer getOne_pay() {
        return one_pay;
    }

    public void setOne_pay(Integer one_pay) {
        this.one_pay = one_pay;
    }

    public Integer getSix_pay() {
        return six_pay;
    }

    public void setSix_pay(Integer six_pay) {
        this.six_pay = six_pay;
    }

    public Date getOne_date() {
        return one_date;
    }

    public void setOne_date(Date one_date) {
        this.one_date = one_date;
    }

    public Date getSix_date() {
        return six_date;
    }

    public void setSix_date(Date six_date) {
        this.six_date = six_date;
    }

	public Integer getThird_grade_pay() {
		return third_grade_pay;
	}

	public void setThird_grade_pay(Integer third_grade_pay) {
		this.third_grade_pay = third_grade_pay;
	}

	public Date getThird_grade_date() {
		return third_grade_date;
	}

	public void setThird_grade_date(Date third_grade_date) {
		this.third_grade_date = third_grade_date;
	}

	
    
    
}
