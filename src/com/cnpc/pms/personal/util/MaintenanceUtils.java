package com.cnpc.pms.personal.util;


import java.util.Date;

import javax.persistence.Column;

@SuppressWarnings("all")
public class MaintenanceUtils{
	//地址
	private String address;
	//检测开始时间
	private Date start_time;
	//姓名 
	private String name;
	//卧室数
	private Integer bedroom_num; 
	//客厅数
	private Integer livingroom_num;
	//卫生间数
	private Integer bathroom_num;
	//装修等级
	private String decorate_level;
	//////////静态拼接字段/////////
	private String bathroom_title;  	//卫生间
	private String house_title;     	//总信息
	private String switch_board_title;  //灯具与开关
	private String pipe_title;   		//排水标题
	private String electric_title;  	//弱电标题
	private String cust_title;  		//用户信息
	private String ret_info; 			//检测结果
	
	public String getRet_info() {
		String ret_info="    依据GB50325-2010《民用建筑工程室内环境污染控制规范" +
                "》中对Ⅱ类民用建筑工程室内环境污染物浓度限量" +
                "的规定，" + getAddress() + getDecorate_level() + "室内空气质量合格。";
		return ret_info;
	}
	public String getCust_title() {
		String cust_title="        " + getAddress() + getDecorate_level();
		return cust_title;
	}
	public String getElectric_title() {
		String electric_title="根据GB50303-2002《电气工程质量验收规范》、DB11/883-2012" +
                "《建筑弱电工程施工及验收规范》规定，" + getAddress() + "强弱电质量如下表。";
		return electric_title;
	}

