package model.viewmodel;

import java.io.Serializable;

public class AuthorSearchVM implements Serializable {
    private String nom;
    private String prenom;

    public AuthorSearchVM(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
