package com.cnpc.pms.utils;

import java.sql.*;

import com.cnpc.pms.base.util.PropertiesUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
  * Description:  Hikari连接池 
  * @author wuxinxin
  * @date 2018年9月20日
 */
public class HikariInner {
	//定义 Hikari 连接池配置对象
    private static  HikariConfig poolConfig = null;
    //定义 Hikari 连接池对象
    private static HikariDataSource dataSource = null;
    


    static{
        try {
            String impala_host = PropertiesUtil.getValue("impala_host");
            String impala_port = PropertiesUtil.getValue("impala_port");
            String impala_user =  PropertiesUtil.getValue("impala_user");
            String impala_password = PropertiesUtil.getValue("impala_password");
            String connectionUrl = "jdbc:impala://" + "10.16.21.74" + ':' + "21050" + "/daqweb;auth=noSasl";
            String driverName = "com.cloudera.impala.jdbc41.Driver";
        	 poolConfig = new HikariConfig();
        	 
        	 //基本配置
         	  poolConfig.setDriverClassName(driverName);
         	  poolConfig.setJdbcUrl(connectionUrl);
         	  //等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒 
         	  poolConfig.setConnectionTimeout(500000);
         	  //个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟
         	  poolConfig.setIdleTimeout(600000);
         	  //一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒，参考MySQL wait_timeout参数（show variables like '%timeout%';） 
         	  poolConfig.setMaxLifetime(1800000);
         	  //连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count) 
         	  poolConfig.setMaximumPoolSize(50);
         	  poolConfig.setMinimumIdle(10);
        	 
          	  dataSource = new HikariDataSource(poolConfig);
          	
          	  System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取与指定数据库的连接
    public static HikariDataSource getInstance(){
        return dataSource;
    }

    //从连接池返回一个连接
    public static Connection getConnection(){

        Logger logger = LoggerFactory.getLogger(HikariInner.class);
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            
        } catch (Exception e) {
            logger.info("Exception is {}",e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    //释放资源
    public static void realeaseResource(ResultSet rs,PreparedStatement ps,Connection conn){
        if(null != rs){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(null != ps){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //关闭数据库连接

    public static void close(Connection conn, Statement st, ResultSet rs) {

        if(rs != null) {

            try{

                //关闭存储查询结果的ResultSet对象
                rs.close();

            }catch (Exception e) {

                e.printStackTrace();

            }

            rs= null;

        }

        if(st != null) {

            try{

                //关闭负责执行SQL命令的Statement对象
                st.close();

            }catch (Exception e) {

                e.printStackTrace();

            }

        }



        if(conn != null) {

            try{

                //将Connection连接对象还给数据库连接池
                conn.close();

            }catch (Exception e) {

                e.printStackTrace();

            }

        }
    }
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            Connection connection = getConnection();
            Statement stat=null;
            stat = connection.createStatement();
            ResultSet result =stat.executeQuery(" select count(1) from bb_t_order");

            while(result.next()){
                System.out.println("order1表数量：----------------"+i+"------------"+result.getObject(1));
                //
            }
            close(connection,stat,result);
        }

	}
    
    
}
