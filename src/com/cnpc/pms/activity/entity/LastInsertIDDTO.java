package com.cnpc.pms.activity.entity;

import com.cnpc.pms.base.dto.PMSDTO;

/**
 * Created by litianyu on 2017/10/24.
 */
public class LastInsertIDDTO extends PMSDTO {

    /*LAST_INSERT_ID 返回最后一条ID**/
    private String LAST_INSERT_ID_;


    //getter and setter ......

    public String getLAST_INSERT_ID_() {
        return LAST_INSERT_ID_;
    }

    public void setLAST_INSERT_ID_(String lAST_INSERT_ID_) {
        LAST_INSERT_ID_ = lAST_INSERT_ID_;
    }

}
