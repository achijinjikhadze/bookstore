package tables;

public class employee {

	  private int id;
	    private String firstname;
	    private String lastname;
	    private boolean isAdmin;
	    private String username;
	    private String password;

	    public employee(int id, String firstname, String lastname, boolean isAdmin, String username, String password) {
	        this.id = id;
	        this.firstname = firstname;
	        this.lastname = lastname;
	        this.isAdmin = isAdmin;
	        this.username = username;
	        this.password = password;
	    }

	    public int getId() {
	        return id;
	    }

	    public String getFirstname() {
	        return firstname;
	    }

	    public String getLastname() {
	        return lastname;
	    }

	    public boolean isAdmin() {
	        return isAdmin;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public String getPassword() {
	        return password;
	    }


  public String getFullName() {
      return firstname + " " + lastname;
  }
}
