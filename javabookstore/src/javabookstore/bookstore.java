package javabookstore;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javabookstore.DatabaseConnection;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class bookstore extends Application{
	
	public static void main(String[] args) {
        launch(args);
    }
	
	 @Override
	    public void start(Stage primaryStage) {
	        // Set up the basic layout
	        VBox vbox = new VBox();
	        vbox.setSpacing(10);

	        // Create UI components
	        Label titleLabel = new Label("Database Application");

	        // ComboBox to select the table
	        ComboBox<String> tableSelector = new ComboBox<>();
	        tableSelector.getItems().addAll("Authors", "Books", "Categories", "Customers", "Orders");
	        tableSelector.setValue("Authors"); // Default to Authors

	        // Button to load data
	        Button loadButton = new Button("Load Data");
	        TextArea dataArea = new TextArea();
	        dataArea.setEditable(false);

	        // Set up TableView for displaying data
	        TableView<String[]> tableView = new TableView<>();

	        /*
	        // Add input fields for adding data
	        Label addDataLabel = new Label("Add Data:");
	        TextField nameField = new TextField();
	        nameField.setPromptText("Name");
	        TextField descriptionField = new TextField();
	        descriptionField.setPromptText("Description");
	        Button addDataButton = new Button("Add Data");
	        */

	        // Button action to load data based on table selected
	        loadButton.setOnAction(e -> loadData(tableSelector.getValue(), tableView));

	        // Button action to add data to the selected table
	        //addDataButton.setOnAction(e -> addData(tableSelector.getValue(), nameField.getText(), descriptionField.getText()));

	        // Add components to layout
	        vbox.getChildren().addAll(titleLabel, tableSelector, loadButton, tableView);

	        // Set the stage (window) properties
	        Scene scene = new Scene(vbox, 1200, 600);
	        primaryStage.setTitle("Database Application");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    }

	    private void loadData(String tableName, TableView<String[]> tableView) {
	        // Clear existing data in TableView
	        tableView.getItems().clear();
	        tableView.getColumns().clear();

	        String query = "";
	        switch (tableName) {
	            case "Authors":
	                query = "SELECT * FROM authors";
	                break;
	            case "Books":
	                query = "SELECT * FROM books";
	                break;
	            case "Categories":
	                query = "SELECT * FROM categories";
	                break;
	            case "Customers":
	                query = "SELECT * FROM customers";
	                break;
	            case "Orders":
	                query = "SELECT * FROM orders";
	                break;
	        }

	        try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {

	            // Get columns from the result set
	            ResultSetMetaData rsMeta = rs.getMetaData();
	            int columnCount = rsMeta.getColumnCount();

	            // Create TableView columns based on result set columns
	            for (int i = 1; i <= columnCount; i++) {
	                TableColumn<String[], String> column = new TableColumn<>(rsMeta.getColumnLabel(i));
	                int colIndex = i - 1;
	                column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[colIndex]));
	                tableView.getColumns().add(column);
	            }

	            // Add rows to TableView
	            while (rs.next()) {
	                String[] row = new String[columnCount];
	                for (int i = 1; i <= columnCount; i++) {
	                    row[i - 1] = rs.getString(i);
	                }
	                tableView.getItems().add(row);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	  /*  private void addData(String tableName, String name, String description) {
	        // Prepare the SQL query based on the table selected
	        String query = "";
	        switch (tableName) {
	            case "Authors":
	                query = "INSERT INTO authors (firstname, lastname, bio) VALUES (?, ?, ?)";
	                break;
	            case "Books":
	                query = "INSERT INTO books (title, authorid, publisher, publicationdate, price, categoryid, quantity, descript) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	                break;
	            case "Categories":
	                query = "INSERT INTO categories (categoryname) VALUES (?)";
	                break;
	            case "Customers":
	                query = "INSERT INTO customers (firstname, lastname, email, phone, address, registrationdate) VALUES (?, ?, ?, ?, ?, ?)";
	                break;
	            case "Orders":
	                query = "INSERT INTO orders (customerid, orderdate, orderaddress, orderprice) VALUES (?, ?, ?, ?)";
	                break;
	        }

	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)) {

	            // Set parameters based on the table
	            if (tableName.equals("Authors")) {
	                stmt.setString(1, name);  // First Name
	                stmt.setString(2, description);  // Last Name
	                stmt.setString(3, description);  // Bio
	            } else if (tableName.equals("Books")) {
	                // Example for Books table, assuming appropriate data is available
	                stmt.setString(1, name);  // Title
	                stmt.setInt(2, 1);  // Author ID (dummy value)
	                stmt.setString(3, description);  // Publisher
	                stmt.setDate(4, Date.valueOf("2025-01-01"));  // Publication Date (example)
	                stmt.setBigDecimal(5, new java.math.BigDecimal(9.99));  // Price (example)
	                stmt.setInt(6, 1);  // Category ID (dummy value)
	                stmt.setInt(7, 10);  // Quantity (example)
	                stmt.setString(8, description);  // Description
	            } else if (tableName.equals("Categories")) {
	                stmt.setString(1, name);  // Category Name
	            } else if (tableName.equals("Customers")) {
	                // Example for Customers table
	                stmt.setString(1, name);  // First Name
	                stmt.setString(2, description);  // Last Name
	                stmt.setString(3, description + "@example.com");  // Email
	                stmt.setString(4, "1234567890");  // Phone
	                stmt.setString(5, description + " Address");  // Address
	                stmt.setDate(6, Date.valueOf("2025-01-01"));  // Registration Date
	            } else if (tableName.equals("Orders")) {
	                // Example for Orders table
	                stmt.setInt(1, 1);  // Customer ID (dummy value)
	                stmt.setDate(2, Date.valueOf("2025-01-01"));  // Order Date (example)
	                stmt.setString(3, description);  // Order Address
	                stmt.setBigDecimal(4, new java.math.BigDecimal(100));  // Order Price (example)
	            }

	            // Execute insert query
	            stmt.executeUpdate();

	            // After inserting, refresh the data in the TableView
	            System.out.println("Data added successfully to " + tableName);

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }*/
}
