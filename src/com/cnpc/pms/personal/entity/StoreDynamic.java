package com.cnpc.pms.personal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.cnpc.pms.base.entity.OptLockEntity;

/**
 * 门店动态
 * 
 * @author sunning
 *
 */
@Entity
@Table(name = "t_store_dynamic")
public class StoreDynamic extends OptLockEntity {

	@Id
	@GeneratedValue
	private Long store_id;
	/**
	 * 高德城市编码
	 */
	@Column(length = 45, name = "gaode_citycode")
	private String gaode_cityCode;
	/**
	 * 高德省编码
	 */
	@Column(length = 45, name = "gaode_provincecode")
	private String gaode_provinceCode;
	/**
	 * 高德区编码
	 */
	@Column(length = 45, name = "gaode_adcode")
	private String gaode_adCode;
	/**
	 * 高德地址
	 */
	@Column(length = 255, name = "gaode_address")
	private String gaode_address;
	/**
	 * 门店名称
	 */
	@Column(length = 20, name = "name")
	private String name;
	/**
	 * 地址
	 */
	@Column(length = 100, name = "address")
	private String address;
	/**
	 * 详细地址
	 */
	@Column(length = 100, name = "detail_address")
	private String detail_address;
	/**
	 * 电话
	 */
	@Column(length = 20, name = "mobilephone")
	private String mobilephone;
	/**
	 * 开店时间
	 */
	@Column(name = "open_shop_time")
	private Date open_shop_time;
	/**
	 * 审批流程状态
	 */
	@Column(name = "auditor_status")
	private Integer auditor_status;

	/**
	 * 详细地址
	 */
	@Column(length = 45, name = "storeno")
	private String storeno;

	/**
	 * 门店类型编码
	 */
	@Column(length = 45, name = "storetype")
	private String storetype;
	/**
	 * 门店类型名称
	 */
	@Column(length = 45, name = "storetypename")
	private String storetypename;
	// 区ids
	@Column(length = 45, name = "county_ids")
	private String county_ids;
	// 属性
	@Column(length = 255, name = "nature")
	private String nature;
	// 租期
	@Column(length = 45, name = "tenancy_term")
	private String tenancyTerm;
	// 租金
	@Column(length = 45, name = "rental")
	private String rental;
	// 付款放式
	@Column(length = 45, name = "payment_method")
	private String payment_method;
	// 计租面积
	@Column(length = 45, name = "rent_area")
	private String rent_area;
	// 使用面积
	@Column(length = 45, name = "usable_area")
	private String usable_area;
	// 递增
	@Column(length = 45, name = "increase")
	private String increase;
	// 免租期
	@Column(length = 45, name = "rent_free")
	private String rent_free;
	// 税金
	@Column(length = 45, name = "taxes")
	private String taxes;
	// 中介费
	@Column(length = 45, name = "agency_fee")
	private String agency_fee;
	// 增容费
	@Column(length = 45, name = "increase_fee")
	private String increase_fee;
	@Transient
	private String caozuo;
	// 流程id
	@Column(length = 255, name = "work_id")
	private String work_id;
	// 门店位置坐标
	@Column(length = 255, name = "store_position")
	private String store_position;
	// 门店所在街道id
	@Column(length = 255, name = "place_town_id")
	private Long place_town_id;

	public Long getPlace_town_id() {
		return place_town_id;
	}

	public void setPlace_town_id(Long place_town_id) {
		this.place_town_id = place_town_id;
	}

	public String getStore_position() {
		return store_position;
	}

	public void setStore_position(String store_position) {
		this.store_position = store_position;
	}

	public String getWork_id() {
		return work_id;
	}

	public void setWork_id(String work_id) {
		this.work_id = work_id;
	}

	public String getCaozuo() {
		return caozuo;
	}

	public void setCaozuo(String caozuo) {
		this.caozuo = caozuo;
	}

	public Integer getAuditor_status() {
		return auditor_status;
	}

	public void setAuditor_status(Integer auditor_status) {
		this.auditor_status = auditor_status;
	}

	public String getCounty_ids() {
		return county_ids;
	}

