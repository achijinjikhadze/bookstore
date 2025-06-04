package application;

import database.dbconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import tables.employee;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

//droplististvis
record AuthorItem(int id, String name) {
    public String toString() { return name; }
}

record CategoryItem(int id, String name) {
     public String toString() { return name; }
}


public class adminwindow {

    public void start(Stage stage, employee admin) {
    	
    	//tabebi
        TabPane tabpane = new TabPane();
        tabpane.setId("tabpane");
        Tab bookstab = new Tab("წიგნები", createBooksPane());
        bookstab.setId("bookstab");
        Tab authorstab= new Tab("ავტორები", createauthorspane());
        authorstab.setId("authrostab");
        Tab catetab= new Tab("ჟანრები", createcategspane());
        catetab.setId("catetab");
        Tab employeestab = new Tab("თანამშრომლები", createEmployeesPane());
        employeestab.setId("emptab");
        Tab orderstab = new Tab("გაყიდვები", createOrdersPane());
        orderstab.setId("orderstab");
        Tab ordersdettab = new Tab("გაყიდვის დეტალები", createOrdersdetPane());
        ordersdettab.setId("dettab");
        Tab reporttab = new Tab("ანგარიში", createReportPane());
        reporttab.setId("reptab");
        
        Button logoutbtn = new Button("გამოსვლა");
        logoutbtn.setId("lgout");
        
        tabpane.getTabs().addAll(bookstab, authorstab, catetab, employeestab, orderstab,ordersdettab, reporttab);
        tabpane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
       
        
        //adminis scena
        Label adm = new Label("ადმინი: ");
    	adm.setId("adm");

    	Label adm2 = new Label(admin.getFullName());
    	adm2.setId("adm2");
    	
    	
    	//box
        VBox root = new VBox(10,new HBox(5, adm, adm2), tabpane, logoutbtn );
        root.setPadding(new Insets(10));
        double screenwd = Screen.getPrimary().getBounds().getWidth();
        double screenhg = Screen.getPrimary().getBounds().getHeight();
        stage.setScene(new Scene(root, screenwd, screenhg-80));
        stage.setTitle("ადმინი");
        stage.show();
        root.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        root.setId("adminpn");
        //gamosvla
        logoutbtn.setOnAction(e -> {
            try {
                loginwindow loginWindow = new loginwindow();
                loginWindow.start(stage);  //loginis fanjrara
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
    
    
    //wignebis tabi



	private Pane createBooksPane() {
        TableView<ObservableList<String>> table = new TableView<>();
        table.setId("cxrili");
        //Button refreshbtn = new Button("ცხრილის ნახვა");
        //refreshbtn.setId("rfbt");
        Button addbtn = new Button("წიგნის დამატება");
       // refreshbtn.setId("bkad");
        Button editbtn = new Button("წიგნის რედაქტირება");
        //refreshbtn.setId("bked");
        Button deletebtn = new Button("წიგნის წაშლა");
        //refreshbtn.setId("bkdel");

        String sql = """
            select 
                books.bookid as "წიგნის ID", books.productcode as "წიგნის კოდი", books.title as "სათაური",  authors.firstname + ' ' + authors.lastname as "ავტორი" ,
                books.publicationdate as "დაწერის თარიღი", books.price as "გაყიდვის ფასი", books.getprice as "მიღების ფასი" ,
                categories.categoryname as "ჟანრი", books.quantity as "რაოდენობა", books.descript as "აღწერა"
	            from books
	            inner join authors on books.authorid = authors.authorid
	            inner join categories on books.categoryid = categories.categoryid
                    """;

        loadData(sql, table);
       // refreshbtn.setOnAction(e -> loadData(sql, table));
        //damateba
        addbtn.setOnAction(e -> addbookdialog(() -> loadData(sql, table)));

        //editi
        editbtn.setOnAction(e -> {
            TextInputDialog bookcode = new TextInputDialog();
            bookcode.setHeaderText("ჩაწერე კოდი:");
            bookcode.setTitle("წიგნის რედაქტირება");
            bookcode.showAndWait().ifPresent(code -> {
                int bookid = fetchbookidbycode(code);
                if (bookid == -1) {
                    new Alert(Alert.AlertType.WARNING, "ამ კოდით წიგნი არ მოიძებნა: " + code).showAndWait();
                    return;
                }
                editbookdialog(bookid, () -> loadData(sql, table));
            });
        });

        //washla
        deletebtn.setOnAction(e -> {
            TextInputDialog bookcode = new TextInputDialog();
            bookcode.setHeaderText("ჩაწერე კოდი:");
            bookcode.setTitle("წიგნის წაშლა");
            bookcode.showAndWait().ifPresent(code -> {
                int bookid = fetchbookidbycode(code);
                if (bookid == -1) {
                    new Alert(Alert.AlertType.WARNING, "ამ კოდით წიგნი არ მოიძებნა: " + code).showAndWait();
                    return;
                }

                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "დარწმუნებული ხარ რომ წაიშალოს?", ButtonType.YES, ButtonType.NO);
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        try (Connection conn = dbconnection.getConnection();
                             PreparedStatement stmt = conn.prepareStatement("delete from books where bookid = ?")) {
                            stmt.setInt(1, bookid);
                            stmt.executeUpdate();
                            loadData(sql, table);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, "შეცდომა: " + ex.getMessage()).showAndWait();
                        }
                    }
                });
            });
        });

