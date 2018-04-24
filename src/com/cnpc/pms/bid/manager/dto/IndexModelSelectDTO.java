package com.cnpc.pms.bid.manager.dto;

import java.util.ArrayList;
import java.util.List;

import com.cnpc.pms.bid.entity.IndexModelSelect;

/**
 * 首页模块选择DTO
 * @author IBM
 *
 */
public class IndexModelSelectDTO {

	/** 已选择的模块列表 */
	private List<IndexModelSelect> selectIm = new ArrayList<IndexModelSelect>();
	
	/** 未选择的模块列表 */
	private List<IndexModelSelect> unSelectIm = new ArrayList<IndexModelSelect>();

	public List<IndexModelSelect> getSelectIm() {
		return selectIm;
	}

	public void setSelectIm(List<IndexModelSelect> selectIm) {
		this.selectIm = selectIm;
	}

	public List<IndexModelSelect> getUnSelectIm() {
		return unSelectIm;
	}

	public void setUnSelectIm(List<IndexModelSelect> unSelectIm) {
		this.unSelectIm = unSelectIm;
	}

}
