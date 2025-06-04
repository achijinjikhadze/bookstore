package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import tables.employee;


public class login {

	 public static employee authenticate(String username, String password) {
	        String sql = "select * from employees where username = ? and password = ?";

	        try (Connection connection = dbconnection.getConnection();
	             PreparedStatement statement = connection.prepareStatement(sql)) {

	            statement.setString(1, username);
	            statement.setString(2, password);

	            ResultSet resultSet = statement.executeQuery();

	            if (resultSet.next()) {
	                return new employee(
	                    resultSet.getInt("empid"),
	                    resultSet.getString("firstname"),
	                    resultSet.getString("lastname"),
	                    resultSet.getBoolean("isadmin"),
	                    resultSet.getString("username"),
	                    resultSet.getString("password")
	                );
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	 
}
