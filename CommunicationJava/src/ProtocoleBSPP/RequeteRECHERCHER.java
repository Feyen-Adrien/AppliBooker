package ProtocoleBSPP;

import ServeurTCP.Requete;

public class RequeteRECHERCHER implements Requete{
    String Auteur;
    String Titre;
    String Sujet;
    int Prix;


    public int getPrix() {
        return Prix;
    }
    public void setPrix(int prix) {
        Prix = prix;
    }
    public String getAuteur() {
        return Auteur;
    }
    public void setAuteur(String auteur) {
        Auteur = auteur;
    }
    public String getTitre() {
        return Titre;
    }
    public void setTitre(String titre) {
        Titre = titre;
    }
    public String getSujet() {
        return Sujet;
    }
    public void setSujet(String sujet) {
        Sujet = sujet;
    }
}
