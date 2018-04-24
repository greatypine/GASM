/**
 * gaobaolei
 */
package com.cnpc.pms.dynamic.entity;

/**
 * @author gaobaolei
 *
 */
public class DynamicDto {

	private Long cityId;
	private String cityName;
	private String employeeName;
	private Long employeeId;
	private String employeeNo;
	private Integer year;
	private Integer month;
	private String limit;
	private String beginDate;
	private String endDate;
	private String storeNumer;
	private String storeNo;
	private String storeIds;
	private Long storeId;
	private String provinceId;
	private String provinceName;
	private String searchstr;
	private Integer target;// 执行人（0:总部 1：城市总监级别 2：店长级别 3:国安侠）
	private String areaName;
	private String areaNo;
	private String storeName;
	private String townName;
	private String villageName;
	private String tinyvillageName;
	private String tinyvillageCode;
    private Long tinyvillageId;//小区ID
    
	/** 接口类型，为了标识出的特殊接口字段，是否需要在拜访记录和客户画像接口中传递 grade 这个字段 */
	private String interfaceType;
	
	//数据类型
	private String dataType;
	
	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStoreNumer() {
		return storeNumer;
	}

	public void setStoreNumer(String storeNumer) {
		this.storeNumer = storeNumer;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}

	public String getSearchstr() {
		return searchstr;
	}

	public void setSearchstr(String searchstr) {
		this.searchstr = searchstr;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getStoreIds() {
		return storeIds;
	}

	public void setStoreIds(String storeIds) {
		this.storeIds = storeIds;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getTinyvillageName() {
		return tinyvillageName;
	}

	public void setTinyvillageName(String tinyvillageName) {
		this.tinyvillageName = tinyvillageName;
	}

	public String getTinyvillageCode() {
		return tinyvillageCode;
	}

	public void setTinyvillageCode(String tinyvillageCode) {
		this.tinyvillageCode = tinyvillageCode;
	}

	public Long getTinyvillageId() {
		return tinyvillageId;
	}

	public void setTinyvillageId(Long tinyvillageId) {
		this.tinyvillageId = tinyvillageId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	

}
