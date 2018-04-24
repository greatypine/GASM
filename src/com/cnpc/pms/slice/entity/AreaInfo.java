package com.cnpc.pms.slice.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 片区详细
 * Created by liuxi on 2017/2/27.
 */
@Entity
@Table(name = "t_area_info")
public class AreaInfo extends DataEntity {

    /**
     * 片区id
     */
    @Column(name = "area_id")
    private Long area_id;
    @Column(name = "store_id")
    private Long store_id;
    //片区编号
    @Column(name="area_no",length = 50)
    private String area_no; 
    
    @Column(name="town_id")
    private Long town_id;
    
    @Column(name = "village_id")
    private Long village_id;

    @Column(name = "tin_village_id")
    private Long tin_village_id;

    @Column(name = "building_id")
    private Long building_id;
    
    @Column(name = "build_model")
    private Integer build_model;//根据此字段判断 building_id 存储的是楼房ID还是平房ID （1：平房  2：楼房）
    
    /**
     *  负责片区A员工编号
     */
    @Column(name = "employee_a_no",length = 20)
    private String employee_a_no;
    
    /**
     *  负责片区B员工编号
     */
    @Column(name = "employee_b_no",length = 20)
    private String employee_b_no;
    
    @Transient
    private String tiny_village_name;
    
    @Transient
    private String village_name;
    
    @Transient
    private String storeName;
    
    @Transient
    private String areaName;
    
    public Long getArea_id() {
        return area_id;
    }

    public void setArea_id(Long area_id) {
        this.area_id = area_id;
    }

    public Long getVillage_id() {
        return village_id;
    }

    public void setVillage_id(Long village_id) {
        this.village_id = village_id;
    }

    public Long getTin_village_id() {
        return tin_village_id;
    }

    public void setTin_village_id(Long tin_village_id) {
        this.tin_village_id = tin_village_id;
    }

    public Long getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(Long building_id) {
        this.building_id = building_id;
    }

	public Integer getBuild_model() {
		return build_model;
	}

	public void setBuild_model(Integer build_model) {
		this.build_model = build_model;
	}

	public String getArea_no() {
		return area_no;
	}

	public void setArea_no(String area_no) {
		this.area_no = area_no;
	}

	public String getEmployee_a_no() {
		return employee_a_no;
	}

	public void setEmployee_a_no(String employee_a_no) {
		this.employee_a_no = employee_a_no;
	}

	public String getEmployee_b_no() {
		return employee_b_no;
	}

	public void setEmployee_b_no(String employee_b_no) {
		this.employee_b_no = employee_b_no;
	}

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public Long getTown_id() {
		return town_id;
	}

	public void setTown_id(Long town_id) {
		this.town_id = town_id;
	}

	public String getTiny_village_name() {
		return tiny_village_name;
	}

	public void setTiny_village_name(String tiny_village_name) {
		this.tiny_village_name = tiny_village_name;
	}

	public String getVillage_name() {
		return village_name;
	}

	public void setVillage_name(String village_name) {
		this.village_name = village_name;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
    
    
    
}
