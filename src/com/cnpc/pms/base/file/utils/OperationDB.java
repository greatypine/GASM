package com.cnpc.pms.base.file.utils;

import java.sql.*;

/**
 * Created by cyh(275923233@qq.com) on 2015/9/16.
 */
public class OperationDB {

    public static final String url = "jdbc:mysql://127.0.0.1:3306/dataanalysis?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "";

    public Connection conn = null;
    public PreparedStatement pst = null;
    public ResultSet rs = null;

    public OperationDB() {
        try {
            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int update(String sql) {
        boolean flag = false;
        int n = 0;
        try {
            pst = conn.prepareStatement(sql);//准备执行语句
            flag = pst.execute();

            if (flag) {
                ResultSet rs = pst.getResultSet();
            } else {
                n = pst.getUpdateCount();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n;
    }

    public ResultSet select(String sql) {
        try {
            pst = conn.prepareStatement(sql);//准备执行语句
            rs = pst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static void main(String args[]) {
        OperationDB connectionDB = new OperationDB();
        ResultSet ret = connectionDB.select("select * from t_community");

        try {
            ret = connectionDB.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                String uid = ret.getString(1);
                String ufname = ret.getString(2);
                String ulname = ret.getString(3);
                String udate = ret.getString(4);
                System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate);
            }//显示数据
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
