package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.ImportHumanresources;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.XxExpress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface HumanresourcesManager extends IManager {

    
    public String saveHumanresource(String ids);
    
    public Humanresources queryHumanresourceById(Long id);
    
    public Humanresources queryHumanenTeachById(Long id);
    
    public Humanresources updateHumanresources(Humanresources humanresources);
    
    public Humanresources updateHumanresourcesTeach(Humanresources humanresources);
    
    public Humanresources updateHumanresourcesenTeach(Humanresources humanresources);
    
    public String saveHumanTeachForExcel(Humanresources hr) throws Exception;
    
    public Map<String, Object> queryhumanList(QueryConditions condition);
    
    public void importHumanresourceTeach(List<File> lst_import_excel) throws Exception;
    
    public void updateAuthorizedtype(Humanresources humanresources);

    File exportHumanExcel() throws IOException, Exception;
    
    public String saveHumanresourceHSTK(List<File> lst_import_excel) throws Exception;
    
    
    /**
     * 保存店长信息
     */
    public void saveStoreKeeper(Humanresources humanresources);
    
    public List<Humanresources> queryHumanresourceListByStoreId(Long store_id);
    
    
    public Map<String, Object> queryhumanbaseList(QueryConditions condition);
    
    
    
    public List<Humanresources> queryHumanresourceListByStoreIdLz(Long store_id,String month);
    
    
    public List<Humanresources> queryHumanresourceListByStoreIdRz(Long store_id,String month);
    
    
    public Map<String, Object> queryEmployeeInfoByNo(String employee_no);
    
    public int findEmployeeInfoByNo(String employee_no);
    
    
    public void updateStoreNameById(Long store_id,String store_name);
    
    /**
     * 根据门店 查询该门店所有在职员工的集合(查国安侠)
     */
    public List<Humanresources> queryHumanresourceGAXListByStoreId(Long store_id);
    
    /**
     * 删除导入的 未分配门店 最大员工号数据 
     * @param human_id
     * @return
     */
    public String removeHumanresourceById(Long human_id);
    
    /**
     * 保存员工基础信息 点击新增 保存方法 
     * @param humanresources
     * @return
     */
    public String saveHumanresourcesInfo(Humanresources humanresources);
    
    /**
     * 
     * TODO 查询员工信息 
     * 2017年6月29日
     * @author gaobaolei
     * @param employee_no
     * @return
     */
    public Humanresources getEmployeeInfoByEmployeeNo(String employee_no);
    
    
    public Humanresources getEmployeeInfoByEmployeeNoExtend(String employee_no);
    
    /**
     * 批量分配门店的方法
     * @param arr
     * @param storename
     * @return
     */
    public List<Humanresources> updateHumanresourcesMult(List<Map<String, Object>> arr,String storename);
    
    
    /**
     * 根据登录人所管理 的城市  进行数据的备份 
     * @param citySelect
     * @return
     */
    public List<Humanresources> queryHumanresourceListByCity(String citySelect);
    
    
    /**
     * 店长查询列表 
     * @param condition
     * @return
     */
    public Map<String, Object> queryhumanbaseDZList(QueryConditions condition);
    
    
    /**
     * 店长修改 事业群 
     * @param hr
     * @return
     */
    public Humanresources updateHumanresourceCareerGroupById(Humanresources hr);
    
    
    /**
     * 根据事业群 获取所有的该事业群下的门店服务专员 
     * @param careername
     * @return
     */
    public Map<String, Object> queryHumanresourcesByCareerName(String careername);
    
    
    
    /**
     * 根据门店 查询该门店所有在职员工的集合(只查国安侠)公海订单分配用
     */
    public List<Humanresources> queryHumanGAXListByStoreId(String storeno);
    
    
    public Map<String, Object> queryHumanresourcesList(Humanresources humanresources, PageInfo pageInfo);
    public Map<String, Object> exportHuman(Humanresources humanresources);
    
    
    //门店运营数据 
    public Map<String, Object> queryStoreOperationList(Store store, PageInfo pageInfo);
    //导出门店运营数据 tab1
    public Map<String, Object> exportStoreOperationHuman(Store store);
    //导出门店运营 tab2
    public Map<String, Object> exportStoreOperationStore(Store store);
    
    
    /**
     * 根据电话  查询系统中是否存在相同的电话信息 
     */
    public List<Humanresources> queryHumanresourceListByPhone(String phone);
    
    
    //导出系统数据 
    public Map<String, Object> exportSysUser(Humanresources humanresources);
    
}
