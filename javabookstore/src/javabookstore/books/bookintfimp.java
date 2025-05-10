package javabookstore.books;

import javabookstore.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class bookintfimp implements bookintf {

    public void insertBook(Book book) {
        String sql = "insert into books (title, authorid, publicationdate, price, getprice, categoryid, quantity, descript) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, book.getTitle());
            st.setInt(2, book.getAuthorId());
            st.setDate(3, new java.sql.Date(book.getPublicationDate().getTime()));
            st.setDouble(4, book.getPrice());
            st.setDouble(5, book.getGetPrice());
            st.setInt(6, book.getCategoryId());
            st.setInt(7, book.getQuantity());
            st.setString(8, book.getDescription());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   

    
    public void updateBook(Book book) {
        String sql = "update books set title=?, authorid=?, publicationdate=?, price=?, getprice=?, categoryid=?, quantity=?, descript=? where bookid=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, book.getTitle());
            st.setInt(2, book.getAuthorId());
            st.setDate(3, new java.sql.Date(book.getPublicationDate().getTime()));
            st.setDouble(4, book.getPrice());
            st.setDouble(5, book.getGetPrice());
            st.setInt(6, book.getCategoryId());
            st.setInt(7, book.getQuantity());
            st.setString(8, book.getDescription());
            st.setInt(9, book.getBookId());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  
    public void deleteBook(int id) {
        String sql = "delete from books where bookid = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void printAllBooks() {
        String sql = "select b.bookid, b.title, a.firstname + ' ' + a.lastname AS author, c.categoryname, b.price " +
                     "FROM books b " +
                     "JOIN authors a ON b.authorid = a.authorid " +
                     "JOIN categories c ON b.categoryid = c.categoryid";

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

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
