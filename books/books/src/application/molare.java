package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import application.molare.cartitem;
import database.dbconnection;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import tables.employee;

public class molare {

    private final List<cartitem> cart = new ArrayList<>();

    static class cartitem {
        int bookId;
        String title;
        double price;
        int quantity;
        double discount; 

        public double getTotalPrice() {
            double discounti = 1 - (discount / 100.0);
            double discountprice = price * discounti;
            return discountprice * quantity;
        }

        public String toString() {
            return title + " | რაოდენობა: " + quantity + " | ფასი: ₾" + price + " | ფასდაკლება: %" + discount + " | სულ: ₾" + getTotalPrice();
        }
        
    }

    public void start(Stage stage, employee employee) {
    	
    	//btns
    	TextField bookcode = new TextField();
    	bookcode.setPromptText("კოდი");
    	bookcode.setId("bookcode");

    	TextField quantityf = new TextField("1");
    	quantityf.setId("quantityf");

    	TextField discountf = new TextField("0");
    	discountf.setId("discountf");

    	Button addbt = new Button("კალათში დამატება");
    	addbt.setId("addbt");

    	Button sellbt = new Button("გაყიდვა");
    	sellbt.setId("sellbt");
    	
    	Label totallb1= new Label("სულ:");
        totallb1.setId("namelb");
    	Label totallb2= new Label("₾0.00");
        totallb2.setId("totallb");

    	Button logoutbt = new Button("გამოსვლა");
    	logoutbt.setId("logoutbt");

    	ListView<cartitem> cartlist = new ListView<>();

    	Label mol = new Label("მოლარე: ");
    	mol.setId("mol");

    	Label mol2 = new Label(employee.getFullName());
    	mol2.setId("mol2");

    	Label wgc = new Label("წიგნის კოდი 🆔:");
    	wgc.setId("wgc");

    	Label namelb = new Label("სახელი: ");
    	namelb.setId("namelb");

    	Label pricelb = new Label("ფასი: ");
    	pricelb.setId("pricelb");

    	Label rdn = new Label("რაოდენობა 📦:");
    	rdn.setId("rdn");

    	Label disc = new Label("ფასდაკლება %:");
    	disc.setId("disc");

    	Label kal = new Label("კალათა:");
    	kal.setId("kal");

    	Label searchlb = new Label("ძიება წიგნის, ავტორის ან კატეგორიის მიხედვით:");
    	searchlb.setId("searchlb");

    	TextField searchbytt = new TextField();
    	searchbytt.setPromptText("წიგნის სახელი");

    	TextField searchbyaut = new TextField();
    	searchbyaut.setPromptText("ავტორის სახელი");

    	TextField searchbycat = new TextField();
    	searchbycat.setPromptText("კატეგორია");

    	
    	 
    	//wignis povna
    	ListView<String> searchresults = new ListView<>();

    	Button searchbtn = new Button("ძებნა");
    	searchbtn.setOnAction(ev -> {
    	    String titletext = searchbytt.getText().trim();
    	    String authortext = searchbyaut.getText().trim();
    	    String categorytext = searchbycat.getText().trim();

    	    StringBuilder query = new StringBuilder("""
    	        select books.productcode, books.title, books.quantity, books.price, 
    	               concat(authors.firstname, ' ', authors.lastname) as author_fullname,
    	               categories.categoryname AS category_name
    	        from books
    	        join authors on books.authorid = authors.authorid
    	        join categories on books.categoryid = categories.categoryid
    	       
    	    """);  // where 1=1

    	    List<String> params = new ArrayList<>();

    	    if (!titletext.isEmpty()) {
    	        query.append(" and books.title like ?");
    	        params.add("%" + titletext + "%");
    	    }

    	    if (!authortext.isEmpty()) {
    	        query.append(" and (authors.firstname like ? or authors.lastname like ? or concat(authors.firstname, ' ', authors.lastname) like ?)");
    	        params.add("%" + authortext + "%");
    	        params.add("%" + authortext + "%");
    	        params.add("%" + authortext + "%");
    	    }

    	    if (!categorytext.isEmpty()) {
    	        query.append(" and categories.categoryname like ?");
    	        params.add("%" + categorytext + "%");
    	    }

    	    try (Connection con = dbconnection.getConnection()) {
    	        PreparedStatement st = con.prepareStatement(query.toString());
    	        for (int i = 0; i < params.size(); i++) {
    	            st.setString(i + 1, params.get(i));
    	        }

    	        ResultSet rs = st.executeQuery();
    	        searchresults.getItems().clear();
    	        while (rs.next()) {
    	            String title = rs.getString("title");
    	            String author = rs.getString("author_fullname");
    	            String category = rs.getString("category_name");
    	            int quantity = rs.getInt("quantity");
    	            double price = rs.getDouble("price");
    	            String bookcod = rs.getString("productcode");

    	            String result = String.format(
    	                "📘 %s | 👤 %s | 📚 %s | 📦 %d ცალი | ₾%.2f | 🆔 %s",
    	                title, author, category, quantity, price, bookcod
    	            );
    	            searchresults.getItems().add(result);
    	        }

    	        if (searchresults.getItems().isEmpty()) {
    	            searchresults.getItems().add("შედეგი არ მოიძებნა");
    	        }

    	    } catch (SQLException ex) {
    	        new Alert(Alert.AlertType.ERROR, "შეცდომა: " + ex.getMessage()).showAndWait();
    	    }
    	});

    	/*VBox bookinput = new VBox(5,
    	    
    	    new HBox(10, wgc, bookcode),
    	    new HBox(10, rdn, quantityf),
    	    new HBox(10, disc, discountf),
    	    namelb,
    	    pricelb,
    	    addbt
    	   
    	);*/
    	
    	
    	VBox bookinput = new VBox(5,
    		   new HBox(10,
    			new VBox (5,wgc,  bookcode), 
    			new VBox (5, rdn, quantityf), 
    			new VBox (5, disc,  discountf)),
    		   new VBox(10,
    		    namelb, 
    		    pricelb),
    		    addbt
    		);

    	//bookinput.setPadding(new Insets(10));
    	bookinput.setId("bookinput");

    	VBox searchbox = new VBox(10, searchlb,new HBox(10, searchbytt, searchbyaut, searchbycat), searchbtn, searchresults);
    	searchbox.setPadding(new Insets(10));
    	//searchbox.setPrefHeight(300);
    	//VBox.setVgrow(searchresults, Priority.ALWAYS);
    	searchbox.setId("search-section");

    	VBox rightcenter = new VBox(20, bookinput, searchbox);
    	rightcenter.setPadding(new Insets(10));
    	//rightcenter.setPrefWidth(450);
    	//VBox.setVgrow(searchbox, Priority.ALWAYS);

    	VBox cartbox = new VBox(10, kal, cartlist, new HBox(20,sellbt, new HBox(5,totallb1, totallb2)));
    	cartbox.setPadding(new Insets(10));
    	//cartBox.setPrefWidth(400);
    	VBox.setVgrow(cartlist, Priority.ALWAYS);
    	cartbox.setId("cart-section");

    	HBox maincontent = new HBox(20, rightcenter, cartbox);
    	maincontent.setPadding(new Insets(10));
    	HBox.setHgrow(cartbox, Priority.ALWAYS);

    	VBox layout = new VBox(new HBox(5, mol, mol2), maincontent,   logoutbt);
    	layout.setPadding(new Insets(15));
    	layout.setId("molarelt");

        // bookcode search
        bookcode.setOnAction(e -> {
            String input = bookcode.getText().trim();
            if (!input.isEmpty()) {
               // int bookId = Integer.parseInt(input);
                try (Connection con = dbconnection.getConnection()) {
                    PreparedStatement st = con.prepareStatement("select bookid, title, price from books where productcode = ?");
                   st.setString(1, input);
                    ResultSet rs = st.executeQuery();

                    if (rs.next()) {
                    	int bookid = rs.getInt("bookid");
                        String title = rs.getString("title");
                        Label ttl=new Label(title);
                        ttl.setId("ttl");
                        double price = rs.getDouble("price");
                        Label prc=new Label(String.valueOf(price));
                        prc.setId("prc");

                        namelb.setText("სახელი: " + title);
                        pricelb.setText("ფასი: ₾" + price);

                        bookcode.setUserData(new Object[]{bookid, title, price});
                    } else {
                        namelb.setText("წიგნი არ მოიძებნა...");
                        pricelb.setText("");
                        bookcode.setUserData(null);
                    }

                } catch (SQLException ex) {
                    namelb.setText("შეცდომა: " + ex.getMessage());
                }
            }
        });

        
        // damateba
        addbt.setOnAction(e -> {
            Object[] data = (Object[]) bookcode.getUserData();
            if (data == null) return;

            try {
                int bookId = (int) data[0];
                String title = (String) data[1];
                double price = (double) data[2];
                int qty = Integer.parseInt(quantityf.getText().trim());
                double discount = Double.parseDouble(discountf.getText().trim());

                // raodenobis shemowmeba
                try (Connection conn = dbconnection.getConnection()) {
                    PreparedStatement checkStmt = conn.prepareStatement("select quantity from books where bookid = ?");
                    checkStmt.setInt(1, bookId);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        int available = rs.getInt("quantity");

                        // sul raodenoba kalatashi
                        int cartQuantity = cart.stream()
                            .filter(item -> item.bookId == bookId)
                            .mapToInt(item -> item.quantity)
                            .sum();

                        if (qty + cartQuantity > available) {
                            new Alert(Alert.AlertType.WARNING,
                                "არასაკმარისი რაოდენობა: " + title +
                                "\nმარაგში: " + available +
                                "\nკალათაში: " + cartQuantity +
                                "\nმოთხოვნილია: " + qty).showAndWait();
                            return;
                        }

                        cartitem item = new cartitem();
                        item.bookId = bookId;
                        item.title = title;
                        item.price = price;
                        item.quantity = qty;
                        item.discount = discount;

                        cart.add(item);
                        cartlist.getItems().add(item);

                        
                        updateTotalPrice(totallb2);
                        
                        // gasuftaveba
                        bookcode.clear(); namelb.setText("სახელი: "); pricelb.setText("ფასი: ");
                        quantityf.setText("1"); discountf.setText("0");
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Book not found in database.").showAndWait();
                    }

                } catch (SQLException ex) {
                    new Alert(Alert.AlertType.ERROR, "შეცდომა: " + ex.getMessage()).showAndWait();
                }

                

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "ჩაწერე რაოდენობა და ფასდაკლება.").showAndWait();
            }
        });

        // gayidva
        sellbt.setOnAction(e -> {
            if (cart.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "კალათა ცარიელია.").showAndWait();
                return;
            }

            try (Connection con = dbconnection.getConnection()) {

                con.setAutoCommit(false);

                // insert ordershi
                PreparedStatement orderStmt = con.prepareStatement(
                    "insert into orders (empid, orderdate) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
                orderStmt.setInt(1, employee.getId());
                orderStmt.setDate(2, Date.valueOf(LocalDate.now()));
                orderStmt.executeUpdate();

                ResultSet generatedKeys = orderStmt.getGeneratedKeys();
                if (!generatedKeys.next()) throw new SQLException("შეცდომა...");
                int orderId = generatedKeys.getInt(1);

                // insert orderdetailshi
                PreparedStatement detailStmt = con.prepareStatement(
                        "insert into order_details (orderid, bookid, quantity, discount) values (?, ?, ?, ?)");
                PreparedStatement updateStockStmt = con.prepareStatement(
                        "update books set quantity = quantity - ? where bookid = ?");

                for (cartitem item : cart) {
                	//inserti
                    detailStmt.setInt(1, orderId);
                    detailStmt.setInt(2, item.bookId);
                    detailStmt.setInt(3, item.quantity);
                    detailStmt.setDouble(4, item.discount);
                    detailStmt.addBatch();
                    
                    // Update
                    updateStockStmt.setInt(1, item.quantity);
                    updateStockStmt.setInt(2, item.bookId);
                    updateStockStmt.addBatch();
                }

                detailStmt.executeBatch();
                updateStockStmt.executeBatch();
                con.commit();
                
              
                
                new Alert(Alert.AlertType.INFORMATION, "წარმატებით გაიყიდა.").showAndWait();
                cart.clear();
                cartlist.getItems().clear();
                
                updateTotalPrice(totallb2);

            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, "შეცდომა0: " + ex.getMessage()).showAndWait();
            }
        });

        // gasvla
        logoutbt.setOnAction(e -> {
            try {
                loginwindow loginWindow = new loginwindow();
                loginWindow.start(stage);  // gamosvla loginze
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
       
        double screenwd = Screen.getPrimary().getBounds().getWidth();
        double screenhg = Screen.getPrimary().getBounds().getHeight();
        
        
        stage.setScene(new Scene(layout, screenwd, screenhg-80));
        stage.setTitle("მოლარის ფანჯარა");
        stage.show();
        layout.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

   

	double calculateTotal() {
        double total = 0.0;
        for (cartitem item : cart) {
            total += item.getTotalPrice();
        }
        return total;
    }

    void updateTotalPrice(Label totallb) {
        double total = calculateTotal();
        totallb.setText("₾" + String.format("%.2f", total));
    }
    
 


}

