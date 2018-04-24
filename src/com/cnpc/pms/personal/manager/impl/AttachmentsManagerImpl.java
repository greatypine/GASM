package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.manager.AttachmentsManager;
import com.cnpc.pms.personal.manager.AttachmentsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunning on 2016/7/22.
 */
public class AttachmentsManagerImpl extends BaseManagerImpl implements AttachmentsManager  {
    
    public List<Attachment> getAttachmentList() {
    	
        List<Attachment> list_village = new ArrayList<Attachment>();
        List<?> lst_vilage_data = getList();
        if(lst_vilage_data != null && lst_vilage_data.size() > 0){
            for(Object obj_data : lst_vilage_data){
                list_village.add((Attachment)obj_data);
            }
        }

        return list_village;
    }
}
