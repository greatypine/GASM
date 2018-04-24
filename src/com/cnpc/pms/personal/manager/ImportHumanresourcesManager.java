package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.ImportHumanresources;
import com.cnpc.pms.personal.entity.XxExpress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface ImportHumanresourcesManager extends IManager {

    String importHumanresourceForExcel(List<File> lst_import_excel) throws Exception;
    
    //public ImportHumanresources saveImportHumanresource(ImportHumanresources importHumanresources);
    
    //public XxExpress deleteHumanresource(Long id);
    
    //public Map<String, Object> queryHumanresourceList(QueryConditions condition);
}
