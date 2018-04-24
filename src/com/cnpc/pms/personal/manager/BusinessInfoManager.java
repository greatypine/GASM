package com.cnpc.pms.personal.manager;

import java.io.File;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.BusinessInfo;

public interface BusinessInfoManager extends IManager{
	/**
     * 获取商业信息对象
     * 
     * @param conditions 查询条件
     * @return
     */
    Map<String, Object> getBussinessInfoList(QueryConditions conditions);
    
    Result saveBussinessInfo(BusinessInfo businessInfo);
    //app添加商业信息加验证
    Result saveBussinessInfoData(BusinessInfo businessInfo);
    
    BusinessInfo getBusinessInfoBy(Long id,String name,String address);
    
    BusinessInfo getBusinessInfoById(Long id);
    
    
    //验证商业信息是否存在
    Result getBussinessInfo(BusinessInfo businessInfo);
    
  //修改时验证商业信息是否存重复
    Result getBussinessInfoData(BusinessInfo businessInfo);
    
    /**
     * 获取商业全信息列表
     * 
     * @param conditions 查询条件
     * @return
     */
    File exportBusiNessList(Map<String,String> param)  throws Exception;
    
    /**
     * 更新商家信息
     * @param businessInfo
     * @return
     */
    public int saveBusinessInfo(BusinessInfo businessInfo);
    
    public void deleteBusinessById(Long id);
    
    Result findBusinessInfoList(BusinessInfo businessInfo);
    //验证商业信息是否存在web
    BusinessInfo verisionBusinessInfo(BusinessInfo businessInfo);
    
}
