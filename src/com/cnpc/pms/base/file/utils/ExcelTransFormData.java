package com.cnpc.pms.base.file.utils;

import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.personal.entity.City;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.VillageManager;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunning on 2016/7/21.
 */
public class ExcelTransFormData {
    public static final String SHEQU = "1-社区信息";
    public static final String LOUFANG = "2-房屋信息(楼房)";
    public static final String SHANGYE = "2-房屋信息(商业楼宇)";
    public static final String PINGFANG = "2-房屋信息(平房)";
    public static final String HUXING = "3-楼房户型信息";
    public static void getListObject(String filePath){
        HSSFWorkbook hssFWork=null;
        HSSFSheet hssfSheet=null;
        try {
            hssFWork=new HSSFWorkbook(new FileInputStream(filePath));
            String sheetName=null;
            for(int i=0;i<hssFWork.getNumberOfSheets();i++){
                hssfSheet=hssFWork.getSheetAt(i);
                if(hssfSheet!=null&&"".equals(hssfSheet)){
                    sheetName=hssfSheet.getSheetName();
                    if(sheetName!=null&&"".equals(sheetName)){
                        if(SHEQU.equals(sheetName)){

                        }else if(LOUFANG.equals(sheetName)){

                        }else if(SHANGYE.equals(sheetName)){

                        }else if(PINGFANG.equals(sheetName)){

                        }
                    }
                }
            };



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<?> getVillage(HSSFSheet hssfSheet){
       List list= new ArrayList<Map>();
        Map map=new HashMap<String,String>();
        HSSFRow hssfRow=hssfSheet.getRow(4);
        String cityName=hssfRow.getCell(1).toString();
        map.put("cityName",cityName);
        String quName=hssfRow.getCell(3).toString();
        map.put("quName",quName);
        String streeName=hssfRow.getCell(5).toString();
        map.put("streeName",streeName);
        HSSFRow hssfRow1=hssfSheet.getRow(5);
        String shequName=hssfRow1.getCell(1).toString();
        map.put("shequName",shequName);
        String phone=hssfRow1.getCell(3).toString();
        map.put("phone",phone);
        String juweohuiAddress=hssfRow.getCell(5).toString();
        map.put("juweohuiAddress",juweohuiAddress);
        HSSFRow hssfRow2=hssfSheet.getRow(6);
        String totalHuShu=hssfRow2.getCell(1).toString();
        map.put("totalHuShu",totalHuShu);
        String totalPerpore=hssfRow2.getCell(3).toString();
        map.put("totalPerpore",totalPerpore);
        String totalMianJi=hssfRow2.getCell(5).toString();
        map.put("totalMianJi",totalMianJi);
        HSSFRow hssfRow3=hssfSheet.getRow(7);
        String xiaqufanwei=hssfRow3.getCell(1).toString();
        map.put("xiaqufanwei",xiaqufanwei);
    list.add(map);
        return  list;
    }

    public Village getVillageByVillageName(String name){
        VillageManager villageManager=(VillageManager) SpringHelper.getBean("villageManager");
        return villageManager.getVillageByVillageName(name);
    }
}
