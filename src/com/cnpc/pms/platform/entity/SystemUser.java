package com.cnpc.pms.platform.entity;

import java.util.List;

/**
 *
 * @author Ju_weigang
 * @时间 2018年1月24日上午10:22:28
 * @项目路径 com.gasq.bdp.logn.model
 * @描述
 */
public class SystemUser extends BaseBean{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private TSysUser user;

	private TSysUserExt userext;

	private List<TSysRole> role;//角色

	private List<TSysPermission> promissions;//权限

	private TCompany company;

    private List<TStore> stores;//门店

    private List<TDistCity> citys;//城市

    public List<TStore> getStores() {
        return stores;
    }

    public void setStores(List<TStore> stores) {
        this.stores = stores;
    }

    public List<TDistCity> getCitys() {
        return citys;
    }

    public void setCitys(List<TDistCity> citys) {
        this.citys = citys;
    }

	public TSysUserExt getUserext() {
		return userext;
	}

	public void setUserext(TSysUserExt userext) {
		this.userext = userext;
	}

	public TSysUser getUser() {
		return user;
	}

	public void setUser(TSysUser user) {
		this.user = user;
	}

	public List<TSysRole> getRole() {
		return role;
	}

	public void setRole(List<TSysRole> r) {
		this.role = r;
	}

	public List<TSysPermission> getPromissions() {
		return promissions;
	}

	public void setPromissions(List<TSysPermission> promissions) {
		this.promissions = promissions;
	}

	public TCompany getCompany() {
		return company;
	}

	public void setCompany(TCompany company) {
		this.company = company;
	}

}
