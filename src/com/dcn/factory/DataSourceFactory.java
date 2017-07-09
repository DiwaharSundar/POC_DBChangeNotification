package com.dcn.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import static com.dcn.constants.ApplicationConstants.DB_URL;
import static com.dcn.constants.ApplicationConstants.DB_USER_NAME;
import static com.dcn.constants.ApplicationConstants.DB_PASSWORD;
import static com.dcn.constants.ApplicationConstants.ORACLE_DRIVER;

import javax.resource.spi.ConnectionManager;

import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleDriver;

public class DataSourceFactory {

	
public static OracleConnection connect() throws SQLException
	{
		OracleDriver dr = new OracleDriver();
		Properties prop = new Properties();
		prop.setProperty("user",DB_USER_NAME);
		prop.setProperty("password",DB_PASSWORD);
		return (OracleConnection)dr.connect(DB_URL,prop);
	}



	public static Connection getSqlConnection() throws ClassNotFoundException, SQLException{
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		Connection sqlConn = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
		
		if(sqlConn != null)
			System.out.println("Sql Connection Established!!!!");
			
		return sqlConn;
		
	}



}
