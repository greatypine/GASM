package com.cnpc.pms.personal.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name = "t_approval")
public class Approval extends DataEntity{
		//填报月份
		@Column(name = "str_month",length=45)
		private String strMonth;
		//所属门店
		@Column(name = "store_id",length=45)
		private Long store_id;
		//所属城市
		@Column(name = "cityName",length=45)
		private String cityName;
		//状态(0.等待审核，1.通过审核，2.退回)
		@Column(name = "state_type",length=45)
		private Integer stateType;
		//业务名称
		@Column(name = "name",length=255)
		private String name;
		//业务名称编码(1-摆渡车，2-考勤.)
		@Column(name = "type_id",length=255)
		private String typeId;
		//业务名称编码(1-摆渡车，2-考勤.)
		@Column(name = "remark",length=4000)
		private String remark;
		//门店名称
		@Column(name = "store_name",length=255)
		private String store_name;
		
		public String getStore_name() {
			return store_name;
		}
		public void setStore_name(String store_name) {
			this.store_name = store_name;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getTypeId() {
			return typeId;
		}
		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}
		public String getStrMonth() {
			return strMonth;
		}
		public void setStrMonth(String strMonth) {
			this.strMonth = strMonth;
		}
		public Long getStore_id() {
			return store_id;
		}
		public void setStore_id(Long store_id) {
			this.store_id = store_id;
		}
		public String getCityName() {
			return cityName;
		}
		public void setCityName(String cityName) {
			this.cityName = cityName;
		}
		public Integer getStateType() {
			return stateType;
		}
		public void setStateType(Integer stateType) {
			this.stateType = stateType;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}	

}
