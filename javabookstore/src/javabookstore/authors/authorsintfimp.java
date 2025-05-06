package javabookstore.authors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javabookstore.DatabaseConnection;
import javabookstore.books.Book;
import java.util.Date;


public class authorsintfimp implements authorintf {
	
	
	 public void insertAuthor(Author author) {
	        String sql = "INSERT INTO authors (firstname, lastname, bio, birthdate) VALUES (?, ?, ?, ?)";
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement st = con.prepareStatement(sql)) {
	            st.setString(1, author.getFirstname());
	            st.setString(2, author.getLastname());
	            st.setString(3, author.getBio());
	            st.setDate(4, new java.sql.Date(author.getBirthdate().getTime()));
	            st.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	  
	    
	
	    
	    public void updateAuthor(Author author) {
	        String sql = "UPDATE authors SET firstname = ?, lastname = ?, bio = ?, birthdate = ? WHERE authorid = ?";
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement st = con.prepareStatement(sql)) {
	            st.setString(1, author.getFirstname());
	            st.setString(2, author.getLastname());
	            st.setString(3, author.getBio());
	            st.setDate(4, new java.sql.Date(author.getBirthdate().getTime()));
	            st.setInt(5, author.getAuthorid());
	            st.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	
	    public void deleteAuthor(int id) {
	        String sql = "DELETE FROM authors WHERE authorid = ?";
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement st = con.prepareStatement(sql)) {
	            st.setInt(1, id);
	            st.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public void printAllAuthors() {
	        String sql = "SELECT * FROM authors";
	        try (Connection con = DatabaseConnection.getConnection();
	             Statement st = con.createStatement();
	             ResultSet rs = st.executeQuery(sql)) {
	            System.out.println("ID | Name | Birthdate | Bio");
	            System.out.println("-----------------------------");
	            while (rs.next()) {
	                System.out.println(
	                    rs.getInt("authorid") + " | " +
	                    rs.getString("firstname") + " " +
	                    rs.getString("lastname") + " | " +
	                    rs.getDate("birthdate") + " | " +
	                    rs.getString("bio")
	                );
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    
}
