package javabookstore.categories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javabookstore.DatabaseConnection;

public class categoryintfimp implements categoryintf {
       
	public void insertcategory(Category ctg) {
		 String sql = "insert into categories (categoryname) values (?)";
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement st = con.prepareStatement(sql)) {
	        	st.setString(1, ctg.getcategoryname());
	        	
	        	st.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
}
	
	
	public void updatecategory(Category ctg) {
		 String sql = "update categories set categoryname=? where categroyid=?";
		 try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement st = con.prepareStatement(sql)) {
			 st.setString(1, ctg.getcategoryname());
			 st.setInt(2,ctg.getcategoryid());
			 
			 st.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	
	public void deletecategory(int id) {
		 String sql = "delete from categories where categroyid=?";
		 try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement st = con.prepareStatement(sql)) {
	            st.setInt(1, id);
	            st.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	
	public void printAllcategory() {
		String sql = "select * from categories " ;
		
		  try (Connection con = DatabaseConnection.getConnection();
		             Statement st = con.createStatement();
		             ResultSet rs = st.executeQuery(sql)) {
			  System.out.println("categories");
	            System.out.println("------------------------------------------");
			  while (rs.next()) {
	                int id = rs.getInt("categoryid");
	                String category = rs.getString("categoryname");
	                System.out.println(id + " | " + category);
		  }
		  } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
		  
}
