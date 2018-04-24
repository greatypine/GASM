/**
 * gaobaolei
 */
package com.cnpc.pms.slice.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.pms.base.entity.DataEntity;

/**
 * @author gaobaolei
 * 片区变更记录表
 */
@Entity
@Table(name = "t_area_history")
public class AreaHistory extends DataEntity {

    /**
     * 门店id
     */
	@Column(name = "area_id")
	private Long area_id;
   
	@Column(name = "store_id")
    private Long store_id;
    //街道id
    @Column(name="town_id")
    private Long town_id;
    //片区编号
    @Column(name="area_no",length = 50)
    private String area_no; 
    /**
     * 片区名称
     */
    @Column(name = "name",nullable = false)
    private String name;
    /**
     *  负责片区A员工编号
     */
    @Column(name = "employee_a_no",length = 20)
    private String employee_a_no;


    /**
     *  负责片区A员工名称
     */
    @Column(name = "employee_a_name")
    private String employee_a_name;
    
    /**
     *  负责片区B员工编号
     */
    @Column(name = "employee_b_no",length = 20)
    private String employee_b_no;


    /**
     *  负责片区B员工名称
     */
    @Column(name = "employee_b_name")
    private String employee_b_name;

    @Transient
    private List<AreaInfo> childrens;
    
    /**
     *  编辑目的 0：只是编辑国安侠 1：编辑内容  2：国安侠离职
     */
    @Column(name = "flag")
    private int flag;

    public Long getStore_id() {
        return store_id;
    }

    public void setStore_id(Long store_id) {
        this.store_id = store_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   
    public List<AreaInfo> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<AreaInfo> childrens) {
        this.childrens = childrens;
    }

	public String getEmployee_a_no() {
		return employee_a_no;
	}

	public void setEmployee_a_no(String employee_a_no) {
		this.employee_a_no = employee_a_no;
	}

	public String getEmployee_a_name() {
		return employee_a_name;
	}

	public void setEmployee_a_name(String employee_a_name) {
		this.employee_a_name = employee_a_name;
	}

	public String getEmployee_b_no() {
		return employee_b_no;
	}

	public void setEmployee_b_no(String employee_b_no) {
		this.employee_b_no = employee_b_no;
	}

	public String getEmployee_b_name() {
		return employee_b_name;
	}

	public void setEmployee_b_name(String employee_b_name) {
		this.employee_b_name = employee_b_name;
	}

	public Long getTown_id() {
		return town_id;
	}

	public void setTown_id(Long town_id) {
		this.town_id = town_id;
	}

	public String getArea_no() {
		return area_no;
	}

	public void setArea_no(String area_no) {
		this.area_no = area_no;
	}

	public Long getArea_id() {
		return area_id;
	}

	public void setArea_id(Long area_id) {
		this.area_id = area_id;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	
	
}
