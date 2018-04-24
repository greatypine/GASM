package com.cnpc.pms.upload.entity;

import com.cnpc.pms.base.dict.entity.Dict;
import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.entity.IEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by liu on 2016/7/5 0005.
 */
@Entity
@Table(name = "dict_upload_folder_resource")
public class UploadFolder {

    /**
     * 文件夹名称
     */
    @Id
    @Column(name="DICTCODE")
    private String dictCode;

    /**
     * 文件夹显示名称
     */
    @Column(name="DICTTEXT")
    private String dictText;

    @Column(name="SERIALNUMBER",length = 20)
    private String serialNumber;

    @Column(name="DISABLEDFLAG",length = 20)
    private String disabledFlag;

    /**
     * 父类文件夹名称
     */
    @Column(name="parent_code")
    private String parent_code;

    /**
     * 导入执行的业务类
     */
    @Column(name="manager_name")
    private String manager_name;

    /**
     * 导入执行的业务类方法
     */
    @Column(name="method_name")
    private String method_name;

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictText() {
        return dictText;
    }

    public void setDictText(String dictText) {
        this.dictText = dictText;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDisabledFlag() {
        return disabledFlag;
    }

    public void setDisabledFlag(String disabledFlag) {
        this.disabledFlag = disabledFlag;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }
}
