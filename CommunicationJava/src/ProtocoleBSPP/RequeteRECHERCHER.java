package ProtocoleBSPP;

import ServeurTCP.Requete;
import model.viewmodel.BookSearchVM;

public class RequeteRECHERCHER implements Requete{
    BookSearchVM bookSearchVM;

    public RequeteRECHERCHER(String auteur, String titre, String sujet , Float prix)
    {
        bookSearchVM = new BookSearchVM(auteur,titre,sujet,prix);
    }

    public BookSearchVM getBookSearchVM() {
        return bookSearchVM;
    }
    public void setBookSearchVM(BookSearchVM bookSearchVM) {
        this.bookSearchVM = bookSearchVM;
    }
}
