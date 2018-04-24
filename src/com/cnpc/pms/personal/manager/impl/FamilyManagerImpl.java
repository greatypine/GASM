package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.dict.entity.Dict;
import com.cnpc.pms.base.dict.manager.DictManager;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.dao.HouseStyleDao;
import com.cnpc.pms.personal.entity.*;
import com.cnpc.pms.personal.manager.*;
import com.cnpc.pms.utils.CompressFile;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * 家庭成员业务实现类
 * Created by liuxiao on 2016/5/24 0024.
 */
public class FamilyManagerImpl extends BizBaseCommonManager implements FamilyManager {

    /**
     * 保存家庭成员信息
     * @param lst_save_family 家庭成员信息
     */
    @Override
    public void saveFamilyInfo(List<Family> lst_save_family) {
        if(lst_save_family == null || lst_save_family.size() ==0){
            return;
        }
        List<?> lst_family = this.getList(
                FilterFactory.getSimpleFilter("customer_id"
                        ,lst_save_family.get(0).getCustomer_id()));

        if(lst_family != null && lst_family.size() != 0){
            for(Object object : lst_family){
                Family obj_family = (Family)object;
                this.removeObject(obj_family);
            }
        }
        for(Family obj_family : lst_save_family){
            preObject(obj_family);
            saveObject(obj_family);
        }
    }
}
