/**
 * 
 */
/**
 * 
 */
module javabookstore {
	
	  requires javafx.controls;
	    requires javafx.fxml;
	    requires java.sql;
		requires javafx.base;  

	    
	    opens  javabookstore to javafx.fxml;
	    exports  javabookstore;
}