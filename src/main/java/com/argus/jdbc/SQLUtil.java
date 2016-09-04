package com.argus.jdbc;

import java.sql.*;

import org.apache.commons.lang.StringUtils;

/**
 * User: xingding Date: Oct 31, 2011 Time: 6:40:09 PM To change this template
 * use File | Settings | File Templates.
 */
public class SQLUtil {

	public static Connection getConnection(String url, String userName,
			String password, String driverName) {
		Connection conn = null;
		try {
			Class.forName(driverName).newInstance();
			try {
				conn = DriverManager.getConnection(url, userName, password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;

	}

	public static Connection getOracleConnection() {
		return getConnection("jdbc:oracle:thin:@ip:1521:instanceid",
				"username", "password", "oracle.jdbc.driver.OracleDriver");
	}

	public static Connection getMySQLConn() {
		
		return getConnection("jdbc:mysql://ip:3306/instanceid", "root",
				"mysql", "com.mysql.jdbc.Driver");
	}

	public static Connection getHsqlConnection() {
		return getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa", "",
				"org.hsqldb.jdbcDriver");
	}

	public static void release(Connection conn, Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void main(String[] args){
		Connection conn = getMySQLConn();
		System.out.println(conn);
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//initial service code
		String selectSql = "select * from service order by serviceUrl";
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(selectSql);
			int i = 0;
			while(rs.next()){
				i++;
				pstmt = conn.prepareStatement("update service set serviceCode = ? where serviceId = ?");
				String serviceId = rs.getString(1);
				String serviceCode = StringUtils.leftPad(String.valueOf(i), 8, '0');
				pstmt.setString(1, serviceCode);
				pstmt.setString(2, serviceId);
				pstmt.execute();
			}
			System.out.println("update done.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			SQLUtil.release(conn, stmt, rs);
		}
		
		
		//update service url
		/*String selectSql = "select serviceId, serviceCode, serviceName, serviceUrl from service where serviceUrl like '%APP0%'";
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(selectSql);
			int i = 0;
			while(rs.next()){
				i++;
				pstmt = conn.prepareStatement("update service set serviceUrl = ? where serviceId = ?");
				String origUrl = rs.getString(4);
				int index = origUrl.indexOf("servicecode=");
				String serviceCode = rs.getString(2);
				String url = origUrl.substring(0, index) + "servicecode=" + serviceCode;
				System.out.println(url);
				String serviceId = rs.getString(1);
				pstmt.setString(1, url);
				pstmt.setString(2, serviceId);
				pstmt.execute();
			}
			System.out.println("update done.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			SQLUtil.release(conn, stmt, rs);
		}
		*/
		
		
	}

}
