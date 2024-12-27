package model.entity;

import java.io.Serializable;

public class Author implements Serializable {
    private Integer author_id;
    private String last_name;
    private String first_surname;
    private String author_birthdate;

    public Author(Integer id, String last_name, String first_surname, String birthdate) {
        this.author_id = id;
        this.last_name = last_name;
        this.first_surname = first_surname;
        this.author_birthdate = birthdate;
    }



    public Integer getId() {
        return author_id;
    }

    public String getLastName() {
        return last_name;
    }

    public String getFirstSurname() {
        return first_surname;
    }

    public String getBirthdate() {
        return author_birthdate;
    }

    public void setId(Integer id) {
        this.author_id = id;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public void setFirstSurname(String first_surname) {
        this.first_surname = first_surname;
    }

    public void setBirthdate(String birthdate) {
        this.author_birthdate = birthdate;
    }
}
