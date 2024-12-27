package ProtocoleBSPPS;

import ServeurTCP.Reponse;
import model.entity.Author;

import java.util.ArrayList;

public class ReponseGetAuteur implements Reponse {
    private ArrayList<Author> ListeAuteur;

    public ReponseGetAuteur() {

    }

    public  ArrayList<Author> getListeAuteur() {
        return ListeAuteur;
    }
    public void setListeAuteur(ArrayList<Author> listeAuteur) {
        this.ListeAuteur = listeAuteur;
    }
}
