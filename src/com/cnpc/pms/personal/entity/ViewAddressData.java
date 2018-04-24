package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.IEntity;

/**
 * 地址信息以及地址内住户信息
 * Created by liuxi on 2016/10/21 0021.
 */
@Entity
@Table(name="view_address_data")
public class ViewAddressData implements IEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "house_id")
    private Long house_id;

    @Column(name = "address")
    private String address;

    @Column(name = "house_area")
    private String house_area;

    @Column(name = "house_toward")
    private String house_toward;

    @Column(name = "house_style")
    private String house_style;

    @Column(name = "is_pic")
    private String is_pic;

    @Column(name = "people_num")
    private Integer people_num;

    @Column(name = "tv_id")
    private Long tv_id;

    @Column(name = "tv_name")
    private String tv_name;

    @Column(name = "building_id")
    private Long building_id;

    @Column(name = "building_name")
    private String building_name;

    @Column(name = "house_no")
    private String house_no;

    @Column(name = "unit_no")
    private String unit_no;

    @Column(name = "store_id")
    private Long store_id;
    
    @Column(name="house_type")
    private Integer house_type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHouse_id() {
        return house_id;
    }

    public void setHouse_id(Long house_id) {
        this.house_id = house_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHouse_area() {
        return house_area;
    }

    public void setHouse_area(String house_area) {
        this.house_area = house_area;
    }

    public String getHouse_toward() {
        return house_toward;
    }

    public void setHouse_toward(String house_toward) {
        this.house_toward = house_toward;
    }

    public String getHouse_style() {
        return house_style;
    }

    public void setHouse_style(String house_style) {
        this.house_style = house_style;
    }

    public String getIs_pic() {
        return is_pic;
    }

    public void setIs_pic(String is_pic) {
        this.is_pic = is_pic;
    }

    public Integer getPeople_num() {
        return people_num;
    }

    public void setPeople_num(Integer people_num) {
        this.people_num = people_num;
    }

    public Long getTv_id() {
        return tv_id;
    }

    public void setTv_id(Long tv_id) {
        this.tv_id = tv_id;
    }

    public Long getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(Long building_id) {
        this.building_id = building_id;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getUnit_no() {
        return unit_no;
    }

    public void setUnit_no(String unit_no) {
        this.unit_no = unit_no;
    }

    public Long getStore_id() {
        return store_id;
    }

    public void setStore_id(Long store_id) {
        this.store_id = store_id;
    }

    public String getTv_name() {
        return tv_name;
    }

    public void setTv_name(String tv_name) {
        this.tv_name = tv_name;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

	public Integer getHouse_type() {
		return house_type;
	}

	public void setHouse_type(Integer house_type) {
		this.house_type = house_type;
	}
    
    
}
