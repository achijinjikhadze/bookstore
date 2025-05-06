package javabookstore;

import javabookstore.books.*;
import javabookstore.authors.*;

import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        bookintf booki = new bookintfimp();
        authorintf at = new authorsintfimp();

        // insert
      Book bk1 = new Book("axali wigni", 1, new Date(), 24.99, 19.99, 2, 5, "textii");
      Author avt1 = new Author("saxeli", "gvari", "biograpia", new Date() );
      
      
      at.insertAuthor(avt1);
      at.deleteAuthor(20);
      at.updateAuthor(avt1);
      at.printAllAuthors();
      
    		  
        booki.insertBook(bk1);
        //System.out.println("daemata wigni");
          booki.updateBook(bk1);
            //System.out.println("Book updated!");
        booki.deleteBook(15);
       // System.out.println("wigni waishala");
        booki.printAllBooks();
        
        
        
    }
}