	public String getPipe_title() {
		String pipe_title="依据GB50268-2009《给排水管道工程施工及验收规范》" +
                "规定，" + getAddress() + "给排水质量如下表。";
		return pipe_title;
	}
	public String getSwitch_board_title() {
		String switch_board_title=" 依据GB50303-2002《电气工程质量验收规范》" +
                "规定，" + getAddress() + "灯具与开关质量如下表。";
		return switch_board_title;
	}
	public String getHouse_title() {
		String house_title = "        " + getName() + "委托我单位对" + getAddress() + "室内空气质量进行检测,共在" +
                "卧室设置" + getBedroom_num() + "个点,客厅设置" + getLivingroom_num() + "个点,卫生间设置" + getBathroom_num() + "个点,厨房设置1个点.";;
		return house_title;
	}
	public String getBathroom_title() {
		String bathroom_title = "依据GB50210-2001《建筑装饰装修工程质量验收规范》规定，" + getAddress() + "卫生间质量如下表。";
		return bathroom_title;
	}
	/*
	 * 卫生间检测
	 */
	//卫生间坡度状态
	private Integer bathroom_slope_1;
	//卫生间坡度建议
	private Integer bathroom_slope_2;
	//卫生间坡度备注
	private String bathroom_slope_3;
	//卫生间防水层状态
	private Integer bathroom_waterproof_1;
	//卫生间防水层建议
	private Integer bathroom_waterproof_2;
	//卫生间防水层备注
	private String bathroom_waterproof_3;
	//淋浴设施状态
	private Integer shower_1;
	//淋浴设施建议
	@Column(name="shower_2")
	private Integer shower_2;
	//淋浴设施备注
	private String shower_3;
	//洗衣机热水器进出水口位置状态
	private Integer washmachine_outlet_1;
	//洗衣机热水器进出水口位置建议
	private Integer washmachine_outlet_2;
	//洗衣机热水器进出水口位置备注
	private String washmachine_outlet_3;
	//洁具封胶检测状态
	private Integer toilet_sealing_1;
	//洁具封胶检测建议
	private Integer toilet_sealing_2;
	//洁具封胶检测备注
	private String toilet_sealing_3;
	/*
	 * 厨房检测
	 */
	//燃气灶具状态
	private Integer gas_cooker_1;
	//燃气灶具建议
	private Integer gas_cooker_2;
	//燃气灶具备注
	private String gas_cooker_3;
	//台面封胶检测状态
	private Integer mesa_sealing_1;
	//台面封胶检测建议
	private Integer mesa_sealing_2;
	//台面封胶检测备注
	private String mesa_sealing_3;
	/*
	 * 灯具与开关检测
	 */
	//开关面板状态
	private Integer switch_board_1;
	//开关面板建议
	private Integer switch_board_2;
	//开关面板备注
	private String switch_board_3;
	//开关线路状态
	private Integer switch_wire_1;
	//开关线路建议
	private Integer switch_wire_2;
	//开关线路备注
	private String switch_wire_3;
	//厨卫插座防水盒状态
	private Integer waterproof_box_1;
	//厨卫插座防水盒建议
	private Integer waterproof_box_2;
	//厨卫插座防水盒备注
	private String waterproof_box_3;
	//灯具接头牢固状态
	private Integer lamp_secure_1;
	//灯具接头牢固建议
	private Integer lamp_secure_2;
	//灯具接头牢固备注
	private String lamp_secure_3;
	//灯具接头绝缘状态
	private Integer lamp_insulate_1;
	//灯具接头绝缘建议
	private Integer lamp_insulate_2;
	//灯具接头绝缘备注
	private String lamp_insulate_3;
	//灯具照度情况状态
	private Integer lamp_illumination_1;
	//灯具照度情况建议
	private Integer lamp_illumination_2;
	//灯具照度情况备注
	private String lamp_illumination_3;
	/*
	 * 水龙头、下水检测
	 */
	//排水管道状态
	private Integer pipe_1;
	//排水管道建议
	private Integer pipe_2;
	//排水管道备注
	private String pipe_3;
	//水管固定情况状态
	private Integer pipe_fix_1;
	//水管固定情况建议
	private Integer pipe_fix_2;
	//水管固定情况备注
	private String pipe_fix_3;
	//地漏定位状态
	private Integer drain_location_1;
	//地漏定位建议
	private Integer drain_location_2;
	//地漏定位备注
	private String drain_location_3;
	//地漏密闭性状态
	private Integer drain_tight_1;
	//地漏密闭性建议
	private Integer drain_tight_2;
	//地漏密闭性备注
	private String drain_tight_3;
	//水龙头状态
	private Integer stopcock_1;
	//水龙头建议
	private Integer stopcock_2;
	//水龙头备注
	private String stopcock_3;
	//阀门与龙头接头状态
	private Integer valve_joint_1;
	//阀门与龙头接头建议
	private Integer valve_joint_2;
	//阀门与龙头接头备注
	private String valve_joint_3;
	//面盆下水状态
	private Integer basin_sewer_1;
	//面盆下水建议
	private Integer basin_sewer_2;
	//面盆下水备注
	private String basin_sewer_3;
	//洗菜盆下水状态
	private Integer kitchen_sewer_1;
	//洗菜盆下水建议
	private Integer kitchen_sewer_2;
	//洗菜盆下水备注
	private String kitchen_sewer_3;
	/*
	 * 墙体检测
	 */
	//墙砖勾缝状态
	private Integer wall_gap_1;
	//墙砖勾缝建议
	private Integer wall_gap_2;
	//墙砖勾缝备注
	private String wall_gap_3;
	//墙砖空鼓状态
	private Integer wall_hollow_1;
	//墙砖空鼓建议
	private Integer wall_hollow_2;
	//墙砖空鼓备注
	private String wall_hollow_3;
	//墙砖松动状态
	private Integer wall_loose_1;
	//墙砖松动建议
	private Integer wall_loose_2;
	//墙砖松动备注
	private String wall_loose_3;
	//墙皮开裂状态
	private Integer wall_crack_1;
	//墙皮开裂建议
	private Integer wall_crack_2;
	//墙皮开裂备注
	private String wall_crack_3;
	//墙皮脱落状态
	private Integer wall_drop_1;
	//墙皮脱落建议
	private Integer wall_drop_2;
	//墙皮脱落备注
	private String wall_drop_3;
	//墙皮变色状态
	private Integer wall_color_1;
	//墙皮变色建议
	private Integer wall_color_2;
	//墙皮变色备注
	private String wall_color_3;
	/*
	 * 地面检测
	 */
	//地砖勾缝状态
	private Integer floor_gap_1;
	//地砖勾缝建议
	private Integer floor_gap_2;
	//地砖勾缝备注
	private String floor_gap_3;
	//地砖空鼓状态
	private Integer floor_hollow_1;
	//地砖空鼓建议
	private Integer floor_hollow_2;
	//地砖空鼓备注
	private String floor_hollow_3;
	//地砖松动状态
	private Integer floor_loose_1;
	//地砖松动建议
	private Integer floor_loose_2;
	//地砖松动备注
	private String floor_loose_3;
	/*
	 * 强弱电、功率检测
	 */
	//强弱电间距状态
	private Integer electric_distance_1;
	//强弱电间距建议
	private Integer electric_distance_2;
	//强弱电间距备注
	private String electric_distance_3;
	//强弱电排布状态
	private Integer electric_arrange_1;
	//强弱电排布建议
	private Integer electric_arrange_2;
	//强弱电排布备注
	private String electric_arrange_3;
	//水电气交叉状态
	private Integer electric_water_cross_1;
	//水电气交叉建议
	private Integer electric_water_cross_2;
	//水电气交叉备注
	private String electric_water_cross_3;
	//配电箱功率状态
	private Integer distribution_power_1;
	//配电箱功率建议
	private Integer distribution_power_2;
	//配电箱功率备注
	private String distribution_power_3;
	//空调电路功率状态
	private Integer aircondition_power_1;
	//空调电路功率建议
	private Integer aircondition_power_2;
	//空调电路功率备注
	private String aircondition_power_3;

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getStart_time() {
		return start_time;
	}
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}
	public Integer getBathroom_slope_1() {
		return bathroom_slope_1;
	}
	public String getBathroom_slope_1_t(){
		String t_bathroom_slope_1 = "";
		if(getBathroom_slope_1()!=null){
			t_bathroom_slope_1=getBathroom_slope_1().equals(1)?"正常":getBathroom_slope_1().equals(2)?"无 ":getBathroom_slope_1().equals(3)?"反向":"";
		}
		return t_bathroom_slope_1;
	}
	public void setBathroom_slope_1(Integer bathroom_slope_1) {
		this.bathroom_slope_1 = bathroom_slope_1;
	}
	public Integer getBathroom_slope_2() {
		return bathroom_slope_2;
	}
	public String getBathroom_slope_2_t(){
		String t_bathroom_slope_2 = "";
		if(getBathroom_slope_2()!=null){
			t_bathroom_slope_2=getBathroom_slope_2().equals(1)?"无需处理":getBathroom_slope_2().equals(2)?"整改 ":"";
		}
		return t_bathroom_slope_2;
	}
	public void setBathroom_slope_2(Integer bathroom_slope_2) {
		this.bathroom_slope_2 = bathroom_slope_2;
	}
	public String getBathroom_slope_3() {
		return bathroom_slope_3;
	}
	public void setBathroom_slope_3(String bathroom_slope_3) {
		this.bathroom_slope_3 = bathroom_slope_3;
	}
	public Integer getBathroom_waterproof_1() {
		return bathroom_waterproof_1;
	}
	public String getBathroom_waterproof_1_t(){
		String t_bathroom_waterproof_1="";
		if(getBathroom_waterproof_1()!=null){
			t_bathroom_waterproof_1=getBathroom_waterproof_1().equals(1)?"完好":getBathroom_waterproof_1().equals(2)?"破损 ":"";
		}
		return t_bathroom_waterproof_1;
	}
	public void setBathroom_waterproof_1(Integer bathroom_waterproof_1) {
		this.bathroom_waterproof_1 = bathroom_waterproof_1;
	}
	public Integer getBathroom_waterproof_2() {
		return bathroom_waterproof_2;
	}
	public String getBathroom_waterproof_2_t(){
		String t_bathroom_waterproof_2="";
		if(getBathroom_waterproof_2()!=null){
			t_bathroom_waterproof_2=getBathroom_waterproof_2().equals(1)?"无需处理":getBathroom_waterproof_2().equals(2)?"整改 ":"";
		}
		return t_bathroom_waterproof_2;
	}
	public void setBathroom_waterproof_2(Integer bathroom_waterproof_2) {
		this.bathroom_waterproof_2 = bathroom_waterproof_2;
	}
	public String getBathroom_waterproof_3() {
		return bathroom_waterproof_3;
	}
	public void setBathroom_waterproof_3(String bathroom_waterproof_3) {
		this.bathroom_waterproof_3 = bathroom_waterproof_3;
	}
	public Integer getShower_1() {
		return shower_1;
	}
	public String getShower_1_t(){
		String t_getShower_1 = "";
		if(getShower_1()!=null){
			t_getShower_1=getShower_1().equals(1)?"完好":getShower_1().equals(2)?"渗水 ":getShower_1().equals(3)?"漏水":"";
		}
		return t_getShower_1;
	}
	public void setShower_1(Integer shower_1) {
		this.shower_1 = shower_1;
	}
	public Integer getShower_2() {
		return shower_2;
	}
	public String getShower_2_t(){
		String t_getShower_2="";
		if(getShower_2()!=null){
			t_getShower_2=getShower_2().equals(1)?"无需处理":getShower_2().equals(2)?"整改 ":"";
		}
		return t_getShower_2;
	}
	public void setShower_2(Integer shower_2) {
		this.shower_2 = shower_2;
	}
	public String getShower_3() {
		return shower_3;
	}
	public void setShower_3(String shower_3) {
		this.shower_3 = shower_3;
	}
	public Integer getWashmachine_outlet_1() {
		return washmachine_outlet_1;
	}
	public String getWashmachine_outlet_1_t(){
		String t_getWashmachine_outlet_1="";
		if(getWashmachine_outlet_1()!=null){
			t_getWashmachine_outlet_1=getWashmachine_outlet_1().equals(1)?"合理":getWashmachine_outlet_1().equals(2)?"不合理 ":"";
		}
		return t_getWashmachine_outlet_1;
	}
	public void setWashmachine_outlet_1(Integer washmachine_outlet_1) {
		this.washmachine_outlet_1 = washmachine_outlet_1;
	}
	public Integer getWashmachine_outlet_2() {
		return washmachine_outlet_2;
	}
	public String getWashmachine_outlet_2_t(){
		String t_getWashmachine_outlet_2="";
		if(getWashmachine_outlet_2()!=null){
			t_getWashmachine_outlet_2=getWashmachine_outlet_2().equals(1)?"无需处理":getWashmachine_outlet_2().equals(2)?"整改 ":"";
		}
		return t_getWashmachine_outlet_2;
	}
	public void setWashmachine_outlet_2(Integer washmachine_outlet_2) {
		this.washmachine_outlet_2 = washmachine_outlet_2;
	}
	public String getWashmachine_outlet_3() {
		return washmachine_outlet_3;
	}
	public void setWashmachine_outlet_3(String washmachine_outlet_3) {
		this.washmachine_outlet_3 = washmachine_outlet_3;
	}
	public Integer getToilet_sealing_1() {
		return toilet_sealing_1;
	}
	public String getToilet_sealing_1_t(){
		String t_getToilet_sealing_1 = "";
		if(getShower_1()!=null){
			t_getToilet_sealing_1=getToilet_sealing_1().equals(1)?"严密":getToilet_sealing_1().equals(2)?"渗水 ":getToilet_sealing_1().equals(3)?"漏水":"";
		}
		return t_getToilet_sealing_1;
	}
	public void setToilet_sealing_1(Integer toilet_sealing_1) {
		this.toilet_sealing_1 = toilet_sealing_1;
	}
	public Integer getToilet_sealing_2() {
		return toilet_sealing_2;
	}
	public String getToilet_sealing_2_t(){
		String t_getToilet_sealing_2="";
		if(getToilet_sealing_2()!=null){
			t_getToilet_sealing_2=getToilet_sealing_2().equals(1)?"无需处理":getToilet_sealing_2().equals(2)?"整改 ":"";
		}
		return t_getToilet_sealing_2;
	}
	public void setToilet_sealing_2(Integer toilet_sealing_2) {
		this.toilet_sealing_2 = toilet_sealing_2;
	}
	public String getToilet_sealing_3() {
		return toilet_sealing_3;
	}
	public void setToilet_sealing_3(String toilet_sealing_3) {
		this.toilet_sealing_3 = toilet_sealing_3;
	}
	public Integer getGas_cooker_1() {
		return gas_cooker_1;
	}
	public String getGas_cooker_1_t(){
		String t_getGas_cooker_1="";
		if(getGas_cooker_1()!=null){
			t_getGas_cooker_1=getGas_cooker_1().equals(1)?"完好":getGas_cooker_1().equals(2)?"老化 ":"";
		}
		return t_getGas_cooker_1;
	}
	public void setGas_cooker_1(Integer gas_cooker_1) {
		this.gas_cooker_1 = gas_cooker_1;
	}
	public Integer getGas_cooker_2() {
		return gas_cooker_2;
	}
	public String getGas_cooker_2_t(){
		String t_getGas_cooker_2="";
		if(getGas_cooker_2()!=null){
			t_getGas_cooker_2=getGas_cooker_2().equals(1)?"无需处理":getGas_cooker_2().equals(2)?"整改 ":"";
		}
		return t_getGas_cooker_2;
	}
	public void setGas_cooker_2(Integer gas_cooker_2) {
		this.gas_cooker_2 = gas_cooker_2;
	}
	public String getGas_cooker_3() {
		return gas_cooker_3;
	}
	public void setGas_cooker_3(String gas_cooker_3) {
		this.gas_cooker_3 = gas_cooker_3;
	}
	public Integer getMesa_sealing_1() {
		return mesa_sealing_1;
	}
	public String getMesa_sealing_1_t(){
		String t_getMesa_sealing_1 = "";
		if(getMesa_sealing_1()!=null){
			t_getMesa_sealing_1=getMesa_sealing_1().equals(1)?"严密":getMesa_sealing_1().equals(2)?"渗水 ":getMesa_sealing_1().equals(3)?"漏水":"";
		}
		return t_getMesa_sealing_1;
	}
	public void setMesa_sealing_1(Integer mesa_sealing_1) {
		this.mesa_sealing_1 = mesa_sealing_1;
	}
	public Integer getMesa_sealing_2() {
		return mesa_sealing_2;
	}
	public String getMesa_sealing_2_t(){
		String t_getMesa_sealing_2="";
		if(getMesa_sealing_2()!=null){
			t_getMesa_sealing_2=getMesa_sealing_2().equals(1)?"无需处理":getMesa_sealing_2().equals(2)?"整改 ":"";
		}
		return t_getMesa_sealing_2;
	}
	public void setMesa_sealing_2(Integer mesa_sealing_2) {
		this.mesa_sealing_2 = mesa_sealing_2;
	}
	public String getMesa_sealing_3() {
		return mesa_sealing_3;
	}
	public void setMesa_sealing_3(String mesa_sealing_3) {
		this.mesa_sealing_3 = mesa_sealing_3;
	}
	public Integer getSwitch_board_1() {
		return switch_board_1;
	}
	public String getSwitch_board_1_t(){
		String t_getSwitch_board_1 = "";
		if(getMesa_sealing_1()!=null){
			t_getSwitch_board_1=getSwitch_board_1().equals(1)?"完好":getSwitch_board_1().equals(2)?"松动 ":getSwitch_board_1().equals(3)?"破损":getSwitch_board_1().equals(4)?"脱落":"";
		}
		return t_getSwitch_board_1;
	}
	public void setSwitch_board_1(Integer switch_board_1) {
		this.switch_board_1 = switch_board_1;
	}
	public Integer getSwitch_board_2() {
		return switch_board_2;
	}
	public String getSwitch_board_2_t(){
		String t_getSwitch_board_2="";
		if(getSwitch_board_2()!=null){
			t_getSwitch_board_2=getSwitch_board_2().equals(1)?"无需处理":getSwitch_board_2().equals(2)?"整改 ":"";
		}
		return t_getSwitch_board_2;
	}
	public void setSwitch_board_2(Integer switch_board_2) {
		this.switch_board_2 = switch_board_2;
	}
	public String getSwitch_board_3() {
		return switch_board_3;
	}
	public void setSwitch_board_3(String switch_board_3) {
		this.switch_board_3 = switch_board_3;
	}
	public Integer getSwitch_wire_1() {
		return switch_wire_1;
	}
	public String getSwitch_wire_1_t(){
		String t_getSwitch_wire_1="";
		if(getSwitch_board_2()!=null){
			t_getSwitch_wire_1=getSwitch_wire_1().equals(1)?"合规":getSwitch_wire_1().equals(2)?"不合规":"";
		}
		return t_getSwitch_wire_1;
	}
	public void setSwitch_wire_1(Integer switch_wire_1) {
		this.switch_wire_1 = switch_wire_1;
	}
	public Integer getSwitch_wire_2() {
		return switch_wire_2;
	}
	public String getSwitch_wire_2_t(){
		String t_getSwitch_wire_2="";
		if(getSwitch_board_2()!=null){
			t_getSwitch_wire_2=getSwitch_wire_2().equals(1)?"无需处理":getSwitch_wire_2().equals(2)?"整改 ":"";
		}
		return t_getSwitch_wire_2;
	}
	public void setSwitch_wire_2(Integer switch_wire_2) {
		this.switch_wire_2 = switch_wire_2;
	}
	public String getSwitch_wire_3() {
		return switch_wire_3;
	}
	public void setSwitch_wire_3(String switch_wire_3) {
		this.switch_wire_3 = switch_wire_3;
	}
	public Integer getWaterproof_box_1() {
		return waterproof_box_1;
	}
	public String getWaterproof_box_1_t(){
		String t_getWaterproof_box_1="";
		if(getSwitch_board_2()!=null){
			t_getWaterproof_box_1=getWaterproof_box_1().equals(1)?"合规":getWaterproof_box_1().equals(2)?"不合规":"";
		}
		return t_getWaterproof_box_1;
	}
	public void setWaterproof_box_1(Integer waterproof_box_1) {
		this.waterproof_box_1 = waterproof_box_1;
	}
	public Integer getWaterproof_box_2() {
		return waterproof_box_2;
	}
	public String getWaterproof_box_2_t(){
		String t_getWaterproof_box_2="";
		if(getSwitch_board_2()!=null){
			t_getWaterproof_box_2=getWaterproof_box_2().equals(1)?"无需处理":getWaterproof_box_2().equals(2)?"整改 ":"";
		}
		return t_getWaterproof_box_2;
	}
	public void setWaterproof_box_2(Integer waterproof_box_2) {
		this.waterproof_box_2 = waterproof_box_2;
	}
	public String getWaterproof_box_3() {
		return waterproof_box_3;
	}
	public void setWaterproof_box_3(String waterproof_box_3) {
		this.waterproof_box_3 = waterproof_box_3;
	}
	public Integer getLamp_secure_1() {
		return lamp_secure_1;
	}
	public String getLamp_secure_1_t(){
		String t_getLamp_secure_1 = "";
		if(getMesa_sealing_1()!=null){
			t_getLamp_secure_1=getLamp_secure_1().equals(1)?"合规":getLamp_secure_1().equals(2)?"松动 ":getLamp_secure_1().equals(3)?"不合规":"";
		}
		return t_getLamp_secure_1;
	}
	public void setLamp_secure_1(Integer lamp_secure_1) {
		this.lamp_secure_1 = lamp_secure_1;
	}
	public Integer getLamp_secure_2() {
		return lamp_secure_2;
	}
	public String getLamp_secure_2_t(){
		String t_getLamp_secure_2="";
		if(getLamp_secure_2()!=null){
			t_getLamp_secure_2=getLamp_secure_2().equals(1)?"无需处理":getLamp_secure_2().equals(2)?"整改 ":"";
		}
		return t_getLamp_secure_2;
	}
	public void setLamp_secure_2(Integer lamp_secure_2) {
		this.lamp_secure_2 = lamp_secure_2;
	}
	public String getLamp_secure_3() {
		return lamp_secure_3;
	}
	public void setLamp_secure_3(String lamp_secure_3) {
		this.lamp_secure_3 = lamp_secure_3;
	}
	public Integer getLamp_insulate_1() {
		return lamp_insulate_1;
	}
	public String getLamp_insulate_1_t(){
		String t_getLamp_insulate_1="";
		if(getLamp_insulate_1()!=null){
			t_getLamp_insulate_1=getLamp_insulate_1().equals(1)?"合规":getLamp_insulate_1().equals(2)?"不合规":"";
		}
		return t_getLamp_insulate_1;
	}
	public void setLamp_insulate_1(Integer lamp_insulate_1) {
		this.lamp_insulate_1 = lamp_insulate_1;
	}
	public Integer getLamp_insulate_2() {
		return lamp_insulate_2;
	}
	public String getLamp_insulate_2_t(){
		String t_getLamp_insulate_2="";
		if(getLamp_insulate_2()!=null){
			t_getLamp_insulate_2=getLamp_insulate_2().equals(1)?"无需处理":getLamp_insulate_2().equals(2)?"整改 ":"";
		}
		return t_getLamp_insulate_2;
	}
	public void setLamp_insulate_2(Integer lamp_insulate_2) {
		this.lamp_insulate_2 = lamp_insulate_2;
	}
	public String getLamp_insulate_3() {
		return lamp_insulate_3;
	}
	public void setLamp_insulate_3(String lamp_insulate_3) {
		this.lamp_insulate_3 = lamp_insulate_3;
	}
	public Integer getLamp_illumination_1() {
		return lamp_illumination_1;
	}
	public String getLamp_illumination_1_t(){
		String t_getLamp_illumination_1 = "";
		if(getLamp_illumination_1()!=null){
			t_getLamp_illumination_1=getLamp_illumination_1().equals(1)?"正常":getLamp_illumination_1().equals(2)?"变暗 ":getLamp_illumination_1().equals(3)?"异响":"";
		}
		return t_getLamp_illumination_1;
	}
	public void setLamp_illumination_1(Integer lamp_illumination_1) {
		this.lamp_illumination_1 = lamp_illumination_1;
	}
	public Integer getLamp_illumination_2() {
		return lamp_illumination_2;
	}
	public String getLamp_illumination_2_t(){
		String t_getLamp_illumination_2="";
		if(getLamp_illumination_2()!=null){
			t_getLamp_illumination_2=getLamp_illumination_2().equals(1)?"无需处理":getLamp_illumination_2().equals(2)?"整改 ":"";
		}
		return t_getLamp_illumination_2;
	}
	public void setLamp_illumination_2(Integer lamp_illumination_2) {
		this.lamp_illumination_2 = lamp_illumination_2;
	}
	public String getLamp_illumination_3() {
		return lamp_illumination_3;
	}
	public void setLamp_illumination_3(String lamp_illumination_3) {
		this.lamp_illumination_3 = lamp_illumination_3;
	}
	public Integer getPipe_1() {
		return pipe_1;
	}
	public String getPipe_1_t(){
		String t_getPipe_1 = "";
		if(getPipe_1()!=null){
			t_getPipe_1=getPipe_1().equals(1)?"完好":getPipe_1().equals(2)?"渗水 ":getPipe_1().equals(3)?"漏水":"";
		}
		return t_getPipe_1;
	}
	public void setPipe_1(Integer pipe_1) {
		this.pipe_1 = pipe_1;
	}
	public Integer getPipe_2() {
		return pipe_2;
	}
	public String getPipe_2_t(){
		String t_getPipe_2="";
		if(getPipe_2()!=null){
			t_getPipe_2=getPipe_2().equals(1)?"无需处理":getPipe_2().equals(2)?"整改 ":"";
		}
		return t_getPipe_2;
	}
	public void setPipe_2(Integer pipe_2) {
		this.pipe_2 = pipe_2;
	}
	public String getPipe_3() {
		return pipe_3;
	}
	public void setPipe_3(String pipe_3) {
		this.pipe_3 = pipe_3;
	}
	public Integer getPipe_fix_1() {
		return pipe_fix_1;
	}
	public String getPipe_fix_1_t(){
		String t_getPipe_fix_1 = "";
		if(getPipe_fix_1()!=null){
			t_getPipe_fix_1=getPipe_fix_1().equals(1)?"合规":getPipe_fix_1().equals(2)?"松动 ":getPipe_fix_1().equals(3)?"不合格":"";
		}
		return t_getPipe_fix_1;
	}
	public void setPipe_fix_1(Integer pipe_fix_1) {
		this.pipe_fix_1 = pipe_fix_1;
	}
	public Integer getPipe_fix_2() {
		return pipe_fix_2;
	}
	public String getPipe_fix_2_t(){
		String t_getPipe_fix_2="";
		if(getPipe_fix_2()!=null){
			t_getPipe_fix_2=getPipe_fix_2().equals(1)?"无需处理":getPipe_fix_2().equals(2)?"整改 ":"";
		}
		return t_getPipe_fix_2;
	}
	public void setPipe_fix_2(Integer pipe_fix_2) {
		this.pipe_fix_2 = pipe_fix_2;
	}
	public String getPipe_fix_3() {
		return pipe_fix_3;
	}
	public void setPipe_fix_3(String pipe_fix_3) {
		this.pipe_fix_3 = pipe_fix_3;
	}
	public Integer getDrain_location_1() {
		return drain_location_1;
	}
	public String getDrain_location_1_t(){
		String t_getDrain_location_1="";
		if(getDrain_location_1()!=null){
			t_getDrain_location_1=getDrain_location_1().equals(1)?"合规":getDrain_location_1().equals(2)?"不合规":"";
		}
		return t_getDrain_location_1;
	}
	
	public void setDrain_location_1(Integer drain_location_1) {
		this.drain_location_1 = drain_location_1;
	}
	public Integer getDrain_location_2() {
		return drain_location_2;
	}
	public String getDrain_location_2_t(){
		String t_getDrain_location_2="";
		if(getDrain_location_2()!=null){
			t_getDrain_location_2=getDrain_location_2().equals(1)?"无需处理":getDrain_location_2().equals(2)?"整改 ":"";
		}
		return t_getDrain_location_2;
	}
	public void setDrain_location_2(Integer drain_location_2) {
		this.drain_location_2 = drain_location_2;
	}
	public String getDrain_location_3() {
		return drain_location_3;
	}
	public void setDrain_location_3(String drain_location_3) {
		this.drain_location_3 = drain_location_3;
	}
	public Integer getDrain_tight_1() {
		return drain_tight_1;
	}
	public String getDrain_tight_1_t(){
		String t_getDrain_tight_1="";
		if(getDrain_tight_1()!=null){
			t_getDrain_tight_1=getDrain_tight_1().equals(1)?"完好":getDrain_tight_1().equals(2)?"反味 ":"";
		}
		return t_getDrain_tight_1;
	}
	
	public void setDrain_tight_1(Integer drain_tight_1) {
		this.drain_tight_1 = drain_tight_1;
	}
	public Integer getDrain_tight_2() {
		return drain_tight_2;
	}
	public String getDrain_tight_2_t(){
		String t_getDrain_tight_2="";
		if(getDrain_tight_2()!=null){
			t_getDrain_tight_2=getDrain_tight_2().equals(1)?"无需处理":getDrain_tight_2().equals(2)?"整改 ":"";
		}
		return t_getDrain_tight_2;
	}
	public void setDrain_tight_2(Integer drain_tight_2) {
		this.drain_tight_2 = drain_tight_2;
	}
	public String getDrain_tight_3() {
		return drain_tight_3;
	}
	public void setDrain_tight_3(String drain_tight_3) {
		this.drain_tight_3 = drain_tight_3;
	}
	public Integer getStopcock_1() {
		return stopcock_1;
	}
	public String getStopcock_1_t(){
		String t_getStopcock_1 = "";
		if(getStopcock_1()!=null){
			t_getStopcock_1=getStopcock_1().equals(1)?"合规":getStopcock_1().equals(2)?"漏水 ":getStopcock_1().equals(3)?"不合格":"";
		}
		return t_getStopcock_1;
	}
	public void setStopcock_1(Integer stopcock_1) {
		this.stopcock_1 = stopcock_1;
	}
	public Integer getStopcock_2() {
		return stopcock_2;
	}
	public String getStopcock_2_t(){
		String t_getStopcock_2="";
		if(getStopcock_2()!=null){
			t_getStopcock_2=getStopcock_2().equals(1)?"无需处理":getStopcock_2().equals(2)?"整改 ":"";
		}
		return t_getStopcock_2;
	}
	public void setStopcock_2(Integer stopcock_2) {
		this.stopcock_2 = stopcock_2;
	}
	public String getStopcock_3() {
		return stopcock_3;
	}
	public void setStopcock_3(String stopcock_3) {
		this.stopcock_3 = stopcock_3;
	}
	public Integer getValve_joint_1() {
		return valve_joint_1;
	}
	public String getValve_joint_1_t(){
		String t_getValve_joint_1 = "";
		if(getValve_joint_1()!=null){
			t_getValve_joint_1=getValve_joint_1().equals(1)?"合规":getValve_joint_1().equals(2)?"漏水 ":getValve_joint_1().equals(3)?"不合格":"";
		}
		return t_getValve_joint_1;
	}
	public void setValve_joint_1(Integer valve_joint_1) {
		this.valve_joint_1 = valve_joint_1;
	}
	public Integer getValve_joint_2() {
		return valve_joint_2;
	}
	public String getValve_joint_2_t(){
		String t_getValve_joint_2="";
		if(getValve_joint_2()!=null){
			t_getValve_joint_2=getValve_joint_2().equals(1)?"无需处理":getValve_joint_2().equals(2)?"整改 ":"";
		}
		return t_getValve_joint_2;
	}
	public void setValve_joint_2(Integer valve_joint_2) {
		this.valve_joint_2 = valve_joint_2;
	}
	public String getValve_joint_3() {
		return valve_joint_3;
	}
	public void setValve_joint_3(String valve_joint_3) {
		this.valve_joint_3 = valve_joint_3;
	}
	public Integer getBasin_sewer_1() {
		return basin_sewer_1;
	}
	public String getBasin_sewer_1_t(){
		String t_getBasin_sewer_1 = "";
		if(getBasin_sewer_1()!=null){
			t_getBasin_sewer_1=getBasin_sewer_1().equals(1)?"完好":getBasin_sewer_1().equals(2)?"渗水 ":getBasin_sewer_1().equals(3)?"漏水":"";
		}
		return t_getBasin_sewer_1;
	}
	public void setBasin_sewer_1(Integer basin_sewer_1) {
		this.basin_sewer_1 = basin_sewer_1;
	}
	public Integer getBasin_sewer_2() {
		return basin_sewer_2;
	}
	public String getBasin_sewer_2_t(){
		String t_getBasin_sewer_2="";
		if(getBasin_sewer_2()!=null){
			t_getBasin_sewer_2=getBasin_sewer_2().equals(1)?"无需处理":getBasin_sewer_2().equals(2)?"整改 ":"";
		}
		return t_getBasin_sewer_2;
	}
	public void setBasin_sewer_2(Integer basin_sewer_2) {
		this.basin_sewer_2 = basin_sewer_2;
	}
	public String getBasin_sewer_3() {
		return basin_sewer_3;
	}
	public void setBasin_sewer_3(String basin_sewer_3) {
		this.basin_sewer_3 = basin_sewer_3;
	}
	public Integer getKitchen_sewer_1() {
		return kitchen_sewer_1;
	}
	public String getKitchen_sewer_1_t(){
		String t_getKitchen_sewer_1 = "";
		if(getKitchen_sewer_1()!=null){
			t_getKitchen_sewer_1=getKitchen_sewer_1().equals(1)?"完好":getKitchen_sewer_1().equals(2)?"渗水 ":getKitchen_sewer_1().equals(3)?"漏水":"";
		}
		return t_getKitchen_sewer_1;
	}
	public void setKitchen_sewer_1(Integer kitchen_sewer_1) {
		this.kitchen_sewer_1 = kitchen_sewer_1;
	}
	public Integer getKitchen_sewer_2() {
		return kitchen_sewer_2;
	}
	public String getKitchen_sewer_2_t(){
		String t_getKitchen_sewer_2="";
		if(getKitchen_sewer_2()!=null){
			t_getKitchen_sewer_2=getKitchen_sewer_2().equals(1)?"无需处理":getKitchen_sewer_2().equals(2)?"整改 ":"";
		}
		return t_getKitchen_sewer_2;
	}
	public void setKitchen_sewer_2(Integer kitchen_sewer_2) {
		this.kitchen_sewer_2 = kitchen_sewer_2;
	}
	public String getKitchen_sewer_3() {
		return kitchen_sewer_3;
	}
	public void setKitchen_sewer_3(String kitchen_sewer_3) {
		this.kitchen_sewer_3 = kitchen_sewer_3;
	}
	public Integer getWall_gap_1() {
		return wall_gap_1;
	}
	public String getWall_gap_1_t(){
		String t_getWall_gap_1 = "";
		if(getWall_gap_1()!=null){
			t_getWall_gap_1=getWall_gap_1().equals(1)?"完好":getWall_gap_1().equals(2)?"不饱满 ":getWall_gap_1().equals(3)?"不防水":"";
		}
		return t_getWall_gap_1;
	}
	public void setWall_gap_1(Integer wall_gap_1) {
		this.wall_gap_1 = wall_gap_1;
	}
	public Integer getWall_gap_2() {
		return wall_gap_2;
	}
	public String getWall_gap_2_t(){
		String t_getWall_gap_2="";
		if(getWall_gap_2()!=null){
			t_getWall_gap_2=getWall_gap_2().equals(1)?"无需处理":getWall_gap_2().equals(2)?"整改 ":"";
		}
		return t_getWall_gap_2;
	}
	public void setWall_gap_2(Integer wall_gap_2) {
		this.wall_gap_2 = wall_gap_2;
	}
	public String getWall_gap_3() {
		return wall_gap_3;
	}
	public void setWall_gap_3(String wall_gap_3) {
		this.wall_gap_3 = wall_gap_3;
	}
	public Integer getWall_hollow_1() {
		return wall_hollow_1;
	}
	public String getWall_hollow_1_t(){
		String t_getWall_hollow_1 = "";
		if(getWall_hollow_1()!=null){
			t_getWall_hollow_1=getWall_hollow_1().equals(1)?"无":getWall_hollow_1().equals(2)?"局部":getWall_hollow_1().equals(3)?"大面积":"";
		}
		return t_getWall_hollow_1;
	}
	public void setWall_hollow_1(Integer wall_hollow_1) {
		this.wall_hollow_1 = wall_hollow_1;
	}
	public Integer getWall_hollow_2() {
		return wall_hollow_2;
	}
	public String getWall_hollow_2_t(){
		String t_getWall_hollow_2="";
		if(getWall_hollow_2()!=null){
			t_getWall_hollow_2=getWall_hollow_2().equals(1)?"无需处理":getWall_hollow_2().equals(2)?"整改 ":"";
		}
		return t_getWall_hollow_2;
	}
	public void setWall_hollow_2(Integer wall_hollow_2) {
		this.wall_hollow_2 = wall_hollow_2;
	}
	public String getWall_hollow_3() {
		return wall_hollow_3;
	}
	public void setWall_hollow_3(String wall_hollow_3) {
		this.wall_hollow_3 = wall_hollow_3;
	}
	public Integer getWall_loose_1() {
		return wall_loose_1;
	}
	public String getWall_loose_1_t(){
		String t_getWall_loose_1 = "";
		if(getWall_loose_1()!=null){
			t_getWall_loose_1=getWall_loose_1().equals(1)?"无":getWall_loose_1().equals(2)?"局部":getWall_loose_1().equals(3)?"大面积":"";
		}
		return t_getWall_loose_1;
	}
	public void setWall_loose_1(Integer wall_loose_1) {
		this.wall_loose_1 = wall_loose_1;
	}
	public Integer getWall_loose_2() {
		return wall_loose_2;
	}
	public String getWall_loose_2_t(){
		String t_getWall_loose_2="";
		if(getWall_loose_2()!=null){
			t_getWall_loose_2=getWall_loose_2().equals(1)?"无需处理":getWall_loose_2().equals(2)?"整改 ":"";
		}
		return t_getWall_loose_2;
	}
	public void setWall_loose_2(Integer wall_loose_2) {
		this.wall_loose_2 = wall_loose_2;
	}
	public String getWall_loose_3() {
		return wall_loose_3;
	}
	public void setWall_loose_3(String wall_loose_3) {
		this.wall_loose_3 = wall_loose_3;
	}
	public Integer getWall_crack_1() {
		return wall_crack_1;
	}
	public String getWall_crack_1_t(){
		String t_getWall_crack_1 = "";
		if(getWall_crack_1()!=null){
			t_getWall_crack_1=getWall_crack_1().equals(1)?"无":getWall_crack_1().equals(2)?"局部":getWall_crack_1().equals(3)?"大面积":"";
		}
		return t_getWall_crack_1;
	}
	public void setWall_crack_1(Integer wall_crack_1) {
		this.wall_crack_1 = wall_crack_1;
	}
	public Integer getWall_crack_2() {
		return wall_crack_2;
	}
	public String getWall_crack_2_t(){
		String t_getWall_crack_2="";
		if(getWall_crack_2()!=null){
			t_getWall_crack_2=getWall_crack_2().equals(1)?"无需处理":getWall_crack_2().equals(2)?"整改 ":"";
		}
		return t_getWall_crack_2;
	}
	public void setWall_crack_2(Integer wall_crack_2) {
		this.wall_crack_2 = wall_crack_2;
	}
	public String getWall_crack_3() {
		return wall_crack_3;
	}
	public void setWall_crack_3(String wall_crack_3) {
		this.wall_crack_3 = wall_crack_3;
	}
	public Integer getWall_drop_1() {
		return wall_drop_1;
	}
	public String getWall_drop_1_t(){
		String t_getWall_drop_1 = "";
		if(getWall_drop_1()!=null){
			t_getWall_drop_1=getWall_drop_1().equals(1)?"无":getWall_drop_1().equals(2)?"局部":getWall_drop_1().equals(3)?"大面积":"";
		}
		return t_getWall_drop_1;
	}
	public void setWall_drop_1(Integer wall_drop_1) {
		this.wall_drop_1 = wall_drop_1;
	}
	public Integer getWall_drop_2() {
		return wall_drop_2;
	}
	public String getWall_drop_2_t(){
		String t_getWall_drop_2="";
		if(getWall_drop_2()!=null){
			t_getWall_drop_2=getWall_drop_2().equals(1)?"无需处理":getWall_drop_2().equals(2)?"整改 ":"";
		}
		return t_getWall_drop_2;
	}
	public void setWall_drop_2(Integer wall_drop_2) {
		this.wall_drop_2 = wall_drop_2;
	}
	public String getWall_drop_3() {
		return wall_drop_3;
	}
	public void setWall_drop_3(String wall_drop_3) {
		this.wall_drop_3 = wall_drop_3;
	}
	public Integer getWall_color_1() {
		return wall_color_1;
	}
	public String getWall_color_1_t(){
		String t_getWall_color_1 = "";
		if(getWall_color_1()!=null){
			t_getWall_color_1=getWall_color_1().equals(1)?"无":getWall_color_1().equals(2)?"局部":getWall_color_1().equals(3)?"大面积":"";
		}
		return t_getWall_color_1;
	}
	public void setWall_color_1(Integer wall_color_1) {
		this.wall_color_1 = wall_color_1;
	}
	public Integer getWall_color_2() {
		return wall_color_2;
	}
	public String getWall_color_2_t(){
		String t_getWall_color_2="";
		if(getWall_color_2()!=null){
			t_getWall_color_2=getWall_color_2().equals(1)?"无需处理":getWall_color_2().equals(2)?"整改 ":"";
		}
		return t_getWall_color_2;
	}
	public void setWall_color_2(Integer wall_color_2) {
		this.wall_color_2 = wall_color_2;
	}
	public String getWall_color_3() {
		return wall_color_3;
	}
	public void setWall_color_3(String wall_color_3) {
		this.wall_color_3 = wall_color_3;
	}
	public Integer getFloor_gap_1() {
		return floor_gap_1;
	}
	public String getFloor_gap_1_t(){
		String t_getFloor_gap_1="";
		if(getFloor_gap_1()!=null){
			t_getFloor_gap_1=getFloor_gap_1().equals(1)?"无":getFloor_gap_1().equals(2)?"不饱满":"";
		}
		return t_getFloor_gap_1;
	}
	
	public void setFloor_gap_1(Integer floor_gap_1) {
		this.floor_gap_1 = floor_gap_1;
	}
	public Integer getFloor_gap_2() {
		return floor_gap_2;
	}
	public String getFloor_gap_2_t(){
		String t_getFloor_gap_2="";
		if(getFloor_gap_2()!=null){
			t_getFloor_gap_2=getFloor_gap_2().equals(1)?"无需处理":getFloor_gap_2().equals(2)?"整改 ":"";
		}
		return t_getFloor_gap_2;
	}
	public void setFloor_gap_2(Integer floor_gap_2) {
		this.floor_gap_2 = floor_gap_2;
	}
	public String getFloor_gap_3() {
		return floor_gap_3;
	}
	public void setFloor_gap_3(String floor_gap_3) {
		this.floor_gap_3 = floor_gap_3;
	}
	public Integer getFloor_hollow_1() {
		return floor_hollow_1;
	}
	public String getFloor_hollow_1_t(){
		String t_getFloor_hollow_1 = "";
		if(getFloor_hollow_1()!=null){
			t_getFloor_hollow_1=getFloor_hollow_1().equals(1)?"无":getFloor_hollow_1().equals(2)?"局部":getFloor_hollow_1().equals(3)?"大面积":"";
		}
		return t_getFloor_hollow_1;
	}
	public void setFloor_hollow_1(Integer floor_hollow_1) {
		this.floor_hollow_1 = floor_hollow_1;
	}
	public Integer getFloor_hollow_2() {
		return floor_hollow_2;
	}
	public String getFloor_hollow_2_t(){
		String t_getFloor_hollow_2="";
		if(getFloor_hollow_2()!=null){
			t_getFloor_hollow_2=getFloor_hollow_2().equals(1)?"无需处理":getFloor_hollow_2().equals(2)?"整改 ":"";
		}
		return t_getFloor_hollow_2;
	}
	public void setFloor_hollow_2(Integer floor_hollow_2) {
		this.floor_hollow_2 = floor_hollow_2;
	}
	public String getFloor_hollow_3() {
		return floor_hollow_3;
	}
	public void setFloor_hollow_3(String floor_hollow_3) {
		this.floor_hollow_3 = floor_hollow_3;
	}
	public Integer getFloor_loose_1() {
		return floor_loose_1;
	}
	public String getFloor_loose_1_t(){
		String t_getFloor_loose_1 = "";
		if(getFloor_loose_1()!=null){
			t_getFloor_loose_1=getFloor_loose_1().equals(1)?"无":getFloor_loose_1().equals(2)?"局部":getFloor_loose_1().equals(3)?"大面积":"";
		}
		return t_getFloor_loose_1;
	}
	public void setFloor_loose_1(Integer floor_loose_1) {
		this.floor_loose_1 = floor_loose_1;
	}
	public Integer getFloor_loose_2() {
		return floor_loose_2;
	}
	public String getFloor_loose_2_t(){
		String t_getFloor_loose_2="";
		if(getFloor_loose_2()!=null){
			t_getFloor_loose_2=getFloor_loose_2().equals(1)?"无需处理":getFloor_loose_2().equals(2)?"整改 ":"";
		}
		return t_getFloor_loose_2;
	}
	public void setFloor_loose_2(Integer floor_loose_2) {
		this.floor_loose_2 = floor_loose_2;
	}
	public String getFloor_loose_3() {
		return floor_loose_3;
	}
	public void setFloor_loose_3(String floor_loose_3) {
		this.floor_loose_3 = floor_loose_3;
	}
	public Integer getElectric_distance_1() {
		return electric_distance_1;
	}
	public String getElectric_distance_1_t(){
		String t_getElectric_distance_1="";
		if(getElectric_distance_1()!=null){
			t_getElectric_distance_1=getElectric_distance_1().equals(1)?"合规":getElectric_distance_1().equals(2)?"不合规":"";
		}
		return t_getElectric_distance_1;
	}
	public void setElectric_distance_1(Integer electric_distance_1) {
		this.electric_distance_1 = electric_distance_1;
	}
	public Integer getElectric_distance_2() {
		return electric_distance_2;
	}
	public String getElectric_distance_2_t(){
		String t_getElectric_distance_2="";
		if(getElectric_distance_2()!=null){
			t_getElectric_distance_2=getElectric_distance_2().equals(1)?"无需处理":getElectric_distance_2().equals(2)?"整改 ":"";
		}
		return t_getElectric_distance_2;
	}
	public void setElectric_distance_2(Integer electric_distance_2) {
		this.electric_distance_2 = electric_distance_2;
	}
	public String getElectric_distance_3() {
		return electric_distance_3;
	}
	public void setElectric_distance_3(String electric_distance_3) {
		this.electric_distance_3 = electric_distance_3;
	}
	public Integer getElectric_arrange_1() {
		return electric_arrange_1;
	}
	public String getElectric_arrange_1_t(){
		String t_getElectric_arrange_1="";
		if(getElectric_arrange_1()!=null){
			t_getElectric_arrange_1=getElectric_arrange_1().equals(1)?"合规":getElectric_arrange_1().equals(2)?"不合规":"";
		}
		return t_getElectric_arrange_1;
	}
	public void setElectric_arrange_1(Integer electric_arrange_1) {
		this.electric_arrange_1 = electric_arrange_1;
	}
	public Integer getElectric_arrange_2() {
		return electric_arrange_2;
	}
	public String getElectric_arrange_2_t(){
		String t_getElectric_arrange_2="";
		if(getElectric_arrange_2()!=null){
			t_getElectric_arrange_2=getElectric_arrange_2().equals(1)?"无需处理":getElectric_arrange_2().equals(2)?"整改 ":"";
		}
		return t_getElectric_arrange_2;
	}
	public void setElectric_arrange_2(Integer electric_arrange_2) {
		this.electric_arrange_2 = electric_arrange_2;
	}
	public String getElectric_arrange_3() {
		return electric_arrange_3;
	}
	public void setElectric_arrange_3(String electric_arrange_3) {
		this.electric_arrange_3 = electric_arrange_3;
	}
	public Integer getElectric_water_cross_1() {
		return electric_water_cross_1;
	}
	public String getElectric_water_cross_1_t(){
		String t_getElectric_water_cross_1="";
		if(getElectric_water_cross_1()!=null){
			t_getElectric_water_cross_1=getElectric_water_cross_1().equals(1)?"合规":getElectric_water_cross_1().equals(2)?"不合规":"";
		}
		return t_getElectric_water_cross_1;
	}
	public void setElectric_water_cross_1(Integer electric_water_cross_1) {
		this.electric_water_cross_1 = electric_water_cross_1;
	}
	public Integer getElectric_water_cross_2() {
		return electric_water_cross_2;
	}
	public String getElectric_water_cross_2_t(){
		String t_getElectric_water_cross_2="";
		if(getElectric_water_cross_2()!=null){
			t_getElectric_water_cross_2=getElectric_water_cross_2().equals(1)?"无需处理":getElectric_water_cross_2().equals(2)?"整改 ":"";
		}
		return t_getElectric_water_cross_2;
	}
	public void setElectric_water_cross_2(Integer electric_water_cross_2) {
		this.electric_water_cross_2 = electric_water_cross_2;
	}
	public String getElectric_water_cross_3() {
		return electric_water_cross_3;
	}
	public void setElectric_water_cross_3(String electric_water_cross_3) {
		this.electric_water_cross_3 = electric_water_cross_3;
	}
	public Integer getDistribution_power_1() {
		return distribution_power_1;
	}
	public String getDistribution_power_1_t(){
		String t_getDistribution_power_1="";
		if(getDistribution_power_1()!=null){
			t_getDistribution_power_1=getDistribution_power_1().equals(1)?"合规":getDistribution_power_1().equals(2)?"不合规":"";
		}
		return t_getDistribution_power_1;
	}
	public void setDistribution_power_1(Integer distribution_power_1) {
		this.distribution_power_1 = distribution_power_1;
	}
	public Integer getDistribution_power_2() {
		return distribution_power_2;
	}
	public String getDistribution_power_2_t(){
		String t_getDistribution_power_2="";
		if(getDistribution_power_2()!=null){
			t_getDistribution_power_2=getDistribution_power_2().equals(1)?"无需处理":getDistribution_power_2().equals(2)?"整改 ":"";
		}
		return t_getDistribution_power_2;
	}
	public void setDistribution_power_2(Integer distribution_power_2) {
		this.distribution_power_2 = distribution_power_2;
	}
	public String getDistribution_power_3() {
		return distribution_power_3;
	}
	public void setDistribution_power_3(String distribution_power_3) {
		this.distribution_power_3 = distribution_power_3;
	}
	public Integer getAircondition_power_1() {
		return aircondition_power_1;
	}
	public String getAircondition_power_1_t(){
		String t_getAircondition_power_1="";
		if(getAircondition_power_1()!=null){
			t_getAircondition_power_1=getAircondition_power_1().equals(1)?"合规":getAircondition_power_1().equals(2)?"不合规":"";
		}
		return t_getAircondition_power_1;
	}
	public void setAircondition_power_1(Integer aircondition_power_1) {
		this.aircondition_power_1 = aircondition_power_1;
	}
	public Integer getAircondition_power_2() {
		return aircondition_power_2;
	}
	public String getAircondition_power_2_t(){
		String t_getAircondition_power_2="";
		if(getAircondition_power_2()!=null){
			t_getAircondition_power_2=getAircondition_power_2().equals(1)?"无需处理":getAircondition_power_2().equals(2)?"整改 ":"";
		}
		return t_getAircondition_power_2;
	}
	public void setAircondition_power_2(Integer aircondition_power_2) {
		this.aircondition_power_2 = aircondition_power_2;
	}
	public String getAircondition_power_3() {
		return aircondition_power_3;
	}
	public void setAircondition_power_3(String aircondition_power_3) {
		this.aircondition_power_3 = aircondition_power_3;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getBedroom_num() {
		return bedroom_num;
	}
	public void setBedroom_num(Integer bedroom_num) {
		this.bedroom_num = bedroom_num;
	}
	public Integer getLivingroom_num() {
		return livingroom_num;
	}
	public void setLivingroom_num(Integer livingroom_num) {
		this.livingroom_num = livingroom_num;
	}
	public Integer getBathroom_num() {
		return bathroom_num;
	}
	public void setBathroom_num(Integer bathroom_num) {
		this.bathroom_num = bathroom_num;
	}
	public String getDecorate_level() {
		return decorate_level;
	}
	public void setDecorate_level(String decorate_level) {
		this.decorate_level = decorate_level;
	}

		
}
