/**
 * 
 */
package com.cnpc.pms.platform.entity;

/**
 * @author 巨伟刚
 * @packageName com.gasq.bdp.logn.model
 * @creatTime 2017年9月13日上午8:49:42
 * @remark 
 */
public abstract class ParamsObject extends BaseBean{

	public Integer getPage() {
		if(this.page==null || this.page==0) this.page = 1;
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		if(this.rows==null || this.rows==0) this.rows = 1;
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	private static final long serialVersionUID = 1L;
	private Integer page;
	private Integer rows;
	private String q;
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	
	
}
