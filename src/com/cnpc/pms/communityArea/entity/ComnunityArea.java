package com.cnpc.pms.communityArea.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.*;

/**
 * Created by litianyu on 2017/9/11.
 */
@Entity
@Table(name="t_comnunity_block")
public class ComnunityArea extends DataEntity {

    /**小区名称*/
    @Column(name="community_name")
    private String community_name;

    /**小区表外键*/
    @Column(name="communityId")
    private Long communityId;

    /**片区名称*/
    @Column(name="address_relevanceName")
    private String address_relevanceName;

    /**片区表外键*/
    @Column(name="address_relevance_id")
    private Long address_relevance_id;


    @Column(name="communityAreastatus")
    private String communityAreastatus;
    @Transient
    private Integer publicarea;
    //区块的id的字符串
    @Transient
    private String pids;
    
    public Integer getPublicarea() {
		return publicarea;
	}

	public void setPublicarea(Integer publicarea) {
		this.publicarea = publicarea;
	}

	public String getPids() {
		return pids;
	}

	public void setPids(String pids) {
		this.pids = pids;
	}

	public String getCommunityAreastatus() {
        return communityAreastatus;
    }

    public void setCommunityAreastatus(String communityAreastatus) {
        this.communityAreastatus = communityAreastatus;
    }


    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public void setAddress_relevanceName(String address_relevanceName) {
        this.address_relevanceName = address_relevanceName;
    }


    public String getCommunity_name() {
        return community_name;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public String getAddress_relevanceName() {
        return address_relevanceName;
    }

	public Long getAddress_relevance_id() {
		return address_relevance_id;
	}

	public void setAddress_relevance_id(Long address_relevance_id) {
		this.address_relevance_id = address_relevance_id;
	}

   
}
