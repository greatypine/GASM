package com.cnpc.pms.dynamic.entity;

/**
 * @Function：清洗出的企业购数据
 * @author：chenchuang
 * @date:2017-12-25 下午5:26:57
 *
 * @version V1.0
 */
public class EshopPurchaseDto {
	
	private String eshopId;
	private String eshopCode;
	private String eshopName;
	private String eshopModel;
	
	
	public String getEshopId() {
		return eshopId;
	}
	public void setEshopId(String eshopId) {
		this.eshopId = eshopId;
	}
	public String getEshopCode() {
		return eshopCode;
	}
	public void setEshopCode(String eshopCode) {
		this.eshopCode = eshopCode;
	}
	public String getEshopName() {
		return eshopName;
	}
	public void setEshopName(String eshopName) {
		this.eshopName = eshopName;
	}
	public String getEshopModel() {
		return eshopModel;
	}
	public void setEshopModel(String eshopModel) {
		this.eshopModel = eshopModel;
	}
	
}
