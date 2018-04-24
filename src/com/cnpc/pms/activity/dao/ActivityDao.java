package com.cnpc.pms.activity.dao;

import com.cnpc.pms.activity.entity.LastInsertIDDTO;
import com.cnpc.pms.base.dao.IDAO;

import java.util.List;

/**
 * Created by litianyu on 2017/10/13.
 */
public interface ActivityDao extends IDAO {

    public List<LastInsertIDDTO> queryActivityLast_Insert_ID();

}
