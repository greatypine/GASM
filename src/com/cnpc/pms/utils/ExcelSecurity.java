package com.cnpc.pms.utils;

import java.io.IOException;

import com.jxcell.CellException;
import com.jxcell.View;

public class ExcelSecurity {

    public static void encrypt(String url, String pwd) {
        View m_view = new View();
        try {
            // read excel
            m_view.read(url);
            // set the workbook open password
            m_view.write(url, pwd);
        } catch (CellException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decrypt(String url, String pwd) {
        View m_view = new View();
        try {
            // read the encrypted excel file
            m_view.read(url, pwd);
            // write without password protected
            m_view.write(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static final String FILE = "C:\\Users\\Administrator\\Desktop\\testpassword.xls";
    
    public static void main(String args[]) {
        // 下面1与2 两个方法请分开执行，可以看到效果
        //
        //1. 把g:\\test.xls 添加打开密码123
        ExcelSecurity.encrypt(FILE, "1234");
        System.out.println("success!");
        //2. 把g:\\test.xls 密码123 去除
        //AddPasswordExcel.decrypt("g:\\test.xls", "123");

    }
}
