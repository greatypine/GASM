package com.cnpc.pms.personal.entity;




import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSAuditEntity;


/**
 * 
 * 类名: Questionnaire  
 * 功能描述: 问卷调查主表（静态的问卷调查 前端页面写死问题） 实体类
 * 作者: 常鹏飞  
 * 时间: 2016-3-1   
 *   
 */

@Entity
@Table(name = "t_maintenance")

public class Maintenance extends PMSAuditEntity{
	/**
	 */
	private static final long serialVersionUID = 1L;	
    //维修检测ID
	@Id
	@Column(name="id")
	private Long id;
	//地址ID
	@Column(name="address_id")
	private Long address_id;
	//客户ID
	@Column(name="customer_id")
	private Long customer_id;
	//地址
	@Column(name="address")
	private String address;
	//姓名
	@Column(name="name")
	private String name;
	//性别
	@Column(name="sex")
	private Integer sex;
	//电话
	@Column(name="mobilephone")
	private String mobilephone;
	//房屋朝向
	@Column(name="house_direction")
	private String house_direction;
	//建筑面积
	@Column(name="buildup_area")
	private Integer buildup_area;
	//卧室数量
	@Column(name="bedroom_num")
	private Integer bedroom_num;
	//客厅数量
	@Column(name="livingroom_num")
	private Integer livingroom_num;
	//卫生间数量
	@Column(name="bathroom_num")
	private Integer bathroom_num;
	//购买时间
	@Column(name="purchase_date")
	private Date purchase_date;
	//预约时间
	@Column(name="appointment_time")
	private Date appointment_time;
	//检测开始时间
	@Column(name="start_time")
	private Date start_time;
	//检测状态 （1：未开始，2：检测中，3：已完成）
	@Column(name="mainte_status")
	private Integer mainte_status;
	//装修等级
	@Column(name="decorate_level")
	private String decorate_level;
	//==============以下是问题的字段============================
	/*
	 * 卫生间检测
	 */
	//卫生间坡度状态
	@Column(name="bathroom_slope_1")
	private Integer bathroom_slope_1;
	//卫生间坡度建议
	@Column(name="bathroom_slope_2")
	private Integer bathroom_slope_2;
	//卫生间坡度备注
	@Column(name="bathroom_slope_3")
	private String bathroom_slope_3;
	//卫生间防水层状态
	@Column(name="bathroom_waterproof_1")
	private Integer bathroom_waterproof_1;
	//卫生间防水层建议
	@Column(name="bathroom_waterproof_2")
	private Integer bathroom_waterproof_2;
	//卫生间防水层备注
	@Column(name="bathroom_waterproof_3")
	private String bathroom_waterproof_3;
	//淋浴设施状态
	@Column(name="shower_1")
	private Integer shower_1;
	//淋浴设施建议
	@Column(name="shower_2")
	private Integer shower_2;
	//淋浴设施备注
	@Column(name="shower_3")
	private String shower_3;
	//洗衣机热水器进出水口位置状态
	@Column(name="washmachine_outlet_1")
	private Integer washmachine_outlet_1;
	//洗衣机热水器进出水口位置建议
	@Column(name="washmachine_outlet_2")
	private Integer washmachine_outlet_2;
	//洗衣机热水器进出水口位置备注
	@Column(name="washmachine_outlet_3")
	private String washmachine_outlet_3;
	//洁具封胶检测状态
	@Column(name="toilet_sealing_1")
	private Integer toilet_sealing_1;
	//洁具封胶检测建议
	@Column(name="toilet_sealing_2")
	private Integer toilet_sealing_2;
	//洁具封胶检测备注
	@Column(name="toilet_sealing_3")
	private String toilet_sealing_3;
	/*
	 * 厨房检测
	 */
	//燃气灶具状态
	@Column(name="gas_cooker_1")
	private Integer gas_cooker_1;
	//燃气灶具建议
	@Column(name="gas_cooker_2")
	private Integer gas_cooker_2;
	//燃气灶具备注
	@Column(name="gas_cooker_3")
	private String gas_cooker_3;
	//台面封胶检测状态
	@Column(name="mesa_sealing_1")
	private Integer mesa_sealing_1;
	//台面封胶检测建议
	@Column(name="mesa_sealing_2")
	private Integer mesa_sealing_2;
	//台面封胶检测备注
	@Column(name="mesa_sealing_3")
	private String mesa_sealing_3;
	/*
	 * 灯具与开关检测
	 */
	//开关面板状态
	@Column(name="switch_board_1")
	private Integer switch_board_1;
	//开关面板建议
	@Column(name="switch_board_2")
	private Integer switch_board_2;
	//开关面板备注
	@Column(name="switch_board_3")
	private String switch_board_3;
	//开关线路状态
	@Column(name="switch_wire_1")
	private Integer switch_wire_1;
	//开关线路建议
	@Column(name="switch_wire_2")
	private Integer switch_wire_2;
	//开关线路备注
	@Column(name="switch_wire_3")
	private String switch_wire_3;
	//厨卫插座防水盒状态
	@Column(name="waterproof_box_1")
	private Integer waterproof_box_1;
	//厨卫插座防水盒建议
	@Column(name="waterproof_box_2")
	private Integer waterproof_box_2;
	//厨卫插座防水盒备注
	@Column(name="waterproof_box_3")
	private String waterproof_box_3;
	//灯具接头牢固状态
	@Column(name="lamp_secure_1")
	private Integer lamp_secure_1;
	//灯具接头牢固建议
	@Column(name="lamp_secure_2")
	private Integer lamp_secure_2;
	//灯具接头牢固备注
	@Column(name="lamp_secure_3")
	private String lamp_secure_3;
	//灯具接头绝缘状态
	@Column(name="lamp_insulate_1")
	private Integer lamp_insulate_1;
	//灯具接头绝缘建议
	@Column(name="lamp_insulate_2")
	private Integer lamp_insulate_2;
	//灯具接头绝缘备注
	@Column(name="lamp_insulate_3")
	private String lamp_insulate_3;
	//灯具照度情况状态
	@Column(name="lamp_illumination_1")
	private Integer lamp_illumination_1;
	//灯具照度情况建议
	@Column(name="lamp_illumination_2")
	private Integer lamp_illumination_2;
	//灯具照度情况备注
	@Column(name="lamp_illumination_3")
	private String lamp_illumination_3;
	/*
	 * 强弱电、功率检测
	 */
	//强弱电间距状态
	@Column(name="electric_distance_1")
	private Integer electric_distance_1;
	//强弱电间距建议
	@Column(name="electric_distance_2")
	private Integer electric_distance_2;
	//强弱电间距备注
	@Column(name="electric_distance_3")
	private String electric_distance_3;
	//强弱电排布状态
	@Column(name="electric_arrange_1")
	private Integer electric_arrange_1;
	//强弱电排布建议
	@Column(name="electric_arrange_2")
	private Integer electric_arrange_2;
	//强弱电排布备注
	@Column(name="electric_arrange_3")
	private String electric_arrange_3;
	//水电气交叉状态
	@Column(name="electric_water_cross_1")
	private Integer electric_water_cross_1;
	//水电气交叉建议
	@Column(name="electric_water_cross_2")
	private Integer electric_water_cross_2;
	//水电气交叉备注
	@Column(name="electric_water_cross_3")
	private String electric_water_cross_3;
	//配电箱功率状态
	@Column(name="distribution_power_1")
	private Integer distribution_power_1;
	//配电箱功率建议
	@Column(name="distribution_power_2")
	private Integer distribution_power_2;
	//配电箱功率备注
	@Column(name="distribution_power_3")
	private String distribution_power_3;
	//空调电路功率状态
	@Column(name="aircondition_power_1")
	private Integer aircondition_power_1;
	//空调电路功率建议
	@Column(name="aircondition_power_2")
	private Integer aircondition_power_2;
	//空调电路功率备注
	@Column(name="aircondition_power_3")
	private String aircondition_power_3;
	/*
	 * 水龙头、下水检测
	 */
	//排水管道状态
	@Column(name="pipe_1")
	private Integer pipe_1;
	//排水管道建议
	@Column(name="pipe_2")
	private Integer pipe_2;
	//排水管道备注
	@Column(name="pipe_3")
	private String pipe_3;
	//水管固定情况状态
	@Column(name="pipe_fix_1")
	private Integer pipe_fix_1;
	//水管固定情况建议
	@Column(name="pipe_fix_2")
	private Integer pipe_fix_2;
	//水管固定情况备注
	@Column(name="pipe_fix_3")
	private String pipe_fix_3;
	//地漏定位状态
	@Column(name="drain_location_1")
	private Integer drain_location_1;
	//地漏定位建议
	@Column(name="drain_location_2")
	private Integer drain_location_2;
	//地漏定位备注
	@Column(name="drain_location_3")
	private String drain_location_3;
	//地漏密闭性状态
	@Column(name="drain_tight_1")
	private Integer drain_tight_1;
	//地漏密闭性建议
	@Column(name="drain_tight_2")
	private Integer drain_tight_2;
	//地漏密闭性备注
	@Column(name="drain_tight_3")
	private String drain_tight_3;
	//水龙头状态
	@Column(name="stopcock_1")
	private Integer stopcock_1;
	//水龙头建议
	@Column(name="stopcock_2")
	private Integer stopcock_2;
	//水龙头备注
	@Column(name="stopcock_3")
	private String stopcock_3;
	//阀门与龙头接头状态
	@Column(name="valve_joint_1")
	private Integer valve_joint_1;
	//阀门与龙头接头建议
	@Column(name="valve_joint_2")
	private Integer valve_joint_2;
	//阀门与龙头接头备注
	@Column(name="valve_joint_3")
	private String valve_joint_3;
	//面盆下水状态
	@Column(name="basin_sewer_1")
	private Integer basin_sewer_1;
	//面盆下水建议
	@Column(name="basin_sewer_2")
	private Integer basin_sewer_2;
	//面盆下水备注
	@Column(name="basin_sewer_3")
	private String basin_sewer_3;
	//洗菜盆下水状态
	@Column(name="kitchen_sewer_1")
	private Integer kitchen_sewer_1;
	//洗菜盆下水建议
	@Column(name="kitchen_sewer_2")
	private Integer kitchen_sewer_2;
	//洗菜盆下水备注
	@Column(name="kitchen_sewer_3")
	private String kitchen_sewer_3;
	/*
	 * 窗户检测
	 */
	//窗户开启关闭情况状态
	@Column(name="window_switch_1")
	private Integer window_switch_1;
	//窗户开启关闭情况建议
	@Column(name="window_switch_2")
	private Integer window_switch_2;
	//窗户开启关闭情况备注
	@Column(name="window_switch_3")
	private String window_switch_3;
	//窗户指手状态
	@Column(name="window_handle_1")
	private Integer window_handle_1;
	//窗户指手建议
	@Column(name="window_handle_2")
	private Integer window_handle_2;
	//窗户指手备注
	@Column(name="window_handle_3")
	private String window_handle_3;
	//窗户密封胶条状态
	@Column(name="window_sealing_1")
	private Integer window_sealing_1;
	//窗户密封胶条建议
	@Column(name="window_sealing_2")
	private Integer window_sealing_2;
	//窗户密封胶条备注
	@Column(name="window_sealing_3")
	private String window_sealing_3;
	//阳台泛水状态
	@Column(name="balcony_flash_1")
	private Integer balcony_flash_1;
	//阳台泛水建议
	@Column(name="balcony_flash_2")
	private Integer balcony_flash_2;
	//阳台泛水备注
	@Column(name="balcony_flash_3")
	private String balcony_flash_3;
	/*
	 * 门检测
	 */
	//门扇下沉状态
	@Column(name="door_sink_1")
	private Integer door_sink_1;
	//门扇下沉建议
	@Column(name="door_sink_2")
	private Integer door_sink_2;
	//门扇下沉备注
	@Column(name="door_sink_3")
	private String door_sink_3;
	//门扇观感状态
	@Column(name="door_impression_1")
	private Integer door_impression_1;
	//门扇观感建议
	@Column(name="door_impression_2")
	private Integer door_impression_2;
	//门扇观感备注
	@Column(name="door_impression_3")
	private String door_impression_3;
	//门把手固定情况状态
	@Column(name="door_handle_1")
	private Integer door_handle_1;
	//门把手固定情况建议
	@Column(name="door_handle_2")
	private Integer door_handle_2;
	//门把手固定情况备注
	@Column(name="door_handle_3")
	private String door_handle_3;
	//门扇玻璃(如有)状态
	@Column(name="door_glass_1")
	private Integer door_glass_1;
	//门扇玻璃(如有)建议
	@Column(name="door_glass_2")
	private Integer door_glass_2;
	//门扇玻璃(如有)备注
	@Column(name="door_glass_3")
	private String door_glass_3;
	//门锁安全等级监测状态
	@Column(name="lock_security_1")
	private Integer lock_security_1;
	//门锁安全等级监测建议
	@Column(name="lock_security_2")
	private Integer lock_security_2;
	//门锁安全等级监测备注
	@Column(name="lock_security_3")
	private String lock_security_3;
	/*
	 * 墙体检测
	 */
	//墙砖勾缝状态
	@Column(name="wall_gap_1")
	private Integer wall_gap_1;
	//墙砖勾缝建议
	@Column(name="wall_gap_2")
	private Integer wall_gap_2;
	//墙砖勾缝备注
	@Column(name="wall_gap_3")
	private String wall_gap_3;
	//墙砖空鼓状态
	@Column(name="wall_hollow_1")
	private Integer wall_hollow_1;
	//墙砖空鼓建议
	@Column(name="wall_hollow_2")
	private Integer wall_hollow_2;
	//墙砖空鼓备注
	@Column(name="wall_hollow_3")
	private String wall_hollow_3;
	//墙砖松动状态
	@Column(name="wall_loose_1")
	private Integer wall_loose_1;
	//墙砖松动建议
	@Column(name="wall_loose_2")
	private Integer wall_loose_2;
	//墙砖松动备注
	@Column(name="wall_loose_3")
	private String wall_loose_3;
	//墙皮开裂状态
	@Column(name="wall_crack_1")
	private Integer wall_crack_1;
	//墙皮开裂建议
	@Column(name="wall_crack_2")
	private Integer wall_crack_2;
	//墙皮开裂备注
	@Column(name="wall_crack_3")
	private String wall_crack_3;
	//墙皮脱落状态
	@Column(name="wall_drop_1")
	private Integer wall_drop_1;
	//墙皮脱落建议
	@Column(name="wall_drop_2")
	private Integer wall_drop_2;
	//墙皮脱落备注
	@Column(name="wall_drop_3")
	private String wall_drop_3;
	//墙皮变色状态
	@Column(name="wall_color_1")
	private Integer wall_color_1;
	//墙皮变色建议
	@Column(name="wall_color_2")
	private Integer wall_color_2;
	//墙皮变色备注
	@Column(name="wall_color_3")
	private String wall_color_3;
	/*
	 * 地面检测
	 */
	//地砖勾缝状态
	@Column(name="floor_gap_1")
	private Integer floor_gap_1;
	//地砖勾缝建议
	@Column(name="floor_gap_2")
	private Integer floor_gap_2;
	//地砖勾缝备注
	@Column(name="floor_gap_3")
	private String floor_gap_3;
	//地砖空鼓状态
	@Column(name="floor_hollow_1")
	private Integer floor_hollow_1;
	//地砖空鼓建议
	@Column(name="floor_hollow_2")
	private Integer floor_hollow_2;
	//地砖空鼓备注
	@Column(name="floor_hollow_3")
	private String floor_hollow_3;
	//地砖松动状态
	@Column(name="floor_loose_1")
	private Integer floor_loose_1;
	//地砖松动建议
	@Column(name="floor_loose_2")
	private Integer floor_loose_2;
	//地砖松动备注
	@Column(name="floor_loose_3")
	private String floor_loose_3;
	
