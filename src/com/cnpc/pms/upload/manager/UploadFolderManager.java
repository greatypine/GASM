package com.cnpc.pms.upload.manager;

import com.cnpc.pms.base.file.entity.PMSFile;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.dto.PMSFileDto;
import com.cnpc.pms.upload.entity.UploadFolder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 文件管理根文件目录数据字典
 * Created by liu on 2016/7/5 0005.
 */
public interface UploadFolderManager extends IManager {

    /**
     *  新增一个上传文件夹
     * @param uploadFolder 保存的文件夹
     */
    Result saveUploadFolder(UploadFolder uploadFolder);

    List<Map<String,String>> getFileListByFolderName(String strFoilderName);

    List<UploadFolder> getUploadFolderList(UploadFolder tmp);

    PMSFile uploadObjects(String folderName, List<?> lst_item) throws IOException;

    String getFileRoot();

    PMSFile saveObjectsForFile(String folderName, List<?> lst_item,String fname) throws IOException;

    
    public PMSFileDto uploadObjectsSiteSelection(String folderName, List<?> lst_item) throws IOException;
    
    public PMSFileDto uploadObjectsBannerInfo(String folderName, List<?> lst_item) throws IOException;
}
