package com.cnpc.pms.communityArea;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by litianyu on 2017/9/11.
 */
@Entity
@Table(name="t_comnunity_block")
public class ComnunityArea {


    @Id
    @GeneratedValue
    private Long id;

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
    private String address_relevance_id;

    //创建时间
    @Column(name="createDate")
    private Date createDate;

    //创建人
    @Column(name="createUser")
    private String createUser;

    public String getCommunityAreastatus() {
        return communityAreastatus;
    }

    public void setCommunityAreastatus(String communityAreastatus) {
        this.communityAreastatus = communityAreastatus;
    }

    @Column(name="communityAreastatus")
    private String communityAreastatus;


    //getter and setter ......


    public void setId(Long id) {
        this.id = id;
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

    public void setAddress_relevance_id(String address_relevance_id) {
        this.address_relevance_id = address_relevance_id;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getId() {
        return id;
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

    public String getAddress_relevance_id() {
        return address_relevance_id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getCreateUser() {
        return createUser;
    }
}
