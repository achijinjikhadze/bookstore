package application;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import tables.employee;
import database.login;


public class loginwindow extends Application {

	 public void start(Stage primaryStage) {
		 
	     
	        Label title = new Label("ლოგინი");
	        title.setId("logintitle");
	       // title.setStyle("-fx-font-size: 24;");

	        CheckBox ckb = new CheckBox("ადმინი");
	        ckb.setId("saxlb");
	        
	        TextField usernamet= new TextField();
	        usernamet.setPromptText("სახელი:");
	        usernamet.setId("logusr");
	        PasswordField passwordt = new PasswordField();
	        passwordt.setPromptText("პაროლი:");
	        passwordt.setId("logpas");
	        Label messaget= new Label();
	        messaget.setId("logmes");

	        Button loginbt = new Button("შესვლა");
	        loginbt.setId("loginbt");


	     // layout
	        VBox log = new VBox(15); 
	        log.setPadding(new Insets(20));
	        log.setAlignment(Pos.CENTER);
	        log.setId("log");

	       
	        
	        log.getChildren().addAll(title, ckb, usernamet, passwordt, loginbt, messaget);

	        // buttoni
	        loginbt.setOnAction(e -> {
	            String username = usernamet.getText();
	            String password = passwordt.getText();
	            boolean isChecked = ckb.isSelected();

	            employee employee = login.authenticate(username, password);

	            if (employee != null) {
	                if (isChecked && !employee.isAdmin()) {
	                    messaget.setText("მხოლოდ ადმინისტრაცია");
	                    return;
	                }

	                if (isChecked && employee.isAdmin()) {
	                    primaryStage.close();
	                    Stage newStage = new Stage();
	                    new adminwindow().start(newStage, employee);
	                } else if (!isChecked) {
	                    primaryStage.close();
	                    Stage newStage = new Stage();
	                    new molare().start(newStage, employee);
	                }
	            } else {
	                messaget.setText("მომხმარებლის სახელი ან პაროლი არასწორია");
	            }
	        });



	        // fanjris gaxsna
	        double screenwd = Screen.getPrimary().getBounds().getWidth();
	        double screenhg = Screen.getPrimary().getBounds().getHeight();
	        Scene scene = new Scene(log, screenwd, screenhg-80);
	        // Scene scene = new Scene(log, 700, 400);
	        
	      
	       
	        primaryStage.setTitle("შესვლა");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
	    
}
