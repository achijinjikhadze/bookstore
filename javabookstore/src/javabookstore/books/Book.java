package javabookstore.books;



import java.util.Date;

public class Book {
    private int bookId;
    private String title;
    private int authorId;
    private Date publicationDate;
    private double price;
    private double getPrice;
    private int categoryId;
    private int quantity;
    private String description;

    

    public Book(String title, int authorId, Date publicationDate, double price, double getPrice, int categoryId, int quantity, String description) {
        this.title = title;
        this.authorId = authorId;
        this.publicationDate = publicationDate;
        this.price = price;
        this.getPrice = getPrice;
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.description = description;
    }


    

    public int getBookId() { 
    	return bookId; 
    	}
    public void setBookId(int bookId) { 
    	this.bookId = bookId;
    	}

    
    public String getTitle() { 
    	return title; 
    	}
    public void setTitle(String title) { 
    	this.title = title;
    	}

    
    public int getAuthorId() { 
    	return authorId; 
    	}
    public void setAuthorId(int authorId) { 
    	this.authorId = authorId; 
    	}

    
    public Date getPublicationDate() { 
    	return publicationDate; 
    	}
    public void setPublicationDate(Date publicationDate) {
    	this.publicationDate = publicationDate; 
    	}

    
    public double getPrice() { 
    	return price; 
    	}
    public void setPrice(double price) { 
    	this.price = price; 
    	}

    
    public double getGetPrice() {
    	return getPrice; 
    	}
    public void setGetPrice(double getPrice) {
    	this.getPrice = getPrice; 
    	}

    
    public int getCategoryId() { 
    	return categoryId; 
    	}
    public void setCategoryId(int categoryId) {
    	this.categoryId = categoryId; 
    	}

    
    public int getQuantity() { 
    	return quantity;
    	}
    public void setQuantity(int quantity) { 
    	this.quantity = quantity; 
    	}

    
    public String getDescription() { 
    	return description; 
    	}
    public void setDescription(String description) { 
    	this.description = description; 
    	}
}
