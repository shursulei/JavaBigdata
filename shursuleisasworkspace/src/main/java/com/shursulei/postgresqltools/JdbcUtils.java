package com.shursulei.postgresqltools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**
 * java使用jdbc的方式连接postgresql
 * @author shursulei
 *
 */
public class JdbcUtils {
    private static String posgresql_driver;
    private static String posgresql_url;
    private static String posgresql_user;
    private static String posgresql_password;
    static {
    	posgresql_driver = ResourceBundle.getBundle("db").getString("posgresql_driver");
    	posgresql_url = ResourceBundle.getBundle("db").getString("posgresql_url");
    	posgresql_user = ResourceBundle.getBundle("db").getString("posgresql_user");
    	posgresql_password = ResourceBundle.getBundle("db").getString("posgresql_password");
    }
    // 1、Postgresql
    public static Connection getPostgresqlConnection() throws ClassNotFoundException, SQLException {
        // 加载数据库驱动
        Class.forName(posgresql_driver);
//        System.out.println("测试加载数据库成功");
        Connection con = DriverManager.getConnection(posgresql_url, posgresql_user, posgresql_password);
//        System.out.println("测试数据库链接成功");
        return con;
    }
    public static void main(String[] args) {
        try {
            JdbcUtils.getPostgresqlConnection();
            System.out.println("汇聚数据区连接成功.....");
            System.out.println("=======================================");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
