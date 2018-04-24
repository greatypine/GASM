package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.dict.entity.Dict;
import com.cnpc.pms.base.dict.manager.DictManager;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.HouseStyleDao;
import com.cnpc.pms.personal.entity.*;
import com.cnpc.pms.personal.manager.*;
import com.cnpc.pms.utils.CompressFile;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;
import com.cnpc.pms.utils.excel.ExcelReaderUtil;
import com.cnpc.pms.utils.excel.IRowReader;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 房屋户型业务实现类
 * Created by liuxiao on 2016/5/24 0024.
 */
public class HouseStyleManagerImpl extends BizBaseCommonManager implements HouseStyleManager,IRowReader {

    PropertiesValueUtil propertiesValueUtil = null;

    private String folder_path = null;

    /**
     * 到处户型excel单元格公共样式
     */
    CellStyle cellStyle_common = null;

    private List<Integer> lst_sheet = new ArrayList<Integer>();

    private Map<Integer,List<Map<Integer,String>>> map_sheet_data = new HashMap<Integer, List<Map<Integer, String>>>();

    /**
     * 查询房屋户型展示数据
     *
     * @param conditions 数据条件
     * @return 房屋户型数据集合
     */
    @Override
    public Map<String, Object> showHouseStyleData(QueryConditions conditions) {
        HouseStyleDao dao_houseStyle = (HouseStyleDao) SpringHelper.getBean(HouseStyleDao.class.getName());
        //查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();

        for (Map<String, Object> map_where : conditions.getConditions()) {
            if ("name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND tv.name LIKE '").append(map_where.get("value")).append("'");
            }

            if ("address".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND tv.address LIKE '").append(map_where.get("value")).append("'");
            }
            if ("address".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND tv.address LIKE '").append(map_where.get("value")).append("'");
            }

            if ("building_id".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND h.building_id = '").append(map_where.get("value")).append("'");
            }

            if ("house_no".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND h.building_house_no LIKE '").append(map_where.get("value")).append("'");
            }

            if ("floor_number".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND h.commercial_floor_number LIKE '").append(map_where.get("value")).append("'");
            }
        }
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "房屋户型信息");
        map_result.put("data", dao_houseStyle.queryHouseStyleData(sb_where.toString(), obj_page));
        return map_result;
    }

    /**
     * 根据导入的Excel保存数据
     *
     * @param lst_houseStyle Excel文件集合
     */
    @Override
    public void saveOrUpdateHouseStyleByExcel(List<File> lst_houseStyle) throws Exception {
        //小区业务类对象
        TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
        //楼房业务类对象
        BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
        //房屋业务类对象
        HouseManager houseManager = (HouseManager) SpringHelper.getBean("houseManager");
        //门店业务类对象
        StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");

        HouseStyleDao dao_houseStyle = (HouseStyleDao) SpringHelper.getBean(HouseStyleDao.class.getName());

        for (File file_excel : lst_houseStyle) {
            //读取excel文件
            InputStream is_excel = new FileInputStream(file_excel);
            POIFSFileSystem pfs_fileSystem = new POIFSFileSystem(is_excel);
            //Excel工作簿
            Workbook wb_excel;
            Sheet sheet_data;
            try {
                //解析2003的xls模式的excel
                wb_excel = new HSSFWorkbook(pfs_fileSystem);
            } catch (IOException e) {
                //如果2003模式解析异常，尝试解析2007模式
                wb_excel = new XSSFWorkbook(file_excel.getAbsolutePath());
            }
            String str_store_name = file_excel.getName().substring(0, file_excel.getName().lastIndexOf("."));

            //获取工作簿数量
            int nSheetsCount = wb_excel.getNumberOfSheets();
            //如果如同模板一样是3个工作簿，取第三个
            if (nSheetsCount == 3) {
                sheet_data = wb_excel.getSheetAt(2);
            } else if (nSheetsCount == 1) {     //如果是一个工作簿只取第一个
                sheet_data = wb_excel.getSheetAt(0);
            } else {    //其他的，因为不知道需要解析第几个，是否有模板内容，所以无法解析
                throw new Exception("Sheet数量异常，无法解析内容");
            }

            //获取行数
            int n_rowCount = sheet_data.getPhysicalNumberOfRows();
            //如果只有两行说明，只有表头没有导入数据
            if (n_rowCount <= 2) {
                throw new Exception("工作簿中不足两行，缺少导入数据");
            }

            //合并行的行号
            String str_unit_no = null;

            TinyVillage obj_tiny_village = null;
            Building obj_building = null;
            Map<String, Building> map_building = null;
            Map<Long, Map<String,House>> map_house = null;
            Store obj_store = storeManager.getStoreByName(str_store_name);
            if (obj_store == null) {
                obj_store = new Store();
            }
            for (int i = 2; i < n_rowCount; i++) {
                //每行的数据值
                Row row_excel_data = sheet_data.getRow(i);
                String house_no;
                try {
                    house_no = String.valueOf(Double.valueOf(getCellValue(row_excel_data.getCell(6))).intValue());
                } catch (Exception e) {
                    house_no = getCellValue(row_excel_data.getCell(6));
                }

                if ("".equals(house_no)) {
                    continue;
                }
                //小区名
                if (!"".equals(getCellValue(row_excel_data.getCell(1)))) {
                    String str_village_name = getCellValue(row_excel_data.getCell(1));
                   // obj_tiny_village = tinyVillageManager.getTinyVilageByName(obj_store.getTown_id(),str_village_name);
                    if (obj_tiny_village == null) {
                        obj_tiny_village = new TinyVillage();
                        map_building = new HashMap<String, Building>();
                        map_house = new HashMap<Long, Map<String, House>>();
                    } else {
                        dao_houseStyle.updateBuildingAndHouseStatus(obj_tiny_village.getId());
                        map_building = buildingManager.getBuildingMapByTinyVillageId(obj_tiny_village.getId());
                        map_house = houseManager.getHouseMapByBuildingId(map_building);
                    }
                    obj_tiny_village.setName(str_village_name);
                    //obj_tiny_village.setTown_id(obj_store.getTown_id());
                    //小区地址
                    obj_tiny_village.setAddress(getCellValue(row_excel_data.getCell(2)));
                    obj_tiny_village.setStatus(0);
                    preObject(obj_tiny_village);
                    tinyVillageManager.save(obj_tiny_village);
                }

                if (!"".equals(getCellValue(row_excel_data.getCell(3)))) {
                    String str_building_name = getCellValue(row_excel_data.getCell(3));
                    assert map_building != null;
                    if (map_building.containsKey(str_building_name)) {
                        obj_building = map_building.get(str_building_name);
                    } else {
                        obj_building = new Building();
                    }

                    obj_building.setTinyvillage_id(obj_tiny_village.getId());
                    obj_building.setVillage_id(obj_tiny_village.getVillage_id());
                    //楼号
                    obj_building.setName(str_building_name);
                    obj_building.setStatus(0);
                    preObject(obj_building);
                    buildingManager.save(obj_building);
                }

                //单元号
                if (!"".equals(getCellValue(row_excel_data.getCell(4)))) {
                    str_unit_no = getCellValue(row_excel_data.getCell(4));
                }

                House obj_house = null;

                String key = str_unit_no + "," + "house_no";
                assert map_house != null;
                if (map_house.containsKey(obj_building.getId()) && map_house.get(obj_building.getId()).containsKey(key)) {
                    obj_house = map_house.get(obj_building.getId()).get(key);
                } else {
                    obj_house = new House();
                }
                assert obj_building != null;
                obj_house.setBuilding_id(obj_building.getId());
                obj_house.setBuilding_unit_no(str_unit_no);
                //楼层
                String str_floor_num = getCellValue(row_excel_data.getCell(5));
                if (checkValue(str_floor_num)) {
                    obj_house.setCommercial_floor_number(str_floor_num);
                }
                //房号
                obj_house.setHouse_no(house_no);
                obj_house.setBuilding_house_no(house_no);
                obj_house.setStatus(0);
                preObject(obj_house);
                houseManager.save(obj_house);

                HouseStyle obj_houseStyle = getHouseStyleByHouseId(obj_house.getId());
                if (obj_houseStyle == null) {
                    obj_houseStyle = new HouseStyle();
                }
                obj_houseStyle.setHouse_id(obj_house.getId());
                obj_houseStyle.setHouse_area(getCellValue(row_excel_data.getCell(7)));
                obj_houseStyle.setHouse_toward(getCellValue(row_excel_data.getCell(8)));
                obj_houseStyle.setHouse_style(getCellValue(row_excel_data.getCell(9)));
                obj_houseStyle.setHouse_pic(getCellValue(row_excel_data.getCell(10)));
//                obj_houseStyle.setOwn_name(getCellValue(row_excel_data.getCell(11)));
//                obj_houseStyle.setOwn_phone(getCellValue(row_excel_data.getCell(12)));
//                String str_ownnum = getCellValue(row_excel_data.getCell(13));
//                if (checkValue(str_ownnum)) {
//                    obj_houseStyle.setOwn_num(Double.valueOf(str_ownnum).intValue());
//                }
                obj_houseStyle.setStatus(0);
                preObject(obj_houseStyle);
                super.save(obj_houseStyle);
            }
            is_excel.close();
        }
    }

    /**
     * 根据房屋id获取户型对象
     *
     * @param house_id 房屋id
     * @return 户型对象
     */
    @Override
    public HouseStyle getHouseStyleByHouseId(Long house_id) {
        List<?> lst_data = getList(FilterFactory.getSimpleFilter("house_id", house_id));
        if (lst_data != null && lst_data.size() > 0) {
            return (HouseStyle) lst_data.get(0);
        }
        return null;
    }

    /**
     * 根据门店编码获取所有的门店下的户型文件
     */
    @Override
    public void saveHouseStyleForRar() throws Exception {

        String str_file_name = "house_info_proofread";
        String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
        //配置文件中的路径
        String str_filepath = strRootpath.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));

        File file_template = new File(str_filepath);
        //门店业务类对象
        StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
        //小区业务类对象
        TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
        //楼房业务类对象
        BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
        //房屋业务类对象
        HouseManager houseManager = (HouseManager) SpringHelper.getBean("houseManager");
        //社区业务类对象
        VillageManager villageManager = (VillageManager) SpringHelper.getBean("villageManager");

        List<?> lst_store = storeManager.getList();

        for (Object obj_store_object : lst_store) {
            //获取门店的对象
            Store store = (Store) obj_store_object;
            //社区ID集合
            List<Long> lst_villgae_id = villageManager.getVillageIdByTownId(Long.parseLong(store.getTown_id()));

            //小区列表
            List<?> lst_tinyVillage = tinyVillageManager.getList(FilterFactory.getSimpleFilter("town_id", store.getTown_id())
                    .appendOr(FilterFactory.getInFilter("village_id", lst_villgae_id)));

            String str_file_dir_path = file_template.getAbsolutePath()
                    .substring(0, file_template.getAbsolutePath().lastIndexOf(File.separator) + 1);

            int sort_no = 1;
            //所有的excel文件用于压缩文件
            List<File> lst_rar_excel = new ArrayList<File>();
            List<File> lst_all_excel = new ArrayList<File>();
            //循环小区
            for (Object obj_temp_tv : lst_tinyVillage) {
                int nRowIndex = 4;
                TinyVillage obj_tv = (TinyVillage) obj_temp_tv;
                String str_newfilepath = str_file_dir_path + store.getName()
                        + "_" + obj_tv.getName() + ".xls";
                File file_new = new File(str_newfilepath);
                boolean b_flag;
                if (file_new.exists()) {
                    b_flag = file_new.delete();
                    if (!b_flag) {
                        throw new Exception("无法删除旧的excel文件");
                    }
                }
                b_flag = file_new.createNewFile();
                if (!b_flag) {
                    throw new Exception("无法创建新的excel文件");
                }
                FileCopyUtils.copy(file_template, file_new);
                FileInputStream fis_input_excel = new FileInputStream(file_new);
                Workbook wb_houseinfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
                setCellStyle_common(wb_houseinfo);
                Sheet sh_data = wb_houseinfo.getSheetAt(1);

                List<?> lst_building = buildingManager.getList(FilterFactory.getSimpleFilter("tinyvillage_id", obj_tv.getId()));
                //开始行
                int n_building_start = 4;
                String str_building_name = null;
                String unit_no = null;
                int n_unit_start = 4;
                for (int nBuildIndex = 0; nBuildIndex < lst_building.size(); nBuildIndex++) {
                    Building obj_building = (Building) lst_building.get(nBuildIndex);
                    List<?> lst_house = houseManager.getList(FilterFactory.getSimpleFilter("building_id", obj_building.getId()));
                    for (int nHouseIndex = 0; nHouseIndex < lst_house.size(); nHouseIndex++) {
                        if (str_building_name == null) {
                            str_building_name = obj_building.getName();
                        } else if (!obj_building.getName().equals(str_building_name)) {
                            sh_data.addMergedRegion(new CellRangeAddress(n_building_start, nRowIndex - 1, 2, 2));
                            str_building_name = obj_building.getName();
                            n_building_start = nRowIndex;
                        }

                        House obj_house = (House) lst_house.get(nHouseIndex);
                        if (unit_no == null) {
                            unit_no = obj_house.getBuilding_unit_no();
                        } else if (!unit_no.equals(obj_house.getBuilding_unit_no())) {
                            sh_data.addMergedRegion(new CellRangeAddress(n_unit_start, nRowIndex - 1, 3, 3));
                            unit_no = obj_house.getBuilding_unit_no();
                            n_unit_start = nRowIndex;
                        }

                        sh_data.createRow(nRowIndex);
                        Row obj_row = sh_data.getRow(nRowIndex);
                        HouseStyle obj_house_style = getHouseStyleByHouseId(obj_house.getId());
                        if (obj_house_style == null) {
                            obj_house_style = new HouseStyle();
                        }
                        setCellValue(obj_row, 0, obj_tv.getName());
                        setCellValue(obj_row, 1, obj_tv.getAddress());
                        setCellValue(obj_row, 2, obj_building.getName());
                        setCellValue(obj_row, 3, obj_house.getBuilding_unit_no());
                        setCellValue(obj_row, 4, obj_house.getCommercial_floor_number());
                        setCellValue(obj_row, 5, obj_house.getBuilding_house_no());
                        setCellValue(obj_row, 6, obj_house_style.getHouse_area());
                        setCellValue(obj_row, 7, obj_house_style.getHouse_toward());
                        setCellValue(obj_row, 8, obj_house_style.getHouse_style());
                        setCellValue(obj_row, 9, obj_house_style.getHouse_pic());
                        nRowIndex++;

                        if (nHouseIndex == lst_house.size() - 1) {
                            sh_data.addMergedRegion(new CellRangeAddress(n_unit_start, nRowIndex - 1, 3, 3));
                        }
                    }
                    if (nBuildIndex == lst_building.size() - 1 && nRowIndex != 2) {
                        sh_data.addMergedRegion(new CellRangeAddress(n_building_start, nRowIndex - 1, 2, 2));
                    }
                }
                if (nRowIndex != 4) {
                    sh_data.addMergedRegion(new CellRangeAddress(4, nRowIndex - 1, 0, 0));
                    sh_data.addMergedRegion(new CellRangeAddress(4, nRowIndex - 1, 1, 1));
                }
                if (sh_data.getPhysicalNumberOfRows() > 4) {
                    lst_rar_excel.add(file_new);
                }
                lst_all_excel.add(file_new);
                FileOutputStream fis_out_excel = new FileOutputStream(file_new);
                wb_houseinfo.write(fis_out_excel);
                fis_out_excel.close();
                fis_input_excel.close();
                sort_no++;
            }
            File file_zip = new File(str_file_dir_path + store.getName() + ".zip");
            boolean b_flag;
            if (file_zip.exists()) {
                b_flag = file_zip.delete();
                if (!b_flag) {
                    throw new Exception("无法删除旧的压缩包文件");
                }
            }
            b_flag = file_zip.createNewFile();
            if (!b_flag) {
                throw new Exception("无法删除旧的压缩包文件");
            }
            CompressFile.ZipFiles(lst_rar_excel.toArray(new File[lst_rar_excel.size()]), file_zip);
            for (File file_temp : lst_all_excel) {
                file_temp.delete();
            }
        }

    }

    @Override
    public void saveHouseStyleForExcel() throws Exception {
        String str_file_name = "house_info_proofread";
        String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
        //配置文件中的路径
        String str_filepath = strRootpath.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));

        File file_template = new File(str_filepath);
        //门店业务类对象
        StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
        //小区业务类对象
        TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
        //楼房业务类对象
        BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
        //房屋业务类对象
        HouseManager houseManager = (HouseManager) SpringHelper.getBean("houseManager");
        //社区业务类对象
        VillageManager villageManager = (VillageManager) SpringHelper.getBean("villageManager");

        List<?> lst_store = storeManager.getList();

        for (Object obj_store_object : lst_store) {
            List<File> lst_all_excel = new ArrayList<File>();
            //获取门店的对象
            Store store = (Store) obj_store_object;
            //社区ID集合
            List<Long> lst_villgae_id = villageManager.getVillageIdByTownId(Long.parseLong((store.getTown_id())));

            StringBuilder sb_village_id = new StringBuilder();

            for (Long long_villgae_id : lst_villgae_id) { 
                sb_village_id.append(",").append(long_villgae_id);
            }
            if(sb_village_id.length() == 0){
                continue;
            }
            String str_village_id = sb_village_id.substring(1, sb_village_id.length());

            String str_file_dir_path = getFolder_path();


            String str_newfilepath = str_file_dir_path + store.getName()
                    + ".xls";
            File file_new = new File(str_newfilepath);
            lst_all_excel.add(file_new);
            boolean b_flag;
            if (file_new.exists()) {
                b_flag = file_new.delete();
                if (!b_flag) {
                    throw new Exception("无法删除旧的excel文件");
                }
            }
            b_flag = file_new.createNewFile();
            if (!b_flag) {
                throw new Exception("无法创建新的excel文件");
            }
            FileCopyUtils.copy(file_template, file_new);
            FileInputStream fis_input_excel = new FileInputStream(file_new);
            Workbook wb_houseinfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
            setCellStyle_common(wb_houseinfo);
            Sheet sh_data = wb_houseinfo.getSheetAt(1);
            //开始行
            int nRowIndex = 4;
            int n_building_start = 4;
            int n_unit_start = 4;
            int nStartIndex = 4;
            HouseStyleDao dao_houseStyle = (HouseStyleDao) SpringHelper.getBean(HouseStyleDao.class.getName());
            List<?> lst_data = dao_houseStyle.getHouseListByTown(Long.parseLong(store.getTown_id()), str_village_id);
            if (lst_data == null) {
                lst_data = new ArrayList<Object>();
            }
            //循环小区
            for (int i = 0; i < lst_data.size(); i++) {
                if(nRowIndex % 60000 == 0){
                    sh_data.addMergedRegion(new CellRangeAddress(nStartIndex, nRowIndex, 0, 0));
                    sh_data.addMergedRegion(new CellRangeAddress(nStartIndex, nRowIndex, 1, 1));
                    sh_data.addMergedRegion(new CellRangeAddress(n_building_start, nRowIndex, 2, 2));
                    sh_data.addMergedRegion(new CellRangeAddress(n_unit_start, nRowIndex, 3, 3));
                    FileOutputStream fis_out_excel = new FileOutputStream(file_new);
                    wb_houseinfo.write(fis_out_excel);
                    fis_out_excel.close();
                    fis_input_excel.close();

                    str_newfilepath = str_file_dir_path + store.getName() + (nRowIndex / 60000)
                            + ".xls";
                    file_new = new File(str_newfilepath);
                    lst_all_excel.add(file_new);
                    if (file_new.exists()) {
                        b_flag = file_new.delete();
                        if (!b_flag) {
                            throw new Exception("无法删除旧的excel文件");
                        }
                    }
                    b_flag = file_new.createNewFile();
                    if (!b_flag) {
                        throw new Exception("无法创建新的excel文件");
                    }
                    FileCopyUtils.copy(file_template, file_new);
                    fis_input_excel = new FileInputStream(file_new);
                    wb_houseinfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
                    setCellStyle_common(wb_houseinfo);
                    sh_data = wb_houseinfo.getSheetAt(1);
                    nRowIndex = 4;
                    n_building_start = 4;
                    n_unit_start = 4;
                    nStartIndex = 4;
                }
                Map<String,Object> map_data = (Map<String,Object>)lst_data.get(i);
                String str_building_name = ValueUtil.getStringValue(map_data.get("楼号"));
                String unit_no = ValueUtil.getStringValue(map_data.get("单元号"));
                String str_village_name = ValueUtil.getStringValue(map_data.get("小区名"));
                sh_data.createRow(nRowIndex);
                Row obj_row = sh_data.getRow(nRowIndex);
                setCellValue(obj_row, 0, str_village_name);
                setCellValue(obj_row, 1, ValueUtil.getStringValue(map_data.get("小区地址")));
                setCellValue(obj_row, 2, str_building_name);
                setCellValue(obj_row, 3, unit_no);
                setCellValue(obj_row, 4, ValueUtil.getStringValue(map_data.get("楼层")));
                setCellValue(obj_row, 5, ValueUtil.getStringValue(map_data.get("房间号")));
                setCellValue(obj_row, 6, ValueUtil.getStringValue(map_data.get("面积")));
                setCellValue(obj_row, 7, ValueUtil.getStringValue(map_data.get("朝向")));
                setCellValue(obj_row, 8, ValueUtil.getStringValue(map_data.get("户型")));
                setCellValue(obj_row, 9, ValueUtil.getStringValue(map_data.get("户型图")));
                setCellValue(obj_row, 10, ValueUtil.getStringValue(map_data.get("姓名")));
                setCellValue(obj_row, 11, ValueUtil.getStringValue(map_data.get("性别")));
                setCellValue(obj_row, 12, ValueUtil.getStringValue(map_data.get("年龄")));
                setCellValue(obj_row, 13, ValueUtil.getStringValue(map_data.get("电话")));
                setCellValue(obj_row, 14, ValueUtil.getStringValue(map_data.get("照片")));
                setCellValue(obj_row, 15, ValueUtil.getStringValue(map_data.get("民族")));
                setCellValue(obj_row, 16, ValueUtil.getStringValue(map_data.get("住房性质")));
                setCellValue(obj_row, 17, ValueUtil.getStringValue(map_data.get("职业")));
                setCellValue(obj_row, 18, ValueUtil.getStringValue(map_data.get("户收入")));
                setCellValue(obj_row, 19, ValueUtil.getStringValue(map_data.get("加班")));
                setCellValue(obj_row, 20, ValueUtil.getStringValue(map_data.get("家庭人口数")));
                setCellValue(obj_row, 21, ValueUtil.getStringValue(map_data.get("学龄前人数")));
                setCellValue(obj_row, 22, ValueUtil.getStringValue(map_data.get("未成年人数")));
                setCellValue(obj_row, 23, ValueUtil.getStringValue(map_data.get("宠物类型")));
                setCellValue(obj_row, 25, ValueUtil.getStringValue(map_data.get("家庭人员称谓")));
                setCellValue(obj_row, 26, ValueUtil.getStringValue(map_data.get("家庭成员姓名")));
                setCellValue(obj_row, 27, ValueUtil.getStringValue(map_data.get("家庭成员电话")));
                setCellValue(obj_row, 28, ValueUtil.getStringValue(map_data.get("家庭成员年龄")));
                nRowIndex++;

                Map<String,Object> map_next_data = null;
                boolean isLast = false;
                if(i == lst_data.size() - 1){
                    map_next_data  = map_data;
                    isLast = true;
                }else{
                    map_next_data = (Map<String,Object>)lst_data.get(i + 1);
                }

                String str_tmp_build = ValueUtil.getStringValue(map_next_data.get("楼号"));
                if (!str_tmp_build.equals(str_building_name) || isLast) {
                    sh_data.addMergedRegion(new CellRangeAddress(n_building_start, nRowIndex, 2, 2));
                    n_building_start = nRowIndex + 1;
                }

                String str_tmp_util = ValueUtil.getStringValue(map_next_data.get("单元号"));
                if (!unit_no.equals(str_tmp_util)  || isLast) {
                    sh_data.addMergedRegion(new CellRangeAddress(n_unit_start, nRowIndex, 3, 3));
                    n_unit_start = nRowIndex + 1;
                }
                String str_tmp_village = ValueUtil.getStringValue(map_next_data.get("小区名"));
                if (!str_tmp_village.equals(str_village_name)  || isLast) {
                    sh_data.addMergedRegion(new CellRangeAddress(nStartIndex, nRowIndex, 0, 0));
                    sh_data.addMergedRegion(new CellRangeAddress(nStartIndex, nRowIndex, 1, 1));
                    nStartIndex = nRowIndex + 1;
                }
        }
        FileOutputStream fis_out_excel = new FileOutputStream(file_new);
        wb_houseinfo.write(fis_out_excel);
        fis_out_excel.close();
        fis_input_excel.close();

        File file_zip = new File(str_file_dir_path + store.getName() + ".zip");
        if (file_zip.exists()) {
            b_flag = file_zip.delete();
            if (!b_flag) {
                throw new Exception("无法删除旧的压缩包文件");
            }
        }
        b_flag = file_zip.createNewFile();
        if (!b_flag) {
            throw new Exception("无法删除旧的压缩包文件");
        }
//        CompressFile.ZipFiles(lst_all_excel.toArray(new File[lst_all_excel.size()]), file_zip);
        CompressFile.compressFiles2Zip(lst_all_excel.toArray(new File[lst_all_excel.size()]), file_zip.getAbsolutePath());
        for (File file_temp : lst_all_excel) {
            file_temp.delete();
        }
    }
}

    @Override
    public void saveHouseStyleForTownExcel() throws Exception {
        {
            String str_file_name = "house_info_proofread";
            String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
            //配置文件中的路径
            String str_filepath = strRootpath.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));

            File file_template = new File(str_filepath);
            //门店业务类对象
            TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
            //小区业务类对象
            TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
            //楼房业务类对象
            BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
            //房屋业务类对象
            HouseManager houseManager = (HouseManager) SpringHelper.getBean("houseManager");
            //社区业务类对象
            VillageManager villageManager = (VillageManager) SpringHelper.getBean("villageManager");

            List<?> lst_town = townManager.getList();

            for (Object obj_town : lst_town) {
                List<File> lst_all_excel = new ArrayList<File>();
                //获取门店的对象
                Town town = (Town) obj_town;
                //社区ID集合
                List<Long> lst_villgae_id = villageManager.getVillageIdByTownId(town.getId());

                StringBuilder sb_village_id = new StringBuilder();

                for (Long long_villgae_id : lst_villgae_id) {
                    sb_village_id.append(",").append(long_villgae_id);
                }
                if(sb_village_id.length() == 0){
                    continue;
                }
                String str_village_id = sb_village_id.substring(1, sb_village_id.length());

                String str_file_dir_path = getFolder_path();


                String str_newfilepath = str_file_dir_path + town.getName()
                        + ".xls";
                File file_new = new File(str_newfilepath);
                lst_all_excel.add(file_new);
                boolean b_flag;
                if (file_new.exists()) {
                    b_flag = file_new.delete();
                    if (!b_flag) {
                        throw new Exception("无法删除旧的excel文件");
                    }
                }
                b_flag = file_new.createNewFile();
                if (!b_flag) {
                    throw new Exception("无法创建新的excel文件");
                }
                FileCopyUtils.copy(file_template, file_new);
                FileInputStream fis_input_excel = new FileInputStream(file_new);
                Workbook wb_houseinfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
                setCellStyle_common(wb_houseinfo);
                Sheet sh_data = wb_houseinfo.getSheetAt(1);
                //开始行
                int nRowIndex = 4;
                int n_building_start = 4;
                int n_unit_start = 4;
                int nStartIndex = 4;
                HouseStyleDao dao_houseStyle = (HouseStyleDao) SpringHelper.getBean(HouseStyleDao.class.getName());
                List<?> lst_data = dao_houseStyle.getHouseListByTown(town.getId(), str_village_id);
                if (lst_data == null) {
                    lst_data = new ArrayList<Object>();
                }
                //循环小区
                for (int i = 0; i < lst_data.size(); i++) {
                    if(nRowIndex % 60000 == 0){
                        sh_data.addMergedRegion(new CellRangeAddress(nStartIndex, nRowIndex, 0, 0));
                        sh_data.addMergedRegion(new CellRangeAddress(nStartIndex, nRowIndex, 1, 1));
                        sh_data.addMergedRegion(new CellRangeAddress(n_building_start, nRowIndex, 2, 2));
                        sh_data.addMergedRegion(new CellRangeAddress(n_unit_start, nRowIndex, 3, 3));
                        FileOutputStream fis_out_excel = new FileOutputStream(file_new);
                        wb_houseinfo.write(fis_out_excel);
                        fis_out_excel.close();
                        fis_input_excel.close();
                        System.gc();
                        str_newfilepath = str_file_dir_path + town.getName() + (nRowIndex / 60000)
                                + ".xls";
                        file_new = new File(str_newfilepath);
                        lst_all_excel.add(file_new);
                        if (file_new.exists()) {
                            b_flag = file_new.delete();
                            if (!b_flag) {
                                throw new Exception("无法删除旧的excel文件");
                            }
                        }
                        b_flag = file_new.createNewFile();
                        if (!b_flag) {
                            throw new Exception("无法创建新的excel文件");
                        }
                        FileCopyUtils.copy(file_template, file_new);
                        fis_input_excel = new FileInputStream(file_new);
                        wb_houseinfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
                        setCellStyle_common(wb_houseinfo);
                        sh_data = wb_houseinfo.getSheetAt(1);
                        nRowIndex = 4;
                        n_building_start = 4;
                        n_unit_start = 4;
                        nStartIndex = 4;
                    }
                    Map<String,Object> map_data = (Map<String,Object>)lst_data.get(i);
                    String str_building_name = ValueUtil.getStringValue(map_data.get("楼号"));
                    String unit_no = ValueUtil.getStringValue(map_data.get("单元号"));
                    String str_village_name = ValueUtil.getStringValue(map_data.get("小区名"));
                    sh_data.createRow(nRowIndex);
                    Row obj_row = sh_data.getRow(nRowIndex);
                    setCellValue(obj_row, 0, str_village_name);
                    setCellValue(obj_row, 1, ValueUtil.getStringValue(map_data.get("小区地址")));
                    setCellValue(obj_row, 2, str_building_name);
                    setCellValue(obj_row, 3, unit_no);
                    setCellValue(obj_row, 4, ValueUtil.getStringValue(map_data.get("楼层")));
                    setCellValue(obj_row, 5, ValueUtil.getStringValue(map_data.get("房间号")));
                    setCellStyle(obj_row, 6);
                    setCellStyle(obj_row, 7);
                    setCellStyle(obj_row, 8);
                    setCellStyle(obj_row, 9);
                    setCellStyle(obj_row, 10);
                    setCellStyle(obj_row, 11);
                    setCellStyle(obj_row, 12 );
                    setCellStyle(obj_row, 13);
                    setCellStyle(obj_row, 14);
                    setCellStyle(obj_row, 15);
                    setCellStyle(obj_row, 16);
                    setCellStyle(obj_row, 17);
                    setCellStyle(obj_row, 18);
                    setCellStyle(obj_row, 19);
                    setCellStyle(obj_row, 20);
                    setCellStyle(obj_row, 21);
                    setCellStyle(obj_row, 22);
                    setCellStyle(obj_row, 23);
                    setCellStyle(obj_row, 24);
                    setCellStyle(obj_row, 25);
                    setCellStyle(obj_row, 26);
                    setCellStyle(obj_row, 27);
                    setCellStyle(obj_row, 28);
                    setCellStyle(obj_row, 29);
                    setCellStyle(obj_row, 30);
                    setCellStyle(obj_row, 31);
                    nRowIndex++;

                    Map<String,Object> map_next_data = null;
                    boolean isLast = false;
                    if(i == lst_data.size() - 1){
                        map_next_data  = map_data;
                        isLast = true;
                    }else{
                        map_next_data = (Map<String,Object>)lst_data.get(i + 1);
                    }

                    String str_tmp_build = ValueUtil.getStringValue(map_next_data.get("楼号"));
                    if (!str_tmp_build.equals(str_building_name) || isLast) {
                        sh_data.addMergedRegion(new CellRangeAddress(n_building_start, nRowIndex, 2, 2));
                        n_building_start = nRowIndex + 1;
                    }

                    String str_tmp_util = ValueUtil.getStringValue(map_next_data.get("单元号"));
                    if (!unit_no.equals(str_tmp_util)  || isLast) {
                        sh_data.addMergedRegion(new CellRangeAddress(n_unit_start, nRowIndex, 3, 3));
                        n_unit_start = nRowIndex + 1;
                    }
                    String str_tmp_village = ValueUtil.getStringValue(map_next_data.get("小区名"));
                    if (!str_tmp_village.equals(str_village_name)  || isLast) {
                        sh_data.addMergedRegion(new CellRangeAddress(nStartIndex, nRowIndex, 0, 0));
                        sh_data.addMergedRegion(new CellRangeAddress(nStartIndex, nRowIndex, 1, 1));
                        nStartIndex = nRowIndex + 1;
                    }
                }
                FileOutputStream fis_out_excel = new FileOutputStream(file_new);
                wb_houseinfo.write(fis_out_excel);
                fis_out_excel.close();
                fis_input_excel.close();

                File file_zip = new File(str_file_dir_path + town.getName() + ".zip");
                if (file_zip.exists()) {
                    b_flag = file_zip.delete();
                    if (!b_flag) {
                        throw new Exception("无法删除旧的压缩包文件");
                    }
                }
                b_flag = file_zip.createNewFile();
                if (!b_flag) {
                    throw new Exception("无法删除旧的压缩包文件");
                }
//        CompressFile.ZipFiles(lst_all_excel.toArray(new File[lst_all_excel.size()]), file_zip);
                CompressFile.compressFiles2Zip(lst_all_excel.toArray(new File[lst_all_excel.size()]), file_zip.getAbsolutePath());
                for (File file_temp : lst_all_excel) {
                    file_temp.delete();
                }
                System.gc();
            }
        }
    }

    @Override
    public void saveOrUpdateHouseStyleAndCustomer(List<File> lst_houseStyle) throws Exception {
        //门店业务类对象
        StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");

        for (File file_excel : lst_houseStyle) {
            //读取excel文件
//            InputStream is_excel = null;
            try{

                String str_store_name = file_excel.getName().substring(0, file_excel.getName().lastIndexOf("."));
                Store obj_store = storeManager.getStoreByName(str_store_name);
                if (obj_store == null) {
                    obj_store = new Store();
                }
                //获取工作簿数量
                ExcelReaderUtil.readExcel(this,file_excel.getAbsolutePath());
                for(int nSheetIndex : lst_sheet){
                    if(nSheetIndex == 1){
                        saveBuildingData(map_sheet_data.get(nSheetIndex),obj_store);
                    }else if(nSheetIndex == 2){
                        saveBungalowData(map_sheet_data.get(nSheetIndex),obj_store);
                    }
                }
            }catch (Exception e){
                throw e;
            }finally {
                lst_sheet.clear();
                map_sheet_data.clear();
            }
        }
    }

    private void saveBungalowData(List<Map<Integer,String>> sheet_house_data,Store obj_store) throws Exception {

        DictManager dictManager = (DictManager) SpringHelper.getBean("dictManager");
        //客户性别
        Map<String,Dict> map_sex_dict = dictManager.getDict("customer_sex");

        //租房性质
        Map<String,Dict> map_house_pro_dict = dictManager.getDict("house_property_resource");

        //加班频率
        Map<String,Dict> map_work_overtime_dict = dictManager.getDict("work_overtime_resource");

        //小区业务类对象
        TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
        //楼房业务类对象
        BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
        //房屋业务类对象
        HouseManager houseManager = (HouseManager) SpringHelper.getBean("houseManager");
        //员工业务类对象
        UserManager userManager = (UserManager) SpringHelper.getBean("userManager");

        FamilyManager familyManager = (FamilyManager) SpringHelper.getBean("familyManager");

        CustomerManager customerManager = (CustomerManager) SpringHelper.getBean("customerManager");

        HouseCustomerManager houseCustomerManager = (HouseCustomerManager)SpringHelper.getBean("houseCustomerManager");

//        HouseStyleDao dao_houseStyle = (HouseStyleDao) SpringHelper.getBean(HouseStyleDao.class.getName());

        Map<Long,Map<String, Building>> map_building = new HashMap<Long, Map<String, Building>>();
        Map<Long,House> map_house = new HashMap<Long, House>();
        Map<String,TinyVillage> map_tiny = new HashMap<String, TinyVillage>();
        TinyVillage obj_tiny_village = null;
        for(int nRowIndex = 4;nRowIndex < sheet_house_data.size();nRowIndex++ ){
            Map<Integer,String> row_current = sheet_house_data.get(nRowIndex);
            User obj_employee = null;
            Customer customer = null;
            String str_employee_phone = row_current.get(25);
            String str_employee_no = row_current.get(26);
            if(!"".equals(str_employee_phone)){
                obj_employee = userManager.findEmployeeByEmployeeNo(str_employee_no);
            }
            String str_name = row_current.get(5);
            if(checkValue(str_name)){
                String str_phone = row_current.get(8);
                List<?> lst_customer_object = customerManager.getList(FilterFactory.getSimpleFilter("name",str_name)
                        .appendAnd(FilterFactory.getSimpleFilter("mobilephone",str_phone)));
                if(lst_customer_object != null && lst_customer_object.size() > 0){
                    customer = (Customer)lst_customer_object.get(0);
                }else{
                    customer = new Customer();
                }
                customer.setName(str_name);
                customer.setMobilephone(str_phone);
                customer.setCustomer_pic(row_current.get(9));
                String str_sex_name = row_current.get(6);
                if(checkValue(str_sex_name)){
                    for(Dict obj_sex_dict : map_sex_dict.values()){
                        if(str_sex_name.equals(obj_sex_dict.getDictText())){
                            customer.setSex(Integer.valueOf(obj_sex_dict.getDictCode()));
                            break;
                        }
                    }
                }
                customer.setAge(getIntegerValue(row_current.get(7)));
                customer.setNationality(row_current.get(10));

                String str_house_property = row_current.get(11);
                if(checkValue(str_house_property)) {
                    for (Dict obj_hp_dict : map_house_pro_dict.values()) {
                        if (str_house_property.trim().equals(obj_hp_dict.getDictText().trim())) {
                            customer.setHouse_property(obj_hp_dict.getDictCode());
                            break;
                        }
                    }
                }
                customer.setJob(row_current.get(12));

                customer.setIncome(row_current.get(13));

                String str_work_overtime = row_current.get(14);
                if(checkValue(str_work_overtime)) {
                    for (Dict obj_wo_dict : map_work_overtime_dict.values()) {
                        if (str_work_overtime.trim().equals(obj_wo_dict.getDictText().trim())) {
                            customer.setWork_overtime(obj_wo_dict.getDictCode());
                            break;
                        }
                    }
                }
                customer.setOther(row_current.get(23));
                customer.setFamily_num(row_current.get(15));
                customer.setPreschool_num(getIntegerValue(row_current.get(16)));
                customer.setMinor_num(getIntegerValue(row_current.get(17)));
                customer.setPet_type(row_current.get(18));

                String str_symbol = null;

                String str_family_name = row_current.get(20);

//                str_symbol = str_family_name.contains("，")?"，":",";

//                String[] str_family_names = str_family_name.split(str_symbol);
//
//                String[] str_family_relations = row_current.get(19).split(str_symbol);
//
//                String[] str_family_phones = row_current.get(21).split(str_symbol);
//
//                String[] str_family_ages = row_current.get(22).split(str_symbol);

//                if(str_family_names.length != str_family_relations.length){
//                    throw new Exception("楼房数据，第"+(nRowIndex + 1) + "行，家庭成员姓名与称谓数量不一致，请检查数量与分隔符是否一致");
//                }
//
//                if(str_family_names.length != str_family_phones.length){
//                    throw new Exception("楼房数据，第"+(nRowIndex + 1) + "行，家庭成员姓名与电话数量不一致，请检查数量与分隔符是否一致");
//                }
//
//                if(str_family_names.length != str_family_ages.length){
//                    throw new Exception("楼房数据，第"+(nRowIndex + 1) + "行，家庭成员姓名与年龄数量不一致，请检查数量与分隔符是否一致");
//                }

                if(obj_employee != null){
                    customer.setEmployee_id(obj_employee.getId());
                }
                customer.setEmployee_no(str_employee_no);
                preObject(customer);
                saveObject(customer);
                List<Family> lst_save_family = new ArrayList<Family>();
//                for(int i = 0;i < str_family_names.length;i++){
//                    str_family_name = str_family_names[i];
                    if(!"".equals(str_family_name) && str_family_name != null) {
                        Family obj_family = new Family();
                        obj_family.setCustomer_id(customer.getId());
                        obj_family.setFamily_name(str_family_name);
                        obj_family.setFamily_relation(row_current.get(19));
                        obj_family.setFamily_phone(row_current.get(21));
                        String age = row_current.get(22);
                        Pattern reg = Pattern.compile("\\d+");
                        if(reg.matcher(age).matches()){
                            obj_family.setFamily_age(Integer.valueOf(age));
                        }
                        lst_save_family.add(obj_family);
                    }
//                }
                familyManager.saveFamilyInfo(lst_save_family);
              }

            String str_building_name = row_current.get(1);
            if(!"".equals(str_building_name) && (checkValue(row_current.get(2)) || checkValue(row_current.get(3))
                    || checkValue(row_current.get(4)))){
                String str_tmp_village = row_current.get(0);
                if(!"".equals(str_tmp_village)){
                    if(map_tiny.containsKey(str_tmp_village)){
                        obj_tiny_village = map_tiny.get(str_tmp_village);
                    }else{
                        obj_tiny_village = tinyVillageManager.getTinyVilageByName(Long.parseLong(obj_store.getTown_id()),str_tmp_village);
                        if(obj_tiny_village == null){
                            obj_tiny_village = new TinyVillage();
                            obj_tiny_village.setName(str_tmp_village);
                            obj_tiny_village.setTinyvillage_type(0);
                            obj_tiny_village.setTown_id(Long.parseLong(obj_store.getTown_id()));
                        }
                        obj_tiny_village.setStatus(0);
                        obj_tiny_village.setStore_id(obj_store.getStore_id());
                        preObject(obj_tiny_village);
                        tinyVillageManager.saveObject(obj_tiny_village);
                        TinyVillageCodeManager tinyVillageCodeManager=(TinyVillageCodeManager)SpringHelper.getBean("tinyVillageCodeManager");
                        tinyVillageCodeManager.saveTinyVillageCode(obj_tiny_village);
                        map_tiny.put(obj_tiny_village.getName(),obj_tiny_village);
                    }
                }
                Building obj_building = null;
                if(!map_building.containsKey(obj_tiny_village.getId())){
                    Map<String,Building> map_tmp_building
                            = buildingManager.getBuildingMapByTinyVillageId(obj_tiny_village.getId());
                    map_building.put(obj_tiny_village.getId(),map_tmp_building);
                }
                obj_building = map_building.get(obj_tiny_village.getId()).get(str_building_name);
                if(obj_building == null){
                    obj_building = new Building();
                    obj_building.setName(str_building_name);
                    obj_building.setType(0);
                    obj_building.setTinyvillage_id(obj_tiny_village.getId());
                }
                obj_building.setStatus(0);
                preObject(obj_building);
                buildingManager.saveObject(obj_building);
                map_building.get(obj_tiny_village.getId()).put(str_building_name,obj_building);
                House obj_house = null;
                if(!map_house.containsKey(obj_building.getId())){
                    obj_house = houseManager.getBungalowHouseByBuildingId(obj_building.getId());
                    if(obj_house == null){
                        obj_house = new House();
                        obj_house.setHouse_type(0);
                        obj_house.setBuilding_id(obj_building.getId());
                        obj_house.setBuilding_unit_no("1");
                        obj_house.setCommercial_floor_number("1");
                        //房号
                        obj_house.setHouse_no("101");
                        obj_house.setBuilding_house_no("101");
                        obj_house.setApprove_status(1);
                        obj_house.setHouse_type(0);
                    }
                }else{
                    obj_house = map_house.get(obj_building.getId());
                }
                obj_house.setStatus(0);
                preObject(obj_house);
                houseManager.saveObject(obj_house);
                map_house.put(obj_tiny_village.getId(),obj_house);
                HouseStyle houseStyle = new HouseStyle();
                houseStyle.setHouse_id(obj_house.getId());
                houseStyle.setHouse_area(row_current.get(2));
                houseStyle.setHouse_toward(row_current.get(3));
                houseStyle.setHouse_style(row_current.get(4));
                if(obj_employee != null){
                    houseStyle.setEmployee_id(obj_employee.getId());
                }
                if(customer != null){
                    houseStyle.setCustomer_id(customer.getId());
                    HouseCustomer houseCustomer = new HouseCustomer();
                    houseCustomer.setHouse_id(obj_house.getId());
                    houseCustomer.setCustomer_id(customer.getId());
                    houseCustomerManager.saveHouseCustomer(houseCustomer);
                }
                houseStyle.setEmployee_no(str_employee_no);
                houseStyle.setStatus(0);
                preObject(houseStyle);
                saveObject(houseStyle);
            }
        }
    }

    /**
     * 保存楼房数据
     * @param sheet_house_data 楼房数据工作簿
     * @param obj_store 门店对象
     * @throws Exception 异常抛出
     */
    private void saveBuildingData(List<Map<Integer,String>> sheet_house_data,Store obj_store) throws Exception {

        DictManager dictManager = (DictManager) SpringHelper.getBean("dictManager");
        //客户性别
        Map<String,Dict> map_sex_dict = dictManager.getDict("customer_sex");

        //租房性质
        Map<String,Dict> map_house_pro_dict = dictManager.getDict("house_property_resource");

        //加班频率
        Map<String,Dict> map_work_overtime_dict = dictManager.getDict("work_overtime_resource");

        //小区业务类对象
        TinyVillageManager tinyVillageManager = (TinyVillageManager) SpringHelper.getBean("tinyVillageManager");
        //楼房业务类对象
        BuildingManager buildingManager = (BuildingManager) SpringHelper.getBean("buildingManager");
        //房屋业务类对象
        HouseManager houseManager = (HouseManager) SpringHelper.getBean("houseManager");
        //员工业务类对象
        UserManager userManager = (UserManager) SpringHelper.getBean("userManager");

        FamilyManager familyManager = (FamilyManager) SpringHelper.getBean("familyManager");

        CustomerManager customerManager = (CustomerManager) SpringHelper.getBean("customerManager");

        HouseCustomerManager houseCustomerManager = (HouseCustomerManager) SpringHelper.getBean("houseCustomerManager");

        HouseStyleDao dao_houseStyle = (HouseStyleDao) SpringHelper.getBean(HouseStyleDao.class.getName());

        Map<String, Building> map_building = null;
        Map<Long, Map<String,House>> map_house = null;
        TinyVillage obj_tiny_village = null;
        Building obj_building = null;
        String str_unit_no = null;
        House obj_house = null;

        for(int nRowIndex = 4;nRowIndex < sheet_house_data.size();nRowIndex++ ){
            User obj_employee = null;
            Customer customer = null;
            Map<Integer,String> row_data = sheet_house_data.get(nRowIndex);
            String str_employee_phone = row_data.get(30);
            String str_employee_no = row_data.get(31);
            if(checkValue(str_employee_phone) || checkValue(str_employee_no)){
                obj_employee = userManager.findEmployeeByEmployeeNo(str_employee_no);
            }
            String str_name = row_data.get(10);
            if(checkValue(str_name)){
                String str_phone = row_data.get(13);
                List<?> lst_customer_object = customerManager.getList(FilterFactory.getSimpleFilter("name",str_name)
                        .appendAnd(FilterFactory.getSimpleFilter("mobilephone",str_phone)));
                if(lst_customer_object != null && lst_customer_object.size() > 0){
                    customer = (Customer)lst_customer_object.get(0);
                }else{
                    customer = new Customer();
                }
                customer.setName(str_name);
                customer.setMobilephone(str_phone);
                customer.setCustomer_pic(row_data.get(14));
                String str_sex_name = row_data.get(11);
                if(checkValue(str_sex_name)){
                    for(Dict obj_sex_dict : map_sex_dict.values()){
                        if(str_sex_name.equals(obj_sex_dict.getDictText())){
                            customer.setSex(Integer.valueOf(obj_sex_dict.getDictCode()));
                            break;
                        }
                    }
                }
                customer.setAge(getIntegerValue(row_data.get(12)));
                customer.setNationality(row_data.get(15));

                String str_house_property = row_data.get(16);
                if(checkValue(str_house_property)) {
                    for (Dict obj_hp_dict : map_house_pro_dict.values()) {
                        if (str_house_property.trim().equals(obj_hp_dict.getDictText().trim())) {
                            customer.setHouse_property(obj_hp_dict.getDictCode());
                            break;
                        }
                    }
                }
                customer.setJob(row_data.get(17));
                customer.setIncome(row_data.get(18));

                String str_work_overtime = row_data.get(19);
                if(checkValue(str_work_overtime)) {
                    for (Dict obj_wo_dict : map_work_overtime_dict.values()) {
                        if (str_work_overtime.trim().equals(obj_wo_dict.getDictText().trim())) {
                            customer.setWork_overtime(obj_wo_dict.getDictCode());
                            break;
                        }
                    }
                }

                customer.setOther(row_data.get(28));
                customer.setFamily_num(row_data.get(20));
                customer.setPreschool_num(getIntegerValue(row_data.get(21)));
                customer.setMinor_num(getIntegerValue(row_data.get(22)));
                customer.setPet_type(row_data.get(23));

                String str_symbol = null;

                String str_family_name = row_data.get(25);

//                str_symbol = str_family_name.contains("，")?"，":",";

//                String[] str_family_names = str_family_name.split(str_symbol);

                String str_family_relations = row_data.get(24);

                String str_family_phones = row_data.get(26);

                String str_family_ages = row_data.get(27);

//                if(str_family_names.length != str_family_relations.length){
//                    throw new Exception("楼房数据，第"+(nRowIndex + 1) + "行，家庭成员姓名与称谓数量不一致，请检查数量与分隔符是否一致");
//                }
//
//                if(str_family_names.length != str_family_phones.length){
//                    throw new Exception("楼房数据，第"+(nRowIndex + 1) + "行，家庭成员姓名与电话数量不一致，请检查数量与分隔符是否一致");
//                }
//
//                if(str_family_names.length != str_family_ages.length){
//                    throw new Exception("楼房数据，第"+(nRowIndex + 1) + "行，家庭成员姓名与年龄数量不一致，请检查数量与分隔符是否一致");
//                }

                if(obj_employee != null){
                    customer.setEmployee_id(obj_employee.getId());
                    customer.setEmployee_no(obj_employee.getEmployeeId());
                }
                preObject(customer);
                saveObject(customer);
                List<Family> lst_save_family = new ArrayList<Family>();
                if(!"".equals(str_family_name) && str_family_name != null){
                    Family obj_family = new Family();
                    obj_family.setCustomer_id(customer.getId());
                    obj_family.setFamily_name(str_family_name);
                    obj_family.setFamily_relation(str_family_relations);
                    obj_family.setFamily_phone(str_family_phones);
                    String age = str_family_ages;
                    Pattern reg = Pattern.compile("\\d+");
                    if(reg.matcher(age).matches()){
                        obj_family.setFamily_age(Integer.valueOf(str_family_ages));
                    }
                    lst_save_family.add(obj_family);
                    familyManager.saveFamilyInfo(lst_save_family);
                }
            }

            String strHouseNo =  row_data.get(5);
            if(checkValue(strHouseNo)){
                String str_village_name = row_data.get(0);
                if(checkValue(str_village_name)){
                    str_village_name = str_village_name.trim();
                    if(obj_tiny_village == null || !str_village_name.equals(obj_tiny_village.getName())){
                        obj_tiny_village = tinyVillageManager.getTinyVilageByName(Long.parseLong(obj_store.getTown_id()),str_village_name);
                        if (obj_tiny_village == null) {
                            obj_tiny_village = new TinyVillage();
                            map_building = new HashMap<String, Building>();
                            map_house = new HashMap<Long, Map<String, House>>();
                            obj_tiny_village.setTinyvillage_type(1);
                        } else {
                            dao_houseStyle.updateBuildingAndHouseStatus(obj_tiny_village.getId());
                            map_building = buildingManager.getBuildingMapByTinyVillageId(obj_tiny_village.getId());
                            map_house = houseManager.getHouseMapByBuildingId(map_building);
                        }
                        obj_tiny_village.setName(str_village_name);
                        obj_tiny_village.setTown_id(Long.parseLong(obj_store.getTown_id()));
                        //小区地址
                        obj_tiny_village.setAddress(row_data.get(1));
                        obj_tiny_village.setStatus(0);
                        obj_tiny_village.setStore_id(obj_store.getStore_id());
                        preObject(obj_tiny_village);
                        tinyVillageManager.save(obj_tiny_village);
                    }
                }
                String str_building_name = row_data.get(2);
                if (checkValue(str_building_name)) {
                    if(obj_building == null || !str_building_name.equals(obj_building.getName())){
                        assert map_building != null;
                        if (map_building.containsKey(str_building_name)) {
                            obj_building = map_building.get(str_building_name);
                        } else {
                            obj_building = new Building();
                            obj_building.setType(1);
                        }

                        obj_building.setTinyvillage_id(obj_tiny_village.getId());
                        obj_building.setVillage_id(obj_tiny_village.getVillage_id());
                        //楼号
                        obj_building.setName(str_building_name);
                        obj_building.setStatus(0);
                        preObject(obj_building);
                        buildingManager.save(obj_building);
                    }
                }
                if (checkValue(row_data.get(4)) || checkValue(row_data.get(6)) || checkValue(row_data.get(7))
                        || checkValue(row_data.get(8)) || checkValue(row_data.get(9))) {
                    String str_tmp_house_no = row_data.get(3);
                    if (!"".equals(str_tmp_house_no)) {
                        str_unit_no = str_tmp_house_no;
                    }
                    String str_house_no = row_data.get(5);
                    String key = str_unit_no + "," + str_house_no;
                    assert map_house != null;
                    if (map_house.containsKey(obj_building.getId()) && map_house.get(obj_building.getId()).containsKey(key)) {
                        obj_house = map_house.get(obj_building.getId()).get(key);
                    } else {
                        obj_house = new House();
                    }
                    assert obj_building != null;
                    obj_house.setBuilding_id(obj_building.getId());
                    obj_house.setBuilding_unit_no(str_unit_no);
                    //楼层
                    String str_floor_num = row_data.get(4);
                    if (checkValue(str_floor_num)) {
                        if (str_floor_num.contains("地下")) {
                            str_floor_num = "-1";
                        }
                        if (str_floor_num.contains("B")) {
                            str_floor_num = str_floor_num.replaceAll("B","-");
                        }
                        obj_house.setCommercial_floor_number(str_floor_num);
                    }
                    //房号
                    obj_house.setHouse_no(str_house_no);
                    obj_house.setBuilding_house_no(str_house_no);
                    obj_house.setStatus(0);
                    obj_house.setApprove_status(1);
                    obj_house.setHouse_type(1);
                    preObject(obj_house);
                    houseManager.save(obj_house);

                    HouseStyle obj_houseStyle = getHouseStyleByHouseId(obj_house.getId());
                    if (obj_houseStyle == null) {
                        obj_houseStyle = new HouseStyle();
                    }
                    obj_houseStyle.setHouse_id(obj_house.getId());
                    obj_houseStyle.setHouse_area(row_data.get(6));
                    obj_houseStyle.setHouse_toward(row_data.get(7));
                    obj_houseStyle.setHouse_style(row_data.get(8));
                    obj_houseStyle.setHouse_pic(row_data.get(9));
                    obj_houseStyle.setStatus(0);
                    if (obj_employee != null) {
                        obj_houseStyle.setEmployee_id(obj_employee.getId());
                        obj_houseStyle.setEmployee_no(obj_employee.getEmployeeId());
                    }
                    if (customer != null) {
                        obj_houseStyle.setCustomer_id(customer.getId());
                        HouseCustomer houseCustomer = new HouseCustomer();
                        houseCustomer.setHouse_id(obj_house.getId());
                        houseCustomer.setCustomer_id(customer.getId());
                        houseCustomerManager.saveHouseCustomer(houseCustomer);
                    }
                    if (checkValue(obj_houseStyle.getHouse_area()) || checkValue(obj_houseStyle.getHouse_style())
                            || checkValue(obj_houseStyle.getHouse_toward()) || checkValue(obj_houseStyle.getHouse_pic())) {
                        preObject(obj_houseStyle);
                        super.save(obj_houseStyle);
                    }
                }
            }
        }
    }

    /**
     * 判断字符串数组中是否包含某个字符串
     * @param strArrays 字符串数组
     * @param str_value 字符串对象
     * @return 是否包含
     */
    private String getArrayHave(String[] strArrays,String str_value){
        for (String strArray : strArrays) {
            if (str_value.contains(strArray)) {//循环查找字符串数组中的每个字符串中是否包含所有查找的内容
                return strArray;//查找到了就返回真，不在继续查询
            }
        }
        return null;//没找到返回false
    }

    private Integer getIntegerValue(Object value){
        if(value != null && !"".equals(value.toString().trim())
                && !value.toString().trim().contains("无") && !value.toString().trim().contains("没")){
            return Double.valueOf(value.toString()).intValue();
        }
        return null;
    }

    private String getCellValue(Cell cell){
        if(cell == null){
            return "";
        }
        String cellValue;
        switch(cell.getCellType()){
            case Cell.CELL_TYPE_STRING:
                cellValue=cell.getRichStringCellValue().getString().trim();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                cellValue=cell.getNumericCellValue()==0D?null:String.valueOf(cell.getNumericCellValue());
                if(cellValue!=null&&cellValue.contains("E")){
                    cellValue=new BigDecimal(cellValue).toPlainString();
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue=String.valueOf(cell.getBooleanCellValue()).trim();
                break;
            case Cell.CELL_TYPE_FORMULA:
                    cellValue=cell.getCellFormula();
                    break;
            default:
                cellValue="";
        }
        return cellValue;
    }

    /**
     * 设置单元格内的值
     *
     * @param obj_row 行 对象
     * @param nCellIndex 单元格索引
     * @param value 单元格内的值
     */
    public void setCellValue(Row obj_row,int nCellIndex,Object value){
        Cell cell=obj_row.createCell(nCellIndex);
        cell.setCellStyle(getCellStyle_common());
        cell.setCellValue(new HSSFRichTextString(value==null?null:value.toString()));
    }

    public void setCellStyle(Row obj_row,int nCellIndex){
        Cell cell=obj_row.createCell(nCellIndex);
        cell.setCellStyle(getCellStyle_common());
    }


/**
 * 获取字符串对象
 * @param obj_value Object对象
 * @return 字符串对象
 */
//    public String getStringValue(Object obj_value){
//        if(checkValue(obj_value)){
//            return obj_value.toString();
//        }
//        return null;
//    }

    /**
     * 检查对象是否为空
     * @param obj_value 要坚持的对象
     * @return 检查结果
     */
    public boolean checkValue(Object obj_value){
            return null!=obj_value&&!"".equals(obj_value.toString().trim());
            }


    /**
     * 设置户型信息excel单元格样式
     */
    public void setCellStyle_common(Workbook wb){
        cellStyle_common=wb.createCellStyle();
        cellStyle_common.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle_common.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle_common.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle_common.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle_common.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        cellStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);//垂直居中
        cellStyle_common.setWrapText(true);//设置自动换行
    }

    /**
     * 获取户型信息excel单元格样式
     * @return 单元格样式
     */
    public CellStyle getCellStyle_common(){
            return cellStyle_common;
            }

    /**
     * 获取配置文件
     * @return 配置文件对象
     */
    public PropertiesValueUtil getPropertiesValueUtil(){
        if(propertiesValueUtil==null){
            propertiesValueUtil = new PropertiesValueUtil(File.separator+"conf"+File.separator+"download_config.properties");
        }
        return propertiesValueUtil;
    }

    @Override
    public void getRows(int sheetIndex, int curRow, Map<Integer, String> rowlist) {
        if(!lst_sheet.contains(sheetIndex)){
            lst_sheet.add(sheetIndex);
            map_sheet_data.put(sheetIndex,new ArrayList<Map<Integer, String>>());
        }
        map_sheet_data.get(sheetIndex).add(rowlist);
    }


    /**
     * 获取上传文件夹路径
     * @return 上传文件夹路径
     */
    public String getFolder_path() {
        if(folder_path == null){
            String FILE_ROOT = PropertiesUtil.getValue("file.root");
            folder_path = FILE_ROOT.concat(File.separator).concat("template").concat(File.separator);
        }
        return folder_path;
    }

    public void setFolder_path(String folder_path) {
        this.folder_path = folder_path;
    }
    
    public String getHousehouseIds() {
    	String houseIds="";
    	List<?> list = getList();
    	if(list!=null&&list.size()>0){
    		for (Object object : list) {
    			HouseStyle houseStyle=(HouseStyle)object;
    			houseIds+=houseStyle.getHouse_id()+",";
			}
    		houseIds=houseIds.substring(0, houseIds.length()-1);
    		return houseIds;
    	}
		return null;
	}

    @Override
    public List<Map<String, String>> getTinyVillageHousePic(ViewAddressCustomer viewAddressCustomer) {
        ViewAddressCustomerManager viewAddressCustomerManager = (ViewAddressCustomerManager)SpringHelper.getBean("viewAddressCustomerManager");
        List<?> lst_address_obj = viewAddressCustomerManager.getList(FilterFactory.getSimpleFilter("tv_id",viewAddressCustomer.getTv_id()));
        List<Map<String,String>> map_result = new ArrayList<Map<String, String>>();
        List<String> lst_checked = new ArrayList<String>();
        String web = PropertiesUtil.getValue("file.web.root").concat("house_type_image").concat("/");

        String picPath = PropertiesUtil.getValue("file.root").concat("house_type_image").concat(File.separator);
        for(Object obj_add : lst_address_obj){
            ViewAddressCustomer address = (ViewAddressCustomer)obj_add;
            if(address.getHouse_pic() != null && !"".equals(address.getHouse_pic())){
                if(lst_checked.contains(address.getHouse_pic())){
                    continue;
                }
                lst_checked.add(address.getHouse_pic());
                Map<String,String> map_content = new HashMap<String, String>();
                String path = "";
                File pic_dir = new File(picPath);
                final String house_pic = address.getHouse_pic();
                File[] file_pics = pic_dir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().contains(house_pic);
                    }
                });
                if(file_pics != null && file_pics.length != 0 ){
                    path =  web.concat(file_pics[0].getName());
                    map_content.put("name",house_pic);
                    map_content.put("path",path);
                    map_result.add(map_content);
                }
            }

        }
        return map_result;
    }

	@Override
	public List<Map<String, Object>> getnewTinyVillageHousePic(
			ViewAddressCustomer viewAddressCustomer) {
		// TODO Auto-generated method stub
		String web = PropertiesUtil.getValue("file.web.root").concat("house_type_image").concat("/");
        String picPath = PropertiesUtil.getValue("file.root").concat("house_type_image").concat(File.separator);
        List<Map<String,Object>> map_result = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> lst_address_obj=new ArrayList<Map<String,Object>>();
		HouseStyleDao dao_houseStyle = (HouseStyleDao) SpringHelper.getBean(HouseStyleDao.class.getName());
		lst_address_obj=dao_houseStyle.getHousePicByTinyId(viewAddressCustomer.getTv_id());
		 for(Map obj_add : lst_address_obj){
	                Map<String,Object> map_content = new HashMap<String, Object>();
	                String path = "";
	                File pic_dir = new File(picPath);
	                final Object house_pic = obj_add.get("house_pic");
	                if(obj_add==null){
	                	continue;
	                }
	                File[] file_pics = pic_dir.listFiles(new FileFilter() {
	                    @Override
	                    public boolean accept(File pathname) {
	                        return pathname.getName().contains(house_pic.toString());
	                    }
	                });
	                if(file_pics != null && file_pics.length != 0 ){
	                    path =  web.concat(file_pics[0].getName());
	                    map_content.put("name",house_pic);
	                    map_content.put("path",path);
	                    map_result.add(map_content);
	                }
	            

	        }
		
		return map_result;
	}
}
