package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tables.employee;
import database.login;


public class loginwindow extends Application {

	 public void start(Stage primaryStage) {
		 
	     
	        Label title = new Label("ლოგინი");
	        title.setStyle("-fx-font-size: 24;");

	        TextField usernamet= new TextField();
	        PasswordField passwordt = new PasswordField();
	        Label messaget= new Label();

	        Button loginbt = new Button("შესვლა");

	        // layout
	        GridPane log = new GridPane();
	        log.setPadding(new Insets(20));
	        log.setVgap(15);
	        log.setHgap(10);
	        log.setAlignment(Pos.CENTER);

	        log.add(title, 0, 0, 2, 1);
	        log.add(new Label("სახელი:"), 0, 1);
	        log.add(usernamet, 1, 1);
	        log.add(new Label("პაროლი:"), 0, 2);
	        log.add(passwordt, 1, 2);
	        log.add(loginbt, 1, 3);
	        log.add(messaget, 0, 4, 2, 1);

	        
	        // buttoni
	        loginbt.setOnAction(e -> {
	            String username = usernamet.getText();
	            String password = passwordt.getText();

	            employee employee = login.authenticate(username, password);
	            if (employee != null) {
	                primaryStage.close(); 

	                Stage newstage = new Stage();

	                if (employee.isAdmin()) {
	                    // TODO: open Admin dashboard
	                } else {
	                    new molare().start(newstage, employee);
	                }
	            }
	            else {
	                messaget.setText("მომხმარებლის სახელი ან პაროლი არასწორია");
	            }
	        });

	        // fanjris gaxsna
	        Scene scene = new Scene(log, 400, 300);
	        primaryStage.setTitle("შესვლა");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
	    
}
