package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.SelfExpress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SelfExpressManager extends IManager {

    /**
     * 解析Excel导入快递数据
     * @param lst_import_excel 快递Excel
     */
    void importSelfExpressForExcel(List<File> lst_import_excel) throws Exception;
    
    
    /**
     * 保存菜鸟驿站信息
     * @param selfExpress
     * @return
     */
    public SelfExpress saveSelfExpress(SelfExpress selfExpress);
    
    
    /**
     * 根据ID查找自提订单信息
     * @param selfExpress
     * @return
     */
    public int querySelfExpressByExpressNo(SelfExpress selfExpress);
    
    /**
     * 根据ID删除菜鸟驿站信息
     * @param id
     * @return
     */
    public SelfExpress deleteSelfExpress(Long id);
    
    /**
     * 查询菜鸟驿站列表
     * @param condition
     * @return
     */
    public Map<String, Object> querySelfExpressList(QueryConditions condition);
    
    /**
     * 导入模板不完善菜鸟驿站信息 
     * @param lst_import_excel
     * @throws Exception
     */
    public void importSelfExpressTemplateForExcel(List<File> lst_import_excel) throws Exception;
    
    
    /**
     * 查找一条未完善的信息
     * @param selfExpress
     * @return
     */
    public SelfExpress querySelfExpressByNext(SelfExpress selfExpress);
    
    
    public SelfExpress querySelfExpressById(Long id);
}
