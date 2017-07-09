package com.dcn.constants;

public interface ApplicationConstants {
	
	String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
	
	String DB_USER_NAME = "SYSTEM";
	
	String DB_PASSWORD = "diwahar";

	String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	
	String DCN_QUERY = "Select SNO,EMPNAME,DESIG from EMP";
	
	String QUERY = "SELECT * FROM EMP WHERE ROWID =\'";

}
