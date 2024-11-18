package src.Helpers.Entity;

public class Author {
    private String lastname;
    private String firstname;
    private String dateofbirth;

    public Author(String lastname, String firstname, String dateofbirth) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.dateofbirth = dateofbirth;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }


    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }
}