        //wignis tabi
        HBox buttonbar = new HBox(10, addbtn, editbtn, deletebtn);
        VBox box = new VBox(10, buttonbar, table);
        box.setPadding(new Insets(10));
        return box;
    }

    
    //wignis id codis mixedvit
    private int fetchbookidbycode(String code) {
        try (Connection con = dbconnection.getConnection();
             PreparedStatement st = con.prepareStatement("select bookid from books where productcode = ?")) {
            st.setString(1, code);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("bookid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }

   // daamtebis dialoguri fanjara
    private void addbookdialog(Runnable onSuccess) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("წიგნის დამატება");
         
        //dropdown
        ComboBox<AuthorItem> authorbox = new ComboBox<>();
        ComboBox<CategoryItem> categorybox = new ComboBox<>();
        
        //text
        TextField codefield = new TextField();
        TextField titlefield = new TextField();
        TextField pubdatefield = new TextField("2024-01-01");
        TextField pricefield = new TextField("10.00");
        TextField getpricefield = new TextField("5.00");
        TextField quantityfield = new TextField("1");
        TextArea descriptfield = new TextArea();

        // dropdown asarchevi
        try (Connection con = dbconnection.getConnection()) {
            // avtrorebi
            PreparedStatement st = con.prepareStatement("select authorid, firstname, lastname from authors");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("authorid");
                String name = rs.getString("firstname") + " " + rs.getString("lastname");
                authorbox.getItems().add(new AuthorItem(id, name));
            }

            // categoriebi
            st = con.prepareStatement("select categoryid, categoryname from categories");
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("categoryid");
                String name = rs.getString("categoryname");
                categorybox.getItems().add(new CategoryItem(id, name));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        authorbox.setPromptText("აირჩიე ავტორი");
        categorybox.setPromptText("აირჩიე ჟანრი");

        // damatebis layout
        GridPane damateba = new GridPane();
        damateba.setVgap(5);
        damateba.setHgap(5);
        damateba.addRow(0, new Label("კოდი:"), codefield);
        damateba.addRow(1, new Label("სათაური:"), titlefield);
        damateba.addRow(2, new Label("ავტორი:"), authorbox);
        damateba.addRow(3, new Label("ჟანრი:"), categorybox);
        damateba.addRow(4, new Label("გამ. თარიღი:"), pubdatefield);
        damateba.addRow(5, new Label("გაყიდვის ფასი:"), pricefield);
        damateba.addRow(6, new Label("მიღების ფასი:"), getpricefield);
        damateba.addRow(7, new Label("რაოდენობა:"), quantityfield);
        damateba.addRow(8, new Label("აღწერა:"), descriptfield);

        dialog.getDialogPane().setContent(damateba);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        //bazashi damateba
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try (Connection con = dbconnection.getConnection()) {
                    PreparedStatement st = con.prepareStatement("""
                        insert into books (productcode, title, authorid, publicationdate, price, getprice, categoryid, quantity, descript)
                        values (?, ?, ?, ?, ?, ?, ?, ?, ?) """);

                    st.setString(1, codefield.getText());
                    st.setString(2, titlefield.getText());
                    st.setInt(3, authorbox.getValue().id());
                    st.setDate(4, Date.valueOf(pubdatefield.getText()));
                    st.setBigDecimal(5, new BigDecimal(pricefield.getText()));
                    st.setBigDecimal(6, new BigDecimal(getpricefield.getText()));
                    st.setInt(7, categorybox.getValue().id());
                    st.setInt(8, Integer.parseInt(quantityfield.getText()));
                    st.setString(9, descriptfield.getText());

                    st.executeUpdate();
                    onSuccess.run(); // refresh 
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "შეცდომა: " + ex.getMessage()).showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }


    //wignis redactireba
    private void editbookdialog(int bookId, Runnable onSuccess) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("წიგნის რედაქტირება");

        //dropdown
        ComboBox<AuthorItem> authorBox = new ComboBox<>();
        ComboBox<CategoryItem> categoryBox = new ComboBox<>();
        
        //text
        TextField codefield = new TextField();
        TextField titlefield = new TextField();
        TextField pubdatefield = new TextField();
        TextField pricefield = new TextField();
        TextField getpricefield = new TextField();
        TextField quantityfield = new TextField();
        TextArea descriptfield = new TextArea();

        // dropdowns 
        try (Connection con = dbconnection.getConnection()) {
        	//avtoirebi
            PreparedStatement st = con.prepareStatement("select authorid, firstname, lastname from authors");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("authorid");
                String name = rs.getString("firstname") + " " + rs.getString("lastname");
                authorBox.getItems().add(new AuthorItem(id, name));
            }

            //category
            st = con.prepareStatement("select categoryid, categoryname from categories");
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("categoryid");
                String name = rs.getString("categoryname");
                categoryBox.getItems().add(new CategoryItem(id, name));
            }

            // wignis informaciis chawera
            st = con.prepareStatement("""
                select productcode, title, authorid, publicationdate, price, getprice, categoryid, quantity, descript
                from books where bookid = ?   """);
            st.setInt(1, bookId);
            rs = st.executeQuery();
            if (rs.next()) {
                codefield.setText(rs.getString("productcode"));
                titlefield.setText(rs.getString("title"));

                int authorid = rs.getInt("authorid");
                for (AuthorItem item : authorBox.getItems()) {
                    if (item.id() == authorid) {
                        authorBox.setValue(item);
                        break;
                    }
                }

                pubdatefield.setText(rs.getDate("publicationdate").toString());
                pricefield.setText(rs.getBigDecimal("price").toString());
                getpricefield.setText(rs.getBigDecimal("getprice").toString());

                int categoryid = rs.getInt("categoryid");
                for (CategoryItem item : categoryBox.getItems()) {
                    if (item.id() == categoryid) {
                        categoryBox.setValue(item);
                        break;
                    }
                }

                quantityfield.setText(String.valueOf(rs.getInt("quantity")));
                descriptfield.setText(rs.getString("descript"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "შეცდომა: " + ex.getMessage()).showAndWait();
            return;
        }

        //ds
        authorBox.setPromptText("აირჩიე ავტორი");
        categoryBox.setPromptText("აირჩიე ჟანრი");

        //editis layout
        GridPane edit = new GridPane();
        edit.setVgap(5);
        edit.setHgap(5);
        edit.addRow(0, new Label("კოდი:"), codefield);
        edit.addRow(1, new Label("სათაური:"), titlefield);
        edit.addRow(2, new Label("ავტორი:"), authorBox);
        edit.addRow(3, new Label("ჟანრი:"), categoryBox);
        edit.addRow(4, new Label("გამ. თარიღი:"), pubdatefield);
        edit.addRow(5, new Label("გაყიდვის ფასი:"), pricefield);
        edit.addRow(6, new Label("მიღების ფასი:"), getpricefield);
        edit.addRow(7, new Label("რაოდენობა:"), quantityfield);
        edit.addRow(8, new Label("აღწერა:"), descriptfield);

        dialog.getDialogPane().setContent(edit);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        
        //bazashi ganaxleba
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try (Connection conn = dbconnection.getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement("""
                        update books set
                        productcode = ?, title = ?, authorid = ?, publicationdate = ?, price = ?, getprice = ?, categoryid = ?, quantity = ?, descript = ?
                        where bookid = ?   """);
                    stmt.setString(1, codefield.getText());
                    stmt.setString(2, titlefield.getText());
                    stmt.setInt(3, authorBox.getValue().id());
                    stmt.setDate(4, Date.valueOf(pubdatefield.getText()));
                    stmt.setBigDecimal(5, new BigDecimal(pricefield.getText()));
                    stmt.setBigDecimal(6, new BigDecimal(getpricefield.getText()));
                    stmt.setInt(7, categoryBox.getValue().id());
                    stmt.setInt(8, Integer.parseInt(quantityfield.getText()));
                    stmt.setString(9, descriptfield.getText());
                    stmt.setInt(10, bookId);

                    stmt.executeUpdate();
                    onSuccess.run();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "შეცდომა: " + ex.getMessage()).showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

	
	
	// avtoris tabi
	private Pane createauthorspane() {
	    TableView<ObservableList<String>> table = new TableView<>();
	    
	    table.setId("cxrili");
	   
	    //btns
	    //Button refreshbtn = new Button("ცხრილის ნახვა");
	    Button addbtn = new Button("ავტორის დამატება");
	    Button deletebtn = new Button("ავტორის წაშლა");
	    Button editbtn = new Button("ავტორის რედაქტირება");
	  

	   //edit act
	    editbtn.setOnAction(e -> {
	        TextInputDialog dialog = new TextInputDialog();
	        dialog.setHeaderText("ჩაწერე ID:");
	        dialog.setTitle("ავტორის რედაქტირება");

	        //tu id modzebna
	        dialog.showAndWait().ifPresent(idstr -> {
	            try {
	                int authorid = Integer.parseInt(idstr.trim());

	                TextInputDialog namedialog = new TextInputDialog();
	                namedialog.setHeaderText("ჩაწერე ავტორის სახელი:");
	                namedialog.setTitle("სახელი");
                      
	                //bazashi ganaxleba
	                namedialog.showAndWait().ifPresent(newname -> {
	                    try (Connection con = dbconnection.getConnection();
	                         PreparedStatement st = con.prepareStatement("update authors set authorname = ? where authorid = ?")) {
	                        st.setString(1, newname.trim());
	                        st.setInt(2, authorid);
	                        int rows = st.executeUpdate();
	                        if (rows > 0) {
	                            loadData("select * from authors", table);
	                            new Alert(Alert.AlertType.INFORMATION, "ავტორი განახლდა").showAndWait();
	                        } else {
	                            new Alert(Alert.AlertType.WARNING, "ავტორი არ მოიძებნა: " + authorid).showAndWait();
	                        }
	                    } catch (SQLException ex) {
	                        ex.printStackTrace();
	                        new Alert(Alert.AlertType.ERROR, "შეცდომა: " + ex.getMessage()).showAndWait();
	                    }
	                });

	            } catch (NumberFormatException ex) {
	                new Alert(Alert.AlertType.ERROR, "სხვა შეცდომა").showAndWait();
	            }
	        });
	    });
	    
	    
	    //washla
	    deletebtn.setOnAction(e -> {
	        TextInputDialog dialog = new TextInputDialog();
	        dialog.setHeaderText("ჩაწერე ID:");
	        dialog.setTitle("ავტორის წაშლა");
	        dialog.showAndWait().ifPresent(idStr -> {
	            try {
	                int id = Integer.parseInt(idStr);

	               
	                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "დარწმუნებული ხართ რომ წაიშალოს: " + id + "?",
                            ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            try (Connection con = dbconnection.getConnection();
                                 PreparedStatement st = con.prepareStatement("delete from authors where authorid = ?")) {
                                st.setInt(1, id);
                                int rows = st.executeUpdate();
                                if (rows > 0) {
                                    loadData("select * from authors", table);
                                    new Alert(Alert.AlertType.INFORMATION, "ავტორი წაშლილია.").showAndWait();
                                } else {
                                    new Alert(Alert.AlertType.WARNING, "ავტორი არ მოიძებნა: " + id).showAndWait();
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                new Alert(Alert.AlertType.ERROR, "შეცდომა: " + ex.getMessage()).showAndWait();
                            }
                        }
                    });
	                

	            } catch (NumberFormatException ex) {
	            	 new Alert(Alert.AlertType.ERROR, "სხვა შეცდომა.").showAndWait();
	            }
	        });
	    });

	    //scene
	    String sql=""" 
	    	select
	            authors.authorid as "ავტორის ID", 
		    	authors.firstname as "სახელი",
		    	authors.lastname as "გვარი",
		    	authors.bio as "ბიოგრაფია" ,
		    	authors.birthdate as "დაბადების თარიღი" 
		    	   from authors
	    		""";
	    	
	    loadData(sql, table);
	   // refreshbtn.setOnAction(e -> loadData(sql, table));
	    addbtn.setOnAction(e -> addauthordialog(() -> loadData("select * from authors", table)));

	    HBox buttonbar = new HBox(10, addbtn, deletebtn);
        VBox box = new VBox(10, buttonbar, table);
	    
	    box.setPadding(new Insets(10));
	    return box;
	}

	private void addauthordialog(Runnable onSuccess) {
	    Dialog<Void> dialog = new Dialog<>();
	    dialog.setTitle("ავტორის დამატება");

	    TextField firstnamefield = new TextField();
	    TextField lastnamefield = new TextField();
	    TextField biofield = new TextField();
	    DatePicker birthdatepicker = new DatePicker();

	    firstnamefield.setPromptText("სახელი");
	    lastnamefield.setPromptText("გვარი");
	    biofield.setPromptText("ბიოგრაფია");

	    VBox content = new VBox(10,
	        new Label("სახელი:"), firstnamefield,
	        new Label("გვარი:"), lastnamefield,
	        new Label("ბიოგრაფია:"), biofield,
	        new Label("დაბ. თარიღი:"), birthdatepicker
	    );
	    content.setPadding(new Insets(10));
	    dialog.getDialogPane().setContent(content);
	    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

	    dialog.setResultConverter(button -> {
	        if (button == ButtonType.OK) {
	            try (Connection conn = dbconnection.getConnection()) {
	                PreparedStatement stmt = conn.prepareStatement("insert into authors (firstname, lastname, bio, birthdate) values (?, ?, ?, ?)");
	                stmt.setString(1, firstnamefield.getText().trim());
	                stmt.setString(2, lastnamefield.getText().trim());
	                stmt.setString(3, biofield.getText().trim());
	                stmt.setDate(4, Date.valueOf(birthdatepicker.getValue()));
	                stmt.executeUpdate();
	                onSuccess.run();
	            } catch (SQLException ex) {
	            	 ex.printStackTrace();
	                    new Alert(Alert.AlertType.ERROR, "შეცდომა: "  + ex.getMessage());
	            }
	        }
	        return null;
	    });

	    dialog.showAndWait();
	}


	
    
    
    //janris tabi
    
	private Pane createcategspane() {
	    TableView<ObservableList<String>> table = new TableView<>();
	    table.setId("cxrili");
	    //Button refreshbtn = new Button("ცხრილის ნახვა");
	    Button addbtn = new Button("ჟანრის დამატება");

	    Button deletebtn = new Button("ჟანრის წაშლა");

	    //janris washla
	    deletebtn.setOnAction(e -> {
	        TextInputDialog catid = new TextInputDialog(); //categoryid it dzebna
	        catid.setHeaderText("ჩაწერე ჟანრი:");
	        catid.setTitle("ჟანრის წაშლა");

	        catid.showAndWait().ifPresent(name -> {
	            try {
	            	String categoryname = name.trim();//janris saxeli
	            	 int categoryid = fetchCategoryIdByName(categoryname);//saxelidan id-is migeba

	                // dasturi da washla
	                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, 
	                    "დაწრმუნებული ხარ რომ წაიშალოს: " + categoryname + "?", 
	                    ButtonType.YES, ButtonType.NO);
	                confirm.showAndWait().ifPresent(response -> {
	                    if (response == ButtonType.YES) {
	                        try (Connection con = dbconnection.getConnection();
	                             PreparedStatement st = con.prepareStatement("delete from categories where categoryid = ?")) {
	                            st.setInt(1, categoryid);
	                            int washlilebi = st.executeUpdate();

	                            if (washlilebi > 0) {
	                                loadData("select * from categories", table);
	                                new Alert(Alert.AlertType.INFORMATION, "წაიშალა.").showAndWait();
	                            } else {
	                                new Alert(Alert.AlertType.WARNING, "ჟანრის ვერ მოიძებნა: " + categoryid).showAndWait();
	                            }

	                        } catch (SQLException ex) {
	                            ex.printStackTrace();
	                            new Alert(Alert.AlertType.ERROR, "შეცდომა: " + ex.getMessage()).showAndWait();
	                        }
	                    }
	                });

	            } catch (NumberFormatException ex) {
	                new Alert(Alert.AlertType.ERROR, "სხვა შეცდომა").showAndWait();
	            }
	        });
	    });


	    //category tabis scena
	    
	    String sql=""" 
		    	select
		            categories.categoryid as "კატეგორიის ID",
		            categories.categoryname as "ჟანრი"
		             
			    	   from categories
		    		""";
	    loadData(sql, table);
	    //refreshbtn.setOnAction(e -> loadData(sql, table));
	    addbtn.setOnAction(e -> addcategorydialog(() -> loadData("select * from categories", table)));

	    HBox buttonbar = new HBox(10, addbtn, deletebtn);
        VBox box = new VBox(10, buttonbar, table);
        

	    box.setPadding(new Insets(10));
	    return box;
	}

	
	//saxelidan id
	private int fetchCategoryIdByName(String name) {
	    try (Connection conn = dbconnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement("select categoryid from categories where categoryname = ?")) {
	        stmt.setString(1, name);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt("categoryid");
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return -1; // ver ipova
	}

	
	//janris damatebis dialoguri fanjara
	private void addcategorydialog(Runnable onSuccess) {
	    Dialog<Void> dialog = new Dialog<>();
	    dialog.setTitle("ჟანრის დამატება");

	    TextField namefield = new TextField();
	    namefield.setPromptText("სახელი");

	    
	    VBox content = new VBox(10, new Label("ჟანრი:"), namefield);
	    content.setPadding(new Insets(10));
	    dialog.getDialogPane().setContent(content);
	    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

	    dialog.setResultConverter(button -> {
	        if (button == ButtonType.OK && !namefield.getText().isBlank()) {
	            try (Connection con = dbconnection.getConnection()) {
	                PreparedStatement st = con.prepareStatement("insert into categories (categoryname) values (?)");
	                st.setString(1, namefield.getText().trim());
	                st.executeUpdate();
	                onSuccess.run();
	            } catch (SQLException ex) {
	            	 ex.printStackTrace();
	                    new Alert(Alert.AlertType.ERROR, "შეცდომა: "  + ex.getMessage());
	                //showError("შეცდომა: " + ex.getMessage());
	            }
	        }
	        return null;
	    });

	    dialog.showAndWait();
	}

	

     //tanamshrolebis fanjara
    private Pane createEmployeesPane() {
        TableView<ObservableList<String>> table = new TableView<>();
        table.setId("cxrili");
       
        //btns
       // Button refreshBtn = new Button("ცხრილის ნახვა");
        Button addBtn = new Button("თანამშრომლის დამატება");
        Button deleteEmpBtn = new Button("თანამშრომლის წაშლა");
        Button editEmpBtn = new Button("თანამშრომლის რედაქტირება");

        //redact
         editEmpBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("ჩაწერე ID:");
            dialog.setTitle("თანამშრომლის რედაქტირება");
            dialog.showAndWait().ifPresent(idstr -> {
                try {
                    int empid = Integer.parseInt(idstr.trim());
                    editemployeedialog(empid, () -> loadData("select * from employees", table));
                } catch (NumberFormatException ex) {
                    new Alert(Alert.AlertType.ERROR, "შეცდომა").showAndWait();
                }
            });
        });

        //washla
        deleteEmpBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("ჩაწერე ID:");
            dialog.setTitle("თანამშრომლის წაშლა");

            dialog.showAndWait().ifPresent(idStr -> {
                try {
                    int empid = Integer.parseInt(idStr.trim());

                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "დარწმუნებული ხართ რომ წაიშალოს: " + empid + "?",
                            ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            try (Connection con = dbconnection.getConnection();
                                 PreparedStatement st = con.prepareStatement("delete from employees where empid = ?")) {
                                st.setInt(1, empid);
                                int rows = st.executeUpdate();
                                if (rows > 0) {
                                    loadData("select * from employees", table);
                                    new Alert(Alert.AlertType.INFORMATION, "თანამშრომელი წაიშალა.").showAndWait();
                                } else {
                                    new Alert(Alert.AlertType.WARNING, "თანამშრომელი არ მოიძებნა: " + empid).showAndWait();
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                new Alert(Alert.AlertType.ERROR, "შეცდომა: " + ex.getMessage()).showAndWait();
                            }
                        }
                    });

                } catch (NumberFormatException ex) {
                    new Alert(Alert.AlertType.ERROR, "სხვა შეცდომა.").showAndWait();
                }
            });
        });

       

        //scena
        
        String sql = """
        	 select
	    employees.empid as "თანამშრომლის ID",
	    employees.firstname as "სახელი",
	    employees.lastname as "გვარი",
	    employees.phone as "ტელეფონი",
	    case
	        when employees.isadmin = 1 then N'კი'
	        else N'არა'
	    end as "ადმინი",
	    employees.username AS "იუზერნეიმი",
	    employees.password AS "პაროლი",
	    format(
	        coalesce(
	            (select sum(order_details.quantity * (books.price - (books.price * order_details.discount / 100)))
	             from orders
	             inner join order_details on orders.orderid = order_details.orderid
	             inner join books on order_details.bookid = books.bookid
	             where orders.empid = employees.empid), 0), '0.00') AS "სულ გაყიდვები ₾"
        		   		from employees

        	""";

        loadData(sql, table);
       // refreshBtn.setOnAction(e -> loadData(sql, table));
        addBtn.setOnAction(e -> addemployeedialog(() -> loadData("select * from employees", table)));

        HBox buttonbar = new HBox(10, addBtn, editEmpBtn, deleteEmpBtn);
        VBox box = new VBox(10, buttonbar, table);
         box.setPadding(new Insets(10));
        return box;
    }

    //tamahsromlis damatebis dialoguri fanjara
    private void addemployeedialog(Runnable onSuccess) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("თანამშრომლის დამატება");

        //textfld
        TextField firstnamefield = new TextField();
        TextField lastnamefield = new TextField();
        TextField phonefield= new TextField();
        TextField usernamefield = new TextField();
        PasswordField passwordfield = new PasswordField();
        CheckBox isadmincheck = new CheckBox("ადმინი");

        firstnamefield.setPromptText("სახელი");
        lastnamefield.setPromptText("გვარი");
        phonefield.setPromptText("ტელეფონი");
        usernamefield.setPromptText("უიზერნეიმი");
        passwordfield.setPromptText("პაროლი");

        //bx
        VBox content = new VBox(10,
            new Label("სახელი:"), firstnamefield,
            new Label("გვარი:"), lastnamefield,
            new Label("ტელეფონი"), phonefield,
            new Label("იუზერნეიმი:"), usernamefield,
            new Label("პაროლი:"), passwordfield,
            isadmincheck
        );
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        //damateba bazashi
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try (Connection conn = dbconnection.getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement(
                        "insert into employees (firstname, lastname, phone, username, password, isadmin) values (?, ?, ?, ?, ?, ?)"
                    );
                    stmt.setString(1, firstnamefield.getText().trim());
                    stmt.setString(2, lastnamefield.getText().trim());
                    stmt.setString(3, usernamefield.getText().trim());
                    stmt.setString(4, passwordfield.getText().trim());
                    stmt.setBoolean(5, isadmincheck.isSelected());
                    stmt.executeUpdate();
                    onSuccess.run();
                } catch (SQLException ex) {
                	 ex.printStackTrace();
	                    new Alert(Alert.AlertType.ERROR, "შეცდომა: "  + ex.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    
    //tamashromis redact
    private void editemployeedialog(int employeeId, Runnable onSuccess) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Edit Employee");

        TextField firstnamefield = new TextField();
        TextField lastnamefield = new TextField();
        TextField phonefield = new TextField();
        TextField usernamefield = new TextField();
        PasswordField passwordfield = new PasswordField();
        CheckBox isadmincheck = new CheckBox("ადმინი");

        // tanamshromlis infos load
        try (Connection con = dbconnection.getConnection();
             PreparedStatement st = con.prepareStatement("select * from employees where empid = ?")) {

            st.setInt(1, employeeId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
            	 firstnamefield.setText(rs.getString("firstname"));
                 lastnamefield.setText(rs.getString("lastname"));
                 phonefield.setText(rs.getString("phone"));
                 usernamefield.setText(rs.getString("username"));
                 passwordfield.setText(rs.getString("password"));
                 isadmincheck.setSelected(rs.getBoolean("isadmin"));
            } else {
                new Alert(Alert.AlertType.WARNING, "თანამშრომელი ვერ მოიძებნა: " + employeeId).showAndWait();
                return;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "შეცდომა: " + ex.getMessage()).showAndWait();
            return;
        }

        firstnamefield.setPromptText("სახელი");
        lastnamefield.setPromptText("გვარი");
        phonefield.setPromptText("ტელეფონი");
        usernamefield.setPromptText("იუზერნეიმი");
        passwordfield.setPromptText("პაროლი");

        VBox content = new VBox(10,
            new Label("სახელი:"), firstnamefield,
            new Label("გვარი:"), lastnamefield,
            new Label("ტელეფონი:"), phonefield,
            new Label("იუზერნეიმი:"), usernamefield,
            new Label("პაროლი:"), passwordfield,
            isadmincheck
        );
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        
        //bazashi ganaxleba
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try (Connection con = dbconnection.getConnection();
                     PreparedStatement st = con.prepareStatement(
                        "update employees set firstname = ?, lastname = ?, phone = ?, username = ?, password = ?, isadmin = ? where empid = ?"
                     )) {
                    st.setString(1, firstnamefield.getText().trim());
                    st.setString(2, lastnamefield.getText().trim());
                    st.setString(3, phonefield.getText().trim());
                    st.setString(4, usernamefield.getText().trim());
                    st.setString(5, passwordfield.getText().trim());
                    st.setBoolean(6, isadmincheck.isSelected());
                    st.setInt(7, employeeId);
                    st.executeUpdate();

                    onSuccess.run();
                    new Alert(Alert.AlertType.INFORMATION, "თანამშრომელი განახლდა.").showAndWait();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "შეცდომა: " + ex.getMessage()).showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }


 
    // shekvetebis tabi
    private Pane createOrdersPane() {
        TableView<ObservableList<String>> table = new TableView<>();
        table.setId("cxrili");
        //Button refreshbtn = new Button("ცხრილის ნახვა");
        String sql = """
        	    select
        	        orders.orderid as "შეკვეთის ID",
        	        orders.empid "თანამშრომლის ID",
        	        employees.firstname + ' ' + employees.lastname as "თანამშრომელი",
        	        orders.orderdate as "შეკვეთის თარიღი",
        	         cast(round(sum(books.price * order_details.quantity * (1 - order_details.discount / 100.0)),  2) as decimal(10, 2)) as "შეკვეთის ფასი ₾"
        	    from orders
        	    inner join order_details ON orders.orderid = order_details.orderid
        	    inner join books ON order_details.bookid = books.bookid
        	     inner join employees ON orders.empid = employees.empid
        	   group by
        	        orders.orderid,
        	        orders.empid,
        	        employees.firstname,
        	        employees.lastname,
        	        orders.orderdate
        	    """;
       
        loadData(sql, table);
        //refreshbtn.setOnAction(e -> loadData(sql, table));

        VBox box = new VBox(10, table);
        box.setPadding(new Insets(10));
        return box;
    }
    
    //yidvis detalebis tabi
    private Pane createOrdersdetPane() {
        TableView<ObservableList<String>> table = new TableView<>();
        table.setId("cxrili");
       // Button refreshbtn = new Button("ცხრილის ნახვა");
        String sql = """
        	   select
		    order_details.orderdetailsid as "დეტალური შეკვეთის ID",
		    order_details.orderid as "შეკვეთის ID",
		    books.title as "წიგნი",
		    books.price as "წიგნის ფასი",
		    order_details.quantity as "რაოდენობა",
		     cast(order_details.discount as decimal(10, 0)) as "ფასდაკლება %",
		    cast( round(books.price * order_details.quantity * (1 - order_details.discount / 100.0), 2) as decimal(10, 2)) as "ჯამური ფასი ₾"
		from order_details
		inner join books on order_details.bookid = books.bookid

        	    """;

        loadData(sql, table);
        //refreshbtn.setOnAction(e -> loadData(sql, table)); 

        VBox box = new VBox(10, table);
        box.setPadding(new Insets(10));
        return box;
    }


    //balansii
    private Pane createReportPane() {
        DatePicker fromdate = new DatePicker();
        DatePicker todate = new DatePicker();
        Button checkbtn = new Button("შეამოწმე ბალანსი");
        Label resultlabel = new Label("სულ გაყიდვები: $0.00");
        resultlabel.setId("sq");

        checkbtn.setOnAction(e -> {
            LocalDate from = fromdate.getValue();
            LocalDate to = todate.getValue();

            if (from == null || to == null) {
                resultlabel.setText("აირჩიეთ თარიღი.");
                return;
            }

            /*
            if (from.isAfter(to)) {
                resultlabel.setText("საიდან თარიღი უნდა იყოს ადრე ვიდრე საქამდე.");
                return;
            }*/

            String sql = """
                 select
		    sum( (books.price * (1 - order_details.discount / 100)) * order_details.quantity) as total_sales,
		    sum(books.getprice * order_details.quantity) as total_cost,
		    sum(((books.price * (1 - order_details.discount / 100)) - books.getprice) * order_details.quantity) as profit
			from orders
			inner join order_details on orders.orderid = order_details.orderid
			inner join books on order_details.bookid = books.bookid
			where orders.orderdate >= ? 
			and orders.orderdate < ?

                    """; //coalesce

           
            
            LocalDate nextDay = to.plusDays(1);

            //mogeba
            try (Connection conn = dbconnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, Date.valueOf(from));
                stmt.setDate(2, Date.valueOf(nextDay)); 

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    BigDecimal totalSales = rs.getBigDecimal("total_sales");
                    BigDecimal totalCost = rs.getBigDecimal("total_cost");
                    BigDecimal profit = rs.getBigDecimal("profit");  

                    resultlabel.setText(String.format("""
                        ▶ სულ გაყიდვები: ₾%.2f
                        ▶ თვითღირებულება: ₾%.2f
                        ▶ მოგება: ₾%.2f
                    """, 
                        totalSales != null ? totalSales : BigDecimal.ZERO,
                        totalCost != null ? totalCost : BigDecimal.ZERO,
                        profit != null ? profit : BigDecimal.ZERO
                    ));
                }

            } catch (Exception ex) {
                resultlabel.setText("შეცდომა: " + ex.getMessage());
            }

        });

        
        //box
        Label sd=new Label("საიდან:");
        sd.setId("sq");
        Label sq=new Label("საქამდე:");
        sq.setId("sq");
        HBox inputBox = new HBox(10, sd, fromdate, sq, todate, checkbtn);
        VBox layout = new VBox(15, inputBox, resultlabel);
        layout.setPadding(new Insets(15));

        return layout;
    }


    //cxrilsi gamotana
    private void loadData(String sql, TableView<ObservableList<String>> table) {
        table.getItems().clear();
        table.getColumns().clear();

        try (Connection con = dbconnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            int colCount = rs.getMetaData().getColumnCount();

            for (int i = 1; i <= colCount; i++) {
                final int colIndex = i;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(rs.getMetaData().getColumnName(i));
                col.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get(colIndex - 1)));
                table.getColumns().add(col);
            }

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= colCount; i++) {
                    row.add(rs.getString(i));
                }
                table.getItems().add(row);
            }

        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, "შეცდომა: " + ex.getMessage()).showAndWait();
        }
    }
}
