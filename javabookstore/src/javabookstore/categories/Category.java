package javabookstore.categories;

public class Category {
	private int categoryid;
    private String categoryname;
    
    public Category(String categoryname ) {
    	//this.categoryid=categoryid;
    	this.categoryname=categoryname;
    }
    
    public String getcategoryname() {
    	return categoryname;
    }
    public void setcategoryname(String newcategory) {
    	this.categoryname=newcategory;
    }
    
   public int getcategoryid() {
	   return categoryid;
   }
   public void setcategoryid(int nwid) {
	   this.categoryid=nwid;
   }
}
