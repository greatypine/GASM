package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.Building;
import com.cnpc.pms.personal.entity.Family;

import java.util.List;
import java.util.Map;

/**
 * 家庭成员相关业务接口
 * Created by liuxiao on 2016/5/25 0025.
 */
public interface FamilyManager extends IManager {

    /**
     * 保存家庭成员信息，保存前删除
     * @param lst_save_family 家庭成员信息集合
     */
    void saveFamilyInfo(List<Family> lst_save_family);
}
