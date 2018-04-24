package com.cnpc.pms.personal.entity;

import java.util.Date;

import com.cnpc.pms.base.entity.IEntity;

import javax.persistence.*;

/**
 * 客户地址视图
 * Created by liuxi on 2016/10/12 0012.
 */
@Entity
@Table(name="view_address_of_customer")
public class ViewAddressCustomer  implements IEntity {
	
	@Column(name="town_id")
	private Long town_id;
	
	@Column(name="town_name")
	private String town_name;
	
	@Column(name="village_id")
	private Long  village_id;
	
	@Column(name="village_name")
	private String village_name;
	
    @Column(name="tv_id")
    private Long tv_id;

    @Column(name="tv_name")
    private String tv_name;

    @Column(name="tv_address")
    private String tv_address;

    @Column(name="building_id")
    private Long building_id;

    @Column(name="building_name")
    private String building_name;

    @Id
    @Column(name="house_id")
    private Long house_id;

    @Column(name="house_no")
    private String house_no;

    @Column(name="unit_no")
    private String unit_no;

    @Column(name="house_style_id")
    private Long house_style_id;

    @Column(name="house_toward")
    private String house_toward;

    @Column(name="house_style")
    private String house_style;

    @Column(name="house_area")
    private String house_area;

    @Column(name="house_pic")
    private String house_pic;

    @Column(name="house_type")
    private Integer house_type;

    @Column(name="customer_id")
    private Long customer_id;

    @Transient
    private Long store_id;
    
    /**
     * 创建时间
     */
    @Transient
    private Date create_time;


    /**
     * 更新用户
     */
    @Transient
    private String update_user;

    /**
     * 创建者
     */
    @Transient
    private String create_user;

    /**
     * 更新时间
     */
    @Transient
    private Date update_time;

    @Transient
    private Long create_user_id;

    @Transient
    private Long update_user_id;

    public Long getTv_id() {
        return tv_id;
    }

    public void setTv_id(Long tv_id) {
        this.tv_id = tv_id;
    }

    public String getTv_name() {
        return tv_name;
    }

    public void setTv_name(String tv_name) {
        this.tv_name = tv_name;
    }

    public String getTv_address() {
        return tv_address;
    }

    public void setTv_address(String tv_address) {
        this.tv_address = tv_address;
    }

    public Long getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(Long building_id) {
        this.building_id = building_id;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public Long getHouse_id() {
        return house_id;
    }

    public void setHouse_id(Long house_id) {
        this.house_id = house_id;
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

    public Long getHouse_style_id() {
        return house_style_id;
    }

    public void setHouse_style_id(Long house_style_id) {
        this.house_style_id = house_style_id;
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

    public String getHouse_area() {
        return house_area;
    }

    public void setHouse_area(String house_area) {
        this.house_area = house_area;
    }

    public String getHouse_pic() {
        return house_pic;
    }

    public void setHouse_pic(String house_pic) {
        this.house_pic = house_pic;
    }

    public Integer getHouse_type() {
        return house_type;
    }

    public void setHouse_type(Integer house_type) {
        this.house_type = house_type;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public Long getStore_id() {
        return store_id;
    }

    public void setStore_id(Long store_id) {
        this.store_id = store_id;
    }


	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}


	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public Long getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(Long create_user_id) {
		this.create_user_id = create_user_id;
	}

	public Long getUpdate_user_id() {
		return update_user_id;
	}

	public void setUpdate_user_id(Long update_user_id) {
		this.update_user_id = update_user_id;
	}
   

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

	public Long getTown_id() {
		return town_id;
	}

	public void setTown_id(Long town_id) {
		this.town_id = town_id;
	}

	
	public Long getVillage_id() {
		return village_id;
	}

	public void setVillage_id(Long village_id) {
		this.village_id = village_id;
	}

	public String getVillage_name() {
		return village_name;
	}

	public void setVillage_name(String village_name) {
		this.village_name = village_name;
	}

	public String getTown_name() {
		return town_name;
	}

	public void setTown_name(String town_name) {
		this.town_name = town_name;
	}

	
    
    
}