	public void setCounty_ids(String county_ids) {
		this.county_ids = county_ids;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getTenancyTerm() {
		return tenancyTerm;
	}

	public void setTenancyTerm(String tenancyTerm) {
		this.tenancyTerm = tenancyTerm;
	}

	public String getRental() {
		return rental;
	}

	public void setRental(String rental) {
		this.rental = rental;
	}

	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	public String getRent_area() {
		return rent_area;
	}

	public void setRent_area(String rent_area) {
		this.rent_area = rent_area;
	}

	public String getUsable_area() {
		return usable_area;
	}

	public void setUsable_area(String usable_area) {
		this.usable_area = usable_area;
	}

	public String getIncrease() {
		return increase;
	}

	public void setIncrease(String increase) {
		this.increase = increase;
	}

	public String getRent_free() {
		return rent_free;
	}

	public void setRent_free(String rent_free) {
		this.rent_free = rent_free;
	}

	public String getTaxes() {
		return taxes;
	}

	public void setTaxes(String taxes) {
		this.taxes = taxes;
	}

	public String getAgency_fee() {
		return agency_fee;
	}

	public void setAgency_fee(String agency_fee) {
		this.agency_fee = agency_fee;
	}

	public String getIncrease_fee() {
		return increase_fee;
	}

	public void setIncrease_fee(String increase_fee) {
		this.increase_fee = increase_fee;
	}

	public String getStoretype() {
		return storetype;
	}

	public void setStoretype(String storetype) {
		this.storetype = storetype;
	}

	public String getStoretypename() {
		return storetypename;
	}

	public void setStoretypename(String storetypename) {
		this.storetypename = storetypename;
	}

	public String getStoreno() {
		return storeno;
	}

	public void setStoreno(String storeno) {
		this.storeno = storeno;
	}

	public Date getOpen_shop_time() {
		return open_shop_time;
	}

	public void setOpen_shop_time(Date open_shop_time) {
		this.open_shop_time = open_shop_time;
	}

	/**
	 * 城市名称
	 * 
	 * @return
	 */
	@Column(length = 20, name = "city_name")
	private String cityName;
	/**
	 * 城市code
	 * 
	 * @return
	 */
	@Column(length = 45, name = "cityno")
	private String cityNo;

	public String getCityNo() {
		return cityNo;
	}

	public void setCityNo(String cityNo) {
		this.cityNo = cityNo;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetail_address() {
		return detail_address;
	}

	public void setDetail_address(String detail_address) {
		this.detail_address = detail_address;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	/**
	 * 门店类别
	 */
	@Column(name = "type")
	private Integer type;

	@Column(name = "house_id")
	private Long house_id;

	/**
	 * 楼房ID
	 */
	@Column(name = "building_id")
	private Long building_id;

	/**
	 * 小区ID
	 */
	@Column(name = "tinyvillage_Id")
	private Long tinyvillage_Id;

	/**
	 * 社区ID
	 */
	@Column(name = "village_id")
	private Long village_id;

	/**
	 * 门店服务街道ID
	 */
	@Column(name = "town_id", length = 255)
	private String town_id;

	/**
	 * 区县ＩＤ
	 */
	@Column(name = "county_id")
	private Long county_id;

	/**
	 * 市地区ID
	 */
	@Column(name = "city_id")
	private Long city_id;

	/**
	 * 省ID
	 */
	@Column(name = "province_id")
	private Long province_id;

	/**
	 * 地理信息X-百度
	 */
	@Column(name = "coordinate_x")
	private Float coordinate_x;

	/**
	 * 地理信息Y-百度
	 */
	@Column(name = "coordinate_y")
	private Float coordinate_y;

	/**
	 * 状态标志位
	 */
	@Column(name = "status")
	private int status;
	/**
	 * 状态标志位
	 */
	@Generated(GenerationTime.INSERT)
	@Column(columnDefinition = " INT default 0 ")
	private Integer flag;

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	/**
	 * 创建者
	 */
	@Column(length = 36, name = "create_user")
	private String create_user;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date create_time;

	/**
	 * 修改者
	 */
	@Column(length = 36, name = "update_user")
	private String update_user;

	/**
	 * 管理的街道
	 */
	@Column(length = 255, name = "town_name")
	private String town_name;
	/**
	 * 备注
	 */
	@Column(length = 255, name = "remark")
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 第几家店
	 */
	@Column(name = "ordnumber")
	private Integer ordnumber;
	/**
	 * 是否微超
	 */
	@Column(length = 255, name = "superMicro")
	private String superMicro;
	/**
	 * 目前状态
	 */
	@Column(length = 255, name = "estate")
	private String estate;

	public String getSuperMicro() {
		return superMicro;
	}

	public void setSuperMicro(String superMicro) {
		this.superMicro = superMicro;
	}

	public String getEstate() {
		return estate;
	}

	public void setEstate(String estate) {
		this.estate = estate;
	}

	public Integer getOrdnumber() {
		return ordnumber;
	}

	public void setOrdnumber(Integer ordnumber) {
		this.ordnumber = ordnumber;
	}

	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getTown_name() {
		return town_name;
	}

	public void setTown_name(String town_name) {
		this.town_name = town_name;
	}

	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Date update_time;

	@Column(length = 36, name = "platformid")
	private String platformid;
	@Column(length = 255, name = "platformname")
	private String platformname;

	public String getPlatformname() {
		return platformname;
	}

	public void setPlatformname(String platformname) {
		this.platformname = platformname;
	}

	@Column(length = 36, name = "id")
	private String id;
	/**
	 * 门店编号
	 */
	@Column(name = "number")
	private Integer number;

	/**
	 * white
	 */
	@Column(name = "white")
	private String white;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getWhite() {
		return white;
	}

	public void setWhite(String white) {
		this.white = white;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlatformid() {
		return platformid;
	}

	public void setPlatformid(String platformid) {
		this.platformid = platformid;
	}

	/**
	 * 开店时间
	 */
	@Column(name = "open_time")
	private Date open_time;

	/**
	 * 店长ID
	 */
	private Long skid;
	/**
	 * 区域管理ID
	 */
	private Long rmid;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getHouse_id() {
		return house_id;
	}

	public void setHouse_id(Long house_id) {
		this.house_id = house_id;
	}

	public Long getBuilding_id() {
		return building_id;
	}

	public void setBuilding_id(Long building_id) {
		this.building_id = building_id;
	}

	public Long getTinyvillage_Id() {
		return tinyvillage_Id;
	}

	public void setTinyvillage_Id(Long tinyvillage_Id) {
		this.tinyvillage_Id = tinyvillage_Id;
	}

	public Long getVillage_id() {
		return village_id;
	}

	public void setVillage_id(Long village_id) {
		this.village_id = village_id;
	}

	public String getTown_id() {
		return town_id;
	}

	public void setTown_id(String town_id) {
		this.town_id = town_id;
	}

	public Long getCounty_id() {
		return county_id;
	}

	public void setCounty_id(Long county_id) {
		this.county_id = county_id;
	}

	public Long getCity_id() {
		return city_id;
	}

	public void setCity_id(Long city_id) {
		this.city_id = city_id;
	}

	public Long getProvince_id() {
		return province_id;
	}

	public void setProvince_id(Long province_id) {
		this.province_id = province_id;
	}

	public Float getCoordinate_x() {
		return coordinate_x;
	}

	public void setCoordinate_x(Float coordinate_x) {
		this.coordinate_x = coordinate_x;
	}

	public void setCoordinate_y(Float coordinate_y) {
		this.coordinate_y = coordinate_y;
	}

	public Float getCoordinate_y() {
		return coordinate_y;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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

	public Date getOpen_time() {
		return open_time;
	}

	public void setOpen_time(Date open_time) {
		this.open_time = open_time;
	}

	public Long getSkid() {
		return skid;
	}

	public void setSkid(Long skid) {
		this.skid = skid;
	}

	public Long getRmid() {
		return rmid;
	}

	public void setRmid(Long rmid) {
		this.rmid = rmid;
	}

	public String getGaode_cityCode() {
		return gaode_cityCode;
	}

	public void setGaode_cityCode(String gaode_cityCode) {
		this.gaode_cityCode = gaode_cityCode;
	}

	public String getGaode_provinceCode() {
		return gaode_provinceCode;
	}

	public void setGaode_provinceCode(String gaode_provinceCode) {
		this.gaode_provinceCode = gaode_provinceCode;
	}

	public String getGaode_adCode() {
		return gaode_adCode;
	}

	public void setGaode_adCode(String gaode_adCode) {
		this.gaode_adCode = gaode_adCode;
	}

	public String getGaode_address() {
		return gaode_address;
	}

	public void setGaode_address(String gaode_address) {
		this.gaode_address = gaode_address;
	}

}
