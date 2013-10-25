package com.inncrewin.waza.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private Connection conn = null;

	public static final String USER_NAME = "wazaroot";

	public static final String PASSWORD = "wazaroot";

	public static final String DRIVER = "com.mysql.jdbc.Driver";

	public static final String URL = "jdbc:mysql://waza.cp8wd3ddgsq4.us-west-2.rds.amazonaws.com:3306/";

	public static final String DB_NAME = "waza";

	public Connection getDBConnection() {

		try {
			Class.forName(DRIVER);
			String url = URL + DB_NAME;
			conn = (Connection) DriverManager.getConnection(url, USER_NAME,
					PASSWORD);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
