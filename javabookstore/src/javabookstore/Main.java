package javabookstore;

import javabookstore.books.*;

import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        bookintf dao = new bookintfimp();

        // insert
      Book newBook = new Book("axali wigni", 1, new Date(), 24.99, 19.99, 2, 5, "textii");
        dao.insertBook(newBook);
        System.out.println("daemata wigni");

 
       //updatr
            dao.updateBook(newBook);
            System.out.println("Book updated!");
        

        
        // delete
        dao.deleteBook(15);
        System.out.println("wigni waishala");
        
        dao.printAllBooks();
        
    }
}
