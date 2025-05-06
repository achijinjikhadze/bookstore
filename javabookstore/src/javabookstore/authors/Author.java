package javabookstore.authors;

import java.util.Date;

public class Author {
    private int authorid;
    private String firstname;
    private String lastname;
    private String bio;
    private Date birthdate;


    

    public Author(String firstname, String lastname, String bio, Date birthdate) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.bio = bio;
        this.birthdate = birthdate;
    }

    public int getAuthorid() { 
    	return authorid;
    	}
    public void setAuthorid(int authorid) {
    	this.authorid = authorid; 
    	}

    
    public String getFirstname() { 
    	return firstname; 
    	}
    public void setFirstname(String firstname) { 
    	this.firstname = firstname;
    	}

    
    public String getLastname() { 
    	return lastname; 
    	}
    public void setLastname(String lastname) { 
    	this.lastname = lastname; 
    	}

    
    public String getBio() { 
    	return bio;
    	}
    public void setBio(String bio) { 
    	this.bio = bio; 
    	}

    
    public Date getBirthdate() { 
    	return birthdate; 
    	}
    public void setBirthdate(Date birthdate) { 
    	this.birthdate = birthdate;
    	}
}
