package com.cnpc.pms.base.file.utils;


import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cnpc.pms.base.entity.Community;
import com.cnpc.pms.base.entity.CommunityInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by cyh(275923233@qq.com) on 2015/9/16.
 */
public class ListOneToDB {

    private Community community = new Community();
    private CommunityInfo communityInfo = new CommunityInfo();
    private OperationDB operationDB = new OperationDB();
    Logger logger = Logger.getLogger("ListOneToDB.class");

    public void exlToDB(String filePath) {
        boolean flag = true;

        try {
            // 文件流指向excel文件
            FileInputStream fin = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fin);// 创建工作薄
            XSSFSheet sheet = workbook.getSheetAt(0);// 得到工作表
            XSSFRow row = null;// 对应excel的行
            XSSFCell cell = null;// 对应excel的列
            int totalRow = sheet.getLastRowNum();// 得到excel的总记录条数
            for (int i = 1; i <= totalRow; i++) {
                row = sheet.getRow(i);
                if (row.getCell(0) != null && !"".equals(row.getCell(0)) && row.getCell(1) != null && !"".equals(row.getCell(1))) {
                    cell = row.getCell((short) 1);
                    this.community.setCname(cell.toString());
                    cell = row.getCell((short) 2);
                    this.community.setCclass(cell.toString());
                    cell = row.getCell((short) 3);
                    this.community.setCode(cell.toString());
                    cell = row.getCell((short) 4);
                    if (null == cell.toString() || "" == cell.toString()) {
                        this.community.setTotalfamily(0);
                    } else {
                        this.community.setTotalfamily(Double.parseDouble(cell.toString()));
                    }
                    cell = row.getCell((short) 5);
                    if (null == cell.toString() || "" == cell.toString()) {
                        this.community.setTotalperson(0);
                    } else {
                        this.community.setTotalperson(Double.parseDouble(cell.toString()));
                    }
                    cell = row.getCell((short) 6);
                    if (null == cell.toString() || "" == cell.toString()) {
                        this.community.setTotalarea(0);
                    } else {
                        this.community.setTotalarea(Double.parseDouble(cell.toString()));
                    }
                    cell = row.getCell((short) 7);
                    this.community.setIntroduction(cell.toString());

                    //System.out.println(jdbcTemplate.toString());
                    String sql = "insert into t_community(" +
                            "cname," +
                            "cclass," +
                            "ccode," +
                            "totalfamily," +
                            "totalperson," +
                            "totalarea," +
                            "introduction," +
                            "provinceid," +
                            "cityid," +
                            "townid ) values('" +
                            this.community.getCname() + "','" +
                            this.community.getCclass() + "','" +
                            this.community.getCode() + "','" +
                            this.community.getTotalfamily() + "','" +
                            this.community.getTotalperson() + "','" +
                            this.community.getTotalarea() + "','" +
                            this.community.getIntroduction() + "','"+
                            this.community.getProvinceid() +"','"+
                            this.community.getCityid() +"','"+
                            this.community.getTownid() +"')";
                    //删除
                    //String sql2="delete from person where id="+1001;
                    System.out.println(sql);

                    //执行insert、update、delete
                    int temp = operationDB.update(sql);

                    if (temp>0) {
                        logger.info("插入数据成功！ 居委会：" + this.community.getCname() + ",  街道：" + this.community.getCode());
                    } else {
                        logger.info("插入数据失败！ 居委会：" + this.community.getCname());
                    }
                }
            }

            sheet = workbook.getSheetAt(1);// 得到工作表
            totalRow = sheet.getLastRowNum();// 得到excel的总记录条数

            for (int i = 1; i <= totalRow; i++) {
                row = sheet.getRow(i);
                if (row.getCell(0) != null && !"".equals(row.getCell(0)) && row.getCell(1) != null && !"".equals(row.getCell(1))) {
                    cell = row.getCell((short) 1);
                    this.communityInfo.setCname(cell.toString());
                    cell = row.getCell((short) 2);
                    this.communityInfo.setScname(cell.toString());
                    cell = row.getCell((short) 3);
                    if (null == cell.toString() || "" == cell.toString()){
                        this.communityInfo.setTotalfloor(0);
                    }else{
                        this.communityInfo.setTotalfloor(Double.parseDouble(cell.toString()));
                    }
                    cell = row.getCell((short) 4);
                    if (null == cell.toString() || "" == cell.toString()) {
                        this.communityInfo.setTotalfamily(0);
                    } else {
                        this.communityInfo.setTotalfamily(Double.parseDouble(cell.toString()));
                    }
                    cell = row.getCell((short) 5);
                    if (null == cell.toString() || "" == cell.toString() || "无".equals(cell.toString())) {
                        this.communityInfo.setAvgprice(0);
                    } else {
                        this.communityInfo.setAvgprice(Double.parseDouble(cell.toString()));
                    }
                    cell = row.getCell((short) 6);
                    if (null == cell.toString() || "" == cell.toString()) {
                        this.communityInfo.setRent(0);
                    } else {
                        this.communityInfo.setRent(Double.parseDouble(cell.toString()));
                    }

                    //System.out.println(jdbcTemplate.toString());
                    String sql = "insert into t_communityinfo(" +
                            "cname," +
                            "scname," +
                            "totalfloor," +
                            "totalfamily," +
                            "avgprice," +
                            "rent ) values('" +
                            this.communityInfo.getCname() + "','" +
                            this.communityInfo.getScname() + "','" +
                            this.communityInfo.getTotalfloor() + "','" +
                            this.communityInfo.getTotalfamily() + "','" +
                            this.communityInfo.getAvgprice() + "','" +
                            this.communityInfo.getRent() + "')";

                    //删除
                    //String sql2="delete from person where id="+1001;

                    System.out.println(sql);

                    //执行insert、update、delete
                    int temp = operationDB.update(sql);
                    if (temp>0) {
                        logger.info("插入数据成功！ 社区：" + this.communityInfo.getCname() + ",  小区：" + this.communityInfo.getScname());
                    } else {
                        logger.info("插入数据失败！ 社区：" + this.communityInfo.getCname());
                    }
                }
            }

            operationDB.close();
            fin.close();
        } catch (FileNotFoundException e) {
            flag = false;
            e.printStackTrace();
        } catch (IOException ex) {
            flag = false;
            ex.printStackTrace();
        }
    }

}
