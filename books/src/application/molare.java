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
import javafx.scene.layout.VBox;
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
        TextField bookcode = new TextField();
        bookcode.setPromptText("ჩაწერე წიგნის ID");

        Label namelb = new Label("სახელი: ");
        Label pricelb = new Label("ფასი: ");

        TextField quantityf = new TextField("1");
        TextField discountf = new TextField("0.00");

        Button addbt = new Button("კალათში დამატება");
        Button sellbt = new Button("გაყიდვა");
        Button logoutbt = new Button("გამოსვლა");

        ListView<cartitem> cartlist = new ListView<>();

        VBox layout = new VBox(10,
            new Label("მოლარე: " + employee.getFullName()),
            new Label("წიგნის კოდი:"), bookcode, namelb, pricelb,
            new Label("რაოდენობა:"), quantityf,
            new Label("ფასდაკლება:"), discountf, addbt,
            new Label("კალათა:"),
            cartlist,
            sellbt,
            logoutbt
        );
        layout.setPadding(new Insets(15));

        // bookcode search
        bookcode.setOnAction(e -> {
            String input = bookcode.getText().trim();
            if (!input.isEmpty()) {
                int bookId = Integer.parseInt(input);
                try (Connection con = dbconnection.getConnection()) {
                    PreparedStatement st = con.prepareStatement("select title, price from books where bookid = ?");
                    st.setInt(1, bookId);
                    ResultSet rs = st.executeQuery();

                    if (rs.next()) {
                        String title = rs.getString("title");
                        double price = rs.getDouble("price");

                        namelb.setText("სახელი: " + title);
                        pricelb.setText("ფასი: ₾" + price);

                        bookcode.setUserData(new Object[]{bookId, title, price});
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

                cartitem item = new cartitem();
                item.bookId = bookId;
                item.title = title;
                item.price = price;
                item.quantity = qty;
                item.discount = discount;

                cart.add(item);
                cartlist.getItems().add(item);

                // gasuftaveba
                bookcode.clear(); namelb.setText("სახელი: "); pricelb.setText("ფასი: ");
                quantityf.setText("1"); discountf.setText("0.00");

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

                for (cartitem item : cart) {
                    detailStmt.setInt(1, orderId);
                    detailStmt.setInt(2, item.bookId);
                    detailStmt.setInt(3, item.quantity);
                    detailStmt.setDouble(4, item.discount);
                    detailStmt.addBatch();
                }

                detailStmt.executeBatch();
                con.commit();

                new Alert(Alert.AlertType.INFORMATION, "წარმატებით გაიყიდა.").showAndWait();
                cart.clear();
                cartlist.getItems().clear();

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

        stage.setScene(new Scene(layout, 450, 600));
        stage.setTitle("მოლარის ფანჯარა");
        stage.show();
    }

}
