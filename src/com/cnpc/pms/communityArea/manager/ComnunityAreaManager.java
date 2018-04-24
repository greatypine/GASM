package com.cnpc.pms.communityArea.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.communityArea.entity.ComnunityArea;

import java.util.List;
import java.util.Map;

/**
 * Created by litianyu on 2017/9/11.
 */
public interface ComnunityAreaManager extends IManager {


    public Map<String, Object> queryComnunityAreaList(QueryConditions condition);
    public ComnunityArea saveOrUPdateComnunityArea(ComnunityArea comnunityArea);
    //根据小区id和区块id查找不在该(adderssIdS 当前门店的区块id的集合)
    List<ComnunityArea> findComnunityArea(Long tinyvillageId,String pids,String adderssIdS);
    ComnunityArea findComnunityAreaBytinyvillageIdAndPId(Long tinyvillageId,String pid);
    
    ComnunityArea SaveComnunityArea(ComnunityArea comnunityArea);
    //根据区块id查找是否绑定有小区
    List<ComnunityArea> findComnunityAreaByAddressId(Long addressId);
    //根据小区查找下面的片区
    List<ComnunityArea> findComnunityAreaByTinyvillageId(Long tinyvillageId);
    //删除订单区块
    ComnunityArea deleteComnunity(Long address_relevance_id);
    //根据片区id和小区id查找片区是否绑定到其他小区下
    List<ComnunityArea> getComnunityAreaByaddress_idAndNOtINTinyID(ComnunityArea comnunityArea);
    

}
