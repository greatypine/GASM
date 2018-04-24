 package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.HouseStyle;
import com.cnpc.pms.personal.entity.ViewAddressCustomer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 房屋户型结构业务类接口
 * Created by liuxiao on 2016/5/24 0024.
 */
public interface HouseStyleManager extends IManager {

//    Boolean importExcelToHouseType

    /**
     * 查询房屋户型展示数据
     * @param conditions 数据条件
     * @return 房屋户型数据集合
     */
    Map<String,Object> showHouseStyleData(QueryConditions conditions);

    /**
     * 根据导入的Excel文件导入数据
     * @param lst_houseStyle Excel文件集合
     */
    void saveOrUpdateHouseStyleByExcel(List<File> lst_houseStyle) throws Exception;

    /**
     * 根据房屋id获取户型对象
     * @param house_id 房屋id
     * @return 户型对象
     */
    HouseStyle getHouseStyleByHouseId(Long house_id);

    /**
     * 根据门店编码获取所有的门店下的户型文件
     */
    void saveHouseStyleForRar() throws Exception;


    /**
     * 根据门店编码获取所有的门店下的户型文件
     */
    void saveHouseStyleForExcel() throws Exception;

    /**
     * 根据门店编码获取所有的门店下的户型文件
     */
    void saveHouseStyleForTownExcel() throws Exception;


    void saveOrUpdateHouseStyleAndCustomer(List<File> lst_houseStyle) throws IOException, Exception;
    
    /**
     * 获取所有楼房的id集合
     * @return
     */
    String getHousehouseIds();

    List<Map<String,String>> getTinyVillageHousePic(ViewAddressCustomer viewAddressCustomer);
    List<Map<String,Object>> getnewTinyVillageHousePic(ViewAddressCustomer viewAddressCustomer);

}
