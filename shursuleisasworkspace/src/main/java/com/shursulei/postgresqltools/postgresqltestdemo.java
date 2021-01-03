package com.shursulei.postgresqltools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * https://blog.csdn.net/u013456370/article/details/79668420
 * 
 * @author shursulei
 *
 */
public class postgresqltestdemo {
	/**
	 * 创建表
	 */
	public static void createTable() {
		Connection c = null;
		Statement stmt = null;
		try {
			c = JdbcUtils.getPostgresqlConnection();
			stmt = c.createStatement();
			String sql = "CREATE TABLE COMPANY02 " + "(ID INT PRIMARY KEY     NOT NULL,"
					+ " NAME           TEXT    NOT NULL, " + " AGE            INT     NOT NULL, "
					+ " ADDRESS        CHAR(50), " + " SALARY         REAL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("新表创建成功!");
	}

	public static void insertdata() {
		Connection c = null;
		Statement stmt = null;
		try {
			c = JdbcUtils.getPostgresqlConnection();
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO COMPANY02 (ID,NAME,AGE,ADDRESS,SALARY) "
					+ "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO COMPANY02 (ID,NAME,AGE,ADDRESS,SALARY) "
					+ "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO COMPANY02 (ID,NAME,AGE,ADDRESS,SALARY) "
					+ "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO COMPANY02 (ID,NAME,AGE,ADDRESS,SALARY) "
					+ "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			c.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void querydata() {
		Connection c = null;
		Statement stmt = null;
		try {
			c = JdbcUtils.getPostgresqlConnection();
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from company02");
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String address = rs.getString("address");
				float salary = rs.getFloat("salary");
				System.out.println(id + "," + name + "," + age + "," + address.trim() + "," + salary);
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("查询数据成功");
	}

	public static void updatedata() {
		Connection c = null;
		Statement stmt = null;
		try {
			c = JdbcUtils.getPostgresqlConnection();
			stmt = c.createStatement();
			c.setAutoCommit(false);
			String sql = "Delete from COMPANY02 where ID=4 ";
			stmt.executeUpdate(sql);
			c.commit();
			ResultSet rs = stmt.executeQuery("select * from company02 order by id");
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String address = rs.getString("address");
				float salary = rs.getFloat("salary");
				System.out.println(id + "," + name + "," + age + "," + address.trim() + "," + salary);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deletedata() {
		Connection c = null;
		Statement stmt = null;
		try {
			c = JdbcUtils.getPostgresqlConnection();
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "UPDATE COMPANY02 set SALARY = 250 where ID=1 ";
			stmt.executeUpdate(sql);
			c.commit();

			ResultSet rs = stmt.executeQuery("select * from company02 order by id");
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String address = rs.getString("address");
				float salary = rs.getFloat("salary");
				System.out.println(id + "," + name + "," + age + "," + address.trim() + "," + salary);
			}
			rs.close();
			stmt.close();
			c.close();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("删除数据成功！");
	}
	public static void querybytable(String table,String cloumn) {
		Connection c = null;
		Statement stmt = null;
		try {
			c = JdbcUtils.getPostgresqlConnection();
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "select "+cloumn+" from "+table+" ;";
			c.commit();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("name");
				System.out.println(name);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void querytest() {
		Connection c = null;
		Statement stmt = null;
		try {
			c = JdbcUtils.getPostgresqlConnection();
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "select name from COMPANY02;";
			c.commit();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("name");
				System.out.println(name);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	
	}
	public static void main(String[] args) {
//		createTable();
//		insertdata();
//		querydata();
//		updatedata();
//		deletedata();
		querybytable("COMPANY02","name");
//		querytest();
	}
}
