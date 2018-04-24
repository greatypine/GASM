package com.cnpc.pms.personal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
/**
 * 小区坐标范围录入表
 * @author sunning
 * @author gaobaolei
 */
@Entity
@Table(name = "tiny_area")
public class TinyArea extends DataEntity{
	
		//小区编码
		@Column(name = "code",length=22)
		private String code;
		//小区名字
		@Column(name = "name",length=255)
		private String name;
		//员工a编号
		@Column(name = "employee_a_no",length=45)
		private String employee_a_no;
		//员工b编号
		@Column(name = "employee_b_no",length=45)
		private String employee_b_no;
		//小区坐标范围
		@Column(name = "coordinate_range",columnDefinition="TEXT")
		private String coordinate_range;
		//小区位置坐标
		@Column(name = "position",length=255)
		private String position;
		//小区id
		@Column(name="tiny_village_id")
		private Long tiny_village_id;
		//片区编号
		@Column(name = "area_no",length=255)
		private String area_no;
		//门店编号
		@Column(name = "store_no",length=50)
		private String storeNo;
		@Column(name = "vallage_area",length=255)
		private String vallage_area;
		

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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

		public String getCoordinate_range() {
			return coordinate_range;
		}

		public void setCoordinate_range(String coordinate_range) {
			this.coordinate_range = coordinate_range;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public Long getTiny_village_id() {
			return tiny_village_id;
		}

		public void setTiny_village_id(Long tiny_village_id) {
			this.tiny_village_id = tiny_village_id;
		}

		public String getArea_no() {
			return area_no;
		}

		public void setArea_no(String area_no) {
			this.area_no = area_no;
		}


		public String getStoreNo() {
			return storeNo;
		}

		public void setStoreNo(String storeNo) {
			this.storeNo = storeNo;
		}

		public String getVallage_area() {
			return vallage_area;
		}

		public void setVallage_area(String vallage_area) {
			this.vallage_area = vallage_area;
		}

	
		

		
		
		
		
		
}
