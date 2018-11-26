package com.cnpc.pms.utils;

import com.cnpc.pms.base.paging.impl.PageInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * impala 连接工具类
 * @author gbl
 *
 */
public class ImpalaUtil {
	
	
	
	public static List<Map<String,Object>> execute(String sql){
		
		Connection con = null;  
		ResultSet rs = null;
		Statement stat=null;
        List<Map<String,Object>> list = null;
        PreparedStatement ps = null;
        try {  
        	
            
            con = HikariInner.getConnection();
            System.out.println("\n== Begin Query Results ======================");  
            //ps = con.prepareStatement(sql);
            stat = con.createStatement();
            //rs = ps.executeQuery();
            rs = stat.executeQuery(sql);
            list = convertList(rs);
           
            System.out.println("== End Query Results =======================\n\n");  
   
        } catch (SQLException e) {  
            e.printStackTrace();  
            return list;
        } catch (Exception e) {  
            e.printStackTrace(); 
            return list;
        } finally {  
            try {
                HikariInner.close(con, stat, rs);
            } catch (Exception e) {  
                e.printStackTrace();
                return list;
            }  
        }
        return list;
    }

    public static Map<String,Object> executeByPage(String sql, PageInfo pageInfo){

        Connection con = null;
        ResultSet rs = null;
        Statement stat=null;
        List<Map<String,Object>> list = null;
        PreparedStatement ps = null;
        Map<String,Object> map_result = new HashMap<String,Object>();
        try {


            con = HikariInner.getConnection();
            System.out.println("\n== Begin Query Results ======================");
            //ps = con.prepareStatement(sql);
            stat = con.createStatement();
            //rs = ps.executeQuery();
            if(pageInfo!=null){
                String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";
                rs = stat.executeQuery(sql_count);
                List<Map<String,Object>> list1 = convertList(rs);
                pageInfo.setTotalRecords(Integer.valueOf(list1.get(0).get("total").toString()));
                sql += " limit "+ pageInfo.getRecordsPerPage()+" offset "+ (pageInfo.getRecordsPerPage()*(pageInfo.getCurrentPage() - 1));
                rs = stat.executeQuery(sql);
                list = convertList(rs);
                Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
                map_result.put("pageinfo",pageInfo);
                map_result.put("total_pages", total_pages);
                map_result.put("totalRecords", pageInfo.getTotalRecords());
                map_result.put("user_active",list);
            }else{
                rs = stat.executeQuery(sql);
                list = convertList(rs);
                map_result.put("user_active",list);
            }


            System.out.println("== End Query Results =======================\n\n");

        } catch (SQLException e) {
            e.printStackTrace();
            return map_result;
        } catch (Exception e) {
            e.printStackTrace();
            return map_result;
        } finally {
            try {
                HikariInner.close(con, stat, rs);
            } catch (Exception e) {
                e.printStackTrace();
                return map_result;
            }
        }
        return map_result;
    }

    public static List<Map<String,Object>> executeGuoan(String sql){

        Connection con = null;
        ResultSet rs = null;
        Statement stat=null;
        List<Map<String,Object>> list = null;
        PreparedStatement ps = null;
        try {


            con = HikariGuoanInner.getConnection();
            System.out.println("\n== Begin Guoan Query Results ======================");
//            ps = con.prepareStatement(sql);
            stat = con.createStatement();
//            rs = ps.executeQuery();
            rs = stat.executeQuery(sql);
            list = convertList(rs);

            System.out.println("== End Guoan Query Results =======================\n\n");

        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                HikariGuoanInner.close(con, stat, rs);
            } catch (Exception e) {
                e.printStackTrace();
                return list;
            }
        }
        return list;
    }

    public static List<Map<String,Object>> executeGemini(String sql){

        Connection con = null;
        ResultSet rs = null;
        Statement stat=null;
        List<Map<String,Object>> list = null;
        PreparedStatement ps = null;
        try {


            con = HikariGeminiInner.getConnection();
            System.out.println("\n== Begin Query Results ======================");
            //ps = con.prepareStatement(sql);
            stat = con.createStatement();
            //rs = ps.executeQuery();
            rs = stat.executeQuery(sql);
            list = convertList(rs);

            System.out.println("== End Query Results =======================\n\n");

        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                HikariInner.close(con, stat, rs);
            } catch (Exception e) {
                e.printStackTrace();
                return list;
            }
        }
        return list;
    }

	private static List<Map<String,Object>> convertList(ResultSet rs) throws SQLException{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		ResultSetMetaData md = rs.getMetaData();//获取键名
		int columnCount = md.getColumnCount();//获取行的数量
		while (rs.next()) {
		Map rowData = new HashMap();//声明Map
		for (int i = 1; i <= columnCount; i++) {
		rowData.put(md.getColumnName(i), rs.getObject(i));//获取键名及值
		}
		list.add(rowData);
		}
		return list;
	}




    // set the impalad host
    private static final String IMPALAD_HOST = "10.16.31.192";

    // port 21050 is the default impalad JDBC port
    private static final String IMPALAD_JDBC_PORT = "21050";

    private static final String CONNECTION_URL = "jdbc:impala://" + IMPALAD_HOST + ':' + IMPALAD_JDBC_PORT + "/gemini;auth=noSasl";

    private static final String JDBC_DRIVER_NAME = "com.cloudera.impala.jdbc41.Driver";

    public static List<Map<String,Object>> test(String sql) {

        System.out.println("\n=============================================");
        System.out.println("Cloudera Impala JDBC Example");
        System.out.println("Using Connection URL: " + CONNECTION_URL);

        List<Map<String,Object>> list = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            Class.forName(JDBC_DRIVER_NAME);

            con = DriverManager.getConnection(CONNECTION_URL);

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            list = convertList(rs);
            System.out.println("\n== Begin Query Results ======================");

            
            System.out.println("== End Query Results =======================\n\n");

        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                con.close();
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return list;
    }
	
}
