package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.Attachment;

import java.util.List;

/**
 * Created by sunning on 2016/7/22.
 */
public interface AttachmentsManager extends IManager {
    /**
     * 获取当前上传的文件名称
     * @return
     */
    List<Attachment>  getAttachmentList();


}
