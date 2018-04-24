package com.cnpc.pms.base.file.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.Attachment;

import java.io.InputStream;
import java.util.List;

/**
 * Created by zhangjy on 2015/8/5.
 */

public interface ExcelManager extends IManager {
    /**
     * 多文件
     * 根据文件路径存储excel中的数据
     * 返回值 0 保存成功 1 社区不存在 2 社区存在多个 3 社区已审核 4 excel解析错误
     * @param
     */
    
    public String saveFileExcelData(User user, InputStream inp, String fileName, int type, String filePath, String remark,String ip) throws Exception;

   

    /**
     * 根据社区Id获取小区信息
     * @param list_file
     * @return
    */
    public List<String> getTinyVillageByVillage_id(List<String> list_file); 
    
    /**
     * 多文件
     * 根据文件路径存储excel中的数据
     * 返回值 0 保存成功 1 社区不存在 2 社区存在多个 3 社区已审核 4 excel解析错误
     * @param
     */
    
    public String saveFileExcel( User user,InputStream inp, String fileName, int type, String filePath,Attachment attachment) throws Exception;
    
    /**
     * 根据社区Id获取小区信息
     * @param list_file
     * @return
    */
    public String getTinyVillageByVillage(String fileName); 
    
    
}
