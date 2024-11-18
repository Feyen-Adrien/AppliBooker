package ProtocoleBSPP;

import ServeurTCP.Reponse;
import model.entity.Book;

import java.util.ArrayList;

public class ReponseRECHERCHER implements Reponse {
    private ArrayList<Book> books;

    public ReponseRECHERCHER() {

    }

    public ArrayList<Book> getBooks() {
        return books;
    }
    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
}
