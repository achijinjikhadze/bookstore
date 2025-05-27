package database;

import java.sql.*;


public class dbconnection {
	 private static final String URL = "jdbc:sqlserver://DESKTOP-JL615BG:1433;databaseName=bookstore;integratedSecurity=true;encrypt=false";
	    
	   
	    public static Connection getConnection() throws SQLException {
	        return DriverManager.getConnection(URL);
	    }
}