	//检测项目1
	@Column(name="other_1")
	private String other_1;
	//处理结果1
	@Column(name="result_1")
	private String result_1;
	//检测项目2
	@Column(name="other_2")
	private String other_2;
	//处理结果2
	@Column(name="result_2")
	private String result_2;
	//工程师处理意见
	@Column(name="comment")
	private String comment;

	
	
	

	//创建者
	//问卷调查问题实体类
//	@Transient
//	private QuestionnaireQuestion questionnaireQuestion;
//	//问卷调查问题的集合
//	@Transient
//	private List<QuestionnaireQuestion> QQList;
//	//问卷调查问题选项实体类
//	@Transient
//	private QuestionnaireItem questionnaireItem;
//	//问卷调查问题选项集合
//	@Transient
//	private List<QuestionnaireItem> QIList;
	
	//客户实体类
	@Transient
	private Customer customer;
	// 门店ID
	@Transient
	private Long store_id;
	//接收传递的时间
	@Transient
	private String time;
	//判断是添加用户检测还是修改用户检测
	@Transient
	private String flag;
	
	//小区id
	private Long tinyVillageId;
	
	//房间号
	@Transient
	private String building_house_no;
	
	//单元号
	@Transient
	private String building_unit_no;
	
	//楼号
	@Transient
	private String building_name;
	//检测状态
	@Transient
	private String statuss;
	
	
	/**
	 * 返回: statuss
	 */
	public String getStatuss() {
		return statuss;
	}

