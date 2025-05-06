package javabookstore.books;

import javabookstore.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class bookintfimp implements bookintf {

    public void insertBook(Book book) {
        String sql = "INSERT INTO books (title, authorid, publicationdate, price, getprice, categoryid, quantity, descript) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorId());
            stmt.setDate(3, new java.sql.Date(book.getPublicationDate().getTime()));
            stmt.setDouble(4, book.getPrice());
            stmt.setDouble(5, book.getGetPrice());
            stmt.setInt(6, book.getCategoryId());
            stmt.setInt(7, book.getQuantity());
            stmt.setString(8, book.getDescription());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   

    
    public void updateBook(Book book) {
        String sql = "UPDATE books SET title=?, authorid=?, publicationdate=?, price=?, getprice=?, categoryid=?, quantity=?, descript=? WHERE bookid=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorId());
            stmt.setDate(3, new java.sql.Date(book.getPublicationDate().getTime()));
            stmt.setDouble(4, book.getPrice());
            stmt.setDouble(5, book.getGetPrice());
            stmt.setInt(6, book.getCategoryId());
            stmt.setInt(7, book.getQuantity());
            stmt.setString(8, book.getDescription());
            stmt.setInt(9, book.getBookId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  
    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE bookid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void printAllBooks() {
        String sql = "SELECT b.bookid, b.title, a.firstname + ' ' + a.lastname AS author, c.categoryname, b.price " +
                     "FROM books b " +
                     "JOIN authors a ON b.authorid = a.authorid " +
                     "JOIN categories c ON b.categoryid = c.categoryid";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("ID | Title | Author | Category | Price");
            System.out.println("------------------------------------------");
            while (rs.next()) {
                int id = rs.getInt("bookid");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String category = rs.getString("categoryname");
                double price = rs.getDouble("price");

                System.out.println(id + " | " + title + " | " + author + " | " + category + " | $" + price);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}
