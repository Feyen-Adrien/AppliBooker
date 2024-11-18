package ProtocoleBSPP;

import ServeurTCP.Reponse;
import model.entity.Author;
import model.entity.Book;

import java.util.ArrayList;

public class ReponseGetBooks implements Reponse {
    private ArrayList<Book> listeLivres;

    public ReponseGetBooks() {

    }

    public  ArrayList<Book> getListeLivres() {
        return listeLivres;
    }
    public void setListeLivres(ArrayList<Book> listeLivres) {
        this.listeLivres = listeLivres;
    }
}
