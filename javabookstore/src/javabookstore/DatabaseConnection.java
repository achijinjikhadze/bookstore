package javabookstore;

import java.sql.*;


public class DatabaseConnection {

    private static final String URL = "jdbc:sqlserver://DESKTOP-JL615BG:1433;databaseName=sqlbookstore;integratedSecurity=true;encrypt=false";
    
   
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

   
   
}