	/**
	 * 设置:statuss
	 */
	public void setStatuss(String statuss) {
		this.statuss = statuss;
	}

	/**
	 * 返回: time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * 设置:time
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * 返回: store_id
	 */
	public Long getStore_id() {
		return store_id;
	}

	/**
	 * 设置:store_id
	 */
	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	/**
	 * 返回: id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置:id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 返回: address_id
	 */
	public Long getAddress_id() {
		return address_id;
	}

	/**
	 * 设置:address_id
	 */
	public void setAddress_id(Long address_id) {
		this.address_id = address_id;
	}

	/**
	 * 返回: customer_id
	 */
	public Long getCustomer_id() {
		return customer_id;
	}

	/**
	 * 设置:customer_id
	 */
	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}

	/**
	 * 返回: address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置:address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 返回: name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置:name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回: sex
	 */
	public Integer getSex() {
		return sex;
	}

	/**
	 * 设置:sex
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}

	/**
	 * 返回: mobilephone
	 */
	public String getMobilephone() {
		return mobilephone;
	}

	/**
	 * 设置:mobilephone
	 */
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	/**
	 * 返回: house_direction
	 */
	public String getHouse_direction() {
		return house_direction;
	}

	/**
	 * 设置:house_direction
	 */
	public void setHouse_direction(String house_direction) {
		this.house_direction = house_direction;
	}

	/**
	 * 返回: buildup_area
	 */
	public Integer getBuildup_area() {
		return buildup_area;
	}

	/**
	 * 设置:buildup_area
	 */
	public void setBuildup_area(Integer buildup_area) {
		this.buildup_area = buildup_area;
	}

	/**
	 * 返回: bedroom_num
	 */
	public Integer getBedroom_num() {
		return bedroom_num;
	}

	/**
	 * 设置:bedroom_num
	 */
	public void setBedroom_num(Integer bedroom_num) {
		this.bedroom_num = bedroom_num;
	}

	/**
	 * 返回: livingroom_num
	 */
	public Integer getLivingroom_num() {
		return livingroom_num;
	}

	/**
	 * 设置:livingroom_num
	 */
	public void setLivingroom_num(Integer livingroom_num) {
		this.livingroom_num = livingroom_num;
	}

	/**
	 * 返回: bathroom_num
	 */
	public Integer getBathroom_num() {
		return bathroom_num;
	}

	/**
	 * 设置:bathroom_num
	 */
	public void setBathroom_num(Integer bathroom_num) {
		this.bathroom_num = bathroom_num;
	}

	/**
	 * 返回: purchase_date
	 */
	public Date getPurchase_date() {
		return purchase_date;
	}

	/**
	 * 设置:purchase_date
	 */
	public void setPurchase_date(Date purchase_date) {
		this.purchase_date = purchase_date;
	}

	/**
	 * 返回: decorate_level
	 */
	public String getDecorate_level() {
		return decorate_level;
	}

	/**
	 * 设置:decorate_level
	 */
	public void setDecorate_level(String decorate_level) {
		this.decorate_level = decorate_level;
	}

	/**
	 * 返回: bathroom_slope_1
	 */
	public Integer getBathroom_slope_1() {
		return bathroom_slope_1;
	}

	/**
	 * 设置:bathroom_slope_1
	 */
	public void setBathroom_slope_1(Integer bathroom_slope_1) {
		this.bathroom_slope_1 = bathroom_slope_1;
	}

	/**
	 * 返回: bathroom_slope_2
	 */
	public Integer getBathroom_slope_2() {
		return bathroom_slope_2;
	}

	/**
	 * 设置:bathroom_slope_2
	 */
	public void setBathroom_slope_2(Integer bathroom_slope_2) {
		this.bathroom_slope_2 = bathroom_slope_2;
	}

	/**
	 * 返回: bathroom_slope_3
	 */
	public String getBathroom_slope_3() {
		return bathroom_slope_3;
	}

	/**
	 * 设置:bathroom_slope_3
	 */
	public void setBathroom_slope_3(String bathroom_slope_3) {
		this.bathroom_slope_3 = bathroom_slope_3;
	}

	/**
	 * 返回: bathroom_waterproof_1
	 */
	public Integer getBathroom_waterproof_1() {
		return bathroom_waterproof_1;
	}

	/**
	 * 设置:bathroom_waterproof_1
	 */
	public void setBathroom_waterproof_1(Integer bathroom_waterproof_1) {
		this.bathroom_waterproof_1 = bathroom_waterproof_1;
	}

	/**
	 * 返回: bathroom_waterproof_2
	 */
	public Integer getBathroom_waterproof_2() {
		return bathroom_waterproof_2;
	}

	/**
	 * 设置:bathroom_waterproof_2
	 */
	public void setBathroom_waterproof_2(Integer bathroom_waterproof_2) {
		this.bathroom_waterproof_2 = bathroom_waterproof_2;
	}

	/**
	 * 返回: bathroom_waterproof_3
	 */
	public String getBathroom_waterproof_3() {
		return bathroom_waterproof_3;
	}

	/**
	 * 设置:bathroom_waterproof_3
	 */
	public void setBathroom_waterproof_3(String bathroom_waterproof_3) {
		this.bathroom_waterproof_3 = bathroom_waterproof_3;
	}

	/**
	 * 返回: shower_1
	 */
	public Integer getShower_1() {
		return shower_1;
	}

	/**
	 * 设置:shower_1
	 */
	public void setShower_1(Integer shower_1) {
		this.shower_1 = shower_1;
	}

	/**
	 * 返回: shower_2
	 */
	public Integer getShower_2() {
		return shower_2;
	}

	/**
	 * 设置:shower_2
	 */
	public void setShower_2(Integer shower_2) {
		this.shower_2 = shower_2;
	}

	/**
	 * 返回: shower_3
	 */
	public String getShower_3() {
		return shower_3;
	}

	/**
	 * 设置:shower_3
	 */
	public void setShower_3(String shower_3) {
		this.shower_3 = shower_3;
	}

	/**
	 * 返回: washmachine_outlet_1
	 */
	public Integer getWashmachine_outlet_1() {
		return washmachine_outlet_1;
	}

	/**
	 * 设置:washmachine_outlet_1
	 */
	public void setWashmachine_outlet_1(Integer washmachine_outlet_1) {
		this.washmachine_outlet_1 = washmachine_outlet_1;
	}

	/**
	 * 返回: washmachine_outlet_2
	 */
	public Integer getWashmachine_outlet_2() {
		return washmachine_outlet_2;
	}

	/**
	 * 设置:washmachine_outlet_2
	 */
	public void setWashmachine_outlet_2(Integer washmachine_outlet_2) {
		this.washmachine_outlet_2 = washmachine_outlet_2;
	}

	/**
	 * 返回: washmachine_outlet_3
	 */
	public String getWashmachine_outlet_3() {
		return washmachine_outlet_3;
	}

	/**
	 * 设置:washmachine_outlet_3
	 */
	public void setWashmachine_outlet_3(String washmachine_outlet_3) {
		this.washmachine_outlet_3 = washmachine_outlet_3;
	}

	/**
	 * 返回: toilet_sealing_1
	 */
	public Integer getToilet_sealing_1() {
		return toilet_sealing_1;
	}

	/**
	 * 设置:toilet_sealing_1
	 */
	public void setToilet_sealing_1(Integer toilet_sealing_1) {
		this.toilet_sealing_1 = toilet_sealing_1;
	}

	/**
	 * 返回: toilet_sealing_2
	 */
	public Integer getToilet_sealing_2() {
		return toilet_sealing_2;
	}

	/**
	 * 设置:toilet_sealing_2
	 */
	public void setToilet_sealing_2(Integer toilet_sealing_2) {
		this.toilet_sealing_2 = toilet_sealing_2;
	}

	/**
	 * 返回: toilet_sealing_3
	 */
	public String getToilet_sealing_3() {
		return toilet_sealing_3;
	}

	/**
	 * 设置:toilet_sealing_3
	 */
	public void setToilet_sealing_3(String toilet_sealing_3) {
		this.toilet_sealing_3 = toilet_sealing_3;
	}

	/**
	 * 返回: gas_cooker_1
	 */
	public Integer getGas_cooker_1() {
		return gas_cooker_1;
	}

	/**
	 * 设置:gas_cooker_1
	 */
	public void setGas_cooker_1(Integer gas_cooker_1) {
		this.gas_cooker_1 = gas_cooker_1;
	}

	/**
	 * 返回: gas_cooker_2
	 */
	public Integer getGas_cooker_2() {
		return gas_cooker_2;
	}

	/**
	 * 设置:gas_cooker_2
	 */
	public void setGas_cooker_2(Integer gas_cooker_2) {
		this.gas_cooker_2 = gas_cooker_2;
	}

	/**
	 * 返回: gas_cooker_3
	 */
	public String getGas_cooker_3() {
		return gas_cooker_3;
	}

	/**
	 * 设置:gas_cooker_3
	 */
	public void setGas_cooker_3(String gas_cooker_3) {
		this.gas_cooker_3 = gas_cooker_3;
	}

	/**
	 * 返回: mesa_sealing_1
	 */
	public Integer getMesa_sealing_1() {
		return mesa_sealing_1;
	}

	/**
	 * 设置:mesa_sealing_1
	 */
	public void setMesa_sealing_1(Integer mesa_sealing_1) {
		this.mesa_sealing_1 = mesa_sealing_1;
	}

	/**
	 * 返回: mesa_sealing_2
	 */
	public Integer getMesa_sealing_2() {
		return mesa_sealing_2;
	}

	/**
	 * 设置:mesa_sealing_2
	 */
	public void setMesa_sealing_2(Integer mesa_sealing_2) {
		this.mesa_sealing_2 = mesa_sealing_2;
	}

	/**
	 * 返回: mesa_sealing_3
	 */
	public String getMesa_sealing_3() {
		return mesa_sealing_3;
	}

	/**
	 * 设置:mesa_sealing_3
	 */
	public void setMesa_sealing_3(String mesa_sealing_3) {
		this.mesa_sealing_3 = mesa_sealing_3;
	}

	/**
	 * 返回: switch_board_1
	 */
	public Integer getSwitch_board_1() {
		return switch_board_1;
	}

	/**
	 * 设置:switch_board_1
	 */
	public void setSwitch_board_1(Integer switch_board_1) {
		this.switch_board_1 = switch_board_1;
	}

	/**
	 * 返回: switch_board_2
	 */
	public Integer getSwitch_board_2() {
		return switch_board_2;
	}

	/**
	 * 设置:switch_board_2
	 */
	public void setSwitch_board_2(Integer switch_board_2) {
		this.switch_board_2 = switch_board_2;
	}

	/**
	 * 返回: switch_board_3
	 */
	public String getSwitch_board_3() {
		return switch_board_3;
	}

	/**
	 * 设置:switch_board_3
	 */
	public void setSwitch_board_3(String switch_board_3) {
		this.switch_board_3 = switch_board_3;
	}

	/**
	 * 返回: switch_wire_1
	 */
	public Integer getSwitch_wire_1() {
		return switch_wire_1;
	}

	/**
	 * 设置:switch_wire_1
	 */
	public void setSwitch_wire_1(Integer switch_wire_1) {
		this.switch_wire_1 = switch_wire_1;
	}

	/**
	 * 返回: switch_wire_2
	 */
	public Integer getSwitch_wire_2() {
		return switch_wire_2;
	}

	/**
	 * 设置:switch_wire_2
	 */
	public void setSwitch_wire_2(Integer switch_wire_2) {
		this.switch_wire_2 = switch_wire_2;
	}

	/**
	 * 返回: switch_wire_3
	 */
	public String getSwitch_wire_3() {
		return switch_wire_3;
	}

	/**
	 * 设置:switch_wire_3
	 */
	public void setSwitch_wire_3(String switch_wire_3) {
		this.switch_wire_3 = switch_wire_3;
	}

	/**
	 * 返回: waterproof_box_1
	 */
	public Integer getWaterproof_box_1() {
		return waterproof_box_1;
	}

	/**
	 * 设置:waterproof_box_1
	 */
	public void setWaterproof_box_1(Integer waterproof_box_1) {
		this.waterproof_box_1 = waterproof_box_1;
	}

	/**
	 * 返回: waterproof_box_2
	 */
	public Integer getWaterproof_box_2() {
		return waterproof_box_2;
	}

	/**
	 * 设置:waterproof_box_2
	 */
	public void setWaterproof_box_2(Integer waterproof_box_2) {
		this.waterproof_box_2 = waterproof_box_2;
	}

	/**
	 * 返回: waterproof_box_3
	 */
	public String getWaterproof_box_3() {
		return waterproof_box_3;
	}

	/**
	 * 设置:waterproof_box_3
	 */
	public void setWaterproof_box_3(String waterproof_box_3) {
		this.waterproof_box_3 = waterproof_box_3;
	}

	/**
	 * 返回: lamp_secure_1
	 */
	public Integer getLamp_secure_1() {
		return lamp_secure_1;
	}

	/**
	 * 设置:lamp_secure_1
	 */
	public void setLamp_secure_1(Integer lamp_secure_1) {
		this.lamp_secure_1 = lamp_secure_1;
	}

	/**
	 * 返回: lamp_secure_2
	 */
	public Integer getLamp_secure_2() {
		return lamp_secure_2;
	}

	/**
	 * 设置:lamp_secure_2
	 */
	public void setLamp_secure_2(Integer lamp_secure_2) {
		this.lamp_secure_2 = lamp_secure_2;
	}

	/**
	 * 返回: lamp_secure_3
	 */
	public String getLamp_secure_3() {
		return lamp_secure_3;
	}

	/**
	 * 设置:lamp_secure_3
	 */
	public void setLamp_secure_3(String lamp_secure_3) {
		this.lamp_secure_3 = lamp_secure_3;
	}

	/**
	 * 返回: lamp_insulate_1
	 */
	public Integer getLamp_insulate_1() {
		return lamp_insulate_1;
	}

	/**
	 * 设置:lamp_insulate_1
	 */
	public void setLamp_insulate_1(Integer lamp_insulate_1) {
		this.lamp_insulate_1 = lamp_insulate_1;
	}

	/**
	 * 返回: lamp_insulate_2
	 */
	public Integer getLamp_insulate_2() {
		return lamp_insulate_2;
	}

	/**
	 * 设置:lamp_insulate_2
	 */
	public void setLamp_insulate_2(Integer lamp_insulate_2) {
		this.lamp_insulate_2 = lamp_insulate_2;
	}

	/**
	 * 返回: lamp_insulate_3
	 */
	public String getLamp_insulate_3() {
		return lamp_insulate_3;
	}

	/**
	 * 设置:lamp_insulate_3
	 */
	public void setLamp_insulate_3(String lamp_insulate_3) {
		this.lamp_insulate_3 = lamp_insulate_3;
	}

	/**
	 * 返回: lamp_illumination_1
	 */
	public Integer getLamp_illumination_1() {
		return lamp_illumination_1;
	}

	/**
	 * 设置:lamp_illumination_1
	 */
	public void setLamp_illumination_1(Integer lamp_illumination_1) {
		this.lamp_illumination_1 = lamp_illumination_1;
	}

	/**
	 * 返回: lamp_illumination_2
	 */
	public Integer getLamp_illumination_2() {
		return lamp_illumination_2;
	}

	/**
	 * 设置:lamp_illumination_2
	 */
	public void setLamp_illumination_2(Integer lamp_illumination_2) {
		this.lamp_illumination_2 = lamp_illumination_2;
	}

	/**
	 * 返回: lamp_illumination_3
	 */
	public String getLamp_illumination_3() {
		return lamp_illumination_3;
	}

	/**
	 * 设置:lamp_illumination_3
	 */
	public void setLamp_illumination_3(String lamp_illumination_3) {
		this.lamp_illumination_3 = lamp_illumination_3;
	}

	/**
	 * 返回: electric_distance_1
	 */
	public Integer getElectric_distance_1() {
		return electric_distance_1;
	}

	/**
	 * 设置:electric_distance_1
	 */
	public void setElectric_distance_1(Integer electric_distance_1) {
		this.electric_distance_1 = electric_distance_1;
	}

	/**
	 * 返回: electric_distance_2
	 */
	public Integer getElectric_distance_2() {
		return electric_distance_2;
	}

	/**
	 * 设置:electric_distance_2
	 */
	public void setElectric_distance_2(Integer electric_distance_2) {
		this.electric_distance_2 = electric_distance_2;
	}

	/**
	 * 返回: electric_distance_3
	 */
	public String getElectric_distance_3() {
		return electric_distance_3;
	}

	/**
	 * 设置:electric_distance_3
	 */
	public void setElectric_distance_3(String electric_distance_3) {
		this.electric_distance_3 = electric_distance_3;
	}

	/**
	 * 返回: electric_arrange_1
	 */
	public Integer getElectric_arrange_1() {
		return electric_arrange_1;
	}

	/**
	 * 设置:electric_arrange_1
	 */
	public void setElectric_arrange_1(Integer electric_arrange_1) {
		this.electric_arrange_1 = electric_arrange_1;
	}

	/**
	 * 返回: electric_arrange_2
	 */
	public Integer getElectric_arrange_2() {
		return electric_arrange_2;
	}

	/**
	 * 设置:electric_arrange_2
	 */
	public void setElectric_arrange_2(Integer electric_arrange_2) {
		this.electric_arrange_2 = electric_arrange_2;
	}

	/**
	 * 返回: electric_arrange_3
	 */
	public String getElectric_arrange_3() {
		return electric_arrange_3;
	}

	/**
	 * 设置:electric_arrange_3
	 */
	public void setElectric_arrange_3(String electric_arrange_3) {
		this.electric_arrange_3 = electric_arrange_3;
	}

	/**
	 * 返回: electric_water_cross_1
	 */
	public Integer getElectric_water_cross_1() {
		return electric_water_cross_1;
	}

	/**
	 * 设置:electric_water_cross_1
	 */
	public void setElectric_water_cross_1(Integer electric_water_cross_1) {
		this.electric_water_cross_1 = electric_water_cross_1;
	}

	/**
	 * 返回: electric_water_cross_2
	 */
	public Integer getElectric_water_cross_2() {
		return electric_water_cross_2;
	}

	/**
	 * 设置:electric_water_cross_2
	 */
	public void setElectric_water_cross_2(Integer electric_water_cross_2) {
		this.electric_water_cross_2 = electric_water_cross_2;
	}

	/**
	 * 返回: electric_water_cross_3
	 */
	public String getElectric_water_cross_3() {
		return electric_water_cross_3;
	}

	/**
	 * 设置:electric_water_cross_3
	 */
	public void setElectric_water_cross_3(String electric_water_cross_3) {
		this.electric_water_cross_3 = electric_water_cross_3;
	}

	/**
	 * 返回: distribution_power_1
	 */
	public Integer getDistribution_power_1() {
		return distribution_power_1;
	}

	/**
	 * 设置:distribution_power_1
	 */
	public void setDistribution_power_1(Integer distribution_power_1) {
		this.distribution_power_1 = distribution_power_1;
	}

	/**
	 * 返回: distribution_power_2
	 */
	public Integer getDistribution_power_2() {
		return distribution_power_2;
	}

	/**
	 * 设置:distribution_power_2
	 */
	public void setDistribution_power_2(Integer distribution_power_2) {
		this.distribution_power_2 = distribution_power_2;
	}

	/**
	 * 返回: distribution_power_3
	 */
	public String getDistribution_power_3() {
		return distribution_power_3;
	}

	/**
	 * 设置:distribution_power_3
	 */
	public void setDistribution_power_3(String distribution_power_3) {
		this.distribution_power_3 = distribution_power_3;
	}

	/**
	 * 返回: aircondition_power_1
	 */
	public Integer getAircondition_power_1() {
		return aircondition_power_1;
	}

	/**
	 * 设置:aircondition_power_1
	 */
	public void setAircondition_power_1(Integer aircondition_power_1) {
		this.aircondition_power_1 = aircondition_power_1;
	}

	/**
	 * 返回: aircondition_power_2
	 */
	public Integer getAircondition_power_2() {
		return aircondition_power_2;
	}

	/**
	 * 设置:aircondition_power_2
	 */
	public void setAircondition_power_2(Integer aircondition_power_2) {
		this.aircondition_power_2 = aircondition_power_2;
	}

	/**
	 * 返回: aircondition_power_3
	 */
	public String getAircondition_power_3() {
		return aircondition_power_3;
	}

	/**
	 * 设置:aircondition_power_3
	 */
	public void setAircondition_power_3(String aircondition_power_3) {
		this.aircondition_power_3 = aircondition_power_3;
	}

	/**
	 * 返回: pipe_1
	 */
	public Integer getPipe_1() {
		return pipe_1;
	}

	/**
	 * 设置:pipe_1
	 */
	public void setPipe_1(Integer pipe_1) {
		this.pipe_1 = pipe_1;
	}

	/**
	 * 返回: pipe_2
	 */
	public Integer getPipe_2() {
		return pipe_2;
	}

	/**
	 * 设置:pipe_2
	 */
	public void setPipe_2(Integer pipe_2) {
		this.pipe_2 = pipe_2;
	}

	/**
	 * 返回: pipe_3
	 */
	public String getPipe_3() {
		return pipe_3;
	}

	/**
	 * 设置:pipe_3
	 */
	public void setPipe_3(String pipe_3) {
		this.pipe_3 = pipe_3;
	}

	/**
	 * 返回: pipe_fix_1
	 */
	public Integer getPipe_fix_1() {
		return pipe_fix_1;
	}

	/**
	 * 设置:pipe_fix_1
	 */
	public void setPipe_fix_1(Integer pipe_fix_1) {
		this.pipe_fix_1 = pipe_fix_1;
	}

	/**
	 * 返回: pipe_fix_2
	 */
	public Integer getPipe_fix_2() {
		return pipe_fix_2;
	}

	/**
	 * 设置:pipe_fix_2
	 */
	public void setPipe_fix_2(Integer pipe_fix_2) {
		this.pipe_fix_2 = pipe_fix_2;
	}

	/**
	 * 返回: pipe_fix_3
	 */
	public String getPipe_fix_3() {
		return pipe_fix_3;
	}

	/**
	 * 设置:pipe_fix_3
	 */
	public void setPipe_fix_3(String pipe_fix_3) {
		this.pipe_fix_3 = pipe_fix_3;
	}

	/**
	 * 返回: drain_location_1
	 */
	public Integer getDrain_location_1() {
		return drain_location_1;
	}

	/**
	 * 设置:drain_location_1
	 */
	public void setDrain_location_1(Integer drain_location_1) {
		this.drain_location_1 = drain_location_1;
	}

	/**
	 * 返回: drain_location_2
	 */
	public Integer getDrain_location_2() {
		return drain_location_2;
	}

	/**
	 * 设置:drain_location_2
	 */
	public void setDrain_location_2(Integer drain_location_2) {
		this.drain_location_2 = drain_location_2;
	}

	/**
	 * 返回: drain_location_3
	 */
	public String getDrain_location_3() {
		return drain_location_3;
	}

	/**
	 * 设置:drain_location_3
	 */
	public void setDrain_location_3(String drain_location_3) {
		this.drain_location_3 = drain_location_3;
	}

	/**
	 * 返回: drain_tight_1
	 */
	public Integer getDrain_tight_1() {
		return drain_tight_1;
	}

	/**
	 * 设置:drain_tight_1
	 */
	public void setDrain_tight_1(Integer drain_tight_1) {
		this.drain_tight_1 = drain_tight_1;
	}

	/**
	 * 返回: drain_tight_2
	 */
	public Integer getDrain_tight_2() {
		return drain_tight_2;
	}

	/**
	 * 设置:drain_tight_2
	 */
	public void setDrain_tight_2(Integer drain_tight_2) {
		this.drain_tight_2 = drain_tight_2;
	}

	/**
	 * 返回: drain_tight_3
	 */
	public String getDrain_tight_3() {
		return drain_tight_3;
	}

	/**
	 * 设置:drain_tight_3
	 */
	public void setDrain_tight_3(String drain_tight_3) {
		this.drain_tight_3 = drain_tight_3;
	}

	/**
	 * 返回: stopcock_1
	 */
	public Integer getStopcock_1() {
		return stopcock_1;
	}

	/**
	 * 设置:stopcock_1
	 */
	public void setStopcock_1(Integer stopcock_1) {
		this.stopcock_1 = stopcock_1;
	}

	/**
	 * 返回: stopcock_2
	 */
	public Integer getStopcock_2() {
		return stopcock_2;
	}

	/**
	 * 设置:stopcock_2
	 */
	public void setStopcock_2(Integer stopcock_2) {
		this.stopcock_2 = stopcock_2;
	}

	/**
	 * 返回: stopcock_3
	 */
	public String getStopcock_3() {
		return stopcock_3;
	}

	/**
	 * 设置:stopcock_3
	 */
	public void setStopcock_3(String stopcock_3) {
		this.stopcock_3 = stopcock_3;
	}

	/**
	 * 返回: valve_joint_1
	 */
	public Integer getValve_joint_1() {
		return valve_joint_1;
	}

	/**
	 * 设置:valve_joint_1
	 */
	public void setValve_joint_1(Integer valve_joint_1) {
		this.valve_joint_1 = valve_joint_1;
	}

	/**
	 * 返回: valve_joint_2
	 */
	public Integer getValve_joint_2() {
		return valve_joint_2;
	}

	/**
	 * 设置:valve_joint_2
	 */
	public void setValve_joint_2(Integer valve_joint_2) {
		this.valve_joint_2 = valve_joint_2;
	}

	/**
	 * 返回: valve_joint_3
	 */
	public String getValve_joint_3() {
		return valve_joint_3;
	}

	/**
	 * 设置:valve_joint_3
	 */
	public void setValve_joint_3(String valve_joint_3) {
		this.valve_joint_3 = valve_joint_3;
	}

	/**
	 * 返回: basin_sewer_1
	 */
	public Integer getBasin_sewer_1() {
		return basin_sewer_1;
	}

	/**
	 * 设置:basin_sewer_1
	 */
	public void setBasin_sewer_1(Integer basin_sewer_1) {
		this.basin_sewer_1 = basin_sewer_1;
	}

	/**
	 * 返回: basin_sewer_2
	 */
	public Integer getBasin_sewer_2() {
		return basin_sewer_2;
	}

	/**
	 * 设置:basin_sewer_2
	 */
	public void setBasin_sewer_2(Integer basin_sewer_2) {
		this.basin_sewer_2 = basin_sewer_2;
	}

	/**
	 * 返回: basin_sewer_3
	 */
	public String getBasin_sewer_3() {
		return basin_sewer_3;
	}

	/**
	 * 设置:basin_sewer_3
	 */
	public void setBasin_sewer_3(String basin_sewer_3) {
		this.basin_sewer_3 = basin_sewer_3;
	}

	/**
	 * 返回: kitchen_sewer_1
	 */
	public Integer getKitchen_sewer_1() {
		return kitchen_sewer_1;
	}

	/**
	 * 设置:kitchen_sewer_1
	 */
	public void setKitchen_sewer_1(Integer kitchen_sewer_1) {
		this.kitchen_sewer_1 = kitchen_sewer_1;
	}

	/**
	 * 返回: kitchen_sewer_2
	 */
	public Integer getKitchen_sewer_2() {
		return kitchen_sewer_2;
	}

	/**
	 * 设置:kitchen_sewer_2
	 */
	public void setKitchen_sewer_2(Integer kitchen_sewer_2) {
		this.kitchen_sewer_2 = kitchen_sewer_2;
	}

	/**
	 * 返回: kitchen_sewer_3
	 */
	public String getKitchen_sewer_3() {
		return kitchen_sewer_3;
	}

	/**
	 * 设置:kitchen_sewer_3
	 */
	public void setKitchen_sewer_3(String kitchen_sewer_3) {
		this.kitchen_sewer_3 = kitchen_sewer_3;
	}

	/**
	 * 返回: window_switch_1
	 */
	public Integer getWindow_switch_1() {
		return window_switch_1;
	}

	/**
	 * 设置:window_switch_1
	 */
	public void setWindow_switch_1(Integer window_switch_1) {
		this.window_switch_1 = window_switch_1;
	}

	/**
	 * 返回: window_switch_2
	 */
	public Integer getWindow_switch_2() {
		return window_switch_2;
	}

	/**
	 * 设置:window_switch_2
	 */
	public void setWindow_switch_2(Integer window_switch_2) {
		this.window_switch_2 = window_switch_2;
	}

	/**
	 * 返回: window_switch_3
	 */
	public String getWindow_switch_3() {
		return window_switch_3;
	}

	/**
	 * 设置:window_switch_3
	 */
	public void setWindow_switch_3(String window_switch_3) {
		this.window_switch_3 = window_switch_3;
	}

	/**
	 * 返回: window_handle_1
	 */
	public Integer getWindow_handle_1() {
		return window_handle_1;
	}

	/**
	 * 设置:window_handle_1
	 */
	public void setWindow_handle_1(Integer window_handle_1) {
		this.window_handle_1 = window_handle_1;
	}

	/**
	 * 返回: window_handle_2
	 */
	public Integer getWindow_handle_2() {
		return window_handle_2;
	}

	/**
	 * 设置:window_handle_2
	 */
	public void setWindow_handle_2(Integer window_handle_2) {
		this.window_handle_2 = window_handle_2;
	}

	/**
	 * 返回: window_handle_3
	 */
	public String getWindow_handle_3() {
		return window_handle_3;
	}

	/**
	 * 设置:window_handle_3
	 */
	public void setWindow_handle_3(String window_handle_3) {
		this.window_handle_3 = window_handle_3;
	}

	/**
	 * 返回: window_sealing_1
	 */
	public Integer getWindow_sealing_1() {
		return window_sealing_1;
	}

	/**
	 * 设置:window_sealing_1
	 */
	public void setWindow_sealing_1(Integer window_sealing_1) {
		this.window_sealing_1 = window_sealing_1;
	}

	/**
	 * 返回: window_sealing_2
	 */
	public Integer getWindow_sealing_2() {
		return window_sealing_2;
	}

	/**
	 * 设置:window_sealing_2
	 */
	public void setWindow_sealing_2(Integer window_sealing_2) {
		this.window_sealing_2 = window_sealing_2;
	}

	/**
	 * 返回: window_sealing_3
	 */
	public String getWindow_sealing_3() {
		return window_sealing_3;
	}

	/**
	 * 设置:window_sealing_3
	 */
	public void setWindow_sealing_3(String window_sealing_3) {
		this.window_sealing_3 = window_sealing_3;
	}

	/**
	 * 返回: balcony_flash_1
	 */
	public Integer getBalcony_flash_1() {
		return balcony_flash_1;
	}

	/**
	 * 设置:balcony_flash_1
	 */
	public void setBalcony_flash_1(Integer balcony_flash_1) {
		this.balcony_flash_1 = balcony_flash_1;
	}

	/**
	 * 返回: balcony_flash_2
	 */
	public Integer getBalcony_flash_2() {
		return balcony_flash_2;
	}

	/**
	 * 设置:balcony_flash_2
	 */
	public void setBalcony_flash_2(Integer balcony_flash_2) {
		this.balcony_flash_2 = balcony_flash_2;
	}

	/**
	 * 返回: balcony_flash_3
	 */
	public String getBalcony_flash_3() {
		return balcony_flash_3;
	}

	/**
	 * 设置:balcony_flash_3
	 */
	public void setBalcony_flash_3(String balcony_flash_3) {
		this.balcony_flash_3 = balcony_flash_3;
	}

	/**
	 * 返回: door_sink_1
	 */
	public Integer getDoor_sink_1() {
		return door_sink_1;
	}

	/**
	 * 设置:door_sink_1
	 */
	public void setDoor_sink_1(Integer door_sink_1) {
		this.door_sink_1 = door_sink_1;
	}

	/**
	 * 返回: door_sink_2
	 */
	public Integer getDoor_sink_2() {
		return door_sink_2;
	}

	/**
	 * 设置:door_sink_2
	 */
	public void setDoor_sink_2(Integer door_sink_2) {
		this.door_sink_2 = door_sink_2;
	}

	/**
	 * 返回: door_sink_3
	 */
	public String getDoor_sink_3() {
		return door_sink_3;
	}

	/**
	 * 设置:door_sink_3
	 */
	public void setDoor_sink_3(String door_sink_3) {
		this.door_sink_3 = door_sink_3;
	}

	/**
	 * 返回: door_impression_1
	 */
	public Integer getDoor_impression_1() {
		return door_impression_1;
	}

	/**
	 * 设置:door_impression_1
	 */
	public void setDoor_impression_1(Integer door_impression_1) {
		this.door_impression_1 = door_impression_1;
	}

	/**
	 * 返回: door_impression_2
	 */
	public Integer getDoor_impression_2() {
		return door_impression_2;
	}

	/**
	 * 设置:door_impression_2
	 */
	public void setDoor_impression_2(Integer door_impression_2) {
		this.door_impression_2 = door_impression_2;
	}

	/**
	 * 返回: door_impression_3
	 */
	public String getDoor_impression_3() {
		return door_impression_3;
	}

	/**
	 * 设置:door_impression_3
	 */
	public void setDoor_impression_3(String door_impression_3) {
		this.door_impression_3 = door_impression_3;
	}

	/**
	 * 返回: door_handle_1
	 */
	public Integer getDoor_handle_1() {
		return door_handle_1;
	}

	/**
	 * 设置:door_handle_1
	 */
	public void setDoor_handle_1(Integer door_handle_1) {
		this.door_handle_1 = door_handle_1;
	}

	/**
	 * 返回: door_handle_2
	 */
	public Integer getDoor_handle_2() {
		return door_handle_2;
	}

	/**
	 * 设置:door_handle_2
	 */
	public void setDoor_handle_2(Integer door_handle_2) {
		this.door_handle_2 = door_handle_2;
	}

	/**
	 * 返回: door_handle_3
	 */
	public String getDoor_handle_3() {
		return door_handle_3;
	}

	/**
	 * 设置:door_handle_3
	 */
	public void setDoor_handle_3(String door_handle_3) {
		this.door_handle_3 = door_handle_3;
	}

	/**
	 * 返回: door_glass_1
	 */
	public Integer getDoor_glass_1() {
		return door_glass_1;
	}

	/**
	 * 设置:door_glass_1
	 */
	public void setDoor_glass_1(Integer door_glass_1) {
		this.door_glass_1 = door_glass_1;
	}

	/**
	 * 返回: door_glass_2
	 */
	public Integer getDoor_glass_2() {
		return door_glass_2;
	}

	/**
	 * 设置:door_glass_2
	 */
	public void setDoor_glass_2(Integer door_glass_2) {
		this.door_glass_2 = door_glass_2;
	}

	/**
	 * 返回: door_glass_3
	 */
	public String getDoor_glass_3() {
		return door_glass_3;
	}

	/**
	 * 设置:door_glass_3
	 */
	public void setDoor_glass_3(String door_glass_3) {
		this.door_glass_3 = door_glass_3;
	}

	/**
	 * 返回: lock_security_1
	 */
	public Integer getLock_security_1() {
		return lock_security_1;
	}

	/**
	 * 设置:lock_security_1
	 */
	public void setLock_security_1(Integer lock_security_1) {
		this.lock_security_1 = lock_security_1;
	}

	/**
	 * 返回: lock_security_2
	 */
	public Integer getLock_security_2() {
		return lock_security_2;
	}

	/**
	 * 设置:lock_security_2
	 */
	public void setLock_security_2(Integer lock_security_2) {
		this.lock_security_2 = lock_security_2;
	}

	/**
	 * 返回: lock_security_3
	 */
	public String getLock_security_3() {
		return lock_security_3;
	}

	/**
	 * 设置:lock_security_3
	 */
	public void setLock_security_3(String lock_security_3) {
		this.lock_security_3 = lock_security_3;
	}

	/**
	 * 返回: wall_gap_1
	 */
	public Integer getWall_gap_1() {
		return wall_gap_1;
	}

	/**
	 * 设置:wall_gap_1
	 */
	public void setWall_gap_1(Integer wall_gap_1) {
		this.wall_gap_1 = wall_gap_1;
	}

	/**
	 * 返回: wall_gap_2
	 */
	public Integer getWall_gap_2() {
		return wall_gap_2;
	}

	/**
	 * 设置:wall_gap_2
	 */
	public void setWall_gap_2(Integer wall_gap_2) {
		this.wall_gap_2 = wall_gap_2;
	}

	/**
	 * 返回: wall_gap_3
	 */
	public String getWall_gap_3() {
		return wall_gap_3;
	}

	/**
	 * 设置:wall_gap_3
	 */
	public void setWall_gap_3(String wall_gap_3) {
		this.wall_gap_3 = wall_gap_3;
	}

	/**
	 * 返回: wall_hollow_1
	 */
	public Integer getWall_hollow_1() {
		return wall_hollow_1;
	}

	/**
	 * 设置:wall_hollow_1
	 */
	public void setWall_hollow_1(Integer wall_hollow_1) {
		this.wall_hollow_1 = wall_hollow_1;
	}

	/**
	 * 返回: wall_hollow_2
	 */
	public Integer getWall_hollow_2() {
		return wall_hollow_2;
	}

	/**
	 * 设置:wall_hollow_2
	 */
	public void setWall_hollow_2(Integer wall_hollow_2) {
		this.wall_hollow_2 = wall_hollow_2;
	}

	/**
	 * 返回: wall_hollow_3
	 */
	public String getWall_hollow_3() {
		return wall_hollow_3;
	}

	/**
	 * 设置:wall_hollow_3
	 */
	public void setWall_hollow_3(String wall_hollow_3) {
		this.wall_hollow_3 = wall_hollow_3;
	}

	/**
	 * 返回: wall_loose_1
	 */
	public Integer getWall_loose_1() {
		return wall_loose_1;
	}

	/**
	 * 设置:wall_loose_1
	 */
	public void setWall_loose_1(Integer wall_loose_1) {
		this.wall_loose_1 = wall_loose_1;
	}

	/**
	 * 返回: wall_loose_2
	 */
	public Integer getWall_loose_2() {
		return wall_loose_2;
	}

	/**
	 * 设置:wall_loose_2
	 */
	public void setWall_loose_2(Integer wall_loose_2) {
		this.wall_loose_2 = wall_loose_2;
	}

	/**
	 * 返回: wall_loose_3
	 */
	public String getWall_loose_3() {
		return wall_loose_3;
	}

	/**
	 * 设置:wall_loose_3
	 */
	public void setWall_loose_3(String wall_loose_3) {
		this.wall_loose_3 = wall_loose_3;
	}

	/**
	 * 返回: wall_crack_1
	 */
	public Integer getWall_crack_1() {
		return wall_crack_1;
	}

	/**
	 * 设置:wall_crack_1
	 */
	public void setWall_crack_1(Integer wall_crack_1) {
		this.wall_crack_1 = wall_crack_1;
	}

	/**
	 * 返回: wall_crack_2
	 */
	public Integer getWall_crack_2() {
		return wall_crack_2;
	}

	/**
	 * 设置:wall_crack_2
	 */
	public void setWall_crack_2(Integer wall_crack_2) {
		this.wall_crack_2 = wall_crack_2;
	}

	/**
	 * 返回: wall_crack_3
	 */
	public String getWall_crack_3() {
		return wall_crack_3;
	}

	/**
	 * 设置:wall_crack_3
	 */
	public void setWall_crack_3(String wall_crack_3) {
		this.wall_crack_3 = wall_crack_3;
	}

	/**
	 * 返回: wall_drop_1
	 */
	public Integer getWall_drop_1() {
		return wall_drop_1;
	}

	/**
	 * 设置:wall_drop_1
	 */
	public void setWall_drop_1(Integer wall_drop_1) {
		this.wall_drop_1 = wall_drop_1;
	}

	/**
	 * 返回: wall_drop_2
	 */
	public Integer getWall_drop_2() {
		return wall_drop_2;
	}

	/**
	 * 设置:wall_drop_2
	 */
	public void setWall_drop_2(Integer wall_drop_2) {
		this.wall_drop_2 = wall_drop_2;
	}

	/**
	 * 返回: wall_drop_3
	 */
	public String getWall_drop_3() {
		return wall_drop_3;
	}

	/**
	 * 设置:wall_drop_3
	 */
	public void setWall_drop_3(String wall_drop_3) {
		this.wall_drop_3 = wall_drop_3;
	}

	/**
	 * 返回: wall_color_1
	 */
	public Integer getWall_color_1() {
		return wall_color_1;
	}

	/**
	 * 设置:wall_color_1
	 */
	public void setWall_color_1(Integer wall_color_1) {
		this.wall_color_1 = wall_color_1;
	}

	/**
	 * 返回: wall_color_2
	 */
	public Integer getWall_color_2() {
		return wall_color_2;
	}

	/**
	 * 设置:wall_color_2
	 */
	public void setWall_color_2(Integer wall_color_2) {
		this.wall_color_2 = wall_color_2;
	}

	/**
	 * 返回: wall_color_3
	 */
	public String getWall_color_3() {
		return wall_color_3;
	}

	/**
	 * 设置:wall_color_3
	 */
	public void setWall_color_3(String wall_color_3) {
		this.wall_color_3 = wall_color_3;
	}

	/**
	 * 返回: floor_gap_1
	 */
	public Integer getFloor_gap_1() {
		return floor_gap_1;
	}

	/**
	 * 设置:floor_gap_1
	 */
	public void setFloor_gap_1(Integer floor_gap_1) {
		this.floor_gap_1 = floor_gap_1;
	}

	/**
	 * 返回: floor_gap_2
	 */
	public Integer getFloor_gap_2() {
		return floor_gap_2;
	}

	/**
	 * 设置:floor_gap_2
	 */
	public void setFloor_gap_2(Integer floor_gap_2) {
		this.floor_gap_2 = floor_gap_2;
	}

	/**
	 * 返回: floor_gap_3
	 */
	public String getFloor_gap_3() {
		return floor_gap_3;
	}

	/**
	 * 设置:floor_gap_3
	 */
	public void setFloor_gap_3(String floor_gap_3) {
		this.floor_gap_3 = floor_gap_3;
	}

	/**
	 * 返回: floor_hollow_1
	 */
	public Integer getFloor_hollow_1() {
		return floor_hollow_1;
	}

	/**
	 * 设置:floor_hollow_1
	 */
	public void setFloor_hollow_1(Integer floor_hollow_1) {
		this.floor_hollow_1 = floor_hollow_1;
	}

	/**
	 * 返回: floor_hollow_2
	 */
	public Integer getFloor_hollow_2() {
		return floor_hollow_2;
	}

	/**
	 * 设置:floor_hollow_2
	 */
	public void setFloor_hollow_2(Integer floor_hollow_2) {
		this.floor_hollow_2 = floor_hollow_2;
	}

	/**
	 * 返回: floor_hollow_3
	 */
	public String getFloor_hollow_3() {
		return floor_hollow_3;
	}

	/**
	 * 设置:floor_hollow_3
	 */
	public void setFloor_hollow_3(String floor_hollow_3) {
		this.floor_hollow_3 = floor_hollow_3;
	}

	/**
	 * 返回: floor_loose_1
	 */
	public Integer getFloor_loose_1() {
		return floor_loose_1;
	}

	/**
	 * 设置:floor_loose_1
	 */
	public void setFloor_loose_1(Integer floor_loose_1) {
		this.floor_loose_1 = floor_loose_1;
	}

	/**
	 * 返回: floor_loose_2
	 */
	public Integer getFloor_loose_2() {
		return floor_loose_2;
	}

	/**
	 * 设置:floor_loose_2
	 */
	public void setFloor_loose_2(Integer floor_loose_2) {
		this.floor_loose_2 = floor_loose_2;
	}

	/**
	 * 返回: floor_loose_3
	 */
	public String getFloor_loose_3() {
		return floor_loose_3;
	}

	/**
	 * 设置:floor_loose_3
	 */
	public void setFloor_loose_3(String floor_loose_3) {
		this.floor_loose_3 = floor_loose_3;
	}


	/**
	 * 返回: customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * 设置:customer
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * 返回: appointment_time
	 */
	public Date getAppointment_time() {
		return appointment_time;
	}

	/**
	 * 设置:appointment_time
	 */
	public void setAppointment_time(Date appointment_time) {
		this.appointment_time = appointment_time;
	}

	/**
	 * 返回: start_time
	 */
	public Date getStart_time() {
		return start_time;
	}

	/**
	 * 设置:start_time
	 */
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	/**
	 * 返回: mainte_status
	 */
	public Integer getMainte_status() {
		return mainte_status;
	}

	/**
	 * 设置:mainte_status
	 */
	public void setMainte_status(Integer mainte_status) {
		this.mainte_status = mainte_status;
	}

	/**
	 * 返回: flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * 设置:flag
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * 返回: other_1
	 */
	public String getOther_1() {
		return other_1;
	}

	/**
	 * 设置:other_1
	 */
	public void setOther_1(String other_1) {
		this.other_1 = other_1;
	}

	/**
	 * 返回: result_1
	 */
	public String getResult_1() {
		return result_1;
	}

	/**
	 * 设置:result_1
	 */
	public void setResult_1(String result_1) {
		this.result_1 = result_1;
	}

	/**
	 * 返回: other_2
	 */
	public String getOther_2() {
		return other_2;
	}

	/**
	 * 设置:other_2
	 */
	public void setOther_2(String other_2) {
		this.other_2 = other_2;
	}

	/**
	 * 返回: result_2
	 */
	public String getResult_2() {
		return result_2;
	}

	/**
	 * 设置:result_2
	 */
	public void setResult_2(String result_2) {
		this.result_2 = result_2;
	}

	/**
	 * 返回: comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * 设置:comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * 返回: tinyVillageId
	 */
	public Long getTinyVillageId() {
		return tinyVillageId;
	}

	/**
	 * 设置:tinyVillageId
	 */
	public void setTinyVillageId(Long tinyVillageId) {
		this.tinyVillageId = tinyVillageId;
	}

	/**
	 * 返回: building_house_no
	 */
	public String getBuilding_house_no() {
		return building_house_no;
	}

	/**
	 * 设置:building_house_no
	 */
	public void setBuilding_house_no(String building_house_no) {
		this.building_house_no = building_house_no;
	}

	/**
	 * 返回: building_unit_no
	 */
	public String getBuilding_unit_no() {
		return building_unit_no;
	}

	/**
	 * 设置:building_unit_no
	 */
	public void setBuilding_unit_no(String building_unit_no) {
		this.building_unit_no = building_unit_no;
	}

	/**
	 * 返回: building_name
	 */
	public String getBuilding_name() {
		return building_name;
	}

	/**
	 * 设置:building_name
	 */
	public void setBuilding_name(String building_name) {
		this.building_name = building_name;
	}
	
	
	
	



		
}
