package com.cnpc.pms.platform.entity;


import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.OptLockEntity;

import javax.persistence.*;
import java.util.Date;

/**
 *
 * Created by liuxi on 2016/12/19.
 */
@Entity
@Table(name = "t_order")
@AlternativeDS(source = "APP")
public class Order extends OptLockEntity {
    /**
     * 主键
     */
    @Id
    @Column(name="id",length=32)
    private String id;

    /**
     * 订单编号
     */
    @Column(name="order_sn",length=50)
    private String order_sn;

    /**
     * 组合订单ID
     */
    @Column(name="group_id",length=32)
    private String group_id;

    /**
     * 订单类型,依次为商品，服务，预约
     */
    @Column(name="order_type")
    private String order_type;

    /**
     * 业务模式
     */
    @Column(name="business_model_id",length=32)
    private String business_model_id;

    /**
     * 客户ID
     */
    @Column(name="customer_id",length=32)
    private String customer_id;

    /**
     * 订单收货地址ID
     */
    @Column(name="order_address_id",length=32)
    private String order_address_id;

    /**
     * 门店ID
     */
    @Column(name="store_id",length=32)
    private String store_id;

    /**
     * E店ID
     */
    @Column(name="eshop_id",length=32)
    private String eshop_id;

    /**
     * 订单状态，未完成，  需调整
     */
    @Column(name="order_status")
    private String order_status;

    /**
     * 订单来源
     */
    @Column(name="order_source")
    private String order_source;

    /**
     * 发票状态 yes(开)，no(不开)
     */
    @Column(name="invoice_status")
    private String invoice_status;

    /**
     * 买家备注
     */
    @Column(name="buyer_remark",length=200)
    private String buyer_remark;

    /**
     * 卖家备注
     */
    @Column(name="seller_remark",length=200)
    private String seller_remark;

    /**
     * 国安侠备注
     */
    @Column(name="employee_remark",length=200)
    private String employee_remark;

    /**
     * 异常状态,未完成，后续添加
     */
    @Column(name="abnormal_type")
    private String abnormal_type;

    /**
     * 异常备注
     */
    @Column(name="abnormal_remark",length=500)
    private String abnormal_remark;

    /**
     * 配送方式:配送,E店自配送,自提
     */
    @Column(name="delivery_type")
    private String delivery_type;

    /**
     * 交易价格(多)
     */
    @Column(name="trading_price")
    private String trading_price;

    /**
     * 应付金额(少)
     */
    @Column(name="payable_price")
    private String payable_price;

    /**
     * 是否被拆订单
     */
    @Column(name="is_split")
    private String is_split;

    /**
     * 员工ID
     */
    @Column(name="employee_id",length=32)
    private String employee_id;

    /**
     * 员工电话
     */
    @Column(name="employee_phone",length=20)
    private String employee_phone;

    /**
     * 员工姓名
     */
    @Column(name="employee_name",length=20)
    private String employee_name;

    /**
     * 预约开始时间
     */
    @Column(name="appointment_start_time")
    private Date appointment_start_time;

    /**
     * 预约结束时间
     */
    @Column(name="appointment_end_time")
    private Date appointment_end_time;

    /**
     * E店组合商品ID
     */
    @Column(name="eshop_combo_pro_id",length=32)
    private String eshop_combo_pro_id;

    /**
     * 有效期限
     */
    @Column(name="expiry_date")
    private Date expiry_date;

    /**
     * 组合商品价格
     */
    @Column(name="combo_price")
    private String combo_price;

    /**
     * 总数量
     */
    @Column(name="total_quantity")
    private Integer total_quantity;

    /**
     * 状态标志位
     */
    @Column(name="status")
    private Integer status;

    /**
     * 创建者用户
     */
    @Column(name="create_user",length=36)
    private String create_user;

    /**
     * 记录创建时间
     */
    @Column(name="create_time")
    private Date create_time;

    /**
     * 更新用户
     */
    @Column(name="update_user",length=36)
    private String update_user;

    /**
     * 记录更新时间
     */
    @Column(name="update_time")
    private Date update_time;

    /**
     * 创建人ID
     */
    @Column(name="create_user_id",length=32)
    private String create_user_id;

    /**
     * 更新人ID
     */
    @Column(name="update_user_id",length=32)
    private String update_user_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getBusiness_model_id() {
        return business_model_id;
    }

    public void setBusiness_model_id(String business_model_id) {
        this.business_model_id = business_model_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getOrder_address_id() {
        return order_address_id;
    }

    public void setOrder_address_id(String order_address_id) {
        this.order_address_id = order_address_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getEshop_id() {
        return eshop_id;
    }

    public void setEshop_id(String eshop_id) {
        this.eshop_id = eshop_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_source() {
        return order_source;
    }

    public void setOrder_source(String order_source) {
        this.order_source = order_source;
    }

    public String getInvoice_status() {
        return invoice_status;
    }

    public void setInvoice_status(String invoice_status) {
        this.invoice_status = invoice_status;
    }

    public String getBuyer_remark() {
        return buyer_remark;
    }

    public void setBuyer_remark(String buyer_remark) {
        this.buyer_remark = buyer_remark;
    }

    public String getSeller_remark() {
        return seller_remark;
    }

    public void setSeller_remark(String seller_remark) {
        this.seller_remark = seller_remark;
    }

    public String getEmployee_remark() {
        return employee_remark;
    }

    public void setEmployee_remark(String employee_remark) {
        this.employee_remark = employee_remark;
    }

    public String getAbnormal_type() {
        return abnormal_type;
    }

    public void setAbnormal_type(String abnormal_type) {
        this.abnormal_type = abnormal_type;
    }

    public String getAbnormal_remark() {
        return abnormal_remark;
    }

    public void setAbnormal_remark(String abnormal_remark) {
        this.abnormal_remark = abnormal_remark;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getTrading_price() {
        return trading_price;
    }

    public void setTrading_price(String trading_price) {
        this.trading_price = trading_price;
    }

    public String getPayable_price() {
        return payable_price;
    }

    public void setPayable_price(String payable_price) {
        this.payable_price = payable_price;
    }

    public String getIs_split() {
        return is_split;
    }

    public void setIs_split(String is_split) {
        this.is_split = is_split;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_phone() {
        return employee_phone;
    }

    public void setEmployee_phone(String employee_phone) {
        this.employee_phone = employee_phone;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public Date getAppointment_start_time() {
        return appointment_start_time;
    }

    public void setAppointment_start_time(Date appointment_start_time) {
        this.appointment_start_time = appointment_start_time;
    }

    public Date getAppointment_end_time() {
        return appointment_end_time;
    }

    public void setAppointment_end_time(Date appointment_end_time) {
        this.appointment_end_time = appointment_end_time;
    }

    public String getEshop_combo_pro_id() {
        return eshop_combo_pro_id;
    }

    public void setEshop_combo_pro_id(String eshop_combo_pro_id) {
        this.eshop_combo_pro_id = eshop_combo_pro_id;
    }

    public Date getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(Date expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getCombo_price() {
        return combo_price;
    }

    public void setCombo_price(String combo_price) {
        this.combo_price = combo_price;
    }

    public Integer getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(Integer total_quantity) {
        this.total_quantity = total_quantity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

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

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getUpdate_user_id() {
        return update_user_id;
    }

    public void setUpdate_user_id(String update_user_id) {
        this.update_user_id = update_user_id;
    }
}
