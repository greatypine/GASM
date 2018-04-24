package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.XxExpress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface XxExpressManager extends IManager {

    /**
     * 解析Excel导入快递数据
     * @param lst_import_excel 快递Excel
     */
    void importXxExpressForExcel(List<File> lst_import_excel) throws Exception;
    
    
    /**
     * 保存许鲜信息
     * @param xxExpress
     * @return
     */
    public XxExpress saveXxExpress(XxExpress xxExpress);
    
    
    /**
     * 根据ID删除许鲜信息 
     * @param id
     * @return
     */
    public XxExpress deleteXxExpress(Long id);
    
    /**
     * 查找许鲜列表
     * @param condition
     * @return
     */
    public Map<String, Object> queryXxExpressList(QueryConditions condition);
    
    
    public XxExpress queryxxExpressById(Long id);
}
